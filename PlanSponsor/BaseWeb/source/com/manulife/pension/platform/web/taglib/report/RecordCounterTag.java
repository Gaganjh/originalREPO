package com.manulife.pension.platform.web.taglib.report;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Charles Chan
 */
public class RecordCounterTag extends AbstractReportTag {

	private boolean totalOnly;
	private String label;

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public String getTotalOnly() {
		return String.valueOf(totalOnly);
	}

	public void setTotalOnly(String totalOnly) {
		this.totalOnly = Boolean.valueOf(totalOnly).booleanValue();
	}

	public int doStartTag() throws JspException {
		try {
			ReportData theReport = getReportData();
			if (theReport.getTotalCount() > 0) {

				if (label != null) {
					pageContext.getOut().print(label);
					pageContext.getOut().print("&nbsp;");
				}

				/*
				 * If we are only interested in the total count, we only output
				 * the total count.
				 */
				if (totalOnly) {
					pageContext.getOut().print(theReport.getTotalCount());
				} else {
					/*
					 * Otherwise, we output xxx - yyy of zzz where xxx is the
					 * start index, yyy is the stop index, and zzz is the total
					 * count.
					 */
					pageContext.getOut().print(
							theReport.getStartIndex() + "-"
									+ theReport.getStopIndex()
									+ "&nbsp;of&nbsp;"
									+ theReport.getTotalCount());
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
}
