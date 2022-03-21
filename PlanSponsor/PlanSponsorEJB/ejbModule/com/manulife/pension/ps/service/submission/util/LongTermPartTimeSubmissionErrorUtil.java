package com.manulife.pension.ps.service.submission.util;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.submission.SubmissionError;

public class LongTermPartTimeSubmissionErrorUtil {
	private static LongTermPartTimeSubmissionErrorUtil instance = null;

	public static LongTermPartTimeSubmissionErrorUtil getInstance() {
		if (instance == null) {
			synchronized (LongTermPartTimeSubmissionErrorUtil.class) {
				instance = new LongTermPartTimeSubmissionErrorUtil();
			}
		}
		return instance;
	}

	// order of fields in which errors should be displayed
	protected final String[] errorDisplayOrder = {
            SubmissionErrorHelper.SSN_FIELD_KEY,
            SubmissionErrorHelper.FIRST_NAME_FIELD_KEY,
            SubmissionErrorHelper.LAST_NAME_FIELD_KEY,
            SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY,
            SubmissionErrorHelper.LONG_TERM_PART_TIME_ASSESSMENT_YEAR_FIELD_KEY};

	public String[] getFieldsByErrorDisplayOrder() {
		return errorDisplayOrder;
	}

	/**
	 * Sort the validation errors based on the defined errorDisplayOrder
	 * 
	 * @param errors
	 * @return
	 */
	public List<SubmissionError> sort(List<SubmissionError> errors) {
		if (errors == null || errors.size() == 0) {
			return errors;
		}
		List<SubmissionError> sortedList = new ArrayList<SubmissionError>(errors.size());

		for (String fieldKey : getFieldsByErrorDisplayOrder()) {

			for (SubmissionError error : errors) {
				if (error.getField().equals(SubmissionErrorHelper.getFieldLabel(fieldKey))) {
					sortedList.add(error);
				}
			}
		}
		return sortedList;
	}
}
