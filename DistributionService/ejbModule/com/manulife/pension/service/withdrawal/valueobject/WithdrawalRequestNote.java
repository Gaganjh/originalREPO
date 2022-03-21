package com.manulife.pension.service.withdrawal.valueobject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.distribution.valueobject.Note;

/**
 * WithdrawalRequestNote is the value object for the notes within a withdrawal request.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.3 2006/09/06 13:23:21
 */
public class WithdrawalRequestNote extends BaseWithdrawal implements Note {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private String noteTypeCode;

    private String note;

    /**
     * @return Returns the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * @return Returns the note in display format (escaped for HTML and JavaScript).
     */
    public String getNoteForDisplay() {
        return com.manulife.pension.util.StringUtils.escapeHtmlAndFormFeeds(note);
    }

    /**
     * @param note The note to set.
     */
    public void setNote(final String note) {
        this.note = note;
    }

    /**
     * @return Returns the noteTypeCode.
     */
    public String getNoteTypeCode() {
        return noteTypeCode;
    }

    /**
     * @param noteTypeCode The noteTypeCode to set.
     */
    public void setNoteTypeCode(final String noteTypeCode) {
        this.noteTypeCode = noteTypeCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {

        // Check base error codes
        if (CollectionUtils.isNotEmpty(getErrorCodes())) {
            return true;
        }

        // No errors exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {

        // Check base warning codes
        if (CollectionUtils.isNotEmpty(getWarningCodes())) {
            return true;
        }

        // No warnings exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {

        // Check base alert codes
        if (CollectionUtils.isNotEmpty(getAlertCodes())) {
            return true;
        }

        // No alerts exist
        return false;
    }

    /**
     * Checks if the default object has been inititalized or not.
     * 
     * @return boolean - True if the object is still blank.
     */
    public boolean isBlank() {
        return StringUtils.isBlank(getNote());
    }
}
