package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Element;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.util.LoanDocumentHelper;

public class Instructions extends BaseSerializableCloneableObject implements
		LoanDocument, XMLValueObject {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	private String middleInitial;

	private BigDecimal tpaLoanIssueFee;

	private Date loanPackageStatusEffectiveDate;

	private String spousalConsentRequiredInd;

	private String loanType;

	private String contractName;

	private BigDecimal loanAmount;

	private String tpaFirmName;

	private String manulifeCompanyId;

	private Date currentDate;

	private String sectionEVerADisplayInd;

	private String sectionEVerBDisplayInd;

	private boolean moneyTypeExcluded;

	private boolean legallyMarriedInd;

	public boolean isLegallyMarriedInd() {
		return legallyMarriedInd;
	}

	public void setLegallyMarriedInd(boolean legallyMarriedInd) {
		this.legallyMarriedInd = legallyMarriedInd;
	}

	public BigDecimal getTpaLoanIssueFee() {
		return tpaLoanIssueFee;
	}

	public void setTpaLoanIssueFee(BigDecimal tpaLoanIssueFee) {
		this.tpaLoanIssueFee = tpaLoanIssueFee;
	}

	public Date getLoanPackageStatusEffectiveDate() {
		return loanPackageStatusEffectiveDate;
	}

	public void setLoanPackageStatusEffectiveDate(
			Date loanPackageStatusEffectiveDate) {
		this.loanPackageStatusEffectiveDate = loanPackageStatusEffectiveDate;
	}

	public String getSpousalConsentRequiredInd() {
		return spousalConsentRequiredInd;
	}

	public void setSpousalConsentRequiredInd(String spousalConsentRequiredInd) {
		this.spousalConsentRequiredInd = spousalConsentRequiredInd;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getTpaFirmName() {
		return tpaFirmName;
	}

	public void setTpaFirmName(String tpaFirmName) {
		this.tpaFirmName = tpaFirmName;
	}

	public boolean isMoneyTypeExcluded() {
		return moneyTypeExcluded;
	}

	public void setMoneyTypeExcluded(boolean moneyTypeExcluded) {
		this.moneyTypeExcluded = moneyTypeExcluded;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getManulifeCompanyId() {
		return manulifeCompanyId;
	}

	public void setManulifeCompanyId(String manulifeCompanyId) {
		this.manulifeCompanyId = manulifeCompanyId;
	}

	public String getSectionEVerADisplayInd() {
		return sectionEVerADisplayInd;
	}

	public void setSectionEVerADisplayInd(String sectionEVerADisplayInd) {
		this.sectionEVerADisplayInd = sectionEVerADisplayInd;
	}

	public String getSectionEVerBDisplayInd() {
		return sectionEVerBDisplayInd;
	}

	public void setSectionEVerBDisplayInd(String sectionEVerBDisplayInd) {
		this.sectionEVerBDisplayInd = sectionEVerBDisplayInd;
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

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * generates the XML for LoanInstruction data
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

		SimpleDateFormat EXTRA_LONG_MDY = new SimpleDateFormat("MMMMM dd, yyyy");

		Element element = new Element("LoanInstruction", "");

		// Elements common to LoanInstructionToParticipant and
		// LoanInstructionToPlanAdmin
		element.addContent(new Element("loanStatusCreateDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getLoanPackageStatusEffectiveDate(), EXTRA_LONG_MDY)));

		element.addContent(new Element("spousalConsentReqdInd")
				.addContent(getSpousalConsentRequiredInd()));

		element.addContent(new Element("legallyMarriedInd")
				.addContent(LoanDocumentHelper
						.getStringValue(isLegallyMarriedInd())));

		element.addContent(new Element("loanType")
				.addContent(LoanDocumentHelper.getLoanTypeText(getLoanType())));

		element.addContent(new Element("manulifeCompanyId")
				.addContent(LoanDocumentHelper
						.getCompanyName(getManulifeCompanyId())));

		// Elements specific to LoanInstructionToParticipant
		element.addContent(new Element("tpaLoanIssueFee")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getTpaLoanIssueFee())));

		element.addContent(new Element("currentDate")
				.addContent(LoanDocumentHelper.dateFormatter(getCurrentDate(),
						EXTRA_LONG_MDY)));

		// Elements specific to LoanInstructionToPlanAdmin
		element.addContent(new Element("sectionEVerADisplayInd")
				.addContent(getSectionEVerADisplayInd()));

		element.addContent(new Element("sectionEVerBDisplayInd")
				.addContent(getSectionEVerBDisplayInd()));

		element.addContent(new Element("tpaFirmName")
				.addContent(getTpaFirmName()));

		element.addContent(new Element("participantName")
				.addContent(LoanDocumentHelper.getFormattedParticipantName(
						getFirstName(), getLastName(), getMiddleInitial())));

		element.addContent(new Element("contractName")
				.addContent(getContractName()));

		element.addContent(new Element("loanAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getLoanAmount())));

		return element;
	}
}
