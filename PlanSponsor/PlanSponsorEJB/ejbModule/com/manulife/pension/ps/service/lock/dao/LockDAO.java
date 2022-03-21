package com.manulife.pension.ps.service.lock.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanArrayQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * This class provides the functionalities to lock and realse the
 * component.
 * 
 * @author David Li
 */
public class LockDAO extends BaseDatabaseDAO {
    private static final Logger logger = Logger.getLogger(LockDAO.class);
    private static final String className = LockDAO.class.getName();

    private static final String SQL_SELECT_LOCK = new StringBuffer()
        .append(" SELECT COMPONENT_KEY AS componentKey")
        .append("  , COMPONENT_NAME AS componentName")
        .append("  , LOCK_CREATE_TS AS lockCreateTs")
        .append("  , LOCK_USER_PROFILE_ID AS lockUserProfileId")
        .append(" FROM ")
        .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT")
        .append(" WHERE COMPONENT_KEY = ?")
        .append("  AND COMPONENT_NAME = ?")
        .append(" FOR UPDATE")
        .toString();
    private static final String SQL_SELECT_SDU_LOCK = new StringBuffer()
            .append(" SELECT COMPONENT_KEY AS componentKey")
            .append("  , COMPONENT_NAME AS componentName")
            .append("  , LOCK_CREATE_TS AS lockCreateTs")
            .append("  , LOCK_USER_PROFILE_ID AS lockUserProfileId")
            .append(" FROM ")
            .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT")
            .append("  WHERE COMPONENT_NAME = ?")
            .append(" FOR UPDATE")
            .toString();
    private static final String SQL_INSERT_LOCK = new StringBuffer()
        .append(" INSERT INTO ")
        .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT(")
        .append(" COMPONENT_KEY, COMPONENT_NAME, LOCK_CREATE_TS, LOCK_USER_PROFILE_ID)")
        .append(" VALUES(?, ?, CURRENT TIMESTAMP, ?)")
        .toString();
    
    private static final String SQL_UPDATE_LOCK = new StringBuffer()
            .append(" UPDATE ")
            .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT")
            .append(" SET LOCK_CREATE_TS = CURRENT TIMESTAMP")
            .append(" WHERE COMPONENT_KEY = ?")
            .append("   AND COMPONENT_NAME = ?")
            .append("   AND LOCK_USER_PROFILE_ID = ?")
            .toString();

    private static final String SQL_DELETE_LOCK = new StringBuffer()
        .append(" DELETE FROM ")
        .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT")
        .append(" WHERE COMPONENT_KEY = ?")
        .append("   AND COMPONENT_NAME = ?")
        .toString();
    
    private static final String SQL_DELETE_ALL_LOCKS_FOR_USER = new StringBuffer()
    .append(" DELETE FROM ")
    .append(PLAN_SPONSOR_SCHEMA_NAME).append("COMPONENT_LOCK_EVENT")
    .append(" WHERE LOCK_USER_PROFILE_ID = ?")
    .toString();

    /**
     * Check if the component has already been locked.
     * 
     * @param componentName
     * @param componentKey
     * @return Lock
     * @throws SystemException
     */
    public static Lock getLock(String componentName, String componentKey) throws SystemException {
        Lock lock = null;
        
        SelectBeanArrayQueryHandler arrayHandler = new SelectBeanArrayQueryHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_LOCK, Lock.class);

        List<Lock> locks = null;
        try {
            locks = new ArrayList<Lock>(Arrays.asList((Lock[]) arrayHandler.select(
                    new String[] { componentKey, componentName })));

            if (locks != null && !locks.isEmpty()) {
                lock = (Lock)locks.get(0);
            }
        } catch (DAOException e) {
            throw new SystemException(e, "Problem occurred prepared call: " + SQL_SELECT_LOCK);
        }

        return lock;
    }
    /**
     * Check if the component has already been locked.
     * Return a list of locks : to check if no of locks more than threshold
     * @param componentName
     * @param componentKey
     * @return Lock
     * @throws SystemException
     */
    public static List<Lock> getLock(String componentName) throws SystemException {
    	List<Lock> locks = null;
        
        SelectBeanArrayQueryHandler arrayHandler = new SelectBeanArrayQueryHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_SDU_LOCK, Lock.class);

        
        try {
            locks = new ArrayList<Lock>(Arrays.asList((Lock[]) arrayHandler.select(
                    new String[] { componentName })));
        } catch (DAOException e) {
            throw new SystemException(e, "Problem occurred prepared call: " + SQL_SELECT_LOCK);
        }

        return locks;
    }
    
    /**
     * Add the lock record.
     * 
     * @param componentName
     * @param componentKey
     * @param profileId
     * @throws SystemException
     */
    public static void addLock(String componentName, String componentKey, long profileId) throws SystemException {
        SQLInsertHandler handler = new SQLInsertHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_INSERT_LOCK);

        try {
            logger.debug("Executing insert SQL statement: " + SQL_INSERT_LOCK);
            handler.insert(new Object[] { 
                    componentKey,
                    componentName,
                    new Long(profileId)});
        } catch (DAOException e) {
            throw new SystemException(e, "Problem occurred prepared call: " + SQL_INSERT_LOCK);
        }
    }
    
    /**
     * Update the lock record.
     * 
     * @param componentName
     * @param componentKey
     * @param profileId
     * @throws SystemException
     */
    public static void updateLock(String componentName, String componentKey, long profileId) throws SystemException {
        SQLUpdateHandler handler = new SQLUpdateHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_UPDATE_LOCK);

        try {
            logger.debug("Executing update SQL statement: " + SQL_UPDATE_LOCK);
            
            handler.update(new Object[] {
                componentKey,
                componentName,
                new Long(profileId)});
            
        } catch (DAOException e) {
            throw new SystemException(e, "Problem occurred prepared call: " + SQL_UPDATE_LOCK);
        }
    }

    /**
     * Remove the lock record.
     * 
     * @param componentName
     * @param componentKey
     * @throws SystemException
     */
    public static void deleteLock(String componentName, String componentKey) throws SystemException {
        SQLDeleteHandler handler = new SQLDeleteHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_DELETE_LOCK);
        try {
            logger.debug("Executing delete SQL statement: " + SQL_DELETE_LOCK);
            
            handler.delete(new Object[] { 
                componentKey,
                componentName});
            
        } catch (DAOException e) {
            throw new SystemException(e, 
                    "Problem occurred during prepared call: " + SQL_DELETE_LOCK);
        }
    }
    
    /**
     * Remove all lock records for the user with the given profileId.
     * 
     * @param profileId The profile id of the user whose locks should be released
     * @throws SystemException
     */
    public static void deleteAllLocksForUser(long profileId) throws SystemException {
        SQLDeleteHandler handler = new SQLDeleteHandler(
                CUSTOMER_DATA_SOURCE_NAME, SQL_DELETE_ALL_LOCKS_FOR_USER);
        try {
            logger.debug("Executing delete SQL statement: " + SQL_DELETE_ALL_LOCKS_FOR_USER);
            
            handler.delete(new Object[] { profileId });
            
        } catch (DAOException e) {
            throw new SystemException(e, 
            		"Problem occurred during prepared call: " + SQL_DELETE_ALL_LOCKS_FOR_USER);
        }
    }
}