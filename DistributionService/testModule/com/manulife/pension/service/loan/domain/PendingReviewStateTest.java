/**
 * 
 */
package com.manulife.pension.service.loan.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectBeanArrayQueryHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.dao.LoanSupportDao;
import com.manulife.pension.service.loan.event.EventFactory;
import com.manulife.pension.service.loan.event.LoanEventGenerator;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.util.BusinessCalendar;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.schedule.AvailabilitySchedule;
import com.manulife.pension.util.schedule.AvailabilityScheduleDAO;
import com.manulife.pension.util.schedule.AvailabilityStatus;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

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
 * Parasoft Jtest UTA: Test class for PendingReviewState
 *
 * @see com.manulife.pension.service.loan.domain.PendingReviewState
 * @author patelpo
 */
@PrepareForTest({ JdbcHelper.class, Logger.class, AvailabilityScheduleDAO.class, AvailabilitySchedule.class,
		LoanObjectFactory.class, LoanDataHelper.class, DefaultLoanState.class, LoanSupportDao.class,
		LoanValidationHelper.class, LoanStateFactory.class, EventFactory.class, LoanDefaults.class })
@RunWith(PowerMockRunner.class)
public class PendingReviewStateTest {
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
	 * Parasoft Jtest UTA: Test for approve(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#approve(Loan)
	 * @author patelpo
	 */
	@Test
	public void testApprove() throws Throwable {
		spy(LoanStateFactory.class);

		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));

		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), any(Loan.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), any(Loan.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateAllowLoans((List) any(), any(LoanSettings.class));
		spy(AvailabilitySchedule.class);

		AvailabilityStatus status = mock(AvailabilityStatus.class);
		when(status.isAvailable()).thenReturn(true);
		AvailabilitySchedule newInstanceResult = mock(AvailabilitySchedule.class); // UTA: default value
		doReturn(newInstanceResult).when(AvailabilitySchedule.class, "newInstance", any(DataSource.class), anyString());
		when(newInstanceResult.getStatusAsOf(any(Date.class), any(Boolean.class))).thenReturn(status);

		SelectBeanArrayQueryHandler newSelectBeanArrayQueryHandlerResult = mock(SelectBeanArrayQueryHandler.class); // UTA: default value
		whenNew(SelectBeanArrayQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanArrayQueryHandlerResult);

		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan();
		Loan result = underTest.approve(loan);

		// Then
		// assertNotNull(result);
	}

	@Test
	public void testApprove_1() throws Throwable {
		spy(LoanStateFactory.class);

		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));

		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), any(Loan.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), any(Loan.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateAllowLoans((List) any(), any(LoanSettings.class));
		spy(AvailabilitySchedule.class);

		AvailabilityStatus status = mock(AvailabilityStatus.class);
		when(status.isAvailable()).thenReturn(true);
		AvailabilitySchedule newInstanceResult = mock(AvailabilitySchedule.class); // UTA: default value
		doReturn(newInstanceResult).when(AvailabilitySchedule.class, "newInstance", any(DataSource.class), anyString());
		when(newInstanceResult.getStatusAsOf(any(Date.class), any(Boolean.class))).thenReturn(status);

		SelectBeanArrayQueryHandler newSelectBeanArrayQueryHandlerResult = mock(SelectBeanArrayQueryHandler.class); // UTA: default value
		whenNew(SelectBeanArrayQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanArrayQueryHandlerResult);

		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan_1();
		Loan result = underTest.approve(loan);

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
	private static LoanParameter mockLoanParameter() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

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
	private static LoanParameter mockLoanParameter2() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

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
	private static Date mockDate() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult);

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
	private static Date mockDate2() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult2);

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
	private static Fee mockFee() throws Throwable {
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
	private static Date mockDate3() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.before(any(Date.class))).thenReturn(beforeResult3);

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
	private static LoanPlanData mockLoanPlanData() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		BigDecimal getContractLoanExpenseMarginPctResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanExpenseMarginPct())
				.thenReturn(getContractLoanExpenseMarginPctResult2);

		BigDecimal getContractLoanMonthlyFlatFeeResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult2);

		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getMaturityDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getMaturityDateResult.before(any(Date.class))).thenReturn(beforeResult4);

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
	private static LoanParameter mockLoanParameter3() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult3 = 0; // UTA: default value
		when(getOriginalParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult3);

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
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult8);

		BigDecimal getFederalTaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(getRecipientResult.getFederalTaxPercent()).thenReturn(getFederalTaxPercentResult);

		String getLastNameResult2 = ""; // UTA: default value
		when(getRecipientResult.getLastName()).thenReturn(getLastNameResult2);

		String getOrganizationNameResult = ""; // UTA: default value
		when(getRecipientResult.getOrganizationName()).thenReturn(getOrganizationNameResult);

		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		LoanPayee payee = mock(LoanPayee.class);
		PaymentInstruction paymentInstruction = mock(PaymentInstruction.class);
		when(payee.getPaymentInstruction()).thenReturn(paymentInstruction);
		getPayeesResult.add(payee);
		doReturn(getPayeesResult).when(getRecipientResult).getPayees();

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
	private static Loan mockLoan() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter();
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

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter2();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote2();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate2();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate3();
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

		Date getMaturityDateResult = mockDate4();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult2);

		BigDecimal getMaximumLoanPercentageResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter3();
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

		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	private static Loan mockLoan_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter();
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

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter2();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote2();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate2();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate3();
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

		Date getMaturityDateResult = mockDate4();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult2);

		BigDecimal getMaximumLoanPercentageResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter3();
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
	 * Parasoft Jtest UTA: Test for decline(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#decline(Loan)
	 * @author patelpo
	 */
	@Test
	public void testDecline() throws Throwable {
		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan2();
		Loan result = underTest.decline(loan);

		// Then
		// assertNotNull(result);
	}

	@Test
	public void testDecline_1() throws Throwable {
		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan2_1();
		Loan result = underTest.decline(loan);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp15() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp16() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter4() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult = mockTimestamp15();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp16();
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
	private static AtRiskDetailsVO mockAtRiskDetailsVO2() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp17() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp18() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp19() throws Throwable {
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
		Timestamp getCreatedResult3 = mockTimestamp18();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp19();
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
	private static Timestamp mockTimestamp20() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp21() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter5() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult4 = mockTimestamp20();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp21();
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
	private static Timestamp mockTimestamp22() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp23() throws Throwable {
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
		Timestamp getCreatedResult5 = mockTimestamp22();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp23();
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
	private static Date mockDate5() throws Throwable {
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
	private static Date mockDate6() throws Throwable {
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
	private static Timestamp mockTimestamp24() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp25() throws Throwable {
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
		Timestamp getCreatedResult6 = mockTimestamp24();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp25();
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
	private static Date mockDate7() throws Throwable {
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
	private static Timestamp mockTimestamp26() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
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
	private static LoanPlanData mockLoanPlanData2() throws Throwable {
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
	private static Date mockDate8() throws Throwable {
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
	private static Timestamp mockTimestamp27() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp28() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter6() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp27();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		Timestamp getLastUpdatedResult7 = mockTimestamp28();
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
		DistributionAddress getAddressResult = mockDistributionAddress();
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

		Payee payee = new LoanPayee();
		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		getPayeesResult.add(payee);
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
		LoanParameter getAcceptedParameterResult = mockLoanParameter4();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

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

		Timestamp getCreatedResult2 = mockTimestamp17();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter5();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate5();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation2();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate6();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate7();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp26();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

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

		Date getMaturityDateResult = mockDate8();
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

		LoanParameter getOriginalParameterResult = mockLoanParameter6();
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

		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);

		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	private static Loan mockLoan2_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter4();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

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

		Timestamp getCreatedResult2 = mockTimestamp17();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote3();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter5();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote4();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate5();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation2();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate6();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee2();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate7();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp26();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

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

		Date getMaturityDateResult = mockDate8();
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

		LoanParameter getOriginalParameterResult = mockLoanParameter6();
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
	 * Parasoft Jtest UTA: Test for loanPackage(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#loanPackage(Loan)
	 * @author patelpo
	 */
	@Test
		public void testLoanPackage() throws Throwable {
			spy(LoanStateFactory.class);
	
			LoanState getStateResult = mock(LoanState.class);
			doReturn(getStateResult).when(LoanStateFactory.class);
			LoanStateFactory.getState(nullable(LoanStateEnum.class));
			
			spy(Calendar.class);
	
			Calendar getInstanceResult = mock(Calendar.class);
			doReturn(getInstanceResult).when(Calendar.class);
			Calendar.getInstance();
			
			spy(EventFactory.class);
			EventFactory getInstanceResult1 = mock(EventFactory.class);
			doReturn(getInstanceResult1).when(EventFactory.class);
			EventFactory.getInstance();
			LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
			when(getInstanceResult1.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
			
			
			
			// Given
			PendingReviewState underTest = new PendingReviewState();
	
			// When
			Loan loan = mockLoan3();
			spy(LoanDefaults.class);

			int getLoanPackageEstimatedLoanStartDateOffsetResult = 0; // UTA: default value
			doReturn(getLoanPackageEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
			LoanDefaults.getLoanPackageEstimatedLoanStartDateOffset();

			Loan result = underTest.loanPackage(loan);
	
			// Then
			// assertNotNull(result);
		}
	
	@Test
	public void testLoanPackage_1() throws Throwable {
		spy(LoanStateFactory.class);
		
		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));
		
		spy(Calendar.class);
		
		Calendar getInstanceResult = mock(Calendar.class);
		doReturn(getInstanceResult).when(Calendar.class);
		Calendar.getInstance();
		
		// Given
		PendingReviewState underTest = new PendingReviewState();
		
		// When
		Loan loan = mockLoan3_1();
		Loan result = underTest.loanPackage(loan);
		
		// Then
		// assertNotNull(result);
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp29() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp30() throws Throwable {
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
		Timestamp getCreatedResult = mockTimestamp29();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp30();
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
	private static AtRiskDetailsVO mockAtRiskDetailsVO3() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp31() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp32() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp33() throws Throwable {
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
		Timestamp getCreatedResult3 = mockTimestamp32();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp33();
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
	private static Timestamp mockTimestamp34() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp35() throws Throwable {
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
		Timestamp getCreatedResult4 = mockTimestamp34();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp35();
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
	private static Timestamp mockTimestamp36() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp37() throws Throwable {
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
		Timestamp getCreatedResult5 = mockTimestamp36();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp37();
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
	private static Date mockDate9() throws Throwable {
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
	private static Date mockDate10() throws Throwable {
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
	private static Timestamp mockTimestamp38() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp39() throws Throwable {
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
		Timestamp getCreatedResult6 = mockTimestamp38();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp39();
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
	private static Date mockDate11() throws Throwable {
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
	private static Timestamp mockTimestamp40() throws Throwable {
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
		BigDecimal getContractLoanExpenseMarginPctResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanExpenseMarginPct())
				.thenReturn(getContractLoanExpenseMarginPctResult2);

		BigDecimal getContractLoanMonthlyFlatFeeResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult2);

		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult21);
		return getLoanPlanDataResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
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
	private static Timestamp mockTimestamp41() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp42() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter9() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp41();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		Timestamp getLastUpdatedResult7 = mockTimestamp42();
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
		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		Timestamp getLastUpdatedResult8 = mock(Timestamp.class);
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult8);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient3() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress2();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult8);

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

		Payee payee = mock(LoanPayee.class);
		PaymentInstruction paymentInstruction = mock(PaymentInstruction.class);
		when(payee.getPaymentInstruction()).thenReturn(paymentInstruction);
		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		getPayeesResult.add(payee);
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
	private static Loan mockLoan3() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter7();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

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

		Timestamp getCreatedResult2 = mockTimestamp31();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote5();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter8();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote6();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate9();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation3();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate10();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee3();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate11();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp40();
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

		Date getMaturityDateResult = mockDate12();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		BigDecimal getMaximumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult2);

		BigDecimal getMaximumLoanPercentageResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult2);

		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();

		LoanParameter getOriginalParameterResult = mockLoanParameter9();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

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

		String getSpousalConsentReqdIndResult2 = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult2);

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

		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}
	private static Loan mockLoan3_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter7();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);
		
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
		
		Timestamp getCreatedResult2 = mockTimestamp31();
		when(loan.getCreated()).thenReturn(getCreatedResult2);
		
		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);
		
		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);
		
		LoanNote getCurrentAdministratorNoteResult = mockLoanNote5();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);
		
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter8();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);
		
		LoanNote getCurrentParticipantNoteResult = mockLoanNote6();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);
		
		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();
		
		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);
		
		Date getEffectiveDateResult = mockDate9();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation3();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);
		
		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();
		
		Date getExpirationDateResult = mockDate10();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		Fee getFeeResult = mockFee3();
		when(loan.getFee()).thenReturn(getFeeResult);
		
		Date getFirstPayrollDateResult = mockDate11();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);
		
		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);
		
		Timestamp getLastUpdatedResult6 = mockTimestamp40();
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
		
		Date getMaturityDateResult = mockDate12();
		when(loan.getMaturityDate()).thenReturn(getMaturityDateResult);
		
		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);
		
		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(loan.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);
		
		BigDecimal getMaximumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult2);
		
		BigDecimal getMaximumLoanPercentageResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult2);
		
		BigDecimal getMinimumLoanAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(loan.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult2);
		
		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();
		
		List<LoanMoneyType> getMoneyTypesWithAccountBalanceResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesWithAccountBalanceResult).when(loan).getMoneyTypesWithAccountBalance();
		
		LoanParameter getOriginalParameterResult = mockLoanParameter9();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);
		
		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);
		
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
		
		String getSpousalConsentReqdIndResult2 = ""; // UTA: default value
		when(loan.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult2);
		
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
	 * Parasoft Jtest UTA: Test for populate(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#populate(Loan)
	 * @author patelpo
	 */
	@Test
	public void testPopulate() throws Throwable {
		SelectBeanListQueryHandler newSelectBeanListQueryHandlerResult = mock(SelectBeanListQueryHandler.class); // UTA: default value
		whenNew(SelectBeanListQueryHandler.class).withAnyArguments().thenReturn(newSelectBeanListQueryHandlerResult);
		spy(LoanObjectFactory.class);

		LoanObjectFactory getInstanceResult = mock(LoanObjectFactory.class);
		doReturn(getInstanceResult).when(LoanObjectFactory.class);
		LoanObjectFactory.getInstance();
		LoanDataHelper loanDataHelper = mock(LoanDataHelper.class);
		when(getInstanceResult.getLoanDataHelper()).thenReturn(loanDataHelper);

		List<LoanMoneyType> list = new ArrayList<LoanMoneyType>();
		LoanMoneyType loanMoneyType = mock(LoanMoneyType.class);
		when(loanMoneyType.getMoneyTypeId()).thenReturn("");
		list.add(loanMoneyType);

		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		Pair<List<LoanMoneyType>, EmployeeVestingInformation> participantMoneyTypes = new Pair(list,
				employeeVestingInformation);
		when(loanDataHelper.getParticipantMoneyTypesForLoans(anyInt(), anyInt())).thenReturn(participantMoneyTypes);

		LoanStateContext newLoanStateContextResult = mock(LoanStateContext.class); // UTA: default value
		whenNew(LoanStateContext.class).withAnyArguments().thenReturn(newLoanStateContextResult);
		BusinessCalendar businessCalender = mock(BusinessCalendar.class);
		Date date = mock(Date.class);
		Date date1 = mock(Date.class);
		when(businessCalender.getNextBusinessDate(any(Date.class), anyInt())).thenReturn(date);
		when(businessCalender.getCurrentOrNextBusinessDate(any(Date.class))).thenReturn(date1);
		when(newLoanStateContextResult.getBusinessCalendar()).thenReturn(businessCalender);
		Loan loan1 = mock(Loan.class);
		List<LoanMessage> message = new ArrayList<LoanMessage>();
		when(loan1.getMessages()).thenReturn(message);
		when(loan1.getCurrentAccountBalance()).thenReturn(BigDecimal.ONE);
		when(newLoanStateContextResult.getLoan()).thenReturn(loan1);
		
		
		LoanParticipantData loanParticipantData = mock(LoanParticipantData.class);
		when(loanParticipantData.getParticipantStatusCode()).thenReturn("AC");
		when(newLoanStateContextResult.getLoanParticipantData()).thenReturn(loanParticipantData);
		

		LoanPlanData loanPlanData = mock(LoanPlanData.class);
		List<LoanTypeVO> loanTypeVO = new ArrayList<LoanTypeVO>();
		LoanTypeVO typeVO = mock(LoanTypeVO.class);
		when(typeVO.getMaxAmortizationYear()).thenReturn(10);
		loanTypeVO.add(typeVO);
		when(loanPlanData.getLoanTypeList()).thenReturn(loanTypeVO);
		when(newLoanStateContextResult.getLoanPlanData()).thenReturn(loanPlanData);
		spy(LoanDefaults.class);

		int getEstimatedLoanStartDateOffsetResult = 1; // UTA: default value
		doReturn(getEstimatedLoanStartDateOffsetResult).when(LoanDefaults.class);
		LoanDefaults.getEstimatedLoanStartDateOffset();
		
		spy(LoanValidationHelper.class);

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateAllowLoans((List) any(), nullable(LoanSettings.class));
		
		
		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan4();
		underTest.populate(loan);

	}
	


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter10() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		BigDecimal getLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getLoanAmount()).thenReturn(getLoanAmountResult);
		return getCurrentLoanParameterResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getEffectiveDateResult.before(any(Date.class))).thenReturn(beforeResult);

		int compareToResult = 0; // UTA: default value
		when(getEffectiveDateResult.compareTo(any(Date.class))).thenReturn(compareToResult);

		long getTimeResult = 0L; // UTA: default value
		when(getEffectiveDateResult.getTime()).thenReturn(getTimeResult);
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
	private static Date mockDate14() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		int compareToResult2 = 0; // UTA: default value
		when(getExpirationDateResult.compareTo(any(Date.class))).thenReturn(compareToResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult2);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData4() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		BigDecimal getCurrentOutstandingBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getCurrentOutstandingBalance())
				.thenReturn(getCurrentOutstandingBalanceResult2);

		BigDecimal getMaxBalanceLast12MonthsResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanParticipantDataResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult);

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
	private static LoanPlanData mockLoanPlanData4() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		List<LoanTypeVO> getLoanTypeListResult = new ArrayList<LoanTypeVO>(); // UTA: default value
		doReturn(getLoanTypeListResult).when(getLoanPlanDataResult).getLoanTypeList();

		Integer getMaxNumberOfOutstandingLoansResult = 0; // UTA: default value
		when(getLoanPlanDataResult.getMaxNumberOfOutstandingLoans()).thenReturn(getMaxNumberOfOutstandingLoansResult);

		boolean isNoMoneyTypeAllowedForLoanResult = false; // UTA: default value
		when(getLoanPlanDataResult.isNoMoneyTypeAllowedForLoan()).thenReturn(isNoMoneyTypeAllowedForLoanResult);
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
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Loan
	 */
	private static Loan mockLoan4() throws Throwable {
		Loan loan = mock(Loan.class);
		
		List<LoanMoneyType> loan1 = new ArrayList<LoanMoneyType>();
		LoanMoneyType loanType = mock(LoanMoneyType.class);
		when(loanType.getMoneyTypeId()).thenReturn("");
		loan1.add(loanType);
		
		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getCurrentAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAccountBalance()).thenReturn(getCurrentAccountBalanceResult);

		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter10();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = null; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		Date getEffectiveDateResult = mockDate13();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation4();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate14();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData4();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData4();
		when(loan.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

		Integer getLoginUserProfileIdResult = 0; // UTA: default value
		when(loan.getLoginUserProfileId()).thenReturn(getLoginUserProfileIdResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = null; // UTA: default value
		when(loan.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		List<LoanMessage> getMessagesResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getMessagesResult).when(loan).getMessages();

		List<LoanMoneyType> getMoneyTypesResult = new ArrayList<LoanMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(loan).getMoneyTypes();

		Integer getOutstandingLoansCountResult2 = null; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getStatusResult = ""; // UTA: default value
		when(loan.getStatus()).thenReturn(getStatusResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(loan.getSubmissionId()).thenReturn(getSubmissionIdResult);

		boolean isAnyMoneyTypeNotAContractMoneyTypeResult = false; // UTA: default value
		when(loan.isAnyMoneyTypeNotAContractMoneyType()).thenReturn(isAnyMoneyTypeNotAContractMoneyTypeResult);

		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		return loan;
	}
	

	/**
	 * Parasoft Jtest UTA: Test for sendForAcceptance(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#sendForAcceptance(Loan)
	 * @author patelpo
	 */
	@Test
	public void testSendForAcceptance() throws Throwable {
		Loan loan = mockLoan5();
		LoanStateContext newLoanStateContextResult = mock(LoanStateContext.class); // UTA: default value
		whenNew(LoanStateContext.class).withAnyArguments().thenReturn(newLoanStateContextResult);
		when(newLoanStateContextResult.getLoan()).thenReturn(loan);
		spy(LoanStateFactory.class);

		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));
		PowerMockito.doNothing().when(getStateResult).validate(any(LoanStateEnum.class), any(LoanStateEnum.class), any(LoanStateContext.class));
		

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan result = underTest.sendForAcceptance(loan);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testSendForAcceptance_1() throws Throwable {
		Loan loan = mockLoan5_1();
		LoanStateContext newLoanStateContextResult = mock(LoanStateContext.class); // UTA: default value
		whenNew(LoanStateContext.class).withAnyArguments().thenReturn(newLoanStateContextResult);
		when(newLoanStateContextResult.getLoan()).thenReturn(loan);
		
		LoanPlanData planData = mock(LoanPlanData.class);
		when(planData.getContractName()).thenReturn("CONTRACT_NAME");
		
		LoanParticipantData participantData = mock(LoanParticipantData.class);
		when(participantData.getFirstName()).thenReturn("First");
		
		when(newLoanStateContextResult.getLoanPlanData()).thenReturn(planData);
		when(newLoanStateContextResult.getLoanParticipantData()).thenReturn(participantData);
		
		
		spy(LoanStateFactory.class);
		
		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));
		PowerMockito.doNothing().when(getStateResult).validate(any(LoanStateEnum.class), any(LoanStateEnum.class), any(LoanStateContext.class));
		
		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		
		
		
		// Given
		PendingReviewState underTest = new PendingReviewState();
		
		// When
		Loan result = underTest.sendForAcceptance(loan);
		
		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp43() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp44() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter11() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Object cloneResult2 = new Object(); // UTA: default value
		when(getAcceptedParameterResult.clone()).thenReturn(cloneResult2);

		Timestamp getCreatedResult = mockTimestamp43();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp44();
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
	private static AtRiskDetailsVO mockAtRiskDetailsVO4() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp45() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp46() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp47() throws Throwable {
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
		Object cloneResult3 = new Object(); // UTA: default value
		when(getCurrentAdministratorNoteResult.clone()).thenReturn(cloneResult3);

		Timestamp getCreatedResult3 = mockTimestamp46();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp47();
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
	private static Timestamp mockTimestamp48() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp49() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter12() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Object cloneResult4 = new Object(); // UTA: default value
		when(getCurrentLoanParameterResult.clone()).thenReturn(cloneResult4);

		Timestamp getCreatedResult4 = mockTimestamp48();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp49();
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
	private static Timestamp mockTimestamp50() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp51() throws Throwable {
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
		Object cloneResult5 = new Object(); // UTA: default value
		when(getCurrentParticipantNoteResult.clone()).thenReturn(cloneResult5);

		Timestamp getCreatedResult5 = mockTimestamp50();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp51();
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
	private static Date mockDate16() throws Throwable {
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
	private static Timestamp mockTimestamp52() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp53() throws Throwable {
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
		Timestamp getCreatedResult6 = mockTimestamp52();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp53();
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
	private static Timestamp mockTimestamp54() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData5() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		Object cloneResult6 = new Object(); // UTA: default value
		when(getLoanParticipantDataResult.clone()).thenReturn(cloneResult6);

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
	private static LoanPlanData mockLoanPlanData5() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		Object cloneResult7 = new Object(); // UTA: default value
		when(getLoanPlanDataResult.clone()).thenReturn(cloneResult7);

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
	private static Date mockDate18() throws Throwable {
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
	private static Timestamp mockTimestamp55() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp56() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter13() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Object cloneResult8 = new Object(); // UTA: default value
		when(getOriginalParameterResult.clone()).thenReturn(cloneResult8);

		Timestamp getCreatedResult7 = mockTimestamp55();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		Timestamp getLastUpdatedResult7 = mockTimestamp56();
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
	private static DistributionAddress mockDistributionAddress3() throws Throwable {
		DistributionAddress getAddressResult = mock(DistributionAddress.class);
		String getCountryCodeResult = ""; // UTA: default value
		when(getAddressResult.getCountryCode()).thenReturn(getCountryCodeResult);

		Timestamp getLastUpdatedResult8 = mock(Timestamp.class);
		when(getAddressResult.getLastUpdated()).thenReturn(getLastUpdatedResult8);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient4() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		Object cloneResult9 = new Object(); // UTA: default value
		when(getRecipientResult.clone()).thenReturn(cloneResult9);

		DistributionAddress getAddressResult = mockDistributionAddress3();
		when(getRecipientResult.getAddress()).thenReturn(getAddressResult);

		Timestamp getCreatedResult8 = mock(Timestamp.class);
		when(getRecipientResult.getCreated()).thenReturn(getCreatedResult8);

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

		Payee payee = mock(LoanPayee.class);
		PaymentInstruction payementInstruction = mock(PaymentInstruction.class);
		when(payee.getPaymentInstruction()).thenReturn(payementInstruction);
		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		getPayeesResult.add(payee);
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
	private static Loan mockLoan5() throws Throwable {
		Loan loan = mock(Loan.class);

		Object cloneResult = new Object(); // UTA: default value
		when(loan.clone()).thenReturn(cloneResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

		LoanParameter getAcceptedParameterResult = mockLoanParameter11();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);

		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO4();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);

		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);

		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);

		Timestamp getCreatedResult2 = mockTimestamp45();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote7();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter12();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote8();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate15();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation5();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate16();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee4();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate17();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp54();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData5();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData5();
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

		Date getMaturityDateResult = mockDate18();
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

		LoanParameter getOriginalParameterResult = mockLoanParameter13();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

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
	private static Loan mockLoan5_1() throws Throwable {
		Loan loan = mock(Loan.class);
		
		Object cloneResult = new Object(); // UTA: default value
		when(loan.clone()).thenReturn(cloneResult);
		
		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);
		
		LoanParameter getAcceptedParameterResult = mockLoanParameter11();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);
		
		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO4();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);
		
		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);
		
		Integer getContractIdResult = 0; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);
		
		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);
		
		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);
		
		Timestamp getCreatedResult2 = mockTimestamp45();
		when(loan.getCreated()).thenReturn(getCreatedResult2);
		
		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);
		
		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);
		
		LoanNote getCurrentAdministratorNoteResult = mockLoanNote7();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);
		
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter12();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);
		
		LoanNote getCurrentParticipantNoteResult = mockLoanNote8();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);
		
		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();
		
		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);
		
		Date getEffectiveDateResult = mockDate15();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation5();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);
		
		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();
		
		Date getExpirationDateResult = mockDate16();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		Fee getFeeResult = mockFee4();
		when(loan.getFee()).thenReturn(getFeeResult);
		
		Date getFirstPayrollDateResult = mockDate17();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);
		
		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);
		
		Timestamp getLastUpdatedResult6 = mockTimestamp54();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);
		
		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);
		
		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);
		
		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData5();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);
		
		LoanPlanData getLoanPlanDataResult = mockLoanPlanData5();
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
		
		Date getMaturityDateResult = mockDate18();
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
		
		LoanParameter getOriginalParameterResult = mockLoanParameter13();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);
		
		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);
		
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
		
		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(Loan)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#sendForApproval(Loan)
	 * @author patelpo
	 */
	@Test
	public void testSendForApproval() throws Throwable {
		spy(LoanStateFactory.class);

		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));

		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		Loan loan = mockLoan6();
		Loan result = underTest.sendForApproval(loan);

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testSendForApproval_1() throws Throwable {
		spy(LoanStateFactory.class);
		
		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));
		
		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));
		
		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		
		
		// Given
		PendingReviewState underTest = new PendingReviewState();
		
		// When
		Loan loan = mockLoan6_1();
		Loan result = underTest.sendForApproval(loan);
		
		// Then
		// assertNotNull(result);
	}
	@Test
	public void testSendForApproval_2() throws Throwable {
		spy(LoanStateFactory.class);
		
		LoanState getStateResult = mock(LoanState.class);
		doReturn(getStateResult).when(LoanStateFactory.class);
		LoanStateFactory.getState(nullable(LoanStateEnum.class));
		
		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));
		
		spy(EventFactory.class);
		EventFactory getInstanceResult = mock(EventFactory.class);
		doReturn(getInstanceResult).when(EventFactory.class);
		EventFactory.getInstance();
		LoanEventGenerator loanEventGenerator = mock(LoanEventGenerator.class);
		when(getInstanceResult.getLoanEventGenerator(anyInt(), anyInt(), anyInt())).thenReturn(loanEventGenerator);
		
		
		// Given
		PendingReviewState underTest = new PendingReviewState();
		
		// When
		Loan loan = mockLoan6_2();
		Loan result = underTest.sendForApproval(loan);
		
		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp57() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp58() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter14() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult = mockTimestamp57();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp58();
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
	private static AtRiskDetailsVO mockAtRiskDetailsVO5() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getAtRiskDetailsVOResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp59() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult4 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult4);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp60() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult5);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp61() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult6);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote9() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult3 = mockTimestamp60();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp61();
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
	private static Timestamp mockTimestamp62() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult8);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp63() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult9);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter15() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult4 = mockTimestamp62();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp63();
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
	private static Timestamp mockTimestamp64() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult11);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp65() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult12 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult12);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanNote
	 */
	private static LoanNote mockLoanNote10() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Timestamp getCreatedResult5 = mockTimestamp64();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp65();
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
	private static EmployeeVestingInformation mockEmployeeVestingInformation6() throws Throwable {
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
	private static Timestamp mockTimestamp66() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult16);
		return getCreatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp67() throws Throwable {
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
		Timestamp getCreatedResult6 = mockTimestamp66();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp67();
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
	private static Timestamp mockTimestamp68() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult6;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParticipantData
	 */
	private static LoanParticipantData mockLoanParticipantData6() throws Throwable {
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
	private static LoanPlanData mockLoanPlanData6() throws Throwable {
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
	private static Date mockDate22() throws Throwable {
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
	private static Timestamp mockTimestamp69() throws Throwable {
		Timestamp getCreatedResult7 = mock(Timestamp.class);
		String toStringResult23 = ""; // UTA: default value
		when(getCreatedResult7.toString()).thenReturn(toStringResult23);
		return getCreatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp70() throws Throwable {
		Timestamp getLastUpdatedResult7 = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getLastUpdatedResult7.toString()).thenReturn(toStringResult24);
		return getLastUpdatedResult7;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter16() throws Throwable {
		LoanParameter getOriginalParameterResult = mock(LoanParameter.class);
		Timestamp getCreatedResult7 = mockTimestamp69();
		when(getOriginalParameterResult.getCreated()).thenReturn(getCreatedResult7);

		Timestamp getLastUpdatedResult7 = mockTimestamp70();
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
	private static LoanRecipient mockLoanRecipient5() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress4();
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

		Payee payee = new LoanPayee();
		Collection<Payee> getPayeesResult = new ArrayList<Payee>(); // UTA: default value
		getPayeesResult.add(payee);
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
	private static Loan mockLoan6() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter14();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);

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

		Timestamp getCreatedResult2 = mockTimestamp59();
		when(loan.getCreated()).thenReturn(getCreatedResult2);

		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);

		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);

		LoanNote getCurrentAdministratorNoteResult = mockLoanNote9();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter15();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		LoanNote getCurrentParticipantNoteResult = mockLoanNote10();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);

		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();

		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate19();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation6();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);

		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();

		Date getExpirationDateResult = mockDate20();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee5();
		when(loan.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate21();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);

		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);

		Timestamp getLastUpdatedResult6 = mockTimestamp68();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);

		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);

		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData6();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData6();
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

		Date getMaturityDateResult = mockDate22();
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

		LoanParameter getOriginalParameterResult = mockLoanParameter16();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);

		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);

		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);

		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);

		LoanRecipient getRecipientResult = mockLoanRecipient5();
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
	private static Loan mockLoan6_1() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter14();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);
		
		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);
		
		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO5();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);
		
		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);
		
		Integer getContractIdResult = 1; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);
		
		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);
		
		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);
		
		Timestamp getCreatedResult2 = mockTimestamp59();
		when(loan.getCreated()).thenReturn(getCreatedResult2);
		
		String getCreatedByRoleCodeResult = "PA"; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);
		
		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);
		
		LoanNote getCurrentAdministratorNoteResult = mockLoanNote9();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);
		
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter15();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);
		
		LoanNote getCurrentParticipantNoteResult = mockLoanNote10();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);
		
		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();
		
		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);
		
		Date getEffectiveDateResult = mockDate19();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation6();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);
		
		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();
		
		Date getExpirationDateResult = mockDate20();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		Fee getFeeResult = mockFee5();
		when(loan.getFee()).thenReturn(getFeeResult);
		
		Date getFirstPayrollDateResult = mockDate21();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);
		
		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);
		
		Timestamp getLastUpdatedResult6 = mockTimestamp68();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);
		
		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);
		
		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);
		
		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData6();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);
		
		LoanPlanData getLoanPlanDataResult = mockLoanPlanData6();
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
		
		Date getMaturityDateResult = mockDate22();
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
		
		LoanParameter getOriginalParameterResult = mockLoanParameter16();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);
		
		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);
		
		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);
		
		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);
		
		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);
		
		LoanRecipient getRecipientResult = mockLoanRecipient5();
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
		
		boolean isOKResult = true; // UTA: default value
		when(loan.isOK()).thenReturn(isOKResult);
		
		boolean isParticipantInitiatedResult = false; // UTA: default value
		when(loan.isParticipantInitiated()).thenReturn(isParticipantInitiatedResult);
		
		boolean isStatusChangeResult = false; // UTA: default value
		when(loan.isStatusChange()).thenReturn(isStatusChangeResult);
		
		String toStringResult27 = ""; // UTA: default value
		when(loan.toString()).thenReturn(toStringResult27);
		return loan;
	}
	private static Loan mockLoan6_2() throws Throwable {
		Loan loan = mock(Loan.class);
		LoanParameter getAcceptedParameterResult = mockLoanParameter14();
		when(loan.getAcceptedParameter()).thenReturn(getAcceptedParameterResult);
		
		LoanSettings getLoanSettingsResult = mockLoanSettings();
		when(loan.getLoanSettings()).thenReturn(getLoanSettingsResult);
		
		Boolean getApplyIrs10KDollarRuleIndResult = false; // UTA: default value
		when(loan.getApplyIrs10KDollarRuleInd()).thenReturn(getApplyIrs10KDollarRuleIndResult);
		
		AtRiskDetailsVO getAtRiskDetailsVOResult = mockAtRiskDetailsVO5();
		when(loan.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);
		
		String getAtRiskIndResult = ""; // UTA: default value
		when(loan.getAtRiskInd()).thenReturn(getAtRiskIndResult);
		
		Integer getContractIdResult = 1; // UTA: default value
		when(loan.getContractId()).thenReturn(getContractIdResult);
		
		BigDecimal getContractLoanExpenseMarginPctResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanExpenseMarginPct()).thenReturn(getContractLoanExpenseMarginPctResult);
		
		BigDecimal getContractLoanMonthlyFlatFeeResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult);
		
		Timestamp getCreatedResult2 = mockTimestamp59();
		when(loan.getCreated()).thenReturn(getCreatedResult2);
		
		String getCreatedByRoleCodeResult = ""; // UTA: default value
		when(loan.getCreatedByRoleCode()).thenReturn(getCreatedByRoleCodeResult);
		
		Integer getCreatedIdResult = 0; // UTA: default value
		when(loan.getCreatedId()).thenReturn(getCreatedIdResult);
		
		LoanNote getCurrentAdministratorNoteResult = mockLoanNote9();
		when(loan.getCurrentAdministratorNote()).thenReturn(getCurrentAdministratorNoteResult);
		
		LoanParameter getCurrentLoanParameterResult = mockLoanParameter15();
		when(loan.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);
		
		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(loan.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);
		
		LoanNote getCurrentParticipantNoteResult = mockLoanNote10();
		when(loan.getCurrentParticipantNote()).thenReturn(getCurrentParticipantNoteResult);
		
		List<LoanDeclaration> getDeclarationsResult = new ArrayList<LoanDeclaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(loan).getDeclarations();
		
		String getDefaultProvisionResult = ""; // UTA: default value
		when(loan.getDefaultProvision()).thenReturn(getDefaultProvisionResult);
		
		Date getEffectiveDateResult = mockDate19();
		when(loan.getEffectiveDate()).thenReturn(getEffectiveDateResult);
		
		EmployeeVestingInformation getEmployeeVestingInformationResult = mockEmployeeVestingInformation6();
		when(loan.getEmployeeVestingInformation()).thenReturn(getEmployeeVestingInformationResult);
		
		List<LoanMessage> getErrorsResult2 = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult2).when(loan).getErrors();
		
		Date getExpirationDateResult = mockDate20();
		when(loan.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		Fee getFeeResult = mockFee5();
		when(loan.getFee()).thenReturn(getFeeResult);
		
		Date getFirstPayrollDateResult = mockDate21();
		when(loan.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);
		
		Integer getLastFeeChangedByTpaProfileIdResult = 0; // UTA: default value
		when(loan.getLastFeeChangedByTpaProfileId()).thenReturn(getLastFeeChangedByTpaProfileIdResult);
		
		Boolean getLastFeeChangedWasPlanSponsorUserIndResult = false; // UTA: default value
		when(loan.getLastFeeChangedWasPlanSponsorUserInd()).thenReturn(getLastFeeChangedWasPlanSponsorUserIndResult);
		
		Timestamp getLastUpdatedResult6 = mockTimestamp68();
		when(loan.getLastUpdated()).thenReturn(getLastUpdatedResult6);
		
		Integer getLastUpdatedIdResult = 0; // UTA: default value
		when(loan.getLastUpdatedId()).thenReturn(getLastUpdatedIdResult);
		
		Boolean getLegallyMarriedIndResult = false; // UTA: default value
		when(loan.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);
		
		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData6();
		when(loan.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);
		
		LoanPlanData getLoanPlanDataResult = mockLoanPlanData6();
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
		
		Date getMaturityDateResult = mockDate22();
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
		
		LoanParameter getOriginalParameterResult = mockLoanParameter16();
		when(loan.getOriginalParameter()).thenReturn(getOriginalParameterResult);
		
		Integer getOutstandingLoansCountResult = 0; // UTA: default value
		when(loan.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult);
		
		Integer getParticipantIdResult = 0; // UTA: default value
		when(loan.getParticipantId()).thenReturn(getParticipantIdResult);
		
		Integer getParticipantProfileIdResult = 0; // UTA: default value
		when(loan.getParticipantProfileId()).thenReturn(getParticipantProfileIdResult);
		
		String getPreviousStatusResult = ""; // UTA: default value
		when(loan.getPreviousStatus()).thenReturn(getPreviousStatusResult);
		
		LoanRecipient getRecipientResult = mockLoanRecipient5();
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
		
		boolean isOKResult = true; // UTA: default value
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
	 * Parasoft Jtest UTA: Test for validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 *
	 * @see com.manulife.pension.service.loan.domain.PendingReviewState#validate(LoanStateEnum, LoanStateEnum, LoanStateContext)
	 * @author patelpo
	 */
	@Test
	public void testValidate() throws Throwable {
		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), nullable(Loan.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));

		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));

		// Given
		PendingReviewState underTest = new PendingReviewState();

		// When
		LoanStateEnum fromState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateEnum toState = LoanStateEnum.DRAFT; // UTA: default value
		LoanStateContext context = mockLoanStateContext();
		underTest.validate(fromState, toState, context);

	}
	@Test
	public void testValidate_1() throws Throwable {
		spy(LoanValidationHelper.class);
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateLIA((List) any(), nullable(Loan.class));
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validatePayrollDate(nullable(LoanStateContext.class), nullable(LoanStateEnum.class));
		
		PowerMockito.doNothing().when(LoanValidationHelper.class);
		LoanValidationHelper.validateExpirationDate(nullable(Loan.class));
		
		// Given
		PendingReviewState underTest = new PendingReviewState();
		
		// When
		LoanStateEnum fromState = LoanStateEnum.PENDING_APPROVAL; // UTA: default value
		LoanStateEnum toState = LoanStateEnum.PENDING_REVIEW; // UTA: default value
		LoanStateContext context = mockLoanStateContext();
		underTest.validate(fromState, toState, context);
		
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate23() throws Throwable {
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
		Date getCurrentOrNextBusinessDateResult = mockDate23();
		when(getBusinessCalendarResult.getCurrentOrNextBusinessDate(any(Date.class)))
				.thenReturn(getCurrentOrNextBusinessDateResult);

		String toStringResult2 = ""; // UTA: default value
		when(getBusinessCalendarResult.toString()).thenReturn(toStringResult2);
		return getBusinessCalendarResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanParameter
	 */
	private static LoanParameter mockLoanParameter17() throws Throwable {
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
	private static Date mockDate24() throws Throwable {
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
	private static Date mockDate25() throws Throwable {
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
	private static Fee mockFee6() throws Throwable {
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
	private static Date mockDate26() throws Throwable {
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
	private static LoanParticipantData mockLoanParticipantData7() throws Throwable {
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
	private static LoanPlanData mockLoanPlanData7() throws Throwable {
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
	private static LoanSettings mockLoanSettings2() throws Throwable {
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

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getAddressResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getZipCodeResult2 = ""; // UTA: default value
		when(getAddressResult.getZipCode()).thenReturn(getZipCodeResult2);
		return getAddressResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LoanRecipient
	 */
	private static LoanRecipient mockLoanRecipient6() throws Throwable {
		LoanRecipient getRecipientResult = mock(LoanRecipient.class);
		DistributionAddress getAddressResult = mockDistributionAddress5();
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
	private static Date mockDate27() throws Throwable {
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
	private static Loan mockLoan7() throws Throwable {
		Loan getLoanResult = mock(Loan.class);
		BigDecimal getCurrentAvailableAccountBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentAvailableAccountBalance()).thenReturn(getCurrentAvailableAccountBalanceResult);

		LoanParameter getCurrentLoanParameterResult = mockLoanParameter17();
		when(getLoanResult.getCurrentLoanParameter()).thenReturn(getCurrentLoanParameterResult);

		BigDecimal getCurrentOutstandingBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getCurrentOutstandingBalance()).thenReturn(getCurrentOutstandingBalanceResult);

		String getDefaultProvisionResult = ""; // UTA: default value
		when(getLoanResult.getDefaultProvision()).thenReturn(getDefaultProvisionResult);

		Date getEffectiveDateResult = mockDate24();
		when(getLoanResult.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		List<LoanMessage> getErrorsResult = new ArrayList<LoanMessage>(); // UTA: default value
		doReturn(getErrorsResult).when(getLoanResult).getErrors();

		Date getExpirationDateResult = mockDate25();
		when(getLoanResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		Fee getFeeResult = mockFee6();
		when(getLoanResult.getFee()).thenReturn(getFeeResult);

		Date getFirstPayrollDateResult = mockDate26();
		when(getLoanResult.getFirstPayrollDate()).thenReturn(getFirstPayrollDateResult);

		LoanParticipantData getLoanParticipantDataResult = mockLoanParticipantData7();
		when(getLoanResult.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult);

		LoanPlanData getLoanPlanDataResult = mockLoanPlanData7();
		when(getLoanResult.getLoanPlanData()).thenReturn(getLoanPlanDataResult);

		String getLoanReasonResult = ""; // UTA: default value
		when(getLoanResult.getLoanReason()).thenReturn(getLoanReasonResult);

		LoanSettings getLoanSettingsResult = mockLoanSettings2();
		when(getLoanResult.getLoanSettings()).thenReturn(getLoanSettingsResult);

		String getLoanTypeResult = ""; // UTA: default value
		when(getLoanResult.getLoanType()).thenReturn(getLoanTypeResult);

		String getLoginRoleCodeResult = "JH"; // UTA: default value
		when(getLoanResult.getLoginRoleCode()).thenReturn(getLoginRoleCodeResult);

		BigDecimal getMaxBalanceLast12MonthsResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanResult.getMaxBalanceLast12Months()).thenReturn(getMaxBalanceLast12MonthsResult2);

		Integer getMaximumAmortizationYearsResult = 0; // UTA: default value
		when(getLoanResult.getMaximumAmortizationYears()).thenReturn(getMaximumAmortizationYearsResult);

		Integer getOutstandingLoansCountResult2 = 0; // UTA: default value
		when(getLoanResult.getOutstandingLoansCount()).thenReturn(getOutstandingLoansCountResult2);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getLoanResult.getParticipantId()).thenReturn(getParticipantIdResult);

		LoanRecipient getRecipientResult = mockLoanRecipient6();
		when(getLoanResult.getRecipient()).thenReturn(getRecipientResult);

		Date getRequestDateResult = mockDate27();
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
	private static LoanParticipantData mockLoanParticipantData8() throws Throwable {
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
	private static LoanPlanData mockLoanPlanData8() throws Throwable {
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
	private static LoanSettings mockLoanSettings3() throws Throwable {
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

		Loan getLoanResult = mockLoan7();
		when(context.getLoan()).thenReturn(getLoanResult);

		LoanParticipantData getLoanParticipantDataResult2 = mockLoanParticipantData8();
		when(context.getLoanParticipantData()).thenReturn(getLoanParticipantDataResult2);

		LoanPlanData getLoanPlanDataResult2 = mockLoanPlanData8();
		when(context.getLoanPlanData()).thenReturn(getLoanPlanDataResult2);

		LoanSettings getLoanSettingsResult2 = mockLoanSettings3();
		when(context.getLoanSettings()).thenReturn(getLoanSettingsResult2);

		boolean isPrintLoanDocumentResult = false; // UTA: default value
		when(context.isPrintLoanDocument()).thenReturn(isPrintLoanDocumentResult);

		boolean isSaveAndExitResult = false; // UTA: default value
		when(context.isSaveAndExit()).thenReturn(isSaveAndExitResult);

		String toStringResult16 = ""; // UTA: default value
		when(context.toString()).thenReturn(toStringResult16);
		return context;
	}

	
	private static Timestamp mockTimestamp71() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		Class getClassResult2 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult2).when(getCreatedResult).getClass();

		String toStringResult = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult);
		return getCreatedResult;
	}

	private static Timestamp mockTimestamp72() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		Class getClassResult3 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult3).when(getLastUpdatedResult).getClass();

		String toStringResult2 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult2);
		return getLastUpdatedResult;
	}

	private static LoanParameter mockLoanParameter18() throws Throwable {
		LoanParameter getAcceptedParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult = 0; // UTA: default value
		when(getAcceptedParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult);

		Class getClassResult = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult).when(getAcceptedParameterResult).getClass();

		Timestamp getCreatedResult = mockTimestamp71();
		when(getAcceptedParameterResult.getCreated()).thenReturn(getCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp72();
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

	private static AtRiskDetailsVO mockAtRiskDetailsVO6() throws Throwable {
		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		Class getClassResult4 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult4).when(getAtRiskDetailsVOResult).getClass();

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getAtRiskDetailsVOResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		String toStringResult4 = ""; // UTA: default value
		when(getAtRiskDetailsVOResult.toString()).thenReturn(toStringResult4);
		return getAtRiskDetailsVOResult;
	}

	private static Timestamp mockTimestamp73() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		Class getClassResult6 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult6).when(getCreatedResult2).getClass();

		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult5);
		return getCreatedResult2;
	}

	private static Timestamp mockTimestamp74() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		Class getClassResult8 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult8).when(getCreatedResult3).getClass();

		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult6);
		return getCreatedResult3;
	}

	private static Timestamp mockTimestamp75() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		Class getClassResult9 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult9).when(getLastUpdatedResult2).getClass();

		String toStringResult7 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult7);
		return getLastUpdatedResult2;
	}

	private static LoanNote mockLoanNote11() throws Throwable {
		LoanNote getCurrentAdministratorNoteResult = mock(LoanNote.class);
		Class getClassResult7 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult7).when(getCurrentAdministratorNoteResult).getClass();

		Timestamp getCreatedResult3 = mockTimestamp74();
		when(getCurrentAdministratorNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp75();
		when(getCurrentAdministratorNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdministratorNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult8 = ""; // UTA: default value
		when(getCurrentAdministratorNoteResult.toString()).thenReturn(toStringResult8);
		return getCurrentAdministratorNoteResult;
	}

	private static Timestamp mockTimestamp76() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		Class getClassResult11 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult11).when(getCreatedResult4).getClass();

		String toStringResult9 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult9);
		return getCreatedResult4;
	}

	private static Timestamp mockTimestamp77() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		Class getClassResult12 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult12).when(getLastUpdatedResult3).getClass();

		String toStringResult10 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult10);
		return getLastUpdatedResult3;
	}

	private static LoanParameter mockLoanParameter19() throws Throwable {
		LoanParameter getCurrentLoanParameterResult = mock(LoanParameter.class);
		Integer getAmortizationMonthsResult2 = 0; // UTA: default value
		when(getCurrentLoanParameterResult.getAmortizationMonths()).thenReturn(getAmortizationMonthsResult2);

		Class getClassResult10 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult10).when(getCurrentLoanParameterResult).getClass();

		Timestamp getCreatedResult4 = mockTimestamp76();
		when(getCurrentLoanParameterResult.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mockTimestamp77();
		when(getCurrentLoanParameterResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		BigDecimal getPaymentAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getCurrentLoanParameterResult.getPaymentAmount()).thenReturn(getPaymentAmountResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.getStatusCode()).thenReturn(getStatusCodeResult2);

		boolean isReadyToSaveResult2 = false; // UTA: default value
		when(getCurrentLoanParameterResult.isReadyToSave()).thenReturn(isReadyToSaveResult2);

		String toStringResult11 = ""; // UTA: default value
		when(getCurrentLoanParameterResult.toString()).thenReturn(toStringResult11);
		return getCurrentLoanParameterResult;
	}

	private static Timestamp mockTimestamp78() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		Class getClassResult14 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult14).when(getCreatedResult5).getClass();

		String toStringResult12 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult12);
		return getCreatedResult5;
	}

	private static Timestamp mockTimestamp79() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		Class getClassResult15 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult15).when(getLastUpdatedResult4).getClass();

		String toStringResult13 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult13);
		return getLastUpdatedResult4;
	}

	private static LoanNote mockLoanNote12() throws Throwable {
		LoanNote getCurrentParticipantNoteResult = mock(LoanNote.class);
		Class getClassResult13 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult13).when(getCurrentParticipantNoteResult).getClass();

		Timestamp getCreatedResult5 = mockTimestamp78();
		when(getCurrentParticipantNoteResult.getCreated()).thenReturn(getCreatedResult5);

		Timestamp getLastUpdatedResult4 = mockTimestamp79();
		when(getCurrentParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult14 = ""; // UTA: default value
		when(getCurrentParticipantNoteResult.toString()).thenReturn(toStringResult14);
		return getCurrentParticipantNoteResult;
	}

	private static Date mockDate28() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(nullable(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getEffectiveDateResult.before(nullable(Date.class))).thenReturn(beforeResult);

		Class getClassResult16 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult16).when(getEffectiveDateResult).getClass();

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

		String toStringResult15 = ""; // UTA: default value
		when(getEffectiveDateResult.toString()).thenReturn(toStringResult15);
		return getEffectiveDateResult;
	}

	private static EmployeeVestingInformation mockEmployeeVestingInformation7() throws Throwable {
		EmployeeVestingInformation getEmployeeVestingInformationResult = mock(EmployeeVestingInformation.class);
		Class getClassResult17 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult17).when(getEmployeeVestingInformationResult).getClass();

		Integer getContractIdResult2 = 0; // UTA: default value
		when(getEmployeeVestingInformationResult.getContractId()).thenReturn(getContractIdResult2);

		Set getErrorsResult = new HashSet(); // UTA: default value
		when(getEmployeeVestingInformationResult.getErrors()).thenReturn(getErrorsResult);

		Map getMoneyTypeVestingPercentagesResult = new HashMap(); // UTA: default value
		when(getEmployeeVestingInformationResult.getMoneyTypeVestingPercentages())
				.thenReturn(getMoneyTypeVestingPercentagesResult);

		String toStringResult16 = ""; // UTA: default value
		when(getEmployeeVestingInformationResult.toString()).thenReturn(toStringResult16);
		return getEmployeeVestingInformationResult;
	}

	private static Date mockDate29() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getExpirationDateResult.after(nullable(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getExpirationDateResult.before(nullable(Date.class))).thenReturn(beforeResult2);

		Class getClassResult18 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult18).when(getExpirationDateResult).getClass();

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

		String toStringResult17 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult17);
		return getExpirationDateResult;
	}

	private static Timestamp mockTimestamp80() throws Throwable {
		Timestamp getCreatedResult6 = mock(Timestamp.class);
		Class getClassResult20 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult20).when(getCreatedResult6).getClass();

		String toStringResult18 = ""; // UTA: default value
		when(getCreatedResult6.toString()).thenReturn(toStringResult18);
		return getCreatedResult6;
	}

	private static Timestamp mockTimestamp81() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		Class getClassResult21 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult21).when(getLastUpdatedResult5).getClass();

		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult5;
	}

	private static Fee mockFee7() throws Throwable {
		Fee getFeeResult = mock(Fee.class);
		Class getClassResult19 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult19).when(getFeeResult).getClass();

		Timestamp getCreatedResult6 = mockTimestamp80();
		when(getFeeResult.getCreated()).thenReturn(getCreatedResult6);

		Timestamp getLastUpdatedResult5 = mockTimestamp81();
		when(getFeeResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getFeeResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		String getTypeCodeResult = ""; // UTA: default value
		when(getFeeResult.getTypeCode()).thenReturn(getTypeCodeResult);

		BigDecimal getValueResult = BigDecimal.ZERO; // UTA: default value
		when(getFeeResult.getValue()).thenReturn(getValueResult);

		boolean isBlankResult3 = false; // UTA: default value
		when(getFeeResult.isBlank()).thenReturn(isBlankResult3);

		String toStringResult20 = ""; // UTA: default value
		when(getFeeResult.toString()).thenReturn(toStringResult20);
		return getFeeResult;
	}

	private static Date mockDate30() throws Throwable {
		Date getFirstPayrollDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.after(nullable(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getFirstPayrollDateResult.before(nullable(Date.class))).thenReturn(beforeResult3);

		Class getClassResult22 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult22).when(getFirstPayrollDateResult).getClass();

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

		String toStringResult21 = ""; // UTA: default value
		when(getFirstPayrollDateResult.toString()).thenReturn(toStringResult21);
		return getFirstPayrollDateResult;
	}

	private static Timestamp mockTimestamp82() throws Throwable {
		Timestamp getLastUpdatedResult6 = mock(Timestamp.class);
		Class getClassResult23 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult23).when(getLastUpdatedResult6).getClass();

		String toStringResult22 = ""; // UTA: default value
		when(getLastUpdatedResult6.toString()).thenReturn(toStringResult22);
		return getLastUpdatedResult6;
	}

	private static LoanParticipantData mockLoanParticipantData9() throws Throwable {
		LoanParticipantData getLoanParticipantDataResult = mock(LoanParticipantData.class);
		String getAddressLine1Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine1()).thenReturn(getAddressLine1Result);

		String getAddressLine2Result = ""; // UTA: default value
		when(getLoanParticipantDataResult.getAddressLine2()).thenReturn(getAddressLine2Result);

		String getCityResult = ""; // UTA: default value
		when(getLoanParticipantDataResult.getCity()).thenReturn(getCityResult);

		Class getClassResult24 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult24).when(getLoanParticipantDataResult).getClass();

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

		String toStringResult23 = ""; // UTA: default value
		when(getLoanParticipantDataResult.toString()).thenReturn(toStringResult23);
		return getLoanParticipantDataResult;
	}

	private static LoanPlanData mockLoanPlanData9() throws Throwable {
		LoanPlanData getLoanPlanDataResult = mock(LoanPlanData.class);
		Class getClassResult25 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult25).when(getLoanPlanDataResult).getClass();

		BigDecimal getContractLoanExpenseMarginPctResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanExpenseMarginPct())
				.thenReturn(getContractLoanExpenseMarginPctResult2);

		BigDecimal getContractLoanMonthlyFlatFeeResult2 = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanMonthlyFlatFee()).thenReturn(getContractLoanMonthlyFlatFeeResult2);

		BigDecimal getContractLoanSetupFeeAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getContractLoanSetupFeeAmount()).thenReturn(getContractLoanSetupFeeAmountResult);

		String getContractNameResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getContractName()).thenReturn(getContractNameResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPercentageResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMaximumLoanPercentage()).thenReturn(getMaximumLoanPercentageResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getLoanPlanDataResult.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getSpousalConsentReqdIndResult = ""; // UTA: default value
		when(getLoanPlanDataResult.getSpousalConsentReqdInd()).thenReturn(getSpousalConsentReqdIndResult);

		String toStringResult24 = ""; // UTA: default value
		when(getLoanPlanDataResult.toString()).thenReturn(toStringResult24);
		return getLoanPlanDataResult;
	}

	private static Date mockDate31() throws Throwable {
		Date getMaturityDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getMaturityDateResult.after(nullable(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getMaturityDateResult.before(nullable(Date.class))).thenReturn(beforeResult4);

		Class getClassResult26 = Class.forName("java.lang.Object"); // UTA: default value
		doReturn(getClassResult26).when(getMaturityDateResult).getClass();

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

		String toStringResult25 = ""; // UTA: default value
		when(getMaturityDateResult.toString()).thenReturn(toStringResult25);
		return getMaturityDateResult;
	}

	
}