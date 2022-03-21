package com.manulife.pension.ps.web.tools;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.profiles.RoleValueLabelBean;
/**
 * 
 * @author Steven
 *
 */
public class BusinessConvertionItem implements Cloneable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_ROLE = "select one";
    private String userName;
    private Ssn ssn;
    private String profileStatus;
    private RoleValueLabelBean role = new RoleValueLabelBean(DEFAULT_ROLE,DEFAULT_ROLE);
    private String contactType;
    private boolean primaryContact;
    private boolean mailRecipient;
    private boolean webAccess;
    private boolean trusteeMailRecipient;
    private boolean pptStatementConsultant;
    private boolean signReceivedTrustee;
    private boolean signReceivedAuthorizedSignor;
    
    
    public boolean isMailRecipient() {
        return mailRecipient;
    }
    public void setMailRecipient(boolean mailRecipient) {
        this.mailRecipient = mailRecipient;
    }
    public boolean isPrimaryContact() {
        return primaryContact;
    }
    public void setPrimaryContact(boolean primaryContact) {
        this.primaryContact = primaryContact;
    }
    public String getProfileStatus() {
        return profileStatus;
    }
    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }
 
    public Ssn getSsn() {
        return ssn;
    }
    public void setSsn(Ssn ssn) {
        this.ssn = ssn;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public RoleValueLabelBean getRole() {
        return role;
    }
    public void setRole(RoleValueLabelBean role) {
        this.role = role;
    }
    
    /**
     * Clone object for change tracker function and highlight if changed function
     */
    public Object clone() {
        try {
            BusinessConvertionItem clonedObject = (BusinessConvertionItem) super.clone();
            clonedObject.role = (RoleValueLabelBean) role.clone();
            if(ssn!=null){
            	clonedObject.ssn = (Ssn)ssn.clone();	
            }
            
            return clonedObject;
        } catch (CloneNotSupportedException e) {
            // this should not happen because this object implements Cloneable
            throw new NestableRuntimeException(e);
        }
    }
    public String getContactType() {
        return contactType;
    }
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
	public boolean isWebAccess() {
		return webAccess;
	}
	public void setWebAccess(boolean webAccess) {
		this.webAccess = webAccess;
	}
	public boolean isTrusteeMailRecipient() {
		return trusteeMailRecipient;
	}
	public void setTrusteeMailRecipient(boolean trusteeMailRecipient) {
		this.trusteeMailRecipient = trusteeMailRecipient;
	}
	public boolean isPptStatementConsultant() {
		return pptStatementConsultant;
	}
	public void setPptStatementConsultant(boolean pptStatementConsultant) {
		this.pptStatementConsultant = pptStatementConsultant;
	}
	public boolean isSignReceivedTrustee() {
		return signReceivedTrustee;
	}
	public void setSignReceivedTrustee(boolean signReceivedTrustee) {
		this.signReceivedTrustee = signReceivedTrustee;
	}
	public boolean isSignReceivedAuthorizedSignor() {
		return signReceivedAuthorizedSignor;
	}
	public void setSignReceivedAuthorizedSignor(boolean signReceivedAuthorizedSignor) {
		this.signReceivedAuthorizedSignor = signReceivedAuthorizedSignor;
	}
    
    
}
