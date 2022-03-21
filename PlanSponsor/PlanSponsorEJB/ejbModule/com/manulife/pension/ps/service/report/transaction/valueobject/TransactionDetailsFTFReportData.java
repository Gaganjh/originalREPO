package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

import com.manulife.pension.ps.service.report.transaction.reporthandler.TransactionDetailsFTFReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

public class TransactionDetailsFTFReportData extends TransactionDetailsIATReportData {

	public static final String REPORT_ID = TransactionDetailsFTFReportHandler.class.getName(); 
	public static final String REPORT_NAME = "fundToFundTransferReport";
	
	// redemption & MVA 
	private BigDecimal redemptionFees;
	private BigDecimal mva;
	
	/**
	 * Constructor.
	 */
	public TransactionDetailsFTFReportData() {
		super();
	}		
				
	public TransactionDetailsFTFReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}


	public boolean showComments() {
		
		return (redemptionFees.doubleValue() != 0)    
 			|| (mva.doubleValue() != 0);
					
	}
	
	/**
	 * Gets the redemptionFees
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getRedemptionFees() {
		if (redemptionFees == null) {
			redemptionFees = new BigDecimal("0");
		}
		return redemptionFees;
	}
	/**
	 * Sets the redemptionFees
	 * @param redemptionFees The redemptionFees to set
	 */
	public void setRedemptionFees(BigDecimal redemptionFees) {
		this.redemptionFees = redemptionFees;
	}


	/**
	 * Gets the mva
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getMva() {
		if (mva == null) {
			mva = new BigDecimal("0");
		}
		return mva;
	}
	/**
	 * Sets the mva
	 * @param mva The mva to set
	 */
	public void setMva(BigDecimal mva) {
		this.mva = mva;
	}
}