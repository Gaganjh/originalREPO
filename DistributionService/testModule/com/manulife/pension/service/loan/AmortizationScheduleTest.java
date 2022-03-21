package com.manulife.pension.service.loan;

import java.util.Calendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule;

public class AmortizationScheduleTest {

	AmortizationSchedule amortizationSchedule;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		amortizationSchedule = new AmortizationSchedule();
		amortizationSchedule.setFirstName("ROY");
		amortizationSchedule.setLastName("PAUL");
		amortizationSchedule.setMiddleInitial("P");
		amortizationSchedule.setFirstPaymentDate(Calendar.getInstance().getTime());
		amortizationSchedule.setFinalPaymentDate(Calendar.getInstance().getTime());
		amortizationSchedule.setSocialSecurityNumber("123456789");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.manulife.pension.service.loan.valueobject.document.AmortizationSchedule#toXML()}
	 *
	 */
	@Test
	public void testToXML() {
		String xml = amortizationSchedule.toXML();
		System.out.print(xml);
		Assert.assertNotNull(xml);
	}
}
