package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class TedHistoryFilesItem implements Serializable {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	public static final String SORT_FIELD_LAST_RUN_DATE = "lastRunDate";
	public static final String SORT_FIELD_QUARTER_END_DATE = "quarterEndDate";
	
	private String quarter;
	private Date quarterEndDate;
	private String year;
	private Date lastRunDate;
	private int fileSize;
	private String fileName;
	private String quarterEndLink="";

	
	/**
	 * @return Returns the lastRunDate.
	 */
	public Date getLastRunDate() {
		return lastRunDate;
	}
	/**
	 * @param lastRunDate The lastRunDate to set.
	 */
	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	/**
	 * @return Returns the fileSize.
	 */
	public int getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize The fileSize to set.
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// Required for the comparator to work correctly in sorting.
	public int equals(Object o1, Object o2) {
		TedHistoryFilesItem lhs = (TedHistoryFilesItem)o1;
		TedHistoryFilesItem rhs = (TedHistoryFilesItem)o2;
	    int result = rhs.getQuarterEndDate().compareTo(lhs.getQuarterEndDate());
		return result;
	}
	/**
	 * @return Returns the quarter.
	 */
	public String getQuarter() {
		return quarter;
	}
	/**
	 * @param quarter The quarter to set.
	 */
	public void setQuarter(Integer quarter) {
		this.quarter = quarter.toString();
	}
	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year The year to set.
	 */
	public void setYear(Integer year) {
		this.year = year.toString();
	}

	public String getQuarterText() {
		String strQuarter = this.year + " Q" + this.quarter;
		return strQuarter;
	}
	
	public String getQuarterEndLink() {
		if (quarterEndLink == null || quarterEndLink.equals("")) {			
			quarterEndLink = "<A href=\"javascript:doDownloadSingleFile('" + getFileName() + "')\">" + getQuarterText() + "</A>";
		}
		return quarterEndLink;
	}
	
	public Date getQuarterEndDate() {
		if (quarterEndDate == null) {
			int iYear = Integer.valueOf(year).intValue();
			int month=0;
			if (quarter.equals("1")) {
				month = 2;
			} else if (quarter.equals("2")) {
				month = 5;
			} else if (quarter.equals("3")) {
				month = 8;
			} else {
				month = 11;
			}
			GregorianCalendar cal = new GregorianCalendar(iYear, month, 1);
			int lastDayOfMonth = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			cal.set(GregorianCalendar.DAY_OF_MONTH, lastDayOfMonth);
			Date qDate = cal.getTime();			
			quarterEndDate = qDate;
		}
		return quarterEndDate;
	}
	
}

