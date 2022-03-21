package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ps.service.report.transaction.reporthandler.CashAccountBalanceDetailsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class represents the value object for a cash account balance details report. 
 * 
 * @author Rajesh Rajendran
 */
public class CashAccountBalanceDetailsReportData extends ReportData {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Required string report ID.
	 */
	public static final String REPORT_ID = CashAccountBalanceDetailsReportHandler.class.getName();
	
	/**
	 * Required string report name.
	 */
	public static final String REPORT_NAME = "CashAccountBalanceDetailsReport";

	/**
	 * This string will be used for giving CSV File name for cash account balance details reports.
	 */
	public static final String CSV_REPORT_NAME = "CashAccountBalanceDetails";
	
	/**
	 * The criteria filter parameter for from date.
	 */
	public static final String FILTER_FROM_DATE = "fromDate";

	/**
	 * The report criteria filter parameter for to date.
	 */
	public static final String FILTER_TO_DATE = "toDate";

	/**
	 * The criteria filter parameter for contract number
	 */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

	/**
	 * The criteria filter parameter for client ID
	 */
	public static final String FILTER_CLIENT_ID = "clientId";

	/**
	 * As Of Date Filter name
	 */
	public static final String FILTER_AS_OF_DATE = "asOfDate";
	
	/**
	 * Task Filter name
	 */
	public static final String FILTER_TASK = "task";
	
	private BigDecimal currentBalance;
	private Date fromDate;
	private Date toDate;
	private boolean hasMultipleContracts;
	private boolean hasTooManyItems;
	
	/**
	 * Constructor.
	 * 
	 * @param criteria The report criteria associated with this report data.
	 * @param totalCount The total number of items to be stored in this report data.
	 */
	public CashAccountBalanceDetailsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * @return Returns the currentBalance.
	 */
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance The currentBalance to set.
	 */
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
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
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return Returns the hasMultipleContracts.
	 */
	public boolean getHasMultipleContracts() {
		return hasMultipleContracts;
	}

	/**
	 * @param hasMultipleContracts The hasMultipleContracts to set.
	 */
	public void setHasMultipleContracts(boolean hasMultipleContracts) {
		this.hasMultipleContracts = hasMultipleContracts;
	}

	/**
	 * @return Returns the hasTooManyItems.
	 */
	public boolean getHasTooManyItems() {
		return hasTooManyItems;
	}
	
	/**
	 * @param hasTooManyItems The hasTooManyItems to set.
	 */
	public void setHasTooManyItems(boolean hasTooManyItems) {
		this.hasTooManyItems = hasTooManyItems;
	}

    /*
     * (non-Javadoc)
     * @see com.manulife.pension.service.report.valueobject.ReportData#toString()
     */	
	public String toString() {
	 	
		StringBuffer dump = new StringBuffer();
		
		dump.append("Summary Data --> ").append("\n");
		
		dump.append("currentBalance: ").append(currentBalance).append("\n");
 		dump.append("hasMultipleContracts: ").append(hasMultipleContracts).append("\n");
 		dump.append("hasTooManyItems: ").append(hasTooManyItems).append("\n");
 		
 		dump.append("Detail Items --> ").append("\n");
 		dump.append(getDetails().toString()).append("\n");
 		 
 		return dump.toString();
	}
}