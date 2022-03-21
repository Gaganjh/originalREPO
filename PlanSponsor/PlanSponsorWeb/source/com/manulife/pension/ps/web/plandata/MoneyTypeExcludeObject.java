package com.manulife.pension.ps.web.plandata;



import com.manulife.pension.common.BaseSerializableCloneableObject;

public class MoneyTypeExcludeObject extends BaseSerializableCloneableObject{
  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String moneyTypeId;
    private String excludeMoneyTypeIndicator;
    
	
	

	public String getExcludeMoneyTypeIndicator() {
		return excludeMoneyTypeIndicator;
	}

	public void setExcludeMoneyTypeIndicator(String excludeMoneyTypeIndicator) {
		this.excludeMoneyTypeIndicator = excludeMoneyTypeIndicator;
	}

	public String getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(String moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

}
