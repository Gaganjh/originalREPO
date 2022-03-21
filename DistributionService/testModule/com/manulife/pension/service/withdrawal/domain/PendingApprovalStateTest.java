/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for PendingApprovalState
 *
 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState
 * @author patelpo
 */
@PrepareForTest({ WithdrawalRequest.class, Principal.class, ActivitySummaryDao.class, WithdrawalLoggingHelper.class,
		ActivityHistoryHelper.class })
@RunWith(PowerMockRunner.class)
public class PendingApprovalStateTest {

	/**
	 * Parasoft Jtest UTA: Test for approve(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#approve(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testApprove() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		WithdrawalLoggingHelper helper=mock(WithdrawalLoggingHelper.class);
		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class,"log",any(WithdrawalRequest.class), any(WithdrawalEvent.class), any(Class.class),anyString());
		

		spy(ActivityHistoryHelper.class);
		ActivityHistoryHelper activityHistoryHelper=mock(ActivityHistoryHelper.class);
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"updateActivityHistory",any(Withdrawal.class), any(Withdrawal.class));
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"saveSummary",any(Withdrawal.class), anyString());
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal4();
		underTest.approve(withdrawal);

	}
	@Test
	public void testApprove_1() throws Throwable {
		
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal4_1();
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
		Timestamp getLastSavedTimestampResult2 = mock(Timestamp.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getLastSavedTimestampResult2.getTime()).thenReturn(getTimeResult2);

		String toStringResult2 = ""; // UTA: default value
		when(getLastSavedTimestampResult2.toString()).thenReturn(toStringResult2);
		return getLastSavedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp3() throws Throwable {
		Timestamp getLastSavedTimestampResult3 = mock(Timestamp.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getLastSavedTimestampResult3.getTime()).thenReturn(getTimeResult3);

		String toStringResult3 = ""; // UTA: default value
		when(getLastSavedTimestampResult3.toString()).thenReturn(toStringResult3);
		return getLastSavedTimestampResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal() throws Throwable {
		Withdrawal getSavedWithdrawalResult3 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult4 = mock(Timestamp.class);
		when(getSavedWithdrawalResult3.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult4);

		Withdrawal getSavedWithdrawalResult4 = mock(Withdrawal.class);
		when(getSavedWithdrawalResult3.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult4);

		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		when(getSavedWithdrawalResult3.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = false; // UTA: default value
		when(getSavedWithdrawalResult3.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return getSavedWithdrawalResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult2 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		AtRiskDetailsVO getAtRiskDetailsVOResult = mock(AtRiskDetailsVO.class);
		when(getWithdrawalRequestResult2.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult);

		Date getBirthDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult2.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractIssuedStateCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult);

		Timestamp getCreatedResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult2).getDeclarations();

		Date getDisabilityDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		when(getWithdrawalRequestResult2.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult2).getFees();

		Date getFinalContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHasBeenPersistedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult);

		boolean getHaveStep1DriverFieldsChangedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getHaveStep1DriverFieldsChanged())
				.thenReturn(getHaveStep1DriverFieldsChangedResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		Boolean getIsLegaleseConfirmedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIsLegaleseConfirmed()).thenReturn(getIsLegaleseConfirmedResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		boolean getIsPostDraftResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIsPostDraft()).thenReturn(getIsPostDraftResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getLastUpdated()).thenReturn(getLastUpdatedResult);

		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult2).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult2).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Collection<Note> getNotesResult = new ArrayList<Note>(); // UTA: default value
		doReturn(getNotesResult).when(getWithdrawalRequestResult2).getNotes();

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		when(getWithdrawalRequestResult2.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(getWithdrawalRequestResult2.getPrincipal()).thenReturn(getPrincipalResult);

		Collection<WithdrawalRequestNote> getReadOnlyAdminToAdminNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToAdminNotesResult).when(getWithdrawalRequestResult2).getReadOnlyAdminToAdminNotes();

		Collection<WithdrawalRequestNote> getReadOnlyAdminToParticipantNotesResult = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToParticipantNotesResult).when(getWithdrawalRequestResult2)
				.getReadOnlyAdminToParticipantNotes();

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult2).getRecipients();

		Date getRequestDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getRobustDateChangedAfterVestingResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getRobustDateChangedAfterVesting())
				.thenReturn(getRobustDateChangedAfterVestingResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getTerminationDate()).thenReturn(getTerminationDateResult);

		BigDecimal getTotalBalanceResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getTotalBalance()).thenReturn(getTotalBalanceResult);

		BigDecimal getTotalRequestedWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getTotalRequestedWithdrawalAmount())
				.thenReturn(getTotalRequestedWithdrawalAmountResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isAtRiskDeclarationPermittedForUserResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isAtRiskDeclarationPermittedForUser())
				.thenReturn(isAtRiskDeclarationPermittedForUserResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult4 = ""; // UTA: default value
		when(getWithdrawalRequestResult2.toString()).thenReturn(toStringResult4);
		return getWithdrawalRequestResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal2() throws Throwable {
		Withdrawal getSavedWithdrawalResult2 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult3 = mockTimestamp3();
		when(getSavedWithdrawalResult2.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult3);

		Withdrawal getSavedWithdrawalResult3 = mockWithdrawal();
		when(getSavedWithdrawalResult2.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult3);

		WithdrawalRequest getWithdrawalRequestResult2 = mockWithdrawalRequest();
		when(getSavedWithdrawalResult2.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult2);

		boolean isReadyForDoSaveResult2 = false; // UTA: default value
		when(getSavedWithdrawalResult2.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult2);
		return getSavedWithdrawalResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp4() throws Throwable {
		Timestamp getApprovedTimestampResult2 = mock(Timestamp.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getApprovedTimestampResult2.getTime()).thenReturn(getTimeResult4);

		String toStringResult5 = ""; // UTA: default value
		when(getApprovedTimestampResult2.toString()).thenReturn(toStringResult5);
		return getApprovedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult3 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult2);

		Timestamp getApprovedTimestampResult2 = mockTimestamp4();
		when(getWithdrawalRequestResult3.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult2);

		AtRiskDetailsVO getAtRiskDetailsVOResult2 = mock(AtRiskDetailsVO.class);
		when(getWithdrawalRequestResult3.getAtRiskDetailsVO()).thenReturn(getAtRiskDetailsVOResult2);

		Date getBirthDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getBirthDate()).thenReturn(getBirthDateResult2);

		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContractIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getContractId()).thenReturn(getContractIdResult2);

		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		when(getWithdrawalRequestResult3.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractIssuedStateCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getContractIssuedStateCode()).thenReturn(getContractIssuedStateCodeResult2);

		Timestamp getCreatedResult2 = mock(Timestamp.class);
		when(getWithdrawalRequestResult3.getCreated()).thenReturn(getCreatedResult2);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult2 = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult3.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult2);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult2 = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult3.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult2);

		Date getDeathDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getDeathDate()).thenReturn(getDeathDateResult2);

		Collection<Declaration> getDeclarationsResult2 = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult2).when(getWithdrawalRequestResult3).getDeclarations();

		Date getDisabilityDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getDisabilityDate()).thenReturn(getDisabilityDateResult2);

		Integer getEmployeeProfileIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult2);

		Date getExpectedProcessingDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult2);

		Date getExpirationDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getExpirationDate()).thenReturn(getExpirationDateResult2);

		FederalTaxVO getFederalTaxVoResult2 = mock(FederalTaxVO.class);
		when(getWithdrawalRequestResult3.getFederalTaxVo()).thenReturn(getFederalTaxVoResult2);

		Collection<Fee> getFeesResult2 = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult2).when(getWithdrawalRequestResult3).getFees();

		Date getFinalContributionDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getFinalContributionDate()).thenReturn(getFinalContributionDateResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getFirstName()).thenReturn(getFirstNameResult2);

		boolean getHasBeenPersistedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult2);

		boolean getHaveStep1DriverFieldsChangedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getHaveStep1DriverFieldsChanged())
				.thenReturn(getHaveStep1DriverFieldsChangedResult2);

		String getIraServiceProviderCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult2);

		String getIrsDistributionCodeLoanClosureResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult2);

		Boolean getIsLegaleseConfirmedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIsLegaleseConfirmed()).thenReturn(getIsLegaleseConfirmedResult2);

		boolean getIsParticipantCreatedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult2);

		boolean getIsPostDraftResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIsPostDraft()).thenReturn(getIsPostDraftResult2);

		BigDecimal getLastFeeChangeByTPAUserIDResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLastName()).thenReturn(getLastNameResult2);

		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		when(getWithdrawalRequestResult3.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		LegaleseInfo getLegaleseInfoResult2 = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult3.getLegaleseInfo()).thenReturn(getLegaleseInfoResult2);

		String getLegallyMarriedIndResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult2);

		String getLoan1099RNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoan1099RName()).thenReturn(getLoan1099RNameResult2);

		String getLoanOptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoanOption()).thenReturn(getLoanOptionResult2);

		Collection<WithdrawalRequestLoan> getLoansResult2 = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult2).when(getWithdrawalRequestResult3).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(getWithdrawalRequestResult3).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult2);

		Collection<Note> getNotesResult2 = new ArrayList<Note>(); // UTA: default value
		doReturn(getNotesResult2).when(getWithdrawalRequestResult3).getNotes();

		Boolean getPartWithPbaMoneyIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult2);

		Integer getParticipantIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantId()).thenReturn(getParticipantIdResult2);

		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		when(getWithdrawalRequestResult3.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		Boolean getParticipantLeavingPlanIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantLeavingPlanInd())
				.thenReturn(getParticipantLeavingPlanIndResult2);

		LegaleseInfo getParticipantLegaleseInfoResult2 = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult3.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantSSN()).thenReturn(getParticipantSSNResult2);

		String getParticipantStateOfResidenceResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult2);

		String getPaymentToResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getPaymentTo()).thenReturn(getPaymentToResult2);

		Principal getPrincipalResult2 = mock(Principal.class);
		when(getWithdrawalRequestResult3.getPrincipal()).thenReturn(getPrincipalResult2);

		Collection<WithdrawalRequestNote> getReadOnlyAdminToAdminNotesResult2 = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToAdminNotesResult2).when(getWithdrawalRequestResult3).getReadOnlyAdminToAdminNotes();

		Collection<WithdrawalRequestNote> getReadOnlyAdminToParticipantNotesResult2 = new ArrayList<WithdrawalRequestNote>(); // UTA: default value
		doReturn(getReadOnlyAdminToParticipantNotesResult2).when(getWithdrawalRequestResult3)
				.getReadOnlyAdminToParticipantNotes();

		String getReasonCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonCode()).thenReturn(getReasonCodeResult2);

		String getReasonDescriptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDescription()).thenReturn(getReasonDescriptionResult2);

		String getReasonDetailCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult2);

		Collection<Recipient> getRecipientsResult2 = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult2).when(getWithdrawalRequestResult3).getRecipients();

		Date getRequestDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRequestDate()).thenReturn(getRequestDateResult2);

		boolean getRequestRiskIndicatorResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult2);

		Date getRetirementDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRetirementDate()).thenReturn(getRetirementDateResult2);

		boolean getRobustDateChangedAfterVestingResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getRobustDateChangedAfterVesting())
				.thenReturn(getRobustDateChangedAfterVestingResult2);

		boolean getShowFinalContributionDateResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowFinalContributionDate())
				.thenReturn(getShowFinalContributionDateResult2);

		boolean getShowOptionForUnvestedAmountResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult2);

		boolean getShowTaxWitholdingSectionResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getStatusCode()).thenReturn(getStatusCodeResult2);

		Date getTerminationDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getTerminationDate()).thenReturn(getTerminationDateResult2);

		BigDecimal getTotalBalanceResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getTotalBalance()).thenReturn(getTotalBalanceResult2);

		BigDecimal getTotalRequestedWithdrawalAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getTotalRequestedWithdrawalAmount())
				.thenReturn(getTotalRequestedWithdrawalAmountResult2);

		String getUnvestedAmountOptionCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult2);

		String getUserRoleCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUserRoleCode()).thenReturn(getUserRoleCodeResult2);

		Boolean getVestingCalledIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingCalledInd()).thenReturn(getVestingCalledIndResult2);

		Boolean getVestingOverwriteIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult2);

		BigDecimal getWithdrawalAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult2);

		boolean isAtRiskDeclarationPermittedForUserResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isAtRiskDeclarationPermittedForUser())
				.thenReturn(isAtRiskDeclarationPermittedForUserResult2);

		boolean isDeclarationAvailableResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult2);

		boolean isLastFeeChangeWasPSUserIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult2);

		boolean isRemoveAllNotesOnSaveResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult2);

		boolean isValidToProcessResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isValidToProcess()).thenReturn(isValidToProcessResult2);

		boolean isWmsiOrPenchecksSelectedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult2);

		String toStringResult6 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.toString()).thenReturn(toStringResult6);
		return getWithdrawalRequestResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal3() throws Throwable {
		Withdrawal getSavedWithdrawalResult = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult2 = mockTimestamp2();
		when(getSavedWithdrawalResult.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult2);

		Withdrawal getSavedWithdrawalResult2 = mockWithdrawal2();
		when(getSavedWithdrawalResult.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult2);

		WithdrawalRequest getWithdrawalRequestResult3 = mockWithdrawalRequest2();
		when(getSavedWithdrawalResult.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult3);

		boolean isReadyForDoSaveResult3 = false; // UTA: default value
		when(getSavedWithdrawalResult.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult3);
		return getSavedWithdrawalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal4() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		Withdrawal getSavedWithdrawalResult = mockWithdrawal3();
		when(withdrawal.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult);

		WithdrawalRequest getWithdrawalRequestResult4 = mock(WithdrawalRequest.class);
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult4);
		when(getWithdrawalRequestResult4.getIsLegaleseConfirmed()).thenReturn(true);

		boolean isReadyForDoSaveResult4 = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult4);
		return withdrawal;
	}
	private static Withdrawal mockWithdrawal4_1() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		Withdrawal getSavedWithdrawalResult = mockWithdrawal3();
		when(withdrawal.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult);

		WithdrawalRequest getWithdrawalRequestResult4 = mock(WithdrawalRequest.class);
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult4);
		when(getWithdrawalRequestResult4.getIsLegaleseConfirmed()).thenReturn(false);

		boolean isReadyForDoSaveResult4 = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult4);
		return withdrawal;
	}
	

	/**
	 * Parasoft Jtest UTA: Test for deny(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#deny(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testDeny() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		WithdrawalLoggingHelper helper=mock(WithdrawalLoggingHelper.class);
		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class,"log",any(WithdrawalRequest.class), any(WithdrawalEvent.class), any(Class.class),anyString());
		

		spy(ActivityHistoryHelper.class);
		ActivityHistoryHelper activityHistoryHelper=mock(ActivityHistoryHelper.class);
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"updateActivityHistory",any(Withdrawal.class), any(Withdrawal.class));
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"saveSummary",any(Withdrawal.class), anyString());
		// Given
		PendingApprovalState underTest = new PendingApprovalState();
	
		// When
		Withdrawal withdrawal = mockWithdrawal8();
		underTest.deny(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp5() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp6() throws Throwable {
		Timestamp getLastSavedTimestampResult2 = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastSavedTimestampResult2.toString()).thenReturn(toStringResult2);
		return getLastSavedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp7() throws Throwable {
		Timestamp getLastSavedTimestampResult3 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getLastSavedTimestampResult3.toString()).thenReturn(toStringResult3);
		return getLastSavedTimestampResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal5() throws Throwable {
		Withdrawal getSavedWithdrawalResult3 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult4 = mock(Timestamp.class);
		when(getSavedWithdrawalResult3.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult4);

		Withdrawal getSavedWithdrawalResult4 = mock(Withdrawal.class);
		when(getSavedWithdrawalResult3.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult4);

		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		when(getSavedWithdrawalResult3.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = false; // UTA: default value
		when(getSavedWithdrawalResult3.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return getSavedWithdrawalResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult2 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult2.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getContractName()).thenReturn(getContractNameResult);

		ContractPermission getContractPermissionResult = mock(ContractPermission.class);
		when(getWithdrawalRequestResult2.getContractPermission()).thenReturn(getContractPermissionResult);

		Timestamp getCreatedResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult2).getDeclarations();

		Date getDisabilityDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		when(getWithdrawalRequestResult2.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult2).getFees();

		Date getFinalContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHasBeenPersistedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult);

		Boolean getIgnoreErrorsResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIgnoreErrors()).thenReturn(getIgnoreErrorsResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getLastUpdated()).thenReturn(getLastUpdatedResult);

		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult2).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult2).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		when(getWithdrawalRequestResult2.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(getWithdrawalRequestResult2.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult2).getRecipients();

		Date getRequestDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		String getRequestTypeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getRequestType()).thenReturn(getRequestTypeResult);

		Date getRetirementDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult4 = ""; // UTA: default value
		when(getWithdrawalRequestResult2.toString()).thenReturn(toStringResult4);
		return getWithdrawalRequestResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal6() throws Throwable {
		Withdrawal getSavedWithdrawalResult2 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult3 = mockTimestamp7();
		when(getSavedWithdrawalResult2.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult3);

		Withdrawal getSavedWithdrawalResult3 = mockWithdrawal5();
		when(getSavedWithdrawalResult2.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult3);

		WithdrawalRequest getWithdrawalRequestResult2 = mockWithdrawalRequest3();
		when(getSavedWithdrawalResult2.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult2);

		boolean isReadyForDoSaveResult2 = false; // UTA: default value
		when(getSavedWithdrawalResult2.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult2);
		return getSavedWithdrawalResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp8() throws Throwable {
		Timestamp getApprovedTimestampResult2 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getApprovedTimestampResult2.toString()).thenReturn(toStringResult5);
		return getApprovedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getBirthDateResult2 = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult2.getTime()).thenReturn(getTimeResult);

		String toStringResult6 = ""; // UTA: default value
		when(getBirthDateResult2.toString()).thenReturn(toStringResult6);
		return getBirthDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo() throws Throwable {
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		String getClientAccountRepEmailResult = ""; // UTA: default value
		when(getContractInfoResult2.getClientAccountRepEmail()).thenReturn(getClientAccountRepEmailResult);

		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult2.getMailChequeToAddressIndicator())
				.thenReturn(getMailChequeToAddressIndicatorResult);

		boolean isBundledGaIndicatorResult = false; // UTA: default value
		when(getContractInfoResult2.isBundledGaIndicator()).thenReturn(isBundledGaIndicatorResult);

		String toStringResult7 = ""; // UTA: default value
		when(getContractInfoResult2.toString()).thenReturn(toStringResult7);
		return getContractInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractPermission
	 */
	private static ContractPermission mockContractPermission() throws Throwable {
		ContractPermission getContractPermissionResult2 = mock(ContractPermission.class);
		boolean isSigningAuthorityResult = false; // UTA: default value
		when(getContractPermissionResult2.isSigningAuthority()).thenReturn(isSigningAuthorityResult);

		String toStringResult8 = ""; // UTA: default value
		when(getContractPermissionResult2.toString()).thenReturn(toStringResult8);
		return getContractPermissionResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp9() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult9 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult9);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult2 = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.doErrorCodesExist()).thenReturn(doErrorCodesExistResult);

		boolean doWarningCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.doWarningCodesExist()).thenReturn(doWarningCodesExistResult);

		Timestamp getCreatedResult3 = mock(Timestamp.class);
		when(getCurrentAdminToAdminNoteResult2.getCreated()).thenReturn(getCreatedResult3);

		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		when(getCurrentAdminToAdminNoteResult2.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.isBlank()).thenReturn(isBlankResult);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.toString()).thenReturn(toStringResult10);
		return getCurrentAdminToAdminNoteResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote2() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult2 = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.doErrorCodesExist()).thenReturn(doErrorCodesExistResult2);

		boolean doWarningCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.doWarningCodesExist()).thenReturn(doWarningCodesExistResult2);

		Timestamp getCreatedResult4 = mock(Timestamp.class);
		when(getCurrentAdminToParticipantNoteResult2.getCreated()).thenReturn(getCreatedResult4);

		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		when(getCurrentAdminToParticipantNoteResult2.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.isBlank()).thenReturn(isBlankResult2);

		String toStringResult11 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.toString()).thenReturn(toStringResult11);
		return getCurrentAdminToParticipantNoteResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getDeathDateResult2 = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult2.getTime()).thenReturn(getTimeResult2);

		String toStringResult12 = ""; // UTA: default value
		when(getDeathDateResult2.toString()).thenReturn(toStringResult12);
		return getDeathDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getDisabilityDateResult2 = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult2.getTime()).thenReturn(getTimeResult3);

		String toStringResult13 = ""; // UTA: default value
		when(getDisabilityDateResult2.toString()).thenReturn(toStringResult13);
		return getDisabilityDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getExpectedProcessingDateResult2 = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult2.getTime()).thenReturn(getTimeResult4);

		String toStringResult14 = ""; // UTA: default value
		when(getExpectedProcessingDateResult2.toString()).thenReturn(toStringResult14);
		return getExpectedProcessingDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getExpirationDateResult2 = mock(Date.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult2.getTime()).thenReturn(getTimeResult5);

		String toStringResult15 = ""; // UTA: default value
		when(getExpirationDateResult2.toString()).thenReturn(toStringResult15);
		return getExpirationDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult3 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult2);

		Timestamp getApprovedTimestampResult2 = mockTimestamp8();
		when(getWithdrawalRequestResult3.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult2);

		Date getBirthDateResult2 = mockDate();
		when(getWithdrawalRequestResult3.getBirthDate()).thenReturn(getBirthDateResult2);

		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContractIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getContractId()).thenReturn(getContractIdResult2);

		ContractInfo getContractInfoResult2 = mockContractInfo();
		when(getWithdrawalRequestResult3.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getContractName()).thenReturn(getContractNameResult2);

		ContractPermission getContractPermissionResult2 = mockContractPermission();
		when(getWithdrawalRequestResult3.getContractPermission()).thenReturn(getContractPermissionResult2);

		Timestamp getCreatedResult2 = mockTimestamp9();
		when(getWithdrawalRequestResult3.getCreated()).thenReturn(getCreatedResult2);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult2 = mockWithdrawalRequestNote();
		when(getWithdrawalRequestResult3.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult2);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult2 = mockWithdrawalRequestNote2();
		when(getWithdrawalRequestResult3.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult2);

		Date getDeathDateResult2 = mockDate2();
		when(getWithdrawalRequestResult3.getDeathDate()).thenReturn(getDeathDateResult2);

		Collection<Declaration> getDeclarationsResult2 = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult2).when(getWithdrawalRequestResult3).getDeclarations();

		Date getDisabilityDateResult2 = mockDate3();
		when(getWithdrawalRequestResult3.getDisabilityDate()).thenReturn(getDisabilityDateResult2);

		Integer getEmployeeProfileIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult2);

		Date getExpectedProcessingDateResult2 = mockDate4();
		when(getWithdrawalRequestResult3.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult2);

		Date getExpirationDateResult2 = mockDate5();
		when(getWithdrawalRequestResult3.getExpirationDate()).thenReturn(getExpirationDateResult2);

		FederalTaxVO getFederalTaxVoResult2 = mock(FederalTaxVO.class);
		when(getWithdrawalRequestResult3.getFederalTaxVo()).thenReturn(getFederalTaxVoResult2);

		Collection<Fee> getFeesResult2 = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult2).when(getWithdrawalRequestResult3).getFees();

		Date getFinalContributionDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getFinalContributionDate()).thenReturn(getFinalContributionDateResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getFirstName()).thenReturn(getFirstNameResult2);

		boolean getHasBeenPersistedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult2);

		Boolean getIgnoreErrorsResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIgnoreErrors()).thenReturn(getIgnoreErrorsResult2);

		String getIraServiceProviderCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult2);

		String getIrsDistributionCodeLoanClosureResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult2);

		boolean getIsParticipantCreatedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult2);

		BigDecimal getLastFeeChangeByTPAUserIDResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLastName()).thenReturn(getLastNameResult2);

		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		when(getWithdrawalRequestResult3.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		LegaleseInfo getLegaleseInfoResult2 = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult3.getLegaleseInfo()).thenReturn(getLegaleseInfoResult2);

		String getLegallyMarriedIndResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult2);

		String getLoan1099RNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoan1099RName()).thenReturn(getLoan1099RNameResult2);

		String getLoanOptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoanOption()).thenReturn(getLoanOptionResult2);

		Collection<WithdrawalRequestLoan> getLoansResult2 = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult2).when(getWithdrawalRequestResult3).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(getWithdrawalRequestResult3).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult2);

		Boolean getPartWithPbaMoneyIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult2);

		Integer getParticipantIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantId()).thenReturn(getParticipantIdResult2);

		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		when(getWithdrawalRequestResult3.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		Boolean getParticipantLeavingPlanIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantLeavingPlanInd())
				.thenReturn(getParticipantLeavingPlanIndResult2);

		LegaleseInfo getParticipantLegaleseInfoResult2 = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult3.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantSSN()).thenReturn(getParticipantSSNResult2);

		String getParticipantStateOfResidenceResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult2);

		String getPaymentToResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getPaymentTo()).thenReturn(getPaymentToResult2);

		Principal getPrincipalResult2 = mock(Principal.class);
		when(getWithdrawalRequestResult3.getPrincipal()).thenReturn(getPrincipalResult2);

		String getReasonCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonCode()).thenReturn(getReasonCodeResult2);

		String getReasonDescriptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDescription()).thenReturn(getReasonDescriptionResult2);

		String getReasonDetailCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult2);

		Collection<Recipient> getRecipientsResult2 = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult2).when(getWithdrawalRequestResult3).getRecipients();

		Date getRequestDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRequestDate()).thenReturn(getRequestDateResult2);

		boolean getRequestRiskIndicatorResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult2);

		String getRequestTypeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getRequestType()).thenReturn(getRequestTypeResult2);

		Date getRetirementDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRetirementDate()).thenReturn(getRetirementDateResult2);

		boolean getShowFinalContributionDateResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowFinalContributionDate())
				.thenReturn(getShowFinalContributionDateResult2);

		boolean getShowOptionForUnvestedAmountResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult2);

		boolean getShowTaxWitholdingSectionResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getStatusCode()).thenReturn(getStatusCodeResult2);

		Date getTerminationDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getTerminationDate()).thenReturn(getTerminationDateResult2);

		String getUnvestedAmountOptionCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult2);

		String getUserRoleCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUserRoleCode()).thenReturn(getUserRoleCodeResult2);

		Boolean getVestingCalledIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingCalledInd()).thenReturn(getVestingCalledIndResult2);

		Boolean getVestingOverwriteIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult2);

		BigDecimal getWithdrawalAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult2);

		boolean isDeclarationAvailableResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult2);

		boolean isLastFeeChangeWasPSUserIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult2);

		boolean isRemoveAllNotesOnSaveResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult2);

		boolean isValidToProcessResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isValidToProcess()).thenReturn(isValidToProcessResult2);

		boolean isWmsiOrPenchecksSelectedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult2);

		String toStringResult16 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.toString()).thenReturn(toStringResult16);
		return getWithdrawalRequestResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal7() throws Throwable {
		Withdrawal getSavedWithdrawalResult = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult2 = mockTimestamp6();
		when(getSavedWithdrawalResult.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult2);

		Withdrawal getSavedWithdrawalResult2 = mockWithdrawal6();
		when(getSavedWithdrawalResult.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult2);

		WithdrawalRequest getWithdrawalRequestResult3 = mockWithdrawalRequest4();
		when(getSavedWithdrawalResult.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult3);

		boolean isReadyForDoSaveResult3 = false; // UTA: default value
		when(getSavedWithdrawalResult.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult3);
		return getSavedWithdrawalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal8() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp5();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		Withdrawal getSavedWithdrawalResult = mockWithdrawal7();
		when(withdrawal.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult);

		Principal principal = mock(Principal.class);
		when(principal.getProfileId()).thenReturn(10l);
		WithdrawalRequest getWithdrawalRequestResult4 = mock(WithdrawalRequest.class);
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult4);
		when(getWithdrawalRequestResult4.getPrincipal()).thenReturn(principal);

		boolean isReadyForDoSaveResult4 = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult4);
		
		
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalStateEnum()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#getWithdrawalStateEnum()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalStateEnum() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		WithdrawalStateEnum result = underTest.getWithdrawalStateEnum();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for save(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#save(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testSave() throws Throwable {
		spy(WithdrawalLoggingHelper.class);

		WithdrawalLoggingHelper helper=mock(WithdrawalLoggingHelper.class);
		PowerMockito.doNothing().when(WithdrawalLoggingHelper.class,"log",any(WithdrawalRequest.class), any(WithdrawalEvent.class), any(Class.class),anyString());
		

		spy(ActivityHistoryHelper.class);
		ActivityHistoryHelper activityHistoryHelper=mock(ActivityHistoryHelper.class);
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"updateActivityHistory",any(Withdrawal.class), any(Withdrawal.class));
		PowerMockito.doNothing().when(ActivityHistoryHelper.class,"saveSummary",any(Withdrawal.class), anyString());

		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal12();
		underTest.save(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp10() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp11() throws Throwable {
		Timestamp getLastSavedTimestampResult2 = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getLastSavedTimestampResult2.toString()).thenReturn(toStringResult2);
		return getLastSavedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp12() throws Throwable {
		Timestamp getLastSavedTimestampResult3 = mock(Timestamp.class);
		String toStringResult3 = ""; // UTA: default value
		when(getLastSavedTimestampResult3.toString()).thenReturn(toStringResult3);
		return getLastSavedTimestampResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal9() throws Throwable {
		Withdrawal getSavedWithdrawalResult3 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult4 = mock(Timestamp.class);
		when(getSavedWithdrawalResult3.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult4);

		Withdrawal getSavedWithdrawalResult4 = mock(Withdrawal.class);
		when(getSavedWithdrawalResult3.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult4);

		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		when(getSavedWithdrawalResult3.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = false; // UTA: default value
		when(getSavedWithdrawalResult3.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return getSavedWithdrawalResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest5() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult2 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult2.getContractInfo()).thenReturn(getContractInfoResult);

		ContractPermission getContractPermissionResult = mock(ContractPermission.class);
		when(getWithdrawalRequestResult2.getContractPermission()).thenReturn(getContractPermissionResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		when(getWithdrawalRequestResult2.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult2).getDeclarations();

		Date getDisabilityDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		when(getWithdrawalRequestResult2.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult2).getFees();

		Date getFinalContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getFirstName()).thenReturn(getFirstNameResult);

		boolean getHasBeenPersistedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult);

		Boolean getIgnoreErrorsResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIgnoreErrors()).thenReturn(getIgnoreErrorsResult);

		String getIraServiceProviderCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult);

		String getIrsDistributionCodeLoanClosureResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult);

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLastName()).thenReturn(getLastNameResult);

		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		when(getWithdrawalRequestResult2.getLastUpdated()).thenReturn(getLastUpdatedResult);

		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getLegaleseInfo()).thenReturn(getLegaleseInfoResult);

		String getLegallyMarriedIndResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult);

		String getLoan1099RNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoan1099RName()).thenReturn(getLoan1099RNameResult);

		String getLoanOptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getLoanOption()).thenReturn(getLoanOptionResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult).when(getWithdrawalRequestResult2).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult2).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantId()).thenReturn(getParticipantIdResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult2.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

		String getParticipantStateOfResidenceResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult);

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mock(Principal.class);
		when(getWithdrawalRequestResult2.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult2).getRecipients();

		Date getRequestDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getRetirementDate()).thenReturn(getRetirementDateResult);

		boolean getShowFinalContributionDateResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowFinalContributionDate()).thenReturn(getShowFinalContributionDateResult);

		boolean getShowOptionForUnvestedAmountResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult);

		boolean getShowTaxWitholdingSectionResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult);

		String getStatusCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getStatusCode()).thenReturn(getStatusCodeResult);

		Date getTerminationDateResult = mock(Date.class);
		when(getWithdrawalRequestResult2.getTerminationDate()).thenReturn(getTerminationDateResult);

		String getUnvestedAmountOptionCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult);

		String getUserRoleCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult2.getUserRoleCode()).thenReturn(getUserRoleCodeResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingOverwriteIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult);

		BigDecimal getWithdrawalAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult2.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);

		boolean isLastFeeChangeWasPSUserIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult);

		boolean isRemoveAllNotesOnSaveResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult);

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult2.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);

		String toStringResult4 = ""; // UTA: default value
		when(getWithdrawalRequestResult2.toString()).thenReturn(toStringResult4);
		return getWithdrawalRequestResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal10() throws Throwable {
		Withdrawal getSavedWithdrawalResult2 = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult3 = mockTimestamp12();
		when(getSavedWithdrawalResult2.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult3);

		Withdrawal getSavedWithdrawalResult3 = mockWithdrawal9();
		when(getSavedWithdrawalResult2.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult3);

		WithdrawalRequest getWithdrawalRequestResult2 = mockWithdrawalRequest5();
		when(getSavedWithdrawalResult2.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult2);

		boolean isReadyForDoSaveResult2 = false; // UTA: default value
		when(getSavedWithdrawalResult2.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult2);
		return getSavedWithdrawalResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp13() throws Throwable {
		Timestamp getApprovedTimestampResult2 = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getApprovedTimestampResult2.toString()).thenReturn(toStringResult5);
		return getApprovedTimestampResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getBirthDateResult2 = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getBirthDateResult2.after(any(Date.class))).thenReturn(afterResult);

		String toStringResult6 = ""; // UTA: default value
		when(getBirthDateResult2.toString()).thenReturn(toStringResult6);
		return getBirthDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo2() throws Throwable {
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult2.getMailChequeToAddressIndicator())
				.thenReturn(getMailChequeToAddressIndicatorResult);

		boolean isBundledGaIndicatorResult = false; // UTA: default value
		when(getContractInfoResult2.isBundledGaIndicator()).thenReturn(isBundledGaIndicatorResult);

		String toStringResult7 = ""; // UTA: default value
		when(getContractInfoResult2.toString()).thenReturn(toStringResult7);
		return getContractInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractPermission
	 */
	private static ContractPermission mockContractPermission2() throws Throwable {
		ContractPermission getContractPermissionResult2 = mock(ContractPermission.class);
		boolean isSigningAuthorityResult = false; // UTA: default value
		when(getContractPermissionResult2.isSigningAuthority()).thenReturn(isSigningAuthorityResult);

		String toStringResult8 = ""; // UTA: default value
		when(getContractPermissionResult2.toString()).thenReturn(toStringResult8);
		return getContractPermissionResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote3() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult2 = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.doErrorCodesExist()).thenReturn(doErrorCodesExistResult);

		boolean doWarningCodesExistResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.doWarningCodesExist()).thenReturn(doWarningCodesExistResult);

		Timestamp getLastUpdatedResult2 = mock(Timestamp.class);
		when(getCurrentAdminToAdminNoteResult2.getLastUpdated()).thenReturn(getLastUpdatedResult2);

		String getNoteResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.getNote()).thenReturn(getNoteResult);

		String getNoteTypeCodeResult = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult);

		boolean isBlankResult = false; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.isBlank()).thenReturn(isBlankResult);

		String toStringResult9 = ""; // UTA: default value
		when(getCurrentAdminToAdminNoteResult2.toString()).thenReturn(toStringResult9);
		return getCurrentAdminToAdminNoteResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote4() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult2 = mock(WithdrawalRequestNote.class);
		boolean doErrorCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.doErrorCodesExist()).thenReturn(doErrorCodesExistResult2);

		boolean doWarningCodesExistResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.doWarningCodesExist()).thenReturn(doWarningCodesExistResult2);

		Timestamp getLastUpdatedResult3 = mock(Timestamp.class);
		when(getCurrentAdminToParticipantNoteResult2.getLastUpdated()).thenReturn(getLastUpdatedResult3);

		String getNoteResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.getNote()).thenReturn(getNoteResult2);

		String getNoteTypeCodeResult2 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.getNoteTypeCode()).thenReturn(getNoteTypeCodeResult2);

		boolean isBlankResult2 = false; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.isBlank()).thenReturn(isBlankResult2);

		String toStringResult10 = ""; // UTA: default value
		when(getCurrentAdminToParticipantNoteResult2.toString()).thenReturn(toStringResult10);
		return getCurrentAdminToParticipantNoteResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getDeathDateResult2 = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getDeathDateResult2.after(any(Date.class))).thenReturn(afterResult2);

		String toStringResult11 = ""; // UTA: default value
		when(getDeathDateResult2.toString()).thenReturn(toStringResult11);
		return getDeathDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate8() throws Throwable {
		Date getDisabilityDateResult2 = mock(Date.class);
		boolean afterResult3 = false; // UTA: default value
		when(getDisabilityDateResult2.after(any(Date.class))).thenReturn(afterResult3);

		String toStringResult12 = ""; // UTA: default value
		when(getDisabilityDateResult2.toString()).thenReturn(toStringResult12);
		return getDisabilityDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getExpectedProcessingDateResult2 = mock(Date.class);
		boolean afterResult4 = false; // UTA: default value
		when(getExpectedProcessingDateResult2.after(any(Date.class))).thenReturn(afterResult4);

		String toStringResult13 = ""; // UTA: default value
		when(getExpectedProcessingDateResult2.toString()).thenReturn(toStringResult13);
		return getExpectedProcessingDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getExpirationDateResult2 = mock(Date.class);
		boolean afterResult5 = false; // UTA: default value
		when(getExpirationDateResult2.after(any(Date.class))).thenReturn(afterResult5);

		String toStringResult14 = ""; // UTA: default value
		when(getExpirationDateResult2.toString()).thenReturn(toStringResult14);
		return getExpirationDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO() throws Throwable {
		FederalTaxVO getFederalTaxVoResult2 = mock(FederalTaxVO.class);
		String toStringResult15 = ""; // UTA: default value
		when(getFederalTaxVoResult2.toString()).thenReturn(toStringResult15);
		return getFederalTaxVoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate11() throws Throwable {
		Date getFinalContributionDateResult2 = mock(Date.class);
		boolean afterResult6 = false; // UTA: default value
		when(getFinalContributionDateResult2.after(any(Date.class))).thenReturn(afterResult6);

		String toStringResult16 = ""; // UTA: default value
		when(getFinalContributionDateResult2.toString()).thenReturn(toStringResult16);
		return getFinalContributionDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp14() throws Throwable {
		Timestamp getLastUpdatedResult4 = mock(Timestamp.class);
		String toStringResult17 = ""; // UTA: default value
		when(getLastUpdatedResult4.toString()).thenReturn(toStringResult17);
		return getLastUpdatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo() throws Throwable {
		LegaleseInfo getLegaleseInfoResult2 = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getLegaleseInfoResult2.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult2.getContentId()).thenReturn(getContentIdResult);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult2.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		Timestamp getLastUpdatedResult5 = mock(Timestamp.class);
		when(getLegaleseInfoResult2.getLastUpdated()).thenReturn(getLastUpdatedResult5);

		String toStringResult18 = ""; // UTA: default value
		when(getLegaleseInfoResult2.toString()).thenReturn(toStringResult18);
		return getLegaleseInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate12() throws Throwable {
		Date getMostRecentPriorContributionDateResult2 = mock(Date.class);
		boolean afterResult7 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.after(any(Date.class))).thenReturn(afterResult7);

		String toStringResult19 = ""; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.toString()).thenReturn(toStringResult19);
		return getMostRecentPriorContributionDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest6() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult3 = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult2);

		Timestamp getApprovedTimestampResult2 = mockTimestamp13();
		when(getWithdrawalRequestResult3.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult2);

		Date getBirthDateResult2 = mockDate6();
		when(getWithdrawalRequestResult3.getBirthDate()).thenReturn(getBirthDateResult2);

		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContractIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getContractId()).thenReturn(getContractIdResult2);

		ContractInfo getContractInfoResult2 = mockContractInfo2();
		when(getWithdrawalRequestResult3.getContractInfo()).thenReturn(getContractInfoResult2);

		ContractPermission getContractPermissionResult2 = mockContractPermission2();
		when(getWithdrawalRequestResult3.getContractPermission()).thenReturn(getContractPermissionResult2);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult2 = mockWithdrawalRequestNote3();
		when(getWithdrawalRequestResult3.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult2);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult2 = mockWithdrawalRequestNote4();
		when(getWithdrawalRequestResult3.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult2);

		Date getDeathDateResult2 = mockDate7();
		when(getWithdrawalRequestResult3.getDeathDate()).thenReturn(getDeathDateResult2);

		Collection<Declaration> getDeclarationsResult2 = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult2).when(getWithdrawalRequestResult3).getDeclarations();

		Date getDisabilityDateResult2 = mockDate8();
		when(getWithdrawalRequestResult3.getDisabilityDate()).thenReturn(getDisabilityDateResult2);

		Integer getEmployeeProfileIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult2);

		Date getExpectedProcessingDateResult2 = mockDate9();
		when(getWithdrawalRequestResult3.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult2);

		Date getExpirationDateResult2 = mockDate10();
		when(getWithdrawalRequestResult3.getExpirationDate()).thenReturn(getExpirationDateResult2);

		FederalTaxVO getFederalTaxVoResult2 = mockFederalTaxVO();
		when(getWithdrawalRequestResult3.getFederalTaxVo()).thenReturn(getFederalTaxVoResult2);

		Collection<Fee> getFeesResult2 = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult2).when(getWithdrawalRequestResult3).getFees();

		Date getFinalContributionDateResult2 = mockDate11();
		when(getWithdrawalRequestResult3.getFinalContributionDate()).thenReturn(getFinalContributionDateResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getFirstName()).thenReturn(getFirstNameResult2);

		boolean getHasBeenPersistedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getHasBeenPersisted()).thenReturn(getHasBeenPersistedResult2);

		Boolean getIgnoreErrorsResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIgnoreErrors()).thenReturn(getIgnoreErrorsResult2);

		String getIraServiceProviderCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIraServiceProviderCode()).thenReturn(getIraServiceProviderCodeResult2);

		String getIrsDistributionCodeLoanClosureResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getIrsDistributionCodeLoanClosure())
				.thenReturn(getIrsDistributionCodeLoanClosureResult2);

		boolean getIsParticipantCreatedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult2);

		BigDecimal getLastFeeChangeByTPAUserIDResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLastName()).thenReturn(getLastNameResult2);

		Timestamp getLastUpdatedResult4 = mockTimestamp14();
		when(getWithdrawalRequestResult3.getLastUpdated()).thenReturn(getLastUpdatedResult4);

		LegaleseInfo getLegaleseInfoResult2 = mockLegaleseInfo();
		when(getWithdrawalRequestResult3.getLegaleseInfo()).thenReturn(getLegaleseInfoResult2);

		String getLegallyMarriedIndResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLegallyMarriedInd()).thenReturn(getLegallyMarriedIndResult2);

		String getLoan1099RNameResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoan1099RName()).thenReturn(getLoan1099RNameResult2);

		String getLoanOptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getLoanOption()).thenReturn(getLoanOptionResult2);

		Collection<WithdrawalRequestLoan> getLoansResult2 = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getLoansResult2).when(getWithdrawalRequestResult3).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(getWithdrawalRequestResult3).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult2 = mockDate12();
		when(getWithdrawalRequestResult3.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult2);

		Boolean getPartWithPbaMoneyIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult2);

		Integer getParticipantIdResult2 = 0; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantId()).thenReturn(getParticipantIdResult2);

		Boolean getParticipantLeavingPlanIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantLeavingPlanInd())
				.thenReturn(getParticipantLeavingPlanIndResult2);

		LegaleseInfo getParticipantLegaleseInfoResult2 = mock(LegaleseInfo.class);
		when(getWithdrawalRequestResult3.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult2);

		String getParticipantStateOfResidenceResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getParticipantStateOfResidence())
				.thenReturn(getParticipantStateOfResidenceResult2);

		String getPaymentToResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getPaymentTo()).thenReturn(getPaymentToResult2);

		Principal getPrincipalResult2 = mock(Principal.class);
		when(getWithdrawalRequestResult3.getPrincipal()).thenReturn(getPrincipalResult2);

		String getReasonCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonCode()).thenReturn(getReasonCodeResult2);

		String getReasonDescriptionResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDescription()).thenReturn(getReasonDescriptionResult2);

		String getReasonDetailCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult2);

		Collection<Recipient> getRecipientsResult2 = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult2).when(getWithdrawalRequestResult3).getRecipients();

		Date getRequestDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRequestDate()).thenReturn(getRequestDateResult2);

		boolean getRequestRiskIndicatorResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult2);

		Date getRetirementDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getRetirementDate()).thenReturn(getRetirementDateResult2);

		boolean getShowFinalContributionDateResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowFinalContributionDate())
				.thenReturn(getShowFinalContributionDateResult2);

		boolean getShowOptionForUnvestedAmountResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowOptionForUnvestedAmount())
				.thenReturn(getShowOptionForUnvestedAmountResult2);

		boolean getShowTaxWitholdingSectionResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getShowTaxWitholdingSection()).thenReturn(getShowTaxWitholdingSectionResult2);

		String getStatusCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getStatusCode()).thenReturn(getStatusCodeResult2);

		Date getTerminationDateResult2 = mock(Date.class);
		when(getWithdrawalRequestResult3.getTerminationDate()).thenReturn(getTerminationDateResult2);

		String getUnvestedAmountOptionCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUnvestedAmountOptionCode()).thenReturn(getUnvestedAmountOptionCodeResult2);

		String getUserRoleCodeResult2 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.getUserRoleCode()).thenReturn(getUserRoleCodeResult2);

		Boolean getVestingCalledIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingCalledInd()).thenReturn(getVestingCalledIndResult2);

		Boolean getVestingOverwriteIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.getVestingOverwriteInd()).thenReturn(getVestingOverwriteIndResult2);

		BigDecimal getWithdrawalAmountResult2 = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult3.getWithdrawalAmount()).thenReturn(getWithdrawalAmountResult2);

		boolean isDeclarationAvailableResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult2);

		boolean isLastFeeChangeWasPSUserIndResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isLastFeeChangeWasPSUserInd()).thenReturn(isLastFeeChangeWasPSUserIndResult2);

		boolean isRemoveAllNotesOnSaveResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isRemoveAllNotesOnSave()).thenReturn(isRemoveAllNotesOnSaveResult2);

		boolean isValidToProcessResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isValidToProcess()).thenReturn(isValidToProcessResult2);

		boolean isWmsiOrPenchecksSelectedResult2 = false; // UTA: default value
		when(getWithdrawalRequestResult3.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult2);

		String toStringResult20 = ""; // UTA: default value
		when(getWithdrawalRequestResult3.toString()).thenReturn(toStringResult20);
		return getWithdrawalRequestResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal11() throws Throwable {
		Withdrawal getSavedWithdrawalResult = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult2 = mockTimestamp11();
		when(getSavedWithdrawalResult.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult2);

		Withdrawal getSavedWithdrawalResult2 = mockWithdrawal10();
		when(getSavedWithdrawalResult.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult2);

		WithdrawalRequest getWithdrawalRequestResult3 = mockWithdrawalRequest6();
		when(getSavedWithdrawalResult.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult3);

		boolean isReadyForDoSaveResult3 = false; // UTA: default value
		when(getSavedWithdrawalResult.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult3);
		return getSavedWithdrawalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal12() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp10();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		Withdrawal getSavedWithdrawalResult = mockWithdrawal11();
		when(withdrawal.getSavedWithdrawal()).thenReturn(getSavedWithdrawalResult);

		WithdrawalRequest getWithdrawalRequestResult4 = mock(WithdrawalRequest.class);
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult4);

		boolean isReadyForDoSaveResult4 = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult4);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForReview(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#sendForReview(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testSendForReview() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		
	   List<WithdrawalActivityDetail> ads = new ArrayList<WithdrawalActivityDetail>();
	   WithdrawalActivityDetail activityDetail = new WithdrawalActivityDetail();
	   activityDetail.setLastUpdatedById(10);
	   ads.add(activityDetail);
	   
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal13();
		underTest.sendForReview(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp15() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp16() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate13() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo3() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp17() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp18() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		return getCreatedResult2;
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

		Timestamp getCreatedResult2 = mockTimestamp18();
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
	private static Timestamp mockTimestamp19() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		return getCreatedResult3;
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

		Timestamp getCreatedResult3 = mockTimestamp19();
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
	private static Date mockDate14() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult2);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate15() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult3);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate16() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult4);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate17() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult5);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO2() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate18() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		long getTimeResult6 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult6);
		return getFinalContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp20() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo2() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp20();
		when(getLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult4);

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		String getLegaleseTextResult = ""; // UTA: default value
		when(getLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate19() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		long getTimeResult7 = 0L; // UTA: default value
		when(getMostRecentPriorContributionDateResult.getTime()).thenReturn(getTimeResult7);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate20() throws Throwable {
		Date getLastInvestmentDateResult = mock(Date.class);
		long getTimeResult8 = 0L; // UTA: default value
		when(getLastInvestmentDateResult.getTime()).thenReturn(getTimeResult8);
		return getLastInvestmentDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getContractNameResult = ""; // UTA: default value
		when(getParticipantInfoResult.getContractName()).thenReturn(getContractNameResult);

		String getContractStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getContractStatusCode()).thenReturn(getContractStatusCodeResult);

		Date getLastInvestmentDateResult = mockDate20();
		when(getParticipantInfoResult.getLastInvestmentDate()).thenReturn(getLastInvestmentDateResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(getParticipantInfoResult).getMoneyTypes();

		Collection<WithdrawalRequestLoan> getOutstandingLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		doReturn(getOutstandingLoansResult).when(getParticipantInfoResult).getOutstandingLoans();

		String getParticipantStatusCodeResult = ""; // UTA: default value
		when(getParticipantInfoResult.getParticipantStatusCode()).thenReturn(getParticipantStatusCodeResult);

		Boolean getThirdPartyAdminIdResult = false; // UTA: default value
		when(getParticipantInfoResult.getThirdPartyAdminId()).thenReturn(getThirdPartyAdminIdResult);
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp21() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo3() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp21();
		when(getParticipantLegaleseInfoResult.getCreated()).thenReturn(getCreatedResult5);

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		String getLegaleseTextResult2 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getLegaleseText()).thenReturn(getLegaleseTextResult2);
		return getParticipantLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate21() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		long getTimeResult9 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult9);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate22() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		long getTimeResult10 = 0L; // UTA: default value
		when(getRetirementDateResult.getTime()).thenReturn(getTimeResult10);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate23() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		long getTimeResult11 = 0L; // UTA: default value
		when(getTerminationDateResult.getTime()).thenReturn(getTimeResult11);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest7() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);
		when(getWithdrawalRequestResult.toLog()).thenReturn("withdrawalRequestInLogFormat");

		Timestamp getApprovedTimestampResult = mockTimestamp16();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate13();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo3();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		Timestamp getCreatedResult = mockTimestamp17();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote5();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote6();
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

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO2();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate18();
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

		BigDecimal getLastFeeChangeByTPAUserIDResult = BigDecimal.ZERO; // UTA: default value
		when(getWithdrawalRequestResult.getLastFeeChangeByTPAUserID()).thenReturn(getLastFeeChangeByTPAUserIDResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo2();
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

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo3();
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

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		String getReasonDescriptionResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDescription()).thenReturn(getReasonDescriptionResult);

		String getReasonDetailCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonDetailCode()).thenReturn(getReasonDetailCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new LoanRecipient();
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate21();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate22();
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

		Date getTerminationDateResult = mockDate23();
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

		boolean isValidToProcessResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isValidToProcess()).thenReturn(isValidToProcessResult);

		boolean isWmsiOrPenchecksSelectedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isWmsiOrPenchecksSelected()).thenReturn(isWmsiOrPenchecksSelectedResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal13() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp15();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest7();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);

		boolean isReadyForDoSaveResult = true; // UTA: default value
		when(withdrawal.isReadyForDoSave()).thenReturn(isReadyForDoSaveResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for transitionToState(Withdrawal, WithdrawalStateEnum)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingApprovalState#transitionToState(Withdrawal, WithdrawalStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testTransitionToState() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalStateEnum newState = WithdrawalStateEnum.PENDING_APPROVAL; // UTA: default value
		underTest.transitionToState(withdrawal, newState);

	}
	@Test
	public void testTransitionToState_1() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalStateEnum newState = WithdrawalStateEnum.PENDING_REVIEW; // UTA: default value
		underTest.transitionToState(withdrawal, newState);

	}
	@Test(expected = IllegalStateException.class)
	public void testTransitionToState_Exception() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalStateEnum newState = WithdrawalStateEnum.DRAFT; // UTA: default value
		underTest.transitionToState(withdrawal, newState);

	}

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingState#applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyDefaultDataForEdit() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal14();
		WithdrawalRequest defaultVo = mockWithdrawalRequest9();
		underTest.applyDefaultDataForEdit(withdrawal, defaultVo);

	}
	@Test
	public void testApplyDefaultDataForEdit_1() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal14_1();
		WithdrawalRequest defaultVo = mockWithdrawalRequest9();
		underTest.applyDefaultDataForEdit(withdrawal, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate24() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.after(any(Date.class))).thenReturn(afterResult);

		boolean beforeResult = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult.before(any(Date.class))).thenReturn(beforeResult);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo2() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult).when(getParticipantInfoResult).getMoneyTypeAliases();
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest8() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan requestLoan = new WithdrawalRequestLoan();
		requestLoan.setLoanNo(10);
		getLoansResult.add(requestLoan);
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate24();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo2();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new LoanRecipient();
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();
		return getWithdrawalRequestResult;
	}
	private static WithdrawalRequest mockWithdrawalRequest8_1() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Collection<WithdrawalRequestLoan> getLoansResult = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		
		doReturn(getLoansResult).when(getWithdrawalRequestResult).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult = mockDate24();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo2();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		Recipient recipient = new LoanRecipient();
		getRecipientsResult.add(recipient);
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal14() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest8();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
	private static Withdrawal mockWithdrawal14_1() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest8_1();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate25() throws Throwable {
		Date getMostRecentPriorContributionDateResult2 = mock(Date.class);
		boolean afterResult2 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.after(any(Date.class))).thenReturn(afterResult2);

		boolean beforeResult2 = false; // UTA: default value
		when(getMostRecentPriorContributionDateResult2.before(any(Date.class))).thenReturn(beforeResult2);
		return getMostRecentPriorContributionDateResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo3() throws Throwable {
		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult2 = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult2).when(getParticipantInfoResult2).getMoneyTypeAliases();
		return getParticipantInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest9() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		when(defaultVo.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractNameResult2 = ""; // UTA: default value
		when(defaultVo.getContractName()).thenReturn(getContractNameResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(defaultVo.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(defaultVo.getLastName()).thenReturn(getLastNameResult2);

		Collection<WithdrawalRequestLoan> getLoansResult2 = new ArrayList<WithdrawalRequestLoan>(); // UTA: default value
		WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
		loan.setLoanNo(10);
		getLoansResult2.add(loan);
		doReturn(getLoansResult2).when(defaultVo).getLoans();

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();

		Date getMostRecentPriorContributionDateResult2 = mockDate25();
		when(defaultVo.getMostRecentPriorContributionDate()).thenReturn(getMostRecentPriorContributionDateResult2);

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo3();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(defaultVo.getParticipantSSN()).thenReturn(getParticipantSSNResult2);

		Collection<Recipient> getRecipientsResult2 = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult2).when(defaultVo).getRecipients();
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingState#applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyDefaultDataForView() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal15();
		WithdrawalRequest defaultVo = mockWithdrawalRequest11();
		underTest.applyDefaultDataForView(withdrawal, defaultVo);

	}
	@Test
	public void testApplyDefaultDataForView_1() throws Throwable {
		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal15();
		WithdrawalRequest defaultVo = mockWithdrawalRequest11_1();
		underTest.applyDefaultDataForView(withdrawal, defaultVo);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo4() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult).when(getParticipantInfoResult).getMoneyTypeAliases();
		return getParticipantInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest10() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

		String getLastNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getLastName()).thenReturn(getLastNameResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
		moneyType.setMoneyTypeId("Test");
		getMoneyTypesResult.add(moneyType);
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo4();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);
		return getWithdrawalRequestResult;
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal15() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest10();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
	

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo5() throws Throwable {
		ParticipantInfo getParticipantInfoResult2 = mock(ParticipantInfo.class);
		Map<String, String> getMoneyTypeAliasesResult2 = new HashMap<String, String>(); // UTA: default value
		doReturn(getMoneyTypeAliasesResult2).when(getParticipantInfoResult2).getMoneyTypeAliases();
		return getParticipantInfoResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest11() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		when(defaultVo.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractNameResult2 = ""; // UTA: default value
		when(defaultVo.getContractName()).thenReturn(getContractNameResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(defaultVo.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(defaultVo.getLastName()).thenReturn(getLastNameResult2);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
		moneyType.setMoneyTypeId("Test");
		getMoneyTypesResult2.add(moneyType);
		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo5();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(defaultVo.getParticipantSSN()).thenReturn(getParticipantSSNResult2);
		return defaultVo;
	}
	private static WithdrawalRequest mockWithdrawalRequest11_1() throws Throwable {
		WithdrawalRequest defaultVo = mock(WithdrawalRequest.class);
		ContractInfo getContractInfoResult2 = mock(ContractInfo.class);
		when(defaultVo.getContractInfo()).thenReturn(getContractInfoResult2);

		String getContractNameResult2 = ""; // UTA: default value
		when(defaultVo.getContractName()).thenReturn(getContractNameResult2);

		String getFirstNameResult2 = ""; // UTA: default value
		when(defaultVo.getFirstName()).thenReturn(getFirstNameResult2);

		String getLastNameResult2 = ""; // UTA: default value
		when(defaultVo.getLastName()).thenReturn(getLastNameResult2);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult2 = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value

		doReturn(getMoneyTypesResult2).when(defaultVo).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo5();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(defaultVo.getParticipantSSN()).thenReturn(getParticipantSSNResult2);
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.PendingState#delete(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testDelete() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);

		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal16();
		underTest.delete(withdrawal);

	}
	@Test
	public void testDelete_1() throws Throwable {
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);

		// Given
		PendingApprovalState underTest = new PendingApprovalState();

		// When
		Withdrawal withdrawal = mockWithdrawal16_1();
		underTest.delete(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp22() throws Throwable {
		Timestamp getLastSavedTimestampResult = mock(Timestamp.class);
		String toStringResult = ""; // UTA: default value
		when(getLastSavedTimestampResult.toString()).thenReturn(toStringResult);
		return getLastSavedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp23() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		String toStringResult2 = ""; // UTA: default value
		when(getApprovedTimestampResult.toString()).thenReturn(toStringResult2);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate26() throws Throwable {
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
	private static ContractInfo mockContractInfo4() throws Throwable {
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
	private static Timestamp mockTimestamp24() throws Throwable {
		Timestamp getCreatedResult = mock(Timestamp.class);
		String toStringResult5 = ""; // UTA: default value
		when(getCreatedResult.toString()).thenReturn(toStringResult5);
		return getCreatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp25() throws Throwable {
		Timestamp getCreatedResult2 = mock(Timestamp.class);
		String toStringResult6 = ""; // UTA: default value
		when(getCreatedResult2.toString()).thenReturn(toStringResult6);
		return getCreatedResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote7() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult2 = mockTimestamp25();
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
	private static Timestamp mockTimestamp26() throws Throwable {
		Timestamp getCreatedResult3 = mock(Timestamp.class);
		String toStringResult8 = ""; // UTA: default value
		when(getCreatedResult3.toString()).thenReturn(toStringResult8);
		return getCreatedResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote8() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
		Timestamp getCreatedResult3 = mockTimestamp26();
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
	private static Date mockDate27() throws Throwable {
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
	private static Date mockDate28() throws Throwable {
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
	private static Date mockDate29() throws Throwable {
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
	private static Date mockDate30() throws Throwable {
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
	private static FederalTaxVO mockFederalTaxVO3() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		String toStringResult14 = ""; // UTA: default value
		when(getFederalTaxVoResult.toString()).thenReturn(toStringResult14);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate31() throws Throwable {
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
	private static Timestamp mockTimestamp27() throws Throwable {
		Timestamp getCreatedResult4 = mock(Timestamp.class);
		String toStringResult16 = ""; // UTA: default value
		when(getCreatedResult4.toString()).thenReturn(toStringResult16);
		return getCreatedResult4;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo4() throws Throwable {
		LegaleseInfo getLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult2 = ""; // UTA: default value
		when(getLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult2);

		Integer getContentIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult);

		Timestamp getCreatedResult4 = mockTimestamp27();
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
	private static Date mockDate32() throws Throwable {
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
	private static Timestamp mockTimestamp28() throws Throwable {
		Timestamp getCreatedResult5 = mock(Timestamp.class);
		String toStringResult19 = ""; // UTA: default value
		when(getCreatedResult5.toString()).thenReturn(toStringResult19);
		return getCreatedResult5;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of LegaleseInfo
	 */
	private static LegaleseInfo mockLegaleseInfo5() throws Throwable {
		LegaleseInfo getParticipantLegaleseInfoResult = mock(LegaleseInfo.class);
		String getCmaSiteCodeResult3 = ""; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult3);

		Integer getContentIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getContentId()).thenReturn(getContentIdResult2);

		Timestamp getCreatedResult5 = mockTimestamp28();
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
	private static Date mockDate33() throws Throwable {
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
	private static Date mockDate34() throws Throwable {
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
	private static Timestamp mockTimestamp29() throws Throwable {
		Timestamp getSubmissionCaseLastUpdatedResult = mock(Timestamp.class);
		String toStringResult24 = ""; // UTA: default value
		when(getSubmissionCaseLastUpdatedResult.toString()).thenReturn(toStringResult24);
		return getSubmissionCaseLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate35() throws Throwable {
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
	private static WithdrawalRequest mockWithdrawalRequest12() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp23();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate26();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo4();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		Timestamp getCreatedResult = mockTimestamp24();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote7();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote8();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate27();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate28();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Collection<WithdrawalMessage> getErrorCodesResult3 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult3).when(getWithdrawalRequestResult).getErrorCodes();

		Date getExpectedProcessingDateResult = mockDate29();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate30();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO3();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate31();
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

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo4();
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

		Date getMostRecentPriorContributionDateResult = mockDate32();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo5();
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

		Date getRequestDateResult = mockDate33();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		Boolean getRequestInitiatedFromViewResult = true; // UTA: default value
		when(getWithdrawalRequestResult.getRequestInitiatedFromView()).thenReturn(getRequestInitiatedFromViewResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate34();
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

		Timestamp getSubmissionCaseLastUpdatedResult = mockTimestamp29();
		when(getWithdrawalRequestResult.getSubmissionCaseLastUpdated()).thenReturn(getSubmissionCaseLastUpdatedResult);

		Integer getSubmissionIdResult5 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult5);

		Date getTerminationDateResult = mockDate35();
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
	private static WithdrawalRequest mockWithdrawalRequest12_1() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp23();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate26();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo4();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		Timestamp getCreatedResult = mockTimestamp24();
		when(getWithdrawalRequestResult.getCreated()).thenReturn(getCreatedResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote7();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote8();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate27();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate28();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Collection<WithdrawalMessage> getErrorCodesResult3 = new ArrayList<WithdrawalMessage>(); // UTA: default value
		doReturn(getErrorCodesResult3).when(getWithdrawalRequestResult).getErrorCodes();

		Date getExpectedProcessingDateResult = mockDate29();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate30();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO3();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate31();
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

		LegaleseInfo getLegaleseInfoResult = mockLegaleseInfo4();
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

		Date getMostRecentPriorContributionDateResult = mockDate32();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo5();
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

		Date getRequestDateResult = mockDate33();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		Boolean getRequestInitiatedFromViewResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestInitiatedFromView()).thenReturn(getRequestInitiatedFromViewResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Date getRetirementDateResult = mockDate34();
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

		Timestamp getSubmissionCaseLastUpdatedResult = mockTimestamp29();
		when(getWithdrawalRequestResult.getSubmissionCaseLastUpdated()).thenReturn(getSubmissionCaseLastUpdatedResult);

		Integer getSubmissionIdResult5 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult5);

		Date getTerminationDateResult = mockDate35();
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
	private static Withdrawal mockWithdrawal16() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp22();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest12();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
	private static Withdrawal mockWithdrawal16_1() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		Timestamp getLastSavedTimestampResult = mockTimestamp22();
		when(withdrawal.getLastSavedTimestamp()).thenReturn(getLastSavedTimestampResult);

		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest12_1();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}
}