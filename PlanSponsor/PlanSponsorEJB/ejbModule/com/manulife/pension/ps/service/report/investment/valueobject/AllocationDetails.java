package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;

public class AllocationDetails implements Serializable{
	
	private FundCategory fundCategory;
	
	private String fundName;
	private String fundId;
	private int marketingOrder;
	private String fundType;
	private String rateType;
	private int participantsInvestedCurrent;
	private int participantsInvestedFuture;
	private double employeeAssets;
	private double employerAssets;
	private double totalAssets;
	private double percentageOfTotal;
	private String fundClass;
	private String tickerSymbol;
	
	//The following fields are sortable
	public final static String FUND_NAME = "fundName";
	public final static String MARKETING_SORT_ORDER = "marketingSortOrder";
	public final static String PARTICIPANTS_INVESTED_CURRENT = "participantsInvestedCurrent";
	public final static String EMPLOYEE_ASSETS = "employeeAssets";
	public final static String EMPLOYER_ASSETS = "employerAssets";
	public final static String TOTAL_ASSETS = "totalAssets";
	public final static String PERCENTAGE_OF_TOTAL = "percentageOfTotal";
	public final static String FUND_CLASS = "fundClass";
	
	
	public AllocationDetails() {}
	
	public AllocationDetails(String fundName, String fundId, String fundType, int investedCurrVal, 
		int investedFutVal,	double employeeAssets, double employerAssets, 
		double totalAssets, double percentageOfTotal, String tickerSymbol) {
		this.fundName = fundName;
		this.fundId = fundId;
		this.fundType = fundType;
		this.participantsInvestedCurrent = investedCurrVal;
		this.participantsInvestedFuture = investedFutVal;
		this.employeeAssets = employeeAssets;
		this.employerAssets = employerAssets;
		this.totalAssets = totalAssets;
		this.percentageOfTotal = percentageOfTotal;
	}
	public AllocationDetails(String fundName, String fundId, String fundType, int investedCurrVal, 
			int investedFutVal,	double employeeAssets, double employerAssets, 
			double totalAssets, double percentageOfTotal) {
			this.fundName = fundName;
			this.fundId = fundId;
			this.fundType = fundType;
			this.participantsInvestedCurrent = investedCurrVal;
			this.participantsInvestedFuture = investedFutVal;
			this.employeeAssets = employeeAssets;
			this.employerAssets = employerAssets;
			this.totalAssets = totalAssets;
			this.percentageOfTotal = percentageOfTotal;
		}
			
	public String getFundName(){
		return fundName;
	}
	
	public void setFundName(String value){
		fundName = value;
	}
	
	public String getFundId(){
		return fundId;
	}
	
	public void setFundId(String value){
		fundId = value;
	}

	public String getFundType(){
		return fundType;
	}
	
	public void setFundType(String value){
		fundType = value;
	}
	
	public int getParticipantsInvestedCurrent(){
		return participantsInvestedCurrent;
	}
	
	public void setParticipantsInvestedCurrent(int value){
		participantsInvestedCurrent = value;
	}
	
	public int getParticipantsInvestedFuture(){
		return participantsInvestedFuture;
	}
	
	public void setParticipantsInvestedFuture(int value){
		participantsInvestedFuture = value;
	}
	
	public double getEmployeeAssets(){
		return employeeAssets;
	}
	
	public void setEmployeeAssets(double value){
		employeeAssets = value;
	}
	
	public double getEmployerAssets(){
		return employerAssets;
	}
	
	public void setEmployerAssets(double value){
		employerAssets = value;
	}
	
	public double getTotalAssets(){
		return totalAssets;
	}
	
	public void setTotalAssets(double value){
		totalAssets = value;
	}
	
	public double getPercentageOfTotal(){
		return percentageOfTotal;
	}
	
	public void setPercentageOfTotal(double value){
		percentageOfTotal = value;
	}
	
	public FundCategory getFundCategory(){
		return fundCategory;
	}
	
	public void setFundCategory(FundCategory value){
		fundCategory = value;
	}
	
	public String getTickerSymbol() {
		return tickerSymbol;
	}

	public void setTickerSymbol(String tickerSymbol) {
		this.tickerSymbol = tickerSymbol;
	}

	public String toString() {
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("Fund Name = ").append(fundName);
		strBuff.append("; Fund Id = ").append(fundId);
		strBuff.append("; Fund sort order = ").append(marketingOrder);
		strBuff.append("; Fund Type = ").append(fundType);
		strBuff.append("; Part. Invested Current = ").append(participantsInvestedCurrent);
		strBuff.append("; Part. Invested Future = ").append(participantsInvestedFuture);
		strBuff.append("; Employee Assets = ").append(employeeAssets);
		strBuff.append("; Employer Assets = ").append(employerAssets);
		strBuff.append("; Total Assets = ").append(totalAssets);
		strBuff.append("; percentageOfTotal = ").append(percentageOfTotal);
		strBuff.append("; fundClass = ").append(fundClass);
		strBuff.append("; tickerSymbol = ").append(tickerSymbol);
		return strBuff.toString();
	}
	
	
	/**
 	* Given a numeric sortable field, this function will return 
 	* the value for that sortable field. This function is
 	* used by the sorting routine in InvestmentAllocationReportData.
	* 
 	* @param sortableField
 	*	The name of the sortable field. The allowable values 
 	* 	are:
 	*    	- PARTICIPANTS_INVESTED_CURRENT
 	* 		- EMPLOYEE_ASSETS
 	* 		- EMPLOYER_ASSETS
 	* 		- TOTAL_ASSETS
 	* 		- PERCENTAGE_OF_TOTAL
 	* 		- MARKETING_SORT_ORDER
 	*       - FUND_CLASS
	*
 	* @return
 	*     The value of the field. If the sortableField does not exist
 	* 	  return -99;
	*
 	**/
	protected double getSortableValue(String sortableField) {
		if(PARTICIPANTS_INVESTED_CURRENT.equals(sortableField))
			return participantsInvestedCurrent;
		else if(EMPLOYEE_ASSETS.equals(sortableField))
			return employeeAssets;
		else if(EMPLOYER_ASSETS.equals(sortableField))
			return employerAssets;
		else if(TOTAL_ASSETS.equals(sortableField))
			return totalAssets;
		else if(MARKETING_SORT_ORDER.equals(sortableField))
			return marketingOrder;
		else if(PERCENTAGE_OF_TOTAL.equals(sortableField))
			return percentageOfTotal;
		else
			return -99;		//TODO: Throw an exception here!!
	}
	
	/**
 	* Given a numeric sortable field, this function will check 
 	* if the field in valid.
	* 
 	* @param sortableField
 	*	The name of the sortable field. The allowable values 
 	* 	are:
 	*    	- PARTICIPANTS_INVESTED_CURRENT
 	* 		- EMPLOYEE_ASSETS
 	* 		- EMPLOYER_ASSETS
 	* 		- TOTAL_ASSETS
 	* 		- PERCENTAGE_OF_TOTAL
 	* 		- MARKETING_SORT_ORDER
 	*       - FUND_CLASS
	*
 	* @return
 	*     Return true if the sortable field is valid. Or
 	* 	  else return false.
	*
 	**/
	protected static boolean validateSortableField(String sortableField) {
		if(PARTICIPANTS_INVESTED_CURRENT.equals(sortableField) ||
			EMPLOYEE_ASSETS.equals(sortableField) ||
			EMPLOYER_ASSETS.equals(sortableField) ||
			TOTAL_ASSETS.equals(sortableField) ||
			PERCENTAGE_OF_TOTAL.equals(sortableField) ||
			MARKETING_SORT_ORDER.equals(sortableField) ||
			FUND_NAME.equals(sortableField) ||
			FUND_CLASS.equals(sortableField))
			return true;
		else
			return false;
	}
	/**
	 * Gets the marketingOrder
	 * @return Returns a String
	 */
	public int getMarketingOrder() {
		return marketingOrder;
	}
	/**
	 * Sets the marketingOrder
	 * @param marketingOrder The marketingOrder to set
	 */
	public void setMarketingOrder(int marketingOrder) {
		this.marketingOrder = marketingOrder;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getRateType() {
		return rateType;
	}

	/**
	 * @return the fundClass
	 */
	public String getFundClass() {
		return fundClass;
	}

	/**
	 * @param fundClass the fundClass to set
	 */
	public void setFundClass(String fundClass) {
		this.fundClass = fundClass;
	}
	
}

