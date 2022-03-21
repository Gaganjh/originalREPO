package com.manulife.pension.ps.service.notice.periodicProcess;



import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.event.Event;
import com.manulife.pension.event.NoticeManagerAlertNotificationEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.notice.dao.PlanNoticeDocumentChangeDao;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Periodic process used to handle the Notice Manager alert Periodic Process
 * 
 * @author Tamilarasu
 */
public class NoticeManagerAlertNotifyPeriodicProcessor {

	private static NoticeManagerAlertNotifyPeriodicProcessor instance;
	private static FastDateFormat DISTRIBUTION_DATE = FastDateFormat.getInstance("MMM dd,yyyy");
	
	/**
	 * Constructor 
	 */
	private NoticeManagerAlertNotifyPeriodicProcessor()  {
		
	}

	/**
	 * Returns the instance
	 * @return
	 * @throws SystemException
	 */
	public static NoticeManagerAlertNotifyPeriodicProcessor getInstance() {
		if ( instance == null )
			instance = new NoticeManagerAlertNotifyPeriodicProcessor();	
		return instance;
	}
	
	/**
	 * 
	 * @throws SystemException
	 */
	public void execute() throws SystemException { 
		List<UserNoticeManagerAlertVO> userNoticeAlertPreference = PlanNoticeDocumentChangeDao.getNoticeManagerAlertNotificationPreferences();
		
		for(UserNoticeManagerAlertVO userNoticeManagerAlertVO:userNoticeAlertPreference){
			String termsOfUseInd = PlanNoticeDocumentChangeDao.getTermsAndAcceptanceInd(userNoticeManagerAlertVO.getProfileId());
			NoticeManagerAlertNotificationEvent event = new NoticeManagerAlertNotificationEvent();
			event.setInitiator(Event.SYSTEM_USER_PROFILE_ID);
			event.setContractId(userNoticeManagerAlertVO.getContractId());
			event.setAlertName(userNoticeManagerAlertVO.getAlertName());
			event.setAlertTiming(userNoticeManagerAlertVO.getAlertTimingCode());
			if(userNoticeManagerAlertVO.getStartDate()!=null){
				event.setDistrubutionDate(DISTRIBUTION_DATE.format(userNoticeManagerAlertVO.getStartDate()));
			}
			event.setProfileId(userNoticeManagerAlertVO.getProfileId().longValue());
			int priority =  5;
			if("U".equalsIgnoreCase(userNoticeManagerAlertVO.getAlertUrgencyName().trim())){
				priority = 0;
			}
			event.setTermsOfUseInd(termsOfUseInd);
			event.setPriority(priority);
			// trigger the event
			EventClientUtility.getInstance(
					new BaseEnvironment().getAppId()).prepareAndSendJMSMessage(event); 
		}
	}


}
