package com.manulife.pension.ps.web.taglib.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.PilotHelper;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * @author maceadi
 */

public class IsAnyPilotAvailableTag extends TagSupport {
		
	private String name;
	
	/**
	 * Constructor.
	 */
	public IsAnyPilotAvailableTag() {
		super();
	}

		
	public int doStartTag() throws JspTagException, JspException
    {
		if(PilotHelper.isAnyPilotAvailable()) {
			return EVAL_BODY_INCLUDE;
		}
		else { 
			return SKIP_BODY;
		}
    }
	
	public int doEndTag() throws JspTagException
    {
        return EVAL_PAGE;
    }
}
