package com.manulife.pension.ps.service.delegate;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.PartyServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.party.valueobject.GeneralInformationValueObject;

/**
 * Adaptor class for ezk's Party Services
 * 
 * @author drotele
 * Created on Apr 30, 2004
 */
public class PartyServiceDelegateAdaptor extends BaseServiceDelegateAdaptor {

	private static Logger logger =
		Logger.getLogger(PartyServiceDelegateAdaptor.class);

	/**
	 * constructor
	 */
	public PartyServiceDelegateAdaptor() {
		super();
	}

	/**
	 * Retrieves a general information object (Partycipant data)
	 *
	 * @param principal user identifier
	 * @param profileId employee identifier
	 * @return GeneralInformationValueObject value object
	 */
	public GeneralInformationValueObject getGeneralInformation(
		CustomerServicePrincipal principal,
		String profileId,
		String contractNumber)
		throws SystemException {

		GeneralInformationValueObject dataReturn = null;

		if (logger.isDebugEnabled())
			logger.debug("entry -> getGeneralInformation");
		try {

			// Retrieve the data using ezk AccountService
			PartyServiceDelegate psInst =
				PartyServiceDelegate.getInstance();
			if (principal == null) {
				principal = this.createCustomerServicePrincipal(profileId);
			}
			dataReturn =
				psInst.getGeneralInformation(
					principal,
					profileId,
					contractNumber);
		} catch (Exception e) {
			throw new SystemException(
				e,
				getClass().getName(),
				"getGeneralInformation",
				"Profile ID ["
					+ profileId
					+ "]"
					+ "Conteract Number ["
					+ contractNumber
					+ "]");
		}
		if (logger.isDebugEnabled())
			logger.debug("exit <- getGeneralInformation");
		return dataReturn;
	}
}
