package com.manulife.pension.platform.web.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Charles Chan
 */
public abstract class ParamSupportTag extends BodyTagSupport {

	private final static String PARAM_SUPPORT_TAG_KEY = "parameterSupportTagKey";
	
	/**
	 * Constructor.
	 */
	public ParamSupportTag() {
		super();
	}

	protected abstract String getParameterMapKey();
	
	protected Map getMap(String parameterMapKey) {
		Map map = (Map)pageContext.getAttribute(parameterMapKey);
		if (map == null) {
			map = new HashMap();
			pageContext.setAttribute(parameterMapKey, map);
		}
		return map;
	}
	
	public void addParameter(Object name, Object value) {
		getMap(getParameterMapKey()).put(name, value);
	}
	
	public static ParamSupportTag getEnclosingParamSupportTag(PageContext pageContext) {
		return (ParamSupportTag)pageContext.getAttribute(PARAM_SUPPORT_TAG_KEY);
	}
	
	public int doStartTag() throws JspException {
		getMap(getParameterMapKey()).clear();
		pageContext.setAttribute(PARAM_SUPPORT_TAG_KEY, this);
		return EVAL_BODY_TAG;
	}

	public int doEndTag() throws JspException {
		pageContext.removeAttribute(PARAM_SUPPORT_TAG_KEY);
		return EVAL_PAGE;
	}
}