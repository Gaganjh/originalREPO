package com.manulife.pension.platform.web.taglib;

/**
 * This tag checks if the supplied object (retrieved from name + property) is
 * an instance of the supplied type. The result of the evaluation is stored
 * in "var" as a Boolean object.
 * 
 * @author Charles Chan
 */
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

public class InstanceOfTag extends TagSupport {

	private String var;

	private String name;

	private String property;

	private String type;

	private String scope;

	public int doStartTag() throws JspException {

		Object value = TagUtils.getInstance().lookup(pageContext, name, property, scope);
		boolean instanceOf = false;
		try {
			Class c = Class.forName(type, true, Thread.currentThread()
					.getContextClassLoader());
			instanceOf = c.isInstance(value);
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		pageContext.setAttribute(var, new Boolean(instanceOf));
		return SKIP_BODY;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}