package com.manulife.pension.platform.web.investment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;
import com.manulife.pension.util.content.GenericException;
/**
 * Validator class for Fund track page. 
 * 
 * @author thangjo
 *
 */
public class IPSAndReviewDetailsDataValidator {

	public static final GenericException DUPLICATE_CRITERIA_SELECTED_ERROR = new GenericException(CommonErrorCodes.DUPLICATE_CRITERIA_SELECTED);
	public static final GenericException MUST_ENTER_WHOLE_NUMBER_ERROR = new GenericException(CommonErrorCodes.MUST_ENTER_WHOLE_NUMBER);
	public static final GenericException NON_NUMERIC_VALUE_ERROR = new GenericException(CommonErrorCodes.NON_NUMERIC_VALUE);
	public static final GenericException WEIGHTING_NOT_SELETED_ERROR = new GenericException(CommonErrorCodes.WEIGHTING_NOT_SELETED);
	public static final GenericException CRITERIA_NOT_SELECTED_ERROR = new GenericException(CommonErrorCodes.CRITERIA_NOT_SELECTED);
	public static final GenericException TOTAL_WEIGHTING_PERCENT_SHOULD_BE_100_ERROR = new GenericException(
			CommonErrorCodes.TOTAL_WEIGHTING_PERCENT_SHOULD_BE_100); 
	public static final GenericException NO_ACCESS_ERROR = new GenericException(CommonErrorCodes.NO_ACCESS_ERROR);
	public static final GenericException NO_ACTION_TAKEN = new GenericException(CommonErrorCodes.IPS_NO_ACTION_TAKEN_ERROR);
	public static final GenericException IPS_INVALID_SERVICE_DATE_ERROR = new GenericException(CommonErrorCodes.IPS_INVALID_SERVICE_DATE);
	public static final GenericException IPS_PC_REVIEW_EXISTS_ERROR = new GenericException(CommonErrorCodes.IPS_PC_REVIEW_EXISTS);
	public static final GenericException IPS_IAT_EFFECTIVE_DATE_FORMAT_ERROR = new GenericException(CommonErrorCodes.IPS_INVALID_SERVICE_DATE_FORMAT);
	public static final GenericException IPS_AGREE_APPROVAL_NOT_CHECKED_ERROR = new GenericException(CommonErrorCodes.IPS_CONFIRMATION_CHECKBOX_NOT_CHECKED);
	public static final GenericException IPS_INVALID_IPS_IAT_EFFECTIVE_DATE_RANGE_ERROR = new GenericException(CommonErrorCodes.IPS_INVALID_IPS_IAT_EFFECTIVE_DATE_RANGE_ERROR);
	public static final GenericException IPS_NON_BUSINESS_IPS_IAT_EFFECTIVE_DATE_ERROR = new GenericException(CommonErrorCodes.IPS_NON_BUSINESS_IPS_IAT_EFFECTIVE_DATE_ERROR);
	public static final GenericException INVALID_IPS_IAT_EFFECTIVE_DATE_ERROR = new GenericException(CommonErrorCodes.IPS_INVALID_IAT_EFFECTIVE_DATE_ERROR); 
	
	/**
	 * Validate and populate the passed error object if there are any errors
	 * 
	 * @param ipsAssistServiceForm
	 * @param errors
	 */
	public static void validate(IPSAndReviewDetailsForm ipsAssistServiceForm,
			List<GenericException> errors) {
		List<CriteriaAndWeightingPresentation> criteriaAndWeightingList = ipsAssistServiceForm
				.getCriteriaAndWeightingPresentationList();
		List<String> criteriaList = new ArrayList<String>();

		for (CriteriaAndWeightingPresentation criteriaAndWeighting : criteriaAndWeightingList) {
			String criteria = criteriaAndWeighting.getCriteriaCode();
			String weighting = criteriaAndWeighting.getWeighting();
			
			// Check whether criteria is selected if the weighting is entered
			if (isCriteriaNotSelected(criteria, weighting)) {
				addError(errors, CRITERIA_NOT_SELECTED_ERROR);
			}

			// Check whether weighting is entered if the criteria is selected
			if (isWeightingNotSelected(criteria, weighting)) {
				addError(errors, WEIGHTING_NOT_SELETED_ERROR);
			}

			// Check whether duplicate criteria is selected
			if (StringUtils.isNotBlank(criteria)
					&& isDuplicateCriteria(criteriaList, criteria)) {
				addError(errors, DUPLICATE_CRITERIA_SELECTED_ERROR);
			}

			if (StringUtils.isNotBlank(weighting)) {
				// Check whether the entered value is valid numeric
				if (isInvalidNumeric(weighting)) {
					addError(errors, NON_NUMERIC_VALUE_ERROR);
				} else if (isInvalidRangeOrWholeNumber(weighting)) { // Check whether entered value is a whole number 
					addError(errors, MUST_ENTER_WHOLE_NUMBER_ERROR);
				}
			}
		}
		
		String newServiceMonth = ipsAssistServiceForm.getNewAnnualReviewMonth();
		String newServiceDate = ipsAssistServiceForm.getNewAnnualReviewDate();
		
		if(isInvalidServiceDate(newServiceDate, newServiceMonth)) {
			addError(errors, IPS_INVALID_SERVICE_DATE_ERROR);
		}
		
		// Check whether the total weighting is 100
		if (isInvalidTotal(ipsAssistServiceForm.getTotalWeighting())) {
			addError(errors, TOTAL_WEIGHTING_PERCENT_SHOULD_BE_100_ERROR);
		}
	}
	
	/**
	 * Return true if there is a duplicate criteria
	 * else return false
	 * 
	 * @param criteriaList
	 * @param criteria
	 * @return
	 */
	private static boolean isDuplicateCriteria(List<String> criteriaList,
			String criteria) {
		if (criteriaList.contains(criteria)) {
			return true;
		} else{
			criteriaList.add(criteria);
			return false;
		}
	}
	
	/**
	 * Return true if the value is non numeric 
	 * else return false
	 * 
	 * @param weighting
	 * @return
	 */
	private static boolean isInvalidNumeric(String weighting){
		if(NumberUtils.isNumber(weighting)) {
			return false;
		} else{
			return true;
		}
	}
	
	/**
	 * Return true if the value is not in range
	 * else return false
	 * 
	 * @param weighting
	 * @return
	 */
	private static boolean isInvalidRangeOrWholeNumber(String value) {

		if (NumberUtils.isDigits(value)
				&& DataValidationHelper.isInRange(Integer.parseInt(value),
						1, 100)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Return true if criteria is selected and weighting is not selected
	 * else return false
	 * 
	 * @param criteria
	 * @param weighting
	 * @return
	 */
	private static boolean isWeightingNotSelected(String criteria,
			String weighting) {
		if (StringUtils.isNotBlank(criteria) && StringUtils.isBlank(weighting)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if weighting is entered and criteria is not selected
	 * else return false
	 * 
	 * @param criteria
	 * @param weighting
	 * @return
	 */
	private static boolean isCriteriaNotSelected(String criteria,
			String weighting){
		if (StringUtils.isNotBlank(weighting) && StringUtils.isBlank(criteria)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if the total is not equal to 100
	 * else return false
	 * 
	 * @param total
	 * @return
	 */
	private static boolean isInvalidTotal(int total) {
		if (total == 100) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Return true if the contract status is invalid
	 * 
	 * @param contractStatus
	 * @return
	 */
	public static boolean isInvalidcontract(String contractStatus) {
		if (Contract.STATUS_CONTRACT_DISCONTINUED.equals(contractStatus)
				|| Contract.STATUS_CONTRACT_FROZEN.equals(contractStatus)
				|| Contract.STATUS_INACTIVE_CONTRACT.equals(contractStatus)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if the New Service Date is invalid
	 * 
	 * @param newServiceDate
	 * @param newServiceMonth
	 * @return
	 */
	private static boolean isInvalidServiceDate(String newServiceDate,
			String newServiceMonth) {
		if ("31".equals(newServiceDate)) {
			if ("02".equals(newServiceMonth) || "04".equals(newServiceMonth)
					|| "06".equals(newServiceMonth)
					|| "09".equals(newServiceMonth)
					|| "08".equals(newServiceMonth)
					|| "11".equals(newServiceMonth)) {
				return true;
			} else {
				return false;
			}
		} else if (("30".equals(newServiceDate) || "29".equals(newServiceDate))
				&& "02".equals(newServiceMonth)) {
				return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Validates Fund Instructions submitted by Trustee
	 * 
	 * @param request
	 * @param ipsAssistServiceForm
	 * @param errors
	 * @return int
	 */
	public static boolean validateFundInstructions(
			List<IPSFundInstructionPresentation> fundInstructions,
			List<GenericException> errors) {

		boolean valid = true;
		
		// Update No Action Taken to true if no action has been taken by the Trustee
		for (IPSFundInstructionPresentation fundInstruction : fundInstructions) {
			List<ToFundVO> toFundVOList = fundInstruction.getToFundVO();

			for (ToFundVO toFundVO : toFundVOList) {
				if (StringUtils.isBlank(toFundVO.getActionIndicator())) {
					toFundVO.setNoActionTaken(true);
					valid = false;
				} else {
					toFundVO.setNoActionTaken(false);
				}
			}
		}
		return valid;
	}

	/**
	 * Validates IPS IAT Effective Date for Blank and Date Pattern
	 * 
	 * @param request
	 * @param ipsAssistServiceForm
	 * @param errors
	 */
	public static void validateIPSIATEffectiveDate(HttpServletRequest request,
			IPSReviewResultDetailsForm ipsAssistServiceForm,
			List<GenericException> errors) {
		String ipsIatEffectDate = ipsAssistServiceForm.getIpsIatEffectiveDate(); 
		
		if (StringUtils.isBlank(ipsIatEffectDate)) {
			IPSAndReviewDetailsDataValidator
					.addError(
							errors,
							IPSAndReviewDetailsDataValidator.IPS_IAT_EFFECTIVE_DATE_FORMAT_ERROR);

		} else {
			// IPS IAT Effective Date must be in mm/dd/yyyy Date pattern 
			Pattern datePattern = Pattern
					.compile("(^\\d{1,2})/(\\d{1,2})/(\\d{4})$");
			Matcher dateMatcher = datePattern
					.matcher(ipsIatEffectDate);
			if (!dateMatcher.find()) {
				
				IPSAndReviewDetailsDataValidator
						.addError(
								errors,
								IPSAndReviewDetailsDataValidator.IPS_IAT_EFFECTIVE_DATE_FORMAT_ERROR);
				ipsAssistServiceForm.setIpsIatEffectiveDate(null);
			}
		}
	}
	
	/**
	 * Validates IPS IAT Effective Date Range provided by the Trustee
	 * 
	 * @param expiryDate
	 * @param iatEffectiveDate
	 * @param iatEffectiveDateRange
	 * @param errors
	 * @param availabilityStatus
	 * @throws SystemException 
	 */
	public static void validateIATEffectiveDateRange(Date expiryDate,
			Date iatEffectiveDate, int iatEffectiveDateRange,
			List<GenericException> errors) throws SystemException {

		// Start date is current date + 45 days
		Calendar currentDateCal = Calendar.getInstance();
		currentDateCal.add(Calendar.DAY_OF_MONTH, iatEffectiveDateRange);

		// End date is expire date + 45 days
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(expiryDate);
		endDateCal.add(Calendar.DAY_OF_MONTH, iatEffectiveDateRange);

		Calendar ipsIatDateCal = Calendar.getInstance();
		ipsIatDateCal.setTime(iatEffectiveDate);
	
		// Reset the time
		IPSManagerUtility.resetTheTime(ipsIatDateCal);
		IPSManagerUtility.resetTheTime(currentDateCal);
		IPSManagerUtility.resetTheTime(endDateCal);
		
		// Checks for IPS IAT Effective Date is before Expiry Date and after Start Date
		if ((ipsIatDateCal.before(currentDateCal)
				|| ipsIatDateCal.after(endDateCal))) {
			IPSAndReviewDetailsDataValidator
					.addError(
							errors,
							IPSAndReviewDetailsDataValidator.IPS_INVALID_IPS_IAT_EFFECTIVE_DATE_RANGE_ERROR);
		}

	}
	
	/**
	 * Validate whether the IAT date is a valid business date
	 * 
	 * @param errors
	 * @param availabilityStatus
	 */
	public static void validateIATEffectiveDateFoNonBusinessDate(
			List<GenericException> errors, AvailabilityStatus availabilityStatus) {
		if((!availabilityStatus.isAvailable() && !availabilityStatus
						.isAvailableLaterToday())){
			IPSAndReviewDetailsDataValidator
			.addError(
					errors,
					IPSAndReviewDetailsDataValidator.IPS_NON_BUSINESS_IPS_IAT_EFFECTIVE_DATE_ERROR);
		}
	}
	
	/**
	 * Validate the IAT effective date
	 * 
	 * @param iatEffectiveDate
	 * @param fundMergeDate
	 * @param errors
	 */
	public static void validateIATEffectiveDateForFundMergeDate(Date iatEffectiveDate,
			Date fundMergeDate, List<GenericException> errors) {
		if (DateUtils.isSameDay(iatEffectiveDate, fundMergeDate)) {
			// validate whether the IAT effective date and fund merge date are
			// same. If so return error
			addError(errors, INVALID_IPS_IAT_EFFECTIVE_DATE_ERROR);
		}
	}
	
	/**
	 * Validates input Date
	 * 
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	protected boolean isValidDate(int day, int month, int year) {
	    if (month < 1 || month > 12) {
	              return false;
	    }
	    if (day < 1 || day > 31) {
	        return false;
	    }
	    if ((month == 4 || month == 6 || month == 9 || month == 11) &&
	        (day == 31)) {
	        return false;
	    }
	    if (month == 2) {
	        boolean leap = (year % 4 == 0 &&
	                   (year % 100 != 0 || year % 400 == 0));
	        if (day>29 || (day == 29 && !leap)) {
	            return false;
	        }
	    }
	    if (year < 1000) {
	    	return false;
	    }
	    
	    return true;
	}
	
	/**
	 * Check whether the error already exist, if not add the error
	 * to the passed list
	 * 
	 * @param errors
	 * @param exception
	 */
	public static void addError(List<GenericException> errors,
			GenericException exception) {
		if (!errors.contains(exception)) {
			errors.add(exception);
		}
	}
}
