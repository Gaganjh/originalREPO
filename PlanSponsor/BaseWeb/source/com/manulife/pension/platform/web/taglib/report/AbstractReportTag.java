package com.manulife.pension.platform.web.taglib.report;

import javax.servlet.jsp.tagext.BodyTagSupport;

import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Charles Chan
 */
public abstract class AbstractReportTag extends BodyTagSupport {

	protected String report;

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		report = null;
	}

	/**
	 * Gets the reportBean
	 * 
	 * @return Returns a String
	 */
	public String getReport() {
		return report;
	}

	/**
	 * Sets the reportBean
	 * 
	 * @param reportBean
	 *            The reportBean to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * gets the actual bean associated with the report name
	 */
	public ReportData getReportData() {
		return (ReportData) pageContext.getAttribute(getReport());
	}
}
