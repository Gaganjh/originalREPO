package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.ParticipantWebsiteReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantWebsiteReportData extends ReportData {

	private static final long serialVersionUID = -5556173639674116802L;

	public static final String REPORT_NAME = "participant Website as of";

	/** The filter parameter for a String object contract number */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

	/** The filter parameter for the fromDate */
	public static final String FILTER_FROM_DATE = "fromDate";

	/** The filter parameter for the toDate */
	public static final String FILTER_TO_DATE = "toDate";

	/** The filter parameter for the task */
	public static final String FILTER_TASK = "task";

	public static final String TASK_PRINT = "print";
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	public FastDateFormat format = FastDateFormat.getInstance("MMM dd, yyyy");
	
	private Integer totalVisitorsCount;

	private Integer totalRepeatVisitorsCount;

	private Integer totalNewVisitorsCount;

	private List<String> mostVisitedMonth;

	private Integer totalDocumentViewCount;

	private Date fromDate;

	private Date toDate;
	
	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return format.format(Calendar.getInstance().getTime());
	}

	/**
	 * @return the totalVisitorsCount
	 */
	public Integer getTotalVisitorsCount() {
		return totalVisitorsCount;
	}

	/**
	 * @param totalVisitorsCount
	 *            the totalVisitorsCount to set
	 */
	public void setTotalVisitorsCount(Integer totalVisitorsCount) {
		this.totalVisitorsCount = totalVisitorsCount;
	}

	/**
	 * @return the totalRepeatVisitorsCount
	 */
	public Integer getTotalRepeatVisitorsCount() {
		return totalRepeatVisitorsCount;
	}

	/**
	 * @param totalRepeatVisitorsCount
	 *            the totalRepeatVisitorsCount to set
	 */
	public void setTotalRepeatVisitorsCount(Integer totalRepeatVisitorsCount) {
		this.totalRepeatVisitorsCount = totalRepeatVisitorsCount;
	}

	/**
	 * @return the totalNewVisitorsCount
	 */
	public Integer getTotalNewVisitorsCount() {
		return totalNewVisitorsCount;
	}

	/**
	 * @param totalNewVisitorsCount
	 *            the totalNewVisitorsCount to set
	 */
	public void setTotalNewVisitorsCount(Integer totalNewVisitorsCount) {
		this.totalNewVisitorsCount = totalNewVisitorsCount;
	}

	/**
	 * @return the totalDocumentViewCount
	 */
	public Integer getTotalDocumentViewCount() {
		return totalDocumentViewCount;
	}

	/**
	 * @param totalDocumentViewCount
	 *            the totalDocumentViewCount to set
	 */
	public void setTotalDocumentViewCount(Integer totalDocumentViewCount) {
		this.totalDocumentViewCount = totalDocumentViewCount;
	}

	/**
	 * @return the mostVisitedMonth
	 */
	public List<String> getMostVisitedMonth() {
		return mostVisitedMonth;
	}

	/**
	 * @param mostVisitedMonth
	 *            the mostVisitedMonth to set
	 */
	public void setMostVisitedMonth(List<String> mostVisitedMonth) {
		this.mostVisitedMonth = mostVisitedMonth;
	}

	/**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public ParticipantWebsiteReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);

	}

	public ParticipantWebsiteReportData() {
		super(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID), 0);

	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
