package com.manulife.pension.ps.web.fee.util;

/**
 * 
 * This class is VO for Reconciliation tool fee Descriptions.
 * @author Eswar
 *
 */
public class PartStmtFeesDescription {
	private String shortDescription;
	private String longDescription;
	
	
	/**
	 * Constructor
	 * @param shortDesc
	 * @param longDesc
	 */
	PartStmtFeesDescription(String shortDesc ,String longDesc){
		this.longDescription = longDesc;
		this.shortDescription  = shortDesc;
	}

	/**
	 * @return shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * @param longDescription
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
}
