package com.manulife.pension.service.withdrawal.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.ClassPatternPointcut;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.content.service.BrowseService;
import com.manulife.pension.content.service.BrowseServiceBean;
import com.manulife.pension.content.service.BrowseServiceHome;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.AccountService;
import com.manulife.pension.service.account.AccountServiceBean;
import com.manulife.pension.service.account.AccountServiceHome;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.email.EmailProcessingService;
import com.manulife.pension.service.email.EmailProcessingServiceBean;
import com.manulife.pension.service.email.EmailProcessingServiceHome;
import com.manulife.pension.service.employee.EmployeeService;
import com.manulife.pension.service.employee.EmployeeServiceBean;
import com.manulife.pension.service.employee.EmployeeServiceHome;
import com.manulife.pension.service.environment.EnvironmentService;
import com.manulife.pension.service.environment.EnvironmentServiceBean;
import com.manulife.pension.service.environment.EnvironmentServiceHome;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.security.SecurityService;
import com.manulife.pension.service.security.SecurityServiceBean;
import com.manulife.pension.service.security.SecurityServiceHome;
import com.manulife.pension.service.testutility.MockContainerEnvironment;
import com.manulife.pension.service.withdrawal.WithdrawalService;
import com.manulife.pension.service.withdrawal.WithdrawalServiceBean;
import com.manulife.pension.service.withdrawal.WithdrawalServiceLocalHome;
import com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.util.log.MrlLogger;
import com.manulife.pension.util.log.MrlLoggerBean;
import com.manulife.pension.util.log.MrlLoggerHome;
import com.manulife.util.converter.DateConverter;

/**
 * Test class for ReadyForEntryEmailHandler.
 * 
 * @author Kristin Kerr
 */
public class TestReadyForEntryEmailHandler 
//extends WithdrawalContainerEnvironmentTestCase {
{

    /**
     * TEST_EMAIL_ADDRESS.
     */
    private static final String TEST_EMAIL_ADDRESS = "paul_glenn@jhancock.com";

    private Map lookupData;

    private String[] datePattern = { "MM/dd/yyyy" };

    private static final int TEST_CONTRACT_NUMBER = 70300;

    private static final int LOGIN_ID_PS = 1576495;

    private static final int TEST_SUBMISSION_NUMBER = 9783456;

    private static final String TEST_CONTRACT_NAME = "Test Contract";

    
//    protected void setUp() throws Exception {
//        MockContainerEnvironment.initialize();
//    }
//
//    protected void tearDown() throws Exception {
//        MockContainerEnvironment.destroy();
//    }

    /**
     * Registers the service classes provided with the mock environment.
     * 
     * @param serviceHomeClass The service home class.
     * @param serviceClass The service interface.
     * @param serviceBeanClass The service bean class.
     * @throws NamingException If an exception occurs.
     * @throws Exception If an exception occurs.
     */
    protected void registerMockService(final Class serviceHomeClass, final Class serviceClass,
            final Class serviceBeanClass) throws NamingException, Exception {
        MockContainerEnvironment.getMockContainer().deploy(
                new SessionBeanDescriptor(serviceHomeClass.getName(), serviceHomeClass,
                        serviceClass, serviceBeanClass));
        MockContainerEnvironment.getAspectSystem().add(
                new ClassPatternPointcut(serviceBeanClass.getName()),
                new TransactionManager(TransactionPolicy.REQUIRED));
    }

    
    /**
     * @see com.manulife.pension.service.withdrawal.testutility.WithdrawalContainerEnvironmentTestCase#setUp()
     */
//    @Override
    @Before
    public void setUp() throws Exception {
//        super.setUp();
        MockContainerEnvironment.initialize();

        registerMockService(MrlLoggerHome.class, MrlLogger.class, MrlLoggerBean.class);

        registerMockService(WithdrawalServiceLocalHome.class, WithdrawalService.class,
                WithdrawalServiceBean.class);

        
        // blah

        registerMockService(EmailProcessingServiceHome.class, EmailProcessingService.class,
                EmailProcessingServiceBean.class);

        registerMockService(BrowseServiceHome.class, BrowseService.class, BrowseServiceBean.class);

        registerMockService(AccountServiceHome.class, AccountService.class,
                AccountServiceBean.class);

        registerMockService(EmployeeServiceHome.class, EmployeeService.class,
                EmployeeServiceBean.class);

        registerMockService(SecurityServiceHome.class, SecurityService.class,
                SecurityServiceBean.class);

        registerMockService(EnvironmentServiceHome.class, EnvironmentService.class,
                EnvironmentServiceBean.class);

// end blah        
        final InitialContext initialContext = new InitialContext();

        initialContext.bind("psw.withdrawal.email.readyForEntry.sender.emailAddress",
                TEST_EMAIL_ADDRESS);
        initialContext.bind("psw.withdrawal.email.readyForEntry.recipient.emailAddress",
                TEST_EMAIL_ADDRESS);

        // setProfile(TEST_CONTRACT_NUMBER);
        lookupData = getLookupData();
    }

    /**
     * @see com.manulife.pension.service.withdrawal.testutility.WithdrawalContainerEnvironmentTestCase#tearDown()
     */
//    @Override
    @After
    public void tearDown() throws Exception {
//        super.tearDown();
        MockContainerEnvironment.destroy();
  }

    @Test
    @Ignore("don't ignore this, it's temp test....")
    public void testLookupDescription() throws Exception {

        WithdrawalRequest wr = new WithdrawalRequest();
        wr.setRecipients(new ArrayList<Recipient>());
        wr.getRecipients().add(new WithdrawalRequestRecipient());

        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));

        assertEquals("Courier name should be FedEx", "FedEx", emailHandler.lookupDescription(
                CodeLookupCache.COURIER_COMPANY, "F"));

        assertEquals("Courier name should be FedEx", "UPS", emailHandler.lookupDescription(
                CodeLookupCache.COURIER_COMPANY, "U"));
    }

    @Test
    public void testGetPayeeKey() throws Exception {

        WithdrawalRequest wr = new WithdrawalRequest();
        WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        Collection<Payee> payees = new ArrayList<Payee>();
        Collection<Recipient> recipients = new ArrayList<Recipient>();
        payees.add(payee);
        recipient.setPayees(payees);
        recipients.add(recipient);
        wr.setRecipients(recipients);
        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        assertEquals("Participant key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_PARTICIPANT, emailHandler.getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        assertEquals("Trustee key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_TRUSTEE, emailHandler.getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        assertEquals("Rollover to plan key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_ROLLOVER_PLAN, emailHandler.getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        assertEquals("Rollover to IRA key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_ROLLOVER_IRA, emailHandler.getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        payee.setPayeeNo(new Integer(1));
        assertEquals("Remainder to plan, payee 1 key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_1, emailHandler
                        .getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        payee.setPayeeNo(new Integer(2));
        assertEquals("Remainder to plan, payee 2 key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_2, emailHandler
                        .getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        payee.setPayeeNo(new Integer(1));
        assertEquals("Remainder to IRA, payee 1 key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_1, emailHandler
                        .getPayeeKey(payee));

        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        payee.setPayeeNo(new Integer(2));
        assertEquals("Remainder to IRA, payee 2 key should be returned.",
                ReadyForEntryEmailHandler.PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_2, emailHandler
                        .getPayeeKey(payee));
    }

    @Test
    public void testGetPaymentKey() throws Exception {

        WithdrawalRequest wr = new WithdrawalRequest();
        WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        Collection<Payee> payees = new ArrayList<Payee>();
        Collection<Recipient> recipients = new ArrayList<Recipient>();
        payees.add(payee);
        recipient.setPayees(payees);
        recipients.add(recipient);
        wr.setRecipients(recipients);
        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        assertEquals("Check payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_CHECK, emailHandler.getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        assertEquals("Participant payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_PARTICIPANT, emailHandler
                        .getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        assertEquals("Trustee payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_TRUSTEE, emailHandler.getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        assertEquals("Rollover payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_ROLLOVER_FI, emailHandler
                        .getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        assertEquals("Rollover payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_ROLLOVER_FI, emailHandler
                        .getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        assertEquals("Participant payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_PARTICIPANT, emailHandler
                        .getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        assertEquals("Trustee payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_TRUSTEE, emailHandler.getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE);
        assertEquals("Rollover payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_ROLLOVER_FI, emailHandler
                        .getPaymentKey(payee));

        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE);
        assertEquals("Rollover payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_ROLLOVER_FI, emailHandler
                        .getPaymentKey(payee));

        wr.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        assertEquals("WMSI/Penchecks payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_WMSI_PENCHECKS, emailHandler
                        .getPaymentKey(payee));

        wr.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        assertEquals("WMSI/Penchecks payment key should be returned.",
                ReadyForEntryEmailHandler.PAYMENT_KEY_WMSI_PENCHECKS, emailHandler
                        .getPaymentKey(payee));

    }

    @Test
    public void testGetZipCode() throws Exception {

        WithdrawalRequest wr = new WithdrawalRequest();
        wr.setRecipients(new ArrayList<Recipient>());
        wr.getRecipients().add(new WithdrawalRequestRecipient());
        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));

        Address address = new Address();
        address.setZipCode("12345");
        address.setCountryCode("USA");
        assertEquals("US 5-digit zip code should not contain a dash.", "12345", emailHandler
                .getZipCode(address));

        address.setZipCode("123456789");
        assertEquals("US 9-digit zip code should contain two parts seperated by a dash.",
                "12345-6789", emailHandler.getZipCode(address));

        address.setCountryCode("FLK");
        assertEquals("Non-US zip code should not contain a dash.", "123456789", emailHandler
                .getZipCode(address));
    }

    @Test
    public void testGetCountry() throws Exception {

        WithdrawalRequest wr = new WithdrawalRequest();
        wr.setRecipients(new ArrayList<Recipient>());
        wr.getRecipients().add(new WithdrawalRequestRecipient());
        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));

        Address address = new Address();
        address.setCountryCode("MEX");
        assertEquals("Country names of 25 characters or less should display in full.", "MEXICO",
                emailHandler.getCountry(address));

        address.setCountryCode("FLK");
        assertEquals(
                "Country names of more than 25 characters should display only the first 25 characters.",
                "FALKLAND ISLANDS (MALVINA", emailHandler.getCountry(address));
    }
//    Leads to assertion failure
    @Test
    public void testGetFormattedNote() throws Exception {

//        WithdrawalRequest wr = new WithdrawalRequest();
//        wr.setRecipients(new ArrayList<WithdrawalRequestRecipient>());
//        wr.getRecipients().add(new WithdrawalRequestRecipient());
//        WithdrawalRequestNote note = new WithdrawalRequestNote();
//        note.setCreatedById(new Integer(LOGIN_ID_PS));
//        note.setNote("Test Note");
//        Calendar cal = Calendar.getInstance();
//        cal.set(2000, 11, 25, 3, 30, 5);
//        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
//        note.setCreated(timestamp);
//        wr.setReadOnlyAdminToAdminNotes(new ArrayList<WithdrawalRequestNote>());
//        wr.getReadOnlyAdminToAdminNotes().add(note);
//        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
//                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));
//        assertEquals(
//                "The note should be html formatted and contain the user's name, the formatted date and time, a dash, and the text of the note.",
//                "Wendy&nbsp;Zammit&nbsp;12/25/2000 03:30 AM ET&nbsp;-&nbsp;Test Note", emailHandler
//                        .getFormattedNote(note).toString());
    }

    /*
     * This is not a unit test per se - it creates a Ready For Entry email and sends it to the
     * iwithdrawals mailbox. See method getWithdrawalRequst() for content details.
     * 
     */
    @Test
    @Ignore("Ignored, as we don't want to send email, if you activate it, please set the email constant.")
    public void testSendEmail() throws Exception {
        WithdrawalRequest wr = getWithdrawalRequest();

        DateConverter dateConverter = new DateConverter(null);

        wr.setBirthDate((Date) dateConverter.convert(java.util.Date.class, "12/03/1955"));
        wr.setTerminationDate((Date) dateConverter.convert(java.util.Date.class, "12/26/2006"));
        wr.setFinalContributionDate((Date) dateConverter
                .convert(java.util.Date.class, "12/06/2006"));

        wr.getRecipients().iterator().next().setUsCitizenInd(Boolean.TRUE);

        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));
        emailHandler.sendEmail();
    }

    /*
     * This is not a unit test per se - it creates a Ready For Entry email and sends it to the
     * iwithdrawals mailbox. See method getWithdrawalRequst() for content details.
     * 
     */
    @Test
    @Ignore("Ignored, as we don't want to send email, if you activate it, please set the email constant.")
    public void testSendEmailNoNotes() throws Exception {
        WithdrawalRequest wr = getWithdrawalRequest();

        DateConverter dateConverter = new DateConverter(null);

        wr.setBirthDate((Date) dateConverter.convert(java.util.Date.class, "12/03/1955"));
        wr.setTerminationDate((Date) dateConverter.convert(java.util.Date.class, "12/26/2006"));
        wr.setFinalContributionDate((Date) dateConverter
                .convert(java.util.Date.class, "12/06/2006"));

        wr.getRecipients().iterator().next().setUsCitizenInd(Boolean.TRUE);
        wr.setReadOnlyAdminToAdminNotes(null);
        wr.setReadOnlyAdminToParticipantNotes(null);
        ReadyForEntryEmailHandler emailHandler = new ReadyForEntryEmailHandler(wr, lookupData,
                WithdrawalInfoDao.getUserNames(ReadyForEntryEmailHandler.getUserProfileIds(wr)));
        emailHandler.sendEmail();
    }

    private Map getLookupData() throws SystemException {
        return new WithdrawalLookupDataManager(null, StringUtils.EMPTY, ReadyForEntryEmailHandler
                .getLookupKeys()).getLookupData();
    }

    private WithdrawalRequest getWithdrawalRequest() throws ParseException {

        WithdrawalRequest wr = new WithdrawalRequest();
        wr.setContractId(new Integer(TEST_CONTRACT_NUMBER));
        wr.setContractName(TEST_CONTRACT_NAME);
        wr.setSubmissionId(new Integer(TEST_SUBMISSION_NUMBER));
        // Principal principal = TestPrincipalFactory.getInstance().getPrincipal(116496357, "TPA",
        // "uname", "fname", "lname");
        // wr.setPrincipal(principal);
        wr.setLastName("De La Cruz");
        wr.setFirstName("Maria A");
        wr.setLoans(dummyLoanData());
        wr.setParticipantStateOfResidence("CO");
        wr.setParticipantSSN("123456789");
        wr.setBirthDate(DateUtils.parseDate("01/15/1970", datePattern));
        wr.setReasonCode(WithdrawalReason.TERMINATION);
        wr.setReasonDescription(lookupDescription(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, wr
                .getReasonCode()));
        wr.setFinalContributionDate(new Date());
        wr.setTerminationDate(new Date());
        wr.setEmployeeProfileId(new Integer(116496357));
        wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE);
        // wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE);
        // wr.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        wr.setApprovedTimestamp(new Timestamp(new Date().getTime()));
        wr.setExpectedProcessingDate(new Date());
        wr.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE);
        // wr.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE);
        // wr.setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE);
        wr.setParticipantLeavingPlanInd(Boolean.TRUE);
        // wr.setParticipantLeavingPlanInd(Boolean.FALSE);
        wr.setWithdrawalAmount(new BigDecimal(1234567.89));
        // wr.setWithdrawalAmount(new BigDecimal(0.0));
        wr.setUnvestedAmountOptionCode(WithdrawalRequest.UNVESTED_TRANSFER_TO_CASH_ACCOUNT_CODE);
        // wr.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        // wr.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        wr.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        wr.setLoanOption("RO");
        wr.setIrsDistributionCodeLoanClosure("G");

        // Add fees
        Collection<Fee> fees = new ArrayList<Fee>();
        WithdrawalRequestFee fee = new WithdrawalRequestFee();
        // fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setTypeCode(WithdrawalRequestFee.PERCENT_TYPE_CODE);
        fee.setValue(new BigDecimal("12.3456"));
        // fee.setValue(new BigDecimal("0.0"));
        fees.add(fee);
        wr.setFees(fees);

        // Add contract information
        ContractInfo ci = new ContractInfo();
        ci.setClientAccountRepId("SMITHJO");
        ci.setTeamCode("GA");
        wr.setContractInfo(ci);

        // Add recipients and payees
        WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setFederalTaxPercent(new BigDecimal(10));
        recipient.setStateTaxPercent(new BigDecimal(5.25));
        // recipient.setStateTaxTypeCode("F");
        recipient.setStateTaxTypeCode("W");
        // recipient.setStateOfResidenceCode("IL");
        recipient.setStateOfResidenceCode("ZZ");
        recipient.setFirstName("Maria");
        recipient.setLastName("De La Cruz");
        WithdrawalRequestPayee payee1 = new WithdrawalRequestPayee();
        payee1.setPayeeNo(new Integer(1));
        PayeePaymentInstruction instruction1 = new PayeePaymentInstruction();
        instruction1.setPayeeNo(new Integer(1));
        // instruction1.setAttentionName("ABC Rollover Inc");
        instruction1.setCreditPartyName("ABC Rollover Inc");
        payee1.setPaymentInstruction(instruction1);
        payee1.setSendCheckByCourier(Boolean.TRUE);
        payee1.setMailCheckToAddress(Boolean.FALSE);
        payee1.setCourierCompanyCode("F"); // FedEx
        // payee1.setCourierCompanyCode("U"); // UPS
        payee1.setCourierNo("1234567890123456");
        payee1.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        Address address1 = new Address();
        address1.setAddressLine1("Address 1");
        address1.setAddressLine2("Address 2");
        address1.setCity("New Jersey");
        address1.setStateCode("NY");
        address1.setZipCode("123456789");
        address1.setCountryCode("USA");
        // address1.setCountryCode("MEX");
        // address1.setCountryCode("FLK");
        payee1.setAddress(address1);
        recipient.setAddress(address1);
        payee1.setIrsDistCode("7A");
        payee1.setRolloverAccountNo("987654G");
        payee1.setRolloverPlanName("Trustee of Parti-Newco Inc. Plan");
        payee1.setOrganizationName("New Rollover Plan Inc");
        payee1.setLastName("De la Cruz");
        payee1.setFirstName("Maria");
        WithdrawalRequestPayee payee2 = new WithdrawalRequestPayee();
        payee2.setPayeeNo(new Integer(2));
        payee2.setMailCheckToAddress(Boolean.TRUE);
        payee2.setSendCheckByCourier(Boolean.FALSE);
        payee2.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        // payee2.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        // payee2.setPaymentMethodCode("");
        payee2.setIrsDistCode("2A");
        Address address2 = new Address();
        address2.setAddressLine1("Address 1");
        address2.setAddressLine2("Address 2");
        address2.setCity("New Jersey");
        address2.setStateCode("NY");
        address2.setZipCode("123456789");
        address2.setCountryCode("USA");
        payee2.setAddress(address2);
        payee2.setOrganizationName("Big Bank Inc.");
        payee2.setFirstName("Maria");
        payee2.setLastName("De La Cruz");
        PayeePaymentInstruction instruction2 = new PayeePaymentInstruction();
        instruction2.setBankName("Bank Co. Inc");
        // instruction2.setBankAccountTypeCode(WithdrawalRequestPayee.CHECKING_ACCOUNT_TYPE_CODE);
        // instruction2.setBankAccountTypeCode(WithdrawalRequestPayee.SAVINGS_ACCOUNT_TYPE_CODE);
        instruction2.setBankAccountTypeCode("");
        instruction2.setBankAccountNumber("99999999A");
        instruction2.setBankTransitNumber(3880);
        instruction2.setCreditPartyName("Credit Party");
        payee2.setPaymentInstruction(instruction2);
        Collection<Payee> payees = new ArrayList<Payee>();
        payees.add(payee1);
        payees.add(payee2);
        recipient.setPayees(payees);
        Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        wr.setRecipients(recipients);

        // Add money types.
        Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        wr.setMoneyTypes(moneyTypes);
        WithdrawalRequestMoneyType moneyType = new WithdrawalRequestMoneyType();
        moneyType.setMoneyTypeName("Money Type 1");
        moneyType.setMoneyTypeAliasId("ERPSH");
        moneyType.setMoneyTypeCategoryCode("ER");
        moneyType.setTotalBalance(new BigDecimal("123456789.10"));
        moneyType.setVestingPercentage(new BigDecimal("55.555555"));
        moneyType.setAvailableWithdrawalAmount(new BigDecimal("5000.00"));
        moneyType.setWithdrawalAmount(new BigDecimal("2500.00"));
        moneyType.setWithdrawalPercentage(new BigDecimal("50"));
        moneyTypes.add(moneyType);

        WithdrawalRequestMoneyType moneyType2 = (WithdrawalRequestMoneyType) moneyType.clone();
        moneyType2.setMoneyTypeName("Money Type 2");
        moneyType2.setMoneyTypeAliasId("EEDEF");
        moneyType2.setMoneyTypeCategoryCode("ER");
        moneyType2.setWithdrawalAmount(new BigDecimal(0.1));
        moneyTypes.add(moneyType2);

        WithdrawalRequestMoneyType moneyType3 = (WithdrawalRequestMoneyType) moneyType.clone();
        moneyType3.setMoneyTypeName("Money Type 3");
        moneyType3.setMoneyTypeAliasId("EEDEF");
        moneyType3.setMoneyTypeCategoryCode("EE");
        moneyType3.setWithdrawalAmount(new BigDecimal(123456.78));
        moneyTypes.add(moneyType3);

        WithdrawalRequestMoneyType moneyType4 = (WithdrawalRequestMoneyType) moneyType.clone();
        moneyType4.setMoneyTypeName("Money Type 4");
        moneyType4.setMoneyTypeAliasId("ERMCH");
        moneyType4.setMoneyTypeCategoryCode("ER");
        moneyType4.setWithdrawalAmount(new BigDecimal(3000));
        moneyTypes.add(moneyType4);

        WithdrawalRequestMoneyType moneyType5 = (WithdrawalRequestMoneyType) moneyType.clone();
        moneyType5.setMoneyTypeName("Money Type 5");
        moneyType5.setMoneyTypeId("MTID5");
        moneyType5.setMoneyTypeAliasId(null);
        moneyType5.setMoneyTypeCategoryCode("EE");
        moneyType5.setWithdrawalAmount(new BigDecimal(200.00));
        moneyTypes.add(moneyType5);

        WithdrawalRequestMoneyType moneyTypeZeroAmount = (WithdrawalRequestMoneyType) moneyType
                .clone();
        moneyTypeZeroAmount.setMoneyTypeName("Money Type Zero Amount");
        moneyTypeZeroAmount.setMoneyTypeAliasId("Zero Amount");
        moneyTypeZeroAmount.setMoneyTypeCategoryCode("EE");
        moneyTypeZeroAmount.setWithdrawalAmount(new BigDecimal(0.0));
        moneyTypes.add(moneyTypeZeroAmount);

        WithdrawalRequestMoneyType moneyTypeZeroBalanceZeroAmount = (WithdrawalRequestMoneyType) moneyType
                .clone();
        moneyTypeZeroBalanceZeroAmount.setMoneyTypeName("Money Type Zero Balance & Amount");
        moneyTypeZeroBalanceZeroAmount.setMoneyTypeAliasId("Zero Bal & Amount");
        moneyTypeZeroBalanceZeroAmount.setMoneyTypeCategoryCode("EE");
        moneyTypeZeroBalanceZeroAmount.setAvailableWithdrawalAmount(new BigDecimal(0.0));
        moneyTypeZeroBalanceZeroAmount.setWithdrawalAmount(new BigDecimal(0.0));
        moneyTypes.add(moneyTypeZeroBalanceZeroAmount);

        // Create the read only notes
        final Collection<WithdrawalRequestNote> participantNotes = new ArrayList<WithdrawalRequestNote>();
        final Collection<WithdrawalRequestNote> adminNotes = new ArrayList<WithdrawalRequestNote>();
        Calendar cal = Calendar.getInstance();

        final WithdrawalRequestNote participantNote1 = new WithdrawalRequestNote();
        participantNote1.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        participantNote1.setNote("This is an admin to participant note.");
        participantNote1.setCreatedById(LOGIN_ID_PS);
        participantNote1.setCreated(new Timestamp(cal.getTimeInMillis()));
        participantNotes.add(participantNote1);

        final WithdrawalRequestNote participantNote2 = new WithdrawalRequestNote();
        participantNote2.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        participantNote2.setNote("This is the newest admin to participant note.");
        participantNote2.setCreatedById(LOGIN_ID_PS);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        participantNote2.setCreated(new Timestamp(cal.getTimeInMillis()));
        participantNotes.add(participantNote2);

        final WithdrawalRequestNote participantNote3 = new WithdrawalRequestNote();
        participantNote3.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        participantNote3.setNote("This is the oldest admin to participant note.");
        participantNote3.setCreatedById(LOGIN_ID_PS);
        cal.add(Calendar.DAY_OF_MONTH, -2);
        participantNote3.setCreated(new Timestamp(cal.getTimeInMillis()));
        participantNotes.add(participantNote3);

        final WithdrawalRequestNote participantNote4 = new WithdrawalRequestNote();
        participantNote4.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        participantNote4
                .setNote("yyydyyyssyy yyyy yy y yy y yyyy y y yy y y yyyy y yy y yy yyy y y y yyyy yyy yy y yyyyy y y y yyyyyyy y yy yyy y yyy y yy yy y y yy y yyy y yy y yy y y yy yy y yy yy yy y yy yy");
        participantNote4.setCreatedById(LOGIN_ID_PS);
        cal.add(Calendar.DAY_OF_MONTH, -5);
        participantNote4.setCreated(new Timestamp(cal.getTimeInMillis()));
        participantNotes.add(participantNote4);

        final WithdrawalRequestNote adminNote1 = new WithdrawalRequestNote();
        adminNote1.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
        adminNote1.setNote("This is an admin to admin note.");
        adminNote1.setCreatedById(LOGIN_ID_PS);
        adminNote1.setCreated(new Timestamp(cal.getTimeInMillis()));
        adminNotes.add(adminNote1);

        final WithdrawalRequestNote adminNote2 = new WithdrawalRequestNote();
        adminNote2.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
        adminNote2.setNote("This is the oldest admin to admin note.");
        adminNote2.setCreatedById(LOGIN_ID_PS);
        cal.add(Calendar.DAY_OF_MONTH, -10);
        adminNote2.setCreated(new Timestamp(cal.getTimeInMillis()));
        adminNotes.add(adminNote2);

        final WithdrawalRequestNote adminNote3 = new WithdrawalRequestNote();
        adminNote3.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
        adminNote3.setNote("This is the newest admin to admin note.");
        adminNote3.setCreatedById(LOGIN_ID_PS);
        cal.add(Calendar.DAY_OF_MONTH, 15);
        adminNote3.setCreated(new Timestamp(cal.getTimeInMillis()));
        adminNotes.add(adminNote3);

        wr.setReadOnlyAdminToParticipantNotes(participantNotes);
        wr.setReadOnlyAdminToAdminNotes(adminNotes);

        return wr;
    }

    private Collection<WithdrawalRequestLoan> dummyLoanData() {
        Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();
        WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
        loan.setLoanNo(new Integer(1));
        loan.setOutstandingLoanAmount(new BigDecimal(40000));
        loans.add(loan);
        loan = null;
        loan = new WithdrawalRequestLoan();
        loan.setLoanNo(new Integer(2));
        loan.setOutstandingLoanAmount(new BigDecimal(12580));
        loans.add(loan);
        return loans;
    }

    private String lookupDescription(String item, String code) {
        ArrayList list = (ArrayList) lookupData.get(item);
        Iterator iterator = list.iterator();
        DeCodeVO vo;
        while (iterator.hasNext()) {
            vo = (DeCodeVO) iterator.next();
            if (vo.getCode().equals(code)) {
                return vo.getDescription();
            }
        }
        return "";
    }
}
