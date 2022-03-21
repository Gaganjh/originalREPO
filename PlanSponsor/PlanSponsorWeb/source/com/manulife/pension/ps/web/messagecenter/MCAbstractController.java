package com.manulife.pension.ps.web.messagecenter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCPrintFriendlyPrefernce;
import com.manulife.pension.ps.web.messagecenter.model.MCSectionPreference;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacade;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.service.message.report.valueobject.RecipientMessageReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * Abstract class for all the action related to in message center.
 * 
 * To support print friendly on the model
 * 
 * @author guweigu
 * 
 */
abstract public class MCAbstractController extends PsController implements MCConstants {

	private static final String[] SortFieldOrder = {
			RecipientMessageReportData.SORT_FIELD_PRIORITY,
			RecipientMessageReportData.SORT_FIELD_EFFECTIVE_TS,
			RecipientMessageReportData.SORT_FIELD_SHORT_TEXT ,
			RecipientMessageReportData.SORT_FIELD_CONTRACT_ID,
			RecipientMessageReportData.SORT_FIELD_CONTRACT_NAME};

	private static final String[] SortDirectionOrder = { ReportSort.DESC_DIRECTION, ReportSort.DESC_DIRECTION,
			ReportSort.ASC_DIRECTION, ReportSort.ASC_DIRECTION, ReportSort.ASC_DIRECTION };
	
	protected MCAbstractController() {
		super(MCAbstractController.class);
	}
	
	protected MessageServiceFacade getMessageServiceFacade() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest req = attr.getRequest();
		return MessageServiceFacadeFactory.getInstance((ServletContext)req.getServletContext());
	}

	/**
	 * Set the correction information for the model in the case of print
	 * friendly mode
	 * 
	 * @param request
	 * @param model
	 */
	protected void setPrintFriendly(HttpServletRequest request,
			MCAbstractReportModel model) {
		if ("true".equals(request.getParameter(ParamPrintFriendly))) {
			model.setNavigatable(false);
			model.setPrintFriendly(true);
		} else {
			model.setPrintFriendly(false);
		}
	}

	/**
	 * Returns the MCPreference object based on the request. If the request is
	 * for print-friendly page, then it returns a print-friendly preference.
	 * Otherwise, it returns the original preference
	 * 
	 * @param request
	 * @param preference
	 * @return
	 */
	protected MCPreference getPreferenceOnRequest(HttpServletRequest request,
			MCPreference preference) {
		if ("true".equals(request.getParameter(ParamPrintFriendly))) {
			return new MCPrintFriendlyPrefernce(preference);
		} else {
			return preference;
		}
	}

	/**
	 * Set the page size for the report
	 * 
	 * @param criteria
	 * @param sectionPref
	 */
	protected void setPageSize(ReportCriteria criteria,
			MCSectionPreference sectionPref) {
		if (sectionPref.isShowAll()) {
			criteria.setPageSize(MCEnvironment.getMaximumMessageCount());
		} else {
			criteria.setPageSize(sectionPref.getDefaultMessageCount());
		}
	}

	/**
	 * Set the sort list for the report
	 * 
	 * @param criteria
	 * @param sectionPref
	 */
	protected void setSortList(ReportCriteria criteria,
			MCSectionPreference sectionPref) {
		String field = sectionPref.getPrimarySortAttribute();
		String direction = sectionPref.isAscending() ? ReportSort.ASC_DIRECTION
				: ReportSort.DESC_DIRECTION;

		ReportSortList sortList = new ReportSortList();
			sortList.add(new ReportSort(field, direction));
		for (int i = 0; i < SortFieldOrder.length; i++) {
			if (!SortFieldOrder[i].equals(field)) {
				sortList.add(new ReportSort(SortFieldOrder[i],
						SortDirectionOrder[i]));
			}
		}
		criteria.setSorts(sortList);
	}
	
	/**
	 * The forward to summary page
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected String getSummaryForward(
			HttpServletRequest request) {
		return MCUtils.isInGlobalContext(request) ? "multiSummary" : "summary";
	}

	/**
	 * The forward to detail page
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected String getDetailForward(
			HttpServletRequest request) {
		return MCUtils.isInGlobalContext(request) ? "multiDetail" : "detail";
	}
}
