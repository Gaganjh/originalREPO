package com.manulife.pension.bd.web.bob.blockOfBusiness.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.util.LabelInfoBean;

/**
 * This class contains the column information specific for a particular tab.
 * 
 * The information in this class is populated by reading the XML file that contains the column info.
 * 
 * @author harlomte
 * 
 */
public class BOBColumnsApplicableToTab {
    private Map<String, LabelInfoBean> applicableColumnsForTab;

    public Map<String, LabelInfoBean> getApplicableColumnsForTab() {
        return applicableColumnsForTab;
    }

    public void setApplicableColumnsForTab(Map<String, LabelInfoBean> applicableColumnsForTab) {
        this.applicableColumnsForTab = applicableColumnsForTab;
    }
    
    /**
     * This method checks if a given column is applicable or not.
     * 
     * @param columnId - the column name
     * @return - "true", if the column is applicable, else "false".
     */
    public Boolean isColumnEnabled(String columnId) {
        return applicableColumnsForTab.get(columnId) == null ? Boolean.FALSE
                : applicableColumnsForTab.get(columnId).getEnabled();
    }
    
    /**
     * This method checks if a given column is applicable for CSV or not.
     * 
     * @param columnId - the column name
     * @return - "true", if the column is applicable, else "false".
     */
    public Boolean isColumnEnabledForPdfAndCsv(String columnId) {
        return applicableColumnsForTab.get(columnId) == null ? Boolean.FALSE
                : (applicableColumnsForTab.get(columnId).getEnabled() || 
                  (applicableColumnsForTab.get(columnId).getDisplayInPdfAndCsv() == null ? false : applicableColumnsForTab.get(columnId).getDisplayInPdfAndCsv()));
    }
    
    /**
     * This method gets the "Title" of a given column.
     * 
     * @param columnId - the column name
     * @return - the title of the given column name.
     */
    public String getTitle(String columnId) {
        return applicableColumnsForTab.get(columnId) == null ? BDConstants.SPACE_SYMBOL
                : applicableColumnsForTab.get(columnId).getTitle();
    }
    
    /**
     * This method gets the "Title" of a given column.
     * 
     * @param columnId - the column name
     * @return - the title of the given column name.
     */
    public String getPDFTitle(String columnId) {
        String pdfTitle =  applicableColumnsForTab.get(columnId) == null ? BDConstants.SPACE_SYMBOL
                : applicableColumnsForTab.get(columnId).getPDFTitle();
        if(StringUtils.isEmpty(pdfTitle)) {
        	pdfTitle = getTitle(columnId);
        }
        return pdfTitle;
    }
    
    public BOBColumnsApplicableToTab createCopy() {
        
        final HashMap<String, LabelInfoBean> mapCopy = new HashMap<String, LabelInfoBean>();
        
        for (final Map.Entry<String, LabelInfoBean> column : applicableColumnsForTab.entrySet()) {
            
            mapCopy.put(column.getKey(), column.getValue().createCopy());
            
        }
        
        final BOBColumnsApplicableToTab objCopy = new BOBColumnsApplicableToTab();
        objCopy.setApplicableColumnsForTab(mapCopy);
        
        return objCopy;
        
    }
    
}
