package com.manulife.pension.bd.web.bob.planReview.sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;

/**
 * This is Comparator class for Report MonthEnd Date used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public class PrintMonthAndDateComparator extends BaseContractReviewReportComparator{



	public PrintMonthAndDateComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PlanReviewReportUIHolder arg0,
			PlanReviewReportUIHolder arg1) {
		Date date1 =null;
		Date date2 =null;
		String rptmntenddt0 = arg0.getSelectedReportMonthEndDate();
		String rptmntenddt1 = arg1.getSelectedReportMonthEndDate();	
		Integer contractNbr0 = arg0.getContractNumber();
		Integer contractNbr1 = arg1.getContractNumber();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			 date1 = formatter.parse(rptmntenddt0);
			
			 date2= formatter.parse(rptmntenddt1);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Exception while Parsing ReportMonthEnd dates Date1:"
					+rptmntenddt0+", Date2:"+rptmntenddt1, e);
		}	

		int multiplier = isAscending() ? 1 : -1;
		
		int result = multiplier
				* date1.compareTo(date2);
		if  (result==0){
				result =  contractNbr0.compareTo(contractNbr1);
		}
		return result;
	}





}
