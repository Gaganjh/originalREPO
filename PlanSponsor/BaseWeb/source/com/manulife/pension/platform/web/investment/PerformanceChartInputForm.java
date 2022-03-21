package com.manulife.pension.platform.web.investment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * Copied from PlanSponsorWeb
 * 
 * PerformanceChartInputForm submitted by the user and contains data for
 * plotting a chart
 * 
 * @author 	SAyyalusamy
 */

 public class PerformanceChartInputForm extends BaseForm implements Serializable{

	protected String 	fundSelection1 	= " ";
	protected String 	fundSelection2 	= " ";
	protected String 	fundSelection3	= " ";
	protected String 	fundSelection4	= " ";
	protected String 	fundSelection5	= " ";
	protected String 	fundSelection6	= " ";
	protected String 	fundPercentage1	= "";
	protected String 	fundPercentage2	= "";
	protected String 	fundPercentage3	= "";
	protected String 	fundPercentage4	= "";
	protected String 	fundPercentage5	= "";
	protected String 	fundPercentage6	= "";
	protected String 	startDate		= "";
	protected String 	endDate			= "";
	protected String 	button			= "";

	/**
	 * Default Constructor
	 */
	public PerformanceChartInputForm() {
		super();
		setDates();
	}

	/**
	 * Reset all properties to their default values.
	 */
    public void resetFundSelection() {
		this.fundSelection1 = " ";
		this.fundSelection2 = " ";
		this.fundSelection3 = " ";
		this.fundSelection4 = " ";
		this.fundSelection5 = " ";
		this.fundSelection6 = " ";
		this.fundPercentage1 = "";
		this.fundPercentage2 = "";
		this.fundPercentage3 = "";
		this.fundPercentage4 = "";
		this.fundPercentage5 = "";
		this.fundPercentage6 = "";
		setDates();
    }

    /**
     * 	Initializes the startDate and endDate (Date format "MM/yyyy")
     * 		--> startDate will be the current date
     * 	 	--> endDate will be startDate's year - 1
     */
	private void setDates() {
		SimpleDateFormat formatter = new SimpleDateFormat(CommonConstants.CHART_DATE_PATTERN);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 0);
		Date eDate = new Date(cal.getTime().getTime());
		cal.add(Calendar.YEAR, -1);
		Date sDate = new Date(cal.getTime().getTime());
		this.endDate = formatter.format(eDate);
		this.startDate = formatter.format(sDate);
	}


	/**
	 * Get fundSelection1
	 * @return String
	 */
	public String getFundSelection1() {
		return fundSelection1;
	}

	/**
	 * Get fundSelection2
	 * @return String
	 */
	public String getFundSelection2() {
		return fundSelection2;
	}

	/**
	 * Get fundSelection3
	 * @return String
	 */
	public String getFundSelection3() {
		return fundSelection3;
	}

	/**
	 * Get fundSelection4
	 * @return String
	 */
	public String getFundSelection4() {
		return fundSelection4;
	}

	/**
	 * Get fundSelection5
	 * @return String
	 */
	public String getFundSelection5() {
		return fundSelection5;
	}

	/**
	 * Get fundSelection6
	 * @return String
	 */
	public String getFundSelection6() {
		return fundSelection6;
	}

	/**
	 * Get fundPercentage1
	 * @return String
	 */
	public String getFundPercentage1() {
		return fundPercentage1;
	}

	/**
	 * Get fundPercentage2
	 * @return String
	 */
	public String getFundPercentage2() {
		return fundPercentage2;
	}

	/**
	 * Get fundPercentage3
	 * @return String
	 */
	public String getFundPercentage3() {
		return fundPercentage3;
	}

	/**
	 * Get fundPercentage4
	 * @return String
	 */
	public String getFundPercentage4() {
		return fundPercentage4;
	}

	/**
	 * Get fundPercentage5
	 * @return String
	 */
	public String getFundPercentage5() {
		return fundPercentage5;
	}

	/**
	 * Get fundPercentage6
	 * @return String
	 */
	public String getFundPercentage6() {
		return fundPercentage6;
	}

	/**
	 * Get startDate
	 * @return String
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Get endDate
	 * @return String
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Get button
	 * @return String
	 */
	public String getButton() {
		return button;
	}

/* setter methods */

	/**
	 * Set Fund Selection 1
	 * @param fundSelection1 - String
	 */
	public void setFundSelection1(String fundSelection1) {
		this.fundSelection1 = fundSelection1;
	}

	/**
	 * Set Fund Selection 2
	 * @param fundSelection2 - String
	 */
	public void setFundSelection2(String fundSelection2) {
		this.fundSelection2 = fundSelection2;
	}

	/**
	 * Set Fund Selection 3
	 * @param fundSelection3 - String
	 */
	public void setFundSelection3(String fundSelection3) {
		this.fundSelection3 = fundSelection3;
	}

	/**
	 * Set Fund Selection 4
	 * @param fundSelection4 - String
	 */
	public void setFundSelection4(String fundSelection4) {
		this.fundSelection4 = fundSelection4;
	}

	/**
	 * Set Fund Selection 5
	 * @param fundSelection5 - String
	 */
	public void setFundSelection5(String fundSelection5) {
		this.fundSelection5 = fundSelection5;
	}

	/**
	 * Set Fund Selection 6
	 * @param fundSelection6 - String
	 */
	public void setFundSelection6(String fundSelection6) {
		this.fundSelection6 = fundSelection6;
	}

	/**
	 * Set Fund Percentage 1
	 * @param fundPercentage1 - String
	 */
	public void setFundPercentage1(String fundPercentage1) {
		this.fundPercentage1 = fundPercentage1;
	}

	/**
	 * Set Fund Percentage 2
	 * @param fundPercentage2 - String
	 */
	public void setFundPercentage2(String fundPercentage2) {
		this.fundPercentage2 = fundPercentage2;
	}

	/**
	 * Set Fund Percentage 3
	 * @param fundPercentage3 - String
	 */
	public void setFundPercentage3(String fundPercentage3) {
		this.fundPercentage3 = fundPercentage3;
	}

	/**
	 * Set Fund Percentage 4
	 * @param fundPercentage4 - String
	 */
	public void setFundPercentage4(String fundPercentage4) {
		this.fundPercentage4 = fundPercentage4;
	}

	/**
	 * Set Fund Percentage 5
	 * @param fundPercentage5 - String
	 */
	public void setFundPercentage5(String fundPercentage5) {
		this.fundPercentage5 = fundPercentage5;
	}

	/**
	 * Set Fund Percentage 6
	 * @param fundPercentage6 - String
	 */
	public void setFundPercentage6(String fundPercentage6) {
		this.fundPercentage6 = fundPercentage6;
	}

	/**
	 * Set Start Date
	 * @param startDate - String
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set End Date
	 * @param endDate - String
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Set Button
	 * @param button - String
	 */
	public void setButton(String button) {
		this.button = button;
	}
}

