package com.manulife.pension.service.withdrawal.valueobject;

import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * This class represents 1 activity history event. This intention of this class is: 1. to be used in
 * the web tier, as a VO that represents 1 row in a list 2. to be created based on ActivityDetail or
 * ActivityDynamicDetail records.
 * 
 * @author Dennis
 * 
 */

public class Activity extends BaseSerializableCloneableObject {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    private Integer itemNo;

    private Integer secondaryNo;

    private String secondaryName;

    private String itemName;

    private String systemOfRecordValue;

    private String originalValue;

    private String currentValue;

    private String changedBy;

    private Date lastUpdated;

    private Integer lastUpdateUserProfileId;

    private Integer sortOrder;

    private Boolean showUserIdAndLastUpdated;

    private boolean showLastUpdatedTimeAsNa;

    private boolean hasSavedValue;
    
    private String internalUserName;

    /**
     * @return the true if we show the user id and the time stamp
     */
    public final Boolean getShowUserIdAndLastUpdated() {
        return showUserIdAndLastUpdated;
    }

    /**
     * @param showUserIdAndLastUpdated true if we need to show the user id and the time stamp
     */
    public final void setShowUserIdAndLastUpdated(final Boolean showUserIdAndLastUpdated) {
        this.showUserIdAndLastUpdated = showUserIdAndLastUpdated;
    }

    /**
     * @return the sort order
     */
    public final Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder the sort order
     */
    public final void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @return the user who last updated the record
     */
    public final Integer getLastUpdateUserProfileId() {
        return lastUpdateUserProfileId;
    }

    /**
     * @param lastUpdateUserProfileId the user who last updated the record
     */
    public final void setLastUpdateUserProfileId(final Integer lastUpdateUserProfileId) {
        this.lastUpdateUserProfileId = lastUpdateUserProfileId;
    }

    /**
     * @return the display name
     */
    public final String getChangedBy() {
        return changedBy;
    }

    /**
     * @param changedBy the display name
     */
    public final void setChangedBy(final String changedBy) {
        this.changedBy = changedBy;
    }

    /**
     * @return the current value
     */
    public final String getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue the current value
     */
    public final void setCurrentValue(final String currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return the item name
     */
    public final String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the item name
     */
    public final void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the item number
     */
    public final Integer getItemNo() {
        return itemNo;
    }

    /**
     * @param itemNo the imtem number
     */
    public final void setItemNo(final Integer itemNo) {
        this.itemNo = itemNo;
    }

    /**
     * @return the last updated date
     */
    public final Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the last updated date
     */
    public final void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return the original value
     */
    public final String getOriginalValue() {
        return originalValue;
    }

    /**
     * @param originalValue the original value
     */
    public final void setOriginalValue(final String originalValue) {
        this.originalValue = originalValue;
    }

    /**
     * @return the system of record value
     */
    public final String getSystemOfRecordValue() {
        return systemOfRecordValue;
    }

    /**
     * @param systemOfRecordValue the system of record value
     */
    public final void setSystemOfRecordValue(final String systemOfRecordValue) {
        this.systemOfRecordValue = systemOfRecordValue;
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
    public final Integer getSecondaryNo() {
        return secondaryNo;
    }

    /**
     * @param secondaryNo the secondary number
     */
    public final void setSecondaryNo(final Integer secondaryNo) {
        this.secondaryNo = secondaryNo;
    }

    /**
     * @return true if the user id and last updated time stamp are not the original values
     */
    public boolean getHasSavedValue() {
        return hasSavedValue;
    }

    /**
     * 
     * @param hasSavedValue set to true if the value object is using non original values
     */
    public void setHasSavedValue(final boolean hasSavedValue) {
        this.hasSavedValue = hasSavedValue;
    }

    /**
     * This returns true if the request is Participant initiated and we want to display time as n/a
     * 
     * @return
     */
    public boolean getShowLastUpdatedTimeAsNa() {
        return showLastUpdatedTimeAsNa;
    }

    /**
     * is set to true if the the request is Participant initiated and we want to display time as n/a
     * 
     * @param showLastUpdatedTimeAsNa
     */
    public void setShowLastUpdatedTimeAsNa(boolean showLastUpdatedTimeAsNa) {
        this.showLastUpdatedTimeAsNa = showLastUpdatedTimeAsNa;
    }

	public String getInternalUserName() {
		return internalUserName;
	}

	public void setInternalUserName(String internalUserName) {
		this.internalUserName = internalUserName;
	}

}
