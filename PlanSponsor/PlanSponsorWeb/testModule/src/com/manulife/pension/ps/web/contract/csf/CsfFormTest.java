/**
 * 
 */
package com.manulife.pension.ps.web.contract.csf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;

/**
 * Parasoft Jtest UTA: Test class for CsfForm
 *
 * @see com.manulife.pension.ps.web.contract.csf.CsfForm
 * @author ashoksu
 */
public class CsfFormTest {

	/**
	 * Parasoft Jtest UTA: Test for testCopyLastUpdatedEligibityServiceMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#testCopyLastUpdatedEligibityServiceMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testCopyLastUpdatedEligibityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypeList = new ArrayList<>();
		EligibilityCalculationMoneyType eligibilityCalculationMoneyType = new EligibilityCalculationMoneyType();
		eligibilityCalculationMoneyType.setCalculationOverride("StringValue");
		eligibilityCalculationMoneyType.setMoneyTypeDescription("Money");
		eligibilityCalculationMoneyType.setMoneyTypeId("123");
		eligibilityCalculationMoneyType.setMoneyTypeName("TestMoney");
		eligibilityCalculationMoneyType.setMoneyTypeShortName("T1");
		eligibilityCalculationMoneyType.setMoneyTypeValue("TypeValue");
		lastUpdatedEligibilityServiceMoneyTypeList.add(eligibilityCalculationMoneyType);
		ArrayList result = underTest
				.copyLastUpdatedEligibityServiceMoneyTypes(lastUpdatedEligibilityServiceMoneyTypeList);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for revert()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#revert()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testRevert() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();
		CloneableForm clonedFormValue = mockCloneableForm();
		Field clonedFormField = ReflectionUtils.findField(CloneableAutoForm.class, "clonedForm", null);
		ReflectionUtils.makeAccessible(clonedFormField);
		ReflectionUtils.setField(clonedFormField, underTest, clonedFormValue);

		// When
		underTest.revert();

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm() throws Throwable {
		CloneableForm clonedFormValue = mock(CloneableForm.class);
		return clonedFormValue;
	}

	/**
	 * Parasoft Jtest UTA: Test for getAciAnniversaryDate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAciAnniversaryDate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAciAnniversaryDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAciAnniversaryDate();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAciAnniversaryYear()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAciAnniversaryYear()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAciAnniversaryYear() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAciAnniversaryYear();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setWhoWillReviewWithdrawals(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setWhoWillReviewWithdrawals(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetWhoWillReviewWithdrawals() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String whoWillReviewWithdrawals = ""; // UTA: default value
		underTest.setWhoWillReviewWithdrawals(whoWillReviewWithdrawals);

	}

	/**
	 * Parasoft Jtest UTA: Test for toString()
	 *
	 * @see com.manulife.pension.platform.web.controller.BaseForm#toString()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testToString() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.toString();

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for trim(String)
	 *
	 * @see com.manulife.pension.platform.web.controller.BaseForm#trim(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testTrim() throws Exception {
		// When
		String str = ""; // UTA: default value
		String result = BaseForm.trim(str);

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for trimString(String)
	 *
	 * @see com.manulife.pension.platform.web.controller.BaseForm#trimString(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testTrimString() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String value = ""; // UTA: default value
		String result = underTest.trimString(value);

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getActiveAddress()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getActiveAddress()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetActiveAddress() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getActiveAddress();

		// Then
		 assertEquals("N", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getDisabledAddress()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDisabledAddress()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDisabledAddress() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDisabledAddress();

		// Then
		 assertEquals("N", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getRetiredAddress()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getRetiredAddress()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetRetiredAddress() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getRetiredAddress();

		// Then
		 assertEquals("N", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getTerminatedAddress()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getTerminatedAddress()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetTerminatedAddress() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getTerminatedAddress();

		// Then
		 assertEquals("N", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for revert()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#revert()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testRevert2() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();
		CloneableForm clonedFormValue = mockCloneableForm2();
		Field clonedFormField = ReflectionUtils.findField(CloneableAutoForm.class, "clonedForm", null);
		ReflectionUtils.makeAccessible(clonedFormField);
		ReflectionUtils.setField(clonedFormField, underTest, clonedFormValue);

		// When
		underTest.revert();

	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of CloneableForm
	 */
	private static CloneableForm mockCloneableForm2() throws Exception {
		CloneableForm clonedFormValue = mock(CloneableForm.class);
		return clonedFormValue;
	}

	/**
	 * Parasoft Jtest UTA: Test for getManagedAccountServiceAvailabilityDateDb()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getManagedAccountServiceAvailabilityDateDb()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetManagedAccountServiceAvailabilityDateDb() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getManagedAccountServiceAvailabilityDateDb();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setManagedAccountServiceAvailabilityDateDb(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setManagedAccountServiceAvailabilityDateDb(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetManagedAccountServiceAvailabilityDateDb() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String managedAccountServiceAvailabilityDateDb = ""; // UTA: default value
		underTest.setManagedAccountServiceAvailabilityDateDb(managedAccountServiceAvailabilityDateDb);

	}

	/**
	 * Parasoft Jtest UTA: Test for getNextBusinessDate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getNextBusinessDate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetNextBusinessDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getNextBusinessDate();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getManagedAccountServiceDb()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getManagedAccountServiceDb()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetManagedAccountServiceDb() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getManagedAccountServiceDb();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setManagedAccountServiceDb(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setManagedAccountServiceDb(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetManagedAccountServiceDb() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String managedAccountServiceDb = ""; // UTA: default value
		underTest.setManagedAccountServiceDb(managedAccountServiceDb);

	}

	/**
	 * Parasoft Jtest UTA: Test for setNextBusinessDate(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setNextBusinessDate(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetNextBusinessDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String nextBusinessDate = ""; // UTA: default value
		underTest.setNextBusinessDate(nextBusinessDate);

	}

	/**
	 * Parasoft Jtest UTA: Test for setManagedAccountServiceAvailabilityDate(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setManagedAccountServiceAvailabilityDate(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetManagedAccountServiceAvailabilityDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String managedAccountServiceAvailabilityDate = ""; // UTA: default value
		underTest.setManagedAccountServiceAvailabilityDate(managedAccountServiceAvailabilityDate);

	}

	/**
	 * Parasoft Jtest UTA: Test for getCoFidServiceFeatureDetails()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getCoFidServiceFeatureDetails()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetCoFidServiceFeatureDetails() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<CoFidServiceFeatureDetails> result = underTest.getCoFidServiceFeatureDetails();

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for setCoFidServiceFeatureDetails(ArrayList)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCoFidServiceFeatureDetails(ArrayList)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCoFidServiceFeatureDetails() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<CoFidServiceFeatureDetails> coFidServiceFeatureDetails = new ArrayList<>(); // UTA: default value
		CoFidServiceFeatureDetails item = mock(CoFidServiceFeatureDetails.class);
		coFidServiceFeatureDetails.add(item);
		underTest.setCoFidServiceFeatureDetails(coFidServiceFeatureDetails);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanStatus()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanStatus()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanStatus() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPlanStatus();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanStatus(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanStatus(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanStatus() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String planStatus = ""; // UTA: default value
		underTest.setPlanStatus(planStatus);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAddressChanges()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAddressChanges()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAddressChanges() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String[] result = underTest.getAddressChanges();

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.length);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAddressChanges(String[])
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAddressChanges(String[])
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAddressChanges() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String[] addressChanges = new java.lang.String[0]; // UTA: default value
		underTest.setAddressChanges(addressChanges);

	}

	/**
	 * Parasoft Jtest UTA: Test for getIsDeferralEZiDisabled()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getIsDeferralEZiDisabled()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetIsDeferralEZiDisabled() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.getIsDeferralEZiDisabled();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralEZiDisabled(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralEZiDisabled(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralEZiDisabled() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean isDeferralEZiDisabled = false; // UTA: default value
		underTest.setDeferralEZiDisabled(isDeferralEZiDisabled);

	}

	/**
	 * Parasoft Jtest UTA: Test for getSelectedMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSelectedMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetSelectedMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String[] result = underTest.getSelectedMoneyTypes();

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.length);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSelectedMoneyTypes(String[])
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSelectedMoneyTypes(String[])
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSelectedMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String[] selectedMoneyTypes = new java.lang.String[0]; // UTA: default value
		underTest.setSelectedMoneyTypes(selectedMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for isEditing()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isEditing()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsEditing() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isEditing();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEditing(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEditing(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEditing() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean editing = false; // UTA: default value
		underTest.setEditing(editing);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEditMode()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEditMode()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEditMode() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getEditMode();

		// Then
		 assertEquals("false", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEditMode(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEditMode(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEditMode() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String editMode = ""; // UTA: default value
		underTest.setEditMode(editMode);

	}

	/**
	 * Parasoft Jtest UTA: Test for getButton()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getButton()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetButton() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getButton();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setButton(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setButton(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetButton() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String button = ""; // UTA: default value
		underTest.setButton(button);

	}

	/**
	 * Parasoft Jtest UTA: Test for getVestingPercentagesMethod()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getVestingPercentagesMethod()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetVestingPercentagesMethod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getVestingPercentagesMethod();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setVestingPercentagesMethod(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setVestingPercentagesMethod(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetVestingPercentagesMethod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String vestingPercentagesMethod = ""; // UTA: default value
		underTest.setVestingPercentagesMethod(vestingPercentagesMethod);

	}

	/**
	 * Parasoft Jtest UTA: Test for getVestingDataOnStatement()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getVestingDataOnStatement()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetVestingDataOnStatement() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getVestingDataOnStatement();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setVestingDataOnStatement(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setVestingDataOnStatement(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetVestingDataOnStatement() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String vestingDataOnStatement = ""; // UTA: default value
		underTest.setVestingDataOnStatement(vestingDataOnStatement);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAutoEnrollInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAutoEnrollInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAutoEnrollInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAutoEnrollInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAutoEnrollInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAutoEnrollInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAutoEnrollInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String autoEnrollInd = ""; // UTA: default value
		underTest.setAutoEnrollInd(autoEnrollInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAutoPayrollInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAutoPayrollInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAutoPayrollInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAutoPayrollInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAutoPayrollInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAutoPayrollInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAutoPayrollInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String autoPayrollInd = ""; // UTA: default value
		underTest.setAutoPayrollInd(autoPayrollInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getCreatorMayApproveInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getCreatorMayApproveInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetCreatorMayApproveInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getCreatorMayApproveInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setCreatorMayApproveInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCreatorMayApproveInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCreatorMayApproveInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String creatorMayApproveInd = ""; // UTA: default value
		underTest.setCreatorMayApproveInd(creatorMayApproveInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getInitialEnrollmentDate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getInitialEnrollmentDate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetInitialEnrollmentDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getInitialEnrollmentDate();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setInitialEnrollmentDate(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setInitialEnrollmentDate(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetInitialEnrollmentDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String initialEnrollmentDate = ""; // UTA: default value
		underTest.setInitialEnrollmentDate(initialEnrollmentDate);

	}

	/**
	 * Parasoft Jtest UTA: Test for getOnlineWithdrawalProcess()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getOnlineWithdrawalProcess()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetOnlineWithdrawalProcess() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getOnlineWithdrawalProcess();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setOnlineWithdrawalProcess(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setOnlineWithdrawalProcess(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetOnlineWithdrawalProcess() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String onlineWithdrawalProcess = ""; // UTA: default value
		underTest.setOnlineWithdrawalProcess(onlineWithdrawalProcess);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanFrequency()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanFrequency()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanFrequency() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPlanFrequency();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanFrequency(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanFrequency(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanFrequency() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String planFrequency = ""; // UTA: default value
		underTest.setPlanFrequency(planFrequency);

	}

	/**
	 * Parasoft Jtest UTA: Test for getWithdrawalInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getWithdrawalInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetWithdrawalInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getWithdrawalInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setWithdrawalInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setWithdrawalInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetWithdrawalInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String withdrawalInd = ""; // UTA: default value
		underTest.setWithdrawalInd(withdrawalInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEnrollOnline()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEnrollOnline()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEnrollOnline() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getEnrollOnline();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEnrollOnline(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEnrollOnline(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEnrollOnline() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String enrollOnline = ""; // UTA: default value
		underTest.setEnrollOnline(enrollOnline);

	}

	/**
	 * Parasoft Jtest UTA: Test for getChangeDeferralsOnline()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getChangeDeferralsOnline()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetChangeDeferralsOnline() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getChangeDeferralsOnline();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setChangeDeferralsOnline(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setChangeDeferralsOnline(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetChangeDeferralsOnline() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralChangeOnline = ""; // UTA: default value
		underTest.setChangeDeferralsOnline(deferralChangeOnline);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAddressManagementView()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAddressManagementView()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAddressManagementView() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAddressManagementView();

		// Then
		 assertEquals("None", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getInitialEnrollmentDateAsString()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getInitialEnrollmentDateAsString()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetInitialEnrollmentDateAsString() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();
		String initialEnrollmentDate = ""; // UTA: default value
		underTest.setInitialEnrollmentDate(initialEnrollmentDate);

		// When
		String result = underTest.getInitialEnrollmentDateAsString();

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAnniversaryDateAsString()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAnniversaryDateAsString()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAnniversaryDateAsString() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();
		String aciAnniversaryDate = ""; // UTA: default value
		underTest.setAciAnniversaryDate(aciAnniversaryDate);
		String aciAnniversaryYear = ""; // UTA: default value
		underTest.setAciAnniversaryYear(aciAnniversaryYear);

		// When
		String result = underTest.getAnniversaryDateAsString();

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for removeLeadingZeros()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#removeLeadingZeros()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testRemoveLeadingZeros() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		underTest.removeLeadingZeros();

	}

	/**
	 * Parasoft Jtest UTA: Test for getChecksMailedTo()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getChecksMailedTo()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetChecksMailedTo() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getChecksMailedTo();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setChecksMailedTo(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setChecksMailedTo(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetChecksMailedTo() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String checksMailedTo = ""; // UTA: default value
		underTest.setChecksMailedTo(checksMailedTo);

	}

	/**
	 * Parasoft Jtest UTA: Test for isEditable()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isEditable()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsEditable() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isEditable();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEditable(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEditable(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEditable() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean editable = false; // UTA: default value
		underTest.setEditable(editable);

	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantWithdrawalInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getParticipantWithdrawalInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetParticipantWithdrawalInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getParticipantWithdrawalInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setParticipantWithdrawalInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setParticipantWithdrawalInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetParticipantWithdrawalInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String participantWithdrawalInd = ""; // UTA: default value
		underTest.setParticipantWithdrawalInd(participantWithdrawalInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDeferralLimitDollars()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDeferralLimitDollars()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDeferralLimitDollars() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDeferralLimitDollars();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralLimitDollars(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralLimitDollars(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralLimitDollars() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralLimitDollars = ""; // UTA: default value
		underTest.setDeferralLimitDollars(deferralLimitDollars);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDeferralLimitPercent()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDeferralLimitPercent()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDeferralLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDeferralLimitPercent();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralLimitPercent(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralLimitPercent(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralLimitPercent = ""; // UTA: default value
		underTest.setDeferralLimitPercent(deferralLimitPercent);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDeferralType()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDeferralType()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDeferralType() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDeferralType();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralType(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralType(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralType() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralType = ""; // UTA: default value
		underTest.setDeferralType(deferralType);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAciSignupMethod()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAciSignupMethod()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAciSignupMethod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAciSignupMethod();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAciSignupMethod(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAciSignupMethod(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAciSignupMethod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String aciSignupMethod = ""; // UTA: default value
		underTest.setAciSignupMethod(aciSignupMethod);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAutoContributionIncrease()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAutoContributionIncrease()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAutoContributionIncrease() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAutoContributionIncrease();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAutoContributionIncrease(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAutoContributionIncrease(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAutoContributionIncrease() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String autoContributionIncrease = ""; // UTA: default value
		underTest.setAutoContributionIncrease(autoContributionIncrease);

	}

	/**
	 * Parasoft Jtest UTA: Test for getIncreaseAnniversary()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getIncreaseAnniversary()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetIncreaseAnniversary() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getIncreaseAnniversary();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setIncreaseAnniversary(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setIncreaseAnniversary(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetIncreaseAnniversary() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String increaseAnniversary = ""; // UTA: default value
		underTest.setIncreaseAnniversary(increaseAnniversary);

	}

	/**
	 * Parasoft Jtest UTA: Test for getIsFreezePeriod()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getIsFreezePeriod()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetIsFreezePeriod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.getIsFreezePeriod();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setIsFreezePeriod(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setIsFreezePeriod(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetIsFreezePeriod() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean isFreezePeriod = false; // UTA: default value
		underTest.setIsFreezePeriod(isFreezePeriod);

	}

	/**
	 * Parasoft Jtest UTA: Test for getWhoWillReviewWithdrawals()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getWhoWillReviewWithdrawals()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetWhoWillReviewWithdrawals() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getWhoWillReviewWithdrawals();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAllowOnlineLoans()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAllowOnlineLoans()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAllowOnlineLoans() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAllowOnlineLoans();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAllowOnlineLoans(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAllowOnlineLoans(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAllowOnlineLoans() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String allowOnlineLoans = ""; // UTA: default value
		underTest.setAllowOnlineLoans(allowOnlineLoans);

	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantInitiateLoansInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getParticipantInitiateLoansInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetParticipantInitiateLoansInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getParticipantInitiateLoansInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setParticipantInitiateLoansInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setParticipantInitiateLoansInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetParticipantInitiateLoansInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String participantInitiateLoansInd = ""; // UTA: default value
		underTest.setParticipantInitiateLoansInd(participantInitiateLoansInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getWhoWillReviewLoanRequests()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getWhoWillReviewLoanRequests()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetWhoWillReviewLoanRequests() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getWhoWillReviewLoanRequests();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setWhoWillReviewLoanRequests(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setWhoWillReviewLoanRequests(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetWhoWillReviewLoanRequests() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String whoWillReviewLoanRequests = ""; // UTA: default value
		underTest.setWhoWillReviewLoanRequests(whoWillReviewLoanRequests);

	}

	/**
	 * Parasoft Jtest UTA: Test for getCreatorMayApproveLoanRequestsInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getCreatorMayApproveLoanRequestsInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetCreatorMayApproveLoanRequestsInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getCreatorMayApproveLoanRequestsInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setCreatorMayApproveLoanRequestsInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCreatorMayApproveLoanRequestsInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCreatorMayApproveLoanRequestsInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String creatorMayApproveLoanRequestsInd = ""; // UTA: default value
		underTest.setCreatorMayApproveLoanRequestsInd(creatorMayApproveLoanRequestsInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getLoansChecksMailedTo()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getLoansChecksMailedTo()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetLoansChecksMailedTo() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getLoansChecksMailedTo();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setLoansChecksMailedTo(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setLoansChecksMailedTo(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetLoansChecksMailedTo() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String loansChecksMailedTo = ""; // UTA: default value
		underTest.setLoansChecksMailedTo(loansChecksMailedTo);

	}

	/**
	 * Parasoft Jtest UTA: Test for getAllowLoansPackageToGenerate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getAllowLoansPackageToGenerate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetAllowLoansPackageToGenerate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getAllowLoansPackageToGenerate();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setAllowLoansPackageToGenerate(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setAllowLoansPackageToGenerate(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetAllowLoansPackageToGenerate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String allowLoansPackageToGenerate = ""; // UTA: default value
		underTest.setAllowLoansPackageToGenerate(allowLoansPackageToGenerate);

	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanRecordKeepingInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getLoanRecordKeepingInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetLoanRecordKeepingInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getLoanRecordKeepingInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setLoanRecordKeepingInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setLoanRecordKeepingInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetLoanRecordKeepingInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String loanRecordKeepingInd = ""; // UTA: default value
		underTest.setLoanRecordKeepingInd(loanRecordKeepingInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getOutstandingOldILoanRequestCount()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getOutstandingOldILoanRequestCount()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetOutstandingOldILoanRequestCount() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		int result = underTest.getOutstandingOldILoanRequestCount();

		// Then
		 assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setOutstandingOldILoanRequestCount(int)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setOutstandingOldILoanRequestCount(int)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetOutstandingOldILoanRequestCount() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		int outstandingOldILoanRequestCount = 0; // UTA: default value
		underTest.setOutstandingOldILoanRequestCount(outstandingOldILoanRequestCount);

	}

	/**
	 * Parasoft Jtest UTA: Test for getConsentInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getConsentInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetConsentInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getConsentInd();

		// Then
		 assertEquals("consentInd", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setConsentInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setConsentInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetConsentInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String consentInd = ""; // UTA: default value
		underTest.setConsentInd(consentInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getIsHideConsent()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getIsHideConsent()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetIsHideConsent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.getIsHideConsent();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setHideConsent(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setHideConsent(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetHideConsent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean isHideConsent = false; // UTA: default value
		underTest.setHideConsent(isHideConsent);

	}

	/**
	 * Parasoft Jtest UTA: Test for isTpaFirmExists()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isTpaFirmExists()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsTpaFirmExists() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isTpaFirmExists();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setTpaFirmExists(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setTpaFirmExists(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetTpaFirmExists() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean tpaFirmExists = false; // UTA: default value
		underTest.setTpaFirmExists(tpaFirmExists);

	}

	/**
	 * Parasoft Jtest UTA: Test for setDirectMailInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDirectMailInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDirectMailInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String directMailInd = ""; // UTA: default value
		underTest.setDirectMailInd(directMailInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDirectMailInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDirectMailInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDirectMailInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDirectMailInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getSpecialTaxNotice()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSpecialTaxNotice()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetSpecialTaxNotice() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getSpecialTaxNotice();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSpecialTaxNotice(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSpecialTaxNotice(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSpecialTaxNotice() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String specialTaxNotice = ""; // UTA: default value
		underTest.setSpecialTaxNotice(specialTaxNotice);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEligibilityCalculationInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEligibilityCalculationInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEligibilityCalculationInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getEligibilityCalculationInd();

		// Then
		 assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEligibilityCalculationInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEligibilityCalculationInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEligibilityCalculationInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String eligibilityCalculationInd = ""; // UTA: default value
		underTest.setEligibilityCalculationInd(eligibilityCalculationInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEligibilityServiceMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEligibilityServiceMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> result = underTest.getEligibilityServiceMoneyTypes();

		// Then
		 assertNotNull(result);
		 
	}

	/**
	 * Parasoft Jtest UTA: Test for setEligibilityServiceMoneyTypes(ArrayList)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEligibilityServiceMoneyTypes(ArrayList)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> eligibilityServiceMoneyTypes = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		eligibilityServiceMoneyTypes.add(item);
		underTest.setEligibilityServiceMoneyTypes(eligibilityServiceMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for getSelectedEligibilityServiceMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSelectedEligibilityServiceMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception 
	 */
	@Test
	public void testGetSelectedEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> result = underTest.getSelectedEligibilityServiceMoneyTypes();

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSelectedEligibilityServiceMoneyTypes(ArrayList)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSelectedEligibilityServiceMoneyTypes(ArrayList)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSelectedEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> selectedEligibilityServiceMoneyTypes = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		selectedEligibilityServiceMoneyTypes.add(item);
		underTest.setSelectedEligibilityServiceMoneyTypes(selectedEligibilityServiceMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for getContractMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getContractMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetContractMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> result = underTest.getContractMoneyTypes();

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setContractMoneyTypes(ArrayList)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setContractMoneyTypes(ArrayList)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetContractMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> contractMoneyTypes = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		contractMoneyTypes.add(item);
		underTest.setContractMoneyTypes(contractMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for getContractMoneyTypesList()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getContractMoneyTypesList()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetContractMoneyTypesList() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		List<MoneyTypeVO> result = underTest.getContractMoneyTypesList();

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setContractMoneyTypesList(List)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setContractMoneyTypesList(List)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetContractMoneyTypesList() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		List<MoneyTypeVO> contractMoneyTypesList = new ArrayList<>(); // UTA: default value
		MoneyTypeVO item = mock(MoneyTypeVO.class);
		contractMoneyTypesList.add(item);
		underTest.setContractMoneyTypesList(contractMoneyTypesList);

	}

	/**
	 * Parasoft Jtest UTA: Test for setEligibityServiceMoneyTypeId(EligibilityCalculationMoneyType)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEligibityServiceMoneyTypeId(EligibilityCalculationMoneyType)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEligibityServiceMoneyTypeId() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		EligibilityCalculationMoneyType ecMoneyType = mock(EligibilityCalculationMoneyType.class);
		underTest.setEligibityServiceMoneyTypeId(ecMoneyType);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEligibityServiceMoneyTypeId(int)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEligibityServiceMoneyTypeId(int)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEligibityServiceMoneyTypeId() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		int index = 0; // UTA: default value
		EligibilityCalculationMoneyType result = underTest.getEligibityServiceMoneyTypeId(index);

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLastUpdatedEligibilityServiceMoneyTypes()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getLastUpdatedEligibilityServiceMoneyTypes()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetLastUpdatedEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> result = underTest.getLastUpdatedEligibilityServiceMoneyTypes();

		// Then
		 assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setLastUpdatedEligibilityServiceMoneyTypes(ArrayList)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setLastUpdatedEligibilityServiceMoneyTypes(ArrayList)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetLastUpdatedEligibilityServiceMoneyTypes() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ArrayList<EligibilityCalculationMoneyType> lastUpdatedEligibilityServiceMoneyTypes = new ArrayList<>(); // UTA: default value
		EligibilityCalculationMoneyType item = mock(EligibilityCalculationMoneyType.class);
		lastUpdatedEligibilityServiceMoneyTypes.add(item);
		underTest.setLastUpdatedEligibilityServiceMoneyTypes(lastUpdatedEligibilityServiceMoneyTypes);

	}

	/**
	 * Parasoft Jtest UTA: Test for isShowEligibilitySection()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isShowEligibilitySection()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsShowEligibilitySection() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isShowEligibilitySection();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setShowEligibilitySection(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setShowEligibilitySection(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetShowEligibilitySection() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean showEligibilitySection = false; // UTA: default value
		underTest.setShowEligibilitySection(showEligibilitySection);

	}

	/**
	 * Parasoft Jtest UTA: Test for getEligibilityServiceMoneyTypesListSize()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getEligibilityServiceMoneyTypesListSize()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetEligibilityServiceMoneyTypesListSize() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		int result = underTest.getEligibilityServiceMoneyTypesListSize();

		// Then
		// assertEquals(0, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setEligibilityServiceMoneyTypesListSize(int)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setEligibilityServiceMoneyTypesListSize(int)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetEligibilityServiceMoneyTypesListSize() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		int eligibilityServiceMoneyTypesListSize = 0; // UTA: default value
		underTest.setEligibilityServiceMoneyTypesListSize(eligibilityServiceMoneyTypesListSize);

	}

	/**
	 * Parasoft Jtest UTA: Test for getSummaryPlanHighlightAvailable()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSummaryPlanHighlightAvailable()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetSummaryPlanHighlightAvailable() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getSummaryPlanHighlightAvailable();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSummaryPlanHighlightAvailable(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSummaryPlanHighlightAvailable(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSummaryPlanHighlightAvailable() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String summaryPlanHighlightAvailable = ""; // UTA: default value
		underTest.setSummaryPlanHighlightAvailable(summaryPlanHighlightAvailable);

	}

	/**
	 * Parasoft Jtest UTA: Test for getSummaryPlanHighlightReviewed()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSummaryPlanHighlightReviewed()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetSummaryPlanHighlightReviewed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getSummaryPlanHighlightReviewed();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSummaryPlanHighlightReviewed(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSummaryPlanHighlightReviewed(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSummaryPlanHighlightReviewed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String summaryPlanHighlightReviewed = ""; // UTA: default value
		underTest.setSummaryPlanHighlightReviewed(summaryPlanHighlightReviewed);

	}

	/**
	 * Parasoft Jtest UTA: Test for reset(HttpServletRequest)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#reset(HttpServletRequest)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testReset() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		HttpServletRequest request = mock(HttpServletRequest.class);
		underTest.reset(request);

	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantServicesData()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getParticipantServicesData()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetParticipantServicesData() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ParticipantServicesData result = underTest.getParticipantServicesData();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setParticipantServicesData(ParticipantServicesData)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setParticipantServicesData(ParticipantServicesData)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetParticipantServicesData() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		ParticipantServicesData participantServicesData = mock(ParticipantServicesData.class);
		underTest.setParticipantServicesData(participantServicesData);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanSponsorServicesData()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanSponsorServicesData()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanSponsorServicesData() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		PlanSponsorServicesData result = underTest.getPlanSponsorServicesData();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanSponsorServicesData(PlanSponsorServicesData)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanSponsorServicesData(PlanSponsorServicesData)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanSponsorServicesData() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		PlanSponsorServicesData planSponsorServicesData = mock(PlanSponsorServicesData.class);
		underTest.setPlanSponsorServicesData(planSponsorServicesData);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDeferralMaxLimitPercent()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDeferralMaxLimitPercent()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDeferralMaxLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDeferralMaxLimitPercent();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralMaxLimitPercent(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralMaxLimitPercent(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralMaxLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralMaxLimitPercent = ""; // UTA: default value
		underTest.setDeferralMaxLimitPercent(deferralMaxLimitPercent);

	}

	/**
	 * Parasoft Jtest UTA: Test for getDeferralMaxLimitDollars()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getDeferralMaxLimitDollars()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetDeferralMaxLimitDollars() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getDeferralMaxLimitDollars();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDeferralMaxLimitDollars(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDeferralMaxLimitDollars(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDeferralMaxLimitDollars() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String deferralMaxLimitDollars = ""; // UTA: default value
		underTest.setDeferralMaxLimitDollars(deferralMaxLimitDollars);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPayrollCutoff()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPayrollCutoff()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPayrollCutoff() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPayrollCutoff();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPayrollCutoff(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPayrollCutoff(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPayrollCutoff() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String payrollCutoff = ""; // UTA: default value
		underTest.setPayrollCutoff(payrollCutoff);

	}

	/**
	 * Parasoft Jtest UTA: Test for getParticiapntIATs()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getParticiapntIATs()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetParticiapntIATs() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getParticiapntIATs();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setParticiapntIATs(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setParticiapntIATs(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetParticiapntIATs() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String particiapntIATs = ""; // UTA: default value
		underTest.setParticiapntIATs(particiapntIATs);

	}

	/**
	 * Parasoft Jtest UTA: Test for getMoneyTypeEligibilityCriteria()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getMoneyTypeEligibilityCriteria()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetMoneyTypeEligibilityCriteria() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		List<MoneyTypeEligibilityCriterion> result = underTest.getMoneyTypeEligibilityCriteria();

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for setMoneyTypeEligibilityCriteria(List)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setMoneyTypeEligibilityCriteria(List)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetMoneyTypeEligibilityCriteria() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		List<MoneyTypeEligibilityCriterion> moneyTypeEligibilityCriteria = new ArrayList<>(); // UTA: default value
		MoneyTypeEligibilityCriterion item = mock(MoneyTypeEligibilityCriterion.class);
		moneyTypeEligibilityCriteria.add(item);
		underTest.setMoneyTypeEligibilityCriteria(moneyTypeEligibilityCriteria);

	}

	/**
	 * Parasoft Jtest UTA: Test for getMoneyTypesWithFutureDatedDeletionDate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getMoneyTypesWithFutureDatedDeletionDate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetMoneyTypesWithFutureDatedDeletionDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		Map<String, Date> result = underTest.getMoneyTypesWithFutureDatedDeletionDate();

		// Then
		 assertNotNull(result);
		 assertEquals(0, result.size());
		 assertTrue(result.containsValue(null));
	}

	/**
	 * Parasoft Jtest UTA: Test for setMoneyTypesWithFutureDatedDeletionDate(Map)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setMoneyTypesWithFutureDatedDeletionDate(Map)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetMoneyTypesWithFutureDatedDeletionDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		Map<String, Date> moneyTypesWithFutureDatedDeletionDate = new HashMap<>(); // UTA: default value
		String key = ""; // UTA: default value
		Date value = mock(Date.class);
		moneyTypesWithFutureDatedDeletionDate.put(key, value);
		underTest.setMoneyTypesWithFutureDatedDeletionDate(moneyTypesWithFutureDatedDeletionDate);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanAciInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanAciInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanAciInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPlanAciInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanDeferralLimitPercent()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanDeferralLimitPercent()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanDeferralLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPlanDeferralLimitPercent();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanAciInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanAciInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanAciInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String planAciInd = ""; // UTA: default value
		underTest.setPlanAciInd(planAciInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanDeferralLimitPercent(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanDeferralLimitPercent(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanDeferralLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String planDeferralLimitPercent = ""; // UTA: default value
		underTest.setPlanDeferralLimitPercent(planDeferralLimitPercent);

	}

	/**
	 * Parasoft Jtest UTA: Test for getPlanDeferralMaxLimitPercent()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getPlanDeferralMaxLimitPercent()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetPlanDeferralMaxLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getPlanDeferralMaxLimitPercent();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setPlanDeferralMaxLimitPercent(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setPlanDeferralMaxLimitPercent(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetPlanDeferralMaxLimitPercent() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String planDeferralMaxLimitPercent = ""; // UTA: default value
		underTest.setPlanDeferralMaxLimitPercent(planDeferralMaxLimitPercent);

	}

	/**
	 * Parasoft Jtest UTA: Test for getCheckEligibilityCalculationInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getCheckEligibilityCalculationInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetCheckEligibilityCalculationInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getCheckEligibilityCalculationInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setCheckEligibilityCalculationInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCheckEligibilityCalculationInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCheckEligibilityCalculationInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String checkEligibilityCalculationInd = ""; // UTA: default value
		underTest.setCheckEligibilityCalculationInd(checkEligibilityCalculationInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getResetDeferralValues()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getResetDeferralValues()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetResetDeferralValues() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getResetDeferralValues();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setResetDeferralValues(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setResetDeferralValues(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetResetDeferralValues() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String resetDeferralValues = ""; // UTA: default value
		underTest.setResetDeferralValues(resetDeferralValues);

	}

	/**
	 * Parasoft Jtest UTA: Test for getOnlineBeneficiaryInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getOnlineBeneficiaryInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetOnlineBeneficiaryInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getOnlineBeneficiaryInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setOnlineBeneficiaryInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setOnlineBeneficiaryInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetOnlineBeneficiaryInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String onlineBeneficiaryInd = ""; // UTA: default value
		underTest.setOnlineBeneficiaryInd(onlineBeneficiaryInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for isIpsServiceSuppressed()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isIpsServiceSuppressed()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsIpsServiceSuppressed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isIpsServiceSuppressed();

		// Then
		 assertEquals(true, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setIpsServiceSuppressed(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setIpsServiceSuppressed(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetIpsServiceSuppressed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean ipsServiceSuppressed = false; // UTA: default value
		underTest.setIpsServiceSuppressed(ipsServiceSuppressed);

	}

	/**
	 * Parasoft Jtest UTA: Test for isCoFiduciary()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isCoFiduciary()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsCoFiduciary() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isCoFiduciary();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setCoFiduciary(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCoFiduciary(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCoFiduciary() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean coFiduciary = false; // UTA: default value
		underTest.setCoFiduciary(coFiduciary);

	}

	/**
	 * Parasoft Jtest UTA: Test for isCoFidFeatureSuppressed()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isCoFidFeatureSuppressed()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsCoFidFeatureSuppressed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isCoFidFeatureSuppressed();

		// Then
		 assertTrue(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setCoFidFeatureSuppressed(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setCoFidFeatureSuppressed(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetCoFidFeatureSuppressed() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean coFidFeatureSuppressed = false; // UTA: default value
		underTest.setCoFidFeatureSuppressed(coFidFeatureSuppressed);

	}

	/**
	 * Parasoft Jtest UTA: Test for getSelectedInvestmentProfile()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getSelectedInvestmentProfile()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetSelectedInvestmentProfile() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getSelectedInvestmentProfile();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setSelectedInvestmentProfile(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setSelectedInvestmentProfile(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetSelectedInvestmentProfile() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String selectedInvestmentProfile = ""; // UTA: default value
		underTest.setSelectedInvestmentProfile(selectedInvestmentProfile);

	}

	/**
	 * Parasoft Jtest UTA: Test for getNoticeServiceInd()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getNoticeServiceInd()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetNoticeServiceInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getNoticeServiceInd();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setNoticeServiceInd(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setNoticeServiceInd(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetNoticeServiceInd() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String noticeServiceInd = ""; // UTA: default value
		underTest.setNoticeServiceInd(noticeServiceInd);

	}

	/**
	 * Parasoft Jtest UTA: Test for getNoticeTypeSelected()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getNoticeTypeSelected()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetNoticeTypeSelected() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getNoticeTypeSelected();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setNoticeTypeSelected(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setNoticeTypeSelected(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetNoticeTypeSelected() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String noticeTypeSelected = ""; // UTA: default value
		underTest.setNoticeTypeSelected(noticeTypeSelected);

	}

	/**
	 * Parasoft Jtest UTA: Test for isDisplayNoticeGeneration()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#isDisplayNoticeGeneration()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testIsDisplayNoticeGeneration() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean result = underTest.isDisplayNoticeGeneration();

		// Then
		 assertFalse(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setDisplayNoticeGeneration(boolean)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setDisplayNoticeGeneration(boolean)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetDisplayNoticeGeneration() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		boolean displayNoticeGeneration = false; // UTA: default value
		underTest.setDisplayNoticeGeneration(displayNoticeGeneration);

	}

	/**
	 * Parasoft Jtest UTA: Test for getManagedAccountService()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getManagedAccountService()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetManagedAccountService() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getManagedAccountService();

		// Then
		 assertEquals(null, result);
	}

	/**
	 * Parasoft Jtest UTA: Test for setManagedAccountService(String)
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#setManagedAccountService(String)
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testSetManagedAccountService() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String managedAccountService = ""; // UTA: default value
		underTest.setManagedAccountService(managedAccountService);

	}

	/**
	 * Parasoft Jtest UTA: Test for getManagedAccountServiceAvailabilityDate()
	 *
	 * @see com.manulife.pension.ps.web.contract.csf.CsfForm#getManagedAccountServiceAvailabilityDate()
	 * @author ashoksu
	 * @throws Exception - Exception
	 */
	@Test
	public void testGetManagedAccountServiceAvailabilityDate() throws Exception {
		// Given
		CsfForm underTest = new CsfForm();

		// When
		String result = underTest.getManagedAccountServiceAvailabilityDate();

		// Then
		 assertEquals(null, result);
	}
	
	
}