package com.manulife.pension.ps.web.investment;
/*
  File: ViewFundPresentation.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/


import java.math.BigDecimal;

import com.manulife.pension.service.account.valueobject.HypotheticalInfo;
import com.manulife.pension.service.account.valueobject.ViewFund;

/**
 * This class is the presentation value object for ViewFundPresentation.
 * 
 * Copied from ezk
 * 
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.web.investment.ViewFundPresentation
 **/

public class ViewFundPresentation implements Comparable{
	
	private ViewFund fund;
	
	/**
     * Constructor creates the presentation object for the ViewFund value object.
     * 
     * @param fundgr
     * 		ViewFundGroup
     */	
	public ViewFundPresentation(ViewFund fd){
		super();
		this.fund = fd;
	}
	/**
	 * Sets the view fund value object for the presentation object
	 * @param ViewFund 
	 */
	public ViewFund getViewFund(){
		return fund;
	}
	
	public int compareTo(Object obj) {
        ViewFund that = ((ViewFundPresentation) obj).getViewFund();
        
		return this.getViewFund().compareTo(that);
	}

	/**
	 * Gets the fund manager name
	 * @return Returns a String
	 */
	public String getFundManagerName() {
    	return fund.getManagerName();
    }
    /**
	 * Gets the footnote symbols
	 * @return Returns an array of Strings
	 */        
    public String[] getFundFootnoteSymbols() {
    	return fund.getFootnoteSymbols();
    }
    /**
	 * Gets the unit value
	 * @return Returns BigDecimal
	 */   
       
    public BigDecimal getUnitValue() {
    	
    	if (fund.getUnitValue() != null) {
	    	return formatDecimalTruncated(fund.getUnitValue(),2);
    	}
       	return null;	
    }
    /**
	 * Gets the daily variance
	 * @return Returns BigDecimal
	 */
      
    public BigDecimal getDailyVariance() {
    	
    	if (fund.getDailyVariance() != null) {
    		return formatDecimalTruncated(fund.getDailyVariance(),2);
    	}
       	return null;	
    }
    /**
	 * Gets the daily return
	 * @return Returns BigDecimal
	 */
        
    public BigDecimal getDailyReturn() {
    	
    	if (fund.getDailyReturn() != null) {
    		return formatDecimalTruncated(fund.getDailyReturn(),2);
    	}
       	return null;	
    }
    
    /**
	 * Gets the one month return
	 * @return Returns BigDecimal
	 */
    
    public BigDecimal getOneMonthReturn() {
    	
    	if (fund.getOneMonthReturn() != null) {
    		return formatDecimalTruncated(fund.getOneMonthReturn(),2);
    	}
       	return null;	
    }
    /**
	 * Gets the three month return
	 * @return Returns BigDecimal
	 */
        
    public BigDecimal getThreeMonthReturn() {
    	
    	if (fund.getThreeMonthReturn() != null) {
    		return formatDecimalTruncated(fund.getThreeMonthReturn(),2);
    	}
       	return null;	
    }
    /**
	 * Gets the year to date return
	 * @return Returns BigDecimal
	 */
    
    public BigDecimal getYearToDateReturn() {
    	
    	if (fund.getYearToDateReturn() != null) {
    		return formatDecimalTruncated(fund.getYearToDateReturn(),2);
    	 }
       	return null;		
    }
    /**
	 * Gets the one year return
	 * @return Returns BigDecimal
	 */
        
    public BigDecimal getOneYearReturn() {
    	
    	if (fund.getOneYearReturn() != null) {
    		return formatDecimalTruncated(fund.getOneYearReturn(),2);
    	}
       	return null;	
    }
    
    /**
	 * Gets the three year return
	 * @return Returns BigDecimal
	 */
    
    public BigDecimal getThreeYearReturn() {
    	
		if (fund.getThreeYearReturn() != null) {   
    		return formatDecimalTruncated(fund.getThreeYearReturn(),2);
   		}
       	return null;	
    }
    
    /**
	 * Gets the five year return
	 * @return Returns BigDecimal
	 */
    
	public BigDecimal getFiveYearReturn() {
		
		if (fund.getFiveYearReturn() != null) {   
			return formatDecimalTruncated(fund.getFiveYearReturn(),2);
		}
       	return null;	
	}

	/**
	 * Gets the ten year return
	 * @return Returns BigDecimal
	 */
	public BigDecimal getTenYearReturn() {
		
		if (fund.getTenYearReturn() != null) {   
			return formatDecimalTruncated(fund.getTenYearReturn(),2);
		}
       	return null;	
	}

	/**
	 * Gets the annual investment charge return
	 * @return Returns BigDecimal
	 */

	public BigDecimal getAnnualInvestmentCharge() {
		
		if (fund.getAnnualInvestmentCharge() != null) {  
			return formatDecimalTruncated(fund.getAnnualInvestmentCharge(),2);
		}
       	return null;	
	}

	/**
	 * Gets the morning star category
	 * @return Returns String
	 */
	public String getMorningstarCategory() {
		return fund.getMorningstarCategory();
	}

	/**
	 * Gets the average expense ratio
	 * @return Returns BigDecimal
	 */

	public BigDecimal getAverageExpenseRatio() {
		
		if (fund.getAverageExpenseRatio() != null) {  
			return formatDecimalTruncated(fund.getAverageExpenseRatio(),2);
		}
       	return null;				
	}
	
	/**
	 * Gets the since inception
	 * @return
	 */
	public BigDecimal getSinceInceptionReturn(){
		if (fund.getSinceInception() != null) {  
			return formatDecimalTruncated(fund.getSinceInception(),2);
		}
       	return null;
	}
	
	 /**
	 * Gets the Quarterly one year return
	 * @return Returns BigDecimal
	 */
        
    public BigDecimal getOneYearQuarterlyReturn() {
    	
    	if (fund.getOneYearQuarterReturn() != null) {
    		return formatDecimalTruncated(fund.getOneYearQuarterReturn(),2);
    	}
       	return null;	
    }
    
    /**
	 * Gets the Quarterly three year return
	 * @return Returns BigDecimal
	 */
    
    public BigDecimal getThreeYearQuarterlyReturn() {
    	
		if (fund.getThreeYearQuarterReturn() != null) {   
    		return formatDecimalTruncated(fund.getThreeYearQuarterReturn(),2);
   		}
       	return null;	
    }
    
    /**
	 * Gets the Quarterly five year return
	 * @return Returns BigDecimal
	 */
    
	public BigDecimal getFiveYearQuarterlyReturn() {
		
		if (fund.getFiveYearQuarterReturn() != null) {   
			return formatDecimalTruncated(fund.getFiveYearQuarterReturn(),2);
		}
       	return null;	
	}

	/**
	 * Gets the Quarterly ten year return
	 * @return Returns BigDecimal
	 */
	public BigDecimal getTenYearQuarterlyReturn() {
		
		if (fund.getTenYearQuarterReturn() != null) {   
			return formatDecimalTruncated(fund.getTenYearQuarterReturn(),2);
		}
       	return null;	
	}

	/**
	 * Gets the Quarterly since inception
	 * @return
	 */
	public BigDecimal getSinceInceptionQuarterlyReturn(){
		if (fund.getSinceInceptionQuarter() != null) {  
			return formatDecimalTruncated(fund.getSinceInceptionQuarter(),2);
		}
       	return null;
	}
	
	/**
	 * Gets the Fund Expenses Ratio - FER
	 * @return
	 */
	public BigDecimal getFundExpenseRatio(){
		if (fund.getFundExpenseRatio() != null) {  
			return formatDecimalTruncated(fund.getFundExpenseRatio(),2);
		}
       	return null;
	}

	
	/**
	 * Gets the Administrator Maintenance Charge - AMC
	 * @return
	 */
	public BigDecimal getAdminMaintenanceCharge(){
		if (fund.getAdminMaintenanceCharge() != null) {  
			return formatDecimalTruncated(fund.getAdminMaintenanceCharge(),2);
		}
       	return null;
	}

	
	/**
	 * Gets the Sales and Service Fee
	 * @return
	 */
	public BigDecimal getSalesAndServiceFee(){
		if (fund.getSalesAndServiceFee() != null) {						
			return formatDecimalTruncated( fund.getSalesAndServiceFee(),2);
	}
   	return fund.getSalesAndServiceFee();
	}

	

	/**
	 * Gets the package flag
	 * @return Returns boolean
	 */

	public boolean getPackageFlag() {
		return fund.getPackageFlag();
	}
	/**
	 * Gets fund code
	 * @return Returns String
	 */

	public String getFundId() {
   		return fund.getId();
   	}
    
    /**
	 * Gets fund name
	 * @return Returns String
	 */
    public String getFundName() {
    	return fund.getName();
    }
    /**
	 * Gets fund type
	 * @return Returns String
	 */
      
    public String getFundType() {
    	return fund.getType();
    }
    /**
	 * Gets fund rateType
	 * @return Returns String
	 */
      
    public String getRateType() {
    	return fund.getRatetype();
    }  
    
 	/**
	 * Certain contracts exclude funds from the list of available funds.
	 * These unselected funds are not excluded from the list of funds; 
	 * instead they're marked with a <code>selectedFlag = false</code>
	 * because even the unselected funds are displayed on the TPA site.
	 * Thus, we assume that the decision to display or not display 
	 * unselected funds is a presentation responsibility.
	 * 
	 * @return true if the fund is available in the contract, false otherwise.
	 */
	public boolean getSelectedFlag() {
		return fund.getSelectedFlag();
	}


	public boolean getInParticipantHoldings() {
		if (fund.getInParticipantHoldings() != null){
			return fund.getInParticipantHoldings().booleanValue();
		}
		return false;
	}	
	
	/**
	 * This method was created in VisualAge.
	 * @return java.lang.String
	 * @param bigDecimal BigDecimal
	 * @param precision int
	 */
	public static BigDecimal formatDecimalTruncated(BigDecimal bigDecimal, int precision) {
		BigDecimal bd = bigDecimal.setScale(precision, BigDecimal.ROUND_DOWN);
		return bd;
}
	
	/**
	 * Gets Hypothetical Information
	 * @return Returns String
	 */
	
	public HypotheticalInfo getHypotheticalInfo(){
		if(fund.getHypotheticalInfo()!= null){
			return fund.getHypotheticalInfo();
		}
		return fund.getHypotheticalInfo();
	}

	/**
	 * Gets FundDisclosure Text for Money market.
	 * @return Returns String
	 */
	public String getFundDisclosureText(){
		if(fund.getFundDisclosureText() != null){
			return fund.getFundDisclosureText();
		}
		return fund.getFundDisclosureText();
	}
}



