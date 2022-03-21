package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.party.valueobject.BusinessParameterValueObject;

/**
 * COntractDetails Action class 
 * This class is used to forward the users's request to 
 * ContractDetails page
 * 
 * @author Ilker Celikyilmaz
 */

@Controller
@RequestMapping( value = "/contract")

public class ContractDetailsController extends PsController 
{
	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{ 
	return	new DynaForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/contract/contractDetails.jsp");
		forwards.put("contractDetails","/contract/contractDetails.jsp");
			}

	private static final String CONTRACT_DETAILS_PAGE = "contractDetails";
	
	private static final String HIDE_CSF_LINK = "hideCsfLink";
	
	private static Contract contract = null;
	
	public ContractDetailsController()
	{
		super(ContractDetailsController.class);
	} 
	
	
		
		@RequestMapping(value ="/contractDetails/", method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doExecute(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }
		
		
		if ( logger.isDebugEnabled() )
			logger.debug(ContractDetailsController.class.getName()+":forwarding to Contract Details Page.");
        
        // Get the Principal from the request
		UserProfile userProfile = getUserProfile(request);
		// get the user profile object and set the current contract to null
		Contract currentContract = userProfile.getCurrentContract();
		contract = currentContract;
			
        boolean isPlanAvailable = false;
        try {
            String contractStatus = getUserProfile(request).getCurrentContract().getStatus();
            Contract contract=getUserProfile(request).getCurrentContract();
            boolean isDefinedBenefitContract = getUserProfile(request).getCurrentContract().isDefinedBenefitContract();
            boolean isUserContractOK = (!getUserProfile(request).isInternalUser() && !(PlanConstants.CONTRACT_STATUS_EXTERNAL_USER_ACCESS.contains(contractStatus))) ? false : true;
            boolean isContractOK = (PlanConstants.CONTRACT_STATUS_HOME_PAGE.contains(contractStatus)) ? true : false;
            ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
            
            if (!isDefinedBenefitContract && isUserContractOK && isContractOK) {
                isPlanAvailable = true;
            }
            //Call Contract service to find if the ContractNumber has any Contract or Amendment documents
            // and enable the 'Contract Documents' link only if  contract or Amendment document is present.
            boolean isContractDocPresent = contractServiceDelegate
					.checkContractHasAmendmentOrContractDocuments((new Integer(contract.getContractNumber())).toString());
   
            //Contract and contract amendment PDFs only available to external users on PSW in Trustee or 
            //TPA role if consent = Y
            if(getUserProfile(request).isTPA()||getUserProfile(request).isTrustee()){
            	boolean isPaperConsentGiven = contractServiceDelegate.checkContractConsent(contract.getContractNumber());
            	if(isPaperConsentGiven && isContractDocPresent){
            		request.setAttribute(Constants.IS_CONTRACT_DOC_PRESENT, isContractDocPresent);	
            	}
            }else{
            	if(isContractDocPresent){
            		request.setAttribute(Constants.IS_CONTRACT_DOC_PRESENT, isContractDocPresent);
            	}
            }
            
            // CSF link display for Define Benefit contract
            if (isDefinedBenefitContract) {
            	// Added for PSW 'paper consent CR'. If the contract is old i.e. Effective date < Launch date then hide the CSF link.
            	BusinessParameterValueObject businessParameterObject = PartyServiceDelegate.getInstance().getBuisnessParameterValueObject();
            	Date effectiveDate = contract.getEffectiveDate();
            	if (businessParameterObject != null){
            		Date consentImplementationDate = businessParameterObject.getCsfContentWebLaunchDate();
                
            		if((effectiveDate != null && (consentImplementationDate != null))){
            			if (effectiveDate.before(consentImplementationDate)) {
            				request.setAttribute(HIDE_CSF_LINK, HIDE_CSF_LINK);
            			}
            		}
            	}
            }
                                        
        } catch (Exception e) {
            logger.warn(e);
        }
        request.setAttribute("isPlanAvailable", isPlanAvailable);
        
		return forwards.get(CONTRACT_DETAILS_PAGE);
	}	
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	
	

  @Autowired
 private PSValidatorFWNull  psValidatorFWNull;
  
 @InitBinder
 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
  binder.bind( request);
  binder.addValidators(psValidatorFWNull);
}
	
	
}