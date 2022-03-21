package com.manulife.pension.service.loan.valueobject;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * MoneyTypeComparator is used to sort {@link LoanMoneyType} objects.
 * 
 * @author matyste
 */
public class MoneyTypeComparator implements Comparator<LoanMoneyType>, Serializable {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Compare money types by money type category code, then by money type
     * name. Use the custom contract short name if available, otherwise
     * use the Manulife short name.
     * 
     * @param firstMoneyType The first object to compare.
     * @param secondMoneyType The second object to compare.
     * @return int Zero if the objects are equal, negative if the first sorts before the second,
     *         positive otherwise.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final LoanMoneyType firstMoneyType,
            final LoanMoneyType secondMoneyType) {

        final String firstCategoryCode = firstMoneyType.getMoneyTypeCategoryCode();
        final String secondCategoryCode = secondMoneyType.getMoneyTypeCategoryCode();

        if ((firstCategoryCode == null) && (secondCategoryCode != null)) {
            return -1;
        }
        if ((firstCategoryCode != null) && (secondCategoryCode == null)) {
            return 1;
        }

        if ((firstCategoryCode != null) && (secondCategoryCode != null)) {
            final int primaryCompareResult = firstCategoryCode.compareTo(secondCategoryCode);

            if (primaryCompareResult != 0) {
                return primaryCompareResult;
            }
        }

        final String firstId = StringUtils.defaultString(firstMoneyType.getContractMoneyTypeShortName(),
                firstMoneyType.getMoneyTypeId());
        final String secondId = StringUtils.defaultString(secondMoneyType.getContractMoneyTypeShortName(),
                secondMoneyType.getMoneyTypeId());

        if (firstId == null) {
            if (secondId == null) {
                return 0;
            } else {
                return -1;
            }
        }
        if (secondId == null) {
            return 1;
        }

        return firstId.compareTo(secondId);
    }

}
