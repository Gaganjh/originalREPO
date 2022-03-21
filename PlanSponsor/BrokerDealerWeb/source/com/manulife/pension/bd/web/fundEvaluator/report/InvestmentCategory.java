package com.manulife.pension.bd.web.fundEvaluator.report;

import java.awt.Color;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.manulife.pension.service.fund.valueobject.InvestmentGroup;

/**
 * This class holds all fund data categorized by InvestmentCategory
 * @author PWakode
 */

public class InvestmentCategory {
	
	private List<DecoratedFund> decoratedFunds = null; // decorated funds
	private List<DecoratedFund> basicDecoratedFunds = null; // holds references to basic funds

	private InvestmentGroup investmentGroup = null;
	
	public InvestmentCategory(Integer newCategoryId, List<DecoratedFund> newDecoratedFunds, Hashtable<Integer, InvestmentGroup> investmentGroupObjTable) {
        this.decoratedFunds = newDecoratedFunds;
        init(newCategoryId, investmentGroupObjTable);
    }
	
	public List<DecoratedFund> getBasicDecoratedFunds() {
		return basicDecoratedFunds;
	}
	
	public int getOrder() {
		return investmentGroup.getOrder();
	}
	public Integer getCategoryId() {
		return investmentGroup.getOrderInteger();
	}
	public String getColorcode() {
		return investmentGroup.getColorcode();
	}
	
	public Color getFontColor() {
		// if not white, assume black
		// note that we use the old, lowercase constants, as required by JDK 1.3
		return "white".equalsIgnoreCase(investmentGroup.getFontcolor()) ? Color.white : Color.black;
	}
	
	public Color getBackgroundColor() {
		return investmentGroup.getColor();
	}
	
	public String getGroupname() {
		return investmentGroup.getGroupname();
	}
	
	public List<DecoratedFund> getDecoratedFunds() {
		return decoratedFunds;
	}
	
	/** number of funds that are mandatory, tool recommended and broker selected */
	public int getNumberOfBasicFunds() {
		return basicDecoratedFunds.size();
	}
	
	public int getNumberOfFunds() {
		return decoratedFunds.size();
	}
	
	@SuppressWarnings("unchecked")
    private void init(Integer newCategoryId, Hashtable<Integer, InvestmentGroup> investmentGroupObjTable) {
        
        this.investmentGroup = (InvestmentGroup)investmentGroupObjTable.get(newCategoryId);
                
        if (decoratedFunds == null) {
            decoratedFunds = new Vector();
        }
        
        basicDecoratedFunds = new Vector();
    
        for (int i=0; i < decoratedFunds.size(); i++) {
            DecoratedFund decoratedFund = (DecoratedFund) decoratedFunds.get(i);
            if (decoratedFund.isBasicFund()) {
                basicDecoratedFunds.add(decoratedFund);
            }   
        }   
    }

}
