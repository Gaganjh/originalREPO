package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.bd.web.content.BDContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED;
import static com.manulife.pension.platform.web.CommonConstants.REPORT_BEAN;
import static com.manulife.pension.platform.web.CommonConstants.TRANSACTION_TYPES;
import static com.manulife.pension.platform.web.CommonConstants.TXN_HISTORY_FROM_DATES;
import static com.manulife.pension.platform.web.CommonConstants.TXN_HISTORY_TO_DATES;

import java.io.IOException;
import java.util.ArrayList;
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

import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionType;
import com.manulife.pension.ps.service.report.transaction.handler.TransactionTypeDescription;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class handles the transaction history page.
 * 
 * @author Siby Thomas
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"transactionHistoryReportForm"})

public class TransactionHistoryReportController extends AbstractTransactionReportController {
	@ModelAttribute("transactionHistoryReportForm") 
	public TransactionHistoryReportForm populateForm() 
	{
		return new TransactionHistoryReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ forwards.put("input","/transaction/transactionHistoryReport.jsp");
	forwards.put("default","/transaction/transactionHistoryReport.jsp");
	forwards.put("sort","/transaction/transactionHistoryReport.jsp");
	forwards.put("filter","/transaction/transactionHistoryReport.jsp");
	forwards.put("page","/transaction/transactionHistoryReport.jsp");
	}
    
	private static Logger logger = Logger.getLogger(TransactionHistoryReportController.class);
	
	private static final String DEFAULT_SORT_FIELD = TransactionHistoryReportData.SORT_FIELD_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String CSV_HEADER_FROM_DATE = "From date";
	private static final String CSV_HEADER_TO_DATE = "To date";
	private static final String CSV_HEADER_TRANSACTION_TYPE = "Type";
	
	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[]{
			"Transaction Date", "Type Line1", "Type Line2",
			"Description Line 1", "Description Line 2", "Amount ($)",
			"Transaction Number"};
	
	private static final String XSLT_FILE_KEY_NAME = "ContractTransactionHistoryReport.XSLFile";
	private static final String CONTRACT = "Contract";

	private static final String ALL_TYPES = "ALL";
    private static final List<LabelValueBean> TYPES_DROPDOWN = new ArrayList<LabelValueBean>();
    private static final List<LabelValueBean> NO_LOANS_TYPES_DROPDOWN = new ArrayList<LabelValueBean>();

	static {
		TYPES_DROPDOWN.add(new LabelValueBean("All", ALL_TYPES));
		NO_LOANS_TYPES_DROPDOWN.add(new LabelValueBean("All", ALL_TYPES));

		String[] types = new String[]{TransactionType.ADJUSTMENT,
				TransactionType.ALLOCATION,
				TransactionType.INTER_ACCOUNT_TRANSFER,
				TransactionType.LOAN_DEFAULT,
				TransactionType.LOAN_ISSUE, 
				TransactionType.LOAN_REPAYMENT,
				TransactionType.LOAN_TRANSFER,
				TransactionType.MATURITY_REINVESTMENT,
				TransactionType.WITHDRAWAL};
		for (int i = 0; i < types.length; i++) {
			LabelValueBean lvbean = new LabelValueBean(
					TransactionTypeDescription.getDescription(types[i]),
					types[i]);
			TYPES_DROPDOWN.add(lvbean);
			if (types[i] != TransactionType.LOAN_ISSUE
					&& types[i] != TransactionType.LOAN_REPAYMENT
					&& types[i] != TransactionType.LOAN_TRANSFER
					&& types[i] != TransactionType.LOAN_DEFAULT) {
				NO_LOANS_TYPES_DROPDOWN.add(lvbean);
			}
		}
	}

	/**
	 * Constructor.
	 */
	public TransactionHistoryReportController() {
		super(TransactionHistoryReportController.class);
	}

    /**
     * Validate the input action form. FROM date must be less than TO date.
     * 
     * @see BaseController#doValidate()
     */
	@Autowired
	private TransactionHistoryReportValidator transactionHistoryReportValidator;
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	    binder.addValidators(transactionHistoryReportValidator);
	}
	

    /**
     * @see BaseReportController#populateReportCriteria(String, HttpServletRequest)
     */ 
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		TransactionHistoryReportForm theForm = (TransactionHistoryReportForm) form;

		BobContext bob = getBobContext(request);

		Contract currentContract = bob.getCurrentContract();

		criteria.addFilter(TransactionHistoryReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));
		
		criteria.addFilter(TransactionHistoryReportData.FILTER_PROPOSAL_NUMBER,
				currentContract.getProposalNumber());

		criteria.addFilter(TransactionHistoryReportData.FILTER_FROM_DATE,
				new Date(Long.valueOf(theForm.getFromDate()).longValue()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_TO_DATE,
				new Date(Long.valueOf(theForm.getToDate()).longValue()));

		criteria.addFilter(TransactionHistoryReportData.FILTER_TYPE_LIST,
				getTransactionTypeCode(theForm.getTransactionType()));
		
		if (currentContract.isDefinedBenefitContract()) {
			criteria.addFilter(TransactionHistoryReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}
	}

    /**
     * If it's sort by Date, secondary sort is transaction number. If it's sort by amount, secondary
     * sort is date and number. If it's sort by number, secondary sort is date.
     * 
     * @see BaseReportController#populateSortCriteria()
     * 
     */
	@Override
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (TransactionHistoryReportData.SORT_FIELD_DATE.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (TransactionHistoryReportData.SORT_FIELD_AMOUNT.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_NUMBER,
					sortDirection);
		} else if (TransactionHistoryReportData.SORT_FIELD_NUMBER.equals(sortField)) {
			criteria.insertSort(TransactionHistoryReportData.SORT_FIELD_DATE,
					sortDirection);
		}
	}

    /**
     * This method populates a default form when the report page is first brought up. This method is
     * called before populateReportCriteria() to allow default sort and other criteria to be set
     * properly.
     * 
     * @see BaseReportController#populateReportForm()
     */
	@Override
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		/*
		 * Obtain the contract dates object.
		 */
		List<Date> fromDates = new ArrayList<Date>();
        List<Date> toDates = new ArrayList<Date>();

        /*
         * Using the contract dates, generate the date range (from dates and to dates).
         */
        ContractDateHelper.populateFromToDates(getBobContext(request).getCurrentContract(),
                fromDates, toDates);
        
		/*
		 * Stores the from dates, to dates and the transaction types labels into
		 * the request.
		 */
		request.setAttribute(TXN_HISTORY_FROM_DATES, fromDates);
        request.setAttribute(TXN_HISTORY_TO_DATES, toDates);
        request.setAttribute(TRANSACTION_TYPES,
				NO_LOANS_TYPES_DROPDOWN);

		TransactionHistoryReportForm theForm = (TransactionHistoryReportForm) reportForm;

		if (theForm.getFromDate() == null
				|| theForm.getFromDate().length() == 0) {
			theForm.setFromDate(String.valueOf((fromDates.iterator()
					.next()).getTime()));
		}

		if (theForm.getToDate() == null || theForm.getToDate().length() == 0) {
			theForm.setToDate(String.valueOf(((Date) toDates.iterator().next())
					.getTime()));
		}

		if (theForm.getTransactionType() == null
				|| theForm.getTransactionType().length() == 0) {
			// defaults to all
			theForm.setTransactionType(ALL_TYPES);
		}
		
	}

	/**
     * @see BaseReportController#getReportId()
     */
    @Override
	protected String getReportId() {
		return TransactionHistoryReportData.REPORT_ID;
	}

	/**
     * @see BaseReportController#getReportName()
     */
    @Override
	protected String getReportName() {
		return TransactionHistoryReportData.REPORT_NAME;
	}

	/**
     * @see BaseReportController#getDefaultSort()
     */
    @Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
     * @see BaseReportController#getDefaultSortDirection()
     */
    @Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	/**
     * @see BaseReportController#getDownloadData()
     */
	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
	    
		if (logger.isDebugEnabled()) 
			logger.debug("entry -> populateDownloadData");
		
		TransactionHistoryReportData data = (TransactionHistoryReportData) report;
		StringBuffer buffer = new StringBuffer();
		TransactionHistoryReportForm form = (TransactionHistoryReportForm) reportForm;

        Contract currentContract = getBobContext(request).getCurrentContract();
        buffer.append(CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		try {
			/*
			 * Obtain the from date and to date.
			 */
			Date fromDate = (Date) report.getReportCriteria().getFilterValue(
					TransactionHistoryReportData.FILTER_FROM_DATE);
			Date toDate = (Date) report.getReportCriteria().getFilterValue(
					TransactionHistoryReportData.FILTER_TO_DATE);

			String fromDateStr = DateRender.format(fromDate,
					RenderConstants.EXTRA_LONG_MDY);
			String toDateStr = DateRender.format(toDate,
					RenderConstants.EXTRA_LONG_MDY);

			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
			buffer.append(fromDateStr).append(COMMA);
			buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
			buffer.append(toDateStr).append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			
			String transactionType = getTransactionTypeDescription(form
					.getTransactionType());

			buffer.append(CSV_HEADER_TRANSACTION_TYPE).append(COMMA);
			buffer.append(transactionType).append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			if (data.getDetails().size() == 0) {
				Content message = null;
				message = ContentCacheManager
						.getInstance()
						.getContentById(
								MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
								ContentTypeManager.instance().MESSAGE);

				buffer.append(
						ContentUtility.getContentAttribute(message, "text"))
						.append(LINE_BREAK);

			} else {

				for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
					buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
					if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1) {
						buffer.append(COMMA);
					}
				}
				buffer.append(LINE_BREAK);

				/*
				 * Individual Line Items
				 */
				Iterator<TransactionHistoryItem> iterator = report.getDetails().iterator();
				while (iterator.hasNext()) {
					TransactionHistoryItem theItem = iterator.next();
					String transactionDate = DateRender.format(theItem
							.getTransactionDate(), null);
					buffer.append(transactionDate).append(COMMA);
					buffer.append(getCsvString(theItem.getTypeDescription1()))
							.append(COMMA);
					buffer.append(getCsvString(theItem.getTypeDescription2()))
							.append(COMMA);

					Iterator it = theItem.getDescriptions().iterator();
					for (int i = 0; i < 2; i++) {
						if (it.hasNext()) {
							buffer.append(getCsvString(it.next()));
						}
						buffer.append(COMMA);
					}

					buffer.append(getCsvString(theItem.getAmount())).append(
							COMMA);
					buffer.append(getCsvString(theItem.getTransactionNumber()));

					buffer.append(LINE_BREAK);
				}
			}
		} catch (ContentException e) {
			throw new SystemException(e, "Something wrong with CMA");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		
		return buffer.toString().getBytes();
	}

	/**
	 * Gets the transaction type description.
	 * 
	 * @return Returns the transaction type description.
	 */
	protected String getTransactionTypeDescription(String code) {
		/*
		 * construct an array of keys if user selected "All" option Array starts
		 * with index 1 i.e. minus key for ALL
		 */
		for (Iterator<LabelValueBean> it = TYPES_DROPDOWN.iterator(); it.hasNext();) {
			LabelValueBean bean = it.next();
			if (bean.getValue().equals(code)) {
				return bean.getLabel();
			}
		}
		throw new IllegalArgumentException("Invalid code [" + code + "]");
	}

	/**
	 * Gets the transactionTypes
	 * 
	 * @return Returns a Collection
	 */
	protected Collection<String> getTransactionTypeCode(String type) {
		/*
		 * construct an array of keys if user selected "All" option Array starts
		 * with index 1 i.e. minus key for ALL
		 */
		List<String> types = new ArrayList<String>();
		if (type.equalsIgnoreCase(ALL_TYPES)) {
			for (Iterator<LabelValueBean> it = TYPES_DROPDOWN.iterator(); it.hasNext();) {
                LabelValueBean bean = it.next();
				if (bean.getValue().equals(ALL_TYPES)) {
					continue;
				}
				types.add(bean.getValue());
			}
		} else {
			types.add(type);
		}
		return types;
	}

    /**
     * @see BaseReportController#doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse)
     */
	@Override
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		String forward = super.doCommon( reportForm, request,
				response);
        /*
         * determine if the results have any loan items in them if not, remove the loan-related
         * types from the types drop down
         */
        TransactionHistoryReportData report = (TransactionHistoryReportData) request
				.getAttribute(REPORT_BEAN);
		if (report != null && (report.hasLoans()
				|| getBobContext(request).getCurrentContract()
                        .isLoanFeature())) {
			request.setAttribute(TRANSACTION_TYPES, TYPES_DROPDOWN);
		} else {
			request.setAttribute(TRANSACTION_TYPES,
					NO_LOANS_TYPES_DROPDOWN);
		}
		return forward;
	}
	@RequestMapping(value ="/transaction/transactionHistoryReport/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null? forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		 forward=super.doDefault( form, request, response);
		return StringUtils.contains(forward,'/')? forwards.get(forward):forwards.get(forward); 
	}
	@RequestMapping(value = "/transaction/transactionHistoryReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null? forwards.get(errDirect):forwards.get("input");
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/transactionHistoryReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession()
					.getAttribute(CommonEnvironment.getInstance().getErrorKey());
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return 	forwards.get("input");
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/transactionHistoryReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession()
					.getAttribute(CommonEnvironment.getInstance().getErrorKey());
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return	forwards.get("input");
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/transactionHistoryReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession()
					.getAttribute(CommonEnvironment.getInstance().getErrorKey());
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return	forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/transaction/transactionHistoryReport/",params = {"task=downloadAll"}, method = {RequestMethod.GET})
	public String doDownloadAll(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession()
					.getAttribute(CommonEnvironment.getInstance().getErrorKey());
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return	forwards.get("input");
			}
		}
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}	
	@RequestMapping(value = "/transaction/transactionHistoryReport/",params = {"task=printPDF"}, method = {RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("transactionHistoryReportForm") TransactionHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			form.setTransactionType(null);
			populateReportForm( form, request);
			request.setAttribute("displayDates", "true");
			String errDirect = (String) request.getSession()
					.getAttribute(CommonEnvironment.getInstance().getErrorKey());
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return	forwards.get("input");
			}
		}
		 forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}	
	/**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request)
           throws ParserConfigurationException {
        
        TransactionHistoryReportForm form = (TransactionHistoryReportForm) reportForm;
        TransactionHistoryReportData data = (TransactionHistoryReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();

        // Gets layout page for transactionHistoryReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.CONTRACT_TXN_HISTORY_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.CONTRACT_TXN_HISTORY);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets Roth Message.
        setRothMessageElement(doc, rootElement, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        doc.appendTextNode(rootElement, BDPdfConstants.FROM_DATE, 
                DateRender.formatByPattern(new Date(Long.parseLong(form.getFromDate())), null, RenderConstants.MEDIUM_MDY_SLASHED));

        doc.appendTextNode(rootElement, BDPdfConstants.TO_DATE, 
                DateRender.formatByPattern(new Date(Long.parseLong(form.getToDate())), null, RenderConstants.MEDIUM_MDY_SLASHED));

        // Sets Txn Type selected
        String transactionType = getTransactionTypeDescription(form.getTransactionType());
        doc.appendTextNode(rootElement, BDPdfConstants.TXN_TYPE, transactionType);
        
        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Transaction Details - start
            Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
            Element txnDetailElement;
            Iterator iterator = data.getDetails().iterator();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {   
                txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
                TransactionHistoryItem theItem = (TransactionHistoryItem) iterator.next();
                // Sets main report.
                setReportDetailsXMLElements(doc, txnDetailElement, theItem);
                doc.appendElement(txnDetailsElement, txnDetailElement);
                rowCount++;
            }
            doc.appendElement(rootElement, txnDetailsElement);
            // Transaction Details - end
        } else {
        	// Sets Info. message.
            int msgNum = 0;
            Element infoMessagesElement = doc.createElement(BDPdfConstants.INFO_MESSAGES);
            Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            String	message = ContentHelper.getContentText(BDContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED, 
            			ContentTypeManager.instance().MESSAGE, null);
            doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String.valueOf(++msgNum));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
            doc.appendElement(rootElement, infoMessagesElement);

        }
        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
 
        return doc.getDocument();

    }

    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param txnDetailElement
     * @param theItem
     */
    @SuppressWarnings("unchecked")
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement,
            TransactionHistoryItem theItem) {
        if (theItem != null) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DATE, DateRender
                    .formatByPattern(theItem.getTransactionDate(), null,
                            RenderConstants.MEDIUM_MDY_SLASHED));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_TYPE_DESCRIPTION1, theItem
                    .getTypeDescription1());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_TYPE_DESCRIPTION2, theItem
                    .getTypeDescription2());

            for (String description : (ArrayList<String>) theItem.getDescriptions()) {
                doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_DESCRIPTION, description);
            }

            if (theItem.getType().equals(TransactionType.ADJUSTMENT)
                    && theItem.getAmount().doubleValue() == 0) {
                doc.appendTextNode(txnDetailElement, BDPdfConstants.AMT_NA, null);
            } else {
                String amount = NumberRender.formatByType(theItem.getAmount(), null,
                        RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_AMT, removeParanthesesAndPrefixMinus(amount));
            }
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TXN_NUMBER, theItem
                    .getTransactionNumber());
        }
    }

    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
}
