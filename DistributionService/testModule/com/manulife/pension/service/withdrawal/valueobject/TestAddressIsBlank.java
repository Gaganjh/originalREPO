package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * Tests the is blank query used to determine if the address object has been initialized / changed
 * from the default object.
 *
 * @author Andrew Dick
 */
public class TestAddressIsBlank {

    private static final String NON_BLANK_STRING = "Foo";

    private static final Integer NON_BLANK_INTEGER = 1;

    private static final Timestamp NON_BLANK_TIMESTAMP = new Timestamp(new Date().getTime());

    /**
     * Tests the is blank with all blank fields.
     */
    @Test
    public void testAllBlank() {

        final Address address = new Address();
        assertTrue("Address is blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank address line 1.
     */
    @Test
    public void testAddressLine1NonBlank() {

        final Address address = new Address();
        address.setAddressLine1(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank address line 2.
     */
    @Test
    public void testAddressLine2NonBlank() {

        final Address address = new Address();
        address.setAddressLine2(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank city.
     */
    @Test
    public void testCityNonBlank() {

        final Address address = new Address();
        address.setCity(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank state code.
     */
    @Test
    public void testStateCodeNonBlank() {

        final Address address = new Address();
        address.setStateCode(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank zip code 1.
     */
    @Test
    public void testZipCode1NonBlank() {

        final Address address = new Address();
        address.setZipCode1(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank zip code 2.
     */
    @Test
    public void testZipCode2NonBlank() {

        final Address address = new Address();
        address.setZipCode2(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank country code.
     */
    @Test
    public void testCountryCodeNonBlank() {

        final Address address = new Address();
        address.setCountryCode(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank recipient no.
     */
    @Test
    public void testRecipientNoNonBlank() {

        final Address address = new Address();
        address.setRecipientNo(NON_BLANK_INTEGER);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank payee no.
     */
    @Test
    public void testPayeeNoNonBlank() {

        final Address address = new Address();
        address.setPayeeNo(NON_BLANK_INTEGER);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank distribution type code.
     */
    @Test
    public void testDistributionTypeCodeNonBlank() {

        final Address address = new Address();
        address.setDistributionTypeCode(NON_BLANK_STRING);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank submission id.
     */
    @Test
    public void testSubmissionIdNonBlank() {

        final Address address = new Address();
        address.setSubmissionId(NON_BLANK_INTEGER);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank created by id.
     */
    @Test
    public void testCreatedByIdNonBlank() {

        final Address address = new Address();
        address.setCreatedById(NON_BLANK_INTEGER);
        assertTrue("Address is blank - created by id should be ignored.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank last updated by id.
     */
    @Test
    public void testLastUpdatedByIdNonBlank() {

        final Address address = new Address();
        address.setLastUpdatedById(NON_BLANK_INTEGER);
        assertTrue("Address is blank - last updated by id should be ignored.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank created.
     */
    @Test
    public void testCreatedNonBlank() {

        final Address address = new Address();
        address.setCreated(NON_BLANK_TIMESTAMP);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Tests the is blank with a non blank last updated.
     */
    @Test
    public void testLastUpdatedNonBlank() {

        final Address address = new Address();
        address.setLastUpdated(NON_BLANK_TIMESTAMP);
        assertFalse("Address is not blank.", address.isBlank());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestAddressIsBlank.class);
    }
}
