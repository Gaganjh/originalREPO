package com.manulife.pension.ps.web.taglib.security;

import javax.servlet.jsp.JspException;

/**
 * @author marcest
 * 
 */
public class NotPermissionAccessTag extends PermissionAccessTag {

    private static final long serialVersionUID = 6864433251167688332L;

    /**
     * Perform the test required for this particular tag, and either evaluate or skip the body of
     * this tag.
     * 
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {
        return hasPermission() ? SKIP_BODY : EVAL_BODY_INCLUDE;
    }

}
