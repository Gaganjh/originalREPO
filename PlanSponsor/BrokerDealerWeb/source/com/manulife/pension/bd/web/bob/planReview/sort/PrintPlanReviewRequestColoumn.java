package com.manulife.pension.bd.web.bob.planReview.sort;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.exception.SystemException;

/**
 * 
 * Plan Review - Print Request Page Comparator
 * 
 * @author akarave
 * 
 */

public enum PrintPlanReviewRequestColoumn {

	CONTRACT_NAME		("contractName", ContractNameComparator.class), 
	CONTRACT_NUMBER		("contractNumber", ContractNumberComparator.class), 
	MONTH_END_DATE		("selectedReportMonthEndDate", PrintMonthAndDateComparator.class), 
	NUMBER_OF_COPIES	("numberOfCopies", NumberOfCopiesComparator.class);

	private String fieldName;
	private Class<? extends BaseContractReviewReportComparator> comparatorClass;

	PrintPlanReviewRequestColoumn(String fieldName,
			Class<? extends BaseContractReviewReportComparator> comparatorClass) {
		this.fieldName = fieldName;
		this.comparatorClass = comparatorClass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<? extends BaseContractReviewReportComparator> getComparatorClass() {
		return comparatorClass;
	}

	/**
	 * @param sortDirection
	 * @return BaseContractReviewReportComparator
	 * @throws SystemException
	 */
	public BaseContractReviewReportComparator getComparatorInstance(
			String sortDirection) throws SystemException {
		return PlanReviewReportUtils.getComparatorInstance(sortDirection,
				getComparatorClass());
	}

	/**
	 * @param fieldName
	 * @return ContractReviewReportStep2PageColumn
	 */
	public static PrintPlanReviewRequestColoumn getPrintPlanReviewRequestColoumn(
			String fieldName) {
		PrintPlanReviewRequestColoumn object = null;

		for (PrintPlanReviewRequestColoumn column : PrintPlanReviewRequestColoumn
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
