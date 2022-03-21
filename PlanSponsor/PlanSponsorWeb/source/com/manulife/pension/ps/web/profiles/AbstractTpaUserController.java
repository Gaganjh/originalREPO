package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.passcode.MobileMask;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;


public abstract class AbstractTpaUserController extends AbstractAddEditUserController {
	private static final String CLASS_NAME = AbstractTpaUserController.class.getName();

	protected static final String ADD_TPA_FIRM_ACTION = "addTpaFirm";

	protected static final String VIEW_PERMISSIONS = "viewPermissions";

	protected static final String NEW = "new";
	protected static final String MANAGE_USERS = "manageUsers";
	
	private static final String IS_SINGLE_TPA_FIRM_VIEW = "isSingleTPAFirmView";
	
	private static final String TPAUM = "tpaum";								//CL 110473
	
//	public static Properties properties = new Properties();
	
	/**
	 * Constructor.
	 */
	public AbstractTpaUserController(Class clazz) {
		super(clazz);
	}
	
	// Commented for PPM DM# 346328
	/*static {
    	InputStream propertyFileStream = null;
        try {
            propertyFileStream = Class.forName(CLASS_NAME)
                    .getClassLoader().getResourceAsStream(Constants.MANAGE_USER_HELPER_PROPERTIES_FILE_NAME);
            properties.load(propertyFileStream);
        } catch (Throwable e) {
            SystemException se = new SystemException(e,
                    "AbstractTpaUserAction - static: Static block failed for manage_user_helper.properties");
            throw ExceptionHandlerUtility.wrap(se);
        }finally{
        	try {
        		if(propertyFileStream != null)
        			propertyFileStream.close();
			} catch (IOException e) {
				SystemException se = new SystemException(e,
	                    "AbstractTpaUserAction - static: Static block failed for manage_user_helper.properties");
	            throw ExceptionHandlerUtility.wrap(se);
			}
        }
    }*/


	/**
	 * Sets the request parameters after each and every task.
	 */
	protected void postExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		super.postExecute( form, request, response);

		request.setAttribute(Constants.SHOW_TPA_PERMISSIONS_ONLY, "true");
	}



	protected void populateForm(HttpServletRequest request,
			AddEditUserForm form, UserInfo userInfo) throws SystemException {			
		// Commented for PPM DM# 346328
		//if(properties.getProperty(Constants.TPA_USER_EDIT_PERFORMANCE_IMPROVED_FLAG).equals(Constants.TPA_USER_EDIT_PERFORMANCE_IMPROVED_FLAG_ENABLED)){
			populateFormNonContractLevel(request, form, userInfo);
		/*} else {
			populateFormContractLevel(request, form, userInfo);
		}*/		
	}
	
	/* Commented for PPM DM# 346328
	protected void populateFormContractLevel(HttpServletRequest request,
			AddEditUserForm form, UserInfo userInfo) throws SystemException {
		
		SecurityServiceDelegate ssDelegate = SecurityServiceDelegate.getInstance();
        UserInfo loginUserInfo = ssDelegate.getUserInfo(getUserProfile(request).getPrincipal());

		form.setPasswordState(userInfo.getPasswordState());
		form.setUserRole(userInfo.getRole()); // needed for suspend/unsuspend/delete
		form.setTabOpen(false); // reset on new user load.
        form.setGenerateChangeTrackingMessage(false);
        form.setWebAccess(userInfo.isWebAccessInd());

		String ssn = userInfo.getSsn();
		if (!StringUtils.isBlank(ssn)) {
			form.setSsn(new Ssn(ssn));
		}
		form.setPhone(userInfo.getPhoneNumber());
		form.setExt(userInfo.getPhoneExtension());
		form.setFax(userInfo.getFax());	
        
        // Before populating the Tpa firms in the form, clear out any existing ones
        form.getTpaFirms().clear();

        //LS added to implement TPAUM
        form.setHiddenFirmExist(Boolean.FALSE);

        boolean isSingleTPAFirmView = (Boolean)request.getSession(false).getAttribute(IS_SINGLE_TPA_FIRM_VIEW);
        int currentContract = 0; 
        if (getUserProfile(request).getCurrentContract() != null) {
        	currentContract = getUserProfile(request).getCurrentContract().getContractNumber();
        }	
		TPAFirmInfo[] tpaFirms = userInfo.getTpaFirms();
		if (tpaFirms.length > 0) {
            for (TPAFirmInfo tpaFirmInfo : userInfo.getTpaFirms()) {
				TpaFirm tpaForm = new TpaFirm();
                tpaForm.setId(new Integer(tpaFirmInfo.getId()));

                tpaForm.setName(tpaFirmInfo.getName());

                EmailPreferences emailPrefs = form.getEmailPreferences();
                if (emailPrefs == null) {
                    emailPrefs = new EmailPreferences();
                }
                // Reloading the email preferences for each firm will consoliate them
                emailPrefs.loadPreferences(tpaFirmInfo.getContractPermission());
                form.setEmailPreferences(emailPrefs);

				tpaForm.setPersisted(true);

                TPAUserContractAccessActionHelper.populateContractAccess(tpaForm.getContractAccess(0), tpaFirmInfo.getContractPermission());

                if (loginUserInfo.getRole().isInternalUser()
                        || (loginUserInfo.getTpaFirm(tpaFirmInfo.getId()) != null && loginUserInfo.getTpaFirm(tpaFirmInfo.getId()).getContractPermission().isManageTpaUsers() && !tpaFirmInfo
                                .getContractPermission().isManageTpaUsers())) {
                    tpaForm.setHidden(false);

				// if TPA w/ Manage Users , filter firm access  STA.29/STA.30
                    TPAUserContractAccessActionHelper.filterFirmContractAccess(loginUserInfo, tpaForm);// DFS25, 3.3.5 rules

				// Populate warning flags
                TPAUserContractAccessActionHelper.setLastPermissionFlags(tpaForm, userInfo.getProfileId());

                } else {
                    tpaForm.setHidden(true);
                    form.setHiddenFirmExist(Boolean.TRUE);
                }
                
                if(isSingleTPAFirmView && 
            			(tpaFirmInfo.getContractToContractPermission().get(currentContract) == null )){
                	tpaForm.setHidden(true);
                    form.setHiddenFirmExist(Boolean.TRUE);
            	}
                
				form.getTpaFirms().add(tpaForm);
			}

			//  Sorts TPA firm by ID.
			Collections.sort(form.getTpaFirms(), new Comparator() {
				public int compare(Object o1, Object o2) {
					TpaFirm t1 = (TpaFirm) o1;
					TpaFirm t2 = (TpaFirm) o2;
					return t1.getId().compareTo(t2.getId());
				}
			});
		}

		// this is done here and not in loop above since email display rules
		// are no per firm, but depend on all firms(and contracts) associated with the profile
		evaluateEmailDisplayRules(form, request);
		
		// To display contract information, Only for Single TPA firm view
		if(isSingleTPAFirmView){
			ContractPermission permissions = userInfo.getContractPermission(currentContract);
			if(permissions != null){
				form.setPrimaryContact(permissions.isPrimaryContact());
				form.setSignatureReceivedAuthSigner(permissions.isSignatureReceivedAuthSigner());
			}
			
			form.setCurrentContractId(currentContract);
		}
		
					
	}*/
	
	protected void populateFormNonContractLevel(HttpServletRequest request,
			AddEditUserForm form, UserInfo userInfo) throws SystemException {

		SecurityServiceDelegate ssDelegate = SecurityServiceDelegate.getInstance();
        UserInfo loginUserInfo = ssDelegate.getUserInfo(getUserProfile(request).getPrincipal());

		form.setPasswordState(userInfo.getPasswordState());
		form.setUserRole(userInfo.getRole()); // needed for suspend/unsuspend/delete
		form.setTabOpen(false); // reset on new user load.
        form.setGenerateChangeTrackingMessage(false);
        form.setWebAccess(userInfo.isWebAccessInd());

		String ssn = userInfo.getSsn();
		if (!StringUtils.isBlank(ssn)) {
			form.setSsn(new Ssn(ssn));
		}
		form.setMobile(MobileMask.maskPhone(userInfo.getMobileNumber().toString()));
		form.setPhone(userInfo.getPhoneNumber());
		form.setExt(userInfo.getPhoneExtension());
		form.setFax(userInfo.getFax());	
        
        // Before populating the Tpa firms in the form, clear out any existing ones
        form.getTpaFirms().clear();

        //LS added to implement TPAUM
        form.setHiddenFirmExist(Boolean.FALSE);

        boolean isSingleTPAFirmView = (Boolean)request.getSession(false).getAttribute(IS_SINGLE_TPA_FIRM_VIEW);
        int currentContract = 0; 
        if (getUserProfile(request).getCurrentContract() != null) {
        	currentContract = getUserProfile(request).getCurrentContract().getContractNumber();
        }	
		TPAFirmInfo[] tpaFirms = userInfo.getTpaFirms();
		if (tpaFirms.length > 0) {
            for (TPAFirmInfo tpaFirmInfo : userInfo.getTpaFirms()) {
				TpaFirm tpaForm = new TpaFirm();
                tpaForm.setId(new Integer(tpaFirmInfo.getId()));

                tpaForm.setName(tpaFirmInfo.getName());

                EmailPreferences emailPrefs = form.getEmailPreferences();
                if (emailPrefs == null) {
                    emailPrefs = new EmailPreferences();
                }
                // Reloading the email preferences for each firm will consoliate them
                emailPrefs.loadPreferences(tpaFirmInfo.getContractPermission());
                form.setEmailPreferences(emailPrefs);

				tpaForm.setPersisted(true);
                TPAUserContractAccessActionHelper.populateContractAccess(tpaForm.getContractAccess(0), tpaFirmInfo.getContractPermission());
                if (loginUserInfo.getRole().isInternalUser()
                        || (loginUserInfo.getTpaFirm(tpaFirmInfo.getId()) != null && loginUserInfo.getTpaFirm(tpaFirmInfo.getId()).getContractPermission().isManageTpaUsers() && !tpaFirmInfo
                                .getContractPermission().isManageTpaUsers())) {
                    tpaForm.setHidden(false);
                    
                    // if TPA w/ Manage Users , filter firm access  STA.29/STA.30
                    TPAUserContractAccessActionHelper.filterFirmContractAccessNonContractLevel(loginUserInfo, tpaForm);// DFS25, 3.3.5 rules
                       
				// Populate warning flags
                //	Commented for Performance improvement
                //TPAUserContractAccessActionHelper.setLastPermissionFlags(tpaForm, userInfo.getProfileId());                
                if (!tpaForm.isNewFirm()) { // Added for setLastPermissionFlags()
                    if (tpaForm.getContractAccess(0).isShowTpaStaffPlanAccess()) {
                        tpaForm.setLastUserWithReceiveILoansEmailAndTPAStaffPlan(false);
                    }
                    tpaForm.setLastUserWithReceiveILoansEmail(false);
                }
                tpaForm.setLastUserWithManageUsers(false);
                tpaForm.setLastRegisteredUser(false);                
                

                } else {
                    tpaForm.setHidden(true);
                    form.setHiddenFirmExist(Boolean.TRUE);
                }
                
                if(isSingleTPAFirmView && 
            			(tpaFirmInfo.getContractToContractPermission().get(currentContract) == null )){
                	tpaForm.setHidden(true);
                    form.setHiddenFirmExist(Boolean.TRUE);
            	}
                
				form.getTpaFirms().add(tpaForm);
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
		}

		// this is done here and not in loop above since email display rules
		// are no per firm, but depend on all firms(and contracts) associated with the profile
		//evaluateEmailDisplayRules(form, request); skipped due to performance issue.
		
		// To display contract information, Only for Single TPA firm view
		if(isSingleTPAFirmView){
			ContractPermission permissions = userInfo.getContractPermission(currentContract);
			if(permissions != null){
				form.setPrimaryContact(permissions.isPrimaryContact());
				form.setSignatureReceivedAuthSigner(permissions.isSignatureReceivedAuthSigner());
			}
			
			form.setCurrentContractId(currentContract);
		}		
		
	}

	/**
	 * DFS22(add/edit TPA user), Section 3.3.5.2 Email display rules.
	 */
	protected void evaluateEmailDisplayRules(AddEditUserForm form, HttpServletRequest request) throws SystemException  {
		ContractServiceDelegate csd = ContractServiceDelegate.getInstance();
		TPAServiceDelegate tpaSD = TPAServiceDelegate.getInstance();

		// settings for the email preferences fly-out section of the screen.
		EmailPreferences ePrefs = form.getEmailPreferences();

		boolean internalUser = getUserProfile(request).getRole().isInternalUser();


		// these are the per Profile values(for all contracts on all firms on the profile)
		// this is hard to understand since the email stuff is not PER firm, but other display settings are
        boolean showiLoanEmail = false;
	    boolean aciOn = false;
	    
		Iterator firmIt = form.getTpaFirms().iterator(); // all firms associated with the Profile
		while (firmIt.hasNext()) {

            TpaFirm tpaFirmForm = (TpaFirm) firmIt.next();
            if (!tpaFirmForm.isRemoved()) {

                List caList = tpaFirmForm.getContractIds();
                if (caList == null) {
                    caList = tpaSD.getContractsByFirm(tpaFirmForm.getId().intValue()); // all contracts associated with the firm
                    tpaFirmForm.setContractIds(caList);
                }
                Iterator caIter = caList.iterator();
                while (caIter.hasNext()) {
                    int contractNumber = ((Integer) caIter.next()).intValue();
                    LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(
                            contractNumber);
                    if (!(loanSettings.isAllowOnlineLoans()) && (loanSettings.isLrk01())) {
                        showiLoanEmail = true;
                    }

                }
            }
		} // end while, contract for this firm

        ePrefs.setShowiLoanEmail(showiLoanEmail);
	}

	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		//ActionForward result = super.validate( actionForm, request);

		String result = super.validate( actionForm, request);
		AddEditUserForm form = (AddEditUserForm) actionForm;

		// if no error so far, and user did not already dismiss warnings...
		if (result == null && !form.isIgnoreWarnings()) {
			result = doWarningsValidation( form, request);
		}
		
		
		return result;
	}


	/**
	 * return null if no warnings generated
	 *
	 * @return
	 */
	protected abstract String doWarningsValidation(
			AddEditUserForm form, HttpServletRequest request);


	/*
	 * 	come back from sub-page (change permissions)
	 */
	public String doReload(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		AddEditUserForm form = (AddEditUserForm) actionForm;

		// possibly some attributes where change on the sub-page that don't show here
		// if so, we need to signal to jsp that warning need to be generated on cancel
		Iterator firmIterator = form.getUndeletedTpaFirms().iterator();
		while(firmIterator.hasNext()) {
			TpaFirm tpaFirm = (TpaFirm)firmIterator.next();
			if (tpaFirm.getContractAccess(0).getDetailedPermissionsSet()) { // sub-page visited and exit via continue
				if (!tpaFirm.getChanges().isChanged()) {
					form.setGenerateChangeTrackingMessage(true);
				}
			}
		}
//TODO
		//return mapping.getInputForward(); // just like doDefault's result
		return "input";
	}



	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
	 *      AutoForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String doDefault(
			AutoForm actionForm,ModelMap model,HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		String mapping=(String) model.get("mapping");
		if (logger.isDebugEnabled()) {
			logger.debug("entry --> doDefault");
		}
		
		long doDefaultCall = System.currentTimeMillis();
		
		// To identify Single TPA firm view
		AddEditUserForm form = (AddEditUserForm)actionForm;
		request.getSession(false).setAttribute(IS_SINGLE_TPA_FIRM_VIEW,
				(isViewUser(mapping) || isEditUser(mapping)) && form.isFromTPAContactsTab());

		/*
		 * Call super's doDefault. If it returns any forward other than the
		 * input forward, we just return.
		 */
		String forward = super.doDefault( actionForm,model,request,response);
		
		if (!forward.equals("input")) {
			return forward;
		}

		
		UserProfile userProfile = getUserProfile(request);
		
		long beforeMethodCall = System.currentTimeMillis();
		
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				getUserProfile(request).getPrincipal());
		
		long afterMethodCall = System.currentTimeMillis();
        logger.error("EDIT TPA Performance Trace - Calling ***getUserInfo()*** from AbstractTpaUserAction : " 
        		+"SecurityServiceDelegate.getInstance().getUserInfo: " 
        		+ "Start time: " + beforeMethodCall + ", End time: " + afterMethodCall 
        		+ ", Duration: " + (afterMethodCall - beforeMethodCall) + " ms ");

		if (TpaumHelper.isTPAUM(userProfile)) {
			List tpaumFirms =TpaumHelper.getTpaFirmsForTpaum(loginUserInfo
					.getTpaFirmsAsCollection());
			//populate tpaumFirms list for TPAUM
			form.setTpaumFirms(tpaumFirms);

			/*
			 * TTP.34 Default 1 Permission Section for single-firm TPAUMs Only
			 * [a] If the logged in user is a TPAUM and has only one firm where
			 * their access level is TPAUM, then the initial display of the page
			 * will include one permission section showing that one firm with
			 * access level and permissions displayed and defaulted as per
			 * requirements in section 3.3.4.2.
			 */
			if (isAddUser(mapping) && tpaumFirms.size() == 1) {
				TPAFirmInfo tpaFirm = (TPAFirmInfo) tpaumFirms.iterator().next();
				TpaFirm tpaForm = new TpaFirm(new Integer(tpaFirm.getId()),
						tpaFirm.getName(), new Integer(9999));
                tpaForm.setNewFirm(true);

				if (tpaForm != null) {
					tpaForm.getContractAccesses().add(
							getDefaultContractAccess());
					TPAUserContractAccessActionHelper.filterFirmContractAccess(
							loginUserInfo, tpaForm);
					setTpaFirmCommon(tpaForm, tpaFirm);
                    TPAUserContractAccessActionHelper.setLastPermissionFlags(tpaForm, 0);

					form.getTpaFirms().add(0, tpaForm);
					/*
					 * Add a dummy TPA firm to the cloned form to maintain the
					 * order.
					 */
					((AddEditUserForm) form.getClonedForm())
							.getTpaFirms().add(0, new TpaFirm());

                    evaluateEmailDisplayRules(form, request);
				}
			}
			request.setAttribute(TPAUM, TPAUM);									//CL 110473
		}
		
		long afterDoDefaultCall = System.currentTimeMillis();
		logger.error("EDIT TPA Performance Trace - " 
				+ " Stored Proc calls End in AbstractTpaUserAction.doDefault"
				+ " Start time = " + doDefaultCall + ", "
				+ " End time = " + afterDoDefaultCall + ", "
				+ " Total Duration = " + (afterDoDefaultCall - doDefaultCall) + " ms ");
		
		if (!isAddUser(mapping) && !isViewUser(mapping))
		{
            Collection<GenericException> errors = TPAUserContractAccessActionHelper.checkForLockOrDelete(userProfile, form.getProfileId(), form.getUserName());
            if (!errors.isEmpty()) {
                SessionHelper.setErrorsInSession(request, errors);
                
             // if user navigates through TPA Contacts tab, then redirect to TPA contacts tab
                if(form.isFromTPAContactsTab()){
                	//return mapping.findForward(Constants.FORWARD_TPA_CONTACTS);
                	return Constants.FORWARD_TPA_CONTACTS;
                }
                //return mapping.findForward(MANAGE_USERS);
                return MANAGE_USERS;
            }
		}

		resetTpaDropdowns( form, request);

        if (isAddUser(mapping)) {
            // Set the default email preferences for a new user
            EmailPreferences emailPreferences = form.getEmailPreferences();
            if (emailPreferences == null) {
                emailPreferences = new EmailPreferences();
                form.setEmailPreferences(emailPreferences);
            }
            emailPreferences.loadDefaults(new ThirdPartyAdministrator());
            
            boolean showiLoanEmail = false;
            
            if (userProfile.getCurrentContract() != null) { // null if tpa with multiple contracts navs to manage users directly
                int contractNumber = userProfile.getContractProfile().getContract()
                        .getContractNumber();

                LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(
                        contractNumber);
                if (!(loanSettings.isAllowOnlineLoans()) && (loanSettings.isLrk01())) {
                    showiLoanEmail = true;
                }

            }

            emailPreferences.setShowiLoanEmail(showiLoanEmail);
        }

        // The super class also does a clone. Remove this clone before re-cloning.
        Object clonedForm = form.getClonedForm();
        if (clonedForm != null) {
            clonedForm = null;
        }
		form.storeClonedForm();

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-- doDefault");
		}
		//TODO
		//return mapping.getInputForward();
		return "input";
	}


	public String doCancel(
			AutoForm actionForm,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		//TODO-Adding string variable mapping to avoid error(check with previos code)
				String mapping=(String) model.get("mapping");

			if (!isAddUser(mapping) && !isViewUser(mapping)) {
				LockServiceDelegate.getInstance().releaseLock(
						LockHelper.USER_PROFILE_LOCK_NAME,
						LockHelper.USER_PROFILE_LOCK_NAME + ((AddEditUserForm)actionForm).getProfileId());
			}
		    return super.doCancel( actionForm,model,request, response);
	}

	public String doSave(
			AutoForm actionForm,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		//TODO-Adding string variable mapping to avoid error(check with previos code)
		String mapping=(String) model.get("mapping");
		if (!isAddUser(mapping)) {
			LockServiceDelegate.getInstance().releaseLock(
					LockHelper.USER_PROFILE_LOCK_NAME,
					LockHelper.USER_PROFILE_LOCK_NAME + ((AddEditUserForm)actionForm).getProfileId());
		}
		//TODO
		//ActionForward forward = super.doSave( actionForm, request, response);
		String forward = super.doSave( actionForm,model,request, response);
		
		return forward;
	}

	protected void resetTpaDropdowns(
			AddEditUserForm form, HttpServletRequest request) {

		if (getUserProfile(request).getRole().isTPA() &&
				 getUserProfile(request).getRole().hasPermission(PermissionType.MANAGE_TPA_USERS)) {
			TpaumHelper.populateTpaFirmDropDown( form, request);
		}
	}


	protected void setTpaFirmCommon(TpaFirm firm, TPAFirmInfo info) throws SystemException {

		/*
		 * TTP.39 TPA Access Level Selection - Default to TPA When a firm is
		 * added, the system will default the access level to “TPA” (i.e. “TPA
		 * full access”)
		 *
		 */

		TPAUserContractAccess contractAccess = firm.getContractAccess(0);
		contractAccess.setPlanSponsorSiteRole(AccessLevelHelper.TPA_ACCESS);
		contractAccess.setManageUsers(false);

		setAddedFirmsUserPermissions(firm);
	}


   protected void setAddedFirmsUserPermissions(TpaFirm theTpaFirm) {
    	UserPermissions up = theTpaFirm.getContractAccess(0).getUserPermissions();

 		if (theTpaFirm.getContractAccess(0).getReviewIWithdrawals()) {
 			up.setViewAllWithdrawals(true);
 			up.setInitiateAndViewMyWithdrawals(true);
 			up.setReviewWithdrawals(true);
 		} else {
 			up.setViewAllWithdrawals(true);
 			up.setInitiateAndViewMyWithdrawals(false);
 			up.setReviewWithdrawals(false);
 		}
 		if (theTpaFirm.getContractAccess(0).isReviewLoans()) {
 			up.setViewAllLoans(true);
 			up.setInitiateLoans(true);
 			up.setReviewLoans(true);
 		} else {
 			up.setViewAllLoans(true);
 			up.setInitiateLoans(false);
 			up.setReviewLoans(false);
 		}
 		
    }


	protected TPAUserContractAccess getDefaultContractAccess() throws SystemException {
        TPAUserContractAccess access = new TPAUserContractAccess();
        TPAUserContractAccessActionHelper.populatePermissionDefaults(access, null);
        return access;
    }

	protected void clearSession( ModelMap model,HttpServletRequest request) {
        super.clearSession( model,request);
        request.getSession(false).removeAttribute(IS_SINGLE_TPA_FIRM_VIEW);
    }
	
}

