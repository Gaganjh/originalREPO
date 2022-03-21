package com.manulife.pension.bd.web.estatement.sort;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;

/**
 * This is enum class for Step1 Page columns used for Plan Review report pages.
 * 
 * @author NagaRaju
 * 
 */

public enum RiaStatementsPageColumn {

	STATEMENT_DATE("statementDate", StatementDateComparator.class), FIRM_NAME(
			"firmName", FirmNameComparator.class);

	private String fieldName;
	private Class<? extends BaseRiaStatementsReportComparator> comparatorClass;

	RiaStatementsPageColumn(String fieldName,
			Class<? extends BaseRiaStatementsReportComparator> comparatorClass) {
		this.fieldName = fieldName;
		this.comparatorClass = comparatorClass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<? extends BaseRiaStatementsReportComparator> getComparatorClass() {
		return comparatorClass;
	}

	/**
	 * @param sortDirection
	 * @return BaseContractReviewReportComparator
	 * @throws SystemException
	 */
	public BaseRiaStatementsReportComparator getComparatorInstance(
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
	 * @return ContractReviewReportStep1PageColumn
	 */
	public static RiaStatementsPageColumn getRiaStatementsPageColumn(
			String fieldName) {
		RiaStatementsPageColumn object = null;

		for (RiaStatementsPageColumn column : RiaStatementsPageColumn
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
