package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxType;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;

/**
 * WithdrawalRequest is the value object for the withdrawal request.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.7 2006/09/06 19:52:54
 */
public class WithdrawalRequest extends BaseWithdrawal {

    private static final Logger logger = Logger.getLogger(WithdrawalRequest.class);

    /**
     * FIELDS_TO_EXCLUDE_FROM_LOGGING.
     */
    private static final String[] FIELDS_TO_EXCLUDE_FROM_LOGGING = { "currentAdminToAdminNote",
            "currentAdminToParticipantNote", "originalAmountTypeCode",
            "originalParticipantLeavingPlanInd", "originalParticipantStateOfResidence",
            "originalPaymentTo", "originalReasonCode", "originalBirthDate",
            "readOnlyAdminToAdminNotes", "readOnlyAdminToParticipantNotes", "notes",
            "participantInfo", "contractInfo", "showStep1DriverFieldsChangedSinceSave",
            "federalTaxVo", "recalculationRequired", "trusteeAddress", "participantAddress",
            "ignoreErrors", "ignoreWarnings", "participantSSN", "partWithPbaMoneyInd",
            "spousalConsentRequired", "userRoleCode", "activityHistory", "principal",
            "vestingCouldNotBeCalculatedInd", "robustDateChangedAfterVesting",
            "requestInitiatedFromView", "vestingOverwriteInd", "vestingCriticalError",
            "vestingNonCriticalErrorWithWarning", "removeAllNotesOnSave", "activityHistoryEnabled", "pinEmailVerficationCode" };

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * SUBMISSION_CASE_TYPE_CODE_WITHDRAWAL.
     */
    public static final String SUBMISSION_CASE_TYPE_CODE_WITHDRAWAL = "W";

    /**
     * Defines the permitted number of recipients per request.
     */
    public static final IntRange RECIPIENT_RANGE = new IntRange(1, 1);

    /**
     * Unvested option code for leave in participant's account.
     */
    public static final String UNVESTED_LEAVE_IN_PARTICIPANT_ACCOUNT_CODE = "PA";

    /**
     * Unvested option code for transfer to contract's cash account.
     */
    public static final String UNVESTED_TRANSFER_TO_CASH_ACCOUNT_CODE = "CA";

    /**
     * Unvested option code for refund to trustee for deposit.
     */
    public static final String UNVESTED_REFUND_TO_TRUSTEE_CODE = "TR";

    /**
     * Payment To code for Direct to participant.
     */
    public static final String PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE = "PA";

    /**
     * Payment To code for Rollover all to IRA.
     */
    public static final String PAYMENT_TO_ROLLOVER_TO_IRA_CODE = "RI";
    
    /**
     * Payment To code for Rollover Roth IRA.
     */
    public static final String PAYMENT_TO_ROLLOVER_TO_ROTH_IRA_CODE = "RR";


    /**
     * Payment To code for Rollover all to John Hancock IRA.
     */
    public static final String PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE = "RJ";

    /**
     * Payment To code for Rollover all to another plan.
     */
    public static final String PAYMENT_TO_ROLLOVER_TO_PLAN_CODE = "RP";

    /**
     * Payment To code for After-tax contribution to participant, remainder to IRA.
     */
    public static final String PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE = "PI";

    /**
     * Payment To code for After-tax contribution to participant, remainder to plan.
     */
    public static final String PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE = "PP";

    /**
     * Payment To code for Plan Trustee.
     */
    public static final String PAYMENT_TO_PLAN_TRUSTEE_CODE = "TR";

    /**
     * Payment To code for Multiple destination
     */
    public static final String PAYMENT_TO_MULTIPLE_DESTINATION = "M";
    
    /**
     * IRA Service Provider Code for WMSI.
     */
    public static final String IRA_SERVICE_PROVIDER_WMSI_CODE = "W";

    /**
     * IRA Service Provider Code for Penchecks.
     */
    public static final String IRA_SERVICE_PROVIDER_PENCHECKS_CODE = "P";

    /**
     * IRA Service Provider Code for Neither (also known as Other).
     */
    public static final String IRA_SERVICE_PROVIDER_NEITHER_CODE = "N";
    
    /**
     * IRA Service Provider Code for JHMIRA.
     */
    public static final String IRA_SERVICE_PROVIDER_JHMIRA_CODE = "J";

    /**
     * Withdrawal Reason Code for Death.
     */
    public static final String WITHDRAWAL_REASON_DEATH_CODE = "DE";

    /**
     * Withdrawal Reason Code for Disability.
     */
    public static final String WITHDRAWAL_REASON_DISABILITY_CODE = "DI";

    /**
     * Withdrawal Reason Code for Hardship Withdrawal.
     */
    public static final String WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE = "HA";

    /**
     * Withdrawal Reason Code for EE Rollover Money.
     */
    public static final String WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE = "IR";

    /**
     * Withdrawal Reason Code for Minimum Distribution.
     */
    public static final String WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE = "MD";

    /**
     * Withdrawal Reason Code for Pre-Retirement Withdrawal.
     */
    public static final String WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE = "PD";

    /**
     * Withdrawal Reason Code for Retirement.
     */
    public static final String WITHDRAWAL_REASON_RETIREMENT_CODE = "RE";
    
    /**
     * irsDistributionCodeLoanClosure for 1.
     */
    public static final String IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_1 = "1";
    
    /**
     * irsDistributionCodeLoanClosure for 2.
     */
    public static final String IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_2 = "2";
    
    /**
     * irsDistributionCodeLoanClosure for 3.
     */
    public static final String IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_7 = "7";    

    /**
     * Withdrawal Reason Code for Termination of Employment.
     */
    public static final String WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE = "TE";

    /**
     * Withdrawal Reason Code for Mandatory Distribution Term.
     */
    public static final String WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE = "MT";

    /**
     * Withdrawal Reason Code for Withdrawal of Voluntary Contributions.
     */
    public static final String WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE = "VC";
    
    /**
     * Withdrawal Reason Code for Qualified domestic relations order.
     */
    public static final String WITHDRAWAL_REASON_QUALIFIED_DOMESTIC_RELATIONS_ORDER = "QD";
    
    /**
     * Withdrawal Reason Code for Excess Contributions.
     */
    public static final String WITHDRAWAL_REASON_EXCESS_CONTRIBUTIONS = "EC";
     
    /**
     * Withdrawal Reason Code for Excess Deferrals.
     */
    public static final String WITHDRAWAL_REASON_EXCESS_DEFERRALS = "ED";
     
    /**
     * Withdrawal Reason Code for Excess Annual Additions.
     */
    public static final String WITHDRAWAL_REASON_EXCESS_ANNUAL_ADDITIONS = "EA";
     
    /**
     * The maximum length for the withdrawal reason explanation.
     */
    public static final int MAXIMUM_REASON_EXPLANATION_LENGTH = 250;

    /**
     * Withdrawal Status Code for Draft.
     */
    public static final String WITHDRAWAL_STATUS_DRAFT_CODE = WithdrawalStateEnum.DRAFT
            .getStatusCode();

    /**
     * Withdrawal Status Code for Pending Review.
     */
    public static final String WITHDRAWAL_STATUS_PENDING_REVIEW_CODE = WithdrawalStateEnum.PENDING_REVIEW
            .getStatusCode();

    /**
     * Withdrawal Status Code for Pending Approval.
     */
    public static final String WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE = WithdrawalStateEnum.PENDING_APPROVAL
            .getStatusCode();

    /**
     * Withdrawal Status Code for Denied.
     */
    public static final String WITHDRAWAL_STATUS_DENIED_CODE = WithdrawalStateEnum.DENIED
            .getStatusCode();

    /**
     * Withdrawal Status Code for Deleted.
     */
    public static final String WITHDRAWAL_STATUS_DELETED_CODE = WithdrawalStateEnum.DELETED
            .getStatusCode();

    /**
     * Withdrawal Status Code for Expired.
     */
    public static final String WITHDRAWAL_STATUS_EXPIRED_CODE = WithdrawalStateEnum.EXPIRED
            .getStatusCode();

    /**
     * Withdrawal Status Code for Approved.
     */
    public static final String WITHDRAWAL_STATUS_APPROVED_CODE = WithdrawalStateEnum.APPROVED
            .getStatusCode();

    /**
     * Withdrawal Status code for Ready for Entry.
     */

    public static final String WITHDRAWAL_STATUS_READY_FOR_ENTRY_CODE = WithdrawalStateEnum.READY_FOR_ENTRY
            .getStatusCode();

    /**
     * Withdrawal Amount Code for Specific Amount.
     */
    public static final String WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE = "SA";

    /**
     * Withdrawal Amount Code for Maximum Available.
     */
    public static final String WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE = "MA";

    /**
     * Withdrawal Amount Code for Percentage Money type.
     */
    public static final String WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE = "MT";

    /**
     * Withdrawal Amount Code for Maxium elective differrel Money type.
     */
    public static final String WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE = "MD";
    
    /**
     * User role code for Participant.
     */
    public static final String USER_ROLE_PARTICIPANT_CODE = "PA";

    /**
     * User role code for Plan Sponsor.
     */
    public static final String USER_ROLE_PLAN_SPONSOR_CODE = "PS";

    /**
     * User role code for TPA.
     */
    public static final String USER_ROLE_TPA_CODE = "TP";

    /**
     * User role code for Bundled GA.
     */
    public static final String USER_ROLE_BUNDLED_GA_CODE = "JH";
    
    /**
     * Loan Option Code for Keep.
     */
    public static final String LOAN_KEEP_OPTION = "KP";

    /**
     * Loan Option Code for Closure.
     */
    public static final String LOAN_CLOSURE_OPTION = "CL";

    /**
     * Loan Option Code for Rollover to New Plan.
     */
    public static final String LOAN_ROLLOVER_OPTION = "RO";

    /**
     * Loan Option Code for Repay.
     */
    public static final String LOAN_REPAY_OPTION = "RP";

    /**
     * The TAX_PERCENTAGE_SCALE.
     */
    public static final int TAX_PERCENTAGE_SCALE = 4;

    /**
     * Participant status codes that are partial.
     */
    public static final String[] PARTIAL_PARTICIPANT_STATUS_CODES = new String[] { "AP", "BP",
            "DP", "RP", "TP" };

    /**
     * Participant status codes that are considered fully vested.
     */
    public static final String[] FULLY_VESTED_PARTICIPANT_STATUS_CODES = new String[] { "BP", "DP",
            "RP", "TP", "BU", "DU", "TU" };

    /**
     * Boundary for displaying the tax withholding section based on total requested withdrawal
     * amount.
     */
    public static final BigDecimal DISPLAY_TAX_WITHHOLDING_SECTION_BOUNDARY = new BigDecimal(
            "200.00");

    /**
     * Employee Status Code for Termination.
     */
    public static final String EMPLOYEE_STATUS_TERMINATION_CODE = "T";

    /**
     * Employee Status Code for Retired.
     */
    public static final String EMPLOYEE_STATUS_RETIRED_CODE = "R";

    /**
     * Employee Status Code for Disabled.
     */
    public static final String EMPLOYEE_STATUS_DISABLED_CODE = "P";

    /**
     * Number of years before or after today, deemed valid for a lastContributionDate.
     */
    private static final int VALIDATION_AGE = 200;

    // Loan Option Codes

    /**
     * LOAN_OPTION_CODE_CLOSURE.
     */
    public static final String LOAN_OPTION_CODE_CLOSURE = "CL";

    /**
     * LOAN_OPTION_CODE_ROLLOVER.
     */
    public static final String LOAN_OPTION_CODE_ROLLOVER = "RO";

    /**
     * LOAN_OPTION_CODE_REPAY.
     */
    public static final String LOAN_OPTION_CODE_REPAY = "RP";

    /**
     * LOAN_OPTION_CODE_KEEP.
     */
    public static final String LOAN_OPTION_CODE_KEEP = "KP";

    // IRS Loan closure codes for Loan Options

    /**
     * IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF.
     */
    public static final String IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF = "1M";

    /**
     * IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION.
     */
    public static final String IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION = "2M";

    /**
     * IRS_DISTRIBUTION_LOAN_CODE_DISABILITY.
     */
    public static final String IRS_DISTRIBUTION_LOAN_CODE_DISABILITY = "3  ";

    /**
     * IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION.
     */
    public static final String IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION = "G  ";

    /**
     * IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF.
     */
    public static final String IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF = "7M";

    /**
     * Minimum value for withdrawal amounts.
     */
    public static final BigDecimal WITHDRAWAL_AMOUNT_MINIMUM = new BigDecimal("0.01");

    /**
     * Maximum value for withdrawal amounts.
     */
    public static final BigDecimal SPECIFIC_AMOUNT_DATABASE_FIELD_LIMIT = new BigDecimal(
            "999999999.99");

    /**
     * Modes for mode specific functions.
     */
    public enum Mode {
        INITIATE_STEP_1, INITIATE_STEP_2, REVIEW_AND_APPROVE;
    };

    private static final String NAME_SEPARATOR = " ";

    private static final int STEP_1_MODE_INDEX = 0;

    private static final int STEP_2_MODE_INDEX = 1;

    private static final int REVIEW_MODE_INDEX = 2;

    /**
     * The Plan Sponsor/TPA legalese documents.
     */
    public static final String CMA_SITE_CODE_PSW = "PS";

    /**
     * The participant legalese documents.
     */
    public static final String CMA_SITE_CODE_EZK = "PA";

    /*
     * Used for setting the default requested amount percentage.
     */
    private String originalAmountTypeCode;

    private String amountTypeCode;

    private Date birthDate;

    private Integer contractId;

    private String contractName;

    private String contractIssuedStateCode;

    private String cmaSiteCode;

    private Date deathDate;

    private Collection<Declaration> declarations = new ArrayList<Declaration>();

    private Date disabilityDate;

    private Date expirationDate;

    private Date defaultFinalContributionDateForWmsiPenChecks;

    private Collection<Fee> fees = new ArrayList<Fee>();

    private String firstName;

    private String hardshipReasons;

    private String irsDistributionCodeLoanClosure;

    private Boolean ignoreWarnings = Boolean.FALSE;

    private Boolean forceData = Boolean.FALSE;

    private boolean vestingCouldNotBeCalculatedInd = false;

    private Boolean ignoreErrors = Boolean.FALSE;

    private Date finalContributionDate;

    private String lastName;

    private String loanOption;

    private String loanRequestId;

    private boolean[] initialMessagesLoaded = new boolean[] { false, false, false };

    private Boolean isLegaleseConfirmed = null;;

    private Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>(
            0);

    private Date mostRecentPriorContributionDate;

    private Collection<WithdrawalRequestNote> readOnlyAdminToAdminNotes = new ArrayList<WithdrawalRequestNote>(
            0);

    private WithdrawalRequestNote currentAdminToAdminNote = new WithdrawalRequestNote();

    private Collection<WithdrawalRequestNote> readOnlyAdminToParticipantNotes = new ArrayList<WithdrawalRequestNote>(
            0);

    private WithdrawalRequestNote currentAdminToParticipantNote = new WithdrawalRequestNote();

    private Collection<Note> notes = new ArrayList<Note>(0);

    private Integer participantId;

    private Boolean participantLeavingPlanInd;

    private boolean robustDateChangedAfterVesting = false;

    private Boolean requestInitiatedFromView;

    private Boolean activityHistoryEnabled;

    /**
     * Originial value of the participant leaving plan indicator field - used to determine if the
     * participant leaving plan indicator field has been modified since the last save.
     */
    private Boolean originalParticipantLeavingPlanInd;

    private String participantSSN;

    private String participantStateOfResidence;

    /**
     * Original value of the state of residence field - used to determine if the state of residence
     * field has been modified since the last save.
     */
    private String originalParticipantStateOfResidence;

    /**
     * Original state tax type code - used to determine if the state tax type code has been
     * modified.
     */
    private StateTaxType originalStateTaxType;

    private String paymentTo;

    /**
     * Originial value of the payment to field - used to determine if the payment to field has been
     * modified since the last save.
     */
    private String originalPaymentTo;

    /**
     * Holds the employee profile Id.
     */
    private Integer employeeProfileId;

    /**
     * Withdrawal reason code.
     * 
     * DB ref: SUBMISSION.SUBMISSION_WITHDRAWAL.WITHDRAWAL_REASON_CODE
     */
    private String reasonCode;

    /**
     * Original Withdrawal reason code - this field is used to determine if the value of the reason
     * code has been modified since the last save.
     */
    private String originalReasonCode;

    /**
     * Detail reason code set if the selected reasonCode requires further specification. Currently
     * used when reasonCode is HA - Hardship to specify hardship categories
     * 
     * DB ref: SUBMISSION.SUBMISSION_WITHDRAWAL.WITHDRAWAL_REASON_DETAIL_CODE
     */
    private String reasonDetailCode;

    /**
     * Used only in conjunction with the reasonDetailCode to provide a free form explanation of the
     * detailed reason.
     * 
     * DB ref: SUBMISSION.SUBMISSION_WITHDRAWAL.WITHDRAWAL_REASON_EXPLANATION
     */
    private String reasonDescription;

    private Collection<Recipient> recipients = new ArrayList<Recipient>();

    private Date requestDate;

    private Date retirementDate;

    private String statusCode;

    private Timestamp statusLastChanged;

    private String requestType = SUBMISSION_CASE_TYPE_CODE_WITHDRAWAL; // default WithdrawalRequest

    private Date terminationDate;

    private String unvestedAmountOptionCode;

    private BigDecimal withdrawalAmount;

    private Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();

    private String userRoleCode;

    private Boolean vestingCalledInd;

    private Boolean vestingOverwriteInd;

    private Boolean vestingCriticalError = false;

    private Boolean vestingNonCriticalErrorWithWarning = false;

    private String loan1099RName;

    private String iraServiceProviderCode;

    private Date expectedProcessingDate;

    private Timestamp approvedTimestamp;

    private Boolean partWithPbaMoneyInd;

    private boolean isNoLongerValid = false;

    private boolean isStateTaxValid = true;

    private boolean isCensusInfoAvailable = false;
    
    private boolean isCensusInfoAvailablePDInd = false;
    
    private boolean eedefFlag;

	/**
     * Specifies if spousal consent is required.
     */
    private Boolean spousalConsentRequired;

    /**
     * Specifies if the WD has P/C data. It is set to true if there is P/C data available for the
     * (profileId, contractId) pair.
     */
    private Boolean hasPCData;

    /**
     * Participant's employment status code.
     * 
     * @see com.manulife.pension.service.employee.valueobjec.EmployeeDetailVO#employeeStatusCode_
     */
    private String emplStatusCode;

    /**
     * Participant's employment status effective date.
     * 
     * @see com.manulife.pension.service.employee.valueobjec.EmployeeDetailVO#employeeStatusEffDate
     */
    private Date emplStatusEffectiveDate;

    /**
     * Participant's address.
     */
    private DistributionAddress participantAddress = new Address();

    /**
     * Trustee's address.
     */
    private Address trusteeAddress = new Address();

    /**
     * Participant Withdrawal information retrieved from the CSDB database.
     */
    private ParticipantInfo participantInfo = new ParticipantInfo();

    private ContractInfo contractInfo = new ContractInfo();

    /**
     * Used to determine if message should be displayed convering step 1 fields being changed any
     * time since the last save (even if changed back to original values).
     */
    private boolean showStep1DriverFieldsChangedSinceSave = false;

    private FederalTaxVO federalTaxVo;

    /**
     * Used to determine if a recalculation is required before saving.
     */
    private boolean recalculationRequired = true;

    private ActivityHistory activityHistory = null;

    /**
     * stores the legalese information shown on the withdrawal request.
     */
    private LegaleseInfo legaleseInfo;

    /**
     * stores the participant legalese information shown on the withdrawal request.
     */
    private LegaleseInfo participantLegaleseInfo;

    private BigDecimal lastFeeChangeByTPAUserID;

    private boolean lastFeeChangeWasPSUserInd;

    /**
     * Holds the principal object.
     */
    private Principal principal;

    /**
     * This field indicates to the DAO that we need to remove notes on save, then insert the current
     * contents.
     */
    private boolean removeAllNotesOnSave;

    /**
     * This field indicates if the request is participant created or not.
     */
    private boolean isParticipantCreated;

    /**
     * This indicator is true if the withdrawal request is participant initiated that is at risk.
     */
    private boolean requestRiskIndicator;
    
    
    private String legallyMarriedInd;
    
    private String participant;
	private String taxableBal;
	private String participantAftrTax;
	private String participantRoth;
	private String participantNonRoth;
	private String rothBal;
	
	
	/* save the payeeInfo */
	private String participantDetails;
	private String taxableParticipantInfo;
	private String nonTaxableParticipantInfo;
	private String rothParticaipantInfo;

    /**
     * This stores the at risk address to where the PIN was sent.
     */
    private Address atRiskAddress = new Address();
    
    private AtRiskDetailsVO atRiskDetailsVO = null;
    
	/**
     * This stores the verification Code for pin email
     */
    private String pinEmailVerficationCode = StringUtils.EMPTY;

    /**
     * latestVestingEffectiveDate holds the value of the latest effective date for the given output
     * from the vesting engine. It's only set for provide output from vesting.
     */
    private Date latestVestingEffectiveDate;
    
    private ContractPermission contractPermission;
    
	private String payDirectlyTome;
	private BigDecimal payDirectlyTomeAmount;
	private String validatePA;
	private String validatePAAT;
	private String validatePAR;
	/**
	 * @return the validatePA
	 */
	public String getValidatePA() {
		return validatePA;
	}

	/**
	 * @param validatePA the validatePA to set
	 */
	public void setValidatePA(String validatePA) {
		this.validatePA = validatePA;
	}

	/**
	 * @return the validatePAAT
	 */
	public String getValidatePAAT() {
		return validatePAAT;
	}

	/**
	 * @param validatePAAT the validatePAAT to set
	 */
	public void setValidatePAAT(String validatePAAT) {
		this.validatePAAT = validatePAAT;
	}

	/**
	 * @return the validatePAR
	 */
	public String getValidatePAR() {
		return validatePAR;
	}

	/**
	 * @param validatePAR the validatePAR to set
	 */
	public void setValidatePAR(String validatePAR) {
		this.validatePAR = validatePAR;
	}

	
	
	private String participantTaxesFlag;
	private String taxableFlag;
	private String nontaxableFlag;
	private String rothTaxesFlag;
	
	private String traditionalIRAFlag;
	private String rothIRAFlag;
	private String empQulifiedPlanFlag;
	
	private String traditionalIRAPayee;
	private String rothIRAPayee;
	private String empQulifiedPlanPayee;
	
	private boolean totalRothBalFlag;
	private boolean nonTaxableFlag;
	
	private boolean isRolloverTypeEligible  = false; 
	
	//Hardship withdrawals
	private BigDecimal minimumHarshipAmount;
	private BigDecimal maximumHarshipAmount;
	private Collection<String> avilableHarshipMoneyType;


	private String rbCategory;
    private String tbCategory;
    private String nratCategory;
    // for error mark multipayee
    private String missingRollover;
    private String selectOnePayee;
    /**
   	 * @return the missingRollover
   	 */
   	public String getMissingRollover() {
   		return missingRollover;
   	}

   	/**
   	 * @param missingRollover the missingRollover to set
   	 */
   	public void setMissingRollover(String missingRollover) {
   		this.missingRollover = missingRollover;
   	}

   	/**
   	 * @return the selectOnePayee
   	 */
   	public String getSelectOnePayee() {
   		return selectOnePayee;
   	}

   	/**
   	 * @param selectOnePayee the selectOnePayee to set
   	 */
   	public void setSelectOnePayee(String selectOnePayee) {
   		this.selectOnePayee = selectOnePayee;
   	}


	/**
	 * @return the payDirectlyTomeAmount
	 */
	public BigDecimal getPayDirectlyTomeAmount() {
		return payDirectlyTomeAmount;
	}

	/**
	 * @param payDirectlyTomeAmount the payDirectlyTomeAmount to set
	 */
	public void setPayDirectlyTomeAmount(BigDecimal payDirectlyTomeAmount) {
		this.payDirectlyTomeAmount = payDirectlyTomeAmount;
	}

	public String getPayDirectlyTome() {
		return payDirectlyTome;
	}

	public void setPayDirectlyTome(String payDirectlyTome) {
		this.payDirectlyTome = payDirectlyTome;
	}

	public ContractPermission getContractPermission() {
		return contractPermission;
	}

	public void setContractPermission(ContractPermission contractPermission) {
		this.contractPermission = contractPermission;
	}

	/**
     * @return the isStateTaxValid
     */
    public boolean getIsStateTaxValid() {
        return isStateTaxValid;
    }

    /**
     * @param isStateTaxValid the isStateTaxValid to set
     */
    public void setIsStateTaxValid(final boolean isStateTaxValid) {
        this.isStateTaxValid = isStateTaxValid;
    }
    
    /**
     * @return the isCensusInfoAvailable
     */
    public boolean getIsCensusInfoAvailable() {
        return isCensusInfoAvailable;
    }

    /**
     * @param isCensusInfoAvailable the isCensusInfoAvailable to set
     */
    public void setIsCensusInfoAvailable(final boolean isCensusInfoAvailable) {
        this.isCensusInfoAvailable = isCensusInfoAvailable;
    }

    /**
     * @return the isParticipantCreated
     */
    public boolean getIsParticipantCreated() {
        return isParticipantCreated;
    }

    /**
     * @param isParticipantCreated the isParticipantCreated to set
     */
    public void setIsParticipantCreated(final boolean isParticipantCreated) {
        this.isParticipantCreated = isParticipantCreated;
    }

    /**
     * Timestamp when the submission case was last updated.
     */
    private Timestamp submissionCaseLastUpdated;

    /**
     * @return the approved timestamp
     */
    public final Timestamp getApprovedTimestamp() {
        return approvedTimestamp;
    }

    /**
     * @param approvedTimestamp the approved timestamp
     */
    public final void setApprovedTimestamp(final Timestamp approvedTimestamp) {
        this.approvedTimestamp = approvedTimestamp;
    }

    /**
     * Constructor for WithdrawalRequest.
     */
    public WithdrawalRequest() {
        super();

        // Set default Federal Tax.
        federalTaxVo = new FederalTaxVO();

        // Set default notes
        setCurrentAdminToAdminNote(new WithdrawalRequestNote() {
            private static final long serialVersionUID = 1L;
            {
                this.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
            };
        });
        setCurrentAdminToParticipantNote(new WithdrawalRequestNote() {
            private static final long serialVersionUID = 1L;
            {
                this.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
            };
        });
    }

    /**
     * @return the amountTypeCode
     */
    public String getAmountTypeCode() {
        return amountTypeCode;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Originial value of the birth date field - used to determine if the birth date field has been
     * modified since the last save.
     */
    private Date originalBirthDate;

    /**
     * WITHDRAWAL_ACTIVITY_HISTORY_ENABLED_KEY is the location in the configuration where the
     * setting for enabling activity history is stored.
     */
    public static final String WITHDRAWAL_ACTIVITY_HISTORY_ENABLED_KEY = "withdrawal.activityHistory.enabled";

	public static final String TRADTIONAL_IRA = "Traditional IRA";

	public static final String ROTH_IRA = "Roth IRA";

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }
    
    /**
	 * @return the contractIssuedStateCode
	 */
	public String getContractIssuedStateCode() {
		return contractIssuedStateCode;
	}

    /**
     * @return the contractName
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * @return the deathDate
     */
    public Date getDeathDate() {
        return deathDate;
    }

    /**
     * @return the declarations
     */
    public Collection<Declaration> getDeclarations() {
        return declarations;
    }

    /**
     * @return the disabilityDate
     */
    public Date getDisabilityDate() {
        return disabilityDate;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @return the reason detail code
     */
    public String getReasonDetailCode() {

        return reasonDetailCode;
    }

    /**
     * @param reasonDetailCode the reason detail code
     */
    public void setReasonDetailCode(final String reasonDetailCode) {
        this.reasonDetailCode = reasonDetailCode;
    }

    /**
     * @return the fees
     */
    public Collection<Fee> getFees() {
        return fees;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the hardshipReasons
     */
    public String getHardshipReasons() {
        return hardshipReasons;
    }

    /**
     * @return the irsDistributionCodeLoanClosure
     */
    public String getIrsDistributionCodeLoanClosure() {
        return irsDistributionCodeLoanClosure;
    }

    /**
     * @return the finalContributionDate
     */
    public Date getFinalContributionDate() {
        return finalContributionDate;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the loanOption
     */
    public String getLoanOption() {
        return loanOption;
    }

    /**
     * @return the loanRequestId
     */
    public String getLoanRequestId() {
        return loanRequestId;
    }

    /**
     * @return the moneyTypes
     */
    public Collection<WithdrawalRequestMoneyType> getMoneyTypes() {
        return moneyTypes;
    }

    /**
     * @return the mostRecentPriorContributionDate
     */
    public Date getMostRecentPriorContributionDate() {
        return mostRecentPriorContributionDate;
    }

    /**
     * @return the readOnlyAdminToAdminNotes
     */
    public Collection<WithdrawalRequestNote> getReadOnlyAdminToAdminNotes() {
        return readOnlyAdminToAdminNotes;
    }

    /**
     * @return the participantId
     */
    public Integer getParticipantId() {
        return participantId;
    }

    /**
     * @return the participantLeavingPlanInd
     */
    public Boolean getParticipantLeavingPlanInd() {
        return participantLeavingPlanInd;
    }

    /**
     * Retrieves the participant name which is the first name and last name concatenated and
     * separated by a single white space character.
     * 
     * @return String - The participant name constructed from the first and last name.
     */
    public String getParticipantName() {
        return new StringBuffer(StringUtils.defaultString(getFirstName())).append(NAME_SEPARATOR)
                .append(StringUtils.defaultString(getLastName())).toString();
    }

    /**
     * @return the participantSSN
     */
    public String getParticipantSSN() {
        return participantSSN;
    }

    /**
     * @return the participantStateOfResidence
     */
    public String getParticipantStateOfResidence() {
        return participantStateOfResidence;
    }

    /**
     * @return the paymentTo
     */
    public String getPaymentTo() {
        return paymentTo;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @return the reasonDescription
     */
    public String getReasonDescription() {
        return reasonDescription;
    }

    /**
     * @return the recipients
     */
    public Collection<Recipient> getRecipients() {
        return recipients;
    }

    /**
     * @return the requestDate
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @return the request Type (W or L)
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @return the terminationDate
     */
    public Date getTerminationDate() {
        return terminationDate;
    }

    /**
     * @return the unvestedAmountOptionCode
     */
    public String getUnvestedAmountOptionCode() {
        return unvestedAmountOptionCode;
    }

    /**
     * @return the withdrawalAmount
     */
    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    /**
     * 
     * @return the loans
     */

    public Collection<WithdrawalRequestLoan> getLoans() {
        return loans;
    }

    /**
     * @param amountTypeCode the amountTypeCode to set
     */
    public void setAmountTypeCode(final String amountTypeCode) {
        this.amountTypeCode = amountTypeCode;
    }

    /**
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(final Integer contractId) {
        this.contractId = contractId;
    }
    
    /**
	 * @param contractIssuedStateCode the contractIssuedStateCode to set
	 */
	public void setContractIssuedStateCode(String contractIssuedStateCode) {
		this.contractIssuedStateCode = contractIssuedStateCode;
	}

    /**
     * @param contractName the contractName to set
     */
    public void setContractName(final String contractName) {
        this.contractName = contractName;
    }

    /**
     * @param deathDate the deathDate to set
     */
    public void setDeathDate(final Date deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * @param declarations the declarations to set
     */
    public void setDeclarations(final Collection<Declaration> declarations) {

        this.declarations = (declarations == null) ? new ArrayList<Declaration>() : declarations;
    }

    /**
     * @param disabilityDate the disabilityDate to set
     */
    public void setDisabilityDate(final Date disabilityDate) {
        this.disabilityDate = disabilityDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @param fees the fees to set
     */
    public void setFees(final Collection<Fee> fees) {
        this.fees = (fees == null) ? new ArrayList<Fee>() : fees;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(final String firstName) {
        this.firstName = StringUtils.trimToEmpty(firstName);
    }

    /**
     * @param hardshipReasons the hardshipReasons to set
     */
    public void setHardshipReasons(final String hardshipReasons) {
        this.hardshipReasons = hardshipReasons;
    }

    /**
     * @param irsDistributionCodeLoanClosure the irsDistributionCodeLoanClosure to set
     */
    public void setIrsDistributionCodeLoanClosure(final String irsDistributionCodeLoanClosure) {
        this.irsDistributionCodeLoanClosure = irsDistributionCodeLoanClosure;
    }

    /**
     * @param finalContributionDate the finalContributionDate to set
     */
    public void setFinalContributionDate(final Date finalContributionDate) {
        this.finalContributionDate = filterInvalidApolloDates(finalContributionDate);
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(final String lastName) {
        this.lastName = StringUtils.trimToEmpty(lastName);
    }

    /**
     * @param loanOption the loanOption to set
     */
    public void setLoanOption(final String loanOption) {
        this.loanOption = loanOption;
    }

    /**
     * @param loanRequestId the loanRequestId to set
     */
    public void setLoanRequestId(final String loanRequestId) {
        this.loanRequestId = loanRequestId;
    }

    /**
     * @return the statusLastChanged
     */
    public Timestamp getStatusLastChanged() {
        return statusLastChanged;
    }

    /**
     * @param statusLastChanged the statusLastChanged to set
     */
    public void setStatusLastChanged(final Timestamp statusLastChanged) {
        this.statusLastChanged = statusLastChanged;
    }

    /**
     * @param moneyTypes the moneyTypes to set
     */
    public void setMoneyTypes(final Collection<WithdrawalRequestMoneyType> moneyTypes) {
        this.moneyTypes = (moneyTypes == null) ? new ArrayList<WithdrawalRequestMoneyType>()
                : moneyTypes;
    }

    /**
     * @param mostRecentPriorContributionDate the mostRecentPriorContributionDate to set
     */
    public void setMostRecentPriorContributionDate(final Date mostRecentPriorContributionDate) {
        this.mostRecentPriorContributionDate = filterInvalidApolloDates(mostRecentPriorContributionDate);
    }

    /**
     * @param readOnlyAdminToAdminNotes the readOnlyAdminToAdminNotes to set
     */
    public void setReadOnlyAdminToAdminNotes(
            final Collection<WithdrawalRequestNote> readOnlyAdminToAdminNotes) {
        if (readOnlyAdminToAdminNotes == null) {
            this.readOnlyAdminToAdminNotes = new ArrayList<WithdrawalRequestNote>(0);
        } else {
            this.readOnlyAdminToAdminNotes = readOnlyAdminToAdminNotes;
        } // fi
    }

    /**
     * @param participantId the participantId to set
     */
    public void setParticipantId(final Integer participantId) {
        this.participantId = participantId;
    }

    /**
     * @param participantLeavingPlanInd the participantLeavingPlanInd to set
     */
    public void setParticipantLeavingPlanInd(final Boolean participantLeavingPlanInd) {
        this.participantLeavingPlanInd = participantLeavingPlanInd;
    }

    /**
     * @param participantSSN the participantSSN to set
     */
    public void setParticipantSSN(final String participantSSN) {
        this.participantSSN = participantSSN;
    }

    /**
     * @param participantStateOfResidence the participantStateOfResidence to set
     */
    public void setParticipantStateOfResidence(final String participantStateOfResidence) {
        this.participantStateOfResidence = participantStateOfResidence;
    }

    /**
     * @param paymentTo the paymentTo to set
     */
    public void setPaymentTo(final String paymentTo) {
        this.paymentTo = paymentTo;
    }

    /**
     * @param reasonCode the reasonCode to set
     */
    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @param reasonDescription the reasonDescription to set
     */
    public void setReasonDescription(final String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(final Collection<Recipient> recipients) {

        this.recipients = (recipients == null) ? new ArrayList<Recipient>() : recipients;
    }

    /**
     * @param requestDate the requestDate to set
     */
    public void setRequestDate(final Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(final String requestType) {
        this.requestType = requestType;
    }

    /**
     * @param terminationDate the terminationDate to set
     */
    public void setTerminationDate(final Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    /**
     * @param unvestedAmountOptionCode the unvestedAmountOptionCode to set
     */
    public void setUnvestedAmountOptionCode(final String unvestedAmountOptionCode) {
        this.unvestedAmountOptionCode = unvestedAmountOptionCode;
    }

    /**
     * @param withdrawalAmount the withdrawalAmount to set
     */
    public void setWithdrawalAmount(final BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    /**
     * @param loans the loan details to set
     */
    public void setLoans(final Collection<WithdrawalRequestLoan> loans) {
        this.loans = (loans == null) ? new ArrayList<WithdrawalRequestLoan>() : loans;
    }

    /**
     * @return The userRoleCode
     */
    public String getUserRoleCode() {
        return userRoleCode;
    }

    /**
     * @param userRoleCode the type of user
     */
    public void setUserRoleCode(final String userRoleCode) {
        this.userRoleCode = userRoleCode;
    }

    /**
     * @return the vesting indicator
     */
    public Boolean getVestingOverwriteInd() {

        return vestingOverwriteInd;
    }

    /**
     * @return the 1099R loan name
     */
    public String getLoan1099RName() {

        return loan1099RName;
    }

    /**
     * @return the ira service provider code
     */
    public String getIraServiceProviderCode() {

        return iraServiceProviderCode;
    }

    /**
     * @param iraServiceProviderCode the ira service provider code
     */
    public void setIraServiceProviderCode(final String iraServiceProviderCode) {
        this.iraServiceProviderCode = iraServiceProviderCode;
    }

    /**
     * @param loan1099RName the 1099R loan name
     */
    public void setLoan1099RName(final String loan1099RName) {
        this.loan1099RName = loan1099RName;
    }

    /**
     * @param vestingVerwriteInd the vesting indicator
     */
    public void setVestingOverwriteInd(final Boolean vestingVerwriteInd) {
        this.vestingOverwriteInd = vestingVerwriteInd;
    }

    /**
     * @return the expected processing date
     */
    public Date getExpectedProcessingDate() {
        return expectedProcessingDate;
    }

    /**
     * @param expectedProcessingDate the expected processing date
     */
    public void setExpectedProcessingDate(final Date expectedProcessingDate) {
        this.expectedProcessingDate = expectedProcessingDate;
    }

    /**
     * @return the readOnlyAdminToParticipantNotes
     */
    public Collection<WithdrawalRequestNote> getReadOnlyAdminToParticipantNotes() {
        return readOnlyAdminToParticipantNotes;
    }

    /**
     * @param readOnlyAdminToParticipantNotes the readOnlyAdminToParticipantNotes to set
     */
    public void setReadOnlyAdminToParticipantNotes(
            final Collection<WithdrawalRequestNote> readOnlyAdminToParticipantNotes) {
        if (readOnlyAdminToParticipantNotes == null) {
            this.readOnlyAdminToParticipantNotes = new ArrayList<WithdrawalRequestNote>(0);
        } else {
            this.readOnlyAdminToParticipantNotes = readOnlyAdminToParticipantNotes;
        } // fi
    }

    /**
     * @return the currentAdminToAdminNote
     */
    public WithdrawalRequestNote getCurrentAdminToAdminNote() {
        return currentAdminToAdminNote;
    }

    /**
     * @param currentAdminToAdminNote the currentAdminToAdminNote to set
     */
    public void setCurrentAdminToAdminNote(final WithdrawalRequestNote currentAdminToAdminNote) {
        if (currentAdminToAdminNote == null) {
            this.currentAdminToAdminNote = new WithdrawalRequestNote();
        } else {
            this.currentAdminToAdminNote = currentAdminToAdminNote;
        } // fi
        this.currentAdminToAdminNote
                .setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE);
    }

    /**
     * @return the currentAdminToParticipantNote
     */
    public WithdrawalRequestNote getCurrentAdminToParticipantNote() {
        return currentAdminToParticipantNote;
    }

    /**
     * @param currentAdminToParticipantNote the currentAdminToParticipantNote to set
     */
    public void setCurrentAdminToParticipantNote(
            final WithdrawalRequestNote currentAdminToParticipantNote) {
        if (currentAdminToParticipantNote == null) {
            this.currentAdminToParticipantNote = new WithdrawalRequestNote();
        } else {
            this.currentAdminToParticipantNote = currentAdminToParticipantNote;
        } // fi
        this.currentAdminToParticipantNote
                .setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
    }

    // NOTE: Getter/Setters for the transient fields
    /**
     * Returns the value of the has "Spousal consent required" flag.
     * 
     * @return true if the contract has "Spousal consent required" feature false otherwise
     */
    public Boolean getSpousalConsentRequired() {
        return spousalConsentRequired;
    }

    /**
     * Sets the "Spousal consent required" flag.
     * 
     * @param consentRequired The value to set.
     */
    public void setSpousalConsentRequired(final Boolean consentRequired) {
        this.spousalConsentRequired = consentRequired;
    }

    /**
     * Returns the value of the has P/C Data flag.
     * 
     * @return true if the participant has Planet Census data false otherwise
     */
    public Boolean getHasPCData() {
        return hasPCData;
    }

    /**
     * Sets the has P/C Data flag.
     * 
     * @param hasPCData The value to set.
     */
    public void setHasPCData(final Boolean hasPCData) {
        this.hasPCData = hasPCData;
    }

    /**
     * Returns the participant's employment status code.
     * 
     * @return Participant's employment status effective date.
     */
    public String getEmplStatusCode() {
        return emplStatusCode;
    }

    /**
     * Sets the participant's employment status code.
     * 
     * @param emplStatusCode Participant's employment status code.
     */
    public void setEmplStatusCode(final String emplStatusCode) {
        this.emplStatusCode = emplStatusCode;
    }

    /**
     * Returns the participant's employment status effective date.
     * 
     * @return Participant's employment status effective date.
     */
    public Date getEmplStatusEffectiveDate() {
        return emplStatusEffectiveDate;
    }

    /**
     * Sets the participant's employment status effective date.
     * 
     * @param emplStatusEffectiveDate Participant's employment status effective date.
     */
    public void setEmplStatusEffectiveDate(final Date emplStatusEffectiveDate) {
        this.emplStatusEffectiveDate = emplStatusEffectiveDate;
    }

    /**
     * Returns the participant's address if available (hasPCData = true), null otherwise.
     * 
     * @return Address - The Participant's address.
     */
    public DistributionAddress getParticipantAddress() {
        return participantAddress;
    }

    /**
     * Sets the Participant's address.
     * 
     * @param participantAddress The value to set.
     */
    public void setParticipantAddress(final DistributionAddress participantAddress) {
        this.participantAddress = (participantAddress == null) ? new Address() : participantAddress;
    }

    /**
     * Returns the trustee's address if available.
     * 
     * @return Address - The Trustee's address.
     */
    public Address getTrusteeAddress() {
        return trusteeAddress;
    }

    /**
     * Sets the Trustee's address.
     * 
     * @param trusteeAddress The value to set.
     */
    public void setTrusteeAddress(final Address trusteeAddress) {
        this.trusteeAddress = (trusteeAddress == null) ? new Address() : trusteeAddress;
    }

    /**
     * Returns the participant withdrawal information.
     * 
     * @return ParticipantInfo The data.
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo
     */

    public ParticipantInfo getParticipantInfo() {
        return participantInfo;
    }

    /**
     * Sets the participant withdrawal information for UI consumption.
     * 
     * @param participantInfo The value to set.
     */
    public void setParticipantInfo(final ParticipantInfo participantInfo) {
        if (participantInfo == null) {
            this.participantInfo = new ParticipantInfo();
        } else {
            this.participantInfo = participantInfo;
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {

        // Check base error codes
        if (CollectionUtils.isNotEmpty(getErrorCodes())) {
            return true;
        }
        // Check notes
        if ((getCurrentAdminToAdminNote() != null)
                && (getCurrentAdminToAdminNote().doErrorCodesExist())) {
            return true;
        }
        if ((getCurrentAdminToParticipantNote() != null)
                && (getCurrentAdminToParticipantNote().doErrorCodesExist())) {
            return true;
        }

        // Check declarations
        for (Declaration declaration : getDeclarations()) {
            if (((WithdrawalRequestDeclaration) declaration).doErrorCodesExist()) {
                return true;
            }
        }

        // Check loans
        for (WithdrawalRequestLoan loan : getLoans()) {
            if (loan.doErrorCodesExist()) {
                return true;
            }
        }

        // Check fees
        for (Fee fee : getFees()) {
            if (((WithdrawalRequestFee) fee).doErrorCodesExist()) {
                return true;
            }
        }

        // Check money types
        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
            if (moneyType.doErrorCodesExist()) {
                return true;
            }
        }

        // Check recipients
        for (Recipient recipient : getRecipients()) {
            if (((WithdrawalRequestRecipient) recipient).doErrorCodesExist()) {
                return true;
            }
        }

        // No errors exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {

        // Check base warning codes
        if (CollectionUtils.isNotEmpty(getWarningCodes())) {
            return true;
        }
        // Check notes
        if ((getCurrentAdminToAdminNote() != null)
                && (getCurrentAdminToAdminNote().doWarningCodesExist())) {
            return true;
        }
        if ((getCurrentAdminToParticipantNote() != null)
                && (getCurrentAdminToParticipantNote().doWarningCodesExist())) {
            return true;
        }

        // Check declarations
        for (Declaration declaration : getDeclarations()) {
            if (((WithdrawalRequestDeclaration) declaration).doWarningCodesExist()) {
                return true;
            }
        }

        // Check loans
        for (WithdrawalRequestLoan loan : getLoans()) {
            if (loan.doWarningCodesExist()) {
                return true;
            }
        }

        // Check fees
        for (Fee fee : getFees()) {
            if (((WithdrawalRequestFee) fee).doWarningCodesExist()) {
                return true;
            }
        }

        // Check money types
        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
            if (moneyType.doWarningCodesExist()) {
                return true;
            }
        }

        // Check recipients
        for (Recipient recipient : getRecipients()) {
            if (((WithdrawalRequestRecipient) recipient).doWarningCodesExist()) {
                return true;
            }
        }

        // No warnings exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {

        // Check base alert codes
        if (CollectionUtils.isNotEmpty(getAlertCodes())) {
            return true;
        }
        // Check notes
        if ((getCurrentAdminToAdminNote() != null)
                && (getCurrentAdminToAdminNote().doAlertCodesExist())) {
            return true;
        }
        if ((getCurrentAdminToParticipantNote() != null)
                && (getCurrentAdminToParticipantNote().doAlertCodesExist())) {
            return true;
        }

        // Check declarations
        for (Declaration declaration : getDeclarations()) {
            if (((WithdrawalRequestDeclaration) declaration).doAlertCodesExist()) {
                return true;
            }
        }

        // Check loans
        for (WithdrawalRequestLoan loan : getLoans()) {
            if (loan.doAlertCodesExist()) {
                return true;
            }
        }

        // Check fees
        for (Fee fee : getFees()) {
            if (((WithdrawalRequestFee) fee).doAlertCodesExist()) {
                return true;
            }
        }

        // Check money types
        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
            if (moneyType.doAlertCodesExist()) {
                return true;
            }
        }

        // Check recipients
        for (Recipient recipient : getRecipients()) {
            if (((WithdrawalRequestRecipient) recipient).doAlertCodesExist()) {
                return true;
            }
        }

        // No alerts exist
        return false;
    }

    /**
     * Determines if this request is valid to be processed. If errors are being ignored, this method
     * always returns true, otherwise if it contains errors, it's not valid (returns false). If it
     * contains warnings, and warnings aren't being ignored, it's not valid. Otherwise it is valid.
     * This method is intended to be checked after validation has run.
     * 
     * @return boolean True if it's valid to process, false otherwise.
     */
    public boolean isValidToProcess() {

        if (BooleanUtils.isTrue(getIgnoreErrors())) {
            return true;
        } // fi

        if (doErrorCodesExist()) {
            return false;
        } // fi

        if (!(BooleanUtils.isTrue(getIgnoreWarnings()))) {
            // We need to return false if there are warnings.
            if (doWarningCodesExist()) {
                return false;
            } // fi
        } // fi

        return true;
    }

    /**
     * This is the PBA value for the participant. If the participant has PBA money, then this is
     * true.
     * 
     * @return the indicator
     */
    public Boolean getPartWithPbaMoneyInd() {
        return partWithPbaMoneyInd;
    }

    /**
     * @param partWithPbaMoneyInd the indicator
     */
    public void setPartWithPbaMoneyInd(final Boolean partWithPbaMoneyInd) {
        this.partWithPbaMoneyInd = partWithPbaMoneyInd;
    }

    /**
     * @return the retirement date
     */
    public Date getRetirementDate() {
        return retirementDate;
    }

    /**
     * @param retirementDate the retirement date
     */
    public void setRetirementDate(final Date retirementDate) {
        this.retirementDate = retirementDate;
    }

    /**
     * @return the employee profile id
     */
    public Integer getEmployeeProfileId() {
        return employeeProfileId;
    }

    /**
     * @param employeeProfileId the employee profile id
     */
    public void setEmployeeProfileId(final Integer employeeProfileId) {
        this.employeeProfileId = employeeProfileId;
    }

    /**
     * Queries if the withdrawal reason is simple.
     * 
     * @return boolean - True if the withdrawal reason is simple.
     */
    public boolean getWithdrawalReasonSimple() {

        return (StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_DISABILITY_CODE)
                || StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
                || StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE)
                || StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE)
                || StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE)
                || StringUtils.equals(
                getReasonCode(), WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE));
    }

    /**
     * Queries if the WMSI or Penchecks has been selected.
     * 
     * @return boolean - True if the WMSI or Penchecks has been selected.
     */
    public boolean isWmsiOrPenchecksSelected() {
        return StringUtils.equals(getIraServiceProviderCode(), IRA_SERVICE_PROVIDER_WMSI_CODE)
                || StringUtils.equals(getIraServiceProviderCode(),
                        IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
    }

    /**
     * Queries if the WMSI has been selected.
     * 
     * @return boolean - True if the WMSI has been selected.
     */
    public boolean isWmsiSelected() {
        return StringUtils.equals(getIraServiceProviderCode(), IRA_SERVICE_PROVIDER_WMSI_CODE);
    }

    /**
     * @return the originalPaymentTo
     */
    public String getOriginalPaymentTo() {
        return originalPaymentTo;
    }

    /**
     * @param originalPaymentTo the originalPaymentTo to set
     */
    public void setOriginalPaymentTo(final String originalPaymentTo) {
        this.originalPaymentTo = originalPaymentTo;
    }

    /**
     * @return the originalReasonCode
     */
    public String getOriginalReasonCode() {
        return originalReasonCode;
    }

    /**
     * @param originalReasonCode the originalReasonCode to set
     */
    public void setOriginalReasonCode(final String originalReasonCode) {
        this.originalReasonCode = originalReasonCode;
    }

    /**
     * hasReasonCodeChanged is true if the value changes from the original value.
     * 
     * @return boolean True if the reason code is not the same as the original, false otherwise.
     */
    public boolean hasReasonCodeChanged() {
        return !StringUtils.equals(reasonCode, originalReasonCode);
    }

    /**
     * hasPaymentToChanged is true if the value changes from the original value.
     * 
     * @return boolean True if the payment to is not the same as the original, false otherwise.
     */
    public boolean hasPaymentToChanged() {
        boolean hasChanged = false;
        // if (originalPaymentTo != null) {
        hasChanged = !StringUtils.equals(paymentTo, originalPaymentTo);
        // }
        return hasChanged;
    }

    /**
     * @return true if the vesting engine has been called
     */
    public Boolean getVestingCalledInd() {
        return BooleanUtils.isTrue(vestingCalledInd);
    }

    /**
     * @param vestingCalledInd Indictar telling if the vesting engine has been called or not
     */
    public void setVestingCalledInd(final Boolean vestingCalledInd) {
        this.vestingCalledInd = vestingCalledInd;
    }

    /**
     * @return the ignoreWarnings
     */
    public Boolean getIgnoreWarnings() {
        return ignoreWarnings;
    }

    /**
     * @param ignoreWarnings the ignoreWarnings to set
     */
    public void setIgnoreWarnings(final Boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
    }

    
    /**
     * @return the vestingNonCriticalErrorWithWarning
     */
    public Boolean getVestingNonCriticalErrorWithWarning() {
        return vestingNonCriticalErrorWithWarning;
    }

    /**
     * @param vestingNonCriticalErrorWithWarning the vestingNonCriticalErrorWithWarning to set
     */
    public void setVestingNonCriticalErrorWithWarning(final Boolean vestingNonCriticalErrorWithWarning) {
        this.vestingNonCriticalErrorWithWarning = vestingNonCriticalErrorWithWarning;
    }

    /**
     * @return the ignoreErrors
     */
    public Boolean getIgnoreErrors() {
        return ignoreErrors;
    }

    /**
     * @param ignoreErrors the ignoreErrors to set
     */
    public void setIgnoreErrors(final Boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    /**
     * @return The unfiltered collection of notes
     */
    public Collection<Note> getNotes() {
        return notes;
    }

    /**
     * @param notes Sets the unfiltered collection of notes
     */
    public void setNotes(final Collection<Note> notes) {
        this.notes = notes;
    }

    /**
     * @return the contractInfo
     */
    public ContractInfo getContractInfo() {
        return contractInfo;
    }

    /**
     * @param contractInfo the contractInfo to set
     */
    public void setContractInfo(final ContractInfo contractInfo) {
        this.contractInfo = contractInfo;
    }

    /**
     * Retrieves the total requested withdrawal amount. If one of the requested amounts is not set,
     * the total returns as null.
     * 
     * @return BigDecimal - The total requested withdrawal amount.
     * @throws SystemException 
     */
    public BigDecimal getTotalRequestedWithdrawalAmount()  {
        BigDecimal total = BigDecimal.ZERO;
        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
	if(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE.equals(getReasonCode())&& WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE.equals(getAmountTypeCode())){
        		
        		if("EEDEF".equals(moneyType.getMoneyTypeId() ) && moneyType.getAvailableHarshipAmount() !=null){
        		
        			total = total.add(moneyType.getAvailableHarshipAmount());
        		}
        	}else{
            if (moneyType.getWithdrawalAmount() != null) {
                total = total.add(moneyType.getWithdrawalAmount());
            } else {
                return null;
            }
        	}
        }
        return total;
    }

    /**
     * Retrieves the total available withdrawal amount. If one of the available amounts is not set,
     * the total returns as null.
     * 
     * @return BigDecimal - The total available withdrawal amount.
     */
    public BigDecimal getTotalAvailableWithdrawalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
        
            if (moneyType.getAvailableWithdrawalAmount() != null) {
                total = total.add(moneyType.getAvailableWithdrawalAmount());
            } else {
                return null;
            }
        	
        }
        return total;
    }

    /**
     * Retrieves the total balance amount. If one of the total balance amounts is not set, the total
     * returns as null.
     * 
     * @return BigDecimal - The total balance amount.
     */
    public BigDecimal getTotalBalance() {
        BigDecimal total = BigDecimal.ZERO;

        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
            final BigDecimal totalBalance = moneyType.getTotalBalance();
            if (totalBalance != null) {
                total = total.add(totalBalance);
            } else {
                return null;
            } // fi
        } // end for
        return total;
    }

    /**
     * Calculates the total loan amount for the selected participant.
     * 
     * @return {@link BigDecimal} The total of all loans.
     */
    public BigDecimal getTotalOutstandingLoanAmt() {
        Iterator<WithdrawalRequestLoan> it = loans.iterator();
        WithdrawalRequestLoan loan = null;
        BigDecimal totalAmt = new BigDecimal(0.00);
        while (it.hasNext()) {
            loan = it.next();
            BigDecimal amt = loan.getOutstandingLoanAmount();
            totalAmt = totalAmt.add(amt);

        }
        // BigDecimal totalLoanAmt = new BigDecimal(totalAmt);
        // return totalLoanAmt;
        return totalAmt;
    }

    /**
     * @return the originalParticipantLeavingPlanInd
     */
    public Boolean getOriginalParticipantLeavingPlanInd() {
        return originalParticipantLeavingPlanInd;
    }

    /**
     * @param originalParticipantLeavingPlanInd the originalParticipantLeavingPlanInd to set
     */
    public void setOriginalParticipantLeavingPlanInd(final Boolean originalParticipantLeavingPlanInd) {
        this.originalParticipantLeavingPlanInd = originalParticipantLeavingPlanInd;
    }

    /**
     * @return the originalBirthDate
     */
    public Date getOriginalBirthDate() {
        return originalBirthDate;
    }

    /**
     * @param originalBirthDate the originalBirthDate to set
     */
    public void setOriginalBirthDate(final Date originalBirthDate) {
        this.originalBirthDate = originalBirthDate;
    }

    /**
     * @return the originalParticipantStateOfResidence
     */
    public String getOriginalParticipantStateOfResidence() {
        return originalParticipantStateOfResidence;
    }

    /**
     * @param originalParticipantStateOfResidence the originalParticipantStateOfResidence to set
     */
    public void setOriginalParticipantStateOfResidence(
            final String originalParticipantStateOfResidence) {
        this.originalParticipantStateOfResidence = originalParticipantStateOfResidence;
    }

    /**
     * Determines if any step 1 driver fields have changed since last save using the original step 1
     * driver field values.
     * 
     * @return boolean - True if any step 1 driver fields have changed since the last save.
     */
    public boolean getHaveStep1DriverFieldsChanged() {

        return !(StringUtils.equals(getParticipantStateOfResidence(),
                getOriginalParticipantStateOfResidence())
                && StringUtils.equals(getReasonCode(), getOriginalReasonCode())
                && StringUtils.equals(getPaymentTo(), getOriginalPaymentTo())
                && ObjectUtils.equals(getBirthDate(), getOriginalBirthDate()) && ObjectUtils
                .equals(getParticipantLeavingPlanInd(), getOriginalParticipantLeavingPlanInd()));
    }

    /**
     * Updates the step 1 driver field original values which allows us to track whether they have
     * changed during the navigation between step 1 and step 2. Currently the step 1 driver fields
     * that are copied are:
     * <ul>
     * <li>State of Residence</li>
     * <li>Participant Leaving Plan Indicator</li>
     * <li>Date of Birth</li>
     * <li>Payment To</li>
     * <li>Withdrawal Reason</li>
     * <li>Eligible for Rollover</li>
     * </ul>
     */
    public void updateOriginalStep1DriverFields() {

        setOriginalReasonCode(getReasonCode());
        setOriginalPaymentTo(getPaymentTo());
        setOriginalParticipantLeavingPlanInd(getParticipantLeavingPlanInd());
        setOriginalBirthDate(getBirthDate());
        setOriginalParticipantStateOfResidence(getParticipantStateOfResidence());
        if (CollectionUtils.isEmpty(getRecipients())) {
            setOriginalStateTaxType(StateTaxType.NONE);
        } else {
            final StateTaxVO stateTax = getRecipients().iterator().next().getStateTaxVo();
            setOriginalStateTaxType(stateTax == null ? StateTaxType.NONE : stateTax
                    .getStateTaxType());
        } // fi
    }

    /**
     * @return originalAmountTypeCode
     */
    public String getOriginalAmountTypeCode() {
        return originalAmountTypeCode;
    }

    /**
     * @param originalAmountTypeCode the originalAmountTypeCode to set.
     */
    public void setOriginalAmountTypeCode(final String originalAmountTypeCode) {
        this.originalAmountTypeCode = originalAmountTypeCode;
    }

    /**
     * @return the federalTaxVo
     */
    public FederalTaxVO getFederalTaxVo() {
        return federalTaxVo;
    }

    /**
     * @param federalTaxVo the federalTaxVo to set
     */
    public void setFederalTaxVo(final FederalTaxVO federalTaxVo) {
        this.federalTaxVo = federalTaxVo;
    }

    /**
     * Determines if the Show Option For Unvested Amount field is shown or not.
     * 
     * @return boolean - True if the field is shown, false otherwise.
     */
    public boolean getShowOptionForUnvestedAmount() {

        // Check to see if all vesting percentages are 100% (full).
        boolean allFull = true;
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : getMoneyTypes()) {
            if (withdrawalRequestMoneyType.getVestingPercentage() != null) {
                boolean isOneHundredPercent = (withdrawalRequestMoneyType.getVestingPercentage()
                        .compareTo(GlobalConstants.ONE_HUNDRED) == 0);
                allFull = allFull && isOneHundredPercent;
            } else {
                allFull = false;
                break;
            }
        } // end for

        if (allFull) {
            // Don't show, as there is no unvested money remaining.
            return false;
        }

        // If it's not all full, only suppress with certain types.
        boolean isSuppressedType = (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE, getReasonCode())
                || StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE,
                        getReasonCode()) || StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE,
                getReasonCode()));

        return !isSuppressedType;
    }

    /**
     * Determines if the Last Contribution Applicable Date field is shown or not.
     * 
     * @return boolean - True if the field is shown, false otherwise.
     */
    public boolean getShowFinalContributionDate() {

        if ((BooleanUtils.isTrue(getParticipantLeavingPlanInd()))
                && (!(StringUtils.equals(getReasonCode(),
                        WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)))) {
            return true;
        } // fi
        if ((StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE))
                || (StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_RETIREMENT_CODE))
                || (StringUtils.equals(getReasonCode(), WITHDRAWAL_REASON_DISABILITY_CODE))) {
            return true;
        } // fi
        if ((!(isWmsiOrPenchecksSelected()))
                && (StringUtils.equals(getReasonCode(),
                        WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE))) {
            return true;
        } // fi

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMessages() {
        // Check base
        super.removeMessages();

        // Check notes
        if (getCurrentAdminToAdminNote() != null) {
            getCurrentAdminToAdminNote().removeMessages();
        } // fi

        if (getCurrentAdminToParticipantNote() != null) {
            getCurrentAdminToParticipantNote().removeMessages();
        } // fi

        // Check declarations
        for (Declaration declaration : getDeclarations()) {
            ((WithdrawalRequestDeclaration) declaration).removeMessages();
        } // end for

        // Check loans
        for (WithdrawalRequestLoan loan : getLoans()) {
            loan.removeMessages();
        } // end for

        // Check fees
        for (Fee fee : getFees()) {
            ((WithdrawalRequestFee) fee).removeMessages();
        } // end for

        // Check money types
        for (WithdrawalRequestMoneyType moneyType : getMoneyTypes()) {
            moneyType.removeMessages();
        } // end for

        // Check recipients
        for (Recipient recipient : getRecipients()) {
            ((WithdrawalRequestRecipient) recipient).removeMessages();
        } // end for
    }

    /**
     * @return the recalculationRequired
     */
    public boolean isRecalculationRequired() {
        return recalculationRequired;
    }

    /**
     * @param recalculationRequired the recalculationRequired to set
     */
    public void setRecalculationRequired(final boolean recalculationRequired) {
        this.recalculationRequired = recalculationRequired;
    }

    /**
     * @return returns the activity history value object
     */
    public final ActivityHistory getActivityHistory() {
        return activityHistory;
    }

    /**
     * @param activityHistory sets the activity history value object
     */
    public final void setActivityHistory(final ActivityHistory activityHistory) {
        this.activityHistory = activityHistory;
    }

    /**
     * This method determines if this request was participant initiated.
     * 
     * @return boolean - True if the request was initiated by a user that's a participant (role).
     */
    public boolean getParticipantInitiated() {
        return StringUtils.equals(getUserRoleCode(), WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE);
    }

    /**
     * @return the legaleseInfo
     */
    public LegaleseInfo getLegaleseInfo() {
        return legaleseInfo;
    }

    /**
     * @param legaleseInfo the legaleseInfo to set
     */
    public void setLegaleseInfo(final LegaleseInfo legaleseInfo) {
        this.legaleseInfo = legaleseInfo;
    }

    /**
     * Determines whether the tax withholding section should be displayed or suppressed. Currently
     * the tax withholding section is suppressed if:
     * <ul>
     * <li>Payment To is Plan trustee.
     * <li>Payment To is Rollover to IRA.
     * <li>Payment To is Rollover to plan.
     * <li>Payment To is After-tax contributions direct to participant, remainder to IRA.
     * <li>Payment To is After-tax contributions direct to participant, remainder to plan.
     * <li>Total requested amount is less than $200.00.
     * </ul>
     * 
     * @return the showTaxWitholdingSection
     */
    public boolean getShowTaxWitholdingSection() {
        if (StringUtils.equals(paymentTo, PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)
                || StringUtils.equals(paymentTo, PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo, PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE)
                || StringUtils.equals(paymentTo, PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)
                || StringUtils.equals(paymentTo, PAYMENT_TO_PLAN_TRUSTEE_CODE)) {

            return false;
        } // fi

        final BigDecimal totalRequestedWithdrawalAmount = getTotalRequestedWithdrawalAmount();

        if (totalRequestedWithdrawalAmount == null) {
            // No total, then show.
            return true;
        } // fi
       
        //CL -137150 i:withdrawal Tax Withholding for any amount, if the contract issue state and state of residence of the participant is PR.
        if ("PR".equals(getParticipantStateOfResidence()) && "PR".equals(getContractIssuedStateCode())) {
            return true;
        }
        
        if((getParticipantStateOfResidence()).equals("CT") && totalRequestedWithdrawalAmount.compareTo(BigDecimal.ZERO) > 0){
        	//
        	return true;
        	
        }else if (totalRequestedWithdrawalAmount.compareTo(DISPLAY_TAX_WITHHOLDING_SECTION_BOUNDARY) < 0 ) {
            // Total requested is less than boundary - so suppress removed and tax dropdown will display even withdrasl is 0 $ 
            return true;
        } // fi
       
        return true;
    }

    /**
     * Queries if the request is post draft (status has been set and is not equal to draft).
     * 
     * @return boolean - True if the request is post draft.
     */
    public boolean getIsPostDraft() {
        return StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE,
                getStatusCode())
                || StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE,
                        getStatusCode())
                || StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_APPROVED_CODE,
                        getStatusCode());
    }

    /**
     * Filters invalid Apollo dates.
     * 
     * @param date The date to filter.
     * @return Date The date entered if it's in the valid range, otherwise null.
     */
    private Date filterInvalidApolloDates(final Date date) {

        final Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        final Date oldestDate = DateUtils.addYears(now, -1 * VALIDATION_AGE);
        final Date farthestDate = DateUtils.addYears(now, 1 * VALIDATION_AGE);

        return (date != null && date.after(oldestDate) && date.before(farthestDate)) ? date : null;
    }

    /**
     * @return the lastFeeChangeByTPAUserID
     */
    public BigDecimal getLastFeeChangeByTPAUserID() {
        return lastFeeChangeByTPAUserID;
    }

    /**
     * @param lastFeeChangeByTPAUserID the lastFeeChangeByTPAUserID to set
     */
    public void setLastFeeChangeByTPAUserID(final BigDecimal lastFeeChangeByTPAUserID) {
        this.lastFeeChangeByTPAUserID = lastFeeChangeByTPAUserID;
    }

    /**
     * @return the lastFeeChangeWasPSUserInd
     */
    public boolean isLastFeeChangeWasPSUserInd() {
        return lastFeeChangeWasPSUserInd;
    }

    /**
     * @param lastFeeChangeWasPSUserInd the lastFeeChangeWasPSUserInd to set
     */
    public void setLastFeeChangeWasPSUserInd(final boolean lastFeeChangeWasPSUserInd) {
        this.lastFeeChangeWasPSUserInd = lastFeeChangeWasPSUserInd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> fieldNamesToExcludeFromLogging() {

        final Collection<String> toExclude = new ArrayList<String>(
                FIELDS_TO_EXCLUDE_FROM_LOGGING.length);

        CollectionUtils.addAll(toExclude, FIELDS_TO_EXCLUDE_FROM_LOGGING);

        toExclude.addAll(super.fieldNamesToExcludeFromLogging());

        return toExclude;
    }

    /**
     * @return boolean - The removeAllNotesOnSave.
     */
    public boolean isRemoveAllNotesOnSave() {
        return removeAllNotesOnSave;
    }

    /**
     * @param removeAllNotesOnSave - The removeAllNotesOnSave to set.
     */
    public void setRemoveAllNotesOnSave(final boolean removeAllNotesOnSave) {
        this.removeAllNotesOnSave = removeAllNotesOnSave;
    }

    /**
     * @return the isLegaleseConfirmed
     */
    public Boolean getIsLegaleseConfirmed() {
        return isLegaleseConfirmed;
    }

    /**
     * @param isLegaleseConfirmed the isLegaleseConfirmed to set
     */
    public void setIsLegaleseConfirmed(final Boolean isLegaleseConfirmed) {
        this.isLegaleseConfirmed = isLegaleseConfirmed;
    }

    /**
     * @return the isNoLongerValid
     */
    public boolean getIsNoLongerValid() {
        return isNoLongerValid;
    }

    /**
     * @param isNoLongerValid the isNoLongerValid to set
     */
    public void setIsNoLongerValid(final boolean isNoLongerValid) {
        this.isNoLongerValid = isNoLongerValid;
    }

    /**
     * @return Boolean - The requestInitiatedFromView.
     */
    public Boolean getRequestInitiatedFromView() {
        return requestInitiatedFromView;
    }

    /**
     * @param requestInitiatedFromView - The requestInitiatedFromView to set.
     */
    public void setRequestInitiatedFromView(final Boolean requestInitiatedFromView) {
        this.requestInitiatedFromView = requestInitiatedFromView;
    }

    /**
     * isEligibleForRollover returns true if the request is eligible, otherise it returns false.
     * 
     * @return boolean - True if eligible, false otherwise.
     */
    public boolean isEligibleForRollover() {
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
                || StringUtils.equals(reasonCode,
                        WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE)) {
            // This is NOT eligible for rollover.
            return false;
        } // fi

        // Otherwise it is eligible for rollover.
        return true;
    }

    /**
     * @return the originalStateTaxType
     */
    public StateTaxType getOriginalStateTaxType() {
        return originalStateTaxType;
    }

    /**
     * @param originalStateTaxType the originalStateTaxType to set
     */
    public void setOriginalStateTaxType(final StateTaxType originalStateTaxType) {
        this.originalStateTaxType = originalStateTaxType;
    }

    /**
     * Determines if this request has been persisted or not. This is done by checking if the
     * submission ID has been set or not (which can only be done once it's been saved in the
     * database.
     * 
     * @return boolean - True if the request has been saved, false otherwise.
     */
    public boolean getHasBeenPersisted() {
        return (getSubmissionId() != null);
    }

    /**
     * Returns an escaped version of the reason description for display.
     * 
     * @return the reasonDescription
     */
    public String getReasonDescriptionForDisplay() {
        return com.manulife.pension.util.StringUtils.escapeHtmlAndFormFeeds(reasonDescription);
    }

    /**
     * @return the principal
     */
    public Principal getPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(final Principal principal) {
        this.principal = principal;
    }
    
    /**
     * @return the forceData
     */
    public Boolean getForceData() {
        return forceData;
    }

    /**
     * @param forceData the forceData to set
     */
    public void setForceData(final Boolean forceData) {
        this.forceData = forceData;
    }

    /**
     * Determines if the DisabiltiyDate is shown or suppressed.
     * 
     * @return boolean - True if shown, otherwise suppressed.
     */
    public boolean getShowDisabilityDate() {
        return (WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE.equals(getReasonCode()));
    }

    /**
     * getShowRetirementDate determines if the element should be shown or not.
     * 
     * @return boolean - true if the element should be shown, false otherwise.
     */
    public boolean getShowRetirementDate() {
        return (WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE.equals(getReasonCode()));

    }

    /**
     * getShowTerminationDate determines if the element should be shown or not.
     * 
     * @return boolean - true if the element should be shown, false otherwise.
     */
    public boolean getShowTerminationDate() {
        return (WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE
                .equals(getReasonCode()) || WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE
                .equals(getReasonCode()));
    }

    /**
     * Queries if initial messages have been loaded for initiate step 1.
     * 
     * @return boolean - True if the initial messages are loaded for step 1.
     */
    public boolean getHaveInitiateStep1InitialMessagesLoaded() {
        return initialMessagesLoaded[STEP_1_MODE_INDEX];
    }

    /**
     * Queries if initial messages have been loaded for initiate step 2.
     * 
     * @return boolean - True if the initial messages are loaded for step 2.
     */
    public boolean getHaveInitiateStep2InitialMessagesLoaded() {
        return initialMessagesLoaded[STEP_2_MODE_INDEX];
    }

    /**
     * Queries if initial messages have been loaded for review and approve.
     * 
     * @return boolean - True if the initial messages are loaded for review and approve.
     */
    public boolean getHaveReviewAndApproveInitialMessagesLoaded() {
        return initialMessagesLoaded[STEP_1_MODE_INDEX];
    }

    /**
     * Sets that initial messages have been loaded for the specified mode.
     * 
     * @param mode The mode specify which initial messages have been loaded.
     */
    public void setInitialMessagesLoaded(final Mode mode) {

        switch (mode) {
            case INITIATE_STEP_1:
                initialMessagesLoaded[STEP_1_MODE_INDEX] = true;
                break;
            case INITIATE_STEP_2:
                initialMessagesLoaded[STEP_2_MODE_INDEX] = true;
                break;
            case REVIEW_AND_APPROVE:
                initialMessagesLoaded[REVIEW_MODE_INDEX] = true;
                break;
            default:
                throw new IllegalArgumentException(new StringBuffer("Unknown mode [").append(mode)
                        .append("] passed.").toString());
        }
    }

    /**
     * @return the vestingCriticalError
     */
    public Boolean getVestingCriticalError() {
        return vestingCriticalError;
    }

    /**
     * @param vestingCriticalError the vestingCriticalError to set
     */
    public void setVestingCriticalError(final Boolean vestingCriticalError) {
        this.vestingCriticalError = vestingCriticalError;
    }

    /**
     * @return the defaultFinalContributionDateForWmsiPenChecks
     */
    public Date getDefaultFinalContributionDateForWmsiPenChecks() {
        return defaultFinalContributionDateForWmsiPenChecks;
    }

    /**
     * @param defaultFinalContributionDateForWmsiPenChecks the
     *            defaultFinalContributionDateForWmsiPenChecks to set
     */
    public void setDefaultFinalContributionDateForWmsiPenChecks(
          final Date defaultFinalContributionDateForWmsiPenChecks) {
        this.defaultFinalContributionDateForWmsiPenChecks = defaultFinalContributionDateForWmsiPenChecks;
    }

    /**
     * @return the robustDateChangedAfterVesting
     */
    public boolean getRobustDateChangedAfterVesting() {
        return robustDateChangedAfterVesting;
    }

    /**
     * @param robustDateChangedAfterVesting the robustDateChangedAfterVesting to set
     */
    public void setRobustDateChangedAfterVesting(final boolean robustDateChangedAfterVesting) {
        this.robustDateChangedAfterVesting = robustDateChangedAfterVesting;
    }

    /**
     * Determines the default loan option.
     * 
     * @return String - The default loan option.
     */
    public String getDefaultLoanOption() {

        if (StringUtils.isNotBlank(getLoanOption())) {
            return getLoanOption();
        } else if (StringUtils.isBlank(getReasonCode()) || getWithdrawalReasonSimple()) {
            // Use KEEP if withdrawal reason is blank or simple
            return LOAN_KEEP_OPTION;
        } else {
            return LOAN_CLOSURE_OPTION;
        }
    }

    /**
     * Queries if the vesting engine could not calculate - indicator is only for use on step 2 and
     * is reset on leaving page.
     * 
     * @return boolean - True if the vesting engine was just called and the vesting could not be
     *         calculated.
     */
    public boolean getVestingCouldNotBeCalculatedInd() {
        return vestingCouldNotBeCalculatedInd;
    }

    /**
     * @param vestingCouldNotBeCalculatedInd the vestingCouldNotBeCalculatedInd to set
     */
    public void setVestingCouldNotBeCalculatedInd(final boolean vestingCouldNotBeCalculatedInd) {
        this.vestingCouldNotBeCalculatedInd = vestingCouldNotBeCalculatedInd;
    }

    /**
     * @return Boolean - The activityHistoryEnabled.
     */
    public Boolean getActivityHistoryEnabled() {
        return activityHistoryEnabled;
    }

    /**
     * @param activityHistoryEnabled - The activityHistoryEnabled to set.
     */
    public void setActivityHistoryEnabled(final Boolean activityHistoryEnabled) {
        this.activityHistoryEnabled = activityHistoryEnabled;
    }

    /**
     * @return the cmaSiteCode
     */
    public String getCmaSiteCode() {
        return cmaSiteCode;
    }

    /**
     * @param cmaSiteCode the cmaSiteCode to set
     */
    public void setCmaSiteCode(final String cmaSiteCode) {
        this.cmaSiteCode = cmaSiteCode;
    }

    /**
     * @return the participantLegaleseInfo
     */
    public LegaleseInfo getParticipantLegaleseInfo() {
        return participantLegaleseInfo;
    }

    /**
     * @param participantLegaleseInfo the participantLegaleseInfo to set
     */
    public void setParticipantLegaleseInfo(final LegaleseInfo participantLegaleseInfo) {
        this.participantLegaleseInfo = participantLegaleseInfo;
    }

    /**
     * @return Timestamp - The submissionCaseLastUpdated.
     */
    public Timestamp getSubmissionCaseLastUpdated() {
        return submissionCaseLastUpdated;
    }

    /**
     * @param submissionCaseLastUpdated - The submissionCaseLastUpdated to set.
     */
    public void setSubmissionCaseLastUpdated(final Timestamp submissionCaseLastUpdated) {
        this.submissionCaseLastUpdated = submissionCaseLastUpdated;
    }

    /**
     * Queries if the specified declaration type is available to the request (not suppressed).
     * 
     * @param declaration The declaration code to query.
     * @return boolean - True if the declaration is available, false otherwise.
     */
    public boolean isDeclarationAvailable(final String declaration) {
        if (StringUtils.equals(declaration, WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE)
                || StringUtils.equals(declaration,
                        WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE)) {
            // Declarations are always shown
            return true;
        } else if (StringUtils.equals(declaration,
                WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE)) {
            // Declaration is only shown if WMSI or PenChecks has been selected
            return isWmsiOrPenchecksSelected();
            // checks to see if the At Risk declaration should be displayed.
        } else if (StringUtils.equals(declaration,
                WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE)) {
            return isAtRiskDeclarationPermittedForUser();
        } else {
            // Gracefully handle any unknown declarations
            return false;
        }
    }

    /**
     * @return the requestRiskIndicator
     */
    public boolean getRequestRiskIndicator() {
        return requestRiskIndicator;
    }

    /**
     * @param requestRiskIndicator the requestRiskIndicator to set
     */
    public void setRequestRiskIndicator(final boolean requestRiskIndicator) {
        this.requestRiskIndicator = requestRiskIndicator;
    }
    
    
    public String getLegallyMarriedInd() {
		return legallyMarriedInd;
	}

	public void setLegallyMarriedInd(String legallyMarriedIn) {
		this.legallyMarriedInd = legallyMarriedIn;
	}

    /**
     * @return the atRiskAddress
     */
    public Address getAtRiskAddress() {
        return atRiskAddress;
    }

    /**
     * @param atRiskAddress the atRiskAddress to set
     */
    public void setAtRiskAddress(final Address atRiskAddress) {
        this.atRiskAddress = atRiskAddress;
    }

	/**
	 * @return the pinEmailVerficationCode
	 */
	public String getPinEmailVerficationCode() {
		return pinEmailVerficationCode;
	}

	/**
	 * @param pinEmailVerficationCode the pinEmailVerficationCode to set
	 */
	public void setPinEmailVerficationCode(String pinEmailVerficationCode) {
		this.pinEmailVerficationCode = pinEmailVerficationCode;
	}

	/**
     * This method checks if the PlanSponsor or TPA is eligible to see the At Risk Declaration. It
     * returns true if:
     * 
     * <pre>
     *   1. the withdrawal request is participant initiated
     *   2. the at risk indicator is set to Y
     *   3. the user has approve persmissions.
     * </pre>
     * 
     * @return boolean
     */
    public boolean isAtRiskDeclarationPermittedForUser() {
        if ((getStatusCode() != null)
                && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
                        getStatusCode())) {
            if (contractInfo.getHasApprovePermission() != null) {
                if (getIsParticipantCreated() && requestRiskIndicator
                        && contractInfo.getHasApprovePermission()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return Date - The latestVestingEffectiveDate.
     */
    public Date getLatestVestingEffectiveDate() {
        return latestVestingEffectiveDate;
    }

    /**
     * @param latestVestingEffectiveDate - The latestVestingEffectiveDate to set.
     */
    public void setLatestVestingEffectiveDate(final Date latestVestingEffectiveDate) {
        this.latestVestingEffectiveDate = latestVestingEffectiveDate;
    }

    /**
     * Determines if the participant status is considered fully vested.
     * 
     * @return boolean - True if it`s fully vested, false otherwise.
     */
    public boolean getIsParticipantStatusFullyVested() {
        return ArrayUtils.contains(FULLY_VESTED_PARTICIPANT_STATUS_CODES, getEmplStatusCode());
    }

    /**
     * Gets the vesting event date that is applicable to this withdrawal request. This takes into
     * account if the withdrwal request type.
     * 
     * @return Date The Vesting Event Date, called the 'as of' date in the vesting world.
     */
    public Date getVestingEventDate() {
        final Date eventDate;
        // Determine which date to call vesting with.
        if (getShowTerminationDate()) {
            eventDate = getTerminationDate();
        } else if (getShowRetirementDate()) {
            eventDate = getRetirementDate();
        } else if (getShowDisabilityDate()) {
            eventDate = getDisabilityDate();
        } else {
            eventDate = new Date();
        } // fi

        if (logger.isDebugEnabled()) {
            logger.debug("getVestingEventDate: [" + eventDate + "]");
        } // fi

        return eventDate;
    }

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getTaxableBal() {
		return taxableBal;
	}

	public void setTaxableBal(String taxableBal) {
		this.taxableBal = taxableBal;
	}

	public String getParticipantAftrTax() {
		return participantAftrTax;
	}

	public void setParticipantAftrTax(String participantAftrTax) {
		this.participantAftrTax = participantAftrTax;
	}

	public String getParticipantRoth() {
		return participantRoth;
	}

	public void setParticipantRoth(String participantRoth) {
		this.participantRoth = participantRoth;
	}

	public String getParticipantNonRoth() {
		return participantNonRoth;
	}

	public void setParticipantNonRoth(String participantNonRoth) {
		this.participantNonRoth = participantNonRoth;
	}

	public String getRothBal() {
		return rothBal;
	}

	public void setRothBal(String rothBal) {
		this.rothBal = rothBal;
	}

	/**
	 * @return the atRiskDetailsVO
	 */
	public final AtRiskDetailsVO getAtRiskDetailsVO() {
		return atRiskDetailsVO;
	}

	/**
	 * @param atRiskDetailsVO the atRiskDetailsVO to set
	 */
	public final void setAtRiskDetailsVO(AtRiskDetailsVO atRiskDetailsVO) {
		this.atRiskDetailsVO = atRiskDetailsVO;
	}

	

	public String getTaxableFlag() {
		return taxableFlag;
	}

	public void setTaxableFlag(String taxableFlag) {
		this.taxableFlag = taxableFlag;
	}

	public String getNontaxableFlag() {
		return nontaxableFlag;
	}

	public void setNontaxableFlag(String nontaxableFlag) {
		this.nontaxableFlag = nontaxableFlag;
	}

	public String getParticipantTaxesFlag() {
		return participantTaxesFlag;
	}

	public void setParticipantTaxesFlag(String participantTaxesFlag) {
		this.participantTaxesFlag = participantTaxesFlag;
	}

	public String getRothTaxesFlag() {
		return rothTaxesFlag;
	}

	public void setRothTaxesFlag(String rothTaxesFlag) {
		this.rothTaxesFlag = rothTaxesFlag;
	}

	

	public String getParticipantDetails() {
		return participantDetails;
	}

	public void setParticipantDetails(String participantDetails) {
		this.participantDetails = participantDetails;
	}

	public boolean isRolloverTypeEligible() {
		return isRolloverTypeEligible;
	}

	public void setRolloverTypeEligible(boolean isRolloverTypeEligible) {
		this.isRolloverTypeEligible = isRolloverTypeEligible;
	}

	public String getTaxableParticipantInfo() {
		return taxableParticipantInfo;
	}

	public void setTaxableParticipantInfo(String taxableParticipantInfo) {
		this.taxableParticipantInfo = taxableParticipantInfo;
	}

	public String getNonTaxableParticipantInfo() {
		return nonTaxableParticipantInfo;
	}

	public void setNonTaxableParticipantInfo(String nonTaxableParticipantInfo) {
		this.nonTaxableParticipantInfo = nonTaxableParticipantInfo;
	}

	public String getRothParticaipantInfo() {
		return rothParticaipantInfo;
	}

	public void setRothParticaipantInfo(String rothParticaipantInfo) {
		this.rothParticaipantInfo = rothParticaipantInfo;
	}

	public String getTraditionalIRAFlag() {
		return traditionalIRAFlag;
	}

	public void setTraditionalIRAFlag(String traditionalIRAFlag) {
		this.traditionalIRAFlag = traditionalIRAFlag;
	}

	public String getRothIRAFlag() {
		return rothIRAFlag;
	}

	public void setRothIRAFlag(String rothIRAFlag) {
		this.rothIRAFlag = rothIRAFlag;
	}

	public String getEmpQulifiedPlanFlag() {
		return empQulifiedPlanFlag;
	}

	public void setEmpQulifiedPlanFlag(String empQulifiedPlanFlag) {
		this.empQulifiedPlanFlag = empQulifiedPlanFlag;
	}

	public String getTraditionalIRAPayee() {
		return traditionalIRAPayee;
	}

	public void setTraditionalIRAPayee(String traditionalIRAPayee) {
		this.traditionalIRAPayee = traditionalIRAPayee;
	}

	public String getRothIRAPayee() {
		return rothIRAPayee;
	}

	public void setRothIRAPayee(String rothIRAPayee) {
		this.rothIRAPayee = rothIRAPayee;
	}

	public String getEmpQulifiedPlanPayee() {
		return empQulifiedPlanPayee;
	}

	public void setEmpQulifiedPlanPayee(String empQulifiedPlanPayee) {
		this.empQulifiedPlanPayee = empQulifiedPlanPayee;
	}

	public boolean isTotalRothBalFlag() {
		return totalRothBalFlag;
	}

	public void setTotalRothBalFlag(boolean totalRothBalFlag) {
		this.totalRothBalFlag = totalRothBalFlag;
	}

	public boolean isNonTaxableFlag() {
		return nonTaxableFlag;
	}

	public void setNonTaxableFlag(boolean nonTaxableFlag) {
		this.nonTaxableFlag = nonTaxableFlag;
	}

	
	/**
	 * @return the rbCategory
	 */
	public String getRbCategory() {
		return rbCategory;
	}

	/**
	 * @param rbCategory the rbCategory to set
	 */
	public void setRbCategory(String rbCategory) {
		this.rbCategory = rbCategory;
	}

	/**
	 * @return the tbCategory
	 */
	public String getTbCategory() {
		return tbCategory;
	}

	/**
	 * @param tbCategory the tbCategory to set
	 */
	public void setTbCategory(String tbCategory) {
		this.tbCategory = tbCategory;
	}

	/**
	 * @return the nratCategory
	 */
	public String getNratCategory() {
		return nratCategory;
	}

	/**
	 * @param nratCategory the nratCategory to set
	 */
	public void setNratCategory(String nratCategory) {
		this.nratCategory = nratCategory;
	}

	public BigDecimal getMinimumHarshipAmount() {
		return minimumHarshipAmount;
	}

	public void setMinimumHarshipAmount(BigDecimal minimumHarshipAmount) {
		this.minimumHarshipAmount = minimumHarshipAmount;
	}

	public BigDecimal getMaximumHarshipAmount() {
		return maximumHarshipAmount;
	}

	public void setMaximumHarshipAmount(BigDecimal maximumHarshipAmount) {
		this.maximumHarshipAmount = maximumHarshipAmount;
	}

	public Collection<String> getAvilableHarshipMoneyType() {
		return avilableHarshipMoneyType;
	}

	public void setAvilableHarshipMoneyType(Collection<String> avilableHarshipMoneyType) {
		this.avilableHarshipMoneyType = avilableHarshipMoneyType;
	}
	 public boolean isCensusInfoAvailablePDInd() {
			return isCensusInfoAvailablePDInd;
		}

	public void setCensusInfoAvailablePDInd(boolean isCensusInfoAvailablePDInd) {
			this.isCensusInfoAvailablePDInd = isCensusInfoAvailablePDInd;
		}

	public boolean isEedefFlag() {
		return eedefFlag;
	}

	public void setEedefFlag(boolean eedefFlag) {
		this.eedefFlag = eedefFlag;
	}



	
}
