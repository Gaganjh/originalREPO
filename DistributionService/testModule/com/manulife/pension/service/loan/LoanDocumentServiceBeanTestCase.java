package com.manulife.pension.service.loan;

import java.io.File;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Assert;
import org.mockejb.SessionBeanDescriptor;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanState;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.domain.LoanStateFactory;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.util.fop.FopUtils;

/**
 * Test case for the LoanDocumentServiceBean
 * 
 * To run this testcase  LoanRequestForm.xsl  and PromissoryNote.xslt to the database.
 * 
 * @author jthangad
 */
public class LoanDocumentServiceBeanTestCase extends
		DistributionContainerEnvironment {

	private static final String MONEY_TYPE_EXC_IND_ON = "UPDATE STP100.LOAN_MONEY_TYPE SET MONEY_TYPE_EXCLUDE_IND='Y' WHERE SUBMISSION_ID= ? and MONEY_TYPE_ID='EEDEF'";

	private static final String MONEY_TYPE_EXC_IND_OFF = "UPDATE STP100.LOAN_MONEY_TYPE SET MONEY_TYPE_EXCLUDE_IND='N' WHERE SUBMISSION_ID= ?";

	private static final String SPOUSAL_CONSENT_N = "UPDATE EZK100C.PLAN_WEB SET SPOUSAL_CONSENT_REQD_IND='N' WHERE PLAN_ID= ?";

	private static final String SPOUSAL_CONSENT_Y = "UPDATE EZK100C.PLAN_WEB SET SPOUSAL_CONSENT_REQD_IND='Y' WHERE PLAN_ID= ?";

	private static final String SPOUSAL_CONSENT_U = "UPDATE EZK100C.PLAN_WEB SET SPOUSAL_CONSENT_REQD_IND='U' WHERE PLAN_ID= ?";

	private static final String TPA_FEE_NON_ZERO = "UPDATE STP100.FEE SET FEE_VALUE=0.10 WHERE SUBMISSION_ID= ?";

	private static final String TPA_FEE_ZERO = "UPDATE STP100.FEE SET FEE_VALUE=0.00 WHERE SUBMISSION_ID= ?";
	
	private static final String SET_LOAN_AMOUNT = "UPDATE STP100.LOAN_STAGE_PARAMETER SET LOAN_AMOUNT=10 WHERE SUBMISSION_ID= ?";
	
	private static final String SELECT_CSF = "SELECT SERVICE_FEATURE_CODE ,SERVICE_FEATURE_VALUE FROM EZK100C.CONTRACT_SERVICE_FEATURE WHERE CONTRACT_ID=70300";

	private static final String UPDATE_CSF = "UPDATE EZK100C.CONTRACT_SERVICE_FEATURE	SET SERVICE_FEATURE_VALUE='Y' WHERE CONTRACT_ID =70300";

	private static final String Insert_CSF = "INSERT INTO EZK100C.CONTRACT_SERVICE_FEATURE (CONTRACT_ID,SERVICE_FEATURE_CODE,SERVICE_FEATURE_VALUE) VALUES (70300, 'UOL','Y')";
	
	private static final int TEST_CONTRACT_ID = 11847;
	
	private static final int TEST_PROFILE_ID = 130747405;

	private DataSource stpDatasource = null;

	private DataSource csdbDatasource = null;

	private static Integer submissionId = null;
	
	private static boolean loanRequestFlag = false;
	
	/**
	 * Empty Constructor
	 */
	public LoanDocumentServiceBeanTestCase() {
		super();
	}

	/**
	 * Sets up the fixture
	 * 
	 * @throws Exception
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();

		SessionBeanDescriptor loanDocumentServiceLocalBeanDescriptor = new SessionBeanDescriptor(
				"java:comp/env/ejb/LoanDocumentServiceLocal",
				LoanDocumentServiceLocalHome.class, LoanDocumentService.class,
				LoanDocumentServiceBean.class);
		SessionBeanDescriptor loanServiceLocalBeanDescriptor = new SessionBeanDescriptor(
				"java:comp/env/ejb/LoanServiceLocal",
				LoanServiceLocalHome.class, LoanService.class,
				LoanServiceBean.class);
		MockContainerEnvironment.getMockContainer().deploy(
				loanDocumentServiceLocalBeanDescriptor);
		MockContainerEnvironment.getMockContainer().deploy(
				loanServiceLocalBeanDescriptor);
		if (stpDatasource == null) {
			stpDatasource = (DataSource) new InitialContext()
					.lookup("jdbc/customerService");
		}
		if (csdbDatasource == null) {
			csdbDatasource = (DataSource) new InitialContext()
					.lookup("jdbc/customerService");
		}
		
		//Creates loan request only once
		if(loanRequestFlag == false) {
			initiate();
		}
	}

	/**
	 * Tears down the fixture
	 * 
	 * @throws Exception
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	protected LoanDocumentService getLoanDocumentService()
			throws RemoteException, CreateException, NamingException {
		return LoanDocumentServiceUtil.getLocalHome().create();
	}

	/**
	 * Test case for getPromissoryNoteAndIrrevocablePledgeHtml method
	 * 
	 * @throws Exception
	 */
	public void testGetPromissoryNoteAndIrrevocablePledgeHtml()
			throws Exception {

		String result = getLoanDocumentService()
				.getPromissoryNoteAndIrrevocablePledgeHtml(TEST_PROFILE_ID, TEST_CONTRACT_ID,
						submissionId);
		System.out.println("OUTPUT:"+result);
		if (result == null) {
			assertFalse("Problem occured while getting the html", true);
		}
	}
	
	/**
	 * Test case for getTruthInLendingNoticeHtml method
	 * 
	 * @throws Exception
	 */
	public void testGetTruthInLendingNoticeHtml()
			throws Exception {

		String result = getLoanDocumentService()
				.getTruthInLendingNoticeHtml(TEST_PROFILE_ID, TEST_CONTRACT_ID,
						submissionId);
		System.out.println("OUTPUT:"+result);
		if (result == null) {
			assertFalse("Problem occured while getting the html", true);
		}
	}
	
	/**
	 * Test case for getAmortizationScheduleHtml() method
	 * 
	 * @throws Exception
	 */
	public void testGetAmortizationScheduleHtml() throws Exception {

		String result = getLoanDocumentService().getAmortizationScheduleHtml(
				TEST_PROFILE_ID, TEST_CONTRACT_ID, submissionId);
		System.out.println("OUTPUT:" + result);
		if (result == null) {
			assertFalse("Problem occured while getting the html", true);
		}
	}

	/**
	 * Creates pdf without money types table
	 * 
	 * @throws Exception
	 */
	public void testGetLoanFormWithOutMoneyTypes() throws Exception {
		try {
			executeStatement(MONEY_TYPE_EXC_IND_OFF, submissionId,
					stpDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}
	
	/**
	 * Creates pdf with money types table
	 * 
	 * @throws Exception
	 */
	public void testGetLoanFormWithMoneyTypes() throws Exception {
		try {
			executeStatement(SET_LOAN_AMOUNT, submissionId, stpDatasource);
			executeStatement(MONEY_TYPE_EXC_IND_ON, submissionId,
					stpDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}

	}

	/**
	 * Creates the loan form pdf with spousalconsentreqdind = Y
	 *
	 */
	public void testGetLoanFormSecGVerA() {
		try {
			int arg = 70300;
			executeStatement(SPOUSAL_CONSENT_Y, arg, csdbDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}

	/**
	 * Creates the loan form pdf with spousalconsentreqdind = N
	 *
	 */
	public void testGetLoanFormSecGVerB() {
		try {
			int arg = 70300;
			executeStatement(SPOUSAL_CONSENT_N, arg, csdbDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}

	/**
	 * Creates the loan form pdf with spousalconsentreqdind = U
	 *
	 */
	public void testGetLoanFormSecGVerC() {
		try {
			int arg = 70300;
			executeStatement(SPOUSAL_CONSENT_U, arg, csdbDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}

	/**
	 * Creates the loan form pdf with FEE_VALUE = 0
	 *
	 */
	public void testLoanFormSecFVerA() {
		try {
			executeStatement(TPA_FEE_ZERO, submissionId, stpDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}


	/**
	 * Creates the loan form pdf with FEE_VALUE >0
	 *
	 */
	public void testLoanFormSecFVerB() {
		try {
			executeStatement(TPA_FEE_NON_ZERO, submissionId, stpDatasource);
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			assertFalse("Test case failed", true);
		}
	}

	/**
	 * Test case for getLoanPackage method
	 * 
	 * @throws Exception
	 */
	public void testGetLoanPackage() throws Exception {
		try {
			byte result[] = getLoanDocumentService().getLoanPackage(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Test case failed", true);
		}

	}
	
	/**
	 * Test case for getLoanDocument method "draft=false"
	 * TODO: Watermark is now dependent on the status of the loan. We need to fix this test case later.
	 * @throws Exception
	 */
	public void testGetLoanDocumentWithoutDraftWaterMark() throws Exception {
		try {
			byte result[] = getLoanDocumentService().getLoanDocuments(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId, false);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Test case failed", true);
		}
	}
	
	/**
	 * Test case for getLoanDocument method "draft=true"
	 * 
	 * @throws Exception
	 */
	public void testGetLoanDocumentWithDraftWaterMark() throws Exception {
		try {
			byte result[] = getLoanDocumentService().getLoanDocuments(TEST_PROFILE_ID,
					TEST_CONTRACT_ID, submissionId, false);
			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Test case failed", true);
		}
	}
	
	public void executeStatement(String query, int arg, DataSource dataSource)
			throws SQLException {

		Connection conn = null;
		PreparedStatement stmt = null;
		conn = dataSource.getConnection();
		stmt = conn.prepareStatement(query);
		stmt.setInt(1, arg);
		stmt.execute();
		if (conn != null && !conn.isClosed()) {
			conn.close();
			conn = null;
		}
	}
	
	/**
	 * Checks whether allow online loans is on or off and set's the csf accordingly
	 * 
	 * @throws Exception
	 */
	private void verifyAndSetAllowOnlineLoans() throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		Boolean onlineLoansFlag = false;
		
		conn = csdbDatasource.getConnection();
		stmt = conn.prepareStatement(SELECT_CSF);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String serviceFeatureCode = rs.getString("SERVICE_FEATURE_CODE");
			String serviceFeatureValue = rs.getString("SERVICE_FEATURE_VALUE");
			if (serviceFeatureCode.equals("UOL")) {
				if (serviceFeatureValue.equals("Y")) {
					onlineLoansFlag = true;
					break;
				}
				stmt = conn.prepareStatement(UPDATE_CSF);
				stmt.execute();
				onlineLoansFlag = true;
				break;
			}
		}
		rs.close();
		if (onlineLoansFlag.equals(false)) {
			stmt = conn.prepareStatement(Insert_CSF);
			stmt.execute();
		}
		stmt.close();
	}

	/**
	 * Initiates the loan request
	 * 
	 * @throws Exception
	 */
	private void initiate() throws Exception {
		verifyAndSetAllowOnlineLoans();
		Calendar requestCalendar = Calendar.getInstance();
		Calendar expirationCalendar = Calendar.getInstance();
		expirationCalendar.add(Calendar.MONTH, 1);
		Calendar effectiveDate = Calendar.getInstance();
		effectiveDate.add(Calendar.MONTH, 1);
		Calendar maturityDate = Calendar.getInstance();
		maturityDate.add(Calendar.YEAR, 1);
		Calendar firstPayrollDate = Calendar.getInstance();
		firstPayrollDate.add(Calendar.MONTH, 2);
		
		LoanState state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		Loan loan = state.initiate(130747405, 11847,
				130747405);
		loan.setExpirationDate(expirationCalendar.getTime());
		loan.setCreatedByRoleCode(LoanConstants.USER_ROLE_PARTICIPANT_CODE);
		loan.setRequestDate(requestCalendar.getTime());
		
		loan.setLegallyMarriedInd(true);
		
		loan.getCurrentLoanParameter().setPaymentAmount(new BigDecimal(10));
		loan.getMessages().clear();
		loan.setLoanReason("To buy a car");
		loan.getOriginalParameter().setLoanAmount(new BigDecimal(2000));
		loan.getOriginalParameter().setPaymentFrequency(
				GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY);
		loan.getOriginalParameter().setInterestRate(new BigDecimal(12.5));
		
		state = LoanStateFactory.getState(LoanStateEnum.DRAFT);
		loan.setFirstPayrollDate(firstPayrollDate.getTime());
		loan.setEffectiveDate(effectiveDate.getTime());
		loan.setMaturityDate(maturityDate.getTime());
		loan = state.saveAndExit(loan);
		submissionId = loan.getSubmissionId();
		
		loanRequestFlag = true;
		Assert.assertNotNull(loan.getSubmissionId());
		//setting the font assets base path
		String assetsCommonPath = new File("../common").toURL().toString();
		FopUtils.getFopFactory().setFontBaseURL(assetsCommonPath);
	}
}
