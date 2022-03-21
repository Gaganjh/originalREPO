package com.manulife.pension.bd.web.bob.transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription;
import javax.servlet.http.HttpServletRequest;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.delegate.DelegateConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;

import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.RenderConstants;
@Component
public class ParticipantTransactionHistoryValidator extends ValidatorUtil implements Validator {
	protected static final String DOWNLOAD_TASK = "download";
	protected static final String PRINT_PDF_TASK = "printPDF";
	protected static final String FILTER_TASK = "filter";
	protected static final String SORT_TASK = "sort";
	public static final String INPUT = "input";
	protected Date getBalanceAsOfDate(HttpServletRequest request) {
		return getBobContext(request).getCurrentContract().getContractDates()
				.getAsOfDate();
	}
	private static Logger logger = Logger.getLogger(ParticipantTransactionHistoryValidator.class);
	@Override
	public boolean supports(Class clazz) {
		return ParticipantTransactionHistoryForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;	
		if(!bindingResult.hasErrors()){
		Collection<GenericException> error = new ArrayList();
		String[] errorCodes = new String[10];
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		
		/** This code has been changed and added  to 
		 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
		 */
		
			populateTransactionTypesDropdown(request, theForm);
			TransactionHistoryReportData report = (TransactionHistoryReportData) request
					.getAttribute(BDConstants.REPORT_BEAN);

			// Exclude loan-related types from the types dropdown if the results
			// have no loan items
			if (report != null
					&& (report.hasLoans() || getBobContext(request)
							.getCurrentContract().isLoanFeature())) {
				request.setAttribute(BDConstants.TRANSACTION_TYPES,
						theForm.getTypesDropdown());
			} else {
				request.setAttribute(BDConstants.TRANSACTION_TYPES,
						theForm.getNoLonsTypesDropdown());
			}

			// Repopulate action form with default information.
			populateReportForm( theForm, request);
			try {
				populateProfileId(theForm, getBobContext(request)
						.getCurrentContract().getContractNumber());
				populateParticipantDetails(theForm, request);
			} catch (SystemException se) {
				request.setAttribute(
						DelegateConstants.ACCOUNT_VIEW_DETAILS,
						new ParticipantAccountDetailsVO());
				LogUtility.logSystemException(
						BDConstants.BD_APPLICATION_ID, se);
			}
			// Signal the JSP to display date dropdowns again for user to
			// change their selection
			request.setAttribute(BDConstants.CSV_DISPLAY_DATES,
					BDConstants.CSV_TRUE);
			
		

		

		if (FILTER_TASK.equals(getTask(request))
				|| DOWNLOAD_TASK.equals(getTask(request))
				|| PRINT_PDF_TASK.equals(getTask(request))
				|| SORT_TASK.equals(getTask(request))) {
			Date fromDate = new Date();
			Date toDate = new Date();
			boolean validDates = false;
			boolean validFromDate = false;
			boolean validFromDateRange = false;
			Date asOfDate = getBalanceAsOfDate(request);
			Date cutOffDate = getMax24MonthsCutOffDate(asOfDate);

			// FROM date empty
			if ((StringUtils.isBlank(theForm.getFromDate()))
					&& (!StringUtils.isBlank(theForm.getToDate()))) {
				error.add(new GenericException(BDErrorCodes.FROM_DATE_EMPTY));
			}

			// Both dates empty
			if ((StringUtils.isBlank(theForm.getFromDate()))
					&& (StringUtils.isBlank(theForm.getToDate()))) {
				error.add(new GenericException(BDErrorCodes.BOTH_DATES_EMPTY));
			}

			// Valid date format
			if ((theForm.getToDate().trim().length() > 0)
					&& (theForm.getFromDate().trim().length() > 0)) {

				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
					validFromDate = true;
				} catch (Exception e) {
					error.add(new GenericException(BDErrorCodes.INVALID_DATE));
					validDates = false;
					validFromDate = false;
				}
			}

			// Empty FROM date, invalid TO date
			if ((StringUtils.isEmpty(theForm.getFromDate()))
					&& (theForm.getToDate().trim().length() > 0)) {
				try {
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
				} catch (Exception e) {
					error.add(new GenericException(BDErrorCodes.INVALID_DATE));
					validDates = false;
				}
			}

			// Invalid FROM date format, empty TO date
			if ((theForm.getFromDate().trim().length() > 0)
					&& (theForm.getToDate().trim().length() == 0)) {
				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					validFromDate = true;
				} catch (Exception e) {
					error.add(new GenericException(BDErrorCodes.INVALID_DATE));
					validFromDate = false;
				}
			}

			// Valid FROM date, empty TO date, and FROM date greater than
			// default TO date
			if (validFromDate) {
				if ((!StringUtils.isEmpty(theForm.getFromDate()))
						&& (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						error.add(new GenericException(
								BDErrorCodes.FROM_DATE_AFTER_TO));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}
				}
			}

			// Valid FROM date, empty TO date, and FROM date not within the last
			// 24 months of default TO date
			if (validFromDate) {
				if ((!StringUtils.isEmpty(theForm.getFromDate()))
						&& (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						error.add(new GenericException(
								BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}
				}
			}

			// If from date valid, date range valid, and to date is empty, then
			// set default TO Date
			if (validFromDateRange) {
				if ((!StringUtils.isEmpty(theForm.getFromDate()))
						&& (StringUtils.isEmpty(theForm.getToDate()))) {

					Calendar cal = Calendar.getInstance();
					cal.setTime(asOfDate);
					theForm.setToDate(dateFormatter(cal.getTime()));
				}
			}

			// From date must be earlier than To date
			if (validDates) {
				if ((!StringUtils.isEmpty(theForm.getFromDate()))
						&& (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						error.add(new GenericException(
								BDErrorCodes.FROM_DATE_AFTER_TO));
					}
				}
			}

			// From date outside 24 month range
			if (validDates) {
				if ((!StringUtils.isEmpty(theForm.getFromDate()))
						&& (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						if (request
								.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
							// We're coming from the contract level transaction
							// history page so adjust to valid date range.
							Calendar cal = Calendar.getInstance();
							cal.setTime(cutOffDate);
							theForm.setFromDate(dateFormatter(cal.getTime()));
						} else {
							error.add(new GenericException(
									BDErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						}
					}
				}
			}
		}
 
		populateTransactionTypesDropdown(request, theForm);
		TransactionHistoryReportData report1 = (TransactionHistoryReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		// Exclude loan-related types from the types dropdown if the results
		// have no loan items
		if (report != null
				&& (report.hasLoans() || getBobContext(request)
						.getCurrentContract().isLoanFeature())) {
			request.setAttribute(BDConstants.TRANSACTION_TYPES,
					theForm.getTypesDropdown());
		} else {
			request.setAttribute(BDConstants.TRANSACTION_TYPES,
					theForm.getNoLonsTypesDropdown());
		}

		// Reset the information for JSP to display.
		if (error.size() > 0) {

			if (DOWNLOAD_TASK.equals(getTask(request))
					|| PRINT_PDF_TASK.equals(getTask(request))) {
				// For the print PDF / Download CSV tasks, we want to make sure
				// that if there were
				// any errors recorded, then the report should not be obtained
				// from DAO.
				request.setAttribute(BDConstants.ERRORS_PRESENT, Boolean.TRUE);
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
					
					
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.setAttribute(BdBaseController.ERROR_KEY, bindingResult);
				
			} else {
				// Repopulate action form with default information.
				populateReportForm( theForm, request);
				try {
					populateProfileId(theForm, getBobContext(request)
							.getCurrentContract().getContractNumber());
					populateParticipantDetails(theForm, request);
				} catch (SystemException se) {
					request.setAttribute(
							DelegateConstants.ACCOUNT_VIEW_DETAILS,
							new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(
							BDConstants.BD_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to
				// change their selection
				request.setAttribute(BDConstants.CSV_DISPLAY_DATES,
						BDConstants.CSV_TRUE);
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
					
				request.getSession(false).removeAttribute(BdBaseController.ERROR_KEY);
				request.getSession(false).setAttribute(BdBaseController.ERROR_KEY,bindingResult);
					request.removeAttribute(BdBaseController.ERROR_KEY);
					request.setAttribute(BdBaseController.ERROR_KEY, bindingResult);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		}		
	}
	private Date getMax24MonthsCutOffDate(Date asOfDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(asOfDate);
		cal.add(Calendar.YEAR, -2);

		return cal.getTime();
	}
	protected static synchronized Date DateParser(String value)
			throws ParseException {
		return sdf.parse(value);
	}
	private Date validateDateFormat(String dateString) throws ParseException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateDateFormat");
		}

		Date validDate = null;

		if (!((dateString.trim().length() == 10)
				&& (BDConstants.SLASH_SYMBOL.equals(dateString.substring(2, 3))) && (BDConstants.SLASH_SYMBOL
					.equals(dateString.substring(5, 6))))) {
			throw new ParseException(BDConstants.CSV_INVALID_DT_FORMAT,
					BDConstants.NUMBER_0);
		}

		String month = dateString.substring(0, 2);
		String day = dateString.substring(3, 5);
		String year = dateString.substring(6, 10);

		try {
			if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
				throw new ParseException(BDConstants.CSV_INVALID_MONTH,
						BDConstants.NUMBER_0);

			if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
				throw new ParseException(BDConstants.CSV_INVALID_DAY,
						BDConstants.NUMBER_0);

			if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2)
					&& (Integer.parseInt(year) % 4 > 0))
				throw new ParseException(BDConstants.CSV_INVALID_DAY,
						BDConstants.NUMBER_0);
		} catch (Exception e) {
			throw new ParseException(BDConstants.CSV_INVALID_DT_FORMAT,
					BDConstants.NUMBER_0);
		}

		validDate = DateParser(dateString);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- validateDateFormat");
		}
		return validDate;
	}
	protected static BobContext getBobContext(HttpServletRequest request) {
		return BDController.getBobContext(request);
	}
	public void populateParticipantDetails(BaseReportForm reportForm,
			HttpServletRequest request) throws SystemException {

		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		ParticipantAccountVO participantAccountVO = null;
		ParticipantAccountDetailsVO participantDetailsVO = null;

		Contract currentContract = getBobContext(request).getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		String productId = getBobContext(request).getCurrentContract()
				.getProductId();

		BDPrincipal principal = getUserProfile(request).getBDPrincipal();
		participantAccountVO = ParticipantServiceDelegate.getInstance()
				.getParticipantAccount(principal, contractNumber, productId,
						theForm.getProfileId(), null, false, false);
		participantDetailsVO = participantAccountVO
				.getParticipantAccountDetailsVO();

		request.setAttribute(DelegateConstants.ACCOUNT_VIEW_DETAILS,
				participantDetailsVO);

		theForm.setShowAge(participantDetailsVO.getBirthDate() != null);
		theForm.setShowPba(currentContract.isPBA()
				|| participantDetailsVO.getPersonalBrokerageAccount() > 0);
		theForm.setShowLoans(participantDetailsVO.getLoanAssets() > 0);

		// loan details drop down
		Collection<ParticipantLoanDetails> loans = participantAccountVO
				.getLoanDetailsCollection();
		theForm.setLoanDetailList(loans);

		if (loans.size() == 1) {
			Iterator loansIt = loans.iterator();
			if (loansIt.hasNext()) {
				ParticipantLoanDetails loanDetails = (ParticipantLoanDetails) loansIt
						.next();
				theForm.setSelectedLoan(loanDetails.getLoanId());
			}
		}
	}
	public void populateProfileId(ParticipantTransactionHistoryForm theForm,
			int contractNumber) throws SystemException {

		if (theForm.getProfileId() == null
				|| theForm.getProfileId().length() == 0) {

			// common log 78460 lookup profileId by participant id and contract
			// number
			if (theForm.getParticipantId() != null
					&& theForm.getParticipantId().length() > 0) {
				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
				theForm.setProfileId(asd
						.getProfileIdByParticipantIdAndContractNumber(
								theForm.getParticipantId(),
								Integer.toString(contractNumber)));
			}

			if (theForm.getProfileId() == null
					|| theForm.getProfileId().length() == 0) {
				Exception ex = new Exception("Failed to get the profileId");
				throw new SystemException(ex, this.getClass().getName(),
						"populateProfileId", "Failed to get profileId on form "
								+ theForm.toString());
			}
		}
	}
	private static synchronized String dateFormatter(Date inputDate) {
		return sdf.format(inputDate);
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			RenderConstants.MEDIUM_MDY_SLASHED);

	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		// Obtain the profileId or if not available the participantId
		String profileId = request
				.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PROFILE_ID);
		String participantId = request
				.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PARTICIPANT_ID);

		if (profileId != null && profileId.length() > 0) {
			theForm.setProfileId(profileId);
			theForm.setParticipantId(null);
		} else if (participantId != null && participantId.length() > 0) {
			theForm.setParticipantId(participantId);
			theForm.setProfileId(null);
		}

		// Set default FROM and TO dates
		if (theForm.getToDate() == null || theForm.getFromDate() == null) {

			Date asOfDate = getBalanceAsOfDate(request);

			Calendar cal = Calendar.getInstance();
			cal.setTime(asOfDate);
			// Set toDate to asOfDate
			theForm.setToDate(dateFormatter(cal.getTime()));

			// Set fromDate to 1 month prior to toDate
			cal.add(Calendar.MONTH, -1);
			theForm.setFromDate(dateFormatter(cal.getTime()));
		}

		if (StringUtils.isEmpty(theForm.getTransactionType())
				|| BDConstants.NULL.equalsIgnoreCase(theForm
						.getTransactionType())) {
			theForm.setTransactionType(BDConstants.ALL_TYPES); // defaults to
																// all
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}
	private void populateTransactionTypesDropdown(HttpServletRequest request,
			BaseReportForm reportForm) {
		/*
		 * because these lists are dynamic - based on contract data - they must
		 * be rebuilt everytime through
		 */
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;
		List<LabelValueBean> typesDropdown = new ArrayList<LabelValueBean>();
		List<LabelValueBean> noLonsTypesDropdown = new ArrayList<LabelValueBean>();

		Contract currentContract = getBobContext(request).getCurrentContract();
		boolean isSignatureContract = (currentContract.getDefaultClass() != null && !currentContract
				.getDefaultClass().trim().equals(BDConstants.SPACE_SYMBOL));
		typesDropdown.add(new LabelValueBean(BDConstants.CSV_ALL,
				BDConstants.ALL_TYPES));
		noLonsTypesDropdown.add(new LabelValueBean(BDConstants.CSV_ALL,
				BDConstants.ALL_TYPES));

		String[] types = new String[] {
				TransactionType.REQUEST_PS_ADJUSTMENT,
				TransactionType.REQUEST_PS_ALLOCATION_INSTRUCTION,
				TransactionType.REQUEST_PS_CONTRIBUTION,
				// TransactionType.REQUEST_DEFERRALS,
				TransactionType.REQUEST_PS_INTER_ACCOUNT_TRANSFER,
				TransactionType.REQUEST_PS_LOAN_CLOSURE,
				TransactionType.REQUEST_PS_LOAN_ISSUE,
				TransactionType.REQUEST_PS_LOAN_REPAYMENT,
				TransactionType.REQUEST_PS_LOAN_TRANSFER,
				TransactionType.REQUEST_PS_MATURITY_INVESTMENT,
				TransactionType.REQUEST_PS_PBA_TIK,
				TransactionType.REQUEST_PS_WITHDRAWAL,
				TransactionType.REQUEST_PS_CLASS_CONVERSION };
		for (int i = 0; i < types.length; i++) {
			LabelValueBean lvbean = new LabelValueBean(
					TransactionTypeDescription
							.getPsDescription(types[i], false),
					types[i]);
			if (!TransactionType.REQUEST_PS_CLASS_CONVERSION.equals(types[i])
					|| isSignatureContract) {
				typesDropdown.add(lvbean);
			}
			if (types[i] != TransactionType.REQUEST_PS_LOAN_ISSUE
					&& types[i] != TransactionType.REQUEST_PS_LOAN_CLOSURE
					&& types[i] != TransactionType.REQUEST_PS_LOAN_REPAYMENT
					&& types[i] != TransactionType.REQUEST_PS_LOAN_TRANSFER) {
				if (TransactionType.REQUEST_PS_CLASS_CONVERSION
						.equals(types[i])) {
					if (isSignatureContract) {
						noLonsTypesDropdown.add(lvbean);
					} else {
						// exclude
					}
				} else {
					noLonsTypesDropdown.add(lvbean);
				}
			}
		}
		theForm.setTypesDropdown(typesDropdown);
		theForm.setNoLonsTypesDropdown(noLonsTypesDropdown);
	}
}
