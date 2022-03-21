package com.manulife.pension.service.withdrawal.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * WithdrawalVestingMessageHelper is used to manage Vesting Messages (map them to withdrawals
 * messages).
 * 
 * @author glennpa
 */
public final class WithdrawalVestingMessageHelper {

    private static WithdrawalVestingMessageHelper instance;

    private Map<VestingMessageType, WithdrawalMessageType> messageMap;

    private static final Logger logger = Logger.getLogger(WithdrawalVestingMessageHelper.class);

    /**
     * MESSAGE_FOR_UNSPECIFIED_CODES holds the default message if no mapping is found for the
     * vesting code provided.
     */
    private static final WithdrawalMessageType MESSAGE_FOR_UNSPECIFIED_CODES = WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ALERT;

    /**
     * MESSAGE_FOR_CRITICAL_ERRORS holds the default message for critical errors.
     */
    private static final WithdrawalMessageType MESSAGE_FOR_CRITICAL_ERRORS = WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ERROR;

    /**
     * Default Constructor.
     */
    private WithdrawalVestingMessageHelper() {
        super();
        initialize();
    }

    /**
     * Initialize the object.
     */
    private void initialize() {
        messageMap = new HashMap<VestingMessageType, WithdrawalMessageType>();

        // Here is where all the mappings go.
        messageMap.put(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED,
                WithdrawalMessageType.VESTING_CREDITING_METHOD_IS_UNSPECIFIED);
        messageMap.put(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP,
                WithdrawalMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);
        messageMap
                .put(
                        VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED,
                        WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA);
        messageMap.put(VestingMessageType.HIRE_DATE_NOT_PROVIDED,
                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA);
        messageMap.put(VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED,
                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA);
        messageMap.put(VestingMessageType.MORE_RECENT_DATA_USED_FOR_CALCULATION,
                WithdrawalMessageType.VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE);
    }

    /**
     * Maps from the vesting message to a withdrawal message.
     * 
     * @param vestingMessageType The {@link VestingMessageType} which we looked up from the code.
     * @return {@link WithdrawalMessageType} - The withdrawal message to display.
     */
    public WithdrawalMessageType getWithdrawalMessageTypeFromVestingMessageType(
            final VestingMessageType vestingMessageType) {

        final WithdrawalMessageType withdrawalMessageType = messageMap.get(vestingMessageType);
        if (logger.isDebugEnabled()) {
            logger
                    .debug(new StringBuffer(
                            "getWithdrawalMessageTypeFromVestingMessageType> Converted vesting message type [")
                            .append(vestingMessageType).append("] to withdrawal message type [")
                            .append(withdrawalMessageType).append("].").toString());
        }

        if (withdrawalMessageType != null) {
            return withdrawalMessageType;
        } else {
            // No mapping was found. Use the default catch all message.
            return MESSAGE_FOR_UNSPECIFIED_CODES;
        } // fi
    }

    /**
     * Looks up the message type from the code. If it's not known, null is returned.
     * 
     * @param vestingMessageTypeCode The code to lookup the message with.
     * @return {@link VestingMessageType} - The type if a match is found, null otherwise.
     */
    public VestingMessageType getVestingMessageTypeFromCode(final int vestingMessageTypeCode) {
        switch (vestingMessageTypeCode) {
            case VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED_CODE:
                return VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED;

            case VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED_CODE:
                return VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED;

            case VestingMessageType.HIRE_DATE_NOT_PROVIDED_CODE:
                return VestingMessageType.HIRE_DATE_NOT_PROVIDED;

            case VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED_CODE:
                return VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED;

            case VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP_CODE:
                return VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP;

            default:
                return null;
        } // end switch
    }

    /**
     * Adds the vesting errors to the {@link WithdrawalRequest} object.
     * 
     * @param withdrawalRequest The object to add the messages to.
     * @param employeeVestingInformation The object holding the vesting errors.
     */
    public void addVestingErrors(final WithdrawalRequest withdrawalRequest,
            final EmployeeVestingInformation employeeVestingInformation) {

        // These values may be null if there was some kind of critical error.
        if (employeeVestingInformation == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("employeeVestingInformation was null.");
            } // fi
            return;
        } // fi

        final Set vestingErrorCodes = employeeVestingInformation.getErrors();

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("addVestingErrors> Adding [").append(
                    (CollectionUtils.isEmpty(vestingErrorCodes) ? 0 : CollectionUtils
                            .size(vestingErrorCodes)))
                    .append("] vesting errors to request object.").toString());
        }
        if (CollectionUtils.isNotEmpty(vestingErrorCodes)) {

            for (final Iterator iterator = vestingErrorCodes.iterator(); iterator.hasNext();) {
                final VestingMessageType vestingMessageType = (VestingMessageType) iterator.next();

                final WithdrawalMessageType withdrawalMessageType = getWithdrawalMessageTypeFromVestingMessageType(vestingMessageType);

                // Only add the message if it doesn't already exist (prevent dupes)
                if (!CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(withdrawalMessageType))) {
                    final WithdrawalMessage withdrawalMessage = new WithdrawalMessage(
                            withdrawalMessageType);

                    // Add the vesting message code as a parameter to the message.
                    final Collection parameters = new ArrayList(1);
                    parameters.add(vestingMessageType.getTypeCode());
                    withdrawalMessage.setParameters(parameters);

                    withdrawalRequest.addMessage(withdrawalMessage);
                }
            } // end for

            // Mark vesting could not be calculated indicator
            logger.debug("addVestingErrors> Marking vesting could not be calculated indicator.");
            withdrawalRequest.setVestingCouldNotBeCalculatedInd(true);
        } // fi
    }

    /**
     * Adds the message for critical vesting errors to the withdrawal request.
     * 
     * @param withdrawalRequest The request to add the message to.
     */
    public void addMessageForCriticalException(final WithdrawalRequest withdrawalRequest) {
        withdrawalRequest.addMessage(new WithdrawalMessage(MESSAGE_FOR_CRITICAL_ERRORS));
    }

    /**
     * Gets all possible values for withdrawal message types.
     * 
     * @return {@link Collection} of {@link WithdrawalMessageType} objects.
     */
    public Collection<WithdrawalMessageType> getAllWithdrawalMessageTypes() {
        final Collection<WithdrawalMessageType> types = new ArrayList<WithdrawalMessageType>();
        types.addAll(messageMap.values());
        types.add(MESSAGE_FOR_UNSPECIFIED_CODES);
        types.add(MESSAGE_FOR_CRITICAL_ERRORS);
        types.add(WithdrawalMessageType.VESTING_ENGINE_ALLOWS_VALUE_UPDATES);
        return types;
    }

    /**
     * Gets the instance of this class. Singleton pattern.
     * 
     * @return WithdrawalVestingMessageHelper The instance of this class.
     */
    public static WithdrawalVestingMessageHelper getInstance() {
        if (instance == null) {
            instance = new WithdrawalVestingMessageHelper();
        } // fi
        return instance;
    }
}
