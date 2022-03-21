package com.manulife.pension.platform.web.taglib.report;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.platform.web.taglib.BaseTagHelper;
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
 * @author Venkata Akarapu
 */
public class BDSortLinkViaSubmitTag extends BDSortLinkTag {


	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		
	}


	/**
	 * Computes the result URL. This method overrides the Struts's method by
	 * adding sort specific parameters to the URL.
	 * 
	 * 
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
		ReportSort currentSort = getCurrentReportSort(pageContext, getFormName());
		if (currentSort != null && currentSort.getSortField().equals(getField()) && Boolean.valueOf(getToggle()).booleanValue()) {
			if (currentSort.isAscending()) {
				sortDirection = ReportSort.DESC_DIRECTION;
			} else {
				sortDirection = ReportSort.ASC_DIRECTION;
			}
		} else {
			sortDirection = getDirection();
		}
		
		String url = null;

		/*
		 * If no path is specified, use the current path.
		 */
		if (forward == null && href == null && page == null && action == null) {
			setHref("");
		}

		url = "javascript: sortSubmit('" + getField() + "','" + sortDirection + "')";
		
		return BaseTagHelper.getPublicUrl(url);
	}

}
