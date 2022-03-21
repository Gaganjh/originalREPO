package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import com.manulife.pension.ps.service.report.transaction.reporthandler.TransactionDetailsRebalReportHandler;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.util.render.SSNRender;

public class TransactionDetailsRebalReportData extends ReportData {
	private static final String SSN_DEFAULT_VALUE = "000000000";
	public static final String REPORT_ID = TransactionDetailsRebalReportHandler.class.getName();
	public static final String REPORT_NAME ="rebalanceReport"; 
	
	public static final String SORT_FIELD_RISK_CATEGORY = "riskCategoryCode";
	public static final String SORT_FIELD_WEBSRTNO = "websrtno";
	public static final String SORT_FIELD_MONEY_TYPE_DESCRIPTION = "moneyTypeDescription";
	
	public static final String FILTER_PARTICIPANT_ID = "participantId";
	public static final String FILTER_TRANSACTION_NUMBER = "transactionNumber";
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

	// summary data
	private String contractNumber;
	private Date transactionDate;
	private Date requestDate;
	private String transactionNumber;
	private String mediaCode;
	private String participantName = "";
	private String participantSSN = "";
	private String participantUnmaskedSSN ="";
	// transfer from and tos
	private List beforeChange;
	private List afterChange;
	
	// redemption & MVA 
	private BigDecimal redemptionFees;
	private BigDecimal mva;
	
	private BigDecimal totalEEBeforeAmount = null;
	private BigDecimal totalEEAfterAmount = null;
	private BigDecimal totalEEBeforePct = null;
	private BigDecimal totalEEAfterPct = null;
	
	private BigDecimal totalERBeforeAmount = null;
	private BigDecimal totalERAfterAmount = null;
	private BigDecimal totalERBeforePct = null;
	private BigDecimal totalERAfterPct = null;
	
	/**
	 * Constructor.
	 */
	public TransactionDetailsRebalReportData() {
		super();
		beforeChange = new ArrayList();
		afterChange = new ArrayList();
	}		
				
	public TransactionDetailsRebalReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		beforeChange = new ArrayList();
		afterChange = new ArrayList();
	}


	/**
	 * Gets the requestDate
	 * @return Returns a Date
	 */
	public Date getRequestDate() {
		return requestDate;
	}
	/**
	 * Sets the requestDate
	 * @param requestDate The requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * Gets the transactionDate
	 * @return Returns a Date
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	/**
	 * Sets the transactionDate
	 * @param transactionDate The transactionDate to set
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Gets the transactionNumber
	 * @return Returns a String
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}
	/**
	 * Sets the transactionNumber
	 * @param transactionNumber The transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * Gets the mediaCode
	 * @return Returns a String
	 */
	public String getMediaCode() {
		return mediaCode;
	}
	/**
	 * Sets the mediaCode
	 * @param mediaCode The mediaCode to set
	 */
	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}

	/**
	 * Gets the beforeChange
	 * @return Returns a List
	 */
	public List getBeforeChange() {		
		return beforeChange;
	}
	/**
	 * Sets the beforeChange
	 * @param beforeChange The beforeChange to set
	 */
	public void setBeforeChange(List beforeChange) {
		this.beforeChange = beforeChange;
	}

	/**
	 * Gets the afterChange
	 * @return Returns a List
	 */
	public List getAfterChange() {
		return afterChange;
	}
	/**
	 * Sets the afterChange
	 * @param afterChange The afterChange to set
	 */
	public void setAfterChange(List afterChange) {		
		this.afterChange = afterChange;		
	}
	
	public BigDecimal getTotalEEAfterAmount() {
		if (totalEEAfterAmount == null) {
			calculateEEAndERAfterTotals();
		}
		return totalEEAfterAmount;
	}
	
	public BigDecimal getTotalEEAfterPct() {
		if (totalEEAfterPct == null) {
			calculateEEAndERAfterTotals();
		}
		return totalEEAfterPct;
	}

	public BigDecimal getTotalERAfterAmount() {
		if (totalERAfterAmount == null) {
			calculateEEAndERAfterTotals();
		}
		return totalERAfterAmount;
	}
	
	public BigDecimal getTotalERAfterPct() {
		if (totalERAfterPct == null) {
			calculateEEAndERAfterTotals();
		}
		return totalERAfterPct;
	}
		
	private void calculateEEAndERAfterTotals() {
		// The rest of the setter determines the totals
		try {
		BigDecimal totalEEAmt = new BigDecimal("0");
		BigDecimal totalEEPct = new BigDecimal("0");
		BigDecimal totalERAmt = new BigDecimal("0");
		BigDecimal totalERPct = new BigDecimal("0");
		
		ListIterator iterator = afterChange.listIterator();
		while(iterator.hasNext()) {
			FundGroup group = (FundGroup)iterator.next();
			Fund[] funds = group.getFunds();
			for (int i=0; i < funds.length; i++) {
				TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
				if (detailsFund.getEmployeeAmount() != null) {
					totalEEAmt = totalEEAmt.add(detailsFund.getEmployeeAmount());
				}
				if (detailsFund.getEmployeePercentage() != null) {
					totalEEPct = totalEEPct.add(detailsFund.getEmployeePercentage());
				}
				if (detailsFund.getEmployerAmount() != null) {
					totalERAmt = totalERAmt.add(detailsFund.getEmployerAmount());
				}
				if (detailsFund.getEmployerPercentage() != null) {
					totalERPct = totalERPct.add(detailsFund.getEmployerPercentage());
				}

			}
		}
		setTotalEEAfterAmount(totalEEAmt);		
		setTotalEEAfterPct(totalEEPct);
		setTotalERAfterAmount(totalERAmt);		
		setTotalERAfterPct(totalERPct);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BigDecimal getTotalEEBeforeAmount() {
		if (totalEEBeforeAmount == null) {
			calculateEEAndERBeforeTotals();
		}
		return totalEEBeforeAmount;
	}

	public BigDecimal getTotalEEBeforePct() {
		if (totalEEBeforePct == null) {
			calculateEEAndERBeforeTotals();
		}
		return totalEEBeforePct;
	}

	public BigDecimal getTotalERBeforeAmount() {
		if (totalERBeforeAmount == null) {
			calculateEEAndERBeforeTotals();
		}
		return totalERBeforeAmount;
	}

	public BigDecimal getTotalERBeforePct() {
		if (totalERBeforePct == null) {
			calculateEEAndERBeforeTotals();
		}
		return totalERBeforePct;
	}
					
	private void calculateEEAndERBeforeTotals() {
		// The rest of the setter determines the totals
		try {
		BigDecimal totalEEAmt = new BigDecimal("0");
		BigDecimal totalEEPct = new BigDecimal("0");
		BigDecimal totalERAmt = new BigDecimal("0");
		BigDecimal totalERPct = new BigDecimal("0");
		ListIterator iterator = beforeChange.listIterator();
		while(iterator.hasNext()) {		
			FundGroup group = (FundGroup)iterator.next();
			Fund[] funds = group.getFunds();
			for (int i=0; i < funds.length; i++) {
				TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
				if (detailsFund.getEmployeeAmount() != null) {
					totalEEAmt = totalEEAmt.add(detailsFund.getEmployeeAmount());
				}
				if (detailsFund.getEmployeePercentage() != null) {
					totalEEPct = totalEEPct.add(detailsFund.getEmployeePercentage());
				}
				if (detailsFund.getEmployerAmount() != null) {
					totalERAmt = totalERAmt.add(detailsFund.getEmployerAmount());
				}
				if (detailsFund.getEmployerPercentage() != null) {
					totalERPct = totalERPct.add(detailsFund.getEmployerPercentage());
				}
			}
		}
		setTotalEEBeforeAmount(totalEEAmt);		
		setTotalEEBeforePct(totalEEPct);
		setTotalERBeforeAmount(totalERAmt);		
		setTotalERBeforePct(totalERPct);
		} catch (Exception e) {
			e.printStackTrace();
		}
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





	/**
	 * Sets the totalEEAfterAmount
	 * @param totalEEAfterAmount The totalEEAfterAmount to set
	 */
	public void setTotalEEAfterAmount(BigDecimal totalEEAfterAmount) {
		this.totalEEAfterAmount = totalEEAfterAmount;
	}

	/**
	 * Sets the totalEEAfterPct
	 * @param totalEEAfterPct The totalEEAfterPct to set
	 */
	public void setTotalEEAfterPct(BigDecimal totalEEAfterPct) {
		this.totalEEAfterPct = totalEEAfterPct;
	}

	/**
	 * Sets the totalEEBeforeAmount
	 * @param totalEEBeforeAmount The totalEEBeforeAmount to set
	 */
	public void setTotalEEBeforeAmount(BigDecimal totalEEBeforeAmount) {
		this.totalEEBeforeAmount = totalEEBeforeAmount;
	}

	/**
	 * Sets the totalEEBeforePct
	 * @param totalEEBeforePct The totalEEBeforePct to set
	 */
	public void setTotalEEBeforePct(BigDecimal totalEEBeforePct) {
		this.totalEEBeforePct = totalEEBeforePct;
	}

	/**
	 * Sets the totalERAfterAmount
	 * @param totalERAfterAmount The totalERAfterAmount to set
	 */
	public void setTotalERAfterAmount(BigDecimal totalERAfterAmount) {
		this.totalERAfterAmount = totalERAfterAmount;
	}

	/**
	 * Sets the totalERAfterPct
	 * @param totalERAfterPct The totalERAfterPct to set
	 */
	public void setTotalERAfterPct(BigDecimal totalERAfterPct) {
		this.totalERAfterPct = totalERAfterPct;
	}

	/**
	 * Sets the totalERBeforeAmount
	 * @param totalERBeforeAmount The totalERBeforeAmount to set
	 */
	public void setTotalERBeforeAmount(BigDecimal totalERBeforeAmount) {
		this.totalERBeforeAmount = totalERBeforeAmount;
	}

	/**
	 * Sets the totalERBeforePct
	 * @param totalERBeforePct The totalERBeforePct to set
	 */
	public void setTotalERBeforePct(BigDecimal totalERBeforePct) {
		this.totalERBeforePct = totalERBeforePct;
	}

	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getParticipantSSN() {
		return participantSSN;
	}
	public void setParticipantSSN(String participantSSN) {
		this.participantSSN = SSNRender.format(participantSSN, SSN_DEFAULT_VALUE, true);
	}
	public String getParticipantUnmaskedSSN() {
		return participantUnmaskedSSN;
	}
	public void setParticipantUnmaskedSSN(String participantUnmaskedSSN) {
		this.participantUnmaskedSSN = participantUnmaskedSSN;
	}
	/**
	 * Gets the ContractNumber
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * Sets the ContractNumber
	 * @param ContractNumber 
	 * The ContractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
}