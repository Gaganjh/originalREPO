package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

/**
 * invoked when sort by user name
 * 
 * @author Siby Thomas
 * 
 */
public class FeeScheduleChangeItemUserNameComparator extends
		FeeScheduleChangeItemComparator {

	public FeeScheduleChangeItemUserNameComparator() {
		super();
	}

	@Override
	public int compare(FeeScheduleChangeItem arg0, FeeScheduleChangeItem arg1) {

		String name0 = arg0.getUserName();
		String name1 = arg1.getUserName();

		int result = (isAscending() ? 1 : -1)
				* name0.compareToIgnoreCase(name1);

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