package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.util.SortedMap;

public class ParticipantVesting implements Serializable {
	
	private String identifier;
	private String name;
	private SortedMap moneyTypePercentages;
	
	
	public ParticipantVesting() {
	}
	
	public ParticipantVesting(String identifier, String firstName, String lastName, 
				SortedMap moneyTypePercentages) {
		this.identifier = identifier;
		this.name = lastName + ", " + firstName;
		this.moneyTypePercentages = moneyTypePercentages;
	}
	
	public ParticipantVesting(String identifier, String name, 
			SortedMap moneyTypePercentages) {
		this.identifier = identifier;
		this.name = name;
		this.moneyTypePercentages = moneyTypePercentages;
	}
	
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}


	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

    public SortedMap getMoneyTypePercentages() {
        return moneyTypePercentages;
    }

    public void setMoneyTypePercentages(SortedMap moneyTypePercentages) {
        this.moneyTypePercentages = moneyTypePercentages;
    }

}
