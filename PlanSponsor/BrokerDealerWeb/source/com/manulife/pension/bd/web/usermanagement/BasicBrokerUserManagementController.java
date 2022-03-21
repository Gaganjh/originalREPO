package com.manulife.pension.bd.web.usermanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.firmsearch.FirmSearchUtils;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWView;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBasicBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for Basic Broker user management
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value = "/manage")
@SessionAttributes({ "manageBasicBrokerForm" })

public class BasicBrokerUserManagementController extends AbstractUserManagementController {
	@ModelAttribute("manageBasicBrokerForm")
	public BasicBrokerUserManagementForm populateForm() {
		return new BasicBrokerUserManagementForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("exception", "redirect:/do/usermanagement/search?task=exception");
		forwards.put("back", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("search", "redirect:/do/usermanagement/search");
		forwards.put("view", "/usermanagement/basicBroker.jsp");
		forwards.put("resendSuccess", "redirect:/do/manage/basicBroker?resendSuccess=y");
		forwards.put("resetPassword", "redirect:/do/usermanagement/resetPassword");
		forwards.put("passcodeView", "redirect:/do/usermanagement/passcodeView");
		forwards.put("exemptPasscode", "redirect:/do/usermanagement/passcodeExemption");
	}

	private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
			BDErrorCodes.PERSONAL_INFO_INVALID_VALUE, BDRuleConstants.INVALID_VALUE_WITH_EMPTY_RE);

	private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

	private static final String COMPANY_NAME_LABEL = "Company Name";

	/**
	 * Constructor
	 */
	public BasicBrokerUserManagementController() {
		super(BasicBrokerUserManagementController.class);
	}

	/**
	 * This is the override method for the method in BaseAutoAction
	 * 
	 * @param mapping
	 *            The action mapping.
	 * @param actionForm
	 *            The action form.
	 * @param request
	 *            The HTTP request.
	 * @param response
	 *            The HTTP response.
	 * @return ActionForward The forward to process.
	 * @throws IOException
	 *             When an IO problem occurs.
	 * @throws ServletException
	 *             When an Servlet problem occurs.
	 * @throws SystemException
	 *             When an generic application problem occurs.
	 */

	@RequestMapping(value = "/basicBroker", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		} else {
			return forwards.get(View);
		}
	}

	/**
	 * Save the update
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = "/basicBroker", params = { "action=resendActivation" }, method = { RequestMethod.POST })
	public String doResendActivation(
			@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}
		if (!setupForm(request, actionForm, false)) {
			return forwards.get(Back);
		}
		BasicBrokerUserManagementForm form = (BasicBrokerUserManagementForm) actionForm;

		if (!form.isResendInvitationAllowed()) {
			return forwards.get(Back);
		}

		try {
			BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
			long userProfileId = form.getBasicBrokerUserProfile().getProfileId();
			BDUserManagementServiceDelegate.getInstance().resendBasicBrokerActivation(principal, userProfileId);
			// update the current user information
			UserManagementContext.setCurrentUser(request, principal, userProfileId);
			return forwards.get(ResendSuccess);
		} catch (SecurityServiceException e) {
			if (UserManagementHelper.isManagementConflictException(e)) {
				return handleSecurityServiceException(request, e);
			}
			List<ValidationError> errors = new ArrayList<ValidationError>(1);
			errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			setErrorsInRequest(request, errors);
		}
		return forwards.get(View);
	}

	/**
	 * Save the update
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */

	@RequestMapping(value = "/basicBroker", params = { "action=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}
		if (!setupForm(request, actionForm, true)) {
			return forwards.get(Back);
		}
		BasicBrokerUserManagementForm form = (BasicBrokerUserManagementForm) actionForm;

		form.setRiafirms(BDWebCommonUtils.getRiaFirmsWithPermission(form.getRiaFirmListStr(),
				form.getRiaFirmPermissionsListStr()));

		if (!form.isUpdateAllowed()) {
			return forwards.get(Back);
		}
		String companyName = "";
		Long firmId = null;
		List<ValidationError> errors = new ArrayList<ValidationError>(1);
		if (StringUtils.isNotEmpty(form.getRiaFirmPermissionsListStr())) {
			String permissionCheck = form.getRiaFirmPermissionsListStr();
			String[] buf = permissionCheck.split(",");
			if (buf.length > 20) {
				errors.add(new ValidationError(BasicBrokerUserManagementForm.FIELD_FIRMS,
						BDErrorCodes.USER_MANAGEMENT_VIEW_RIA_FIRM_PERMISSION_EXCEEDED_BROKER));
			}
		}
		if (StringUtils.equals(BasicBrokerUserManagementForm.INDEPENDENT_TYPE, form.getPartyType())) {
			companyName = form.getCompanyName();
			// clear the firm name
			form.setFirmId("");
			form.setFirmName("");
			boolean isValid = invalidValueRErule.validate(BasicBrokerUserManagementForm.FIELD_COMPANY_NAME,
					tempArrayList, companyName);
			if (!isValid) {
				errors.add(new ValidationError(BasicBrokerUserManagementForm.FIELD_COMPANY_NAME,
						BDErrorCodes.PERSONAL_INFO_INVALID_VALUE, new Object[] { COMPANY_NAME_LABEL }));
			}
		} else {
			form.setFirmId(FirmSearchUtils.getFirmPartyId(form.getFirmName()));
			form.setSelectedRiaFirmId(FirmSearchUtils.getFirmPartyId(form.getFirmName()));

			form.setCompanyName("");
			if (!StringUtils.isEmpty(form.getFirmId())) {
				firmId = Long.parseLong(form.getFirmId());
			}
			if (firmId == null) {
				// show the same error message for the blank firm name and
				// invalid firm name.
				errors.add(new ValidationError("firmName", BDErrorCodes.USER_MANAGEMENT_INVALID_BD_FIRM));
			}
		}

		if (errors.isEmpty()) {
			try {
				BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
				long userProfileId = form.getBasicBrokerUserProfile().getProfileId();
				BDUserManagementServiceDelegate.getInstance().updateBasicBrokerFirmInfo(principal, userProfileId,
						companyName, firmId);
				BDUserManagementServiceDelegate.getInstance().updateRiaFirmDetails(principal, userProfileId,
						form.getRiafirms());
				// update the current user information
				UserManagementContext.setCurrentUser(request, principal, userProfileId);

				form.setUpdateSuccess(true);
				form.setChanged(false);
			} catch (SecurityServiceException e) {
				if (UserManagementHelper.isManagementConflictException(e)) {
					return handleSecurityServiceException(request, e);
				}

				errors.add(new ValidationError("", SecurityServiceExceptionHelper.getErrorCode(e)));
			}
		}
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			form.setChanged(true);
			form.setUpdateSuccess(false);
		}
		return forwards.get(View);
	}

	@RequestMapping(value = "/basicBroker", params = { "action=mimic" }, method = { RequestMethod.POST })
	public String doMimic(@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		String forward = doMimic(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/basicBroker", params = { "action=exemptPasscode" }, method = { RequestMethod.POST })
	public String doExemptPasscode(
			@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		return forwards.get(doExemptPasscode(actionForm, request, response));
	}

	@RequestMapping(value = "/basicBroker", params = { "action=passcodeView" }, method = { RequestMethod.POST })
	public String doPasscodeView(
			@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		return forwards.get(doPasscodeView(actionForm, request, response));
	}

	@RequestMapping(value = "/basicBroker", params = { "action=resetPassword" }, method = { RequestMethod.POST })
	public String doResetPassword(
			@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		return forwards.get(doResetPassword(actionForm, request, response));
	}

	@RequestMapping(value = "/basicBroker", params = { "action=delete" }, method = { RequestMethod.POST })
	public String doDelete(@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		return forwards.get(doDelete(actionForm, request, response));
	}

	@RequestMapping(value = "/basicBroker", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(@Valid @ModelAttribute("manageBasicBrokerForm") BasicBrokerUserManagementForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("view");// if input forward not //available,
											// provided default
			}
		}

		String forward = doCancel(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This method sets the user information in the form object
	 * 
	 * @param request
	 * @param actionForm
	 * @return boolean
	 */
	protected boolean setupForm(HttpServletRequest request, ActionForm actionForm, boolean isUpdate) {
		ExtendedBDExtUserProfile managedUserProfile = UserManagementContext.getContext(request).getManagedUserProfile();

		if (managedUserProfile == null
				|| managedUserProfile.getRoleType().compareTo(BDUserRoleType.BasicFinancialRep) != 0) {
			// this should not happen unless using book mark
			logger.error("The selected user is not a Basic Broker User, userProfileId = "
					+ (managedUserProfile == null ? "" : managedUserProfile.getProfileId()));
			return false;
		}
		BasicBrokerUserManagementForm basicBrokerForm = (BasicBrokerUserManagementForm) actionForm;
		ExtendedBasicBrokerUserProfile basicBrokerUserProfile = (ExtendedBasicBrokerUserProfile) managedUserProfile;
		basicBrokerForm.setBasicBrokerUserProfile(basicBrokerUserProfile);
		if (!isUpdate) {
			BrokerDealerFirm firm = basicBrokerUserProfile.getBrokerDealerFirm();
			if (firm == null) {
				basicBrokerForm.setPartyType(BDConstants.INDEPENDENT_PARTY);
				basicBrokerForm.setCompanyName(basicBrokerUserProfile.getCompanyName());
			} else {
				basicBrokerForm.setPartyType(BDConstants.BD_FIRM_PARTY);
				basicBrokerForm.setFirmId(String.valueOf(basicBrokerUserProfile.getBrokerDealerFirm().getId()));
				basicBrokerForm.setFirmName(basicBrokerUserProfile.getBrokerDealerFirm().getFirmName());
			}
		}
		basicBrokerForm.setInternalUserRole(request);
		basicBrokerForm.setRiafirms(((ExtendedBasicBrokerUserProfile) managedUserProfile).getRiaFirms());
		UserManagementHelper.sortFirmsById(basicBrokerForm.getRiafirms());
		if (StringUtils.isNotEmpty(request.getParameter("resendSuccess"))) {
			basicBrokerForm.setEnableResend(false);
			basicBrokerForm.setResendActivationSuccess(true);
		}
		return true;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */

	@Autowired
	private BDValidatorFWView bdValidatorFWView;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWView);
	}

}
