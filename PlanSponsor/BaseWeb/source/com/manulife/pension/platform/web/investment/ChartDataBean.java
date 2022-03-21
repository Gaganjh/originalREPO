package com.manulife.pension.platform.web.investment;

import java.io.Serializable;
import java.util.List;

/**
 * Bean class for Performance Charting.  This bean is stored in the session and is used by the performance
 * chart tag to draw the line graph
 *
 * @author   Chris Shin
 * @version  CS1.0  (April 14, 2004)
 **/
public class ChartDataBean implements Serializable{

	public final static String PRESENT_APPLET="applet";
	public final static String PRESENT_IMAGE="image";
	public final static String IMAGE_TYPE_GIF="GIF";
	public final static String IMAGE_TYPE_JPEG="JPEG";
	private List 		unitValues;
	private List 		effectiveDates;
	private List 		fundNames;
	private List		pctValues;
	private String[] 	fundIds;
	private String[]	fundFootnotes;
	private List		fundFootnotesByFund;
	private String		startDate;
	private String 		endDate;
	private Boolean 	valid = new Boolean(false);
	private String mode = PRESENT_IMAGE;
	private String imageType = IMAGE_TYPE_GIF;



	public ChartDataBean(){
	}

    public ChartDataBean(String startDate, String endDate, List unitValues,
        List effectiveDates, List fundNames, List pctValues, String[] fundIds,
        String[] fundFootnotes, List fundFootnotesByFund, Boolean valid) {

        this.unitValues = unitValues;
        this.effectiveDates = effectiveDates;
        this.fundNames = fundNames;
        this.pctValues = pctValues;
        this.fundIds = fundIds;
        this.fundFootnotes = fundFootnotes;
        this.fundFootnotesByFund = fundFootnotesByFund;
        this.startDate = startDate;
        this.endDate = endDate;
        this.valid = valid;
    }

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getStartDate(){
		return this.startDate;
	}

	public String getEndDate(){
		return this.endDate;
	}

	public void setUnitValues(List unitValues){
		this.unitValues = unitValues;
	}

	public void setPctValues(List pctValues){
		this.pctValues = pctValues;
	}

	public void setEffectiveDates(List effectiveDates){
		this.effectiveDates = effectiveDates;
	}

	public void setFundIds(String[] fundIds){
		this.fundIds = fundIds;
	}

	public void setFundFootnotes(String[] fundFootnotes){
		this.fundFootnotes = fundFootnotes;
	}

	public String[] getFundFootnotes(){
		return this.fundFootnotes;
	}

	public void setFundFootnotesByFund(List fundFootnotesByFund){
		this.fundFootnotesByFund = fundFootnotesByFund;
	}

	public List getFundFootnotesByFund(){
		return this.fundFootnotesByFund;
	}


	public void setFundNames(List fundNames){
		this.fundNames = fundNames;
	}

	public void setValid(Boolean valid){
		this.valid = valid;
	}

	public List getUnitValues(){
		return this.unitValues;
	}

	public List getPctValues(){
		return this.pctValues;
	}

	public List getEffectiveDates(){
		return this.effectiveDates;
	}

	public String[] getFundIds(){
		return this.fundIds;
	}

	public List getFundNames(){
		return this.fundNames;
	}

	public Boolean getValid(){
		return valid;
	}
	/**
	 * Gets the mode
	 * @return Returns a String
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * Sets the mode
	 * @param mode The mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Gets the imageType
	 * @return Returns a String
	 */
	public String getImageType() {
		return imageType;
	}
	/**
	 * Sets the imageType
	 * @param imageType The imageType to set
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

}

