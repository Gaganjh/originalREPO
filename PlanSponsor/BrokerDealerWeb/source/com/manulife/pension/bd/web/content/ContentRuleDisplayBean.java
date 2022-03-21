package com.manulife.pension.bd.web.content;

import java.util.List;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

/**
 * @author Ilamparithi
 * 
 *         A bean to display the firm content restriction rules
 * 
 */
public class ContentRuleDisplayBean extends BaseSerializableCloneableObject {
    private static final long serialVersionUID = 1L;

    private int contentId;

    private String name;

    private String category;

    private String ruleType;

    private List<? extends BrokerDealerFirm> brokerDealerFirms;

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
     * Returns the content name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the content name
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the content category
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the content category
     * 
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
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
     * Return a list of objects of type BrokerDealerFirm
     * 
     * @return the brokerDealerFirms
     */
    public List<? extends BrokerDealerFirm> getBrokerDealerFirms() {
        return brokerDealerFirms;
    }

    /**
     * Sets a list of objects of type BrokerDealerFirm
     * 
     * @param brokerDealerFirms the brokerDealerFirms to set
     */
    public void setBrokerDealerFirms(List<? extends BrokerDealerFirm> brokerDealerFirms) {
        this.brokerDealerFirms = brokerDealerFirms;
    }

}
