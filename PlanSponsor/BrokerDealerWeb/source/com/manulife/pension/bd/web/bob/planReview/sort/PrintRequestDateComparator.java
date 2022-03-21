package com.manulife.pension.bd.web.bob.planReview.sort;

import java.sql.Timestamp;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author Ashok
 * 
 */

 public class PrintRequestDateComparator extends
BaseHistoryDetailsComparator {

	public PrintRequestDateComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PrintDocumentPackgeVo arg0, PrintDocumentPackgeVo arg1) {

		Timestamp createdTs0 = arg0.getCreatedTs();
		Timestamp createdTs1 = arg1.getCreatedTs();

		int multiplier = isAscending() ? 1 : -1;
        
		int result = multiplier * createdTs0.compareTo(createdTs1);
		
		if  (result==0){
			result = super.compare(arg0, arg1);
		}

		return result;
	}

}
