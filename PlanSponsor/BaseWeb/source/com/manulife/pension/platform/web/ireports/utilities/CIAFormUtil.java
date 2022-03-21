package com.manulife.pension.platform.web.ireports.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.view.ContentToContentFile;
import com.manulife.pension.exception.SystemException;

/**
 * Utility class for appending Contract Investment Administration PDF form
 * 
 * @author nallaba
 *
 */
public class CIAFormUtil {

	private static final String PDF_PATH = "pdf_path";
	private static final String PDF_PASSWORD = "pdf_password";
	
	
	 
	/**
	 * This method used to append Contract Investment Administration PDF form
	 * 
	 * @param pdfOutStream
	 * @param cmakey
	 * @param context
	 * @return
	 * @throws SystemException
	 */
	public static ByteArrayOutputStream appendCIAForm(ByteArrayOutputStream pdfOutStream, int cmakey, String context, HttpServletRequest request) throws SystemException {

		Map<String, String> pdfDetails;
		List<InputStream> pdfsList = new ArrayList<InputStream>();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutStream.toByteArray());
		pdfsList.add(inputStream);
		
		ByteArrayOutputStream pdfOutStream1 = new ByteArrayOutputStream();

		try {
			pdfDetails = getPdfDtailsFromCMA(cmakey, context);
			
			URL url = new URL(request.getRequestURL().substring(0,
					request.getRequestURL().indexOf("/"))
					+ "//"
					+ request.getServerName()
					+ pdfDetails.get(PDF_PATH));
			byte[] staticPdf = getDecryptedPdf(url, pdfDetails.get(PDF_PASSWORD));
			if (staticPdf != null) {
				pdfsList.add(new ByteArrayInputStream(staticPdf));
			}
			
			pdfOutStream1 = encryptPDF(merge(pdfsList).toByteArray(), pdfDetails.get(PDF_PASSWORD) );
		} catch (Exception e) {
			throw new SystemException(e,
					"unable to append CIA pdf form in appendCIAForm method:");
		}

		return pdfOutStream1;
	}
	 
	/**
	 * This method used to get PDF from CMA using fileId
	 * 
	 * @param pdfsList
	 * @param contentFileId
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	public static String getPdfFromCMA(List<InputStream> pdfsList, int contentFileId, HttpServletRequest request) throws SystemException {
		try {
			BrowseServiceDelegate browseServiceDelegate = BrowseServiceDelegate.getInstance();
			String pdfFilePathFromCMA = browseServiceDelegate.findContentFile(contentFileId).getPath();
			
			URL url = new URL(request.getRequestURL().substring(0,
					request.getRequestURL().indexOf("/"))
					+ "//"
					+ request.getServerName()
					+ pdfFilePathFromCMA);
			byte[] staticPdf = getDecryptedPdf(url, null);
			if (staticPdf != null) {
				pdfsList.add(new ByteArrayInputStream(staticPdf));
			}
		} catch (Exception e) {
			throw new SystemException(e,
					"unable to get Pdf From CMA in getPdfFromCMA method:");
		}
		return null;
	}
	
	/**
	 * This method used to append Contract Investment Administration PDF form
	 * 
	 * @param pdfOutStream
	 * @param cmakey
	 * @param context
	 * @return
	 * @throws SystemException
	 */
	public static String getCIAForm(List<InputStream> pdfsList, int cmakey, String context, HttpServletRequest request) throws SystemException {
		Map<String, String> pdfDetails;
		try {
			pdfDetails = getPdfDtailsFromCMA(cmakey, context);
			
			URL url = new URL(request.getRequestURL().substring(0,
					request.getRequestURL().indexOf("/"))
					+ "//"
					+ request.getServerName()
					+ pdfDetails.get(PDF_PATH));
			byte[] staticPdf = getDecryptedPdf(url, pdfDetails.get(PDF_PASSWORD));
			if (staticPdf != null) {
				pdfsList.add(new ByteArrayInputStream(staticPdf));
			}
		} catch (Exception e) {
			throw new SystemException(e,
					"unable to append CIA pdf form in appendCIAForm method:");
		}
		return pdfDetails.get(PDF_PASSWORD);
	}
	
	/**
	 * This method used to append Contract Investment Administration PDF form
	 * 
	 * @param pdfOutStream
	 * @param cmakey
	 * @param context
	 * @return
	 * @throws SystemException
	 */
	public static ByteArrayOutputStream appendForms(List<InputStream> pdfsList, String password) throws SystemException {
		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		try {
			if(StringUtils.isNotBlank(password)) {
				pdfOutStream = encryptPDF(merge(pdfsList).toByteArray(), password);
			} else {
				pdfOutStream = merge(pdfsList);
			}
		} catch (Exception e) {
			throw new SystemException(e,
					"unable to append CIA pdf form in appendCIAForm method:");
		}
		return pdfOutStream;
	}
	
	/**
	 * Used to merge the pdf documents , allows filling form. 
	 * 
	 * @param pdfsList
	 * @return
	 * @throws Exception
	 */
	private static ByteArrayOutputStream merge(List<InputStream> pdfsList) throws Exception {
		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(pdfOutStream);

		for (InputStream stream : pdfsList) {
			PdfReader pdfReader = new PdfReader(stream);
			renameFields(pdfReader.getAcroFields());
			copy.addDocument(pdfReader);
			stream.close();
		}
		copy.close();
		return pdfOutStream;
	}
	
	/**
	 * Utility method for renaming form fields.
	 * 
	 * @param fields
	 */
	private static void renameFields(AcroFields fields) {
		int counter = 0;
		Set<String> fieldNames = fields.getFields().keySet();
		String prepend = String.format("_%d.", counter++);

		for (String fieldName : fieldNames) {
			fields.renameField(fieldName, prepend + fieldName);
		}
	}

	/**
	 * Retrieves CIA PDF details by providing CMA key .
	 * 
	 * @param cmaKey
	 * @param context
	 * @return
	 * @throws SystemException
	 */
	public static Map<String, String> getPdfDtailsFromCMA(int cmaKey , String context) throws SystemException{
		int contentId = 0;
		int contentFileId = 0;
		String pdfFilePathFromCMA = null;
		//String urlStaticPdf = null;
		HashMap<String, String> pdfDetails = new HashMap<String, String>();
		try {
			BrowseServiceDelegate browseServiceDelegate = BrowseServiceDelegate.getInstance();
			contentId = browseServiceDelegate.findContentByKey(cmaKey).getId();
			ContentToContentFile[] contentToContentFile  = browseServiceDelegate.getAllByContentId(contentId);
			for (int i = 0; i < contentToContentFile.length; i++) {
				if (StringUtils.equalsIgnoreCase(context, contentToContentFile[i].getContext())){
					contentFileId =  contentToContentFile[i].getContentFileId();
				}
			}
			pdfFilePathFromCMA = browseServiceDelegate.findContentFile(contentFileId).getPath();
			pdfDetails.put(PDF_PATH, pdfFilePathFromCMA);
			String pdfPassword = browseServiceDelegate.findContentFile(contentFileId).getAuthor();
			pdfDetails.put(PDF_PASSWORD, pdfPassword);
			//urlStaticPdf = CIAFormUtil.class.getResource(pdfFilePathFromCMA).getFile();
			//pdfDetails.put(PDF_PATH, urlStaticPdf);
			//pdfDetails.put(PDF_PATH, urlStaticPdf);

		} catch (Exception e) {
			throw new SystemException(e,"The exception occured in the method getPdfDtailsFromCMA():: This may due to cmaKey:"+cmaKey+", contentId:"+contentId+", pdfFilePathFromCMA:"+pdfFilePathFromCMA);
		}
		return pdfDetails;
	}
	
	/**
	 * Returns the decrypted pdf.
	 * 
	 * @param pdfDocument
	 * @param password
	 * @return byte[] after decrypting the pdf
	 * @throws SystemException
	 */
	public static byte[] getDecryptedPdf(URL pdfurl, String password)
			throws SystemException {

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PDDocument doc = null;
		try {
			doc = PDDocument.load(pdfurl.openStream(),password);
			if (doc.isEncrypted()) {
				decryptPDF(doc, password);
			}
			doc.save(output);
			doc.close();
		} catch (Exception e) {
			throw new SystemException(e,"The exception occured in the method getDecryptedPdf()");
		}
		return output.toByteArray();
	}
    
	/**
	 * Used to decrypt the encrypted pdf.
	 * 
	 * @param doc
	 * @param password
	 * @throws SystemException
	 */
    private static void decryptPDF(PDDocument doc, String password) throws SystemException {
		try {
			AccessPermission ap = doc.getCurrentAccessPermission();
			if (ap.isOwnerPermission()) {
				doc.setAllSecurityToBeRemoved(true);
			}
		} catch (Exception e) {
			throw new SystemException(e,"The exception occured in the method decryptPDF()");
		}
	}
	
    /**
     * Used to encrypt the pdf.
     * 
     * @param pdfOutStream
     * @param password
     * @return
     * @throws SystemException
     */
	public static ByteArrayOutputStream encryptPDF(byte[] pdfOutStream, String password) throws SystemException {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutStream);
			PDDocument doc = PDDocument.load(inputStream);
			AccessPermission ap = new AccessPermission();
			ap.setCanFillInForm(true);
			//second argument is the user password. If set to something it asks for password when opening file, not wanted.
			StandardProtectionPolicy policy = new StandardProtectionPolicy(password, null, ap);
			doc.protect(policy);
			doc.save(output);
			doc.close();
    		
    	 } catch (Exception e) {
    		 throw new SystemException(e.getMessage());
    		}
    	 return output;
    	}
	
	

}
