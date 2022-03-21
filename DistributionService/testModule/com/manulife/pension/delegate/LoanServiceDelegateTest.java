/**
 * 
 */
package com.manulife.pension.delegate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.reset;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.LoanService;
import com.manulife.pension.service.loan.LoanServiceHome;
import com.manulife.pension.service.loan.LoanServiceLocalHome;
import com.manulife.pension.service.loan.LoanServiceRemote;
import com.manulife.pension.service.loan.LoanServiceUtil;
import com.manulife.pension.service.loan.valueobject.EjbLoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanSupportDataRetriever;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.util.Pair;
import com.sun.naming.internal.ResourceManager;

/**
 * Parasoft Jtest UTA: Test class for LoanServiceDelegate
 *
 * @see com.manulife.pension.delegate.LoanServiceDelegate
 * @author test user
 */
@PrepareForTest({ InitialContext.class, LoanServiceUtil.class,NamingManager.class,ResourceManager.class })
@RunWith(PowerMockRunner.class)
public class LoanServiceDelegateTest {

	static LoanServiceHome loanServiceHome;
	static LoanServiceLocalHome loanServiceLocalHome;
	static LoanService loanService;
	static LoanServiceRemote loanServiceRemote;
	Context context=mock(Context.class);

	@BeforeClass
	public static void setUp() {
		loanService = mock(LoanService.class);
		loanServiceHome = mock(LoanServiceHome.class);
		loanServiceLocalHome = mock(LoanServiceLocalHome.class);
		loanServiceRemote = mock(LoanServiceRemote.class);
	}

	@Before
	public void tearDown() throws Exception {
		reset(loanServiceHome);
		reset(loanServiceLocalHome);
		reset(loanServiceRemote);
		reset(loanService);

		spy(LoanServiceUtil.class);
		doReturn(loanServiceLocalHome).when(LoanServiceUtil.class);
		LoanServiceUtil.getLocalHome();
		
		spy(NamingManager.class);
		doReturn(true).when(NamingManager.class);
		NamingManager.hasInitialContextFactoryBuilder();
			
		doReturn(context).when(NamingManager.class);
		NamingManager.getInitialContext(nullable(Hashtable.class));
		spy(ResourceManager.class);
		Hashtable<String,Object> hashobj=new Hashtable<String,Object>();
		hashobj.put("java.naming.factory.initial",new Object());
		doReturn(hashobj).when(ResourceManager.class);
		ResourceManager.getInitialEnvironment(nullable(Hashtable.class));
	}

	/**
	 * Parasoft Jtest UTA: Test for getPhysicalHome()
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getPhysicalHome()
	 * @author test user
	 */
	@Test
	public void testGetPhysicalHome() throws Throwable {
		// When
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		LoanServiceHome result = LoanServiceDelegate.getPhysicalHome();

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for initiateLoan(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#initiateLoan(Integer, Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testInitiateLoan() throws Throwable {
		// Given

		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loan = new Loan();
		LoanParameter acceptedParameter=new LoanParameter();
		acceptedParameter.setAmortizationMonths(10);
		acceptedParameter.setCreated(new Timestamp(2L));
		acceptedParameter.setCreatedById(23);
		acceptedParameter.setInterestRate(BigDecimal.TEN);
		acceptedParameter.setLastUpdated(new Timestamp(3L));
		acceptedParameter.setLastUpdatedById(20);
		acceptedParameter.setLoanAmount(BigDecimal.TEN);
		acceptedParameter.setMaximumAvailable(BigDecimal.TEN);
		acceptedParameter.setPaymentAmount(BigDecimal.TEN);
		acceptedParameter.setPaymentFrequency("PAYFRE");
		acceptedParameter.setReadyToSave(true);
		acceptedParameter.setStatusCode("STCODE34");
		loan.setAcceptedParameter(acceptedParameter);
		loan.setApplyIrs10KDollarRuleInd(true);
		Address address=new Address();
		address.setAddressLine1("");
		address.setAddressLine2("");
		address.setCity("");
		loan.setAtRiskAddress(address);
		AtRiskDetailsVO atRiskDetailsVO=new AtRiskDetailsVO();
		AtRiskAddressChangeVO addresschange=new AtRiskAddressChangeVO();
		atRiskDetailsVO.setAddresschange(addresschange);
		atRiskDetailsVO.setContractId("");
		AtRiskForgetUserName forgetUserName=new AtRiskForgetUserName();
		atRiskDetailsVO.setForgetUserName(forgetUserName);
		AtRiskPasswordResetVO passwordReset=new AtRiskPasswordResetVO();
		atRiskDetailsVO.setPasswordReset(passwordReset);
		atRiskDetailsVO.setProfileId(22);
		atRiskDetailsVO.setSubmissionId(55);
		AtRiskWebRegistrationVO webRegistration=new AtRiskWebRegistrationVO();
		atRiskDetailsVO.setWebRegistration(webRegistration);
		atRiskDetailsVO.setAddresschange(addresschange);
		loan.setAtRiskDetailsVO(atRiskDetailsVO);
		loan.setAtRiskInd("");
		loan.setBundledContract(true);
		loan.setContractId(10);
		loan.setContractLoanExpenseMarginPct(BigDecimal.TEN);
		loan.setContractLoanMonthlyFlatFee(BigDecimal.TEN);
		loan.setCreated(new Timestamp(2L));
		loan.setCreatedByRoleCode("");
		loan.setCreatedId(33);
		LoanNote currentAdministratorNote=new LoanNote();
		currentAdministratorNote.setCreated(new Timestamp(2L));
		currentAdministratorNote.setCreatedById(66);
		currentAdministratorNote.setLastUpdated(new Timestamp(3L));
		currentAdministratorNote.setLastUpdatedById(44);
		currentAdministratorNote.setNote("SFF");
		currentAdministratorNote.setNoteTypeCode("");
		loan.setCurrentAdministratorNote(currentAdministratorNote);
		LoanSupportDataRetriever dataRetriever=new EjbLoanSupportDataRetriever();
		loan.setDataRetriever(dataRetriever);
		List<LoanDeclaration> listLoanDeclaration=new ArrayList<LoanDeclaration>();
		LoanDeclaration loanDeclaration=new LoanDeclaration();
		loanDeclaration.setCreated(new Timestamp(2L));
		loanDeclaration.setCreatedById(66);
		loanDeclaration.setLastUpdated(new Timestamp(3L));
		loanDeclaration.setLastUpdatedById(44);
		loanDeclaration.setTypeCode("");
		listLoanDeclaration.add(loanDeclaration);
		loan.setDeclarations(listLoanDeclaration);
		loan.setDeclartionSectionDisplayed(true);
		loan.setDefaultProvision("");
		loan.setEffectiveDate(new Date());
		loan.setEffectiveDateOriginalDBValue(new Date());
		loan.setEmailChangePinAtRiskDays("");
		EmployeeVestingInformation employeeVestingInformation=mock(EmployeeVestingInformation.class) ;
		loan.setEmployeeVestingInformation(employeeVestingInformation);
		List<LoanMessage> listLoanMsg=new ArrayList<LoanMessage>();
		LoanMessage loanMessage=new LoanMessage(LoanErrorCode.ABA_ROUTING_NUMBER_NON_NUMERIC);
		listLoanMsg.add(loanMessage);
		loan.setErrors(listLoanMsg);
		loan.setExpirationDate(new Date());
		Fee fee=new LoanFee();
		fee.setCreated(new Timestamp(2L));
		fee.setCreatedById(66);
		fee.setLastUpdated(new Timestamp(3L));
		fee.setLastUpdatedById(44);
		fee.setTypeCode("TC44");
		fee.setSubmissionId(99);
		fee.setValue(BigDecimal.TEN);
		loan.setFee(fee);
		loan.setFeeChanged(true);
		loan.setFirstPayrollDate(new Date());
		loan.setIgnoreWarning(true);
		loan.setLastFeeChangedByTpaProfileId(44);
		loan.setLastFeeChangedWasPlanSponsorUserInd(true);
		loan.setLastUpdated(new Timestamp(2L));
		loan.setLastUpdatedId(22);
		loan.setLegallyMarriedInd(true);
		LoanParticipantData loanParticipantData=new LoanParticipantData();
		loanParticipantData.setAddressLine1("");
		loanParticipantData.setAddressLine2("");
		loanParticipantData.setCity("");
		loan.setLoanParticipantData(loanParticipantData);
		LoanPlanData loanPlanData=new LoanPlanData();
		loanPlanData.setContractName("");
		loan.setLoanPlanData(loanPlanData);
		loan.setLoanReason("");
		LoanSettings loanSettings=new LoanSettings();
		loan.setLoanSettings(loanSettings);
		loan.setLoanType("");
		loan.setLoginRoleCode("");
		loan.setLoginUserProfileId(88);
		List<ManagedContent> listManagedContent=new ArrayList<ManagedContent>();
		ManagedContent managedContent=new ManagedContent();
		listManagedContent.add(managedContent);
		loan.setManagedContents(listManagedContent);
		loan.setMaturityDate(new Date());
		loan.setMaturityDateOriginalDBValue(new Date());
		loan.setMaxBalanceLast12Months(BigDecimal.TEN);
		loan.setMaximumAmortizationYears(33);
		loan.setMaximumLoanAmount(BigDecimal.TEN);
		loan.setMaximumLoanPercentage(BigDecimal.TEN);
		loan.setMessages(listLoanMsg);
		loan.setMinimumLoanAmount(BigDecimal.TEN);
		List<LoanMoneyType> listLoanMoneyType=new ArrayList<LoanMoneyType>();
		LoanMoneyType loanMoneyType=new LoanMoneyType();
		loanMoneyType.setAccountBalance(BigDecimal.TEN);
		listLoanMoneyType.add(loanMoneyType);
		loan.setMoneyTypes(listLoanMoneyType);
		loan.setOriginalParameter(acceptedParameter);
		loan.setOutstandingLoansCount(22);
		loan.setParticipantId(23);
		loan.setParticipantProfileId(33);
		List<LoanNote> listLoanNote=new ArrayList<LoanNote>();
		LoanNote loanNote=new LoanNote();
		loanNote.setCreated(new Timestamp(2L));
		loanNote.setCreatedById(66);
		loanNote.setLastUpdated(new Timestamp(3L));
		loanNote.setLastUpdatedById(44);
		loanNote.setNoteTypeCode("");
		listLoanNote.add(loanNote);
		loan.setPreviousAdministratorNotes(listLoanNote);
		loan.setPreviousParticipantNotes(listLoanNote);
		loan.setPreviousStatus("");
		LoanRecipient loanRecipient=new LoanRecipient();
		loanRecipient.setCreated(new Timestamp(2L));
		loanRecipient.setCreatedById(66);
		loanRecipient.setLastUpdated(new Timestamp(3L));
		loanRecipient.setLastUpdatedById(44);
		loanRecipient.setFirstName("FNAME");
		loan.setRecipient(loanRecipient);
		loan.setRequestDate(new Date());
		loan.setReviewedParameter(acceptedParameter);
		loan.setSigningAuthorityForContractTpaFirm(true);
		loan.setSpousalConsentReqdInd("A");
		loan.setStatus("ACTIVE");
		loan.setSubmissionId(33);
		UserRole userRole=new AdministrativeContact();
		loan.setUserRole(userRole);
		when(loanService.initiate(nullable(Integer.class), nullable(Integer.class), nullable(Integer.class)))
				.thenReturn(loan);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer participantProfileId = 10;
		Integer contractId = 20;
		Integer userProfileId = 30;
		Loan result = underTest.initiateLoan(participantProfileId, contractId, userProfileId);

		// Then
		assertNotNull(result);
	}

	@Test(expected = NestableRuntimeException.class)
	public void testInitiateLoan_NamingException() throws Throwable {
		// Given
		PowerMockito.doThrow(new NamingException("MSG")).when(LoanServiceUtil.class);
		LoanServiceUtil.getLocalHome();

		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer participantProfileId = 10;
		Integer contractId = 20;
		Integer userProfileId = 30;
		Loan result = underTest.initiateLoan(participantProfileId, contractId, userProfileId);
	}

	@Test(expected = SystemException.class)
	public void testInitiateLoan_CreateException() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenThrow(new CreateException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer participantProfileId = 10;
		Integer contractId = 20;
		Integer userProfileId = 30;
		Loan result = underTest.initiateLoan(participantProfileId, contractId, userProfileId);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanRequestsByCreatedId(Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getLoanRequestsByCreatedId(Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testGetLoanRequestsByCreatedId() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		List<Loan> listObj = new ArrayList<Loan>();
		Loan loan = new Loan();
		listObj.add(loan);
		when(loanService.getLoanRequestsByCreatedId(nullable(Integer.class), nullable(Integer.class)))
				.thenReturn(listObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer userProfileId = 0;
		Integer contractId = 0;
		List<Loan> result = underTest.getLoanRequestsByCreatedId(userProfileId, contractId);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(loan));
	}

	/**
	 * Parasoft Jtest UTA: Test for getLastLoanRequest(Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getLastLoanRequest(Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testGetLastLoanRequest() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loan = new Loan();
		when(loanService.getLastLoanRequest(nullable(Integer.class), nullable(Integer.class))).thenReturn(loan);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer userProfileId = 20;
		Integer contractId = 30;
		Loan result = underTest.getLastLoanRequest(userProfileId, contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for save(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#save(Loan)
	 * @author test user
	 */
	@Test
	public void testSave() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.save(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.save(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for printLoanDocument(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#printLoanDocument(Loan)
	 * @author test user
	 */
	@Test
	public void testPrintLoanDocument() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.printLoanDocument(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.printLoanDocument(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for printLoanDocumentReview(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#printLoanDocumentReview(Loan)
	 * @author test user
	 */
	@Test
	public void testPrintLoanDocumentReview() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.printLoanDocumentReview(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.printLoanDocumentReview(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for read(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#read(Integer, Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testRead() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.read(nullable(Integer.class), nullable(Integer.class), nullable(Integer.class)))
				.thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer userProfileId = 30;
		Integer contractId = 40;
		Integer submissionId = 50;
		Loan result = underTest.read(userProfileId, contractId, submissionId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for readActivities(Integer, Integer, String, Integer, String, String)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#readActivities(Integer, Integer, String, Integer, String, String)
	 * @author test user
	 */
	@Test
	public void testReadActivities() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		LoanActivities loanActivities = new LoanActivities("SUB333", 10, 20, "JH", "HANCOCK");
		loanActivities.setOriginalCountryUSA(true);
		loanActivities.setSavedCountryUSA(true);
		loanActivities.setSystemOfRecordCountryUSA(true);
		when(loanService.readActivities(nullable(Integer.class), nullable(Integer.class), nullable(String.class),
				nullable(Integer.class), nullable(String.class), nullable(String.class))).thenReturn(loanActivities);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer userProfileId = 10;
		Integer contractId = 20;
		String submissionId = "SUB333";
		Integer participantUserProfileId = 20;
		String participantFirstName = "JH";
		String participantLastName = "HAN";
		LoanActivities result = underTest.readActivities(userProfileId, contractId, submissionId,
				participantUserProfileId, participantFirstName, participantLastName);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for approve(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#approve(Loan)
	 * @author test user
	 */
	@Test
	public void testApprove() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.approve(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.approve(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for validateApprove(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#validateApprove(Loan)
	 * @author test user
	 */
	@Test
	public void testValidateApprove() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.validateApprove(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.validateApprove(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for expire(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#expire(Loan)
	 * @author test user
	 */
	@Test
	public void testExpire() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.expire(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.expire(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for complete(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#complete(Loan)
	 * @author test user
	 */
	@Test
	public void testComplete() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.complete(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.complete(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for decline(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#decline(Loan)
	 * @author test user
	 */
	@Test
	public void testDecline() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.decline(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.decline(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for loanPackage(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#loanPackage(Loan)
	 * @author test user
	 */
	@Test
	public void testLoanPackage() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.loanPackage(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.loanPackage(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForAcceptance(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#sendForAcceptance(Loan)
	 * @author test user
	 */
	@Test
	public void testSendForAcceptance() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.sendForAcceptance(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.sendForAcceptance(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForApproval(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#sendForApproval(Loan)
	 * @author test user
	 */
	@Test
	public void testSendForApproval() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.sendForApproval(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.sendForApproval(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for sendForReview(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#sendForReview(Loan)
	 * @author test user
	 */
	@Test
	public void testSendForReview() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.sendForReview(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.sendForReview(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for delete(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#delete(Loan)
	 * @author test user
	 */
	@Test
	public void testDelete() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.delete(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.delete(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for reject(Loan)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#reject(Loan)
	 * @author test user
	 */
	@Test
	public void testReject() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Loan loanObj = new Loan();
		when(loanService.reject(nullable(Loan.class))).thenReturn(loanObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Loan loan = mock(Loan.class);
		Loan result = underTest.reject(loan);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanPlanData(Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getLoanPlanData(Integer)
	 * @author test user
	 */
	@Test
	public void testGetLoanPlanData() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		LoanPlanData loanPlanData = new LoanPlanData();
		when(loanService.getLoanPlanData(nullable(Integer.class))).thenReturn(loanPlanData);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 20;
		LoanPlanData result = underTest.getLoanPlanData(contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanSettings(Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getLoanSettings(Integer)
	 * @author test user
	 */
	@Test
	public void testGetLoanSettings() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		LoanSettings loanSettings = new LoanSettings();
		when(loanService.getLoanSettings(nullable(Integer.class))).thenReturn(loanSettings);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 30;
		LoanSettings result = underTest.getLoanSettings(contractId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getLoanParticipantData(Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getLoanParticipantData(Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testGetLoanParticipantData() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		LoanParticipantData loanParticipantData = new LoanParticipantData();
		when(loanService.getLoanParticipantData(nullable(Integer.class), nullable(Integer.class)))
				.thenReturn(loanParticipantData);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 20;
		Integer participantProfileId = 30;
		LoanParticipantData result = underTest.getLoanParticipantData(contractId, participantProfileId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getParticipantMoneyTypesForLoans(Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getParticipantMoneyTypesForLoans(Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testGetParticipantMoneyTypesForLoans() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		List<LoanMoneyType> listLoanMoneyType = new ArrayList<LoanMoneyType>();
		EmployeeVestingInformation employeeVestingInformation = mock(EmployeeVestingInformation.class);
		Pair<List<LoanMoneyType>, EmployeeVestingInformation> loanParticipantData = new Pair<List<LoanMoneyType>, EmployeeVestingInformation>(
				listLoanMoneyType, employeeVestingInformation);
		when(loanService.getParticipantMoneyTypesForLoans(nullable(Integer.class), nullable(Integer.class)))
				.thenReturn(loanParticipantData);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 20;
		Integer participantProfileId = 30;
		Pair<List<LoanMoneyType>, EmployeeVestingInformation> result = underTest
				.getParticipantMoneyTypesForLoans(contractId, participantProfileId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for markExpiredLoans(java.util.Date, java.lang.Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#markExpiredLoans(java.util.Date, java.lang.Integer)
	 * @author test user
	 */
	@Test
	public void testMarkExpiredLoans() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenReturn(loanServiceRemote);
		when(loanServiceRemote.markExpiredLoans(nullable(Date.class), nullable(Integer.class))).thenReturn(22);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 20;
		underTest.markExpiredLoans(checkDate, profileId);
	}

	@Test(expected = SystemException.class)
	public void testMarkExpiredLoans_NamingException() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenThrow(new NamingException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 20;
		underTest.markExpiredLoans(checkDate, profileId);
	}

	@Test(expected = SystemException.class)
	public void testMarkExpiredLoans_CreateException() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenThrow(new CreateException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 20;
		underTest.markExpiredLoans(checkDate, profileId);
	}

	@Test(expected = SystemException.class)
	public void testMarkExpiredLoans_RemoteException() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenThrow(new RemoteException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 20;
		underTest.markExpiredLoans(checkDate, profileId);
	}

	@Test(expected = EJBException.class)
	public void testMarkExpiredLoans_RemoteException_1() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenReturn(loanServiceRemote);
		when(loanServiceRemote.markExpiredLoans(nullable(Date.class), nullable(Integer.class)))
				.thenThrow(new RemoteException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Date checkDate = mock(Date.class);
		Integer profileId = 20;
		underTest.markExpiredLoans(checkDate, profileId);
	}

	/**
	 * Parasoft Jtest UTA: Test for getAboutToExpireLoanRequests(Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getAboutToExpireLoanRequests(Integer)
	 * @author test user
	 */
	@Test
	public void testGetAboutToExpireLoanRequests() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenReturn(loanServiceRemote);
		List<Object> list = new ArrayList<Object>();
		list.add("SS");
		when(loanServiceRemote.getAboutToExpireLoanRequests(nullable(Integer.class))).thenReturn(list);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer numberOfBusinessDaysBeforeExpiryDate = 0;
		List result = underTest.getAboutToExpireLoanRequests(numberOfBusinessDaysBeforeExpiryDate);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test(expected = EJBException.class)
	public void testGetAboutToExpireLoanRequests_Exceptiopn() throws Throwable {
		// Given
		when(context.lookup(anyString())).thenReturn(loanServiceHome);
		when(loanServiceHome.create()).thenReturn(loanServiceRemote);
		List<Object> list = new ArrayList<Object>();
		list.add("SS");
		when(loanServiceRemote.getAboutToExpireLoanRequests(nullable(Integer.class))).thenThrow(new RemoteException());
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer numberOfBusinessDaysBeforeExpiryDate = 0;
		List result = underTest.getAboutToExpireLoanRequests(numberOfBusinessDaysBeforeExpiryDate);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Parasoft Jtest UTA: Test for getOutstandingOldILoanRequestCount(int)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getOutstandingOldILoanRequestCount(int)
	 * @author test user
	 */
	@Test
	public void testGetOutstandingOldILoanRequestCount() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		when(loanService.getOutstandingOldILoanRequestCount(anyInt())).thenReturn(new Integer(23));
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		int contractId = 1230;
		Integer result = underTest.getOutstandingOldILoanRequestCount(contractId);

		// Then
		assertEquals(23, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Test for getPartialLoanSettingsData(Integer[])
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getPartialLoanSettingsData(Integer[])
	 * @author test user
	 */
	@Test
	public void testGetPartialLoanSettingsData() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		Map<Integer, ArrayList<LoanSettings>> mapObj=new HashMap<Integer, ArrayList<LoanSettings>>();
		ArrayList<LoanSettings> listLoanSettings=new ArrayList<LoanSettings>();
		LoanSettings loanSettings=new LoanSettings();
		loanSettings.setAllowLoanPackageGeneration(true);
		loanSettings.setAllowOnlineLoans(true);
		loanSettings.setAllowParticipantInitiateLoan(true);
		loanSettings.setInitiatorCanApproveLoan(true);
		loanSettings.setLrk01(true);
		listLoanSettings.add(loanSettings);
		mapObj.put(new Integer(12), listLoanSettings);
		when(loanService.getPartialLoanSettingsData(nullable(Integer[].class))).thenReturn(mapObj);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer[] contractIdList = new java.lang.Integer[1];
		Map<Integer, ArrayList<LoanSettings>> result = underTest.getPartialLoanSettingsData(contractIdList);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.containsKey(new Integer(12)));
		assertTrue(result.containsValue(listLoanSettings));
	}

	/**
	 * Parasoft Jtest UTA: Test for hasLoanRecordKeepingProductFeature(int)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#hasLoanRecordKeepingProductFeature(int)
	 * @author test user
	 */
	@Test
	public void testHasLoanRecordKeepingProductFeature() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		when(loanService.hasLoanRecordKeepingProductFeature(anyInt())).thenReturn(true);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		int contractId = 220;
		boolean result = underTest.hasLoanRecordKeepingProductFeature(contractId);

		// Then
		assertTrue(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getOutstandingLoan(Integer, Long, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getOutstandingLoan(Integer, Long, Integer)
	 * @author test user
	 */
	@Test
	public void testGetOutstandingLoan() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		OutstandingLoan outstandingLoan=new OutstandingLoan(1L, 2L, 22, 33, BigDecimal.TEN, new Date(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, new Date(), BigDecimal.TEN, new Date(), BigDecimal.TEN, 3L, new Date());
		when(loanService.getOutstandingLoan(nullable(Integer.class),nullable(Long.class),nullable(Integer.class))).thenReturn(outstandingLoan);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 20;
		Long profileId = 3L;
		Integer loanId = 40;
		OutstandingLoan result = underTest.getOutstandingLoan(contractId, profileId, loanId);

		// Then
		assertNotNull(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getUOLCount(Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getUOLCount(Integer)
	 * @author test user
	 */
	@Test
	public void testGetUOLCount() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		when(loanService.getUOLCount(nullable(Integer.class))).thenReturn(new Integer(23));
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer contractId = 0;
		Integer result = underTest.getUOLCount(contractId);

		// Then
		assertEquals(23, result.intValue());
	}

	/**
	 * Parasoft Jtest UTA: Test for checkLoanStatusExists(Integer, String)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#checkLoanStatusExists(Integer, String)
	 * @author test user
	 */
	@Test
	public void testCheckLoanStatusExists() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		when(loanService.checkLoanStatusExists(nullable(Integer.class),nullable(String.class))).thenReturn(true);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer submissionId = 20;
		String statusCode = "STCODE45";
		boolean result = underTest.checkLoanStatusExists(submissionId, statusCode);

		// Then
		assertTrue(result);
	}

	/**
	 * Parasoft Jtest UTA: Test for getManagedContent(Integer, Integer, Integer)
	 *
	 * @see com.manulife.pension.delegate.LoanServiceDelegate#getManagedContent(Integer, Integer, Integer)
	 * @author test user
	 */
	@Test
	public void testGetManagedContent() throws Throwable {
		// Given
		when(loanServiceLocalHome.create()).thenReturn(loanService);
		ManagedContent managedContent=new ManagedContent();
		managedContent.setCmaSiteCode("CMA");
		managedContent.setContentId(23);
		managedContent.setContentKey(33);
		managedContent.setContentTypeCode("CTC");
		managedContent.setCreated(new Timestamp(2L));
		managedContent.setCreatedById(55);
		List<ManagedContent> listManagedContent=new ArrayList<ManagedContent>();
		listManagedContent.add(managedContent);
		when(loanService.getManagedContent(nullable(Integer.class),nullable(Integer.class),nullable(Integer.class))).thenReturn(listManagedContent);
		LoanServiceDelegate underTest = LoanServiceDelegate.getInstance();

		// When
		Integer submissionId = 20;
		Integer contractId = 30;
		Integer userProfileId = 40;
		List<ManagedContent> result = underTest.getManagedContent(submissionId, contractId, userProfileId);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.contains(managedContent));
	}
}