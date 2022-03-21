/**
 * 
 */
package com.manulife.pension.service.loan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.manulife.pension.service.loan.LoanMessage.Type;

import junitparams.internal.parameters.toarray.ParamsToArrayConverter;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Parasoft Jtest UTA: Test class for LoanMessage
 *
 * @see com.manulife.pension.service.loan.LoanMessage
 * @author patelpo
 */
public class LoanMessageTest {

	/**
	 * Parasoft Jtest UTA: Test for getErrorCode()
	 *
	 * @see com.manulife.pension.service.loan.LoanMessage#getErrorCode()
	 * @author patelpo
	 */
	@Test
	public void testGetErrorCode() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanMessage underTest = new LoanMessage(id);

		// When
		LoanErrorCode result = underTest.getErrorCode();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetErrorCode_1() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanField field = LoanField.ABA_ROUTING_NUMBER;
		LoanMessage underTest = new LoanMessage(id , field);

		// When
		LoanErrorCode result = underTest.getErrorCode();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetErrorCode_2() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanField field = LoanField.ABA_ROUTING_NUMBER;
		List<String> params = new ArrayList();
		params.add("Test");
		LoanMessage underTest = new LoanMessage(id , field, params);

		// When
		LoanErrorCode result = underTest.getErrorCode();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetErrorCode_3() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanField field = LoanField.ABA_ROUTING_NUMBER;
		String[] params = new String[1];
		params[0]="Test";
		LoanMessage underTest = new LoanMessage(id , field, params);

		// When
		LoanErrorCode result = underTest.getErrorCode();

		// Then
		// assertNotNull(result);
	}
	@Test
	public void testGetErrorCode_4() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanField field = LoanField.ABA_ROUTING_NUMBER;
		String params = "Test";
		LoanMessage underTest = new LoanMessage(id , field, params);

		// When
		LoanErrorCode result = underTest.getErrorCode();

		// Then
		// assertNotNull(result);
	}
	
	/**
	 * Parasoft Jtest UTA: Test for getFieldNames()
	 *
	 * @see com.manulife.pension.service.loan.LoanMessage#getFieldNames()
	 * @author patelpo
	 */
	@Test
	public void testGetFieldNames() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanMessage underTest = new LoanMessage(id);

		// When
		List<String> result = underTest.getFieldNames();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(""));
	}
	/*@Test
	public void testGetFieldNames_1() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanField field = LoanField.ABA_ROUTING_NUMBER;
		String params = "Test";
		
		LoanMessage underTest = new LoanMessage(id, field, params);

		// When
		List<String> result = underTest.getFieldNames();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(""));
	}*/
	/**
	 * Parasoft Jtest UTA: Test for getParams()
	 *
	 * @see com.manulife.pension.service.loan.LoanMessage#getParams()
	 * @author patelpo
	 */
	@Test
	public void testGetParams() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanMessage underTest = new LoanMessage(id);

		// When
		List<String> result = underTest.getParams();

		// Then
		// assertNotNull(result);
		// assertEquals(0, result.size());
		// assertTrue(result.contains(""));
	}

	/**
	 * Parasoft Jtest UTA: Test for getType()
	 *
	 * @see com.manulife.pension.service.loan.LoanMessage#getType()
	 * @author patelpo
	 */
	@Test
	public void testGetType() throws Throwable {
		// Given
		LoanErrorCode id = LoanErrorCode.LRK01_IS_OFF; // UTA: default value
		LoanMessage underTest = new LoanMessage(id);

		// When
		Type result = underTest.getType();

		// Then
		// assertNotNull(result);
	}
	
}