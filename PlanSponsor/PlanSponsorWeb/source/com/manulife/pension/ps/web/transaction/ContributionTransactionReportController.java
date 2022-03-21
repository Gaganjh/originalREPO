package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionTypeDescription;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.ParticipantMoneyTypeAllocation;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * ContributionTransactionReportReportAction ReportAction class This class is
 * used to get the information for the Transaction Details / Contribution page
 * 
 * @author drotele
 */
@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({ "contributionTransactionReportForm" })

public class ContributionTransactionReportController extends AbstractTransactionReportController {

	@ModelAttribute("contributionTransactionReportForm")
	public ContributionTransactionReportForm populateForm() {
		return new ContributionTransactionReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/contributionTransactionReport.jsp");
		forwards.put("default", "/transaction/contributionTransactionReport.jsp");
		forwards.put("sort", "/transaction/contributionTransactionReport.jsp");
		forwards.put("filter", "/transaction/contributionTransactionReport.jsp");
		forwards.put("page", "/transaction/contributionTransactionReport.jsp");
		forwards.put("print", "/transaction/contributionTransactionReport.jsp");
	}

	private static Logger logger = Logger.getLogger(ContributionTransactionReportController.class);
	protected static final String DEFAULT_SORT_FIELD = ContributionTransactionReportData.SORT_FIELD_NAME;
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	protected static final String DOWNLOAD_COLUMN_HEADING = "";
	protected static final int DEFAULT_PAGE_SIZE = 35;

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_MONEY_TYPE_PREFIX = "Money type";
	private static final String DOWNLOAD_COLUMN_HEADING_MONEY_TYPE = "Amount ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_NAMES = "Name, SSN";

	private static final String DOWNLOAD_COLUMN_HEADING_EE_CONTRIBUTIONS = "Employee contributions ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_ER_CONTRIBUTIONS = "Employer contributions ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS = "Total contributions ($)";

	private static final String NUMBER_FORMAT_PATTERN = "########0.00";
	private static final String SSN_DEFAULT_VALUE = "000000000";
	protected static final String CURRENCY = "";

	// money type column ordering
	private static final Map<String, Integer> DETAIL_EE_ID_ORDER_MAP;
	private static final Map<String, Integer> DETAIL_ER_ID_ORDER_MAP;

	static {
		int order;

		order = 0;
		DETAIL_EE_ID_ORDER_MAP = new HashMap<String, Integer>();
		DETAIL_EE_ID_ORDER_MAP.put("EEDEF", order++);
		DETAIL_EE_ID_ORDER_MAP.put("EEROT", order++);

		order = 0;
		DETAIL_ER_ID_ORDER_MAP = new HashMap<String, Integer>();
		DETAIL_ER_ID_ORDER_MAP.put("ERMAT", order++);
		DETAIL_ER_ID_ORDER_MAP.put("ERPS", order++);
		DETAIL_ER_ID_ORDER_MAP.put("ERCON", order++);
		DETAIL_ER_ID_ORDER_MAP.put("ERMP", order++);
	}

	/**
	 * Constructor.
	 */
	public ContributionTransactionReportController() {
		super(ContributionTransactionReportController.class);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ContributionTransactionReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ContributionTransactionReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * This method is called to populate a report criteria from the report action
	 * form and the request. It is called right before getReportData is called.
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request) {

		ContributionTransactionReportForm theForm = (ContributionTransactionReportForm) form;

		// get the user profile object and get the current contractId
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		criteria.addFilter(ContributionTransactionReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));

		// add Transaction Id to the Criteria
		criteria.addFilter(ContributionTransactionReportData.FILTER_TRANSACTION_NUMBER, theForm.getTransactionNumber());

		// web or download report
		String task = getTask(request);
		String reportType;
		if (DOWNLOAD_TASK.equals(task)) {
			reportType = ContributionTransactionReportData.FILTER_REPORT_TYPE_DOWNLOAD;
		} else {
			reportType = ContributionTransactionReportData.FILTER_REPORT_TYPE_PAGE;
		}
		criteria.addFilter(ContributionTransactionReportData.FILTER_REPORT_TYPE, reportType);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getPageSize()
	 */
	protected int getPageSize(HttpServletRequest request) {
		return DEFAULT_PAGE_SIZE;
	}

	/**
	 * Obtains the actual report data.
	 * 
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		ContributionTransactionReportData ctData;
		ctData = (ContributionTransactionReportData) super.getReportData(reportId, reportCriteria, request);

		return ctData;
	}

	/**
	 * If it's sort by Date, secondary sort is transaction number. If it's sort by
	 * amount, secondary sort is date and number. If it's sort by number, secondary
	 * sort is date.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		// get sort opposite direction
		String oppositeSortDirection = "";
		if (ReportSort.ASC_DIRECTION.equals(sortDirection)) {
			oppositeSortDirection = ReportSort.DESC_DIRECTION;
		} else {
			oppositeSortDirection = ReportSort.ASC_DIRECTION;
		}

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(ContributionTransactionReportData.SORT_FIELD_NAME)) {
			// sort by name : Name | SSN
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_SSN, sortDirection);
		} else if (sortField.equals(ContributionTransactionReportData.SORT_FIELD_SSN)) {
			// just SSN
		} else if (sortField.equals(ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION)) {
			// EmployEE Contribution : EEContrib | Name(a) | SSN(a)
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_NAME, oppositeSortDirection);
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_SSN, oppositeSortDirection);
		} else if (sortField.equals(ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION)) {
			// EmployER Contribution : ERContrib | Name(a) | SSN(a)
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_NAME, oppositeSortDirection);
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_SSN, oppositeSortDirection);
		} else if (sortField.equals(ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION)) {
			// Tota Contribution : Total Contrib | Name(a) | SSN(a)
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_NAME, oppositeSortDirection);
			criteria.insertSort(ContributionTransactionReportData.SORT_FIELD_SSN, oppositeSortDirection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateDownloadData(java.io.
	 * PrintWriter, com.manulife.pension.ps.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");

		ContributionTransactionReportData data = (ContributionTransactionReportData) report;
		StringBuilder buffer = new StringBuilder();
		UserProfile user = getUserProfile(request);
		Contract currentContract = user.getCurrentContract();
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// Transaction Type
		buffer.append("Transaction type").append(COMMA)
				.append(TransactionTypeDescription.getDescription(data.getTransactionType())).append(LINE_BREAK);

		// Payroll Ending
		buffer.append("Payroll ending").append(COMMA)
				.append(DateRender.format(data.getPayrollEndingDate(), RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);

		// Number of Participants
		buffer.append("Number of participants").append(COMMA).append(data.getNumberOfParticipants()).append(LINE_BREAK);

		// Transaction date
		buffer.append("Invested date").append(COMMA);
		if (data.getTransactionDate() != null) {
			buffer.append(DateRender.format(data.getTransactionDate(), RenderConstants.MEDIUM_YMD_SLASHED));
		}
		buffer.append(LINE_BREAK);

		// Transaction number
		buffer.append("Transaction number").append(COMMA);
		if (data.getTransactionNumber() != null) {
			buffer.append(data.getTransactionNumber());
		}

		buffer.append(LINE_BREAK).append(LINE_BREAK);

		// Totals (Regular Contributions)
		// headings
		String headingEeEr = "";
		if (data.isHasEmployeeContribution()) {
			headingEeEr = headingEeEr + DOWNLOAD_COLUMN_HEADING_EE_CONTRIBUTIONS + COMMA;
		}
		if (data.isHasEmployerContribution()) {
			headingEeEr = headingEeEr + DOWNLOAD_COLUMN_HEADING_ER_CONTRIBUTIONS + COMMA;
		}

		buffer.append(COMMA).append(COMMA).append(headingEeEr);

		// logic for the totals
		if (data.isHasEmployerContribution() && data.isHasEmployeeContribution()) {
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS);
		}

		buffer.append(LINE_BREAK);
		buffer.append("Contributions").append(COMMA).append(COMMA);

		// employee contribution
		if (data.isHasEmployeeContribution()) {
			BigDecimal eeContribution = new BigDecimal(0d);
			if (data.getTotalEmployeeContribution() != null) {
				eeContribution = data.getTotalEmployeeContribution();
			}
			buffer.append(CURRENCY)
					.append(NumberRender.formatByPattern(eeContribution, ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN))
					.append(COMMA);
		}

		// employer contribution
		if (data.isHasEmployerContribution()) {
			BigDecimal erContribution = new BigDecimal(0d);
			if (data.getTotalEmployeeContribution() != null) {
				erContribution = data.getTotalEmployerContribution();
			}
			buffer.append(CURRENCY)
					.append(NumberRender.formatByPattern(erContribution, ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN))
					.append(COMMA);
		}

		if (data.isHasEmployerContribution() && data.isHasEmployeeContribution()) {
			buffer.append(CURRENCY).append(NumberRender.formatByPattern(data.getTotalContribution(), ZERO_AMOUNT_STRING,
					NUMBER_FORMAT_PATTERN)).append(COMMA);
		}
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// sort money types
		String[] detailMtArray = new String[data.getMoneyTypes().size()];
		Map<String, Integer> detailMtIndex = new HashMap<String, Integer>();
		buildDetailMoneyTypeIndex(detailMtArray, detailMtIndex, data.getMoneyTypes());

		// money type summary
		buffer.append("Total contributions by money type").append(COMMA)
				.append(DOWNLOAD_COLUMN_HEADING_MONEY_TYPE_PREFIX).append(COMMA)
				.append(DOWNLOAD_COLUMN_HEADING_MONEY_TYPE);
		if (data.isHasEmployerContribution() && data.isHasEmployeeContribution()) {
			buffer.append(COMMA).append(DOWNLOAD_COLUMN_HEADING_MONEY_TYPE);
		}

		buffer.append(LINE_BREAK);
		buffer.append(getMoneyTypes(data.getMoneyTypes(), detailMtArray, data.isHasEmployeeContribution()));
		buffer.append(LINE_BREAK);

		// Report Details heading
		buffer.append(DOWNLOAD_COLUMN_HEADING_NAMES);

		for (String mtHeading : detailMtArray) {

			buffer.append(COMMA);
			buffer.append(QUOTE);

			buffer.append(findMoneyTypeForId(mtHeading, data.getMoneyTypes()).getShortDescription());

			buffer.append(QUOTE);

		}

		buffer.append(COMMA);
		buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS);
		buffer.append(LINE_BREAK);

		// report details data
		if (data.getDetails() != null) {

			// determine if SSN should be masked
			boolean maskSSN = true;
			try {
				maskSSN = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());
			} catch (SystemException se) {
				logger.error(se);
			}

			buffer.append(getReportDetails(detailMtIndex, (List) data.getDetails(), maskSSN));

		}
		buffer.append(LINE_BREAK);

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	/**
	 * Retrieves Money Types
	 * 
	 * @param data
	 * @return String
	 */
	static private String getMoneyTypes(List<MoneyTypeAmount> data, String[] order, boolean hasEmployeeContribution) {

		StringBuilder moneyTypes = new StringBuilder();

		if (data != null) {

			for (String mtid : order) {

				MoneyTypeAmount mt = findMoneyTypeForId(mtid, data);

				// money type description
				moneyTypes.append(COMMA);
				moneyTypes.append(QUOTE + mt.getShortDescription() + QUOTE).append(COMMA);

				// EE/ER amount column formatting
				if (hasEmployeeContribution && mt.isEmployerContribution()) {
					moneyTypes.append(COMMA);
				}

				// allocated amount
				if (mt.getAmount() != null) {
					moneyTypes.append(CURRENCY).append(
							NumberRender.formatByPattern(mt.getAmount(), ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				}

				moneyTypes.append(LINE_BREAK);

			}

		}

		return moneyTypes.toString();

	}

	/**
	 * Retrieves Transaction Detail Data
	 * 
	 * @param data
	 * @return String
	 */
	private String getReportDetails(Map<String, Integer> detailMtIndex, List data, boolean maskSSN) {

		StringBuilder reportDetails = new StringBuilder();
		BigDecimal[] amounts = new BigDecimal[detailMtIndex.size()];

		if (data != null) {
			for (Iterator i = data.iterator(); i.hasNext();) {

				ContributionTransactionItem item = (ContributionTransactionItem) i.next();

				// append to the buffer
				reportDetails.append(QUOTE + item.getParticipant().getWholeName() + QUOTE).append(COMMA);

				// Mask SSN
				reportDetails.append(SSNRender.format(item.getParticipant().getSsn(), SSN_DEFAULT_VALUE, maskSSN));

				// allocations

				Arrays.fill(amounts, null);
				for (ParticipantMoneyTypeAllocation allocation : item.getAllocations()) {
					amounts[detailMtIndex.get(allocation.getId())] = allocation.getAmount();
				}
				for (BigDecimal amount : amounts) {
					reportDetails.append(COMMA);
					reportDetails.append(CURRENCY).append(NumberRender.formatByPattern(
							amount == null ? BigDecimal.ZERO : amount, ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				}

				// total contributions
				if (item.getTotalContribution() != null) {
					reportDetails.append(COMMA).append(CURRENCY).append(NumberRender
							.formatByPattern(item.getTotalContribution(), ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				}

				reportDetails.append(LINE_BREAK);

			}

		}

		return reportDetails.toString();

	}

	private static void buildDetailMoneyTypeIndex(String[] mtIndexMap, Map<String, Integer> orderMap,
			Collection<MoneyTypeAmount> moneyTypes) {

		MoneyTypeAmount[] mtArray = moneyTypes.toArray(new MoneyTypeAmount[0]);
		Arrays.sort(mtArray, new DetailMoneyTypeComparator());
		for (int i = 0; i < mtArray.length; i++) {
			mtIndexMap[i] = mtArray[i].getId();
			orderMap.put(mtArray[i].getId(), i);
		}

	}

	private static MoneyTypeAmount findMoneyTypeForId(String id, List<MoneyTypeAmount> moneyTypes) {

		MoneyTypeAmount mta = null;

		for (MoneyTypeAmount mt : moneyTypes) {
			if (id.equals(mt.getId())) {
				mta = mt;
				break;
			}
		}

		assert mta != null;
		return mta;

	}

	private static class DetailMoneyTypeComparator implements Comparator<MoneyTypeAmount> {

		public int compare(MoneyTypeAmount mt1, MoneyTypeAmount mt2) {

			int comparison = 0;

			if (mt1.isEmployeeContribution()) {
				if (mt2.isEmployerContribution()) {
					comparison = -1;
				} else {
					comparison = compareSameCategory(DETAIL_EE_ID_ORDER_MAP, mt1, mt2);
				}
			} else {
				if (mt2.isEmployeeContribution()) {
					comparison = 1;
				} else {
					comparison = compareSameCategory(DETAIL_ER_ID_ORDER_MAP, mt1, mt2);
				}
			}

			return comparison;

		}

		private int compareSameCategory(Map<String, Integer> catOrderMap, MoneyTypeAmount mt1, MoneyTypeAmount mt2) {

			int comparison = 0;

			String id1 = mt1.getId();
			String id2 = mt2.getId();

			Integer order1 = catOrderMap.get(id1);
			Integer order2 = catOrderMap.get(id2);

			if (order1 != null) {
				if (order2 != null) {
					comparison = order1.compareTo(order2);
				} else {
					comparison = -1;
				}
			} else {
				if (order2 != null) {
					comparison = 1;
				} else {
					comparison = id1.compareTo(id2);
				}
			}

			if (comparison == 0) {
				comparison = mt1.getShortDescription().compareTo(mt2.getShortDescription());
			}

			if (comparison == 0) {
				comparison = mt1.getLongDescription().compareTo(mt2.getLongDescription());
			}

			return comparison;

		}
	}

	@RequestMapping(value ="/contributionTransactionReport/", method = { RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/contributionTransactionReport/", params = { "task=filter" }, method = {
			RequestMethod.GET })
	public String doFilter(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/contributionTransactionReport/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/contributionTransactionReport/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/contributionTransactionReport/", params = { "task=download" }, method = {
			RequestMethod.GET })
	public String doDownload(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/contributionTransactionReport/", params = { "task=print" }, method = {
			RequestMethod.GET })
	public String doPrint(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/contributionTransactionReport/", params = { "task=downloadAll" }, method = {
			RequestMethod.GET })
	public String doDownloadAll(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();
			}
		}
		String forward = super.doDownloadAll(form, request, response);
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
