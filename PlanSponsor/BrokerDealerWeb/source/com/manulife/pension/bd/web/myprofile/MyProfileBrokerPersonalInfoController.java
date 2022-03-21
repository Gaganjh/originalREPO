package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.navigation.UserNavigationFactory;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for MyProfile Level2 Broker Personal Info tab
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping(value ="/myprofile")
@SessionAttributes({"myprofileBrokerPersonalInfoForm"})

public class MyProfileBrokerPersonalInfoController extends BaseAutoController {
	@ModelAttribute("myprofileBrokerPersonalInfoForm")
	public MyProfileBrokerPersonalInfoForm populateForm() {
		return new MyProfileBrokerPersonalInfoForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/myprofile/brokerPersonalInfo.jsp");
		forwards.put("cancel", "redirect:/do/home/");
		forwards.put("fail", "/myprofile/brokerPersonalInfo.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

	private static final String CANCEL_FORWARD = "cancel";

	private final MandatoryRule profileFirstNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_PROFILE_FIRST_NAME);

	private final MandatoryRule profileLastNameMandatoryRule = new MandatoryRule(
			BDErrorCodes.MISSING_PROFILE_LAST_NAME);

	private static final RegularExpressionRule invalidValueRErule = new RegularExpressionRule(
			BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

	private final List<ValidationError> tempArrayList = new ArrayList<ValidationError>();

	private static final String PROFILE_FIRST_NAME_LABEL = "Profile First Name";

	private static final String PROFILE_LAST_NAME_LABEL = "Profile Last Name";

	/**
	 * Constructor
	 */
	public MyProfileBrokerPersonalInfoController() {
		super(MyProfileBrokerPersonalInfoController.class);
	}

    /**
     * This method will be called if the action parameter is default or null. This will retrieve the
     * broker personal information from the database.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When an Servlet problem occurs.
     * @throws SystemException When an generic application problem occurs.
     */ 

    @RequestMapping(value = "/brokerPersonalInfo", method =  {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("myprofileBrokerPersonalInfoForm") MyProfileBrokerPersonalInfoForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("fail");
    		}
    	}
    	
    	//if user is bookmarked the URL, we still need to challenge.
    	if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
    		if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
    			request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.BrokerPersonalInfoTabId);
    			return forwards.get("myprofileDispatch");
    		}
    	}
    	
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
        try {
          
            BDPrincipal user = BDSessionHelper.getUserProfile(request).getBDPrincipal();
            ExtendedBDExtUserProfile profile = BDUserSecurityServiceDelegate.getInstance()
                    .getExtendedBDExtUserProfile(user);
            if (profile.getRoleType().compareTo(BDUserRoleType.FinancialRep) == 0) {
                ExtendedBrokerUserProfile brokerUserProfile = (ExtendedBrokerUserProfile) profile;
                List<WebBrokerEntityProfile> brokerEntityProfilesList = WebBrokerEntityProfileHelper
                        .createWebBrokerEntityList(brokerUserProfile);
                form.setProfileFirstName(brokerUserProfile.getFirstName());
                form.setProfileLastName(brokerUserProfile.getLastName());
                form.setPrimaryBrokerPartyId(brokerUserProfile
                        .getPrimaryBrokerEntityProfile().getId());
                List<WebBrokerEntityProfile> sortedList = WebBrokerEntityProfileHelper
                        .sortBrokerEntityProfiles(brokerEntityProfilesList);
                form.setBrokerEntityProfilesList(sortedList);
                form
                        .setLastUpdatedBrokerEntityProfilesList(WebBrokerEntityProfileHelper
                                .copyBrokerEntityProfilesList(sortedList));
                form.setAddressFlagMap(WebBrokerEntityProfileHelper
                        .getAddressFlagMap(brokerEntityProfilesList));
                // we have to reset these flags since the form is in
                // session
                form.setChanged(false);
                form.setSuccess(false);
            }
        } catch (SecurityServiceException sse) {
            logger.debug("Retrieving Broker Personal Info Failed. ", sse);
            List<ValidationError> errors = new ArrayList<ValidationError>(1);
            errors.add(new ValidationError("", SecurityServiceExceptionHelper
                    .getErrorCode(sse)));
            setErrorsInRequest(request, errors);
        }
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.BrokerPersonalInfoTabId);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault");
        }
        return forwards.get("input");
    }

    

    /**
     * This method will be called if the action parameter is save. This will save the broker
     * personal info to database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    @RequestMapping(value = "/brokerPersonalInfo",params= {"action=save"}, method =  {RequestMethod.POST})
  	public String doSave(@Valid @ModelAttribute("myprofileBrokerPersonalInfoForm") MyProfileBrokerPersonalInfoForm form,BindingResult bindingResult,
  			HttpServletRequest request, HttpServletResponse response)
  					throws IOException, ServletException, SystemException {
		
	if(bindingResult.hasErrors()){
		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		if(errDirect!=null){
			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return forwards.get("fail");//if input forward not //available, provided default
		}
	}
   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSave");
        }
        MyProfileBrokerPersonalInfoForm personalInfoForm = (MyProfileBrokerPersonalInfoForm) form;
        List<ValidationError> errors = validate(personalInfoForm, request);
        if (errors.size() == 0) {
            try {
                BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();

                BDUserSecurityServiceDelegate.getInstance().updateBrokerPersonalInfo(
                        principal,
                        personalInfoForm.getProfileFirstName(),
                        personalInfoForm.getProfileLastName(),
                        WebBrokerEntityProfileHelper.getBrokerExtendedProfile(personalInfoForm
                                .getBrokerEntityProfilesList()),
                        WebBrokerEntityProfileHelper.getBrokerExtendedProfile(personalInfoForm
                                .getLastUpdatedBrokerEntityProfilesList()),
                        personalInfoForm.getPrimaryBrokerPartyId(), IPAddressUtils.getRemoteIpAddress(request));
                principal.setFirstName(personalInfoForm.getProfileFirstName());
                principal.setLastName(personalInfoForm.getProfileLastName());
                ServletContext context = request.getServletContext();
                UserNavigationFactory.getInstance(context).updateNavigation(request, context);
                personalInfoForm
                        .setLastUpdatedBrokerEntityProfilesList(WebBrokerEntityProfileHelper
                                .copyBrokerEntityProfilesList(personalInfoForm
                                        .getBrokerEntityProfilesList()));
            } catch (SecurityServiceException e) {
                logger.debug("Fail to update the personal information", e);
                errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCode(
												e,
												MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
            }
        }
        if (errors.size() > 0) {
            setErrorsInSession(request, errors);
            personalInfoForm.setChanged(true);
            personalInfoForm.setSuccess(false);
        } else {
            personalInfoForm.setChanged(false);
            personalInfoForm.setSuccess(true);
        }
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.BrokerPersonalInfoTabId);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSave");
        }
        return forwards.get("input");
    }

    /**
     * This method will be called when the action parameter is cancel. This will forward the user to
     * secure home page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value = "/brokerPersonalInfo", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("myprofileBrokerPersonalInfoForm") MyProfileBrokerPersonalInfoForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");//if input forward not //available, provided default
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}
		MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.BrokerPersonalInfoTabId);
		return forwards.get(CANCEL_FORWARD);
	}

    /**
     * The validation methods for the form
     * 
     * @param form
     * @param request
     * @return List<ValidationError>
     * @throws SystemException
     */
    private List<ValidationError> validate(MyProfileBrokerPersonalInfoForm form,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validate");
        }
        List<ValidationError> errors = new ArrayList<ValidationError>();
        List<WebBrokerEntityProfile> brokerEntityProfilesList = form.getBrokerEntityProfilesList();
        boolean isValid = false;
        isValid = profileFirstNameMandatoryRule.validate(
                MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_FIRST_NAME, errors, form
                        .getProfileFirstName());
        if (isValid) {
            isValid = invalidValueRErule.validate(
                    MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_FIRST_NAME, tempArrayList, form
                            .getProfileFirstName());
            if (!isValid) {
                errors.add(new ValidationError(
                        MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_FIRST_NAME,
                        BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] {PROFILE_FIRST_NAME_LABEL }));
            }
        }
        isValid = profileLastNameMandatoryRule.validate(
                MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_LAST_NAME, errors, form
                        .getProfileLastName());
        if (isValid) {
            isValid = invalidValueRErule.validate(
                    MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_LAST_NAME, tempArrayList, form
                            .getProfileLastName());
            if (!isValid) {
                errors.add(new ValidationError(
                        MyProfileBrokerPersonalInfoForm.FIELD_PROFILE_LAST_NAME,
                        BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, new Object[] { PROFILE_LAST_NAME_LABEL }));
            }
        }
        WebBrokerEntityProfileHelper.validateBrokerEntityProfile(errors, brokerEntityProfilesList, form.getAddressFlagMap());
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validate");
        }
        return errors;
	}

	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
  
    
}
