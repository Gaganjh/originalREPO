package com.manulife.pension.ps.web.taglib;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.taglib.util.QuickReportItem;
import com.manulife.pension.ps.web.taglib.util.QuickReportLabel;
import com.manulife.pension.ps.web.taglib.util.QuickReportSeparator;
import com.manulife.pension.ps.web.taglib.util.QuickReportsList;
import com.manulife.pension.ps.web.util.SessionHelper;

/**
 * Tag for creation of report list.
 * 
 * Pretty simple usage:
 * 
 * <code>
 * &lt;render:reportList id="rlist" /&gt;
 * </code>
 * 
 * @author grouzan
 * @author Charles Chan
 */
public class QuickReportsListTag extends BodyTagSupport {

	private String id;

	private static final String SEPARATOR = "------------------------------------------";

	/**
	 * Called after processing of body content is complete. We use it to output
	 * the content we built up during processing of the body content.
	 * 
	 * @return EVAL_PAGE
	 * @throws JspException
	 */
	public final int doEndTag() throws JspException {

		UserProfile user = SessionHelper
				.getUserProfile((HttpServletRequest) pageContext.getRequest());

		List reportList = user.getContractProfile().getQuickReportList();

		if (reportList == null) {
			try {
				reportList = QuickReportsList.getInstance().getReports(user);
			} catch (SystemException e) {
				throw new JspException(e.getMessage());
			}
			user.getContractProfile().setQuickReportList(reportList);
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<select name=\"").append(getId()).append(
				"\"  onChange=\"MM_jumpMenu('parent',this,1)\""
						+ " onBlur=\"this.selectedIndex=0;\">");

		if (reportList != null) {
			for (Iterator it = reportList.iterator(); it.hasNext();) {
				Object obj = it.next();
				//sb.append("<option value=\"");
				if (obj instanceof QuickReportItem) {
					sb.append("<option value=\"");
					QuickReportItem rep = (QuickReportItem) obj;
					sb.append(rep.getUrl()).append("\">")
							.append(rep.getTitle());
				} else if (obj instanceof QuickReportLabel) {
					sb.append("<option value=\"");
					sb.append("#\">")
							.append(((QuickReportLabel) obj).getText());
				} else if (obj instanceof QuickReportSeparator) {
					sb.append("<option value=\"");
					sb.append("##\">").append(SEPARATOR);
				}
				sb.append("</option>");
			}
		}

		sb.append("</select>");

		try {
			pageContext.getOut().print(sb.toString());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}

		return EVAL_PAGE;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}
}