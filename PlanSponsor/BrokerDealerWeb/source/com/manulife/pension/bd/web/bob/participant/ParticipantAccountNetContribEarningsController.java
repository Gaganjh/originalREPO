package com.manulife.pension.bd.web.bob.participant;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantNetContribEarningsVO;
import com.manulife.util.render.NumberRender;

/**
 * Action class for Participant Account - After-Tax tab
 * 
 * @author Saravana
 */
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountNetContribEarningsController extends ParticipantAccountCommonController {
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
		 forwards.put("input","/participant/participantAccountNetContribEarnings.jsp");
		 forwards.put("participantAccountNetContribEarnings","/participant/participantAccountNetContribEarnings.jsp");}

    /**
     * Constructor
     */
    public ParticipantAccountNetContribEarningsController() {
        super(ParticipantAccountNetContribEarningsController.class);
    }

    /* (non-Javadoc)
     * @see com.manulife.pension.bd.web.bob.participant.ParticipantAccountCommonAction#populateDetailedDownloadData(com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO, com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm)
     */
    @SuppressWarnings("unchecked")
    protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,HttpServletRequest request) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateNetContribEarningsDownloadData");
        }

        StringBuffer buff = new StringBuffer();
        Collection netContribEarningsDetails = participantAccountVO.getNetContribEarningsDetailsCollection();
        buff.append("After Tax Money").append(LINE_BREAK).append(LINE_BREAK);
       
        buff.append("After Tax Money Types");
        buff.append(COMMA).append("Net Contributions($)");
        buff.append(COMMA).append("Earnings($)");
        buff.append(LINE_BREAK).append(LINE_BREAK);
        if(form.getShowNonRothHeader()){
        	buff.append("Non-Roth").append(LINE_BREAK);

        	Iterator netContribEarningsDetailsIt = netContribEarningsDetails.iterator();
        	while (netContribEarningsDetailsIt.hasNext()){
        		ParticipantNetContribEarningsVO netContribEarningsItem = (ParticipantNetContribEarningsVO)netContribEarningsDetailsIt.next();
        		if(netContribEarningsItem.isNonRothMoneyTypeInd()){
        			buff.append(netContribEarningsItem.getMoneyTypeName()).append(COMMA);
        			buff.append(netContribEarningsItem.getNetContributions()).append(COMMA);
        			buff.append(netContribEarningsItem.getEarnings()).append(LINE_BREAK);
        		}
        	}
        }
        if(form.getShowRothHeader())
        {
        	buff.append("Roth").append(LINE_BREAK);
        	Iterator netContribEarningsDetailsIt = netContribEarningsDetails.iterator();
            while (netContribEarningsDetailsIt.hasNext()){
                ParticipantNetContribEarningsVO netContribEarningsItem = (ParticipantNetContribEarningsVO)netContribEarningsDetailsIt.next();
                if(netContribEarningsItem.isRothMoneyTypeInd()){
                buff.append(netContribEarningsItem.getMoneyTypeName()).append(COMMA);
                buff.append(netContribEarningsItem.getNetContributions()).append(COMMA);
                buff.append(netContribEarningsItem.getEarnings()).append(LINE_BREAK);
                }
            }
        	
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateNetContribEarningsDownloadData");
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
                            BDPdfConstants.PPT_ACCOUNT_NET_CONTRIB_EARNINGS_PATH, request);
        BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request, accountForm);
        if (accountForm.isAsOfDateCurrent()) {
            doc.appendTextNode(rootElement, BDPdfConstants.AS_OF_DATE_REPORT_CURRENT, null);
            int size = getNumberOfRowsInReport(participantAccountVO);
            maxRowsinPDF = accountForm.getCappedRowsInPDF();
            Iterator netContribEarningsDetailsIt = participantAccountVO.getNetContribEarningsDetailsCollection().iterator();
            for (int i = 0; i < size && rowCount <= maxRowsinPDF; i++) {
                ParticipantNetContribEarningsVO netContribEarningsItem = (ParticipantNetContribEarningsVO) netContribEarningsDetailsIt.next();
                setReportDetailsXMLElements(doc, rootElement, netContribEarningsItem);
                rowCount++;
            }
            if (accountForm.getPdfCapped()) {
                doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
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
    private void setReportDetailsXMLElements(PDFDocument doc, Element rootElement, ParticipantNetContribEarningsVO netContribEarningsItem) {       
       if(netContribEarningsItem.isNonRothMoneyTypeInd()){
    	Element reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL_NON_ROTH);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.MONEY_TYPE, netContribEarningsItem.getMoneyTypeName());
        String netContrib = NumberRender.formatByPattern(netContribEarningsItem.getNetContributions(), null, 
                CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.NET_CONTRIB, removeParanthesesAndPrefixMinus(netContrib));
                        
        String earnings = NumberRender.formatByPattern(netContribEarningsItem.getEarnings(), null, 
                CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
        doc.appendTextNode(reportDetailElement, BDPdfConstants.EARNINGS, removeParanthesesAndPrefixMinus(earnings));

        doc.appendElement(rootElement, reportDetailElement);
       }else
        {
        	Element reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL_ROTH);
            doc.appendTextNode(reportDetailElement, BDPdfConstants.MONEY_TYPE, netContribEarningsItem.getMoneyTypeName());
            String netContrib = NumberRender.formatByPattern(netContribEarningsItem.getNetContributions(), null, 
                    CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
            doc.appendTextNode(reportDetailElement, BDPdfConstants.NET_CONTRIB, removeParanthesesAndPrefixMinus(netContrib));
                            
            String earnings = NumberRender.formatByPattern(netContribEarningsItem.getEarnings(), null, 
                    CommonConstants.AMT_PATTERN_TWO_TWO_DECIMALS);
            doc.appendTextNode(reportDetailElement, BDPdfConstants.EARNINGS, removeParanthesesAndPrefixMinus(earnings));

            doc.appendElement(rootElement, reportDetailElement);
        }
    }
    
    /**
     * @See ParticipantAccountCommonAction#getTabName()
     */
    @Override
    public Integer getNumberOfRowsInReport(Object reportData) {
        ParticipantAccountVO participantAccountVO = (ParticipantAccountVO) reportData;
        int noOfRows = 0;
        if(participantAccountVO.getNetContribEarningsDetailsCollection() != null) {
            noOfRows += participantAccountVO.getNetContribEarningsDetailsCollection().size();   
        }
        return noOfRows;
    }
     
    /**
     * @See ParticipantAccountCommonAction#getTabName()
     */
    @Override
    protected String getTabName() {
        return BDConstants.AFTER_TAX_MONEY_CSV_NAME;
    }

    @RequestMapping(value ="/participant/participantAccountNetContribEarnings/",  method =  {RequestMethod.GET}) 
    public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	String forward=super.preExecute(form, request, response);
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
    	 forward=super.doExecute( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward);  
    }
    
    
}