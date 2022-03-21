package com.manulife.pension.ps.administration.valueobject;

import java.io.Serializable;

/**
 * @author celikil
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TpaUserKey implements Serializable {
	private String firmId;
	private String ssn;
	
	public TpaUserKey(String firmId, String ssn)
	{
		this.firmId = firmId;
		this.ssn = ssn;
	}

	/**
	 * @return Returns the firmId.
	 */
	public String getFirmId() {
		return firmId;
	}
	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}
	
	public boolean equals(Object obj)
	{
		TpaUserKey otherTpaUser = (TpaUserKey)obj;
		if ( firmId.trim().equals(otherTpaUser.getFirmId().trim()) && 
				ssn.trim().equals(otherTpaUser.getSsn().trim()) )
			return true;
		else
			return false;
	}
	
	public int hashCode()
	{
		return (Integer.parseInt(firmId) + Integer.parseInt(ssn));
	}		
}
