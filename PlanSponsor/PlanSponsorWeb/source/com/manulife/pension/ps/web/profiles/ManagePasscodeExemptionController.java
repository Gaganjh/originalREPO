package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.IllegalPasscodeExemptException;
import com.manulife.pension.service.security.passcode.PasscodeExemptInfo;
import com.manulife.pension.service.security.role.PilotCAR;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;



@Controller
@RequestMapping( value ="/passcode")
@SessionAttributes({"managePasscodeExemptionForm"})

public class ManagePasscodeExemptionController extends PsAutoController {
	@ModelAttribute("managePasscodeExemptionForm") 
	public ManagePasscodeExemptionForm populateForm()
	{
		return new ManagePasscodeExemptionForm();
		}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static final String REMOVE = "remove";

	private static final String EXEMPT = "exempt";

	private static final String EXEMPT_CONFIRM = "exemptConfirm";

	private static final String REMOVE_CONFIRM = "removeConfirm";

	private static final String FINISH = "finish";

	private static final String BACK = "back";
	private static final String INPUT = "input";

	private static final String FROM_PS_CONTRACT_TAB = "fromPSContactTab";

	private static final String TPA_CONTRACTS_TAB = "tpaContactsTab";
	private static final String FROM_TPA_CONTRACTS_TAB = "fromTPAContactsTab";

	private static final String DATE_FORMATTER = "MMM dd, yyyy HH:mm:ss";

	private static final String SPACE = " ";
	static {
		forwards.put(INPUT, "/password/passcodeExempt.jsp");
		forwards.put(EXEMPT, "/password/passcodeExempt.jsp");
		forwards.put(BACK, "redirect:/do/contacts/planSponsor/?lastVisited=true");
		forwards.put(TPA_CONTRACTS_TAB, "redirect:/do/contacts/thirdPartyAdministrator/");
		forwards.put(REMOVE, "/password/passcodeExemptRemove.jsp");
		forwards.put(FINISH, "redirect:/do/contacts/planSponsor/?lastVisited=true");
		forwards.put(EXEMPT_CONFIRM, "/password/passcodeExemptConfirm.jsp");
		forwards.put(REMOVE_CONFIRM, "/password/passcodeExemptRemoveConfirm.jsp");
	}

	/**
	 * Constructor.
	 */
	public ManagePasscodeExemptionController() {
		super();
	}

		@RequestMapping(value ="/managePasscodeExempt/" , method =  RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doDefault");
		}
		
		try {
			String profileId = request.getParameter("profileId");
			String userName = StringUtils.EMPTY;
			userName = request.getParameter("userName");

			if (StringUtils.isBlank(profileId) && StringUtils.isBlank(userName)) {
				return forwards.get(FINISH);
			}

			if (StringUtils.isBlank(userName)
					&& !StringUtils.isBlank(profileId)) {
				userName = SecurityServiceDelegate.getInstance()
						.getLDAPUserNameByProfileId(Long.valueOf(profileId));
			}

			final UserInfo userInfo = getUserInfoInstance(userName,
					getUserProfile(request).getPrincipal());

			if (userInfo != null) {
				request.setAttribute(Constants.USERINFO_KEY, userInfo);

			}
			ManagePasscodeExemptionForm form = (ManagePasscodeExemptionForm) actionForm;

			form.resetForm();
			PasscodeExemptInfo passcodeExemptInfo;
			if (userInfo != null
					&& userInfo.getProfileId() != 0
					&& getUserProfile(request).getPrincipal().getRole() instanceof PilotCAR) {
				passcodeExemptInfo = SecurityServiceDelegate.getInstance()
						.getPasscodeExemptInfo(userInfo.getProfileId());
			} else {
				return forwards.get(FINISH);
			}

			populateForm(request, form, userInfo, passcodeExemptInfo);

			form.setUserRole(getUserRole(request, userInfo));

			if (passcodeExemptInfo == null) {
				return forwards.get(EXEMPT);
			} else {
				request.setAttribute(
						ManagePasscodeExemptionForm.EXEMPTINFO_KEY,
						passcodeExemptInfo);
			}

			if (logger.isDebugEnabled()) {
				logger.debug("exit <-- doDefault");
			}
			return forwards.get(REMOVE);

		} catch (SecurityServiceException e){
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
		}
		return forwards.get(INPUT);

	}
	@RequestMapping(value ="/managePasscodeExempt/" ,params="action=exempt"   , method =  RequestMethod.POST) 
	public String doExempt (@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 


		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doExempt");
		}
		Principal principal= getUserProfile(request).getPrincipal();

		final UserInfo userInfo = populateUserInfo(form);
		request.setAttribute(Constants.USERINFO_KEY, userInfo);

		List<GenericException> errors;
		errors = validateExemptRequest( form, request);

		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
			request.setAttribute(Constants.USERINFO_KEY, userInfo);
			return forwards.get(EXEMPT);
		}

		PasscodeExemptInfo passcodeExemptInfo = populatePasscodeInfo(form,
				request, userInfo);

		try {
			SecurityServiceDelegate.getInstance().exemptPasscode(
					passcodeExemptInfo,principal);
		} catch (IllegalPasscodeExemptException e) {
			errors.add(new ValidationError(ManagePasscodeExemptionForm.FIELD_EXEMPTION_REASON,
					ErrorCodes.ERROR_ALREADY_EXEMPTED, Type.error));
			if (!errors.isEmpty()) {
				setErrorsInSession(request, errors);
				return forwards.get(EXEMPT);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doExempt");
		}
		return forwards.get(EXEMPT_CONFIRM);

	}

	@RequestMapping(value ="/managePasscodeExempt/",params="action=printPDF"  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
	       String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	public List<GenericException> validateExemptRequest(
			ActionForm form, HttpServletRequest request) {
		ManagePasscodeExemptionForm managePasscodeExemptionForm = (ManagePasscodeExemptionForm) form;

		List<GenericException> errorCodes = new ArrayList<GenericException>();
		BaseSessionHelper.removeErrorsInSession(request);

		if (StringUtils.isBlank(managePasscodeExemptionForm
				.getExemptionReason())) {
			errorCodes.add(new ValidationError(
					ManagePasscodeExemptionForm.FIELD_EXEMPTION_REASON,
					ErrorCodes.ERROR_EXEMPTION_REASON_MANDATORY, Type.error));
		}
		if (managePasscodeExemptionForm.getExemptionType().equalsIgnoreCase(
				ManagePasscodeExemptionForm.TEMPORARY)
				&& StringUtils.isBlank(managePasscodeExemptionForm
						.getPpmTicket())) {
			errorCodes.add(new ValidationError(
					ManagePasscodeExemptionForm.FIELD_PPMTICKET,
					ErrorCodes.ERROR_PPM_TICKET_NUMBER_MANDATORY, Type.error));
		}
		if (StringUtils.isBlank(managePasscodeExemptionForm
				.getExemptRequestedName())) {
			errorCodes.add(new ValidationError(
					ManagePasscodeExemptionForm.FIELD_EXEMPTION_REQUESTEDBY,
					ErrorCodes.ERROR_EXEMPTION_REQUESTED_BY_MANDATORY,
					Type.error));
		}

		return errorCodes;
	}

	@RequestMapping(value ="/managePasscodeExempt/", params="action=remove", method =  RequestMethod.POST) 
	public String doRemoveExempt (@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 

		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doRemoveExempt");
		}
		Principal principal= getUserProfile(request).getPrincipal();
		List<GenericException> errors = new ArrayList<GenericException>();

		final UserInfo userInfo = populateUserInfo(form);
		request.setAttribute(Constants.USERINFO_KEY, userInfo);

		PasscodeExemptInfo passcodeExemptInfo = SecurityServiceDelegate
				.getInstance().getPasscodeExemptInfo(userInfo.getProfileId());

		passcodeExemptInfo.setRemovedByProfileId(getUserProfile(request)
				.getPrincipal().getProfileId());

		Boolean removeStatus = SecurityServiceDelegate.getInstance()
				.removePasscodeExempt(passcodeExemptInfo,principal);

		if (!removeStatus) {
			errors.add(new ValidationError(ManagePasscodeExemptionForm.FIELD_EXEMPTION_REASON,
					ErrorCodes.ERROR_ALREADY_REMOVED_EXEMPTION, Type.error));
			if (!errors.isEmpty()) {
				setErrorsInSession(request, errors);
				return forwards.get(EXEMPT);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doRemoveExempt");
		}
		return forwards.get(REMOVE_CONFIRM);

	}

	@RequestMapping(value ="/managePasscodeExempt/", params="action=finish"  , method =  RequestMethod.POST) 
	public String doFinish (@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response){

		
		return forwards.get(FINISH);

	}
	@RequestMapping(value ="/managePasscodeExempt/", params="action=back", method =  RequestMethod.POST) 
	public String doBack (@Valid @ModelAttribute("managePasscodeExemptionForm") ManagePasscodeExemptionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response){
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        } 
	
		if (form.isFromTPAContactsTab()) {
			return forwards.get(TPA_CONTRACTS_TAB);
		}

		return forwards.get(BACK);

	}

	protected void populateForm(HttpServletRequest request,
			ManagePasscodeExemptionForm form, UserInfo userInfo,
			PasscodeExemptInfo passcodeExemptInfo) {

		form.setUserName(userInfo.getUserName());
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		form.setEmail(userInfo.getEmail());
		form.setUserProfileId(userInfo.getProfileId());
		form.setMobile(MobileMask.maskPhone(userInfo.getMobileNumber().toString()));
		
		form.setFromPSContactTab(request.getParameter(FROM_PS_CONTRACT_TAB) != null
				&& request.getParameter("fromPSContactTab").equalsIgnoreCase(
						"true") ? true : false);

		form.setFromTPAContactsTab(request.getParameter(FROM_TPA_CONTRACTS_TAB) != null
				&& request.getParameter(FROM_TPA_CONTRACTS_TAB).equalsIgnoreCase(
						"true") ? true : false);

		if (passcodeExemptInfo != null) {
			form.setExemptionType(passcodeExemptInfo.getTypeCode());
			form.setExemptionRequestedBy(passcodeExemptInfo
					.getRequestedByName());
			form.setPpmTicket(passcodeExemptInfo.getPpmNumber());
			form.setExemptTimeStamp(new SimpleDateFormat(DATE_FORMATTER)
					.format(passcodeExemptInfo.getExemptionCreatedTs()));
			form.setExemptProccessedBy(String.valueOf(passcodeExemptInfo
					.getCreatedByProfileId()));
			form.setExemptProccessedByName(passcodeExemptInfo
					.getCreatedByFirstName()
					+ SPACE + passcodeExemptInfo.getCreatedByLastName());

		}
	}

	protected UserInfo populateUserInfo(ManagePasscodeExemptionForm form) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(form.getUserName());
		userInfo.setFirstName(form.getFirstName());
		userInfo.setLastName(form.getLastName());
		userInfo.setEmail(form.getEmail());
		userInfo.setProfileId(form.getUserProfileId());
		return userInfo;
	}

	protected PasscodeExemptInfo populatePasscodeInfo(
			ManagePasscodeExemptionForm form, HttpServletRequest request,
			UserInfo userInfo) {
		PasscodeExemptInfo passcodeExemptInfo = new PasscodeExemptInfo();
		passcodeExemptInfo.setCreatedByProfileId(getUserProfile(request)
				.getPrincipal().getProfileId());
		passcodeExemptInfo.setExemptionReason(form.getExemptionReason());
		passcodeExemptInfo.setRequestedByName(form.getExemptRequestedName());
		passcodeExemptInfo.setPpmNumber(form.getPpmTicket());
		passcodeExemptInfo.setProfileId(userInfo.getProfileId());
		passcodeExemptInfo.setTypeCode(form.getExemptionType());
		return passcodeExemptInfo;
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
	
	private static UserInfo getUserInfoInstance(final String userName,
			final Principal initiator) throws SecurityServiceException,
			SystemException {

		UserInfo userInfo = null;

		if (StringUtils.isNotBlank(userName)) {
			userInfo = SecurityServiceDelegate.getInstance().searchByUserName(
					initiator, userName);
		}

		if (userInfo != null) {
			userInfo.setPasscodeInfo(SecurityServiceDelegate.getInstance()
					.getUserPasscodeInfo(userInfo.getProfileId()));
		}

		return userInfo;

	}

	private static String getUserRole(HttpServletRequest request,
			final UserInfo userInfo) throws SystemException {
		String userRole = null;

		UserProfile loginUserProfile = getUserProfile(request);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(loginUserProfile.getPrincipal());

		List<Integer> cannotManageRoleContracts = new ArrayList<Integer>();
		List<ClientUserContractAccess> list = ClientUserContractAccessActionHelper
				.buildContractAccesses(loginUserProfile, loginUserInfo,
						userInfo, cannotManageRoleContracts);
		for (ClientUserContractAccess clientUserContractAccess : list) {
			userRole = clientUserContractAccess.getPlanSponsorSiteRole()
					.getLabel();
		}

		return userRole;
	}

	
}