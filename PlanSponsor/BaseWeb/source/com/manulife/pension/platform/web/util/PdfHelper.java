package com.manulife.pension.platform.web.util;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;

/**
 * This class contains utility methods needed for PDF generation functionality.
 * 
 * @author Ramkumar
 *
 */
public class PdfHelper {

    public static final Logger logger = Logger.getLogger(PdfHelper.class);

    /**
     * This sets footer, footnotes and disclaimer XML elements common for reports
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param params
     */
    public static void setFooterXMLElements(LayoutPage layoutPageBean,
            PDFDocument pdfDoc,
            Element rootElement, Location location, int globalDisclosureContentId,
            String[]... params) {
        
        // Checking the size of params and assigning accordingly 
        String[] footerParams = (params.length > 0) ? params[0] : null;
        String[] footnotesParams = (params.length > 1) ? params[1] : null;
        String[] disclaimerParams = (params.length > 2) ? params[2] : null;

        // FootNotes - start
        String footer = ContentUtility.getPageFooter(layoutPageBean, footerParams);
        String footnotes = ContentUtility.getPageFootnotes(layoutPageBean, footnotesParams, -1);
        String disclaimer = ContentUtility.getPageDisclaimer(layoutPageBean, disclaimerParams, -1);
  
        convertIntoDOM(PdfConstants.FOOTER, rootElement, pdfDoc, footer);
        if (StringUtils.isNotBlank(footnotes)) {
        	convertIntoDOM(PdfConstants.FOOTNOTES, rootElement, pdfDoc, footnotes);
        }
        convertIntoDOM(PdfConstants.DISCLAIMER, rootElement, pdfDoc, disclaimer);

        setGlobalDisclosureXMLElement(pdfDoc, rootElement, location, globalDisclosureContentId);

    }

    /**
     * This sets Global Disclosure XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setGlobalDisclosureXMLElement(PDFDocument pdfDoc, Element rootElement,
            Location location, int globalDisclosureContentId) {
        
        String globalDisclosureText = ContentHelper.getContentText(globalDisclosureContentId,
                ContentTypeManager.instance().MISCELLANEOUS, location);
        convertIntoDOM(PdfConstants.GLOBAL_DISCLOSURE, rootElement, pdfDoc, globalDisclosureText);

    }

    /**
     * This sets PBA footnote XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setFootnotePBAXMLElement(PDFDocument pdfDoc, Element rootElement) {
        String footnotePBA = ContentHelper.getContentText(
                CommonContentConstants.FIXED_FOOTNOTE_PBA,
                ContentTypeManager.instance().PAGE_FOOTNOTE, null);  
        // Delete ANSI code present within <sup> tags used for on-line page because FOP knows Unicode only and
        // Get Unicode equivalent for that ANSI code in XSL for displaying in PDF
        footnotePBA = footnotePBA.replaceAll("<sup>.*</sup>", PdfConstants.BLANK_STRING);
        // Delete the outermost opening and closing <p> tags to avoid displaying the footnote on the next line of that superscript symbol
        // Because <p> tag is getting formatted into new line in XSL
        footnotePBA = StringUtils.removeStart(footnotePBA, "<p>");
        footnotePBA = StringUtils.removeEnd(StringUtils.trim(footnotePBA), "</p>");
        convertIntoDOM(PdfConstants.FOOTNOTE_PBA, rootElement, pdfDoc, footnotePBA);
    }
    
    public static void setFootnotePBAXMLElementNew(PDFDocument pdfDoc, Element rootElement) {
        String footnotePBA = ContentHelper.getContentText(
                CommonContentConstants.FIXED_FOOTNOTE_PBA_FOR_PARTICIPANT_REPORTS,
                ContentTypeManager.instance().PAGE_FOOTNOTE, null);  
        // Delete ANSI code present within <sup> tags used for on-line page because FOP knows Unicode only and
        // Get Unicode equivalent for that ANSI code in XSL for displaying in PDF
        footnotePBA = footnotePBA.replaceAll("<sup>.*</sup>", PdfConstants.BLANK_STRING);
        // Delete the outermost opening and closing <p> tags to avoid displaying the footnote on the next line of that superscript symbol
        // Because <p> tag is getting formatted into new line in XSL
        footnotePBA = StringUtils.removeStart(footnotePBA, "<p>");
        footnotePBA = StringUtils.removeEnd(StringUtils.trim(footnotePBA), "</p>");
        convertIntoDOM(PdfConstants.FOOTNOTE_PBA, rootElement, pdfDoc, footnotePBA);
    }

    /**
     * This sets Logo, Page Name, Contract Details, Intro-1, Intro-2 XML elements common for reports
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setIntroXMLElements(LayoutPage layoutPageBean,
            PDFDocument doc, Element rootElement, Contract currentContract) {

        setLogoAndPageName(layoutPageBean, doc, rootElement);

        String contractName = currentContract.getCompanyName();
        String contractNumber = String.valueOf(currentContract.getContractNumber());
        doc.appendTextNode(rootElement, PdfConstants.CONTRACT_NAME, contractName);
        doc.appendTextNode(rootElement, PdfConstants.CONTRACT_NUMBER, contractNumber);       
        setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);
    }

    /**
     * This method sets the Logo and the Page Name.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setLogoAndPageName(LayoutPage layoutPageBean,
            PDFDocument pdfDoc,
            Element rootElement) {
        StringBuffer logoPath = new StringBuffer();
        // Logo will be the same for both US and NY because, they both see the same page
        // with the same logo.
        logoPath.append(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX + CommonConstants.JHRPS_LOGO_FILE));
        pdfDoc.appendTextNode(rootElement, PdfConstants.JH_LOGO_PATH, logoPath.toString());

        String pageName = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.PAGE_NAME, null);
        convertIntoDOM(PdfConstants.PAGE_NAME, rootElement, pdfDoc, pageName);
    }

    /**
     * This sets Introduction-1, Introduction-2 XML elements.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setIntro1Intro2XMLElements(LayoutPage layoutPageBean,
            PDFDocument pdfDoc,
            Element rootElement) {

        String intro1Text = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.INTRO1_TEXT, null);
        String intro2Text = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.INTRO2_TEXT, null);

        convertIntoDOM(PdfConstants.INTRO1_TEXT, rootElement, pdfDoc, intro1Text);
        convertIntoDOM(PdfConstants.INTRO2_TEXT, rootElement, pdfDoc, intro2Text);
        
    }
    
	/**
	 * This sets the fee waiver disclsoure
	 * 
	 * @param doc
	 * @param rootElement
	 * @param request
	 */
	public static void setFeeWaiverDisclosure(PDFDocument pdfDoc,
			Element rootElement) {
		String feeWaiverDisclosure = ContentHelper.getContentText(CommonContentConstants.FEE_WAIVER_DISCLOSURE_TEXT, ContentTypeManager.instance().DISCLAIMER, null);
		convertIntoDOM(PdfConstants.FEE_WAIVER_DISCLOSURE, rootElement, pdfDoc, feeWaiverDisclosure);

	}
    
	/**
	 * This sets the Generic fund disclosure
	 * 
	 * @param doc
	 * @param rootElement
	 * @param request
	 */
	public static void setGenericFundDisclosure(PDFDocument pdfDoc,
			Element rootElement) {
		String genericFundDisclosure = ContentHelper.getContentText(CommonContentConstants.FAP_GENERIC_VIEW_DISCLOSURE, ContentTypeManager.instance().DISCLAIMER, null);
		convertIntoDOM(PdfConstants.GENERIC_OR_CONTRACT_FUNDS_DISCLOSURE, rootElement, pdfDoc, genericFundDisclosure);
	}
    
	/**
	 * This sets the Contract fund disclosure
	 * 
	 * @param doc
	 * @param rootElement
	 * @param request
	 */
	public static void setContractFundDisclosure(PDFDocument pdfDoc,
			Element rootElement) {
		String contractFundDisclosure = ContentHelper.getContentText(CommonContentConstants.FAP_CONTRACT_VIEW_DISCLOSURE, ContentTypeManager.instance().DISCLAIMER, null);
		convertIntoDOM(PdfConstants.GENERIC_OR_CONTRACT_FUNDS_DISCLOSURE, rootElement, pdfDoc, contractFundDisclosure);
	}
	
	/**
	 * This sets the Restricted Funds disclosure
	 * 
	 * @param doc
	 * @param rootElement
	 */
	public static void setRestrictedFundsDisclosure(PDFDocument pdfDoc,
			Element rootElement) {
		String restrictedFundsDisclosure = ContentHelper.getContentText(CommonContentConstants.MERRILL_RESRICTED_FUNDS_CONTENT, ContentTypeManager.instance().DISCLAIMER, null);
		convertIntoDOM(PdfConstants.RESTRICTED_FUNDS_DISCLOSURE, rootElement, pdfDoc, restrictedFundsDisclosure);
	}
	
	/**
	 * This sets the fund scorecard disclosure
	 * 
	 * @param doc
	 * @param rootElement
	 * @param request
	 */
	public static void setFundScorecardDisclosure(PDFDocument pdfDoc,
			Element rootElement) {
		String fundScorecardDisclosure = ContentHelper.getContentText(CommonContentConstants.FUND_SCORECARD_DISCLOSURE_TEXT, ContentTypeManager.instance().DISCLAIMER, null);
		convertIntoDOM(PdfConstants.FUND_SCORECARD_DISCLOSURE, rootElement, pdfDoc, fundScorecardDisclosure);
	}

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public static void setInfoMessagesXMLElementsForReqAttr(PDFDocument doc, Element rootElement,
            HttpServletRequest request, String reqAttribute) {
        
        Collection infoMessageCollection = (Collection) request.getAttribute(reqAttribute);
        setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
        
    }

    /**
     * This sets XML elements of Error messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public static void setErrorMessagesXMLElementsForReqAttr(PDFDocument doc, Element rootElement,
            HttpServletRequest request, String reqAttribute) {

        Collection infoMessageCollection = (Collection) request.getAttribute(reqAttribute);
        setErrorMessagesXMLElements(doc, rootElement, infoMessageCollection);

    }

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setInfoMessagesXMLElements(PDFDocument pdfDoc, Element rootElement,
            Collection<GenericException> infoMessageCollection) {
        
        Element infoMessagesElement = pdfDoc.createElement(PdfConstants.INFO_MESSAGES);
        
        setMessagesIntoXMLElement(pdfDoc, rootElement, infoMessagesElement, infoMessageCollection);
    }

    /**
     * This sets XML elements of Error messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setErrorMessagesXMLElements(PDFDocument pdfDoc, Element rootElement,
            Collection<GenericException> errorMessageCollection) {

        Element errorMessagesElement = pdfDoc.createElement(PdfConstants.ERROR_MESSAGES);

        setMessagesIntoXMLElement(pdfDoc, rootElement, errorMessagesElement, errorMessageCollection);
    }

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setMessagesIntoXMLElement(PDFDocument pdfDoc, Element rootElement,
            Element messageElement, Collection<GenericException> messageCollection) {
        
        if (messageCollection != null) {
            String[] errors = null;
            try {
                errors = ContentHelper.getMessagesUsingContentType(messageCollection);
            } catch (ContentException exception) {
                logger.error(exception);
            }
            for (int i = 0; i < errors.length; i++) {
                Element messagesElement = pdfDoc.createElement(PdfConstants.MESSAGE);
                convertIntoDOM(PdfConstants.MESSAGE_TEXT, messagesElement, pdfDoc, errors[i]);
                pdfDoc.appendTextNode(messagesElement, PdfConstants.MESSAGE_NUM, String
                        .valueOf(i + 1));
                pdfDoc.appendElement(messageElement, messagesElement);
            }
        }
        pdfDoc.appendElement(rootElement, messageElement);
    }

    /**
     * This sets XML element for Roth message
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setRothMessageElement(PDFDocument pdfDoc, Element rootElement,
            Contract currentContract) {
        boolean hasRoth = currentContract.hasRothNoExpiryCheck();
        if (hasRoth) {
            String rothText = ContentHelper.getContentText(CommonContentConstants.MISCELLANEOUS_ROTH_INFO, ContentTypeManager.instance().MISCELLANEOUS, null);
            convertIntoDOM(PdfConstants.ROTH_MSG, rootElement, pdfDoc, rothText);
        }
    }

    /**
     * This sets XML element for Signature Plus Disclosure message
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    public static void setSigPlusDisclosureMessageElement(PDFDocument pdfDoc, Element rootElement, String sigPlusText) {
        convertIntoDOM(PdfConstants.SIG_PLUS_MSG, rootElement, pdfDoc, sigPlusText);
    }

    /**
     * This removes parentheses if present and prefix with minus sign instead
     * Example  input:(123.45678)  returns:-123.34567   
     * 
     * @param value
     * @return String
     */
    public static String removeParanthesesAndPrefixMinus(String value) {
        StringBuffer signed = new StringBuffer();
        if (StringUtils.isNotEmpty(value)) {
            String parentheses = value.trim(); 
            // Parenthesis should only exist at position 1
            if (parentheses.charAt(0) == CommonConstants.BRACKET_OPEN_SYMBOL.charAt(0)) {
                signed.append(CommonConstants.HYPHON_SYMBOL);
                signed.append(parentheses.substring(1).replace(CommonConstants.BRACKET_CLOSE_SYMBOL, PdfConstants.BLANK_STRING));
            } else {
                signed.append(parentheses);
            } 
        }
        return signed.toString().trim();
    }

    /**
     * This creates a element for the given element name, 
     * converts the given text into DOM node, places the DOM node under the created element and
     * appends the created element to the given root element.
     * 
     * @param createElementString
     * @param rootElement
     * @param pdfDoc
     * @param text
     */
    public static void convertIntoDOM(String elementName, Element rootElement, PDFDocument pdfDoc, String text) {
        Element createElement = pdfDoc.createElement(elementName);
        Document doc = pdfDoc.getDocument();
        Element contentElement = ContentUtility.createContentDOMNodeForPDF(text);
        if (contentElement != null) {
            Node contentNode = doc.importNode(contentElement, true);
            createElement.appendChild(contentNode);
        }
        pdfDoc.appendElement(rootElement, createElement);
    }
}
