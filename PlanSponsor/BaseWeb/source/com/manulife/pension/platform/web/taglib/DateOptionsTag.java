package com.manulife.pension.platform.web.taglib;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.SelectTag;
import com.manulife.pension.platform.web.taglib.util.TagUtils;


import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This tag formats a collection of Date objects or a collection of Calendar
 * objects as an HTML option list. Since this tag derives from the Struts
 * OptionsTag, it can be used in conjunction with the Struts Select tag and the
 * Struts Options tag. This tag supports most of the attributes of the
 * Struts Options tag except for the followings:
 * <p>
 * <ul>
 *   <li>collection</li>
 *   <li>labelName</li>
 *   <li>labelProperty</li>
 *   <li>filter</li>
 * </ul>
 * </p>
 * The following additional attributes are also supported:
 * <p>
 * <ul>
 *   <li>patternOut</li>
 *   <li>renderStyle</li>
 * </ul>
 * </p>
 * Please refer to the Javadoc in the method of the above attributes for
 * explanation of their usages.
 * 
 * @author Charles Chan
 */
public class DateOptionsTag extends PrintFriendlyOptionsTag {

	private String patternOut;

	private String renderStyle = RenderConstants.SHORT_STYLE;

	/**
	 *  Constructor.
	 */
	public DateOptionsTag() {
		super();
	}

	public int doEndTag() throws JspException {

		// Acquire the select tag we are associated with
		SelectTag selectTag = BaseTagHelper.getEnclosingSelectTag(pageContext);

		StringBuffer sb = new StringBuffer();

		// Construct iterators for the values collections
		Iterator valuesIterator = getIterator(name, property);

		// Render the options tags for each element of the values coll.
		while (valuesIterator.hasNext()) {
			Object valueObject = valuesIterator.next();
			Date valueDate = null;
			String value = null;
			String label = null;

			if (valueObject == null) {
				value = "";
				label = "";
			} else {
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

				value = String.valueOf(valueDate.getTime());

				try {
					if (patternOut == null) {
						label = DateRender.formatByStyle(valueDate,
								"UnknownDate", null, renderStyle);
					} else {
						label = DateRender.formatByPattern(valueDate,
								"UnknownDate", null, patternOut);
					}
				} catch (Exception e) {
					throw new JspException(e.getMessage());
				}
			}
			addOption(sb, value, label, selectTag.isMatched(value));
		}

		// Render this element to our writer
		TagUtils.getInstance().write(pageContext, sb.toString());

		// Evaluate the remainder of this page
		return EVAL_PAGE;

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
	public String getCollection() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setCollection(String collection) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public boolean getFilter() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setFilter(boolean filter) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public String getLabelProperty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	public void setLabelProperty(String labelProperty) {
		throw new UnsupportedOperationException();
	}
}
