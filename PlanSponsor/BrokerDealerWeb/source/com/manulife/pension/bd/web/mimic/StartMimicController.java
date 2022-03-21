package com.manulife.pension.bd.web.mimic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.login.UserCookie;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileFactory;
import com.manulife.pension.bd.web.userprofile.LoginStatus;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.security.bd.exception.BDBrokerNoActiveAssociatedPartyException;
import com.manulife.pension.service.security.bd.exception.BDInvalidFirmRepUserException;
import com.manulife.pension.service.security.bd.exception.BDParentBrokerNoActiveAssociatedPartyException;
import com.manulife.pension.service.security.bd.exception.BDSecurityOperationNotAllowedException;
import com.manulife.pension.service.security.bd.valueobject.BDMimicUserSecurityProfile;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;
import com.manulife.pension.service.security.exception.UserNotRegisteredException;
import com.manulife.pension.validator.ValidationError;

/**
 * Start the mimic session. If the mimic fails, it will return to UserManage
 * (search) page
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/mimic")

public class StartMimicController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static Map<String, Integer> localSecurityServiceExceptionMapping = new HashMap<String, Integer>(11);
	
	static {
		forwards.put("fail", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("success", "redirect:/do/home/");
		
		localSecurityServiceExceptionMapping.put(UserNotRegisteredException.class.getName(),
				BDErrorCodes.MIMIC_NOT_ALLOWED);
		localSecurityServiceExceptionMapping.put(BDInvalidFirmRepUserException.class.getName(),
				BDErrorCodes.MIMIC_NOT_ALLOWED);
		localSecurityServiceExceptionMapping.put(BDBrokerNoActiveAssociatedPartyException.class.getName(),
				BDErrorCodes.MIMIC_NOT_ALLOWED);
		localSecurityServiceExceptionMapping.put(BDParentBrokerNoActiveAssociatedPartyException.class.getName(),
				BDErrorCodes.MIMIC_NOT_ALLOWED);
		localSecurityServiceExceptionMapping.put(BDSecurityOperationNotAllowedException.class.getName(),
				BDErrorCodes.USER_SEARCH_INSUFFICIENT_ACCESS);
		localSecurityServiceExceptionMapping.put(UserNotFoundException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);
		localSecurityServiceExceptionMapping.put(DisabledUserException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);

	}

	public static final String PARAM_USER_PROFILE_ID = "userProfileId";

	public static final String FORWARD_SUCCESS = "success";

	public static final String FORWARD_FAIL = "fail";

	private MimicSession mimicSession = new MimicSession();

	public StartMimicController() {
		super(StartMimicController.class);
	}

	@RequestMapping(value = "/start", method = { RequestMethod.POST,RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");
			}
		}
		Long profileId = null;
		try {
			profileId = BDWebCommonUtils.getRequestParameterAsLong(request, PARAM_USER_PROFILE_ID);
			if(profileId==null){				
				profileId = (Long)request.getAttribute(PARAM_USER_PROFILE_ID);				
			}
			BDMimicUserSecurityProfile mimickedSecurityProfile = BDUserManagementServiceDelegate.getInstance()
					.mimicExternalUserByProfileId(BDSessionHelper.getUserProfile(request).getBDPrincipal(), profileId);
			BDUserProfile mimickedUser = BDUserProfileFactory.getInstance()
					.getMimickedUserProfile(mimickedSecurityProfile);
			mimickedUser.setLoginStatus(LoginStatus.FullyLogin);

			BDUserProfile currentUser = BDSessionHelper.getUserProfile(request);
			if (mimickedUser.startMimic(currentUser)) {
				// clean up the session
				mimicSession.createMimicSession(request, currentUser, mimickedUser);
				// We now update the UserCookie to set any values that are
				// allowed to be mimicked.
				// Currently only NFA code, which must be updated to the
				// mimicked user.
				try {
					UserCookie cookie = new UserCookie(currentUser, mimickedUser);
					response.addCookie(cookie);
				} catch (Exception e) {
					logger.error("Error setting UserCookie to mimic user - Advisor will not see mimicked content! - ",
							e);
				}
				return forwards.get(FORWARD_SUCCESS);
			}
		} catch (NumberFormatException e) {
			logger.error("parameter not a long", e);
			return forwards.get(FORWARD_FAIL);
		} catch (SecurityServiceException e) {
			logger.debug("Fail to mimic user", e);
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("",
					SecurityServiceExceptionHelper.getErrorCode(e, localSecurityServiceExceptionMapping)));
			setErrorsInSession(request, errors);
		}
		return forwards.get(FORWARD_FAIL);
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
}
