package com.manulife.pension.platform.web.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Base Action class for PDF functionalities
 * 
 * @author Ramkumar
 * 
 */
public abstract class BasePdfController extends BaseController {

    @SuppressWarnings("unchecked")
    public BasePdfController(Class clazz) {
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
     * This method returns PDF capped text
     * 
     * @return String
     */
    protected String getPDFCappedText() {
        return PdfConstants.PDF_CAPPED_MESSAGE1 + getMaxCappedRowsInPDF()
                + PdfConstants.PDF_CAPPED_MESSAGE2;
    }
    
    /**
     * This method returns the # of rows where the PDF will be capped.
     * 
     * @return Integer
     */
    protected Integer getMaxCappedRowsInPDF() {
        return Integer.valueOf(ReportsXSLProperties.get(CommonConstants.MAX_CAPPED_ROWS_IN_PDF));
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
     * This method checks to see if the # of rows in the Report would be greater than the PDF cap
     * limit. If yes, the "pdfCapped" is set to true in the form. This boolean value will be later
     * used in JSP to open a pop-up whenever the user clicks on PDF button to open "PDF-version" of
     * the Report.
     */
    protected void populateCappingCriteria(ReportData report, BaseReportForm reportForm,
            HttpServletRequest request) {

        reportForm.setPdfCapped(false);

        if (getNumberOfRowsInReport(report) > getMaxCappedRowsInPDF()) {
            reportForm.setCappedRowsInPDF(getMaxCappedRowsInPDF());
            reportForm.setPdfCapped(true);
        } else {
            reportForm.setCappedRowsInPDF(getNumberOfRowsInReport(report));
        }

    }

    /**
     * This method will return the # of rows that is present in the current Report. The Integer
     * returned will be used to see if we need to cap the # of rows or not.
     * 
     * @param report
     * @return - The number of rows in the current Report.
     */
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if (report.getDetails() != null) {
            noOfRows = report.getDetails().size();
        }
        return noOfRows;
    }

    /**
     * This method will be called when the user clicks on the "Print PDF" button. This method
     * fetches the data from the database and places the information into an XML file. Using
     * Apache-FOP, the XML file, XSLT file is converted into a PDF file. The PDF file is sent back
     * to the user.
     * 
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
    public String doPrintPDF(BaseReportForm reportForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }
        ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);

        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(reportForm, report, request);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline");
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
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(BaseReportForm reportForm,
            ReportData report, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {

            Object xmlTree = prepareXMLFromReport(reportForm, report, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties
                    .get(CommonConstants.INCLUDED_XSL_FILES_PATH);
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document) xmlTree,
                        xsltfile, configfile, includedXSLPath);
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
     */
    public Object prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws ParserConfigurationException {
        return null;
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets footer, footnotes and disclaimer XML elements common for reports
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param params
     */
    protected void setFooterXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, Location location, String[]... params) {   
        PdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                CommonContentConstants.GLOBAL_DISCLOSURE, params);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets Logo, PathName, Introduction-1 & 2 XML elements common for reports.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setIntroXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, Contract contract) {
        PdfHelper.setIntroXMLElements(layoutPageBean, doc, rootElement, contract);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets the Logo, Path Name XML elements.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setLogoAndPageName(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement) {
        PdfHelper.setLogoAndPageName(layoutPageBean, doc, rootElement);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets the Introduction-1, Introduction-2 XML elements.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setIntro1Intro2XMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement) {
        PdfHelper.setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets XML elements of Informational messages common for reports.
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement,
            Collection infoMessageCollection) {
        PdfHelper.setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets XML elements of Informational messages common for reports.
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setInfoMessagesXMLElementsForReqAttr(PDFDocument doc, Element rootElement,
            HttpServletRequest request, String reqAttribute) {
        PdfHelper.setInfoMessagesXMLElementsForReqAttr(doc, rootElement, request, reqAttribute);
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets XML element for Roth message.
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setRothMessageElement(PDFDocument doc, Element rootElement, Contract contract) {
        PdfHelper.setRothMessageElement(doc, rootElement, contract);
    }

    /**
     * This sets Global Disclosure XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setGlobalDisclosureXMLElement(PDFDocument pdfDoc, Element rootElement, Location location) {
        PdfHelper.setGlobalDisclosureXMLElement(pdfDoc, rootElement, location,
                CommonContentConstants.GLOBAL_DISCLOSURE);
    }

    /**
     * This removes parentheses if present and prefix with minus sign instead Example
     * input:(123.45678) returns:-123.34567
     * 
     * @param value
     * @return String
     */
    protected String removeParanthesesAndPrefixMinus(String value) {
        return PdfHelper.removeParanthesesAndPrefixMinus(value);
    }
    
}
