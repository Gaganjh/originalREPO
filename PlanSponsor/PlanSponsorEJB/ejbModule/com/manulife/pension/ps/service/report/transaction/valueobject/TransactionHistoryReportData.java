package com.manulife.pension.ps.service.report.transaction.valueobject;

import com.manulife.pension.ps.service.report.transaction.reporthandler.TransactionHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Charles Chan
 * @author Maria Lee
 */
public class TransactionHistoryReportData extends ReportData {

	public static final String REPORT_ID = TransactionHistoryReportHandler.class.getName();

	public static final String REPORT_NAME = "transactionHistoryReport"; 

	/**
	 * The criteria filter parameter for from date. (A Date object)
	 */
	public static final String FILTER_FROM_DATE = "fromDate";

	/**
	 * The report criteria filter parameter for to date. (A Date object)
	 */
	public static final String FILTER_TO_DATE = "toDate";

	/**
	 * The criteria filter parameter for contract number
	 */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	
	/**
	 * The criteria filter parameter for proposal number
	 */
	public static final String FILTER_PROPOSAL_NUMBER = "proposalNumber";


	/**
	 * The criteria filter parameter for types (A collection of type String)
	 */
	public static final String FILTER_TYPE_LIST = "types";

	/**
	 * Sorts by transaction date
	 */
	public static final String SORT_FIELD_DATE = "date";

	/**
	 * Sorts by transaction amount
	 */
	public static final String SORT_FIELD_AMOUNT = "amount";

	/**
	 * Sorts by transaction number
	 */
	public static final String SORT_FIELD_NUMBER = "number";
	
	public static final String CONTRACT_TYPE_DB = "isDBContract"; // defined benefit contract


	/** indicator for any loan transactions */
	private boolean hasLoans = false;

	public TransactionHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	/**
	 * Gets the hasLoans
	 * @return Returns a boolean
	 */
	public boolean hasLoans() {
		return hasLoans;
	}
	/**
	 * Sets the hasLoans
	 * @param hasLoans The hasLoans to set
	 */
	public void setHasLoans(boolean hasLoans) {
		this.hasLoans = hasLoans;
	}

}

