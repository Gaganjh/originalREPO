/**
 * 
 */
package com.manulife.pension.service.loan.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.JdbcHelper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for LoanPackageState
 *
 * @see com.manulife.pension.service.loan.domain.LoanPackageState
 * @author patelpo
 */
@PrepareForTest({ AtRiskAddressChangeVO.class, AtRiskForgetUserName.class, JdbcHelper.class, LoanDefaults.class,
		LoanValidationHelper.class })
@RunWith(PowerMockRunner.class)
public class LoanPackageStateTest {
	DataSource getCachedDataSourceResult = mock(DataSource.class);
	Connection getConnectionResult = mock(Connection.class);
	PreparedStatement prepareStatementResult = mock(PreparedStatement.class);
	Statement statementResult = mock(Statement.class);
	CallableStatement callableStatementResult = mock(CallableStatement.class);
	ResultSet executeQueryResult = mock(ResultSet.class);

	@Before
	public void setUp() throws Exception {
		spy(JdbcHelper.class);

		when(statementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(prepareStatementResult.executeQuery()).thenReturn(executeQueryResult);
		when(callableStatementResult.executeQuery(anyString())).thenReturn(executeQueryResult);
		when(getConnectionResult.prepareStatement(anyString())).thenReturn(prepareStatementResult);
		when(getConnectionResult.createStatement()).thenReturn(statementResult);
		when(getConnectionResult.prepareCall(anyString())).thenReturn(callableStatementResult);
		when(getCachedDataSourceResult.getConnection()).thenReturn(getConnectionResult);
		doReturn(getCachedDataSourceResult).when(JdbcHelper.class, "getCachedDataSource", anyString());

	}

	/**
	 * Parasoft Jtest UTA: Test for validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.LoanPackageState#validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testValidate() throws Throwable {
		

		spy(LoanValidationHelper.class);
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));

		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateEnum fromState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateEnum toState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateContext context = mockLoanStateContext();
		underTest.validate(fromState, toState, context);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult);

		int compareToResult = 0; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.getTime()).thenReturn(getTimeResult);
		return getCurrentOrNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mockDate();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult2);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult3);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult4);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		int compareToResult5 = 0; // UTA: default value
		when(getRequestDateResult.compareTo(any(Date.class))).thenReturn(compareToResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult5);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		String getDefaultProvisionResult = ""; // UTA: default value
		when(getLoanResult.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate2();
		when(getLoanResult.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate3();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee();
		when(getLoanResult.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate4();
		when(getLoanResult.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(getLoanResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(getLoanResult.getLoanReason()).thenReturn(getLoanReasonResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(getLoanResult.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(getLoanResult.getLoanType()).thenReturn(getLoanTypeResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(getLoanResult.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(getLoanResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		LoanRecipient getRecipientResult = mockLoanRecipient();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate5();
		when(getLoanResult.getRequestDate()).thenReturn(getRequestDateResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(getLoanResult.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData2() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		String getAddressLine1Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine1()).thenReturn(getAddressLine1Result3);

		String getAddressLine2Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine2()).thenReturn(getAddressLine2Result3);

		String getCityResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getCity()).thenReturn(getCityResult3);

		BigDecimal getCurrentOutstandingBalanceResult3 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult2.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult3);

		BigDecimal getMaxBalanceLast12MonthsResult3 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult2.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult3);

		Integer getOutstandingLoansCountResult3 = 0; // UTA: default value
		when(getLoanParticipantDataResult2.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult3);

		List<Integer> getPendingRequestsResult2 = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult2).when(getLoanParticipantDataResult2).getPendingRequests();

		String getStateCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getStateCode()).thenReturn(getStateCodeResult3);

		String getZipCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getZipCode()).thenReturn(getZipCodeResult3);

		boolean isForwardUnreversedLoanTransactionExistResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult2);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData2() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult2 = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult2).when(getLoanPlanDataResult2).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult2 = 0; // UTA: default value
		when(getLoanPlanDataResult2.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult2.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		String getPayrollFrequencyResult2 = ""; // UTA: default value
		when(getLoanPlanDataResult2.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult2);

		String getSpousalConsentReqdIndResult2 = ""; // UTA: default value
		when(getLoanPlanDataResult2.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult2);
		return getLoanPlanDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings2() throws Throwable {
		LoanSettings getLoanSettingsResult2 = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult2 = false; // UTA: default value
		when(getLoanSettingsResult2.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult2);

		boolean isLrk01Result2 = false; // UTA: default value
		when(getLoanSettingsResult2.isLrk01()).thenReturn(isLrk01Result2);
		return getLoanSettingsResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		BusinessCalendar getBusinessCalendarResult = mockBusinessCalendar();
		when(context.getBusinessCalendar()).thenReturn(getBusinessCalendarResult);

		Loan getLoanResult = mockLoan();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData2();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData2();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		LoanSettings getLoanSettingsResult2 = mockLoanSettings2();
		when(context.getLoanSettings()).thenReturn(getLoanSettingsResult2);

		boolean isPrintLoanDocumentResult = false; // UTA: default value
		when(context.isPrintLoanDocument()).thenReturn(isPrintLoanDocumentResult);

		boolean isSaveAndExitResult = false; // UTA: default value
		when(context.isSaveAndExit()).thenReturn(isSaveAndExitResult);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for approve(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#approve(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testApprove() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.approve(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for complete(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#complete(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testComplete() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.complete(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for decline(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#decline(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testDecline() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.decline(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#delete(Loan)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan2();
		Loan result = underTest.delete(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter2() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult = mockTimestamp();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp2();
		when(getAcceptedParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult3 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult3);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp5() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult6);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp4();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp5();
		when(getCurrentAdministratorNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult7);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp6() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp7() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter3() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult4 = mockTimestamp6();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp7();
		when(getCurrentLoanParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult10);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp8() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp9() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote2() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp8();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp9();
		when(getCurrentParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult13);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		int getDateResult = 0; // UTA: default value
		when(getEffectiveDateResult.getDate()).thenReturn(getDateResult);

		int getHoursResult = 0; // UTA: default value
		when(getEffectiveDateResult.getHours()).thenReturn(getHoursResult);

		int getMinutesResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMinutes()).thenReturn(getMinutesResult);

		int getMonthResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMonth()).thenReturn(getMonthResult);

		int getSecondsResult = 0; // UTA: default value
		when(getEffectiveDateResult.getSeconds()).thenReturn(getSecondsResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		int getYearResult = 0; // UTA: default value
		when(getEffectiveDateResult.getYear()).thenReturn(getYearResult);

		String toStringResult14 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult14);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		int getDateResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getDate()).thenReturn(getDateResult2);

		int getHoursResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getHours()).thenReturn(getHoursResult2);

		int getMinutesResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMinutes()).thenReturn(getMinutesResult2);

		int getMonthResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMonth()).thenReturn(getMonthResult2);

		int getSecondsResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getSeconds()).thenReturn(getSecondsResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);

		int getYearResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getYear()).thenReturn(getYearResult2);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp10() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp11() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee2() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp10();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp11();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		int getDateResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getDate()).thenReturn(getDateResult3);

		int getHoursResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getHours()).thenReturn(getHoursResult3);

		int getMinutesResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMinutes()).thenReturn(getMinutesResult3);

		int getMonthResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMonth()).thenReturn(getMonthResult3);

		int getSecondsResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getSeconds()).thenReturn(getSecondsResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult3);

		int getYearResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getYear()).thenReturn(getYearResult3);

		String toStringResult18 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult18);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp12() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData3() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		String getEmploymentStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		String toStringResult20 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult20);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData3() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		int getDateResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getDate()).thenReturn(getDateResult4);

		int getHoursResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getHours()).thenReturn(getHoursResult4);

		int getMinutesResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMinutes()).thenReturn(getMinutesResult4);

		int getMonthResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMonth()).thenReturn(getMonthResult4);

		int getSecondsResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getSeconds()).thenReturn(getSecondsResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult4);

		int getYearResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getYear()).thenReturn(getYearResult4);

		String toStringResult22 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult22);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp13() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp14() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter4() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp13();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		Timestamp getLastUpdatedResult7 = mockTimestamp14();
		when(getOriginalParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult7);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult25 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult25);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress2() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getAddressResult.getCreated()).thenReturn(getCreatedResult8);

		Timestamp getLastUpdatedResult8 = mock(Timestamp.class);
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult8);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);

		boolean isBlankResult4 = false; // UTA: default value
		when(getAddressResult.isBlank()).thenReturn(isBlankResult4);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient2() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress2();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult9 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult9);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

		Timestamp getLastUpdatedResult9 = mock(Timestamp.class);
		when(getRecipientResult.getLastUpdated()).thenReturn(getLastUpdatedResult9);

		String getOrganizationNameResult = ""; // UTA: default value
		when(getRecipientResult.getOrganizationName()).thenReturn(getOrganizationNameResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String getShareTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getShareTypeCode()).thenReturn(getShareTypeCodeResult);

		BigDecimal getShareValueResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getShareValue()).thenReturn(getShareValueResult);

		String getStateOfResidenceCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateOfResidenceCode()).thenReturn(getStateOfResidenceCodeResult);

		BigDecimal getStateTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getStateTaxPercent()).thenReturn(getStateTaxPercentResult);

		String getStateTaxTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateTaxTypeCode()).thenReturn(getStateTaxTypeCodeResult);

		String getTaxpayerIdentNoResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentNo()).thenReturn(getTaxpayerIdentNoResult);

		String getTaxpayerIdentTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentTypeCode()).thenReturn(getTaxpayerIdentTypeCodeResult);

		Boolean getUsCitizenIndResult = false; // UTA: default value
		when(getRecipientResult.getUsCitizenInd()).thenReturn(getUsCitizenIndResult);

		String toStringResult26 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult26);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter2();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp3();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter3();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote2();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate6();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate7();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate8();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp12();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData3();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData3();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		String getLoginRoleCodeResult = ""; // UTA: default value
		when(loan.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate9();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter4();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient2();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mock(Date.class);
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult4 = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult4);

		boolean isFeeChangedResult = false; // UTA: default value
		when(loan.isFeeChanged()).thenReturn(isFeeChangedResult);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isOKResult = false; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for expire(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#expire(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testExpire() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.expire(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEffectiveDateForApproval(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getEffectiveDateForApproval(LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testGetEffectiveDateForApproval() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext2();
		Date result = underTest.getEffectiveDateForApproval(context);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.getTime()).thenReturn(getTimeResult);
		return getCurrentOrNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar2() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mockDate10();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext2() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		BusinessCalendar getBusinessCalendarResult = mockBusinessCalendar2();
		when(context.getBusinessCalendar()).thenReturn(getBusinessCalendarResult);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for getEstimatedEffectiveDate(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getEstimatedEffectiveDate(LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testGetEstimatedEffectiveDate() throws Throwable {
		spy(LoanDefaults.class);

		int getEstimatedLoanStartDateOffsetResult = 0; // UTA: default value
		doReturn(getEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getEstimatedLoanStartDateOffset();

		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext3();
		Date result = underTest.getEstimatedEffectiveDate(context);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.getTime()).thenReturn(getTimeResult);
		return getCurrentOrNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getNextBusinessDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getNextBusinessDateResult.getTime()).thenReturn(getTimeResult2);
		return getNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar3() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mockDate11();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);

		Date getNextBusinessDateResult = mockDate12();
		when(getBusinessCalendarResult.getNextBusinessDate(any(Date.class), anyInt()))
				.thenReturn(getNextBusinessDateResult);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext3() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		BusinessCalendar getBusinessCalendarResult = mockBusinessCalendar3();
		when(context.getBusinessCalendar()).thenReturn(getBusinessCalendarResult);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for getEstimatedExpirationDate()
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getEstimatedExpirationDate()
	 * @author patelpo
	 */
	@Test
	public void testGetEstimatedExpirationDate() throws Throwable {
		spy(LoanDefaults.class);

		int getExpirationDateOffsetResult = 0; // UTA: default value
		doReturn(getExpirationDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getExpirationDateOffset();

		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Date result = underTest.getEstimatedExpirationDate();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getEstimatedMaturityDate(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getEstimatedMaturityDate(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetEstimatedMaturityDate() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan3();
		Date result = underTest.getEstimatedMaturityDate(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter5() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getCurrentLoanParameterResult.clone()).thenReturn(cloneResult2);

		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		Object cloneResult3 = new Object(); // UTA: default value
		when(getEffectiveDateResult.clone()).thenReturn(cloneResult3);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan3() throws Throwable {
		Loan loan = mock(Loan.class);
		Object cloneResult = new Object(); // UTA: default value
		when(loan.clone()).thenReturn(cloneResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter5();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		Date getEffectiveDateResult = mockDate13();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanStateContext(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getLoanStateContext(Loan)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanStateContext() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		LoanStateContext result = underTest.getLoanStateContext(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for initiate(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#initiate(Integer, Integer, Integer)
	 * @author patelpo
	 */
	@Test
	public void testInitiate() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Integer profileId = 0; // UTA: default value
		Integer contractId = 0; // UTA: default value
		Integer userProfileId = 0; // UTA: default value
		Loan result = underTest.initiate(profileId, contractId, userProfileId);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for loanPackage(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#loanPackage(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testLoanPackage() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.loanPackage(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for logToMrl(String, LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#logToMrl(String, LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testLogToMrl() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		String action = ""; // UTA: default value
		LoanStateContext context = mockLoanStateContext4();
		underTest.logToMrl(action, context);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp15() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData4() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getEmploymentStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String toStringResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult2);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData4() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		String toStringResult3 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult3);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan4() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		Timestamp getLastUpdatedResult = mockTimestamp15();
		when(getLoanResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData4();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData4();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		String getStatusResult = ""; // UTA: default value
		when(getLoanResult.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(getLoanResult.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult4 = ""; // UTA: default value
		when(getLoanResult.toString()).thenReturn(toStringResult4);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData5() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		String getEmploymentStatusCodeResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getLastName()).thenReturn(getLastNameResult2);

		String getMiddleInitialResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getMiddleInitial()).thenReturn(getMiddleInitialResult2);

		String getSsnResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getSsn()).thenReturn(getSsnResult2);

		String toStringResult5 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.toString()).thenReturn(toStringResult5);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData5() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		String getContractNameResult2 = ""; // UTA: default value
		when(getLoanPlanDataResult2.getContractName()).thenReturn(getContractNameResult2);

		String toStringResult6 = ""; // UTA: default value
		when(getLoanPlanDataResult2.toString()).thenReturn(toStringResult6);
		return getLoanPlanDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext4() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		Loan getLoanResult = mockLoan4();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData5();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData5();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		String toStringResult7 = ""; // UTA: default value
		when(context.toString()).thenReturn(toStringResult7);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for populate(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#populate(Loan)
	 * @author patelpo
	 */
	@Test
	public void testPopulate() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		underTest.populate(loan);

	}

	/**
	 * Parasoft Jtest UTA: Test for populateMessages(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#populateMessages(LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testPopulateMessages() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext5();
		underTest.populateMessages(context);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter6() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Object cloneResult3 = new Object(); // UTA: default value
		when(getCurrentLoanParameterResult.clone()).thenReturn(cloneResult3);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation2() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate14() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		Object cloneResult4 = new Object(); // UTA: default value
		when(getExpirationDateResult.clone()).thenReturn(cloneResult4);

		int compareToResult = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData6() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		Object cloneResult5 = new Object(); // UTA: default value
		when(getLoanParticipantDataResult.clone()).thenReturn(cloneResult5);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getParticipantId()).thenReturn(getParticipantIdResult);

		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);

		boolean isGiflFeatureSelectedResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isGiflFeatureSelected()).thenReturn(isGiflFeatureSelectedResult);

		boolean isPendingWithdrawalRequestExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isPendingWithdrawalRequestExist())
				.thenReturn(isPendingWithdrawalRequestExistResult);

		boolean isPositivePbaMoneyTypeBalanceResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isPositivePbaMoneyTypeBalance())
				.thenReturn(isPositivePbaMoneyTypeBalanceResult);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData6() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		Object cloneResult6 = new Object(); // UTA: default value
		when(getLoanPlanDataResult.clone()).thenReturn(cloneResult6);

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings3() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		Object cloneResult7 = new Object(); // UTA: default value
		when(getLoanSettingsResult.clone()).thenReturn(cloneResult7);

		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan5() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getLoanResult.clone()).thenReturn(cloneResult2);

		Integer getContractIdResult = 0; // UTA: default value
		when(getLoanResult.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getCurrentAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAccountBalance()).thenReturn(getCurrentAccountBalanceResult);

		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter6();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation2();
		when(getLoanResult.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate14();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData6();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData6();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings3();
		when(getLoanResult.getLoanSettings()).thenReturn(getLoanSettingsResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<LoanMessage> getMessagesResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getMessagesResult).when(getLoanResult).getMessages();

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getStatusResult = ""; // UTA: default value
		when(getLoanResult.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		boolean isAnyMoneyTypeNotAContractMoneyTypeResult = false; // UTA: default value
		when(getLoanResult.isAnyMoneyTypeNotAContractMoneyType()).thenReturn(isAnyMoneyTypeNotAContractMoneyTypeResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData7() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		Object cloneResult8 = new Object(); // UTA: default value
		when(getLoanParticipantDataResult2.clone()).thenReturn(cloneResult8);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(getLoanParticipantDataResult2.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantIdResult2 = 0; // UTA: default value
		when(getLoanParticipantDataResult2.getParticipantId()).thenReturn(getParticipantIdResult2);

		String getParticipantStatusCodeResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult2);

		List<Integer> getPendingRequestsResult2 = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult2).when(getLoanParticipantDataResult2).getPendingRequests();

		boolean isForwardUnreversedLoanTransactionExistResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult2);

		boolean isGiflFeatureSelectedResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isGiflFeatureSelected()).thenReturn(isGiflFeatureSelectedResult2);

		boolean isPendingWithdrawalRequestExistResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isPendingWithdrawalRequestExist())
				.thenReturn(isPendingWithdrawalRequestExistResult2);

		boolean isPositivePbaMoneyTypeBalanceResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isPositivePbaMoneyTypeBalance())
				.thenReturn(isPositivePbaMoneyTypeBalanceResult2);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData7() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		Object cloneResult9 = new Object(); // UTA: default value
		when(getLoanPlanDataResult2.clone()).thenReturn(cloneResult9);

		Integer getMaxNumberOfOutstandingLoansResult2 = 0; // UTA: default value
		when(getLoanPlanDataResult2.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult2);
		return getLoanPlanDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings4() throws Throwable {
		LoanSettings getLoanSettingsResult2 = mock(LoanSettings.class);
		Object cloneResult10 = new Object(); // UTA: default value
		when(getLoanSettingsResult2.clone()).thenReturn(cloneResult10);

		boolean isAllowOnlineLoansResult2 = false; // UTA: default value
		when(getLoanSettingsResult2.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult2);

		boolean isLrk01Result2 = false; // UTA: default value
		when(getLoanSettingsResult2.isLrk01()).thenReturn(isLrk01Result2);
		return getLoanSettingsResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext5() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		Object cloneResult = new Object(); // UTA: default value
		when(context.clone()).thenReturn(cloneResult);

		Loan getLoanResult = mockLoan5();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData7();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData7();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		LoanSettings getLoanSettingsResult2 = mockLoanSettings4();
		when(context.getLoanSettings()).thenReturn(getLoanSettingsResult2);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp16() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp17() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter7() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Timestamp getCreatedResult = mockTimestamp16();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		Timestamp getLastUpdatedResult = mockTimestamp17();
		when(getAcceptedParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult3 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult3);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO2() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp18() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp19() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp20() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult6);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote3() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp19();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp20();
		when(getCurrentAdministratorNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult7);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp21() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp22() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter8() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Timestamp getCreatedResult4 = mockTimestamp21();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		Timestamp getLastUpdatedResult3 = mockTimestamp22();
		when(getCurrentLoanParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult10);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp23() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp24() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote4() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp23();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp24();
		when(getCurrentParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult13);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate15() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		int compareToResult = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		int getDateResult = 0; // UTA: default value
		when(getEffectiveDateResult.getDate()).thenReturn(getDateResult);

		int getHoursResult = 0; // UTA: default value
		when(getEffectiveDateResult.getHours()).thenReturn(getHoursResult);

		int getMinutesResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMinutes()).thenReturn(getMinutesResult);

		int getMonthResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMonth()).thenReturn(getMonthResult);

		int getSecondsResult = 0; // UTA: default value
		when(getEffectiveDateResult.getSeconds()).thenReturn(getSecondsResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		int getYearResult = 0; // UTA: default value
		when(getEffectiveDateResult.getYear()).thenReturn(getYearResult);

		String toStringResult14 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult14);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation3() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate16() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		int getDateResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getDate()).thenReturn(getDateResult2);

		int getHoursResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getHours()).thenReturn(getHoursResult2);

		int getMinutesResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMinutes()).thenReturn(getMinutesResult2);

		int getMonthResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMonth()).thenReturn(getMonthResult2);

		int getSecondsResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getSeconds()).thenReturn(getSecondsResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);

		int getYearResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getYear()).thenReturn(getYearResult2);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp25() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp26() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee3() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp25();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp26();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate17() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		int getDateResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getDate()).thenReturn(getDateResult3);

		int getHoursResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getHours()).thenReturn(getHoursResult3);

		int getMinutesResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMinutes()).thenReturn(getMinutesResult3);

		int getMonthResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMonth()).thenReturn(getMonthResult3);

		int getSecondsResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getSeconds()).thenReturn(getSecondsResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult3);

		int getYearResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getYear()).thenReturn(getYearResult3);

		String toStringResult18 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult18);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp27() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData8() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		String getEmploymentStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);

		String toStringResult20 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult20);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData8() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings5() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);

		String toStringResult22 = ""; // UTA: default value
		when(getLoanSettingsResult.toString()).thenReturn(toStringResult22);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate18() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getMaturityDateResult.after(any(Date.class))).thenReturn(afterResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getMaturityDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		int getDateResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getDate()).thenReturn(getDateResult4);

		int getHoursResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getHours()).thenReturn(getHoursResult4);

		int getMinutesResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMinutes()).thenReturn(getMinutesResult4);

		int getMonthResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMonth()).thenReturn(getMonthResult4);

		int getSecondsResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getSeconds()).thenReturn(getSecondsResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult4);

		int getYearResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getYear()).thenReturn(getYearResult4);

		String toStringResult23 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult23);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp28() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult24);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp29() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult25 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult25);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter9() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		Timestamp getCreatedResult7 = mockTimestamp28();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		Timestamp getLastUpdatedResult7 = mockTimestamp29();
		when(getOriginalParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult7);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult26 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult26);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient3() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan6() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter7();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO2();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp18();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter8();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate15();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation3();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate16();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee3();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate17();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp27();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData8();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData8();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings5();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		String getLoginRoleCodeResult = ""; // UTA: default value
		when(loan.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate18();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter9();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient3();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mock(Date.class);
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isFeeChangedResult = false; // UTA: default value
		when(loan.isFeeChanged()).thenReturn(isFeeChangedResult);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isOKResult = false; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp30() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp31() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter10() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Timestamp getCreatedResult = mockTimestamp30();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		Timestamp getLastUpdatedResult = mockTimestamp31();
		when(getAcceptedParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult3 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult3);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO3() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp32() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp33() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp34() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult6);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote5() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp33();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp34();
		when(getCurrentAdministratorNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult7);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp35() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp36() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter11() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Timestamp getCreatedResult4 = mockTimestamp35();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		Timestamp getLastUpdatedResult3 = mockTimestamp36();
		when(getCurrentLoanParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult10);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp37() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp38() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote6() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp37();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp38();
		when(getCurrentParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult13);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate19() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		int compareToResult = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		int getDateResult = 0; // UTA: default value
		when(getEffectiveDateResult.getDate()).thenReturn(getDateResult);

		int getHoursResult = 0; // UTA: default value
		when(getEffectiveDateResult.getHours()).thenReturn(getHoursResult);

		int getMinutesResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMinutes()).thenReturn(getMinutesResult);

		int getMonthResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMonth()).thenReturn(getMonthResult);

		int getSecondsResult = 0; // UTA: default value
		when(getEffectiveDateResult.getSeconds()).thenReturn(getSecondsResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		int getYearResult = 0; // UTA: default value
		when(getEffectiveDateResult.getYear()).thenReturn(getYearResult);

		String toStringResult14 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult14);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation4() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate20() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		int getDateResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getDate()).thenReturn(getDateResult2);

		int getHoursResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getHours()).thenReturn(getHoursResult2);

		int getMinutesResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMinutes()).thenReturn(getMinutesResult2);

		int getMonthResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMonth()).thenReturn(getMonthResult2);

		int getSecondsResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getSeconds()).thenReturn(getSecondsResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);

		int getYearResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getYear()).thenReturn(getYearResult2);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp39() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp40() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee4() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp39();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp40();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate21() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		int getDateResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getDate()).thenReturn(getDateResult3);

		int getHoursResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getHours()).thenReturn(getHoursResult3);

		int getMinutesResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMinutes()).thenReturn(getMinutesResult3);

		int getMonthResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMonth()).thenReturn(getMonthResult3);

		int getSecondsResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getSeconds()).thenReturn(getSecondsResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult3);

		int getYearResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getYear()).thenReturn(getYearResult3);

		String toStringResult18 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult18);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp41() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData9() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		String getEmploymentStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);

		String toStringResult20 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult20);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData9() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings6() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);

		String toStringResult22 = ""; // UTA: default value
		when(getLoanSettingsResult.toString()).thenReturn(toStringResult22);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate22() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getMaturityDateResult.after(any(Date.class))).thenReturn(afterResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getMaturityDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		int getDateResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getDate()).thenReturn(getDateResult4);

		int getHoursResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getHours()).thenReturn(getHoursResult4);

		int getMinutesResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMinutes()).thenReturn(getMinutesResult4);

		int getMonthResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMonth()).thenReturn(getMonthResult4);

		int getSecondsResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getSeconds()).thenReturn(getSecondsResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult4);

		int getYearResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getYear()).thenReturn(getYearResult4);

		String toStringResult23 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult23);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp42() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult24);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp43() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult25 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult25);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter12() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		Timestamp getCreatedResult7 = mockTimestamp42();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		Timestamp getLastUpdatedResult7 = mockTimestamp43();
		when(getOriginalParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult7);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult26 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult26);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient4() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan7() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter10();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO3();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp32();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote5();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter11();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote6();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate19();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation4();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate20();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee4();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate21();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp41();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData9();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData9();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings6();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		String getLoginRoleCodeResult = ""; // UTA: default value
		when(loan.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate22();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter12();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient4();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mock(Date.class);
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult2 = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult2);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isFeeChangedResult = false; // UTA: default value
		when(loan.isFeeChanged()).thenReturn(isFeeChangedResult);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isOKResult = false; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for refreshLoanMoneyTypes(LoanPlanData, List)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#refreshLoanMoneyTypes(LoanPlanData, List)
	 * @author patelpo
	 */
	@Test
	public void testRefreshLoanMoneyTypes() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanPlanData loanPlanData = mockLoanPlanData10();
		List<LoanMoneyType> loanMoneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		List<LoanMoneyType> participantMoneyTypes = new ArrayList<LoanMoneyType>(); // UTA: default value
		underTest.refreshLoanMoneyTypes(loanPlanData, loanMoneyTypes, participantMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData10() throws Throwable {
		LoanPlanData loanPlanData = mock(LoanPlanData.class);
		boolean isNoMoneyTypeAllowedForLoanResult = false; // UTA: default value
		when(loanPlanData.isNoMoneyTypeAllowedForLoan()).thenReturn(isNoMoneyTypeAllowedForLoanResult);
		return loanPlanData;
	}

	/**
	 * Parasoft Jtest UTA: Test for reject(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#reject(Loan)
	 * @author patelpo
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testReject() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.reject(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for save(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#save(LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testSave() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext6();
		Loan result = underTest.save(context);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp44() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp45() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter13() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Timestamp getCreatedResult = mockTimestamp44();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		Timestamp getLastUpdatedResult = mockTimestamp45();
		when(getAcceptedParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult3 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult3);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskAddressChangeVO
	 */
	private static AtRiskAddressChangeVO mockAtRiskAddressChangeVO() throws Throwable {
		AtRiskAddressChangeVO getAddresschangeResult = mock(AtRiskAddressChangeVO.class);
		Address getApprovalAddressResult = mock(Address.class);
		when(getAddresschangeResult.getApprovalAddress()).thenReturn(getApprovalAddressResult);

		Integer getApprovalUpdatedProfileIdResult = 0; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedProfileId()).thenReturn(getApprovalUpdatedProfileIdResult);

		String getApprovalUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getAddresschangeResult.getApprovalUpdatedUserIdType()).thenReturn(getApprovalUpdatedUserIdTypeResult);

		String getCreatedUserFistNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserFistName()).thenReturn(getCreatedUserFistNameResult);

		String getCreatedUserLastNameResult = ""; // UTA: default value
		when(getAddresschangeResult.getCreatedUserLastName()).thenReturn(getCreatedUserLastNameResult);
		return getAddresschangeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskForgetUserName
	 */
	private static AtRiskForgetUserName mockAtRiskForgetUserName() throws Throwable {
		AtRiskForgetUserName getForgetUserNameResult = mock(AtRiskForgetUserName.class);
		String getForgotPasswordEmailAddressResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordEmailAddress()).thenReturn(getForgotPasswordEmailAddressResult);

		Date getForgotPasswordRequestedDateResult = mock(Date.class);
		when(getForgetUserNameResult.getForgotPasswordRequestedDate()).thenReturn(getForgotPasswordRequestedDateResult);

		Integer getForgotPasswordUpdatedProfileIdResult = 0; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedProfileId())
				.thenReturn(getForgotPasswordUpdatedProfileIdResult);

		String getForgotPasswordUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserFirstName())
				.thenReturn(getForgotPasswordUpdatedUserFirstNameResult);

		String getForgotPasswordUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserIdType())
				.thenReturn(getForgotPasswordUpdatedUserIdTypeResult);

		String getForgotPasswordUpdatedUserLastNameResult = ""; // UTA: default value
		when(getForgetUserNameResult.getForgotPasswordUpdatedUserLastName())
				.thenReturn(getForgotPasswordUpdatedUserLastNameResult);
		return getForgetUserNameResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskPasswordResetVO
	 */
	private static AtRiskPasswordResetVO mockAtRiskPasswordResetVO() throws Throwable {
		AtRiskPasswordResetVO getPasswordResetResult = mock(AtRiskPasswordResetVO.class);
		Integer getEmailAddressLastUpdatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedProfileId())
				.thenReturn(getEmailAddressLastUpdatedProfileIdResult);

		String getEmailAddressLastUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailAddressLastUpdatedUserIdType())
				.thenReturn(getEmailAddressLastUpdatedUserIdTypeResult);

		Date getEmailPasswordResetDateResult = mock(Date.class);
		when(getPasswordResetResult.getEmailPasswordResetDate()).thenReturn(getEmailPasswordResetDateResult);

		String getEmailPasswordResetEmailAddressResult = ""; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetEmailAddress())
				.thenReturn(getEmailPasswordResetEmailAddressResult);

		Integer getEmailPasswordResetInitiatedProfileIdResult = 0; // UTA: default value
		when(getPasswordResetResult.getEmailPasswordResetInitiatedProfileId())
				.thenReturn(getEmailPasswordResetInitiatedProfileIdResult);
		return getPasswordResetResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskWebRegistrationVO
	 */
	private static AtRiskWebRegistrationVO mockAtRiskWebRegistrationVO() throws Throwable {
		AtRiskWebRegistrationVO getWebRegistrationResult = mock(AtRiskWebRegistrationVO.class);
		Address getAddressResult = mock(Address.class);
		when(getWebRegistrationResult.getAddress()).thenReturn(getAddressResult);

		Integer getConfirmUpdatedProfileIdResult = 0; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedProfileId()).thenReturn(getConfirmUpdatedProfileIdResult);

		String getConfirmUpdatedUserFirstNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserFirstName())
				.thenReturn(getConfirmUpdatedUserFirstNameResult);

		String getConfirmUpdatedUserIdTypeResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserIdType()).thenReturn(getConfirmUpdatedUserIdTypeResult);

		String getConfirmUpdatedUserLastNameResult = ""; // UTA: default value
		when(getWebRegistrationResult.getConfirmUpdatedUserLastName()).thenReturn(getConfirmUpdatedUserLastNameResult);

		Date getWebRegConfirmationMailedDateResult = mock(Date.class);
		when(getWebRegistrationResult.getWebRegConfirmationMailedDate())
				.thenReturn(getWebRegConfirmationMailedDateResult);

		Date getWebRegistrationDateResult = mock(Date.class);
		when(getWebRegistrationResult.getWebRegistrationDate()).thenReturn(getWebRegistrationDateResult);
		return getWebRegistrationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO4() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		AtRiskAddressChangeVO getAddresschangeResult = mockAtRiskAddressChangeVO();
		when(getAtRiskDetailsVOResult.getAddresschange()).thenReturn(getAddresschangeResult);

		AtRiskForgetUserName getForgetUserNameResult = mockAtRiskForgetUserName();
		when(getAtRiskDetailsVOResult.getForgetUserName()).thenReturn(getForgetUserNameResult);

		AtRiskPasswordResetVO getPasswordResetResult = mockAtRiskPasswordResetVO();
		when(getAtRiskDetailsVOResult.getPasswordReset()).thenReturn(getPasswordResetResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		AtRiskWebRegistrationVO getWebRegistrationResult = mockAtRiskWebRegistrationVO();
		when(getAtRiskDetailsVOResult.getWebRegistration()).thenReturn(getWebRegistrationResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp46() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp47() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp48() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult6);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote7() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp47();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp48();
		when(getCurrentAdministratorNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult7);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp49() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp50() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter14() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Timestamp getCreatedResult4 = mockTimestamp49();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		Timestamp getLastUpdatedResult3 = mockTimestamp50();
		when(getCurrentLoanParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult10);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp51() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp52() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote8() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp51();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp52();
		when(getCurrentParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult13);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate23() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult14 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult14);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation5() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate24() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp53() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp54() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee5() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp53();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp54();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate25() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult18 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult18);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp55() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData10() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		String toStringResult20 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult20);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData11() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate26() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult22 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult22);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp56() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp57() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter15() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		Timestamp getCreatedResult7 = mockTimestamp56();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		Timestamp getLastUpdatedResult7 = mockTimestamp57();
		when(getOriginalParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult7);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult25 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult25);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress3() throws Throwable {
		DistributionAddress getAddressResult2 = mock(DistributionAddress.class);
		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult2.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult2.getCity()).thenReturn(getCityResult2);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult2.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult2.getSubmissionId()).thenReturn(getSubmissionIdResult3);
		return getAddressResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient5() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult2 = mockDistributionAddress3();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult2);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult8);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

		Timestamp getLastUpdatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getLastUpdated()).thenReturn(getLastUpdatedResult8);

		String getOrganizationNameResult = ""; // UTA: default value
		when(getRecipientResult.getOrganizationName()).thenReturn(getOrganizationNameResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String getShareTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getShareTypeCode()).thenReturn(getShareTypeCodeResult);

		BigDecimal getShareValueResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getShareValue()).thenReturn(getShareValueResult);

		String getStateOfResidenceCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateOfResidenceCode()).thenReturn(getStateOfResidenceCodeResult);

		BigDecimal getStateTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getStateTaxPercent()).thenReturn(getStateTaxPercentResult);

		String getStateTaxTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateTaxTypeCode()).thenReturn(getStateTaxTypeCodeResult);

		String getTaxpayerIdentNoResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentNo()).thenReturn(getTaxpayerIdentNoResult);

		String getTaxpayerIdentTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentTypeCode()).thenReturn(getTaxpayerIdentTypeCodeResult);

		Boolean getUsCitizenIndResult = false; // UTA: default value
		when(getRecipientResult.getUsCitizenInd()).thenReturn(getUsCitizenIndResult);

		String toStringResult26 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult26);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan8() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter13();
		when(getLoanResult.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(getLoanResult.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO4();
		when(getLoanResult.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(getLoanResult.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getLoanResult.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp46();
		when(getLoanResult.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(getLoanResult.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(getLoanResult.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote7();
		when(getLoanResult.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter14();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote8();
		when(getLoanResult.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getLoanResult).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(getLoanResult.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate23();
		when(getLoanResult.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation5();
		when(getLoanResult.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate24();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee5();
		when(getLoanResult.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate25();
		when(getLoanResult.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(getLoanResult.getLastFeeChangedWasPlanSponsorUserInd())
				.thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp55();
		when(getLoanResult.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(getLoanResult.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(getLoanResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData10();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData11();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(getLoanResult.getLoanReason()).thenReturn(getLoanReasonResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(getLoanResult.getLoanType()).thenReturn(getLoanTypeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(getLoanResult).getManagedContents();

		Date getMaturityDateResult = mockDate26();
		when(getLoanResult.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(getLoanResult.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getLoanResult).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(getLoanResult).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter15();
		when(getLoanResult.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getLoanResult.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(getLoanResult.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient5();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mock(Date.class);
		when(getLoanResult.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		when(getLoanResult.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(getLoanResult.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult4 = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult4);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(getLoanResult.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isOKResult = false; // UTA: default value
		when(getLoanResult.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(getLoanResult.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(getLoanResult.toString()).thenReturn(toStringResult27);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext6() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		Loan getLoanResult = mockLoan8();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		String toStringResult28 = ""; // UTA: default value
		when(context.toString()).thenReturn(toStringResult28);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation6() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp58() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		int getNanosResult = 0; // UTA: default value
		when(getLastUpdatedResult.getNanos()).thenReturn(getNanosResult);

		long getTimeResult = 0L; // UTA: default value
		when(getLastUpdatedResult.getTime()).thenReturn(getTimeResult);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee6() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getLastUpdatedResult = mockTimestamp58();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp59() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		int getNanosResult2 = 0; // UTA: default value
		when(getLastUpdatedResult2.getNanos()).thenReturn(getNanosResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getLastUpdatedResult2.getTime()).thenReturn(getTimeResult2);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData11() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData12() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress4() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp60() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		int getNanosResult3 = 0; // UTA: default value
		when(getLastUpdatedResult4.getNanos()).thenReturn(getNanosResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getLastUpdatedResult4.getTime()).thenReturn(getTimeResult3);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient6() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress4();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getLastUpdatedResult4 = mockTimestamp60();
		when(getRecipientResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan9() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(getLoanResult.getContractId()).thenReturn(getContractIdResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation6();
		when(getLoanResult.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		Fee getFeeResult = mockFee6();
		when(getLoanResult.getFee()).thenReturn(getFeeResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp59();
		when(getLoanResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(getLoanResult.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData11();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData12();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(getLoanResult).getMoneyTypesWithAccountBalance();

		LoanRecipient getRecipientResult = mockLoanRecipient6();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData12() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		String getAddressLine1Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine1()).thenReturn(getAddressLine1Result3);

		String getAddressLine2Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine2()).thenReturn(getAddressLine2Result3);

		String getCityResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getCity()).thenReturn(getCityResult3);

		String getCountryResult2 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getCountry()).thenReturn(getCountryResult2);

		String getStateCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getStateCode()).thenReturn(getStateCodeResult3);

		String getZipCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getZipCode()).thenReturn(getZipCodeResult3);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData13() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult2.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult2);
		return getLoanPlanDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext7() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		Loan getLoanResult = mockLoan9();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData12();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData13();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp61() throws Throwable {
		Timestamp updatedTs = mock(Timestamp.class);
		int getNanosResult4 = 0; // UTA: default value
		when(updatedTs.getNanos()).thenReturn(getNanosResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(updatedTs.getTime()).thenReturn(getTimeResult4);
		return updatedTs;
	}

	/**
	 * Parasoft Jtest UTA: Test for saveActivitySummary(Loan, Timestamp)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#saveActivitySummary(Loan, Timestamp)
	 * @author patelpo
	 */
	@Test
	public void testSaveActivitySummary() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan10();
		Timestamp updatedTs = mockTimestamp63();
		underTest.saveActivitySummary(loan, updatedTs);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp62() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		int getNanosResult = 0; // UTA: default value
		when(getCreatedResult.getNanos()).thenReturn(getNanosResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCreatedResult.getTime()).thenReturn(getTimeResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan10() throws Throwable {
		Loan loan = mock(Loan.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		Timestamp getCreatedResult = mockTimestamp62();
		when(loan.getCreated()).thenReturn(getCreatedResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp63() throws Throwable {
		Timestamp updatedTs = mock(Timestamp.class);
		int getNanosResult2 = 0; // UTA: default value
		when(updatedTs.getNanos()).thenReturn(getNanosResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(updatedTs.getTime()).thenReturn(getTimeResult2);
		return updatedTs;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp64() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter16() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Timestamp getCreatedResult = mockTimestamp64();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getAcceptedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult);

		boolean isReadyToSaveResult = false; // UTA: default value
		when(getAcceptedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult);

		String toStringResult2 = ""; // UTA: default value
		when(getAcceptedParameterResult.toString()).thenReturn(toStringResult2);
		return getAcceptedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of AtRiskDetailsVO
	 */
	private static AtRiskDetailsVO mockAtRiskDetailsVO5() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp65() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult3);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp66() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult4);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote9() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp66();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult5 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult5);
		return getCurrentAdministratorNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp67() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult6);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter17() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Timestamp getCreatedResult4 = mockTimestamp67();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getInterestRateResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult2);

		BigDecimal getLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult2);

		BigDecimal getMaximumAvailableResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult2);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getPaymentFrequencyResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult7);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp68() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult8);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote10() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp68();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult9);
		return getCurrentParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate27() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		int compareToResult = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		int getDateResult = 0; // UTA: default value
		when(getEffectiveDateResult.getDate()).thenReturn(getDateResult);

		int getHoursResult = 0; // UTA: default value
		when(getEffectiveDateResult.getHours()).thenReturn(getHoursResult);

		int getMinutesResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMinutes()).thenReturn(getMinutesResult);

		int getMonthResult = 0; // UTA: default value
		when(getEffectiveDateResult.getMonth()).thenReturn(getMonthResult);

		int getSecondsResult = 0; // UTA: default value
		when(getEffectiveDateResult.getSeconds()).thenReturn(getSecondsResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		int getYearResult = 0; // UTA: default value
		when(getEffectiveDateResult.getYear()).thenReturn(getYearResult);

		String toStringResult10 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult10);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of EmployeeVestingInformation
	 */
	private static EmployeeVestingInformation mockEmployeeVestingInformation7() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate28() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		int getDateResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getDate()).thenReturn(getDateResult2);

		int getHoursResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getHours()).thenReturn(getHoursResult2);

		int getMinutesResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMinutes()).thenReturn(getMinutesResult2);

		int getMonthResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getMonth()).thenReturn(getMonthResult2);

		int getSecondsResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getSeconds()).thenReturn(getSecondsResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);

		int getYearResult2 = 0; // UTA: default value
		when(getExpirationDateResult.getYear()).thenReturn(getYearResult2);

		String toStringResult11 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult11);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp69() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult12);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp70() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult13 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult13);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee7() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp69();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult = mockTimestamp70();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);
		return getFeeResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate29() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		int getDateResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getDate()).thenReturn(getDateResult3);

		int getHoursResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getHours()).thenReturn(getHoursResult3);

		int getMinutesResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMinutes()).thenReturn(getMinutesResult3);

		int getMonthResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getMonth()).thenReturn(getMonthResult3);

		int getSecondsResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getSeconds()).thenReturn(getSecondsResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult3);

		int getYearResult3 = 0; // UTA: default value
		when(getFirstPayrollDateResult.getYear()).thenReturn(getYearResult3);

		String toStringResult14 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult14);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp71() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult15 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult15);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData13() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		String getCountryResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCountry()).thenReturn(getCountryResult);

		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		String getEmploymentStatusCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getEmploymentStatusCode()).thenReturn(getEmploymentStatusCodeResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getLastName()).thenReturn(getLastNameResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		String getMiddleInitialResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getMiddleInitial()).thenReturn(getMiddleInitialResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		String getSsnResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getSsn()).thenReturn(getSsnResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);

		String toStringResult16 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult16);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData14() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);

		String toStringResult17 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult17);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings7() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);

		String toStringResult18 = ""; // UTA: default value
		when(getLoanSettingsResult.toString()).thenReturn(toStringResult18);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate30() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getMaturityDateResult.after(any(Date.class))).thenReturn(afterResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getMaturityDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		int getDateResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getDate()).thenReturn(getDateResult4);

		int getHoursResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getHours()).thenReturn(getHoursResult4);

		int getMinutesResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMinutes()).thenReturn(getMinutesResult4);

		int getMonthResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getMonth()).thenReturn(getMonthResult4);

		int getSecondsResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getSeconds()).thenReturn(getSecondsResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getMaturityDateResult.getTime()).thenReturn(getTimeResult4);

		int getYearResult4 = 0; // UTA: default value
		when(getMaturityDateResult.getYear()).thenReturn(getYearResult4);

		String toStringResult19 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult19);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp72() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult20 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult20);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter18() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

		Timestamp getCreatedResult7 = mockTimestamp72();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getInterestRateResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getInterestRate()).thenReturn(getInterestRateResult3);

		BigDecimal getLoanAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult3);

		BigDecimal getMaximumAvailableResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult3);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getPaymentFrequencyResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult21 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult21);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient7() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult8);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String getShareTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getShareTypeCode()).thenReturn(getShareTypeCodeResult);

		BigDecimal getShareValueResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getShareValue()).thenReturn(getShareValueResult);

		String getStateTaxTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getStateTaxTypeCode()).thenReturn(getStateTaxTypeCodeResult);

		String getTaxpayerIdentNoResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentNo()).thenReturn(getTaxpayerIdentNoResult);

		String getTaxpayerIdentTypeCodeResult = ""; // UTA: default value
		when(getRecipientResult.getTaxpayerIdentTypeCode()).thenReturn(getTaxpayerIdentTypeCodeResult);

		Boolean getUsCitizenIndResult = false; // UTA: default value
		when(getRecipientResult.getUsCitizenInd()).thenReturn(getUsCitizenIndResult);

		String toStringResult22 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult22);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan11() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter16();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO5();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp65();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote9();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter17();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote10();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate27();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation7();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate28();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee7();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate29();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp71();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData13();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData14();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(loan.getLoanReason()).thenReturn(getLoanReasonResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings7();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(loan.getLoanType()).thenReturn(getLoanTypeResult);

		String getLoginRoleCodeResult = ""; // UTA: default value
		when(loan.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		List<ManagedContent> getManagedContentsResult = new ArrayList<ManagedContent>(); // UTA: default value
		doReturn(getManagedContentsResult).when(loan).getManagedContents();

		Date getMaturityDateResult = mockDate30();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter18();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient7();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mock(Date.class);
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		when(loan.getReviewedParameter()).thenReturn(getReviewedParameterResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isFeeChangedResult = false; // UTA: default value
		when(loan.isFeeChanged()).thenReturn(isFeeChangedResult);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(loan.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isOKResult = false; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult23 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult23);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for setAdditionalPlanAndContractData(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#setAdditionalPlanAndContractData(Loan)
	 * @author patelpo
	 */
	@Test
	public void testSetAdditionalPlanAndContractData() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan12();
		underTest.setAdditionalPlanAndContractData(loan);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData15() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan12() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanPlanData getLoanPlanDataResult = mockLoanPlanData15();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for setCommonLoanValues(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#setCommonLoanValues(LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testSetCommonLoanValues() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext8();
		underTest.setCommonLoanValues(context);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan13() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		String getLoginRoleCodeResult = ""; // UTA: default value
		when(getLoanResult.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(getLoanResult.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		boolean isFeeChangedResult = false; // UTA: default value
		when(getLoanResult.isFeeChanged()).thenReturn(isFeeChangedResult);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext8() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		Loan getLoanResult = mockLoan13();
		when(context.getLoan()).thenReturn(getLoanResult);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for setLoanPackageDates(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#setLoanPackageDates(Loan)
	 * @author patelpo
	 */
	@Test
	public void testSetLoanPackageDates() throws Throwable {
		spy(LoanDefaults.class);

		int getLoanPackageEstimatedLoanStartDateOffsetResult = 0; // UTA: default value
		doReturn(getLoanPackageEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getLoanPackageEstimatedLoanStartDateOffset();

		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan14();
		underTest.setLoanPackageDates(loan);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan14() throws Throwable {
		Loan loan = mock(Loan.class);
		Object cloneResult = new Object(); // UTA: default value
		when(loan.clone()).thenReturn(cloneResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for validateLoanCalculationSection(Loan, LoanPlanData, LoanStateEnum)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#validateLoanCalculationSection(Loan, LoanPlanData, LoanStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testValidateLoanCalculationSection() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan15();
		LoanPlanData loanPlanData = mockLoanPlanData16();
		LoanStateEnum toState = LoanStateEnum.DRAFT; // UTA: default value
		underTest.validateLoanCalculationSection(loan, loanPlanData, toState);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter19() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan15() throws Throwable {
		Loan loan = mock(Loan.class);
		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter19();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(loan).getErrors();

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData16() throws Throwable {
		LoanPlanData loanPlanData = mock(LoanPlanData.class);
		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(loanPlanData.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(loanPlanData.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);
		return loanPlanData;
	}

	/**
	 * Parasoft Jtest UTA: Test for validateOnSave(LoanStateContext, LoanStateEnum, LoanStateEnum)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#validateOnSave(LoanStateContext, LoanStateEnum, LoanStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testValidateOnSave() throws Throwable {
		spy(LoanValidationHelper.class);

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));

		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		LoanStateContext context = mockLoanStateContext9();
		LoanStateEnum fromState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateEnum toState = LoanStateEnum.DRAFT; // UTA: default value
		underTest.validateOnSave(context, fromState, toState);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate31() throws Throwable {
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult);

		int compareToResult = 0; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.getTime()).thenReturn(getTimeResult);
		return getCurrentOrNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar4() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mockDate31();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter20() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getInterestRateResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getInterestRate()).thenReturn(getInterestRateResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);

		BigDecimal getMaximumAvailableResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getMaximumAvailable()).thenReturn(getMaximumAvailableResult);

		String getPaymentFrequencyResult = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentFrequency()).thenReturn(getPaymentFrequencyResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate32() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult2);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate33() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult3);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate34() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult4);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData14() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(getLoanParticipantDataResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		List<Integer> getPendingRequestsResult = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult).when(getLoanParticipantDataResult).getPendingRequests();

		String getStateCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getZipCode()).thenReturn(getZipCodeResult);

		boolean isForwardUnreversedLoanTransactionExistResult = false; // UTA: default value
		when(getLoanParticipantDataResult.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData17() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings8() throws Throwable {
		LoanSettings getLoanSettingsResult = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult = false; // UTA: default value
		when(getLoanSettingsResult.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult);

		boolean isLrk01Result = false; // UTA: default value
		when(getLoanSettingsResult.isLrk01()).thenReturn(isLrk01Result);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress5() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result2);

		String getAddressLine2Result2 = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result2);

		String getCityResult2 = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult2);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult2);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient8() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress5();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate35() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		int compareToResult5 = 0; // UTA: default value
		when(getRequestDateResult.compareTo(any(Date.class))).thenReturn(compareToResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult5);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan16() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter20();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		Date getEffectiveDateResult = mockDate32();
		when(getLoanResult.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate33();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		Date getFirstPayrollDateResult = mockDate34();
		when(getLoanResult.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData14();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData17();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings8();
		when(getLoanResult.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(getLoanResult.getLoanType()).thenReturn(getLoanTypeResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(getLoanResult.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(getLoanResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		LoanRecipient getRecipientResult = mockLoanRecipient8();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate35();
		when(getLoanResult.getRequestDate()).thenReturn(getRequestDateResult);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData15() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult2 = mock(LoanParticipantData.class);
		String getAddressLine1Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine1()).thenReturn(getAddressLine1Result3);

		String getAddressLine2Result3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getAddressLine2()).thenReturn(getAddressLine2Result3);

		String getCityResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getCity()).thenReturn(getCityResult3);

		BigDecimal getCurrentOutstandingBalanceResult3 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult2.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult3);

		BigDecimal getMaxBalanceLast12MonthsResult3 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult2.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult3);

		Integer getOutstandingLoansCountResult3 = 0; // UTA: default value
		when(getLoanParticipantDataResult2.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult3);

		List<Integer> getPendingRequestsResult2 = new ArrayList<Integer>(); // UTA: default value
		doReturn(getPendingRequestsResult2).when(getLoanParticipantDataResult2).getPendingRequests();

		String getStateCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getStateCode()).thenReturn(getStateCodeResult3);

		String getZipCodeResult3 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.getZipCode()).thenReturn(getZipCodeResult3);

		boolean isForwardUnreversedLoanTransactionExistResult2 = false; // UTA: default value
		when(getLoanParticipantDataResult2.isForwardUnreversedLoanTransactionExist())
				.thenReturn(isForwardUnreversedLoanTransactionExistResult2);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData18() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult2 = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult2).when(getLoanPlanDataResult2).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult2 = 0; // UTA: default value
		when(getLoanPlanDataResult2.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult2.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		String getPayrollFrequencyResult2 = ""; // UTA: default value
		when(getLoanPlanDataResult2.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult2);
		return getLoanPlanDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanSettings
	 */
	private static LoanSettings mockLoanSettings9() throws Throwable {
		LoanSettings getLoanSettingsResult2 = mock(LoanSettings.class);
		boolean isAllowOnlineLoansResult2 = false; // UTA: default value
		when(getLoanSettingsResult2.isAllowOnlineLoans()).thenReturn(isAllowOnlineLoansResult2);

		boolean isLrk01Result2 = false; // UTA: default value
		when(getLoanSettingsResult2.isLrk01()).thenReturn(isLrk01Result2);
		return getLoanSettingsResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext9() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		BusinessCalendar getBusinessCalendarResult = mockBusinessCalendar4();
		when(context.getBusinessCalendar()).thenReturn(getBusinessCalendarResult);

		Loan getLoanResult = mockLoan16();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData15();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData18();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		LoanSettings getLoanSettingsResult2 = mockLoanSettings9();
		when(context.getLoanSettings()).thenReturn(getLoanSettingsResult2);

		boolean isPrintLoanDocumentResult = false; // UTA: default value
		when(context.isPrintLoanDocument()).thenReturn(isPrintLoanDocumentResult);

		boolean isSaveAndExitResult = false; // UTA: default value
		when(context.isSaveAndExit()).thenReturn(isSaveAndExitResult);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for validatePaymentSectionInvalidCharacter(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#validatePaymentSectionInvalidCharacter(Loan)
	 * @author patelpo
	 */
	@Test
	public void testValidatePaymentSectionInvalidCharacter() throws Throwable {
		// Given
		LoanPackageState underTest = new LoanPackageState();

		// When
		Loan loan = mockLoan17();
		underTest.validatePaymentSectionInvalidCharacter(loan);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress6() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getAddressResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getAddressResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getAddressResult.getCity()).thenReturn(getCityResult);

		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		String getStateCodeResult = ""; // UTA: default value
		when(getAddressResult.getStateCode()).thenReturn(getStateCodeResult);

		String getZipCodeResult = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient9() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress6();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String toStringResult = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan17() throws Throwable {
		Loan loan = mock(Loan.class);
		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(loan).getErrors();

		LoanRecipient getRecipientResult = mockLoanRecipient9();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult2 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult2);
		return loan;
	}

}