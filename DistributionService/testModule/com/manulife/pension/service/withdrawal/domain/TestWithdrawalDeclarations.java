package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.withdrawal.common.WithdrawalDeclarationPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the mandatory validations for the Declarations.
 * 
 * @author dickand
 */
public class TestWithdrawalDeclarations {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final Collection<Declaration> declarations = new ArrayList<Declaration>();
        declarations.add(new WithdrawalRequestDeclaration() {
            {
                this.setTypeCode(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
            }
        });
        declarations.add(new WithdrawalRequestDeclaration() {
            {
                this.setTypeCode(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE);
            }
        });
        declarations.add(new WithdrawalRequestDeclaration() {
            {
                this.setTypeCode(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
            }
        });
        request.setDeclarations(declarations);

        return request;
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testTaxNoticeDeclarationSetWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testTaxNoticeDeclarationNotSetWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE)));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_ERROR).append("].")
                        .toString(), CollectionUtils.exists(request.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.TAX_NOTICE_DECLARATION)));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testTaxNoticeDeclarationSetWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testTaxNoticeDeclarationNotSetWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE)));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.TAX_NOTICE_DECLARATION)));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testWaitingPeriodDeclarationSetWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testWaitingPeriodDeclarationNotSetWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE)));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.WAITING_PERIOD_WAIVED_DECLARATION)));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testWaitingPeriodDeclarationSetWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testWaitingPeriodDeclarationNotSetWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE)));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.WAITING_PERIOD_WAIVED_DECLARATION)));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationSetWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithBlankWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithNullWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithNeitherWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithWmsiWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be a error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_DECLARATION)));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithPenchecksWhenMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_ERROR_MESSAGES);

        // Should be a error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_DECLARATION)));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationSetWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithBlankWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(StringUtils.EMPTY);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithNullWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithNeitherWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithWmsiWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_DECLARATION)));
    }

    /**
     * Tests the validations for the declarations.
     */
    @Test
    public void testIraProviderDeclarationNotSetWithPenchecksWhenNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDeclarations(CollectionUtils.selectRejected(request.getDeclarations(),
                new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)));
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform declaration validation
        withdrawal.validateDeclarations(Withdrawal.DECLARATION_WARNING_MESSAGES);

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));

        // Should be a warning
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_DECLARATION)));
    }
}
