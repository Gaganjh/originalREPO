package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ParticipantBenefitBaseDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String firstName;

	private String lastName;

	private String middleInitial;

	private String ssn;

	private Date dateOfBirth;
	
	private Date asOfDate;
	
	private BigDecimal marketValueGoFunds;
	
	private int age;
	
	private String name;

	/**
	 * Constructor
	 */
	public ParticipantBenefitBaseDetails() {
	}
	
	public Date getAsOfDate() {
	    return asOfDate;
	}
	
	public void setAsOfDate(Date asOfDate) {
	    this.asOfDate = asOfDate;
	}
	
	/**
	 * Gets the marketValueGoFunds
	 * @return Returns BigDecimal
	 */
	
	public BigDecimal getMarketValueGoFunds() {
		return marketValueGoFunds;
	}

	/**
	 * Sets the marketValueGoFunds
	 * 
	 * @param marketValueGoFunds 
	 */
	
	public void setMarketValueGoFunds(BigDecimal marketValueGoFunds) {
		this.marketValueGoFunds = marketValueGoFunds;
	}

	/**
	 * Gets the age
	 * 
	 * @return  int
	 */
	
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age
	 * 
	 * @param age 
	 */
	
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets the dateOfBirth
	 * 
	 * @return  Date
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Sets the dateOfBirth
	 * 
	 * @param dateOfBirth 
	 */
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the lastName
	 * 
	 * @return  String
	 */
	
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the lastName
	 * 
	 * @param lastName 
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the lastName
	 * 
	 * @return  String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the lastName
	 * 
	 * @param lastName 
	 */
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the middleInitial
	 * 
	 * @return  String
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * Sets the middleInitial
	 * 
	 * @param middleInitial 
	 */
	
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	
	/**
	 * Returns full name in format 'LastName, FirstName MiddleInitial'
	 * @return String
	 */
	public String getName(){
		StringBuffer nameBuffer = new StringBuffer();
		nameBuffer.append(lastName);
		nameBuffer.append(", ");
		nameBuffer.append(firstName);
		if(middleInitial != null){
			nameBuffer.append(" ");
			nameBuffer.append(middleInitial);
		}
		this.name = nameBuffer.toString();
		return this.name;
	}

	/**
	 * Gets the ssn
	 * 
	 * @return Returns String
	 */
	
	public String getSsn() {
		return ssn;
	}

	/**
	 * Sets the ssn
	 * 
	 * @param ssn 
	 */
	
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

}
