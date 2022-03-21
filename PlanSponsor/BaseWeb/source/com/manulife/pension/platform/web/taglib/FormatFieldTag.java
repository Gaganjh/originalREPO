package com.manulife.pension.platform.web.taglib;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

public class FormatFieldTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	protected String name;
	protected String property;
	protected String scope;
	protected String formatStr;

	public FormatFieldTag() {
		this.name = null;
		this.property = null;
		this.scope = null;
		this.formatStr = null;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return this.property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getFormat() {
		return this.formatStr;
	}

	public void setFormat(String formatStr) {
		this.formatStr = formatStr;
	}

	@Override
	public int doStartTag() throws JspException {

		Object value = TagUtils.getInstance().lookup(this.pageContext, this.name,
				this.property, this.scope);

		if (value == null) {
			return 0;
		}

		String output = formatValue(value);
		TagUtils.getInstance().write(this.pageContext, output);

		return 0;
	}

	protected String formatValue(Object valueToFormat) throws JspException {
		Format format = null;
		Object value = valueToFormat;

		String formatString = this.formatStr;

		if (value instanceof String) {
			return (String) value;
		}

		if (formatString != null) {
			if (value instanceof Number) {
				try {
					format = NumberFormat.getNumberInstance();
					((DecimalFormat) format).applyPattern(formatString);
				} catch (IllegalArgumentException e) {
					TagUtils.getInstance().saveException(this.pageContext, e);
					throw new JspException(e);
				}
			} else if (value instanceof java.util.Date) {
				format = new SimpleDateFormat(formatString);
			}
		}

		if (format != null) {
			return format.format(value);
		}
		return value.toString();
	}

	@Override
	public void release() {
		super.release();
		this.name = null;
		this.property = null;
		this.scope = null;
		this.formatStr = null;
	}
}
