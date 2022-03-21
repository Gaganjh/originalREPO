package com.manulife.pension.ps.web.investment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;


import com.manulife.pension.platform.web.investment.IPSAndReviewDetailsForm;
import com.manulife.pension.ps.web.util.CloneableForm;

public class IPSAndReviewForm extends IPSAndReviewDetailsForm implements
	Cloneable, CloneableForm {

	private static final long serialVersionUID = 1L;
	private IPSAndReviewForm clonedForm;

	public void clear( HttpServletRequest request) {
		clonedForm = null;
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		try {
			IPSAndReviewForm obj = (IPSAndReviewForm) super.clone();
			if (obj.clonedForm != null) {
				obj.clonedForm = (IPSAndReviewForm) clonedForm
						.clone();
			}
			return obj;
		} catch (CloneNotSupportedException e) {
			// This should not happen because we have made this class Cloneable.
			throw new NestableRuntimeException(e);
		}
	}

	public void storeClonedForm() {
		clonedForm = (IPSAndReviewForm) clone();
	}
}
