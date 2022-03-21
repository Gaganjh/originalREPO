package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 */
public class TpaFirm extends CloneableAutoForm {

	private Integer id;

	private String name;

	private boolean removed;

	private boolean persisted;
    
    private boolean newFirm;

    private boolean hidden;

	private List<TPAUserContractAccess> contractAccesses;
    
    private List<Integer> contractIds;
	
	private String actionLabel;

    private boolean lastUserWithManageUsers;
    private boolean lastUserWithReceiveILoansEmail;
    private boolean lastRegisteredUser;
    private boolean lastUserWithReceiveILoansEmailAndTPAStaffPlan;
    private String lastUserWithSigningAuthorityContracts;
    private String lastUserWithReviewIWithdrawalsContracts;
    private boolean firmContractsHaveDirectDebitAccounts;
    private boolean bundledGaIndicator = false;

	public static final String FIELD_ID = "id";

	public static final String FIELD_NAME = "name";

	public static final String FIELD_CONTRACT_ACCESS = "contractAccess";

	private static final Map ACTION_LABEL_MAP = new HashMap();
	
	private boolean showTpaFirmWithdrawals = false;

	private Boolean showReportingSection = true;
	private Boolean showPlanServicesSection = true;
	private Boolean showClientServicesSection = true;
	private Boolean showPayrollPathSection = true;
	private Boolean showSubmissionsSection = true;
	private Boolean showiWithdrawalsSection = true;
	private Boolean showCensusManagementSection = true;
	private Boolean showEverything = true;
	private Boolean showTPAUserList = true;
	
	private Boolean enablePlanData = true;
	private Boolean enableInitiateWithdrawals = true;
	private Boolean enableSigningAuthority = true;
	private Boolean enableReviewWithdrawals = true;
	
	/*Added this attribute for FeeAccess404A5*/
	private Boolean enableFeeAccess404A5 = true;
	private Boolean enableTPAUserList = true;
	private Boolean enableDirectDebit = true;
	private Boolean enableDirectDebitAccounts = true;
	private List<Long> clientUsersWithReviewWithdrawalPermission;
	private List<Long> clientUsersWithSigningAuthorityPermission;
	private String[] selectedTPAUsers = new String[0];
	private List<UserInfo> TPAUsers = null;

	public static final String FIELD_SELECTED_TPA_USERS = "selectedTPAUsers";

	//Loans related permissions
	private Boolean showLoansSection = true;
    private String lastUserWithReviewLoansContracts;	
    private List<Long> clientUsersWithReviewLoansPermission;
    private Boolean enableInitiateLoans = true;
    private Boolean enableReviewLoans = true;
    private Boolean enableViewAllLoans = true;
	
	/*
	 * Maps the button label to the corresponding action.
	 */
	
	public static final String NO_TPA_USER = "none";
	public static final String BUTTON_LABEL_SAVE = "save";

	public static final String BUTTON_LABEL_CANCEL = "cancel";

	private static final String SAVE_ACTION = "save";

	static {
		ACTION_LABEL_MAP.put(BUTTON_LABEL_SAVE, SAVE_ACTION);
		ACTION_LABEL_MAP.put(BUTTON_LABEL_CANCEL, "cancel");
	}

	/**
	 * Constructor.
	 */
	public TpaFirm() {
		super();
		contractAccesses = new ArrayList();
	}

	public TpaFirm(Integer id, String name, Integer registeredUserCount) {
		super();
		this.id = id;
		this.name = name;
		contractAccesses = new ArrayList();
	}

	public boolean isSaveAction() {
		return SAVE_ACTION.equals(getAction());
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Object clone() {
		TpaFirm myClone = (TpaFirm) super.clone();
		myClone.contractAccesses = new ArrayList();

		for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
			TPAUserContractAccess access = (TPAUserContractAccess) it.next();
			myClone.contractAccesses.add((TPAUserContractAccess)access.clone());
		}
		return myClone;
	}

	public boolean isPersisted() {
		return persisted;
	}

	public void setPersisted(boolean fromDatabase) {
		this.persisted = fromDatabase;
	}

	/**
	 * @return Returns the removed.
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed
	 *            The removed to set.
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof TpaFirm) {
			TpaFirm firm = (TpaFirm) obj;
			return ObjectUtils.equals(id, firm.id);
		}
		return false;
	}

	public List<TPAUserContractAccess> getContractAccesses() {
		return contractAccesses;
	}

	public TPAUserContractAccess getContractAccess(int index) {
		while (index > contractAccesses.size() - 1) {
			contractAccesses.add(new TPAUserContractAccess());
		}
		return (TPAUserContractAccess) contractAccesses.get(index);
	}

	public void setContractAccesses(List contractAccesses) {
		this.contractAccesses = contractAccesses;
	}

	/**
	 * @return Returns the actionLabel.
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * @param actionLabel
	 *            The actionLabel to set.
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}

	public void clear(HttpServletRequest request) {
		super.reset( request);
		super.clear( request);
		contractAccesses.clear();
		id = null;
		name = null;
	}

	public void reset( HttpServletRequest request) {
		super.reset( request);
		actionLabel = null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("name [").append(name).append(
				"] id [").append(id).append("] removed [").append(removed)
				.append("]");
		return sb.toString();
	}

	/**
	 * Produces a flattened map filled with values of the form. The key of the
	 * map is a Struts compatible expression of the field, the value is the
	 * actual value. The value can be null.
	 * 
	 * @return A flattened map filled with values of the form.
	 */
	private Map getFormAsMap() {
		Map formMap = new HashMap();

		for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
			TPAUserContractAccess contractAccess = (TPAUserContractAccess) it.next();

			String fieldPrefix = new StringBuffer(FIELD_CONTRACT_ACCESS)
					.append("[").append(contractAccess.getContractNumber())
					.append("].").toString();
			formMap.putAll(contractAccess.getFormAsMap(fieldPrefix));
		}
		StringBuffer val = new StringBuffer();
		Set<String> sortedUsers = new TreeSet<String>();
		for ( String s : getSelectedTPAUsers()) {
			if (!StringUtils.equals(s, "none")) {
				sortedUsers.add(s);
			}
		}
		for (String s : sortedUsers) {
			val.append(s).append(",");
		}
		formMap.put(FIELD_SELECTED_TPA_USERS, val.toString());
		
		return formMap;
	}

	/**
	 * Compute the changes in this form using the stored form object. Changes
	 * are stored in the FormChanges object. The FormChanges object remembers
	 * additions, removals, and modifications.
	 * 
	 * @return A FormChanges object that captures all the changes made in the
	 *         form.
	 */
	public FormChanges getChanges() {
		FormChanges formChanges = new FormChanges();

		if (getClonedForm() != null) {
			/*
			 * Obtains a flattened map for both forms.
			 */
			Map newFormMap = getFormAsMap();
			Map oldFormMap = ((TpaFirm) getClonedForm()).getFormAsMap();
			formChanges.computeChanges(newFormMap, oldFormMap);
		}
		return formChanges;
	}

	public boolean isShowTpaFirmWithdrawals() {
		return showTpaFirmWithdrawals;
	}

	public void setShowTpaFirmWithdrawals(boolean showTpaFirmWithdrawals) {
		this.showTpaFirmWithdrawals = showTpaFirmWithdrawals;
	}

	public Boolean getShowReportingSection() {
		return showReportingSection;
	}

	public void setShowReportingSection(Boolean showReporting) {
		this.showReportingSection = showReporting;
	}

	public Boolean getShowCensusManagementSection() {
		return showCensusManagementSection;
	}

	public void setShowCensusManagementSection(Boolean showCensusManagementSection) {
		this.showCensusManagementSection = showCensusManagementSection;
	}

	public Boolean getShowClientServicesSection() {
		return showClientServicesSection;
	}

	public void setShowClientServicesSection(Boolean showClientServicesSection) {
		this.showClientServicesSection = showClientServicesSection;
	}

	public Boolean getShowiWithdrawalsSection() {
		return showiWithdrawalsSection;
	}

	public void setShowiWithdrawalsSection(Boolean showiWithdrawalsSection) {
		this.showiWithdrawalsSection = showiWithdrawalsSection;
	}

	public Boolean getShowPayrollPathSection() {
		return showPayrollPathSection;
	}

	public void setShowPayrollPathSection(Boolean showPayrollPathSection) {
		this.showPayrollPathSection = showPayrollPathSection;
	}

	public Boolean getShowPlanServicesSection() {
		return showPlanServicesSection;
	}

	public void setShowPlanServicesSection(Boolean showPlanServicesSection) {
		this.showPlanServicesSection = showPlanServicesSection;
	}

	public Boolean getShowSubmissionsSection() {
		return showSubmissionsSection;
	}

	public void setShowSubmissionsSection(Boolean showSubmissionsSection) {
		this.showSubmissionsSection = showSubmissionsSection;
	}

	public Boolean getShowEverything() {
		return showEverything;
	}

	public void setShowEverything(Boolean showEverything) {
		this.showEverything = showEverything;
	}

	public Boolean getEnableSigningAuthority() {
		return enableSigningAuthority;
	}

	public void setEnableSigningAuthority(Boolean enableSigningAuthority) {
		this.enableSigningAuthority = enableSigningAuthority;
	}

	public Boolean getEnableInitiateWithdrawals() {
		return enableInitiateWithdrawals;
	}

	public void setEnableInitiateWithdrawals(Boolean enableInitiateWithdrawals) {
		this.enableInitiateWithdrawals = enableInitiateWithdrawals;
	}

	public Boolean getEnablePlanData() {
		return enablePlanData;
	}

	public void setEnablePlanData(Boolean enablePlanData) {
		this.enablePlanData = enablePlanData;
	}

	public Boolean getEnableReviewWithdrawals() {
		return enableReviewWithdrawals;
	}

	public void setEnableReviewWithdrawals(Boolean enableReviewWithdrawals) {
		this.enableReviewWithdrawals = enableReviewWithdrawals;
	}
	
	/*get the value for FeeAccess404A5 */
	public Boolean getEnableFeeAccess404A5() {
		return enableFeeAccess404A5;
	}
    /*Set the value for FeeAccess404A5 */
	public void setEnableFeeAccess404A5(Boolean enableFeeAccess404A5) {
		this.enableFeeAccess404A5 = enableFeeAccess404A5;
	}

	

	

	public Boolean getShowTPAUserList() {
		return showTPAUserList;
	}

	public void setShowTPAUserList(Boolean showTPAUserList) {
		this.showTPAUserList = showTPAUserList;
	}

	public Boolean getEnableTPAUserList() {
		return enableTPAUserList;
	}

	public void setEnableTPAUserList(Boolean enableTPAUserList) {
		this.enableTPAUserList = enableTPAUserList;
	}

	public Boolean getEnableDirectDebit() {
		return enableDirectDebit;
	}

	public void setEnableDirectDebit(Boolean enableDirectDebit) {
		this.enableDirectDebit = enableDirectDebit;
	}

	public Boolean getEnableDirectDebitAccounts() {
		return enableDirectDebitAccounts;
	}

	public void setEnableDirectDebitAccounts(Boolean enableDirectDebitAccounts) {
		this.enableDirectDebitAccounts = enableDirectDebitAccounts;
	}

	public String[] getSelectedTPAUsers() {
		return selectedTPAUsers;
	}

	public void setSelectedTPAUsers(String[] selectedTPAUsers) {
		this.selectedTPAUsers = selectedTPAUsers;
	}

	public List<UserInfo> getTPAUsers() {
		return TPAUsers;
	}

	public void setTPAUsers(List<UserInfo> users) {
		TPAUsers = users;
	}

	public String getSelectedTPAUsersAsString() {
        List<UserInfo> users = getSelectedTPAUsersAsList();
        StringBuffer sb = new StringBuffer();
        for (UserInfo user : users ){
            sb.append(user.getProfileId()).append(",");
        }
        return sb.toString();
	}
    public List<UserInfo> getSelectedTPAUsersAsList() {
        List<UserInfo> returnUsers = new ArrayList<UserInfo>();
        if (getTPAUsers() != null) {
            for (UserInfo user : getTPAUsers()) {
                for (int i = 0; i < selectedTPAUsers.length; i++) {
                    if (selectedTPAUsers[i].equals(TpaFirm.NO_TPA_USER)) {
                        continue;
                    }
                    if (String.valueOf(user.getProfileId()).equals(
                    		selectedTPAUsers[i])) {
                    	returnUsers.add(user);
                        break;
                    }
                }
            }
        }
        return returnUsers;
    }

    public List<Long> getClientUsersWithReviewWithdrawalPermissionList() {
        return clientUsersWithReviewWithdrawalPermission;
    }

    public Boolean getClientUsersWithReviewWithdrawalPermission() {
        return clientUsersWithReviewWithdrawalPermission != null && !clientUsersWithReviewWithdrawalPermission.isEmpty();
    }

	public void setClientUsersWithReviewWithdrawalPermissionList(
			List<Long> clientUsersWithReviewWithdrawalPermission) {
		this.clientUsersWithReviewWithdrawalPermission = clientUsersWithReviewWithdrawalPermission;
	}

    public List<Long> getClientUsersWithApproveWithdrawalPermissionList() {
        return clientUsersWithSigningAuthorityPermission;
    }

    public Boolean getClientUsersWithSigningAuthorityPermission() {
        return clientUsersWithSigningAuthorityPermission != null && !clientUsersWithSigningAuthorityPermission.isEmpty();
    }

	public void setClientUsersWithApproveWithdrawalPermissionList(
			List<Long> clientUsersWithApproveWithdrawalPermission) {
		this.clientUsersWithSigningAuthorityPermission = clientUsersWithApproveWithdrawalPermission;
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
     * @return the contractIds
     */
    public List<Integer> getContractIds() {
        return contractIds;
    }

    /**
     * @param contractIds the contractIds to set
     */
    public void setContractIds(List<Integer> contractIds) {
        this.contractIds = contractIds;
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
     * @return the newFirm
     */
    public boolean isNewFirm() {
        return newFirm;
    }

    /**
     * @param newFirm the newFirm to set
     */
    public void setNewFirm(boolean newFirm) {
        this.newFirm = newFirm;
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
     * @return the lastUserWithReceiveILoansEmail
     */
    public boolean isLastUserWithReceiveILoansEmail() {
        return lastUserWithReceiveILoansEmail;
    }

    /**
     * @param lastUserWithReceiveILoansEmail the lastUserWithReceiveILoansEmail to set
     */
    public void setLastUserWithReceiveILoansEmail(boolean lastUserWithReceiveILoansEmail) {
        this.lastUserWithReceiveILoansEmail = lastUserWithReceiveILoansEmail;
    }

    /**
     * @return the lastRegisteredUser
     */
    public boolean isLastRegisteredUser() {
        return lastRegisteredUser;
    }

    /**
     * @param lastRegisteredUser the lastRegisteredUser to set
     */
    public void setLastRegisteredUser(boolean lastRegisteredUser) {
        this.lastRegisteredUser = lastRegisteredUser;
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param hidden the hidden to set
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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
	 * @return the showLoansSection
	 */
	public Boolean getShowLoansSection() {
		return showLoansSection;
	}

	/**
	 * @param showLoansSection the showLoansSection to set
	 */
	public void setShowLoansSection(Boolean showLoansSection) {
		this.showLoansSection = showLoansSection;
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
	 * returns the list of users who have review loans permission
	 * @return
	 */
    public List<Long> getClientUsersWithReviewLoansPermissionList() {
        return clientUsersWithReviewLoansPermission;
    }

    /**
     * returns true if there are any client users with review loans permission
     * @return
     */
    public Boolean getClientUsersWithReviewLoansPermission() {
        return clientUsersWithReviewLoansPermission != null && !clientUsersWithReviewLoansPermission.isEmpty();
    }

    /**
     * sets list of client users with review loans permissions
     * @param clientUsersWithReviewWithdrawalPermission
     */
	public void setClientUsersWithReviewLoansPermissionList(
			List<Long> clientUsersWithReviewLoansPermission) {
		this.clientUsersWithReviewLoansPermission = clientUsersWithReviewLoansPermission;
	}
	

	/**
	 * @return the enableInitiateLoans
	 */
	public Boolean getEnableInitiateLoans() {
		return enableInitiateLoans;
	}

	/**
	 * @param enableInitiateLoans the enableInitiateLoans to set
	 */
	public void setEnableInitiateLoans(Boolean enableInitiateLoans) {
		this.enableInitiateLoans = enableInitiateLoans;
	}

	/**
	 * @return the enableReviewLoans
	 */
	public Boolean getEnableReviewLoans() {
		return enableReviewLoans;
	}

	/**
	 * @param enableReviewLoans the enableReviewLoans to set
	 */
	public void setEnableReviewLoans(Boolean enableReviewLoans) {
		this.enableReviewLoans = enableReviewLoans;
	}

	/**
	 * @return the enableViewAllLoans
	 */
	public Boolean getEnableViewAllLoans() {
		return enableViewAllLoans;
	}

	/**
	 * @param enableViewAllLoans the enableViewAllLoans to set
	 */
	public void setEnableViewAllLoans(Boolean enableViewAllLoans) {
		this.enableViewAllLoans = enableViewAllLoans;
	}

	/**
	 * @return the bundledGaIndicator
	 */
	public boolean isBundledGaIndicator() {
		return bundledGaIndicator;
	}

	/**
	 * @param bundledGaIndicator the bundledGaIndicator to set
	 */
	public void setBundledGaIndicator(boolean bundledGaIndicator) {
		this.bundledGaIndicator = bundledGaIndicator;
	}

}