package com.manulife.pension.bd.web.messagecenter;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.service.message.valueobject.EmailPreference;
import com.manulife.pension.service.message.valueobject.EmailPreference.EmailFormat;
import com.manulife.pension.service.message.valueobject.EmailPreference.NormalMessageFrequency;

public interface BDMessageCenterConstants {
	String APPLICATION_ID = BDConstants.BD_APPLICATION_ID;

	int GlobalMessageTemplateId = 120;

	// The content group id for Global Messages
	int GlobalMessageContentGroupId = 125;
	
	String DefaultUrgentEmailFrequency = EmailPreference.UrgentMessageFrequency.IMMEDIATELY
			.getValue();

	String NoSummaryEmailPreference = NormalMessageFrequency.Never.getValue();
	
	String ReceiveSummaryEmailPreference = EmailPreference.WeeklyDay.MONDAY.getValue();

	String DefaultNoMessageOption = "N";

	String DefaultEmailFormat = EmailFormat.HTML.getValue();
	
	int FundCheckMessageId = 153;

}
