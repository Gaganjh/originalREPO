package com.manulife.pension.ps.web.contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
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
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * Edit contract contact addresses action class 
 * @author Ranjith Kumar
 *
 */

@Controller
@RequestMapping( value = "/editContractAddress")
@SessionAttributes({"editContractContactAddressForm"})
public class EditContractContactAddressController extends PsAutoController {

	
	@ModelAttribute("editContractContactAddressForm") 
	public EditContractContactAddressForm populateForm()
	{
		return new EditContractContactAddressForm();
		
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/contacts/editContractAddress.jsp" );
		forwards.put("input","/contacts/editContractAddress.jsp" );
	    forwards.put("save","redirect:/do/contacts/planSponsor/" );
	    forwards.put("cancel","redirect:/do/contacts/planSponsor/" );
	    forwards.put("planSponsorContacts","redirect:/do/contacts/planSponsor/" );
	
	}
	
	 private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
	 private ServiceLogRecord logRecord = new ServiceLogRecord("EditContractContactAddressAction");
	/**
	 * Default constructor
	 */
	public EditContractContactAddressController() {
		super(EditContractContactAddressController.class);
	}

 @RequestMapping(value = "/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("editContractContactAddressForm") EditContractContactAddressForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	 throws IOException,ServletException, SystemException {	
	
	if(bindingResult.hasErrors()){
        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       if(errDirect!=null){
              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       }
	}
	
		if (logger.isDebugEnabled()) {
			logger.debug("Entry -> doDefault");
		}

		
		UserProfile loggedUserProfile = getUserProfile(request);
		Contract contract = loggedUserProfile.getCurrentContract();

		// get the first client contact details
		actionForm.setFirstPointOfContact(FirstPointOfContactHelper
				.getFirstPointOfContact(contract.getContractNumber()));
		// To retrieve contract passiveTrustee information
		Map<String, String> passiveTrusteeOptions = new TreeMap<String, String>();
		passiveTrusteeOptions.put(Constants.NO, Constants.NO_INDICATOR);
		passiveTrusteeOptions.put(Constants.YES, Constants.YES_INDICATOR);
		actionForm.setPassiveTrusteeOptions(passiveTrusteeOptions);
		String passiveTrustee = ContractServiceDelegate.getInstance()
				.getPassiveTrustee(contract.getContractNumber());
		// added oldPassiveTrustee information
		actionForm.setoldPassiveTrustee(passiveTrustee);

		if (StringUtils.equalsIgnoreCase(Constants.NO, passiveTrustee)
				|| StringUtils.isBlank(passiveTrustee)) {
			actionForm.setPassiveTrustee(Constants.NO);
		} else {
			actionForm.setPassiveTrustee(passiveTrustee);
		}
		// obtain the lock on contract contact address
		if (EditContractContactAddressActionHelper
				.obtainLock(contract, request)) {
			// Contact address
			Collection<Address> contractAddresses = ContractServiceDelegate
					.getInstance().getContractAddresses(
							contract.getContractNumber());

			EditContractContactAddressActionHelper.populateAddressesInForm(
					contractAddresses, actionForm);

			if (actionForm.getStates() == null) {
				List<LabelValueBean> states = EditContractContactAddressActionHelper
						.getUSStates();
				actionForm.setStates(states);
				// request.setAttribute("states", states);
			}
		} else {
			// If addresses already locked by another Internal user.
			try {
				Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(
						LockHelper.CONTACT_ADDRESS_LOCK_NAME,
						LockHelper.CONTACT_ADDRESS_LOCK_NAME
								+ contract.getContactId());

				UserInfo lockOwnerUserInfo = SecurityServiceDelegate
						.getInstance().searchByProfileId(
								loggedUserProfile.getPrincipal(),
								lockInfo.getLockUserProfileId());

				String lockOwnerDisplayName = LockHelper
						.getLockOwnerDisplayName(loggedUserProfile,
								lockOwnerUserInfo);

				Collection<GenericException> errors = new ArrayList<GenericException>();
				errors.add(new GenericException(2399,
						new String[] { lockOwnerDisplayName }));

				SessionHelper.setErrorsInSession(request, errors);

				return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
			} catch (SecurityServiceException e) {
				throw new SystemException(
						e,
						"com.manulife.pension.ps.web.contacts.EditContractContactAddressAction."
								+ "doDefault()"
								+ "Failed to get the lock on contract addresseses "
								+ e.toString());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exit -> doDefault");
		}
		return forwards.get("default");
	}

	/**
	 * To save the updated addresses
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws ApplicationException
	 */
 @RequestMapping(value = "/",params= {"action=save"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave(@Valid @ModelAttribute("editContractContactAddressForm") EditContractContactAddressForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	 throws IOException,ServletException, SystemException {	
	
	 if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Entry -> doSave");
		}

		
		UserProfile loggedUserProfile = getUserProfile(request);
		Contract contract = loggedUserProfile.getCurrentContract();
		//ME - CL -122023 - Allow CAR user can save the address records
		boolean nonIS_InternalUser = getUserProfile(request).isInternalUser() && !getUserProfile(request).isInternalServicesCAR();
		if (!nonIS_InternalUser) {
			// Update the addresses
			ContractServiceDelegate.getInstance().updateContractAddresses(
					loggedUserProfile.getPrincipal(),
					contract.getContractNumber(),
					EditContractContactAddressActionHelper
							.getUserUpdatedAddresses(actionForm),
					loggedUserProfile.getPrincipal().getProfileId());
		}
		
		
		// To update contract passiveTrustee information
        // changes made for CR16
		boolean isInternalServiceCar = getUserProfile(request).isInternalServicesCAR();
		if(isInternalServiceCar) {
			if(!StringUtils.equalsIgnoreCase(actionForm.getPassiveTrustee(), actionForm.getoldPassiveTrustee())) {
				SecurityServiceDelegate.getInstance().updatePassiveTrusee(
						loggedUserProfile.getPrincipal(), loggedUserProfile.getRole(),
						contract.getContractNumber(),
						actionForm.getoldPassiveTrustee(),
						actionForm.getPassiveTrustee());
			}
		}
		

		// update the FCC
		FirstPointOfContactHelper.saveFirstPointOfContact(actionForm
				.getFirstPointOfContact(), loggedUserProfile);
		// Release the lock
		if (actionForm.getContactAddresses() != null) {
			EditContractContactAddressActionHelper.releaseLock(contract,
					request);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exit -> doSave");
		}
		return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
	}

	/**
	 * To cancel the edit contract contact addresses and forward to PS contact tab
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
 
 @RequestMapping(value = "/",params= {"action=cancel"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCancel(@Valid @ModelAttribute("editContractContactAddressForm") EditContractContactAddressForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	 throws IOException,ServletException, SystemException {	
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry -> doCancel");
		}

		UserProfile loggedUserProfile = getUserProfile(request);
		Contract contract = loggedUserProfile.getCurrentContract();

		// Release the lock on conttract contact address
		EditContractContactAddressActionHelper.releaseLock(contract, request);

		if (logger.isDebugEnabled()) {
			logger.debug("Exit -> doCancel");
		}

		return forwards.get(Constants.FORWARD_PLANSPONSOR_CONTACTS);
	}

	/**
	 * Valides the user input data.
	 */
	@Autowired
	private EditContractContactAddressValidator editContractContactAddressValidator;
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(editContractContactAddressValidator);
	}
}
