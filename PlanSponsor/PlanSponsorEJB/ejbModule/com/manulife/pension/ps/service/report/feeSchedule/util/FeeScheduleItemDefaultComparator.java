package com.manulife.pension.ps.service.report.feeSchedule.util;

import java.util.Date;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

public class FeeScheduleItemDefaultComparator extends
		FeeScheduleChangeItemComparator {

	public FeeScheduleItemDefaultComparator() {
		super();
	}

	@Override
	public int compare(FeeScheduleChangeItem arg0, FeeScheduleChangeItem arg1) {

		Date date0 = arg0.getChangedDate();
		Date date1 = arg1.getChangedDate();

		int multiplier = isAscending() ? 1 : -1;

		int result = multiplier * date0.compareTo(date1);

		if (result == 0) {
			result = arg0.getTypeSortOrder().compareTo(arg1.getTypeSortOrder());
		}

		if (result == 0) {
			result = arg0.getChangedType().compareToIgnoreCase(
					arg1.getChangedType());
		}

		if (result == 0) {
			result = arg0.getUserName().compareToIgnoreCase(arg1.getUserName());
		}

		if (result == 0) {
			result = compareItemValue(arg0, arg1);
		}

		return result;
	}
}