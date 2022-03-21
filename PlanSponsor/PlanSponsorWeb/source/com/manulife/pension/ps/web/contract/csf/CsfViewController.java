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
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCSF;
import com.manulife.pension.service.contract.util.CoFidPlanReviewHelper;
import com.manulife.pension.service.contract.valueobject.CoFiduciaryVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;

/**
 * Action class to handle both the CSF Standard and Customized view
 * 
 * @author Arugunta Puttaiah
 *
 */

@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"csfForm"})
public class CsfViewController extends CsfBaseController {	

	@ModelAttribute("csfForm")
	public CsfForm  populateForm() 
	{
		return new CsfForm();
	}

	public static Map<String,String> forwards = new HashMap<>();
	static
	{
		forwards.put("input", "/csf/csfView.jsp");
		forwards.put("csfElectronServices", "/csf/csfView.jsp");
		forwards.put("csfElectronServicesEdit", "redirect:/do/contract/editServiceFeatures/");
		forwards.put("csfElectronServicesConf", "redirect:/do/contract/confServiceFeatures/");
		forwards.put("homePageFinder", "redirect:/do/home/homePageFinder/");
	} 

	@Autowired
	   private PSValidatorFWCSF  psValidatorFWInput;
	
	/**
	 * @param clazz
	 */
	public CsfViewController() {
		super(CsfViewController.class);
	}

	/**
	 * Method to populate the View CSF page. 
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
	@RequestMapping(value ="/viewServiceFeatures/", method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("csfForm") CsfForm csfForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException, ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
		
		//CsfForm csfForm = (CsfForm) actionForm;

		
		// get the userProfile from the session
		UserProfile userProfile = getUserProfile(request);
		
		// get the contract object from the profile object
		Contract currentContract = userProfile.getCurrentContract();
		
		// get the contractId 
        int contractId = currentContract.getContractNumber();
        
        // get the contract profile from database and place in request scope 
        setContractProfile(contractId, request);
        
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
						return  forwards.get(CsfConstants.GLOBAL_HOME_PAGE_FINDER);
					}
					csfForm.setHideConsent(true);
				}
			}
		}
        
    	Map csfMap = null;
		/*
		 *  get the csf and attributes value from database 
		 *  for the specified contract
		 */
		try {
			csfMap = ContractServiceDelegate.getInstance().
							getContractServiceFeatures(contractId);
		} catch (ApplicationException ae) {
			throw new SystemException("An exception occured at " 
					+ "CsfViewAction.doDefault()" + ae.getDisplayMessage());
		}
		
		// validate the user has edit permissions
		csfForm.setEditable(isAllowedEditing(userProfile, currentContract));
		
		// get the plan data for the contract
		PlanDataLite planDataLite = getPlanDataLite(request, contractId);
		
		//condition to display the Notice Generation section
		//when the Contract has the Product Types - DB06, DBNY06
		boolean isDefinedBenefitContrct = getUserProfile(request).getCurrentContract().isDefinedBenefitContract();
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
			  csfForm.setDisplayNoticeGeneration(true);
	        }else{
	        	csfForm.setDisplayNoticeGeneration(false);
	        }
		
		// determine the CMA Key for each attribute based on the value
		CsfDataHelper.getInstance().loadCMAdata(
				csfMap, csfForm, contractId, planDataLite);
		
		loadCSFDataToForm(csfForm, csfMap, currentContract, planDataLite, false);
		
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		// Do cross - edit validations if Contract is not Defined Benfit
		if(!getUserProfile(request).getCurrentContract().isDefinedBenefitContract()){
			
			WithdrawalDistributionMethod withdrawalDistributionMethod = ContractServiceDelegate
			.getInstance().getWithdrawalDistributionMethods(contractId);
			
			// validate the cross-edits
    		CsfDataValidator.getInstance().validateCSFAgainstPlanData(
    				csfForm, planDataLite, withdrawalDistributionMethod, errors);
		}
		boolean isCustomized = CsfDataHelper.getInstance().isStandardView(csfForm, contractId);
		request.setAttribute("isCustomizedCSF", Boolean.valueOf(!isCustomized));
		
		//Co-Fid change: //If the contract has CoFid or not. 
			CoFiduciaryVO coFiduciaryVO=ContractServiceDelegate.getInstance().getCoFiduciaryVO(contractId);
				
		//Setting CoFid Description and Investment profile option for View page
		if (coFiduciaryVO.isCoFiduciary()) {
			csfForm.setSelectedInvestmentProfile(coFiduciaryVO
					.getSelectedInvestmentOptionDescription());
		}
		//loading Edit page data
		populateCoFidServiceFeatureDetails(csfForm,coFiduciaryVO);
		
		if (!errors.isEmpty()) {
    		setErrorsInSession(request, errors);
		}

		return forwards.get(getViewForwardName());

	}


	/**
	 * Method to forward the request to the Edit page and setting the csf page
	 * attribute to true, to confirm the edit request is from the CSF page aprart 
	 * from the others. 
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

	@RequestMapping(value ="/viewServiceFeatures/", params="action=edit", method = {RequestMethod.POST})
    public String doEdit(@ModelAttribute ("csfForm") CsfForm csfForm, BindingResult bindingResult,
            final HttpServletRequest request, final HttpServletResponse response){
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
		
		request.getSession().setAttribute("csf", Boolean.TRUE);
		csfForm.setButton(CsfConstants.EDIT_BUTTON);
		return forwards.get(getEditForwardName());
	}
	
	
	public void populateCoFidServiceFeatureDetails(CsfForm csfForm,
			CoFiduciaryVO coFiduciaryVO) throws SystemException {
		csfForm.setCoFidServiceFeatureDetails(new ArrayList<CoFidServiceFeatureDetails>());
		ArrayList<Pair<String, String>> activeCoFidServiceProvidersList = ContractServiceDelegate
				.getInstance().getActiveCoFidServiceProviders();
		for (Pair activeCoFidServiceProvider : activeCoFidServiceProvidersList) {
			CoFidServiceFeatureDetails coFidServicefeatureDetail = new CoFidServiceFeatureDetails();

			coFidServicefeatureDetail
					.setCoFidServiceProviderDescription(CoFidPlanReviewHelper
							.getInstance().getCoFidLookUpDescription(
									(String) activeCoFidServiceProvider
											.getFirst(),
									(String) activeCoFidServiceProvider
											.getSecond()));
			if ((activeCoFidServiceProvider.getFirst().equals(coFiduciaryVO
					.getServiceProviderCode()))
					&& ((activeCoFidServiceProvider.getSecond()
							.equals(coFiduciaryVO.getFiduciaryType()
									.getDbCode())))) {
				coFidServicefeatureDetail.setSelectedServiceProvider(true);
				;
			}
			csfForm.getCoFidServiceFeatureDetails().add(
					coFidServicefeatureDetail);
		}
	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations
   	 */
	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
}
}