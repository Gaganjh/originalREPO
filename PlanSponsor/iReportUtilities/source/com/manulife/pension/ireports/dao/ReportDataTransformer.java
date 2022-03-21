package com.manulife.pension.ireports.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.model.report.AssetClassReportData;
import com.manulife.pension.ireports.model.report.FundReportData;
import com.manulife.pension.ireports.model.report.LifecycleReportData;
import com.manulife.pension.ireports.model.report.LifestyleReportData;
import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.ReportOptions;
import com.manulife.pension.ireports.util.FootnotesComparator;
import com.manulife.pension.ireports.util.InvestmentidTransformer;
import com.manulife.pension.platform.utility.CommonConstants;
import com.manulife.pension.service.fund.dao.FundDAO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.standardreports.valueobject.AssetClass;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;
import com.manulife.pension.service.fund.standardreports.valueobject.FundFootnoteSymbol;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.FundRatetypeKey;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;
import com.manulife.pension.service.fund.standardreports.valueobject.MorningstarCategoryPerformance;


public class ReportDataTransformer implements StandardReportsConstants {
	private static final FootnoteSymbolComparator FOOTNOTE_SYMBOL_COMPARATOR = new FootnoteSymbolComparator();

	private static final AlphabeticalReportFundComparator REPORTFUNDCOMPARATOR_ALPHABETICAL = new AlphabeticalReportFundComparator(); 
	private static final RiskReturnReportFundComparator REPORTFUNDCOMPARATOR_RISKRETURN = new RiskReturnReportFundComparator();
	private static final AssetCategoryReportFundComparator REPORTFUNDCOMPARATOR_ASSETCATEGORY = new AssetCategoryReportFundComparator();
	private static final AssetClassReportFundComparator REPORTFUNDCOMPARATOR_ASSETCLASS = new AssetClassReportFundComparator();
	private static final OneMonthReturnReportFundComparator REPORTFUNDCOMPARATOR_ONEMONTHRETURN = new OneMonthReturnReportFundComparator();

	private static Map<String, Comparator<Object>> reportFundComparatorMap;

	private static final Logger log = Logger.getLogger(ReportDataTransformer.class);
	
	static {
		reportFundComparatorMap = new HashMap<String, Comparator<Object>>();
		reportFundComparatorMap.put(ReportDataRepository.REPORTFUND_SORT_ORDER_ALPHABETICAL, REPORTFUNDCOMPARATOR_ALPHABETICAL);
		reportFundComparatorMap.put(ReportDataRepository.REPORTFUND_SORT_ORDER_RISKRETURN, REPORTFUNDCOMPARATOR_RISKRETURN);
		reportFundComparatorMap.put(ReportDataRepository.REPORTFUND_SORT_ORDER_ASSETCATEGORY, REPORTFUNDCOMPARATOR_ASSETCATEGORY);
		reportFundComparatorMap.put(ReportDataRepository.REPORTFUND_SORT_ORDER_ASSETCLASS, REPORTFUNDCOMPARATOR_ASSETCLASS);
		reportFundComparatorMap.put(ReportDataRepository.REPORTFUND_SORT_ORDER_ONEMONTHRETURN, REPORTFUNDCOMPARATOR_ONEMONTHRETURN);
	}
	
	public static Comparator<Object> getFundComparator(String comparatorName) {
		return reportFundComparatorMap.get(comparatorName);
	}
	
	/**
	 * Returns data to be used to create a fund list based report. 
	 * 
	 * This class filters and transforms the universe of data kept in the repository 
	 * to make it more easy to use for a specific report. 
	 * 
	 * @param companyId (019, 094)
	 * @param fundSeriesCode (1,2,3,4,5,6)
	 * 
	 * @param staticFootnoteSymbols 
	 * @return
	 */
	static FundReportData getFundReportData(
							FundReportDataBean repositoryDataBean,
							ReportOptions options,
							String fundSortOrder,
							String[] staticFootnoteSymbols) {

		FundReportData reportData = new FundReportData();

		List reportFunds = buildReportFunds(repositoryDataBean, options);
		
		List reportMarketIndexes = buildMarketIndexes(repositoryDataBean);
		reportFunds.addAll(reportMarketIndexes);
		
		Collections.sort(reportFunds, (Comparator) reportFundComparatorMap.get(fundSortOrder));
		reportData.setFunds(reportFunds);

		if (isGuaranteedAccountFundsChosen(repositoryDataBean, options)) {
			if (options.getContractShortlistOptions() != null
					&& options.getContractShortlistOptions().isContract()) {
				// Contract Ireport
				String contractId = options.getContractShortlistOptions().getContractNumber();
				// changes done as part of ACR REWRITE (Historical iReport changes)
				if(!options.isSelectedFromHistoricalReport()){
				try {
					reportData.setGuaranteedAccountRates(FundServiceDelegate.getInstance()
							.getGuaranteedAccountRatesForContractForIreport(contractId,options.getPeriodendingDate()));
				} catch (SystemException e) {
					throw new NestableRuntimeException("Problem retrieving guaranteed account rates for contract: "+contractId
		                    + ReportDataTransformer.class.getName(), e);
				}
				}else{
					try {
						reportData.setGuaranteedAccountRates(FundServiceDelegate.getInstance()
								.getGuaranteedAccountRatesForContractForIreport(contractId));
					} catch (SystemException e) {
						throw new NestableRuntimeException("Problem retrieving guaranteed account rates for contract: "+contractId
			                    + ReportDataTransformer.class.getName(), e);
					}
				}
			} else {
				// Generic Ireport
				reportData.setGuaranteedAccountRates(repositoryDataBean
						.getGuaranteedAccountRates(options.getClassMenu()));
			}
        }
		
		Map<String, String> footnotesOnReport = buildStaticFootnotesOnReport(options
				.getFundOffering().getCompanyId(), staticFootnoteSymbols);
		reportData.setFootnotes(footnotesOnReport);

		Map<String, String> companyIdFootnotesOnReport = buildCompanyIdFootnotesOnReport(
				repositoryDataBean.footnotesByCompany, options
						.getFundOffering().getCompanyId(), reportData
						.getFunds());
		reportData.setCompanyIdFootnotes(companyIdFootnotesOnReport);
		
		reportData.setInvestmentGroups(repositoryDataBean.investmentGroups);
		reportData.setAssetClasses(repositoryDataBean.assetClasses);
		reportData.setAssetCategories(repositoryDataBean.assetCategories);
		reportData.setAssetCategoryGroups(repositoryDataBean.assetCategoryGroups);
		
		reportData
				.setMarketIndexIbPerformances(buildMarketIndexIbPerformances(repositoryDataBean));
		reportData
				.setMorningstarCategoryPerformances(buildMorningstarCategoryPerformances(repositoryDataBean));
		reportData
				.setMorningstarStaticFootNotes(buildMorningstarStaticFootNotes(options));       
		//Changes done as part of ACR REWRITE (Historical iReport changes)
		reportData.setSelectedFromHistoricalReport(options.isSelectedFromHistoricalReport());
		reportData.setFootnotesByCompany(repositoryDataBean.footnotesByCompany);
		reportData.setMarketIndexFootnote(repositoryDataBean.footnotesByMarketIndex);
		
		return reportData;
	}

    /**
     * This method checks if the Guaranteed Account funds were part of the funds chosen.
     * 
     * @param dataBean
     * @param options
     * @return - true, if the Guaranteed Account funds were part of the funds chosen, else, returns
     *         false.
     */
	static boolean isGuaranteedAccountFundsChosen(FundReportDataBean dataBean, ReportOptions options) {
        boolean isGuaranteedAccountFundsChosen = false;

        for (int i = 0; i < options.getFundsChosen().length; i++) {
            String fundId = options.getFundsChosen()[i];
            Fund fund = dataBean.getFund(fundId);

            if (fund.isGuaranteedAccount()) {
                isGuaranteedAccountFundsChosen = true;
            }
        }

        return isGuaranteedAccountFundsChosen;
    }
	
	static LifestyleReportData getLifestyleReportData(
			FundReportDataBean repositoryDataBean,
			ReportOptions options,
			String fundSortOrder,
			String[] staticFootnoteSymbols) {
	
		LifestyleReportData reportData = new LifestyleReportData();
		
		//Alignment May 2014 release- Fetching Lifestyle funds dynamically
		Collection lifestyleFunds = repositoryDataBean.getAllFunds(options.getFundOffering(), StandardReportsConstants.ASSET_CLASS_LIFESTYLE);
		Collection fundInvestmentids = CollectionUtils.collect(lifestyleFunds,new InvestmentidTransformer());
		String[] fundsChosen = (String[]) fundInvestmentids.toArray(new String[0]);
		options.setFundsChosen(fundsChosen);
		
		List reportFunds = buildReportFunds(repositoryDataBean, options);
		
		Collections.sort(reportFunds, (Comparator) reportFundComparatorMap.get(fundSortOrder));
		reportData.setFunds(reportFunds);
		
		Map footnotesOnReport = buildFootnotesOnReport(
				repositoryDataBean.footnotesByCompany, options
						.getFundOffering().getCompanyId(),
				staticFootnoteSymbols, reportData.getFunds());
		reportData.setFootnotes(footnotesOnReport);
		
		reportData.setMarketIndexIbPerformances(buildMarketIndexIbPerformances(repositoryDataBean));
		
		return reportData;
	}

	private static List<FundMetrics> buildReportFunds(FundReportDataBean dataBean,
            ReportOptions options) {
		
		List reportFunds = new ArrayList();
		Map fundMetricsForDate = dataBean.fundMetricsByDate;
        Map fundExpensesForDate = dataBean.fundExpensesByDate;
        Map fundStandardDeviationsForDate = dataBean.fundStandardDeviationsByDate;
		
		for (int i = 0; i < options.getFundsChosen().length; i++) {
			String fundId = options.getFundsChosen()[i];
			//TODO This null check is added for the Historical iReport Funds
			//if( dataBean.getFund(fundId)!=null){
			Fund fund = dataBean.getFund(fundId);
			
			if (fund.isMarketIndex()) {
                // Funds Chosen that we get from F&P page also has the Market Index Funds. This is
                // being stripped out. The Market Indexes are added in the later steps in
                // getFundReportData() method.
                continue;
            }
			
			ReportFund reportFund = new ReportFund(fund);
			String investmentid = fund.getInvestmentid();
			FundMetrics fundMetrics = null;
			
			String classSelected = options.getClassMenu();
			
			if (options.getContractFundsMap() != null
					&& !options.getContractFundsMap().isEmpty()
					&& options.getContractFundsMap().get(fundId) != null) {
				classSelected = options.getContractFundsMap().get(fundId).getRatetype();
			}
			
			List<String> jhiFundList = null;
			String classID = "";
			try {
				jhiFundList = FundDAO.getJHIFunds();
				classID = FundDAO.getJHIClassID(classSelected);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			if (fundMetricsForDate != null) {
				if (classSelected.equals(StandardReportsConstants.RATE_TYPE_CX0) && !jhiFundList.contains(investmentid)) {
					fundMetrics = (FundMetrics) fundMetricsForDate.get(new FundRatetypeKey(investmentid, classID));
				} else {
					fundMetrics = (FundMetrics) fundMetricsForDate
							.get(new FundRatetypeKey(investmentid, classSelected));
				}
			}
			// should we exclude funds with no metrics?
			reportFund.setFundMetrics(fundMetrics == null ? new FundMetrics() : fundMetrics);
			
			FundExpenses fundExpenses = null;
			
			if (classSelected.equals(StandardReportsConstants.RATE_TYPE_CX0) && !(jhiFundList.contains(fundId))) {
				fundExpenses = (FundExpenses) fundExpensesForDate.get(new FundRatetypeKey(investmentid, classID));
			}else {
				fundExpenses = (FundExpenses) fundExpensesForDate.get(new FundRatetypeKey(investmentid, classSelected));
			}			
			
			reportFund.setFundExpenses(fundExpenses == null ? new FundExpenses() : fundExpenses);
			
			if (fundStandardDeviationsForDate == null) {
				reportFund.setFundStandardDeviation(new FundStandardDeviation());
			} else {
				FundStandardDeviation fundStandardDeviation = (FundStandardDeviation) fundStandardDeviationsForDate
						.get(new FundRatetypeKey(investmentid, classSelected));
				reportFund.setFundStandardDeviation(fundStandardDeviation == null ? new FundStandardDeviation() : fundStandardDeviation);
			}
			
			String[] symbolsArray = buildFundFootnoteSymbols(dataBean, investmentid);
			reportFund.setFootnoteSymbols(symbolsArray);
			
			reportFunds.add(reportFund);
		}
		//}
		return reportFunds;
	}

	private static List buildMarketIndexes(FundReportDataBean dataBean) {
		List reportMarketIndexes = new ArrayList();
		
		Map marketIndexPerformancesForDate = dataBean.marketIndexPerformancesByDate;
		if (marketIndexPerformancesForDate == null) {
			throw new IllegalStateException("Can not find any market index performance data ");
		}
		for (Iterator iter = dataBean.marketIndexes.iterator(); iter.hasNext();) {
			Fund marketIndex = (Fund) iter.next();
			ReportFund reportFund = new ReportFund(marketIndex);
			FundMetrics miMetrics =
					(FundMetrics) marketIndexPerformancesForDate.get(marketIndex.getInvestmentid());
			// should we exclude funds with no metrics?
			reportFund.setFundMetrics(miMetrics == null ? new FundMetrics() : miMetrics);
			
			String[] symbolsArray = buildFundFootnoteSymbols(dataBean, marketIndex.getInvestmentid());
			reportFund.setFootnoteSymbols(symbolsArray);
			
			reportMarketIndexes.add(reportFund);
		}
		
		return reportMarketIndexes;
	}

	private static Map buildMarketIndexIbPerformances(FundReportDataBean dataBean) {
        Map marketIndexIbPerformancesForDate = dataBean.marketIndexIbPerformancesByDate;
		return marketIndexIbPerformancesForDate;
	}
	
	private static Map buildMorningstarCategoryPerformances(FundReportDataBean dataBean) {
        Map morningstarCatgeoryPerformancesForDate = dataBean.morningstarCategoryPerformancesByDate;
		return morningstarCatgeoryPerformancesForDate;
	}


	protected static Map buildFootnotesOnReport(Map footnotesByCompany, String companyId, 
			String[] staticFootnoteSymbols, Collection reportFunds) {
		
		Map unorderedFootnotes = new HashMap();
		
		buildStaticFootnoteOnReport(companyId, staticFootnoteSymbols, unorderedFootnotes);
		Map orderedFootnotes = sortFootnotesOnReport(unorderedFootnotes);
//		Map orderedFundFootnotes = new LinkedMap();
		Map unorderedFundFootnotes = new LinkedMap();
		buildFundFootnoteOnReport(footnotesByCompany, companyId, reportFunds, unorderedFundFootnotes);
		
		Footnote[] footnotes = (Footnote[])unorderedFundFootnotes.values().toArray(new Footnote[0]);
		Arrays.sort( footnotes, FootnotesComparator.getInstance() );
		for (Footnote footnote: footnotes) {
			String symbol = footnote.getSymbol();
			orderedFootnotes.put(symbol, footnote.getText());
		}
//		orderedFootnotes.putAll(orderedFundFootnotes);
		return orderedFootnotes;
		
	}
	
	/**
	 * Build Static Foot Notes
	 * 
	 * @param companyId
	 * @param staticFootnoteSymbols
	 * 
	 * @return Map
	 */
	protected static Map<String, String> buildStaticFootnotesOnReport(String companyId,
			String[] staticFootnoteSymbols) {

		Map<String, Footnote> unorderedFootnotes = new HashMap<String, Footnote>();
		buildStaticFootnoteOnReport(companyId, staticFootnoteSymbols,
				unorderedFootnotes);
		Map<String, String> orderedFootnotes = sortFootnotesOnReport(unorderedFootnotes);
		
		return orderedFootnotes;
	}

	/**
	 * Build Foot Notes for CompanyId
	 * 
	 * @param footnotesByCompany
	 * @param companyId
	 * @param reportFunds
	 * 
	 * @return Map
	 */
	protected static Map<String, String> buildCompanyIdFootnotesOnReport(
			Map footnotesByCompany, String companyId, Collection reportFunds) {

		Map<String, String> unorderedFundFootnotes = new LinkedMap();
		buildFundFootnoteOnReport(footnotesByCompany, companyId, reportFunds,
				unorderedFundFootnotes);
		Map<String, String> orderedFootnotes = sortFootnotesOnReport(unorderedFundFootnotes);
		Footnote[] footnotes = (Footnote[]) unorderedFundFootnotes.values()
				.toArray(new Footnote[0]);
		Arrays.sort(footnotes, FootnotesComparator.getInstance());
		for (Footnote footnote : footnotes) {
			String symbol = footnote.getSymbol();
			orderedFootnotes.put(symbol, footnote.getText());
		}
		
		return orderedFootnotes;
	}
	
	/**
	 * Builds Morningstar Foot Notes
	 * 
	 * @param fundList
	 * @param reportData
	 * @param options
	 * 
	 * @return morningStarFootnotes Map<String, String>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> buildMorningstarFootNotes(
			List<List<ReportFund>> fundList, FundReportData reportData,
			ReportOptions options) {

		Map<String, String> morningStarFootnotes = new LinkedMap();
		Map<String, MorningstarCategoryPerformance> morningstarCategoryPerformances = reportData
				.getMorningstarCategoryPerformances();
		String companyId = options.getCompanyId();

		if (morningstarCategoryPerformances != null) {
			StaticFootnoteDAO staticFootnoteDAO = null;
			for (Iterator iter = fundList.iterator(); iter.hasNext();) {
				List<ReportFund> reportFundsList = (List) iter.next();
				for (Iterator itera = reportFundsList.iterator(); itera
						.hasNext();) {
					ReportFund reportFund = (ReportFund) itera.next();
					Footnote footNote = null;
					MorningstarCategoryPerformance morningstar = (MorningstarCategoryPerformance) morningstarCategoryPerformances
							.get(reportFund.getFund().getInvestmentid());
					if (morningstar != null
							&& StringUtils.isNotBlank(reportFund.getFund()
									.getMorningstarRating())) {
						Map<String, List<String>> morningstarFootNoteMap = morningstar
								.getMorningstarFootNoteMap();
						if (morningstarFootNoteMap != null) {
							List<String> morningstarFootNoteList = morningstarFootNoteMap
									.get(reportFund.getFund().getInvestmentid());
							staticFootnoteDAO = new StaticFootnoteDAO(companyId);
							if (morningstarFootNoteList != null && !morningstarFootNoteList.isEmpty()) {
								String[] contentParams = morningstarFootNoteList
										.toArray(new String[morningstarFootNoteList.size()]);
								// Replacing the fund name with long name to show in i:reports
								contentParams[0] = reportFund.getFund().getFundLongName();
								footNote = staticFootnoteDAO
										.retrieveFootnoteWithParams(
												Integer
														.toString(morningstar
																.getMorningstarFootNoteCMAId()),
												contentParams);
							}
							if (footNote != null) {
								morningStarFootnotes.put(reportFund.getFund()
										.getInvestmentid(), footNote.getText());
							}
						}
					}
				}
			}
		}
		return morningStarFootnotes;
	}

	private static Map sortFootnotesOnReport(Map unorderedFootnotes) {
		List allFootnoteSymbolsOnReport = new ArrayList(unorderedFootnotes.keySet());
		Collections.sort(allFootnoteSymbolsOnReport, FOOTNOTE_SYMBOL_COMPARATOR);

		Map result = new LinkedMap();
		for (Iterator iter = allFootnoteSymbolsOnReport.iterator(); iter.hasNext();) {
			String symbol = (String) iter.next();
			result.put(symbol, ((Footnote)unorderedFootnotes.get(symbol)).getText());
		}
		return result;
	}

	private static void buildStaticFootnoteOnReport(String companyId, String[] staticFootnoteSymbols, Map unorderedFootnotes) {
		StaticFootnoteDAO staticFootnoteDAO = new StaticFootnoteDAO(companyId);
		
		Map<String, Footnote> staticFootnotes = staticFootnoteDAO.retrieveFootnote(staticFootnoteSymbols);
		unorderedFootnotes.putAll(staticFootnotes);
	}

	protected static void buildFundFootnoteOnReport(Map footnotesByCompany, String companyId, Collection reportFunds, Map unorderedFootnotes) {
		Map footnotesForCompany = (Map) footnotesByCompany.get(companyId);
		if (footnotesForCompany == null) {
			return;
		}

		for (Iterator iter = reportFunds.iterator(); iter.hasNext();) {
			ReportFund reportFund = (ReportFund) iter.next();
			String[] footnoteSymbols = reportFund.getFootnoteSymbols();
			if (footnoteSymbols != null) {
				for (int i = 0; i < footnoteSymbols.length; i++) {
					String symbol = footnoteSymbols[i];
					if (unorderedFootnotes.containsKey(symbol)) {
						log.warn("Footnote symbol [" + symbol + "] exists in both static footnotes and fund footnotes. Overriding static footnote with fund footnote.");
					}
					MutableFootnote footnote = (MutableFootnote) footnotesForCompany.get(symbol);
					if (footnote != null) {
						String footnoteText = footnote.getText();
						unorderedFootnotes.put(symbol, new Footnote(symbol, footnoteText, footnote.getOrderNumber()));
					}
				}
			}
		}
	}

	private static String[] buildFundFootnoteSymbols(FundReportDataBean dataBean, String fundId) {
		String[] symbolsArray = new String[0];
		List symbolsList = (List)dataBean.fundFootnoteSymbols.get(fundId);
		if (symbolsList != null) {
			symbolsArray = new String[symbolsList.size()];
			int j = 0;
			for (Iterator iter = symbolsList.iterator(); iter.hasNext();) {
				FundFootnoteSymbol fundFootnoteSymbol = (FundFootnoteSymbol) iter.next();
				symbolsArray[j] = fundFootnoteSymbol.getFootnotesymbol().trim();
				j++;
			}
		}
		return symbolsArray;
	}

	public static AssetClassReportData getAssetClassReportData(Map<String, AssetClass> assetClasses) {
		AssetClassReportData data = new AssetClassReportData();
		data.setAssetClasses(assetClasses);
		return data;
	}
	
	public static LifecycleReportData getLifecycleReportData(FundReportDataBean dataBean,
            ReportOptions options, String fundSortOrder, String[] staticFootnoteSymbols) {
		LifecycleReportData reportData = new LifecycleReportData();
		
		Collection lifecycleFunds = dataBean.getAllFunds(options.getFundOffering(), StandardReportsConstants.ASSET_CLASS_LIFECYCLE);
		Collection fundInvestmentids = CollectionUtils.collect(lifecycleFunds,new InvestmentidTransformer());
		String[] fundsChosen = (String[]) fundInvestmentids.toArray(new String[0]);
		options.setFundsChosen(fundsChosen);
		
		List reportFunds = buildReportFunds(dataBean, options);
		
		Collections.sort(reportFunds, (Comparator) reportFundComparatorMap.get(fundSortOrder));
		reportData.setFunds(reportFunds);
		
		Map footnotesOnReport = buildFootnotesOnReport(dataBean.footnotesByCompany, options.getFundOffering().getCompanyId(), staticFootnoteSymbols, reportData.getFunds());
		reportData.setFootnotes(footnotesOnReport);
		
		reportData.setMarketIndexIbPerformances(buildMarketIndexIbPerformances(dataBean));
		
		return reportData;
		
	}
	
	private static class AlphabeticalReportFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			ReportFund reportFund1 = (ReportFund)p1;
			ReportFund reportFund2 = (ReportFund)p2;
			String fundName1 = reportFund1.getFund().getFundname();
			String fundName2 = reportFund2.getFund().getFundname();
			return fundName1.compareToIgnoreCase(fundName2);
		}
	}
		
	private static class RiskReturnReportFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			ReportFund reportFund1 = (ReportFund)p1;
			ReportFund reportFund2 = (ReportFund)p2;
			Fund fund1 = reportFund1.getFund();
			Fund fund2 = reportFund2.getFund();
			
			// Lifecycle/Lifestyle investment groups actually appear just before market indexes,
			// not first, so if Order is 1 (Lifestyle) then treat it as 98 (and 0, LC, is 97)
			
			int compare1 = fund1.getOrder() == 1 ? 98 : fund1.getOrder();
			int compare2 = fund2.getOrder() == 1 ? 98 : fund2.getOrder();
			compare1 = compare1 == 0 ? 97 : compare1;
			compare2 = compare2 == 0 ? 97 : compare2;
			
			//Guaranteed Income feature category should be placed after asset allocation lifestyle 
			//if order is 2 treat it as 99
			compare1 = compare1 == 2 ? 99 : compare1;
			compare2 = compare2 == 2 ? 99 : compare2;
			
			if (compare1 == compare2) {
				// do secondary sort
				compare1 = fund1.getSortnumber();
				compare2 = fund2.getSortnumber();
			}
			return compare1 - compare2; 
		}
	}
	
	private static class AssetCategoryReportFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			ReportFund reportFund1 = (ReportFund)p1;
			ReportFund reportFund2 = (ReportFund)p2;
			Fund fund1 = reportFund1.getFund();
			Fund fund2 = reportFund2.getFund();
			
			Integer compare1 = fund1.getAssetCategoryGroupOrderNo() == null ? new Integer(0) : fund1.getAssetCategoryGroupOrderNo();
			Integer compare2 = fund2.getAssetCategoryGroupOrderNo() == null ? new Integer(0) : fund2.getAssetCategoryGroupOrderNo();;
			
			if (compare1 == compare2) {
				// do secondary sort
				compare1 = fund1.getAssetCategoryOrderNo() == null ? new Integer(0) : fund1.getAssetCategoryOrderNo();
				compare2 = fund2.getAssetCategoryOrderNo() == null ? new Integer(0) : fund2.getAssetCategoryOrderNo();;
			}
			return compare1.compareTo(compare2); 
		}
	}
	
	private static class AssetClassReportFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			ReportFund reportFund1 = (ReportFund)p1;
			ReportFund reportFund2 = (ReportFund)p2;
			Fund fund1 = reportFund1.getFund();
			Fund fund2 = reportFund2.getFund();
			
			Integer compare1 = fund1.getAssetclsOrder() == null ? new Integer(0) : fund1.getAssetclsOrder();
			Integer compare2 = fund2.getAssetclsOrder() == null ? new Integer(0) : fund2.getAssetclsOrder();
			
			return compare1.compareTo(compare2); 
		}
	}
	
	private static class OneMonthReturnReportFundComparator implements Comparator {
		public int compare (Object p1, Object p2){
			ReportFund reportFund1 = (ReportFund)p1;
			ReportFund reportFund2 = (ReportFund)p2;
			FundMetrics fundMetrics1 = reportFund1.getFundMetrics();
			FundMetrics fundMetrics2 = reportFund2.getFundMetrics();
			
			BigDecimal compare1 = fundMetrics1.getRor1mth() == null ? new BigDecimal(-999) : fundMetrics1.getRor1mth();
			BigDecimal compare2 = fundMetrics2.getRor1mth() == null ? new BigDecimal(-999) : fundMetrics2.getRor1mth();
			
			return (-1 * compare1.compareTo(compare2)); 
		}
	}
	
	/**
	 * Gets Morningstar Static Foot Notes
	 * 
	 * @param options
	 * 
	 * @return Map<String, String> morningstarStaticFootNoteMap
	 */
	private static Map<String, String> buildMorningstarStaticFootNotes(
			ReportOptions options) {
		Map<String, String> morningstarStaticFootNoteMap = new LinkedHashMap<String, String>();
		StaticFootnoteDAO staticFootnoteDAO = new StaticFootnoteDAO(options
				.getCompanyId());
		Footnote footNote = null;
		String[] contentParams = null;
		footNote = staticFootnoteDAO
				.retrieveFootnoteWithParams(
						CommonConstants.FOOTNOTE_MORNINGSTAR_PERFORMANCE,
						contentParams);
		if (footNote != null) {
			morningstarStaticFootNoteMap.put(
					CommonConstants.FIRST_FOOTNOTE_MORNINGSTAR_PERFORMANCE,
					footNote.getText());
		}
		footNote = staticFootnoteDAO.retrieveFootnoteWithParams(
				CommonConstants.FOOTNOTE_MORNINGSTAR_SYMBOL_EXPLANATION,
				contentParams);
		if (footNote != null) {
			morningstarStaticFootNoteMap
					.put(
							CommonConstants.SECOND_FOOTNOTE_MORNINGSTAR_SYMBOL_EXPLANATION,
							footNote.getText());
		}
		return morningstarStaticFootNoteMap;
	}
}
