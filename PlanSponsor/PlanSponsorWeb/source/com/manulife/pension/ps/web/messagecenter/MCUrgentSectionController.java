package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;

/**
 * Super class Action for all Urgent message section related actions
 * 
 * @author guweigu
 *
 */
abstract public class MCUrgentSectionController extends MCAbstractSummaryTabController {

	protected MCUrgentSectionController() {
		logger = Logger.getLogger(MCUrgentSectionController.class);
	}

	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		MCPreference preference = MCUtils.getPreference(request);
		updatePreference(request, preference);
		MCSummaryTabReportModel model = getSummaryTabReportModel(request, preference);

		if (model == null) {
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		request.setAttribute(MCConstants.AttrModel, model);
		return getSummaryForward( request);
	}


	abstract protected void updatePreference(HttpServletRequest request,
			MCPreference preference);
}
