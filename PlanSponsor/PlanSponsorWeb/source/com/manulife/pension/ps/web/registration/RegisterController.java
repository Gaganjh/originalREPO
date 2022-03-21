package com.manulife.pension.ps.web.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.profiles.ContractAccess;
import com.manulife.pension.ps.web.profiles.ContractAccessActionHelper;
import com.manulife.pension.ps.web.profiles.ContractAccessComparator;
import com.manulife.pension.ps.web.profiles.TPAUserContractAccessActionHelper;
import com.manulife.pension.ps.web.profiles.TpaFirm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.PasscodeDeliveryPreference;
import com.manulife.pension.service.security.role.ExternalClientUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.content.GenericException;

/**
 * Handles the third step in the registration process
 * 
 * @author Tony Tomasone
 * 
 */

@Controller
@RequestMapping(value = "/registration")
@SessionAttributes({"registerForm"})

public final class RegisterController extends BaseRegisterController {
	@ModelAttribute("registerForm")
	public RegisterForm populateForm() {
		return new RegisterForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private final static String ACTION_INPUT = "input";
	private final static String ACTION_INPUT_TPA 	= "tpainput";
	static {
		forwards.put(ACTION_INPUT, "/registration/register.jsp");
		forwards.put(FORWARD_AUTHENTICATE, "redirect:/do/registration/authentication/");
		forwards.put(FORWARD_TPAAUTHENTICATE, "redirect:/do/registration/tpaauthentication/");
		forwards.put(FORWARD_TERMS, "redirect:/do/registration/terms/");
		forwards.put(FORWARD_REGISTER, "/registration/register.jsp/");
		forwards.put(FORWARD_CONFIRM, "redirect:/do/registration/confirm/");
		forwards.put(FORWARD_LOGIN, "/login/loginServlet");
		forwards.put(ACTION_INPUT_TPA, "/registration/tpaRegister.jsp");
		forwards.put(FORWARD_TPAREGISTER, "/registration/tpaRegister.jsp");
	}

	/**
	 * Constructor for RegisterAction
	 */
	public RegisterController() {
		super(AuthenticationController.class);
	}

	/**
	 * Constructor for RegisterAction
	 */
	public RegisterController(Class clazz) {
		super(clazz);
	}

	@RequestMapping(value = "/register/", method = RequestMethod.GET)
	public String doDefault(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response){
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
		// the default action just forwards to the input jsp in this case

		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		form.setPasscodeDeliveryPreference(PasscodeDeliveryPreference.SMS);
		if (!isInCorrectStep(FORWARD_REGISTER, form))
			return forwards.get(getStepForward(form));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		return forwards.get(ACTION_INPUT);
	}
	
	@RequestMapping(value = "/tparegister/", method = RequestMethod.GET)
	public String doDefaultTpa(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT_TPA);
        	}
        }
		form.setPasscodeDeliveryPreference(PasscodeDeliveryPreference.SMS);
		// the default action just forwards to the input jsp in this case

		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		if (!isInCorrectStep(FORWARD_REGISTER, form))
			return forwards.get( getStepForward(form));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		return forwards.get(ACTION_INPUT_TPA);
	}

	/**
	 * Handles the user clicking the continue button on the registration page. Uses
	 * the dynamic action framework to call this method. This method actually
	 * processes the authentication page request. It populates the UserInfo object
	 * and then calls the SecurityService to do the user registration.
	 * 
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws SystemException
	 *             ConfirmAction
	 * 
	 */
	@RequestMapping(value = "/register/", params = "action=continue", method = {RequestMethod.POST,RequestMethod.GET})
	public String doContinue(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doContinue");
		}
		if(bindingResult.hasErrors() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT);
        	}
        }
		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		if (!isInCorrectStep(FORWARD_REGISTER, form))
			return forwards.get(getStepForward(form));

		// by this time we've already passed the basic validations
		// now do the business validations
		// MPR 286. System must be able to determin that user is an existing site user
		// if the PIN entered is numeric. All users created on new site are initially
		// provided a alpha PIN.
		// 4.1.4.1.6 System verifies Data
		// MPR 287. System must display an error message if mandatory data has not been
		// entered.
		// MPR 288. System must display an error message if SSN and Contract combination
		// have already registered on new site.
		// 4.1.4.1.7 System verifies data from existing Apollo External User Management
		// tables
		// MPR 289. System must display an error message if Apollo is not available.
		// MPR 290. System must display an error message if Contract #, SSN does not
		// exist on Apollo External User Table
		// MPR 291. System must display an error if PIN entered does not match Contract
		// # and SSN on Apollo External User Table (TLP1206), and increment PIN
		// incorrect PIN count
		// MPR 292. System must display an error message if the SSN is not associated
		// with a Client. (Existing TPA users will not be able to register this way)
		// 4.1.4.1.8 System creates profile
		// MPR 293. System must set the profile status of a new profile to "New"
		// MPR 294. System must set the Password status of a new profile to "reset"
		// 4.1.4.1.10 System must set up permissions for each of Client Users contracts
		// MPR 295. System must retrieve from Apollo all of the contract numbers
		// associated with the SSN entered and set the profile up with roles and
		// permissions identified above for each of those contracts.

		// call SecurityService.register() method here

		UserInfo userInfo = new UserInfo();

		populateUserInfo(request, userInfo, form);

		if (form.getTpa()) {
			userInfo.setRole(new ThirdPartyAdministrator());
		} else {
			userInfo.setRole(new ExternalClientUser());
		}

		String forward = forwards.get(FORWARD_CONFIRM);

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		try {

			userInfo.setPassword(form.getPin());
			userInfo.setNewPassword(form.getPassword());
			String userflow = "RegisterUser";
			UserInfo newUser = service.registerUser(userInfo, Environment.getInstance().getSiteLocation(),
				IPAddressUtils.getRemoteIpAddress(request));
			 // start changes for US 44837
			HttpSession session = request.getSession(false);
		    if(null == session){
				session = request.getSession();
		    }
		    // changes for defect 8591
		    UserProfile userProfile = getUserProfile(request); 
		    Principal principal = null;
		    if(null != userProfile){
		    	principal = userProfile.getPrincipal();
		     }
		    service.updateDBTransactionPassword(principal,newUser,userflow,
                		IPAddressUtils.getRemoteIpAddress(request),session.getId());
		    // end changes for defect 8591
		    
		    // End changes for US 44837 
			form.setAccessLevel(newUser.getRole());

			if (newUser.getRole() instanceof ThirdPartyAdministrator) {
				Collection firms = newUser.getTpaFirmsAsCollection();
				for (Iterator it = firms.iterator(); it.hasNext();) {
					TPAFirmInfo firmInfo = (TPAFirmInfo) it.next();
					TpaFirm firm = new TpaFirm();

					firm.setId(new Integer(firmInfo.getId()));
					firm.setName(firmInfo.getName());
					TPAUserContractAccessActionHelper.populateContractAccess(firm.getContractAccess(0),
							firmInfo.getContractPermission());
					// firm.getContractAccess(0).setShowPayrollEmail(firmInfo.getContractPermission().isPayrollEmail());
					form.getTpaFirms().add(firm);
					form.setAccessLevel(newUser.getRole());
				}
				/*
				 * Sorts TPA firm by ID.
				 */
				Collections.sort(form.getTpaFirms(), new Comparator() {
					public int compare(Object o1, Object o2) {
						TpaFirm t1 = (TpaFirm) o1;
						TpaFirm t2 = (TpaFirm) o2;
						return t1.getId().compareTo(t2.getId());
					}
				});

			} else {
				ContractPermission[] contractPermission = newUser.getContractPermissions();
				TreeSet treeSet = new TreeSet(new ContractAccessComparator(true));
				for (int i = 0; i < contractPermission.length; i++) {
					ContractAccess contractAccess = new ContractAccess();
					ContractAccessActionHelper.populateContractAccess(contractAccess, contractPermission[i]);
					treeSet.add(contractAccess);
				}
				form.setAccessLevel(contractPermission[0].getRole());
				form.setContractAccess((ContractAccess[]) treeSet.toArray(new ContractAccess[] {}));
			}

		} catch (SecurityServiceException sse) {

			List errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(sse.getErrorCode())));

			// haven't successfully passed this step
			// registerForm.setAuthenticateValid(false);

			// setErrorsInRequest(request, errors);
			SessionHelper.setErrorsInSession(request, errors);

			// send them back to the input page if we get a
			// SecurityServiceException
			forward = forwards.get(ACTION_INPUT);
		}catch(RemoteException e){
        	throw new SystemException(e, "com.manulife.pension.ps.web.registration.RegisterController", "doContinue", "Exception not handled: " + e.toString());
        }


		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doContinue");
		}
		return forward;
	}
	@RequestMapping(value = "/tparegister/", params = "action=continue", method = RequestMethod.POST)
	public String doContinueTpa(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException, RemoteException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doTpaContinue");
		}
		if(bindingResult.hasErrors() || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null || (null!= request.getSession(false).getAttribute("Deapi") && request.getSession(false).getAttribute("Deapi").equals("down"))){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(ACTION_INPUT_TPA);
        	}
        }
		// if someone is trying to jump ahead in the
		// registration wizard, send them to the right page/step
		if (!isInCorrectStep(FORWARD_REGISTER, form))
			return forwards.get(getStepForward(form));

		// by this time we've already passed the basic validations
		// now do the business validations
		// MPR 286. System must be able to determin that user is an existing site user
		// if the PIN entered is numeric. All users created on new site are initially
		// provided a alpha PIN.
		// 4.1.4.1.6 System verifies Data
		// MPR 287. System must display an error message if mandatory data has not been
		// entered.
		// MPR 288. System must display an error message if SSN and Contract combination
		// have already registered on new site.
		// 4.1.4.1.7 System verifies data from existing Apollo External User Management
		// tables
		// MPR 289. System must display an error message if Apollo is not available.
		// MPR 290. System must display an error message if Contract #, SSN does not
		// exist on Apollo External User Table
		// MPR 291. System must display an error if PIN entered does not match Contract
		// # and SSN on Apollo External User Table (TLP1206), and increment PIN
		// incorrect PIN count
		// MPR 292. System must display an error message if the SSN is not associated
		// with a Client. (Existing TPA users will not be able to register this way)
		// 4.1.4.1.8 System creates profile
		// MPR 293. System must set the profile status of a new profile to "New"
		// MPR 294. System must set the Password status of a new profile to "reset"
		// 4.1.4.1.10 System must set up permissions for each of Client Users contracts
		// MPR 295. System must retrieve from Apollo all of the contract numbers
		// associated with the SSN entered and set the profile up with roles and
		// permissions identified above for each of those contracts.

		// call SecurityService.register() method here

		UserInfo userInfo = new UserInfo();

		populateUserInfo(request, userInfo, form);

		if (form.getTpa()) {
			userInfo.setRole(new ThirdPartyAdministrator());
		} else {
			userInfo.setRole(new ExternalClientUser());
		}

		String forward = forwards.get(FORWARD_CONFIRM);

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		try {

			userInfo.setPassword(form.getPin());
			userInfo.setNewPassword(form.getPassword());
			UserInfo newUser = service.registerUser(userInfo, Environment.getInstance()
					.getSiteLocation(), IPAddressUtils.getRemoteIpAddress(request));
			form.setAccessLevel(newUser.getRole());
			 // start changes for US 44837
			String userflow = "RegisterTPAUser";
			HttpSession session = request.getSession(false);
		    if(null == session){
				session = request.getSession();
		    }
		    // changes for defect 8591
		    UserProfile userProfile = getUserProfile(request); 
		    Principal principal = null;
		    if(null != userProfile){
		    	principal = userProfile.getPrincipal();
		    }
		    service.updateDBTransactionPassword(principal,newUser,userflow,
            		   IPAddressUtils.getRemoteIpAddress(request),session.getId());
		    // end changes for 8591
		    
		    // End changes for US 44837 

			if (newUser.getRole() instanceof ThirdPartyAdministrator) {
				Collection firms = newUser.getTpaFirmsAsCollection();
				for (Iterator it = firms.iterator(); it.hasNext();) {
					TPAFirmInfo firmInfo = (TPAFirmInfo) it.next();
					TpaFirm firm = new TpaFirm();

					firm.setId(new Integer(firmInfo.getId()));
					firm.setName(firmInfo.getName());
					TPAUserContractAccessActionHelper.populateContractAccess(firm.getContractAccess(0),
							firmInfo.getContractPermission());
					// firm.getContractAccess(0).setShowPayrollEmail(firmInfo.getContractPermission().isPayrollEmail());
					form.getTpaFirms().add(firm);
					form.setAccessLevel(newUser.getRole());
				}
				/*
				 * Sorts TPA firm by ID.
				 */
				Collections.sort(form.getTpaFirms(), new Comparator() {
					public int compare(Object o1, Object o2) {
						TpaFirm t1 = (TpaFirm) o1;
						TpaFirm t2 = (TpaFirm) o2;
						return t1.getId().compareTo(t2.getId());
					}
				});

			} else {
				ContractPermission[] contractPermission = newUser.getContractPermissions();
				TreeSet treeSet = new TreeSet(new ContractAccessComparator(true));
				for (int i = 0; i < contractPermission.length; i++) {
					ContractAccess contractAccess = new ContractAccess();
					ContractAccessActionHelper.populateContractAccess(contractAccess, contractPermission[i]);
					treeSet.add(contractAccess);
				}
				form.setAccessLevel(contractPermission[0].getRole());
				form.setContractAccess((ContractAccess[]) treeSet.toArray(new ContractAccess[] {}));
			}

		} catch (SecurityServiceException sse) {

			List errors = new ArrayList();
			errors.add(new GenericException(Integer.parseInt(sse.getErrorCode())));

			// haven't successfully passed this step
			// registerForm.setAuthenticateValid(false);

			// setErrorsInRequest(request, errors);
			SessionHelper.setErrorsInSession(request, errors);

			// send them back to the input page if we get a
			// SecurityServiceException
			forward = forwards.get(ACTION_INPUT_TPA);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doContinue");
		}
		return forward;
	}

	
	@Autowired
	private RegisterValidator registerValidator;
	 @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
		    binder.bind(request);
		    binder.addValidators(registerValidator);
		}
	
	 @RequestMapping(value ="/register/ajaxvalidator/" ,method =  {RequestMethod.POST})
		@ResponseBody
		public String doDeApiAjaxCall(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult,
	           HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException,SecurityServiceException, IOException{
		 final String jsonObj = IOUtils.toString(request.getInputStream());
			String jsonText2 = "[" + jsonObj + "]";
			JsonParser parser = new JsonParser();
			JsonArray jsonArr = (JsonArray) parser.parse(jsonText2);
			String newpassword  = null;
			String userName = null;
			if (StringUtils.isNotEmpty(jsonObj)) {
				for (int i = 0; i < jsonArr.size(); i++) {

					JsonObject obj = (JsonObject) jsonArr.get(i);

				newpassword = (obj.get("newPassword")).getAsString();
				userName = (obj.get("userName")).getAsString();
				}
			}
			String password  = newpassword;
			String responseText = StringUtils.EMPTY;
			SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			//Passing Boolean value as False for only FRW External Users to validate
			String errors = service.passwordStrengthValidation(password,userName,Boolean.FALSE); 
			responseText = errors.toString();
			response.setContentLength(responseText.length());
	    	response.setContentType("application/json");
	    	PrintWriter out = response.getWriter();
	        out.print(responseText);
	        out.flush();
			
			return null;
		}
	 
	
}
