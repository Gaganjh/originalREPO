package com.manulife.pension.bd.web.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * Base Action class for ParticipantAccountCommonAction
 * 
 * @author Ramkumar
 * 
 */
public abstract class BDPdfController extends BDController {
    
	
    public static final Logger logger = Logger.getLogger(BDPdfController.class);
    
    @SuppressWarnings("unchecked")
    public BDPdfController(Class clazz) {
        super(clazz);
    }
  
    /**
     * This method is used to get XSL file name for PDF generation
     * 
     * @return String XSLT file name
     */
    protected abstract String getXSLTFileName();
    
    /**
     * This method returns PDF capped text
     * 
     * @return String
     */
    protected String getPDFCappedText() {
        return BDPdfConstants.PDF_CAPPED_MESSAGE1 + getMaxCappedRowsInPDF()
                + BDPdfConstants.PDF_CAPPED_MESSAGE2;
    }

    /**
     * This method needs to be overridden by the Action class that needs PDF Generation
     * functionality. This method checks to see if the # of rows in the Report would be greater than
     * the PDF cap limit. If yes, the "pdfCapped" is set to true in the form. This boolean value
     * will be later used in JSP to open a pop-up whenever the user clicks on PDF button to open
     * "PDF-version" of the Report. Currently Participant account and Defined Benefit account
     * reports use participantAccountVO and they pass reportData object as null. And rest of the
     * reports pass participantAccountVO as null. Since LoanRepaymentDetailsReportData does not
     * extend ReportData, Object type is used here for reportData. Type cast reportData to the
     * respective type on overriding.
     * 
     * @param reportData
     * @param form
     * @param participantAccountVO
     */
    protected void populateCappingCriteria(Object reportData, BaseForm form,
            ParticipantAccountVO participantAccountVO) {
        return;
    }
    
    /**
     * This method returns the # of rows where the PDF will be capped.
     * @return Integer
     */
    protected Integer getMaxCappedRowsInPDF() {
        return Integer.valueOf(ReportsXSLProperties.get(BDConstants.MAX_CAPPED_ROWS_IN_PDF));
    }
    
    /**
     * This method needs to be overridden by the Action class that needs PDF Generation
     * functionality. This method will return the # of rows that is present in the current Report.
     * The Integer returned will be used to see if we need to cap the # of rows or not. Since
     * LoanRepaymentDetailsReportData does not extend ReportData, Object type is used here for
     * reportData. Type cast reportData to the respective type on overriding.
     * 
     * @param reportData
     * @return - The number of rows in the current Report.
     */
    protected Integer getNumberOfRowsInReport(Object reportData) {
        return 0;
    }

    /**
     * This method will be called when the user clicks on the "Print PDF" button. This method
     * fetches the data from the database and places the information into an XML file. Using
     * Apache-FOP, the XML file, XSLT file is converted into a PDF file. The PDF file is sent back
     * to the user. Currently Participant account and Defined Benefit account reports use
     * participantAccountVO and they pass reportData object as null. And rest of the reports pass
     * participantAccountVO as null. Since LoanRepaymentDetailsReportData does not extend
     * ReportData, Object type is used here for reportData. Type cast reportData to the respective
     * type on overriding.
     * 
     * @param form
     * @param report
     * @param participantAccountVO
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    public String doPrintPDF(BaseForm form, Object reportData,
            ParticipantAccountVO participantAccountVO, HttpServletRequest request,
            HttpServletResponse response) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }
        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(form, reportData,
                participantAccountVO, request);
        
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
     * created XML-String and XSLT file. Currently Participant account and Defined Benefit account
     * reports use participantAccountVO and they pass reportData object as null. And rest of the
     * reports pass participantAccountVO as null. Since LoanRepaymentDetailsReportData does not
     * extend ReportData, Object type is used here for reportData. Type cast reportData to the
     * respective type on overriding.
     * 
     * @param form
     * @param report
     * @param participantAccountVO
     * @param request
     * @return ByteArrayOutputStream
     * @throws SystemException
     */
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(BaseForm form,
            Object reportData, ParticipantAccountVO participantAccountVO, HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {
            
            Object xmlTree = prepareXMLFromReport(form, reportData, participantAccountVO, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties.get(BDConstants.INCLUDED_XSL_FILES_PATH);
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
     * This method would return the key present in ReportsXSL.properties file. This key has the
     * value as path to FOP Configuration file.
     * 
     * @return String
     */
    protected String getFOPConfigFileName() {
        return BDConstants.FOP_CONFIG_FILE_KEY_NAME;
    }

    /**
     * This method needs to be overridden by any Report that needs PDF Generation functionality.
     * This method would generate the XML file. Currently Participant account and Defined Benefit
     * account reports use participantAccountVO and they pass reportData object as null. And rest of
     * the reports pass participantAccountVO as null. Since LoanRepaymentDetailsReportData does not
     * extend ReportData, Object type is used here for reportData. Type cast reportData to the
     * respective type on overriding.
     * 
     * @param reportForm
     * @param report
     * @param request
     * @return Document
     */
    public Object prepareXMLFromReport(BaseForm reportForm, Object reportData,
            ParticipantAccountVO participantAccountVO, HttpServletRequest request)
            throws ParserConfigurationException {
        return null;
    }
    
    /**
     * This method gets layout page for the given layout id.
     * 
     * @param path
     * @return LayoutPage
     */
    protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
        //BDLayoutBean bean = ApplicationHelper.getLayoutStore(getServlet().getServletContext()).getLayoutBean(id, request);
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpServletRequest req = attr.getRequest();
    	BDLayoutBean bean = ApplicationHelper.getLayoutStore(req.getServletContext()).getLayoutBean(id, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
        return layoutPageBean;
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
            Element rootElement, HttpServletRequest request) {
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);
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
        BDPdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                BDContentConstants.BD_GLOBAL_DISCLOSURE, params);
    }

    /**
     * This sets Global Disclosure XML element common for reports
     * 
     * @param doc
     * @param rootElement
     */
    public static void setGlobalDisclosureXMLElement(PDFDocument pdfDoc, Element rootElement,
            Location location) {
        BDPdfHelper.setGlobalDisclosureXMLElement(pdfDoc, rootElement, location,
                BDContentConstants.BD_GLOBAL_DISCLOSURE);
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
    protected void setRothMessageElement(PDFDocument doc, Element rootElement,
            HttpServletRequest request) {
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        setRothMessageElement(doc, rootElement, currentContract);
    }

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement,
            HttpServletRequest request) {
        Collection infoMessageCollection = (Collection) request
                .getAttribute(BDConstants.INFO_MESSAGES);
        setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
    }
    
}
