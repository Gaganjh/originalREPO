package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

/**
 * invoked when sort by value is clicked
 * 
 * @author Siby Thomas
 * 
 */
public class FeeScheduleChangeItemValueComparator extends
		FeeScheduleChangeItemComparator {

	public FeeScheduleChangeItemValueComparator() {
		super();
	}

	@Override
	public int compare(FeeScheduleChangeItem arg0, FeeScheduleChangeItem arg1) {

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier * compareItemValue(arg0, arg1);

		if (result == 0) {
			result = arg1.getChangedDate().compareTo(arg0.getChangedDate());
		}

		if (result == 0) {
			result = arg0.getTypeSortOrder().compareTo(arg1.getTypeSortOrder());
		}

		if (result == 0) {
			result = arg0.getChangedType().compareToIgnoreCase(
					arg1.getChangedType());
		}

		return result;
	}
}