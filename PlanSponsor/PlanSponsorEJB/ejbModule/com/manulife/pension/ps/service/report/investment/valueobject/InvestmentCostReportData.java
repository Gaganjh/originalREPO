package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.service.report.investment.reporthandler.InvestmentCostReportHandler;
import com.manulife.pension.service.fee.util.ContractFeeDetail;
import com.manulife.pension.service.fee.valueobject.ContractDetails;
import com.manulife.pension.service.fee.valueobject.FundFeeVO;
import com.manulife.pension.service.fee.valueobject.InvestmentGroup;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;


/**
 * This class is used as the data object 
 * for the InvestmentCost report it will 
 * hold a map containing the report data.
 * 
 * @author Murali Chandran.
 *
 */
public class InvestmentCostReportData extends ReportData  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String REPORT_ID = InvestmentCostReportHandler.class.getName();
	
	public static final String REPORT_NAME = "ChangedFundsReport";
	
	public static final String FILTER_CONTRACT_NO = "contractNumber";
	
	public static final String FILTER_AS_OF_DATE = "asOfDate";
	
	public static final String FILTER_CHECK_FOR_HISTORY = "checkForFundFeeChanges";
	
	public static final String FILTER_MERRILL_COVERED_FUNDS = "filterMerrillCovered";
	
	public static final String SELECTED_VIEW = "selectedView";
	
	private Date feeEffectiveDate;
	private boolean nonClass1SignatureContract;
	private String contractClassName;
	private int numberOfFundsSelected;
	private Map<InvestmentGroup,List<FundFeeVO>> investmentData = null;
	private boolean hasSvfFund;
	private boolean hasSvpFund;
	
	private ContractDetails contractDetails = null;
	private BigDecimal johnHancockPartyContractCosts;
	private BigDecimal johnHancockPartyInvestmentCosts;
	private boolean isDefinedBenefitContract;
	private List<ContractFeeDetail> recordedEstimatedCostOfRKCharges;
	private BigDecimal averageUnderlyingFundNetCost;
	private BigDecimal averageRevenueFromUnderlyingFund;
	private BigDecimal averageRevenueFromSubAccount;
	private BigDecimal averageTotalRevenueUsedTowardsPlanCosts;
	
	private List<String> feeWaiverFunds;
	private Map<String, com.manulife.pension.service.fund.valueobject.Fund> restrictedFunds;
	

	/**
	 * Constructor 
	 * @param criteria report criteria object
	 * @param totalCount count of the records for the report.
	 */
	public InvestmentCostReportData(ReportCriteria criteria,int totalCount){
		super(criteria,totalCount);
	}
	
	/**
	 * @return the feeEffectiveDate
	 */
	public Date getFeeEffectiveDate() {
		return feeEffectiveDate;
	}

	/**
	 * @param feeEffectiveDate the feeEffectiveDate to set
	 */
	public void setFeeEffectiveDate(Date feeEffectiveDate) {
		this.feeEffectiveDate = feeEffectiveDate;
	}
	
	/**
	 * @param investmentData the investmentData to set
	 */
	public void setInvestmentData(Map<InvestmentGroup,List<FundFeeVO>> investmentData){
		this.investmentData = investmentData;
	}
	
	/**
	 * @return the investmentData
	 */
	public Map<InvestmentGroup,List<FundFeeVO>> getInvestmentData(){
		return this.investmentData;
	}
	
	/**
	 * @param numberOfFundsSelected the numberOfFundsSelected to set
	 */
	public void setNumberOfFundsSelected(int numberOfFundsSelected){
		this.numberOfFundsSelected = numberOfFundsSelected;
	}
	
	/**
	 * @return the numberOfFundsSelected
	 */
	public int getNumberOfFundsSelected(){
		return this.numberOfFundsSelected;
	}
	
	/**
	 * @return the nonClass1SignatureContract
	 */
	public boolean isNonClass1SignatureContract() {
		return nonClass1SignatureContract;
	}
	
	/**
	 * @param nonClass1SignatureContract the nonClass1SignatureContract to set
	 */
	public void setNonClass1SignatureContract(boolean nonClass1SignatureContract) {
		this.nonClass1SignatureContract = nonClass1SignatureContract;
	}
	
	/**
	 * @return the contractClassName
	 */
	public String getContractClassName() {
		return contractClassName;
	}

	/**
	 * @param contractClassName the contractClassName to set
	 */
	public void setContractClassName(String contractClassName) {
		this.contractClassName = contractClassName;
	}
	
	/**
	 * @return the hasSvfFund
	 */
	public boolean getHasSvfFund() {
		return hasSvfFund;
	}

	/**
	 * @param hasSvfFund the hasSvfFund to set
	 */
	public void setHasSvfFund(boolean hasSvfFund) {
		this.hasSvfFund = hasSvfFund;
	}
	
	/**
	 * @return the hasSvpFund
	 */
	public boolean getHasSvpFund() {
		return hasSvpFund;
	}
	
	/**
	 * @param hasSvpFund the hasSvpFund to set
	 */
	public void setHasSvpFund(boolean hasSvpFund) {
		this.hasSvpFund = hasSvpFund;
	}
	
	 /**
	 * Gets the unique footnotes symbols for the investment report
	 * @return Returns an array of String
	 */
    public List<String> getFootNotes() {
    	List<String> footnotesSymbols = new ArrayList<String>();
    	if(this.investmentData != null) {
    		for (List<FundFeeVO>  fundFeeVos : investmentData.values()) {
        		for(FundFeeVO fundFeeVO : fundFeeVos){
        			footnotesSymbols.addAll(fundFeeVO.getFootNoteMarkers());
        		}
        	}
    	}
    	return  footnotesSymbols;
    }
    
	/**
	 * @return the contractDetails
	 */
	public ContractDetails getContractDetails() {
		return contractDetails;
	}
	
	/**
	 * @param contractDetails the contractDetails to set
	 */
	public void setContractDetails(ContractDetails contractDetails) {
		this.contractDetails = contractDetails;
	}
	
	/**
	 * @return the johnHancockPartyContractCosts
	 */
	public BigDecimal getJohnHancockPartyContractCosts() {
		return displayRoundedValue(this.johnHancockPartyContractCosts);
	}
	/**
	 * @param johnHancockPartyContractCosts the johnHancockPartyContractCosts to set
	 */
	public void setJohnHancockPartyContractCosts(
			BigDecimal johnHancockPartyContractCosts) {
		this.johnHancockPartyContractCosts = johnHancockPartyContractCosts;
	}
	
	/**
	 * @return the johnHancockPartyInvestmentCosts
	 */
	public BigDecimal getJohnHancockPartyInvestmentCosts() {
		return displayRoundedValue(this.johnHancockPartyInvestmentCosts);
	}
	/**
	 * @param johnHancockPartyInvestmentCosts the johnHancockPartyInvestmentCosts to set
	 */
	public void setJohnHancockPartyInvestmentCosts(
			BigDecimal johnHancockPartyInvestmentCosts) {
		this.johnHancockPartyInvestmentCosts = johnHancockPartyInvestmentCosts;
	}
	
	/**
	 * @return Return Total JohnHancock Party Costs.
	 */
	public BigDecimal getTotalJohnHancockPartyCosts(){
		return getJohnHancockPartyContractCosts().add(getJohnHancockPartyInvestmentCosts());
	}
	
	/**
	 * If page element is greater than 0.00 and less than 0.01 then returns the value as 0.01  
	 * @param value
	 * @return Big Decimal value
	 */
	public BigDecimal displayRoundedValue(BigDecimal value){
		if(value == null) {
			return new BigDecimal(0.00);
		}
		value = value.setScale(2, RoundingMode.HALF_UP);
		return value;
	}

	/**
	 * @return isDefinedBenefitContract
	 */
	public boolean getIsDefinedBenefitContract() {
		return isDefinedBenefitContract;
	}

	/**
	 * @param isDefinedBenefitContract the isDefinedBenefitContract to set
	 */
	public void setIsDefinedBenefitContract(boolean isDefinedBenefitContract) {
		this.isDefinedBenefitContract = isDefinedBenefitContract;
	}
	
	/**
	 * @return the DisplayContractCostsFootnote
	 */
	public boolean isDisplayContractCostsFootnote() {
		if (johnHancockPartyContractCosts != null && (johnHancockPartyContractCosts.signum() == -1)) {
			return true;
		}
		return false;
	}

	/**
	 * @return the averageUnderlyingFundNetCost
	 */
	public BigDecimal getAverageUnderlyingFundNetCost() {
		return averageUnderlyingFundNetCost;
	}

	/**
	 * @param averageUnderlyingFundNetCost the averageUnderlyingFundNetCost to set
	 */
	public void setAverageUnderlyingFundNetCost(
			BigDecimal averageUnderlyingFundNetCost) {
		this.averageUnderlyingFundNetCost = averageUnderlyingFundNetCost;
	}

    public void setAverageRevenueFromUnderlyingFund(final BigDecimal revenueUnderlyingFund) {
        averageRevenueFromUnderlyingFund = revenueUnderlyingFund;
    }

	/**
	 * @return the averageRevenueFromUnderlyingFund
	 */
	public BigDecimal getAverageRevenueFromUnderlyingFund() {
	    return averageRevenueFromUnderlyingFund;
	}

	/**
	 * @return the averageRevenueFromSubAccount
	 */
	public BigDecimal getAverageRevenueFromSubAccount() {
		return averageRevenueFromSubAccount;
	}

	/**
	 * @param averageRevenueFromSubAccount the averageRevenueFromSubAccount to set
	 */
	public void setAverageRevenueFromSubAccount(
			BigDecimal averageRevenueFromSubAccount) {
		this.averageRevenueFromSubAccount = averageRevenueFromSubAccount;
	}

	public void setAverageTotalRevenueUsedTowardsPlanCosts(final BigDecimal totalRevenue) {
	    averageTotalRevenueUsedTowardsPlanCosts = totalRevenue;
	}
	
	/**
	 * @return the averageTotalRevenueUsedTowardsPlanCosts
	 */
	public BigDecimal getAverageTotalRevenueUsedTowardsPlanCosts() {
		return averageTotalRevenueUsedTowardsPlanCosts;
	}

	/**
	 * @return the averageExpenseRatio
	 */
	public BigDecimal getAverageExpenseRatio() {
		/*ContractDetails contractDetails = getContractDetails();
		if(contractDetails != null && contractDetails.getContractClass() != null && contractDetails.getContractClass().isClassZero()){
			return getAmountValueWithSetScaleTwo(getAverageUnderlyingFundNetCost());
		}else{
			return getAmountValueWithSetScaleTwo(
					getAverageTotalRevenueUsedTowardsPlanCosts())
					.add(getAmountValueWithSetScaleTwo(getAverageUnderlyingFundNetCost()));
		}*/
		return getAmountValueWithSetScaleTwo(
				getAverageTotalRevenueUsedTowardsPlanCosts())
				.add(getAmountValueWithSetScaleTwo(getAverageUnderlyingFundNetCost()));
	}

	/**
	 * @return the recordedEstimatedCostOfRKCharges
	 */
	public List<ContractFeeDetail> getRecordedEstimatedCostOfRKCharges() {
		return recordedEstimatedCostOfRKCharges;
	}

	/**
	 * @param recordedEstimatedCostOfRKCharges the recordedEstimatedCostOfRKCharges to set
	 */
	public void setRecordedEstimatedCostOfRKCharges(
			List<ContractFeeDetail> recordedEstimatedCostOfRKCharges) {
		this.recordedEstimatedCostOfRKCharges = recordedEstimatedCostOfRKCharges;
	}
	
	public BigDecimal getAmountValueWithSetScaleTwo(BigDecimal value) {
		if(value == null){
			return BigDecimal.ZERO;
		}
		return value.scale() > 2 ? value.setScale(2, RoundingMode.HALF_UP) : value;
	}
	
	public void setFeeWaiverFunds(List<String> feeWaiverFunds) {
		this.feeWaiverFunds = feeWaiverFunds;
	}
	
	public boolean isFeeWaiverFund(String fundId) {
		return this.feeWaiverFunds.contains(fundId);
	}

	/**
	 * @return the restrictedFund
	 */
	public boolean isRestrictedFund(String fundId) {
		return this.restrictedFunds.containsKey(fundId);
	}

	/**
	 * @param restrictedFunds the restrictedFunds to set
	 */
	public void setRestrictedFunds(Map<String, com.manulife.pension.service.fund.valueobject.Fund> restrictedFunds) {
		this.restrictedFunds = restrictedFunds;
	}
}
