package com.manulife.pension.service.withdrawal.valueobject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.collections.collection.UnmodifiableCollection;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.common.MessageCategory;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.log.ReflectionToLogBuilder;

/**
 * BaseWithdrawal provides a common base for all the withdrawal value objects.
 * 
 * @author Dennis_Snowdon
 * @author glennpa
 * @version 1.6 2006/09/11 14:51:34
 */
public abstract class BaseWithdrawal extends BaseSerializableCloneableObject {

    /**
     * For error reporting, this holds the error codes.
     */
    private Collection<WithdrawalMessage> errorCodes;

    /**
     * For warning reporting, this holds the warning codes.
     */
    private Collection<WithdrawalMessage> warningCodes;

    /**
     * For alert reporting, this holds the alert codes.
     */
    private Collection<WithdrawalMessage> alertCodes;

    /**
     * User profile id of the creator.
     */
    private Integer createdById;

    /**
     * User profile id of the updater.
     */
    private Integer lastUpdatedById;

    /**
     * timestamp for when it was created.
     */
    private Timestamp created;

    /**
     * timestamp for when it was updated.
     */
    private Timestamp lastUpdated;

    /**
     * The submission id.
     */
    private Integer submissionId;

    public static final int ERROR_CODE_NO_ERROR = 0;

    /**
     * Default Constructor.
     */
    public BaseWithdrawal() {
        super();

        errorCodes = new ArrayList<WithdrawalMessage>();
        warningCodes = new ArrayList<WithdrawalMessage>();
        alertCodes = new ArrayList<WithdrawalMessage>();
    }

    /**
     * @return the errorCodes
     */
    public Collection<WithdrawalMessage> getErrorCodes() {
        return errorCodes;
    }

    /**
     * @param errorCodes the errorCodes to set
     */
    public void setErrorCodes(final Collection<WithdrawalMessage> errorCodes) {
        this.errorCodes = errorCodes;
    }

    /**
     * @return the warningCodes
     */
    public Collection<WithdrawalMessage> getWarningCodes() {
        return warningCodes;
    }

    /**
     * @param warningCodes the warningCodes to set
     */
    public void setWarningCodes(final Collection<WithdrawalMessage> warningCodes) {
        this.warningCodes = warningCodes;
    }

    /**
     * @return the alertCodes
     */
    public Collection<WithdrawalMessage> getAlertCodes() {
        return alertCodes;
    }

    /**
     * @param alertCodes the alertCodes to set
     */
    public void setAlertCodes(final Collection<WithdrawalMessage> alertCodes) {
        this.alertCodes = alertCodes;
    }

    /**
     * This method returns the contents of all the messages (errors, warnings, and alerts) grouped
     * together in no specific order.
     * 
     * @return Collection - The withdrawal messages.
     */
    public Collection<WithdrawalMessage> getMessages() {
        final Collection<WithdrawalMessage> messages = new ArrayList<WithdrawalMessage>(
                getErrorCodes().size() + getWarningCodes().size() + getAlertCodes().size());

        messages.addAll(getErrorCodes());
        messages.addAll(getWarningCodes());
        messages.addAll(getAlertCodes());

        return (Collection<WithdrawalMessage>) UnmodifiableCollection.decorate(messages);
    }

    /**
     * Adds a {@link WithdrawalMessage} to the appropriate collection (Errors, Warnings, or Alerts),
     * based on it's {@link MessageCategory}.
     * 
     * @see MessageCategory
     * 
     * @param withdrawalMessage The {@link WithdrawalMessage} to add to the collection.
     */
    public void addMessage(final WithdrawalMessage withdrawalMessage) {
        final MessageCategory messageCategory = withdrawalMessage.getWithdrawalMessageType()
                .getMessageCategory();
        switch (messageCategory) {
            case ERROR:
                getErrorCodes().add(withdrawalMessage);
                break;

            case WARNING:
                getWarningCodes().add(withdrawalMessage);
                break;

            case ALERT:
                getAlertCodes().add(withdrawalMessage);
                break;

            default:
                throw new RuntimeException(
                        "Unable to determine an appropriate Message Category for ["
                                + withdrawalMessage.toString() + "]");
        }

    }

    /**
     * Adds a {@link WithdrawalMessage} to the appropriate collection (Errors, Warnings, or Alerts),
     * based on it's {@link MessageCategory} if it does not already exist.
     * 
     * @see MessageCategory
     * 
     * @param withdrawalMessage The {@link WithdrawalMessage} to add to the collection.
     */
    public void addUniqueMessage(final WithdrawalMessage withdrawalMessage) {

        if (!CollectionUtils.exists(getMessages(), new WithdrawalMessageTypePredicate(
                withdrawalMessage.getWithdrawalMessageType()))) {
            addMessage(withdrawalMessage);
        }
    }

    /**
     * Queries the object graph for the existence of any error codes.
     * 
     * @return boolean - True if error codes exist in this object or a nested object.
     */
    public abstract boolean doErrorCodesExist();

    /**
     * Queries the object graph for the existence of any warning codes.
     * 
     * @return boolean - True if warning codes exist in this object or a nested object.
     */
    public abstract boolean doWarningCodesExist();

    /**
     * Queries the object graph for the existence of any alert codes.
     * 
     * @return boolean - True if alert codes exist in this object or a nested object.
     */
    public abstract boolean doAlertCodesExist();

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        final Object myClone = super.clone();
        final BaseWithdrawal myBaseWithdrawal = (BaseWithdrawal) myClone;

        myBaseWithdrawal.setErrorCodes((Collection<WithdrawalMessage>) TransformerUtils
                .cloneTransformer().transform(getErrorCodes()));
        myBaseWithdrawal.setWarningCodes((Collection<WithdrawalMessage>) TransformerUtils
                .cloneTransformer().transform(getWarningCodes()));
        myBaseWithdrawal.setAlertCodes((Collection<WithdrawalMessage>) TransformerUtils
                .cloneTransformer().transform(getAlertCodes()));

        return myBaseWithdrawal;
    }

    /**
     * @return Gets the created timestamp for this object
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * @param created sets the created timestamp for this object This property is intended to be
     *            READ-ONLY. Do not set this property unless you are a Dao.
     */
    public void setCreated(final Timestamp created) {
        this.created = created;
    }

    /**
     * @return gets the user profile id of the creator of this object
     */
    public Integer getCreatedById() {
        return createdById;
    }

    /**
     * @param createdById sets the user profile id of the creator of this object. This property is
     *            intended to be READ-ONLY. Do not set this property unless you are a Dao.
     */
    public void setCreatedById(final Integer createdById) {
        this.createdById = createdById;
    }

    /**
     * @return returns the last updated timestamp for this object
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated This property is intended to be READ-ONLY. Do not set this property unless
     *            you are a Dao.
     */
    public void setLastUpdated(final Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return returns the last updated user profile id for this object
     */
    public Integer getLastUpdatedById() {
        return lastUpdatedById;
    }

    /**
     * @param lastUpdatedById This property is intended to be READ-ONLY. Do not set this property
     *            unless you are a Dao.
     */
    public void setLastUpdatedById(final Integer lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    /**
     * @return returns the submission id for this object
     */
    public Integer getSubmissionId() {
        return submissionId;
    }

    /**
     * @param submissionId sets the submission id for this object
     */
    public void setSubmissionId(final Integer submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * Removes all messages from the error, warning and alert collections.
     */
    public void removeMessages() {
        errorCodes = new ArrayList<WithdrawalMessage>();
        warningCodes = new ArrayList<WithdrawalMessage>();
        alertCodes = new ArrayList<WithdrawalMessage>();
    }

    /**
     * This method is used to get a {@link String} representation of this object for logging. Note
     * that some fields are specifically filtered out for logging.
     * 
     * @return String - The {@link String} to be used for logging this object.
     */
    public String toLog() {
        return ReflectionToLogBuilder.toLogExclude(this, fieldNamesToExcludeFromLogging());
    }

    /**
     * @return String[] - The fieldNamesToExcludeFromLogging.
     */
    public Collection<String> fieldNamesToExcludeFromLogging() {

        Collection<String> list = new ArrayList<String>();

        list.add("fieldNamesToExcludeFromLogging");
        list.add("errorCodes");
        list.add("warningCodes");
        list.add("alertCodes");

        return list;
    }
}
