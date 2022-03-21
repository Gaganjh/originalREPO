/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.VestingServiceDelegate;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.util.BaseEnvironment;
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
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mozilla.javascript.ObjArray;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for DraftState
 *
 * @see com.manulife.pension.service.withdrawal.domain.DraftState
 * @author patelpo
 */
@PrepareForTest({ WithdrawalRequest.class, Principal.class, ActivitySummaryDao.class, JdbcHelper.class,
		ActivityHistoryHelper.class, EmployeeServiceDelegate.class, WithdrawalVestingEngine.class,
		VestingServiceDelegate.class, WithdrawalLoggingHelper.class })
@RunWith(PowerMockRunner.class)
public class DraftStateTest {
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
	 * Parasoft Jtest UTA: Test for applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyDefaultDataForEdit() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal();
		WithdrawalRequest defaultVo = mockWithdrawalRequest2();
		underTest.applyDefaultDataForEdit(withdrawal, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getDisabilityDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getDisabilityDateResult.before(any(Date.class))).thenReturn(beforeResult);
		return getDisabilityDateResult;
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
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.before(any(Date.class))).thenReturn(beforeResult3);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getChequePayableToCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getChequePayableToCode()).thenReturn(getChequePayableToCodeResult);

		String getContractStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getContractStatusCode()).thenReturn(getContractStatusCodeResult);

		Map<String, String> getMoneyTypeAliasesResult = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult).when(getParticipantInfoResult).getMoneyTypeAliases();

		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult4);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getRetirementDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getRetirementDateResult.before(any(Date.class))).thenReturn(beforeResult5);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean afterResult6 = false; // UTA: default value
		when(getTerminationDateResult.after(any(Date.class))).thenReturn(afterResult6);

		boolean beforeResult6 = false; // UTA: default value
		when(getTerminationDateResult.before(any(Date.class))).thenReturn(beforeResult6);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		Date getDisabilityDateResult = mockDate();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Date getExpirationDateResult = mockDate2();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate3();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		DistributionAddress getParticipantAddressResult = mock(DistributionAddress.class);
		when(getWithdrawalRequestResult.getParticipantAddress()).thenReturn(getParticipantAddressResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate4();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		Date getRetirementDateResult = mockDate5();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		Date getTerminationDateResult = mockDate6();
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getDisabilityDateResult2 = mock(Date.class);
		boolean afterResult7 = false; // UTA: default value
		when(getDisabilityDateResult2.after(any(Date.class))).thenReturn(afterResult7);

		boolean beforeResult7 = false; // UTA: default value
		when(getDisabilityDateResult2.before(any(Date.class))).thenReturn(beforeResult7);
		return getDisabilityDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getExpirationDateResult2 = mock(Date.class);
		boolean afterResult8 = false; // UTA: default value
		when(getExpirationDateResult2.after(any(Date.class))).thenReturn(afterResult8);

		boolean beforeResult8 = false; // UTA: default value
		when(getExpirationDateResult2.before(any(Date.class))).thenReturn(beforeResult8);
		return getExpirationDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getMostRecentPriorContributionDateResult2 = mock(Date.class);
		boolean afterResult9 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.after(any(Date.class))).thenReturn(afterResult9);

		boolean beforeResult9 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.before(any(Date.class))).thenReturn(beforeResult9);
		return getMostRecentPriorContributionDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo2() throws Throwable {
		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		String getChequePayableToCodeResult2 = ""; // UTA: default value
		when(getParticipantInfoResult2.getChequePayableToCode()).thenReturn(getChequePayableToCodeResult2);

		String getContractStatusCodeResult2 = ""; // UTA: default value
		when(getParticipantInfoResult2.getContractStatusCode()).thenReturn(getContractStatusCodeResult2);

		Map<String, String> getMoneyTypeAliasesResult2 = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult2).when(getParticipantInfoResult2).getMoneyTypeAliases();

		String getParticipantStatusCodeResult2 = ""; // UTA: default value
		when(getParticipantInfoResult2.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult2);
		return getParticipantInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getRequestDateResult2 = mock(Date.class);
		boolean afterResult10 = false; // UTA: default value
		when(getRequestDateResult2.after(any(Date.class))).thenReturn(afterResult10);

		boolean beforeResult10 = false; // UTA: default value
		when(getRequestDateResult2.before(any(Date.class))).thenReturn(beforeResult10);
		return getRequestDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getRetirementDateResult2 = mock(Date.class);
		boolean afterResult11 = false; // UTA: default value
		when(getRetirementDateResult2.after(any(Date.class))).thenReturn(afterResult11);

		boolean beforeResult11 = false; // UTA: default value
		when(getRetirementDateResult2.before(any(Date.class))).thenReturn(beforeResult11);
		return getRetirementDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getTerminationDateResult2 = mock(Date.class);
		boolean afterResult12 = false; // UTA: default value
		when(getTerminationDateResult2.after(any(Date.class))).thenReturn(afterResult12);

		boolean beforeResult12 = false; // UTA: default value
		when(getTerminationDateResult2.before(any(Date.class))).thenReturn(beforeResult12);
		return getTerminationDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		when(defaultVo.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractNameResult2 = ""; // UTA: default value
		when(defaultVo.getContractName()).thenReturn(getContractNameResult2);

		Date getDisabilityDateResult2 = mockDate7();
		when(defaultVo.getDisabilityDate()).thenReturn(getDisabilityDateResult2);

		Date getExpirationDateResult2 = mockDate8();
		when(defaultVo.getExpirationDate()).thenReturn(getExpirationDateResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(defaultVo.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(defaultVo.getLastName()).thenReturn(getLastNameResult2);

		Collection<WithdrawalRequestLoan> getLoansResult2 = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult2).when(defaultVo).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult2 = mockDate9();
		when(defaultVo.getMostRecentPriorContributionDate()).thenReturn(getMostRecentPriorContributionDateResult2);

		DistributionAddress getParticipantAddressResult2 = mock(DistributionAddress.class);
		when(defaultVo.getParticipantAddress()).thenReturn(getParticipantAddressResult2);

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo2();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(defaultVo.getParticipantSSN()).thenReturn(getParticipantSSNResult2);

		String getPaymentToResult2 = ""; // UTA: default value
		when(defaultVo.getPaymentTo()).thenReturn(getPaymentToResult2);

		String getReasonCodeResult2 = ""; // UTA: default value
		when(defaultVo.getReasonCode()).thenReturn(getReasonCodeResult2);

		Collection<Recipient> getRecipientsResult2 = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult2).when(defaultVo).getRecipients();

		Date getRequestDateResult2 = mockDate10();
		when(defaultVo.getRequestDate()).thenReturn(getRequestDateResult2);

		Date getRetirementDateResult2 = mockDate11();
		when(defaultVo.getRetirementDate()).thenReturn(getRetirementDateResult2);

		Date getTerminationDateResult2 = mockDate12();
		when(defaultVo.getTerminationDate()).thenReturn(getTerminationDateResult2);
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for approve(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#approve(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testApprove() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal2();
		underTest.approve(withdrawal);

	}
	@Test
	public void testApprove_1() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class);
		WithdrawalLoggingHelper.log(nullable(WithdrawalRequest.class), nullable(WithdrawalEvent.class),
				nullable(Class.class), nullable(String.class));

		spy(ActivityHistoryHelper.class);

		PowerMockito.doNothing().when(ActivityHistoryHelper.class);
		ActivityHistoryHelper.saveOriginalValues(nullable(Withdrawal.class));
		
		// Given
		DraftState underTest = new DraftState();
		
		// When
		Withdrawal withdrawal = mockWithdrawal2_1();
		underTest.approve(withdrawal);
		
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		long getTimeResult = 0L; // UTA: default value
		when(getLastSavedTimestampResult.getTime()).thenReturn(getTimeResult);

		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getApprovedTimestampResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult2 = ""; // UTA: default value
		when(getApprovedTimestampResult.toString()).thenReturn(toStringResult2);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getBirthDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getBirthDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult3 = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult3 = ""; // UTA: default value
		when(getBirthDateResult.toString()).thenReturn(toStringResult3);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		String getClientAccountRepIdResult = ""; // UTA: default value
		when(getContractInfoResult.getClientAccountRepId()).thenReturn(getClientAccountRepIdResult);

		String getDefaultUnvestedMoneyOptionCodeResult = ""; // UTA: default value
		when(getContractInfoResult.getDefaultUnvestedMoneyOptionCode())
				.thenReturn(getDefaultUnvestedMoneyOptionCodeResult);

		Boolean getHasApprovePermissionResult = false; // UTA: default value
		when(getContractInfoResult.getHasApprovePermission()).thenReturn(getHasApprovePermissionResult);

		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);

		String getStatusResult = ""; // UTA: default value
		when(getContractInfoResult.getStatus()).thenReturn(getStatusResult);

		String getTeamCodeResult = ""; // UTA: default value
		when(getContractInfoResult.getTeamCode()).thenReturn(getTeamCodeResult);

		Boolean getTwoStepApprovalRequiredResult = true; // UTA: default value
		when(getContractInfoResult.getTwoStepApprovalRequired()).thenReturn(getTwoStepApprovalRequiredResult);

		Collection<DeCodeVO> getUnvestedMoneyOptionsResult = new ArrayList<DeCodeVO>(); // UTA: default value
		doReturn(getUnvestedMoneyOptionsResult).when(getContractInfoResult).getUnvestedMoneyOptions();

		Collection<WithdrawalReason> getWithdrawalReasonsResult = new ArrayList<WithdrawalReason>(); // UTA: default value
		doReturn(getWithdrawalReasonsResult).when(getContractInfoResult).getWithdrawalReasons();

		String toStringResult4 = ""; // UTA: default value
		when(getContractInfoResult.toString()).thenReturn(toStringResult4);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getCreatedResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult5);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getCreatedResult2.getTime()).thenReturn(getTimeResult5);

		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult6);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp5() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		long getTimeResult6 = 0L; // UTA: default value
		when(getLastUpdatedResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult7 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult7);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult);

		boolean doWarningCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult);

		Timestamp getCreatedResult2 = mockTimestamp4();
		when(getCurrentAdminToAdminNoteResult.getCreated()).thenReturn(getCreatedResult2);

		Timestamp getLastUpdatedResult = mockTimestamp5();
		when(getCurrentAdminToAdminNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult8 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.toString()).thenReturn(toStringResult8);
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp6() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		long getTimeResult7 = 0L; // UTA: default value
		when(getCreatedResult3.getTime()).thenReturn(getTimeResult7);

		String toStringResult9 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult9);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp7() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		long getTimeResult8 = 0L; // UTA: default value
		when(getLastUpdatedResult2.getTime()).thenReturn(getTimeResult8);

		String toStringResult10 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult10);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote2() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult2);

		boolean doWarningCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult2);

		Timestamp getCreatedResult3 = mockTimestamp6();
		when(getCurrentAdminToParticipantNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp7();
		when(getCurrentAdminToParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult11 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.toString()).thenReturn(toStringResult11);
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate14() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getDeathDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getDeathDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		long getTimeResult9 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult9);

		String toStringResult12 = ""; // UTA: default value
		when(getDeathDateResult.toString()).thenReturn(toStringResult12);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate15() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getDisabilityDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getDisabilityDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		long getTimeResult10 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult10);

		String toStringResult13 = ""; // UTA: default value
		when(getDisabilityDateResult.toString()).thenReturn(toStringResult13);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate16() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		long getTimeResult11 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult11);

		String toStringResult14 = ""; // UTA: default value
		when(getExpectedProcessingDateResult.toString()).thenReturn(toStringResult14);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate17() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		long getTimeResult12 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult12);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		String toStringResult16 = ""; // UTA: default value
		when(getFederalTaxVoResult.toString()).thenReturn(toStringResult16);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate18() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		boolean afterResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.after(any(Date.class))).thenReturn(afterResult6);

		boolean beforeResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.before(any(Date.class))).thenReturn(beforeResult6);

		long getTimeResult13 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult13);

		String toStringResult17 = ""; // UTA: default value
		when(getFinalContributionDateResult.toString()).thenReturn(toStringResult17);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp8() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		long getTimeResult14 = 0L; // UTA: default value
		when(getLastUpdatedResult3.getTime()).thenReturn(getTimeResult14);

		String toStringResult18 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult18);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp9() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		long getTimeResult15 = 0L; // UTA: default value
		when(getCreatedResult4.getTime()).thenReturn(getTimeResult15);

		String toStringResult19 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult19);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp10() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		long getTimeResult16 = 0L; // UTA: default value
		when(getLastUpdatedResult4.getTime()).thenReturn(getTimeResult16);

		String toStringResult20 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult20);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp9();
		when(getLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult4);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		Timestamp getLastUpdatedResult4 = mockTimestamp10();
		when(getLegaleseInfoResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String getLegaleseTextResult = ""; // UTA: default value
		when(getLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult);

		String toStringResult21 = ""; // UTA: default value
		when(getLegaleseInfoResult.toString()).thenReturn(toStringResult21);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate19() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		boolean afterResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.after(any(Date.class))).thenReturn(afterResult7);

		boolean beforeResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.before(any(Date.class))).thenReturn(beforeResult7);

		long getTimeResult17 = 0L; // UTA: default value
		when(getMostRecentPriorContributionDateResult.getTime()).thenReturn(getTimeResult17);

		String toStringResult22 = ""; // UTA: default value
		when(getMostRecentPriorContributionDateResult.toString()).thenReturn(toStringResult22);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo3() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		Boolean getParticipantHasPbaMoneyResult = false; // UTA: default value
		when(getParticipantInfoResult.getParticipantHasPbaMoney()).thenReturn(getParticipantHasPbaMoneyResult);

		Boolean getParticipantHasRothMoneyResult = false; // UTA: default value
		when(getParticipantInfoResult.getParticipantHasRothMoney()).thenReturn(getParticipantHasRothMoneyResult);

		boolean isParticipantStatusTotalResult = false; // UTA: default value
		when(getParticipantInfoResult.isParticipantStatusTotal()).thenReturn(isParticipantStatusTotalResult);

		String toStringResult23 = ""; // UTA: default value
		when(getParticipantInfoResult.toString()).thenReturn(toStringResult23);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp11() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		long getTimeResult18 = 0L; // UTA: default value
		when(getCreatedResult5.getTime()).thenReturn(getTimeResult18);

		String toStringResult24 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult24);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp12() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		long getTimeResult19 = 0L; // UTA: default value
		when(getLastUpdatedResult5.getTime()).thenReturn(getTimeResult19);

		String toStringResult25 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult25);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo2() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp11();
		when(getParticipantLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult5);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		Timestamp getLastUpdatedResult5 = mockTimestamp12();
		when(getParticipantLegaleseInfoResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		String getLegaleseTextResult2 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult2);

		String toStringResult26 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.toString()).thenReturn(toStringResult26);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		String toStringResult27 = ""; // UTA: default value
		when(getPrincipalResult.toString()).thenReturn(toStringResult27);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate20() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult8 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult8);

		boolean beforeResult8 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult8);

		long getTimeResult20 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult20);

		String toStringResult28 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult28);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate21() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean afterResult9 = false; // UTA: default value
		when(getRetirementDateResult.after(any(Date.class))).thenReturn(afterResult9);

		boolean beforeResult9 = false; // UTA: default value
		when(getRetirementDateResult.before(any(Date.class))).thenReturn(beforeResult9);

		long getTimeResult21 = 0L; // UTA: default value
		when(getRetirementDateResult.getTime()).thenReturn(getTimeResult21);

		String toStringResult29 = ""; // UTA: default value
		when(getRetirementDateResult.toString()).thenReturn(toStringResult29);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp2();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate13();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);

		Timestamp getCreatedResult = mockTimestamp3();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote2();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate14();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate15();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mockDate16();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate17();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate18();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHaveStep1DriverFieldsChangedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getHaveStep1DriverFieldsChanged())
				.thenReturn(getHaveStep1DriverFieldsChangedResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		Boolean getIsLegaleseConfirmedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsLegaleseConfirmed()).thenReturn(getIsLegaleseConfirmedResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		boolean getIsPostDraftResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsPostDraft()).thenReturn(getIsPostDraftResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult3 = mockTimestamp8();
		when(getWithdrawalRequestResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo();
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate19();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Collection<Note> getNotesResult = new ArrayList<Note>(); // UTA: default value
		doReturn(getNotesResult).when(getWithdrawalRequestResult).getNotes();

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo3();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo2();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mockPrincipal();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		Collection<WithdrawalRequestNote> getReadOnlyAdminToAdminNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToAdminNotesResult).when(getWithdrawalRequestResult).getReadOnlyAdminToAdminNotes();

		Collection<WithdrawalRequestNote> getReadOnlyAdminToParticipantNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToParticipantNotesResult).when(getWithdrawalRequestResult)
				.getReadOnlyAdminToParticipantNotes();

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate20();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate21();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getRobustDateChangedAfterVestingResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRobustDateChangedAfterVesting())
				.thenReturn(getRobustDateChangedAfterVestingResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);

		BigDecimal getTotalBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getTotalBalance()).thenReturn(getTotalBalanceResult);

		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getTotalRequestedWithdrawalAmount())
				.thenReturn(getTotalRequestedWithdrawalAmountResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isAtRiskDeclarationPermittedForUserResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isAtRiskDeclarationPermittedForUser())
				.thenReturn(isAtRiskDeclarationPermittedForUserResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult30 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult30);
		return getWithdrawalRequestResult;
	}
	private static WithdrawalRequest mockWithdrawalRequest3_1() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);
		
		Timestamp getApprovedTimestampResult = mockTimestamp2();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);
		
		Date getBirthDateResult = mockDate13();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);
		
		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);
		
		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);
		
		ContractInfo getContractInfoResult = mockContractInfo();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);
		
		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);
		
		Timestamp getCreatedResult = mockTimestamp3();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);
		
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);
		
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote2();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
		.thenReturn(getCurrentAdminToParticipantNoteResult);
		
		Date getDeathDateResult = mockDate14();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);
		
		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();
		
		Date getDisabilityDateResult = mockDate15();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);
		
		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);
		
		Date getExpectedProcessingDateResult = mockDate16();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);
		
		Date getExpirationDateResult = mockDate17();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);
		
		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);
		
		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();
		
		Date getFinalContributionDateResult = mockDate18();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);
		
		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);
		
		boolean getHaveStep1DriverFieldsChangedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getHaveStep1DriverFieldsChanged())
		.thenReturn(getHaveStep1DriverFieldsChangedResult);
		
		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);
		
		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
		.thenReturn(getIrsDistributionCodeLoanClosureResult);
		
		Boolean getIsLegaleseConfirmedResult = true; // UTA: default value
		when(getWithdrawalRequestResult.getIsLegaleseConfirmed()).thenReturn(getIsLegaleseConfirmedResult);
		
		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);
		
		boolean getIsPostDraftResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsPostDraft()).thenReturn(getIsPostDraftResult);
		
		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);
		
		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);
		
		Timestamp getLastUpdatedResult3 = mockTimestamp8();
		when(getWithdrawalRequestResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);
		
		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo();
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);
		
		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);
		
		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);
		
		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);
		
		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();
		
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();
		
		Date getMostRecentPriorContributionDateResult = mockDate19();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
		.thenReturn(getMostRecentPriorContributionDateResult);
		
		Collection<Note> getNotesResult = new ArrayList<Note>(); // UTA: default value
		doReturn(getNotesResult).when(getWithdrawalRequestResult).getNotes();
		
		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);
		
		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);
		
		ParticipantInfo getParticipantInfoResult = mockParticipantInfo3();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		
		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);
		
		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo2();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);
		
		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);
		
		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
		.thenReturn(getParticipantStateOfResidenceResult);
		
		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);
		
		Principal getPrincipalResult = mockPrincipal();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);
		
		Collection<WithdrawalRequestNote> getReadOnlyAdminToAdminNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToAdminNotesResult).when(getWithdrawalRequestResult).getReadOnlyAdminToAdminNotes();
		
		Collection<WithdrawalRequestNote> getReadOnlyAdminToParticipantNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToParticipantNotesResult).when(getWithdrawalRequestResult)
		.getReadOnlyAdminToParticipantNotes();
		
		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);
		
		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);
		
		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);
		
		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();
		
		Date getRequestDateResult = mockDate20();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);
		
		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);
		
		Date getRetirementDateResult = mockDate21();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);
		
		boolean getRobustDateChangedAfterVestingResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRobustDateChangedAfterVesting())
		.thenReturn(getRobustDateChangedAfterVestingResult);
		
		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);
		
		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
		.thenReturn(getShowOptionForUnvestedAmountResult);
		
		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);
		
		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);
		
		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);
		
		BigDecimal getTotalBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getTotalBalance()).thenReturn(getTotalBalanceResult);
		
		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getTotalRequestedWithdrawalAmount())
		.thenReturn(getTotalRequestedWithdrawalAmountResult);
		
		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);
		
		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);
		
		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);
		
		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);
		
		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);
		
		boolean isAtRiskDeclarationPermittedForUserResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isAtRiskDeclarationPermittedForUser())
		.thenReturn(isAtRiskDeclarationPermittedForUserResult);
		
		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);
		
		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);
		
		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);
		
		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isValidToProcess()).thenReturn(isValidToProcessResult);
		
		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);
		
		String toStringResult30 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult30);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal2() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest3();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = false; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}
	private static Withdrawal mockWithdrawal2_1() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);
		
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest3_1();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		
		boolean isReadyForDoSaveResult = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#delete(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		Object[] params = new Object[1];
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(params);
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal3();
		underTest.delete(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp13() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp14() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getApprovedTimestampResult.toString()).thenReturn(toStringResult2);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate22() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		boolean beforeResult = false; // UTA: default value
		when(getBirthDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult3 = ""; // UTA: default value
		when(getBirthDateResult.toString()).thenReturn(toStringResult3);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo2() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);

		String toStringResult4 = ""; // UTA: default value
		when(getContractInfoResult.toString()).thenReturn(toStringResult4);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp15() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult5);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp16() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult6);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote3() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult2 = mockTimestamp16();
		when(getCurrentAdminToAdminNoteResult.getCreated()).thenReturn(getCreatedResult2);

		Collection<WithdrawalMessage> getErrorCodesResult = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult).when(getCurrentAdminToAdminNoteResult).getErrorCodes();

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult7 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.toString()).thenReturn(toStringResult7);
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp17() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult8);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote4() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult3 = mockTimestamp17();
		when(getCurrentAdminToParticipantNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Collection<WithdrawalMessage> getErrorCodesResult2 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult2).when(getCurrentAdminToParticipantNoteResult).getErrorCodes();

		Integer getLastUpdatedByIdResult2 = 0; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult2);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		Integer getSubmissionIdResult2 = 0; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getSubmissionId()).thenReturn(getSubmissionIdResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.toString()).thenReturn(toStringResult9);
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate23() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		boolean beforeResult2 = false; // UTA: default value
		when(getDeathDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getDeathDateResult.toString()).thenReturn(toStringResult10);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate24() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		boolean beforeResult3 = false; // UTA: default value
		when(getDisabilityDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult11 = ""; // UTA: default value
		when(getDisabilityDateResult.toString()).thenReturn(toStringResult11);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate25() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		boolean beforeResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult12 = ""; // UTA: default value
		when(getExpectedProcessingDateResult.toString()).thenReturn(toStringResult12);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate26() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean beforeResult5 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult13 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult13);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO2() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		String toStringResult14 = ""; // UTA: default value
		when(getFederalTaxVoResult.toString()).thenReturn(toStringResult14);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate27() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		boolean beforeResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.before(any(Date.class))).thenReturn(beforeResult6);

		long getTimeResult6 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult15 = ""; // UTA: default value
		when(getFinalContributionDateResult.toString()).thenReturn(toStringResult15);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp18() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult16);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo3() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp18();
		when(getLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult4);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		Collection<WithdrawalMessage> getErrorCodesResult4 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult4).when(getLegaleseInfoResult).getErrorCodes();

		Integer getLastUpdatedByIdResult4 = 0; // UTA: default value
		when(getLegaleseInfoResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult4);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);

		String toStringResult17 = ""; // UTA: default value
		when(getLegaleseInfoResult.toString()).thenReturn(toStringResult17);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate28() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		boolean beforeResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.before(any(Date.class))).thenReturn(beforeResult7);

		long getTimeResult7 = 0L; // UTA: default value
		when(getMostRecentPriorContributionDateResult.getTime()).thenReturn(getTimeResult7);

		String toStringResult18 = ""; // UTA: default value
		when(getMostRecentPriorContributionDateResult.toString()).thenReturn(toStringResult18);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp19() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult19);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo4() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp19();
		when(getParticipantLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult5);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		Collection<WithdrawalMessage> getErrorCodesResult5 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult5).when(getParticipantLegaleseInfoResult).getErrorCodes();

		Integer getLastUpdatedByIdResult5 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult5);

		Integer getSubmissionIdResult4 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult4);

		String toStringResult20 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.toString()).thenReturn(toStringResult20);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal2() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		String toStringResult21 = ""; // UTA: default value
		when(getPrincipalResult.toString()).thenReturn(toStringResult21);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate29() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean beforeResult8 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult8);

		long getTimeResult8 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult8);

		String toStringResult22 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult22);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate30() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean beforeResult9 = false; // UTA: default value
		when(getRetirementDateResult.before(any(Date.class))).thenReturn(beforeResult9);

		long getTimeResult9 = 0L; // UTA: default value
		when(getRetirementDateResult.getTime()).thenReturn(getTimeResult9);

		String toStringResult23 = ""; // UTA: default value
		when(getRetirementDateResult.toString()).thenReturn(toStringResult23);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp20() throws Throwable {
		Timestamp getSubmissionCaseLastUpdatedResult = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getSubmissionCaseLastUpdatedResult.toString()).thenReturn(toStringResult24);
		return getSubmissionCaseLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate31() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean beforeResult10 = false; // UTA: default value
		when(getTerminationDateResult.before(any(Date.class))).thenReturn(beforeResult10);

		long getTimeResult10 = 0L; // UTA: default value
		when(getTerminationDateResult.getTime()).thenReturn(getTimeResult10);

		String toStringResult25 = ""; // UTA: default value
		when(getTerminationDateResult.toString()).thenReturn(toStringResult25);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp14();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate22();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo2();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		Timestamp getCreatedResult = mockTimestamp15();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote3();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote4();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate23();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate24();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Collection<WithdrawalMessage> getErrorCodesResult3 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult3).when(getWithdrawalRequestResult).getErrorCodes();

		Date getExpectedProcessingDateResult = mockDate25();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate26();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO2();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate27();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHasBeenPersistedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Integer getLastUpdatedByIdResult3 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult3);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo3();
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate28();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo4();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mockPrincipal2();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate29();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate30();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);

		Timestamp getSubmissionCaseLastUpdatedResult = mockTimestamp20();
		when(getWithdrawalRequestResult.getSubmissionCaseLastUpdated()).thenReturn(getSubmissionCaseLastUpdatedResult);

		Integer getSubmissionIdResult5 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult5);

		Date getTerminationDateResult = mockDate31();
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult26 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult26);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal3() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp13();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest4();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalStateEnum()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#getWithdrawalStateEnum()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalStateEnum() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		WithdrawalStateEnum result = underTest.getWithdrawalStateEnum();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for readActivityHistory(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#readActivityHistory(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testReadActivityHistory() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		ActivityHistory result = underTest.readActivityHistory(withdrawal);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for save(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#save(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testSave() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal4();
		underTest.save(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal4() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		boolean isReadyForDoSaveResult = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#sendForApproval(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testSendForApproval() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class);
		WithdrawalLoggingHelper.log(nullable(WithdrawalRequest.class), nullable(WithdrawalEvent.class),
				nullable(Class.class), nullable(String.class));

		spy(ActivityHistoryHelper.class);

		PowerMockito.doNothing().when(ActivityHistoryHelper.class);
		ActivityHistoryHelper.saveOriginalValues(nullable(Withdrawal.class));

		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal5();
		underTest.sendForApproval(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp21() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp22() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getApprovedTimestampResult.toString()).thenReturn(toStringResult2);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate32() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getBirthDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getBirthDateResult.before(any(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult3 = ""; // UTA: default value
		when(getBirthDateResult.toString()).thenReturn(toStringResult3);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo3() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		String getClientAccountRepEmailResult = ""; // UTA: default value
		when(getContractInfoResult.getClientAccountRepEmail()).thenReturn(getClientAccountRepEmailResult);

		Boolean getHasApprovePermissionResult = false; // UTA: default value
		when(getContractInfoResult.getHasApprovePermission()).thenReturn(getHasApprovePermissionResult);

		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);

		Boolean getTwoStepApprovalRequiredResult = true; // UTA: default value
		when(getContractInfoResult.getTwoStepApprovalRequired()).thenReturn(getTwoStepApprovalRequiredResult);

		boolean isBundledGaIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.isBundledGaIndicator()).thenReturn(isBundledGaIndicatorResult);

		String toStringResult4 = ""; // UTA: default value
		when(getContractInfoResult.toString()).thenReturn(toStringResult4);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractPermission
	 */
	private static ContractPermission mockContractPermission() throws Throwable {
		ContractPermission getContractPermissionResult = mock(ContractPermission.class);
		boolean isSigningAuthorityResult = false; // UTA: default value
		when(getContractPermissionResult.isSigningAuthority()).thenReturn(isSigningAuthorityResult);

		String toStringResult5 = ""; // UTA: default value
		when(getContractPermissionResult.toString()).thenReturn(toStringResult5);
		return getContractPermissionResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp23() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult6);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp24() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult7 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult7);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp25() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult8);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote5() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult);

		boolean doWarningCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult);

		Timestamp getCreatedResult2 = mockTimestamp24();
		when(getCurrentAdminToAdminNoteResult.getCreated()).thenReturn(getCreatedResult2);

		Timestamp getLastUpdatedResult = mockTimestamp25();
		when(getCurrentAdminToAdminNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.toString()).thenReturn(toStringResult9);
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp26() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult10 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult10);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp27() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult11);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote6() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult2);

		boolean doWarningCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult2);

		Timestamp getCreatedResult3 = mockTimestamp26();
		when(getCurrentAdminToParticipantNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp27();
		when(getCurrentAdminToParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult12 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.toString()).thenReturn(toStringResult12);
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate33() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getDeathDateResult.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getDeathDateResult.before(any(Date.class))).thenReturn(beforeResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getDeathDateResult.toString()).thenReturn(toStringResult13);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate34() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getDisabilityDateResult.after(any(Date.class))).thenReturn(afterResult3);

		boolean beforeResult3 = false; // UTA: default value
		when(getDisabilityDateResult.before(any(Date.class))).thenReturn(beforeResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult14 = ""; // UTA: default value
		when(getDisabilityDateResult.toString()).thenReturn(toStringResult14);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate35() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.after(any(Date.class))).thenReturn(afterResult4);

		boolean beforeResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.before(any(Date.class))).thenReturn(beforeResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult15 = ""; // UTA: default value
		when(getExpectedProcessingDateResult.toString()).thenReturn(toStringResult15);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate36() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult5);

		boolean beforeResult5 = false; // UTA: default value
		when(getExpirationDateResult.before(any(Date.class))).thenReturn(beforeResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult16 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult16);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO3() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		String toStringResult17 = ""; // UTA: default value
		when(getFederalTaxVoResult.toString()).thenReturn(toStringResult17);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate37() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		boolean afterResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.after(any(Date.class))).thenReturn(afterResult6);

		boolean beforeResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.before(any(Date.class))).thenReturn(beforeResult6);

		long getTimeResult6 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult18 = ""; // UTA: default value
		when(getFinalContributionDateResult.toString()).thenReturn(toStringResult18);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp28() throws Throwable {
		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getLastUpdatedResult3.toString()).thenReturn(toStringResult19);
		return getLastUpdatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp29() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult20 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult20);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp30() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult21 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult21);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo5() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp29();
		when(getLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult4);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		Timestamp getLastUpdatedResult4 = mockTimestamp30();
		when(getLegaleseInfoResult.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		String toStringResult22 = ""; // UTA: default value
		when(getLegaleseInfoResult.toString()).thenReturn(toStringResult22);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate38() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		boolean afterResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.after(any(Date.class))).thenReturn(afterResult7);

		boolean beforeResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.before(any(Date.class))).thenReturn(beforeResult7);

		long getTimeResult7 = 0L; // UTA: default value
		when(getMostRecentPriorContributionDateResult.getTime()).thenReturn(getTimeResult7);

		String toStringResult23 = ""; // UTA: default value
		when(getMostRecentPriorContributionDateResult.toString()).thenReturn(toStringResult23);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo4() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getManulifeCompanyIdResult = ""; // UTA: default value
		when(getParticipantInfoResult.getManulifeCompanyId()).thenReturn(getManulifeCompanyIdResult);

		Boolean getParticipantHasPbaMoneyResult = false; // UTA: default value
		when(getParticipantInfoResult.getParticipantHasPbaMoney()).thenReturn(getParticipantHasPbaMoneyResult);

		Boolean getParticipantHasRothMoneyResult = false; // UTA: default value
		when(getParticipantInfoResult.getParticipantHasRothMoney()).thenReturn(getParticipantHasRothMoneyResult);

		boolean isParticipantStatusTotalResult = false; // UTA: default value
		when(getParticipantInfoResult.isParticipantStatusTotal()).thenReturn(isParticipantStatusTotalResult);

		String toStringResult24 = ""; // UTA: default value
		when(getParticipantInfoResult.toString()).thenReturn(toStringResult24);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp31() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult25 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult25);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp32() throws Throwable {
		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		String toStringResult26 = ""; // UTA: default value
		when(getLastUpdatedResult5.toString()).thenReturn(toStringResult26);
		return getLastUpdatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo6() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp31();
		when(getParticipantLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult5);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		Timestamp getLastUpdatedResult5 = mockTimestamp32();
		when(getParticipantLegaleseInfoResult.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		String toStringResult27 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.toString()).thenReturn(toStringResult27);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal3() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		String toStringResult28 = ""; // UTA: default value
		when(getPrincipalResult.toString()).thenReturn(toStringResult28);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate39() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		boolean afterResult8 = false; // UTA: default value
		when(getRequestDateResult.after(any(Date.class))).thenReturn(afterResult8);

		boolean beforeResult8 = false; // UTA: default value
		when(getRequestDateResult.before(any(Date.class))).thenReturn(beforeResult8);

		long getTimeResult8 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult8);

		String toStringResult29 = ""; // UTA: default value
		when(getRequestDateResult.toString()).thenReturn(toStringResult29);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate40() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		boolean afterResult9 = false; // UTA: default value
		when(getRetirementDateResult.after(any(Date.class))).thenReturn(afterResult9);

		boolean beforeResult9 = false; // UTA: default value
		when(getRetirementDateResult.before(any(Date.class))).thenReturn(beforeResult9);

		long getTimeResult9 = 0L; // UTA: default value
		when(getRetirementDateResult.getTime()).thenReturn(getTimeResult9);

		String toStringResult30 = ""; // UTA: default value
		when(getRetirementDateResult.toString()).thenReturn(toStringResult30);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate41() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		boolean afterResult10 = false; // UTA: default value
		when(getTerminationDateResult.after(any(Date.class))).thenReturn(afterResult10);

		boolean beforeResult10 = false; // UTA: default value
		when(getTerminationDateResult.before(any(Date.class))).thenReturn(beforeResult10);

		long getTimeResult10 = 0L; // UTA: default value
		when(getTerminationDateResult.getTime()).thenReturn(getTimeResult10);

		String toStringResult31 = ""; // UTA: default value
		when(getTerminationDateResult.toString()).thenReturn(toStringResult31);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest5() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp22();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate32();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo3();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		ContractPermission getContractPermissionResult = mockContractPermission();
		when(getWithdrawalRequestResult.getContractPermission()).thenReturn(getContractPermissionResult);

		Timestamp getCreatedResult = mockTimestamp23();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote5();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote6();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate33();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate34();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mockDate35();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate36();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO3();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate37();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHaveStep1DriverFieldsChangedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getHaveStep1DriverFieldsChanged())
				.thenReturn(getHaveStep1DriverFieldsChangedResult);

		Boolean getIgnoreErrorsResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIgnoreErrors()).thenReturn(getIgnoreErrorsResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		boolean getIsPostDraftResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsPostDraft()).thenReturn(getIsPostDraftResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult3 = mockTimestamp28();
		when(getWithdrawalRequestResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo5();
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate38();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo4();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo6();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mockPrincipal3();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate39();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		String getRequestTypeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getRequestType()).thenReturn(getRequestTypeResult);

		Date getRetirementDateResult = mockDate40();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mockDate41();
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isAtRiskDeclarationPermittedForUserResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isAtRiskDeclarationPermittedForUser())
				.thenReturn(isAtRiskDeclarationPermittedForUserResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult32 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult32);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal5() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp21();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest5();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForReview(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#sendForReview(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testSendForReview() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class);
		WithdrawalLoggingHelper.log(nullable(WithdrawalRequest.class), nullable(WithdrawalEvent.class),
				nullable(Class.class), nullable(String.class));
 
		spy(ActivityHistoryHelper.class);

		PowerMockito.doNothing().when(ActivityHistoryHelper.class);
		ActivityHistoryHelper.saveOriginalValues(nullable(Withdrawal.class));

		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal6();
		underTest.sendForReview(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp33() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp34() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getApprovedTimestampResult.toString()).thenReturn(toStringResult2);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate42() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getBirthDateResult.after(any(Date.class))).thenReturn(afterResult);

		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult3 = ""; // UTA: default value
		when(getBirthDateResult.toString()).thenReturn(toStringResult3);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo4() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		String getClientAccountRepEmailResult = ""; // UTA: default value
		when(getContractInfoResult.getClientAccountRepEmail()).thenReturn(getClientAccountRepEmailResult);

		Boolean getHasApprovePermissionResult = false; // UTA: default value
		when(getContractInfoResult.getHasApprovePermission()).thenReturn(getHasApprovePermissionResult);

		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);

		Boolean getTwoStepApprovalRequiredResult = false; // UTA: default value
		when(getContractInfoResult.getTwoStepApprovalRequired()).thenReturn(getTwoStepApprovalRequiredResult);

		boolean isBundledGaIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.isBundledGaIndicator()).thenReturn(isBundledGaIndicatorResult);

		String toStringResult4 = ""; // UTA: default value
		when(getContractInfoResult.toString()).thenReturn(toStringResult4);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractPermission
	 */
	private static ContractPermission mockContractPermission2() throws Throwable {
		ContractPermission getContractPermissionResult = mock(ContractPermission.class);
		boolean isSigningAuthorityResult = false; // UTA: default value
		when(getContractPermissionResult.isSigningAuthority()).thenReturn(isSigningAuthorityResult);

		String toStringResult5 = ""; // UTA: default value
		when(getContractPermissionResult.toString()).thenReturn(toStringResult5);
		return getContractPermissionResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp35() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult6);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp36() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult7 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult7);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp37() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getLastUpdatedResult.toString()).thenReturn(toStringResult8);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote7() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult);

		boolean doWarningCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult);

		Timestamp getCreatedResult2 = mockTimestamp36();
		when(getCurrentAdminToAdminNoteResult.getCreated()).thenReturn(getCreatedResult2);

		Timestamp getLastUpdatedResult = mockTimestamp37();
		when(getCurrentAdminToAdminNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.isBlank()).thenReturn(isBlankResult);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.toString()).thenReturn(toStringResult9);
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp38() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult10 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult10);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp39() throws Throwable {
		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		String toStringResult11 = ""; // UTA: default value
		when(getLastUpdatedResult2.toString()).thenReturn(toStringResult11);
		return getLastUpdatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote8() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doErrorCodesExist()).thenReturn(doErrorCodesExistResult2);

		boolean doWarningCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.doWarningCodesExist()).thenReturn(doWarningCodesExistResult2);

		Timestamp getCreatedResult3 = mockTimestamp38();
		when(getCurrentAdminToParticipantNoteResult.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mockTimestamp39();
		when(getCurrentAdminToParticipantNoteResult.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);

		String toStringResult12 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.toString()).thenReturn(toStringResult12);
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate43() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getDeathDateResult.after(any(Date.class))).thenReturn(afterResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult13 = ""; // UTA: default value
		when(getDeathDateResult.toString()).thenReturn(toStringResult13);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate44() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getDisabilityDateResult.after(any(Date.class))).thenReturn(afterResult3);

		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult3);

		String toStringResult14 = ""; // UTA: default value
		when(getDisabilityDateResult.toString()).thenReturn(toStringResult14);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate45() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult.after(any(Date.class))).thenReturn(afterResult4);

		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult4);

		String toStringResult15 = ""; // UTA: default value
		when(getExpectedProcessingDateResult.toString()).thenReturn(toStringResult15);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate46() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getExpirationDateResult.after(any(Date.class))).thenReturn(afterResult5);

		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult5);

		String toStringResult16 = ""; // UTA: default value
		when(getExpirationDateResult.toString()).thenReturn(toStringResult16);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO4() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		String toStringResult17 = ""; // UTA: default value
		when(getFederalTaxVoResult.toString()).thenReturn(toStringResult17);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate47() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		boolean afterResult6 = false; // UTA: default value
		when(getFinalContributionDateResult.after(any(Date.class))).thenReturn(afterResult6);

		long getTimeResult6 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult6);

		String toStringResult18 = ""; // UTA: default value
		when(getFinalContributionDateResult.toString()).thenReturn(toStringResult18);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest6() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp34();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate42();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo4();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		ContractPermission getContractPermissionResult = mockContractPermission2();
		when(getWithdrawalRequestResult.getContractPermission()).thenReturn(getContractPermissionResult);

		Timestamp getCreatedResult = mockTimestamp35();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote7();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote8();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate43();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate44();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mockDate45();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate46();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO4();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate47();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		Boolean getHasPCDataResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getHasPCData()).thenReturn(getHasPCDataResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		when(getWithdrawalRequestResult.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Collection<Note> getNotesResult = new ArrayList<Note>(); // UTA: default value
		doReturn(getNotesResult).when(getWithdrawalRequestResult).getNotes();

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		String getRequestTypeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getRequestType()).thenReturn(getRequestTypeResult);

		Date getRetirementDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isAtRiskDeclarationPermittedForUserResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isAtRiskDeclarationPermittedForUser())
				.thenReturn(isAtRiskDeclarationPermittedForUserResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult19 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult19);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal6() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp33();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest6();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for transitionToState(Withdrawal, WithdrawalStateEnum)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DraftState#transitionToState(Withdrawal, WithdrawalStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testTransitionToState() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalStateEnum newState = WithdrawalStateEnum.DRAFT; // UTA: default value
		underTest.transitionToState(withdrawal, newState);

	}

	/**
	 * Parasoft Jtest UTA: Test for cancel(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#cancel(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testCancel() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.cancel(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for expire(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#expire(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testExpire() throws Throwable {
		Object[] params = new Object[1];
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(params);

		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal7();
		underTest.expire(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp40() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp41() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate48() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo5() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp42() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp43() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote9() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult2 = mockTimestamp43();
		when(getCurrentAdminToAdminNoteResult.getCreated()).thenReturn(getCreatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult.isBlank()).thenReturn(isBlankResult);
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp44() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote10() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult3 = mockTimestamp44();
		when(getCurrentAdminToParticipantNoteResult.getCreated()).thenReturn(getCreatedResult3);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult.isBlank()).thenReturn(isBlankResult2);
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate49() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate50() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate51() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate52() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO5() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate53() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp45() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo7() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp45();
		when(getLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult4);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate54() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp46() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo8() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp46();
		when(getParticipantLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult5);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal4() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate55() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate56() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate57() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest7() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp41();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate48();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo5();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		Timestamp getCreatedResult = mockTimestamp42();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote9();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote10();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate49();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate50();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mockDate51();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate52();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO5();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate53();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo7();
		when(getWithdrawalRequestResult.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate54();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo8();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mockPrincipal4();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate55();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate56();
		when(getWithdrawalRequestResult.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mockDate57();
		when(getWithdrawalRequestResult.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal7() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp40();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest7();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for getSystemOfRecordValues(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#getSystemOfRecordValues(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testGetSystemOfRecordValues() throws Throwable {

		spy(VestingServiceDelegate.class);

		VestingServiceDelegate getInstanceResult2 = mock(VestingServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult2).when(VestingServiceDelegate.class, "getInstance", anyString());

		BaseEnvironment newBaseEnvironmentResult = mock(BaseEnvironment.class); // UTA: default value
		whenNew(BaseEnvironment.class).withAnyArguments().thenReturn(newBaseEnvironmentResult);

		spy(EmployeeServiceDelegate.class);

		Employee val = new Employee();

		EmployeeServiceDelegate getInstanceResult = mock(EmployeeServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EmployeeServiceDelegate.class, "getInstance", anyString());
		when(getInstanceResult.getEmployeeByProfileId(anyLong(), anyInt(), any(Date.class))).thenReturn(val);
		spy(ActivityHistoryHelper.class);

		SystemOfRecordValues getSystemOfRecordValuesResult = mock(SystemOfRecordValues.class);
		doReturn(getSystemOfRecordValuesResult).when(ActivityHistoryHelper.class);
		ActivityHistoryHelper.getSystemOfRecordValues(nullable(Withdrawal.class));

		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mockWithdrawal8();
		SystemOfRecordValues result = underTest.getSystemOfRecordValues(withdrawal);

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate58() throws Throwable {
		Date getVestingEventDateResult = mock(Date.class);
		String toStringResult = ""; // UTA: default value
		when(getVestingEventDateResult.toString()).thenReturn(toStringResult);
		return getVestingEventDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest8() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		Date getVestingEventDateResult = mockDate58();
		when(getWithdrawalRequestResult.getVestingEventDate()).thenReturn(getVestingEventDateResult);

		String toStringResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult.toString()).thenReturn(toStringResult2);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal8() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest8();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for updateMoneyTypeDataFromSource(WithdrawalRequestMoneyType, WithdrawalRequestMoneyType)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#updateMoneyTypeDataFromSource(WithdrawalRequestMoneyType, WithdrawalRequestMoneyType)
	 * @author patelpo
	 */
	@Test
	public void testUpdateMoneyTypeDataFromSource() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mockWithdrawalRequestMoneyType();
		WithdrawalRequestMoneyType defaultDataMoneyType = mockWithdrawalRequestMoneyType2();
		underTest.updateMoneyTypeDataFromSource(withdrawalRequestMoneyType, defaultDataMoneyType);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMoneyType
	 */
	private static WithdrawalRequestMoneyType mockWithdrawalRequestMoneyType() throws Throwable {
		WithdrawalRequestMoneyType withdrawalRequestMoneyType = mock(WithdrawalRequestMoneyType.class);
		Boolean getIsPre1987MoneyTypeResult = false; // UTA: default value
		when(withdrawalRequestMoneyType.getIsPre1987MoneyType()).thenReturn(getIsPre1987MoneyTypeResult);

		Boolean getIsRolloverMoneyTypeResult = false; // UTA: default value
		when(withdrawalRequestMoneyType.getIsRolloverMoneyType()).thenReturn(getIsRolloverMoneyTypeResult);

		Boolean getIsVoluntaryContributionMoneyTypeResult = false; // UTA: default value
		when(withdrawalRequestMoneyType.getIsVoluntaryContributionMoneyType())
				.thenReturn(getIsVoluntaryContributionMoneyTypeResult);

		String getMoneyTypeAliasIdResult = ""; // UTA: default value
		when(withdrawalRequestMoneyType.getMoneyTypeAliasId()).thenReturn(getMoneyTypeAliasIdResult);

		String getMoneyTypeCategoryCodeResult = ""; // UTA: default value
		when(withdrawalRequestMoneyType.getMoneyTypeCategoryCode()).thenReturn(getMoneyTypeCategoryCodeResult);

		String getMoneyTypeNameResult = ""; // UTA: default value
		when(withdrawalRequestMoneyType.getMoneyTypeName()).thenReturn(getMoneyTypeNameResult);

		BigDecimal getTotalBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(withdrawalRequestMoneyType.getTotalBalance()).thenReturn(getTotalBalanceResult);
		return withdrawalRequestMoneyType;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestMoneyType
	 */
	private static WithdrawalRequestMoneyType mockWithdrawalRequestMoneyType2() throws Throwable {
		WithdrawalRequestMoneyType defaultDataMoneyType = mock(WithdrawalRequestMoneyType.class);
		Boolean getIsPre1987MoneyTypeResult2 = false; // UTA: default value
		when(defaultDataMoneyType.getIsPre1987MoneyType()).thenReturn(getIsPre1987MoneyTypeResult2);

		Boolean getIsRolloverMoneyTypeResult2 = false; // UTA: default value
		when(defaultDataMoneyType.getIsRolloverMoneyType()).thenReturn(getIsRolloverMoneyTypeResult2);

		Boolean getIsVoluntaryContributionMoneyTypeResult2 = false; // UTA: default value
		when(defaultDataMoneyType.getIsVoluntaryContributionMoneyType())
				.thenReturn(getIsVoluntaryContributionMoneyTypeResult2);

		String getMoneyTypeAliasIdResult2 = ""; // UTA: default value
		when(defaultDataMoneyType.getMoneyTypeAliasId()).thenReturn(getMoneyTypeAliasIdResult2);

		String getMoneyTypeCategoryCodeResult2 = ""; // UTA: default value
		when(defaultDataMoneyType.getMoneyTypeCategoryCode()).thenReturn(getMoneyTypeCategoryCodeResult2);

		String getMoneyTypeNameResult2 = ""; // UTA: default value
		when(defaultDataMoneyType.getMoneyTypeName()).thenReturn(getMoneyTypeNameResult2);

		BigDecimal getTotalBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(defaultDataMoneyType.getTotalBalance()).thenReturn(getTotalBalanceResult2);
		return defaultDataMoneyType;
	}

	/**
	 * Parasoft Jtest UTA: Test for updateMoneyTypeNames(WithdrawalRequest, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#updateMoneyTypeNames(WithdrawalRequest, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testUpdateMoneyTypeNames() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest9();
		WithdrawalRequest defaultVo = mockWithdrawalRequest10();
		underTest.updateMoneyTypeNames(withdrawalRequest, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo5() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult).when(getParticipantInfoResult).getMoneyTypeAliases();
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest9() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo5();
		when(withdrawalRequest.getParticipantInfo()).thenReturn(getParticipantInfoResult);
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo6() throws Throwable {
		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult2 = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult2).when(getParticipantInfoResult2).getMoneyTypeAliases();
		return getParticipantInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest10() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo6();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for updateMoneyTypesWithDefaultData(WithdrawalRequest, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.BeforeEndState#updateMoneyTypesWithDefaultData(WithdrawalRequest, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testUpdateMoneyTypesWithDefaultData() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		WithdrawalRequest withdrawalRequest = mockWithdrawalRequest11();
		WithdrawalRequest defaultVo = mockWithdrawalRequest12();
		underTest.updateMoneyTypesWithDefaultData(withdrawalRequest, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest11() throws Throwable {
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(withdrawalRequest).getMoneyTypes();
		return withdrawalRequest;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest12() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testApplyDefaultDataForView() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.applyDefaultDataForView(withdrawal, withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for clone()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#clone()
	 * @author patelpo
	 */
	@Test(expected = CloneNotSupportedException.class)
	public void testClone() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Object result = underTest.clone();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for deny(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#deny(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testDeny() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.deny(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for processApproved(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#processApproved(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testProcessApproved() throws Throwable {
		// Given
		DraftState underTest = new DraftState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.processApproved(withdrawal);

	}
}