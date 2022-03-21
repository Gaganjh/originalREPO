package com.manulife.pension.ps.web.profiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.broker.valueobject.RegionalVicePresident;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.RelationshipManagerVO;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"deleteProfileForm"})

public class DeleteProfileController  extends PsAutoController {
	@ModelAttribute("deleteProfileForm") 
	public DeleteProfileForm populateForm() 
	{
		return new DeleteProfileForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/deleteUser.jsp");
		forwards.put("deletePage","redirect:/do/profiles/deleteProfile/?action=confirm");
		forwards.put("viewPermissions","redirect:/do/profiles/userPermissions/?fromPage=delete");
		forwards.put("refresh","redirect:/do/profiles/deleteProfile/?action=refresh");
		forwards.put("confirmation","/profiles/deleteUserConfirm.jsp"); 
		forwards.put("planSponsorContacts","redirect:/do/contacts/planSponsor/");
		forwards.put("manageTPAUsers","redirect:/do/profiles/manageTpaUsers/"); 
		forwards.put("manageInternalUsers","redirect:/do/profiles/manageInternalUsers/");
		}
	

	private static final String DELETE_USER = "deletePage";
	private static final String VIEW_PERMISSIONS = "viewPermissions";
	private static final String CONFIRMATION_PAGE = "confirmation";
	private static final String MANAGE_INTERNAL_USERS = "internal";
	private static final String MANAGE_TPA_USERS = "manageTPAUsers";
	private static final String REFRESH = "refresh";

	private static SecurityServiceDelegate service = SecurityServiceDelegate
			.getInstance();

	/**
	 * Constructor for DeleteProfileAction
	 */
	public DeleteProfileController() {
		super();
	}

	/**
	 * Constructor for DeleteProfileAction
	 */
	public DeleteProfileController(Class clazz) {
		super(clazz);
	}

	@RequestMapping(value ="/deleteProfile/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(actionForm, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		if(actionForm.getProfileId() > 0){
			actionForm.setUserName(null);
		}
		String forward = forwards.get("input");

		DeleteProfileForm form = (DeleteProfileForm) actionForm;
		request.getSession().setAttribute("userProfileForm", form);

		if (form.getAction().equals(REFRESH)) {
			return forwards.get(REFRESH);
		}

		String userName = form.getUserName();
		UserProfile loggedInUserProfile = getUserProfile(request);
		Principal loggedInUserPrincipal = loggedInUserProfile.getPrincipal();
		form.clear( request);

		UserInfo userInfo = ClientUserContractAccessActionHelper
				.getManagedUserInfo(loggedInUserPrincipal, userName);
		
		long userProfileId = userInfo == null ? 0 : userInfo.getProfileId();
		Collection<GenericException> errors = ClientUserContractAccessActionHelper
				.checkForLockOrDelete(loggedInUserProfile, userProfileId,
						userName);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
		}

		if (userInfo == null) {
			forward = forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
		} else {
			int contractNumber = 0;
			if (loggedInUserProfile.getCurrentContract() != null) {
				contractNumber = loggedInUserProfile.getCurrentContract()
						.getContractNumber();
			}

			populateForm(request, form, userInfo, contractNumber);
		}

		return forward;
	}

	protected void populateForm(HttpServletRequest request,
			DeleteProfileForm form, UserInfo userInfo, int contractNumber)
			throws SystemException {
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		UserProfile loginUserProfile = getUserProfile(request);

		form.setProfileId(userInfo.getProfileId());
		form.setUserName(userInfo.getUserName());
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		form.setEmail(userInfo.getEmail());
		form.setSecondaryEmail(userInfo.getSecondaryEmail());
		form.setMobileNumber(MobileMask.maskPhone(userInfo.getMobileNumber().toString()));
		form.setTelephoneNumber(userInfo.getPhoneNumber());
		form.setTelephoneExtension(userInfo.getPhoneExtension());
		form.setFaxNumber(userInfo.getFax());
		form.setCommentDetails(userInfo.getContactComment());
		form.setEmployeeNumber(userInfo.getEmployeeNumber());
		form.setPasswordState(userInfo.getPasswordState());
		form.setProfileStatus(userInfo.getProfileStatus());
		form
				.setWebAccess(userInfo.isWebAccessInd() ? DeleteProfileForm.FORM_YES_CONSTANT
						: DeleteProfileForm.FORM_NO_CONSTANT);
		form.setUserRole(userInfo.getRole());
		form.setInternalUser(userInfo.getRole() instanceof InternalUser);
		form.setParticipantRole(userInfo.getParticipantRole());
		if (userInfo.getRole() != null) {
			form.setPlanSponsorSiteRole(userInfo.getRole().toString());
			form.setAccess408DisclosureRegen(userInfo.getRole().hasPermission(PermissionType.REGEN_408_DISCLOSURE)?"Yes":"No");
			form.setAccessIPIHypotheticalTool(userInfo.getRole().hasPermission(PermissionType.IPI_HYPOTHETICAL_TOOL)?"Yes":"No");
		}
		if (userInfo.getRole() instanceof InternalUser) {
			BDUserRole bdRole = userInfo.getBdUserRole();
			if (bdRole == null) {
				form.setBrokerDealerSiteRole(AccessLevelHelper.NO_ACCESS);
			} else {
				BDUserRoleType roleType = bdRole.getRoleType();
				form.setBrokerDealerSiteRole(roleType.getUserRoleCode());
				if (BDUserRoleType.RVP.compareTo(roleType) == 0) {
					RegionalVicePresident rvpEntity = ((BDRvp) bdRole).getRvpEntity();
					String rvpId = rvpEntity == null ? "" : Long.toString(rvpEntity.getId());
					if (StringUtils.isEmpty(rvpId)) {
						form.setRvpDisplayName("");
					} else {
						List<LabelValueBean> rvpList = ManageInternalUserHelper.getRVPs();
						for (LabelValueBean rvp : rvpList) {
							if (rvp.getValue().equals(rvpId)) {
								form.setRvpDisplayName(rvp.getLabel());
								break;
							}
						}
					}
				}
				form.setBdLicenceVerified(ManageInternalUserHelper
						.getLicenseVerifiedDisplay(userInfo
								.getBdLicenseVerified()));
			}
			
			if (userInfo.getRole() != null) {
				form.setPlanSponsorSiteRole(userInfo.getRole().toString());
				String psRole = form.getPlanSponsorSiteRole();
				if (RelationshipManager.ID.equals(psRole)) {
					RelationshipManagerVO relationshipManagerVO = userInfo.getPswRole();
					String rmId = relationshipManagerVO == null ? "" : relationshipManagerVO.getPartyId();
					if (StringUtils.isEmpty(rmId)) {
						form.setRmDisplayName("");

					} else {
						List<LabelValueBean> rmList = ManageInternalUserHelper.getRMs();
						for (LabelValueBean rmLablevalueBean : rmList) {
							if (rmLablevalueBean.getValue().equals(rmId)) {
								form.setRmDisplayName(rmLablevalueBean.getLabel());
								break;
							}

						}
					}
					
				} 
			}
		}

		String ssn = userInfo.getSsn();

		if (ssn != null && ssn.length() > 0) {
			form.setSsn(new Ssn(ssn));
		}

		form.setContractAccesses(ClientUserContractAccessActionHelper
				.buildContractAccesses(loginUserProfile, loginUserInfo,
						userInfo));

		if (form.getContractAccesses().size() == userInfo
				.getContractPermissions().length
				&& ClientUserContractAccessActionHelper.canManageAllContracts(
						loginUserInfo, userInfo)) {
			form.setCanManageAllContracts(true);
		} else {
			form.setCanManageAllContracts(false);
		}

		ContractPermission[] permissions = userInfo.getContractPermissions();

		// Sort contracts by contract number
		Arrays.sort(permissions, new Comparator() {
			public int compare(Object o1, Object o2) {
				ContractPermission bean1 = (ContractPermission) o1;
				ContractPermission bean2 = (ContractPermission) o2;
				Integer contractNumber1 = new Integer(bean1.getContractNumber());
				Integer contractNumber2 = new Integer(bean2.getContractNumber());
				return contractNumber1.compareTo(contractNumber2);
			}
		});

		StringBuffer buff = new StringBuffer();
		StringBuffer buff2 = new StringBuffer();

		for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) {
			ClientUserContractAccess contractAccess = (ClientUserContractAccess) it
					.next();
			if (contractAccess.isLastUserWithSubmissionsAccess()) {
				if (buff.length() > 0) {
					buff.append(",");
				}
				buff.append(contractAccess.getContractNumber());
			}

			if (contractAccess.isLastUserWithManageUsers()) {
				if (buff2.length() > 0) {
					buff2.append(",");
				}
				buff2.append(contractAccess.getContractNumber());
			}
		}

		form.setIfileLastContractList(buff.toString());
		form.setLastPseumContractList(buff2.toString());

		TPAFirmInfo[] tpaFirms = userInfo.getTpaFirms();

		buff = new StringBuffer();
		StringBuffer buffLastIloansEmail = new StringBuffer();
		StringBuffer buffLastIloansEmailAndTpaStaffPlan = new StringBuffer();
		StringBuffer buffListLastTPAUM = new StringBuffer();

		if (tpaFirms.length > 0) {
			for (int i = 0; i < tpaFirms.length; i++) {
				TpaFirm tpaForm = new TpaFirm();
				tpaForm.setId(new Integer(tpaFirms[i].getId()));
				tpaForm.setName(tpaFirms[i].getName());

				form.getTpaFirms().add(tpaForm);

				TPAUserContractAccessActionHelper.populateContractAccess(
						tpaForm.getContractAccess(0), tpaFirms[i]
								.getContractPermission());

				TPAUserContractAccessActionHelper.setLastPermissionFlags(
						tpaForm, userInfo.getProfileId());

				if (tpaForm.isLastRegisteredUser()) {
					if (buff.length() > 0) {
						buff.append(",");
					}
					buff.append(tpaFirms[i].getId());
				}

				if (tpaForm.isLastUserWithReceiveILoansEmailAndTPAStaffPlan()) {
					if (buffLastIloansEmailAndTpaStaffPlan.length() > 0) {
						buffLastIloansEmailAndTpaStaffPlan.append(",");
					}
					buffLastIloansEmailAndTpaStaffPlan.append(tpaFirms[i]
							.getId());
					if (tpaForm.isLastUserWithReceiveILoansEmail()) {
						if (buffLastIloansEmail.length() > 0) {
							buffLastIloansEmail.append(",");
						}
						buffLastIloansEmail.append(tpaFirms[i].getId());
					}
				}

				if (tpaForm.isLastUserWithManageUsers()) {
					if (buffListLastTPAUM.length() > 0) {
						buffListLastTPAUM.append(",");
					}
					buffListLastTPAUM.append(tpaFirms[i].getId());
				}

			}

			form.setLastTpaFirmsRegisteredUserList(buff.toString());
			form.setLastIloansEmailFirmsList(buffLastIloansEmail.toString());
			form
					.setLastIloansEmailAndTpaStaffPlanFirmsList(buffLastIloansEmailAndTpaStaffPlan
							.toString());
			form.setLastTUMFirmtList(buffListLastTPAUM.toString());
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
		}
	}

	/**
	 * This is the delete action, the userprofile is deleted using the security
	 * service
	 */
	@RequestMapping(value ="/deleteProfile/" ,params={"action=delete"}, method =  RequestMethod.POST) 
	public String doDelete (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		ServletContext httpservlet = request.getServletContext();
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		Collection errors = new ArrayList();
		Integer contractId = null;
		Integer alertId = null;
		BigDecimal profileId = null;

		if(userProfile.getCurrentContract()!= null){
			contractId = userProfile.getCurrentContract().getContractNumber();
		}
	    
		
		String forward = null;
		
		// Notice Manager Alert Removal 
		if(contractId!=null){
		List<UserNoticeManagerAlertVO> userNoticeAlertPreference = PlanNoticeDocumentServiceDelegate.getInstance().getUserNoticePreferences(new BigDecimal(form.getProfileId()), contractId);	
		
		for(UserNoticeManagerAlertVO userNoticeManagerAlertVO:userNoticeAlertPreference){
			alertId = userNoticeManagerAlertVO.getAlertId();
			profileId = userNoticeManagerAlertVO.getProfileId();
		  if(alertId > 0 && alertId!=null)
			{
				MessageServiceFacadeFactory.getInstance(httpservlet).deleteAlert(alertId);
				String userAction =CommonConstants.ALERT_DELETE;
				System.out.print("User Action:"+userAction);
				MessageServiceFacadeFactory.getInstance(httpservlet).userActionLog(contractId,profileId,userAction);
				
			}
		}
	}
		if (logger.isDebugEnabled()) {
			logger.debug("entry <-- doDelete");
		}

		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(form.getUserName());
		userInfo.setRole(form.getUserRole());
		userInfo.setEmail(form.getEmail());

		for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) {
			ClientUserContractAccess access = (ClientUserContractAccess) it
					.next();
			ContractPermission permission = new ContractPermission(null);
			ClientUserContractAccessActionHelper.populateContractPermission(
					permission, access);
			userInfo.addContractPermission(permission);
		}

		try {
			if (logger.isDebugEnabled())
				logger.debug("deleteUser");

			request.setAttribute("deleteProfileForm", form);
			service.deleteUser(principal, userInfo, Environment.getInstance()
					.getSiteLocation());
			forward = forwards.get(DELETE_USER);

			LockServiceDelegate.getInstance()
					.releaseLock(
							LockHelper.USER_PROFILE_LOCK_NAME,
							LockHelper.USER_PROFILE_LOCK_NAME
									+ userInfo.getProfileId());

		} catch (SecurityServiceException e) {
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));

			SessionHelper.setErrorsInSession(request, errors);
			forward = forwards.get("input");
			if (logger.isDebugEnabled()) {
				logger.debug("security exception" + e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDelete");
		}

		return forward;

	}

	@RequestMapping(value ="/deleteProfile/", params={"action=viewPermissions"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doViewPermissions (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doViewPermissions");
		}

		String forward = forwards.get(VIEW_PERMISSIONS);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doViewPermissions");
		}
		return forward;
	}

	@RequestMapping(value ="/deleteProfile/", params={"action=cancel"}, method =  RequestMethod.POST) 
	public String doCancel (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		LockServiceDelegate.getInstance().releaseLock(
				LockHelper.USER_PROFILE_LOCK_NAME,
				LockHelper.USER_PROFILE_LOCK_NAME + form.getProfileId());
		request.getSession(false).removeAttribute("deleteProfileForm");
		request.getSession(false).removeAttribute("userProfileForm");
		request.getSession(false).removeAttribute("userPermissionsForm");
		
		return forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
	}

	@RequestMapping(value ="/deleteProfile/", params={"action=confirm"}, method =  RequestMethod.GET) 
	public String doConfirm (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		return forwards.get(CONFIRMATION_PAGE);
	}

	@RequestMapping(value ="/deleteProfile/" ,params={"action=refresh"}, method =  RequestMethod.GET) 
	public String doRefresh (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		form.setAction(null);
		return forwards.get("input");
	}

	@RequestMapping(value ="/deleteProfile/", params={"action=finish"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFinish (@Valid @ModelAttribute("deleteProfileForm") DeleteProfileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response)  {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String formErrors=validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors); 
		}
		String forward = null;

		
		UserRole role = form.getUserRole();
		form.clear( request);

		if (role != null) {
			if (role instanceof InternalUser) {
				forward = forwards.get(MANAGE_INTERNAL_USERS);
			} else {
				if (role instanceof ThirdPartyAdministrator) {
					forward = forwards.get(MANAGE_TPA_USERS);
				} else {
					forward = forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
				}
			}
		} else {
			forward = forwards.get(SessionHelper.getLastVisitedManageUsersPage(request));
		}

		request.getSession(false).removeAttribute("deleteProfileForm");
		request.getSession(false).removeAttribute("userProfileForm");
		request.getSession(false).removeAttribute("userPermissionsForm");

		return forward;
	}

	@SuppressWarnings("rawtypes")
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		DeleteProfileForm form = (DeleteProfileForm) actionForm;
		/*This code has been changed and added  to 
	   	 * Validate form and request against penetration attack, prior to other validations as part of the .
	   	 */
		
		if ("delete".equals(form.getAction())) {
			Collection errors = doValidate( form, request);

			/*
			 * Errors are stored in the session so that our REDIRECT can look up
			 * the errors.
			 */
			if (!errors.isEmpty()) {
				SessionHelper.setErrorsInSession(request, errors);
				return forwards.get(REFRESH);
			}

		}

		return null;
	}

	protected Collection doValidate(
			DeleteProfileForm form, HttpServletRequest request) {

		Collection<GenericException> errors=new ArrayList();

		if (!form.isCanManageAllContracts()) {
			errors.add(new GenericException(1082));
		}

        for (Iterator i = form.getContractAccesses().iterator(); i.hasNext();) {
            ClientUserContractAccess contractAccess = (ClientUserContractAccess) i.next();

            Map<String, Boolean> lastUserFlagMap = null;
            String[] params = new String[] { contractAccess.getContractNumber().toString() };
            if (contractAccess.isCbcIndicator()) {
                // SVC.6
                if (Trustee.ID.equals(contractAccess.getPlanSponsorSiteRole().getValue())) {
                    lastUserFlagMap = ClientUserContractAccessActionHelper.getLastUserFlags(contractAccess.getContractNumber().intValue(), form.getProfileId(), false);
                    if (lastUserFlagMap.get(Trustee.ID).booleanValue()) {
                        GenericException ex = new GenericException(1066, params);
                        errors.add(ex);
                    }
				}
			}
                // SVC.7
			if (contractAccess.isPrimaryContact()) {
				GenericException ex = new GenericException(1055, params);
				errors.add(ex);
			}
			// SVC.8
			if (contractAccess.isMailRecepient()) {
				GenericException ex = new GenericException(1056, params);
				errors.add(ex);
			}
			
		}
        
        return errors;
    }
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
