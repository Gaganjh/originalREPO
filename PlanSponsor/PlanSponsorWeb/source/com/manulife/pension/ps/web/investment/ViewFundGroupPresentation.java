package com.manulife.pension.ps.web.investment;
/*
  File: ViewFundGroupPresentation.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.account.valueobject.HypotheticalInfo;
import com.manulife.pension.service.account.valueobject.ViewFund;

/**
 * This class is the presentation value object for ViewFundGroupPresentation.
 * 
 * Copied from ezk
 * 
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.web.investment.ViewFundGroupPresentation
 **/

public class ViewFundGroupPresentation implements Comparable, Serializable{
	
	private int presentationOrder = Integer.MIN_VALUE;
	private String groupName = null;
    private String backgroundColorCode = null;
    private String foregroundColorCode = null;
    private FundGroup fundgroupvalue;
    private ViewFund[] funds;
    
    private Set footnotes = new HashSet();
        
    /** 
	 * Default constructor
	 */
    public ViewFundGroupPresentation() {
    	super();
    }
    /**
     * Constructor creates the presentation object for the FundGroup value object.
     * 
     * @param fundgr
     * 		FundGroup
     */	
	public ViewFundGroupPresentation(FundGroup fundgr) {
		super();
		setFundGroup(fundgr);
    	    		
	}
	/**
	 * Sets the view fund group value object for the presentation object
	 * @param ViewFundGroup 
	 */

	public void setFundGroup(FundGroup fundgr){
		this.fundgroupvalue = fundgr;
		setBackgroundColorCode(fundgroupvalue.getBackgroundColorCode());
    	setForegroundColorCode(fundgroupvalue.getForegroundColorCode());
    	setGroupName(fundgroupvalue.getGroupName());
    	setPresentationOrder(fundgroupvalue.getPresentationOrder());
    	
    	Fund[] funds = fundgroupvalue.getFunds();
    	for (int i=0, length=(funds != null?funds.length:0);
    		i < length; i++) {
    		ViewFund fundelement = (ViewFund) funds[i];
    		addFund(fundelement);
    	}
	}
	/**
	 * Gets the view fund group value object
	 * @return Returns a ViewFundGroup
	 */
	public FundGroup getFundGroup() {
		return fundgroupvalue;
	}
	
	/**
	 * Gets the set of footnotes symbols
	 * @return Returns a Set
	 */
	public Set getFootNotes() {
		
		return footnotes;
	}
		
	
	/**
	 * Gets the foregroundColorCode
	 * @return Returns a String
	 */
	public String getForegroundColorCode() {
		return this.foregroundColorCode;
	}
	/**
	 * Sets the foregroundColorCode
	 * @param foregroundColorCode String
	 */
	public void setForegroundColorCode(String foregroundColorCode) {
		this.foregroundColorCode = foregroundColorCode;
	}

	/**
	 * Gets the backgroundColorCode
	 * @return Returns a String
	 */
	public String getBackgroundColorCode() {
		return this.backgroundColorCode;
	}
	/**
	 * Sets the backgroundColorCode
	 * @param backgroundColorCode String
	 */
	public void setBackgroundColorCode(String backgroundColorCode) {
		this.backgroundColorCode = backgroundColorCode;
	}


	/**
	 * Gets the groupName
	 * @return Returns a String
	 */
	public String getGroupName() {
		return this.groupName;
	}
	
	/**
	 * Sets the groupName
	 * @param groupName String
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * returns the order in which the groups are to be displayed.
	 * @return Returns a int
	 */
	public int getPresentationOrder() {
		return presentationOrder;
	}
	/**
	 * Sets the order
	 * @param order The order to set
	 */
	public void setPresentationOrder(int order) {
		this.presentationOrder = order;
	}
	/**
	 * Compare this object to annother of the same type, 
	 * by returning the difference between the order attributes
	 * @param order The order to set
	 * @return int	the difference between the order attributes
	 * 				of the two objects
	 */	
	public int compareTo(Object obj) {
        ViewFundGroupPresentation that = (ViewFundGroupPresentation) obj;
		return this.getPresentationOrder() - that.getPresentationOrder();
	}
	
	/**
	 * Gets list of default sorted funds.
	 * The sort order is specified in the database
	 * @return Returns a List
	 */
	protected ViewFund[] getFunds() {
		Collections.sort(Arrays.asList(funds));
		return this.funds;
	}
	
	/**
	 * Gets list of default sorted funds.
	 * The sort order is specified in the database
	 * @return Returns a List
	 */
	public Object[] getSortedFunds() {

		if (funds == null){
			return null;
		}
		
		Object[] result = new Object[funds.length];
		for (int i = 0; i < funds.length; i++){
			Vector element = new Vector();
			element.add(funds[i].getName());
			element.add(funds[i].getId());
			element.add(funds[i].getManagerName());
			element.add(funds[i].getUnitValue());
			element.add(funds[i].getDailyVariance());
			element.add(funds[i].getDailyReturn());
			element.add(funds[i].getOneMonthReturn());
			element.add(funds[i].getThreeMonthReturn());
			element.add(funds[i].getYearToDateReturn());
			element.add(funds[i].getOneYearReturn());
			element.add(funds[i].getThreeYearReturn());
			element.add(funds[i].getFiveYearReturn());
			element.add(funds[i].getTenYearReturn());
			element.add(funds[i].getSinceInception());
			element.add(funds[i].getFundExpenseRatio());
			element.add(funds[i].getAdminMaintenanceCharge());
			element.add(funds[i].getSalesAndServiceFee());
			element.add(funds[i].getAnnualInvestmentCharge());
			element.add(funds[i].getMorningstarCategory());
			element.add(funds[i].getInParticipantHoldings());
			element.add(funds[i].getFootnoteSymbols());
			element.add(funds[i].getSelectedFlag()?"true":"false");
			element.add(funds[i].getType());
			element.add(funds[i].getRatetype());
			element.add(funds[i].getOneYearQuarterReturn());
			element.add(funds[i].getThreeYearQuarterReturn());
			element.add(funds[i].getFiveYearQuarterReturn());
			element.add(funds[i].getTenYearQuarterReturn());
			element.add(funds[i].getSinceInceptionQuarter());
			element.add(funds[i].getFundDisclosureText());
			element.add(funds[i].getHypotheticalInfo());
			
			result[i] = element;
		}

		return result;
	}
	
	public boolean setSortedViewFunds(Vector orderFunds){

		for (int i = 0; i < orderFunds.size(); i++) {
			Vector element = (Vector)orderFunds.get(i);
			funds[i].setName((String)element.get(0));
			funds[i].setId((String)element.get(1));
			funds[i].setManagerName((String)element.get(2));
			funds[i].setUnitValue((BigDecimal)element.get(3));
			funds[i].setDailyVariance((BigDecimal)element.get(4));
			funds[i].setDailyReturn((BigDecimal)element.get(5));
			funds[i].setOneMonthReturn((BigDecimal)element.get(6));
			funds[i].setThreeMonthReturn((BigDecimal)element.get(7));
			funds[i].setYearToDateReturn((BigDecimal)element.get(8));
			funds[i].setOneYearReturn((BigDecimal)element.get(9));
			funds[i].setThreeYearReturn((BigDecimal)element.get(10));
			funds[i].setFiveYearReturn((BigDecimal)element.get(11));
			funds[i].setTenYearReturn((BigDecimal)element.get(12));
			funds[i].setSinceInception((BigDecimal)element.get(13));
			funds[i].setFundExpenseRatio((BigDecimal)element.get(14));
			funds[i].setAdminMaintenanceCharge((BigDecimal)element.get(15));
			funds[i].setSalesAndServiceFee((BigDecimal)element.get(16));
			funds[i].setAnnualInvestmentCharge((BigDecimal)element.get(17));
			funds[i].setMorningstarCategory((String)element.get(18));
			funds[i].setInParticipantHoldings((Boolean)element.get(19));
			funds[i].setFootnoteSymbols((String[])element.get(20));
			funds[i].setSelectedFlag(Boolean.valueOf(element.get(21).toString()).booleanValue());
			funds[i].setType((String)element.get(22));
			funds[i].setRatetype((String)element.get(23));
			funds[i].setOneYearQuarterReturn((BigDecimal)element.get(24));
			funds[i].setThreeYearQuarterReturn((BigDecimal)element.get(25));
			funds[i].setFiveYearQuarterReturn((BigDecimal)element.get(26));
			funds[i].setTenYearQuarterReturn((BigDecimal)element.get(27));
			funds[i].setSinceInceptionQuarter((BigDecimal)element.get(28));
			funds[i].setFundDisclosureText((String)element.get(29));
			funds[i].setHypotheticalInfo((HypotheticalInfo)element.get(30));
		}
		return true;
	}
	
	public ViewFundPresentation[] getViewFunds() {

		if (funds == null)
			return null;
		ViewFundPresentation[] presentation = new ViewFundPresentation[funds.length];
		for (int i = 0; i< funds.length; i++){
			presentation[i] = new ViewFundPresentation(funds[i]);
		}
		return presentation;
	}
	/**
	 * Add individual funds
	 * @param fund FundValueObject
	 */
	public void addFund(ViewFund fund) {
		BigDecimal dailyVariance = fund.getDailyVariance(); 
		BigDecimal dailyReturn = fund.getDailyReturn();
		BigDecimal oneMonthReturn = fund.getOneMonthReturn();
		BigDecimal threeMonthReturn = fund.getThreeMonthReturn();
	    BigDecimal yearToDateReturn = fund.getYearToDateReturn();
		BigDecimal oneYearReturn = fund.getOneYearReturn();
		BigDecimal threeYearReturn = fund.getThreeYearReturn();
		BigDecimal fiveYearReturn = fund.getFiveYearReturn();
		BigDecimal tenYearReturn = fund.getTenYearReturn();
		
		if (DataUtility.isZeroValue(tenYearReturn)){
			fund.setTenYearReturn(null);
								
			if (DataUtility.isZeroValue(fiveYearReturn)){
				fund.setFiveYearReturn(null);
							
				if (DataUtility.isZeroValue(threeYearReturn)){
					fund.setThreeYearReturn(null);
										
					if (DataUtility.isZeroValue(oneYearReturn)){
						fund.setOneYearReturn(null);
											
						if (DataUtility.isZeroValue(yearToDateReturn)){
							fund.setYearToDateReturn(null);
												
							if (DataUtility.isZeroValue(threeMonthReturn)){
								fund.setThreeMonthReturn(null);
													
								if (DataUtility.isZeroValue(oneMonthReturn)){
									fund.setOneMonthReturn(null);
														
									if (DataUtility.isZeroValue(dailyReturn)){
										fund.setDailyReturn(null);
															
										if (DataUtility.isZeroValue(dailyVariance)) {
											fund.setDailyVariance(null);
										}
															
									}
				
								}
							}
						}
					} 
				} 
			} 
		} 

		
		Vector list = new Vector();
		if (funds != null) {
			list.addAll(Arrays.asList(funds));
			
		}
		list.add(fund);
		
		if (fund.getFootnoteSymbols() != null)
    			footnotes.addAll(Arrays.asList(fund.getFootnoteSymbols()));
		
		Object[] allFunds = list.toArray();
		this.funds = new ViewFund[allFunds.length];
		for (int i = 0; i < allFunds.length; i++)
			funds[i] = (ViewFund) allFunds[i];
		
	}

	/**
	 * Add individual funds
	 * @param fund FundValueObject
	 */
	public void addFunds(ViewFund[] funds) {
		for (int i = 0, length = (funds == null) ? 0 : funds.length; i < length; i++) {
			addFund(funds[i]);
		}
	}

}

