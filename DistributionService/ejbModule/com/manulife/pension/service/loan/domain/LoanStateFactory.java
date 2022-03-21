/*
 * LoanStateFactory.java,v 1.1 2006/09/25 19:15:28 Paul_Glenn Exp
 * LoanStateFactory.java,v
 * Revision 1.1  2006/09/25 19:15:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.loan.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.service.loan.valueobject.Loan;



/**
 * LoanStateFactory creates {@link LoanState} objects and keeps a pool of instances.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public final class LoanStateFactory {

    private static final Map<Class, LoanState> STATE_INSTANCES = new HashMap<Class, LoanState>();

    private static final Map<String, LoanStateEnum> DATA_FIELD_STATE_LOOKUP = new HashMap<String, LoanStateEnum>();

    private static final Map<LoanStateEnum, Class> ENUM_STATE_LOOKUP = new HashMap<LoanStateEnum, Class>();

    static {

        // Load up the hashmaps with data.
        for (final LoanStateEnum loanStateEnum : LoanStateEnum.values()) {
            DATA_FIELD_STATE_LOOKUP.put(loanStateEnum.getStatusCode(), loanStateEnum);
            ENUM_STATE_LOOKUP.put(loanStateEnum, loanStateEnum.getStateClass());
        } // end for

    } // end static

    /**
     * Default Constructor.
     */
    private LoanStateFactory() {
    }

    /**
     * Gets a {@link LoanState} for the enum.
     * 
     * @param loanStateEnum The enum to find the state for.
     * @return LoanState The state for the given enum.
     */
    public static LoanState getState(final LoanStateEnum loanStateEnum) {

        // Lookup the enum in the list of supported enum states.
        final Class clazz = (Class) ENUM_STATE_LOOKUP.get(loanStateEnum);
        if (clazz != null) {
            return getState(clazz);
        } // fi

        // If the state isn't in our map of classes (states), then we want to return an error.
        throw new NotImplementedException(new StringBuffer(
                "No state defined for the enum value of [").append(loanStateEnum.toString())
                .append("]").toString());
    }

    /**
     * Gets a state object for the given Class.
     * 
     * @param clazz The Class to get a state for.
     * @return LoanState The state for the given class.
     */
    private static LoanState getState(final Class clazz) {
        final LoanState state;
        final Object obj = STATE_INSTANCES.get(clazz);
        if (obj == null) {
            try {
                state = (LoanState) clazz.newInstance();
            } catch (InstantiationException instantiationException) {
                throw new NestableRuntimeException(instantiationException);
            } catch (IllegalAccessException illegalAccessException) {
                throw new NestableRuntimeException(illegalAccessException);
            }
            STATE_INSTANCES.put(clazz, state);
        } else {
            state = (LoanState) obj;
        } // fi
        return state;
    }

	public static LoanState getState(Loan inputLoan) {
		return getState(DATA_FIELD_STATE_LOOKUP.get(inputLoan.getStatus()));
	}

}
