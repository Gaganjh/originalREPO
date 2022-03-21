package com.manulife.pension.platform.web.taglib;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.LinkTag;
import com.manulife.pension.platform.web.taglib.util.TagUtils;



/**
 * @author Charles Chan
 */
public class PrintFriendlyLinkTag extends LinkTag {

	/**
	 * Constructor.
	 */
	public PrintFriendlyLinkTag() {
		super();
	}

	public int doStartTag() throws JspException {

        if (!BaseTagHelper.isPrintFriendly(pageContext)) {
            return super.doStartTag();
        } // fi
        
		this.text = null;
		return EVAL_BODY_TAG;
	}

	public int doEndTag() throws JspException {

        if (!BaseTagHelper.isPrintFriendly(pageContext)) {
            return super.doEndTag();
        } // fi
        
        StringBuffer results = new StringBuffer();
		if (text != null) {
			results.append(text);
        }

		TagUtils.getInstance().write(pageContext, results.toString());

		return (EVAL_PAGE);
	}
	
	protected String calculateURL() throws JspException {
		return BaseTagHelper.getPublicUrl(super.calculateURL());
	}
}
