package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

@Controller
@RequestMapping(value = "/phoneCollection")
public class PhoneCollectionController extends BaseController {
    public PhoneCollectionController() {
        super(PhoneCollectionController.class);
    }

    @ModelAttribute("psDynForm")
    public DynaForm populateForm() {
        return new DynaForm();
    }

    private static final String HOME_PAGE_FINDER = "homePageFinder";
    private static final String EDIT_MY_PROFILE = "editMyProfile";
    
    private static Logger logger = Logger.getLogger(PhoneCollectionController.class);
    public static HashMap<String, String> forwards = new HashMap<String, String>();
    
    static {
        forwards.put(HOME_PAGE_FINDER, "redirect:/do/home/homePageFinder/");
        forwards.put(Constants.PHONE_COLLECTION, "/home/deliveryPreferenceOverlay.jsp");
        forwards.put(EDIT_MY_PROFILE, "/do/profiles/editMyProfile/");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String doDefault(@Valid @ModelAttribute("psDynForm") DynaForm psDynForm,
            BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SystemException {
        if (bindingResult.hasErrors()) {
            String errDirect = (String) request.getSession()
                    .getAttribute(CommonConstants.ERROR_RDRCT);
            if (errDirect != null) {
                request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
                return forwards.get(errDirect) != null ? forwards.get(errDirect)
                        : forwards.get(Constants.PHONE_COLLECTION);
            }
        }
        UserProfile userProfile = getUserProfile(request);

        return verifyAndFindForward(request, userProfile);

    }

	private String verifyAndFindForward(HttpServletRequest request, UserProfile userProfile) throws SystemException {
        
        String forwardToPage = Constants.PHONE_COLLECTION;
        final HttpSession session = request.getSession(false);
        
        Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.USER_PROFILE_LOCK_NAME, LockHelper.USER_PROFILE_LOCK_NAME + userProfile.getPrincipal().getProfileId());

		if ((lockInfo != null && userProfile.getPrincipal().getProfileId() != lockInfo.getLockUserProfileId() && !lockInfo.isExpired())
				|| SecurityConstants.RESET_PASSWORD_STATUS.equals(userProfile.getPasswordStatus())) {
			if (session != null && session.getAttribute(Constants.PHONE_COLLECTION) != null) {
				session.removeAttribute(Constants.PHONE_COLLECTION);
			}

			if (session != null && session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE) != null) {
				session.removeAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE);
			}
			
			logSkipPhoneCollection(userProfile,
					lockInfo != null ? String.valueOf(lockInfo.getLockUserProfileId()) : "Not Locked",
					"verifyAndFindForward");

			return forwards.get(HOME_PAGE_FINDER);
		}

        if (session != null && session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE) != null) {
            List<GenericException> errors = new ArrayList<>();
            if ((boolean) session.getAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE)) {
                errors.add(new GenericException(ErrorCodes.ERROR_PHONE_COLLECTION_EDIT_MY_PROFILE));
                request.setAttribute("loginFlow", "Y");
                forwardToPage = EDIT_MY_PROFILE;
            } else {
                errors.add(new GenericException(ErrorCodes.ERROR_PHONE_COLLECTION_OERLAY));
            }
            setErrorsInRequest(request, errors);
            return forwards.get(forwardToPage);
        }
		if (session != null) {
			session.setAttribute(Constants.PHONE_COLLECTION_EDIT_MY_PROFILE, false);
		}
		return forwards.get(forwardToPage);
    }

    public static UserProfile getUserProfile(final HttpServletRequest request) {
        return SessionHelper.getUserProfile(request);
    }
    
	private void logSkipPhoneCollection(UserProfile userProfile, String lockedBy, String loggingPoint) {
		ServiceLogRecord record = new ServiceLogRecord("PhoneCollectionController");

		StringBuilder logData = new StringBuilder();
		logData.append("Important Security Update page of security code prefence collection is skipped for userProfile:"
				+ userProfile.getPrincipal().getProfileId() + ",");
		logData.append("Password status:" + userProfile.getPasswordStatus() + "],");
		logData.append("Locked by:" + lockedBy + "]");
		
		record.setMethodName(loggingPoint);
		record.setData(logData.toString());
		record.setDate(new Date());
		record.setUserIdentity(String.valueOf(userProfile.getPrincipal().getProfileId()));

		logger.error(record);
	}

    @Autowired
    private PSValidatorFWInput psValidatorFWInput;

    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.bind(request);
        binder.addValidators(psValidatorFWInput);
    }

}
