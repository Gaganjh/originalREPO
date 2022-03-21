package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.HashMap;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;

/**
 * Investment Menu Action class gets required value objects , forwards to
 * investmentmenu.jsp PlanSponsor.
 * 
 * @author Chris Shin Jan 2004
 */
 @Controller
@RequestMapping(value = "/investment")

public class InvestmentMenuPageController extends PsController {

	 @ModelAttribute("dynaForm") 
	 public DynaForm populateForm()
	 { 
	return new DynaForm();
		 }

	 public static Map<String,String> forwards = new HashMap<>();
	 static{
		 forwards.put("input", "/investment/investments.jsp" );
		 forwards.put("investmentMenu", "/investment/investments.jsp" );
		 }

	 
	 
	public InvestmentMenuPageController() {
		super(InvestmentMenuPageController.class);
	}

	@RequestMapping(value ="/investments/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
 			 return "redirect:"+forward;
        	}
        }
	
		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page
		// finder
		if (userProfile.getCurrentContract() == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		// If IPS option is enabled then show the IPS assist link in Investment menu page
		InvestmentPolicyStatementVO investmentPolicyStatementVO = ContractServiceDelegate
				.getInstance().getIpsBaseData(
						userProfile.getCurrentContract().getContractNumber());
		boolean isIPSAvailable = false;
		// Set to true if at least once the service was turned on. (There will
		// be minimum of one entry in db if the service was turned on once)
		if (investmentPolicyStatementVO != null) {
			isIPSAvailable = true;
		}
		request.setAttribute("isIPSAvailable", isIPSAvailable);
		
		// If the contract is CoFid contract then show the Wilshire Adviser Report 
		// link in Investment menu page
		boolean isCoFiduciary = ContractServiceDelegate.getInstance()
				.checkCoFidContractIndicator(
						userProfile.getCurrentContract().getContractNumber());
		request.setAttribute("isCoFiduciary", isCoFiduciary);
		
		// If the Service Provider has past 24 months documents then show the
		// Wilshire Adviser Report
		// link in Investment menu page
		boolean isServiceProviderHasDocuemnts = false;
		if (/* isCoFiduciary && */userProfile.getCurrentContract().isServiceProviderHasPast24MonthsDocuments()) {
			isServiceProviderHasDocuemnts = true;
		}
		request.setAttribute("isServiceProviderHasDocuemnts", isServiceProviderHasDocuemnts);
				
		// If Fund replacement Recommendation file is available and Auto-Execute
		// is on then show the
		// Co-fiduciary fund recommendation link
				
		boolean isAutoexecuteOnAndFundRecommendationAvailable = ContractServiceDelegate.getInstance()
				.checkFundRecommendationFileAndAutoExecuteStatus(
						userProfile.getCurrentContract().getContractNumber());

				
						request.setAttribute("isAutoexecuteOnAndFundRecommendationAvailable",
								isAutoexecuteOnAndFundRecommendationAvailable);
								
		
		return forwards.get("investmentMenu");
	}
	
	/*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations 
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}