package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;

/**
 * Super class for all section related action.
 * 
 * @author guweigu
 * 
 */
abstract public class MCSectionController extends MCAbstractDetailTabController {

	protected MCSectionController() {
		logger = Logger.getLogger(MCSectionController.class);
	}

	public String doExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		int tabId = MCUtils.getId(request, MCConstants.ParamTabId);

		int sectionId = MCUtils.getId(request, MCConstants.ParamSectionId);

		MessageCenterComponent top = MCUtils.getMessageCenterTree(request.getServletContext());
		MessageCenterComponent selectedTab = top.getChild(tabId);
		MessageCenterComponent selectedSection = null;

		if (selectedTab != null) {
			selectedSection = selectedTab.getChild(sectionId);
		}

		if (selectedTab == null || selectedSection == null) {
			logger.error("The tab id or section are not valid = " + tabId + ","
					+ sectionId);
			return getSummaryForward( request);
		}

		MCPreference preference = MCUtils.getPreference(request);

		updatePreference(request, selectedTab, selectedSection, preference);

		MCDetailTabReportModel model = super.getDetailTabReportModel(request,
				preference, top, selectedTab);

		if (model == null || model.getSelectedTab() == null) {
			logger.error("The tab id is not valid = " + tabId);
			return getSummaryForward( request);
		}

		if (model.getTabMessageCount(model.getSelectedTab()) == 0) {
			return getSummaryForward( request);
		}

		request.setAttribute(MCConstants.AttrModel, model);
		return getDetailForward( request);
	}

	/**
	 * According the request, update the display preference
	 * 
	 * @param request
	 * @param selectedTab
	 * @param selectedSection
	 * @param pref
	 * @throws SystemException
	 */
	abstract protected void updatePreference(HttpServletRequest request,
			MessageCenterComponent selectedTab,
			MessageCenterComponent selectedSection, MCPreference pref)
			throws SystemException;
}
