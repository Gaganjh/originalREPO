package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * Tests the is blank query used to determine if the payment instruction object has been initialized /
 * changed from the default object.
 *
 * @author Andrew Dick
 */
public class TestPayeePaymentInstructionIsBlank {

    private static final String NON_BLANK_STRING = "Foo";

    private static final Integer NON_BLANK_INTEGER = 1;

    /**
     * Tests the is blank with all blank fields.
     */
    @Test
    public void testAllBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        assertTrue("Instruction is blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank bank account type code.
     */
    @Test
    public void testBankAccountTypeCodeNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setBankAccountTypeCode(NON_BLANK_STRING);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank bank transit number.
     */
    @Test
    public void testBankTransitNumberNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setBankTransitNumber(NON_BLANK_INTEGER);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank bank account number.
     */
    @Test
    public void testBankAccountNumberNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setBankAccountNumber(NON_BLANK_STRING);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank bank name.
     */
    @Test
    public void testBankNameNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setBankName(NON_BLANK_STRING);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank attention name.
     */
    @Test
    public void testAttentionNameNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setAttentionName(NON_BLANK_STRING);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank credit party name.
     */
    @Test
    public void testCreditPartyNameNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setCreditPartyName(NON_BLANK_STRING);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank recipient no.
     */
    @Test
    public void testRecipientNoNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setRecipientNo(NON_BLANK_INTEGER);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank payee no.
     */
    @Test
    public void testPayeeNoNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setPayeeNo(NON_BLANK_INTEGER);
        assertFalse("Instruction is not blank.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank created by id.
     */
    @Test
    public void testCreatedByIdNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setCreatedById(NON_BLANK_INTEGER);
        assertTrue("Instruction is blank - created by id should be ignored.", instruction.isBlank());
    }

    /**
     * Tests the is blank with a non blank last updated by id.
     */
    @Test
    public void testLastUpdatedByIdNonBlank() {

        final PayeePaymentInstruction instruction = new PayeePaymentInstruction();
        instruction.setLastUpdatedById(NON_BLANK_INTEGER);
        assertTrue("Instruction is blank - last updated by id should be ignored.", instruction
                .isBlank());
    }

    /**
     * Creates a suite of Junit 4 tests.
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestPayeePaymentInstructionIsBlank.class);
    }
}
