package com.manulife.pension.ps.service.report.tpabob;

import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpabob.dao.SearchCarNamesDAO;

/**
 * This session bean will be used to carry out get information for a TPA.
 * 
 * @author harlomte
 * 
 */
public class TpaSearchServiceBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    private SessionContext mySessionCtx;

    /**
     * getSessionContext
     */
    public SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(SessionContext ctx) {
        mySessionCtx = ctx;
    }

    /**
     * ejbActivate
     */
    public void ejbActivate() {
    }
    
    /**
     * ejbCreate
     */
    public void ejbCreate() throws CreateException {
    }

    /**
     * ejbPassivate
     */
    public void ejbPassivate() {
    }
    
    /**
     * ejbRemove
     */
    public void ejbRemove() {
    }

    /**
     * This method retrieves a list of Car Names of contracts that the user is allowed to access.
     * 
     * @param userProfileId - UserProfile ID.
     * @return - A List of Car Names.
     * @throws SystemException
     */
    public ArrayList<String> getCarForUserProfile(Long userProfileId) throws SystemException {
        return SearchCarNamesDAO.getCarListForUserProfile(userProfileId);
    }

}
