package com.manulife.pension.service.loan;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.naming.InitialContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransaction;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionBean;
import com.manulife.pension.service.account.transaction.LoanAmortizationTransactionHome;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransaction;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionBean;
import com.manulife.pension.service.account.transaction.LoanRequestPackageTransactionHome;
import com.manulife.pension.service.account.valueobject.LoanAmortizationSchedule;
import com.manulife.pension.service.account.valueobject.LoanAmortizationSchedulePayment;
import com.manulife.pension.service.account.valueobject.LoanScenario;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.WebLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule;
import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedulePayment;
import com.manulife.pension.service.loan.valueobject.document.LoanDocumentBundle;
import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;

public class LoanDocumentFactoryTestOld extends
		DistributionContainerEnvironment {

	private LoanAmortizationTransactionHome loanAmortizationTransactionHome;
	private String[] datePattern = { "MM/dd/yyyy" };
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		registerMockService(LoanAmortizationTransactionHome.class,
				LoanAmortizationTransaction.class,
				LoanAmortizationTransactionBean.class);
		registerMockService(LoanRequestPackageTransactionHome.class,
				LoanRequestPackageTransaction.class,
				LoanRequestPackageTransactionBean.class);
		InitialContext context = new InitialContext();
		loanAmortizationTransactionHome = (LoanAmortizationTransactionHome) context
				.lookup(LoanAmortizationTransactionHome.class.getName());
	}

	private LoanAmortizationSchedule getLoanAmortizationSchedule(
			Date startDate, Date firstPaymentDate, BigDecimal loanAmount,
			Integer amortizationYears, String paymentFrequency,
			BigDecimal annualNominalInterestRate) throws Exception {

		LoanAmortizationTransaction loanAmortizationTransaction = loanAmortizationTransactionHome
				.create();
		LoanScenario loanScenario = new LoanScenario();
		loanScenario.setLoanAmount(loanAmount);
		loanScenario.setPaymentAmount(BigDecimal.ONE);
		loanScenario.setAmortizationYears(amortizationYears);
		loanScenario.setInterestRate(annualNominalInterestRate.divide(
				LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));

		Integer paymentPeriodsPerYear = 0;
		if (GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY.equals(paymentFrequency)) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_BI_WEEKLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_MONTHLY
				.equals(paymentFrequency)) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_MONTHLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY
				.equals(paymentFrequency)) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_SEMI_MONTHLY_PERIODS_PER_YEAR;
		} else if (GlobalConstants.FREQUENCY_TYPE_WEEKLY
				.equals(paymentFrequency)) {
			paymentPeriodsPerYear = LoanConstants.FREQUENCY_TYPE_WEEKLY_PERIODS_PER_YEAR;
		}

		loanScenario.setPaymentPeriodsPerYear(paymentPeriodsPerYear);
		return loanAmortizationTransaction.execute(startDate, firstPaymentDate,
				loanScenario);
	}

	private AmortizationSchedule getAmortizationSchedule(Date startDate,
			Date firstPaymentDate, BigDecimal loanAmount,
			Integer amortizationMonths, String paymentFrequency,
			BigDecimal annualNominalInterestRate) throws Exception {
		LoanParameter loanParameter = new LoanParameter();
		loanParameter.setAmortizationMonths(amortizationMonths);
		loanParameter.setLoanAmount(loanAmount);
		loanParameter.setInterestRate(annualNominalInterestRate);
		loanParameter.setPaymentFrequency(paymentFrequency);
		loanParameter.setPaymentAmount(BigDecimal.ONE);

		Loan loan = new Loan();
		loan.setDataRetriever(new WebLoanSupportDataRetriever());
		loan.setContractId(68350);
		loan.setParticipantProfileId(138845003);
		loan.setEffectiveDate(startDate);
		loan.setFirstPayrollDate(firstPaymentDate);
		loan.setAcceptedParameter(loanParameter);

		LoanDocumentFactory factory = new LoanDocumentFactory();
		LoanDocumentBundle bundle = factory.getDocumentBundle(loan,
				AmortizationSchedule.class);
		return bundle.getAmortizationSchedule();
	}

	private TruthInLendingNotice getTruthInLendingNotice(Date startDate,
			Date firstPaymentDate, BigDecimal loanAmount,
			Integer amortizationMonths, String paymentFrequency,
			BigDecimal annualNominalInterestRate) throws Exception {
		LoanParameter loanParameter = new LoanParameter();
		loanParameter.setAmortizationMonths(amortizationMonths);
		loanParameter.setLoanAmount(loanAmount);
		loanParameter.setInterestRate(annualNominalInterestRate);
		loanParameter.setPaymentFrequency(paymentFrequency);
		loanParameter.setPaymentAmount(BigDecimal.ONE);

		Loan loan = new Loan();
		loan.setDataRetriever(new WebLoanSupportDataRetriever());
		loan.setContractId(68350);
		loan.setParticipantProfileId(138845003);
		loan.setEffectiveDate(startDate);
		loan.setFirstPayrollDate(firstPaymentDate);
		loan.setAcceptedParameter(loanParameter);

		LoanDocumentFactory factory = new LoanDocumentFactory();
		LoanDocumentBundle bundle = factory.getDocumentBundle(loan,
				TruthInLendingNotice.class);
		return bundle.getTruthInLendingNotice();
	}

	private void assertScheduleEquals(Date startDate, Date firstPaymentDate,
			LoanAmortizationSchedule oldSchedule, AmortizationSchedule schedule)
			throws Exception {

		Assert.assertEquals("Errors size not zero ["
				+ ToStringBuilder.reflectionToString(oldSchedule) + "]", 0,
				oldSchedule.getErrorCodes().length);
		Assert.assertEquals("Errors size not zero ["
				+ ToStringBuilder.reflectionToString(oldSchedule
						.getLoanScenario()) + "]", 0, oldSchedule
				.getLoanScenario().getErrorCodes().length);

		Assert.assertEquals(oldSchedule.getLoanAmortizationPayments().length,
				schedule.getLoanPayments().size());

		for (int i = 0; i < oldSchedule.getLoanAmortizationPayments().length; i++) {
			LoanAmortizationSchedulePayment oldPayment = oldSchedule
					.getLoanAmortizationPayments()[i];
			AmortizationSchedulePayment payment = schedule.getLoanPayments()
					.get(i);
			System.out.println(oldSchedule.getLoanAmortizationPayments().length);
			Assert.assertNotNull(payment.getLoanPrincipalRemaining());
			Assert.assertNotNull(payment.getPaymentAmount());
			Assert.assertNotNull(payment.getPaymentAmountInterest());
			Assert.assertNotNull(payment.getPaymentAmountPrincipal());
			Assert.assertNotNull(payment.getTotalPaymentAmount());
			Assert.assertNotNull(payment.getTotalPaymentAmountInterest());
			if (startDate == null) {
				Assert.assertNull(payment.getPaymentDate());
			} else {
				Assert.assertNotNull(payment.getPaymentDate());
				if (i == 0) {
					Assert.assertEquals(firstPaymentDate, payment
							.getPaymentDate());
				}
			}
			System.out.println(oldPayment.getLoanPrincipalRemaining());
			System.out.println(payment
					.getLoanPrincipalRemaining());

			Assert.assertEquals(oldPayment.getLoanPrincipalRemaining(), payment
					.getLoanPrincipalRemaining());
			Assert.assertEquals(oldPayment.getPaymentAmount(), payment
					.getPaymentAmount());
			Assert.assertEquals(oldPayment.getPaymentAmountInterest(), payment
					.getPaymentAmountInterest());
			Assert.assertEquals(oldPayment.getPaymentAmountPrincipal(), payment
					.getPaymentAmountPrincipal());
			Assert.assertEquals(oldPayment.getPaymentDate(), payment
					.getPaymentDate());
			Assert.assertEquals(oldPayment.getTotalPaymentAmount(), payment
					.getTotalPaymentAmount());
			Assert.assertEquals(oldPayment.getTotalPaymentAmountInterest(),
					payment.getTotalPaymentAmountInterest());
		}

	}

	public void test1() throws Exception {
		LoanAmortizationSchedule oldSchedule = getLoanAmortizationSchedule(
				null, null, new BigDecimal(10000), 5,
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, new BigDecimal(5.5));

		AmortizationSchedule schedule = getAmortizationSchedule(null, null,
				new BigDecimal(10000), 60,
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, new BigDecimal(5.5));

		assertScheduleEquals(null, null, oldSchedule, schedule);
	}

	public void test2() throws Exception {
		Date startDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 3);
		Date firstPaymentDate = calendar.getTime();

		LoanAmortizationSchedule oldSchedule = getLoanAmortizationSchedule(
				startDate, firstPaymentDate, new BigDecimal(10000), 5,
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, new BigDecimal(5.5));

		AmortizationSchedule schedule = getAmortizationSchedule(startDate,
				firstPaymentDate, new BigDecimal(10000), 60,
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, new BigDecimal(5.5));

		assertScheduleEquals(startDate, firstPaymentDate, oldSchedule, schedule);
	}

	public void test3() throws Exception {
		
		Date startDate = DateUtils.parseDate("01/02/2009", datePattern);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, 3);
		Date firstPaymentDate = calendar.getTime();

		TruthInLendingNotice notice = getTruthInLendingNotice(startDate,
				firstPaymentDate, new BigDecimal(10000), 60,
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, new BigDecimal(5.5));

		Assert.assertEquals(new Integer(60), notice.getAmortizationMonths());
		Assert.assertEquals("TOWN OF SOUTH PADRE ISLAN", notice
				.getContractName().trim());
		System.out.println(notice.getFinanceCharge().setScale(2, BigDecimal.ROUND_HALF_DOWN));
		Assert.assertEquals(new BigDecimal("1564.99"), notice
				.getFinanceCharge().setScale(2, BigDecimal.ROUND_HALF_DOWN));
	}

}
