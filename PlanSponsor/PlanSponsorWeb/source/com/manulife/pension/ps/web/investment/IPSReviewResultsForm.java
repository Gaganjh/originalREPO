package com.manulife.pension.ps.web.investment;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;


import com.manulife.pension.platform.web.investment.IPSReviewResultDetailsForm;
import com.manulife.pension.ps.web.util.CloneableForm;

/**
 * IPS Review Results Form
 * 
 * @author Karthik
 *
 */
public class IPSReviewResultsForm extends IPSReviewResultDetailsForm implements
		Cloneable, CloneableForm {

	private static final long serialVersionUID = 1L;
	private IPSReviewResultsForm clonedForm;

	public void clear( HttpServletRequest request) {
		clonedForm = null;
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		try {
			IPSReviewResultsForm obj = (IPSReviewResultsForm) super.clone();
			if (obj.clonedForm != null) {
				obj.clonedForm = (IPSReviewResultsForm) clonedForm
						.clone();
			}
			return obj;
		} catch (CloneNotSupportedException e) {
			// This should not happen because we have made this class Cloneable.
			throw new NestableRuntimeException(e);
		}
	}

	public void storeClonedForm() {
		clonedForm = (IPSReviewResultsForm) clone();
	}

}
