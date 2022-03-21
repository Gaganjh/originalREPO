package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.BaseWithdrawalServiceTestCase;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the validation of payee addresses.
 * 
 * @author dickand
 * @TODO - Need to add tests for FI (ACH AND WIRE)
 * @TODO - Need to add test to verify multiple errors
 */
public class TestPayeeAddressValidation extends BaseWithdrawalServiceTestCase {

    private static final String VALID_ADDRESS_LINE_1 = "Address Line 1";

    private static final String VALID_CITY = "City";

    private static final String VALID_STATE = "CA";

    private static final String SECOND_VALID_STATE = "FL";

    private static final String NUMERIC_ZIP_CODE_ONE = "90210";

    private static final String NUMERIC_ZIP_CODE_TWO = "1234";

    private static final String ALPHA_NUMERIC_ZIP_CODE = "abc123";

    private static final String ALPHA_NUMERIC_ZIP_CODE_ONE = "abc12";

    private static final String ALPHA_NUMERIC_ZIP_CODE_TWO = "zy98";

    private static final String VALID_COUNTRY_CODE = Address.USA_COUNTRY_CODE;

    private static final String VALID_NONUS_COUNTRY_CODE = "CAN";

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        final Address address = new Address();
        address.setAddressLine1(VALID_ADDRESS_LINE_1);
        address.setCity(VALID_CITY);
        address.setStateCode(VALID_STATE);
        address.setZipCode(NUMERIC_ZIP_CODE_ONE);
        address.setCountryCode(VALID_COUNTRY_CODE);
        payee.setAddress(address);
        final Collection<Payee> payees = new ArrayList<Payee>();
        payees.add(payee);
        recipient.setPayees(payees);
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);
        return request;
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeAddressLine1Blank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID).append("].")
                        .toString(), CollectionUtils.exists(address.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeAddressLine1Null() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID).append("].")
                        .toString(), CollectionUtils.exists(address.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeAddressLine1Valid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(VALID_ADDRESS_LINE_1);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCityBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCityNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCityValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(VALID_CITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateBlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateNullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeStateValidWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode1BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setZipCode1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode1NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode1NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode1NumericNonValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(SECOND_VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one warning.", 1, CollectionUtils.size(address
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE).append("].")
                .toString(), CollectionUtils.exists(address.getMessages(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE)));
        assertEquals("There should be one message for the field.", 1, CollectionUtils.countMatches(
                address.getMessages(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode1NumericValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCodeBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCodeNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCodeNonNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(ALPHA_NUMERIC_ZIP_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCodeNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode2BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode2NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeZipCode2NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(ALPHA_NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCountryBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCountryNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeCountryValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeAddressLine1Blank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeAddressLine1Null() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeAddressLine1Valid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(VALID_ADDRESS_LINE_1);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCityBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCityNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCityValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(VALID_CITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateBlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateNullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeStateValidWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode1BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setZipCode1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode1NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode1NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode1NumericNonValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(SECOND_VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one warning.", 1, CollectionUtils.size(address
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE).append("].")
                .toString(), CollectionUtils.exists(address.getMessages(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE)));
        assertEquals("There should be one message for the field.", 1, CollectionUtils.countMatches(
                address.getMessages(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode1NumericValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCodeBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCodeNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCodeNonNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(ALPHA_NUMERIC_ZIP_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCodeNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode2BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode2NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeZipCode2NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(ALPHA_NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCountryBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCountryNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeCountryValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeAddressLine1Blank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeAddressLine1Null() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeAddressLine1Valid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(VALID_ADDRESS_LINE_1);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCityBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCityNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCityValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCity(VALID_CITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateBlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateNullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeStateValidWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode1BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        payee.getAddress().setZipCode1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode1NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode1NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode1NumericNonValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(SECOND_VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one warning.", 1, CollectionUtils.size(address
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE).append("].")
                .toString(), CollectionUtils.exists(address.getMessages(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE)));
        assertEquals("There should be one message for the field.", 1, CollectionUtils.countMatches(
                address.getMessages(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode1NumericValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setStateCode(VALID_STATE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCodeBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCodeNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCodeNonNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode(ALPHA_NUMERIC_ZIP_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCodeNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode2BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode2NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeZipCode2NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        payee.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setZipCode2(ALPHA_NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCountryBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCountryNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be an error on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeCountryValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be no errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testCheckPayeeMultipleErrors() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        payee.getAddress().setCity(null);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be four errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be four errors.", 4, CollectionUtils.size(address
                .getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID).append("].")
                        .toString(), CollectionUtils.exists(address.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testWirePayeeMultipleErrors() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        payee.getAddress().setCity(null);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be four errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be four errors.", 4, CollectionUtils.size(address
                .getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the payee address.
     */
    @Test
    public void testAchPayeeMultipleErrors() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestPayee payee = (WithdrawalRequestPayee)request.getRecipients().iterator().next().getPayees()
                .iterator().next();
        payee.setPaymentMethodCode(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE);
        payee.getAddress().setAddressLine1(StringUtils.EMPTY);
        payee.getAddress().setCity(null);
        payee.getAddress().setStateCode(StringUtils.EMPTY);
        payee.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        payee.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validatePayeeAddresses(payee);

        // Should be four errors on address
        final Address address = (Address)payee.getAddress();
        assertEquals("There should be four errors.", 4, CollectionUtils.size(address
                .getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }
}
