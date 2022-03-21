package com.manulife.pension.ps.web.investment;


import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This class is used to hold the data for 
 * Funds changed report.
 * 
 * @author Murali Chandran
 *
 */
public class InvestmentSelectionReportForm extends BaseReportForm {
	
	private static final long serialVersionUID = 1L;
	
	private List<Date> availableReportDates;
	private String selectedAsOfDate;
	private String selectedView;
	private boolean showAvailableOptions;
	private Date displaySelectedAsOfDate;
	private boolean classZero = false;
	private boolean svgifFlag = false;
	/**
	 * @return the availableReportDates
	 */
	public List<Date> getAvailableReportDates() {
		return availableReportDates;
	}
	/**
	 * @param availableReportDates the availableReportDates to set
	 */
	public void setAvailableReportDates(List<Date> availableReportDates) {
		this.availableReportDates = availableReportDates;
	}
	/**
	 * @return the selectedAsOfDate
	 */
	public String getSelectedAsOfDate() {
		return selectedAsOfDate;
	}
	/**
	 * @param selectedAsOfDate the selectedAsOfDate to set
	 */
	public void setSelectedAsOfDate(String selectedAsOfDate) {
		this.selectedAsOfDate = selectedAsOfDate;
	}
	/**
	 * @return the selectedView
	 */
	public String getSelectedView() {
		return selectedView;
	}
	/**
	 * @param selectedView the selectedView to set
	 */
	public void setSelectedView(String selectedView) {
		this.selectedView = selectedView;
	}
	
	/**
	 * @return the showAvailableOptions
	 */
	public boolean isShowAvailableOptions() {
		return showAvailableOptions;
	}
	
	/**
	 * @param showAvailableOptions the showAvailableOptions to set
	 */
	public void setShowAvailableOptions(boolean showAvailableOptions) {
		this.showAvailableOptions = showAvailableOptions;
	}
	/**
	 * @return the displaySelectedAsOfDate
	 */
	public Date getDisplaySelectedAsOfDate() {
		return displaySelectedAsOfDate;
	}
	/**
	 * @param displaySelectedAsOfDate the displaySelectedAsOfDate to set
	 */
	public void setDisplaySelectedAsOfDate(Date displaySelectedAsOfDate) {
		this.displaySelectedAsOfDate = displaySelectedAsOfDate;
	}
	
	public boolean isClassZero() {
		return classZero;
	}

	public void setClassZero(boolean classZero) {
		this.classZero = classZero;
	}
	public boolean isSvgifFlag() {
		return svgifFlag;
	}

	public void setSvgifFlag(boolean svgifFlag) {
		this.svgifFlag = svgifFlag;
	}
}
