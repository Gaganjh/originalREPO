package com.manulife.pension.bd.web.bob.participant;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;
import static com.manulife.pension.bd.web.BDConstants.DB_CSV_NAME;
import static com.manulife.pension.bd.web.BDConstants.NOT_APPLICABLE;
import static com.manulife.pension.platform.web.CommonConstants.DOLLAR_SIGN;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.PERCENTAGE_SIGN;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_AGGRESIVE;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_CONSERVATIVE;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_GROWTH;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_GROWTH_INCOME;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_INCOME;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_LIFECYCLE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.time.FastDateFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountAssetsByRiskVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Custom version of ParticipantAccountCommonAction for DefinedBenefit support. DefinedBenefit needs
 * to hide/rename some fields on the screen/down load report, so we have a custom version. This is
 * in support of the jsp pages DefinedBenefitAccount*.jsp
 * 
 * 
 * @author Siby Thomas
 * 
 */
public abstract class DefinedBenefitAccountCommonController extends ParticipantAccountCommonController {

	private static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MMddyyyy");

    /**
     * Constructor.
     * 
     * @param clazz Class
     */
    @SuppressWarnings("unchecked")
    public DefinedBenefitAccountCommonController(Class clazz) {
        super(clazz);
    }

    /**
     * Method that produces the first(top) part of the down load data, specific screens implement
     * the populateDetailedDownloadData method.
     * 
     * Based on populateDownloadData in ParticipantAccountCommonAction
     * 
     * @see ParticipantAccountCommonController#populateDownloadData()
     * 
     */
    protected void populateDownloadData(ParticipantAccountVO participantAccountVO,
            ParticipantAccountForm form, HttpServletRequest request, HttpServletResponse response)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadData");
        }

        Contract currentContract = getBobContext(request).getCurrentContract();
        StringBuffer buff = new StringBuffer("");

        buff.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);
        Date asOfDate = new Date(Long.parseLong(form.getSelectedAsOfDate()));

        buff.append("As of,").append(dateFormatter.format(asOfDate)).append(LINE_BREAK).append(
                LINE_BREAK);

        buff.append("Name,");
        buff.append(LINE_BREAK);

        ParticipantAccountDetailsVO detailsVO = participantAccountVO
                .getParticipantAccountDetailsVO();

        buff.append(QUOTE).append(detailsVO.getFirstName() + " ").append(detailsVO.getLastName())
                .append(QUOTE).append(COMMA);
        //buff.append(detailsVO.getEmployeeStatus()).append(COMMA);

        buff.append(LINE_BREAK).append(LINE_BREAK).append(LINE_BREAK);

        buff.append("Total assets,");
        buff.append("$");
        buff.append(detailsVO.getTotalAssets()).append(
                LINE_BREAK);
        buff.append("Allocated assets,");
        buff.append("$");
        buff.append(detailsVO.getAllocatedAssets());

        if (form.isAsOfDateCurrent()) {
            buff.append(LINE_BREAK);
            buff.append("Last contribution date,");
            if (detailsVO.getLastContributionDate() != null) {
                buff.append(dateFormatter.format(detailsVO.getLastContributionDate())).append(
                        LINE_BREAK);
            } else {
                buff.append(NOT_APPLICABLE).append(LINE_BREAK);
            }
        }

        buff.append(LINE_BREAK);
        ParticipantAccountAssetsByRiskVO assetsByRiskVO = participantAccountVO.getAssetsByRisk();

        if (assetsByRiskVO != null) {
            buff.append(LINE_BREAK).append(LINE_BREAK);

            if (currentContract.getHasLifecycle()
                    || assetsByRiskVO.getTotalAssetsByRisk(RISK_LIFECYCLE) > 0) {
                buff.append("Target Date,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(RISK_LIFECYCLE)).append(COMMA).append(
                        Math.round(HUNDRED
                                * assetsByRiskVO.getPercentageTotalByRisk(RISK_LIFECYCLE))).append(
                        PERCENTAGE_SIGN).append(LINE_BREAK);
            }

            buff.append("Aggressive,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_AGGRESIVE)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_AGGRESIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_GROWTH)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_GROWTH)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth & income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_GROWTH_INCOME)).append(COMMA).append(
                    Math.round(HUNDRED
                            * assetsByRiskVO.getPercentageTotalByRisk(RISK_GROWTH_INCOME))).append(
                    PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_INCOME)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_INCOME)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Conservative,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_CONSERVATIVE)).append(COMMA).append(
                    Math
                            .round(HUNDRED
                                    * assetsByRiskVO.getPercentageTotalByRisk(RISK_CONSERVATIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append(LINE_BREAK);
        }

        buff.append(populateDetailedDownloadData(participantAccountVO, form,request));

        String fileName = getFileName(form);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setContentType(BDReportController.CSV_TEXT);
        response.setHeader(BDReportController.CONTENT_DISPOSITION_TEXT, BDReportController.ATTACHMENT_TEXT
                + fileName);

        try {
            response.getOutputStream().println(buff.toString());
        } catch (IOException ioe) {
            SystemException se = new SystemException(ioe,
                    "populateDownloadData caught Exception writing csv data to ouput stream.");
            throw se;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDownloadData");
        }
    }

    /**
     * We need to jump through some hoops here to find the one participant's profile ID for a given
     * contract. There should be a one to one relationship between the contract and (one
     * fake)participant.
     * 
     * @param form Form
     * @param request HttpServletRequest
     * 
     * @throws SystemException
     * @throws ReportServiceException
     */
    public void findProfileIdByContract(ActionForm form, HttpServletRequest request)
            throws SystemException, ReportServiceException {

        /*
         * run a query simulating the ACCOUNT SUMMARY page
         */
        ReportCriteria criteria = new ReportCriteria(ParticipantSummaryReportData.REPORT_ID);
        criteria.insertSort(ParticipantSummaryReportData.DEFAULT_SORT, ReportSort.ASC_DIRECTION);
        criteria.setPageNumber(1);
        criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);

        Contract currentContract = getBobContext(request).getCurrentContract();
        criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));
        ReportServiceDelegate service = ReportServiceDelegate.getInstance();
        ReportData bean = service.getReportData(criteria);
       
        ParticipantSummaryDetails theItem = (ParticipantSummaryDetails) bean.getDetails()
                .iterator().next();
        String profileId = theItem.getProfileId();

        ParticipantAccountForm accountForm = (ParticipantAccountForm) form;
        accountForm.setIsDefinedBenefitContract(true);
        accountForm.setProfileId(profileId);
    }

    /**
     * @see ParticipantAccountCommonController#preExecute(ActionMapping, ActionForm, HttpServletRequest,
     *      HttpServletResponse)
     */
    @Override
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {
        
        super.preExecute( form, request, response);

        Contract contract = getBobContext(request).getCurrentContract();
        ParticipantAccountForm reportForm = (ParticipantAccountForm) form;
        reportForm.setContractNumber(contract.getContractNumber());
        if (contract.isDiscontinued()) {
           // return mapping.findForward(BOB_PAGE_FORWARD);
        	return BOB_PAGE_FORWARD;
        }
        try {
            findProfileIdByContract(form, request);
        } catch (ReportServiceException e) {
            /*
             * this is how ReportAction handles this exception
             */
            logger.error("Received a Report service exception: ", e);
            List<GenericException> errors = new ArrayList<GenericException>();
            errors.add(new GenericException(Integer.parseInt(e.getErrorCode())));
            setErrorsInRequest(request, errors);
            //TODO
            //return mapping.getInputForward();
            return "input";
        }
        return null;
    }

    /**
     * @See BDPdfAction#prepareXMLFromReport()
     */
    @Override
    public Document prepareXMLFromReport(BaseForm reportForm, Object report, ParticipantAccountVO participantAccountVO,
            HttpServletRequest request) throws ParserConfigurationException {
       
        PDFDocument doc = new PDFDocument();
        ParticipantAccountForm accountForm = (ParticipantAccountForm) reportForm;
        Element rootElement = doc.createRootElement(BDPdfConstants.DB_ACCOUNT);
        ParticipantAccountDetailsVO detailsVO = participantAccountVO.getParticipantAccountDetailsVO();
        ParticipantAccountAssetsByRiskVO assetsByRiskVO = participantAccountVO.getAssetsByRisk();
        prepareXMLFromReportCommon(accountForm, detailsVO, assetsByRiskVO, request, rootElement, doc);
        int msgNum = 0;
        String message;
        Element infoMessagesElement = doc.createElement(BDPdfConstants.INFO_MESSAGES);
        if (!accountForm.isAsOfDateCurrent()) {
            Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            message = ContentHelper.getContentTextWithParamsSubstitution(BDContentConstants.MISCELLANEOUS_DB_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE, ContentTypeManager.instance().MESSAGE, null,
                      DateRender.formatByPattern(getBobContext(request).getCurrentContract().getContractDates().getAsOfDate(), null, RenderConstants.MEDIUM_MDY_SLASHED));
            doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String.valueOf(++msgNum));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }

        if (!accountForm.getHasInvestments()) {
            Element messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            message = ContentHelper.getContentText(BDContentConstants.MESSAGE_DB_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS, ContentTypeManager.instance().MESSAGE, null);
            doc.appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String.valueOf(++msgNum));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }
        doc.appendElement(rootElement, infoMessagesElement);
        prepareDetailedXMLFromReport(participantAccountVO, accountForm, doc, rootElement, request);
        return doc.getDocument();

    }

    /**
     * Generates the file name for the down load report (CSV file)
     * 
     * @param form ParticipantAccountForm
     * 
     * @return String The file name
     */
    @Override
    protected String getFileName(ParticipantAccountForm form) {

        String fileName = null;
        String dateString = null;
        Long selectedAsOfDate = new Long(form.getSelectedAsOfDate());
        dateString = DATE_FORMATTER.format(new Date(selectedAsOfDate));
        fileName = DB_CSV_NAME + HYPHON_SYMBOL + getTabName() + HYPHON_SYMBOL
                + form.getContractNumber() + HYPHON_SYMBOL + dateString + CSV_EXTENSION;
        return fileName;
    }

    /**
     * @See BDPdfAction#getXSLTFileName()
     */
    protected String getXSLTFileName() {
        String fileName = null;
        fileName = BDConstants.DB_CSV_NAME + CommonConstants.HYPHON_SYMBOL + getTabName()
                + XSL_EXTENSION;
        return fileName;
    }
    
    /**
	 /** This code has been changed and added  to 
	 /	Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@SuppressWarnings("rawtypes")
	public Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		Collection penErrors = FrwValidator.doValidatePenTestAction(form, request, CommonConstants.INPUT);
		
		if (penErrors != null && penErrors.size() > 0) {
			request.setAttribute("penErrors", true);
		   	ParticipantAccountVO participantAccountVO = (ParticipantAccountVO) request.getSession().getAttribute("account");
		       if(participantAccountVO != null){
		       	request.setAttribute("account", participantAccountVO);
		       	request.setAttribute("details", participantAccountVO.getParticipantAccountDetailsVO());
		       	request.setAttribute("assets", participantAccountVO.getAssetsByRisk());
		           request.setAttribute("organizedFunds", participantAccountVO.getOrganizedParticipantFunds());
		       }
		       if(pieChartBean != null){
		          	request.setAttribute("pieChartBean", pieChartBean);
		          }
			return penErrors;
		}
		return super.doValidate( form, request);
	}
}
