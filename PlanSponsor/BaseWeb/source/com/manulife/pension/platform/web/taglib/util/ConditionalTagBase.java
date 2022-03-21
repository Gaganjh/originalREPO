package com.manulife.pension.platform.web.taglib.util;



import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;


public abstract class ConditionalTagBase extends TagSupport {


    protected String cookie = null;

    protected String header = null;

    protected String name = null;


    protected String parameter = null;

    protected String property = null;

    protected String role = null;

    protected String scope = null;

    protected String user = null;
    public String value = null;
    protected static final int LONG_COMPARE = 1;
    protected static final int DOUBLE_COMPARE = 0;
    protected static final int STRING_COMPARE = 2;
    

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCookie() {
        return (this.cookie);
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getHeader() {
        return (this.header);
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return (this.parameter);
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getRole() {
        return (this.role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUser() {
        return (this.user);
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int doStartTag() throws JspException {
        if (condition()) {
            return (EVAL_BODY_INCLUDE);
        } else {
            return (SKIP_BODY);
        }
    }


    public int doEndTag() throws JspException {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        cookie = null;
        header = null;
        name = null;
        parameter = null;
        property = null;
        role = null;
        scope = null;
        user = null;
    }

    protected abstract boolean condition()
        throws JspException;

    protected boolean condition(int desired1, int desired2)
        throws JspException {
        // Acquire the value and determine the test type
        int type = -1;
        double doubleValue = 0.0;
        long longValue = 0;

        if ((type < 0) && (value.length() > 0)) {
            try {
                doubleValue = Double.parseDouble(value);
                type = DOUBLE_COMPARE;
            } catch (NumberFormatException e) {
                ;
            }
        }

        if ((type < 0) && (value.length() > 0)) {
            try {
                longValue = Long.parseLong(value);
                type = LONG_COMPARE;
            } catch (NumberFormatException e) {
                ;
            }
        }

        if (type < 0) {
            type = STRING_COMPARE;
        }

        // Acquire the unconverted variable value
        Object variable = null;

        if (cookie != null) {
            Cookie[] cookies =
                ((HttpServletRequest) pageContext.getRequest()).getCookies();

            if (cookies == null) {
                cookies = new Cookie[0];
            }

            for (int i = 0; i < cookies.length; i++) {
                if (cookie.equals(cookies[i].getName())) {
                    variable = cookies[i].getValue();

                    break;
                }
            }
        } else if (header != null) {
            variable =
                ((HttpServletRequest) pageContext.getRequest()).getHeader(header);
        } else if (name != null) {
            Object bean =
                TagUtils.getInstance().lookup(pageContext, name, scope);

            if (property != null) {
                if (bean == null) {
                    JspException e =
                        new JspException(BaseBundle.getMessage("logic.bean", name));

                    TagUtils.getInstance().saveException(pageContext, e);
                    throw e;
                }

                try {
                    variable = PropertyUtils.getProperty(bean, property);
                } catch (InvocationTargetException e) {
                    Throwable t = e.getTargetException();

                    if (t == null) {
                        t = e;
                    }

                    TagUtils.getInstance().saveException(pageContext, t);
                    throw new JspException(BaseBundle.getMessage(
                            "logic.property",  t.toString()));
                } catch (Throwable t) {
                    TagUtils.getInstance().saveException(pageContext, t);
                    throw new JspException(/*BaseBundle.getMessage(
                            "logic.property", name, property, t.toString())*/);
                }
            } else {
                variable = bean;
            }
        } else if (parameter != null) {
            variable = pageContext.getRequest().getParameter(parameter);
        } else {
            JspException e =
                new JspException(BaseBundle.getMessage("logic.selector"));

            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        if (variable == null) {
            variable = ""; // Coerce null to a zero-length String
        }

        // Perform the appropriate comparison
        int result = 0;

        if (type == DOUBLE_COMPARE) {
            try {
                double doubleVariable = Double.parseDouble(variable.toString());

                if (doubleVariable < doubleValue) {
                    result = -1;
                } else if (doubleVariable > doubleValue) {
                    result = +1;
                }
            } catch (NumberFormatException e) {
                result = variable.toString().compareTo(value);
            }
        } else if (type == LONG_COMPARE) {
            try {
                long longVariable = Long.parseLong(variable.toString());

                if (longVariable < longValue) {
                    result = -1;
                } else if (longVariable > longValue) {
                    result = +1;
                }
            } catch (NumberFormatException e) {
                result = variable.toString().compareTo(value);
            }
        } else {
            result = variable.toString().compareTo(value);
        }

        // Normalize the result
        if (result < 0) {
            result = -1;
        } else if (result > 0) {
            result = +1;
        }

        // Return true if the result matches either desired value
        return ((result == desired1) || (result == desired2));
    }

}