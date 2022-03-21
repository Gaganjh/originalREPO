/*
 * Created on August 17, 2004
 */
package com.manulife.pension.ps.service.submission.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * One contribution detail item.
 *
 * @author Jim Adamthwaite
 */
public class ContributionDetailItem extends SubmissionHistoryItem {

	private static final String STRING_TRUE = "Y";
	private static final BigDecimal ZERO_BIG_DECIMAL = new BigDecimal("0.00");

	private int numberOfParticipants;
	private BigDecimal employeeContributionTotal = ZERO_BIG_DECIMAL;
	private BigDecimal employerContributionTotal = ZERO_BIG_DECIMAL;
	private BigDecimal loanRepaymentTotal;
	private BigDecimal withdrawlTotal;
	private BigDecimal participantTotal;
	private Map allocationTotalValues;
	private Collection loanTotalValues;
	private Boolean generateStatementsIndicator;
	private SubmissionPaymentItem submissionPaymentItem;
	private List allocationMoneyTypes;
	private List contractMoneyTypes;
	private Map contractMoneySources;
	private boolean contractHasLoanFeature;
	private int maximumNumberOfLoans = 0;
	private List submissionParticipants = new ArrayList();
	private String participantSortOption;
	private ReportDataErrors reportDataErrors = new ReportDataErrors();
	private int transmissionId;
	private boolean zeroContributionFile;

	public ContributionDetailItem() {
		super();
	}

	public ContributionDetailItem(String submitterID, String submitterType, String submitterName, String submitterEmail,
			Integer submissionNumber, Date submissionDate, String type, String systemStatus, Date payrollDate,
			BigDecimal contributionTotal, BigDecimal paymentTotal, String moneySourceID, String applicationCode,
			Integer contractNumber, String contractName, int numberOfParticipants, BigDecimal loanRepaymentTotal, 
			Boolean generateStatementsIndicator, String tpaSystemName, String tpaSubmissionType, String tpaVersionNo, Lock lock) {
		super(submitterID, submitterType, submitterName, submitterEmail, submissionNumber, submissionDate, type, systemStatus, payrollDate,
				contributionTotal, paymentTotal, moneySourceID, applicationCode, contractNumber, contractName, tpaSystemName, tpaSubmissionType, tpaVersionNo, lock);
		this.numberOfParticipants = numberOfParticipants;
		this.loanRepaymentTotal = loanRepaymentTotal;
		this.generateStatementsIndicator = generateStatementsIndicator;
	}
	
	/**
	 * @return Returns the numberOfParticipants
	 */
	public int getNumberOfParticipants(){
		return this.numberOfParticipants;
	}
	/**
	 * @return Returns the employeeContributionTotal.
	 */
	public BigDecimal getEmployeeContributionTotal() {
		return employeeContributionTotal;
	}

	/**
	 * @return Returns the employerContributionTotal.
	 */
	public BigDecimal getEmployerContributionTotal() {
		return employerContributionTotal;
	}

	/**
	 * @return Returns the loanRepaymentTotal.
	 */
	public BigDecimal getLoanRepaymentTotal() {
		return loanRepaymentTotal;
	}

	/**
	 * @return Returns the SubmissionTotal
	 */
	public BigDecimal getSubmissionTotal() {
		return loanRepaymentTotal.add(employerContributionTotal.add(employeeContributionTotal));
	}

	/**
	 * @return Returns the submissionPaymentItem
	 */
	public SubmissionPaymentItem getSubmissionPaymentItem(){
		return this.submissionPaymentItem;
	}
	/**
	 * @return Returns the allocationMoneyTypes
	 */
	public List getAllocationMoneyTypes(){
		return this.allocationMoneyTypes;
	}
	/**
	 * @return Returns the maximumNumberOfLoans for any any participant
	 */
	public int getMaximumNumberOfLoans(){
		return this.maximumNumberOfLoans;
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
	 * @param contributionTotal The employeeContributionTotal to set.
	 */
	public void setEmployeeContributionTotal(BigDecimal contributionTotal) {
		this.employeeContributionTotal = contributionTotal;
	}

	/**
	 * @param contributionTotal The employerContributionTotal to set.
	 */
	public void setEmployerContributionTotal(BigDecimal contributionTotal) {
		this.employerContributionTotal = contributionTotal;
	}
	
	/**
	 * @param paymentTotal The paymentTotal to set.
	 */
	public void setLoanRepaymentTotal(BigDecimal repaymentTotal) {
		this.loanRepaymentTotal = repaymentTotal;
	}

	/**
	 * @param numberOfParticipants The numberOfParticipants to set.
	 */
	public void setNumberOfParticipants(int numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	/**
	 * @param generateStatementsIndicator The generateStatementsIndicator to set.
	 */
	public void setGenerateStatementsIndicator(Boolean generateStatementsIndicator) {
		this.generateStatementsIndicator = generateStatementsIndicator;
	}

	/**
	 * @param submissionPaymentItem The SubmissionPaymentItem to set.
	 */	
	public void setSubmissionPaymentItem(SubmissionPaymentItem submissionPaymentItem) {
		this.submissionPaymentItem = submissionPaymentItem;
	}
	
	/**
	 * @param moneyTypes The list of money types to set
	 */
	public void setAllocationMoneyTypes(List moneyTypes) {
		this.allocationMoneyTypes = moneyTypes;
	}
	
	/**
	 * @param moneyTypeKey the moneyTypeKey to add 
	 * @param moneyType the moneyTypeVO to add
	 * 
	 * (key consists of money type alias id and occurence number e.g. "EEDEF/0")
	 */
	public void addAllocationMoneyTypes(MoneyTypeHeader moneyTypeHeader) {
		if (null == this.allocationMoneyTypes) {
			this.allocationMoneyTypes = new ArrayList();
		}
		this.allocationMoneyTypes.add(moneyTypeHeader);
	}
	
	/**
	 * @param maximumNumberOfLoans the number of loans to set
	 */
	public void setMaximumNumberOfLoans(int maximumNumberOfLoans) {
		this.maximumNumberOfLoans = maximumNumberOfLoans;
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
	public void addSubmissionParticipant(SubmissionParticipant participant) {
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
		String generateStatementsIndicatorString = "Statement Indicator Empty";
		if (getGenerateStatementsIndicator() != null)
			generateStatementsIndicatorString = getGenerateStatementsIndicator().toString();
		myString = myString + ", " + getLockUserId() + ", " + getNumberOfParticipants() + ", " + 
				getEmployeeContributionTotal() + ", " + getEmployerContributionTotal() + ", " +
				getLoanRepaymentTotal() + ", " + generateStatementsIndicatorString;
		return myString;
	}

	/**
	 * @return Returns the allocationTotalValues.
	 */
	public Map getAllocationTotalValues() {
		return allocationTotalValues;
	}
	/**
	 * @param allocationTotalValues The allocationTotalValues to set.
	 */
	public void setAllocationTotalValues(Map allocationTotalValues) {
		this.allocationTotalValues = allocationTotalValues;
	}
	/**
	 * @return Returns the loanTotalValues.
	 */
	public Collection getLoanTotalValues() {
		return loanTotalValues;
	}
	/**
	 * @param loanTotalValues The loanTotalValues to set.
	 */
	public void setLoanTotalValues(Collection loanTotalValues) {
		this.loanTotalValues = loanTotalValues;
	}
	/**
	 * @return Returns the participantTotal.
	 */
	public BigDecimal getParticipantTotal() {
		return participantTotal;
	}
	/**
	 * @param participantTotal The participantTotal to set.
	 */
	public void setParticipantTotal(BigDecimal participantTotal) {
		this.participantTotal = participantTotal;
	}
	/**
	 * @return Returns the generateStatementsIndicator.
	 */
	public Boolean getGenerateStatementsIndicator() {
		return generateStatementsIndicator;
	}
	/**
	 * @return Returns the contractMoneyTypes.
	 */
	public List getContractMoneyTypes() {
		return contractMoneyTypes;
	}
	/**
	 * @param contractMoneyTypes The contractMoneyTypes to set.
	 */
	public void setContractMoneyTypes(List contractMoneyTypes) {
		this.contractMoneyTypes = contractMoneyTypes;
	}
	/**
	 * @return Returns the contractMoneySources.
	 */
	public Map getContractMoneySources() {
		return contractMoneySources;
	}
	/**
	 * @param contractMoneySources The contractMoneySources to set.
	 */
	public void setContractMoneySources(Map contractMoneySources) {
		this.contractMoneySources = contractMoneySources;
	}
	/**
	 * @return Returns the withdrawlTotal.
	 */
	public BigDecimal getWithdrawlTotal() {
		return withdrawlTotal;
	}
	/**
	 * @param withdrawlTotal The withdrawlTotal to set.
	 */
	public void setWithdrawlTotal(BigDecimal withdrawlTotal) {
		this.withdrawlTotal = withdrawlTotal;
	}
	
	public int getTransmissionId() {
		return this.transmissionId;
	}
	
	public void setTransmissionId(int transmissionId) {
		this.transmissionId = transmissionId;
	}

	public boolean isContractHasLoanFeature() {
		return this.contractHasLoanFeature;
	}
	
	public void setContractHasLoanFeature(boolean contractHasLoanFeature) {
		this.contractHasLoanFeature = contractHasLoanFeature;
	}

	public void setStatementRequestInd(String statementRequestInd) {
		setGenerateStatementsIndicator(statementRequestInd == null ? null
			: (statementRequestInd.equals(STRING_TRUE) ? Boolean.TRUE : Boolean.FALSE));
	}
	
	public void setErrorCondString(String errorCondString) {
		setReportDataErrors(new ReportDataErrors(errorCondString));
	}
	
	public boolean isZeroContributionFile() {
		return zeroContributionFile;
	}

	public void setZeroContributionFile(boolean zeroContributionFile) {
		this.zeroContributionFile = zeroContributionFile;
	}

}
