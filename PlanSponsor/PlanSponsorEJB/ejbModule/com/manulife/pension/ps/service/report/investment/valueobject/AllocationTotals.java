package com.manulife.pension.ps.service.report.investment.valueobject;

import java.io.Serializable;

public class AllocationTotals  implements Serializable{
	
	private String fundCategoryType;
	
	private int numberOfOptions;
	private int participantsInvested;
	private double employeeAssets;
	private double employerAssets;
	private double totalAssets;
	private double percentageOfTotal;
	
	public int getNumberOfOptions(){
		return numberOfOptions;
	}
	
	public void setNumberOfOptions(int value){
		numberOfOptions = value;
	}
	
	public int getParticipantsInvested(){
		return participantsInvested;
	}
	
	public void setParticipantsInvested(int value){
		participantsInvested = value;
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
	
	public String getFundCategoryType(){
		return fundCategoryType;
	}
	
	public void setFundCategoryType(String value){
		fundCategoryType = value;
	}
	
	public String toString(){
		StringBuffer temp = new StringBuffer();
		temp.append("fundCategoryType = " + fundCategoryType + "\n");
		temp.append("numberOfOptions = " + numberOfOptions + "\n");
		temp.append("participantsInvested = " + participantsInvested + "\n");
		temp.append("employeeAssets = " + employeeAssets + "\n");
		temp.append("employerAssets = " + employerAssets + "\n");
		temp.append("totalAssets = " + totalAssets + "\n");
		temp.append("percentageOfTotal = " + percentageOfTotal + "\n");
		return temp.toString();
	}
}

