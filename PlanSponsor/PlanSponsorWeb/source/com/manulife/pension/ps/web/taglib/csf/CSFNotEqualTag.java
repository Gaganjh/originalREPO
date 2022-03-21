package com.manulife.pension.ps.web.taglib.csf;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * @author Aron Rogers
 */
public class CSFNotEqualTag extends CSFEqualTag {

    private static final long serialVersionUID = -1L;

    public int doStartTag() throws JspTagException, JspException {
        boolean csfEqual = !condition();

        return csfEqual ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

}
