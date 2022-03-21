package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;


import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.pension.util.StaticHelperClass;

/**
 * A helper class for add/edit external user action. The goal is to cut down the
 * amount of non-workflow related codes in the actual action.
 *
 * @author Charles Chan
 */
public class ContractAccessActionHelper {

    private static final int MAX_ACCOUNT_LABEL_WIDTH = 9999;

    private static final Logger logger = Logger
            .getLogger(ContractAccessActionHelper.class);

//    private static final String PSEUM = new ExternalUserManager().toString();
//    private static final String TPAUM = new TPAUserManager().toString();
//    private static final String PA    = new PlanAdministrator().toString();
//    private static final String TPA   = new ThirdPartyAdministrator().toString();
      private static final String PAYROLL_ADMINISTRATOR = new PayrollAdministrator().toString();


    /**
     * Constructor.
     */
    private ContractAccessActionHelper() {
        super();
    }

    /**
     * This method populates a new contract access object from a contract
     * object. This method is only called when a new contract is added to the
     * user (either the first default one or subsequent ones).
     *
     * @param accessForm
     * @param contract
     * @throws SystemException
     */
    static void populateNewContractAccess(UserInfo loginUserInfo,
            AddEditUserForm actionForm, ContractAccess accessForm,
            Contract contract) throws SystemException {
        accessForm.setContractNumber(new Integer(contract.getContractNumber()));
        accessForm.setCompanyName(contract.getCompanyName());
       // TODO accessForm.setContractAllocated(new Boolean(contract.isContractAllocated()));
        populateDirectDebitAccountsFromUserInfo(accessForm, loginUserInfo, contract.getContractNumber());

        filterContractAccess(loginUserInfo, contract.getContractNumber(), accessForm);
    }

    public static void populateContractAccess(ContractAccess accessForm,
            ContractPermission contractPermission) throws SystemException {
        populateContractAccess(accessForm, contractPermission, null);
    }

    /**
     * Populates a contract access object with the given contract permission,
     * filtered by what is accessible by the user info.
     *
     * @param accessForm
     * @param contractPermission
     * @param filterUserInfo
     *
     * @throws SystemException
     */
    static void populateContractAccess(ContractAccess accessForm,
            ContractPermission contractPermission, UserInfo filterUserInfo)
            throws SystemException {
        accessForm.setContractNumber(new Integer(contractPermission
                .getContractNumber()));
        accessForm.setCompanyName(contractPermission.getCompanyName());
       /* TODO accessForm.setContractAllocated(new Boolean(contractPermission
                .isAllocated()));*/

        UserRole role = contractPermission.getRole();
        accessForm.setPlanSponsorSiteRole(role.toString());
        if (contractPermission.getRoleType() != null) {
            accessForm.setRoleType(contractPermission.getRoleType().getDisplayName());
        }
        if (role.isTPA())
        {
        	accessForm.setTpaUserManager(role.hasPermission(PermissionType.MANAGE_TPA_USERS));
        }
        else
        {
        	accessForm.setExternalUserManager(role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS));
        }

        /*
         * Set submission access flag. We need to also set showSubmissionAccess
         * flag here because an IUM needs this flag to determine which
         * permission to show. For external users, this flag may be further
         * modified by filterContractAccess.
         */
        accessForm.setSubmissionAccess(new Boolean(contractPermission.isSubmissionAccess()));
        accessForm.setShowSubmissionAccess(true);
        accessForm.setViewAllSubmissions(new Boolean(contractPermission.isViewAllSubmissions()));

        accessForm.setDirectDebit(new Boolean(contractPermission.isDirectDebit()));
        accessForm.setCashAccount(new Boolean(contractPermission.isCashAccount()));
        accessForm.setUploadSubmissions(new Boolean(contractPermission.isUploadSubmissions()));
        accessForm.setParticipantAddressDownloadAccessAvailable(new Boolean(
                contractPermission.isParticipantAddressDownloadAccessAvailable()));
        accessForm.setStatementsAccessAvailable(new Boolean(contractPermission.isStatementsAccessAvailable()));
        accessForm.setReportDownload(new Boolean(contractPermission.isReportDownload()));
//        accessForm.setDeferralEmail(new Boolean(contractPermission.isDeferralEmail()));
//        accessForm.setEnrollmentEmail(new Boolean(contractPermission.isEnrollmentEmail()));
        accessForm.setTpaStaffPlanAccess(new Boolean(contractPermission.isTpaStaffPlanAccess()));
//        accessForm.setReceiveIloansEmail(new Boolean(contractPermission.isReceiveIloansEmail()));
        accessForm.setUpdateCensusData(new Boolean(contractPermission.isUpdateCensusData()));
        accessForm.setViewSalary(new Boolean(contractPermission.isViewSalary()));
        accessForm.setReviewIWithdrawals(new Boolean(contractPermission.isReviewIWithdrawals()));
        accessForm.setEditApprovePlan(new Boolean(contractPermission.isEditPlanData()));

        if (filterUserInfo != null) {
            populateDirectDebitAccountsFromUserInfo(accessForm, filterUserInfo, contractPermission.getContractNumber());
            accessForm.setAccountsNotShown(hasDirectDebitAccountNotShown(
                    contractPermission.getContractNumber(), filterUserInfo,
                    contractPermission));
            setSelectedDirectDebitAccounts(contractPermission, accessForm);
            filterContractAccess(filterUserInfo, contractPermission.getContractNumber(), accessForm);
        } else {
            populateDirectDebitAccountsFromContractPermission(accessForm, contractPermission);
            setSelectedDirectDebitAccounts(contractPermission, accessForm);
            accessForm.setAccountsNotShown(false);
        }
    }

    /**
     * Unsets any permission that the logged in user do not have access to. This
     * is important to avoid a user creating another user with more privileges.
     *
     * @param filterUserInfo
     *            The logged in user's UserInfo object.
     * @param contractNumber
     *            The contract number of this contract access.
     * @param accessForm
     *            The contract access form.
     */
    public static void filterContractAccess(UserInfo filterUserInfo,
            int contractNumber, ContractAccess accessForm) {

        if (!(filterUserInfo.getRole() instanceof InternalUser)) {

            /*
             * Unset any permission that the login user does not have access to.
             * Once it's unset, JSP will suppressed it.
             */
        	ContractPermission filterContractPermission = filterUserInfo
                    .getContractPermission(contractNumber);

            if (filterContractPermission == null) {
                accessForm.setAllShowPermissions(false);
            } else {
                accessForm.setShowDirectDebit(filterContractPermission.isDirectDebit());
                accessForm.setShowCashAccount(filterContractPermission.isCashAccount());
                accessForm.setShowStatementsAccessAvailable(filterContractPermission.isStatementsAccessAvailable());
                accessForm.setShowUploadSubmissions(filterContractPermission.isUploadSubmissions());
                accessForm.setShowParticipantAddressDownloadAccessAvailable(filterContractPermission.isParticipantAddressDownloadAccessAvailable());
                accessForm.setShowReportDownload(filterContractPermission.isReportDownload());
//                accessForm.setShowDeferralEmail(filterContractPermission.isDeferralEmail());
//                accessForm.setShowEnrollmentEmail(filterContractPermission.isEnrollmentEmail());
                accessForm.setShowViewAllSubmissions(filterContractPermission.isViewAllSubmissions());
                accessForm.setShowSubmissionAccess(filterContractPermission.isSubmissionAccess());
                accessForm.setShowTpaStaffPlanAccess(filterContractPermission.isTpaStaffPlanAccess());
//                accessForm.setShowReceiveIloansEmail(filterContractPermission.isReceiveIloansEmail());
                accessForm.setUpdateCensusData(filterContractPermission.isUpdateCensusData());
                accessForm.setViewSalary(filterContractPermission.isViewSalary());
                accessForm.setReviewIWithdrawals(filterContractPermission.isReviewIWithdrawals());
                accessForm.setEditApprovePlan(filterContractPermission.isEditPlanData());

                /*
                 * ICE 11.2 Checks if there is any permission which is
                 * suppressed.
                 */
                if (filterContractPermission.isSubmissionAccess()) {
                    boolean permissionsNotShown = false;
                    if ((accessForm.isDirectDebit() && !filterContractPermission.isDirectDebit())
                            || (accessForm.isCashAccount() && !filterContractPermission.isCashAccount())
                            || (accessForm.isViewAllSubmissions() && !filterContractPermission.isViewAllSubmissions())
                            || (accessForm.isUploadSubmissions() && !filterContractPermission.isUploadSubmissions())) {
                        permissionsNotShown = true;
                    }
                    accessForm.setPermissionsNotShown(permissionsNotShown);
                }
            }
        }
    }

 /**
  * requiresPayrollEmailNotification
  * @param contractNumber
  */
	public static boolean requiresPayrollEmailNotification(int contractNumber) {
		if (contractNumber == 0) return false;

		String feature = null;
		try {
			com.manulife.pension.delegate.ContractServiceDelegate service = com.manulife.pension.delegate.ContractServiceDelegate.getInstance();
			ContractServiceFeature contractService = service.getContractServiceFeature(contractNumber,ServiceFeatureConstants.PAYROLL_PATH_FEATURE);
			if (contractService !=null)
				feature = (String)contractService.getValue();
			logger.debug("requiresPayrollEmailNotification : returned "+feature);
		} catch(SystemException e) {
			logger.error("Error occurred while executing requiresPayrollEmailNotification: "+e.getMessage());
			return false;
		} catch(ApplicationException e) {
			logger.error("In requiresPayrollEmailNotification: " + e.getMessage());
			return false;
		}
		return ContractServiceFeature.internalToBoolean(feature).booleanValue();
	}
     /**
     * Filters permissions for looged user TPUM
     *
     * @param filterUserInfo
     *            The logged in user's UserInfo object.
     * @param firmId
     *            The firmId of this contract access.
     * @param accessForm
     *            The contract access form.
     */
    public static void filterFirmContractAccess(UserInfo filterUserInfo,
            int firmId, ContractAccess accessForm) {
    	/*
    	 * Showing payrollpath email for TPA users
    	 */
    	try{
    	TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance()
        .getFirmInfoByContractId(filterUserInfo.getContractNumber());
    	}
    	catch(SystemException e){
    		logger.error("Error occurred while executing filterFirmContractAccess  "+e.toString());
    	}
        if (!(filterUserInfo.getRole() instanceof InternalUser)) {

            /*
             * Unset any permission that the login user does not have access to.
             * Once it's unset, JSP will suppressed it.
             */
            ContractPermission filterContractPermission = filterUserInfo
                    .getTpaFirm(firmId).getContractPermission();


            if (filterContractPermission == null) {
            //no permisssions for all
                accessForm.setAllShowPermissions(false);
            } else {


            	//debugPermission( filterContractPermission,"permissions in FilterFirmContractAccess: ");
            	/** TPA15 tpa staff plan access
            	 * If user is TPAUM who has this permission him/herself, then system
            	 * displays (and lets user change)  this permission.
            	 * If user is TPAUM who does not have this permission,
            	 * then system suppresses it.
            	 */

                accessForm.setShowTpaStaffPlanAccess(filterContractPermission
                        .isTpaStaffPlanAccess());

            	/** TTP.2 receive iloans email
            	 *  If user is a TPAUM then system displays  (and lets user change)
            	 *  this permission whether or not TPAUM has this permission themselves.
                 */
//                accessForm.setShowReceiveIloansEmail(true);

            	/**
            	 * TPA9	Electronic Submission tool permissions  Display & Default Rules
            	 * TPA9.1 If user is TPAUM who has this permission him/herself,
            	 * then system displays (and lets user change) this permission
            	 * If user is TPAUM who does not have this permission,
            	 * then system suppresses this permission and
            	 * suppresses all the sub-permissions..
            	*/
             		accessForm.setShowSubmissionAccess(filterContractPermission
             			.isSubmissionAccess());
             		//accessForm.setShowIfileAccessAvailable(filterContractPermission.isIfileAccessAvailable());
                     /*
                     * ICE 11.2 Checks if there is any permission which is
                     * suppressed.
                     */
             		boolean showPermissions = filterContractPermission.isSubmissionAccess();
             		// The below is replaced with the above one becasue there is no more iFile Permission
             		//boolean showPermissions = filterContractPermission.isIfileAccessAvailable()||filterContractPermission.isSubmissionAccess();

                       accessForm.setPermissionsNotShown(!showPermissions);
                 /* TPA9.1.1 upload
                 * If user is TPAUM who has this permission him/herself,
                 * then system displays (and lets user change) this permission
                 * If user is TPAUM who does not have this permission,
                 * then system displays and disables this permission.
                 */
                 accessForm.setShowUploadSubmissions(filterContractPermission
                                .isUploadSubmissions());
                  /* TPA9.1.2 view submissions
                  * If user is TPAUM who has this permission him/herself,
                  * then system displays (and lets user change) this permission
                  * If user is TPAUM who does not have this permission,
                  * then system displays and disables this permission.
                  */
                 accessForm.setShowViewAllSubmissions(filterContractPermission
                         .isViewAllSubmissions());
                  /* TPA9.1.4 cash account
                  * If user is TPAUM who has this permission him/herself,
                  * then system displays (and lets user change) this permission
                  * If user is TPAUM who does not have this permission,
                  * then system displays and disables this permission.
                  */
                 accessForm.setShowCashAccount(filterContractPermission
                         .isCashAccount());
                 /* TPA9.1.5 direct debit
                  * If user is TPAUM who has this permission him/herself,
                  * then system displays (and lets user change) this permission
                  * If user is TPAUM who does not have this permission,
                  * then system displays and disables this permission.
                 */
                accessForm.setShowDirectDebit(filterContractPermission
                        .isDirectDebit());

                /** TTP.40	Report Download
                 * If user is TPAUM who has this permission him/herself,
                 * then system displays (and lets user change)  this permission.
                 * If user is TPAUM who does not have this permission,
                 * then system suppresses it.
                */
                accessForm
				.setShowReportDownload(filterContractPermission
						.isReportDownload());

//            	/** TTP.42	Deferral email
//            	 * If user is TPAUM then system displays (and lets user change)
//            	 *  this permission whether or not TPAUM has this permission themselves.
//            	*/
//                accessForm.setShowDeferralEmail(true);
//
//
//            	/** TTP.41	Enrollment email
//            	 * If user is a TPAUM  then system displays (and lets user change)
//            	 *  this permission whether or not TPAUM has this permission themselves
//            	*/
//
//                accessForm.setShowEnrollmentEmail(true);

               	//to do: add TPA notifications
                //accessForm.setShowReceiveNotification(filterContractPermission
               //         .isReceiveNotification());

                /* Checks if there is any permission which is
                * suppressed.
                */
               if (filterContractPermission.isSubmissionAccess()) {
                   boolean permissionsNotShown = false;
                   if ((accessForm.isDirectDebit() && !filterContractPermission
                           .isDirectDebit())
                           || (accessForm.isCashAccount() && !filterContractPermission
                                   .isCashAccount())
                           || (accessForm.isViewAllSubmissions() && !filterContractPermission
                                   .isViewAllSubmissions())
                           || (accessForm.isUploadSubmissions() && !filterContractPermission
                                   .isUploadSubmissions())) {
                       permissionsNotShown = true;
                   }
                   accessForm.setPermissionsNotShown(permissionsNotShown);
               }
            }
       }
   }



    /**-----------------------------------------------------------------LS

    /**
     * Populates the given contract access object with the list of direct debit
     * accounts in the contract permission object. Also sets the
     * selectedAccounts attribute to be all of the accounts. This method is used
     * in DeleteProfileAction and RegisterAction to prepare the list of direct
     * debit accounts for display.
     *
     * @param contractAccess
     * @param contractPermission
     */
    public static void populateDirectDebitAccountsFromContractPermission(
            ContractAccess contractAccess, ContractPermission contractPermission) {
        /*
         * Set the list of direct debit accounts for this user.
         */
        String[] selectedAccounts = new String[contractPermission.getRole()
                .getDirectDebitAccounts().size()];
        int index = 0;
        for (Iterator it = contractPermission.getRole()
                .getDirectDebitAccounts().iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            BankAccount bankAccount = BankAccount.getBankAccount(account);
            bankAccount.setLabel(FormatUtils.formatAccountName(account,
                    MAX_ACCOUNT_LABEL_WIDTH));

            selectedAccounts[index] = bankAccount.getPrimaryKey();
            index++;
            contractAccess.getDirectDebitAccounts().add(bankAccount);
        }
        contractAccess.setSelectedDirectDebitAccounts(selectedAccounts);
    }

    /**
     *
     * Populates the given contract access form with direct debit accounts
     * available to the login user for the given contract.
     *
     * @param accessForm
     * @param userInfo
     * @param contractNumber
     *
     * @throws SystemException
     */
    private static void populateDirectDebitAccountsFromUserInfo(
            ContractAccess accessForm, UserInfo userInfo, int contractNumber)
            throws SystemException {

        accessForm.getDirectDebitAccounts().clear();

        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        List directDebitAccounts = service
                .getDirectDebitAccounts(contractNumber);
        List allowedDirectDebitAccounts = null;

        if (!(userInfo.getRole() instanceof InternalUser)) {
            /*
             * If login user is NOT an internal user, we limit the list of
             * direct debit accounts to the ones the current user have access
             * to.
             */
            ContractPermission loginUserPermission = userInfo
                    .getContractPermission(contractNumber);

            if (loginUserPermission != null) {
                allowedDirectDebitAccounts = loginUserPermission
                        .getDirectDebitAccounts();
            } else {
                allowedDirectDebitAccounts = new ArrayList();
            }
        }

        if (directDebitAccounts != null) {
            for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
                DirectDebitAccount account = (DirectDebitAccount) it.next();
                BankAccount bankAccount = BankAccount.getBankAccount(account);
                bankAccount.setLabel(FormatUtils.formatAccountName(account,
                        MAX_ACCOUNT_LABEL_WIDTH));
                if (!(userInfo.getRole() instanceof InternalUser)) {
                    if (!allowedDirectDebitAccounts.contains(account)) {
                        bankAccount.setNoAccess(true);
                    }
                }
                accessForm.getDirectDebitAccounts().add(bankAccount);
            }
        }
    }

    /**
     * Set the selected direct debit accounts based on the given contract
     * permission. This method does not expose any additional bank account that
     * the login user does not have access to. Remember that the initial list of
     * accounts (stored in the action form) comes from what the login user have
     * access to.
     *
     * @param permission
     * @param accessForm
     */
    private static void setSelectedDirectDebitAccounts(
            ContractPermission permission, ContractAccess accessForm) {

        String[] selectedAccounts = new String[accessForm
                .getDirectDebitAccounts().size()];

        int index = 0;

        /*
         * Goes through each of the direct debit accounts the user have access
         * to, selects only those that are available.
         */
        for (Iterator it = permission.getRole().getDirectDebitAccounts()
                .iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            BankAccount bankAccount = BankAccount.getBankAccount(account);
            if (accessForm.getDirectDebitAccounts().contains(bankAccount)) {
                selectedAccounts[index] = bankAccount.getPrimaryKey();
                index++;
            }
        }
        accessForm.setSelectedDirectDebitAccounts(selectedAccounts);
    }

    /**
     * Populates the given contract permission object with the direct debit
     * accounts from the given contract access form.
     *
     * @param loginUserInfo
     * @param permission
     * @param access
     */
    static void populateDirectDebitAccounts(ContractPermission permission,
            ContractAccess access) {

        permission.getRole().getDirectDebitAccounts().clear();

        List selectedAccounts = access.getSelectedDirectDebitAccountsAsList();
        for (Iterator it = selectedAccounts.iterator(); it.hasNext();) {
            BankAccount account = (BankAccount) it.next();
            DirectDebitAccount directDebitAccount = account
					.getDirectDebitAccount();
			permission.getRole().getDirectDebitAccounts().add(
					directDebitAccount);
        }

        /*
         * Find out which direct debit accounts are deselected.
         */
        permission.getRole().getRemovedDirectDebitAccounts().clear();
        for (Iterator it = access.getDirectDebitAccounts().iterator(); it
                .hasNext();) {
            BankAccount account = (BankAccount) it.next();
            DirectDebitAccount directDebitAccount = account
					.getDirectDebitAccount();
            if (!permission.getRole().getDirectDebitAccounts().contains(
                    directDebitAccount)) {
                permission.getRole().getRemovedDirectDebitAccounts().add(
                        directDebitAccount);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Contract Access Accounts:\n"
                    + StaticHelperClass.toXML(access.getDirectDebitAccounts()));
            logger.debug("User Info Selected Accounts:\n"
                    + StaticHelperClass.toXML(permission.getRole()
                            .getDirectDebitAccounts()));
            logger.debug("User Info Removed Accounts:\n"
                    + StaticHelperClass.toXML(permission.getRole()
                            .getRemovedDirectDebitAccounts()));
        }
    }

    /**
     * Obtains the list of contracts (in the form of Integer objects) that the
     * user is an external user manager of.
     *
     * @param userName
     *            The user name to check.
     * @return
     */
    static List getExternalUserManagerContracts(String userName) {
        List contracts = null;
        try {
            UserInfo userInfo = SecurityServiceDelegate.getInstance()
                    .searchByUserName(
                            AbstractAddEditUserController.newPrincipal(new Long(0),
                                    null, null), userName);
            ContractPermission[] editUserContracts = userInfo
                    .getContractPermissions();
            contracts = new ArrayList(editUserContracts.length);
            for (int i = 0; i < editUserContracts.length; i++) {
            	UserRole contractUserRole = editUserContracts[i].getRole();
                if (contractUserRole.isExternalUser() && contractUserRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
                    contracts.add(new Integer(editUserContracts[i]
                            .getContractNumber()));
                }
            }
        } catch (Exception e) {
            /*
             * This should not happen because we are only retrieving the user
             * info object using a generic principal.
             */
            throw new NestableRuntimeException(e);
        }
        return contracts;
    }

    /**
     * Prepares the request. Specifically, it sets up the contract drop down
     * list if the user is a PSEUM and has multiple contracts.
     *
     * @param actionForm
     * @param request
     */
    static void populateContractDropDown(
            ActionForm actionForm, HttpServletRequest request) {

        UserProfile userProfile = PsController.getUserProfile(request);
        AddEditUserForm form = (AddEditUserForm) actionForm;
        UserRole userRole = userProfile.getRole();

        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				PsController.getUserProfile(request).getPrincipal());

        /*
		 * MPR 20 & MPR 21. Show drop down list of contracts if logged in user
		 * is an PSEUM and has multiple contracts.
		 */
        if (userRole.isExternalUser() && userRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
//                && userProfile.isMultipleContracts()) {

            ContractPermission[] contracts = loginUserInfo
                    .getContractPermissions();

            /*
             * The labels are sorted by contract IDs in ascending order.
             */
            Set contractLabelValueBeans = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    LabelValueBean bean1 = (LabelValueBean) o1;
                    LabelValueBean bean2 = (LabelValueBean) o2;
                    Integer contractNumber1 = new Integer(bean1.getValue());
                    Integer contractNumber2 = new Integer(bean2.getValue());
                    return contractNumber1.compareTo(contractNumber2);
                }
            });

            /*
             * Obtains the list of contracts that the edited user is a PSEUM of.
             */
            List editUserPseumContracts;
            //TODO Spring changes
            /*if (!AbstractAddEditUserAction.isAddUser(mapping)) {
                editUserPseumContracts = getExternalUserManagerContracts(form
                        .getUserName());
            } else {*/
                editUserPseumContracts = new ArrayList();
           /* }*/

            for (int i = 0; i < contracts.length; i++) {

                /*
                 * Drop down should only contains contracts that the login user
                 * is a PSEUM of.
                 */
            	UserRole contractUserRole = contracts[i].getRole();

                if (!(contractUserRole.isExternalUser() && contractUserRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS))) {
                    continue;
                }

                int contractId = contracts[i].getContractNumber();

                /*
                 * Also, if the edited user is a PSEUM of any contract, the drop
                 * down should not show the contract.
                 */
                if (editUserPseumContracts.contains(new Integer(contractId)) ||
                		(userProfile.getCurrentContract() != null && 
                				userProfile.getCurrentContract().getContractNumber() == contractId)) {
                    continue;
                }

                /*
                 * Skip all contracts that is already in the form.
                 */
                boolean contractFound = false;

                for (Iterator it2 = form.getContractAccesses().iterator(); it2
                        .hasNext()
                        && !contractFound;) {
                    ContractAccess accessForm = (ContractAccess) it2.next();
                    if (accessForm.getContractNumber().intValue() == contractId) {
                        contractFound = true;
                    }
                }

                if (contractFound) {
                    continue;
                }

                String label = contractId + " " + contracts[i].getCompanyName();

                contractLabelValueBeans.add(new LabelValueBean(label, String
                        .valueOf(contractId)));
            }

            /*
             * Add an empty label.
             */
            List contractList = new ArrayList();
            if (contractLabelValueBeans.size() > 0) {
                contractList.add(new LabelValueBean("", ""));
                contractList.addAll(contractLabelValueBeans);
	            request.setAttribute(Constants.ADD_EDIT_USER_CONTRACT_LIST,
	                    contractList);
            }
        }
    }

    /**
     * Checks whether there is any direct debit account that is not shown to the
     * editor.
     *
     * @param contractNumber
     * @param filterUserInfo
     * @param permissionToCheck
     * @return
     */
    static boolean hasDirectDebitAccountNotShown(int contractNumber,
            UserInfo filterUserInfo, ContractPermission permissionToCheck) {

        /*
         * For internal user, all accounts are shown.
         */
        if (filterUserInfo.getRole() instanceof InternalUser) {
            return false;
        }

        /*
         * If the permission to check has no direct debit account, that means no
         * account is filtered.
         */
        if (permissionToCheck.getDirectDebitAccounts().size() == 0) {
            return false;
        }

        ContractPermission filterUserPermission = filterUserInfo
                .getContractPermission(contractNumber);

        /*
         * If filter user has fewer direct debit accounts, we can be sure that
         * some accounts are not shown on the screen.
         */
        if (filterUserPermission.getDirectDebitAccounts().size() < permissionToCheck
                .getDirectDebitAccounts().size()) {
            return true;
        }

        for (Iterator it = permissionToCheck.getDirectDebitAccounts()
                .iterator(); it.hasNext();) {
            DirectDebitAccount account = (DirectDebitAccount) it.next();
            if (!filterUserPermission.getDirectDebitAccounts()
                    .contains(account)) {
                return true;
            }
        }
        return false;
    }

    static void populateContractPermission(ContractPermission permission, ContractAccess access) {

        permission.setContractNumber(access.getContractNumber() == null ? 0 : access.getContractNumber().intValue());
        permission.setCashAccount(access.isCashAccount());
        permission.setDirectDebit(access.isDirectDebit());
        permission.setSubmissionAccess(access.isViewSubmissions());
        permission.setViewAllSubmissions(access.isViewAllSubmissions());
        permission.setUploadSubmissions(access.isUploadSubmissions());
        permission.setStatementsAccessAvailable(access.isStatementsAccessAvailable());
        permission.setParticipantAddressDownloadAccessAvailable(access.isParticipantAddressDownloadAccessAvailable());
        permission.setTpaStaffPlanAccess(access.getTpaStaffPlanAccess());
//        permission.setReceiveIloansEmail(access.isReceiveIloansEmail());
        permission.setReportDownload(access.getReportDownload().booleanValue());
//        permission.setDeferralEmail(access.isDeferralEmail());
//        permission.setEnrollmentEmail(access.isEnrollmentEmail());

        permission.setUpdateCensusData(access.getUpdateCensusData().booleanValue());
        permission.setViewSalary(access.getViewSalary().booleanValue());
        permission.setReviewIWithdrawals(access.getReviewIWithdrawals().booleanValue());
        permission.setEditPlanData(access.getEditApprovePlan().booleanValue());


        populateDirectDebitAccounts(permission, access);
    }

    private static void debugPermission(ContractPermission p, String message)
    {
       	StringBuffer str = new StringBuffer();
    	//str.append(StaticHelperClass.toString(this))
		str.append(", IsDirectDebit()")
    	.append(p.isDirectDebit())
		.append(", .isCashAccount()")
		.append(p.isCashAccount())
		.append(", isStatementsAccessAvailable()")
		.append(p.isStatementsAccessAvailable())
//		.append(", isIfileAccessAvailable()")
//		.append(p.isIfileAccessAvailable())
		.append(", isUploadSubmissions()")
		.append(p.isUploadSubmissions())
		.append(", isParticipantAddressDownloadAccessAvailable()")
		.append(p.isParticipantAddressDownloadAccessAvailable())
		.append(", isReportDownload()")
		.append(p.isReportDownload())
		.append(", isViewAllSubmissions()")
		.append(p.isViewAllSubmissions())
		.append(", isSubmissionAccess()")
		.append (p.isSubmissionAccess())
		.append(", isTpaStaffPlanAccess()")
		.append(p.isTpaStaffPlanAccess())
		.append(", isReceiveIloansEmail()")
		.append(p.isReceiveIloansEmail());

		System.out.println(message);
    	System.out.println(str);

    }
 
}
