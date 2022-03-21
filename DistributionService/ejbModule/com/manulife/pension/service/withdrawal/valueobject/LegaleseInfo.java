package com.manulife.pension.service.withdrawal.valueobject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

/**
 * This is the legalese data transfer object, so it holds data about legalese.
 * 
 * @author kuthiha
 */
public class LegaleseInfo extends BaseWithdrawal {

    /**
     * FIELDS_TO_EXCLUDE_FROM_LOGGING.
     */
    private static final String[] FIELDS_TO_EXCLUDE_FROM_LOGGING = { "legaleseText",
            "creatorUserProfileId", "createdTimestamp" };

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private Integer contentId;

    private int contentVersionNumber;

    private String cmaSiteCode;

    private String legaleseText;

    private Integer creatorUserProfileId;

    private Timestamp createdTimestamp;

    /**
     * Default Constructor.
     * 
     * @param contentId The content ID of this legalese text.
     * @param legaleseText The text String of the legalese.
     * @param userId The user that is looking up the legalese.
     */
    public LegaleseInfo(final Integer contentId, final String legaleseText, final Integer userId) {
        this.contentId = contentId;
        this.legaleseText = legaleseText;
        creatorUserProfileId = userId;
    }

    /**
     * Default Constructor.
     * 
     * @param legaleseText The legalese text.
     */
    public LegaleseInfo(final String legaleseText) {
        this.legaleseText = legaleseText;
    }

    /**
     * @return the contentId
     */
    public Integer getContentId() {
        return contentId;
    }

    /**
     * @return the contentVersionNumber
     */
    public int getContentVersionNumber() {
        return contentVersionNumber;
    }

    /**
     * @return the createdTimestamp
     */
    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * @return the creatorUserProfileId
     */
    public Integer getCreatorUserProfileId() {
        return creatorUserProfileId;
    }

    /**
     * @return the legaleseText
     */
    public String getLegaleseText() {
        return legaleseText;
    }

    /**
     * @param contentId the contentId to set
     */
    public void setContentId(final Integer contentId) {
        this.contentId = contentId;
    }

    /**
     * @param contentVersionNumber the contentVersionNumber to set
     */
    public void setContentVersionNumber(final int contentVersionNumber) {
        this.contentVersionNumber = contentVersionNumber;
    }

    /**
     * @param createdTimestamp the createdTimestamp to set
     */
    public void setCreatedTimestamp(final Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * @param creatorUserProfileId the creatorUserProfileId to set
     */
    public void setCreatorUserProfileId(final Integer creatorUserProfileId) {
        this.creatorUserProfileId = creatorUserProfileId;
    }

    /**
     * @param legaleseText the legaleseText to set
     */
    public void setLegaleseText(final String legaleseText) {
        this.legaleseText = legaleseText;
    }

    /**
     * @return the cmaSiteCode
     */
    public String getCmaSiteCode() {
        return cmaSiteCode;
    }

    /**
     * @param cmaSiteCode the cmaSiteCode to set
     */
    public void setCmaSiteCode(final String cmaSiteCode) {
        this.cmaSiteCode = cmaSiteCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {
        return CollectionUtils.isNotEmpty(getAlertCodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {
        return CollectionUtils.isNotEmpty(getErrorCodes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {
        return CollectionUtils.isNotEmpty(getWarningCodes());
    }

    /**
     * @return String[] - The fieldNamesToExcludeFromLogging.
     */
    public Collection<String> fieldNamesToExcludeFromLogging() {

        final Collection<String> toExclude = new ArrayList<String>(
                FIELDS_TO_EXCLUDE_FROM_LOGGING.length);

        CollectionUtils.addAll(toExclude, FIELDS_TO_EXCLUDE_FROM_LOGGING);

        toExclude.addAll(super.fieldNamesToExcludeFromLogging());

        return toExclude;
    }

}
