
package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * Highlight the input field for error/warning/alert for Iterated fields
 * @author Tamilarasu Krishnamoorthy
 *
 */
public class IteratorFieldHighlightTag extends FieldHighlightTag {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	private Integer index;

	private String type;

	private Logger logger = Logger.getLogger(IteratorFieldHighlightTag.class);


	private static final String START_LIST = "<ul>";

	private static final String END_LIST = "</ul>";

	private static final String LIST_ELEMENT_START = "<li>";

	private static final String LIST_ELEMENT_END = "</li>";

	private static final String ACTIVITY_HISTORY_IMAGE_SOURCE = "/assets/unmanaged/images/arrow_green.gif";
	/**
	 * Display Error Symbol for the iterated set of fields
	 */
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		Collection<GenericException> errors = null;
        List<ObjectError>  errorList =null;
        Object obj =  pageContext.findAttribute(PsBaseUtil.ERROR_KEY);
        if( obj instanceof Collection){
        	errors = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
    		errors=new ArrayList<GenericException>();
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
			for(ObjectError objError:errorList){
				 if (objError != null) {
					 errors.add(new ValidationError(new String[0], Integer.parseInt(objError.getCode()), objError.getArguments(),
	                            ValidationError.Type.error));
               }
				
			}
		}
        
		boolean hasError = false;
		boolean hasWarning = false;
		boolean hasAlert = false;

		ValidationError fieldError = null;
		final Collection<GenericException> fieldErrors = new ArrayList<GenericException>();
		if (errors != null && errors.size() > 0) {
			for (Iterator it = errors.iterator(); it.hasNext() && fieldError == null;) {
				Object error = it.next();
				if (error instanceof ValidationError) {
					ValidationError validationError = (ValidationError) error;
					Object[] string= validationError.getParams();
					java.util.List list = Arrays.asList(string);

					if (validationError.hasFieldId(getName()) 
							&& list.get(0).equals(index.toString()) 
							&& list.get(1).equals(type)) {
						switch (validationError.getType()) {
						case error:
							hasError = true;
							break;
						case warning:
							hasWarning = true;
							break;
						case alert:
							hasAlert = true;
							break;
						default:
							throw new NotImplementedException("Validation Type not supported: "
									+ validationError.getType());
						} // end switch

						// If tooltips is on - add to our list of errors
						if (BooleanUtils.isTrue(getDisplayToolTip())) {
							fieldErrors.add((GenericException) error);
						}
					} // fi
				} // fi
			} // end for
		} // fi

		// If tooltips is on - retrieve the list of error messages and add to our buffer
		if (BooleanUtils.isTrue(getDisplayToolTip())) {
			final Collection<String> errorMessages;
			try {
				errorMessages = MessageProvider.getInstance().getMessageCollection(fieldErrors);
			} catch (ContentException contentException) {
				logger.warn("Got an exception while processing tag", contentException);
				throw new JspException("Got an exception while processing tag", contentException);
			} // end try/catch
			getToolTip().append(START_LIST);
			for (String message : errorMessages) {
				getToolTip().append(LIST_ELEMENT_START).append(
						escapeJavaScript(message)).append(LIST_ELEMENT_END);
			}
			getToolTip().append(END_LIST);
		}

		// Check the special case where we only display one icon.
		if (BooleanUtils.isTrue(getSingleDisplay())) {
			// Only one error type will be displayed (if existing), so we set the others
			// values to false.
			if (hasError) {
				hasWarning = false;
				hasAlert = false;
			} else if (hasWarning) {
				hasAlert = false;
			} // fi
		} // fi

		if (hasError || hasWarning || hasAlert || getDisplayActivityHistory()) {
			final JspWriter jspWriter = pageContext.getOut();
			try {
				if (hasError) {
					writeImage(jspWriter, getImageSrc(Type.error), getStyleIdPrefix(Type.error), null);
				} // fi
				if (hasWarning) {
					writeImage(jspWriter, getImageSrc(Type.warning), getStyleIdPrefix(Type.warning), null);
				} // fi
				if (hasAlert) {
					writeImage(jspWriter, getImageSrc(Type.alert), getStyleIdPrefix(Type.alert), null);
				} // fi
				if (getDisplayActivityHistory()) {
					boolean hideActivityIcon = false;
					if (BooleanUtils.isTrue(getSingleDisplay()) && (hasError
							|| hasWarning || hasAlert)) {
						hideActivityIcon = true;
					}
					if (hideActivityIcon) {
						writeImage(jspWriter, ACTIVITY_HISTORY_IMAGE_SOURCE, "activityIcon_", "display:none");
					} else {
						writeImage(jspWriter, ACTIVITY_HISTORY_IMAGE_SOURCE, "activityIcon_", null);
					}
				} // fi
			} catch (IOException ioException) {
				throw new JspException(ioException);
			} // end try/catch
			return SKIP_BODY;
		} else {
			return EVAL_PAGE;
		} // fi
	}
	
	/**
	 * Get the list index field
	 * @return  Integer
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Set the list Index 
	 * @param index
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Get the Type of list
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the Type of list
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}


}
