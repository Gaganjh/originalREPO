/*
 * WithdrawalTest.java,v 1.1 2006/10/02 20:01:50 Paul_Glenn Exp
 * WithdrawalTest.java,v
 * Revision 1.1  2006/10/02 20:01:50  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import static com.manulife.pension.service.testutility.Assert.assertBigDecimalValueEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.dao.EnvironmentDAO;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxType;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * WithdrawalTest tests the {@link Withdrawal} domain object.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/02 20:01:50
 */
public class WithdrawalTestOld extends DistributionContainerEnvironment {

    final static Logger logger = Logger.getLogger(WithdrawalTestOld.class);

    private final static String US_SITE_ID = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA;

    private final static String NY_SITE_ID = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY;

    public static final int LEGALESE_STATIC_CONTENT_TEXT = 56216;

    public static final int LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT = 55750;

    public static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT = 56217;

    public static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT = 56218;

    public static final int LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT = 56219;

    private WithdrawalRequest withdrawalRequest;

    private WithdrawalRequest withdrawalRequestDraft;

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(WithdrawalTestOld.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();

        withdrawalRequestDraft = new WithdrawalRequest();
        withdrawalRequest = new WithdrawalRequest();
        withdrawalRequestDraft.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());

    }

    private void setSiteLocation(final String site) {
        final ParticipantInfo info = new ParticipantInfo();
        info.setManulifeCompanyId(site);
        withdrawalRequest.setParticipantInfo(info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Gets the message type of the first error in the error collection.
     * 
     * @param withdrawal The withdrawal to look for errors in.
     * @return WithdrawalMessageType The message type found.
     */
    private WithdrawalMessageType getFirstErrorType(final Withdrawal withdrawal) {
        return (withdrawal.getWithdrawalRequest().getErrorCodes().iterator().next())
                .getWithdrawalMessageType();
    }

    /**
     * Test method for {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#save()}.
     */
    @Ignore
    @Test
    public void testSaveFromDraft() throws DistributionServiceException {

        final WithdrawalRequest withdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft
                .clone();
        withdrawalRequest.setContractId(new Integer(70300));
        withdrawalRequest.setLastUpdatedById(new Integer(1));

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.save();

        assertEquals("Status should be draft after save.", withdrawal.getWithdrawalRequest()
                .getStatusCode(), WithdrawalStateEnum.DRAFT.getStatusCode());
    }

    @Test
    public void testDenyFromDraft() throws DistributionServiceException {
        final Withdrawal withdrawal = new Withdrawal(withdrawalRequestDraft);

        try {
            withdrawal.deny();
        } catch (final IllegalStateException illegalStateException) {
            // This is the success condition.
            return;
        }

        fail("Deny is not allowed in Draft state.");
    }

    @Test
    public void testInRangeBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date twentyOneYearsOld = DateUtils.truncate(DateUtils.addYears(new Date(), -21),
                Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(twentyOneYearsOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 21 years old.", CollectionUtils.isEmpty(withdrawal.getWithdrawalRequest()
                .getErrorCodes()));
    }

    @Test
    public void testTooYoungBirthDate() {

        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date twelveYearsOld = DateUtils.truncate(DateUtils.addYears(new Date(), -12),
                Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(twelveYearsOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 12 years old.", CollectionUtils.isNotEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue("Test 12 years old error.", getFirstErrorType(withdrawal).equals(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID));
    }

    @Test
    public void testLowerBoundBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date fifteenYearsOld = DateUtils.truncate(DateUtils.addYears(new Date(), -15),
                Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(fifteenYearsOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();

        assertTrue("Test 15 years old.", CollectionUtils.isEmpty(withdrawal.getWithdrawalRequest()
                .getErrorCodes()));
    }

    @Test
    public void testInsideLowerBoundBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date fifteenYearsAndOneDayOld = DateUtils.truncate(DateUtils.addDays(DateUtils
                .addYears(new Date(), -15), -1), Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(fifteenYearsAndOneDayOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 15 years +1day old.", CollectionUtils.isEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
    }

    @Test
    public void testUpperBoundBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date oneHundredAndFiftyYearsOld = DateUtils.truncate(DateUtils.addYears(new Date(),
                -150), Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(oneHundredAndFiftyYearsOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 150 years old.", CollectionUtils.isEmpty(withdrawal.getWithdrawalRequest()
                .getErrorCodes()));
    }

    @Test
    public void testOutsideUpperBoundBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date oneHundredAndFiftyYearsAndOneDayOld = DateUtils.truncate(DateUtils.addDays(
                DateUtils.addYears(new Date(), -150), -1), Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(oneHundredAndFiftyYearsAndOneDayOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 150 years +1day old.", CollectionUtils.isNotEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue("Test 150 years +1day old error.", getFirstErrorType(withdrawal).equals(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID));
    }

    @Test
    public void testTooOldBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date oneHundredAndFiftyFiveYearsOld = DateUtils.truncate(DateUtils.addYears(
                new Date(), -155), Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(oneHundredAndFiftyFiveYearsOld);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Test 155 years old.", CollectionUtils.isNotEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue("Test 155 years old error.", getFirstErrorType(withdrawal).equals(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID));
    }

    @Test
    public void testNullBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        // final Date oneHundredAndFiftyFiveYearsOld = DateUtils.truncate(DateUtils.addYears(
        // new Date(), -155), Calendar.DATE);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(null);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Birth date is null should produce an error.", CollectionUtils
                .isNotEmpty(withdrawal.getWithdrawalRequest().getErrorCodes()));
        assertTrue("Verify that the Birth date empty/blank message is given.", getFirstErrorType(
                withdrawal).equals(WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK));
    }

    @Test
    public void testFutureBirthDate() {
        WithdrawalRequest myWithdrawalRequest;
        Withdrawal withdrawal;

        final Date futureDate = DateUtils.addDays(new Date(), 35);

        myWithdrawalRequest = (WithdrawalRequest) withdrawalRequestDraft.clone();
        myWithdrawalRequest.setBirthDate(futureDate);
        withdrawal = new Withdrawal(myWithdrawalRequest);
        withdrawal.validateBirthDate();
        assertTrue("Birth date is in future", CollectionUtils.isNotEmpty(withdrawal
                .getWithdrawalRequest().getErrorCodes()));
        assertTrue("Birth date is in future", getFirstErrorType(withdrawal).equals(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID));
    }

    @Test
    public void testNYTaxEligibleForRollover() {

        final String stateCode = "NY";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(20);
        final Boolean taxRolloverIndicator = Boolean.TRUE;
        final BigDecimal stateTaxDefaultPercentage = BigDecimal.ZERO;
        final BigDecimal stateTaxMinimumPercentage = BigDecimal.ZERO;
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(100);
        final Boolean stateTaxRequired = Boolean.FALSE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_WITHDRAWAL;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testNYTaxNotEligibleForRollover() {

        final String stateCode = "NY";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(10);
        final Boolean taxRolloverIndicator = Boolean.FALSE;
        final BigDecimal stateTaxDefaultPercentage = BigDecimal.ZERO;
        final BigDecimal stateTaxMinimumPercentage = BigDecimal.ZERO;
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(100);
        final Boolean stateTaxRequired = Boolean.FALSE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_WITHDRAWAL;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testNDTaxEligibleForRollover() {

        final String stateCode = "ND";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(20);
        final Boolean taxRolloverIndicator = Boolean.TRUE;
        final BigDecimal stateTaxDefaultPercentage = new BigDecimal(21);
        final BigDecimal stateTaxMinimumPercentage = new BigDecimal(21);
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(21);
        final Boolean stateTaxRequired = Boolean.FALSE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testNDTaxNotEligibleForRollover() {

        final String stateCode = "ND";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(10);
        final Boolean taxRolloverIndicator = Boolean.FALSE;
        final BigDecimal stateTaxDefaultPercentage = new BigDecimal(21);
        final BigDecimal stateTaxMinimumPercentage = new BigDecimal(21);
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(21);
        final Boolean stateTaxRequired = Boolean.FALSE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testVTTaxEligibleForRollover() {

        final String stateCode = "VT";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(20);
        final Boolean taxRolloverIndicator = Boolean.TRUE;
        final BigDecimal stateTaxDefaultPercentage = new BigDecimal(27);
        final BigDecimal stateTaxMinimumPercentage = new BigDecimal(27);
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(100);
        final Boolean stateTaxRequired = Boolean.TRUE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testVTTaxNOTEligibleForRollover() {

        final String stateCode = "VT";
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(10);
        final Boolean taxRolloverIndicator = Boolean.FALSE;
        final BigDecimal stateTaxDefaultPercentage = new BigDecimal(27);
        final BigDecimal stateTaxMinimumPercentage = new BigDecimal(27);
        final BigDecimal stateTaxMaximumPercentage = new BigDecimal(100);
        final Boolean stateTaxRequired = Boolean.TRUE;
        final String stateTaxTypeCode = StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX;

        testTaxes(stateCode, reasonCode, federalTaxPercentage, taxRolloverIndicator,
                stateTaxDefaultPercentage, stateTaxMinimumPercentage, stateTaxMaximumPercentage,
                stateTaxRequired, stateTaxTypeCode);

    }

    @Test
    public void testZZTaxEligibleForRollover() {

        final String stateCode = WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US;
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(20);
        final Boolean taxRolloverIndicator = Boolean.TRUE;

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.setReasonCode(reasonCode);

        final WithdrawalRequestRecipient withdrawalRequestRecipientInput = new WithdrawalRequestRecipient();
        withdrawalRequestRecipientInput.setStateOfResidenceCode(stateCode);

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipientInput);

        assertEquals("Should have one recipient.", 1, withdrawalRequest.getRecipients().size());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.updateTax();

        final WithdrawalRequest result = withdrawal.getWithdrawalRequest();

        final Recipient withdrawalRequestRecipient = result.getRecipients()
                .iterator().next();

        final FederalTaxVO federalTaxVo = result.getFederalTaxVo();
        final StateTaxVO stateTaxVo = withdrawalRequestRecipient.getStateTaxVo();

        assertBigDecimalValueEquals("Federal tax %: ", federalTaxPercentage, federalTaxVo
                .getTaxPercentage());

        assertEquals("Federal tax rollover indicator: ", taxRolloverIndicator, federalTaxVo
                .getRolloverIndicator());

        assertEquals("State tax should be null: ", null, stateTaxVo);
    }

    @Test
    public void testZZTaxNotEligibleForRollover() {

        final String stateCode = WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US;
        final String reasonCode = WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE;
        final BigDecimal federalTaxPercentage = new BigDecimal(10);
        final Boolean taxRolloverIndicator = Boolean.FALSE;

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.setReasonCode(reasonCode);

        final WithdrawalRequestRecipient withdrawalRequestRecipientInput = new WithdrawalRequestRecipient();
        withdrawalRequestRecipientInput.setStateOfResidenceCode(stateCode);

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipientInput);

        assertEquals("Should have one recipient.", 1, withdrawalRequest.getRecipients().size());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.updateTax();

        final WithdrawalRequest result = withdrawal.getWithdrawalRequest();

        final Recipient withdrawalRequestRecipient = result.getRecipients()
                .iterator().next();

        final FederalTaxVO federalTaxVo = result.getFederalTaxVo();
        final StateTaxVO stateTaxVo = withdrawalRequestRecipient.getStateTaxVo();

        assertBigDecimalValueEquals("Federal tax %: ", federalTaxPercentage, federalTaxVo
                .getTaxPercentage());

        assertEquals("Federal tax rollover indicator: ", taxRolloverIndicator, federalTaxVo
                .getRolloverIndicator());

        assertEquals("State tax should be null: ", null, stateTaxVo);
    }

    /**
     * This method tests the federal and state taxes for the given state and withdrawal reason. The
     * expected results are passed in.
     * 
     * @param stateCode The state code to test.
     * @param reasonCode The withdrawal reason to use.
     * @param federalTaxPercentage The expected federalTaxPercentage.
     * @param taxRolloverIndicator The expected taxRolloverIndicator.
     * @param stateTaxDefaultPercentage The expected stateTaxDefaultPercentage.
     * @param stateTaxMinimumPercentage The expected stateTaxMinimumPercentage.
     * @param stateTaxMaximumPercentage The expected stateTaxMaximumPercentage.
     * @param stateTaxRequired The expected stateTaxRequired.
     * @param stateTaxTypeCode The expected stateTaxTypeCode.
     */
    private void testTaxes(final String stateCode, final String reasonCode,
            final BigDecimal federalTaxPercentage, final Boolean taxRolloverIndicator,
            final BigDecimal stateTaxDefaultPercentage, final BigDecimal stateTaxMinimumPercentage,
            final BigDecimal stateTaxMaximumPercentage, final Boolean stateTaxRequired,
            final String stateTaxTypeCode) {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.setReasonCode(reasonCode);

        final WithdrawalRequestRecipient withdrawalRequestRecipientInput = new WithdrawalRequestRecipient();
        withdrawalRequestRecipientInput.setStateOfResidenceCode(stateCode);

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipientInput);

        assertEquals("Should have one recipient.", 1, withdrawalRequest.getRecipients().size());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.updateTax();

        final WithdrawalRequest result = withdrawal.getWithdrawalRequest();

        final Recipient withdrawalRequestRecipient = result.getRecipients()
                .iterator().next();

        final FederalTaxVO federalTaxVo = result.getFederalTaxVo();
        final StateTaxVO stateTaxVo = withdrawalRequestRecipient.getStateTaxVo();

        assertBigDecimalValueEquals("Federal tax %: ", federalTaxPercentage, federalTaxVo
                .getTaxPercentage());

        assertEquals("Federal tax rollover indicator: ", taxRolloverIndicator, federalTaxVo
                .getRolloverIndicator());

        assertBigDecimalValueEquals("State tax default %: ", stateTaxDefaultPercentage, stateTaxVo
                .getDefaultTaxRatePercentage());

        assertBigDecimalValueEquals("State tax minimum %: ", stateTaxMinimumPercentage, stateTaxVo
                .getTaxPercentageMinimum());

        assertBigDecimalValueEquals("State tax maximum %: ", stateTaxMaximumPercentage, stateTaxVo
                .getTaxPercentageMaximum());

        assertEquals("State tax required: ", stateTaxRequired, stateTaxVo.getTaxRequiredIndicator());

        assertEquals("State tax type: ", stateTaxTypeCode, stateTaxVo.getTaxTypeCode());

        assertEquals("State tax rollover indicator: ", taxRolloverIndicator, stateTaxVo
                .getRolloverIndicator());
    }

    /**
     * Tests the state tax type of the lookedup {@link StateTaxVO}.
     */
    @Test
    public void testStateTaxTypeMandatory() {

        final String stateOfResidenceCode = "AR";
        final boolean eligibleForRollover = false;

        // Note: The state tax can come back as null (if the state isn't found (like the
        // 'Outside the US').
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();
        final StateTaxVO stateTaxVo;
        try {
            stateTaxVo = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                    eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state tax", systemException);
        } // end try/catch

        assertEquals("Type should be mandatory.", stateTaxVo.getStateTaxType(),
                StateTaxType.MANDATORY);

    }

    /**
     * Tests the state tax type of the lookedup {@link StateTaxVO}.
     */
    @Test
    public void testStateTaxTypeNone() {

        final String stateOfResidenceCode = "FL";
        final boolean eligibleForRollover = false;

        // Note: The state tax can come back as null (if the state isn't found (like the
        // 'Outside the US').
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();
        final StateTaxVO stateTaxVo;
        try {
            stateTaxVo = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                    eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state tax", systemException);
        } // end try/catch

        assertEquals("Type should be none.", stateTaxVo.getStateTaxType(), StateTaxType.NONE);

    }

    /**
     * Tests the state tax type of the lookedup {@link StateTaxVO}.
     */
    @Test
    public void testStateTaxTypeVoluntaryFixed() {

        final String stateOfResidenceCode = "ND";
        final boolean eligibleForRollover = true;

        // Note: The state tax can come back as null (if the state isn't found (like the
        // 'Outside the US').
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();
        final StateTaxVO stateTaxVo;
        try {
            stateTaxVo = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                    eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state tax", systemException);
        } // end try/catch

        assertEquals("Type should be voluntary fixed.", stateTaxVo.getStateTaxType(),
                StateTaxType.VOLUNTARY_FIXED);

    }

    /**
     * Tests the state tax type of the lookedup {@link StateTaxVO}.
     */
    @Test
    public void testStateTaxTypeOptOut() {

        final String stateOfResidenceCode = "CA";
        final boolean eligibleForRollover = false;

        // Note: The state tax can come back as null (if the state isn't found (like the
        // 'Outside the US').
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();
        final StateTaxVO stateTaxVo;
        try {
            stateTaxVo = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                    eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state tax", systemException);
        } // end try/catch

        assertEquals("Type should be opt out.", stateTaxVo.getStateTaxType(), StateTaxType.OPT_OUT);

    }

    /**
     * Tests the state tax type of the lookedup {@link StateTaxVO}.
     */
    @Test
    public void testStateTaxTypeVoluntaryFreeForm() {

        final String stateOfResidenceCode = "NY";
        final boolean eligibleForRollover = false;

        // Note: The state tax can come back as null (if the state isn't found (like the
        // 'Outside the US').
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();
        final StateTaxVO stateTaxVo;
        try {
            stateTaxVo = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                    eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state tax", systemException);
        } // end try/catch

        assertEquals("Type should be voluntary free form.", stateTaxVo.getStateTaxType(),
                StateTaxType.VOLUNTARY_FREE_FORM);

    }

    /**
     * Tests the StateTax validations.
     */
    @Test
    public void testStateTaxFreeFormBelowMaximum() {

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);

        final WithdrawalRequestRecipient withdrawalRequestRecipientInput = new WithdrawalRequestRecipient();
        withdrawalRequestRecipientInput.setStateOfResidenceCode("NY");
        withdrawalRequestRecipientInput.setPayees(new ArrayList<Payee>() {
            /**
             * Default serial version UID.
             */
            private static final long serialVersionUID = 1L;

            {
                add(new WithdrawalRequestPayee());
            }
        });
        withdrawalRequest.setVestingCalledInd(Boolean.TRUE);

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipientInput);

        withdrawalRequestRecipientInput.setStateTaxPercent(new BigDecimal("5"));

        assertEquals("Should have one recipient.", 1, withdrawalRequest.getRecipients().size());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.recalculate();

        final WithdrawalRequest result = withdrawal.getWithdrawalRequest();

        final WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient)result.getRecipients()
                .iterator().next();

        final StateTaxVO stateTaxVo = withdrawalRequestRecipient.getStateTaxVo();

        // Should be no messages.
        assertEquals("There should be no messages.", 0, CollectionUtils
                .size(withdrawalRequestRecipient.getMessages()));

    }

    /**
     * Tests the StateTax validations.
     * 
     * Note: You need to see the homestate data to make this test succeed, ignored for now.
     */
    @Test
    @Ignore
    public void testStateTaxFreeFormAboveMaximum() {

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        withdrawalRequest.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        final WithdrawalRequestRecipient withdrawalRequestRecipientInput = new WithdrawalRequestRecipient();
        withdrawalRequestRecipientInput.setStateOfResidenceCode("CA");
        withdrawalRequestRecipientInput.setPayees(new ArrayList<Payee>() {
            /**
             * Default serial version UID.
             */
            private static final long serialVersionUID = 1L;

            {
                add(new WithdrawalRequestPayee());
            }
        });
        withdrawalRequest.setVestingCalledInd(Boolean.TRUE);

        withdrawalRequest.getRecipients().add(withdrawalRequestRecipientInput);

        withdrawalRequestRecipientInput.setStateTaxPercent(new BigDecimal("90"));
        withdrawalRequestRecipientInput.setFederalTaxPercent(new BigDecimal("10"));
        final StateTaxVO stateTaxVo = new StateTaxVO();
        stateTaxVo.setTaxPercentageMaximum(new BigDecimal("50"));
        stateTaxVo.setTaxPercentageMinimum(new BigDecimal("0"));
        stateTaxVo.setDefaultTaxRatePercentage(new BigDecimal("10"));
        withdrawalRequestRecipientInput.setStateTaxVo(stateTaxVo);
        assertEquals("Should have one recipient.", 1, withdrawalRequest.getRecipients().size());

        withdrawalRequest.setMoneyTypes(new ArrayList<WithdrawalRequestMoneyType>() {
            private static final long serialVersionUID = 1L;

            {
                add(new WithdrawalRequestMoneyType() {
                    private static final long serialVersionUID = 1L;
                    {
                        setWithdrawalAmount(new BigDecimal("1000"));
                    }
                });
            }
        });

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.validateForRecalculate();

        final WithdrawalRequest result = withdrawal.getWithdrawalRequest();

        final WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient)result.getRecipients()
                .iterator().next();

        // Should be an error.
        assertTrue("There should be an error.", CollectionUtils
                .isNotEmpty(withdrawalRequestRecipient.getMessages()));
        assertEquals("There should be one error.", 1, CollectionUtils
                .size(withdrawalRequestRecipient.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.STATE_TAX_EXCEEDS_MAXIMUM).append("].").toString(),
                CollectionUtils.exists(withdrawalRequestRecipient.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.STATE_TAX_EXCEEDS_MAXIMUM)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                withdrawalRequestRecipient.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_TAX_PERCENT)));

    }

    public void testRetrieveContentStaticUS() {
        setSiteLocation(US_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);
        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_STATIC_CONTENT_TEXT));
        assertNotNull(contentText);
    }

    public void testRetrieveContentStaticNY() {
        setSiteLocation(NY_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_STATIC_CONTENT_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentMTUS() {
        setSiteLocation(US_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentMTNY() {

        setSiteLocation(NY_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;

        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT));
        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentYesUS() {

        setSiteLocation(US_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;

        contentText = w
                .retrieveContent(new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentYesNY() {

        setSiteLocation(NY_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;

        contentText = w
                .retrieveContent(new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentNoUS() {

        setSiteLocation(US_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;

        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentNoNY() {

        setSiteLocation(NY_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);
        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentBlankUS() {

        setSiteLocation(US_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT));

        assertNotNull(contentText);
    }

    public void testRetrieveContentSpousalConsentBlankNY() {

        setSiteLocation(NY_SITE_ID);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        final Withdrawal w = new Withdrawal(withdrawalRequest);

        String contentText = null;
        contentText = w.retrieveContent(new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT));

        assertNotNull(contentText);
    }
}
