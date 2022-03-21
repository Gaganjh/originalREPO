/**
 * 
 */
package com.manulife.pension.service.loan.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Parasoft Jtest UTA: Test class for LoanDocumentHelper
 *
 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper
 * @author patelpo
 */
@PrepareForTest({ DateFormat.class, EnvironmentServiceDelegate.class })
@RunWith(PowerMockRunner.class)
public class LoanDocumentHelperTest {

	/**
	 * Parasoft Jtest UTA: Test for currencyFormatter(BigDecimal)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#currencyFormatter(BigDecimal)
	 * @author patelpo
	 */
	@Test
	public void testCurrencyFormatter() throws Throwable {
		// When
		BigDecimal value = BigDecimal.ZERO; // UTA: default value
		String result = LoanDocumentHelper.currencyFormatter(value);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for dateFormatter(Date, SimpleDateFormat)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#dateFormatter(Date, SimpleDateFormat)
	 * @author patelpo
	 */
	@Test
	public void testDateFormatter() throws Throwable {
		// When
		Date value = null;
		SimpleDateFormat dateFormat = mockSimpleDateFormat();
		String result = LoanDocumentHelper.dateFormatter(value, dateFormat);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	@Test(expected = NullPointerException.class)
	public void testDateFormatter_Exception() throws Throwable {
		// When
		Date value = mock(Date.class);
		SimpleDateFormat dateFormat = mockSimpleDateFormat();
		String result = LoanDocumentHelper.dateFormatter(value, dateFormat);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Helper method to generate and configure mock of SimpleDateFormat
	 */
	private static SimpleDateFormat mockSimpleDateFormat() throws Throwable {
		SimpleDateFormat dateFormat = mock(SimpleDateFormat.class);
		String formatResult = ""; // UTA: default value
		when(dateFormat.format(any(Date.class))).thenReturn(formatResult);
		return dateFormat;
	}

	/**
	 * Parasoft Jtest UTA: Test for formatPercentageFormatter(BigDecimal)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#formatPercentageFormatter(BigDecimal)
	 * @author patelpo
	 */
	@Test
	public void testFormatPercentageFormatter() throws Throwable {
		// When
		BigDecimal value = BigDecimal.ZERO; // UTA: default value
		String result = LoanDocumentHelper.formatPercentageFormatter(value);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getCompanyName(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getCompanyName(String)
	 * @author patelpo
	 */
	@Test
	public void testGetCompanyName() throws Throwable {
		// When
		String manulifeId = "019"; // UTA: default value
		String result = LoanDocumentHelper.getCompanyName(manulifeId);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetCompanyName_1() throws Throwable {
		// When
		String manulifeId = "094"; // UTA: default value
		String result = LoanDocumentHelper.getCompanyName(manulifeId);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getCountryName(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getCountryName(String)
	 * @author patelpo
	 */
	@Test
	public void testGetCountryName() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// When
		String country = "Test"; // UTA: default value
		String result = LoanDocumentHelper.getCountryName(country);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test(expected=RuntimeException.class)
	public void testGetCountryName_exception() throws Throwable {
				// When
		String country = ""; // UTA: default value
		String result = LoanDocumentHelper.getCountryName(country);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getFormattedAddress(String, String, String, String, String, String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getFormattedAddress(String, String, String, String, String, String)
	 * @author patelpo
	 */
	@Test
	public void testGetFormattedAddress() throws Throwable {
		spy(EnvironmentServiceDelegate.class);

		EnvironmentServiceDelegate getInstanceResult = mock(EnvironmentServiceDelegate.class); // UTA: default value
		doReturn(getInstanceResult).when(EnvironmentServiceDelegate.class, "getInstance", anyString());

		// When
		String addressLine1 = ""; // UTA: default value
		String addressLine2 = ""; // UTA: default value
		String city = ""; // UTA: default value
		String stateCode = ""; // UTA: default value
		String zipCode = "5770028"; // UTA: default value
		String country = "India"; // UTA: default value
		String result = LoanDocumentHelper.getFormattedAddress(addressLine1, addressLine2, city, stateCode, zipCode,
				country);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getFormattedParticipantName(String, String, String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getFormattedParticipantName(String, String, String)
	 * @author patelpo
	 */
	@Test
	public void testGetFormattedParticipantName() throws Throwable {
		// When
		String firstName = ""; // UTA: default value
		String lastName = ""; // UTA: default value
		String middleInitial = ""; // UTA: default value
		String result = LoanDocumentHelper.getFormattedParticipantName(firstName, lastName, middleInitial);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getFormattedParticipantNameAO(String, String, String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getFormattedParticipantNameAO(String, String, String)
	 * @author patelpo
	 */
	@Test
	public void testGetFormattedParticipantNameAO() throws Throwable {
		// When
		String firstName = ""; // UTA: default value
		String lastName = ""; // UTA: default value
		String middleInitial = "GP"; // UTA: default value
		String result = LoanDocumentHelper.getFormattedParticipantNameAO(firstName, lastName, middleInitial);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getFormattedSSN(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getFormattedSSN(String)
	 * @author patelpo
	 */
	@Test
	public void testGetFormattedSSN() throws Throwable {
		// When
		String ssn = "123456789"; // UTA: default value
		String result = LoanDocumentHelper.getFormattedSSN(ssn);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getFormattedZipCode(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getFormattedZipCode(String)
	 * @author patelpo
	 */
	@Test
	public void testGetFormattedZipCode() throws Throwable {
		// When
		String zipCode = "5770028"; // UTA: default value
		String result = LoanDocumentHelper.getFormattedZipCode(zipCode);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanTypeText(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getLoanTypeText(String)
	 * @author patelpo
	 */
	@Test
	public void testGetLoanTypeText() throws Throwable {
		// When
		String loanType = "GP"; // UTA: default value
		String result = LoanDocumentHelper.getLoanTypeText(loanType);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetLoanTypeText_1() throws Throwable {
		// When
		String loanType = "HA"; // UTA: default value
		String result = LoanDocumentHelper.getLoanTypeText(loanType);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetLoanTypeText_2() throws Throwable {
		// When
		String loanType = "PR"; // UTA: default value
		String result = LoanDocumentHelper.getLoanTypeText(loanType);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getSortedList(List)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getSortedList(List)
	 * @author patelpo
	 */
	@Test
	public void testGetSortedList() throws Throwable {
		// When
		List<String> value = new ArrayList<String>(); // UTA: default value
		List<String> result = LoanDocumentHelper.getSortedList(value);

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(""));
	}

	/**
	 * Parasoft Jtest UTA: Test for getStringValue(Object)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#getStringValue(Object)
	 * @author patelpo
	 */
	@Test
	public void testGetStringValue() throws Throwable {
		// When
		Object obj = new Object(); // UTA: default value
		String result = LoanDocumentHelper.getStringValue(obj);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetStringValue_1() throws Throwable {
		// When
		Object obj = null; // UTA: default value
		String result = LoanDocumentHelper.getStringValue(obj);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetStringValue_2() throws Throwable {
		// When
		Object obj = new Boolean(true); // UTA: default value
		String result = LoanDocumentHelper.getStringValue(obj);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetStringValue_3() throws Throwable {
		// When
		Object obj = new Integer(1); // UTA: default value
		String result = LoanDocumentHelper.getStringValue(obj);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testGetStringValue_4() throws Throwable {
		// When
		Object obj = new BigDecimal(10); // UTA: default value
		String result = LoanDocumentHelper.getStringValue(obj);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for paymentFrequency(String)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#paymentFrequency(String)
	 * @author patelpo
	 */
	@Test
	public void testPaymentFrequency() throws Throwable {
		// When
		String paymentFrequency = "W"; // UTA: default value
		String result = LoanDocumentHelper.paymentFrequency(paymentFrequency);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testPaymentFrequency_1() throws Throwable {
		// When
		String paymentFrequency = "B"; // UTA: default value
		String result = LoanDocumentHelper.paymentFrequency(paymentFrequency);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testPaymentFrequency_2() throws Throwable {
		// When
		String paymentFrequency = "H"; // UTA: default value
		String result = LoanDocumentHelper.paymentFrequency(paymentFrequency);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
	@Test
	public void testPaymentFrequency_3() throws Throwable {
		// When
		String paymentFrequency = "M"; // UTA: default value
		String result = LoanDocumentHelper.paymentFrequency(paymentFrequency);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}

	/**
	 * Parasoft Jtest UTA: Test for percentageFormatter(BigDecimal)
	 *
	 * @see com.manulife.pension.service.loan.util.LoanDocumentHelper#percentageFormatter(BigDecimal)
	 * @author patelpo
	 */
	@Test
	public void testPercentageFormatter() throws Throwable {
		// When
		BigDecimal value = BigDecimal.ZERO; // UTA: default value
		String result = LoanDocumentHelper.percentageFormatter(value);

		// Then
		// assertNotNull(result);
		// assertEquals("", result);
	}
}