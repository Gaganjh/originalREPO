package com.manulife.pension.ps.web.census.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.DeferralReportForm;
import com.manulife.pension.validator.ValidationError;

/**
 * The form field validation rules for the Deferral Report
 * 
 * @author patuadr
 * 
 */
public class DeferralReportValidationRules {
	public static String AUTO_INCREASE_FLAG = "autoIncreaseFlag401";
	public static String NEXT_ADI_YEAR = "nextADIYear";
	public static String INCREASE_TYPE = "increaseType";
	public static String INCREASE = "increase";
	public static String LIMIT = "limit";

	/**
	 * Combined edits done: #TBD, so far it is done in UI
	 * 
	 * @param errors
	 * @param element
	 * @return
	 */
	public static boolean validateNextADIYear(List<ValidationError> errors, DeferralDetails element, int optOutDays, Date previousDateNextADI) {
		boolean noError = true;

		if (element.getNextADIYear() == null)
			return true;
		if (hasDateChanged(element.getDateNextADI(), previousDateNextADI)) {
			if (element.getDateNextADI() != null) {
				if (DeferralUtils.beforeCurrentDate(element.getDateNextADI())) {
					errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.AnniversaryDateBeforeCurrentDate, new Object[] { getEmployeeName(element) }));
					element.setNextADIYearStatus(DeferralDetails.ERROR);
					noError = false;
				}
				if (DeferralUtils.beforeCurrentDate(DeferralUtils.getOptOutDate(optOutDays, element.getDateNextADI())) && DeferralUtils.afterCurrentDate(element.getDateNextADI())) {
					errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.NewAnniversaryDateOptOutDateConflict, new Object[] { getEmployeeName(element), new Integer(optOutDays) + " days" }));
					element.setNextADIYearStatus(DeferralDetails.ERROR);
					noError = false;
				}
			}

			// CIS.381
			int currentYear = GregorianCalendar.getInstance().get(Calendar.YEAR);
			if (Integer.parseInt(element.getNextADIYear()) > (currentYear + 2)) {
				errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.AnniversaryDateTooFarAway, new Object[] { getEmployeeName(element) }));
				element.setNextADIYearStatus(DeferralDetails.ERROR);
				noError = false;
			}

			if (previousDateNextADI != null) {
				if (DeferralUtils.beforeCurrentDate(DeferralUtils.getOptOutDate(optOutDays, previousDateNextADI)) && DeferralUtils.afterCurrentDate(previousDateNextADI)) {
					errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.AnniversaryDateOptOutDateConflict, new Object[] { getEmployeeName(element), new Integer(optOutDays) + " days" }));
					element.setNextADIYearStatus(DeferralDetails.ERROR);
					noError = false;
				}
			}
		}

		return noError;
	}

	private static boolean hasDateChanged(Date newDate, Date prevDate) {
		if (newDate == null) {
			return (prevDate != null);
		}

		return !newDate.equals(prevDate);
	}

	public static boolean validateNextADI(List<ValidationError> errors, DeferralDetails element, DeferralReportForm form) {
		// partial validation, rest is done in validateNextADIYear later
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);

		if (!"Y".equals(element.getAciSettingsInd()))
			return true;

		if (form.getDniEditState() == DeferralReportForm.DNI_EDIT_STATE_YEAR_ONLY) { // year
																						// edit
																						// only
			// Set the date correctly in given element, otherwise on changing
			// just the year
			// for a personalized AD, correct year won't reflect in further
			// validations
			Calendar cal = new GregorianCalendar();
			if (element.getDateNextADI() == null) {
				// Use form's NextADI element's date only if element's date is
				// null
				cal.setTime(form.getNextAnniversaryDateAsDate());
			} else {
				cal.setTime(element.getDateNextADI());
			}
			cal = DateUtils.truncate(cal, Calendar.DATE);

			if (element.getNextADIYear() != null) {
				// Change the year part of the element's nextADI based on input
				cal.set(Calendar.YEAR, Integer.parseInt(element.getNextADIYear()));
			}
			element.setDateNextADI(cal.getTime());
		} else if (form.getDniEditState() == DeferralReportForm.DNI_EDIT_STATE_FREE_FORM) { // free
																							// form
			try {
				// need to check for 02/29 first since dateFormat CAN NOT parse
				// an invalid date(such as a year with 02/29)
				if(element.getNextAD()!=null){	
				if ((element.getNextAD().length() > 6) && "02/29".equals(element.getNextAD().substring(0, 5)) || (element.getNextAD().length() > 5)
						&& "2/29".equals(element.getNextAD().substring(0, 4))) { // CIS.380
					errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.AnniversaryDateOnLeapYearDay, new Object[] { getEmployeeName(element) }));
					element.setNextADIYearStatus(DeferralDetails.ERROR);
					return false;
				}

				Date inputDate = dateFormat.parse(element.getNextAD());
				element.setDateNextADI(inputDate);
				element.setNextADIYear(dateFormat.format(inputDate).substring(6)); // setup for year validation which already uses this field
				}
			} catch (ParseException pe) { // CIS.379
				errors.add(new ValidationError(NEXT_ADI_YEAR, CensusErrorCodes.AnniversaryDateFuckedFormat, new Object[] { getEmployeeName(element) }));
				element.setNextADIYearStatus(DeferralDetails.ERROR);

				return false;
			}
		} else if (form.getDniEditState() == DeferralReportForm.DNI_EDIT_STATE_MULTI_SELECT) { // MULTI_SELECT
			// year stored directly in nextADIYear, just need to fix dateNextADI
			// which holds only mm/dd
			try {
				Date inputDate = dateFormat.parse(element.getNextADIMonthDay() + "/" + element.getNextADIYear());
				element.setDateNextADI(inputDate);
			} catch (ParseException pe) {
			} // select box input only
		}

		return true;
	}

	/**
	 * Combined edits done: # Error1: Before Tax Deferral + Increase amount is
	 * greater than Limit (personal) Warning 2: Before Tax Deferral + Increase
	 * AND ROTH amount is greater than Plan Limit AND Plan Limit is not blank
	 * Error 3:Increase amount and Limit must be greater than or equal to zero
	 * 
	 * @param errors
	 * @param element
	 * @return
	 */
	public static boolean validateIncrease(List<ValidationError> errors, DeferralDetails element, DeferralReportForm form) {
		boolean noError = true;
		double beforeTaxDeferralPct = 0;
		double beforeTaxDeferralAmt = 0;
		double increase = 0;
		double limit = 0;
		double planLimitPct = 0;
		double planLimitAmt = 0;
		double designatedRothPct = 0;
		double designatedRothAmt = 0;
		
		if ("Y".equals(element.getAciSettingsInd())) {
			if (element.getIncrease() == null || "".equals(element.getIncrease())) {
				// Nothing to validate in this case
				return noError;
			} else {
				try {
					increase = parseAmount(element.getIncrease());
				} catch (NumberFormatException e) {
					errors.add(new ValidationError(INCREASE, CensusErrorCodes.IncreaseAndLimitNotIntegers, new Object[] { getEmployeeName(element) }));
					element.setIncreaseStatus(DeferralDetails.ERROR);
					return false;
				}
			}
	
			if (increase < 0) {
				errors.add(new ValidationError(INCREASE, CensusErrorCodes.IncreaseAndLimitNotGreaterThanZero, new Object[] { getEmployeeName(element) }));
				element.setIncreaseStatus(DeferralDetails.ERROR);
				noError = false;
			}
	
			if (Constants.DEFERRAL_TYPE_PERCENT.equals(element.getIncreaseType())) {
				// Validate increase is numeric with no decimal point
				if (element.getIncrease().indexOf(".") >= 0) {
					errors.add(new ValidationError(INCREASE, CensusErrorCodes.IncreaseAndLimitNotIntegers, new Object[] { getEmployeeName(element) }));
					element.setIncreaseStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				// 1
				if (element.getBeforeTaxDeferralPct() != null) {
					try {
						beforeTaxDeferralPct = Double.parseDouble(element.getBeforeTaxDeferralPct());
					} catch (NumberFormatException e) {
						beforeTaxDeferralPct = 0;
					}
				}
	
				// Adjust for the changes performed in DAO
				beforeTaxDeferralPct = beforeTaxDeferralPct * 100;
	
				if (element.getLimit() == null || "".equals(element.getLimit())) {
					return noError;
				} else {
					try {
						limit = Double.parseDouble(element.getLimit());
					} catch (NumberFormatException e) {
						return false;
					}
				}
	
				if (beforeTaxDeferralPct + increase > limit) {
					// Report 2 errors in this case
					errors.add(new ValidationError(INCREASE, CensusErrorCodes.DeferralPlusIncreaseGreaterThanLimit, new Object[] { getEmployeeName(element) }));
					element.setIncreaseStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				// 2
				if (form.getPlanLimitPercent() != null) {
					try {
						planLimitPct = Double.parseDouble(form.getPlanLimitPercent());
					} catch (NumberFormatException e) {
						planLimitPct = 0;
					}
				}
	
				if (planLimitPct > 0) {
					if (element.getDesignatedRothDeferralPct() != null) {
						try {
							designatedRothPct = Double.parseDouble(element.getDesignatedRothDeferralPct());
						} catch (NumberFormatException e) {
							designatedRothPct = 0;
						}
					}
	
					// Adjust for the changes performed in DAO
					designatedRothPct = designatedRothPct * 100;
	
					if (beforeTaxDeferralPct + increase + designatedRothPct > planLimitPct) {
						if (!element.getWarnings().contains(DeferralReportData.CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING))
							element.getWarnings().add(DeferralReportData.CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING);
	
					}
				}
	
			} else {
				// 1
				if (element.getBeforeTaxDeferralAmt() != null) {
					try {
						beforeTaxDeferralAmt = Double.parseDouble(element.getBeforeTaxDeferralAmt());
					} catch (NumberFormatException e) {
						beforeTaxDeferralAmt = 0;
					}
				}
	
				if (element.getLimit() == null || "".equals(element.getLimit())) {
					return noError;
				
				} else {
					try {
						limit = parseAmount(element.getLimit());
					} catch (NumberFormatException e) {
						return false;
					}
				}
	
				if (beforeTaxDeferralAmt + increase > limit) {
					errors.add(new ValidationError(INCREASE, CensusErrorCodes.DeferralPlusIncreaseGreaterThanLimit, new Object[] { getEmployeeName(element) }));
					element.setIncreaseStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				// 2
				if (form.getPlanLimitAmount() != null) {
					try {
						planLimitAmt = Double.parseDouble(form.getPlanLimitAmount());
					} catch (NumberFormatException e) {
						planLimitAmt = 0;
					}
				}
	
				if (planLimitAmt > 0) {
					if (element.getDesignatedRothDeferralAmt() != null) {
						try {
							designatedRothAmt = Double.parseDouble(element.getDesignatedRothDeferralAmt());
						} catch (NumberFormatException e) {
							designatedRothAmt = 0;
						}
					}
	
					if (beforeTaxDeferralAmt + increase + designatedRothAmt > planLimitAmt) {
						if (!element.getWarnings().contains(DeferralReportData.CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING))
							element.getWarnings().add(DeferralReportData.CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING);
					}
				}
	
			}
		}
		return noError;
	}

	/**
	 * Combined edits done: # Error 1:Increase amount and Limit must be greater
	 * than or equal to zero Error 2:Personal Limit greater than plan limit
	 * Error 3:Limit is pct and is greater than 25%
	 * 
	 * @param errors
	 * @param element
	 * @return
	 */
	public static boolean validateLimit(List<ValidationError> errors, DeferralDetails element, DeferralReportForm form) {
		boolean noError = true;
		double limit = 0;
		double planLimitPct = 0;
		double planLimitAmt = 0;
		double increase = 0;
		double beforeTaxDeferralPct = 0;
		double beforeTaxDeferralAmt = 0;
		double catchupContriAmt = 0;

		if ("Y".equals(element.getAciSettingsInd())) {
			if (element.getLimit() == null || "".equals(element.getLimit())) {
				if (form.getAciDefaultDeferralLimitByPercent() != null) {
					try {
						limit = Double.parseDouble(form.getAciDefaultDeferralLimitByPercent());
					} catch (NumberFormatException e) {
						limit = 0;
					}
				}
			} else {
				try {
					// participants defderral max limit
					limit = parseAmount(element.getLimit());
				} catch (NumberFormatException e) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.IncreaseAndLimitNotIntegers, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					return false;
				}
			}
			
			catchupContriAmt = form.getCatchupContriAmt().doubleValue();
			
			if (limit < 0) {
				errors.add(new ValidationError(LIMIT, CensusErrorCodes.IncreaseAndLimitNotGreaterThanZero, new Object[] { getEmployeeName(element) }));
				element.setLimitStatus(DeferralDetails.ERROR);
				noError = false;
			}
	
			if (Constants.DEFERRAL_TYPE_PERCENT.equals(element.getIncreaseType())) {
				// Validate limit is numeric with no decimal point
				if (element.getLimit()!=null && element.getLimit().indexOf(".") >= 0) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.IncreaseAndLimitNotIntegers, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				if (element.getIncrease() == null || "".equals(element.getIncrease())) {
					if (form.getAciDefaultDeferralIncreaseByPercent() != null) {
						try {
							increase = Double.parseDouble(form.getAciDefaultDeferralIncreaseByPercent());
						} catch (NumberFormatException e) {
							increase = 0;
						}
					}
	
				} else {
					try {
						increase = Double.parseDouble(element.getIncrease());
					} catch (NumberFormatException e) {
						return false;
					}
				}
	
				if (form.getPlanLimitPercent() != null) {
					try {
						planLimitPct = Double.parseDouble(form.getPlanLimitPercent());
					} catch (NumberFormatException e) {
						planLimitPct = 0;
					}
				}
	
				if (limit > 25 && planLimitPct == 0) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.PersonalLimitGreaterThan25pct, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				if (planLimitPct > 0 && limit > planLimitPct) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.PersonalLimitGreaterThanPlanLimit, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				if (element.getBeforeTaxDeferralPct() != null) {
					try {
						beforeTaxDeferralPct = Double.parseDouble(element.getBeforeTaxDeferralPct());
					} catch (NumberFormatException e) {
						beforeTaxDeferralPct = 0;
					}
				}
	
				// Adjust for the changes performed in DAO
				beforeTaxDeferralPct = beforeTaxDeferralPct * 100;
	
				if (beforeTaxDeferralPct + increase > limit) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.PersonalLimitIsLessThanCurrentContribPlusIncrease, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
			} else {
				if (element.getIncrease() == null || "".equals(element.getIncrease())) {
					if (form.getAciDefaultDeferralIncreaseByAmount() != null) {
						try {
							increase = Double.parseDouble(form.getAciDefaultDeferralIncreaseByAmount());
						} catch (NumberFormatException e) {
							increase = 0;
						}
					}
	
				} else {
					try {
						increase = parseAmount(element.getIncrease());
					} catch (NumberFormatException e) {
						return false;
					}
				}
				if (element.getLimit() == null || "".equals(element.getLimit())) {
	
					if (form.getAciDefaultDeferralLimitByAmount() != null) {
						try {
							limit = Double.parseDouble(form.getAciDefaultDeferralLimitByAmount());
						} catch (NumberFormatException e) {
							limit = 0;
						}
					}
					}
				 else {
						try {
							limit = parseAmount(element.getLimit());
						} catch (NumberFormatException e) {
							return false;
						}
					}
				
	
				if (form.getPlanLimitAmount() != null) {
					try {
						planLimitAmt = Double.parseDouble(form.getPlanLimitAmount());
					} catch (NumberFormatException e) {
						planLimitAmt = 0;
					}
				}
	
				if (planLimitAmt > 0 && limit + catchupContriAmt > planLimitAmt) {
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.PersonalLimitGreaterThanPlanLimit, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
	
				if (element.getBeforeTaxDeferralAmt() != null) {
					try {
						beforeTaxDeferralAmt = Double.parseDouble(element.getBeforeTaxDeferralAmt());
					} catch (NumberFormatException e) {
						beforeTaxDeferralAmt = 0;
					}
				}
	
				if (beforeTaxDeferralAmt + increase > limit) {
					// Report 2 errors in this case
					errors.add(new ValidationError(LIMIT, CensusErrorCodes.PersonalLimitIsLessThanCurrentContribPlusIncrease, new Object[] { getEmployeeName(element) }));
					element.setLimitStatus(DeferralDetails.ERROR);
					noError = false;
				}
			}
		}
		return noError;
	}

	private static Double parseAmount(String amount) throws NumberFormatException {
		String amt = amount.trim();
		int index = amt.indexOf(',');
		if (index != -1) {
			if (index < 1 || index > 2)
				return null;
			else
				amt = amt.substring(0, index) + amt.substring(index + 1);
		}
		Double am = Double.valueOf(amt);
		return am;
	}

	/**
	 * Returns the employee names trimmed, to be inserted into the error message
	 * 
	 * @param employeeDetails
	 * @return
	 */
	public static String getEmployeeName(DeferralDetails employeeDetails) {

		StringBuffer buffer = new StringBuffer();
		buffer.append(CensusUtils.processString(employeeDetails.getLastName()));
		buffer.append(", ");
		buffer.append(CensusUtils.processString(employeeDetails.getFirstName()));
		if (employeeDetails.getMiddleInitial() != null && !"".equals(employeeDetails.getMiddleInitial().trim())) {
			buffer.append(" ");
			buffer.append(CensusUtils.processString(employeeDetails.getMiddleInitial()));
		}

		return buffer.toString();

	}

}
