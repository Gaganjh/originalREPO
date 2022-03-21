package com.manulife.pension.ireports.dao;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.content.bizdelegates.ContentBrowseServiceDelegate;
import com.manulife.pension.content.service.ContentSource;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.service.fund.dao.HistoricalIreportDAO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategory;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategoryGroup;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetClass;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;
import com.manulife.pension.service.fund.standardreports.valueobject.FundFootnoteSymbol;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.FundRatetypeKey;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;
import com.manulife.pension.service.fund.standardreports.valueobject.GARates;
import com.manulife.pension.service.fund.standardreports.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.pension.service.fund.valueobject.MarketIndexIbPerformance;

public class ReportDataRepositoryImplByMonthEndDate implements  StandardReportsConstants { 
	
	private Logger logger = Logger.getLogger(ReportDataRepositoryImplByMonthEndDate.class);
	private static ReportDataRepositoryImplByMonthEndDate instance = null;
	
	public ReportDataRepositoryImplByMonthEndDate() {
	}
	
	static ReportDataRepositoryImplByMonthEndDate getInstance() {
		if (instance == null) {
			instance = new ReportDataRepositoryImplByMonthEndDate();
		}
		return instance;
	}

	public FundReportDataBean getFundReportDataBean(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {

		FundReportDataBean dataBean = new FundReportDataBean();

		dataBean.currentAsOfDatesMap = getCurrentAsOfDates(periodendingDate,historicalIreportDAO);
		dataBean.currentAsOfDates = dataBean.currentAsOfDatesMap;
		dataBean.allFunds = getAllFunds(periodendingDate,historicalIreportDAO); 
		dataBean.fundMetricsByDate = getAllFundMetrics(periodendingDate,historicalIreportDAO);
		dataBean.fundFootnoteSymbols = getAllFundFootnoteSymbols(periodendingDate,historicalIreportDAO);
		dataBean.marketIndexes = getAllMarketIndexFunds(periodendingDate,historicalIreportDAO);
		dataBean.marketIndexPerformancesByDate = getAllMarketIndexPerformances(periodendingDate,historicalIreportDAO);
		dataBean.marketIndexIbPerformancesByDate = getAllMarketIndexIbPerformances(periodendingDate,historicalIreportDAO);
		dataBean.morningstarCategoryPerformancesByDate = getAllMorningstarCategoryPerformances(periodendingDate,historicalIreportDAO); 
		dataBean.fundStandardDeviationsByDate = getAllFundStandardDeviations(periodendingDate,historicalIreportDAO);
		dataBean.fundExpensesByDate = getAllFundExpenses(periodendingDate,historicalIreportDAO);
		dataBean.guaranteedAccounts = getAllGuaranteedFunds(periodendingDate,historicalIreportDAO);
		dataBean.guaranteedAccountRates = getAllGuaranteedAccountRates(periodendingDate,historicalIreportDAO);
		dataBean.investmentGroups = getInvestmentGroups(periodendingDate,historicalIreportDAO);
		dataBean.assetClasses = getAssetClasses(periodendingDate,historicalIreportDAO);
		dataBean.assetCategoryGroups = getAssetCategoryGroups(periodendingDate,historicalIreportDAO);
		dataBean.assetCategories = getAssetCategories(periodendingDate,historicalIreportDAO);
		Timestamp timestamp = new Timestamp(getEndOfDay(periodendingDate).getTime());
		dataBean.footnotesByCompany = getFootnotes(timestamp);
		dataBean.footnotesByMarketIndex = getMarketIndexFootNote(periodendingDate);

		return dataBean;
	}
	/**
	 * Method to get the MarketIndexFootnotes bases on Date.
	 * 
	 * @param periodendingDate
	 * @return Map
	 */

	private Map<String, String> getMarketIndexFootNote(Date periodendingDate){
		
			try {
				return FundServiceDelegate.getInstance().getAllMarketIndexFootnote(periodendingDate);
			} catch (SystemException e) {
				throw new NestableRuntimeException("Problem retrieving data in getCurrentAsOfDates in "
						+ getClass().getName(), e);
			}
			
	}

	/**
	 * Retrieve Current as of dates for each context by periodendingDate. The returned Map contains the context name as
	 * the key and the as of date specific to that context as value.
	 * @throws DAOException 
	 */
	public Map<String, CurrentAsOfDate> getCurrentAsOfDates(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO)  {
		try {
			return FundServiceDelegate.getInstance().getAllCurrentAsOfDatesMapForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getCurrentAsOfDates in "
					+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Funds by MonthendDate
	 * @param date 
	 */
	private Map<String, Fund> getAllFunds(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllFundsForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getAllFunds in "
					+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Fund Metrics by MonthendDate
	 */
	private Map<FundRatetypeKey, FundMetrics> getAllFundMetrics(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllFundMetricsForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getAllFundMetrics in "
					+ getClass().getName(), e);
		}
	}
	/**
	 * Retrieve Fund Footnote symbols by MonthendDate
	 */
	private Map<String, List<FundFootnoteSymbol>> getAllFundFootnoteSymbols(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllFundFootnoteSymbolForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllFundFootnoteSymbols in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Market Index Funds By MonthendDate.
	 */
	private List<Fund> getAllMarketIndexFunds(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllMarketIndexFundsForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllMarketIndexFunds in " + getClass().getName(),
					e);
		}
	}
	/**
	 * Retrieve Market IndexIB Performances By MonthendDate
	 * 
	 * @return
	 */
	private Map<String, MarketIndexIbPerformance> getAllMarketIndexIbPerformances(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllMarketIndexIbPerformanceForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllMarketIndexIbPerformances in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Market Index Performances By MonthendDate
	 * 
	 * @return
	 */
	private Map<String, FundMetrics> getAllMarketIndexPerformances(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllMarketIndexPerformanceForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllMarketIndexPerformances in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Morningstar category Performances By MonthendDate.
	 */
	private Map<String, MorningstarCategoryPerformance> getAllMorningstarCategoryPerformances(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllMorninsgstarCategoryPerformanceForIreports(periodendingDate, historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllMorningstarCategoryPerformances in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Fund Standard Deviations By MonthendDate.
	 */
	private Map<FundRatetypeKey, FundStandardDeviation> getAllFundStandardDeviations(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllFundStandardDeviationsForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllFundStandardDeviations in "
							+ getClass().getName(), e);
		}
	}
	/**
	 * Retrieve Fund Expenses By MonthendDate
	 */
	private Map<FundRatetypeKey, FundExpenses> getAllFundExpenses(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllFundExpensesForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getAllFundExpenses in "
					+ getClass().getName(), e);
		}
	}
	/**
	 * Retrieve Guaranteed Account Funds BY MonthendDate
	 */
	private Map<String, Fund> getAllGuaranteedFunds(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllGuaranteedFundsForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllGuaranteedFunds in " + getClass().getName(),
					e);
		}
	}

	/**
	 * Retrieve All Guaranteed Account Rates By MonthendDate.
	 * 
	 * @return
	 */
	private Map<String, Map<String, GARates>> getAllGuaranteedAccountRates(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllGuaranteedAccountRates(periodendingDate, historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllGuaranteedAccountRates in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Investment Groups BY MonthendDate
	 * 
	 * @return
	 */
	private Map<Integer, InvestmentGroup> getInvestmentGroups(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllInvestmentGroupsForIreports(periodendingDate, historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getInvestmentGroups in "
					+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Asset Classes By MonthendDate
	 * 
	 * @return
	 */
	private Map<String, AssetClass> getAssetClasses(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
		try {
			return FundServiceDelegate.getInstance().getAllAssetClassForIreports(periodendingDate,historicalIreportDAO);
		} catch (SystemException e) {
			throw new NestableRuntimeException("Problem retrieving data in getAssetClasses in "
					+ getClass().getName(), e);
		}
	}
	 /**
     * Retrieve Asset Category Groups By MonthendDate
     * 
     * @return
     */
    private Map<String, AssetCategoryGroup> getAssetCategoryGroups(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
        try {
            return FundServiceDelegate.getInstance().getAllAssetCategoryGroupsForIreports(periodendingDate,historicalIreportDAO);
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAssetCategoryGroups in " + getClass().getName(),
                    e);
        }
    }
    /**
     * Retrieve Asset Categories By MonthendDate
     * 
     * @return
     */
    private Map<String, AssetCategory> getAssetCategories(Date periodendingDate,HistoricalIreportDAO historicalIreportDAO) {
        try {
            return FundServiceDelegate.getInstance().getAllAssetCategoryForIreports(periodendingDate,historicalIreportDAO);
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAssetCategories in "
                    + getClass().getName(), e);
        }
    }
	 
	 public Map<String, Map<String, MutableFootnote>> getFootnotes(Timestamp periodendingDate) {
	        
		 Map<String, Map<String, MutableFootnote>> footnotesByCompany = null;
	       
	            try {
	            	footnotesByCompany = reloadFootnotes(periodendingDate);
	            } catch (RuntimeException e) {
	                if (footnotesByCompany == null) { // first time in
	                    throw e;
	                } else { // has expired continue to use existing one.
	                    System.err
	                            .println("WARNING: Time cached has expired but not able to get new data.");
	                    e.printStackTrace(System.err);
	                    
	                }
	            }
	       
	        return footnotesByCompany;
	    }
	
	 private Map<String, Map<String, MutableFootnote>> reloadFootnotes(Timestamp periodendingDate) {
		 
		 //Map<String, MutableFootnote>  marketIndexFootnotes = new HashMap<String, MutableFootnote>();
		 Map<String, MutableFootnote> footnotesUSAMap = new LinkedMap();
		 MutableFootnote[] footnotesUSA = retrieveFootnotes(Location.USA,periodendingDate);
		 if (footnotesUSA != null) {
			 for (MutableFootnote footnoteUSA : footnotesUSA) {
				 footnotesUSAMap.put(footnoteUSA.getSymbol().trim(), footnoteUSA);
			 }
		 }
		 Map<String, Map<String, MutableFootnote>>  footnotesByCompany = new HashMap<String, Map<String, MutableFootnote>>();
	        
			
			footnotesByCompany.put(StandardReportsConstants.COMPANY_ID_USA, footnotesUSAMap);

     Map<String, MutableFootnote> footnotesNYMap = new LinkedMap();
			MutableFootnote[] footnotesNY = retrieveFootnotes(Location.NEW_YORK, periodendingDate);
			if (footnotesNY != null) {
			    for (MutableFootnote footnoteNY : footnotesNY) {
			        footnotesNYMap.put(footnoteNY.getSymbol().trim(), footnoteNY);
			    }
			}
			footnotesByCompany.put(StandardReportsConstants.COMPANY_ID_NY, footnotesNYMap);
			return footnotesByCompany;
	    }

	
	private MutableFootnote[] retrieveFootnotes(Location location,Timestamp periodendingDate) {
		
		 MutableFootnote[] footnotes = null;
			try {
				footnotes = (MutableFootnote[]) ContentBrowseServiceDelegate.getInstance().findContent(
					   ContentTypeManager.instance().FOOTNOTE.getName(),location, ContentSource.PSW, true,periodendingDate);
			}
			catch (SystemException e) {
				System.err
				.println("Error occuerd: while retieving footnotes source.com.manulife.pension.ireports.dao.ReportDataRepositoryImplByMonthEndDate.retrieveFootnotes(Location, Timestamp)");
				e.printStackTrace(System.err);
            }
	        return footnotes == null ? new MutableFootnote[0] : footnotes;
	}
	
	public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
