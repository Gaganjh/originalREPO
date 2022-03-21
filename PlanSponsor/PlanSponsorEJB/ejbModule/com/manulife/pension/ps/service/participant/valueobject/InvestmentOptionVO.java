package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;

public class InvestmentOptionVO implements Serializable
{
	private FundCategory category;
	private ParticipantFundSummaryVO[] participantFundSummaryArray;
	
	public ParticipantFundSummaryVO[] getParticipantFundSummaryArray()
	{
		return participantFundSummaryArray;
	}	
	
	public void setParticipantFundSummaryArray(ParticipantFundSummaryVO[] participantFundSummaryArray)
	{
		this.participantFundSummaryArray = participantFundSummaryArray;
	}	
	/**
	 * Gets the category
	 * @return Returns a FundCategory
	 */
	public FundCategory getCategory() {
		return category;
	}
	/**
	 * Sets the category
	 * @param category The category to set
	 */
	public void setCategory(FundCategory category) {
		this.category = category;
	}

}

