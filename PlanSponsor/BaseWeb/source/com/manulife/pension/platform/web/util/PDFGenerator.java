package com.manulife.pension.platform.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.manulife.pension.util.content.helper.ContentUtility;

/**
 * This file is used to Generate an OutputStream of the PDF file, given the xmlfile, and xslt file. Apache-FOP is used
 * to convert the XML, XSLT file to PDF file.
 * 
 * @author HArlomte
 * 
 */
public class PDFGenerator {

    private static PDFGenerator instance = null;

    public static final Logger logger = Logger.getLogger(PDFGenerator.class);

    static {
        instance = new PDFGenerator();
    }

    public static PDFGenerator getInstance() {
        return instance;
    }

    /**
     * This method generates the OutputStream of the PDF file.
     * 
     * @param xmlfile
     * @param xsltfile
     * @param configfile
     * @param includedXSLPath
     * @param convertHTMLToXSLFO
     * @return pdf
     * @throws FOPException
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     */
    public ByteArrayOutputStream generatePDF(String xmlfile, String xsltfile, 
    		String configfile, String includedXSLPath, boolean convertHTMLToXSLFO) 
    				throws FOPException, TransformerException, IOException, SAXException {
        ByteArrayOutputStream pdfOutStream = null;
        try {

            if (logger.isDebugEnabled()) {
                logger.debug("> entered PDFGenerator.generatorPDF");
            }
            
            pdfOutStream = new ByteArrayOutputStream();
            
            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();

            // set configuration file
            configfile = getClass().getClassLoader().getResource(configfile).getPath();
            File configFile = new File(configfile);
            fopFactory.setUserConfig(configFile);
            
            // set font base URL
            String fontBaseURL = StringUtils.removeEnd(configfile, "Config/fop.xconf");
            fopFactory.setFontBaseURL(fontBaseURL);
            
            // configure foUserAgent as desired
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOutStream);
            
            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            URIResolver uriResolver = new XSLFileURIResolver(includedXSLPath);
            factory.setURIResolver(uriResolver);
            InputStream xsltStream = getClass().getClassLoader().getResourceAsStream(xsltfile);
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

            // Set the value of a <param> in the stylesheet
            transformer.setParameter("versionParam", "2.0");

            // Setup input for XSLT transformation
            Source src = new StreamSource(new StringReader(xmlfile));
            
            if (convertHTMLToXSLFO) {
	            ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 50);
	            transformer.transform(src, new StreamResult(out));
	            String foString = out.toString("utf-8");
	            foString = ContentUtility.convertCMAContentToXSLFO(foString, false, true);
	            src = new StreamSource(new ByteArrayInputStream(foString.getBytes("utf-8")));
	    		transformer = TransformerFactory.newInstance().newTransformer();
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("PDF Generation: XML SOURCE = " + src.toString());
            }
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
            
            if (logger.isDebugEnabled()) {
                logger.debug("> exited PDFGenerator.generatorPDF");
            }
        } finally {
        	if(pdfOutStream!=null)
            pdfOutStream.close();
        }
        return pdfOutStream;
    }

    /**
     * This method generates the OutputStream of the PDF file.
     * 
     * @param xmlfile - A String having the contents of an XML file.
     * @param xsltfile - XSLT file.
     * @return - ByteArrayOutputStream of the PDF generated.
     * @throws TransformerException
     * @throws IOException
     * @throws SAXException
     */
    public ByteArrayOutputStream generatePDFFromDOM(Document xmlfile, String xsltfile, String configfile, String includedXSLPath) throws TransformerException, IOException, SAXException {
        ByteArrayOutputStream pdfOutStream = null;
        InputStream xsltStream = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("> entered PDFGenerator.generatorPDFFromDOM");
                logger.debug("### XML OUTPUT FOR FUND EVALUATOR PDF");
                serializeDOM(xmlfile);
            }
            pdfOutStream = new ByteArrayOutputStream();

            // configure fopFactory as desired
            FopFactory fopFactory = FopFactory.newInstance();

            // set configuration file
            configfile = getClass().getClassLoader().getResource(configfile).getPath();
            File configFile = new File(configfile);
            fopFactory.setUserConfig(configFile);

            // set font base URL
            String fontBaseURL = StringUtils.removeEnd(configfile, "Config/fop.xconf");
            fopFactory.setFontBaseURL(fontBaseURL);

            // configure foUserAgent as desired
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfOutStream);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            URIResolver uriResolver = new XSLFileURIResolver(includedXSLPath);
            factory.setURIResolver(uriResolver);
            xsltStream = getClass().getClassLoader().getResourceAsStream(xsltfile);
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));

            // Set the value of a <param> in the stylesheet
            transformer.setParameter("versionParam", "2.0");

            // Setup input for XSLT transformation
            Source src = new DOMSource(xmlfile);
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);

            if (logger.isDebugEnabled()) {
                logger.debug("> exited PDFGenerator.generatorPDFFromDOM");
            }
        } finally {
        	if(xsltStream != null)
        		xsltStream.close();
        	
        }
        return pdfOutStream;
    }

    public void serializeDOM(Document doc) {
        try {
            OutputFormat format = new OutputFormat(doc);
            format.setLineWidth(254);
            format.setIndenting(true);
            format.setIndent(2);
            XMLSerializer serializer = new XMLSerializer(System.out, format);
            serializer.serialize(doc);
        } catch (Exception e) {
            // this method is only used for Debug so we can dump the output of the exception
            e.printStackTrace();
        }
    }
}