package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.Map;

/**
 * This class will hold all the Filter information retrieved from XML file.
 * 
 * @author harlomte
 * 
 */
public class BOBFilterStore {

    // This Map holds the Filters applicable for all tabs... The key is the tab Name and the value
    // is the Filter Information for that particular tab.
    private Map<String, BOBFilterMap> filtersApplicableToTab;

    /**
     * This method returns a Map<tabName, filters applicable for that tab> for all the tabs.
     * 
     * @return
     */
    public Map<String, BOBFilterMap> getFiltersApplicableToTab() {
        return filtersApplicableToTab;
    }

    public void setFiltersApplicableToTab(Map<String, BOBFilterMap> filtersApplicableToTab) {
        this.filtersApplicableToTab = filtersApplicableToTab;
    }

    /**
     * This method returns back the Filters applicable for a given tab.
     * 
     * @param tabName - The tab Name.
     * @return - Filters Information applicable for the given tab.
     */
    public BOBFilterMap getAllFiltersForTab(String tabName) {
        return filtersApplicableToTab.get(tabName);
    }
}
