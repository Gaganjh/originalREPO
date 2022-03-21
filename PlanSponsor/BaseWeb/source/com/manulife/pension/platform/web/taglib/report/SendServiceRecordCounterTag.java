package com.manulife.pension.platform.web.taglib.report;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.manulife.pension.ps.service.report.sendservice.valueobject.ReportData;


/**
 * @author Dheepa
 */

public class SendServiceRecordCounterTag extends SendServiceAbstractReportTag {
	
	private static final long serialVersionUID = 1L;
	private boolean totalOnly;
	private String label;

	public String getLabel() {
		return label;
	}

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

				 
				if (totalOnly) {
					pageContext.getOut().print(theReport.getTotalCount());
				} else {
					 
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