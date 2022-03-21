package com.manulife.pension.platform.web.taglib.util;

import org.apache.commons.beanutils.BeanUtils;


import javax.servlet.jsp.JspException;

import java.lang.reflect.InvocationTargetException;
public class SelectTag extends BaseHandlerTag {
    protected String[] match = null;
    protected String multiple = null;
    protected String name = Constants.BEAN_KEY;
    protected String property = null;
    protected String saveBody = null;
    protected String size = null;
    protected String value = null;

    public String getMultiple() {
        return (this.multiple);
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return (this.size);
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isMatched(String value) {
        if ((this.match == null) || (value == null)) {
            return false;
        }

        for (int i = 0; i < this.match.length; i++) {
            if (value.equals(this.match[i])) {
                return true;
            }
        }

        return false;
    }
    public String getProperty() {
        return (this.property);
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getValue() {
        return (this.value);
    }
    public void setValue(String value) {
        this.value = value;
    }
    public int doStartTag() throws JspException {
        TagUtils.getInstance().write(pageContext, renderSelectStartElement());

        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.SELECT_KEY, this);

        this.calculateMatchValues();

        return (EVAL_BODY_TAG);
    }
    protected String renderSelectStartElement()
        throws JspException {
        StringBuffer results = new StringBuffer("<select");

        prepareAttribute(results, "name", prepareName());
        prepareAttribute(results, "accesskey", getAccesskey());

        if (multiple != null) {
            results.append(" multiple=\"multiple\"");
        }

        prepareAttribute(results, "size", getSize());
        prepareAttribute(results, "tabindex", getTabindex());
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        prepareOtherAttributes(results);
        results.append(">");

        return results.toString();
    }
    private void calculateMatchValues()
        throws JspException {
        if (this.value != null) {
            this.match = new String[1];
            this.match[0] = this.value;
        } else {
            Object bean =
                TagUtils.getInstance().lookup(pageContext, name, null);

            if (bean == null) {
                JspException e =
                    new JspException("getter.bean:"+ name);

                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            }

            try {
                this.match = BeanUtils.getArrayProperty(bean, property);

                if (this.match == null) {
                    this.match = new String[0];
                }
            } catch (IllegalAccessException e) {
                TagUtils.getInstance().saveException(pageContext, e);
                throw new JspException("getter.access:"+ property+", name: "+ name);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();

                TagUtils.getInstance().saveException(pageContext, t);
                throw new JspException("getter.result : "+property+"   "+ t.toString());
            } catch (NoSuchMethodException e) {
                TagUtils.getInstance().saveException(pageContext, e);
                throw new JspException("getter.method"+property+",name : "+ name);
            }
        }
    }

     public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            String value = bodyContent.getString();

            if (value == null) {
                value = "";
            }

            this.saveBody = value.trim();
        }

        return (SKIP_BODY);
    }
    public int doEndTag() throws JspException {
        // Remove the page scope attributes we created
        pageContext.removeAttribute(Constants.SELECT_KEY);

        // Render a tag representing the end of our current form
        StringBuffer results = new StringBuffer();

        if (saveBody != null) {
            results.append(saveBody);
            saveBody = null;
        }

        results.append("</select>");

        TagUtils.getInstance().write(pageContext, results.toString());

        return (EVAL_PAGE);
    }

     protected String prepareName()
        throws JspException {
        if (property == null) {
            return null;
        }

        // * @since Struts 1.1
        if (indexed) {
            StringBuffer results = new StringBuffer();

            prepareIndex(results, name);
            results.append(property);

            return results.toString();
        }

        return property;
    }

    public void release() {
        super.release();
        match = null;
        multiple = null;
        name = Constants.BEAN_KEY;
        property = null;
        saveBody = null;
        size = null;
        value = null;
    }
}
