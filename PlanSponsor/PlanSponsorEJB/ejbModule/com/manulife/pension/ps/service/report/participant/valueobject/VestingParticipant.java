package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.submission.dao.SubmissionConstants;

public class VestingParticipant implements Serializable {
	
	private String ssn;
    private String empId;
	private String firstName;
    private String lastName;
    private String middleInitial;
    private String name;
    private String percDate;
    private String vyos;
    private String vyosDate;
	private SortedMap moneyTypePercentages;
    private SortedMap moneyTypeErrors;
    private int recordNumber;
    private String errorCondString;
    private int recordStatus;
    private boolean markedForDelete = false;
    private boolean hasErrors;
    private String applyLTPTCrediting;


    private static final String DESCENDING_INDICATOR = "DESC";
    private static final String ERROR_COND_STRING_OK = "OK";
    private static final int MAX_FIRST_NAME_LENGTH = 16;
    private static final int MAX_LAST_NAME_LENGTH = 16;
    
    //The following fields are sortable
    public final static String SORT_RECORD_NUMBER = "sourceRecordNo";
    public final static String SORT_SSN = "ssn";
    public final static String SORT_EMP_ID = "empId";
    public final static String SORT_NAME = "name";
	
	
	public VestingParticipant() {
	}
	
	public VestingParticipant(String ssn, String empId, String firstName, String lastName, String middleInitial,
            int recordNumber, String errorCondString, String percDate, String vyos, String vyosDate, 
            int recordStatus, SortedMap moneyTypePercentages, SortedMap moneyTypeErrors,String applyLTPTCrediting) {
		this.ssn = ssn;
		this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.recordNumber = recordNumber;
        this.errorCondString = errorCondString;
        this.percDate = percDate;
        this.vyos = vyos;
        this.vyosDate = vyosDate;
        this.recordStatus = recordStatus;
		this.moneyTypePercentages = moneyTypePercentages;
        this.moneyTypeErrors = moneyTypeErrors;
        this.applyLTPTCrediting=applyLTPTCrediting;
	}
	
	/**
	 * @return Returns the moneyTypePercentages.
	 */
	public SortedMap getMoneyTypePercentages() {
		return moneyTypePercentages;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param moneyTypePercentages The moneyTypePercentages to set.
	 */
	public void setMoneyTypePercentages(SortedMap moneyTypePercentages) {
		this.moneyTypePercentages = moneyTypePercentages;
	}

    public String getPercDate() {
        return percDate;
    }

    public void setPercDate(String percDate) {
        this.percDate = percDate;
    }

    public String getVyosDate() {
        return vyosDate;
    }

    public void setVyosDate(String vyosDate) {
        this.vyosDate = vyosDate;
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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
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
        if (!StringUtils.isEmpty(empId)) {
            return empId;
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

    public SortedMap getMoneyTypeErrors() {
        return moneyTypeErrors;
    }

    public void setMoneyTypeErrors(SortedMap moneyTypeErrors) {
        this.moneyTypeErrors = moneyTypeErrors;
    }

    public boolean isDoubleRowHeight() {
        return hasErrors;
    }
    
    public void setErrors(boolean flag) {
        this.hasErrors = flag;
    }

    public String getVyos() {
        return vyos;
    }

    public void setVyos(String vyos) {
        this.vyos = vyos;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }
    
    public String getApplyLTPTCrediting() {
		return applyLTPTCrediting;
	}

	public void setApplyLTPTCrediting(String applyLTPTCrediting) {
		this.applyLTPTCrediting = applyLTPTCrediting;
	}

    /**
     * If edits S2 (missing SSN) or S3 (unknown SSN) were encountered,
     * in which case record status is 21, ignore this participant, 
     * show in read-only mode and do not update data to CSDB
     * 
     * @return if participant is ignored or not
     */
    public boolean isIgnored() {
        if (SubmissionConstants.Vesting.PROCESS_STATUS_CODE_WARNING_IGNORES.
            equals(String.valueOf(getRecordStatus()))) {
            return true;
        } else {
            return false;
        }
    }
}
