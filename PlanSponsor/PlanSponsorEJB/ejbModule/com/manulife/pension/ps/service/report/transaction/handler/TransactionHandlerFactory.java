package com.manulife.pension.ps.service.report.transaction.handler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.CashAccountReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionHistoryReportData;

/**
 * Factory class to return an appropriate transaction handler object based on
 * the transaction type and transaction reason code.
 * 
 * @author Maria Lee
 * @author Charles Chan
 *  
 */
public class TransactionHandlerFactory {

	/**
	 * The singleton instance of the factory.
	 */
	private static final TransactionHandlerFactory instance = new TransactionHandlerFactory(false);
	private static final TransactionHandlerFactory dbInstance = new TransactionHandlerFactory(true);

	/**
	 * The TransactionHandlerMap to house the TransactionHandlers.
	 */
	private TransactionHandlerMap handlerMap;

	/**
	 * Constructor
	 */
	private TransactionHandlerFactory(boolean dbVersion) {
		handlerMap = new TransactionHandlerMap();
		initTransactionHandlers(dbVersion);
	}

	/**
	 * Method to get the instance
	 */
	public static TransactionHandlerFactory getInstance(boolean dbVersion) {
		if (dbVersion) 
			return dbInstance;
		else 
		return instance;
	}

	/**
	 * Method to return the appropriate transaction handler object
	 * 
	 * @param txType
	 *            is the transaction type as stored in Apollo
	 * @param txReason
	 *            is the transaction reason as stored in Apollo
	 * @return TransactionHandler is the corresponding handler for the
	 *         transaction
	 * @exception Exception
	 */
	public TransactionHandler getTransactionHandler(TransactionHandlerKey key)
			throws SystemException {
		return handlerMap.getTransactionHandler(key);
	}

	/**
	 * Initializes the TransactionHandlers and associates them with the right
	 * keys.
	 * 
	 * @param dbVersion indicates if it is a defined benefit version
	 */
	private void initTransactionHandlers(boolean dbVersion) {
		/*
		 * credit txns start
		 */

		/*
		 * Cash Adjustments (CASH ACCOUNT)
		 */
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "BS"),
				new CreditTransactionHandler("Service related credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "CT"),
				new CreditTransactionHandler("Transfer of contract assets"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "ER"),
				new CreditTransactionHandler("Service related credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "FE"),
				new CreditTransactionHandler("Interest credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TC"),
				new CreditTransactionHandler("Temporary credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "UW"),
				new CreditTransactionHandler("Underpayment adjustment"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "WO"),
				new CreditTransactionHandler("Service related credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "FN"),
				new CreditTransactionHandler("Forfeiture of stale dated check"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "FO"),
				new CreditTransactionHandler("Forfeiture of outstanding check "));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "FS"),
				new CreditTransactionHandler("Forfeiture of stale dated check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "LC"),
				new CreditTransactionHandler("Loan consolidation"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "MV"),
				new CreditTransactionHandler("Market value equalizer"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "PN"),
				new CreditTransactionHandler("Redeposit of stale dated participant check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "PO"),
				new CreditTransactionHandler("Redeposit of outstanding participant check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "PS"),
				new CreditTransactionHandler("Redeposit of stale dated participant check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "RM"),
				new CreditTransactionHandler("Reinstatement of participant account"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TI"),
				new CreditTransactionHandler("Transfer of participant assets"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TN"),
				new CreditTransactionHandler("Redeposit of stale dated trustee check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TS"),
				new CreditTransactionHandler("Redeposit of stale dated trustee check"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TU"),
				new CreditTransactionHandler("Redeposit of outstanding trustee check"));
		
		/*
		 * Payment/deposit (CASH ACCOUNT)
		 */
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.PAYMENT_DEPOSITS, "PC"),
				new CreditTransactionHandler("Payment by check"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.PAYMENT_DEPOSITS, "PL"),
				new CreditTransactionHandler("Payment by check"));

		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.PAYMENT_DEPOSITS, "PM"),
						new CreditTransactionHandler(
								"Payment by pre-authorized debit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.PAYMENT_DEPOSITS, "PT"),
				new CreditTransactionHandler("Payment by wire transfer"));
		/*
		 * Withdrawals
		 */
		/*
		 * CASH ACCOUNT
		 */
		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.WITHDRAWAL, "IP"),
						new WithdrawalTransactionHandler(
								"Negative contribution credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "IM"),
				new WithdrawalTransactionHandler("Withdrawal credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "PW"),
				new WithdrawalTransactionHandler("Pre-allocation credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "UM"),
				new WithdrawalTransactionHandler("Unvested money credit"));
		
        handlerMap.putTransactionHandler(new TransactionHandlerKey(
                TransactionType.WITHDRAWAL, "UM", CashAccountReportData.REPORT_ID, "EA"),
                new WithdrawalTransactionHandler("Excess withdrawal credit"));
        
        handlerMap.putTransactionHandler(new TransactionHandlerKey(
                TransactionType.WITHDRAWAL, "UM", CashAccountReportData.REPORT_ID, "EC"),
                new WithdrawalTransactionHandler("Excess withdrawal credit"));
        
        handlerMap.putTransactionHandler(new TransactionHandlerKey(
                TransactionType.WITHDRAWAL, "UM", CashAccountReportData.REPORT_ID, "ED"),
                new WithdrawalTransactionHandler("Excess withdrawal credit"));
        
        handlerMap.putTransactionHandler(new TransactionHandlerKey( 
                TransactionType.WITHDRAWAL, "EA"), 
                new WithdrawalTransactionHandler("Excess annual additions")); 		

		/*
		 * TRANSACTION HISTORY
		 */
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.ADJUSTMENT),
						new AdjustmentTransactionHandler());

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.LOAN_DEFAULT), new GenericTransactionHandler(
				GenericTransactionHandler.DESC_SSN_LAST_FIRST_NAME));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.INTER_ACCOUNT_TRANSFER),
				new InterAccountTransferHandler() );
				
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.LOAN_ISSUE), new GenericTransactionHandler(
		GenericTransactionHandler.DESC_SSN_LAST_FIRST_NAME));		

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.LOAN_TRANSFER), new GenericTransactionHandler(
		GenericTransactionHandler.DESC_SSN_LAST_FIRST_NAME));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.MATURITY_REINVESTMENT),
				new GenericTransactionHandler("Maturity reinvestment"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "DE"),
				new WithdrawalTransactionHandler("Death claim"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "DI"),
				new WithdrawalTransactionHandler("Disability claim"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "EC"),
				new WithdrawalTransactionHandler("Excess contributions"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "ED"),
				new WithdrawalTransactionHandler("Excess deferrals"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "HA"),
				new WithdrawalTransactionHandler("Hardship"));

		if (dbVersion) {
			handlerMap.putTransactionHandler(new TransactionHandlerKey(
					TransactionType.WITHDRAWAL, "IL"),
					new WithdrawalTransactionHandler(
							"Defined Benefit withdrawal for loan"));
	
			handlerMap.putTransactionHandler(new TransactionHandlerKey(
					TransactionType.WITHDRAWAL, "IO"),
					new WithdrawalTransactionHandler("Defined Benefit withdrawal"));			
		} else {
			handlerMap.putTransactionHandler(new TransactionHandlerKey(
					TransactionType.WITHDRAWAL, "IL"),
					new WithdrawalTransactionHandler(
							"In-service withdrawal for loan"));
			
			handlerMap.putTransactionHandler(new TransactionHandlerKey(
					TransactionType.WITHDRAWAL, "IO"),
					new WithdrawalTransactionHandler("Other withdrawal"));
		}

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "IP",
				TransactionHistoryReportData.REPORT_ID),
				new WithdrawalTransactionHandler("Withdrawal to cash account"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "IM",
				TransactionHistoryReportData.REPORT_ID),
				new WithdrawalTransactionHandler("Withdrawal to cash account"));

		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.WITHDRAWAL, "IR"),
						new WithdrawalTransactionHandler(
								"Employee rollover withdrawal"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "MD"),
				new WithdrawalTransactionHandler("Minimum distribution"));

        handlerMap.putTransactionHandler(new TransactionHandlerKey(
                TransactionType.WITHDRAWAL, "MT"),
                new WithdrawalTransactionHandler("Mandatory distribution"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "PD"),
				new WithdrawalTransactionHandler("Pre-retirement withdrawal"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "PW",
				TransactionHistoryReportData.REPORT_ID),
				new WithdrawalTransactionHandler("Pre-allocation surrender"));

		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.WITHDRAWAL, "RA"),
						new WithdrawalTransactionHandler(
								"Retirement, annuity purchase"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "RE"),
				new WithdrawalTransactionHandler("Retirement"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "RO"),
				new WithdrawalTransactionHandler("Retirement, rollover"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "SR"),
				new WithdrawalTransactionHandler(
						"Withdrawal of additional contributions"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "TE"),
				new WithdrawalTransactionHandler("Termination of employment"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "TO"),
				new WithdrawalTransactionHandler("Termination, rollover"));

		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.WITHDRAWAL, "TP"),
						new WithdrawalTransactionHandler(
								"Termination of participation"));
								
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "QD"),
				new WithdrawalTransactionHandler(
						"Qualified Domestic Relations Order"));

		handlerMap
				.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.WITHDRAWAL, "UE"),
						new WithdrawalTransactionHandler(
								"Unvested earnings withdrawal"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "UM",
				TransactionHistoryReportData.REPORT_ID),
				new WithdrawalTransactionHandler("Unvested money withdrawal"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "VC"),
				new WithdrawalTransactionHandler(
						"Voluntary contributions withdrawal"));
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL, "NE"),
				new WithdrawalTransactionHandler(
						"90 day withdrawal election"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.WITHDRAWAL), new WithdrawalTransactionHandler(
				"Withdrawal"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				"WITHDRAWAL","Pending"), new WithdrawalTransactionHandler(
				"Pending withdrawal of additional contributions"));

		/*
		 * Discontinuance
		 */
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.MATURITY_AMOUNT_TRANSFERRED_TO_CASH, "MC"),
				new CreditTransactionHandler("Credit of matured funds"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.DISCONTINUANCE, "TD"),
				new CreditTransactionHandler("Discontinuance credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.DISCONTINUANCE, "TT"),
				new CreditTransactionHandler("Discontinuance credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.DISCONTINUANCE_AMOUNT_BA_BM, "TD"),
				new CreditTransactionHandler("Refund cash account balance"));

		/*
		 * debit txns start
		 */
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.ALLOCATION),
				new ContributionTransactionHandler());

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "OW"),
				new DebitTransactionHandler("Overpayment adjustment"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CASH_ADJUSTMENT, "TP"),
				new DebitTransactionHandler("Repayment of temporary credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CHECK_REQUEST, "OP"),
				new DebitTransactionHandler("Payment to trustees"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.LOAN_REPAYMENT),
				new LoanRepaymentTransactionHandler("Loan repayment"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.PAYMENT_OF_BILLED_CHARGES, "RC"),
				new DebitTransactionHandler("Payment of charges"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CHECK_REQUEST, "MC"),
				new DebitTransactionHandler("Payment to trustees"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.CHECK_REQUEST, "DT"),
				new DebitTransactionHandler("Payment to trustees"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.BILLED_CHARGE_HA_HM_WT, "TD"),
				new DebitTransactionHandler("Payment of charges"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.BILLED_CHARGE_BA_BM, "TD"),
				new DebitTransactionHandler("Payment of charges"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.TEMPORARY_CREDIT_PAYMENT_HA_HM_WT, "TD"),
				new DebitTransactionHandler("Repayment of temporary credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.TEMPORARY_CREDIT_PAYMENT_HA_HM_WT, "PD"),
				new DebitTransactionHandler("Repayment of temporary credit"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.DISCONTINUANCE_AMOUNT_HA_HM_WT, "TD"),
				new DebitTransactionHandler("Refund cash account balance"));

		//added below code as part of PPM:401243
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
						TransactionType.DISCONTINUANCE_AMOUNT_HA_HM_WT, "TT"),
						new DebitTransactionHandler("Refund cash account balance"));
		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.AUTOMATIC_DEDUCTION_OF_UNPAID_CHARGES, "TT"),
				new DebitTransactionHandler("Payment of charges"));

		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.PAYMENT_OF_TEMPORARY_CREDIT, "TT"),
				new DebitTransactionHandler("Repayment of temporary credit"));
		
		
		
		/*
		 * Fees
		 */
		/*
		 * CASH ACCOUNT
		 */
		//TPA Fee
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001"),
				new DebitTransactionHandler("TPA Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001AF"),
				new DebitTransactionHandler("TPA Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001LI"),
				new DebitTransactionHandler("TPA Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001RA"),
				new DebitTransactionHandler("TPA Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001TS"),
				new DebitTransactionHandler("TPA Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400001WD"),
				new DebitTransactionHandler("TPA Withdrawal Fee"));
		//AUD
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002"),
				new DebitTransactionHandler("AUD Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002AF"),
				new DebitTransactionHandler("AUD Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002LI"),
				new DebitTransactionHandler("AUD Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002RA"),
				new DebitTransactionHandler("AUD Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002TS"),
				new DebitTransactionHandler("AUD Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400002WD"),
				new DebitTransactionHandler("AUD Withdrawal Fee"));		
		
		//RIA Fee
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003"),
				new DebitTransactionHandler("RIA Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003AF"),
				new DebitTransactionHandler("RIA Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003LI"),
				new DebitTransactionHandler("RIA Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003RA"),
				new DebitTransactionHandler("RIA Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003TS"),
				new DebitTransactionHandler("RIA Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400003WD"),
				new DebitTransactionHandler("RIA Withdrawal Fee"));	
		
		//TRU Fee
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004"),
				new DebitTransactionHandler("TRU Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004AF"),
				new DebitTransactionHandler("TRU Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004LI"),
				new DebitTransactionHandler("TRU Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004RA"),
				new DebitTransactionHandler("TRU Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004TS"),
				new DebitTransactionHandler("TRU Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400004WD"),
				new DebitTransactionHandler("TRU Withdrawal Fee"));	
		//DBP Fee
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005"),
				new DebitTransactionHandler("DBP Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005AF"),
				new DebitTransactionHandler("DBP Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005LI"),
				new DebitTransactionHandler("DBP Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005RA"),
				new DebitTransactionHandler("DBP Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005TS"),
				new DebitTransactionHandler("DBP Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "400005WD"),
				new DebitTransactionHandler("DBP Withdrawal Fee"));
		//unknown party just in case
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "AF"),
				new DebitTransactionHandler("Admin Fee"));		
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "LI"),
				new DebitTransactionHandler("Loan Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "RA"),
				new DebitTransactionHandler("Investment Advisory Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "TS"),
				new DebitTransactionHandler("Service Fee"));	
		handlerMap.putTransactionHandler(new TransactionHandlerKey(
				TransactionType.FEES_TRANSACTION, "WD"),
				new DebitTransactionHandler("Withdrawal Fee"));	
	}
}