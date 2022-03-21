package com.manulife.pension.ps.service.report.feeSchedule.util;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

/**
 * 
 * invoked when sort by type is clicked
 * 
 * @author Siby Thomas
 *
 */
public class FeeScheduleChangeItemTypeComparator extends FeeScheduleChangeItemComparator {
	
	public FeeScheduleChangeItemTypeComparator() {
		super();
	}

	public FeeScheduleChangeItemTypeComparator(boolean ascending) {
		super(ascending);
	}


	@Override
	public int compare(FeeScheduleChangeItem arg0, FeeScheduleChangeItem arg1) {

		Integer name0 = arg0.getTypeSortOrder();
		Integer name1 = arg1.getTypeSortOrder();

		int result = (isAscending() ? 1 : -1) * name0.compareTo(name1);

		if (result == 0) {
			result = (isAscending() ? 1 : -1)
					* arg0.getChangedType().compareToIgnoreCase(arg1.getChangedType());
		}

		if (result == 0) {
			result = arg1.getChangedDate().compareTo(arg0.getChangedDate());
		}

		if (result == 0) {
			result = arg0.getUserName().compareToIgnoreCase(arg1.getUserName());
		}

		return result;
	}
}