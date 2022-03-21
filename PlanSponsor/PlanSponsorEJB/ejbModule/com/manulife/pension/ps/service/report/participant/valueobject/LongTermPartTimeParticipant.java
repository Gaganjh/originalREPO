package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.submission.dao.SubmissionConstants;

public class LongTermPartTimeParticipant implements Serializable {

	private static final long serialVersionUID = 7439165650128622286L;
	private String ssn;
	private String firstName;
    private String lastName;
    private String middleInitial;
    private String name;
    private int recordNumber;
    private String errorCondString;
    private int recordStatus;
    private boolean markedForDelete = false;
    private boolean hasErrors;
    private String longTermPartTimeAssessmentYear;


	private static final String DESCENDING_INDICATOR = "DESC";
    private static final String ERROR_COND_STRING_OK = "OK";
    private static final int MAX_FIRST_NAME_LENGTH = 16;
    private static final int MAX_LAST_NAME_LENGTH = 16;
    
    //The following fields are sortable
    public final static String SORT_RECORD_NUMBER = "sequenceNo";
    public final static String SORT_SSN = "ssn";
    public final static String SORT_NAME = "name";
	
	
	public LongTermPartTimeParticipant() {
	}
	
	public LongTermPartTimeParticipant(String ssn, String firstName, String lastName, String middleInitial,
			String longTermPartTimeAssessmentYear, int recordNumber, String errorCondString, 
            int recordStatus) {
		this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.recordNumber = recordNumber;
        this.errorCondString = errorCondString;
        this.recordStatus = recordStatus;
        this.longTermPartTimeAssessmentYear = longTermPartTimeAssessmentYear;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstNameTruncated() {
        if (StringUtils.isNotEmpty(firstName) && firstName.length() > MAX_FIRST_NAME_LENGTH) {
            return firstName.substring(0, MAX_FIRST_NAME_LENGTH - 3) + "...";
        }
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameTruncated() {
        if (StringUtils.isNotEmpty(lastName) && lastName.length() > MAX_LAST_NAME_LENGTH) {
            return lastName.substring(0, MAX_LAST_NAME_LENGTH - 3) + "...";
        }
        return lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getEmployerDesignatedId() {
    	if (!StringUtils.isEmpty(ssn)) {
    		return ssn;
    	}
    	return "";
    }
    
    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getErrorCondString() {
        return errorCondString;
    }

    public void setErrorCondString(String errorCondString) {
        this.errorCondString = errorCondString;
    }

    public boolean isDoubleRowHeight() {
        return hasErrors;
    }
    
    public void setErrors(boolean flag) {
        this.hasErrors = flag;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * If edits S2 (missing SSN) or S3 (unknown SSN) were encountered,
     * in which case record status is 21, ignore this participant, 
     * show in read-only mode and do not update data to CSDB
     * 
     * @return if participant is ignored or not
     */
    public boolean isIgnored() {
        if (SubmissionConstants.LongTermPartTime.PROCESS_LTPT_STATUS_CODE_WARNINGS.
            equals(String.valueOf(getRecordStatus()))) {
            return true;
        } else {
            return false;
        }
    }
    
    public String getLongTermPartTimeAssessmentYear() {
		return longTermPartTimeAssessmentYear;
	}

	public void setLongTermPartTimeAssessmentYear(String longTermPartTimeAssessmentYear) {
		this.longTermPartTimeAssessmentYear = longTermPartTimeAssessmentYear;
	}
}
