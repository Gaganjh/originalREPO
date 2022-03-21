package com.manulife.pension.ireports.report.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.report.StandardReportBuilder;
import com.manulife.pension.platform.utility.util.ContentHelper;
import com.manulife.pension.ireports.content.ContentUtility;
import com.manulife.pension.ireports.dao.Footnote;
import com.manulife.util.pdf.PdfParagraph;
import com.manulife.util.pdf.PdfText;

/**
 * This helper class is useful to convert the CMA Content into equivalent PdfParagraph / PdfText
 * objects with some of the HTML tags converted properly to equivalent XSL-FO tags.
 * 
 * @author HArlomte
 * 
 */
public class CMAContentHelper {

    public static final Logger logger = Logger.getLogger(CMAContentHelper.class);
    
    /**
     * This method retrieves the Page Footnote text.
     * 
     * @param contentId - content ID.
     * @param companyId - company ID
     * @return - text retrieved from CMA.
     */
    public static String getPageFootnoteText(String contentId, String companyId) {
        String contentText = "";

        if (!StringUtils.isBlank(contentId)) {
            try {
                contentText = ContentHelper.getContentText(Integer.parseInt(contentId.trim()),
                        ContentTypeManager.instance().PAGE_FOOTNOTE, getLocation(companyId));
            } catch (NumberFormatException nfe) {
                logger.error("Number Format Exception occurred for content ID: " + contentId);
            }

        }
        return contentText;
    }
    /**
     * This method retrieves the Page Footnote text.
     * 
     * @param staticFootnoteSymbols - List of symbols.
     * @param companyId - company ID
     * @return - Map Symbol and its footnote object.
     */
	public static Map<String, Footnote> getPageFootnoteTextByDisplayName(
			String[] staticFootnoteSymbols, String companyId)
			throws SystemException {
		Map<String, Footnote> result = new HashMap<String, Footnote>();
		result = ContentHelper.getContentTextByDisplayName(
				staticFootnoteSymbols, getLocation(companyId));
		return result;
	}


    /**
     * This method is used when creating the PdfParagraph for Intro Disclosure.
     * 
     * @param report
     * @param contentIds
     * @param companyId
     * @return
     */
    public static PdfParagraph createIntroDisclosureParagraph(StandardReportBuilder report,
            String contentId, String companyId) {
        PdfParagraph headerParagraph = null;

        if (!StringUtils.isBlank(contentId)) {
            String contentText = "";
            try {
                contentText = ContentHelper.getContentText(Integer.parseInt(contentId.trim()),
                        ContentTypeManager.instance().FOOTNOTE, getLocation(companyId));
            } catch (NumberFormatException nfe) {
                // Don't do anything. The Text will not be shown.
            }
                
                headerParagraph = createParagraphForCMAText(report, contentText);
        }
        
        return headerParagraph;
    }

    /**
     * This method will create the PdfParagraph for a given CMA text.
     * 
     * @param report
     * @param contentText
     * @return
     */
    public static PdfParagraph createParagraphForCMAText(StandardReportBuilder report,
            String contentText) {
        PdfParagraph headerParagraph = null;

        if (!StringUtils.isBlank(contentText)) {
            String contentWithFo = ContentUtility.convertCMAContentToXSLFO(contentText);
            Element contentElement = ContentUtility
                    .createContentDOMNodeFromXslFOString(contentWithFo);
            headerParagraph = report.createParagraph(contentElement);
        }

        return headerParagraph;
    }

    /**
     * This method will create the PdfText for a given CMA text.
     * 
     * @param report
     * @param contentText
     * @return
     */
    public static PdfText createInlineForCMAText(StandardReportBuilder report, String contentText) {
        PdfText text = null;

        if (!StringUtils.isBlank(contentText)) {
            String contentWithFo = ContentUtility.convertCMAContentToXSLFO(contentText);
            Element contentElement = ContentUtility
                    .createContentDOMNodeFromXslFOString(contentWithFo);
            text = report.createText(contentElement);
        }

        return text;
    }

    /**
     * This method is used to determine the Location, given the company ID.
     * 
     * @param companyId
     * @return
     */
    public static Location getLocation(String companyId) {
        if (StandardReportsConstants.COMPANY_ID_NY.equals(companyId)) {
            return Location.NEW_YORK;
        } else {
            return Location.USA;
        }
    }
    
	/**
	 * This method retrieves the Page Footnote text for a ContentId with Params Substitution.
	 * 
	 * @param contentId
	 * @param companyId
	 * @param contentParams
	 * 
	 * @return text retrieved from CMA.
	 */
	public static String getPageFootnoteTextWithParams(String contentId,
			String companyId, String... contentParams) {
		String contentText = StandardReportsConstants.SPACE;

		if (StringUtils.isNotBlank(contentId)) {
			contentText = ContentHelper.getContentTextWithParamsSubstitution(
					Integer.parseInt(contentId.trim()), ContentTypeManager
							.instance().PAGE_FOOTNOTE, getLocation(companyId),
					contentParams);
		}
		return contentText;
    }

}
