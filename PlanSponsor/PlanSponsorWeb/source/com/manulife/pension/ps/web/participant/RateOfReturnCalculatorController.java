package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.AdhocRORException;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.service.account.valueobject.AdhocROROutput;
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping(value ="/db")
@SessionAttributes({"rateOfReturnCalculatorForm"})

public class RateOfReturnCalculatorController extends PsAutoController {

	@ModelAttribute("rateOfReturnCalculatorForm")
public  RateOfReturnCalculatorForm  populateForm()
	{
		return new  RateOfReturnCalculatorForm ();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/rateofReturnCalculator.jsp"); 
		forwards.put("default","/participant/rateofReturnCalculator.jsp");
		forwards.put("calculate","/participant/rateofReturnCalculator.jsp"); 
		forwards.put("reset","/participant/rateofReturnCalculator.jsp");
		forwards.put("back","redirect:/do/db/definedBenefitAccount/");}

    public static final String CALCULATE_ADHOC_ROR_FORM = "rateOfReturnCalculatorForm";

    public static final String CALCULATE_BUTTON = "Calculate";

    public static final String DEFAULT = "default";

    public static final String CALCULATE = "calculate";

    public static final String BACK_BUTTON = "Back";

    public static final String BACK = "back";

    public static final String RESET_BUTTON = "Reset";

    public static final String RESET = "reset";

    public static final String HOME_BUTTON = "Home";

    public static final String HOME = "home";

    public static final String EMPTY = StringUtils.EMPTY;

    public static final int DATE_VALID = 0;

    public static final int DATE_INVALID = -1;

    public static final int DATE_EMPTY = -2;

    public static final int DAY_INVALID = 1;

    public static final int MONTH_INVALID = 2;

    public static final int YEAR_INVALID_MIN = 4;

    public static final int YEAR_INVALID = 5;

    public static final int JANUARY_MAX = 31;

    public static final int FEBRUARY_MAX = 28;

    public static final int FEBRUARY_MAXLEAP = 29;

    public static final int MARCH_MAX = 31;

    public static final int APRIL_MAX = 30;

    public static final int MAY_MAX = 31;

    public static final int JUNE_MAX = 30;

    public static final int JULY_MAX = 31;

    public static final int AUGUST_MAX = 31;

    public static final int SEPTEMBER_MAX = 30;

    public static final int OCTOBER_MAX = 31;

    public static final int NOVEMBER_MAX = 30;

    public static final int DECEMBER_MAX = 31;

    /* constants for sanity check */
    public static final int MIN_YEAR = 1950;

    public static final int MAX_YEAR = 3000;

    public RateOfReturnCalculatorController() {
        super(RateOfReturnCalculatorController.class);
    }

    @RequestMapping(value ="/rateOfReturnCalculator/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	actionForm.reset(request);
        actionForm.setRateOfReturn("");
        actionForm.setResultsCalculatedFlag(false);
        return forwards.get(DEFAULT);
    }

    @RequestMapping(value ="/rateOfReturnCalculator/", params={"action=back"}  , method =  {RequestMethod.POST}) 
    public String doBack (@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    
        HttpSession session = request.getSession(false);
        RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) form;
        session.removeAttribute(CALCULATE_ADHOC_ROR_FORM);
        rateOfReturnCalculatorForm.reset( request);
        return forwards.get(BACK);
    }

    @RequestMapping(value ="/rateOfReturnCalculator/" ,params={"action=reset"}   , method =  {RequestMethod.POST}) 
    public String doReset (@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        HttpSession session = request.getSession(false);
        session.removeAttribute(CALCULATE_ADHOC_ROR_FORM);
        RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) form;
        rateOfReturnCalculatorForm.reset( request);
        return forwards.get(RESET);
    }

    @RequestMapping(value ="/rateOfReturnCalculator/", params={"action=calculate"} , method =  {RequestMethod.POST}) 
    public String doCalculate (@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
        RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) form;
        HttpSession session = request.getSession(false);
        String forward = null;

        float rs = 0;

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

        // get the form

        rateOfReturnCalculatorForm.setResultsCalculatedFlag(false);

        UserProfile userProfile = getUserProfile(request);
        // handle cacluate function

        if (RateOfReturnCalculatorForm.TIME_PERIOD_FROM_DATE_RANGE
                .equals(rateOfReturnCalculatorForm.getTimePeriodFromToday())) {

            // user entered dates
            startDate = new GregorianCalendar(Integer.parseInt(rateOfReturnCalculatorForm
                    .getStartYear()),
                    Integer.parseInt(rateOfReturnCalculatorForm.getStartMonth()) - 1,
                    Integer.parseInt(rateOfReturnCalculatorForm.getStartDay()));
            endDate = new GregorianCalendar(Integer.parseInt(rateOfReturnCalculatorForm
                    .getEndYear()), Integer.parseInt(rateOfReturnCalculatorForm.getEndMonth()) - 1,
                    Integer.parseInt(rateOfReturnCalculatorForm.getEndDay()));

        } else {
            // end date is previous date
            endDate = new GregorianCalendar();
			endDate.add(Calendar.DAY_OF_YEAR, -1); 
            // calculate start date
            if (RateOfReturnCalculatorForm.TIME_PERIODS_REQUIRE_CALCULATION
                    .contains(rateOfReturnCalculatorForm.getTimePeriodFromToday())) {

                // start date is 1 month, 3 month ...from today
                startDate.setTime(addMonth(
                        Integer.parseInt(rateOfReturnCalculatorForm.getTimePeriodFromToday())
                                * (-1), new GregorianCalendar().getTime()));
            } else {
                // must be YTD so make start date jan 1 of this year
                startDate.set(endDate.get(Calendar.YEAR), 0, 1);
            }
        }

        // setup result dates for form

        rateOfReturnCalculatorForm.setResultStartDate(df.format(startDate.getTime()));
        rateOfReturnCalculatorForm.setResultEndDate(df.format(endDate.getTime()));

        try {

            AccountServiceDelegate accountService = AccountServiceDelegate.getInstance();

            String contractNumber = Integer.toString(userProfile.getCurrentContract()
                    .getContractNumber());
            String profileId = Long.toString(userProfile.getPrincipal().getProfileId());
            ParticipantListVO participantList = ContractServiceDelegate.getInstance()
                    .getParticipantList(Integer.parseInt(contractNumber), "1");
            ArrayList<ParticipantVO> list = (ArrayList<ParticipantVO>) participantList
                    .getParticipants();

            String participantId = list.get(0).getProfileId();
            // TODO: currently passing null for principal as it is not
            // required in account service
            AdhocROROutput arOutput = accountService.executeAdhocRORTransaction(null,
                    startDate.getTime(), endDate.getTime(), participantId, contractNumber);

            int returnCode = arOutput.getErrorCode();

            if (returnCode == 4702) {
                processError(request, "***ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY***" + returnCode,
                        ErrorCodes.ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY);
            } else if (returnCode == 4703) {
                processError(request,
                        "***ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END***"
                                + returnCode,
                        ErrorCodes.ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END);
            } else if (returnCode == 4704) {
                processError(request, "***ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS***"
                        + returnCode,
                        ErrorCodes.ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS);
            } else {

                BigDecimal rateOfReturn = arOutput.getRateOfReturn();
                if (rateOfReturn != null) {

                    rs = rateOfReturn.floatValue();
                    rateOfReturnCalculatorForm.setResultsCalculatedFlag(true);

                    rateOfReturnCalculatorForm.setRateOfReturn(String.valueOf(rs));

                    // handle special case where warning message should show
                    // just after ROR result
                    if (returnCode == 3404) {
                        rateOfReturnCalculatorForm.setWarningMessageFlag(true);
                    }
                } else {

                    String systemOutMsg = null;
                    String errorMsg = arOutput.getErrorMessage();
                    if ((returnCode != 0) && (errorMsg != null)) {
                        systemOutMsg = "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***" + " "
                                + String.valueOf(returnCode) + " " + errorMsg;
                    } else {
                        systemOutMsg = "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***";
                    }

                    processError(request, systemOutMsg,
                            ErrorCodes.ERROR_CALCADHOCROR_MF_GENERAL_ERROR);
                }
            }

        } catch (AdhocRORException e) {
            processError(request, "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***",
                    ErrorCodes.ERROR_CALCADHOCROR_MF_GENERAL_ERROR);

        } catch (SystemUnavailableException e) {
            processError(request, "***ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS***",
                    ErrorCodes.ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS);

        } catch (SystemException e) {
            processError(request, "***SYSTEM_UNAVAILABLE***", ErrorCodes.SYSTEM_UNAVAILABLE);
        }

        forward = forwards.get(CALCULATE);

        return forward;
    }
    
   

    /**
     * This method validates the parameters' values
     * 
     * @param mapping ActionMapping object
     * 
     * @param request HttpServletRequest object
     * 
     * @return Returns the ActionErrors object
     */
   

    /**
     * Empty and Null check for the date fields
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int isValidDate(String year, String month, String day) {
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

    /**
     * Month and year validation
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
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

    /**
     * Utility method for calculating number of months in range of selection
     * 
     * @param months
     * @param date
     * @return
     */
    public static Date addMonth(int months, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Processing errors
     * 
     * @param request
     * @param systemOutMsg
     * @param errorCode
     */
    @SuppressWarnings("unchecked")
    protected void processError(HttpServletRequest request, String systemOutMsg, int errorCode) {
        ArrayList errors = new ArrayList();

        request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, errors);
        errors.add(new GenericException(errorCode));
    }
    
    @Autowired RateOfReturnCalculatorValidator rateOfReturnCalculatorValidator;
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(rateOfReturnCalculatorValidator);
	}
}
