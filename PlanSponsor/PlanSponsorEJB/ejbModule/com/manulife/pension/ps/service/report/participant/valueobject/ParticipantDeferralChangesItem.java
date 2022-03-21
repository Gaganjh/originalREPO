package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;


public class ParticipantDeferralChangesItem implements Serializable {
	private double contributionAmt;
	private double contributionPct;
	private String moneyTypeCode;	//EEROT or TRAD for now

	public ParticipantDeferralChangesItem(double contributionAmt, 
		double contributionPct, String moneyTypeCode) {
		this.contributionAmt = contributionAmt;
		this.contributionPct = contributionPct;
		this.moneyTypeCode = moneyTypeCode;
	}

	public void setContributionAmt(double contributionAmt) { this.contributionAmt = contributionAmt; }
	public double getContributionAmt() { return this.contributionAmt; }	
	public void setContributionPct(double contributionPct) { this.contributionPct = contributionPct; }
	public double getContributionPct() { return this.contributionPct; }	
	public void setMoneyTypeCode(String moneyTypeCode) {this.moneyTypeCode = moneyTypeCode; }
	public String getMoneyTypeCode() { return this.moneyTypeCode; }
}
