package com.manulife.pension.service.loan.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.testutility.MockContainerEnvironmentTestCase;
import com.manulife.pension.service.withdrawal.valueobject.Address;

public class TestDistributionAtRiskDetailsDAO extends
		MockContainerEnvironmentTestCase {

	private DistributionAtRiskDetailsDAO dao = null;
	private static final String SUBMISSION_DATA_SOURCE_NAME = "jdbc/customerService";
	private DataSource datasource = null;
	private AtRiskDetailsVO mainObj = null;

	public TestDistributionAtRiskDetailsDAO(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		

		 suite.addTest(new TestDistributionAtRiskDetailsDAO("testInsert"));
		 suite.addTest(new TestDistributionAtRiskDetailsDAO("testInsertAddressNull"));
		 suite.addTest(new TestDistributionAtRiskDetailsDAO("testInsertSubmissionIdNull"));

		suite.addTest(new TestDistributionAtRiskDetailsDAO("testUpdate"));
	    suite.addTest(new TestDistributionAtRiskDetailsDAO("testUpdateInvalidSubmissionId"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testUpdateNullSubmissionId"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testUpdateAlldataNull"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testUpdateAlldataNullExceptSubmissionId"));

		suite.addTest(new TestDistributionAtRiskDetailsDAO("testRetrieve"));
	    suite.addTest(new TestDistributionAtRiskDetailsDAO("testRetrieveNullSubmissionId"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testRetrieveInvalidSubmissionId"));

		suite.addTest(new TestDistributionAtRiskDetailsDAO("testinsertOrUpdate"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testinsertOrUpdateNewRecordInsertion"));
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testinsertOrUpdateNullSubmissionId"));
		
		suite.addTest(new TestDistributionAtRiskDetailsDAO("testIsWebConfirmationLetterAvailable"));
		return suite;
	}

	public void setUp() throws Exception {

		super.setUp();

		if (dao == null) {
			dao = new DistributionAtRiskDetailsDAO();
		}
		if (datasource == null) {
			datasource = (DataSource) new InitialContext()
					.lookup(SUBMISSION_DATA_SOURCE_NAME);
		}
		// setupValueObjects();

	}

	private void setupValueObjects() {
		// Integer submissionId = 10000;

		mainObj = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1("conf First Line");
		confAddress.setAddressLine2("conf Second Line");
		confAddress.setCity("Confirm City");
		confAddress.setStateCode("CS");
		confAddress.setZipCode("0441-1234");
		confAddress.setCountryCode("TES");
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1("Appr First Line");
		apprAddress.setAddressLine2("Appr Second Line");
		apprAddress.setCity("Approval City");
		apprAddress.setStateCode("AS");
		apprAddress.setZipCode("0441-4444");
		apprAddress.setCountryCode("TES");

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj.setContractId("10025");
		mainObj.setProfileId(22235);
		mainObj.setWebRegistration(webReg);
		mainObj.setAddresschange(riskAddr);
		mainObj.setPasswordReset(riskPassword);
		mainObj.setForgetUserName(riskUsername);

	}

	public void testRetrieve() throws Exception {
		
		AtRiskDetailsInputVO vo = new AtRiskDetailsInputVO();
		vo.setSubmissionId(10023);
		
		AtRiskDetailsVO obj = DistributionAtRiskDetailsDAO.retrieve(vo);
		assertNotNull(obj);
	}

	public void testRetrieveNullSubmissionId() throws Exception {
		try {
			DistributionAtRiskDetailsDAO.retrieve(null);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}

	}

	public void testRetrieveInvalidSubmissionId() throws Exception {
		try {
			AtRiskDetailsInputVO vo = new AtRiskDetailsInputVO();
			vo.setSubmissionId(11111);
			
			DistributionAtRiskDetailsDAO.retrieve(vo);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}

	}

	public void testInsert() throws Exception {
		dao.insert(mainObj);
	}

	public void testUpdate() throws Exception {

		AtRiskDetailsVO mainObj2 = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1("conf First Line");
		confAddress.setAddressLine2("conf Second Line");
		confAddress.setCity("Confirm City");
		confAddress.setStateCode("CS");
		confAddress.setZipCode("0441-1234");
		confAddress.setCountryCode("TES");
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj2.setContractId("10025");
		mainObj2.setProfileId(22235);
		mainObj2.setWebRegistration(webReg);
		mainObj2.setAddresschange(riskAddr);
		mainObj2.setPasswordReset(riskPassword);
		mainObj2.setForgetUserName(riskUsername);
		mainObj2.setSubmissionId(10023);

		dao.update(mainObj2);
	}

	public void testUpdateInvalidSubmissionId() throws Exception {

		AtRiskDetailsVO mainObj3 = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1("conf First Line");
		confAddress.setAddressLine2("conf Second Line");
		confAddress.setCity("Confirm City");
		confAddress.setStateCode("CS");
		confAddress.setZipCode("0441-1234");
		confAddress.setCountryCode("TES");
		confAddress.setSubmissionId(10024);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj3.setContractId("10025");
		mainObj3.setProfileId(22235);
		mainObj3.setWebRegistration(webReg);
		mainObj3.setAddresschange(riskAddr);
		mainObj3.setPasswordReset(riskPassword);
		mainObj3.setForgetUserName(riskUsername);

		try {
			DistributionAtRiskDetailsDAO.update(mainObj3);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}
	}

	public void testUpdateNullSubmissionId() throws Exception {

		AtRiskDetailsVO mainObj4 = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1("conf First Line");
		confAddress.setAddressLine2("conf Second Line");
		confAddress.setCity("Confirm City");
		confAddress.setStateCode("CS");
		confAddress.setZipCode("0441-1234");
		confAddress.setCountryCode("TES");
		confAddress.setSubmissionId(null);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj4.setContractId("10025");
		mainObj4.setProfileId(22235);
		mainObj4.setWebRegistration(webReg);
		mainObj4.setAddresschange(riskAddr);
		mainObj4.setPasswordReset(riskPassword);
		mainObj4.setForgetUserName(riskUsername);

		try {
			DistributionAtRiskDetailsDAO.update(mainObj4);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}
	}

	
	
	public void testIsWebConfirmationLetterAvailable()throws Exception{
		AtRiskDetailsInputVO atRiskDetils  = new  AtRiskDetailsInputVO();
		
		atRiskDetils.setContractId(58535);
		atRiskDetils.setProfileId(216512194);
		
		java.sql.Timestamp time =  java.sql.Timestamp.valueOf("2014-10-16 05:48:19.668"); 
		
		DistributionAtRiskDetailsDAO.isWebConfirmationLetterAvailable(atRiskDetils, time);
		
	}
	
	
	public void testUpdateAlldataNull() throws Exception {

		AtRiskDetailsVO mainObj5 = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1(null);
		confAddress.setAddressLine2(null);
		confAddress.setCity(null);
		confAddress.setStateCode(null);
		confAddress.setZipCode(null);
		confAddress.setCountryCode(null);
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(null);
		riskUsername.setForgotPasswordEmailAddress(null);
		riskUsername.setForgotPasswordUpdatedProfileId(null);
		riskUsername.setForgotPasswordUpdatedUserIdType(null);
		riskUsername.setForgotPasswordUpdatedUserFirstName(null);
		riskUsername.setForgotPasswordUpdatedUserLastName(null);

		riskPassword.setEmailPasswordResetDate(null);
		riskPassword.setEmailPasswordResetEmailAddress(null);
		riskPassword.setEmailPasswordResetInitiatedProfileId(null);
		riskPassword.setEmailAddressLastUpdatedProfileId(null);
		riskPassword.setEmailAddressLastUpdatedUserIdType(null);

		riskAddr.setApprovalAddress(null);
		riskAddr.setApprovalUpdatedProfileId(null);
		riskAddr.setApprovalUpdatedUserIdType(null);

		webReg.setAddress(null);
		webReg.setWebRegistrationDate(null);
		webReg.setWebRegConfirmationMailedDate(null);
		webReg.setConfirmUpdatedProfileId(null);
		webReg.setConfirmUpdatedUserIdType(null);

		mainObj5.setContractId(null);
		mainObj5.setProfileId(null);
		mainObj5.setWebRegistration(null);
		mainObj5.setAddresschange(null);
		mainObj5.setPasswordReset(null);
		mainObj5.setForgetUserName(null);

		try {
			DistributionAtRiskDetailsDAO.update(mainObj5);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}
	}

	public void testUpdateAlldataNullExceptSubmissionId() throws Exception {

		AtRiskDetailsVO mainObj5 = new AtRiskDetailsVO();
		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();
		confAddress.setAddressLine1(null);
		confAddress.setAddressLine2(null);
		confAddress.setCity(null);
		confAddress.setStateCode(null);
		confAddress.setZipCode(null);
		confAddress.setCountryCode(null);
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();
		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(null);
		riskUsername.setForgotPasswordEmailAddress(null);
		riskUsername.setForgotPasswordUpdatedProfileId(null);
		riskUsername.setForgotPasswordUpdatedUserIdType(null);
		riskUsername.setForgotPasswordUpdatedUserFirstName(null);
		riskUsername.setForgotPasswordUpdatedUserLastName(null);

		riskPassword.setEmailPasswordResetDate(null);
		riskPassword.setEmailPasswordResetEmailAddress(null);
		riskPassword.setEmailPasswordResetInitiatedProfileId(null);
		riskPassword.setEmailAddressLastUpdatedProfileId(null);
		riskPassword.setEmailAddressLastUpdatedUserIdType(null);

		riskAddr.setApprovalAddress(null);
		riskAddr.setApprovalUpdatedProfileId(null);
		riskAddr.setApprovalUpdatedUserIdType(null);

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(null);
		webReg.setWebRegConfirmationMailedDate(null);
		webReg.setConfirmUpdatedProfileId(null);
		webReg.setConfirmUpdatedUserIdType(null);

		mainObj5.setContractId(null);
		mainObj5.setProfileId(null);
		mainObj5.setWebRegistration(webReg);
		mainObj5.setAddresschange(null);
		mainObj5.setPasswordReset(null);
		mainObj5.setForgetUserName(null);

		try {
			DistributionAtRiskDetailsDAO.update(mainObj5);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}
	}

	// public void testInsertUpdate() throws Exception {
	// dao.insertUpdate(null);
	// }

	public void testInsertAddressNull() throws Exception {

		AtRiskDetailsVO mainObj1 = new AtRiskDetailsVO();

		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();

		confAddress.setAddressLine1(null);
		confAddress.setAddressLine2(null);
		confAddress.setCity(null);
		confAddress.setStateCode(null);
		confAddress.setZipCode(null);
		confAddress.setCountryCode(null);
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();

		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj1.setContractId("10025");
		mainObj1.setProfileId(22235);
		mainObj1.setWebRegistration(webReg);
		mainObj1.setAddresschange(riskAddr);
		mainObj1.setPasswordReset(riskPassword);
		mainObj1.setForgetUserName(riskUsername);
		mainObj1.setSubmissionId(10023);

		DistributionAtRiskDetailsDAO.insert(mainObj1);
	}

	public void testInsertSubmissionIdNull() throws Exception {

		AtRiskDetailsVO mainObj1 = new AtRiskDetailsVO();

		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();

		confAddress.setAddressLine1("A");
		confAddress.setAddressLine2("C");
		confAddress.setCity("City");
		confAddress.setStateCode("dsfd");
		confAddress.setZipCode("dsdf");
		confAddress.setCountryCode("sdsdf");
		confAddress.setSubmissionId(null);

		Address apprAddress = new Address();

		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj1.setContractId("10025");
		mainObj1.setProfileId(22235);
		mainObj1.setWebRegistration(webReg);
		mainObj1.setAddresschange(riskAddr);
		mainObj1.setPasswordReset(riskPassword);
		mainObj1.setForgetUserName(riskUsername);

		try {
			DistributionAtRiskDetailsDAO.insert(mainObj1);
		} catch (DistributionServiceException e) {
			assertTrue(true);
		}
	}
	
	
	public void testinsertOrUpdate() throws Exception {

		AtRiskDetailsVO mainObj1 = new AtRiskDetailsVO();

		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();

		confAddress.setAddressLine1("AAA");
		confAddress.setAddressLine2("C");
		confAddress.setCity("City");
		confAddress.setStateCode("SC");
		confAddress.setZipCode("dsdf");
		confAddress.setCountryCode("sdsdf");
		confAddress.setSubmissionId(10023);

		Address apprAddress = new Address();

		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj1.setContractId("10025");
		mainObj1.setProfileId(22235);
		mainObj1.setWebRegistration(webReg);
		mainObj1.setAddresschange(riskAddr);
		mainObj1.setPasswordReset(riskPassword);
		mainObj1.setForgetUserName(riskUsername);
		mainObj1.setSubmissionId(10023);

		
			List<AtRiskDetailsVO> voObj = new ArrayList<AtRiskDetailsVO>(); 
			voObj.add(mainObj1);
			DistributionAtRiskDetailsDAO.insertOrUpdate(voObj);
			assertTrue(true);
		
	}
	
	public void testinsertOrUpdateNewRecordInsertion() throws Exception {

		AtRiskDetailsVO mainObj1 = new AtRiskDetailsVO();

		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();

		confAddress.setAddressLine1("AAA");
		confAddress.setAddressLine2("C");
		confAddress.setCity("City");
		confAddress.setStateCode("SC");
		confAddress.setZipCode("dsdf");
		confAddress.setCountryCode("sdsdf");
		confAddress.setSubmissionId(10024);

		Address apprAddress = new Address();

		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj1.setContractId("10025");
		mainObj1.setProfileId(22235);
		mainObj1.setWebRegistration(webReg);
		mainObj1.setAddresschange(riskAddr);
		mainObj1.setPasswordReset(riskPassword);
		mainObj1.setForgetUserName(riskUsername);
		mainObj1.setSubmissionId(10024);

		
			List<AtRiskDetailsVO> voObj = new ArrayList<AtRiskDetailsVO>(); 
			voObj.add(mainObj1);
			DistributionAtRiskDetailsDAO.insertOrUpdate(voObj);
			assertTrue(true);
		
	}

	public void testinsertOrUpdateNullSubmissionId() throws Exception {

		AtRiskDetailsVO mainObj1 = new AtRiskDetailsVO();

		AtRiskWebRegistrationVO webReg = new AtRiskWebRegistrationVO();
		AtRiskAddressChangeVO riskAddr = new AtRiskAddressChangeVO();
		AtRiskPasswordResetVO riskPassword = new AtRiskPasswordResetVO();
		AtRiskForgetUserName riskUsername = new AtRiskForgetUserName();

		Address confAddress = new Address();

		confAddress.setAddressLine1("AAA");
		confAddress.setAddressLine2("C");
		confAddress.setCity("City");
		confAddress.setStateCode("SC");
		confAddress.setZipCode("dsdf");
		confAddress.setCountryCode("sdsdf");
		confAddress.setSubmissionId(null);

		Address apprAddress = new Address();

		apprAddress.setAddressLine1(null);
		apprAddress.setAddressLine2(null);
		apprAddress.setCity(null);
		apprAddress.setStateCode(null);
		apprAddress.setZipCode(null);
		apprAddress.setCountryCode(null);

		riskUsername.setForgotPasswordRequestedDate(new Date(111223));
		riskUsername.setForgotPasswordEmailAddress("aaa@gmail.com");
		riskUsername.setForgotPasswordUpdatedProfileId(10020);
		riskUsername.setForgotPasswordUpdatedUserIdType("TPA");
		riskUsername
				.setForgotPasswordUpdatedUserFirstName("Username firstname");
		riskUsername.setForgotPasswordUpdatedUserLastName("Username lastname");

		riskPassword.setEmailPasswordResetDate(new Date(111223));
		riskPassword.setEmailPasswordResetEmailAddress("bbb@gmail.com");
		riskPassword.setEmailPasswordResetInitiatedProfileId(10021);
		riskPassword.setEmailAddressLastUpdatedProfileId(12212);
		riskPassword.setEmailAddressLastUpdatedUserIdType("UPA");

		riskAddr.setApprovalAddress(apprAddress);
		riskAddr.setApprovalUpdatedProfileId(10050);
		riskAddr.setApprovalUpdatedUserIdType("NSA");

		webReg.setAddress(confAddress);
		webReg.setWebRegistrationDate(new Date(111223));
		webReg.setWebRegConfirmationMailedDate(new Date(111223));
		webReg.setConfirmUpdatedProfileId(12345);
		webReg.setConfirmUpdatedUserIdType("BSA");

		mainObj1.setContractId("10025");
		mainObj1.setProfileId(22235);
		mainObj1.setWebRegistration(webReg);
		mainObj1.setAddresschange(riskAddr);
		mainObj1.setPasswordReset(riskPassword);
		mainObj1.setForgetUserName(riskUsername);

		try{
			List<AtRiskDetailsVO> voObj = new ArrayList<AtRiskDetailsVO>(); 
			voObj.add(mainObj1);
			DistributionAtRiskDetailsDAO.insertOrUpdate(voObj);
		}
		catch(DistributionServiceException e)
		{
			assertTrue(true);
		}
	}

}
