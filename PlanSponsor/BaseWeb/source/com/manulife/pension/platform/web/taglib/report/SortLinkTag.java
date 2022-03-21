package com.manulife.pension.platform.web.taglib.report;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.ReportConstants;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.platform.web.taglib.PrintFriendlyLinkTag;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This class renders a link that contains the necessary parameters to perform
 * a sort. It supports all of the Struts's Link Tag attributes except for the
 * <code>linkName</code> attribute. The following sort specific attributes
 * are also supported:
 * 
 * <ul>
 * <li>field - The field to sort</li>
 * <li>direction - The direction to sort. (Default is Ascending)</li>
 * <li>toggle - Whether the direction should toggle per click. (Default is
 * true)</li>
 * </ul>
 * 
 * @author Charles Chan
 */
public class SortLinkTag extends PrintFriendlyLinkTag {

	private String field;
	private String direction = ReportSort.ASC_DIRECTION;
	private boolean toggle = true;
	private static final String TASK_KEY = "task";
	private static final String SORT = "sort";
	private static final String SORT_FIELD_KEY = "sortField";
	private static final String SORT_DIRECTION_KEY = "sortDirection";
	private String formName = "actionForm";
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		field = null;
		direction = null;
		toggle = true;
	}

	/**
	 * @return Returns the direction.
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            The direction to set. Default is ascending.
	 * @see com.manulife.pension.platform.web.report.ReportConstants#ASC_DIRECTION
	 * @see com.manulife.pension.platform.web.report.ReportConstants#DESC_DIRECTION
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}

	/**
	 * @return Returns the sort field.
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            The sort field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return The current toggle value.
	 */
	public String getToggle() {
		return String.valueOf(toggle);
	}

	
	
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * Sets the toggle value. If toggle is true (default), the sort link will
	 * toggle between ascending and descending when it's clicked upon.
	 * 
	 * @param toggle
	 *            Toggle value.
	 */
	public void setToggle(String toggle) {
		this.toggle = Boolean.valueOf(toggle).booleanValue();
	}

	/**
	 * Writes the end tag. If this field represents the current sort field, an
	 * image (either an arrow up or arrow down) will be shown next to the link.
	 * 
	 * @see com.manulife.pension.platform.web.report.ReportConstants#ASC_SORT_IMAGE_URL
	 * @see com.manulife.pension.platform.web.report.ReportConstants#DESC_SORT_IMAGE_URL
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {

		super.doEndTag();

		ReportSort currentSort = getCurrentReportSort(pageContext);
		if (currentSort != null) {
			if (currentSort.getSortField().equals(getField())) {
				StringBuffer results = new StringBuffer();
	
				results.append("<img src=\"");
				results.append(currentSort.isAscending()
						? ReportConstants.ASC_SORT_IMAGE_URL
						: ReportConstants.DESC_SORT_IMAGE_URL);
				results.append("\">");
				TagUtils.getInstance().write(pageContext, results.toString());
			}
		}
		return (EVAL_PAGE);
	}

	/**
	 * Computes the result URL. This method overrides the Struts's method by
	 * adding sort specific parameters to the URL.
	 * 
	 * @see org.apache.struts.taglib.html.LinkTag#calculateURL()
	 */
	protected String calculateURL() throws JspException {
		// Identify the parameters we will add to the completed URL
		Map params = TagUtils.getInstance().computeParameters(pageContext, paramId,
				paramName, paramProperty, paramScope, name, property, scope,
				transaction);

		if (params == null) {
			params = new HashMap();
		}

		/*
		 * Check the current sort field in the form, if it's the same as this
		 * tag and toggle is true, toggle the sort direction.
		 */
		String sortDirection;
		ReportSort currentSort = getCurrentReportSort(pageContext);
		if (currentSort != null && currentSort.getSortField().equals(getField()) && toggle) {
			if (currentSort.isAscending()) {
				sortDirection = ReportSort.DESC_DIRECTION;
			} else {
				sortDirection = ReportSort.ASC_DIRECTION;
			}
		} else {
			sortDirection = getDirection();
		}
		params.put(TASK_KEY, SORT);
		params.put(SORT_FIELD_KEY, getField());
		params.put(SORT_DIRECTION_KEY, sortDirection);
		String url = null;

		/*
		 * If no path is specified, use the current path.
		 */
		if (forward == null && href == null && page == null && action == null) {
			setHref("");
		}

		try {
			/*url = org.apache.struts.taglib.TagUtils.getInstance().computeURL(pageContext, forward, href, page,
					action, module, params, anchor, false);*/
			url = TagUtils.getInstance().computeURL(pageContext, forward, href, page,
					action, module, params, anchor, false);
			
		} catch (MalformedURLException e) {
			throw new JspException(e.getMessage());
		}
		return BaseTagHelper.getPublicUrl(url);
	}

	/**
	 * Obtains the current report sort from the associated BaseReportForm.
	 * 
	 * @param pageContext
	 *            The page context to search for the form.
	 * @return The current ReportSort object.
	 * @throws JspException
	 *             if the form cannot be retrieved.
	 * @throws IllegalStateException
	 *             if the form is does not contain any sort field or sort
	 *             direction.
	 */
	protected ReportSort getCurrentReportSort(PageContext pageContext)
			throws JspException {
		/*
		 * Obtain the form associated with this request.
		 */
		BaseReportForm form = (BaseReportForm) BaseTagHelper
				.getSpringForm(pageContext,formName);

		/*
		 * Throws IllegalStateException if the form does not contain any sort
		 * field or sort direction.
		 */
		if (form.getSortField() == null || form.getSortDirection() == null) {
			return null;
		}

		return new ReportSort(form.getSortField(), form.getSortDirection());
	}
}
