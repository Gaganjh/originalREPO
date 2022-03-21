package com.manulife.pension.service.loan;

import java.util.List;

public class LoanError extends LoanMessage {

	private static final long serialVersionUID = 1L;

	public LoanError(LoanErrorCode id, LoanField field, List<String> params) {
		super(id, field, params);
	}

	public LoanError(LoanErrorCode id, LoanField field, String param) {
		super(id, field, param);
	}

	public LoanError(LoanErrorCode id, LoanField field, String[] params) {
		super(id, field, params);
	}

	public LoanError(LoanErrorCode id, LoanField field) {
		super(id, field);
	}

	public LoanError(LoanErrorCode id, String fieldName, List<String> params) {
		super(id, fieldName, params);
	}

	public LoanError(LoanErrorCode id, String fieldName, String param) {
		super(id, fieldName, param);
	}

	public LoanError(LoanErrorCode id, String fieldName, String[] params) {
		super(id, fieldName, params);
	}

	public LoanError(LoanErrorCode id, String fieldName) {
		super(id, fieldName);
	}

	public LoanError(LoanErrorCode id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.error;
	}

}
