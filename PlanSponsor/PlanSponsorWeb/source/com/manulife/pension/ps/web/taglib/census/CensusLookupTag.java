package com.manulife.pension.ps.web.taglib.census;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.ps.web.census.util.CensusLookups;

/**
 * Convert the census lookup code to description.
 * 
 * @author guweigu
 * 
 */
public class CensusLookupTag extends TagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = 880696329518147614L;

    private static final Logger log = Logger.getLogger(CensusLookupTag.class);

    /**
     * bean name stored in out of page,request,session and application scope. It will search by this
     * order and use the first one found.
     */
    private String name;

    /**
     * Census lookup type name;
     */
    private String typeName;

    /**
     * Census lookup code property from the bean
     */
    private String property;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int doEndTag() throws JspException {
        Object bean = pageContext.findAttribute(name);
        try {
            if (bean == null) {
                pageContext.getOut().write("");
            } else {
                String code = (String) PropertyUtils.getProperty(bean, property);
                String value = CensusLookups.getInstance().getLookupValue(typeName,
                        code == null ? "" : code.trim());
                pageContext.getOut().write(value == null ? "" : value);
            }
        } catch (IOException e) {
            throw new JspException("IO fails", e);
        } catch (Exception e) {
            log.error("Problem in getting property value from property = " + property + " bean = "
                    + bean);
            throw new JspException("Fail to get property " + property);
        }
        return super.doEndTag();
    }

}
