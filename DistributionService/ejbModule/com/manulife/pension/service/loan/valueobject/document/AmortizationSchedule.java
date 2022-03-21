package com.manulife.pension.service.loan.valueobject.document;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jdom.Element;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.loan.util.LoanDocumentHelper;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.util.render.SSNRender;

public class AmortizationSchedule extends BaseSerializableCloneableObject
		implements LoanDocument, XMLValueObject {

	private static final long serialVersionUID = 1L;

	private String contractName;
	private String firstName;
	private String lastName;
	private String middleInitial;
	private String socialSecurityNumber;
	private String employeeNumber;
	private String paymentFrequency;
	private BigDecimal loanInterestRate;
	private BigDecimal effectiveAnnualRate;
	private BigDecimal periodicRate;
	private BigDecimal dailyRate;
	private Date loanStartDate;
	private Date firstPaymentDate;
	private Date finalPaymentDate;
	private BigDecimal loanAmount;
	private BigDecimal paymentAmount;
	private BigDecimal finalPaymentAmount;
	private Date secondLastPaymentDate;
	private Integer numberOfPayments;
	private Integer numberOfPaymentsLessOne;
	private boolean finalPaymentDifferent;

	private List<AmortizationSchedulePayment> loanPayments = new ArrayList<AmortizationSchedulePayment>();

	public AmortizationSchedule() {
		super();
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
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

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public BigDecimal getLoanInterestRate() {
		return loanInterestRate;
	}

	public void setLoanInterestRate(BigDecimal loanInterestRate) {
		this.loanInterestRate = loanInterestRate;
	}

	public BigDecimal getEffectiveAnnualRate() {
		return effectiveAnnualRate;
	}

	public void setEffectiveAnnualRate(BigDecimal effectiveAnnualRate) {
		this.effectiveAnnualRate = effectiveAnnualRate;
	}

	public BigDecimal getPeriodicRate() {
		return periodicRate;
	}

	public void setPeriodicRate(BigDecimal periodicRate) {
		this.periodicRate = periodicRate;
	}

	public BigDecimal getDailyRate() {
		return dailyRate;
	}

	public void setDailyRate(BigDecimal dailyRate) {
		this.dailyRate = dailyRate;
	}

	public Date getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(Date loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public Date getFinalPaymentDate() {
		return finalPaymentDate;
	}

	public void setFinalPaymentDate(Date finalPaymentDate) {
		this.finalPaymentDate = finalPaymentDate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Date getSecondLastPaymentDate() {
		return secondLastPaymentDate;
	}

	public void setSecondLastPaymentDate(Date secondLastPaymentDate) {
		this.secondLastPaymentDate = secondLastPaymentDate;
	}

	public Integer getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public List<AmortizationSchedulePayment> getLoanPayments() {
		return loanPayments;
	}

	public void setLoanPayments(List<AmortizationSchedulePayment> loanPayments) {
		this.loanPayments = loanPayments;
	}

	public boolean isFinalPaymentDifferent() {
		return finalPaymentDifferent;
	}

	public void setFinalPaymentDifferent(boolean finalPaymentDifferent) {
		this.finalPaymentDifferent = finalPaymentDifferent;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public BigDecimal getFinalPaymentAmount() {
		return finalPaymentAmount;
	}

	public void setFinalPaymentAmount(BigDecimal finalPaymentAmount) {
		this.finalPaymentAmount = finalPaymentAmount;
	}

	public Integer getNumberOfPaymentsLessOne() {
		return numberOfPaymentsLessOne;
	}

	public void setNumberOfPaymentsLessOne(Integer numberOfPaymentsLessOne) {
		this.numberOfPaymentsLessOne = numberOfPaymentsLessOne;
	}

	/**
	 * generates the XML for the Amortization Schedule data
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

		SimpleDateFormat SHORT_MDY_SLASHED = new SimpleDateFormat("MM/dd/yyyy");

		Element element = new Element("AmortizationSchedule", "");

		element.addContent(new Element("participantName")
				.addContent(LoanDocumentHelper.getFormattedParticipantName(
						getFirstName(), getLastName(), getMiddleInitial())));

		element.addContent(new Element("contractName")
				.addContent(getContractName()));

		element.addContent(new Element("socialSecurityNumber")
				.addContent(SSNRender.format(getSocialSecurityNumber(), "")));

		element.addContent(new Element("employeeNumber")
				.addContent(getEmployeeNumber()));

		element.addContent(new Element("paymentFrequency")
				.addContent(LoanDocumentHelper
						.paymentFrequency(getPaymentFrequency())));

		element.addContent(new Element("loanInterestRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getLoanInterestRate())));

		element.addContent(new Element("effectiveAnnualRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getEffectiveAnnualRate())));

		element.addContent(new Element("periodicRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getPeriodicRate())));

		element.addContent(new Element("dailyRate")
				.addContent(LoanDocumentHelper
						.percentageFormatter(getDailyRate())));

		element.addContent(new Element("loanEffectiveDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getLoanStartDate(), SHORT_MDY_SLASHED)));

		element.addContent(new Element("firstPaymentDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getFirstPaymentDate(), SHORT_MDY_SLASHED)));

		element.addContent(new Element("finalPaymentDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getFinalPaymentDate(), SHORT_MDY_SLASHED)));

		element.addContent(new Element("loanAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getLoanAmount())));

		element.addContent(new Element("paymentAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getPaymentAmount())));

		element.addContent(new Element("finalPaymentAmount")
				.addContent(LoanDocumentHelper
						.currencyFormatter(getFinalPaymentAmount())));
		
		if (isFinalPaymentDifferent()) {
			element.addContent(new Element("isFinalPaymentDifferent")
					.addContent(JdbcHelper.INDICATOR_YES));
		} else {
			element.addContent(new Element("isFinalPaymentDifferent")
					.addContent(JdbcHelper.INDICATOR_NO));
		}
		
		element.addContent(new Element("numberOfPayments")
				.addContent(LoanDocumentHelper
						.getStringValue(getNumberOfPayments())));

		element.addContent(new Element("numberOfPaymentsLessOne")
				.addContent(LoanDocumentHelper
						.getStringValue(getNumberOfPaymentsLessOne())));

		element.addContent(new Element("secondLastPaymentDate")
				.addContent(LoanDocumentHelper.dateFormatter(
						getSecondLastPaymentDate(), SHORT_MDY_SLASHED)));

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(getFirstPaymentDate());
		int startingYear = calStart.get(Calendar.YEAR);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(getFinalPaymentDate());
		int endYear = calEnd.get(Calendar.YEAR);

		Element loanPayment = null;

		BigDecimal grandTotalPayment = new BigDecimal(0);
		BigDecimal grandTotalInterest = new BigDecimal(0);
		BigDecimal grandTotalPrincipal = new BigDecimal(0);

		int paymentCount = 1;

		// Framing elements for each payment
		for (int year = startingYear; year <= endYear; year++) {

			BigDecimal totalPayment = new BigDecimal(0);
			BigDecimal totalInterest = new BigDecimal(0);
			BigDecimal totalPrincipal = new BigDecimal(0);
			List<AmortizationSchedulePayment> payments = new ArrayList<AmortizationSchedulePayment>();

			for (AmortizationSchedulePayment amortizationSchedulePayment : getLoanPayments()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(amortizationSchedulePayment.getPaymentDate());
				int paymentYear = cal.get(Calendar.YEAR);

				if (paymentYear == year) {
					payments.add(amortizationSchedulePayment);

					loanPayment = new Element("loanPayment");

					loanPayment.addContent(new Element("count")
							.addContent(LoanDocumentHelper
									.getStringValue(paymentCount)));

					loanPayment.addContent(new Element("paymentDate")
							.addContent(LoanDocumentHelper.dateFormatter(
									amortizationSchedulePayment
											.getPaymentDate(),
									SHORT_MDY_SLASHED)));

					loanPayment
							.addContent(new Element("paymentAmount")
									.addContent(LoanDocumentHelper
											.currencyFormatter(amortizationSchedulePayment
													.getPaymentAmount())));

					loanPayment
							.addContent(new Element("paymentAmountInterest")
									.addContent(LoanDocumentHelper
											.currencyFormatter(amortizationSchedulePayment
													.getPaymentAmountInterest())));

					loanPayment
							.addContent(new Element("paymentAmountPrincipal")
									.addContent(LoanDocumentHelper
											.currencyFormatter(amortizationSchedulePayment
													.getPaymentAmountPrincipal())));

					loanPayment
							.addContent(new Element("loanPrincipalRemaining")
									.addContent(LoanDocumentHelper
											.currencyFormatter(amortizationSchedulePayment
													.getLoanPrincipalRemaining())));

					loanPayment.addContent(new Element("summaryInd")
							.addContent(JdbcHelper.INDICATOR_NO));

					element.addContent(loanPayment);

					// Yearly total calulation
					totalPayment = totalPayment.add(amortizationSchedulePayment
							.getPaymentAmount());
					totalInterest = totalInterest
							.add(amortizationSchedulePayment
									.getPaymentAmountInterest());
					totalPrincipal = totalPrincipal
							.add(amortizationSchedulePayment
									.getPaymentAmountPrincipal());
					paymentCount++;
				}
			}

			// Elements to display yearly total row
			loanPayment = new Element("loanPayment");

			loanPayment.addContent(new Element("totalLabel").addContent(year
					+ " Totals"));

			loanPayment.addContent(new Element("totalPayment")
					.addContent(LoanDocumentHelper
							.currencyFormatter(totalPayment)));

			loanPayment.addContent(new Element("totalInterest")
					.addContent(LoanDocumentHelper
							.currencyFormatter(totalInterest)));

			loanPayment.addContent(new Element("totalPrincipal")
					.addContent(LoanDocumentHelper
							.currencyFormatter(totalPrincipal)));

			loanPayment.addContent(new Element("summaryInd")
					.addContent(JdbcHelper.INDICATOR_YES));

			element.addContent(loanPayment);

			// Grand total calculation
			grandTotalPayment = grandTotalPayment.add(totalPayment);
			grandTotalInterest = grandTotalInterest.add(totalInterest);
			grandTotalPrincipal = grandTotalPrincipal.add(totalPrincipal);

		}

		// Elements to display grand total row
		loanPayment = new Element("grandTotals");

		loanPayment.addContent(new Element("grandtotalPayment")
				.addContent(LoanDocumentHelper
						.currencyFormatter(grandTotalPayment)));

		loanPayment.addContent(new Element("grandTotalInterest")
				.addContent(LoanDocumentHelper
						.currencyFormatter(grandTotalInterest)));

		loanPayment.addContent(new Element("grandTotalPrincipal")
				.addContent(LoanDocumentHelper
						.currencyFormatter(grandTotalPrincipal)));

		element.addContent(loanPayment);

		return element;
	}
}
