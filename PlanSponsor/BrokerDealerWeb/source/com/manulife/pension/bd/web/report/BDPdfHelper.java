package com.manulife.pension.bd.web.report;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.participant.ParticipantAccountForm;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class contains BD specific utility methods needed for PDF generation functionality.
 * 
 * @author Ramkumar
 * 
 */
public class BDPdfHelper extends PdfHelper {
	
    public static final Logger logger = Logger.getLogger(BDPdfHelper.class);

	/**
	 * This method sets specific XML elements common for Participant Account and DB Account reports
	 * 
	 * @param bean
	 * @param doc
	 * @param rootElement
	 * @param request
	 * @param accountForm
	 */
	public static void setLayoutPageSpecificData(BDLayoutBean bean, PDFDocument doc, Element rootElement, 
	       HttpServletRequest request, ParticipantAccountForm accountForm) {

        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        Location location = ApplicationHelper.getRequestContentLocation(request);
        
        setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);
        setRothMessageElement(doc, rootElement, currentContract);
        
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, 
                DateRender.formatByPattern(new Date(Long.parseLong(accountForm.getSelectedAsOfDate())), null, RenderConstants.MEDIUM_MDY_SLASHED));
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        convertIntoDOM(PdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);      
        String bodyHeader2 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY2_HEADER, null);
        convertIntoDOM(PdfConstants.BODY_HEADER2, rootElement, doc, bodyHeader2);
        
        if (BDPdfConstants.PPT_ACCOUNT_AFTER_TAX_MONEY_TAB_WEB_INF_PATH.equals(bean.getBody())) {
            // Set Roth footnote only for NetContribEarnings tab
            String rothFootnote = ContentHelper.getContentText(BDContentConstants.PS_PARTICIPANT_ACCOUNT_EARNINGS_FOOTNOTE, ContentTypeManager.instance().PAGE_FOOTNOTE, null);
           if(accountForm.isShowMultileRothFootnote()){
            String multipleRothFootnote = ContentHelper.getContentText(BDContentConstants.MULTIPLE_ROTH_FOOTNOTES, ContentTypeManager.instance().PAGE_FOOTNOTE, null);
            convertIntoDOM(PdfConstants.MULTIPLE_FOOTNOTE_ROTH, rootElement, doc, multipleRothFootnote);
           }
           convertIntoDOM(PdfConstants.FOOTNOTE_ROTH, rootElement, doc, rothFootnote);
        }
        if (accountForm.isShowGiflFootnote()) {
            // Set GIFL footnote
            setFootnoteGIFLXMLElement(doc, rootElement);
        }
        if (accountForm.getShowManagedAccount()) {
            // Set Managed Account footnote
            setFootnoteMAXMLElement(doc, rootElement);
        }
        if (accountForm.getShowPba()) {
        	// Set PBA footnote
        	if(accountForm.isShowParticipantNewFooter()){
        		setFootnotePBAXMLElementNew(doc, rootElement);
        	}
        	else{
        		setFootnotePBAXMLElement(doc, rootElement);
        	}
        }
        if(accountForm.isShowParticipantNewFooter()) {
        	setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                    BDContentConstants.BD_PARTICIPANT_REPORTS_GLOBAL_DISCLOSURE);
        }else {
        	setFooterXMLElements(layoutPageBean, doc, rootElement, location,
        			BDContentConstants.BD_GLOBAL_DISCLOSURE);
        }
        
        accountForm.setShowParticipantNewFooter(false);
	}

	 /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public static void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement, HttpServletRequest request) {
        
        Collection infoMessageCollection = (Collection) request.getAttribute(BDConstants.INFO_MESSAGES);
        setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
        
    }
    /**
     * This sets GIFL footnote XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setFootnoteGIFLXMLElement(PDFDocument pdfDoc, Element rootElement) {
        String footnoteGIFL = ContentHelper.getContentText(
        		BDContentConstants.BDW_PA_GIFL_FOOTNOTE,
                ContentTypeManager.instance().PAGE_FOOTNOTE, null);  
        // Delete ANSI code present within <sup> tags used for on-line page because FOP knows Unicode only and
        // Get Unicode equivalent for that ANSI code in XSL for displaying in PDF
        footnoteGIFL = footnoteGIFL.replaceAll("<sup>.*</sup>", PdfConstants.BLANK_STRING);
        // Delete the outermost opening and closing <p> tags to avoid displaying the footnote on the next line of that superscript symbol
        // Because <p> tag is getting formatted into new line in XSL
        footnoteGIFL = StringUtils.removeStart(footnoteGIFL, "<p>");
        footnoteGIFL = StringUtils.removeEnd(StringUtils.trim(footnoteGIFL), "</p>");
        convertIntoDOM(PdfConstants.FOOTNOTE_GIFL, rootElement, pdfDoc, footnoteGIFL);
    }
    /**
     * This sets Managed Acoount footnote XML element common for reports
     * 
     * @param pdfDoc - pdfDoc parameter
     * @param rootElement - rootElement parameter
     */
    public static void setFootnoteMAXMLElement(PDFDocument pdfDoc, Element rootElement) {
        String footnoteMA = ContentHelper.getContentText(
        		BDContentConstants.MA_FOOTNOTE,
                ContentTypeManager.instance().PAGE_FOOTNOTE, null);  
        // Delete ANSI code present within <sup> tags used for on-line page because FOP knows Unicode only and
        // Get Unicode equivalent for that ANSI code in XSL for displaying in PDF
        footnoteMA = footnoteMA.replaceAll("<sup>.*</sup>", PdfConstants.BLANK_STRING);
        // Delete the outermost opening and closing <p> tags to avoid displaying the footnote on the next line of that superscript symbol
        // Because <p> tag is getting formatted into new line in XSL
        footnoteMA = StringUtils.removeStart(footnoteMA, "<p>");
        footnoteMA = StringUtils.removeEnd(StringUtils.trim(footnoteMA), "</p>");
        convertIntoDOM(PdfConstants.FOOTNOTE_MA, rootElement, pdfDoc, footnoteMA);
    }
}
