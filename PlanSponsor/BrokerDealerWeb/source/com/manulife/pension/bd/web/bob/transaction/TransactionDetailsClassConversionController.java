package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.ClassConversionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsClassConversionReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * Action class for class conversion report page
 * 
 * @author Siby Thomas
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"transactionDetailsClassConversionForm"})

public class TransactionDetailsClassConversionController extends BOBReportController {
	@ModelAttribute("transactionDetailsClassConversionForm") 
	public TransactionDetailsClassConversionForm populateForm() 
	{
		return new TransactionDetailsClassConversionForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/classConversionTransactionReport.jsp");
		forwards.put("default","/transaction/classConversionTransactionReport.jsp");
		forwards.put("sort","/transaction/classConversionTransactionReport.jsp");
		forwards.put("page","/transaction/classConversionTransactionReport.jsp");
		forwards.put("filter","/transaction/classConversionTransactionReport.jsp");
		}
    
    private final String CSV_FILE_NAME = "ClassConversionDetails";
    private static final String XSLT_FILE_KEY_NAME = "TxnDetailsClassConversion.XSLFile";
        
    /**
     * @see BaseReportController#getDefaultSort()
     */
    protected String getDefaultSort() {
        return TransactionDetailsClassConversionReportData.SORT_FIELD_WEBSRTNO;
    }

     /**
     * @see BaseReportController#getDefaultSortDirection()
     */
    protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;
    }

    /**
     * @see BaseReportController#populateReportCriteria()
     */
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }

        TransactionDetailsClassConversionForm form = (TransactionDetailsClassConversionForm) actionForm;

        String transactionNumber = (String) request.getParameter("transactionNumber");
        if (transactionNumber == null || transactionNumber.equals("")) {
            transactionNumber = form.getTransactionNumber();
        }

        BobContext bobContext = getBobContext(request);
        Contract currentContract = bobContext.getCurrentContract();

        String contractNumber = String.valueOf(currentContract.getContractNumber());
        if (contractNumber == null || contractNumber.equals("") || contractNumber.equals("0")) {
            contractNumber = form.getContractNumber();
        }

        String participantId = (String) request.getParameter("pptId"); // new reference form.
        if (participantId == null || participantId.equals("")) {
            participantId = (String) request.getParameter("participantId"); // do legacy check
                                                                            // incase a link was
                                                                            // missed
            if (participantId == null || participantId.equals("")) {
                participantId = form.getPptId();
            }
        }

        if (currentContract.isDefinedBenefitContract()) {
            criteria.addFilter(TransactionDetailsClassConversionReportData.CONTRACT_TYPE_DB,
                    Boolean.TRUE);
        }

        criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_TRANSACTION_NUMBER,
                transactionNumber);
        criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_PARTICIPANT_ID,
                participantId);
        criteria.addFilter(TransactionDetailsClassConversionReportData.FILTER_CONTRACT_NUMBER,
                contractNumber);
        

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportCriteria");
        }
    }

    /**
     * The method populates the sort criteria
     * 
     * @param criteria
     * @param actionForm
     * @param request
     * @throws SystemException
     */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm actionForm,
            HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateSortCriteria");
        }

        // default sort is risk category
        TransactionDetailsFTFForm form = (TransactionDetailsFTFForm) actionForm;
        String sortField = form.getSortField();
        String sortDirection = form.getSortDirection();

        criteria.insertSort(sortField, sortDirection);

        // add additional sort criteria websrtno and money type description
        criteria.insertSort(TransactionDetailsClassConversionReportData.SORT_FIELD_WEBSRTNO,
                ReportSort.ASC_DIRECTION);
        criteria.insertSort(
                TransactionDetailsClassConversionReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
                ReportSort.ASC_DIRECTION);

        if (logger.isDebugEnabled()) {
            logger.debug("populateSortCriteria: inserting sort with field:" + sortField
                    + " and direction: " + sortDirection);
        }
    }

    public TransactionDetailsClassConversionController() {
        super(TransactionDetailsClassConversionController.class);
    }

    /**
     * @see BaseReportController#getReportId()
     */
    protected String getReportId() {
        return TransactionDetailsClassConversionReportData.REPORT_ID;
    }

    /**
     * @see BaseReportController#getReportName()
     */
    protected String getReportName() {
        return CSV_FILE_NAME;
    }

    /**
     * @see BaseReportController#doCommon()
     */
    public String doCommon( TransactionDetailsClassConversionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon()");
        }

        String forward = null;
        forward = super.doCommon( actionForm, request, response);
        TransactionDetailsClassConversionReportData report = (TransactionDetailsClassConversionReportData) 
            request.getAttribute(BDConstants.REPORT_BEAN);
       

        if (report.getDetails() != null && report.getDetails().size() != 0) {
        	actionForm.setReport(report);
        	actionForm.setTransactionNumber(report.getTransactionNumber());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doCommon()");
        }

        return forward;
    }

    /**
     * The methods returns the csv.
     * 
     * @return byte[]
     */
    @SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm,
            ReportData report,
            HttpServletRequest request) throws SystemException {

        TransactionDetailsClassConversionReportData theReport = (TransactionDetailsClassConversionReportData) report;

        if (logger.isDebugEnabled())
            logger.debug("entry -> getDownloadData");
        // get the content objects
        StringBuffer buffer = new StringBuffer();

        // Title
        buffer.append("Transaction details").append(LINE_BREAK + LINE_BREAK);
        buffer.append("Class conversion details").append(LINE_BREAK + LINE_BREAK);

        Contract currentContract = getBobContext(request).getCurrentContract();
        buffer.append("Contract number:").append(COMMA).append(currentContract.getContractNumber());
        buffer.append(LINE_BREAK);
        buffer.append("Company name:").append(COMMA).append(currentContract.getCompanyName());
        buffer.append(LINE_BREAK);
        buffer.append(LINE_BREAK);

        buffer.append("Class conversion:").append(LINE_BREAK);
        buffer.append("Transaction type:").append(COMMA).append("Class conversion").append(
                LINE_BREAK);

        if (currentContract.isDefinedBenefitContract() == false) {
            buffer.append("Name:").append(COMMA).append(theReport.getParticipantName()).append(
                    LINE_BREAK);
            buffer.append("SSN:").append(COMMA).append(theReport.getParticipantSSN()).append(
                    LINE_BREAK);
        }

        buffer.append("Invested date:").append(COMMA).append(
                DateRender.format(theReport.getTransactionDate(),
                        RenderConstants.MEDIUM_YMD_SLASHED)).append(LINE_BREAK);
        buffer.append("Request date:").append(COMMA).append(
                DateRender.format(theReport.getRequestDate(), RenderConstants.MEDIUM_YMD_SLASHED))
                .append(LINE_BREAK);
        buffer.append("Total amount transferred out:").append(COMMA).append(
                theReport.getTotalAmount()).append(LINE_BREAK);
        buffer.append("Total amount transferred in:").append(COMMA).append(
                theReport.getTotalToAmount()).append(LINE_BREAK);
        buffer.append("Transaction number:").append(COMMA).append(theReport.getTransactionNumber())
                .append(LINE_BREAK);
        buffer.append("Submission method:").append(COMMA).append(theReport.getMediaCode()).append(
                LINE_BREAK);
        buffer.append("Source of transfer:").append(COMMA).append(theReport.getSourceOfTransfer())
                .append(LINE_BREAK);

    
        // Titles - Class Conversion Summary section
        buffer.append(LINE_BREAK).append(LINE_BREAK);
        buffer.append("Class conversion summary:").append(LINE_BREAK);
        buffer.append(" ").append(COMMA);
        buffer.append("Risk/Return Category:").append(COMMA);
        buffer.append("Investment Option:").append(COMMA);
        buffer.append("Transfer Out ($):").append(COMMA);
        buffer.append("Unit Value").append(COMMA);
        buffer.append("Number Of Units").append(COMMA);
        buffer.append("Transfer In ($):").append(COMMA);
        buffer.append("Unit Value").append(COMMA);
        buffer.append("Number Of Units").append(COMMA);
        buffer.append(LINE_BREAK).append(LINE_BREAK);

        List<FundGroup> summaryList = theReport.getTransferFromsAndTos();
        Iterator<FundGroup> iterator = summaryList.iterator();
        while (iterator.hasNext()) {
            FundGroup category = iterator.next();
            if (category != null) {
                Object o[] = category.getFunds();
                for (int i = 0; i < o.length; i++) {
                    buffer.append(" ").append(COMMA);
                    buffer.append(category.getGroupName()).append(COMMA);
                    ClassConversionDetailsFund fund = (ClassConversionDetailsFund) o[i];
                    if (fund != null) {
                        buffer.append(fund.getName()).append(COMMA);
                        buffer.append(fund.getAmount()).append(COMMA);
                        buffer.append(fund.getDisplayUnitValue()).append(COMMA);
                        buffer.append(fund.getNumberOfUnits()).append(COMMA);
                        buffer.append(fund.getToAmount()).append(COMMA);
                        buffer.append(fund.getDisplayToUnitValue()).append(COMMA);
                        buffer.append(fund.getToNumberOfUnits()).append(COMMA);
                    }
                    buffer.append(LINE_BREAK);
                }
            }
        }

        // Titles - Details section
        buffer.append(LINE_BREAK).append(LINE_BREAK);
        buffer.append("Class conversion details:").append(LINE_BREAK);
        buffer.append(" ").append(COMMA);
        buffer.append("Risk Category:").append(COMMA);
        buffer.append("Investment Option:").append(COMMA);
        buffer.append("Money Type:").append(COMMA);
        buffer.append("Amount ($):").append(COMMA);
        buffer.append("Unit Value:").append(COMMA);
        buffer.append("Number Of Units:").append(COMMA).append(LINE_BREAK);

        Collection<FundGroup> detailsList = theReport.getDetails();
        iterator = detailsList.iterator();
        while (iterator.hasNext()) {
            FundGroup category = iterator.next();
            if (category != null) {
                Object object[] = category.getFunds();
                for (int i = 0; i < object.length; i++) {
                    buffer.append(" ").append(COMMA);
                    buffer.append(category.getGroupName()).append(COMMA);
                    TransactionDetailsFund fund = (TransactionDetailsFund) object[i];
                    if (fund != null) {
                        buffer.append(fund.getName()).append(COMMA);
                        buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
                        buffer.append(fund.getAmount()).append(COMMA);
                        if (fund.getUnitValue().doubleValue() == (double) 0.0) {
                            buffer.append(" ").append(COMMA);
                        } else {
                            buffer.append(fund.getDisplayUnitValue()).append(COMMA);
                        }
                        if (fund.getNumberOfUnits().doubleValue() == (double) 0.0) {
                            buffer.append(" ").append(COMMA);
                        } else {
                            buffer.append(fund.getNumberOfUnits()).append(COMMA);
                        }
                    }
                    buffer.append(LINE_BREAK);
                }
            }
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getDownloadData");

        return buffer.toString().getBytes();
    }

    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
   
    public Document prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException {

        TransactionDetailsClassConversionForm form = (TransactionDetailsClassConversionForm) reportForm;
        TransactionDetailsClassConversionReportData data = (TransactionDetailsClassConversionReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();

        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext())
                .getLayoutBean(BDPdfConstants.CLASS_CONVERSION_PATH, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();

        Element rootElement = doc.createRootElement(BDPdfConstants.CLASS_CONVERSION);

        setIntroXMLElements(layoutPageBean, doc, rootElement, request);

        // Summary Info
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean);

        String bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader);

        bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY2_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc, bodyHeader);

        setFundSummaryXMLElements(data, doc, rootElement);

        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Fund Details - start
            Element fundDetailsElement = doc.createElement(BDPdfConstants.FUND_DETAILS);
            Iterator iterator = report.getDetails().iterator();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {

                Element fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                FundGroup theItem = (FundGroup) iterator.next();
                if (theItem != null) {
                    // Sets fund group.
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, theItem.getGroupName());
                    for (Fund fund : theItem.getFunds()) {
                        // Sets main report.
                        setFundDetailsXMLElements(doc, fundDetailElement, fund);
                    }
                }
                doc.appendElement(fundDetailsElement, fundDetailElement);

            }
            doc.appendElement(rootElement, fundDetailsElement);
            // Fund Details - end
        }

        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }

        setFooterXMLElements(layoutPageBean, doc, rootElement, request);

        return doc.getDocument();

    }

    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /**
     * @see BaseReportController#getFileName()
     */
    @Override
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        Contract currentContract = getBobContext(request).getCurrentContract();
        String date = DateRender.formatByPattern(currentContract
                .getContractDates().getAsOfDate(), null, RenderConstants.MEDIUM_YMD_DASHED,
                 RenderConstants.MEDIUM_MDY_SLASHED)
                .replace(BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
        String csvFileName = getReportName()
                + HYPHON_SYMBOL
                + currentContract.getContractNumber()
                + HYPHON_SYMBOL
                + date
                + CSV_EXTENSION;
        return csvFileName;
    }
    
    /**
     * This method creates XML elements for both from_fund and to_fund details.
     * 
     * @param data
     * @param doc
     * @param rootElement
     */
    @SuppressWarnings("unchecked")
    private void setFundSummaryXMLElements(TransactionDetailsClassConversionReportData data,
            PDFDocument doc, Element rootElement) {

        // Summary Details - start
        Element reportSummaryDetails = doc.createElement(BDPdfConstants.REPORT_SUMMARY_DETAILS);
        Element reportSummaryDetail;

        for (FundGroup theItem : (List<FundGroup>) data.getTransferFromsAndTos()) {
            reportSummaryDetail = doc.createElement(BDPdfConstants.REPORT_SUMMARY_DETAIL);
            if (theItem != null) {
                doc.appendTextNode(reportSummaryDetail, BDPdfConstants.FUND_GROUP, theItem.getGroupName());
                setFundItems(data, theItem, doc, reportSummaryDetail);
            }
            doc.appendElement(reportSummaryDetails, reportSummaryDetail);
        }

        doc.appendElement(rootElement, reportSummaryDetails);
        // Summary Details - end

    }

    /**
     * This method creates XML elements for each fund item for from_fund and to_fund details.
     * 
     * @param data
     * @param theItem
     * @param doc
     * @param isFromFund
     * @param fundDetailElement
     */
    private void setFundItems(TransactionDetailsClassConversionReportData data, FundGroup theItem,
            PDFDocument doc, Element reportSummaryDetail) {

        for (Fund fund : theItem.getFunds()) {
            Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
            ClassConversionDetailsFund txnDetailsFund = (ClassConversionDetailsFund) fund;
            doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
            if (txnDetailsFund.getAmount() != null) {

                String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null, null);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT, fundAmt);
                String unitValue = NumberRender.formatByType(txnDetailsFund.getDisplayUnitValue(),
                        null, null, 2, BigDecimal.ROUND_HALF_DOWN, 1);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.UNIT_VALUE, unitValue);
                String noOfUnits = NumberRender.formatByType(txnDetailsFund
                        .getDisplayNumberOfUnits(), null, null, 6, BigDecimal.ROUND_HALF_DOWN, 1);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.NO_OF_UNITS, noOfUnits);
                fundAmt = NumberRender.formatByType(txnDetailsFund.getToAmount(), null,
                        RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.TO_AMT, fundAmt);
                unitValue = NumberRender.formatByType(txnDetailsFund.getDisplayToUnitValue(), null,
                        null, 2, BigDecimal.ROUND_HALF_DOWN, 1);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.TO_UNIT_VALUE, unitValue);
                noOfUnits = NumberRender.formatByType(txnDetailsFund.getDisplayToNumberOfUnits(),
                        null, null, 6, BigDecimal.ROUND_HALF_DOWN, 1);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.TO_NO_OF_UNITS, noOfUnits);

            }
            doc.appendElement(reportSummaryDetail, fundTxnElement);
        }

    }
    
    /**
     * This method sets summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param data
     * @param layoutPageBean
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, TransactionDetailsClassConversionReportData data, 
                 LayoutPage layoutPageBean) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, data.getParticipantName());
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, data.getParticipantSSN());

        String formattedDate = DateRender.formatByStyle(data.getTransactionDate(), null,
                RenderConstants.MEDIUM_STYLE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.INVESTED_DATE, formattedDate);

        formattedDate = DateRender.formatByStyle(data.getRequestDate(), null,
                RenderConstants.MEDIUM_STYLE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.REQUEST_DATE, formattedDate);

        String totalAmt = NumberRender.formatByType(data.getTotalToAmount(), null,
                RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_TO_AMT, totalAmt);

        totalAmt = NumberRender.formatByType(data.getTotalAmount(), null,
                RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_AMT, totalAmt);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER, data.getTransactionNumber());

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.SUBMISSION_METHOD, data.getMediaCode());

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.SOURCE_OF_TRANSFER, data.getSourceOfTransfer());

        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method sets fund details XML elements
     * 
     * @param doc
     * @param fundDetailElement
     * @param fund
     */
    private void setFundDetailsXMLElements(PDFDocument doc, Element fundDetailElement, Fund fund) {
        Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
        TransactionDetailsFund txnDetailsFund = (TransactionDetailsFund) fund;
        doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
        doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE,
                txnDetailsFund.getMoneyTypeDescription());
        String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null, null);
        doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
                removeParanthesesAndPrefixMinus(fundAmt));
        if (txnDetailsFund.displayUnitValue()) {
            String unitValue = NumberRender.formatByType(txnDetailsFund.getDisplayUnitValue(), null, null, 2,
                    BigDecimal.ROUND_HALF_DOWN, 1);
            doc.appendTextNode(fundTxnElement, BDPdfConstants.UNIT_VALUE, unitValue);
            if (txnDetailsFund.displayNumberOfUnits()) {
                String noOfUnits = NumberRender.formatByType(txnDetailsFund
                        .getDisplayNumberOfUnits(), null, null, 6,
                        BigDecimal.ROUND_HALF_DOWN, 1);
                doc.appendTextNode(fundTxnElement, BDPdfConstants.NO_OF_UNITS,
                        noOfUnits);
            }
        }
        doc.appendElement(fundDetailElement, fundTxnElement);
    }
    
    /**
     * @See BaseReportAction#getNumberOfRowsInReport()
     */
    @Override
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if(report.getDetails() != null) {
            noOfRows = report.getDetails().size();
        }
        return noOfRows;
    }
    @RequestMapping(value ="/transaction/classConversionTransactionReport/" , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			try {
    				doCommon(form, request, null);
    			} catch (SystemException e) {
    				e.printStackTrace();
    			}
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doDefault( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

   @RequestMapping(value ="/transaction/classConversionTransactionReport/" ,params={"task=page"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
	   String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
	   if(bindingResult.hasErrors()){
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   try {
   				doCommon(form, request, null);
   			} catch (SystemException e) {
   				e.printStackTrace();
   			}
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
	    forward=super.doPage( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   
   @RequestMapping(value ="/transaction/classConversionTransactionReport/" ,params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   public String doSort (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
	   if(bindingResult.hasErrors()){
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   try {
   				doCommon(form, request, null);
   			} catch (SystemException e) {
   				e.printStackTrace();
   			}
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
	    forward=super.doSort( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   @RequestMapping(value ="/transaction/classConversionTransactionReport/", params={"task=download"},method =  {RequestMethod.POST,RequestMethod.GET}) 
   public String doDownload (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
	   if(bindingResult.hasErrors()){
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   try {
   				doCommon(form, request, null);
   			} catch (SystemException e) {
   				e.printStackTrace();
   			}
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
	    forward=super.doDownload( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   
   @RequestMapping(value ="/transaction/classConversionTransactionReport/", params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   public String doDownloadAll (@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	   String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
	   if(bindingResult.hasErrors()){
		   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		   if(errDirect!=null){
			   try {
   				doCommon(form, request, null);
   			} catch (SystemException e) {
   				e.printStackTrace();
   			}
			   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		   }
	   }
	    forward=super.doDownloadAll( form, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
   @RequestMapping(value = "/transaction/classConversionTransactionReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
	public String doPrintPDF(@Valid @ModelAttribute("transactionDetailsClassConversionForm") TransactionDetailsClassConversionForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		 String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
	    	}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				try {
    				doCommon(form, request, null);
    			} catch (SystemException e) {
    				e.printStackTrace();
    			}
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																										// default
			}
		}
		 forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
   
    /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
}
