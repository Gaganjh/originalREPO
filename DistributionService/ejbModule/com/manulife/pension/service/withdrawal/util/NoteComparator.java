package com.manulife.pension.service.withdrawal.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Comparator;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;

/**
 * NoteComparator compares the notes based off of the {@link Timestamp} they were created on.
 * 
 * @author glennpa
 */
public class NoteComparator implements Comparator<WithdrawalRequestNote>, Serializable {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Orders the notes according to their dates.
     * 
     * @param first The first object to compare.
     * @param second The second object to compare.
     * @return int Zero if the objects are equal, negative if the first sorts before the second,
     *         positive otherwise.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final WithdrawalRequestNote first, final WithdrawalRequestNote second) {

        final Timestamp firstTimestamp = first.getCreated();
        final Timestamp secondTimestamp = second.getCreated();

        if (firstTimestamp == null) {
            if (secondTimestamp == null) {
                return 0;
            } else {
                return -1;
            } // fi
        } // fi
        if (secondTimestamp == null) {
            return 1;
        } // fi

        return firstTimestamp.compareTo(secondTimestamp);
    }

}
