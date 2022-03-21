package com.manulife.pension.ireports.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.ireports.model.report.AssetClassReportData;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.LifecycleReportData;
import com.manulife.pension.ireports.model.report.LifestyleReportData;
import com.manulife.pension.ireports.model.report.MarketReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.util.FundHelper;
import com.manulife.pension.ireports.util.cache.RetryPolicy;
import com.manulife.pension.ireports.util.propertymanager.PropertyManager;
import com.manulife.pension.platform.utility.cache.FootnoteCacheImpl;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategory;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetCategoryGroup;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetClass;
import com.manulife.pension.service.fund.standardreports.valueobject.CurrentAsOfDate;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundClassVO;
import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;
import com.manulife.pension.service.fund.standardreports.valueobject.FundFootnoteSymbol;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.FundRatetypeKey;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;
import com.manulife.pension.service.fund.standardreports.valueobject.GARates;
import com.manulife.pension.service.fund.standardreports.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;
import com.manulife.pension.service.fund.valueobject.MarketIndexIbPerformance;

/**
 * This class gets the all the data (All Funds, All Fund Metrics, All Market Index, All Market
 * Performance, etc) by calling appropriate FundServiceDelegate methods. This data will be used to
 * generate the i:report PDF.
 * 
 * @author harlomte
 * 
 */
public class ReportDataRepositoryImpl implements ReportDataRepository, StandardReportsConstants {
    
	private static ReportDataRepositoryImpl instance = null;

	private static Map<String, Map<String, MutableFootnote>> footnotesByCompany = null;
	
    private static int EXPIRY_TIMEOUT_IN_MS = 86400000; // 24 hours

    private static final String DATA_REPOSITORY_TIMEOUT_PROPERTY_KEY = "datarepository.expiry.timeout.in.ms";

    private long expireTime;

    private RetryPolicy retryPolicy;

    static {
        loadProperties();
    }

    /**
     * This method sets the Expiry timeout.
     */
    private static void loadProperties() {
        EXPIRY_TIMEOUT_IN_MS = PropertyManager.getInt(DATA_REPOSITORY_TIMEOUT_PROPERTY_KEY,
                86400000);
    }

    /**
     * Get the RetryPolicy object.
     * 
     * @return
     */
    RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    /**
     * Get the expireTime.
     * 
     * @return
     */
    private long getExpireTime() {
        return expireTime;
    }

    /**
     * Set the expiry time.
     * 
     * @param expireTime
     */
    private void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * This method checks if the current cache is expired or not. This would mean, we need to reload
     * the cache.
     * 
     * @return - true, if the current cache is expired.
     */
    private boolean hasExpired() {
        return System.currentTimeMillis() > instance.getExpireTime();
    }

    /**
     * Get a instance of ReportDataRepository object.
     * 
     * @return
     */
	static ReportDataRepository getInstance() {
	    if (instance == null) {
            instance = new ReportDataRepositoryImpl();
        }
		return instance;
	}

    /**
     * Constructor - private as this class is a Singleton
     * 
     */
    private ReportDataRepositoryImpl() {
    }

    /**
     * This methods gets the Fund Report Data based on the options selected by the user in online
     * F&P page.
     */
	public FundReportData getFundReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols) {
		FundReportDataBean dataBean =null;
		FundReportData reportData = null;
		if(!options.isSelectedFromHistoricalReport()){
			dataBean = ReportDataRepositoryImplByMonthEndDate.getInstance().getFundReportDataBean(options.getPeriodendingDate(),options.gethistoricalIreportDAO());
			
			 reportData = ReportDataTransformer.getFundReportData(dataBean, options,
	                fundSortOrder,
	                staticFootnoteSymbols);
			 reportData.setCurrentAsOfDates(dataBean.currentAsOfDatesMap);
			 reportData.setFeeWaiverFundIds(getFeeWaiverFundIds(options.getPeriodendingDate()));
			
		}else
		{
			dataBean = getFundReportDataBean();
			
			reportData = ReportDataTransformer.getFundReportData(dataBean, options,
	                fundSortOrder,
	                staticFootnoteSymbols);
			
			reportData.setCurrentAsOfDates(getCurrentAsOfDates());
			reportData.setFeeWaiverFundIds(getFeeWaiverFundIds());
			reportData.setRestrictedFunds(getRestrictedFunds());
			reportData.setMerrillAdvisor(options.isMerrillAdvisor());
		}

        return reportData;
	}
	
	/**
	 * Populates Morningstar FootNotes
	 * 
	 * @param options
	 * @param reportData
	 * @param funds
	 */
	public void getMorningstarFootNotes(ReportOptions options,
			FundReportData reportData, List<List<ReportFund>> funds) {
		Map<String, String> morningstarFootnotesOnReport = ReportDataTransformer
				.buildMorningstarFootNotes(funds, reportData, options);
		reportData.setMorningstarFootNotes(morningstarFootnotesOnReport);
	}

    /**
     * This method retrieves the AssetClassReportData object containing the assetClasses.
     */
	public AssetClassReportData getAssetClassReportData() {
		return ReportDataTransformer.getAssetClassReportData(getAssetClasses());
	}

	/**
     * This method retrieves the MarketReportData object.
     */
	public MarketReportData getMarketReportData() {
        
		MarketReportData reportData = new MarketReportData();

		reportData.setAssetCategoryGroups(getAssetCategoryGroups());
        reportData.setAssetCategories(getAssetCategories());
		reportData.setMarketIndexIbPerformances(getAllMarketIndexIbPerformanceForMarketIreport());

		return reportData;
	}

    /**
     * Hari -- Can be reused..
     */
	public LifestyleReportData getLifestyleReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols) {
		FundReportDataBean dataBean = getFundReportDataBean();
		LifestyleReportData lifestyleReportData = ReportDataTransformer
				.getLifestyleReportData(dataBean, options, fundSortOrder,
						staticFootnoteSymbols);
		lifestyleReportData.setFeeWaiverFundIds(getFeeWaiverFundIds());
		return lifestyleReportData;
	}

    /**
     * This method populates the FundReportDataBean object with the data fetched from
     * FundServiceDelegate.
     */
	public FundReportDataBean getFundReportDataBean() {
		FundReportDataBean dataBean = new FundReportDataBean();

		dataBean.currentAsOfDates = getCurrentAsOfDates();
		dataBean.allFunds = getAllFunds();
        dataBean.fundMetricsByDate = getAllFundMetrics();
        dataBean.fundFootnoteSymbols = getAllFundFootnoteSymbols();
        dataBean.marketIndexes = getAllMarketIndexFunds();
		dataBean.marketIndexPerformancesByDate = getAllMarketIndexPerformances();
        dataBean.marketIndexIbPerformancesByDate = getAllMarketIndexIbPerformances();
		dataBean.morningstarCategoryPerformancesByDate = getAllMorningstarCategoryPerformances();
        dataBean.fundStandardDeviationsByDate = getAllFundStandardDeviations();
        dataBean.fundExpensesByDate = getAllFundExpenses();
		dataBean.guaranteedAccounts = getAllGuaranteedFunds();
        dataBean.guaranteedAccountRates = getAllGuaranteedAccountRates();
		dataBean.investmentGroups = getInvestmentGroups();
        dataBean.assetClasses = getAssetClasses();
        dataBean.assetCategoryGroups = getAssetCategoryGroups();
        dataBean.assetCategories = getAssetCategories();
        dataBean.footnotesByCompany = FootnoteCacheImpl.getInstance().getFootnotes();
		dataBean.footnotesByMarketIndex = getMarketIndexFootnote();
		return dataBean;
	}

    /**
     * Returns fund classes.
     * 
     * @return Map
     */	
	public Map<String, FundClassVO> getFundClasses() {
		return Collections.unmodifiableMap(Collections.synchronizedMap(getAllFundClasses()));
	}

	/**
     * Retrieve Funds
     */
	public Map<String, Fund> getFunds() {
		return Collections.unmodifiableMap(Collections.synchronizedMap(getAllFunds()));
	}

    /**
     * Retrieve Closed to New Business Funds
     */
	public Map getClosedToNewBusinessFunds() {
		return Collections.unmodifiableMap(Collections.synchronizedMap(getAllClosedToNewBusinessFunds()));
	}

    /**
     * Retrieve Funds based on FundOffering provided.
     */
	public Map getFunds(FundOffering fundOffering, boolean onlyOpenFunds) {
		Collection result = getFunds(fundOffering);
		if (onlyOpenFunds) {
			final Date now = new Date();
			result = CollectionUtils.select(result, new Predicate() {
				public boolean evaluate(Object fund) {
					return ((Fund) fund).getStopDate().after(now);
				}
			});
		}
		return FundHelper.makeFundMap(result);
	}

    /**
     * Retrieve Current as of dates for each context. The returned Map contains the context name as
     * the key and the as of date specific to that context as value.
     */
	public Map<String, CurrentAsOfDate> getCurrentAsOfDates() {
        try {
            return FundServiceDelegate.getInstance().getAllCurrentAsOfDatesMapForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getCurrentAsOfDates in "
                    + getClass().getName(), e);
        }
    }
	   /**
     * Retrieve the list of feeWaiverFund ids
     */
	public List<String> getFeeWaiverFundIds() {
        try {
            return FundServiceDelegate.getInstance().getFundFeeWaiverIndicator();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getFeeWaiverFundIds in "
                    + getClass().getName(), e);
        }
    }
	/**
     * Retrieve the list of RestrictedFunds
     */
	public Map<String, com.manulife.pension.service.fund.valueobject.Fund> getRestrictedFunds() {
        try {
            return FundServiceDelegate.getInstance().getRestrictedFunds();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getRestrictedFunds in "
                    + getClass().getName(), e);
        }
    }
	
	/**
     * Retrieve the list of feeWaiverFund ids
     */
	public List<String> getFeeWaiverFundIds(Date periodendingDate) {
        try {
            return FundServiceDelegate.getInstance().getFundFeeWaiverIndicator(periodendingDate);
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getFeeWaiverFundIds in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Funds FundClasses.
     * 
     */
	private Map<String, FundClassVO> getAllFundClasses() {
        try {
            return FundServiceDelegate.getInstance().getAllFundClassesForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAllFundClasses in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Returns classId (keyed) and class Name map object.
     * 
     * @return Map
     */	
	public Map<String, String> getFundClassMenu() {
        Map<String, String> classMenu = new LinkedMap();
		
		Map<String, FundClassVO> fundClasses = getFundClasses();

		for (String fundKey : fundClasses.keySet()) {
            classMenu.put(fundKey, (fundClasses.get(fundKey)).getClassName());
		}
		return classMenu;
	}

    /**
     * Retrieve Funds
     */
	private Map<String, Fund> getAllFunds() {
        try {
            return FundServiceDelegate.getInstance().getAllFundsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAllFunds in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Guaranteed Account Funds
     */
	private Map<String, Fund> getAllGuaranteedFunds() {
        try {
            return FundServiceDelegate.getInstance().getAllGuaranteedFundsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllGuaranteedFunds in " + getClass().getName(),
                    e);
        }
    }

    /**
     * Retrieve Market Index Funds
     */
    private List<Fund> getAllMarketIndexFunds() {
        try {
            return FundServiceDelegate.getInstance().getAllMarketIndexFundsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllMarketIndexFunds in " + getClass().getName(),
                    e);
        }
    }

    /**
     * Retrieve Closed to New Business Funds
     */
    private Map<String, Fund> getAllClosedToNewBusinessFunds() {
        try {
            return FundServiceDelegate.getInstance().getAllClosedToNewBusinessFundsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllClosedToNewBusinessFunds in "
                            + getClass().getName(), e);
        }
    }
	
    /**
     * Retrieve All Guaranteed Account Rates.
     * 
     * @return
     */
    private Map<String, Map<String, GARates>> getAllGuaranteedAccountRates() {
        try {
            return FundServiceDelegate.getInstance().getAllGuaranteedAccountRates();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllGuaranteedAccountRates in "
                            + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Fund Metrics
     */
	private Map<FundRatetypeKey, FundMetrics> getAllFundMetrics() {
        try {
            return FundServiceDelegate.getInstance().getAllFundMetricsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAllFundMetrics in "
                    + getClass().getName(), e);
        }
	}

    /**
     * Retrieve Fund Footnote symbols
     */
	private Map<String, List<FundFootnoteSymbol>> getAllFundFootnoteSymbols() {
        try {
            return FundServiceDelegate.getInstance().getAllFundFootnoteSymbolForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllFundFootnoteSymbols in "
                            + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Market Index Performances
     * 
     * @return
     */
	private Map<String, FundMetrics> getAllMarketIndexPerformances() {
        try {
            return FundServiceDelegate.getInstance().getAllMarketIndexPerformanceForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllMarketIndexPerformances in "
                            + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Market IndexIB Performances
     * 
     * @return
     */
    private Map<String, MarketIndexIbPerformance> getAllMarketIndexIbPerformances() {
        try {
            return FundServiceDelegate.getInstance().getAllMarketIndexIbPerformanceForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllMarketIndexIbPerformances in "
                            + getClass().getName(), e);
        }
    }

	/**
	 * Retrieve Market IndexIB Performances for Market Ireport.
	 * 
	 * @return
	 */
	private Map<String, MarketIndexIbPerformance> getAllMarketIndexIbPerformanceForMarketIreport() {
		try {
			return FundServiceDelegate.getInstance()
					.getAllMarketIndexIbPerformanceForMarketIreport();
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getAllMarketIndexIbPerformanceForMarketIreport in "
							+ getClass().getName(), e);
		}
	}

	/**
	 * Retrieve Morningstar category Performances
	 */
    private Map<String, MorningstarCategoryPerformance> getAllMorningstarCategoryPerformances() {
        try {
            return FundServiceDelegate.getInstance()
                    .getAllMorninsgstarCategoryPerformanceForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllMorningstarCategoryPerformances in "
                            + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Fund Standard Deviations
     */
	private Map<FundRatetypeKey, FundStandardDeviation> getAllFundStandardDeviations() {
        try {
            return FundServiceDelegate.getInstance().getAllFundStandardDeviationsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAllFundStandardDeviations in "
                            + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Fund Expenses
     */
	private Map<FundRatetypeKey, FundExpenses> getAllFundExpenses() {
        try {
            return FundServiceDelegate.getInstance().getAllFundExpensesForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAllFundExpenses in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Investment Groups
     * 
     * @return
     */
    private Map<Integer, InvestmentGroup> getInvestmentGroups() {
        try {
            return FundServiceDelegate.getInstance().getAllInvestmentGroupsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getInvestmentGroups in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Asset Classes
     * 
     * @return
     */
	private Map<String, AssetClass> getAssetClasses() {
        try {
            return FundServiceDelegate.getInstance().getAllAssetClassForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAssetClasses in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Retrieve Asset Category Groups
     * 
     * @return
     */
    private Map<String, AssetCategoryGroup> getAssetCategoryGroups() {
        try {
            return FundServiceDelegate.getInstance().getAllAssetCategoryGroupsForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException(
                    "Problem retrieving data in getAssetCategoryGroups in " + getClass().getName(),
                    e);
        }
    }

    /**
     * Retrieve Asset Categories
     * 
     * @return
     */
    private Map<String, AssetCategory> getAssetCategories() {
        try {
            return FundServiceDelegate.getInstance().getAllAssetCategoryForIreports();
        } catch (SystemException e) {
            throw new NestableRuntimeException("Problem retrieving data in getAssetCategories in "
                    + getClass().getName(), e);
        }
    }

    /**
     * Current as of date comparator.
     * 
     */
	static class CurrentAsOfDateComparator implements Comparator {
		public int compare(Object p1, Object p2) {
			CurrentAsOfDate currentAsOfDate1 = (CurrentAsOfDate) p1;
			CurrentAsOfDate currentAsOfDate2 = (CurrentAsOfDate) p2;

			Date compare1 = currentAsOfDate1.getAsofdate();
			Date compare2 = currentAsOfDate2.getAsofdate();

			return compare1.compareTo(compare2);
		}
	}

    /**
     * This method retrieves the LifecycleReportData object.
     */
	public LifecycleReportData getLifecycleReportData(ReportOptions options, String fundSortOrder,
            String[] staticFootnoteSymbols) {
		LifecycleReportData lifecycleReportData = ReportDataTransformer.getLifecycleReportData(getFundReportDataBean(), options,
                fundSortOrder, staticFootnoteSymbols);
		lifecycleReportData.setFeeWaiverFundIds(getFeeWaiverFundIds());
		return lifecycleReportData;
	}

    /**
     * This method is copied over from FundReportDataBean. This was copied to improve the
     * performance. Generally, the way this method was called is to first create a
     * FundReportDatabean() object and then call this method over it. Creating FundReportDataBean
     * object is a time / memory consuming process and hence this method was copied to avoid it.
     * 
     * @param fundOffering
     * @param lifeportfolio
     * @return Collection &ltFund&gt;
     */
    public Collection getAllFunds(FundOffering fundOffering, final String lifeportfolio) {
        Collection allCompanyFunds = getFunds(fundOffering);
        Collection lifecycleFunds = CollectionUtils.select(allCompanyFunds, new Predicate() {
            public boolean evaluate(Object obj) {
                return lifeportfolio.equals(((Fund) obj).getAssetcls());
            }
        });
        return lifecycleFunds;
    }

    /**
     * This method is copied over from FundReportDataBean. This was copied to improve the
     * performance. Generally, the way this method was called is to first create a
     * FundReportDatabean() object and then call this method over it. Creating FundReportDataBean
     * object is a time / memory consuming process and hence this method was copied to avoid it.
     * 
     * @param fundOffering
     * @return
     */
    public Collection getFunds(final FundOffering fundOffering) {
        return CollectionUtils.select(getAllFunds().values(), new Predicate() {
            public boolean evaluate(Object fund) {
                return fund instanceof Fund && fundOffering.isIncluded((Fund) fund);
            }
        });
    }

    /**
     * This method retrieves the Funds based on Fundoffering, assetClass selected.
     * 
     * @return Map &lt;investmentid, Fund&gt;
     */
    public Map getFunds(FundOffering fundOffering, String assetcls) {
        Collection funds = getAllFunds(fundOffering, assetcls);
        return FundHelper.makeFundMap(funds);
    }

    /**
     * This method retrieves the Funds based on Investment id
     * 
     */
	public String getFundLongName(String investmentId) {
		FundReportDataBean dataBean = getFundReportDataBean();
		Fund fund =  dataBean.getFund(investmentId);
		return fund.getFundLongName();
	}
	/**
	 * Method to get the marketIndexFootnotes bases on month end date.
	 * @return MarketIndexFootnote MAP
	 */
	 
	private Map<String, String> getMarketIndexFootnote() {

		Date date = getAsOfDateForWebPage();
		try {
			return FundServiceDelegate.getInstance().getAllMarketIndexFootnote(
					date);
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getCurrentAsOfDates in "
							+ getClass().getName(), e);
		}

	}
	/**
	 * Method to fetch the current AsOfDate based CYCLE_ID and BUSINESS_UNIT. 
	 * 
	 * @return Current ASOfDate
	 */
	private Date getAsOfDateForWebPage() {

		try {
			return FundServiceDelegate.getInstance().getAsOfDateForWebPage();
		} catch (SystemException e) {
			throw new NestableRuntimeException(
					"Problem retrieving data in getCurrentAsOfDates in "
							+ getClass().getName(), e);
		}
	}
}
