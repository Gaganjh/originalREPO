package com.manulife.pension.ps.web.taglib.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.PilotHelper;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * @author celarro
 */
public class IsInPilotTag extends TagSupport {
		
	private String name;
	
	/**
	 * Constructor.
	 */
	public IsInPilotTag() {
		super();
	}

	/**
	 * @return Returns the id.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public int doStartTag() throws JspTagException, JspException
    {
		int contractNumber = 0;
		UserProfile userProfile = (UserProfile)SessionHelper.getUserProfile((HttpServletRequest)pageContext.getRequest());		
		if(userProfile != null) {
			contractNumber = userProfile.getCurrentContract().getContractNumber();
		}

		if(PilotHelper.isInPilot(contractNumber, name)) {
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
