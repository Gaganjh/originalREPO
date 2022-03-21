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
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeTotalsVO;
import com.manulife.util.render.NumberRender;

/**
 * Action class for Participant Account - Money Type Summary tab
 * 
 * @author Saravana
 */
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountMoneyTypeSummaryController extends ParticipantAccountCommonController {
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
		forwards.put("input","/participant/participantAccountMoneyTypeSummary.jsp");
		forwards.put("participantAccountMoneyTypeSummary","/participant/participantAccountMoneyTypeSummary.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
		}

    /**
     * Constructor
     */
    public ParticipantAccountMoneyTypeSummaryController() {
        super(ParticipantAccountMoneyTypeSummaryController.class);
    }
    @RequestMapping(value ="/participant/participantAccountMoneyTypeSummary/",  method =  {RequestMethod.GET}) 
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
    /* (non-Javadoc)
     * @see com.manulife.pension.bd.web.bob.participant.ParticipantAccountCommonAction#populateDetailedDownloadData(com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO, com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm)
     */
    protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,HttpServletRequest request) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDetailedDownloadData");
        }

        boolean showLoans = true;
        if ( !"YES".equals(participantAccountVO.getParticipantAccountDetailsVO().getShowLoanFeature() ))
            showLoans = false;
        
        StringBuffer buff = new StringBuffer();
        
        buff.append("Money Type Summary").append(LINE_BREAK).append(LINE_BREAK);
        
        // Section 4 money type summary for the report
        buff.append("Money Types");
        if ( showLoans )
            buff.append(COMMA).append(" Total Assets Excluding Loans($),Loan Assets($)");
        else
            buff.append(COMMA).append(" Total Assets($)");
        
        buff.append(LINE_BREAK).append(LINE_BREAK);
        
        // Employee contributions
        buff.append("Employee Contributions").append(COMMA);
        buff.append(participantAccountVO.getTotalEmployeeContributionsAssets());
        if ( showLoans )
            buff.append(COMMA).append(participantAccountVO.getTotalEmployeeContributionsLoanAssets());
        
        buff.append(LINE_BREAK);
        
        ParticipantFundMoneyTypeTotalsVO[] employeeMoneyTypeTotals = participantAccountVO.getEmployeeMoneyTypeTotals();
        
        for (int i=0; i<employeeMoneyTypeTotals.length; i++) {
            buff.append(employeeMoneyTypeTotals[i].getMoneyTypeName()).append(COMMA);
            buff.append(employeeMoneyTypeTotals[i].getBalance());
            if ( showLoans )
                buff.append(COMMA).append(employeeMoneyTypeTotals[i].getLoanBalance());
            
            buff.append(LINE_BREAK);
        }
        
        buff.append(LINE_BREAK);
        
        // Employer contributions
        buff.append("Employer Contributions").append(COMMA);
        buff.append(participantAccountVO.getTotalEmployerContributionsAssets());
        if ( showLoans )
            buff.append(COMMA).append(participantAccountVO.getTotalEmployerContributionsLoanAssets());
        
        buff.append(LINE_BREAK);
        
        ParticipantFundMoneyTypeTotalsVO[] employerMoneyTypeTotals = participantAccountVO.getEmployerMoneyTypeTotals();
        
        for (int i=0; i<employerMoneyTypeTotals.length; i++) {
            buff.append(employerMoneyTypeTotals[i].getMoneyTypeName()).append(COMMA);
            buff.append(employerMoneyTypeTotals[i].getBalance());
            if ( showLoans )
                buff.append(COMMA).append(employerMoneyTypeTotals[i].getLoanBalance());
            
            buff.append(LINE_BREAK);
        }
        
        buff.append(LINE_BREAK);        
        
        // Total contributions
        buff.append("Total").append(COMMA);
        buff.append(participantAccountVO.getTotalContributionsAssets());
        if ( showLoans )
            buff.append(COMMA).append(participantAccountVO.getTotalContributionsLoanAssets());      
        
        buff.append(LINE_BREAK);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDetailedDownloadData");
        }
        return buff.toString();     
    }
    
    /**
     * @See BDPdfAction#prepareDetailedXMLFromReport()
     */
    
    protected Document prepareDetailedXMLFromReport(ParticipantAccountVO participantAccountVO, ParticipantAccountForm accountForm,
              PDFDocument doc, Element rootElement, HttpServletRequest request) throws ParserConfigurationException {
        
        ParticipantAccountDetailsVO detailsVO = participantAccountVO.getParticipantAccountDetailsVO();
        
        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(
                            BDPdfConstants.PPT_ACCOUNT_MONEY_TYPE_SUMMARY_PATH, request);
        accountForm.setShowParticipantNewFooter(true);
        BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request, accountForm);
        if (accountForm.isAsOfDateCurrent()) {
            doc.appendTextNode(rootElement, BDPdfConstants.AS_OF_DATE_REPORT_CURRENT, null);
            boolean isShowLoanFeature =ParticipantAccountCommonController.STRING_YES.equals(detailsVO.getShowLoanFeature());
            if (isShowLoanFeature) {
                doc.appendTextNode(rootElement, BDPdfConstants.SHOW_LOAN_FEATURE, null);
            }
            if (accountForm.getHasInvestments()) {
                setReportDetailsXMLElements(doc, rootElement, participantAccountVO, isShowLoanFeature);  
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
     * @param isShowLoanFeature
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element rootElement, ParticipantAccountVO participantAccountVO,
            boolean isShowLoanFeature) {
        
        Element reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
        setTotalContributionsXMLElements(doc, reportDetailElement, participantAccountVO, isShowLoanFeature);
        for (ParticipantFundMoneyTypeTotalsVO eeMoneyTypeTotals : participantAccountVO.getEmployeeMoneyTypeTotals()) {
            setEmployeeBalanceXMLElements(doc, reportDetailElement, eeMoneyTypeTotals, isShowLoanFeature);
        }
        
        for (ParticipantFundMoneyTypeTotalsVO erMoneyTypeTotals : participantAccountVO.getEmployerMoneyTypeTotals()) {
            setEmployerBalanceXMLElements(doc, reportDetailElement, erMoneyTypeTotals, isShowLoanFeature);
        }
        doc.appendElement(rootElement, reportDetailElement);
        
    }
        

    /**
     * This method sets total contributions details XML elements of both
     * employee and employer
     * 
     * @param doc
     * @param reportDetailElement
     * @param participantAccountVO
     * @param isShowLoanFeature
     */
    private void setTotalContributionsXMLElements(PDFDocument doc, Element reportDetailElement, ParticipantAccountVO participantAccountVO,
            boolean isShowLoanFeature) {

        String eeContrib = NumberRender.formatByPattern(participantAccountVO.getTotalEmployeeContributionsAssets(), 
                null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.EE_CONTRIB, eeContrib);
        String erContrib = NumberRender.formatByPattern(participantAccountVO.getTotalEmployerContributionsAssets(), 
                        null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.ER_CONTRIB, erContrib);
        String totalContrib = NumberRender.formatByPattern(participantAccountVO.getTotalContributionsAssets(), 
                           null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.TOTAL_CONTRIB, totalContrib);
        if (isShowLoanFeature) {
         String eeContribLoan = NumberRender.formatByPattern(participantAccountVO.getTotalEmployeeContributionsLoanAssets(), 
                                null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
         doc.appendTextNode(reportDetailElement, BDPdfConstants.EE_CONTRIB_LOAN, eeContribLoan);
         String erContribLoan = NumberRender.formatByPattern(participantAccountVO.getTotalEmployerContributionsLoanAssets(), 
                                null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
         doc.appendTextNode(reportDetailElement, BDPdfConstants.ER_CONTRIB_LOAN, erContribLoan);
         String totalContribLoan = NumberRender.formatByPattern(participantAccountVO.getTotalContributionsLoanAssets(), 
                               null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
         doc.appendTextNode(reportDetailElement, BDPdfConstants.TOTAL_CONTRIB_LOAN, totalContribLoan);
        }
        
    }
        
    /**
     * This method sets employee balance details XML elements
     * 
     * @param doc
     * @param reportDetailElement
     * @param participantAccountVO
     * @param isShowLoanFeature
     */
    private void setEmployeeBalanceXMLElements(PDFDocument doc, Element reportDetailElement, ParticipantFundMoneyTypeTotalsVO eeMoneyTypeTotals,
            boolean isShowLoanFeature) {
        
        Element eeMoneyType = doc.createElement(BDPdfConstants.EE_MONEY_TYPE);
        doc.appendTextNode(eeMoneyType, BDPdfConstants.MONEY_TYPE, eeMoneyTypeTotals.getMoneyTypeName());
        String balance = NumberRender.formatByPattern(eeMoneyTypeTotals.getBalance(), 
                               null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
        doc.appendTextNode(eeMoneyType, BDPdfConstants.BALANCE, balance);
        if (isShowLoanFeature) {
            String loanBalance = NumberRender.formatByPattern(eeMoneyTypeTotals.getLoanBalance(), 
                                 null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(eeMoneyType, BDPdfConstants.LOAN_BALANCE, loanBalance);
        }
        doc.appendElement(reportDetailElement, eeMoneyType);
        
    }

    /**
     * This method sets employer balance details XML elements
     * 
     * @param doc
     * @param reportDetailElement
     * @param participantAccountVO
     * @param isShowLoanFeature
     */
    private void setEmployerBalanceXMLElements(PDFDocument doc, Element reportDetailElement, ParticipantFundMoneyTypeTotalsVO erMoneyTypeTotals,
            boolean isShowLoanFeature) {
        
        Element erMoneyType = doc.createElement(BDPdfConstants.ER_MONEY_TYPE);
        doc.appendTextNode(erMoneyType, BDPdfConstants.MONEY_TYPE, erMoneyTypeTotals.getMoneyTypeName());
        String balance = NumberRender.formatByPattern(erMoneyTypeTotals.getBalance(), 
                               null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
        doc.appendTextNode(erMoneyType, BDPdfConstants.BALANCE, balance);
        if (isShowLoanFeature) {
            String loanBalance = NumberRender.formatByPattern(erMoneyTypeTotals.getLoanBalance(), 
                                 null, BDConstants.AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(erMoneyType, BDPdfConstants.LOAN_BALANCE, loanBalance);
        }
        doc.appendElement(reportDetailElement, erMoneyType);
        
    }
        
    /**
     * @See ParticipantAccountCommonAction#getTabName()
     */
    @Override
    protected String getTabName() {
        return BDConstants.MONEY_TYPE_SUMMARY_CSV_NAME;
    }
    @Autowired
   	private BDValidatorFWInput bdValidatorFWInput;

   	@InitBinder
   	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
   		binder.bind(request);
   		binder.addValidators(bdValidatorFWInput);
   	}

	
    
   
}
