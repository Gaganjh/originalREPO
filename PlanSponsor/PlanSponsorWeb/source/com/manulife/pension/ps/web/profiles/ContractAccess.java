package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
 
/**
 * @author Charles Chan
 */
public class ContractAccess implements Cloneable, Serializable {

    private Integer contractNumber;
	private String companyName;
	private String planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;
    private String roleType;
	// TODO private Boolean contractAllocated;
	private Boolean directDebit;
	private Boolean cashAccount;
	private Boolean uploadSubmissions;
	private Boolean statementsAccessAvailable;
	private Boolean participantAddressDownloadAccessAvailable;
	private Boolean reportDownload;
//	private Boolean deferralEmail;
//	private Boolean enrollmentEmail;
	private Boolean viewAllSubmissions;
	private Boolean submissionAccess;
	private Boolean tpaStaffPlanAccess;
//	private Boolean receiveIloansEmail;
	private List directDebitAccounts = new ArrayList();
	private String[] selectedDirectDebitAccounts = new String[0];
	private Boolean manageUsers;
	private Boolean updateCensusData;
	
	private Boolean viewSalary;
	private Boolean reviewIWithdrawals;
	private Boolean reviewLoans;
	private Boolean editApprovePlan;
	private Boolean submitUpdateVesting;
	
	private boolean showDirectDebit;
	private boolean showCashAccount;
	private boolean showUploadSubmissions;
	private boolean showStatementsAccessAvailable;
	private boolean showParticipantAddressDownloadAccessAvailable;
	private boolean showReportDownload;
//	private boolean showDeferralEmail;
//	private boolean showEnrollmentEmail;
	private boolean showViewAllSubmissions;
	private boolean showSubmissionAccess;
	private boolean showTpaStaffPlanAccess;
//	private boolean showReceiveIloansEmail;
	private boolean permissionsNotShown;
	private boolean accountsNotShown;
	private boolean isExternalUserManager;
	private boolean isTpaUserManager;
	
	/**
	 * The NO_DIRECT_DEBIT_ACCOUNT flag is used to ensure that the browser
	 * send back something for the checkboxes. Normally, when none of the
	 * checkboxes are checked, the browser will not send any information back
	 * to the server. This causes problem because we cached the FORM in the
	 * session. We have no way to deselect all checkboxes if the browser
	 * won't send back anything.
	 */
	public static final String NO_DIRECT_DEBIT_ACCOUNT = "none";
	public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
	public static final String FIELD_PLANSPONSOR_SITE_ROLE = "planSponsorSiteRole";
	public static final String FIELD_DIRECT_DEBIT = "directDebit";
	public static final String FIELD_CASH_ACCOUNT = "cashAccount";
	public static final String FIELD_UPLOAD_SUBMISSIONS = "uploadSubmissions";
	public static final String FIELD_STATEMENTS_ACCESS_AVAILABLE = "statementsAccessAvailable";
	public static final String FIELD_PARTICIPANT_ADDRESS_DOWNLOAD_ACCESS_AVAILABLE = "participantAddressDownloadAccessAvailable";
	public static final String FIELD_REPORT_DOWNLOAD = "reportDownload";
	public static final String FIELD_SUBMISSION_ACCESS = "submissionAccess";
	public static final String FIELD_VIEW_ALL_SUBMISSIONS = "viewAllSubmissions";
	public static final String FIELD_DIRECT_DEBIT_ACCOUNTS = "directDebitAccounts";
	public static final String FIELD_TPA_STAFF_PLAN_ACCESS = "tpaStaffPlanAccess";	
	public static final String FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS = "selectedDirectDebitAccounts";
	public static final String FIELD_UPDATE_CENSUS = "updateCensus";
	public static final String FIELD_VIEW_SALARY = "viewSalary";
	public static final String FIELD_REVIEW_IWITHDRAWALS = "reviewIWithdrawals";
	public static final String FIELD_EDIT_APPROVE_PLAN_DATA = "editPlanData";
	public static final String FIELD_SUBMIT_UPDATE_VESTING = "submitUpdateVesting";
	public static final String FIELD_REVIEW_LOANS = "reviewLoans";
	/**
	 * Constructor.
	 */
	public ContractAccess() {
		super();
		directDebit = Boolean.FALSE;
		cashAccount = Boolean.FALSE;
		uploadSubmissions = Boolean.FALSE;
		statementsAccessAvailable = Boolean.FALSE;
		participantAddressDownloadAccessAvailable = Boolean.FALSE;
		reportDownload = Boolean.FALSE;
		viewAllSubmissions = Boolean.FALSE;
		tpaStaffPlanAccess = Boolean.FALSE;
		submissionAccess = Boolean.FALSE;
		manageUsers = Boolean.FALSE;
		updateCensusData = Boolean.FALSE;
		viewSalary = Boolean.FALSE;
		reviewIWithdrawals = Boolean.FALSE;
		editApprovePlan = Boolean.FALSE;
        submitUpdateVesting = Boolean.FALSE;
		reviewLoans = Boolean.FALSE;
		setAllShowPermissions(true);
	}

	/**
	 * Gets the contractNumber
	 *
	 * @return Returns a int
	 */
	public Integer getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 *
	 * @param contractNumber
	 *            The contractNumber to set
	 */
	public void setContractNumber(Integer contractNumber) {
		this.contractNumber = contractNumber;
	}

	public boolean isDirectDebit() {
		return directDebit == null ? false : directDebit.booleanValue();
	}

	/**
	 * Checks if directDebit
	 *
	 * @return Returns a boolean
	 */
	public Boolean getDirectDebit() {
		return directDebit;
	}

	/**
	 * Sets the directDebit
	 *
	 * @param directDebit
	 *            The directDebit to set
	 */
	public void setDirectDebit(Boolean directDebit) {
		this.directDebit = directDebit;
	}

	public boolean isCashAccount() {
		return cashAccount == null ? false : cashAccount.booleanValue();
	}

	/**
	 * Checks if cashAccount
	 *
	 * @return Returns a Boolean
	 */
	public Boolean getCashAccount() {
		return cashAccount;
	}

	/**
	 * Sets the cashAccount
	 *
	 * @param cashAccount
	 *            The cashAccount to set
	 */
	public void setCashAccount(Boolean cashAccount) {
		this.cashAccount = cashAccount;
	}

	/**
	 * Chekcs if viewSubmissions
	 *
	 * @return Returns a Boolean
	 */
	public Boolean getSubmissionAccess() {
		return submissionAccess;
	}

	/**
	 * Sets the viewSubmissions
	 *
	 * @param viewSubmissions
	 *            The viewSubmissions to set
	 */
	public void setSubmissionAccess(Boolean viewSubmissions) {
		this.submissionAccess = viewSubmissions;
	}

	public boolean isViewSubmissions() {
        return submissionAccess == null ? false : submissionAccess.booleanValue();
    }

	public boolean isUploadSubmissions() {
		return uploadSubmissions == null ? false : uploadSubmissions.booleanValue();
	}

	/**
	 * Chekcs if uploadSubmissions
	 *
	 * @return Returns a Boolean
	 */
	public Boolean getUploadSubmissions() {
		return uploadSubmissions;
	}

	/**
	 * Sets the uploadSubmissions
	 *
	 * @param uploadSubmissions
	 *            The uploadSubmissions to set
	 */
	public void setUploadSubmissions(Boolean uploadSubmissions) {
		this.uploadSubmissions = uploadSubmissions;
	}

	public boolean isStatementsAccessAvailable() {
		return statementsAccessAvailable == null ? false : statementsAccessAvailable.booleanValue();
	}

	/**
	 * Checks if statementsAccessAvailable
	 *
	 * @return Returns a Boolean
	 */
	public Boolean getStatementsAccessAvailable() {
		return statementsAccessAvailable;
	}

	/**
	 * Sets the statementsAccessAvailable
	 *
	 * @param statementsAccessAvailable
	 *            The statementsAccessAvailable to set
	 */
	public void setStatementsAccessAvailable(Boolean statementsAccessAvailable) {
		this.statementsAccessAvailable = statementsAccessAvailable;
	}

	public boolean isParticipantAddressDownloadAccessAvailable() {
		return participantAddressDownloadAccessAvailable == null
				? false
				: participantAddressDownloadAccessAvailable.booleanValue();
	}

	/**
	 * Checks if participantAddressDownloadAccessAvailable
	 *
	 * @return Returns a Boolean
	 */
	public Boolean getParticipantAddressDownloadAccessAvailable() {
		return participantAddressDownloadAccessAvailable;
	}

	/**
	 * Sets the participantAddressDownloadAccessAvailable
	 *
	 * @param participantAddressDownloadAccessAvailable
	 *            The participantAddressDownloadAccessAvailable to set
	 */
	public void setParticipantAddressDownloadAccessAvailable(
			Boolean participantAddressDownloadAccessAvailable) {
		this.participantAddressDownloadAccessAvailable = participantAddressDownloadAccessAvailable;
	}

	/* TODO public boolean isContractAllocated()
	{
		return contractAllocated == null ? false : contractAllocated.booleanValue();
	}	*/


//	public boolean isReportDownload() {
//		return reportDownload == null ? false : reportDownload
//				.booleanValue();
//	}
	
	/**
	 * @return Returns the reportDownload.
	 */
	public Boolean getReportDownload() {
		return reportDownload;
	}

	/**
	 * @param reportDownload
	 *            The reportDownload to set.
	 */
	public void setReportDownload(Boolean reportDownload) {
		this.reportDownload = reportDownload;
	}

	public boolean isViewAllSubmissions() {
		return viewAllSubmissions == null ? false : viewAllSubmissions.booleanValue();
	}

	/**
	 * @return Returns the viewAllSubmissions.
	 */
	public Boolean getViewAllSubmissions() {
		return viewAllSubmissions;
	}

	/**
	 * @param tpaStaffPlanAccess
	 *            The tpaStaffPlanAccess to set.
	 */
	public void setViewAllSubmissions(Boolean viewAllSubmissions) {
		this.viewAllSubmissions = viewAllSubmissions;
	}

//	public boolean isTpaStaffPlanAccess() {
//		return tpaStaffPlanAccess == null ? false : tpaStaffPlanAccess.booleanValue();
//	}
	
	/**
	 * @return Returns the tpaStaffPlanAccess.
	 */
	public Boolean getTpaStaffPlanAccess() {
		return tpaStaffPlanAccess;
	}
	
	/**
	 * @param tpaStaffPlanAccess
	 *            The tpaStaffPlanAccess to set.
	 */
	public void setTpaStaffPlanAccess(Boolean tpaStaffPlanAccess) {
		this.tpaStaffPlanAccess = tpaStaffPlanAccess;
	}

	
	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            The companyName to set.
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
	/**
	 * @return Returns the accessLevel.
	 */
	public String getPlanSponsorSiteRole() {
		return planSponsorSiteRole;
	}

	/**
	 * @param accessLevel
	 *            The accessLevel to set.
	 */
	public void setPlanSponsorSiteRole(String accessLevel) {
		this.planSponsorSiteRole = accessLevel;
	}

	public void setAllShowPermissions(boolean value) {
		showDirectDebit = value;
		showCashAccount = value;
		showSubmissionAccess = value;
		showUploadSubmissions = value;
		showStatementsAccessAvailable = value;
		showParticipantAddressDownloadAccessAvailable = value;
		showReportDownload = value;
		showViewAllSubmissions = value;
		showTpaStaffPlanAccess = value;
	}

	public Object clone() {
		try {
			ContractAccess clonedObject = (ContractAccess)super.clone();
			/*
			 * Add all direct debit accounts into the clone object.
			 */
			clonedObject.directDebitAccounts = new ArrayList();
			for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
				BankAccount account = (BankAccount)it.next();
				clonedObject.getDirectDebitAccounts().add(account.clone());
			}
			return clonedObject;
		} catch (CloneNotSupportedException e) {
			// this should not happen because this object implements Cloneable
			throw new NestableRuntimeException(e);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("contractNumber [").append(contractNumber).append(
				"] companyName [").append(companyName).append(
				"] planSponsorSiteRole [").append(planSponsorSiteRole).append(
				"] directDebit [").append(directDebit)
				.append("] cashAccount [").append(cashAccount).append(
						"] statementsAccessAvailable [").append(
						statementsAccessAvailable).append(
						"] participantAddressDownloadAccessAvailable [")
				.append(
                        participantAddressDownloadAccessAvailable).append("]");
		
		return sb.toString();
	}

	public boolean isShowCashAccount() {
		return showCashAccount;
	}

	public void setShowCashAccount(boolean showCashAccount) {
		this.showCashAccount = showCashAccount;
	}

	public boolean isShowDirectDebit() {
		return showDirectDebit;
	}

	public void setShowDirectDebit(boolean showDirectDebit) {
		this.showDirectDebit = showDirectDebit;
	}

	public boolean isShowSubmissionAccess() {
		return showSubmissionAccess;
	}

	public void setShowSubmissionAccess(boolean showViewSubmissions) {
		this.showSubmissionAccess = showViewSubmissions;
	}

	public boolean isShowUploadSubmissions() {
		return showUploadSubmissions;
	}

	public void setShowUploadSubmissions(
			boolean showUploadSubmissions) {
		this.showUploadSubmissions = showUploadSubmissions;
	}

	public boolean isShowParticipantAddressDownloadAccessAvailable() {
		return showParticipantAddressDownloadAccessAvailable;
	}

	public void setShowParticipantAddressDownloadAccessAvailable(
			boolean showParticipantAddressDownloadAccessAvailable) {
		this.showParticipantAddressDownloadAccessAvailable = showParticipantAddressDownloadAccessAvailable;
	}

	public boolean isShowStatementsAccessAvailable() {
		return showStatementsAccessAvailable;
	}

	public void setShowStatementsAccessAvailable(
			boolean showStatementsAccessAvailable) {
		this.showStatementsAccessAvailable = showStatementsAccessAvailable;
	}

	public boolean isShowReportDownload() {
		return showReportDownload;
	}

	public void setShowReportDownload(boolean showReportDownload) {
		this.showReportDownload = showReportDownload;
	}
	
	public boolean isShowViewAllSubmissions() {
		return showViewAllSubmissions;
	}

	public void setShowViewAllSubmissions(boolean showViewAllSubmissions) {
		this.showViewAllSubmissions = showViewAllSubmissions;
	}
	
	public boolean isShowTpaStaffPlanAccess() {
		return showTpaStaffPlanAccess;
	}

	public void setShowTpaStaffPlanAccess(boolean showTpaStaffPlanAccess) {
		this.showTpaStaffPlanAccess = showTpaStaffPlanAccess;
	}

	
	public List getDirectDebitAccounts() {
		return directDebitAccounts;
	}

	public String[] getSelectedDirectDebitAccounts() {
	    return selectedDirectDebitAccounts;
	}

	public void setSelectedDirectDebitAccounts(String[] selectedDirectDebitAccounts) {
	    this.selectedDirectDebitAccounts = selectedDirectDebitAccounts;
	}

	/*public void setContractAllocated(Boolean isAllocated) {
		//this.contractAllocated = isAllocated;
	}*/
	
    public List getSelectedDirectDebitAccountsAsList() {
        List accounts = new ArrayList();
        if (selectedDirectDebitAccounts != null) {
            for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
                BankAccount bankAccount = (BankAccount) it.next();
                for (int i = 0; i < selectedDirectDebitAccounts.length; i++) {
                    /*
                     * Skip the NO_DIRECT_DEBIT_ACCOUNT flag.
                     */
                    if (selectedDirectDebitAccounts.equals(NO_DIRECT_DEBIT_ACCOUNT)) {
                        continue;
                    }
                    if (bankAccount.getPrimaryKey().equals(
                            selectedDirectDebitAccounts[i])) {
                        accounts.add(bankAccount);
                        break;
                    }
                }
            }
        }
        return accounts;
    }

    public String getSelectedDirectDebitAccountsAsString() {
        List accounts = getSelectedDirectDebitAccountsAsList();
        StringBuffer sb = new StringBuffer();
        for (Iterator it = accounts.iterator(); it.hasNext();) {
            BankAccount bankAccount = (BankAccount)it.next();
            sb.append(bankAccount.getLabel());
            if (it.hasNext()) {
                sb.append(ReportController.LINE_BREAK);
            }
        }
        return sb.toString();
    }

    public String getSelectedDirectDebitAccountIdsAsString() {
        List accounts = getSelectedDirectDebitAccountsAsList();
        StringBuffer sb = new StringBuffer();
        for (Iterator it = accounts.iterator(); it.hasNext();) {
            BankAccount bankAccount = (BankAccount)it.next();
            sb.append(bankAccount.getPrimaryKey()).append(",");
        }
        return sb.toString();
    }

    public int getNumberOfSelectedDirectDebitAccounts() {
        return getSelectedDirectDebitAccountsAsList().size();
    }
    
    public boolean getAccountsNotShown() {
        return accountsNotShown;
    }

    public void setAccountsNotShown(boolean accountsNotShown) {
        this.accountsNotShown = accountsNotShown;
    }

    public boolean getPermissionsNotShown() {
        return permissionsNotShown;
    }

    public void setPermissionsNotShown(boolean permissionsNotShown) {
        this.permissionsNotShown = permissionsNotShown;
    }
    
    public Map getFormAsMap(String fieldPrefix) {
        Map formMap = new HashMap();
        
		String fieldId = fieldPrefix + FIELD_CONTRACT_NUMBER;
		formMap.put(fieldId, getContractNumber());

        /*
         * No need to log TPA role because there's only 1. (Defect 10673)
         */
        if (!getPlanSponsorSiteRole().equals(
                new ThirdPartyAdministrator().toString())) {
            fieldId = fieldPrefix + FIELD_PLANSPONSOR_SITE_ROLE;
            formMap.put(fieldId, getPlanSponsorSiteRole());
        }
        
		fieldId = fieldPrefix + FIELD_CASH_ACCOUNT;
		formMap.put(fieldId, getCashAccount());

		fieldId = fieldPrefix + FIELD_DIRECT_DEBIT;
		formMap.put(fieldId, getDirectDebit());

		fieldId = fieldPrefix + FIELD_UPLOAD_SUBMISSIONS;
		formMap.put(fieldId, getUploadSubmissions());

		fieldId = fieldPrefix
				+ FIELD_PARTICIPANT_ADDRESS_DOWNLOAD_ACCESS_AVAILABLE;
		formMap.put(fieldId, getParticipantAddressDownloadAccessAvailable());

		fieldId = fieldPrefix
				+ FIELD_STATEMENTS_ACCESS_AVAILABLE;
		formMap.put(fieldId, getStatementsAccessAvailable());

        fieldId = fieldPrefix + FIELD_REPORT_DOWNLOAD;
		formMap.put(fieldId, getReportDownload());
                
		fieldId = fieldPrefix + FIELD_SUBMISSION_ACCESS;
		formMap.put(fieldId, getSubmissionAccess());
		
		formMap.put(fieldPrefix + FIELD_UPDATE_CENSUS, getUpdateCensusData());
		
		formMap.put(fieldPrefix + FIELD_VIEW_SALARY, getViewSalary());
		formMap.put(fieldPrefix + FIELD_REVIEW_IWITHDRAWALS, getReviewIWithdrawals());
		formMap.put(fieldPrefix + FIELD_EDIT_APPROVE_PLAN_DATA, getEditApprovePlan());
		formMap.put(fieldPrefix + FIELD_SUBMIT_UPDATE_VESTING, getSubmitUpdateVesting());
		
		fieldId = fieldPrefix + FIELD_VIEW_ALL_SUBMISSIONS;
		formMap.put(fieldId, getViewAllSubmissions());

		if (getPlanSponsorSiteRole().equals(
				new ThirdPartyAdministrator().toString()) || getPlanSponsorSiteRole().equals(
				new TPAUserManager().toString()) ) {
			fieldId = fieldPrefix + FIELD_TPA_STAFF_PLAN_ACCESS;
			formMap.put(fieldId, getTpaStaffPlanAccess());
		}

        fieldId = fieldPrefix
                + FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS;
        StringBuffer directDebitAccounts = new StringBuffer();
        for (Iterator dit = getSelectedDirectDebitAccountsAsList().iterator(); dit
                .hasNext();) {
            BankAccount bankAccount = (BankAccount) dit.next();
            if (bankAccount.getDirectDebitAccount() != null) {
                directDebitAccounts.append("(").append(
                        bankAccount.getDirectDebitAccount()
                                .getInstructionNumber()).append(") ");
            }
            directDebitAccounts.append(bankAccount.getLabel()).append(", ");
        }
        formMap.put(fieldId, directDebitAccounts.toString());
   
        return formMap;
    }

	public Boolean getManageUsers() {
		return manageUsers;
	}

	public void setManageUsers(Boolean manageUsers) {
		this.manageUsers = manageUsers;
	}

	public Boolean getReviewIWithdrawals() {
		return reviewIWithdrawals;
	}

	public void setReviewIWithdrawals(Boolean reviewIWithdrawals) {
		this.reviewIWithdrawals = reviewIWithdrawals;
	}

	public Boolean getUpdateCensusData() {
		return updateCensusData;
	}

	public void setUpdateCensusData(Boolean updateCensusData) {
		this.updateCensusData = updateCensusData;
	}

	public Boolean getViewSalary() {
		return viewSalary;
	}

	public void setViewSalary(Boolean viewSalary) {
		this.viewSalary = viewSalary;
	}

	
	public Boolean getEditApprovePlan() {
		return editApprovePlan;
	}

	public void setEditApprovePlan(Boolean editApprovePlan) {
		this.editApprovePlan = editApprovePlan;
	}

	public Boolean getSubmitUpdateVesting() {
        return submitUpdateVesting;
    }

    public void setSubmitUpdateVesting(Boolean submitUpdateVesting) {
        this.submitUpdateVesting = submitUpdateVesting;
    }

    public void setExternalUserManager(boolean isExternalUserManager)
	{
		this.isExternalUserManager = isExternalUserManager;
	}
	
	public boolean isExternalUserManager()
	{
		return this.isExternalUserManager;
	}

	public void setTpaUserManager(boolean isExternalUserManager)
	{
		this.isTpaUserManager = isExternalUserManager;
	}
	
	public boolean isTpaUserManager()
	{
		return this.isTpaUserManager;
	}

	/**
	 * @return Returns true if there is any access.
	 */
	public boolean getHasAccess() {
	
		if (showReportDownload == true
//		        || showDeferralEmail == true
//		        || showEnrollmentEmail == true
		        || showViewAllSubmissions == true
		        || showSubmissionAccess == true
		        || showTpaStaffPlanAccess == true
//		        || showReceiveIloansEmail == true
        )
			return true;
		else
			return false;
	}

    /**
     * @return the roleType
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * @param roleType the roleType to set
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public Boolean getReviewLoans() {
		return reviewLoans;
	}

	public void setReviewLoans(Boolean reviewLoans) {
		this.reviewLoans = reviewLoans;
	}
}
