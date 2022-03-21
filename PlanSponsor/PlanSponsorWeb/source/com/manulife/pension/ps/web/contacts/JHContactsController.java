package com.manulife.pension.ps.web.contacts;

import java.io.IOException;
import java.util.HashMap;

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
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.log.LogUtility;
/**
 * Contact Management added the new class For JH Contact tab
 * @author Venkatesh Kasiraj
 *
 */


@Controller
@RequestMapping (value="/contacts")
@SessionAttributes("jhContactsForm")
public class JHContactsController extends PsController{

	/**
	 * Default Constructor
	 */
	
	/**
	 * Argumented Constructor
	 * @param clazz
	 */
	@ModelAttribute("jhContactsForm")
	public  JHContactsForm populateForm() {
		return new  JHContactsForm();
	}
	
	
	public JHContactsController() {
		super(JHContactsController.class);
	}
	

	private static final String DEFAULT_FORWARD = "default";
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put(DEFAULT_FORWARD,"/contacts/JHContacts.jsp"); 
		forwards.put("print","/contacts/JHContacts.jsp");
		forwards.put("input","/contacts/JHContacts.jsp");
	}
			
	/** 
	 * Method doExecute is used forward the JH Contacts page 
	 * @see com.manulife.pension.platform.web.controller.BaseController#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping (value="/jhcontacts/",method={RequestMethod.GET})
	public String doExecute (@Valid @ModelAttribute("jhContactsForm") JHContactsForm form,BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		final String methodName = "doExecute";
		LogUtility.logEntry(logger, methodName);
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		ContactVO contactVO = contractServiceDelegate.getContactsDetail(currentContract.getContractNumber());
		
		JHContactsForm contactsForm = (JHContactsForm)form;
		contactsForm.setContactVO(contactVO);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
				try {
					contactVO = contractServiceDelegate.getContactsDetail(currentContract.getContractNumber());
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getSession(true).setAttribute("tpaFirmAccessForContract", Boolean.TRUE);
				request.getSession(true).setAttribute("displayBrokerTab", Boolean.TRUE);
	            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		
		// if there're no current contract, forward them to the home page finder
		if(currentContract == null) {
			LogUtility.logExit(logger, methodName);
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD);
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}	
		// check if the contract has TPA firm assigned		
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(currentContract.getContractNumber());
		if (firmInfo != null) {
			request.setAttribute("tpaFirmAccessForContract", Boolean.TRUE);
		}
		else {
			logDebug("No TPA Firms Exists for contract " + currentContract.getContractNumber());
			request.setAttribute("tpaFirmAccessForContract", Boolean.FALSE);
		}
		
		// check if the contract has FR tab display		
		boolean frTabDisplay = SecurityServiceDelegate.getInstance().displayBrokerContactsTab(currentContract.getContractNumber());
		if (frTabDisplay) {
			request.setAttribute("displayBrokerTab", Boolean.TRUE);
		}
		else {
			request.setAttribute("displayBrokerTab", Boolean.FALSE);
		}
		
		
		
		//To retrieve contract passiveTrustee information
		String passiveTrustee = ContractServiceDelegate.getInstance().getPassiveTrustee(currentContract.getContractNumber()); 
		if (StringUtils.equalsIgnoreCase(Constants.NO, passiveTrustee)
				|| StringUtils.isBlank(passiveTrustee)) {
			request.setAttribute("passiveTrustee", Constants.NO_INDICATOR);
		} else {
			request.setAttribute("passiveTrustee", Constants.YES_INDICATOR);
		}
		
		//ActionForward forward = mapping.findForward(DEFAULT_FORWARD);
		String forward= forwards.get(DEFAULT_FORWARD);
		LogUtility.logExit(logger, methodName);
		return forward;
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}

}
