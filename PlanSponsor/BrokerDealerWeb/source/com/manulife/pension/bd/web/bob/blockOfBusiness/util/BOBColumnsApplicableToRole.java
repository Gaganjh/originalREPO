package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.Map;

/**
 * This class will have the column information specific to a given user type.
 * 
 * @author harlomte
 * 
 */
public class BOBColumnsApplicableToRole {

    // This will hold the applicable columns for a given user role.
    private Map<String, BOBColumnsApplicableToTab> applicableColumnsForAUserRole;

    public Map<String, BOBColumnsApplicableToTab> getApplicableColumnsForAUserRole() {
        return applicableColumnsForAUserRole;
    }

    public void setApplicableColumnsForAUserRole(
            Map<String, BOBColumnsApplicableToTab> applicableColumnsForAUserRole) {
        this.applicableColumnsForAUserRole = applicableColumnsForAUserRole;
    }

    /**
     * This method gets the applicable columns for a given tab, for a given user type.
     * 
     * @param tabName - the tab name.
     * @return - the BOBColumnsApplicableToTab object, having applicable columns.
     */
    public BOBColumnsApplicableToTab getApplicableColumnsForTab(String tabName) {
        BOBColumnsApplicableToTab columnsApplicableToTab = applicableColumnsForAUserRole
                .get(tabName);

        return columnsApplicableToTab;
    }
}
