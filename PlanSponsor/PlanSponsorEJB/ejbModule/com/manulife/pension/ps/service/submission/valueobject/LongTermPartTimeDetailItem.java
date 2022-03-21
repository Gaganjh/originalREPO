/*
 */
package com.manulife.pension.ps.service.submission.valueobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;

public class LongTermPartTimeDetailItem extends SubmissionHistoryItem {

    private static final String FIELD_SEPARATOR = "|";

	private int numberOfRecords;
	private List submissionParticipants = new ArrayList();
	private String participantSortOption;
	private ReportDataErrors reportDataErrors = new ReportDataErrors();
	private int transmissionId;
    private String errorMessage;
    private String errorLevel;

	public LongTermPartTimeDetailItem() {
		super();
	}

	public LongTermPartTimeDetailItem(String submitterID, String submitterType, String submitterName, 
			Integer submissionNumber, Date submissionDate, String type, String systemStatus, 
			String applicationCode, Integer contractNumber, String contractName, 
            String tpaSystemName, String tpaSubmissionType, String tpaVersionNo, 
            int numberOfRecords, Lock lock) {
		super(submitterID, submitterType, submitterName, submissionNumber, submissionDate, type, systemStatus, null,
				null, null, null, applicationCode, contractNumber, contractName, 
                tpaSystemName, tpaSubmissionType, tpaVersionNo, lock);
		this.numberOfRecords = numberOfRecords;
	}

	/**
	 * @return Returns the submissionParticipants
	 */
	public List getSubmissionParticipants(){
		return this.submissionParticipants;
	}

	public String getParticipantSortOption(){
		return this.participantSortOption;
	}
	
	/**
	 * @return Returns the errors
	 */
	public ReportDataErrors getReportDataErrors(){
		return this.reportDataErrors;
	}
	
	/**
	 * @param participants The list of participants to set
	 */
	public void setSubmissionParticipants(List participants) {
		this.submissionParticipants = participants;
	}
	
	/**
	 * @param participant The participant to add
	 */
	public void addSubmissionParticipant(LongTermPartTimeParticipant participant) {
		if (null == this.submissionParticipants) {
			this.submissionParticipants = new ArrayList();
		}
		this.submissionParticipants.add(participant);
	}
	
	public void setParticipantSortOption(String participantSortOption) {
		this.participantSortOption = participantSortOption;
	}
	
	/**
	 * @param errors The reportDataErrors to set
	 */
	public void setReportDataErrors(ReportDataErrors reportDataErrors) {
		this.reportDataErrors = reportDataErrors;
	}
	
	public String toString() {
		String myString = super.toString();
		
		myString = getSubmissionId() + ", " + getLockUserId() + ", " + getNumberOfRecords();
		return myString;
	}

	public int getTransmissionId() {
		return this.transmissionId;
	}
	
	public void setTransmissionId(int transmissionId) {
		this.transmissionId = transmissionId;
	}
	
	public void setErrorCondString(String errorCondString) {
		setReportDataErrors(new ReportDataErrors(errorCondString,false));
	}

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

    public String[] getErrorMessage() {
        String[] genericErrors = null;
        
        if (errorMessage != null && errorMessage.length() > 0) {
            StringTokenizer st = new StringTokenizer(errorMessage,FIELD_SEPARATOR);
            genericErrors = new String[st.countTokens()];
            int index = 0;
            while (st.hasMoreTokens()) {
                genericErrors[index] = st.nextToken();
                index++;
            }
        }
        return genericErrors;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}