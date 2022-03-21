package com.manulife.pension.ps.web.participant.payrollSelfService;

public enum PayrollFeedbackServiceType {
	
	PSS("PSS", "Payroll Self-Service"), PTS("PTS", "Payroll 360"), NS("NS", "Not Selected"), NA("NA", "Not Applicable");

	private final String code;
	private final String description;

	PayrollFeedbackServiceType(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

}
