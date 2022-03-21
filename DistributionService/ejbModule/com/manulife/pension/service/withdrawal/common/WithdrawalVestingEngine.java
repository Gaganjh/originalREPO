package com.manulife.pension.service.withdrawal.common;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.VestingServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.employee.EmployeeConstants;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.util.BaseEnvironment;

/**
 * WithdrawalVestingEngine calculates vesting for the requests that have simple reasons.
 * 
 * @author Mihai Popa
 */
public class WithdrawalVestingEngine {

    private static final Logger logger = Logger.getLogger(WithdrawalVestingEngine.class);

    /**
     * Default Constructor.
     */
    public WithdrawalVestingEngine() {
        if (logger.isDebugEnabled()) {
            logger.debug("enter: WithdrawalVestingEngine()");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit: WithdrawalVestingEngine()");
        }
    }

    /**
     * Sets the vesting percentage and the vestingPercentage updatable flag. This calls the vesting
     * engine to get the data. The updated values are saved directly into the
     * {@link WithdrawalReason} provided.
     * 
     * @param withdrawalRequest The withdrawal request to get the vesting details for.
     */
    public void calculate(final WithdrawalRequest withdrawalRequest) {

        EmployeeVestingInformation employeeVestingInformation = null;
        final Date eventDate = getVestingEventDate(withdrawalRequest);

        if (eventDate == null) {
            employeeVestingInformation = EmployeeVestingInformation.EMPTY;
        } else {
            try {
                employeeVestingInformation = getEmployeeVestingInformation(withdrawalRequest);
            } catch (VestingException vestingException) {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn(vestingException);
                } // fi

                // Note: The handler below sets the WithdrawalRequest's vestingCriticalError field.
                handleVestingCriticalError(withdrawalRequest);
            } // end try/catch
            if (logger.isDebugEnabled()) {
                logger.debug("EmployeeVestingInformation: " + employeeVestingInformation);
            } // fi

            applyVestingInformationToWithdrawalReqeust(employeeVestingInformation,
                    withdrawalRequest);

            WithdrawalVestingMessageHelper.getInstance().addVestingErrors(withdrawalRequest,
                    employeeVestingInformation);
        }
    }

    /**
     * Gets the employee vesting information object based on the current withdrawal request.
     * 
     * @param withdrawalRequest
     * @return
     * @throws VestingException
     */
    public EmployeeVestingInformation getEmployeeVestingInformation(
            final WithdrawalRequest withdrawalRequest) throws VestingException {

        final Date eventDate = getVestingEventDate(withdrawalRequest);

        // Call the actual vesting engine.
        final EmployeeVestingInformation employeeVestingInformation;

        if (eventDate == null) {
            employeeVestingInformation = EmployeeVestingInformation.EMPTY;
        } else {
            try {
                employeeVestingInformation = VestingServiceDelegate
                        .getInstance((new BaseEnvironment()).getAppId())
                        .getEmployeeWithdrawalVestingInformation(
                                withdrawalRequest.getContractId(),
                                withdrawalRequest.getEmployeeProfileId().longValue(),
                                lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(withdrawalRequest
                                        .getReasonCode()), eventDate);

            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
        }
        return employeeVestingInformation;
    }

    /**
     * Applies the vesting information to the withdrawal request object.
     * 
     * @param employeeVestingInformation The vesting information to apply.
     * @param withdrawalRequest The withdrawal request object to apply the data to.
     */
    public void applyVestingInformationToWithdrawalReqeust(
            final EmployeeVestingInformation employeeVestingInformation,
            final WithdrawalRequest withdrawalRequest) {
        boolean foundANull = false;
        // Now move the data into the withdrawal objects.

        // These values may be null if there was some kind of critical error.
        if (employeeVestingInformation == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("employeeVestingInformation was null.");
            } // fi
            return;
        } // fi

        final Map moneyTypeVestingPercentages = employeeVestingInformation
                .getMoneyTypeVestingPercentages();
        if (moneyTypeVestingPercentages == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("moneyTypeVestingPercentages was null.");
            } // fi
            return;
        } // fi

        // Set flags to check the CSF settings.
        final boolean vestingServiceFeatureIsProvided = StringUtils.equals(
                employeeVestingInformation.getVestingServiceFeature(),
                ServiceFeatureConstants.PROVIDED);
        final boolean vestingServiceFeatureIsCalculated = StringUtils.equals(
                employeeVestingInformation.getVestingServiceFeature(),
                ServiceFeatureConstants.CALCULATED);

        // We only set the LatestVestingEffectiveDate if it's a provide contract.
        if (vestingServiceFeatureIsProvided) {

            Collection<MoneyTypeVestingPercentage> vestingPercentages = (Collection<MoneyTypeVestingPercentage>) moneyTypeVestingPercentages
                    .values();

            // Note: this is going across all values from the vesting engine, not just the
            // values that are displayed on withdrawals.

            Date latest = null;
            for (MoneyTypeVestingPercentage moneyTypeVestingPercentage : vestingPercentages) {
                if (moneyTypeVestingPercentage != null) {
                    if ((latest == null)
                            || ((moneyTypeVestingPercentage.getEffectiveDate() != null) && (moneyTypeVestingPercentage
                                    .getEffectiveDate().after(latest)))) {
                        latest = moneyTypeVestingPercentage.getEffectiveDate();
                    } // fi
                } // fi
            } // end for

            // Exclude the 'LOW_DATE' as it may have been used as a null filler.
            if ((latest != null) && (!(DateUtils.isSameDay(latest, VestingConstants.LOW_DATE)))) {
                withdrawalRequest.setLatestVestingEffectiveDate(latest);
            } else {
                withdrawalRequest.setLatestVestingEffectiveDate(null);
            } // fi
        } // fi

        // This flag keeps track of us finding a vesting percentage value that allows updates in
        // withdrawals.
        boolean foundVestingChangePermitted = false;

        // This loops over only the money types that are displayed in withdrawals, there may
        // be more money types than this that are a result of output from the vesting engine.
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {

            final String moneyTypeId = withdrawalRequestMoneyType.getMoneyTypeId();

            final MoneyTypeVestingPercentage moneyTypeVestingPercentage = (MoneyTypeVestingPercentage) moneyTypeVestingPercentages
                    .get(StringUtils.trim(moneyTypeId));

            if (moneyTypeVestingPercentage != null) {
                withdrawalRequestMoneyType.setVestingEngineValue(moneyTypeVestingPercentage
                        .getPercentage());
                withdrawalRequestMoneyType.setVestingPercentage(moneyTypeVestingPercentage
                        .getPercentage());

                final boolean isChangePermitted = moneyTypeVestingPercentage.isChangePermitted();

                withdrawalRequestMoneyType.setVestingPercentageUpdateable(isChangePermitted);

                if (isChangePermitted) {
                    foundVestingChangePermitted = true;
                } // fi

            } else {
            	withdrawalRequestMoneyType.setVestingEngineValue(BigDecimal.ZERO);
                foundANull = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("\nFailed to find a vesting value with: [" + moneyTypeId + "]");
                } // fi
            } // fi
        } // end for

        if (foundANull && CollectionUtils.isNotEmpty(employeeVestingInformation.getErrors())) {
            withdrawalRequest.setVestingNonCriticalErrorWithWarning(true);
        } // fi

        // We found at least one value from the vesting engine where it allows changes
        // to the value and vesting is turned on.
        if (foundVestingChangePermitted
                && (vestingServiceFeatureIsProvided || vestingServiceFeatureIsCalculated)) {
            // Show warning message.
            withdrawalRequest.addMessage(new WithdrawalMessage(
                    WithdrawalMessageType.VESTING_ENGINE_ALLOWS_VALUE_UPDATES));
        } // fi
    }

    /**
     * Returns true if a non-critical exception is found with warning.
     * 
     * <pre>
     * This definition means that:
     * 1. There are non-critical errors.
     * 2. There is at least 1 null money type percentage in employee vesting information.
     * </pre>
     * 
     * @param employeeVestingInformation
     * @param withdrawalRequest
     * @return
     */
    public boolean isVestingNonCriticalErrorWithWarning(
            final EmployeeVestingInformation employeeVestingInformation,
            final WithdrawalRequest withdrawalRequest) {
        boolean foundANull = false;
        // Now move the data into the withdrawal objects.
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {

            final String moneyTypeId = withdrawalRequestMoneyType.getMoneyTypeId();

            final MoneyTypeVestingPercentage moneyTypeVestingPercentage = (MoneyTypeVestingPercentage) employeeVestingInformation
                    .getMoneyTypeVestingPercentages().get(StringUtils.trim(moneyTypeId));

            if ((moneyTypeVestingPercentage == null)
                    || (moneyTypeVestingPercentage.getPercentage() == null)) {
                foundANull = true;
            }
        }
        return foundANull && CollectionUtils.isNotEmpty(employeeVestingInformation.getErrors());
    }

    /**
     * Gets the event date for the provided withdrawal request.
     * 
     * @param withdrawalRequest The request to get the event date for.
     * @return Date - The event date.
     */
    private Date getVestingEventDate(final WithdrawalRequest withdrawalRequest) {
        return withdrawalRequest.getVestingEventDate();
    }

    /**
     * Updates the request based on the fact that a critical vesting error has been detected.
     * 
     * @param withdrawalRequest The request to update.
     */
    private void handleVestingCriticalError(final WithdrawalRequest withdrawalRequest) {

        // Now set the vesting percentages to null, and let them be updatable.
        for (WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
                .getMoneyTypes()) {

            withdrawalRequestMoneyType.setVestingPercentage(null);
            withdrawalRequestMoneyType.setVestingPercentageUpdateable(Boolean.TRUE);
        } // end for
        withdrawalRequest.setVestingCriticalError(true);
        WithdrawalVestingMessageHelper.getInstance().addMessageForCriticalException(
                withdrawalRequest);
    }

    /**
     * Maps the WithdrawalReasonCode to an EmployeeWithdrawalReasonCode. If no matching reason is
     * found, null is returned.
     * 
     * @param reasonCode - From the Withdrawal perspective.
     * @return String - An employee withdrawal reason code.
     */
    public static String lookupEmployeeWithdrawalReasonCodeFromWithdrawalReasonCode(
            final String reasonCode) {

        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE)) {
            return EmployeeConstants.WithdrawalReason.TERMINATION;
        } // fi
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_DEATH_CODE)) {
            return EmployeeConstants.WithdrawalReason.DEATH;
        } // fi
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE)) {
            return EmployeeConstants.WithdrawalReason.DISABILITY;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE)) {
            return EmployeeConstants.WithdrawalReason.HARDSHIP;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE)) {
            return EmployeeConstants.WithdrawalReason.MANDATORY_DISTRIBUTION_TERM;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE)) {
            return EmployeeConstants.WithdrawalReason.MINIMUM_DISTRIBUTION;
        } // fi
        if (StringUtils.equals(reasonCode, WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE)) {
            return EmployeeConstants.WithdrawalReason.RETIREMENT;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE)) {
            return EmployeeConstants.WithdrawalReason.ROLLOVER;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE)) {
            return EmployeeConstants.WithdrawalReason.PRE_RETIREMENT;
        } // fi
        if (StringUtils.equals(reasonCode,
                WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE)) {
            return EmployeeConstants.WithdrawalReason.VOLUNTARY_CONTRIBUTIONS;
        } // fi

        return null;
    }
}
