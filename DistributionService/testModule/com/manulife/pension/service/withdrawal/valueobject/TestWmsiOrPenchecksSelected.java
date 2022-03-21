package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * Tests if the WMSI or Penchecks indicator has been selected.
 *
 * @author Andrew Dick
 */
public class TestWmsiOrPenchecksSelected {

    /**
     * Tests whether the WMSI or Penchecks is selected works when WMSI is selected.
     */
    @Test
    public void testWmsiSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        assertTrue("WMSI or Penchecks should be selected.", request.isWmsiOrPenchecksSelected());
    }

    /**
     * Tests whether the WMSI or Penchecks is selected works when Penchecks is selected.
     */
    @Test
    public void testPenChecksSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        assertTrue("WMSI or Penchecks should be selected.", request.isWmsiOrPenchecksSelected());
    }

    /**
     * Tests whether the WMSI or Penchecks is selected works when neither is selected.
     */
    @Test
    public void testNeitherSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        assertFalse("WMSI or Penchecks should not be selected.", request
                .isWmsiOrPenchecksSelected());
    }

    /**
     * Tests whether the WMSI or Penchecks is selected works when nothing (null) has been selected.
     * This is currently defined as being the same as neither.
     */
    @Test
    public void testNothingSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode(null);
        assertFalse("WMSI or Penchecks should not be selected.", request
                .isWmsiOrPenchecksSelected());
    }

    /**
     * Tests whether the WMSI or Penchecks is selected works when nothing (blank) has been selected.
     * This is currently defined as being the same as neither.
     */
    @Test
    public void testBlankSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode("");
        assertFalse("WMSI or Penchecks should not be selected.", request
                .isWmsiOrPenchecksSelected());
    }

    /**
     * Tests whether the WMSI or Penchecks is selected works when an invalid selection has been
     * selected. This is currently defined as being the same as neither.
     */
    @Test
    public void testInvalidSelected() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setIraServiceProviderCode("NOT A CODE");
        assertFalse("WMSI or Penchecks should not be selected.", request
                .isWmsiOrPenchecksSelected());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestWmsiOrPenchecksSelected.class);
    }
}
