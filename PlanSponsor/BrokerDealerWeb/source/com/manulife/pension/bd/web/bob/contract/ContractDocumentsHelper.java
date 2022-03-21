package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.DATE_FORMAT_MMDDYYYY;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SINGLE_SPACE_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.lp.bos.ereports.common.EReportsSystemException;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.ps.cache.CofidProperties;
import com.manulife.util.render.DateRender;

/**
 * This is a helper class for Contract documents, it's used to build contract documents fileName,
 * pasre and retrieve the information from the contract document's fileName.
 * 
 * @author Siby Thomas
 * 
 */
public class ContractDocumentsHelper {

	private static final String PDF_FILENAME_EXTENSION = ".pdf";

	
	
	/**
	 * This method parses the PDF file name and retreives informations like ContractDocType,
	 * Document created date and Contract number.
	 * @param fileName
	 * @return ContractDocumentInfo
	 * @throws ParseException
	 */
	public static ContractDocumentInfo parseContractDocPdfFileName(
			String fileName) throws ParseException {
		SimpleDateFormat pdfNameDateFormat = new SimpleDateFormat(DATE_FORMAT_MMDDYYYY);
		ContractDocumentInfo contractDocInfo = null;
		if (fileName == null || fileName.length() == 0)
			return contractDocInfo;

		contractDocInfo = new ContractDocumentInfo();

		String[] tokens = StringUtils.split(fileName.substring(0, fileName
				.indexOf(PDF_FILENAME_EXTENSION)),
				HYPHON_SYMBOL);
		if (tokens.length == 4) {
			contractDocInfo.setContractNumber(tokens[0]);
			contractDocInfo.setDocumentType(tokens[1]);
			contractDocInfo.setDocumentSubType(tokens[2]);
			contractDocInfo.setDocumentCreatedDate(pdfNameDateFormat
					.parse(tokens[3]));
		}else {
			contractDocInfo.setContractNumber(tokens[0]);
			contractDocInfo.setDocumentType(tokens[1]);
			contractDocInfo.setDocumentCreatedDate(pdfNameDateFormat
					.parse(tokens[2]));
		}
		

		return contractDocInfo;
	}

	/**
	 * This method contructs the label for Contract/Amendment documents for showing in the 
	 * dropdown in ContractDocuments page. 
	 * @param contractDocInfo
	 * @return String
	 */
	public static String buildContractLabel(ContractDocumentInfo contractDocInfo) {
		StringBuffer strbf = new StringBuffer();
		strbf.append(DateRender.format(
				contractDocInfo.getDocumentAvailableTS(), ""));
		strbf.append(SINGLE_SPACE_SYMBOL);
		if(contractDocInfo.getDocumentAvailableTS()!= null){
			DateFormat format = new SimpleDateFormat( "hh:mm:ss a" );
			String str = format.format( contractDocInfo.getDocumentAvailableTS() );
			strbf.append(str);
		}else{
			strbf.append("00:00:00 AM");
		}
		return strbf.toString();
	}

	/**
	 * This method builds the ContractDocPdfFileName
	 * @param contractInfo
	 * @return String
	 */
	public static String buildContractDocPdfFileName(
			ContractDocumentInfo contractInfo) {

		StringBuilder fileName = new StringBuilder();
		fileName.append(contractInfo.getContractNumber());
		fileName.append(HYPHON_SYMBOL);
		fileName.append(contractInfo.getDocumentType());
		fileName.append(HYPHON_SYMBOL);
		String documentSubType = contractInfo.getDocumentSubType();
		if(StringUtils.isNotBlank(documentSubType) ){
			fileName.append(contractInfo.getDocumentSubType().trim());
			fileName.append(HYPHON_SYMBOL);
		}
		fileName.append(DateRender.formatByPattern(contractInfo
				.getDocumentCreatedDate(),
                SPACE_SYMBOL, DATE_FORMAT_MMDDYYYY));
		fileName.append(PDF_FILENAME_EXTENSION);
		return fileName.toString();
	}
	
	/**
	 * This method builds the ContractDocPdfFileName
	 * @param contractInfo
	 * @return String
	 * @throws 	 
	 */
	public static String getPdfReportFileName(ContractDocumentInfo contractInfo) throws EReportsSystemException{
		
	String fileName = CofidProperties.properties.getProperty(MessageFormat.format(CofidProperties.PLAN_REVIEW_FILE_NAME, contractInfo.getPartyIdentifierCode().trim()));	
	String 	formattedQuarterEndDate = getFormattedQuarterEndDate(contractInfo.getDocumentCreatedDate());
		fileName = fileName+formattedQuarterEndDate+"_"+contractInfo.getContractNumber()+".pdf";
	return fileName;
	}

	public static String getFormattedQuarterEndDate(Date quarterEndDate) throws EReportsSystemException{
		Calendar calendar = Calendar.getInstance(Locale.US);
		calendar.setTime(quarterEndDate);
		return  calendar.get(Calendar.YEAR) + "_Q"
				+ ((calendar.get(Calendar.MONTH) / 3) + 1);
		
	}



}
