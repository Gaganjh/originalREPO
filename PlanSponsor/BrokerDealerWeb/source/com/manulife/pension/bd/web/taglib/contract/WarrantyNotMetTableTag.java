package com.manulife.pension.bd.web.taglib.contract;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import com.manulife.pension.platform.web.taglib.home.BaseWarrantyNotMetTableTag;

/**
 * This class is the tag that will build the warrantyNotMet table
 * LS Fall 2006 fund launch. Added life asset grouping for warranty validation
 **/


public class WarrantyNotMetTableTag extends BaseWarrantyNotMetTableTag {

	
	protected void generateTable(List firstList, List secondList) throws IOException {
		
		JspWriter out = pageContext.getOut();

		out.println("<table width=\"50%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
       	out.println("<tr>");
        out.println(generateList(firstList));
       	out.println("</td>");
       	out.println(generateList(secondList)); 
       	out.println("</tr>");
       	out.println("</table>");
	}	
}
