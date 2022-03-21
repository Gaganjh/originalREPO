package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;

/**
 * Tests the zipcode getter and setter which assembles and desembles itself from the underlying two
 * zip code fields.
 *
 * @author Andrew Dick
 */
public class TestAddressZipCode {

    public static final String ZIP_CODE_1 = "12345";

    public static final String ZIP_CODE_2 = "6789";

    /**
     * Tests the getter with a null zip code 1.
     */
    @Test
    public void testGetterNullZipCode1() {

        final Address address = new Address();
        address.setZipCode1(ZIP_CODE_1);

        assertEquals("Zip code should be generated correctly.", ZIP_CODE_1, address.getZipCode());
    }

    /**
     * Tests the getter with a null zip code 2.
     */
    @Test
    public void testGetterNullZipCode2() {

        final Address address = new Address();
        address.setZipCode2(ZIP_CODE_2);

        assertEquals("Zip code should be generated correctly.", ZIP_CODE_2, address.getZipCode());
    }

    /**
     * Tests the getter with a null zip code 1 and zip code 2.
     */
    @Test
    public void testGetterNullZipCode1AndZipCode2() {

        final Address address = new Address();

        assertEquals("Zip code should be generated correctly.", StringUtils.EMPTY, address
                .getZipCode());
    }

    /**
     * Tests the getter with non-null zip code 1 and zip code 2.
     */
    @Test
    public void testGetterNonNullZipCode1AndZipCode2() {

        final Address address = new Address();
        address.setZipCode1(ZIP_CODE_1);
        address.setZipCode2(ZIP_CODE_2);

        assertEquals("Zip code should be generated correctly.", ZIP_CODE_1 + ZIP_CODE_2, address
                .getZipCode());
    }

    /**
     * Tests the setter with a null zip code 1.
     */
    @Test
    public void testSetterNullZipCode2() {

        final Address address = new Address();
        address.setZipCode(ZIP_CODE_1);

        assertEquals("Zip code 1 should be parsed correctly.", ZIP_CODE_1, address
                .getZipCode1());
        assertEquals("Zip code 2 should be parsed correctly.", StringUtils.EMPTY, address.getZipCode2());
    }

    /**
     * Tests the setter with a null zip code 1 and zip code 2.
     */
    @Test
    public void testSetterNullZipCode1AndZipCode2() {

        final Address address = new Address();
        address.setZipCode(null);

        assertEquals("Zip code 1 should be parsed correctly.", StringUtils.EMPTY, address
                .getZipCode1());
        assertEquals("Zip code 2 should be parsed correctly.", StringUtils.EMPTY, address
                .getZipCode2());
    }

    /**
     * Tests the setter with non-null zip code 1 and zip code 2.
     */
    @Test
    public void testSetterNonNullZipCode1AndZipCode2() {

        final Address address = new Address();
        address.setZipCode(ZIP_CODE_1 + ZIP_CODE_2);

        assertEquals("Zip code 1 should be parsed correctly.", ZIP_CODE_1, address.getZipCode1());
        assertEquals("Zip code 2 should be parsed correctly.", ZIP_CODE_2, address.getZipCode2());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestAddressZipCode.class);
    }
}
