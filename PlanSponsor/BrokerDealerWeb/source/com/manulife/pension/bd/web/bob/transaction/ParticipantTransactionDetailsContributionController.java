package com.manulife.pension.bd.web.bob.transaction;

import static com.manulife.pension.platform.web.CommonConstants.SLASH_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.ContractServiceDelegate;
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
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsContributionReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action class handles the participant transaction details contribution page.
 * 
 * @author harlomte
 */
@Controller
@RequestMapping( value ="/bob")
@SessionAttributes({"participantContributionDetailsForm"})

public class ParticipantTransactionDetailsContributionController 
		extends	AbstractTransactionReportController {

	@ModelAttribute("participantContributionDetailsForm")
	public ParticipantTransactionDetailsContributionForm populateForm() {
		return new ParticipantTransactionDetailsContributionForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/transaction/participantContributionDetailsReport.jsp");
		forwards.put("default","/transaction/participantContributionDetailsReport.jsp");
		forwards.put("filter","/transaction/participantContributionDetailsReport.jsp");
		forwards.put("page","/transaction/participantContributionDetailsReport.jsp");
		forwards.put("print","/transaction/participantContributionDetailsReport.jsp");

	}
	
	private static Logger logger = Logger.getLogger(ParticipantTransactionDetailsContributionController.class);
	
	private static final String DEFAULT_SORT_FIELD = TransactionDetailsContributionReportData.SORT_FIELD_RISK_CATEGORY;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

	private static final String DOWNLOAD_SUMMARY_COLUMN_HEADINGS = 
			"Name, SSN, Transaction Date, Payroll Ending";
	private static final String DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS = 
		"Transaction Date, Contribution Date";
	
	private static final String DOWNLOAD_COLUMN_HEADINGS = 
			"Investment Option, Money Type, Amount ($), Unit Value, Number Of Units";
	
	private static final String CONTRIBUTION_DETAILS="ContributionDetails";
	
	private static final String XSLT_FILE_KEY_NAME = "ParticipantTransactionDetailsReport.XSLFile";
	
	public static final String NUMBER_OF_UNITS_FORMAT = "########0.000000";

	
	/**
	 * Constructor.
	 */
	public ParticipantTransactionDetailsContributionController() {
		super(ParticipantTransactionDetailsContributionController.class);
	}

	/**
     * This method will retrieve the report data to be shown on the JSP page.
     */
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		String forward = super.doCommon(reportForm, request, response);

		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) reportForm;
		
		ParticipantAccountVO participantAccountVO = null;
		ParticipantAccountDetailsVO participantDetailsVO = null;

		Contract currentContract = getBobContext(request).getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		String productId = currentContract.getProductId();
		
		BDPrincipal principal = getUserProfile(request).getBDPrincipal();
		
		String profileId = theForm.getProfileId();
		if (currentContract.isDefinedBenefitContract()) {
			// page is navigated to DIRECTLY from transaction history page, need to lookup profileId
			// since DB only has one(fake)participant per contract, we will just look it up via contact #
			ContractServiceDelegate csDelegate = ContractServiceDelegate.getInstance();
			ParticipantListVO pListVO = csDelegate.getParticipantList(contractNumber, null);
			if (pListVO != null) {
                ParticipantVO participantVO = (ParticipantVO) pListVO.getParticipants().iterator()
                        .next(); // only one in DB
                profileId = participantVO.getProfileId();
            }
		}
		participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(principal, contractNumber,
				productId, profileId, null, false, false);
		participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();
		
		theForm.setSsn(SSNRender.format(participantDetailsVO.getSsn(), BDConstants.SSN_MASK_CHARS));
		theForm.setFirstName(participantDetailsVO.getFirstName()); 
		theForm.setLastName(participantDetailsVO.getLastName()); 
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon ");
		}
		return forward;
	}

	/**
	 * @see BaseReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) form;
        Contract currentContract = getBobContext(request).getCurrentContract();

		// Retrieve profileId using participantId if the profileId was not provided
        if (StringUtils.isEmpty(theForm.getProfileId())) {
			
			if (StringUtils.trimToNull(theForm.getParticipantId()) != null) {
				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();

                // common log 78460 lookup profileId by participant id and contract number
                theForm.setProfileId(asd.getProfileIdByParticipantIdAndContractNumber(
                        theForm.getParticipantId(), Integer.toString(currentContract.getContractNumber())));
			}
			
			if (StringUtils.trimToNull(theForm.getProfileId()) == null) {
				throw new SystemException(null, this.getClass().getName(),
						"populateReportCriteria", "Failed to get the profileId");
			}
		}
		
		criteria.addFilter(TransactionDetailsContributionReportData.FILTER_CONTRACT_NUMBER, 
				String.valueOf(currentContract.getContractNumber()));
		criteria.addFilter(TransactionDetailsContributionReportData.FILTER_PROFILE_ID, 
				theForm.getProfileId());
		criteria.addFilter(
				TransactionDetailsContributionReportData.FILTER_TRANSACTION_NUMBER,
				theForm.getTransactionNumber());
		criteria.addFilter(
				TransactionDetailsContributionReportData.APPLICATION_ID,
				TransactionDetailsContributionReportData.PS_APPLICATION_ID);

		if (logger.isDebugEnabled()) {
			criteria.toString();
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
     * This method will populate the report action form with details such as profile ID, participant
     * ID.
     */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) reportForm;

		// Obtain the profileId or if not available the participantId
		String profileId = request.getParameter(ParticipantTransactionDetailsContributionForm.PARAMETER_KEY_PROFILE_ID);
		String participantId = request.getParameter(ParticipantTransactionDetailsContributionForm.PARAMETER_KEY_PARTICIPANT_ID);

		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		
		if (currentContract.isDefinedBenefitContract()) {
			// page is navigated to DIRECTLY from transaction history page, need to lookup profileId
			// since DB only has one(fake)participant per contract, we will just look it up via contact #
			ContractServiceDelegate csDelegate = ContractServiceDelegate.getInstance();
			try {
				ParticipantListVO pListVO = csDelegate.getParticipantList(contractNumber, null);
				if (pListVO != null) {
                    ParticipantVO participantVO = (ParticipantVO) pListVO.getParticipants()
                            .iterator().next(); // only one in DB
                    profileId = participantVO.getProfileId();
                }
			} catch(SystemException se) {
				// log (sends general tech error message to page)
				logger.error("Problem attempting to find profileId in DefinedBenefit contract case", se);
			}
		}		

		if (profileId != null && profileId.length() > 0) {
			theForm.setProfileId(profileId);
			theForm.setParticipantId(null);
		} else if (participantId != null && participantId.length() >0) {
			theForm.setParticipantId(participantId);
			theForm.setProfileId(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportForm");
		}
		
	}
	
	/**
     * This method is used to populate the sort criteria.
     */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		// default sort is risk category			
		ParticipantTransactionDetailsContributionForm theForm = (ParticipantTransactionDetailsContributionForm) form;
		String sortField = theForm.getSortField();
		String sortDirection = theForm.getSortDirection();
		
		criteria.insertSort(sortField, sortDirection);
		
		// add additional sort criteria websrtno and money type description
		criteria.insertSort(TransactionDetailsContributionReportData.SORT_FIELD_WEBSRTNO,
								ReportSort.ASC_DIRECTION);
								
		criteria.insertSort(TransactionDetailsContributionReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
								ReportSort.ASC_DIRECTION);
								
		if (logger.isDebugEnabled()) {
			logger.debug("populateSortCriteria: inserting sort with field:"+sortField+" and direction: " + sortDirection);
		}
	}	

	/**
     * This method will create the CSV version of the report.
     */
	@SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		TransactionDetailsContributionReportData data = (TransactionDetailsContributionReportData) report;
		StringBuffer buffer = new StringBuffer();
		ParticipantTransactionDetailsContributionForm form = (ParticipantTransactionDetailsContributionForm) reportForm;
		
		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append(BDConstants.CSV_CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
						currentContract.getCompanyName()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// summary column heading
		if (currentContract.isDefinedBenefitContract()) {
			buffer.append(DB_DOWNLOAD_SUMMARY_COLUMN_HEADINGS);
		} else {
			buffer.append(DOWNLOAD_SUMMARY_COLUMN_HEADINGS);
		}

		if (data.getMoneySourceDescription().length() > 0){
			buffer
			.append(COMMA)			
			.append(BDConstants.CSV_CONTRIBUTION_TYPE);
		}
		
		if (data.isHasEmployeeContribution()){
			buffer
			.append(COMMA)			
			.append(BDConstants.CSV_EMPLOYEE_CONTRIB);
		}
		if (data.isHasEmployerContribution()){
			buffer
			.append(COMMA)			
			.append(BDConstants.CSV_EMPLOYER_CONTRIB);
		}
		if (data.isHasEmployeeContribution() && data.isHasEmployerContribution() ){
			buffer
			.append(COMMA)			
			.append(BDConstants.CSV_TOTAL_AMT);
		}
		buffer
		.append(COMMA)	
		.append(BDConstants.CSV_TXN_NUM)
		.append(LINE_BREAK);
		
		// summary details
		if (currentContract.isDefinedBenefitContract() == false) {
		buffer
			.append(escapeField(form.getLastName() + ", "
					+ form.getFirstName()))
			.append(COMMA)
			.append(form.getSsn())
				.append(COMMA);
		}
		
		buffer
			.append(DateRender.format(data.getTransactionDate(), RenderConstants.MEDIUM_YMD_SLASHED))
			.append(COMMA)
			.append(DateRender.format(data.getPayrollEndDate(), RenderConstants.MEDIUM_YMD_SLASHED))
			;
		
		if (data.getMoneySourceDescription().length() > 0){
			buffer
			.append(COMMA)			
			.append(data.getMoneySourceDescription());
		}

		if (data.isHasEmployeeContribution()){
			buffer
			.append(COMMA)
			.append(BDConstants.DOLLAR_SIGN).append(
                    NumberRender.formatByPattern(data.getContributionEEAmount(),
                            ZERO_AMOUNT_STRING, BDConstants.AMOUNT_FORMAT))
			;
		}
		if (data.isHasEmployerContribution()){
			buffer
			.append(COMMA)
			.append(BDConstants.DOLLAR_SIGN).append(
                    NumberRender.formatByPattern(data.getContributionERAmount(),
                            ZERO_AMOUNT_STRING, BDConstants.AMOUNT_FORMAT))
			;
		}
		if (data.isHasEmployeeContribution() && data.isHasEmployerContribution()){
			buffer
			.append(COMMA)
			.append(BDConstants.DOLLAR_SIGN).append(
                    NumberRender.formatByPattern(data.getTotalContribution(), ZERO_AMOUNT_STRING,
                            BDConstants.AMOUNT_FORMAT))
			;
		}
		buffer
			.append(COMMA)
			.append(data.getTransactionNumber())
			.append(LINE_BREAK);

		buffer.append(LINE_BREAK);

		// detail table column heading
		buffer
		.append(DOWNLOAD_COLUMN_HEADINGS)
		.append(LINE_BREAK);

		// individual line items
		Iterator it1 = report.getDetails().iterator();	
		while (it1.hasNext()) {
			FundGroup fundGroup = (FundGroup) it1.next();
			buffer
			.append(fundGroup.getGroupName())
			.append(LINE_BREAK);
			Fund funds[] = fundGroup.getFunds();
			for (int i=0; i<funds.length; i++){
				TransactionDetailsFund fund = (TransactionDetailsFund) funds[i];
				buffer
				.append(fund.getName())
				.append(COMMA)
				.append(fund.getMoneyTypeDescription())
				.append(COMMA)
				.append(
						removeParanthesesAndPrefixMinus(NumberRender.formatByPattern(fund.getAmount(), ZERO_AMOUNT_STRING,
                                BDConstants.AMOUNT_FORMAT)))
				.append(COMMA);
				if (fund.getUnitValue().doubleValue() != 0) {
					buffer.append(removeParanthesesAndPrefixMinus(NumberRender.formatByPattern(fund.getUnitValue(), BDConstants.NO_RULE,
	                        BDConstants.AMOUNT_FORMAT)));
				} else {
					buffer.append(BDConstants.NO_RULE);
				}
				buffer.append(COMMA);
				if (fund.getNumberOfUnits().doubleValue() != 0) {
					buffer.append(removeParanthesesAndPrefixMinus(NumberRender.formatByPattern(fund.getNumberOfUnits(), BDConstants.NO_RULE,
	                        NUMBER_OF_UNITS_FORMAT,6,0)));
				} else {
					buffer.append(BDConstants.NO_RULE);
				}
				buffer.append(LINE_BREAK);
			}
		}
		buffer.append(LINE_BREAK);
		buffer.append("Total Amount:");
			String totalAmt = NumberRender.formatByPattern(data.getTotalContribution(), null,
					BDConstants.AMOUNT_FORMAT);
			buffer.append(COMMA).append(COMMA);
			buffer.append(totalAmt);
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	protected String getReportId() {
		return TransactionDetailsContributionReportData.REPORT_ID;
	}

	protected String getReportName() {
		return TransactionDetailsContributionReportData.REPORT_NAME;
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
       
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

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
	    
		ParticipantTransactionDetailsContributionForm form = (ParticipantTransactionDetailsContributionForm) reportForm;
		TransactionDetailsContributionReportData data = (TransactionDetailsContributionReportData) report;
		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		// Gets layout page for participantContributionDetailsReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.PPT_TXN_DETAILS_CONTRIB_PATH, request);

		Element rootElement = doc.createRootElement(BDPdfConstants.PPT_TXN_DETAILS_CONTRIB);

		// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		// Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form, request);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Fund Details - start
            Element fundDetailsElement = doc.createElement(BDPdfConstants.FUND_DETAILS);
            Element fundDetailElement;
            Iterator iterator = report.getDetails().iterator();
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {

                fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                FundGroup theItem = (FundGroup) iterator.next();
                if (theItem != null) {
                    // Sets fund group.
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_GROUP, theItem.getGroupName());
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
            TransactionDetailsContributionReportData data, LayoutPage layoutPageBean,
            ParticipantTransactionDetailsContributionForm form, HttpServletRequest request) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        String formattedDate = DateRender.formatByPattern(data.getTransactionDate(), null,
                RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_DATE, formattedDate);
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TXN_NUMBER, data
                .getTransactionNumber());

        formattedDate = DateRender.formatByPattern(data.getPayrollEndDate(), null,
                RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED);

        if (!(getBobContext(request).getCurrentContract().isDefinedBenefitContract())) {
            StringBuffer pptName = new StringBuffer();
            pptName.append(form.getLastName()).append(COMMA).append(WHITE_SPACE_CHAR).append(
                    form.getFirstName());
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_NAME, pptName.toString());

            doc.appendTextNode(summaryInfoElement, BDPdfConstants.PPT_SSN, form.getSsn());

            // If the contract is DB, show "Payroll Ending:" field.
            if (data.isHasPayrollEndDate()) {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.PAYROLL_END_DATE,
                        formattedDate);
            }
        } else {
            // If the contract is not DB, show "Contribution Date:" field.
            if (data.isHasPayrollEndDate()) {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.CONTRIB_DATE,
                        formattedDate);
            }
        }
        if (data.getMoneySourceDescription().length() > 0) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.CONTRIB_TYPE, data
                    .getMoneySourceDescription());
        }
        if (data.isHasEmployeeContribution()) {
            // If data has employee contribution, show employee contribution amt.
            String contribEEAmt = NumberRender.formatByType(data.getContributionEEAmount(),
                    null, RenderConstants.CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.CONTRIB_EE_AMT, contribEEAmt);
        } 
        if (data.isHasEmployerContribution()) {
            // If data has employer contribution, show employer contribution amt.
            String contribERAmt = NumberRender.formatByType(data.getContributionERAmount(),
                    null, RenderConstants.CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.CONTRIB_ER_AMT, contribERAmt);
        }

        String totalAmt = NumberRender.formatByType(data.getTotalContribution(), null,
                RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
        doc.appendTextNode(rootElement, BDPdfConstants.TXN_TOTAL_AMT, totalAmt);
        if (data.isHasEmployeeContribution() && data.isHasEmployerContribution()) {
            // If data has both employee and employer contributions, show total amt.
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_AMT, totalAmt);
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
        doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, txnDetailsFund.getName());
        doc.appendTextNode(fundTxnElement, BDPdfConstants.MONEY_TYPE, txnDetailsFund
                .getMoneyTypeDescription());
        String fundAmt = NumberRender.formatByType(txnDetailsFund.getAmount(), null,
                RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
        doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_AMT,
                removeParanthesesAndPrefixMinus(fundAmt));
        if (StringUtils.isNotBlank(txnDetailsFund.getDisplayPsUnitValue())) {
	        doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_UNIT_VALUE, 
	        		removeParanthesesAndPrefixMinus(txnDetailsFund.getDisplayPsUnitValue()));
        } else {
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_UNIT_VALUE, BDConstants.NO_RULE);
        }
        if (StringUtils.isNotBlank(txnDetailsFund.getDisplayPsNumberOfUnits())) {
	        doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_NUM_OF_UNITS, 
	        		removeParanthesesAndPrefixMinus(txnDetailsFund.getDisplayPsNumberOfUnits()));
        } else {
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.PS_NUM_OF_UNITS, BDConstants.NO_RULE);
        }
        if (txnDetailsFund.isGuaranteedAccount()) {
            doc.appendTextNode(fundTxnElement, BDPdfConstants.IS_GUARANTEED_ACCOUNT, null);
        }
        doc.appendElement(fundDetailElement, fundTxnElement);
    }
     
    /**
     * @See BaseReportAction#getNumberOfRowsInReport() 
     * Each fund in fund group comprises one row in report table and 
     * so this method is overridden and modified.
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
     * This method is used to frame the file name used for the downloaded CSV.
     * 
     * @param BaseReportForm
     * @param HttpServletRequest
     * @return The file name used for the downloaded CSV.
     */

    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
    	Contract currentContract = getBobContext(request).getCurrentContract();
    	TransactionDetailsContributionReportData report = (TransactionDetailsContributionReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
    	String formattedTransactionDate = DateRender.format(report.getTransactionDate(),
                RenderConstants.MEDIUM_MDY_SLASHED).replace(SLASH_SYMBOL, SPACE_SYMBOL);
        String csvFileName=CONTRIBUTION_DETAILS
                     + BDConstants.HYPHON_SYMBOL
                     + currentContract.getContractNumber()
                     + BDConstants.HYPHON_SYMBOL
                     + formattedTransactionDate
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
	 * don't want excel to think the , is the next field
	 * 
	 * @param String
	 *            field
	 * @return String field
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append(BDConstants.DOUBLE_QUOTES).append(field)
					.append(BDConstants.DOUBLE_QUOTES);
			return newField.toString();
		} else {
			return field;
		}
	}  

	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}
	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/transaction/pptContributionDetailsReport/", params = {"task=printPDF"}, method = {RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("participantContributionDetailsForm") ParticipantTransactionDetailsContributionForm form,
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
				try {
					doCommon((BaseReportForm)form, request, null);
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPrintPDF(form, request, response);
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
