
package com.manulife.pension.ps.web.home;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.transaction.reporthandler.UncashedChecksReportHandler;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
/**
 * HomePage Action class
 * gets required value objects , farwards to secureHomePage.jsp
 * PlanSponsor.
 *
 * @author Ludmila Stern
 * Dec 2003
 */
@Controller
@RequestMapping( value = "/home")

public class HomePageController extends PsController {
	
	
	@ModelAttribute("homePageForm")
	public HomePageForm populateForm() {
		return new HomePageForm();
	}
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	
	static{
		forwards.put("input","/home/secureHomePage.jsp");
		forwards.put("homePage","/home/secureHomePage.jsp");
		forwards.put("definedBenefitHomePage","/home/definedBenefitSecureHomePage.jsp");
		forwards.put("homePageFinder","redirect:/do/home/homePageFinder/");
		}


	
	
	public static final String DISCONTINUED_STATUS = "DI";
	public static final String DEFINED_BENEFIT = "DB06";
	public static final String DEFINED_BENEFIT_NY = "DBNY06";
	private static final String HOMEPAGE_FINDER_FORWARD = "homePageFinder";
	
	
	

	public HomePageController()
	{
		super(HomePageController.class);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/homePage/", method = {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("homePageForm") HomePageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	    String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	     if(errDirect!=null){
	       request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       return forwards.get("homePage");//if input forward not //available, provided default
	      }

		}
		

		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doExecute");
	    }
		
	   	ContractSummaryVO contractSummaryVO = null;
		// This code has been changed and added to validate form and request
		// against penetration attack, prior to other validations as part of the CL#137697.
		/*Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			ActionForward forward = new ActionForward(CommonConstants.ERROR_RDRCT, "/do"
					+ mapping.findForward("homePage"), true);
			return forward;
		}*/
	   	// by default we won't show the loans area
	   	String showLoans=null;
	   	String showFiduciary = null;

	 	UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		
		// if there're no current contract, forward them to the home page finder
		if(currentContract == null)
			
			return forwards.get(HOMEPAGE_FINDER_FORWARD);

	 	// if value object already exist then get from the session
   		// TODO Need to check the size of this to make sure its not too big
	 	// contractSummaryVO = (ContractSummaryVO) session.getAttribute("contractSummary");

	 	if (contractSummaryVO == null) {
	 		// if not - get request the value object from business delegate and add to the session
 	 		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();

			// TODO TEST CODE remove these two lines later
			// if its a real contract in the database
			// use it
			int contractNumber= currentContract.getContractNumber();
			
			//CL 134735: June 2016 Release ME
			getUncashedChecksData(request,contractNumber);
			
			if(contractNumber>1000)
				contractSummaryVO = contractServiceDelegate.getContractSummary(currentContract.getContractNumber(),getUserProfile(request).getRole().isExternalUser() );
			else {
				// Lets fill this with dummy data for the other test stub contracts
				contractSummaryVO = new ContractSummaryVO();
				ParticipantListVO participantListVO = new ParticipantListVO();

				contractSummaryVO.setTotalContractAssets(1700500.325);
				contractSummaryVO.setCashAccountBalanace(7122.657);
				contractSummaryVO.setOutstandingLoansCount(5);
				contractSummaryVO.setOutstandingLoans(23350.734);
				contractSummaryVO.setLastAllocationAmount(8761.87);
				contractSummaryVO.setLastPayrollDate (new java.util.Date());
				contractSummaryVO.setLastSubmissionDate(new java.util.Date());


				// Setting car values
				ContactVO contactVO = new ContactVO();
				contactVO.setName("John Manulife");
				contactVO.setPhone("1.800.926.3000");
				contactVO.setEmail("John_Manulife@manulife.com");
				contactVO.setExtension("21250");
				contactVO.setContactType(ContactVO.CONTACT_CAR_TYPE);
				contractSummaryVO.setCarContact(contactVO);

				// Setting participant values
				participantListVO.setTotalCount(5);
				for (int i = 1; i <= 5; i++) {
					ParticipantVO p = new ParticipantVO();
					p.setFirstName("John" + i);
		        	p.setLastName("Smith" + i);
		        	p.setWholeName(p.getLastName() + ", " + p.getFirstName());
		        	p.setProfileId("100" + i);
		        	participantListVO.addParticipant(p);
				}
				contractSummaryVO.setParticipants(participantListVO);
			}
			contractSummaryVO.setContractNumber(currentContract.getContractNumber());
			contractSummaryVO.setContractName(currentContract.getCompanyName());
			// put it back in the session for later
			// TODO Need to check the size of this to make sure its not too big
	    	// session.setAttribute("contractSummary",contractSummaryVO);
	 	}

		// set the loans total amount
		currentContract.setLoansTotalAmount(new Double(contractSummaryVO.getOutstandingLoans()));

	 	if (!currentContract.isLoanFeature()
				&& contractSummaryVO.getOutstandingLoans() == 0)
			showLoans = "false";
		else
			showLoans = "true";

	 	
	 	if (currentContract.getStatus().equals(DISCONTINUED_STATUS) ||
	 		currentContract.isDefinedBenefitContract()
	 		|| contractSummaryVO.isCoFiduciary())  {
	 		showFiduciary = "false";
	 	} else {
	 		showFiduciary = "true";
	 	}

        
	 	boolean hasLifecycle = false;
	 	// LS set hasLifecycle indicator
	 	// to do - should be replaced with a field in contract_cs
	 	List selectedFunds =contractSummaryVO.getSelectedFunds();
	 	Iterator fundsIteraror = selectedFunds.iterator();
		while (fundsIteraror.hasNext()) 
	 	{
	 		String fundId =	 (String)fundsIteraror.next();
	 		if(FundVO.RISK_LIFECYCLE.equals(FundInfoCache.getRiskCategoryCode(fundId)))
	 		{
	 			hasLifecycle = true;
	 			break;
	 		}
	 		
	 	}
		currentContract.setHasLifecycle(hasLifecycle);
		boolean isLateBatch = EnvironmentServiceHelper.getInstance().isLateBatchPSW();
   	    // put it in the request for now
		String lateBatch = isLateBatch?"true":"false";
		request.setAttribute("lateBatch",lateBatch);
		request.setAttribute("showLoans",showLoans);
    	request.setAttribute("contractSummary",contractSummaryVO);
    	request.setAttribute("participantList",contractSummaryVO.getParticipants());
	    request.setAttribute("showFiduciary", showFiduciary);    	
        request.setAttribute("employeeList",contractSummaryVO.getEmployees());
        request.setAttribute("contractPeraDetailsVO",contractSummaryVO.getContractPeraDetailsVO());

        if (!userProfile.isInternalUser()) {
	        request.setAttribute("mcModel", MCUtils
					.getHomePageBoxModel(request, request.getServletContext(), SessionHelper
							.getUserProfile(request)));
        }
        
	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doExecute");
	    }
        
        if (!currentContract.isDefinedBenefitContract())  {
        	return forwards.get("homePage");
			
        } else {
        	return forwards.get("definedBenefitHomePage");
           
        }
        
        
    }
	
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
	/**
	 * Get the Uncashed checks data and keeps it in request scope
	 * @param request
	 * @param contractId
	 * @throws SystemException
	 */
	public void getUncashedChecksData(HttpServletRequest request,int contractId) throws SystemException{
		ReportCriteria reportCriteria = new ReportCriteria(UncashedChecksReportHandler.REPORT_ID);
		UncashedChecksReportHandler handler = new UncashedChecksReportHandler();
		ReportData reportBean;
        try {
        	reportCriteria.addFilter("contractNumber", contractId);
        	reportBean = handler.getReportData(reportCriteria);
            request.setAttribute("uncashedChecks", reportBean);
        } catch (ReportServiceException e)
 		{
			throw ExceptionHandlerUtility.wrap(e);
 		}
	}
	

	
}
