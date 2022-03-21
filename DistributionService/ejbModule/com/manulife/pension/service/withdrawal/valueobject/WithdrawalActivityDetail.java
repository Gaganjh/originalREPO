package com.manulife.pension.service.withdrawal.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;

/**
 * @author Dennis
 * 
 */
public class WithdrawalActivityDetail extends BaseSerializableCloneableObject implements
        ActivityDetail {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    private Integer itemNumber;

    private String actionCode;

    private String value;

    private Timestamp lastUpdated;

    private Integer lastUpdatedById;

    /**
     * @deprecated Use getType() instead.
     * @return the action code
     */
    public final String getActionCode() {
        return this.actionCode;
    }

    /**
     * @deprecated Use setType() instead.
     * @param actionCode the action cocde
     */
    public final void setActionCode(final String actionCode) {
        this.actionCode = actionCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#getItemNumber()
     */
    public final Integer getItemNumber() {
        return this.itemNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#setItemNumber(java.lang
     * .Integer)
     */
    public final void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#getLastUpdatedById()
     */
    public final Integer getLastUpdatedById() {
        return this.lastUpdatedById;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#setLastUpdatedById(java
     * .lang.Integer)
     */
    public final void setLastUpdatedById(final Integer lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#getLastUpdated()
     */
    public final Timestamp getLastUpdated() {
        return this.lastUpdated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#setLastUpdated(java.sql
     * .Timestamp)
     */
    public final void setLastUpdated(final Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#getValue()
     */
    public final String getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.service.withdrawal.valueobject.ActivityDetail#setValue(java.lang.String)
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    public String getTypeCode() {
        return getActionCode();
    }

    public void setTypeCode(String type) {
        setActionCode(type);
    }

}
