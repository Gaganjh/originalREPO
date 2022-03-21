package com.manulife.pension.service.loan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransaction;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionBean;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionHome;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransaction;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionHome;
import com.manulife.pension.service.account.transaction.util.LoanRequestTransactionHelper;
import com.manulife.pension.service.account.valueobject.LoanAmortizationSchedule;
import com.manulife.pension.service.account.valueobject.LoanAmortizationSchedulePayment;
import com.manulife.pension.service.account.valueobject.LoanRequestPackageCharges;
import com.manulife.pension.service.account.valueobject.LoanScenario;
import com.manulife.pension.service.cache.Cache;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedulePayment;
import com.manulife.pension.service.loan.valueobject.document.Instructions;
import com.manulife.pension.service.loan.valueobject.document.LoanDocument;
import com.manulife.pension.service.loan.valueobject.document.LoanDocumentBundle;
import com.manulife.pension.service.loan.valueobject.document.LoanForm;
import com.manulife.pension.service.loan.valueobject.document.PromissoryNote;
import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.JdbcHelper;

/**
 * This class creates and populates various loan document objects.
 */
public class LoanDocumentFactory {

	public LoanDocumentFactory() {
		super();
	}

	/**
	 * Retrieves a document bundle which contains the requested documents.
	 * 
	 * @param userProfileId
	 *            The user who is calling this method.
	 * @param contractId
	 *            The contract ID
	 * @param submissionId
	 *            The submission ID
	 * @param documentClasses
	 *            An optional argument. If it's supplied, the loan document
	 *            bundle will only contain the given documents.
	 * @return
	 */
	public LoanDocumentBundle getDocumentBundle(final Loan loan,
			Class<? extends LoanDocument>... documentClasses) {

		LoanDocumentBundle bundle = new LoanDocumentBundle();

		boolean generateTruthInLendingNotice = false;
		boolean generatePromissoryNote = false;
		boolean generateAmortizationSchedule = false;
		boolean generateInstructions = false;
		boolean generateLoanForm = false;

		/*
		 * If the given document classes is empty, we populate the entire loan
		 * document bundle.
		 */
		if (documentClasses.length == 0) {
			generateTruthInLendingNotice = true;
			generatePromissoryNote = true;
			generateAmortizationSchedule = true;
			generateInstructions = true;
			generateLoanForm = true;
		}

		for (Class<? extends LoanDocument> documentClass : documentClasses) {
			if (documentClass.equals(TruthInLendingNotice.class)) {
				generateTruthInLendingNotice = true;
			} else if (documentClass.equals(PromissoryNote.class)) {
				generatePromissoryNote = true;
			} else if (documentClass.equals(AmortizationSchedule.class)) {
				generateAmortizationSchedule = true;
			} else if (documentClass.equals(Instructions.class)) {
				generateInstructions = true;
			} else if (documentClass.equals(LoanForm.class)) {
				generateLoanForm = true;
			}
		}

		/*
		 * Obtain various data objects.
		 */
		LoanPlanData loanPlanData = loan.getLoanPlanData();
		LoanParticipantData loanParticipantData = loan.getLoanParticipantData();
		LoanAmortizationSchedule loanAmortizationSchedule = getLoanAmortizationSchedule(loan);

		if (generateTruthInLendingNotice) {
			TruthInLendingNotice truthInLendingNotice = createTruthInLendingNotice(
					loan, loanPlanData, loanParticipantData,
					loanAmortizationSchedule);
			bundle.setTruthInLendingNotice(truthInLendingNotice);
		}
		if (generatePromissoryNote) {
			PromissoryNote promissoryNote = createPromissoryNote(loan,
					loanParticipantData, loanPlanData, loanAmortizationSchedule);
			bundle.setPromissoryNote(promissoryNote);
		}
		if (generateAmortizationSchedule) {
			AmortizationSchedule schedule = createAmortizationSchedule(loan,
					loanPlanData, loanParticipantData, loanAmortizationSchedule);
			bundle.setAmortizationSchedule(schedule);
		}
		if (generateInstructions) {
			Instructions instructions = createInstructions(loan, loanPlanData,
					loanParticipantData);
			bundle.setInstructionsForAdministrator(instructions);
			bundle.setInstructionsForParticipant(instructions);
		}
		if (generateLoanForm) {
			LoanForm loanForm = createLoanForm(loan, loanParticipantData,
					loanPlanData, loanAmortizationSchedule);
			bundle.setLoanForm(loanForm);
		}

		return bundle;
	}

	/**
	 * Creates new loan form object and populates the values
	 * 
	 * @param loan
	 * @param loanParticipantData
	 * @param loanPlanData
	 * @param loanAmortizationSchedule
	 * @return
	 */
	private LoanForm createLoanForm(Loan loan,
			LoanParticipantData loanParticipantData, LoanPlanData loanPlanData,
			LoanAmortizationSchedule loanAmortizationSchedule) {
		// TODO Need to add more logic and tune the existing.
		LoanForm loanForm = new LoanForm();
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		BigDecimal tpaLoanIssueFee = BigDecimal.ZERO;
		try {
			boolean isBundledGaContract = ContractServiceDelegate.getInstance()
					.hasContractWithContractProductFeature(
							loan.getContractId(),
							LoanConstants.PRODUCT_FEATURE_BUNDLED_GA);
			loanForm.setBundledGaIndicator(isBundledGaContract);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		if (loan.getFee() != null && loan.getFee().getValue() != null) {
			tpaLoanIssueFee = loan.getFee().getValue();
		}
		if (loan.getStatus().equals(LoanStateEnum.LOAN_PACKAGE.getStatusCode())
				|| loan.getStatus().equals(
						LoanStateEnum.APPROVED.getStatusCode())) {
			loanForm.setSpousalConsentReqdInd(loan.getSpousalConsentReqdInd());
		} else {
			loanForm.setSpousalConsentReqdInd(loanPlanData
					.getSpousalConsentReqdInd());
		}
		loanForm.setTpaLoanIssueFee(tpaLoanIssueFee);
		loanForm.setFirstName(loanParticipantData.getFirstName());
		loanForm.setLastName(loanParticipantData.getLastName());
		loanForm.setMiddleInitial(loanParticipantData.getMiddleInitial());
		loanForm.setAddressLine1(loanParticipantData.getAddressLine1());
		loanForm.setAddressLine2(loanParticipantData.getAddressLine2());
		loanForm.setCity(loanParticipantData.getCity());
		loanForm.setStateCode(loanParticipantData.getStateCode());
		loanForm.setZipCode(loanParticipantData.getZipCode());
		loanForm.setCountry(loanParticipantData.getCountry());
		loanForm.setSsn(loanParticipantData.getSsn());
		loanForm.setPlanName(loanPlanData.getPlanLegalName());
		loanForm.setLoanType(loan.getLoanType());
		loanForm.setContractId(loan.getContractId());
		loanForm.setSubmissionId(loan.getSubmissionId());
		LoanAmortizationSchedulePayment[] amortizationSchedulePayments = loanAmortizationSchedule
				.getLoanAmortizationPayments();
		if (amortizationSchedulePayments != null
				&& amortizationSchedulePayments.length > 0) {
			LoanAmortizationSchedulePayment finalPayment = amortizationSchedulePayments[amortizationSchedulePayments.length - 1];
			loanForm.setFinalPaymentDate(finalPayment.getPaymentDate());
		}
		if (loan.getRecipient() != null) {
			Recipient recipient = loan.getRecipient();
			Collection<Payee> payees = recipient.getPayees();
			if (payees != null && payees.size() > 0) {
				Payee payee = payees.iterator().next();
				DistributionAddress distributionAddress = payee.getAddress();
				PaymentInstruction paymentInstruction = payee
						.getPaymentInstruction();
				loanForm.setDistAddressLine1(distributionAddress
						.getAddressLine1());
				loanForm.setDistAddressLine2(distributionAddress
						.getAddressLine2());
				loanForm.setDistStateCode(distributionAddress.getStateCode());
				loanForm.setDistCity(distributionAddress.getCity());
				loanForm.setDistCountry(distributionAddress.getCountryCode());
				loanForm.setDistZipCode(distributionAddress.getZipCode());
				loanForm.setAddressToMailCheckTo(payee.getMailCheckToAddress());
				loanForm.setPaymentMethodCode(payee.getPaymentMethodCode());
				loanForm.setBankAccountTypeCode(paymentInstruction
						.getBankAccountTypeCode());
				loanForm.setBankAccountNumber(paymentInstruction
						.getBankAccountNumber());
				loanForm.setBankName(paymentInstruction.getBankName());
				if (paymentInstruction.getBankTransitNumber() != null) {
					loanForm.setAbaRoutingNumber(StringUtils.leftPad(
								String.valueOf(paymentInstruction.getBankTransitNumber()),9,"0"));
				}
			}
		}
		loanForm.setLoanAmount(loanParameter.getLoanAmount());
		loanForm.setLoanInterestRate(loanParameter.getInterestRate());
		loanForm.setManulifeCompanyId(loanPlanData.getManulifeCompanyId());
		populateMoneyTypes(loanForm, loan);
		return loanForm;
	}

	/**
	 * Populates the Money types to be included and excluded
	 * 
	 * @param loanForm
	 * @param loan
	 * @return
	 */
	private LoanForm populateMoneyTypes(LoanForm loanForm, Loan loan) {
		List<LoanMoneyType> moneyTypes;
		List<String> moneyTypesExc = new ArrayList<String>();
		List<String> moneyTypesInc = new ArrayList<String>();
		String temp = JdbcHelper.INDICATOR_NO;
		moneyTypes = loan.getMoneyTypesWithAccountBalance();
		for (LoanMoneyType moneyType : moneyTypes) {
			boolean moneyTypeExInd = moneyType.getExcludeIndicator();
			if (moneyTypeExInd == true) {
				temp = JdbcHelper.INDICATOR_YES;
			}
		}
		if (temp.equals(JdbcHelper.INDICATOR_YES)) {
			loanForm.setMoneyTypeInd(JdbcHelper.INDICATOR_YES);
			for (LoanMoneyType moneyType : moneyTypes) {
				if (moneyType.getExcludeIndicator()) {
					moneyTypesExc
							.add(moneyType.getContractMoneyTypeShortName());
				} else {
					moneyTypesInc
							.add(moneyType.getContractMoneyTypeShortName());
				}
			}
			loanForm.setMoneyTypesExc(moneyTypesExc);
			if (moneyTypesInc.size() == 1) {
				loanForm.setLoanAmountDisplayInd(JdbcHelper.INDICATOR_YES);
			} else {
				loanForm.setLoanAmountDisplayInd(JdbcHelper.INDICATOR_NO);
			}
			loanForm.setMoneyTypesInc(moneyTypesInc);
		} else {
			loanForm.setMoneyTypeInd(JdbcHelper.INDICATOR_NO);
		}
		return loanForm;
	}

	private Instructions createInstructions(Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData) {
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		Instructions instructions = new Instructions();

		// Current Date
		Calendar cal = Calendar.getInstance();
		instructions.setCurrentDate(cal.getTime());

		// Loan status create date plus 30 days
		cal.setTime(loan.getLastUpdated());
		cal.add(Calendar.DATE, 30);
		instructions.setLoanPackageStatusEffectiveDate(cal.getTime());
		
		// setting the spousal consent to unspecified to start with, it will get replaced by 
		// either plan data or loan data
		instructions.setSpousalConsentRequiredInd(PlanData.UNSPECIFIED_CODE);
		if (isDraftDocument(loan)) {
			if (loanPlanData.getSpousalConsentReqdInd() != null) {
				instructions.setSpousalConsentRequiredInd(loanPlanData
						.getSpousalConsentReqdInd());
			}
		} else {
			if (loan.getSpousalConsentReqdInd() != null) {
				instructions.setSpousalConsentRequiredInd(loan
						.getSpousalConsentReqdInd());
			} 
		}

		instructions.setFirstName(loanParticipantData.getFirstName());
		instructions.setLastName(loanParticipantData.getLastName());
		instructions.setMiddleInitial(loanParticipantData.getMiddleInitial());
		if (loan.getLegallyMarriedInd() != null) {
			instructions.setLegallyMarriedInd(loan.getLegallyMarriedInd());
		} else {
			instructions.setLegallyMarriedInd(false);
		}

		if (loan.getFee() != null && loan.getFee().getValue() != null) {
			instructions.setTpaLoanIssueFee(loan.getFee().getValue());
		}
		
		instructions.setLoanType(loan.getLoanType());
		instructions.setManulifeCompanyId(loanPlanData.getManulifeCompanyId());
		instructions.setContractName(loanPlanData.getContractName());
		instructions.setLoanAmount(loanParameter.getLoanAmount());

		List<LoanMoneyType> moneyTypes;
		moneyTypes = loan.getMoneyTypesWithAccountBalance();
		String temp = JdbcHelper.INDICATOR_NO;

		for (LoanMoneyType moneyType : moneyTypes) {
			boolean moneyTypeExInd = moneyType.getExcludeIndicator();
			if (moneyTypeExInd == true) {
				temp = JdbcHelper.INDICATOR_YES;
				instructions.setSectionEVerBDisplayInd(temp);
				break;
			}
		}
		if (temp.equals(JdbcHelper.INDICATOR_NO)) {
			instructions.setSectionEVerADisplayInd(JdbcHelper.INDICATOR_YES);
		}

		// Getting TPA firm name
		TPAFirmInfo firmInfo = null;
		try {
			firmInfo = TPAServiceDelegate.getInstance()
					.getFirmInfoByContractId(loan.getContractId());
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

		instructions.setTpaFirmName(firmInfo.getName());
		return instructions;
	}

	private PromissoryNote createPromissoryNote(Loan loan,
			LoanParticipantData loanParticipantData, LoanPlanData loanPlanData,
			LoanAmortizationSchedule loanAmortizationSchedule) {
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		PromissoryNote promissoryNote = new PromissoryNote();
		promissoryNote.setFirstName(loanParticipantData.getFirstName());
		promissoryNote.setLastName(loanParticipantData.getLastName());
		promissoryNote.setMiddleInitial(loanParticipantData.getMiddleInitial());
		promissoryNote.setPlanName(loanPlanData.getPlanLegalName());
		promissoryNote.setLoanAmount(loanParameter.getLoanAmount());
		promissoryNote.setLoanInterestRate(loanParameter.getInterestRate());
		promissoryNote.setContractName(loanPlanData.getContractName());
		promissoryNote.setDefaultProvision(loan.getDefaultProvision());
		promissoryNote.setMaximumLoanPercentage(loanPlanData
				.getMaximumLoanPercentage());
		promissoryNote.setLoanStatus(loan.getStatus());

		if (loan.getAcceptedParameter() != null
				&& loan.getAcceptedParameter().getCreated() != null) {
			promissoryNote.setLoanAcceptedDate(loan.getAcceptedParameter()
					.getCreated());
		}

		if (loan.isParticipantInitiated()) {
			promissoryNote.setParticipanInitiatedLoan(JdbcHelper.INDICATOR_YES);
		}

		return promissoryNote;
	}

	private TruthInLendingNotice createTruthInLendingNotice(Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanAmortizationSchedule loanAmortizationSchedule) {
		TruthInLendingNotice notice = new TruthInLendingNotice();
		Date startDate = loan.getEffectiveDate();
		Date firstPaymentDate = loan.getFirstPayrollDate();
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		BigDecimal tpaLoanIssueFee = BigDecimal.ZERO;
		if (loan.getFee() != null && loan.getFee().getValue() != null) {
			tpaLoanIssueFee = loan.getFee().getValue();
		}

		BigDecimal maximumLoanPercentage = null;
		BigDecimal expenseRatio = null;
		BigDecimal monthlyFlatFee = null;

		if (isDraftDocument(loan)) {
			/*
			 * Draft document uses data from Plan Data, otherwise, use saved
			 * data from loan request.
			 */
			expenseRatio = loanPlanData.getContractLoanExpenseMarginPct();
			monthlyFlatFee = loanPlanData.getContractLoanMonthlyFlatFee();
			maximumLoanPercentage = loanPlanData.getMaximumLoanPercentage();
		} else {
			expenseRatio = loan.getContractLoanExpenseMarginPct();
			monthlyFlatFee = loan.getContractLoanMonthlyFlatFee();
			maximumLoanPercentage = loan.getMaximumLoanPercentage();
		}

		expenseRatio = expenseRatio.divide(
				LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES, 20,
				BigDecimal.ROUND_HALF_UP);

		LoanRequestPackageCharges loanRequestPackageCharges = null;
		try {
			LoanRequestPackageTransaction loanRequestPackageTransaction = ((LoanRequestPackageTransactionHome) getHome(LoanRequestPackageTransactionHome.class))
					.create();
			loanRequestPackageCharges = loanRequestPackageTransaction
					.getLoanRequestPackageCharges(loanAmortizationSchedule,
							startDate, firstPaymentDate, tpaLoanIssueFee,
							expenseRatio, monthlyFlatFee);
		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}

		notice.setFirstPaymentDate(firstPaymentDate);
		notice.setFinanceCharge(loanRequestPackageCharges.getFinanceCharges());
		notice.setAnnualPercentageRate(loanRequestPackageCharges
				.getAnnualPercentageRate().multiply(
						LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));
		notice.setTotalOfPayments(loanRequestPackageCharges
				.getTotalOfPayments());
		notice.setPaymentAmount(loanAmortizationSchedule.getLoanScenario()
				.getPaymentAmount());
		notice.setLoanAmount(loanParameter.getLoanAmount());

		notice.setMaximumLoanPercentage(maximumLoanPercentage);
		notice.setPlanName(loanPlanData.getPlanLegalName());
		notice.setContractName(loanPlanData.getContractName());
		notice.setDefaultProvision(loan.getDefaultProvision());
		notice.setFirstName(loanParticipantData.getFirstName());
		notice.setLastName(loanParticipantData.getLastName());
		notice.setMiddleInitial(loanParticipantData.getMiddleInitial());
		notice.setLoanStartDate(loan.getEffectiveDate());
		notice.setPaymentFrequency(loanParameter.getPaymentFrequency());
		notice.setAmortizationMonths(loanParameter.getAmortizationMonths());
		notice.setLoanStatus(loan.getStatus());

		if (loan.getAcceptedParameter() != null
				&& loan.getAcceptedParameter().getCreated() != null) {
			notice
					.setLoanAcceptedDate(loan.getAcceptedParameter()
							.getCreated());
		}

		if (loan.isParticipantInitiated()) {
			notice.setParticipanInitiatedLoan(JdbcHelper.INDICATOR_YES);
		}

		return notice;
	}

	/**
	 * Creates a new amortization schedule object using the given loan object.
	 * 
	 * @param loan
	 * @return
	 */
	private AmortizationSchedule createAmortizationSchedule(Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanAmortizationSchedule loanAmortizationSchedule) {
		AmortizationSchedule schedule = new AmortizationSchedule();
		Date startDate = loan.getEffectiveDate();
		Date firstPaymentDate = loan.getFirstPayrollDate();
		LoanParameter loanParameter = loan.getCurrentLoanParameter();
		populateAmortizationSchedule(schedule, loanAmortizationSchedule,
				startDate, firstPaymentDate, loanParameter);

		schedule.setFirstName(loanParticipantData.getFirstName());
		schedule.setLastName(loanParticipantData.getLastName());
		schedule.setMiddleInitial(loanParticipantData.getMiddleInitial());
		schedule.setContractName(loanPlanData.getContractName());
		schedule.setSocialSecurityNumber(loanParticipantData.getSsn());
		schedule.setEmployeeNumber(loanParticipantData.getEmployeeNumber());

		return schedule;
	}

	/**
	 * Calculate the loan amortization schedule. This schedule is reused in
	 * other documents.
	 * 
	 * @param startDate
	 * @param firstPaymentDate
	 * @param loanParameter
	 * @return
	 */
	public static LoanAmortizationSchedule getLoanAmortizationSchedule(Loan loan) {
		Date startDate = loan.getEffectiveDate();
		Date firstPaymentDate = loan.getFirstPayrollDate();
		LoanParameter loanParameter = loan.getCurrentLoanParameter();

		LoanScenario loanScenario = new LoanScenario();
		loanScenario
				.setAmortizationYears(loanParameter.getAmortizationMonths() / 12);
		loanScenario.setAmortizationMonths(loanParameter
				.getAmortizationMonths() % 12);
		loanScenario.setLoanAmount(loanParameter.getLoanAmount());
		loanScenario.setPaymentAmount(loanParameter.getPaymentAmount());
		loanScenario.setInterestRate(loanParameter.getInterestRate().divide(
				LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));

		int paymentPeriodsPerYear = 0;
		if (GlobalConstants.FREQUENCY_TYPE_WEEKLY.equals(loanParameter
				.getPaymentFrequency())) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_WEEKLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY
				.equals(loanParameter.getPaymentFrequency())) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_BI_WEEKLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY
				.equals(loanParameter.getPaymentFrequency())) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_SEMI_MONTHLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_MONTHLY.equals(loanParameter
				.getPaymentFrequency())) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_MONTHLY_PERIODS_PER_YEAR;
		}

		loanScenario.setPaymentPeriodsPerYear(paymentPeriodsPerYear);
		LoanAmortizationSchedule amortizationSchedule = null;

		try {
			LoanAmortizationTransaction amortizationTransaction = ((LoanAmortizationTransactionHome) getHome(LoanAmortizationTransactionHome.class))
					.create();
			amortizationSchedule = amortizationTransaction.execute(startDate,
					firstPaymentDate, loanScenario);

			StringBuffer errorStringBuffer = new StringBuffer();
			int[] errorCodes = amortizationSchedule.getLoanScenario().getErrorCodes();
			if (errorCodes != null) {
				for (int errorCode : errorCodes) {
					switch (errorCode) {
					case LoanScenario.ERROR_CODE_BAD_INTEREST_RATE:
						errorStringBuffer.append(" Interest rate less than minimum [" + LoanAmortizationTransactionBean.MINIMUM_INTEREST_RATE + "]");
						break;
					case LoanScenario.ERROR_CODE_BAD_PAYROLL_FREQUENCY:
						errorStringBuffer.append(" Payroll frequency does not match payment periods per year");
						break;
					case LoanScenario.ERROR_CODE_BAD_AMORTIZATION_YEARS:
						errorStringBuffer.append(" Amortization years <= 0");
						break;
					case LoanScenario.ERROR_CODE_BAD_PAYMENT_AMOUNT:
						errorStringBuffer.append(" Payment amount <= 0");
						break;
					case LoanScenario.ERROR_CODE_BAD_LOAN_AMOUNT:
						errorStringBuffer.append(" Loan amount <= 0");
						break;
					case LoanAmortizationSchedule.ERROR_CODE_MISSING_START_DATE:
						errorStringBuffer.append(" Missing loan start date");
						break;
					}
				}
			}
			errorCodes = amortizationSchedule.getErrorCodes();
			if (errorCodes != null) {
				for (int errorCode : errorCodes) {
					switch (errorCode) {
					case LoanAmortizationSchedule.ERROR_CODE_MISSING_FIRST_PAYMENT_DATE:
						errorStringBuffer.append(" Missing first payroll date");
						break;
					case LoanAmortizationSchedule.ERROR_CODE_FIRST_PAYMENT_DATE_TOO_LOW:
						errorStringBuffer.append(" First payroll date less than loan start date");
						break;

					}
				}
			}
			
			if (errorStringBuffer.length() > 0) {
				throw new IllegalArgumentException(errorStringBuffer.toString());
			}

		} catch (Exception e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
		return amortizationSchedule;
	}

	/**
	 * Populate the given amortization schedule object with the calculated
	 * values.
	 * 
	 * @param startDate
	 *            Start date of the loan
	 * @param firstPaymentDate
	 *            date of the first payment
	 * @param loanParameter
	 *            contains loan parameters
	 */
	private void populateAmortizationSchedule(AmortizationSchedule schedule,
			LoanAmortizationSchedule amortizationSchedule, Date startDate,
			Date firstPaymentDate, LoanParameter loanParameter) {

		schedule.setLoanAmount(loanParameter.getLoanAmount());
		schedule.setPaymentAmount(amortizationSchedule.getLoanScenario()
				.getPaymentAmount());
		schedule.setPaymentFrequency(loanParameter.getPaymentFrequency());
		schedule.setLoanStartDate(startDate);
		schedule.setFirstPaymentDate(firstPaymentDate);
		schedule.setEffectiveAnnualRate(amortizationSchedule
				.getAnnualEffectiveInterestRate().multiply(
						LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));
		schedule.setLoanInterestRate(loanParameter.getInterestRate());
		schedule.setDailyRate(amortizationSchedule.getDailyInterestRate()
				.multiply(LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));
		BigDecimal adjustedInterestRatePerPeriod = LoanRequestTransactionHelper
				.calculateAdjustedInterestRatePerPeriod(amortizationSchedule
						.getAnnualEffectiveInterestRate(), amortizationSchedule
						.getLoanScenario().getPaymentPeriodsPerYear());
		schedule.setPeriodicRate(adjustedInterestRatePerPeriod
				.multiply(LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));

		LoanAmortizationSchedulePayment[] amortizationSchedulePayments = amortizationSchedule
				.getLoanAmortizationPayments();

		if (amortizationSchedulePayments != null
				&& amortizationSchedulePayments.length > 0) {
			LoanAmortizationSchedulePayment finalPayment = amortizationSchedulePayments[amortizationSchedulePayments.length - 1];
			LoanAmortizationSchedulePayment secondLastPayment = null;

			if (amortizationSchedulePayments.length > 1) {
				secondLastPayment = amortizationSchedulePayments[amortizationSchedulePayments.length - 2];
				schedule.setSecondLastPaymentDate(secondLastPayment
						.getPaymentDate());
			}

			schedule.setFinalPaymentDate(finalPayment.getPaymentDate());
			schedule.setFinalPaymentAmount(finalPayment.getPaymentAmount());

			if (amortizationSchedule.getLoanScenario()
					.getFinalPaymentAmountAdjustment().compareTo(
							BigDecimal.ZERO) != 0) {
				schedule.setFinalPaymentDifferent(true);
			} else {
				schedule.setFinalPaymentDifferent(false);
			}
			
			schedule.setNumberOfPayments(amortizationSchedulePayments.length);
			schedule
					.setNumberOfPaymentsLessOne((amortizationSchedulePayments.length) - 1);

			for (LoanAmortizationSchedulePayment amortizationSchedulePayment : amortizationSchedulePayments) {
				AmortizationSchedulePayment payment = new AmortizationSchedulePayment();
				payment.setLoanPrincipalRemaining(amortizationSchedulePayment
						.getLoanPrincipalRemaining());
				payment.setPaymentAmount(amortizationSchedulePayment
						.getPaymentAmount());
				payment.setPaymentAmountInterest(amortizationSchedulePayment
						.getPaymentAmountInterest());
				payment.setPaymentAmountPrincipal(amortizationSchedulePayment
						.getPaymentAmountPrincipal());
				payment.setPaymentDate(amortizationSchedulePayment
						.getPaymentDate());
				payment.setTotalPaymentAmount(amortizationSchedulePayment
						.getTotalPaymentAmount());
				payment
						.setTotalPaymentAmountInterest(amortizationSchedulePayment
								.getTotalPaymentAmountInterest());
				schedule.getLoanPayments().add(payment);
			}
		}
	}

	public boolean isDraftDocument(Loan loan) {
		if (LoanStateEnum.APPROVED.getStatusCode().equals(loan.getStatus())
				|| LoanStateEnum.APPROVED.isBefore(loan.getStatus())) {
			/*
			 * If the loan has already been approved, the documents are final.
			 * Since APPROVED status is before the LOAN_PACKAGE status
			 * (according to LoanStateEnum), therefore, loan package will also
			 * produce final loan documents.
			 */
			return false;
		}
		if (LoanConstants.USER_ROLE_PARTICIPANT_CODE.equals(loan
				.getCreatedByRoleCode())) {
			if (LoanStateEnum.PENDING_ACCEPTANCE.isBefore(loan.getStatus())) {
				/*
				 * If the loan is a participant initiated, then the loan
				 * document becomes final after acceptance.
				 */
				return false;
			}
		}
		return true;
	}

	protected static EJBHome getHome(Class homeClass) {

		EJBHome home = (EJBHome) Cache.getFromCache(EJBHome.class.getName(),
				homeClass.getName());

		if (home == null) {
			try {
				Context context = new InitialContext();
				home = (EJBHome) PortableRemoteObject.narrow(context
						.lookup(homeClass.getName()), homeClass);
				Cache.cacheObject(EJBHome.class.getName(), homeClass.getName(),
						home, Cache.EXPIRES_NEVER);
			} catch (Exception e) {
				throw ExceptionHandlerUtility.wrap(e);
			}
		}
		return home;
	}

}