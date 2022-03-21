package com.manulife.pension.ps.service.report.participant.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;

public class ParticipantDeferralUpdateDetails implements Serializable {
	

	private Timestamp changeTS;
	private double profileId;
	private boolean processInd;
	private DecimalFormat decimalFormatter;
	
	public ParticipantDeferralUpdateDetails() {}
	

	public ParticipantDeferralUpdateDetails( 
							  		 	Timestamp changeTS,
							  		 	double profileId,
							  			boolean processInd)
	{
		this.changeTS = changeTS;
		this.profileId = profileId;
		this.processInd = processInd;
	}
	
			


	/**
	 * Gets the changeTS
	 * @return Returns a Timestamp
	 */
	public Timestamp getChangeTS() {
		return changeTS;
	}
	/**
	 * Sets the changeTS
	 * @param changeTS The changeTS to set
	 */
	public void setChangeTS(Timestamp changeTS) {
		this.changeTS = changeTS;
	}

	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(double profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the profile id
	 * @return Returns a double
	 */
	public double getProfileId() {
		
		return profileId;
	}
	
	/**
	 * Sets the process Indicator
	 * @param processInd The processInd to set
	 */
	public void setProcessInd(boolean processInd) {
		this.processInd = processInd;
	}
	
	/**
	 * Gets the process Ind
	 * @return Returns a boolean
	 */
	public boolean getProcessInd() {
		return processInd;
	}
	
}

