/*
 * Created on Mar 23, 2004
 *
 */
package com.manulife.pension.ps.service.report.tools.valueobject;

import java.io.Serializable;
import java.util.ArrayList;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author drotele
 * 
 * ReportData object for UploadHistory Tools page. 
 * Holds collection of UploadHistoryItem objects (as 
 * "details") as well as totals 
 *
 */
public class UploadHistoryReportData
	extends ReportData
	implements Serializable {

	public static final String REPORT_ID = "UploadHistoryReportHandler"; // never used?

	public static final String FILTER_CONTRACT_NO = "contractNumber";

	private int numUploads;

	public UploadHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		this.details = new ArrayList(0);
		// a graceful way to report "No matches"
	}

	/**
	 * @return
	 */
	public int getNumUploads() {
		return numUploads;
	}

	/**
	 * @param i
	 */
	public void setNumUploads(int i) {
		numUploads = i;
	}
}