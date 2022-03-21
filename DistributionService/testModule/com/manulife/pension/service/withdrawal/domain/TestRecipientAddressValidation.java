package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.BaseWithdrawalServiceTestCase;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the validation of recipient addresses.
 * 
 * @author dickand
 */
public class TestRecipientAddressValidation extends BaseWithdrawalServiceTestCase {

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
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)new WithdrawalRequestRecipient();
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        final Address address = (Address)new Address();
        address.setAddressLine1(VALID_ADDRESS_LINE_1);
        address.setCity(VALID_CITY);
        address.setStateCode(VALID_STATE);
        address.setZipCode(NUMERIC_ZIP_CODE_ONE);
        address.setCountryCode(VALID_COUNTRY_CODE);
        recipient.setAddress(address);
        recipients.add(recipient);
        request.setRecipients(recipients);
        return request;
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testAddressLine1Blank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setAddressLine1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testAddressLine1Null() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setAddressLine1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testAddressLine1Valid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress()
                .setAddressLine1(VALID_ADDRESS_LINE_1);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCityBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCity(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCityNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCity(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCityValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCity(VALID_CITY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateBlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setStateCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateNullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setStateCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        recipient.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testStateValidWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setStateCode(VALID_STATE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode1BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode1NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode1NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode1NumericNonValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setStateCode(SECOND_VALID_STATE);
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one warning.", 1, CollectionUtils.size(address
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE).append("].")
                .toString(), CollectionUtils.exists(address.getMessages(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE)));
        assertEquals("There should be one message for the field.", 1, CollectionUtils.countMatches(
                address.getMessages(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode1NumericValidWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setStateCode(VALID_STATE);
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCodeBlankWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setZipCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCodeNullWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setZipCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCodeNonNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setZipCode(ALPHA_NUMERIC_ZIP_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCodeNumericWithCountryNonUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(VALID_NONUS_COUNTRY_CODE);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        recipient.getAddress().setZipCode2(NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode2BlankWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        recipient.getAddress().setZipCode2(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode2NullWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        recipient.getAddress().setZipCode2(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testZipCode2NonNumericWithCountryUsa() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)(WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setCountryCode(GlobalConstants.COUNTRY_CODE_USA);
        recipient.getAddress().setZipCode1(NUMERIC_ZIP_CODE_ONE);
        recipient.getAddress().setZipCode2(ALPHA_NUMERIC_ZIP_CODE_TWO);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)(Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_TWO_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_TWO_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCountryBlank() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCountryCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCountryNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCountryCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be an error on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be one error.", 1, CollectionUtils.size(address.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.COUNTRY_CODE)));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testCountryValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.getRecipients().iterator().next().getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(request.getRecipients().iterator().next());

        // Should be no errors on address
        final Address address = (Address)withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                .getAddress();
        assertEquals("There should be no errors.", 0, CollectionUtils.size(address.getErrorCodes()));
    }

    /**
     * Tests the validations for the recipient address.
     */
    @Test
    public void testRecipientMultipleErrors() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        recipient.getAddress().setAddressLine1(StringUtils.EMPTY);
        recipient.getAddress().setCity(null);
        recipient.getAddress().setStateCode(StringUtils.EMPTY);
        recipient.getAddress().setZipCode1(ALPHA_NUMERIC_ZIP_CODE_ONE);
        recipient.getAddress().setCountryCode(VALID_COUNTRY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform amount type validation
        withdrawal.validateRecipientAddresses(recipient);

        // Should be four errors on address
        final Address address = (Address)recipient.getAddress();
        assertEquals("There should be four errors.", 4, CollectionUtils.size(address
                .getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ADDRESS_LINE_ONE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.CITY)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.STATE_CODE)));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID).append("].").toString(),
                CollectionUtils.exists(address.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                address.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.ZIP_CODE)));
    }
}
