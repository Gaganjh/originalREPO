package com.manulife.pension.bd.web.taglib.investment;

import javax.servlet.jsp.JspException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.manulife.pension.platform.web.taglib.util.BaseHandlerTag;
import com.manulife.pension.platform.web.taglib.util.Constants;
import com.manulife.pension.platform.web.taglib.util.TagUtils;


import com.manulife.pension.bd.web.bob.investment.CloneableForm;

/**
 * @author Charles Chan
 */
public class TrackChangesTag extends BaseHandlerTag {

	private String name;

	private String cloneName;

	private String changeTrackerVariable = "changeTracker";

	private String property;

	private String scope;

	private String indexPrefix;

	private String addHiddenField;

	private String value;

	private boolean escape = false;

	private static final String lineBreak = System
			.getProperty("line.separator");

	/**
	 * Constructor.
	 */
	public TrackChangesTag() {
		super();
	}

	public int doStartTag() throws JspException {
		if (name == null) {
			name = Constants.BEAN_KEY;
		}
		Object bean = TagUtils.getInstance().lookup(pageContext, name, scope);
		Object clonedBean = null;

		if (cloneName != null) {
			clonedBean = TagUtils.getInstance().lookup(pageContext, cloneName,
					scope);
		} else {
			if (!(bean instanceof CloneableForm)) {
				throw new JspException(
						"Only ChangeTrackedForm is supported!");
			}
			CloneableForm currentForm = (CloneableForm) bean;
			clonedBean = currentForm.getClonedForm();
		}

		StringBuffer result = new StringBuffer(
				"<script language=\"javascript\">");
		result.append(lineBreak);

		StringBuffer propertyString = new StringBuffer();
		if (indexPrefix != null) {
			prepareIndex(propertyString, indexPrefix);
		}
		propertyString.append(property);

		Object clonedValue = null;
		
		if (value != null) {
			clonedValue = value;
		} else {
			if (clonedBean != null) {
				try {
					clonedValue = PropertyUtils.getProperty(clonedBean,
							propertyString.toString());
				} catch (Exception e) {
					// do nothing, assume clonedValue is null.
				}
			}
		}
		
		if (clonedValue == null) {
			result.append(changeTrackerVariable).append(".trackElement('")
					.append(propertyString.toString()).append("',null);");
		} else {
			if (isEscape()) {
				clonedValue = StringEscapeUtils.escapeJavaScript(clonedValue
						.toString());
			}
			result.append(changeTrackerVariable).append(".trackElement('")
					.append(propertyString.toString()).append("','").append(
							clonedValue.toString().replaceAll("'","\\\\'")).append("');");
		}
		result.append(lineBreak);

		result.append("</script>").append(lineBreak);

		if (Boolean.valueOf(addHiddenField).booleanValue()) {
			if (clonedValue != null) {
				result.append("<input type=\"hidden\" name=\"").append(
						"clonedForm.").append(propertyString).append(
						"\" value=\"").append(clonedValue).append("\"/>")
						.append(lineBreak);
			}
		}

		TagUtils.getInstance().write(pageContext, result.toString());

		return EVAL_BODY_TAG;
	}

	/**
	 * @return Returns the changeTracker.
	 */
	public String getChangeTrackerVariable() {
		return changeTrackerVariable;
	}

	/**
	 * @param changeTracker
	 *            The changeTracker to set.
	 */
	public void setChangeTrackerVariable(String changeTracker) {
		this.changeTrackerVariable = changeTracker;
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
	 * @return Returns the cloneName.
	 */
	public String getCloneName() {
		return cloneName;
	}

	/**
	 * @param cloneName
	 *            The cloneName to set.
	 */
	public void setCloneName(String cloneName) {
		this.cloneName = cloneName;
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

	/**
	 * @return Returns the addHiddenField.
	 */
	public String getAddHiddenField() {
		return addHiddenField;
	}

	/**
	 * @param addHiddenField
	 *            The addHiddenField to set.
	 */
	public void setAddHiddenField(String addHiddenField) {
		this.addHiddenField = addHiddenField;
	}

	public boolean isEscape() {
		return escape;
	}

	/**
	 * If the value should be escaped for javascript
	 * 
	 * @param escape
	 */
	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public String getValue() {
		return value;
	}

	/**
	 * Use this value for change tracking instead of the ones retrieved from the
	 * property.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}