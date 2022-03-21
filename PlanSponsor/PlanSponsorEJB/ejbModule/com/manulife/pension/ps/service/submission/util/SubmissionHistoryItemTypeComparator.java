package com.manulife.pension.ps.service.submission.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;

/**
 * Comparator implementation by type where the secondary sort by tracing (submission) number descending always
 * 
 * @author Tony Tomasone
 */
public class SubmissionHistoryItemTypeComparator extends SubmissionHistoryItemComparator {

	private static final Integer MIN_PRIORITY = new Integer(0);
	private static final String SUBMISSION_TYPE_CONVERSION = "Z";
	private static final String SUBMISSION_TYPE_COMBO_CONTRIBUTION = "CC";
	private static final String SUBMISSION_TYPE_COMBO_CENSUS = "CE";
	private static final String SUBMISSION_TYPE_LTPT = "Q";
	
	private static Map priority = new HashMap();
	static {
		//priority.put(GFTUploadDetail.SUBMISSION_TYPE_ADDRESS, new Integer(1)); 		// "Address"
		priority.put(GFTUploadDetail.SUBMISSION_TYPE_CENSUS, new Integer(2)); 			// "Census"
		priority.put(SUBMISSION_TYPE_COMBO_CENSUS, new Integer(3)); 			        // "Census #"
		priority.put(MoneySourceDescription.CONTRIBUTION, new Integer(4));				// "Contribution"
		priority.put(SUBMISSION_TYPE_CONVERSION, new Integer(5)); 						// "Conversion"
		priority.put(MoneySourceDescription.FORFEITURE_CONTRIBUTION, new Integer(6));	// "Forfeiture Contribution"
		priority.put(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT, new Integer(7)); 			// "Payment"
		priority.put(MoneySourceDescription.REGULAR_CONTRIBUTION, new Integer(8)); 		// "Regular Contribution"
		priority.put(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR, new Integer(8)); 	// "Regular Contribution"
		priority.put(SUBMISSION_TYPE_COMBO_CONTRIBUTION, new Integer(9));				// "Regular Contribution #"
		priority.put(MoneySourceDescription.REINSTATEMENT_CONTRIBUTION, new Integer(10));// "Reinstatement of Contribution"
		priority.put(GFTUploadDetail.SUBMISSION_TYPE_MISCELLANEOUS, new Integer(11)); 	// "Sample"
		priority.put(GFTUploadDetail.SUBMISSION_TYPE_TEST, new Integer(12));			// "Test"
		priority.put(MoneySourceDescription.TRANSFER_CONTRIBUTION, new Integer(13)); 	// "Transfer Contribution"
		//CL113495 fix - submissionType sort order - start
        priority.put(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER, new Integer(13)); // "Transfer Contribution"
		//CL113495 fix - submissionType sort order - end
        priority.put(GFTUploadDetail.SUBMISSION_TYPE_VESTING, new Integer(14)); 			// "Vesting"
        priority.put(SUBMISSION_TYPE_LTPT, new Integer(15));								// "LTPT"
	}

	public SubmissionHistoryItemTypeComparator() {
		super();
	}

	public int compare(Object arg0, Object arg1) {

		String type0 = null;
		String type1 = null;

		SubmissionHistoryItem item0 = (SubmissionHistoryItem)arg0;
		SubmissionHistoryItem item1 = (SubmissionHistoryItem)arg1;
		
		if (arg0 != null) {
			
			type0 = item0.getType();	
			
			if(isComboFile(item0.getComboFileInd())) {
				if(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(type0) || 
						GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(type0)) {
					type0 = SUBMISSION_TYPE_COMBO_CONTRIBUTION;
				} else {
					type0 = SUBMISSION_TYPE_COMBO_CENSUS;
				}
			} else if (GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(type0) || GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(type0)) {
				if ( item0.getMoneySource() != null ) {
					type0 = item0.getMoneySource().getDisplayName();
				}
			} 
		}
		if (arg1 != null) {
			
			type1 = item1.getType();
			
            if(isComboFile(item1.getComboFileInd())) {
				if(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(type1) || 
						GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(type1)) {
					type1 = SUBMISSION_TYPE_COMBO_CONTRIBUTION;
				} else {
					type1 = SUBMISSION_TYPE_COMBO_CENSUS;
				}
			
			} else if (GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR.equals(type1) || GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER.equals(type1)) {
				if ( item1.getMoneySource() != null ) {
					type1 = item1.getMoneySource().getDisplayName();
				}
			} 
		}
		
		int result = (isAscending() ? 1 : -1) * getPriority(type0).compareTo(getPriority(type1));
		
		// if they are equal, goto the secondary sort by tracing (submission) number descending always
		if (result == 0) {
			result = new SubmissionHistoryItemSubmissionNumberComparator(false).compare(arg0, arg1);
		}
		return result;
	}

	/**
	 * returns combo file ind
	 * @param indicator
	 * @return boolean
	 */
	private boolean isComboFile(String indicator) {
		return StringUtils.equals("Y", indicator);
	}
	
	/**
	 * Returns the priority of a type.
	 */
	private static Integer getPriority(String type) {
		Integer result = (Integer) priority.get(type);
		if (result == null) {
			result = MIN_PRIORITY;
		}
		return result;
	}
}