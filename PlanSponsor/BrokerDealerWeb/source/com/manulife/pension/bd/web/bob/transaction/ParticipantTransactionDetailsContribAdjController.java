package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.InitBinder;	
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import javax.validation.Valid;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContribAdjReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the participant transaction details Adjustment page.
 * 
 * @author harlomte
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantContribAdjDetailsForm"})

public class ParticipantTransactionDetailsContribAdjController extends AbstractTransactionReportController {
	@ModelAttribute("participantContribAdjDetailsForm")
	public ParticipantTransactionDetailsContribAdjForm populateForm() {
		return new ParticipantTransactionDetailsContribAdjForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/participantContribAdjDetailsReport.jsp");
		forwards.put("default","/transaction/participantContribAdjDetailsReport.jsp");
		forwards.put("filter","/transaction/participantContribAdjDetailsReport.jsp");
		forwards.put("page","/transaction/participantContribAdjDetailsReport.jsp");
		forwards.put("print","/transaction/participantContribAdjDetailsReport.jsp");

	}

	private static Logger logger = Logger
            .getLogger(ParticipantTransactionDetailsContribAdjController.class);

    private static final String DEFAULT_SORT_FIELD = TransactionDetailsContribAdjReportData.SORT_FIELD_RISK_CATEGORY;

    private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

    private static final String DOWNLOAD_SUMMARY_COLUMN_HEADINGS = "Last name, First name, SSN, Transaction date, Payroll ending, Total amount, Transaction number";

    private static final String DOWNLOAD_SUMMARY_COLUMN_HEADINGS_WITHOUT_PAYROLL_ENDING_DT = "Last name, First name, SSN, Transaction date, Total amount, Transaction number";

    private static final String DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS = "Transaction date, Contribution date, Total amount, Transaction number"; // defined benefit version

    private static final String DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS_WITHOUT_PAYROLL_ENDING_DT = "Transaction date, Total amount, Transaction number"; // defined benefit version

    private static final String DOWNLOAD_COLUMN_HEADINGS = "Investment Option, Money Type, Amount($), Unit Value, Number Of Units";

    private static final String ADJUSTMENT_DETAILS_REPORT = "AdjustmentDetails";

    private static final String XSLT_FILE_KEY_NAME = "ParticipantTransactionAdjustmentDetailsReport.XSLFile";
	

	/**
	 * Constructor.
	 */
	public ParticipantTransactionDetailsContribAdjController() {
		super(ParticipantTransactionDetailsContribAdjController.class);
	}

    /**
     * Overriding the doCommon() method of AbstractTrnansactionReportAction to set the SSN,
     * FirstName, LastName of the user into the Form.
     */
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		String forward = super.doCommon(reportForm, request,response);
		// If the request has errors, return to page with error message.
		// Otherwise proceed with normal flow
Object obj =null;
Collection<GenericException> errorCollection = null;
List<ObjectError>  errorList =null;
boolean flag=false;
        
        obj = request.getAttribute(BdBaseController.ERROR_KEY);
        if( obj instanceof Collection){
    		flag = true;
    	} else if (obj instanceof BeanPropertyBindingResult){
    		flag = true;
    	}
		//Collection errorKey = (Collection) request.getAttribute(BdBaseController.ERROR_KEY);
		if(!flag){

			ParticipantTransactionDetailsContribAdjForm theForm = (ParticipantTransactionDetailsContribAdjForm) reportForm;

			ParticipantAccountVO participantAccountVO = null;
			ParticipantAccountDetailsVO participantDetailsVO = null;

			BobContext bobContext = getBobContext(request);
			Contract currentContract = bobContext.getCurrentContract();
			int contractNumber = currentContract.getContractNumber();
			String productId = bobContext.getCurrentContract().getProductId();

			BDPrincipal principal = getUserProfile(request).getBDPrincipal();
			participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(
					principal, contractNumber,productId, theForm.getProfileId(), null, false,false);
			participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();

			theForm.setSsn(SSNRender.format(participantDetailsVO.getSsn(),BDConstants.SSN_MASK_CHARS));
			theForm.setFirstName(participantDetailsVO.getFirstName());
			theForm.setLastName(participantDetailsVO.getLastName());

		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}
		return forward;
	}

    /**
	 * The getReportData() method of BaseReportAction class is being overridden,
	 * so that the user cannot see the Resource Limit Exceeded Exception
	 * message. Instead, he will see a Technical Difficulties message.
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
	 * @see BaseReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)  throws SystemException  {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		ParticipantTransactionDetailsContribAdjForm theForm = (ParticipantTransactionDetailsContribAdjForm) form;

		// Retrieve profileId using participantId if the profileId was not provided
		if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {

			if (theForm.getParticipantId() != null && theForm.getParticipantId().length() > 0) {
				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
                // lookup profileId by participant id and contract number
                theForm.setProfileId(asd.getProfileIdByParticipantIdAndContractNumber(theForm
                        .getParticipantId(), Integer.toString(getBobContext(request)
                        .getCurrentContract().getContractNumber())));
			}
			
			if (theForm.getProfileId() == null || theForm.getProfileId().length() == 0) {
				throw new SystemException(null, this.getClass().getName(),
						"populateReportCriteria", "Failed to get the profileId");
			}
		}

		Contract currentContract = getBobContext(request).getCurrentContract();
		
		criteria.addFilter(TransactionDetailsContribAdjReportData.FILTER_CONTRACT_NUMBER, 
				String.valueOf(currentContract.getContractNumber()));
		criteria.addFilter(TransactionDetailsContribAdjReportData.FILTER_PROFILE_ID, 
				theForm.getProfileId());
		criteria.addFilter(
				TransactionDetailsContribAdjReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());
		criteria.addFilter(
				TransactionDetailsContribAdjReportData.APPLICATION_ID,
				TransactionDetailsContribAdjReportData.PS_APPLICATION_ID);

		if (logger.isDebugEnabled()) {
			criteria.toString();
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
     * This method is used to populate the ProfileID, ParticipantID into the Form.
     */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm(reportForm, request);
		ParticipantTransactionDetailsContribAdjForm theForm = (ParticipantTransactionDetailsContribAdjForm) reportForm;

		// Obtain the profileId or if not available the participantId
		String profileId = request.getParameter(ParticipantTransactionDetailsContribAdjForm.PARAMETER_KEY_PROFILE_ID);
		String participantId = request.getParameter(ParticipantTransactionDetailsContribAdjForm.PARAMETER_KEY_PARTICIPANT_ID);
		if (participantId == null) { // try alternate used by some Defined Benefit linked pages
			participantId = request.getParameter(ParticipantTransactionDetailsContribAdjForm.DB_PARAMETER_KEY_PARTICIPANT_ID);
		}
		
		if (StringUtils.trimToNull(profileId) != null) {
			theForm.setProfileId(profileId);
			theForm.setParticipantId(null);
		} else if (StringUtils.trimToNull(participantId) != null) {
			theForm.setParticipantId(participantId);
			theForm.setProfileId(null);
		}
	}
	
	/**
     * Populate the Sort criteria.
     */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
        ParticipantTransactionDetailsContribAdjForm theForm = (ParticipantTransactionDetailsContribAdjForm) form;
		String sortField = theForm.getSortField();
		String sortDirection = theForm.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and monty type description
		criteria.insertSort(TransactionDetailsContribAdjReportData.SORT_FIELD_WEBSRTNO,
								ReportSort.ASC_DIRECTION);
								
		criteria.insertSort(TransactionDetailsContribAdjReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
								ReportSort.ASC_DIRECTION);
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:" + sortField
                    + " and direction: " + sortDirection);
		}
	}	

	/**
     * This method is called when the user clicks on the CSV Button. This method will download the
     * CSV Version of the report.
     */
    @SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		TransactionDetailsContribAdjReportData data = (TransactionDetailsContribAdjReportData) report;
		request.setAttribute("transactionDate", data.getTransactionDate());
		StringBuffer buffer = new StringBuffer();
		ParticipantTransactionDetailsContribAdjForm form = (ParticipantTransactionDetailsContribAdjForm) reportForm;
		
		if (data == null) {
            return buffer.toString().getBytes();
        }
		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
						currentContract.getCompanyName()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		if (currentContract.isDefinedBenefitContract()) {
		    if (data.displayPayrollEndDate()) {
                // summary column heading
                buffer.append(DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS).append(LINE_BREAK);
            } else {
                // summary column heading
                buffer.append(DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS_WITHOUT_PAYROLL_ENDING_DT)
                        .append(LINE_BREAK);
            }
        } else {
            if (data.displayPayrollEndDate()) {
                // summary column heading
                buffer.append(DOWNLOAD_SUMMARY_COLUMN_HEADINGS).append(LINE_BREAK);
            } else {
                // summary column heading
                buffer.append(DOWNLOAD_SUMMARY_COLUMN_HEADINGS_WITHOUT_PAYROLL_ENDING_DT).append(
                        LINE_BREAK);
            }
            buffer.append(form.getLastName()).append(COMMA).append(form.getFirstName()).append(
                    COMMA).append(form.getSsn()).append(COMMA);
        }
							
		buffer	
			.append(DateRender.format(data.getTransactionDate(), RenderConstants.MEDIUM_YMD_SLASHED))
			.append(COMMA);
		if (data.displayPayrollEndDate()) {
            buffer.append(DateRender.format(data.getPayrollEndDate(), 
                    RenderConstants.MEDIUM_YMD_SLASHED)).append(COMMA);
		}
		buffer.append(BDConstants.DOLLAR_SIGN)
			.append(NumberRender.formatByPattern(data.getTotalAmount(), ZERO_AMOUNT_STRING, 
			        BDConstants.AMOUNT_FORMAT))
			.append(COMMA)
			.append(data.getTransactionNumber())
			.append(LINE_BREAK);

		buffer.append(LINE_BREAK);

		// detail table column heading
		buffer
		.append(DOWNLOAD_COLUMN_HEADINGS)
		.append(LINE_BREAK);

        ArrayList<FundGroup> fundGroups = (ArrayList<FundGroup>) report.getDetails();
        
        for (FundGroup fundGroup : fundGroups) {
			buffer
			.append(fundGroup.getGroupName())
			.append(LINE_BREAK);
			Fund funds[] = fundGroup.getFunds();
			
			for (Fund singleFund : funds) {
			    TransactionDetailsFund fund = (TransactionDetailsFund) singleFund;
			    // If the Investment Option is a GIC, then the Number of Units column will show a dash (-) 
			    String numberOfUnits = "";
			    if (fund.isGuaranteedAccount()) {
			    	numberOfUnits = "-";
			    }else{
			    	numberOfUnits = fund.getDisplayPsNumberOfUnits();
			    }
				buffer
				.append(fund.getName())
				.append(COMMA)
				.append(fund.getMoneyTypeDescription())
				.append(COMMA)
				.append(NumberRender.formatByPattern(fund.getAmount(), ZERO_AMOUNT_STRING,
                                        BDConstants.AMOUNT_FORMAT))
				.append(COMMA)
				.append(fund.getDisplayPsUnitValue())
				.append(COMMA)
				.append(numberOfUnits)
				.append(LINE_BREAK);
			}
		}			
	  buffer.append("Total amount").append(COMMA).append(COMMA);
	  buffer.append(data.getTotalAmount()).append(LINE_BREAK);
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	/**
     * This method returns the name of the Report Handler.
     */
	protected String getReportId() {
		return TransactionDetailsContribAdjReportData.REPORT_ID;
	}

	/**
     * This method returns the CSV File Name.
     */
	protected String getReportName() {
		return TransactionDetailsContribAdjReportData.REPORT_NAME;
	}
	
	/**
     * This method returns the default sort field.
     */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
     * This method returns the default sort direction.
     */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	
    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm, 
           ReportData report, HttpServletRequest request) 
           throws ParserConfigurationException {
        
        ParticipantTransactionDetailsContribAdjForm form = (ParticipantTransactionDetailsContribAdjForm) reportForm;
        TransactionDetailsContribAdjReportData data = (TransactionDetailsContribAdjReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();

        // Gets layout page for participantContribAdjDetailsReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.PPT_CONTRIB_ADJ_DETAILS_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.PPT_TXN_DETAILS_CONTRIB_ADJ);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);

        // Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Fund Details - start
            Element fundDetailsElement = doc.createElement(BDPdfConstants.FUND_DETAILS);
            Element fundDetailElement;
            Iterator iterator = data.getDetails().iterator();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                FundGroup theItem = (FundGroup) iterator.next();
                if (theItem != null) {
                    // Sets fund group.
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, theItem
                            .getGroupName());
                    for (Fund fund : theItem.getFunds()) {
                        if (rowCount <= maxRowsinPDF) {
                            // Sets main report.
                            setFundDetailsXMLElements(doc, fundDetailElement, fund);
                            rowCount++;
                        }
                    }
                }
                doc.appendElement(fundDetailsElement, fundDetailElement);   
            }
            String totalAmt = NumberRender.formatByType(data.getTotalAmount(), null,
                    RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
            doc.appendTextNode(fundDetailsElement, BDPdfConstants.TOTAL_AMT, removeParanthesesAndPrefixMinus(totalAmt));
            doc.appendElement(rootElement, fundDetailsElement);
            // Fund Details - end 
        }
        
        if (form.getPdfCapped()) { 
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
     * @param request
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement,
            TransactionDetailsContribAdjReportData data, LayoutPage layoutPageBean,
            ParticipantTransactionDetailsContribAdjForm form, HttpServletRequest request) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        String formattedDate = null;
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        formattedDate = DateRender.formatByPattern(data.getTransactionDate(), null,
                RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_DATE, formattedDate);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER, data
                .getTransactionNumber());

        String totalAmt = NumberRender.formatByType(data.getTotalAmount(), null,
                RenderConstants.CURRENCY_TYPE);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_AMT, removeParanthesesAndPrefixMinus(totalAmt));

        formattedDate = DateRender.formatByPattern(data.getPayrollEndDate(), null,
                RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        if (!(getBobContext(request).getCurrentContract().isDefinedBenefitContract())) {
            StringBuffer pptName = new StringBuffer();
            pptName.append(form.getLastName()).append(COMMA).append(WHITE_SPACE_CHAR).append(
                    form.getFirstName());
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, pptName.toString());

            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, form.getSsn());

            if (data.displayPayrollEndDate()) {
                // If the contract is DB, show "Payroll Ending:" field.
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.PAYROLL_END_DATE, formattedDate);
            }
        } else {
            if (data.displayPayrollEndDate()) {
                // If the contract is not DB, show "Contribution Date:" field.
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.CONTRIB_DATE, formattedDate);
            }
        }
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
        if (txnDetailsFund != null) {
            doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
            doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE, txnDetailsFund
                    .getMoneyTypeDescription());
            String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null,
                    RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
            doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
                    removeParanthesesAndPrefixMinus(fundAmt));
            doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_UNIT_VALUE, txnDetailsFund
                    .getDisplayPsUnitValue());
            doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_NUM_OF_UNITS, removeParanthesesAndPrefixMinus(txnDetailsFund
                    .isGuaranteedAccount()? "-" : txnDetailsFund.getDisplayPsNumberOfUnits()));
        }
        doc.appendElement(fundDetailElement, fundTxnElement);
    }

    /**
     * @See BDPdfAction#getNumberOfRowsInReport() 
     * Each fund in fund group comprises one row in 
     * report table and so this method is overridden and modified.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if (report.getDetails() != null) {
            for (FundGroup theItem : (ArrayList<FundGroup>) report.getDetails()) {
                noOfRows += theItem.getFunds().length;     
            }
        }
        return noOfRows;
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
     * @param request
     * @return The file name used for the downloaded CSV.
     */

    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
    	Date transactionDate=(Date)request.getAttribute("transactionDate");
    	Contract currentContract = getBobContext(request).getCurrentContract();
    	String formattedTransactionDate = DateRender.formatByPattern(transactionDate,
    			null,
                RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED).replace(
                BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
        String csvFileName=ADJUSTMENT_DETAILS_REPORT
        					+BDConstants.HYPHON_SYMBOL
        					+currentContract.getContractNumber()
        					+BDConstants.HYPHON_SYMBOL
        					+formattedTransactionDate
        					+CSV_EXTENSION;
        return csvFileName;
    }
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/" , method = RequestMethod.GET) 
	public String doDefault (@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
				doCommon((BaseReportForm)form, request, null);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
				
					
				
				
			}
		}
		
		  forward=super.doDefault( form, request, response);
		 return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward); 
	}
    
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/" ,params="task=filter", method =  {RequestMethod.GET}) 
	public String doFilter(@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		 forward=super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward); 
	}
    
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/" ,params="task=page", method = RequestMethod.GET) 
	public String doPage(@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		 forward=super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward); 
	}
    
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/" ,params="task=print", method = RequestMethod.GET) 
	public String doPrint(@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		 forward=super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
    
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/" ,params="task=download", method = RequestMethod.GET) 
	public String doDownload(@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		 forward=super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
    
    @RequestMapping(value ="/transaction/pptContribAdjDetailsReport/", params = {"task=printPDF"}, method = {RequestMethod.GET})
    public String doPrintPDF(@Valid @ModelAttribute("participantContribAdjDetailsForm") ParticipantTransactionDetailsContribAdjForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		
		 forward=super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
    
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
