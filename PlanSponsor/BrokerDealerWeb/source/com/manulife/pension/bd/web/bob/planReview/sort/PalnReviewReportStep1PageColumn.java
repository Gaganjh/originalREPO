package com.manulife.pension.bd.web.bob.planReview.sort;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.exception.SystemException;

/**
 * This is enum class for Step1 Page columns used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public enum PalnReviewReportStep1PageColumn {

	CONTRACT_NAME			("contractName", ContractNameComparator.class), 
	CONTRACT_NUMBER			("contractNumber", ContractNumberComparator.class), 
	INDUSTRY_SEGMENT		("selectedIndustrySegment", IndustrySegmentComparator.class), 
	CONTRACT_REPORT_END_DATE("selectedReportMonthEndDate", MonthEndDateComparator.class), 
	I_REPORT_IND			("selectedperformanceAndExpenseRatio", PerformanceRatioComparator.class), 
	NUMBER_OF_COPIES		("numberOfCopies", NumberOfCopiesComparator.class);
	
	private String fieldName;
	private Class<? extends BaseContractReviewReportComparator> comparatorClass;

	PalnReviewReportStep1PageColumn(String fieldName,
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
	 * @return ContractReviewReportStep1PageColumn
	 */
	public static PalnReviewReportStep1PageColumn getContractReviewReportStep1PageColumn(
			String fieldName) {
		PalnReviewReportStep1PageColumn object = null;

		for (PalnReviewReportStep1PageColumn column : PalnReviewReportStep1PageColumn
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
