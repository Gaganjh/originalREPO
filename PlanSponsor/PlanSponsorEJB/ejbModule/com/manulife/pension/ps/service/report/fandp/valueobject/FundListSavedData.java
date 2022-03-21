package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This data structure is used by the Funds and Performance section of Broker
 * and TPA. It allows users to retrieve saved lists of funds, for the
 * "My Saved Lists" feature. This specialization overrides a method in the base
 * class UserSavedData to custom parse the data for this particular type of
 * UserSavedData.
 * 
 * @author Mark Eldridge
 * @version initial (Feb 2009)
 * @see UserSavedData
 **/
public class FundListSavedData extends UserSavedData implements Serializable {

    private static final long serialVersionUID = -5728912010220940491L;

    /**
     * Parses fund ids that are delimited by a pipe character and places them in
     * the parsedData structure which is part of the super class.
     * 
     * No exceptions are thrown. Emtpy parsedData object means nothing could be
     * retreived. (To avoid crashing the page).
     **/
    @Override
    protected void populateParsedData() {
        StringTokenizer st = new StringTokenizer(this.getDelimtedData(),
                USER_DATA_FIELD_DELIMITER);
        parsedData = new ArrayList<Object>();

        try {
            while (st.hasMoreElements()) {
                parsedData.add(st.nextToken());
            }
        } catch (Exception e) {
            // If we fail to parse for whatever reason (eg st=null) we just
            // don't populate anything to the parsedData object.
        }
    }
}
