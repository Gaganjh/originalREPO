package com.manulife.pension.ps.web.profiles;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.ps.web.util.CloneableAutoForm;

/**
 * User by Client/TPA firm user access permissions(sub page-via change permissions button)
 * 
 */
public class UserPermissionsForm extends CloneableAutoForm {
    private static final long serialVersionUID = -1L;

    private boolean tpaData;

    private String userName;

    private String button;

    private int contractNumber;

    private String contractName;

    private String userFirstName;

    private String userLastName;

    private RoleValueLabelBean role;
    
    private String contactType;

    private boolean primaryContact;

    private boolean mailRecipient;

    private boolean businessConverted;

    private String returnToScreen; // add or edit

    private UserPermissions userPermissions;

    // Fields for warning messages
    private boolean lastUserWithManageUsers;

    private boolean lastUserWithReviewIWithdrawals;

    private boolean lastUserWithSigningAuthority;

    private boolean lastUserWithSubmissionsAccess;
    
    // field for review loans warning message
    private boolean lastUserWithReviewLoans;
    
    // TPA warnings
    private boolean lastUserWithReceiveILoansEmailAndTPAStaffPlan;
    private String lastUserWithSigningAuthorityContracts;
    private String lastUserWithReviewIWithdrawalsContracts;
    private boolean firmContractsHaveDirectDebitAccounts;
    // field for review loans warning message
    private String lastUserWithReviewLoansContracts;

    // last client user with review withdrawal permission
    private boolean lastClientUserWithReviewIWithdrawals;
    
    // last client user with review loans permission
    private boolean lastClientUserWithReviewLoans;
    
    // need 2nd copy of clone, used to hold original on load
    // value of form from main page.
    private CloneableForm originalClonedForm;

    // tpa firm permission stuff.
    private String tpaFirmID;

    private String tpaFirmName;

    // Maps the button label to the corresponding action.
    private static final Map<String, String> ACTION_LABEL_MAP = new HashMap<String, String>();

    public static final String BACK_BUTTON_LABEL = "back";

    public static final String CANCEL_BUTTON_LABEL = "cancel";

    public static final String CONTINUE_BUTTON_LABEL = "continue";

    public static final String BACK_BUTTON_ACTION = "back";

    public static final String CANCEL_BUTTON_ACTION = "cancel";

    public static final String CONTINUE_BUTTON_ACTION = "continue";

    static {
        ACTION_LABEL_MAP.put(BACK_BUTTON_LABEL, BACK_BUTTON_ACTION);
        ACTION_LABEL_MAP.put(CANCEL_BUTTON_LABEL, CANCEL_BUTTON_ACTION);
        ACTION_LABEL_MAP.put(CONTINUE_BUTTON_LABEL, CONTINUE_BUTTON_ACTION);
    }

    public Object clone() {
        UserPermissionsForm clonedForm = (UserPermissionsForm) super.clone();
        clonedForm.userPermissions = (UserPermissions) userPermissions.clone();
        if (role != null) {
            clonedForm.role = (RoleValueLabelBean) role.clone();
        }
        return clonedForm;
    }

    /**
     * @return the button
     */
    public String getButton() {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(String button) {
        this.button = button;
        setAction(ACTION_LABEL_MAP.get(button));
    }

    /**
     * @return the userPermissions
     */
    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    /**
     * @param userPermissions the userPermissions to set
     */
    public void setUserPermissions(UserPermissions userPermissions) {
        this.userPermissions = userPermissions;
    }

    /**
     * @return the businessConverted
     */
    public boolean isBusinessConverted() {
        return businessConverted;
    }

    /**
     * @param businessConverted the businessConverted to set
     */
    public void setBusinessConverted(boolean businessConverted) {
        this.businessConverted = businessConverted;
    }

    /**
     * @return the contractName
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * @param contractName the contractName to set
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    /**
     * @return the contractNumber
     */
    public int getContractNumber() {
        return contractNumber;
    }

    /**
     * @param contractNumber the contractNumber to set
     */
    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * @return the mailRecipient
     */
    public boolean isMailRecipient() {
        return mailRecipient;
    }

    /**
     * @param mailRecipient the mailRecipient to set
     */
    public void setMailRecipient(boolean mailRecipient) {
        this.mailRecipient = mailRecipient;
    }

    /**
     * @return the primaryContact
     */
    public boolean isPrimaryContact() {
        return primaryContact;
    }

    /**
     * @param primaryContact the primaryContact to set
     */
    public void setPrimaryContact(boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    /**
     * @return the role
     */
    public RoleValueLabelBean getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(RoleValueLabelBean role) {
        this.role = role;
    }

    /**
     * @return the userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName the userFirstName to set
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @return the userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName the userLastName to set
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the lastUserWithSigningAuthority
     */
    public boolean isLastUserWithSigningAuthority() {
        return lastUserWithSigningAuthority;
    }

    /**
     * @param lastUserWithSigningAuthority the lastUserWithSigningAuthority to set
     */
    public void setLastUserWithSigningAuthority(boolean lastUserWithSigningAuthority) {
        this.lastUserWithSigningAuthority = lastUserWithSigningAuthority;
    }

    /**
     * @return the lastUserWithManageUsers
     */
    public boolean isLastUserWithManageUsers() {
        return lastUserWithManageUsers;
    }

    /**
     * @param lastUserWithManageUsers the lastUserWithManageUsers to set
     */
    public void setLastUserWithManageUsers(boolean lastUserWithManageUsers) {
        this.lastUserWithManageUsers = lastUserWithManageUsers;
    }

    /**
     * @return the lastUserWithReviewIWithdrawals
     */
    public boolean isLastUserWithReviewIWithdrawals() {
        return lastUserWithReviewIWithdrawals;
    }

    /**
     * @param lastUserWithReviewIWithdrawals the lastUserWithReviewIWithdrawals to set
     */
    public void setLastUserWithReviewIWithdrawals(boolean lastUserWithReviewIWithdrawals) {
        this.lastUserWithReviewIWithdrawals = lastUserWithReviewIWithdrawals;
    }

    /**
     * @return the lastUserWithSubmissionsAccess
     */
    public boolean isLastUserWithSubmissionsAccess() {
        return lastUserWithSubmissionsAccess;
    }

    /**
     * @param lastUserWithSubmissionsAccess the lastUserWithSubmissionsAccess to set
     */
    public void setLastUserWithSubmissionsAccess(boolean lastUserWithSubmissionsAccess) {
        this.lastUserWithSubmissionsAccess = lastUserWithSubmissionsAccess;
    }

    public String getTpaFirmID() {
        return tpaFirmID;
    }

    public void setTpaFirmID(String tpaFirmID) {
        this.tpaFirmID = tpaFirmID;
    }

    public String getTpaFirmName() {
        return tpaFirmName;
    }

    public void setTpaFirmName(String tpaFirmName) {
        this.tpaFirmName = tpaFirmName;
    }

    public boolean isTpaData() {
        return tpaData;
    }

    public void setTpaData(boolean tpaData) {
        this.tpaData = tpaData;
    }

    public String getReturnToScreen() {
        return returnToScreen;
    }

    public void setReturnToScreen(String returnToScreen) {
        this.returnToScreen = returnToScreen;
    }

    public CloneableForm getOriginalClonedForm() {
        return originalClonedForm;
    }

    public void setOriginalClonedForm(CloneableForm originalClonedForm) {
        this.originalClonedForm = originalClonedForm;
    }

    /**
     * @return the lastUserWithSigningAuthorityContracts
     */
    public String getLastUserWithSigningAuthorityContracts() {
        return lastUserWithSigningAuthorityContracts;
    }

    /**
     * @param lastUserWithSigningAuthorityContracts the lastUserWithSigningAuthorityContracts to set
     */
    public void setLastUserWithSigningAuthorityContracts(String lastUserWithSigningAuthorityContracts) {
        this.lastUserWithSigningAuthorityContracts = lastUserWithSigningAuthorityContracts;
    }

    /**
     * @return the lastUserWithReviewIWithdrawalsContracts
     */
    public String getLastUserWithReviewIWithdrawalsContracts() {
        return lastUserWithReviewIWithdrawalsContracts;
    }

    /**
     * @param lastUserWithReviewIWithdrawalsContracts the lastUserWithReviewIWithdrawalsContracts to set
     */
    public void setLastUserWithReviewIWithdrawalsContracts(String lastUserWithReviewIWithdrawalsContracts) {
        this.lastUserWithReviewIWithdrawalsContracts = lastUserWithReviewIWithdrawalsContracts;
    }

    /**
     * @return the lastUserWithReceiveILoansEmailAndTPAStaffPlan
     */
    public boolean isLastUserWithReceiveILoansEmailAndTPAStaffPlan() {
        return lastUserWithReceiveILoansEmailAndTPAStaffPlan;
    }

    /**
     * @param lastUserWithReceiveILoansEmailAndTPAStaffPlan the lastUserWithReceiveILoansEmailAndTPAStaffPlan to set
     */
    public void setLastUserWithReceiveILoansEmailAndTPAStaffPlan(boolean lastUserWithReceiveILoansEmailAndTPAStaffPlan) {
        this.lastUserWithReceiveILoansEmailAndTPAStaffPlan = lastUserWithReceiveILoansEmailAndTPAStaffPlan;
    }

    /**
     * @return the contactType
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * @param contactType the contactType to set
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    /**
     * @return the firmContractsHaveDirectDebitAccounts
     */
    public boolean isFirmContractsHaveDirectDebitAccounts() {
        return firmContractsHaveDirectDebitAccounts;
    }

    /**
     * @param firmContractsHaveDirectDebitAccounts the firmContractsHaveDirectDebitAccounts to set
     */
    public void setFirmContractsHaveDirectDebitAccounts(boolean firmContractsHaveDirectDebitAccounts) {
        this.firmContractsHaveDirectDebitAccounts = firmContractsHaveDirectDebitAccounts;
    }

    /**
     * returns true if the it is the last user with review loans
     * @return boolean
     */
	public boolean isLastUserWithReviewLoans() {
		return lastUserWithReviewLoans;
	}

	/**
	 * can be used to set the flag if it is last user with review loans
	 * @param lastUserWithReviewLoans
	 */
	public void setLastUserWithReviewLoans(boolean lastUserWithReviewLoans) {
		this.lastUserWithReviewLoans = lastUserWithReviewLoans;
	}

	/**
	 * @return the lastUserWithReviewLoansContracts
	 */
	public String getLastUserWithReviewLoansContracts() {
		return lastUserWithReviewLoansContracts;
	}

	/**
	 * @param lastUserWithReviewLoansContracts the lastUserWithReviewLoansContracts to set
	 */
	public void setLastUserWithReviewLoansContracts(
			String lastUserWithReviewLoansContracts) {
		this.lastUserWithReviewLoansContracts = lastUserWithReviewLoansContracts;
	}

	/**
	 * @return the lastClientUserWithReviewIWithdrawals
	 */
	public boolean isLastClientUserWithReviewIWithdrawals() {
		return lastClientUserWithReviewIWithdrawals;
	}

	/**
	 * @param lastClientUserWithReviewIWithdrawals the lastClientUserWithReviewIWithdrawals to set
	 */
	public void setLastClientUserWithReviewIWithdrawals(
			boolean lastClientUserWithReviewIWithdrawals) {
		this.lastClientUserWithReviewIWithdrawals = lastClientUserWithReviewIWithdrawals;
	}

	/**
	 * @return the lastClientUserWithReviewLoans
	 */
	public boolean isLastClientUserWithReviewLoans() {
		return lastClientUserWithReviewLoans;
	}

	/**
	 * @param lastClientUserWithReviewLoans the lastClientUserWithReviewLoans to set
	 */
	public void setLastClientUserWithReviewLoans(
			boolean lastClientUserWithReviewLoans) {
		this.lastClientUserWithReviewLoans = lastClientUserWithReviewLoans;
	}



}
