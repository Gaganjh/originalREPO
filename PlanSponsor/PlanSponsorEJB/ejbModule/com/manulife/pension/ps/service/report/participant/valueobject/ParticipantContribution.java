package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.util.SortedMap;

public class ParticipantContribution implements Serializable {
	
	private String identifier;
	private String name;
	private SortedMap moneyTypeAmounts;
	private SortedMap loanAmounts;
	
	public ParticipantContribution() {
	}
	
	public ParticipantContribution(String identifier, String firstName, String lastName, 
				SortedMap moneyTypeAmounts, SortedMap loanAmounts) {
		this.identifier = identifier;
		this.name = lastName + ", " + firstName;
		this.moneyTypeAmounts = moneyTypeAmounts;
		this.loanAmounts = loanAmounts;
	}
	
	public ParticipantContribution(String identifier, String name, 
			SortedMap moneyTypeAmounts, SortedMap loanAmounts) {
		this.identifier = identifier;
		this.name = name;
		this.moneyTypeAmounts = moneyTypeAmounts;
		this.loanAmounts = loanAmounts;
	}
	
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return Returns the loanAmounts.
	 */
	public SortedMap getLoanAmounts() {
		return loanAmounts;
	}

	/**
	 * @return Returns the moneyTypeAmounts.
	 */
	public SortedMap getMoneyTypeAmounts() {
		return moneyTypeAmounts;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param loanAmounts The loanAmounts to set.
	 */
	public void setLoanAmounts(SortedMap loanAmounts) {
		this.loanAmounts = loanAmounts;
	}
	/**
	 * @param moneyTypeAmounts The moneyTypeAmounts to set.
	 */
	public void setMoneyTypeAmounts(SortedMap moneyTypeAmounts) {
		this.moneyTypeAmounts = moneyTypeAmounts;
	}
}
