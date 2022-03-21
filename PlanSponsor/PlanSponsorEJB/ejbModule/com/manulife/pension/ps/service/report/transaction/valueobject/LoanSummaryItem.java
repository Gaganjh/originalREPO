package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang.ObjectUtils;

import com.manulife.pension.util.EqualityUtils;

public class LoanSummaryItem implements Serializable {

	public final static String SORT_NAME = "name";
	public final static String SORT_LOAN_NUMBER = "loanNumber";
	public final static String SORT_OUTSTANDING_BALANCE = "outstandingBalance";
	public final static String SORT_LAST_REPAYMENT_DATE = "lastRepaymentDate";
	public final static String SORT_LAST_REPAYMENT_AMT = "lastRepaymentAmt";
	public final static String SORT_ALERT = "alertText";
	public final static String SORT_ISSUE_DATE = "issueDate";

    public final static String SORT_INTEREST_RATE = "interestRate";

    public final static String SORT_MATURITY_DATE = "maturityDate";

    public final static String SORT_ORIGINAL_LOAN_AMT = "originalLoanAmt";

	private String name;
	private String lastName;
	private String firstName;
	private String ssn;
	private int loanNumber;
	private BigDecimal outstandingBalance;
	private BigDecimal lastRepaymentAmt;
	private BigDecimal lastRepaymentTransactionNo;
	private BigDecimal interestRate;

    private BigDecimal loanAmt;
	private Date lastRepaymentDate;
	private Date maturityDate;
	private Date effectiveDate;
	private Date creationDate;
	private String profileId;
	private String[] alerts;
	private String maturityDateExtended;
	private BigDecimal participantID;

	public LoanSummaryItem(String firstName, String lastName, String ssn,
			int loanNumber, BigDecimal outstandingBalance,
			BigDecimal lastRepaymentAmt, Date lastRepaymentDate,
			BigDecimal lastRepaymentTransactionNo, Date maturityDate,
			Date effectiveDate,
            Date creationDate, String profileId, BigDecimal interestRate, BigDecimal loanAmt,BigDecimal participantID) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.name = lastName + ", " + firstName;
		this.ssn = ssn;
		this.loanNumber = loanNumber;
		this.outstandingBalance = outstandingBalance;
		this.lastRepaymentAmt = lastRepaymentAmt;
		this.lastRepaymentDate = lastRepaymentDate;
		this.lastRepaymentTransactionNo = lastRepaymentTransactionNo;
		this.maturityDate = maturityDate;
		this.effectiveDate = effectiveDate;
		this.creationDate = creationDate;
		this.profileId = profileId;
		this.interestRate = interestRate;
        this.loanAmt = loanAmt;
        this.participantID = participantID;
	}

    public boolean isNoRepayment() {
			
		return lastRepaymentAmt.doubleValue() == 0.00 ? true: false; 
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof LoanSummaryItem) {
			LoanSummaryItem item = (LoanSummaryItem) obj;
			return ObjectUtils.equals(firstName, item.firstName)
					&& ObjectUtils.equals(lastName, item.lastName)
					&& ObjectUtils.equals(name, item.name)
					&& ObjectUtils.equals(ssn, item.ssn)
					&& loanNumber == item.loanNumber
					&& ObjectUtils.equals(outstandingBalance,
							item.outstandingBalance)
					&& ObjectUtils.equals(lastRepaymentAmt,
							item.lastRepaymentAmt)
					&& ObjectUtils.equals(lastRepaymentDate,
							item.lastRepaymentDate)
					&& ObjectUtils.equals(lastRepaymentTransactionNo,
							item.lastRepaymentTransactionNo)
					&& ObjectUtils.equals(maturityDate, item.maturityDate)
					&& ObjectUtils.equals(effectiveDate, item.effectiveDate)
					&& ObjectUtils.equals(profileId, item.profileId)
					&& EqualityUtils.equals(alerts, item.alerts)
					&& ObjectUtils.equals(participantID, item.participantID);
		}
		return false;
	}

	/**
	 * @return lastRepaymentAmt
	 */
	public BigDecimal getLastRepaymentAmt() {
		return lastRepaymentAmt;
	}

	/**
	 * @return lastRepaymentTransactionNo
	 */
	public BigDecimal getLastRepaymentTransactionNo() {
		return lastRepaymentTransactionNo;
	}

	/**
	 * @return lastRepaymentDate
	 */
	public Date getLastRepaymentDate() {
		return lastRepaymentDate;
	}

	/**
	 * @return loanNumber
	 */
	public int getLoanNumber() {
		return loanNumber;
	}

	/**
	 * @return maturityDate
	 */
	public Date getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return outstandingBalance
	 */
	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	/**
	 * @return ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param decimal
	 */
	public void setLastRepaymentAmt(BigDecimal lastRepaymentAmt) {
		this.lastRepaymentAmt = lastRepaymentAmt;
	}

	/**
	 * @param date
	 */
	public void setLastRepaymentDate(Date date) {
	    this.lastRepaymentDate = date;
	}

	/**
	 * @param date
	 */
	public void setLastRepaymentTransactionNo(BigDecimal transactionNo) {
	    this.lastRepaymentTransactionNo = transactionNo;
	}

	/**
	 * @param i
	 */
	public void setLoanNumber(int loanNumber) {
	    this.loanNumber = loanNumber;
	}

	/**
	 * @param date
	 */
	public void setMaturityDate(Date maturityDate) {
	    this.maturityDate = maturityDate;
	}

	/**
	 * @param string
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * @param decimal
	 */
	public void setOutstandingBalance(BigDecimal outstandingBalance) {
	    this.outstandingBalance = outstandingBalance;
	}

	/**
	 * @param string
	 */
	public void setSsn(String ssn) {
	    this.ssn = ssn;
	}

	/**
	 * @return
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @return
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @param date
	 */
	public void setEffectiveDate(Date effectiveDate) {
	    this.effectiveDate = effectiveDate;
	}

	/**
	 * @param decimal
	 */
	public void setProfileId(String profileId) {
	    this.profileId = profileId;
	}

	/**
	 * @return
	 */
	public String[] getAlerts() {
		if (alerts == null)
			alerts = new String[0];

		return alerts;
	}

	/**
	 * @param strings
	 */
	public void setAlerts(String[] alerts) {
	    this.alerts = alerts;
	}

	/**
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the firstName
	 * @param string
	 */
	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}

	/**
	 * Sets the lastName
	 * @param lastName
	 */
	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}

	/**
	 * Gets the creationDate
	 * @return Returns a Date
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * Sets the creationDate
	 * @param creationDate The creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the interestRate
     * @return interestRate
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    /**
     * Sets the interestRate
     * @param interestRate
     */
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Gets the loanAmt
     * @return
     */
    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    /**
     * Sets the loanAmt
     * @param loanAmt
     */
    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    public String getMaturityDateExtended() {
        return maturityDateExtended;
    }

    public void setMaturityDateExtended(String maturityDateExtended) {
        this.maturityDateExtended = maturityDateExtended;
    }
    
    /**
	 * @return the participantID
	 */
	public BigDecimal getParticipantID() {
		return participantID;
	}

	/**
	 * @param participantID the participantID to set
	 */
	public void setParticipantID(BigDecimal participantID) {
		this.participantID = participantID;
	}

}


