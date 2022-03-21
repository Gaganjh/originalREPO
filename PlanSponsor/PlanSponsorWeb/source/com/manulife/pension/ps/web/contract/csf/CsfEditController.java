package com.manulife.pension.ps.web.contract.csf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
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
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApolloBackEndException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.util.CommonMrlLoggingUtil;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCSF;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.notices.valueobject.AutomaticContributionVO;
import com.manulife.pension.service.notices.valueobject.ContributionsAndDistributionsVO;
import com.manulife.pension.service.notices.valueobject.InvestmentInformationVO;
import com.manulife.pension.service.notices.valueobject.SafeHarborVO;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;

/**
 * Handles the CSF Edit action
 *  
 * @author Puttaiah Arugunta
 */

@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"csfForm"})

public class CsfEditController extends CsfBaseController {
	
	public static final String CONTRIBUTION_AND_DISTRIBUTION = "contributionAndDistribution";
    public static final String SAFE_HARBOR = "safeHarbor";
    public static final String AUTOMATIC_CONTRIBUTION = "automaticContribution";
    public static final String INVESTMENT_INFO = "investmentInformation";
	
    
    @ModelAttribute("csfForm") 
	public CsfForm populateForm() 
	{
		return new CsfForm();
	}

	public static Map<String,String> forwards = new HashMap<>();
	static {
		forwards.put("input", "/csf/csfEdit.jsp");
		forwards.put("csfElectronServices", "redirect:/do/contract/viewServiceFeatures/");
		forwards.put("csfElectronServicesEdit", "/csf/csfEdit.jsp");
		forwards.put("csfElectronServicesConf", "redirect:/do/contract/confServiceFeatures/");
		forwards.put("homePageFinder", "redirect:/do/home/homePageFinder/");
		forwards.put("csfElectronServicesConftest","/csf/csfConfirmation.jsp");
		}
	
	@Autowired
	private PSValidatorFWCSF  psValidatorFWCSF;

	/**
	 * Constructor
	 */
	public CsfEditController() {
		super(CsfEditController.class);
	}

    /**
     * Method to populate the Edit CSF page once the request is from the CSF pages. Otherwise it will
     * redirect the request to the View/Home page depends on the access.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */

	@RequestMapping(value ="/editServiceFeatures/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("csfForm") CsfForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
		// Retrieve and set form data for Contract Service Features
		//CsfForm csfForm = (CsfForm) actionForm;

		// Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);
		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();

		if (currentContract == null 
				|| !isAllowedPageAccess(userProfile, currentContract)) {
			return forwards.get(CsfConstants.GLOBAL_HOME_PAGE_FINDER);
		}
		int contractId = currentContract.getContractNumber();
		
        /*if contract has a below product type,
         *   1) 457 product or
         *   2) RA457 plan type or
         *   3) industry type GT or
         *   4) organization type GO*/
        boolean isContractAGovtPlan;
        try{
            isContractAGovtPlan = ContractServiceDelegate.getInstance().isGovernmentPlan(getUserProfile(request).getCurrentContract().getContractNumber());
        }catch (ContractDoesNotExistException e) {

            //logger.error(e);
            return "redirect:"+Constants.HOME_URL;

        }
        Map<String, String> mtaContractDetails = ContractServiceDelegate.getInstance().getMtaContractDetails(contractId);
        if((!currentContract.getStatus().equals(CsfConstants.CONTRACT_STATUS_DI))
	    		  && !currentContract.isDefinedBenefitContract()
	    		  && (mtaContractDetails != null && (!StringUtils.equals(mtaContractDetails.get(CsfConstants.DISTRIBUTION_CHANNEL), CsfConstants.MTA) 
	    			  && !StringUtils.equals(mtaContractDetails.get(CsfConstants.GROUP_FIELD_OFFICE_NO), CsfConstants.GFO_CODE_25270)
	    			  && !StringUtils.equals(mtaContractDetails.get(CsfConstants.GROUP_FIELD_OFFICE_NO), CsfConstants.GFO_CODE_25280)))
	    		  && ( !isContractAGovtPlan)
	    		  &&(FeeDisclosureUtility.isPinpoinContract(contractId))
	    		  &&!currentContract.isBundledGaIndicator()){
        	actionForm.setDisplayNoticeGeneration(true);
        }else{
        	actionForm.setDisplayNoticeGeneration(false);
        }
		
		// CSF. 371 If contract product feature not = “LRK01”, then do not display the Loans attributes 
        actionForm.setLoanRecordKeepingInd(ContractServiceDelegate.getInstance().
				hasContractWithContractProductFeature(contractId,CsfConstants.CONTRACT_PRODUCT_FEATURE_LRK01) ? 
						CsfConstants.CSF_YES : CsfConstants.CSF_NO);
		
		// Method to determine the Auto/Sign up from the Database
        actionForm.setAciSignupMethod(
				ContractServiceDelegate.getInstance().determineSignUpMethod(contractId));

		String forward = null;
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		//CSF.8 If the contract effective date for the selected contract was before the COW implementation date 
		//and the contract has product type “Defined benefit”, users are not allowed to navigate to the Contract Service Features page
		BusinessParameterValueObject businessParameterObject = 
			PartyServiceDelegate.getInstance().getBuisnessParameterValueObject();
		
		Date effectiveDate = currentContract.getEffectiveDate();
		
		if (businessParameterObject != null){
			Date consentImplementationDate = businessParameterObject.getCsfContentWebLaunchDate();

			if((effectiveDate != null && (consentImplementationDate != null))){
				if (effectiveDate.before(consentImplementationDate)) {
					if (getUserProfile(request).getCurrentContract().isDefinedBenefitContract()) {
						forward =  forwards.get(CsfConstants.GLOBAL_HOME_PAGE_FINDER);
					}
					actionForm.setHideConsent(true);
				}
			}
		}

		// At present we have only the consent information service feature for DB contract.
		// Should not display edit button for external user seeing the consent information of a Defined Benefit contract.
		if(getUserProfile(request).getCurrentContract().isDefinedBenefitContract()){
			if(!getUserProfile(request).isInternalUser()){
				actionForm.setEditable(false);
			}
		}
			// We're just refreshing the page, no button (Edit or Cancel was clicked)
			if (actionForm.getButton() == null) {
				actionForm.revert();
				// If this request is not coming form a csf page, go to view mode
				//if (request.getAttribute("csf") == null) 
				if(request.getSession().getAttribute("csf")== null){
					CommonMrlLoggingUtil.logUnAuthAcess(request,"User is not authorized",this.getClass().getName()+":"+"processRequest");
					forward =  forwards.get(getViewForwardName());

				} else {
					if (LockServiceDelegate.getInstance().lock(
							LockHelper.CSF_LOCK_NAME, 
							LockHelper.CSF_LOCK_NAME + contractId, 
							userProfile.getPrincipal().getProfileId())) {
						forward =  forwards.get(getEditForwardName());
					} else {
						// error message
						forward = forwards.get(getViewForwardName());
					}
				}
				
			}else if (LockServiceDelegate.getInstance().lock(LockHelper.CSF_LOCK_NAME, 
				LockHelper.CSF_LOCK_NAME + contractId, userProfile.getPrincipal().getProfileId())){
			
				PlanDataLite planDataLite = getPlanDataLite(request, contractId);
				
				if(actionForm.getButton().equals(CsfConstants.CONTINUE_EDIT_VALUE)){
					
					actionForm = (CsfForm)request.getSession(false).getAttribute(CsfConstants.CSF_FORM);
					 
					//Method commneted because already checkbox values are in the session. no need to populate it again
					CsfDataHelper.getInstance().populateCheckBoxValues(actionForm);
				}else{
					loadCSFDataToForm(actionForm,null, currentContract, planDataLite, false);
				}
				
				// Do  validations if Contract is not Defined Benfit
				if(!getUserProfile(request).getCurrentContract().isDefinedBenefitContract()){
					CsfDataValidator.getInstance().validatePlanData(planDataLite, actionForm);
				    WithdrawalDistributionMethod withdrawalDistributionMethod = ContractServiceDelegate
						.getInstance().getWithdrawalDistributionMethods(contractId);
					CsfDataValidator.getInstance().validateCSFAgainstPlanData(actionForm,  planDataLite, withdrawalDistributionMethod, errors);
					CsfDataValidator.getInstance().validateOnlineDeferrals(actionForm, planDataLite, errors);
				}
				forward = forwards.get(getEditForwardName());
				
			} else {
				
				try	{
					Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(
							LockHelper.CSF_LOCK_NAME, LockHelper.CSF_LOCK_NAME + contractId);
	
					UserInfo lockOwnerUserInfo =
						SecurityServiceDelegate.getInstance().searchByProfileId(
								userProfile.getPrincipal(), lockInfo.getLockUserProfileId());
	
					String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(
							userProfile, lockOwnerUserInfo);
					errors.add(new ValidationError(
							CsfConstants.EMPTY_STRING, ErrorCodes.ERROR_CSF_LOCKED_BY_JH_REPRESENTATIVE, new String[] {lockOwnerDisplayName}));
	
					forward = forwards.get(getViewForwardName());
				} catch(SecurityServiceException e)	{
					throw new SystemException("com.manulife.pension.ps.web.contract.csf.CsfAction.doExecute "
							+ "Failed to get user info of lock own. " + e.toString());
				}
			}

		// to handle double submissions
		//saveToken(request);
			
		if (!errors.isEmpty()) {
    		setErrorsInSession(request, errors);
		}
		request.getSession().removeAttribute("csf");
		
		return forward;
	}

	
  
	/**
     * Method to validate the Editable values with the existing values once Save operation is performed.
     * Request redirects to the Edit page, if it has any validation errors else go to the confirmation page to
     * confirm the Newly edited values.
     * 
     * @param mapping
     * @param form
     * @param request
     */

    /*@SuppressWarnings("rawtypes")
	protected ActionForward validate(ActionMapping mapping, Form actionForm, HttpServletRequest request) {
        ActionForward forward = null;
        
		// This code has been changed and added to Validate form and request
		// against penetration attack, prior to other validations 
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(
				actionForm, mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(ERROR_KEY);
			return new ActionForward(CommonConstants.ERROR_RDRCT, "/do"
					+ mapping.getPath(), true);
		}
        // Need to add the contractProfile VO to the request
		int contractId = getUserProfile(request).getCurrentContract()
				.getContractNumber();
		setContractProfile(contractId, request);
        return forward;
    }*/

    /**
     * Method to Save the newly given values in the edit CSF page, once all the validations are successfull.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws SecurityServiceException 
     */
   
    @RequestMapping(value ="/editServiceFeatures/",params={"action=save"}, method =  {RequestMethod.POST}) 
   	public String doSave(@Valid @ModelAttribute("csfForm") CsfForm csfForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException, SecurityServiceException {
   	if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       	}
       } 
         	// this is protect the double submit. If double submit happens,
       	// then redirect to View Page
           /*if (!(isTokenValid(request, true))) {
               return mapping.findForward(CsfConstants.CSF_ELECTRON_CONTRACT_SERVICES_PAGE);
           }*/
           
       	//CsfForm csfForm = (CsfForm) form;

       	String forward = null;
       	UserProfile userProfile =  getUserProfile(request);
       	csfForm.setButton(CsfConstants.SAVE_BUTTON);
       	
       	Collection<GenericException> errors = new ArrayList<GenericException>();
       	Collection<GenericException> warnings = new ArrayList<GenericException>();
       	
       	// Adds a new token, as we forward to a JSP 
       	//saveToken(request);
           
       	if(getUserProfile(request).getCurrentContract().isDefinedBenefitContract()){
       		if(StringUtils.equals(((CsfForm)csfForm.getClonedForm()).getConsentInd(),
       				csfForm.getConsentInd())){
       			errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.SAVING_WITH_NO_CHANGES));
       			setErrorsInSession(request, errors);
   				return forwards.get(getEditForwardName());
       		}
       		try {
   				saveContractServiceFeatureData(csfForm, request);
   			} catch (ApolloBackEndException e) {
   				errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.TECHNICAL_DIFFICULTIES));
   				setErrorsInSession(request, errors);
   				return forwards.get(getEditForwardName());
   			}
       		return forwards.get(getViewForwardName());
       	}

       	final PlanDataLite planDataLite = getPlanDataLite(request,
       			getUserProfile(request).getCurrentContract()
       			.getContractNumber());

       	CsfDataValidator csfDataValidator = CsfDataValidator.getInstance();
       	CsfDataHelper csfDataHelper =  CsfDataHelper.getInstance();

       	// Method to determine the Auto/Signup method from the CSF form. This may varies from 
       	//the database value, which is loaded On-load (doDefault) method
       	csfForm.setAciSignupMethod(csfDataValidator.determineSignupMethod(csfForm, planDataLite));
       	
       	 WithdrawalDistributionMethod withdrawalDistributionMethod = ContractServiceDelegate
   			.getInstance().getWithdrawalDistributionMethods(userProfile.getCurrentContract().getContractNumber());
       	
       	csfDataValidator.validateCSFValues(csfForm, request, userProfile, withdrawalDistributionMethod, errors , warnings);     	
   		
       	if(errors.isEmpty()){
   			ParticipantACIFeatureUpdateVO paf = getParticipantACIFeatureUpdateVO(request);
   			paf.setAppliesTo(planDataLite.getAciApplyTo());

   	    	Collection<ContractServiceFeature> changedCsfCollection = csfDataHelper.getChangedContractServiceFeatures(
   	    			userProfile.getCurrentContract(), csfForm, userProfile
   	    			.getPrincipal(), paf);

   	    	request.getSession(false).setAttribute(CsfConstants.CHANGED_CSF_COLLECTION,changedCsfCollection);
   	    	request.getSession(false).setAttribute(CsfConstants.PARTICIPANT_ACI_FEATURE_VO,paf);

   	    	if(changedCsfCollection.size() > 0 ){
   	    		csfDataHelper.validateOtherContractServiceFeatureData(csfForm, request, changedCsfCollection, errors, warnings, false);
   	    	}else{
   	    		errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.SAVING_WITH_NO_CHANGES));
   	    	}
   		}
       	if (!errors.isEmpty()) {
       		errors.addAll(warnings);
       		setErrorsInSession(request, errors);
       		forward = forwards.get(getEditForwardName());
       	}else if(!warnings.isEmpty()){
       		setErrorsInSession(request, warnings);
       		setFormInSession(request, csfForm);
       		forward = forwards.get(getConfForwardName());
       	}else{
       		try {
       			Collection<GenericException> newErrors = saveContractServiceFeatureData(csfForm, request);
       			if(newErrors!= null && !newErrors.isEmpty()){
       				setErrorsInSession(request, newErrors);
       				return forwards.get(getEditForwardName());
       			}
   			} catch (ApolloBackEndException e) {
   				errors.add(new ValidationError(CsfConstants.EMPTY_STRING,ErrorCodes.TECHNICAL_DIFFICULTIES));
   				setErrorsInSession(request, errors);
   				return forwards.get(getEditForwardName());
   			}
       		userProfile.getContractProfile().setQuickReportList(null);
       		return forwards.get(getViewForwardName());
       	}
       	return forward;
       }
    /**
     * Method to set the FormBean in Session
     * @param request
     * @param csfForm
     */
    private static void setFormInSession(final HttpServletRequest request,
			final CsfForm csfForm) {
		if (csfForm != null) {
			// check for csfForm already in session scope
			CsfForm oldCsfForm = (CsfForm) request.getSession(false).getAttribute(CsfConstants.CSF_FORM);
			if (oldCsfForm != null) {
				request.getSession(false).removeAttribute(CsfConstants.CSF_FORM);
			}

			request.getSession(false).setAttribute(CsfConstants.CSF_FORM, csfForm);
		}
	}
    /**
     * Method to Cancel the current Edit page request and redirects to the View page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
   @RequestMapping(value ="/editServiceFeatures/",params={"action=Cancel"}, method =  {RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute("csfForm") CsfForm myForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException{
    	
    	//CsfForm myForm = (CsfForm) form;
    	
    	UserProfile userProfile = getUserProfile(request);
    	int contractId = userProfile.getCurrentContract().getContractNumber();
    	
		LockServiceDelegate.getInstance().releaseLock(LockHelper.CSF_LOCK_NAME,
				LockHelper.CSF_LOCK_NAME + contractId);

		myForm.setButton(null);
		myForm.revert();
		return forwards.get(getViewForwardName());
    }
    

    
        /**
     * 
     */
    private HashMap<String, String> getNoticePlanDataDetails(HttpServletRequest request, Integer contractId){
        HashMap<String, String> planDataMap = new HashMap<String, String>();
        try {
            // get the plan data from database
            
            ContributionsAndDistributionsVO contributionsAndDistributionsVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractId, "contributionAndDistribution").getContributionsAndDistributionsVO();
            SafeHarborVO safeHarborVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractId, "safeHarbor").getSafeHarborVO();
            AutomaticContributionVO automaticContributionVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractId, "automaticContribution").getAutomaticEnrollmentVO();
            InvestmentInformationVO invInfoVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractId, "investmentInformation").getInvestmentInformationVO();
            
            String contriAndDistriDataCompleteInd = contributionsAndDistributionsVO.getDataCompleteInd();
            String shDataCompleteInd = safeHarborVO.getDataCompleteInd();
            String autoContriDataCompleteInd = automaticContributionVO.getDataCompleteInd();
            String isDIOaQDIA = invInfoVO.getdIOisQDIA();
            String transferOutDays = invInfoVO.getqDIAFeeRestrictionOnTransferOutDays();
            
            request.setAttribute(CONTRIBUTION_AND_DISTRIBUTION, contriAndDistriDataCompleteInd);
            request.setAttribute(SAFE_HARBOR, shDataCompleteInd);
            request.setAttribute(AUTOMATIC_CONTRIBUTION, autoContriDataCompleteInd);
            request.setAttribute(INVESTMENT_INFO, isDIOaQDIA);
            
            planDataMap.put(CONTRIBUTION_AND_DISTRIBUTION, contriAndDistriDataCompleteInd);
            planDataMap.put(SAFE_HARBOR, shDataCompleteInd);
            planDataMap.put(AUTOMATIC_CONTRIBUTION, autoContriDataCompleteInd);
            planDataMap.put(INVESTMENT_INFO, isDIOaQDIA);
            planDataMap.put("TRANSFER_NO_OF_DAYS", transferOutDays);
            
        } catch (SystemException e) {
            throw new NestableRuntimeException(e);
        }
        return planDataMap;
        
    }
    
    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWCSF);
	}
}
