package com.manulife.pension.ps.web.tools.util;

import java.util.Vector;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;

/**
 * Utility class, translate the UploadFileType to a displable string
 */
public class SubmissionTypeTranslater {

	private static final Vector SUBMISSION_TYPES = new Vector(7);

	private static final String[] SUBMISSION_TYPE_NAMES = {
														"Address",
														"Regular Contribution",
														"Transfer Contribution",
														"Sample",
														"Test",
														"Payment",
														"Census",
                                                        "Vesting",
                                                        "Conversion",
                                                        "LTPT"
                                                        
													};

	static {
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_ADDRESS);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_MISCELLANEOUS);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_TEST);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT);
		SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_CENSUS);
        SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_VESTING);
        
        /**
    	 * fileType Conversion 
    	 */
        SUBMISSION_TYPES.addElement(GFTUploadDetail.SUBMISSION_TYPE_CONVERSION);
        
        /**
    	 * fileType LTPT template file submission 
    	 */
        SUBMISSION_TYPES.addElement(SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT); 
	}
/**
 * SubmissionTypeTranslater constructor comment.
 */
private SubmissionTypeTranslater() {
	super();
}
/**
 * SubmissionTypeTranslater constructor comment.
 */
public static String translate(String type) {
	if (!SubmissionTypeTranslater.SUBMISSION_TYPES.contains(type)) {
		throw new IllegalArgumentException(new StringBuffer("Submission Type [").append(type).append("] is not valid").toString());
	}
	return SubmissionTypeTranslater.SUBMISSION_TYPE_NAMES[SUBMISSION_TYPES.indexOf(type)];
}
}

