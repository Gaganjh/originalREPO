package com.manulife.pension.platform.web.taglib;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.Constants;
import com.manulife.pension.platform.web.taglib.util.SelectTag;
import com.manulife.pension.platform.web.taglib.util.TagUtils;



/**
 * @author Charles Chan
 */
public class PrintFriendlySelectTag extends SelectTag {

	/**
	 *  
	 */
	public PrintFriendlySelectTag() {
		super();
	}

	protected String renderSelectStartElement() throws JspException {
		if (BaseTagHelper.isPrintFriendly(pageContext)) {
			return "";
		} else {
			return super.renderSelectStartElement();
		}
	}

	public int doEndTag() throws JspException {
		// Remove the page scope attributes we created
		pageContext.removeAttribute(Constants.SELECT_KEY);

		// Render a tag representing the end of our current form
		StringBuffer results = new StringBuffer();
		if (saveBody != null) {
			results.append(saveBody);
		}

		if (!BaseTagHelper.isPrintFriendly(pageContext)) {
			results.append("</select>");
		}

		TagUtils.getInstance().write(pageContext, results.toString());

		return (EVAL_PAGE);
	}
}
