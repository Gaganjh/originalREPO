package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;

/**
 * @author parkand
 */
public class MoneySource implements Serializable {
	
	private String sourceCode;
	private String displayName;
	private String transactionCode;
	private String userFileTypeCode;
	
	public MoneySource(String sourceCode, String displayName, String transactionCodeForCSV, String userFileTypeCode) {
		this.displayName = displayName;
		this.sourceCode = sourceCode;
		this.transactionCode = transactionCodeForCSV;
		this.userFileTypeCode = userFileTypeCode;
	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @return Returns the sourceCode.
	 */
	public String getSourceCode() {
		return sourceCode;
	}
	/**
	 * @return Returns the transactionCode.
	 */
	public String getTransactionCode() {
		return transactionCode;
	}
	/**
	 * @return Returns the userFileTypeCode.
	 */
	public String getUserFileTypeCode() {
		return userFileTypeCode;
	}
}
