package com.manulife.pension.platform.web.taglib.report;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.ReportConstants;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.service.report.sendservice.valueobject.ReportData;

/**
 * This class represents a page counter tag. The business requirements are as
 * follows:
 * 
 * <ul>
 * 
 * <li>Only the page numbers for which there is data on that page will show in
 * “Page 1 2 3” etc. (e.g. If there are only 2 pages of data, show “Page 1 2”,
 * not “Page 1 2 3”)</li>
 * 
 * <li>All page numbers in “Page 1 2 3” etc. except the currently showing page
 * are links to the page in question (e.g. if you are on page 2 of 4, then it
 * displays as “Page 1 2 3 4” and 1, 3 and 4 are links to pages 1, 3 and 4
 * respectively)</li>
 * 
 * <li>If there are between 1 and 5 pages of data, show “Page 1 2 3” etc. with
 * no arrows</li>
 * 
 * <li>If there are more than 5 pages of data, show a maximum of 5 page numbers
 * in “Page 1 2 3” etc.</li>
 * 
 * <li>If there more than 5 pages of data, show the Next and Previous arrows (
 * or ) as follows:
 * 
 * <ul>
 * 
 * <li>Next arrow takes you to the next group of pages. Previous arrow takes
 * you to the previous group of pages. A group is 5 or less pages of data as per
 * [d].</li>
 * 
 * <li>Next arrow () only appears if there is a group of pages to go to in that
 * direction. Similarly, Previous arrow () only appears if there is a group of
 * pages to go to in that direction. For example: if there are 13 pages of data,
 * the possibilities are: Page 1 2 3 4 5 Page 6 7 8 9 10 Page 11 12 13</li>
 * 
 * <li>Pushing the Next arrow takes you to the first page of the next group of
 * 5. (e.g. if you are on page 8 and you push Next, you go to page 11)</li>
 * 
 * <li>Pushing the Previous arrow takes you the last page of the previous group
 * of 5. (e.g. if you are on page 9 and you push Previous, you go to page 5)
 * </li>
 * 
 * </ul>
 * 
 * </li>
 * 
 * </ul>
 * 
 * @author Dheepa
 */
public class SendServicePageCounterTag extends SendServiceAbstractReportTag {
	
	private static final long serialVersionUID = 1L;

	private static final int PAGE_GROUP_SIZE = 5;

	private static final String PAGE = "Page";

	/*
	 * name refers to a bean that is contains a HashMap of additional parameters
	 * to the page link.
	 */
	private String name = null;
	
	private String anchor = null;

	private String arrowColor = "white";
	private String formName = "actionForm";
	private static Map ARROW_MAP = new HashMap();

	static {
		ARROW_MAP.put("white", new String[] {
				ReportConstants.LEFT_ARROW_IMAGE_URL,
				ReportConstants.RIGHT_ARROW_IMAGE_URL });
		ARROW_MAP.put("black", new String[] {
				ReportConstants.LEFT_BLACK_ARROW_IMAGE_URL,
				ReportConstants.RIGHT_BLACK_ARROW_IMAGE_URL });
	}

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the arrowColor.
	 */
	public String getArrowColor() {
		return arrowColor;
	}

	/**
	 * @param arrowColor The arrowColor to set.
	 */
	public void setArrowColor(String arrowColor) {
		this.arrowColor = arrowColor;
	}
	
	

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	private String getLeftArrowUrl() {
		return ((String[])ARROW_MAP.get(arrowColor))[0];
	}
	
	private String getRightArrowUrl() {
		return ((String[])ARROW_MAP.get(arrowColor))[1];
	}

	public int doStartTag() throws JspException {
		try {
			if (BaseTagHelper.isPrintFriendly(pageContext)) {
				return SKIP_BODY;
			}

			ReportData theReport = getReportData();

			/*
			 * Obtains the action form first.
			 */
			BaseReportForm actionForm = (BaseReportForm) BaseTagHelper
					.getSpringForm(pageContext,formName);

			/*
			 * Gets the current page number.
			 */
			int currentPage = actionForm.getPageNumber();

			/*
			 * Gets the total page count.
			 */
			int totalPageCount = theReport.getTotalPageCount();

			if (totalPageCount > 0) {

				String url = "?";
				if (name != null) {
					Object mapObj = TagUtils.getInstance()
							.lookup(pageContext, name, null);
					if (mapObj != null && mapObj instanceof HashMap) {
						HashMap map = (HashMap) mapObj;
						Iterator iter = map.keySet().iterator();
						while (iter.hasNext()) {
							Object o = iter.next();
							url += o + "=" + map.get(o) + "&";
						}
					}
				}

				url += "task=page&pageNumber=";

				JspWriter out = pageContext.getOut();

				int pageGroupStartPage = getPageGroupStartPage(currentPage);

				/*
				 * Beginning of page group to current page.
				 */
				out.print(PAGE);
				out.print("&nbsp;");

				/*
				 * Left arrow to navigate to the previous group of pages.
				 */
				if (pageGroupStartPage != 1) {
					out.print("<a href=\"");
					out.print(url);
					out.print(pageGroupStartPage - 1);
					if (anchor != null) out.print("#" + anchor);
					out.print("\">");
					out.print("<img src=\"");
					out.print(getLeftArrowUrl());
					out.print("\" border=\"0\"></a>&nbsp;");
				}

				for (int i = pageGroupStartPage; i < currentPage; i++) {
					out.print("<a href='");
					out.print(url);
					out.print(i);
					if (anchor != null) out.print("#" + anchor);
					out.print("'>");
					out.print(i);
					out.print("</a>&nbsp;");
				}

				/*
				 * Current Page.
				 */
				out.print("<b>" + currentPage + "</b>&nbsp;");

				/*
				 * Between current page and the end of page group.
				 */
				for (int i = currentPage + 1; i <= totalPageCount
						&& i < PAGE_GROUP_SIZE + pageGroupStartPage; i++) {
					out.print("<a href='");
					out.print(url);
					out.print(i);
					if (anchor != null) out.print("#" + anchor);
					out.print("'>");
					out.print(i);
					out.print("</a>");
					out.print("&nbsp;");
				}

				/*
				 * Right arrow to navigate to the next group of pages.
				 */
				if (PAGE_GROUP_SIZE + pageGroupStartPage <= totalPageCount) {
					out.print("<a href=\"");
					out.print(url);
					out.print(PAGE_GROUP_SIZE + pageGroupStartPage);
					if (anchor != null) out.print("#" + anchor);
					out.print("\">");
					out.print("<img src=\"");
					out.print(getRightArrowUrl());
					out.print("\" border=\"0\"></a>");
				}
			}

		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * Obtains the starting page number for the current page group.
	 * 
	 * @param currentPage
	 *            The current selected page number (starts from 1).
	 * @return The starting page number for the current page group.
	 */
	private int getPageGroupStartPage(int currentPage) {
		return ((currentPage - 1) / PAGE_GROUP_SIZE) * PAGE_GROUP_SIZE + 1;
	}
	/**
	 * @return Returns the anchor.
	 */
	public String getAnchor() {
		return anchor;
	}
	/**
	 * @param anchor The anchor to set.
	 */
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
}
