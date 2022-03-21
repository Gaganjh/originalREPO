package com.manulife.pension.bd.web.bob.participant;

import static com.manulife.pension.bd.web.BDConstants.DEFAULT_VALUE_ZERO;
import static com.manulife.pension.bd.web.BDConstants.DEFAULT_VALUE_ZERO_PERCENT;
import static com.manulife.pension.bd.web.BDConstants.PERCENT_TWO_DECIMAL_FORMAT;
import static com.manulife.pension.bd.web.BDConstants.RATE_FORMAT;
import static com.manulife.pension.bd.web.BDConstants.ZERO_STRING;
import static com.manulife.pension.platform.web.CommonConstants.AMOUNT_FORMAT_SIX_DECIMALS;
import static com.manulife.pension.platform.web.CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

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
import com.manulife.pension.bd.web.bob.transaction.LoanRepaymentDetailsReportForm;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDPdfHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeDetailVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.NumberRender;

/**
 * This is the action class for Defined Benefit Money Details Page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantAccountForm"})

public class DefinedBenefitAccountMoneyTypeDetailsController extends
		DefinedBenefitAccountCommonController {
	@ModelAttribute("participantAccountForm") 
	public ParticipantAccountForm populateForm() 
	{
		return new ParticipantAccountForm();
		}
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateFormLoan() {
		return new LoanRepaymentDetailsReportForm();
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantAccountMoneyTypeDetails.jsp");
		forwards.put("definedBenefitAccountMoneyTypeDetails","/participant/definedBenefitAccountMoneyTypeDetails.jsp");
		}

	public DefinedBenefitAccountMoneyTypeDetailsController() {
		super(DefinedBenefitAccountMoneyTypeDetailsController.class);
	}

	/**
	 * @See ParticipantAccountCommonAction#populateDetailedDownloadData()
	 */
	
	protected String populateDetailedDownloadData(
			ParticipantAccountVO participantAccountVO,
			ParticipantAccountForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> DefinedBenefitAccountMoneyTypeDetailsAction");
		}

		DecimalFormat unitsHeldFormatter = new DecimalFormat("#0.000000");
		DecimalFormat percentFormatter = new DecimalFormat("#0.00");
		DecimalFormat unitValueFormatter = new DecimalFormat("#0.00");

		StringBuffer buffer = new StringBuffer();
		buffer.append(BDConstants.MONEY_TYPE_DETAILS_TAB_NAME).append(
				LINE_BREAK);
		buffer.append(LINE_BREAK);
		BDLayoutBean bean = ApplicationHelper.getLayoutStore(
				request.getServletContext()).getLayoutBean(
				BDPdfConstants.DB_ACCOUNT_PATH, request);
		LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
		String bodyHeader2 = ContentUtility.getContentAttributeText(
				layoutPageBean, BDContentConstants.BODY2_HEADER, null);

		buffer.append(bodyHeader2 + "  organized by").append(COMMA);
		if (BDConstants.VIEW_BY_ASSET_CLASS.equals(form.getFundsOrganizedBy())) {
			buffer.append("Asset Class");
		} else if (BDConstants.VIEW_BY_RISK_CATEGORY.equals(form
				.getFundsOrganizedBy())) {
			buffer.append("Risk/Return category");
		}
		buffer.append(LINE_BREAK);

		buffer.append(
				"Investment Option,Class,Number Of Units,Unit Value($) / Interest Rate,Balance($),Percentage Of Total(%)")
				.append(LINE_BREAK);

		InvestmentOptionVO[] options = participantAccountVO
				.getOrganizedParticipantFunds();
		if (BDConstants.VIEW_BY_RISK_CATEGORY
				.equals(form.getFundsOrganizedBy())) {
			options = participantAccountVO.getParticipantFundsByRisk();
		}
		for (InvestmentOptionVO optionVO : options) {

			if (optionVO.getParticipantFundSummaryArray().length == 0) {
				continue;
			}

			buffer.append(LINE_BREAK)
					.append(optionVO.getCategory().getCategoryDesc())
					.append(LINE_BREAK);

			ParticipantFundSummaryVO[] summaries = optionVO
					.getParticipantFundSummaryArray();

			for (ParticipantFundSummaryVO summVO : summaries) {

				buffer.append(summVO.getFundName()).append(COMMA);
				buffer.append(summVO.getFundClass()).append(COMMA);
				if (summVO.getFundTotalNumberOfUnitsHeld() == 0) {
					buffer.append(COMMA);
					if (summVO.getFundTotalCompositeRate() > 0) {
						buffer.append(
								percentFormatter.format(summVO
										.getFundTotalCompositeRate())).append(
								COMMA);
					} else {
						buffer.append(
								unitValueFormatter.format(summVO
										.getFundUnitValue())).append(COMMA);
					}
				} else {
					buffer.append(
							unitsHeldFormatter.format(summVO
									.getFundTotalNumberOfUnitsHeld())).append(
							COMMA);
					buffer.append(
							unitValueFormatter.format(summVO.getFundUnitValue()))
							.append(COMMA);
				}

				buffer.append(summVO.getFundTotalBalance()).append(COMMA);
				buffer.append(
						percentFormatter.format(summVO
								.getFundTotalPercentageOfTotal() * 100.0d))
						.append("%").append(LINE_BREAK);

				ParticipantFundMoneyTypeDetailVO[] moneyTypeDetails = summVO
						.getFundMoneyTypeDetails();

				for (ParticipantFundMoneyTypeDetailVO moneyTypeDetail : moneyTypeDetails) {
					buffer.append(moneyTypeDetail.getMoneyTypeName())
							.append(COMMA).append(COMMA);

					if (summVO.getFundTotalNumberOfUnitsHeld() == 0) {
						if (summVO.getFundTotalCompositeRate() > 0)
							buffer.append(COMMA).append(COMMA);
						else
							buffer.append(
									unitValueFormatter.format(moneyTypeDetail
											.getNumberOfUnitsHeld())).append(
									COMMA);
					} else {
						buffer.append(
								unitsHeldFormatter.format(moneyTypeDetail
										.getNumberOfUnitsHeld())).append(COMMA);
						buffer.append(
								unitValueFormatter.format(summVO
										.getFundUnitValue())).append(COMMA);
					}

					buffer.append(LINE_BREAK);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- DefinedBenefitAccountMoneyTypeDetailsAction");
		}
		return buffer.toString();
	}

	/**
	 * @See ParticipantAccountCommonAction#getTabName()
	 */
	@Override
	protected String getTabName() {
		return BDConstants.MONEY_TYPE_DETAILS_CSV_NAME;
	}

	/**
	 * @See BDPdfAction#prepareDetailedXMLFromReport()
	 */
	
	public Document prepareDetailedXMLFromReport(
			ParticipantAccountVO participantAccountVO,
			ParticipantAccountForm accountForm, PDFDocument doc,
			Element rootElement, HttpServletRequest request)
			throws ParserConfigurationException {

		int rowCount = 1;
		int maxRowsinPDF;

		BDLayoutBean bean = ApplicationHelper.getLayoutStore(
				request.getServletContext()).getLayoutBean(
				BDPdfConstants.PPT_ACCOUNT_MONEY_TYPE_DETAILS_PATH, request);
		BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request,
				accountForm);
		doc.appendTextNode(rootElement, BDPdfConstants.ORGANIZED_BY,
				accountForm.getFundsOrganizedBy());
		if (accountForm.isAsOfDateCurrent()) {
			doc.appendTextNode(rootElement,
					BDPdfConstants.AS_OF_DATE_REPORT_CURRENT, null);
			if (accountForm.getHasInvestments()) {

				Element reportDetailsElement = doc
						.createElement(BDPdfConstants.REPORT_DETAILS);
				Element reportDetailElement = null;
				InvestmentOptionVO[] options = participantAccountVO
						.getOrganizedParticipantFunds();
				if (options != null) {
					maxRowsinPDF = accountForm.getCappedRowsInPDF();
					for (int i = 0; i < options.length
							&& rowCount <= maxRowsinPDF; i++) {

						InvestmentOptionVO optionVO = options[i];
						if (optionVO != null
								&& optionVO.getParticipantFundSummaryArray().length == 0) {
							continue;
						}

						Element fundDetailElement;
						reportDetailElement = doc
								.createElement(BDPdfConstants.REPORT_DETAIL);

						doc.appendTextNode(reportDetailElement,
								BDPdfConstants.FUND_CATEGORY, optionVO
										.getCategory().getCategoryDesc());

						for (ParticipantFundSummaryVO summaryVO : optionVO
								.getParticipantFundSummaryArray()) {

							fundDetailElement = doc
									.createElement(BDPdfConstants.FUND_DETAIL);
							setFundDetailsXMLElements(doc, fundDetailElement,
									summaryVO, optionVO);

							for (ParticipantFundMoneyTypeDetailVO moneyTypeVO : summaryVO
									.getFundMoneyTypeDetails()) {

								setMoneyTypeDetailsXMLElements(doc,
										fundDetailElement, summaryVO, optionVO,
										moneyTypeVO);

							}
							doc.appendElement(reportDetailElement,
									fundDetailElement);
							rowCount++;
						}

						doc.appendElement(reportDetailsElement,
								reportDetailElement);
					}
				}

				doc.appendElement(rootElement, reportDetailsElement);
			}
		}

		if (accountForm.getPdfCapped()) {
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		return doc.getDocument();

	}

	/**
	 * This method sets fund details XML elements
	 * 
	 * @param doc
	 * @param reportDetailElement
	 * @param summaryVO
	 * @param optionVO
	 */
	private void setFundDetailsXMLElements(PDFDocument doc,
			Element fundDetailElement, ParticipantFundSummaryVO summaryVO,
			InvestmentOptionVO optionVO) {

		if (summaryVO != null) {
			doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_NAME,
					summaryVO.getFundName());
			doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_CLASS,
					String.valueOf(summaryVO.getFundClass()));
			if (!optionVO.getCategory().getCategoryCode()
					.equals(Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT)) {

				String fundUnitValue = NumberRender.formatByPattern(
						summaryVO.getFundUnitValue(), DEFAULT_VALUE_ZERO,
						AMOUNT_FORMAT_TWO_DECIMALS);
				if (summaryVO.getFundTotalNumberOfUnitsHeld() == 0.00) {

					if (summaryVO.getFundTotalCompositeRate() != 0.00) {
						String compositeRate = NumberRender.formatByPattern(
								summaryVO.getFundTotalCompositeRate(),
								ZERO_STRING, RATE_FORMAT, 4,
								BigDecimal.ROUND_HALF_DOWN);
						doc.appendTextNode(fundDetailElement,
								BDPdfConstants.COMPOSITE_RATE, compositeRate);
					} else {
						doc.appendTextNode(fundDetailElement,
								BDPdfConstants.UNIT_VALUE, fundUnitValue);
					}

				} else {

					doc.appendTextNode(fundDetailElement,
							BDPdfConstants.UNIT_VALUE, fundUnitValue);

					String noOfUnits = NumberRender.formatByPattern(
							summaryVO.getFundTotalNumberOfUnitsHeld(),
							DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_SIX_DECIMALS, 6,
							BigDecimal.ROUND_HALF_DOWN);
					doc.appendTextNode(fundDetailElement,
							BDPdfConstants.NO_OF_UNITS, noOfUnits);

				}

				String totalBalance = NumberRender.formatByPattern(
						summaryVO.getFundTotalBalance(), DEFAULT_VALUE_ZERO,
						AMOUNT_FORMAT_TWO_DECIMALS);
				doc.appendTextNode(fundDetailElement,
						BDPdfConstants.TOTAL_BALANCE, totalBalance);

				String totalPercent = NumberRender.formatByPattern(
						summaryVO.getFundTotalPercentageOfTotal(),
						DEFAULT_VALUE_ZERO_PERCENT, PERCENT_TWO_DECIMAL_FORMAT,
						4, BigDecimal.ROUND_HALF_DOWN);
				doc.appendTextNode(fundDetailElement,
						BDPdfConstants.TOTAL_PERCENT, totalPercent);
			}
		}

	}

	/**
	 * This method sets money type details XML elements
	 * 
	 * @param doc
	 * @param reportDetailElement
	 * @param summaryVO
	 * @param optionVO
	 */
	private void setMoneyTypeDetailsXMLElements(PDFDocument doc,
			Element fundDetailElement, ParticipantFundSummaryVO summaryVO,
			InvestmentOptionVO optionVO,
			ParticipantFundMoneyTypeDetailVO moneyTypeVO) {

		Element moneyTypes = doc.createElement(BDPdfConstants.MONEY_TYPES);
		if (moneyTypeVO != null) {
			doc.appendTextNode(moneyTypes, BDPdfConstants.MONEY_TYPE,
					moneyTypeVO.getMoneyTypeName());
			if (!optionVO.getCategory().getCategoryCode()
					.equals(Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT)) {

				if (moneyTypeVO.getNumberOfUnitsHeld() != 0.00) {

					String unitValue = NumberRender.formatByPattern(
							summaryVO.getFundUnitValue(), DEFAULT_VALUE_ZERO,
							AMOUNT_FORMAT_TWO_DECIMALS);
					doc.appendTextNode(moneyTypes, BDPdfConstants.UNIT_VALUE,
							unitValue);

					String noOfUnits = NumberRender.formatByPattern(
							moneyTypeVO.getNumberOfUnitsHeld(),
							DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_SIX_DECIMALS, 6,
							BigDecimal.ROUND_HALF_DOWN);
					doc.appendTextNode(moneyTypes, BDPdfConstants.NO_OF_UNITS,
							noOfUnits);

				}

			}
		}
		doc.appendElement(fundDetailElement, moneyTypes);

	}

	/**
	 * @See BDPdfAction#getNumberOfRowsInReport()
	 */
	@Override
	public Integer getNumberOfRowsInReport(Object reportData) {
		ParticipantAccountVO participantAccountVO = (ParticipantAccountVO) reportData;
		int noOfRows = 0;
		if (participantAccountVO.getParticipantFundsByRisk() != null) {
			for (InvestmentOptionVO optionVO : participantAccountVO
					.getOrganizedParticipantFunds()) {
				noOfRows += optionVO.getParticipantFundSummaryArray().length;
			}
		}
		return noOfRows;
	}
	
	@RequestMapping(value ="/db/definedBenefitAccountMoneyTypeDetails/",method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
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
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@Autowired
    private BDValidatorFWInput  bDValidatorFWInput ;  

    @InitBinder
    protected void initBinder(HttpServletRequest request,
    			ServletRequestDataBinder  binder) {
    	binder.bind(request);
    	binder.addValidators(bDValidatorFWInput);
    }
	


	
}