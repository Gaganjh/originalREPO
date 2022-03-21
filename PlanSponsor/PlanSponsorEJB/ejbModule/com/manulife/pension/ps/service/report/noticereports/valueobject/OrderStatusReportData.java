package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.ps.service.report.noticereports.reporthandler.ParticipantWebsiteReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class OrderStatusReportData extends ReportData {

	private static final long serialVersionUID = -5556173639674116802L;

	public static final String REPORT_NAME = "Order Status as of";

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
	
	public static final String INITIATED_STATUS = "Initiated";
    public static final String INITIATED_STATUS_CODE = "IN";
    public static final String ERROR_INVALID_LEGEND = "Error - Invalid order request";
	public static final String ERROR_INVALID_REQUEST_STATUS = "Error - Invalid Order Request";
    public static final String ERROR_INVALID_REQUEST_STATUS_CODE = "ER";
	public static final String IN_PROGRESS_STATUS = "In Progress";
    public static final String IN_PROGRESS_STATUS_CODE = "IP";
	public static final String COMPLETED_STATUS = "Completed";
	public static final String COMPLETED_STATUS_CODE = "CM";
	public static final String NOT_COMPLETED_STATUS = "Not Completed";
	public static final String NOT_COMPLETED_STATUS_CODE = "IC";
	public static final String CANCELLED_STATUS = "Cancelled";
	public static final String CANCELLED_STATUS_CODE = "CN";

	private Date fromDate;

	private Date toDate;
	
	private List<OrderStatusReportVO> orderStatusReportVOList;
	
	private boolean suppressStatusByPercentageGraphTitle = false ;
	
    private boolean suppressInitiatedLegend = false;
    
    private boolean suppressErrorInvalidlegend = false;
    
    private boolean suppressInProgressLegend = false;
    
    private boolean suppressCompleteLegend = false;
    
    private boolean suppressNotCompelteLegend = false;
    
    private boolean suppressCancelledLegend = false;
    
    

	    
	    
	    
	    
	/**
	 * @return the suppressErrorInvalidlegend
	 */
	public boolean isSuppressErrorInvalidlegend() {
		return suppressErrorInvalidlegend;
	}

	/**
	 * @param suppressErrorInvalidlegend the suppressErrorInvalidlegend to set
	 */
	public void setSuppressErrorInvalidlegend(boolean suppressErrorInvalidlegend) {
		this.suppressErrorInvalidlegend = suppressErrorInvalidlegend;
	}

	/**
	 * @return the suppressInProgressLegend
	 */
	public boolean isSuppressInProgressLegend() {
		return suppressInProgressLegend;
	}

	/**
	 * @param suppressInProgressLegend the suppressInProgressLegend to set
	 */
	public void setSuppressInProgressLegend(boolean suppressInProgressLegend) {
		this.suppressInProgressLegend = suppressInProgressLegend;
	}

	/**
	 * @return the suppressCompleteLegend
	 */
	public boolean isSuppressCompleteLegend() {
		return suppressCompleteLegend;
	}

	/**
	 * @param suppressCompleteLegend the suppressCompleteLegend to set
	 */
	public void setSuppressCompleteLegend(boolean suppressCompleteLegend) {
		this.suppressCompleteLegend = suppressCompleteLegend;
	}

	/**
	 * @return the suppressNotCompelteLegend
	 */
	public boolean isSuppressNotCompelteLegend() {
		return suppressNotCompelteLegend;
	}

	/**
	 * @param suppressNotCompelteLegend the suppressNotCompelteLegend to set
	 */
	public void setSuppressNotCompelteLegend(boolean suppressNotCompelteLegend) {
		this.suppressNotCompelteLegend = suppressNotCompelteLegend;
	}

	/**
	 * @return the suppressCancelledLegend
	 */
	public boolean isSuppressCancelledLegend() {
		return suppressCancelledLegend;
	}

	/**
	 * @param suppressCancelledLegend the suppressCancelledLegend to set
	 */
	public void setSuppressCancelledLegend(boolean suppressCancelledLegend) {
		this.suppressCancelledLegend = suppressCancelledLegend;
	}

	/**
	 * @return the suppressInitiatedLegend
	 */
	public boolean isSuppressInitiatedLegend() {
		return suppressInitiatedLegend;
	}

	/**
	 * @param suppressInitiatedLegend the suppressInitiatedLegend to set
	 */
	public void setSuppressInitiatedLegend(boolean suppressInitiatedLegend) {
		this.suppressInitiatedLegend = suppressInitiatedLegend;
	}

	/**
	 * @return the suppressStatusByPercentageGraphTitle
	 */
	public boolean isSuppressStatusByPercentageGraphTitle() {
		return suppressStatusByPercentageGraphTitle;
	}

	/**
	 * @param suppressStatusByPercentageGraphTitle
	 *            the suppressStatusByPercentageGraphTitle to set
	 */
	public void setSuppressStatusByPercentageGraphTitle(
			boolean suppressStatusByPercentageGraphTitle) {
		this.suppressStatusByPercentageGraphTitle = suppressStatusByPercentageGraphTitle;
	}
	
	
	

	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return format.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * @return the orderStatusReportVOList
	 */
	public List<OrderStatusReportVO> getOrderStatusReportVOList() {
		return orderStatusReportVOList;
	}

	/**
	 * @param orderStatusReportVOList the orderStatusReportVOList to set
	 */
	public void setOrderStatusReportVOList(
			List<OrderStatusReportVO> orderStatusReportVOList) {
		this.orderStatusReportVOList = orderStatusReportVOList;
	}

	/**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public OrderStatusReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	public OrderStatusReportData() {
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
