package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.DefaultRolePermissions;

public class EmailPreferences implements  Serializable, Cloneable {
	
	private boolean showiLoanEmail; // all default to false, set in addEditTpaUserAction
	
	private Boolean sendNewsletters;
    private Boolean receiveiLoans;
    
    public EmailPreferences() {	    	
    	this.sendNewsletters = Boolean.FALSE;
    	this.receiveiLoans = Boolean.FALSE;
    }
    
    public void loadPreferences(ContractPermission cp) { // see PermissonType 
        UserRole userRole = cp.getRole();
        if (cp.isReceiveIloansEmail()) {
            this.receiveiLoans = Boolean.TRUE;
        }       
        if (userRole.hasPermission(PermissionType.NEWSLETTER_EMAIL)) {
            this.sendNewsletters = Boolean.TRUE;
        }
    }
    
    public void loadDefaults(UserRole userRole) throws SystemException {
        DefaultRolePermissions defaultPermissions = userRole.getDefaultRolePermissions();
        if (defaultPermissions == null) {
            defaultPermissions = SecurityServiceDelegate.getInstance().getDefaultRolePermissions(userRole.toString());
        }

        this.receiveiLoans = getDefault(defaultPermissions, PermissionType.RECEIVE_ILOANS_EMAIL);
        this.sendNewsletters = getDefault(defaultPermissions, PermissionType.NEWSLETTER_EMAIL);
    }
    
    private Boolean getDefault(DefaultRolePermissions defaultPermissions, PermissionType permissionType) {
        String defaultValue = defaultPermissions.getDefaultPermissionValue(permissionType);
        if (DefaultRolePermissions.TRUE.equals(defaultValue) || DefaultRolePermissions.YES.equals(defaultValue)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    // used to calculate if change was made
    public void populateMap(Map dataMap) {
    	if (sendNewsletters !=null) dataMap.put("sendNewsletters", this.sendNewsletters.toString());
        if (receiveiLoans !=null) dataMap.put("receiveiLoans", this.receiveiLoans.toString());
    }
    
    
    // used to setup for save. 
    public void populatePermissions(ContractPermission contPer) {
    	Collection permissions = contPer.getRole().getPermissions();
    	
    	if (Boolean.TRUE.equals(this.receiveiLoans)) permissions.add(PermissionType.RECEIVE_ILOANS_EMAIL);
    	if (Boolean.TRUE.equals(this.sendNewsletters)) permissions.add(PermissionType.NEWSLETTER_EMAIL);

    }
    
	public Boolean getReceiveiLoads() {
		return receiveiLoans;
	}

	public void setReceiveiLoads(Boolean receiveiLoads) {
		this.receiveiLoans = receiveiLoads;
	}

	public Boolean getSendNewsletters() {
		return sendNewsletters;
	}

	public void setSendNewsletters(Boolean sendNewsletters) {
		this.sendNewsletters = sendNewsletters;
	}

	public Boolean getReceiveiLoans() {
		return receiveiLoans;
	}

	public void setReceiveiLoans(Boolean receiveiLoans) {
		this.receiveiLoans = receiveiLoans;
	}

	// clone
	public Object clone() {
		EmailPreferences theClone = new EmailPreferences();
		
		theClone.setReceiveiLoads(receiveiLoans);
		theClone.setSendNewsletters(sendNewsletters);
		
		return theClone;
	}

	/**
	 * @return the showiLoanEmail
	 */
	public boolean isShowiLoanEmail() {
		return showiLoanEmail;
	}

	/**
	 * @param showiLoanEmail the showiLoanEmail to set
	 */
	public void setShowiLoanEmail(boolean showiLoanEmail) {
		this.showiLoanEmail = showiLoanEmail;
	}
	
}
