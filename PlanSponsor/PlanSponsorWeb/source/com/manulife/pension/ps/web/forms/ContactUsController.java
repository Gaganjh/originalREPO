package com.manulife.pension.ps.web.forms;

/*	Revision
	May 10 2005	RH	Added new TPA Contact Us page
*/

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWContactUS;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;


@Controller
@RequestMapping( value = "/resources/ContactUsAction")
public class ContactUsController extends PsController{
	
public ContactUsController()
{
	super(ContactUsController.class);
} 

@ModelAttribute("dynaForm") 
public DynaForm populateForm() 
{
	return new DynaForm();
	}

public static HashMap<String,String> forwards = new HashMap<String,String>();
static{
	forwards.put("contactUsPageSecure","/public/contactUsPageSecure");
	forwards.put("contactUsPageSecureNoNav","/public/contactUsPageSecureNoNav");
	forwards.put("tpaContactUs","/public/tpaContactUs");
	
}


@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {

	if(bindingResult.hasErrors()){
		HttpSession session = request.getSession(false);
		UserProfile userProfile = (UserProfile) session
				.getAttribute(Constants.USERPROFILE_KEY);

		UserInfo userInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(userProfile.getPrincipal());
		Collection firms = userInfo.getTpaFirmsAsCollection();
		String firstFirmId = null;
		for (Iterator it = firms.iterator(); it.hasNext();) {
			TPAFirmInfo firmInfo = (TPAFirmInfo) it.next();

			firstFirmId = Integer.toString(firmInfo.getId());
			break;
		}
		// Build the tpaMailToString from the first name, last name and the
		// firm id
		String tpaMailtoString = userProfile.getPrincipal().getFirstName()
				+ " " + userProfile.getPrincipal().getLastName()
				+ " - TPA Firm  " + firstFirmId;
		// Set the tpaMailtoString into the request
		request.setAttribute("firstFirmId", tpaMailtoString);
		request.removeAttribute(PsBaseUtil.ERROR_KEY);
		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		if(errDirect!=null){
			request.getSession().removeAttribute(CommonConstants.ERROR_KEY);
			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		}
	}



	if(logger.isDebugEnabled()) {
		logger.debug("entry ContactUsAction -> doExecute");
	}


	ContractSummaryVO contractSummaryVO = null;

	HttpSession session = request.getSession(false);
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);

	// if there're no current contract
	if(userProfile.getCurrentContract()!=null) {
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		contractSummaryVO = contractServiceDelegate.getContractSummary(userProfile.getCurrentContract().getContractNumber(), userProfile.getRole().isExternalUser());

		// put it in the request for now
		request.setAttribute("contractSummary",contractSummaryVO);
	} 

	if(logger.isDebugEnabled()) {
		logger.debug("exit ContactUsAction <- doExecute");
	}




	// get the first firm id if the user role is TPA
	if ( userProfile.getRole() instanceof ThirdPartyAdministrator ) {
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal());
		Collection firms = userInfo.getTpaFirmsAsCollection();
		String firstFirmId = null;
		for (Iterator it = firms.iterator(); it.hasNext();) {
			TPAFirmInfo firmInfo = (TPAFirmInfo)it.next();

			firstFirmId = Integer.toString(firmInfo.getId());
			break;
		}

		//Build the tpaMailToString from the first name, last name and the firm id
		String tpaMailtoString = userProfile.getPrincipal().getFirstName() + " " + 
				userProfile.getPrincipal().getLastName() +  " - TPA Firm  " + firstFirmId;

		//Set the tpaMailtoString into the request
		request.setAttribute("firstFirmId",tpaMailtoString );
	}


	//	  Added new ContactUs page for TPA's
	if ( userProfile.getRole() instanceof ThirdPartyAdministrator ) {
		return forwards.get("tpaContactUs");
	} else {

		if(userProfile.getCurrentContract()!=null) {	    
			return forwards.get("contactUsPageSecure");
		} else {
			return forwards.get("contactUsPageSecureNoNav");
		}
	}
}

/**
 * This code has been changed and added to Validate form and request against
 * penetration attack, prior to other validations.
 */


@Autowired
private PSValidatorFWContactUS  psValidatorFWContactUS;
@InitBinder
public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
 binder.bind( request);
 binder.addValidators(psValidatorFWContactUS);
}
	
		
		

}


