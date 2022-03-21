/**
 * 
 */
package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome;
import com.manulife.pension.service.withdrawal.WithdrawalServiceUtil;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
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
import javax.naming.NamingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for ApprovedState
 *
 * @see com.manulife.pension.service.withdrawal.domain.ApprovedState
 * @author patelpo
 */
@PrepareForTest({ WithdrawalRequest.class, Principal.class, ActivityDetailDao.class, WithdrawalServiceUtil.class,
		WithdrawalServiceDelegate.class, ActivitySummaryDao.class, ActivityDynamicDetailDao.class,
		ActivityHistoryHelper.class })
@RunWith(PowerMockRunner.class)
public class ApprovedStateTest {

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalStateEnum()
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.ApprovedState#getWithdrawalStateEnum()
	 * @author patelpo
	 */
	@Test
	public void testGetWithdrawalStateEnum() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		WithdrawalStateEnum result = underTest.getWithdrawalStateEnum();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for processApproved(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.ApprovedState#processApproved(Withdrawal)
	 * @author patelpo
	 */
	@Test
	public void testProcessApproved() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mockWithdrawal();
		underTest.processApproved(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp() throws Throwable {
		Timestamp getApprovedTimestampResult = mock(Timestamp.class);
		return getApprovedTimestampResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getBirthDateResult = mock(Date.class);
		long getTimeResult = 0L; // UTA: default value
		when(getBirthDateResult.getTime()).thenReturn(getTimeResult);
		return getBirthDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractInfo
	 */
	private static ContractInfo mockContractInfo() throws Throwable {
		ContractInfo getContractInfoResult = mock(ContractInfo.class);
		String getClientAccountRepEmailResult = ""; // UTA: default value
		when(getContractInfoResult.getClientAccountRepEmail()).thenReturn(getClientAccountRepEmailResult);

		Boolean getMailChequeToAddressIndicatorResult = false; // UTA: default value
		when(getContractInfoResult.getMailChequeToAddressIndicator()).thenReturn(getMailChequeToAddressIndicatorResult);
		return getContractInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mock(WithdrawalRequestNote.class);
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
		return getCurrentAdminToAdminNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequestNote
	 */
	private static WithdrawalRequestNote mockWithdrawalRequestNote2() throws Throwable {
		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mock(WithdrawalRequestNote.class);
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
		return getCurrentAdminToParticipantNoteResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getDeathDateResult = mock(Date.class);
		long getTimeResult2 = 0L; // UTA: default value
		when(getDeathDateResult.getTime()).thenReturn(getTimeResult2);
		return getDeathDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate3() throws Throwable {
		Date getDisabilityDateResult = mock(Date.class);
		long getTimeResult3 = 0L; // UTA: default value
		when(getDisabilityDateResult.getTime()).thenReturn(getTimeResult3);
		return getDisabilityDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate4() throws Throwable {
		Date getExpectedProcessingDateResult = mock(Date.class);
		long getTimeResult4 = 0L; // UTA: default value
		when(getExpectedProcessingDateResult.getTime()).thenReturn(getTimeResult4);
		return getExpectedProcessingDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate5() throws Throwable {
		Date getExpirationDateResult = mock(Date.class);
		long getTimeResult5 = 0L; // UTA: default value
		when(getExpirationDateResult.getTime()).thenReturn(getTimeResult5);
		return getExpirationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of FederalTaxVO
	 */
	private static FederalTaxVO mockFederalTaxVO() throws Throwable {
		FederalTaxVO getFederalTaxVoResult = mock(FederalTaxVO.class);
		return getFederalTaxVoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate6() throws Throwable {
		Date getFinalContributionDateResult = mock(Date.class);
		long getTimeResult6 = 0L; // UTA: default value
		when(getFinalContributionDateResult.getTime()).thenReturn(getTimeResult6);
		return getFinalContributionDateResult;
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

		Integer getCreatorUserProfileIdResult = 0; // UTA: default value
		when(getLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult);

		Integer getLastUpdatedByIdResult4 = 0; // UTA: default value
		when(getLegaleseInfoResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult4);

		Integer getSubmissionIdResult3 = 0; // UTA: default value
		when(getLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult3);
		return getLegaleseInfoResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate7() throws Throwable {
		Date getMostRecentPriorContributionDateResult = mock(Date.class);
		long getTimeResult7 = 0L; // UTA: default value
		when(getMostRecentPriorContributionDateResult.getTime()).thenReturn(getTimeResult7);
		return getMostRecentPriorContributionDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantInfo
	 */
	private static ParticipantInfo mockParticipantInfo() throws Throwable {
		ParticipantInfo getParticipantInfoResult = mock(ParticipantInfo.class);
		String getManulifeCompanyIdResult = ""; // UTA: default value
		when(getParticipantInfoResult.getManulifeCompanyId()).thenReturn(getManulifeCompanyIdResult);
		return getParticipantInfoResult;
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

		Integer getCreatorUserProfileIdResult2 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getCreatorUserProfileId()).thenReturn(getCreatorUserProfileIdResult2);

		Integer getLastUpdatedByIdResult5 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult5);

		Integer getSubmissionIdResult4 = 0; // UTA: default value
		when(getParticipantLegaleseInfoResult.getSubmissionId()).thenReturn(getSubmissionIdResult4);
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
	private static Date mockDate8() throws Throwable {
		Date getRequestDateResult = mock(Date.class);
		long getTimeResult8 = 0L; // UTA: default value
		when(getRequestDateResult.getTime()).thenReturn(getTimeResult8);
		return getRequestDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate9() throws Throwable {
		Date getRetirementDateResult = mock(Date.class);
		long getTimeResult9 = 0L; // UTA: default value
		when(getRetirementDateResult.getTime()).thenReturn(getTimeResult9);
		return getRetirementDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate10() throws Throwable {
		Date getTerminationDateResult = mock(Date.class);
		long getTimeResult10 = 0L; // UTA: default value
		when(getTerminationDateResult.getTime()).thenReturn(getTimeResult10);
		return getTerminationDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Timestamp getApprovedTimestampResult = mockTimestamp();
		when(getWithdrawalRequestResult.getApprovedTimestamp()).thenReturn(getApprovedTimestampResult);

		Date getBirthDateResult = mockDate();
		when(getWithdrawalRequestResult.getBirthDate()).thenReturn(getBirthDateResult);

		String getCmaSiteCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getCmaSiteCode()).thenReturn(getCmaSiteCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		ContractInfo getContractInfoResult = mockContractInfo();
		when(getWithdrawalRequestResult.getContractInfo()).thenReturn(getContractInfoResult);

		String getContractNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getContractName()).thenReturn(getContractNameResult);

		WithdrawalRequestNote getCurrentAdminToAdminNoteResult = mockWithdrawalRequestNote();
		when(getWithdrawalRequestResult.getCurrentAdminToAdminNote()).thenReturn(getCurrentAdminToAdminNoteResult);

		WithdrawalRequestNote getCurrentAdminToParticipantNoteResult = mockWithdrawalRequestNote2();
		when(getWithdrawalRequestResult.getCurrentAdminToParticipantNote())
				.thenReturn(getCurrentAdminToParticipantNoteResult);

		Date getDeathDateResult = mockDate2();
		when(getWithdrawalRequestResult.getDeathDate()).thenReturn(getDeathDateResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Date getDisabilityDateResult = mockDate3();
		when(getWithdrawalRequestResult.getDisabilityDate()).thenReturn(getDisabilityDateResult);

		Integer getEmployeeProfileIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getEmployeeProfileId()).thenReturn(getEmployeeProfileIdResult);

		Date getExpectedProcessingDateResult = mockDate4();
		when(getWithdrawalRequestResult.getExpectedProcessingDate()).thenReturn(getExpectedProcessingDateResult);

		Date getExpirationDateResult = mockDate5();
		when(getWithdrawalRequestResult.getExpirationDate()).thenReturn(getExpirationDateResult);

		FederalTaxVO getFederalTaxVoResult = mockFederalTaxVO();
		when(getWithdrawalRequestResult.getFederalTaxVo()).thenReturn(getFederalTaxVoResult);

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		Date getFinalContributionDateResult = mockDate6();
		when(getWithdrawalRequestResult.getFinalContributionDate()).thenReturn(getFinalContributionDateResult);

		String getFirstNameResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getFirstName()).thenReturn(getFirstNameResult);

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

		Integer getLastUpdatedByIdResult3 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult3);

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

		Date getMostRecentPriorContributionDateResult = mockDate7();
		when(getWithdrawalRequestResult.getMostRecentPriorContributionDate())
				.thenReturn(getMostRecentPriorContributionDateResult);

		Boolean getPartWithPbaMoneyIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getPartWithPbaMoneyInd()).thenReturn(getPartWithPbaMoneyIndResult);

		Integer getParticipantIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantId()).thenReturn(getParticipantIdResult);

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		Boolean getParticipantLeavingPlanIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantLeavingPlanInd()).thenReturn(getParticipantLeavingPlanIndResult);

		LegaleseInfo getParticipantLegaleseInfoResult = mockLegaleseInfo2();
		when(getWithdrawalRequestResult.getParticipantLegaleseInfo()).thenReturn(getParticipantLegaleseInfoResult);

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
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		Date getRequestDateResult = mockDate8();
		when(getWithdrawalRequestResult.getRequestDate()).thenReturn(getRequestDateResult);

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		String getRequestTypeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getRequestType()).thenReturn(getRequestTypeResult);

		Date getRetirementDateResult = mockDate9();
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

		Integer getSubmissionIdResult5 = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult5);

		Date getTerminationDateResult = mockDate10();
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
	private static Withdrawal mockWithdrawal() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for transitionToState(Withdrawal, WithdrawalStateEnum)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.ApprovedState#transitionToState(Withdrawal, WithdrawalStateEnum)
	 * @author patelpo
	 */
	@Test
	public void testTransitionToState() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalStateEnum newState = WithdrawalStateEnum.READY_FOR_ENTRY; // UTA: default value
		underTest.transitionToState(withdrawal, newState);

	}

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.EndState#applyDefaultDataForView(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test
	public void testApplyDefaultDataForView() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mockWithdrawal2();
		WithdrawalRequest defaultVo = mockWithdrawalRequest3();
		underTest.applyDefaultDataForView(withdrawal, defaultVo);

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
	private static WithdrawalRequest mockWithdrawalRequest2() throws Throwable {
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
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		ParticipantInfo getParticipantInfoResult = mockParticipantInfo2();
		when(getWithdrawalRequestResult.getParticipantInfo()).thenReturn(getParticipantInfoResult);

		String getParticipantSSNResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getParticipantSSN()).thenReturn(getParticipantSSNResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal2() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest2();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
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
	private static WithdrawalRequest mockWithdrawalRequest3() throws Throwable {
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

		ParticipantInfo getParticipantInfoResult2 = mockParticipantInfo3();
		when(defaultVo.getParticipantInfo()).thenReturn(getParticipantInfoResult2);

		String getParticipantSSNResult2 = ""; // UTA: default value
		when(defaultVo.getParticipantSSN()).thenReturn(getParticipantSSNResult2);
		return defaultVo;
	}

	/**
	 * Parasoft Jtest UTA: Test for getSystemOfRecordValues(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.EndState#getSystemOfRecordValues(Withdrawal)
	 * @author patelpo
	 */
/*	@Test
	public void testGetSystemOfRecordValues() throws Throwable {
		Object[] parameters = new Object[1];

		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(parameters);

		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mockWithdrawal3();
		SystemOfRecordValues result = underTest.getSystemOfRecordValues(withdrawal);

		// Then
		// assertNotNull(result);
	}
*/
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal2() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		long getProfileIdResult = 0L; // UTA: default value
		when(getPrincipalResult.getProfileId()).thenReturn(getProfileIdResult);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest4() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		Principal getPrincipalResult = mockPrincipal2();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal3() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest4();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#applyDefaultDataForEdit(Withdrawal, WithdrawalRequest)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testApplyDefaultDataForEdit() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest withdrawalRequest = mock(WithdrawalRequest.class);
		underTest.applyDefaultDataForEdit(withdrawal, withdrawalRequest);

	}

	/**
	 * Parasoft Jtest UTA: Test for approve(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#approve(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testApprove() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.approve(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for cancel(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#cancel(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testCancel() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.cancel(withdrawal);

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
		ApprovedState underTest = new ApprovedState();

		// When
		Object result = underTest.clone();

		// Then
		// assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#delete(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testDelete() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.delete(withdrawal);

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
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.deny(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for expire(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#expire(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testExpire() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.expire(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for readActivityHistory(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#readActivityHistory(Withdrawal)
	 * @author patelpo
	 */
/*	@Test
	public void testReadActivityHistory() throws Throwable {
		spy(ActivityHistoryHelper.class);

		Collection<Activity> getWithdrawalActivitiesResult = new ArrayList<Activity>(); // UTA: default value
		Activity activity = new Activity();
		getWithdrawalActivitiesResult.add(activity);
		ActivityHistoryHelper helper=mock(ActivityHistoryHelper.class);
		doReturn(getWithdrawalActivitiesResult).when(ActivityHistoryHelper.class,"getWithdrawalActivities",(any(Withdrawal.class)), (List) any(), (Map) any(),
				any(WithdrawalStateEnum.class));
		List<WithdrawalActivityDetail> details = new ArrayList<WithdrawalActivityDetail>();
		Map<WithdrawalFieldDef, String> withdrawalSORValues = new HashMap<WithdrawalFieldDef, String>();
		
		
		Object[] tempList = new Object[1];
		ActivitySummary ac = new LoanActivitySummary();
		tempList[0] = ac;
		Object[] tempList1 = new Object[1];
		tempList1[0] = tempList;
		
		Object[] actList = new Object[1];
		ActivityDetail wad = new WithdrawalActivityDetail();
		wad.setItemNumber(100);
		actList[0] = wad;
		Object[] actList1 = new Object[1];
		actList1[0] = actList;
		
		Object[] addList = new Object[1];
		ActivityDynamicDetail add = new ActivityDynamicDetail();
		add.setSecondaryNumber(10);
		add.setItemNumber(100);
		addList[0] = add;
		Object[] addList1 = new Object[1];
		addList1[0] = addList;
		
		StoredProcedureHandler newStoredProcedureHandlerResult = mock(StoredProcedureHandler.class); // UTA: default value
		whenNew(StoredProcedureHandler.class).withAnyArguments().thenReturn(newStoredProcedureHandlerResult);
		when(newStoredProcedureHandlerResult.execute(any(Object[].class))).thenReturn(tempList1,actList1, addList1,actList1,addList1);

		spy(WithdrawalServiceDelegate.class);

		ContractInfo contractInfo = new ContractInfo();
		WithdrawalServiceDelegate getInstanceResult = mock(WithdrawalServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(WithdrawalServiceDelegate.class, "getInstance");
		when(getInstanceResult.getContractInfo(any(Integer.class), any(Principal.class))).thenReturn(contractInfo);

		spy(WithdrawalServiceUtil.class);

		WithdrawalServiceLocalHome getLocalHomeResult = mock(WithdrawalServiceLocalHome.class); // UTA: default value
		doReturn(getLocalHomeResult).when(WithdrawalServiceUtil.class, "getLocalHome");

		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mockWithdrawal4();
		ActivityHistory result = underTest.readActivityHistory(withdrawal);

		// Then
		// assertNotNull(result);
	}
*/
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Timestamp
	 */
	private static Timestamp mockTimestamp2() throws Throwable {
		Timestamp getLastUpdatedResult = mock(Timestamp.class);
		int getNanosResult = 0; // UTA: default value
		when(getLastUpdatedResult.getNanos()).thenReturn(getNanosResult);

		long getTimeResult = 0L; // UTA: default value
		when(getLastUpdatedResult.getTime()).thenReturn(getTimeResult);
		return getLastUpdatedResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserRole
	 */
	private static UserRole mockUserRole() throws Throwable {
		UserRole getRoleResult = mock(UserRole.class);
		boolean hasPermissionResult = true; // UTA: default value
		when(getRoleResult.hasPermission(any(PermissionType.class))).thenReturn(hasPermissionResult);
		return getRoleResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Principal
	 */
	private static Principal mockPrincipal3() throws Throwable {
		Principal getPrincipalResult = mock(Principal.class);
		long getProfileIdResult = 0L; // UTA: default value
		when(getPrincipalResult.getProfileId()).thenReturn(getProfileIdResult);

		UserRole getRoleResult = mockUserRole();
		when(getPrincipalResult.getRole()).thenReturn(getRoleResult);
		return getPrincipalResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalRequest
	 */
	private static WithdrawalRequest mockWithdrawalRequest5() throws Throwable {
		WithdrawalRequest getWithdrawalRequestResult = mock(WithdrawalRequest.class);
		String getAmountTypeCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getAmountTypeCode()).thenReturn(getAmountTypeCodeResult);

		Integer getContractIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getContractId()).thenReturn(getContractIdResult);

		Collection<Declaration> getDeclarationsResult = new ArrayList<Declaration>(); // UTA: default value
		doReturn(getDeclarationsResult).when(getWithdrawalRequestResult).getDeclarations();

		Collection<Fee> getFeesResult = new ArrayList<Fee>(); // UTA: default value
		doReturn(getFeesResult).when(getWithdrawalRequestResult).getFees();

		boolean getIsParticipantCreatedResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getIsParticipantCreated()).thenReturn(getIsParticipantCreatedResult);

		Timestamp getLastUpdatedResult = mockTimestamp2();
		when(getWithdrawalRequestResult.getLastUpdated()).thenReturn(getLastUpdatedResult);

		Integer getLastUpdatedByIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getLastUpdatedById()).thenReturn(getLastUpdatedByIdResult);

		Collection<WithdrawalRequestMoneyType> getMoneyTypesResult = new ArrayList<WithdrawalRequestMoneyType>(); // UTA: default value
		doReturn(getMoneyTypesResult).when(getWithdrawalRequestResult).getMoneyTypes();

		String getPaymentToResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getPaymentTo()).thenReturn(getPaymentToResult);

		Principal getPrincipalResult = mockPrincipal3();
		when(getWithdrawalRequestResult.getPrincipal()).thenReturn(getPrincipalResult);

		String getReasonCodeResult = ""; // UTA: default value
		when(getWithdrawalRequestResult.getReasonCode()).thenReturn(getReasonCodeResult);

		Collection<Recipient> getRecipientsResult = new ArrayList<Recipient>(); // UTA: default value
		doReturn(getRecipientsResult).when(getWithdrawalRequestResult).getRecipients();

		boolean getRequestRiskIndicatorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getRequestRiskIndicator()).thenReturn(getRequestRiskIndicatorResult);

		Integer getSubmissionIdResult = 0; // UTA: default value
		when(getWithdrawalRequestResult.getSubmissionId()).thenReturn(getSubmissionIdResult);

		Boolean getVestingCalledIndResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCalledInd()).thenReturn(getVestingCalledIndResult);

		Boolean getVestingCriticalErrorResult = false; // UTA: default value
		when(getWithdrawalRequestResult.getVestingCriticalError()).thenReturn(getVestingCriticalErrorResult);

		boolean isDeclarationAvailableResult = false; // UTA: default value
		when(getWithdrawalRequestResult.isDeclarationAvailable(anyString())).thenReturn(isDeclarationAvailableResult);
		return getWithdrawalRequestResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Withdrawal
	 */
	private static Withdrawal mockWithdrawal4() throws Throwable {
		Withdrawal withdrawal = mock(Withdrawal.class);
		WithdrawalRequest getWithdrawalRequestResult = mockWithdrawalRequest5();
		when(withdrawal.getWithdrawalRequest()).thenReturn(getWithdrawalRequestResult);
		return withdrawal;
	}

	/**
	 * Parasoft Jtest UTA: Test for save(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#save(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testSave() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.save(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#sendForApproval(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testSendForApproval() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.sendForApproval(withdrawal);

	}

	/**
	 * Parasoft Jtest UTA: Test for sendForReview(Withdrawal)
	 *
	 * @see com.manulife.pension.service.withdrawal.domain.DefaultWithdrawalState#sendForReview(Withdrawal)
	 * @author patelpo
	 */
	@Test(expected = IllegalStateException.class)
	public void testSendForReview() throws Throwable {
		// Given
		ApprovedState underTest = new ApprovedState();

		// When
		Withdrawal withdrawal = mock(Withdrawal.class);
		underTest.sendForReview(withdrawal);

	}
}