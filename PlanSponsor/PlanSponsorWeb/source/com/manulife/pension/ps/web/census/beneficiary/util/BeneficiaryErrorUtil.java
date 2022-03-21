package com.manulife.pension.ps.web.census.beneficiary.util;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;


import com.manulife.pension.service.beneficiary.util.BeneficiaryDesignationErrors;
import com.manulife.pension.service.beneficiary.util.BeneficiaryValidationError;
import com.manulife.pension.service.beneficiary.util.BeneficiaryValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationError.ErrorType;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;


/**
 * 
 * Utility class for converting BeneficiaryDesignationError into ValidationError 
 * @author Tamilarasu krishnamoorthy
 *
 */
public class BeneficiaryErrorUtil {

	private static BeneficiaryErrorUtil instance = null;

	/**
	 * Defines the mapping between the BeneficiaryValidationErrorCode  with the BeneficiaryErrorCodes
	 * name
	 */
	protected static final EnumMap<BeneficiaryValidationErrorCode, Integer> ErrorMapping = new EnumMap<BeneficiaryValidationErrorCode, Integer>(
			BeneficiaryValidationErrorCode.class);

	public static final EnumMap<ErrorType, Type> ErrorTypeMap = new EnumMap<ErrorType, Type>(
			ErrorType.class);
	static {
		ErrorTypeMap.put(ErrorType.error, Type.error);
		ErrorTypeMap.put(ErrorType.warning, Type.warning);

		ErrorMapping.put(BeneficiaryValidationErrorCode.InvalidFirstName,BeneficiaryErrorCodes.ERROR_FIRST_NAME_INVALID );
		ErrorMapping.put(BeneficiaryValidationErrorCode.MandatoryFirstName,BeneficiaryErrorCodes.ERROR_FIRST_NAME_REQUIRED );
		ErrorMapping.put(BeneficiaryValidationErrorCode.LastNameInvalid,BeneficiaryErrorCodes.ERROR_LAST_NAME_INVALID );
		ErrorMapping.put(BeneficiaryValidationErrorCode.LastNameMandatory,BeneficiaryErrorCodes.ERROR_LAST_NAME_REQUIRED );
		ErrorMapping.put(BeneficiaryValidationErrorCode.BirthDateInvalid,BeneficiaryErrorCodes.ERROR_DATE_OF_BIRTH_INVALID );
		ErrorMapping.put(BeneficiaryValidationErrorCode.BirthDateFutureDateInvalid,BeneficiaryErrorCodes.ERROR_DOB_GREATER_THAN_CURRENT_DATE );
		ErrorMapping.put(BeneficiaryValidationErrorCode.RelationshipCodeMandatory,BeneficiaryErrorCodes.ERROR_RELATIONSHIP_REQUIRED );
		ErrorMapping.put(BeneficiaryValidationErrorCode.OtherRelationshipCodeMandatory,BeneficiaryErrorCodes.ERROR_RELATIONSHIP_REQUIRED );
		ErrorMapping.put(BeneficiaryValidationErrorCode.OtherRelationshipCodeInvalid,BeneficiaryErrorCodes.ERROR_RELATIONSHIP_OTHER_INVALID );
		ErrorMapping.put(BeneficiaryValidationErrorCode.SharePctNull,BeneficiaryErrorCodes.ERROR_PERCENTAGE_SHARE_NOT_NULL_OR_NOT_NUMERIC );
		ErrorMapping.put(BeneficiaryValidationErrorCode.SharePctMandatory,BeneficiaryErrorCodes.ERROR_PERCENTAGE_SHARE_NOT_NULL_OR_NOT_NUMERIC );
		ErrorMapping.put(BeneficiaryValidationErrorCode.SharePctInvalid,BeneficiaryErrorCodes.ERROR_PERCENTAGE_SHARE_NOT_BW_0_AND_100 );
		ErrorMapping.put(BeneficiaryValidationErrorCode.PrimaryBeneficiaryInvalidCount,BeneficiaryErrorCodes.ERROR_ADD_MAXIMUM_BENEFICIARY );
		ErrorMapping.put(BeneficiaryValidationErrorCode.ContingentBeneficiaryInvalidCount,BeneficiaryErrorCodes.ERROR_ZERO_PRIMARY_WITH_CONTIGENT );
		ErrorMapping.put(BeneficiaryValidationErrorCode.ZeroBeneficiaryCount,BeneficiaryErrorCodes.ZERO_BENEFICIARY );
		ErrorMapping.put(BeneficiaryValidationErrorCode.ContingentWithZeroPrimary,BeneficiaryErrorCodes.ERROR_ZERO_PRIMARY_WITH_CONTIGENT );
		ErrorMapping.put(BeneficiaryValidationErrorCode.PrimaryPercentageExceedsHundred,BeneficiaryErrorCodes.ERROR_PERCENTAGE_SHARE_NOT_EQUAL_100 );
		ErrorMapping.put(BeneficiaryValidationErrorCode.ContingentPercentageExceedsHundred,BeneficiaryErrorCodes.ERROR_PERCENTAGE_SHARE_NOT_EQUAL_100 );
		

	}

	/**
	 * Get the BeneficiaryErrorUtil instance
	 * @return BeneficiaryErrorUtil
	 */
	public static BeneficiaryErrorUtil getInstance() {
		if (instance == null) {
				instance = new BeneficiaryErrorUtil();
		}
		return instance;
	}

	/**
	 * Set the Error in validation Error list 
	 * @param errors
	 * @param vErrors
	 * @return
	 */
	public List<ValidationError> setErrorInValidationFormat(BeneficiaryDesignationErrors errors,
			List<ValidationError> vErrors){
		List<BeneficiaryValidationError> recordErrors=errors.getRecordErrors();
		//Convert the BeneficiaryDesignationErrors into ValidationError list
		convert(recordErrors, vErrors);
		return vErrors;

	}

	/**
	 * Add the Error into validationError list
	 * @param errors
	 * @param error
	 */
	@SuppressWarnings("unchecked")
	private void addValidationError(List<ValidationError> errors, ValidationError error) {
		// if there is params, always add it
		Object[] params = error.getParams();
		if (params == null || params.length == 0) {
			for (ValidationError e : errors) {
				if (e.getErrorCode() == error.getErrorCode()
						&& e.getType().compareTo(error.getType()) == 0) {
					e.getFieldIds().addAll(error.getFieldIds());
					return;
				}
			}
		}
		errors.add(error);
	}

	/**
	 * Convert the BeneficiaryDesignationErrors into ValidationError list
	 * @param field
	 * @param errors
	 * @param vErrors
	 */
	public void convert(List<BeneficiaryValidationError> errors, List<ValidationError> vErrors) {

		for (BeneficiaryValidationError error : errors) {
			Integer vErrorCode = ErrorMapping.get(error.getErrorCode());
			if (vErrorCode != null) {
				String fieldName ="";// getFormFieldName(field);

				//Get the form FieldName name from param 
				Object[] string= error.getParams();
				List<Object> list = Arrays.asList(string);
				fieldName=(String) list.get(2);
				if(fieldName != null ){
					ValidationError vError = new ValidationError(fieldName, vErrorCode,
							Type.error);
					vError.setParams(error.getParams());
					addValidationError(vErrors, vError);
				}
			} else {
				throw new IllegalArgumentException(
						"Cannot map Beneficiary validation error ["
						+ error.getErrorCode()
						+ "] to validation error.");
			}

		}    	

	}



}
