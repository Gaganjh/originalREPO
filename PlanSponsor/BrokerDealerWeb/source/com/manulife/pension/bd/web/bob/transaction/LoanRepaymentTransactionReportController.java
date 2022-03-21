package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the loan repayment transaction page.
 * 
 * @author harlomte
 * 
 */
@Controller
@RequestMapping(value = "/bob")
@SessionAttributes({ "loanRepaymentTransactionReportForm" })

public class LoanRepaymentTransactionReportController extends AbstractTransactionReportController {

	@ModelAttribute("loanRepaymentTransactionReportForm")
	public LoanRepaymentTransactionReportForm populateForm() {
		return new LoanRepaymentTransactionReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("default","/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("sort","/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("filter","/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("page","/transaction/loanRepaymentTransactionReport.jsp");
		forwards.put("print","/transaction/loanRepaymentTransactionReport.jsp");
	}

	private static final String DEFAULT_SORT_FIELD = LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static final String[] CSV_COLUMN_HEADINGS = new String[] { "Name", "SSN", "Loan Number",
			"Repayment Amount ($)", "Principal ($)", "Interest ($)" };

	private static final String CSV_CONTRACT = "Contract";

	private static final String CSV_HEADER_CONTRACT_LOAN_REPAYMENT_SUMMARY = "Contract Loan Repayment Summary";

	private static final String CSV_HEADER_NUMBER_OF_PARTICIPANTS = "Number of Participants";

	private static final String CSV_HEADER_TRANSACTION_DATE = "Transaction Date";

	private static final String CSV_HEADER_TRANSACTION_NUMBER = "Transaction Number";

	private static final String CSV_HEADER_TOTAL_LOAN_REPAYMENT = "Loan Repayment";

	private static final String CSV_HEADER_TOTAL_LOAN_REPAYMENT_AMOUNT = "Total Repayment Amount";

	private static final String CSV_HEADER_TOTAL_LOAN_PRINCIPAL_AMOUNT = "Total Principal";

	private static final String CSV_HEADER_TOTAL_LOAN_INTEREST_AMOUNT = "Total Interest";

	private static final String TXN_DETAILS_LOAN_REPAYMENT = "TxnDetailsLoanRepayment";

	private static final String XSLT_FILE_KEY_NAME = "TxnDetailsLoanRepaymentReport.XSLFile";

	/**
	 * Constructor.
	 */
	public LoanRepaymentTransactionReportController() {
		super(LoanRepaymentTransactionReportController.class);
	}

	/**
	 * @see BDReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		LoanRepaymentTransactionReportForm theForm = (LoanRepaymentTransactionReportForm) form;
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		String contractNumber = String.valueOf(currentContract.getContractNumber());

		criteria.addFilter(LoanRepaymentTransactionReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());

		request.setAttribute(BDConstants.TRANSACTION_DATE, theForm.getTransactionDate());

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
		if (LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT.equals(sortField)
				|| LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL.equals(sortField)
				|| LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST.equals(sortField)) {
			if (ReportSort.ASC_DIRECTION.equals(sortDirection)) {
				sortDirection = ReportSort.DESC_DIRECTION;
			} else {
				sortDirection = ReportSort.ASC_DIRECTION;
			}
		}

		/* primary sort */
		criteria.insertSort(sortField, sortDirection);

		/* logic for secondary sorts */
		/* sort on name */
		if (LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME.equals(sortField)) {
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME, sortDirection);
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN, sortDirection);
			criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER, sortDirection);
		} else {
			if (LoanRepaymentTransactionReportData.SORT_FIELD_SSN.equals(sortField)) {
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME, sortDirection);
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME, sortDirection);
				criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER, sortDirection);
				/* all other sorts */
			} else {

				if (LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT.equals(sortField)
						|| LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL.equals(sortField)
						|| LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST.equals(sortField)) {

					if (ReportSort.DESC_DIRECTION.equals(sortDirection)) {
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
								ReportSort.DESC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
								ReportSort.DESC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN,
								ReportSort.DESC_DIRECTION);
					} else {
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
								ReportSort.ASC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
								ReportSort.ASC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN,
								ReportSort.ASC_DIRECTION);
					}
				} else {
					if (ReportSort.ASC_DIRECTION.equals(sortDirection)) {
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
								ReportSort.DESC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
								ReportSort.DESC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN,
								ReportSort.DESC_DIRECTION);
					} else {
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
								ReportSort.ASC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
								ReportSort.ASC_DIRECTION);
						criteria.insertSort(LoanRepaymentTransactionReportData.SORT_FIELD_SSN,
								ReportSort.ASC_DIRECTION);
					}
				}
			}
		}
	}

	/**
	 * This method gives back the Report handler name
	 */
	protected String getReportId() {
		return LoanRepaymentTransactionReportData.REPORT_ID;
	}

	/**
	 * This method gives back the CSV file name
	 */
	protected String getReportName() {
		return LoanRepaymentTransactionReportData.REPORT_NAME;
	}

	/**
	 * The getReportData() method of BaseReportAction class is being overridden,
	 * so that the user cannot see the Resource Limit Exceeded Exception
	 * message. Instead, he will see a Technical Difficulties message.
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		ReportData reportData = null;

		try {
			reportData = super.getReportData(reportId, reportCriteria, request);
		} catch (ResourceLimitExceededException e) {
			logger.error("Received a ResourceLimitExceededException: ", e);
			throw new SystemException(e,
					"ResourceLimitExceededException occurred. Showing it as TECHNICAL_DIFFICULTIES to the user.");
		}

		return reportData;
	}

	/**
	 * This method gives the default sort field
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * This method gives the default sort direction
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
	 * This method is called when the user clicks on CSV icon. This method
	 * creates the report information in CSV format.
	 */
	@SuppressWarnings("unchecked")
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		Content message = null;

		LoanRepaymentTransactionReportData data = (LoanRepaymentTransactionReportData) report;
		StringBuffer buffer = new StringBuffer();

		try {

			message = ContentCacheManager.getInstance().getContentById(
					BDContentConstants.TRANSACTION_LOAN_REPAYMENT_REPORT_LAYOUT_PAGE,
					ContentTypeManager.instance().LAYOUT_PAGE);

			Contract currentContract = getBobContext(request).getCurrentContract();
			buffer.append(CSV_CONTRACT).append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
					.append(currentContract.getCompanyName()).append(LINE_BREAK);
			buffer.append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			LoanRepaymentTransactionReportForm form = (LoanRepaymentTransactionReportForm) reportForm;

			buffer.append(CSV_HEADER_CONTRACT_LOAN_REPAYMENT_SUMMARY).append(LINE_BREAK).append(LINE_BREAK);

			buffer.append(CSV_HEADER_NUMBER_OF_PARTICIPANTS).append(COMMA);
			buffer.append(data.getNumberOfParticipants()).append(LINE_BREAK);

			buffer.append(CSV_HEADER_TRANSACTION_DATE).append(COMMA);
			String formattedDate = DateRender.formatByPattern(form.getTransactionDate(), null,
					RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
			buffer.append(formattedDate).append(LINE_BREAK);

			buffer.append(CSV_HEADER_TRANSACTION_NUMBER).append(COMMA);
			buffer.append(form.getTransactionNumber()).append(LINE_BREAK);

			buffer.append(CSV_HEADER_TOTAL_LOAN_REPAYMENT_AMOUNT).append(COMMA);
			buffer.append(BDConstants.DOLLAR_SIGN).append(data.getTotalRepaymentAmount()).append(LINE_BREAK);
			buffer.append(CSV_HEADER_TOTAL_LOAN_PRINCIPAL_AMOUNT).append(COMMA);
			buffer.append(BDConstants.DOLLAR_SIGN).append(data.getTotalPrincipalAmount()).append(LINE_BREAK);
			buffer.append(CSV_HEADER_TOTAL_LOAN_INTEREST_AMOUNT).append(COMMA);
			buffer.append(BDConstants.DOLLAR_SIGN).append(data.getTotalInterestAmount()).append(LINE_BREAK)
					.append(LINE_BREAK);

			buffer.append(ContentUtility.getContentAttribute(message, BDContentConstants.BODY1_HEADER))
					.append(LINE_BREAK);

			for (int columnCount = 0; columnCount < CSV_COLUMN_HEADINGS.length; columnCount++) {
				buffer.append(CSV_COLUMN_HEADINGS[columnCount]);
				if (columnCount != CSV_COLUMN_HEADINGS.length - 1) {
					buffer.append(COMMA);
				}
			}

			/*
			 * Individual Line Items
			 */
			ArrayList<LoanRepaymentTransactionItem> theItems = (ArrayList<LoanRepaymentTransactionItem>) report
					.getDetails();
			StringBuffer nameBuffer = null;
			for (LoanRepaymentTransactionItem theItem : theItems) {
				buffer.append(LINE_BREAK);
				ParticipantVO participant = theItem.getParticipant();

				String ssnString = SSNRender.format(participant.getSsn(), BDConstants.DEFAULT_SSN_VALUE);

				nameBuffer = new StringBuffer();

				/*
				 * build a buffer string for the name and last name which will
				 * then be treated as a whole string for getCsvString()
				 */
				nameBuffer.append(participant.getLastName()).append(COMMA).append(" ")
						.append(participant.getFirstName());
				buffer.append(getCsvString(nameBuffer.toString())).append(COMMA);
				buffer.append(getCsvString(ssnString)).append(COMMA);

				/* converts loanNumber from short to Short for getCsvString() */
				buffer.append(getCsvString(new Short(theItem.getLoanNumber()))).append(COMMA);

				if (theItem.getRepaymentAmount() != null) {
					buffer.append(getCsvString(Double.toString(theItem.getRepaymentAmount().doubleValue() * -1)))
							.append(COMMA);
				}
				if (theItem.getPrincipalAmount() != null) {
					buffer.append(getCsvString(Double.toString(theItem.getPrincipalAmount().doubleValue() * -1)))
							.append(COMMA);
				}
				if (theItem.getInterestAmount() != null) {
					buffer.append(getCsvString(Double.toString(theItem.getInterestAmount().doubleValue() * -1)))
							.append(COMMA);
				}
			}
		} catch (ContentException e) {
			throw new SystemException(e, getClass().getName(), "populateDownloadData", "Something wrong with CMA");
		}
		return buffer.toString().getBytes();
	}

	/**
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		LoanRepaymentTransactionReportForm form = (LoanRepaymentTransactionReportForm) reportForm;
		LoanRepaymentTransactionReportData data = (LoanRepaymentTransactionReportData) report;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		// Gets layout page for loanRepaymentTransactionReport.jsp
		LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.TXN_DETAILS_LOAN_REPAYMENT_PATH, request);

		Element rootElement = doc.createRootElement(BDPdfConstants.TXN_DETAILS_LOAN_REPAYMENT);

		// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		// Sets Summary Info.
		setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form);

		String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER,
				null);
		PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

		// Gets number of rows present in report page.
		int noOfRows = getNumberOfRowsInReport(report);

		if (noOfRows > 0) {
			// Main Report - start
			Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
			Iterator iterator = report.getDetails().iterator();
			// Gets number of rows to be shown in PDF.
			maxRowsinPDF = form.getCappedRowsInPDF();
			for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
				Element txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
				LoanRepaymentTransactionItem theItem = (LoanRepaymentTransactionItem) iterator.next();
				ParticipantVO participant = theItem.getParticipant();
				// Sets main report.
				setReportDetailsXMLElements(doc, txnDetailElement, theItem, participant);
				doc.appendElement(txnDetailsElement, txnDetailElement);
				rowCount++;
			}
			doc.appendElement(rootElement, txnDetailsElement);
			// Main Report - end
		}

		if (form.getPdfCapped()) {
			// Sets PDF Capped message.
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
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
	 * @param form
	 */
	private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement,
			LoanRepaymentTransactionReportData data, LayoutPage layoutPageBean,
			LoanRepaymentTransactionReportForm form) {

		Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

		String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

		String noOfParticipants = NumberRender.formatByPattern(data.getNumberOfParticipants(), BDConstants.ZERO_STRING,
				BDConstants.INTEGER_FORMAT);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PPT, noOfParticipants);

		String formattedDate = DateRender.formatByPattern(form.getTransactionDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_DATE, formattedDate);

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER, form.getTransactionNumber());

		String totalRepaymentAmt = NumberRender.formatByType(data.getTotalRepaymentAmount(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_REPAYMENT_AMT, totalRepaymentAmt);

		String totalPrincipal = NumberRender.formatByType(data.getTotalPrincipalAmount(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_PRINCIPAL, totalPrincipal);

		String totalInterest = NumberRender.formatByType(data.getTotalInterestAmount(), null,
				RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_INTEREST, totalInterest);
		doc.appendElement(rootElement, summaryInfoElement);
	}

	/**
	 * This method sets report details XML elements
	 * 
	 * @param doc
	 * @param txnDetailElement
	 * @param theItem
	 * @param participant
	 */
	private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement,
			LoanRepaymentTransactionItem theItem, ParticipantVO participant) {
		StringBuffer pptName = new StringBuffer();
		BigDecimal value;
		pptName.append(participant.getLastName()).append(COMMA).append(WHITE_SPACE_CHAR)
				.append(participant.getFirstName());
		doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_NAME, pptName.toString());

		String ssnString = SSNRender.format(participant.getSsn(), BDConstants.DEFAULT_SSN_VALUE);
		doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_SSN, ssnString);

		doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_LOAN_NUMBER, String.valueOf(theItem.getLoanNumber()));

		if (theItem.getRepaymentAmount() != null) {
			value = theItem.getRepaymentAmount().negate();
			doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_REPAYMENT_AMT,
					NumberRender.formatByType(value, null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
		}

		if (theItem.getPrincipalAmount() != null) {
			value = theItem.getPrincipalAmount().negate();
			doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_PRINCIPAL,
					NumberRender.formatByType(value, null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
		}

		if (theItem.getInterestAmount() != null) {
			value = theItem.getInterestAmount().negate();
			doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_INTEREST,
					NumberRender.formatByType(value, null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
		}
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

	protected String getFileName(BaseReportForm form, HttpServletRequest request) {
		LoanRepaymentTransactionReportForm theForm = (LoanRepaymentTransactionReportForm) form;
		Contract currentContract = getBobContext(request).getCurrentContract();
		String formattedTransactionDate = DateRender.formatByPattern(theForm.getTransactionDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED)
				.replace(BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
		String csvFileName = TXN_DETAILS_LOAN_REPAYMENT + BDConstants.HYPHON_SYMBOL
				+ currentContract.getContractNumber() + BDConstants.HYPHON_SYMBOL + formattedTransactionDate
				+ CSV_EXTENSION;

		return csvFileName;
	}

	@RequestMapping(value = "/transaction/loanRepaymentTransactionReport/", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			try {
				Collection<GenericException> errorCollection = null;
		        List<ObjectError>  errorList =null;
		        Object obj =null; 
		        obj = request.getAttribute(BdBaseController.ERROR_KEY);
				if (obj instanceof BeanPropertyBindingResult){
			    		errorCollection=new ArrayList<GenericException>();
						errorList=((BeanPropertyBindingResult) obj).getAllErrors();
						for(ObjectError objError:errorList){
							 if (objError != null) {
								 errorCollection.add(new ValidationError(new String[0], Integer.parseInt(objError.getCode()), objError.getArguments(),
				                            ValidationError.Type.error));
			               }
							
						}
						request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
						request.removeAttribute(BdBaseController.ERROR_KEY);
						request.getSession().setAttribute(BdBaseController.ERROR_KEY, errorCollection);
			 			request.setAttribute(BdBaseController.ERROR_KEY, errorCollection);
				}
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/loanRepaymentTransactionReport/", params = "task=filter", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String doFilter(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/loanRepaymentTransactionReport/", params = "task=page", method = RequestMethod.GET)
	public String doPage(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/transaction/loanRepaymentTransactionReport/", params = "task=sort", method = RequestMethod.GET)
	public String doSort(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	  @RequestMapping(value = "/transaction/loanRepaymentTransactionReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
			public String doPrintPDF(@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
					BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {
				 String forward = preExecute(form, request, response);
			    	if(StringUtils.isNotBlank(forward)) {
			    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
			    	}
				if (bindingResult.hasErrors()) {
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																												// default
					}
				}
				 forward = super.doPrintPDF(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}
	@RequestMapping(value = "/transaction/loanRepaymentTransactionReport/", params = "task=download", method = RequestMethod.GET)
	public String doDownload(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/loanRepaymentTransactionReport/", params = "task=downloadAll", method = RequestMethod.GET)
	public String doDownloadAll(
			@Valid @ModelAttribute("loanRepaymentTransactionReportForm") LoanRepaymentTransactionReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	/**
	 * This code has been changed and added to / Validate form and request
	 * against penetration attack, prior to other validations as part of the
	 * CL#136970.
	 */

	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
}
