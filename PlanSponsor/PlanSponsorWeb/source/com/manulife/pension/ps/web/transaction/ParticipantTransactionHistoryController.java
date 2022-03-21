package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.dao.ParticipantAccountDAO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the participant transaction history page.
 * 
 * @author Kristin Kerr
 */
@Controller
@RequestMapping(value ="/transaction")
@SessionAttributes({"participantTransactionHistoryForm"})

public class ParticipantTransactionHistoryController extends AbstractTransactionReportController {
	@ModelAttribute("participantTransactionHistoryForm")
	public ParticipantTransactionHistoryForm populateForm() {
		return new ParticipantTransactionHistoryForm();
	}
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateFormLoan() {
		return new LoanRepaymentDetailsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/participantTransactionHistory.jsp");
		forwards.put("default", "/transaction/participantTransactionHistory.jsp");
		forwards.put("sort", "/transaction/participantTransactionHistory.jsp");
		forwards.put("filter", "/transaction/participantTransactionHistory.jsp");
		forwards.put("page", "/transaction/participantTransactionHistory.jsp");
		forwards.put("print", "/transaction/participantTransactionHistory.jsp");
	}

	private static Logger logger = Logger.getLogger(ParticipantTransactionHistoryController.class);

	private static final String DEFAULT_SORT_FIELD = TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String CSV_HEADER_FROM_DATE = "From date";
	private static final String CSV_HEADER_TO_DATE = "To date";
	private static final String CSV_HEADER_TRANSACTION_TYPE = "Type";
	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] { "Transaction date", "Payroll ending",
			"Type line1", "Type line2", "Amount ($)", "Transaction number" };

	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	public static final String ALL_TYPES = "ALL";
	public static final String NULL = "null";
	public static final String DUMMY_DATE = "01/01/0001";

	private static SimpleDateFormat sdf = new SimpleDateFormat(ParticipantTransactionHistoryForm.FORMAT_DATE_SHORT_MDY);

	static {
		sdf.setLenient(false);
	}

	protected static synchronized Date DateParser(String value) throws ParseException {
		return sdf.parse(value);
	}

	// synchronized method to avoid race condition.
	private static synchronized String dateFormatter(Date inputDate) {
		return sdf.format(inputDate);
	}

	/**
	 * Constructor.
	 */
	public ParticipantTransactionHistoryController() {
		super(ParticipantTransactionHistoryController.class);
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */

	@SuppressWarnings("rawtypes")
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) form;
		/*
		 * This code has been changed and added to Validate form and request against
		 * penetration attack, prior to other validations as part of the CL#137697.
		 */

		Collection errors = super.doValidate(form, request);

		if (getTask(request).equals(FILTER_TASK)) {
			Date fromDate = new Date();
			Date toDate = new Date();
			boolean validDates = false;
			boolean validFromDate = false;
			boolean validFromDateRange = false;
			Date asOfDate = getBalanceAsOfDate(request);
			Date cutOffDate = getMax24MonthsCutOffDate(asOfDate);

			// FROM date empty
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
				errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
			}

			// Both dates empty
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
				errors.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
			}

			// Valid date format
			if ((theForm.getToDate().trim().length() > 0) && (theForm.getFromDate().trim().length() > 0)) {

				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
					validFromDate = true;
				} catch (Exception e) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
					validFromDate = false;
				}
			}

			// Empty FROM date, invalid TO date
			if ((StringUtils.isEmpty(theForm.getFromDate())) && (theForm.getToDate().trim().length() > 0)) {
				try {
					toDate = validateDateFormat(theForm.getToDate());
					validDates = true;
				} catch (Exception e) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE));
					validDates = false;
				}
			}

			// Invalid FROM date format, empty TO date
			if ((theForm.getFromDate().trim().length() > 0) && (theForm.getToDate().trim().length() == 0)) {
				try {
					fromDate = validateDateFormat(theForm.getFromDate());
					validFromDate = true;
				} catch (Exception e) {
					errors.add(new GenericException(ErrorCodes.INVALID_DATE));
					validFromDate = false;
				}
			}

			// Valid FROM date, empty TO date, and FROM date greater than default TO date
			if (validFromDate) {
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}
				}
			}

			// Valid FROM date, empty TO date, and FROM date not within the last 24 months
			// of default TO date
			if (validFromDate) {
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						validFromDateRange = false;
					} else {
						validFromDateRange = true;
					}
				}
			}

			// If from date valid, date range valid, and to date is empty, then set default
			// TO Date
			if (validFromDateRange) {
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (StringUtils.isEmpty(theForm.getToDate()))) {

					Calendar cal = Calendar.getInstance();
					cal.setTime(asOfDate);
					theForm.setToDate(dateFormatter(cal.getTime()));
				}
			}

			// From date must be earlier than To date
			if (validDates) {
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.after(toDate)) {
						errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
					}
				}
			}

			// From date outside 24 month range
			if (validDates) {
				if ((!StringUtils.isEmpty(theForm.getFromDate())) && (!StringUtils.isEmpty(theForm.getToDate()))) {
					if (fromDate.before(cutOffDate)) {
						if (request
								.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
							// We're coming from the contract level transaction history page so adjust to
							// valid date range.
							Calendar cal = Calendar.getInstance();
							cal.setTime(cutOffDate);
							theForm.setFromDate(dateFormatter(cal.getTime()));
						} else {
							errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						}
					}
				}
			}
		}

		// Reset the information for JSP to display.
		if (errors.size() > 0) {

			// Repopulate action form with default information.
			populateReportForm(theForm, request);
			try {
				populateProfileId(theForm, getUserProfile(request).getCurrentContract().getContractNumber());
				populateParticipantDetails(theForm, request);
			} catch (SystemException se) {
				request.setAttribute("details", new ParticipantAccountDetailsVO());
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
			}
			// Signal the JSP to display date dropdowns again for user to change their
			// selection
			request.setAttribute("displayDates", "true");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		return errors;
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

	private Date getMax24MonthsCutOffDate(Date asOfDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(asOfDate);
		cal.add(Calendar.YEAR, -2);

		return cal.getTime();
	}

	protected Date getBalanceAsOfDate(HttpServletRequest request) {
		return getUserProfile(request).getCurrentContract().getContractDates().getAsOfDate();
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) form;

		Contract currentContract = getUserProfile(request).getCurrentContract();

		populateProfileId(theForm, currentContract.getContractNumber());

		if (theForm.getParticipantId() == null || theForm.getParticipantId().length() == 0) {
			int profileIdInt = ParticipantAccountDAO.getParticipantIdByProfileId(Long.parseLong(theForm.getProfileId()),
					currentContract.getContractNumber());
			theForm.setParticipantId(String.valueOf(profileIdInt));
		}
		request.setAttribute("participantId", theForm.getParticipantId());
		request.setAttribute("requestName", "participantTransactionHistory");
		request.setAttribute("profileId", theForm.getProfileId());

		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

		// Get the from Date
		if (!StringUtils.isEmpty(theForm.getFromDate())) {
			try {
				Date fromDate = format.parse(theForm.getFromDate());
				criteria.addFilter(TransactionHistoryReportData.FILTER_FROM_DATE, fromDate);
			} catch (ParseException pe) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"ParseException in fromDate populateReportCriteria() ParticipantTransactionHistoryAction:",
							pe);
				}
			}
		}

		// Get the to Date
		if (!StringUtils.isEmpty(theForm.getToDate())) {
			try {
				Date toDate = format.parse(theForm.getToDate());
				criteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE, toDate);
			} catch (ParseException pe) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"ParseException in toDate populateReportCriteria() ParticipantTransactionHistoryAction:",
							pe);
				}
			}
		}

		criteria.addFilter(TransactionHistoryReportData.FILTER_AS_OF_DATE, getBalanceAsOfDate(request));

		criteria.addFilter(TransactionHistoryReportData.FILTER_PROFILE_ID, theForm.getProfileId());

		criteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER,
				String.valueOf(currentContract.getContractNumber()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_PROPOSAL_NUMBER, currentContract.getProposalNumber());

		criteria.addFilter(TransactionHistoryReportData.APPLICATION_ID, Constants.PS_APPLICATION_ID);

		populateTransactionTypesDropdown(request);

		criteria.addFilter(TransactionHistoryReportData.FILTER_TYPE_LIST,
				getTransactionTypeCode(theForm.getTransactionType(), request));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * Set sorting criteria
	 * 
	 * @see ReportController#populateReportCriteria(String, HttpServletRequest)
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

		/**
		 * Sort by descending (ascending) transaction date Secondary sorts: ascending
		 * (descending) transaction type and loan number, ascending (ascending)
		 * transaction number
		 */
		if (sortField.equals(TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE,
					getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER, getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO, ReportSort.ASC_DIRECTION);

			/**
			 * Sort by descending (ascending) payroll end date Secondary sorts: descending
			 * (ascending) transaction date, ascending (descending) transaction type and
			 * loan number in reversed sort order, ascending (ascending) transaction number
			 */
		} else if (sortField.equals(TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE, sortDirection);
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE,
					getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER, getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO, ReportSort.ASC_DIRECTION);

			/**
			 * Sort by ascending (descending) transaction type and loan number Secondary
			 * sorts: descending (ascending) transaction date descending (ascending) payroll
			 * ending date ascending (ascending) transaction number
			 */
		} else if (sortField.equals(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER, sortDirection);
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE, getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE,
					getReversedSort(sortDirection));
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO, ReportSort.ASC_DIRECTION);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateSortCriteria");
		}
	}

	private String getReversedSort(String sortDirection) {

		return (sortDirection.equalsIgnoreCase(ReportSort.ASC_DIRECTION) ? ReportSort.DESC_DIRECTION
				: ReportSort.ASC_DIRECTION);
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		// Obtain the profileId or if not available the participantId
		String profileId = request.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PROFILE_ID);
		String participantId = request.getParameter(ParticipantTransactionHistoryForm.PARAMETER_KEY_PARTICIPANT_ID);

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

		if (theForm.getTransactionType() == null || theForm.getTransactionType().length() == 0
				|| theForm.getTransactionType().equalsIgnoreCase(NULL)) {
			theForm.setTransactionType(ALL_TYPES); // defaults to all
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}

	protected String getReportId() {
		return TransactionHistoryReportData.REPORT_ID;
	}

	protected String getReportName() {
		return TransactionHistoryReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		TransactionHistoryReportData data = (TransactionHistoryReportData) report;
		StringBuffer buffer = new StringBuffer();
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		// Get dates for display
		Date asOfDate = userProfile.getCurrentContract().getContractDates().getAsOfDate();
		Date fromDate = new Date();
		Date toDate = new Date();

		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

		buffer.append("As of,").append(format.format(asOfDate)).append(LINE_BREAK).append(LINE_BREAK);

		try {
			fromDate = format.parse(theForm.getFromDate());
		} catch (ParseException pe) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in fromDate getDownloadData() ParticipantTransactionHistoryAction:", pe);
			}
		}

		try {
			toDate = format.parse(theForm.getToDate());
		} catch (ParseException pe) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in fromDate getDownloadData() ParticipantTransactionHistoryAction:", pe);
			}
		}

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request.getAttribute("details");

		// SSE024, mask ssn if no download report full ssn permission
		boolean maskSSN = true;
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(userProfile, currentContract.getContractNumber());
		} catch (SystemException se) {
			logger.error(se);
		}

		try {

			buffer.append("Last name,First name,SSN,Birth Date,");
			buffer.append("Age,");
			buffer.append("Address line 1,Address line 2,City,State,Zip Code,Status,");
			buffer.append(LINE_BREAK);

			buffer.append(QUOTE).append(detailsVO.getLastName()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(detailsVO.getFirstName()).append(QUOTE).append(COMMA);
			buffer.append(SSNRender.format(detailsVO.getSsn(), "", maskSSN)).append(COMMA);
			buffer.append(detailsVO.getBirthDate() == null ? " " : format.format(detailsVO.getBirthDate()))
					.append(COMMA);
			buffer.append(detailsVO.getAge()).append(COMMA);
			buffer.append(QUOTE).append(detailsVO.getAddressLine1()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(detailsVO.getAddressLine2()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(detailsVO.getCityName()).append(QUOTE).append(COMMA);
			buffer.append(detailsVO.getStateCode()).append(COMMA);
			buffer.append(detailsVO.getZipCode()).append(COMMA);
			buffer.append(detailsVO.getEmployeeStatus()).append(COMMA);
			buffer.append(LINE_BREAK).append(LINE_BREAK);

			buffer.append("Total assets,");
			buffer.append("Allocated assets,");
			buffer.append("Personal brokerage account assets,");
			buffer.append("Loan assets,");
			buffer.append("Investment instruction type,");
			buffer.append("Default date of birth,");
			buffer.append("Last contribution date,");
			buffer.append("Automatic rebalance indicator,");
			buffer.append(LINE_BREAK);

			buffer.append(detailsVO.getTotalAssets()).append(COMMA);
			buffer.append(detailsVO.getAllocatedAssets()).append(COMMA);
			buffer.append(detailsVO.getPersonalBrokerageAccount()).append(COMMA);
			buffer.append(detailsVO.getLoanAssets()).append(COMMA);
			String investInstTypeDes = "";
			if (detailsVO.getInvestmentInstructionType() != null) {
				if ("TR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType())) {
					investInstTypeDes = "TR – Instructions were provided by Trustee - Mapped";
				} else if ("PA".equalsIgnoreCase(detailsVO.getInvestmentInstructionType())) {
					investInstTypeDes = "PA – Participant Provided";
				} else if ("PR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType())) {
					investInstTypeDes = "PR – Instructions prorated - participant instructions incomplete / incorrect";
				} else if ("DF".equalsIgnoreCase(detailsVO.getInvestmentInstructionType())) {
					investInstTypeDes = "DF – Default investment option was used";
				} else if ("MA".equalsIgnoreCase(detailsVO.getInvestmentInstructionType())) {
					investInstTypeDes = "MA - Managed Accounts";
				}
			}
			buffer.append(investInstTypeDes).append(COMMA);
			buffer.append(detailsVO.getBirthDate() == null ? "Yes" : "No").append(COMMA);
			buffer.append(detailsVO.getLastContributionDate() == null ? " "
					: format.format(detailsVO.getLastContributionDate())).append(COMMA);
			buffer.append(detailsVO.getAutomaticRebalanceIndicator()).append(COMMA);
			buffer.append(LINE_BREAK).append(LINE_BREAK);

			String fromDateStr = DateRender.format(fromDate, RenderConstants.MEDIUM_MDY_SLASHED);
			String toDateStr = DateRender.format(toDate, RenderConstants.MEDIUM_MDY_SLASHED);

			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
			buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
			buffer.append(LINE_BREAK);
			buffer.append(fromDateStr).append(COMMA);
			buffer.append(toDateStr).append(LINE_BREAK);

			buffer.append(LINE_BREAK);

			String transactionType = getTransactionTypeDescription(theForm.getTransactionType(), request);

			buffer.append(CSV_HEADER_TRANSACTION_TYPE).append(COMMA);
			buffer.append(transactionType).append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			if (data.getDetails().size() == 0) {
				Content message = null;
				message = ContentCacheManager.getInstance().getContentById(
						ContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
						ContentTypeManager.instance().MESSAGE);

				buffer.append(ContentUtility.getContentAttribute(message, "text")).append(LINE_BREAK);

			} else {

				for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
					buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
					if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
						buffer.append(COMMA);
					}
				}
				buffer.append(LINE_BREAK);

				// Individual Line Items
				Iterator iterator = report.getDetails().iterator();
				while (iterator.hasNext()) {

					TransactionHistoryItem theItem = (TransactionHistoryItem) iterator.next();
					String transactionDate = DateRender.format(theItem.getTransactionDate(), null);
					String payrollDate = " ";
					if (theItem.displayPayrollDate()) {
						payrollDate = DateRender.format(theItem.getPayrollEndingDate(), null);
					}
					String transactionNumber = " ";
					if (!theItem.getTransactionNumber().trim().equals("0")) {
						transactionNumber = theItem.getTransactionNumber();
					}

					if (theItem.getType().equals("WD")) {

						// Withdrawal line 1
						buffer.append(transactionDate).append(COMMA);
						buffer.append(payrollDate).append(COMMA);
						buffer.append(getCsvString(theItem.getTypeDescription1())).append(COMMA);
						buffer.append(getCsvString(theItem.getTypeDescription2())).append(COMMA);
						buffer.append(getCsvString(null)).append(COMMA);
						buffer.append(getCsvString(transactionNumber));
						buffer.append(LINE_BREAK);

						// Withdrawal line 2
						buffer.append(getCsvString(null)).append(COMMA);
						buffer.append(getCsvString(null)).append(COMMA);
						buffer.append(getCsvString(ParticipantTransactionHistoryForm.WITHDRAWAL_AMOUNT)).append(COMMA);
						buffer.append(getCsvString(null)).append(COMMA);
						buffer.append(getCsvString(theItem.getAmount())).append(COMMA);
						buffer.append(getCsvString(null));
						buffer.append(LINE_BREAK);

						// Withdrawal line 3
						if (!theItem.getDisplayChequeAmount().equals("-")) {
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(getCsvString(ParticipantTransactionHistoryForm.DISTRIBUTION_AMOUNT))
									.append(COMMA);
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(getCsvString(theItem.getChequeAmount())).append(COMMA);
							buffer.append(getCsvString(null));
							buffer.append(LINE_BREAK);
						}

					} else {
						buffer.append(transactionDate).append(COMMA);
						buffer.append(payrollDate).append(COMMA);
						buffer.append(getCsvString(theItem.getTypeDescription1())).append(COMMA);
						buffer.append(getCsvString(theItem.getTypeDescription2())).append(COMMA);
						buffer.append(getCsvString(theItem.getAmount())).append(COMMA);
						buffer.append(getCsvString(transactionNumber));
					}

					buffer.append(LINE_BREAK);
				}
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(), "getDownloadData", "Something wrong with CMA");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * Gets the transaction type description.
	 * 
	 * @return Returns the transaction type description.
	 */
	protected String getTransactionTypeDescription(String code, HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTransactionTypeDescription");
		}

		// Construct an array of keys if user selected "All" option
		// Array starts with index 1 (i.e. minus key for ALL)
		List typesDropdown = (List) request.getAttribute(Constants.TYPES_DROPDOWN);
		for (Iterator it = typesDropdown.iterator(); it.hasNext();) {
			LabelValueBean bean = (LabelValueBean) it.next();
			if (bean.getValue().equals(code)) {
				return bean.getLabel();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTransactionTypeDescription");
		}

		IllegalArgumentException ae = new IllegalArgumentException("Invalid code [" + code + "]");
		throw new SystemException(ae, this.getClass().getName(), "getTransactionTypeDescription",
				"Failed to get transaction type description.");

	}

	/**
	 * Gets the transactionTypes
	 * 
	 * @return Returns a Collection
	 */
	protected Collection getTransactionTypeCode(String type, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTransactionTypeCode");
		}

		// Construct an array of keys if user selected "All" option
		// Array starts with index 1 (i.e. minus key for ALL)
		List types = new ArrayList();
		if (type.equalsIgnoreCase(ALL_TYPES)) {
			List typesDropdown = (List) request.getAttribute(Constants.TYPES_DROPDOWN);
			for (Iterator it = typesDropdown.iterator(); it.hasNext();) {
				LabelValueBean bean = (LabelValueBean) it.next();
				if (bean.getValue().equals(ALL_TYPES)) {
					continue;
				}
				types.add(bean.getValue());
			}
		} else {
			types.add(type);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTransactionTypeCode");
		}

		return types;
	}

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		String forward = super.doCommon(reportForm, request, response);

		TransactionHistoryReportData report = (TransactionHistoryReportData) request
				.getAttribute(Constants.REPORT_BEAN);

		// Exclude loan-related types from the types dropdown if the results have no
		// loan items
		if (report != null && (report.hasLoans() || getUserProfile(request).getCurrentContract().isLoanFeature())) {
			List typesDropdown = (List) request.getAttribute(Constants.TYPES_DROPDOWN);
			request.setAttribute(Constants.TRANSACTION_TYPES, typesDropdown);
		} else {
			List noLoansTypesDropdown = (List) request.getAttribute(Constants.LOANS_DROPDOWN);
			request.setAttribute(Constants.TRANSACTION_TYPES, noLoansTypesDropdown);
		}

		if (((Collection) request.getAttribute(PsBaseUtil.ERROR_KEY)) == null) {
			populateParticipantDetails(reportForm, request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}

		return forward;
	}

	private void populateTransactionTypesDropdown(HttpServletRequest request) {
		/*
		 * because these lists are dynamic - based on contract data - they must be
		 * rebuilt everytime through
		 */

		List typesDropdown = new ArrayList();
		List noLoansTypesDropdown = new ArrayList();

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		boolean isSignatureContract = (userProfile.getCurrentContract().getDefaultClass() != null
				&& !userProfile.getCurrentContract().getDefaultClass().trim().equals(""));
		typesDropdown.add(new LabelValueBean("All", ALL_TYPES));
		noLoansTypesDropdown.add(new LabelValueBean("All", ALL_TYPES));

		String[] types = new String[] { TransactionType.REQUEST_PS_ADJUSTMENT,
				TransactionType.REQUEST_PS_ALLOCATION_INSTRUCTION, TransactionType.REQUEST_PS_CONTRIBUTION,
				// TransactionType.REQUEST_DEFERRALS,
				TransactionType.REQUEST_PS_INTER_ACCOUNT_TRANSFER, TransactionType.REQUEST_PS_LOAN_CLOSURE,
				TransactionType.REQUEST_PS_LOAN_ISSUE, TransactionType.REQUEST_PS_LOAN_REPAYMENT,
				TransactionType.REQUEST_PS_LOAN_TRANSFER, TransactionType.REQUEST_PS_MATURITY_INVESTMENT,
				TransactionType.REQUEST_PS_PBA_TIK, TransactionType.REQUEST_PS_WITHDRAWAL,
				TransactionType.REQUEST_PS_CLASS_CONVERSION };
		for (int i = 0; i < types.length; i++) {
			LabelValueBean lvbean = new LabelValueBean(TransactionTypeDescription.getPsDescription(types[i], false),
					types[i]);
			if (!types[i].equals(TransactionType.REQUEST_PS_CLASS_CONVERSION) || isSignatureContract) {
				typesDropdown.add(lvbean);
			}
			if (types[i] != TransactionType.REQUEST_PS_LOAN_ISSUE && types[i] != TransactionType.REQUEST_PS_LOAN_CLOSURE
					&& types[i] != TransactionType.REQUEST_PS_LOAN_REPAYMENT
					&& types[i] != TransactionType.REQUEST_PS_LOAN_TRANSFER) {
				if (types[i].equals(TransactionType.REQUEST_PS_CLASS_CONVERSION)) {
					if (isSignatureContract) {
						noLoansTypesDropdown.add(lvbean);
					} else {
						// exclude
					}
				} else {
					noLoansTypesDropdown.add(lvbean);
				}
			}
		}

		// set to request scope
		request.setAttribute(CommonConstants.TYPES_DROPDOWN, typesDropdown);
		request.setAttribute(CommonConstants.LOANS_DROPDOWN, noLoansTypesDropdown);

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
	 * Check if the profileId was provided and if not retrieve using the
	 * participantId
	 */
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

	@RequestMapping(value ="/participantTransactionHistory/", method = {RequestMethod.GET})
	public String doDefault(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value ="/participantTransactionHistory/",params= {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/participantTransactionHistory/",params= {"task=sort"}, method = RequestMethod.GET)
	public String doSort(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/participantTransactionHistory/",params= {"task=page"}, method = RequestMethod.GET)
	public String doPage(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/participantTransactionHistory/",params= {"task=print"}, method = RequestMethod.GET)
	public String doPrint(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value ="/participantTransactionHistory/",params= {"task=download"}, method = RequestMethod.GET)
	public String doDownload(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				// Repopulate action form with default information.
				populateReportForm(form, request);
				try {
					populateProfileId(form, getUserProfile(request).getCurrentContract().getContractNumber());
					populateParticipantDetails(form, request);
				} catch (SystemException se) {
					request.setAttribute("details", new ParticipantAccountDetailsVO());
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
				}
				// Signal the JSP to display date dropdowns again for user to change their
				// selection
				request.setAttribute("displayDates", "true");
				
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
