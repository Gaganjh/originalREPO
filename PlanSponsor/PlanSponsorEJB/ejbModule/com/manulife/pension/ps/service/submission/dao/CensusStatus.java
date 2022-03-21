package com.manulife.pension.ps.service.submission.dao;

class CensusStatus {
	
	private String statusCode;
	private String displayStatus;
	private int displayOrder;

	public CensusStatus(String statusCode, String displayStatus,
			int displayOrder) {
		this.statusCode = statusCode;
		this.displayStatus = displayStatus;
		this.displayOrder = displayOrder;
	}

	/**
	 * @return Returns the displayOrder.
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @return Returns the displayStatus.
	 */
	public String getDisplayStatus() {
		return displayStatus;
	}

	/**
	 * @return Returns the statusCode.
	 */
	public String getStatusCode() {
		return statusCode;
	}
}