package com.manulife.pension.service.loan;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;

public class TruthInLendingNoticeTest {
	
	TruthInLendingNotice notice;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		notice = new TruthInLendingNotice();
		notice.setFirstName("ROY");
		notice.setLastName("PAUL");
		notice.setMiddleInitial("P");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice#toXML()}.
	 */
	@Test
	public void testToXML() {
		String xml = notice.toXML();
		System.out.print(xml);
		Assert.assertNotNull(xml);
	}
}
