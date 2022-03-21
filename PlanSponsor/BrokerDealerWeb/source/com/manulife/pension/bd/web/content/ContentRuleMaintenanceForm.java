package com.manulife.pension.bd.web.content;

import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

/**
 * This is a form bean for ContentRuleMaintenanceAction.java
 * 
 * @author Ilamparithi
 * 
 */
public class ContentRuleMaintenanceForm extends AutoForm {

	private static final long serialVersionUID = 1L;
	
	
	private String query;
	private String firmType;
	private List<? extends BrokerDealerFirm> matchingFirms;
	
	public List<? extends BrokerDealerFirm> getMatchingFirms() {
		return matchingFirms;
	}
	public void setMatchingFirms(List<? extends BrokerDealerFirm> matchingFirms) {
		this.matchingFirms = matchingFirms;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getFirmType() {
		return firmType;
	}
	public void setFirmType(String firmType) {
		this.firmType = firmType;
	}
	
	

    private int contentId;

    ContentRuleDisplayBean contentRule;

    private String firmListStr;

    private String ruleType;

    private String task;

    private String selectedFirmId;

    private String selectedFirmName;

    /**
     * Returns the content id
     * 
     * @return the contentId
     */
    public int getContentId() {
        return contentId;
    }

    /**
     * Sets the content id
     * 
     * @param contentId the contentId to set
     */
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    /**
     * Returns the content rule display bean
     * 
     * @return the contentRule
     */
    public ContentRuleDisplayBean getContentRule() {
        return contentRule;
    }

    /**
     * Sets the content rule display bean
     * 
     * @param contentRule the contentRule to set
     */
    public void setContentRule(ContentRuleDisplayBean contentRule) {
        this.contentRule = contentRule;
    }

    /**
     * Returns a comma separated list of firms
     * 
     * @return the firmListStr
     */
    public String getFirmListStr() {
        return firmListStr;
    }

    /**
     * Sets the comma separted list of firms
     * 
     * @param firmListStr the firmListStr to set
     */
    public void setFirmListStr(String firmListStr) {
        this.firmListStr = firmListStr;
    }

    /**
     * Returns the rule type
     * 
     * @return the ruleType
     */
    public String getRuleType() {
        return ruleType;
    }

    /**
     * Sets the rule type
     * 
     * @param ruleType the ruleType to set
     */
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * Returns the task (add/edit)
     * 
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets the task (add/edit)
     * 
     * @param task the task to set
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Returns the selected firm id. This is for the Auto Complete widget.
     * 
     * @return the selectedFirmId
     */
    public String getSelectedFirmId() {
        return selectedFirmId;
    }

    /**
     * Sets the selected firm id. This is for the Auto Complete widget.
     * 
     * @param selectedFirmId the selectedFirmId to set
     */
    public void setSelectedFirmId(String selectedFirmId) {
        this.selectedFirmId = selectedFirmId;
    }

    /**
     * Returns the selected firm name. This is for the Auto Complete widget.
     * 
     * @return the selectedFirmName
     */
    public String getSelectedFirmName() {
        return selectedFirmName;
    }

    /**
     * Sets the selected firm name. This is for the Auto Complete widget.
     * 
     * @param selectedFirmName the selectedFirmName to set
     */
    public void setSelectedFirmName(String selectedFirmName) {
        this.selectedFirmName = selectedFirmName;
    }

}
