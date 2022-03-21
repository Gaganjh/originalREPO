package com.manulife.pension.ps.service.report.transaction.handler;

/**
 * @author Charles Chan
 */
public interface TransactionType {

	String ADJUSTMENT = "AD";
	
	String LOAN_DEFAULT = "LD";
	
	String CASH_ADJUSTMENT = "CA";
	
	String PAYMENT_DEPOSITS = "PY";
	
	String WITHDRAWAL = "WD";
	
	String NEGATIVE_ELECTION = "NE";

	String MATURITY_AMOUNT_TRANSFERRED_TO_CASH = "MC";
	
	String DISCONTINUANCE = "DI";
	
	String ALLOCATION = "AL";

	String CHECK_REQUEST = "CR";
	
	String PAYMENT_OF_BILLED_CHARGES = "PC";
	
	String BILLED_CHARGE_HA_HM_WT = "BA";
	
	String BILLED_CHARGE_BA_BM = "BB";
	
	String TEMPORARY_CREDIT_PAYMENT_HA_HM_WT = "KA";
	
	String DISCONTINUANCE_AMOUNT_HA_HM_WT = "DA";
	
	String DISCONTINUANCE_AMOUNT_BA_BM = "DB";

	String AUTOMATIC_DEDUCTION_OF_UNPAID_CHARGES = "AW";
	
	String PAYMENT_OF_TEMPORARY_CREDIT = "KP";
	
	String INTER_ACCOUNT_TRANSFER = "IT";

	String LOAN_ISSUE = "LI";
	
	String LOAN_TRANSFER = "LT";

	String LOAN_REPAYMENT = "LR";

	String MATURITY_REINVESTMENT = "MR";
	String FEES_TRANSACTION = "FC";

}
