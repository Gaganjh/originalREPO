package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.CAR;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.SuperCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;
import com.manulife.pension.service.util.FormatUtils;

/**
 * A helper class for populating user permissions.
 * 
 * @author Aron Rogers
 */
public class UserPermissionsHelper {

    private static final int MAX_ACCOUNT_LABEL_WIDTH = 30;

    /**
     * Constructor.
     */
    private UserPermissionsHelper() {
        super();
    }

    /**
     * Create a UserPermissions object based on the role of the logged in and selected users for the given contract.
     * 
     * @param loggedInRole
     * @param selectedRole
     * @param contract
     * @return UserPermissions
     * @throws SystemException
     */
    public static UserPermissions getNewUserPermissions(UserRole loggedInRole, UserRole selectedRole, Contract contract) throws SystemException {
        return createUserPermissions(loggedInRole, selectedRole, contract, true);
    }

    /**
     * Create a new UserPermissions object based on the role of the logged in and a new user for the given contract.
     * 
     * @param loggedInRole
     * @param selectedRole
     * @param contract
     * @return UserPermissions
     * @throws SystemException
     */
    public static UserPermissions getUserPermissions(UserRole loggedInRole, UserRole selectedRole, Contract contract) throws SystemException {
        return createUserPermissions(loggedInRole, selectedRole, contract, false);
    }

    private static UserPermissions createUserPermissions(UserRole loggedInRole, UserRole selectedRole, Contract contract, boolean newUser) throws SystemException {

        UserPermissions userPermissions = new UserPermissions();

        if (selectedRole.getDefaultRolePermissions() == null) {
            selectedRole.setDefaultRolePermissions(getDefaultRolePermissions(selectedRole));
        }

        populateUserPermissions(userPermissions, selectedRole, newUser);
        populateDirectDebitAccounts(userPermissions, loggedInRole, contract);
        setSelectedDirectDebitAccounts(userPermissions, selectedRole);
        filterUserPermissions(userPermissions, loggedInRole, contract, newUser);
        
        
        //Notice Manager
        
        if(!filterOnNoticeManagerPermission(userPermissions, loggedInRole, contract, newUser)){
        	userPermissions.setShowNoticeManager(false);
        	userPermissions.setNoticeManager(false);
        }
        if(loggedInRole.isInternalUser() 
        		&& !isNoticeManagerCCSRepCheck(loggedInRole) 
        		&& selectedRole instanceof IntermediaryContact){
        	userPermissions.setNoticeManagerDefault(DefaultRolePermissions.TRUE);
        
        }
        // Introducing Signing Authority Permission that can be used for both loans and withdrawals
        // Online Loans project requirements are replacing the existing approve withdrawal permission
        // The signing authority internally uses the same database column that is set for approve withdrawal permission
        // Business rule in displaying the permission varies from the approve withdrawals
        if (contract != null) {
        	if (contract.isBusinessConverted()) {
        		if ((selectedRole != null) && (selectedRole.getDefaultRolePermissions() != null)) {
        			String defaultPermissionValue = selectedRole.getDefaultRolePermissions().getDefaultPermissionValue(PermissionType.SIGNING_AUTHORITY);
        			if (!(DefaultRolePermissions.NOT_APPLICABLE.equals(defaultPermissionValue))) {
        				userPermissions.setShowSigningAuthority(true);
        			}
        		}
        	}
        }
        // if Who will review the iloans and iwithdrawals is set 
        // to PS, User roles = AUS/TRT/ADC (new users) are given the permission to 
        // review.
       
    	populateReviewWithdrawalsPermissionAsPerCSF(contract, userPermissions, selectedRole, newUser);
    	populateReviewLoansPermissionAsPerCSF(contract, userPermissions, selectedRole, newUser);
 
        return userPermissions;
    }

    private static void populateUserPermissions(UserPermissions userPermissions, UserRole selectedRole, boolean newUser) throws SystemException {

        DefaultRolePermissions defaultPermissions = selectedRole.getDefaultRolePermissions();

        // User management
        userPermissions.setManageUsersDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.MANAGE_EXTERNAL_USERS));
        userPermissions.setManageUsers(hasPermission(PermissionType.MANAGE_EXTERNAL_USERS, selectedRole, newUser));

        userPermissions.setSelectedAccessDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SELECTED_ACCESS));
        userPermissions.setSelectedAccess(hasPermission(PermissionType.SELECTED_ACCESS, selectedRole, newUser));

        userPermissions.setEditContractServiceFeaturesDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.EDIT_SERVICE_FEATURES));
        userPermissions.setEditContractServiceFeatures(hasPermission(PermissionType.EDIT_SERVICE_FEATURES, selectedRole, newUser));

        // Reporting
        userPermissions.setDownloadReportsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REPORT_DOWNLOAD));
        userPermissions.setDownloadReports(hasPermission(PermissionType.REPORT_DOWNLOAD, selectedRole, newUser));

        userPermissions.setEmployerStatementsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.EMPLOYER_STATEMENT_ACCESS));
        userPermissions.setEmployerStatements(hasPermission(PermissionType.EMPLOYER_STATEMENT_ACCESS, selectedRole, newUser));

        userPermissions.setEditPlanDataDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.EDIT_PLAN_DATA));
        userPermissions.setEditPlanData(hasPermission(PermissionType.EDIT_PLAN_DATA, selectedRole, newUser));

        // Client services
        // Submissions
        userPermissions.setViewSubmissionsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SUBMISSION_ACCESS));
        userPermissions.setViewSubmissions(hasPermission(PermissionType.SUBMISSION_ACCESS, selectedRole, newUser));

        userPermissions.setCreateUploadSubmissionsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.UPLOAD_SUBMISSIONS));
        userPermissions.setCreateUploadSubmissions(hasPermission(PermissionType.UPLOAD_SUBMISSIONS, selectedRole, newUser));

        userPermissions.setViewAllUsersSubmissionsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_ALL_SUBMISSIONS));
        userPermissions.setViewAllUsersSubmissions(hasPermission(PermissionType.VIEW_ALL_SUBMISSIONS, selectedRole, newUser));

        userPermissions.setCashAccountDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.CASH_ACCOUNT_ACCESS));
        userPermissions.setCashAccount(hasPermission(PermissionType.CASH_ACCOUNT_ACCESS, selectedRole, newUser));

        userPermissions.setDirectDebitDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.DIRECT_DEBIT_ACCOUNT));
        userPermissions.setDirectDebit(hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT, selectedRole, newUser));

        // i:withdrawals
        userPermissions.setInitiateAndViewMyWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE));
        userPermissions.setInitiateAndViewMyWithdrawals(hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE, selectedRole, newUser));

        userPermissions.setViewAllWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_ALL_WITHDRAWALS));
        userPermissions.setViewAllWithdrawals(hasPermission(PermissionType.VIEW_ALL_WITHDRAWALS, selectedRole, newUser));

        userPermissions.setReviewWithdrawalsDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REVIEW_WITHDRAWALS));
        userPermissions.setReviewWithdrawals(hasPermission(PermissionType.REVIEW_WITHDRAWALS, selectedRole, newUser));
 
        
        // Signing Authority
        userPermissions.setSigningAuthorityDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.SIGNING_AUTHORITY));
        userPermissions.setSigningAuthority(hasPermission(PermissionType.SIGNING_AUTHORITY, selectedRole, newUser));

        // Employee Management
        userPermissions.setUpdateCensusDataDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.UPDATE_CENSUS_DATA));
        userPermissions.setUpdateCensusData(hasPermission(PermissionType.UPDATE_CENSUS_DATA, selectedRole, newUser));

        userPermissions.setViewSalaryDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_SALARY));
        userPermissions.setViewSalary(hasPermission(PermissionType.VIEW_SALARY, selectedRole, newUser));
        
        // Loans permissions
        userPermissions.setInitiateLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.INITIATE_LOANS));
        userPermissions.setInitiateLoans(hasPermission(PermissionType.INITIATE_LOANS, selectedRole, newUser));
        
        userPermissions.setViewAllLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.VIEW_ALL_LOANS));
        userPermissions.setViewAllLoans(hasPermission(PermissionType.VIEW_ALL_LOANS, selectedRole, newUser));
        
        userPermissions.setReviewLoansDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.REVIEW_LOANS));
        userPermissions.setReviewLoans(hasPermission(PermissionType.REVIEW_LOANS, selectedRole, newUser));
        
        //Notice Manager
        
        userPermissions.setNoticeManagerDefault(defaultPermissions.getDefaultPermissionValue(PermissionType.ACCESS_NOTICE_MANAGER));
        userPermissions.setNoticeManager(hasPermission(PermissionType.ACCESS_NOTICE_MANAGER, selectedRole, newUser));
        
    }

    private static boolean hasPermission(PermissionType permissionsType, UserRole userRole, boolean newUser) {
        boolean hasPermission = false;

        String defaultPermissionValue = userRole.getDefaultRolePermissions().getDefaultPermissionValue(permissionsType);
           
        
       if (DefaultRolePermissions.TRUE.equals(defaultPermissionValue)) {
            hasPermission = true;
        } else if (DefaultRolePermissions.NOT_APPLICABLE.equals(defaultPermissionValue)) {
            hasPermission = false;
        } else {
            if (newUser) {
                hasPermission = DefaultRolePermissions.YES.equals(defaultPermissionValue);
            } else {
                hasPermission = userRole.hasPermission(permissionsType);
            }
        }
       
       
        return hasPermission;
    }
    
    public static boolean hasDirectDebitPermission(UserRole userRole) {
    	
    	if(userRole.getDefaultRolePermissions() == null){
    		return false;
    	}
    	
        return hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT, userRole, false);
    }
 
    private static void filterUserPermissions(UserPermissions userPermissions, UserRole loggedInRole, Contract contract, boolean newUser)
            throws SystemException {

        // First filter the form based on the defaults
        filterOnPermissionsDefault(userPermissions);

        // If the logged in user is not internal, filter based on permissions for this contract
        if (!loggedInRole.isInternalUser()) {
            filterOnUserPermissions(userPermissions, loggedInRole);
        }

        // If the contract is business converted we need to check a few CSFs
        if (contract.isBusinessConverted()) {
            filterOnContractServiceFeatures(userPermissions, contract);
        }

         // Now the special case stuff
        if (!loggedInRole.isInternalUser()) {
            userPermissions.setShowManageUsers(false);
        }

        // SAC.105 If a TPA Firm for the contract has "Review i:withdrawals" permission set to "yes", then the default value for the Client profile's "Review i:withdrawals"
        // permission will be "no'. This overrides the normal default value which is determined by role.
        if (userPermissions.isShowReviewWithdrawals() && DefaultRolePermissions.YES.equals(userPermissions.getReviewWithdrawalsDefault())) {
            TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contract.getContractNumber());
            if (firmInfo != null && firmInfo.getContractPermission() != null && firmInfo.getContractPermission().isReviewIWithdrawals()) {
                userPermissions.setReviewWithdrawalsDefault(DefaultRolePermissions.NO);
                if (newUser) {
                    userPermissions.setReviewWithdrawals(false);
                }
            }
        }


    }

    private static void filterOnPermissionsDefault(UserPermissions userPermissions) {
        // If the form value is N/A then the field isn't visible
        userPermissions.setShowManageUsers(permissionApplicable(userPermissions.getManageUsersDefault()));
        userPermissions.setShowSelectedAccess(permissionApplicable(userPermissions.getSelectedAccessDefault()));
        userPermissions.setShowEditContractServiceFeatures(permissionApplicable(userPermissions.getEditContractServiceFeaturesDefault()));

        // Reporting
        userPermissions.setShowDownloadReports(permissionApplicable(userPermissions.getDownloadReportsDefault()));
        userPermissions.setShowEmployerStatements(permissionApplicable(userPermissions.getEmployerStatementsDefault()));

        // Plan services
        userPermissions.setShowEditPlanData(permissionApplicable(userPermissions.getEditPlanDataDefault()));

        // Client services
        // Submissions
        userPermissions.setShowViewSubmissions(permissionApplicable(userPermissions.getViewSubmissionsDefault()));
        userPermissions.setShowCreateUploadSubmissions(permissionApplicable(userPermissions.getCreateUploadSubmissionsDefault()));
        userPermissions.setShowViewAllUsersSubmissions(permissionApplicable(userPermissions.getViewAllUsersSubmissionsDefault()));
        userPermissions.setShowCashAccount(permissionApplicable(userPermissions.getCashAccountDefault()));
        userPermissions.setShowDirectDebit(permissionApplicable(userPermissions.getDirectDebitDefault()));

        // i:withdrawals
        userPermissions.setShowInitiateAndViewMyWithdrawals(permissionApplicable(userPermissions.getInitiateAndViewMyWithdrawalsDefault()));
        userPermissions.setShowViewAllWithdrawals(permissionApplicable(userPermissions.getViewAllWithdrawalsDefault()));
        userPermissions.setShowReviewWithdrawals(permissionApplicable(userPermissions.getReviewWithdrawalsDefault()));
        
        // Signing authority 
        userPermissions.setShowSigningAuthority(permissionApplicable(userPermissions.getSigningAuthorityDefault()));

        // Employee Management
        userPermissions.setShowUpdateCensusData(permissionApplicable(userPermissions.getUpdateCensusDataDefault()));
        userPermissions.setShowViewSalary(permissionApplicable(userPermissions.getViewSalaryDefault()));
        
        // Loans
        userPermissions.setShowInitiateLoans(permissionApplicable(userPermissions.getInitiateLoansDefault()));
        userPermissions.setShowViewAllLoans(permissionApplicable(userPermissions.getViewAllLoansDefault()));
        userPermissions.setShowReviewLoans(permissionApplicable(userPermissions.getReviewLoansDefault()));
        
        //Notice Manager Permission
        userPermissions.setShowNoticeManager(permissionApplicable(userPermissions.getNoticeManagerDefault()));
        
        
    }
    
    private static boolean filterOnNoticeManagerPermission(UserPermissions userPermissions, UserRole loggedInRole, Contract contract,boolean newUser) throws SystemException{
    	boolean noticeManagerDisplay = false;
    	try {
    		noticeManagerDisplay = NoticeManagerUtility.validateProductRestriction(contract)
    		||(Contract.STATUS_OTHER.equals(contract.getStatus())|| NoticeManagerUtility.validateContractRestriction(contract))
    		|| NoticeManagerUtility.validateDIStatus(contract,loggedInRole);
    	} catch (ContractDoesNotExistException e) {
    		throw new SystemException(e, "Exception while retrieving contract details");
    	}
    	if(noticeManagerDisplay ){
    		//suppress Access to Notice manager and display the no indicator
    		return false;
    	}

    	return true;

    }
    

    private static boolean permissionApplicable(String defaultPermissionValue) {
        return !(defaultPermissionValue == null || DefaultRolePermissions.NOT_APPLICABLE.equals(defaultPermissionValue));
    }

    private static void filterOnUserPermissions(UserPermissions userPermissions, UserRole loggedInRole) {
        // User management
        if (!loggedInRole.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS)) {
            userPermissions.setShowManageUsers(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.EDIT_SERVICE_FEATURES)) {
            userPermissions.setShowEditContractServiceFeatures(false);
        }

        // Reporting
        if (!loggedInRole.hasPermission(PermissionType.REPORT_DOWNLOAD)) {
            userPermissions.setShowDownloadReports(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.EMPLOYER_STATEMENT_ACCESS)) {
            userPermissions.setShowEmployerStatements(false);
        }

        // Client services
        // Submissions
        if (!loggedInRole.hasPermission(PermissionType.SUBMISSION_ACCESS)) {
            userPermissions.setShowViewSubmissions(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.UPLOAD_SUBMISSIONS)) {
            userPermissions.setShowCreateUploadSubmissions(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.VIEW_ALL_SUBMISSIONS)) {
            userPermissions.setShowViewAllUsersSubmissions(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.CASH_ACCOUNT_ACCESS)) {
            userPermissions.setShowCashAccount(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT)) {
            userPermissions.setShowDirectDebit(false);
        }

        // i:withdrawals
        if (!loggedInRole.hasPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE)) {
            userPermissions.setShowInitiateAndViewMyWithdrawals(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.VIEW_ALL_WITHDRAWALS)) {
            userPermissions.setShowViewAllWithdrawals(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.REVIEW_WITHDRAWALS)) {
            userPermissions.setShowReviewWithdrawals(false);
        }
        
        // Signing Authority : Loans project
        if (!loggedInRole.hasPermission(PermissionType.SIGNING_AUTHORITY)) {
            userPermissions.setShowSigningAuthority(false);
        }

        // Employee Management
        if (!loggedInRole.hasPermission(PermissionType.UPDATE_CENSUS_DATA)) {
            userPermissions.setShowUpdateCensusData(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.VIEW_SALARY)) {
            userPermissions.setShowViewSalary(false);
        }

        // Plan services
        if (!loggedInRole.hasPermission(PermissionType.EDIT_PLAN_DATA)) {
            userPermissions.setShowEditPlanData(false);
        }

        // Loans Permissions
        if (!loggedInRole.hasPermission(PermissionType.INITIATE_LOANS)) {
            userPermissions.setShowInitiateLoans(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.VIEW_ALL_LOANS)) {
            userPermissions.setShowViewAllLoans(false);
        }
        if (!loggedInRole.hasPermission(PermissionType.REVIEW_LOANS)) {
            userPermissions.setShowReviewLoans(false);
        }
        if(!loggedInRole.hasPermission(PermissionType.ACCESS_NOTICE_MANAGER)){
        	userPermissions.setShowNoticeManager(false);
        }

    }
   

    private static void filterOnContractServiceFeatures(UserPermissions userPermissions, Contract contract) {
        ContractServiceFeature withdrawalsCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        if (withdrawalsCSF != null) {
            if (ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue())) {
                if (!ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW))) {
                    userPermissions.setShowReviewWithdrawals(false);
                }
            } else {
                userPermissions.setShowInitiateAndViewMyWithdrawals(false);
                userPermissions.setShowViewAllWithdrawals(false);
                userPermissions.setShowReviewWithdrawals(false);
            }
        }
        // Don't display loans permissions if the LRK01 flag is not turned on and allow online loans is not true
        ContractServiceFeature allowOnlineLoansCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
        if (!(contract.isLoansRecordKeepingIndicator() && (ContractServiceFeature.internalToBoolean(allowOnlineLoansCSF.getValue())))) {
            userPermissions.setShowInitiateLoans(false);
            userPermissions.setShowViewAllLoans(false);
            userPermissions.setShowReviewLoans(false);
        }
    }

    private static void populateDirectDebitAccounts(UserPermissions userPermissions, UserRole loggedInRole, Contract contract) throws SystemException {

        userPermissions.getDirectDebitAccounts().clear();

        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        List<DirectDebitAccount> directDebitAccounts = service.getDirectDebitAccounts(contract.getContractNumber());
        List<DirectDebitAccount> allowedDirectDebitAccounts = null;

        // If login user is NOT an internal user, we limit the list of direct debit accounts to
        // the ones the current user have access to.
        if (!loggedInRole.isInternalUser()) {
            allowedDirectDebitAccounts = loggedInRole.getDirectDebitAccounts();
        }

        if (directDebitAccounts != null) {
            for (DirectDebitAccount account : directDebitAccounts) {
                BankAccount bankAccount = BankAccount.getBankAccount(account);
                bankAccount.setLabel(FormatUtils.formatAccountName(account, MAX_ACCOUNT_LABEL_WIDTH));
                if (!loggedInRole.isInternalUser()) {
                    if (!allowedDirectDebitAccounts.contains(account)) {
                        bankAccount.setNoAccess(true);
                    }
                }
                userPermissions.getDirectDebitAccounts().add(bankAccount);
            }
        }
    }

    private static void setSelectedDirectDebitAccounts(UserPermissions userPermissions, UserRole selectedRole) {

        String[] selectedAccounts = new String[userPermissions.getDirectDebitAccounts().size()];

        int index = 0;

        // Goes through each of the direct debit accounts the user have access to, selects only
        // those that are available.
        List<DirectDebitAccount> directDebitAccounts = selectedRole.getDirectDebitAccounts();
        for (DirectDebitAccount account : directDebitAccounts) {
            BankAccount bankAccount = BankAccount.getBankAccount(account);
            if (userPermissions.getDirectDebitAccounts().contains(bankAccount)) {
                selectedAccounts[index] = bankAccount.getPrimaryKey();
                index++;
            }
        }
        userPermissions.setSelectedDirectDebitAccounts(selectedAccounts);
    }

    /**
     * Check if all permissions is No value
     * 
     * @param permissions
     * @return true - all permissions value is no.
     */
    public static boolean isAllPermissionIsNo(UserPermissions permissions) {
        return (!permissions.isManageUsers() && !permissions.isSelectedAccess() && !permissions.isEditContractServiceFeatures() && !permissions.isDownloadReports()
                && !permissions.isEmployerStatements() && !permissions.isViewSubmissions() && !permissions.isCreateUploadSubmissions() && !permissions.isViewAllUsersSubmissions()
                && !permissions.isCashAccount() && !permissions.isDirectDebit() && !permissions.isInitiateAndViewMyWithdrawals() && !permissions.isViewAllWithdrawals()
                && !permissions.isReviewWithdrawals() && !permissions.isSigningAuthority() && !permissions.isUpdateCensusData() && !permissions.isViewSalary() 
                && !permissions.isInitiateLoans() && !permissions.isViewAllLoans() && !permissions.isReviewLoans() && !permissions.isNoticeManager());

    }

    /**
     * Get the DefaultRolePermissions for the UserRole
     * 
     * @param userRole
     * @return DefaultRolePermissions
     * @throws SystemException
     */
    public static DefaultRolePermissions getDefaultRolePermissions(UserRole userRole) throws SystemException {
        return SecurityServiceDelegate.getInstance().getDefaultRolePermissions(userRole.toString());
    }

    public static void populateRolePermissions(UserRole userRole, UserPermissions userPermissions) {
        // User management
        if (userPermissions.isManageUsers()) {
            userRole.addPermission(PermissionType.MANAGE_EXTERNAL_USERS);
        }
        if (userPermissions.isSelectedAccess()) {
            userRole.addPermission(PermissionType.SELECTED_ACCESS);
        }
        if (userPermissions.isEditContractServiceFeatures()) {
            userRole.addPermission(PermissionType.EDIT_SERVICE_FEATURES);
        }

        // Reporting
        if (userPermissions.isDownloadReports()) {
            userRole.addPermission(PermissionType.REPORT_DOWNLOAD);
        }
        if (userPermissions.isEmployerStatements()) {
            userRole.addPermission(PermissionType.EMPLOYER_STATEMENT_ACCESS);
        }

        if (userPermissions.isEditPlanData()) {
            userRole.addPermission(PermissionType.EDIT_PLAN_DATA);
        }

        // Client services
        // Submissions
        if (userPermissions.isViewSubmissions()) {
            userRole.addPermission(PermissionType.SUBMISSION_ACCESS);
        }
        if (userPermissions.isCreateUploadSubmissions()) {
            userRole.addPermission(PermissionType.UPLOAD_SUBMISSIONS);
        }
        if (userPermissions.isViewAllUsersSubmissions()) {
            userRole.addPermission(PermissionType.VIEW_ALL_SUBMISSIONS);
        }
        if (userPermissions.isCashAccount()) {
            userRole.addPermission(PermissionType.CASH_ACCOUNT_ACCESS);
        }
        if (userPermissions.isDirectDebit()) {
            userRole.addPermission(PermissionType.DIRECT_DEBIT_ACCOUNT);
        }

        List<DirectDebitAccount> selectedDDAccounts = new ArrayList<DirectDebitAccount>();
        List<DirectDebitAccount> deselectedDDAccounts = new ArrayList<DirectDebitAccount>();
        for (BankAccount bankAccount : userPermissions.getDirectDebitAccounts()) {
            if (userPermissions.getSelectedDirectDebitAccountsAsList().contains(bankAccount)) {
                selectedDDAccounts.add(bankAccount.getDirectDebitAccount());
            } else {
                deselectedDDAccounts.add(bankAccount.getDirectDebitAccount());
            }
        }
        userRole.setDirectDebitAccounts(selectedDDAccounts);
        userRole.setRemovedDirectDebitAccounts(deselectedDDAccounts);

        // i:withdrawals
        if (userPermissions.isInitiateAndViewMyWithdrawals()) {
            userRole.addPermission(PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
        }
        if (userPermissions.isViewAllWithdrawals()) {
            userRole.addPermission(PermissionType.VIEW_ALL_WITHDRAWALS);
        }
        if (userPermissions.isReviewWithdrawals()) {
            userRole.addPermission(PermissionType.REVIEW_WITHDRAWALS);
        }
        
        // Signing Authority : Loans project
        if (userPermissions.isSigningAuthority()) {
            userRole.addPermission(PermissionType.SIGNING_AUTHORITY);
        }

        // Employee Management
        if (userPermissions.isUpdateCensusData()) {
            userRole.addPermission(PermissionType.UPDATE_CENSUS_DATA);
        }
        if (userPermissions.isViewSalary()) {
            userRole.addPermission(PermissionType.VIEW_SALARY);
        }
        
        // Loans permissions
        if (userPermissions.isInitiateLoans()) {
            userRole.addPermission(PermissionType.INITIATE_LOANS);
        }
        if (userPermissions.isViewAllLoans()) {
            userRole.addPermission(PermissionType.VIEW_ALL_LOANS);
        }
        if (userPermissions.isReviewLoans()) {
            userRole.addPermission(PermissionType.REVIEW_LOANS);
        }
        if (userPermissions.isNoticeManager()) {
            userRole.addPermission(PermissionType.ACCESS_NOTICE_MANAGER);
        } 
    }
    
   /**
    * This method checks to see if the iwithdrawals CSF is turned on and if the review is 
    * required and if the who will review is set to PS, the review withdrawal permission
    * for the user role (if eligible) is set to true.
    * @param contract
    * @param userPermissions
    */
    private static void populateReviewWithdrawalsPermissionAsPerCSF (Contract contract, UserPermissions userPermissions, 
    					UserRole userRole, boolean newUser) {
    	if(contract != null && contract.isBusinessConverted() && 
    			newUser && isClientValidForReviewLoansWithdrawalsPermission(userRole)) {
    	   	ContractServiceFeature withdrawalsCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
        	if (ContractServiceFeature.internalToBoolean(withdrawalsCSF.getValue())) {
                if (ContractServiceFeature.internalToBoolean(withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW))) {
                	if(StringUtils.equals(ServiceFeatureConstants.WHO_WILL_REVIEW_PS, 
                			withdrawalsCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_WITHDRAWALS))) {
                		userPermissions.setReviewWithdrawals(true);
                	}
                }
        	}
    	}
     }
    /**
     * This method checks if the Online feature is turned on and if who will review the 
     * online loan is set to PS, the User (if eligible) is granted the review loans permission.
     * @param contract
     * @param userPermissions
     */
    private static void populateReviewLoansPermissionAsPerCSF (Contract contract, UserPermissions userPermissions,
    				UserRole userRole, boolean newUser) {
    	if(contract != null && contract.isBusinessConverted() && 
    			newUser && isClientValidForReviewLoansWithdrawalsPermission(userRole)) {
        	ContractServiceFeature loansCSF = contract.getServiceFeatureMap().get(ServiceFeatureConstants.ALLOW_LOANS_FEATURE);
        	if (ContractServiceFeature.internalToBoolean(loansCSF.getValue())) {
                if(StringUtils.equals(ServiceFeatureConstants.WHO_WILL_REVIEW_PS, 
                			loansCSF.getAttributeValue(ServiceFeatureConstants.WHO_WILL_REVIEW_LOANS))) {
                		userPermissions.setReviewLoans(true);
                	
                }
        	}
    	}
    }
    /**
     * This method checks to see if the User role is Trustee / Suthorized Signor
     * or Administrative Contact and returns true.
     * @param userRole
     * @return boolean
     */
    private static boolean isClientValidForReviewLoansWithdrawalsPermission (UserRole userRole) {
    	if (userRole instanceof Trustee || userRole instanceof AuthorizedSignor 
    			|| userRole instanceof AdministrativeContact) {
			return true;
		}
    	return false;
    }
    
    /**
     * This method checks to see if the User role is a CCS Rep role and returns true.
     * @param userRole
     * @return boolean
     */
    private static boolean isNoticeManagerCCSRepCheck(UserRole userRole) {
    	if  (userRole instanceof InternalServicesCAR || 
    			userRole instanceof CAR ||
    			userRole instanceof SuperCAR ||
    			userRole instanceof TeamLead ||
    			userRole instanceof BundledGaCAR) {
			return true;
		}
    	return false;
    }
 
}
