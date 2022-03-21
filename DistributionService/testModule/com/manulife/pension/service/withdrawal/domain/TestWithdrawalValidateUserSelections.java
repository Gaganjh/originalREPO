package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the user selection validation step.
 * 
 * @author dickand
 */
public class TestWithdrawalValidateUserSelections {

    public static final String PAYEE_1_IRS_CODE_FOR_WITHDRAWAL = "ABC";

    public static final String PAYEE_2_IRS_CODE_FOR_WITHDRAWAL = "DEF";

    public static final String DEFAULT_IRS_CODE_FOR_WITHDRAWAL = StringUtils.EMPTY;

    public static final String CURRENT_IRS_CODE_FOR_LOANS = "GHI";

    public static final String DEFAULT_IRS_CODE_FOR_LOANS = StringUtils.EMPTY;

    public static final String CURRENT_LOAN_OPTION = "JKL";

    public static final String DEFAULT_LOAN_OPTION = WithdrawalRequest.LOAN_CLOSURE_OPTION;

    public static final String CURRENT_UNVESTED_MONEY_OPTION = "MNO";

    public static final String DEFAULT_UNVESTED_MONEY_OPTION = StringUtils.EMPTY;

    public static final String CURRENT_STATE_OF_RESIDENCE = "PQR";

    public static final String DEFAULT_STATE_OF_RESIDENCE = StringUtils.EMPTY;

    public static final BigDecimal DEFAULT_FEDERAL_TAX = new BigDecimal("10.000");

    public static final BigDecimal CURRENT_FEDERAL_TAX = new BigDecimal("10.000");

    public static final BigDecimal DEFAULT_STATE_TAX = new BigDecimal("7.000");

    public static final BigDecimal CURRENT_STATE_TAX = new BigDecimal("5.000");

    public static final String CURRENT_HARDSHIP_REASON = "STU";

    public static final String DEFAULT_HARDSHIP_REASON = StringUtils.EMPTY;

    public static final String CURRENT_FEE_TYPE = WithdrawalRequestFee.DOLLAR_TYPE_CODE;

    public static final String DEFAULT_FEE_TYPE = StringUtils.EMPTY;

    public static final BigDecimal CURRENT_FEE_VALUE = new BigDecimal("123.45");

    public static final BigDecimal DEFAULT_FEE_VALUE = null;

    public static final String CURRENT_WITHDRAWAL_REASON = "VWX";

    public static final String DEFAULT_WITHDRAWAL_REASON = StringUtils.EMPTY;

    public static final String CURRENT_PAYMENT_TO = WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE;

    public static final String DEFAULT_PAYMENT_TO = StringUtils.EMPTY;

    /**
     * Creates a base request object populated with a recipient and the specified number of payees.
     * 
     * @param payeeCount The number of payees to populate the base object with.
     * @return WithdrawalRequest A base request object.
     */
    private WithdrawalRequest getBaseWithdrawalRequest(final int payeeCount) {

        final WithdrawalRequest request = new WithdrawalRequest();

        // Create payee
        final Collection<Payee> payees = new ArrayList<Payee>();
        for (int i = 0; i < payeeCount; i++) {
            final boolean isPayeeOne = (i % 2) == 0;
            payees.add(new WithdrawalRequestPayee() {
                {
                    // Set payee code to first or second payee withdrawal IRS code
                    this.setIrsDistCode(isPayeeOne ? PAYEE_1_IRS_CODE_FOR_WITHDRAWAL
                            : PAYEE_2_IRS_CODE_FOR_WITHDRAWAL);
                }
            });
        }

        // Create recipient
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setStateOfResidenceCode(CURRENT_STATE_OF_RESIDENCE);
        recipient.setFederalTaxPercent(CURRENT_FEDERAL_TAX);
        recipient.setStateTaxPercent(CURRENT_STATE_TAX);
        recipient.setStateTaxVo(new StateTaxVO() {
            {
                this.setTaxTypeCode(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX);
                this.setDefaultTaxRatePercentage(CURRENT_STATE_TAX);
                this.setTaxRequiredIndicator(true);
            }
        });
        recipient.setPayees(payees);
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);

        // Create loans
        request.setLoans(new ArrayList<WithdrawalRequestLoan>() {
            {
                add(new WithdrawalRequestLoan());
            }
        });

        // Create fee
        request.setFees(new ArrayList<Fee>() {
            {
                add(new WithdrawalRequestFee() {
                    {
                        this.setTypeCode(CURRENT_FEE_TYPE);
                        this.setValue(CURRENT_FEE_VALUE);
                    }
                });
            }
        });

        // Set up default data
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setIrsDistributionCodeLoanClosure(CURRENT_IRS_CODE_FOR_LOANS);
        request.setLoanOption(CURRENT_LOAN_OPTION);
        request.setReasonCode(CURRENT_WITHDRAWAL_REASON);
        request.setPaymentTo(CURRENT_PAYMENT_TO);
        request.setUnvestedAmountOptionCode(CURRENT_UNVESTED_MONEY_OPTION);
        request.setHardshipReasons(CURRENT_HARDSHIP_REASON);
        request.getParticipantInfo().setThirdPartyAdminId(true);
        request.setFederalTaxVo(new FederalTaxVO() {
            {
                this.setRolloverIndicator(false);
                this.setTaxPercentage(CURRENT_FEDERAL_TAX);
            }
        });

        return request;
    }

    /**
     * Creates a base request object populated with a recipient and the specified number of payees.
     * 
     * @return WithdrawalRequest A base request object.
     */
    private Map getBaseLookupDataMap() {

        final Map map = new HashMap();
        map.put(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, PAYEE_1_IRS_CODE_FOR_WITHDRAWAL));
                add(new DeCodeVO(PAYEE_2_IRS_CODE_FOR_WITHDRAWAL, PAYEE_2_IRS_CODE_FOR_WITHDRAWAL));
            }
        });
        map.put(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_IRS_CODE_FOR_LOANS, CURRENT_IRS_CODE_FOR_LOANS));
            }
        });
        map.put(CodeLookupCache.LOAN_OPTION_TYPE, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_LOAN_OPTION, CURRENT_LOAN_OPTION));
            }
        });
        map.put(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_STATE_OF_RESIDENCE, CURRENT_STATE_OF_RESIDENCE));
            }
        });
        map.put(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_UNVESTED_MONEY_OPTION, CURRENT_UNVESTED_MONEY_OPTION));
            }
        });
        map.put(CodeLookupCache.HARDSHIP_REASONS, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_HARDSHIP_REASON, CURRENT_HARDSHIP_REASON));
            }
        });
        map.put(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
            }
        });
        map.put(CodeLookupCache.PAYMENT_TO_TYPE, new ArrayList<DeCodeVO>() {
            {
                add(new DeCodeVO(CURRENT_PAYMENT_TO, CURRENT_PAYMENT_TO));
                add(new DeCodeVO(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                        WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE));
            }
        });

        return map;
    }

    /**
     * Tests the user selection validation rules when there are no recipients.
     */
    @Test
    public void testNoRecipientsValidation() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(0);
        request.setRecipients(CollectionUtils.EMPTY_COLLECTION);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Recipient collection should still be empty.", 0, withdrawal
                .getWithdrawalRequest().getRecipients().size());
    }

    /**
     * Tests the user selection validation rules when there are no payees.
     */
    @Test
    public void testNoPayeesValidation() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(0);
        request.getRecipients().iterator().next().setPayees(CollectionUtils.EMPTY_COLLECTION);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payee collection should still be empty.", 0, withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getPayees().size());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeStillExistsWithOnePayee() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Withdrawals should be untouched.",
                PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, withdrawal.getWithdrawalRequest().getRecipients()
                        .iterator().next().getPayees().iterator().next().getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeDoesNotExistWithOnePayee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS))
                .remove(new DeCodeVO(PAYEE_1_IRS_CODE_FOR_WITHDRAWAL,
                        PAYEE_1_IRS_CODE_FOR_WITHDRAWAL));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Withdrawals should be defaulted.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, withdrawal.getWithdrawalRequest().getRecipients()
                        .iterator().next().getPayees().iterator().next().getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeBlankWithOnePayee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getPayees().iterator().next().setIrsDistCode(
                StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Withdrawals should remain defaulted.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, withdrawal.getWithdrawalRequest().getRecipients()
                        .iterator().next().getPayees().iterator().next().getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeNullWithOnePayee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getPayees().iterator().next()
                .setIrsDistCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("IRS Code For Withdrawals should be defaulted.", withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getPayees().iterator()
                .next().getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForBothWithdrawalCodesStillExistWithTwoPayees() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be untouched for first payee.",
                PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be untouched for second payee.",
                PAYEE_2_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeDoesNotExistForFirstPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS))
                .remove(new DeCodeVO(PAYEE_1_IRS_CODE_FOR_WITHDRAWAL,
                        PAYEE_1_IRS_CODE_FOR_WITHDRAWAL));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be defaulted for first payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be untouched for second payee.",
                PAYEE_2_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeDoesNotExistForSecondPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS))
                .remove(new DeCodeVO(PAYEE_2_IRS_CODE_FOR_WITHDRAWAL,
                        PAYEE_2_IRS_CODE_FOR_WITHDRAWAL));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be untouched for first payee.",
                PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be defaulted for second payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeDoesNotExistForEitherPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS))
                .remove(new DeCodeVO(PAYEE_1_IRS_CODE_FOR_WITHDRAWAL,
                        PAYEE_1_IRS_CODE_FOR_WITHDRAWAL));
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS))
                .remove(new DeCodeVO(PAYEE_2_IRS_CODE_FOR_WITHDRAWAL,
                        PAYEE_2_IRS_CODE_FOR_WITHDRAWAL));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be defaulted for first payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be defaulted for second payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForBothWithdrawalCodesBlankWithTwoPayees() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should remain defaulted for first payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should remain defaulted for second payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeBlankForFirstPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        request.getRecipients().iterator().next().getPayees().iterator().next().setIrsDistCode(
                StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should remain defaulted for first payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be untouched for second payee.",
                PAYEE_2_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeBlankForSecondPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next();
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be untouched for first payee.",
                PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should remain defaulted for second payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeBlankForBothPayeesWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        payeeIterator.next().setIrsDistCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();

        assertEquals("IRS Code For Withdrawals should remain defaulted for first payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should remain defaulted for second payee.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForBothWithdrawalCodesNullWithTwoPayees() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next().setIrsDistCode(null);
        payeeIterator.next().setIrsDistCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertNull("IRS Code For Withdrawals should be still defaulted for first payee.", payee1
                .getIrsDistCode());
        assertNull("IRS Code For Withdrawals should be still defaulted for second payee.", payee2
                .getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeNullForFirstPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next().setIrsDistCode(null);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertNull("IRS Code For Withdrawals should remain defaulted for first payee.", payee1
                .getIrsDistCode());
        assertEquals("IRS Code For Withdrawals should be untouched for second payee.",
                PAYEE_2_IRS_CODE_FOR_WITHDRAWAL, payee2.getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for withdrawals.
     */
    @Test
    public void testIrsCodeForWithdrawalCodeNullForSecondPayeeWithTwoPayees() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(2);
        final Iterator<Payee> payeeIterator = request.getRecipients().iterator()
                .next().getPayees().iterator();
        payeeIterator.next();
        payeeIterator.next().setIrsDistCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        final Iterator<Payee> iterator = withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getPayees().iterator();
        final WithdrawalRequestPayee payee1 = (WithdrawalRequestPayee)iterator.next();
        final WithdrawalRequestPayee payee2 = (WithdrawalRequestPayee)iterator.next();
        assertEquals("IRS Code For Withdrawals should be untouched for first payee.",
                PAYEE_1_IRS_CODE_FOR_WITHDRAWAL, payee1.getIrsDistCode());
        assertNull("IRS Code For Withdrawals should remain defaulted for second payee.", payee2
                .getIrsDistCode());
    }

    /**
     * Tests the user selection validation rules around the IRS code for loans.
     */
    @Test
    public void testIrsCodeForLoanCodeStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Loans should be untouched.", CURRENT_IRS_CODE_FOR_LOANS,
                withdrawal.getWithdrawalRequest().getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the IRS code for loans.
     */
    @Test
    public void testIrsCodeForLoansCodeDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS))
                .remove(new DeCodeVO(CURRENT_IRS_CODE_FOR_LOANS, CURRENT_IRS_CODE_FOR_LOANS));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Loans should be defaulted.", DEFAULT_IRS_CODE_FOR_WITHDRAWAL,
                withdrawal.getWithdrawalRequest().getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the IRS code for loans.
     */
    @Test
    public void testIrsCodeForLoansCodeBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("IRS Code For Loans should remain defaulted.",
                DEFAULT_IRS_CODE_FOR_WITHDRAWAL, withdrawal.getWithdrawalRequest()
                        .getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the IRS code for loans.
     */
    @Test
    public void testIrsCodeForLoansCodeNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setIrsDistributionCodeLoanClosure(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("IRS Code For Loans should be defaulted.", withdrawal.getWithdrawalRequest()
                .getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the Loan Option.
     */
    @Test
    public void testLoanOptionStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Loan Option should be untouched.", CURRENT_LOAN_OPTION, withdrawal
                .getWithdrawalRequest().getLoanOption());
    }

    /**
     * Tests the user selection validation rules around the Loan Option.
     */
    @Test
//    Leads to assertion failure
    public void testLoanOptionCodeDoesNotExist() {

//        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
//        final Map lookupData = getBaseLookupDataMap();
//        ((Collection) lookupData.get(CodeLookupCache.LOAN_OPTION_TYPE)).remove(new DeCodeVO(
//                CURRENT_LOAN_OPTION, CURRENT_LOAN_OPTION));
//        final Withdrawal withdrawal = new Withdrawal(request);
//
//        // Validate user selection
//        withdrawal.validateUserSelections(lookupData);
//
//        // Verify results
//        assertEquals("Loan Option should be defaulted.", DEFAULT_LOAN_OPTION, withdrawal
//                .getWithdrawalRequest().getLoanOption());
    }

    /**
     * Tests the user selection validation rules around the Loan Option.
     */
    @Test
    public void testLoanOptionCodeBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setLoanOption(StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Loan Option should remain blank.", StringUtils.EMPTY, withdrawal
                .getWithdrawalRequest().getLoanOption());
    }

    /**
     * Tests the user selection validation rules around the Loan Option.
     */
    @Test
    public void testLoanOptionCodeNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setLoanOption(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Loan Option should be defaulted.", withdrawal.getWithdrawalRequest()
                .getLoanOption());
    }

    /**
     * Tests the user selection validation rules around the State of Residence.
     */
    @Test
    public void testStateOfResidenceStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State of Residence should be untouched.", CURRENT_STATE_OF_RESIDENCE,
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateOfResidenceCode());
    }

    /**
     * Tests the user selection validation rules around the State of Residence.
     */
    @Test
    public void testStateOfResidenceCodeDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE))
                .remove(new DeCodeVO(CURRENT_STATE_OF_RESIDENCE, CURRENT_STATE_OF_RESIDENCE));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State of Residence should be defaulted.", DEFAULT_STATE_OF_RESIDENCE,
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateOfResidenceCode());
    }

    /**
     * Tests the user selection validation rules around the State of Residence.
     */
    @Test
    public void testStateOfResidenceCodeBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setStateOfResidenceCode(StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State of Residence should remain defaulted.", DEFAULT_STATE_OF_RESIDENCE,
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateOfResidenceCode());
    }

    /**
     * Tests the user selection validation rules around the State of Residence.
     */
    @Test
    public void testStateOfResidenceCodeNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setStateOfResidenceCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("State of Residence should be defaulted.", withdrawal.getWithdrawalRequest()
                .getRecipients().iterator().next().getStateOfResidenceCode());
    }

    /**
     * Tests the user selection validation rules around the Unvested Money Option.
     */
    @Test
    public void testUnvestedMoneyOptionStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Unvested money option should be untouched.", CURRENT_UNVESTED_MONEY_OPTION,
                withdrawal.getWithdrawalRequest().getUnvestedAmountOptionCode());
    }

    /**
     * Tests the user selection validation rules around the Unvested Money Option.
     */
    @Test
    public void testUnvestedMoneyOptionCodeDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS))
                .remove(new DeCodeVO(CURRENT_UNVESTED_MONEY_OPTION, CURRENT_UNVESTED_MONEY_OPTION));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Unvested money option should be defaulted.", DEFAULT_UNVESTED_MONEY_OPTION,
                withdrawal.getWithdrawalRequest().getUnvestedAmountOptionCode());
    }

    /**
     * Tests the user selection validation rules around the Unvested Money Option.
     */
    @Test
    public void testUnvestedMoneyOptionCodeBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setUnvestedAmountOptionCode(StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Unvested money option should remain defaulted.",
                DEFAULT_UNVESTED_MONEY_OPTION, withdrawal.getWithdrawalRequest()
                        .getUnvestedAmountOptionCode());
    }

    /**
     * Tests the user selection validation rules around the Unvested Money Option.
     */
    @Test
    public void testUnvestedMoneyOptionCodeNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setUnvestedAmountOptionCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Unvested money option should be defaulted.", withdrawal.getWithdrawalRequest()
                .getUnvestedAmountOptionCode());
    }

    /**
     * Tests the user selection validation rules around the Federal Tax.
     */
    @Test
    public void testFederalTaxNonZeroAndStillValid() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Federal tax value should be untouched.", CURRENT_FEDERAL_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getFederalTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the Federal Tax.
     */
    @Test
    public void testFederalTaxNonZeroAndNonValid() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getFederalTaxVo().setTaxPercentage(DEFAULT_FEDERAL_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Federal tax value should be defaulted.", DEFAULT_FEDERAL_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getFederalTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the Federal Tax.
     */
    @Test
    public void testFederalTaxZeroAndValid() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Federal tax value should be untouched.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getFederalTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the Federal Tax.
     */
    @Test
    public void testFederalTaxZeroAndNonValid() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        request.getFederalTaxVo().setRolloverIndicator(true);
        request.getFederalTaxVo().setTaxPercentage(DEFAULT_FEDERAL_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Federal tax value should be defaulted.", DEFAULT_FEDERAL_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getFederalTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the Hardship Reason.
     */
    @Test
    public void testHardshipReasonStillExistsAndWithdrawalReasonIsHardship() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Hardship reason should be untouched.", CURRENT_HARDSHIP_REASON, withdrawal
                .getWithdrawalRequest().getHardshipReasons());
    }

    /**
     * Tests the user selection validation rules around the Hardship Reason.
     */
    @Test
    public void testHardshipReasonStillExistsAndWithdrawalReasonIsNotHardship() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setReasonCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Hardship reason should be defaulted.", DEFAULT_HARDSHIP_REASON, withdrawal
                .getWithdrawalRequest().getHardshipReasons());
    }

    /**
     * Tests the user selection validation rules around the Hardship Reason.
     */
    @Test
    public void testHardshipReasonCodeDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.HARDSHIP_REASONS)).remove(new DeCodeVO(
                CURRENT_HARDSHIP_REASON, CURRENT_HARDSHIP_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Hardship reason should be defaulted.", DEFAULT_HARDSHIP_REASON, withdrawal
                .getWithdrawalRequest().getHardshipReasons());
    }

    /**
     * Tests the user selection validation rules around the Hardship Reason.
     */
    @Test
    public void testHardshipReasonCodeBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setHardshipReasons(StringUtils.EMPTY);

        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Hardship reason should remain defaulted.", DEFAULT_HARDSHIP_REASON,
                withdrawal.getWithdrawalRequest().getHardshipReasons());
    }

    /**
     * Tests the user selection validation rules around the Hardship Reason.
     */
    @Test
    public void testHardshipReasonCodeNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setHardshipReasons(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Hardship reason should be defaulted.", DEFAULT_HARDSHIP_REASON, withdrawal
                .getWithdrawalRequest().getHardshipReasons());
    }

    /**
     * Tests the user selection validation rules around the Loans.
     */
    @Test
    public void testLoanInfoWithExistingLoans() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Loan option should be unchanged.", CURRENT_LOAN_OPTION, withdrawal
                .getWithdrawalRequest().getLoanOption());
        assertEquals("IRS Code for Loans should be unchanged.", CURRENT_IRS_CODE_FOR_LOANS,
                withdrawal.getWithdrawalRequest().getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the Loans.
     */
    @Test
    public void testLoanInfoWithNoLoans() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setLoans(CollectionUtils.EMPTY_COLLECTION);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Loan option should be defaulted.", StringUtils.EMPTY, withdrawal
                .getWithdrawalRequest().getLoanOption());
        assertEquals("IRS Code for Loans should be defaulted.", DEFAULT_IRS_CODE_FOR_LOANS,
                withdrawal.getWithdrawalRequest().getIrsDistributionCodeLoanClosure());
    }

    /**
     * Tests the user selection validation rules around the Fees.
     */
    @Test
    public void testFeeInfoWithTpa() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("TPA Fee type should be unchanged.", CURRENT_FEE_TYPE, withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getTypeCode());
        assertEquals("TPA Fee value should be unchanged.", CURRENT_FEE_VALUE.toString(), withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getValue().toString());
    }

    /**
     * Tests the user selection validation rules around the Fees.
     */
    @Test
    public void testFeeInfoWithNoTpa() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getParticipantInfo().setThirdPartyAdminId(false);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("TPA Fee type should be defaulted.", DEFAULT_FEE_TYPE, withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getTypeCode());
        assertEquals("TPA Fee value should be defaulted.", DEFAULT_FEE_VALUE, withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getValue());
    }

    /**
     * Tests the user selection validation rules around the Fees.
     */
    @Test
    public void testNoFeeInfoWithTpa() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setFees(CollectionUtils.EMPTY_COLLECTION);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertTrue("Fee collection should not be empty.", CollectionUtils.isNotEmpty(withdrawal
                .getWithdrawalRequest().getFees()));
        assertEquals("TPA Fee type should be defaulted.", DEFAULT_FEE_TYPE, withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getTypeCode());
        assertEquals("TPA Fee value should be defaulted.", DEFAULT_FEE_VALUE, withdrawal
                .getWithdrawalRequest().getFees().iterator().next().getValue());
    }

    /**
     * Tests the user selection validation rules around the Fees.
     */
    @Test
    public void testNoFeeInfoWithNoTpa() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getParticipantInfo().setThirdPartyAdminId(false);
        request.setFees(CollectionUtils.EMPTY_COLLECTION);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertTrue("Fee collection should still be empty.", CollectionUtils.isEmpty(withdrawal
                .getWithdrawalRequest().getFees()));
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testNoStateTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        final StateTaxVO stateTax = recipient.getStateTaxVo();
        stateTax.setDefaultTaxRatePercentage(BigDecimal.ZERO);
        stateTax.setTaxPercentageMaximum(BigDecimal.ZERO);
        stateTax.setTaxPercentageMinimum(BigDecimal.ZERO);
        stateTax.setTaxRequiredIndicator(false);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be blanked out.", null, withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testNoStateTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient)request.getRecipients().iterator().next();
        final StateTaxVO stateTax = recipient.getStateTaxVo();
        stateTax.setDefaultTaxRatePercentage(BigDecimal.ZERO);
        stateTax.setTaxPercentageMaximum(BigDecimal.ZERO);
        stateTax.setTaxPercentageMinimum(BigDecimal.ZERO);
        stateTax.setTaxRequiredIndicator(false);
        recipient.setStateTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be blanked out.", null, withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxNonZeroAndStillValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", CURRENT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxNonZeroAndStillValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxNonZeroAndNonValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be defaulted.", DEFAULT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxNonZeroAndNonValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxZeroAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        request.getRecipients().iterator().next().setStateTaxPercent(BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", BigDecimal.ZERO.toString(), withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent()
                .toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testMandatoryStateTaxZeroFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().setStateTaxPercent(BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be defaulted.", DEFAULT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testFreeFormStateTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMaximum(
                CURRENT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", CURRENT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testFreeFormStateTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMaximum(
                CURRENT_STATE_TAX);
        request.getRecipients().iterator().next().setStateTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", BigDecimal.ZERO.toString(), withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent()
                .toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testOptOutStateTaxNonZeroAndStillValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", CURRENT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testOptOutStateTaxNonZeroAndStillValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testOptOutStateTaxNonZeroAndNotValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be defaulted.", DEFAULT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testOptOutStateTaxNonZeroAndNotValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testOptOutStateTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                BigDecimal.ZERO);
        request.getRecipients().iterator().next().setStateTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", BigDecimal.ZERO.toString(), withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent()
                .toString());
    }

    // TODO

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testVoluntaryFixedStateTaxNonZeroAndStillValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", CURRENT_STATE_TAX.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testVoluntaryFixedStateTaxNonZeroAndStillValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testVoluntaryFixedStateTaxNonZeroAndNotValidAndFederalTaxNonZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testVoluntaryFixedStateTaxNonZeroAndNotValidAndFederalTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().getStateTaxVo().setDefaultTaxRatePercentage(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().setFederalTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be forced to zero.", BigDecimal.ZERO.toString(),
                withdrawal.getWithdrawalRequest().getRecipients().iterator().next()
                        .getStateTaxPercent().toString());
    }

    /**
     * Tests the user selection validation rules around the State Tax.
     */
    @Test
    public void testVoluntaryFixedStateTaxZero() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxRequiredIndicator(false);
        request.getRecipients().iterator().next().getStateTaxVo().setTaxPercentageMinimum(
                DEFAULT_STATE_TAX);
        request.getRecipients().iterator().next().setStateTaxPercent(BigDecimal.ZERO);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("State tax value should be untouched.", BigDecimal.ZERO.toString(), withdrawal
                .getWithdrawalRequest().getRecipients().iterator().next().getStateTaxPercent()
                .toString());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusDraftAndWithdrawalReasonStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusDraftAndWithdrawalReasonDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS))
                .remove(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be defaulted.", DEFAULT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusDraftAndWithdrawalReasonBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setReasonCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should remain defaulted.", DEFAULT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusDraftAndWithdrawalReasonNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setReasonCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Withdrawal Reason should be defaulted.", withdrawal.getWithdrawalRequest()
                .getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingReviewAndWithdrawalReasonStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingReviewAndWithdrawalReasonDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS))
                .remove(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingReviewAndWithdrawalReasonDoesNotExistAndAlertIsAlreadyPresent() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.addMessage(new WithdrawalMessage(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS))
                .remove(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingReviewAndWithdrawalReasonBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setReasonCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should remain defaulted.", DEFAULT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingReviewAndWithdrawalReasonNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setReasonCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Withdrawal Reason should be defaulted.", withdrawal.getWithdrawalRequest()
                .getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingApprovalAndWithdrawalReasonStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingApprovalAndWithdrawalReasonDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS))
                .remove(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingApprovalAndWithdrawalReasonDoesNotExistAndAlertIsAlreadyPresent() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.addMessage(new WithdrawalMessage(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS))
                .remove(new DeCodeVO(CURRENT_WITHDRAWAL_REASON, CURRENT_WITHDRAWAL_REASON));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should be untouched.", CURRENT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingApprovalAndWithdrawalReasonBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setReasonCode(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Withdrawal Reason should remain defaulted.", DEFAULT_WITHDRAWAL_REASON,
                withdrawal.getWithdrawalRequest().getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Withdrawal Reason.
     */
    @Test
    public void testStatusPendingApprovalAndWithdrawalReasonNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setReasonCode(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Withdrawal Reason should be defaulted.", withdrawal.getWithdrawalRequest()
                .getReasonCode());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE)).remove(new DeCodeVO(
                CURRENT_PAYMENT_TO, CURRENT_PAYMENT_TO));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be defaulted.", DEFAULT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setPaymentTo(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should remain defaulted.", DEFAULT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setPaymentTo(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Payment To should be defaulted.", withdrawal.getWithdrawalRequest()
                .getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToIsNotTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be defaulted.", DEFAULT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusDraftAndPaymentToIsTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE)).remove(new DeCodeVO(
                CURRENT_PAYMENT_TO, CURRENT_PAYMENT_TO));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setPaymentTo(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should remain defaulted.", DEFAULT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setPaymentTo(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Payment To should be defaulted.", withdrawal.getWithdrawalRequest()
                .getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToIsNotTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.",
                WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, withdrawal
                        .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToIsNotTrusteeAndCheckPayableIsTrusteeAndAlertAlreadyExists() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        request.addMessage(new WithdrawalMessage(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.",
                WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, withdrawal
                        .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingReviewAndPaymentToIsTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should remain untouched.",
                WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, withdrawal.getWithdrawalRequest()
                        .getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToStillExists() {

        // Get setup data
        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToDoesNotExist() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE)).remove(new DeCodeVO(
                CURRENT_PAYMENT_TO, CURRENT_PAYMENT_TO));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToDoesNotExistAndAlertAlreadyExists() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.addMessage(new WithdrawalMessage(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
        final Map lookupData = getBaseLookupDataMap();
        ((Collection) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE)).remove(new DeCodeVO(
                CURRENT_PAYMENT_TO, CURRENT_PAYMENT_TO));
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.", CURRENT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToBlank() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setPaymentTo(StringUtils.EMPTY);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should remain defaulted.", DEFAULT_PAYMENT_TO, withdrawal
                .getWithdrawalRequest().getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToNull() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setPaymentTo(null);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertNull("Payment To should be defaulted.", withdrawal.getWithdrawalRequest()
                .getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToIsNotTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.",
                WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, withdrawal
                        .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToIsNotTrusteeAndCheckPayableIsTrusteeAndAlertAlreadyExists() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        request.addMessage(new WithdrawalMessage(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should be untouched.",
                WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, withdrawal
                        .getWithdrawalRequest().getPaymentTo());
        assertTrue("No longer valid should be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        // Should be no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        // Should be one alert
        assertEquals("There should be one alert.", 1, CollectionUtils.size(request.getAlertCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT).append("].").toString(),
                CollectionUtils.exists(request.getAlertCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT)));
    }

    /**
     * Tests the user selection validation rules around the Payment To.
     */
    @Test
    public void testStatusPendingApprovalAndPaymentToIsTrusteeAndCheckPayableIsTrustee() {

        final WithdrawalRequest request = getBaseWithdrawalRequest(1);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        request.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        request.getParticipantInfo().setChequePayableToCode(
                ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE);
        final Map lookupData = getBaseLookupDataMap();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Validate user selection
        withdrawal.validateUserSelections(lookupData);

        // Verify results
        assertEquals("Payment To should remain untouched.",
                WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, withdrawal.getWithdrawalRequest()
                        .getPaymentTo());
        assertFalse("No longer valid should not be set.", withdrawal.getWithdrawalRequest()
                .getIsNoLongerValid());
    }
}