package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDBrokerUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.SecurityServiceExceptionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.delegate.BDUserManagementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.generic.MandatoryRule;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantCreationValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping( value ="/myprofile")

public class AddBrokerAssistantController extends BDController {
	@ModelAttribute("addBrokerAssistantForm") 
	public AddBrokerAssistantForm populateForm() 
	{
		return new AddBrokerAssistantForm();
		}
	
	
	@ModelAttribute("manageBrokerAssistantForm")
	public ManageBrokerAssistantForm manageBrokerAssistantForm() {
		return new ManageBrokerAssistantForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/myprofile/assistants.jsp");
		forwards.put("success","redirect:/do/myprofile/assistants");
		forwards.put("fail","/myprofile/assistants.jsp");
		}

	private final MandatoryRule firstNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_FIRST_NAME);

    private final MandatoryRule lastNameMandatoryRule = new MandatoryRule(
            BDErrorCodes.MISSING_LAST_NAME);
    
    private static final RegularExpressionRule invalidFirstAndLastNameValueRErule = new RegularExpressionRule(
            BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME, BDRuleConstants.VALIDATE_FIRST_NAME_AND_LAST_NAME_RE);

    private static final String FIRST_NAME_LABEL = "First Name";
    
    private static final String LAST_NAME_LABEL = "Last Name";
    
    
    public AddBrokerAssistantController() {
        super(AddBrokerAssistantController.class);
    }

    @RequestMapping(value ="/addAssistant", method =  {RequestMethod.GET,RequestMethod.POST})
   	public String doExecute(@Valid @ModelAttribute("addBrokerAssistantForm") AddBrokerAssistantForm actionForm,BindingResult bindingResult,
   			HttpServletRequest request, HttpServletResponse response)
   					throws IOException, ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
       		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       		if(errDirect!=null){
       			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
       		return	forwards.get("fail");//if input forward not //available, provided default
       		}
       	}
        MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(),
                request);
        context.getNavigation().setCurrentTabId(MyProfileNavigation.AssistantsTabId);

       
        actionForm.setShow(true);
        List<ValidationError> errors = new ArrayList<ValidationError>();
        errors.addAll(validate(actionForm));
        if (errors.isEmpty()) {
            BrokerAssistantCreationValueObject vo = new BrokerAssistantCreationValueObject();
            vo.setFirstName(actionForm.getFirstName());
            vo.setLastName(actionForm.getLastName());
            vo.setEmailAddress(actionForm.getEmail());
            BDPrincipal brokerUser = BDSessionHelper.getUserProfile(request).getBDPrincipal();
            vo.setParentUserProfileId(brokerUser.getProfileId());
            try {
                long profileId = BDUserManagementServiceDelegate.getInstance()
                        .createBrokerAssistant(brokerUser, vo,IPAddressUtils.getRemoteIpAddress(request));
                BDBrokerUserProfile broker = (BDBrokerUserProfile) BDSessionHelper
                        .getUserProfile(request);
                broker.addDisableAssistant(profileId);
            } catch (SecurityServiceException e) {
                errors
						.add(new ValidationError(
								"",
								SecurityServiceExceptionHelper
										.getErrorCode(
												e,
												MyProfileUtil.MyProfileSecurityServiceExceptionMapping)));
            }
        }
        MyProfileUtil.updateAssistantList(request);
        if (errors.isEmpty()) {
            request.getSession().setAttribute("successContent",
                    BDContentConstants.ADD_ASSISTANT_SUCCESS_TEXT);
            return forwards.get("success");
        } else {
            setErrorsInSession(request, errors);
            return forwards.get("input");
        }
    }

    /**
     * The validation methods for the form
     * 
     * @param form
     * @return
     * @throws SystemException
     */
    private List<ValidationError> validate(AddBrokerAssistantForm form) throws SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
        boolean isValid = false;
        isValid = firstNameMandatoryRule.validate(AddBrokerAssistantForm.FIELD_FIRST_NAME, errors,
                form.getFirstName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(AddBrokerAssistantForm.FIELD_FIRST_NAME,
                    tempArrayList, form.getFirstName());
            if (!isValid) {
                errors
                        .add(new ValidationError(AddBrokerAssistantForm.FIELD_FIRST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { FIRST_NAME_LABEL }));
            }
        }

        isValid = lastNameMandatoryRule.validate(AddBrokerAssistantForm.FIELD_LAST_NAME, errors,
                form.getLastName());
        if (isValid) {
            isValid = invalidFirstAndLastNameValueRErule.validate(AddBrokerAssistantForm.FIELD_LAST_NAME,
                    tempArrayList, form.getLastName());
            if (!isValid) {
                errors
                        .add(new ValidationError(AddBrokerAssistantForm.FIELD_LAST_NAME,
                                BDErrorCodes.INVALID_PROFILE_FIRST_AND_LAST_NAME,
                                new Object[] { LAST_NAME_LABEL }));
            }
        }
        EmailAddressRule.getInstance().validate(AddBrokerAssistantForm.FIELD_EMAIL, errors,
                form.getEmail());
        if (!form.getTerm()) {
            errors.add(new ValidationError(AddBrokerAssistantForm.FIELD_TERM,
                    BDErrorCodes.ASSISTANT_CREATION_MISSING_TERM));
        }
        return errors;
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    @Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
/*	
	 * avoids token generation as this class acts as intermediate for many
	 * transactions(non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired
	 * (java.lang.String)
	 
	protected boolean isTokenRequired(String action) {
		return true;
	}

	
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ezk.web.BaseAction#isTokenValidatorEnabled(java
	 * .lang.String)
	 
	@Override
	public boolean isTokenValidatorEnabled(String actionName) {
		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(actionName)
				&& (StringUtils.equalsIgnoreCase(actionName, "Save") || StringUtils.equalsIgnoreCase(actionName, "Cancel"))?true:false;
	}*/
}
