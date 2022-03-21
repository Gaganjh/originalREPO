package com.manulife.pension.bd.web.bob.planReview.sort;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;


/**
 * This is base Comparator class used for Plan Review report pages.
 * 
 * @author Ashok
 * 
 */

 public class PrintConfirmationNumberComparator extends
BaseHistoryDetailsComparator {

	public PrintConfirmationNumberComparator(String sortDirection) {
		super(sortDirection);
	}

	@Override
	public int compare(PrintDocumentPackgeVo arg0, PrintDocumentPackgeVo arg1) {

		int result = super.compare(arg0, arg1);
				
		return result;
	}

}
