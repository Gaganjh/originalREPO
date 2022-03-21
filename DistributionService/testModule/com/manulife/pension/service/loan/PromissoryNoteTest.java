/**
 * 
 */
package com.manulife.pension.service.loan;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.loan.valueobject.document.PromissoryNote;

/**
 * @author DODDAAN
 *
 */
public class PromissoryNoteTest {

	PromissoryNote note;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		note = new PromissoryNote("ROY", "Paul", "R", "Dummy Plan", new BigDecimal(
				1000), new BigDecimal(12.5), 60, "M", new BigDecimal(50),
				new BigDecimal(100), new Date(), new Date(), "Dummy Contract",
				"defaultProvision", new BigDecimal(5000), true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.manulife.pension.service.loan.valuobject.document.PromissoryNote#toXML()}.
	 */
	@Test
	public void testToXML() {
		String xml = note.toXML();
		System.out.print(xml);
		Assert.assertNotNull(xml);
	}
}
