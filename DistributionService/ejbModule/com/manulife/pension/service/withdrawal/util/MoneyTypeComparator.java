package com.manulife.pension.service.withdrawal.util;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * MoneyTypeComparator is used to sort {@link WithdrawalRequestMoneyType} objects.
 * 
 * @author glennpa
 */
public class MoneyTypeComparator implements Comparator<WithdrawalRequestMoneyType>, Serializable {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Compare money types by money type code, then by money type name. Use the custom short name if
     * available, otherwise use the Manulife short name.
     * 
     * @param firstMoneyType The first object to compare.
     * @param secondMoneyType The second object to compare.
     * @return int Zero if the objects are equal, negative if the first sorts before the second,
     *         positive otherwise.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final WithdrawalRequestMoneyType firstMoneyType,
            final WithdrawalRequestMoneyType secondMoneyType) {

        final String firstCategoryCode = firstMoneyType.getMoneyTypeCategoryCode();
        final String secondCategoryCode = secondMoneyType.getMoneyTypeCategoryCode();

        if ((firstCategoryCode == null) && (secondCategoryCode != null)) {
            return -1;
        } // fi
        if ((firstCategoryCode != null) && (secondCategoryCode == null)) {
            return 1;
        } // fi

        if ((firstCategoryCode != null) && (secondCategoryCode != null)) {
            final int primaryCompareResult = firstCategoryCode.compareTo(secondCategoryCode);

            if (primaryCompareResult != 0) {
                return primaryCompareResult;
            } // fi
        } // fi

        final String firstId = StringUtils.defaultString(firstMoneyType.getMoneyTypeAliasId(),
                firstMoneyType.getMoneyTypeId());
        final String secondId = StringUtils.defaultString(secondMoneyType.getMoneyTypeAliasId(),
                secondMoneyType.getMoneyTypeId());

        if (firstId == null) {
            if (secondId == null) {
                return 0;
            } else {
                return -1;
            } // fi
        } // fi
        if (secondId == null) {
            return 1;
        } // fi

        return firstId.compareTo(secondId);
    }

}
