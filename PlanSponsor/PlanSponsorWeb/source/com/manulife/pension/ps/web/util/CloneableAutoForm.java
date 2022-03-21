package com.manulife.pension.ps.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * @author Charles Chan
 */
public abstract class CloneableAutoForm extends AutoForm
		implements
			Cloneable, CloneableForm {

	private CloneableForm clonedForm;

	public void clear( HttpServletRequest request) {
		clonedForm = null;
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		try {
			CloneableAutoForm obj = (CloneableAutoForm) super
					.clone();
			if (obj.clonedForm != null) {
				obj.clonedForm = (CloneableForm) clonedForm
						.clone();
			}
			return obj;
		} catch (CloneNotSupportedException e) {
			// This should not happen because we have made this class Cloneable.
			throw new NestableRuntimeException(e);
		}
	}

	public void storeClonedForm() {
		clonedForm = (CloneableForm) clone();
	}
}