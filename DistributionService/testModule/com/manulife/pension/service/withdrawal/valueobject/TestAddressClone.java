package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BigIntegerConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;

public class TestAddressClone {

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUpConverters() throws Exception {

        // Need to ensure that our bean utils conversion helpers are registered
        // Re-register Timestamp converter.
        ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
        // Re-register BigDecimal converter.
        ConvertUtils.register(new BigDecimalConverter(null), java.math.BigDecimal.class);
        // Re-register BigInteger converter.
        ConvertUtils.register(new BigIntegerConverter(null), java.math.BigInteger.class);
        // Re-register Integer converter.
        ConvertUtils.register(new IntegerConverter(null), java.lang.Integer.class);
    }

    /**
     * Generates a base address object that is fully populated.
     *
     * @return Address - An address object.
     */
    private Address getBaseAddress() {

        final Address address = new Address();
        // Base address fields
        address.setAddressLine1("Address Line 1");
        address.setAddressLine2("Address Line 2");
        address.setCity("City");
        address.setZipCode("444444444");
        address.setStateCode("FL");
        address.setCountryCode("USA");
        address.setDistributionTypeCode("Distribution code.");
        address.setPayeeNo(11);
        address.setRecipientNo(22);

        // Meta data fields
        address.setCreated(new Timestamp(new Date().getTime()));
        address.setLastUpdated(new Timestamp(new Date().getTime()));
        address.setCreatedById(9999);
        address.setLastUpdatedById(8888);
        address.setSubmissionId(7777);

        // Error handling
        final Collection<WithdrawalMessage> errors = new ArrayList<WithdrawalMessage>();
        errors.add(new WithdrawalMessage(WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                CollectionUtils.EMPTY_COLLECTION));
        address.setErrorCodes(errors);
        final Collection<WithdrawalMessage> warnings = new ArrayList<WithdrawalMessage>();
        warnings.add(new WithdrawalMessage(WithdrawalMessageType.TEMP_WARNING,
                CollectionUtils.EMPTY_COLLECTION));
        address.setWarningCodes(warnings);
        final Collection<WithdrawalMessage> alerts = new ArrayList<WithdrawalMessage>();
        alerts.add(new WithdrawalMessage(WithdrawalMessageType.TEMP_ALERT,
                CollectionUtils.EMPTY_COLLECTION));
        address.setAlertCodes(alerts);

        return address;
    }

    /**
     * Tests the full clone method which copies all data and metadata.
     */
    @Test
    public void testClone() {

        final Address address = getBaseAddress();
        final Address clone = (Address) address.clone();

        assertEquals("Clone should be equal.", address, clone);

        assertNotSame("Clone should be a different object.", address, clone);
    }

    /**
     * Tests the partial clone method that only copies all address data.
     */
    @Test
    public void testCloneAddress() {

        final Address address = getBaseAddress();
        final Address clone = address.cloneAddress();

        // Verify not the same object
        Assert.assertNotSame("Address should be cloned.", address, clone);

        // Verify that address data copied
        assertEquals("Address line 1 should be copied.", address.getAddressLine1(), clone
                .getAddressLine1());
        assertEquals("Address line 2 should be copied.", address.getAddressLine2(), clone
                .getAddressLine2());
        assertEquals("City should be copied.", address.getCity(), clone.getCity());
        assertEquals("Zip code should be copied.", address.getZipCode(), clone.getZipCode());
        assertEquals("State code should be copied.", address.getStateCode(), clone.getStateCode());
        assertEquals("Country code should be copied.", address.getCountryCode(), clone
                .getCountryCode());
        assertEquals("Distribution type code should be copied.", address.getDistributionTypeCode(),
                clone.getDistributionTypeCode());
        assertEquals("Payee no should be copied.", address.getPayeeNo(), clone.getPayeeNo());
        assertEquals("Recipient no should be copied.", address.getRecipientNo(), clone
                .getRecipientNo());

        // Verify that address meta data has not been copied
        assertFalse("Created should not be copied.", address.getCreated()
                .equals(clone.getCreated()));
        assertFalse("Last updated should not be copied.", address.getLastUpdated().equals(
                clone.getLastUpdated()));
        assertFalse("Created by id should not be copied.", address.getCreatedById().equals(
                clone.getCreatedById()));
        assertFalse("Last updated by id should not be copied.", address.getLastUpdatedById()
                .equals(clone.getLastUpdatedById()));
        assertFalse("Submission id should not be copied.", address.getSubmissionId().equals(
                clone.getSubmissionId()));

        // Verify that error handling collections not copied
        assertFalse("Error codes should not be copied.", CollectionUtils.isEqualCollection(address
                .getErrorCodes(), clone.getErrorCodes()));
        assertFalse("Warning codes should not be copied.", CollectionUtils.isEqualCollection(
                address.getWarningCodes(), clone.getWarningCodes()));
        assertFalse("Alert codes should not be copied.", CollectionUtils.isEqualCollection(address
                .getAlertCodes(), clone.getAlertCodes()));
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestAddressClone.class);
    }
}
