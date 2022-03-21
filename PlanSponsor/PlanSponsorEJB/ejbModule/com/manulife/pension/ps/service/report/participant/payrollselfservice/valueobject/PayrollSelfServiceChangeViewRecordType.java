package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

/**
 * Record Type for Payroll Self Service Change for View.
 *
 */
public enum PayrollSelfServiceChangeViewRecordType {
	
	ALL("ALL", "All"), DEFERRAL("DEFERRAL", "Deferral Rate Changes"), ENROLLMENT("ENROLLMENT", "Enrollments"),
	LOAN("LOAN", "Loans");

	private final String code; 
	private final String description;

	PayrollSelfServiceChangeViewRecordType(String code, String description) {
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
