package com.manulife.pension.ps.web.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.util.log.ServiceLogRecord;

public enum FunctionalLogger {
    
    INSTANCE;
    
    private final Logger logger = Logger.getLogger(FunctionalLogger.class);
    
    //TODO identify contract
    public void log(final String functionName, final UserProfile user, final String transactionData, final Class<?> loggingPointClass, final String loggingPointFunction) {
        
        final ServiceLogRecord record = new ServiceLogRecord();
        record.setApplicationId("PS");
        record.setUserIdentity(user.getProfileId() + ":" + user.getName());
        record.setData(transactionData);
        record.setMethodName(loggingPointClass.getName() + ":" + loggingPointFunction);
        record.setServiceName(functionName);
        record.setDate(new Date());
        
        logger.error(record);
        
    }
    
    public void log(final String functionName, final HttpServletRequest request, final String transactionData, final Class<?> loggingPointClass, final String loggingPointFunction) {
        
        log(functionName, SessionHelper.getUserProfile(request), transactionData, loggingPointClass, loggingPointFunction);
        
    }
        
    public void log(final String functionName, final UserProfile user, final Class<?> loggingPointClass, final String loggingPointFunction) {
        
        log(functionName, user, Integer.toString(user.getCurrentContract().getContractNumber()), loggingPointClass, loggingPointFunction);
        
    }
    
    public void log(final String functionName, final HttpServletRequest request, final Class<?> loggingPointClass, final String loggingPointFunction) {
        
        log(functionName, SessionHelper.getUserProfile(request), loggingPointClass, loggingPointFunction);
        
    }
        
}
