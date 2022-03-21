package com.manulife.pension.bd.web.bob.participant;

import java.io.IOException;
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
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDPdfHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeTotalsVO;
import com.manulife.util.render.NumberRender;

/**
 * This is the action class for Defined Benefit Money Summary Page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantAccountForm"})

public class DefinedBenefitAccountMoneyTypeSummaryController extends DefinedBenefitAccountCommonController {
	@ModelAttribute("participantAccountForm")
	public ParticipantAccountForm populateForm() {
		return new ParticipantAccountForm();
	}
	@ModelAttribute("loanRepaymentDetailsReportForm")
	public LoanRepaymentDetailsReportForm populateFormLoan() {
		return new LoanRepaymentDetailsReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/participant/participantAccountMoneyTypeSummary.jsp");
		forwards.put("definedBenefitAccountMoneyTypeSummary", "/participant/definedBenefitAccountMoneyTypeSummary.jsp");
	}

	/**
	 * Constructor.
	 */
	public DefinedBenefitAccountMoneyTypeSummaryController() {
		super(DefinedBenefitAccountMoneyTypeSummaryController.class);
	}

	/**
	 * Custom version for defined benefit that removes some data in the base
	 * version.
	 */
	protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO,
			ParticipantAccountForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> DefinedBenefitAccountMoneyTypeSummaryAction");
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append(BDConstants.MONEY_TYPE_SUMMARY_TAB_NAME).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Money Types").append(COMMA).append(" Total Assets($)");
		buffer.append(LINE_BREAK).append(LINE_BREAK);
		buffer.append("Employer contributions").append(COMMA);
		buffer.append(participantAccountVO.getTotalEmployerContributionsAssets());

		buffer.append(LINE_BREAK);

		ParticipantFundMoneyTypeTotalsVO[] employerMoneyTypeTotals = participantAccountVO.getEmployerMoneyTypeTotals();
		for (ParticipantFundMoneyTypeTotalsVO moneyTypeTotal : employerMoneyTypeTotals) {
			buffer.append(moneyTypeTotal.getMoneyTypeName()).append(COMMA);
			buffer.append(moneyTypeTotal.getBalance());
			buffer.append(LINE_BREAK);
		}

		buffer.append(LINE_BREAK);
		buffer.append("Total").append(COMMA);
		buffer.append(participantAccountVO.getTotalContributionsAssets());

		buffer.append(LINE_BREAK);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- DefinedBenefitAccountMoneyTypeSummaryAction");
		}
		return buffer.toString();
	}

	/**
	 * @See ParticipantAccountCommonAction#getTabName()
	 */
	@Override
	protected String getTabName() {
		return BDConstants.MONEY_TYPE_SUMMARY_CSV_NAME;
	}

	/**
	 * @See BDPdfAction#prepareDetailedXMLFromReport()
	 */

	protected Document prepareDetailedXMLFromReport(ParticipantAccountVO participantAccountVO,
			ParticipantAccountForm accountForm, PDFDocument doc, Element rootElement, HttpServletRequest request)
			throws ParserConfigurationException {

		BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext())
				.getLayoutBean(BDPdfConstants.DB_ACCOUNT_MONEY_TYPE_SUMMARY_PATH, request);
		BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request, accountForm);
		if (accountForm.isAsOfDateCurrent()) {
			doc.appendTextNode(rootElement, BDPdfConstants.AS_OF_DATE_REPORT_CURRENT, null);
			if (accountForm.getHasInvestments()) {
				setReportDetailsXMLElements(doc, rootElement, participantAccountVO);
			}
		}
		return doc.getDocument();

	}

	/**
	 * This method sets report details XML elements
	 * 
	 * @param doc
	 * @param rootElement
	 * @param participantAccountVO
	 */
	private void setReportDetailsXMLElements(PDFDocument doc, Element rootElement,
			ParticipantAccountVO participantAccountVO) {

		Element reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
		String erContrib = NumberRender.formatByPattern(participantAccountVO.getTotalEmployerContributionsAssets(),
				null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
		doc.appendTextNode(reportDetailElement, BDPdfConstants.ER_CONTRIB, erContrib);
		String totalContrib = NumberRender.formatByPattern(participantAccountVO.getTotalContributionsAssets(), null,
				BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
		doc.appendTextNode(reportDetailElement, BDPdfConstants.TOTAL_CONTRIB, totalContrib);

		for (ParticipantFundMoneyTypeTotalsVO erMoneyTypeTotals : participantAccountVO.getEmployerMoneyTypeTotals()) {

			Element erMoneyType = doc.createElement(BDPdfConstants.ER_MONEY_TYPE);
			doc.appendTextNode(erMoneyType, BDPdfConstants.MONEY_TYPE, erMoneyTypeTotals.getMoneyTypeName());
			String balance = NumberRender.formatByPattern(erMoneyTypeTotals.getBalance(), null,
					BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
			doc.appendTextNode(erMoneyType, BDPdfConstants.BALANCE, balance);
			doc.appendElement(reportDetailElement, erMoneyType);

		}
		doc.appendElement(rootElement, reportDetailElement);
	}

	@RequestMapping(value="/db/definedBenefitAccountMoneyTypeSummary/", method = { RequestMethod.GET
			})
	public String doExecute(@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
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
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		forward = super.doExecute(form, request, response);
		
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
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