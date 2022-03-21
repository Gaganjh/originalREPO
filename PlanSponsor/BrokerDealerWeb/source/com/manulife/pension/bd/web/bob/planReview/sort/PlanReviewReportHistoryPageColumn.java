package com.manulife.pension.bd.web.bob.planReview.sort;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;

/**
 * This is enum class for History Page columns used for Plan Review report
 * pages.
 * 
 * @author NagaRaju
 * 
 */

public enum PlanReviewReportHistoryPageColumn {

	CONTRACT_REPORTENDDATE("selectedReportMonthEndDate",
			MonthEndDateComparator.class);

	private String fieldName;
	private Class<? extends BaseContractReviewReportComparator> comparatorClass;

	PlanReviewReportHistoryPageColumn(String fieldName,
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

		try {

			return comparatorClass.getConstructor(String.class).newInstance(
					sortDirection);

		} catch (InstantiationException e) {
			throw new SystemException(e, "InstantiationException occured for :"
					+ comparatorClass.toString());
		} catch (IllegalAccessException e) {
			throw new SystemException(e, "IllegalAccessException occured for :"
					+ comparatorClass.toString());
		} catch (NoSuchMethodException e) {
			throw new SystemException(e, "NoSuchMethodException occured for :"
					+ comparatorClass.toString());
		} catch (InvocationTargetException e) {
			throw new SystemException(e,
					"InvocationTargetException occured for :"
							+ comparatorClass.toString());
		}
	}

	/**
	 * @param fieldName
	 * @return ContractReviewReportHistoryPageColumn
	 */
	public static PlanReviewReportHistoryPageColumn getContractReviewReportHistoryPageColumn(
			String fieldName) {
		PlanReviewReportHistoryPageColumn object = null;

		for (PlanReviewReportHistoryPageColumn column : PlanReviewReportHistoryPageColumn
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
