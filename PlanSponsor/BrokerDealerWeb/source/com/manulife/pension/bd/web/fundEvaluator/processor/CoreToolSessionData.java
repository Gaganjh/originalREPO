package com.manulife.pension.bd.web.fundEvaluator.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.coretool.model.common.SelectionCriteria;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.ContractFund;
import com.manulife.pension.service.fund.valueobject.Fund;
import com.manulife.pension.service.fund.valueobject.FundMetrics;
import com.manulife.pension.service.fund.valueobject.PercentileRankedFund;


/**
 * This object will has base fund line up & ranking information (but not detailed fund metrics )
 * @author: PWakode
 */

public class CoreToolSessionData implements CoreToolConstants, java.io.Serializable {
    
    private static final Logger logger = Logger.getLogger(CoreToolSessionData.class);

	private static final long serialVersionUID = 1L;

    private Map<String, List<PercentileRankedFund>> rankedFundsByAssetClassID = new Hashtable<String, List<PercentileRankedFund>>();

	private Map<String, PercentileRankedFund> rankedFundsByFundID = new Hashtable<String, PercentileRankedFund>();

	private Hashtable<String, String> toolRecommendedFunds = new Hashtable<String, String>();

	private Hashtable<String, String> selectedAssetClasses = new Hashtable<String, String>();
	
	private List<String> investmentIdsPlusRateType;
	private List<String> investmentIds;
	private List<String> contractSelectedFunds;

	/**
	 * CoreToolBOSData constructor.
	 * @param SessionContext object - contains all user input from UI
	 * @throws SystemException 
	 */
	public CoreToolSessionData(SessionContext sessionContext) throws SystemException {
		
		populateSelectedAssetClasses(sessionContext);
		logger.debug("populateSelectedAssetClasses Done");
		
		calculateRankedFunds(sessionContext);
		logger.debug("calculateRankedFunds Done");

		calculateToolRecommendedFunds(sessionContext);
		logger.debug("calculateToolRecommendedFunds Done");
	}
	
	/**
     * This method is called from calculateRankedFunds() to populate the rankedFundsByAssetClassID Map
     * 
     * @param assetClassID
     * @param rankedFund
     * @throws SystemException
     */
    private void addRankedFundToAssetClassIDTable(String assetClassID,
            PercentileRankedFund rankedFund)
            throws SystemException {
        
        try {

            // First add the Percentile Ranked Fund to rankedFundsByAssetClassID
            List<PercentileRankedFund> funds = rankedFundsByAssetClassID.get(assetClassID);
            if (funds == null) {
                funds = new Vector<PercentileRankedFund>();
                if (!funds.contains(rankedFund)) {
                    rankedFundsByAssetClassID.put(assetClassID, funds);
                }
            }
            funds.add(rankedFund);// added to its own based on Asset class Id (eg. LCV fund will be added to LCV asset class, IDX fund would be added to IDX asset class)
            
        } catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Problem occured while creating Map rankedFundsByAssetClassID" + e);
             }
            throw new SystemException(e, "Problem occured while creating Map rankedFundsByAssetClassID" + e.getMessage());
        }
    }
    
    /**
	 * This method is called from calculateRankedFunds() to populate the rankedFundsByFundID Map
	 * 
	 * @param fundID
	 * @param rankedFund
	 */
	private void addRankedFundToFundIDTable(String fundID, PercentileRankedFund rankedFund) {
		rankedFundsByFundID.put(fundID, rankedFund);
	}
	
	/**
	 * We are passed the SessionContext from which we will use the metrics criteria
	 * to calculate the scores of each of the funds in the product ID/Asset class combination
	 * and populates required map objects
	 * 
	 * @param SessionContext object - contains all user input from UI
	 * @throws SystemException
	 */
	private void calculateRankedFunds(SessionContext sessionInfo) throws SystemException{
	
		FundMetrics fundMetrics = null;
		int displayRank = 0;
        BigDecimal rank = new BigDecimal(-1);
		String thisFundsAssetClass = "";
		PercentileRankedFund percentileRankedFund = null;
		Hashtable<String, ContractFund> contractFundTable = null;
		String rateType = null;
        investmentIdsPlusRateType = new ArrayList<String>();
        int sortNumber = 0;
        CoreToolHelper coreToolHelper = new CoreToolHelper();
        
		// Obtain the array of criteria the user selected:
        LinkedHashMap<Integer, Integer> criteria = sessionInfo.getMetricSelectionCriteria();
		
		// GIFL select funds should be part of giflFundsList, so that those funds are not filtered. 
		List<String> giflFundsList = FundServiceDelegate.getInstance().getGIFLFundsList(sessionInfo.isIncludeGIFLSelectFunds());
	
		// Obtain an enumeration of the funds under the selected product ID:
		try {
		    if(StringUtils.isNotBlank(sessionInfo.getContract()) ){//if contract is provided
                int contractId = Integer.parseInt(sessionInfo.getContract());
                contractFundTable = coreToolHelper.getContractFunds(contractId);
                contractSelectedFunds = new ArrayList<String>();
                
                Iterator<String> itr = contractFundTable.keySet().iterator();
                while(itr.hasNext()){   
                    String investmentId = itr.next();
                    
                    if(contractFundTable.get(investmentId).isSelected()){
                        contractSelectedFunds.add(investmentId);
                    }
                }
            }
            //below method call determines Base fund line up (by contract OR by fund/class menu options)
            investmentIds = coreToolHelper.getInvestmentIdListForRankCalculation(sessionInfo, contractFundTable, giflFundsList);
            
            //below method call assigns appropriate Rate type to an Individual Id (if Contract is provided, then rate type is taken from contract, else rate type selected on UI is considered)
            investmentIdsPlusRateType = coreToolHelper.getInvestmentIdsPlusRateTypeList(sessionInfo, investmentIds, contractFundTable);
                        
            Hashtable<String, FundMetrics> fundLineupFundMetricTable = FundServiceDelegate.getInstance().getFundMetricObjTableForFundAndRateList(investmentIdsPlusRateType);
            
            Hashtable<String, Fund> fundLineupFundTable = FundServiceDelegate.getInstance().getFundObjTableForFundList(investmentIds);
            
            List<String> assetClassesWithBenchmarkMetricsAvailableList = coreToolHelper.getAssetClassesWithBenchmarkMetricsAvailableList();
            
            investmentIds = coreToolHelper.filterClosedFundsBasedOnMode(sessionInfo, investmentIds, contractFundTable, investmentIdsPlusRateType);
            List<String> jhiFunds = FundServiceDelegate.getInstance().getJHIFunds();
            Iterator<String> investmentIdIter = investmentIds.iterator();
            logger.debug("calculateRankedFunds: Obtained Enumeration of Funds under selected product Id:" + investmentIds);
		
            logger.debug("LIST OF FUNDS AND RATE TYPE:");
            
			// Loop through the funds for this product ID
			while (investmentIdIter.hasNext()) {
				// "this" is the currently looping fund in the set of all funds
				// for this particular product ID
				String investmentId = investmentIdIter.next();
				logger.debug(investmentId);
				
				// Get the Asset Class for "this" fund:
				thisFundsAssetClass = fundLineupFundTable.get(investmentId).getAssetClassId();
				logger.debug("ASSETCLASS=" + thisFundsAssetClass);
	
				if(StringUtils.isNotBlank(sessionInfo.getContract())) {
				    rateType = contractFundTable.get( investmentId).getRateType();
				    logger.debug("RATE TYPE for CONTRACT=" + rateType +"investmentId was :"+investmentId);
				}
				else{
				    rateType = sessionInfo.getClassMenu();
				    logger.debug("RATE TYPE GENERIC=" + rateType);
				}
				if(rateType.equals(BDConstants.SIGNATURE_PLUS)){
					if (jhiFunds.contains(investmentId)) {
					fundMetrics = fundLineupFundMetricTable.get(investmentId + rateType);
					}else {
						rateType = FundServiceDelegate.getInstance().getJHIClassID(rateType);
						fundMetrics = fundLineupFundMetricTable.get(investmentId + rateType);
					}
			    }else{
				fundMetrics = fundLineupFundMetricTable.get(investmentId + rateType);
			    }
                sortNumber = fundLineupFundTable.get(investmentId).getSortNumber();//required for fund sorting
                
	
				//The fund should have a fund metrics, and an entry in the BenchMark metrics in order to be ranked
				if (fundMetrics != null &&
                        (hasFundMetrics(fundMetrics) || isOnlyAICSelected(criteria) && fundMetrics.getPercentileRankings()[SelectionCriteria.FEES] != null) &&
                        assetClassesWithBenchmarkMetricsAvailableList.contains(thisFundsAssetClass)) {
	
	
					// Loop through the 11 criteria (Tracking error is not an iEvaluator option criteria).  Of which 5 may have weights.

					// SelectionCriteria is subtracted by 4 to ignore the 1yr, 5yr and 10yr
					// criteria's. Once the 1yr, 5yr and 10yr are implemented this should be
					// changed as (SelectionCriteria.TOTAL)-1
					boolean nullRankingExists = false;
					for(Entry<Integer, Integer> entry : criteria.entrySet()) {
						// This criterion wasn't selected. Try the next criterion.
						if(entry.getValue() == 0) {
							continue;
						}
						if (fundMetrics.getPercentileRankings()[entry.getKey()] == null ) {
							nullRankingExists = true;
							break;		
						}
						// a criteria > 0 implies we have to multiply this criterion to the percentile
						// and add that to our score, which is an Integer value, representing a percentage
						rank = rank.add(fundMetrics.getPercentileRankings()[entry.getKey()].multiply(new BigDecimal(entry.getValue())));
					}
					
					if (nullRankingExists) {
						percentileRankedFund = new PercentileRankedFund(investmentId, rateType, new BigDecimal(-1), false, sortNumber, displayRank);
	                    addRankedFundToFundIDTable(investmentId, percentileRankedFund);
						addRankedFundToAssetClassIDTable(thisFundsAssetClass, percentileRankedFund);
						logger.debug("No Fund metrics=" + investmentId);
					}else{
	
						rank = rank.divide(new BigDecimal(100), 6, 2);//round ceiling. Maintaining extra scale of 6, which is 2 digits extra so that rounding operation has no impact on precision
	                    displayRank = Integer.parseInt(coreToolHelper.convertPercentileToRankForDisplay(rank));
						
						percentileRankedFund = new PercentileRankedFund(investmentId, rateType, rank, true, sortNumber, displayRank);
	                    
	                    // We add the percentile ranked fund
	                    addRankedFundToAssetClassIDTable(thisFundsAssetClass, percentileRankedFund);//here AssetClass being passed is the Asset Class of the fund (not general Asset Class)
						addRankedFundToFundIDTable(investmentId, percentileRankedFund);
					}
	
				} else {
					// There was no fund metrics object so we cannot rank.
					percentileRankedFund = new PercentileRankedFund(investmentId, rateType, new BigDecimal(-1), false, sortNumber, displayRank);
                    addRankedFundToFundIDTable(investmentId, percentileRankedFund);
					addRankedFundToAssetClassIDTable(thisFundsAssetClass, percentileRankedFund);
					logger.debug("No Fund metrics=" + investmentId);
				}
	
				// Prepare for next fund in this product ID set
				rank = new BigDecimal(0);
                displayRank = 0;
				percentileRankedFund = null;
				fundMetrics = null;
				thisFundsAssetClass = null;
			}
	
		} catch (Throwable e) {
		    if (logger.isDebugEnabled()) {
                logger.debug("Problem occured while calculating Fund Ranking" + e);
             }
            throw new SystemException(e, "calculateRankedFunds : Problem occured while calculating Fund Ranking "+ e.getMessage());
        }
		for (Iterator<String> assetClassIDs = rankedFundsByAssetClassID.keySet().iterator(); assetClassIDs.hasNext(); ) { 
			thisFundsAssetClass = assetClassIDs.next();
			logger.debug("sortRankedFundsVector");
			sortRankedFundsVector(thisFundsAssetClass);
		}
	}
	
	/**
     * This methods toolRecommendedFunds hash table (contains tool pre-selected funds based on user input)
     * 
     * @param SessionContext object - contains all user input from UI
     * @throws SystemException
     */
    private void calculateToolRecommendedFunds(SessionContext sessionInfo) throws SystemException {
         
        try {
            List<String> allFundsClosedToNB = FundServiceDelegate.getInstance().getListOfAllFundsClosedToNB();
            
            CoreToolHelper coreToolHelper = new CoreToolHelper();
            Map<String, String> additionalFundsNotToBeSelected = coreToolHelper.getAdditionalFundsNotToBeSelected(sessionInfo.getSVFFundList());
            
            List<String> assetClassesWithBenchmarkMetricsAvailableList = coreToolHelper.getAssetClassesWithBenchmarkMetricsAvailableList();
		
			Enumeration<String> assetClassEnum = selectedAssetClasses.keys();
	
			while (assetClassEnum.hasMoreElements()) {
				String assetClassId = assetClassEnum.nextElement();
				
				//skips adding top ranked for 12 Asset Classes i.e. Hybrid(IDX, BAL, SEC, LSF,LCF), Multi (MCF,MBC,MGC), GUA(GA3,GA5,G10), GLB Asset Classes
				// checking for benchmarks if not we know for sure we would not have calculated the ranks on Step 1
                if (assetClassesWithBenchmarkMetricsAvailableList.contains(assetClassId)) {
                    //as per new rule - SEC will also have top ranked fund selected
                    // Only the fund with the highest score is selected by the tool, provided that it is not Contra nor Janus
					List<PercentileRankedFund> rankedFunds = getRankedFundsByAssetClassID(assetClassId);
					if (rankedFunds.isEmpty()) {
					    logger.debug("ERROR in CoreToolSessionData - getRankedFundsByAssetClassID() returned empty list for assetClassId: " + assetClassId);
					    logger.debug("rankedFundsByAssetClassID=" + rankedFundsByAssetClassID);
					}
					Iterator percentileRankedFunds = rankedFunds.iterator();
					
                    BigDecimal topRankInThisAssetClass = new BigDecimal(0);
					while (percentileRankedFunds.hasNext()) {
						PercentileRankedFund percentileRankedFund = (PercentileRankedFund) percentileRankedFunds.next();
						String fundId = percentileRankedFund.getFundID();
                        
                        if(sessionInfo.isSelectAll()){//select top ranked without checking FSW compliance (this applies to FXM, FXL & HYF funds - none of them are FSW compliant)
                            if (percentileRankedFund.isBenchmarkMetricsAvailable()
                                    && !(additionalFundsNotToBeSelected.containsKey(fundId) || allFundsClosedToNB.contains(fundId) )){//if fund not in any of these lists, its eligible
                                toolRecommendedFunds.put(fundId, assetClassId);
                                topRankInThisAssetClass = percentileRankedFund.getRank();
                                break;//here it breaks out just after adding top most fund, but we have to add all top ranked funds, thus cannot break here
                            }
                        }
					}
                    //another loop is to add other funds(if not already present) with same ranking as top most fund
                    percentileRankedFunds = rankedFunds.iterator();//re-set iterator & loop all again
                    while (percentileRankedFunds.hasNext()) {
                        PercentileRankedFund percentileRankedFund = (PercentileRankedFund) percentileRankedFunds.next();
                        String fundId = percentileRankedFund.getFundID();
                        
                        BigDecimal currentFundRank = percentileRankedFund.getRank();
                        if (currentFundRank.equals(topRankInThisAssetClass)){//then this fund should also be added to toolRecommended
                            if(sessionInfo.isSelectAll()){//select top ranked without checking FSW compliance (this applies to FXM, FXL & HYF funds - none of them are FSW compliant)
                                if (percentileRankedFund.isBenchmarkMetricsAvailable()
                                        && !(additionalFundsNotToBeSelected.containsKey(fundId) || allFundsClosedToNB.contains(fundId) )){//if fund not in any of these lists, its eligible
                                    toolRecommendedFunds.put(fundId, assetClassId);
                                    topRankInThisAssetClass = percentileRankedFund.getRank();
                                }
                            }
                        }
                        else//if currentRank is less than topmost rank - there is no way any other fund could be candidate for adding to toolRecommended Map, hence we break here
                            break;
                    }
				}
			}
			   
		}
        catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Problem occured while determining Tool Recommended Funds" + e);
             }
            throw new SystemException(e, "calculateToolRecommendedFunds : Problem occured while determining Tool Recommended Funds "+e.getMessage());
        }
	}
	
	/**
	 * This returns a PercentileRankedFund given a fundID from the RankedFundsByFundID hash table.
	 * This is simply an object retriever for this hash table.
	 * 
	 * @param fundID
	 * @return
	 */
	public PercentileRankedFund getRankedFundByFundID(String fundID) {
        return rankedFundsByFundID.get(fundID);
    }
	
	/**
    * This method returns list of Fund id concatenated by rate type for each fund in base fund line up
    * @return List<String>
    */
	public List<String> getInvestmentIdsPlusRateType() {
		return investmentIdsPlusRateType;
	}
	
	/**
    * This method returns list of Fund IDs which will make up the base fund line up
    * @return List<String>
    */
	public List<String> getInvestmentIds() {
        return investmentIds;
    }
	/**
	 * This method returns List of fund IDs within contract with Selected flag = Y
	 * @return List<String>
	 */
	public List<String> getContractSelectedFunds() {
        return contractSelectedFunds;
    }
	
	/**
	* This returns a List of PercentileRankedFunds sorted by rank,
	* given an assetClass.  It retrieves from the RankedFundsByAssetClassID hash table.
	* This is simply an object retriever for this hash table.
	* 
	* @return List<PercentileRankedFund>
	*/
	public List<PercentileRankedFund> getRankedFundsByAssetClassID(String assetClassID) {
		return rankedFundsByAssetClassID.get(assetClassID);
	}
	
	/**
	 * 
	 * This map contains list of PercentileRankedFund objects.  The vectors are in
	 * decreasing order by rank.  The key for the map is Asset Class ID.   You can use
	 * This method to directly access the map, or you can use
	 * getRankedfundsByAssetClassID(assetClassID) to retrieve from this map.
	 * 
	 * @return Map<String, List<PercentileRankedFund>>
	 */
	public Map<String, List<PercentileRankedFund>> getRankedFundsByAssetClassIDMap() {
		return rankedFundsByAssetClassID;
	}
	
	/**
	 * This hash table contains PercentileRankedFund objects. The key for the
	 * map is Fund ID.   You can use this method to directly access the
	 * map, or you can use getRankedfundsByFundID(fundID) to retrieve from this hash.
	 * 
	 * @return Map<String, PercentileRankedFund>
	 */ 
	public Map<String, PercentileRankedFund> getRankedFundsByFundIDMap() {
		return rankedFundsByFundID;
	}
	
	/**
     * Returns hash table of selected Asset class IDs
     * @return Map<String, String>
     */ 
	public Map<String, String> getSelectedAssetClassesMap() {
		return selectedAssetClasses;
	}
	
	/**
	 * Returns map of tool recommended fund IDs. Key is fundID and value is AssetClassID
	 * @return Map<String, String>
	 */
	public Map<String, String> getToolRecommendedFundsMap() {
		return toolRecommendedFunds;
	}
	
	/**
	 * @param fundMetrics
	 * @return boolean
	 */
	private boolean hasFundMetrics(FundMetrics fundMetrics) {
		return
			fundMetrics.getValues()[SelectionCriteria.INFORMATION_RATIO] != null ||
			fundMetrics.getValues()[SelectionCriteria.ALPHA] != null ||
			fundMetrics.getValues()[SelectionCriteria.R2] != null ||
			fundMetrics.getValues()[SelectionCriteria.SHARPE_RATIO] != null ||
			fundMetrics.getValues()[SelectionCriteria.STANDARD_DEVIATION] != null ||
			fundMetrics.getValues()[SelectionCriteria.TOTAL_RETURN] != null ||
            fundMetrics.getValues()[SelectionCriteria.UPSIDE_CAPTURE] != null ||
            fundMetrics.getValues()[SelectionCriteria.DOWNSIDE_CAPTURE] != null ||
            fundMetrics.getValues()[SelectionCriteria.BETA] != null  ||
            fundMetrics.getValues()[SelectionCriteria.TOTAL_RETURN_5YEAR] != null ||
           fundMetrics.getValues()[SelectionCriteria.TOTAL_RETURN_10YEAR] != null;
	}
	
	/**
	 * If all the other criteria are 0 and only AIC != 0 return true
	 * @param criteria
	 * @return boolean
	 */
	private static boolean isOnlyAICSelected( LinkedHashMap<Integer, Integer> criteria) {
		int result = 0;
		// SelectionCriteria is subtracted by 4 to ignore the 1yr, 5yr and 10yr
		// criteria's. Once the 1yr, 5yr and 10yr are implemented this should be
		// changed as (SelectionCriteria.TOTAL)-1
		for(Entry<Integer, Integer> i :  criteria.entrySet()){
			if (i.getKey() == SelectionCriteria.FEES) continue;
			result += i.getValue();
		}
	
		return result == 0 && criteria.get(SelectionCriteria.FEES) != 0;
	}
	
	
	/**
	 * This method populates keys for rankedFundsByAssetClassID map using asset class IDs
	 * 
	 * @param sessionContext
	 * @throws SystemException
	 */
	private void populateSelectedAssetClasses(SessionContext sessionContext) throws SystemException {
        try {
            CoreToolHelper coreToolHelper = new CoreToolHelper();
            String[] selectedACs = coreToolHelper.getSelectedAssetClasses(sessionContext.isSelectAll(), sessionContext.isIncludeGIFLSelectFunds());

            if (selectedACs != null) {
                for (int i = 0; i < selectedACs.length; i++) {
                    String ac = selectedACs[i];
                    selectedAssetClasses.put(ac, "");
                    this.rankedFundsByAssetClassID.put(ac, new ArrayList<PercentileRankedFund>());
                }
            }
        } catch (Throwable e) {
            logger.debug("Problem occured while populating selected Asset classes" + e);
            throw new SystemException(e, "populateSelectedAssetClasses : Problem occured while populating selected Asset classes "+e.getMessage());
        }
    }
	
	
	/**
	 * This method sorts funds so that tool can pick top ranked funds. This sorting is not used for display on UI
	 * @param assetClass
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
    private void sortRankedFundsVector(String assetClass) throws SystemException{
		try{
    		List<PercentileRankedFund> rankedFundsList = getRankedFundsByAssetClassID(assetClass);
    //      enforcing a comparator for Primary sort on Rank(descending) and Secondary sort on FundName(asc)
    		//The sorting here is to set ToolRecommended (for Top ranked fund), but not for display
            Collections.sort(rankedFundsList, new FundComparator());//enforcing a comparator for Primary sort on Rank(desc) and Sec sort on FundName(asc)
        }
        catch (Throwable e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Problem occured while Sorting Funds by Rank, Sort#" + e);
             }
            throw new SystemException(e, "sortRankedFundsVector : Problem occured while Sorting Funds by Rank, Sort# "+e.getMessage());
        }
	}
    
	/**
     * Comparator using Primarily on Percentile Rank(big decimal)- descending, then on sort number - ascending
     */
    public static class FundComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {  
            PercentileRankedFund prf0 = (PercentileRankedFund)arg0;
            PercentileRankedFund prf1 = (PercentileRankedFund)arg1;
    
            BigDecimal order0 = prf0.getRank();
            BigDecimal order1 = prf1.getRank();
            if (order0.compareTo(order1) < 0) {
                return 1;
            } else if (order0.compareTo(order1) > 0) {
                return -1;
            } else {//sort # is not required here - because this is only for establishing top ranked and tool selection
                int sortNumber0 = prf0.getSortNumber();
                int sortNumber1 = prf1.getSortNumber();
                if (sortNumber0 < sortNumber1) {
                    return -1;
                } else if (sortNumber0 > sortNumber1) {
                    return 1;
                } else return 1;//keeping this else statement because Home-state has bad data. 2 sort #s can never be same, but AFH and BGA have same sort # (299) in home-state. Production data is fine
            }
        }
    }
}
