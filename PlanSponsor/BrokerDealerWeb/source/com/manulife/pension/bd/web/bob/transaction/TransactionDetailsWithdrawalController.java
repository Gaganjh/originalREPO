package com.manulife.pension.bd.web.bob.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.TransactionDetailsWithdrawalForm;
import com.manulife.pension.platform.web.report.util.WithdrawalDetailsUtility;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.exception.ResourceLimitExceededException;
import com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalAfterTaxInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalGeneralInfoVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
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
 * Action class for Completed Withdrawal Details page
 * 
 * @author Tamilarasu Krishnamoorthy
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"transactionDetailsWithdrawalForm"})

public class TransactionDetailsWithdrawalController extends BOBReportController {
	@ModelAttribute("transactionDetailsWithdrawalForm") 
	public TransactionDetailsWithdrawalForm populateForm() 
	{
		return new TransactionDetailsWithdrawalForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/transaction/withdrawalTransactionReport.jsp");
		forwards.put("default","/transaction/withdrawalTransactionReport.jsp");
		forwards.put("print","/transaction/withdrawalTransactionReport.jsp");
	}


	/**
	 * XSLT File name
	 */
	private static final String XSLT_FILE_KEY_NAME = "WithdrawalTransactionDetailsReport.XSLFile";

	private static final String WITHRAWAL_DETAILS = "Withdrawal";

	/**
	 * Default Constructor
	 */
	public TransactionDetailsWithdrawalController() {
		super(TransactionDetailsWithdrawalController.class);
	}

	/**
	 * Get the Default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return null;
	}

	/**
	 * Get the Default sort Direction.
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * Get the report Id for reportCriteria.
	 */
	protected String getReportId() {
		return WithdrawalDetailsUtility.getReportId();
	}

	/**
	 * Get the Report Name
	 */
	protected String getReportName() {
		return WithdrawalDetailsUtility.getReportName();
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * This method will retrieve the data from APOLLO and 1. Sets the value for
	 * TransactionDetailsWithdrawalForm from reportData 2. Checks for
	 * correctionIndicator to display error message. 3. And sets reportData and
	 * formBean in request.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 */
	 protected String doCommon(
	            BaseReportForm reportForm, HttpServletRequest request,
	            HttpServletResponse response) throws
	            SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		List<GenericException> errors = new ArrayList<GenericException>();
		String forward = null;

		try {
			forward = super.doCommon( reportForm, request, response);
			WithdrawalDetailsUtility.doCommon( reportForm, request,
					response, BDConstants.BD_APPLICATION_ID);
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, e);

			// Show user friendly message.

			errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors);
			forward = forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}

		return forward;
	}

	/**
	 * The getReportData() method of BaseReportAction class is being overridden,
	 * so that the user cannot see the Resource Limit Exceeded Exception
	 * message. Instead, he will see a Technical Difficulties message
	 */
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportData reportData = null;

		try {
			reportData = super.getReportData(reportId, reportCriteria, request);
		} catch (ResourceLimitExceededException e) {
			logger.error("Received a ResourceLimitExceededException: ", e);
			throw new SystemException(e, "ResourceLimitExceededException "
					+ "occurred. Showing it as TECHNICAL_DIFFICULTIES "
					+ "to the user.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> getReportData");
		}

		return reportData;
	}

	/**
	 * Download complete withdrawal information for the withdrawal transaction
	 * with respect to transactionNumber and participantId.
	 * 
	 * Note: PSW & FRW Common download functionality are coded in
	 * withdrawalDetailsUtility class
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}
		byte[] downloadData = null;
		String bodyHeader = "";

		List<String> withdrawalFieldLabels = new ArrayList<String>();
		// Withdrawal Summary section label
		withdrawalFieldLabels.add(0, "Payment Amount");
		withdrawalFieldLabels.add(1, "Transaction Type");
		withdrawalFieldLabels.add(2, "Type of Withdrawal");
		withdrawalFieldLabels.add(3, "Name");
		withdrawalFieldLabels.add(4, "SSN");
		withdrawalFieldLabels.add(5, "Withdrawal Date");
		withdrawalFieldLabels.add(6, "Transaction Number");
		// Payee Payment section label
		withdrawalFieldLabels.add(7, "Payment To");
		withdrawalFieldLabels.add(8, "Payment Method");
		withdrawalFieldLabels.add(9, "Bank Account");
		withdrawalFieldLabels.add(10, "Payee Address");
		withdrawalFieldLabels.add(11, "Bank Details");
		withdrawalFieldLabels.add(12, "Bank / Branch Name");
		withdrawalFieldLabels.add(13, "ABA / Routing Number");
		withdrawalFieldLabels.add(14, "Account Number");
		withdrawalFieldLabels.add(15, "Credit Party Name");
		// Withdrawal Detail information section label
		withdrawalFieldLabels.add(16, "Money Type");
		withdrawalFieldLabels.add(17, "Withdrawal Amount($)");
		withdrawalFieldLabels.add(18, "Account Balance($)");
		withdrawalFieldLabels.add(19, "Vesting(%)");
		withdrawalFieldLabels.add(20, "Available Amount($)");
		withdrawalFieldLabels.add(21, "Total Withdrawal Amount");
		withdrawalFieldLabels.add(22, "Total Available Amount");
		withdrawalFieldLabels.add(23, "Market Value Adjustment(MVA)");
		withdrawalFieldLabels.add(24, "Funds On Deposit Interest");
		withdrawalFieldLabels.add(25, "Taxable Amount");
		withdrawalFieldLabels.add(26, "State Tax");
		withdrawalFieldLabels.add(27, "Federal Tax");
		withdrawalFieldLabels.add(28, "Taxable Amount - ROTH");
		withdrawalFieldLabels.add(29, "State Tax - ROTH");
		withdrawalFieldLabels.add(30, "Federal Tax - ROTH");
		withdrawalFieldLabels.add(31, "Total Payment Amount");

		// get the layout bean for this page from session
		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.WITHDRAWAL_DETAILS_PATH, request);
		// Withdrawal Summary section header
		bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
				BDContentConstants.SUB_HEADER, null);
		withdrawalFieldLabels.add(32, StringUtils.trimToEmpty(bodyHeader));
		// Payee Payment section header
		bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
				BDContentConstants.BODY1_HEADER, null);
		withdrawalFieldLabels.add(33, StringUtils.trimToEmpty(bodyHeader));
		// Withdrawal Detail information header
		bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
				BDContentConstants.BODY2_HEADER, null);
		withdrawalFieldLabels.add(34, StringUtils.trimToEmpty(bodyHeader));

		//wilange
		withdrawalFieldLabels.add(35, "1st Year of Designated Roth Contributions");
		withdrawalFieldLabels.add(36, "Pre-87 After Tax Employee Contributions Withdrawn");
		withdrawalFieldLabels.add(37 ,"Payee Name");
		withdrawalFieldLabels.add(38 ,"Money type");
		withdrawalFieldLabels.add(39 ,"Net contributions ($)");
		withdrawalFieldLabels.add(40 ,"Net earnings ($)");
		withdrawalFieldLabels.add(41 ,"Withdrawal amount ($)");
		withdrawalFieldLabels.add(42 ,"After Tax Cost Basis");
		
		
		Contract currentContract = getBobContext(request).getCurrentContract();
		String message = ContentHelper.getContentText(
				BDContentConstants.CORRECTION_INDICATOR_MESSAGE,
				ContentTypeManager.instance().MESSAGE, null);
		downloadData = WithdrawalDetailsUtility.getDownloadData(reportForm,
				report, request, currentContract, true, message,
				withdrawalFieldLabels, BDConstants.BD_APPLICATION_ID);

		if (logger.isDebugEnabled())
			logger.debug("exit <- getDownloadData");

		// return withdrawal information in byte[] object
		return downloadData;
	}

	/**
	 * Set Participant id ,transaction number,contract id in report criteria
	 * object.
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		Contract currentContract = getBobContext(request).getCurrentContract();

		WithdrawalDetailsUtility.populateReportCriteria(criteria, form,
				request, currentContract);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}

	}

	/**
	 * Method to prepare XML from ReportData.
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @exception ParserConfigurationException
	 */
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> prepareXMLFromReport");
		}

		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		Collection<WithdrawalPayeePaymentVO> withdrawalPayeeList = null;
		Collection<WithdrawalMoneyTypeVO> moneyTypeVOList = null;

		boolean isMultiPayee = false;
		Integer payeeId = 0;

		Element commonIndicatorElement;

		CompletedWithdrawalDetailsReportData data = (CompletedWithdrawalDetailsReportData) report;

		PDFDocument doc = new PDFDocument();

		commonIndicatorElement = doc
				.createElement(BDPdfConstants.COMMON_INDICATOR);

		// get the details list from the ReportData
		ArrayList<WithdrawalDetailsReportItem> details = (ArrayList<WithdrawalDetailsReportItem>) data
				.getDetails();

		// validate the details list is not empty
		if (details != null && !details.isEmpty()) {

			// we know that the data is for only one transaction and so no
			// need to iterate the list
			WithdrawalDetailsReportItem withdrawalDetailsItem = details.get(0);

			// get the general information VO from Report Item
			withdrawalGeneralInfoVO = withdrawalDetailsItem
					.getWithdrawalGeneralInfoVO();

			// get the Money type List from Report Item
			moneyTypeVOList = withdrawalDetailsItem.getWithdrawalMoneyTypeVO();
			
			// get the Payee List from Report Item
			withdrawalPayeeList = withdrawalDetailsItem
								.getWithdrawalPayeePaymentVO();

			// get the layout bean for this page from session
			LayoutPage layoutPageBean = getLayoutPage(
					BDPdfConstants.WITHDRAWAL_DETAILS_PATH, request);

			Element rootElement = doc
					.createRootElement(BDPdfConstants.WITHDRAWAL_DETAILS);

			// Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
			setIntroXMLElements(layoutPageBean, doc, rootElement, request);

			// Sets Summary Info.
			setSummaryInfoXMLElements(doc, rootElement,
					withdrawalGeneralInfoVO, layoutPageBean, request);

			// Getting body header and setting it root Element
			String bodyHeader = ContentUtility.getContentAttributeText(
					layoutPageBean, BDContentConstants.BODY1_HEADER, null);
			PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement,
					doc, bodyHeader);

			bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
					BDContentConstants.BODY2_HEADER, null);
			PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement,
					doc, bodyHeader);

			bodyHeader = ContentUtility.getContentAttributeText(layoutPageBean,
					BDContentConstants.BODY3_HEADER, null);
			PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER3, rootElement,
					doc, bodyHeader);
			
			// Withdrawal information Details
			setWithdrawalInformationDetailsXMLElement(request, moneyTypeVOList,
					commonIndicatorElement, data, doc, rootElement,withdrawalGeneralInfoVO);
			// Life Income Amount Details
			setLifeIncomeAmountDetailsXMLElement(reportForm,
					commonIndicatorElement, doc);
			
			// Payee Information detail
						setPayeeDetailsXMLElement(withdrawalPayeeList, isMultiPayee,
								payeeId, commonIndicatorElement, doc, rootElement);

			doc.appendElement(rootElement, commonIndicatorElement);

			// Sets footer, footnotes and disclaimer
			setFooterXMLElements(layoutPageBean, doc, rootElement, request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> prepareXMLFromReport");
		}

		return doc.getDocument();
	}

	/**
	 * @param request
	 * @param moneyTypeVOList
	 * @param commonIndicatorElement
	 * @param data
	 * @param doc
	 * @param rootElement
	 */
	private void setWithdrawalInformationDetailsXMLElement(
			HttpServletRequest request,
			Collection<WithdrawalMoneyTypeVO> moneyTypeVOList,
			Element commonIndicatorElement,
			CompletedWithdrawalDetailsReportData data, PDFDocument doc,
			Element rootElement,WithdrawalGeneralInfoVO withdrawalGeneralInfoVO) {
		WithdrawalCalculatedInfoVO withdrawalCalculatedInfoVO;
		Element withdrawalDetailElement;
		Element withdrawalcalcElement;
		// If loan default Indicator is yes ,append the loan default message.
		if (data.getLoanDefaultIndicator() != null
				&& BDConstants.YES.equals(data.getLoanDefaultIndicator())) {

			doc.appendTextNode(commonIndicatorElement, BDPdfConstants.LOAN_IND,
					data.getLoanDefaultIndicator());

			// get the loan notification message from the cma
			String message = ContentHelper.getContentText(
					BDContentConstants.MESSAGE_LOAN_DEFAULT_NOTIFICATION,
					ContentTypeManager.instance().MESSAGE, null);

			// append the loan notification message to the doc
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.LOAN_DEFAULT_NOTIF, message);
		}

		// If PBA disbursement Indicator is yes ,append the PBA message.
		if (data.getPbaDisbursementIndicator() != null
				&& BDConstants.PBA_DISBURSEMENT_INDICATOR.equals(data
						.getPbaDisbursementIndicator())) {

			doc.appendTextNode(commonIndicatorElement, BDPdfConstants.PBA_IND,
					data.getPbaDisbursementIndicator());

			// get the PBA notification message from the cma
			String message = ContentHelper.getContentText(
					BDContentConstants.MISCELLANEOUS_PBA_NOTIFICATION,
					ContentTypeManager.instance().MESSAGE, null);

			// append the PBA notification message to the doc
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.PBA_DEFAULT_NOTIF, message);
		}

		if(withdrawalGeneralInfoVO.getRothStartedYears()>0  && withdrawalGeneralInfoVO.getRothStartedYears()!=9999){
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.DESGINATED_ROTH, Integer.toString(withdrawalGeneralInfoVO.getRothStartedYears()));
		}
		if(withdrawalGeneralInfoVO.isPre87Present()){
			String availableAmount = formatToCurrencyTypeWithoutSign(withdrawalGeneralInfoVO.getPre87Amount());
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.PRE_87, "$"+availableAmount);
		}
		// Appending the web version display.
		doc.appendTextNode(commonIndicatorElement, BDPdfConstants.WEB_VERSION,
				data.getWebDisplayVersion());

		// append the money type data
		if (moneyTypeVOList != null && !moneyTypeVOList.isEmpty()) {
			for (WithdrawalMoneyTypeVO withdrawalMoneyTypeVO : moneyTypeVOList) {
				if (withdrawalMoneyTypeVO != null) {
					withdrawalDetailElement = doc
							.createElement(BDPdfConstants.WITHDRAWAL_TXN);

					// set the money type details in the doc
					setWithdrawalMoneyType(data, doc, withdrawalDetailElement,
							withdrawalMoneyTypeVO);
					doc.appendElement(rootElement, withdrawalDetailElement);
				}
			}
		}

		// append the Calculation information
		withdrawalCalculatedInfoVO = data.getWithdrawalCalculatedInfoVO();
		withdrawalcalcElement = doc
				.createElement(BDPdfConstants.WITHDRAWAL_CALC);

		if (withdrawalCalculatedInfoVO != null) {
			setWithdrawalCalcInfo(doc, withdrawalcalcElement,
					withdrawalCalculatedInfoVO, request);
		}

		doc.appendElement(rootElement, withdrawalcalcElement);
	}

	/**
	 * @param withdrawalPayeeList
	 * @param isMultiPayee
	 * @param payeeId
	 * @param commonIndicatorElement
	 * @param doc
	 * @param rootElement
	 */
	private void setPayeeDetailsXMLElement(
			Collection<WithdrawalPayeePaymentVO> withdrawalPayeeList,
			boolean isMultiPayee, Integer payeeId,
			Element commonIndicatorElement, PDFDocument doc, Element rootElement) {
		Element payeeDetailElement;
		if (withdrawalPayeeList != null && !withdrawalPayeeList.isEmpty()) {

			// if its Size is greater than 2 set isMultiPayee to true
			if (withdrawalPayeeList.size() > 1) {
				isMultiPayee = true;
			}

			// Append the withdrawalPayeePaymentVO with payeeDetailElement
			// for each payee.
			Element afterTaxRothElement;
			for (WithdrawalPayeePaymentVO withdrawalPayeePaymentVO : withdrawalPayeeList) {
				payeeId++;
				payeeDetailElement = doc
						.createElement(BDPdfConstants.PAYEE_DETAILS);

				if (withdrawalPayeePaymentVO != null) {
					setPayeeDetails(doc, payeeDetailElement,
							withdrawalPayeePaymentVO, isMultiPayee, payeeId);
				}
				if(withdrawalPayeePaymentVO!=null && withdrawalPayeePaymentVO.getAfterTaxVO()!=null){
					
					for(WithdrawalAfterTaxInfoVO aftertaxObject : withdrawalPayeePaymentVO.getAfterTaxVO()) {
						afterTaxRothElement = doc
								.createElement(BDPdfConstants.AFTER_TAX_ROTH);
						doc.appendTextNode(afterTaxRothElement,
								BDPdfConstants.AFTER_TAX_MTY, aftertaxObject.getMoneyTypeInfo());
						String netContrib = formatToCurrencyTypeWithoutSign(aftertaxObject.getAfterTaxNetContribution());
							doc.appendTextNode(afterTaxRothElement,
									BDPdfConstants.AFTER_TAX_CONTRB_AMT, netContrib);
							String netEarning = formatToCurrencyTypeWithoutSign(aftertaxObject.getAfterTaxNetEarning());
							doc.appendTextNode(afterTaxRothElement,
									BDPdfConstants.AFTER_TAX_EARN_AMT,netEarning);
							String withdrAmt = formatToCurrencyTypeWithoutSign(aftertaxObject.getAfterTaxWDAmt());
							doc.appendTextNode(afterTaxRothElement,
									BDPdfConstants.AFTER_TAX_WD_AMT, withdrAmt);
							doc.appendElement(payeeDetailElement, afterTaxRothElement);
					}
					
				}
				doc.appendElement(rootElement, payeeDetailElement);

				// Complete withdrawal details page should contain 2 or less
				// than 2 payee list to be displayed
				/*if (payeeId == 2) {
					break;
				}*/
			}

			// If it is multipayee, append the multi payee
			// notification message.
			if (withdrawalPayeeList.size() > 2) {

				// get the notification message from the cma
				String message = ContentHelper.getContentText(
						BDContentConstants.MESSAGE_MULTI_PAYEE_NOTIFICATION,
						ContentTypeManager.instance().MESSAGE, null);

				// append the notification message to the doc
				doc.appendTextNode(commonIndicatorElement,
						BDPdfConstants.MULTI_PAYEE_NOTIF, message);
			}
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String formatToCurrencyTypeWithoutSign(BigDecimal value) {
		return NumberRender.formatByType(value, null,
				RenderConstants.CURRENCY_TYPE, false);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	private String formatToCurrencyTypeWithSign(BigDecimal value) {
		return NumberRender.formatByType(value, null,
				RenderConstants.CURRENCY_TYPE, true);
	}

	/**
	 * Method to append WithdrawalCalculatedInfo to withdrawalcalcElement
	 * 
	 * @param doc
	 * @param withdrawalcalcElement
	 * @param withdrawalCalculatedInfoVO
	 */
	private void setWithdrawalCalcInfo(PDFDocument doc,
			Element withdrawalcalcElement,
			WithdrawalCalculatedInfoVO withdrawalCalculatedInfoVO,
			HttpServletRequest request) {

		// append total amount
		String totalAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
				.getTotalAmount());
		doc.appendTextNode(withdrawalcalcElement, BDPdfConstants.TOTAL_AMT,
				totalAmt);

		// append Roth Indicator
		doc.appendTextNode(withdrawalcalcElement, BDPdfConstants.ROTH_IND,
				withdrawalCalculatedInfoVO.getRothMoneyIndicator());

		// append MVA Indicator
		doc.appendTextNode(withdrawalcalcElement, BDPdfConstants.MVA_IND,
				withdrawalCalculatedInfoVO.getMvaAppliesIndicator());

		// append the fundOnDeposit value, if it contain
		if (withdrawalCalculatedInfoVO.getFundsOnDepositInterest() != null) {
			String fundOnDepositInst = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getFundsOnDepositInterest());
			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.FUND_DEPOSIT_INST, fundOnDepositInst);
		}

		// append the MVA value
		if (BDConstants.YES.equalsIgnoreCase(withdrawalCalculatedInfoVO
				.getMvaAppliesIndicator())) {
			String mvaAmount = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getMvaAmount());
			doc.appendTextNode(withdrawalcalcElement, BDPdfConstants.MVA_AMT,
					mvaAmount);
		}

		if (!(getBobContext(request).getCurrentContract()
				.isDefinedBenefitContract())) {

			// append Federal Tax amount
			String federalTaxAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getFederalTaxAmount());
			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.FEDERAL_TAX_AMT, federalTaxAmt);

			// append Tax amount
			String taxableAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getTaxableAmount());
			doc.appendTextNode(withdrawalcalcElement, BDPdfConstants.TAX_AMT,
					taxableAmt);

			// append State Tax amount
			String stateTaxAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getStateTaxAmount());
			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.STATE_TAX_AMT, stateTaxAmt);

			// append the roth Tax ,if roth money type is yes.
			if (BDConstants.YES.equalsIgnoreCase(withdrawalCalculatedInfoVO
					.getRothMoneyIndicator())) {

				// append Roth Federal tax amount
				String federalTaxAmtRoth = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
						.getRothFederalTaxAmount());
				doc.appendTextNode(withdrawalcalcElement,
						BDPdfConstants.ROTH_FEDERAL_TAX_AMT, federalTaxAmtRoth);

				// append Roth State tax amount
				String stateTaxAmtRoth = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
						.getRothStateTaxAmount());
				doc.appendTextNode(withdrawalcalcElement,
						BDPdfConstants.ROTH_STATE_TAX_AMT, stateTaxAmtRoth);

				// append Roth tax amount
				String taxableAmtRoth = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
						.getRothTaxableAmount());
				doc.appendTextNode(withdrawalcalcElement,
						BDPdfConstants.ROTH_TAX_AMT, taxableAmtRoth);
			}
		}

		// append total Payment amount
		String totalPaymentAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
				.getTotalPaymentAmount());
		doc.appendTextNode(withdrawalcalcElement,
				BDPdfConstants.TOTAL_PYMT_AMT, totalPaymentAmt);

		// append the forfeited or UM Amount
		if (StringUtils.isNotBlank(withdrawalCalculatedInfoVO
				.getForfeitedOrUMText1ForFRW())
				&& StringUtils.isNotBlank(withdrawalCalculatedInfoVO
						.getForfeitedOrUMText2ForFRW())) {

			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.FORFEITED_OR_UM_TEXT1,
					withdrawalCalculatedInfoVO.getForfeitedOrUMText1ForFRW());
			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.FORFEITED_OR_UM_TEXT2,
					withdrawalCalculatedInfoVO.getForfeitedOrUMText2ForFRW());

			String forfeitedOrUMAmt = formatToCurrencyTypeWithoutSign(withdrawalCalculatedInfoVO
					.getForfeitedOrUnvestedERAmount());

			doc.appendTextNode(withdrawalcalcElement,
					BDPdfConstants.FORFEITED_OR_UM_ER_AMOUNT, forfeitedOrUMAmt);
		}
	}

	/**
	 * Method to append WithdrawalPayeePaymentVO with payeeDetailElement.
	 * 
	 * @param doc
	 * @param payeeDetailElement
	 * @param withdrawalPayeePaymentVO
	 * @param isMultiPayee
	 * @param payeeId
	 */
	private void setPayeeDetails(PDFDocument doc, Element payeeDetailElement,
			WithdrawalPayeePaymentVO withdrawalPayeePaymentVO,
			boolean isMultiPayee, Integer payeeId) {

		// append payee numbering, if it is multiple payee.
		if (isMultiPayee) {
			doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_ID,
					payeeId.toString());
		}

		// append payee amount
		String participantAmt = formatToCurrencyTypeWithSign(withdrawalPayeePaymentVO
				.getPaymentAmount());
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYMENT_AMOUNT,
				participantAmt);

		// append payment to
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PYM_TO,
				withdrawalPayeePaymentVO.getPaymentTo());

		// append payment method
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PYM_METHOD,
				withdrawalPayeePaymentVO.getPaymentMethod());

		// append Payee Name
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_NAME,
				withdrawalPayeePaymentVO.getPayeeName());

		// append Payee address1
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_ADDR1,
				withdrawalPayeePaymentVO.getAddressLine1());

		// append payee address2
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_ADDR2,
				withdrawalPayeePaymentVO.getAddressLine2());

		// append payee city
		if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getCity())) {
			doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_CITY,
					withdrawalPayeePaymentVO.getCity());
		}

		// append payee state
		if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getState())) {
			doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_STATE,
					withdrawalPayeePaymentVO.getState());
		}

		// append payee Zip
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_ZIP,
				withdrawalPayeePaymentVO.getZip());

		// append payee country
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.PAYEE_COUNTRY,
				withdrawalPayeePaymentVO.getCountry());

		// append payee account type
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.ACC_TYPE,
				withdrawalPayeePaymentVO.getAccountType());

		// append Bank branch name
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.BANK_BRANCH_NAME,
				withdrawalPayeePaymentVO.getBankBranchName());

		// append Bank Rounting number
		doc.appendTextNode(payeeDetailElement, BDPdfConstants.ROUTINGABA_NUM,
				withdrawalPayeePaymentVO.getRoutingABAnumber());

		// append account number
		if (StringUtils.isNotBlank(withdrawalPayeePaymentVO.getAccountNumber())) {
			doc.appendTextNode(payeeDetailElement, BDPdfConstants.ACC_NUMBER,
					withdrawalPayeePaymentVO.getAccountNumber());
		}
		// append credit payee name
		doc.appendTextNode(payeeDetailElement,
				BDPdfConstants.CREDIT_PAYEE_NAME,
				withdrawalPayeePaymentVO.getCreditPayeeName());

	}

	/**
	 * Method to append WithdrawalMoneyTypeVO with withdrawalDetailElement.
	 * 
	 * @param data
	 * @param doc
	 * @param withdrawalDetailElement
	 * @param withdrawalMoneyTypeVO
	 */
	private void setWithdrawalMoneyType(
			CompletedWithdrawalDetailsReportData data, PDFDocument doc,
			Element withdrawalDetailElement,
			WithdrawalMoneyTypeVO withdrawalMoneyTypeVO) {

		// append Mopney type details
		doc.appendTextNode(withdrawalDetailElement, BDPdfConstants.MONEY_TYPE,
				withdrawalMoneyTypeVO.getMoneyType());

		if (BDConstants.VERSION_1.equals(data.getWebDisplayVersion())) {

			// append account balance
			String accountBalance = formatToCurrencyTypeWithoutSign(withdrawalMoneyTypeVO
					.getAccountBalance());
			doc.appendTextNode(withdrawalDetailElement,
					BDPdfConstants.ACCT_BALANCE, accountBalance);

			// append the vesting percentage
			String vestingPerc = NumberRender.formatByPattern(
					withdrawalMoneyTypeVO.getVestingPercentage(), null,
					"0.000", 3, BigDecimal.ROUND_HALF_UP);
			doc.appendTextNode(withdrawalDetailElement,
					BDPdfConstants.VESTING_PERCT, vestingPerc);
		}

		// append available amount
		String availableAmount = formatToCurrencyTypeWithoutSign(withdrawalMoneyTypeVO
				.getAvailableAmount());
		doc.appendTextNode(withdrawalDetailElement, BDPdfConstants.AVL_AMT,
				availableAmount);

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
	private void setSummaryInfoXMLElements(PDFDocument doc,
			Element rootElement,
			WithdrawalGeneralInfoVO withdrawalGeneralInfoVO,
			LayoutPage layoutPageBean, HttpServletRequest request) {

		Element summaryInfoElement = doc
				.createElement(BDPdfConstants.SUMMARY_INFO);

		// Get the subheader content cma and append to the doc
		String subHeader = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.SUB_HEADER, null);
		PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement,
				doc, subHeader);

		// format Withdrawal Date
		String formattedDate = DateRender.formatByPattern(
				withdrawalGeneralInfoVO.getWithdrawalDate(), null,
				RenderConstants.MEDIUM_YMD_DASHED,
				RenderConstants.MEDIUM_MDY_SLASHED);
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.WITHDRAWAL_DATE,
				formattedDate);

		// append participant amount
		String participantAmt = formatToCurrencyTypeWithSign(withdrawalGeneralInfoVO
				.getPaymentAmount());
		doc.appendTextNode(summaryInfoElement, BDPdfConstants.PAYMENT_AMOUNT,
				participantAmt);

		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.TRANSACTION_NUMBER,
				withdrawalGeneralInfoVO.getTransactionNumber());

		doc.appendTextNode(summaryInfoElement, BDPdfConstants.TRANSACTION_TYPE,
				withdrawalGeneralInfoVO.getTransactionType());

		doc.appendTextNode(summaryInfoElement,
				BDPdfConstants.TRANSACTION_TYPE_DECS,
				withdrawalGeneralInfoVO.getTransactionTypeDescription());

		// append participant name and ssn , if it is not DBcontract.
		if (!(getBobContext(request).getCurrentContract()
				.isDefinedBenefitContract())) {

			// append name
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.PARTICIPANT_NAME,
					withdrawalGeneralInfoVO.getName());

			// append SSN
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.PARTICIPANT_SSN,
					withdrawalGeneralInfoVO.getMaskedSSN());
		}

		doc.appendElement(rootElement, summaryInfoElement);
	}

	/**
	 * This method is used to frame the file name used for the downloaded CSV.
	 * 
	 * @param BaseReportForm
	 * @param HttpServletRequest
	 * @return The file name used for the downloaded CSV.
	 */
	@SuppressWarnings("unchecked")
	protected String getFileName(BaseReportForm form,
			HttpServletRequest request) {
		Contract currentContract = getBobContext(request).getCurrentContract();
		WithdrawalGeneralInfoVO withdrawalGeneralInfoVO = null;
		CompletedWithdrawalDetailsReportData report = (CompletedWithdrawalDetailsReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);
		List<WithdrawalDetailsReportItem> details = (List<WithdrawalDetailsReportItem>) report
				.getDetails();
		withdrawalGeneralInfoVO = details.get(0).getWithdrawalGeneralInfoVO();
		String formattedTransactionDate = DateRender.format(
				withdrawalGeneralInfoVO.getWithdrawalDate(),
				RenderConstants.MEDIUM_MDY_SLASHED).replace(
				BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL);
		String csvFileName = WITHRAWAL_DETAILS + "-"
				+ currentContract.getContractNumber() + "-"
				+ formattedTransactionDate + CSV_EXTENSION;
		return csvFileName;
	}

	/**
	 * @param reportForm
	 * @param commonIndicatorElement
	 * @param doc
	 */
	private void setLifeIncomeAmountDetailsXMLElement(
			BaseReportForm reportForm, Element commonIndicatorElement,
			PDFDocument doc) {
		TransactionDetailsWithdrawalForm form = (TransactionDetailsWithdrawalForm) reportForm;

		doc.appendTextNode(commonIndicatorElement,
				BDPdfConstants.LIA_WITHDRAWAL_MESSAGE_IND,
				String.valueOf(form.isShowLiaWithdrawalMessage()));
		doc.appendTextNode(commonIndicatorElement,
				BDPdfConstants.LIA_WITHDRAWAL_NOTIFICATION_IND,
				String.valueOf(form.isShowLiaWithdrawalNotification()));

		// If showLiaWithdrawalMessage is true ,append the LIA message.
		if (form.isShowLiaWithdrawalMessage()) {

			// get the LIA message from the cma
			String message = ContentHelper.getContentText(
					BDContentConstants.MISCELLANEOUS_WITHDRAWAL_AMOUNT_FOR_LIA,
					ContentTypeManager.instance().MESSAGE, null);

			// append the LIA message to the doc
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.LIA_WITHDRAWAL_MESSAGE, message);
		}

		// If showLiaWithdrawalNotification is true ,append the LIA
		// notification.
		if (form.isShowLiaWithdrawalNotification()) {

			// get the notification message from the cma
			String message = ContentHelper
					.getContentText(
							BDContentConstants.MESSAGE_WITHDRAWAL_PARTICPANT_APPLICABLE_TO_LIA,
							ContentTypeManager.instance().MESSAGE, null);

			// append the notification message to the doc
			doc.appendTextNode(commonIndicatorElement,
					BDPdfConstants.LIA_WITHDRAWAL_NOTIFICATION, message);
		}
	}
	@RequestMapping(value ="/transaction/withdrawalDetailsReport/" , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/transaction/withdrawalDetailsReport/",params={"task=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    	 @RequestMapping(value ="/transaction/withdrawalDetailsReport/" ,params={"task=page"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
 	    public String doPage (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
 	   
 	   @RequestMapping(value ="/transaction/withdrawalDetailsReport/" ,params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doSort (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
 	   @RequestMapping(value ="/transaction/withdrawalDetailsReport/", params={"task=download"},method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doDownload (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
 		    forward=super.doDownload( form, request, response);
 		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	   }
 	  @RequestMapping(value = "/transaction/withdrawalDetailsReport/", params = {"task=printPDF"}, method = {RequestMethod.GET })
		public String doPrintPDF(@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form,
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
 	   
 	   @RequestMapping(value ="/transaction/withdrawalDetailsReport/", params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
 	   public String doDownloadAll (@Valid @ModelAttribute("transactionDetailsWithdrawalForm") TransactionDetailsWithdrawalForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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