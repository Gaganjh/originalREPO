package com.manulife.pension.ps.service.delegate;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.LoanHoldings;
import com.manulife.pension.service.account.valueobject.ParticipantDataValueObject;
import com.manulife.pension.service.account.valueobject.BenefitBaseBatchStatus;

/**
 * AccountServiceDelegateAdaptor - is an Adaptor class to the ezk's
 * AccountServiceDelegate business delegate that uses AccountService ejb to
 * return account data value object
 * 
 * @author Leon Drotenko
 * @author Charles Chan
 */
public class AccountServiceDelegateAdaptor extends BaseServiceDelegateAdaptor {

	/**
	 * Creates a fake customer service principal based on profile ID.
	 * 
	 * @param profileId
	 * @return
	 */
	private static Logger logger = Logger
			.getLogger(AccountServiceDelegateAdaptor.class);

	/**
	 * @see AccountServiceDelegate#getLoanHoldings() This method is used to
	 *      retrieve LoanHolding VO
	 * 
	 * @param CustomerServicePrincipal
	 *            (normally passed null)
	 * @param profileId
	 *            Loanholder's profile ID
	 * @param contractNumber
	 *            Contract Number
	 * @return LoanHoldings
	 * @exception SystemException
	 */
	public LoanHoldings getLoanHoldings(CustomerServicePrincipal cs,
			String profileId, int contractNumber) throws SystemException,
			SystemUnavailableException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getLoanHoldings");

		LoanHoldings result = null;
		try {
			if (cs == null) {
				// if we don't have a principal, create a fake one.
				cs = createCustomerServicePrincipal(profileId);
			}

			checkAPOLLOAvailableForDB2Connect(cs);

			// Retrieve the data using ezk AccountService
			AccountServiceDelegate asInst = AccountServiceDelegate
					.getInstance();
			result = asInst.getLoanHoldings(cs, profileId, String
					.valueOf(contractNumber));

		} catch (RemoteException e) {
			throw new SystemException(e, getClass().getName(),
					"getLoanHoldings", "Profile ID [" + profileId
							+ "] contract number [" + contractNumber + "]");
		} catch (SystemUnavailableException e) {
			throw e;
		} catch (AccountException e) {
			throw new SystemException(e, getClass().getName(),
					"getLoanHoldings", "Profile ID [" + profileId
							+ "] contract number [" + contractNumber + "]");
		} catch (Exception e) {
			throw new SystemException(e, getClass().getName(),
					"getLoanHoldings", "Profile ID [" + profileId
							+ "] contract number [" + contractNumber + "]");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getLoanHoldings");
		return result;
	}

	/**
	 * Checks whether the mainframe is available or not.
	 * 
	 * @param cs
	 *            The CustomerServicePrincipal to use.
	 * @throws SystemException
	 *             if AccountServiceDelegate throws any exception.
	 *             SystemUnavailableException if APOLLO is not available.
	 */
	public void checkAPOLLOAvailableForDB2Connect(CustomerServicePrincipal cs)
			throws SystemException, SystemUnavailableException {
		boolean available = true;
		Boolean checkSysAvailability = Boolean.TRUE;
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkAPOLLOAvailableForDB2Connect");

		try {
			Context ctx = new InitialContext();
			checkSysAvailability = (Boolean) ctx
					.lookup("java:comp/env/useSystemAvailabilityForDB2Connect");
		} catch (NamingException e) {
			// exception ignored. We will assume we want to check system
			// availability.
		}

		if (checkSysAvailability.booleanValue()) {
			available = isAPOLLOAvailable();
		}

		/*
		 * Checks for system availablity. If it's not available, throws a system
		 * exception.
		 */
		if (!available) {
			throw new SystemUnavailableException("Apollo is not available");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- checkAPOLLOAvailableForDB2Connect");
	}

	/**
	 * @see AccountServiceDelegate#getParticipantDataValueObject() This method
	 *      is used to retrieve ParticipantData VO
	 * 
	 * @param CustomerServicePrincipal
	 *            (normally passed null)
	 * @param profileId
	 *            Loanholder's profile ID
	 * @param contractNumber
	 *            Contract Number
	 * @return ParticipantDataValueObject
	 * @exception SystemException
	 */
	public ParticipantDataValueObject getParticipantDataValueObject(
			CustomerServicePrincipal cs, String profileId, String contractNumber)
			throws SystemException {

		ParticipantDataValueObject dataReturn = null;

		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantDataValueObject");
		try {

			// Retrieve the data using ezk AccountService
			AccountServiceDelegate asInst = AccountServiceDelegate
					.getInstance();
			dataReturn = asInst.getParticipantDataValueObject(cs, profileId,
					contractNumber);
		} catch (AccountException e) {
			throw new SystemException(e, getClass().getName(),
					"getParticipantDataValueObject", "Profile ID [" + profileId
							+ "] contract number [" + contractNumber + "]");
		} catch (Exception e) {
			throw new SystemException(e, getClass().getName(),
					"getParticipantDataValueObject", "Profile ID [" + profileId
							+ "] contract number [" + contractNumber + "]");
		}
		if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantDataValueObject");
		return dataReturn;

	}

    /**
     * @see AccountServiceDelegate#getParticipantDataValueObject() This method
     *      is used to retrieve ParticipantData VO
     * 
     * @param CustomerServicePrincipal
     *            (normally passed null)
     * @param participantId
     *            participant ID
     * @param contractNumber
     *            Contract Number
     * @param boolean
     *            is search on participantId
     *            
     * Note: there is another similar method called getParticipantDataValueObject(
     *       CustomerServicePrincipal cs, String participantId) but the participanId
     *       being passed is actually the profile id
     *      
     * @return ParticipantDataValueObject
     * @exception SystemException
     */
    public ParticipantDataValueObject getParticipantDataValueObject(
            CustomerServicePrincipal cs, String participantId, String contractNumber, boolean isParticipantIdSearch)
            throws SystemException {

        ParticipantDataValueObject dataReturn = null;

        if (logger.isDebugEnabled())
            logger.debug("entry -> getParticipantDataValueObject");
        try {

            // Retrieve the data using ezk AccountService
            AccountServiceDelegate asInst = AccountServiceDelegate
                    .getInstance();
            dataReturn = asInst.getParticipantDataValueObject(cs, participantId,
                    contractNumber, isParticipantIdSearch);
        } catch (AccountException e) {
            throw new SystemException(e, getClass().getName(),
                    "getParticipantDataValueObject", "Participant ID [" + participantId
                            + "] contract number [" + contractNumber + "]");
        } catch (Exception e) {
            throw new SystemException(e, getClass().getName(),
                    "getParticipantDataValueObject", "Participant ID [" + participantId
                            + "] contract number [" + contractNumber + "]");
        }
        if (logger.isDebugEnabled())
            logger.debug("exit <- getParticipantDataValueObject");
        return dataReturn;

    }
    
	/**
	 * @see AccountServiceDelegate#getParticipantDataValueObject() This method
	 *      is used to retrieve ParticipantData VO
	 * 
	 * @param CustomerServicePrincipal
	 *            (normally passed null)
	 * @param profileId
	 *            Loanholder's profile ID
	 * @param contractNumber
	 *            Contract Number
	 * @return ParticipantDataValueObject
	 * @exception SystemException
	 */
	public ParticipantDataValueObject getParticipantDataValueObject(
			CustomerServicePrincipal cs, String participantId)
			throws SystemException {

		ParticipantDataValueObject dataReturn = null;
		if (logger.isDebugEnabled())
			logger.debug("entry -> getParticipantDataValueObject");
		try {

			// Retrieve the data using ezk AccountService
			AccountServiceDelegate asInst = AccountServiceDelegate
					.getInstance();
			dataReturn = asInst
					.getParticipantDataValueObject(cs, participantId);
		} catch (AccountException e) {
			throw new SystemException(e, getClass().getName(),
					"getParticipantDataValueObject", "Participant ID ["
							+ participantId + "]");
		} catch (Exception e) {
			throw new SystemException(e, getClass().getName(),
					"getParticipantDataValueObject", "participantId ["
							+ participantId + "]");
		}
		if (logger.isDebugEnabled())
			logger.debug("exit <- getParticipantDataValueObject");
		return dataReturn;
	}

	/**
	 * This method is used to retrieve ProfileId based on ParticipantId
	 * 
	 * @param participantId
	 * 
	 * @return profileId
	 * @exception SystemException
	 */
	public String getProfileIdByParticipantId(String participantId)
			throws SystemException {

		String profileId = null;
		ParticipantDataValueObject dataReturn = null;
		dataReturn = this.getParticipantDataValueObject(null, participantId);

		if (dataReturn != null) {
			profileId = dataReturn.getProfileId();
		}

		return profileId;
	}

    /**
     * This method is used to retrieve ProfileId based on participantId and contractNumber
     * 
     * @param participantId
     * @param contractNumber
     * 
     * @return profileId
     * @exception SystemException
     */
    public String getProfileIdByParticipantIdAndContractNumber(
            String participantId, String contractNumber) throws SystemException {

        String profileId = null;
        ParticipantDataValueObject dataReturn = null;
        dataReturn = this.getParticipantDataValueObject(null, participantId, contractNumber, true);

        if (dataReturn != null) {
            profileId = dataReturn.getProfileId();
        }

        return profileId;
    }
    
	public boolean isAPOLLOAvailable() throws SystemException {
		try {
			// check the APOLLO availability. It works with a null Principal as
			// well.
			return AccountServiceDelegate.getInstance().isAPOLLOAvailable(null);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"isAPOLLOAvailable", "Caught exception");
		}
	}
	
	/**
	 * @see AccountServiceDelegate#getBenefitBaseBatchStatusDetails() This method
	 *      is used to retrieve BenefitBaseBatchStatus VO
	 * @param String businessUnit value - PS or CS
	 * @return BenefitBaseBatchStatus
	 * @throws SystemException
	 */
	public BenefitBaseBatchStatus getBenefitBaseBatchStatusDetails(String businessUnit)
			throws SystemException {

		BenefitBaseBatchStatus dataReturn = null;

		if (logger.isDebugEnabled())
			logger.debug("entry -> getBenefitBaseBatchStatusDetails");
		try {

			// Retrieve the data using AccountService
			AccountServiceDelegate asInst = AccountServiceDelegate
					.getInstance();
			dataReturn = asInst.getBenefitBaseBatchStatusData(businessUnit);
		} catch (Exception e) {
			throw new SystemException(e, getClass().getName(),
					"getBenefitBaseBatchStatusDetails", "Benefit Base Batch Status Details");
		}
		if (logger.isDebugEnabled())
			logger.debug("exit <- getBenefitBaseBatchStatusDetails");
		return dataReturn;

	}
	
}