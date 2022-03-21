package com.manulife.pension.ps.service.report.sendservice.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.report.sendservice.valueobject.ReportData;
import com.manulife.pension.service.common.valueobject.ReportCriteria;

/**
 * 
 * Report Data for Send Service
 * 
 * @author Dheepa
 *
 */
public class SendServiceReportData extends ReportData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public SendServiceReportData(ReportCriteria criteria, int totalCount) {
		super(criteria,totalCount);
	}
}