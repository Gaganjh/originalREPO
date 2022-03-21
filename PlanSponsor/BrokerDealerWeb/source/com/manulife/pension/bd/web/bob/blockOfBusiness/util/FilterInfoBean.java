package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import com.manulife.pension.platform.web.util.LabelInfoBean;

/**
 * This class will hold the Filter Information: ID, Title, Enabled taken from the FilterInfo.xml
 * file. It will also hold the Filter Value entered by the user in the JSP page.
 * 
 * @author harlomte
 * 
 */
public class FilterInfoBean implements java.io.Serializable {
    
    private static final long serialVersionUID = -1875238908935944246L;

    // This varaible holds the Filter Information: ID, Title, Enabled indicator.
    private LabelInfoBean filterInfo;

    // This variable holds the filter Value entered by the User in JSP page..
    private String filterValue;

    /**
     * Constructor
     */
    public FilterInfoBean() {

    }

    /**
     * Constructor.
     * 
     * @param filterInfo
     * @param filterValue
     */
    public FilterInfoBean(LabelInfoBean filterInfo, String filterValue) {
        this.filterInfo = filterInfo;
        this.filterValue = filterValue;
    }
    
    public LabelInfoBean getFilterInfo() {
        return filterInfo;
    }

    public void setFilterInfo(LabelInfoBean filterInfo) {
        this.filterInfo = filterInfo;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}
