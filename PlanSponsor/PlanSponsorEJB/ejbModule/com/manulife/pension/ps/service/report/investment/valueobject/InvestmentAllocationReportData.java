package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.manulife.pension.ps.service.report.investment.reporthandler.InvestmentAllocationReportHandler;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class InvestmentAllocationReportData extends ReportData  implements Serializable{

	public static final String REPORT_ID = InvestmentAllocationReportHandler.class.getName();
	
	public static final String REPORT_NAME = "investmentAllocationReport";
	
	public static final String CSV_REPORT_NAME = "ContractInvestmentAllocation";

	public static final String FILTER_CONTRACT_NO = "contractNumber";
    public static final String FILTER_FUND_ID = "fundId";
    public static final String FILTER_MARKETING_SORT_ORDER = "marketingSortOrder";
	public static final String FILTER_ASOFDATE_REPORT = "asOfDateReport";
	public static final String FILTER_ASOFDATE_DETAILS = "asOfDateDetails";
	public static final String FILTER_CURRENTDATE = "currentDate";
	public static final String FILTER_ISPBA = "ispba";
	public static final String FILTER_SITE = "site";
	public static final String FILTER_VIEW_OPTION = "viewOption";
	public static final String FILTER_ORGANIZING_OPTION = "organizingOption";
	
	public static final String SORT_OPTION = "option";

	public static final String SORT_ASCENDING = "sortAscending";
	public static final String SORT_DESCENDING = "sortDescending";

	private TreeMap map = new TreeMap();
	private ArrayList list = new ArrayList();
	private ContractDatesVO contractDates;
    private int numberOfFundsSelected;
    private boolean jhiIndicatorFlg;

	public InvestmentAllocationReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	public ArrayList getAllocationTotals(){
		return list;
	}

	public SortedMap getAllocationDetails(){
		return (SortedMap)map;
	}
	
	public void sort(String sortField, String sortOrder){
		TreeMap newmap = (TreeMap)getSortedAllocationDetails(sortField, sortOrder);
		this.map = newmap;
	}
	
   /**
 	* Given a numeric sortable field and a sort order,
 	* sort the funds within each fund category.
	*
 	* @param sortableField
 	*	The name of the sortable field. The allowable values
 	*	are:
 	*    	- AllocationDetails.PARTICIPANTS_INVESTED_CURRENT
 	* 		- AllocationDetails.EMPLOYEE_ASSETS
 	* 		- AllocationDetails.EMPLOYER_ASSETS
 	* 		- AllocationDetails.TOTAL_ASSETS
 	* 		- AllocationDetails.PERCENTAGE_OF_TOTAL
 	* 		- AllocationDetails.FUND_NAME
 	*  		- AllocationDetails.MARKETING_SORT_ORDER
 	*       - AllocationDetails.FUND_CLASSS
	*
 	* @param sortOrder
 	*	The order in which the funds are to be sorted. The
 	* 	allowable values are:
 	* 		- SORT_ASCENDING
 	* 		- SORT_DESCENDING
 	*
 	* @return
 	* 	Return a SortedMap with the sorted funds. Returns null
 	* 	if the sortField is invalid.
	*
 	**/
	public SortedMap getSortedAllocationDetails(String sortField, String sortOrder) {
		TreeMap newMap = new TreeMap();
		if(!AllocationDetails.validateSortableField(sortField))
			return null;		//TODO: Throw an exception here

		Set categoryList = map.keySet();
		Iterator categoryIt = categoryList.iterator();
		while(categoryIt.hasNext()) {
			FundCategory fundCat = (FundCategory)categoryIt.next();
			ArrayList allocDetailList = (ArrayList)map.get(fundCat);
			if(AllocationDetails.FUND_NAME.equals(sortField)
					|| AllocationDetails.FUND_CLASS.equals(sortField)){
				//Perform String sort
				map.put(fundCat, sortString(allocDetailList, sortField, sortOrder));
			} else {
				//Perform Numeric sort
				map.put(fundCat,sortNumeric(allocDetailList, sortField, sortOrder));
			}
		}
		return (SortedMap)map;

	}

	private ArrayList sortString(ArrayList allocDetailList, String sortField, String sortOrder) {
		AllocationDetails tempDetail = null;
		for(int i=0; i<allocDetailList.size(); i++) {
			for (int j=i+1; j<allocDetailList.size(); j++) {
				AllocationDetails firstDetail = (AllocationDetails)allocDetailList.get(i);
				AllocationDetails secondDetail = (AllocationDetails)allocDetailList.get(j);
				String firstVal = null;
				String secondVal = null;
				
				if(AllocationDetails.FUND_NAME.equals(sortField)){
					firstVal = firstDetail.getFundName();
					secondVal = secondDetail.getFundName();
				} else {
					//FUND_CLASS column
					firstVal = firstDetail.getFundClass();
					secondVal = secondDetail.getFundClass();
				}

				if(SORT_ASCENDING.equals(sortOrder)) {
					if(firstVal.compareTo(secondVal) > 0) {
						allocDetailList.remove(i);
						allocDetailList.add(i, secondDetail);
						allocDetailList.remove(j);
						allocDetailList.add(j, firstDetail);
					}
				} else {
					if(firstVal.compareTo(secondVal) < 0) {
						allocDetailList.remove(i);
						allocDetailList.add(i, secondDetail);
						allocDetailList.remove(j);
						allocDetailList.add(j, firstDetail);
					}
				}
			}
		}
		return allocDetailList;
	}

	private ArrayList sortNumeric(ArrayList allocDetailList, String sortField, String sortOrder) {
		AllocationDetails tempDetail = null;
		for(int i=0; i<allocDetailList.size(); i++) {
			for (int j=i+1; j<allocDetailList.size(); j++) {
				AllocationDetails firstDetail = (AllocationDetails)allocDetailList.get(i);
				AllocationDetails secondDetail = (AllocationDetails)allocDetailList.get(j);
				double firstVal = firstDetail.getSortableValue(sortField);
				double secondVal = secondDetail.getSortableValue(sortField);

				if(SORT_ASCENDING.equals(sortOrder)) {
					if(firstVal>secondVal) {
						allocDetailList.remove(i);
						allocDetailList.add(i, secondDetail);
						allocDetailList.remove(j);
						allocDetailList.add(j, firstDetail);
					}
				} else {
					if(firstVal<secondVal) {
						allocDetailList.remove(i);
						allocDetailList.add(i, secondDetail);
						allocDetailList.remove(j);
						allocDetailList.add(j, firstDetail);
					}
				}
			}
		}
		return allocDetailList;
	}


	public ContractDatesVO getContractDates(){
		return contractDates;
	}

	public void setContractDates(ContractDatesVO dates){
		contractDates = dates;
	}

	public void addAllocationDetails(FundCategory catCode, AllocationDetails details){
		ArrayList alist = (ArrayList)map.get(catCode);
		if(alist == null){
			alist = new ArrayList();
		}
		alist.add(details);
		map.put(catCode,alist);
	}

	public void addAllocationTotal(AllocationTotals totals){
		/*if(	(totals.getNumberOfOptions()!=0) ||
			(totals.getParticipantsInvested()!=0) ||
			(totals.getEmployeeAssets()!=0) ||
			(totals.getEmployerAssets()!=0) ||
			(totals.getTotalAssets()!=0) ||
			(totals.getPercentageOfTotal()!=0))
		{	*/
			list.add(totals);
			/*
			if( totals.getFundCategoryType().equals(FundCategory.LIFESTYLE) ) list.add(2,totals);
			if( totals.getFundCategoryType().equals(FundCategory.NON_LIFESTYLE) ) list.add(1,totals);
			if( totals.getFundCategoryType().equals(FundCategory.PBA) ) list.add(3,totals);
			*/
		//}
	}

	public int getNumberOfFundsSelected() {
        return numberOfFundsSelected;
    }

    public void setNumberOfFundsSelected(int numberOfFundsSelected) {
        this.numberOfFundsSelected = numberOfFundsSelected;
    }

    /**
	 * @return the jhiIndicatorFlg
	 */
	public boolean isJhiIndicatorFlg() {
		return jhiIndicatorFlg;
	}

	/**
	 * @param jhiIndicatorFlg the jhiIndicatorFlg to set
	 */
	public void setJhiIndicatorFlg(boolean jhiIndicatorFlg) {
		this.jhiIndicatorFlg = jhiIndicatorFlg;
	}

	public String toString() {

		return map + "\n" + list + "\n";
	}
}
