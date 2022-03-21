package com.manulife.pension.bd.web.fundEvaluator.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolConstants;
import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.bd.web.fundEvaluator.common.FundForInvOption;
import com.manulife.pension.bd.web.fundEvaluator.session.SessionContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.AssetClass;
import com.manulife.pension.service.fund.valueobject.ContractFund;
import com.manulife.pension.service.fund.valueobject.FundOffering;
import com.manulife.pension.service.fund.valueobject.InvestmentGroup;


/**
 * Helper class to CoreToolProcessor and CoreToolSessionData classes
 * @author PWakode
 */
public class CoreToolHelper implements CoreToolConstants{
    
    private static final Logger logger = Logger.getLogger(CoreToolHelper.class);
    
    private static String[] includedRiskCategories = {RISK_CAT_LCF, RISK_CAT_LSF, RISK_CAT_AGGRESSIVE_GROWTH, RISK_CAT_GROWTH, RISK_CAT_GROWTH_INCOME, RISK_CAT_INCOME, RISK_CAT_CONSERVATIVE};
    private static ArrayList<String> includedRiskCategoriesList = new ArrayList<String>(Arrays.asList(includedRiskCategories));
    
    private static String[] ignoredAssetClasses = {ASSET_CLASS_ID_STABLE_VALUE, ASSET_CLASS_ID_BLANK};
    private static ArrayList<String> ignoredAssetClassesList = new ArrayList<String>(Arrays.asList(ignoredAssetClasses));
    
    /**
     * This method returns map of funds which are never to be pre-selected by tool. 
     * Generally all SVF competing funds (with exception of Guaranteed funds) makeup this list
     * 
     * @return - Map - key is fund id and value is fund type
     * @throws SystemException 
     */
    public HashMap<String, String> getAdditionalFundsNotToBeSelected(String svfInvestmentId) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAdditionalFundsNotToBeSelected()");
        }
        
        HashMap<String, String> additionalFundsNotToBeSelected = new HashMap<String, String>();
        
        if(StringUtils.isNotBlank(svfInvestmentId)) {
        	 List<String> svfCompetingFundsMap = FundServiceDelegate.getInstance().getListOfSVFCompetingFunds(svfInvestmentId);
             for(String competingFUnd:svfCompetingFundsMap){
             	if(StringUtils.equals(GUARANTEED_FUND_TYPE, competingFUnd)){
             		 additionalFundsNotToBeSelected.put(competingFUnd, competingFUnd);
             	}
             	//CL121786 fix start - exclude Guaranteed account funds 3Yr, 5Yr, 10Yr from this map 
             	if(!StringUtils.equals(THREE_YEAR_GUARANTEED_FUND, competingFUnd) ||
                 		!StringUtils.equals(FIVE_YEAR_GUARANTEED_FUND, competingFUnd) ||
                 		!StringUtils.equals(TEN_YEAR_GUARANTEED_FUND, competingFUnd)){
                 		additionalFundsNotToBeSelected.put(competingFUnd, competingFUnd);
                 }
             	//CL121786 fix end
             }
        }
        
        
   	   	for( String svfFund:CoreToolGlobalData.defaultSelectedStableValueFunds){
    	   		additionalFundsNotToBeSelected.put(svfFund, "");
         }
   		for( String svfFund:CoreToolGlobalData.defaultSelectedMoneyMarketFunds){
	   		additionalFundsNotToBeSelected.put(svfFund, "");
        }
   	   	
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getAdditionalFundsNotToBeSelected()");
        }
        return additionalFundsNotToBeSelected;
    }
    
    /**
     * This method returns list of Fund IDs which will make up the base fund line up
     * 
     * @param SessionContext object - contains all user input from UI
     * @param Hashtable with key as fund id and value as ContractFund object
     * @return - List - Fund IDs
     * @throws SystemException 
     */
    public List<String> getInvestmentIdListForRankCalculation(SessionContext sessionInfo, Hashtable<String, ContractFund> contractFundTable, List<String> giflFundsList) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getInvestmentIdListForRankCalculation()");
        }
        
        List<String> investmentIds = null;
        
        if(StringUtils.isNotBlank(sessionInfo.getContract())) {//if contract is provided
        	
        	if (sessionInfo.isMerrillFirmFilter()) {
                investmentIds = filterMerrillCoveredFunds(contractFundTable);
            } else {
            	investmentIds = getInvestmentIdsForContract(contractFundTable);
            }
            //TODO : find out if Investment IDs need to be filtered out here if not in base class ? (see 2.4 b)
            //If include closed is enabled - we include closed funds which are selected irrespective of their rate type. Hence we should not have to 
            //filter out funds not belonging to contract base class
            
            //Stripping off any GIFL funds the contract may contain in its base funds line up
            List<String> tempInvestmentIds = new ArrayList<String>();
            Iterator<String> itr = investmentIds.iterator();
            while(itr.hasNext()){
                String investmentId = itr.next();
                
                if(!giflFundsList.contains(investmentId)){
                    tempInvestmentIds.add(investmentId);
                }
            }
            investmentIds = tempInvestmentIds;
        }
        else{
            //do this only if Contract not provided
            FundOffering fundOffering = FundServiceDelegate.getInstance().getProduct(sessionInfo.getSiteId(), sessionInfo.getFundMenu(), sessionInfo.isIncludeNML());
            if (fundOffering == null) {
                throw new RuntimeException("Failed to find product for request: " + sessionInfo);
            }
            investmentIds = fundOffering.getInvestmentIds();
            
            // added for IPS
            // remove funds that are pending approval from states
            investmentIds = filterByStateApprovalPending(sessionInfo.getStateCode(), investmentIds);
            
            // added for MV4
            // remove GIFL V3 optional LSPS funds from non-contract view
            investmentIds = filterGiflV3OptionalLSPSFunds(investmentIds);
            
            if (sessionInfo.isExcludeEDJ()) {
                // added for IPS
                // remove funds that are excluded for EDJ
                investmentIds = filterByExcludingEdwardJones(investmentIds);
            }
            
            if (sessionInfo.isMerrillFirmFilter()) {
                investmentIds = filterMerrillCoveredFunds(investmentIds);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getInvestmentIdListForRankCalculation()");
        }
        return investmentIds;
    }
    
    private List<String> filterByStateApprovalPending(String state, List<String> investmentIds)
            throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> filterByStateApprovalPending()");
        }

        List<String> filteredInvestmentIds = new ArrayList<String>();

        ArrayList<String> stateApprovalPendingFundList = 
            FundServiceDelegate.getInstance().getStateApprovalPendingFunds(state);
        
        if (stateApprovalPendingFundList.isEmpty()) {
            filteredInvestmentIds = investmentIds;
        } else {
            Iterator<String> invIdIter = investmentIds.iterator();
            while (invIdIter.hasNext()) {
                String investmentId = invIdIter.next();
                
                if(!stateApprovalPendingFundList.contains(investmentId)){
                    filteredInvestmentIds.add(investmentId);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> filterByStateApprovalPending()");
        }
        return filteredInvestmentIds;
    }
    
    private List<String> filterGiflV3OptionalLSPSFunds(List<String> investmentIds) 
    		throws SystemException {
    	
    	if (logger.isDebugEnabled()) {
            logger.debug("entry -> filterGiflV3OptionalLSPSFunds()");
        }
    	
    	List<String> filteredInvestmentIds = new ArrayList<String>();
    	List<String> excludedFundList = CoreToolGlobalData.giflV3OptionalLSPSFunds;
    	Iterator<String> invIdIter = investmentIds.iterator();
        while (invIdIter.hasNext()) {
            String investmentId = invIdIter.next();
            if(!excludedFundList.contains(investmentId)){
                filteredInvestmentIds.add(investmentId);
            }
        }
    			
    	if (logger.isDebugEnabled()) {
    	    logger.debug("exit -> filterGiflV3OptionalLSPSFunds()");
    	}
    	return filteredInvestmentIds;
    }
    

    private List<String> filterByExcludingEdwardJones(List<String> investmentIds) 
            throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> filterByExcludingEdwardJones()");
        }
        List<String> filteredInvestmentIds = new ArrayList<String>();

        ArrayList<String> excludedFundList = 
            FundServiceDelegate.getInstance().getExcludedFundsForDistChannel(CoreToolConstants.DIST_CHANNEL_EDWARD_JONES);
        
        if (excludedFundList.isEmpty()) {
            filteredInvestmentIds = investmentIds;
        } else {
            Iterator<String> invIdIter = investmentIds.iterator();
            while (invIdIter.hasNext()) {
                String investmentId = invIdIter.next();
                
                if(!excludedFundList.contains(investmentId)){
                    filteredInvestmentIds.add(investmentId);
                }
            }
        }        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> filterByExcludingEdwardJones()");
        }
        return filteredInvestmentIds;        
    }
    
    /**
     * This method returns List of Fund IDs determined as part of base fund line up for contract
     * 
     * @param List  - input list Investment IDs 
     * @param Hashtable with key as fund id and value as ContractFund object
     * @return - List - List of Fund IDs determined as part of base fund line up for contract
     * @throws SystemException 
     */
    private List<String> getInvestmentIdsForContract(Hashtable<String, ContractFund> contractFundTable) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getInvestmentIdsForContract()");
        }
        Enumeration<String> e = contractFundTable.keys();
        ArrayList<String> investmentIdsForContract = new ArrayList<String>();
        while(e.hasMoreElements()){   
            investmentIdsForContract.add(e.nextElement());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getInvestmentIdsForContract()");
        }
        return investmentIdsForContract;
    }
    
    /**
     * This method returns list of Fund id concatenated by rate type for each fund in base fund line up
     * Fund within the same contract can have different rate types, hence if base fund line up is for contract, then rate type
     * is taken from contract. If contract # is not provided, then the class indicated in drop down menu on UI is considered for rate type
     * 
     * @param SessionContext object - contains all user input from UI
     * @param List  - input list Investment IDs 
     * @param Hashtable with key as fund id and value as ContractFund object
     * @return - List - Fund id concatenated by rate type for each fund in base fund line up
     * @throws SystemException 
     */
    public List<String> getInvestmentIdsPlusRateTypeList(SessionContext sessionInfo, List<String> investmentIds, Hashtable<String, ContractFund> contractFundTable) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getInvestmentIdsPlusRateTypeList()");
        }
        
        List<String> investmentIdsPlusRateType = new ArrayList<String>();
        Iterator<String> invIdIter = investmentIds.iterator();
        String rateType = null;
        List<String> jhiFunds = FundServiceDelegate.getInstance().getJHIFunds();
        while (invIdIter.hasNext()) {
            String investmentId = (String) invIdIter.next();
            if(StringUtils.isNotBlank(sessionInfo.getContract())){
                rateType = ((ContractFund)contractFundTable.get( investmentId)).getRateType();
                logger.debug("RATE TYPE for CONTRACT=" + rateType +"investmentId was :"+investmentId);
                investmentIdsPlusRateType.add(investmentId+rateType);
            }
            else{
                rateType = sessionInfo.getClassMenu();
                if( rateType.equals(BDConstants.SIGNATURE_PLUS)){
					
					if(jhiFunds.contains(investmentId)){
					investmentIdsPlusRateType.add(investmentId+rateType);
					}else{
					String classID= FundServiceDelegate.getInstance().getJHIClassID(rateType);
					investmentIdsPlusRateType.add(investmentId+classID);
					}
				}else{
				investmentIdsPlusRateType.add(investmentId+rateType);
				}
                
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getInvestmentIdsPlusRateTypeList()");
        }
        return investmentIdsPlusRateType;
    }
    
    /**
     * This method gets list of asset class IDs which have benchmark metrics available
     * 
     * @return - List - asset class IDs which have benchmark metrics available
     * @throws SystemException 
     */
    public List<String> getAssetClassesWithBenchmarkMetricsAvailableList() throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAssetClassesWithBenchmarkMetricsAvailableList()");
        }
        List<String> assetClassesWithBenchmarkMetricsAvailableList = new ArrayList<String>();
        
        List<String> assetClassIds = new ArrayList<String>();
        Iterator<AssetClass> allAssetClasses = FundServiceDelegate.getInstance().getAllAssetClasses().iterator();
        while (allAssetClasses.hasNext()) {
            AssetClass assetClass = (AssetClass) allAssetClasses.next();
            String assetClassId = assetClass.getAssetClass();
            assetClassIds.add(assetClassId);
        }
        assetClassesWithBenchmarkMetricsAvailableList = FundServiceDelegate.getInstance().getAssetClassesWithBenchmarkMetricsAvailable(assetClassIds);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getAssetClassesWithBenchmarkMetricsAvailableList()");
        }
        return assetClassesWithBenchmarkMetricsAvailableList;
    }
    
    /**
     * This method filters closed funds based on whether base fund line up is for contract or not
     *  
     * @param SessionContext object - contains all user input from UI
     * @param List  - input list Investment IDs 
     * @param Hashtable with key as fund id and value as ContractFund object
     * @return - List - resulting list Investment IDs after filtering
     * @throws SystemException 
     */
    public List<String> filterClosedFundsBasedOnMode(SessionContext sessionInfo, List<String> investmentIds, Hashtable<String, ContractFund> contractFundTable, List<String> investmentIdsPlusRateType) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> filterClosedFundsBasedOnMode()");
        }
        /*
         * If Contract mode & ClosedFunds to be include - (1)Include all funds returned by stored procedure, exclude nothing
         * If Contract mode & ClosedFunds to be excluded(default) - (1)Include the closed funds if they are selectedFlag Y, exclude closed funds if they are selectedFlag N
         * if closed funds are to be excluded && Contract is not provided - straight away strip of all Closed funds, else exclude nothing
         */
        List<String> closedFundList = new ArrayList<String>();
        
        closedFundList = FundServiceDelegate.getInstance().getListOfFundsClosedToNB(investmentIds);
        
        List<String> tempInvestmentIds = new ArrayList<String>();
        Iterator<String> invIdIter = investmentIds.iterator();
        
        if(sessionInfo.isIncludeClosedFunds() && StringUtils.isNotBlank(sessionInfo.getContract())){//include mode and contract is provided, then include all closed funds which (1)have Selected = Y and (2)they belong to same base class
            while (invIdIter.hasNext()) {
                
                String investmentId = invIdIter.next();
                String rateTypeClosedFund = "";
                logger.debug("investmentId ="+investmentId);
                
                if(!closedFundList.contains(investmentId)){//open fund - so add it to base fund line up
                    tempInvestmentIds.add(investmentId);
                }
                else{//its a closed fund(in include mode & in contract mode) - so check it for selected flag & base class
                    if(contractFundTable.get(investmentId).isSelected()){//Its a closed fund, but selectedFlag is yes - hence we include anyway(no need to check base class matches or not)
                        tempInvestmentIds.add(investmentId);
                    }
                    else{//its a closed fund(in include mode & in contract mode) -if not already selected by contract - we still need to add if base class also matches fund id rate type. BEV 41 2 c
                        Iterator<String> itrIdRate = investmentIdsPlusRateType.iterator();
                        while(itrIdRate.hasNext()){
                            String tempString = itrIdRate.next();
                            if(tempString.startsWith(investmentId, 0)){
                               rateTypeClosedFund =  tempString.substring(3);//extracting rate type of Closed fund Id
                               break;
                            }
                        }
                        if(rateTypeClosedFund.equals(sessionInfo.getContractBaseClass())){//include since base class also matches fund id rate type
                            tempInvestmentIds.add(investmentId);
                        }
                        
                    }
                }
            }
            investmentIds = tempInvestmentIds;//revised list after excluding closed funds
        }

        else if(!sessionInfo.isIncludeClosedFunds() && StringUtils.isBlank(sessionInfo.getContract())){//if closed funds are to be excluded && Contract is not provided - straight away strip of Closed funds
              
            //If Contract mode & ClosedFunds to be included - (1)Include all funds returned by stored procedure, exclude nothing
//          If Contract mode & ClosedFunds to be excluded(default) - (1)Include all contract funds(including the closed funds if they are selectedFlag Y), exclude closed funds if they are selectedFlag N
            while (invIdIter.hasNext()) {
                
                String investmentId = invIdIter.next();
                logger.debug("investmentId ="+investmentId);
                
                if(!closedFundList.contains(investmentId)){
                    tempInvestmentIds.add(investmentId);
                }
            }
            investmentIds = tempInvestmentIds;//revised list after excluding closed funds
        }
        else if(!sessionInfo.isIncludeClosedFunds() && StringUtils.isNotBlank(sessionInfo.getContract())){//if closed funds are to be excluded && Contract is provided - still include closed funds selected by contract
            while (invIdIter.hasNext()) {
                String investmentId = invIdIter.next();
                
                logger.debug("investmentId ="+investmentId);
                if(!closedFundList.contains(investmentId)){//open fund
                    tempInvestmentIds.add(investmentId);
                }
                else{//its a closed fund(in exclude mode & in contract mode) - so check it for selected flag
                    if(contractFundTable.get(investmentId).isSelected()){//Its a closed fund, but selectedFlag is yes - hence we include anyway
                        tempInvestmentIds.add(investmentId);
                    }
                }
            }
            investmentIds = tempInvestmentIds;//revised list after excluding closed funds
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> filterClosedFundsBasedOnMode()");
        }
        return investmentIds;
    }
    
    /**
     * This method returns funds available to the contract (base fund line up for Contract) 
     * @param - int - contract id
     * @return - Hash table with key as fund id and value as ContractFund object
     * @throws SystemException 
     */
    public Hashtable<String, ContractFund> getContractFunds(int contractId) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getContractFunds()");
        }
        
        Hashtable<String, ContractFund> contractFundTable = FundServiceDelegate.getInstance().getContractFunds(contractId);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getContractFunds()");
        }
        return contractFundTable;
    }
    
    /**
     * This method checks if any of contracts selected funds are Money Market funds 
     * @param selectSvfSuites 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    public boolean isContractHavingAnyMoneyMarketFunds(Hashtable<String, ContractFund> contractFundTable, Map<String, Boolean> contractSelectedMMFFunds) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnyMoneyMarketFunds()");
        }
        for(ContractFund contractFund : contractFundTable.values()){
        	if (StringUtils.equals(CoreToolConstants.MMF, contractFund.getFundFamilyCategoryCode())
					&& contractFund.isSelected()) {
        		contractSelectedMMFFunds.put(contractFund.getInvestmentId(),true);
        	}
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnyMoneyMarketFunds()");
        }
        return contractSelectedMMFFunds.isEmpty() ? false:true;
    }
    
    /**
     * This method checks if any of contracts selected funds are Stable value funds 
     * @param selectSvfSuites 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    public boolean isContractHavingAnyStableValueFunds(Hashtable<String, ContractFund> contractFundTable, List<String> contractSelectedSVFFunds) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnyStableValueFunds()");
        }
        
        for(ContractFund contractFund : contractFundTable.values()){
        	if (StringUtils.equals(CoreToolConstants.SVF, contractFund.getFundFamilyCategoryCode())
					&& contractFund.isSelected()) {
        		contractSelectedSVFFunds.add(contractFund.getInvestmentId());
        	}
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnyStableValueFunds()");
        }
        return contractSelectedSVFFunds.size()> 0 ? true:false;
    }
    
    /**
     * This method checks if any of contracts selected funds are SVF competing funds 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    public boolean isContractHavingAnySVFCompetingFunds(Hashtable<String, ContractFund> contractFundTable) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnySVFCompetingFunds()");
        }
        boolean isContractHavingAnySVFCompetingFunds = false;
        List<String> svfCompetingFundList = new ArrayList<String>();
        
        Iterator<String> itr = FundServiceDelegate.getInstance().getStableValueFunds().keySet().iterator();//key is fund id, value is type
        
        while (itr.hasNext()){
            svfCompetingFundList.add(itr.next());
        }
        
        Enumeration<String> e = contractFundTable.keys();
        String fundIdKey = null;
        while(e.hasMoreElements()){   
            fundIdKey = e.nextElement();
            if(svfCompetingFundList.contains(fundIdKey)){
                if(contractFundTable.get( fundIdKey).isSelected()){
                    isContractHavingAnySVFCompetingFunds = true;
                    break;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnySVFCompetingFunds()");
        }
        return isContractHavingAnySVFCompetingFunds;
    }
    
    /**
     * This method checks if any of contracts selected funds are from "LifeStyle" or LSF asset class 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    public boolean isContractHavingAnyLifeStyleFunds(Hashtable<String, ContractFund> contractFundTable) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnyLifeStyleFunds()");
        }
        boolean isContractHavingAnyLifeStyleFunds = false;
        List<String> lifeStyleFundList = new ArrayList<String>();
        
        lifeStyleFundList = FundServiceDelegate.getInstance().getAllLifeStyleFundList();
        
        Enumeration<String> e = contractFundTable.keys();
        String fundIdKey = null;
        while(e.hasMoreElements()){   
            fundIdKey = e.nextElement();
            if(lifeStyleFundList.contains(fundIdKey)){
                if(contractFundTable.get( fundIdKey).isSelected()){
                    isContractHavingAnyLifeStyleFunds = true;
                    break;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnyLifeStyleFunds()");
        }
        return isContractHavingAnyLifeStyleFunds;
    }
    
    /**
     * This method checks if any of contracts selected funds are from "LifeCycle" or LCF asset class 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    
    public boolean isContractHavingAnyLifeCycleFunds(Hashtable<String, ContractFund> contractFundTable) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnyLifeCycleFunds()");
        }
        boolean isContractHavingAnyLifeCycleFunds = false;
        List<String> lifeCycleFundList = new ArrayList<String>();
        
        lifeCycleFundList = FundServiceDelegate.getInstance().getAllLifeCycleFundList();
        
        Enumeration<String> e = contractFundTable.keys();
        String fundIdKey = null;
        while(e.hasMoreElements()){   
            fundIdKey = e.nextElement();
            if(lifeCycleFundList.contains(fundIdKey)){
                if(contractFundTable.get( fundIdKey).isSelected()){
                    isContractHavingAnyLifeCycleFunds = true;
                    break;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnyLifeCycleFunds()");
        }
        return isContractHavingAnyLifeCycleFunds;
    }
    
    /**
     * This method checks if any of contracts selected funds are from "High Quality Short Term Fixed Income" or FXS asset class 
     * @param - Hashtable  of contract funds
     * @return - boolean
     * @throws SystemException 
     */
    public boolean isContractHavingAnyHQSFunds(Hashtable<String,ContractFund> contractFundTable) throws SystemException{
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isContractHavingAnyHQSFunds()");
        }
        boolean isContractHavingAnyHQSFunds = false;
        List<String> hqsFundList = new ArrayList<String>();
        
        hqsFundList = FundServiceDelegate.getInstance().getHQSFundList();
        
        Enumeration<String> e = contractFundTable.keys();
        String fundIdKey = null;
        while(e.hasMoreElements()){   
            fundIdKey = e.nextElement();
            if(hqsFundList.contains(fundIdKey)){
                if(contractFundTable.get( fundIdKey).isSelected()){
                    isContractHavingAnyHQSFunds = true;
                    break;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isContractHavingAnyHQSFunds()");
        }
        return isContractHavingAnyHQSFunds;
    }
    
    /**
     * This method converts Percentile to 'Display Rank' for display purposes only. 
     * @param - BigDecimal -Percentile Ranking 
     * @return - String
     */
    public String convertPercentileToRankForDisplay(BigDecimal percentileValue){
        BigDecimal tempRank;
        int displayRank = 0;
        
        if ( percentileValue.compareTo(HUNDRED) >= 0)  {
            displayRank = ONE;
        } else {
            tempRank = HUNDRED.subtract(percentileValue);
            displayRank = tempRank.setScale(0,BigDecimal.ROUND_CEILING).intValue();//setting scale to zero and rounding up
        }
        return Integer.toString(displayRank);
    }
    
    /**
     * This method organizes fund display sequence (keeps Index funds at bottom of list)
     * @return - List of fund objects
     */
    @SuppressWarnings("unchecked")
    public List<FundForInvOption> organizeFundDisplaySequence(List<FundForInvOption> sortedFunds, String assetClassId, String order){
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> organizeFundDisplaySequence()");
        }
        logger.debug("#### #### Processor Impl Before sorting");
        logger.debug(sortedFunds);
        
        Iterator<FundForInvOption> itr = sortedFunds.iterator();
        List<FundForInvOption> tempFundList = new ArrayList<FundForInvOption>();
        if(!assetClassId.equals(ASSET_CLASS_ID_INDEX )){//identify Index funds from non IDX asset classes
            while(itr.hasNext()){//create separate list of Index funds
                FundForInvOption tempFund = itr.next();
                if(tempFund.isIndex()){
                    tempFundList.add(tempFund);
                }
            }
        }
        Iterator<FundForInvOption> itrTempListToRemove = tempFundList.iterator();
        while(itrTempListToRemove.hasNext()){//remove Index funds before sorting
            FundForInvOption tempFund = itrTempListToRemove.next();
            sortedFunds.remove(tempFund);
        }
        logger.debug("#### #### Removed list before sort");
        logger.debug(tempFundList);
        
        if((order != null) && order.equals(REVERSE_ORDER)){
            Collections.sort(sortedFunds, new FundComparatorReverseOrder());
        }
        else{
            Collections.sort(sortedFunds, new FundComparator());
        }
        
        Iterator<FundForInvOption> itrTempList = tempFundList.iterator();
        while(itrTempList.hasNext()){//again add all sorted IDX funds back to original list
            FundForInvOption tempFund = itrTempList.next();
            sortedFunds.add(tempFund);
        }
        logger.debug("#### #### Processor Impl After sorting");
        logger.debug(sortedFunds);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> organizeFundDisplaySequence()");
        }
        
        return sortedFunds;
    }

    /**
     * This method returns hash table object with order as key, Investment category name as value
     * @return - Hash table object- order as key, InvestmentGroup as value (filtered with only required Investment categories)
     * @throws SystemException 
     */
    public Hashtable<Integer, InvestmentGroup> getInvestmentGroupTable(boolean isIncludeGIFLSelectFunds) throws SystemException{
        Hashtable<Integer, InvestmentGroup> invCat = new Hashtable<Integer, InvestmentGroup>();
        
        Map<Integer, InvestmentGroup> investmentGroupMap = FundServiceDelegate.getInstance().getInvestmentGroupTable();
        Iterator<Integer> itrMap = investmentGroupMap.keySet().iterator();
        while(itrMap.hasNext()){
            int order = itrMap.next();
            String investmentGroupName = investmentGroupMap.get(order).getGroupname().trim();
            //GIFL P3c
            // GIFL Select funds should be included based on user selection in Step1
            // Personal Brokerage & Market Indexes will continue to be suppressed
            if(includedRiskCategoriesList.contains(investmentGroupName) || (isIncludeGIFLSelectFunds? RISK_CAT_LSG.equals(investmentGroupName) : false)){
                invCat.put(order, investmentGroupMap.get(order));
            }
        }
        return invCat;
    }
    
    /**
     * This method returns selected Asset classes string array (excludes LCF & LSF as they are dealt with using another user preference) depending on user selection in 'narrow your list' page
     * 
     * @param boolean selectAll
     * @return - String[] - contains Asset class IDs
     * @throws SystemException 
     */
    public String[] getSelectedAssetClasses(boolean selectAll, boolean includeGIFLSelectFunds) throws SystemException{
        
        ArrayList<String> selectedAssetClassesAL = new ArrayList<String>();
        String[] selectedAssetClasses = new String[0];
        
        if(selectAll){
            Map<String, AssetClass> allClassesMap = FundServiceDelegate.getInstance().getAssetClasses();
            Iterator<String> it = allClassesMap.keySet().iterator();
            
            while (it.hasNext()) {
                String key = (it.next()).trim();
                if(!ignoredAssetClassesList.contains(key) && (includeGIFLSelectFunds? true : !ASSET_CLASS_ID_GIFL.equals(key))){
                    if(!key.equals(ASSET_CLASS_ID_LIFECYCLE) && !key.equals(ASSET_CLASS_ID_LIFESTYLE)){
                        selectedAssetClassesAL.add(key);
                    }
                }
            }
            selectedAssetClasses = selectedAssetClassesAL.toArray(new String[0]);
            return selectedAssetClasses;
        }
        else{//start from scratch option
            return null;
        }
    }
    
    /**
     * Comparator using Primarily on Percentile Rank(big decimal)- descending, then on sort number - ascending
     */
    @SuppressWarnings("unchecked")
    public static class FundComparator implements Comparator {
        public int compare(Object arg0, Object arg1) {
            FundForInvOption fundInvOption0 = (FundForInvOption)arg0;
            FundForInvOption fundInvOption1 = (FundForInvOption)arg1;
    
            BigDecimal order0 = fundInvOption0.getRank();
            BigDecimal order1 = fundInvOption1.getRank();
            if (order0.compareTo(order1) < 0) {
                return 1;
            } else if (order0.compareTo(order1) > 0) {
                return -1;
            } else {//sort # is not required here - because this is only for establishing top ranked and tool selection
                int sortNumber0 = fundInvOption0.getFund().getSortNumber();
                int sortNumber1 = fundInvOption1.getFund().getSortNumber();
                if (sortNumber0 < sortNumber1) {
                    return -1;
                } else if (sortNumber0 > sortNumber1) {
                    return 1;
                } else {//if rank is same, just do a string comparison based on fund name
                    return fundInvOption0.getFund().getFundName().compareTo(fundInvOption1.getFund().getFundName());
                }
            }
        }
    }
    
    /**
     * Comparator using Primarily sort number - descending, then on Percentile Rank(big decimal)- ascending
     */
    @SuppressWarnings("unchecked")
    public static class FundComparatorReverseOrder implements Comparator {
        public int compare(Object arg0, Object arg1) {
            FundForInvOption fundInvOption0 = (FundForInvOption)arg0;
            FundForInvOption fundInvOption1 = (FundForInvOption)arg1;
    
            BigDecimal order0 = fundInvOption0.getRank();
            BigDecimal order1 = fundInvOption1.getRank();
            if (order0.compareTo(order1) < 0) {
                return -1;
            } else if (order0.compareTo(order1) > 0) {
                return 1;
            } else {//sort # is not required here - because this is only for establishing top ranked and tool selection
                int sortNumber0 = fundInvOption0.getFund().getSortNumber();
                int sortNumber1 = fundInvOption1.getFund().getSortNumber();
                if (sortNumber0 < sortNumber1) {
                    return 1;
                } else if (sortNumber0 > sortNumber1) {
                    return -1;
                } else {//if rank is same, just do a string comparison based on fund name
                    return fundInvOption0.getFund().getFundName().compareTo(fundInvOption1.getFund().getFundName());
                }
            }
        }
    }
    
	/**
	 * filter fund to include only ones that are either Merrill covered or
	 * selected by contract.
	 * 
	 * @param contractFundTable
	 * @return List<String>
	 * @throws SystemException
	 */
	private List<String> filterMerrillCoveredFunds(Hashtable<String, ContractFund> contractFundTable)
			throws SystemException {
		Set<String> coveredFundList = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
		ArrayList<String> investmentIdsForContract = new ArrayList<String>();
		for (Entry<String, ContractFund> entry : contractFundTable.entrySet()) {
			if (entry.getValue().isSelected() || coveredFundList.contains(entry.getKey())) {
				investmentIdsForContract.add(entry.getKey());
			}
		}
		return investmentIdsForContract;
	}

	/**
	 * filter fund to include only ones that are Merrill covered 
	 * 
	 * @param investmentIds
	 * @return List<String>
	 * @throws SystemException
	 */
	private List<String> filterMerrillCoveredFunds(List<String> investmentIds) throws SystemException {
		List<String> filteredInvestmentIds = new ArrayList<String>();
		Set<String> coveredFundList = FundServiceDelegate.getInstance().getMerrillCoveredFundInvestmentIds();
		for (String investmentId : investmentIds) {
			if (coveredFundList.contains(investmentId)) {
				filteredInvestmentIds.add(investmentId);
			}
		}
		return filteredInvestmentIds;
	}

}
    
    
    

