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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLoanSummary;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;

/**
 * <P>
 * This action determines the appropriate tab of the contacts page to 
 * display contact details for the given user profile.  The tab selection / suppression is 
 * based on the permission that has been granted to the logged in user profile.
 * </p>
 * 
 * @author Ranjith Kumar
 *
 */
@Controller
@RequestMapping( value = "/contacts")
@SessionAttributes({"contactsForm"})

public class ContactsController extends PsController {

	@ModelAttribute("contactsForm") 
	public ContactsForm populateForm() 
	{
		return new ContactsForm();
	}
	
	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/contacts/contacts.jsp");
		forwards.put("planSponsorContacts","/do/contacts/planSponsor/");
		forwards.put("thirdPartyAdministratorContacts","/do/contacts/thirdPartyAdministrator/");
		forwards.put("johnHancockContacts","redirect:/do/contacts/jhcontacts/");
		forwards.put("brokerContacts","redirect:/do/contacts/broker/");
		forwards.put("ContactXSS","/do/contacts/");
	}
	
	
	
	private static final String FORWARD_JOHNHANCOCK_CONTACTS_TAB = "johnHancockContacts";

	/**
	 * Default constructor
	 *
	 */
	public ContactsController() {
		super(ContactsController.class);
	}
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public ContactsController(Class clazz)
	{
		super(clazz);
	}

	/**
	 * Method doExecute will be invoked when ever user click the contact information link 
	 * from the navigation header
	 * @param mapping action mapping
	 * @param form action form
	 * @param request http servlet request
	 * @param response http servlet response
	 * @return action forward
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws SystemException
	 */
	@RequestMapping( value="/", method = {RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("contactsForm") ContactsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              forwards.get("ContactXSS");//if input forward not //available, provided default
		}
	
	
		String forwardName = null;
		UserProfile user = getUserProfile(request);
		UserRole role = user.getPrincipal().getRole();
		Contract contract = user.getCurrentContract();
		
		// if there're no current contract, forward them to the home page finder
		if(contract == null){
			return Constants.HOMEPAGE_FINDER_FORWARD;	
		}
        if (logger.isDebugEnabled()) {
			logger.debug("Is Internal User --> "  + role.isInternalUser());
			logger.debug("Is External User --> "  + role.isExternalUser());
			logger.debug("Is Plan Sponsor --> "  + role.isPlanSponsor());
			logger.debug("Is User having Selected Access --> "  + user.isSelectedAccess());
        }
		// check if the contract has TPA firm assigned		
		TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contract.getContractNumber());
		if (firmInfo != null) {
			request.setAttribute("tpaFirmAccessForContract", Boolean.TRUE);
		}
		else {
			request.setAttribute("tpaFirmAccessForContract", Boolean.FALSE);
		}
		
		// check if the contract has FR tab display		
		boolean frTabDisplay = SecurityServiceDelegate.getInstance().displayBrokerContactsTab(contract.getContractNumber());
		if (frTabDisplay) {
			request.setAttribute("displayBrokerTab", Boolean.TRUE);
		}
		else {
			request.setAttribute("displayBrokerTab", Boolean.FALSE);
		}
		
        if (logger.isDebugEnabled()) {
        	logger.debug("Tpa firm access for contract --> "  + firmInfo);
        }
        
		if(role.isInternalUser()) {
			forwardName = Constants.FORWARD_PLANSPONSOR_CONTACTS;
		}
		else if(role.isExternalUser() || (role.isPlanSponsor() && user.isSelectedAccess())) {
			forwardName = FORWARD_JOHNHANCOCK_CONTACTS_TAB;
		}
		else
			forwardName = Constants.FORWARD_PLANSPONSOR_CONTACTS;
		
		//when page linked from contact us link in navigation bar
		String queryParam = request.getParameter("parm");
		if(!StringUtils.isBlank(queryParam) && StringUtils.equals(queryParam, "contactUs")) {
			forwardName = FORWARD_JOHNHANCOCK_CONTACTS_TAB;
		}
		
		return forwards.get(forwardName);
	}
	
	
	 @Autowired
	   private PSValidatorFWLoanSummary  psValidatorFWLoanSummary;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWLoanSummary);
	}

}
