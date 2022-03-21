package com.manulife.pension.platform.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * Base Action class for PDF functionalities
 * 
 * @author ayyalsa
 * 
 */
public abstract class BasePdfAutoController extends BaseAutoController {

    public BasePdfAutoController(Class clazz) {
        super(clazz);
    }

    /**
     * This method is used to get XSL file name for PDF generation
     * 
     * @return String XSLT file name
     */
    protected String getXSLTFileName() {
        return null;
    }
    
    /**
     * @return the XSL included path
     */
    public String getIncludedXSLPath() {
        return null;
    }

    /**
     * This method would return the key present in ReportsXSL.properties file. This key has the
     * value as path to FOP Configuration file.
     * 
     * @return String
     */
    protected String getFOPConfigFileName() {
        return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
    }
    
    /**
     * This method will be called when the user clicks on the "Print PDF" button. This method
     * fetches the data from the database and places the information into an XML file. Using
     * Apache-FOP, the XML file, XSLT file is converted into a PDF file. The PDF file is sent back
     * to the user.
     * 
     * @param mapping
     * @param reportForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws FOPException
     * @throws TransformerException
     * @throws ContentException
     */
    public String doPrintPDF( AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException, IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }
        ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);

        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(actionForm, report, request);
        String filename = getFilename(actionForm, request);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
        response.setContentLength(pdfOutStream.size());
            
        try {
            ServletOutputStream sos = response.getOutputStream();
            pdfOutStream.writeTo(sos);
            sos.flush();
        } catch (IOException ioException) {
            throw new SystemException(ioException, "Exception writing pdfData.");
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException ioException) {
                throw new SystemException(ioException, "Exception writing pdfData.");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting doPrintPDF");
        }
        return null;
    }

    /**
     * This method will generate the PDF and return a ByteArrayOutputStream which will be sent back
     * to the user. This method would: - Create the XML-String from VO. - Create the PDF using the
     * created XML-String and XSLT file.
     * 
     * @throws ContentException
     */
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(AutoForm actionForm,
            ReportData report, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {

            Object xmlTree = prepareXMLFromReport(actionForm, report, request);
            String xsltFileName = getXSLTFileName();
            String xslInculdedPath = getIncludedXSLPath();
            if (StringUtils.isBlank(xslInculdedPath)) {
            	xslInculdedPath = CommonConstants.INCLUDED_XSL_FILES_PATH;
            }
            
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties.get(xslInculdedPath);
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document) xmlTree,
                        xsltfile, configfile, includedXSLPath);
            } else if (xmlTree instanceof String) {
            	pdfOutStream = PDFGenerator.getInstance().generatePDF((String) xmlTree,
                        xsltfile, configfile, includedXSLPath, true);
            }

        } catch (Exception exception) {
            String message = null;
            if (exception instanceof ContentException) {
                message = "Error occured while retrieveing CMA Content during PDF creation.";
            } else if (exception instanceof ParserConfigurationException) {
                message = "Error occured while creating Document object during PDF creation.";
            } else if (exception instanceof FOPException
                    || exception instanceof TransformerException
                    || exception instanceof IOException) {
                message = "Error occured during PDF generation.";
            } else {
                message = "Error occured during PDF generation.";
            }

            throw new SystemException(exception, message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting prepareXMLandGeneratePDF");
        }
        return pdfOutStream;
    }

    /**
     * This method needs to be overridden by any Report that needs PDF Generation functionality.
     * This method would generate the XML file.
     * 
     * @param reportForm
     * @param report
     * @param request
     * @return Object
     * @throws ParserConfigurationException
     * @throws SystemException
     * @throws ContentException
     */
    public Object prepareXMLFromReport(AutoForm actionForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException, SystemException, ContentException {
        return null;
    }

    /**
     * returns file name
     * @param actionForm
     * @param request
     * @return file name
     */
    public String getFilename(AutoForm actionForm, HttpServletRequest request){
    	return null;
    }
    
    /**
	 * Method to get the Content
	 * 
	 * @param contentId
	 * @param contentType
	 * @param attribute
	 * @param member
	 * @param params
	 * 
	 * @return String
	 * 
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	public static String getContent(String contentId, ContentType contentType,
			String attribute, String member, String[] params)
			throws NumberFormatException, ContentException {
		Content content = ContentCacheManager.getInstance().getContentById(
				Integer.parseInt(contentId), contentType);
		String contentText = "";
		if (params != null && params.length != 0) {
			contentText = ContentUtility.getContentAttribute(content,
					attribute, member, params);
			if(StringUtils.isNotBlank(contentText) && contentText.contains("<li>")){
				contentText = contentText.replaceAll("<li>", StringUtils.EMPTY);
				contentText = contentText.replaceAll("</li>", StringUtils.EMPTY);
				return StringUtils.defaultIfEmpty(contentText, StringUtils.EMPTY);
			}
			
		} else {
			contentText = ContentUtility
					.getContentAttribute(content, attribute);
			if(StringUtils.isNotBlank(contentText) && contentText.contains("<li>")){
				contentText = contentText.replaceAll("<li>", StringUtils.EMPTY);
				contentText = contentText.replaceAll("</li>", StringUtils.EMPTY);
				return StringUtils.defaultIfEmpty(contentText, StringUtils.EMPTY);
			}
			
		}

//		return ContentUtility.convertCMAContentToXSLFO(StringUtils.defaultIfEmpty(contentText, StringUtils.EMPTY));
		return StringUtils.defaultIfEmpty(contentText, StringUtils.EMPTY);
	}
	
	/**
	 * Method to get the CMA content Text attribute 
	 * 
	 * @param contentId
	 * @return
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	public static String getCMAContentAttributeText(String contentId) 
			throws NumberFormatException, ContentException {
		
		return getContent(contentId, ContentTypeManager.instance().MISCELLANEOUS, PdfConstants.TEXT, null, null);
	}
	
	/**
	 * Method to get the CMA content Title attribute 
	 * 
	 * @param contentId
	 * @return
	 * @throws NumberFormatException
	 * @throws ContentException
	 */
	public static String getCMAContentAttributeTitle(String contentId) 
			throws NumberFormatException, ContentException {
		
		return getContent(contentId, ContentTypeManager.instance().MISCELLANEOUS, PdfConstants.TITLE, null, null);
	}
	
	/**
	 * REturns the printParticipant request value
	 * @param request
	 * @return printParticipant
	 */
	protected static boolean getPrintParticipant(HttpServletRequest request) {
		return request.getParameter("printParticipant") == null ? 
				false : Boolean.valueOf(request.getParameter("printParticipant"));
	}
	
}
