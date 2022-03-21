package com.manulife.pension.ps.service.domain;

import java.util.Date;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.cache.CodeLookupCache;
import com.manulife.pension.ps.service.domain.dao.DomainDAO;

//[Refactoring] AP 20060927: Moved withdrawal cache methods to the EnvironmentService
//[TODO] AP 20060927: Shouldn't this service be deprecated and replaced with com.manulife.pension.cache.CodeLookupCache?
//[Refactoring] AP20061011: Commented out getStates/getCountries methods. Use the methods from Environment Service instead

/**
 * Bean implementation class for Enterprise Bean: DomainService
 */
public class DomainServiceBean implements javax.ejb.SessionBean {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(DomainServiceBean.class);

    private javax.ejb.SessionContext mySessionCtx;

    /**
     * getSessionContext
     */
    public javax.ejb.SessionContext getSessionContext() {
        return mySessionCtx;
    }

    /**
     * setSessionContext
     */
    public void setSessionContext(javax.ejb.SessionContext ctx) {
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
    public void ejbCreate() throws javax.ejb.CreateException {
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
     * @ejb.interface-method view-type="remote"
     */
    public Date getAsOfDate() {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAsOfDate");
        }

        Date asOfDate = null;
        try {
            asOfDate = DomainDAO.getAsOfDate();
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getAsOfDate", "Unchecked exception occurred.");
            throw ExceptionHandlerUtility.wrap(se);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getAsOfDate");

        return asOfDate;
    }

    /**
     * @see DomainService#getFeatureMap()
     */
    public Map getProductFeatures() {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getFeatureMap");
        }

        java.util.Map features = CodeLookupCache.getInstance()
                .getProductFeatures();

        if (features != null)
            return features;

        try {
            features = DomainDAO
                    .getLookupCodes(CodeLookupCache.PRODUCT_FEATURE_TYPE);
            CodeLookupCache.getInstance().setProductFeatures(features);
        } catch (SystemException e) {
            throw new EJBException(e);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getFeatureMap");

        return features;
    }

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from Environment Service instead
    /**
     * @see DomainService#getCountries()
     */
    /*
    public Map getCountries() {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getCountries");
        }

        java.util.Map countries = CodeLookupCache.getInstance().getCountries();

        if (countries != null)
            return countries;

        try {
            countries = DomainDAO
                    .getSTPLookupCodes(CodeLookupCache.COUNTRY_TYPE);
            CodeLookupCache.getInstance().setCountries(countries);
        } catch (SystemException e) {
            throw new EJBException(e);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getCountries");

        return countries;
    }
    */

    /**
     * @see DomainService#getUSAStates()
     */
    /*
    public HashMap getUSAStates() {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getUSAStates");
        }

        Map usaStates = CodeLookupCache.getInstance().getUSAStates();

        if (usaStates == null) {
            try {
                usaStates = DomainDAO
                        .getSTPLookupCodes(CodeLookupCache.USA_STATE_TYPE);
                CodeLookupCache.getInstance().setUSAStates(usaStates);
            } catch (SystemException e) {
                throw new EJBException(e);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getUSAStates");
        }

        return new HashMap(usaStates);
    }
*/

    /**
     * @ejb.interface-method view-type="remote"
     */
    public Date getEffectiveDate(int contractNumber) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getEffectiveDate");
        }

        Date effectiveDate = null;
        try {
            effectiveDate = DomainDAO.getEffectiveDate(contractNumber);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getEffectiveDate",
                    "Unchecked exception occurred.");
            throw ExceptionHandlerUtility.wrap(se);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getEffectiveDate");

        return effectiveDate;
    }


    //[Refactoring] AP 20060927: Moved the following methods in the EnvironmentService
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