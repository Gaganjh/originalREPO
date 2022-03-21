package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

import com.manulife.pension.ps.service.report.transaction.reporthandler.CashAccountReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class represents the value object for a cash account report. It is
 * created by the backend and passed to the front end for display.
 * 
 * @author Charles Chan
 */
public class CashAccountReportData extends ReportData {

	/**
	 * Required string report ID.
	 */
	public static final String REPORT_ID = CashAccountReportHandler.class.getName();
	
	/**
	 * Required string report ID.
	 */
	public static final String REPORT_NAME = "cashAccountReport";

	/**
	 * This string will be used for giving CSV File name for NBDW reports.
	 */
	public static final String CSV_REPORT_NAME = "CashAccountHistory";
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
	 * The criteria filter parameter for Page
	 */
	public static final String FILTER_PAGE = "page";	

	/**
	 * Constructor.
	 * 
	 * @param criteria
	 *            The report criteria associated with this report data.
	 * @param totalCount
	 *            The total number of items to be stored in this report data.
	 */
	public CashAccountReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * @return Returns the closingBalanceForPeriod.
	 */
	public BigDecimal getClosingBalanceForPeriod() {
		return closingBalanceForPeriod;
	}

	/**
	 * @param closingBalanceForPeriod
	 *            The closingBalanceForPeriod to set.
	 */
	public void setClosingBalanceForPeriod(BigDecimal closingBalanceForPeriod) {
		this.closingBalanceForPeriod = closingBalanceForPeriod;
	}

	/**
	 * @return Returns the currentBalance.
	 */
	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance
	 *            The currentBalance to set.
	 */
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @return Returns the openingBalanceForPeriod.
	 */
	public BigDecimal getOpeningBalanceForPeriod() {
		return openingBalanceForPeriod;
	}

	/**
	 * @param openingBalanceForPeriod
	 *            The openingBalanceForPeriod to set.
	 */
	public void setOpeningBalanceForPeriod(BigDecimal openingBalanceForPeriod) {
		this.openingBalanceForPeriod = openingBalanceForPeriod;
	}

	/**
	 * @return Returns the totalCreditsForPeriod.
	 */
	public BigDecimal getTotalCreditsForPeriod() {
		return totalCreditsForPeriod;
	}

	/**
	 * @param totalCreditsForPeriod
	 *            The totalCreditsForPeriod to set.
	 */
	public void setTotalCreditsForPeriod(BigDecimal totalCreditsForPeriod) {
		this.totalCreditsForPeriod = totalCreditsForPeriod;
	}

	/**
	 * @return Returns the totalDebitsForPeriod.
	 */
	public BigDecimal getTotalDebitsForPeriod() {
		return totalDebitsForPeriod;
	}

	/**
	 * @param totalDebitsForPeriod
	 *            The totalDebitsForPeriod to set.
	 */
	public void setTotalDebitsForPeriod(BigDecimal totalDebitsForPeriod) {
		this.totalDebitsForPeriod = totalDebitsForPeriod;
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
	
	public String toString() {
	 	
		StringBuffer dump = new StringBuffer();
		
		dump.append("Summary Data --> ").append("\n");
		
		dump.append("currentBalance: ").append(currentBalance).append("\n");
		dump.append("openingBalanceForPeriod: ").append(openingBalanceForPeriod).append("\n");
		dump.append("closingBalanceForPeriod: ").append(closingBalanceForPeriod).append("\n");
		dump.append("totalDebitsForPeriod: ").append(totalDebitsForPeriod).append("\n");
 		dump.append("totalCreditsForPeriod: ").append(totalCreditsForPeriod).append("\n");
 		dump.append("hasMultipleContracts: ").append(hasMultipleContracts).append("\n");
 		dump.append("hasTooManyItems: ").append(hasTooManyItems).append("\n");
 		
 		dump.append("Detail Items --> ").append("\n");
 		dump.append(getDetails().toString()).append("\n");
 		 
 		return dump.toString();
	}
	
	private BigDecimal currentBalance;
	private BigDecimal openingBalanceForPeriod;
	private BigDecimal closingBalanceForPeriod;
	private BigDecimal totalDebitsForPeriod;
	private BigDecimal totalCreditsForPeriod;
	private boolean hasMultipleContracts;
	private boolean hasTooManyItems;
}

