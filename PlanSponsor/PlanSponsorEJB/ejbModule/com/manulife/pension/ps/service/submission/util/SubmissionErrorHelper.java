package com.manulife.pension.ps.service.submission.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.submission.util.SubmissionErrorParsingHelper;

/**
 * @author parkand
 */
public class SubmissionErrorHelper {

	private static Logger logger = Logger.getLogger(SubmissionErrorHelper.class);

	private static int warningLevel = 20;	// this includes warnings (10) and problems (20)

	public static final String ADDRESS1_FIELD_KEY = "FIELD_ADDRESS_LINE_1";
    public static final String ADDRESS2_FIELD_KEY = "FIELD_ADDRESS_LINE_2";
	public static final String CITY_FIELD_KEY = "FIELD_CITY_NAME";
	public static final String STATE_FIELD_KEY = "FIELD_STATE_CODE";
	public static final String ZIP_FIELD_KEY = "FIELD_ZIP_CODE";
    public static final String COUNTRY_FIELD_KEY = "FIELD_COUNTRY_NAME";
	public static final String SSN_FIELD_KEY = "FIELD_SSN";
    public static final String EMPLOYEE_NUMBER_FIELD_KEY = "FIELD_EMPLOYEE_NUMBER";
    public static final String FIRST_NAME_FIELD_KEY = "FIELD_FIRST_NAME";
    public static final String LAST_NAME_FIELD_KEY = "FIELD_LAST_NAME";
    public static final String BIRTH_DATE_FIELD_KEY = "FIELD_BIRTH_DATE";
    public static final String FULL_NAME_FIELD_KEY = "FIELD_FULL_NAME";
    public static final String HIRE_DATE_FIELD_KEY = "FIELD_HIRE_DATE";
    public static final String STATE_OF_RESIDENCE_FIELD_KEY = "FIELD_STATE_OF_RESIDENCE";
    public static final String EMAIL_FIELD_KEY = "FIELD_EMAIL";
    public static final String EMPLOYEE_STATUS_FIELD_KEY = "FIELD_EMPLOYEE_STATUS";
    public static final String EMPLOYEE_STATUS_DATE_FIELD_KEY = "FIELD_EMPLOYEE_STATUS_DATE";
    public static final String NAME_PREFIX_FIELD_KEY = "FIELD_NAME_PREFIX";
    public static final String MIDDLE_INITIAL_FIELD_KEY = "FIELD_MIDDLE_INITIAL";
    public static final String DIVISION_FIELD_KEY = "FIELD_DIVISION";
    public static final String YTD_HOURS_WORKED_FIELD_KEY = "FIELD_YTD_HOURS_WORKED";
    public static final String YTD_HOURS_WORKED_EFFDATE_FIELD_KEY = "FIELD_YTD_HOURS_WORKED_EFFDATE";
    public static final String ELIGIBILITY_INDICATOR_FIELD_KEY = "FIELD_ELIGIB_INDICATOR";
    public static final String ELIGIBILITY_DATE_FIELD_KEY = "FIELD_ELIGIB_DATE";
    public static final String OPT_OUT_INDICATOR_FIELD_KEY = "FIELD_OPT_OUT_INDICATOR";
    public static final String BEFORE_TAX_DEFER_PERCENTAGE_FIELD_KEY = "FIELD_BEFORE_TAX_DEFER_PERCENTAGE";
    public static final String DESIG_ROTH_DEFER_PERCENTAGE_FIELD_KEY = "FIELD_DESIG_ROTH_DEFER_PERCENTAGE";
    public static final String BEFORE_TAX_DEFER_AMOUNT_FIELD_KEY = "FIELD_BEFORE_TAX_DEFER_AMOUNT";
    public static final String DESIG_ROTH_DEFER_AMOUNT_FIELD_KEY = "FIELD_DESIG_ROTH_DEFER_AMOUNT";
    public static final String CONTRACT_ID_FIELD_KEY = "FIELD_CONTRACT_ID";
    public static final String PARTICIPANT_ID_FIELD_KEY = "FIELD_PARTICIPANT_ID";
    public static final String PLAN_YTD_COMPENSATION_KEY = "FIELD_PLAN_YTD_COMPENSATION";
    public static final String ANNUAL_BASE_SALARY_KEY = "FIELD_ANNUAL_BASE_SALARY";
    public static final String VESTED_YEARS_OF_SERVICE_KEY = "FIELD_VESTED_YEARS_OF_SERVICE";
    public static final String VESTED_YEARS_OF_SERVICE_EFF_DATE_KEY = "FIELD_VESTED_YEARS_OF_SERVICE_EFFDATE";
    public static final String VESTING_DATE_KEY = "FIELD_VESTING_DATE";
    public static final String VESTING_PERCENTAGE_KEY = "FIELD_VESTING_PERCENTAGE";
    public static final String LONG_TERM_PART_TIME_ASSESSMENT_YEAR_FIELD_KEY = "FIELD_LONG_TERM_PART_TIME_ASSESSMENT_YEAR";
    public static final String APPLY_LTPT_CREDITING_KEY = "FIELD_APPLY_LTPT_CREDITING";

	private SubmissionErrorHelper() {
		super();
	}

	/**
     * Adding an argument to a submission error in the collection. 
     * The error is identified by given code.
     * 
     * @param   Collection of submision errors
     * @param   code
     * @param   argument
     */
    public static void addArgument(Collection errors,String code,String argument) {
        for (Object error : errors) {
            SubmissionError submissionError = (SubmissionError)error;
            if (ObjectUtils.equals(submissionError.getCode(), code)) {
                submissionError.setArgument(argument);
            }
        }
    }

	/**
	 * returns a Collection of SubmissionErrors
	 *
	 * input formats expected:
     * ER,fieldName1:errorCode1,fieldName2:errorCode2
     * ER,fieldName1:errorCode1:value1,fieldName2:errorCode2:value2
	 * ER,fieldName1:errorCode1|value1;errorCode2|value2,fieldName2:errorCode3|value3;errorCode4|value4
	 */
	public static Collection parseErrorConditionString(String errorString, int rowNumber) {
	    return SubmissionErrorParsingHelper.parseErrorConditionString(errorString, rowNumber);
    }
	
	public static boolean isWarning(SubmissionError error) {
		return (error.getSeverity() <= warningLevel ? true : false);
	}

    public static boolean isEditableWarning(SubmissionError error) {
        return (error.getSeverity() == warningLevel ? true : false);
    }

	public static boolean isError(SubmissionError error) {
		return (error.getSeverity() > warningLevel ? true : false);
	}

    public static boolean isErrorOrEditableWarning(SubmissionError error) {
        return (error.getSeverity() >= warningLevel ? true : false);
    }

	public static int getNumberOfErrors(Collection errors) {
		int count = 0;
		Iterator iter = errors.iterator();
		while (iter.hasNext()) {
			SubmissionError error = (SubmissionError)iter.next();
			if (isError(error)) count++;
		}
		return count;
	}

    public static int getNumberOfErrorsAndEditableWarnings(Collection errors) {
        int count = 0;
        Iterator iter = errors.iterator();
        while (iter.hasNext()) {
            SubmissionError error = (SubmissionError)iter.next();
            if (isErrorOrEditableWarning(error)) count++;
        }
        return count;
    }

	public static int getNumberOfWarnings(Collection errors) {
		int count = 0;
		Iterator iter = errors.iterator();
		while (iter.hasNext()) {
			SubmissionError error = (SubmissionError)iter.next();
			if (isWarning(error)) count++;
		}
		return count;
	}

	public static int getErrorStatus(Collection errors, String fieldKey) {
		int status = CensusSubmissionItem.OK;
		if (errors != null && !errors.isEmpty()) {
			// iterate through the errors and look for warnings/errors for the specified field
			Iterator iter = errors.iterator();
			while (iter.hasNext()) {
				SubmissionError error = (SubmissionError)iter.next();
				String field = (String) SubmissionErrorParsingHelper.getFieldMap().get(fieldKey);
				if (field == null) {
					throw new IllegalArgumentException("Invalid field key: " + fieldKey);
				} else if (field.equals(error.getField())) {
					if (isWarning(error)) {
                        if (isEditableWarning(error)) {
                            return CensusSubmissionItem.EDITABLE_WARNING;
                        } else {
                            return CensusSubmissionItem.NON_EDITABLE_WARNING;
                        }
					} else if (isError(error)) {
						return CensusSubmissionItem.ERROR;
					}
				}
			}
		}
		return status;
	}
    
    public static String getFieldLabel(String fieldKey) {
        String field = (String) SubmissionErrorParsingHelper.getFieldMap().get(fieldKey);
        if (field == null) {
            throw new IllegalArgumentException("Invalid field key: " + fieldKey);
        } else {
            return field;
        }
    }
    
	/**
	 * This method finds the field/value in a particular row with matching error codes
	 * and pass it through the given predicate. See its references for details.   
	 * 
	 * @param field
	 * @param value
	 * @param row
	 * @param errors
	 * @param codesToCheck
	 * @param predicate
	 * @return
	 */
	public static boolean evaluateMatchedField(String field,
			String value, int row, SubmissionErrorCollection errors,
			List<String> codesToCheck, Predicate predicate) {
		if (errors != null && errors.getErrors() != null) {
			Iterator<SubmissionError> errorsIt = errors.getErrors().iterator();
			// check for errors first
			while (errorsIt.hasNext()) {
				SubmissionError error = (SubmissionError) errorsIt.next();
				Iterator<String> codesIt = codesToCheck.iterator();
				while (codesIt.hasNext()) {
					String code = (String) codesIt.next();
					if (error.getCode().equalsIgnoreCase(code)
							&& (row == 0 || error.getRowNumber() == row)
							&& (value == null || value.equalsIgnoreCase(error
									.getValue()))
							&& (field == null || field.equalsIgnoreCase(error
									.getField()))) {
						if (predicate.evaluate(error)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
