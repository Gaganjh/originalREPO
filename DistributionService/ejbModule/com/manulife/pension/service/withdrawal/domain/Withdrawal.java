package com.manulife.pension.service.withdrawal.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.AtRiskHandler;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.AtRiskAddressChangeVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskForgetUserName;
import com.manulife.pension.service.distribution.valueobject.AtRiskPasswordResetVO;
import com.manulife.pension.service.distribution.valueobject.AtRiskWebRegistrationVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.employee.EmploymentStatus;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.EmployeeFunctionShutdownVO.FunctionCode;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.environment.dao.EnvironmentDAO;
import com.manulife.pension.service.environment.valueobject.CodeEqualityPredicate;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.environment.valueobject.FederalTaxVO;
import com.manulife.pension.service.environment.valueobject.StateTaxType;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.loan.dao.DistributionAtRiskDetailsDAO;
import com.manulife.pension.service.loan.dao.LoanDao;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.notification.util.BrowseServiceHelper;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.entity.SecurityActivity.SecurityActivityTypeCode;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.withdrawal.common.WithdrawalDataManager;
import com.manulife.pension.service.withdrawal.common.WithdrawalDeclarationPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalEmailHandler;
import com.manulife.pension.service.withdrawal.common.WithdrawalLookupDataManager;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine;
import com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper;
import com.manulife.pension.service.withdrawal.dao.ApolloDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.dao.WithdrawalLegaleseDao;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.exception.WithdrawalEmailException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalServiceFeatureException;
import com.manulife.pension.service.withdrawal.util.ReadyForEntryEmailHandler;
import com.manulife.pension.service.withdrawal.util.WithdrawalHelper;
import com.manulife.pension.service.withdrawal.util.WithdrawalRequestHelper;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.MultiPayeeMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.MultiPayeeTaxes;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantFlag;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalEmailVO;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalMultiPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.CalendarUtils;
import com.manulife.pension.util.NumberUtils;
import com.manulife.pension.util.config.ConfigurationFactory;

/**
 * Withdrawal is the domain object (biz tier) for a withdrawal. This object contains the business
 * level representation of a withdrawal. The data is set/read with the {@link WithdrawalRequest}
 * value object. This object is stateful and uses a state pattern. The stateful methods are
 * delegated out to the appropriate state objects for processing.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public class Withdrawal {

    /**
     * The number of months in two years.
     */
    private static final int MONTHS_IN_TWO_YEARS = 24;

    private static final int MONTHS_IN_HALF_A_YEAR = 6;

    /**
     * REQUESTED_AMOUNT_PERCENTAGE_THRESHOLD.
     */
    private static final BigDecimal REQUESTED_AMOUNT_PERCENTAGE_THRESHOLD = new BigDecimal("5");

    /**
     * SPECIFIC_AMOUNT_PERCENTAGE_THRESHOLD.
     */
    private static final BigDecimal SPECIFIC_AMOUNT_PERCENTAGE_THRESHOLD = new BigDecimal("5");

    /**
     * Final Contribution Date Request Date Threshold.
     */
    private static final int FINAL_CONTRIBUTION_DATE_FUTURE_MONTHS_THRESHOLD = 6;

    private static final String ADDRESS_LINE_ONE_INVALID_KEY = "ADDRESS_LINE_ONE_INVALID_KEY";

    private static final String CITY_INVALID_KEY = "CITY_INVALID_KEY";

    private static final String STATE_INVALID_KEY = "STATE_INVALID_KEY";

    private static final String ZIP_ONE_INVALID_KEY = "ZIP_ONE_INVALID_KEY";

    private static final String ZIP_TWO_INVALID_KEY = "ZIP_TWO_INVALID_KEY";

    private static final String ZIP_STATE_INVALID_KEY = "ZIP_STATE_INVALID_KEY";

    private static final String COUNTRY_INVALID_KEY = "COUNTRY_INVALID_KEY";

    private static final boolean FUNCTION_IS_SAVE = false;

    private static final boolean FUNCTION_IS_DENY = true;

    private static final BigDecimal TWO_HUNDRED_AMOUNT = new BigDecimal("200.00");
    
    private static final String TRADITIONAL_IRA = "{\"Roth_IRA\":\"N\",\"Non_Taxable\":\"Y\",\"Taxable\":\"Y\",\"Roth_Non_Tax\":\"Y\",\"Roth_Taxable\":\"Y\"}";
    private static final String ROTH_IRA = "{\"Roth_IRA\":\"Y\",\"Non_Taxable\":\"Y\",\"Taxable\":\"Y\",\"Roth_Non_Tax\":\"Y\",\"Roth_Taxable\":\"Y\"}";


    private static final Map<String, WithdrawalMessageType> RECIPIENT_ADDRESS_MESSAGES = new HashMap<String, WithdrawalMessageType>() {
        private static final long serialVersionUID = 1L;
        {
            put(ADDRESS_LINE_ONE_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID);
            put(CITY_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID);
            put(STATE_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID);
            put(ZIP_ONE_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID);
            put(ZIP_TWO_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_ZIP_TWO_INVALID);
            put(ZIP_STATE_INVALID_KEY,
                    WithdrawalMessageType.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE);
            put(COUNTRY_INVALID_KEY, WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID);
        }
    };

    private static final Map<String, WithdrawalMessageType> CHECK_PAYEE_ADDRESS_MESSAGES = new HashMap<String, WithdrawalMessageType>() {
        private static final long serialVersionUID = 1L;
        {
            put(ADDRESS_LINE_ONE_INVALID_KEY,
                    WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID);
            put(CITY_INVALID_KEY, WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID);
            put(STATE_INVALID_KEY, WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID);
            put(ZIP_ONE_INVALID_KEY, WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID);
            put(ZIP_TWO_INVALID_KEY, WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID);
            put(ZIP_STATE_INVALID_KEY,
                    WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE);
            put(COUNTRY_INVALID_KEY, WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID);
        }
    };

    private static final Map<String, WithdrawalMessageType> FI_PAYEE_ADDRESS_MESSAGES = new HashMap<String, WithdrawalMessageType>() {
        private static final long serialVersionUID = 1L;
        {
            put(ADDRESS_LINE_ONE_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID);
            put(CITY_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_CITY_INVALID);
            put(STATE_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_STATE_INVALID);
            put(ZIP_ONE_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID);
            put(ZIP_TWO_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID);
            put(ZIP_STATE_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE);
            put(COUNTRY_INVALID_KEY, WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID);
        }
    };

    protected static final Map<String, WithdrawalMessageType> DECLARATION_WARNING_MESSAGES = new HashMap<String, WithdrawalMessageType>() {
        private static final long serialVersionUID = 1L;
        {
            put(Declaration.TAX_NOTICE_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_WARNING);
            put(Declaration.WAITING_PERIOD_WAIVED_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_WARNING);
            put(Declaration.IRA_SERVICE_PROVIDER_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING);
        }
    };

    protected static final Map<String, WithdrawalMessageType> DECLARATION_ERROR_MESSAGES = new HashMap<String, WithdrawalMessageType>() {
        private static final long serialVersionUID = 1L;
        {
            put(Declaration.TAX_NOTICE_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_ERROR);
            put(Declaration.WAITING_PERIOD_WAIVED_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_ERROR);
            put(Declaration.IRA_SERVICE_PROVIDER_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR);
            put(Declaration.AT_RISK_TRANSACTION_TYPE_CODE,
                    WithdrawalMessageType.DECLARATION_AT_RISK_INDICATOR_ERROR);
        }
    };

    private static final Logger logger = Logger.getLogger(Withdrawal.class);

    private static final BigDecimal MANDATORY_DISTRIBUTION_NON_ROLLOVER_THRESHHOLD_HIGH = new BigDecimal(
            "5000");

    private static final BigDecimal MANDATORY_DISTRIBUTION_NON_ROLLOVER_THRESHHOLD_LOW = new BigDecimal(
            "1000");

    /**
     * Warning date threshold for expiration date (less than three days).
     */
    private static final int EXPIRATION_DATE_WARNING_THRESHOLD = -2;

    /**
     * VALIDATION_MINIMUM_AGE.
     */
    private static final int VALIDATION_MINIMUM_AGE = 15;

    /**
     * VALIDATION_MAXIMUM_AGE.
     */
    private static final int VALIDATION_MAXIMUM_AGE = 150;

    private static final int NORMAL_RETIREMENT_AGE_YEARS = 65;

    private static final int NORMAL_RETIREMENT_AGE_MONTHS = 0;

    private static final int WITHDRAWAL_PERCENTAGE_SCALE = 2;

    private static final BigDecimal WMSI_THRESHOLD = new BigDecimal("900");

    protected static final BigDecimal FEE_DOLLAR_THRESHOLD = new BigDecimal("1000");

    protected static final BigDecimal FEE_PERCENT_THRESHOLD = BigDecimal.TEN;

    private static final int LEGALESE_STATIC_CONTENT_TEXT_KEY = 56216;

    private static final int LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT_KEY = 55750;

    private static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT_KEY = 56217;

    private static final int LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT_KEY = 56218;

    private static final int LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT_KEY = 56219;

    private static final String LOAN_OPTION_REPAY = "RP";

    private WithdrawalRequest withdrawalRequest;

    private WithdrawalState withdrawalState;

    private transient WithdrawalState previousWithdrawalState;

    private transient WithdrawalRequest savedWithdrawalRequest;

    private transient ParticipantInfo participantInfo;

    private transient ContractInfo contractInfo;

    private transient Timestamp lastSavedTimestamp;

    private boolean validateVesting = false;
    // a check for validating Money Types and Vesting Percentage.
    private boolean validateForDenyAndSave = false;
    
    private boolean validateVestingForRecaluculate = false;

    private boolean restartReview = false;
    
    /**
     * Default Constructor.
     * 
     * @param withdrawalRequest The withdrawal request containing the object data.
     */
    public Withdrawal(final WithdrawalRequest withdrawalRequest) {
        setWithdrawalRequest(withdrawalRequest);
    }

    /**
     * @param withdrawalRequest the withdrawalRequest to set
     */
    private void setWithdrawalRequest(final WithdrawalRequest withdrawalRequest) {
        this.withdrawalRequest = withdrawalRequest;

        WithdrawalStateFactory.updateStateFromStatusCode(this);
    }

    /**
     * Gets the value object containing the data.
     * 
     * @return WithdrawalRequest The value object.
     */
    public WithdrawalRequest getWithdrawalRequest() {
        return withdrawalRequest;
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#save()
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void save() throws DistributionServiceException {
        withdrawalState.save(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#deny()
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void deny() throws DistributionServiceException {
        withdrawalState.deny(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#delete()
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void delete() throws DistributionServiceException {
        withdrawalState.delete(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#approve()
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void approve() throws DistributionServiceException {
        validateVesting = true;
        withdrawalState.approve(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#cancel()
     */
    public void cancel() {
        withdrawalState.cancel(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#expire()
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void expire() throws DistributionServiceException {
        withdrawalState.expire(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#processApproved()
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void processApproved() throws DistributionServiceException {
        withdrawalState.processApproved(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#sendForApproval()
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void sendForApproval() throws DistributionServiceException {
        withdrawalState.sendForApproval(this);
    }

    /**
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#sendForReview()
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void sendForReview() throws DistributionServiceException {
        withdrawalState.sendForReview(this);
    }

    /**
     * {@inheritDoc}
     */
    public void applyDefaultDataForEdit(final WithdrawalRequest withdrawalRequestDefaultData)
            throws SystemException {

        withdrawalState.applyDefaultDataForEdit(this, withdrawalRequestDefaultData);
    }

    /**
     * {@inheritDoc}
     */
    public void applyDefaultDataForView(final WithdrawalRequest withdrawalRequestDefaultData)
            throws SystemException {
        withdrawalState.applyDefaultDataForView(this, withdrawalRequestDefaultData);
    }

    /**
     * @return the withdrawalState
     */
    protected WithdrawalState getWithdrawalState() {
        return withdrawalState;
    }

    /**
     * Sets the withdrawal state. Note that this also updates the status code in the value object
     * with the status code that corresponds to this state.
     * 
     * @param withdrawalState the withdrawalState to set
     */
    protected void setWithdrawalState(final WithdrawalState withdrawalState) {
        if ((this.withdrawalState != null) && (!(this.withdrawalState.equals(withdrawalState)))) {
            previousWithdrawalState = this.withdrawalState;
        } // fi

        this.withdrawalState = withdrawalState;

        updateStatusCodeFromState();
    }

    /**
     * Gets the previous value of the withdrawal state. This value can be null if there hasn't been
     * a state transition. This value is not persisted.
     * 
     * @return the previousWithdrawalState
     */
    public WithdrawalState getPreviousWithdrawalState() {
        return previousWithdrawalState;
    }

    /**
     * Sets the status code in the withdrawal's value object based on it's {@link WithdrawalState}.
     */
    private void updateStatusCodeFromState() {
        getWithdrawalRequest().setStatusCode(
                getWithdrawalState().getWithdrawalStateEnum().getStatusCode());
    }

    /**
     * This method is called before a transition to the {@link WithdrawalStateEnum} APPROVED state,
     * after it has been validated, but before save has occurred.
     */
    public void beforeEnteringApprovedState() {
        // Set the approved timestamp in the database.
        setLastSavedTimestamp();
        final Timestamp approvedTimestamp = new Timestamp(getLastSavedTimestamp().getTime());
        withdrawalRequest.setApprovedTimestamp(approvedTimestamp);

        // Set the expected processing date.
        final Date expectedProcessingDate = getExpectedProcessingDateFromNow();
        withdrawalRequest.setExpectedProcessingDate(expectedProcessingDate);

        // Ensure the legalese user ID is set, taking the value from the withdrawal request.
        withdrawalRequest.getLegaleseInfo().setCreatorUserProfileId(
                (int) withdrawalRequest.getPrincipal().getProfileId());

        // Add the Legalese details to the Legalese table if not already there.
        /*
         * try { final WithdrawalLegaleseDao dao = new WithdrawalLegaleseDao();
         * withdrawalRequest.getLegaleseInfo().setCmaSiteCode(withdrawalRequest.getCmaSiteCode());
         * logger.debug("Legalese**** :: " + withdrawalRequest.getLegaleseInfo().toString());
         * dao.updateLegaleseInfo(withdrawalRequest); final Integer version =
         * dao.getLegaleseTextVersion(withdrawalRequest.getLegaleseInfo());
         * 
         * withdrawalRequest.getLegaleseInfo().setContentVersionNumber(version.intValue()); } catch
         * (final SystemException systemException) { throw
         * ExceptionHandlerUtility.wrap(systemException); }
         */
    }

    /**
     * This method is called after a successful transition to the {@link WithdrawalStateEnum}
     * APPROVED state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void afterEnteringApprovedState() throws DistributionServiceException {

        // Don't insert notes twice.
        withdrawalRequest.setCurrentAdminToAdminNote(null);
        withdrawalRequest.setCurrentAdminToParticipantNote(null);

        // Reload notes with the current values.
        NoteDao withdrawalNoteDao = new NoteDao();
        try {
            List<Note> notes = (List<Note>) withdrawalNoteDao.select(withdrawalRequest
                    .getSubmissionId(), WithdrawalRequestNote.class);

            withdrawalRequest.setNotes(notes);

        } catch (DistributionServiceException DistributionServiceException) {
            throw new RuntimeException(DistributionServiceException);
        } // end try/catch

        processNotes(withdrawalRequest);

        // if (checkLastFeeUpdate()) {
        // final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
        // .getManulifeCompanyId();
        // final Location location = BrowseServiceHelper
        // .convertManulifeCompanyIdToLocation(manulifeCompanyId);
        // final WithdrawalEmailVO withdrawalEmailVo = new WithdrawalEmailVO(withdrawalRequest
        // .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
        // .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
        // .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
        // .getLastName(), null, location, withdrawalRequest.getRequestDate(),
        // withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
        // withdrawalRequest.getContractInfo().getClientAccountRepEmail(),
        // withdrawalRequest.getLastFeeChangeByTPAUserID(), withdrawalRequest
        // .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
        // withdrawalRequest.getEmployeeProfileId());
        //
        // final WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();
        // withdrawalEmailHandler.sendTPAFeeEmail(withdrawalEmailVo);
        // }

        final Collection<String> keys = ReadyForEntryEmailHandler.getLookupKeys();
        final Collection<Integer> userProfileIds = ReadyForEntryEmailHandler
                .getUserProfileIds(withdrawalRequest);

        final Map lookupDataMap = new WithdrawalLookupDataManager(null, StringUtils.EMPTY, keys)
                .getLookupData();

        final Map<Integer, UserName> userNamesMap;
        userNamesMap = WithdrawalInfoDao.getUserNames(userProfileIds);

      
        
        final ReadyForEntryEmailHandler readyForEntryEmailHandler = new ReadyForEntryEmailHandler(
                withdrawalRequest, lookupDataMap, userNamesMap);
        
        if (!(getWithdrawalTypes().contains(withdrawalRequest.getReasonCode().trim()))) {
	        
	        try {
	        		readyForEntryEmailHandler.sendEmail();
	        	}
	        catch (final SystemException systemException) {
	            throw new WithdrawalEmailException(systemException, CLASS_NAME,
	                    "afterEnteringApprovedState", "Error sending read for entry email.");
	        } // end try/catch
        }
        
        WithdrawalHelper.fireWithdrawalApprovedEvent(withdrawalRequest);
        
        // This updates the state to 'ready for entry' and saves the object.
        processApproved();
    }

    /**
     * If a TPA user changed the TPA fee at any time in the life of the WD request and if the very
     * last person to change the TPA Fee was a Plan Sponsor user, then this returns true.
     * 
     * @return boolean - True if a TPA has changed the fee and the last change was by a PS user,
     *         false otherwise.
     */
    private boolean checkLastFeeUpdate() {
        return (withdrawalRequest.getLastFeeChangeByTPAUserID() != null && withdrawalRequest
                .isLastFeeChangeWasPSUserInd());
    }

    /**
     * This method is called after a successful transition to the {@link WithdrawalStateEnum}
     * READY_FOR_ENTRY state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void afterEnteringReadyForEntryState() throws DistributionServiceException {

        final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
                .getManulifeCompanyId();

        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        final WithdrawalEmailVO withdrawalEmailVo = new WithdrawalEmailVO(withdrawalRequest
                .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
                .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
                .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
                .getLastName(), null, location, withdrawalRequest.getRequestDate(),
                withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
                withdrawalRequest.getContractInfo().getClientAccountRepEmail(), withdrawalRequest
                        .getLastFeeChangeByTPAUserID(), withdrawalRequest
                        .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
                withdrawalRequest.getEmployeeProfileId());

        WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();

        if (withdrawalRequest.getIsParticipantCreated()) {
            withdrawalEmailHandler.sendEmailToParticipant(withdrawalEmailVo,
                    WithdrawalEmailHandler.EVENT_TYPE_ID_APPROVED);
        }
    }

    /**
     * This method is called after a successful transition to the {@link WithdrawalStateEnum}
     * PENDING_APPROVAL state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void afterEnteringPendingApprovalState() throws DistributionServiceException {

        final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
                .getManulifeCompanyId();

        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        final WithdrawalEmailVO withdrawalEmailVO = new WithdrawalEmailVO(withdrawalRequest
                .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
                .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
                .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
                .getLastName(), null, location, withdrawalRequest.getRequestDate(),
                withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
                withdrawalRequest.getContractInfo().getClientAccountRepEmail(), withdrawalRequest
                        .getLastFeeChangeByTPAUserID(), withdrawalRequest
                        .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
                withdrawalRequest.getEmployeeProfileId());

        WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();
        try {
            // withdrawalEmailHandler.sendReadyForApprovalEmail(withdrawalEmailVO);
            if ((withdrawalRequest.getIsParticipantCreated() && StringUtils.equals(
                    withdrawalRequest.getCmaSiteCode(), WithdrawalRequest.CMA_SITE_CODE_EZK))) {
                withdrawalEmailHandler.sendEmailToParticipant(withdrawalEmailVO,
                        WithdrawalEmailHandler.EVENT_TYPE_ID_SUBMITTED);
            }
        } catch (WithdrawalEmailException withdrawalEmailException) {
            // FIXME - should we just eat the exception ?
            logger.error("Exception while trying to send ready for approval email on "
                    + "entering pending approval.", withdrawalEmailException);
        }

        WithdrawalHelper.fireWithdrawalPendingApprovalEvent(withdrawalRequest);

    }

    /**
     * This method is called after a successful transition to the {@link WithdrawalStateEnum}
     * PENDING_REVIEW state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void afterEnteringPendingReviewState() throws DistributionServiceException {

        final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
                .getManulifeCompanyId();

        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        final WithdrawalEmailVO withdrawalEmailVO = new WithdrawalEmailVO(withdrawalRequest
                .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
                .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
                .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
                .getLastName(), null, location, withdrawalRequest.getRequestDate(),
                withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
                withdrawalRequest.getContractInfo().getClientAccountRepEmail(), withdrawalRequest
                        .getLastFeeChangeByTPAUserID(), withdrawalRequest
                        .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
                withdrawalRequest.getEmployeeProfileId());

        final WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();
        try {
            // withdrawalEmailHandler.sendReadyForReviewEmail(withdrawalEmailVO);
            if (withdrawalRequest.getIsParticipantCreated()
                    && StringUtils.equals(withdrawalRequest.getCmaSiteCode(),
                            WithdrawalRequest.CMA_SITE_CODE_EZK)) {
                withdrawalEmailHandler.sendEmailToParticipant(withdrawalEmailVO,
                        WithdrawalEmailHandler.EVENT_TYPE_ID_SUBMITTED);
            }

        } catch (final WithdrawalEmailException withdrawalEmailException) {
            logger.error("Exception while trying to send email on " + "entering pending review"
                    + withdrawalEmailException.toString());
            throw new RuntimeException(withdrawalEmailException);
        } // end try/catch

        WithdrawalHelper.fireWithdrawalPendingReviewEvent(withdrawalRequest);

    }

    /**
     * This method is called after a successful transition to the {@link WithdrawalStateEnum} DENIED
     * state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void afterEnteringDeniedState() throws DistributionServiceException {

        final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
                .getManulifeCompanyId();

        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        final WithdrawalEmailVO withdrawalEmailVo = new WithdrawalEmailVO(withdrawalRequest
                .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
                .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
                .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
                .getLastName(), null, location, withdrawalRequest.getRequestDate(),
                withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
                withdrawalRequest.getContractInfo().getClientAccountRepEmail(), withdrawalRequest
                        .getLastFeeChangeByTPAUserID(), withdrawalRequest
                        .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
                withdrawalRequest.getEmployeeProfileId());

        WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();
        try {
            // withdrawalEmailHandler.sendDeniedEmail(withdrawalEmailVo);
            if (withdrawalRequest.getIsParticipantCreated()) {
                withdrawalEmailHandler.sendEmailToParticipant(withdrawalEmailVo,
                        WithdrawalEmailHandler.EVENT_TYPE_ID_DENIED);
            }

        } catch (WithdrawalEmailException withdrawalEmailException) {
            // FIXME - should we just eat the exception ?
            logger.error("Exception while trying to send email on " + "denied",
                    withdrawalEmailException);
        } // end try/catch

        WithdrawalHelper.fireWithdrawalDeniedEvent(withdrawalRequest);

    }

    /**
     * This method is called after a successful transition to the
     * {@link WithdrawalStateEnum#DELETED} state.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void afterEnteringDeletedState() throws DistributionServiceException {
        WithdrawalHelper.fireWithdrawalDeletedEvent(withdrawalRequest);
    }

    /**
     * Validate the Birth Date field.
     */
    protected void validateBirthDate() {

        final Date birthDate = withdrawalRequest.getBirthDate();
        if (birthDate == null) {

            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK,
                    WithdrawalRequestProperty.DATE_OF_BIRTH));
        } else {

            // Get todays date, without time components.
            final Date now = DateUtils.truncate(new Date(), Calendar.DATE);

            final Date youngestDateOfBirth = DateUtils.addYears(now, -1 * VALIDATION_MINIMUM_AGE);
            final Date oldestDateOfBirth = DateUtils.addYears(now, -1 * VALIDATION_MAXIMUM_AGE);

            // Check if birth date is between 15 and 150 years
            if (birthDate.after(youngestDateOfBirth) || birthDate.before(oldestDateOfBirth)) {

                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.DATE_OF_BIRTH_INVALID,
                        WithdrawalRequestProperty.DATE_OF_BIRTH));
            }

            // Check if birth date is different from system of record
            if (!ObjectUtils.equals(birthDate, withdrawalRequest.getParticipantInfo()
                    .getSystemOfRecordDateOfBirth())) {

                // Error if birth date is greater than date of enrollment
                final Date enrollmentDate = withdrawalRequest.getParticipantInfo()
                        .getParticipantEnrollmentDate();
                if ((enrollmentDate != null) && birthDate.after(enrollmentDate)) {

                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE,
                            WithdrawalRequestProperty.DATE_OF_BIRTH));
                }
            }
        }
    }
    
    
    protected void validateLegallyMarriedInd() {
        Boolean spousalConsentRequired=withdrawalRequest.getContractInfo().getSpousalConsentRequired();
        if (spousalConsentRequired == null || spousalConsentRequired) {
            if (StringUtils.isBlank(withdrawalRequest.getLegallyMarriedInd())) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.LEGALLY_MARRIED_IND_NULL,
                        WithdrawalRequestProperty.LEGALLY_MARRIED_IND));
            }
        }
    }

    /**
     * Validate the Expiration Date.
     */
    protected void validateExpirationDate() {

        final Date expirationDate = withdrawalRequest.getExpirationDate();
        String days="";
        if (expirationDate != null) {

            // Check if expiration date is more than maximum after request date
            final Date requestDate = getWithdrawalRequest().getRequestDate();
            final Date maximumExpirationDate = DateUtils.addDays(requestDate, ConfigurationFactory
                    .getConfiguration().getInt("withdrawal.expiry.maximumExpiryInterval"));

            if (maximumExpirationDate.before(expirationDate)) {
            	final Collection<String> messageParams = new ArrayList<String>(1);
            	days=CalendarUtils.getIntervalInDays(requestDate,expirationDate);
            	messageParams.add(days);
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.EXPIRATION_DATE_GREATER_THAN_MAXIMUM,
                        WithdrawalRequestProperty.EXPIRATION_DATE,messageParams));
            } // fi

            // Check if new expiration date is less than saved expiration date
            // It could be null if this request has not yet been saved.
            if (getSavedWithdrawalRequest() != null) {
                final Date savedExpirationDate = getSavedWithdrawalRequest().getExpirationDate();

                if ((savedExpirationDate != null) && expirationDate.before(savedExpirationDate)) {

                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.EXPIRATION_DATE_BEFORE_SAVED,
                            WithdrawalRequestProperty.EXPIRATION_DATE));
                } // fi
            } // fi
        } // fi
    }

    /**
     * Validate the Expiration Date Values.
     */
    private void validateExpirationDateValues() {

        // Only check the values if the date exists.
        if (getWithdrawalRequest().getExpirationDate() != null) {
            if (getSavedWithdrawalRequest() != null) {
                // Check if the expiration date is before the saved value.
                if (getWithdrawalRequest().getExpirationDate().before(
                        getSavedWithdrawalRequest().getExpirationDate())) {
                    getWithdrawalRequest().addMessage(
                            new WithdrawalMessage(
                                    WithdrawalMessageType.EXPIRATION_DATE_BEFORE_SAVED,
                                    "expirationDate"));
                } // fi
            } // fi

            // Check if the expiration date is more than 60d.
            final Date requestDate = getWithdrawalRequest().getRequestDate();
            final Date expirationDate = getWithdrawalRequest().getExpirationDate();
            final Date maximumExpirationDate = DateUtils.addDays(requestDate, ConfigurationFactory
                    .getConfiguration().getInt("withdrawal.expiry.maximumExpiryInterval"));
            if (maximumExpirationDate.before(expirationDate)) {
                getWithdrawalRequest().addMessage(
                        new WithdrawalMessage(
                                WithdrawalMessageType.EXPIRATION_DATE_GREATER_THAN_MAXIMUM,
                                "expirationDate"));
            } // fi
        } // fi
    }

    /**
     * Validate the WithdrawalReason - Mandatory check.
     */
    private void validateWithdrawalReasonMandatory() {

        final String reasonCode = withdrawalRequest.getReasonCode();

        if (StringUtils.isEmpty(reasonCode)) {
            // They haven't selected a reason code. This is always mandatory.
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.WITHDRAWAL_REASON_INVALID,
                    WithdrawalRequestProperty.REASON_CODE));
            return;
        } // fi
    }
    private void validateWithdrawalReasonForMinimumDistribution() {
    	final String reasonCode = withdrawalRequest.getReasonCode();
    	if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE)) {
    		
    		
            if (getAgeCalculate(withdrawalRequest.getBirthDate()) < 71.000 ){
                   withdrawalRequest.addMessage(new WithdrawalMessage(
                           WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT,
                           WithdrawalRequestProperty.REASON_CODE));
            
           } 
       } 
    }

    


    private static double getAgeCalculate(Date dateOfBirth) {
		double age;
		
		Calendar birthDay = Calendar.getInstance();
		birthDay.setTimeInMillis(dateOfBirth.getTime());
		// direct age calculation
		LocalDate participantDateOfBirth = LocalDate.of(birthDay.get(Calendar.YEAR), birthDay.get(Calendar.MONTH) + 1,
				birthDay.get(Calendar.DAY_OF_MONTH)); // specify year, month, date directly
		LocalDate currentDate = LocalDate.now(); // gets localDate
		Period diff = Period.between(participantDateOfBirth, currentDate); // difference between the dates is calculated
		age = diff.getYears() + diff.getMonths()/12.0;
		return age;
			
    }

    /**
     * Validate the WithdrawalReason.
     */
    private void validateWithdrawalReasonOnProceedToStep2OrSendForApprovalOrApproval() {

        final String reasonCode = withdrawalRequest.getReasonCode();

        // If the reason is terminiation, check they employment status and termination date.
        final EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
                .getInstance(new BaseEnvironment().getAppId());

        final Employee employee;
        try {
            employee = employeeServiceDelegate.getEmployeeByProfileId(new Long(withdrawalRequest
                    .getEmployeeProfileId()), withdrawalRequest.getContractId(), null);
        } catch (final SystemException systemException) {
            throw new RuntimeException(systemException);
        } // end try/catch

        final String employeeStatus;
        if (employee.getEmployeeDetailVO() != null) {
            employeeStatus = employee.getEmployeeDetailVO().getEmploymentStatusCode();
        } else {
            employeeStatus = null;
        } // fi

        if (StringUtils.equals(employeeStatus, WithdrawalRequest.EMPLOYEE_STATUS_TERMINATION_CODE)) {
            if (!((StringUtils.equals(reasonCode,
                    WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE)) || (StringUtils
                    .equals(reasonCode,
                            WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)))) {
                // The reason should match the participant status.
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS,
                        WithdrawalRequestProperty.REASON_CODE));
            } // fi
        } // fi

        if (StringUtils.equals(employeeStatus, WithdrawalRequest.EMPLOYEE_STATUS_RETIRED_CODE)) {
            if (!(StringUtils.equals(reasonCode,
                    WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE))) {
                // The reason should match the participant status.
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS,
                        WithdrawalRequestProperty.REASON_CODE));
            } // fi
        } // fi

        // If the reason is retirement.
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE)) {

            final Date birthDate = withdrawalRequest.getBirthDate();
            final Date retirementDate = withdrawalRequest.getRetirementDate();
            if ((birthDate != null) && (retirementDate != null)) {
                // Note: We read the retirement date that the user provides rather than using the
                // 'current age' (system time).

                if (isAgeLessThanNormalRetirementAge(birthDate, retirementDate)) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT,
                            WithdrawalRequestProperty.REASON_CODE));
                } // fi
            } // fi
        } // fi

        // If the reason is pre-retirement.
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE)) {

            final Date birthDate = withdrawalRequest.getBirthDate();
            if (birthDate != null) {
                // Note: The requirement reads 'current age' rather than using the retirement date
                // that the user provides. This is different than the retirement one, as we don't
                // ask for a retirement date for pre-retirement.

                if (isAgeLessThanEarlyRetirementAge(birthDate, new Date())) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT,
                            WithdrawalRequestProperty.REASON_CODE));
                } // fi
            } // fi
        } // fi

    }

    /**
     * Validates the payment to field.
     */
    public void validatePaymentTo() {

        final String paymentTo = withdrawalRequest.getPaymentTo();

        if (StringUtils.isEmpty(paymentTo)) {
            // They haven't selected a 'payment to'. This is always mandatory.
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.PAYMENT_TO_INVALID, "paymentTo"));
            return;
        } // fi
    }

    /**
     * This recalculates the values in the money type section.
     */
    public void recalculate() {

        boolean calledVesting = false;     
        if (withdrawalRequest.getIsParticipantCreated()) {
            isCensusInformationAvailableforParticipant();
            if (withdrawalRequest.getIsCensusInfoAvailable()) {
                if (!BooleanUtils.isTrue(withdrawalRequest.getVestingCalledInd())) {
                    // The vesting engine has not already been called, so we will call it.
                    WithdrawalVestingEngine vestingEngine = new WithdrawalVestingEngine();
                    vestingEngine.calculate(withdrawalRequest);

                    // if (!withdrawalRequest.getVestingCouldNotBeCalculatedInd()) {
                    withdrawalRequest.setVestingCalledInd(Boolean.TRUE);
                    calledVesting = true;
                    logger.debug("Called Vesting for Participant...");
                    // }

                }
            }
        } else {
        	// This will be true for non participant withdrawal request.
        	validateVestingForRecaluculate = true;
            // Check if we need to call the vesting engine to get the calculated values.
            if (!BooleanUtils.isTrue(withdrawalRequest.getVestingCalledInd())) {
                // The vesting engine has not already been called, so we will call it.
                WithdrawalVestingEngine vestingEngine = new WithdrawalVestingEngine();
                vestingEngine.calculate(withdrawalRequest);

                // Now we set the vesting called to true, so we don't call it again.
                withdrawalRequest.setVestingCalledInd(Boolean.TRUE);
                calledVesting = true;
                logger.debug("Called Vesting...");
            } // fi
        }
        // Tax must be updated before validations are run as they use the tax objects
        updateTax();

        // Validate federal tax
        validateFederalTaxUserSelection();

        // Validate state tax
        if (!withdrawalRequest.getIsParticipantCreated()) {
            validateStateTaxUserSelection();
        }

        // Clear messages.
        if (calledVesting) {
            logger.debug("Cleared all but vesting messages.");
            clearAllButVestingMessages();
        } else {
            logger.debug("Cleared all messages.");
            removeMessages();
        } // fi

        // We need to validate for recalculate.
        validateForRecalculate();

        // This uses the correct scale/precision.
        final BigDecimal oneHundredPercentWithdrawalPercentage = GlobalConstants.ONE_HUNDRED
                .setScale(WITHDRAWAL_PERCENTAGE_SCALE);

        // Special case to determine if the maximum value is used, as it can be set before the
        // user picks from the list.
        if (StringUtils.isBlank(getWithdrawalRequest().getAmountTypeCode())) {
            if ((!getWithdrawalRequest().getWithdrawalReasonSimple())
                    && (Boolean.TRUE.equals(getWithdrawalRequest().getParticipantLeavingPlanInd()))) {
                // Sets the default value in this case.
                getWithdrawalRequest().setAmountTypeCode(
                        WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE);
            } // fi
        } // fi

        // Now the vesting engine has been called, we calculate our values.
        moneyTypeLoop: for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {
            // New money type.
            if (withdrawalRequestMoneyType.getVestingPercentage() == null) {
            	if(("EE").equals(withdrawalRequestMoneyType.getMoneyTypeCategoryCode()))
            	{
            		withdrawalRequestMoneyType.setVestingPercentage(GlobalConstants.ONE_HUNDRED);
            		withdrawalRequestMoneyType.setVestingPercentageUpdateable(Boolean.FALSE);
            	}
            	else
            	{
            		withdrawalRequestMoneyType.setVestingPercentageUpdateable(Boolean.TRUE);
            	}
            }

            if (withdrawalRequestMoneyType.doErrorCodesExist()) {
                final Collection<WithdrawalMessage> errorCodes = withdrawalRequestMoneyType
                        .getErrorCodes();
                for (final WithdrawalMessage withdrawalMessage : errorCodes) {
                    if (WithdrawalMessageType.VESTED_PERCENTAGE_INVALID.equals(withdrawalMessage
                            .getWithdrawalMessageType())) {
                        // Clear the values for this row.

                        clearMoneyTypeData(withdrawalRequestMoneyType);
                        continue moneyTypeLoop;
                    } // fi
                } // end for
            } // fi

            // Note: The scale adjustment here is because the value is divided by 100 (shift 2).
            final BigDecimal vestingPercentage = NumberUtils.getPercentageValue(
                    withdrawalRequestMoneyType.getVestingPercentage(),
                    WithdrawalRequestMoneyType.VESTING_PERCENTAGE_SCALE
                            + NumberUtils.LOG_BASE_TEN_OF_ONE_HUNDRED);

            // Updates the available withdrawal amount (vesting% * totalBalance).
            if (vestingPercentage == null) {
                // No %, so clear the rest of the values.
                clearMoneyTypeData(withdrawalRequestMoneyType);
                continue;
            } else {
                final BigDecimal totalBalance = withdrawalRequestMoneyType.getTotalBalance();
                final BigDecimal availableAmount = NumberUtils.multiply(totalBalance,
                        vestingPercentage, GlobalConstants.DEFAULT_AMOUNT_SCALE,
                        GlobalConstants.DEFAULT_ROUNDING_MODE);

                withdrawalRequestMoneyType.setAvailableWithdrawalAmount(availableAmount);
            } // fi

            // If not simple.
            if (!withdrawalRequest.getWithdrawalReasonSimple()) {
                if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                        WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE)) {
                    // Set the Requested % to 100%.
                    withdrawalRequestMoneyType
                            .setWithdrawalPercentage(oneHundredPercentWithdrawalPercentage);

                    // Set the requested amount.
                    withdrawalRequestMoneyType.setWithdrawalAmount(withdrawalRequestMoneyType
                            .getAvailableWithdrawalAmount());
                } // fi
            } // fi

            // Now we calculate the fields for display/edit.
            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {
                // They are selecting based off of percentage, so we calculate the amount
                // from the given percentage.
                // Note: The "+ 2" here is because the value is divided by 100 (shift 2).
                final BigDecimal withdrawalAmount = NumberUtils
                        .multiply(
                                withdrawalRequestMoneyType.getAvailableWithdrawalAmount(),
                                NumberUtils.getPercentageValue(withdrawalRequestMoneyType
                                        .getWithdrawalPercentage(),
                                        WithdrawalRequestMoneyType.WITHDRAWAL_PERCENTAGE_SCALE + 2),
                                GlobalConstants.DEFAULT_AMOUNT_SCALE,
                                GlobalConstants.DEFAULT_ROUNDING_MODE);

                withdrawalRequestMoneyType.setWithdrawalAmount(withdrawalAmount);
            } // fi

            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                // They are selecting based off of amount, so we calculate the percentage
                // from the given amount.
                if ((withdrawalRequestMoneyType.getAvailableWithdrawalAmount() != null)
                        && (withdrawalRequestMoneyType.getAvailableWithdrawalAmount().compareTo(
                                BigDecimal.ZERO) != 0)) {
                    final BigDecimal withdrawalPercentage = NumberUtils.divide(
                            withdrawalRequestMoneyType.getWithdrawalAmount(),
                            withdrawalRequestMoneyType.getAvailableWithdrawalAmount(),
                            WITHDRAWAL_PERCENTAGE_SCALE, GlobalConstants.DEFAULT_ROUNDING_MODE);

                    withdrawalRequestMoneyType.setWithdrawalPercentage(withdrawalPercentage);
                } else {
                    // Either the available is null or zero, so we can't calculate a percentage.
                    withdrawalRequestMoneyType.setWithdrawalPercentage(null);
                } // fi
            } // fi
            if(StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE)){
            	if("EEDEF".equals(withdrawalRequestMoneyType.getMoneyTypeId())){
            		if ((withdrawalRequestMoneyType.getAvailableHarshipAmount() != null)
                            && (withdrawalRequestMoneyType.getAvailableHarshipAmount().compareTo(
                                    BigDecimal.ZERO) != 0)) {
            			withdrawalRequestMoneyType.setWithdrawalPercentage(oneHundredPercentWithdrawalPercentage);
            			
            		}
            	}
            	else {
                	withdrawalRequestMoneyType.setWithdrawalPercentage(null);
                }
            }
        } // end for
        if(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode())){
            validateInvalidMoneyTypeForHardship();
            validateEEDFFMaximumHarshipAmount();
            validateMaximumHarshipAmount();
            validateMinimumHarshipAmount();
            validateHardshipAmount();
            validateAvailableHardshipAmount();
            }
        // Now that we've updated the values, there are some checks to be performed.
        if (getWithdrawalRequest().getShowTaxWitholdingSection()) {
            validateForStateTaxForParticipantCreated();
        }

        validateForRecalculateAfterUpdates();

    }
    public static void  updatedPayeesForMultipleDestination(WithdrawalRequest request){
    	WithdrawalRequestHelper.updatedPayeesForMultipleDestination(request);
    }

    /**
     * Clears the MoneyType Data.
     * 
     * @param withdrawalRequestMoneyType The Money Type to clear.
     */
    private void clearMoneyTypeData(final WithdrawalRequestMoneyType withdrawalRequestMoneyType) {
        withdrawalRequestMoneyType.setAvailableWithdrawalAmount(null);
        // Only clear withdrawal amount if not specific amount (otherwise we clear user input)
        if (!StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
            withdrawalRequestMoneyType.setWithdrawalAmount(null);
        }
        // Only clear percentage if not percent by moneytype (otherwise we clear user input)
        if (!StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {
            withdrawalRequestMoneyType.setWithdrawalPercentage(null);
        }
    }

    /**
     * Validates the disability date.
     */
    public void validateDisabilityDate() {

        final String reasonCode = withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE)) {

            final Date disabilityDate = withdrawalRequest.getDisabilityDate();
            if (disabilityDate == null) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.DISABILITY_DATE_MISSING_ERROR,
                        WithdrawalRequestProperty.DISABILITY_DATE));
            }
        }
    }

    /**
     * Validates the retirement date.
     */
    public void validateRetirementDate() {

        final String reasonCode = withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE)) {

            final Date retirementDate = withdrawalRequest.getRetirementDate();
            if (retirementDate == null) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.RETIREMENT_DATE_MISSING_ERROR,
                        WithdrawalRequestProperty.RETIREMENT_DATE));
            }
            /*
             * Don't redefault the date to contract effective date. this is else { // Check if
             * retirement date is before contract effective date final Date contractEffectiveDate =
             * withdrawalRequest.getParticipantInfo() .getContractEffectiveDate(); if
             * ((contractEffectiveDate != null) && (retirementDate.before(contractEffectiveDate))) {
             * // Add error message and update retirement date withdrawalRequest.addMessage(new
             * WithdrawalMessage( WithdrawalMessageType.RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE,
             * WithdrawalRequestProperty.RETIREMENT_DATE));
             * withdrawalRequest.setRetirementDate(contractEffectiveDate); // Mark withdrawal as
             * having updated data withdrawalRequest.setForceData(true); } }
             */
        }
    }

    /**
     * Validates the irsDistributionCodeLoanClosure.
     * @throws SystemException 
     */
    public void validateIrsDistributionCodeLoanClosure() throws SystemException {

        String irsDistributionCodeLoanClosureNew = withdrawalRequest.getIrsDistributionCodeLoanClosure();
        String irsDistributionCodeLoanClosureOld=null;
        if(null!=withdrawalRequest.getSubmissionId()) {
        irsDistributionCodeLoanClosureOld = new WithdrawalDao().getIrsDistCodeLoanClosure(withdrawalRequest.getSubmissionId());
        }
        if(null==irsDistributionCodeLoanClosureOld) {
        	irsDistributionCodeLoanClosureOld=StringUtils.EMPTY;
        }
        if(null==irsDistributionCodeLoanClosureNew) {
        	irsDistributionCodeLoanClosureNew=StringUtils.EMPTY;
        }
		if ((StringUtils.isEmpty(irsDistributionCodeLoanClosureNew)) && ((StringUtils.equalsIgnoreCase(irsDistributionCodeLoanClosureOld,
				WithdrawalRequest.IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_1))
				|| (StringUtils.equalsIgnoreCase(irsDistributionCodeLoanClosureOld,
						WithdrawalRequest.IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_2))
				|| (StringUtils.equalsIgnoreCase(irsDistributionCodeLoanClosureOld,
						WithdrawalRequest.IRS_DISTRIBUTION_CODE_LOAN_CLOSURE_7)))) {
         
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.IRS_DISTRIBUTION_CODE_FOR_LOANS_ERROR,
                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN));
        }
    }

    /**
     * Validates the termination date.
     */
    public void validateTerminationDate() {

        final String reasonCode = withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE)
                || StringUtils.equals(reasonCode,
                        WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)) {

            final Date terminationDate = withdrawalRequest.getTerminationDate();
            if (terminationDate == null) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR,
                        WithdrawalRequestProperty.TERMINATION_DATE));
            }
            /*
             * Don't' reset the termination date to contract effective date. else { // Check if
             * termination date is before contract effective date final Date contractEffectiveDate =
             * withdrawalRequest.getParticipantInfo() .getContractEffectiveDate(); if
             * ((contractEffectiveDate != null) && (terminationDate.before(contractEffectiveDate)))
             * { // Add error message, update termination date withdrawalRequest.addMessage(new
             * WithdrawalMessage( WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE,
             * WithdrawalRequestProperty.TERMINATION_DATE));
             * withdrawalRequest.setTerminationDate(contractEffectiveDate); // Mark withdrawal as
             * having updated data withdrawalRequest.setForceData(true); } }
             */
        }  
    }
    
    /**
     * Validates the termination date against future date. Since, the requirement is to allow
     * the user to move back and forth between Step 1 and Step 2 screens, this validation has 
     * been done in a separate method.
     */
    public void validateFutureDatedTerminationOrRetirementDate() {

        final String reasonCode = withdrawalRequest.getReasonCode();
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE)
                || StringUtils.equals(reasonCode,
                        WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)) {

            final Date terminationDate = withdrawalRequest.getTerminationDate();
            if (terminationDate != null) {
            	
            //Get todays date
        	final Date currentDate = DateUtils.truncate(new Date(), Calendar.DATE);        	
	            if(terminationDate.after(currentDate)){
	            	withdrawalRequest.addMessage(new WithdrawalMessage(
	                			WithdrawalMessageType.TERMINATION_DATE_EXCEEDED,
	     				WithdrawalRequestProperty.TERMINATION_DATE));
	            }
            }
        }else if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE)) {
        	
            final Date retirementDate = withdrawalRequest.getRetirementDate();
            if (retirementDate != null) {
        	
            	
            final Date currentDate = DateUtils.truncate(new Date(), Calendar.DATE);        	
	            if(retirementDate.after(currentDate)){
	            	withdrawalRequest.addMessage(new WithdrawalMessage(
	                			WithdrawalMessageType.RETIREMENT_DATE_EXCEEDED,
	     				WithdrawalRequestProperty.RETIREMENT_DATE));

	            }
            }
        }
    }
    
    /**
     * Validates the Disability date against future date. Since, the requirement is to allow
     * the user to move back and forth between Step 1 and Step 2 screens, this validation has 
     * been done in a separate method.
     */
    public void validateFutureDatedDisabilityDate() { 
    final String reasonCode = withdrawalRequest.getReasonCode();
    if (StringUtils.equals(reasonCode,
            WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE)) {
    	  final Date disabilityDate = withdrawalRequest.getDisabilityDate();
          if (disabilityDate != null) {        	
          //Get todays date
      	final Date currentDate = DateUtils.truncate(new Date(), Calendar.DATE);        	
              if(disabilityDate.after(currentDate)){
              	withdrawalRequest.addMessage(new WithdrawalMessage(
                  			WithdrawalMessageType.DISABILITY_DATE_EXCEEDED,
       				WithdrawalRequestProperty.DISABILITY_DATE));
              }
          }
      }
      
    }

    /**
     * This method processes all of the validations that are run when the user performs a
     * 'recalculate'.
     */
    protected void validateForRecalculate() {

        if (withdrawalRequest.getIsParticipantCreated()) {
            validateTerminationDate();
            validateRetirementDate();
        }

        // Validate Amount Type
        validateAmountType();

        // Validate Specific Amount
        validatePreRecalculateSpecificAmount();

        // Validate the money type values.
        validateMoneyTypes();
        if(withdrawalRequest.getPaymentTo().equals("M")) {      	
        	validateMultipayee();
        
        }
        // Validate TPA Fee Amount
        // Validate TPA Fee Type
        validateTpa();

        // Validate Federal Tax % and State Tax %
        validateTaxes();

        // Only need to validate deleted if draft or post draft
        if (withdrawalRequest.getSubmissionId() != null) {

            validateRequestHasNotBeenDeleted();
        }
    }
    private void validateMultipayee() {
 		// TODO Auto-generated method stub
    	 
           //validate paydirectly to me only when PA and PAAT combination and only BOT ,else its optional section
     		validatePaydirectly();   
     	     //validate amount text field
     		validateFollowingAmount();   
     		//validate rollover section
			validateRollover();

      
 	}

	protected void validateRollover() {
		int count = 0;
		int unique_count = 0;
		if(withdrawalRequest.getPayDirectlyTome() == null){
		if (!validateUnique(withdrawalRequest.getNratCategory(), withdrawalRequest.getTbCategory(),
				withdrawalRequest.getRbCategory())) {
			unique_count++;
		}
		if (withdrawalRequest.getTbCategory() == null || withdrawalRequest.getRbCategory() == null
				|| withdrawalRequest.getNratCategory() == null) {
			count++;
		}
		}
		
		if(withdrawalRequest.getPayDirectlyTome() != null && withdrawalRequest.getPayDirectlyTome().equals("PAAT")) {    	
	         if(withdrawalRequest.getTbCategory() == null || withdrawalRequest.getRbCategory() == null) {
		    	 count++;
		     } 
	    	}
	    	if(withdrawalRequest.getPayDirectlyTome() != null && withdrawalRequest.getPayDirectlyTome().equals("PAR")) {
	     		
	             if(withdrawalRequest.getNratCategory() == null || withdrawalRequest.getTbCategory() == null) {
	    	    	 count++;
	    	     } 
	     	}
	    	if(withdrawalRequest.getPayDirectlyTome() != null && withdrawalRequest.getPayDirectlyTome().equals("PA")) {
	         	
	         	 if(withdrawalRequest.getNratCategory() == null ||withdrawalRequest.getTbCategory() == null || withdrawalRequest.getRbCategory() == null) {
	    	    	 count++;
	    	     }
	    	}
		
		if (count > 0) {
				withdrawalRequest.addMessage(
 					new WithdrawalMessage(WithdrawalMessageType.MISSING_MULTIPLE_DESTINATION_SELECTION, "missingRollover"));
		} else if (unique_count > 0) {
			withdrawalRequest
					.addMessage(new WithdrawalMessage(WithdrawalMessageType.SELECTED_ONE_PAYEE, "selectOnePayee"));
		}
	}

	protected void validateFollowingAmount() {
		if (withdrawalRequest.getPayDirectlyTome() != null && withdrawalRequest.getPayDirectlyTome().equals("PA")) {

			if (withdrawalRequest.getPayDirectlyTomeAmount() == null) {
// 				// We are missing a value.
				withdrawalRequest.addMessage(new WithdrawalMessage(WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
						"payDirectlyTomeAmount"));

			} else {
				// Verify that amount is not null and is greater than or equal to zero
				if ((NumberUtils.isZeroValue(withdrawalRequest.getPayDirectlyTomeAmount()))) {
					withdrawalRequest.addMessage(new WithdrawalMessage(WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
							"payDirectlyTomeAmount"));
				} // fi
// 					// Verify that number is not larger than total balance (if non-null)
				final BigDecimal totalBalance = getWithdrawalRequest().getTotalBalance();

				if ((totalBalance != null)
						&& (withdrawalRequest.getPayDirectlyTomeAmount().compareTo(totalBalance) > 0)) {
					withdrawalRequest.addMessage(new WithdrawalMessage(
							WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_BALANCE, "payDirectlyTomeAmount"));
				}
			}

		}
	}
   
	private void validatePaydirectly() {
			// validate for EAR with ROTH
		if (withdrawalRequest.getValidatePA() != null && withdrawalRequest.getValidatePA().equals("validatePA")
				&& withdrawalRequest.getValidatePAAT() != null
				&& withdrawalRequest.getValidatePAAT().equals("validatePAAT")
				&& withdrawalRequest.getValidatePAR() == null && withdrawalRequest.getNratCategory() != null
				&& withdrawalRequest.getNratCategory().equals("NA") && withdrawalRequest.getTbCategory() != null
				&& withdrawalRequest.getTbCategory().equals("NA")) {
			if (withdrawalRequest.getPayDirectlyTome() == null) {
				// withdrawalRequest.addMessage(
				// new
				// WithdrawalMessage(WithdrawalMessageType.PAY_DIRECT_TO_ME_OPTIONLA_SEC_USER_INPUT,
				// "payDirectlyTome"));
				withdrawalRequest.addMessage(new WithdrawalMessage(
						WithdrawalMessageType.MISSING_MULTIPLE_DESTINATION_SELECTION, "payDirectlyTome"));
         
			}
		}

		// validate for only BOT
		if (withdrawalRequest.getValidatePA() != null && withdrawalRequest.getValidatePA().equals("validatePA")
				&& withdrawalRequest.getValidatePAAT() == null && withdrawalRequest.getValidatePAR() == null) {
			if (withdrawalRequest.getPayDirectlyTome() == null) {
				//withdrawalRequest.addMessage(new WithdrawalMessage(
				//		WithdrawalMessageType.MISSING_MULTIPLE_DESTINATION_SELECTION, "payDirectlyTome"));
                  //validate amount text field
				if (withdrawalRequest.getPayDirectlyTomeAmount() == null) {
//	 				// We are missing a value.
					withdrawalRequest.addMessage(new WithdrawalMessage(WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
							"payDirectlyTomeAmount"));

				} else {
					// Verify that amount is not null and is greater than or equal to zero
					if ((NumberUtils.isZeroValue(withdrawalRequest.getPayDirectlyTomeAmount()))) {
						withdrawalRequest.addMessage(new WithdrawalMessage(WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
								"payDirectlyTomeAmount"));
					} // fi
//	 					// Verify that number is not larger than total balance (if non-null)
					final BigDecimal totalBalance = getWithdrawalRequest().getTotalBalance();

					if ((totalBalance != null)
							&& (withdrawalRequest.getPayDirectlyTomeAmount().compareTo(totalBalance) > 0)) {
						withdrawalRequest.addMessage(new WithdrawalMessage(
								WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_BALANCE, "payDirectlyTomeAmount"));
					}
				}    
			}
		}
	}

     
    
    private boolean validateUnique(String v1, String v2, String v3) {
    	Set<String> vlist = new HashSet<String>();
    	if(v1 != null && !v1.equals("NA") && !vlist.add(v1)) {    	
    		return false;
    	}
    	if(v2 != null && !v2.equals("NA") && !vlist.add(v2)) {    		
    		return false;
    	}
    	if(v3 != null && !v3.equals("NA") && !vlist.add(v3)) {    		
    		return false;
    	}    	
    	return true;
    }
    /**
     * Validate Option For Unvested Amount.
     */
    private void validateOptionForUnvestedAmount() {
        // Only validate if the field is shown.
        if (getWithdrawalRequest().getShowOptionForUnvestedAmount()) {

            // Check for mandatory value.
            if (StringUtils.isBlank(withdrawalRequest.getUnvestedAmountOptionCode())) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.OPTION_FOR_UNVESTED_AMOUNT_INVALID,
                        "unvestedAmountOptionCode"));
            } // fi
        } // fi
    }

    /**
     * Validate Taxes.
     */
    private void validateTaxes() {

        final WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient) getFirstRecipient();

        final BigDecimal federalPercentage = withdrawalRequestRecipient.getFederalTaxPercent();
        final BigDecimal statePercentage = withdrawalRequestRecipient.getStateTaxPercent();

        final boolean showTax = withdrawalRequest.getShowTaxWitholdingSection();
        final boolean showStateTax = withdrawalRequestRecipient.getShowStateTax();
        if (showTax) {
            if (federalPercentage == null) {
                // Mandatory if shown.
                withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.FEDERAL_TAX_INVALID, "federalTaxPercent"));
            } // fi
            if (showStateTax) {
                if (statePercentage == null) {
                    // Mandatory if shown.
                    withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.STATE_TAX_INVALID, "stateTaxPercent"));
                } else {
                    // Validate state tax range.
                    if (!(NumberUtils.isInPercentageRange(statePercentage))) {
                        withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.STATE_TAX_INVALID, "stateTaxPercent"));
                    } // fi

                    final BigDecimal taxPercentageMaximum = withdrawalRequestRecipient
                            .getStateTaxVo().getTaxPercentageMaximum();
                    // Validate it does not exceed the maximum tax rate in the table.
                    if (statePercentage.compareTo(taxPercentageMaximum) > 0) {

                        final Collection<String> messageParameters = new ArrayList<String>(1);
                        messageParameters.add(taxPercentageMaximum.toString());

                        withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.STATE_TAX_EXCEEDS_MAXIMUM, "stateTaxPercent",
                                messageParameters));
                    } // fi
                    if (WithdrawalRequestRecipient.PR_STATE
							.equals(withdrawalRequestRecipient.getStateTaxVo().getStateCode())
								&& WithdrawalRequestRecipient.PR_STATE
									.equals(withdrawalRequest.getContractIssuedStateCode())) {
						validateStateTaxForPuertoRicoState(withdrawalRequestRecipient);
					}
                } // fi
            } // fi

            if ((federalPercentage != null) && (statePercentage != null)) {
                boolean hasTheOverOneHundredError = false;
                if (StringUtils.equals(withdrawalRequestRecipient.getStateTaxTypeCode(),
                        StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_WITHDRAWAL)) {
                    // Ensure the sum is < 100.
                    final BigDecimal result = federalPercentage.add(statePercentage);
                    if (result.compareTo(GlobalConstants.ONE_HUNDRED) >= 0) {
                        hasTheOverOneHundredError = true;
                    } // fi
                } // fi
                if (StringUtils.equals(withdrawalRequestRecipient.getStateTaxTypeCode(),
                        StateTaxVO.STATE_TAX_TYPE_CODE_PERCENTAGE_OF_FEDERAL_TAX)) {
                    // Ensure the sum is < 100.
                    final BigDecimal result = federalPercentage.add(NumberUtils.multiply(
                            statePercentage, NumberUtils.getPercentageValue(federalPercentage, 4)));
                    if (result.compareTo(GlobalConstants.ONE_HUNDRED) >= 0) {
                        hasTheOverOneHundredError = true;
                    } // fi
                } // fi
                if (hasTheOverOneHundredError) {
                    withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.TAXES_OVER_ONE_HUNDRED_PERCENT,
                            "federalTaxPercent"));
                } // fi

                // Now check that state is zero if federal is zero.
				if (federalPercentage.compareTo(BigDecimal.ZERO) == 0 && (!WithdrawalRequestRecipient.PR_STATE
						.equals(withdrawalRequestRecipient.getStateTaxVo().getStateCode())
						&& !WithdrawalRequestRecipient.CT_STATE.equals(withdrawalRequest.getParticipantStateOfResidence()))) {
					   // it's 0.
                    if (statePercentage.compareTo(BigDecimal.ZERO) != 0) {
                        withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.STATE_TAX_NOT_ZERO_WHEN_FEDERAL_IS,
                                "stateTaxPercent"));
                    } // fi
                } // fi
            } // fi
        } // fi
    }

    /**
     * This method is used for validating the Puerto Rico state tax, and is being
     * implemented as part of the ME March 2016 CL 131784.
     * 
     * @param withdrawalRequestRecipient
     */
    private void validateStateTaxForPuertoRicoState(
			WithdrawalRequestRecipient withdrawalRequestRecipient) {
    	final BigDecimal stateTaxPCT = withdrawalRequestRecipient.getStateTaxPercent();
		final Boolean rollOverInd = withdrawalRequestRecipient.getStateTaxVo()
				.getRolloverIndicator();
		final BigDecimal defaultStateTaxPct = withdrawalRequestRecipient.getStateTaxVo().getDefaultTaxRatePercentage();
    	if(rollOverInd.equals(true)){
			if((stateTaxPCT.compareTo(defaultStateTaxPct) < 0 
					&& stateTaxPCT.compareTo(GlobalConstants.ZERO_AMOUNT) > 0)
							|| stateTaxPCT.compareTo(withdrawalRequestRecipient
		                            .getStateTaxVo().getTaxPercentageMaximum()) > 0) {
				withdrawalRequestRecipient
						.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WD_STATE_TAX_INVALID_FOR_PR_STATE_ROLLOVER,"stateTaxPercent"));
    		}
    	}
    	else{
    		if((stateTaxPCT.compareTo(defaultStateTaxPct) < 0
    				&& stateTaxPCT.compareTo(GlobalConstants.ZERO_AMOUNT) > 0)
    						|| stateTaxPCT.compareTo(withdrawalRequestRecipient
		                            .getStateTaxVo().getTaxPercentageMaximum()) > 0) {
    			withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WD_STATE_TAX_INVALID_FOR_PR_STATE_NONROLLOVER,"stateTaxPercent"));
    		}
    		
    	}
	}

	/**
     * Validate the TPA fee mandatory rules.
     */
    protected void validateTpa() {

        for (final Fee fee : withdrawalRequest.getFees()) {
            WithdrawalRequestFee withdrawalFee = (WithdrawalRequestFee) fee;
            // Determine which fee type we should validate
            if (StringUtils.equals(WithdrawalRequestFee.PERCENT_TYPE_CODE, fee.getTypeCode())) {

                // Check for missing value
                if (withdrawalFee.getValue() == null) {
                    // We are missing a value.
                    withdrawalFee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID,
                            WithdrawalRequestProperty.FEE_VALUE));
                } else {

                    // Verify that percent is in the valid percent range
                    if (!NumberUtils.isInPercentageRange(fee.getValue())) {
                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }

                    // Verify that value is not above threshold
                    if (fee.getValue().compareTo(FEE_PERCENT_THRESHOLD) > 0) {

                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_PERCENT_THRESHOLD,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }
                }

            } else if (StringUtils.equals(WithdrawalRequestFee.DOLLAR_TYPE_CODE, fee.getTypeCode())) {

                if (fee.getValue() == null) {
                    // We are missing a value.
                    withdrawalFee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID,
                            WithdrawalRequestProperty.FEE_VALUE));

                } else {

                    // Verify that amount is not null and is greater than or equal to zero
                    if (!(NumberUtils.isGreaterThanOrEqualToZeroValue(fee.getValue()))) {
                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }

                    // Verify that number is not larger than threshold
                    if (fee.getValue().compareTo(FEE_DOLLAR_THRESHOLD) > 0) {
                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_DOLLAR_THRESHOLD,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }

                    // Verify that number is not larger than total balance (if non-null)
                    final BigDecimal totalBalance = getWithdrawalRequest().getTotalBalance();
                    if ((totalBalance != null) && (fee.getValue().compareTo(totalBalance) > 0)) {
                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }
                }

            } else {
                // Verify that value is null
                if (fee.getValue() != null) {
                    withdrawalFee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.TPA_FEE_TYPE_INVALID,
                            WithdrawalRequestProperty.FEE_VALUE));
                }
            }
        }
    }

    /**
     * Validate the TPA fee range rules (necessary for save).
     */
    protected void validateTpaFieldLimit() {

        for (final Fee fee : withdrawalRequest.getFees()) {
            WithdrawalRequestFee withdrawalFee = (WithdrawalRequestFee) fee;
            // Check if we are dealing with a value to check range against
            if (fee.getValue() != null) {

                // Verify size if type is dollar or not specified
                if (StringUtils.isBlank(fee.getTypeCode())
                        || StringUtils.equals(WithdrawalRequestFee.DOLLAR_TYPE_CODE, fee
                                .getTypeCode())) {

                    if (fee.getValue().compareTo(WithdrawalRequestFee.FEE_DATABASE_FIELD_LIMIT) > 0) {
                        // Number is larger than field can support
                        withdrawalFee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID,
                                WithdrawalRequestProperty.FEE_VALUE));
                    }
                }
            }
        }
    }

    /**
     * This method processes all of the validations that are run when the user performs a
     * 'recalculate', after we have updated the values.
     */
    private void validateForRecalculateAfterUpdates() {

        final BigDecimal totalRequestedWithdrawalAmount = withdrawalRequest
                .getTotalRequestedWithdrawalAmount();
        if (totalRequestedWithdrawalAmount != null  && !StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE)) {
            if (!(NumberUtils.isGreaterThanZeroValue(totalRequestedWithdrawalAmount))) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.TOTAL_REQUESTED_NOT_GREATER_THAN_ZERO));
            } // fi
        } // fi

        for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {

            if ((withdrawalRequestMoneyType.getAvailableWithdrawalAmount() != null)
                    && (withdrawalRequestMoneyType.getWithdrawalAmount() != null)) {
                if (withdrawalRequestMoneyType.getWithdrawalAmount().compareTo(
                        withdrawalRequestMoneyType.getAvailableWithdrawalAmount()) > 0) {
                    // CR07
                    final String reasonCode = withdrawalRequest.getReasonCode();
                    if (!StringUtils.equals(reasonCode,
                            WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
                            && !StringUtils.equals(reasonCode,
                                    WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE)
                            && !StringUtils
                                    .equals(
                                            reasonCode,
                                            WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE)) {
                        // end CR07
                        withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT,
                                "withdrawalAmount"));
                    }
                } // fi
            } // fi
        } // end for

        // Check for Mandatory Distribution Termination restrictions.
        if (StringUtils.equals(withdrawalRequest.getReasonCode(),
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)) {
            boolean errorFound = false;
            BigDecimal totalOfNonRolloverMoneyTypes = BigDecimal.ZERO;
            for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                    .getMoneyTypes()) {
                if (!(BooleanUtils.isTrue(withdrawalRequestMoneyType.getIsRolloverMoneyType()))) {
                    if (withdrawalRequestMoneyType.getWithdrawalAmount() != null) {
                        totalOfNonRolloverMoneyTypes = totalOfNonRolloverMoneyTypes
                                .add(withdrawalRequestMoneyType.getWithdrawalAmount());
                    } // fi
                } // fi
            } // end for
            if (totalOfNonRolloverMoneyTypes
                    .compareTo(MANDATORY_DISTRIBUTION_NON_ROLLOVER_THRESHHOLD_HIGH) > 0) {
                // Exceeds the threshold.
                errorFound = true;
            } // fi
            if ((!(errorFound))
                    && (!(StringUtils.equals(withdrawalRequest.getPaymentTo(),
                            WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)))) {
                if (totalRequestedWithdrawalAmount != null) {
                    if ((totalRequestedWithdrawalAmount
                            .compareTo(MANDATORY_DISTRIBUTION_NON_ROLLOVER_THRESHHOLD_LOW) > 0)) {
                        errorFound = true;
                    }
                } // fi
            } // fi
            if (errorFound) {
                withdrawalRequest
                        .addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.TOTAL_REQUESTED_AMOUNT_RESTRICTED_FOR_MANDATORY_DISTRIBUTION_TERMINATION,
                                "totalRequestedWithdrawalAmount"));
            } // fi
        } // fi

        if (withdrawalRequest.isWmsiSelected()) {
            if ((totalRequestedWithdrawalAmount != null)
                    && (totalRequestedWithdrawalAmount.compareTo(WMSI_THRESHOLD) < 0)) {
                // It's under the limit.
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WMSI_UNDER_THRESHHOLD,
                        "totalRequestedWithdrawalAmount"));
            } // fi
        } // fi

        // Post recalculation validation of specific amount
        validatePostRecalculateSpecificAmount();
    }

    /**
     * Validates the values in the money type row.
     */
    private void validateMoneyTypes() {

        BigDecimal sumOfRequestedAmounts = BigDecimal.ZERO;

        for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : getWithdrawalRequest()
                .getMoneyTypes()) {

			if (withdrawalRequest.getStatusCode().equals(WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode())
					|| validateVesting
					|| validateVestingForRecaluculate) {
                // Check vesting percentage.
                if (BooleanUtils
                        .isTrue(withdrawalRequestMoneyType.getVestingPercentageUpdateable())) {
                    if (!(NumberUtils.isInPercentageRange(withdrawalRequestMoneyType
                            .getVestingPercentage()))) {
                        // It's not a valid percentage.
                        withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.VESTED_PERCENTAGE_INVALID,
                                "vestingPercentage"));
                    } // fi
                } // fi
            }
			if(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE.equals(withdrawalRequest.getReasonCode()) && 
           			withdrawalRequestMoneyType.getMoneyTypeId().equals("EEROT") || withdrawalRequestMoneyType.getMoneyTypeId().equals("EERRT")){ 
				
				
				 if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
		                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
					 if ((NumberUtils.isZeroValue(withdrawalRequestMoneyType
		                        .getWithdrawalAmount())) || withdrawalRequestMoneyType.getWithdrawalAmount() == null) {
						 BigDecimal bd = new BigDecimal(0.0);
						 withdrawalRequestMoneyType.setWithdrawalAmount(bd);
						// validatePreRecalculateSpecificAmount();
		                }
				 
				 }
				 
				 if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
		                    WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {            	      	 
		                 if ((NumberUtils.isZeroValue(withdrawalRequestMoneyType
		                        .getWithdrawalPercentage())) || withdrawalRequestMoneyType.getWithdrawalAmount() == null ) {
		                	 BigDecimal bd = new BigDecimal(0.0);
							 withdrawalRequestMoneyType.setWithdrawalPercentage(bd);
		                } // fi
				 }
				
			}
				
			else {

            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                if (!(NumberUtils.isGreaterThanOrEqualToZeroValue(withdrawalRequestMoneyType
                        .getWithdrawalAmount()))) {
                    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_AMOUNT_NOT_GREATER_THAN_ZERO,
                            "withdrawalAmount"));
                } // fi
                if ((NumberUtils.isZeroValue(withdrawalRequestMoneyType.getTotalBalance()))
                        && (NumberUtils.isNotZeroValue(withdrawalRequestMoneyType
                                .getWithdrawalAmount()))) {
                    withdrawalRequestMoneyType
                            .addMessage(new WithdrawalMessage(
                                    WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_ZERO,
                                    "withdrawalAmount"));
                } // fi

                final BigDecimal totalBalance = withdrawalRequestMoneyType.getTotalBalance();
                final BigDecimal availableHarshipAmount = withdrawalRequestMoneyType. getAvailableHarshipAmount();
                final BigDecimal withdrawalAmount = withdrawalRequestMoneyType
                        .getWithdrawalAmount();
                final String withdrawalReasonCode = withdrawalRequest.getReasonCode();

                if ((withdrawalAmount != null) && (totalBalance != null)) {
                    // Error if the value exceeds.
                    if (withdrawalAmount.compareTo(totalBalance) > 0 && (!StringUtils.equals(withdrawalReasonCode,
            				WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE))) {
                        withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_BALANCE,
                                "withdrawalAmount"));
                    
                    } else if (NumberUtils.isWithinPercentage(totalBalance, withdrawalAmount,
                    				REQUESTED_AMOUNT_PERCENTAGE_THRESHOLD) && (!StringUtils.equals(withdrawalReasonCode,
                            				WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE))) {
              
                        	    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.REQUESTED_AMOUNT_WITHIN_THRESHOLD,
                                "withdrawalAmount"));
					} else if (StringUtils.equals(withdrawalReasonCode,
							WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
							&& (availableHarshipAmount != null)
							&& (availableHarshipAmount.compareTo(BigDecimal.ZERO) > 0)
							&& (NumberUtils.isWithinPercentage(availableHarshipAmount, withdrawalAmount,
									REQUESTED_AMOUNT_PERCENTAGE_THRESHOLD))) {
						if (("EEDEF".equals(withdrawalRequestMoneyType.getMoneyTypeId()))
								&& ((withdrawalAmount.compareTo(availableHarshipAmount) < 0)
										|| (withdrawalAmount.compareTo(availableHarshipAmount) == 0))) {

							withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
									WithdrawalMessageType.REQUESTED_AMOUNT_WITHIN_THRESHOLD_FOR_HA,
									"withdrawalAmount"));
						} else if ((!("EEDEF".equals(withdrawalRequestMoneyType.getMoneyTypeId())))
								&& ((withdrawalAmount.compareTo(totalBalance) < 0)
										|| (withdrawalAmount.compareTo(totalBalance) == 0))) {

							withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
									WithdrawalMessageType.REQUESTED_AMOUNT_WITHIN_THRESHOLD_FOR_HA,
									"withdrawalAmount"));
						}
					}
				} // fi
        	} // end else
            } // fi

            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {
                if (!(NumberUtils.isInPercentageRange(withdrawalRequestMoneyType
                        .getWithdrawalPercentage()))) {
                    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_PERCENTAGE_INVALID,
                            "withdrawalPercentage"));
                } // fi
                if ((NumberUtils.isZeroValue(withdrawalRequestMoneyType.getTotalBalance()))
                        && (NumberUtils.isNotZeroValue(withdrawalRequestMoneyType
                                .getWithdrawalPercentage()))) {
                    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_ZERO,
                            "withdrawalPercentage"));
                } // fi
            } // fi

            if (withdrawalRequestMoneyType.getWithdrawalAmount() != null) {
                sumOfRequestedAmounts = sumOfRequestedAmounts.add(withdrawalRequestMoneyType
                        .getWithdrawalAmount());
            } // fi
        } // end for
    }

    /**
     * Removes all messages set in the object (and it's sub-objects).
     */
    private void removeMessages() {
        withdrawalRequest.removeMessages();
    }

    /**
     * Validates the SpecificAmount database field limit.
     */
    protected void validateSpecificAmountFieldLimit() {

        // Verify size if type is specific amount
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                withdrawalRequest.getAmountTypeCode())) {

            if (withdrawalRequest.getWithdrawalAmount() != null) {
                if (withdrawalRequest.getWithdrawalAmount().compareTo(
                        WithdrawalRequest.SPECIFIC_AMOUNT_DATABASE_FIELD_LIMIT) > 0) {
                    // Number is larger than field can support
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
                            WithdrawalRequestProperty.SPECIFIC_AMOUNT));
                }
            }
        }
    }

    /**
     * Validates the Requested Percent database field limit.
     */
    protected void validateRequestedPercentFieldLimit() {

        for (final WithdrawalRequestMoneyType moneyType : withdrawalRequest.getMoneyTypes()) {
            if (moneyType.getWithdrawalPercentage() != null) {
                // Verify size if requested percent amount type
                if (moneyType.getWithdrawalPercentage().compareTo(
                        WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM) > 0) {
                    // Number is larger than field can support
                    moneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_PERCENTAGE_INVALID,
                            WithdrawalRequestProperty.REQUESTED_PERCENTAGE));
                }
            }
        }
    }

    /**
     * Validates the Requested Amount database field limit.
     */
    protected void validateRequestedAmountFieldLimit() {

        for (final WithdrawalRequestMoneyType moneyType : withdrawalRequest.getMoneyTypes()) {
            if (moneyType.getWithdrawalAmount() != null) {
                // Verify size if specific amount amount type
                if (moneyType.getWithdrawalAmount().compareTo(
                        WithdrawalRequestMoneyType.REQUESTED_AMOUNT_MAXIMUM) > 0) {
                    // Number is larger than field can support
                    moneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_AMOUNT_INVALID,
                            WithdrawalRequestProperty.REQUESTED_AMOUNT));
                }
            }
        }
    }

    /**
     * Validates the State Tax database field limit.
     */
    protected void validateStateTaxFieldLimit() {

        for (final Recipient myRecipient : withdrawalRequest.getRecipients()) {
            WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient) myRecipient;
            if (recipient.getStateTaxPercent() != null) {
                // Verify size if state tax is shown
                if (withdrawalRequest.getShowTaxWitholdingSection() && recipient.getShowStateTax()) {
                    if (recipient.getStateTaxPercent().compareTo(
                            WithdrawalRequestRecipient.PERCENTAGE_FIELD_LIMIT_MAXIMUM) > 0) {
                        // Number is larger than field can support
                        recipient.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.STATE_TAX_INVALID,
                                WithdrawalRequestProperty.STATE_TAX_PERCENT));
                    }
                }
            }
        }
    }

    /**
     * Validates the Vested Percent database field limit.
     */
    protected void validateVestingPercentFieldLimit() {

        if (withdrawalRequest.getStatusCode().equals(
                WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode())
                || validateVesting) {
            for (final WithdrawalRequestMoneyType moneyType : withdrawalRequest.getMoneyTypes()) {
                if (moneyType.getVestingPercentage() != null) {
                    // Verify size if editable
                    if (BooleanUtils.isTrue(moneyType.getVestingPercentageUpdateable())) {
                        if (moneyType.getVestingPercentage().compareTo(
                                WithdrawalRequestMoneyType.PERCENTAGE_FIELD_LIMIT_MAXIMUM) > 0) {
                            // Number is larger than field can support
                            moneyType.addMessage(new WithdrawalMessage(
                                    WithdrawalMessageType.VESTED_PERCENTAGE_INVALID,
                                    WithdrawalRequestProperty.VESTING_PERCENTAGE));
                        }
                    }
                }
            }
        }
    }

    /**
     * Validates the SpecificAmount field after recalculation in order to make use of values that
     * were updated during recalculation.
     */
    protected void validatePostRecalculateSpecificAmount() {

        // Only need to validate if amount type is specific amount and specific amount entered
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                withdrawalRequest.getAmountTypeCode())
                && (withdrawalRequest.getWithdrawalAmount() != null)) {

            // Check if specific amount is within threshold of total available if robust
            if (!withdrawalRequest.getWithdrawalReasonSimple()) {
                if (NumberUtils.isWithinPercentageInclusive(withdrawalRequest
                        .getTotalAvailableWithdrawalAmount(), withdrawalRequest
                        .getWithdrawalAmount(), SPECIFIC_AMOUNT_PERCENTAGE_THRESHOLD)) {

                    // Warning if in the threshold zone.
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD,
                            WithdrawalRequestProperty.SPECIFIC_AMOUNT));
                }
            }
        }
    }

    /**
     * Validates the SpecificAmount field.
     */
    protected void validatePreRecalculateSpecificAmount() {

        // Only need to validate if amount type is specific amount
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                withdrawalRequest.getAmountTypeCode())) {

            // Verify specific amount is larger than zero
            if (!NumberUtils.isGreaterThanZeroValue(withdrawalRequest.getWithdrawalAmount())) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT));
            }

            if (withdrawalRequest.getWithdrawalAmount() != null) {

                // Verify specific amount is less than or equal to total balance
                final BigDecimal totalBalance = withdrawalRequest.getTotalBalance();
                if ((totalBalance != null)
                        && (withdrawalRequest.getWithdrawalAmount().compareTo(totalBalance) > 0)) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE,
                            WithdrawalRequestProperty.SPECIFIC_AMOUNT));
                }

                // Verify specific amount is equal to total requested
                final BigDecimal totalRequested = withdrawalRequest
                        .getTotalRequestedWithdrawalAmount();
                if ((totalRequested != null)
                        && (withdrawalRequest.getWithdrawalAmount().compareTo(totalRequested) != 0)) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC,
                            WithdrawalRequestProperty.SPECIFIC_AMOUNT));
                }
            }
        }
    }

    /**
     * Validates the AmountType field.
     */
    protected void validateAmountType() {

        // The amount type is mandatory for recalculate.
        if (StringUtils.isBlank(withdrawalRequest.getAmountTypeCode())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.AMOUNT_TYPE_REQUIRED,
                    WithdrawalRequestProperty.AMOUNT_TYPE_CODE));
        } // fi
    }

    /**
     * Validates the Recipient Addresses.
     * 
     * @param withdrawalRequestRecipient The recipient to validate.
     */
    protected void validateRecipientAddresses(final Recipient withdrawalRequestRecipient) {

        validateAddress((Address) withdrawalRequestRecipient.getAddress(),
                RECIPIENT_ADDRESS_MESSAGES);
    }

    /**
     * Validates the Payee Addresses.
     * 
     * @param withdrawalRequestPayee The payee to validate.
     */
    protected void validatePayeeAddresses(final WithdrawalRequestPayee withdrawalRequestPayee) {

        // Check the address is valid.
        final Address address = (Address) withdrawalRequestPayee.getAddress();
        if (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE)) {
            validateAddress(address, CHECK_PAYEE_ADDRESS_MESSAGES);
        } else if (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE)
                || StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                        WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE)) {
            validateAddress(address, FI_PAYEE_ADDRESS_MESSAGES);
        }
    }

    /**
     * Validate the address.
     * 
     * @param address The address to validate.
     * @param addressMessageMap The {@link Map} of address messages to use if there are validation
     *            errors found.
     */
    private void validateAddress(final Address address,
            final Map<String, WithdrawalMessageType> addressMessageMap) {

        // Address line 1 is mandatory
        if (StringUtils.isBlank(address.getAddressLine1())) {
            address
                    .addMessage(new WithdrawalMessage(addressMessageMap
                            .get(ADDRESS_LINE_ONE_INVALID_KEY),
                            WithdrawalRequestProperty.ADDRESS_LINE_ONE));
        }
        // City is mandatory
        if (StringUtils.isBlank(address.getCity())) {
            address.addMessage(new WithdrawalMessage(addressMessageMap.get(CITY_INVALID_KEY),
                    WithdrawalRequestProperty.CITY));
        }
        // Country is mandatory
        if (StringUtils.isBlank(address.getCountryCode())) {
            address.addMessage(new WithdrawalMessage(addressMessageMap.get(COUNTRY_INVALID_KEY),
                    WithdrawalRequestProperty.COUNTRY_CODE));
        }
        // State and zip code are mandatory if country is USA
        if (StringUtils.equals(address.getCountryCode(), GlobalConstants.COUNTRY_CODE_USA)
                || StringUtils.isBlank(address.getCountryCode())) {
            if (StringUtils.isBlank(address.getStateCode())) {
                address.addMessage(new WithdrawalMessage(addressMessageMap.get(STATE_INVALID_KEY),
                        WithdrawalRequestProperty.STATE_CODE));
            }
            if (StringUtils.isBlank(address.getZipCode1())
                    || !StringUtils.isNumeric(address.getZipCode1())) {
                address.addMessage(new WithdrawalMessage(
                        addressMessageMap.get(ZIP_ONE_INVALID_KEY),
                        WithdrawalRequestProperty.ZIP_CODE));
            }
            // Zip code 2 is non-mandatory and numeric
            if (!StringUtils.isBlank(address.getZipCode2())
                    && !StringUtils.isNumeric(address.getZipCode2())) {
                address.addMessage(new WithdrawalMessage(
                        addressMessageMap.get(ZIP_TWO_INVALID_KEY),
                        WithdrawalRequestProperty.ZIP_CODE));
            }

            // Verify state and zip combination if zip numeric and both state and zip are present
            if (StringUtils.isNotBlank(address.getZipCode1())
                    && StringUtils.isNumeric(address.getZipCode1())
                    && StringUtils.isNotBlank(address.getStateCode())) {
                final boolean isValid = EnvironmentServiceHelper.getInstance()
                        .isUsZipCodeValidForState(address.getStateCode(), address.getZipCode1());
                if (!isValid) {
                    // The zip code was not in the valid range.
                    address.addMessage(new WithdrawalMessage(addressMessageMap
                            .get(ZIP_STATE_INVALID_KEY), WithdrawalRequestProperty.ZIP_CODE));
                }
            }
        }
    }

    /**
     * isEligibleForRollover returns true if the request is eligible, otherise it returns false.
     * 
     * @return boolean - True if eligible, false otherwise.
     */
    public boolean isEligibleForRollover() {
        final String reasonCode = withdrawalRequest.getReasonCode();

		if (StringUtils.equals(reasonCode,
				WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)
				|| StringUtils.equals(reasonCode,
								WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE)
				|| StringUtils.equals(reasonCode,
								WithdrawalRequest.WITHDRAWAL_REASON_QUALIFIED_DOMESTIC_RELATIONS_ORDER)
				|| StringUtils.equals(reasonCode,
								WithdrawalRequest.WITHDRAWAL_REASON_EXCESS_CONTRIBUTIONS)
				|| StringUtils.equals(reasonCode,
						WithdrawalRequest.WITHDRAWAL_REASON_EXCESS_DEFERRALS)
				|| StringUtils.equals(reasonCode,
								WithdrawalRequest.WITHDRAWAL_REASON_EXCESS_ANNUAL_ADDITIONS)) {
            // This is NOT eligible for rollover.
            return false;
        } // fi

        // Otherwise it is eligible for rollover.
        return true;
    }

    /**
     * This method updates the available options in the tax withholding section.
     */
    public void updateTax() {
        final boolean eligibleForRollover = isEligibleForRollover();

        final EnvironmentDAO environmentDAO = new EnvironmentDAO();

        final FederalTaxVO federalTaxVO;
        try {
            federalTaxVO = environmentDAO
                    .getCurrentFederalTax(eligibleForRollover, BigDecimal.ZERO);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting federal tax", systemException);
        } // end try/catch

        withdrawalRequest.setFederalTaxVo(federalTaxVO);

        for (final Recipient recipient : withdrawalRequest.getRecipients()) {
            WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient) recipient;

            final String stateOfResidenceCode = withdrawalRequestRecipient
                    .getStateOfResidenceCode();

            // Note: The state tax can come back as null (if the state isn't found (like the
            // 'Outside the US').
            final StateTaxVO stateTaxVO;
            try {
                stateTaxVO = environmentDAO.getCurrentStateTax(stateOfResidenceCode,
                        eligibleForRollover);
            } catch (final SystemException systemException) {
                throw new RuntimeException("Error getting state tax", systemException);
            } // end try/catch

            withdrawalRequestRecipient.setStateTaxVo(stateTaxVO);
        } // end for
    }

    /**
     * This method retrieves the available state tax options in the tax withholding section.
     * 
     * @return {@link Collection} - A Collection of {@link StateTaxVO} objects.
     */
    public Collection<StateTaxVO> getAllStateTaxOptions() {

        final boolean eligibleForRollover = isEligibleForRollover();
        final EnvironmentDAO environmentDAO = new EnvironmentDAO();

        final Collection<StateTaxVO> taxes;
        try {
            taxes = environmentDAO.getAllCurrentStateTaxes(eligibleForRollover);
        } catch (final SystemException systemException) {
            throw new RuntimeException("Error getting state taxes", systemException);
        } // end try/catch

        return taxes;
    }

    /**
     * performCommonDefaultSetup is called to execute the common default setup steps for initiate
     * and review.
     */
    public void performCommonDefaultSetup() {

        final Map lookupData = new WithdrawalLookupDataManager(getWithdrawalRequest()
                .getContractInfo(), getWithdrawalRequest().getParticipantInfo()
                .getParticipantStatusCode(), null).getLookupData();

        loadConfiguration();

        // Tax must be updated before validations are run as they use the tax objects
        updateTax();

        // Fees must be updated before validations are run as they use the fee objects
        updateFees();

        validateUserSelections(lookupData);
    }

    /**
     * Loads the current configuration for the withdrawal.
     */
    private void loadConfiguration() {

        final Configuration configuration = ConfigurationFactory.getConfiguration();

        // Determines if activity history is enabled or not, defaulting it to disabled.
        getWithdrawalRequest().setActivityHistoryEnabled(
                configuration.getBoolean(WithdrawalRequest.WITHDRAWAL_ACTIVITY_HISTORY_ENABLED_KEY,
                        Boolean.FALSE));
    }

    /**
     * This method fetches the lookup data.
     * 
     * @return Map - A {@link Map} of the lookup data.
     */
    public Map getLookupData() {

        final Collection<String> lookupDataKeys = new ArrayList<String>();
        lookupDataKeys.add(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);
        lookupDataKeys.add(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        lookupDataKeys.add(CodeLookupCache.HARDSHIP_REASONS);
        lookupDataKeys.add(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        lookupDataKeys.add(CodeLookupCache.LOAN_OPTION_TYPE);

        try {
            final Map map = EnvironmentServiceDelegate
                    .getInstance(new BaseEnvironment().getAppId()).getLookupData(lookupDataKeys);

            // Need to add the ZZ state of residence outside US option to the states collection
            ((Collection) map.get(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE))
                    .add(new DeCodeVO(WithdrawalRequestRecipient.STATE_OF_RESIDENCE_OUTSIDE_US,
                            StringUtils.EMPTY));

            // Withdrawal reasons are specific to the contract and participant
            final Collection reasons = WithdrawalInfoDao.getParticipantWithdrawalReasons(
                    getWithdrawalRequest().getParticipantInfo().getContractStatusCode(),
                    getWithdrawalRequest().getParticipantInfo().getParticipantStatusCode());
            map.put(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS, reasons);

            // Payment to options are specific to various participant information
            final Collection paymentToOptions = WithdrawalDataManager
                    .getParticipantPaymentToOptions(getWithdrawalRequest().getParticipantInfo());
            map.put(CodeLookupCache.PAYMENT_TO_TYPE, paymentToOptions);

            return map;

        } catch (final SystemException se) {
            throw ExceptionHandlerUtility.wrap(se);
        }
    }

    /**
     * Revalidates a number of fields to determine if their values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateUserSelections(final Map lookupData) {

        // Validate IRS code for Withdrawals
        validateIrsCodeForWithdrawalUserSelection(lookupData);

        // Validate IRS Code for Loans
       // validateIrsCodeForLoansUserSelection(lookupData);

        // Validate loan option
        validateLoanOptionUserSelection(lookupData);

        // Validate hardship reason
        validateHardshipReasonUserSelection(lookupData);

        // Validate state of residence
        validateStateOfResidenceUserSelection(lookupData);

        // Validate unvested money options
        validateUnvestedMoneyOptionsUserSelection(lookupData);

        // Validate federal tax
        validateFederalTaxUserSelection();

        // Validate state tax
        if (!getWithdrawalRequest().getIsParticipantCreated()) {
            validateStateTaxUserSelection();
        }

        // Validate loans
       // validateLoansUserSelection();

        // Validate fee details
        validateFeeDetailsUserSelection();

        // Validate withdrawal reason
        validateWithdrawalReasonUserSelection(lookupData);

        // Validate payment to
        validatePaymentToUserSelection(lookupData);
    }

    /**
     * Revalidates the Loan fields to determine if the values are necessary (based on the presence
     * of any loans).
     */
    public void validateLoansUserSelection() {

        // Reset loan fields if there are no longer any loans
        if (CollectionUtils.isEmpty(getWithdrawalRequest().getLoans())) {
            getWithdrawalRequest().setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
            getWithdrawalRequest().setLoanOption((StringUtils.EMPTY));
        }
    }

    /**
     * Revalidates the Fee fields to determine if the values are necessary (based on the contract
     * having a TPA).
     */
    public void validateFeeDetailsUserSelection() {

        // If contract has TPA then ensure that collection has at least one fee
        if ((getWithdrawalRequest().getParticipantInfo() != null)
                && (BooleanUtils.isTrue(getWithdrawalRequest().getParticipantInfo()
                        .getThirdPartyAdminId()))) {

            if (CollectionUtils.isEmpty(getWithdrawalRequest().getFees())) {
                getWithdrawalRequest().setFees(new ArrayList<Fee>() {
                    private static final long serialVersionUID = 1L;
                    {
                        add(new WithdrawalRequestFee() {
                            private static final long serialVersionUID = 1L;
                            {
                                this.setTypeCode(StringUtils.EMPTY);
                            }
                        });
                    }
                });
            }
        } else {

            // Reset fee fields if the contract is no longer TPA
            for (final Fee fee : getWithdrawalRequest().getFees()) {
                fee.setTypeCode(StringUtils.EMPTY);
                fee.setValue(null);
            }
        }
    }

    /**
     * Revalidates the IRS Code for Withdrawal field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateIrsCodeForWithdrawalUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData
                .get(CodeLookupCache.IRS_DISTRIBUTION_FOR_WITHDRAWALS);
        for (final Recipient myRecipient : getWithdrawalRequest().getRecipients()) {
            for (final Payee payee : myRecipient.getPayees()) {
                if (StringUtils.isNotBlank(payee.getIrsDistCode())
                        && !CollectionUtils.exists(collection, new CodeEqualityPredicate(payee
                                .getIrsDistCode()))) {

                    payee.setIrsDistCode(StringUtils.EMPTY);
                }
            }
        }
    }

    /**
     * Revalidates the IRS Code for Loans field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateIrsCodeForLoansUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData
                .get(CodeLookupCache.IRS_DISTRIBUTION_FOR_LOANS);
        if (StringUtils.isNotBlank(getWithdrawalRequest().getIrsDistributionCodeLoanClosure())
                && !CollectionUtils.exists(collection, new CodeEqualityPredicate(
                        getWithdrawalRequest().getIrsDistributionCodeLoanClosure()))) {

            getWithdrawalRequest().setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        }

    }

    /**
     * Revalidates the Loan Option field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateLoanOptionUserSelection(final Map lookupData) {

        logger.info("validateLoanOptionUserSelection> Entry with loan option ["
                + withdrawalRequest.getLoanOption() + "]");

        final Collection collection = (Collection) lookupData.get(CodeLookupCache.LOAN_OPTION_TYPE);
        if (StringUtils.isNotBlank(getWithdrawalRequest().getLoanOption())
                && !CollectionUtils.exists(collection, new CodeEqualityPredicate(
                        getWithdrawalRequest().getLoanOption()))) {

            withdrawalRequest.setLoanOption(withdrawalRequest.getDefaultLoanOption());
        }
        logger.info("validateLoanOptionUserSelection> Exit with loan option ["
                + withdrawalRequest.getLoanOption() + "]");
    }

    /**
     * Revalidates the Withdrawal Reason field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateWithdrawalReasonUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData
                .get(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
        if (StringUtils.isNotBlank(getWithdrawalRequest().getReasonCode())
                && !CollectionUtils.exists(collection, new CodeEqualityPredicate(
                        getWithdrawalRequest().getReasonCode()))) {

            if (getWithdrawalRequest().getIsPostDraft()) {
                // Post draft leaves invalid data and sets an invalid flag
                getWithdrawalRequest().setIsNoLongerValid(true);

                // Only add alert if not already present
                if (!CollectionUtils.exists(withdrawalRequest.getAlertCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT))) {
                    // Set initial message
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
                }
            } else {
                // Draft redefaults the invalid data
                getWithdrawalRequest().setReasonCode(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Revalidates the Payment To field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validatePaymentToUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData.get(CodeLookupCache.PAYMENT_TO_TYPE);
        // Check if payment to doesn't exist or if check payable now trustee but payment to is not
        if ((StringUtils.isNotBlank(getWithdrawalRequest().getPaymentTo()) && !CollectionUtils
                .exists(collection,
                        new CodeEqualityPredicate(getWithdrawalRequest().getPaymentTo())))
                || (StringUtils.equals(withdrawalRequest.getParticipantInfo()
                        .getChequePayableToCode(), ParticipantInfo.CHECK_PAYABLE_TO_TRUSTEE) && !StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, withdrawalRequest
                                .getPaymentTo()))) {

            if (getWithdrawalRequest().getIsPostDraft()) {
                // Post draft leaves invalid data and sets an invalid flag
                getWithdrawalRequest().setIsNoLongerValid(true);

                // Only add alert if not already present
                if (!CollectionUtils.exists(withdrawalRequest.getAlertCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT))) {
                    // Set initial message
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
                }
            } else {
                // Draft redefaults the invalid data
                getWithdrawalRequest().setPaymentTo(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Revalidates the State of Residence field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateStateOfResidenceUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData
                .get(CodeLookupCache.USA_STATE_WITHOUT_MILITARY_TYPE);
        for (final Recipient recipient : getWithdrawalRequest().getRecipients()) {

            if (StringUtils.isNotBlank(recipient.getStateOfResidenceCode())
                    && !CollectionUtils.exists(collection, new CodeEqualityPredicate(recipient
                            .getStateOfResidenceCode()))) {
                recipient.setStateOfResidenceCode(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Revalidates the Unvested Money Options field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateUnvestedMoneyOptionsUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData
                .get(CodeLookupCache.OPTIONS_FOR_UNVESTED_AMOUNTS);
        if (StringUtils.isNotBlank(getWithdrawalRequest().getUnvestedAmountOptionCode())
                && !CollectionUtils.exists(collection, new CodeEqualityPredicate(
                        getWithdrawalRequest().getUnvestedAmountOptionCode()))) {

            getWithdrawalRequest().setUnvestedAmountOptionCode(StringUtils.EMPTY);
        }
    }

    /**
     * Revalidates the Federal Tax field to determine if the values are still allowed.
     */
    public void validateFederalTaxUserSelection() {

        for (final Recipient recipient : getWithdrawalRequest().getRecipients()) {
        	
        	// CL 131784 begin
        	if(WithdrawalRequestRecipient.PR_STATE.equals(recipient.getStateOfResidenceCode())
        			&& WithdrawalRequestRecipient.PR_STATE.equals(getWithdrawalRequest().getContractIssuedStateCode())
        				&& recipient.getFederalTaxPercent() != null){
        		continue;
        	}
        	// CL 131784 end

            // Redefault if federal tax is zero and is eligible for rollover
            if (NumberUtils.isZeroValue(recipient.getFederalTaxPercent())
                    && getWithdrawalRequest().getFederalTaxVo().getRolloverIndicator()) {
                recipient.setFederalTaxPercent(getWithdrawalRequest().getFederalTaxVo()
                        .getTaxPercentage());
            }

            // Redefault if the federal tax is non-zero and the rate has changed
            /*
            if (NumberUtils.isNotZeroValue(recipient.getFederalTaxPercent())
                    && !ObjectUtils.equals(recipient.getFederalTaxPercent(), getWithdrawalRequest()
                            .getFederalTaxVo().getTaxPercentage())) {

                recipient.setFederalTaxPercent(getWithdrawalRequest().getFederalTaxVo()
                        .getTaxPercentage());
            }
			*/
            // Default it if the section is shown and the value is null.
            if ((withdrawalRequest.getShowTaxWitholdingSection())
                    && (recipient.getFederalTaxPercent() == null)) {
                recipient.setFederalTaxPercent(getWithdrawalRequest().getFederalTaxVo()
                        .getTaxPercentage());
            } // fi
        }
    }

    /**
     * Revalidates the State Tax field to determine if the values are still allowed.
     */
    public void validateStateTaxUserSelection() {

        for (final Recipient recipient : getWithdrawalRequest().getRecipients()) {

            // Note: The state tax VO can be set as null (if the state isn't found (like the
            // 'Outside the US').

            // If for some reason there is no state tax VO set, we do not default the value.
            if (recipient.getStateTaxVo() == null) {
                continue;
            } // fi
            
            //CL 103133 Begin
            String stateOfResidence = withdrawalRequest.getParticipantStateOfResidence();
            BigDecimal mimimumStateTaxPercent = recipient.getStateTaxVo().getTaxPercentageMinimum();
            BigDecimal selectedStateTaxPercent = recipient.getStateTaxPercent();
            //When state of residence is MS and user selected state tax percent is minimum, we do not default the value.
            if(  "MS".equals(stateOfResidence) && mimimumStateTaxPercent.equals(selectedStateTaxPercent)){
            	continue;
            }
          //CL 103133 End
            
            //CL 131784 begin
            if(WithdrawalRequestRecipient.PR_STATE.equals(recipient.getStateOfResidenceCode())
        			&& WithdrawalRequestRecipient.PR_STATE.equals(getWithdrawalRequest().getContractIssuedStateCode())
        				&& recipient.getStateTaxPercent() != null){
        		continue;
        	}
            //CL 131784 end

            final StateTaxType stateTaxType = recipient.getStateTaxVo().getStateTaxType();
            if (StateTaxType.NONE.equals(stateTaxType)) {
                logger
                        .info("validateStateTaxUserSelection> State tax was NONE - defaulting to null.");

                // Reset the state tax field
                recipient.setStateTaxPercent(null);

			} else if ((NumberUtils.isZeroValue(recipient.getFederalTaxPercent()))
					&& (!WithdrawalRequestRecipient.CT_STATE.equals(recipient.getStateOfResidenceCode())))  {

                logger
                        .info("validateStateTaxUserSelection> Federal tax was zero - defaulting to zero.");
                // If federal tax is zero, we force state tax to zero
                recipient.setStateTaxPercent(BigDecimal.ZERO);
            } else if (StateTaxType.OPT_OUT.equals(stateTaxType)) {

                logger
                        .info("validateStateTaxUserSelection> State tax was OPT OUT - check if non-zero and valid.");
                // Redefault if nonzero and no longer valid
                if (NumberUtils.isNotZeroValue(recipient.getStateTaxPercent())
                        && !ObjectUtils.equals(recipient.getStateTaxPercent(), recipient
                                .getStateTaxVo().getDefaultTaxRatePercentage())) {

                    logger
                            .info("validateStateTaxUserSelection> State tax was OPT OUT - current is non-zero or invalid - redefaulting to zero.");
                    recipient.setStateTaxPercent(recipient.getStateTaxVo()
                            .getDefaultTaxRatePercentage());
                } // fi

            } else if (StateTaxType.VOLUNTARY_FIXED.equals(stateTaxType)) {

                logger
                        .info("validateStateTaxUserSelection> State tax was VOLUNTARY_FIXED - check if non-zero and valid.");
                // Redefault if nonzero and no longer valid
                if (NumberUtils.isNotZeroValue(recipient.getStateTaxPercent())
                        && !ObjectUtils.equals(recipient.getStateTaxPercent(), recipient
                                .getStateTaxVo().getDefaultTaxRatePercentage())) {

                    logger
                            .info("validateStateTaxUserSelection> State tax was VOLUNTARY FIXED - current is non-zero or invalid - redefaulting to zero.");
                    recipient.setStateTaxPercent(BigDecimal.ZERO);
                } // fi
            } else if (StateTaxType.MANDATORY.equals(stateTaxType)) {
                logger
                        .info("validateStateTaxUserSelection> State tax was MANDATORY - check if valid.");
                // Mandatory - redefault if no longer valid
                if (!ObjectUtils.equals(recipient.getStateTaxPercent(), recipient.getStateTaxVo()
                        .getDefaultTaxRatePercentage())) {

                    logger
                            .info("validateStateTaxUserSelection> State tax was MANDATORY - is valid - redefaulting to new default.");
                    recipient.setStateTaxPercent(recipient.getStateTaxVo()
                            .getDefaultTaxRatePercentage());
                } // fi
            } else if (StateTaxType.VOLUNTARY_FREE_FORM.equals(stateTaxType)) {
                logger
                        .info("validateStateTaxUserSelection> State tax was VOLUNTARY_FREE_FORM - do nothing.");

                // Do nothing
            }

            // Default it if the section is shown and the value is null.
            if ((((WithdrawalRequestRecipient) recipient).getShowStateTax())
                    && (recipient.getStateTaxPercent() == null)) {
                final StateTaxVO stateTax = recipient.getStateTaxVo();
                logger
                        .info("validateStateTaxUserSelection> State tax is visible and percentage is null - set to new default.");
                recipient
                        .setStateTaxPercent((stateTax.getStateTaxType() == StateTaxType.VOLUNTARY_FIXED) ? BigDecimal.ZERO
                                : stateTax.getDefaultTaxRatePercentage());
            } // fi
        } // end for
    }

    public boolean validateStateTaxUserSelectionForParticipant() {
        boolean isValid = true;

        for (Recipient recipient : getWithdrawalRequest().getRecipients()) {

            StateTaxVO stateTaxVO = recipient.getStateTaxVo();
            if (stateTaxVO == null) {
                continue;
            }

            //CL 119487 Begin
            if ("MS".equals(withdrawalRequest.getParticipantStateOfResidence())) {
            	BigDecimal mimimumStateTaxPercent = recipient.getStateTaxVo().getTaxPercentageMinimum();
            	BigDecimal selectedStateTaxPercent = recipient.getStateTaxPercent();
            	//When state of residence is MS and user selected state tax percent is minimum, we do not default the value.
            	if (mimimumStateTaxPercent.equals(selectedStateTaxPercent)){
            		continue;
            	}
            }
            //CL 119487 End
            
          //CL 131784 begin
            if(WithdrawalRequestRecipient.PR_STATE.equals(recipient.getStateOfResidenceCode())
        			&& WithdrawalRequestRecipient.PR_STATE.equals(getWithdrawalRequest().getContractIssuedStateCode())
        				&& recipient.getStateTaxPercent() != null){
        		continue;
        	}
            //CL 131784 end

            final StateTaxType stateTaxType = recipient.getStateTaxVo().getStateTaxType();
            final BigDecimal stateTaxPercent = recipient.getStateTaxPercent();
            if (((WithdrawalRequestRecipient) recipient).getShowStateTax()) {
                if (StateTaxType.NONE.equals(stateTaxType)) {
                    if (stateTaxPercent != null || NumberUtils.isNotZeroValue(stateTaxPercent)) {
                        isValid = false;
                    }
                } else if (StateTaxType.OPT_OUT.equals(stateTaxType)) {
                    if (NumberUtils.isNotZeroValue(stateTaxPercent)
                            && !ObjectUtils.equals(stateTaxPercent, stateTaxVO
                                    .getDefaultTaxRatePercentage())) {
                        isValid = false;
                    }

                } else if (StateTaxType.VOLUNTARY_FIXED.equals(stateTaxType)) {
                    if (NumberUtils.isNotZeroValue(recipient.getStateTaxPercent())
                            && !ObjectUtils.equals(recipient.getStateTaxPercent(), recipient
                                    .getStateTaxVo().getDefaultTaxRatePercentage())) {
                        isValid = false;
                    }
                } else if (StateTaxType.MANDATORY.equals(stateTaxType)) {

                    if (!ObjectUtils.equals(recipient.getStateTaxPercent(), recipient
                            .getStateTaxVo().getDefaultTaxRatePercentage())) {

                        isValid = false;
                    }
                } else if (StateTaxType.VOLUNTARY_FREE_FORM.equals(stateTaxType)) {
                    NumberRange range = new NumberRange(stateTaxVO.getTaxPercentageMinimum(),
                            stateTaxVO.getTaxPercentageMaximum());
                    if (stateTaxPercent != null && !range.containsNumber(stateTaxPercent)) {
                        isValid = false;
                    }
                }
            }

        } // end for

        return isValid;
    }

    /**
     * Revalidates the Loan Option field to determine if the values are still allowed.
     * 
     * @param lookupData The lookup data used to determine if the value is still valid.
     */
    public void validateHardshipReasonUserSelection(final Map lookupData) {

        final Collection collection = (Collection) lookupData.get(CodeLookupCache.HARDSHIP_REASONS);
        if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE,
                getWithdrawalRequest().getReasonCode())
                || (StringUtils.isNotBlank(getWithdrawalRequest().getHardshipReasons()) && !CollectionUtils
                        .exists(collection, new CodeEqualityPredicate(getWithdrawalRequest()
                                .getHardshipReasons())))) {

            getWithdrawalRequest().setHardshipReasons(StringUtils.EMPTY);
        }
    }

    /**
     * performStep1DefaultSetup is called to execute the default setup steps when step 1 is entered.
     * 
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     */
    public WithdrawalRequest performStep1DefaultSetup() {

        // The taxes are required to be loaded for the method below.
        updateTax();

        getWithdrawalRequest().updateOriginalStep1DriverFields();

        performCommonDefaultSetup();

        // Set up initial messages
        loadInitialMessages(WithdrawalRequest.Mode.INITIATE_STEP_1);

        return withdrawalRequest;
    }

    /**
     * performStep2DefaultSetup is called to execute the default setup steps when step 2 is entered.
     * 
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     */
    public WithdrawalRequest performStep2DefaultSetup() {

        recalculate();

        // Remove any recalculate error messages as we don't want them to display on load
        clearAllButVestingMessages();

        performCommonDefaultSetup();
        updateLegaleseContent();

        // Set up initial messages
        loadInitialMessages(WithdrawalRequest.Mode.INITIATE_STEP_2);

        return withdrawalRequest;
    }

    /**
     * This method removes all but vesting messages from the withdrawal request.
     */
    private void clearAllButVestingMessages() {

        // Luckily all vesting messages are on the WithdrawalRequest object, so we only search
        // there.
        final Collection<WithdrawalMessageType> allVestingMessageTypes = WithdrawalVestingMessageHelper
                .getInstance().getAllWithdrawalMessageTypes();

        // Need a predicate for each type.
        final Predicate[] messageTypePredicates = new Predicate[allVestingMessageTypes.size()];

        int i = 0;
        for (final Iterator<WithdrawalMessageType> iterator = allVestingMessageTypes.iterator(); iterator
                .hasNext(); i++) {
            messageTypePredicates[i] = new WithdrawalMessageTypePredicate(iterator.next());
        } // end for

        // Have to create a new collection, as the fetched one is not modifiable.
        final Collection<WithdrawalMessage> messages = new ArrayList<WithdrawalMessage>(
                withdrawalRequest.getMessages());

        // The predicates are used to filter the list for any matches.
        CollectionUtils.filter(messages, new AnyPredicate(messageTypePredicates));

        // Remove all from the withdrawal request.
        withdrawalRequest.removeMessages();

        for (final WithdrawalMessage withdrawalMessage : messages) {
            withdrawalRequest.addMessage(withdrawalMessage);
        } // end for
    }

    /**
     * performReviewDefaultSetup is called to execute the default setup steps when review is
     * entered.
     * 
     * @return {@link WithdrawalRequest} The {@link WithdrawalRequest} that has been updated.
     */
    public WithdrawalRequest performReviewDefaultSetup() {

        recalculate();

        clearAllButVestingMessages();

        performCommonDefaultSetup();

        getWithdrawalRequest().updateOriginalStep1DriverFields();

        updateLegaleseContent();
        isCensusInformationAvailableforParticipant();
        // Set up initial messages
        loadInitialMessages(WithdrawalRequest.Mode.REVIEW_AND_APPROVE);

        return withdrawalRequest;
    }

    /**
     * Initializes a fee if one doesn't already exist.
     */
    public void updateFees() {
        if (withdrawalRequest.getContractInfo() != null) {
            if (BooleanUtils.isTrue(withdrawalRequest.getContractInfo().getHasATpaFirm())) {
                // Initialize a fee if one doesn't exist.
                if (CollectionUtils.isEmpty(withdrawalRequest.getFees())) {
                    final WithdrawalRequestFee withdrawalRequestFee = new WithdrawalRequestFee();
                    final Collection<Fee> fees = new ArrayList<Fee>(1);
                    fees.add(withdrawalRequestFee);
                    withdrawalRequest.setFees(fees);
                } // fi
            } // fi
        } // fi
    }

    /**
     * Validate for SendForReview.
     */
    public void validateForSendForReview() {

        removeMessages();

        // Validations common to send to review, send to approval and approve
        validateForSendToOrApprovalCommon();

        // Validates common to send to review and send to approval
        // passing 'false' for certian validations based on the 'send For Review' state
        validateForSendForCommon(false);
        validateRolloverType();
        // Check the CSF is still 2 step.
        if (BooleanUtils.isNotTrue(getContractInfo().getTwoStepApprovalRequired())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.TWO_STEP_REQUEST_APPROVAL));
        } // fi
    }

    /**
     * Validates for SendForApproval.
     */
    public void validateForSendForApproval() {

        validateVesting = true;

        removeMessages();

        if (BooleanUtils.isTrue(withdrawalRequest.getIgnoreErrors())) {
            return;
        } // fi

        // Validations common to send to review, send to approval and approve
        validateForSendToOrApprovalCommon();

        // Validates common to send to approver or approval
        validateForSendToApproverOrApproval();
        
        // validating Money Types for bundled contract and having TPA signing authority
        if(!isMandatoryForBundledContract())
        	validateForDenyAndSave = true;
        
        // Validations common to send for review or send for approval
        // passing 'true' for certian validations based on the 'send For Approval' state
        validateForSendForCommon(true);
        validateRolloverType();
        validateForProceedToStep2OrSendForApprovalOrApproval();

    }

    /**
     * Validates for Next or SendForApproval or Approval.
     */
    private void validateForProceedToStep2OrSendForApprovalOrApproval() {

        validateLoanOptionRepay();
        validateWithdrawalReasonOnProceedToStep2OrSendForApprovalOrApproval();
    }

    /**
     * Validates final contribution date if existing.
     */
    protected void validateFinalContributionDate() {

        // Check if final contribution date is suppressed and is non-null
        final Date finalContributionDate = withdrawalRequest.getFinalContributionDate();
        if (withdrawalRequest.getShowFinalContributionDate() && finalContributionDate != null) {

            // Check final contribution date is not before contract effective date
            final Date contractEffectiveDate = withdrawalRequest.getParticipantInfo()
                    .getContractEffectiveDate();
            if ((contractEffectiveDate != null)
                    && (finalContributionDate.before(contractEffectiveDate))) {

                withdrawalRequest
                        .addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR,
                                WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE));
            }

            // Check if final contribution date is more than six months after request date
            final Date requestDate = withdrawalRequest.getRequestDate();
            final Date threshold = DateUtils.addMonths(requestDate,
                    FINAL_CONTRIBUTION_DATE_FUTURE_MONTHS_THRESHOLD);
            if (finalContributionDate.after(threshold)) {

                withdrawalRequest
                        .addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR,
                                WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE));
            }
        }
    }

    /**
     * Validates if final contribution date exists.
     * 
     * @param messageType The message type to use if final contribution date does not exist.
     */
    protected void validateFinalContributionDateExists(final WithdrawalMessageType messageType) {

        // Check if final contribution date is suppressed
        if (withdrawalRequest.getShowFinalContributionDate()) {

            // Check if final contribution date is null
            if (withdrawalRequest.getFinalContributionDate() == null) {
                withdrawalRequest.addMessage(new WithdrawalMessage(messageType,
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE));
            }
        }
    }

    /**
     * Validates for the approval function.
     */
    public void validateForApproval() {

        removeMessages();

        validateForRecalculate();

        // Validations common to send to review, send to approval and approve
        validateForSendToOrApprovalCommon();

        // Do validations common to Send To Approver and Approve
        validateForSendToApproverOrApproval();

        // Do validations specific to Approval
        validateIRAProvider(WithdrawalMessageType.MISSING_IRA_PROVIDER_ERROR);
        validateContractStatus();

        validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        if (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans())) {
            validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);
        }

        // These are validations that aren't common to the 'validateForSendToOrApproval' method
        // above.

        // Now validate the Recipient (and sub-objects).
        for (final Recipient withdrawalRequestRecipient : withdrawalRequest.getRecipients()) {
            for (final Payee withdrawalRequestPayee : withdrawalRequestRecipient.getPayees()) {
                validateIrsCodeForWithdrawalExists((WithdrawalRequestPayee) withdrawalRequestPayee,
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);
            } // end for
        } // end for

        validateParticipantUsCitizen(WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_ERROR);

        validateDeclarations(DECLARATION_ERROR_MESSAGES);

        validateForProceedToStep2OrSendForApprovalOrApproval();

        validateRobustDateChangedAfterVestingCalled();

        // Validate Option for unvested amount
        validateOptionForUnvestedAmount();
        
        validateRolloverType();

    }

    /**
     * Validates if the robust date (termination, retirement or disability) were changed after
     * vesting was called. Only handles if date was changed and the withdrawal request submitted for
     * approval in the same session.
     */
    void validateRobustDateChangedAfterVestingCalled() {

        if (withdrawalRequest.getRobustDateChangedAfterVesting()) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.VESTING_ROBUST_DATE_CHANGED_AFTER_VESTING_CALLED));
        }
    }

    /**
     * Can't approve a withdrawal request if the contract is frozen.
     * 
     */
    private void validateContractStatus() {
        if (StringUtils.equals(withdrawalRequest.getContractInfo().getStatus(),
                ContractInfo.CONTRACT_STATUS_FROZEN)) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.CONTRACT_STATUS_FROZEN));
        }
    }

    /**
     * Validates for the common validation rules for 'save' or 'proceed to step2' (the 'next' button
     * on step 1) or 'return to step1' (the 'back' button on step 2).
     */
    public void validateForSaveCommon() {

        // Mandatory validations for step 1 fields
        validateParticipantStateOfResidence();
        validateBirthDate();
        if (!StringUtils.equals(withdrawalRequest.getCmaSiteCode(),
                WithdrawalRequest.CMA_SITE_CODE_EZK)) {
            validateRetirementDate();
            validateTerminationDate();
        }
        try {
			validateIrsDistributionCodeLoanClosure();
		} catch (SystemException e) {
			 logger.debug("SystemException validateIrsDistributionCodeLoanClosure> Validating Irs Distribution Code Loan Closure");
			  throw ExceptionHandlerUtility.wrap(e);
		}
        validateDisabilityDate();
        validateFinalContributionDate();
        validatePaymentTo();
        validateWithdrawalReasonMandatory();

        if (withdrawalRequest.getSubmissionId() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("validateForSaveCommon> Validating if timestamp has changed.");
            }
            // Only need to validate timestamp if draft or post draft
            validateRequestTimestampHasNotChangedOrExpiryTimePassed();

            // Only need to validate deleted if draft or post draft
            validateRequestHasNotBeenDeleted();
        }
        
        validateLegallyMarriedInd();
    }

    /**
     * Verifies that the request has not expired while request was being edited by checking the
     * timestamp.
     */
    public void validateRequestTimestampHasNotChangedOrExpiryTimePassed() {

        // We need to reload the request and compare timestamps.
        final WithdrawalRequest savedRequest = getSavedWithdrawalRequest();
        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "validateRequestTimestampHasNotChangedOrExpiryTimePassed> Comparing current withdrawal timestamp [")
                            .append(withdrawalRequest.getLastUpdated()).append(
                                    "] to saved withdrawal timestamp [").append(
                                    savedRequest.getLastUpdated()).append("].").toString());
        }

        // Check the SUBMISSION_WITHDRAWAL table timestamp.
        if (!DateUtils.isSameInstant(withdrawalRequest.getLastUpdated(), savedRequest
                .getLastUpdated())) {
            withdrawalRequest.addUniqueMessage(new WithdrawalMessage(
                    WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));
        }

        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "validateRequestTimestampHasNotChangedOrExpiryTimePassed> Comparing current submission timestamp [")
                            .append(withdrawalRequest.getSubmissionCaseLastUpdated()).append(
                                    "] to saved submission timestamp [").append(
                                    savedRequest.getSubmissionCaseLastUpdated()).append("].")
                            .toString());
        } // fi

        // Check the SUBMISSION_CASE table timestamp as well.
        if (!DateUtils.isSameInstant(withdrawalRequest.getSubmissionCaseLastUpdated(), savedRequest
                .getSubmissionCaseLastUpdated())) {
            withdrawalRequest.addUniqueMessage(new WithdrawalMessage(
                    WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));
        } // fi

        // Take the expiration date from the saved data.
        // We check the saved expiration date here, as the request may have just passed over the
        // expiry date, and we want it to be expired if it passed the expiration date with whatever
        // was saved on the server. i.e. Even if they've moved the expiration date later in a window
        // they had open, the request is already expired from the server perspective and it's not
        // allowed to be updated.
        final Date expirationDate = savedRequest.getExpirationDate();
        if (expirationDate != null) {

            final Date now = new Date();
            final Date nowWithoutTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);

            final boolean expirationIsBeforeNow = expirationDate.before(nowWithoutTime);

            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "validateRequestHasExpiredByStateOrTime> Comparing expiration date [")
                        .append(expirationDate).append("] to current date/time [").append(
                                nowWithoutTime).append("]. Expiration is before now[").append(
                                expirationIsBeforeNow).append("].").toString());
            } // fi

            if (expirationIsBeforeNow) {
                withdrawalRequest.addUniqueMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));

            } // fi
        } // fi
    }

    /**
     * Verifies that the request has not been deleted while request was being edited by checking the
     * request state.
     */
    public void validateRequestHasNotBeenDeleted() {

        // We need to reload the request and compare timestamps.
        final WithdrawalRequest savedRequest = getSavedWithdrawalRequest();
        final String status = savedRequest.getStatusCode();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "validateRequestHasNotBeenDeleted> Comparing withdrawal state [")
                    .append(status).append("] to deleted state [").append(
                            WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE).append("].")
                    .toString());
        }
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE, status)) {
            withdrawalRequest.addUniqueMessage(new WithdrawalMessage(
                    WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));
            withdrawalRequest.setIsNoLongerValid(true);
        }
    }

    /**
     * Verifies that the request has not expired while request was being edited by checking the
     * request state.
     */
    public void validateRequestHasExpiredByStateOrTime() {

        // We need to reload the request and compare timestamps.
        final WithdrawalRequest savedRequest = getSavedWithdrawalRequest();
        final String status = savedRequest.getStatusCode();
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "validateRequestHasExpiredByStateOrTime> Examining withdrawal state [").append(
                    status).append("].").toString());
        }
        if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE, status)
                && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE,
                        status)) {

            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE));
        }

        // Take the expiration date from the saved data.
        // We check the saved expiration date here, as the request may have just passed over the
        // expiry date, and we want it to be expired if it passed the expiration date with whatever
        // was saved on the server. i.e. Even if they've moved the expiration date later in a window
        // they had open, the request is already expired from the server perspective and it's not
        // allowed to be updated.
        final Date expirationDate = savedRequest.getExpirationDate();
        if (expirationDate != null) {

            final Date now = new Date();
            final Date nowWithoutTime = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);

            final boolean expirationIsBeforeNow = expirationDate.before(nowWithoutTime);

            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "validateRequestHasExpiredByStateOrTime> Comparing expiration date [")
                        .append(expirationDate).append("] to current date/time [").append(
                                nowWithoutTime).append("]. Expiration is before now[").append(
                                expirationIsBeforeNow).append("].").toString());
            } // fi

            if (expirationIsBeforeNow) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE));

            } // fi
        } // fi
    }

    /**
     * Validates for the common validation rules for 'save' or 'proceed to step2' (the 'next' button
     * on step 1) or 'return to step1' (the 'back' button on step 2).
     */
    public void validateForSaveFromDraft() {

        removeMessages();
        validateForSaveCommon();
        validateForSaveFieldLimits();
    }

    /**
     * Validates for the common validation rules for 'save' or 'proceed to step2' (the 'next' button
     * on step 1) or 'return to step1' (the 'back' button on step 2).
     */
    public void validateForSavePostDraft() {

        // Field limits
        validateForSaveFieldLimits();

        // Check expiration date is present
        if (withdrawalRequest.getExpirationDate() == null) {

            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.EXPIRATION_DATE_INVALID,
                    WithdrawalRequestProperty.EXPIRATION_DATE));
        }
    }

    /**
     * Validates field limits on numerical fields in situations where the fields are not being
     * validated by business rules (save, next, back).
     */
    public void validateForSaveFieldLimits() {

        // Field limit validations for step 2 fields
        validateTpaFieldLimit();
        validateSpecificAmountFieldLimit();
        validateRequestedAmountFieldLimit();
        validateRequestedPercentFieldLimit();
        validateVestingPercentFieldLimit();
        validateStateTaxFieldLimit();
    }

    /**
     * Validate for proceed to step 2 has the logic for validating all fields in draft before
     * allowing the user to move to step 2 (where they may subsequently save the step 1 fields).
     */
    public void validateForProceedToStep2() {

        removeMessages();

        validateForSaveFromDraft();
        validateForProceedToStep2OrSendForApprovalOrApproval();
    }

    /**
     * Validate for proceed to step 2 has the logic for validating all fields in draft before
     * allowing the user to move back to step 1 (where they may subsequently save the step 2
     * fields).
     */
    public void validateForReturnToStep1() {

        removeMessages();

        validateForSaveFieldLimits();

        // Only need to validate deleted if draft or post draft
        if (withdrawalRequest.getSubmissionId() != null) {
            validateRequestHasNotBeenDeleted();
        }
    }

    /**
     * Removes any validations found that are not required for save and deny (which make use of
     * underlying send to review/send to approve validation logic).
     * 
     * @param isDeny True if the function requested was deny, otherwise false (function is save)
     */
    protected void clearNonRequiredValidationsForSaveAndDeny(final boolean isDeny) {

        for (final Recipient myRecipient : withdrawalRequest.getRecipients()) {
            WithdrawalRequestRecipient recipient = (WithdrawalRequestRecipient) myRecipient;
            for (final Payee myPayee : recipient.getPayees()) {
                WithdrawalRequestPayee payee = (WithdrawalRequestPayee) myPayee;

                payee.setWarningCodes(CollectionUtils.selectRejected(payee.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING)));
                payee.setErrorCodes(CollectionUtils.selectRejected(payee.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR)));
                payee
                        .setWarningCodes(CollectionUtils
                                .selectRejected(
                                        payee.getWarningCodes(),
                                        new WithdrawalMessageTypePredicate(
                                                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION)));
                payee.setWarningCodes(CollectionUtils.selectRejected(payee.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER)));
                payee.setWarningCodes(CollectionUtils.selectRejected(payee.getWarningCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL)));

                final Address payeeAddress = (Address) payee.getAddress();
                payeeAddress.setWarningCodes(CollectionUtils.selectRejected(payeeAddress
                        .getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE)));
                payeeAddress.setWarningCodes(CollectionUtils.selectRejected(payeeAddress
                        .getWarningCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE)));
            }

            recipient.setErrorCodes(CollectionUtils.selectRejected(recipient.getErrorCodes(),
                    new WithdrawalMessageTypePredicate(
                            WithdrawalMessageType.PARTICIPANT_NOT_US_CITIZEN)));
            recipient.setWarningCodes(CollectionUtils.selectRejected(recipient.getWarningCodes(),
                    new WithdrawalMessageTypePredicate(
                            WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_WARNING)));
            recipient.setErrorCodes(CollectionUtils.selectRejected(recipient.getErrorCodes(),
                    new WithdrawalMessageTypePredicate(
                            WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_ERROR)));
            
            final Address recipientAddress = (Address) recipient.getAddress();
            recipientAddress.setWarningCodes(CollectionUtils.selectRejected(recipientAddress
                    .getWarningCodes(), new WithdrawalMessageTypePredicate(
                    WithdrawalMessageType.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE)));
        }

        // Added for Participant initiated request.
        if (withdrawalRequest.getIsParticipantCreated()) {
            if (StringUtils.equals(
                    WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE,
                    withdrawalRequest.getReasonCode())) {
                withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                        .getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR)));
            } else if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE,
                    withdrawalRequest.getReasonCode())) {
                withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                        .getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.RETIREMENT_DATE_MISSING_ERROR)));
            }
            withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                    .getErrorCodes(), new WithdrawalMessageTypePredicate(
                    WithdrawalMessageType.VESTED_PERCENTAGE_INVALID)));

            Collection<WithdrawalRequestMoneyType> moneyTypes = withdrawalRequest.getMoneyTypes();
            Collection<WithdrawalRequestMoneyType> vestingPercentInvalidErrorRemoved = new ArrayList<WithdrawalRequestMoneyType>();
            for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : moneyTypes) {
                if (withdrawalRequestMoneyType.doErrorCodesExist()) {
                    withdrawalRequestMoneyType.setErrorCodes(CollectionUtils.selectRejected(
                            withdrawalRequestMoneyType.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.VESTED_PERCENTAGE_INVALID)));
                } // fi
                vestingPercentInvalidErrorRemoved.add(withdrawalRequestMoneyType);
            } // end for
            withdrawalRequest.setMoneyTypes(vestingPercentInvalidErrorRemoved);

        }

        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT)));
        if (isDeny) {
            withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                    .getWarningCodes(), new WithdrawalMessageTypePredicate(
                    WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_WARNING)));
            withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                    .getWarningCodes(), new WithdrawalMessageTypePredicate(
                    WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_WARNING)));
            withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                    .getWarningCodes(), new WithdrawalMessageTypePredicate(
                    WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING)));
            if (withdrawalRequest.getIsParticipantCreated()) {
                withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                        .getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.NOTE_PARTICIPANT_INVALID_FOR_POW)));
            }
        }
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.MISSING_IRA_PROVIDER_WARNING)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.MISSING_IRA_PROVIDER_ERROR)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE_POST_DRAFT)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS)));
       
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.OPTION_FOR_UNVESTED_AMOUNT_INVALID)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_ERROR)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_ERROR)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR)));
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.DECLARATION_AT_RISK_INDICATOR_ERROR)));
        
        // cleared all the error and warning messages(if any) related to Final Contribution Date
        // as later added this perticular warning message based on it's value
        // to show for Deny and save(after draft).
        withdrawalRequest.setErrorCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getErrorCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        withdrawalRequest.setWarningCodes(CollectionUtils.selectRejected(withdrawalRequest
                .getWarningCodes(), new WithdrawalMessageTypePredicate(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));

        
    }

    /**
     * Validate for Deny provides the validation logic that occurs when a request is denied.
     */
    public void validateForDenyPendingReview() {
    	
    	validateForDenyAndSave = true;
        // Pending review validations
        validateForSendForReview();

        // Validate mandatory calculate fields
        validateMandatoryCalculateFieldsForSave();

        // Need to catch any validations not done during send to review
        validateForSavePostDraft();

        // Validate Note to Participant.
        validateNoteToParticipant();

        // Clear validations not relevant to deny
        clearNonRequiredValidationsForSaveAndDeny(FUNCTION_IS_DENY);
        validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
    }

    /**
     * Validate for Deny provides the validation logic that occurs when a request is denied.
     */
    public void validateForDenyPendingApproval() {
    	
    	validateForDenyAndSave = true;
        // Pending approval validations
        validateForSendForApproval();

        // Validate mandatory calculate fields
        validateMandatoryCalculateFieldsForSave();

        // Validate Note to Participant.
        validateNoteToParticipant();
        
        // Clear validations not relevant to deny
        clearNonRequiredValidationsForSaveAndDeny(FUNCTION_IS_DENY);
        validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
    }

    /**
     * Validate for Save provides the validation logic that occurs when a request is saved in
     * pending approval.
     */
    public void validateForSavePendingApproval() {
    	
    	validateForDenyAndSave = true;
        // Validate
        validateForSendForApproval();
        
        //Validate against future dated termination date
        validateFutureDatedTerminationOrRetirementDate();
        // Validate against future Dated Disability Date
        validateFutureDatedDisabilityDate();
        // Field checks on step 2 fields
        validateForSaveFieldLimits();

        // Validate mandatory calculate fields
        validateMandatoryCalculateFieldsForSave();

        // Clear validations not relevant to save
        clearNonRequiredValidationsForSaveAndDeny(FUNCTION_IS_SAVE);
        validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
    }

    /**
     * Validate for Save provides the validation logic that occurs when a request is saved in
     * pending review.
     */
    public void validateForSavePendingReview() {
    	
    	validateForDenyAndSave = true;
        // Validate
        validateForSendForReview();

        //Validate against future dated termination date
        validateFutureDatedTerminationOrRetirementDate();
        // Validate against future Dated Disability Date
        validateFutureDatedDisabilityDate();
        // Validate mandatory calculate fields
        validateMandatoryCalculateFieldsForSave();

        // Validate save post draft
        validateForSavePostDraft();
        
        // validates expiration date when user clicks on save&exit from Withdrawal Review page
        validateExpirationDate();
        
        // Clear validations not relevant to save
        clearNonRequiredValidationsForSaveAndDeny(FUNCTION_IS_SAVE);
        validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
    }

    /**
     * Validate for SendForReview Or SendForApproval provides the validation that is common to the
     * send for review and the send for approval functions.
     */
    private void validateForSendForCommon(boolean isSentForApproval) {
    	if(!validateForDenyAndSave){
        	validateMoneyTypes();
    	}
    	if(isMandatoryForBundledContract() && isSentForApproval){
    		validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);
    		validateIRAProvider(WithdrawalMessageType.MISSING_IRA_PROVIDER_ERROR);
    	}else{
    		validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
    		validateIRAProvider(WithdrawalMessageType.MISSING_IRA_PROVIDER_WARNING);
    	}
        if (CollectionUtils.isNotEmpty(withdrawalRequest.getLoans())) {
        	if(isMandatoryForBundledContract()&& isSentForApproval){
        		validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR);
        	}else{
        		validateIrsCodeForLoanExists(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING);
        	}
        }

        // Now validate the Recipient (and sub-objects).
        for (final Recipient withdrawalRequestRecipient : withdrawalRequest.getRecipients()) {
            for (final Payee withdrawalRequestPayee : withdrawalRequestRecipient.getPayees()) {
            	if(isMandatoryForBundledContract()&& isSentForApproval){
            		validateIrsCodeForWithdrawalExists((WithdrawalRequestPayee) withdrawalRequestPayee,
                            WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR);
            	}else{
            		validateIrsCodeForWithdrawalExists((WithdrawalRequestPayee) withdrawalRequestPayee,
                        WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING);

            	}

            } // end for
            if(isMandatoryForBundledContract() && isSentForApproval){
            	
            	validateParticipantUsCitizen(WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_ERROR);  
            	validateDeclarations(DECLARATION_ERROR_MESSAGES);
        	}else{
        		validateParticipantUsCitizen(WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_WARNING);
        		validateDeclarations(DECLARATION_WARNING_MESSAGES);
        	}
            
        } // end for
        if(isMandatoryForBundledContract()&& isSentForApproval ){
    		// Validate Option for unvested amount
    		validateOptionForUnvestedAmount();
        }      
    }

    /**
     * Validates that the ParticipantUsCitizen field is valid.
     * 
     * @param withdrawalMessageType The message to use if the field is invalid.
     */
    private void validateParticipantUsCitizen(final WithdrawalMessageType withdrawalMessageType) {
        for (final Recipient recipient : withdrawalRequest.getRecipients()) {
            WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient) recipient;
            if (withdrawalRequestRecipient.getShowParticipantUsCitizenField(withdrawalRequest)) {
                // The value hasn't been selected
                if (withdrawalRequestRecipient.getUsCitizenInd() == null) {
                    withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                            withdrawalMessageType, "usCitizenInd"));
                } else if (BooleanUtils.isFalse(withdrawalRequestRecipient.getUsCitizenInd())) {
                    // The value is 'no'.
                    withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.PARTICIPANT_NOT_US_CITIZEN, "usCitizenInd"));
                } // fi
            } // fi
        } // end for
    }

    /**
     * Validates the declarations.
     * 
     * @param messageTypes The message types to use if validation errors are found.
     */
    protected void validateDeclarations(final Map<String, WithdrawalMessageType> messageTypes) {

        // Check for tax notice declaration
        final Collection<Declaration> declarations = withdrawalRequest.getDeclarations();
        if (!CollectionUtils.exists(declarations, new WithdrawalDeclarationPredicate(
                WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE))) {

            withdrawalRequest.addMessage(new WithdrawalMessage(messageTypes
                    .get(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE),
                    WithdrawalRequestProperty.TAX_NOTICE_DECLARATION));
        }

        // Check for waiting period declaration
        if (!CollectionUtils.exists(declarations, new WithdrawalDeclarationPredicate(
                WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE))) {

            withdrawalRequest.addMessage(new WithdrawalMessage(messageTypes
                    .get(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE),
                    WithdrawalRequestProperty.WAITING_PERIOD_WAIVED_DECLARATION));
        }

        // Check IRA provider if visible
        if (withdrawalRequest.isWmsiOrPenchecksSelected()) {

            if (!CollectionUtils.exists(declarations, new WithdrawalDeclarationPredicate(
                    WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE))) {

                withdrawalRequest.addMessage(new WithdrawalMessage(messageTypes
                        .get(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE),
                        WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_DECLARATION));
            }
        }

        if (withdrawalRequest.isAtRiskDeclarationPermittedForUser()) {
            if (messageTypes
                    .containsKey(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE)) {
                if (!CollectionUtils.exists(declarations, new WithdrawalDeclarationPredicate(
                        WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE))) {

                    withdrawalRequest.addMessage(new WithdrawalMessage(messageTypes
                            .get(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE),
                            WithdrawalRequestProperty.AT_RISK_TRANSACTION_DECLARATION));
                }
            }
        }

    }

    /**
     * Determines if the declaration for IRA Service Provider is shown or not.
     * 
     * @return boolean - True if shown, false otherwise.
     */
    private boolean getShowDeclarationForIraServiceProvider() {
        return (StringUtils.equals(withdrawalRequest.getReasonCode(),
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE));
    }

    /**
     * Determines if any step 1 driver fields have changed and adds a warning.
     */
    protected void validateStep1DriverFieldsChanged() {

        if (withdrawalRequest.getHaveStep1DriverFieldsChanged()
                && withdrawalRequest.getIsPostDraft()) {

            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE_POST_DRAFT));
        }
    }

  
    /**
     * Validate the Irs Code for a loan mandatory check.
     * 
     * @param withdrawalMessageType The message to use if the validation fails.
     */
    protected void validateIrsCodeForLoanExists(final WithdrawalMessageType withdrawalMessageType) {

    		// Suppress if loan option is keep
            if (!StringUtils.equals(WithdrawalRequest.LOAN_KEEP_OPTION, withdrawalRequest
                    .getLoanOption())) {

                // Check if blank
                if (StringUtils.isBlank(withdrawalRequest.getIrsDistributionCodeLoanClosure())) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(withdrawalMessageType,
                            WithdrawalRequestProperty.IRS_CODE_FOR_LOAN));
                }
            }
        }
    

    /**
     * Validate the Irs Code for a loan business validations.
     */
    protected void validateIrsCodeForLoan() {

            // Check if not blank
            final String loanIrsDistribution = withdrawalRequest
                    .getIrsDistributionCodeLoanClosure();
            if (StringUtils.isNotBlank(loanIrsDistribution)) {

                // Check for rollover
                if (StringUtils.equals(WithdrawalRequest.LOAN_ROLLOVER_OPTION, withdrawalRequest
                        .getLoanOption())
                        && !StringUtils
                                .equals(
                                        WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DIRECT_ROLLOVER_TO_ANOTHER_PLAN_AND_ROLLOVER_CONTRIBUTION,
                                        loanIrsDistribution)) {

                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER,
                            WithdrawalRequestProperty.IRS_CODE_FOR_LOAN));
                }

                // Check for early or normal distribution
                if (StringUtils.equals(WithdrawalRequest.LOAN_CLOSURE_OPTION, withdrawalRequest
                        .getLoanOption())) {

                    // Both validations are birth date based - check if birth date is not null
                    if (withdrawalRequest.getBirthDate() != null) {

                        // Check if under 59.5 (early distribution validation)
                        if (CalendarUtils.isAgeLessThanFiftyNineAndAHalf(withdrawalRequest
                                .getBirthDate())) {

                            if (!StringUtils
                                    .equals(
                                            WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_YOUNGER_THAN_FIFTY_NINE_AND_A_HALF,
                                            loanIrsDistribution)
                                    && !StringUtils
                                            .equals(
                                                    WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_EARLY_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF_WITH_EXCEPTION,
                                                    loanIrsDistribution)
                                    && !StringUtils
                                            .equals(
                                                    WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_DISABILITY,
                                                    loanIrsDistribution)) {

                                withdrawalRequest
                                        .addMessage(new WithdrawalMessage(
                                                WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION,
                                                WithdrawalRequestProperty.IRS_CODE_FOR_LOAN));
                            }
                        } else {

                            // Normal distribution validation
                            if (!StringUtils
                                    .equals(
                                            WithdrawalRequest.IRS_DISTRIBUTION_LOAN_CODE_NORMAL_DISTRIBUTION_LESS_THAN_FIFTY_NINE_AND_A_HALF,
                                            loanIrsDistribution)) {

                                withdrawalRequest.addMessage(new WithdrawalMessage(
                                        WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL,
                                        WithdrawalRequestProperty.IRS_CODE_FOR_LOAN));

                            }
                       }
                }
            }
        }
    }

    /**
     * Validate the Irs Code for a withdrawal business validations.
     * 
     * @param payee The payee to validate.
     * @param isFirstPayee True if this is payee one, false otherwise.
     */
    protected void validateIrsCodeForWithdrawal(final Payee payee, final boolean isFirstPayee) {

        WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
        // Only validate if IRS visible and value selected for both IRS and payment to
        final String irsCode = withdrawalRequestPayee.getIrsDistCode();
        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (withdrawalRequestPayee.getShowIrsCodeForWithdrawal(withdrawalRequest)
                && StringUtils.isNotBlank(irsCode) && StringUtils.isNotBlank(paymentTo)) {

            // Check if it should be a early distribution or a normal distribution
            final String reason = withdrawalRequest.getReasonCode();
            // Only validate if reason is selected
            if (StringUtils.isNotBlank(reason)) {

                // Check if not disability
                if (!StringUtils
                        .equals(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE, reason)) {

                    // Check if direct to participant or payee 2 of an after tax payment
                    if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                            paymentTo)
                            || (!isFirstPayee && (StringUtils
                                    .equals(
                                            WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                                            paymentTo) || StringUtils
                                    .equals(
                                            WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                                            paymentTo)))) {

                        // Both validations are birth date based - check if birth date is not null
                        if (withdrawalRequest.getBirthDate() != null) {

                            // Check if under 59.5 (early distribution validation)
                            if (CalendarUtils.isAgeLessThanFiftyNineAndAHalf(withdrawalRequest
                                    .getBirthDate())) {

                                // Check if early distribution has been selected
                                if (!StringUtils
                                        .equals(
                                                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5,
                                                irsCode)
                                        && !StringUtils
                                                .equals(
                                                        WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_TEN_YEAR_OPTION,
                                                        irsCode)
                                        && !StringUtils
                                                .equals(
                                                        WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5_WITH_EXCEPTION,
                                                        irsCode)) {
                                    // Add warning
                                    withdrawalRequestPayee
                                            .addMessage(new WithdrawalMessage(
                                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION,
                                                    WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL));
                                } // fi
                            } else {
                                // Over 59.5 (normal distribution validation)
                                // Check if normal distribution has been selected
                                if (!StringUtils
                                        .equals(
                                                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5,
                                                irsCode)
                                        && !StringUtils
                                                .equals(
                                                        WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5_TEN_YEAR_OPTION,
                                                        irsCode)) {
                                    // Add warning
                                    withdrawalRequestPayee
                                            .addMessage(new WithdrawalMessage(
                                                    WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL,
                                                    WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL));
                                }
                            }
                        }
                    }
                }
            }

            // Check if rollover to IRA or plan or is payee 2 of an after tax payment
            if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                    || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE,
                            paymentTo)
                    || (isFirstPayee && (StringUtils
                            .equals(
                                    WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                                    paymentTo) || StringUtils
                            .equals(
                                    WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                                    paymentTo)))) {

                // Check if rollover has been selected
                if (!StringUtils.equals(WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER,
                        irsCode)) {

                    // Add warning
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER,
                            WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL));
                } // fi
            } // fi
        } // fi
    }

    /**
     * Validate the Irs Code for a withdrawal mandatory check.
     * 
     * @param payee The payee to validate.
     * @param withdrawalMessageType The message to use if the validation fails.
     */
    protected void validateIrsCodeForWithdrawalExists(final WithdrawalRequestPayee payee,
            final WithdrawalMessageType withdrawalMessageType) {

        // If the field is visible
    	if ((withdrawalRequest.getReasonCode().equals("RE") || withdrawalRequest.getReasonCode().equals("TE"))) {
            // Verify field is entered
            if (StringUtils.isBlank(payee.getIrsDistCode())) {
                payee.addMessage(new WithdrawalMessage(withdrawalMessageType,
                        WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL));
            }
        } 
    	else 
    	{
    	  if (payee.getShowIrsCodeForWithdrawal(withdrawalRequest)) {
    		  if (StringUtils.isBlank(payee.getIrsDistCode())) {
                  payee.addMessage(new WithdrawalMessage(withdrawalMessageType,
                          WithdrawalRequestProperty.IRS_CODE_FOR_WITHDRAWAL));
              }
    	  }
    	}
    }

    /**
     * Validates that a NoteToParticipant exists.
     */
    private void validateNoteToParticipant() {
        final WithdrawalRequestNote withdrawalRequestNote = withdrawalRequest
                .getCurrentAdminToParticipantNote();
        if (withdrawalRequestNote.isBlank()) {
            withdrawalRequestNote.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.NOTE_TO_PARTICIPANT_INVALID, "noteToParticipant"));
        } // fi
    }

    /**
     * This method has common validations for the send for approval and the approve functions.
     */
    private void validateForSendToApproverOrApproval() {

        validateForSavePostDraft();
        validateExpirationDate();
        validateStep1DriverFieldsChanged();
        //validateUserIsNotParticipant();
    }

    /**
     * If the user is a plansponsor or TPA user and is also the participant, then throw an error
     * message.
     * 
     */
//    private void validateUserIsNotParticipant() {
//
//        final Principal principal = getWithdrawalRequest().getPrincipal();
//        if (principal.getRole().isPlanSponsor()
//                || (principal.getRole().isTPA() && getWithdrawalRequest().getContractInfo()
//                        .getTpaStaffPlanIndicator())) {
//            boolean userMatchesTheParticipant = false;
//            UserInfo userInfo;
//            try {
//                userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(null,
//                        principal.getProfileId());
//            } catch (final SecurityServiceException securityServiceException) {
//                throw new RuntimeException(securityServiceException);
//            } catch (final SystemException systemException) {
//                throw new RuntimeException(systemException);
//            } // end try/catch
//            final String userSSN = userInfo.getSsn();
//            final String ssn = getWithdrawalRequest().getParticipantSSN();
//            if (StringUtils.equals(userSSN, ssn)) {
//                userMatchesTheParticipant = true;
//            } else {
//                ParticipantListVO participants;
//                try {
//                    participants = ContractServiceDelegate.getInstance().getParticipantList(
//                            getWithdrawalRequest().getContractId(), null);
//                } catch (final SystemException systemException) {
//                    throw new RuntimeException(systemException);
//                } // end try/catch
//                final boolean lastNamesEqual = StringUtils.isNotBlank(userInfo.getLastName())
//                        && StringUtils.equalsIgnoreCase(userInfo.getLastName(),
//                                getWithdrawalRequest().getLastName());
//                final String firstInitial1 = userInfo.getFirstName() != null
//                        && userInfo.getFirstName().length() > 0 ? new Character(userInfo
//                        .getFirstName().charAt(0)).toString() : null;
//                final String firstInitial2 = getWithdrawalRequest().getFirstName() != null
//                        && getWithdrawalRequest().getFirstName().length() > 0 ? new Character(
//                        getWithdrawalRequest().getFirstName().charAt(0)).toString() : null;
//                final boolean firstInitialsEqual = StringUtils.isNotBlank(firstInitial1)
//                        && StringUtils.equalsIgnoreCase(firstInitial1, firstInitial2);
//                final boolean firstNamesEqual = StringUtils.isNotBlank(userInfo.getFirstName())
//                        && StringUtils.equalsIgnoreCase(userInfo.getFirstName(),
//                                getWithdrawalRequest().getFirstName());
//
//                if (lastNamesEqual
//                        && !hasParticipantsWith(participants.getParticipants(),
//                                getWithdrawalRequest().getEmployeeProfileId(), userInfo
//                                        .getLastName(), null, null)) {
//                    userMatchesTheParticipant = true;
//
//                } else if (lastNamesEqual
//                        && firstInitialsEqual
//                        && !hasParticipantsWith(participants.getParticipants(),
//                                getWithdrawalRequest().getEmployeeProfileId(), userInfo
//                                        .getLastName(), firstInitial1, null)) {
//                    userMatchesTheParticipant = true;
//
//                } else if (lastNamesEqual
//                        && firstNamesEqual
//                        && !hasParticipantsWith(participants.getParticipants(),
//                                getWithdrawalRequest().getEmployeeProfileId(), userInfo
//                                        .getLastName(), null, userInfo.getFirstName())) {
//                    userMatchesTheParticipant = true;
//                } // fi
//            } // fi
//            if (userMatchesTheParticipant) {
//                getWithdrawalRequest().addMessage(
//                        new WithdrawalMessage(WithdrawalMessageType.USER_MATCHES_THE_PARTICIPANT));
//            } // fi
//        } // fi
//    }

    /**
     * @param participants The list of participants to match against
     * @param participantId The participant id to exclude
     * @param lastName The last name to compare
     * @param firstInitial The first Initial to compare
     * @param firstName The first Name to compare
     * @return true if a match is found
     */
    private boolean hasParticipantsWith(final Collection<ParticipantVO> participants,
            final Integer participantId, final String lastName, final String firstInitial,
            final String firstName) {

        final boolean compareLastName = StringUtils.isNotBlank(lastName);
        final boolean compareFirstInitial = StringUtils.isNotBlank(firstInitial);
        final boolean compareFirstName = StringUtils.isNotBlank(firstName);

        for (final ParticipantVO participant : participants) {
            if (new Integer(participant.getProfileId()).intValue() == participantId.intValue()) {
                continue;
            }
            boolean match = true;
            if (compareLastName) {
                match &= StringUtils.equalsIgnoreCase(participant.getLastName(), lastName);
            }
            if (compareFirstInitial) {
                final String firstInitial2 = participant.getFirstName() != null
                        && participant.getFirstName().length() > 0 ? new Character(participant
                        .getFirstName().charAt(0)).toString() : null;
                match &= StringUtils.equalsIgnoreCase(firstInitial, firstInitial2);

            }
            if (compareFirstName) {
                match &= StringUtils.equalsIgnoreCase(participant.getFirstName(), firstName);
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method has common validations for the send for review, send for approval and the approve
     * functions.
     */
    private void validateForSendToOrApprovalCommon() {

        validateIrsCodeForLoan();
        validateWithdrawalReasonForMinimumDistribution();

        // Now validate the Recipient (and sub-objects).
        for (final Recipient recipient : withdrawalRequest.getRecipients()) {
            validateRecipient(recipient);

            boolean isFirstPayee = true;
            for (final Payee payee : recipient.getPayees()) {
                validateIrsCodeForWithdrawal(payee, isFirstPayee);
                isFirstPayee = false;
            }
        }

        // validate notes for participant for POW
        validateNotesToParticipantForPOW();

        // Check error messages (alert level)
        loadInitialErrorMessages(true);

        // Validate common validations from draft
        validateForSaveCommon();
        // if(withdrawalRequest.getIsParticipantCreated()) {
        // recalculate();
        // }
        
        // check for participant is applicable to LIA
		if (!validateForDenyAndSave) {
			validateLIAAvailableForParticipant();
			// Validate against future dated termination date. This has been called during
			//Send For Review, Send For Approval & Approve
			validateFutureDatedTerminationOrRetirementDate();
			   // Validate against future Dated Disability Date
	        validateFutureDatedDisabilityDate();
		}
    }

    /**
     * Validate the recipient and it's sub-objects..
     * 
     * @param withdrawalRequestRecipient The recipient to validate.
     */
    private void validateRecipient(final Recipient withdrawalRequestRecipient) {

        for (final Payee withdrawalRequestPayee : withdrawalRequestRecipient.getPayees()) {
            validatePayee(withdrawalRequestPayee);
        } // end for

        if (!(StringUtils.equals(withdrawalRequest.getPaymentTo(),
                WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE))) {
            validateRecipientAddresses(withdrawalRequestRecipient);

            // validateParticipantUsCitizen();
        } // fi
    }

    /**
     * Validates the given Payee.
     * 
     * @param payee The payee to validate.
     */
    protected void validatePayee(final Payee payee) {

        WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
        if (withdrawalRequestPayee.getShowAccountNumber(withdrawalRequest)) {
            if (StringUtils.isBlank(withdrawalRequestPayee.getRolloverAccountNo())) {
                withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.ACCOUNT_NUMBER_FOR_ROLLOVER_INVALID,
                        "rolloverAccountNo"));
            } // fi
        } // fi

        if (withdrawalRequestPayee.getShowTrusteeForRollover(withdrawalRequest)) {
            final String planName = withdrawalRequestPayee.getRolloverPlanName();
            if (StringUtils.isBlank(planName)
                    || StringUtils.equals(WithdrawalRequestPayee.DEFAULT_ROLLOVER_PLAN_NAME,
                            planName)) {
                withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.NAME_OF_PLAN_INVALID,
                        WithdrawalRequestProperty.ROLLOVER_PLAN_NAME));
            } // fi
        } // fi

        if (!getWithdrawalRequest().isWmsiOrPenchecksSelected()) {
            validatePaymentMethod(withdrawalRequestPayee);

            if (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                    WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE)) {
                if (StringUtils.isBlank(withdrawalRequestPayee.getPaymentInstruction()
                        .getBankAccountTypeCode()) && !(StringUtils.equals(withdrawalRequest.getPaymentTo(),
                        		WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE))) {
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.ACCOUNT_TYPE_INVALID, "bankAccountTypeCode"));
                } // fi
            } // fi

            if ((StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                    WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE))
                    || (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                            WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE))) {
                // It's EFT / FI
                if (StringUtils.isBlank(withdrawalRequestPayee.getOrganizationName())) {
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.FI_NAME_INVALID, "organizationName"));
                } // fi

                if (StringUtils.isBlank(withdrawalRequestPayee.getPaymentInstruction()
                        .getBankName())) {
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.FI_BANK_NAME_INVALID, "bankName"));
                } // fi
                if (withdrawalRequestPayee.getPaymentInstruction().getBankTransitNumber() == null) {
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.FI_ABA_NUMBER_INVALID, "bankTransitNumber"));
                } else {
                    if (withdrawalRequestPayee.getPaymentInstruction().getBankTransitNumber()
                            .compareTo(GlobalConstants.INTEGER_ZERO) <= 0) {
                        withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.FI_ABA_NUMBER_NOT_GREATER_THAN_ZERO,
                                "bankTransitNumber"));
                    } // fi
                } // fi
                if (StringUtils.isBlank(withdrawalRequestPayee.getPaymentInstruction()
                        .getBankAccountNumber())) {
                    withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.FI_ACCOUNT_NUMBER_INVALID, "bankAccountNumber"));
                } // fi
                
            } // fi

            // Payment method of Cheque.
            if (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                    WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE)) {

                if (isCheckPayeeNameEditable(withdrawalRequestPayee)) {
                    if (StringUtils.isBlank(withdrawalRequestPayee.getOrganizationName())) {
                        withdrawalRequestPayee
                                .addMessage(new WithdrawalMessage(
                                        WithdrawalMessageType.CHECK_PAYEE_NAME_INVALID,
                                        "organizationName"));
                    } // fi
                } // fi

            } // fi

            if (withdrawalRequestPayee.isPayeeAddressEditable(withdrawalRequest)) {
            	validatePayeeAddresses(withdrawalRequestPayee);
            }
            
            // Security Enhancements - removed validation on CreditPartyName, replaced with organization name
            if (isEftPayeeNameEditable(withdrawalRequestPayee)) {
                validateOrganizationName(withdrawalRequestPayee);
            }
        } // fi
    }

    /**
     * Determines whether the check payee name should be editable or not. Currently the check payee
     * name is editable if:
     * <ul>
     * <li>Payment To is Rollover to IRA.
     * <li>Payment To is Rollover to plan.
     * <li>Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li>Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @param withdrawalRequestPayee The payee to use.
     * @return boolean - True if the check payee name is editable.
     */
    public boolean isCheckPayeeNameEditable(final WithdrawalRequestPayee withdrawalRequestPayee) {

        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return false;
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Determine if we are the first payee or the second payee
            return isFirstPayee(withdrawalRequestPayee);

        } else {
            return false;
        }
    }

    /**
     * Queries if the payee is the first payee in the recipient collection.
     * 
     * @param withdrawalRequestPayee The payee to check.
     * @return boolean - True if this is the first payee, false otherwise.
     */
    public boolean isFirstPayee(final WithdrawalRequestPayee withdrawalRequestPayee) {
        return (withdrawalRequestPayee == getFirstRecipient().getPayees().iterator().next());
    }

    /**
     * Queries if the payee is the second payee in the recipient collection.
     * 
     * @param withdrawalRequestPayee The payee to check.
     * @return boolean - True if this is the second payee, false otherwise.
     */
    public boolean isSecondPayee(final Payee withdrawalRequestPayee) {

        final Collection<Payee> collection = withdrawalRequest.getRecipients().iterator().next()
                .getPayees();
        if (collection.size() > 1) {
            final Iterator<Payee> iterator = collection.iterator();
            iterator.next();
            return (withdrawalRequestPayee == iterator.next());
        } // fi

        return false;
    }

    /**
     * Validates the Payment Method.
     * 
     * @param withdrawalRequestPayee The payee to validate.
     */
    private void validatePaymentMethod(final WithdrawalRequestPayee withdrawalRequestPayee) {
        if (!(withdrawalRequest.isWmsiOrPenchecksSelected())) {
            if (StringUtils.isBlank(withdrawalRequestPayee.getPaymentMethodCode())) {
                withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.PAYMENT_METHOD_INVALID, "paymentMethodCode"));
            } // fi
        } // fi
    }

    /**
     * Get the first {@link WithdrawalRequestRecipient}.
     * 
     * @return {@link WithdrawalRequestRecipient} - The first recipient.
     */
    private Recipient getFirstRecipient() {
        if (withdrawalRequest.getRecipients().size() == 0) {
            return null;
        } // fi
        return withdrawalRequest.getRecipients().iterator().next();
    }

    /**
     * doSave actually saves the withdrawal request to the database.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    protected void doSave() throws DistributionServiceException {
        final int profileId = (int) withdrawalRequest.getPrincipal().getProfileId();

    	// Special State Change for Bundled GA.  Only BGA contracts can be moved from Pending Approval
    	// back to Pending Review by the BGA Rep.  We are using this to detect this case.
        boolean stateChangeFromApprovalToReview = false;
    	if (previousWithdrawalState != null && withdrawalState != null) {
        	stateChangeFromApprovalToReview = 
    			     (WithdrawalStateEnum.PENDING_APPROVAL.getStateClass().equals(previousWithdrawalState.getClass())) 
			      && (WithdrawalStateEnum.PENDING_REVIEW.getStateClass().equals(withdrawalState.getClass()));
    	}
    	
    	//save the RolloverType
    	 saveRolloverType();
    	
    	if (!stateChangeFromApprovalToReview) {
	    	// Determine if we need an insert or update.
	
	        prepareForSave();
		
	        if (withdrawalRequest.getSubmissionId() == null) {
	            // Insert
	            Integer submissionId = null;
	            submissionId = new WithdrawalDao().insert(withdrawalRequest.getContractId(), profileId,
	                    withdrawalRequest, getLastSavedTimestamp());
	
	            if (submissionId != null) {
	                withdrawalRequest.setSubmissionId(submissionId);
	            } // fi
	        } else {
	            new WithdrawalDao().update(withdrawalRequest.getContractId(), withdrawalRequest
	                    .getSubmissionId(), profileId, withdrawalRequest);
	        } // fi
    	} else {
    		// For BGA we just want to change status, and record the history, nothing else.
    		boolean statusUpdateOnly = true;
    		new WithdrawalDao().update(withdrawalRequest.getContractId(), withdrawalRequest
                    .getSubmissionId(), profileId, withdrawalRequest, statusUpdateOnly);    		
    	}
    }

    /**
     * Applies business rules on withdrawal request before saving.
     */
    private void prepareForSave() {

        // Clear the value of the most recent prior contribution date on save, if the field isn't
        // shown.
        if (!(withdrawalRequest.getShowFinalContributionDate())) {
            withdrawalRequest.setMostRecentPriorContributionDate(null);
        } // fi

        // We put a check here to ensure that the recipient has the US Citizen
        // indicator nulled out
        // if the indicator isn't shown.
        for (final Recipient recipient : withdrawalRequest.getRecipients()) {
            WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient) recipient;
            // If suppressed, null the value.
            if (!(withdrawalRequestRecipient.getShowParticipantUsCitizenField(withdrawalRequest))) {

                if (withdrawalRequestRecipient.getUsCitizenInd() != null) {
                    logger.error("\n\nWhy was the US Citizen indicator set?\n\n");
                } // fi

                withdrawalRequestRecipient.setUsCitizenInd(null);
            } // fi

            for (final Payee payee : withdrawalRequestRecipient.getPayees()) {

                if (withdrawalRequest.isWmsiOrPenchecksSelected()) {

                    // WMSI / PenChecks suppresses most payee information
                    final PaymentInstruction paymentInstruction = payee.getPaymentInstruction();
                    paymentInstruction.setBankAccountTypeCode(null);
                    payee.setOrganizationName(null);
                    payee.setPaymentMethodCode(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE);
                    final DistributionAddress address = payee.getAddress();
                    address.setAddressLine1(null);
                    address.setAddressLine2(null);
                    address.setCity(null);
                    address.setStateCode(null);
                    address.setZipCode(null);
                    address.setCountryCode(null);
                    paymentInstruction.setBankName(null);
                    paymentInstruction.setBankTransitNumber(null);
                    paymentInstruction.setBankAccountNumber(null);
                    paymentInstruction.setCreditPartyName(null);
                    payee.setMailCheckToAddress(null);

                } else {

                    if (StringUtils.equals(payee.getPaymentMethodCode(),
                            WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE)) {
                        final PaymentInstruction paymentInstruction = payee.getPaymentInstruction();
                        paymentInstruction.setBankName(null);
                        paymentInstruction.setCreditPartyName(null);
                        paymentInstruction.setBankTransitNumber(null);
                        paymentInstruction.setBankAccountNumber(null);
                        if (!withdrawalRequest.getContractInfo().getMailChequeToAddressIndicator()) {
                            payee.setMailCheckToAddress(null);
                        }

                    } else {
                        payee.setMailCheckToAddress(null);
                      
                    } // fi

                    // Check for bank account type (only used for ACH)
                    if (!StringUtils.equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, payee
                            .getPaymentMethodCode())) {
                        payee.getPaymentInstruction().setBankAccountTypeCode(null);
                    }
                }
            } // fi

        } // end for

        // Cleanup the tax values if any were set, so that they're not saved if the sections were
        // suppressed.
        if (!(withdrawalRequest.getShowTaxWitholdingSection())) {
            // Not showing the tax section, so we clear the values.
            for (final Recipient withdrawalRequestRecipient : withdrawalRequest.getRecipients()) {
                withdrawalRequestRecipient.setFederalTaxPercent(null);
                withdrawalRequestRecipient.setStateTaxPercent(null);
                withdrawalRequestRecipient.setStateTaxTypeCode(null);
            } // end for
        } else {

            if (withdrawalRequest.getFederalTaxVo() == null) {
                // Lookup tax VOs.
                this.updateTax();
            } // fi

            for (final Recipient withdrawalRequestRecipient : withdrawalRequest.getRecipients()) {

                // Not showing the state tax, so we clear the values.
                if (!(((WithdrawalRequestRecipient) withdrawalRequestRecipient).getShowStateTax() || isRestartReview())) {
                    withdrawalRequestRecipient.setStateTaxPercent(null);
                    withdrawalRequestRecipient.setStateTaxTypeCode(null);
                } else {
                    // Always update to the current state value.
                    // Lookup tax VOs.
                    this.updateTax();

                    withdrawalRequestRecipient.setStateTaxTypeCode(withdrawalRequestRecipient
                            .getStateTaxVo().getTaxTypeCode());
                }
            } // end for
        } // fi

        // Ensure that we remove the old notes from the state if they're saving from the draft state
        // or sending to a new state from draft.
        if ((WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE.equals(withdrawalState
                .getWithdrawalStateEnum().getStatusCode()))
                || ((previousWithdrawalState != null) && ((previousWithdrawalState
                        .getWithdrawalStateEnum().equals(WithdrawalStateEnum.DRAFT)) || (withdrawalState
                        .getWithdrawalStateEnum().equals(WithdrawalStateEnum.DRAFT))))) {
            withdrawalRequest.setRemoveAllNotesOnSave(true);
        }

        // Ensure we don't remove notes that should be kept.
        if ((previousWithdrawalState != null)
                && (previousWithdrawalState.getWithdrawalStateEnum()
                        .equals(WithdrawalStateEnum.DRAFT))
                && ((withdrawalState.getWithdrawalStateEnum().equals(WithdrawalStateEnum.APPROVED)) || (withdrawalState
                        .getWithdrawalStateEnum().equals(WithdrawalStateEnum.READY_FOR_ENTRY)))) {
            withdrawalRequest.setRemoveAllNotesOnSave(false);
        } // fi

        cleanUpMoneyTypesForSave();
        updateTPAFeeChangeIndicator();

        // withdrawalAmount
        if (!StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
            withdrawalRequest.setWithdrawalAmount(null);
        }

        // blank out irs distribution code if it is supressed
        if ( WithdrawalRequest.LOAN_KEEP_OPTION.equals(withdrawalRequest.getLoanOption())) {
            withdrawalRequest.setIrsDistributionCodeLoanClosure(null);
        }

        // Clears the option for unvested amount if it's not shown on the page.
        if (!(withdrawalRequest.getShowOptionForUnvestedAmount())) {
            withdrawalRequest.setUnvestedAmountOptionCode(null);
        } // fi

        // Create the timestamp to be used for all activity history operations
        setLastSavedTimestamp();
    }

    /**
     * isReadyForDoSave returns true if there are no errors or warnings, or only warnings, and
     * warnings are flagged to be ignored.
     * 
     * @return boolean - True if ready for save, false otherwise.
     */
    protected boolean isReadyForDoSave() {
        return withdrawalRequest.isValidToProcess();
    }

    private ActivityHistory readActivityHistory() throws DistributionServiceException {
        return withdrawalState.readActivityHistory(this);
    }

    /**
     * Gets the Expected Processing Date from now.
     * 
     * @return Date The current date, if the NYSE is open, otherwise the next NYSE open date.
     */
    protected Date getExpectedProcessingDateFromNow() {
        Date currentBusinessDate = null;

        try {
            currentBusinessDate = AccountServiceDelegate.getInstance().getNextNYSEClosureDate(null);
        } catch (final AccountException accountException) {
            throw new RuntimeException(accountException);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        } // end try/catch

        return currentBusinessDate;
    }

    /**
     * Retrieves the CMA content by ID.
     * 
     * @param bodyContentId The content ID to lookup.
     * @return String - The CMA Content found with the given ID.
     * 
     */
    protected String retrieveContent(final Integer bodyContentId) {

        final BrowseServiceHelper bsh = BrowseServiceHelper.getInstance();
        final ContentType miscellaneous = ContentTypeManager.instance().MISCELLANEOUS;
        final String manulifeCompanyId = getWithdrawalRequest().getParticipantInfo()
                .getManulifeCompanyId();
        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        try {
            final Content content = bsh.getContentById(bodyContentId, miscellaneous, location);
            final Miscellaneous mContent = (Miscellaneous) content;
            return mContent.getText();

        } catch (final SystemException e) {
            throw ExceptionHandlerUtility.wrap(new SystemException(
                    "System exception CMA content for id ::  " + bodyContentId + " " + e));
        } // end try/catch
    }

    /**
     * This method is called to update the Legalese Info in the withdrawal request when the request
     * is approved.
     * 
     * It merges the static content and the dynamic content based on the Withdrawal reason selected
     * by the user.
     */
    private void updateLegaleseContent() {
        LegaleseInfo legaleseInfo = null;
        String staticLegaleseText = null;
        String legaleseText = null;

        // Boolean spousalConsent = getWithdrawalRequest().getSpousalConsentRequired();
        final Boolean spousalConsent = getWithdrawalRequest().getContractInfo()
                .getSpousalConsentRequired();
        final Integer contentId = getLegaleseContentId(spousalConsent);
        if (contentId != null && contentId == LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT_KEY) {
            legaleseText = retrieveContent(contentId);
            legaleseInfo = new LegaleseInfo(contentId, legaleseText, (int) getWithdrawalRequest()
                    .getPrincipal().getProfileId());
        } else {
            staticLegaleseText = retrieveContent(new Integer(LEGALESE_STATIC_CONTENT_TEXT_KEY));
            legaleseText = retrieveContent(contentId);
            legaleseInfo = new LegaleseInfo(contentId, legaleseText + " " + staticLegaleseText,
                    (int) getWithdrawalRequest().getPrincipal().getProfileId());
        }
        /*
         * final String manulifeCompanyId = getWithdrawalRequest().getParticipantInfo()
         * .getManulifeCompanyId(); final Location location = BrowseServiceHelper
         * .convertManulifeCompanyIdToLocation(manulifeCompanyId);
         * legaleseInfo.setSiteId(BrowseServiceHelper.getLocationCodeFromLocation(location));
         */
        legaleseInfo.setCmaSiteCode(getWithdrawalRequest().getCmaSiteCode());
        getWithdrawalRequest().setLegaleseInfo(legaleseInfo);
    }

    /**
     * This method returns the the content Id to be used based on the withdrawal reason and the
     * spousal consent.
     * 
     * @param spousalConsent The spousal consent value.
     * @return Integer - The content ID to use based on the given parameters.
     */
    protected Integer getLegaleseContentId(final Boolean spousalConsent) {
        Integer contentId = null;
        if (WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE
                .equals(getWithdrawalRequest().getReasonCode())) {
            contentId = new Integer(LEGALESE_DYNAMIC_MANDATORY_TERMINATION_TEXT_KEY);
        } else {
            if (BooleanUtils.isTrue(spousalConsent)) {
                contentId = new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_REQUIRED_TEXT_KEY);
            } else if (BooleanUtils.isFalse(spousalConsent)) {
                contentId = new Integer(LEGALESE_DYNAMIC_NO_SPOUSAL_CONSENT_TEXT_KEY);
            } else if (spousalConsent == null) {
                contentId = new Integer(LEGALESE_DYNAMIC_SPOUSAL_CONSENT_BLANK_TEXT_KEY);
            }

        }
        return contentId;
    }

    /**
     * Performs validations when Next button is clicked on the step One page.
     * 
     */
    public void proceedToStep2() {
        validateForProceedToStep2();
    }

    /**
     * Performs validations when Back button is clicked on the step Two page.
     * 
     */
    public void returnToStep1() {
        validateForReturnToStep1();
    }
    
    /**
     * Performs termination date validation when Back button is clicked on the step Two page.
     * 
     */
    public void returnToStep1WithTerminationOrRetirementDate() {
        validateFutureDatedTerminationOrRetirementDate();
        // Validate against future Dated Disability Date
        validateFutureDatedDisabilityDate();
    }

    /**
     * Validates the Participant's State Of Residence.
     */
    private void validateParticipantStateOfResidence() {
        if (StringUtils.isBlank(withdrawalRequest.getParticipantStateOfResidence())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.STATE_OF_RESIDENCE_INVALID, "stateOfResidence"));
        } // fi
    }

    /**
     * Validates the Loan Option if it exists.
     */
    private void validateLoanOptionRepay() {
        // Creates an error message if the Loan Option selected is RP = Repay
        if (StringUtils.equals(LOAN_OPTION_REPAY, withdrawalRequest.getLoanOption())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.LOAN_OPTION_REPAY_SELECTED_ERROR,
                    WithdrawalRequestProperty.LOAN_OPTION));
        } // fi
    }

    /**
     * Determines if the age is less than the normal retirement age, based off the date of birth and
     * the retirement date provided. The normal retirement age is looked up from the plan data and
     * defaulted to 65 if that value is not set.
     * 
     * @param dateOfBirth The date of birth to check with.
     * @param retirementDate The retirement date to check with.
     * @return boolean - True if the age is less than the normal retirement age, false otherwise.
     */
    protected boolean isAgeLessThanNormalRetirementAge(final Date dateOfBirth,
            final Date retirementDate) {

        final BigDecimal retirementAge = withdrawalRequest.getContractInfo()
                .getNormalRetirementAge();

        return isAgeLessThanRetirementAge(dateOfBirth, retirementDate, retirementAge);

    }

    /**
     * Determines if the age is less than the early retirement age, based off the date of birth and
     * the retirement date provided. The early retirement age is looked up from the plan data and
     * defaulted to 65 if that value is not set.
     * 
     * @param dateOfBirth The date of birth to check with.
     * @param retirementDate The retirement date to check with.
     * @return boolean - True if the age is less than the early retirement age, false otherwise.
     */
    protected boolean isAgeLessThanEarlyRetirementAge(final Date dateOfBirth,
            final Date retirementDate) {

        final BigDecimal retirementAge = withdrawalRequest.getContractInfo()
                .getEarlyAgeRetirement();

        return isAgeLessThanRetirementAge(dateOfBirth, retirementDate, retirementAge);

    }

    private boolean isAgeLessThanRetirementAge(final Date dateOfBirth, final Date retirementDate,
            final BigDecimal retirementAge) {

        int yearsToAdd = NORMAL_RETIREMENT_AGE_YEARS;
        int monthsToAdd = NORMAL_RETIREMENT_AGE_MONTHS;

        if (retirementAge != null) {
            final BigDecimal years = retirementAge.setScale(0, RoundingMode.DOWN);
            yearsToAdd = years.intValue();

            if (retirementAge.subtract(years).compareTo(new BigDecimal("0.5")) == 0) {
                monthsToAdd = MONTHS_IN_HALF_A_YEAR;
            } else {
                monthsToAdd = 0;
            } // fi
        } // fi

        return CalendarUtils.isAdjustedDateAfterDate(dateOfBirth, yearsToAdd, monthsToAdd,
                retirementDate);
    }

    /**
     * Retrieve a withdrawal request for editing.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void readWithdrawalRequestForEdit() throws DistributionServiceException {
        readWithdrawalRequestForEdit(withdrawalRequest.getSubmissionId(), withdrawalRequest
                .getPrincipal());
    }

    /**
     * Retrieve a withdrawal request for editing.
     * 
     * @param submissionId the id of the submission to read
     * @param principal the user principal
     * @throws DistributionServiceException thrown if there is an exception
     */

    public void readWithdrawalRequestForEdit(final Integer submissionId, final Principal principal)
            throws DistributionServiceException {

        WithdrawalRequest withdrawalRequestResult = null;
        WithdrawalRequest withdrawalRequestFromDatabase = null;
        try {
            withdrawalRequestFromDatabase = new WithdrawalDao().read(submissionId);
            getRolloverTypeValue(withdrawalRequestFromDatabase.getRecipients());
            withdrawalRequestResult = getMergedWithdrawalRequestForEdit(
                    withdrawalRequestFromDatabase, getWithdrawalRequestDefaultData(
                            withdrawalRequestFromDatabase.getEmployeeProfileId(),
                            withdrawalRequestFromDatabase.getContractId(), principal));
            
            calculateAtRiskActivities(submissionId, withdrawalRequestResult);
            withdrawalRequestResult.setPrincipal(principal);
            processNotes(withdrawalRequestResult);
            // get participant legalese info if this is participant initiated withdrawal
            if (StringUtils.equals(WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE,
                    withdrawalRequestResult.getUserRoleCode())) {

                final String participantLegaleseText = new WithdrawalDao().getAgreedLegaleseText(
                        submissionId, WithdrawalRequest.CMA_SITE_CODE_EZK);
                if (withdrawalRequestFromDatabase.getParticipantLegaleseInfo() == null) {
                    final LegaleseInfo participantLegaleseInfo = new LegaleseInfo(
                            participantLegaleseText);
                    withdrawalRequestResult.setParticipantLegaleseInfo(participantLegaleseInfo);
                } else {
                    LegaleseInfo participantLegaleseInfo = withdrawalRequestFromDatabase
                            .getParticipantLegaleseInfo();
                    if (StringUtils.isBlank(participantLegaleseInfo.getLegaleseText())) {
                        participantLegaleseInfo.setLegaleseText(participantLegaleseText);
                    }
                    withdrawalRequestResult.setParticipantLegaleseInfo(participantLegaleseInfo);
                }
            }

        } catch (final SystemException se) {
            throw ExceptionHandlerUtility.wrap(se);
        }
        setWithdrawalRequest(withdrawalRequestResult);
        final ActivityHistory activityHistory = readActivityHistory();
        if (activityHistory != null) {
            activityHistory.setActivities(updateActivity(activityHistory.getActivities()));
            withdrawalRequestResult.setActivityHistory(activityHistory);
        }
    }

    private Collection<Activity> updateActivity(final Collection<Activity> activities) {

        final Collection<Activity> updatedActivities = new ArrayList<Activity>();
        for (final Activity element : activities) {
            if (!isActivityInvalid(element)) {
                updatedActivities.add(element);
            }
        }
        return updatedActivities;
    }

    private boolean isActivityInvalid(final Activity activity) {
        return (WithdrawalRequestPayee.DEFAULT_ROLLOVER_PLAN_NAME.equals(activity
                .getOriginalValue()) && StringUtils.isBlank(activity.getCurrentValue()));
    }

    /**
     * getMergedWithdrawalRequest takes the given withdrawal request and loads it with the default
     * data. The loading logic is state dependent (so it's delegated to the state classes).
     * 
     * @param withdrawalRequestToMergeInto The withdrawal request to merge the default data into.
     * @param defaultData The withdrawal request containing the default data
     * @return {@link WithdrawalRequest} The merged withdrawal request.
     * @throws DistributionServiceException If an error occurs.
     * @throws SystemException thrown if there is a system error
     */
    private WithdrawalRequest getMergedWithdrawalRequestForEdit(
            final WithdrawalRequest withdrawalRequestToMergeInto,
            final WithdrawalRequest defaultData) throws DistributionServiceException,
            SystemException {

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequestToMergeInto);
        withdrawal.applyDefaultDataForEdit(defaultData);

        return withdrawal.getWithdrawalRequest();

    }

    /**
     * This method returns default WithdrawalRequest data gathered from the CustomerService
     * database, EmploymentService and other sources.
     * 
     * The WithdrawalRequest VO is sparsely populated with the default data and should be used as
     * input to a "merge" algorithm used to initialize/reset a WithdrawalRequest VO according to the
     * default data.
     * 
     * @param profileId The employee profile ID
     * @param contractId The employee contract ID
     * @param principal The user principal
     * @return A WithdrawalRequest VO sparsely populated with default data.
     */
    private static WithdrawalRequest getWithdrawalRequestDefaultData(final Integer profileId,
            final Integer contractId, final Principal principal) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getWithdrawalRequestDefaultData(" + profileId + ", "
                    + contractId + ")");
        }

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        // FIXME: Should update BaseWithdrawal to Long
        withdrawalRequest.setEmployeeProfileId(profileId.intValue());
        withdrawalRequest.setContractId(contractId);

        final Calendar calendar = Calendar.getInstance();
        withdrawalRequest.setRequestDate(calendar.getTime());

        final int expiresAfter = ConfigurationFactory.getConfiguration().getInt(
                "withdrawal.expiry.defaultExpiryInterval");

        calendar.add(Calendar.DAY_OF_YEAR, expiresAfter);

        withdrawalRequest.setExpirationDate(calendar.getTime());

        withdrawalRequest.setParticipantLeavingPlanInd(true);

        // Initialize the WithdrawalRequest VO with the participant info
        final Employee employee;
        try {
            employee = EmployeeServiceDelegate.getInstance(new BaseEnvironment().getAppId())
                    .getEmployeeByProfileId(new Long(profileId), contractId, null);
        } catch (final SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(systemException);
        } // end try/catch

        //
        // Planet/Census Participant Information Section
        //
        // If employee data found the set the hasPCInfo flag to true
        withdrawalRequest.setHasPCData(employee != null && employee.getEmployeeDetailVO() != null);

        ParticipantInfo participantInfo = null;
        if (withdrawalRequest.getHasPCData()) {

            if (employee.getParticipantContract() != null) {
                withdrawalRequest.setParticipantId(employee.getParticipantContract()
                        .getParticipantId().intValue());
                // Set non-persistent withdrawal info data
                participantInfo = WithdrawalDataManager.getParticipantInfo(withdrawalRequest
                        .getEmployeeProfileId(), withdrawalRequest.getParticipantId(), contractId);
                withdrawalRequest.setParticipantInfo(participantInfo);
            }

            final EmployeeDetailVO employeeDetailVO = employee.getEmployeeDetailVO();
            if (employeeDetailVO != null) {
                final String employmentStatusCode = employeeDetailVO.getEmploymentStatusCode();
                final Date employmentStatusEffDate = employeeDetailVO.getEmploymentStatusEffDate();

                withdrawalRequest.setBirthDate(employeeDetailVO.getBirthDate());
                withdrawalRequest.getParticipantInfo().setSystemOfRecordDateOfBirth(
                        employeeDetailVO.getBirthDate());
                withdrawalRequest.setParticipantStateOfResidence(employeeDetailVO
                        .getResidenceStateCode());
                withdrawalRequest.setFirstName(composeFirstName(employeeDetailVO.getFirstName(),
                        employeeDetailVO.getMiddleInitial()));
                withdrawalRequest.setLastName(StringUtils.trim(employeeDetailVO.getLastName()));
                withdrawalRequest.setParticipantSSN(employeeDetailVO.getSocialSecurityNumber());
                withdrawalRequest.setEmplStatusCode(employeeDetailVO.getEmploymentStatusCode());
                withdrawalRequest.setEmplStatusEffectiveDate(employeeDetailVO
                        .getEmploymentStatusEffDate());

                final Collection<DeCodeVO> withdrawalReasons;
                try {
                    withdrawalReasons = WithdrawalInfoDao.getParticipantWithdrawalReasons(
                            participantInfo.getContractStatusCode(), participantInfo
                                    .getParticipantStatusCode());
                } catch (final SystemException systemException) {
                    throw ExceptionHandlerUtility.wrap(systemException);
                } // end try/catch

                if (StringUtils.isNotEmpty(employmentStatusCode)) {
                    if (employmentStatusCode.equals(EmploymentStatus.Retired)
                            && CollectionUtils.exists(withdrawalReasons, new CodeEqualityPredicate(
                                    employmentStatusCode))) {
                        withdrawalRequest.setReasonCode(WithdrawalReason.RETIREMENT);
                    } else if (employmentStatusCode.equals(EmploymentStatus.Terminated)
                            && CollectionUtils.exists(withdrawalReasons, new CodeEqualityPredicate(
                                    employmentStatusCode))) {
                        withdrawalRequest.setReasonCode(WithdrawalReason.TERMINATION);
                    } else if (employmentStatusCode
                            .equals(EmploymentStatus.TotalPermanentDisability)
                            && CollectionUtils.exists(withdrawalReasons, new CodeEqualityPredicate(
                                    employmentStatusCode))) {
                        withdrawalRequest.setReasonCode(WithdrawalReason.DISABILITY);
                    }
                    if (employmentStatusEffDate != null) {
                        if (employmentStatusCode.equals(EmploymentStatus.Terminated)) {
                            withdrawalRequest.setTerminationDate(employmentStatusEffDate);
                        } else if (employmentStatusCode.equals(EmploymentStatus.Retired)) {
                            withdrawalRequest.setRetirementDate(employmentStatusEffDate);
                        } else if (employmentStatusCode
                                .equals(EmploymentStatus.TotalPermanentDisability)) {
                            withdrawalRequest.setDisabilityDate(employmentStatusEffDate);
                        }
                    }
                }
            }

        }
        // Set participant's address
        final AddressVO employeeAddressVO = employee.getAddressVO();
        // Set the participant's address if found
        if (employeeAddressVO != null && !employeeAddressVO.isAddressNotFound()) {
            final Address address = new Address();
            address.setAddressLine1(employeeAddressVO.getAddressLine1());
            address.setAddressLine2(employeeAddressVO.getAddressLine2());
            address.setCity(StringUtils.trim(employeeAddressVO.getCity()));

            if (StringUtils.isBlank(employeeAddressVO.getCountry())) {
                address.setCountryCode(StringUtils.EMPTY);
            } else {
                // convert country name to country code
                final DeCodeVO country;
                try {
                    country = EnvironmentServiceDelegate.getInstance(
                            new BaseEnvironment().getAppId()).resolveCountryNameAndCode(
                            StringUtils.trim(employeeAddressVO.getCountry()));
                } catch (final SystemException systemException) {
                    throw ExceptionHandlerUtility.wrap(systemException);
                } // end try/catch
                // check if no match found
                if (country == null) {
                    address.setCountryCode(StringUtils.EMPTY);
                    address.setNonMatchedCountryName(employeeAddressVO.getCountry());
                } else {
                    address.setCountryCode(country.getCode());
                }
            }

            address.setStateCode(employeeAddressVO.getStateCode());
            address.setZipCode(employeeAddressVO.getZipCode());
            withdrawalRequest.setParticipantAddress(address);
        }
        // FIXME: Get the trustee address from Plan Service

        // Set withdrawal info default data
        // FIXME: This should be part of the "merge" algorithm
        if (participantInfo != null) {
            withdrawalRequest.setMostRecentPriorContributionDate(participantInfo
                    .getLastInvestmentDate());
            withdrawalRequest.setContractName(participantInfo.getContractName());

            // Loans Section
            withdrawalRequest.setLoans(participantInfo.getOutstandingLoans());

            // Available Money Type balances
            withdrawalRequest.setMoneyTypes(participantInfo.getMoneyTypes());

            // Get the contract information
            withdrawalRequest.setContractInfo(WithdrawalDataManager.loadContractInfo(contractId,
                    principal));

            // Set the value.
            withdrawalRequest.getContractInfo().setHasATpaFirm(
                    participantInfo.getThirdPartyAdminId());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getWithdrawalRequestDefaultData(" + profileId + ", " + contractId
                    + ")");
        }
        
		
        return withdrawalRequest;
    }

    /**
     * takes the withdrawalrequest.notes collection and populates 1. current admin to participant 2.
     * current admint to admin 3. read only admin to participant 4. read only admin to admin
     * 
     * @param returnReq The withdrawal Request containing the notes
     */
    private void processNotes(final WithdrawalRequest returnReq) {

        if (returnReq.getStatusCode() == null) {
            throw new RuntimeException("Can't process notes since there is no status!");
        }

        final Collection<WithdrawalRequestNote> readOnlyAdminToAdmin = new ArrayList<WithdrawalRequestNote>();
        final Collection<WithdrawalRequestNote> readOnlyAdminToParticipant = new ArrayList<WithdrawalRequestNote>();

        final boolean isDraft = returnReq.getStatusCode().equals(
                WithdrawalStateEnum.DRAFT.getStatusCode());

        for (final Note note : returnReq.getNotes()) {
            if (note.getNoteTypeCode() == null) {
                throw new RuntimeException("Note type code is null.  cannot proceed");
            }
            if (note.getNoteTypeCode().equals(WithdrawalRequestNote.ADMIN_TO_ADMIN_TYPE_CODE)) {
                readOnlyAdminToAdmin.add((WithdrawalRequestNote) note);
                if (isDraft) {
                    returnReq.setCurrentAdminToAdminNote((WithdrawalRequestNote) note);
                }
            } else if (note.getNoteTypeCode().equals(
                    WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE)) {
                readOnlyAdminToParticipant.add((WithdrawalRequestNote) note);
                if (isDraft) {
                    returnReq.setCurrentAdminToParticipantNote((WithdrawalRequestNote) note);
                }
            }
        }
        // returnReq.setReadOnlyAdminToParticipantNotes(readOnlyAdminToAdmin);
        returnReq.setReadOnlyAdminToAdminNotes((readOnlyAdminToAdmin));
        returnReq.setReadOnlyAdminToParticipantNotes(readOnlyAdminToParticipant);
    }

    private static String composeFirstName(final String firstName, final String middleInitial) {

        final String[] names = { StringUtils.trim(firstName), StringUtils.trim(middleInitial) };

        return StringUtils.trim(StringUtils.join(names, " "));
    }

    /**
     * Read a withdrawal request for viewing.
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void readWithdrawalRequestForView() throws DistributionServiceException {
        readWithdrawalRequestForView(withdrawalRequest.getSubmissionId(), withdrawalRequest
        // .getPrincipal(), withdrawalRequest.getCmaSiteCode());
                .getPrincipal());
    }

    /**
     * Read a withdrawal request for viewing.
     * 
     * @param submissionId the id of the submission to read
     * @param principal the user principal
     * @param cmaSiteCode whether it's from psw/tpa or ezk
     * @throws DistributionServiceException thrown if there is an exception
     */
    private void readWithdrawalRequestForView(final Integer submissionId, final Principal principal)
            throws DistributionServiceException {

        WithdrawalRequest withdrawalRequestResult = null;
        try {
            final WithdrawalDao withdrawalDao = new WithdrawalDao();
            WithdrawalRequest withdrawalRequestFromDatabase = withdrawalDao.read(submissionId);
            getRolloverTypeValue(withdrawalRequestFromDatabase.getRecipients());
            withdrawalRequestResult = getMergedWithdrawalRequestForView(
                    withdrawalRequestFromDatabase, getWithdrawalRequestDefaultData(
                            withdrawalRequestFromDatabase.getEmployeeProfileId(),
                            withdrawalRequestFromDatabase.getContractId(), principal));
            
            PlanData planData=ContractServiceDelegate.getInstance().readPlanData(withdrawalRequestFromDatabase.getContractId());
            withdrawalRequestResult.setContractIssuedStateCode(planData.getContractIssuedStateCode());
            
            calculateAtRiskActivities(submissionId, withdrawalRequestResult);
            withdrawalRequestResult.setPrincipal(principal);
            /*
             * final Location location = BrowseServiceHelper
             * .convertManulifeCompanyIdToLocation(withdrawalRequestResult
             * .getParticipantInfo().getManulifeCompanyId()); final String siteId =
             * BrowseServiceHelper.getLocationCodeFromLocation(location);
             */
            final String legaleseText = withdrawalDao.getAgreedLegaleseText(submissionId,
                    WithdrawalRequest.CMA_SITE_CODE_PSW);

            if (withdrawalRequestFromDatabase.getLegaleseInfo() == null) {
                final LegaleseInfo legaleseInfo = new LegaleseInfo(legaleseText);
                withdrawalRequestResult.setLegaleseInfo(legaleseInfo);
            } else {
                final LegaleseInfo legalese = withdrawalRequestFromDatabase.getLegaleseInfo();
                if (StringUtils.isBlank(legalese.getLegaleseText())) {
                    legalese.setLegaleseText(legaleseText);
                }
                withdrawalRequestResult.setLegaleseInfo(legalese);

            }
            // get participant legalese info if this is participant initiated withdrawal
            if (StringUtils.equals(WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE,
                    withdrawalRequestResult.getUserRoleCode())) {

                final String participantLegaleseText = withdrawalDao.getAgreedLegaleseText(
                        submissionId, WithdrawalRequest.CMA_SITE_CODE_EZK);
                if (withdrawalRequestFromDatabase.getParticipantLegaleseInfo() == null) {
                    final LegaleseInfo participantLegaleseInfo = new LegaleseInfo(
                            participantLegaleseText);
                    withdrawalRequestResult.setParticipantLegaleseInfo(participantLegaleseInfo);
                } else {
                    LegaleseInfo participantLegaleseInfo = withdrawalRequestFromDatabase
                            .getParticipantLegaleseInfo();
                    if (StringUtils.isBlank(participantLegaleseInfo.getLegaleseText())) {
                        participantLegaleseInfo.setLegaleseText(participantLegaleseText);
                    }
                    withdrawalRequestResult.setParticipantLegaleseInfo(participantLegaleseInfo);
                }
            }

        } catch (final SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(systemException);
        } // end try/catch

        processNotes(withdrawalRequestResult);

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequestResult);
        final ActivityHistory activityHistory = withdrawal.readActivityHistory();
        withdrawalRequestResult.setActivityHistory(activityHistory);
        activityHistory.setActivities(updateActivity(activityHistory.getActivities()));

        // bypass calculation if participant initated and vesting
        // has not been called
        if (withdrawalRequestResult.getIsParticipantCreated()
                && !withdrawalRequestResult.getVestingCalledInd()) {
        } else {
            withdrawal.recalculate();
        }
        // added for the back button (if no fees are saved, back button from
        // confirmation to step2 fails unless we populate some dummy fees)
        withdrawal.updateFees();
        withdrawalRequestResult = withdrawal.getWithdrawalRequest();
        setWithdrawalRequest(withdrawalRequestResult);

        loadConfiguration();
    }

    /**
     * getMergedWithdrawalRequest takes the given withdrawal request and loads it with the default
     * data. The loading logic is state dependent (so it's delegated to the state classes).
     * 
     * @param withdrawalRequestToMergeInto The withdrawal request to merge the default data into.
     * @param defaultData The withdrawal request containing the default data
     * @return {@link WithdrawalRequest} The merged withdrawal request.
     * @throws DistributionServiceException If an error occurs.
     * @throws SystemException thrown if there is a system error
     */
    private WithdrawalRequest getMergedWithdrawalRequestForView(
            final WithdrawalRequest withdrawalRequestToMergeInto,
            final WithdrawalRequest defaultData) throws DistributionServiceException,
            SystemException {

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequestToMergeInto);
        withdrawal.applyDefaultDataForView(defaultData);

        return withdrawal.getWithdrawalRequest();

    }

    /**
     * This method cleans up the money types before saving it to the database by allowing only one
     * of the money type amount value or the money type percentage to be saved. If the Amount type
     * is 'maximum available' or 'by percentage', the percntage is stored and the amount value is
     * set to null. If the Amount type is 'specific amount', the withdrawal amount is stored and the
     * percentage is set to null.
     */
    private void cleanUpMoneyTypesForSave() {

        for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {

            // Get the type of withdrawal that they're doing, then reset the fields that aren't
            // used as values (% if by $, or $ if by %).
            final String amountTypeCode = withdrawalRequest.getAmountTypeCode();

            if (StringUtils.equals(amountTypeCode,
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE)) {
                withdrawalRequestMoneyType.setWithdrawalAmount(null);
            } else if (StringUtils.equals(amountTypeCode,
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {
                withdrawalRequestMoneyType.setWithdrawalAmount(null);
            } else if (StringUtils.equals(amountTypeCode,
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                withdrawalRequestMoneyType.setWithdrawalPercentage(null);
            } // fi

        } // end for
    }

    /**
     * This method checks to see if any change has been made to the TPA fees. If the change is made
     * by the TPA, it updates the value object with the profile id of the TPA. If the change is made
     * by Plan Sponsor, the indicator is set to true.
     */
    private void updateTPAFeeChangeIndicator() {
        WithdrawalRequestFee oldFee = null;
        WithdrawalRequestFee newFee = null;
        try {
            if (withdrawalRequest.getSubmissionId() != null) {
                oldFee = new WithdrawalDao().getFees(withdrawalRequest.getSubmissionId());
            }

        } catch (final SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
        // This logic will change when the application can take more than one
        // TPA fees
        final Collection<Fee> newFeesColl = withdrawalRequest.getFees();
        if (1 == newFeesColl.size()) {
            newFee = (WithdrawalRequestFee) CollectionUtils.get(newFeesColl, 0);
        }

        if (isNewFeeDifferentFromOldFee(newFee, oldFee)) {
            if (StringUtils.equals(withdrawalRequest.getUserRoleCode(),WithdrawalRequest.USER_ROLE_TPA_CODE) ||
            	StringUtils.equals(withdrawalRequest.getUserRoleCode(),WithdrawalRequest.USER_ROLE_BUNDLED_GA_CODE)) {
				// Bundled GA Project - Modified to set last fee changed by user id to BGA CAR profile ID
				// for BGA contracts, where JH is the TPA.
                withdrawalRequest.setLastFeeChangeByTPAUserID(new BigDecimal(withdrawalRequest
                        .getPrincipal().getProfileId()));
                withdrawalRequest.setLastFeeChangeWasPSUserInd(false);
            } else if (StringUtils.equals(withdrawalRequest.getUserRoleCode(),
                    WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE)) {
                withdrawalRequest.setLastFeeChangeWasPSUserInd(true);
            }
        }
    }

    /**
     * This method compares the old and the new fees. Returns true, if the fees have changed.
     * 
     * @param newFee The new fee.
     * @param oldFee The old fee.
     * @return boolean - true if fees not equal, else false.
     */
    private boolean isNewFeeDifferentFromOldFee(final WithdrawalRequestFee newFee,
            final WithdrawalRequestFee oldFee) {
        // If the old value was never entered.
        if ((oldFee == null) || (oldFee.isBlank())) {
            // True if the request has a new fee value, false if there is no new value.
            return ((newFee != null) && (!(newFee.isBlank())));
        } // fi

        // Check if the Type code has changed
        // and set the flag
        if (!ObjectUtils.equals(oldFee.getTypeCode(), newFee.getTypeCode())) {
            return true;
        }

        // check if the value has changed.
        if (!(ObjectUtils.equals(oldFee.getValue(), newFee.getValue()))) {
            return true;
        }

        // Otherwise the value hasn't changed.
        return false;
    }

    /**
     * HOUR_OF_DAY_FOUR_PM.
     */
    private static final int HOUR_OF_DAY_FOUR_PM = 16;

    private static final String CLASS_NAME = Withdrawal.class.getName();

    /**
     * Returns today if today if a valid business day and the current hour is < 16 ( i.e., before
     * 4pm ) else returns the next business day.
     * 
     * @return the 'current business date' as defined by rules above
     * @throws SystemException If an exception occurs.
     */
    private Date getCurrentBusinessDate() throws SystemException {
        Date currentBusinessDate = null;
        final Calendar today = Calendar.getInstance();
        try {
            if (isThisABusinessDay(today.getTime())
                    && today.get(Calendar.HOUR_OF_DAY) < HOUR_OF_DAY_FOUR_PM) {
                currentBusinessDate = today.getTime();
            } else {
                currentBusinessDate = AccountServiceDelegate.getInstance().getNextNYSEClosureDate(
                        null);
            }
        } catch (final Exception exception) {
            throw new SystemException(exception, CLASS_NAME, "getCurrentBusinessDate",
                    "failed to get closure dates");
        }
        return currentBusinessDate;
    }

    /**
     * TODO isThisABusinessDay Description.
     * 
     * @param date The date to check.
     * @return Returns true if today is a business day
     * @throws Exception If an exception occurs.
     */
    private boolean isThisABusinessDay(final Date date) throws Exception {
        final Date[] datesToCheck = { date };
        final Date[] closureDates = AccountServiceDelegate.getInstance()
                .getFilteredNYSEClosureDates(null, datesToCheck);
        return closureDates.length != 0;
    }

    /**
     * TODO getNumberOfCompletedWithdrawalTransaction Description.
     * 
     * @param contractId The current contract ID.
     * @param employeeProfileId The employee profile ID.
     * @return the number of completed withdrawal transactions.
     */
    public static int getNumberOfCompletedWithdrawalTransaction(final Integer contractId,
            final Integer employeeProfileId) {

        if (!(isApolloAvailable())) {
            return -1;
        } // fi

        try {
            final int result = new ApolloDao().getNumberOfCompletedWithdrawalTransaction(
                    contractId, employeeProfileId);
            return result;
        } catch (DAOException daoException) {
            // apollo might be down, so we return a special value.
            // To show that we were unable to get the data, we return -1.
            return -1;
        } // end try/catch
    }

    /**
     * TODO getNumberOfPendingWithdrawalTransaction Description.
     * 
     * @param contractId The current contract ID.
     * @param employeeProfileId The employee profile ID.
     * @return the number of completed withdrawal transactions.
     */
    public static int getNumberOfPendingWithdrawalTransaction(final Integer contractId,
            final Integer employeeProfileId) {

        if (!(isApolloAvailable())) {
            return -1;
        } // fi

        try {
            final int result = new ApolloDao().getNumberOfPendingWithdrawalTransaction(contractId,
                    employeeProfileId);
            return result;
        } catch (final DAOException daoException) {
            // apollo might be down, so we return a special value.
            // To show that we were unable to get the data, we return -1.
            return -1;
        } // end try/catch
    }

    /**
     * This method attempts to check the system availability of Apollo.
     * 
     * @return boolean - True if Apollo is available, false otherwise.
     */
    private static boolean isApolloAvailable() {

        try {
            final boolean result = AccountServiceDelegate.getInstance().isAPOLLOAvailable(null);
            return result;
        } catch (final Exception exception) {
            throw new RuntimeException(new SystemException(exception, CLASS_NAME,
                    "isApolloAvailable", "Error determining system availability"));
        } // end try/catch
    }

    /**
     * TODO getWithdrawalRequests Description.
     * 
     * @param profileId The profile ID to use.
     * @param contractId The contract ID to use.
     * @return Collection - The withdrawal requests found.
     */
    public static Collection<WithdrawalRequest> getWithdrawalRequests(final Integer profileId,
            final Integer contractId) {
        try {
            final Collection<WithdrawalRequest> withdrawalRequests = new WithdrawalDao()
                    .getWithdrawalRequests(profileId, contractId);
            return withdrawalRequests;
        } catch (final SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } // end try/catch
    }

    /**
     * This method is used to create a new {@link WithdrawalRequest}.
     * 
     * @param profileId The profile ID.
     * @param contractId The contract ID.
     * @param principal The principal of the user performing this action.
     * @return WithdrawalRequest - A new withdrawal request.
     */
    public static WithdrawalRequest initiateNewWithdrawalRequest(final Integer profileId,
            final Integer contractId, final Principal principal) {

        WithdrawalRequest withdrawalRequest = null;

        withdrawalRequest = getWithdrawalRequestDefaultData(profileId, contractId, principal);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());

        return withdrawalRequest;
    }

    /**
     * This method returns the latest saved value of the current withdrawal request, or null if the
     * request has not yet been persisted (saved).
     * 
     * @return WithdrawalRequest - the savedWithdrawalRequest.
     */
    protected WithdrawalRequest getSavedWithdrawalRequest() {

        if ((savedWithdrawalRequest == null) && (getWithdrawalRequest().getHasBeenPersisted())) {
            try {
                savedWithdrawalRequest = new WithdrawalDao().read(getWithdrawalRequest()
                        .getSubmissionId());
                calculateAtRiskActivities(getWithdrawalRequest().getSubmissionId(), savedWithdrawalRequest);
            } catch (final DistributionServiceException DistributionServiceException) {
                throw new RuntimeException(DistributionServiceException);
            } // end try/catch
        } // fi

        return savedWithdrawalRequest;
    }

    /**
     * This method returns a withdrawal domain object containing the latest saved value of the
     * current withdrawal request, or null if the request has not yet been persisted (saved).
     * 
     * @return Withdrawal - the savedWithdrawal.
     */
    public Withdrawal getSavedWithdrawal() {

        final WithdrawalRequest request = getSavedWithdrawalRequest();
        if (request == null) {
            return null;
        }

        return new Withdrawal(request);
    }

    /**
     * @param savedWithdrawalRequest the savedWithdrawalRequest to set
     */
    protected void setSavedWithdrawalRequest(final WithdrawalRequest savedWithdrawalRequest) {
        this.savedWithdrawalRequest = savedWithdrawalRequest;
    }

    /**
     * This method returns the cached value of the participant info. The information is lazy loaded
     * if this is the first time the information has been requested.
     * 
     * @return ParticipantInfo - the participantInfo.
     */
    protected ParticipantInfo getParticipantInfo() {

        if (participantInfo == null) {
            participantInfo = WithdrawalDataManager.getParticipantInfo(withdrawalRequest
                    .getEmployeeProfileId(), withdrawalRequest.getParticipantId(),
                    withdrawalRequest.getContractId());
        }

        return participantInfo;
    }

    /**
     * This method returns the cached value of the contract info. The information is lazy loaded if
     * this is the first time the information has been requested.
     * 
     * @return ContractInfo - the contractInfo.
     */
    protected ContractInfo getContractInfo() {

        if (contractInfo == null) {
            contractInfo = WithdrawalDataManager.loadContractInfo(
                    withdrawalRequest.getContractId(), withdrawalRequest.getPrincipal());
        }

        return contractInfo;
    }

    /**
     * Loads the initial messages for the specified mode.
     * 
     * @param mode The mode to general initial messages for (step1, step2, review).
     */
    public void loadInitialMessages(final WithdrawalRequest.Mode mode) {

        switch (mode) {
            case INITIATE_STEP_1:
                if (!withdrawalRequest.getHaveInitiateStep1InitialMessagesLoaded()) {
                	loadCommonInitialMessages();
                    loadInitialMessagesForInitiateStep1();
                }
                break;
            case INITIATE_STEP_2:
                if (!withdrawalRequest.getHaveInitiateStep2InitialMessagesLoaded()) {
                    loadInitialMessagesForInitiateStep2();
                }
                break;
            case REVIEW_AND_APPROVE:
                if (!withdrawalRequest.getHaveReviewAndApproveInitialMessagesLoaded()) {
                    loadCommonInitialMessages();
                    loadInitialMessagesForReviewAndApprove();
                }
                break;
            default:
                throw new IllegalArgumentException(new StringBuffer("Unknown mode [").append(mode)
                        .append("] passed.").toString());
        }
    }

    /**
     * Examines initial messages that are considered error level and will prevent the user from
     * proceeding (PBA, Roth, Total Status, Pending requests, Ready for Entry requests).
     * 
     * @param isError True if the initial message should be handled as an error.
     */
    protected void loadInitialErrorMessages(final boolean isError) {

        // Check endstate
        final String statusCode = withdrawalRequest.getStatusCode();
        final boolean isEndState = StringUtils.equals(WithdrawalStateEnum.DELETED.getStatusCode(),
                statusCode)
                || StringUtils.equals(WithdrawalStateEnum.DELETED.getStatusCode(), statusCode)
                || StringUtils.equals(WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode(),
                        statusCode)
                || StringUtils.equals(WithdrawalStateEnum.EXPIRED.getStatusCode(), statusCode);
        final boolean participantHasRothMoney = withdrawalRequest.getParticipantInfo()
                .getParticipantHasRothMoney();
        final boolean participantHasPBAMoney = withdrawalRequest.getParticipantInfo()
                .getParticipantHasPbaMoney();
        final boolean participantHasTotalStatus = withdrawalRequest.getParticipantInfo()
                .isParticipantStatusTotal();

        // Currently user can always delete or deny
        final boolean userCanDeleteOrDeny = true;

        if (!isEndState && userCanDeleteOrDeny) {

            boolean participantHasAnotherRequest = false;
            // check for pending_review, pending_approval, Approved
            boolean participantHasReadyForEntryRequest = false; // check for ready for entry with
            // expected processing date >= current business date
            final Collection<WithdrawalRequest> existingRequests = Withdrawal
                    .getWithdrawalRequests(withdrawalRequest.getEmployeeProfileId(),
                            withdrawalRequest.getContractId());

            // Check for any existing requests (other than itself) in pending review, pending
            // approval or approved state
            for (final WithdrawalRequest request : existingRequests) {
                final String existingRequestStatusCode = request.getStatusCode();
                if (logger.isDebugEnabled()) {
                    logger.debug(new StringBuffer(
                            "loadInitialErrorMessages> Examining request with status [").append(
                            existingRequestStatusCode).append(
                            "] and comparing existing submission ID [").append(
                            request.getSubmissionId()).append("] against current submission ID [")
                            .append(withdrawalRequest.getSubmissionId()).append("].").toString());
                }
                if ((StringUtils.equals(existingRequestStatusCode,
                        WithdrawalStateEnum.PENDING_REVIEW.getStatusCode())
                        || StringUtils.equals(existingRequestStatusCode,
                                WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode()) || StringUtils
                        .equals(existingRequestStatusCode, WithdrawalStateEnum.APPROVED
                                .getStatusCode()))
                        && !ObjectUtils.equals(request.getSubmissionId(), withdrawalRequest
                                .getSubmissionId())) {
                    participantHasAnotherRequest = true;
                    withdrawalRequest.getParticipantInfo().setHasExistingWithdrawalRequest(true);
                    break;
                }
            }

            Date currentBusinessDate;
            try {
                currentBusinessDate = getCurrentBusinessDate();
            } catch (final SystemException systemException) {
                throw ExceptionHandlerUtility.wrap(systemException);
            }

            currentBusinessDate = DateUtils.truncate(currentBusinessDate, Calendar.DATE);
            for (final WithdrawalRequest request : existingRequests) {
                if (request.getStatusCode().equals(
                        WithdrawalStateEnum.READY_FOR_ENTRY.getStatusCode())) {
                    if (request.getExpectedProcessingDate() == null) {
                        throw new IllegalStateException("did not find a valid processing date");
                    }
                    Date expectedProcessingDate = new Date(request.getExpectedProcessingDate()
                            .getTime());
                    expectedProcessingDate = DateUtils.truncate(expectedProcessingDate,
                            Calendar.DATE);
                    if (expectedProcessingDate.getTime() >= currentBusinessDate.getTime()) {
                        participantHasReadyForEntryRequest = true;
                        withdrawalRequest.getParticipantInfo()
                                .setHasExistingWithdrawalRequest(true);
                        break;
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "loadInitialErrorMessages> Checking whether request can be processed with ROTH [")
                                .append(participantHasRothMoney).append("], PBA [").append(
                                        participantHasPBAMoney).append("], total status [").append(
                                        participantHasTotalStatus).append("], another request[")
                                .append(participantHasAnotherRequest).append(
                                        "], another pending request [").append(
                                        participantHasReadyForEntryRequest).append("].").toString());
            }
            // Check if any conditions met
            if (participantHasAnotherRequest 
            		|| participantHasReadyForEntryRequest || participantHasPBAMoney) {
                // Error/alert can be added in multiple areas but we only want one copy
                if (isError) {

                    // Only add error if not already present
                    if (!CollectionUtils.exists(withdrawalRequest.getErrorCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ERROR))) {
                        withdrawalRequest.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ERROR));
                    }
                } else {

                    // Only add alert if not already present
                    if (!CollectionUtils.exists(withdrawalRequest.getAlertCodes(),
                            new WithdrawalMessageTypePredicate(
                                    WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT))) {
                        withdrawalRequest.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
                    }
                }
            }
            //ME-CL 116299-Converted Error Message as Alert message if the participant is terminated.
            if(participantHasTotalStatus){
            	 // Only add alert if not already present
                if (!CollectionUtils.exists(withdrawalRequest.getAlertCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT))) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT));
                }
            	
            }
        }
    }

    /**
     * Loads the initial messages common to initiates step 1 and review and approve as entry points
     * to the withdrawal request flow.
     */
    protected void loadCommonInitialMessages() {

        // Check for completed and pending withdrawal transactions
        if ((getNumberOfCompletedWithdrawalTransaction(withdrawalRequest.getContractId(),
                withdrawalRequest.getParticipantId()) > 0)
                || (getNumberOfPendingWithdrawalTransaction(withdrawalRequest.getContractId(),
                        withdrawalRequest.getParticipantId()) > 0)) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.PARTICIPANT_HAS_ANOTHER_WITHDRAWAL));
        }

        final WithdrawalInfo withdrawalInfo = WithdrawalDataManager.getWithdrawalInfo(
                withdrawalRequest.getParticipantId(), withdrawalRequest.getContractId());
        if (withdrawalInfo.getLoanRequestId() != 0) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.PARTICIPANT_HAS_I_LOANS));
        }

        LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance().getLoanDataHelper();
        LoanSettings loanSettings = loanDataHelper.getLoanSettings(withdrawalRequest
                .getContractId());
        if (loanSettings != null && loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
            LoanDao loanDao = new LoanDao();
            List<Integer> pendingRequests;

            try {
                pendingRequests = loanDao.getPendingRequestSubmissionIds(withdrawalRequest
                        .getContractId(), withdrawalRequest.getEmployeeProfileId());
                if (pendingRequests != null && pendingRequests.size() > 0) {
                    withdrawalRequest.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.PARTICIPANT_HAS_ONLINE_LOANS));
                }

            } catch (LoanDaoException daoException) {
                logger.error("Exception while trying to fetch pending loan requests "
                        + daoException.toString());
                throw new RuntimeException(daoException);

            }

        }

        if (withdrawalInfo != null
                && (withdrawalInfo.getTotalBalance() == null || withdrawalInfo.getTotalBalance() == BigDecimal.ZERO)) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.PARTICIPANT_HAS_ZERO_ACCOUNT_BALANCE));
        }

        // Check error messages (alert level)
        loadInitialErrorMessages(false);
    }

    /**
     * Loads the initial messages for initiates step 1.
     */
    protected void loadInitialMessagesForInitiateStep1() {

        if (logger.isDebugEnabled()) {
            logger.debug("loadInitialMessagesForInitiateStep1> Entry.");
        }

        // Mark initial messages have been loaded
        withdrawalRequest.setInitialMessagesLoaded(WithdrawalRequest.Mode.INITIATE_STEP_1);
    }

    /**
     * Loads the initial messages for initiates step 2.
     */
    protected void loadInitialMessagesForInitiateStep2() {

        if (logger.isDebugEnabled()) {
            logger.debug("loadInitialMessagesForInitiateStep2> Entry.");
        }

        if (!StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, withdrawalRequest
                .getPaymentTo())
                && (withdrawalRequest.getParticipantAddress().getNonMatchedCountryName() != null)
                && StringUtils.isNotBlank(withdrawalRequest.getParticipantAddress()
                        .getNonMatchedCountryName())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.INVALID_PARTICIPANT_ADDRESS_COUNTRY));
            withdrawalRequest.getParticipantAddress().setNonMatchedCountryName(null);
        }
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, withdrawalRequest
                .getPaymentTo())
                && (withdrawalRequest.getTrusteeAddress().getNonMatchedCountryName() != null)
                && StringUtils.isNotBlank(withdrawalRequest.getTrusteeAddress()
                        .getNonMatchedCountryName())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.INVALID_TRUSTEE_ADDRESS_COUNTRY));
            withdrawalRequest.getTrusteeAddress().setNonMatchedCountryName(null);
        }

        // Mark initial messages have been loaded
        withdrawalRequest.setInitialMessagesLoaded(WithdrawalRequest.Mode.INITIATE_STEP_2);
    }

    /**
     * Loads the initial messages for review and approve.
     */
    protected void loadInitialMessagesForReviewAndApprove() {

        if (logger.isDebugEnabled()) {
            logger.debug("loadInitialMessagesForReviewAndApprove> Entry.");
        }

        // Check if current expiration date is within warning threshold
        final Date warningThresholdDate = DateUtils.addDays(withdrawalRequest.getExpirationDate(),
                EXPIRATION_DATE_WARNING_THRESHOLD);
        final Date now = DateUtils.truncate(new Date(), Calendar.DATE);
        // Trigger if we are on same day or after (!before)
        if (!now.before(warningThresholdDate)) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.EXPIRATION_DATE_WITHIN_WARNING_THRESHOLD));
        }
        if (getWithdrawalRequest().getShowTaxWitholdingSection()) {
            validateForStateTaxForParticipantCreated();
        }

        if (withdrawalRequest.getIsParticipantCreated() && !isVestingServiceCalled()&& !withdrawalRequest.getReasonCode().equals("PD")) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.VESTING_SERVICE_HAS_ALREADY_CALLED_INDICATOR_NO));
        }

        // Mark initial messages have been loaded
        withdrawalRequest.setInitialMessagesLoaded(WithdrawalRequest.Mode.REVIEW_AND_APPROVE);
    }

    /**
     * Adds the message for missing IRA Provider as a warning or an error message based on the
     * Message Category.
     * 
     * @param withdrawalMessageType The withdrawal message type.
     */
    private void validateIRAProvider(final WithdrawalMessageType withdrawalMessageType) {

        if (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE,
                withdrawalRequest.getReasonCode())
                && StringUtils.isBlank(withdrawalRequest.getIraServiceProviderCode())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(withdrawalMessageType,
                    WithdrawalRequestProperty.IRA_SERVICE_PROVIDER_CODE));
        }
    }

    /**
     * Validates that any mandatory calculate fields have been populated as the user may save
     * without calculating.
     */
    private void validateMandatoryCalculateFieldsForSave() {

        // Amount type
        if (StringUtils.isBlank(withdrawalRequest.getAmountTypeCode())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.AMOUNT_TYPE_REQUIRED,
                    WithdrawalRequestProperty.AMOUNT_TYPE_CODE));
        }

        // Specific amount
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE,
                withdrawalRequest.getAmountTypeCode())) {

            // Verify specific amount is larger than zero
            if (!NumberUtils.isGreaterThanZeroValue(withdrawalRequest.getWithdrawalAmount())) {
                withdrawalRequest.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
                        WithdrawalRequestProperty.SPECIFIC_AMOUNT));
            }
        }

        // Money type values
        for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : getWithdrawalRequest()
                .getMoneyTypes()) {

            // Check vesting percentage.
            if (withdrawalRequest.getStatusCode().equals(
                    WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode())
                    || validateVesting || validateForDenyAndSave) {
                if (BooleanUtils
                        .isTrue(withdrawalRequestMoneyType.getVestingPercentageUpdateable())) {
                    if (!(NumberUtils.isInPercentageRange(withdrawalRequestMoneyType
                            .getVestingPercentage()))) {
                        // It's not a valid percentage.
                        withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                                WithdrawalMessageType.VESTED_PERCENTAGE_INVALID,
                                "vestingPercentage"));
                    }
                }
            }

            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE)) {
                if (!(NumberUtils.isGreaterThanOrEqualToZeroValue(withdrawalRequestMoneyType
                        .getWithdrawalAmount()))) {
                    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_AMOUNT_NOT_GREATER_THAN_ZERO,
                            "withdrawalAmount"));
                }
            }

            if (StringUtils.equals(withdrawalRequest.getAmountTypeCode(),
                    WithdrawalRequest.WITHDRAWAL_AMOUNT_PERCENTAGE_MONEYTYPE_CODE)) {
                if (!(NumberUtils.isInPercentageRange(withdrawalRequestMoneyType
                        .getWithdrawalPercentage()))) {
                    withdrawalRequestMoneyType.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.REQUESTED_PERCENTAGE_INVALID,
                            "withdrawalPercentage"));
                }
            }
        }

        // Taxes
        final WithdrawalRequestRecipient withdrawalRequestRecipient = (WithdrawalRequestRecipient) getFirstRecipient();

        final BigDecimal federalPercentage = withdrawalRequestRecipient.getFederalTaxPercent();
        final BigDecimal statePercentage = withdrawalRequestRecipient.getStateTaxPercent();

        final boolean showTax = withdrawalRequest.getShowTaxWitholdingSection();
        final boolean showStateTax = withdrawalRequestRecipient.getShowStateTax();

        if (showTax) {
            if (federalPercentage == null) {
                // Mandatory if shown.
                withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.FEDERAL_TAX_INVALID, "federalTaxPercent"));
            } // fi
            if (showStateTax) {
                if (statePercentage == null) {
                    // Mandatory if shown.
                    withdrawalRequestRecipient.addMessage(new WithdrawalMessage(
                            WithdrawalMessageType.STATE_TAX_INVALID, "stateTaxPercent"));
                }
            }
        }
    }

    /**
     * @return the lastSavedTimestamp or current timestamp if last saved timestamp has not been set.
     */
    public Timestamp getLastSavedTimestamp() {
        if (lastSavedTimestamp == null) {
            return new Timestamp(System.currentTimeMillis());
        } else {
            return lastSavedTimestamp;
        }
    }

    /**
     * This represents the last time the withdrawal was saved (including for a state change, deny,
     * expire etc.).
     * 
     * @param lastSavedTimestamp the lastSavedTimestamp to set.
     */
    public void setLastSavedTimestamp(final Timestamp lastSavedTimestamp) {
        this.lastSavedTimestamp = lastSavedTimestamp;
    }

    /**
     * Sets the value of the last time the withdrawal was saved (including for a state change, deny,
     * expire etc.) to the current system time.
     * 
     * @see System#currentTimeMillis()
     */
    public void setLastSavedTimestamp() {
        if (lastSavedTimestamp == null) {
            this.lastSavedTimestamp = new Timestamp(System.currentTimeMillis());
        }
    }

    /**
     * Primarily used by the ezk site, called to get the most recent withdrawal request regardless
     * of status.
     * 
     * @param profileId The profile ID.
     * @param contractId The contract ID.
     * @return WithdrawalRequest - the withdrawal request requested.
     * @throws DistributionServiceException If an exception occurs.
     */
    public static WithdrawalRequest getMostRecentWithdrawalRequest(final Integer profileId,
            final Integer contractId) throws DistributionServiceException {

        // gets a collection of all the requests for the requested contract and profile id
        final Collection<WithdrawalRequest> requests = getWithdrawalRequests(profileId, contractId);
        if (CollectionUtils.isEmpty(requests)) {
            return null;
        }
        Collection<WithdrawalRequest> withdrawalRequests = processWithdrawalRequestsForParticipant(requests);

        Timestamp earliestDate = new Timestamp((DateUtils.addMonths(new Date(),
                -MONTHS_IN_TWO_YEARS).getTime()));
        Timestamp latestDate = null;
        WithdrawalRequest recentRequest = null;
        // determine the most recent withdrawal txn
        for (WithdrawalRequest withdrawalRequest : withdrawalRequests) {
            final Timestamp createdDate = withdrawalRequest.getCreated();
            // handle the 1st time
            if (latestDate == null) {
                latestDate = createdDate;
                recentRequest = withdrawalRequest;
            } else if (createdDate.after(earliestDate) && createdDate.after(latestDate)) {
                recentRequest = withdrawalRequest;
                latestDate = createdDate;
            }
        }
        if (recentRequest == null) {
            return null;
        }
        // fill the most recent txn with data
        Principal principal = Principal.getSystemUserPrincipal();
        recentRequest.setPrincipal(principal);
        Withdrawal withdrawal = new Withdrawal(recentRequest);

        withdrawal.readWithdrawalRequestForView(recentRequest.getSubmissionId(), recentRequest
                .getPrincipal());

        return withdrawal.getWithdrawalRequest();
    }

    /**
     * Removes the draft, deleted and expired withdrawal request from the list of withdrawal
     * requests.
     * 
     * @param requests The withdrawal requests to process.
     * @return A Collection<WithdrawalRequest> of filtered requests.
     */
    private static Collection<WithdrawalRequest> processWithdrawalRequestsForParticipant(
            Collection<WithdrawalRequest> requests) {
        Collection<WithdrawalRequest> requetsLists = new ArrayList<WithdrawalRequest>();
        for (WithdrawalRequest withdrawalRequest : requests) {
            if (!StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE,
                    withdrawalRequest.getStatusCode())
                    && !isWithdrawalRequestExpired(withdrawalRequest)
                    && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE,
                            withdrawalRequest.getStatusCode())
                    && !StringUtils.equals(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE,
                            withdrawalRequest.getStatusCode())) {

                requetsLists.add(withdrawalRequest);
            }
        }
        return requetsLists;
    }

    /**
     * Returns true if the withdrawal request is expired as of the current system time.
     * 
     * @param withdrawalRequest The request to check.
     * @return boolean - True if the withdrawal request has expired.
     */
    private static boolean isWithdrawalRequestExpired(final WithdrawalRequest withdrawalRequest) {
        if (withdrawalRequest.getExpirationDate() != null) {
            if (withdrawalRequest.getExpirationDate().before(new Date())) {
                return true;
            }
        }
        return false;
    }

    protected void doSaveWithdrawalLegaleseInfo() throws DistributionServiceException {

        try {
            final WithdrawalLegaleseDao legaleseDao = new WithdrawalLegaleseDao();
            legaleseDao.insertWithdrawalLegaleseInfo(withdrawalRequest.getSubmissionId(),
                    withdrawalRequest);
        } catch (DistributionServiceException exception) {
            throw ExceptionHandlerUtility.wrap(exception);
        }
    }

    /**
     * Returns false if the the amount requested to withdraw is less than $200.00 and the state tax
     * is greater than zero.
     * 
     * @return boolean - True if the state tax applied is valid, false otherwise.
     */
    protected boolean validateStateTaxAppliedForParticipantCreated() {
        boolean isValid = true;
        BigDecimal amountRequested = withdrawalRequest.getTotalRequestedWithdrawalAmount();
        if (amountRequested != null) {
            for (Recipient recipient : getWithdrawalRequest().getRecipients()) {
                final BigDecimal stateTaxPercent = recipient.getStateTaxPercent();
                if (TWO_HUNDRED_AMOUNT.compareTo(amountRequested) > 0
                        && NumberUtils.isNotZeroValue(stateTaxPercent)) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    /**
     * validates participant initiated request for state tax changes during Review and Approve
     * process.
     * 
     */
    protected void validateForStateTaxForParticipantCreated() {
        if (withdrawalRequest.getIsParticipantCreated()
                && !validateStateTaxUserSelectionForParticipant()) {
            // withdrawalRequest.setIsParticipantCreated(false);
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.STATE_TAX_CHANGED));
            withdrawalRequest.setIsNoLongerValid(true);
        }

        // Fix: Bug for POW warranty. Request less than $200 to go through
        // if (withdrawalRequest.getIsParticipantCreated() &&
        // withdrawalRequest.getVestingCalledInd()
        // && !validateStateTaxAppliedForParticipantCreated()) {
        // withdrawalRequest.addMessage(new WithdrawalMessage(
        // WithdrawalMessageType.STATE_TAX_CHANGED));
        // withdrawalRequest.setIsNoLongerValid(true);
        //
        // }
    }

    /**
     * This method is used to create a new {@link WithdrawalRequest} for participant initiated from
     * ezk.
     * 
     * @param profileId The profile ID.
     * @param contractId The contract ID.
     * @return WithdrawalRequest - A new withdrawal request.
     */
    public static WithdrawalRequest initiateNewParticipantWithdrawalRequest(
            final Integer profileId, final Integer contractId) {

        WithdrawalRequest withdrawalRequest = null;

        Principal principal = Principal.getSystemUserPrincipal();

        withdrawalRequest = initiateNewWithdrawalRequest(profileId, contractId, principal);

        // override with participant initiated default values
        withdrawalRequest.setUserRoleCode(WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE);
        withdrawalRequest.setParticipantLeavingPlanInd(true);
        withdrawalRequest
                .setAmountTypeCode(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_AVAILABLE_CODE);
        withdrawalRequest.setVestingCalledInd(false);
        withdrawalRequest.setPartWithPbaMoneyInd(false);
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.setIsParticipantCreated(true);
        withdrawalRequest.setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_EZK);
        withdrawalRequest.setCreatedById(new Integer(String.valueOf(principal.getProfileId())));
        withdrawalRequest.setLastUpdatedById(new Integer(String.valueOf(principal.getProfileId())));
        withdrawalRequest.setIgnoreWarnings(true);

        // set money type withdrawal percentage to 100%
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {
            final String moneyTypeId = withdrawalRequestMoneyType.getMoneyTypeId();
            if (moneyTypeId != null) {
                withdrawalRequestMoneyType.setWithdrawalPercentage(new BigDecimal("100.00"));
            }
        }
        if (!CollectionUtils.isEmpty(withdrawalRequest.getLoans())) {
            withdrawalRequest.setLoanOption(WithdrawalRequest.LOAN_CLOSURE_OPTION);
        }
        return withdrawalRequest;
    }

    /**
     * This is called by ezk to submit a participant initiated withdrawal.
     * 
     * @see com.manulife.pension.service.withdrawal.domain.WithdrawalState#sendForReview()
     * 
     * @throws SystemException Thrown if there is an exception.
     */
    public void submitParticipantInitiatedWithdrawal() throws SystemException {

        if ( ! isParticipantAllowedToSubmit(getWithdrawalRequest())) {            
            throw new SystemException(
                    "Employee function shutdown is in effect."
                            + " Participant submitting withdrawal not allowed for payment methods other than Check. "
                            + "contractId:" + getWithdrawalRequest().getContractId()
                            + " employeeProfileId:" + getWithdrawalRequest().getEmployeeProfileId()
                            );
        };
      
        Principal principal = Principal.getSystemUserPrincipal();
        getWithdrawalRequest().setPrincipal(principal);
        getWithdrawalRequest().setRequestRiskIndicator(Boolean.FALSE);
        getWithdrawalRequest().setAtRiskAddress(null);
        
        // check if the withdrawal request is at risk
        AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
		atRiskDetils.setContractId(getWithdrawalRequest().getContractId());
		atRiskDetils.setProfileId(getWithdrawalRequest().getEmployeeProfileId());
		atRiskDetils.setSubmissionId(getWithdrawalRequest().getSubmissionId());
		atRiskDetils.setLoanOrWithdrawalReq(RequestType.WITHDRAWAL);
		
		
		try {

			AtRiskWebRegistrationVO atRiskWebRegistrationVO = null;

			if(AtRiskHandler.getInstance().isRegistrationAtRiskPeriod(atRiskDetils))
				atRiskWebRegistrationVO = AtRiskHandler.getInstance().getWebRigistrationInfo(atRiskDetils);

			AtRiskDetailsVO atRiskDetailsVO = new AtRiskDetailsVO();

			if(atRiskWebRegistrationVO !=  null){
				atRiskDetailsVO.setWebRegistration(atRiskWebRegistrationVO);

				atRiskDetailsVO.setSubmissionId(getWithdrawalRequest().getSubmissionId());

				getWithdrawalRequest().setAtRiskAddress(atRiskWebRegistrationVO.getAddress());
				getWithdrawalRequest().setAtRiskDetailsVO(atRiskDetailsVO);
				getWithdrawalRequest().setRequestRiskIndicator(Boolean.TRUE);
			}


		} catch (SystemException e) {
				//TODO : Properly handling exception ?
				throw new RuntimeException(e);
			}
		
        // get the contract feature again in case it's changed since the last call
        try {
            ContractServiceFeature feature = ContractServiceDelegate.getInstance()
                    .getContractServiceFeature(withdrawalRequest.getContractId(),
                            ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            if (BooleanUtils.isFalse(ContractServiceFeature.internalToBoolean(feature.getValue()))) {
                throw new WithdrawalServiceFeatureException(Withdrawal.CLASS_NAME,
                        "submitParticipantInitiatedWithdrawal()",
                        "Withdrawal Service Feature has been disabled. Feature "
                                + ServiceFeatureConstants.IWITHDRAWALS_FEATURE + "="
                                + feature.getValue());
            }
            if (BooleanUtils.isFalse(ContractServiceFeature.internalToBoolean(feature
                    .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT)))) {
                throw new WithdrawalServiceFeatureException(
                        Withdrawal.CLASS_NAME,
                        "submitParticipantInitiatedWithdrawal()",
                        "Participant Withdrawal Service sub Feature has been disabled. Feature "
                                + ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT
                                + "="
                                + feature
                                        .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_PARTICIPANT));
            }
            // if 2 steps, send for review otherwise send for approval
            if (BooleanUtils.isTrue(ContractServiceFeature.internalToBoolean(feature
                    .getAttributeValue(ServiceFeatureConstants.IWITHDRAWALS_REVIEW)))) {
                withdrawalState.sendForReview(this);
            } else {
                withdrawalState.sendForApproval(this);
            }
        } catch (final ApplicationException e) {
            throw new SystemException(e, CLASS_NAME, "submitParticipantInitiatedWithdrawal()",
                    "contractId:" + withdrawalRequest.getContractId() + "employeeProfileId: "
                            + withdrawalRequest.getEmployeeProfileId());
            
        }
    }

   public void isCensusInformationAvailableforParticipant() {
        if (getIsCensusInformationAvailable()) {
            withdrawalRequest.setIsCensusInfoAvailable(true);
        } else if(withdrawalRequest.isCensusInfoAvailablePDInd()){
            withdrawalRequest.setIsCensusInfoAvailable(true);
        }else{
        	 withdrawalRequest.setIsCensusInfoAvailable(false);
        }
    }

    /**
     * Checks to see if census information is available or not.
     * 
     * @return boolean - True if the census information is available, false otherwise.
     */
    public boolean getIsCensusInformationAvailable() {
        boolean isAvailable = false;
        String reasonCode = getWithdrawalRequest().getReasonCode();
        if (StringUtils.equals(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE, reasonCode)
                && getWithdrawalRequest().getRetirementDate() != null) {
            isAvailable = true;
        } else if (StringUtils.equals(
                WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE, reasonCode)
                && getWithdrawalRequest().getTerminationDate() != null) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private boolean isVestingServiceCalled() {
        return withdrawalRequest.getVestingCalledInd();
    }

    /**
     * Validates Notes for the participant initiated withdrawal request.
     * 
     */
    private void validateNotesToParticipantForPOW() {
        if (withdrawalRequest.getIsParticipantCreated()
                && StringUtils.isNotBlank(withdrawalRequest.getCurrentAdminToParticipantNote()
                        .getNote())) {
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.NOTE_PARTICIPANT_INVALID_FOR_POW,
                    WithdrawalRequestProperty.NOTE_TO_PARTICIPANT));
        }
    }

    /**
     * This method invokes the Data Access Object to insert the Suspicious Pin address for the
     * participant withdrawal request.
     * 
     * @throws SystemException If an exception occurs.
     */
    public void updateAtRiskAddress() throws SystemException {
        try {
        	if (getWithdrawalRequest().getAtRiskDetailsVO() != null) {
    			List<AtRiskDetailsVO> objects = new ArrayList<AtRiskDetailsVO>();
    			AtRiskDetailsVO atRiskDetailsVO = getWithdrawalRequest().getAtRiskDetailsVO();
    			atRiskDetailsVO.setSubmissionId(getWithdrawalRequest().getSubmissionId());
    			objects.add(atRiskDetailsVO);
    			DistributionAtRiskDetailsDAO.insertOrUpdate(objects);
    		}
        } catch (DistributionServiceException e) {
            throw new SystemException(e, CLASS_NAME, "updateAtRiskAddress()", "contractId:"
                    + withdrawalRequest.getContractId() + "employeeProfileId: "
                    + withdrawalRequest.getEmployeeProfileId());
        }
    }
    

    public boolean isValidateVesting() {
        return validateVesting;
    }

    public void setValidateVesting(boolean validateVesting) {
        this.validateVesting = validateVesting;
    }
    
    /**
     * Validates that the organization name length for ACH and Wire payment methods.
     * <ul>
     * <li>For ACH, length cannot be > 30
     * <li>For Wire, length cannot be > 35
     * </ul>
     * This is a limitation for the eTreasury feed when the info goes to Apollo 
     *  
     * @param withdrawalRequestPayee
     */
    public void validateOrganizationName(WithdrawalRequestPayee withdrawalRequestPayee){
        
        //Check the payee/provider value is within the limit        
        if (StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE)) {
            if (withdrawalRequestPayee.getOrganizationName().length() > 30) {
                withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_ACH,
                        "organizationName"));
            }
        }
        else if(StringUtils.equals(withdrawalRequestPayee.getPaymentMethodCode(),
                    WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE)){
            if (withdrawalRequestPayee.getOrganizationName().length() > 35) {
                withdrawalRequestPayee.addMessage(new WithdrawalMessage(
                        WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_WIRE,
                        "organizationName"));
            }
        }
    }
    /**
     * A check for the field is mandatory or not 
	 * when the contract is a bundled and before sending for Approval.
     * @return Boolean
     */
	public boolean isMandatoryForBundledContract() {
		boolean bgaInd = getWithdrawalRequest().getContractInfo()
				.isBundledGaIndicator();
		ContractPermission tpaFirmContractPermission = null;

		if (withdrawalRequest.getContractPermission() == null) {
			try {
				tpaFirmContractPermission = SecurityServiceDelegate
						.getInstance().getTpaFirmContractPermission(
								withdrawalRequest.getContractId());
				withdrawalRequest.setContractPermission(tpaFirmContractPermission);
			} catch (SystemException e) {
				throw ExceptionHandlerUtility.wrap(e);
			}
		} else {
			tpaFirmContractPermission = withdrawalRequest
					.getContractPermission();
		}

		if (bgaInd
				&& (WithdrawalStateEnum.DRAFT.getStatusCode().equals(
						withdrawalRequest.getStatusCode()) || WithdrawalStateEnum.PENDING_REVIEW
						.getStatusCode().equals(
								withdrawalRequest.getStatusCode()))
				&& tpaFirmContractPermission.isSigningAuthority()) {
			return true;
		}
		return false;
	}

	public boolean isRestartReview() {
		return restartReview;
	}

	public void setRestartReview(boolean restartReview) {
		this.restartReview = restartReview;
	}
	
	/**
     * Validate participant is applicable to LIA.
     */
	private void validateLIAAvailableForParticipant() {
		try {
			LifeIncomeAmountDetailsVO particpantLIADetails = ContractServiceDelegate
					.getInstance().getLIADetailsByParticipantId(
							String
									.valueOf(withdrawalRequest
											.getParticipantId()));
			if (particpantLIADetails.isLIAAvailableForParticipant()) {
				withdrawalRequest.addMessage(new WithdrawalMessage(
						WithdrawalMessageType.PARTICPANT_APPLICABLE_TO_LIA));
			}
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	/**
	 * PAR Changes
	 * 
	 * Method to Read Risk Activities
	 * 
	 * @param submissionId
	 * @param wr
	 * @throws DistributionServiceException
	 * @throws SystemException
	 */
	private void calculateAtRiskActivities(final Integer submissionId,
			WithdrawalRequest wr) throws DistributionServiceException {
		try {
			boolean isAtRisk = false;

			AtRiskDetailsVO atRiskDetailsVO = null;

			AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
			atRiskDetils.setContractId(wr.getContractId());
			atRiskDetils.setProfileId(wr.getEmployeeProfileId());
			atRiskDetils.setSubmissionId(submissionId);
			atRiskDetils.setLoanOrWithdrawalReq(RequestType.WITHDRAWAL);

			AtRiskAddressChangeVO atRiskAddressChangeVO = null;
			boolean isWebRegAtRisk;

			isWebRegAtRisk = AtRiskHandler.getInstance().isRegistrationAtRiskPeriod(atRiskDetils);

			if(isWebRegAtRisk ){
				if(atRiskDetailsVO == null){
					atRiskDetailsVO = new AtRiskDetailsVO();
					atRiskDetailsVO.setWebRegistration(AtRiskHandler.getInstance().getWebRigistrationInfo(atRiskDetils));
				}
				//We should have get the WebRegistrationVO from DISTRIBUTION_AT_RISK_DETAIL
				if(atRiskDetailsVO.getWebRegistration() != null){
					atRiskDetailsVO.getWebRegistration().setWebRegAtRiskPeriod(isWebRegAtRisk);
				}
				atRiskAddressChangeVO = AtRiskHandler.getInstance().getCurrentAddress(atRiskDetils);

				atRiskDetailsVO.setAddresschange(atRiskAddressChangeVO);
				isAtRisk = true;
			}

			ArrayList <AtRiskPasswordResetVO> passwordResetVOList = 
					AtRiskHandler.getInstance().getForgotUserNameAndPassowordFunction(atRiskDetils);

			AtRiskPasswordResetVO atRiskPasswordResetVO = null;
			AtRiskForgetUserName forgetUserNameVO = null;

			//If the list size is >= 2 means, Password Reset & Forgot Username functions are at risk
			if(passwordResetVOList.size() >= 2){
				
				forgetUserNameVO = AtRiskHandler.getInstance().getAtRiskActivitiesForgotUserName(atRiskDetils);
				
				for(AtRiskPasswordResetVO passwordResetVo :passwordResetVOList){
					if(SecurityActivityTypeCode.SA_PS_INIT_RESET_USER_PWD_SENT.getValue()
							.equals(passwordResetVo.getActivityTypeCode())){
						atRiskPasswordResetVO = AtRiskHandler.getInstance().getForgotPasswordFunction(atRiskDetils);
					}
				}
			}

			//If they are at risk period then only these Objects will have values. 
			//Hence no need to check again whether they are in risk period or not  
			if(forgetUserNameVO != null && atRiskPasswordResetVO != null){
				if(atRiskDetailsVO == null)
					atRiskDetailsVO = new AtRiskDetailsVO();
				atRiskDetailsVO.setForgetUserName(forgetUserNameVO);
				atRiskDetailsVO.setPasswordReset(atRiskPasswordResetVO);
				isAtRisk = true;
			}

			if(isAtRisk){
				atRiskDetailsVO.setSubmissionId(submissionId);
				//wr.setAtRiskAddress(atRiskAddressChangeVO.getApprovalAddress());
				wr.setRequestRiskIndicator(Boolean.TRUE);
				wr.setAtRiskDetailsVO(atRiskDetailsVO);
			}
		} catch (SystemException e) {
			throw new DistributionServiceDaoException("Exception thrown while reading Risk Activities"+e.getMessage(),e); 
		}
	}
    
	/**
	 * Determines if participant is allowed to submit withdrawal
	 * @param withdrawalRequest
	 * @return
	 */
	public static boolean isParticipantAllowedToSubmit(WithdrawalRequest withdrawalRequest) {
        
        boolean isAllowed = false;
      
        try {
            isAllowed = EmployeeServiceDelegate.getInstance(new BaseEnvironment().getAppId())
                    .isFunctionAvailableForParticipant(withdrawalRequest.getContractId(), 
                            withdrawalRequest.getEmployeeProfileId().longValue(),
                            FunctionCode.WITHDRAWALS); 
        } catch (Exception e) {
            throw ExceptionHandlerUtility.wrap(e);
        }   
                
        if (! isAllowed) {
            String paymentTo = withdrawalRequest.getPaymentTo();            
            if (StringUtils.equalsIgnoreCase(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                    || StringUtils.equalsIgnoreCase(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                    || StringUtils.equalsIgnoreCase(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE) ) {
            
                for (final Recipient myRecipient : withdrawalRequest.getRecipients()) {
                    // allowed as long as the payment method is Check
                    for (final Payee payee : myRecipient.getPayees()) {
                        if (StringUtils.equalsIgnoreCase(
                                payee.getPaymentMethodCode(), Payee.CHECK_PAYMENT_METHOD_CODE)) {
                            isAllowed = true;
                        }
                    }
                }
            } else {
                isAllowed = true;
            }
        }    
        return isAllowed;
    }	

    /**
     * Determines whether the Eft payee name should be editable or not. Currently the eft
     * payee name is editable if:
     * <ul>
     * <li>Payment To is Rollover to IRA.
     * <li>Payment To is Rollover to plan.
     * <li>Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li>Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @param withdrawalRequestPayee The payee to check.
     * @return boolean - True if the Eft payee name is editable, false otherwise.
     */
    public boolean isEftPayeeNameEditable(final WithdrawalRequestPayee withdrawalRequestPayee) {

        final String paymentTo = withdrawalRequest.getPaymentTo();
        if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE)
                || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE)) {
            return false;
        } else if (StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)) {
            return true;
        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(paymentTo,
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE)) {

            // Determine if we are the first payee or the second payee
            return isFirstPayee(withdrawalRequestPayee);
        } else {
            return false;
        }
    }  
    
    /**
     * validateRolloverType()
     */
    protected void validateRolloverType() {
    	
        if ((WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE.equals(withdrawalRequest.getReasonCode()) || 
        		WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE.equals(withdrawalRequest.getReasonCode())
        		|| WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode())
        		|| WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE.equals(withdrawalRequest.getReasonCode()))        		
        		&& StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, withdrawalRequest.getPaymentTo()) 
        		&& withdrawalRequest.isRolloverTypeEligible() ) {
               ParticipantInfo participantInfo = withdrawalRequest.getParticipantInfo();
   	        for (final Recipient myRecipient : getWithdrawalRequest().getRecipients()) {
   	            for (final Payee payee : myRecipient.getPayees()) {
   	            	if (!StringUtils.isNotEmpty(payee.getRolloverType())) {
   	            		WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
   	            		withdrawalRequestPayee.addMessage(new WithdrawalMessage(
   	            		WithdrawalMessageType.MISSING_ROLLOVER_TYPE,
   	            		WithdrawalRequestProperty.ROLLOVER_TYPE));
   	            		
   	            	}
   	       	     // Check for participant Roth money
   	            	if (WithdrawalRequest.TRADTIONAL_IRA.equals(payee.getRolloverType()) && participantInfo.getParticipantHasRothMoney()) {
   	            		WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
   	            		withdrawalRequestPayee.addMessage(new WithdrawalMessage(
   	            		WithdrawalMessageType.ROLLOVER_TYPE_MUST_BE_ROTH_IRA,
   	            		WithdrawalRequestProperty.ROLLOVER_TYPE));
   	                }
   	                
   	            }
   	        }
   	        
   	     for (final Recipient myRecipient : getWithdrawalRequest().getRecipients()) {
	             getRolloverType(myRecipient.getPayees()); 
	       }

       	}
        // eligible for Traditional IRA , if Roth balance either(EEROT or EERRT) is Zero, else not eligible
      		else if ((WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE.equals(withdrawalRequest.getReasonCode()))
      				&& StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE,
      						withdrawalRequest.getPaymentTo())) {
      			ParticipantInfo participantInfo = withdrawalRequest.getParticipantInfo();
      			for (final Recipient myRecipient : getWithdrawalRequest().getRecipients()) {
      				for (final Payee payee : myRecipient.getPayees()) {
      					if (!StringUtils.isNotEmpty(payee.getRolloverType())) {
      						WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
      						withdrawalRequestPayee.addMessage(new WithdrawalMessage(
      								WithdrawalMessageType.MISSING_ROLLOVER_TYPE, WithdrawalRequestProperty.ROLLOVER_TYPE));

      					}
      					// Check for participant Roth money
      					if (WithdrawalRequest.TRADTIONAL_IRA.equals(payee.getRolloverType())
      							&& participantInfo.getParticipantHasRothMoney()) {
      						for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : getWithdrawalRequest()
      								.getMoneyTypes()) {
      							if ((withdrawalRequestMoneyType.getMoneyTypeId().equals("EEROT")
      									|| withdrawalRequestMoneyType.getMoneyTypeId().equals("EERRT"))
      									&& NumberUtils
      											.isGreaterThanZeroValue(withdrawalRequestMoneyType.getWithdrawalAmount())) {
      								WithdrawalRequestPayee withdrawalRequestPayee = (WithdrawalRequestPayee) payee;
      								withdrawalRequestPayee.addMessage(
      										new WithdrawalMessage(WithdrawalMessageType.ROLLOVER_TYPE_MUST_BE_ROTH_IRA,
      												WithdrawalRequestProperty.ROLLOVER_TYPE));
      							} 						
      						}

      					}
      				}
      			}
      			for (final Recipient myRecipient1 : getWithdrawalRequest().getRecipients()) {
      				getRolloverType(myRecipient1.getPayees());
      			}

      		}
       }
    
    /***
     * saveRolloverType
     */
    protected void saveRolloverType() {
    	
      if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, withdrawalRequest.getPaymentTo()) && withdrawalRequest.isRolloverTypeEligible() ) {   	     
    	   for (final Recipient myRecipient : getWithdrawalRequest().getRecipients()) {
               getRolloverType(myRecipient.getPayees()); 
          }
        }
    }
    
    /**
     * 
     * @param collection
     * @return
     */
    private String getRolloverType(Collection<Payee> collection) {
		 // Set Rollover type value     
      if (collection.size() == 1) {
      	for (Payee payee : collection) {
      		if(StringUtils.isNotEmpty(payee.getRolloverType())) {
	        		if (WithdrawalRequest.TRADTIONAL_IRA.equals(payee.getRolloverType())) {
	        			payee.setTaxes(TRADITIONAL_IRA);
	        		}else {
	        			payee.setTaxes(ROTH_IRA);
	        		}
      		}
      	}
      }
	return "";
	}
      
    /**
     * 
     * @param collection
     * @return
     */
    private String getRolloverTypeValue(Collection<Recipient> collection) {
		 // Set Rollover type value   
    	MultiPayeeTaxes mp = new MultiPayeeTaxes();
    	 for (final Recipient myRecipient : collection) {
    		 for (Payee payee : myRecipient.getPayees()) {
    			 getValues(payee.getTaxes(), mp);
    			 if(mp.getRothIRA().equals("Y")){
    				 payee.setRolloverType(WithdrawalRequest.ROTH_IRA);
    			 }else{
    				 payee.setRolloverType(WithdrawalRequest.TRADTIONAL_IRA);
    			 }
    		 }
    	 }
	return "";
	}
    
    
    public static void getMultipayeeSection (Integer contractId, WithdrawalRequest withdrawalRequest){
    	
    	String botInd     = "N";
        String earInd     = "N";
        String rothInd    = "N";
        String nonRothInd = "N";
        String moneyTypes = "";
		try {
			ParticipantInfo pinfo = withdrawalRequest.getParticipantInfo();
			List< WithdrawalRequestMoneyType> moneyTypesList =  (List<WithdrawalRequestMoneyType>) pinfo.getMoneyTypes();			
			String prefix="";
			for(WithdrawalRequestMoneyType mtList : moneyTypesList) {
		      moneyTypes += prefix + "'" + mtList.getMoneyTypeId().trim() + "'";
		      prefix=",";				
			}		
			List<MultiPayeeMoneyType>mpmType = WithdrawalInfoDao.getParticipantMoneytypetaxCode(contractId,moneyTypes);
			for(MultiPayeeMoneyType mpMoneyType : mpmType){
				if (mpMoneyType.getTaxCode().equals("EAR")) {
						earInd = "Y";
					if (mpMoneyType.getRothInd().equals("Y")) {
						rothInd ="Y";
					}else if (mpMoneyType.getRothInd().equals("N")) {
						nonRothInd="Y";
					}
				}
				if(mpMoneyType.getTaxCode().equals("BOT")) {
					botInd = "Y";
				}
			}
		} catch (SystemException systemException) {
		
			throw ExceptionHandlerUtility.wrap(systemException);
		} 
		    	
		
		try {
			List <WithdrawalMultiPayee> mpList = WithdrawalInfoDao.getMultipayeeDiplayFlag(botInd,earInd,rothInd,nonRothInd);
			for(WithdrawalMultiPayee multiPayee : mpList){
				if("TB".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setTaxableBal("TB");
				}
				if("PA".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setParticipant("PA");
				}
				if("PAAT".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setParticipantAftrTax("PAAT");
				}
				if("PAR".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setParticipantRoth("PAR");
				}if("NRAT".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setParticipantNonRoth("NRAT");
				}if("RB".equals(multiPayee.getPayeeCategories().trim())){
					withdrawalRequest.setRothBal("RB");
				}
				
			}
		} catch (SystemException systemException) {
			throw ExceptionHandlerUtility.wrap(systemException);
		}

    }
     /**
      * ULTRAS -1939 OW - PSW - Hardships - Error out for invalid money types
      */
    private void validateInvalidMoneyTypeForHardship(){
    	
    	Collection<WithdrawalRequestMoneyType> moneyType = withdrawalRequest.getMoneyTypes();
    	if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){
    	for(WithdrawalRequestMoneyType wdMoneyType : moneyType){
    		if(withdrawalRequest.getAvilableHarshipMoneyType() !=null && !withdrawalRequest.getAvilableHarshipMoneyType().contains(wdMoneyType.getMoneyTypeId().trim())){
    			if(wdMoneyType.getWithdrawalAmount() != null && wdMoneyType.getWithdrawalAmount().compareTo(BigDecimal.ZERO) > 0){
    				 wdMoneyType.setHardshipFlag(true);
    				withdrawalRequest.addMessage(new WithdrawalMessage(
    	                    WithdrawalMessageType.INVALID_MONEY_TYPE,
    	                    WithdrawalRequestProperty.INVALID_MONEY_TYPE));
    				
    			}
    		}
    	}
    	
    	}	
    
    	
    }
    
    /**
     * ULTRAS - 1940 Error out if EEDEF amount is greater than Maximum hardship allowed
     */
    private void validateEEDFFMaximumHarshipAmount(){
    	Collection<WithdrawalRequestMoneyType> moneyType = withdrawalRequest.getMoneyTypes();
    	
    	
    	if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){
    		
    		for(WithdrawalRequestMoneyType wdMoneyType : moneyType){
    			BigDecimal maxHardshipAmount = null ;
        		if(withdrawalRequest.getMaximumHarshipAmount() == null){
        			maxHardshipAmount = new BigDecimal(999999.99);
        		}else{
        			maxHardshipAmount = withdrawalRequest.getMaximumHarshipAmount();
        		}
    			if(withdrawalRequest.getAvilableHarshipMoneyType() !=null && !withdrawalRequest.getAvilableHarshipMoneyType().contains(wdMoneyType.getMoneyTypeId().trim())){
    				if(wdMoneyType.getTotalBalance() !=null ){
    				if(wdMoneyType.getTotalBalance().compareTo(maxHardshipAmount) == 1){
    					if("EEDEF".equals(wdMoneyType.getMoneyTypeId())){
    					if(withdrawalRequest.getWithdrawalAmount() !=null && wdMoneyType.getWithdrawalAmount().compareTo(withdrawalRequest.getMaximumHarshipAmount()) == 1){
    	    				withdrawalRequest.addMessage(new WithdrawalMessage(
    	    	                    WithdrawalMessageType.MAX_HARDSHIP_AMOUNT,
    	    	                    "withdrawalAmount"));
    					}}
    				}
    			}
    		}
    		}
    	}
    }
    /**
     * ULTRAS -2074 Error out if total withdrawal amount is greater than maximum withdrawal amount
     */
    private void validateMaximumHarshipAmount(){
    
    	    BigDecimal maxHardshipAmount = null ;
    		if(withdrawalRequest.getMaximumHarshipAmount() == null){
    			maxHardshipAmount = new BigDecimal(999999.99);
    		}else{
    			maxHardshipAmount = withdrawalRequest.getMaximumHarshipAmount();
    		}
    		if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){			
    		if(withdrawalRequest.getWithdrawalAmount() !=null && withdrawalRequest.getWithdrawalAmount().compareTo(maxHardshipAmount) == 1){
    			withdrawalRequest.addMessage(new WithdrawalMessage(
    	    	WithdrawalMessageType.MAXIMUM_HARDSHIP_AMOUNT,
    	    	"withdrawalAmount"));
    		}
    		}else{
    			if(withdrawalRequest.getTotalBalance() !=null && withdrawalRequest.getTotalBalance().compareTo(maxHardshipAmount) > 0){
    			withdrawalRequest.addMessage(new WithdrawalMessage(
 		    	       WithdrawalMessageType.MAXIMUM_HARDSHIP_AMOUNT,
 		    	       "availableHardshipAmount"));
    			}
    		}

    }
    /**
     * ULTRAS -2075 Error out if total withdrawal amount is less than minimum withdrawal amount
     */
    private void validateMinimumHarshipAmount(){
    		
    		BigDecimal minHardshipAmount = null ;
    		if(withdrawalRequest.getMinimumHarshipAmount() == null){
    			minHardshipAmount = new BigDecimal(0.00);
    		}else{
    			minHardshipAmount = withdrawalRequest.getMinimumHarshipAmount();
    		}
    		if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){	
    		if(withdrawalRequest.getWithdrawalAmount() !=null && withdrawalRequest.getWithdrawalAmount().compareTo(minHardshipAmount) == -1){
    	       withdrawalRequest.addMessage(new WithdrawalMessage(
    	       WithdrawalMessageType.MINIMUM_HARDSHIP_AMOUNT,
    	       "withdrawalAmount"));
    		}
    		}else{
    			if(withdrawalRequest.getTotalBalance() !=null && withdrawalRequest.getTotalBalance().compareTo(minHardshipAmount) < 0 ){
    			 withdrawalRequest.addMessage(new WithdrawalMessage(
    		    	       WithdrawalMessageType.MINIMUM_HARDSHIP_AMOUNT,
    		    	       "availableHardshipAmount"));
    			}
    		}
    }
    /**
     * This method is called after a successful submitting request from participant site
     * 
     * @throws DistributionServiceException thrown if there is an exception
     */
    public void sendWithdrawalNotificationsForReviewOrApprove(String statusCode) throws DistributionServiceException {

        final String manulifeCompanyId = withdrawalRequest.getParticipantInfo()
                .getManulifeCompanyId();

        final Location location = BrowseServiceHelper
                .convertManulifeCompanyIdToLocation(manulifeCompanyId);

        final WithdrawalEmailVO withdrawalEmailVO = new WithdrawalEmailVO(withdrawalRequest
                .getSubmissionId(), withdrawalRequest.getContractId(), withdrawalRequest
                .getContractName(), null, withdrawalRequest.getStatusCode(), withdrawalRequest
                .getRequestType(), withdrawalRequest.getFirstName(), withdrawalRequest
                .getLastName(), null, location, withdrawalRequest.getRequestDate(),
                withdrawalRequest.getExpirationDate(), withdrawalRequest.getPrincipal(),
                withdrawalRequest.getContractInfo().getClientAccountRepEmail(), withdrawalRequest
                        .getLastFeeChangeByTPAUserID(), withdrawalRequest
                        .isLastFeeChangeWasPSUserInd(), withdrawalRequest.getCmaSiteCode(),
                withdrawalRequest.getEmployeeProfileId());

        final WithdrawalEmailHandler withdrawalEmailHandler = new WithdrawalEmailHandler();
        try {
                withdrawalEmailHandler.sendEmailToParticipant(withdrawalEmailVO,
                        WithdrawalEmailHandler.EVENT_TYPE_ID_SUBMITTED);
        } catch (final WithdrawalEmailException withdrawalEmailException) {
            String state = statusCode.equalsIgnoreCase("W6")?"review":"approve";
            logger.error("Exception while trying to send email on " + "entering pending"+state+
                     withdrawalEmailException.toString());
            throw new RuntimeException(withdrawalEmailException);
        } // end try/catch
        if(statusCode.equalsIgnoreCase("W6")){ // review
            WithdrawalHelper.fireWithdrawalPendingReviewEvent(withdrawalRequest);
        }
        else{
            WithdrawalHelper.fireWithdrawalPendingApprovalEvent(withdrawalRequest);
        }
  }
    /**
	 * Withdrawal Reason list 
	 * @return
	 */
	
	public List<String> getWithdrawalTypes(){
		
		List<String> withdrawalTypes = new ArrayList <String>();
		
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
		
		return withdrawalTypes;
				
	} 
	/**
	 * ULTRAS -2080 Changes
	 */
    private void validateAvailableHardshipAmount(){
    	Collection<WithdrawalRequestMoneyType> moneyType = withdrawalRequest.getMoneyTypes();
    		for(WithdrawalRequestMoneyType wdMoneyType : moneyType){
    			if(withdrawalRequest.getAvilableHarshipMoneyType() != null && withdrawalRequest.getAvilableHarshipMoneyType().contains(wdMoneyType.getMoneyTypeId().trim())){
    			 if("EEDEF".equals(wdMoneyType.getMoneyTypeId().trim())){
    				 if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){
    				 if(wdMoneyType.getWithdrawalAmount() !=null && wdMoneyType.getAvailableHarshipAmount().compareTo(BigDecimal.valueOf(0.00)) == 0 && wdMoneyType.getWithdrawalAmount().compareTo(BigDecimal.ZERO) >0){
    					wdMoneyType.setHardshipAvaliableamountFlag(true);
    					withdrawalRequest.addMessage(new WithdrawalMessage(
	    	                    WithdrawalMessageType.AVAILABLE_HARDSHIP_AMOUNT,
	    	                    WithdrawalRequestProperty.AVAILABLE_HARDSHIP_AMOUNT));
    				}
    				 }else{
    					 if(wdMoneyType.getAvailableHarshipAmount().compareTo( BigDecimal.valueOf(0.00)) == 0 ){
    	    					wdMoneyType.setHardshipAvaliableamountFlag(true);
    	    					withdrawalRequest.addMessage(new WithdrawalMessage(
    		    	                    WithdrawalMessageType.AVAILABLE_HARDSHIP_AMOUNT,
    		    	                    WithdrawalRequestProperty.AVAILABLE_HARDSHIP_AMOUNT));
    					 }
    				 }
    			}
    		}else {
    			if(WithdrawalRequest.WITHDRAWAL_AMOUNT_MAXIMUM_DEFERRAL_MONEYTYPE_CODE.equals(withdrawalRequest.getAmountTypeCode())){
    				if("EEDEF".equals(wdMoneyType.getMoneyTypeId().trim())){
    				if(wdMoneyType.getAvailableHarshipAmount().compareTo(BigDecimal.valueOf(0.00)) == 0 ){
    					wdMoneyType.setHardshipAvaliableamountFlag(true);
    					withdrawalRequest.addMessage(new WithdrawalMessage(
	    	                    WithdrawalMessageType.AVAILABLE_HARDSHIP_AMOUNT,
	    	                    WithdrawalRequestProperty.AVAILABLE_HARDSHIP_AMOUNT));
    				}
    			}
    		}
    		}	
    	}
    }
    /**
	 * ULTRAS -2080 Changes
	 */
    private void validateHardshipAmount(){
    	
    	Collection<WithdrawalRequestMoneyType> moneyType = new ArrayList();
    	if(WithdrawalRequest.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE.equals(withdrawalRequest.getAmountTypeCode())){
    		for(WithdrawalRequestMoneyType wdMoneyType : withdrawalRequest.getMoneyTypes()) {
    			 wdMoneyType.setHardshipAvaliableamountFlag(false);
    			 wdMoneyType.setHardshipAvaliableamountEedefFlag(false);
    			if(withdrawalRequest.getAvilableHarshipMoneyType() != null && withdrawalRequest.getAvilableHarshipMoneyType().contains(wdMoneyType.getMoneyTypeId().trim()) ){
    				if(wdMoneyType.getAvailableHarshipAmount().compareTo(BigDecimal.ZERO) >0  && wdMoneyType.getWithdrawalAmount().compareTo(BigDecimal.ZERO) >0 ){
    				if(withdrawalRequest.getWithdrawalAmount() !=null && wdMoneyType.getWithdrawalAmount().compareTo(wdMoneyType.getAvailableHarshipAmount()) == 1){
    					 if("EEDEF".equals(wdMoneyType.getMoneyTypeId().trim())){
    						 	wdMoneyType.setHardshipAvaliableamountEedefFlag(true);
    						 	withdrawalRequest.addMessage(new WithdrawalMessage(
    						 			WithdrawalMessageType.MAX_HARDSHIP_AMOUNT,
    						 			WithdrawalRequestProperty.AVAILABLE_HARDSHIP_AMOUNT));
    					 }else if (!("EEDEF".equals(wdMoneyType.getMoneyTypeId().trim()))) {
    						 wdMoneyType.setHardshipAvaliableamountFlag(true);
 						 	withdrawalRequest.addMessage(new WithdrawalMessage(
 						 			WithdrawalMessageType.MAX_HARDSHIP_AMOUNT,
 						 			WithdrawalRequestProperty.AVAILABLE_HARDSHIP_AMOUNT));
    					 }
					}
    		}
    			}
    		moneyType.add(wdMoneyType);
    		}
    		withdrawalRequest.setMoneyTypes(moneyType);
    	 }
    	}
    
    public  void getValues (String taxesFlag ,MultiPayeeTaxes payee ) {
		 
    	if(taxesFlag != null && taxesFlag.trim().length()>0){
		 taxesFlag = taxesFlag.substring(1,taxesFlag.length() - 1);
		 String newtaxFlag = taxesFlag.replaceAll("\"", "");
		 String newFlag[] = newtaxFlag.split(","); 
		 String tax= null; 
		 for( String tax1 :newFlag) 
		 { 
			 if(tax1.trim().equals("Non_Taxable:Y")) {
				 payee.setNonTaxable("Y");
			 }
			 if(tax1.trim().equals("Taxable:Y")) {
				 payee.setTaxable("Y");
			 }
			 if(tax1.trim().equals("Roth_Non_Tax:Y")) {
				 payee.setRothNonTax("Y");
			 }
			 if(tax1.trim().equals("Roth_Taxable:Y")) {
				 payee.setRothTaxable("Y");
			 }
			 if(tax1.trim().equals("Roth_IRA:Y")) {
				 payee.setRothIRA("Y");
			 }
		 }
    	}
     }
}
