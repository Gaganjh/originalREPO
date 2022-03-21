package com.manulife.pension.ps.web.fee;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.notification.handlers.BaseEmailNotificationHandler;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.notification.handlers.vo.EmailInfoVO;
import com.manulife.pension.util.BaseEnvironment;

/**
 * 
 * Based on EcommPrimaryPagerNotificationHandler in NotificationService
 *
 */
class ConfigurableEmailNotificationHandler
extends BaseEmailNotificationHandler {
    
    private static final String NAMING_NOTIFICATION_FROM_EMAIL = "notificationFromEmail";
    
    private final String sender;
    
    private final String[] recipients;
    
    public ConfigurableEmailNotificationHandler(final String namespaceBindingKey) {
        
        super();
        
        try {
            
            BaseEnvironment environment = new BaseEnvironment();
            
            sender = environment.getNamingVariable(NAMING_NOTIFICATION_FROM_EMAIL,
                    null);
            
            recipients = StringUtils.split(environment.getNamingVariable(
                    namespaceBindingKey, null), ",");
            
        } catch (Exception e) {
            
            SystemException se =
                    new SystemException(
                            e,
                            "BaseEnvironemnt instantiation failed.");
            
            throw ExceptionHandlerUtility.wrap(se);
            
        }
        
    }
    
    public final String getSender() {
        return sender;
    }
    
    public final String[] getRecipients() {
        return recipients;
    }
    
    public String getBody(Object dataObject) {
        EmailInfoVO vo = (EmailInfoVO) dataObject;
        return "\r" + vo.getBody();
    }
    
    public String getSubject(Object dataObject) {
        EmailInfoVO vo = (EmailInfoVO) dataObject;
        return vo.getSubject();
    }
    
    public final void handleNotification(String applicationId, Object dataObject)
            throws SystemException {
        
        EmailInfoVO vo = (EmailInfoVO) dataObject;
        
        this.sendEmail(getSender(), getRecipients(), getSender(),
                getSubject(vo), getBody(vo), "text/html");
        
    }
    
}
