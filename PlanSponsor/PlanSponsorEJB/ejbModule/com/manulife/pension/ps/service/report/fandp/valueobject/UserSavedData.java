package com.manulife.pension.ps.service.report.fandp.valueobject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This abstract data structure is used by the Funds and Performance section of
 * Broker and TPA. It allows users to retrieve saved "lists of funds" or
 * "custom query data", for the "My Saved Lists" and "My Custom Queries"
 * features. This class is overridden by specific implementations of the value
 * object
 * 
 * @author Mark Eldridge
 * @version initial (Feb 2009)
 * @see FundListSavedData
 * @see CustomQuerySavedData
 **/

public abstract class UserSavedData implements Serializable {

    private static final long serialVersionUID = 1601945220009119074L;

    protected static final String USER_DATA_FIELD_DELIMITER = "|";

    protected static final String USER_DATA_ROW_DELIMITER = "[$$]";

    private int id;

    private String name = "";

    private String delimtedData = "";

    private String listType = "";

    protected ArrayList<Object> parsedData = null;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelimtedData() {
        return delimtedData;
    }

    public void setDelimtedData(String delimtedData) {
        this.delimtedData = delimtedData;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    /**
     * @return Returns the array list which contains all the data in a consumable
     *         manner. For "Saved Fund Lists" the ArrayList contains a fund id
     *         (String) in each element. For "Saved Queries" the ArrayList
     *         contains a CustomQueryRow value object.
     * 
     *         We only parse data if we are asked for it, not when it is loaded.
     *         (Lazy parsing)
     */
    public ArrayList<Object> getDataArrayList() {
        if (this.parsedData == null) {
            populateParsedData();
        }
        return parsedData;
    }

    /**
     * This method is overridden by specific implementations of this value
     * object type.
     * 
     * @see CustomQuerySavedData
     * @see FundListSavedData
     */
    protected abstract void populateParsedData();
    
}
