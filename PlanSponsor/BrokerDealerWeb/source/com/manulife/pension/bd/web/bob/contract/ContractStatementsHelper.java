package com.manulife.pension.bd.web.bob.contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.util.render.DateRender;

public class ContractStatementsHelper {

	private static final String PDF_FILENAME_EXTENSION = ".pdf";
	private static final String PDF_FILENAME_SEPARATOR = "_";
	private static final String PDF_FILENAME_YEAR_END_IND = "Y";
	private static final String KEY_DATE_FORMAT = "yyyyMMdd";

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

	public static ReportInfo parsePdfFileName(String fileName)
			throws ParseException {
		SimpleDateFormat pdfNameDateFormat = new SimpleDateFormat("yyyyMMdd");
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
	
	// creats a record for drop down list from ReportInfo fields
	public static String buildLabel(ReportInfo reportInfo) {
		StringBuffer strbf = new StringBuffer();
		if (RequestConstants.REPORT_TYPE_CLASS_CONVERSION.equals(reportInfo.getReportType()))
		strbf.append(EFFECTIVE_DATE);
		else
		strbf.append(PERIOD_END);
		strbf.append(DateRender.format(reportInfo.getReportPeriodEndDate(), ""));
		if (!RequestConstants.REPORT_TYPE_CLASS_CONVERSION.equals(reportInfo.getReportType()))
		
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

