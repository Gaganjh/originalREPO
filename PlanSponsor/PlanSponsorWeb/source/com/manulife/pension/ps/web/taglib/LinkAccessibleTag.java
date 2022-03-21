package com.manulife.pension.ps.web.taglib;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.ConditionalTagBase;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * This method extends the Struts logic equal tag. Given the URL/path to the link,
 * it calls the SecurityManager to determine whether the user's contract has 
 * access to that link.
 * 
 * @author Chris Shin
 */
public class LinkAccessibleTag extends ConditionalTagBase {

	private String path;
	private boolean value;
	
	/**
	 * Constructor.
	 *  
	 */
	public LinkAccessibleTag() {
		super();
	}

	/*
	 * 
	 * @see org.apache.struts.taglib.logic.EqualTag#condition()
	 */
    protected boolean condition()
        throws JspException {

		boolean result = false;
		
		try {
			
			HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();

			UserProfile profile = SessionHelper.getUserProfile(request);
			SecurityManager mgr = SecurityManager.getInstance();

			if (profile != null) {
				result = mgr.isUserAuthorized(profile, getPath());			
			}
			
		} catch (Exception e) {
			
			StringBuffer buf = new StringBuffer();
			buf.append("Error in condition for path [ ");
			buf.append(getPath());
			buf.append(" ] ");
			buf.append(e.toString());

			throw new JspException(buf.toString());
		}

		return result;

    }

    /**
     * @return string
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param string
     */
    public void setPath(String path) {
        this.path = path;
    }



}
