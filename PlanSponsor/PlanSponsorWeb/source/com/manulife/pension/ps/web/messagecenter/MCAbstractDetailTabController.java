package com.manulife.pension.ps.web.messagecenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.model.MCDetailTabReportModel;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCSectionPreference;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.message.report.valueobject.RecipientMessageReportData;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Abstract super class for Detail Tab related actions.
 * 
 * Implements the assemble of DetailTabReportModel
 * 
 * @author guweigu
 * 
 */
abstract public class MCAbstractDetailTabController extends MCAbstractController {

	protected MCAbstractDetailTabController() {
		logger = Logger.getLogger(MCAbstractDetailTabController.class);
	}

	/**
	 * Get the BizTabReportModel based on the request parameter on the detail
	 * tab and selected section.
	 * 
	 * @param request
	 * @param tabId
	 * @return
	 * @throws SystemException
	 */
	protected MCDetailTabReportModel getDetailTabReportModel(
			HttpServletRequest request, MCPreference preference,
			MessageCenterComponent top, MessageCenterComponent selectedTab)
			throws SystemException {

		// preference is already set up
		// clear the selection
		MCUtils.getPreferencesHolder(request).clearSelection();
		MCPreference pref = super.getPreferenceOnRequest(request, preference);
		if (selectedTab == null) {
			return null;
		}
		pref.setSelectedTabId(selectedTab.getId());

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Collection<Integer> contracts = MCUtils.getContractList(userProfile);
		if (contracts.isEmpty()) {
			return null;
		}

		List<ReportCriteria> sectionsCriteria = new ArrayList<ReportCriteria>(
				selectedTab.getChildrenSize());

		for (MessageCenterComponent section : selectedTab.getChildren()) {
			ReportCriteria criteria = new ReportCriteria(
					RecipientMessageReportData.REPORT_ID);
			criteria.addFilter(
					RecipientMessageReportData.FILTER_APPLICATION_ID, top
							.getId().getValue());
			criteria.addFilter(
					RecipientMessageReportData.FILTER_USER_PROFILE_ID,
					userProfile.getPrincipal().getProfileId());
			criteria.addFilter(RecipientMessageReportData.FILTER_CONTRACT_LIST,
					contracts);
			criteria.addFilter(RecipientMessageReportData.FILTER_SECTION_ID,
					section.getId().getValue());

			if ( MCUtils.isInGlobalContext(request) && userProfile.getRole().isTPA() ) {
				Collection<Integer> tpaFirmIds = userProfile.getMessageCenterTpaFirms();
				if ( tpaFirmIds == null ) {
					tpaFirmIds = new ArrayList<Integer>();
				}
				criteria.addFilter(RecipientMessageReportData.FILTER_TPA_FIRM_LIST,
						tpaFirmIds);
				
			}

			MCSectionPreference sectionPref = pref
					.getSectionPreference(section);
			criteria.setPageNumber(1);
			setPageSize(criteria, sectionPref);
			setSortList(criteria, sectionPref);
			sectionsCriteria.add(criteria);
		}
		MCDetailTabReportModel model = new MCDetailTabReportModel(top, pref,
				selectedTab, getMessageServiceFacade().getDetails(
						sectionsCriteria), MCUtils.isInGlobalContext(request));
		model.setTabsSummary(getMessageServiceFacade().getAllTabSummaries(request,
				userProfile, top.getId(), true));
		return model;
	}
}
