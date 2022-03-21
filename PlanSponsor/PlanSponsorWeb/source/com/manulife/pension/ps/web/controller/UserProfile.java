package com.manulife.pension.ps.web.controller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.NoticeServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.SuperCAR;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.log.LogUtility;
 
/**
 * This class is the User class that will created by the authentication service
 * and will be put into the session.
 * 
 * @author Ilker Celikyilmaz
 */
public class UserProfile extends BaseUserProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Access404a5 NO_ACCESS_404A5;
    
    static {
        
        final Set<Facility> emptySet = Collections.unmodifiableSet(EnumSet.noneOf(Facility.class));
        NO_ACCESS_404A5 =
                new Access404a5() {
                    private static final long serialVersionUID = 1L;
                    public Qualification getAccess(Facility facility) { return null; }
                    public Set<Facility> getAccessibleFacilities() { return emptySet; }
                    public Exception getException(Facility facility) { return null; }
                };
    
    }
    
	// the following IDs will go away soon
	private String name;
	private int numberOfContracts;
	private Date lastLoginDate;
	private String passwordStatus;
	private ContractProfile contractProfile;
	private boolean showCensusHistoryValue = false;
	private String email;
	private Boolean feeDisclsoureAccessAllowed = null;
	private Access404a5 access404a5;
	private Boolean tpaFeeScheduleAccessAllowed = null;
	private String selectedTpaUserProfileId = null;
	private String profileId;
	private boolean userAccessPermissionsInd = true;
	private String Ssn;
	private String emailAdddress;
	private boolean sendServiceAccessible = false;
	private Boolean payrollFeedbackServiceEnabled;

	/**
	 * @return the userAccessPermissionsInd
	 */
	public boolean isUserAccessPermissionsInd() {
		return userAccessPermissionsInd;
	}

	/**
	 * @param userAccessPermissionsInd the userAccessPermissionsInd to set
	 */
	public void setUserAccessPermissionsInd(boolean userAccessPermissionsInd) {
		this.userAccessPermissionsInd = userAccessPermissionsInd;
	}
	
	/**
	 * @return the teamLead
	 */
	public boolean getTeamLead() {
		if (getRole() instanceof TeamLead) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return Ssn;
	}

	/**
	 * @param ssn the ssn to set
	 */
	public void setSsn(String ssn) {
		Ssn = ssn;
	}
	/**
	 * @return the emailAdddress
	 */
	public String getEmailAdddress() {
		return emailAdddress;
	}

	/**
	 * @param emailAdddress the emailAdddress to set
	 */
	public void setEmailAdddress(String emailAdddress) {
		this.emailAdddress = emailAdddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// A set of accessible contract for message center
	// This is set only for external user, In Phase I,
	// the contracts has to be AE contracts.
	private Set<Integer> messageCenterAccessibleContracts;
	private Collection<Integer> messageCenterTpaFirms;

	public Collection<Integer> getMessageCenterTpaFirms() {
		return messageCenterTpaFirms;
	}

	public void setMessageCenterTpaFirms(
			Collection<Integer> messageCenterTpaFirms) {
		this.messageCenterTpaFirms = messageCenterTpaFirms;
	}


	public UserProfile() {
	}

	/**
	 * Constructor for User
	 */
	public UserProfile(String name) {
		super();
		this.name = name;
		this.numberOfContracts = 0;
	}

	/**
	 * @see Principal#getName()
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the principal
	 * 
	 * @return Returns a Principal
	 */
	public Principal getPrincipal() {
		return (Principal) getAbstractPrincipal();
	}

	/**
	 * Sets the principal
	 * 
	 * @param principal
	 *            The principal to set
	 */
	public void setPrincipal(Principal principal) {
		setAbstractPrincipal(principal);
	}

	/**
	 * This method returns the clientId
	 * 
	 * @return String
	 */
	public String getClientId() {
		String returnValue = "";
		if (contractProfile != null) {
			returnValue = contractProfile.getContract().getClientId();
		}
		return returnValue;
	}

	public String toString() {
		return StaticHelperClass.toString(this);
	}

	/**
	 * Sets the contract
	 * 
	 * @param contract
	 *            The contract to set
	 */
	public void setCurrentContract(Contract contract) {
		this.contractProfile = new ContractProfile(contract);
		this.feeDisclsoureAccessAllowed = null;
		resetAccess404a5();
		if (contract != null) {
			getPrincipal().getRole().setContractServiceFeatureMap(
					contract.getServiceFeatureMap());
		}
	}
	
	public void resetAccess404a5() {
	    this.access404a5 = null;
	}
	
	/**
	 * Gets the contract
	 * 
	 * @return Returns a the current Contract or null if the current contract is
	 *         not selected yet.
	 */
	public Contract getCurrentContract() {
		return contractProfile == null ? null : contractProfile.getContract();
	}

	// TODO: revisit this if we decide to store
	// only one contract in the user profile
	public boolean isMultipleContracts() {
		return this.numberOfContracts > 1 ? true : false;
	}

	public boolean isNavBarFullAvailable() {
		// contract status
		List allowedList = new ArrayList();
		allowedList.add(Contract.STATUS_ACTIVE_CONTRACT);
		allowedList.add(Contract.STATUS_CONTRACT_FROZEN);
		allowedList.add(Contract.STATUS_CONTRACT_DISCONTINUED);

		boolean result = allowedList.contains(getCurrentContract().getStatus());
		result = result & !getCurrentContract().isMta();
		/* TODO result = result & getCurrentContract().isContractAllocated();*/
		result = result
				& !(getPrincipal().getRole()
						.hasPermission(PermissionType.SELECTED_ACCESS));

		return result;
	}

	/**
	 * Checks if current contract status is one of the following: Proposal
	 * Signed (PS), Details Complete (DC), Pending Contract Approval (PC),
	 * Contract Approved (CA) In this case user has limited access to the site.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isWelcomePageAccessOnly() {
		// contract statuses for limited access
		List allowedList = new ArrayList();
		allowedList.add(Contract.STATUS_PROPOSAL_SIGNED);
		allowedList.add(Contract.STATUS_DETAILS_COMPLETED);
		allowedList.add(Contract.STATUS_PENDING_CONTRACT_APPROVAL);
		allowedList.add(Contract.STATUS_CONTRACT_APPROVED);

		boolean result = allowedList.contains(getCurrentContract().getStatus());

		return result;
	}

	/**
	 * Checks if current contract status is one of the following: Proposal
	 * Signed (PS), Details Complete (DC), Pending Contract Approval (PC) In
	 * this case user has limited access to the site.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isBeforeCAStatusAccessOnly() {
		// contract statuses for limited access
		List allowedList = new ArrayList();
		allowedList.add(Contract.STATUS_PROPOSAL_SIGNED);
		allowedList.add(Contract.STATUS_DETAILS_COMPLETED);
		allowedList.add(Contract.STATUS_PENDING_CONTRACT_APPROVAL);

		boolean result = allowedList.contains(getCurrentContract().getStatus());

		return result;
	}

	/**
	 * Gets the numberOfContracts
	 * 
	 * @return Returns a int
	 */
	public int getNumberOfContracts() {
		return numberOfContracts;
	}

	/**
	 * Sets the numberOfContracts
	 * 
	 * @param numberOfContracts
	 *            The numberOfContracts to set
	 */
	public void setNumberOfContracts(int numberOfContracts) {
		this.numberOfContracts = numberOfContracts;
	}

	/**
	 * Gets the lastLoginDate
	 * 
	 * @return Returns a Date
	 */
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Sets the lastLoginDate
	 * 
	 * @param lastLoginDate
	 *            The lastLoginDate to set
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * calculate if the user of the MTA contract is able to view SA
	 * 
	 * CNS.18. If the user is an External User Manager, Plan Administrator or
	 * Payroll administrator AND the currently viewed contract is an MTA
	 * contract (see definition above) then the Schedule A title, description,
	 * dropdown list and pdf icon will not be displayed.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isAllowedScheduleA() {
		if (contractProfile.getContract().isMta()) {
			UserRole role = getPrincipal().getRole();
			if (role.isPlanSponsor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the showContractSatements flag
	 * 
	 * @return Returns a boolean
	 */
	public boolean isAllowedContractStatements() {
		return getRole()
				.hasPermission(PermissionType.EMPLOYER_STATEMENT_ACCESS);
	}

	/**
	 * Checks whether the user has access permission to Contract documents.
	 * 
	 * @return boolean
	 */
	public boolean isAllowedContractDocuments() {
		return getRole()
				.hasPermission(PermissionType.CONTRACT_DOCUMENTS_ACCESS);
	}

	/**
	 * Flag indicating if user has access to cash accounts
	 * 
	 * @return
	 */
	public boolean isAllowedCashAccount() {
		return getRole().hasPermission(PermissionType.CASH_ACCOUNT_ACCESS);
	}

	/**
	 * Flag indicating if the user has access to direct debit
	 * 
	 * @return
	 */
	public boolean isAllowedDirectDebit() {
		return getRole().hasPermission(PermissionType.DIRECT_DEBIT_ACCOUNT);
	}

	/**
	 * Flag indicating if the user is allowed to upload submissions
	 * 
	 * @return
	 */
	public boolean isAllowedUploadSubmissions() {
		return getRole().hasPermission(PermissionType.UPLOAD_SUBMISSIONS);
	}

	/**
	 * Flag indicating if the user is allowed to update census data
	 * 
	 * @return
	 */
	public boolean isAllowedUpdateCensusData() {
		return getRole().hasPermission(PermissionType.UPDATE_CENSUS_DATA);
	}

	/**
	 * Flag indicating if the user is allowed to download report
	 * 
	 * @return
	 */
	public boolean isAllowedDownloadReport() {
		return getRole().hasPermission(PermissionType.REPORT_DOWNLOAD);
	}

	/**
	 * Flag indicating if the user has full access permission
	 * 
	 * @return
	 */
	public boolean isAllowedFullAccess() {
		return getRole().hasPermission(PermissionType.FULL_WEB_ACCESS);
	}

	/**
	 * Flag indicating if the user has Selected Access
	 * 
	 * @return
	 */
	public boolean isSelectedAccess() {
		return getRole().hasPermission(PermissionType.SELECTED_ACCESS);
	}

	/**
	 * @return true if you have role to access address history, otherwise false
	 */
	public boolean isAllowedParticipantAddressHistoryDownload() {
		return getRole().hasPermission(
				PermissionType.PARTICIPANT_ADDRESS_ACCESS); // TODO: verify
															// check is ok
	}

	/**
	 * @return
	 */
	public boolean isAllowedParticipantAddressesDownload() {
		return getRole().hasPermission(
				PermissionType.PARTICIPANT_ADDRESS_ACCESS);
	}

	/**
	 * Flag indicating if the user is allowed submission
	 * 
	 * @return
	 */
	public boolean isSubmissionAccess() {
		return getRole().hasPermission(PermissionType.SUBMISSION_ACCESS);
	}

	/**
	 * Flag indicating if the user is allowed submit/update vesting info
	 * 
	 * @return
	 */
	public boolean isAllowedSubmitUpdateVesting() {
		return getRole().hasPermission(PermissionType.SUBMIT_UPDATE_VESTING);
	}

	/**
	 * Flag indicating if the user is allowed to download reports
	 * 
	 * @return
	 */
	public boolean isReportDownloadAllowed() {
		return getRole().hasPermission(PermissionType.REPORT_DOWNLOAD);
	}

	/**
	 * Flag indicating if the user is allowed to access Notice Manager
	 * 
	 * @return
	 */
	public boolean isNoticeManagerAccessAllowed() {
		return getRole().hasPermission(PermissionType.ACCESS_NOTICE_MANAGER);
	}
	
	/**
	 * External users must have this permission set to be able to see
	 * submissions, other then their own, for a given contract.
	 * 
	 * Internal users will see all the submissions.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isAllowedToViewAllSubmissions() {
		return getRole().hasPermission(PermissionType.VIEW_ALL_SUBMISSIONS);
	}

	/**
	 * Internal users must have this permission set to be able to edit Contract
	 * Service Features for a given contract.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isAllowedToEditServiceFeatures() {
		return getRole().hasPermission(PermissionType.EDIT_SERVICE_FEATURES);
	}

	public boolean isAllowedIPIHypotheticalTool() {
		return getRole().hasPermission(PermissionType.IPI_HYPOTHETICAL_TOOL);
	}
	
	public boolean isAllowed408b2DisclosureRegen() {
		return getRole().hasPermission(PermissionType.REGEN_408_DISCLOSURE);
	}
	
	/**
	 * Gets the role
	 * 
	 * @return Returns a UserRole
	 */
	public UserRole getRole() {
		return getPrincipal().getRole();
	}

	/**
	 * Gets the passwordStatus
	 * 
	 * @return Returns a String
	 */
	public String getPasswordStatus() {
		return this.passwordStatus;
	}

	/**
	 * Sets the passwordStatus
	 * 
	 * @param passwordStatus
	 *            The passwordStatus to set
	 */
	public void setPasswordStatus(String passwordStatus) {
		this.passwordStatus = passwordStatus;
	}

	/**
	 * @return Returns the contractProfile.
	 */
	public ContractProfile getContractProfile() {
		return this.contractProfile;
	}

	/**
	 * @param contractProfile
	 *            The contractProfile to set.
	 */
	public void setContractProfile(ContractProfile contractProfile) {
		this.contractProfile = contractProfile;
	}

	public boolean isInternalUser() {
		return getRole().isInternalUser();
	}
	
	public boolean isBundledGAApprover() {
		if (getRole() instanceof BundledGaApprover) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method name is incorrect. Doesnt return true if the user is an
	 * external user. Returns true only if the user is external user manager.
	 * 
	 * @return
	 */
	public boolean isExternalUser() {
		UserInfo userInfo = new UserInfo();
		return userInfo.isExternalUserManagerInd();
	}

	/**
	 * Flag indicating if the user is a Super CAR.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isSuperCar() {
		if (getRole() instanceof SuperCAR) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Flag indicating if the user is a Bundled GA CAR.
	 * 
	 * @return Returns a boolean
	 */
	public boolean isBundledGACAR() {
		if (getRole() instanceof BundledGaCAR) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Flag indicating if the user is Trustee.
	 * 
	 * @return boolean
	 */
	public boolean isTrustee() {
		if (getRole() instanceof Trustee) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Flag indicating if the user is TPA.
	 * 
	 * @return boolean
	 */
	public boolean isTPA() {
		if (getRole() instanceof ThirdPartyAdministrator) {
			return true;
		} else {
			return false;
		}
	}

	// CR 16 added a method for InternalServiceCAR User
	/**
	 * Flag indicating if the user is a InternalServiceCAR.
	 * 
	 * @return Returns a boolean
	 */

	public boolean isInternalServicesCAR() {
		if (getRole() instanceof InternalServicesCAR) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the email newsletter preferences.
	 */
	public String getEmailNewsletter() {
		return preferences.get(UserPreferenceKeys.EMAIL_NEWSLETTER_PREFERENCE,
				"");
	}


	/**
	 * TODO: This has to be removed after code clean up of 404. The reference
	 * for this method is in navigationbar.jsp and while doing code clean up for
	 * 408 or 404 in navigationbar.jsp, this method has to be removed
	 * accordingly. Need to discuss
	 * 
	 * @return Returns a boolean
	 */
	public boolean isParticipantDisclosureAllowed() {
	    return ! getAccess404a5().getAccessibleFacilities().isEmpty();
	}
	
	public Access404a5 getAccess404a5() {
	    
	    if (access404a5 == null) {
	        
	        UserAccess access;
	        
	        if (isTPA()) {
	            access = UserAccess.TPA;
	        } else if (isInternalUser()) {
	            access = UserAccess.INTERNAL_USER;
	        } else if (getRole().isPlanSponsor()) {
	            access = UserAccess.PSW;
	        } else {
	            access = null;
	        }
	        
	        if (access == null) {
	            
	            access404a5 = NO_ACCESS_404A5;
	            
	        } else {
	            
	            Contract currentContract = getCurrentContract();
	            
	            try {
	                
    	            access404a5 =
    	                    FundServiceDelegate
    	                    .getInstance()
    	                    .get404a5Permissions(
    	                            EnumSet.of(
    	                                    Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
    	                                    Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
    	                                    Facility.INVESTMENT_COMPARATIVE_CHART,
    	                                    Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE,
    	                                    Facility.PARTICIPANT_STATEMENT_FEES_TOOL,
    	                                    Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL,
    	                                    Facility._404a5_NOTICE_INFO),
    	                            currentContract.getContractNumber(),
    	                            access);
    	            
    	        } catch (Exception e) {
    	            
    	            LogUtility.logSystemException(
    	                    Constants.PS_APPLICATION_ID,
    	                    new SystemException(
    	                            e,
    	                            "Profile ID " + getPrincipal() == null ? null : getPrincipal().getProfileId() +
    	                            " contract ID " + currentContract == null ? null : currentContract.getContractNumber() +
    	                            ":  " + e.getMessage()));
    	            
    	            access404a5 = NO_ACCESS_404A5;
    	            
    	        }
    
	        }
	        
	    }
	    
        return access404a5;
        
	}

	/**
	 * 
	 * @return Returns a boolean
	 * @throws RemoteException 
	 */
	public boolean isFeeDisclsoureAccessAllowed(){

		if (feeDisclsoureAccessAllowed == null) {
			Contract currentContract = getCurrentContract();
			try {
				feeDisclsoureAccessAllowed = FeeDisclosureUtility
						.isFeeDisclsoureAvaiable(currentContract);
			} catch (SystemException | RemoteException e) {
				  LogUtility.logSystemException(
				            Constants.PS_APPLICATION_ID,
				            new SystemException(
				                    e,
				                    "Profile ID " + getPrincipal() == null ? null : getPrincipal().getProfileId() +
				                    " contract ID " + currentContract == null ? null : currentContract.getContractNumber() +
				                    ":  " + e.getMessage()));
				feeDisclsoureAccessAllowed = false;
			}
		}
		return feeDisclsoureAccessAllowed;
	}
	
	/**
	 * 
	 * @return Returns a boolean
	 */
	public boolean isTpaFeeScheduleAccessAllowed() {
		if (tpaFeeScheduleAccessAllowed == null) {
			if(getRole().isInternalUser()) {
				if(StringUtils.isEmpty(getSelectedTpaUserProfileId())) {
					tpaFeeScheduleAccessAllowed = false;
				} else {
					tpaFeeScheduleAccessAllowed = isTpaFeeScheduleApplicableFirmsAvailable(new Long(getSelectedTpaUserProfileId()));
				}
			} else if(getRole().isTPA()){
				tpaFeeScheduleAccessAllowed = isTpaFeeScheduleApplicableFirmsAvailable(getPrincipal().getProfileId());
			} else {
				tpaFeeScheduleAccessAllowed = false;
			}
		}
		return tpaFeeScheduleAccessAllowed;
	}

	private boolean isTpaFeeScheduleApplicableFirmsAvailable(Long profileId){
		boolean result;
		try {
			String companyCode = com.manulife.pension.ps.web.Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment.getInstance().getSiteLocation()) 
	        		? com.manulife.pension.ps.web.Constants.COMPANY_ID_NY : com.manulife.pension.ps.web.Constants.COMPANY_ID_US;
			Map<Integer, String> firms = TPAServiceDelegate.getInstance()
			.retrieveTpaFirmsByTPAUserProfileId(profileId, companyCode);
			if(firms.size() == 0) {
				result = false;
			} else {
				result = true;
			}
		} catch (SystemException e) {
			 LogUtility.logSystemException(Constants.PS_APPLICATION_ID,
			            new SystemException(e, "Profile ID " + profileId  + e.getMessage()));
			 result = false;
		}
		return result;
	}
	
	public String getSelectedTpaUserProfileId() {
		return selectedTpaUserProfileId;
	}

	public void setSelectedTpaUserProfileId(String selectedTpaUserProfileId) {
		this.selectedTpaUserProfileId = selectedTpaUserProfileId;
		this.tpaFeeScheduleAccessAllowed = null;
	}
	
	/**
	 * 
	 * @return Returns a boolean
	 */
	public boolean isParticipantStatementAvaiable() {

		Contract currentContract = getCurrentContract();
		List<String> allowedList = new ArrayList<String>();
		allowedList.add(Contract.STATUS_ACTIVE_CONTRACT);
		allowedList.add(Contract.STATUS_CONTRACT_FROZEN);
		allowedList.add(Contract.STATUS_CONTRACT_DISCONTINUED);
		allowedList.add(Contract.STATUS_INACTIVE_CONTRACT);
		boolean isAllowed = false;
		if (isInternalUser() && ! currentContract.isDefinedBenefitContract()
				&& allowedList.contains(currentContract.getStatus())){
			isAllowed = true;
        }
		return isAllowed;
	}
	
	/**
	 * A flag to indicate if the previous value should be shown for census pages
	 * 
	 * @return
	 */
	public boolean isShowCensusHistoryValue() {
		return showCensusHistoryValue;
	}

	/**
	 * Set a flag to indicate if the previous value should be shown for census
	 * pages
	 */
	public void setShowCensusHistoryValue(boolean showCensusHistoryValue) {
		this.showCensusHistoryValue = showCensusHistoryValue;
	}

	/**
	 * Returns a set of accessible contracts for message center
	 * 
	 * @return
	 */
	public Set<Integer> getMessageCenterAccessibleContracts() {
		return messageCenterAccessibleContracts;
	}

	/**
	 * Set accessible contracts for message center
	 * 
	 * @return
	 */
	public void setMessageCenterAccessibleContracts(
			Set<Integer> messageCenterAccessibleContracts) {
		this.messageCenterAccessibleContracts = messageCenterAccessibleContracts;
	}
	
	/**
	 * This method name is incorrect. Doesnt return true if the user is an
	 * TPA user. Returns true only if the user is TPA user manager.
	 * 
	 * @return
	 */
	public boolean isTpaUserManager() {
		UserInfo userInfo = new UserInfo();
		return userInfo.isTpaUserManagerInd();
	}

	/**
	 * @return the sendServiceAccess
	 */
	public boolean isSendServiceAccessible() {
		Contract currentContract = getCurrentContract();
		if( currentContract!=null  ){
			NoticeServiceDelegate noticeServiceDelegate = 	NoticeServiceDelegate.getInstance();
			try {
				sendServiceAccessible = noticeServiceDelegate.isNoticeAvailable24Months(currentContract.getContractNumber());
			} catch (SystemException e) {
				  LogUtility.logSystemException(
				            Constants.PS_APPLICATION_ID,
				            new SystemException(
				                    e,
				                    "Profile ID " + getPrincipal() == null ? null : getPrincipal().getProfileId() +
				                    " contract ID " + currentContract == null ? null : currentContract.getContractNumber() +
				                    ":  " + e.getMessage()));
				  sendServiceAccessible = false;
			}
		}
	
		return sendServiceAccessible;
	}

	/**
	 * checks whether contract is merrill or not.
	 * @return
	 * @throws SystemException
	 */
	public boolean isMerrillLynchContract() throws SystemException {
		boolean merrillContract = false;
		int contractNumber = getCurrentContract().getContractNumber();
		if(contractNumber != 0){
			ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
    		ContractDetailsOtherVO contractDetailsOtherVO = contractServiceDelegate.getContractDetailsOther(contractNumber);
			if(contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()){
				merrillContract = true;
			}
    	}
		return merrillContract;
	}
	
	/**
	 * Determine if Payroll Feedback Service is enabled. 
	 * 
	 * @return the payrollFeedbackServiceEnabled
	 */
	@SuppressWarnings("unchecked")
	public boolean isPayrollFeedbackServiceEnabled() {		
			int contractNumber = getCurrentContract().getContractNumber();
			try {
				this.payrollFeedbackServiceEnabled = Boolean.TRUE
						.equals(ContractServiceFeatureUtil.hasContractServiceFeature(
								ContractServiceDelegate.getInstance().getContractServiceFeatures(contractNumber),
								ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE,
								new HashSet<>(Collections.singletonList(ServiceFeatureConstants.YES))));
			} catch (NumberFormatException | ApplicationException | SystemException e) {
				throw new IllegalStateException(
						"Failed while checking Payroll Feedback Service Feature flag for the contract ID [" + contractNumber
								+ "]!",
						e);
			}
	
		return this.payrollFeedbackServiceEnabled;
	}
	
}