package com.manulife.pension.ps.web.contract;

import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.util.content.helper.ContentUtility;

public class ContractSnapshotForm extends PsForm 
{
	private String stringDate = "";
	private String error1 = "";
	private String error2 = "";
	private String error3 = "";
	private boolean isRecentDate = true;
	private boolean displayLoan = true;
	private boolean displayPba = false;
	private boolean displayLifecycle = false;
	private boolean displayBlendedAssets = false;
	
	
	/**
	 * Gets the displayLoan. 
	 * A loan should be displayed if a contract has a loan feature or balance > 0.
	 * @return Returns a boolean
	 */
	public boolean getDisplayLoan() 
	{
		return displayLoan;
	}
	
	/**
	 * Sets the displayLoan
	 * A loan should be displayed if a contract has a loan feature or balance > 0. 
	 * @param boolean displayLoan
	 */
	public void setDisplayLoan(boolean displayLoan) 
	{
		this.displayLoan = displayLoan;
	}

	
	/**
	 * Gets the stringDate. In this format: yyyy MM dd
	 * This is either the most recent business date, or it could be any other contract date.
	 * @return Returns a string
	 */
	public String getStringDate() 
	{
		return stringDate;
	}
	
	/**
	 * Sets the stringDate. In this format: yyyy MM dd
 	 * This is either the most recent business date, or it could be any other contract date.
	 * @param stringDate
	 */
	public void setStringDate(String stringDate) 
	{
		this.stringDate = stringDate;
	}


	/**
	 * This method is true if we are displaying data for the most recent business date
	 * @return  boolean "true" if we are displaying data for the most recent business date
	 */
	public boolean getIsRecentDate() 
	{
		return isRecentDate;
	}

	/**
	 * Set to true if we are displaying data for the most recent business date. 
	 * If we are using other contract dates, set to false.
	 * @param boolean isRecentDate. "true" if we are using the most recent business date data.
	 */
	public void setIsRecentDate(boolean isRecentDate) 
	{
		this.isRecentDate = isRecentDate;
	}
	

	public String getError1() 
	{
		return jsEsc(error1);
	}
	
	public void setError1(String error1) 
	{
		this.error1 = error1;
	}
	
	
	public String getError2() 
	{
		return jsEsc(error2);
	}
	
	public void setError2(String error2) 
	{
		this.error2 = error2;
	}


	public String getError3() 
	{
		return jsEsc(error3);
	}
	
	public void setError3(String error3) 
	{
		this.error3 = error3;
	}


	/**
	 * Gets the displayPba
	 * @return Returns a boolean
	 */
	public boolean getDisplayPba() {
		return displayPba;
	}
	/**
	 * Sets the displayPba
	 * @param displayPba The displayPba to set
	 */
	public void setDisplayPba(boolean displayPba) {
		this.displayPba = displayPba;
	}
	private String jsEsc(String text) {
	    
	    String temp = ContentUtility.jsEsc(text);
	    String returnStr = "";
	    int index1 = 0;
	    int index2 = 0;
	    while (  (index2 = temp.indexOf("&nbsp;",index1)) != -1) {
	    	returnStr = returnStr + temp.substring(index1,index2);
	    	index1=index2+6;
	    }
	    
	    returnStr = returnStr +  temp.substring(index1);
	    
	    return returnStr;
	}

	/**
	 * @param dsiplayLifecycle The dsiplayLifecycle to set.
	 */
	public void setDisplayLifecycle(boolean dsiplayLifecycle) {
		this.displayLifecycle = dsiplayLifecycle;
	}

	/**
	 * @return Returns the dsiplayLifecycle.
	 */
	public boolean getDisplayLifecycle() {
		return displayLifecycle;
	}
	/**
	 * Gets the displayBlendedAssets
	 * @return Returns a boolean
	 */
	public boolean isDisplayBlendedAssets() {
		return displayBlendedAssets;
	}
	
	/**
	 * Sets the displayBlendedAssets
	 * @param displayBlendedAssets The displayBlendedAssets to set
	 */
	public void setDisplayBlendedAssets(boolean displayBlendedAssets) {
		this.displayBlendedAssets = displayBlendedAssets;
	}

	
}


