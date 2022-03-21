/**
 * 
 */
package com.manulife.pension.ps.web.contract.csf;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ParticipantACIFeatureUpdateVO;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.security.Principal;

/**
 * Parasoft Jtest UTA: Test class for CsfDataHelper
 *
 * @see com.manulife.pension.ps.web.contract.csf.CsfDataHelper
 * @author ashoksu
 */
@PrepareForTest({ ContractServiceDelegate.class, CsfDataHelperTest.class })
@RunWith(PowerMockRunner.class)
public class CsfDataHelperTest {

	/**
	 * Parasoft Jtest UTA: Test for loadCMAdata(Map, CsfForm, int, PlanDataLite)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataHelper#loadCMAdata(Map, CsfForm, int, PlanDataLite)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testLoadCMAdata() throws Exception {
		// Given
		CsfDataHelper underTest = new CsfDataHelper();

		ContractServiceFeature csf = mock(ContractServiceFeature.class);

		// When
		Map csfMap = new HashMap(); // UTA: default value
		csfMap.put("MD", csf);

		Map<String, String> serviceFeaturesUnModifiable = new HashMap<>();
		serviceFeaturesUnModifiable.put("S1", "ServiceFeature");
		ContractServiceDelegate csd = mock(ContractServiceDelegate.class);
		PowerMockito.spy(ContractServiceDelegate.class);
		PowerMockito.doReturn(csd).when(ContractServiceDelegate.class);

		ContractServiceDelegate.getInstance();
		when(csd.getProductServiceFeaturesByGroupCode(anyString())).thenReturn(serviceFeaturesUnModifiable);
		Map<String, String> csdMap = new HashMap<>();
		csdMap.put("test1", "welcome");
		//when(csd.getInstance().getProductServiceFeaturesByGroupCode("test")).thenReturn(csdMap);
		CsfForm csfForm = mock(CsfForm.class); // UTA: default value
		int contractId = 70300; // UTA: default value
		PlanDataLite planDataLite = mock(PlanDataLite.class); // UTA: default value

		underTest.loadCMAdata(csfMap, csfForm, contractId, planDataLite);

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(nullable(Date.class))).thenReturn(afterResult);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Contract
	 */
	private static Contract mockContract() throws Throwable {
		Contract contract = mock(Contract.class);
		int getContractNumberResult = 0; // UTA: default value
		when(contract.getContractNumber()).thenReturn(getContractNumberResult);

		Date getEffectiveDateResult = mockDate();
		when(contract.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		boolean isBusinessConvertedResult = false; // UTA: default value
		when(contract.isBusinessConverted()).thenReturn(isBusinessConvertedResult);
		return contract;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm() throws Throwable {
		CloneableForm getClonedFormResult3 = mock(CloneableForm.class);
		CloneableForm getClonedFormResult4 = mock(CloneableForm.class);
		when(getClonedFormResult3.getClonedForm()).thenReturn(getClonedFormResult4);
		return getClonedFormResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm2() throws Throwable {
		CloneableForm getClonedFormResult2 = mock(CloneableForm.class);
		CloneableForm getClonedFormResult3 = mockCloneableForm();
		when(getClonedFormResult2.getClonedForm()).thenReturn(getClonedFormResult3);
		return getClonedFormResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm3() throws Throwable {
		CloneableForm getClonedFormResult = mock(CloneableForm.class);
		CloneableForm getClonedFormResult2 = mockCloneableForm2();
		when(getClonedFormResult.getClonedForm()).thenReturn(getClonedFormResult2);
		return getClonedFormResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CsfForm
	 */
	private static CsfForm mockCsfForm() throws Throwable {
		CsfForm csfForm = mock(CsfForm.class);
		String getAciAnniversaryDateResult = ""; // UTA: default value
		when(csfForm.getAciAnniversaryDate()).thenReturn(getAciAnniversaryDateResult);

		String getAciAnniversaryYearResult = ""; // UTA: default value
		when(csfForm.getAciAnniversaryYear()).thenReturn(getAciAnniversaryYearResult);

		String getAciSignupMethodResult = ""; // UTA: default value
		when(csfForm.getAciSignupMethod()).thenReturn(getAciSignupMethodResult);

		String getAddressManagementViewResult = ""; // UTA: default value
		when(csfForm.getAddressManagementView()).thenReturn(getAddressManagementViewResult);

		String getAllowOnlineLoansResult = ""; // UTA: default value
		when(csfForm.getAllowOnlineLoans()).thenReturn(getAllowOnlineLoansResult);

		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		CloneableForm getClonedFormResult = mockCloneableForm3();
		when(csfForm.getClonedForm()).thenReturn(getClonedFormResult);

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

		String getEligibilityCalculationIndResult = ""; // UTA: default value
		when(csfForm.getEligibilityCalculationInd()).thenReturn(getEligibilityCalculationIndResult);

		ArrayList<EligibilityCalculationMoneyType> getEligibilityServiceMoneyTypesResult = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		getEligibilityServiceMoneyTypesResult.add(item);
		doReturn(getEligibilityServiceMoneyTypesResult).when(csfForm).getEligibilityServiceMoneyTypes();

		ArrayList<EligibilityCalculationMoneyType> getLastUpdatedEligibilityServiceMoneyTypesResult = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item2 = mock(EligibilityCalculationMoneyType.class);
		getLastUpdatedEligibilityServiceMoneyTypesResult.add(item2);
		doReturn(getLastUpdatedEligibilityServiceMoneyTypesResult).when(csfForm)
				.getLastUpdatedEligibilityServiceMoneyTypes();

		String getManagedAccountServiceResult = ""; // UTA: default value
		when(csfForm.getManagedAccountService()).thenReturn(getManagedAccountServiceResult);

		String getManagedAccountServiceAvailabilityDateResult = ""; // UTA: default value
		when(csfForm.getManagedAccountServiceAvailabilityDate())
				.thenReturn(getManagedAccountServiceAvailabilityDateResult);

		String getNoticeServiceIndResult = ""; // UTA: default value
		when(csfForm.getNoticeServiceInd()).thenReturn(getNoticeServiceIndResult);

		String getPlanDeferralLimitPercentResult = ""; // UTA: default value
		when(csfForm.getPlanDeferralLimitPercent()).thenReturn(getPlanDeferralLimitPercentResult);

		String getPlanDeferralMaxLimitPercentResult = ""; // UTA: default value
		when(csfForm.getPlanDeferralMaxLimitPercent()).thenReturn(getPlanDeferralMaxLimitPercentResult);

		String getResetDeferralValuesResult = ""; // UTA: default value
		when(csfForm.getResetDeferralValues()).thenReturn(getResetDeferralValuesResult);

		String[] getSelectedMoneyTypesResult = new java.lang.String[0]; // UTA: default value
		when(csfForm.getSelectedMoneyTypes()).thenReturn(getSelectedMoneyTypesResult);
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantACIFeatureUpdateVO
	 */
	private static ParticipantACIFeatureUpdateVO mockParticipantACIFeatureUpdateVO() throws Throwable {
		return mock(ParticipantACIFeatureUpdateVO.class);
	}

	/**
	 * Parasoft Jtest UTA: Test for getChangedContractServiceFeatures(Contract, CsfForm, Principal, ParticipantACIFeatureUpdateVO)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfDataHelper#getChangedContractServiceFeatures(Contract, CsfForm, Principal, ParticipantACIFeatureUpdateVO)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetChangedContractServiceFeatures() throws Exception {
		// Given
		CsfDataHelper underTest = new CsfDataHelper();

		// When
		Contract contract = mockContract2();
		CsfForm csfForm = mockCsfForm2();
		Principal principal = Principal.getSystemUserPrincipal();
		ParticipantACIFeatureUpdateVO paf = mockParticipantACIFeatureUpdateVO2();
		Collection<ContractServiceFeature> result = underTest.getChangedContractServiceFeatures(contract, csfForm,
				principal, paf);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(null));
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Date
	 */
	private static Date mockDate2() throws Throwable {
		Date getEffectiveDateResult = mock(Date.class);
		boolean afterResult = false; // UTA: default value
		when(getEffectiveDateResult.after(nullable(Date.class))).thenReturn(afterResult);
		return getEffectiveDateResult;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of Contract
	 */
	private static Contract mockContract2() throws Throwable {
		Contract contract = mock(Contract.class);
		int getContractNumberResult = 0; // UTA: default value
		when(contract.getContractNumber()).thenReturn(getContractNumberResult);

		Date getEffectiveDateResult = mockDate2();
		when(contract.getEffectiveDate()).thenReturn(getEffectiveDateResult);

		boolean isBusinessConvertedResult = false; // UTA: default value
		when(contract.isBusinessConverted()).thenReturn(isBusinessConvertedResult);
		return contract;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm4() throws Throwable {
		CloneableForm getClonedFormResult3 = mock(CloneableForm.class);
		CloneableForm getClonedFormResult4 = mock(CloneableForm.class);
		when(getClonedFormResult3.getClonedForm()).thenReturn(getClonedFormResult4);
		return getClonedFormResult3;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm5() throws Throwable {
		CloneableForm getClonedFormResult2 = mock(CloneableForm.class);
		CloneableForm getClonedFormResult3 = mockCloneableForm4();
		when(getClonedFormResult2.getClonedForm()).thenReturn(getClonedFormResult3);
		return getClonedFormResult2;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm6() throws Throwable {
		CloneableForm getClonedFormResult = mock(CloneableForm.class);
		CloneableForm getClonedFormResult2 = mockCloneableForm5();
		when(getClonedFormResult.getClonedForm()).thenReturn(getClonedFormResult2);
		return getClonedFormResult;
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

		String getAddressManagementViewResult = ""; // UTA: default value
		when(csfForm.getAddressManagementView()).thenReturn(getAddressManagementViewResult);

		String getAllowOnlineLoansResult = ""; // UTA: default value
		when(csfForm.getAllowOnlineLoans()).thenReturn(getAllowOnlineLoansResult);

		String getAutoContributionIncreaseResult = ""; // UTA: default value
		when(csfForm.getAutoContributionIncrease()).thenReturn(getAutoContributionIncreaseResult);

		CloneableForm getClonedFormResult = mockCloneableForm6();
		when(csfForm.getClonedForm()).thenReturn(getClonedFormResult);

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

		String getEligibilityCalculationIndResult = ""; // UTA: default value
		when(csfForm.getEligibilityCalculationInd()).thenReturn(getEligibilityCalculationIndResult);

		ArrayList<EligibilityCalculationMoneyType> getEligibilityServiceMoneyTypesResult = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		getEligibilityServiceMoneyTypesResult.add(item);
		doReturn(getEligibilityServiceMoneyTypesResult).when(csfForm).getEligibilityServiceMoneyTypes();

		ArrayList<EligibilityCalculationMoneyType> getLastUpdatedEligibilityServiceMoneyTypesResult = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item2 = mock(EligibilityCalculationMoneyType.class);
		getLastUpdatedEligibilityServiceMoneyTypesResult.add(item2);
		doReturn(getLastUpdatedEligibilityServiceMoneyTypesResult).when(csfForm)
				.getLastUpdatedEligibilityServiceMoneyTypes();

		String getManagedAccountServiceResult = ""; // UTA: default value
		when(csfForm.getManagedAccountService()).thenReturn(getManagedAccountServiceResult);

		String getManagedAccountServiceAvailabilityDateResult = ""; // UTA: default value
		when(csfForm.getManagedAccountServiceAvailabilityDate())
				.thenReturn(getManagedAccountServiceAvailabilityDateResult);

		String getNoticeServiceIndResult = ""; // UTA: default value
		when(csfForm.getNoticeServiceInd()).thenReturn(getNoticeServiceIndResult);

		String getPlanDeferralLimitPercentResult = ""; // UTA: default value
		when(csfForm.getPlanDeferralLimitPercent()).thenReturn(getPlanDeferralLimitPercentResult);

		String getPlanDeferralMaxLimitPercentResult = ""; // UTA: default value
		when(csfForm.getPlanDeferralMaxLimitPercent()).thenReturn(getPlanDeferralMaxLimitPercentResult);

		String getResetDeferralValuesResult = ""; // UTA: default value
		when(csfForm.getResetDeferralValues()).thenReturn(getResetDeferralValuesResult);

		String[] getSelectedMoneyTypesResult = new java.lang.String[0]; // UTA: default value
		when(csfForm.getSelectedMoneyTypes()).thenReturn(getSelectedMoneyTypesResult);
		
		return csfForm;
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of ParticipantACIFeatureUpdateVO
	 */
	private static ParticipantACIFeatureUpdateVO mockParticipantACIFeatureUpdateVO2() throws Throwable {
		return mock(ParticipantACIFeatureUpdateVO.class);
	}
}