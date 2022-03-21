package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Element;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.util.LoanDocumentHelper;
import com.manulife.util.render.RenderConstants;

public class PromissoryNote extends BaseSerializableCloneableObject implements
		LoanDocument, XMLValueObject {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	private String middleInitial;

	private String planName;

	private BigDecimal loanAmount;

	private BigDecimal loanInterestRate;

	private String contractName;

	private String defaultProvision;

	private BigDecimal maximumLoanPercentage;
	
	private String loanStatus;
	
	private String participanInitiatedLoan;
	
	private Date loanAcceptedDate;

	private boolean finalPaymentDifferent;

	/**
	 * Default constructor
	 */
	public PromissoryNote() {
		super();
	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param planName
	 * @param loanAmount
	 * @param loanInterestRate
	 * @param numberOfPayments
	 * @param paymentFrequency
	 * @param paymentAmount
	 * @param finalPaymentAmount
	 * @param firstPaymentDate
	 * @param finalPaymentDate
	 * @param contractName
	 * @param defaultProvision
	 * @param maximumLoanPercentage
	 * @param finalPaymentDifferent
	 */
	public PromissoryNote(String firstName, String lastName,
			String middleInitial, String planName, BigDecimal loanAmount,
			BigDecimal loanInterestRate, Integer numberOfPayments,
			String paymentFrequency, BigDecimal paymentAmount,
			BigDecimal finalPaymentAmount, Date firstPaymentDate,
			Date finalPaymentDate, String contractName,
			String defaultProvision, BigDecimal maximumLoanPercentage,
			boolean finalPaymentDifferent) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.middleInitial = middleInitial;
		this.planName = planName;
		this.loanAmount = loanAmount;
		this.loanInterestRate = loanInterestRate;
		this.contractName = contractName;
		this.defaultProvision = defaultProvision;
		this.maximumLoanPercentage = maximumLoanPercentage;
		this.finalPaymentDifferent = finalPaymentDifferent;
	}

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

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getLoanInterestRate() {
		return loanInterestRate;
	}

	public void setLoanInterestRate(BigDecimal loanInterestRate) {
		this.loanInterestRate = loanInterestRate;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getDefaultProvision() {
		return defaultProvision;
	}

	public void setDefaultProvision(String defaultProvision) {
		this.defaultProvision = defaultProvision;
	}

	public BigDecimal getMaximumLoanPercentage() {
		return maximumLoanPercentage;
	}

	public void setMaximumLoanPercentage(BigDecimal maximumLoanPercentage) {
		this.maximumLoanPercentage = maximumLoanPercentage;
	}

	public boolean isFinalPaymentDifferent() {
		return finalPaymentDifferent;
	}

	public void setFinalPaymentDifferent(boolean finalPaymentDifferent) {
		this.finalPaymentDifferent = finalPaymentDifferent;
	}
	
	public String getLoanStatus() {
		return loanStatus;
	}
	
	public String getParticipanInitiatedLoan() {
		return participanInitiatedLoan;
	}

	public void setParticipanInitiatedLoan(String participanInitiatedLoan) {
		this.participanInitiatedLoan = participanInitiatedLoan;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	
	public Date getLoanAcceptedDate() {
		return loanAcceptedDate;
	}

	public void setLoanAcceptedDate(Date loanAcceptedDate) {
		this.loanAcceptedDate = loanAcceptedDate;
	}
	
	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * generates the XML for the PromissaryNote data
	 * 
	 * @return
	 */
	public String toXML() {

		return LoanDocXMLOutputter.getInstance().outputString(getElement());

	}

	/**
	 * returns specific element
	 * 
	 * @return
	 */
	public Element getElement() {

		Element element = new Element("PromissaryNote", "");

		element.addContent(new Element("participantName")
				.addContent(LoanDocumentHelper.getFormattedParticipantName(
						getFirstName(), getLastName(), getMiddleInitial())));

		element.addContent(new Element("planName").addContent(getPlanName()));

		element.addContent(new Element("loanAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getLoanAmount())));

		element.addContent(new Element("loanInterestRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter((getLoanInterestRate()))));

		element.addContent(new Element("contractName")
				.addContent(getContractName()));

		element.addContent(new Element("defaultProvision")
				.addContent(getDefaultProvision()));

		element.addContent(new Element("maximumLoanPercentage")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getMaximumLoanPercentage())));
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
