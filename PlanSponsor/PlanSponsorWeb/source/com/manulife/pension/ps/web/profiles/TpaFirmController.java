package com.manulife.pension.ps.web.profiles;

import static com.manulife.pension.ps.web.Constants.FORWARD_TPA_CONTACTS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.event.IpiHostingTpaFirmAccessChangeEvent;
import com.manulife.pension.event.SigningAuthorityAssignedToListOfTPAUsersEvent;
import com.manulife.pension.event.SigningAuthorityPermissionAssignedToTPAUserEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantAddressContractServiceFeature;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserSearchCriteria;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;


@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"tpaFirmForm"})

public class TpaFirmController extends PsAutoController {

	@ModelAttribute("tpaFirmForm") 
	public TpaFirm populateForm()
	{
		return new TpaFirm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/editTpaFirm.jsp");
		forwards.put("confirm","redirect:/do/profiles/editTpaFirm/?action=confirm");
		forwards.put("refresh","redirect:/do/profiles/editTpaFirm/?action=refresh");
		forwards.put("confirmPage","/profiles/editTpaFirmConfirmation.jsp");
		forwards.put("tpaContactsTab","redirect:/do/contacts/thirdPartyAdministrator/");
	}

	
    protected static final String MANAGE_USERS = "manageUsers";

    private static final String CONFIRMATION_PAGE = "confirmPage";

    private static final String CONFIRMATION = "confirm";

    private static final String REFRESH = "refresh";

    /**
     * Constructor.
     */
    public TpaFirmController() {
        super(TpaFirmController.class);
    }
 
    
   /* protected void postExecute (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {  
    	
 
        super.postExecute( form, request, response);
        request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
    }*/

	
	@RequestMapping(value ="/editTpaFirm/",  params={"action=printPDF"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
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
	       String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	

    /**
     * Forward to the confirmation page. This action is needed because it's the
     * result of a REDIRECT after a POST.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value ="/editTpaFirm/", params={"action=confirm"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doConfirm(@Valid @ModelAttribute("tpaFirmForm") TpaFirm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
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
        logger.debug("doConfirm is invoked... return forward:"
                + StaticHelperClass.toXML(forwards.get(CONFIRMATION_PAGE)));
        return forwards.get(CONFIRMATION_PAGE);
    }

    /**
     * Simply refresh the page. This action is used when we try to perform a
     * REDIRECT after a POST.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return The input forward.
     */
	@RequestMapping(value ="/editTpaFirm/" , params={"action=refresh"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		
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
        return forwards.get("input");
    }

    /**
     * Removes the form from the session and forward user to the manage users
     * page.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value ="/editTpaFirm/", params={"action=finish"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFinish (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
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
       request.getSession(false).removeAttribute("tpaFirmForm");
        return forwards.get(FORWARD_TPA_CONTACTS);
    }

    /**
     * Cancel the current add/edit operation. The form in the session is
     * cleared.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     */
	@RequestMapping(value ="/editTpaFirm/", params={"action=cancel"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCancel (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
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

        LockServiceDelegate.getInstance().releaseLock(
                LockHelper.TPA_FIRM_LOCK_NAME, LockHelper.TPA_FIRM_LOCK_NAME + form.getId());

        request.getSession(false).removeAttribute("tpaFirmForm");
        return forwards.get(FORWARD_TPA_CONTACTS);
    }


    /**
     * Saves the user input form.
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value ="/editTpaFirm/", params={"action=save"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
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
			logger.debug("entry <-- doSave");
			logger.debug(form);
		}

		String forward = null;
		
		TPAFirmInfo firmInfo = new TPAFirmInfo();
		populateTpaFirmInfo(firmInfo, form);
		TPAUserContractAccess access = form.getContractAccess(0);
		UserProfile userProfile = getUserProfile(request);
		Principal principal = userProfile.getPrincipal();
		int contractNumber = userProfile.getCurrentContract()
				.getContractNumber();

		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();

		String warningMessage = null;
		try {
			if (logger.isDebugEnabled()) {
				logger
						.debug("Firm info:\n"
								+ StaticHelperClass.toXML(firmInfo));
			}
			TpaFirm clonedForm = (TpaFirm) form.getClonedForm();
			TPAUserContractAccess clonedAccess = clonedForm
					.getContractAccess(0);
			
			service.updateTpaFirm(principal, firmInfo);

			// Here's how we will handle the tpauser/contract approve
			// permission.
			// 1. if a user's checkbox goes from no to yes, then we
			// a. give the user the tpauser/contract approve permission, and
			// b. give the user the tpauser/tpafirm approve permission.
			// 2. if a user's checkbox goes from yes to no, then we
			// c. remove the tpauser/contract approve permission.
			// if 1 or 2, then call service.updateUser.

			// the call to
			// SecurityServiceDelegate.getInstance().searchByUserName is very
			// slow
			// ( but necessary in order to call updateUser )
			// therefore, we will perform a & b on the same service.updateUser
			// call.

			// create 2 lists. people who gained the permission, and people who
			// lost the permission.

			List<UserInfo> gainedSigningAuthorityPermission = new ArrayList<UserInfo>();
			List<UserInfo> lostSigningAuthorityPermission = new ArrayList<UserInfo>();
			if (form.getTPAUsers() != null
					&& principal.getRole() instanceof InternalServicesCAR) {
				List<UserInfo> beforeSelectedUsers = clonedForm
						.getSelectedTPAUsersAsList();
				List<UserInfo> afterSelectedUsers = form
						.getSelectedTPAUsersAsList();
				for (UserInfo tpaUser : form.getTPAUsers()) {
					if (afterSelectedUsers.contains(tpaUser)
							&& !beforeSelectedUsers.contains(tpaUser)) {
						gainedSigningAuthorityPermission.add(tpaUser);
					}
					if (!afterSelectedUsers.contains(tpaUser)
							&& beforeSelectedUsers.contains(tpaUser)) {
						lostSigningAuthorityPermission.add(tpaUser);
					}
				}
			}

			if (!gainedSigningAuthorityPermission.isEmpty()) {
				List<String> newApproverEmails = new ArrayList<String>();
				String listOfTPANames = null;
				for (UserInfo tpaUser : gainedSigningAuthorityPermission) {
					// TPF33
					UserInfo userInfo = SecurityServiceDelegate.getInstance()
							.searchByUserName(userProfile.getPrincipal(),
									tpaUser.getUserName());
					ContractPermission tpaUsertpaFirmPermission = userInfo
							.getTpaFirm(firmInfo.getId())
							.getContractPermission();
					ContractPermission tpaUserContractPermission = userInfo
							.getTpaFirm(firmInfo.getId())
							.getContractToContractPermission().get(
									contractNumber);
					// changing ApproveWithdrawals to Signing Authority : Loans
					// project
					if (!tpaUsertpaFirmPermission.isSigningAuthority()) {
						tpaUsertpaFirmPermission.setSigningAuthority(true);
					}
					tpaUserContractPermission.setSigningAuthority(true);
					service.updateUser(principal, userInfo, Environment
							.getInstance().getSiteLocation(), true);
					service.createHistoryForSigningAuthorityChange(principal, principal.getRole(), true, userInfo, contractNumber);
					newApproverEmails.add(userInfo.getEmail());
					
					if (listOfTPANames == null) {
						listOfTPANames = tpaUser.getFirstName()+" "+tpaUser.getLastName();
					} else {
						listOfTPANames = listOfTPANames + ", "
								+ tpaUser.getFirstName()+" "+tpaUser.getLastName();
					}
					
					// Triggering SigningAuthorityPermissionAssignedToTPAUser Event
					SigningAuthorityPermissionAssignedToTPAUserEvent assignedToTPAUsersEvent = new SigningAuthorityPermissionAssignedToTPAUserEvent(
							"TpaFirmAction", "doSave");
					assignedToTPAUsersEvent.setInitiator(principal.getProfileId());
					assignedToTPAUsersEvent.setContractId(contractNumber);
					assignedToTPAUsersEvent.setTpaUserId(tpaUser.getProfileId());
					EventClientUtility.getInstance(
							GlobalConstants.PSW_APPLICATION_ID)
							.prepareAndSendJMSMessage(assignedToTPAUsersEvent);
				}
				
				//Triggering SigningAuthorityAssignedToListOfTPAUsers Event
				SigningAuthorityAssignedToListOfTPAUsersEvent assignedToListOfTPAUsersEvent = new SigningAuthorityAssignedToListOfTPAUsersEvent(
						"TpaFirmAction", "doSave");
				assignedToListOfTPAUsersEvent.setInitiator(principal.getProfileId());
				assignedToListOfTPAUsersEvent.setContractId(contractNumber);
				assignedToListOfTPAUsersEvent.setTpaUserNames(listOfTPANames);
				EventClientUtility.getInstance(GlobalConstants.PSW_APPLICATION_ID).prepareAndSendJMSMessage(
						assignedToListOfTPAUsersEvent);
				
				// TPF38
				SecurityServiceDelegate.getInstance()
						.sendTPAUserGrantedPermissionEmail(principal, firmInfo,
								PermissionType.SIGNING_AUTHORITY,
								newApproverEmails,
								Environment.getInstance().getSiteLocation());
			}
			if (!lostSigningAuthorityPermission.isEmpty()) {
				for (UserInfo tpaUser : lostSigningAuthorityPermission) {
					UserInfo userInfo = SecurityServiceDelegate.getInstance()
							.searchByUserName(userProfile.getPrincipal(),
									tpaUser.getUserName());
					ContractPermission tpaUserContractPermission = userInfo
							.getTpaFirm(firmInfo.getId())
							.getContractToContractPermission().get(
									contractNumber);
					tpaUserContractPermission.setSigningAuthority(false);
					service.updateUser(principal, userInfo, Environment
							.getInstance().getSiteLocation(), true);
					service.createHistoryForSigningAuthorityChange(principal, principal.getRole(), false, userInfo, contractNumber);
				}
			}
			// TODO Need to implement TPF64 Loans project - Message Center
			// related

			// TPF39
			boolean checkTPAUsersWithInitiateIWithdrawals = access
					.getInitiateIWithdrawals().booleanValue()
					&& !clonedAccess.getInitiateIWithdrawals().booleanValue();
			boolean checkTPAUsersWithReviewIWithdrawals = access
					.getReviewIWithdrawals().booleanValue()
					&& !clonedAccess.getReviewIWithdrawals().booleanValue();
			if (checkTPAUsersWithInitiateIWithdrawals
					|| checkTPAUsersWithReviewIWithdrawals) {
				ArrayList<String> permissionList = new ArrayList<String>();
				String initiateWithdrawalsCode = PermissionType
						.getPermissionCode(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
				String reviewWithdrawalsCode = PermissionType
						.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
				if (checkTPAUsersWithInitiateIWithdrawals) {
					permissionList.add(initiateWithdrawalsCode);
				}
				if (checkTPAUsersWithReviewIWithdrawals) {
					permissionList.add(reviewWithdrawalsCode);
				}
				Map permissionMap = SecurityServiceDelegate.getInstance()
						.getTPAUsersWithRolePermission(form.getId().intValue(),
								permissionList, null);
				if (checkTPAUsersWithInitiateIWithdrawals) {
					ArrayList profileList = (ArrayList) permissionMap
							.get(initiateWithdrawalsCode);
					if (profileList == null || profileList.isEmpty()) {
						SecurityServiceDelegate
								.getInstance()
								.sendTPAFirmGrantedPermissionEmail(
										principal,
										firmInfo,
										PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE,
										Environment.getInstance()
												.getSiteLocation());
					}
				}
				if (checkTPAUsersWithReviewIWithdrawals) {
					ArrayList profileList = (ArrayList) permissionMap
							.get(reviewWithdrawalsCode);
					if (profileList == null || profileList.isEmpty()) {
						SecurityServiceDelegate.getInstance()
								.sendTPAFirmGrantedPermissionEmail(
										principal,
										firmInfo,
										PermissionType.REVIEW_WITHDRAWALS,
										Environment.getInstance()
												.getSiteLocation());
					}
				}
			}

			boolean reviewIWithdrawalsChangedToYes = access
					.getReviewIWithdrawals()
					&& !clonedAccess.getReviewIWithdrawals();
			boolean reviewIWithdrawalsChangedToNo = !access
					.getReviewIWithdrawals()
					&& clonedAccess.getReviewIWithdrawals();
			// implementation of TPF62 & TPF63
			boolean reviewLoansChangedToYes = access.isReviewLoans()
					&& !clonedAccess.isReviewLoans();
			boolean reviewLoansChangedToNo = !access.isReviewLoans()
					&& clonedAccess.isReviewLoans();
			boolean initiateLoansChangedToYes = access.isInitiateLoans() 
					&& !clonedAccess.isInitiateLoans();		
			
			boolean noTPAWithInitiateLoanPermission = true;
			boolean noTPAWithReviewLoanPermission = true;
			
			//TODO: Once the method is added in ContactsDAO, following code will be moved to seperate context validator.
			//Checking whether any tpa user has initiate loans permission
			if (form.getTPAUsers() != null) {
				for (UserInfo tpaUser : form.getTPAUsers()) {
					ContractPermission contractPermission = tpaUser.getTpaFirm(
							firmInfo.getId()).getContractPermission();
					if (contractPermission.isInitiateLoans()) {
						noTPAWithInitiateLoanPermission = false;
						break;
					}
				}
				
				for (UserInfo tpaUser : form.getTPAUsers()) {
					ContractPermission contractPermission = tpaUser.getTpaFirm(
							firmInfo.getId()).getContractPermission();
					if (contractPermission.isReviewLoans()) {
						noTPAWithReviewLoanPermission = false;
						break;
					}
				}
			}			
			
			Map<String, List<Long>> clientPermissionMap = null;
			List<Long> clientUsersWithReviewIWithdrawals = null;
			List<Long> clientUsersWithReviewLoans = null;
			String reviewIWithdrawalsCode = PermissionType
					.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
			String reviewLoansCode = PermissionType
					.getPermissionCode(PermissionType.REVIEW_LOANS);
			String whoWillReviewWithdrawals = "";
			String whoWillReviewLoans = "";
			ContractServiceFeature withdrawalCSFChanged = null;
			ContractServiceFeature loansCSFChanged = null;
			boolean whoWillReviewWithdrawalsCSFChanged = false;
			boolean whoWillReviewLoansCSFChanged = false;
			if (reviewIWithdrawalsChangedToYes || reviewIWithdrawalsChangedToNo
					|| reviewLoansChangedToYes || reviewLoansChangedToNo) {
				// We already retrieved the list of client users with review
				// i:withdrawals for this contract, but get it again here to
				// make sure the data isn't stale

				ArrayList<String> serviceFeatureNameList = new ArrayList<String>();
				serviceFeatureNameList
						.add(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
				serviceFeatureNameList
						.add(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				Map csfMap = ContractServiceDelegate.getInstance()
						.getContractServiceFeatures(contractNumber,
								serviceFeatureNameList);
				ContractServiceFeature withdrawalCSF = null;
				ContractServiceFeature loansCSF = null;

				if (csfMap != null) {
					withdrawalCSF = (ContractServiceFeature) csfMap
							.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
					loansCSF = (ContractServiceFeature) csfMap
							.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
				}
				if (withdrawalCSF != null) {
					whoWillReviewWithdrawals = withdrawalCSF
							.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
				}
				if (loansCSF != null) {
					whoWillReviewLoans = loansCSF
							.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS);
				}

				List<String> permissionList = new ArrayList<String>();
				if (reviewIWithdrawalsChangedToYes
						|| reviewIWithdrawalsChangedToNo) {
					permissionList.add(reviewIWithdrawalsCode);
				}
				if (reviewLoansChangedToYes || reviewLoansChangedToNo) {
					permissionList.add(reviewLoansCode);
				}
				clientPermissionMap = SecurityServiceDelegate.getInstance()
						.getExternalUsersWithRolePermission(contractNumber,
								permissionList, null);
				clientUsersWithReviewIWithdrawals = clientPermissionMap
						.get(reviewIWithdrawalsCode);
				clientUsersWithReviewLoans = clientPermissionMap
						.get(reviewLoansCode);
			}
			if (reviewIWithdrawalsChangedToYes) {
				// Change the WHO WILL REVIEW WITHDRAWALS CSF value to
				// "TPA"
				if (!(ServiceFeatureConstants.WHO_WILL_REVIEW_TPA
						.equals(whoWillReviewWithdrawals))) {
					withdrawalCSFChanged = new ContractServiceFeature();
					withdrawalCSFChanged.setContractId(contractNumber);
					withdrawalCSFChanged
							.setName(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
					setCSFUserInfo(withdrawalCSFChanged, principal);
					withdrawalCSFChanged
							.addAttribute(
									ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS,
									ServiceFeatureConstants.WHO_WILL_REVIEW_TPA);
					whoWillReviewWithdrawalsCSFChanged = true;
				}
				if (CollectionUtils
						.isNotEmpty(clientUsersWithReviewIWithdrawals)) {
					// taking off the review withdrawal permission from the
					// client users
					warningMessage = removeReviewPermissionForClientUsers(
							userProfile, reviewIWithdrawalsCode,
							clientUsersWithReviewIWithdrawals);
				}
			} else if (reviewIWithdrawalsChangedToNo) {
				// Change the WHO WILL REVIEW WITHDRAWALS CSF value to
				// "PS"
				if (!(ServiceFeatureConstants.WHO_WILL_REVIEW_PS
						.equals(whoWillReviewWithdrawals))) {
					withdrawalCSFChanged = new ContractServiceFeature();
					withdrawalCSFChanged.setContractId(contractNumber);
					withdrawalCSFChanged
							.setName(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
					setCSFUserInfo(withdrawalCSFChanged, principal);
					withdrawalCSFChanged
							.addAttribute(
									ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS,
									ServiceFeatureConstants.WHO_WILL_REVIEW_PS);
					whoWillReviewWithdrawalsCSFChanged = true;
				}
				if (CollectionUtils.isEmpty(clientUsersWithReviewIWithdrawals)) {
					// Give the review withdrawal permission to the client
					// users, if
					// no one already has the same
					warningMessage = grantReviewPermissionForClientUsers(
							userProfile, reviewIWithdrawalsCode);
				}
			}
			// TPF 62 implementation
			if (reviewLoansChangedToYes) {
				// Change the WHO WILL REVIEW LOANS CSF value to "TPA"
				if (!(ServiceFeatureConstants.WHO_WILL_REVIEW_TPA
						.equals(whoWillReviewLoans))) {
					loansCSFChanged = new ContractServiceFeature();
					loansCSFChanged.setContractId(contractNumber);
					loansCSFChanged
							.setName(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
					setCSFUserInfo(loansCSFChanged, principal);
					loansCSFChanged.addAttribute(
							ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS,
							ServiceFeatureConstants.WHO_WILL_REVIEW_TPA);
					whoWillReviewLoansCSFChanged = true;
				}

				if (CollectionUtils.isNotEmpty(clientUsersWithReviewLoans)) {
					// Removing the review loans permissions from all the client
					// users
					warningMessage = removeReviewPermissionForClientUsers(
							userProfile, reviewLoansCode,
							clientUsersWithReviewLoans);
				}
			}
			// TPF 63 implementation
			else if (reviewLoansChangedToNo) {

				// Change the WHO WILL REVIEW LOANS CSF value to "Plan
				// Sponsor"
				if (!(ServiceFeatureConstants.WHO_WILL_REVIEW_PS
						.equals(whoWillReviewLoans))) {
					loansCSFChanged = new ContractServiceFeature();
					loansCSFChanged.setContractId(contractNumber);
					loansCSFChanged
							.setName(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
					setCSFUserInfo(loansCSFChanged, principal);
					loansCSFChanged.addAttribute(
							ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS,
							ServiceFeatureConstants.WHO_WILL_REVIEW_PS);
					whoWillReviewLoansCSFChanged = true;
				}
				if (CollectionUtils.isEmpty(clientUsersWithReviewLoans)) {

					// Give permissions to the client users, if no client user
					// is already having the permission
					warningMessage = grantReviewPermissionForClientUsers(
							userProfile, reviewLoansCode);
				}
			}
			// update both CSFs in one time
			if (whoWillReviewLoansCSFChanged
					|| whoWillReviewWithdrawalsCSFChanged) {
				Collection<ContractServiceFeature> changedCsfCollection = new ArrayList<ContractServiceFeature>();
				if (withdrawalCSFChanged != null) {
					changedCsfCollection.add(withdrawalCSFChanged);
				}
				if (loansCSFChanged != null) {
					changedCsfCollection.add(loansCSFChanged);
				}
				ContractServiceDelegate.getInstance()
						.updateContractServiceFeatures(changedCsfCollection);
			}
			
			// If 404a5 Permissions made Off and the Contract is not customized, then make it to be Customized.
			boolean isTpaAccessRestrictedTo404a5FeeTool = access.getFeeAccess404A5().booleanValue()
					&& !clonedAccess.getFeeAccess404A5().booleanValue();
			
			if (isTpaAccessRestrictedTo404a5FeeTool) {
				FeeServiceDelegate.getInstance(
						CommonConstants.PS_APPLICATION_ID)
						.setContractAsCustomized(contractNumber, form.getId(),
								principal.getProfileId());
			}
			
			// To Trigger Event 203
			boolean accessTo404a5FeeToolPermChanged = (access.getFeeAccess404A5().booleanValue() 
			&& !clonedAccess.getFeeAccess404A5().booleanValue()) || (!access.getFeeAccess404A5().booleanValue() 
					&& clonedAccess.getFeeAccess404A5().booleanValue());
			
			if (accessTo404a5FeeToolPermChanged) {
				// fire an event (Event Id: 203) that accessTo404a5FeeToolPerm had changed 
				fireIPIHostingTPAFirmAccessChangeEvent(access
						.getFeeAccess404A5().booleanValue(), contractNumber, principal.getProfileId());
			}

		} catch (SecurityServiceException e) {
			List errors = new ArrayList();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		} catch (ApplicationException e) {
			List errors = new ArrayList();
			errors
					.add(new GenericException(Integer
							.parseInt(e.getErrorCode())));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
		}

		LockServiceDelegate.getInstance().releaseLock(
				LockHelper.TPA_FIRM_LOCK_NAME,
				LockHelper.TPA_FIRM_LOCK_NAME + firmInfo.getId());

		if (StringUtils.isEmpty(warningMessage)) {
			forward = forwards.get(CONFIRMATION);
		} else {
			request.setAttribute("WARNINGS", warningMessage);
			forward = forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doSave");
		}

		return forward;
	}

    /**
	 * method that helps removing a specific permission to all the client users
	 * that belongs to that contract
	 */
	private String removeReviewPermissionForClientUsers(UserProfile profile,
			String permissionCode, List<Long> clientUsers)
			throws SecurityServiceException, SystemException {
		String warningMessage = "";
		StringBuffer lockedUserNames = new StringBuffer();
		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		Principal principal = profile.getPrincipal();
		boolean isReviewWithdrawalPermission = false;
		boolean isReviewLoanPermission = false;
		if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS))) {
			isReviewWithdrawalPermission = true;
		} else if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_LOANS))) {
			isReviewLoanPermission = true;
		}
		for (Long profileId : clientUsers) {
			UserInfo userInfo = SecurityServiceDelegate.getInstance()
					.searchByProfileId(principal, profileId);
			if (userInfo != null) {
				String componentKey = LockHelper.USER_PROFILE_LOCK_NAME
				+ profileId;
				if (LockServiceDelegate.getInstance().lock(
						LockHelper.USER_PROFILE_LOCK_NAME, componentKey,
						principal.getProfileId())) {
					int contractNumber = profile.getCurrentContract()
					.getContractNumber();
					if (isReviewWithdrawalPermission){
						userInfo.getContractPermission(contractNumber)
						.setReviewIWithdrawals(false);
					} 	else if (isReviewLoanPermission){
						userInfo.getContractPermission(contractNumber)
						.setReviewLoans(false);
					}

					service.updateUser(principal, Environment.getInstance()
							.getSiteLocation(), userInfo);
					LockServiceDelegate.getInstance().releaseLock(
							LockHelper.USER_PROFILE_LOCK_NAME, componentKey);
				} else {
					lockedUserNames.append("\\n").append(
							userInfo.getFirstName()).append(" ").append(
							userInfo.getLastName());
				}
			}
		}
		if (lockedUserNames.length() > 0) {
			String permissionForWarning = "";
			StringBuffer message;
			if(isReviewWithdrawalPermission){
				permissionForWarning = " Review i:withdrawal permission ";
			} else if(isReviewWithdrawalPermission) {
				permissionForWarning = " Review loan permission ";
			}
			if (lockedUserNames.length() > 1) {
				message = new StringBuffer(
						"Because other users are editing the profiles, ");
				message.append(permissionForWarning);
				message.append(" could not be removed for the following client users:");
			} else {
				message = new StringBuffer(
				"Because another user is editing the profile, ");
				message.append(permissionForWarning);
				message.append(" could not be removed for the following client user:");
			}
			message.append(lockedUserNames);
			warningMessage = message.toString();
		}		
		return warningMessage;
	}
	
    /**
	 * method that helps granting review withdrawal/loan permission to all the client users
	 * that belongs to that contract
	 */
	private String grantReviewPermissionForClientUsers(UserProfile profile,
			String permissionCode) throws SecurityServiceException, SystemException {
		String warningMessage = "";
		UserSearchCriteria sc1 = new UserSearchCriteria();
		int contractNumber = profile.getCurrentContract().getContractNumber();
		sc1.setContractId(contractNumber);
		sc1.setSearchCriteria(UserSearchCriteria.SEARCH_BY_CONTRACT_NUMBER);
		sc1.setSearchObject(String.valueOf(contractNumber));
		SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
		Principal principal = profile.getPrincipal();
		boolean isReviewWithdrawalPermission = false;
		boolean isReviewLoanPermission = false;
		if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS))) {
			isReviewWithdrawalPermission = true;
		} else if (permissionCode.equals(PermissionType
				.getPermissionCode(PermissionType.REVIEW_LOANS))) {
			isReviewLoanPermission = true;
		}
		List<UserInfo> clientUsers = SecurityServiceDelegate.getInstance()
				.searchUser(profile.getPrincipal(), sc1);
		StringBuffer lockedUserNames = new StringBuffer();
		for (UserInfo clientUser : clientUsers) {
			if (!clientUser.getProfileStatus().equalsIgnoreCase("deleted")) {
				UserInfo userInfo = SecurityServiceDelegate.getInstance()
						.searchByUserName(profile.getPrincipal(),
								clientUser.getUserName());
				UserRole role = userInfo.getContractPermission(contractNumber)
						.getRole();
				String defaultPermissionValue = "";
				if (isReviewWithdrawalPermission) {
					defaultPermissionValue = role.getDefaultRolePermissions().getDefaultPermissionValue(
							PermissionType.REVIEW_WITHDRAWALS);
				} else if (isReviewLoanPermission) {
					defaultPermissionValue = role.getDefaultRolePermissions().getDefaultPermissionValue(
							PermissionType.REVIEW_LOANS);
				}
				if (DefaultRolePermissions.YES.equals(defaultPermissionValue)
						|| DefaultRolePermissions.TRUE
								.equals(defaultPermissionValue)) {
					String componentKey = LockHelper.USER_PROFILE_LOCK_NAME
							+ userInfo.getProfileId();
					if (LockServiceDelegate.getInstance().lock(
							LockHelper.USER_PROFILE_LOCK_NAME, componentKey,
							principal.getProfileId())) {
						if (isReviewWithdrawalPermission) {
							userInfo.getContractPermission(contractNumber)
									.setReviewIWithdrawals(true);
						} else if (isReviewLoanPermission) {
							userInfo.getContractPermission(contractNumber)
									.setReviewLoans(true);
						}
						service.updateUser(principal, Environment.getInstance()
								.getSiteLocation(), userInfo);
						LockServiceDelegate.getInstance()
								.releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
										componentKey);
					} else {
						lockedUserNames.append("\\n").append(
								clientUser.getFirstName()).append(" ").append(
								clientUser.getLastName());
					}
				}
			}
		}
		if (lockedUserNames.length() > 0) {
			String permissionForWarning = "";
			StringBuffer message;
			if(isReviewWithdrawalPermission){
				permissionForWarning = " Review i:withdrawal permission ";
			} else if(isReviewWithdrawalPermission) {
				permissionForWarning = " Review loan permission ";
			}
			if (lockedUserNames.length() > 1) {
				message = new StringBuffer(
						"Because other users are editing the profiles, ");
				message.append(permissionForWarning);
				message.append(" could not be granted for the following client users:");
				message.append(lockedUserNames);
			} else {
				message = new StringBuffer(
				"Because another user is editing the profile, ");
				message.append(permissionForWarning);
				message.append(" could not be granted for the following client user:");
				message.append(lockedUserNames);
			}
		warningMessage = message.toString();
		}
		return warningMessage;
	}

    
    /**
     * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping, AutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	@RequestMapping(value ="/editTpaFirm/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("tpaFirmForm") TpaFirm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
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
            logger.debug("entry <-- doDefault");
        }

        UserProfile userProfile = getUserProfile(request);
        UserRole role = userProfile.getRole();

        if ((!role.isInternalUser() && (role
				.hasPermission(PermissionType.SELECTED_ACCESS) || !role
				.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)))
				|| (role.isInternalUser() && !role
						.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
        form.clear( request);

        UserProfile loginUserProfile = getUserProfile(request);
        Principal loginPrincipal = loginUserProfile.getPrincipal();
        Contract currentContract = loginUserProfile.getCurrentContract();

        TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
                .getFirmInfoByContractId(currentContract.getContractNumber());

        if (firmInfo == null) {
            return forwards.get(FORWARD_TPA_CONTACTS);
        }

        int firmId = firmInfo.getId();

		if (!LockServiceDelegate.getInstance().lock(LockHelper.TPA_FIRM_LOCK_NAME, LockHelper.TPA_FIRM_LOCK_NAME + firmId, loginPrincipal.getProfileId()))
        {
        	try
        	{
            	Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(LockHelper.TPA_FIRM_LOCK_NAME, LockHelper.TPA_FIRM_LOCK_NAME + firmId);

        		UserInfo lockOwnerUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(
        				loginPrincipal, lockInfo.getLockUserProfileId());

        		String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(loginUserProfile, lockOwnerUserInfo);
        		Collection<GenericException> errors = new ArrayList<GenericException>();
        		errors.add(new GenericException(1057, new String[] {lockOwnerDisplayName}));
                SessionHelper.setErrorsInSession(request, errors);

        		return forwards.get(FORWARD_TPA_CONTACTS);
    		}
    		catch(SecurityServiceException e)
    		{
    			throw new SystemException(e, "com.manulife.pension.ps.web.profiles.TpaFirmAction", "doDefault", "Failed to get user info of lock own. " + e.toString());
    		}
    	}

        form.setName(firmInfo.getName());
        form.setId(new Integer(firmId));
        TPAUserContractAccess contractAccess = new TPAUserContractAccess();

        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				getUserProfile(request).getPrincipal());


        TPAUserContractAccessActionHelper.populateContractAccess(contractAccess,
                firmInfo.getContractPermission(), loginUserInfo);


        form.getContractAccesses().add(contractAccess);
        EmailPreferences ep = new EmailPreferences();
        ep.loadPreferences(firmInfo.getContractPermission());

        doSetupVisibility(role, firmInfo, form, contractAccess, ep, currentContract);

        if(!currentContract.isBundledGaIndicator()){
        	if (form.getShowTPAUserList()) {
        		setTpaUserList(form, firmInfo, currentContract.getContractNumber(), userProfile);
        	}
        } else {
        	//As the contract is a bundled GA no need to show the TPA user list.
        	form.setShowTPAUserList(Boolean.FALSE);
        }
        form.setBundledGaIndicator(currentContract.isBundledGaIndicator());
        
        form.storeClonedForm();

        if (logger.isDebugEnabled()) {
            logger.debug(StaticHelperClass.toXML(form));
            logger.debug("exit <-- doDefault");
        }
        request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
        return forwards.get("input");
    }
	
	
	

	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
    private void setTpaUserList(TpaFirm firm, TPAFirmInfo firmInfo, int contractNumber, UserProfile userProfile) throws SystemException {
        UserSearchCriteria sc1 = new UserSearchCriteria();
        sc1.setContractId(contractNumber);
        sc1.setSearchCriteria(UserSearchCriteria.SEARCH_BY_TPA_FIRM_ID);
        sc1.setSearchObject(Integer.toString(firm.getId()));
        List<UserInfo> tpaUsers;
        List<UserInfo> activeTpaUsers = new ArrayList<UserInfo>();
        List<TPAFirmInfo> tpaFirmInfos = new ArrayList<TPAFirmInfo>();
        tpaFirmInfos.add(firmInfo);

        try {
            // Get the list of all tpa users for this firm
            tpaUsers = SecurityServiceDelegate.getInstance().searchTpaUserFirmPermissions(tpaFirmInfos);
            for (UserInfo userInfo : tpaUsers) {
                if (!userInfo.getProfileStatus().equalsIgnoreCase("deleted") && userInfo.isWebAccessInd()) {
                    activeTpaUsers.add(userInfo);
                }
            }
            firm.setTPAUsers(activeTpaUsers);

            // Get the list of tpa users on the approver list
            // Change from APPROVE WITHDRAWALS to SIGNING AUTHORITY
            List<String> tpaUsersAsStrings = new ArrayList<String>();
            for (Integer approverId : firmInfo.getUseridToContractPermission().keySet()) {
            	if (firmInfo.getUseridToContractPermission().get(approverId)
						.getRole().hasPermission(
								PermissionType.SIGNING_AUTHORITY)) {
					tpaUsersAsStrings.add(approverId.toString());
				}
            }
            firm.setSelectedTPAUsers(tpaUsersAsStrings.toArray(new String[tpaUsersAsStrings.size()]));

            // Get the list of users with review and approve withdrawals permission
            //Change from APPROVE WITHDRAWALS to SIGNING AUTHORITY
            String signingAuthorityCode = PermissionType
					.getPermissionCode(PermissionType.SIGNING_AUTHORITY);
			String reviewIWithdrawalsCode = PermissionType
					.getPermissionCode(PermissionType.REVIEW_WITHDRAWALS);
			String reviewLoansCode = PermissionType.getPermissionCode(PermissionType.REVIEW_LOANS);
			
			List<String> permissionList = new ArrayList<String>();
			permissionList.add(signingAuthorityCode);
            permissionList.add(reviewIWithdrawalsCode);
            permissionList.add(reviewLoansCode);
            
			Map<String, List<Long>> clientPermissionMap = SecurityServiceDelegate.getInstance()
					.getExternalUsersWithRolePermission(contractNumber, permissionList, null);
			firm.setClientUsersWithReviewWithdrawalPermissionList(clientPermissionMap
					.get(reviewIWithdrawalsCode));
			firm.setClientUsersWithApproveWithdrawalPermissionList(clientPermissionMap
					.get(signingAuthorityCode));
			firm.setClientUsersWithReviewLoansPermissionList(clientPermissionMap
					.get(reviewLoansCode));
        } catch (SecurityServiceException e) {
            throw new SystemException(e, "falied to get Userinfo for TpaFirmAction.doDefault");
        }
    }

	private String[] doSelectedTpaUsers(List<UserInfo> tpaUsers, int contractNumber, int firmId) {

    	List<String> selectedUsers = new ArrayList<String>();
    	for (UserInfo user : tpaUsers) {
    		ContractPermission cp = user.getTpaFirm(firmId).getContractPermission();
    		if ( cp != null && cp.isSigningAuthority()) {
    			selectedUsers.add(String.valueOf(user.getProfileId()));
    		}
    	}
		return selectedUsers.toArray(new String[0]);
	}

	/**
     * everything on access has been filtered by the user's permissions.
     * This function will filter the email preferences using the users permissions, then
     * setup default rules for visibility.
     *
     *
     *
     * @param role
     * @param firmInfo
     * @param firm
     * @param access
     * @param emailAccess
     */
    private void doSetupVisibility(UserRole role, TPAFirmInfo firmInfo,
			TpaFirm firm, TPAUserContractAccess access,
			EmailPreferences emailAccess, Contract contract) {

		boolean isClientUserManager = !role.isInternalUser()
				&& (!role.hasPermission(PermissionType.SELECTED_ACCESS) && role
						.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS));

		boolean isInternalServicesCar = role instanceof InternalServicesCAR;

		boolean isTrustee = isClientUserManager && role instanceof Trustee;
		boolean withdrawalsFeature = false;
		boolean reviewStageRequired = false;
		boolean autoEnrollmentServiceFeature = false;

		boolean submitUpdateVesting = true; // TODO: changed to untie CSF Vesting to Plan Data
        boolean payrollServiceFeature = false;
        boolean addressServiceFeature = false;
        boolean aciOn = false;
        // allow online loans feature
        boolean loansFeature = false;

		Map<String, ContractServiceFeature> featureMap = contract.getServiceFeatureMap();

		if (featureMap != null) {
			ContractServiceFeature feature = featureMap.get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
			withdrawalsFeature = feature != null && ContractServiceFeature.internalToBoolean(feature.getValue()).booleanValue();
			if (withdrawalsFeature) {
				String attr = feature.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW);
				String whoWillReview = feature.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS);
				reviewStageRequired = ((attr != null && ContractServiceFeature.internalToBoolean(attr).booleanValue()) && 
						((whoWillReview != null) && (
								(ServiceFeatureConstants.WHO_WILL_REVIEW_TPA.equalsIgnoreCase(whoWillReview))
								||(ServiceFeatureConstants.WHO_WILL_REVIEW_PS.equalsIgnoreCase(whoWillReview)))));
			}
			feature = featureMap.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
			autoEnrollmentServiceFeature = feature != null && ContractServiceFeature.internalToBoolean(feature.getValue()).booleanValue();

//			feature = featureMap.get(ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
//			submitUpdateVesting = feature != null && !feature.getValue().equalsIgnoreCase(com.manulife.pension.service.contract.util.Constants.NA);

            feature = featureMap.get(ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
            payrollServiceFeature = feature != null && ContractServiceFeature.internalToBoolean(feature.getValue()).booleanValue();

            feature = featureMap.get(ServiceFeatureConstants.ADDRESS_MANAGEMENT_FEATURE);
            if (feature != null) {
                ParticipantAddressContractServiceFeature pacsf = new ParticipantAddressContractServiceFeature(feature);
                addressServiceFeature = (pacsf.getValues().size() > 0);
			}

            feature = featureMap.get(ServiceFeatureConstants.AUTO_CONTRIBUTION_INCREASE);

            if ((feature !=null) &&
    				(ContractServiceFeature.internalToBoolean(feature.getValue()).booleanValue() ||
            		 Constants.ACI_DEFAULTED_TO_YES.equals(feature.getValue()))) {
            	aciOn = true;
    		}
            // Adding Loan stuff
			ContractServiceFeature allowLoansFeature = featureMap.get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
			loansFeature = (contract.isLoansRecordKeepingIndicator() && (allowLoansFeature != null && ContractServiceFeature.internalToBoolean(allowLoansFeature.getValue()).booleanValue()));
		}

		access.setShowPlanData(submitUpdateVesting);//this one doesn't belong in this function, but i can't put
		//it in TPAUserContractAccessActionHelper.populateContractAccess because this is a firm contract access
		//with different rules.  need to refactor all of this code and create a new clas TPAFirmContractAccess.
		access.setShowReviewIWithdrawals(reviewStageRequired);
		access.setShowInitiateIWithdrawals(withdrawalsFeature);
		
		// signing authority is based on whether the contract is business converted or not
		access.setShowSigningAuthority(contract.isBusinessConverted());
		
		// setting the TPAUserContractAccess with loans features
		access.setShowInitiateLoans(loansFeature);
		access.setShowViewAllLoans(loansFeature);
		access.setShowReviewLoans(loansFeature);

        if (loansFeature) {
        	emailAccess.setShowiLoanEmail(false);
        }
		if (isClientUserManager) {
			firm.setEnablePlanData(false);
			firm.setEnableReviewWithdrawals(false);
			firm.setEnableInitiateWithdrawals(false);
			
			firm.setEnableInitiateLoans(false);
			firm.setEnableReviewLoans(false);
			/*Added new attribute for 404a5 permission*/
			firm.setEnableFeeAccess404A5(false);

            if ((contract.isBusinessConverted() && !isTrustee) || (!contract.isBusinessConverted() && !role.hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT))) {
				firm.setEnableDirectDebit(false);
				firm.setEnableDirectDebitAccounts(false);
			}
		}

		firm.setShowReportingSection(access.isShowReportDownload());
		firm.setShowPlanServicesSection(access.isShowPlanData());
		firm.setShowSubmissionsSection(
				access.isShowUploadSubmissions()
				|| access.isShowCashAccount()
				&& !access.isShowDirectDebit());
		firm.setShowiWithdrawalsSection(withdrawalsFeature
				&& (access.isShowReviewIWithdrawals()
						 || access.isShowInitiateIWithdrawals()));
        firm.setEnableSigningAuthority(isInternalServicesCar);
        firm.setEnableTPAUserList(isInternalServicesCar);

		firm.setShowCensusManagementSection(access.isShowUpdateCensusData()
				|| access.isShowViewSalary());
		// whether to show the loans section or not
		firm.setShowLoansSection(loansFeature
				&& (access.isShowReviewLoans()
						|| access.isShowInitiateLoans()));
        firm.setShowTPAUserList(contract.isBusinessConverted());
        
        /*Added new attribute for 404a5 permission*/
        firm.setShowPlanServicesSection(access.isShowFeeAccess404A5());
		
		firm.setShowClientServicesSection(firm.getShowPayrollPathSection()
				|| firm.getShowSubmissionsSection()
				|| firm.getShowiWithdrawalsSection()
				|| firm.getShowCensusManagementSection()
				|| firm.getShowLoansSection());
		
		firm.setShowEverything(firm.getShowReportingSection()
				|| firm.getShowPlanServicesSection()
				|| firm.getShowClientServicesSection());
	}

	
    
	
    protected void populateTpaFirmInfo(TPAFirmInfo firmInfo, TpaFirm form) {
        firmInfo.setId(form.getId().intValue());
        ContractPermission permission = new ContractPermission(null);
        firmInfo.setContractPermission(permission);
        firmInfo.setName(form.getName());
        TPAUserContractAccessActionHelper.populateContractPermission(firmInfo
                .getContractPermission(), form.getContractAccess(0));
        firmInfo.setUpdatedInformation(form.getChanges().toString());
        if (logger.isDebugEnabled()) {
            logger.debug("Updated information is:\n"
                    + firmInfo.getUpdatedInformation());
        }
    }
    
    private void setCSFUserInfo(ContractServiceFeature csf, Principal principal) {
        csf.setUserName(principal.getUserName());
        csf.setPrincipalProfileId(principal.getProfileId());
        csf.setPrincipalFirstName(principal.getFirstName());
        csf.setPrincipalLastName(principal.getLastName());
        if(principal.getRole().isExternalUser())
        	csf.setUserIdType(Constants.EXTERNAL_USER_ID_TYPE);
        else
        	csf.setUserIdType(Constants.INTERNAL_USER_ID_TYPE);

        csf.setSourceChannelCode(Constants.PS_SOURCE_CHANNEL_CODE);
    }
    
    
	private void fireIPIHostingTPAFirmAccessChangeEvent(
			boolean accessTo404a5FeeToolPermission, int contractId, long userProfileId)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> fireIPIHostingTPAFirmAccessChangeEvent");
		}

		IpiHostingTpaFirmAccessChangeEvent tpaFirmAccessChangeEvent = new IpiHostingTpaFirmAccessChangeEvent(
				"TpaFirmAction", "doSave");
		tpaFirmAccessChangeEvent.setContractId(contractId);
		tpaFirmAccessChangeEvent
				.setAccessTo404a5FeeToolPermission(accessTo404a5FeeToolPermission);
		tpaFirmAccessChangeEvent.setInitiator(userProfileId);
		EventClientUtility.getInstance(Environment.getInstance().getAppId())
				.prepareAndSendJMSMessage(tpaFirmAccessChangeEvent);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- fireIPIHostingTPAFirmAccessChangeEvent");
		}
	}
	

	public static void setShowManageTpaFirmLink(HttpServletRequest request) throws SystemException {
		UserProfile userProfile = getUserProfile(request);

		if (userProfile.getContractProfile().getShowManageTpaFirmLink() == null) {
			boolean showEditTpaFirm = false;
			Contract currentContract = userProfile.getCurrentContract();

			UserRole role = userProfile.getRole();

			if (role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))
			{

				if ((role.isExternalUser() && !role.hasPermission(PermissionType.SELECTED_ACCESS)) || role.isInternalUser()) {

					TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
							.getFirmInfoByContractId(
									currentContract.getContractNumber());
					if (firmInfo != null) {
						showEditTpaFirm = true;
					}
				}
			}
			userProfile.getContractProfile().setShowManageTpaFirmLink(
					showEditTpaFirm ? Boolean.TRUE : Boolean.FALSE);
		}
		
	}
	protected String validate(
            ActionForm actionForm, HttpServletRequest request) {

		Collection errors = new ArrayList();

		/*
		 * Prevent user from copy and paste URL.
		 */
		UserProfile userProfile = getUserProfile(request);

		try {
			setShowManageTpaFirmLink(request);

			Boolean canManageTpaFirm = userProfile.getContractProfile()
					.getShowManageTpaFirmLink();

			if (canManageTpaFirm == null || !canManageTpaFirm.booleanValue()) {
				return forwards.get(FORWARD_TPA_CONTACTS);
			}
		} catch (SystemException e) {
			/*
			 * We should never see this exception.
			 */
			GenericException ex = new GenericException(
					ErrorCodes.TECHNICAL_DIFFICULTIES);
			errors.add(ex);
		}

        TpaFirm form = (TpaFirm) actionForm;

		/*
		 * If this is a save action, we should compare the token and make sure
		 * it's still valid. Token is initialized in the doDefault() method and
		 * reset in the doSave() method.
		 */
		errors.addAll(doValidate( form, request));

		/*
		 * Errors are stored in the session so that our REDIRECT can look up the
		 * errors.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		return null;
    }
	protected Collection doValidate(
			ActionForm actionForm, HttpServletRequest request) {

		Collection errors = new ArrayList();
		TpaFirm form = (TpaFirm) actionForm;
		/*
		 * Validate the action form only when we save.
		 */
		if (form.isSaveAction()) {
			if (!form.getChanges().isChanged()) {
				GenericException ex = new GenericException(
						ErrorCodes.SAVING_WITH_NO_CHANGES);
				errors.add(ex);
			}

			if(!form.isBundledGaIndicator()){
				if (form.getContractAccess(0).isShowSigningAuthority() && form.getContractAccess(0).getSigningAuthority() && form.getEnableSigningAuthority()
	                    && form.getSelectedTPAUsersAsList().size() == 0) {
	                GenericException ex = new GenericException(ErrorCodes.NO_TPA_USERS_SELECTED);
	                errors.add(ex);
	            }
			}

			/*
	         * Disabled selections are not sent back from the browser. This is a
	         * workaround to clear those flags.
	         */
	        for (Iterator it = form.getContractAccesses().iterator(); it.hasNext();) {
	        	TPAUserContractAccess contractAccess = (TPAUserContractAccess) it.next();

	            if (contractAccess.getDirectDebit() != null
	                    && !contractAccess.getDirectDebit().booleanValue()) {
	                contractAccess.setSelectedDirectDebitAccounts(new String[0]);
	            }
	        }
		}

		return errors;
	}

    
}
