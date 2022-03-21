package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;

import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;

/**
 * One money type header for contribution details page.
 *
 * @author Jim Adamthwaite
 */
public class MoneyTypeHeader implements Serializable {

	private String moneyTypeId;
	private String occurrenceNumber;
	private MoneyTypeVO moneyType;

	public MoneyTypeHeader() {
		super();
	}

	public MoneyTypeHeader(String moneyTypeId, String occurrenceNumber, MoneyTypeVO moneyType) { 
			this.moneyTypeId = moneyTypeId;
			this.occurrenceNumber = occurrenceNumber;
			this.moneyType = moneyType;
	}
	
	public String getKey() {
		return getMoneyTypeId() + "/" + getOccurrenceNumber();
	}
	public String toString() {
		String myString = getMoneyTypeId() + ", " 
				+ getOccurrenceNumber() + ", "  
				+ getMoneyType().toString(); 
		return myString;
	}
	public MoneyTypeVO getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(MoneyTypeVO moneyType) {
		this.moneyType = moneyType;
	}

	public String getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(String moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public String getOccurrenceNumber() {
		return occurrenceNumber;
	}

	public void setOccurrenceNumber(String occurrenceNumber) {
		this.occurrenceNumber = occurrenceNumber;
	}

}