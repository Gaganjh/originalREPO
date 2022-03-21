package com.manulife.pension.ps.web.profiles;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.NestableRuntimeException;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.service.security.role.type.RoleType;

/**
 * A view object contain contract access page elements for client user profile
 * 
 * @author Steven Wang
 */
public class ClientUserContractAccess extends BaseContractAccess {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_PLANSPONSORSITE_ROLE = "planSponsorSiteRole";

    public static final String FIELD_ROLE_TYPE = "roleType";

    public static final String FIELD_PRIMARY_CONTACT = "primaryContact";

    public static final String FIELD_MAIL_RECEPIENT = "mailRecepient";
    
    public static final String FIELD_TRUSTEE_MAIL_RECEPIENT = "trusteeMailRecepient";
    
    public static final String FIELD_STATEMENT_INDICATOR = "statementIndicator";
    
    public static final String FIELD_SIGNATURE_RECEIVED_TRUSTEE = "signatureReceivedTrustee";
    
    public static final String FIELD_SIGNATURE_RECEIVED_AUTH_SIGNER = "signatureReceivedAuthSigner";

    public static final String FIELD_MANAGE_USERS = "manageUsers";

    public static final String FIELD_SELECTED_ACCESS = "selectedAccess";

    public static final String FIELD_USER_PERMISSIONS = "userPermissions.";
    
    public static final String FIELD_ICC_DESIGNATE = "iccDesignate";
    
    public static final String FIELD_SEND_SERVICE_DESIGNATE = "sendServiceDesignate";
    
    /**
     * Contract business converted indicator. <code>true</code> indicates contract after business
     * converted.
     */
    private boolean cbcIndicator;

    /**
     * contract number
     */
    private String contractName;

    /**
     * Current profile user role for current contract
     * 
     */
    private RoleValueLabelBean planSponsorSiteRole = new RoleValueLabelBean(AccessLevelHelper.NO_ACCESS,AccessLevelHelper.NO_ACCESS); 

    private String roleType;
    
    /**
     * primary contact just available for business converted contract
     */
    private boolean primaryContact;

    /**
     * mail receipent just available for business converted contract
     */
    private boolean mailRecepient;
    
    private boolean trusteeMailRecepient;
    
    private boolean statementIndicator;
    
    private boolean signatureReceivedTrustee;
    
    private boolean signatureReceivedAuthSigner;
    
    private boolean iccDesignate;
    
    private boolean sendServiceDesignate;

    /**
     * flag indicates this contract is new added contract with current profile
     */
    private boolean newContract = false;

    /**
     * flag indicates if this is the last client user with manage user permission for the contract
     */
    private boolean lastUserWithManageUsers;

    /**
     * flag indicates if this is the last client user with review i:withdrawals permission for the contract
     */
    private boolean lastUserWithReviewIWithdrawals;

    /**
     * flag indicates if this is the last client user with review i:withdrawals permission for the contract
     */
    private boolean lastClientUserWithReviewIWithdrawals;

    /**
     * flag indicates if this is the last client user with review i:withdrawals permission for the contract
     */
    private boolean lastClientUserWithReviewLoans;
    
    /**
     * flag indicates if this is the last client user with approve i:withdrawals permission for the contract
     */
    private boolean lastUserWithSigningAuthority;

    /**
     * flag indicates if this is the last client user with approve i:withdrawals permission for the contract
     */
    private boolean lastUserWithSubmissionsAccess;

    /**
     * When the client user role is changed the cloned permissions and preferences are reset to the new role defaults.
     * A copy of the originals are required to track some warning & error messages.
     */
    private UserPermissions originalUserPermissions = new UserPermissions();
    
    private long primaryContactProfileId;
    
    private String primaryContactName;
    
    private long iccDesignateProfileId;
    
    private String iccDesignateName;
    
    private long sendServiceDesignateProfileId;
    
    private String sendServiceDesignateName;
    
	private long mailRecipientProfileId;
    
    private String mailRecipientName;
    
    private long trusteeMailRecipientProfileId;
    
    private String trusteeMailRecipientName;
    
    private long participantStatementConsultantProfileId;
    
    private String participantStatementConsultantName;
    
    private boolean changePermissionsClicked;
    
    /**
     * flag that indicates that this is the last user with review loans permission
     */
    private boolean lastUserWithReviewLoansPermission;
    
    /**
     * List of roles that the logged in user can select for the managed user
     */
    private List<LabelValueBean> roleList;
    
    private boolean iccDesignateEligible;
    
    private boolean sendServiceDesignateEligible;
    
    public boolean isIccDesignateEligible() {
		return iccDesignateEligible;
	}

	public void setIccDesignateEligible(boolean iccDesignateEligible) {
		this.iccDesignateEligible = iccDesignateEligible;
	}

	public boolean isSendServiceDesignateEligible() {
		return sendServiceDesignateEligible;
	}

	public void setSendServiceDesignateEligible(boolean sendServiceDesignateEligible) {
		this.sendServiceDesignateEligible = sendServiceDesignateEligible;
	}

	/**
     * Constructor.
     */
    public ClientUserContractAccess() {
        super();
    }

    /**
     * check contract is business converted
     * 
     * @return a boolean. true - A business converted contract. false - A non business converted
     *         contract
     */
    public boolean isCbcIndicator() {
        return cbcIndicator;
    }

    /**
     * Set business converted indicator
     * 
     * @param cbcIndicator
     */
    public void setCbcIndicator(boolean cbcIndicator) {
        this.cbcIndicator = cbcIndicator;
    }

    /**
     * Get mail receipent. Yes/No for business converted contract. Null for non business converted
     * contract.
     * @return
     */
    public boolean isMailRecepient() {
        return mailRecepient;
    }
    /**
     * Set mail recepient
     * @param mailRecepient
     */
    public void setMailRecepient(boolean mailRecepient) {
        this.mailRecepient = mailRecepient;
    }
    /**
     * Get profile role on the contract
     * @return
     */
    public RoleValueLabelBean getPlanSponsorSiteRole() {
        return planSponsorSiteRole;
    }
    /**
     * Set profile role the contract
     * @param planSponsorSiteRole
     */
    public void setPlanSponsorSiteRole(RoleValueLabelBean planSponsorSiteRole) {
        this.planSponsorSiteRole = planSponsorSiteRole;
    }
    /**
     * Get primary contact for buinsess converted contract
     * Null will be return for no business converted contract
     * @return
     */
    public boolean isPrimaryContact() {
        return primaryContact;
    }
    /**
     * Set primary contact
     * @param primaryContact
     */
    public void setPrimaryContact(boolean primaryContact) {
        this.primaryContact = primaryContact;
    }
    public boolean isIccDesignate() {
		return iccDesignate;
	}

	public void setIccDesignate(boolean iccDesignate) {
		this.iccDesignate = iccDesignate;
	}

	public boolean isSendServiceDesignate() {
		return sendServiceDesignate;
	}

	public void setSendServiceDesignate(boolean sendServiceDesignate) {
		this.sendServiceDesignate = sendServiceDesignate;
	}

	/**
     * Set selected access for non business converted contract
     * @return
     */
    public boolean isSelectedAccess() {
        return getUserPermissions().isSelectedAccess();
    }
    /**
     * Set selected access
     * @param selectedAccess
     */
    public void setSelectedAccess(boolean selectedAccess) {
        getUserPermissions().setSelectedAccess(selectedAccess);
    }
    /**
     * Get permissions view object for profile on the contract
     * @return A user permission view object
     */
    public UserPermissions getUserPermissions() {
        return userPermissions;
    }
    /**
     * Set permission view object for profile on the contract
     * @param userPermissions
     */
    public void setUserPermissions(UserPermissions userPermissions) {
        this.userPermissions = userPermissions;
    }
    /**
     * Clone object for change tracker function and highlight if changed function
     */
    public Object clone() {
        try {
            ClientUserContractAccess clonedObject = (ClientUserContractAccess) super.clone();
            clonedObject.userPermissions = (UserPermissions) userPermissions.clone();
            clonedObject.planSponsorSiteRole = (RoleValueLabelBean) planSponsorSiteRole.clone();
            /*
             * Add all direct debit accounts into the clone object.
             */
            return clonedObject;
        } catch (CloneNotSupportedException e) {
            // this should not happen because this object implements Cloneable
            throw new NestableRuntimeException(e);
        }
    }
    
    /**
     * Get object attributes in a flat map for checking entity is changed
     */
    public Map getFormAsMap(String fieldPrefix) {
        Map formMap = super.getFormAsMap(fieldPrefix);

        String fieldId = fieldPrefix + FIELD_PLANSPONSORSITE_ROLE;
        formMap.put(fieldId, planSponsorSiteRole.getValue());

        fieldId = fieldPrefix + FIELD_ROLE_TYPE;
        formMap.put(fieldId, roleType);

        fieldId = fieldPrefix + FIELD_PRIMARY_CONTACT;
        formMap.put(fieldId, isPrimaryContact());

        fieldId = fieldPrefix + FIELD_MAIL_RECEPIENT;
        formMap.put(fieldId, isMailRecepient());
        
        fieldId = fieldPrefix + FIELD_STATEMENT_INDICATOR;
        formMap.put(fieldId, isStatementIndicator());
        
        fieldId = fieldPrefix + FIELD_TRUSTEE_MAIL_RECEPIENT;
        formMap.put(fieldId, isTrusteeMailRecepient());
        
        fieldId = fieldPrefix + FIELD_SIGNATURE_RECEIVED_TRUSTEE;
        formMap.put(fieldId, isSignatureReceivedTrustee());
        
        fieldId = fieldPrefix + FIELD_SIGNATURE_RECEIVED_AUTH_SIGNER;
        formMap.put(fieldId, isSignatureReceivedAuthSigner());
        
        fieldId = fieldPrefix + FIELD_ICC_DESIGNATE;
        formMap.put(fieldId, isIccDesignate());
        
        fieldId = fieldPrefix + FIELD_SEND_SERVICE_DESIGNATE;
        formMap.put(fieldId, isSendServiceDesignate());

        formMap.putAll(userPermissions.getFormAsMap(fieldPrefix + FIELD_USER_PERMISSIONS));

        return formMap;
    }
    /**
     * Getcontract name;
     * @return
     */
    public String getContractName() {
        return contractName;
    }
    /**
     * Set contract name
     * @param contractName
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    /**
     * Get manage users Yes/No for non business converted contract
     * Null for business converted contract
     * @return
     */
    public boolean isManageUsers() {
        return getUserPermissions().isManageUsers(); 
    }
    /**
     * Set manage users
     * @param manageUsers
     */
    public void setManageUsers(boolean manageUsers) {
        getUserPermissions().setManageUsers(manageUsers);
    }
    /**
     * Check contract view is a new contract added by user 
     * @return
     */
    public boolean isNewContract() {
        return newContract;
    }
    /**
     * Set new contract indicator
     * @param newContract
     */
    public void setNewContract(boolean newContract) {
        this.newContract = newContract;
    }

    /**
     * @return the mailRecipientName
     */
    public String getMailRecipientName() {
        return mailRecipientName;
    }

    /**
     * @param mailRecipientName the mailRecipientName to set
     */
    public void setMailRecipientName(String mailRecipientName) {
        this.mailRecipientName = mailRecipientName;
    }

    /**
     * @return the mailRecipientProfileId
     */
    public long getMailRecipientProfileId() {
        return mailRecipientProfileId;
    }

    /**
     * @param mailRecipientProfileId the mailRecipientProfileId to set
     */
    public void setMailRecipientProfileId(long mailRecipientProfileId) {
        this.mailRecipientProfileId = mailRecipientProfileId;
    }

    /**
     * @return the primaryContactName
     */
    public String getPrimaryContactName() {
        return primaryContactName;
    }

    /**
     * @param primaryContactName the primaryContactName to set
     */
    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    /**
     * @return the primaryContactProfileId
     */
    public long getPrimaryContactProfileId() {
        return primaryContactProfileId;
    }

    /**
     * @param primaryContactProfileId the primaryContactProfileId to set
     */
    public void setPrimaryContactProfileId(long primaryContactProfileId) {
        this.primaryContactProfileId = primaryContactProfileId;
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

    /**
     * @return the roleList
     */
    public List<LabelValueBean> getRoleList() {
        return roleList;
    }

    /**
     * @param roleList the roleList to set
     */
    public void setRoleList(List<LabelValueBean> roleList) {
        this.roleList = roleList;
    }

    /**
     * @return the changePermissionsClicked
     */
    public boolean isChangePermissionsClicked() {
        return changePermissionsClicked;
    }

    /**
     * @param changePermissionsClicked the changePermissionsClicked to set
     */
    public void setChangePermissionsClicked(boolean changePermissionsClicked) {
        this.changePermissionsClicked = changePermissionsClicked;
    }

    /**
     * @return the roleType
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * @return the roleType displayName
     */
    public String getRoleTypeDisplayName() {
        RoleType roleTypeObj = RoleType.getRoleTypeById(roleType); 
        return roleTypeObj == null ? null : roleTypeObj.getDisplayName();
    }

    /**
     * @param roleType the roleType to set
     */
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    /**
     * @return the originalUserPermissions
     */
    public UserPermissions getOriginalUserPermissions() {
        return originalUserPermissions;
    }

    /**
     * @param originalUserPermissions the originalUserPermissions to set
     */
    public void setOriginalUserPermissions(UserPermissions originalUserPermissions) {
        this.originalUserPermissions = originalUserPermissions;
    }

    /**
     * returns true if the user is the last one with review loans permission
     * @return boolean
     */
	public boolean isLastUserWithReviewLoansPermission() {
		return lastUserWithReviewLoansPermission;
	}

    /**
     * sets to true if the user is the last one with review loans permission
     * @param lastUserWithReviewLoansPermission
     */	
	public void setLastUserWithReviewLoansPermission(
			boolean lastUserWithReviewLoansPermission) {
		this.lastUserWithReviewLoansPermission = lastUserWithReviewLoansPermission;
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

	/**
	 * 
	 * @return
	 */
	public boolean isTrusteeMailRecepient() {
		return trusteeMailRecepient;
	}

	/**
	 * 
	 * @param trusteeMailRecepient
	 */
	public void setTrusteeMailRecepient(boolean trusteeMailRecepient) {
		this.trusteeMailRecepient = trusteeMailRecepient;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isStatementIndicator() {
		return statementIndicator;
	}

	/**
	 * 
	 * @param statementIndicator
	 */
	public void setStatementIndicator(boolean statementIndicator) {
		this.statementIndicator = statementIndicator;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSignatureReceivedTrustee() {
		return signatureReceivedTrustee;
	}

	/**
	 * 
	 * @param signatureReceivedTrustee
	 */
	public void setSignatureReceivedTrustee(boolean signatureReceivedTrustee) {
		this.signatureReceivedTrustee = signatureReceivedTrustee;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSignatureReceivedAuthSigner() {
		return signatureReceivedAuthSigner;
	}

	/**
	 * 
	 * @param signatureReceivedAuthSigner
	 */
	public void setSignatureReceivedAuthSigner(boolean signatureReceivedAuthSigner) {
		this.signatureReceivedAuthSigner = signatureReceivedAuthSigner;
	}

	/**
	 * 
	 * @return
	 */
	public long getTrusteeMailRecipientProfileId() {
		return trusteeMailRecipientProfileId;
	}

	/**
	 * 
	 * @param trusteeMailRecipientProfileId
	 */
	public void setTrusteeMailRecipientProfileId(
			long trusteeMailRecipientProfileId) {
		this.trusteeMailRecipientProfileId = trusteeMailRecipientProfileId;
	}

	/**
	 * 	
	 * @return
	 */
	public String getTrusteeMailRecipientName() {
		return trusteeMailRecipientName;
	}

	/**
	 * 
	 * @param trusteeMailRecipientName
	 */
	public void setTrusteeMailRecipientName(String trusteeMailRecipientName) {
		this.trusteeMailRecipientName = trusteeMailRecipientName;
	}

	/**
	 * 
	 * @return
	 */
	public long getParticipantStatementConsultantProfileId() {
		return participantStatementConsultantProfileId;
	}

	/**
	 * 
	 * @param participantStatementConsultantProfileId
	 */
	public void setParticipantStatementConsultantProfileId(
			long participantStatementConsultantProfileId) {
		this.participantStatementConsultantProfileId = participantStatementConsultantProfileId;
	}

	/**
	 * 
	 * @return
	 */
	public String getParticipantStatementConsultantName() {
		return participantStatementConsultantName;
	}

	/**
	 * 
	 * @param participantStatementConsultantName
	 */
	public void setParticipantStatementConsultantName(
			String participantStatementConsultantName) {
		this.participantStatementConsultantName = participantStatementConsultantName;
	}
	
	public long getIccDesignateProfileId() {
		return iccDesignateProfileId;
	}

	public void setIccDesignateProfileId(long iccDesignateProfileId) {
		this.iccDesignateProfileId = iccDesignateProfileId;
	}

	public String getIccDesignateName() {
		return iccDesignateName;
	}

	public void setIccDesignateName(String iccDesignateName) {
		this.iccDesignateName = iccDesignateName;
	}

	public long getSendServiceDesignateProfileId() {
		return sendServiceDesignateProfileId;
	}

	public void setSendServiceDesignateProfileId(long sendServiceDesignateProfileId) {
		this.sendServiceDesignateProfileId = sendServiceDesignateProfileId;
	}

	public String getSendServiceDesignateName() {
		return sendServiceDesignateName;
	}

	public void setSendServiceDesignateName(String sendServiceDesignateName) {
		this.sendServiceDesignateName = sendServiceDesignateName;
	}

}
