package com.manulife.pension.ireports.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.ireports.model.report.AssetClassReportData;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.LifecycleReportData;
import com.manulife.pension.ireports.model.report.LifestyleReportData;
import com.manulife.pension.ireports.model.report.MarketReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;

public interface ReportDataRepository {

	public static final String REPORTFUND_SORT_ORDER_ALPHABETICAL = "alpha";
	public static final String REPORTFUND_SORT_ORDER_RISKRETURN = "riskreturn";
	public static final String REPORTFUND_SORT_ORDER_ONEMONTHRETURN = "rf_onemonthreturn";
	public static final String REPORTFUND_SORT_ORDER_ASSETCATEGORY = "assetcategory";
	public static final String REPORTFUND_SORT_ORDER_ASSETCLASS = "assetclass";

    /**
     * Returns data to be used to create a fund list based report.
     * 
     * @param options
     * @param staticFootnoteSymbols
     * @return
     */
	public FundReportData getFundReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols);
	
	public AssetClassReportData getAssetClassReportData();

	public MarketReportData getMarketReportData();

    /**
     * Returns data to be used in the lifestyle report.
     * 
     * @param options
     * @param asOfDate
     * @param fundSortOrder
     * @param staticFootnoteSymbols
     * @return
     */
	public LifestyleReportData getLifestyleReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols);
	
	public LifecycleReportData getLifecycleReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols);

	public Map getFunds(FundOffering fundOffering, boolean onlyOpenFunds);

	public Map getFunds();

	public Map getClosedToNewBusinessFunds();

    /**
     * @return Map &lt;String investmentid, Fund&gt;
     */
	public Map getFunds(FundOffering fundOffering, String assetClass);

	public FundReportDataBean getFundReportDataBean();

    /**
     * Returns fund classId and class Name map object
     * 
     * @return Map
     */
	public Map getFundClasses();

    /**
     * Returns classId as keyed and class name as valued map object
     * 
     * @return
     */
	public Map<String, String> getFundClassMenu();
	
	/**
	 * Returns the as of dates
	 * @return Map<String, CurrentAsOfDate>
	 */
   public Map<String, CurrentAsOfDate> getCurrentAsOfDates() ;
   
   /**
    * @param String investmentId
    * @return String fundLongName
    */
   public String getFundLongName(String investmentId); 
   
	
	/**
	 * Populates Morningstar FootNotes
	 * 
	 * @param options
	 * @param reportData
	 * @param funds
	 */
	public void getMorningstarFootNotes(ReportOptions options,
			FundReportData reportData, List<List<ReportFund>> funds);
}