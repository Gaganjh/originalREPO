package com.manulife.pension.ps.web.profiles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.FaxNumber;
import com.manulife.pension.common.PhoneNumber;

/**
 * @author Steven
 */
public class AddEditClientUserForm extends UserProfileForm {

    private static final long serialVersionUID = 1L;

    /*
     * Maps the button label to the corresponding action.
     */
    public static final String BUTTON_LABEL_SAVE = "save";

    public static final String BUTTON_LABEL_CANCEL = "cancel";

    public static final String BUTTON_LABEL_RESET = "reset password";

    public static final String BUTTON_LABEL_UNLOCK = "unlock password";

    public static final String BUTTON_LABEL_FINISH = "finish";

    private static final String SAVE_ACTION = "save";

    private static final String CANCEL_ACTION = "cancel";

    private static final String CONFIRM_ACTION = "confirm";

    private static final String RESET_ACTION = "reset";

    private static final String UNLOCK_ACTION = "unlock";

    private static final String FINISH_ACTION = "finish";
    /**
     * field prefix constant
     */
    public static final String FIELD_USER_NAME = "userName";

    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FIELD_LAST_NAME = "lastName";

    public static final String FIELD_EMAIL = "email";
    
    public static final String FIELD_SECONDARY_EMAIL = "secondaryEmail";
    
	public static final String FIELD_MOBILE_NUMBER = "mobileNumber";
    
	public static final String FIELD_TELEPHONE_NUMBER = "telephoneNumber";
	
	public static final String FIELD_TELEPHONE_EXTENSION = "telephoneExtension";
	
	public static final String FIELD_FAX_NUMBER = "faxNumber";
	
	public static final String FIELD_EXTENSION_NUMBER = "telephoneExtension";
	
	public static final String FIELD_COMMENTS = "comments";
	
    public static final String FIELD_SSN = "ssn";

    public static final String FIELD_CONTRACT_TO_ADD = "contractToAdd";

    public static final String FIELD_CONTRACT_ACCESS = "contractAccess";

    public static final String FIELD_PLANSPONSOR_SITE_ROLE = "planSponsorSiteRole";

    public static final String FIELD_WEB_ACCESS = "webAccess";
    
    public static final String FIELD_PRIMARY_CONTACT = "primaryContact";
    
    private static final Map ACTION_LABEL_MAP = new HashMap();

    private List<Integer> cannotManageRoleContracts;
    
    private String planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;

    private String actionLabel;

    private String loginUserRole = AccessLevelHelper.NO_ACCESS;

    private String contractToAdd;
    
    private String originalWebAccess;

    //flag to indicates page elements are enabled for loginUser
    private boolean fieldsEnableForInternalUser;
    
    private boolean ignoreSSNWarning = false;
    
    private boolean addStaggingContact = false;
    
    private int staggingContactId;

    static {
        ACTION_LABEL_MAP.put(BUTTON_LABEL_SAVE, SAVE_ACTION);
        ACTION_LABEL_MAP.put(BUTTON_LABEL_CANCEL, CANCEL_ACTION);
        ACTION_LABEL_MAP.put(BUTTON_LABEL_RESET, RESET_ACTION);
        ACTION_LABEL_MAP.put(BUTTON_LABEL_UNLOCK, UNLOCK_ACTION);
        ACTION_LABEL_MAP.put(BUTTON_LABEL_FINISH, FINISH_ACTION);
    }

    /**
     * Constructor.
     */
    public AddEditClientUserForm() {
        super();
    }

    /**
     * @return Returns the contractToAdd.
     */
    public String getContractToAdd() {
        return contractToAdd;
    }

    /**
     * @param contractToAdd The contractToAdd to set.
     */
    public void setContractToAdd(String contractToAdd) {
        this.contractToAdd = trimString(contractToAdd);
    }

    public boolean isConfirmAction() {
        return CONFIRM_ACTION.equals(getAction());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void clear( HttpServletRequest request) {
        super.clear( request);
        contractToAdd = null;
        planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;
        actionLabel = null;
        this.ignoreSSNWarning = false;
        this.addStaggingContact = Boolean.FALSE;
        this.staggingContactId = 0;
    }

    public void reset( HttpServletRequest request) {
        super.reset( request);
        actionLabel = null;
    }

    /**
     * Produces a flattened map filled with values of the form. The key of the map is a Struts
     * compatible expression of the field, the value is the actual value. The value can be null.
     * 
     * @return A flattened map filled with values of the form.
     */
    protected Map getFormAsMap() {
        Map formMap = new HashMap();
        // form fields

        formMap.put(FIELD_USER_NAME, getUserName());
        formMap.put(FIELD_FIRST_NAME, getFirstName());
        formMap.put(FIELD_LAST_NAME, getLastName());
        formMap.put(FIELD_EMAIL, getEmail());
        formMap.put(FIELD_SECONDARY_EMAIL, getSecondaryEmail());
        formMap.put(FIELD_MOBILE_NUMBER, getMobileNumber());
        formMap.put(FIELD_TELEPHONE_NUMBER, convertToNullPhoneNumber(getTelephoneNumber()));
        formMap.put(FIELD_TELEPHONE_EXTENSION, StringUtils.isBlank(getTelephoneExtension())? null: getTelephoneExtension());
        formMap.put(FIELD_FAX_NUMBER, convertToNullFaxNumber(getFaxNumber()));
        formMap.put(FIELD_COMMENTS, getComments());        
        formMap.put(FIELD_SSN, getSsn().toString());
        formMap.put(FIELD_PLANSPONSOR_SITE_ROLE, getPlanSponsorSiteRole());
        formMap.put(FIELD_WEB_ACCESS, getWebAccess());


        // contract access fields
        List contractAccesses = getContractAccesses();
        for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
            ClientUserContractAccess contractAccess = (ClientUserContractAccess) it.next();
            if (contractAccess.getContractNumber() != null) {
                String fieldPrefix = new StringBuffer(FIELD_CONTRACT_ACCESS).append("[").append(
                        contractAccess.getContractNumber()).append("].").toString();
                formMap.putAll(contractAccess.getFormAsMap(fieldPrefix));
            }
        }

        return formMap;
    }

    /**
     * Converts the empty Phone Number to null
     * 
     * @param phoneNumber
     * @return
     */
    public PhoneNumber convertToNullPhoneNumber(PhoneNumber phoneNumber){
    	
    	if(StringUtils.isBlank(phoneNumber.getAreaCode()) && StringUtils.isBlank(phoneNumber.getPhonePrefix()) && 
    								StringUtils.isBlank(phoneNumber.getPhoneSuffix())){
			phoneNumber.setAreaCode(null);
			phoneNumber.setPhonePrefix(null);
			phoneNumber.setPhoneSuffix(null);
    	}
    	return phoneNumber;
    }
    
    /**
     * Converts the empty Fax Number to null
     * @param faxNumber
     * @return
     */
    
    public FaxNumber convertToNullFaxNumber(FaxNumber faxNumber){
    	
    	if(StringUtils.isBlank(faxNumber.getAreaCode()) && StringUtils.isBlank(faxNumber.getFaxPrefix()) && 
    								StringUtils.isBlank(faxNumber.getFaxSuffix())){
    		faxNumber.setAreaCode(null);
    		faxNumber.setFaxPrefix(null);
    		faxNumber.setFaxSuffix(null);
    	}
    	return faxNumber;
    }
    
    /**
     * Compute the changes in this form using the stored form object. Changes are stored in the
     * FormChanges object. The FormChanges object remembers additions, removals, and modifications.
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
            Map oldFormMap = ((AddEditClientUserForm) getClonedForm()).getFormAsMap();
            formChanges.computeChanges(newFormMap, oldFormMap);
        }
        return formChanges;
    }

    /**
     * Help method to check all contracts to see if there is at least one after Business Converted
     * Refer to DFS 11 SVC 42a & DFS10 SCC103
     * 
     * @return
     */
    public boolean isOwnBusnessConvertedContract() {
        return ClientUserContractAccessActionHelper.hasBusinessConvertedContract(getContractAccesses());

    }

    // Business Converted indicator flag at least one no

    public boolean isNonBCFlag() {
        return ClientUserContractAccessActionHelper
                .hasNonBusinessConvertedContract(getContractAccesses());


    }

    public String getLoginUserRole() {
        return loginUserRole;
    }

    public void setLoginUserRole(String loginUserRole) {
        this.loginUserRole = loginUserRole;
    }

    /**
     * @return Returns the actionLabel.
     */
    public String getActionLabel() {
        return actionLabel;
    }
    /**
     * @param actionLabel The actionLabel to set.
     */
    public void setActionLabel(String actionLabel) {
        this.actionLabel = trimString(actionLabel);
        setAction((String) ACTION_LABEL_MAP.get(actionLabel));
    }

    public String getAction() {
        if (super.getAction().length() == 0 && actionLabel != null && actionLabel.length() > 0) {
            setAction((String) ACTION_LABEL_MAP.get(actionLabel));
        }
        return super.getAction();
    }

    public String getPlanSponsorSiteRole() {
        return planSponsorSiteRole;
    }

    public void setPlanSponsorSiteRole(String planSponsorSiteRole) {
        this.planSponsorSiteRole = planSponsorSiteRole;
    }

    public boolean isSaveAction() {
        return SAVE_ACTION.equals(getAction());
    }

    public boolean isContractAccessEmpty(){
    	
    	List contractAccesses = getContractAccesses();
    	
        return contractAccesses == null || contractAccesses.size() == 0 ? true : false; 
    }

    public boolean isFieldsEnableForInternalUser() {
        return fieldsEnableForInternalUser;
    }

    public void setFieldsEnableForInternalUser(boolean fieldsEnableForInternalUser) {
        this.fieldsEnableForInternalUser = fieldsEnableForInternalUser;
    }

    /**
     * @return the originalWebAccess
     */
    public String getOriginalWebAccess() {
        return originalWebAccess;
    }

    /**
     * @param originalWebAccess the originalWebAccess to set
     */
    public void setOriginalWebAccess(String originalWebAccess) {
        this.originalWebAccess = originalWebAccess;
    }

    /**
     * @return the cannotManageRoleContracts
     */
    public List<Integer> getCannotManageRoleContracts() {
        return cannotManageRoleContracts;
    }

    /**
     * @param cannotManageRoleContracts the cannotManageRoleContracts to set
     */
    public void setCannotManageRoleContracts(List<Integer> cannotManageRoleContracts) {
        this.cannotManageRoleContracts = cannotManageRoleContracts;
    }
    
	public boolean isIgnoreSSNWarning() {
		return ignoreSSNWarning;
	}

	public void setIgnoreSSNWarning(boolean ignoreSSNWarning) {
		this.ignoreSSNWarning = ignoreSSNWarning;
	}

	/**
	 * @return the addStaggingContact
	 */
	public boolean isAddStaggingContact() {
		return addStaggingContact;
	}

	/**
	 * @param addStaggingContact the addStaggingContact to set
	 */
	public void setAddStaggingContact(boolean addStaggingContact) {
		this.addStaggingContact = addStaggingContact;
	}

	/**
	 * @return the staggingContactId
	 */
	public int getStaggingContactId() {
		return staggingContactId;
	}

	/**
	 * @param staggingContactId the staggingContactId to set
	 */
	public void setStaggingContactId(int staggingContactId) {
		this.staggingContactId = staggingContactId;
	}


}