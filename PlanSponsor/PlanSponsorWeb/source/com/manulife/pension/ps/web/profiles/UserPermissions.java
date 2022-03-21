package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * 
 * Used to hold both ClientUserPermissions and TPAFirmUserPermissions, for use by the
 * sub settings pages of the client/tpa firm permissions setting pages.
 *
 */
public class UserPermissions implements Cloneable, Serializable {
    private static final long serialVersionUID = -1L;

    public static final String NO_DIRECT_DEBIT_ACCOUNT = "none";

    private boolean isTPAData = false; // indicates to jsp tpa version of the screen 
    
    // User management
    private boolean manageUsers;

    private String manageUsersDefault;

    private boolean showManageUsers = true;

    public final static String FIELD_MANAGE_USERS = "manageUsers";

    private boolean selectedAccess;

    private String selectedAccessDefault;

    private boolean showSelectedAccess = true;

    public final static String FIELD_SELECTED_ACCESS = "selectedAccess";

    private boolean editContractServiceFeatures;

    private String editContractServiceFeaturesDefault;

    private boolean showEditContractServiceFeatures = true;

    public final static String FIELD_EDIT_CONTRACT_SERVICE_FEATURES = "editContractServiceFeatures";
        
    private boolean tpaStaffPlanAccess;

    private String tpaStaffPlanAccessDefault;

    private boolean showTpaStaffPlanAccess = false; // defaults are for client page.

    public final static String FIELD_TPA_STAFF_PLAN_ACCESS = "tpaStaffPlanAccess";

    // Reporting
    private boolean downloadReports;

    private String downloadReportsDefault;

    private boolean showDownloadReports = true;

    public final static String FIELD_DOWNLOAD_REPORTS = "downloadReports";

    private boolean employerStatements;

    private String employerStatementsDefault;

    private boolean showEmployerStatements = true;

    public final static String FIELD_EMPLOYER_STATEMENTS = "employerStatements";

    // Plan services
    private boolean editPlanData;

    private String editPlanDataDefault;

    private boolean showEditPlanData = true;

    public final static String FIELD_EDIT_PLAN_DATA = "editPlanData";

    private boolean submitUpdateVesting;

    private String submitUpdateVestingDefault;

    private boolean showSubmitUpdateVesting = true;

    public final static String FIELD_SUBMIT_UPDATE_VESTING = "submitUpdateVesting";


    // Client services
    // Submissions
    private boolean viewSubmissions;

    private String viewSubmissionsDefault;

    private boolean showViewSubmissions = true;

    public final static String FIELD_VIEW_SUBMISSIONS = "viewSubmissions";

    private boolean createUploadSubmissions;

    private String createUploadSubmissionsDefault;

    private boolean showCreateUploadSubmissions = true;

    public final static String FIELD_CREATE_UPLOAD_SUBMISSIONS = "createUploadSubmissions";

    private boolean viewAllUsersSubmissions;

    private String viewAllUsersSubmissionsDefault;

    private boolean showViewAllUsersSubmissions = true;

    public final static String FIELD_VIEW_ALL_USERS_SUBMISSIONS = "viewAllUsersSubmissions";

    private boolean cashAccount;

    private String cashAccountDefault;

    private boolean showCashAccount = true;

    public final static String FIELD_CASH_ACCOUNT = "cashAccount";

    private boolean directDebit;

    private String directDebitDefault;

    private boolean showDirectDebit = true;

    public final static String FIELD_DIRECT_DEBIT = "directDebit";

    private List<BankAccount> directDebitAccounts = new ArrayList<BankAccount>();

    public final static String FIELD_DIRECT_DEBIT_ACCOUNTS = "directDebitAccounts";

    private String[] selectedDirectDebitAccounts = new String[0];

    public final static String FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS = "selectedDirectDebitAccounts";

    // online loans permissions
    private boolean showLoansPermissions = false;
    
    private String initiateLoansDefault = null;
    
    private String viewAllLoansDefault = null;
    
    private String reviewLoansDefault = null;
    
    private boolean initiateLoans = false;
    
    private boolean viewAllLoans = false;
    
    private boolean reviewLoans = false;
    
    private boolean showInitiateLoans = true;
    
    private boolean showViewAllLoans = true;
    
    private boolean showReviewLoans = true;
    
    public final static String FIELD_VIEW_ALL_LOANS = "viewAllLoans";
    
    public final static String FIELD_INITIATE_LOANS = "initiateLoans";
    
    public final static String FIELD_REVIEW_LOANS = "reviewLoans";
    
    // i:withdrawals
    private boolean initiateAndViewMyWithdrawals;

    private String initiateAndViewMyWithdrawalsDefault;

    private boolean showInitiateAndViewMyWithdrawals = true;

    public final static String FIELD_INITIATE_AND_VIEW_WITHDRAWALS = "initiateAndViewMyWithdrawals";

    private boolean viewAllWithdrawals;

    private String viewAllWithdrawalsDefault;

    private boolean showViewAllWithdrawals = true;

    public final static String FIELD_VIEW_ALL_WITHDRAWALS = "viewAllWithdrawals";

    private boolean reviewWithdrawals;

    private String reviewWithdrawalsDefault;

    private boolean showReviewWithdrawals = true;

    public final static String FIELD_REVIEW_WITHDRAWALS = "reviewWithdrawals";

    // This one has to be renamed to signingAuthority since approve withdrawals is going to become as signing authority 
    // after online loans implementation.
    private boolean signingAuthority;

    private String signingAuthorityDefault;
    
    public final static String FIELD_SIGNING_AUTHORITY = "signingAuthority";

    // Employee Management
    private boolean updateCensusData;

    private String updateCensusDataDefault;

    private boolean showUpdateCensusData = true;

    public final static String FIELD_UPDATE_CENSUS_DATA = "updateCensusData";
    
   /*Added new attribute for 404a5 permission*/
    private boolean feeAccess404A5;

    private String feeAccess404A5Default;

    private boolean showFeeAccess404A5 = true;

    public final static String FIELD_FEE_ACCESS_404A5 = "feeAccess404A5";

    private boolean viewSalary;

    private String viewSalaryDefault;

    private boolean showViewSalary = true;

    public final static String FIELD_VIEW_SALARY = "viewSalary";
    
    //Notice Manager
    
    private boolean noticeManager;
    
    private String noticeManagerDefault;
    
    private boolean showNoticeManager=true;
    
    public final static String FIELD_NOTICE_MANAGER="noticeManager";

    /**
     * showSigningAuthority variable is used to determine whether
     * the signing authority permission has to be shown or not based 
     * on the logged in user and contract that is in use.
     */
    private boolean showSigningAuthority = false; 

    public Object clone() {
        
        try {
            UserPermissions clonedPermissions = (UserPermissions) super.clone();
            // Add all direct debit accounts into the clone object.
            clonedPermissions.directDebitAccounts = new ArrayList<BankAccount>();
            for (BankAccount account : directDebitAccounts) {
                clonedPermissions.getDirectDebitAccounts().add((BankAccount) account.clone());
            }
            return clonedPermissions;
        } catch (CloneNotSupportedException cnse) {
            throw new NestableRuntimeException(cnse);
        }
       
    }

    public List<BankAccount> getDirectDebitAccounts() {
        return directDebitAccounts;
    }

    public String[] getSelectedDirectDebitAccounts() {
        return selectedDirectDebitAccounts;
    }

    public void setSelectedDirectDebitAccounts(String[] selectedDirectDebitAccounts) {
        this.selectedDirectDebitAccounts = selectedDirectDebitAccounts;
    }

    public List<BankAccount> getSelectedDirectDebitAccountsAsList() {
        List<BankAccount> accounts = new ArrayList<BankAccount>();
        if (selectedDirectDebitAccounts != null) {
            for (BankAccount bankAccount : directDebitAccounts) {
                for (int i = 0; i < selectedDirectDebitAccounts.length; i++) {
                    // Skip the NO_DIRECT_DEBIT_ACCOUNT flag.
                    if (selectedDirectDebitAccounts.equals(NO_DIRECT_DEBIT_ACCOUNT)) {
                        continue;
                    }
                    if (bankAccount.getPrimaryKey().equals(selectedDirectDebitAccounts[i])) {
                        accounts.add(bankAccount);
                        break;
                    }
                }
            }
        }
        return accounts;
    }

    public String getSelectedDirectDebitAccountIdsAsString() {
        List<BankAccount> accounts = getSelectedDirectDebitAccountsAsList();
        StringBuffer sb = new StringBuffer();
        for (BankAccount bankAccount : accounts) {
            sb.append(bankAccount.getPrimaryKey()).append(",");
        }
        return sb.toString();
    }

    /**
     * @return the signingAuthority
     */
    public boolean isSigningAuthority() {
        return signingAuthority;
    }

    /**
     * @param signingAuthority the signingAuthority to set
     */
    public void setSigningAuthority(boolean signingAuthority) {
        this.signingAuthority = signingAuthority;
    }

    /**
     * @return the signingAuthorityDefault
     */
    public String getSigningAuthorityDefault() {
        return signingAuthorityDefault;
    }

    /**
     * @param signingAuthorityDefault the signingAuthorityDefault to set
     */
    public void setSigningAuthorityDefault(String signingAuthorityDefault) {
        this.signingAuthorityDefault = signingAuthorityDefault;
    }

    /**
     * @return the cashAccount
     */
    public boolean isCashAccount() {
        return cashAccount;
    }

    /**
     * @param cashAccount the cashAccount to set
     */
    public void setCashAccount(boolean cashAccount) {
        this.cashAccount = cashAccount;
    }

    /**
     * @return the cashAccountDefault
     */
    public String getCashAccountDefault() {
        return cashAccountDefault;
    }

    /**
     * @param cashAccountDefault the cashAccountDefault to set
     */
    public void setCashAccountDefault(String cashAccountDefault) {
        this.cashAccountDefault = cashAccountDefault;
    }

    /**
     * @return the createUploadSubmissions
     */
    public boolean isCreateUploadSubmissions() {
        return createUploadSubmissions;
    }

    /**
     * @param createUploadSubmissions the createUploadSubmissions to set
     */
    public void setCreateUploadSubmissions(boolean createUploadSubmissions) {
        this.createUploadSubmissions = createUploadSubmissions;
    }

    /**
     * @return the createUploadSubmissionsDefault
     */
    public String getCreateUploadSubmissionsDefault() {
        return createUploadSubmissionsDefault;
    }

    /**
     * @param createUploadSubmissionsDefault the createUploadSubmissionsDefault to set
     */
    public void setCreateUploadSubmissionsDefault(String createUploadSubmissionsDefault) {
        this.createUploadSubmissionsDefault = createUploadSubmissionsDefault;
    }

    /**
     * @return the directDebit
     */
    public boolean isDirectDebit() {
        return directDebit;
    }

    /**
     * @param directDebit the directDebit to set
     */
    public void setDirectDebit(boolean directDebit) {
        this.directDebit = directDebit;
    }

    /**
     * @return the directDebitDefault
     */
    public String getDirectDebitDefault() {
        return directDebitDefault;
    }

    /**
     * @param directDebitDefault the directDebitDefault to set
     */
    public void setDirectDebitDefault(String directDebitDefault) {
        this.directDebitDefault = directDebitDefault;
    }

    /**
     * @return the downloadReports
     */
    public boolean isDownloadReports() {
        return downloadReports;
    }

    /**
     * @param downloadReports the downloadReports to set
     */
    public void setDownloadReports(boolean downloadReports) {
        this.downloadReports = downloadReports;
    }

    /**
     * @return the downloadReportsDefault
     */
    public String getDownloadReportsDefault() {
        return downloadReportsDefault;
    }

    /**
     * @param downloadReportsDefault the downloadReportsDefault to set
     */
    public void setDownloadReportsDefault(String downloadReportsDefault) {
        this.downloadReportsDefault = downloadReportsDefault;
    }

    /**
     * @return the editContractServiceFeatures
     */
    public boolean isEditContractServiceFeatures() {
        return editContractServiceFeatures;
    }

    /**
     * @param editContractServiceFeatures the editContractServiceFeatures to set
     */
    public void setEditContractServiceFeatures(boolean editContractServiceFeatures) {
        this.editContractServiceFeatures = editContractServiceFeatures;
    }

    /**
     * @return the editContractServiceFeaturesDefault
     */
    public String getEditContractServiceFeaturesDefault() {
        return editContractServiceFeaturesDefault;
    }

    /**
     * @param editContractServiceFeaturesDefault the editContractServiceFeaturesDefault to set
     */
    public void setEditContractServiceFeaturesDefault(String editContractServiceFeaturesDefault) {
        this.editContractServiceFeaturesDefault = editContractServiceFeaturesDefault;
    }

    /**
     * @return the employerStatements
     */
    public boolean isEmployerStatements() {
        return employerStatements;
    }

    /**
     * @param employerStatements the employerStatements to set
     */
    public void setEmployerStatements(boolean employerStatements) {
        this.employerStatements = employerStatements;
    }

    /**
     * @return the employerStatementsDefault
     */
    public String getEmployerStatementsDefault() {
        return employerStatementsDefault;
    }

    /**
     * @param employerStatementsDefault the employerStatementsDefault to set
     */
    public void setEmployerStatementsDefault(String employerStatementsDefault) {
        this.employerStatementsDefault = employerStatementsDefault;
    }

    /**
     * @return the initiateAndViewMyWithdrawals
     */
    public boolean isInitiateAndViewMyWithdrawals() {
        return initiateAndViewMyWithdrawals;
    }

    /**
     * @param initiateAndViewMyWithdrawals the initiateAndViewMyWithdrawals to set
     */
    public void setInitiateAndViewMyWithdrawals(boolean initiateAndViewMyWithdrawals) {
        this.initiateAndViewMyWithdrawals = initiateAndViewMyWithdrawals;
    }

    /**
     * @return the initiateAndViewMyWithdrawalsDefault
     */
    public String getInitiateAndViewMyWithdrawalsDefault() {
        return initiateAndViewMyWithdrawalsDefault;
    }

    /**
     * @param initiateAndViewMyWithdrawalsDefault the initiateAndViewMyWithdrawalsDefault to set
     */
    public void setInitiateAndViewMyWithdrawalsDefault(String initiateAndViewMyWithdrawalsDefault) {
        this.initiateAndViewMyWithdrawalsDefault = initiateAndViewMyWithdrawalsDefault;
    }

    /**
     * @return the manageUsers
     */
    public boolean isManageUsers() {
        return manageUsers;
    }

    /**
     * @param manageUsers the manageUsers to set
     */
    public void setManageUsers(boolean manageUsers) {
        this.manageUsers = manageUsers;
    }

    /**
     * @return the manageUsersDefault
     */
    public String getManageUsersDefault() {
        return manageUsersDefault;
    }

    /**
     * @param manageUsersDefault the manageUsersDefault to set
     */
    public void setManageUsersDefault(String manageUsersDefault) {
        this.manageUsersDefault = manageUsersDefault;
    }

    /**
     * @return the reviewWithdrawals
     */
    public boolean isReviewWithdrawals() {
        return reviewWithdrawals;
    }

    /**
     * @param reviewWithdrawals the reviewWithdrawals to set
     */
    public void setReviewWithdrawals(boolean reviewWithdrawals) {
        this.reviewWithdrawals = reviewWithdrawals;
    }

    /**
     * @return the reviewWithdrawalsDefault
     */
    public String getReviewWithdrawalsDefault() {
        return reviewWithdrawalsDefault;
    }

    /**
     * @param reviewWithdrawalsDefault the reviewWithdrawalsDefault to set
     */
    public void setReviewWithdrawalsDefault(String reviewWithdrawalsDefault) {
        this.reviewWithdrawalsDefault = reviewWithdrawalsDefault;
    }

    /**
     * @return the selectedAccess
     */
    public boolean isSelectedAccess() {
        return selectedAccess;
    }

    /**
     * @param selectedAccess the selectedAccess to set
     */
    public void setSelectedAccess(boolean selectedAccess) {
        this.selectedAccess = selectedAccess;
    }

    /**
     * @return the selectedAccessDefault
     */
    public String getSelectedAccessDefault() {
        return selectedAccessDefault;
    }

    /**
     * @param selectedAccessDefault the selectedAccessDefault to set
     */
    public void setSelectedAccessDefault(String selectedAccessDefault) {
        this.selectedAccessDefault = selectedAccessDefault;
    }

    /**
     * @return the updateCensusData
     */
    public boolean isUpdateCensusData() {
        return updateCensusData;
    }

    /**
     * @param updateCensusData the updateCensusData to set
     */
    public void setUpdateCensusData(boolean updateCensusData) {
        this.updateCensusData = updateCensusData;
    }

    /**
     * @return the updateCensusDataDefault
     */
    public String getUpdateCensusDataDefault() {
        return updateCensusDataDefault;
    }

    /**
     * @param updateCensusDataDefault the updateCensusDataDefault to set
     */
    public void setUpdateCensusDataDefault(String updateCensusDataDefault) {
        this.updateCensusDataDefault = updateCensusDataDefault;
    }
    
    /**
     * @return the FeeAccess404A5
     */
    public boolean isFeeAccess404A5() {
        return feeAccess404A5;
    }

    /**
     * @param FeeAccess404A5 the FeeAccess404A5 to set
     */
    public void setFeeAccess404A5(boolean feeAccess404A5) {
        this.feeAccess404A5 = feeAccess404A5;
    }

    /**
	 * @return the feeAccess404A5Default
	 */
	public String getFeeAccess404A5Default() {
		return feeAccess404A5Default;
	}

	/**
	 * @param feeAccess404A5Default the feeAccess404A5Default to set
	 */
	public void setFeeAccess404A5Default(String feeAccess404A5Default) {
		this.feeAccess404A5Default = feeAccess404A5Default;
	}

	/**
     * @return the viewAllUsersSubmissions
     */
    public boolean isViewAllUsersSubmissions() {
        return viewAllUsersSubmissions;
    }

    /**
     * @param viewAllUsersSubmissions the viewAllUsersSubmissions to set
     */
    public void setViewAllUsersSubmissions(boolean viewAllUsersSubmissions) {
        this.viewAllUsersSubmissions = viewAllUsersSubmissions;
    }

    /**
     * @return the viewAllUsersSubmissionsDefault
     */
    public String getViewAllUsersSubmissionsDefault() {
        return viewAllUsersSubmissionsDefault;
    }

    /**
     * @param viewAllUsersSubmissionsDefault the viewAllUsersSubmissionsDefault to set
     */
    public void setViewAllUsersSubmissionsDefault(String viewAllUsersSubmissionsDefault) {
        this.viewAllUsersSubmissionsDefault = viewAllUsersSubmissionsDefault;
    }

    /**
     * @return the viewAllWithdrawals
     */
    public boolean isViewAllWithdrawals() {
        return viewAllWithdrawals;
    }

    /**
     * @param viewAllWithdrawals the viewAllWithdrawals to set
     */
    public void setViewAllWithdrawals(boolean viewAllWithdrawals) {
        this.viewAllWithdrawals = viewAllWithdrawals;
    }

    /**
     * @return the viewAllWithdrawalsDefault
     */
    public String getViewAllWithdrawalsDefault() {
        return viewAllWithdrawalsDefault;
    }

    /**
     * @param viewAllWithdrawalsDefault the viewAllWithdrawalsDefault to set
     */
    public void setViewAllWithdrawalsDefault(String viewAllWithdrawalsDefault) {
        this.viewAllWithdrawalsDefault = viewAllWithdrawalsDefault;
    }

    /**
     * @return the viewSalary
     */
    public boolean isViewSalary() {
        return viewSalary;
    }

    /**
     * @param viewSalary the viewSalary to set
     */
    public void setViewSalary(boolean viewSalary) {
        this.viewSalary = viewSalary;
    }

    /**
     * @return the viewSalaryDefault
     */
    public String getViewSalaryDefault() {
        return viewSalaryDefault;
    }

    /**
     * @param viewSalaryDefault the viewSalaryDefault to set
     */
    public void setViewSalaryDefault(String viewSalaryDefault) {
        this.viewSalaryDefault = viewSalaryDefault;
    }

    /**
     * @return the viewSubmissions
     */
    public boolean isViewSubmissions() {
        return viewSubmissions;
    }

    /**
     * @param viewSubmissions the viewSubmissions to set
     */
    public void setViewSubmissions(boolean viewSubmissions) {
        this.viewSubmissions = viewSubmissions;
    }

    /**
     * @return the viewSubmissionsDefault
     */
    public String getViewSubmissionsDefault() {
        return viewSubmissionsDefault;
    }

    /**
     * @param viewSubmissionsDefault the viewSubmissionsDefault to set
     */
    public void setViewSubmissionsDefault(String viewSubmissionsDefault) {
        this.viewSubmissionsDefault = viewSubmissionsDefault;
    }

    /**
     * @param directDebitAccounts the directDebitAccounts to set
     */
    public void setDirectDebitAccounts(List<BankAccount> directDebitAccounts) {
        this.directDebitAccounts = directDebitAccounts;
    }

    public boolean isShowSigningAuthority() {
        return showSigningAuthority;
    }

    public void setShowSigningAuthority(boolean showSigningAuthority) {
        this.showSigningAuthority = showSigningAuthority;
    }

    public boolean isShowCashAccount() {
        return showCashAccount;
    }

    public void setShowCashAccount(boolean showCashAccount) {
        this.showCashAccount = showCashAccount;
    }

    public boolean isShowCreateUploadSubmissions() {
        return showCreateUploadSubmissions;
    }

    public void setShowCreateUploadSubmissions(boolean showCreateUploadSubmissions) {
        this.showCreateUploadSubmissions = showCreateUploadSubmissions;
    }

    public boolean isShowDirectDebit() {
        return showDirectDebit;
    }

    public void setShowDirectDebit(boolean showDirectDebit) {
        this.showDirectDebit = showDirectDebit;
    }

    public boolean isShowDownloadReports() {
        return showDownloadReports;
    }

    public void setShowDownloadReports(boolean showDownloadReports) {
        this.showDownloadReports = showDownloadReports;
    }

    public boolean isShowEditContractServiceFeatures() {
        return showEditContractServiceFeatures;
    }

    public void setShowEditContractServiceFeatures(boolean showEditContractServiceFeatures) {
        this.showEditContractServiceFeatures = showEditContractServiceFeatures;
    }

    public boolean isShowEmployerStatements() {
        return showEmployerStatements;
    }

    public void setShowEmployerStatements(boolean showEmployerStatements) {
        this.showEmployerStatements = showEmployerStatements;
    }

    public boolean isShowInitiateAndViewMyWithdrawals() {
        return showInitiateAndViewMyWithdrawals;
    }

    public void setShowInitiateAndViewMyWithdrawals(boolean showInitiateAndViewMyWithdrawals) {
        this.showInitiateAndViewMyWithdrawals = showInitiateAndViewMyWithdrawals;
    }

    public boolean isShowManageUsers() {
        return showManageUsers;
    }

    public void setShowManageUsers(boolean showManageUsers) {
        this.showManageUsers = showManageUsers;
    }

    public boolean isShowReviewWithdrawals() {
        return showReviewWithdrawals;
    }

    public void setShowReviewWithdrawals(boolean showReviewWithdrawals) {
        this.showReviewWithdrawals = showReviewWithdrawals;
    }

    public boolean isShowSelectedAccess() {
        return showSelectedAccess;
    }

    public void setShowSelectedAccess(boolean showSelectedAccess) {
        this.showSelectedAccess = showSelectedAccess;
    }

    public boolean isShowUpdateCensusData() {
        return showUpdateCensusData;
    }

    public void setShowUpdateCensusData(boolean showUpdateCensusData) {
        this.showUpdateCensusData = showUpdateCensusData;
    }

    public boolean isShowViewAllUsersSubmissions() {
        return showViewAllUsersSubmissions;
    }

    public void setShowViewAllUsersSubmissions(boolean showViewAllUsersSubmissions) {
        this.showViewAllUsersSubmissions = showViewAllUsersSubmissions;
    }

    public boolean isShowViewAllWithdrawals() {
        return showViewAllWithdrawals;
    }

    public void setShowViewAllWithdrawals(boolean showViewAllWithdrawals) {
        this.showViewAllWithdrawals = showViewAllWithdrawals;
    }

    public boolean isShowViewSalary() {
        return showViewSalary;
    }

    public boolean isNoticeManager() {
		return noticeManager;
	}

	public void setNoticeManager(boolean noticeManager) {
		this.noticeManager = noticeManager;
	}

	public String getNoticeManagerDefault() {
		return noticeManagerDefault;
	}

	public void setNoticeManagerDefault(String noticeManagerDefault) {
		this.noticeManagerDefault = noticeManagerDefault;
	}

	public boolean isShowNoticeManager() {
		return showNoticeManager;
	}

	public void setShowNoticeManager(boolean showNoticeManager) {
		this.showNoticeManager = showNoticeManager;
	}

	public void setShowViewSalary(boolean showViewSalary) {
        this.showViewSalary = showViewSalary;
    }

    public boolean isShowViewSubmissions() {
        return showViewSubmissions;
    }

    public void setShowViewSubmissions(boolean showViewSubmissions) {
        this.showViewSubmissions = showViewSubmissions;
    }

    public boolean isShowUserManagementSection() {
        return showManageUsers || showSelectedAccess || showEditContractServiceFeatures;
    }

    public boolean isShowReportingSection() {
        return showDownloadReports || showEmployerStatements;
    }

    public boolean isShowPlanServicesSection() {
        return showEditPlanData;
    }

    public boolean isShowClientServicesSection() {
        return isShowSubmissionsSection() || isShowWithdrawalsSection()
                || isShowEmployeeManagementSection()|| isShowNoticeManager();
    }

    public boolean isShowSubmissionsSection() {
        return showViewSubmissions || showCreateUploadSubmissions || showViewAllUsersSubmissions
                || showCashAccount || showDirectDebit;
    }

    // Loans : updating showApproveWithdrawals to showSigningAuthority
    public boolean isShowWithdrawalsSection() {
        return showInitiateAndViewMyWithdrawals || showViewAllWithdrawals || showReviewWithdrawals;
    }

    public boolean isShowEmployeeManagementSection() {
        return showUpdateCensusData || showViewSalary;
    }
    
    public boolean isTPAData() {
		return isTPAData;
	}

	public void setTPAData(boolean isTPAData) {
		this.isTPAData = isTPAData;
		
		if (isTPAData) { // adjust to show stuff on tpa screen
			this.showSelectedAccess = false;
			this.showEditContractServiceFeatures = false;
			this.showTpaStaffPlanAccess = true;
			this.showEmployerStatements = false;
			this.showEditPlanData = true;
		} 
	}

	public boolean isShowTpaStaffPlanAccess() {
		return showTpaStaffPlanAccess;
	}

	public void setShowTpaStaffPlanAccess(boolean showTpaStaffPlanAccess) {
		this.showTpaStaffPlanAccess = showTpaStaffPlanAccess;
	}

	public boolean isTpaStaffPlanAccess() {
		return tpaStaffPlanAccess;
	}

	public void setTpaStaffPlanAccess(boolean tpaStaffPlanAccess) {
		this.tpaStaffPlanAccess = tpaStaffPlanAccess;
	}

	public String getTpaStaffPlanAccessDefault() {
		return tpaStaffPlanAccessDefault;
	}

	public void setTpaStaffPlanAccessDefault(String tpaStaffPlanAccessDefault) {
		this.tpaStaffPlanAccessDefault = tpaStaffPlanAccessDefault;
	}

	public boolean isEditPlanData() {
		return editPlanData;
	}

	public void setEditPlanData(boolean editPlanData) {
		this.editPlanData = editPlanData;
	}

	public String getEditPlanDataDefault() {
		return editPlanDataDefault;
	}

	public void setEditPlanDataDefault(String editPlanDataDefault) {
		this.editPlanDataDefault = editPlanDataDefault;
	}

	public boolean isShowEditPlanData() {
		return showEditPlanData;
	}

	public void setShowEditPlanData(boolean showEditPlanData) {
		this.showEditPlanData = showEditPlanData;
	}
    
	public boolean isShowSubmitUpdateVesting() {
        return showSubmitUpdateVesting;
    }

    public void setShowSubmitUpdateVesting(boolean showSubmitUpdateVesting) {
        this.showSubmitUpdateVesting = showSubmitUpdateVesting;
    }

    public boolean isSubmitUpdateVesting() {
        return submitUpdateVesting;
    }

    public void setSubmitUpdateVesting(boolean submitUpdateVesting) {
        this.submitUpdateVesting = submitUpdateVesting;
    }

    public String getSubmitUpdateVestingDefault() {
        return submitUpdateVestingDefault;
    }

    public void setSubmitUpdateVestingDefault(String submitUpdateVestingDefault) {
        this.submitUpdateVestingDefault = submitUpdateVestingDefault;
    }

    public Map getFormAsMap(String fieldPrefix) {
        Map formMap = new HashMap();
        // contract number
        String fieldId = fieldPrefix + FIELD_MANAGE_USERS;
        formMap.put(fieldId, isManageUsers());
        fieldId = fieldPrefix + FIELD_SELECTED_ACCESS;
        formMap.put(fieldId, isSelectedAccess());
        fieldId = fieldPrefix + FIELD_EDIT_CONTRACT_SERVICE_FEATURES;
        formMap.put(fieldId, isEditContractServiceFeatures());
        fieldId = fieldPrefix + FIELD_DOWNLOAD_REPORTS;
        formMap.put(fieldId, isDownloadReports());
        fieldId = fieldPrefix + FIELD_EMPLOYER_STATEMENTS;
        formMap.put(fieldId, isEmployerStatements());
        fieldId = fieldPrefix + FIELD_VIEW_SUBMISSIONS;
        formMap.put(fieldId, isViewSubmissions());
        fieldId = fieldPrefix + FIELD_CREATE_UPLOAD_SUBMISSIONS;
        formMap.put(fieldId, isCreateUploadSubmissions());
        fieldId = fieldPrefix + FIELD_VIEW_ALL_USERS_SUBMISSIONS;
        formMap.put(fieldId, isViewAllUsersSubmissions());
        fieldId = fieldPrefix + FIELD_CASH_ACCOUNT;
        formMap.put(fieldId, isCashAccount());
        fieldId = fieldPrefix + FIELD_DIRECT_DEBIT;
        formMap.put(fieldId, isDirectDebit());
        fieldId = fieldPrefix + FIELD_INITIATE_AND_VIEW_WITHDRAWALS;
        formMap.put(fieldId, isInitiateAndViewMyWithdrawals());
        fieldId = fieldPrefix + FIELD_VIEW_ALL_WITHDRAWALS;
        formMap.put(fieldId, isViewAllWithdrawals());
        fieldId = fieldPrefix + FIELD_REVIEW_WITHDRAWALS;
        formMap.put(fieldId, isReviewWithdrawals());
        fieldId = fieldPrefix + FIELD_SIGNING_AUTHORITY;
        formMap.put(fieldId, isSigningAuthority());
        fieldId = fieldPrefix + FIELD_UPDATE_CENSUS_DATA;
        formMap.put(fieldId, isUpdateCensusData());
        fieldId = fieldPrefix + FIELD_VIEW_SALARY;
        formMap.put(fieldId, isViewSalary());
        fieldId = fieldPrefix + FIELD_VIEW_ALL_LOANS;
        formMap.put(fieldId, isViewAllLoans());
        fieldId = fieldPrefix + FIELD_INITIATE_LOANS;
        formMap.put(fieldId, isInitiateLoans());
        fieldId = fieldPrefix + FIELD_REVIEW_LOANS;
        formMap.put(fieldId, isReviewLoans());
        fieldId=fieldPrefix + FIELD_NOTICE_MANAGER;
        formMap.put(fieldId,  isNoticeManager());
        
        fieldId = fieldPrefix + FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS;
        StringBuffer directDebitAccounts = new StringBuffer();
        for (Iterator dit = getSelectedDirectDebitAccountsAsList().iterator(); dit.hasNext();) {
            BankAccount bankAccount = (BankAccount) dit.next();
            if (bankAccount.getDirectDebitAccount() != null) {
                directDebitAccounts.append("(").append(
                        bankAccount.getDirectDebitAccount().getInstructionNumber()).append(") ");
            }
            directDebitAccounts.append(bankAccount.getLabel()).append(", ");
        }
        formMap.put(fieldId, directDebitAccounts.toString());

        return formMap;
    }

	public String getInitiateLoansDefault() {
		return initiateLoansDefault;
	}

	public void setInitiateLoansDefault(String initiateLoansDefault) {
		this.initiateLoansDefault = initiateLoansDefault;
	}

	public String getViewAllLoansDefault() {
		return viewAllLoansDefault;
	}

	public void setViewAllLoansDefault(String viewAllLoansDefault) {
		this.viewAllLoansDefault = viewAllLoansDefault;
	}

	public String getReviewLoansDefault() {
		return reviewLoansDefault;
	}

	public void setReviewLoansDefault(String reviewLoansDefault) {
		this.reviewLoansDefault = reviewLoansDefault;
	}

	public boolean isInitiateLoans() {
		return initiateLoans;
	}

	public void setInitiateLoans(boolean initiateLoans) {
		this.initiateLoans = initiateLoans;
	}

	public boolean isViewAllLoans() {
		return viewAllLoans;
	}

	public void setViewAllLoans(boolean viewAllLoans) {
		this.viewAllLoans = viewAllLoans;
	}

	public boolean isReviewLoans() {
		return reviewLoans;
	}

	public void setReviewLoans(boolean reviewLoans) {
		this.reviewLoans = reviewLoans;
	}

	public boolean isShowInitiateLoans() {
		return showInitiateLoans;
	}

	public void setShowInitiateLoans(boolean showInitiateLoans) {
		this.showInitiateLoans = showInitiateLoans;
	}

	public boolean isShowViewAllLoans() {
		return showViewAllLoans;
	}

	public void setShowViewAllLoans(boolean showViewAllLoans) {
		this.showViewAllLoans = showViewAllLoans;
	}

	public boolean isShowReviewLoans() {
		return showReviewLoans;
	}

	public void setShowReviewLoans(boolean showReviewLoans) {
		this.showReviewLoans = showReviewLoans;
	}

	public boolean isShowLoansPermissions() {
        return showInitiateLoans || showViewAllLoans || showReviewLoans;
    }
}
