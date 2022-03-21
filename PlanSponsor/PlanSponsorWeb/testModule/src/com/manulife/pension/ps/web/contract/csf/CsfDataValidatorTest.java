/**
 * 
 */
package com.manulife.pension.ps.web.contract.csf;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.service.plan.valueobject.IrsAnnualMaximums;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;

/**
 * Parasoft Jtest UTA: Test class for CsfDataValidator
 *
 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator
 * @author ashoksu
 */
@PrepareForTest({ContractServiceDelegate.class})
@RunWith(PowerMockRunner.class)
public class CsfDataValidatorTest {
	
	private CsfDataValidator csfDataValidator;

	private static final String TYRET = "tyret";
	private static final String AVAIL_DATE = "12/21/2019";
	/**
	 * Parasoft Jtest UTA: Test for determineSignupMethod(CsfForm, PlanDataLite)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#determineSignupMethod(CsfForm, PlanDataLite)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	
	@Test
	public void testDetermineSignupMethod() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm_sign();
		PlanDataLite planDataLite = mockPlanDataLite();
		String result = underTest.determineSignupMethod(csfForm, planDataLite);

		// Then
		assertEquals("A", result);
	}
	
	
	/**
	 * Parasoft Jtest UTA: Test for determineSignupMethod(CsfForm, PlanDataLite)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#determineSignupMethod(CsfForm, PlanDataLite)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testDetermineSignupMethod1() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm_signNo();
		PlanDataLite planDataLite = mockPlanDataLite_N();
		String result = underTest.determineSignupMethod(csfForm, planDataLite);

		// Then
		assertEquals("S", result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		String getParticipantInitiateLoansInd =TYRET;
		when(csfForm.getParticipantInitiateLoansInd()).thenReturn(getParticipantInitiateLoansInd);
		String getManagedAccountServiceAvailabilityDate = AVAIL_DATE;
		when(csfForm.getManagedAccountServiceAvailabilityDate()).thenReturn(getManagedAccountServiceAvailabilityDate);
		String getNextBusinessDate =  "12/22/2019";
		when(csfForm.getNextBusinessDate()).thenReturn(getNextBusinessDate);
		String getManagedAccountService = "mas1";
		when(csfForm.getManagedAccountService()).thenReturn(getManagedAccountService);
		ParticipantServicesData participantServicesData = mock(ParticipantServicesData.class);
		when(csfForm.getParticipantServicesData()).thenReturn(participantServicesData);
		when(participantServicesData.isPlanHasManagedAccount()).thenReturn(false);
		return csfForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	@SuppressWarnings("static-access")
	private static CsfForm mockCsfForm_ma() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);
		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		String getParticipantInitiateLoansInd =TYRET;
		when(csfForm.getParticipantInitiateLoansInd()).thenReturn(getParticipantInitiateLoansInd);
		String getManagedAccountServiceAvailabilityDate = AVAIL_DATE;
		when(csfForm.getManagedAccountServiceAvailabilityDate()).thenReturn(getManagedAccountServiceAvailabilityDate);
		String getNextBusinessDate =  "12/20/2019";
		when(csfForm.getNextBusinessDate()).thenReturn(getNextBusinessDate);
		String getManagedAccountService = "mas";
		when(csfForm.getManagedAccountService()).thenReturn(getManagedAccountService);
		ParticipantServicesData participantServicesData = mock(ParticipantServicesData.class);
		when(csfForm.getParticipantServicesData()).thenReturn(participantServicesData);
		when(participantServicesData.isPlanHasManagedAccount()).thenReturn(false);
//		CsfForm form = mock(CsfForm.class);
		when(csfForm.getClonedForm()).thenReturn(csfForm);
		return csfForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	@SuppressWarnings("static-access")
	private static CsfForm mockCsfForm_maf() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);
		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		String getParticipantInitiateLoansInd =TYRET;
		when(csfForm.getParticipantInitiateLoansInd()).thenReturn(getParticipantInitiateLoansInd);
		String getManagedAccountServiceAvailabilityDate = AVAIL_DATE;
		when(csfForm.getManagedAccountServiceAvailabilityDate()).thenReturn(getManagedAccountServiceAvailabilityDate);
		String getNextBusinessDate =  "12/22/2019";
		when(csfForm.getNextBusinessDate()).thenReturn(getNextBusinessDate);
		String getManagedAccountService = "mas";
		when(csfForm.getManagedAccountService()).thenReturn(getManagedAccountService);
		ParticipantServicesData participantServicesData = mock(ParticipantServicesData.class);
		when(csfForm.getParticipantServicesData()).thenReturn(participantServicesData);
		when(participantServicesData.isPlanHasManagedAccount()).thenReturn(false);
//		CsfForm form = mock(CsfForm.class);
		when(csfForm.getClonedForm()).thenReturn(csfForm);
		return csfForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm_sign() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = "Yes"; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = "Yes"; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm_signNo() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = "No"; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = "Yes"; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = "Y"; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);
		return planDataLite;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite_N() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = "N"; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);
		return planDataLite;
	}

	/**
	 * Parasoft Jtest UTA: Test for validateCSFAgainstPlanData(CsfForm, PlanDataLite, WithdrawalDistributionMethod, Collection)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validateCSFAgainstPlanData(CsfForm, PlanDataLite, WithdrawalDistributionMethod, Collection)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidateCSFAgainstPlanData() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm2();
		PlanDataLite planDataLite = mockPlanDataLite2();
		WithdrawalDistributionMethod withdrawalDistributionMethod = mockWithdrawalDistributionMethod();
		Collection<GenericException> errors = new ArrayList<>(); // UTA: default value
		GenericException item4 = mock(GenericException.class);
		errors.add(item4);
		underTest.validateCSFAgainstPlanData(csfForm, planDataLite, withdrawalDistributionMethod, errors);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm2() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAciAnniversaryDateResult = ""; // UTA: default value
		when(csfForm.getAciAnniversaryDate()).thenReturn(getAciAnniversaryDateResult);

		String getAciAnniversaryYearResult = ""; // UTA: default value
		when(csfForm.getAciAnniversaryYear()).thenReturn(getAciAnniversaryYearResult);

		String getAciSignupMethodResult = ""; // UTA: default value
		when(csfForm.getAciSignupMethod()).thenReturn(getAciSignupMethodResult);

		String getAllowOnlineLoansResult = ""; // UTA: default value
		when(csfForm.getAllowOnlineLoans()).thenReturn(getAllowOnlineLoansResult);

		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getAutoEnrollIndResult = ""; // UTA: default value
		when(csfForm.getAutoEnrollInd()).thenReturn(getAutoEnrollIndResult);

		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);

		List<MoneyTypeVO> getContractMoneyTypesListResult = new ArrayList<>(); // UTA: default value
		MoneyTypeVO item = mock(MoneyTypeVO.class);
		getContractMoneyTypesListResult.add(item);
		doReturn(getContractMoneyTypesListResult).when(csfForm).getContractMoneyTypesList();

		String getDeferralLimitDollarsResult = ""; // UTA: default value
		when(csfForm.getDeferralLimitDollars()).thenReturn(getDeferralLimitDollarsResult);

		String getDeferralLimitPercentResult = ""; // UTA: default value
		when(csfForm.getDeferralLimitPercent()).thenReturn(getDeferralLimitPercentResult);

		String getDeferralMaxLimitDollarsResult = ""; // UTA: default value
		when(csfForm.getDeferralMaxLimitDollars()).thenReturn(getDeferralMaxLimitDollarsResult);

		String getDeferralMaxLimitPercentResult = ""; // UTA: default value
		when(csfForm.getDeferralMaxLimitPercent()).thenReturn(getDeferralMaxLimitPercentResult);

		String getDeferralTypeResult = ""; // UTA: default value
		when(csfForm.getDeferralType()).thenReturn(getDeferralTypeResult);

		String getEnrollOnlineResult = ""; // UTA: default value
		when(csfForm.getEnrollOnline()).thenReturn(getEnrollOnlineResult);

		String getInitialEnrollmentDateResult = ""; // UTA: default value
		when(csfForm.getInitialEnrollmentDate()).thenReturn(getInitialEnrollmentDateResult);

		String getLoanRecordKeepingIndResult = ""; // UTA: default value
		when(csfForm.getLoanRecordKeepingInd()).thenReturn(getLoanRecordKeepingIndResult);

		List<MoneyTypeEligibilityCriterion> getMoneyTypeEligibilityCriteriaResult = new ArrayList<>(); // UTA: default value
		MoneyTypeEligibilityCriterion item2 = mock(MoneyTypeEligibilityCriterion.class);
		getMoneyTypeEligibilityCriteriaResult.add(item2);
		doReturn(getMoneyTypeEligibilityCriteriaResult).when(csfForm).getMoneyTypeEligibilityCriteria();

		String getParticipantInitiateLoansIndResult = ""; // UTA: default value
		when(csfForm.getParticipantInitiateLoansInd()).thenReturn(getParticipantInitiateLoansIndResult);

		String getParticipantWithdrawalIndResult = ""; // UTA: default value
		when(csfForm.getParticipantWithdrawalInd()).thenReturn(getParticipantWithdrawalIndResult);

		String getSummaryPlanHighlightAvailableResult = ""; // UTA: default value
		when(csfForm.getSummaryPlanHighlightAvailable()).thenReturn(getSummaryPlanHighlightAvailableResult);

		String getVestingPercentagesMethodResult = ""; // UTA: default value
		when(csfForm.getVestingPercentagesMethod()).thenReturn(getVestingPercentagesMethodResult);

		String toStringResult = ""; // UTA: default value
		when(csfForm.toString()).thenReturn(toStringResult);
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getAciEffectiveDateResult = mock(Date.class);
		boolean beforeResult = false; // UTA: default value
		when(getAciEffectiveDateResult.before(nullable(Date.class))).thenReturn(beforeResult);

		long getTimeResult = 0L; // UTA: default value
		when(getAciEffectiveDateResult.getTime()).thenReturn(getTimeResult);

		String toStringResult2 = ""; // UTA: default value
		when(getAciEffectiveDateResult.toString()).thenReturn(toStringResult2);
		return getAciEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getAutomaticEnrollmentEffectiveDateResult = mock(Date.class);
		boolean beforeResult2 = false; // UTA: default value
		when(getAutomaticEnrollmentEffectiveDateResult.before(nullable(Date.class))).thenReturn(beforeResult2);

		long getTimeResult2 = 0L; // UTA: default value
		when(getAutomaticEnrollmentEffectiveDateResult.getTime()).thenReturn(getTimeResult2);

		String toStringResult3 = ""; // UTA: default value
		when(getAutomaticEnrollmentEffectiveDateResult.toString()).thenReturn(toStringResult3);
		return getAutomaticEnrollmentEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of IrsAnnualMaximums
	 */
	private static IrsAnnualMaximums mockIrsAnnualMaximums() throws Throwable {
		IrsAnnualMaximums getIrsAnnualMaximumsResult = mock(IrsAnnualMaximums.class);
		BigDecimal getIrsAnnualRegularMaximumAmountResult = BigDecimal.ZERO; // UTA: default value
		when(getIrsAnnualMaximumsResult.getIrsAnnualRegularMaximumAmount())
				.thenReturn(getIrsAnnualRegularMaximumAmountResult);

		String toStringResult4 = ""; // UTA: default value
		when(getIrsAnnualMaximumsResult.toString()).thenReturn(toStringResult4);
		return getIrsAnnualMaximumsResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanEmployeeDeferralElection
	 */
	private static PlanEmployeeDeferralElection mockPlanEmployeeDeferralElection() throws Throwable {
		PlanEmployeeDeferralElection getPlanEmployeeDeferralElectionResult = mock(PlanEmployeeDeferralElection.class);
		String getEmployeeDeferralElectionCodeResult = ""; // UTA: default value
		when(getPlanEmployeeDeferralElectionResult.getEmployeeDeferralElectionCode())
				.thenReturn(getEmployeeDeferralElectionCodeResult);

		String toStringResult5 = ""; // UTA: default value
		when(getPlanEmployeeDeferralElectionResult.toString()).thenReturn(toStringResult5);
		return getPlanEmployeeDeferralElectionResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite2() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = ""; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);

		Date getAciEffectiveDateResult = mockDate();
		when(planDataLite.getAciEffectiveDate()).thenReturn(getAciEffectiveDateResult);

		String getAutomaticEnrollmentAllowedResult = ""; // UTA: default value
		when(planDataLite.getAutomaticEnrollmentAllowed()).thenReturn(getAutomaticEnrollmentAllowedResult);

		Date getAutomaticEnrollmentEffectiveDateResult = mockDate2();
		when(planDataLite.getAutomaticEnrollmentEffectiveDate()).thenReturn(getAutomaticEnrollmentEffectiveDateResult);

		String getCatchUpContributionsAllowedResult = ""; // UTA: default value
		when(planDataLite.getCatchUpContributionsAllowed()).thenReturn(getCatchUpContributionsAllowedResult);

		Boolean getDeferralIrsAppliesResult = false; // UTA: default value
		when(planDataLite.getDeferralIrsApplies()).thenReturn(getDeferralIrsAppliesResult);

		Integer getDeferralMaxAmountResult = 0; // UTA: default value
		when(planDataLite.getDeferralMaxAmount()).thenReturn(getDeferralMaxAmountResult);

		BigDecimal getDeferralMaxPercentResult = BigDecimal.ZERO; // UTA: default value
		when(planDataLite.getDeferralMaxPercent()).thenReturn(getDeferralMaxPercentResult);

		IrsAnnualMaximums getIrsAnnualMaximumsResult = mockIrsAnnualMaximums();
		when(planDataLite.getIrsAnnualMaximums()).thenReturn(getIrsAnnualMaximumsResult);

		BigDecimal getLoanInterestRateAbovePrimeResult = BigDecimal.ZERO; // UTA: default value
		when(planDataLite.getLoanInterestRateAbovePrime()).thenReturn(getLoanInterestRateAbovePrimeResult);

		String getLoansAllowedIndResult = ""; // UTA: default value
		when(planDataLite.getLoansAllowedInd()).thenReturn(getLoansAllowedIndResult);

		BigDecimal getMaximumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(planDataLite.getMaximumLoanAmount()).thenReturn(getMaximumLoanAmountResult);

		BigDecimal getMaximumLoanPctResult = BigDecimal.ZERO; // UTA: default value
		when(planDataLite.getMaximumLoanPct()).thenReturn(getMaximumLoanPctResult);

		Integer getMaximumNumberofOutstandingLoansResult = 0; // UTA: default value
		when(planDataLite.getMaximumNumberofOutstandingLoans()).thenReturn(getMaximumNumberofOutstandingLoansResult);

		BigDecimal getMinimumLoanAmountResult = BigDecimal.ZERO; // UTA: default value
		when(planDataLite.getMinimumLoanAmount()).thenReturn(getMinimumLoanAmountResult);

		String getMultipleEligibilityRulesForOneSingleMoneyTypeResult = ""; // UTA: default value
		when(planDataLite.getMultipleEligibilityRulesForOneSingleMoneyType())
				.thenReturn(getMultipleEligibilityRulesForOneSingleMoneyTypeResult);

		String getMultipleVestingSchedulesForOneSingleMoneyTypeResult = ""; // UTA: default value
		when(planDataLite.getMultipleVestingSchedulesForOneSingleMoneyType())
				.thenReturn(getMultipleVestingSchedulesForOneSingleMoneyTypeResult);

		PlanEmployeeDeferralElection getPlanEmployeeDeferralElectionResult = mockPlanEmployeeDeferralElection();
		when(planDataLite.getPlanEmployeeDeferralElection()).thenReturn(getPlanEmployeeDeferralElectionResult);

		String getPlanEntryFrequencyForMoneyTypeEedefResult = ""; // UTA: default value
		when(planDataLite.getPlanEntryFrequencyForMoneyTypeEedef())
				.thenReturn(getPlanEntryFrequencyForMoneyTypeEedefResult);

		String getRequiresSpousalConsentForDistributionsResult = ""; // UTA: default value
		when(planDataLite.getRequiresSpousalConsentForDistributions())
				.thenReturn(getRequiresSpousalConsentForDistributionsResult);

		String getRolloversDelayedUntilEligibilityReqtMetResult = ""; // UTA: default value
		when(planDataLite.getRolloversDelayedUntilEligibilityReqtMet())
				.thenReturn(getRolloversDelayedUntilEligibilityReqtMetResult);

		String getVestingComputationPeriodResult = ""; // UTA: default value
		when(planDataLite.getVestingComputationPeriod()).thenReturn(getVestingComputationPeriodResult);

		Collection<VestingSchedule> getVestingSchedulesResult = new ArrayList<>(); // UTA: default value
		VestingSchedule item3 = mock(VestingSchedule.class);
		getVestingSchedulesResult.add(item3);
		doReturn(getVestingSchedulesResult).when(planDataLite).getVestingSchedules();

		String getVestingServiceCreditMethodResult = ""; // UTA: default value
		when(planDataLite.getVestingServiceCreditMethod()).thenReturn(getVestingServiceCreditMethodResult);

		boolean isAnyDeferralsMoneyTypesAvailableResult = false; // UTA: default value
		when(planDataLite.isAnyDeferralsMoneyTypesAvailable()).thenReturn(isAnyDeferralsMoneyTypesAvailableResult);

		boolean isRetirementAgeSpecifiedResult = false; // UTA: default value
		when(planDataLite.isRetirementAgeSpecified()).thenReturn(isRetirementAgeSpecifiedResult);

		boolean isVestingMissedForAnyERMoneyTypeResult = false; // UTA: default value
		when(planDataLite.isVestingMissedForAnyERMoneyType()).thenReturn(isVestingMissedForAnyERMoneyTypeResult);

		String toStringResult6 = ""; // UTA: default value
		when(planDataLite.toString()).thenReturn(toStringResult6);
		return planDataLite;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalDistributionMethod
	 */
	private static WithdrawalDistributionMethod mockWithdrawalDistributionMethod() throws Throwable {
		WithdrawalDistributionMethod withdrawalDistributionMethod = mock(WithdrawalDistributionMethod.class);
		Boolean getAnnuityIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getAnnuityIndicator()).thenReturn(getAnnuityIndicatorResult);

		Boolean getInstallmentsIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getInstallmentsIndicator()).thenReturn(getInstallmentsIndicatorResult);

		Boolean getLumpSumIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getLumpSumIndicator()).thenReturn(getLumpSumIndicatorResult);

		Boolean getOtherIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getOtherIndicator()).thenReturn(getOtherIndicatorResult);

		Boolean getPartialWithdrawalIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getPartialWithdrawalIndicator())
				.thenReturn(getPartialWithdrawalIndicatorResult);

		String toStringResult7 = ""; // UTA: default value
		when(withdrawalDistributionMethod.toString()).thenReturn(toStringResult7);
		return withdrawalDistributionMethod;
	}

	/**
	 * Parasoft Jtest UTA: Test for validateOnlineDeferrals(CsfForm, PlanDataLite, Collection)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validateOnlineDeferrals(CsfForm, PlanDataLite, Collection)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidateOnlineDeferrals() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm3();
		PlanDataLite planDataLite = mockPlanDataLite3();
		Collection<GenericException> errors = new ArrayList<>(); // UTA: default value
		GenericException item = mock(GenericException.class);
		errors.add(item);
		underTest.validateOnlineDeferrals(csfForm, planDataLite, errors);

	}
	
	/**
	 * Parasoft Jtest UTA: Test for validateOnlineDeferrals(CsfForm, PlanDataLite, Collection)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validateOnlineDeferrals(CsfForm, PlanDataLite, Collection)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidateOnlineDeferrals_No() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm_No();
		PlanDataLite planDataLite = mockPlanDataLite3();
		Collection<GenericException> errors = new ArrayList<>(); // UTA: default value
		GenericException item = mock(GenericException.class);
		errors.add(item);
		underTest.validateOnlineDeferrals(csfForm, planDataLite, errors);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm3() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = "Yes"; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = "Yes"; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm_No() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = "Yes"; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = "No"; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite3() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = "Y"; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);
		return planDataLite;
	}

	/**
	 * Parasoft Jtest UTA: Test for determineSignupMethod(CsfForm, PlanDataLite)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#determineSignupMethod(CsfForm, PlanDataLite)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testDetermineSignupMethod2() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm4();
		PlanDataLite planDataLite = mockPlanDataLite4();
		String result = underTest.determineSignupMethod(csfForm, planDataLite);

		// Then
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm4() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite4() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = ""; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);
		return planDataLite;
	}

	/**
	 * Parasoft Jtest UTA: Test for validate404a5(UserProfile)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validate404a5(UserProfile)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidate404a5() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		UserProfile userProfile = mockUserProfile();
		Access404a5 myAccess = userProfile.getAccess404a5();
		Set<Facility> myFacility = myAccess.getAccessibleFacilities();
		myFacility.clear();
		
		boolean result = underTest.validate404a5(userProfile);

		// Then
		// assertFalse(result);
	}
	
	/**
	 * Parasoft Jtest UTA: Test for validate404a5(UserProfile)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validate404a5(UserProfile)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidate404a5_Access() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		UserProfile userProfile = mockUserProfile();
//		Access404a5 myAccess =  mockAccess404a5();
		boolean result = underTest.validate404a5(userProfile);

		// Then
		// assertFalse(result);
	}


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Qualification
	 */
	private static Qualification mockQualification() throws Throwable {
		Qualification getAccessResult = mock(Qualification.class);
		Set<MissingInformation> getTemporarilyMissingInformationResult = new HashSet<>(); // UTA: default value
		MissingInformation item = MissingInformation.PREVIOUS_YEAR_END_FUND_DATA; // UTA: default value
		getTemporarilyMissingInformationResult.add(item);
		doReturn(getTemporarilyMissingInformationResult).when(getAccessResult).getTemporarilyMissingInformation();
		return getAccessResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Access404a5
	 */
	private static Access404a5 mockAccess404a5() throws Throwable {
		Access404a5 getAccess404a5Result = mock(Access404a5.class);
		Qualification getAccessResult = mockQualification();
		when(getAccess404a5Result.getAccess(nullable(Facility.class))).thenReturn(getAccessResult);

		Set<Facility> getAccessibleFacilitiesResult = new HashSet<>(); // UTA: default value
		Facility item2 = Facility._404A5_PLAN_AND_INVESTMENT_NOTICE; // UTA: default value
		getAccessibleFacilitiesResult.add(item2);
		doReturn(getAccessibleFacilitiesResult).when(getAccess404a5Result).getAccessibleFacilities();
		return getAccess404a5Result;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserProfile
	 */
	private static UserProfile mockUserProfile() throws Throwable {
		UserProfile userProfile = mock(UserProfile.class);
		Access404a5 getAccess404a5Result = mockAccess404a5();
		when(userProfile.getAccess404a5()).thenReturn(getAccess404a5Result);
		return userProfile;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserProfile
	 */
	private static UserProfile mockUserProfile_Contract() throws Throwable {
		UserProfile userProfile = mock(UserProfile.class);
		Access404a5 getAccess404a5Result = mockAccess404a5();
		Collection monthEndDates = new ArrayList();
		ContractDatesVO contractDates = new ContractDatesVO(new Date(), monthEndDates );
		Contract contract = new Contract(70300, true, "avail",true, "101", new Date(), "ASDFGF", contractDates, true, true);
		contract.setCheckPayableToCode("Yes");
		when(userProfile.getCurrentContract()).thenReturn(contract);
		UserRole userRole  = mock(UserRole.class);
		when(userProfile.getRole()).thenReturn(userRole);
		when(userRole.isInternalUser()).thenReturn(true);
		return userProfile;
	}

	/**
	 * Parasoft Jtest UTA: Test for validatePlanData(PlanDataLite, CsfForm)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validatePlanData(PlanDataLite, CsfForm)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testValidatePlanData() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		PlanDataLite planDataLite = mockPlanDataLite5();
		CsfForm myForm = mockCsfForm5();
		underTest.validatePlanData(planDataLite, myForm);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite5() throws Throwable {
		return mock(PlanDataLite.class);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm5() throws Throwable {
		CsfForm myForm = mock(CsfForm.class);
		String getVestingPercentagesMethodResult = "NA"; // UTA: default value
		when(myForm.getVestingPercentagesMethod()).thenReturn(getVestingPercentagesMethodResult);
		return myForm;
	}

	/**
	 * Parasoft Jtest UTA: Test for validateCSFValues(ActionForm, HttpServletRequest, UserProfile, WithdrawalDistributionMethod, Collection)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validateCSFValues(ActionForm, HttpServletRequest, UserProfile, WithdrawalDistributionMethod, Collection)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testValidateCSFValues() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();
		spy(ContractServiceDelegate.class);
		CsfForm actionForm = mockCsfForm_ma();

		HttpServletRequest request = mockHttpServletRequest_Plan();
		UserProfile userProfile = mockUserProfile_Contract();
		ContractServiceDelegate contractServiceDelegate = mock(ContractServiceDelegate.class);
		PowerMockito.doReturn(contractServiceDelegate).when(ContractServiceDelegate.class);
		ContractServiceDelegate.getInstance();
		PlanDataLite planDataLite = new PlanDataLite();
		when((contractServiceDelegate.getPlanDataLight(anyInt()))).thenReturn(planDataLite);
		when(actionForm.getManagedAccountService()).thenReturn("Otherservice");
		
		WithdrawalDistributionMethod withdrawalDistributionMethod = mockWithdrawalDistributionMethod2();
		Collection<GenericException> errors = new ArrayList<>(); // UTA: default value
		GenericException item3 = mock(GenericException.class);
		errors.add(item3);
		Collection<GenericException> warnings = new ArrayList<>(); // UTA: default value
		GenericException item4 = mock(GenericException.class);
		warnings.add(item4);
		Collection<GenericException> result = underTest.validateCSFValues(actionForm, request, userProfile,
				withdrawalDistributionMethod, errors, warnings);
		// Then
//				 assertEquals(null, result);
	}
	
	/**
	 * Parasoft Jtest UTA: Test for validateCSFValues(ActionForm, HttpServletRequest, UserProfile, WithdrawalDistributionMethod, Collection)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#validateCSFValues(ActionForm, HttpServletRequest, UserProfile, WithdrawalDistributionMethod, Collection)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testValidateCSFValues_false() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();
		spy(ContractServiceDelegate.class);
		CsfForm actionForm = mockCsfForm_maf();

		HttpServletRequest request = mockHttpServletRequest_Plan();
		UserProfile userProfile = mockUserProfile_Contract();
		ContractServiceDelegate contractServiceDelegate = mock(ContractServiceDelegate.class);
		PowerMockito.doReturn(contractServiceDelegate).when(ContractServiceDelegate.class);
		ContractServiceDelegate.getInstance();
		PlanDataLite planDataLite = new PlanDataLite();
		when((contractServiceDelegate.getPlanDataLight(anyInt()))).thenReturn(planDataLite);
		when(actionForm.getManagedAccountService()).thenReturn("Otherservice");
		
		WithdrawalDistributionMethod withdrawalDistributionMethod = mockWithdrawalDistributionMethod2();
		Collection<GenericException> errors = new ArrayList<>(); // UTA: default value
		GenericException item3 = mock(GenericException.class);
		errors.add(item3);
		Collection<GenericException> warnings = new ArrayList<>(); // UTA: default value
		GenericException item4 = mock(GenericException.class);
		warnings.add(item4);
		Collection<GenericException> result = underTest.validateCSFValues(actionForm, request, userProfile,
				withdrawalDistributionMethod, errors, warnings);
		// Then
//				 assert(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ActionForm
	 */
	private static ActionForm mockActionForm() throws Throwable {
		ActionForm actionForm = mock(ActionForm.class);
		String toStringResult = ""; // UTA: default value
		when(actionForm.toString()).thenReturn(toStringResult);
		return actionForm;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ContractServiceDelegate
	 */
	private static ContractServiceDelegate mockContractServiceDelegate() throws Throwable {
		return mock(ContractServiceDelegate.class);
	}

	
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Enumeration
	 */
	private static Enumeration<String> mockEnumeration() throws Throwable {
		Enumeration<String> getAttributeNamesResult = mock(Enumeration.class);
		String toStringResult2 = ""; // UTA: default value
		when(getAttributeNamesResult.toString()).thenReturn(toStringResult2);
		return getAttributeNamesResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Enumeration
	 */
	private static Enumeration<String> mockEnumeration2() throws Throwable {
		Enumeration<String> getAttributeNamesResult2 = mock(Enumeration.class);
		String toStringResult3 = ""; // UTA: default value
		when(getAttributeNamesResult2.toString()).thenReturn(toStringResult3);
		return getAttributeNamesResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of HttpSession
	 */
	private static HttpSession mockHttpSession() throws Throwable {
		HttpSession getSessionResult = mock(HttpSession.class);
		Object getAttributeResult2 = new Object(); // UTA: default value
		when(getSessionResult.getAttribute(nullable(String.class))).thenReturn(getAttributeResult2);

		Enumeration<String> getAttributeNamesResult2 = mockEnumeration2();
		doReturn(getAttributeNamesResult2).when(getSessionResult).getAttributeNames();

		String getIdResult = ""; // UTA: default value
		when(getSessionResult.getId()).thenReturn(getIdResult);

		String toStringResult4 = ""; // UTA: default value
		when(getSessionResult.toString()).thenReturn(toStringResult4);
		return getSessionResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of HttpServletRequest
	 */
	private static HttpServletRequest mockHttpServletRequest() throws Throwable {
		HttpServletRequest request = mock(HttpServletRequest.class);
		Object getAttributeResult = new Object(); // UTA: default value
		when(request.getAttribute(nullable(String.class))).thenReturn(getAttributeResult);

		Enumeration<String> getAttributeNamesResult = mockEnumeration();
		doReturn(getAttributeNamesResult).when(request).getAttributeNames();

		HttpSession getSessionResult = mockHttpSession();
		when(request.getSession(anyBoolean())).thenReturn(getSessionResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}
	
	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of HttpServletRequest
	 */
	private static HttpServletRequest mockHttpServletRequest_Plan() throws Throwable {
		HttpServletRequest request = mock(HttpServletRequest.class);
		PlanDataLite getAttributeResult = new PlanDataLite(); // UTA: default value
		when(request.getAttribute(nullable(String.class))).thenReturn(getAttributeResult);

		Enumeration<String> getAttributeNamesResult = mockEnumeration();
		doReturn(getAttributeNamesResult).when(request).getAttributeNames();

		HttpSession getSessionResult = mockHttpSession();
		when(request.getSession(anyBoolean())).thenReturn(getSessionResult);

		String toStringResult5 = ""; // UTA: default value
		when(request.toString()).thenReturn(toStringResult5);
		return request;
	}


	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Qualification
	 */
	private static Qualification mockQualification2() throws Throwable {
		Qualification getAccessResult = mock(Qualification.class);
		Set<MissingInformation> getTemporarilyMissingInformationResult = new HashSet<>(); // UTA: default value
		MissingInformation item = MissingInformation.PREVIOUS_YEAR_END_FUND_DATA; // UTA: default value
		getTemporarilyMissingInformationResult.add(item);
		doReturn(getTemporarilyMissingInformationResult).when(getAccessResult).getTemporarilyMissingInformation();

		String toStringResult6 = ""; // UTA: default value
		when(getAccessResult.toString()).thenReturn(toStringResult6);
		return getAccessResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Access404a5
	 */
	private static Access404a5 mockAccess404a52() throws Throwable {
		Access404a5 getAccess404a5Result = mock(Access404a5.class);
		Qualification getAccessResult = mockQualification2();
		when(getAccess404a5Result.getAccess(nullable(Facility.class))).thenReturn(getAccessResult);

		Set<Facility> getAccessibleFacilitiesResult = new HashSet<>(); // UTA: default value
		Facility item2 = Facility._404A5_PLAN_AND_INVESTMENT_NOTICE; // UTA: default value
		getAccessibleFacilitiesResult.add(item2);
		doReturn(getAccessibleFacilitiesResult).when(getAccess404a5Result).getAccessibleFacilities();

		String toStringResult7 = ""; // UTA: default value
		when(getAccess404a5Result.toString()).thenReturn(toStringResult7);
		return getAccess404a5Result;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Contract
	 */
	private static Contract mockContract() throws Throwable {
		Contract getCurrentContractResult = mock(Contract.class);
		String getCheckPayableToCodeResult = ""; // UTA: default value
		when(getCurrentContractResult.getCheckPayableToCode()).thenReturn(getCheckPayableToCodeResult);

		int getContractNumberResult = 0; // UTA: default value
		when(getCurrentContractResult.getContractNumber()).thenReturn(getContractNumberResult);

		String getStatusResult = ""; // UTA: default value
		when(getCurrentContractResult.getStatus()).thenReturn(getStatusResult);

		String toStringResult8 = ""; // UTA: default value
		when(getCurrentContractResult.toString()).thenReturn(toStringResult8);
		return getCurrentContractResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserRole
	 */
	private static UserRole mockUserRole() throws Throwable {
		UserRole getRoleResult = mock(UserRole.class);
		boolean isExternalUserResult = false; // UTA: default value
		when(getRoleResult.isExternalUser()).thenReturn(isExternalUserResult);

		boolean isInternalUserResult = false; // UTA: default value
		when(getRoleResult.isInternalUser()).thenReturn(isInternalUserResult);

		boolean isPlanSponsorResult = false; // UTA: default value
		when(getRoleResult.isPlanSponsor()).thenReturn(isPlanSponsorResult);

		String toStringResult9 = ""; // UTA: default value
		when(getRoleResult.toString()).thenReturn(toStringResult9);
		return getRoleResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of UserProfile
	 */
	private static UserProfile mockUserProfile2() throws Throwable {
		UserProfile userProfile = mock(UserProfile.class);
		Access404a5 getAccess404a5Result = mockAccess404a52();
		when(userProfile.getAccess404a5()).thenReturn(getAccess404a5Result);

		Contract getCurrentContractResult = mockContract();
		when(userProfile.getCurrentContract()).thenReturn(getCurrentContractResult);

		Principal getPrincipalResult = null; // UTA: default value
		when(userProfile.getPrincipal()).thenReturn(getPrincipalResult);

		UserRole getRoleResult = mockUserRole();
		when(userProfile.getRole()).thenReturn(getRoleResult);

		String toStringResult10 = ""; // UTA: default value
		when(userProfile.toString()).thenReturn(toStringResult10);
		return userProfile;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of WithdrawalDistributionMethod
	 */
	private static WithdrawalDistributionMethod mockWithdrawalDistributionMethod2() throws Throwable {
		WithdrawalDistributionMethod withdrawalDistributionMethod = mock(WithdrawalDistributionMethod.class);
		Boolean getAnnuityIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getAnnuityIndicator()).thenReturn(getAnnuityIndicatorResult);

		Boolean getInstallmentsIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getInstallmentsIndicator()).thenReturn(getInstallmentsIndicatorResult);

		Boolean getLumpSumIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getLumpSumIndicator()).thenReturn(getLumpSumIndicatorResult);

		Boolean getOtherIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getOtherIndicator()).thenReturn(getOtherIndicatorResult);

		Boolean getPartialWithdrawalIndicatorResult = false; // UTA: default value
		when(withdrawalDistributionMethod.getPartialWithdrawalIndicator())
				.thenReturn(getPartialWithdrawalIndicatorResult);

		String toStringResult11 = ""; // UTA: default value
		when(withdrawalDistributionMethod.toString()).thenReturn(toStringResult11);
		return withdrawalDistributionMethod;
	}

	/**
	 * Parasoft Jtest UTA: Test for determineSignupMethod(CsfForm, PlanDataLite)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataValidator#determineSignupMethod(CsfForm, PlanDataLite)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testDetermineSignupMethod3() throws Exception {
		// Given
		CsfDataValidator underTest = new CsfDataValidator();

		// When
		CsfForm csfForm = mockCsfForm6();
		PlanDataLite planDataLite = mockPlanDataLite6();
		String result = underTest.determineSignupMethod(csfForm, planDataLite);

		// Then
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm6() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		String getChangeDeferralsOnlineResult = ""; // UTA: default value
		when(csfForm.getChangeDeferralsOnline()).thenReturn(getChangeDeferralsOnlineResult);
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of PlanDataLite
	 */
	private static PlanDataLite mockPlanDataLite6() throws Throwable {
		PlanDataLite planDataLite = mock(PlanDataLite.class);
		String getAciAllowedResult = ""; // UTA: default value
		when(planDataLite.getAciAllowed()).thenReturn(getAciAllowedResult);
		return planDataLite;
	}
}