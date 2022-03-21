package com.manulife.pension.ps.web.report;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;

/**
 * Base Action class for all reporting sub classes.
 */
public abstract class ReportController extends BaseReportController {

	protected ReportController() {
		super(ReportController.class);
	}

	public ReportController(Class clazz) {
		super(clazz);
	}

	protected static UserProfile getUserProfile(HttpServletRequest request) {
		return PsController.getUserProfile(request);
	}
}