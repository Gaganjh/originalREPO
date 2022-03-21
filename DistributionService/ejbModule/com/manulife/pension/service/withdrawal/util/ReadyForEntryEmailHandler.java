package com.manulife.pension.service.withdrawal.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmailProcessingServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.NumberUtils;
import com.manulife.pension.util.email.BodyPart;
import com.manulife.pension.util.email.EmailMessageException;
import com.manulife.pension.util.email.EmailMessageVO;
import com.manulife.util.render.RenderConstants;

/**
 * A handler for creating the Withdrawal Ready For Entry Email.
 * 
 * @author Kristin Kerr
 */
public class ReadyForEntryEmailHandler {

    private static Logger logger = Logger.getLogger(ReadyForEntryEmailHandler.class);

    private static final String NBSP = "&nbsp;";

    private static final String DASH = "-";

    private static final String COMMA = ",";

    private static final String START_ROW = "<tr><td>";

    private static final String END_ROW = "</td></tr>";

    private static final String BLANK_ROW = "<tr></tr>";

    private static final String DIVIDER_LINE = "------------------------------------------------"
            + "---------------------------";

    private static final String COURIER_REQUESTED_TO = "***COURIER REQUESTED TO ";

    private static final String SEND_CHECK_TO_PAYEE = "***SEND CHECK TO PAYEE - ";

    private static final String DOLLAR_SIGN = "$";

    private static final String PERCENT_SIGN = "%";

    private static final String PERCENTAGE = "% of account balance";

    private static final String NA = "N/A";

    private static final String TOTAL = "Total";

    private static final String PARTIAL = "Partial";

    private static final String PARTICIPANT = "Participant";

    private static final String TRUSTEE = "Trustee";

    private static final String ROLLOVER_FI = "Rollover FI";

    private static final String CHECK = "Check";

    private static final String ACH = "ACH";

    private static final String WIRE = "Wire";

    private static final String NA_TRUSTEE_NAME = "N/A (trustee name)";

    private static final String WMSI = "WMSI";

    private static final String PENCHECKS = "PenChecks";
    
    private static final String JHMI = "JHMI";

    private static final String YES = "Yes";

    private static final String NO = "No";

    private static final String OUTSIDE_US = "Outside US";

    private static final String FORMAT_PATTERN_WITH_COMMAS = "###,###,##0.00";

    private static final String FORMAT_PATTERN_NO_COMMAS = "########0.00";

    private static final String FORMAT_PATTERN_PERCENT = "##0.00";

    private static final String FORMAT_PATTERN_PERCENT_TAX = "###.####";

    private static final String FORMAT_PATTERN_PERCENT_VESTING = "##0.000";

    private static final BigDecimal ZERO_AMOUNT = GlobalConstants.ZERO_AMOUNT;

    private static final FastDateFormat dateFormatShort = FastDateFormat.getInstance("MMM/dd/yyyy");

    private static final FastDateFormat dateFormatLong = FastDateFormat.getInstance(
            "MMM/dd/yyyy hh:mm:ss a");

    private static final FastDateFormat dateFormatNotes =  FastDateFormat.getInstance(
            RenderConstants.LONG_MDY_SLASHED);

    // Email Body Labels
    private static final String APPROVED_LABEL = "<b>Approved:</b>";

    private static final String TEAM_LABEL = "<b>Team:</b>";

    private static final String CAR_ID_LABEL = "<b>CAR ID:</b>";

    private static final String NAME_LABEL = "<b>Name:</b>";

    private static final String ACCT_NO_LABEL = "<b>Acct No:</b>";

    private static final String PARTICIPANT_SSN = "<b>Participant SSN:</b>";

    private static final String SUBMISSION_LABEL = "<b>Submissn#:</b>";

    private static final String TERMINATION_DATE_LABEL = "<b>Termination Date:</b>";

    private static final String RETIREMENT_DATE_LABEL = "<b>Retirement Date:</b>";

    private static final String DISABILITY_DATE_LABEL = "<b>Disability Date:</b>";

    private static final String EFFECTIVE_DATE_LABEL = "<b>Effective Date:</b>";

    private static final String WITHDRAWAL_REASON_LABEL = "<b>Withdrawal Reason:</b>";

    private static final String TPA_WITHDRAWAL_FEE_LABEL = "<b>TPA Withdrawal Fee:</b>";

    private static final String AMOUNT_TYPE_LABEL = "<b>Amount Type:</b>";

    private static final String PAY_TO_LABEL = "<b>Pay To:</b>";

    private static final String TOTAL_PARTIAL_LABEL = "<b>Total/Partial:</b>";

    private static final String FINAL_CONTRIBUTION_DATE_LABEL = "<b>Final Contribn Date:</b>";

    private static final String AMOUNT_LABEL = "<b>Amount:</b>";

    private static final String AMOUNT_DETAILS_LABEL = "<b>Amount Details:</b>";

    private static final String MONEY_TYPE_LABEL = "<b>Money Type</b>";

    private static final String DOLLAR_AMOUNT_REQUESTED_LABEL = "<b>$ Amount Requested</b>";

    private static final String PERCENT_AMOUNT_REQUESTED_LABEL = "<b>% of Available Amount Requested</b>";

    private static final String UNVESTED_MONEY_OPTION_LABEL = "<b>Unvested Money Option:</b>";

    private static final String VESTING_INSTRUCTIONS_LABEL = "<b>Vesting Instructions:</b>";

    private static final String VESTING_PERCENTAGE_LABEL = "<b>Vesting Percentage</b>";

    private static final String IRA_PROVIDER_LABEL = "<b>IRA Provider:</b>";

    private static final String PARTICIPANT_DOB_LABEL = "<b>Participant DOB:</b>";

    private static final String PAYEE_LABEL = "<b>Payee:</b>";

    private static final String PAYEE_1_LABEL = "<b>Payee 1:</b>";

    private static final String PAYEE_2_LABEL = "<b>Payee 2:</b>";
    
    private static final String PAYEE_3_LABEL = "<b>Payee 3:</b>";

    private static final String PAYEE_4_LABEL = "<b>Payee 4:</b>";
    
    private static final String PAYEE_TAXES_LABEL= "<b>Payee And Taxes Section: </b>";
    
    private static final String PAYMENT_METHOD_LABEL = "<b>Payment Method:</b>";

    private static final String CHECK_NAME_LABEL = "<b>Check Name:</b>";

    private static final String PARTICIPANT_NAME_LABEL = "<b>Participant Name:</b>";

    private static final String TRUSTEE_NAME_LABEL = "<b>Trustee Name:</b>";

    private static final String ROLLOVER_FI_NAME_LABEL = "<b>Rollover FI Name:</b>";

    private static final String ADDRESS_LINE_1_LABEL = "<b>Address Line 1:</b>";

    private static final String ADDRESS_LINE_2_LABEL = "<b>Address Line 2:</b>";

    private static final String CITY_LABEL = "<b>City:</b>";

    private static final String STATE_AND_ZIP_CODE_LABEL = "<b>State and ZIP Code:</b>";

    private static final String COUNTRY_LABEL = "<b>Country:</b>";

    private static final String US_CITIZEN_LABEL = "<b>US Citizen:</b>";

    private static final String WD_IRS_DISTRIBUTION_CODE_LABEL = "<b>W/D IRS Distribn Cd:</b>";

    private static final String LOAN_OPTION_LABEL = "<b>Loan Option:</b>";

    private static final String LOAN_IRS_DISTRIBUTION_CODE_LABEL = "<b>Loan IRS Distrib Cd:</b>";

    private static final String FEDERAL_TAX_LABEL = "<b>Federal Tax:</b>";

    private static final String STATE_TAX_LABEL = "<b>State Tax:</b>";

    private static final String STATE_OF_RESIDENCE_LABEL = "<b>State of Residence:</b>";

    private static final String ROLLOVER_ACCOUNT_NO_LABEL = "<b>Rollover Account No:</b>";

    private static final String ROLLOVER_NEW_PLAN_NAME_LABEL = "<b>Rollover New Plan Name:</b>";

    private static final String TAX_FORM_NAME_LABEL = "<b>1099R Tax Form Name:</b>";

    private static final String BANK_NAME_LABEL = "<b>Bank Name:</b>";

    private static final String BANK_BRANCH_NAME_LABEL = "<b>Bank/Branch Name:</b>";

    private static final String ADDRESS_LABEL = "<b>Address:</b>";

    private static final String ABA_LABEL = "<b>ABA Number:</b>";

    private static final String ACCOUNT_NO_LABEL = "<b>Account No:</b>";

    private static final String CREDIT_PARTY_NAME_LABEL = "<b>Credit Party Name:</b>";

    private static final String NOTES_TO_PARTICIPANT_LABEL = "<b>Notes to Participant:</b>";

    private static final String NOTES_BETWEEN_ADMINISTRATORS_LABEL = "<b>Notes between Administrators:</b>";
    
    private static final String TAX_FLAGS_LABEL = "<b>Tax Flags:</b>";
    
    private static final String PAYEE_INFO_LABEL ="<b>Payee Info:</b>";

    private static final String TAX_VALUE_LABEL = "<b>Tax Flags Values:</b>";
    
    private static final String PAYMENT_AMOUNT = "<b>Payee Directly to Me </b>";
    
    protected static final int PAYEE_KEY_PARTICIPANT = 1;

    protected static final int PAYEE_KEY_TRUSTEE = 2;

    protected static final int PAYEE_KEY_ROLLOVER_PLAN = 3;

    protected static final int PAYEE_KEY_ROLLOVER_IRA = 4;

    protected static final int PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_1 = 5;

    protected static final int PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_2 = 6;

    protected static final int PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_1 = 7;

    protected static final int PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_2 = 8;
    
    protected static final int MULTI_DESTINATION_PAYEE_1 = 9;
    protected static final int MULTI_DESTINATION_PAYEE_2 = 10;
    protected static final int MULTI_DESTINATION_PAYEE_3 = 11;
    protected static final int MULTI_DESTINATION_PAYEE_4 = 12;

    // protected static final int PAYEE_KEY_ROLLOVER_TO_JH_IRA = 9;

    protected static final int PAYMENT_KEY_CHECK = 1;

    protected static final int PAYMENT_KEY_PARTICIPANT = 2;

    protected static final int PAYMENT_KEY_TRUSTEE = 3;

    protected static final int PAYMENT_KEY_ROLLOVER_FI = 4;

    protected static final int PAYMENT_KEY_WMSI_PENCHECKS = 5;

    private static final Integer PAYEE_1 = new Integer(1);

    private static final Integer PAYEE_2 = new Integer(2);
    
    private static final Integer PAYEE_3 = new Integer(3);

    private static final Integer PAYEE_4 = new Integer(4);

    private static final Map<Integer, String> PAYEE_DESCRIPTION_MAP = new HashMap<Integer, String>();

    private static final Map<String, String> AMOUNT_TYPE_MAP = new HashMap<String, String>();

    private static final Map<String, String> PAYMENT_TO_MAP = new HashMap<String, String>();

    private static final Map<String, String> AMOUNT_REQUESTED_LABEL_MAP = new HashMap<String, String>();

    private static final Map<String, String> IRA_PROVIDER_MAP = new HashMap<String, String>();

    private static final Map<Integer, String> PAYEE_LABEL_MAP = new HashMap<Integer, String>();

    private static final Map<String, String> PAYMENT_METHOD_MAP = new HashMap<String, String>();

    private static final Map<Integer, String> PAYEE_NAME_LABEL_MAP = new HashMap<Integer, String>();

    private static final Map<String, String> LOAN_OPTION_MAP = new HashMap<String, String>();

    private static final Map<String, String> STATE_TAX_TYPE_MAP = new HashMap<String, String>();

    private static final Map<String, String> BANK_ACCOUNT_TYPE_MAP = new HashMap<String, String>();

    /**
     * Initialize the maps.
     */
    private static void initialiseMaps() {
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_PARTICIPANT, PARTICIPANT);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_TRUSTEE, TRUSTEE);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_ROLLOVER_PLAN, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_ROLLOVER_IRA, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_1, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_2, PARTICIPANT);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_1, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_2, PARTICIPANT);
        
        PAYEE_DESCRIPTION_MAP.put(MULTI_DESTINATION_PAYEE_1, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(MULTI_DESTINATION_PAYEE_2, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(MULTI_DESTINATION_PAYEE_3, ROLLOVER_FI);
        PAYEE_DESCRIPTION_MAP.put(MULTI_DESTINATION_PAYEE_4, PARTICIPANT);
        // PAYEE_DESCRIPTION_MAP.put(PAYEE_KEY_ROLLOVER_TO_JH_IRA, ROLLOVER_FI);

        AMOUNT_TYPE_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                " Specific Dollar Amount");
        AMOUNT_TYPE_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE, StringUtils
                .repeat(NBSP, 10)
                + "Max Available");
        AMOUNT_TYPE_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE,
                StringUtils.repeat(NBSP, 2) + "Percent By Money Type");
        AMOUNT_TYPE_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE, StringUtils
                .repeat(NBSP, 10)
                + "Max Deferral");

        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                "Direct to Participant");
        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, "Trustee");
        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE,
                "Rollover to Another Plan");
        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, "Rollover to IRA");
        PAYMENT_TO_MAP.put(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                "After-tax split (New Plan)");
        PAYMENT_TO_MAP.put(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                "After-tax split (IRA)");
        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION,
                "Multi-Payee");

        PAYMENT_TO_MAP.put(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE, "Rollover JH IRA");

        AMOUNT_REQUESTED_LABEL_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                DOLLAR_AMOUNT_REQUESTED_LABEL);
        AMOUNT_REQUESTED_LABEL_MAP.put(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE,
                PERCENT_AMOUNT_REQUESTED_LABEL);
        AMOUNT_REQUESTED_LABEL_MAP.put(
                WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE,
                PERCENT_AMOUNT_REQUESTED_LABEL);
        AMOUNT_REQUESTED_LABEL_MAP.put(
                WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE,
                PERCENT_AMOUNT_REQUESTED_LABEL);

        IRA_PROVIDER_MAP.put(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE, "WMSI");
        IRA_PROVIDER_MAP.put(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE, "PenChecks");
        IRA_PROVIDER_MAP.put(WithdrawalRequest.IRA_SERVICE_PROVIDER_JHMIRA_CODE, "JHMI");
        IRA_PROVIDER_MAP.put(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE, NA);
        IRA_PROVIDER_MAP.put("", NA);
        IRA_PROVIDER_MAP.put(null, NA);

        PAYEE_LABEL_MAP.put(PAYEE_KEY_PARTICIPANT, PAYEE_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_TRUSTEE, PAYEE_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_ROLLOVER_PLAN, PAYEE_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_ROLLOVER_IRA, PAYEE_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_1, PAYEE_1_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_2, PAYEE_2_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_1, PAYEE_1_LABEL);
        PAYEE_LABEL_MAP.put(PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_2, PAYEE_2_LABEL);
        PAYEE_LABEL_MAP.put(MULTI_DESTINATION_PAYEE_1, PAYEE_1_LABEL);
        PAYEE_LABEL_MAP.put(MULTI_DESTINATION_PAYEE_2, PAYEE_2_LABEL);
        PAYEE_LABEL_MAP.put(MULTI_DESTINATION_PAYEE_3, PAYEE_3_LABEL);
        PAYEE_LABEL_MAP.put(MULTI_DESTINATION_PAYEE_4, PAYEE_4_LABEL);
        
        // PAYEE_LABEL_MAP.put(PAYEE_KEY_ROLLOVER_TO_JH_IRA, PAYEE_LABEL);

        PAYMENT_METHOD_MAP.put(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, ACH);
        PAYMENT_METHOD_MAP.put(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, WIRE);
        PAYMENT_METHOD_MAP.put(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, CHECK);
        PAYMENT_METHOD_MAP.put("", "");
        PAYMENT_METHOD_MAP.put(null, "");

        PAYEE_NAME_LABEL_MAP.put(PAYMENT_KEY_CHECK, CHECK_NAME_LABEL);
        PAYEE_NAME_LABEL_MAP.put(PAYMENT_KEY_PARTICIPANT, PARTICIPANT_NAME_LABEL);
        PAYEE_NAME_LABEL_MAP.put(PAYMENT_KEY_TRUSTEE, TRUSTEE_NAME_LABEL);
        PAYEE_NAME_LABEL_MAP.put(PAYMENT_KEY_ROLLOVER_FI, ROLLOVER_FI_NAME_LABEL);
        PAYEE_NAME_LABEL_MAP.put(PAYMENT_KEY_WMSI_PENCHECKS, ROLLOVER_FI_NAME_LABEL);

        LOAN_OPTION_MAP.put(WithdrawalRequest.LOAN_ROLLOVER_OPTION, "Rollover Any Loans");
        LOAN_OPTION_MAP.put(WithdrawalRequest.LOAN_KEEP_OPTION, "Keep Any Loan");
        LOAN_OPTION_MAP.put(WithdrawalRequest.LOAN_CLOSURE_OPTION, "Default Any Loans");
        LOAN_OPTION_MAP.put(WithdrawalRequest.LOAN_REPAY_OPTION, "Repay Before W/D");

        STATE_TAX_TYPE_MAP.put(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX,
                "of fed tax");
        STATE_TAX_TYPE_MAP.put(StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_WITHDRAWAL,
                "of withdrawl");

        BANK_ACCOUNT_TYPE_MAP.put(WithdrawalRequestPayee.CHECKING_ACCOUNT_TYPE_CODE, "Checking");
        BANK_ACCOUNT_TYPE_MAP.put(WithdrawalRequestPayee.SAVINGS_ACCOUNT_TYPE_CODE, "Savings");
        BANK_ACCOUNT_TYPE_MAP.put(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    private WithdrawalRequest withdrawalRequest;

    private Map lookupData;

    private Map userNames;

    private WithdrawalRequestMoneyType[] moneyTypes;

    private WithdrawalRequestNote[] participantNotes;

    private WithdrawalRequestNote[] adminNotes;

    private WithdrawalRequestRecipient recipient;

    private WithdrawalRequestPayee payee1;

    private WithdrawalRequestPayee payee2;
    
    private WithdrawalRequestPayee payee3;
    
    private WithdrawalRequestPayee payee4;

    private Collection<WithdrawalRequestPayee> payeesSendCheckByCourier = new ArrayList<WithdrawalRequestPayee>();

    private Collection<WithdrawalRequestPayee> payeesMailCheckToAddress = new ArrayList<WithdrawalRequestPayee>();
    
    private static final String BUNDLED_CONTRACT_INDICATOR = "T"; 

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequest
     * @param lookupData
     * @param userNames
     * @throws SystemException
     */
    public ReadyForEntryEmailHandler(final WithdrawalRequest withdrawalRequest,
            final Map lookupData, final Map userNames) {
        initialiseMaps();
        this.withdrawalRequest = withdrawalRequest;
        recipient = (WithdrawalRequestRecipient)withdrawalRequest.getRecipients().iterator().next();

        this.lookupData = lookupData;
        this.userNames = userNames;
    }

    public static Collection<String> getLookupKeys() {

        Collection<String> lookupKeys = new ArrayList<String>();

        // Step One
        lookupKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
        lookupKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);
        lookupKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);

        // Step Two
        lookupKeys.add(CodeLookupCache.WITHDRAWAL_AMOUNT_TYPE);
        lookupKeys.add(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE_BY_DESC);
        lookupKeys.add(CodeLookupCache.TPA_TRANSACTION_FEE_TYPE);
        lookupKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        lookupKeys.add(CodeLookupCache.COURIER_COMPANY);
        lookupKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupKeys.add(CodeLookupCache.PAYMENT_TO_TYPE);

        // added for the back button
        lookupKeys.add(CodeLookupCache.USA_STATE_WITH_MILITARY_TYPE);
        return lookupKeys;
    }
    
    public ArrayList<BodyPart> getSubmissionEmailContent() throws SystemException {

        sortPayees();
        sortMoneyTypes();
        sortAdminToParticipantNotes();
        sortAdminToAdminNotes();
        return createBody();
        
    }
    public void sendEmail() throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> sendEmail");
        }

        sortPayees();
        sortMoneyTypes();
        sortAdminToParticipantNotes();
        sortAdminToAdminNotes();

        EmailProcessingServiceDelegate processingDelegate = EmailProcessingServiceDelegate
                .getInstance(null);

        String subject = createSubject();

        final BaseEnvironment baseEnvironment = new BaseEnvironment();

        final String sender = baseEnvironment.getStringVariable(baseEnvironment.getApplicationId()
                + "." + "withdrawal.email.readyForEntry.sender.emailAddress");

        final String[] recipients =  baseEnvironment
                .getStringVariable(baseEnvironment.getApplicationId() + "."
                        + "withdrawal.email.readyForEntry.recipient.emailAddress").split(",");

        try {
            EmailMessageVO message = new EmailMessageVO(null, sender, null, recipients, null, null,
                    subject, createBody(), 0);
            processingDelegate.sendAndReceiveConfirmation(message);

        } catch (EmailMessageException e) {
            SystemException se = new SystemException(e,
                    "com.manulife.pension.ps.web.withdrawal.util.ReadyForEntryEmailHandler",
                    "sendEmail", "EmailMessageException generating the Ready For Entry Email for "
                            + subject);
            throw se;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- sendEmail");
        }
    }
    
    private String createSubject() {

        StringBuffer subject = new StringBuffer();
        subject.append(withdrawalRequest.getContractInfo().getTeamCode());
        subject.append(" ");
        subject.append(withdrawalRequest.getContractInfo().getClientAccountRepId());
        subject.append(" [");
        subject.append(withdrawalRequest.getContractId().toString());
        subject.append(" - ");
        subject.append(withdrawalRequest.getSubmissionId().toString());
        subject.append("] ");
        subject.append(withdrawalRequest.getLastName());
        return subject.toString();
    }

    private ArrayList<BodyPart> createBody() throws SystemException {
    	
        StringBuffer content = new StringBuffer();
        content.append("<html>");
        content.append("<head>");
        content.append("</head>");
        content.append("<body>");
        content.append("<table style=\"font-family:'Courier New';font-size:10pt\" width=\"615\">");

        content.append(idSection());
        content.append(generalInfoSection());
        content.append(freeFormSection());
        content.append(vestingSection());
        if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        content.append(multipayeeSection(payee1));
        }
        content.append(payeeDetails1Section(payee1));
        content.append(payeeDetails2Section(payee1));
        content.append(eftSection(payee1));
        if (payee2 != null) {
        	 if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	content.append(multipayeeSection(payee2));
        	 }
            content.append(payeeDetails1Section(payee2));
            content.append(payeeDetails2Section(payee2));
            content.append(eftSection(payee2));
        }
        if(withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        if (payee3 != null) {
        	content.append(multipayeeSection(payee3));
            content.append(payeeDetails1Section(payee3));
            content.append(payeeDetails2Section(payee3));
            content.append(eftSection(payee3));
        }
        if (payee4 != null) {
        	content.append(multipayeeSection(payee4));
            content.append(payeeDetails1Section(payee4));
            content.append(payeeDetails2Section(payee4));
            content.append(eftSection(payee4));
        }
        }
        content.append(notesSection());

        content.append("</table>");
        content.append("</body>");
        content.append("</html>");

        ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();
        bodyParts.add(new BodyPart("readyForEntryEmailContent", true, BodyPart.HTML_CONTENT,
                content.toString(), null));
        return bodyParts;
    }

    /**
     * Section 1 - Identification Info.
     */
    private StringBuffer idSection() throws SystemException {

        StringBuffer sb = new StringBuffer();
        int firstRowContentSize = 0;
        // Line 1
        sb.append(START_ROW);
        if(isBundledGaContract()){
            sb.append(BUNDLED_CONTRACT_INDICATOR);
            sb.append(NBSP);
            //Bundle GA indicator will occupy two more spaces, so reduce this two empty space after contract name.
            firstRowContentSize = 2;
        }
        sb.append(withdrawalRequest.getContractId().toString());
        sb.append(NBSP);
        firstRowContentSize = firstRowContentSize + withdrawalRequest.getContractId().toString().length() + 1;
        sb.append(withdrawalRequest.getContractName().trim());
        firstRowContentSize = firstRowContentSize + withdrawalRequest.getContractName().trim().length();
        int numSpaces = 43  - firstRowContentSize;
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(APPROVED_LABEL);
        sb.append(NBSP);
        sb.append(dateFormatLong.format(withdrawalRequest.getApprovedTimestamp()));
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        numSpaces = 43;
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(TEAM_LABEL);
        numSpaces = 5;
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(withdrawalRequest.getContractInfo().getTeamCode());
        if(withdrawalRequest.getContractInfo().getClientAccountRepId().length() == 8){
        	numSpaces = 4;
        }
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(CAR_ID_LABEL);
        sb.append(NBSP);
        sb.append(NBSP);
        sb.append(withdrawalRequest.getContractInfo().getClientAccountRepId());
        sb.append(END_ROW);

        // Line 3
        if (payeesSendCheckByCourier.size() > 0 || payeesMailCheckToAddress.size() > 0) {
            sb.append(BLANK_ROW);
        }

        // Line 4
        String payeeDescription;
        Iterator<WithdrawalRequestPayee> payees = payeesSendCheckByCourier.iterator();
        WithdrawalRequestPayee payee;
       // String payeeDescription;
        while (payees.hasNext()) {
            payee = payees.next();
            sb.append(START_ROW);
            sb.append(COURIER_REQUESTED_TO);
            
            if(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo())){
            	if(payee.getParticipant()!=null && payee.getParticipant().indexOf("Participant") !=-1 ){
            		payeeDescription = PARTICIPANT;
            	}else {
            		payeeDescription = ROLLOVER_FI;
            	}
            }else {
            	payeeDescription = PAYEE_DESCRIPTION_MAP.get(getPayeeKey(payee));
            }
            sb.append(payeeDescription);
            numSpaces = 13 - payeeDescription.length();
            sb.append(StringUtils.repeat(NBSP, numSpaces));
            sb.append(NAME_LABEL);
            sb.append(NBSP);
            String courierName = lookupDescription(CodeLookupCache.COURIER_COMPANY, payee
                    .getCourierCompanyCode());
            sb.append(courierName);
            numSpaces = 8 - courierName.length();
            sb.append(StringUtils.repeat(NBSP, numSpaces));
            sb.append(ACCT_NO_LABEL);
            sb.append(NBSP);
            sb.append(payee.getCourierNo());
            sb.append(END_ROW);
        }

        // Line 5
        payees = payeesMailCheckToAddress.iterator();
        while (payees.hasNext()) {
            payee = payees.next();
            sb.append(START_ROW);
            sb.append(SEND_CHECK_TO_PAYEE);
            if(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo())){
            	if(payee.getParticipant()!=null && payee.getParticipant().indexOf("Participant") !=-1 ){
            		payeeDescription = PARTICIPANT;
            	}else {
            		payeeDescription = ROLLOVER_FI;
            	}
            }else {
            	payeeDescription = PAYEE_DESCRIPTION_MAP.get(getPayeeKey(payee));
            }
            sb.append(payeeDescription);
            sb.append(END_ROW);
        }

        // Line 6
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Section 2 - General Information Screen.
     */
    private StringBuffer generalInfoSection() {

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(PARTICIPANT_SSN);
        sb.append(NBSP);
        String ssn = withdrawalRequest.getParticipantSSN();
        sb.append(ssn.substring(0, 3));
        sb.append(DASH);
        sb.append(ssn.substring(3, 5));
        sb.append(DASH);
        sb.append(ssn.substring(5, 9));
        sb.append(NBSP);
        String name = withdrawalRequest.getLastName().trim() + COMMA + " "
                + withdrawalRequest.getFirstName().trim();
        if (name.length() > 24) {
            name = name.substring(0, 24);
        }
        sb.append(name);
        int numSpaces = 26 - name.length();
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(SUBMISSION_LABEL);
        sb.append(NBSP);
        sb.append(NBSP);
        sb.append(NBSP);
        sb.append(withdrawalRequest.getSubmissionId());
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        String dateLabel = "";
        String date = "";
        final String withdrawalReasonCode = withdrawalRequest.getReasonCode();
        if (WithdrawalReason.TERMINATION.equals(withdrawalReasonCode)
                && withdrawalRequest.getTerminationDate() != null) {
            dateLabel = TERMINATION_DATE_LABEL;
            date = dateFormatShort.format(withdrawalRequest.getTerminationDate());
            numSpaces = 5;
        } else if (WithdrawalReason.MANDATORY_DISTRIBUTION_TERM.equals(withdrawalReasonCode)
                && withdrawalRequest.getTerminationDate() != null) {
            dateLabel = TERMINATION_DATE_LABEL;
            date = dateFormatShort.format(withdrawalRequest.getTerminationDate());
            numSpaces = 5;
        } else if (WithdrawalReason.RETIREMENT.equals(withdrawalReasonCode)
                && withdrawalRequest.getRetirementDate() != null) {
            dateLabel = RETIREMENT_DATE_LABEL;
            date = dateFormatShort.format(withdrawalRequest.getRetirementDate());
            numSpaces = 6;
        } else if (WithdrawalReason.DISABILITY.equals(withdrawalReasonCode)
                && withdrawalRequest.getDisabilityDate() != null) {
            dateLabel = DISABILITY_DATE_LABEL;
            date = dateFormatShort.format(withdrawalRequest.getDisabilityDate());
            numSpaces = 6;
        } else {
            numSpaces = 33;
        }
        sb.append(dateLabel);
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        sb.append(date);
        sb.append(StringUtils.repeat(NBSP, 16));
        sb.append(EFFECTIVE_DATE_LABEL);
        sb.append(NBSP);
        if (withdrawalRequest.getExpectedProcessingDate() != null) {
            sb.append(dateFormatShort.format(withdrawalRequest.getExpectedProcessingDate()));
        }
        sb.append(END_ROW);

        // Line 3
        sb.append(START_ROW);
        sb.append(WITHDRAWAL_REASON_LABEL);
        sb.append(StringUtils.repeat(NBSP, 4));

        withdrawalRequest.setReasonDescription(lookupDescription(
                CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, withdrawalReasonCode));

        sb.append(withdrawalRequest.getReasonDescription());
        sb.append(END_ROW);

        // Line 4
        sb.append(START_ROW);
        sb.append(TPA_WITHDRAWAL_FEE_LABEL);
        numSpaces = 3;
        sb.append(StringUtils.repeat(NBSP, numSpaces));
        String fee = NA;
        if (withdrawalRequest.getFees() != null) {
            Iterator iterator = withdrawalRequest.getFees().iterator();
            if (iterator.hasNext()) {
                WithdrawalRequestFee requestFee = (WithdrawalRequestFee) iterator.next();
                if (requestFee.getValue() != null
                        && requestFee.getValue().compareTo(ZERO_AMOUNT) > 0) {
                    if (WithdrawalRequestFee.DOLLAR_TYPE_CODE.equals(requestFee.getTypeCode())) {
                        fee = DOLLAR_SIGN + formatDecimalValue(requestFee.getValue(),FORMAT_PATTERN_NO_COMMAS);
                    } else if (WithdrawalRequestFee.PERCENT_TYPE_CODE.equals(requestFee
                            .getTypeCode())) {
                        fee = formatDecimalValue(requestFee.getValue(),FORMAT_PATTERN_PERCENT) + PERCENTAGE;
                    }
                }
            }
        }
        sb.append(fee);
        sb.append(END_ROW);

        // Line 5
        sb.append(START_ROW);
        sb.append(AMOUNT_TYPE_LABEL);
        sb.append(AMOUNT_TYPE_MAP.get(withdrawalRequest.getAmountTypeCode()));
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(PAY_TO_LABEL);
        sb.append(NBSP);
        sb.append(PAYMENT_TO_MAP.get(withdrawalRequest.getPaymentTo()));
        sb.append(END_ROW);

        // Line 6
        sb.append(START_ROW);
        sb.append(TOTAL_PARTIAL_LABEL);
        sb.append(StringUtils.repeat(NBSP, 8));
        if (withdrawalRequest.getParticipantLeavingPlanInd() != null) {
            sb.append(withdrawalRequest.getParticipantLeavingPlanInd() ? TOTAL : PARTIAL);
        } else {
            sb.append(NA);
        }
        sb.append(END_ROW);

        // Line 7
        sb.append(START_ROW);
        sb.append(FINAL_CONTRIBUTION_DATE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 2));
        if (withdrawalRequest.getFinalContributionDate() != null) {
            sb.append(dateFormatShort.format(withdrawalRequest.getFinalContributionDate()));
        } else {
            sb.append(NA);
        }
        sb.append(END_ROW);

        // Line 8
        if (withdrawalRequest.getAmountTypeCode().equals(
                WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
            sb.append(START_ROW);
            sb.append(AMOUNT_LABEL);
            sb.append(StringUtils.repeat(NBSP, 15));
            sb.append(DOLLAR_SIGN);
            sb.append(formatDecimalValue(withdrawalRequest.getWithdrawalAmount(), FORMAT_PATTERN_WITH_COMMAS));
            sb.append(END_ROW);
        }

        // Line 9
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Looks up the description with the lookup data from a {@link DeCodeVO} Collection.
     * 
     * @param lookupCode The code of the Collection in the lookup data.
     * @param lookupValue The value to search for.
     * @return String The value of the description in the {@link DeCodeVO}.
     * 
     * @see DeCodeVO
     */
    protected String lookupDescription(final String lookupCode, final String lookupValue) {
        for (DeCodeVO deCodeVO : (Collection<DeCodeVO>) lookupData.get(lookupCode)) {
            if (StringUtils.equals(deCodeVO.getCode(), lookupValue)) {
                return deCodeVO.getDescription();
            } // fi
        } // end for

        return StringUtils.EMPTY;
    }

    /**
     * Section 3 - Free Form Screen.
     */
    private StringBuffer freeFormSection() {

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(AMOUNT_DETAILS_LABEL);
        sb.append(StringUtils.repeat(NBSP, 9));
        sb.append(MONEY_TYPE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 7));
        sb.append(AMOUNT_REQUESTED_LABEL_MAP.get(withdrawalRequest.getAmountTypeCode()));
        sb.append(END_ROW);

        // Line 2
        if (moneyTypes != null) {
            String moneyTypeName;
            for (int i = 0; i < moneyTypes.length; i++) {
                final WithdrawalRequestMoneyType withdrawalRequestMoneyType = moneyTypes[i];
                if (hasMoneyBeingWithdrawn(withdrawalRequestMoneyType)) {
                    sb.append(START_ROW);
                    sb.append(StringUtils.repeat(NBSP, 26));
                    // Use the custom short name if available, otherwise use the Manulife short
                    // name.
                    moneyTypeName = withdrawalRequestMoneyType.getMoneyTypeAliasId() != null ? withdrawalRequestMoneyType
                            .getMoneyTypeAliasId()
                            : withdrawalRequestMoneyType.getMoneyTypeId();
                    if (moneyTypeName.length() > 15) {
                        moneyTypeName = moneyTypeName.substring(0, 14);
                    }
                    sb.append(moneyTypeName);
                    sb.append(StringUtils.repeat(NBSP, 20 - moneyTypeName.length()));
                    if (withdrawalRequest.getAmountTypeCode().equals(
                            WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                        sb.append(DOLLAR_SIGN);
                        sb.append( formatDecimalValue(withdrawalRequestMoneyType
                                .getWithdrawalAmount(), FORMAT_PATTERN_WITH_COMMAS));
                    } else {
                        sb.append(withdrawalRequestMoneyType.getWithdrawalPercentage());
                        sb.append(PERCENT_SIGN);
                    }
                    sb.append(END_ROW);
                }
            }
        }

        // Line 3
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Determines if the money type has money being withdrawn, meaning a non-zero value.
     * 
     * @param withdrawalRequestMoneyType The money type to check.
     * @return boolean - True if it has a value greater than zero, false otherwise.
     */
    private boolean hasMoneyBeingWithdrawn(
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType) {

        final BigDecimal availableWithdrawalAmount = withdrawalRequestMoneyType
                .getAvailableWithdrawalAmount();
        if (NumberUtils.isGreaterThanZeroValue(availableWithdrawalAmount)) {

            final BigDecimal withdrawalAmount = withdrawalRequestMoneyType.getWithdrawalAmount();
            if (NumberUtils.isGreaterThanZeroValue(withdrawalAmount)) {
                return true;
            } // fi

            final BigDecimal withdrawalPercentage = withdrawalRequestMoneyType
                    .getWithdrawalPercentage();
            if (NumberUtils.isGreaterThanZeroValue(withdrawalPercentage)) {
                return true;
            } // fi
        } // fi

        return false;
    }

    /**
     * Section 4 - Vesting Instructions Screen.
     */
    private StringBuffer vestingSection() {

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(UNVESTED_MONEY_OPTION_LABEL);
        sb.append(StringUtils.repeat(NBSP, 2));
        String unvestedOptionCode = withdrawalRequest.getUnvestedAmountOptionCode();
        String unvestedOptionName = "";
        if ((StringUtils.isEmpty(unvestedOptionCode))
                || (!(withdrawalRequest.getShowOptionForUnvestedAmount()))) {
            unvestedOptionName = NA;
        } else {
            unvestedOptionName = lookupDescription(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS,
                    unvestedOptionCode);
        }
        sb.append(unvestedOptionName);
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        sb.append(VESTING_INSTRUCTIONS_LABEL);
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(MONEY_TYPE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 7));
        sb.append(VESTING_PERCENTAGE_LABEL);
        sb.append(END_ROW);

        // Line 3
        if (moneyTypes != null) {
            String moneyTypeName;
            for (int i = 0; i < moneyTypes.length; i++) {
                BigDecimal balance = moneyTypes[i].getAvailableWithdrawalAmount();
                BigDecimal amount = moneyTypes[i].getWithdrawalAmount();
                boolean suppress = (balance.compareTo(ZERO_AMOUNT) == 0)
                        && (amount == null || (amount.compareTo(ZERO_AMOUNT) == 0));
                if (!suppress) {
                    sb.append(START_ROW);
                    sb.append(StringUtils.repeat(NBSP, 26));
                    // Use custom short name if available, otherwise use Manulife short name.
                    moneyTypeName = moneyTypes[i].getMoneyTypeAliasId() != null ? moneyTypes[i]
                            .getMoneyTypeAliasId() : moneyTypes[i].getMoneyTypeId();
                    if (moneyTypeName.length() > 15) {
                        moneyTypeName = moneyTypeName.substring(0, 14);
                    }
                    sb.append(moneyTypeName);
                    sb.append(StringUtils.repeat(NBSP, 20 - moneyTypeName.length()));
                    sb.append(formatDecimalValue(moneyTypes[i].getVestingPercentage(), FORMAT_PATTERN_PERCENT_VESTING));
                    sb.append(PERCENT_SIGN);
                    sb.append(END_ROW);
                }
            }
        }

        // Line 4
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Section 5 - Payee Details 1 of 3 Screen.
     */
    private StringBuffer payeeDetails1Section(final WithdrawalRequestPayee payee)
            throws SystemException {

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(IRA_PROVIDER_LABEL);
        sb.append(StringUtils.repeat(NBSP, 11));
        String iraProvider = IRA_PROVIDER_MAP.get(withdrawalRequest.getIraServiceProviderCode());
        sb.append(iraProvider);
        sb.append(StringUtils.repeat(NBSP, 22 - iraProvider.length()));
        sb.append(PARTICIPANT_DOB_LABEL);
        sb.append(NBSP);
        if (withdrawalRequest.getBirthDate() != null) {
            sb.append(dateFormatShort.format(withdrawalRequest.getBirthDate()));
        }
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        String payeeLabel = PAYEE_LABEL_MAP.get(getPayeeKey(payee));
        sb.append(payeeLabel);
        int num = 31 - payeeLabel.length();
        sb.append(StringUtils.repeat(NBSP, num)); 
        String payeeDescription;
        if(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo())){
        	if(payee.getParticipant()!=null && payee.getParticipant().indexOf("Participant") !=-1 ){
        		payeeDescription = PARTICIPANT;
        	}else {
        		payeeDescription = ROLLOVER_FI;
        	}
        }else {
        	payeeDescription = PAYEE_DESCRIPTION_MAP.get(getPayeeKey(payee));
        }
         
        sb.append(payeeDescription);
        sb.append(StringUtils.repeat(NBSP, 22 - payeeDescription.length()));
        sb.append(PAYMENT_METHOD_LABEL);
        sb.append(StringUtils.repeat(NBSP, 2));
        sb.append(PAYMENT_METHOD_MAP.get(payee.getPaymentMethodCode()));
        sb.append(END_ROW);

        // Line 3
        sb.append(START_ROW);
        String payeeNameLabel = PAYEE_NAME_LABEL_MAP.get(getPaymentKey(payee));
        sb.append(payeeNameLabel);
        sb.append(StringUtils.repeat(NBSP, 31 - payeeNameLabel.length()));
        String payeeName = StringUtils.EMPTY;
        int key = getPaymentKey(payee);
        if (key == PAYMENT_KEY_CHECK) {
            if (payeeDescription.equals(PARTICIPANT)) {
                StringBuffer buff = new StringBuffer();
                buff.append(StringUtils.trim(payee.getFirstName()));
                buff.append(NBSP);
                buff.append(StringUtils.trim(payee.getLastName()));
                payeeName = buff.toString();

            } else if (payeeDescription.equals(TRUSTEE)) {
                payeeName = payee.getOrganizationName();
            } else if (payeeDescription.equals(ROLLOVER_FI)) {
                payeeName = payee.getOrganizationName();
            }

        } else if (key == PAYMENT_KEY_PARTICIPANT) {
            StringBuffer buff = new StringBuffer();
            buff.append(StringUtils.trim(payee.getFirstName()));
            buff.append(NBSP);
            buff.append(StringUtils.trim(payee.getLastName()));
            payeeName = buff.toString();

        } else if (key == PAYMENT_KEY_TRUSTEE) {
            payeeName = NA_TRUSTEE_NAME;
        } else if (key == PAYMENT_KEY_ROLLOVER_FI) {
            payeeName = payee.getOrganizationName();
        } else if (WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE.equals(withdrawalRequest
                .getIraServiceProviderCode())) {
            payeeName = WMSI;
        } else if (WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE.equals(withdrawalRequest
                .getIraServiceProviderCode())) {
            payeeName = PENCHECKS;
        } else if (WithdrawalRequest.IRA_SERVICE_PROVIDER_JHMIRA_CODE.equals(withdrawalRequest
                .getIraServiceProviderCode())) {
            payeeName = JHMI;
        }
        if (payeeName == null) {
            payeeName = StringUtils.EMPTY;
        }
        sb.append(payeeName);
        sb.append(END_ROW);

        // Line 4
        sb.append(START_ROW);
        sb.append(ADDRESS_LINE_1_LABEL);
        sb.append(StringUtils.repeat(NBSP, 9));
        String address1 = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                address1 = payee.getAddress().getAddressLine1();
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                address1 = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(address1);
        sb.append(END_ROW);

        // Line 5
        sb.append(START_ROW);
        sb.append(ADDRESS_LINE_2_LABEL);
        sb.append(StringUtils.repeat(NBSP, 9));
        String address2 = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                address2 = payee.getAddress().getAddressLine2();
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                address2 = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(address2);
        sb.append(END_ROW);

        // Line 6
        sb.append(START_ROW);
        sb.append(CITY_LABEL);
        sb.append(StringUtils.repeat(NBSP, 19));
        String city = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                city = payee.getAddress().getCity();
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                city = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(city);
        sb.append(END_ROW);

        // Line 7
        sb.append(START_ROW);
        sb.append(STATE_AND_ZIP_CODE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 5));
        String state = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                state = payee.getAddress().getStateCode();
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                state = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(state);
        sb.append(StringUtils.repeat(NBSP, 5 - state.length()));
        String zipCode = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                zipCode = getZipCode(payee.getAddress());
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                zipCode = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(zipCode);
        sb.append(StringUtils.repeat(NBSP, 14 - zipCode.length()));
        sb.append(COUNTRY_LABEL);
        sb.append(NBSP);
        String country = "";
        switch (key) {
            case PAYMENT_KEY_CHECK:
            case PAYMENT_KEY_PARTICIPANT:
            case PAYMENT_KEY_ROLLOVER_FI:
                country = getCountry(payee.getAddress());
                break;
            case PAYMENT_KEY_TRUSTEE:
            case PAYMENT_KEY_WMSI_PENCHECKS:
                country = NA;
                break;
            default:
                break;
        } // end switch
        sb.append(country);
        sb.append(END_ROW);

        // Line 8
        sb.append(START_ROW);
        sb.append(US_CITIZEN_LABEL);
        sb.append(StringUtils.repeat(NBSP, 13));
        String citizenUS = NA;

        if (BooleanUtils.isTrue(recipient.getUsCitizenInd())) {
            citizenUS = YES;
        } // fi
        if (BooleanUtils.isFalse(recipient.getUsCitizenInd())) {
            citizenUS = NO;
        } // fi

        sb.append(citizenUS);
        sb.append(StringUtils.repeat(NBSP, 19 - citizenUS.length()));
        sb.append(WD_IRS_DISTRIBUTION_CODE_LABEL);
        sb.append(NBSP);
        String irsDistCode = NA;
        if (StringUtils.isNotBlank(payee.getIrsDistCode())) {
            irsDistCode = payee.getIrsDistCode();
        }
        sb.append(irsDistCode);
        sb.append(END_ROW);

        // Line 9
        sb.append(START_ROW);
        sb.append(LOAN_OPTION_LABEL);
        sb.append(StringUtils.repeat(NBSP, 12));
        String loanOption = NA;
        if (StringUtils.isNotBlank(withdrawalRequest.getLoanOption())) {
            loanOption = LOAN_OPTION_MAP.get(withdrawalRequest.getLoanOption());
        }
        sb.append(loanOption);
        sb.append(StringUtils.repeat(NBSP, 19 - loanOption.length()));
        sb.append(LOAN_IRS_DISTRIBUTION_CODE_LABEL);
        sb.append(NBSP);
        String loanIRSDistCode = NA;
        if (withdrawalRequest.getIrsDistributionCodeLoanClosure() != null
                && StringUtils.isNotBlank(withdrawalRequest.getIrsDistributionCodeLoanClosure())) {
            loanIRSDistCode = withdrawalRequest.getIrsDistributionCodeLoanClosure();
        }
        sb.append(loanIRSDistCode);
        sb.append(END_ROW);

        // Line 10
        sb.append(START_ROW);
        sb.append(FEDERAL_TAX_LABEL);
        sb.append(StringUtils.repeat(NBSP, 12));
        String federalTax = NA;
        BigDecimal federalTaxRate = recipient.getFederalTaxPercent();
        if (federalTaxRate != null && !(federalTaxRate.compareTo(ZERO_AMOUNT) == 0)) {
            federalTax = formatDecimalValue(federalTaxRate, FORMAT_PATTERN_PERCENT_TAX);
            sb.append(federalTax);
            sb.append(PERCENT_SIGN);
            sb.append(StringUtils.repeat(NBSP, 18 - federalTax.length()));
        } else {
            sb.append(federalTax);
            sb.append(StringUtils.repeat(NBSP, 19 - federalTax.length()));
        }
        sb.append(STATE_TAX_LABEL);
        sb.append(NBSP);

        String stateTax = StringUtils.repeat(NBSP, 10) + NA;
        BigDecimal stateTaxRate = recipient.getStateTaxPercent();
        if (stateTaxRate != null && !(stateTaxRate.compareTo(ZERO_AMOUNT) == 0)) {
            stateTax = formatDecimalValue(stateTaxRate, FORMAT_PATTERN_PERCENT_TAX) + PERCENT_SIGN;
            stateTax = stateTax + StringUtils.repeat(NBSP, 10 - stateTax.length());
            stateTax = stateTax + STATE_TAX_TYPE_MAP.get(recipient.getStateTaxTypeCode());
        }
        sb.append(stateTax);
        sb.append(END_ROW);

        // Line 11
        sb.append(START_ROW);
        sb.append(StringUtils.repeat(NBSP, 43));
        sb.append(STATE_OF_RESIDENCE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 2));
        String stateOfResidence = recipient.getStateOfResidenceCode();
        if (WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US.equals(stateOfResidence)) {
            stateOfResidence = OUTSIDE_US;
        }
        sb.append(stateOfResidence);
        sb.append(END_ROW);

        // Line 12
        String paymentTo = withdrawalRequest.getPaymentTo();
        boolean suppress = true;
        boolean isRolloverTypeEligiable  = false;
        if (payee == payee1) {
            suppress = paymentTo.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                    || paymentTo.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
            isRolloverTypeEligiable = paymentTo.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
            		&& withdrawalRequest.isRolloverTypeEligible();
        } else if (payee == payee2) {
            suppress = paymentTo
                    .equals(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)
                    || paymentTo
                            .equals(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                            || paymentTo
                            .equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION);
        }
        
        /* code added to display Rollover account number for all FI type Payee in a Multi Payee WD scenario*/
        if(paymentTo.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) && 
        		payee.getTypeCode().equalsIgnoreCase("FI"))
        {
        	suppress=false;
        }
        
        if (isRolloverTypeEligiable && 
        		(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE.equals(withdrawalRequest.getReasonCode())
        			|| WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE.equals(withdrawalRequest.getReasonCode())
        			|| WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode()))) {
            if(StringUtils.isNotEmpty(payee.getRolloverType())
            		&& WithdrawalRequest.ROTH_IRA.equals(payee.getRolloverType())){
                sb.append(START_ROW);
                sb.append("<b>"+payee.getRolloverType()+":</b>");
            	sb.append(StringUtils.repeat(NBSP, 15));
                sb.append("Y");
                sb.append(END_ROW);
            }

        }
        
        if (!suppress) {
            sb.append(START_ROW);
            sb.append(ROLLOVER_ACCOUNT_NO_LABEL);
            sb.append(StringUtils.repeat(NBSP, 4));
            sb.append(payee.getRolloverAccountNo());
            sb.append(END_ROW);
        }

        // Line 13
        if (payee == payee1) {
            suppress = suppress
                    || paymentTo.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                    || paymentTo
                            .equals(WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                    || paymentTo.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE) 
                    || paymentTo
                    .equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION);
        }
        /* code added to supress Rollover Plan name for all IRA type FI  Payee in a Multi Payee WD scenario*/
        if(paymentTo.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(payee.getParticipant().indexOf(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)!=-1 ||
    				payee.getParticipant().indexOf(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_ROTH_IRA_CODE)!=-1)	 
        	suppress=true;
        }
        if (!suppress) {
            sb.append(START_ROW);
            sb.append(ROLLOVER_NEW_PLAN_NAME_LABEL);
            sb.append(NBSP);
            sb.append(payee.getRolloverPlanName());
            sb.append(END_ROW);
        }

        // Line 14
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Section 6 - Payee Details 2 of 3 Screen.
     */
    private StringBuffer payeeDetails2Section(final WithdrawalRequestPayee payee) {

        if (withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return new StringBuffer(StringUtils.EMPTY);
        }

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(TAX_FORM_NAME_LABEL);
        sb.append(StringUtils.repeat(NBSP, 4));
        sb.append(recipient.getFirstName() + NBSP + recipient.getLastName());
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        sb.append(ADDRESS_LINE_1_LABEL);
        sb.append(StringUtils.repeat(NBSP, 9));
        sb.append(recipient.getAddress().getAddressLine1());
        sb.append(END_ROW);

        // Line 3
        sb.append(START_ROW);
        sb.append(ADDRESS_LINE_2_LABEL);
        sb.append(StringUtils.repeat(NBSP, 9));
        sb.append(recipient.getAddress().getAddressLine2());
        sb.append(END_ROW);

        // Line 4
        sb.append(START_ROW);
        sb.append(CITY_LABEL);
        sb.append(StringUtils.repeat(NBSP, 19));
        sb.append(recipient.getAddress().getCity());
        sb.append(END_ROW);

        // Line 5
        sb.append(START_ROW);
        sb.append(STATE_AND_ZIP_CODE_LABEL);
        sb.append(StringUtils.repeat(NBSP, 5));
        sb.append(recipient.getAddress().getStateCode());
        sb.append(StringUtils.repeat(NBSP, 3));
        String zipCode = getZipCode(recipient.getAddress());
        sb.append(zipCode);
        sb.append(StringUtils.repeat(NBSP, 14 - zipCode.length()));
        sb.append(COUNTRY_LABEL);
        sb.append(NBSP);
        sb.append(getCountry(recipient.getAddress()));
        sb.append(END_ROW);

        // Line 6
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Section 7 - EFT Details Screen.
     */
    private StringBuffer eftSection(WithdrawalRequestPayee payee) throws SystemException {

        if (WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE.equals(payee.getPaymentMethodCode())) {
            return new StringBuffer(StringUtils.EMPTY);
        }

        StringBuffer sb = new StringBuffer();

        String bankName = NA;
        String addressLine1 = NA;
        String addressLine2 = NA;
        String city = NA;
        String state = NA;
        String zipCode = NA;
        String country = NA;
        int paymentKey = getPaymentKey(payee);
        // Note that if the payment key is participant or trustee then the payment method must be
        // ACH or Wire
        if (paymentKey == PAYMENT_KEY_PARTICIPANT || paymentKey == PAYMENT_KEY_TRUSTEE) {
            bankName = payee.getOrganizationName();
            addressLine1 = payee.getAddress().getAddressLine1();
            addressLine2 = payee.getAddress().getAddressLine2();
            city = payee.getAddress().getCity();
            state = payee.getAddress().getStateCode();
            zipCode = getZipCode(payee.getAddress());
            country = getCountry(payee.getAddress());
        }

        // Line 1
        sb.append(START_ROW);
        sb.append(BANK_NAME_LABEL);
        sb.append(StringUtils.repeat(NBSP, 14));
        sb.append(bankName);
        sb.append(END_ROW);

        // Line 2
        sb.append(START_ROW);
        sb.append(BANK_BRANCH_NAME_LABEL);
        sb.append(StringUtils.repeat(NBSP, 7));
        String bankBranchName = NA;
        if (payee.getPaymentInstruction().getBankName() != null
                && StringUtils.isNotBlank(payee.getPaymentInstruction().getBankName())) {
            bankBranchName = payee.getPaymentInstruction().getBankName();
        }
        sb.append(bankBranchName);
        sb.append(END_ROW);

        // Line 3
        sb.append(START_ROW);
        sb.append(ADDRESS_LABEL);
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(addressLine1);
        sb.append(StringUtils.repeat(NBSP, 34 - addressLine1.length()));
        sb.append(addressLine2);
        sb.append(END_ROW);

        // Line 4
        sb.append(START_ROW);
        sb.append(StringUtils.repeat(NBSP, 11));
        sb.append(city);
        sb.append(StringUtils.repeat(NBSP, 26 - city.length()));
        sb.append(state);
        sb.append(StringUtils.repeat(NBSP, 2));
        sb.append(zipCode);
        sb.append(StringUtils.repeat(NBSP, 11 - zipCode.length()));
        sb.append(country);
        sb.append(END_ROW);

        // Line 5
        sb.append(START_ROW);
        sb.append(ABA_LABEL);
        sb.append(StringUtils.repeat(NBSP, 13));
        String abaNumber = NA;
        if (payee.getPaymentInstruction().getBankTransitNumber() != null
                && StringUtils.isNotBlank(payee.getPaymentInstruction().getBankTransitNumber()
                        .toString())) {
            abaNumber = StringUtils.leftPad(payee.getPaymentInstruction().getBankTransitNumber()
                    .toString(), 9, "0");
        }
        sb.append(abaNumber);
        sb.append(StringUtils.repeat(NBSP, 14 - abaNumber.length()));
        sb.append(ACCOUNT_NO_LABEL);
        sb.append(NBSP);
        String bankAccount = NA;
        if (payee.getPaymentInstruction().getBankAccountNumber() != null
                && StringUtils.isNotBlank(payee.getPaymentInstruction().getBankAccountNumber())) {
            bankAccount = payee.getPaymentInstruction().getBankAccountNumber();
        }
        sb.append(bankAccount);
        sb.append(StringUtils.repeat(NBSP, 17 - bankAccount.length()));
        sb.append(NBSP);
        String bankAccountType = StringUtils.EMPTY;
        if (payee.getPaymentInstruction().getBankAccountTypeCode() != null) {
            bankAccountType = BANK_ACCOUNT_TYPE_MAP.get(payee.getPaymentInstruction()
                    .getBankAccountTypeCode());
        }
        sb.append(bankAccountType);
        sb.append(END_ROW);

        // Line 6
        sb.append(START_ROW);
        sb.append(CREDIT_PARTY_NAME_LABEL);
        sb.append(StringUtils.repeat(NBSP, 6));
        String creditPartyName = NA;
        if (payee.getPaymentInstruction().getCreditPartyName() != null
                && StringUtils.isNotBlank(payee.getPaymentInstruction().getCreditPartyName())) {
            creditPartyName = payee.getPaymentInstruction().getCreditPartyName();
        }
        sb.append(creditPartyName);
        sb.append(END_ROW);

        // Line 7
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Section 8 - Notes.
     */
    private StringBuffer notesSection() {

        if (withdrawalRequest.getReadOnlyAdminToParticipantNotes().isEmpty()
                && withdrawalRequest.getReadOnlyAdminToAdminNotes().isEmpty()) {
            return new StringBuffer(StringUtils.EMPTY);
        }

        StringBuffer sb = new StringBuffer();

        // Line 1
        sb.append(START_ROW);
        sb.append(NOTES_TO_PARTICIPANT_LABEL);
        sb.append(END_ROW);

        // Line 2 - Participant Notes
        sb.append(START_ROW);
        sb.append("<dl>");
        for (WithdrawalRequestNote note : participantNotes) {
            sb.append("<dd>");
            sb.append(getFormattedNote(note));
            sb.append("</dd>");
            if (!note.equals(participantNotes[participantNotes.length - 1])) {
                sb.append("<dd>" + NBSP + "</dd>");
            }
        }
        sb.append("</dl>");
        sb.append(END_ROW);

        // Line 3
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);

        // Line 4
        sb.append(START_ROW);
        sb.append(NOTES_BETWEEN_ADMINISTRATORS_LABEL);
        sb.append(END_ROW);

        // Line 5 - Administrator Notes
        sb.append(START_ROW);
        sb.append("<dl>");
        for (WithdrawalRequestNote note : adminNotes) {
            sb.append("<dd>");
            sb.append(getFormattedNote(note));
            sb.append("</dd>");
            if (!note.equals(adminNotes[adminNotes.length - 1])) {
                sb.append("<dd>" + NBSP + "</dd>");
            }
        }
        sb.append("</dl>");
        sb.append(END_ROW);

        return sb;
    }

    /**
     * Get user profile IDs from the given withdrawal request.
     * 
     * @param withdrawalRequest
     * @return
     */
    public static ArrayList<Integer> getUserProfileIds(final WithdrawalRequest withdrawalRequest) {
        // retrieve the list of User Profile IDs and fetch the user names
        ArrayList<Integer> userProfileIds = new ArrayList<Integer>();

        for (WithdrawalRequestNote note : withdrawalRequest.getReadOnlyAdminToParticipantNotes()) {
            if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
                userProfileIds.add(note.getCreatedById());
            }
        }
        for (WithdrawalRequestNote note : withdrawalRequest.getReadOnlyAdminToAdminNotes()) {
            if (note.getCreatedById() != null && !userProfileIds.contains(note.getCreatedById())) {
                userProfileIds.add(note.getCreatedById());
            }
        }
        return userProfileIds;
    }

    protected int getPayeeKey(WithdrawalRequestPayee payee) throws SystemException {

        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)) {
            return PAYEE_KEY_PARTICIPANT;
        }
        if (withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return PAYEE_KEY_TRUSTEE;
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return PAYEE_KEY_ROLLOVER_PLAN;
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)) {
            return PAYEE_KEY_ROLLOVER_IRA;
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE)) {
            return PAYEE_KEY_ROLLOVER_IRA;
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {
            if (payee.getPayeeNo().equals(PAYEE_1)) {
                return PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_1;
            }
            if (payee.getPayeeNo().equals(PAYEE_2)) {
                return PAYEE_KEY_REMAINDER_TO_PLAN_PAYEE_2;
            }
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)) {
            if (payee.getPayeeNo().equals(PAYEE_1)) {
                return MULTI_DESTINATION_PAYEE_1;
            }
            if (payee.getPayeeNo().equals(PAYEE_2)) {
                return MULTI_DESTINATION_PAYEE_2;
            }
            if (payee.getPayeeNo().equals(PAYEE_3)) {
                return MULTI_DESTINATION_PAYEE_3;
            }
            if (payee.getPayeeNo().equals(PAYEE_4)) {
                return MULTI_DESTINATION_PAYEE_4;
            }
        }
        // if (withdrawalRequest.getPaymentTo().equals(
        // WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE)) {
        // return PAYEE_KEY_ROLLOVER_TO_JH_IRA;
        // }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)) {
            if (payee.getPayeeNo().equals(PAYEE_1)) {
                return PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_1;
            }
            if (payee.getPayeeNo().equals(PAYEE_2)) {
                return PAYEE_KEY_REMAINDER_TO_IRA_PAYEE_2;
            }
        }
        if (withdrawalRequest.getPaymentTo().equals(
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)) {
            if (payee.getPayeeNo().equals(PAYEE_1)) {
                return MULTI_DESTINATION_PAYEE_1;
            }
            if (payee.getPayeeNo().equals(PAYEE_2)) {
                return MULTI_DESTINATION_PAYEE_2;
            }
            if (payee.getPayeeNo().equals(PAYEE_3)) {
                return MULTI_DESTINATION_PAYEE_3;
            }
            if (payee.getPayeeNo().equals(PAYEE_4)) {
                return MULTI_DESTINATION_PAYEE_4;
            }
        }
        // We shouldn't get here
        SystemException se = new SystemException(new Exception(),
                "com.manulife.pension.ps.web.withdrawal.util.ReadyForEntryEmailHandler",
                "getPayeeKey", "Unable to determine payee key in ReadyForEntryEmailHandler");
        throw se;
    }

    protected int getPaymentKey(WithdrawalRequestPayee payee) throws SystemException {

        if (withdrawalRequest.isWmsiOrPenchecksSelected()) {
            return PAYMENT_KEY_WMSI_PENCHECKS;
        }
        if (payee.getPaymentMethodCode().equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE)) {
        	String payeeDescription ;
        	
        	  if(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo())){
              	if(payee.getParticipant()!=null && payee.getParticipant().indexOf("Participant") !=-1 ){
              		
              		return PAYMENT_KEY_CHECK;
              	}else {
              		return PAYMENT_KEY_CHECK;
              	}
              }else {
              	payeeDescription = PAYEE_DESCRIPTION_MAP.get(getPayeeKey(payee));
              	if (payeeDescription.equals(TRUSTEE)) {
                    return PAYMENT_KEY_TRUSTEE;
                }
              }
        	 
            return PAYMENT_KEY_CHECK;
        }
        if (payee.getPaymentMethodCode().equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE)
                || payee.getPaymentMethodCode().equals(
                        WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE)) {
            String payeeDescription = PAYEE_DESCRIPTION_MAP.get(getPayeeKey(payee));
            
            if(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo())){
              	if(payee.getParticipant()!=null && payee.getParticipant().indexOf("Participant") !=-1 ){
              		//payeeDescription = PARTICIPANT;
              		return PAYMENT_KEY_PARTICIPANT;
              	}else {
              		//payeeDescription = ROLLOVER_FI;
              		return PAYMENT_KEY_ROLLOVER_FI;
              	}
              }
            if (payeeDescription.equals(PARTICIPANT)) {
                return PAYMENT_KEY_PARTICIPANT;
            }
            if (payeeDescription.equals(TRUSTEE)) {
                return PAYMENT_KEY_TRUSTEE;
            }
            if (payeeDescription.equals(ROLLOVER_FI)
                    && !withdrawalRequest.isWmsiOrPenchecksSelected()) {
                return PAYMENT_KEY_ROLLOVER_FI;
            }
        }

        // We shouldn't get here
        SystemException se = new SystemException(new Exception(),
                "com.manulife.pension.ps.web.withdrawal.util.ReadyForEntryEmailHandler",
                "getPaymentKey", "Unable to determine payment key in ReadyForEntryEmailHandler");
        throw se;
    }

    protected String getZipCode(DistributionAddress address) {
        if (StringUtils.equals(address.getCountryCode(), GlobalConstants.COUNTRY_CODE_USA)
                || StringUtils.isBlank(address.getCountryCode())) {
            if (address.getZipCode2().equals(StringUtils.EMPTY)) {
                return address.getZipCode1();
            } else {
                return address.getZipCode1() + DASH + address.getZipCode2();
            }
        } else {
            return address.getZipCode();
        }
    }

    protected String getCountry(DistributionAddress address) {
        String countryCode = address.getCountryCode();
        String country = "";
        ArrayList list = (ArrayList) lookupData.get(CodeLookupCache.COUNTRY_COLLECTION_TYPE);
        Iterator iterator = list.iterator();
        LabelValueBean bean;
        while (iterator.hasNext()) {
            bean = (LabelValueBean) iterator.next();
            if (bean.getValue().equals(countryCode)) {
                country = bean.getLabel();
            }
        }
        if (country.length() > 25) {
            country = country.substring(0, 25);
        }
        return country;
    }

    protected StringBuffer getFormattedNote(WithdrawalRequestNote note) {
        StringBuffer sb = new StringBuffer();
        UserName userName = (UserName) userNames.get(note.getCreatedById());
        if (userName != null) {
            sb.append(userName.getFirstName());
            sb.append(NBSP);
            sb.append(userName.getLastName());
        }
        sb.append(NBSP);
        sb.append(dateFormatNotes.format(note.getCreated()));
        sb.append(NBSP + DASH + NBSP);
        sb.append(note.getNote());
        return sb;
    }

    private void sortPayees() {

        WithdrawalRequestPayee[] payees = recipient.getPayees().toArray(
                new WithdrawalRequestPayee[0]);

        if (payees.length == 1) {
            payee1 = payees[0];
        } else if (payees[0].getPayeeNo() == null) {
            payee1 = payees[0];
            payee2 = payees[1];
        } else { // use payee numbers if available
            for (int i = 0; i < payees.length; i++) {
                if (payees[i].getPayeeNo().equals(PAYEE_1)) {
                    payee1 = payees[i];
                } else if (payees[i].getPayeeNo().equals(PAYEE_2)) {
                    payee2 = payees[i];
                }else if (payees[i].getPayeeNo().equals(PAYEE_3)) {
                    payee3 = payees[i];
                }
                else if (payees[i].getPayeeNo().equals(PAYEE_4)) {
                    payee4 = payees[i];
                }               
            }
        }

        for (int i = 0; i < payees.length; i++) {
            if (BooleanUtils.isTrue(payees[i].getSendCheckByCourier())) {
                payeesSendCheckByCourier.add(payees[i]);
            }
            if (BooleanUtils.isTrue(payees[i].getMailCheckToAddress())) {
                payeesMailCheckToAddress.add(payees[i]);
            }
        }
    }

    private void sortMoneyTypes() {
        final Collection<WithdrawalRequestMoneyType> moneyTypesCollection = withdrawalRequest
                .getMoneyTypes();
        if (moneyTypesCollection != null) {
            moneyTypes = (WithdrawalRequestMoneyType[]) (moneyTypesCollection)
                    .toArray(new WithdrawalRequestMoneyType[moneyTypesCollection.size()]);

            if (CollectionUtils.isNotEmpty(moneyTypesCollection)) {
                Arrays.sort(moneyTypes, ComparatorUtils
                        .nullLowComparator(new MoneyTypeComparator()));
            } // fi
        }
    }

    private void sortAdminToParticipantNotes() {
        if (withdrawalRequest.getReadOnlyAdminToParticipantNotes() != null) {
            participantNotes = (WithdrawalRequestNote[]) (withdrawalRequest
                    .getReadOnlyAdminToParticipantNotes()).toArray(new WithdrawalRequestNote[0]);
            if (CollectionUtils.isNotEmpty(withdrawalRequest.getReadOnlyAdminToParticipantNotes())) {
                Arrays.sort(participantNotes, ComparatorUtils.nullHighComparator(ComparatorUtils
                        .reversedComparator(new NoteComparator())));
            } // fi
        } // fi
    }

    private void sortAdminToAdminNotes() {
        if (withdrawalRequest.getReadOnlyAdminToAdminNotes() != null) {
            adminNotes = (WithdrawalRequestNote[]) (withdrawalRequest
                    .getReadOnlyAdminToAdminNotes()).toArray(new WithdrawalRequestNote[0]);

            if (CollectionUtils.isNotEmpty(withdrawalRequest.getReadOnlyAdminToAdminNotes())) {
                Arrays.sort(adminNotes, ComparatorUtils.nullHighComparator(ComparatorUtils
                        .reversedComparator(new NoteComparator())));
            } // fi
        } // fi
    }
    
    /**
     * @return boolean isBundledGaContract
     * @throws SystemException
     */
    private boolean isBundledGaContract() throws SystemException{
			return ContractServiceDelegate.getInstance().isBundledGaContract(withdrawalRequest.getContractId());
    }

    
    /**
	 * Used to format a Double value to the required format
	 * 
	 * @param Double value 
	 * @param String format
	 * @return String Formatted value
	 */
	private static synchronized String formatDecimalValue(BigDecimal value, String format) { 
        return new DecimalFormat(format).format(value); 
    }
	
	private StringBuffer multipayeeSection(final WithdrawalRequestPayee payee) {

      

        StringBuffer sb = new StringBuffer();
        
        String taxable = "N";
        String nonTaxable = "N";
        String rothIra = "N";
        String rothTaxable = "N";
        String nonRothTaxable = "N";
        
        String payeeInfo ="";

        // Line 1
        sb.append(START_ROW);
        sb.append(PAYEE_TAXES_LABEL);
        sb.append(END_ROW);

        // Line 2 - Taxes Flag Section
        sb.append(START_ROW);
        
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(TAX_FLAGS_LABEL);
        sb.append(StringUtils.repeat(NBSP, 7));
        sb.append(TAX_VALUE_LABEL);
        sb.append(END_ROW);
        
        String taxesFlag = payee.getTaxes();
        String payeeDetails = payee.getParticipant();
        
        if(taxesFlag != null && taxesFlag.trim().length()>0){
   		 taxesFlag = taxesFlag.substring(1,taxesFlag.length() - 1);
   		 String newtaxFlag = taxesFlag.replaceAll("\"", "");
   		 String newFlag[] = newtaxFlag.split(","); 
   		  
   		 for( String tax :newFlag) 
   		 { 
   			 if(tax.trim().equals("Non_Taxable:Y")) {
   				nonTaxable ="Y";
   			 }
   			 if(tax.trim().equals("Taxable:Y")) {
   				 taxable ="Y";
   			 }
   			 if(tax.trim().equals("Roth_Non_Tax:Y")) {
   				nonRothTaxable = "Y";
   			 }
   			 if(tax.trim().equals("Roth_Taxable:Y")) {
   				rothTaxable ="Y";
   			 }
   			 if(tax.trim().equals("Roth_IRA:Y")) {
   				 rothIra ="Y";
   			 }
   		 }
       	}
        
        
        sb.append("NON TAXABLE");
        sb.append(StringUtils.repeat(NBSP, 16));
        sb.append(nonTaxable);
        sb.append("<br>");
        
        sb.append("TAXABLE");
        sb.append(StringUtils.repeat(NBSP, 20));
        sb.append(taxable);
        sb.append("<br>");
        
        sb.append("ROTH NON TAXABLE");
        sb.append(StringUtils.repeat(NBSP, 11));
        sb.append(nonRothTaxable);
        sb.append("<br>");
        
        sb.append("ROTH TAXABLE");
        sb.append(StringUtils.repeat(NBSP, 15));
        sb.append(rothTaxable);
        sb.append("<br>");
        
        sb.append("ROTH IRA");
        sb.append(StringUtils.repeat(NBSP, 19));
        sb.append(rothIra);
        sb.append("<br>");
        
        //Payee Section
        if(payeeDetails != null && payeeDetails.trim().length()>0){
        	payeeDetails = payeeDetails.substring(1,payeeDetails.length() - 1);
      		 String payeeDetail = payeeDetails.replaceAll("\"", "");
      		 String newPayee[] = payeeDetail.split(":"); 
      		 payeeInfo = newPayee[1];
      		 
        }
        sb.append(START_ROW);
        sb.append(PAYEE_INFO_LABEL);
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(payeeInfo);
        sb.append(END_ROW);
        
        if(payeeInfo != null && payeeInfo.trim().equals("Participant") && withdrawalRequest.getPayDirectlyTomeAmount() != null){
        sb.append(START_ROW);
        sb.append(PAYMENT_AMOUNT);
        sb.append(StringUtils.repeat(NBSP, 3));
        sb.append(withdrawalRequest.getPayDirectlyTomeAmount());
        sb.append(END_ROW);
        }
        // Line 4
        sb.append(START_ROW);
        sb.append(DIVIDER_LINE);
        sb.append(END_ROW);
        
        return sb;
    }

    
}
