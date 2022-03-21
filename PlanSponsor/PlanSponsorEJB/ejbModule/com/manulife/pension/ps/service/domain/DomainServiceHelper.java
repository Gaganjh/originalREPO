package com.manulife.pension.ps.service.domain;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.BaseServiceHelper;

/**
 * This class ised in the EJB container to be accessed to the DomainService from
 * other services or ReportHandlers.
 * 
 * @author Ilker Celikyilmaz
 */
public class DomainServiceHelper extends BaseServiceHelper {

    private static DomainServiceHelper instance = new DomainServiceHelper();

    private DomainServiceHelper() {
        super();
    }

    public static DomainServiceHelper getInstance() {
        return instance;
    }

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from Environment Service instead
    /*
    public Map getCountries() throws SystemException {
        Map countries = null;
        try {

            DomainServiceHome home = (DomainServiceHome) getHome(DomainServiceHome.class);
            DomainService service = home.create();
            countries = service.getCountries();
            service.remove();
        } catch (CreateException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getCountries", "DomainService could not be created.");
        } catch (RemoveException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getCountries", "DomainService could not be removed.");
        } catch (RemoteException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getCountries", "DomainService throw RemoteException.");
        }

        return countries;
    }
    */

    public java.util.Date getEffectiveDate(int contractNumber)
            throws SystemException {
        java.util.Date effectiveDate = null;
        try {

            DomainServiceHome home = (DomainServiceHome) getHome(DomainServiceHome.class);
            DomainService service = home.create();
            effectiveDate = service.getEffectiveDate(contractNumber);
            service.remove();
        } catch (CreateException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getEffectiveDate", "DomainService could not be created.");
        } catch (RemoveException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getEffectiveDate", "DomainService could not be removed.");
        } catch (RemoteException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getEffectiveDate", "DomainService throw RemoteException.");
        }

        return effectiveDate;

    }

    public Date getAsOfDate() throws SystemException {
        Date result = null;
        try {

            DomainServiceHome home = (DomainServiceHome) getHome(DomainServiceHome.class);
            DomainService service = home.create();
            result = service.getAsOfDate();
            service.remove();
        } catch (CreateException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getAsOfDate", "DomainService could not be created.");
        } catch (RemoveException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getAsOfDate", "DomainService could not be removed.");
        } catch (RemoteException e) {
            throw new SystemException(e, this.getClass().getName(),
                    "getAsOfDate", "DomainService throw RemoteException.");
        }

        return result;
    }

}
