package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;


public class FundCategory implements Comparable, Serializable{
	
	public static String[] CATEGORY_DESCRIPTION = {"Asset Allocation - Target Date", "Asset Allocation - Target Risk","Guaranteed Income Feature","Aggressive Growth","Growth","Growth & Income","Income","Conservative","Personal Brokerage Account"};
	
	public static String[] CATEGORY_CODE = {"LC", "LS","GW", "AG", "GR", "GI", "IN", "CN", "PB" };
	public static String[] CATEGORY_DESCRIPTION_EXCLUDED = {"Asset Allocation - Target Date", "Asset Allocation - Target Risk", "Guaranteed Income Feature", "Aggressive Growth","Growth","Growth & Income","Income","Conservative"};
	
	public static String[] CATEGORY_CODE_EXCLUDED = {"LC", "LS", "AG", "GR", "GI", "IN", "CN"};
	
	public static String NON_LIFESTYLE_LIFECYCLE = "nonLifeStyleLifeCycle";
	public static String LIFESTYLE = "lifeStyle";
	public static String LIFECYCLE = "lifeCycle";
	public static String GIFL = "gateWay";
	public static String PBA = "pba";
	
	public FundCategory(String code, int order){
		categoryCode = code;
		sortOrder = order;	
	}
	
	
	public FundCategory(String code){
		this.categoryCode = code;
		for(int i = 0; i<CATEGORY_CODE.length; i++){
			if(code.equalsIgnoreCase(CATEGORY_CODE[i])){
				categoryDesc = CATEGORY_DESCRIPTION[i];
				sortOrder = i+1;
			}
		}	
	}
	
	public FundCategory(String code, String fontColor, String bgColor){
		this.categoryCode = code;
		this.fontColor = fontColor;
		this.bgColor = bgColor;
		for(int i = 0; i<CATEGORY_CODE.length; i++){
			if(code.equalsIgnoreCase(CATEGORY_CODE[i])){
				categoryDesc = CATEGORY_DESCRIPTION[i];
				sortOrder = i+1;
			}
		}	
	}
	
	public FundCategory(String code, String desc, int order) {
		categoryCode = code;
		categoryDesc =  desc;
		sortOrder = order;	
	}
	

	

	public int compareTo(Object o) throws ClassCastException{
		if (!(o instanceof FundCategory))
      	throw new ClassCastException("A FundCategory object expected.");
    	int categorySortOrder = ((FundCategory) o).getSortOrder();  
    	return this.sortOrder - categorySortOrder; 
	}
	
	public int hashCode(){
		return categoryCode.hashCode();
	}
	
	private int sortOrder;
	private String categoryCode;
	private String categoryDesc;
	private String fontColor;
	private String bgColor;
	
	public String getCategoryCode(){
		return categoryCode;
	}
	
	public int getSortOrder(){
		return sortOrder;
	}
	
	public String getCategoryDesc(){
		return categoryDesc;
	}
	
	public String getFontColor(){
		return fontColor;
	}
	
	public String getBgColor(){
		return bgColor;
	}
	
	public void setFontColor(String value){
		fontColor = value;
	}
	
	public void setBgColor(String value){
		bgColor = value;
	}
	
	public String toString(){
		StringBuffer strBuff = new StringBuffer();
		strBuff.append("\n categoryCode = ").append(categoryCode);
		strBuff.append("\n categoryDesc = ").append(categoryDesc);
		strBuff.append("\n fontColor = ").append(fontColor);
		strBuff.append("\n bgColor = ").append(bgColor);
		strBuff.append("\n sortOrder = ").append(sortOrder);
		return strBuff.toString();
	}

}

