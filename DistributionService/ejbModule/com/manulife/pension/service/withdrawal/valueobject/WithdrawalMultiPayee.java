package com.manulife.pension.service.withdrawal.valueobject;

public class WithdrawalMultiPayee {
	
	String payeeCategories;
	String payeeCategoriesDesc;
	String payeeType;
	
	String participantFlag;
	String taxableBal;
	String participantAftrTaxFlag;
	String participantRothFlag;
	String participantNonRothFlag;
	String rothBalFalg;
	
	public String getPayeeCategories() {
		return payeeCategories;
	}
	public void setPayeeCategories(String payeeCategories) {
		this.payeeCategories = payeeCategories;
	}
	public String getPayeeCategoriesDesc() {
		return payeeCategoriesDesc;
	}
	public void setPayeeCategoriesDesc(String payeeCategoriesDesc) {
		this.payeeCategoriesDesc = payeeCategoriesDesc;
	}
	public String getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
	public String getParticipantFlag() {
		return participantFlag;
	}
	public void setParticipantFlag(String participantFlag) {
		this.participantFlag = participantFlag;
	}
	public String getTaxableBal() {
		return taxableBal;
	}
	public void setTaxableBal(String taxableBal) {
		this.taxableBal = taxableBal;
	}
	public String getParticipantAftrTaxFlag() {
		return participantAftrTaxFlag;
	}
	public void setParticipantAftrTaxFlag(String participantAftrTaxFlag) {
		this.participantAftrTaxFlag = participantAftrTaxFlag;
	}
	public String getParticipantRothFlag() {
		return participantRothFlag;
	}
	public void setParticipantRothFlag(String participantRothFlag) {
		this.participantRothFlag = participantRothFlag;
	}
	public String getParticipantNonRothFlag() {
		return participantNonRothFlag;
	}
	public void setParticipantNonRothFlag(String participantNonRothFlag) {
		this.participantNonRothFlag = participantNonRothFlag;
	}
	public String getRothBalFalg() {
		return rothBalFalg;
	}
	public void setRothBalFalg(String rothBalFalg) {
		this.rothBalFalg = rothBalFalg;
	}
	

}
