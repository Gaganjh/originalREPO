package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the loan repayment transaction page.
 * 
 * @author Maria Lee
 * 
 */
@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({ "loanRepaymentTransactionReportForm" })

public class LoanRepaymentTransactionReportController extends AbstractTransactionReportController {

	@ModelAttribute("loanRepaymentTransactionReportForm")
	public LoanRepaymentTransactionReportForm populateForm() {
		return new LoanRepaymentTransactionReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("default", "/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("sort", "/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("filter", "/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("page", "/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("print", "/transaction/loanRepaymentTransactionReport.jsp");
	}

	private static final String DEFAULT_SORT_FIELD = LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static final String DEFAULT_SSN_VALUE = "xxx-xx-xxxx";

	private static final String[] CSV_COLUMN_HEADINGS = new String[] { "Loan repayment details", "Name", "SSN",
			"Loan number", "Repayment amount ($)", "Principal ($)", "Interest ($)" };

	private static final String CSV_HEADER_TRANSACTION_TYPE = "Transaction type";

	private static final String CSV_HEADER_LOAN_REPAYMENT_TYPE = "Loan repayment";

	private static final String CSV_HEADER_NUMBER_OF_PARTICIPANTS = "Number of participants";

	private static final String CSV_HEADER_TRANSACTION_DATE = "Transaction date";

	private static final String CSV_HEADER_TRANSACTION_NUMBER = "Transaction number";

	private static final String CSV_HEADER_TOTAL_LOAN_REPAYMENT = "Total loan repayment";

	private static final String CSV_HEADER_TOTAL_LOAN_REPAYMENT_AMOUNT = "Total repayment amount ($)";

	private static final String CSV_HEADER_TOTAL_LOAN_PRINCIPAL_AMOUNT = "Total principal ($)";

	private static final String CSV_HEADER_TOTAL_LOAN_INTEREST_AMOUNT = "Total interest ($)";

	/**
	 * Constructor.
	 */
	public LoanRepaymentTransactionReportController() {
		super(LoanRepaymentTransactionReportController.class);
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		LoanRepaymentTransactionReportForm theForm = (LoanRepaymentTransactionReportForm) form;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		String contractNumber = String.valueOf(currentContract.getContractNumber());

		criteria.addFilter(LoanRepaymentTransactionReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());

		request.setAttribute(Constants.TRANSACTION_DATE, theForm.getTransactionDate());
		criteria.addFilter(LoanRepaymentTransactionReportData.FILTER_CONTRACT_NUMBER, contractNumber);
	}

	/**
	 * If primary sort is last name, then secondary sorts are first name, ssn &
	 * loan id in the same sort sequence.
	 * 
	 * If primary sort is ssn, then secondary sorts are first name, loan id in
	 * the same sort sequence.
	 * 
	 * For all other sorts, secondary sorts are as follows: last name, first
	 * name & ssn in the opposite sort sequence, i.e. if primary is ascending,
	 * secondaries are descending and vice versa.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		/*
		 * quirky thing - the amount columns are stored as negatives in the
		 * database the list was returned with the sign reversed in order for
		 * the sort to work, we reverse the sort direction
		 */
		if (sortField.equalsIgnoreCase(LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT)
				|| sortField.equalsIgnoreCase(LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL)
				|| sortField.equalsIgnoreCase(LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST)) {
			if (sortDirection.equalsIgnoreCase(ReportSort.ASC_DIRECTION)) {
				sortDirection = ReportSort.DESC_DIRECTION;
			} else {
				sortDirection = ReportSort.ASC_DIRECTION;
			}
		}

		/* primary sort */
		criteria.insertSort(sortField, sortDirection);

		/* logic for secondary sorts */
		/* sort on name */
		if (sortField.equals(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME)) {
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME, sortDirection);
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN, sortDirection);
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER, sortDirection);
		} else {
			if (sortField.equals(LoanRepaymentTransactionReportData.SORT_FIELD_SSN)) {
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME, sortDirection);
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME, sortDirection);
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER, sortDirection);
				/* all other sorts */
			} else {
				if (sortDirection.equals(ReportSort.ASC_DIRECTION)) {
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
							ReportSort.DESC_DIRECTION);
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
							ReportSort.DESC_DIRECTION);
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN, ReportSort.DESC_DIRECTION);
				} else {
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
							ReportSort.ASC_DIRECTION);
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
							ReportSort.ASC_DIRECTION);
					criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN, ReportSort.ASC_DIRECTION);
				}
			}
		}
	}

	protected String getReportId() {
		return LoanRepaymentTransactionReportData.REPORT_ID;
	}

	protected String getReportName() {
		return LoanRepaymentTransactionReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		LoanRepaymentTransactionReportData data = (LoanRepaymentTransactionReportData) report;
		StringBuffer buffer = new StringBuffer();

		Contract currentContract = getUserProfile(request).getCurrentContract();
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		LoanRepaymentTransactionReportForm form = (LoanRepaymentTransactionReportForm) reportForm;

		buffer.append(CSV_HEADER_TRANSACTION_TYPE).append(COMMA);
		buffer.append(CSV_HEADER_LOAN_REPAYMENT_TYPE).append(LINE_BREAK);

		buffer.append(CSV_HEADER_NUMBER_OF_PARTICIPANTS).append(COMMA);
		buffer.append(data.getNumberOfParticipants()).append(LINE_BREAK);

		buffer.append(CSV_HEADER_TRANSACTION_DATE).append(COMMA);
		buffer.append(form.getTransactionDate()).append(LINE_BREAK);

		buffer.append(CSV_HEADER_TRANSACTION_NUMBER).append(COMMA);
		buffer.append(form.getTransactionNumber()).append(LINE_BREAK).append(LINE_BREAK);

		buffer.append(COMMA);
		buffer.append(CSV_HEADER_TOTAL_LOAN_REPAYMENT_AMOUNT).append(COMMA);
		buffer.append(CSV_HEADER_TOTAL_LOAN_PRINCIPAL_AMOUNT).append(COMMA);
		buffer.append(CSV_HEADER_TOTAL_LOAN_INTEREST_AMOUNT).append(LINE_BREAK);

		buffer.append(CSV_HEADER_TOTAL_LOAN_REPAYMENT).append(COMMA);
		buffer.append(data.getTotalRepaymentAmount()).append(COMMA);
		buffer.append(data.getTotalPrincipalAmount()).append(COMMA);
		buffer.append(data.getTotalInterestAmount()).append(LINE_BREAK).append(LINE_BREAK);

		for (int i = 0; i < CSV_COLUMN_HEADINGS.length; i++) {
			buffer.append(CSV_COLUMN_HEADINGS[i]);
			if (i != CSV_COLUMN_HEADINGS.length - 1) {
				buffer.append(COMMA);
			}
		}
		// SSE S024 determine wheather the ssn should be masked on the csv
		// report
		boolean maskSSN = true;// set the mask ssn flag to true as a default
		UserProfile user = getUserProfile(request);
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}
		/*
		 * Individual Line Items
		 */
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			LoanRepaymentTransactionItem theItem = (LoanRepaymentTransactionItem) iterator.next();
			ParticipantVO participant = theItem.getParticipant();

			// String ssnString = SSNRender.format(participant.getSsn(),
			// DEFAULT_SSN_VALUE);
			String ssnString = SSNRender.format(participant.getSsn(), DEFAULT_SSN_VALUE, maskSSN);
			buffer.append(COMMA);
			StringBuffer nameBuffer = new StringBuffer();

			/*
			 * build a buffer string for the name and last name which will then
			 * be treated as a whole string for getCsvString()
			 */
			nameBuffer.append(participant.getLastName()).append(COMMA).append(" ").append(participant.getFirstName());
			buffer.append(getCsvString(nameBuffer.toString())).append(COMMA);
			buffer.append(getCsvString(ssnString)).append(COMMA);

			/* converts loanNumber from short to Short for getCsvString() */
			buffer.append(getCsvString(new Short(theItem.getLoanNumber()))).append(COMMA);

			buffer.append(getCsvString(Double.toString(theItem.getRepaymentAmount().doubleValue() * -1))).append(COMMA);
			buffer.append(getCsvString(Double.toString(theItem.getPrincipalAmount().doubleValue() * -1))).append(COMMA);
			buffer.append(getCsvString(Double.toString(theItem.getInterestAmount().doubleValue() * -1))).append(COMMA);
		}

		return buffer.toString().getBytes();
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", method = RequestMethod.GET)
	public String doDefualt(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=filter", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String doFilter(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=page", method = RequestMethod.GET)
	public String doPage(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=sort", method = RequestMethod.GET)
	public String doSort(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=download", method = RequestMethod.GET)
	public String doDownload(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=downloadAll", method = RequestMethod.GET)
	public String doDownloadAll(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/loanRepaymentTransactionReport/", params = "task=print", method = RequestMethod.GET)
	public String doPrint(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
