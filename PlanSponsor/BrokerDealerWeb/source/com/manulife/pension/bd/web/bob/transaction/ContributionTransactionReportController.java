package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.MoneyTypeAmount;
import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData.ParticipantMoneyTypeAllocation;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * ContributionTransactionReportReportAction ReportAction class This class is
 * used to get the information for the Transaction Details / Contribution page
 * 
 * @author HarikishanRao Lomte
 */
@Controller
@RequestMapping( value ="/bob")
@SessionAttributes({"contributionTransactionReportForm"})

public class ContributionTransactionReportController extends
		AbstractTransactionReportController {
	@ModelAttribute("contributionTransactionReportForm")
	public ContributionTransactionReportForm populateForm() {
		return new ContributionTransactionReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/contributionTransactionReport.jsp");
		forwards.put("default","/transaction/contributionTransactionReport.jsp");
		forwards.put("sort","/transaction/contributionTransactionReport.jsp");
		forwards.put("filter","/transaction/contributionTransactionReport.jsp");
		forwards.put("page","/transaction/contributionTransactionReport.jsp");
		forwards.put("print","/transaction/contributionTransactionReport.jsp");
		
	}

	protected static final String DEFAULT_SORT_FIELD = ContributionTransactionReportData.SORT_FIELD_NAME;
	protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	protected static final int DEFAULT_PAGE_SIZE = 35;

	// CSV Export Related
	private static final String DOWNLOAD_COLUMN_HEADING_MONEY_TYPE = "Money type, Amount ($), Money type, Amount ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_NAMES = "Name, SSN";

	private static final String DOWNLOAD_COLUMN_HEADING_EE_CONTRIBUTIONS = "Employee Contributions ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_ER_CONTRIBUTIONS = "Employer Contributions ($)";
	private static final String DOWNLOAD_COLUMN_HEADING_TOTAL_EE_CONTRIBUTIONS = "Employee Contributions($)";
	private static final String DOWNLOAD_COLUMN_HEADING_TOTAL_ER_CONTRIBUTIONS = "Employer Contributions($)";
	private static final String DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS = "Total Contributions($)";

	private static final String TRANSACTION_DETAILS_CONTRIBUTION_REPORT = "TxnDetailsContributions";
	private static final String XSLT_FILE_KEY_NAME = "ContributionTransactionReport.XSLFile";
	private static final String NUMBER_FORMAT_PATTERN = "########0.00";

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
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return ContributionTransactionReportData.REPORT_ID;
	}

	/**
	 * @see BaseReportController#getReportName()
	 */
	protected String getReportName() {
		return ContributionTransactionReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.bd.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * This method is called to populate a report criteria from the report
	 * action form and the request. It is called right before getReportData is
	 * called.
	 * 
	 * @see com.manulife.pension.bd.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.bd.web.report.ReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		ContributionTransactionReportForm theForm = (ContributionTransactionReportForm) form;

		// get the BobContext object and get the current contractId
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();
		criteria.addFilter(
				ContributionTransactionReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));

		// add Transaction Id to the Criteria
		criteria.addFilter(
				ContributionTransactionReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());

		// web or download report
		String task = getTask(request);
		String reportType;
		if (DOWNLOAD_TASK.equals(task)) {
			reportType = ContributionTransactionReportData.FILTER_REPORT_TYPE_DOWNLOAD;
		} else {
			reportType = ContributionTransactionReportData.FILTER_REPORT_TYPE_PAGE;
		}
		criteria.addFilter(
				ContributionTransactionReportData.FILTER_REPORT_TYPE,
				reportType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.report.ReportAction#getPageSize()
	 */
	protected int getPageSize(HttpServletRequest request) {
		return DEFAULT_PAGE_SIZE;
	}

	/**
	 * Obtains the actual report data. Also, the Resource Limit Exceeded
	 * Exception is being converted to a SystemException, so that the user
	 * cannot see a Resource Limit Exceeded Exception message. Instead, he will
	 * see a Technical Difficulties message.
	 */
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		ContributionTransactionReportData ctData = null;
		try {
			ctData = (ContributionTransactionReportData) super.getReportData(
					reportId, reportCriteria, request);
		} catch (ResourceLimitExceededException e) {
			logger.error("Received a ResourceLimitExceededException: ", e);
			throw new SystemException(
					e,
					"ResourceLimitExceededException occurred. Showing it as TECHNICAL_DIFFICULTIES to the user.");
		}

		// format MoneyTypeAmount array
		if (ctData != null && !DOWNLOAD_TASK.equals(getTask(request))) {
			if (ctData.getMoneyTypes() != null
					&& (ctData.getMoneyTypes().size()) % 2 != 0) {
				// add an empty element
				ctData.getMoneyTypes().add(
						new MoneyTypeAmount(null, null, null, true, null));
			}
		}
		return ctData;
	}

	/**
	 * If it's sort by Date, secondary sort is transaction number. If it's sort
	 * by amount, secondary sort is date and number. If it's sort by number,
	 * secondary sort is date.
	 * 
	 * @see BaseReportController#populateSortCriteria
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {

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
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_SSN,
					sortDirection);
		} else if (sortField
				.equals(ContributionTransactionReportData.SORT_FIELD_SSN)) {
			// just SSN
		} else if (sortField
				.equals(ContributionTransactionReportData.SORT_FIELD_EMPLOYEE_CONTRIBUTION)) {
			// EmployEE Contribution : EEContrib | Name(a) | SSN(a)
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_NAME,
					oppositeSortDirection);
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_SSN,
					oppositeSortDirection);
		} else if (sortField
				.equals(ContributionTransactionReportData.SORT_FIELD_EMPLOYER_CONTRIBUTION)) {
			// EmployER Contribution : ERContrib | Name(a) | SSN(a)
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_NAME,
					oppositeSortDirection);
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_SSN,
					oppositeSortDirection);
		} else if (sortField
				.equals(ContributionTransactionReportData.SORT_FIELD_TOTAL_CONTRIBUTION)) {
			// Tota Contribution : Total Contrib | Name(a) | SSN(a)
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_NAME,
					oppositeSortDirection);
			criteria.insertSort(
					ContributionTransactionReportData.SORT_FIELD_SSN,
					oppositeSortDirection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.bd.web.report.ReportAction#populateDownloadData(
	 * java.io.PrintWriter, com.manulife.pension.bd.web.report.ReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");

		ContributionTransactionReportData data = (ContributionTransactionReportData) report;

		request.setAttribute("transactionDate", data.getTransactionDate());
		StringBuffer buffer = new StringBuffer();

		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append("Contract").append(COMMA)
				.append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		buffer.append(LINE_BREAK);

		// get the content objects
		Content message = null;

		// Contract Contribution Details Summary
		buffer.append("Contract Contribution Details Summary").append(
				LINE_BREAK);

		// Transaction Type
		buffer.append("Transaction Type").append(COMMA)
				.append(MoneySourceDescription.CONTRIBUTION).append(LINE_BREAK);

		// Payroll Ending
		buffer.append("Payroll Ending Date")
				.append(COMMA)
				.append(DateRender.format(data.getPayrollEndingDate(),
						RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);

		// Number of Participants
		buffer.append("Number of Participants").append(COMMA)
				.append(data.getNumberOfParticipants()).append(LINE_BREAK);

		// Transaction date
		buffer.append("Invested Date").append(COMMA);
		if (data.getTransactionDate() != null) {
			buffer.append(DateRender.format(data.getTransactionDate(),
					RenderConstants.MEDIUM_MDY_SLASHED));
		}
		buffer.append(LINE_BREAK);

		// Transaction number
		buffer.append("Transaction Number").append(COMMA);
		if (data.getTransactionNumber() != null) {
			buffer.append(data.getTransactionNumber());
		}

		buffer.append(LINE_BREAK);

		// Totals (Regular Contributions)
		// headings
		String headingEeEr = "";

		if (data.isHasEmployeeContribution()) {
			headingEeEr = headingEeEr
					+ DOWNLOAD_COLUMN_HEADING_EE_CONTRIBUTIONS + COMMA;
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_EE_CONTRIBUTIONS)
					.append(COMMA);
			BigDecimal eeContribution = new BigDecimal(0d);
			if (data.getTotalEmployeeContribution() != null) {
				eeContribution = data.getTotalEmployeeContribution();
			}
			buffer.append(BDConstants.DOLLAR_SIGN)
					.append(removeParanthesesAndPrefixMinus(NumberRender
							.formatByPattern(eeContribution,
									ZERO_AMOUNT_STRING,
									BDConstants.AMOUNT_FORMAT))).append(COMMA);
			buffer.append(LINE_BREAK);
		}
		if (data.isHasEmployerContribution()) {
			headingEeEr = headingEeEr
					+ DOWNLOAD_COLUMN_HEADING_ER_CONTRIBUTIONS + COMMA;
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_ER_CONTRIBUTIONS)
					.append(COMMA);
			BigDecimal erContribution = new BigDecimal(0d);
			if (data.getTotalEmployeeContribution() != null) {
				erContribution = data.getTotalEmployerContribution();
			}
			buffer.append(BDConstants.DOLLAR_SIGN)
					.append(removeParanthesesAndPrefixMinus(NumberRender
							.formatByPattern(erContribution,
									ZERO_AMOUNT_STRING,
									BDConstants.AMOUNT_FORMAT))).append(COMMA);
			buffer.append(LINE_BREAK);

		}

		// logic for the totals
		if (data.isHasEmployerContribution()
				&& data.isHasEmployeeContribution()) {
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS).append(
					COMMA);
			buffer.append(BDConstants.DOLLAR_SIGN)
					.append(removeParanthesesAndPrefixMinus(NumberRender
							.formatByPattern(data.getTotalContribution(),
									ZERO_AMOUNT_STRING,
									BDConstants.AMOUNT_FORMAT))).append(COMMA);
			buffer.append(LINE_BREAK);
		}

		buffer.append(LINE_BREAK);

		buffer.append("Total contributions by money type");
		buffer.append(COMMA).append("Money Type").append(COMMA)
				.append("Amount ($)");
		if (data.isHasEmployerContribution()
				&& data.isHasEmployeeContribution()) {
			buffer.append(COMMA).append("Amount ($)");
		}
		buffer.append(LINE_BREAK);

		buffer.append(getMoneyTypes(data.getMoneyTypes(),
				data.isHasEmployeeContribution()));
		buffer.append(LINE_BREAK);

		buffer.append("Contribution Details");
		buffer.append(LINE_BREAK);
		// buffer.append(DOWNLOAD_COLUMN_HEADING_NAMES).append(COMMA).append(headingEeEr);

		buffer.append(DOWNLOAD_COLUMN_HEADING_NAMES);

		Map<String, Integer> detailMtIndex = new HashMap<String, Integer>();
		buildDetailMoneyTypeIndex(detailMtIndex, data.getMoneyTypes());

		String[] detailMtArray = new String[detailMtIndex.size()];
		for (Entry<String, Integer> indexMapping : detailMtIndex.entrySet()) {
			detailMtArray[indexMapping.getValue()] = indexMapping.getKey();
		}

		for (String mtHeading : detailMtArray) {

			// find appropriate money type heading
			for (MoneyTypeAmount mt : data.getMoneyTypes()) {
				if (mtHeading != null && mtHeading.equals(mt.getId())) {
					buffer.append(COMMA);
					buffer.append(mt.getShortDescription());
					break;
				}
			}

		}
		buffer.append(COMMA);
		buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS);
		buffer.append(LINE_BREAK);
		// Report Details
		/*
		 * if (data.isHasEmployerContribution() &&
		 * data.isHasEmployeeContribution()) {
		 * buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL_CONTRIBUTIONS); }
		 */

		buffer.append(LINE_BREAK);
		if (data.getDetails() != null) {
			buffer.append(getReportDetails(detailMtIndex,
					(List) data.getDetails()));
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
	private String getMoneyTypes(List<MoneyTypeAmount> data,
			boolean hasEmployeeContribution) {

		StringBuilder moneyTypes = new StringBuilder();

		if (data != null) {

			for (MoneyTypeAmount mt : data) {

				// money type description
				moneyTypes.append(COMMA);
				moneyTypes.append(QUOTE + mt.getLongDescription() + QUOTE)
						.append(COMMA);

				// EE/ER amount column formatting
				if (hasEmployeeContribution && mt.isEmployerContribution()) {
					moneyTypes.append(COMMA);
				}

				// allocated amount
				if (mt.getAmount() != null) {
					moneyTypes.append("").append(
							NumberRender.formatByPattern(mt.getAmount(),
									ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
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
	private String getReportDetails(Map<String, Integer> detailMtIndex,
			List data) {

		StringBuilder reportDetails = new StringBuilder();
		BigDecimal[] amounts = new BigDecimal[detailMtIndex.size()];

		if (data != null) {
			for (Iterator i = data.iterator(); i.hasNext();) {

				ContributionTransactionItem item = (ContributionTransactionItem) i
						.next();

				// append to the buffer
				reportDetails.append(
						QUOTE + item.getParticipant().getWholeName() + QUOTE)
						.append(COMMA);

				// Mask SSN
				reportDetails.append(SSNRender.format(item.getParticipant()
						.getSsn(), BDConstants.SSN_DEFAULT_VALUE, true));
				// .append(COMMA);

				// allocations

				Arrays.fill(amounts, null);
				for (ParticipantMoneyTypeAllocation allocation : item
						.getAllocations()) {
					amounts[detailMtIndex.get(allocation.getId())] = allocation
							.getAmount();
				}
				for (BigDecimal amount : amounts) {
					reportDetails.append(COMMA);
					reportDetails.append("").append(
							NumberRender.formatByPattern(
									amount == null ? BigDecimal.ZERO : amount,
									ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				}

				// total contributions
				if (item.getTotalContribution() != null) {
					reportDetails
							.append(COMMA)
							.append("")
							.append(NumberRender.formatByPattern(
									item.getTotalContribution(),
									ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				}

				reportDetails.append(LINE_BREAK);

			}

		}

		return reportDetails.toString();

	}

	/**
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		ContributionTransactionReportForm form = (ContributionTransactionReportForm) reportForm;
		ContributionTransactionReportData data = (ContributionTransactionReportData) report;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		// Gets layout page for contributionTransactionReport.jsp
		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.CONTRIB_TXN_DETAILS_PATH, request);

		Element rootElement = doc.createRootElement(BDPdfConstants.CONTRIB_TXN);

		// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		// Sets Roth Message.
		setRothMessageElement(doc, rootElement, request);

		// Sets Summary Info.
		setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean);

		String bodyHeader1 = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY1_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc,
				bodyHeader1);

		String bodyHeader2 = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY2_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc,
				bodyHeader2);

		// Sets Money Types details.
		setMoneyTypesDetailsXMLElements(doc, rootElement, data);

		// Gets number of rows present in report page.
		int noOfRows = getNumberOfRowsInReport(report);

		if (noOfRows > 0) {
			// Main Report - start
			Element txnDetailsElement = doc
					.createElement(BDPdfConstants.TXN_DETAILS);
			Element txnDetailElement;
			Iterator iterator = report.getDetails().iterator();
			// Gets number of rows to be shown in PDF.
			maxRowsinPDF = form.getCappedRowsInPDF();
			for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
				txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
				ContributionTransactionItem theItem = (ContributionTransactionItem) iterator
						.next();
				ParticipantVO participant = theItem.getParticipant();
				// Sets main report.
				setReportDetailsXMLElements(doc, txnDetailElement, theItem,
						participant);
				doc.appendElement(txnDetailsElement, txnDetailElement);
				rowCount++;
			}
			doc.appendElement(rootElement, txnDetailsElement);
			// Main Report - end
		}

		if (form.getPdfCapped()) {
			// Sets PDF Capped message.
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		// Sets footer, footnotes and disclaimer
		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		return doc.getDocument();
	}

	/**
	 * This method sets summary information XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param data
	 * @param layoutPageBean
	 */
	private void setSummaryInfoXMLElements(PDFDocument doc,
			Element rootElement, ContributionTransactionReportData data,
			LayoutPage layoutPageBean) {

		Element summaryInfoElement = doc
				.createElement(BDPdfConstants.SUMMARY_INFO);

		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement,
				doc, subHeader);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_TYPE,
				MoneySourceDescription.CONTRIBUTION);

		String formattedDate = DateRender.formatByPattern(
				data.getTransactionDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.INVESTED_DATE,
				formattedDate);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER,
				data.getTransactionNumber());

		formattedDate = DateRender.formatByPattern(data.getPayrollEndingDate(),
				null, RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.PAYROLL_END_DATE,
				formattedDate);

		if (data.getNumberOfParticipants() > 0) {
			String noOfParticipants = NumberRender.formatByType(
					data.getNumberOfParticipants(), null,
					RenderConstants.INTEGER_TYPE);
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PPT,
					noOfParticipants);
		}
		if (data.isHasEmployeeContribution()) {
			String contribEEAmt = NumberRender.formatByType(
					data.getTotalEmployeeContribution(), null,
					RenderConstants.CURRENCY_TYPE);
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.CONTRIB_EE_AMT,
					removeParanthesesAndPrefixMinus(contribEEAmt));
		}
		if (data.isHasEmployerContribution()) {
			String contribERAmt = NumberRender.formatByType(
					data.getTotalEmployerContribution(), null,
					RenderConstants.CURRENCY_TYPE);
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.CONTRIB_ER_AMT,
					removeParanthesesAndPrefixMinus(contribERAmt));
		}
		if (data.isHasEmployeeContribution()
				&& data.isHasEmployerContribution()) {
			String totalAmt = NumberRender.formatByType(
					data.getTotalContribution(), null,
					RenderConstants.CURRENCY_TYPE);
			doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_AMT,
					removeParanthesesAndPrefixMinus(totalAmt));
		}
		doc.appendElement(rootElement, summaryInfoElement);

	}

	/**
	 * This method sets money types details XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param data
	 */
	private void setMoneyTypesDetailsXMLElements(PDFDocument doc,
			Element rootElement, ContributionTransactionReportData data) {
		if (data.getMoneyTypes() != null) {
			int moneyTypesCount = data.getMoneyTypes().size();
			// Money Types - start
			if (moneyTypesCount > 0) {
				Element moneyTypes = doc
						.createElement(BDPdfConstants.MONEY_TYPES);
				Object[] types = data.getMoneyTypes().toArray();
				int halfOfMoneyTypesCount = types.length / 2;
				for (int i = 0; i < halfOfMoneyTypesCount; i++) {

					Element moneyType = doc
							.createElement(BDPdfConstants.MONEY_TYPE);
					Element moneyTypeLeft = doc
							.createElement(BDPdfConstants.MONEY_TYPE_LEFT);
					Element moneyTypeRight = doc
							.createElement(BDPdfConstants.MONEY_TYPE_RIGHT);
					MoneyTypeAmount theItemLeft = (MoneyTypeAmount) types[i];
					MoneyTypeAmount theItemRight = (MoneyTypeAmount) types[i
							+ halfOfMoneyTypesCount];
					doc.appendTextNode(moneyTypeLeft,
							BDPdfConstants.MONEY_TYPE_DESC,
							theItemLeft.getLongDescription());
					String moneyAmt = null;
					if (theItemLeft.getAmount() != null) {
						moneyAmt = NumberRender.formatByType(
								theItemLeft.getAmount(), null,
								RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
						doc.appendTextNode(moneyTypeLeft,
								BDPdfConstants.MONEY_AMT,
								removeParanthesesAndPrefixMinus(moneyAmt));
					}

					doc.appendTextNode(moneyTypeRight,
							BDPdfConstants.MONEY_TYPE_DESC,
							theItemRight.getLongDescription());

					if (theItemRight.getAmount() != null) {
						moneyAmt = NumberRender.formatByType(
								theItemRight.getAmount(), null,
								RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
						doc.appendTextNode(moneyTypeRight,
								BDPdfConstants.MONEY_AMT,
								removeParanthesesAndPrefixMinus(moneyAmt));
					}

					doc.appendElement(moneyType, moneyTypeLeft);
					doc.appendElement(moneyType, moneyTypeRight);
					doc.appendElement(moneyTypes, moneyType);

				}

				doc.appendElement(rootElement, moneyTypes);
			}
			// Money Types - end
		}
	}

	/**
	 * This method sets report details XML elements
	 * 
	 * @param doc
	 * @param txnDetailElement
	 * @param theItem
	 * @param participant
	 */
	private void setReportDetailsXMLElements(PDFDocument doc,
			Element txnDetailElement, ContributionTransactionItem theItem,
			ParticipantVO participant) {
		doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_NAME,
				participant.getWholeName());

		String ssnString = SSNRender.format(participant.getSsn(),
				CommonConstants.SSN_DEFAULT_VALUE);
		doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_SSN, ssnString);

		String contribEEAmt = NumberRender.formatByType(
				theItem.getEmployeeContribution(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(txnDetailElement, BDPdfConstants.CONTRIB_EE_AMT,
				removeParanthesesAndPrefixMinus(contribEEAmt));

		String contribERAmt = NumberRender.formatByType(
				theItem.getEmployerContribution(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(txnDetailElement, BDPdfConstants.CONTRIB_ER_AMT,
				removeParanthesesAndPrefixMinus(contribERAmt));

		String totalAmt = NumberRender.formatByType(
				theItem.getTotalContribution(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(txnDetailElement, BDPdfConstants.TOTAL_AMT,
				removeParanthesesAndPrefixMinus(totalAmt));
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
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
		Date transactionDate = (Date) request.getAttribute("transactionDate");
		String formattedTransactionDate = DateRender.formatByPattern(
				transactionDate, null, RenderConstants.MEDIUM_YMD_DASHED,
				BDConstants.DATE_FORMAT_MMDDYYYY);
		String csvFileName = TRANSACTION_DETAILS_CONTRIBUTION_REPORT
				+ BDConstants.HYPHON_SYMBOL
				+ currentContract.getContractNumber()
				+ BDConstants.HYPHON_SYMBOL + formattedTransactionDate
				+ CSV_EXTENSION;
		return csvFileName;
	}

	private static void buildDetailMoneyTypeIndex(
			Map<String, Integer> orderMap,
			Collection<MoneyTypeAmount> moneyTypes) {

		MoneyTypeAmount[] mtArray = moneyTypes.toArray(new MoneyTypeAmount[0]);
		Arrays.sort(mtArray, new DetailMoneyTypeComparator());
		for (int i = 0; i < mtArray.length; i++) {
			orderMap.put(mtArray[i].getId(), i);
		}

	}

	private static class DetailMoneyTypeComparator implements
			Comparator<MoneyTypeAmount> {

		public int compare(MoneyTypeAmount mt1, MoneyTypeAmount mt2) {

			int comparison = 0;

			if (mt1.isEmployeeContribution()) {
				if (mt2.isEmployerContribution()) {
					comparison = -1;
				} else {
					comparison = compareSameCategory(DETAIL_EE_ID_ORDER_MAP,
							mt1, mt2);
				}
			} else {
				if (mt2.isEmployeeContribution()) {
					comparison = 1;
				} else {
					comparison = compareSameCategory(DETAIL_ER_ID_ORDER_MAP,
							mt1, mt2);
				}
			}

			return comparison;

		}

		private int compareSameCategory(Map<String, Integer> catOrderMap,
				MoneyTypeAmount mt1, MoneyTypeAmount mt2) {

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
				comparison = mt1.getShortDescription().compareTo(
						mt2.getShortDescription());
			}

			if (comparison == 0) {
				comparison = mt1.getLongDescription().compareTo(
						mt2.getLongDescription());
			}

			return comparison;

		}
	}

	@RequestMapping(value = "/transaction/contributionTransactionReport/", method = { RequestMethod.POST,RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
			
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/contributionTransactionReport/", params = "task=filter", method = {RequestMethod.POST, RequestMethod.GET })
	public String doFilter(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/contributionTransactionReport/", params ="task=page", method = RequestMethod.GET)
	public String doPage(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/contributionTransactionReport/", params ="task=sort", method = RequestMethod.GET)
	public String doSort(
			@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/contributionTransactionReport/", params="task=download", method = RequestMethod.GET)
	public String doDownload(@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	
	@RequestMapping(value ="/transaction/contributionTransactionReport/", params ="task=downloadAll" , method = RequestMethod.GET)
	public String doDownloadAll(@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
	
	@RequestMapping(value ="/transaction/contributionTransactionReport/", params ="task=printPDF" , method =RequestMethod.GET)
	public String doPrintPDF(@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
	
	@RequestMapping(value ="/transaction/contributionTransactionReport/", params ="task=print" , method =RequestMethod.GET)
	public String doPrint(@Valid @ModelAttribute("contributionTransactionReportForm") ContributionTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				super.doCommon((BaseReportForm) form, request, null);
			} catch (SystemException e) {

				e.printStackTrace();
			}

			HttpSession session = request.getSession();
			if (session != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
	

	/**
	 /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
}
