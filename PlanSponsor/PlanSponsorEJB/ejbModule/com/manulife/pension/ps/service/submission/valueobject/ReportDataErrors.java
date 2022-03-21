package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.submission.util.SubmissionErrorComparator;

/**
 * @author Andrew Park
 */
public class ReportDataErrors implements Serializable {
	
	public static final String ERROR_COND_STRING_OK = "OK";
 
	Collection errors = new TreeSet(new SubmissionErrorComparator()); // sorted collection of submission errors for entire report
	int numErrors = 0;
	int numWarnings = 0;
	int numSyntaxErrors = 0;
	boolean discardFlag = false;
	boolean syntaxErrorIndicator = false; // retrieved from SUBMISSION_CASE.SYNTAX_ERROR_IND

	public ReportDataErrors() {
		super();
	}
    
    public ReportDataErrors(boolean sort) {
        super();
        if (!sort) {
            errors = new TreeSet();                                // do not sort collection
        } else {
            errors = new TreeSet(new SubmissionErrorComparator()); // sort collection
        }
    }
	
	public ReportDataErrors(String errorCondString) {
		this();
		if (null != errorCondString && !ERROR_COND_STRING_OK.equals(errorCondString) && 
				!"".equals(errorCondString)) {
			addErrors(SubmissionErrorHelper.parseErrorConditionString(errorCondString, 0));
		}
	}
    
    public ReportDataErrors(String errorCondString, boolean sort) {
        this(sort);
        if (null != errorCondString && !ERROR_COND_STRING_OK.equals(errorCondString) && 
                !"".equals(errorCondString)) {
            addErrors(SubmissionErrorHelper.parseErrorConditionString(errorCondString, 0));
        }
    }


	/**
	 * Adds a collection of submission errors associated with a single record 
	 * to the aggregate collection of errors for the entire ReportData
	 * 
	 * @param submissionErrors
	 */
	public void addErrors(Collection submissionErrors) {

		// iterate through the submission errors
		Iterator iter = submissionErrors.iterator();
		while (iter.hasNext()) {
			SubmissionError error = (SubmissionError)iter.next();
			// add check for duplicate errors
			if ( !errors.contains(error) ) {
				errors.add(error);
				if (error.isError()) {
					numErrors++;
				} else if (error.isWarning()) {
					numWarnings++;
				}
			}
		}
	}

	/**
	 * @return Returns the discardFlag.
	 */
	public boolean isDiscardFlag() {
		return discardFlag;
	}
	/**
	 * @param discardFlag The discardFlag to set.
	 */
	public void setDiscardFlag(boolean discardFlag) {
		this.discardFlag = discardFlag;
	}
	/**
	 * @return Returns the errors.
	 */
	public Collection getErrors() {
		return errors;
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(Collection errors) {
		this.errors = errors;
	}
	/**
	 * @return Returns the numErrors.
	 */
	public int getNumErrors() {
		return numErrors;
	}
	/**
	 * @param numErrors The numErrors to set.
	 */
	public void setNumErrors(int numErrors) {
		this.numErrors = numErrors;
	}
	/**
	 * @return Returns the numSyntaxErrors.
	 */
	public int getNumSyntaxErrors() {
		return numSyntaxErrors;
	}
	/**
	 * @param numSyntaxErrors The numSyntaxErrors to set.
	 */
	public void setNumSyntaxErrors(int numSyntaxErrors) {
		this.numSyntaxErrors = numSyntaxErrors;
	}
	/**
	 * @return Returns the numWarnings.
	 */
	public int getNumWarnings() {
		return numWarnings;
	}
	/**
	 * @param numWarnings The numWarnings to set.
	 */
	public void setNumWarnings(int numWarnings) {
		this.numWarnings = numWarnings;
	}
	/**
	 * @return Returns the syntaxErrorIndicator.
	 */
	public boolean isSyntaxErrorIndicator() {
		return syntaxErrorIndicator;
	}
	/**
	 * @param syntaxErrorIndicator The syntaxErrorIndicator to set.
	 */
	public void setSyntaxErrorIndicator(boolean syntaxErrorIndicator) {
		this.syntaxErrorIndicator = syntaxErrorIndicator;
	}
}
