package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.ps.service.report.transaction.reporthandler.TransactionDetailsClassConversionReportHandler;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class TransactionDetailsClassConversionReportData extends TransactionDetailsIATReportData {

	public static final String REPORT_ID = TransactionDetailsClassConversionReportHandler.class.getName(); 
	public static final String REPORT_NAME = "classConversionReport";
	
	private List transferFromsAndTos;
	/**
	 * Constructor.
	 */
	public TransactionDetailsClassConversionReportData() {
		super();
		transferFromsAndTos = new ArrayList();
	}		
				
	public TransactionDetailsClassConversionReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		transferFromsAndTos = new ArrayList();
	}

	public List getTransferFromsAndTos() {
		return transferFromsAndTos;
	}	
	
	public void setTransferFromsAndTos(List transferFromsAndTos) {
		this.transferFromsAndTos = transferFromsAndTos;
	}
}