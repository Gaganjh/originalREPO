package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.util.content.GenericException;
import java.util.GregorianCalendar;

@Component
public class RateOfReturnCalculatorValidator implements Validator {
private static Logger logger = Logger.getLogger(RateOfReturnCalculatorValidator.class);
public static final int DATE_INVALID = -1;

public static final int DATE_EMPTY = -2;



public static final int DAY_INVALID = 1;

public static final int MONTH_INVALID = 2;

public static final int YEAR_INVALID_MIN = 4;
public static final String BACK = "back";

public static final String RESET_BUTTON = "Reset";

public static final String RESET = "reset";
public static final int YEAR_INVALID = 5;
public static final String CALCULATE_ADHOC_ROR_FORM = "rateOfReturnCalculatorForm";
public static final int JANUARY_MAX = 31;

public static final int FEBRUARY_MAX = 28;

public static final int FEBRUARY_MAXLEAP = 29;

public static final int MARCH_MAX = 31;

public static final int APRIL_MAX = 30;
public static final String EMPTY = StringUtils.EMPTY;
public static final int MAY_MAX = 31;

public static final int JUNE_MAX = 30;

public static final int JULY_MAX = 31;

public static final int AUGUST_MAX = 31;
public static final String DEFAULT = "default";
public static final int SEPTEMBER_MAX = 30;

public static final int OCTOBER_MAX = 31;

public static final int NOVEMBER_MAX = 30;

public static final int DECEMBER_MAX = 31;
public static final int DATE_VALID = 0;
public static final int MIN_YEAR = 1950;

public static final int MAX_YEAR = 3000;
	@Override
	public boolean supports(Class<?> clazz) {
		return RateOfReturnCalculatorForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection error = new ArrayList();
		
		BindingResult bindingResult = (BindingResult)errors;
		String[] errorCodes = new String[10];
   		if(!bindingResult.hasErrors()){
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) target;
	
		String action = rateOfReturnCalculatorForm.getAction();
        // if page type is equal to default page then return with zero errors.
        if (null == action || StringUtils.isBlank(action) || StringUtils.equals(DEFAULT, action)
                || StringUtils.equals(BACK, action) || StringUtils.equals(RESET, action)) {
            SessionHelper.removeErrorsInSession(request);
            return;
        }
        
        // update the form in session
        HttpSession session = request.getSession(false);
        // if session has expired return without errors and let doPeform handle
        // warning
        if (session != null) {

            // reset for new calculations
            rateOfReturnCalculatorForm.setResultsCalculatedFlag(false);
            rateOfReturnCalculatorForm.setWarningMessageFlag(false);

            // put form in session
            request.getSession(false).setAttribute(CALCULATE_ADHOC_ROR_FORM,
                    rateOfReturnCalculatorForm);

            // check if time period from today radio button has been
            // selected
            if (!(null == rateOfReturnCalculatorForm.getTimePeriodFromToday())
                    && !("".equals(rateOfReturnCalculatorForm.getTimePeriodFromToday()))
                    && !(StringUtils.equals(rateOfReturnCalculatorForm.getTimePeriodFromToday(),
                            EMPTY))) {
                // check if time period from today radio button has a valid
                // value
                if (RateOfReturnCalculatorForm.VALID_TIME_PERIOD_FROM_TODAY_VALUES
                        .contains(rateOfReturnCalculatorForm.getTimePeriodFromToday())) {
                    // check if user supplied dates or we need to generate
                    // dates
                    if (RateOfReturnCalculatorForm.TIME_PERIOD_FROM_DATE_RANGE
                            .equals(rateOfReturnCalculatorForm.getTimePeriodFromToday())) {

                        // validate start date

                        int result1 = isValidDate(rateOfReturnCalculatorForm.getStartYear(),
                                rateOfReturnCalculatorForm.getStartMonth(),
                                rateOfReturnCalculatorForm.getStartDay());
                        if ((result1 == DATE_EMPTY)) {
                        	error.add(new GenericException(
                                    ErrorCodes.ERROR_CALCADHOCROR_START_DATE_REQUIRED));
                        } else {
                            if (result1 == DATE_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_START_DATE_REQUIRED));
                            } else if (result1 == YEAR_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_START_DATE_1950));
                            } else if (result1 == MONTH_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_START_DATE_INVALID));
                            } else if (result1 == DAY_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_START_DATE_INVALID_1));
                            }

                        }

                        int result2 = isValidDate(rateOfReturnCalculatorForm.getEndYear(),
                                rateOfReturnCalculatorForm.getEndMonth(),
                                rateOfReturnCalculatorForm.getEndDay());

                        if ((result2 == DATE_EMPTY)) {
                        	error.add(new GenericException(
                                    ErrorCodes.ERROR_CALCADHOCROR_END_DATE_REQUIRED));
                        } else {
                            if (result2 == DATE_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_END_DATE_REQUIRED));
                            } else if (result2 == MONTH_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_END_DATE_INVALID));
                            } else if (result2 == DAY_INVALID) {
                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_END_DATE_INVALID_1));
                            }

                        }

                        // dates are valid go on and do additional date
                        // checks
                        if (error.size() == 0) {
                            Calendar calendarEndDate = Calendar.getInstance();
                            calendarEndDate.set(
                                    Integer.parseInt(rateOfReturnCalculatorForm.getEndYear()),
                                    Integer.parseInt(rateOfReturnCalculatorForm.getEndMonth()) - 1,
                                    Integer.parseInt(rateOfReturnCalculatorForm.getEndDay()));

                            Calendar calendarStartDate = Calendar.getInstance();
                            calendarStartDate
                                    .set(Integer
                                            .parseInt(rateOfReturnCalculatorForm.getStartYear()),
                                            Integer.parseInt(rateOfReturnCalculatorForm
                                                    .getStartMonth()) - 1, Integer
                                                    .parseInt(rateOfReturnCalculatorForm
                                                            .getStartDay()));

                            Calendar today = Calendar.getInstance();

                            // end date is today or later then today
                           if (calendarEndDate.compareTo(today)>=0) { 

                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_END_DATE_LATER_THEN_TODAY));
                            }

                            // start date later then end date
                            if (calendarStartDate.after(calendarEndDate)) {

                            	error.add(new GenericException(
                                        ErrorCodes.ERROR_CALCADHOCROR_START_DATE_LATER_THEN_END_DATE));
                            }
                        }
                    }
                } else {
                    // radio button is invalid
                    error.add(new GenericException(
                            ErrorCodes.ERROR_CALCADHOCROR_TIME_PERIOD_FROM_TODAY_INVALID));
                }
            } else {
                // no radio button selected
            	error.add(new GenericException(
                        ErrorCodes.ERROR_CALCADHOCROR_TIME_PERIOD_NOT_SELECTED));
            }

            if (error.size() > 0) {
                SessionHelper.setErrorsInSession(request, error);
                
                for (Object e : error) {
					if (e instanceof GenericException) {
						GenericException errorEx=(GenericException) e;
						errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
						bindingResult.addError(new ObjectError(errors
								                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					}
						
					}
                if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
			    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
			    }
                
                request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
    			request.removeAttribute(PsBaseUtil.ERROR_KEY);
    			request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
     			request.setAttribute(PsBaseUtil.ERROR_KEY, error);
            }
        }
   		}
		
		
		
		
		
	}

	private int isValidDate(String year, String month, String day) {
		if ((year == null && month == null && day == null)
                || (year.trim().length() == 0 && month.trim().length() == 0 && day.trim().length() == 0)) {
            return DATE_EMPTY;
        }
        try {
            return isValidDateRange(Integer.parseInt(year), Integer.parseInt(month),
                    Integer.parseInt(day));
        } catch (Exception e) {
            return DATE_INVALID;
        }
	}
	
	public static int isValidDateRange(int year, int month, int day) {
        int valid = DATE_VALID;

        GregorianCalendar today = new GregorianCalendar();

        /* sanity check. detailed check should be done separately */
        if (year < MIN_YEAR || year > MAX_YEAR) {
            valid = valid | YEAR_INVALID;
        }
        switch (month) {
            case 1:
                if (day > JANUARY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 2:
                if (today.isLeapYear(year)) {
                    if (day > FEBRUARY_MAXLEAP)
                        valid = valid | DAY_INVALID;
                } else {
                    if (day > FEBRUARY_MAX)
                        valid = valid | DAY_INVALID;
                }
                break;
            case 3:
                if (day > MARCH_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 4:
                if (day > APRIL_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 5:
                if (day > MAY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 6:
                if (day > JUNE_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 7:
                if (day > JULY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 8:
                if (day > AUGUST_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 9:
                if (day > SEPTEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 10:
                if (day > OCTOBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 11:
                if (day > NOVEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 12:
                if (day > DECEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            default:
                valid = valid | MONTH_INVALID;
        }

        return valid;
    }
}
