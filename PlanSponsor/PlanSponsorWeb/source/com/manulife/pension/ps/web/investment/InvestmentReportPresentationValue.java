package com.manulife.pension.ps.web.investment;

/*
  File: InvestmentReportPresentationValue.java

  Version   Date         Author           Change Description
  -------   ----------   --------------   ------------------------------------------------------------------
  CS1.0     2004-01-01   Chris Shin       Initial version.
*/

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.manulife.pension.ps.web.investment.util.SelectionSorter;
import com.manulife.pension.ps.web.investment.util.SortTool;
import com.manulife.pension.ps.web.investment.util.Sorter;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.FundGroup;
import com.manulife.pension.service.account.valueobject.InvestmentReport;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class is the presentation value object for InvestmentReportValueObject .
 * 
 * Copied from ezk
 * 
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 * @see      com.manulife.pension.ezk.web.investment.InvestmentReportValueObject
 **/

public class InvestmentReportPresentationValue implements Serializable{
	
	private String unitValueEffectiveDate = null;
	private String ferEffectiveDate = null;
	private String qeEffectiveDate = null;
    private String rateOfReturnEffectiveDate = null;
    private String annualInvestmentChargeEffectiveDate = null;
	
	private Set footnotes = new HashSet();
	
	private String contractNumber = null;
	private int groupingScheme = 0;
	private boolean isBrokerDealer = false;
	
	private ViewFundGroupPresentation[] fundGroups;
	private InvestmentReport investmentValueObject;
	
	// static values for sorting 
 	public static final boolean SORT_DESCENDING = true;
   	public static final boolean SORT_ASCENDING = false;
   	public static final boolean SHOW_AVAILABLE = true;
   
   	private int columnnumber = 99;
   	private boolean sorttype;
   	private int assetRiskOrderBy;
 
   	private String sortOrder;  
   	private boolean showAvailableFunds;
   	
   	private List<String> feeWaiverFunds;
	private Map<String, com.manulife.pension.service.fund.valueobject.Fund> restrictedFunds;

	/**
     * Default constructor 
     * 
     */	
	public InvestmentReportPresentationValue() {
		super();
	}
	
	/**
     * Constructor creates the presentation object for the InvestmentReport
     * 
     * @param valobj
     * 		InvestmentReport
     */	

	public InvestmentReportPresentationValue(InvestmentReport valobj) {
		super();
		this.setInvestmentReportValueObject(valobj);
	}
	
	/**
	 * Gets the investment report value object
	 * @return Returns a InvestmentReportValueObject
	 */
	public InvestmentReport getInvestmentReportValueObject() {
    	return this.investmentValueObject;
    }

    /**
	 * Sets the investment report value object for the presentation object
	 * @param InvestmentReport 
	 */
    public void setInvestmentReportValueObject(InvestmentReport valobj) {
    	this.investmentValueObject = valobj;
		if (valobj != null){

			//The default format for Date is "MMMM d, yyyy"
			setAnnualInvestmentChargeEffectiveDate(getFormattedDate(valobj.getAnnualInvestmentChargeEffectiveDate()));
    		setContractNumber(valobj.getContractNumber());
    		setRateOfReturnEffectiveDate(getFormattedDate(valobj.getRateOfReturnEffectiveDate()));
    		setUnitValueEffectiveDate(getFormattedDate(valobj.getUnitValueEffectiveDate()));
    		setFerEffectiveDate(getFormattedDate(valobj.getFerEffectiveDate()));
    		setGroupingScheme(valobj.getGroupingScheme());
    		setIsBrokerDealer(valobj.isBrokerDealer());
    	
    		FundGroup[] fundGroups = valobj.getFundGroups();
    		
    		for (int i=0, length = (fundGroups != null?fundGroups.length:0);
    			i < length; i++) {
    			FundGroup fundgroupvalue = (FundGroup) fundGroups[i];
    			Fund[] funds = fundgroupvalue.getFunds();
    			if ((funds != null) && (funds.length > 0)){
    				ViewFundGroupPresentation fundgroup = new ViewFundGroupPresentation(fundgroupvalue);
    				this.addFundGroup(fundgroup);
    			}
       		    		
    		}
    		
    	}
    }
	/**
	 * Gets the contract number
	 * @return Returns a String
	 */
	public String getContractNumber() {
    	return this.contractNumber;
    }
    /**
	 * Sets the contract number
	 * @param contractNumber The contract number to set
	 */
    public void setContractNumber(String contractNumber) {
    	this.contractNumber = contractNumber;
    }
    /**
	 * Gets the unit value effective date
	 * @return Returns a String
	 */
    public String getUnitValueEffectiveDate() {
    	return this.unitValueEffectiveDate;
    }
    /**
	 * Sets the unit value effective date
	 * @param String unitValueEffectiveDate 
	 */
    public void setUnitValueEffectiveDate(String unitValueEffectiveDate) {
    	this.unitValueEffectiveDate = unitValueEffectiveDate;
    }
    /**
	 * @return the ferEffectiveDate
	 */
	public String getFerEffectiveDate() {
		return ferEffectiveDate;
	}

	/**
	 * @param ferEffectiveDate the ferEffectiveDate to set
	 */
	public void setFerEffectiveDate(String ferEffectiveDate) {
		this.ferEffectiveDate = ferEffectiveDate;
	}

	/**
	 * @return the qeEffectiveDate
	 */
	public String getQeEffectiveDate() {
		return qeEffectiveDate;
	}

	/**
	 * @param qeEffectiveDate the qeEffectiveDate to set
	 */
	public void setQeEffectiveDate(String qeEffectiveDate) {
		this.qeEffectiveDate = qeEffectiveDate;
	}

	/**
	 * Gets the rate of return effective date
	 * @return Returns a String
	 */
    public String getRateOfReturnEffectiveDate() {
    	return this.rateOfReturnEffectiveDate;
    }
    /**
	 * Sets the rate of return effective date
	 * @param String rateOfReturnEffectiveDate
	 */
    public void setRateOfReturnEffectiveDate(String rateOfReturnEffectiveDate) {
    	this.rateOfReturnEffectiveDate = rateOfReturnEffectiveDate;
    }
    /**
	 * Gets the annual investment charge effective date
	 * @return Returns a String
	 */
    public String getAnnualInvestmentChargeEffectiveDate() {
    	return this.annualInvestmentChargeEffectiveDate;
    }
    /**
	 * Sets the annual investment charge effective date
	 * @param String annualInvestmentChargeEffectiveDate
	 */
    public void setAnnualInvestmentChargeEffectiveDate(
            String annualInvestmentChargeEffectiveDate) {
        this.annualInvestmentChargeEffectiveDate 
                = annualInvestmentChargeEffectiveDate;
    }
    /**
     * This method returns all view fund groups for the investment report
     * .
     * 
     * @return array of ViewFundGroupPresentation
     */
    public void setFundGroups(ViewFundGroupPresentation[] groups) {
    	this.fundGroups = groups;
    }
    /**
     * This method returns all view fund groups for the investment report
     * .
     * 
     * @return array of ViewFundGroupPresentation
     */
    public ViewFundGroupPresentation[] getFundGroups() {
    	return this.fundGroups;
    }
    
    /**
     * This method add a view fund groups presentation for investments report
     * 
     * @param ViewFundGroupPresentation fund group 
     */
    public void addFundGroup(ViewFundGroupPresentation fundGroup) {
    	
    	Vector list = new Vector();
    	if (fundGroups != null){
    		list.addAll(Arrays.asList(fundGroups));
    	}
		list.add(fundGroup);
		this.footnotes.addAll(fundGroup.getFootNotes());
		Collections.sort(list);
		Object[] allFunds = list.toArray();
		fundGroups = new ViewFundGroupPresentation[allFunds.length];
		for (int i = 0; i < allFunds.length; i++)
			fundGroups[i] = (ViewFundGroupPresentation) allFunds[i];
			
		    		
    }
    /**
	 * Gets the unique footnotes symbols for the investment report
	 * @return Returns an array of String
	 */
    public String[] getFootNotes() {
    	if (!footnotes.isEmpty()) {
    		Object[] symbols = footnotes.toArray();
    		String[] footsymbols = new String[symbols.length];
    		for (int i = 0; i< symbols.length; i++){
    			footsymbols[i] = (String) symbols[i];
    		}
    		return footsymbols;
    	}
    	return new String[0];
    }
    /**
	 * To identify the fund grouping scheme
	 * @return Returns a int
	 */
	public int getGroupingScheme() {
		return this.groupingScheme;
	}
	/**
	 * Sets the grouping scheme to identify the fund grouping scheme.
	 * @param groupingScheme int
	 */
	public void setGroupingScheme(int groupingScheme) {
		this.groupingScheme = groupingScheme;
	}
	/**
	 * Gets the isBrokerDealer
	 * @return Returns a boolean
	 */
	public boolean isBrokerDealer() {
		return this.isBrokerDealer;
	}
	/**
	 * Sets the isBrokerDealer
	 * @param isBrokerDealer The isBrokerDealer to set
	 */
	public void setIsBrokerDealer(boolean isBrokerDealer) {
		this.isBrokerDealer = isBrokerDealer;
	}
	
	/**
 	 * This method sorts the funds within each group according to the key specified by the SortTool.
 	 * Creation date: (10/5/2001 10:26:51 AM)
 	 */
	public void sortDataBy(SortTool sortTool) {
	
		// sort selected funds in descending order
		sortDataBy(sortTool, SORT_DESCENDING);
	
	}
	
	/**
 	 * This method sorts the funds within each group according to the key specified by the SortTool
 	 * and either ascending or descending depending on value of sortDescending. In general numbers 
 	 * are sorted in descending order (e.g. returns are best to worst) and strings in ascending  order
 	 * (alphabetical).
 	 * @param SortTool sortTool
 	 * @param boolean sortDescending
 	 */
	public void sortDataBy(SortTool sortTool, boolean sortDescending) {
		// sort funds within funds groups
		ViewFundGroupPresentation[] groups = this.getFundGroups();
		int groupCount = groups.length;
		
		
		Vector groupContractfunds = new Vector();
		
		Sorter selectionSorter = new SelectionSorter();

		//Sort funds within groups (the groups themselves are already sorted)
		for (int i = 0; i < groupCount; i++) {
			ViewFundGroupPresentation groupPresentation = groups[i];
			// gets all funds for the current fund group
			
			Object[] tempfunds = groupPresentation.getSortedFunds();
			if (tempfunds != null) {
				
				int fundCount = tempfunds.length;
				Vector sortedfunds = new Vector(fundCount);
			
				selectionSorter.sortItems(tempfunds, sortTool, sortDescending);	
				for (int j = 0; j < fundCount; j++) {
					sortedfunds.addElement((Vector)tempfunds[j]);
							
				}
				//replace old fund vector with sorted one for group
			
				groups[i].setSortedViewFunds(sortedfunds);
			}
		}	
		this.setFundGroups(groups);
		
	}

    /**
	 * Gets the column number
	 * @return Returns a column number
	 */
	public int getColumnNumber() {
		return columnnumber;
	}
	public void setColumnNumber(int nr) {
		this.columnnumber = nr;
	}
	
    /**
	 * Gets the sort type
	 * @return Returns a sort type
	 */
	public boolean getSortType() {
		return sorttype;
	}

	/**
	 * Sets the sort type
	 * @param sort boolean
	 */
	public void setSortType(boolean sort) {
		sorttype = sort;
	}
	
    /**
	 * Gets the asset risk order by
	 * @return Returns a asset risk order by
	 */
	public int getAssetRiskOrderBy() {
		return assetRiskOrderBy;
	}

	/**
	 * Sets the order by
	 * @param orderby int
	 */
	public void setAssetRiskOrderBy(int arorderby) {
		this.assetRiskOrderBy = arorderby;
	}

    /**
	 * Gets the formatted Date
	 * @return Returns a date in MMMM dd, yyyy format
	 */
	public static String getFormattedDate(Date value) {
		return DateRender.format(value, RenderConstants.EXTRA_LONG_MDY);
	}
	
	/**
	 * Gets the feeWaiverFunds
	 * 
	 * @return feeWaiverFunds List<String>
	 */
	public List<String> getFeeWaiverFunds() {
		return feeWaiverFunds;
	}

	/**
	 * Sets the feeWaiverFunds
	 * @param feeWaiverFunds List<String>
	 */
	public void setFeeWaiverFunds(List<String> feeWaiverFunds) {
		this.feeWaiverFunds = feeWaiverFunds;
	}

	public Map<String, com.manulife.pension.service.fund.valueobject.Fund> getRestrictedFunds() {
		return restrictedFunds;
	}
	
	public void setRestrictedFunds(Map<String, com.manulife.pension.service.fund.valueobject.Fund> map) {
		this.restrictedFunds = map;
	}
}

