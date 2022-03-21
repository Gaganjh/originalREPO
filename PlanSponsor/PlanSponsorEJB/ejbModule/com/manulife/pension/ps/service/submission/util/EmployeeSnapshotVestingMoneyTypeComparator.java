package com.manulife.pension.ps.service.submission.util;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;

/**
 * EmployeeSnapshotVestingMoneyTypeComparator provides a {@link Comparator} that sorts based off of
 * the general vesting order, but also puts fully vested money types at the top of the list.
 * 
 * @author glennpa
 */
public class EmployeeSnapshotVestingMoneyTypeComparator<T extends MoneyTypeVO> extends
        VestingMoneyTypeComparator {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private static EmployeeSnapshotVestingMoneyTypeComparator instance;

    /**
     * Default Constructor.
     */
    private EmployeeSnapshotVestingMoneyTypeComparator() {
    }

    /**
     * Gets an instance of this {@link EmployeeSnapshotVestingMoneyTypeComparator}.
     * 
     * @return EmployeeSnapshotVestingMoneyTypeComparator An instance of this {@link Comparator}.
     */
    public static EmployeeSnapshotVestingMoneyTypeComparator getInstance() {
        if (instance == null) {
            instance = new EmployeeSnapshotVestingMoneyTypeComparator();
        } // fi
        return instance;
    }

    /**
     * Changes the behaviour of the {@link VestingMoneyTypeComparator} to sort fully vested money
     * types to the top of the list.
     * 
     * @see com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator#compare(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public int compare(final Object left, final Object right) {

        if ((left == null) || (right == null)) {
            throw new ClassCastException("Cannot compare null values.");
        } // fi

        MoneyTypeVO leftMoneyTypeVO;
        MoneyTypeVO rightMoneyTypeVO;
        if (left instanceof MoneyTypeVO) {
            leftMoneyTypeVO = (MoneyTypeVO) left;
        } else {
            throw new ClassCastException("The left argument is not an instance of MoneyTypeVO.");
        } // fi

        if (right instanceof MoneyTypeVO) {
            rightMoneyTypeVO = (MoneyTypeVO) right;
        } else {
            throw new ClassCastException("The right argument is not an instance of MoneyTypeVO.");
        } // fi

        final CompareToBuilder compareToBuilder = new CompareToBuilder().append(
                isNotFullyVested(leftMoneyTypeVO), isNotFullyVested(rightMoneyTypeVO)).append(
                leftMoneyTypeVO, rightMoneyTypeVO, new VestingMoneyTypeComparator());

        return compareToBuilder.toComparison();
    }

    /**
     * Determines if the given {@link MoneyTypeVO} is fully vested or not.
     * 
     * @param moneyTypeVO The {@link MoneyTypeVO} to test.
     * @return boolean - True if it's fully vested, false otherwise.
     */
    private boolean isNotFullyVested(final MoneyTypeVO moneyTypeVO) {
        return (!(StringUtils.equals(MoneyTypeVO.FULLY_VESTED_VALUE_YES, moneyTypeVO
                .getFullyVested())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        return super.equals(object);
    }
}
