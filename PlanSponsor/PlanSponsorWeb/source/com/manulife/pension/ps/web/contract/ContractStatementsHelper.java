package com.manulife.pension.ps.web.contract;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.util.render.DateRender;
import com.manulife.pension.ps.web.Constants;

public class ContractStatementsHelper {

	private static final String PDF_FILENAME_EXTENSION = ".pdf";
	private static final String PDF_FILENAME_SEPARATOR = "_";
	private static final String PDF_FILENAME_YEAR_END_IND = "Y";
	private static final String KEY_DATE_FORMAT = "yyyyMMdd";
	
	private static final String CSV_FILENAME_EXTENSION = ".csv";
	private static final String PERIOD_END = "Period end: ";
	private static final String EFFECTIVE_DATE = "Effective date: ";
	private static final String PRODUCED = "  Produced: ";
	private static final String ORIGINAL = "  Original";
	private static final String CORRECTED = "  Corrected";
	private static final String YEAR_END = " (Year end)";
		
	private transient static final Object lock = new Object();

	public static String buildPdfFileName(ReportInfo reportInfo) {

		StringBuffer fileName = new StringBuffer();
		fileName.append(reportInfo.getContractNumber());
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(DateRender.formatByPattern(reportInfo
				.getReportPeriodEndDate(), "", KEY_DATE_FORMAT));
		if (reportInfo.getYearEndReport()) {
			fileName.append(PDF_FILENAME_YEAR_END_IND);
		}
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(reportInfo.getReportType());
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(DateRender.formatByPattern(reportInfo
				.getReportPrintDate(), "", KEY_DATE_FORMAT));
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(reportInfo.getUpdated());
		fileName.append(PDF_FILENAME_EXTENSION);
		return fileName.toString();
	}
	/**
	 *  Build the CSV file name
	 * 
	 * @param report type,contract number,period ending date
	 * @return string filename
	 * 
	 */
	public static String buildCsvFileName(String reportType,int contractNumber,String periodEndDate) {
		
		StringBuffer fileName = new StringBuffer();
		if(StringUtils.equals(reportType,Constants.REPORT_TYPE_CONT_HISTORY)){
			fileName.append(Constants.CONTRIBUTION_HISTORY_CSV_FILE_NAME);
		}
		else if (reportType.equals(Constants.REPORT_TYPE_OUTSTANDING_LOAN)){
			fileName.append(Constants.OUTSTANDING_LOAN_CSV_FILE_NAME);
		}
		else
		{
			fileName.append(Constants.LOAN_REPAYMENT_CSV_FILE_NAME);
		}
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(contractNumber);
		fileName.append(PDF_FILENAME_SEPARATOR);
		fileName.append(periodEndDate.replaceAll("/", ""));
		fileName.append(CSV_FILENAME_EXTENSION);
		return fileName.toString();
	}
	public static ReportInfo parsePdfFileName(String fileName)
			throws ParseException {
		FastDateFormat pdfNameDateFormat = FastDateFormat.getInstance("yyyyMMdd");
		ReportInfo reportInfo = null;
		if (fileName == null || fileName.length() == 0)
			return reportInfo;

		reportInfo = new ReportInfo();

		String[] tokens = StringUtils.split(fileName.substring(0, fileName
				.indexOf(PDF_FILENAME_EXTENSION)), PDF_FILENAME_SEPARATOR);
		reportInfo.setContractNumber(tokens[0]);
		reportInfo.setReportPeriodEndDate(pdfNameDateFormat.parse(tokens[1]
				.substring(0, 8)));
		if (tokens[1].length() > 8
				&& PDF_FILENAME_YEAR_END_IND.equals(tokens[1].substring(8))) {
			reportInfo.setYearEndReport(true);
		}
		reportInfo.setReportType(tokens[2]);
		synchronized (lock) {
			reportInfo.setReportPrintDate(pdfNameDateFormat.parse(tokens[3]));
			reportInfo.setUpdated(tokens[4]);
		}
		return reportInfo;
	}
	
	// Creates a record for drop down list from ReportInfo fields
	public static String buildLabel(ReportInfo reportInfo) {
		StringBuffer strbf = new StringBuffer();
		String reportType = reportInfo.getReportType();
		if (RequestConstants.REPORT_TYPE_CLASS_CONVERSION.equals(reportType))
		strbf.append(EFFECTIVE_DATE);
		else
		strbf.append(PERIOD_END);
		strbf.append(DateRender.format(reportInfo.getReportPeriodEndDate(), ""));
		if ((StringUtils.equals(Constants.REPORT_TYPE_CONT_HISTORY,reportType))
				|| (  StringUtils.equals(Constants.REPORT_TYPE_OUTSTANDING_LOAN,reportType))
						|| (StringUtils.equals(Constants.REPORT_TYPE_LOAN_REPAYMENT,reportType)))
		{
			strbf.append(YEAR_END);
			return strbf.toString();
		}
		if (!RequestConstants.REPORT_TYPE_CLASS_CONVERSION.equals(reportType))
		
			strbf.append(PRODUCED).append(
						DateRender.format(reportInfo.getReportPrintDate(), ""))
				.append(
						(reportInfo.getUpdated()
								.equals(RequestConstants.UPDATED_CORRECTED))
								? CORRECTED
								: ORIGINAL).append(
						(reportInfo.getYearEndReport()) ? YEAR_END : "");
		return strbf.toString();
	}

	
}

