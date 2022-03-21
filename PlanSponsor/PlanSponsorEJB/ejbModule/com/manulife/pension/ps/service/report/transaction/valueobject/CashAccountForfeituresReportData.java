package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.manulife.pension.ps.service.report.transaction.reporthandler.CashAccountForfeituresReportHandler;
import com.manulife.pension.service.account.entity.ContractMoneyType;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Class to represent the value object for a cash account Forfeitures report. 
 * 
 * @author Chavva Akhilesh
 *
 */
public class CashAccountForfeituresReportData extends ReportData {

	/**
	 * Default Serial version UID 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Required string report id
	 */
	public static final String REPORT_ID = CashAccountForfeituresReportHandler.class.getName();
	
	/**
	 * Required string report name.
	 */
	public static final String REPORT_NAME = "CashAccountForfeituresReport";
	
	/**
	 * Default sort criteria
	 */
	public static final String DEFAULT_SORT = "transactionDate";
	
	/**
	 * The criteria filter parameter for contract number
	 */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	
	/**
	 * The criteria filter parameter for client ID
	 */
	public static final String FILTER_CLIENT_ID = "clientId";
	
	/**
	 * The report criteria filter parameter for to Money Type.
	 */
	public static final String FILTER_MONEY_ID = "moneyTypeID";
	
	/**
	 * The criteria filter parameter for from date.
	 */
	public static final String FILTER_FROM_DATE = "fromDate";

	/**
	 * The report criteria filter parameter for to date.
	 */
	public static final String FILTER_TO_DATE = "toDate";

	/**
	 * The report criteria filter parameter for task.
	 */
	public static final String FILTER_TASK = "task";

	/**
	 * Money Type key for the all money types filter
	 */
	public static final String ALL_MONEY_TYPES_KEY = "ALL";
	
	/**
	 * Money Type key for the Adjustment money type filter
	 */
	public static final String ADJUSTMENT_MONEY_TYPE_KEY = "AD";
	
	/**
	 * Money Type Value for comparison 
	 */
	public static final String ADJUSTMENT_MONEY_TYPE_VALUE = "Adjustment";
	
	/**
	 * Money type medium name to retrieve data from result set
	 */
	public static final String MONEY_TYPE_MED_NAME = "MONEY_TYPE_MED_NAME";
	
	/**
	 * Money type id medium name to retrieve data from result set
	 */
	public static final String MONEY_TYPE_ID = "MONEY_TYPE_ID";
	
	public BigDecimal totalForfeituresInPlan;
	public BigDecimal totalForfeituresInCashAccount;
	public BigDecimal totalForfeituresInParticipant;
	
	public Date fromDate;
	public Date toDate;
	
	private boolean hasTooManyItems;
	
	public List<ContractMoneyType> listOfContractMoneyTypes;
	
	/**
	 * No Argument constructor 
	 */
	public CashAccountForfeituresReportData() {
		
    }
	
	/**
	 * Constructor
	 * @param criteria
	 * @param totalCount
	 */
	public CashAccountForfeituresReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
    }
	/**
	 * @return totalForfeituresInPlan value
	 */
	public BigDecimal getTotalForfeituresInPlan() {
		return totalForfeituresInPlan;
	}

	public void setTotalForfeituresInPlan(BigDecimal totalForfeituresInPlan) {
		this.totalForfeituresInPlan = totalForfeituresInPlan;
	}
	/**
	 * @return totalForfeituresInCashAccount value
	 */
	public BigDecimal getTotalForfeituresInCashAccount() {
		return totalForfeituresInCashAccount;
	}
	/**
	 * sets totalForfeituresInCashAccount value
	 * @param totalForfeituresInCashAccount
	 */
	public void setTotalForfeituresInCashAccount(
			BigDecimal totalForfeituresInCashAccount) {
		this.totalForfeituresInCashAccount = totalForfeituresInCashAccount;
	}
	/**
	 * @return totalForfeituresInParticipant Account value
	 */
	public BigDecimal getTotalForfeituresInParticipant() {
		return totalForfeituresInParticipant;
	}
	/**
	 * sets totalForfeituresInParticipant value
	 * @param totalForfeituresInParticipant
	 */
	public void setTotalForfeituresInParticipant(
			BigDecimal totalForfeituresInParticipant) {
		this.totalForfeituresInParticipant = totalForfeituresInParticipant;
	}
	/**
	 * @return fromDate value
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * sets fromDate
	 * @param fromDate
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return toDate value
	 */
	public Date getToDate() {
		return toDate;
	}
	/**
	 * sets to date value
	 * @param toDate
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * @return the hasTooManyItems
	 */
	public boolean isHasTooManyItems() {
		return hasTooManyItems;
	}
	/**
	 * @param hasTooManyItems the hasTooManyItems to set
	 */
	public void setHasTooManyItems(boolean hasTooManyItems) {
		this.hasTooManyItems = hasTooManyItems;
	}
	
	/**
	 * @return the listOfContractMoneyTypes
	 */
	public List<ContractMoneyType> getListOfContractMoneyTypes() {
		return listOfContractMoneyTypes;
	}
	
	/**
	 * @param listOfContractMoneyTypes the listOfContractMoneyTypes to set
	 */
	public void setListOfContractMoneyTypes(List<ContractMoneyType> listOfContractMoneyTypes) {
		this.listOfContractMoneyTypes = listOfContractMoneyTypes;
	}
}
