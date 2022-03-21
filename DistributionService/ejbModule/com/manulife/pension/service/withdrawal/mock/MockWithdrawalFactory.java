package com.manulife.pension.service.withdrawal.mock;

import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.ADMIN_TO_ADMIN_NOTE_COUNT;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.ADMIN_TO_PARTICIPANT_NOTE_COUNT;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.AMOUNT_TYPE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.CONTRACT_STATUS;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.HAS_DEFINED_BENEFITS;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.HAS_TPA;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.INITIATOR_MAY_APPROVE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.LOAN_COUNT;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.PARTICIPANT_LEAVING_PLAN;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.PARTICIPANT_STATUS;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.PAYMENT_METHOD;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.PAYMENT_TO;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.PRE_1987;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.REMOVE_APPROVE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.REMOVE_INITIATE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.REMOVE_REVIEW;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.SEND_CHEQUE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.STATE_OF_RESIDENCE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.STATUS_CODE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.USE_TWO_STEP;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.VESTING_UNAVAILABLE;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.WITHDRAWAL_REASON;
import static com.manulife.pension.service.withdrawal.mock.MockWithdrawalParameter.WMSI_PAYCHECKS;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Generates a withdrawal request based on the specified map of parameter/value pairs. The factory
 * is meant to make use of a map of parameters extracted from a web based request object to allow
 * greater control over the composition of a request object during testing and development of the
 * complex withdrawal pages.
 * 
 * @author dickand
 */
public class MockWithdrawalFactory {

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(MockWithdrawalFactory.class);

    private static final int TRUSTEE_ADDRESS = 1;

    private static final int PAYEE_1_ADDRESS = 2;

    private static final int PAYEE_2_ADDRESS = 3;

    private static final int PARTICIPANT_ADDRESS = 4;

    private static final int RECIPIENT_ADDRESS = 5;

    private static final int ADMIN_TO_PARTICIPANT_NOTE_TYPE = 1;

    private static final int ADMIN_TO_ADMIN_NOTE_TYPE = 2;

    /**
     * @param parameterMap
     * @return WithdrawalRequest - The mock withdrawal request populated with default values and
     *         override values specified in the parameter map.
     */
    public static WithdrawalRequest getMockWithdrawal(final Map objectMap) {

        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "MockWithdrawalFactory.getMockWithdrawal> Creating mock withdrawal object with parameter map [")
                            .append(objectMap).append("].").toString());
        }
        final Map<String, String> parameterMap = getTypedMap(objectMap);
        final WithdrawalRequest withdrawal = getMockWithdrawalBase(parameterMap);

        withdrawal.setRecipients(getMockRecipients(parameterMap));
        withdrawal.setMoneyTypes(getMockMoneyTypes(parameterMap));
        withdrawal.setLoans(getMockLoans(parameterMap));
        withdrawal.setFees(getMockFees(parameterMap));
        withdrawal.setDeclarations(getMockDeclarations(parameterMap));
        withdrawal
                .setReadOnlyAdminToAdminNotes(getMockNotes(parameterMap, ADMIN_TO_ADMIN_NOTE_TYPE));
        withdrawal.setReadOnlyAdminToParticipantNotes(getMockNotes(parameterMap,
                ADMIN_TO_PARTICIPANT_NOTE_TYPE));

        withdrawal.setContractInfo(getMockContractInfo(parameterMap));
        withdrawal.setParticipantInfo(getMockParticipantInfo(parameterMap));

        // Get participant and trustee addresses
        withdrawal.setTrusteeAddress(getMockWithdrawalAddress(TRUSTEE_ADDRESS));
        withdrawal.setParticipantAddress(getMockWithdrawalAddress(PARTICIPANT_ADDRESS));

        return withdrawal;
    }

    /**
     * @param parameterMap
     * @return WithdrawalRequest - The mock withdrawal request with the base parameters defined.
     */
    private static WithdrawalRequest getMockWithdrawalBase(final Map<String, String> parameterMap) {

        final WithdrawalRequest withdrawal = new WithdrawalRequest();
        withdrawal.setPaymentTo((parameterMap.containsKey(PAYMENT_TO) ? parameterMap
                .get(PAYMENT_TO) : WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE));
        withdrawal
                .setIraServiceProviderCode(parameterMap.containsKey(WMSI_PAYCHECKS) ? parameterMap
                        .get(WMSI_PAYCHECKS) : WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        withdrawal.setReasonCode(parameterMap.containsKey(WITHDRAWAL_REASON) ? parameterMap
                .get(WITHDRAWAL_REASON) : WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        withdrawal.setStatusCode(parameterMap.containsKey(STATUS_CODE) ? parameterMap
                .get(STATUS_CODE) : WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        withdrawal
                .setParticipantLeavingPlanInd(parameterMap.containsKey(PARTICIPANT_LEAVING_PLAN) ? true
                        : false);
        withdrawal
                .setParticipantStateOfResidence(parameterMap.containsKey(STATE_OF_RESIDENCE) ? parameterMap
                        .get(STATE_OF_RESIDENCE)
                        : "CA");
        withdrawal.setAmountTypeCode(parameterMap.containsKey(AMOUNT_TYPE) ? parameterMap
                .get(AMOUNT_TYPE) : WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE);
        withdrawal.setEmplStatusCode(parameterMap.containsKey(PARTICIPANT_STATUS) ? parameterMap
                .get(PARTICIPANT_STATUS) : "AC");
        withdrawal.setOriginalPaymentTo(parameterMap.containsKey(PAYMENT_METHOD) ? parameterMap
                .get(PAYMENT_TO) : null);
        withdrawal.setEmplStatusEffectiveDate(new Date(20000));
        withdrawal.setFederalTaxVo(new FederalTaxVO());
        withdrawal
                .setHardshipReasons("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin auctor mollis pede. Aliquam aliquam. Nam quis augue a libero semper bibendum.");
        withdrawal.setIrsDistributionCodeLoanClosure("1");
        withdrawal.setFirstName("Howard");
        withdrawal.setLastName("Lovecraft");
        withdrawal.setParticipantSSN("987-65-4320");
        withdrawal.setContractName("John Hancock Demo Contract");
        withdrawal.setContractId(6736473);
        withdrawal.setRequestDate(new Date());
        withdrawal.setExpirationDate(null);
        withdrawal.setTerminationDate(null);
        withdrawal.setEmployeeProfileId(4367463);
        withdrawal
                .setUnvestedAmountOptionCode(WithdrawalRequest.UNVESTED_TRANSFER_TO_CASH_ACCOUNT_CODE);
        withdrawal.setMostRecentPriorContributionDate(new Date());
        withdrawal.setFinalContributionDate(null);
        withdrawal.setWithdrawalAmount(new BigDecimal("123456789.10"));
        withdrawal.setBirthDate(new Date(100000000));
        withdrawal.setCreated(new Timestamp(0));
        withdrawal.setCreatedById(new Integer(0));

        // Set current notes
        final WithdrawalRequestNote adminToAdminNote = new WithdrawalRequestNote();
        final WithdrawalRequestNote adminToParticipantNote = new WithdrawalRequestNote();
        if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE, withdrawal
                .getStatusCode())
                && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE,
                        withdrawal.getStatusCode())) {

            adminToAdminNote.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
            adminToAdminNote
                    .setNote("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin auctor mollis pede. Aliquam aliquam. Nam quis augue a libero semper bibendum. Nulla eu dui. Ut at orci. Vestibulum congue sagittis nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Aenean ac magna vitae odio fringilla aliquam. Proin malesuada volutpat sapien. Nulla facilisi. In convallis.\nInteger sit amet nisi ut orci fringilla varius. Sed quis nulla nec ligula ultricies auctor. Quisque luctus lectus non lacus. Mauris ut augue ac ipsum fermentum semper. Aliquam ultrices neque et turpis. Phasellus bibendum. Duis convallis, sapien sit amet sollicitudin placerat, dolor mauris viverra lacus, non volutpat metus metus sed risus posuere.");
            adminToAdminNote.setCreated(new Timestamp(1234564324));
            adminToAdminNote.setCreatedById(1580521);
            adminToParticipantNote
                    .setNote("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Pellentesque at tortor. Donec auctor, enim id molestie dignissim, nisi neque pharetra ante, sit amet mattis ipsum purus et lectus. Suspendisse a urna. Donec orci dui, faucibus eu, volutpat ut, mollis quis, ante. Morbi ultricies mauris id quam. Fusce nulla erat, commodo in, feugiat id, commodo vel, metus. Donec mauris. Pellentesque et orci in nisl placerat auctor. Duis faucibus turpis duis.");
            adminToParticipantNote
                    .setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
            adminToParticipantNote.setCreated(new Timestamp(947385793));
            adminToParticipantNote.setCreatedById(1580521);

        }
        withdrawal.setCurrentAdminToAdminNote(adminToAdminNote);
        withdrawal.setCurrentAdminToParticipantNote(adminToParticipantNote);

        return withdrawal;
    }

    /**
     * @param parameterMap
     * @return WithdrawalRequest - The collection of mock recipients
     */
    private static Collection<Recipient> getMockRecipients(
            final Map<String, String> parameterMap) {

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(getMockRecipient(parameterMap));
        return recipients;
    }

    /**
     * @param parameterMap
     * @return WithdrawalRequestRecipient - The mock recipient
     */
    private static WithdrawalRequestRecipient getMockRecipient(
            final Map<String, String> parameterMap) {

        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();

        recipient.setFirstName("Irwin");
        recipient.setLastName("Fletcher");
        recipient.setFederalTaxPercent(new BigDecimal(5));
        recipient.setStateTaxPercent(new BigDecimal(10));
        recipient.setUsCitizenInd(Boolean.TRUE);
        recipient.setStateTaxVo(getMockStateTax(parameterMap));
        recipient.setAddress(getMockWithdrawalAddress(RECIPIENT_ADDRESS));
        recipient.setPayees(getMockPayees(parameterMap));

        return recipient;
    }

    /**
     * @param parameterMap
     * @return WithdrawalRequest - The collection of mock payees
     */
    private static Collection<Payee> getMockPayees(
            final Map<String, String> parameterMap) {

        final Collection<Payee> payees = new ArrayList<Payee>();

        final String paymentTo = parameterMap.containsKey(PAYMENT_TO) ? parameterMap
                .get(PAYMENT_TO) : WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE;
        payees.add(getMockPayee(parameterMap, 1));

        if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        paymentTo)) {
            payees.add(getMockPayee(parameterMap, 2));
        }
        return payees;
    }

    /**
     * @param parameterMap
     * @param payeeNumber The number of the payee to create (1 or 2)
     * @return WithdrawalRequestPayee - The mock payee
     */
    private static WithdrawalRequestPayee getMockPayee(final Map<String, String> parameterMap,
            final int payeeNumber) {

        final WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        payee.setPaymentInstruction(new PayeePaymentInstruction());
        if (payeeNumber == 1) {

            payee.setFirstName("Participant 1");
            payee.setPaymentMethodCode(

            parameterMap.containsKey(PAYMENT_METHOD) ? parameterMap.get(PAYMENT_METHOD) : null);
            payee.setReasonCode("33");
            payee.setRecipientNo(1);
            payee.setShareTypeCode("Joint");
            payee.setShareValue(new BigDecimal("12345"));
            payee.setRolloverAccountNo("12345789");
            payee.setRolloverPlanName("Roll Over Plan 1");
            payee.setIrsDistCode("AA");
            payee.setTypeCode("Termination");

            payee.getPaymentInstruction().setAttentionName("Stewie Griffin");
            payee.getPaymentInstruction().setBankAccountTypeCode(
                    WithdrawalRequestPayee.CHECKING_ACCOUNT_TYPE_CODE);
            payee.getPaymentInstruction().setBankTransitNumber(3880);
            payee.getPaymentInstruction().setCreditPartyName("Credit Party");

            payee.setAddress(getMockWithdrawalAddress(PAYEE_1_ADDRESS));

        } else {

            payee.setFirstName("Homer");
            payee.setLastName("Simpson");
            payee.setRolloverPlanName("Simpson Roll Over Plan");
            payee.setPaymentMethodCode(null);
            payee.setMailCheckToAddress(Boolean.TRUE);
            payee.setIrsDistCode("7  ");
            payee.getPaymentInstruction().setAttentionName("Brian Griffin");
            payee.getPaymentInstruction().setBankAccountTypeCode(
                    WithdrawalRequestPayee.SAVINGS_ACCOUNT_TYPE_CODE);
            payee.getPaymentInstruction().setBankTransitNumber(5454);
            payee.getPaymentInstruction().setCreditPartyName("Quahog Kennell");
            payee.setAddress(getMockWithdrawalAddress(PAYEE_2_ADDRESS));
        }

        return payee;
    }

    /**
     * @param parameterMap
     * @return StateTaxVO - The mock state tax object
     */
    private static StateTaxVO getMockStateTax(final Map<String, String> parameterMap) {

        final StateTaxVO stateTax = new StateTaxVO();

        stateTax.setDefaultTaxRatePercentage(new BigDecimal(15));
        stateTax.setTaxPercentageMinimum(new BigDecimal(1));
        stateTax.setTaxPercentageMaximum(new BigDecimal(20));
        stateTax.setTaxRequiredIndicator(true);
        stateTax.setEffectiveDate(new Date());
        stateTax.setRolloverIndicator(true);
        stateTax.setStateCode(parameterMap.containsKey(STATE_OF_RESIDENCE) ? parameterMap
                .get(STATE_OF_RESIDENCE) : "CA");
        stateTax.setTaxTypeCode(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX);

        return stateTax;
    }

    /**
     * @param addressType
     * @return Address - The mock address of the type specified.
     */
    private static Address getMockWithdrawalAddress(final int addressType) {

        final Address address = new Address();
        switch (addressType) {
            case TRUSTEE_ADDRESS:
                address.setAddressLine1("Trustee Address Line 1");
                address.setAddressLine2("Trustee Address Line 2");
                address.setCity("Trustee City");
                address.setCountryCode("USA");
                address.setStateCode("FL");
                address.setZipCode("98765");

                break;
            case PARTICIPANT_ADDRESS:
                address.setAddressLine1("Participant Address Line 1");
                address.setAddressLine2("Participant Address Line 2");
                address.setCity("Participant City");
                address.setCountryCode("USA");
                address.setStateCode("AL");
                address.setZipCode("123456789");

                break;
            case PAYEE_1_ADDRESS:

                address.setAddressLine1("31 Spooner Street");
                address.setAddressLine2("");
                address.setCity("Quahog");
                address.setCountryCode("USA");
                address.setStateCode("RI");
                address.setZipCode("76843");
                break;
            case PAYEE_2_ADDRESS:

                address.setAddressLine1("8 Country Club Dr");
                address.setAddressLine2("Suite # 12");
                address.setCity("Detroit");
                address.setCountryCode("USA");
                address.setStateCode("MI");
                address.setZipCode("48303");
                break;
            case RECIPIENT_ADDRESS:
                address.setAddressLine1("1313 Mockingbird Lane");
                address.setAddressLine2("Penthouse # 13");
                address.setCity("Beverly Hills");
                address.setCountryCode("USA");
                address.setStateCode("CA");
                address.setZipCode("902101234");

                break;
            default:
                // Should not be called - leave blank to signify no match in type
        }

        return address;
    }

    /**
     * @param parameterMap
     * @return Collection<WithdrawalRequestMoneyType> - The collection of mock money types
     */
    private static Collection<WithdrawalRequestMoneyType> getMockMoneyTypes(
            final Map<String, String> parameterMap) {

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();

        // Create money type 1
        WithdrawalRequestMoneyType moneyType1 = new WithdrawalRequestMoneyType();
        moneyType1.setMoneyTypeName("Quahog Fund");
        moneyType1.setTotalBalance(new BigDecimal("123456789.10"));
        moneyType1.setIsPre1987MoneyType(parameterMap.containsKey(PRE_1987));
        moneyType1.setVestingPercentageUpdateable(true);
        if (parameterMap.containsKey(VESTING_UNAVAILABLE)) {
            moneyType1.setVestingPercentage(null);
        } else {
            moneyType1.setVestingPercentage(new BigDecimal("55.555"));
            moneyType1.setAvailableWithdrawalAmount(new BigDecimal("68586419.18"));
            moneyType1.setWithdrawalAmount(new BigDecimal("34293209.59"));
            moneyType1.setWithdrawalPercentage(new BigDecimal("50"));
        }

        // Create money type 2
        WithdrawalRequestMoneyType moneyType2 = new WithdrawalRequestMoneyType();
        moneyType2.setMoneyTypeName("Springfield Fund");
        moneyType2.setTotalBalance(new BigDecimal("10000000.00"));
        moneyType2.setIsPre1987MoneyType(parameterMap.containsKey(PRE_1987));
        moneyType2.setVestingPercentageUpdateable(true);
        if (parameterMap.containsKey(VESTING_UNAVAILABLE)) {
            moneyType2.setVestingPercentage(null);
        } else {
            moneyType2.setVestingPercentage(new BigDecimal("10.000"));
            moneyType2.setAvailableWithdrawalAmount(new BigDecimal("1000000.00"));
            moneyType2.setWithdrawalAmount(new BigDecimal("100000.00"));
            moneyType2.setWithdrawalPercentage(new BigDecimal("10.00"));
        }

        // Create money type 3
        WithdrawalRequestMoneyType moneyType3 = new WithdrawalRequestMoneyType();
        moneyType3.setMoneyTypeName("Red Hook Fund");
        moneyType3.setTotalBalance(new BigDecimal("1"));
        moneyType3.setIsPre1987MoneyType(parameterMap.containsKey(PRE_1987));
        moneyType3.setVestingPercentageUpdateable(false);
        if (parameterMap.containsKey(VESTING_UNAVAILABLE)) {
            moneyType3.setVestingPercentage(null);
        } else {
            moneyType3.setVestingPercentage(new BigDecimal("80.00"));
            moneyType3.setAvailableWithdrawalAmount(new BigDecimal("0.80"));
            moneyType3.setWithdrawalAmount(new BigDecimal("0.20"));
            moneyType3.setWithdrawalPercentage(new BigDecimal("25"));
        }

        moneyTypes.add(moneyType1);
        moneyTypes.add(moneyType2);
        moneyTypes.add(moneyType3);

        return moneyTypes;
    }

    /**
     * @param parameterMap
     * @return Collection<WithdrawalRequestLoan> - The collection of mock declaration
     */
    private static Collection<WithdrawalRequestLoan> getMockLoans(
            final Map<String, String> parameterMap) {

        final Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();
        final int loanCount = parameterMap.containsKey(LOAN_COUNT) ? Integer.valueOf(parameterMap
                .get(LOAN_COUNT)) : 4;
        for (int i = 0; i < loanCount; i++) {
            final WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
            loan.setLoanNo(new Integer(12345) * i);
            loan.setOutstandingLoanAmount(new BigDecimal(String.valueOf(123456 * i)));
        }

        return loans;

    }

    /**
     * @param parameterMap
     * @return Collection<WithdrawalRequestNote> - The collection of mock note
     */
    private static Collection<WithdrawalRequestNote> getMockNotes(
            final Map<String, String> parameterMap, final int noteType) {

        final String status = parameterMap.containsKey(STATUS_CODE) ? parameterMap.get(STATUS_CODE)
                : WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE;
        final Collection<WithdrawalRequestNote> notes = new ArrayList<WithdrawalRequestNote>();
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE, status)
                || StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE,
                        status)) {

            int noteCount;
            if (noteType == ADMIN_TO_ADMIN_NOTE_TYPE) {
                noteCount = parameterMap.containsKey(ADMIN_TO_ADMIN_NOTE_COUNT) ? Integer
                        .valueOf(parameterMap.get(ADMIN_TO_ADMIN_NOTE_COUNT)) : 4;
            } else {
                noteCount = parameterMap.containsKey(ADMIN_TO_PARTICIPANT_NOTE_COUNT) ? Integer
                        .valueOf(parameterMap.get(ADMIN_TO_PARTICIPANT_NOTE_COUNT)) : 4;
            }

            for (int i = 0; i < noteCount; i++) {

                final WithdrawalRequestNote note = new WithdrawalRequestNote();
                note
                        .setNoteTypeCode(noteType == ADMIN_TO_ADMIN_NOTE_TYPE ? WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE
                                : WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
                note
                        .setNote(noteType == ADMIN_TO_ADMIN_NOTE_TYPE ? "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin auctor mollis pede. Aliquam aliquam. Nam quis augue a libero semper bibendum. Nulla eu dui. Ut at orci. Vestibulum congue sagittis nulla. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Aenean ac magna vitae odio fringilla aliquam. Proin malesuada volutpat sapien. Nulla facilisi. In convallis.\nInteger sit amet nisi ut orci fringilla varius. Sed quis nulla nec ligula ultricies auctor. Quisque luctus lectus non lacus. Mauris ut augue ac ipsum fermentum semper. Aliquam ultrices neque et turpis. Phasellus bibendum. Duis convallis, sapien sit amet sollicitudin placerat, dolor mauris viverra lacus, non volutpat metus metus sed risus posuere."
                                : "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Pellentesque at tortor. Donec auctor, enim id molestie dignissim, nisi neque pharetra ante, sit amet mattis ipsum purus et lectus. Suspendisse a urna. Donec orci dui, faucibus eu, volutpat ut, mollis quis, ante. Morbi ultricies mauris id quam. Fusce nulla erat, commodo in, feugiat id, commodo vel, metus. Donec mauris. Pellentesque et orci in nisl placerat auctor. Duis faucibus turpis duis.");
                note.setCreated(new Timestamp(new Date().getTime()));
                note.setCreated(new Timestamp(123456 * i));
                note.setCreatedById(1580521);

                notes.add(note);
            }
        }

        return notes;
    }

    /**
     * @param parameterMap
     * @return Collection<WithdrawalRequestFee> - The collection of mock fees
     */
    private static Collection<Fee> getMockFees(
            final Map<String, String> parameterMap) {

        final Collection<Fee> fees = new ArrayList<Fee>();
        final WithdrawalRequestFee fee = new WithdrawalRequestFee();
        fee.setTypeCode(WithdrawalRequestFee.DOLLAR_TYPE_CODE);
        fee.setValue(new BigDecimal("123.91"));
        fees.add(fee);
        return fees;
    }

    /**
     * @param parameterMap
     * @return Collection<WithdrawalRequestDeclaration> - The collection of mock declaration
     */
    private static Collection<Declaration> getMockDeclarations(
            final Map<String, String> parameterMap) {

        Collection<Declaration> declarations = new ArrayList<Declaration>();
        final WithdrawalRequestDeclaration declaration1 = new WithdrawalRequestDeclaration();
        declaration1.setTypeCode(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
        declarations.add(declaration1);
        final WithdrawalRequestDeclaration declaration2 = new WithdrawalRequestDeclaration();
        declaration2.setTypeCode(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE);
        declarations.add(declaration2);
        final WithdrawalRequestDeclaration declaration3 = new WithdrawalRequestDeclaration();
        declaration3.setTypeCode(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
        declarations.add(declaration3);
        return declarations;
    }

    /**
     * @param parameterMap
     * @return ContractInfo - The mock contract information
     */
    private static ContractInfo getMockContractInfo(final Map<String, String> parameterMap) {

        final ContractInfo contract = new ContractInfo();
        contract.setHasApprovePermission(!parameterMap.containsKey(REMOVE_APPROVE));
        contract.setHasInitiatePermission(!parameterMap.containsKey(REMOVE_INITIATE));
        contract.setHasReviewPermission(!parameterMap.containsKey(REMOVE_REVIEW));
        // contract.setHasViewAllPermission(!parameterMap.containsKey(REMOVE_VIEW_ALL));
        contract.setDefinedBenefits(parameterMap.containsKey(HAS_DEFINED_BENEFITS));
        contract.setInitiatorMayApprove(parameterMap.containsKey(INITIATOR_MAY_APPROVE));
        contract.setHasATpaFirm(parameterMap.containsKey(HAS_TPA));
        contract.setMailChequeToAddressIndicator(parameterMap.containsKey(SEND_CHEQUE));
        contract.setTwoStepApprovalRequired(parameterMap.containsKey(USE_TWO_STEP));
        contract.setStatus(parameterMap.containsKey(CONTRACT_STATUS) ? parameterMap
                .get(CONTRACT_STATUS) : ContractInfo.CONTRACT_STATUS_ACTIVE);

        return contract;
    }

    /**
     * @param parameterMap
     * @return ParticipantInfo - The mock participant withdrawal info
     */
    private static ParticipantInfo getMockParticipantInfo(final Map<String, String> parameterMap) {

        final ParticipantInfo info = new ParticipantInfo();
        info.setTrusteeName("Plan Trustee Name");
        return info;
    }

    private static Map<String, String> getTypedMap(final Map target) {
        final Map<String, String> map = new HashMap<String, String>();
        for (Object key : target.keySet()) {
            final Object value = target.get(key);
            if (!(key instanceof String) || !(value instanceof String[])) {
                throw new ClassCastException(new StringBuffer("Key [").append(key).append(
                        "] is not a String or value [").append(value).append(
                        "] is not a String array.").toString());
            }
            map.put((String) key, ((String[]) value)[0]);
        }

        return map;
    }
}
