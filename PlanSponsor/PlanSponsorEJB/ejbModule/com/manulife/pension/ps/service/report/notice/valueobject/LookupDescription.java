package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class will hold the Reason Codes.
 * 
 * @author krishta
 * 
 */
public class LookupDescription implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String lookupCode;
	private String lookupDesc;
	
	public LookupDescription() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param lookupCode
	 * @param lookupDesc
	 */
	public LookupDescription(String lookupCode, String lookupDesc) {
		this.lookupCode = lookupCode;
		this.lookupDesc = lookupDesc;
	}

	/**
	 * @return the lookupCode
	 */
	public String getLookupCode() {
		return lookupCode;
	}

	/**
	 * @param lookupCode the lookupCode to set
	 */
	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}

	/**
	 * @return the lookupDesc
	 */
	public String getLookupDesc() {
		return lookupDesc;
	}

	/**
	 * @param lookupDesc the lookupDesc to set
	 */
	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
	}
}
