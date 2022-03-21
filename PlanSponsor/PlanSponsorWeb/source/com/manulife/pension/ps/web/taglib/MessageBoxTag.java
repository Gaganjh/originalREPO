package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.view.MutableMiscellaneous;

/**
 * 
 * This class takes in a title Miscellaneous content and its text content
 * and will format it into a HowToLink box for the Plan Sponsor website
 * 
 * @author 	Tamilarasu K
 */

public class MessageBoxTag extends TagSupport {

	protected Category generalLog = Category.getInstance(getClass());	

	private String beanId;

	/**
	 * @return Returns SKIP_BODY
	 */	
	public int doStartTag() throws JspException {
		
		try {

			Collection<Miscellaneous> howTos = null;

            Miscellaneous titleBean = (MutableMiscellaneous)pageContext.getAttribute(getBeanId(),PageContext.REQUEST_SCOPE);
			if (titleBean != null ) {
				buildBox(titleBean);
			} else {
				//do nothing
			}

		} catch (IOException ex) {
			throw new JspException("doStartTag on HowToBoxTag failed: " + ex.getMessage());
		}
		return SKIP_BODY;
	}	
    
   

	/**
	 * @return the beanId
	 */
	public String getBeanId() {
		return beanId;
	}



	/**
	 * @param beanId the beanId to set
	 */
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}



	private void buildBox ( Miscellaneous title) throws IOException {

		JspWriter out = pageContext.getOut();

		out.println("<table width=\"180\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		out.println("<tr>");
		out.println("<td width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td width=\"4\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td width=\"170\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td width=\"4\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td width=\"1\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("</tr>");
		out.println("<tr class=\"whiteBox\">");
		out.println("  <td colspan=\"2\" rowspan=\"2\" valign=\"top\"><img src=\"/assets/unmanaged/images/box_ul_corner_box.gif\" width=\"5\" height=\"5\"></td>");
		out.println("  <td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("</tr>");
		out.println("<tr class=\"whiteBox\">");
		out.println("  <td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"4\"></td>");
		out.println("  <td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("</tr>");
		out.println("<tr class=\"whiteBox\">");
		out.println("  <td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("  <td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		//	                
		out.println("  <td>");
		out.println("<b>");
		if (title != null) {
			out.println(title.getTitle());
		} else {
			out.println("&nbsp;");
		}
		
		out.println("</b><br>");
		
		out.println("<ul class=\"noindent\">");
		StringBuffer buf = new StringBuffer();
			buf.append("<li>");
			String text = title.getText();
			buf.append(text);
			buf.append("</li>");
		out.println(buf.toString());
		out.println("</ul>");

		out.println("</td>");
		out.println("<td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("</tr>");
		out.println("<tr class=\"whiteBox\">");
		out.println("<td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"4\"></td>");
		out.println("<td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td colspan=\"2\" rowspan=\"2\" align=\"right\" valign=\"bottom\"><img src=\"/assets/unmanaged/images/box_lr_corner.gif\" width=\"5\" height=\"5\"></td>");
		out.println("</tr>");
		out.println("<tr class=\"whiteBox\">");
		out.println("<td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("<td class=\"whiteBoxborder\"><img src=\"/assets/unmanaged/images/s.gif\" width=\"1\" height=\"1\"></td>");
		out.println("</tr>");
		out.println("</table>");
	}



}
