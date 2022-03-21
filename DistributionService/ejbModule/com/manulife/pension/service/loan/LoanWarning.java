package com.manulife.pension.service.loan;

import java.util.List;

public class LoanWarning extends LoanMessage {

	private static final long serialVersionUID = 1L;

	public LoanWarning(LoanErrorCode id, LoanField field, List<String> params) {
		super(id, field, params);
	}

	public LoanWarning(LoanErrorCode id, LoanField field, String param) {
		super(id, field, param);
	}

	public LoanWarning(LoanErrorCode id, LoanField field, String[] params) {
		super(id, field, params);
	}

	public LoanWarning(LoanErrorCode id, LoanField field) {
		super(id, field);
	}

	public LoanWarning(LoanErrorCode id, String fieldName, List<String> params) {
		super(id, fieldName, params);
	}

	public LoanWarning(LoanErrorCode id, String fieldName, String param) {
		super(id, fieldName, param);
	}

	public LoanWarning(LoanErrorCode id, String fieldName, String[] params) {
		super(id, fieldName, params);
	}

	public LoanWarning(LoanErrorCode id, String fieldName) {
		super(id, fieldName);
	}

	public LoanWarning(LoanErrorCode id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.warning;
	}

}
