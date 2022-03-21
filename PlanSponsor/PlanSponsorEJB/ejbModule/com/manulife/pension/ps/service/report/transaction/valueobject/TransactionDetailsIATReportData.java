package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.manulife.pension.ps.service.report.transaction.reporthandler.TransactionDetailsFTFReportHandler;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.util.render.SSNRender;

public abstract class TransactionDetailsIATReportData extends ReportData {
	private static final String SSN_DEFAULT_VALUE = "000000000";
	
	public static final String SORT_FIELD_RISK_CATEGORY = "riskCategoryCode";
	public static final String SORT_FIELD_WEBSRTNO = "websrtno";
	public static final String SORT_FIELD_MONEY_TYPE_DESCRIPTION = "moneyTypeDescription";
	
	public static final String FILTER_TRANSACTION_NUMBER = "transactionNumber";
	public static final String FILTER_PARTICIPANT_ID = "participantId";	
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	
	public static final String CONTRACT_TYPE_DB = "isDBContract"; // defined benefit contract	
	
	// summary data
	private Date transactionDate;
	private Date requestDate;
	private BigDecimal totalAmount;
	private String transactionNumber;
	private String contractNumber;
	private String mediaCode;
	private String sourceOfTransfer;
	private String participantName = "";
	private String participantSSN = "";
	private String participantUnmaskedSSN = "";
	private Boolean fromMoneyTypesExist=null;
	
	private Boolean fromHasPercent = null;
	
	public boolean doFromMoneyTypesExist() {
		if (fromMoneyTypesExist == null) {
			// We need to get it set by calling getF
			calculateFromTotals();
		}
		return fromMoneyTypesExist.booleanValue();
	}
	
	public void setFromMoneyTypesExist(boolean fromMoneyTypesExist) {
		this.fromMoneyTypesExist = new Boolean(fromMoneyTypesExist);
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
	// transfer from and tos
	private List transferFroms;
	private List transferTos;
	
	private BigDecimal totalToAmount = null;
	private BigDecimal totalFromAmount = null;
	private BigDecimal totalToPct = null;
	private BigDecimal totalFromPct = null;

	/**
	 * Constructor.
	 */
	public TransactionDetailsIATReportData() {
		super();
		transferFroms = new ArrayList();
		transferTos = new ArrayList();
	}		
				
	public TransactionDetailsIATReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		transferFroms = new ArrayList();
		transferTos = new ArrayList();
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
	 * Gets the totalAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	/**
	 * Sets the totalAmount
	 * @param totalAmount The totalAmount to set
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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
	 * Gets the sourceOfTransfer
	 * @return Returns a String
	 */
	public String getSourceOfTransfer() {
		return sourceOfTransfer;
	}
	/**
	 * Sets the sourceOfTransfer
	 * @param sourceOfTransfer The sourceOfTransfer to set
	 */
	public void setSourceOfTransfer(String sourceOfTransfer) {
		this.sourceOfTransfer = sourceOfTransfer;
	}

	/**
	 * Gets the transferFroms
	 * @return Returns a List
	 */
	public List getTransferFroms() {		
		return transferFroms;
	}
	/**
	 * Sets the transferFroms
	 * @param transferFroms The transferFroms to set
	 */
	public void setTransferFroms(List transferFroms) {
		this.transferFroms = transferFroms;
	}

	/**
	 * Gets the transferTos
	 * @return Returns a List
	 */
	public List getTransferTos() {
		return transferTos;
	}
	/**
	 * Sets the transferTos
	 * @param transferTos The transferTos to set
	 */
	public void setTransferTos(List transferTos) {		
		this.transferTos = transferTos;		
	}
	
	/**
	 * Gets the totalFromAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTotalFromAmount() {
		if (totalFromAmount == null) {
			calculateFromTotals();
		}
		return totalFromAmount;
	}
	
	private void calculateFromTotals() {
		// The rest of the setter determines the totals
		BigDecimal totalAmt = new BigDecimal("0");
		BigDecimal totalPct = new BigDecimal("0");
		ListIterator iterator = transferFroms.listIterator();
		boolean moneyTypeFound = false;
		while(iterator.hasNext()) {		
			FundGroup group = (FundGroup)iterator.next();
			Fund[] funds = group.getFunds();			
			for (int i=0; i < funds.length; i++) {
				TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
				totalAmt = totalAmt.add(detailsFund.getAmount());
				totalPct = totalPct.add(detailsFund.getPercentage());
				if (!detailsFund.getMoneyTypeDescription().trim().equals("")) {
					moneyTypeFound = true;
				}
			}
		}
		setTotalFromAmount(totalAmt);		
		setTotalFromPct(totalPct);
		setFromMoneyTypesExist(moneyTypeFound);
	}
	
	/**
	 * Sets the totalFromAmount
	 * @param totalFromAmount The totalFromAmount to set
	 */
	public void setTotalFromAmount(BigDecimal totalFromAmount) {
		this.totalFromAmount = totalFromAmount;
	}

	/**
	 * Gets the totalToAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTotalToAmount() {
		if (totalToAmount == null) {
			calculateToTotals();
		}		
		return totalToAmount;
	}
	/**
	 * Sets the totalToAmount
	 * @param totalToAmount The totalToAmount to set
	 */
	public void setTotalToAmount(BigDecimal totalToAmount) {
		this.totalToAmount = totalToAmount;
	}

	/**
	 * Gets the totalToPct
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTotalToPct() {
		if (totalToPct == null) {
			calculateToTotals();
		}		
		return totalToPct;
	}
	
	private void calculateToTotals() {
		// The rest of the setter determines the totals
		BigDecimal totalAmt = new BigDecimal("0");
		BigDecimal totalPct = new BigDecimal("0");
		ListIterator iterator = transferTos.listIterator();
		while(iterator.hasNext()) {
			FundGroup group = (FundGroup)iterator.next();
			Fund[] funds = group.getFunds();			
			for (int i=0; i < funds.length; i++) {
				TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
				totalAmt = totalAmt.add(detailsFund.getAmount());
				totalPct = totalPct.add(detailsFund.getPercentage());
			}
		}
		setTotalToAmount(totalAmt);		
		setTotalToPct(totalPct);		
	}
	
	public boolean showMoneyTypeColumnInFromSection() {
	
		Iterator iterator = transferFroms.iterator();
		while(iterator.hasNext()) {
			FundGroup group = (FundGroup)iterator.next();
			Fund[] funds = group.getFunds();			
			for (int i=0; i < funds.length; i++) {
				TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
				if (detailsFund.getMoneyTypeDescription() != null
						&& detailsFund.getMoneyTypeDescription().length() > 0)  
					return true;	
			}
		}		
		return false;
					
	}	
		
	/**
	 * Sets the totalToPct
	 * @param totalToPct The totalToPct to set
	 */
	public void setTotalToPct(BigDecimal totalToPct) {
		this.totalToPct = totalToPct;
	}

	/**
	 * Gets the totalFromPct
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTotalFromPct() {
		if (totalFromPct == null) {
			calculateFromTotals();
		}
		return totalFromPct;
	}
	/**
	 * Sets the totalFromPct
	 * @param totalFromPct The totalFromPct to set
	 */
	public void setTotalFromPct(BigDecimal totalFromPct) {
		this.totalFromPct = totalFromPct;
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
	/**
	 * Returns whether we are showing the Percent column
	 *  
	 * @return Returns the fromHasPercent.
	 */
	public boolean showFromPercent() { 
		if (fromHasPercent == null) {
			fromHasPercent = new Boolean(false);
			Iterator iterator = transferFroms.iterator();
			while(iterator.hasNext()) {
				FundGroup group = (FundGroup)iterator.next();
				Fund[] funds = group.getFunds();			
				for (int i=0; i < funds.length; i++) {
					TransactionDetailsFund detailsFund = (TransactionDetailsFund)funds[i];
					if (detailsFund.getPercentage() != null
							&& detailsFund.getPercentage().doubleValue() != (double)0)  
						fromHasPercent = new Boolean(true);
				}
			}		

		}
		return fromHasPercent.booleanValue();
	}
}