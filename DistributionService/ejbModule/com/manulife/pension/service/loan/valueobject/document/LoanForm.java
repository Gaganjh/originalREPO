package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jdom.Element;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.util.LoanDocumentHelper;
import com.manulife.pension.util.JdbcHelper;

public class LoanForm extends BaseSerializableCloneableObject implements
		LoanDocument, XMLValueObject {

	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	private String middleInitial;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String stateCode;

	private String zipCode;

	private String country;

	private String planName;

	private BigDecimal loanAmount;

	private BigDecimal loanInterestRate;

	private BigDecimal tpaLoanIssueFee;

	private String ssn;

	private Date finalPaymentDate;

	private Integer contractId;

	private Integer submissionId;

	private String loanType;

	private List<String> moneyTypesInc;

	private List<String> moneyTypesExc;

	private String paymentMethodCode;

	private String bankAccountTypeCode;

	private String abaRoutingNumber;

	private String bankName;

	private String bankAccountNumber;

	private Boolean addressToMailCheckTo;

	private String moneyTypeInd;

	private String loanAmountDisplayInd;

	private String SpousalConsentReqdInd;

	private String distAddressLine1;

	private String distAddressLine2;

	private String distCity;

	private String distStateCode;

	private String distZipCode;

	private String distCountry;

	private String manulifeCompanyId;
	
	private boolean bundledGaIndicator = false;

	public String getManulifeCompanyId() {
		return manulifeCompanyId;
	}

	public void setManulifeCompanyId(String manulifeCompanyId) {
		this.manulifeCompanyId = manulifeCompanyId;
	}

	public String getMoneyTypeInd() {
		return moneyTypeInd;
	}

	public void setMoneyTypeInd(String moneyTypeInd) {
		this.moneyTypeInd = moneyTypeInd;
	}

	public Boolean getAddressToMailCheckTo() {
		return addressToMailCheckTo;
	}

	public void setAddressToMailCheckTo(Boolean addressToMailCheckTo) {
		this.addressToMailCheckTo = addressToMailCheckTo;
	}

	public String getAbaRoutingNumber() {
		return abaRoutingNumber;
	}

	public void setAbaRoutingNumber(String abaRoutingNumber) {
		this.abaRoutingNumber = abaRoutingNumber;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public Date getFinalPaymentDate() {
		return finalPaymentDate;
	}

	public void setFinalPaymentDate(Date finalPaymentDate) {
		this.finalPaymentDate = finalPaymentDate;
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

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getBankAccountTypeCode() {
		return bankAccountTypeCode;
	}

	public void setBankAccountTypeCode(String bankAccountTypeCode) {
		this.bankAccountTypeCode = bankAccountTypeCode;
	}

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	public List<String> getMoneyTypesExc() {
		return moneyTypesExc;
	}

	public void setMoneyTypesExc(List<String> moneyTypesExc) {
		this.moneyTypesExc = moneyTypesExc;
	}

	public List<String> getMoneyTypesInc() {
		return moneyTypesInc;
	}

	public void setMoneyTypesInc(List<String> moneyTypesInc) {
		this.moneyTypesInc = moneyTypesInc;
	}

	public String getLoanAmountDisplayInd() {
		return loanAmountDisplayInd;
	}

	public void setLoanAmountDisplayInd(String loanAmountDisplayInd) {
		this.loanAmountDisplayInd = loanAmountDisplayInd;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public BigDecimal getTpaLoanIssueFee() {
		return tpaLoanIssueFee;
	}

	public void setTpaLoanIssueFee(BigDecimal tpaLoanIssueFee) {
		this.tpaLoanIssueFee = tpaLoanIssueFee;
	}

	public String getSpousalConsentReqdInd() {
		return SpousalConsentReqdInd;
	}

	public void setSpousalConsentReqdInd(String spousalConsentReqdInd) {
		SpousalConsentReqdInd = spousalConsentReqdInd;
	}

	public String getDistAddressLine1() {
		return distAddressLine1;
	}

	public void setDistAddressLine1(String distAddressLine1) {
		this.distAddressLine1 = distAddressLine1;
	}

	public String getDistAddressLine2() {
		return distAddressLine2;
	}

	public void setDistAddressLine2(String distAddressLine2) {
		this.distAddressLine2 = distAddressLine2;
	}

	public String getDistCity() {
		return distCity;
	}

	public void setDistCity(String distCity) {
		this.distCity = distCity;
	}

	public String getDistCountry() {
		return distCountry;
	}

	public void setDistCountry(String distCountry) {
		this.distCountry = distCountry;
	}

	public String getDistStateCode() {
		return distStateCode;
	}

	public void setDistStateCode(String distStateCode) {
		this.distStateCode = distStateCode;
	}

	public String getDistZipCode() {
		return distZipCode;
	}

	public void setDistZipCode(String distZipCode) {
		this.distZipCode = distZipCode;
	}
	
	public boolean isBundledGaIndicator() {
		return bundledGaIndicator;
	}
	
	public void setBundledGaIndicator(boolean bundledGaIndicator) {
		this.bundledGaIndicator = bundledGaIndicator;
	}

	/**
	 * generates the XML for the Loan Form data
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
		
		SimpleDateFormat SHORT_MONTH = new SimpleDateFormat("MMM");
		SimpleDateFormat DAY = new SimpleDateFormat("dd");

		Element element = new Element("LoanForm", "");

		element.addContent(new Element("participantName")
				.addContent(LoanDocumentHelper.getFormattedParticipantName(
						getFirstName(), getLastName(), getMiddleInitial())));

		if (getFinalPaymentDate() != null) {
			Element finalPaymentDate = new Element("finalPaymentDate");
			Calendar cal = Calendar.getInstance();
			cal.setTime(getFinalPaymentDate());

			finalPaymentDate.addContent(new Element("month")
					.addContent(LoanDocumentHelper.dateFormatter(
							cal.getTime(), SHORT_MONTH)));

			finalPaymentDate.addContent(new Element("day")
					.addContent(LoanDocumentHelper.dateFormatter(
							cal.getTime(), DAY)));

			finalPaymentDate.addContent(new Element("year")
					.addContent(LoanDocumentHelper.getStringValue(cal
							.get(Calendar.YEAR))));

			element.addContent(finalPaymentDate);
		}

		element.addContent(new Element("participantAddress")
				.addContent(LoanDocumentHelper.getFormattedAddress(
						getAddressLine1(), getAddressLine2(), getCity(),
						getStateCode(), getZipCode(), getCountry())));

		if (getPaymentMethodCode() != null) {

			element.addContent(new Element("paymentMethodCode")
					.addContent(getPaymentMethodCode()));

			// If payment method is 'CH' then then elements related to
			// distribution address will be populated
			if (getPaymentMethodCode().equals("CH")) {
				element.addContent(new Element("distributionAddress")
						.addContent(LoanDocumentHelper.getFormattedAddress(
								getDistAddressLine1(), getDistAddressLine2(),
								getDistCity(), getDistStateCode(),
								getDistZipCode(), getDistCountry())));
			}

			// If paymentmethod is 'AC' or 'WT' then elements related to bank
			// details will be populated
			if (getPaymentMethodCode().equals("AC")
					|| getPaymentMethodCode().equals("WT")) {
				element.addContent(new Element("bankName")
						.addContent(getBankName()));

				element.addContent(new Element("bankAccountNumber")
						.addContent(getBankAccountNumber()));

				element.addContent(new Element("abaRoutingNumber")
						.addContent(getAbaRoutingNumber()));
			}
		}

		element.addContent(new Element("planName").addContent(getPlanName()));

		element.addContent(new Element("ssn").addContent(LoanDocumentHelper
				.getFormattedSSN(getSsn())));

		if(isBundledGaIndicator()){
			
			element	.addContent(new Element("contractId")
					.addContent("T " + getContractId()));
			element	.addContent(new Element("bundledGaIndicator")
					.addContent(JdbcHelper.INDICATOR_YES));
		}else{
			element.addContent(new Element("contractId")
					.addContent(LoanDocumentHelper.getStringValue(getContractId())));
			element	.addContent(new Element("bundledGaIndicator")
					.addContent(JdbcHelper.INDICATOR_NO));
		}

		element.addContent(new Element("submissionId")
				.addContent(LoanDocumentHelper
						.getStringValue(getSubmissionId())));

		element.addContent(new Element("loanAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter((getLoanAmount()))));

		element.addContent(new Element("bankAccountTypeCode")
				.addContent(getBankAccountTypeCode()));

		element.addContent(new Element("loanType")
				.addContent(LoanDocumentHelper.getLoanTypeText(getLoanType())));

		element.addContent(new Element("loanInterestRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getLoanInterestRate())));

		element.addContent(new Element("addressToMailCheckTo")
				.addContent(LoanDocumentHelper
						.getStringValue(getAddressToMailCheckTo())));

		element.addContent(new Element("moneyTypeInd")
				.addContent(getMoneyTypeInd()));

		element.addContent(new Element("tpaLoanIssueFee")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getTpaLoanIssueFee())));

		element.addContent(new Element("spousalConsentReqdInd")
				.addContent(getSpousalConsentReqdInd()));

		if (getLoanAmountDisplayInd() != null) {
			element.addContent(new Element("loanAmountDisplayInd")
					.addContent(getLoanAmountDisplayInd()));
		}

		// Formats the xml tags for MoneyTypeInc and MoneyTypeExc tables
		if (getMoneyTypeInd() != null) {
			if (getMoneyTypeInd().equals(JdbcHelper.INDICATOR_YES)) {
				List<String> moneyTypeInc = getMoneyTypesInc();
				LoanDocumentHelper.getSortedList(moneyTypeInc);

				for (String moneyType : moneyTypeInc) {
					Element moneyTypesInc = new Element("moneyTypesInc");

					moneyTypesInc.addContent(new Element("moneyType")
							.addContent(moneyType));
					element.addContent(moneyTypesInc);
				}

				List<String> moneyTypeExc = getMoneyTypesExc();
				LoanDocumentHelper.getSortedList(moneyTypeExc);
				Element moneyTypesExc;
				moneyTypesExc = new Element("moneyTypesExc");
				int count = 1;

				for (String moneyType : moneyTypeExc) {
					moneyTypesExc.addContent(new Element("moneyType" + count)
							.addContent(moneyType));
					count++;
					if (count > 3) {
						element.addContent(moneyTypesExc);
						moneyTypesExc = new Element("moneyTypesExc");
						count = 1;
					}
				}

				if (count < 4) {
					element.addContent(moneyTypesExc);
				}
			}
		}

		// Element common to loan request form pdf and instructions for loan
		// request pdf
		element.addContent(new Element("manulifeCompanyId")
				.addContent(LoanDocumentHelper
						.getCompanyName(getManulifeCompanyId())));

		// Elements specific to instructions for loan request pdf
		element.addContent(new Element("sectionEVerADisplayInd")
				.addContent("Y"));

		element
				.addContent(new Element("sectionEVerBDisplayInd")
						.addContent(""));

		element.addContent(new Element("logoPath").addContent(LoanDefaults
				.getImagePath()));

		return element;
	}
}
