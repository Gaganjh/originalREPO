package com.manulife.pension.ps.service.domain;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;


/**
 * Remote interface for Enterprise Bean: DomainService
 */
public interface DomainService extends javax.ejb.EJBObject {

    /**
     * Retrieves the Last business Date
     * 
     * @return Date
     * @throws java.rmi.RemoteException
     */
    public Date getAsOfDate() throws RemoteException;

    /**
     * Retrieves a hash map of Fecture (Contract Feacture) codes and
     * descriptions from the lookup table in the database
     * 
     * @return java.util.Map
     * @throws java.rmi.RemoteException
     */
    public Map getProductFeatures() throws RemoteException;

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from Environment Service instead
    /**
     * Retrieves a hash map of country codes and descriptions from the lookup
     * table in the database
     * 
     * @return java.util.Map
     * @throws java.rmi.RemoteException
     */
    //public Map getCountries() throws RemoteException;

    /**
     * Retrieves a hash map of USA state codes and descriptions from the lookup
     * table in the database
     * 
     * @return HashMap
     * @throws java.rmi.RemoteException
     */
    //public HashMap getUSAStates() throws RemoteException;

    /**
     * Retrieves the contract effective Date
     * 
     * @return Date
     * @throws java.rmi.RemoteException
     */
    public Date getEffectiveDate(int contractNumber) throws RemoteException;


    //[Refactoring] AP 20060927: Moving the following withdrawal methods in the EnvironmentService
    //  - getPaymentToTypes()
    //  - getLoanOptionTypes()
    //  - getWithdrawalAmountTypes()
    //  - getTPATransactionFeeTypes()
    //  - getOptionsForUnvestedAmounts()
    //  - getHardshipReasons()
    //  - getWithdrawalRequestReasons()
    //  - getWithdrawalRequestStatus()
    //  - getLookupData()
}
