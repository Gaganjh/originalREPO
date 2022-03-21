package com.manulife.pension.bd.web.bob.planReview.sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Report MonthEnd Date used for Plan Review report
 * pages.
 * 
 * @author NagaRaju
 * 
 */

public class MonthEndDateComparator extends BaseContractReviewReportComparator {

	public MonthEndDateComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0,
			PlanReviewReportUIHolder arg1) {

		Date date1 = null;
		Date date2 = null;
		
		int multiplier = isAscending() ? 1 : -1;

		String monthEndDate0 = arg0.getSelectedReportMonthEndDate();
		String monthEndDate1 = arg1.getSelectedReportMonthEndDate();

		if (monthEndDate0 == null && monthEndDate1 == null) {
			return super.compare(arg0, arg1);
		} else if (monthEndDate0 == null) {
			return -1 * multiplier;
		} else if (monthEndDate1 == null) {
			return 1 * multiplier;
		}

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			date1 = formatter.parse(monthEndDate0);

			date2 = formatter.parse(monthEndDate1);

		} catch (ParseException exception) {
			throw new IllegalArgumentException(
					"Exception while Parsing ReportMonthEnd dates: "
							+ monthEndDate0 + " & " + monthEndDate1, exception);
		}

		int result = multiplier * date1.compareTo(date2);
		
		if (result == 0) {
			result = super.compare(arg0, arg1);
		}
		
		return result;
	}

}
