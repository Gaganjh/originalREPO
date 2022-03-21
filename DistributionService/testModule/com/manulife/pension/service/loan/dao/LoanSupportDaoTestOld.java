package com.manulife.pension.service.loan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.loan.valueobject.LoanEventData;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.util.JdbcHelper;

public class LoanSupportDaoTestOld {

	private static LoanSupportDao dao;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		MockContainerEnvironment.initialize();
		dao = new LoanSupportDao(
				JdbcHelper
						.getCachedDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE));
		// setting up some data for out standing old i:loan requests
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			DataSource ds = JdbcHelper.getCachedDataSource(DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE);
			conn = ds.getConnection();
			stmt = conn.prepareStatement("UPDATE EZK100C.PARTICIPANT_LOAN_REQUEST SET REQ_EXPIRY_DATE=CURRENT DATE+1 DAY WHERE CONTRACT_ID=70300 AND LOAN_REQUEST_STATUS_CODE IN ('RE', 'PE', 'AP')");
			int i = stmt.executeUpdate();
			/*stmt.close();
			conn.close();*/ 
		} catch (Exception ex) {
			/*stmt.close();
			conn.close();*/
		} 
		finally {
			stmt.close();
			conn.close();
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		MockContainerEnvironment.destroy();
	}

	@Test
	public void testGetLoanPlanData() throws Exception {
		/*
		 * Can't really test the data... nothing in the homestate has loan plan
		 * data.
		 */
		LoanPlanData data = dao.getLoanPlanData(70300);
		System.err.println(data);
	}

	@Test
	public void testGetLoanParticipantData() throws Exception {
		/**
		 * To prepare data for this test, you need to run the following SQL:
		 * 
		 * select * from ezk100.PARTICIPANT_CURRENT_BAL_LSA where contract_id =
		 * 70300 and participant_id in (select participant_id from
		 * ezk100.participant_loan where contract_id = 70300);
		 * 
		 * Then, pick any profile ID and contract number combination
		 */
		List<LoanMoneyType> moneyTypes = dao.getParticipantMoneyTypesForLoans(
				70300, 112323993);

		Assert.assertEquals(2, moneyTypes.size());
		Assert.assertEquals("EEDEF", moneyTypes.get(0).getMoneyTypeId());
		Assert.assertEquals(new BigDecimal("65.30"), moneyTypes.get(0)
				.getAccountBalance());
		Assert.assertEquals("ERMC2", moneyTypes.get(1).getMoneyTypeId());
		Assert.assertEquals(new BigDecimal("62.26"), moneyTypes.get(1)
				.getAccountBalance());
	}
	
	@Test
	public void testGetOutstandingOldIloanRequestsCount() throws Exception {
		Integer requestCount = dao.getOutstandingOldIloanRequestsCount(70300);
		Assert.assertNotNull(requestCount);
		// Uncomment this section if you want to print the data that the method returns to console
		if (requestCount != null) {
			System.out.println("Number of outstanding i:loan requests : "+requestCount);
			}	
	}
	
	@Test
	public void testGetLoanDataForEventMessages() throws Exception {
		LoanEventData data = dao.getLoanDataForEventMessages(3500001);
		Assert.assertNotNull(data);
		if (data != null) {
			System.out.println(data.toString());
		}
	}
	
	@Test
	public void testGetContractName() throws Exception {
		String data = dao.getContractName(11847);
		Assert.assertNotNull(data);
		if (data != null) {
			System.out.println(data);
		}
	}		
	
	@Test
	public void testGetOutstandingLoan() throws Exception {
		OutstandingLoan loan = dao.getOutstandingLoan(70300, 112324728L, 1);
		Assert.assertNotNull(loan);
		
		Assert.assertEquals(500.00, loan.getOutstandingPrincipalAmount().doubleValue(), 0.01);
		if (loan != null) {
			System.out.println(loan);
		}		

	}

	
}
