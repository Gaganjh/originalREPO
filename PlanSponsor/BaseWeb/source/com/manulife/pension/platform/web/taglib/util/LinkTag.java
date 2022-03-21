
package com.manulife.pension.platform.web.taglib.util;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Generate a URL-encoded hyperlink to the specified URI.
 *
 * @version $Rev: 519563 $ $Date: 2005-04-06 02:37:00 -0400 (Wed, 06 Apr 2005)
 *          $
 */
public class LinkTag extends BaseHandlerTag {
    protected String text = null;
    protected String anchor = null;
    protected String forward = null;
    protected String href = null;
    protected String linkName = null;
    protected String name = null;
    protected String page = null;
    protected String action = null;
    protected String module = null;
    protected String paramId = null;
    protected String paramName = null;
    protected String paramProperty = null;
    protected String paramScope = null;
    protected String property = null;
    protected String scope = null;
    protected String target = null;
    protected boolean transaction = false;
    protected Map parameters = new HashMap();
    protected String indexId = null;
    protected boolean useLocalEncoding = false;
    public LinkTag() {
        super();
        doDisabled = false;
    }

    public String getAnchor() {
        return (this.anchor);
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getForward() {
        return (this.forward);
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getHref() {
        return (this.href);
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getLinkName() {
        return (this.linkName);
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return (this.page);
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getAction() {
        return (this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return (this.module);
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getParamId() {
        return (this.paramId);
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return (this.paramName);
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamProperty() {
        return (this.paramProperty);
    }

    public void setParamProperty(String paramProperty) {
        this.paramProperty = paramProperty;
    }

    public String getParamScope() {
        return (this.paramScope);
    }

    public void setParamScope(String paramScope) {
        this.paramScope = paramScope;
    }

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTarget() {
        return (this.target);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean getTransaction() {
        return (this.transaction);
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public String getIndexId() {
        return (this.indexId);
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public boolean isUseLocalEncoding() {
        return useLocalEncoding;
    }

    public void setUseLocalEncoding(boolean b) {
        useLocalEncoding = b;
    }
    public int doStartTag() throws JspException {
        this.text = null;
        this.parameters.clear();

        // Evaluate the body of this tag
        return (EVAL_BODY_TAG);
    }

    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            String value = bodyContent.getString().trim();

            if (value.length() > 0) {
                text = value;
            }
        }

        return (SKIP_BODY);
    }

     public int doEndTag() throws JspException {
        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a");

        // Special case for name anchors
        prepareAttribute(results, "name", getLinkName());

        // * @since Struts 1.1
        if ((getLinkName() == null) || (getForward() != null)
            || (getHref() != null) || (getPage() != null)
            || (getAction() != null)) {
            prepareAttribute(results, "href", calculateURL());
        }

        prepareAttribute(results, "target", getTarget());
        prepareAttribute(results, "accesskey", getAccesskey());
        prepareAttribute(results, "tabindex", getTabindex());
        results.append(prepareStyles());
        results.append(prepareEventHandlers());
        prepareOtherAttributes(results);
        results.append(">");

        if (text != null) {
            results.append(text);
        }
        results.append("</a>");
        TagUtils.getInstance().write(pageContext, results.toString());

        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        anchor = null;
        forward = null;
        href = null;
        linkName = null;
        name = null;
        page = null;
        action = null;
        module = null;
        paramId = null;
        paramName = null;
        paramProperty = null;
        paramScope = null;
        parameters.clear();
        property = null;
        scope = null;
        target = null;
        text = null;
        transaction = false;
        indexId = null;
        useLocalEncoding = false;
    }
    protected String calculateURL()
        throws JspException {
        // Identify the parameters we will add to the completed URL
        Map params =
            TagUtils.getInstance().computeParameters(pageContext, paramId,
                paramName, paramProperty, paramScope, name, property, scope,
                transaction);

        // Add parameters collected from the tag's inner body
        if (!this.parameters.isEmpty()) {
            if (params == null) {
                params = new HashMap();
            }
            params.putAll(this.parameters);
        }

        // if "indexed=true", add "index=x" parameter to query string
        // * @since Struts 1.1
        if (indexed) {
            int indexValue = getIndexValue();

            //calculate index, and add as a parameter
            if (params == null) {
                params = new HashMap(); //create new HashMap if no other params
            }

            if (indexId != null) {
                params.put(indexId, Integer.toString(indexValue));
            } else {
                params.put("index", Integer.toString(indexValue));
            }
        }

        String url = null;

        try {
            url = computeURLWithCharEncoding(pageContext,
                    forward, href, page, action, module, params, anchor, false,
                    useLocalEncoding);
        } catch (MalformedURLException e) {
            TagUtils.getInstance().saveException(pageContext, e);
          /*  throw new JspException(messages.getMessage("rewrite.url",
                    e.toString()));*/
        }

        return (url);
    }

    public String computeURLWithCharEncoding(PageContext pageContext,
            String forward, String href, String page, String action, String module,
            Map params, String anchor, boolean redirect, boolean useLocalEncoding)
            throws MalformedURLException {
            return TagUtils.getInstance().computeURLWithCharEncoding(pageContext, forward, href, page,
                action, module, params, anchor, redirect, true, useLocalEncoding);
        }

        public String computeURL(PageContext pageContext, String forward,
            String href, String page, String action, String module, Map params,
            String anchor, boolean redirect, boolean encodeSeparator)
            throws MalformedURLException {
            return TagUtils.getInstance().computeURLWithCharEncoding(pageContext, forward, href, page,
                action, module, params, anchor, redirect, encodeSeparator, false);
        }

    public void addParameter(String paramName, Object paramValue) {
        this.parameters.put(paramName, paramValue);
    }
}
