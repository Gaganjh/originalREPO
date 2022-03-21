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
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDPdfHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeDetailVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.employee.EmployeeConstants.MoneyTypeGroup;
import com.manulife.util.render.NumberRender;

/**
 * Action class for Participant Account - Money Type Details tab
 * 
 * @author Saravana
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantAccountForm"})
public class ParticipantAccountMoneyTypeDetailsController extends ParticipantAccountCommonController {
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
		forwards.put("participantAccountMoneyTypeDetails","/participant/participantAccountMoneyTypeDetails.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
	}

    /**
     * Constructor
     */
    public ParticipantAccountMoneyTypeDetailsController() {
        super(ParticipantAccountMoneyTypeDetailsController.class);
    }

    
    /* (non-Javadoc)
     * @see com.manulife.pension.bd.web.bob.participant.ParticipantAccountCommonAction#populateDetailedDownloadData(com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO, com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm)
     */
    protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDetailedDownloadData");
        }

        DecimalFormat unitsHeldFormatter = new DecimalFormat("#0.000000");
        DecimalFormat percentFormatter = new DecimalFormat("#0.00");
        DecimalFormat unitValueFormatter = new DecimalFormat("#0.00");
        
        StringBuffer buff = new StringBuffer();

        buff.append("Money Type Details").append(LINE_BREAK).append(LINE_BREAK);   
        
        buff.append("Funds Organized by").append(COMMA);
        if("2".equals(form.getFundsOrganizedBy()))
        buff.append("Asset Class");
        else if("3".equals(form.getFundsOrganizedBy())){
            buff.append("Risk/Return Category");    
        }
        buff.append(LINE_BREAK).append(LINE_BREAK);
        
        // Section 4 Money Type Details collection for the report
        buff.append("Investment Option,Class,Number Of Units,Unit Value($) / Interest Rate,Balance Employee Subtotal($), Balance Employer Subtotal($),Balance($),% Of Total").append(LINE_BREAK);    
        InvestmentOptionVO [] options = participantAccountVO.getOrganizedParticipantFunds();
        for(int i=0;i<options.length;i++) {
            InvestmentOptionVO optionVO = options[i];
            
            if(optionVO.getParticipantFundSummaryArray().length==0) {
                continue;
            }
            buff.append(LINE_BREAK).append(optionVO.getCategory().getCategoryDesc()).append(LINE_BREAK);
            
            ParticipantFundSummaryVO [] summaries = optionVO.getParticipantFundSummaryArray();
            for(int j=0;j<summaries.length;j++) {
                ParticipantFundSummaryVO summVO = summaries[j];
                
                // first line is the fund aggregate level
                buff.append(summVO.getFundName()).append(COMMA);
                if("PB".equals(optionVO.getCategory().getCategoryCode())) {
                    buff.append("-").append(COMMA);
                    buff.append("-").append(COMMA);
                    buff.append("-").append(COMMA);
                } else {
                    buff.append(summVO.getFundClass()).append(COMMA);
	                if(summVO.getFundTotalNumberOfUnitsHeld()==0) {
	                	buff.append("-").append(COMMA);
	                    if (summVO.getFundTotalCompositeRate() > 0) {
	                        buff.append(percentFormatter.format(summVO.getFundTotalCompositeRate())).append(COMMA);
	                    } else {
	                        buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
	                    }
	                } else {
	                    buff.append(unitsHeldFormatter.format(summVO.getFundTotalNumberOfUnitsHeld())).append(COMMA);
	                    buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
	                }
                }
                
                buff.append(summVO.getEmployeeBalance()).append(COMMA).append(summVO.getEmployerBalance()).append(COMMA);
                buff.append(summVO.getFundTotalBalance()).append(COMMA);
                buff.append(percentFormatter.format(summVO.getFundTotalPercentageOfTotal()*100.0d)).append(LINE_BREAK);
                
                // next line is the Money Type Details level
                ParticipantFundMoneyTypeDetailVO[] moneyTypeDetails = summVO.getFundMoneyTypeDetails();
                
                for (int k=0; k < moneyTypeDetails.length; k++)
                {
                    buff.append(moneyTypeDetails[k].getMoneyTypeName()).append(COMMA);
                    buff.append(COMMA);
                    if("PB".equals(optionVO.getCategory().getCategoryCode())) {
                        buff.append("-").append(COMMA);
                        buff.append("-").append(COMMA);
                    } else {
	                    if (summVO.getFundTotalNumberOfUnitsHeld()==0)
	                    {
	                        if (summVO.getFundTotalCompositeRate() > 0) 
	                            buff.append("-").append(COMMA);
	                        else 
	                            buff.append(unitValueFormatter.format(moneyTypeDetails[k].getNumberOfUnitsHeld())).append(COMMA).append(COMMA);
	                    } else {
	                        buff.append(unitsHeldFormatter.format(moneyTypeDetails[k].getNumberOfUnitsHeld())).append(COMMA);
	                        buff.append(unitValueFormatter.format(summVO.getFundUnitValue())).append(COMMA);
	                    }
                    }
                    
                    if ( moneyTypeDetails[k].getMoneyType().equals("EE") )
                        buff.append(moneyTypeDetails[k].getBalance()).append(COMMA);
                    else
                    	buff.append("-").append(COMMA);
                    
                    if ( moneyTypeDetails[k].getMoneyType().equals("ER") )
                        buff.append(moneyTypeDetails[k].getBalance()).append(COMMA);
                    else
                    	buff.append("-").append(COMMA);
                    buff.append("-").append(COMMA);
                    buff.append("-").append(COMMA).append(LINE_BREAK);
                }
            }
        }        
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDetailedDownloadData");
        }
        return buff.toString();     
    }
    
    /**
     * @See BDPdfAction#prepareDetailedXMLFromReport()
     */
  
    public Document prepareDetailedXMLFromReport(ParticipantAccountVO participantAccountVO, ParticipantAccountForm accountForm,
           PDFDocument doc, Element rootElement, HttpServletRequest request) throws ParserConfigurationException {

        int rowCount = 1;
        int maxRowsinPDF;
        
        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(
                            BDPdfConstants.PPT_ACCOUNT_MONEY_TYPE_DETAILS_PATH, request);
        accountForm.setShowParticipantNewFooter(true);
        BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request, accountForm);
        doc.appendTextNode(rootElement, BDPdfConstants.ORGANIZED_BY, accountForm.getFundsOrganizedBy());
        if(accountForm.isAsOfDateCurrent()) {
            doc.appendTextNode(rootElement, BDPdfConstants.AS_OF_DATE_REPORT_CURRENT, null);
            if(accountForm.getHasInvestments()) {
                
                Element reportDetailsElement = doc.createElement(BDPdfConstants.REPORT_DETAILS);
                Element reportDetailElement = null;
                InvestmentOptionVO [] options = participantAccountVO.getOrganizedParticipantFunds();
                if (options != null) {
                    maxRowsinPDF = accountForm.getCappedRowsInPDF();
                    for (int i = 0; i < options.length && rowCount <= maxRowsinPDF; i++) {
            
                        InvestmentOptionVO optionVO = options[i];
                        if(optionVO != null && optionVO.getParticipantFundSummaryArray().length == 0) { 
                            continue;
                        }
    
                        Element fundDetailElement;
                        reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
                        
                        doc.appendTextNode(reportDetailElement, BDPdfConstants.FUND_CATEGORY, optionVO.getCategory().getCategoryDesc());
                        //To display * in Guaranteed Income feature category in PDF report
                        if(BDConstants.GIFL_RISK_CATEGORY_CODE.equals(optionVO.getCategory().getCategoryCode()))
                        	doc.appendTextNode(reportDetailElement, BDPdfConstants.GIFL_CATEGORY, optionVO.getCategory().getCategoryDesc());
                        
                        
                        for(ParticipantFundSummaryVO summaryVO : optionVO.getParticipantFundSummaryArray()) {
                            
                            fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                            setFundDetailsXMLElements(doc, fundDetailElement, summaryVO, optionVO);
                            doc.appendElement(reportDetailElement, fundDetailElement);
                            rowCount++;
                        }
            
                        doc.appendElement(reportDetailsElement, reportDetailElement);
                    }
                }
                
                doc.appendElement(rootElement, reportDetailsElement);
            }
        }
        
        if (accountForm.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        
        return doc.getDocument();
        
    }

    /**
     * @See BDPdfAction#getNumberOfRowsInReport()
     */
    @Override
    public Integer getNumberOfRowsInReport(Object reportData) {
        ParticipantAccountVO participantAccountVO = (ParticipantAccountVO) reportData;
        int noOfRows = 0;
        if (participantAccountVO.getParticipantFundsByRisk() != null) { 
            for (InvestmentOptionVO optionVO : participantAccountVO.getOrganizedParticipantFunds()) {
                noOfRows += optionVO.getParticipantFundSummaryArray().length;
            }
        }   
        return noOfRows;
    }

    /**
     * @See ParticipantAccountCommonAction#getTabName()
     */
    @Override
    protected String getTabName() {
        return BDConstants.MONEY_TYPE_DETAILS_CSV_NAME;
    }
    
    /**
     * This method sets fund details XML elements
     * 
     * @param doc
     * @param fundDetailElement
     * @param summaryVO
     * @param optionVO
     */
    private void setFundDetailsXMLElements(PDFDocument doc, Element fundDetailElement, ParticipantFundSummaryVO summaryVO,
            InvestmentOptionVO optionVO) {
        
        if (summaryVO != null) {
            doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_NAME, summaryVO.getFundName());
           
            if(Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT.equals(optionVO.getCategory().getCategoryCode())) {
            	doc.appendTextNode(fundDetailElement, BDPdfConstants.PB_FUND_CATEGORY, null);
            } else {
            	doc.appendTextNode(fundDetailElement, BDPdfConstants.FUND_CLASS, String.valueOf(summaryVO.getFundClass()));
                String fundUnitValue = NumberRender.formatByPattern(summaryVO.getFundUnitValue(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
                if(summaryVO.getFundTotalNumberOfUnitsHeld() == 0.00) {
                   
                    if(summaryVO.getFundTotalCompositeRate() != 0.00) {
                        String compositeRate = NumberRender.formatByPattern(summaryVO.getFundTotalCompositeRate(), ZERO_STRING, RATE_FORMAT, 4, BigDecimal.ROUND_HALF_DOWN);
                        doc.appendTextNode(fundDetailElement, BDPdfConstants.COMPOSITE_RATE, compositeRate);
                    }
                    else {
                        doc.appendTextNode(fundDetailElement, BDPdfConstants.UNIT_VALUE, fundUnitValue);
                    }
    
                }
                else {
                    
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.UNIT_VALUE, fundUnitValue);
                    
                    String noOfUnits = NumberRender.formatByPattern(summaryVO.getFundTotalNumberOfUnitsHeld(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_SIX_DECIMALS, 6, BigDecimal.ROUND_HALF_DOWN);
                    doc.appendTextNode(fundDetailElement, BDPdfConstants.NO_OF_UNITS, noOfUnits);
                    
                }
               
            }
            String eeBalanceFund = NumberRender.formatByPattern(summaryVO.getEmployeeBalance(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.EE_BALANCE_FUND, eeBalanceFund);
            
            String erBalanceFund = NumberRender.formatByPattern(summaryVO.getEmployerBalance(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.ER_BALANCE_FUND, erBalanceFund);
            
            String totalBalance = NumberRender.formatByPattern(summaryVO.getFundTotalBalance(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.TOTAL_BALANCE, totalBalance);
            
            String totalPercent = NumberRender.formatByPattern(summaryVO.getFundTotalPercentageOfTotal(), DEFAULT_VALUE_ZERO_PERCENT, PERCENT_TWO_DECIMAL_FORMAT, 4, BigDecimal.ROUND_HALF_DOWN);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.TOTAL_PERCENT, totalPercent);     
        }

        for(ParticipantFundMoneyTypeDetailVO moneyTypeVO: summaryVO.getFundMoneyTypeDetails()) {   
            Element moneyTypes = doc.createElement(BDPdfConstants.MONEY_TYPES);
            setMoneyTypeDetailsXMLElements(doc, moneyTypes, summaryVO, optionVO, moneyTypeVO);   
            doc.appendElement(fundDetailElement, moneyTypes);
        }
        
    }
    
    /**
     * This method sets money type details XML elements
     * 
     * @param doc
     * @param moneyTypes
     * @param summaryVO
     * @param optionVO
     * @param moneyTypeVO
     */
    private void setMoneyTypeDetailsXMLElements(PDFDocument doc, Element moneyTypes, ParticipantFundSummaryVO summaryVO,
            InvestmentOptionVO optionVO, ParticipantFundMoneyTypeDetailVO moneyTypeVO) {
        
        if (moneyTypeVO != null) {
            doc.appendTextNode(moneyTypes, BDPdfConstants.MONEY_TYPE, moneyTypeVO.getMoneyTypeName());
            if(Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT.equals(optionVO.getCategory().getCategoryCode())) {
            	doc.appendTextNode(moneyTypes, BDPdfConstants.PB_FUND_CATEGORY, null);
            } else {
            
                if(moneyTypeVO.getNumberOfUnitsHeld() != 0.00) {  
            
                    if (summaryVO != null) {
                        String unitValue = NumberRender.formatByPattern(summaryVO.getFundUnitValue(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
                        doc.appendTextNode(moneyTypes, BDPdfConstants.UNIT_VALUE, unitValue);
                    }  
                    
                    String noOfUnits = NumberRender.formatByPattern(moneyTypeVO.getNumberOfUnitsHeld(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_SIX_DECIMALS, 6, BigDecimal.ROUND_HALF_DOWN);
                    doc.appendTextNode(moneyTypes, BDPdfConstants.NO_OF_UNITS, noOfUnits);
                    
                }
                else {
                	if (moneyTypeVO.getCompositeRate() != 0.00) {
                		doc.appendTextNode(moneyTypes, BDPdfConstants.COMPOSITE_RATE, BDConstants.NO_RULE);
                	}
                }
               
            }
            if(moneyTypeVO.getMoneyType().equals(MoneyTypeGroup.MONEY_TYPE_EE)) {
                String eeBalanceMoneyType = NumberRender.formatByPattern(moneyTypeVO.getBalance(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(moneyTypes, BDPdfConstants.EE_BALANCE, eeBalanceMoneyType);
            }
            else if(moneyTypeVO.getMoneyType().equals(MoneyTypeGroup.MONEY_TYPE_ER)) {
                String erBalanceMoneyType = NumberRender.formatByPattern(moneyTypeVO.getBalance(), DEFAULT_VALUE_ZERO, AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(moneyTypes, BDPdfConstants.ER_BALANCE, erBalanceMoneyType);
            }
        }
        
    }
    @RequestMapping(value ="/participant/participantAccountMoneyTypeDetails/",  method =  {RequestMethod.GET}) 
	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	String forward=super.preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)){
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.getSession(false).getAttribute("account");
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
		 forward=super.doExecute( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
 }
	
    @Autowired
   	private BDValidatorFWInput bdValidatorFWInput;

   	@InitBinder
   	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
   		binder.bind(request);
   		binder.addValidators(bdValidatorFWInput);
   	}    
    
}