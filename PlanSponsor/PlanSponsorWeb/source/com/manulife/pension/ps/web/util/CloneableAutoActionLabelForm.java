package com.manulife.pension.ps.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;


import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;

public class CloneableAutoActionLabelForm extends PsAutoActionLabelForm
		implements Cloneable, CloneableForm {

	private static final long serialVersionUID = 1L;

	private CloneableAutoActionLabelForm clonedForm;

	public void clear( HttpServletRequest request) {
		clonedForm = null;
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		try {
			CloneableAutoActionLabelForm obj = (CloneableAutoActionLabelForm) super
					.clone();
			if (obj.clonedForm != null) {
				obj.clonedForm = (CloneableAutoActionLabelForm) clonedForm
						.clone();
			}
			return obj;
		} catch (CloneNotSupportedException e) {
			// This should not happen because we have made this class Cloneable.
			throw new NestableRuntimeException(e);
		}
	}

	public void storeClonedForm() {
		clonedForm = (CloneableAutoActionLabelForm) clone();
	}
}
