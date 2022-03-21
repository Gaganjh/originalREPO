package com.manulife.pension.service.distribution.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class ActivityDynamicDetail extends BaseSerializableCloneableObject implements ActivityDetail {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    private Integer itemNumber;

    private Integer secondaryNumber;

    private String secondaryName;

    private String typeCode;

    private String value;

    private Timestamp lastUpdated;

    private Integer lastUpdatedById;

    /**
     * @return the type code
     */
    public final String getTypeCode() {
        return this.typeCode;
    }

    /**
     * @param typeCode the type code
     */
    public final void setTypeCode(final String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @return the item number
     */
    public final Integer getItemNumber() {
        return this.itemNumber;
    }

    /**
     * @param itemNumber the item number
     */
    public final void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * @return the last updated by user id
     */
    public final Integer getLastUpdatedById() {
        return this.lastUpdatedById;
    }

    /**
     * @param lastUpdatedById the last updated by user id
     */
    public final void setLastUpdatedById(final Integer lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    /**
     * @return the last updated timestamp
     */
    public final Timestamp getLastUpdated() {
        return this.lastUpdated;
    }

    /**
     * @param lastUpdated the last updated timestamp
     */
    public final void setLastUpdated(final Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return this.value;
    }

    /**
     * @param value the value
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return the secondary name
     */
    public final String getSecondaryName() {
        return secondaryName;
    }

    /**
     * @param secondaryName the secondary name
     */
    public final void setSecondaryName(final String secondaryName) {
        this.secondaryName = secondaryName;
    }

    /**
     * @return the secondary number
     */
    public final Integer getSecondaryNumber() {
        return secondaryNumber;
    }

    /**
     * @param secondaryNumber the secondary number
     */
    public final void setSecondaryNumber(final Integer secondaryNumber) {
        this.secondaryNumber = secondaryNumber;
    }

}
