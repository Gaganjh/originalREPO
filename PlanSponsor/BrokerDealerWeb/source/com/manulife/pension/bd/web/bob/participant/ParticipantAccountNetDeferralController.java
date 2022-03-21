package com.manulife.pension.bd.web.bob.participant;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * Action class for Participant Account - Net EE Deferrals tab
 * 
 * @author Saravana
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantAccountForm"})

public class ParticipantAccountNetDeferralController extends ParticipantAccountCommonController {
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
		forwards.put("input","/participant/participantAccountNetDeferral.jsp");
		forwards.put("participantAccountNetDeferral","/participant/participantAccountNetDeferral.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
		}

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	FastDateFormat dateFormatter = FastDateFormat.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	
    /**
     * Constructor
     */
    public ParticipantAccountNetDeferralController() {
        super(ParticipantAccountNetDeferralController.class);
    }
    @RequestMapping(value ="/participant/participantAccountNetDeferral/",  method =  {RequestMethod.GET}) 
   	public String doExecute (@Valid @ModelAttribute("participantAccountForm") ParticipantAccountForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
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
   		String forwardPreExecute=super.preExecute(actionForm, request, response);
   		if(forwardPreExecute!=null){
   			return forwards.get("forwardPreExecute");
   		}
   		else{
   		String forward=super.doExecute( actionForm, request, response);
   		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
   		}
    }
   	

    /* (non-Javadoc)
     * @see com.manulife.pension.bd.web.bob.participant.ParticipantAccountCommonAction#populateDetailedDownloadData(com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO, com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm)
     */
    protected String populateDetailedDownloadData(ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDetailedDownloadData");
        }
        StringBuffer buff =new StringBuffer();
        
        buff.append("EE Deferrals").append(LINE_BREAK).append(LINE_BREAK);
        
        if (participantAccountVO.getParticipantAccountDetailsVO()
                .isNetEEDeferralContributionsAvailable()) {
            buff.append("Net employee contribution").append(COMMA);
            buff.append(
                    escapeField(NumberFormat.getCurrencyInstance().format(
                            participantAccountVO.getParticipantAccountDetailsVO()
                                    .getNetEEDeferralContributions()))).append(LINE_BREAK);
        } else {
            buff.append("Apollo error");
        }
	 buff.append("Maximum hardship amount").append(COMMA);
             buff.append(
                     escapeField(NumberFormat.getCurrencyInstance().format(
                             participantAccountVO.getParticipantAccountDetailsVO()
                                     .getMaximumHardshipAmount()))).append(LINE_BREAK); 
		
        if(form.getDeferralContributionText()!=null){
        	buff.append("Current Deferral on File").append(COMMA);
        	buff.append(form.getDeferralContributionText());
        	buff.append(LINE_BREAK);
        }
        
        ParticipantDeferralVO deferralVO = participantAccountVO.getParticipantAccountDetailsVO().getParticipantDeferralVO();
        
        if (deferralVO.isAuto() || deferralVO.isSignUp()) {   	
            if (deferralVO.isParticipantACIOn() == false) {
                buff.append("Scheduled deferral increase").append(COMMA).append("Off").append(LINE_BREAK);
            } else {
                buff.append("Scheduled deferral increase").append(LINE_BREAK);
                buff.append("Date of Next Increase").append(COMMA).append(
                        dateFormatter.format(deferralVO.getDateOfNextIncreaseAsDate())).append(
                        LINE_BREAK);
                buff.append("Next Increase").append(COMMA).append(form.getNextIncreaseValue())
                        .append(LINE_BREAK);
                buff.append("Personal rate limit").append(COMMA)
                        .append(form.getPersonalRateLimit());
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

        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(BDPdfConstants.PPT_ACCOUNT_NET_DEFERRAL_PATH, request);
        accountForm.setShowParticipantNewFooter(true);
        BDPdfHelper.setLayoutPageSpecificData(bean, doc, rootElement, request, accountForm);

        if(accountForm.isAsOfDateCurrent()) {
            
            if(accountForm.getHasInvestments()) {
                
                ParticipantAccountDetailsVO accDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();
                Element reportDetailElement = null;
                if (accDetailsVO.isNetEEDeferralContributionsAvailable()) {
   
                    reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
                    LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();

                    PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, reportDetailElement, doc, layoutPageBean.getBody1());
               
                    String netEEContrib = NumberRender.formatByPattern(accDetailsVO.getNetEEDeferralContributions(), null, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
					String maximumHardshipAmount = NumberRender.formatByPattern(accDetailsVO.getMaximumHardshipAmount(), null, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
					
                    doc.appendTextNode(reportDetailElement, BDPdfConstants.EE_CONTRIB, netEEContrib);
					doc.appendTextNode(reportDetailElement, BDPdfConstants.MAXIMUM_HARDSHIP_AMOUNT, maximumHardshipAmount);
                    doc.appendTextNode(reportDetailElement, BDPdfConstants.DEFERRAL_CONTRIB_TEXT, accountForm.getDeferralContributionText());
                    
                    if(accDetailsVO.getParticipantDeferralVO().isAuto() || accDetailsVO.getParticipantDeferralVO().isSignUp()) {
                    	String aciTitle = null;
                    	if(accDetailsVO.getParticipantDeferralVO().isAuto()) {
              			   aciTitle = "JH EZincrease";
              		    } else {
              			   aciTitle = "Scheduled deferral change";
              		    }
                    	doc.appendTextNode(reportDetailElement, "aciTitle", aciTitle);
                        doc.appendTextNode(reportDetailElement, BDPdfConstants.CSF_ACI_ON, null);

                        if(accDetailsVO.getParticipantDeferralVO().isParticipantACIOn()) {
                            doc.appendTextNode(reportDetailElement, BDPdfConstants.NEXT_INCREASE_DATE, accDetailsVO.getParticipantDeferralVO().getDateOfNextIncrease());
                            doc.appendTextNode(reportDetailElement, BDPdfConstants.NEXT_INCREASE_VALUE, accountForm.getNextIncreaseValue());
                            doc.appendTextNode(reportDetailElement, BDPdfConstants.PERSONAL_RATE_LIMIT, accountForm.getPersonalRateLimit());
                        }
                      }
                    doc.appendElement(rootElement, reportDetailElement);
                           
                }
  
            }
            
        }
        
        return doc.getDocument();
        
    }
     
    /**
     * @See ParticipantAccountCommonAction#getTabName()
     */
    @Override
    protected String getTabName() {
        return BDConstants.EE_DEFERRALS_CSV_NAME;
    }
    @Autowired
   	private BDValidatorFWInput bdValidatorFWInput;

   	@InitBinder
   	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
   		binder.bind(request);
   		binder.addValidators(bdValidatorFWInput);
   	}
    
}