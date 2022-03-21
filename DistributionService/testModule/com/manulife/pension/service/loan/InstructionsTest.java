package com.manulife.pension.service.loan;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.loan.valueobject.document.Instructions;

public class InstructionsTest {

	Instructions instructions;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		instructions = new Instructions();
		instructions.setFirstName("ROY");
		instructions.setLastName("PAUL");
		instructions.setMiddleInitial("P");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.manulife.pension.service.loan.valueobject.document.Instructions#toXML()}
	 *
	 */
	@Test
	public void testToXML() {
		String xml = instructions.toXML();
		System.out.print(xml);
		Assert.assertNotNull(xml);
	}
}
