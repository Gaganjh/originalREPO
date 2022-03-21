package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Category;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.content.view.MutableMiscellaneous;

/**
 * 
 * This class takes in a title Miscellaneous content and a collection of links,
 * and will format it into a HowToLink box for the Plan Sponsor website
 * 
 * @author 	Chris Shin
 */

public class HowToBoxTag extends TagSupport {

	protected Category generalLog = Category.getInstance(getClass());	

	private String howToLinks;
	private String howToTitle;
    private String excludes;

	/**
	 * @return Returns SKIP_BODY
	 */	
	public int doStartTag() throws JspException {
		
		try {

			Collection<Miscellaneous> howTos = null;
            String excludeIDs = null;

            howTos = (Collection<Miscellaneous>)pageContext.getAttribute(getHowToLinks(),PageContext.REQUEST_SCOPE);
            Miscellaneous titleBean = (MutableMiscellaneous)pageContext.getAttribute(getHowToTitle(),PageContext.REQUEST_SCOPE);
            if (getExcludes() != null) {
                excludeIDs = (String)pageContext.getAttribute(getExcludes());
            }

			if (howTos != null && howTos.size() > 0) {
                // remove excludes if any
                if (excludeIDs != null) {
                    howTos = doExclude(howTos,excludeIDs);
                }
                
				buildBox(howTos, titleBean);
			} else {
				//do nothing
			}

		} catch (IOException ex) {
			throw new JspException("doStartTag on HowToBoxTag failed: " + ex.getMessage());
		}
		return SKIP_BODY;
	}	
    
    /**
     * Exclude any howTos in the excludes List
     */
    private Collection doExclude(Collection<Miscellaneous> howTos,String excludeIDs) {
        Iterator iter = howTos.iterator();
        Collection<Miscellaneous> col = new ArrayList();
        while (iter.hasNext()) {
            Miscellaneous item = (Miscellaneous)iter.next();
            if (!excludeIDs.contains(String.valueOf(item.getKey()))) {
                col.add(item);
            }
        }
        return col;
    }
    
    /**
     * @return string
     */
    public String getHowToLinks() {
        return this.howToLinks;
    }

    /**
     * @param string
     */
    public void setHowToLinks(String howToLinks) {
        this.howToLinks = howToLinks;
    }

	
    /**
     * @return String
     */
    public String getHowToTitle() {
        return howToTitle;
    }

    /**
     * @param string
     */
    public void setHowToTitle(String howToTitle) {
        this.howToTitle = howToTitle;
    }

	private void buildBox (Collection howTos, Miscellaneous title) throws IOException {

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
		Iterator iter = howTos.iterator();
		
		out.println("<ul class=\"noindent\">");
		StringBuffer buf = new StringBuffer();
		while (iter.hasNext()) {
			buf.append("<li>");
			String text = ((Miscellaneous)iter.next()).getText();
			buf.append(text);
			buf.append("</li>");
		}
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

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }


}
