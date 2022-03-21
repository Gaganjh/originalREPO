package com.manulife.pension.ps.web.transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
@Component
public class ParticipantTransactionHistoryValidator extends ValidatorUtil implements Validator {
	
private static Logger logger = Logger.getLogger(ParticipantTransactionHistoryValidator.class);
protected static final String FILTER_TASK = "filter";
public static final String ALL_TYPES = "ALL";
public static final String NULL = "null";
	@Override
	public boolean supports(Class<?> clazz) {
		return ParticipantTransactionHistoryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Collection error = new ArrayList();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = getUserProfile(request);
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) target;

		if (getTask(request).equals(FILTER_TASK)) {
			Date fromDate = new Date();	
			Date toDate = new Date();
			boolean validDates = false;
			boolean validFromDate = false;
			boolean validFromDateRange = false;
			Date asOfDate = getBalanceAsOfDate(request);
			Date cutOffDate = getMax24MonthsCutOffDate(asOfDate);
			
			// FROM date empty
			if((StringUtils.isEmpty(theForm.getFromDate()))  && (!StringUtils.isEmpty(theForm.getToDate()))) {
				error.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
			}	
			
			// Both dates empty		
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				error.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
			}	
			
			// Valid date format
			if ((theForm.getToDate().trim().length() > 0) && (theForm.getFromDate().trim().length() > 0)){
				
				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
					validFromDate = true;
				} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
					validFromDate = false;
				}        	
			}
					
			// Empty FROM date, invalid TO date
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (theForm.getToDate().trim().length() > 0)){
				try {
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
				} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
				}        	
			}
						
			// Invalid FROM date format, empty TO date
			if ((theForm.getFromDate().trim().length() > 0) && (theForm.getToDate().trim().length() == 0)){
				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					validFromDate = true;
				} catch(Exception e) {
					error.add(new GenericException(ErrorCodes.INVALID_DATE));
					validFromDate = false;
				}        	
			}
			
			// Valid FROM date, empty TO date, and FROM date greater than default TO date
			if (validFromDate){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}	
				}
			}				
					
			// Valid FROM date, empty TO date, and FROM date not within the last 24 months of default TO date
			if (validFromDate){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}	
				}
			}
			
			
			// If from date valid, date range valid, and to date is empty, then set default TO Date
			if (validFromDateRange){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(asOfDate);
					theForm.setToDate(dateFormatter(cal.getTime()));
				}
			}		
			
			//From date must be earlier than To date
			if (validDates){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						error.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
					}
				}	
			}	
			
			// From date outside 24 month range
			if (validDates){
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						if (request.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
							// We're coming from the contract level transaction history page so adjust to valid date range.
							Calendar cal = Calendar.getInstance();
							cal.setTime(cutOffDate);
							theForm.setFromDate(dateFormatter(cal.getTime()));
						} else {
							error.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						}
					}
				}
			}
		}
		
		// Reset the information for JSP to display.
		if (error.size() > 0) {
			//TODO
		
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		
		
	}
	protected Date getBalanceAsOfDate(HttpServletRequest request) {	
		return getUserProfile(request).getCurrentContract().getContractDates().getAsOfDate();
	}
private Date getMax24MonthsCutOffDate(Date asOfDate) {
		
		Calendar cal = Calendar.getInstance();
 		cal.setTime(asOfDate);
 		cal.add(Calendar.YEAR, -2);

		return cal.getTime();
	}
//synchronized method to avoid race condition.
	protected static synchronized String dateFormatter(Date inputDate) {
		return sdf.format(inputDate);
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat(ParticipantTransactionHistoryForm.FORMAT_DATE_SHORT_MDY);

	static {
		sdf.setLenient(false);
	}
	private Date validateDateFormat(String dateString) throws ParseException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateDateFormat");
		}

		Date validDate = null;

		if (!((dateString.trim().length() == 10) && (dateString.substring(2, 3).equals("/"))
				&& (dateString.substring(5, 6)).equals("/"))) {
			throw new ParseException("invalid date format", 0);
		}

		String month = dateString.substring(0, 2);
		String day = dateString.substring(3, 5);
		String year = dateString.substring(6, 10);

		try {
			if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
				throw new ParseException("invalid month", 0);

			if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
				throw new ParseException("invalid day", 0);

			if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2) && (Integer.parseInt(year) % 4 > 0))
				throw new ParseException("invalid day", 0);
		} catch (Exception e) {
			throw new ParseException("invalid date format", 0);
		}

		validDate = DateParser(dateString);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- validateDateFormat");
		}
		return validDate;
	}

	public void populateParticipantDetails(BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		ParticipantAccountVO participantAccountVO = null;
		ParticipantAccountDetailsVO participantDetailsVO = null;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		String productId = userProfile.getCurrentContract().getProductId();

		Principal principal = getUserProfile(request).getPrincipal();
		participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(principal, contractNumber,
				productId, theForm.getProfileId(), null, false, false);
		participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();

		request.setAttribute("details", participantDetailsVO);

		theForm.setShowAge(participantDetailsVO.getBirthDate() != null);
		theForm.setShowPba(currentContract.isPBA() || participantDetailsVO.getPersonalBrokerageAccount() > 0);
		theForm.setShowLoans(participantDetailsVO.getLoanAssets() > 0);

		// loan details drop down
		Collection loans = participantAccountVO.getLoanDetailsCollection();
		theForm.setLoanDetailList(loans);

		if (loans.size() == 1) {
			Iterator loansIt = loans.iterator();
			if (loansIt.hasNext()) {
				ParticipantLoanDetails loanDetails = (ParticipantLoanDetails) loansIt.next();
				theForm.setSelectedLoan(loanDetails.getLoanId());
			}
		}
	}
	
	/**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }
    
    protected static final String TASK_KEY = "task";
    protected static final String DEFAULT_TASK = "default";
    
    protected static synchronized Date DateParser(String value) throws ParseException {
		return sdf.parse(value);
	}
    public void populateProfileId(ParticipantTransactionHistoryForm theForm, int contractNumber)
			throws SystemException {

		if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {

			// common log 78460 lookup profileId by particiapnt id and contract number
			if (theForm.getParticipantId() != null && theForm.getParticipantId().length() > 0) {
				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
				theForm.setProfileId(asd.getProfileIdByParticipantIdAndContractNumber(theForm.getParticipantId(),
						Integer.toString(contractNumber)));
			}

			if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {
				Exception ex = new Exception("Failed to get the profileId");
				throw new SystemException(ex, this.getClass().getName(), "populateProfileId",
						"Failed to get profileId on form " + theForm.toString());
			}
		}
	}


}
