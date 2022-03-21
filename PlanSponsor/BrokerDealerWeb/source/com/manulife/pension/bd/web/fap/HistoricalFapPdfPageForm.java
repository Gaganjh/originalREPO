package com.manulife.pension.bd.web.fap;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the form bean for Historical FAP page
 * 
 * @author Siby Thomas
 * 
 */
public class HistoricalFapPdfPageForm extends AutoForm {

    private static final long serialVersionUID = 1L;

    private String includeNml;
    private String region;

    /**
     * gets the include NML indicator
     * 
     * @return String
     */
    public String getIncludeNml() {
        return includeNml;
    }

    /**
     * sets the include NML indicator
     * 
     * @param String includeNml
     */
    public void setIncludeNml(String includeNml) {
        this.includeNml = includeNml;
    }

    /**
     * gets the region
     * 
     * @return String
     */
    public String getRegion() {
        return region;
    }

    /**
     * sets the the region
     * 
     * @param String region
     */
    public void setRegion(String region) {
        this.region = region;
    }
}
