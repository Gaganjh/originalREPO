package com.manulife.pension.ps.service.report.feeSchedule.util;

import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;

/**
 * 
 * @author Siby Thomas
 *
 */
abstract public class FeeScheduleChangeItemComparator implements
		Comparator<FeeScheduleChangeItem> {

	protected static final Date MIN_DATE = new GregorianCalendar(1, 0, 1)
			.getTime();
	protected static final Integer MIN_INTEGER = new Integer(Integer.MIN_VALUE);
	protected static final String EMPTY_STRING = "";

	protected boolean ascending = true;

	public FeeScheduleChangeItemComparator() {
		this.ascending = true;
	}

	public FeeScheduleChangeItemComparator(boolean ascending) {
		this.ascending = ascending;
	}

	abstract public int compare(FeeScheduleChangeItem arg0,
			FeeScheduleChangeItem arg1);

	/**
	 * @return Returns the ascending.
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending
	 *            The ascending to set.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	protected int compareItemValue(FeeScheduleChangeItem arg1,
			FeeScheduleChangeItem arg2) {

		Double item1 = getValue(arg1);
		Double item2 = getValue(arg2);

		if (item1 == null & item2 == null) {
			return arg1.getChangedValue().compareToIgnoreCase(
					arg2.getChangedValue());
		}

		if (item1 == null & item2 != null) {
			return -1;
		}

		if (item1 != null & item2 == null) {
			return 1;
		}

		if (item1 != null & item2 != null) {
			return item1.compareTo(item2);
		}

		return 0;
	}

	private Double getValue(FeeScheduleChangeItem arg) {
		String valueType = arg.getChangedValueType();
		if (StringUtils.isEmpty(valueType)) {
			return null;
		}
		try {
			return Double.parseDouble(arg.getChangedValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
