package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.common.FaxNumber;
import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.contract.valueobject.ContactCommentVO;
import com.manulife.pension.service.security.role.UserRole;

public class UserProfileForm extends CloneableAutoForm
{
	public static final String FORM_YES_CONSTANT = "Yes";
    public static final String FORM_NO_CONSTANT = "No";
    public static final String PROFILE_STATUS_SUSPENDED = "Suspended";
    
    private static final Map<String, String> ACTION_LABEL_MAP = new HashMap<String, String>();

	private String firstName;
	private String lastName;
	private String email;
	private String secondaryEmail;
	private String mobileNumber;
	private PhoneNumber telephoneNumber = new PhoneNumber();
	private String telephoneExtension;
	private FaxNumber faxNumber = new FaxNumber();
	private String comments;  		
	private ContactCommentVO commentDetails;
	private Ssn ssn = new Ssn();
	private boolean canManageAllContracts;  //flag to indicates client can manage all contract
	private String webAccess;
    private String userName;
    private long profileId;
	private String passwordState;
	private String profileStatus;
    private String selectedContractNumber;
    private String clientUserAction;
	private UserRole userRole;
	private List<ClientUserContractAccess> contractAccesses = new ArrayList<ClientUserContractAccess>();
	private String actionLabel;

	static
	{
		ACTION_LABEL_MAP.put("back", "cancel");
	}

	/**
	 * Constructor for SuspendProfileForm
	 */
	public UserProfileForm()
	{
		super();
	}

	// move from AddEditclientUserForm
    public Object clone()
    {
    	UserProfileForm myClone = (UserProfileForm) super.clone();
        myClone.ssn = (Ssn)ssn.clone();
        if(telephoneNumber != null){
        	myClone.telephoneNumber = (PhoneNumber)telephoneNumber.clone();
        }
		if (faxNumber != null) {
        	myClone.faxNumber = (FaxNumber) faxNumber.clone();
        }
        myClone.contractAccesses = new ArrayList<ClientUserContractAccess>();

        for (ClientUserContractAccess access : contractAccesses) {
            myClone.contractAccesses.add((ClientUserContractAccess) access.clone());
        }

        return myClone;
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
    
	/**
     * Helper method to check all contracts to see if there is at least one after Business Converted
     * Refer to DFS 11 SVC 42a & DFS10 SCC103
     * 
     * @return
     */
    public boolean isOwnBusnessConvertedContract()
    {
        return ClientUserContractAccessActionHelper.hasBusinessConvertedContract(getContractAccesses());
    }

	public String getFullName()
	{
		StringBuffer buf = new StringBuffer();
		buf.append(getFirstName());
		buf.append(" ");
		buf.append(getLastName());
		
		return buf.toString();
	}
	
    public boolean isCanManageAllContracts()
    {
        return canManageAllContracts;
    }

    public void setCanManageAllContracts(boolean clientManageAllContract)
    {
        this.canManageAllContracts = clientManageAllContract;
    }
	
    public List<ClientUserContractAccess> getAllContractAccesses()
    {
        return contractAccesses;
    }

    public List<ClientUserContractAccess> getContractAccesses() {
        // Filter out all contract access objects with no access
        List<ClientUserContractAccess> nonDeletedContractAccesses = new ArrayList<ClientUserContractAccess>();
        for (Iterator i = contractAccesses.iterator(); i.hasNext();) {
            ClientUserContractAccess contractAccess =(ClientUserContractAccess) i.next(); 
            if (!contractAccess.getPlanSponsorSiteRole().equals(AccessLevelHelper.NO_ACCESS)) {
                nonDeletedContractAccesses.add(contractAccess);
            }
        }
        return nonDeletedContractAccesses;
    }

	public void setContractAccesses(List<ClientUserContractAccess> contractAccesses)
	{
		this.contractAccesses = contractAccesses;
	}

	public String getSecondaryEmail()
	{
	    if(secondaryEmail != null && secondaryEmail.trim().length()> 0){
            return secondaryEmail.toLowerCase();
        }
		return secondaryEmail;
	}

	public void setSecondaryEmail(String secondaryEmail)
	{
		this.secondaryEmail = trimString(secondaryEmail);
	}
	
	public String getEmail()
	{
	    if(email != null && email.trim().length()> 0){
            return email.toLowerCase();
        }
        return email;
	}

	public void setEmail(String email)
	{
		this.email = trimString(email);
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = trimString(firstName);
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = trimString(lastName);
	}

	public String getPasswordState()
	{
		return passwordState;
	}

	public void setPasswordState(String passwordState)
	{
		this.passwordState = passwordState;
	}

	public String getProfileStatus()
	{
		return profileStatus;
	}

	public void setProfileStatus(String profileStatus)
	{
		this.profileStatus = profileStatus;
	}

	public Ssn getSsn()
	{
		return ssn;
	}

	public void setSsn(Ssn ssn)
	{
		this.ssn = ssn;
	}

	public String getUserName()
	{
		if(this.userName == null){
			// call to security service by using profile id
			try {
				this.userName =  SecurityServiceDelegate.getInstance().getLDAPUserNameByProfileId(getProfileId());
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getWebAccess()
	{
		return webAccess;
	}

	public void setWebAccess(String webAccess)
	{
		this.webAccess = webAccess;
	}

	public UserRole getUserRole()
	{
		return userRole;
	}

	public void setUserRole(UserRole userRole)
	{
		this.userRole = userRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request)
	{
		super.reset( request);
		super.clear( request);
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.mobileNumber=null;
		this.telephoneNumber = new PhoneNumber();
		this.telephoneExtension = null;
		this.faxNumber = new FaxNumber();
		this.comments = null;
		this.commentDetails = null;
		this.ssn = new Ssn();
		this.canManageAllContracts = false;
		this.webAccess = null;
		this.userName = null;
		this.passwordState = null;
		this.profileStatus = null;
		this.userRole = null;
		this.contractAccesses.clear();
	}

    /**
     * @return the profileId
     */
    public long getProfileId() {
        return profileId;
    }

    /**
     * @param profileId the profileId to set
     */
    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    /**
     * @return the selectedContractNumber
     */
    public String getSelectedContractNumber() {
        return selectedContractNumber;
    }

    /**
     * @param selectedContractNumber the selectedContractNumber to set
     */
    public void setSelectedContractNumber(String selectedContractNumber) {
        this.selectedContractNumber = selectedContractNumber;
    }

    /**
     * Find the ContractAccess object for a given contract number
     * 
     * @param contractNumber
     * @return
     */
    public ClientUserContractAccess findContractAccess(int contractNumber) {
        
        List contractAccesses = getAllContractAccesses();
        
        for (Iterator it = contractAccesses.iterator(); it.hasNext();) {
            ClientUserContractAccess access = (ClientUserContractAccess) it.next();
            Integer itContractNumber = access.getContractNumber();
            /*
             * The contract number can be null because we're adding a dummy contract access in the
             * cloned form to maintain the order of contracts.
             */
            if (itContractNumber != null && itContractNumber.intValue() == contractNumber) {
                return access;
            }
        }
        return null;
    }

    /**
     * @return the clientUserAction
     */
    public String getClientUserAction() {
        return clientUserAction;
    }

    /**
     * @param clientUserAction the clientUserAction to set
     */
    public void setClientUserAction(String clientUserAction) {
        this.clientUserAction = clientUserAction;
    }
    
    /**
     * Retuns contact comments
     * @return the comments
     */
	public String getComments() {
		return comments;
	}

	/**
	 * Set the contact comments
	 * @param comments
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	/**
	 * Returns the contact comments details
	 * @return
	 */
	public ContactCommentVO getCommentDetails() {
		return commentDetails;
	}

	/**
	 * Set the contact comments
	 * @param commentDetails
	 */
	public void setCommentDetails(ContactCommentVO commentDetails) {
		this.commentDetails = commentDetails;
	}

	/**
	 * Returns contact fax number
	 * @return the faxNumber
	 */
	public FaxNumber getFaxNumber() {
		return faxNumber;
	}

	/**
	 * Set the fax number 
	 * @param faxNumber
	 */
	public void setFaxNumber(FaxNumber faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * 
	 * @return
	 */
	public String getTelephoneExtension() {
		return telephoneExtension;
	}

	public void setTelephoneExtension(String telephoneExtension) {
		this.telephoneExtension = telephoneExtension;
	}

	public PhoneNumber getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(PhoneNumber telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
    
    

}

