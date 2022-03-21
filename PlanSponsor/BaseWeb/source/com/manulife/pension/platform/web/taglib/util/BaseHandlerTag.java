package com.manulife.pension.platform.web.taglib.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.beanutils.BeanUtils;

public abstract class BaseHandlerTag extends BodyTagSupport {

    protected String accesskey = null;
    protected String tabindex = null;
    protected boolean indexed = false;
    private String onclick = null;
    private String ondblclick = null;
    private String onmouseover = null;
    private String onmouseout = null;
    private String onmousemove = null;
    private String onmousedown = null;
    private String onmouseup = null;
    private String onkeydown = null;
    private String onkeyup = null;
    private String onkeypress = null;
    private String onselect = null;
    private String onchange = null;
    private String onblur = null;
    private String onfocus = null;
    private boolean disabled = false;
    protected boolean doDisabled = true;
    private boolean readonly = false;
    protected boolean doReadonly = false;
    private String style = null;
    private String styleClass = null;
    private String styleId = null;
    private String errorKey = Constants.ERROR_KEY;
    private String errorStyle = null;
    private String errorStyleClass = null;
    private String errorStyleId = null;
    private String alt = null;
    private String altKey = null;
    private String bundle = null;
    private String locale = Constants.LOCALE_KEY;
    private String title = null;
    private String lang = null;
    private String dir = null;
    private String titleKey = null;
    private Class loopTagClass = null;
    private Method loopTagGetStatus = null;
    private Class loopTagStatusClass = null;
    private Method loopTagStatusGetIndex = null;
    private boolean triedJstlInit = false;
    private boolean triedJstlSuccess = false;
    public void setAccesskey(String accessKey) {
        this.accesskey = accessKey;
    }
    public String getAccesskey() {
        return (this.accesskey);
    }
    public void setTabindex(String tabIndex) {
        this.tabindex = tabIndex;
    }
    public String getTabindex() {
        return (this.tabindex);
    }
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }
    public boolean getIndexed() {
        return (this.indexed);
    }
    public void setOnclick(String onClick) {
        this.onclick = onClick;
    }
    public String getOnclick() {
        return onclick;
    }
    public void setOndblclick(String onDblClick) {
        this.ondblclick = onDblClick;
    }
    public String getOndblclick() {
        return ondblclick;
    }
    public void setOnmousedown(String onMouseDown) {
        this.onmousedown = onMouseDown;
    }
    public String getOnmousedown() {
        return onmousedown;
    }
    public void setOnmouseup(String onMouseUp) {
        this.onmouseup = onMouseUp;
    }
    public String getOnmouseup() {
        return onmouseup;
    }
    public void setOnmousemove(String onMouseMove) {
        this.onmousemove = onMouseMove;
    }
    public String getOnmousemove() {
        return onmousemove;
    }
    public void setOnmouseover(String onMouseOver) {
        this.onmouseover = onMouseOver;
    }
    public String getOnmouseover() {
        return onmouseover;
    }
    public void setOnmouseout(String onMouseOut) {
        this.onmouseout = onMouseOut;
    }
    public String getOnmouseout() {
        return onmouseout;
    }
    public void setOnkeydown(String onKeyDown) {
        this.onkeydown = onKeyDown;
    }
    public String getOnkeydown() {
        return onkeydown;
    }
    public void setOnkeyup(String onKeyUp) {
        this.onkeyup = onKeyUp;
    }
    public String getOnkeyup() {
        return onkeyup;
    }
    public void setOnkeypress(String onKeyPress) {
        this.onkeypress = onKeyPress;
    }
    public String getOnkeypress() {
        return onkeypress;
    }
    public void setOnchange(String onChange) {
        this.onchange = onChange;
    }
    public String getOnchange() {
        return onchange;
    }
    public void setOnselect(String onSelect) {
        this.onselect = onSelect;
    }
    public String getOnselect() {
        return onselect;
    }
    public void setOnblur(String onBlur) {
        this.onblur = onBlur;
    }
    public String getOnblur() {
        return onblur;
    }
    public void setOnfocus(String onFocus) {
        this.onfocus = onFocus;
    }
    public String getOnfocus() {
        return onfocus;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public boolean getDisabled() {
        return disabled;
    }
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
    public boolean getReadonly() {
        return readonly;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    public String getStyle() {
        return style;
    }
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
    public String getStyleClass() {
        return styleClass;
    }
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }
    public String getStyleId() {
        return styleId;
    }
    public String getErrorKey() {
        return errorKey;
    }
    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }
    public String getErrorStyle() {
        return errorStyle;
    }
    public void setErrorStyle(String errorStyle) {
        this.errorStyle = errorStyle;
    }
    public String getErrorStyleClass() {
        return errorStyleClass;
    }
    public void setErrorStyleClass(String errorStyleClass) {
        this.errorStyleClass = errorStyleClass;
    }
    public String getErrorStyleId() {
        return errorStyleId;
    }
    public void setErrorStyleId(String errorStyleId) {
        this.errorStyleId = errorStyleId;
    }
    public String getAlt() {
        return alt;
    }
    public void setAlt(String alt) {
        this.alt = alt;
    }
    public String getAltKey() {
        return altKey;
    }
    public void setAltKey(String altKey) {
        this.altKey = altKey;
    }
    public String getBundle() {
        return bundle;
    }
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitleKey() {
        return titleKey;
    }
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }
    public String getLang() {
        return this.lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }
    public String getDir() {
        return this.dir;
    }
    public void setDir(String dir) {
        this.dir = dir;
    }
    public void release() {
        super.release();
        accesskey = null;
        alt = null;
        altKey = null;
        bundle = null;
        dir = null;
        errorKey = Constants.ERROR_KEY;
        errorStyle = null;
        errorStyleClass = null;
        errorStyleId = null;
        indexed = false;
        lang = null;
        locale = Constants.LOCALE_KEY;
        onclick = null;
        ondblclick = null;
        onmouseover = null;
        onmouseout = null;
        onmousemove = null;
        onmousedown = null;
        onmouseup = null;
        onkeydown = null;
        onkeyup = null;
        onkeypress = null;
        onselect = null;
        onchange = null;
        onblur = null;
        onfocus = null;
        disabled = false;
        readonly = false;
        style = null;
        styleClass = null;
        styleId = null;
        tabindex = null;
        title = null;
        titleKey = null;
    }
    protected String message(String literal, String key)
        throws JspException {
        if (literal != null) {
            if (key != null) {
                JspException e =
                    new JspException("common.both");

                TagUtils.getInstance().saveException(pageContext, e);
                throw e;
            } else {
                return (literal);
            }
        } else {
            if (key != null) {
               /* return TagUtils.getInstance().message(pageContext, getBundle(),
                    getLocale(), key);*/
            	return "ABC";
            } else {
                return null;
            }
        }
    }

    private Integer getJstlLoopIndex() {/*
        if (!triedJstlInit) {
            triedJstlInit = true;

            try {
                loopTagClass =
                    RequestUtils.applicationClass(
                        "javax.servlet.jsp.jstl.core.LoopTag");

                loopTagGetStatus =
                    loopTagClass.getDeclaredMethod("getLoopStatus", null);

                loopTagStatusClass =
                    RequestUtils.applicationClass(
                        "javax.servlet.jsp.jstl.core.LoopTagStatus");

                loopTagStatusGetIndex =
                    loopTagStatusClass.getDeclaredMethod("getIndex", null);

                triedJstlSuccess = true;
            } catch (ClassNotFoundException ex) {
                // These just mean that JSTL isn't loaded, so ignore
            } catch (NoSuchMethodException ex) {
            }
        }

        if (triedJstlSuccess) {
            try {
                Object loopTag =
                    findAncestorWithClass(this, loopTagClass);

                if (loopTag == null) {
                    return null;
                }

                Object status = loopTagGetStatus.invoke(loopTag, null);

                return (Integer) loopTagStatusGetIndex.invoke(status, null);
            } catch (IllegalAccessException ex) {
                log.error(ex.getMessage(), ex);
            } catch (IllegalArgumentException ex) {
                log.error(ex.getMessage(), ex);
            } catch (InvocationTargetException ex) {
                log.error(ex.getMessage(), ex);
            } catch (NullPointerException ex) {
                log.error(ex.getMessage(), ex);
            } catch (ExceptionInInitializerError ex) {
                log.error(ex.getMessage(), ex);
            }
        }
*/
        return null;
        
    }

    protected void prepareIndex(StringBuffer handlers, String name)
        throws JspException {
        if (name != null) {
            handlers.append(name);
        }

        
        handlers.append("[");
        handlers.append(getIndexValue());
        handlers.append("]");
       

        if (name != null) {
            handlers.append(".");
        }
    }

   protected int getIndexValue()
        throws JspException {/*
        // look for outer iterate tag
        IterateTag iterateTag =
            (IterateTag) findAncestorWithClass(this, IterateTag.class);

        if (iterateTag != null) {
            return iterateTag.getIndex();
        }

        // Look for JSTL loops
        Integer i = getJstlLoopIndex();

        if (i != null) {
            return i.intValue();
        }

        // this tag should be nested in an IterateTag or JSTL loop tag, if it's not, throw exception
        JspException e =
            new JspException("indexed.noEnclosingIterate");

        TagUtils.getInstance().saveException(pageContext, e);
        throw e;
    */
    return 1;	
    }

    protected String prepareStyles()
        throws JspException {
        StringBuffer styles = new StringBuffer();

        boolean errorsExist = doErrorsExist();

        if (errorsExist && (getErrorStyleId() != null)) {
            prepareAttribute(styles, "id", getErrorStyleId());
        } else {
            prepareAttribute(styles, "id", getStyleId());
        }

        if (errorsExist && (getErrorStyle() != null)) {
            prepareAttribute(styles, "style", getErrorStyle());
        } else {
            prepareAttribute(styles, "style", getStyle());
        }

        if (errorsExist && (getErrorStyleClass() != null)) {
            prepareAttribute(styles, "class", getErrorStyleClass());
        } else {
            prepareAttribute(styles, "class", getStyleClass());
        }

        prepareAttribute(styles, "title", message(getTitle(), getTitleKey()));
        prepareAttribute(styles, "alt", message(getAlt(), getAltKey()));
        prepareInternationalization(styles);

        return styles.toString();
    }

    protected boolean doErrorsExist()
        throws JspException {
        boolean errorsExist = false;

        if ((getErrorStyleId() != null) || (getErrorStyle() != null)
            || (getErrorStyleClass() != null)) {
            String actualName = prepareName();

            if (actualName != null) {
            	System.out.println("errors....");
              /*  ActionMessages errors =
                    TagUtils.getInstance().getActionMessages(pageContext,
                        errorKey);

                errorsExist = ((errors != null)
                    && (errors.size(actualName) > 0));*/
            }
        }

        return errorsExist;
    }

    protected String prepareName()
        throws JspException {
        return null;
    }

    protected String prepareEventHandlers() {
        StringBuffer handlers = new StringBuffer();

        prepareMouseEvents(handlers);
        prepareKeyEvents(handlers);
        prepareTextEvents(handlers);
        prepareFocusEvents(handlers);

        return handlers.toString();
    }

    protected void prepareMouseEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onclick", getOnclick());
        prepareAttribute(handlers, "ondblclick", getOndblclick());
        prepareAttribute(handlers, "onmouseover", getOnmouseover());
        prepareAttribute(handlers, "onmouseout", getOnmouseout());
        prepareAttribute(handlers, "onmousemove", getOnmousemove());
        prepareAttribute(handlers, "onmousedown", getOnmousedown());
        prepareAttribute(handlers, "onmouseup", getOnmouseup());
    }

    protected void prepareKeyEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onkeydown", getOnkeydown());
        prepareAttribute(handlers, "onkeyup", getOnkeyup());
        prepareAttribute(handlers, "onkeypress", getOnkeypress());
    }

    protected void prepareTextEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onselect", getOnselect());
        prepareAttribute(handlers, "onchange", getOnchange());
    }

    protected void prepareFocusEvents(StringBuffer handlers) {
        prepareAttribute(handlers, "onblur", getOnblur());
        prepareAttribute(handlers, "onfocus", getOnfocus());

        // Get the parent FormTag (if necessary)
        Object formTag = null;

        if ((doDisabled && !getDisabled()) || (doReadonly && !getReadonly())) {
            formTag =
                pageContext.getAttribute(Constants.FORM_KEY,
                    PageContext.REQUEST_SCOPE);
        }

        // Format Disabled
        if (doDisabled) {
            boolean formDisabled =
                (formTag == null) ? false : true;

            if (formDisabled || getDisabled()) {
                handlers.append(" disabled=\"disabled\"");
            }
        }

        // Format Read Only
        if (doReadonly) {
            boolean formReadOnly =
                (formTag == null) ? false : true;

            if (formReadOnly || getReadonly()) {
                handlers.append(" readonly=\"readonly\"");
            }
        }
    }

    protected void prepareInternationalization(StringBuffer handlers) {
        prepareAttribute(handlers, "lang", getLang());
        prepareAttribute(handlers, "dir", getDir());
    }

    protected void prepareOtherAttributes(StringBuffer handlers) {
    }

    protected void prepareAttribute(StringBuffer handlers, String name,
        Object value) {
        if (value != null) {
            handlers.append(" ");
            handlers.append(name);
            handlers.append("=\"");
            handlers.append(value);
            handlers.append("\"");
        }
    }

    protected boolean isXhtml() {
        return TagUtils.getInstance().isXhtml(this.pageContext);
    }

    protected String getElementClose() {
        return this.isXhtml() ? " />" : ">";
    }

    protected String lookupProperty(String beanName, String property)
        throws JspException {
        Object bean =
            TagUtils.getInstance().lookup(this.pageContext, beanName, null);

        if (bean == null) {
            throw new JspException("getter.bean : "+ beanName);
        }

        try {
            return BeanUtils.getProperty(bean, property);
        } catch (IllegalAccessException e) {
            throw new JspException("getter.access :"+ property+", bean name :"+ beanName);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();

            throw new JspException("getter.result : "+ property+" "+ t.toString());
        } catch (NoSuchMethodException e) {
            throw new JspException("getter.method:"+ property+"  "+ beanName);
        }
    }
}
