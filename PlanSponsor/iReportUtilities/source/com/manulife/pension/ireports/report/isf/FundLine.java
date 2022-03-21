/**
 * 
 */
package com.manulife.pension.ireports.report.isf;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.report.util.ReportStrings;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;

public class FundLine {

	private String investmentid;
	private boolean checked;
	private String fundLongName;
	private boolean isClosedToNewBusiness;
	boolean isLifecycle;
	private boolean isMorganStanleyFund;
	private ReportStrings messages;
	private String fundFamilyCode;
	
	public static FundLine makeLifecycleFund(boolean checked, ReportStrings messages, String fundFamilyCode) {
		String fundLongName = "";
		if (StandardReportsConstants.RETIREMENT_LIVING_FUND_FAMILY_CD
				.equals(fundFamilyCode)) {
			fundLongName = messages.getString("fund_line.retirementLiving.name");
		} else if (StandardReportsConstants.RETIREMENT_CHOICES_FUND_FAMILY_CD
				.equals(fundFamilyCode)) {
			fundLongName = messages.getString("fund_line.retirementChoices.name");
		}
		
		return new FundLine("", checked, fundLongName, false, true, false, messages, fundFamilyCode);
	}
	
	public static FundLine make(Fund fund, boolean checked, ReportStrings messages) {
		String longName = fund.getFundLongName();
		if (fund.isExclusiveOfSvf()) {
			longName += "**";
		}
		return new FundLine(fund.getInvestmentid(), checked, longName, fund.isClosedToNewBusiness(), fund.isLifecycle(), fund.isStableValue(), messages, null);
	}

	
	FundLine(String investmentid, boolean checked, String fundLongName, boolean isClosedToNewBusiness, boolean isLifecycle, boolean isMorganStanleyFund, ReportStrings messages, String fundFamilyCode) {
		super();
		this.investmentid = investmentid;
		this.checked = checked;
		this.fundLongName = fundLongName;
		this.isClosedToNewBusiness = isClosedToNewBusiness;
		this.isLifecycle = isLifecycle;
		this.isMorganStanleyFund = isMorganStanleyFund;
		this.messages = messages;
		this.fundFamilyCode = fundFamilyCode;
	}
	
	public String getInvestmentid() {
		return investmentid;
	}
	
	public String getFundLongName() {
		return fundLongName;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public String getFundExtraDescription() {
		String result = null;
		
		result = appendMessage(result, isMorganStanleyFund, "fund_line.has_competing_funds");
		result = appendMessage(result, isClosedToNewBusiness, "fund_line.closed_to_new_business");
		//result = appendMessage(result, isLifecycle, "fund_line.lifecycle");
		
		return result;
	}

	public String getFundExtraDescriptionLC() {
		String result = null;
		if (StandardReportsConstants.RETIREMENT_LIVING_FUND_FAMILY_CD
				.equals(fundFamilyCode)) {
			result = appendMessage(result, isLifecycle, "fund_line.retirementLiving");
		} else if (StandardReportsConstants.RETIREMENT_CHOICES_FUND_FAMILY_CD
				.equals(fundFamilyCode)) {
			result = appendMessage(result, isLifecycle, "fund_line.retirementChoices");
		}
		return result;
	}
	
	/**
	 * Append the message found under messageKey if append is true. 
	 * @param currentMessage
	 * @param append
	 * @param messageKey
	 * @return null if currentMessage was null and append is false, currentMessage += message otherwise
	 */
	private String appendMessage(String currentMessage, boolean append, String messageKey) {
		if (append) {
			if (StringUtils.isNotEmpty(currentMessage)) {
				currentMessage += "\n";
			} else {
				currentMessage = "";
			}
			currentMessage += messages.getString(messageKey);
		}
		return currentMessage;
	}

	public String toString() {
		return "[investmentId:" + getInvestmentid() + ", isChecked:" + isChecked() + ", fundLongName:" + getFundLongName()
				+ "]"; 
	}

}