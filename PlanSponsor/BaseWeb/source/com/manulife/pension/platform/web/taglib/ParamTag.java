package com.manulife.pension.platform.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

/**
 * @author Charles Chan
 */
public class ParamTag extends TagSupport {

	private String name;
	private Object value;
	private String valueBeanName;
	private String valueBeanProperty;
	private String scope;

	/**
	 * Constructor.
	 */
	public ParamTag() {
		super();
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
	 * @return Returns the value.
	 */
	public String getValue() {
		return value.toString();
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the valueBeanName.
	 */
	public String getValueBeanName() {
		return valueBeanName;
	}

	/**
	 * @param valueBeanName
	 *            The valueBeanName to set.
	 */
	public void setValueBeanName(String valueBeanName) {
		this.valueBeanName = valueBeanName;
	}

	/**
	 * @return Returns the valueBeanProperty.
	 */
	public String getValueBeanProperty() {
		return valueBeanProperty;
	}

	/**
	 * @param valueBeanProperty
	 *            The valueBeanProperty to set.
	 */
	public void setValueBeanProperty(String valueBeanProperty) {
		this.valueBeanProperty = valueBeanProperty;
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

	public int doStartTag() throws JspException {

		ParamSupportTag paramSupportTag = ParamSupportTag
				.getEnclosingParamSupportTag(pageContext);

		if (value == null) {
			value = TagUtils.getInstance().lookup(pageContext, valueBeanName,
				valueBeanProperty, scope);
		}
		
		paramSupportTag.addParameter(name, value);

		return SKIP_BODY;
	}
}