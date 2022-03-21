package com.manulife.pension.service.loan;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

import com.manulife.pension.service.loan.valueobject.document.LoanForm;
import com.manulife.pension.service.util.iloans.PropertyManager;
import org.powermock.modules.junit4.PowerMockRunner;
@PrepareForTest({PropertyManager.class })
@RunWith(PowerMockRunner.class)
public class LoanFormTest {

	LoanForm loanForm;
	LoanForm loanFormForBundledGA;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		loanForm = new LoanForm();
		loanForm.setFirstName("ROY");
		loanForm.setLastName("PAUL");
		loanForm.setMiddleInitial("P");
		loanForm.setContractId(65921);
		
		//for bundled GA object
		loanFormForBundledGA = new LoanForm();
		loanFormForBundledGA.setFirstName("ROY");
		loanFormForBundledGA.setLastName("PAUL");
		loanFormForBundledGA.setMiddleInitial("P");
		loanFormForBundledGA.setContractId(70300);
		loanFormForBundledGA.setBundledGaIndicator(true);
		
		spy(PropertyManager.class);
		doReturn("STR").when(PropertyManager.class);
		PropertyManager.getString(anyString());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.manulife.pension.service.loan.valuobject.document.LoanForm#toXML()}.
	 */
	@Test
	public void testToXML() {
		String xml = loanForm.toXML();
		Assert.assertNotNull(xml);
	}
	
	/**
	 * Test method for {@link com.manulife.pension.service.loan.valuobject.document.LoanForm#toXML()}.
	 * Validate for Bundled GA.
	 */
	@Test
	public void testToXMLForBundledGA() {
		String xml = loanFormForBundledGA.toXML();
		System.out.print(xml);
		Assert.assertNotNull(xml);
	}
}
