/*
 * Created on Dec 10, 2004
 *
 */
package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jim Adamthwaite
 *
 */
public class CopiedSubmissionHistoryItem implements Serializable {
	
    private static final long serialVersionUID = 1L;
    public static final int NO_ERROR = 0;
    public static final int ERROR_NO_VALID_DATA = -1;
    public static final int ERROR_COPY_SIZE_LIMIT_REACHED = -2;

    private int errorCode = NO_ERROR;
    private int oldSubmissionId = 0;
	private int newSubmissionId = 0;
    private Map participantsNotCopied;
    private Map participantsNotCopiedNonUniqueId;
    private Map moneyTypesNotCopied;
    private Map loanRepaymentsNotCopied;

	/**
	 * default constructor
	 */
	public CopiedSubmissionHistoryItem() {
		super();
		participantsNotCopied = new HashMap();
		participantsNotCopiedNonUniqueId = new HashMap();
		moneyTypesNotCopied = new HashMap();
		loanRepaymentsNotCopied = new HashMap();
	}
	/**
	 * convenience constructor
	 */
	public CopiedSubmissionHistoryItem(int oldSubmissionId, int newSubmissionId) {
		super();
		setOldSubmissionId(oldSubmissionId);
		setNewSubmissionId(newSubmissionId);
		participantsNotCopied = new HashMap();
		participantsNotCopiedNonUniqueId = new HashMap();
		moneyTypesNotCopied = new HashMap();
		loanRepaymentsNotCopied = new HashMap();
	}
	
	public Map getLoanRepaymentsNotCopied() {
		return loanRepaymentsNotCopied;
	}

	public void setLoanRepaymentsNotCopied(Map loanRepaymentsNotCopied) {
		this.loanRepaymentsNotCopied = loanRepaymentsNotCopied;
	}

	public Map getMoneyTypesNotCopied() {
		return moneyTypesNotCopied;
	}

	public void setMoneyTypesNotCopied(Map moneyTypesNotCopied) {
		this.moneyTypesNotCopied = moneyTypesNotCopied;
	}

	public int getNewSubmissionId() {
		return newSubmissionId;
	}

	public void setNewSubmissionId(int newSubmissionId) {
		this.newSubmissionId = newSubmissionId;
	}

	public Map getParticipantsNotCopied() {
		return participantsNotCopied;
	}

	public void setParticipantsNotCopied(Map participantsNotCopied) {
		this.participantsNotCopied = participantsNotCopied;
	}

	public Map getParticipantsNotCopiedNonUniqueId() {
		return this.participantsNotCopiedNonUniqueId;
	}
	public void setParticipantsNotCopiedNonUniqueId(
			Map participantsNotCopiedNonUniqueId) {
		this.participantsNotCopiedNonUniqueId = participantsNotCopiedNonUniqueId;
	}

    /**
     * @return Returns the errorCode.
     */
    public int getErrorCode() {
        return errorCode;
    }
    /**
     * @param errorCode The errorCode to set.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
	public int getOldSubmissionId() {
		return this.oldSubmissionId;
	}
	public void setOldSubmissionId(int oldSubmissionId) {
		this.oldSubmissionId = oldSubmissionId;
	}
}
