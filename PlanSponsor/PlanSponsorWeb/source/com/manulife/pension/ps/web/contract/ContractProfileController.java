
package com.manulife.pension.ps.web.contract;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.home.HomePageForm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO.FeaturesAndServices;
import com.manulife.pension.service.fee.util.Constants.SENDServiceItem;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
       


/**
 * ContractProfile Action class 
 * This class is used to forward the users's request to 
 * ContractProfile page
 * 
 * @author Simona Stoicescu
 */
@Controller
@RequestMapping( value ="/contract")
public class ContractProfileController extends PsController 
{
	private static final String LOAN_COMPLETE_ACCESS = "Complete Online Loans Access (includes participant initiated loans)";
	private static final String LOAN_LIMITED_ACCESS = "Limited Online Loans Access (excludes participant initiated loans)";	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
    private static final String PRINT_FRIENDLY_PARAM = "printFriendly";
    private static final String INPUT_ACTION = "input";
    private static final String CONTRACT_PROFILE = "contractProfile";
    private static final String CONTRACT_PROFILE_PRINT = "contractProfilePrintFriendly";

	static{
		forwards.put(CONTRACT_PROFILE,"/contract/contractProfile.jsp");
		forwards.put(CONTRACT_PROFILE_PRINT,"/contract/contractProfile.jsp");
		forwards.put(INPUT_ACTION,"/contract/contractProfile.jsp");
	}
	public ContractProfileController()
	{
		super(ContractProfileController.class);
	} 
	@ModelAttribute("homePageForm")
	public HomePageForm populateForm() {
		return new HomePageForm();
	}

	@RequestMapping(value = "/contractProfile", method = RequestMethod.GET)
	public String doExecute(@Valid @ModelAttribute("homePageForm") HomePageForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException {
		 	    if (bindingResult.hasErrors()) {
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
					}
			    }
		ContractProfileVO vo = null;
		
		Contract currentContract = getUserProfile(request).getCurrentContract();
				  
		vo = ContractServiceDelegate.getInstance().getContractProfileDetails(
				currentContract.getContractNumber(), Environment.getInstance().getSiteLocation());

		LoanSettings loanSettings = LoanServiceDelegate.getInstance()
				.getLoanSettings(currentContract.getContractNumber());
		if (vo != null && vo.getFeaturesAndServices() != null
				&& vo.getFeaturesAndServices().getAccessChannels() != null) {

			if (loanSettings.isAllowParticipantInitiateLoan()
					&& loanSettings.isLrk01()) {
				vo.getFeaturesAndServices().getAccessChannels().add(
						LOAN_COMPLETE_ACCESS);
			}

			else if (!loanSettings.isAllowParticipantInitiateLoan()
					&& loanSettings.isLrk01()) {
				vo.getFeaturesAndServices().getAccessChannels().add(
						LOAN_LIMITED_ACCESS);
			}
		}	
		
		// set send service data
		Set<SENDServiceItem> SENDServiceItems = new HashSet<SENDServiceItem>();
		SENDServiceItems.add(SENDServiceItem.SERVICE_SELECTED);
		
		//CR032: Send Service feature display
		Map<SENDServiceItem, Object> values = FeeServiceDelegate.getInstance(
				Constants.PS_APPLICATION_ID).getSENDServiceDetails(
				null,
				currentContract.getContractNumber(),
				null,
				SENDServiceItems);

		if (vo != null && vo.getFeaturesAndServices() != null) {
			FeaturesAndServices featuresAndServices = vo.getFeaturesAndServices();
			if (featuresAndServices.getSendService() != null) {
				featuresAndServices.getSendService()
						.setEnabled("Y".equals((String) values.get(SENDServiceItem.SERVICE_SELECTED)) ? true : false);
			}
			if (featuresAndServices.getContractFeatures() != null) {
				Collection tempCollection = featuresAndServices.getContractFeatures();
				if (featuresAndServices.getSendService().isEnabled()) {
					tempCollection.add(Constants.SEND_SERVICE);
				}
				Collections.sort((List<String>) tempCollection);
				featuresAndServices.setContractFeatures(tempCollection);
			}
		}
		
			if ( logger.isDebugEnabled() )
			logger.debug(ContractProfileController.class.getName()+":forwarding to Contract Profile Page.");		
		
    	request.setAttribute(Constants.CONTRACT_PROFILE, vo);
    	request.setAttribute(Constants.MANAGED_ACCOUNT_SERVICE, getManagedAccountService(currentContract.getContractNumber()));
    	
    	 if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doExecute");
	    }
    	  	if ("true".equals (request.getParameter(PRINT_FRIENDLY_PARAM))) {
    	  		request.setAttribute("printFriendly", true);
    		return forwards.get(CONTRACT_PROFILE_PRINT);}
    	else
    		return forwards.get(CONTRACT_PROFILE);
    	  	
    }
	
	private static ManagedAccountServiceFeatureLite getManagedAccountService(int contractId) throws SystemException {
		return ContractServiceDelegate.getInstance()
				.getContractSelectedManagedAccountServiceLite(contractId);
	}

	@Autowired
    private PSValidatorFWNull psValidatorFWNull;  

	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder    binder) {
        binder.bind( request);
        binder.addValidators(psValidatorFWNull);
 }


}

