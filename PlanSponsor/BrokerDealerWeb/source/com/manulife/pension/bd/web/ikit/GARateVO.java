package com.manulife.pension.bd.web.ikit;

import java.util.Date;
import java.util.List;

import com.manulife.pension.service.fund.valueobject.GARates;

/**
 * This method will hold the Guaranteed Account Funds Information for the
 * current month and the previous month.
 * 
 * @author harlomte
 * 
 */
public class GARateVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Date currentEffectiveDate;
	private Date previousEffectiveDate;

	private String currentMonthEndName;
	private String previousMonthEndName;

	private List<GARates> gaRatesList;

	private List<String> currentMonthEndFootnoteSymbols;
	private List<String> previousMonthEndFootnoteSymbols;

	public GARateVO() {

	}

	public Date getCurrentEffectiveDate() {
		return currentEffectiveDate;
	}

	public void setCurrentEffectiveDate(Date currentEffectiveDate) {
		this.currentEffectiveDate = currentEffectiveDate;
	}

	public Date getPreviousEffectiveDate() {
		return previousEffectiveDate;
	}

	public void setPreviousEffectiveDate(Date previousEffectiveDate) {
		this.previousEffectiveDate = previousEffectiveDate;
	}

	public String getCurrentMonthEndName() {
		return currentMonthEndName;
	}

	public void setCurrentMonthEndName(String currentMonthEndName) {
		this.currentMonthEndName = currentMonthEndName;
	}

	public String getPreviousMonthEndName() {
		return previousMonthEndName;
	}

	public void setPreviousMonthEndName(String previousMonthEndName) {
		this.previousMonthEndName = previousMonthEndName;
	}

	public List<GARates> getGaRatesList() {
		return gaRatesList;
	}

	public void setGaRatesList(List<GARates> gaRatesList) {
		this.gaRatesList = gaRatesList;
	}

	public List<String> getCurrentMonthEndFootnoteSymbols() {
		return currentMonthEndFootnoteSymbols;
	}

	public void setCurrentMonthEndFootnoteSymbols(
			List<String> currentMonthEndFootnoteSymbols) {
		this.currentMonthEndFootnoteSymbols = currentMonthEndFootnoteSymbols;
	}

	public List<String> getPreviousMonthEndFootnoteSymbols() {
		return previousMonthEndFootnoteSymbols;
	}

	public void setPreviousMonthEndFootnoteSymbols(
			List<String> previousMonthEndFootnoteSymbols) {
		this.previousMonthEndFootnoteSymbols = previousMonthEndFootnoteSymbols;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
