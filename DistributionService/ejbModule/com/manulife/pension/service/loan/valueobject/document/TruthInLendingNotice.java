package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Element;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.util.LoanDocumentHelper;
import com.manulife.util.render.RenderConstants;

public class TruthInLendingNotice extends BaseSerializableCloneableObject
		implements LoanDocument, XMLValueObject {
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String middleInitial;
	private Date loanStartDate;
	private String contractName;
	private String planName;
	private BigDecimal annualPercentageRate;
	private BigDecimal financeCharge;
	private BigDecimal loanAmount;
	private BigDecimal totalOfPayments;
	private Integer amortizationMonths;
	private BigDecimal paymentAmount;
	private String paymentFrequency;
	private Date firstPaymentDate;
	private BigDecimal maximumLoanPercentage;
	private String defaultProvision;
	private String loanStatus;
	private String participanInitiatedLoan;
	private Date loanAcceptedDate;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public BigDecimal getAnnualPercentageRate() {
		return annualPercentageRate;
	}

	public void setAnnualPercentageRate(BigDecimal annualPercentageRate) {
		this.annualPercentageRate = annualPercentageRate;
	}

	public BigDecimal getFinanceCharge() {
		return financeCharge;
	}

	public void setFinanceCharge(BigDecimal financeCharge) {
		this.financeCharge = financeCharge;
	}

	public BigDecimal getTotalOfPayments() {
		return totalOfPayments;
	}

	public void setTotalOfPayments(BigDecimal totalOfPayments) {
		this.totalOfPayments = totalOfPayments;
	}

	public Integer getAmortizationMonths() {
		return amortizationMonths;
	}

	public void setAmortizationMonths(Integer amortizationMonths) {
		this.amortizationMonths = amortizationMonths;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public BigDecimal getMaximumLoanPercentage() {
		return maximumLoanPercentage;
	}

	public void setMaximumLoanPercentage(BigDecimal maximumLoanPercentage) {
		this.maximumLoanPercentage = maximumLoanPercentage;
	}

	public String getDefaultProvision() {
		return defaultProvision;
	}

	public void setDefaultProvision(String defaultProvision) {
		this.defaultProvision = defaultProvision;
	}
	
	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	
	public String getParticipanInitiatedLoan() {
		return participanInitiatedLoan;
	}

	public void setParticipanInitiatedLoan(String participanInitiatedLoan) {
		this.participanInitiatedLoan = participanInitiatedLoan;
	}
	
	public Date getLoanAcceptedDate() {
		return loanAcceptedDate;
	}

	public void setLoanAcceptedDate(Date loanAcceptedDate) {
		this.loanAcceptedDate = loanAcceptedDate;
	}

	/**
	 * generates the XML for the TruthInLendingNotice data
	 * 
	 * @return
	 */
	public String toXML() {

		return LoanDocXMLOutputter.getInstance().outputString(getElement());

	}

	public Element getElement() {
		Element element = new Element("TruthInLendingNotice", "");

		element.addContent(new Element("participantName")
				.addContent(LoanDocumentHelper.getFormattedParticipantName(
						getFirstName(), getLastName(), getMiddleInitial())));

		element.addContent(new Element("planName").addContent(getPlanName()));

		element.addContent(new Element("contractName")
				.addContent(getContractName()));

		element.addContent(new Element("loanEffectiveDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getLoanStartDate(), new SimpleDateFormat(
								RenderConstants.EXTRA_LONG_MDY))));

		element.addContent(new Element("annualPercentageRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getAnnualPercentageRate())));

		element.addContent(new Element("financeCharge")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getFinanceCharge())));

		element.addContent(new Element("loanAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getLoanAmount())));

		element.addContent(new Element("totalOfPayment")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getTotalOfPayments())));

		element.addContent(new Element("amortizationInMonths")
				.addContent(LoanDocumentHelper
						.getStringValue(getAmortizationMonths())));

		element.addContent(new Element("repaymentAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getPaymentAmount())));

		element.addContent(new Element("paymentFrequency")
				.addContent(LoanDocumentHelper
						.paymentFrequency(getPaymentFrequency())));

		element.addContent(new Element("firstPaymentDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getFirstPaymentDate(), new SimpleDateFormat(
								RenderConstants.EXTRA_LONG_MDY))));

		element.addContent(new Element("maximumLoanPercentage")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getMaximumLoanPercentage())));

		element.addContent(new Element("defaultProvisions")
				.addContent(getDefaultProvision()));
		
		element.addContent(new Element("loanStatus")
				.addContent(getLoanStatus()));
		
		element.addContent(new Element("participanInitiatedLoan")
				.addContent(getParticipanInitiatedLoan()));
		
		element.addContent(new Element("participantNameAO")
				.addContent(LoanDocumentHelper.getFormattedParticipantNameAO(
						getFirstName(), getLastName(), getMiddleInitial())));
		
		element.addContent(new Element("loanAcceptedDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getLoanAcceptedDate(), new SimpleDateFormat(
								RenderConstants.EXTRA_LONG_MDY))));
		
		return element;
	}
}
