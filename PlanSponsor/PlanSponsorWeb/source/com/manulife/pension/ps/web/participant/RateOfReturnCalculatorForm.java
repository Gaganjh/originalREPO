package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import com.manulife.pension.platform.web.controller.AutoForm;

public class RateOfReturnCalculatorForm extends AutoForm {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String timePeriodFromToday;

	private String startDay;

	private String startMonth;

	private String startYear;

	private String endDay;

	private String endMonth;

	private String endYear;

	private String rateOfReturn;

	private String fieldEmpty = StringUtils.EMPTY;

	private String buttonClicked;

	private boolean resultsCalculatedFlag;

	private boolean warningMessageFlag;

	private String resultStartDate;

	private String resultEndDate;

	// Radio button on RateofReturnCalculator Form
	public static List VALID_TIME_PERIOD_FROM_TODAY_VALUES;

	public static List TIME_PERIODS_REQUIRE_CALCULATION;

	public static final String TIME_PERIOD_FROM_TODAY_ONE_MONTH = "1";

	public static final String TIME_PERIOD_FROM_TODAY_THREE_MONTHS = "3";

	public static final String TIME_PERIOD_FROM_TODAY_SIX_MONTHS = "6";

	public static final String TIME_PERIOD_FROM_TODAY_TWELVE_MONTHS = "12";

	public static final String TIME_PERIOD_FROM_TODAY_YTD = "0";

	public static final String TIME_PERIOD_FROM_DATE_RANGE = "99";

	public static final String WARNING_MESSAGE = "You have selected a start date when your account was showing a zero balance. We have calculated your rate of return for the time period your account had a balance.";

	static {
		VALID_TIME_PERIOD_FROM_TODAY_VALUES = new ArrayList();
		VALID_TIME_PERIOD_FROM_TODAY_VALUES
				.add(TIME_PERIOD_FROM_TODAY_ONE_MONTH);
		VALID_TIME_PERIOD_FROM_TODAY_VALUES
				.add(TIME_PERIOD_FROM_TODAY_THREE_MONTHS);
		VALID_TIME_PERIOD_FROM_TODAY_VALUES
				.add(TIME_PERIOD_FROM_TODAY_SIX_MONTHS);
		VALID_TIME_PERIOD_FROM_TODAY_VALUES
				.add(TIME_PERIOD_FROM_TODAY_TWELVE_MONTHS);
		VALID_TIME_PERIOD_FROM_TODAY_VALUES.add(TIME_PERIOD_FROM_TODAY_YTD);
		VALID_TIME_PERIOD_FROM_TODAY_VALUES.add(TIME_PERIOD_FROM_DATE_RANGE);

		TIME_PERIODS_REQUIRE_CALCULATION = new ArrayList();
		TIME_PERIODS_REQUIRE_CALCULATION.add(TIME_PERIOD_FROM_TODAY_ONE_MONTH);
		TIME_PERIODS_REQUIRE_CALCULATION
				.add(TIME_PERIOD_FROM_TODAY_THREE_MONTHS);
		TIME_PERIODS_REQUIRE_CALCULATION.add(TIME_PERIOD_FROM_TODAY_SIX_MONTHS);
		TIME_PERIODS_REQUIRE_CALCULATION
				.add(TIME_PERIOD_FROM_TODAY_TWELVE_MONTHS);
	}

	/**
	 * Default constructor
	 */
	public RateOfReturnCalculatorForm() {
		super();
		timePeriodFromToday = "99";
		startDay = fieldEmpty;
		startMonth = fieldEmpty;
		startYear = fieldEmpty;
		endDay = fieldEmpty;
		endMonth = fieldEmpty;
		endYear = fieldEmpty;
		rateOfReturn = fieldEmpty;
		buttonClicked = "";
		resultStartDate = fieldEmpty;
		resultEndDate = fieldEmpty;
		resultsCalculatedFlag = false;
		warningMessageFlag = false;
	}

	/**
	 * Getters
	 */
	public String getTimePeriodFromToday() {
		return trimString(timePeriodFromToday);
	}

	public String getStartDay() {
		return trimString(startDay);
	}

	public String getStartMonth() {
		return trimString(startMonth);
	}

	public String getStartYear() {
		return trimString(startYear);
	}

	public String getEndDay() {
		return trimString(endDay);
	}

	public String getEndMonth() {
		return trimString(endMonth);
	}

	public String getEndYear() {
		return trimString(endYear);
	}

	public boolean getResultsCalculatedFlag() {
		return resultsCalculatedFlag;
	}

	public boolean getWarningMessageFlag() {
		return warningMessageFlag;
	}

	public String getResultStartDate() {
		return trimString(resultStartDate);
	}

	public String getResultEndDate() {
		return trimString(resultEndDate);
	}

	public String getRateOfReturn() {
		return trimString(rateOfReturn);
	}

	public String getButtonClicked() {
		return trimString(buttonClicked);
	}

	/**
	 * Setters
	 */
	public void setTimePeriodFromToday(String timePeriodFromToday) {
		this.timePeriodFromToday = timePeriodFromToday;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public void setResultsCalculatedFlag(boolean resultsCalculatedFlag) {
		this.resultsCalculatedFlag = resultsCalculatedFlag;
	}

	public void setWarningMessageFlag(boolean warningMessageFlag) {
		this.warningMessageFlag = warningMessageFlag;
	}

	public void setResultStartDate(String resultStartDate) {
		this.resultStartDate = resultStartDate;
	}

	public void setResultEndDate(String resultEndDate) {
		this.resultEndDate = resultEndDate;
	}

	public void setRateOfReturn(String rateOfReturn) {
		this.rateOfReturn = rateOfReturn;
	}

	public void setButtonClicked(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}

	@Override
	public void reset( HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		//super.reset(arg0, arg1);
		this.setEndDay(fieldEmpty);
		this.setEndMonth(fieldEmpty);
		this.setEndYear(fieldEmpty);
		this.setStartDay(fieldEmpty);
		this.setStartMonth(fieldEmpty);
		this.setStartYear(fieldEmpty);
		this.setTimePeriodFromToday(fieldEmpty);
		this.setResultsCalculatedFlag(false);
		this.setRateOfReturn(fieldEmpty);
	}

}
