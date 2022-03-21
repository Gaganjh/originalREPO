package com.manulife.pension.ps.service.report.transaction.reporthandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsItem;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.fund.valueobject.RiskCategoryVO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.util.log.ServiceLogRecord;
public abstract class AbstractTransactionReportHandler implements ReportHandler {
	
	private ServiceLogRecord record = new ServiceLogRecord("ReportHandler");

	private static final Logger logger = Logger.getLogger(AbstractTransactionReportHandler.class);	

	 
	protected List populateFundGroupInfo(List transactionItems,
				Date transactionDate) throws SystemException {

		RiskCategoryVO[] allRiskFundGroups = null;
		allRiskFundGroups = FundInfoCache.getAllRiskCategories();  // Contains all of the risk categories available to everyone, in order
		HashMap allFundGroups = new HashMap();				
		// Create a hash map containing ALL risk groups, in order
		for (int i=0; i < allRiskFundGroups.length; i++) {
			RiskCategoryVO currentGroup = allRiskFundGroups[i];
			FundGroup fundGroup = new FundGroup();
			fundGroup.setBackgroundColorCode(currentGroup.getBackgroundColor());
			fundGroup.setForegroundColorCode(currentGroup.getFontColor());
			fundGroup.setPresentationOrder(new Integer(currentGroup.getOrder()).intValue());
			fundGroup.setGroupName(currentGroup.getRiskCategoryName());
			allFundGroups.put(currentGroup.getOrder().trim(), fundGroup);
		}
		        
        Map<String, SortedMap<String, List<Fund>>> fundGroupsByRiskCategoryCode = 
            new HashMap<String, SortedMap<String, List<Fund>>>();
        
        boolean nullMoneyTypeDescription = isNullMoneyTypeDescription(transactionItems);
        
        // Iterate and group funds by RiskCategoryCode
		for (int i=0; i < transactionItems.size(); i++) {
			TransactionDetailsItem transactionItem = (TransactionDetailsItem) transactionItems.get(i);				
			String fundId = transactionItem.getFundId();		  // Fund id of the current item in the history.
			FundVO fundInfo = FundInfoCache.getFundData(fundId);  // Fund data for current item in history

			if (fundInfo == null) {
				String errorMsg = "Couldn't find matching fund in product for fund ID: " + fundId;
				logger.error(errorMsg);
				throw new EJBException(errorMsg);
			}
			
			String itemRiskCategory = transactionItem.getRiskCategoryCode();
			if (itemRiskCategory != null) {
                if(nullMoneyTypeDescription) {
                    itemRiskCategory=itemRiskCategory.trim();
                    FundGroup groupToAddTo = (FundGroup)allFundGroups.get(itemRiskCategory);
                    if (groupToAddTo != null) {
                        groupToAddTo.addFund(createFundValueObject(fundInfo, transactionItem));
                    }
                
                } else {
                    addFundToGroup(itemRiskCategory.trim(), createFundValueObject(fundInfo, transactionItem), fundGroupsByRiskCategoryCode);
                }
			}
		}
        
        // Add funds to groups based on RiskCategoryCode
        if(!nullMoneyTypeDescription) {
            populateFundsInGroups(allFundGroups, fundGroupsByRiskCategoryCode);
        }
        
		Iterator keyIterator = allFundGroups.keySet().iterator();
		ArrayList removeList = new ArrayList();
		while (keyIterator.hasNext()) {
			String key = (String)keyIterator.next();
			if ( ((FundGroup)allFundGroups.get(key)).getFunds().length <= 0 ) {
				removeList.add(key);				
			}
		}
		
		for (int i=0; i < removeList.size(); i++) {
			String removeKey = (String)removeList.get(i);
			allFundGroups.remove(removeKey);
		}
		
		FundGroup [] groups = getSortedAccountBalanceFundGroups(allFundGroups);
		
		List newList = new ArrayList();
		
		for (int i=0; i < groups.length; i++) {
			newList.add(groups[i]);
		}		
		return newList;
	}

    private boolean isNullMoneyTypeDescription(List transactionItems) {        
        if(transactionItems == null || transactionItems.size() < 1) {
            return true;
        }
        
        boolean isNull = false;
        
        // Iterate and group funds by RiskCategoryCode
        for (int i=0; i < transactionItems.size(); i++) {  
          TransactionDetailsItem transactionItem = (TransactionDetailsItem) transactionItems.get(i);
          
          if(transactionItem == null || transactionItem.getMoneyTypeDescription() == null) {
              isNull = true;
              break;
          }
        }
        
        return isNull;
    }
    
    /**
     * Adds elements to sorted maps keyed by risk category code
     * The maps are going to be ordered by MoneyTypeDescription for each fund in the 
     * natural sort order i.e. alphabeticaly
     * 
     * @param riskCategoryCode
     * @param detail
     * @param groupsByRiskCategoryCode
     */
	private void addFundToGroup(String riskCategoryCode, TransactionDetailsFund detail, 
            Map<String, SortedMap<String, List<Fund>>> groupsByRiskCategoryCode) {
        // Nothing to be done here
        if(detail == null) return;
        
        // It is possible to have multiple funds with the same MoneyTypeDescription
        SortedMap<String, List<Fund>> sortedMap = groupsByRiskCategoryCode.get(riskCategoryCode);
        
        // Create sorted map if it does not exist
        if(sortedMap == null) {
            sortedMap = new TreeMap<String, List<Fund>>();
            groupsByRiskCategoryCode.put(riskCategoryCode, sortedMap);
        }
        
        if(sortedMap.containsKey(detail.getMoneyTypeDescription())) {
            List<Fund> list = sortedMap.get(detail.getMoneyTypeDescription());
            list.add(detail);
        } else {
            List<Fund> list = new ArrayList<Fund>();
            list.add(detail);
            sortedMap.put(detail.getMoneyTypeDescription(), list);
        }
    }
		
    /**
     * Adds all the funds for a FundGroup identified by riskCategoryCode
     * 
     * @param allFundGroups
     * @param fundGroupsByRiskCategoryCode
     */
    private void populateFundsInGroups(HashMap allFundGroups, Map<String, SortedMap<String, List<Fund>>> fundGroupsByRiskCategoryCode) {
      Set<String> riskCategoryCodes = fundGroupsByRiskCategoryCode.keySet();
        
      for (String riskCategoryCode : riskCategoryCodes) {
          FundGroup groupToAddTo = (FundGroup)allFundGroups.get(riskCategoryCode);
          if (groupToAddTo != null) {              
              Collection<List<Fund>> fundCollection = fundGroupsByRiskCategoryCode.get(riskCategoryCode).values();
              List<Fund> list = new ArrayList<Fund>();
              for (List<Fund> fundList : fundCollection) {
                  list.addAll(fundList);
              }
              Fund[] funds = new Fund[list.size()];
              groupToAddTo.setFunds(list.toArray(funds));
          }
      }
    }
    
	protected TransactionDetailsFund createFundValueObject(FundVO fundOffering, 
			TransactionDetailsItem transactionDetailsItem) {
		
		TransactionDetailsFund transactionFund = new TransactionDetailsFund( 
			fundOffering.getId(),
			fundOffering.getName(),
			fundOffering.getType(),
			transactionDetailsItem.getMoneyTypeDescription(),
			transactionDetailsItem.getAmount(),
			transactionDetailsItem.getPercentage(),
			transactionDetailsItem.getUnitValue(),
			transactionDetailsItem.getNumberOfUnits(),
			transactionDetailsItem.getComments(),
			true,
			fundOffering.getSortNumber(),
			fundOffering.getRiskCategoryCode(),
			null);
		
		transactionFund.setEmployeeAmount(transactionDetailsItem.getEmployeeAmount());
		transactionFund.setEmployeePercentage(transactionDetailsItem.getEmployeePercentage());
		transactionFund.setEmployerAmount(transactionDetailsItem.getEmployerAmount());
		transactionFund.setEmployerPercentage(transactionDetailsItem.getEmployerPercentage());
		
		return transactionFund;
	}
	
	protected FundGroup [] getSortedAccountBalanceFundGroups(HashMap activeFundGroups) {


		FundGroup [] groups = new FundGroup[activeFundGroups.size()];
		
		int arrayIndex = 0;
		
		Iterator iterator = activeFundGroups.values().iterator();		
		while (iterator.hasNext()) {
			groups[arrayIndex] = (FundGroup) iterator.next();
			Arrays.sort(groups[arrayIndex].getFunds());
			arrayIndex++;
		}
		
		Arrays.sort(groups);			
		return groups;
	}
	
	protected String getFundName(String fundId) throws SystemException {
		
		FundVO fundInfo = FundInfoCache.getFundData(fundId);

		if (fundInfo == null) {
			String errorMsg = "Couldn't find matching fund in product for fund ID: " + fundId;
					logger.error(errorMsg);
					throw new EJBException(errorMsg);
		}
		
		return fundInfo.getName();
	}
	

	/**
	 * This version accepts a 4th parameter. The 'duration' is the milliseconds the 'methodname' took to complete its work.
	 * Used to ensure we can persist the duration a method took to complete.
	 */
	protected void logInteraction(CustomerServicePrincipal principal, String methodName, String data, int duration) 
	{
		try 
		{
			ServiceLogRecord record = (ServiceLogRecord) this.record.clone();
			record.setMethodName(methodName);
			record.setData(data);
			record.setDate(new Date());
			record.setPrincipalName(principal.getName());
			record.setCode(duration);
		} 
		catch (CloneNotSupportedException e) 
		{
			AbstractTransactionReportHandler.logger.error(e);
		}
	}

	/**
	 * Returns the difference between a start time and end time in milliseconds
	 * 
	 * @param long startTime - the time in milliseconds when the method started
	 * 
	 * @return int - the duration in milliseconds of the method execution
	 */
	protected int calculateMethodDuration(long startTime)
	{
		return (int) (System.currentTimeMillis() - startTime);
	}


}