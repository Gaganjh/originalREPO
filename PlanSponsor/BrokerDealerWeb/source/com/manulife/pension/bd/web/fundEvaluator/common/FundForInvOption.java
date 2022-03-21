package com.manulife.pension.bd.web.fundEvaluator.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.manulife.pension.service.fund.valueobject.Fund;

/**
 * This object is wrapper to Fund object
 * @author PWakode
 */
public class FundForInvOption implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Fund fund;
    private String rateType;
    private int displayRank;
    private BigDecimal rank = new BigDecimal(-1);
    private boolean benchmarkMetricsAvailable = false;
    
    //Fund attributes
    private boolean toolSelected = false;
    private boolean isSelected = false;
    private boolean isChecked = false;
    private boolean selectedAndModifiable= false;
    private boolean isBrokerSelected = false;
    private boolean isContractSelected = false;
    private boolean isClosedToNB = false;
    private boolean isIndex = false;
    private boolean isSVFCompetingFund= false;
    private boolean isSVFFund= false;
    
    private boolean isPBACompetingFund= false;
    
    //criteria names and values
    private ArrayList<String> criteriaShortNamesForScreen = new ArrayList<String>();
    private ArrayList<String> criteriaLongNamesForScreen = new ArrayList<String>();
    private ArrayList<String> resultPercentileCriteria = new ArrayList<String>();
    private ArrayList<String> resultPercentileConvertedToRankCriteria = new ArrayList<String>();
    private ArrayList<String> resultValueCriteria = new ArrayList<String>();
    
    private String overallPercentile;
    private String overallDisplayRank;
    
    //FundSheet URL related attributes, mainly for Guaranteed funds. These attributes are preset for all funds in Fund object except Guaranteed funds
    private String classShortName;
    
    public FundForInvOption(Fund fund) {
        Fund fundobj = new Fund(fund.getFundId(), fund.getFundName());
        new Fund(fund.getFundId(), fund.getFundName());
        this.fund = fund;
        fundobj.setSortNumber(fund.getSortNumber());
    }

    /**
     * @return the rateType
     */
    public String getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the benchmarkMetricsAvailable
     */
    public boolean isBenchmarkMetricsAvailable() {
        return benchmarkMetricsAvailable;
    }

    /**
     * @param benchmarkMetricsAvailable the benchmarkMetricsAvailable to set
     */
    public void setBenchmarkMetricsAvailable(boolean benchmarkMetricsAvailable) {
        this.benchmarkMetricsAvailable = benchmarkMetricsAvailable;
    }

    /**
     * @return the toolSelected
     */
    public boolean isToolSelected() {
        return toolSelected;
    }

    /**
     * @param toolSelected the toolSelected to set
     */
    public void setToolSelected(boolean toolSelected) {
        this.toolSelected = toolSelected;
    }

    public String getOverallPercentile() {
        return overallPercentile;
    }

    public void setOverallPercentile(String overallPercentile) {
        this.overallPercentile = overallPercentile;
    }
    public Fund getFund() {
        return this.fund;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelectedAndModifiable() {
        return selectedAndModifiable;
    }

    public void setSelectedAndModifiable(boolean selectedAndModifiable) {
        this.selectedAndModifiable = selectedAndModifiable;
    }

    public boolean isBrokerSelected() {
        return isBrokerSelected;
    }

    public void setBrokerSelected(boolean isBrokerSelected) {
        this.isBrokerSelected = isBrokerSelected;
    }

    public boolean isContractSelected() {
        return isContractSelected;
    }

    public void setContractSelected(boolean isContractSelected) {
        this.isContractSelected = isContractSelected;
    }
    
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public ArrayList<String> getCriteriaLongNamesForScreen() {
        return criteriaLongNamesForScreen;
    }

    public void setCriteriaLongNamesForScreen(ArrayList<String> criteriaLongNamesForScreen) {
        this.criteriaLongNamesForScreen = criteriaLongNamesForScreen;
    }

    public ArrayList<String> getCriteriaShortNamesForScreen() {
        return criteriaShortNamesForScreen;
    }

    public void setCriteriaShortNamesForScreen(ArrayList<String> criteriaShortNamesForScreen) {
        this.criteriaShortNamesForScreen = criteriaShortNamesForScreen;
    }

    public ArrayList<String> getResultPercentileCriteria() {
        return resultPercentileCriteria;
    }

    public void setResultPercentileCriteria(ArrayList<String> resultPercentileCriteria) {
        this.resultPercentileCriteria = resultPercentileCriteria;
    }

    public ArrayList<String> getResultValueCriteria() {
        return resultValueCriteria;
    }

    public void setResultValueCriteria(ArrayList<String> resultValueCriteria) {
        this.resultValueCriteria = resultValueCriteria;
    }

    public boolean isClosedToNB() {
        return isClosedToNB;
    }

    public void setClosedToNB(boolean isClosedToNB) {
        this.isClosedToNB = isClosedToNB;
    }

    public boolean isIndex() {
        return isIndex;
    }

    public void setIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }
    
    public BigDecimal getRank() {
        return rank;
    }

    public void setRank(BigDecimal rank) {
        this.rank = rank;
    }

    public int getDisplayRank() {
        return displayRank;
    }

    public void setDisplayRank(int displayRank) {
        this.displayRank = displayRank;
    }

    public String getOverallDisplayRank() {
        return overallDisplayRank;
    }

    public void setOverallDisplayRank(String overallDisplayRank) {
        this.overallDisplayRank = overallDisplayRank;
    }

    public boolean isSVFCompetingFund() {
        return isSVFCompetingFund;
    }

    public void setSVFCompetingFund(boolean isSVFCompetingFund) {
        this.isSVFCompetingFund = isSVFCompetingFund;
    }

    /**
    /**
     * @return the resultPercentileConvertedToRankCriteria
     */
    public ArrayList<String> getResultPercentileConvertedToRankCriteria() {
        return resultPercentileConvertedToRankCriteria;
    }

    /**
     * @param resultPercentileConvertedToRankCriteria the resultPercentileConvertedToRankCriteria to set
     */
    public void setResultPercentileConvertedToRankCriteria(
            ArrayList<String> resultPercentileConvertedToRankCriteria) {
        this.resultPercentileConvertedToRankCriteria = resultPercentileConvertedToRankCriteria;
    }

    /**
     * @return the isSVFFund
     */
    public boolean isSVFFund() {
        return isSVFFund;
    }

    /**
     * @param isSVFFund the isSVFFund to set
     */
    public void setSVFFund(boolean isSVFFund) {
        this.isSVFFund = isSVFFund;
    }

    /**
     * @return the classShortName
     */
    public String getClassShortName() {
        return classShortName;
    }

    /**
     * @param classShortName the classShortName to set
     */
    public void setClassShortName(String classShortName) {
        this.classShortName = classShortName;
    }

	public boolean isPBACompetingFund() {
		return isPBACompetingFund;
	}

	public void setPBACompetingFund(boolean isPBACompetingFund) {
		this.isPBACompetingFund = isPBACompetingFund;
	}    
    
    
}
