package com.manulife.pension.ps.web.taglib.profile;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.taglib.util.BaseHandlerTag;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.validator.ValidationError;

/**
 * @author Charles Chan
 */
public class LabelTag extends BaseHandlerTag {

	private String fieldId;

	private String text;

	private String mandatory;

	private boolean hasError;

	private String errorStyleClass;


	private String indexPrefix;

	/**
	 * Constructor.
	 */
	public LabelTag() {
		super();
		setErrorStyleClass("redText");
	}

	public int doStartTag() throws JspException {

		Collection errors = (Collection) TagUtils.getInstance().lookup(pageContext,
				PsBaseUtil.ERROR_KEY, "request");

		StringBuffer result = new StringBuffer("<strong>");
		hasError = false;

		StringBuffer propertyString = new StringBuffer();
		if (indexPrefix != null) {
			prepareIndex(propertyString, indexPrefix);
		}
		propertyString.append(fieldId);

		fieldId = propertyString.toString();

		if (errors != null && errors.size() > 0) {
			for (Iterator it = errors.iterator(); it.hasNext() && !hasError;) {
				Object error = it.next();
				if (error instanceof ValidationError) {
					ValidationError validationError = (ValidationError) error;
					if (validationError.hasFieldId(fieldId)) {
						hasError = true;
					}
				}
			}
		}

		if (hasError) {
			result.append("<span class=\"").append(getErrorStyleClass())
					.append("\" id=\"label_" + fieldId + "\">");
		} else {
			result.append("<span id=\"label_" + fieldId + "\">");
		}

		TagUtils.getInstance().write(pageContext, result.toString());

		return EVAL_BODY_TAG;
	}

	public int doAfterBody() throws JspException {
		if (bodyContent != null) {
			String value = bodyContent.getString().trim();
			if (value.length() > 0)
				text = value;
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		StringBuffer results = new StringBuffer();
		if (text != null)
			results.append(text);

		if (isMandatory()) {
			results.append("&nbsp;<font color=\"#CC6600\">*</font>");
		}

		results.append("</span>");

		if (hasError) {
			hasError = false;
		}
		results.append("</strong>");
		TagUtils.getInstance().write(pageContext, results.toString());
		return EVAL_PAGE;
	}

	/**
	 * @return Returns the name.
	 */
	public String getFieldId() {
		return fieldId;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setFieldId(String name) {
		this.fieldId = name;
	}

	/**
	 * @return Returns the errorStyleClass.
	 */
	public String getErrorStyleClass() {
		return errorStyleClass;
	}

	/**
	 * @param errorStyleClass
	 *            The errorStyleClass to set.
	 */
	public void setErrorStyleClass(String errorStyleClass) {
		this.errorStyleClass = errorStyleClass;
	}

	private boolean isMandatory() {
		return mandatory == null ? false : Boolean.valueOf(mandatory)
				.booleanValue();
	}

	/**
	 * @return Returns the required.
	 */
	public String getMandatory() {
		return mandatory;
	}

	/**
	 * @param required
	 *            The required to set.
	 */
	public void setMandatory(String required) {
		this.mandatory = required;
	}

	/**
	 * @return Returns the indexPrefix.
	 */
	public String getIndexPrefix() {
		return indexPrefix;
	}

	/**
	 * @param indexPrefix
	 *            The indexPrefix to set.
	 */
	public void setIndexPrefix(String indexPrefix) {
		this.indexPrefix = indexPrefix;
	}
}