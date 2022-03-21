package com.manulife.pension.platform.web.taglib.spring;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.servlet.tags.form.TagWriter;


public class FormTag extends org.springframework.web.servlet.tags.form.FormTag{

	/**
	 * Constructor.
	 *  
	 */
	public FormTag() {
		super();
	}
	private TagWriter tagWriter;
	
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		this.tagWriter = tagWriter;
		super.writeTagContent(this.tagWriter);
		RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
		ServletRequest request = this.pageContext.getRequest();
		if ((processor != null) && (request instanceof HttpServletRequest)) {
			writeHiddenFields(processor.getExtraHiddenFields((HttpServletRequest) request));
		}
		return EVAL_PAGE;
	}
	
	
	private void writeHiddenFields(Map<String, String> hiddenFields) throws JspException {
		if (hiddenFields != null) {
			for (String name : hiddenFields.keySet()) {
				this.tagWriter.appendValue("<input type=\"hidden\" ");
				this.tagWriter.appendValue("name=\"" + name + "\" value=\"" + hiddenFields.get(name) + "\" ");
				this.tagWriter.appendValue("/>\n");
			}
		}
	}
	
	
	@Override
	public int doEndTag() throws JspException {
		this.tagWriter.endTag();
		return EVAL_PAGE;
	}
	
	protected String renderFormStartElement() {
        HttpServletResponse response = (HttpServletResponse) this.pageContext
                .getResponse();
        
        StringBuffer results = new StringBuffer("<form");
        results.append(" name=\"");
        results.append(super.getModelAttribute());
        results.append("\"");
        results.append(" method=\"");
        results.append("GET");
        results.append("\" action=\"");
        String url = getAction();


        results.append(url);
        results.append("\"");

        if (getCssClass() != null) {
            results.append(" class=\"");
            results.append(getCssClass());
            results.append("\"");
        }
        if (getEnctype() != null) {
            results.append(" enctype=\"");
            results.append(getEnctype());
            results.append("\"");
        }
        if (getOnreset() != null) {
            results.append(" onreset=\"");
            results.append(getOnreset());
            results.append("\"");
        }
        if (getOnsubmit() != null) {
            results.append(" onsubmit=\"");
            results.append(getOnsubmit());
            results.append("\"");
        }
        if (getId() != null) {
            results.append(" id=\"");
            results.append(getId());
            results.append("\"");
        }
        if (getCssStyle() != null) {
            results.append(" style=\"");
            results.append(getCssStyle());
            results.append("\"");
        }
        /*if (styleId != null) {
            results.append(" id=\"");
            results.append(styleId);
            results.append("\"");
        }
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }*/
        
        results.append(" test = test>");
        System.out.println(results.toString());
        results.append("<input type=\"hidden\"/> ");
        results.append("<input type=\"hidden\" name=\"${_csrf.parameterName}\" value=\"${_csrf.token}\"/>");
        return results.toString();
    }
}
