package com.manulife.pension.platform.web.delegate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.report.tpabob.TpaSearchService;
import com.manulife.pension.ps.service.report.tpabob.TpaSearchServiceHome;

/**
 * This delegate will be used to carry out get information for a TPA.
 * 
 * @author harlomte
 * 
 */
public class TpaSearchServiceDelegate extends PsAbstractServiceDelegate {

    private static TpaSearchServiceDelegate instance = new TpaSearchServiceDelegate();

    /**
     * Constructor
     */
    private TpaSearchServiceDelegate() {
    }

    /**
     * @return TpaServiceDelegate
     */
    public static TpaSearchServiceDelegate getInstance() {
        return instance;
    }

    @Override
    protected EJBObject create() throws SystemException, RemoteException, CreateException {
        return ((TpaSearchServiceHome) getHome()).create();
    }

    @Override
    protected String getHomeClassName() {
        return TpaSearchServiceHome.class.getName();
    }

    /**
     * This method retrieves a list of Car Names of contracts that the user is allowed to access.
     * 
     * @param userProfileId
     * @return
     * @throws SystemException
     */
    public List<String> getCarForUserProfile(Long userProfileId) throws SystemException {
        TpaSearchService service = (TpaSearchService) getService();
        
        List<String> carnameList = new ArrayList<String>();

        try {
            carnameList = service.getCarForUserProfile(userProfileId);
        } catch (RemoteException e) {
            logAndCreateSystemException(e);
        }

        return carnameList;
    }
}
