
package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.PlanConstants;

/**
 * 
 * This class is used to forward the users's request to 
 * the Welcome page if contract is in one of the following
 * statuses: 
 * Proposal Signed (PS), Details Complete (DC), 
 * Pending Contract Approval (PC), and Contract Approved (CA)
 * 
 * @author Diana Macean
 */
@Controller
@RequestMapping( value = "/home")

public class WelcomePageController extends PsController 
{
	
	@ModelAttribute("welcomePageForm")
	public WelcomePageForm populateForm() 
	{
		return new  WelcomePageForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/home/welcomePage.jsp");
		forwards.put("welcomePage","/home/welcomePage.jsp");
	}


	public WelcomePageController()
	{
		super(WelcomePageController.class);
	} 
	
	@RequestMapping(value ="/welcomePage/", method = RequestMethod.GET) 
	public String doExecute(@Valid @ModelAttribute("welcomePageForm") WelcomePageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
		    String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		     if(errDirect!=null){
		       request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }

			}

		ServletContext servlet =request.getSession().getServletContext();
		if ( logger.isDebugEnabled() )
			logger.debug(WelcomePageController.class.getName()+":forwarding to Welcome Page.");
		
		int contractNumber = 0;
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if(userProfile != null) {
			contractNumber = userProfile.getCurrentContract().getContractNumber();
		}
        
        WelcomePageForm welcomeForm = (WelcomePageForm) actionForm;
        
        // user has always access to the Tools page 
        boolean toolsPageAccess = true;
        welcomeForm.setToolsPageAccess(toolsPageAccess);
        
        String contractStatus = userProfile.getCurrentContract().getStatus();
        
        //TODO: Assign appropriate access based on input
        // user should not have access to the Secure Document Upload page if:
        // contract is in PS(Proposal Signed), DC(Details Completed), PC(Pending Contract Approval) or CA(Contract Approved) status
        boolean sumbitDocumentAccess = PlanConstants.CONTRACT_STATUS_WELCOME_PAGE.contains(contractStatus);
        welcomeForm.setSumbitDocumentAccess(sumbitDocumentAccess);
        

        // user has access to the Census Summary page if:
        // contract is not a defined benefit contract and
        // internal user or contract is in CA (Contract Approved) status
        boolean censusSummaryAccess = 
                !userProfile.getCurrentContract().isDefinedBenefitContract() && 
                (userProfile.isInternalUser() ||  
                		PlanConstants.CONTRACT_STATUS_WELCOME_PAGE.contains(contractStatus));
        welcomeForm.setCensusSummaryAccess(censusSummaryAccess);
        
        // user has access to the Contract Service Features page if:
        // contract is not a defined benefit contract and user is internal user
        boolean csfAccess =
                !userProfile.getCurrentContract().isDefinedBenefitContract() && 
                userProfile.isInternalUser();
        welcomeForm.setCsfAccess(csfAccess);
        
        // user has access to Plan Information page if
        // contract is not a defined benefit contract and user is internal user
        //boolean isUserContractOK = (!getUserProfile(request).isInternalUser() && !(PlanConstants.CONTRACT_STATUS_EXTERNAL_USER_ACCESS.contains(contractStatus))) ? false : true;
        boolean isContractOK = (PlanConstants.CONTRACT_STATUS_WELCOME_PAGE.contains(contractStatus)) ? true : false;

        boolean planAccess = 
                !userProfile.getCurrentContract().isDefinedBenefitContract() && 
                //isUserContractOK &&
                isContractOK;

        if (!userProfile.isInternalUser()) {
	        request.setAttribute("mcModel", MCUtils
					.getHomePageBoxModel(request, servlet, SessionHelper
							.getUserProfile(request)));
        }
        
        welcomeForm.setPlanAccess(planAccess);
        boolean fundCheckAccess = false;
        SecurityManager securityManager = SecurityManager.getInstance();
        if(securityManager != null){
        	fundCheckAccess = securityManager.isUserAuthorized(getUserProfile(request), Constants.FUNDCHECK_URL);
        }
        logger.info("fundCheckAccess value:"+fundCheckAccess);
		welcomeForm.setFundCheckAccess(fundCheckAccess);
		
		return forwards.get("welcomePage");	
		
	}
	
  @Autowired
   private PSValidatorFWInput  psValidatorFWInput;

  @InitBinder
  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
    binder.bind( request);
    binder.addValidators(psValidatorFWInput);
}


}
