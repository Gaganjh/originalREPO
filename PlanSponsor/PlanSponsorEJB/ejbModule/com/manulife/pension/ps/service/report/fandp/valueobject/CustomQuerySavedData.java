package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * This data structure is used by the Funds and Performance section of Broker
 * and TPA. It allows users to retrieve saved lists of Custom Query rows, for
 * the "My Saved Custom Queries" feature. This specialization overrides a method
 * in the base class UserSavedData to custom parse the data for this particular
 * type of UserSavedData.
 * 
 * @author Mark Eldridge
 * @version initial (Feb 2009)
 * @see UserSavedData
 **/

public class CustomQuerySavedData extends UserSavedData implements Serializable {
    private static final long serialVersionUID = -3007015170068838816L;

    /**
     * Parses query data, which is row-delimited AND field delimited. It places
     * the data in the parsedData structure which is part of the super class.
     * Each row of the parsedData represents a row of the query. Each value
     * object in the ArrayList will have individual fields for each parameter of
     * the query.
     * 
     * No exceptions are thrown. Empty parsedData object means nothing could be
     * retrieved. (To avoid crashing the page).
     **/
    @Override
    protected void populateParsedData() {
        StringTokenizer st = new StringTokenizer(this.getDelimtedData(),
                USER_DATA_ROW_DELIMITER);
        parsedData = new ArrayList<Object>();

        try {
            while (st.hasMoreElements()) {
                CustomQueryRow queryRow = createQueryRow(st.nextToken());
                parsedData.add(queryRow);

            }
        } catch (Exception e) {
            // If we fail to parse for whatever reason (eg st=null) we just
            // don't populate anything to the parsedData object.
        }
    }

    /**
     * Creates a CustomQueryRow value object given a String object containing
     * the row of delimited data.
     * 
     * @param delimitedRow - the String representing 1 row of User Saved Custom
     *            Query data.
     * @return CustomQueryRow - the value object populated with each token
     *         parsed from the provided string.
     */
    @SuppressWarnings("finally")
    private CustomQueryRow createQueryRow(String delimitedRow) {

        // initializes as blank values, in case we can't populate it due to
        // exception.
        CustomQueryRow queryRow = new CustomQueryRow();

        try {
            StringTokenizer st = new StringTokenizer(delimitedRow,
                    USER_DATA_FIELD_DELIMITER);

            if (st.countTokens() != 6) {
                // Need exactly 6 elements, otherwise return an empty row now.
                return queryRow;
            }

            String logic = StringUtils.replace(st.nextToken(), "null", "");
            String leftBracket = StringUtils.replace(st.nextToken(), "null", "");
            String fieldId = StringUtils.replace(st.nextToken(), "null", "");
            String operator = StringUtils.replace(st.nextToken(), "null", "");
            String value = StringUtils.replace(st.nextToken(), "null", "");
            String rightBracket = StringUtils.replace(st.nextToken(), "null", "");

            queryRow.setLogic(logic);
            queryRow.setLeftBracket(leftBracket);
            queryRow.setOperator(operator);
            queryRow.setValue(value);
            queryRow.setRightBracket(rightBracket);
            // doing this one last in case of type conversion exception.
            // Want to get as much of the queryRow populated with strings before
            // attempting this.
            queryRow.setFieldId(new Integer(fieldId).intValue());
            return queryRow;
        } catch (Exception e) {
            // No need to handle.
            // Sending back an empty VO for the buggered up row.
        } finally {
            return queryRow;
        }
    }

}
