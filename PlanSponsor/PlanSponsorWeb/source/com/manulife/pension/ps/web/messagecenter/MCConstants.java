package com.manulife.pension.ps.web.messagecenter;

import com.manulife.pension.service.message.report.valueobject.RecipientMessageReportData;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentImpl;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.message.valueobject.EmailPreference.EmailFormat;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent.Type;
import com.manulife.util.render.RenderConstants;

/**
 * MessageCenter related constants
 * 
 * @author guweigu
 * 
 */
public interface MCConstants {	
	// Message Center URLs
	String DispatchUrl = "/do/mcdispatch/";
	String DispathUrlfromGlobal = "/do/mcdispatch/global";
	String SummaryTabUrl = "/do/messagecenter/summary";
	String DetailTabUrl = "/do/messagecenter/detail";
	String SectionSelectUrl = "/do/messagecenter/sectionSelect";
	String SectionExpandUrl = "/do/messagecenter/sectionExpand";
	String SectionSortUrl = "/do/messagecenter/sectionSort";
	String SectionShowAllUrl = "/do/messagecenter/sectionShowAll";
	String UrgentMessageSortUrl = "/do/messagecenter/urgentMessageSort";
	String UrgentMessageShowAllUrl = "/do/messagecenter/urgentMessageShowAll";
	String ActionUrl = "/do/messagecenter/actMessage";
	String RemoveMessageUrl = "/do/messagecenter/removeMessage";
	String CompleteMessageUrl = "/do/messagecenter/completeMessage";
	String VisitMessageUrl = "/do/messagecenter/visitMessage";
	String RedirectSectionUrl = "/do/messagecenter/sectionRedirect";	
	String PersonalizationUrl = "/do/messagecenter/personalizeEmail";
	String PersonalizationNoticeUrl = "/do/messagecenter/personalizeNotice";
	String PersonalizationMessageUrl = "/do/mcPersonalizeMessage";
	String TPAMessagePreferencesUrl = "/do/messagecenter/tpaMessagePreferences";
	String ArchiveUrl = "/do/messagecenter/history";
	String CarViewUrl = "/do/mcCarView";
	String CarViewsEmailPrefs = "	/do/mcCarView/viewEmailPreferences";
	String CarViewsMessagePrefs = "/do/mcCarView/viewMessagePreferences";
	String CarViewsTpaMessagePrefs = "/do/mcCarView/viewTpaMessagePreferences";
	String CarViewsNoticeMessagePrefs = "/do/mcCarView/viewNoticePreferences";
	
	
	String SectionAnchorPrefix = "Section";

	// Application Context Attribute
	String AttrMessageCenterTree = "messageCenterTree";
	String AttrMessageServiceFacade = "messageServiceFacade";
	String AttrMessageTemplateRepository = "messageTemplateRepository";
	String AttrUserMessageTemplateRepository= "userMessageTemplateRepository";
	String AttrCarViewFromGlobalView = "carViewFromGlobalView";
	
	// Request Scope attribute
	String AttrModel = "model";
	String AttrUserState = "mcUserState";
	String AttrPreferenceError = "mcPreferenceErrors";
	String AttrUserIdTpa = "userIsTpa";
	String ALERT_NOITICE_PREFERENCE="showNoticePreferenceTab";
	String ENABLE_ALERT_NOITICE_PREFERENCE="enableNoticePreferenceTab";
	
	// Parameter Name
	String ParamTabId = "tabId";
	String ParamSectionId = "sectionId";
	String ParamExpand = "expand";
	String ParamPrintFriendly = "printFriendly";
	String ParamSortOrder = "ascending";
	String ParamSortKey = "sort";
	String ParamShowAll = "showAll";
	String ParamUrlType = "urlType";
	String ParamMessageId = "messageId";
	String ParamMessageAnchor = "messageAnchor";
	String ParamUserProfileId = "userProfileId";
	String ParamContractId = "contractId";
	
	String mappingParameterGlobal = "global";
	
	String ContractIDAttrName = RecipientMessageReportData.SORT_FIELD_CONTRACT_ID;
	String ContractNameAttrName = RecipientMessageReportData.SORT_FIELD_CONTRACT_NAME;
	String PriorityAttrName = RecipientMessageReportData.SORT_FIELD_PRIORITY;
	String PostedTsAttrName = RecipientMessageReportData.SORT_FIELD_EFFECTIVE_TS;
	String ShortTextAttrName = RecipientMessageReportData.SORT_FIELD_SHORT_TEXT;

	String FieldContractId = "contractId";
	String FieldFromDate = "fromDate";
	String FieldToDate = "toDate";
	String FieldName = "name";
	String FieldMessageId = "messageId";
	String FieldSsn = "ssn";
	String FieldSubmissionId = "submissionId";
	
	int ErrorMissingUrgentMessageFrequency = 7010;

	int ErrorMissingNormalMessageFrequency = 7011;

	int ErrorMissingStartingDate = 2712;

	int ErrorInvalidStartingDate = 8021;

	int ErrorStartingDateNotInRange = 2713;

	int ErrorNameInvalid = 7085;

	int ErrorFromDateInvalidFormat = 2268;

	int ErrorToDateInvalidFormat = 2268;

	int ErrorFromToDateInvalid = 1104;

	int ErrorFromDateEmpty = 2265;
	
	int ErrorToDateEmpty = 2383;
	
	int ErrorMessageIdFormat = 2394;
	
	int ErrorToDateInFuture = 3015;
	
	int ErrorFromDateBeforeTwoYears = 2267;
	
	int ErrorContractIdFormat = 2323;
	
	int ErrorSubmissionIdFormat = 2715;
	
	int ErrorLastUserTurnOff = 2714;

	String MaxMessageCountName = "messageCenter.maxMessageCount";
	
	String DefaultMessageCountName = "messageCenter.defaultMessageCount";
	
	String UrgentMessageCountName = "messageCenter.urgentMessageCount";
	
	String HomePageUrgentMessageCountName = "messageCenter.homePageUrgentMessageCount";
	
	String MessageCenterVersion = "messageCenter.version";
	
	int PhaseIVersion = 1;
	
	int DefaultMessageCenterVersion = PhaseIVersion;
	
	int DefaultMessageCount = 20;
	
	int MaxMessageCount = 200;

	int UrgentMessageCount = 10;
	
	int HomePageUrgentMessageCount = 3;
	
	String UrgentMessageSectionName = "Urgent messages";

	String DateFormat = RenderConstants.MEDIUM_MDY_SLASHED;

	MessageCenterComponentId SummaryTabId = new MessageCenterComponentId(
			Type.TAB, 0);

	MessageCenterComponent SummaryTab = new MessageCenterComponentImpl(
			SummaryTabId, "Summary", "");

	MessageCenterComponentId UrgentMessageSectionId = new MessageCenterComponentId(
			Type.SECTION, 0);

	MessageCenterComponent UrgentMessageSection = new MessageCenterComponentImpl(
			UrgentMessageSectionId, "Urgent messages", "Urgent message section");

	RecipientMessageDetail[] ZeroMessages = new RecipientMessageDetail[0];
	
	String PostedDateFormat = "'Posted' MMM dd, yyyy 'at' hh:mm:ss a 'ET'";
	String EscalatedDateFormat = "'Escalated' MMM dd, yyyy 'at' hh:mm:ss a 'ET'";
	
	String LastModifiedFormat = RenderConstants.LONG_MDY;
	
	
	String DefaultUrgentEmailFrequency = EmailPreference.UrgentMessageFrequency.ONCE_A_DAY
			.getValue();
	
	String DefaultNormalEmailFrequency = EmailPreference.WeeklyDay.MONDAY.getValue();
	
	String DefaultNoMessageOption = "N";
	
	String DefaultEmailFormat = EmailFormat.HTML.getValue();
	
	long SystemProfileIdMax = 100;
	
	Integer HeartBeatTemplateId = 79;
}
