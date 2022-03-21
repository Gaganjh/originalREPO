package com.manulife.pension.bd.web.ikit;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class will hold the Utility methods for IkitFundInfoAction.java
 * 
 * @author harlomte
 * 
 */
public class GetIKitFundInfoUtils {
	public static Map<String, String> errorMap;
	
    private static final String EMPTY_STRING = "";
    
    private static final String SPACE = " ";

    // For removing many common tags from content for use in CSV files:
    private static final Pattern PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY = Pattern
			.compile("(<sup[^>]*>.*</sup>)", Pattern.CASE_INSENSITIVE);
    
	// For replacing invalid XML characters with space.
    private static final Pattern PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE = Pattern.compile(
            "([\\x00-\\x1F\\x7F-\\xA8\\xAA-\\xFF])", Pattern.CASE_INSENSITIVE);
	
    private static HashMap<String, Integer> guaranteedAccountFundsOrderMap = new HashMap<String, Integer>();
    
	static {
		 errorMap = new HashMap<String, String>();

		 /**
		  * Add all the error code and its corresponding error message into this Map.
		  */
		 errorMap.put(IKitErrorCodes.CONTRACT_ID_NOT_FOUND_CODE, IKitErrorCodes.CONTRACT_ID_NOT_FOUND_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_CODE, IKitErrorCodes.CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_STATUS_NOT_SUPPORTED_CODE, IKitErrorCodes.CONTRACT_STATUS_NOT_SUPPORTED_MSG);
// 2010-03-05: i:enrollment edit removed at the request of the business
//		 errorMap.put(IKitErrorCodes.IENROLLMENT_NOT_ALLOWED_CODE, IKitErrorCodes.IENROLLMENT_NOT_ALLOWED_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_ID_NON_NUMERIC_CODE, IKitErrorCodes.CONTRACT_ID_NON_NUMERIC_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_CODE, IKitErrorCodes.CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.CONTRACT_DATA_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.UNKNOWN_SYSTEM_EXCEPTION_CODE, IKitErrorCodes.UNKNOWN_SYSTEM_EXCEPTION_MSG);
		 
		 errorMap.put(IKitErrorCodes.CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_CODE, IKitErrorCodes.CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_MSG);
		 errorMap.put(IKitErrorCodes.PARSER_CONFIGURATION_EXCEPTION_CODE, IKitErrorCodes.PARSER_CONFIGURATION_EXCEPTION_MSG);
		 
		 // This Map holds the order in which the Guaranteed Account funds will be placed in the XML file.
		 guaranteedAccountFundsOrderMap.put("3YC", 1);
		 guaranteedAccountFundsOrderMap.put("5YC", 2);
		 guaranteedAccountFundsOrderMap.put("10YC", 3);
	}

	/**
	 * This method will return the error message, if error code is provided as input.
	 * 
	 * @param errorCode - error code.
	 * @return - error message corresponding to the error code.
	 */
	public static String getErrorMessage(String errorCode) {
		return errorMap.get(errorCode);
	}
	
	/**
	 * This method will convert the document obj to string
	 * 
	 * @param doc
	 * @return String that contain values in XML format
	 * @throws TransformerException
	 */
	public static String convertDocumentObjToString(Document doc) throws TransformerException {
		StringWriter writerOut = new StringWriter();

		// Serialisation through Tranform.
		DOMSource domSource = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(writerOut);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.transform(domSource, streamResult);

		String xmlContent = writerOut.toString();

		return xmlContent;
	}

	/**
	 * Remove <sup> element from the input string. This is mainly used to remove
	 * the <sup> elements from the Investment option name.
	 * 
	 * @param content - String with <sup> elements.
	 * @return - String without <sup> elements.
	 */
	public static String removeSupElement(String content) {
        content = PATTERN_CSV_CONTENT_TAGS_TO_REPLACE_WITH_EMPTY.matcher(content).replaceAll(EMPTY_STRING);
        return content;
	}

	/**
	 * Remove the XML Invalid Characters from the input content.
	 * 
	 * @param content - String that may contain some invalid characters.
	 * @return - String that does not have XML Invalid characters.
	 */
	public static String removeXMLInvalidCharacters(String content) {
        content = PATTERN_PDF_CONTENT_TAGS_TO_REPLACE_WITH_SPACE.matcher(content).replaceAll(SPACE);
        return content;
	}

	/**
	 * This method will check the argument and format the data.
	 * @param object
	 * @return formatted value
	 */
	public static String formatValue(Object object){
		String formattedValue = "";
		
		if(object == null){
			formattedValue = "";
		}
		else if (object instanceof Date){
			formattedValue = DateRender.formatByPattern(object, null,
					RenderConstants.MEDIUM_MDY_SLASHED);			
		}
		else if (object instanceof BigDecimal){			
			NumberFormat numberFormat = DecimalFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);
            formattedValue = numberFormat.format(object);			
		}
		else{
			formattedValue = object.toString();
			if("-".equals(formattedValue)){
				formattedValue = "";
			}
		}
		return formattedValue; 
	}

	
	
	/**
	 * This method will check the argument values and add the attributes ( this is used in ROR elements)
	 * @param pdfDocument
	 * @param element
	 * @param nodeName
	 * @param isHypothetical
	 * @param value
	 */
	public static void formatValueWithAttribute(PDFDocument pdfDocument,
			Element element, String nodeName, boolean isHypothetical, 
			BigDecimal value) {
		
		if(isHypothetical){
			pdfDocument.appendTextNodeWithAttribute(element, nodeName, formatValue(value),
					"is_hypothetical", "true");
		}
		else{
			pdfDocument.appendTextNode(element, nodeName,formatValue(value));
		}
		
	}
	
	/**
	 * Returns the date in the format specified.
	 * 
	 * @param format
	 * @param date
	 * @return String
	 */
	public static String formatDate (String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat (format);
		String dateString = sdf.format(date);
		return dateString;
	}
	
	/**
	 * Return the fund sequence order
	 * @param guaranteedAccountFundId
	 * @return
	 */
	public static Integer getGuarateedAccountFundsOrder(String guaranteedAccountFundId) {
		Integer order = 0;
		if (guaranteedAccountFundsOrderMap.containsKey(guaranteedAccountFundId)) {
			order = guaranteedAccountFundsOrderMap.get(guaranteedAccountFundId);
		}
		
		return order;
	}
}
