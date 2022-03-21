package com.manulife.pension.ps.web.census.util;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;

/**
 * A Helper class to encapsulate the security related permission for employee snap shot pages.
 * 
 * @author guweigu
 * 
 */
public class EmployeeSnapshotSecurityProfile {
    private UserProfile user;

    public EmployeeSnapshotSecurityProfile() {
        user = null;
    }

    public EmployeeSnapshotSecurityProfile(UserProfile user) {
        this.user = user;
    }

    public void setUserProfile(UserProfile user) {
        this.user = user;
    }

    /**
     * Is the user the internal user
     * 
     * @return
     */
    public boolean isInternalUser() {
        return user != null && user.getRole().isInternalUser();
    }

    /**
     * Does the user has the permission to update census
     * 
     * @return
     */
    public boolean isUpdateCensusData() {
        return user != null && user.getRole().hasPermission(PermissionType.UPDATE_CENSUS_DATA);
    }

    public boolean isViewSalary(String maskSensitiveInd) {
        if (user == null) {
            return false;
        }
        UserRole role = user.getRole();
        boolean hasViewSalaryPermission = role.hasPermission(PermissionType.VIEW_SALARY);
        
        if (role.isInternalUser() || role instanceof ThirdPartyAdministrator) {
            return hasViewSalaryPermission;
        } else {
            if (hasViewSalaryPermission && !"Y".equals(maskSensitiveInd)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public boolean isContractDI() {
        return Contract.STATUS_CONTRACT_DISCONTINUED.equals(user.getCurrentContract().getStatus());
    }
}
