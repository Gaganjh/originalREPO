package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.Map;

/**
 * This class will be a store for information about the Columns for BOB.
 * 
 * The column information is obtained from XML file.
 * 
 * @author harlomte
 * 
 */
public class BOBColumnStore {
    
    private Map<String, BOBColumnsApplicableToRole> applicableColumns;

    public Map<String, BOBColumnsApplicableToRole> getApplicableColumns() {
        return applicableColumns;
    }

    public void setApplicableColumns(Map<String, BOBColumnsApplicableToRole> applicableColumns) {
        this.applicableColumns = applicableColumns;
    }

    /**
     * This method gives back columns applicable for a given tab, user type.
     * 
     * @param versionInfo - the user type.
     * @param tabName - the tab name.
     * @return - BOBColumnsApplicableToTab object containing columns information.
     */
    public BOBColumnsApplicableToTab getApplicableColumnsforVersionAndTab(String versionInfo,
            String tabName) {

        BOBColumnsApplicableToRole applicableColumnsForaRole = this.applicableColumns
                .get(versionInfo);
        if (applicableColumnsForaRole == null) {
            return null;
        }

        return applicableColumnsForaRole.getApplicableColumnsForTab(tabName);
    }
}
