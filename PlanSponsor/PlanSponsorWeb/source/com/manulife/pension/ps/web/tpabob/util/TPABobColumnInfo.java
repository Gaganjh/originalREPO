package com.manulife.pension.ps.web.tpabob.util;

import java.io.Serializable;

/**
 * This class will hold the Column Info for TPA Bob report.
 * 
 * @author HArlomte
 * 
 */
public class TPABobColumnInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String columnId;

    private String columnTitle;

    private Boolean isEnabled;

    /**
     * Constructor.
     */
    public TPABobColumnInfo() {

    }

    /**
     * Constructor.
     * 
     * @param columnId
     * @param columnTitle
     * @param isEnabled
     */
    public TPABobColumnInfo(String columnId, String columnTitle, Boolean isEnabled) {
        this.columnId = columnId;
        this.columnTitle = columnTitle;
        this.isEnabled = isEnabled;
    }
    
    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    
}
