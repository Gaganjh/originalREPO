package com.manulife.pension.bd.web.fundEvaluator.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundFootnote;
import com.manulife.pension.service.fund.valueobject.FundMetrics;
import com.manulife.pension.service.fund.valueobject.PercentileRankedFund;

/**
 * This object is wrapper to Fund object
 * @author PWakode
 */

public class DecoratedFund {

	private Fund fund;
	private PercentileRankedFund percentileRankedFund;

	private String assetClassStyleId;
	private FundMetrics fundMetrics;
	private FundFootnote fundFootnote;

	private boolean brokerSelected;
    private boolean toolSelected;
    private boolean contractSelected;
    private boolean checked;
    private boolean closedToNB;
    private boolean index;
    
	private boolean toolRecommended;
    private String fundManagerName;

	public DecoratedFund(Fund newFund, PercentileRankedFund newPercentileRankedFund, Hashtable<String, FundMetrics> fundLineupFundMetricTable, Hashtable<String, FundFootnote> fundLineupFundFootnoteTable) throws SystemException {
        super();
        fund = newFund;
        percentileRankedFund = newPercentileRankedFund;

        init(fundLineupFundMetricTable, fundLineupFundFootnoteTable);
    }
	
	public Fund getFund() {
		return fund;
	}

	public PercentileRankedFund getPercentileRankedFund() {
		return percentileRankedFund;
	}
	
	public String getAssetClassStyleId() {
		return assetClassStyleId;
	}
	
	public void setAssetClassStyleId(String newAssetClassStyleId) {
		assetClassStyleId = newAssetClassStyleId;
	}

	public FundMetrics getFundMetrics() {
		return fundMetrics;
	}
	
	public void setFundMetrics(FundMetrics newFundMetrics) {
		fundMetrics = newFundMetrics;
	}
	
	public void setFundFootnote(FundFootnote newFundFootnote) {
		fundFootnote = newFundFootnote;
	}

	public boolean isBrokerSelected() {
		return brokerSelected;
	}
	
	public void setBrokerSelected(boolean newBrokerSelected) {
		brokerSelected = newBrokerSelected;
	}

	public boolean isToolRecommended() {
		return toolRecommended;
	}
	public void setToolRecommended(boolean newToolRecommended) {
		toolRecommended = newToolRecommended;
	}
	
	public String getDecoratedFundManagerName() {
		String managerName = (String) ReportDataModel.REGISTERED_NAME_TABLE.get(fund.getFundManagerName());
		if (managerName == null) {
			managerName = fund.getFundManagerName();
		}
		return managerName;
	}
	
    private void init(Hashtable<String, FundMetrics> fundLineupFundMetricTable, Hashtable<String, FundFootnote> fundLineupFundFootnoteTable) throws SystemException {
        
        fundFootnote = (FundFootnote)fundLineupFundFootnoteTable.get(fund.getFundId());//FundFootnoteTable.getInstance().get(fund.getFundId());
        
        if (percentileRankedFund != null) {
            // Double check that these objects are in synch!
            if (!fund.getFundId().equals(percentileRankedFund.getFundID())) {
                String message = "Error - Fund = [" + fund.getFundName() + "] with FundId = " + fund.getFundId() +
                        "] does NOT match PercentileRankedFund.getFundID = [" + percentileRankedFund.getFundID() + "]";
                throw new SystemException(message);
            }
            fundMetrics = (FundMetrics)fundLineupFundMetricTable.get(fund.getFundId()+percentileRankedFund.getRateType());
        }
    }
	
	public boolean isBasicFund() {
		return (isChecked() );
	}

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isContractSelected() {
        return contractSelected;
    }

    public void setContractSelected(boolean contractSelected) {
        this.contractSelected = contractSelected;
    }

    public boolean isToolSelected() {
        return toolSelected;
    }

    public void setToolSelected(boolean toolSelected) {
        this.toolSelected = toolSelected;
    }
    
    public String getFundName() {
        String name = fund.getFundName();        
        return name;
    }
    
    public ArrayList<String> getFundFootnoteSymbols() {
        String[] footnotes = null;
        if (fundFootnote != null) {
            footnotes = fundFootnote.getFootnoteIdsAsArray();
        }
        ArrayList<String> footnoteList = null;
        if (footnotes!=null && footnotes.length>0){
            footnoteList = new ArrayList<String>(Arrays.asList(footnotes));
        }
        return footnoteList;
    }

    public String getFundManagerName() {
        return fundManagerName;
    }

    public void setFundManagerName(String fundManagerName) {
        this.fundManagerName = fundManagerName;
    }

    public boolean isClosedToNB() {
        return closedToNB;
    }

    public void setClosedToNB(boolean closedToNB) {
        this.closedToNB = closedToNB;
    }

    public boolean isIndex() {
        return index;
    }

    public void setIndex(boolean index) {
        this.index = index;
    }
    
    public String toString() {
        return "FUNDID=" + fund.getFundId() + "|FUND_NAME=" + fund.getFundName(); 
    }
}
