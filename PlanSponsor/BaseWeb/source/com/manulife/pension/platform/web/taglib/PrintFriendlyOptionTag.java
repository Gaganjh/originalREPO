package com.manulife.pension.platform.web.taglib;

import javax.servlet.jsp.JspException;



/**
 * @author Charles Chan
 */
public class PrintFriendlyOptionTag extends OptionTag {

	/**
	 *  
	 */
	public PrintFriendlyOptionTag() {
		super();
	}

	protected String renderOptionElement() throws JspException {
		if (BaseTagHelper.isPrintFriendly(pageContext)) {
			if (BaseTagHelper.getEnclosingSelectTag(pageContext).isMatched(
					this.value)) {
				return text();
			} else {
				return "";
			}
		} else {
			return super.renderOptionElement();
		}
	}
}
