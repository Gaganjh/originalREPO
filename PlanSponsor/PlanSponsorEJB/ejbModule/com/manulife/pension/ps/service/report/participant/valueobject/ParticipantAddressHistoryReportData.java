package com.manulife.pension.ps.service.report.participant.valueobject;

import java.util.Calendar;
import java.util.Date;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantAddressHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ParticipantAddressHistoryReportData extends ReportData {
	private static final long serialVersionUID = 1L;
	
	public static final String REPORT_ID = ParticipantAddressHistoryReportHandler.class.getName();
	public static final String REPORT_NAME = "participantAddressHistoryReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	public static final String FILTER_FIELD_2 = "lastName";
	public static final String FILTER_FIELD_3 = "ssn";
	public static final String FILTER_FIELD_4 = "fromDate";
	public static final String FILTER_FIELD_5 = "toDate";
	public static final String FILTER_STATUS_FIELD = "status";
	public static final String FILTER_SEGMENT_FIELD = "segment";
	public static final String FILTER_DIVISION = "division";
	public static final String FILTER_STATUS_FOR_EXTERNAL_FIELD = "externalStatusFilter";
	
	private Date lastUpdatedDate;
	private int contractNumber;
	private boolean downloadEmployeeNumber;

	public ParticipantAddressHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	/**
	 * Gets the lastUpdatedDate
	 * @return Returns a Date
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * Sets the lastUpdatedDate
	 * @param lastUpdatedDate The lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdatedDate);
		if(calendar.get(Calendar.YEAR) != 9999 && calendar.get(Calendar.YEAR) != 1)
		{
			this.lastUpdatedDate = lastUpdatedDate;
		}

	}

	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public int getContractNumber() {
		return contractNumber;
	}
	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public String toString() {
		StringBuffer dump = new StringBuffer();
		dump.append( super.toString() ).append("\n");
		dump.append("lastUpdatedDate: ").append(getLastUpdatedDate()).append("\n");
		dump.append("contractNumber: ").append(getContractNumber()).append("\n");		
		dump.append("downloadEmployeeNumber: ").append(isDownloadEmployeeNumber()).append("\n");
		return dump.toString();
	}	

	/**
	 * Gets the downloadEmployeeNumber
	 * @return Returns a boolean
	 */
	public boolean isDownloadEmployeeNumber() {
		return downloadEmployeeNumber;
	}
	/**
	 * Sets the downloadEmployeeNumber
	 * @param downloadEmployeeNumber The downloadEmployeeNumber to set
	 */
	public void setDownloadEmployeeNumber(boolean downloadEmployeeNumber) {
		this.downloadEmployeeNumber = downloadEmployeeNumber;
	}

}


