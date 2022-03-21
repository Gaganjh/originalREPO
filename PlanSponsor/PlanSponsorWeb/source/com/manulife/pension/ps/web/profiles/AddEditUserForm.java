package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.FaxNumber;
import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;
  
/**
 * @author Charles Chan
 */
public class AddEditUserForm extends CloneableAutoForm {

	private static final String SAVE_ACTION = "save";
	private static final String CONFIRM_ACTION = "confirm";
	private static final String SUSPEND_ACTION = "suspend";
	private static final String UNSUSPEND_ACTION = "unsuspend";
	private static final String DELETE_ACTION = "delete";	

	private Boolean hiddenFirmExist = new Boolean(false);
	
	private long profileId;
	
	private String userName;

	private String firstName;

	private String lastName;

	private String email;
	
	private String secondaryEmail;

	private String contractToAdd;

	private String tpaFirmId;
	
	private String removeTpaFirmId;

	private String employeeNumber;

	private String planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;

	private String participantSiteRole;

	private String actionLabel;

	private String passwordState;

	private String profileStatus;

	private Ssn ssn = new Ssn();
	
	private PhoneNumber phone = new PhoneNumber();
	
	private String mobile;
	
	private String ext;
	
	private FaxNumber fax = new FaxNumber();
	
	private Boolean tabOpen = Boolean.FALSE; // email tab open or not

	private List contractAccesses = new ArrayList();

	private List<TpaFirm> tpaFirms = new ArrayList<TpaFirm>();
    
	private List tpaumFirms = new ArrayList();

	private boolean showUnlock = false;
	
	private UserRole userRole;
	
	private String brokerDealerSiteRole;
	
	private String rvpId;
	
	private String rmId;
	
	private String rmIdSaved;
	
	private String rmDisplayName;
	
	private List<LabelValueBean> rmList = null;
	
	private String rvpDisplayName;
	
	private String licenceVerified;

	private String accessIPIHypotheticalTool;

	private String access408DisclosureRegen;

	private List<LabelValueBean> rvpList = null;
	
	public static final String FIELD_USER_NAME = "userName";

	public static final String FIELD_FIRST_NAME = "firstName";

	public static final String FIELD_LAST_NAME = "lastName";

	public static final String FIELD_EMAIL = "email";
	
	public static final String FIELD_SECONDARY_EMAIL = "secondaryEmail";
	
	public static final String FIELD_EMAIL_NEWLETTER = "emailNewsletter";

	public static final String FIELD_SSN = "ssn";
	
	public static final String FIELD_PHONE_NUMBER = "phone";
	
	public static final String FIELD_EXTENSION_NUMBER = "ext";
	
	public static final String FIELD_FAX_NUMBER = "fax";

	public static final String FIELD_MOBILE_NUMBER = "mobile";
	
	public static final String FIELD_CONTRACT_TO_ADD = "contractToAdd";

	public static final String FIELD_TPA_FIRM_ID_TO_ADD = "tpaFirmId";

	public static final String FIELD_TPA_FIRMS = "tpaFirms";

	public static final String FIELD_CONTRACT_ACCESS = "contractAccess";

	public static final String FIELD_TPA_FIRM_ACCESS = "tpaFirmAccess";

	public static final String FIELD_EMPLOYEE_NUMBER = "employeeNumber";

	public static final String FIELD_PLANSPONSOR_SITE_ROLE = "planSponsorSiteRole";

	public static final String FIELD_PARTICIPANT_SITE_ROLE = "participantSiteRole";

	public static final String FIELD_BROKERDEALER_SITE_ROLE = "brokerDealerSiteRole";
	
	public static final String FIELD_WEB_ACCESS = "webAccess";
	
	public static final String FIELD_PRIMARY_CONTACT = "primaryContact";
	
	public static final String FIELD_SIGN_RECEIVED_AUTH_SIGNER = "signatureReceivedAuthSigner";
	
	public static final String FIELD_RVP = "rvpId";
	
	public static final String FIELD_RM = "rmId";
	
	public static final String FILED_RMUASSIGNED = "* Unassigned *";
	
	public static final String FIELD_ACCESS_IPI_HYPOTHETICAL_TOOL = "accessIPIHypotheticalTool";
	
	public static final String FIELD_ACCESS_408_DISCLOSURE_REGEN = "access408DisclosureRegen";
	
	private static final Map ACTION_LABEL_MAP = new HashMap();
	
	private EmailPreferences emailPreferences;
	
	private boolean ignoreWarnings = false;
	private boolean generateChangeTrackingMessage = false;
	
	private boolean webAccess = true;
	
	private boolean fromTPAContactsTab = false;
	private boolean signatureReceivedAuthSigner = false;
	private boolean primaryContact = false;
	private int currentContractId;
		
	/*
	 * Maps the button label to the corresponding action.
	 */
	public static final String BUTTON_LABEL_SAVE = "save";
	public static final String BUTTON_LABEL_CANCEL = "cancel";
	public static final String BUTTON_LABEL_CHANGE_PERMISSIONS = "change permissions";
	
	public static final String BUTTON_BACK = "back"; // view, delete, suspend, unsuspend TPA user pages
	public static final String BUTTON_VIEW_PERMISSIONS = "view permissions"; // view, delete, suspend, unsuspend tpa user pages 
	public static final String BUTTON_SUSPEND = "suspend"; // suspend TPA user page
	public static final String BUTTON_UNSUSPEND = "unsuspend"; // unsuspend TPA user page
	public static final String BUTTON_DELETE = "delete"; // delete TPA user page
	public static final String BUTTON_UNLOCK = "unlock password"; // view(tpa) user
	public static final String BUTTON_RESET = "reset password"; // view(tpa) user
	
	static {
		ACTION_LABEL_MAP.put(BUTTON_LABEL_SAVE, "save");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_CANCEL, "cancel");
		ACTION_LABEL_MAP.put(BUTTON_LABEL_CHANGE_PERMISSIONS, "changePermissions");
		ACTION_LABEL_MAP.put(BUTTON_BACK, "cancel");
		ACTION_LABEL_MAP.put(BUTTON_SUSPEND, "suspend");
		ACTION_LABEL_MAP.put(BUTTON_UNSUSPEND, "unsuspend");
		ACTION_LABEL_MAP.put(BUTTON_DELETE, "delete");
		ACTION_LABEL_MAP.put(BUTTON_VIEW_PERMISSIONS, "viewPermissions");
		ACTION_LABEL_MAP.put(BUTTON_UNLOCK, "unlockPassword");
		ACTION_LABEL_MAP.put(BUTTON_RESET, "resetPassword");
	}

	/**
	 * Constructor.
	 */
	public AddEditUserForm() {
		super();		
	}

	/**
	 * Gets the profileId
	 * @return Returns a long
	 */
	public long getProfileId() {
		return profileId;
	}

	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	
	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		if(this.userName == null){
			//TODO call to security service by using profile id
			try {
				this.userName =  SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(getProfileId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		return userName;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = trimString(userName);
	}

	/**
	 * @return Returns the contractToAdd.
	 */
	public String getContractToAdd() {
		return contractToAdd;
	}

	/**
	 * @param contractToAdd
	 *            The contractToAdd to set.
	 */
	public void setContractToAdd(String contractToAdd) {
		this.contractToAdd = trimString(contractToAdd);
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
	    if(email != null && email.trim().length()> 0){
            return email.toLowerCase();
        }
        return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = trimString(email);
	}
	
	/**
	 * @return Returns the secondary email.
	 */
	public String getSecondaryEmail() {
	    if(secondaryEmail != null && secondaryEmail.trim().length()> 0){
            return secondaryEmail.toLowerCase();
        }
		return secondaryEmail;
	}

	/**
	 * @param email
	 *            The secondary email to set.
	 */
	public void setSecondaryEmail(String secondaryEmail) {
		this.secondaryEmail = trimString(secondaryEmail);
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = trimString(firstName);
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = trimString(lastName);
	}

	public String getMaskedSSN() {
		if (ssn!=null && !ssn.isEmpty()) {
			return "XXX-XX-"+getSsn().toString().substring(5);
		} else {
			return "";
		}
	}
	
	/**
	 * @return Returns the ssn.
	 */
	public Ssn getSsn() {
		return ssn;
	}

	/**
	 * @param ssn
	 *            The ssn to set.
	 */
	public void setSsn(Ssn ssn) {
		this.ssn = ssn;
	}
	
	/**
	 * @return Returns the phone.
	 */
	public PhoneNumber getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            The phone to set.
	 */
	public void setPhone(PhoneNumber  phone) {
		this.phone = phone;
	}
	
	/**
	 * @return Returns the ext.
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            The ext to set.
	 */
	public void setExt(String  ext) {
		this.ext = ext;
	}
	/**
	 * @return Returns the mobile.
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            The mobile to set.
	 */
	public void setMobile(String  mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * @return Returns the fax.
	 */
	public FaxNumber getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            The fax to set.
	 */
	public void setFax(FaxNumber  fax) {
		this.fax = fax;
	}
	
	public String getProfileStatus() {
		return profileStatus;
	}

	public void setProfileStatus(String status) {
		this.profileStatus = status;
	}

	public TPAUserContractAccess findContractAccess(int contractNumber) {
		for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
			TPAUserContractAccess access = (TPAUserContractAccess) it.next();
			Integer itContractNumber = access.getContractNumber();
			/*
			 * The contract number can be null because we're adding a dummy
			 * contract access in the cloned form to maintain the order
			 * of contracts.
			 */
			if (itContractNumber != null &&
					itContractNumber.intValue() == contractNumber) {
				return access;
			}
		}
		return null;
	}

	public TPAUserContractAccess getContractAccess(int index) {
		while (index > contractAccesses.size() - 1) {
			contractAccesses.add(new ContractAccess());
		}
		return (TPAUserContractAccess) contractAccesses.get(index);
	}

	public List getContractAccesses() {
		return contractAccesses;
	}

	
	public EmailPreferences getEmailPreferences() {
		return this.emailPreferences;
	}
	
	public void setEmailPreferences(EmailPreferences ep) {
		this.emailPreferences = ep;
	}
	
	/**
	 * @return Returns the tpaFirmToAdd.
	 */
	public String getTpaFirmId() {
		return tpaFirmId;
	}

	/**
	 * @param tpaFirmToAdd
	 *            The tpaFirmToAdd to set.
	 */
	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = trimString(tpaFirmId);
	}

	/**
	 * @return Returns the removeTpaFirmId.
	 */
	public String getRemoveTpaFirmId() {
		return removeTpaFirmId;
	}

	/**
	 * @param removeTpaFirmId The removeTpaFirmId to set.
	 */
	public void setRemoveTpaFirmId(String removeTpaFirmId) {
		this.removeTpaFirmId = removeTpaFirmId;
	}

	public TpaFirm getTpaFirm(int index) {
		while (index > tpaFirms.size() - 1) {
			tpaFirms.add(new TpaFirm());
		}
		return (TpaFirm) tpaFirms.get(index);
	}

	public List<TpaFirm> getTpaFirms() {
		return tpaFirms;
	}

	public List<TpaFirm> getUndeletedTpaFirms() {
        List<TpaFirm> undeletedTpaFirms = new ArrayList<TpaFirm>();
        for (TpaFirm firm : tpaFirms) {
			if (!firm.isRemoved()) {
				undeletedTpaFirms.add(firm);
			}
		}
		return undeletedTpaFirms;
	}

	public int getNumberOfUndeletedTpaFirms() {
		return getUndeletedTpaFirms().size();
	}
	
    public List<TpaFirm> getVisibleTpaFirms() {
        List<TpaFirm> visibleTpaFirms = new ArrayList<TpaFirm>();
        for (TpaFirm firm : tpaFirms) {
            if (!firm.isRemoved() && !firm.isHidden()) {
                visibleTpaFirms.add(firm);
            }
        }
        return visibleTpaFirms;
    }

	/**
	 * @return Returns the employeeNumber.
	 */
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * @param employeeNumber
	 *            The employeeNumber to set.
	 */
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = trimString(employeeNumber);
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
		this.planSponsorSiteRole = trimString(accessLevel);
	}

	/**
	 * @return Returns the accessLevel.
	 */
	public String getParticipantSiteRole() {
		return participantSiteRole;
	}

	/**
	 * @param accessLevel
	 *            The accessLevel to set.
	 */
	public void setParticipantSiteRole(String accessLevel) {
		this.participantSiteRole = trimString(accessLevel);
	}

	/**
	 * @return Returns the passwordState.
	 */
	public String getPasswordState() {
		return passwordState;
	}

	/**
	 * @param passwordState
	 *            The passwordState to set.
	 */
	public void setPasswordState(String passwordState) {
		this.passwordState = trimString(passwordState);
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

	public String getAccessIPIHypotheticalTool() {
		return accessIPIHypotheticalTool;
	}
	
	public void setAccessIPIHypotheticalTool(String value) {
		accessIPIHypotheticalTool = value;
	}
	
	public String getAccess408DisclosureRegen() {
		return access408DisclosureRegen;
	}
	
	public void setAccess408DisclosureRegen(String value) {
		access408DisclosureRegen = value;
	}
	
	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}

	public boolean isSaveAction() {
		return SAVE_ACTION.equals(getAction());
	}

	public boolean isConfirmAction() {
		return CONFIRM_ACTION.equals(getAction());
	}
	
	public boolean isSuspendAction() {
		return SUSPEND_ACTION.equals(getAction());
	}
	
	public boolean isUnsuspendAction() {
		return UNSUSPEND_ACTION.equals(getAction());
	}
	
	public boolean isDeleteAction() {
		return DELETE_ACTION.equalsIgnoreCase(getAction());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
		super.reset( request);
		super.clear( request);
		userName = null;
		firstName = null;
		lastName = null;
		ssn = new Ssn();
		phone = new PhoneNumber();
		ext = null;
		mobile = null;
		fax = new FaxNumber();
		contractAccesses.clear();
		email = null;
		contractToAdd = null;
		tpaFirmId = null;
		removeTpaFirmId = null;
		tpaFirms.clear();
		employeeNumber = null;
		planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;
		participantSiteRole = null;
		brokerDealerSiteRole = AccessLevelHelper.NO_ACCESS;
		rvpId = null;
		rvpDisplayName = null;
		rmId = null;
		rmDisplayName = null;
		profileStatus = null;
		actionLabel = null;
		passwordState = null;
		accessIPIHypotheticalTool = "No";
		access408DisclosureRegen = "No";
		this.ignoreWarnings = false;
	}

	public void reset( HttpServletRequest request) {
		super.reset( request);
		actionLabel = null;
	}

	public Object clone() {
		AddEditUserForm myClone = (AddEditUserForm) super.clone();
		myClone.ssn = (Ssn) ssn.clone();
		myClone.phone = (PhoneNumber) phone.clone();
		myClone.fax = (FaxNumber) fax.clone();
		myClone.contractAccesses = new ArrayList();
		myClone.tpaFirms = new ArrayList();
		
		for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
			TPAUserContractAccess access = (TPAUserContractAccess) it.next();
			myClone.contractAccesses.add(access.clone());
		}
		
		for (Iterator it = tpaFirms.iterator(); it.hasNext();) {
			TpaFirm firm = (TpaFirm) it.next();
			// some settings are shared between the two forms so, prior to 
			// doing clone, we need to propigate these settings over.
	        TPAActionHelper.syncSettingsToUserPermissions(firm);
			firm.storeClonedForm();
			myClone.tpaFirms.add((TpaFirm) firm.getClonedForm());
		}

		if (this.emailPreferences == null) {
			this.emailPreferences = new EmailPreferences(); 
		}
		
		myClone.setEmailPreferences((EmailPreferences)this.emailPreferences.clone());
		
		return myClone;
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
		formMap.put(FIELD_USER_NAME, userName);
		formMap.put(FIELD_FIRST_NAME, firstName);
		formMap.put(FIELD_LAST_NAME, lastName);
		formMap.put(FIELD_EMAIL, email);
		formMap.put(FIELD_EMPLOYEE_NUMBER, employeeNumber);
		formMap.put(FIELD_SSN, ssn.toString());
		formMap.put(FIELD_PHONE_NUMBER, phone.getValue());		
		formMap.put(FIELD_EXTENSION_NUMBER, StringUtils.trimToEmpty(ext));
		formMap.put(FIELD_FAX_NUMBER, fax.getValue());
		formMap.put(FIELD_MOBILE_NUMBER, mobile);
		formMap.put(FIELD_PLANSPONSOR_SITE_ROLE, getPlanSponsorSiteRole(planSponsorSiteRole));
		formMap.put(FIELD_PARTICIPANT_SITE_ROLE, getEzkSiteRole(participantSiteRole));
		formMap.put(FIELD_BROKERDEALER_SITE_ROLE, ManageInternalUserHelper
				.getBDWRoleName(brokerDealerSiteRole));
		formMap.put(FIELD_RVP, getRvpId());
		formMap.put(FIELD_RM, getRmId());
		formMap.put(FIELD_ACCESS_IPI_HYPOTHETICAL_TOOL, accessIPIHypotheticalTool);
		formMap.put(FIELD_ACCESS_408_DISCLOSURE_REGEN, access408DisclosureRegen);
		
		for (Iterator it = contractAccesses.iterator(); it.hasNext(); ) {
			TPAUserContractAccess contractAccess = (TPAUserContractAccess) it.next();
			if (contractAccess.getContractNumber() != null) {
				String fieldPrefix = new StringBuffer(FIELD_CONTRACT_ACCESS)
						.append("[").append(contractAccess.getContractNumber())
						.append("].").toString();
				formMap.putAll(contractAccess.getFormAsMap(fieldPrefix));
			}
		}

		for (Iterator it = tpaFirms.iterator(); it.hasNext();) {
            TpaFirm firm = (TpaFirm) it.next();
            String fieldPrefix = new StringBuffer(FIELD_TPA_FIRMS).append("[")
                    .append(firm.getId()).append("].").toString();
            if (!firm.isRemoved()) {
                String fieldId = fieldPrefix + TpaFirm.FIELD_ID;
                formMap.put(fieldId, firm.getId());
                Iterator cit = firm.getContractAccesses().iterator();
                
                if (cit.hasNext()) {
                	TPAUserContractAccess contractAccess = (TPAUserContractAccess) cit.next();
                    formMap.putAll(contractAccess.getFormAsMap(fieldPrefix));
                    /*
                     * Remove irrelevant information
                     */
                    formMap.remove(fieldPrefix
                            + ContractAccess.FIELD_CONTRACT_NUMBER);
                    formMap
                            .remove(fieldPrefix
                                    + ContractAccess.FIELD_SELECTED_DIRECT_DEBIT_ACCOUNTS);
                }
            }
        }
		
		if (this.emailPreferences !=null) this.emailPreferences.populateMap(formMap);
		
		formMap.put(FIELD_WEB_ACCESS, webAccess);
		
		if(fromTPAContactsTab){
			String fieldPrefix = new StringBuffer("Contract").append("[")
	        		.append(currentContractId).append("].").toString();
			formMap.put(fieldPrefix + FIELD_PRIMARY_CONTACT, primaryContact);
			formMap.put(fieldPrefix + FIELD_SIGN_RECEIVED_AUTH_SIGNER, signatureReceivedAuthSigner);
		}

		return formMap;
	}

    private String getPlanSponsorSiteRole(String value) {
        UserRole userRole = UserRoleFactory.getUserRole(value);
        if (userRole == null) {
            return AccessLevelHelper.NO_ACCESS_STRING;
        } else {
            return userRole.getDisplayName();
        }
    }

    private String getEzkSiteRole(String value) {
        if (value == null || value.length() == 0)
            return AccessLevelHelper.NO_ACCESS_STRING;

        List accessLevels = AccessLevelHelper.getInstance().getEzkAccessLevels(new InternalUserManager());

        for (Iterator it = accessLevels.iterator(); it.hasNext();) {
            LabelValueBean bean = (LabelValueBean) it.next();
            if (bean.getValue().equalsIgnoreCase(value))
                return bean.getLabel();
        }
        return AccessLevelHelper.NO_ACCESS_STRING;
    }

	/**
     * Compute the changes in this form using the stored form object. Changes are stored in the FormChanges object. The FormChanges object remembers additions, removals, and
     * modifications.
	 * 
     * @return A FormChanges object that captures all the changes made in the form.
	 */
	public FormChanges getChanges() {
		FormChanges formChanges = new FormChanges();

		if (getClonedForm() != null) {
			/*
			 * Obtains a flattened map for both forms.
			 */
			Map newFormMap = getFormAsMap();
			Map oldFormMap = ((AddEditUserForm) getClonedForm())
					.getFormAsMap();
			formChanges.computeChanges(newFormMap, oldFormMap);
		}
		return formChanges;
	}

	/**
	 * @param tpaumFirms The tpaumFirms to set.
	 */
	public void setTpaumFirms(List tpaumFirms) {
		this.tpaumFirms = tpaumFirms;
	}

	/**
	 * @return Returns the tpaumFirms.
	 */
	public List getTpaumFirms() {
		return tpaumFirms;
	}

	/**
	 * @param hiddenFirmExist The hiddenFirmExist to set.
	 */
	public void setHiddenFirmExist(Boolean hiddenFirmExist) {
		this.hiddenFirmExist = hiddenFirmExist;
	}

	/**
	 * @return Returns the hiddenFirmExist.
	 */
	public Boolean isHiddenFirmExist() {
		return hiddenFirmExist;
	}

	public Boolean getTabOpen() {
		return tabOpen;
	}

	public void setTabOpen(Boolean tabOpen) {
		this.tabOpen = tabOpen;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public boolean isIgnoreWarnings() {
		return ignoreWarnings;
	}

	public void setIgnoreWarnings(boolean ignoreWarnings) {
		this.ignoreWarnings = ignoreWarnings;
	}

	public boolean getShowUnlock() {
		return showUnlock;
	}

	public void setShowUnlock(boolean showUnlock) {
		this.showUnlock = showUnlock;
	}

	public boolean isGenerateChangeTrackingMessage() {
		return generateChangeTrackingMessage;
	}

	public void setGenerateChangeTrackingMessage(
			boolean generateChangeTrackingMessage) {
		this.generateChangeTrackingMessage = generateChangeTrackingMessage;
	}

	public String getBrokerDealerSiteRole() {
		return brokerDealerSiteRole;
	}

	public void setBrokerDealerSiteRole(String brokerDealerSiteRole) {
		this.brokerDealerSiteRole = brokerDealerSiteRole;
		// if the role is not rvp, clean the rvp id.
		if (!StringUtils.equals(BDUserRoleType.RVP.getUserRoleCode(), brokerDealerSiteRole)) {
			setRvpId("");
		}
	}

	public String getRvpId() {
		return rvpId;
	}

	public void setRvpId(String rvpId) {
		this.rvpId = rvpId;
	}
	
	public String getRvpDisplayName() {
		return rvpDisplayName;
	}

	public void setRvpDisplayName(String rvpDisplayName) {
		this.rvpDisplayName = rvpDisplayName;
	}

	public List<LabelValueBean> getRvpList() {
		return rvpList;
	}
	
	/**
	 * @return the rmDisplayName
	 */
	public String getRmDisplayName() {
		return rmDisplayName;
	}

	/**
	 * @param rmDisplayName the rmDisplayName to set
	 */
	public void setRmDisplayName(String rmDisplayName) {
		this.rmDisplayName = rmDisplayName;
	}

	
	
	
	     
	

	/**
	 * @return the rmId
	 */
	public String getRmId() {
		return rmId;
	}

	/**
	 * @return the rmList
	 */
	public List<LabelValueBean> getRmList() {
		return rmList;
	}

	/**
	 * @param rmList the rmList to set
	 */
	public void setRmList(List<LabelValueBean> rmList) {
		this.rmList = rmList;
	}

	/**
	 * @param rmId the rmId to set
	 */
	public void setRmId(String rmId) {
		this.rmId = rmId;
	}
	
	/**
	 * 
	 * @return the rmId.
	 */
	public String getRmIdSaved() {
		return rmIdSaved;
	}
    /**
     * 
     * @param rmIdSaved the rmId to set
     */
	public void setRmIdSaved(String rmIdSaved) {
		this.rmIdSaved = rmIdSaved;
	}

	public void setRvpList(List<LabelValueBean> rvpList) {
		this.rvpList = rvpList;
	}
	
	public String getBdLicenceVerified() {
		return licenceVerified;
	}

	public void setBdLicenceVerified(String licenceVerified) {
		this.licenceVerified = licenceVerified;
	}

	public boolean isWebAccess() {
		return webAccess;
	}

	public void setWebAccess(boolean webAccess) {
		this.webAccess = webAccess;
	}

	public boolean isFromTPAContactsTab() {
		return fromTPAContactsTab;
	}

	public void setFromTPAContactsTab(boolean fromTPAContactsTab) {
		this.fromTPAContactsTab = fromTPAContactsTab;
	}

	public boolean isSignatureReceivedAuthSigner() {
		return signatureReceivedAuthSigner;
	}

	public void setSignatureReceivedAuthSigner(boolean signatureReceivedAuthSigner) {
		this.signatureReceivedAuthSigner = signatureReceivedAuthSigner;
	}

	public boolean isPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(boolean primaryContact) {
		this.primaryContact = primaryContact;
	}

	public int getCurrentContractId() {
		return currentContractId;
	}

	public void setCurrentContractId(int currentContractId) {
		this.currentContractId = currentContractId;
	}

}