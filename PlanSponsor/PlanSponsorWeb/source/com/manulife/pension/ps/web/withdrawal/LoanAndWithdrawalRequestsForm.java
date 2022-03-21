package com.manulife.pension.ps.web.withdrawal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;

/**
 * Form class to used by the Loan and Withdrawal screen.
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalRequestsForm extends ReportForm {

    public static final int SSN_ONE_LENGTH = 3;

    public static final int SSN_TWO_LENGTH = 2;

    public static final int SSN_THREE_LENGTH = 4;

    public static final int CONTRACT_NUMBER_LENGTH = 5;

    public static final int CONTRACT_NUMBER_LENGTH_MAXIMUM = 7;

    public static final int CONTRACT_NAME_LENGTH = 30;

    public static final int DATE_FIELD_LENGTH = 10;

    private static final long serialVersionUID = 1L;

    // TODO Replace with blank string
    private static final String NO_VALUE_INDICATOR = "-1";

    private String task;

    private String profileId;

    private String contractId;

    private String requestStatus;

    private String submissionId;

    private String filterFromDate;

    private String filterToDate;

    private String filterRequestType;

    private String filterRequestReason;

    private String filterRequestStatus;

    private String filterParticipantLastName;

    private String filterParticipantSSN;

    private String ssnOne;

    private String ssnTwo;

    private String ssnThree;

    private String filterContractNumber;

    private String filterContractName;

    private boolean filterActive;

    private Map lookupData;

    private boolean showSearchLink;

    // The date the page was displayed (to trap rollover of date at Midnight)
    private Date displayDate;
    
    private String requestStatusCode;
    
	private String reasonStatusCode;
    
	private boolean showLoanCreateLink;	
	
	private String typeOfRequest;
	
	private boolean loanTypeILoan = false;

	public LoanAndWithdrawalRequestsForm() {
        super();

        // by default there are no active filters
        filterActive = false;
        // default values
        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.MONTH, -2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        setFilterFromDate(dateFormat.format(fromDate.getTime()));
        setFilterToDate(dateFormat.format(new Date()));
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public String getFilterContractName() {
        return filterContractName;
    }

    public void setFilterContractName(String filterContractName) {
        this.filterContractName = trimString(filterContractName);
    }

    public String getFilterContractNumber() {
        return filterContractNumber;
    }

    public void setFilterContractNumber(String filterContractNumber) {
        this.filterContractNumber = trimString(filterContractNumber);
    }

    public String getFilterFromDate() {
        return filterFromDate;
    }

    public void setFilterFromDate(String filterFromDate) {
        this.filterFromDate = filterFromDate;
    }

    public String getFilterParticipantLastName() {
        return filterParticipantLastName;
    }

    public void setFilterParticipantLastName(String filterParticipantLastName) {
        this.filterParticipantLastName = trimString(filterParticipantLastName);
    }

    public String getFilterParticipantSSN() {
        return filterParticipantSSN;
    }

    public void setFilterParticipantSSN(String filterParticipantSSN) {
        this.filterParticipantSSN = filterParticipantSSN;
    }

    public String getFilterRequestReason() {
        return filterRequestReason;
    }

    public void setFilterRequestReason(String filterReason) {
        this.filterRequestReason = filterReason;
    }

    public String getFilterRequestStatus() {
        return filterRequestStatus;
    }

    public void setFilterRequestStatus(String filterRequestStatus) {
        this.filterRequestStatus = filterRequestStatus;
    }

    public String getFilterRequestType() {
        return filterRequestType;
    }

    public void setFilterRequestType(String filterRequestType) {
        this.filterRequestType = filterRequestType;
    }

    public String getFilterToDate() {
        return filterToDate;
    }

    public void setFilterToDate(String filterToDate) {
        this.filterToDate = filterToDate;
    }

    public Map getLookupData() {
        return lookupData;
    }

    public void setLookupData(Map lookupData) {
        this.lookupData = lookupData;
    }

    /**
     * Convenience method for testing if a certain field has been set.
     * 
     * @param value - object to test
     * @return true is the value passed in is not null and different from the no-value place holder
     */
    public static boolean isFieldSet(Object value) {
        return value != null && !value.equals(NO_VALUE_INDICATOR);
    }

    /**
     * Populates the fileds of a form from a similar one. Care should be taken because the values
     * coming from the page will be overwritten
     */
    public void copyFrom(LoanAndWithdrawalRequestsForm other) {
        this.filterFromDate = other.filterFromDate;
        this.filterToDate = other.filterToDate;
        this.filterRequestType = other.filterRequestType;
        this.filterRequestReason = other.filterRequestReason;
        this.filterRequestStatus = other.filterRequestStatus;
        this.filterParticipantLastName = other.filterParticipantLastName;
        this.filterParticipantSSN = other.filterParticipantSSN;
        this.filterContractNumber = other.filterContractNumber;
        this.filterContractName = other.filterContractName;
        this.filterActive = other.filterActive;
        this.lookupData = other.lookupData;
        this.ssnOne = other.ssnOne;
        this.ssnTwo = other.ssnTwo;
        this.ssnThree = other.ssnThree;
        this.displayDate = other.displayDate;
    }

    public String getSsnOne() {
        return ssnOne;
    }

    public void setSsnOne(String ssnOne) {
        this.ssnOne = ssnOne;
    }

    public String getSsnThree() {
        return ssnThree;
    }

    public void setSsnThree(String ssnThree) {
        this.ssnThree = ssnThree;
    }

    public String getSsnTwo() {
        return ssnTwo;
    }

    public void setSsnTwo(String ssnTwo) {
        this.ssnTwo = ssnTwo;
    }

    public Ssn getSsn() {
        Ssn ssnTemp = new Ssn();
        ssnTemp.setDigits(0, ssnOne);
        ssnTemp.setDigits(1, ssnTwo);
        ssnTemp.setDigits(2, ssnThree);
        return ssnTemp;
    }

    public boolean isShowSearchLink() {
        return showSearchLink;
    }

    public void setShowSearchLink(boolean showSearchLink) {
        this.showSearchLink = showSearchLink;
    }

    /**
     * @return the displayDate
     */
    public Date getDisplayDate() {
        return displayDate;
    }

    /**
     * @param displayDate the displayDate to set
     */
    public void setDisplayDate(Date displayDate) {
        this.displayDate = displayDate;
    }

    /**
     * @return String - The task.
     */
    public String getTask() {
        return task;
    }

    /**
     * @param task - The task to set.
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @return String - The profileId.
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * @param profileId - The profileId to set.
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     * @return String - The contractId.
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId - The contractId to set.
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * @return String - The requestStatus.
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * @param requestStatus - The requestStatus to set.
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * @return String - The submissionId.
     */
    public String getSubmissionId() {
        return submissionId;
    }

    /**
     * @param submissionId - The submissionId to set.
     */
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
    /**
     * @return reasonStatusCode
     */
    public String getReasonStatusCode() {
		return reasonStatusCode;
	}

	/**
	 * @param reasonStatusCode
	 */
	public void setReasonStatusCode(String reasonStatusCode) {
		this.reasonStatusCode = reasonStatusCode;
	}

	/**
	 * @return requestStatusCode
	 */
	public String getRequestStatusCode() {
		return requestStatusCode;
	}

	/**
	 * @param requestStatusCode
	 */
	public void setRequestStatusCode(String requestStatusCode) {
		this.requestStatusCode = requestStatusCode;
	}    
	/**
	 * @return showLoanCreateLink
	 */
	public boolean getShowLoanCreateLink() {
		return showLoanCreateLink;
	}

	/**
	 * @param showLoanCreateLink
	 */
	public void setShowLoanCreateLink(boolean showLoanCreateLink) {
		this.showLoanCreateLink = showLoanCreateLink;
	}

	/**
	 * @return the typeOfRequest
	 */
	public String getTypeOfRequest() {
		return typeOfRequest;
	}

	/**
	 * @param typeOfRequest the typeOfRequest to set
	 */
	public void setTypeOfRequest(String typeOfRequest) {
		this.typeOfRequest = typeOfRequest;
	}
	
	/**
	 * @return the loanTypeILoan
	 */
	public boolean isLoanTypeILoan() {
		return loanTypeILoan;
	}

	/**
	 * @param loanTypeILoan the loanTypeILoan to set
	 */
	public void setLoanTypeILoan(boolean loanTypeILoan) {
		this.loanTypeILoan = loanTypeILoan;
	}
	
	
}