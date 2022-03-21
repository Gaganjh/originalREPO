package com.manulife.pension.service.withdrawal.helper;

import java.io.Serializable;
import java.util.Comparator;

import com.manulife.pension.service.withdrawal.valueobject.Activity;

/**
 * @author Dennis
 * 
 */
public class ActivityComparator implements Comparator<Activity>, Serializable {

    /**
     * {@inheritDoc}
     */
    public int compare(final Activity o1, final Activity o2) {
        return o1.getSortOrder() - o2.getSortOrder();
    }

}
