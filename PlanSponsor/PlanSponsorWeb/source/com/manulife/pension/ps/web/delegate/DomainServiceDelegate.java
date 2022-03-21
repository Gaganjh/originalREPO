package com.manulife.pension.ps.web.delegate;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.domain.DomainService;
import com.manulife.pension.ps.service.domain.DomainServiceHome;
import com.manulife.pension.service.delegate.AbstractServiceDelegate;

/**
 * This is the Business Delegate for Domain Service
 * 
 * @author Ilker Celikyilmaz
 */
public class DomainServiceDelegate extends AbstractServiceDelegate {
    private static DomainServiceDelegate instance = new DomainServiceDelegate();
    private String applicationId;
    
    /* (non-Javadoc)
     * @see com.manulife.pension.service.delegate.AbstractServiceDelegate#getApplicationId()
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @throws ServiceDelegateException
     */
    protected DomainServiceDelegate() {
    }

    /**
     * @return
     */
    public static DomainServiceDelegate getInstance() {
        return instance;
    }

    /**
     * @see AbstractServiceDelegate#getHomeClassName()
     */
    protected String getHomeClassName() {
        return DomainServiceHome.class.getName();
    }

    protected EJBObject create() throws SystemException, RemoteException,
            CreateException {
        return ((DomainServiceHome) getHome()).create();
    }

    /**
     * @return ContractDatesVO which is composite of As of Date and month end
     *         dates
     * @throws SystemException
     */
    public Date getAsOfDate() throws SystemException {
        Date result = null;
        DomainService service = (DomainService) getService();
        try {
            result = service.getAsOfDate();
        } catch (RemoteException e) {
            handleRemoteException(e, "getAsOfDate");
        }
        return result;
    }

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from Environment Service instead
    /**
     * @see com.manulife.pension.ps.service.domain.DomainService#getCountries()
     */
    /*
    public java.util.Map getCountries() throws SystemException {
        java.util.Map result = null;
        DomainService service = (DomainService) getService();
        try {
            result = service.getCountries();
        } catch (RemoteException e) {
            handleRemoteException(e, "getCountries");
        }
        return result;
    }
    */

    /**
     * @see com.manulife.pension.ps.service.domain.DomainService#getProductFeatures()
     */
    public java.util.Map getProductFeatures() throws SystemException {
        java.util.Map result = null;
        DomainService service = (DomainService) getService();
        try {
            result = service.getProductFeatures();
        } catch (RemoteException e) {
            handleRemoteException(e, "getProductFeatures");
        }
        return result;
    }

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from Environment Service instead
    /**
     * @see com.manulife.pension.ps.service.domain.DomainService#getUSAStates()
     */
    /*
    public java.util.Map getUSAStates() throws SystemException {
        java.util.Map result = null;
        DomainService service = (DomainService) getService();
        try {
            result = service.getUSAStates();
        } catch (RemoteException e) {
            handleRemoteException(e, "getUSAStates");
        }
        return result;
    }
    */

    /**
     * Retrieves the contract effective Date
     * 
     * @return Date
     * @throws SystemException
     */
    public Date getEffectiveDate(int contractNumber) throws SystemException {
        Date result = null;
        DomainService service = (DomainService) getService();
        try {
            result = service.getEffectiveDate(contractNumber);
        } catch (RemoteException e) {
            handleRemoteException(e, "getEffectiveDate");
        }
        return result;

    }

    //[Refactoring] AP 20060928: Moving the following methods in the EnvironmentService
    //  - getPaymentToTypes()
    //  - getLoanOptionTypes()
    //  - getWithdrawalAmountTypes()
    //  - getTPATransactionFeeTypes()
    //  - getOptionsForUnvestedAmounts()
    //  - getHardshipReasons()
    //  - getLookupData(final Collection lookupDataKeys)
}
