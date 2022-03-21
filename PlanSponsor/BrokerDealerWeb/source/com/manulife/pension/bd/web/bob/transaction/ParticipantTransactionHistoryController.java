package com.manulife.pension.bd.web.bob.transaction;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.DelegateConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.dao.ParticipantAccountDAO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionTypeDescription;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the participant transaction history page.
 * 
 * @author harlomte
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantTransactionHistoryForm"})

public class ParticipantTransactionHistoryController extends
		AbstractTransactionReportController {
	@ModelAttribute("participantTransactionHistoryForm")
	public ParticipantTransactionHistoryForm populateForm() {
		return new ParticipantTransactionHistoryForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/participantTransactionHistory.jsp");
		forwards.put("default","/transaction/participantTransactionHistory.jsp");
		forwards.put("sort","/transaction/participantTransactionHistory.jsp");
		forwards.put("filter","/transaction/participantTransactionHistory.jsp");
		forwards.put("page","/transaction/participantTransactionHistory.jsp");
		forwards.put("print","/transaction/participantTransactionHistory.jsp");
	}

	private static Logger logger = Logger
			.getLogger(ParticipantTransactionHistoryController.class);

	private static final String DEFAULT_SORT_FIELD = TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] {
			"Transaction Date", "Type Line1", "Type Line2", "Amount ($)",
			"Payroll Ending", "Transaction Number" };

	private static final String PARTICIPANT_TRANSACTION_HISTORY_REPORT_NAME = "participantTransactionHistory";

	private static final String XSLT_FILE_KEY_NAME = "ParticipantTransactionHistoryReport.XSLFile";

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			RenderConstants.MEDIUM_MDY_SLASHED);

	static {
		sdf.setLenient(false);
	}

	protected static synchronized Date DateParser(String value)
			throws ParseException {
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
	@Autowired
	private ParticipantTransactionHistoryValidator participantTransactionHistoryValidator;
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(participantTransactionHistoryValidator);
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

	private Date getMax24MonthsCutOffDate(Date asOfDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(asOfDate);
		cal.add(Calendar.YEAR, -2);

		return cal.getTime();
	}

	protected Date getBalanceAsOfDate(HttpServletRequest request) {
		return getBobContext(request).getCurrentContract().getContractDates()
				.getAsOfDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#
	 * populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) form;
		Contract currentContract = getBobContext(request).getCurrentContract();

		populateProfileId(theForm, currentContract.getContractNumber());

		if (theForm.getParticipantId() == null
				|| theForm.getParticipantId().length() == 0) {
			int profileIdInt = ParticipantAccountDAO
					.getParticipantIdByProfileId(
							Long.parseLong(theForm.getProfileId()),
							currentContract.getContractNumber());
			theForm.setParticipantId(String.valueOf(profileIdInt));
		}
		request.setAttribute(BDConstants.CSV_PARTICIPANT_ID,
				theForm.getParticipantId());
		request.setAttribute(BDConstants.CSV_REQUEST_NAME,
				"participantTransactionHistory");
		request.setAttribute(BDConstants.CSV_PROFILE_ID, theForm.getProfileId());

		SimpleDateFormat format = new SimpleDateFormat(
				RenderConstants.MEDIUM_MDY_SLASHED);

		// Get the from Date
		if (!StringUtils.isEmpty(theForm.getFromDate())) {
			try {
				Date fromDate = format.parse(theForm.getFromDate());
				criteria.addFilter(
						TransactionHistoryReportData.FILTER_FROM_DATE, fromDate);
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
				criteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE,
						toDate);
			} catch (ParseException pe) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"ParseException in toDate populateReportCriteria() ParticipantTransactionHistoryAction:",
							pe);
				}
			}
		}

		criteria.addFilter(TransactionHistoryReportData.FILTER_AS_OF_DATE,
				getBalanceAsOfDate(request));

		criteria.addFilter(TransactionHistoryReportData.FILTER_PROFILE_ID,
				theForm.getProfileId());

		criteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER,
				String.valueOf(currentContract.getContractNumber()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_PROPOSAL_NUMBER,
				currentContract.getProposalNumber());

		// We have to send the PS_APPLICATION_ID instead of BD_APPLICATION_ID.
		// Only then, we will
		// get back 1report that would have the same info as in PSW site. (for
		// ex: Description Line)
		criteria.addFilter(TransactionHistoryReportData.APPLICATION_ID,
				BDConstants.PS_APPLICATION_ID);

		populateTransactionTypesDropdown(request, form);

		criteria.addFilter(
				TransactionHistoryReportData.FILTER_TYPE_LIST,
				getTransactionTypeCode(theForm.getTransactionType(),
						theForm.getTypesDropdown()));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * Set sorting criteria
	 * 
	 * @see BaseReportController#populateReportCriteria(String, HttpServletRequest)
	 */

	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

		/**
		 * Sort by descending (ascending) transaction date Secondary sorts:
		 * ascending (descending) transaction type and loan number, ascending
		 * (ascending) transaction number
		 */
		if (TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE
				.equals(sortField)) {
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO,
					ReportSort.ASC_DIRECTION);

			/**
			 * Sort by descending (ascending) payroll end date Secondary sorts:
			 * descending (ascending) transaction date, ascending (descending)
			 * transaction type and loan number in reversed sort order,
			 * ascending (ascending) transaction number
			 */
		} else if (TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE
				.equals(sortField)) {
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE,
					sortDirection);
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO,
					ReportSort.ASC_DIRECTION);

			/**
			 * Sort by ascending (descending) transaction type and loan number
			 * Secondary sorts: descending (ascending) transaction date
			 * descending (ascending) payroll ending date ascending (ascending)
			 * transaction number
			 */
		} else if (TransactionHistoryReportData.SORT_FIELD_TRANSACTION_TYPE
				.equals(sortField)) {
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_LOAN_NUMBER,
					sortDirection);
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_EFFECTIVE_DATE,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_PAYROLL_ENDING_DATE,
					getReversedSort(sortDirection));
			criteria.insertSort(
					TransactionHistoryReportData.SORT_FIELD_TRANSACTION_NO,
					ReportSort.ASC_DIRECTION);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateSortCriteria");
		}
	}

	private String getReversedSort(String sortDirection) {

		return (ReportSort.ASC_DIRECTION.equalsIgnoreCase(sortDirection) ? ReportSort.DESC_DIRECTION
				: ReportSort.ASC_DIRECTION);
	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);
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

	@SuppressWarnings("unchecked")
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		TransactionHistoryReportData data = (TransactionHistoryReportData) report;
		StringBuffer buffer = new StringBuffer();
		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;

		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append(BDConstants.CSV_CONTRACT).append(COMMA)
				.append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		// Get dates for display
		Date asOfDate = currentContract.getContractDates().getAsOfDate();
		Date fromDate = new Date();
		Date toDate = new Date();

		SimpleDateFormat format = new SimpleDateFormat(
				RenderConstants.MEDIUM_MDY_SLASHED);

		buffer.append(BDConstants.CSV_ASOF).append(format.format(asOfDate))
				.append(LINE_BREAK).append(LINE_BREAK);

		try {
			fromDate = format.parse(theForm.getFromDate());
		} catch (ParseException pe) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"ParseException in fromDate getDownloadData() ParticipantTransactionHistoryAction:",
						pe);
			}
		}

		try {
			toDate = format.parse(theForm.getToDate());
		} catch (ParseException pe) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"ParseException in toDate getDownloadData() ParticipantTransactionHistoryAction:",
						pe);
			}
		}

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute(DelegateConstants.ACCOUNT_VIEW_DETAILS);

		try {

			buffer.append(BDConstants.CSV_LAST_FIRST_NAME_SSN);

			buffer.append(LINE_BREAK);

			buffer.append(QUOTE).append(detailsVO.getLastName()).append(QUOTE)
					.append(COMMA);
			buffer.append(QUOTE).append(detailsVO.getFirstName()).append(QUOTE)
					.append(COMMA);
			buffer.append(SSNRender.format(detailsVO.getSsn(), null)).append(
					COMMA);
			buffer.append(LINE_BREAK).append(LINE_BREAK);

			String fromDateStr = DateRender.format(fromDate,
					RenderConstants.MEDIUM_MDY_SLASHED);
			String toDateStr = DateRender.format(toDate,
					RenderConstants.MEDIUM_MDY_SLASHED);

			buffer.append(BDConstants.CSV_HEADER_FROM_DATE).append(COMMA);
			buffer.append(BDConstants.CSV_HEADER_TO_DATE).append(COMMA);
			buffer.append(LINE_BREAK);
			buffer.append(fromDateStr).append(COMMA);
			buffer.append(toDateStr).append(LINE_BREAK);

			buffer.append(LINE_BREAK);

			String transactionType = getTransactionTypeDescription(
					theForm.getTransactionType(), theForm.getTypesDropdown());

			buffer.append(BDConstants.CSV_HEADER_TRANSACTION_TYPE)
					.append(COMMA);
			buffer.append(transactionType).append(LINE_BREAK);
			for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
				buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
				if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
					buffer.append(COMMA);
				}
			}

			buffer.append(LINE_BREAK);
			if (data != null) {
				if (data.getDetails().size() == 0) {
					Content message = null;
					message = ContentCacheManager
							.getInstance()
							.getContentById(
									BDContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
									ContentTypeManager.instance().MESSAGE);

					buffer.append(
							ContentUtility.getContentAttribute(message,
									BDConstants.CSV_PPT_TEXT)).append(
							LINE_BREAK);

				} else {

					buffer.append(LINE_BREAK);

					// Individual Line Items
					Collection<TransactionHistoryItem> theItems = report
							.getDetails();
					for (TransactionHistoryItem theItem : theItems) {

						String transactionDate = DateRender.format(
								theItem.getTransactionDate(), null);
						String payrollDate = BDConstants.SINGLE_SPACE_SYMBOL;
						if (theItem.displayPayrollDate()) {
							payrollDate = DateRender.format(
									theItem.getPayrollEndingDate(), null);
						}

						// For Transaction Types deferral or Allocation
						// Instruction, show Transaction
						// Number as '-'.
						String transactionNumber = BDConstants.SINGLE_SPACE_SYMBOL;
						if (isDeferralOrAllocationInstructionTxnType(theItem
								.getType())) {
							transactionNumber = BDConstants.HYPHON_SYMBOL;
						} else if (!StringUtils.isEmpty(theItem
								.getTransactionNumber())
								&& !theItem.getTransactionNumber().trim()
										.equals(BDConstants.CSV_0)) {
							transactionNumber = theItem.getTransactionNumber();
						}

						if (BDConstants.CSV_WD.equals(theItem.getType())) {

							// Withdrawal line 1
							buffer.append(transactionDate).append(COMMA);
							buffer.append(
									getCsvString(theItem.getTypeDescription1()))
									.append(COMMA);
							buffer.append(
									getCsvString(theItem.getTypeDescription2()))
									.append(COMMA);
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(payrollDate).append(COMMA);
							buffer.append(getCsvString(transactionNumber));
							buffer.append(LINE_BREAK);

							// Withdrawal line 2
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(
									getCsvString(ParticipantTransactionHistoryForm.WITHDRAWAL_AMOUNT))
									.append(COMMA);
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(getCsvString(theItem.getAmount()))
									.append(COMMA);
							buffer.append(getCsvString(null)).append(COMMA);
							buffer.append(getCsvString(null));
							buffer.append(LINE_BREAK);

							// Withdrawal line 3
							if (!BDConstants.HYPHON_SYMBOL.equals(theItem
									.getDisplayChequeAmount())) {
								buffer.append(getCsvString(null)).append(COMMA);
								buffer.append(
										getCsvString(ParticipantTransactionHistoryForm.DISTRIBUTION_AMOUNT))
										.append(COMMA);
								buffer.append(getCsvString(null)).append(COMMA);
								buffer.append(
										getCsvString(theItem.getChequeAmount()))
										.append(COMMA);
								buffer.append(getCsvString(null)).append(COMMA);
								buffer.append(getCsvString(null));
								buffer.append(LINE_BREAK);
							}

						} else {
							buffer.append(transactionDate).append(COMMA);
							buffer.append(
									getCsvString(theItem.getTypeDescription1()))
									.append(COMMA);
							buffer.append(
									getCsvString(theItem.getTypeDescription2()))
									.append(COMMA);

							// For Transaction Types deferral or Allocation
							// Instruction, show Amount as '-'.
							if (isDeferralOrAllocationInstructionTxnType(theItem
									.getType())) {
								buffer.append(BDConstants.HYPHON_SYMBOL)
										.append(COMMA);
							} else {
								buffer.append(getCsvString(theItem.getAmount()))
										.append(COMMA);
							}
							buffer.append(payrollDate).append(COMMA);
							buffer.append(getCsvString(transactionNumber));
						}

						buffer.append(LINE_BREAK);
					}
				}
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(),
					"getDownloadData", "Something wrong with CMA");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * This method checks if the current transaction type is either
	 * "Allocation Instruction" transaction or "Deferral" Transaction.
	 * 
	 * @param transactionType
	 *            - The Transaction type
	 * @return - true if the transaction type is either "Allocation Instruction"
	 *         transaction or "Deferral" Transaction. Else, returns false.
	 */
	private boolean isDeferralOrAllocationInstructionTxnType(
			String transactionType) {

		if (StringUtils.isEmpty(transactionType)) {
			return false;
		}

		boolean isAllocationInstructionTxn = false;
		boolean isDeferralTxn = false;

		if ("CE".equals(transactionType) || "NE".equals(transactionType)) {
			isAllocationInstructionTxn = true;
		}
		if ("AC1".equals(transactionType) || "AC2".equals(transactionType)) {
			isDeferralTxn = true;
		}

		return isAllocationInstructionTxn || isDeferralTxn;
	}

	/**
	 * Gets the transaction type description.
	 * 
	 * @return Returns the transaction type description.
	 */
	protected String getTransactionTypeDescription(String code,
			List<LabelValueBean> typesDropdown) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTransactionTypeDescription");
		}

		// Construct an array of keys if user selected "All" option
		// Array starts with index 1 (i.e. minus key for ALL)
		for (LabelValueBean bean : typesDropdown) {
			if (bean.getValue().equals(code)) {
				return bean.getLabel();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTransactionTypeDescription");
		}

		IllegalArgumentException ae = new IllegalArgumentException(
				BDConstants.CSV_INVALID_CODE + code
						+ BDConstants.CSV_CLOSING_BRACKET);
		throw new SystemException(ae, this.getClass().getName(),
				"getTransactionTypeDescription",
				"Failed to get transaction type description.");

	}

	/**
	 * Gets the transactionTypes
	 * 
	 * @return Returns a Collection
	 */
	protected Collection<String> getTransactionTypeCode(String type,
			List<LabelValueBean> typesDropdown) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTransactionTypeCode");
		}

		// Construct an array of keys if user selected "All" option
		// Array starts with index 1 (i.e. minus key for ALL)
		List<String> types = new ArrayList<String>();
		if (BDConstants.ALL_TYPES.equalsIgnoreCase(type)) {
			for (LabelValueBean bean : typesDropdown) {
				if (BDConstants.ALL_TYPES.equals(bean.getValue())) {
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

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		String forward = null;

		// isErrorsPresent tells us if any errors were found during the
		// doValidate() method. If yes,
		// the doCommon() method is not called. Instead, the report is set as
		// null in the request.
		Boolean isErrorsPresent = (Boolean) request
				.getAttribute(BDConstants.ERRORS_PRESENT);
		if (isErrorsPresent != null && isErrorsPresent) {
			request.setAttribute(BDConstants.REPORT_BEAN, null);
		} else {
			forward = super.doCommon(reportForm, request, response);
		}

		ParticipantTransactionHistoryForm theForm = (ParticipantTransactionHistoryForm) reportForm;
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
		if (request.getAttribute(BdBaseController.ERROR_KEY) != null)
		{
		if (((Collection) request.getAttribute(BdBaseController.ERROR_KEY)).size() == 0) {
			populateParticipantDetails(reportForm, request);
		}}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}

		return forward;
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

	@SuppressWarnings("unchecked")
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

	/**
	 * Check if the profileId was provided and if not retrieve using the
	 * participantId
	 */
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

	/**
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		ParticipantTransactionHistoryForm form = (ParticipantTransactionHistoryForm) reportForm;
		TransactionHistoryReportData data = (TransactionHistoryReportData) report;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.PPT_TXN_HISTORY_PATH, request);

		Element rootElement = doc
				.createRootElement(BDPdfConstants.PPT_TXN_HISTORY);

		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		setRothMessageElement(doc, rootElement, request);

		// Summary Info
		setSummaryInfoXMLElements(doc, rootElement, layoutPageBean, request);

		String bodyHeader1 = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY1_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc,
				bodyHeader1);

		doc.appendTextNode(rootElement, BDPdfConstants.FROM_DATE,
				form.getFromDate());

		doc.appendTextNode(rootElement, BDPdfConstants.TO_DATE,
				form.getToDate());

		// Show the Main report columns only if there are no errors present.
		Boolean isErrorsPresent = (Boolean) request
				.getAttribute(BDConstants.ERRORS_PRESENT);
		if (isErrorsPresent == null || !isErrorsPresent) {
			doc.appendTextNode(rootElement,
					BDPdfConstants.SHOW_MAIN_REPORT_TABLE,
					BDConstants.SPACE_SYMBOL);
		}
		// Sets Txn Type selected
		String transactionType = null;
		try {
			transactionType = getTransactionTypeDescription(
					form.getTransactionType(), form.getTypesDropdown());
		} catch (SystemException systemException) {
			logger.error("Exception occurred while getting TransactionType for PDF"
					+ systemException);
		}
		doc.appendTextNode(rootElement, BDPdfConstants.TXN_TYPE,
				transactionType);

		if (data != null) {
			int noOfRows = getNumberOfRowsInReport(report);
			if (noOfRows > 0) {
				// Transaction Details - start
				Element txnDetailsElement = doc
						.createElement(BDPdfConstants.TXN_DETAILS);
				Element txnDetailElement;
				Iterator iterator = data.getDetails().iterator();
				maxRowsinPDF = form.getCappedRowsInPDF();
				for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
					txnDetailElement = doc
							.createElement(BDPdfConstants.TXN_DETAIL);
					TransactionHistoryItem theItem = (TransactionHistoryItem) iterator
							.next();
					setReportDetailsXMLElements(doc, txnDetailElement, theItem);
					doc.appendElement(txnDetailsElement, txnDetailElement);
					rowCount++;
				}
				doc.appendElement(rootElement, txnDetailsElement);
				// Transaction Details - end
			} else {
				int msgNum = 0;
				Element infoMessagesElement = doc
						.createElement(BDPdfConstants.INFO_MESSAGES);
				Element messageElement = doc
						.createElement(BDPdfConstants.MESSAGE);
				String message = null;
				try {
					message = ContentHelper
							.getMessage(new GenericExceptionWithContentType(
									BDContentConstants.MESSAGE_NO_RESULTS_FOR_SEARCH_CRITERIA,
									ContentTypeManager.instance().MESSAGE,
									false));
				} catch (ContentException exception) {
					logger.error(exception);
				}
				doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM,
						String.valueOf(++msgNum));
				PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT,
						messageElement, doc, message);
				doc.appendElement(infoMessagesElement, messageElement);
				doc.appendElement(rootElement, infoMessagesElement);

			}
		}

		if (form.getPdfCapped()) {
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		if (data != null) {
			if (data.getHasWithdrawlDistribution()) {
				String message = ContentHelper
						.getContentText(
								BDContentConstants.MISCELLANEOUS_TRANSACTION_HISTORY_WITHDRAWAL_MESSAGE,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);
				PdfHelper.convertIntoDOM(BDPdfConstants.WITHDRAWAL_MSG,
						rootElement, doc, message);
			}
		}
		return doc.getDocument();

	}

	/**
	 * This method is used to frame the file name used for the downloaded CSV.
	 * 
	 * @param BaseReportForm
	 * @param HttpServletRequest
	 * @return The file name used for the downloaded CSV.
	 */

	protected String getFileName(BaseReportForm form,
			HttpServletRequest request) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute(DelegateConstants.ACCOUNT_VIEW_DETAILS);

		String csvFileName = PARTICIPANT_TRANSACTION_HISTORY_REPORT_NAME
				+ BDConstants.HYPHON_SYMBOL
				+ currentContract.getContractNumber()
				+ BDConstants.HYPHON_SYMBOL + detailsVO.getLastName()
				+ CSV_EXTENSION;
		return csvFileName;
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * This method sets summary information XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param layoutPageBean
	 * @param request
	 */
	private void setSummaryInfoXMLElements(PDFDocument doc,
			Element rootElement, LayoutPage layoutPageBean,
			HttpServletRequest request) {
		Element summaryInfoElement = doc
				.createElement(BDPdfConstants.SUMMARY_INFO);

		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement,
				doc, subHeader);

		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request
				.getAttribute(DelegateConstants.ACCOUNT_VIEW_DETAILS);
		if (detailsVO != null) {
			String pptName = detailsVO.getLastName()
					+ (StringUtils.isNotEmpty(detailsVO.getFirstName()) ? ", "
							+ detailsVO.getFirstName() : "");
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME,
					pptName);

			String ssnString = SSNRender.format(detailsVO.getSsn(),
					BDConstants.DEFAULT_SSN_VALUE);
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN,
					ssnString);
		}
		doc.appendElement(rootElement, summaryInfoElement);
	}

	/**
	 * This method sets report details XML elements
	 * 
	 * @param doc
	 * @param txnDetailElement
	 * @param theItem
	 */
	private void setReportDetailsXMLElements(PDFDocument doc,
			Element txnDetailElement, TransactionHistoryItem theItem) {
		if (theItem != null) {
			String formattedTxnDate = DateRender.formatByPattern(
					theItem.getTransactionDate(), null,
					RenderConstants.MEDIUM_YMD_DASHED,
					RenderConstants.MEDIUM_MDY_SLASHED);
			doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DATE,
					formattedTxnDate);
			PdfHelper.convertIntoDOM(BDPdfConstants.TXN_TYPE_DESCRIPTION1,
					txnDetailElement, doc, theItem.getTypeDescription1());

			// Show Amount as '-' for Allocation Instruction, Deferral
			// Transactions.
			String amount = BDConstants.HYPHON_SYMBOL;
			if (!isDeferralOrAllocationInstructionTxnType(theItem.getType())) {
				amount = NumberRender.formatByPattern(theItem.getAmount(),
						null, CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
			}
			doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_AMT,
					removeParanthesesAndPrefixMinus(amount));

			if (theItem.displayPayrollDate()) {
				formattedTxnDate = DateRender.formatByPattern(
						theItem.getPayrollEndingDate(), null,
						RenderConstants.MEDIUM_MDY_SLASHED);
				doc.appendTextNode(txnDetailElement,
						BDPdfConstants.PAYROLL_END_DATE, formattedTxnDate);
			}
			if (theItem.getType().equals(TransactionType.WITHDRAWAL)) {

				if (!BDConstants.NO_RULE.equals(theItem
						.getDisplayChequeAmount())) {
					doc.appendTextNode(
							txnDetailElement,
							BDPdfConstants.DISTRIBUTION_AMT,
							ParticipantTransactionHistoryForm.DISTRIBUTION_AMOUNT);
					String chequeAmt = NumberRender.formatByPattern(
							theItem.getChequeAmount(), null,
							CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
					doc.appendTextNode(txnDetailElement,
							BDPdfConstants.TXN_CHEQUE_AMT, chequeAmt);
				}
				if (!BDConstants.ZERO_STRING.trim().equals(
						theItem.getTransactionNumber())) {
					doc.appendTextNode(txnDetailElement,
							BDPdfConstants.TXN_NUMBER,
							theItem.getTransactionNumber());
				}
				doc.appendTextNode(txnDetailElement,
						BDPdfConstants.WITHDRAWAL_AMT,
						ParticipantTransactionHistoryForm.WITHDRAWAL_AMOUNT);

			} else {
				if (theItem.getTypeDescription2() != null) {
					PdfHelper.convertIntoDOM(
							BDPdfConstants.TXN_TYPE_DESCRIPTION2,
							txnDetailElement, doc,
							theItem.getTypeDescription2());
				}
				// Show Transaction Number as '-' for Allocation Instruction,
				// Deferral txns.
				String transactionNumber = BDConstants.HYPHON_SYMBOL;
				if (!isDeferralOrAllocationInstructionTxnType(theItem.getType())) {
					transactionNumber = theItem.getTransactionNumber();
				}
				doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_NUMBER,
						transactionNumber);
			}
		}
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDefault(form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doFilter(form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPage(form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(
			@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doSort(form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/participantTransactionHistory/", params = {"task=printPDF"}, method = {RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("participantTransactionHistoryForm") ParticipantTransactionHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPrintPDF(form, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

}
