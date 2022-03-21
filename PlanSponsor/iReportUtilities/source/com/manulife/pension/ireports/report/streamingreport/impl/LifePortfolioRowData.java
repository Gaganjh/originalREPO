package com.manulife.pension.ireports.report.streamingreport.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ireports.util.StandardReportsUtils;

public class LifePortfolioRowData implements Comparable {
	private String name;
	private String indexName;
	private Date inceptionDate;

    private BigDecimal ror1mth;
    private BigDecimal ror3mth;
    private BigDecimal rorytd;
    private BigDecimal ror1yr;
    private BigDecimal ror3yr;
    private BigDecimal ror5yr;
    private BigDecimal ror10yr;
	private BigDecimal rorSinceInception;
	
	//quarter-end returns values
	private BigDecimal ror1yrqend;
    private BigDecimal ror5yrqend;
    private BigDecimal ror10yrqend;
    private BigDecimal rorSinceInceptionqend;
    
    // Hypothetical values
    private boolean ror1mthHypothetical;
    private boolean ror3mthHypothetical;
    private boolean rorytdHypothetical;
    private boolean ror1yrHypothetical;
    private boolean ror3yrHypothetical;
    private boolean ror5yrHypothetical;
    private boolean ror10yrHypothetical;
    private boolean ror1yrqendHypothetical;
    private boolean ror5yrqendHypothetical;
    private boolean ror10yrqendHypothetical;
    private boolean rorSinceInceptionHypothetical;
    private boolean rorSinceInceptionqendHypothetical;

    private BigDecimal fundExpenseRatio;
    private BigDecimal maintenanceCharge;
    private String salesAndServiceFee;
	private BigDecimal annualInvestmentCharge;
	private boolean isChosenPortfolio; 
	private String[] fundFootnotes;

	private boolean feeWaiverFund;
	
	public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public String getRor1mth() {
        return StandardReportsUtils.formatPercentage(ror1mth); 
    }

    public BigDecimal getRor1mthInDecimal() {
        return ror1mth; 
    }

    public void setRor1mth(BigDecimal ror1mth) {
        this.ror1mth = ror1mth;
    }

    public String getRor3mth() {
        return StandardReportsUtils.formatPercentage(ror3mth); 
    }

    public void setRor3mth(BigDecimal ror3mth) {
        this.ror3mth = ror3mth;
    }

    public String getRorytd() {
        return StandardReportsUtils.formatPercentage(rorytd); 
    }

    public void setRorytd(BigDecimal rorytd) {
        this.rorytd = rorytd;
    }

    public String getRor1yr() {
        return StandardReportsUtils.formatPercentage(ror1yr); 
    }

    public void setRor1yr(BigDecimal ror1yr) {
        this.ror1yr = ror1yr;
    }

    public String getRor3yr() {
        return StandardReportsUtils.formatPercentage(ror3yr); 
    }

    public void setRor3yr(BigDecimal ror3yr) {
        this.ror3yr = ror3yr;
    }

    public String getRor5yr() {
        return StandardReportsUtils.formatPercentage(ror5yr); 
    }

    public void setRor5yr(BigDecimal ror5yr) {
        this.ror5yr = ror5yr;
    }

    public String getRor10yr() {
        return StandardReportsUtils.formatPercentage(ror10yr); 
    }

    public void setRor10yr(BigDecimal ror10yr) {
        this.ror10yr = ror10yr;
    }

    public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRorSinceInception() {
        return StandardReportsUtils.formatPercentage(rorSinceInception);
	}
	public void setRorSinceInception(BigDecimal rorSinceInception) {
		this.rorSinceInception = rorSinceInception;
	}
	public boolean isChosenPortfolio() {
		return isChosenPortfolio;
	}
	public void setChosenPortfolio(boolean isChosenPortfolio) {
		this.isChosenPortfolio = isChosenPortfolio;
	}

	public String[] getFundFootnotes() {
		return fundFootnotes;
	}
	public void setFundFootnotes(String[] fundFootnotes) {
		this.fundFootnotes = fundFootnotes;
	}

    public boolean isRor1mthHypothetical() {
        return ror1mthHypothetical;
    }

    public boolean isRor3mthHypothetical() {
        return ror3mthHypothetical;
    }

    public boolean isRorytdHypothetical() {
        return rorytdHypothetical;
    }

    public boolean isRor1yrHypothetical() {
        return ror1yrHypothetical;
    }

    public boolean isRor3yrHypothetical() {
        return ror3yrHypothetical;
    }

    public boolean isRor5yrHypothetical() {
        return ror5yrHypothetical;
    }

    public boolean isRor10yrHypothetical() {
        return ror10yrHypothetical;
    }

    public boolean isRorSinceInceptionHypothetical() {
          return rorSinceInceptionHypothetical;
    }

    public boolean isRor1yrqendHypothetical() {
        return ror1yrqendHypothetical;
    }

    public boolean isRor5yrqendHypothetical() {
        return ror5yrqendHypothetical;
    }

    public boolean isRor10yrqendHypothetical() {
        return ror10yrqendHypothetical;
    }

    public boolean getRorSinceInceptionqendHypothetical() {
       return rorSinceInceptionqendHypothetical;
    }
	
	public void setRor1mthHypothetical(boolean ror1mthHypothetical) {
        this.ror1mthHypothetical = ror1mthHypothetical;
    }

    public void setRor3mthHypothetical(boolean ror3mthHypothetical) {
        this.ror3mthHypothetical = ror3mthHypothetical;
    }

    public void setRorytdHypothetical(boolean rorytdHypothetical) {
        this.rorytdHypothetical = rorytdHypothetical;
    }

    public void setRor1yrHypothetical(boolean ror1yrHypothetical) {
        this.ror1yrHypothetical = ror1yrHypothetical;
    }

    public void setRor3yrHypothetical(boolean ror3yrHypothetical) {
        this.ror3yrHypothetical = ror3yrHypothetical;
    }

    public void setRor5yrHypothetical(boolean ror5yrHypothetical) {
        this.ror5yrHypothetical = ror5yrHypothetical;
    }

    public void setRor10yrHypothetical(boolean ror10yrHypothetical) {
        this.ror10yrHypothetical = ror10yrHypothetical;
    }

    public void setRor1yrqendHypothetical(boolean ror1yrqendHypothetical) {
        this.ror1yrqendHypothetical = ror1yrqendHypothetical;
    }

    public void setRor5yrqendHypothetical(boolean ror5yrqendHypothetical) {
        this.ror5yrqendHypothetical = ror5yrqendHypothetical;
    }

    public void setRor10yrqendHypothetical(boolean ror10yrqendHypothetical) {
        this.ror10yrqendHypothetical = ror10yrqendHypothetical;
    }

    public int compareTo(Object rhs) {
        LifePortfolioRowData data1 = this;
        LifePortfolioRowData data2 = (LifePortfolioRowData)rhs;
		BigDecimal ror1 = data1.getRor1mthInDecimal() == null ? new BigDecimal("-9999") : data1.getRor1mthInDecimal();
        BigDecimal ror2 = data2.getRor1mthInDecimal() == null ? new BigDecimal("-9999") : data2.getRor1mthInDecimal();
		return ror2.compareTo(ror1);
	}
	public BigDecimal getAnnualInvestmentCharge() {
		return annualInvestmentCharge;
	}
	public void setAnnualInvestmentCharge(BigDecimal annualInvestmentCharge) {
		this.annualInvestmentCharge = annualInvestmentCharge;
	}
    public String getRor1yrqend() {
        return StandardReportsUtils.formatPercentage(ror1yrqend); 
    }

    public void setRor1yrqend(BigDecimal ror1yrqend) {
        this.ror1yrqend = ror1yrqend;
    }

    public String getRor5yrqend() {
        return StandardReportsUtils.formatPercentage(ror5yrqend); 
    }

    public void setRor5yrqend(BigDecimal ror5yrqend) {
        this.ror5yrqend = ror5yrqend;
    }

    public String getRor10yrqend() {
        return StandardReportsUtils.formatPercentage(ror10yrqend); 
    }

    public void setRor10yrqend(BigDecimal ror10yrqend) {
        this.ror10yrqend = ror10yrqend;
    }

    public String getRorSinceInceptionqend() {
        return StandardReportsUtils.formatPercentage(rorSinceInceptionqend); 
    }

    public void setRorSinceInceptionqend(BigDecimal rorSinceInceptionqend) {
        this.rorSinceInceptionqend = rorSinceInceptionqend;
    }

    public BigDecimal getFundExpenseRatio() {
        return fundExpenseRatio;
    }

    public void setFundExpenseRatio(BigDecimal fundExpenseRatio) {
        this.fundExpenseRatio = fundExpenseRatio;
    }

    public BigDecimal getMaintenanceCharge() {
        return maintenanceCharge;
    }

    public void setMaintenanceCharge(BigDecimal maintenanceCharge) {
        this.maintenanceCharge = maintenanceCharge;
    }

    public String getSalesAndServiceFee() {
        return salesAndServiceFee;
    }

    public void setSalesAndServiceFee(String salesAndServiceFee) {
        this.salesAndServiceFee = salesAndServiceFee;
    }

	public void setRorSinceInceptionHypothetical(boolean rorSinceInceptionHypothetical) {
		this.rorSinceInceptionHypothetical = rorSinceInceptionHypothetical;
	}

	public void setRorSinceInceptionqendHypothetical(boolean rorSinceInceptionqendHypothetical) {
		this.rorSinceInceptionqendHypothetical = rorSinceInceptionqendHypothetical;
	}
	
	public boolean isFeeWaiverFund() {
		return feeWaiverFund;
	}

	public void setFeeWaiverFund(boolean feeWaiverFund) {
		this.feeWaiverFund = feeWaiverFund;
	}
	
}
