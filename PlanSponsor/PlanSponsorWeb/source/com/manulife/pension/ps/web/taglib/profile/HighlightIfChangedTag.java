package com.manulife.pension.ps.web.taglib.profile;

import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;

import com.manulife.pension.platform.web.taglib.util.BaseHandlerTag;
import com.manulife.pension.platform.web.taglib.util.Constants;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.ps.web.util.CloneableForm;

/**
 * @author Charles Chan
 */
public class HighlightIfChangedTag extends BaseHandlerTag {

	private String name;
	private String property;
	private String scope;
	private boolean changed;
	private String text;
	private String indexPrefix;

	/**
	 * Constructor.
	 */
	public HighlightIfChangedTag() {
		super();
		setStyleClass("highlightBold");
	}

	public int doStartTag() throws JspException {
		if (name == null) {
			name = Constants.BEAN_KEY;
		}

		StringBuffer propertyString = new StringBuffer();
		if (indexPrefix != null) {
			prepareIndex(propertyString, indexPrefix);
		}
		propertyString.append(property);

		Object bean = TagUtils.getInstance().lookup(pageContext, name, scope);
		if (!(bean instanceof CloneableForm)) {
			throw new JspException("[" + bean.getClass()
                    + "] is not cloneable. "
                    + "Only CloneableAutoForm is supported!");
		}
		CloneableForm currentForm = (CloneableForm) bean;

		changed = false;

		if (currentForm.getClonedForm() != null) {
			Object currentValue = null;
			Object clonedValue = null;

			try {
				currentValue = PropertyUtils.getProperty(currentForm,
						propertyString.toString());
			} catch (Exception e) {
				throw new JspException(e.getMessage());
			}

			try {
				clonedValue = PropertyUtils.getProperty(currentForm
						.getClonedForm(), propertyString.toString());
			} catch (Exception e) {
				// do nothing, assume clonedValue is null.
			}

			if (currentValue == null) {
				if (clonedValue != null) {
					changed = true;
				}
			} else if (clonedValue == null) {
				if (currentValue != null) {
					changed = true;
				}
			} else if (!currentValue.equals(clonedValue)) {
				changed = true;
			}

			if (changed) {
				TagUtils.getInstance().write(pageContext, "<span class=\""
						+ getStyleClass() + "\">");
			}
		}
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

		if (changed) {
			results.append("</span>");
			changed = false;
		}
		TagUtils.getInstance().write(pageContext, results.toString());
		return EVAL_PAGE;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the scope.
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope
	 *            The scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
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