package com.manulife.pension.platform.web.taglib.report;

import javax.servlet.jsp.tagext.BodyTagSupport;

import com.manulife.pension.ps.service.report.sendservice.valueobject.ReportData;

/**
 * @author Dheepa
 */

public class SendServiceAbstractReportTag extends BodyTagSupport {
	
	private static final long serialVersionUID = 1L;
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


