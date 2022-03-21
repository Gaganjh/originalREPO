package com.manulife.pension.ps.administration;

import java.util.ArrayList;

public class LdapUser {

	private String userName;
	private String profileId;
	private ArrayList manAnswer;
	private String manChallengeAnswer;
	private String manNumericPassword;
	private String manProfileStatus;
	private String pin;
	private String pinStatus;
	private String ontChallenge;

	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	/**
	 * Gets the userName
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * Sets the userName
	 * @param userName The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the manAnswer
	 * @return Returns a String
	 */
	public ArrayList getManAnswer() {
		return manAnswer;
	}
	/**
	 * Sets the manAnswer
	 * @param manAnswer The manAnswer to set
	 */
	public void setManAnswer(ArrayList manAnswer) {
		this.manAnswer = manAnswer;
	}

	/**
	 * Gets the manChallengeAnswer
	 * @return Returns a String
	 */
	public String getManChallengeAnswer() {
		return manChallengeAnswer;
	}
	/**
	 * Sets the manChallengeAnswer
	 * @param manChallengeAnswer The manChallengeAnswer to set
	 */
	public void setManChallengeAnswer(String manChallengeAnswer) {
		this.manChallengeAnswer = manChallengeAnswer;
	}

	/**
	 * Gets the manNumericPassword
	 * @return Returns a String
	 */
	public String getManNumericPassword() {
		return manNumericPassword;
	}
	/**
	 * Sets the manNumericPassword
	 * @param manNumericPassword The manNumericPassword to set
	 */
	public void setManNumericPassword(String manNumericPassword) {
		this.manNumericPassword = manNumericPassword;
	}

	/**
	 * Gets the manProfileStatus
	 * @return Returns a String
	 */
	public String getManProfileStatus() {
		return manProfileStatus;
	}
	/**
	 * Sets the manProfileStatus
	 * @param manProfileStatus The manProfileStatus to set
	 */
	public void setManProfileStatus(String manProfileStatus) {
		this.manProfileStatus = manProfileStatus;
	}

	/**
	 * Gets the pinStatus
	 * @return Returns a String
	 */
	public String getPinStatus() {
		return pinStatus;
	}
	/**
	 * Sets the pinStatus
	 * @param pinStatus The pinStatus to set
	 */
	public void setPinStatus(String pinStatus) {
		this.pinStatus = pinStatus;
	}

	/**
	 * Gets the pin
	 * @return Returns a String
	 */
	public String getPin() {
		return pin;
	}
	/**
	 * Sets the pin
	 * @param pin The pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	public void setOntChallenge(String ontChallenge)
	{
		this.ontChallenge = ontChallenge;
	}

	public String getOntChallenge()
	{
		return ontChallenge;
	}

	public boolean equals(LdapUser otherUser)
	{
		if(this.userName.equals(otherUser.getUserName())
			&& this.profileId.equals(otherUser.getProfileId())
			&& this.getPin().equals(otherUser.getPin())
			&& this.getPinStatus().equals(otherUser.getPinStatus())
			
			&& this.getManChallengeAnswer().equals(otherUser.getManChallengeAnswer())
			&& this.getManNumericPassword().equals(otherUser.getManNumericPassword())
			&& this.getManProfileStatus().equals(otherUser.getManProfileStatus())
			&& this.getOntChallenge().equals(otherUser.getOntChallenge()))
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	private boolean equalManAnswersAttribute(LdapUser otherUser)
	{
		boolean returnValue = false;
		if(manAnswer.size() != otherUser.getManAnswer().size())
		{
			return false;
		}
		
		for(int i = 0; i < otherUser.getManAnswer().size(); i++)
		{
			if(manAnswer.contains(otherUser.getManAnswer().get(i)))
			{
				returnValue = true;
			}
			else
			{
				returnValue = false;
				break;
			}
		}
		return returnValue;
	}
	public String toString()
	{
		StringBuffer value = new StringBuffer();
		value.append("UserName=").append(userName);
		value.append(",profileId=").append(profileId);
		value.append(",manAnswer=").append( manAnswer);
		value.append(",manChallengeAnswer=").append(manChallengeAnswer);
		value.append(",manNumericPassword=").append(manNumericPassword);
		value.append(",manProfileStatus=").append(manProfileStatus);
		value.append(",pin=").append(pin);
		value.append(",pinStatus=").append(pinStatus);
		value.append(",ontChallenge=").append(ontChallenge);
		return value.toString();
	}
}

