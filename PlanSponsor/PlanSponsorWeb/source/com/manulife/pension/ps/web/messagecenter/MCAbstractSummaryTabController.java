package com.manulife.pension.ps.web.messagecenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCSectionPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCSummaryTabReportModel;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.message.report.valueobject.RecipientMessageReportData;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.MessageTemplate.Priority;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Abstract super class for all Summary tab related action
 * 
 * Implements the assemble of SummaryTabReportModel
 * 
 * @author guweigu
 * 
 */
abstract public class MCAbstractSummaryTabController extends MCAbstractController {


	protected MCAbstractSummaryTabController() {
		logger = Logger.getLogger(MCAbstractSummaryTabController.class);
	}
	
	/**
	 * Assembles the SummaryTabReportModel based on the request parameter
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	protected MCSummaryTabReportModel getSummaryTabReportModel(
			HttpServletRequest request, MCPreference preference)
			throws SystemException {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest req = attr.getRequest();
		MessageCenterComponent top = MCUtils.getMessageCenterTree((ServletContext)req.getServletContext());
		// preference is already set up
		// clear the selection
		MCUtils.getPreferencesHolder(request).clearSelection();
		MCPreference pref = super.getPreferenceOnRequest(request, preference);

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		Collection<Integer> contracts = MCUtils.getContractList(userProfile);
		// can not retrieve messages
		if (contracts.isEmpty()) {
			return null;
		}

		ReportCriteria criteria = new ReportCriteria(
				RecipientMessageReportData.REPORT_ID);
		criteria.addFilter(
				RecipientMessageReportData.FILTER_APPLICATION_ID, top
						.getId().getValue());
		MessageContainer urgentMessageContainer;
		criteria.addFilter(
				RecipientMessageReportData.FILTER_USER_PROFILE_ID,
				userProfile.getPrincipal().getProfileId());
		criteria.addFilter(
				RecipientMessageReportData.FILTER_PRIORITY,
				Priority.URGENT);
		criteria.addFilter(
				RecipientMessageReportData.FILTER_CONTRACT_LIST,
				contracts);
		
		
		if ( MCUtils.isInGlobalContext(request) && userProfile.getRole().isTPA() ) {
			Collection<Integer> firmIds = userProfile.getMessageCenterTpaFirms();
			if ( firmIds == null ) {
				firmIds = new ArrayList<Integer>();
			}
			criteria.addFilter(
					RecipientMessageReportData.FILTER_TPA_FIRM_LIST,
					firmIds);
		}
		top = MCUtils.deleteNoticeManagerTab(request, userProfile, top);
		
		MCSectionPreference sectionPref = pref
				.getSectionPreference(UrgentMessageSection);
		criteria.setPageNumber(1);
		setPageSize(criteria, sectionPref);
		setSortList(criteria, sectionPref);

		urgentMessageContainer = getMessageServiceFacade()
				.getUrgentMessages(criteria);

		Set<MessageContainerSummary> summaries = getMessageServiceFacade()
				.getAllSectionSummaries(request, userProfile, top.getId());

		MCSummaryTabReportModel model = new MCSummaryTabReportModel(top, pref,
				summaries, urgentMessageContainer, MCUtils.isInGlobalContext(request));
		return model;
	}
}
