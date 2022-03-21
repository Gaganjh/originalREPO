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

public enum PlanReviewReportStep2PageColumn {

	CONTRACT_NAME("contractName", ContractNameComparator.class), CONTRACT_NUMBER(
			"contractNumber", ContractNumberComparator.class), PRESENTER_NAME(
			"presenterName", PresenterNameComparator.class), REPORT_COVERIMAGE(
			"coverImageName", CoverImageComparator.class), LOGO_IMAGE(
			"logoImage", LogoImageComparator.class);

	private String fieldName;
	private Class<? extends BaseContractReviewReportComparator> comparatorClass;

	PlanReviewReportStep2PageColumn(String fieldName,
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
	public static PlanReviewReportStep2PageColumn getContractReviewReportStep2PageColumn(
			String fieldName) {
		PlanReviewReportStep2PageColumn object = null;

		for (PlanReviewReportStep2PageColumn column : PlanReviewReportStep2PageColumn
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
