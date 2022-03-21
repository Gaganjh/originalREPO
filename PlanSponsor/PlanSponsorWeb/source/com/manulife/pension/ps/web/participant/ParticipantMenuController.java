
package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWParticipantMenu;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.util.log.LogUtility;

/**
 * Participant Menu Action class 
 * This class is used to forward the users's request to 
 * the Participant Menu page
 * 
 * @author Simona Stoicescu
 */
@Controller
@RequestMapping(value ="/participant")

public class ParticipantMenuController extends PsController 
{
	
	
	private static final String PARTICIPANT_MENU_PAGE = "participantMenu";
	
	public ParticipantMenuController()
	{
		super(ParticipantMenuController.class);
	} 
	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm()
	{ 
		return 	new DynaForm();
		}

	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		
		forwards.put("participantMenu","/participant/participants.jsp"); 
	}
	
	
	
	
	@RequestMapping(value ="/participantMenu", method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		request.setAttribute("allowedToAccessEligibTab", true);
                request.setAttribute("allowedToAccessDeferralTab", true);
    	        request.setAttribute("allowedToAccessVestingTab", true);
    	        request.setAttribute("aciOn", false);
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return  forwards.get(PARTICIPANT_MENU_PAGE);//if input forward not //available, provided default
        	}
        }

	
		if ( logger.isDebugEnabled() )
			logger.debug(ParticipantMenuController.class.getName()+":forwarding to Participant Menu Page.");
        
		// set permission flag for auto enrollment
        UserProfile userProfile = getUserProfile(request);
        int contractId = userProfile.getCurrentContract().getContractNumber();
        boolean isEnabled = false;
        try {
            isEnabled = userProfile.isInternalUser() || CensusUtils.isAutoEnrollmentEnabled(contractId);
            request.setAttribute("allowedToAccessEligibTab", isEnabled);
            boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
            request.setAttribute("allowedToAccessDeferralTab", allowedToAccessDeferrals);
            
            isEnabled = CensusUtils.isVestingEnabled(contractId) && 
                        !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                        TODO &&
                        userProfile.getCurrentContract().isContractAllocated()*/;
            request.setAttribute("allowedToAccessVestingTab", isEnabled);
            
            // since we don't have a form for this page, this logic is placed here.
            
            
            // since we don't have a form for this page, this logic is placed here.
            ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
            // Logic for Auto/ SignUp
            Boolean aciOn = Boolean.FALSE;
            
            String autoSignupValue = delegate.determineSignUpMethod(contractId);
            
            if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignupValue) ||
            		ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignupValue)){
            	aciOn = Boolean.TRUE;
            }
            
            request.setAttribute("aciOn", aciOn);
            
            @SuppressWarnings("unchecked")
			boolean allowedToAccessPayrollSelfService = Boolean.TRUE.equals(ContractServiceFeatureUtil.hasContractServiceFeature(
					delegate
					//.getContractServiceFeatures(Integer.parseInt(getContractNumber()), Collections.singletonList(ServiceFeatureConstants.PAYROLL_SELF_SERVICE)), 
					.getContractServiceFeatures(contractId),
				ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE,
				new HashSet<>(Collections.singletonList(ServiceFeatureConstants.YES))
				));

            request.setAttribute("allowedToAccessPayrollSelfServiceTab", allowedToAccessPayrollSelfService);
            
        } catch (ApplicationException | SystemException exception) {
        	SystemException loggedException = exception instanceof SystemException ? (SystemException) exception 
        			: new SystemException(exception, exception.getMessage());
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID, loggedException);

            request.setAttribute("errorCode", "1099");
            request.setAttribute("uniqueErrorId", loggedException.getUniqueId());

            // forward to Error Page
            return forwards.get(SYSTEM_ERROR_PAGE);
        }
		return forwards.get(PARTICIPANT_MENU_PAGE);
	}
	
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	   private PSValidatorFWParticipantMenu  psValidatorFWParticipantMenu;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWParticipantMenu);
	}
	
}
