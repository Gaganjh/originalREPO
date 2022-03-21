package com.manulife.pension.service.loan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.loan.valueobject.document.TruthInLendingNotice;
import com.manulife.pension.service.loan.valueobject.document.XMLValueObject;
import com.manulife.pension.util.fop.FopUtils;
import com.manulife.util.exception.TransformationException;
import com.manulife.util.xml.TransformationHelper;

public class TransformationHelperTest extends TestCase {

	TransformationHelper transform = new TransformationHelper();

	TruthInLendingNotice notice = new TruthInLendingNotice();

	private static final String XSL_PDF_PATH = "xsl/TruthInLendingNotice_PDF.xslt";

	private static final String XSL_HTML_PATH = "xsl/TruthInLendingNotice_HTML.xslt";
	
	private static boolean INTIAL_LOAD = true;

	/**
	 * Empty Constructor
	 */
	public TransformationHelperTest() {
	}

	/**
	 * Sets up the fixtures
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();
		notice.setFirstName("ROY");
		notice.setLastName("PAUL");
		notice.setMiddleInitial("P");
		if(INTIAL_LOAD) {
			//setting the font assets base path
			String assetsCommonPath = new File("../common").toURL().toString();
			FopUtils.getFopFactory().setFontBaseURL(assetsCommonPath);
			INTIAL_LOAD = false;
		}	
	}

	/**
	 * Tears down the fixtures
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test Case for transform method
	 */
	@Test
	public void testTransform() {
		String xml = notice.toXML();
		String xsl = null;
		try {
			InputStream xsltFile = XMLValueObject.class.getResourceAsStream(XSL_HTML_PATH);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			//Convert xsl into String format
			byte xsltData[] = new byte[xsltFile.available()];
			xsltFile.read(xsltData);
			xsl = new String(xsltData, "US-ASCII");
			
			//Transformation
			transform.transform(xml, xsl, out);

			System.out.println(out.toString());

			if (out == null) {
				assertFalse("Problem occured while generating the HTML", true);
			}

		} catch (TransformationException transformationException) {
			assertFalse("Problem in creating HTML", true);
		} catch (Exception e) {
			assertFalse("Problem in reading the file", true);
		}
	}

	/**
	 * Test case for transformFopPdf method
	 * 
	 * @throws IOException
	 */
	@Test
	public void testTransformFopPdf() {

		String xml = notice.toXML();
		String xsl = null;
		try {
			InputStream xslFile = XMLValueObject.class.getResourceAsStream(XSL_PDF_PATH);

			//Convert xsl into String format
			byte xsltData[] = new byte[xslFile.available()];
			xslFile.read(xsltData);
			xsl = new String(xsltData, "US-ASCII");

			OutputStream out = new ByteArrayOutputStream();
			transform.transformFopPdf(xml, xsl, out);

			if (out == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (TransformationException transformationException) {
			assertFalse("Problem in creating PDF", true);
		} catch (Exception e) {
			assertFalse("Problem in reading the file", true);
		}
	}

	/**
	 * Test case for concatPDF mehod
	 * 
	 */
	@Test
	public void testConcatPDF() {
		try {
			String xml1 = notice.toXML();
			String xml2 = notice.toXML();
			InputStream xslFile1 = XMLValueObject.class.getResourceAsStream(XSL_PDF_PATH);
			InputStream xslFile2 = XMLValueObject.class.getResourceAsStream(XSL_PDF_PATH);

			OutputStream out1 = new ByteArrayOutputStream();
			OutputStream out2 = new ByteArrayOutputStream();
			OutputStream result = new ByteArrayOutputStream();
			List<byte[]> output = new ArrayList<byte[]>();

			//Convert xsl into String format
			byte xslData1[] = new byte[xslFile1.available()];
			xslFile1.read(xslData1);
			String xslStream1 = new String(xslData1, "US-ASCII");

			//Convert xsl into String format
			byte xslData2[] = new byte[xslFile2.available()];
			xslFile2.read(xslData2);
			String xslStream2 = new String(xslData2, "US-ASCII");

			//Transformation into pdf
			transform.transformFopPdf(xml1, xslStream1, out1);
			transform.transformFopPdf(xml2, xslStream2, out2);
			output.add(((ByteArrayOutputStream) out1).toByteArray());
			output.add(((ByteArrayOutputStream) out2).toByteArray());
			try {
				transform.concatPDF(output, result);
			} catch (Exception e) {
				assertFalse("Problem in concatenating the PDFs.", true);
			}

			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (TransformationException exception) {
			assertFalse("Problem in creating PDF", true);
		} catch (IOException exception) {
			assertFalse("Problem in reading the file", true);
		}
	}

	/**
	 * Test Case for addDraftWaterMark mehtod
	 * 
	 */
	@Test
	public void testAddDraftWaterMark() {
		try {
			String xml1 = notice.toXML();
			String xml2 = notice.toXML();
			InputStream xslFile1 = XMLValueObject.class.getResourceAsStream(XSL_PDF_PATH);
			InputStream xslFile2 = XMLValueObject.class.getResourceAsStream(XSL_PDF_PATH);
			OutputStream out1 = new ByteArrayOutputStream();
			OutputStream out2 = new ByteArrayOutputStream();
			OutputStream result = new ByteArrayOutputStream();
			List<byte[]> output = new ArrayList<byte[]>();

			//Convert xsl into String format
			byte xslData1[] = new byte[xslFile1.available()];
			xslFile1.read(xslData1);
			String xslStream1 = new String(xslData1, "US-ASCII");

			//Convert xsl into String format
			byte xslData2[] = new byte[xslFile2.available()];
			xslFile2.read(xslData2);
			String xslStream2 = new String(xslData2, "US-ASCII");

			//Transformation
			transform.transformFopPdf(xml1, xslStream1, out1);
			transform.transformFopPdf(xml2, xslStream2, out2);
			output.add(((ByteArrayOutputStream) out1).toByteArray());
			output.add(((ByteArrayOutputStream) out2).toByteArray());

			//Concat the pdf's
			transform.concatPDF(output, result);

			try {
				transform.addDraftWaterMark(result);
			} catch (Exception e) {
				assertFalse("Problem in adding water mark to the PDFs.", true);
			}

			if (result == null) {
				assertFalse("Problem occured while getting the PDF", true);
			}
		} catch (TransformationException exception) {
			assertFalse("Problem in creating PDF", true);
		} catch (IOException exception) {
			assertFalse("Problem in reading the file", true);
		}
	}
}
