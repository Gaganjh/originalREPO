package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

/**
 * invoked when sort by user name
 * 
 * @author Siby Thomas
 * 
 */
public class FeeScheduleChangeItemStndScheduleIndComparator extends
		FeeScheduleChangeItemComparator {

	public FeeScheduleChangeItemStndScheduleIndComparator() {
		super();
	}

	@Override
	public int compare(FeeScheduleChangeItem arg0, FeeScheduleChangeItem arg1) {

		String name0 = arg0.getStandardScheduleAppliedValue();
		String name1 = arg1.getStandardScheduleAppliedValue();

		int result = (isAscending() ? 1 : -1)
				* name0.compareToIgnoreCase(name1);

		if (result == 0) {
			result = arg1.getChangedDate().compareTo(arg0.getChangedDate());
		}
		
		if (result == 0) {
			result = arg0.getUserName().compareToIgnoreCase(arg1.getUserName());
		}

		if (result == 0) {
			result = arg0.getTypeSortOrder().compareTo(arg1.getTypeSortOrder());
		}

		if (result == 0) {
			result = arg0.getChangedType().compareToIgnoreCase(
					arg1.getChangedType());
		}
		
		if (result == 0) {
			result = arg0.getChangedValue().compareToIgnoreCase(
					arg1.getChangedValue());
		}

		return result;

	}
}