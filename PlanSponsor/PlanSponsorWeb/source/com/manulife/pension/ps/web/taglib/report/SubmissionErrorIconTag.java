package com.manulife.pension.ps.web.taglib.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.Predicate;

import com.manulife.pension.platform.web.taglib.report.AbstractReportTag;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.submission.SubmissionError;

/**
 * @author Tony Tomasone
 *
 */
public class SubmissionErrorIconTag extends AbstractReportTag {

	private SubmissionErrorCollection errors;
	private String row = "0";
	private String codes;
	private String identifier; // used for copy warnings
    private String value;
    private String field;
	
	// HTML constants
	protected static final String ERROR_ICON = "<img src=\"/assets/unmanaged/images/error.gif\" alt=\"Error\">";
	protected static final String WARN_ICON = "<img src=\"/assets/unmanaged/images/warning2.gif\" alt=\"Warning\">";

	public int doStartTag() throws JspException {
		
		// first check for copy warnings
		boolean hasCopyIds = false;
		ArrayList copyIds = (ArrayList)pageContext.getSession().getAttribute(Constants.COPY_IDS);
		if ( copyIds != null && !copyIds.isEmpty() ) {
			hasCopyIds = true;
		}
		
		if ( (errors == null || errors.getErrors().size() == 0) && !hasCopyIds ) {
			return SKIP_BODY;
		}
		
		if ( (codes == null || codes.length() == 0) && !hasCopyIds ) {
			throw new JspException("Field name is mandatory");
		}
		
		try {

			if ( errors != null && codes != null ) {
				
				// we need to check if there are multiple fields to check at the same time
				ArrayList<String> codesToCheck = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(codes,",");
				while (tokenizer.hasMoreTokens()) {
					codesToCheck.add(tokenizer.nextToken());
				}
				
				boolean isError = SubmissionErrorHelper
						.evaluateMatchedField(field, value, Integer
								.parseInt(row), errors, codesToCheck,
								new Predicate() {
									public boolean evaluate(Object object) {
										SubmissionError error = (SubmissionError) object;
										return SubmissionErrorHelper
												.isError(error);
									}
								});

				if (isError) {
					pageContext.getOut().print(ERROR_ICON);
					return SKIP_BODY;
				}

				boolean isWarning = SubmissionErrorHelper
						.evaluateMatchedField(field, value, Integer
								.parseInt(row), errors, codesToCheck,
								new Predicate() {
									public boolean evaluate(Object object) {
										SubmissionError error = (SubmissionError) object;
										return SubmissionErrorHelper
												.isWarning(error);
									}
								});

				if (isWarning) {
					pageContext.getOut().print(WARN_ICON);
					return SKIP_BODY;
				}
			}
			
			// finally, check for copy warnings (if applicable)
			if ( hasCopyIds ) {
				if (copyIds.contains(identifier)) {
					// match, add a warning icon and remove the element from the array list
					pageContext.getOut().print(WARN_ICON);
					copyIds.remove(identifier);
					return SKIP_BODY;
				}
			}
			

		} catch (MalformedURLException e) {
			throw new JspException(e.getMessage());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}
	/**
	 * @return Returns the errors.
	 */
	public SubmissionErrorCollection getErrors() {
		return errors;
	}
	/**
	 * @param errors The errors to set.
	 */
	public void setErrors(SubmissionErrorCollection errors) {
		this.errors = errors;
	}

	/**
	 * @return Returns the row.
	 */
	public String getRow() {
		return row;
	}
	/**
	 * @param row The row to set.
	 */
	public void setRow(String row) {
		this.row = row;
	}
	/**
	 * @return Returns the codes.
	 */
	public String getCodes() {
		return codes;
	}
	/**
	 * @param codes The codes to set.
	 */
	public void setCodes(String codes) {
		this.codes = codes;
	}
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
}
