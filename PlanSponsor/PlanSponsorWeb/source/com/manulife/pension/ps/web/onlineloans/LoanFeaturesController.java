package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.contract.PlanDataUi;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;


@Controller
@RequestMapping(value ="/onlineloans")
@SessionAttributes({"loanFeaturesForm"})

/**
 *
 * Action that handles loan features informations on 
 * loan features at a glance screen.
 * 
 * @author Ranjith
 */
public class LoanFeaturesController extends PsController {
	
	@ModelAttribute("loanFeaturesForm")
	public LoanFeaturesForm populateForm()
	{
		return new LoanFeaturesForm();
	}
	
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	
	static{
			forwards.put("default","/onlineloans/loanfeatures.jsp");
	}

	/**
	 * This is a static reference to the logger.
	 */

	public static final String ACTION_FORWARD_DEFAULT = "default";

	public LoanFeaturesController() {
		super(LoanFeaturesController.class);
	}

	
	@RequestMapping(value ="/features/", params={"task=print"},  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("loanFeaturesForm") LoanFeaturesForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		// gets the contractId from user profile
		Contract currentContract = getUserProfile(request).getCurrentContract();
		Contract contract=currentContract;
		
		final Integer contractId = currentContract.getContractNumber();
		
		if (contract == null || !isAllowedPageAccess(getUserProfile(request), contract)) {
            return forwards.get("homePageFinder");
        }
		
		LoanServiceDelegate delegate = LoanServiceDelegate.getInstance();
		LoanSettings loanSettings = delegate.getLoanSettings(Integer
				.valueOf(contractId));
		if (!(loanSettings.isLrk01() && loanSettings.isAllowOnlineLoans())) {
			return forwards.get("homePageFinder");
		}
		
		
		
		//gets the plan datas using contract id
		final PlanData planData = getPlanData(contractId);
		final PlanDataUi ui = new PlanDataUi(planData);
		//populates the loan features value into form bean for dispaly
		actionForm.setPlanDataUi(ui);
		final Map lookupData = ContractServiceDelegate.getInstance()
				.getLookupData(contractId);
		actionForm.setLookupData(lookupData);
		//sets whether participant can initiate loans
		actionForm.setParticipantCanInitiate(isEmployeeParticipation(contractId));
		//populating TPA firm permissions to LoanFeaturesFormBean
		actionForm = tpaFirmPermission(request,actionForm);
		//populate the Plan sponsor users with permissions
		getPSWUsers(contractId,actionForm);
		return forwards.get(ACTION_FORWARD_DEFAULT);
	}
	
    private boolean isAllowedPageAccess(UserProfile userProfile, Contract currentContract) {
        boolean canAccessPage = false;
        // If Internal User, Contract status must be PS, DC, PC, or CA
        if (userProfile.isInternalUser()) {
            if (currentContract.getStatus().equals("PS")
                    || currentContract.getStatus().equals("DC")
                    || currentContract.getStatus().equals("PC")
                    || currentContract.getStatus().equals("AC")
                    || currentContract.getStatus().equals("DI")
                    || currentContract.getStatus().equals("CF")
                    || currentContract.getStatus().equals("CA")) {
                // good
                canAccessPage = true;
            } else {
                logger.warn("Internal user cannot view/edit Contract status"
                        + currentContract.getStatus());
            }
            // External Users will be alloved to view AC, DI, CF
        } else {
            if (currentContract.getStatus().equals("AC")
                    || currentContract.getStatus().equals("DI")
                    || currentContract.getStatus().equals("CF")) {
                // good
                canAccessPage = true;
            } else {
                logger.warn("External user cannot view Contract status"
                        + currentContract.getStatus());
            }
        }
        return canAccessPage;
    }

	/**
	 * Retrieves the plan data information for the view screen.
	 * 
	 * @param contractId The contract id to retrieve plan data for.
	 * @return PlanData - The plan data information for the specified contract id.
	 */
	protected PlanData getPlanData(final Integer contractId)
			throws SystemException {

		final PlanData planData = ContractServiceDelegate.getInstance()
				.readPlanData(contractId);
		return planData;
	}

	/**This method checks and populates the initiate loan permission for Participant.
	 * @param contractId
	 * @return
	 * @throws SystemException 
	 */
	private boolean isEmployeeParticipation(Integer contractId) throws SystemException{
		boolean canInitiate = false;
		
			LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(contractId) ;
			if(loanSettings.isAllowParticipantInitiateLoan()){
				canInitiate = true;
			}
			
		
		return canInitiate;
		
	}
	
	/** This method checks and populates TPA permissions(initiate, reivew and signing authority )and Firm name.
	 * @param request
	 * @param loanFeaturesForm
	 * @return LoanFeaturesForm
	 * @throws SystemException
	 */
	private LoanFeaturesForm tpaFirmPermission(HttpServletRequest request,LoanFeaturesForm loanFeaturesForm) throws SystemException{
		
	
		TPAFirmInfo firmInfo = null;
		firmInfo = TPAServiceDelegate.getInstance()
		.getFirmInfoByContractId(
				getUserProfile(request).getCurrentContract()
						.getContractNumber());
		
		if (firmInfo != null) {
			ContractPermission permissions = firmInfo.getContractPermission();

			loanFeaturesForm.setTpaFirmCanInitiate(permissions.isInitiateLoans());
			loanFeaturesForm.setTpaFirmCanReview(permissions.isReviewLoans());
			loanFeaturesForm.setTpaFirmCanSign(permissions.isSigningAuthority());
			loanFeaturesForm.setTpaFirmName(firmInfo.getName());
			loanFeaturesForm.setTpaFirmId(firmInfo.getId());
		}
	
	return loanFeaturesForm;
	
	}
	
	/**
	 * This method is used to fetch the Plan Sponsor users with permissions by using the contract Id. 
	 * @param contractId
	 * @throws SystemException 
	 */
	private void getPSWUsers(Integer contractId, LoanFeaturesForm loanFeaturesForm) throws SystemException{
		try {
			List<UserInfo> userInfos = SecurityServiceDelegate.getInstance()
			.searchUsersByContractId(contractId);
			
			loanFeaturesForm.setUserInfoCollection(userInfos);
			
		} catch (SecurityServiceException e) {
			logger
            .error("LoanFeaturesAction getPSWUsers() Could not retirieve Plan sponsors data",e);                    
                   
		} 
		
	}
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 */
	 @Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWDefault);
	}
	
}
