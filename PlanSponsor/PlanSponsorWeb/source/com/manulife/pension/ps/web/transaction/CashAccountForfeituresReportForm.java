package com.manulife.pension.ps.web.transaction;

import java.util.List;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.report.ReportForm;
/**
 * Form bean class used to hold the values required by Cash Account Forfeitures Page
 * @author Chavva Akhilesh
 *
 */
public class CashAccountForfeituresReportForm extends ReportForm {
	
	private static final long serialVersionUID = 1L;
	private String moneyType;
	private List<LabelValueBean> listOfContractMoneyTypes;
	private String moneyTypeId = null;

	/**
	 * @return the listOfContractMoneyTypes
	 */
	public List<LabelValueBean> getListOfContractMoneyTypes() {
		return listOfContractMoneyTypes;
	}
	
	/**
	 * @param listOfContractMoneyTypes the listOfContractMoneyTypes to set
	 */
	public void setListOfContractMoneyTypes(
			List<LabelValueBean> listOfContractMoneyTypes) {
		this.listOfContractMoneyTypes = listOfContractMoneyTypes;
	}
	
	/**
	 * @return returns the money type 
	 */
	public String getMoneyType() {
		return moneyType;
	}
	
	/**
	 * sets the money type value
	 * @param moneyType
	 */
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	
	/**
	 * @return the moneyTypeId
	 */
	public String getMoneyTypeId() {
		return moneyTypeId;
	}
	
	/**
	 * @param moneyTypeId the moneyTypeId to set
	 */
	public void setMoneyTypeId(String moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}
	
}
