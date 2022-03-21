/*
 * WithdrawalStateFactory.java,v 1.1 2006/09/25 19:15:28 Paul_Glenn Exp
 * WithdrawalStateFactory.java,v
 * Revision 1.1  2006/09/25 19:15:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * WithdrawalStateFactory creates {@link WithdrawalState} objects and keeps a pool of instances.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public final class WithdrawalStateFactory {

    private static final Map<Class, WithdrawalState> STATE_INSTANCES = new HashMap<Class, WithdrawalState>();

    private static final Map<String, WithdrawalStateEnum> DATA_FIELD_STATE_LOOKUP = new HashMap<String, WithdrawalStateEnum>();

    private static final Map<WithdrawalStateEnum, Class> ENUM_STATE_LOOKUP = new HashMap<WithdrawalStateEnum, Class>();

    static {

        // Load up the hashmaps with data.
        for (final WithdrawalStateEnum withdrawalStateEnum : WithdrawalStateEnum.values()) {
            DATA_FIELD_STATE_LOOKUP.put(withdrawalStateEnum.getStatusCode(), withdrawalStateEnum);

            ENUM_STATE_LOOKUP.put(withdrawalStateEnum, withdrawalStateEnum.getStateClass());
        } // end for

    } // end static

    /**
     * Default Constructor.
     */
    private WithdrawalStateFactory() {
    }

    /**
     * Sets the {@link WithdrawalState} for the withdrawal based on the status code in it's value
     * object.
     * 
     * @param withdrawal The data to find the state object for.
     */
    public static void updateStateFromStatusCode(final Withdrawal withdrawal) {

        // Lookup the data field in the list of supported data field states.
        final WithdrawalStateEnum withdrawalStateEnum = DATA_FIELD_STATE_LOOKUP.get(withdrawal
                .getWithdrawalRequest().getStatusCode());

        if (withdrawalStateEnum != null) {
            withdrawal.setWithdrawalState(getState(withdrawalStateEnum));
        } else {
            // If the state isn't in our map of states, then we want to return an error.
            throw new NotImplementedException(new StringBuffer(
                    "No state defined for the status code of [").append(
                    withdrawal.getWithdrawalRequest().getStatusCode()).append("]").toString());
        } // fi

    }

    /**
     * Gets a {@link WithdrawalState} for the enum.
     * 
     * @param withdrawalStateEnum The enum to find the state for.
     * @return WithdrawalState The state for the given enum.
     */
    public static WithdrawalState getState(final WithdrawalStateEnum withdrawalStateEnum) {

        // Lookup the enum in the list of supported enum states.
        final Class clazz = (Class) ENUM_STATE_LOOKUP.get(withdrawalStateEnum);
        if (clazz != null) {
            return getState(clazz);
        } // fi

        // If the state isn't in our map of classes (states), then we want to return an error.
        throw new NotImplementedException(new StringBuffer(
                "No state defined for the enum value of [").append(withdrawalStateEnum.toString())
                .append("]").toString());
    }

    /**
     * Gets a state object for the given Class.
     * 
     * @param clazz The Class to get a state for.
     * @return WithdrawalState The state for the given class.
     */
    private static WithdrawalState getState(final Class clazz) {
        final WithdrawalState state;
        final Object obj = STATE_INSTANCES.get(clazz);
        if (obj == null) {
            try {
                state = (WithdrawalState) clazz.newInstance();
            } catch (InstantiationException instantiationException) {
                throw new NestableRuntimeException(instantiationException);
            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            }
            STATE_INSTANCES.put(clazz, state);
        } else {
            state = (WithdrawalState) obj;
        } // fi
        return state;
    }

}
