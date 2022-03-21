package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class houses two sets of user saved data for the Funds and Performance
 * page. "My Saved Fund Lists" and "My Custom Queries" are both held in the
 * HashTables shown below. They are indexed on their respective Names.
 * 
 * HashTables are used to preserve the order coming from the database, while
 * allowing for lookups based on ID.
 * 
 * @author eldrima
 * 
 */
public class UserSavedDataLists implements Serializable {

    private static final long serialVersionUID = -9072438455025075430L;

    Map<String, CustomQuerySavedData> customQueries = new LinkedHashMap<String, CustomQuerySavedData>();

    Map<String, FundListSavedData> fundLists = new LinkedHashMap<String, FundListSavedData>();

    public void addSavedList(UserSavedData userSavedData) {
        if (userSavedData.getClass().getName() == FundListSavedData.class
                .getName()) {
            fundLists.put(userSavedData.getName(),
                    (FundListSavedData) userSavedData);
        } else if (userSavedData.getClass().getName() == CustomQuerySavedData.class
                .getName()) {
            customQueries.put(userSavedData.getName(),
                    (CustomQuerySavedData) userSavedData);
        }
    }

    public FundListSavedData getFundList(String name) {
        return fundLists.get(name);
    }

    public CustomQuerySavedData getCustomQuery(String name) {
        return customQueries.get(name);
    }

    public Map<String, CustomQuerySavedData> getCustomQueries() {
        return customQueries;
    }

    public Map<String, FundListSavedData> getFundLists() {
        return fundLists;
    }
}
