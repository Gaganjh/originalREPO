package com.manulife.pension.bd.web.bob.participant;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.commons.lang.StringUtils;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.FrwValidation;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.investment.InvestmentAllocationPageForm;
import com.manulife.pension.bd.web.bob.investment.InvestmentAllocationReportValidator;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails;
import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.service.account.valueobject.GiflMarketValue;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.TradeRestriction;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
@Component
public class ParticipantBenefitBaseInformationValidator extends ValidatorUtil implements Validator {
	public static final String YES = "Y";
	 protected static final String FILTER_TASK = "filter";
	private static Logger logger = Logger.getLogger(ParticipantBenefitBaseInformationValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return ParticipantBenefitBaseInformationForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;		
		Collection<GenericException> error = new ArrayList();
		ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm = (ParticipantBenefitBaseInformationForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
	
	
    if (logger.isDebugEnabled()) {
        logger.debug("entry -> doValidate()");
    }
    
 // This code has been changed and added  to 
	 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	
    // Do not validate when first time coming through Default From/To dates
    // are set in populateReportForm() after the doValidate()

  

    ArrayList<GenericException> transactionErrors = new ArrayList<GenericException>();

    

    // Get the task for From date and To date in Transaction history
    if (getTask(request).equals(FILTER_TASK)) {
        transactionErrors = (ArrayList<GenericException>) validateTransactionHistory(
                participantBenefitBaseInformationForm, request);
    }

    error.addAll(transactionErrors);

    try {
        if (request
                .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID) != null) {
            participantBenefitBaseInformationForm
                    .setProfileId((String) request
                            .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
        }
        retrieveParticipantDetails(participantBenefitBaseInformationForm
                .getProfileId(), request);
        getParticipantBenefitBaseAccountDetails(
                (BaseReportForm) participantBenefitBaseInformationForm, request);
        
        getParticipantLIADetails(participantBenefitBaseInformationForm
				.getProfileId(), request);
    } catch (SystemException se) {
        request.setAttribute(BDConstants.BENEFIT_DETAILS,
                new ParticipantBenefitBaseDetails());
        request.setAttribute(BDConstants.ACCOUNT_DETAILS,
                new WebParticipantGiflData());
        request.setAttribute(BDConstants.LIA_DETAILS,
				new LifeIncomeAmountDetailsVO());

        // Log the system exception.
        LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, se);
        
        // Show user friendly message.
        error.clear();
        error.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
        request.setAttribute(BDConstants.TECHNICAL_DIFFICULTIES, YES);
    }
}
	protected static BobContext getBobContext(HttpServletRequest request) {
		return BDController.getBobContext(request);
	}
	private void getParticipantLIADetails(String profileId,
			HttpServletRequest request) throws SystemException {
		LifeIncomeAmountDetailsVO liaDetails = ContractServiceDelegate
				.getInstance().getLIADetailsByProfileId(profileId);
		request.setAttribute(BDConstants.LIA_DETAILS, liaDetails);

	}
	 public String getDefaultFromDate(Date lastETLRunDate) {

	        // Create a Calendar object for the last ETL run date
	        Calendar lastETLCalendar = Calendar.getInstance();
	        lastETLCalendar.setTime(lastETLRunDate);

	        // Subtracting one month from the last ETL run date
	        lastETLCalendar.add(Calendar.MONTH, -1);

	        // Return the calculated default from date as a String
	        return DateRender.formatByPattern(lastETLCalendar.getTime(), "",
	                RenderConstants.MEDIUM_MDY_SLASHED);
	    }
	private void getParticipantBenefitBaseAccountDetails(
            BaseReportForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getParticipantBenefitBaseAccountDetails");
        }
        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();
        int contractNumber = currentContract.getContractNumber();

        ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

        int participantIdInt = ParticipantServiceDelegate.getInstance()
                .getParticipantIdByProfileId(
                        Long.parseLong(theForm.getProfileId()), contractNumber);
        theForm.setParticipantId(String.valueOf(participantIdInt));
        theForm.setProposalNumber(currentContract.getProposalNumber());

        ParticipantGiflData participantGiflData = AccountServiceDelegate
                .getInstance().getParticipantGiflDetails(
                        String.valueOf(contractNumber),
                        theForm.getParticipantId());
        if (participantGiflData == null) {
            StringBuffer exceptionMessage = new StringBuffer();
            exceptionMessage.append("Participant belongs to contractId:");
            exceptionMessage.append(contractNumber);
            exceptionMessage.append(" ,profileId:");
            exceptionMessage.append(theForm.getProfileId());
            exceptionMessage.append(" has never selected gateway");

            throw new SystemException(exceptionMessage.toString());
        }
        //To format the dates in a different format for NBDW
        WebParticipantGiflData webGiflData = WebParticipantGiflDataHelper.transformToWebFormat(participantGiflData);
        webGiflData.setParticipantId(new BigDecimal(theForm
                .getParticipantId()));

        // set the default from date
        if (theForm.getFromDate() == null) {
            theForm.setFromDate(getDefaultFromDate(webGiflData
                    .getGiflSelectionDate()));
        }

        // set the gifl selection date as participant's latest GIFL selection date
        theForm.setGiflSelectionDate(
                DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(),
                        "", RenderConstants.MEDIUM_MDY_SLASHED));
        
        // Get the Trade Restriction Expiration Date and whether this date is in
        // effect
        try {
            request.setAttribute(BDConstants.TRADING_RESTRICTION,
                    getTradingRestriction(Long.valueOf(participantIdInt),
                            Integer
                                    .valueOf(currentContract
                                            .getProposalNumber())));
        } catch (Exception e) {
            logger
                    .error("ParticipantBenefitBaseInformationAction -> getParticipantBenefitBaseAccountDetails -> getTradingRestriction");
            throw new SystemException(e.getMessage());
        }

        // Set the GIFL data object in the request
        request.setAttribute(BDConstants.ACCOUNT_DETAILS, webGiflData);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- participantGiflData");
        }

    }
	private TradeRestriction getTradingRestriction(Long participantId,
            Integer proposalNumber) throws Exception {
        TradeRestriction tradeRestriction = employeeServiceDelegate
                .getGatewayTradeRestriction(participantId, proposalNumber,
                        AccountServiceDelegate.getInstance()
                                .getNextNYSEClosureDate(null));
        return tradeRestriction;
    }
	 protected static EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
	            .getInstance(BDConstants.BD_APPLICATION_ID);
	 private void retrieveParticipantDetails(String profileId,
	            HttpServletRequest request) throws SystemException {
	    	
	    	try {
				ParticipantBenefitBaseDetails participantBenefitBaseDetails = new ParticipantBenefitBaseDetails();

				int contractNumber = getBobContext(request).getCurrentContract()
						.getContractNumber();

				int participantId = ParticipantServiceDelegate.getInstance()
						.getParticipantIdByProfileId(Long.parseLong(profileId),
								contractNumber);

				// Getting Employee related information like firstName,lastName,
				// SSN, D.O.B
				Employee employee = employeeServiceDelegate.getEmployeeByProfileId(
						Long.parseLong(profileId), contractNumber, null);

				String contractNo = new Integer(contractNumber).toString();

				EmployeeDetailVO employeeDetailVO = employee.getEmployeeDetailVO();

				if (employeeDetailVO == null) {
					throw new SystemException(
							"Problem retrieving the participant details for profileId: "
									+ profileId + "; ContractId:" + contractNumber);
				}

				participantBenefitBaseDetails.setFirstName(employeeDetailVO
						.getFirstName());

				participantBenefitBaseDetails.setLastName(employeeDetailVO
						.getLastName());

				participantBenefitBaseDetails.setMiddleInitial(employeeDetailVO
						.getMiddleInitial());

				participantBenefitBaseDetails.setSsn(employeeDetailVO
						.getSocialSecurityNumber());

				participantBenefitBaseDetails.setDateOfBirth(employeeDetailVO
						.getBirthDate());

				// To get the market value for GO funds
				GiflMarketValue gmv = null;
				try {
					gmv = AccountServiceDelegate.getInstance()
							.getParticipantGiflMarketValue(
									String.valueOf(participantId),
									String.valueOf(contractNumber));
				} catch (Exception exception) {
					throw new SystemException(
							"Unable to retrieve market value for the profileId:"
									+ profileId + "; Contract Number:" + contractNo);
				}

				participantBenefitBaseDetails.setMarketValueGoFunds(gmv
						.getMarketValue());
				participantBenefitBaseDetails.setAsOfDate(gmv.getAsOfDate());

				request.setAttribute(BDConstants.BENEFIT_DETAILS,
						participantBenefitBaseDetails);
			} catch (Exception exception) {
				throw new SystemException(
						"Exception in retrieveParticipantDetails : "
								+ exception.getMessage());
			}    	
	        
	    }
	 private Date getMax24MonthsCutOffDate() {

	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.YEAR, -2);
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();
	    }
	private Collection<GenericException> validateTransactionHistory(
            ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm,
            HttpServletRequest request) {

        ArrayList<GenericException> errors = new ArrayList<GenericException>();

        Date fromDate = new Date();
        Date toDate = new Date();
        boolean validDates = false;
        boolean validFromDate = false;
        boolean validFromDateRange = false;
        Date currentDate = getMax24MonthsCutOffDate();

        // FROM date empty
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            errors.add(new GenericException(BDErrorCodes.FROM_DATE_EMPTY));
        }

        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getToDate()))
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))) {

            errors.add(new GenericException(BDErrorCodes.TO_DATE_EMPTY));
        }

        // Both dates empty
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            errors.add(new GenericException(BDErrorCodes.BOTH_DATES_EMPTY));
        }

        // Valid date format
        if ((participantBenefitBaseInformationForm.getToDate().trim().length() > 0)
                && (participantBenefitBaseInformationForm.getFromDate().trim()
                        .length() > 0)) {

            try {
                fromDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getFromDate());
                toDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getToDate());
                validDates = true;
                validFromDate = true;
            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validDates = false;
                validFromDate = false;
            }
        }

        // Empty FROM date, invalid TO date
        if ((StringUtils.isEmpty(participantBenefitBaseInformationForm
                .getFromDate()))
                && (participantBenefitBaseInformationForm.getToDate().trim()
                        .length() > 0)) {

            try {
                toDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getToDate());
                validDates = true;
            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validDates = false;
            }
        }

        // Invalid FROM date format, empty TO date
        if ((participantBenefitBaseInformationForm.getFromDate().trim()
                .length() > 0)
                && (participantBenefitBaseInformationForm.getToDate().trim()
                        .length() == 0)) {

            try {
                fromDate = validateDateFormat(participantBenefitBaseInformationForm
                        .getFromDate());
                validFromDate = true;

            } catch (Exception e) {
                errors.add(new GenericException(BDErrorCodes.INVALID_DATE));
                validFromDate = false;
            }
        }

        // Valid FROM date, empty TO date, and FROM date greater than
        // default TO date
        if (validFromDate
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {

            if (fromDate.after(toDate)) {
                errors
                        .add(new GenericException(
                                BDErrorCodes.FROM_DATE_AFTER_TO));
                validFromDateRange = false;
            } else {
                validFromDateRange = true;
            }
        }

        // Valid FROM date, empty TO date, and FROM date not within the
        // last 24 months of default TO date
        if (validFromDate
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {
            if (fromDate.before(currentDate)) {
                errors.add(new GenericException(
                        BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
                validFromDateRange = false;
            } else {
                validFromDateRange = true;
            }
        }

        // If from date valid, date range valid, and to date is empty,
        // then set default TO Date
        if (validFromDateRange
                && (!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate()))
                && (StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getToDate()))) {
            Calendar cal = Calendar.getInstance();
            // cal.setTime(asOfDate);
            participantBenefitBaseInformationForm.setToDate(DateRender
                    .formatByPattern(cal.getTime(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED));
        }

        // From date must be earlier than To date
        if (validDates
                && ((!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate())) && (!StringUtils
                        .isEmpty(participantBenefitBaseInformationForm
                                .getToDate()))) && (fromDate.after(toDate))) {
            errors.add(new GenericException(BDErrorCodes.FROM_DATE_AFTER_TO));
        }

        // From date outside 24 month range
        if (validDates
                && ((!StringUtils.isEmpty(participantBenefitBaseInformationForm
                        .getFromDate())) && (!StringUtils
                        .isEmpty(participantBenefitBaseInformationForm
                                .getToDate())))
                && (fromDate.before(currentDate))) {
            if (request
                    .getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                participantBenefitBaseInformationForm.setFromDate(DateRender
                        .formatByPattern(cal.getTime(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED));
            } else {
                errors.add(new GenericException(
                        BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
            }
        }

        return errors;
    }
	private Date validateDateFormat(String dateString) throws ParseException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateDateFormat");
        }
        Date validDate = null;
        if (!((dateString.trim().length() == 10)
                && (dateString.substring(2, 3).equals("/")) && (dateString
                .substring(5, 6)).equals("/"))) {
            throw new ParseException("invalid date format", 0);
        }
        String month = dateString.substring(0, 2);
        String day = dateString.substring(3, 5);
        String year = dateString.substring(6, 10);
        try {
            Integer.parseInt(year);
            if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
                throw new ParseException("invalid month", 0);

            if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
                throw new ParseException("invalid day", 0);

            if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2)
                    && (Integer.parseInt(year) % 4 > 0))
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
	  protected static synchronized Date  DateParser(String value) throws ParseException{ 
	        return sdf.parse(value); 
	    }
	  private static SimpleDateFormat sdf = new SimpleDateFormat(
	            ParticipantBenefitBaseInformationForm.FORMAT_DATE_SHORT_MDY);
}
