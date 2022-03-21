package com.manulife.pension.service.loan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanMessage extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	public static enum Type {
		error, warning, alert
	};

	private LoanErrorCode errorCode;

	private List<String> fieldNames = new ArrayList<String>();

	private List<String> params = new ArrayList<String>();

	public Type getType() {
		return Type.alert;
	}

	public LoanMessage(LoanErrorCode id) {
		this.errorCode = id;
	}

	public LoanMessage(LoanErrorCode id, LoanField field) {
		this(id, field.getFieldName());
	}

	public LoanMessage(LoanErrorCode id, String fieldName) {
		this.errorCode = id;
		this.fieldNames.add(fieldName);
	}

	public LoanMessage(LoanErrorCode id, LoanField field, List<String> params) {
		this(id, field.getFieldName(), params);
	}

	public LoanMessage(LoanErrorCode id, String fieldName, List<String> params) {
		this.errorCode = id;
		this.params.addAll(params);
		this.fieldNames.add(fieldName);
	}

	public LoanMessage(LoanErrorCode id, LoanField field, String[] params) {
		this(id, field.getFieldName(), params);
	}

	public LoanMessage(LoanErrorCode id, String fieldName, String[] params) {
		this.errorCode = id;
		if (params != null) {
			for (String param : params) {
				this.params.add(param);
			}
		}
		this.fieldNames.add(fieldName);
	}

	public LoanMessage(LoanErrorCode id, LoanField field, String param) {
		this(id, field.getFieldName(), param);
	}

	public LoanMessage(LoanErrorCode id, String fieldName, String param) {
		this.errorCode = id;
		if (params != null) {
			this.params.add(param);
		}
		this.fieldNames.add(fieldName);
	}

	public LoanErrorCode getErrorCode() {
		return errorCode;
	}

	public List<String> getParams() {
		return params;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		if (errorCode != null) {
			for (LoanErrorCode code : LoanErrorCode.values()) {
				if (code.name().equals(errorCode.name())) {
					errorCode = code;
					break;
				}
			}
		}
	}
}
