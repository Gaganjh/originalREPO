package com.manulife.pension.service.withdrawal.helper;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.Activity;

/**
 * @author Dennis
 * 
 */
public class MoneyTypeComparator implements Comparator<Activity>, Serializable {

    /**
     * {@inheritDoc}
     */
    public int compare(final Activity o1, final Activity o2) {
        if (StringUtils.equals(o1.getSecondaryName(), o2.getSecondaryName())) {
            // they want vesting to appear before amount
            // so thats why i do o2.compare(o1) instead of the other way around
            return o2.getItemName().compareTo(o1.getItemName());
        } else {
            return o1.getSecondaryName().compareTo(o2.getSecondaryName());
        }
    }
}
