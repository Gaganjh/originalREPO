package com.manulife.pension.ps.web.taglib.census;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

public class EmployeeSnapshotValidationErrorMessageTag extends TagSupport {
    private static final Logger log = Logger.getLogger(EmployeeSnapshotValidationErrorMessageTag.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int doEndTag() throws JspException {
        ValidationError error = (ValidationError) pageContext.findAttribute(name);
        try {
            if (error != null) {
                if (error.getType().compareTo(Type.error) == 0) {
                    pageContext.getOut().write(
                            "<img alt='Error' src='/assets/unmanaged/images/error.gif'> ");
                } else if (error.getType().compareTo(Type.warning) == 0) {
                    pageContext.getOut().write(
                            "<img alt='Warning' src='/assets/unmanaged/images/warning2.gif'> ");
                } else if (error.getType().compareTo(Type.alert) == 0) {
                    pageContext.getOut().write(
                    "<img alt='Alert' src='/assets/unmanaged/images/warning2.gif'> ");
                }

                String msg = MessageProvider.getInstance().getMessage(error);
                pageContext.getOut().write(msg);
            }
        } catch (IOException e) {
            throw new JspException(e);
        } catch (ContentException e) {
            log.error("Got an Content Exception while processing tag", e);
            throw new JspException(e.toString());
        }
        return super.doEndTag();
    }
}
