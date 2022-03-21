package com.manulife.pension.ps.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import com.manulife.pension.ps.web.util.Environment;

/**
 * 
 * This class is taken from the ezk Participant site.  CS
 * 
 * This class write the correct company name to the output stream
 * Manulife USA or Manulife New York
 * 
 * @author 	Jeff Saremi
 */
public class CompanyNameTag extends TagSupport {
	protected Category generalLog = Category.getInstance(getClass());	
	/**
	 * @return Returns SKIP_BODY
	 */	
	public int doStartTag() throws JspException {
		
		try {
			String companyName = Environment.getInstance().getCompanyName();
			pageContext.getOut().print(companyName);
		} catch (IOException ex) {
			throw new JspTagException(ex.getMessage());
		}
		return SKIP_BODY;
	}	
}



