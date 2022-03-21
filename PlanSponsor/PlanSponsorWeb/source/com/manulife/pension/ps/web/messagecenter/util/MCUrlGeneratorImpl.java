package com.manulife.pension.ps.web.messagecenter.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel;
import com.manulife.pension.ps.web.messagecenter.model.MCSectionPreference;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;

public class MCUrlGeneratorImpl extends MCUrlGenerator implements MCConstants {

	public String getInfoUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message) {
		if (tab == null) {
			return getUrl(ActionUrl, new String[] { ParamMessageId, ParamUrlType },
					new Object[] { message.getId(), MessageActionType.FYI.getValue() });
		} else {
			return getUrl(ActionUrl, new String[] { ParamTabId, ParamSectionId,
					ParamMessageId, ParamUrlType }, new Object[] {
					tab.getId().getValue(), section.getId().getValue(),
					message.getId(), MessageActionType.FYI.getValue() });
		}
	}

	public String getActionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message) {
		if (tab == null) {
			return getUrl(ActionUrl, new String[] { ParamMessageId, ParamUrlType },
					new Object[] { message.getId(), MessageActionType.ACTION.getValue() });

		} else {
			return getUrl(ActionUrl, new String[] { ParamTabId, ParamSectionId,
					ParamMessageId, ParamUrlType }, new Object[] {
					tab.getId().getValue(), section.getId().getValue(),
					message.getId(), MessageActionType.ACTION.getValue() });
		}
	}

	public String getRemoveMessageUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message) {
		if (tab == null) {
			return getUrl(RemoveMessageUrl, new String[] { ParamMessageId },
					new Object[] { message.getId() });

		} else {
			return getUrl(RemoveMessageUrl, new String[] { ParamTabId,
					ParamSectionId, ParamMessageId }, new Object[] {
					tab.getId().getValue(), section.getId().getValue(),
					message.getId() });
		}
	}

	public String getCompleteMessageUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message) {
		if (tab == null) {
			return getUrl(CompleteMessageUrl, new String[] { ParamMessageId },
					new Object[] { message.getId() });
		} else {
			return getUrl(CompleteMessageUrl, new String[] { ParamTabId,
					ParamSectionId, ParamMessageId }, new Object[] {
					tab.getId().getValue(), section.getId().getValue(),
					message.getId() });
		}
	}

	public String getTabUrl(MessageCenterComponentId tabId) {
		// summary
		if (MCUtils.isSummaryTab(tabId)) {
			return SummaryTabUrl;
		} else {
			return getUrl(DetailTabUrl, new String[] { ParamTabId },
					new Object[] { tabId.getValue() });
		}
	}

	public String getDetailSectionUrl(MessageCenterComponentId tabId,
			MessageCenterComponentId sectionId) {
		// summary
		if (MCUtils.isSummaryTab(tabId)) {
			return SummaryTabUrl;
		} else {
			return getUrl(DetailTabUrl, new String[] { ParamTabId,
					ParamSectionId }, new Object[] { tabId.getValue(),
					sectionId.getValue() }, "Section" + sectionId.getValue());
		}
	}

	public String getSelectSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section) {
		return getUrl(SectionSelectUrl, new String[] { ParamTabId,
				ParamSectionId }, new Object[] { tab.getId().getValue(),
				section.getId().getValue() }, "Section"
				+ section.getId().getValue());
	}

	public String getExpandSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, boolean expand) {
		return getUrl(SectionExpandUrl, new String[] { ParamTabId,
				ParamSectionId, ParamExpand }, new Object[] {
				tab.getId().getValue(), section.getId().getValue(), expand },
				"Section" + section.getId().getValue());
	}

	public String getShowAllSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, boolean showAll) {
		return getUrl(SectionShowAllUrl, new String[] { ParamTabId,
				ParamSectionId, ParamShowAll }, new Object[] {
				tab.getId().getValue(), section.getId().getValue(), showAll },
				"Section" + section.getId().getValue());
	}

	public String getSortingUrl(MCAbstractReportModel model,
			MessageCenterComponent section, String attrName) {
		MCSectionPreference pref = model.getPreference().getSectionPreference(
				section);
		Boolean defaultSortOrder = model.getDefaultSortOrder(attrName);
		boolean ascending = (defaultSortOrder != null) ? defaultSortOrder
				: true;
		if (attrName.equals(pref.getPrimarySortAttribute())) {
			ascending = !pref.isAscending();
		}
		return getUrl(SectionSortUrl, new String[] { ParamTabId,
				ParamSectionId, ParamSortKey, ParamSortOrder }, new Object[] {
				model.getSelectedTab().getId().getValue(),
				section.getId().getValue(), attrName, ascending }, "Section"
				+ section.getId().getValue());
	}

	public String getUrgentMessageSortingUrl(MCAbstractReportModel model,
			String attrName) {
		MCSectionPreference pref = model.getPreference().getSectionPreference(
				UrgentMessageSection);
		Boolean defaultSortOrder = model.getDefaultSortOrder(attrName);
		boolean ascending = (defaultSortOrder != null) ? defaultSortOrder
				: true;
		if (attrName.equals(pref.getPrimarySortAttribute())) {
			ascending = !pref.isAscending();
		}

		return getUrl(UrgentMessageSortUrl, new String[] { ParamSortKey,
				ParamSortOrder }, new Object[] { attrName, ascending });

	}

	public String getUrgentMessageShowAllUrl(boolean showAll) {
		return getUrl(UrgentMessageShowAllUrl, new String[] { ParamShowAll },
				new String[] { Boolean.toString(showAll) });
	}

	public String getTabPrintFriendlyUrl(MessageCenterComponent tab) {
		if (MCUtils.isSummaryTab(tab)) {
			return getUrl(SummaryTabUrl, new String[] { "printFriendly" },
					new String[] { "true" });
		} else {
			return getUrl(DetailTabUrl, new String[] { ParamTabId,
					ParamPrintFriendly }, new Object[] {
					tab.getId().getValue(), "true" });
		}
	}

	/**
	 * append parameter and anchor to the base url
	 * 
	 * @param baseUrl
	 * @param paramNames
	 * @param paramValues
	 * @param anchor
	 * @return
	 */
	protected String getUrl(String baseUrl, String[] paramNames,
			Object[] paramValues, String anchor) {
		boolean baseContainsParam = StringUtils.contains(baseUrl, '?');
		try {
			StringBuffer buf = new StringBuffer();
			if (paramNames != null && paramValues != null) {
				if (paramNames.length != paramValues.length) {
					throw new RuntimeException(
							"Parameter names and values array must be the same length : Names = "
									+ paramNames + " Values = " + paramValues);
				}
				for (int i = 0; i < paramNames.length; i++) {
					buf.append(URLEncoder.encode(paramNames[i], "UTF-8"));
					buf.append("=");
					buf.append(URLEncoder.encode(paramValues[i].toString(),
							"UTF-8"));
					buf.append("&");
				}
			}
			String params = "";
			// remove the last char, either ? or &
			if (buf.length() > 0) {
				buf.setLength(buf.length() - 1);
				if (baseContainsParam) {
					params = "&" + buf.toString();
				} else {
					params = "?" + buf.toString();
				}
			}

			/*
			 * NOTE: IE6 has a problem with redirect URL that contains anchor.
			 * It will make the anchor as part of the last request parameter. To
			 * workaround this problem, add a & after the last parameter
			 */
			if (anchor != null && anchor.length() > 0) {
				if (StringUtils.isEmpty(params) && !baseContainsParam) {
					params += "?#" + anchor;
				} else {
					params += "&#" + anchor;
				}
			}
			return baseUrl + params;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UTF-8 encoding must be supported, should not happen", e);
		}
	}

	protected String getUrl(String baseUrl, String[] paramNames,
			Object[] paramValues) {
		return getUrl(baseUrl, paramNames, paramValues, null);
	}

	protected String getUrl(String baseUrl, String anchor) {
		return getUrl(baseUrl, null, null, anchor);
	}

	public String getSectionAnchorName(MessageCenterComponent section) {
		return SectionAnchorPrefix + section.getId().getValue();
	}

	@Override
	public String getTabMessageUrl(MessageCenterComponent tab, String anchorName) {
		return getUrl(getTabUrl(tab), anchorName);
	}

	public String getVisitMessageBaseUrl(MessageCenterComponent tab) {
		return getUrl(VisitMessageUrl, new String[] { ParamTabId },
				new Object[] { tab.getId().getValue() });
	}

	@Override
	public String getRedirectSectionUrl(int sectionId) {
		return getUrl(RedirectSectionUrl, new String[] { ParamSectionId },
				new Object[] { sectionId });
	}
}
