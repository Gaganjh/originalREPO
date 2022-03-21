package com.manulife.pension.ps.web.tools.util;

import java.util.Date;

import com.manulife.pension.lp.model.gft.GFTUploadHistorySummary;
import com.manulife.pension.model.common.Money;
import com.manulife.pension.ps.web.util.sort.SortTool;

/**
 * Insert the type's description here.
 * Creation date: (9/17/2002 4:20:02 PM)
 * @author: Steven Wang (Intelliware)
 */
public class UploadHistorySortTool implements SortTool{

	public static final int SORT_BY_TRACKING_NUMBER = 0;
	public static final int SORT_BY_FILE_TYPE = 1;
	public static final int SORT_BY_LAST_RECEIVED = 2;
	public static final int SORT_BY_PMT_EFFECTIVE_DATE = 3;
	public static final int SORT_BY_PMT_TOTAL = 4;
	private final int sortFlag;

	public UploadHistorySortTool(int sortFlag) {
		if (sortFlag > UploadHistorySortTool.SORT_BY_PMT_TOTAL || sortFlag < UploadHistorySortTool.SORT_BY_TRACKING_NUMBER) {
			throw new IllegalArgumentException("Wrong sort flag value = "+sortFlag);
		}
	
		this.sortFlag = sortFlag;
	}

	/**
	 * compare method comment.
	 */
	public int compare(Object first, Object second) {
		int result = 0;
		if (first == null && second == null) {
			result = 0;
		} else if (first == null && second != null) {
			result = -1;
		} else if (first != null && second == null) {
			result = 1;
		} else if (this.sortFlag == UploadHistorySortTool.SORT_BY_TRACKING_NUMBER) {
			result = compareTrackingNumber(first, second);
		} else if (this.sortFlag == UploadHistorySortTool.SORT_BY_FILE_TYPE) {
			result = compareFileType(first, second);
		} else if (this.sortFlag == UploadHistorySortTool.SORT_BY_LAST_RECEIVED) {
			result = compareLastReceivedDate(first, second);
		} else if (this.sortFlag == UploadHistorySortTool.SORT_BY_PMT_EFFECTIVE_DATE) {
			result = comparePaymentEffectiveDate(first, second);
		} else if (this.sortFlag == UploadHistorySortTool.SORT_BY_PMT_TOTAL) {
			result = comparePaymentTotal(first, second);
		} else {
			throw new IllegalStateException("The internal sortFlag ["+this.sortFlag+"] is not valid");	//don't know how to compare
		}
	
		if (result == 0) {
			return SortTool.IS_EQUAL_TO;
		} else if (result > 0) {
			return SortTool.IS_GREATER_THAN;
		} else {
			return SortTool.IS_LESS_THAN;
		}
		
	}

	/**
	 * compare method comment.
	 */
	private int compareDate(Date firstDate, Date secondDate) {
	
		long first = firstDate==null? 0 : firstDate.getTime();
		long second = secondDate==null? 0 : secondDate.getTime();
		long dif = first - second;
		if (dif == 0) {
			return 0;
		} else if (dif < 0) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * FIXME: assume the contract number also can not be null -- need confirm.
	 */
	private int compareFileType(Object first, Object second) {
		int result = 0;
		if ((((GFTUploadHistorySummary)first).getFileType()) == null && (((GFTUploadHistorySummary)second).getFileType() == null)) {
			result = 0;
		} else if ((((GFTUploadHistorySummary)first).getFileType()) == null && (((GFTUploadHistorySummary)second).getFileType() != null)) {
			result = -1;
		} else if ((((GFTUploadHistorySummary)first).getFileType()) != null && (((GFTUploadHistorySummary)second).getFileType() == null)) {
			result = 1;
		}  else {
			result = SubmissionTypeTranslater.translate(((GFTUploadHistorySummary)first).getFileType()).compareTo( SubmissionTypeTranslater.translate(((GFTUploadHistorySummary)second).getFileType()));
		}
		return result;
	}

	private int compareInteger(int first, int second) {

		if (first == 0 && second == 0)  {
			return 0;
		} else if (first == 0 && second != 0) {
			return -1;
		} else if (first != 0 && second == 0) {
			return 1;
		} else {
			int result = (first - second);
			if (result == 0) {
				return 0;
			} else if (result > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	/**
	 * compare method comment.
	 */
	private int compareLastReceivedDate(Object first, Object second) {
		return compareDate(((GFTUploadHistorySummary)first).getReceivedDate(), ((GFTUploadHistorySummary)second).getReceivedDate());
	}

	private int compareMoney(Object first, Object second) {

		if (first == null && second == null)  {
			return 0;
		} else if (first == null && second != null) {
			return -1;
		} else if (first != null && second == null) {
			return 1;
		} else {
			Money result = ((Money)first).subtract((Money)second);
			if (result.isZero()) {
				return 0;
			} else if (result.isPositive()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	private int comparePaymentEffectiveDate(Object first, Object second) {
		return compareDate(((GFTUploadHistorySummary)first).getPaymentEffectiveDate(), ((GFTUploadHistorySummary)second).getPaymentEffectiveDate());
	}

	private int comparePaymentTotal(Object first, Object second) {
		return compareMoney(((GFTUploadHistorySummary)first).getPaymentTotalAmount(), ((GFTUploadHistorySummary)second).getPaymentTotalAmount());
	}

	/**
	 * FIXME: assume the contract number also can not be null -- need confirm.
	 */
	private int compareTrackingNumber(Object first, Object second) {
		return compareInteger(((GFTUploadHistorySummary)first).getSubmissionIdInteger(), ((GFTUploadHistorySummary)second).getSubmissionIdInteger());
	}
}
