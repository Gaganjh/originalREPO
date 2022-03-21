package com.manulife.pension.ps.web.report;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.NestableRuntimeException;


import com.manulife.pension.ps.web.util.CloneableForm;

/**
 * Base action form class for all report forms
 *
 * @see		ReportForm
 */
public abstract class ReportActionCloneableForm extends ReportForm
    implements Cloneable, CloneableForm {
	private static final long serialVersionUID = 2953622847130842147L;
    
    /**
 	* Default Constructor
 	*/
	public ReportActionCloneableForm() {
        super();
	}

    
//   Cloneable from here
    private CloneableForm clonedForm;

    public void clear( HttpServletRequest request) {
        clonedForm = null;
    }

    public CloneableForm getClonedForm() {
        return clonedForm;
    }

    public Object clone() {
        try {
            ReportActionCloneableForm obj = (ReportActionCloneableForm) super.clone();
            
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
