package com.manulife.pension.platform.web.taglib;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class renders a single HTML option tag for a Date object. Since this tag
 * derives from the Struts OptionTag, it can be used in conjunction with the
 * Struts Select tag and the Struts Options tag. This tag supports most of the
 * attributes of the Struts Option tag except for the followings:
 * <p>
 * <ul>
 *   <li>bundle</li>
 *   <li>key</li>
 *   <li>locale</li>
 * </ul>
 * </p>
 * The following additional attributes are also supported:
 * <p>
 * <ul>
 *   <li>name</li>
 *   <li>property</li>
 *   <li>scope</li>
 *   <li>patternOut</li>
 *   <li>renderStyle</li>
 * </ul>
 * </p>
 * Please refer to the Javadoc in the method of the above attributes for
 * explanation of their usages.
 * 
 * @author Charles Chan
 */
public class DateOptionTag extends PrintFriendlyOptionTag {

	/**
	 * Name of the bean that contains the data we will be rendering.
	 */
	private String name;

	/**
	 * Name of the property to be accessed on the specified bean.
	 */
	private String property = null;

	/**
	 * The scope to be searched to retrieve the specified bean.
	 */
	private String scope;

	/**
	 * The pattern used to format the date.
	 */
	private String patternOut;

	/**
	 * The rendering style used to format the date.
	 */
	private String renderStyle = RenderConstants.SHORT_STYLE;

	/**
	 * Constructor. 
	 */
	public DateOptionTag() {
		super();
	}

	public int doStartTag() throws JspException {
		initLabelAndValue();
		return EVAL_BODY_TAG;
	}

	protected void initLabelAndValue() throws JspException {
		// Look up the requested property value
		Object valueObject = TagUtils.getInstance().lookup(pageContext, name, property,
				scope);

		if (valueObject == null) { throw new IllegalArgumentException(
				"No value found for name [" + name + "] property [" + property
						+ "] scope [" + scope + "]"); }

		Date valueDate;

		if (valueObject instanceof Date) {
			valueDate = (Date) valueObject;
		} else if (valueObject instanceof Calendar) {
			valueDate = ((Calendar) valueObject).getTime();
		} else {
			String valueObjectClass = valueObject.getClass().getName();
			throw new IllegalArgumentException("Value of class ["
					+ valueObjectClass
					+ "] is not a Date and is not a Calendar");
		}

		try {
			if (patternOut == null) {
				this.text = DateRender.formatByStyle(valueDate, "UnknownDate",
						null, renderStyle);
			} else {
				this.text = DateRender.formatByPattern(valueDate,
						"UnknownDate", null, patternOut);
			}
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}

		setValue(String.valueOf(valueDate.getTime()));
	}

	/**
	 * @return The name of the bean that contains the data we will be rendering.
	 */
	public String getName() {
		return (this.name);
	}

	/**
	 * Name of the bean that contains the data we will be rendering.
     *
     * @param name The name of the bean. 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Name of the property to be accessed on the specified bean.
	 */
	public String getProperty() {
		return (this.property);
	}

	/**
	 * Name of the property to be accessed on the specified bean.
     * @return property The name of the property.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return The scope to be searched to retrieve the specified bean.
	 */
	public String getScope() {
		return (this.scope);
	}

	/**
	 * The scope to be searched to retrieve the specified bean.
     * @param scope The scope to be searched.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return Returns the pattern.
	 */
	public String getPatternOut() {
		return patternOut;
	}

	/**
	 * Sets the formatting pattern for the tag. If both formatting pattern and
	 * rendering style are specified, formatting pattern takes precedent.
	 * 
	 * @see com.manulife.util.render.RenderConstants
	 * @param pattern
	 *            The pattern to set.
	 */
	public void setPatternOut(String pattern) {
		this.patternOut = pattern;
	}

	/**
	 * @return Returns the renderStyle.
	 */
	public String getRenderStyle() {
		return renderStyle;
	}

	/**
	 * Sets the rendering style for the tag. If both formatting pattern and
	 * rendering style are specified, formatting pattern takes precedent.
	 * 
	 * @see com.manulife.util.render.RenderConstants
	 * @param renderStyle
	 *            The renderStyle to set.
	 */
	public void setRenderStyle(String renderStyle) {
		this.renderStyle = renderStyle;
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public String getBundle() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setBundle(String bundle) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public String getKey() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setKey(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public String getLocale() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setLocale(String locale) {
		throw new UnsupportedOperationException();
	}
}
