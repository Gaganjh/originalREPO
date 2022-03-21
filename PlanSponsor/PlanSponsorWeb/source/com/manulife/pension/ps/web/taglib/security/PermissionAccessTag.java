package com.manulife.pension.ps.web.taglib.security;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author marcest
 * 
 */
public class PermissionAccessTag extends BodyTagSupport {

    private static final long serialVersionUID = 6864433251167688332L;

    private static final Logger logger = Logger.getLogger(PermissionAccessTag.class);

    private static final String ANY_CONTRACT = "ANY";

    private String permissions;

    private String contractId;

    private String userName;

    private String hasAll;
    
    private String tpaFirmId;

    /**
     * @return the permissions
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * The permissions is a comma delimited list of permission codes.
     * 
     * @see com.manulife.pension.service.security.role.permission.PermissionType
     * 
     * @param permissions the permissions to set
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the contractId
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the hasAll
     */
    public String getHasAll() {
        return hasAll;
    }

    /**
     * @param hasAll the hasAll to set
     */
    public void setHasAll(String hasAll) {
        this.hasAll = hasAll;
    }

    /**
     * Return permissions as as String array
     * 
     * @return
     */
    private String[] getPermissionCodes() {
        StringTokenizer strToken = new StringTokenizer(getPermissions(), ",;");
        String perms[] = new String[strToken.countTokens()];
        int index = 0;
        while (strToken.hasMoreTokens()) {
            perms[index] = strToken.nextToken().toUpperCase();
            index++;
        }
        return perms;
    }

    /**
     * Perform the test required for this particular tag, and either evaluate or skip the body of
     * this tag.
     * 
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {
        if (hasPermission())
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);
    }

    /**
     * Evaluate the remainder of the current page normally.
     * 
     * @exception JspException if a JSP exception occurs
     */
    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    /**
     * Verify whether the currently logged in user has the permission to execute this tag
     * 
     * @return
     */
    protected boolean hasPermission() {
        boolean hasPermission = true;
        UserProfile userProfile = SessionHelper
                .getUserProfile((HttpServletRequest) this.pageContext.getRequest());

        String[] permissionArray = getPermissionCodes();
        if (getUserName() == null) {
        	
        	// We need the UserInfo object for the logged in user
        	UserInfo userInfo = null;
        	Object userInfoObject = ((HttpServletRequest) this.pageContext.getRequest()).getAttribute(Constants.USERINFO_KEY);
        	if(userInfoObject != null ){
        		userInfo = (UserInfo) userInfoObject;
        	} else {
        		userInfo =  getUserInfo(userProfile, userProfile.getPrincipal()
                        .getUserName());
        	}
        	
            if (getContractId() == null || userProfile.isInternalUser()) {
                // We're checking if the logged in user has permissions on the current contract
                // If the user is internal, the contract is irrelevant
                hasPermission = checkPermissions(userProfile.getRole(), permissionArray);
            } else if (getTpaFirmId() != null && userProfile.isTPA()) {
            	
                if (ANY_CONTRACT.equals(getContractId().toUpperCase())) {
                    // We're checking if the logged in user has permissions on any of his/her Firms
                    hasPermission = checkAllFirms(userInfo.getTpaFirms(), permissionArray);
                } else {
                    // We're checking if the logged in user has permissions on the specified TPA firm
                    UserRole userRole = getRoleForContractByFirmId(userInfo, Integer.valueOf(getTpaFirmId()));
                    hasPermission = checkPermissions(userRole, permissionArray);
                }
            } else {
            	
                if (ANY_CONTRACT.equals(getContractId().toUpperCase())) {
                    // We're checking if the logged in user has permissions on any of his/her
                    // contracts
                    hasPermission = checkAllContracts(userInfo.getContractPermissions(),
                            permissionArray);
                } else {
                    // We're checking if the logged in user has permissions on the specified
                    // contract
                    UserRole userRole = getRoleForContract(userInfo, getContractNumber());
                    hasPermission = checkPermissions(userRole, permissionArray);
                }
            }
        } else {
            // We need the UserInfo object for the specified user
            UserInfo userInfo = getUserInfo(userProfile, getUserName());
            if (userInfo.getRole() instanceof InternalUser) {
                // If the user is internal, the contract is irrelevant
                hasPermission = checkPermissions(userProfile.getRole(), permissionArray);
            } else if (getTpaFirmId() != null && userInfo.getRole().isTPA()) {
                if (ANY_CONTRACT.equals(getContractId().toUpperCase())) {
                    // We're checking if the specified user has permissions on any of his/her Firms
                    hasPermission = checkAllFirms(userInfo.getTpaFirms(), permissionArray);
                } else {
                    // We're checking if the specified user has permissions on the specified TPA firm
                    UserRole userRole = getRoleForContractByFirmId(userInfo, Integer.valueOf(getTpaFirmId()));
                    hasPermission = checkPermissions(userRole, permissionArray);
                }
            } else {
                if (getContractId() == null) {
                    // We're checking if the specified user has permissions on the current contract
                    UserRole userRole = getRoleForContract(userInfo, userProfile
                            .getCurrentContract().getContractNumber());
                    hasPermission = checkPermissions(userRole, permissionArray);
                } else {
                    if (ANY_CONTRACT.equals(getContractId().toUpperCase())) {
                        // We're checking if the specified user has permissions on any of his/her
                        // contracts
                        hasPermission = checkAllContracts(userInfo.getContractPermissions(),
                                permissionArray);
                    } else {
                        // We're checking if the specified user has permissions on the specified
                        // contract
                        UserRole userRole = getRoleForContract(userInfo, getContractNumber());
                        hasPermission = checkPermissions(userRole, permissionArray);
                    }
                }
            }
        }
        return hasPermission;
    }

    private int getContractNumber() {
        int contractNumber = 0;
        try {
            contractNumber = Integer.parseInt(getContractId());
        } catch (NumberFormatException e) {
            logger.error(e);
        }
        return contractNumber;
    }

    private UserRole getRoleForContract(UserInfo userInfo, int contractNumber) {
        UserRole userRole = null;
        if (userInfo.getRole() instanceof InternalUser) {
            userRole = userInfo.getRole();
        } else {
            ContractPermission contractPermission = userInfo.getContractPermission(contractNumber);
            if (contractPermission != null) {
                userRole = contractPermission.getRole();
            }
        }
        return userRole;
    }

    private UserInfo getUserInfo(UserProfile userProfile, String userName) {
        UserInfo userInfo = null;
        try {
            userInfo = SecurityServiceDelegate.getInstance().searchByUserName(
                    userProfile.getPrincipal(), userName);
        } catch (ApplicationException ae) {
            logger.error(ae);
        } catch (SystemException se) {
            logger.error(se);
        }
        return userInfo;
    }

    private boolean checkAllContracts(ContractPermission[] contractPermissionArray,
            String[] permissionArray) {
        boolean hasPermissions = false;
        for (int i = 0; i < contractPermissionArray.length; i++) {
            if (checkPermissions(contractPermissionArray[i].getRole(), permissionArray)) {
                hasPermissions = true;
                break;
            }
        }
        return hasPermissions;
    }

    private boolean checkPermissions(UserRole userRole, String[] permissionArray) {
        boolean hasPermission = false;
        if (userRole != null) {
            if (getHasAll() != null && "TRUE".equals(getHasAll().toUpperCase())) {
                hasPermission = userRole.hasAllPermissions(permissionArray);
            } else {
                hasPermission = userRole.hasPermissionExist(permissionArray);
            }
        }
        return hasPermission;
    }

    private boolean checkAllFirms(TPAFirmInfo[] tpaFirmArray,
            String[] permissionArray) {
        boolean hasPermissions = false;
        for (TPAFirmInfo tpaFirm : tpaFirmArray) {
            if (checkPermissions(tpaFirm.getContractPermission().getRole(), permissionArray)) {
                hasPermissions = true;
                break;
            }
        }
        return hasPermissions;
    }
    
    private UserRole getRoleForContractByFirmId(UserInfo userInfo, int tpaFirmId) {
        UserRole userRole = null;
        if (userInfo.getRole() instanceof InternalUser) {
            userRole = userInfo.getRole();
        } else {
            ContractPermission contractPermission = userInfo.getTpaFirm(tpaFirmId).getContractPermission();
            if (contractPermission != null) {
                userRole = contractPermission.getRole();
            }
        }
        return userRole;
    }
    
	public String getTpaFirmId() {
		return tpaFirmId;
	}

	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

}
