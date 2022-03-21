/**
 * 
 */
package com.manulife.pension.service.loan.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.exception.LoanValidationException;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
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
import com.manulife.pension.service.vesting.VestingEngine;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;

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

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for PendingAcceptanceState
 *
 * @see com.manulife.pension.service.loan.domain.PendingAcceptanceState
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, LoanValidationHelper.class, EmployeeServiceDelegate.class,
		LoanSupportDao.class, ContractServiceDelegate.class, LoanDataHelper.class, LoanObjectFactory.class,
		LoanDefaults.class, EventFactory.class })
@RunWith(PowerMockRunner.class)
public class PendingAcceptanceStateTest {
	Logger getLoggerResult = mock(Logger.class); // UTA: default value
	DataSource getCachedDataSourceResult = mock(DataSource.class);
	Connection getConnectionResult = mock(Connection.class);
	PreparedStatement prepareStatementResult = mock(PreparedStatement.class);
	Statement statementResult = mock(Statement.class);
	CallableStatement callableStatementResult = mock(CallableStatement.class);
	ResultSet executeQueryResult = mock(ResultSet.class);

	@Before
	public void setUp() throws Exception {
		spy(JdbcHelper.class);

		spy(Logger.class);

		doReturn(getLoggerResult).when(Logger.class, "getLogger", Mockito.any(Class.class));
		when(getLoggerResult.isDebugEnabled()).thenReturn(true);

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
	 * Parasoft Jtest UTA: Test for populate(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingAcceptanceState#populate(Loan)
	 * @author patelpo
	 */
	@Test
	public void testPopulate() throws Throwable {
		spy(LoanObjectFactory.class);
		//spy(DateUtils.class);

		LoanDataHelper loanDataHelper = mock(LoanDataHelper.class);
		LoanObjectFactory getInstanceResult = mock(LoanObjectFactory.class); // UTA: default value
		doReturn(getInstanceResult).when(LoanObjectFactory.class, "getInstance");
		when(getInstanceResult.getLoanDataHelper()).thenReturn(loanDataHelper);
		Pair<List<LoanMoneyType>, EmployeeVestingInformation> pair = new Pair<List<LoanMoneyType>, EmployeeVestingInformation>(
				null, null);
		when(loanDataHelper.getParticipantMoneyTypesForLoans(any(Integer.class), any(Integer.class))).thenReturn(pair);

		BusinessCalendar businessCalendar = mockBusinessCalendar2();
		when(loanDataHelper.getBusinessCalendar()).thenReturn(businessCalendar);

		//when(businessCalendar.getCurrentOrNextBusinessDate(new Date())).thenReturn(new Date());
		//when(businessCalendar.getNextBusinessDate(new Date(), 1)).thenReturn(new Date());
		//doReturn(new Date()).when(DateUtils.class, "truncate");

		EmployeeVestingInformation vestingInformation = mock(EmployeeVestingInformation.class);
		VestingEngine newVestingEngineResult = mock(VestingEngine.class); // UTA: default value
		whenNew(VestingEngine.class).withAnyArguments().thenReturn(newVestingEngineResult);
		when(newVestingEngineResult.getEmployeeVestingInformation(any(Integer.class), any(Long.class), any(Date.class),
				any(Integer.class))).thenReturn(vestingInformation);

		List<LoanMoneyType> loanMoneyTypeLoans = new ArrayList<LoanMoneyType>();
		LoanMoneyType loanMoneyType = new LoanMoneyType();
		loanMoneyType.setMoneyTypeId("Test");
		loanMoneyTypeLoans.add(loanMoneyType);
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		when(newSelectBeanListQueryHandlerResult.select(any(Object[].class))).thenReturn(loanMoneyTypeLoans);
		spy(LoanDefaults.class);

		int getEstimatedLoanStartDateOffsetResult = 0; // UTA: default value
		doReturn(getEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getEstimatedLoanStartDateOffset();

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		Loan loan = mockLoan();
		underTest.populate(loan);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan() throws Throwable {
		Loan loan = mock(Loan.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		Date getEffectiveDateResult = mockDate();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for reject(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingAcceptanceState#reject(Loan)
	 * @author patelpo
	 */
	@Test
	public void testReject() throws Throwable {
		spy(EventFactory.class);

		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		Loan loan = mockLoan2();
		Loan result = underTest.reject(loan);

		// Then
		 assertNotNull(result);
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
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter2() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult = mockTimestamp();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

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
	private static AtRiskDetailsVO mockAtRiskDetailsVO() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult3);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult4);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp3();
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
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult6);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter3() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult4 = mockTimestamp4();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

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
	private static Timestamp mockTimestamp5() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult8);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote2() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp5();
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
	private static Date mockDate2() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		String toStringResult10 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult10);
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
	private static Date mockDate3() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		String toStringResult11 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult11);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp6() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult12);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp7() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult13 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult13);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp6();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult = mockTimestamp7();
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
	private static Date mockDate4() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		String toStringResult14 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult14);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp8() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult15 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult15);
		return getLastUpdatedResult2;
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

		String toStringResult16 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult16);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String toStringResult17 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult17);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		String toStringResult18 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult18);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp9() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult19);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter4() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp9();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult20 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult20);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp10() throws Throwable {
		Timestamp getCreatedResult8 = mock(Timestamp.class);
		String toStringResult21 = ""; // UTA: default value
		when(getCreatedResult8.toString()).thenReturn(toStringResult21);
		return getCreatedResult8;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp11() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult22 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult22);
		return getLastUpdatedResult3;
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

		Timestamp getCreatedResult8 = mockTimestamp10();
		when(getAddressResult.getCreated()).thenReturn(getCreatedResult8);

		Timestamp getLastUpdatedResult3 = mockTimestamp11();
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

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
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp12() throws Throwable {
		Timestamp getCreatedResult9 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult9.toString()).thenReturn(toStringResult23);
		return getCreatedResult9;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult9 = mockTimestamp12();
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult9);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

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

		String toStringResult24 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult24);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		String toStringResult25 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult25);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp13() throws Throwable {
		Timestamp getCreatedResult10 = mock(Timestamp.class);
		String toStringResult26 = ""; // UTA: default value
		when(getCreatedResult10.toString()).thenReturn(toStringResult26);
		return getCreatedResult10;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter5() throws Throwable {
		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult10 = mockTimestamp13();
		when(getReviewedParameterResult.getCreated()).thenReturn(getCreatedResult10);

		BigDecimal getPaymentAmountResult4 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult4);

		String getStatusCodeResult4 = ""; // UTA: default value
		when(getReviewedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult4);

		boolean isReadyToSaveResult4 = false; // UTA: default value
		when(getReviewedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult4);

		String toStringResult27 = ""; // UTA: default value
		when(getReviewedParameterResult.toString()).thenReturn(toStringResult27);
		return getReviewedParameterResult;
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

		Timestamp getCreatedResult2 = mockTimestamp2();
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

		Date getEffectiveDateResult = mockDate2();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate3();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate4();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp8();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData();
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

		Date getMaturityDateResult = mockDate5();
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

		LoanRecipient getRecipientResult = mockLoanRecipient();
		when(loan.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate6();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter5();
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

		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult28 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult28);
		return loan;
	}
	

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingAcceptanceState#sendForApproval(Loan)
	 * @author patelpo
	 */
	@Test
	public void testSendForApproval() throws Throwable {
		spy(EventFactory.class);

		EventFactory getInstanceResult1 = mock(EventFactory.class);
		doReturn(getInstanceResult1).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult1.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);

		spy(EmployeeServiceDelegate.class);

		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		spy(LoanValidationHelper.class);

		boolean isParticipantAllowedToSendForApprovalResult = true; // UTA: default value
		doReturn(isParticipantAllowedToSendForApprovalResult).when(LoanValidationHelper.class);
		LoanValidationHelper.isParticipantAllowedToSendForApproval(nullable(Loan.class));

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		Loan loan = mockLoan3();
		Loan result = underTest.sendForApproval(loan);

		// Then
		assertNotNull(result);
	}
	@Test
	public void testSendForApproval_1() throws Throwable {
		spy(EventFactory.class);
		
		EventFactory getInstanceResult1 = mock(EventFactory.class);
		doReturn(getInstanceResult1).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult1.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		
		spy(EmployeeServiceDelegate.class);
		
		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());
		
		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		spy(LoanValidationHelper.class);
		
		boolean isParticipantAllowedToSendForApprovalResult = true; // UTA: default value
		doReturn(isParticipantAllowedToSendForApprovalResult).when(LoanValidationHelper.class);
		LoanValidationHelper.isParticipantAllowedToSendForApproval(nullable(Loan.class));
		
		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();
		
		// When
		Loan loan = mockLoan3_1();
		Loan result = underTest.sendForApproval(loan);
		
		// Then
		assertNotNull(result);
	}

	@Test(expected = LoanValidationException.class)
	public void testSendForApproval_Exception() throws Throwable {
		spy(EmployeeServiceDelegate.class);

		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);
		spy(LoanValidationHelper.class);

		boolean isParticipantAllowedToSendForApprovalResult = false; // UTA: default value
		doReturn(isParticipantAllowedToSendForApprovalResult).when(LoanValidationHelper.class);
		LoanValidationHelper.isParticipantAllowedToSendForApproval(nullable(Loan.class));

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		Loan loan = mockLoan3();
		Loan result = underTest.sendForApproval(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp14() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter6() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult = mockTimestamp14();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		BigDecimal getPaymentAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getAcceptedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult);

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
	private static AtRiskDetailsVO mockAtRiskDetailsVO2() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp15() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult3);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp16() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult4);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote3() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp16();
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
	private static Timestamp mockTimestamp17() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult6);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter7() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult4 = mockTimestamp17();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

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
	private static Timestamp mockTimestamp18() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult8);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote4() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp18();
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
	private static Date mockDate7() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		String toStringResult10 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult10);
		return getEffectiveDateResult;
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

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);
		return getEmployeeVestingInformationResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		String toStringResult11 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult11);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp19() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult12);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp20() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult13 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult13);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee2() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Timestamp getCreatedResult6 = mockTimestamp19();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult = mockTimestamp20();
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
	private static Date mockDate9() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		String toStringResult14 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult14);
		return getFirstPayrollDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp21() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult15 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult15);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData2() throws Throwable {
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

		String toStringResult16 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult16);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData2() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String toStringResult17 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult17);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		String toStringResult18 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult18);
		return getMaturityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp22() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult19);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter8() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp22();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		BigDecimal getPaymentAmountResult3 = BigDecimal.ZERO; // UTA: default value
		when(getOriginalParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult3);

		String getStatusCodeResult3 = ""; // UTA: default value
		when(getOriginalParameterResult.getStatusCode()).thenReturn(getStatusCodeResult3);

		boolean isReadyToSaveResult3 = false; // UTA: default value
		when(getOriginalParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult3);

		String toStringResult20 = ""; // UTA: default value
		when(getOriginalParameterResult.toString()).thenReturn(toStringResult20);
		return getOriginalParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp23() throws Throwable {
		Timestamp getCreatedResult8 = mock(Timestamp.class);
		String toStringResult21 = ""; // UTA: default value
		when(getCreatedResult8.toString()).thenReturn(toStringResult21);
		return getCreatedResult8;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp24() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult22 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult22);
		return getLastUpdatedResult3;
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

		Timestamp getCreatedResult8 = mockTimestamp23();
		when(getAddressResult.getCreated()).thenReturn(getCreatedResult8);

		Timestamp getLastUpdatedResult3 = mockTimestamp24();
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

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
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp25() throws Throwable {
		Timestamp getCreatedResult9 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult9.toString()).thenReturn(toStringResult23);
		return getCreatedResult9;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient2() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress2();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult9 = mockTimestamp25();
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult9);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

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

		String toStringResult24 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult24);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		String toStringResult25 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult25);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp26() throws Throwable {
		Timestamp getCreatedResult10 = mock(Timestamp.class);
		String toStringResult26 = ""; // UTA: default value
		when(getCreatedResult10.toString()).thenReturn(toStringResult26);
		return getCreatedResult10;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter9() throws Throwable {
		LoanParameter getReviewedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult10 = mockTimestamp26();
		when(getReviewedParameterResult.getCreated()).thenReturn(getCreatedResult10);

		BigDecimal getPaymentAmountResult4 = BigDecimal.ZERO; // UTA: default value
		when(getReviewedParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult4);

		String getStatusCodeResult4 = ""; // UTA: default value
		when(getReviewedParameterResult.getStatusCode()).thenReturn(getStatusCodeResult4);

		boolean isReadyToSaveResult4 = false; // UTA: default value
		when(getReviewedParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult4);

		String toStringResult27 = ""; // UTA: default value
		when(getReviewedParameterResult.toString()).thenReturn(toStringResult27);
		return getReviewedParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan3() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter6();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);
		when(loan.isOK()).thenReturn(true);

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

		Timestamp getCreatedResult2 = mockTimestamp15();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = "PA"; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter7();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate7();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation2();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate8();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate9();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult2 = mockTimestamp21();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData2();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData2();
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

		Date getMaturityDateResult = mockDate10();
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

		LoanParameter getOriginalParameterResult = mockLoanParameter8();
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

		Date getRequestDateResult = mockDate11();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);

		LoanParameter getReviewedParameterResult = mockLoanParameter9();
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

		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult28 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult28);
		return loan;
	}
	private static Loan mockLoan3_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter6();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);
		when(loan.isOK()).thenReturn(true);
		
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
		
		Timestamp getCreatedResult2 = mockTimestamp15();
		when(loan.getCreated()).thenReturn(getCreatedResult2);
		
		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);
		
		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);
		
		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);
		
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter7();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);
		
		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);
		
		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();
		
		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);
		
		Date getEffectiveDateResult = mockDate7();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation2();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);
		
		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();
		
		Date getExpirationDateResult = mockDate8();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);
		
		Date getFirstPayrollDateResult = mockDate9();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);
		
		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);
		
		Timestamp getLastUpdatedResult2 = mockTimestamp21();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult2);
		
		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);
		
		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);
		
		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData2();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);
		
		LoanPlanData getLoanPlanDataResult = mockLoanPlanData2();
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
		
		Date getMaturityDateResult = mockDate10();
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
		
		LoanParameter getOriginalParameterResult = mockLoanParameter8();
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
		
		Date getRequestDateResult = mockDate11();
		when(loan.getRequestDate()).thenReturn(getRequestDateResult);
		
		LoanParameter getReviewedParameterResult = mockLoanParameter9();
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
		
		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);
		
		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		
		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);
		
		String toStringResult28 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult28);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingAcceptanceState#validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testValidate() throws Throwable {
		spy(ContractServiceDelegate.class);

		LifeIncomeAmountDetailsVO liaDetails = new LifeIncomeAmountDetailsVO();
		ContractServiceDelegate getInstanceResult = mock(ContractServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(ContractServiceDelegate.class, "getInstance");
		when(getInstanceResult.getLIADetailsByParticipantId(any(String.class))).thenReturn(liaDetails);
		spy(LoanValidationHelper.class);
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		LoanStateEnum fromState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateEnum toState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateContext context = mockLoanStateContext();
		underTest.validate(fromState, toState, context);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.before(any(Date.class))).thenReturn(beforeResult);

		int compareToResult = 0; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		long getTimeResult = 0L; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult = ""; // UTA: default value
		when(getCurrentOrNextBusinessDateResult.toString()).thenReturn(toStringResult);
		return getCurrentOrNextBusinessDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mockDate12();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);

		String toStringResult2 = ""; // UTA: default value
		when(getBusinessCalendarResult.toString()).thenReturn(toStringResult2);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter10() throws Throwable {
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

		String toStringResult3 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult3);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult4 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult4);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate14() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		int compareToResult3 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult5 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult5);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Fee
	 */
	private static Fee mockFee3() throws Throwable {
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
	private static Date mockDate15() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getFirstPayrollDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		int compareToResult4 = 0; // UTA: default value
		when(getFirstPayrollDateResult.compareTo(any(Date.class))).thenReturn(compareToResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getFirstPayrollDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult6 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult6);
		return getFirstPayrollDateResult;
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

		String toStringResult7 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult7);
		return getLoanParticipantDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData3() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getPayrollFrequencyResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult);

		String toStringResult8 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult8);
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

		String toStringResult9 = ""; // UTA: default value
		when(getLoanSettingsResult.toString()).thenReturn(toStringResult9);
		return getLoanSettingsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of DistributionAddress
	 */
	private static DistributionAddress mockDistributionAddress3() throws Throwable {
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
	private static LoanRecipient mockLoanRecipient3() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress3();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

		String toStringResult10 = ""; // UTA: default value
		when(getRecipientResult.toString()).thenReturn(toStringResult10);
		return getRecipientResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate16() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		int compareToResult5 = 0; // UTA: default value
		when(getRequestDateResult.compareTo(any(Date.class))).thenReturn(compareToResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult11 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult11);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan4() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter10();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		String getDefaultProvisionResult = ""; // UTA: default value
		when(getLoanResult.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate13();
		when(getLoanResult.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate14();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee3();
		when(getLoanResult.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate15();
		when(getLoanResult.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData3();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData3();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

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

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getLoanResult.getParticipantId()).thenReturn(getParticipantIdResult);

		LoanRecipient getRecipientResult = mockLoanRecipient3();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate16();
		when(getLoanResult.getRequestDate()).thenReturn(getRequestDateResult);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getLoanResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		boolean isLoginUserPlanSponsorOrTpaResult = false; // UTA: default value
		when(getLoanResult.isLoginUserPlanSponsorOrTpa()).thenReturn(isLoginUserPlanSponsorOrTpaResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(getLoanResult.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		String toStringResult12 = ""; // UTA: default value
		when(getLoanResult.toString()).thenReturn(toStringResult12);
		return getLoanResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData4() throws Throwable {
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

		String toStringResult13 = ""; // UTA: default value
		when(getLoanParticipantDataResult2.toString()).thenReturn(toStringResult13);
		return getLoanParticipantDataResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanPlanData
	 */
	private static LoanPlanData mockLoanPlanData4() throws Throwable {
		LoanPlanData getLoanPlanDataResult2 = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult2 = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult2).when(getLoanPlanDataResult2).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult2 = 0; // UTA: default value
		when(getLoanPlanDataResult2.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult2.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		String getPayrollFrequencyResult2 = ""; // UTA: default value
		when(getLoanPlanDataResult2.getPayrollFrequency()).thenReturn(getPayrollFrequencyResult2);

		String toStringResult14 = ""; // UTA: default value
		when(getLoanPlanDataResult2.toString()).thenReturn(toStringResult14);
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

		String toStringResult15 = ""; // UTA: default value
		when(getLoanSettingsResult2.toString()).thenReturn(toStringResult15);
		return getLoanSettingsResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanStateContext
	 */
	private static LoanStateContext mockLoanStateContext() throws Throwable {
		LoanStateContext context = mock(LoanStateContext.class);
		BusinessCalendar getBusinessCalendarResult = mockBusinessCalendar();
		when(context.getBusinessCalendar()).thenReturn(getBusinessCalendarResult);

		Loan getLoanResult = mockLoan4();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData4();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData4();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		LoanSettings getLoanSettingsResult2 = mockLoanSettings2();
		when(context.getLoanSettings()).thenReturn(getLoanSettingsResult2);

		boolean isPrintLoanDocumentResult = false; // UTA: default value
		when(context.isPrintLoanDocument()).thenReturn(isPrintLoanDocumentResult);

		boolean isSaveAndExitResult = false; // UTA: default value
		when(context.isSaveAndExit()).thenReturn(isSaveAndExitResult);

		String toStringResult16 = ""; // UTA: default value
		when(context.toString()).thenReturn(toStringResult16);
		return context;
	}

	/**
	 * Parasoft Jtest UTA: Test for getEstimatedEffectiveDate(LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.DefaultLoanState#getEstimatedEffectiveDate(LoanStateContext)
	 * @author reddyab
	 */
	@Test
	public void testGetEstimatedEffectiveDate() throws Throwable {
		spy(LoanDefaults.class);

		int getEstimatedLoanStartDateOffsetResult = 0; // UTA: default value
		doReturn(getEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getEstimatedLoanStartDateOffset();

		// Given
		PendingAcceptanceState underTest = new PendingAcceptanceState();

		// When
		LoanStateContext context = mockLoanStateContext2();
		Date result = underTest.getEstimatedEffectiveDate(context);

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of BusinessCalendar
	 */
	private static BusinessCalendar mockBusinessCalendar2() throws Throwable {
		BusinessCalendar getBusinessCalendarResult = mock(BusinessCalendar.class);
		Date getCurrentOrNextBusinessDateResult = mock(Date.class);
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(nullable(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);

		Date getNextBusinessDateResult = mock(Date.class);
		when(getBusinessCalendarResult.getNextBusinessDate(nullable(Date.class), anyInt()))
				.thenReturn(getNextBusinessDateResult);
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
}