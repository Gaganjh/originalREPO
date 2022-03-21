package com.manulife.pension.bd.web.bob.planReview.sort;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.exception.SystemException;

/**
 * This is enum class for Step2 Page columns used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public enum HistoryDetailsColoum {

	PRINT_CONFIRMATION_NUMBER("activityId", PrintConfirmationNumberComparator.class), PRINT_REQUEST_DATE(
			"createdTs", PrintRequestDateComparator.class), PRINT_REQUEST_STATUS(
			"activityStatus", PrintRequestStatusComparator.class);
	private String fieldName;
	private Class<? extends BaseHistoryDetailsComparator> comparatorClass;

	HistoryDetailsColoum(String fieldName,
			Class<? extends BaseHistoryDetailsComparator> comparatorClass) {
		this.fieldName = fieldName;
		this.comparatorClass = comparatorClass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<? extends BaseHistoryDetailsComparator> getComparatorClass() {
		return comparatorClass;
	}

	/**
	 * @param sortDirection
	 * @return 
	 * @throws SystemException
	 */
	public BaseHistoryDetailsComparator getComparatorInstance(
			String sortDirection) throws SystemException {
		return PlanReviewReportUtils.getComparatorInstanceHistoryDetails(sortDirection,
				getComparatorClass());
	}

	/**
	 * @param fieldName
	 * @return ContractReviewReportStep2PageColumn
	 */
	public static HistoryDetailsColoum getHistoryDetailsColoum(
			String fieldName) {
		HistoryDetailsColoum object = null;

		for (HistoryDetailsColoum column : HistoryDetailsColoum
				.values()) {
			if (StringUtils.equals(column.getFieldName(), fieldName)) {
				object = column;
			}
		}

		if (object == null) {
			throw new IllegalArgumentException("Invalid Column Field Name: "
					+ fieldName);
		}

		return object;
	}

}
