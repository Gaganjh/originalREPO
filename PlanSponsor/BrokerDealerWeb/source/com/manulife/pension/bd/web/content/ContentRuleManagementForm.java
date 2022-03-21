package com.manulife.pension.bd.web.content;

import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the form bean for ContentRuleManagementAction.java
 * 
 * @author Ilamparithi
 * 
 */
public class ContentRuleManagementForm extends AutoForm {

    private static final long serialVersionUID = 1L;

    private String contentIdSortOrder;

    private String ruleSortOrder;

    private String sortClass;

    private String sortProperty;

    private List<ContentRuleDisplayBean> contentRules;

    /**
     * Returns the sort order of content id column
     * 
     * @return the contentIdSortOrder
     */
    public String getContentIdSortOrder() {
        return contentIdSortOrder;
    }

    /**
     * Sets the sort order of content id
     * 
     * @param contentIdSortOrder the contentIdSortOrder to set
     */
    public void setContentIdSortOrder(String contentIdSortOrder) {
        this.contentIdSortOrder = contentIdSortOrder;
    }

    /**
     * Returns the sort order of rule column
     * 
     * @return the ruleSortOrder
     */
    public String getRuleSortOrder() {
        return ruleSortOrder;
    }

    /**
     * Sets the sort order of rule column
     * 
     * @param ruleSortOrder the ruleSortOrder to set
     */
    public void setRuleSortOrder(String ruleSortOrder) {
        this.ruleSortOrder = ruleSortOrder;
    }

    /**
     * Returns the div class attribute of sort type
     * 
     * @return the sortClass
     */
    public String getSortClass() {
        return sortClass;
    }

    /**
     * Sets the div class attribute of sort type
     * 
     * @param sortClass the sortClass to set
     */
    public void setSortClass(String sortClass) {
        this.sortClass = sortClass;
    }

    /**
     * Returns the property on which the list should be sorted
     * 
     * @return the sortProperty
     */
    public String getSortProperty() {
        return sortProperty;
    }

    /**
     * Sets the property on which the list should be sorted
     * 
     * @param sortProperty the sortProperty to set
     */
    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    /**
     * Returns the list of content rules
     * 
     * @return the contentRules
     */
    public List<ContentRuleDisplayBean> getContentRules() {
        return contentRules;
    }

    /**
     * Sets the list of content rules
     * 
     * @param contentRules the contentRules to set
     */
    public void setContentRules(List<ContentRuleDisplayBean> contentRules) {
        this.contentRules = contentRules;
    }

}
