package com.manulife.pension.bd.web.fundEvaluator.report;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.valueobject.BenchmarkMetrics;
import com.manulife.pension.service.fund.cache.BenchmarkMetricsTable;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;

/**
 * This class holds all fund data categorized by Asset class
 * @author PWakode
 */

public class AssetClassStyle {

	private String assetClassId;
	private int numberOfBasicFunds;
	private List<DecoratedFund> decoratedFundsSortedByFundSortNumber;  
	private List<DecoratedFund> decoratedFundsSortedByPercentileRank;
	private Collection<BenchmarkMetrics> allBenchmarkMetrics;
    
	
    public AssetClassStyle(String newAssetClassId, List<DecoratedFund> newDecoratedFunds, List<DecoratedFund> newPercentileRankedDecoratedFunds, Collection<BenchmarkMetrics> allBenchmarkMetrics) {
        super();
        init(newAssetClassId, newDecoratedFunds, newPercentileRankedDecoratedFunds, allBenchmarkMetrics);
    }
	
	public String getAssetClassStyleId() {
		String result = assetClassId;
		if (isHybrid()) {
			result = ReportDataModel.ASSET_CLASS_STYLE_ID_HYBRID;
		}
		return result;
	}
    
	public String getBenchmarkDescription() {
	
		String result = null;
		if (isHybrid()) {
			result = "";
		} else {
			try{//this fund service delegate remains only for styleReport Pdf - not used for XML generation
                result = FundServiceDelegate.getInstance().getAssetClass(assetClassId).getBenchmarkDescription();//AssetClassTable.getInstance().get(assetClassId).getBenchmarkDescription();
            }
            catch(SystemException e){
                e.printStackTrace();
            }
		}	
		return result;
	}

	@SuppressWarnings("static-access")
    public BenchmarkMetrics getBenchmarkMetrics(int percentile) {
		
        return ((BenchmarkMetricsTable)allBenchmarkMetrics).getInstance().get(assetClassId, percentile);
	}

	public List<DecoratedFund> getDecoratedFundsSortedByFundSortNumber() {
		return decoratedFundsSortedByFundSortNumber;
	}

	public List<DecoratedFund> getDecoratedFundsSortedByPercentileRank() {
		return decoratedFundsSortedByPercentileRank;
	}

	/** Return the number of funds that are mandatory, tool recommended and broker selected */
	public int getNumberOfBasicFunds() {
		return numberOfBasicFunds;
	}
	public int getNumberOfFunds() {
		int result = 0;
		if (decoratedFundsSortedByFundSortNumber != null) {
			result = decoratedFundsSortedByFundSortNumber.size();
		}	
		return result;
	}
	
	public List<DecoratedFund> getToolResultDecoratedFunds(){
	
		List<DecoratedFund> funds = null;
		if (decoratedFundsSortedByFundSortNumber != null) {
			funds = new Vector<DecoratedFund>();
			for (int i=0; i < decoratedFundsSortedByFundSortNumber.size(); i++) {
				DecoratedFund fancyFund = (DecoratedFund) decoratedFundsSortedByFundSortNumber.get(i);
				if (isBasicFund(fancyFund)) {
					funds.add(fancyFund);
				}	
			}	
		}	
		return funds;
	}
    
    private void init(String newAssetClassId, List<DecoratedFund> newDecoratedFunds, List<DecoratedFund> newPercentileRankedDecoratedFunds, Collection<BenchmarkMetrics> allBenchmarkMetrics) {
        assetClassId = newAssetClassId;
        decoratedFundsSortedByFundSortNumber = newDecoratedFunds;
        decoratedFundsSortedByPercentileRank = newPercentileRankedDecoratedFunds;
        
        if (decoratedFundsSortedByFundSortNumber != null) {
            for (int i=0; i < decoratedFundsSortedByFundSortNumber.size(); i++) {
                if (isBasicFund((DecoratedFund)decoratedFundsSortedByFundSortNumber.get(i))) {
                    numberOfBasicFunds++;
                }   
            }   
        }   
        
        String styleId = getAssetClassStyleId();
        if (decoratedFundsSortedByFundSortNumber != null ) {
            for (int i = 0; i < decoratedFundsSortedByFundSortNumber.size(); i++) {
                DecoratedFund fund = (DecoratedFund)decoratedFundsSortedByFundSortNumber.get(i);
                fund.setAssetClassStyleId(styleId);
            }
        }
        
        if (decoratedFundsSortedByPercentileRank != null) {
            for (int i = 0; i < decoratedFundsSortedByPercentileRank.size(); i++) {
                DecoratedFund fund = (DecoratedFund)decoratedFundsSortedByPercentileRank.get(i);
                fund.setAssetClassStyleId(styleId);
            }
        }   
    }
	
	public boolean isBasicFund(DecoratedFund fund){
		return (fund.isChecked() );
	}
	
	public boolean isBenchmarkAvailable(){
		boolean result = false;
		
		if (BenchmarkMetricsTable.getInstance().get(assetClassId, ReportDataModel.PERCENTILE_MEDIAN) != null) {
			result = true;
		}	
		return result;
	}

	private boolean isHybrid(){
		boolean result = false;
		if (assetClassId.length() > 3) {
			result = true;
		}			
	
		return result;
	}

	public void setDecoratedFundsSortedBySortNumber(List<DecoratedFund> newDecoratedFunds) {
		decoratedFundsSortedByFundSortNumber = newDecoratedFunds;
	}

	public String getAssetClassId() {
		return assetClassId;
	}

	public void setAssetClassId(String assetClassId) {
		this.assetClassId = assetClassId;
	}
     
}
