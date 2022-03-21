/*
 * Created on Oct 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.taglib.profile;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.ps.web.profiles.AccessLevelHelper;
import com.manulife.pension.service.security.role.TPAUserManager;
import com.manulife.pension.service.security.role.UserRole;

/**
 * @author sternlu
 * 
 * Formats TPA Firm Access
 */
public class FormatTpaFirmAccessTag extends TagSupport {
    private String name;

    private String property;

    private String scope;

    public int doStartTag() throws JspException {
        try {
            Object oRole = TagUtils.getInstance().lookup(pageContext, name, property, scope);
            if (oRole instanceof String) {

                String role = (String) oRole;
                if (AccessLevelHelper.TPA_ACCESS.equals(role))
                    pageContext.getOut().print(AccessLevelHelper.TPA_ACCESS);
                else
                    pageContext.getOut().print(AccessLevelHelper.NO_ACCESS_STRING);

            } else {
                UserRole value = (UserRole) oRole;
                if (value instanceof TPAUserManager) {
                    pageContext.getOut().print(AccessLevelHelper.TPA_ACCESS);
                } else {
                    pageContext.getOut().print(AccessLevelHelper.NO_ACCESS_STRING);
                }
            }
        } catch (IOException ex) {
            throw new JspException(ex.getMessage());
        }
        return SKIP_BODY;
    }

    /**
     * Gets the name
     * 
     * @return Returns a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the valueBeanProperty
     * 
     * @return Returns a String
     */
    public String getVProperty() {
        return property;
    }

    /**
     * Sets the valueBeanProperty
     * 
     * @param valueBeanProperty The valueBeanProperty to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Gets the scope
     * 
     * @return Returns a String
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope
     * 
     * @param scope The scope to set
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

}