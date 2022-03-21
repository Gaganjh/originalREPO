package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.owasp.encoder.Encode;
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
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
/**
 * 
 * Action class for FTF transaction details report
 * @author 
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"fundToFundTransactionReportForm"})

public class TransactionDetailsFTFController extends BOBReportController {
	@ModelAttribute("fundToFundTransactionReportForm") 
	public TransactionDetailsFTFForm populateForm() 
	{
		return new TransactionDetailsFTFForm();
		}
	
	 public static HashMap<String,String> forwards = new HashMap<String,String>();
	 static{
		 forwards.put("input","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("default","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("sort","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("filter","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("page","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("print","/transaction/fundToFundTransactionReport.jsp");
		 forwards.put("bobPage","redirect:/do/bob/");
		 }
		
	private static final String FUND_TO_FUND_DETAILS="FundtoFundDetails";
	
    private static final DecimalFormat TWO_DECIMALS = new DecimalFormat("0.00");
    
    private static final String XSLT_FILE_KEY_NAME = "FundToFundTransactionDetailsReport.XSLFile";
    
    //synchronized method to avoid race condition. 
  	public static synchronized String formatPercentageFormatter(Double value) { 
          return TWO_DECIMALS.format(value); 
      }
	
	protected String getDefaultSort() {
		return TransactionDetailsFTFReportData.SORT_FIELD_WEBSRTNO;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
	
		
	/**
     * @see BaseReportController#populateReportCriteria()
     */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		
		TransactionDetailsFTFForm form = (TransactionDetailsFTFForm)actionForm;

		String transactionNumber = (String)request.getParameter("transactionNumber");
		if (transactionNumber == null || transactionNumber.equals("")) {
			transactionNumber = form.getTransactionNumber();
		}
		
		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();

		String contractNumber = String.valueOf(currentContract.getContractNumber());
		if (contractNumber == null || contractNumber.equals("") || contractNumber.equals("0")) {
			contractNumber = form.getContractNumber();
		}
		
		String participantId = (String)request.getParameter("pptId"); // new reference form.
		if (participantId == null || participantId.equals("")) {
			participantId = (String)request.getParameter("participantId"); // do legacy check incase a link was missed 
		if (participantId == null || participantId.equals("")) {
				participantId = form.getPptId(); 
			}			
		}
		
		if (currentContract.isDefinedBenefitContract()) {
			criteria.addFilter(TransactionDetailsFTFReportData.CONTRACT_TYPE_DB, Boolean.TRUE);
		}
		
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_TRANSACTION_NUMBER, transactionNumber);
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_PARTICIPANT_ID, participantId);
		criteria.addFilter(TransactionDetailsFTFReportData.FILTER_CONTRACT_NUMBER, contractNumber);
		

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}

	/**
     * the method populates sort criteria
     * 
     * @param criteria
     * @param actionForm
     * @param request
     * @throws SystemException
     */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm actionForm, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
		TransactionDetailsFTFForm form = (TransactionDetailsFTFForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsFTFReportData.SORT_FIELD_WEBSRTNO, ReportSort.ASC_DIRECTION);
		criteria.insertSort(TransactionDetailsFTFReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION, ReportSort.ASC_DIRECTION);
								
		
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"+sortField+" and direction: " + sortDirection);
		}
	}
	
	
	public TransactionDetailsFTFController() {
		super(TransactionDetailsFTFController.class);
	}
	
	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsFTFReportData.REPORT_ID;
	}

	/**
	 * @see BaseReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsFTFReportData.REPORT_NAME;
	}

    /**
     * The getReportData() method of BaseReportAction class is being overridden, so that the user
     * cannot see the Resource Limit Exceeded Exception message. Instead, he will see a Technical
     * Difficulties message.
     */
    protected ReportData getReportData(String reportId, ReportCriteria reportCriteria,
            HttpServletRequest request) throws SystemException, ReportServiceException {
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
     * @see BaseReportController#doCommon()
     */
	public String doCommon( TransactionDetailsFTFForm actionForm, HttpServletRequest request,
								  HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		String forward=null;
        try {
            forward = super.doCommon( actionForm, request, response);
        } catch (SystemException e) {
            // Log the system exception.
            LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, e);

            // Show user friendly message.
            List<GenericException> errors = new ArrayList<GenericException>();
            errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
            setErrorsInRequest(request, errors);
            forward = forwards.get("input");
            return forward;
        }
            
		TransactionDetailsFTFReportData report = (TransactionDetailsFTFReportData)request.getAttribute(BDConstants.REPORT_BEAN);				
		

		if (report!=null && (report.getDetails().size() != 0)) {
			actionForm.setReport(report);
			actionForm.setTransactionNumber(report.getTransactionNumber());
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}
						
		return forward;
	}

    /**
     * 
     * It returns the csv.
     * 
     * @return byte[]
     */
	@SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		
		TransactionDetailsFTFReportData theReport = (TransactionDetailsFTFReportData)report;
			
		if (logger.isDebugEnabled())
			logger.debug("entry -> getDownloadData");
		// get the content objects
		StringBuffer buffer = new StringBuffer();

		// Title
        // Gets layout page for fundToFundTransactionReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.FTF_DETAILS_PATH, request);
        String pageName = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.PAGE_NAME, null);        
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);		
		buffer.append(pageName).append(LINE_BREAK+LINE_BREAK);
		buffer.append("Fund to fund transfer").append(LINE_BREAK+LINE_BREAK);
		

	    Contract currentContract = getBobContext(request).getCurrentContract();
	    buffer.append("Contract number:").append(COMMA).append(currentContract.getContractNumber());
		buffer.append(LINE_BREAK);
	    buffer.append("Company name:").append(COMMA).append(currentContract.getCompanyName());
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		buffer.append(subHeader+":").append(LINE_BREAK);
		
		if (currentContract.isDefinedBenefitContract()==false) {
		String participantName = StringUtils.deleteWhitespace(theReport.getParticipantName());
		buffer.append("Name:").append(COMMA).append(BDConstants.DOUBLE_QUOTES).append(StringUtils.replaceOnce(participantName, ",", ", ")).append(BDConstants.DOUBLE_QUOTES).append(LINE_BREAK);
		buffer.append("SSN:").append(COMMA).append(theReport.getParticipantSSN()).append(LINE_BREAK);
		}
		
		buffer.append("Invested date:").append(COMMA).append(
				DateRender.format(theReport.getTransactionDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Request date:").append(COMMA).append(
				DateRender.format(theReport.getRequestDate(),RenderConstants.MEDIUM_YMD_SLASHED))
				.append(LINE_BREAK);
		buffer.append("Total Amount:").append(COMMA).append(theReport.getTotalAmount()).append(
                LINE_BREAK);
        buffer.append("Transaction Number:").append(COMMA).append(theReport.getTransactionNumber())
                .append(LINE_BREAK);
        buffer.append("Submission Method:").append(COMMA).append(theReport.getMediaCode()).append(
                LINE_BREAK);
        buffer.append("Source of Transfer:").append(COMMA).append(theReport.getSourceOfTransfer())
                .append(LINE_BREAK);

		// Titles - Transferred from section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transferred From:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment Option:").append(COMMA);
		if (theReport.doFromMoneyTypesExist()) {
			buffer.append("Money type:").append(COMMA);
		}
		buffer.append("Amount($):").append(COMMA);
		buffer.append("% Out:").append(LINE_BREAK).append(LINE_BREAK);
		
		List<FundGroup> fromList = theReport.getTransferFroms();
		Iterator<FundGroup> it = fromList.iterator();
		while (it.hasNext()) {
			FundGroup category = it.next();
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						if (theReport.doFromMoneyTypesExist()) {
							buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
						}
						buffer.append(fund.getAmount()).append(COMMA);
						BigDecimal percent = fund.getPercentage();
						percent.setScale(2, BigDecimal.ROUND_HALF_UP);
						String pct = formatPercentageFormatter(percent.doubleValue());
						buffer.append(pct);
					}
					buffer.append(LINE_BREAK);
				}
			}
		}
		// Total line
		buffer.append(COMMA).append(COMMA).append("Total:").append(COMMA);
		if (theReport.doFromMoneyTypesExist()) {
			buffer.append(COMMA);
		}		
		buffer.append(theReport.getTotalFromAmount());
		
		// Titles - Transferred to section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transferred To:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment Option:").append(COMMA);
		buffer.append("Amount($):").append(COMMA);
		buffer.append("% In:").append(LINE_BREAK).append(LINE_BREAK);
		
		List<FundGroup> toList = theReport.getTransferTos();
		it = toList.iterator();
		while (it.hasNext()) {
			FundGroup category = it.next();
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						BigDecimal percent = fund.getPercentage();
						percent.setScale(2, BigDecimal.ROUND_HALF_UP);
						String pct = formatPercentageFormatter(percent.doubleValue());
						buffer.append(pct);
					}
					buffer.append(LINE_BREAK);
				}	
			}
		} 
		// Total line
		buffer.append(COMMA).append(COMMA).append("Total:").append(COMMA);
		buffer.append(theReport.getTotalToAmount()).append(COMMA);
		
		BigDecimal percent = theReport.getTotalToPct();
		percent.setScale(2, BigDecimal.ROUND_HALF_UP);
		String pct = formatPercentageFormatter(percent.doubleValue());

		buffer.append(pct).append(LINE_BREAK);


		// Insert Redemption Fees / MVA as needed
		String message, contentParams;
		int k = 0;
		if (theReport.getRedemptionFees().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			contentParams = NumberRender.formatByType(theReport.getRedemptionFees().abs().negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED, 
			                 ContentTypeManager.instance().MISCELLANEOUS, null, 
                    removeParanthesesAndPrefixMinus(contentParams));
			buffer.append(++k).append(".");
			buffer.append(message);
		}
		if (theReport.getMva().doubleValue() > (double)0.0) {
			buffer.append(LINE_BREAK);
			contentParams = NumberRender.formatByType(theReport.getMva().abs().negate(), null, RenderConstants.CURRENCY_TYPE);
			message = ContentHelper.getContentTextWithParamsSubstitution(BDContentConstants.MESSAGE_MVA_APPLIED,
			                 ContentTypeManager.instance().MISCELLANEOUS, null, 
                    removeParanthesesAndPrefixMinus(contentParams));
			buffer.append(++k).append(".");
			buffer.append(message);
		}
		
		boolean showMessage = false;
		if ((theReport.getRedemptionFees().doubleValue() != 0) || 
				(theReport.getMva().doubleValue() > (double)0.0)) {
				    showMessage = true;
				}
		
		// Titles - Details section
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Transfer Details:").append(LINE_BREAK);
		buffer.append(" ").append(COMMA);		
		buffer.append("Risk category:").append(COMMA);		
		buffer.append("Investment Option:").append(COMMA);
        buffer.append("Money Type:").append(COMMA);
		buffer.append("Amount($):").append(COMMA);
		buffer.append("Unit Value:").append(COMMA);
        buffer.append("Number Of Units:").append(COMMA);
        if(showMessage){
        	buffer.append("Comments:");
        }
        buffer.append(LINE_BREAK).append(LINE_BREAK);
		
		Collection<FundGroup> detailsList = theReport.getDetails();
		it = detailsList.iterator();
		while (it.hasNext()) {
			FundGroup category = it.next();
			if (category != null) {
				Object o[] = category.getFunds();
				for (int i=0; i < o.length; i++) {
					buffer.append(" ").append(COMMA);		
					buffer.append(category.getGroupName()).append(COMMA);
					TransactionDetailsFund fund = (TransactionDetailsFund)o[i];
					if (fund != null) {
						buffer.append(fund.getName()).append(COMMA);
						buffer.append(fund.getMoneyTypeDescription()).append(COMMA);
						buffer.append(fund.getAmount()).append(COMMA);
						if (fund.getUnitValue().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(NumberRender.formatByType(fund.getDisplayUnitValue(), null, null)).append(fund.displayNumberOfUnits() ? "" : "%").append(COMMA);
						}
						if (fund.getNumberOfUnits().doubleValue() == (double)0.0) {
							buffer.append(" ").append(COMMA);
						} else {
							buffer.append(fund.getNumberOfUnits().doubleValue()).append(COMMA);
						}
						if(showMessage){
							buffer.append(fund.getComments());
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
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request) throws ParserConfigurationException {
        
        TransactionDetailsFTFForm form = (TransactionDetailsFTFForm) reportForm;
        TransactionDetailsFTFReportData data = (TransactionDetailsFTFReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();
        
        // Gets layout page for fundToFundTransactionReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.FTF_DETAILS_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.FTF_DETAILS);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        boolean showComments = (data.getMva().doubleValue() != (double) 0.0) || (data.getRedemptionFees().doubleValue() != (double) 0.0);

        // Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, request);

        String bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader);
        
        bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY2_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc, bodyHeader);
        
        bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY3_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER3, rootElement, doc, bodyHeader);
        
        if(data.doFromMoneyTypesExist()) {
            //Display "Money Type" column in from fund table
            doc.appendTextNode(rootElement, BDPdfConstants.IS_FROM_MONEY_TYPE_EXIST, null);
        }

        if(data.showFromPercent()) {
            //Display "% Out" column in from fund table
            doc.appendTextNode(rootElement, BDPdfConstants.SHOW_FROM_PERCENT, null);
        }
        
        // Sets Information Messages.
        setMessagesXMLElements(data, doc, rootElement);
        
        // Sets from_fund and to_fund details.
        setFromToFundDetailsXMLElements(data, doc, rootElement);

        // Gets number of rows present in report page.
        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Main Report - start
            Element fundDetailsElement = doc.createElement(BDPdfConstants.FUND_DETAILS);
            Iterator iterator = report.getDetails().iterator();
            // Gets number of rows to be shown in PDF.
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                
                Element fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                FundGroup theItem = (FundGroup) iterator.next();
                if (theItem != null) {
                    // Sets fund group.
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, theItem.getGroupName());
                    for (Fund fund : theItem.getFunds()) {  
                        if (rowCount <= maxRowsinPDF) {
                            // Sets fund to fund transfer details.
                            setFTFTransferDetailsXMLElements(doc, fundDetailElement, fund, showComments);
                            rowCount++;
                        }     
                    }
                }
                doc.appendElement(fundDetailsElement, fundDetailElement);
                
            }
            doc.appendElement(rootElement, fundDetailsElement);
            // Main Report - end
        }
  
        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);

        return doc.getDocument();

    }
	
    /**
     * @See BDPdfAction#getNumberOfRowsInReport()
     * Each fund detail comprises one row in report table and so this method is 
     * overridden and modified.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if(report.getDetails() != null) {
            for (FundGroup theItem : (ArrayList<FundGroup>) report.getDetails()) {
                noOfRows += theItem.getFunds().length;     
            }
        }
        return noOfRows;
    }  
    
	/**
     * This method is used to frame the file name used for the downloaded CSV.
     * 
     * @param BaseReportForm
     * @param HttpServletRequest
     * @return The file name used for the downloaded CSV.
     */
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
    	Contract currentContract = getBobContext(request).getCurrentContract();
    	String formattedTransactionDate = DateRender.formatByPattern(currentContract
                .getContractDates().getAsOfDate(), null, RenderConstants.MEDIUM_YMD_DASHED,
                RenderConstants.MEDIUM_MDY_SLASHED).replace(
                BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
        String csvFileName=FUND_TO_FUND_DETAILS+"-"+currentContract.getContractNumber()+"-"+formattedTransactionDate+CSV_EXTENSION;
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
     * @param data
     * @param layoutPageBean
     * @param request
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, TransactionDetailsFTFReportData data, 
                 LayoutPage layoutPageBean, HttpServletRequest request) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);
        
        String formattedDate = DateRender.formatByPattern(data.getTransactionDate(), null, 
                               RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.INVESTED_DATE, formattedDate);
        
        formattedDate= DateRender.formatByPattern(data.getRequestDate(), null, 
                       RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.REQUEST_DATE, formattedDate);
        
        String totalAmt = NumberRender.formatByType(data.getTotalAmount(), null, RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_AMT, totalAmt);
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER, data.getTransactionNumber());
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.SUBMISSION_METHOD, data.getMediaCode());
        
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.SOURCE_OF_TRANSFER, data.getSourceOfTransfer());
        
        if (!(getBobContext(request).getCurrentContract().isDefinedBenefitContract())) { 
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, data.getParticipantName());
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, data.getParticipantSSN());
        }
       
        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method creates XML elements for both from_fund and to_fund details.
     * 
     * @param data
     * @param doc
     * @param rootElement
     */
    @SuppressWarnings("unchecked")
    private void setFromToFundDetailsXMLElements(TransactionDetailsFTFReportData data, PDFDocument doc, 
            Element rootElement) {

        // Fund Details - start
        Element fromFundDetailsElement = doc.createElement(BDPdfConstants.FROM_FUND_DETAILS);
        Element toFundDetailsElement = doc.createElement(BDPdfConstants.TO_FUND_DETAILS);
        Element fundDetailElement;
        String totalAmt = null;

        for (FundGroup categoryIteratorFromFund : (List<FundGroup>)data.getTransferFroms()) {
            fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
            if (categoryIteratorFromFund != null) {
                // Sets from_fund group.
                doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, categoryIteratorFromFund.getGroupName());
                // Sets from_fund items.
                setFundItems(data, categoryIteratorFromFund, doc, true, fundDetailElement);
            }
            doc.appendElement(fromFundDetailsElement, fundDetailElement);
        }
        
        for (FundGroup categoryIteratorToFund : (List<FundGroup>)data.getTransferTos()) {
            fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
            if (categoryIteratorToFund != null) {
                // Sets to_fund group.
                doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, categoryIteratorToFund.getGroupName());
                // Sets to_fund items.
                setFundItems(data, categoryIteratorToFund, doc, false, fundDetailElement);
            }
            doc.appendElement(toFundDetailsElement, fundDetailElement);
        }
        
        // Sets total from_fund amt, total from_fund percent, total to_fund amt and
        // total to_fund percent
        if (data.getTotalFromAmount() != null && data.getTotalFromPct() != null) {
            totalAmt = NumberRender.formatByType(data.getTotalFromAmount(), null, 
                              RenderConstants.CURRENCY_TYPE, Boolean.FALSE);  
            doc.appendTextNode(fromFundDetailsElement, BDPdfConstants.TXN_TOTAL_AMT,
                    removeParanthesesAndPrefixMinus(totalAmt));
        }  
        if (data.getTotalToAmount() != null && data.getTotalToPct() != null) {
            totalAmt = NumberRender.formatByType(data.getTotalToAmount(), null, 
                              RenderConstants.CURRENCY_TYPE, Boolean.FALSE);  
            doc.appendTextNode(toFundDetailsElement, BDPdfConstants.TXN_TOTAL_AMT,
                    removeParanthesesAndPrefixMinus(totalAmt));
            String totalPercent = NumberRender.formatByType(data.getTotalToPct(), null, null);
            doc.appendTextNode(toFundDetailsElement, BDPdfConstants.TXN_TOTAL_PERCENT, totalPercent);
        }  
        
        doc.appendElement(rootElement, fromFundDetailsElement);
        doc.appendElement(rootElement, toFundDetailsElement);
        // Fund Details - end
       
    }

    /**
     * This method creates XML elements for each fund item for from_fund and to_fund details.
     * 
     * @param data
     * @param theItem
     * @param doc
     * @param isFromFund boolean to determine whether from_fund items or to_fund items
     * @param fundDetailElement
     */
    private void setFundItems(TransactionDetailsFTFReportData data, FundGroup theItem, 
                 PDFDocument doc, boolean isFromFund, Element fundDetailElement){
        for (Fund fund : theItem.getFunds()) {
            Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
            TransactionDetailsFund txnDetailsFund = (TransactionDetailsFund) fund;
            if (txnDetailsFund != null) {
                doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
                doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE, txnDetailsFund.getMoneyTypeDescription());
                if (txnDetailsFund.getAmount() != null && txnDetailsFund.getPercentage() != null) {
                    
                    String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null, 
                                     RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                    // Needs to remove parantheses surrounded by amt and prefix minus with
                    // the amt.
                    doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
                            removeParanthesesAndPrefixMinus(fundAmt));
                    if (isFromFund && data.showFromPercent() || !(isFromFund)) {
                        // Sets fund percent if from_fund and transfer instructions were provided
                        // using percentages. No conditions for to_fund.
                        String fundPercentage = NumberRender.formatByType(txnDetailsFund.getPercentage(), null, null);
                        doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_PERCENTAGE, fundPercentage);
                    }
                    
                }
            }
            doc.appendElement(fundDetailElement, fundTxnElement);
        }
    }
    
    /**
     * This method sets XML elements for info messages
     * 
     * @param data
     * @param doc
     * @param rootElement
     */
    private void setMessagesXMLElements(TransactionDetailsFTFReportData data, PDFDocument doc, Element rootElement) {
        String message, contentParams;
        int msgNum = 0;
        Element infoMessagesElement = doc.createElement(BDPdfConstants.INFO_MESSAGES);
        if (data.getRedemptionFees().doubleValue() > (double)0.0) {
            // Sets Redemption Fees Message if redemption fees has been applied to the transaction.
            Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String.valueOf(++msgNum));
            contentParams = NumberRender.formatByType(data.getRedemptionFees().abs().negate(), null, RenderConstants.CURRENCY_TYPE);
            message = ContentHelper.getContentTextWithParamsSubstitution(
                    BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED,
                    ContentTypeManager.instance().MISCELLANEOUS, null, 
                    removeParanthesesAndPrefixMinus(contentParams));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }
        if (data.getMva().doubleValue() > (double)0.0) {
            // Sets MVA Message if MVA has been applied to the transaction.
            Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String.valueOf(++msgNum));
            contentParams = NumberRender.formatByType(data.getMva().abs().negate(), null, RenderConstants.CURRENCY_TYPE);
            message = ContentHelper.getContentTextWithParamsSubstitution(
                    BDContentConstants.MESSAGE_MVA_APPLIED,
                    ContentTypeManager.instance().MISCELLANEOUS, null, 
                    removeParanthesesAndPrefixMinus(contentParams));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }
        doc.appendElement(rootElement, infoMessagesElement);
    }
    
    /**
     * This method sets fund to fund transfer details XML elements
     * 
     * @param doc
     * @param fundDetailElement
     * @param fund
     */
    private void setFTFTransferDetailsXMLElements(PDFDocument doc, Element fundDetailElement, Fund fund, boolean showComments) {
        Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
        TransactionDetailsFund txnDetailsFund = (TransactionDetailsFund) fund;
        if (txnDetailsFund != null) {
            doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
            if (StringUtils.isNotEmpty(txnDetailsFund.getMoneyTypeDescription())) {
                doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE, txnDetailsFund.getMoneyTypeDescription());
            }
            String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null, 
                             RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
            // Needs to remove parantheses surrounded by amt and prefix minus with
            // the amt.
            doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
                    removeParanthesesAndPrefixMinus(fundAmt));
            if (txnDetailsFund.displayUnitValue()) {
                String unitValue = NumberRender.formatByType(txnDetailsFund.getDisplayUnitValue(), 
                		BDConstants.NO_RULE, null, 2, BigDecimal.ROUND_HALF_DOWN, 1);  
                doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_UNIT_VALUE, unitValue);
            }
            else {
                doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_UNIT_VALUE, BDConstants.NO_RULE);
            }
            if (txnDetailsFund.displayNumberOfUnits()) {
                String numberOfUnits = NumberRender.formatByType(txnDetailsFund.getDisplayNumberOfUnits(), 
                        null, null, 6, BigDecimal.ROUND_HALF_DOWN, 1);  
                doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_NUM_OF_UNITS, numberOfUnits);
            }
            else {
                doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_NUM_OF_UNITS, BDConstants.NO_RULE);
            }
            if (showComments) { 
                // Sets text in "Comments" column
                doc.appendTextNode(fundTxnElement, BDPdfConstants.COMMENTS, txnDetailsFund.getComments());
            }
        }
        doc.appendElement(fundDetailElement, fundTxnElement);
    }
    @RequestMapping(value ="/transaction/fundToFundTransactionReport/" , method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doDefault( form, request, response);
    	return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/fundToFundTransactionReport/",params={"task=filter"}, method =  {RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward = preExecute(form, request, response);
    	if(StringUtils.isNotBlank(forward)) {
    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	}
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	 forward=super.doFilter( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }	
    
    	 @RequestMapping(value ="/transaction/fundToFundTransactionReport/" ,params={"task=page"} , method =  {RequestMethod.GET}) 
 	    public String doPage (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	    		throws IOException,ServletException, SystemException {
    		 String forward = preExecute(form, request, response);
    	    	if(StringUtils.isNotBlank(forward)) {
    	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
    	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doPage( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   
 	   @RequestMapping(value ="/transaction/fundToFundTransactionReport/" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
 	   public String doSort (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		  String forward = preExecute(form, request, response);
 	    	if(StringUtils.isNotBlank(forward)) {
 	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
 	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doSort( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   @RequestMapping(value ="/transaction/fundToFundTransactionReport/", params={"task=download"},method =  {RequestMethod.GET}) 
 	   public String doDownload (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   } 
 		   String forward=super.doDownload( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	   
 	   @RequestMapping(value ="/transaction/fundToFundTransactionReport/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
 	   public String doDownloadAll (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	   throws IOException,ServletException, SystemException {
 		  String forward = preExecute(form, request, response);
 	    	if(StringUtils.isNotBlank(forward)) {
 	    		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
 	    	}
 		   if(bindingResult.hasErrors()){
 			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 			   if(errDirect!=null){
 				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 			   }
 		   }
 		    forward=super.doDownloadAll( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	  @RequestMapping(value ="/transaction/fundToFundTransactionReport/", params={"task=printPDF"}, method =  {RequestMethod.GET}) 
	   public String doPrintPDF (@Valid @ModelAttribute("fundToFundTransactionReportForm") TransactionDetailsFTFForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
 		 String forward = preExecute(form, request, response);
     	if(StringUtils.isNotBlank(forward)) {
     		return StringUtils.contains(forward,'/')? forward:forwards.get(forward); 
     	}
		   if(bindingResult.hasErrors()){
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doPrintPDF( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
    /**
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
