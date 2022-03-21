package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;

public class TPAUserContractAccess extends BaseContractAccess {

	private Integer contractNumber;
	private String companyName;
	private String planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;
	// TODO private Boolean contractAllocated;
	private Boolean directDebit;
	private Boolean cashAccount;
	private Boolean uploadSubmissions;
	private Boolean statementsAccessAvailable;
	private Boolean participantAddressDownloadAccessAvailable;
	private Boolean reportDownload;
	private Boolean tpaStaffPlanAccess;
	private List directDebitAccounts = new ArrayList();
	private String[] selectedDirectDebitAccounts = new String[0];
	private Boolean manageUsers;
	private Boolean updateCensusData;
 /*Added new attribute for 404a5 permission*/
	private Boolean feeAccess404A5;
	private Boolean viewSalary;
	private Boolean reviewIWithdrawals;
	private Boolean editApprovePlan;
	private Boolean submitUpdateVesting;
	
	
	// stuff from the TPA User Permissions sub-screen(some already exist above)	
    private Boolean signingAuthority;
    private Boolean initiateIWithdrawals;
    private Boolean viewAllIWithdrawals;
    private Boolean detailedPermissionsSet; // indicates if sub-page was hit and return from 
    private Boolean originalReviewIWithdrawals;  // value when screen is first rendered
	
    // loans permissions
    private boolean initiateLoans;
    private boolean viewAllLoans;
    private boolean reviewLoans;
    private boolean showInitiateLoans;
    private boolean showViewAllLoans;
    private boolean showReviewLoans;
    private boolean originalReviewLoans;
    
	private boolean showDirectDebit;
	private boolean showCashAccount;
	private boolean showUploadSubmissions;
	private boolean showStatementsAccessAvailable;
	private boolean showParticipantAddressDownloadAccessAvailable;
	private boolean showReportDownload;
	private boolean showTpaStaffPlanAccess;
	private boolean permissionsNotShown;
	private boolean accountsNotShown;
	private boolean showUpdateCensusData;
	private boolean showViewSalary;
	private boolean showReviewIWithdrawals;
	private boolean showSigningAuthority;
	private boolean showInitiateIWithdrawals;
	private boolean showPlanData;
	/*Added new attribute for 404a5 permission*/
	private boolean showFeeAccess404A5  ;
	
	private boolean isExternalUserManager;
		
	
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
	public static final String FIELD_DIRECT_DEBIT_ACCOUNTS = "directDebitAccounts";
	public static final String FIELD_TPA_STAFF_PLAN_ACCESS = "tpaStaffPlanAccess";	
	public static final String FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS = "selectedDirectDebitAccounts";
	public static final String FIELD_UPDATE_CENSUS = "updateCensus";
	/*Added new attribute for 404a5 permission*/
	public static final String FILELD_FEE_ACCESS_404A5="feeAccess404A5";
	public static final String FIELD_VIEW_SALARY = "viewSalary";
	public static final String FIELD_REVIEW_IWITHDRAWALS = "reviewIWithdrawals";
	public static final String FIELD_EDIT_APPROVE_PLAN_DATA = "editPlanData";
	public static final String FIELD_SUBMIT_UPDATE_VESTING = "submitUpdateVesting";
	public static final String FIELD_MANAGE_USERS = "manageUsers";
	public static final String FIELD_SIGNING_AUTHORITY = "signingAuthority";
	public static final String FIELD_INITIATE_WITHDRAWALS = "initWithdrawals";
	public static final String FIELD_VIEW_ALL_WITHDRAWALS = "viewAllWithdrawals";
	public static final String FIELD_INITIATE_LOANS = "initiateLoans";
	public static final String FIELD_VIEW_ALL_LOANS = "viewAllLoans";
	public static final String FIELD_REVIEW_LOANS = "reviewLoans";	

	/**
	 * Constructor.
	 */
	public TPAUserContractAccess() {
		super();
		directDebit = Boolean.FALSE;
		cashAccount = Boolean.FALSE;
		uploadSubmissions = Boolean.FALSE;
		statementsAccessAvailable = Boolean.FALSE;
		participantAddressDownloadAccessAvailable = Boolean.FALSE;
		reportDownload = Boolean.FALSE;
		tpaStaffPlanAccess = Boolean.FALSE;
		manageUsers = Boolean.FALSE;
		updateCensusData = Boolean.FALSE;
		/*Added new attribute for 404a5 permission*/
		feeAccess404A5=Boolean.FALSE;
		viewSalary = Boolean.FALSE;
		reviewIWithdrawals = Boolean.FALSE;
		editApprovePlan = Boolean.FALSE;
	    signingAuthority = Boolean.FALSE;
		submitUpdateVesting = Boolean.FALSE;
	    initiateIWithdrawals = Boolean.FALSE;
	    this.viewAllIWithdrawals = Boolean.FALSE;
	    this.detailedPermissionsSet = Boolean.FALSE;
	    this.initiateLoans = Boolean.FALSE;
	    this.viewAllLoans = Boolean.FALSE;
	    this.reviewLoans = Boolean.FALSE;
		
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

	//we already have a bean accessor name getDirectDebit, 
	//and while this is supposed to be legal, struts or somebody has a problem with it.
	public boolean isDirectDebitTrue() {
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

	//we already have a bean accessor name getCashAccount, 
	//and while this is supposed to be legal, struts or somebody has a problem with it.
	public boolean isCashAccountTrue() {
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

	//we already have a bean accessor name getuploadsubmissions, 
	//and while this is supposed to be legal, struts or somebody has a problem with it.
	public boolean isUploadSubmissionsTrue() {
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
		return false;
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
		return false;
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

	/*
	 * TODO public boolean isContractAllocated()
	{
		return contractAllocated == null ? false : contractAllocated.booleanValue();
	}	*/


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
	
	public boolean isShowUpdateCensusData() {
		return showUpdateCensusData;
	}
	

	public void setShowUpdateCensusData(boolean showUpdateCensusData) {
		this.showUpdateCensusData = showUpdateCensusData;
	}
	/*get the value  404a5 permission*/
	public boolean isShowFeeAccess404A5() {
		return showFeeAccess404A5;
	}
	/* set the value for 404a5 permission*/
	public void setShowFeeAccess404A5(boolean showFeeAccess404A5) {
		this.showFeeAccess404A5 = showFeeAccess404A5;
	}
	public boolean isShowReviewIWithdrawals() {
		return showReviewIWithdrawals;
	}

	public void setShowReviewIWithdrawals(boolean showReviewIWithdrawals) {
		this.showReviewIWithdrawals = showReviewIWithdrawals;
	}

	public boolean isShowPlanData() {
		return showPlanData;
	}

	public void setShowPlanData(boolean showPlanData) {
		this.showPlanData = showPlanData;
	}

	public void setAllShowPermissions(boolean value) {
		showDirectDebit = value;
		showCashAccount = value;
		showUploadSubmissions = value;
		showStatementsAccessAvailable = value;
		showParticipantAddressDownloadAccessAvailable = value;
		showReportDownload = value;
		showTpaStaffPlanAccess = value;
		showUpdateCensusData = value;
		showFeeAccess404A5= value;
		showViewSalary = value;
		showReviewIWithdrawals = value;
		showPlanData = value;
		showInitiateLoans = value;
		showViewAllLoans = value;
		showReviewLoans = value;
	}

	public Object clone() {
		try {
			TPAUserContractAccess clonedObject = (TPAUserContractAccess)super.clone();
			/*
			 * Add all direct debit accounts into the clone object.
			 */
			clonedObject.directDebitAccounts = new ArrayList();
			for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
				BankAccount account = (BankAccount)it.next();
				clonedObject.getDirectDebitAccounts().add(account.clone());
			}
			
			clonedObject.setUserPermissions((UserPermissions)this.userPermissions.clone());
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
	//	this.contractAllocated = isAllocated;
	}*/
	
	public boolean isShowViewSalary() {
		return showViewSalary;
	}

	public void setShowViewSalary(boolean showViewSalary) {
		this.showViewSalary = showViewSalary;
	}

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
                
		formMap.put(fieldPrefix + FIELD_UPDATE_CENSUS, getUpdateCensusData());
		formMap.put(fieldPrefix + FILELD_FEE_ACCESS_404A5,getFeeAccess404A5());
		formMap.put(fieldPrefix + FIELD_VIEW_SALARY, getViewSalary());
		formMap.put(fieldPrefix + FIELD_REVIEW_IWITHDRAWALS, getReviewIWithdrawals());
		formMap.put(fieldPrefix + FIELD_EDIT_APPROVE_PLAN_DATA, getEditApprovePlan());
		formMap.put(fieldPrefix + FIELD_SUBMIT_UPDATE_VESTING, getSubmitUpdateVesting());
		formMap.put(fieldPrefix + FIELD_MANAGE_USERS, getManageUsers());
		formMap.put(fieldPrefix + FIELD_SIGNING_AUTHORITY, getSigningAuthority());
		formMap.put(fieldPrefix + FIELD_INITIATE_WITHDRAWALS, getInitiateIWithdrawals());
		formMap.put(fieldPrefix + FIELD_VIEW_ALL_WITHDRAWALS, getViewAllIWithdrawals());

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

        formMap.put(fieldPrefix + FIELD_INITIATE_LOANS, isInitiateLoans());
		formMap.put(fieldPrefix + FIELD_VIEW_ALL_LOANS, isViewAllLoans());
		formMap.put(fieldPrefix + FIELD_REVIEW_LOANS, isReviewLoans());
		
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
	public Boolean getFeeAccess404A5() {
		return feeAccess404A5;
	}

	public void setFeeAccess404A5(Boolean feeAccess404A5) {
		this.feeAccess404A5 = feeAccess404A5;
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
	
	

	public Boolean getSigningAuthority() {
		return signingAuthority;
	}

	public void setSigningAuthority(Boolean signingAuthority) {
		this.signingAuthority = signingAuthority;
	}

	public Boolean getInitiateIWithdrawals() {
		return initiateIWithdrawals;
	}

	public void setInitiateIWithdrawals(Boolean initiateIWithdrawals) {
		this.initiateIWithdrawals = initiateIWithdrawals;
	}
	
	public Boolean getViewAllIWithdrawals() {
		return viewAllIWithdrawals;
	}

	public void setViewAllIWithdrawals(Boolean viewAllIWithdrawals) {
		this.viewAllIWithdrawals = viewAllIWithdrawals;
	}

	/**
	 * @return Returns true if there is any access ignore any that default to yes.
	 * 
	 * In support of STA.40(section 3.3.8)
	 */
	public boolean getHasAccess() {
	
		if (this.getTpaStaffPlanAccess() ||
			this.getReportDownload()     ||
			this.getUpdateCensusData()   ||
			/*Added new attribute for 404a5 permission*/
			this.getFeeAccess404A5()	 ||
			this.getViewSalary()         ||
			this.getInitiateIWithdrawals() ||
			this.getEditApprovePlan()    ||
			this.getSubmitUpdateVesting() ||
			this.getDirectDebit()        ||
			this.getCashAccount()        ||
			this.getSigningAuthority() ||
			this.getReviewIWithdrawals() ||
			this.isInitiateLoans() ||
			this.isReviewLoans())
		
			return true;
		else
			return false;
	}

	public boolean isExternalUserManager()
	{
		return this.isExternalUserManager;
	}
	
	public void setExternalUserManager(boolean isExternalUserManager)
	{
		this.isExternalUserManager = isExternalUserManager;
	}

	public Boolean getDetailedPermissionsSet() {
		return detailedPermissionsSet;
	}

	public void setDetailedPermissionsSet(Boolean detailedPermissionsSet) {
		this.detailedPermissionsSet = detailedPermissionsSet;
	}

	public Boolean getOriginalReviewIWithdrawals() {
		return originalReviewIWithdrawals;
	}

	public void setOriginalReviewIWithdrawals(Boolean previousReviewWithdrawals) {
		this.originalReviewIWithdrawals = previousReviewWithdrawals;
	}

	public boolean isShowSigningAuthority() {
		return showSigningAuthority;
	}

	public void setShowSigningAuthority(boolean showApproveIWithdrawals) {
		this.showSigningAuthority = showApproveIWithdrawals;
	}

	public boolean isShowInitiateIWithdrawals() {
		return showInitiateIWithdrawals;
	}

	public void setShowInitiateIWithdrawals(boolean showInitiateIWithdrawals) {
		this.showInitiateIWithdrawals = showInitiateIWithdrawals;
	}

	/**
	 * @return the initiateLoans
	 */
	public boolean isInitiateLoans() {
		return initiateLoans;
	}

	/**
	 * @param initiateLoans the initiateLoans to set
	 */
	public void setInitiateLoans(boolean initiateLoans) {
		this.initiateLoans = initiateLoans;
	}

	/**
	 * @return the viewAllLoans
	 */
	public boolean isViewAllLoans() {
		return viewAllLoans;
	}

	/**
	 * @param viewAllLoans the viewAllLoans to set
	 */
	public void setViewAllLoans(boolean viewAllLoans) {
		this.viewAllLoans = viewAllLoans;
	}

	/**
	 * @return the reviewLoans
	 */
	public boolean isReviewLoans() {
		return reviewLoans;
	}

	/**
	 * @param reviewLoans the reviewLoans to set
	 */
	public void setReviewLoans(boolean reviewLoans) {
		this.reviewLoans = reviewLoans;
	}

	/**
	 * @return the showInitiateLoans
	 */
	public boolean isShowInitiateLoans() {
		return showInitiateLoans;
	}

	/**
	 * @param showInitiateLoans the showInitiateLoans to set
	 */
	public void setShowInitiateLoans(boolean showInitiateLoans) {
		this.showInitiateLoans = showInitiateLoans;
	}

	/**
	 * @return the showViewAllLoans
	 */
	public boolean isShowViewAllLoans() {
		return showViewAllLoans;
	}

	/**
	 * @param showViewAllLoans the showViewAllLoans to set
	 */
	public void setShowViewAllLoans(boolean showViewAllLoans) {
		this.showViewAllLoans = showViewAllLoans;
	}

	/**
	 * @return the showReviewLoans
	 */
	public boolean isShowReviewLoans() {
		return showReviewLoans;
	}

	/**
	 * @param showReviewLoans the showReviewLoans to set
	 */
	public void setShowReviewLoans(boolean showReviewLoans) {
		this.showReviewLoans = showReviewLoans;
	}

	public boolean isOriginalReviewLoans() {
		return originalReviewLoans;
	}

	public void setOriginalReviewLoans(boolean originalReviewLoans) {
		this.originalReviewLoans = originalReviewLoans;
	}

    public Boolean getSubmitUpdateVesting() {
        return submitUpdateVesting;
    }

    public void setSubmitUpdateVesting(Boolean submitUpdateVesting) {
        this.submitUpdateVesting = submitUpdateVesting;
    }
}
