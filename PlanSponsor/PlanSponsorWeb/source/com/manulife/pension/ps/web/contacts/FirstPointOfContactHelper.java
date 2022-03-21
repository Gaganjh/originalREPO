package com.manulife.pension.ps.web.contacts;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.valueobject.FirstClientContactData;

/**
 * Helper class to handle the FCC data
 * @author ayyalsa
 *
 */
public class FirstPointOfContactHelper {
	private static final Logger logger = Logger.getLogger(FirstPointOfContactHelper.class);
	
	/**
	 * retrieves the Contact -FCC data
	 * 
	 * @param contractNumber
	 * @return
	 * @throws SystemException
	 */
	public static FirstPointOfContact getFirstPointOfContact(int contractNumber) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry ---> getFirstPointOfContact() for contract " + contractNumber);
		}
		
		FirstPointOfContact firstPointOfContact = new FirstPointOfContact();
		FirstClientContactData firstClientContact = null;
		
		// get the FCC data
		firstClientContact = 
			ContractServiceDelegate.getInstance().getFirstClientContactData(
					contractNumber);

		// create the FirstClientContact object
		if (firstClientContact != null) {
			
			firstPointOfContact.setContractId(
					firstClientContact.getContractId());
			
			firstPointOfContact.setFirstClientContact(
					firstClientContact.getFirstClientContact());

			firstPointOfContact.setFirstClientContactValue(
					Constants.FirstClientContactFeatureValue
					.getFirstClientContractFeatureValue(
							firstClientContact.getFirstClientContact()));

			firstPointOfContact.setFirstClientContactOther(
					firstClientContact.getFirstClientContactOther());

			firstPointOfContact.setFirstClientContactOtherValue(
					Constants.FirstClientContactOtherAttributeValue
					.getFirstClientContactOtherAttributeValue(
							firstClientContact.getFirstClientContactOther()));

			firstPointOfContact.setFirstClientContactOtherType(
					firstClientContact.getFirstClientContactOtherType());

			firstPointOfContact.setFirstClientContactOtherTypeValue(
					Constants.FirstClientContactOtherTypeAttributeValue
					.getFirstClientContactOtherTypeAttributeValue(
							firstClientContact.getFirstClientContactOtherType()));
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("EXIT ---> getFirstPointOfContact() for contract " + contractNumber);
		}

		return firstPointOfContact;
	}
	
	/**
	 * Updates the FCC in the Contact table
	 * 
	 * @param firstPointOfContact
	 * @param userProfile
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public static void saveFirstPointOfContact(
			FirstPointOfContact firstPointOfContact, 
			UserProfile userProfile) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Entry ---> saveFirstPointOfContact() for contract " 
					+ userProfile.getCurrentContract().getContractNumber());
		}
		
		int contractId = userProfile.getCurrentContract().getContractNumber();
		
		FirstClientContactData firstClientContactData = null;
		if (firstPointOfContact != null) {
			firstClientContactData  = new FirstClientContactData();
			firstClientContactData.setContactLevelTypeCode("CO");
			firstClientContactData.setContractId(contractId);
			firstClientContactData.setFirstClientContact(StringUtils.trimToNull(
					firstPointOfContact.getFirstClientContact()));
			firstClientContactData.setFirstClientContactOther(StringUtils.trimToNull(
					firstPointOfContact.getFirstClientContactOther()));
			firstClientContactData.setFirstClientContactOtherType(StringUtils.trimToNull(
					firstPointOfContact.getFirstClientContactOtherType()));
			firstClientContactData.setUserProfileId(userProfile.getPrincipal().getProfileId());
			
			ContractServiceDelegate.getInstance().createOrUpdateFirstClientContact(
					firstClientContactData);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exit ---> saveFirstPointOfContact() for contract " 
					+ userProfile.getCurrentContract().getContractNumber());
		}
	}
}
