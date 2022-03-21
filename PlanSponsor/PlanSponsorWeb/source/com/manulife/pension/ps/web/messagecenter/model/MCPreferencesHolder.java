package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;

/**
 * A holder for both global and contract-specific MC Preferences
 * 
 * @author guweigu
 *
 */
public class MCPreferencesHolder implements MCConstants, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer currentContractId;
	private MCPreference globalPreference;
	private MCPreference contractPreference;

	private static final MCSectionPreference DefaultSectionPreference = new MCSectionPreference();
	private static final MCSectionPreference DefaultGlobalSectionPreference = new MCSectionPreference(
			true, false, PriorityAttrName, false, MCEnvironment
					.getDefaultMessageCount());

	private static final MCSectionPreference DefaultUrgentMessagePreference = new MCSectionPreference(
			true, false, PostedTsAttrName, false, MCEnvironment
					.getUrgentMessageCount());

	private static final MCSectionPreference DefaultGlobalUrgentMessagePreference = new MCSectionPreference(
			true, false, PostedTsAttrName, false, MCEnvironment
					.getUrgentMessageCount());

	public MCPreferencesHolder() {
		globalPreference = new MCPreference(
				DefaultGlobalUrgentMessagePreference,
				DefaultGlobalSectionPreference);
		contractPreference = new MCPreference(DefaultUrgentMessagePreference,
				DefaultSectionPreference);
	}

	public Integer getCurrentContractId() {
		return currentContractId;
	}

	public void setCurrentContractId(Integer contractId) {
		currentContractId = contractId;
	}

	public MCPreference getGlobalPreference() {
		return globalPreference;
	}

	/**
	 * Gets the contract preference for a contract
	 * If the current contract is not the same as the input contractId,
	 * then reset the preferences
	 * @param contractId
	 * @return
	 */
	public MCPreference getContractPreference(Integer contractId) {
		if (!contractId.equals(currentContractId)) {
			contractPreference.reset();
			currentContractId = contractId;
		}
		return contractPreference;
	}
	
	/**
	 * Clear the selection of tab/section which determines where does
	 * the message center link will lead to.
	 */
	public void clearSelection() {
		globalPreference.clearSelection();
		contractPreference.clearSelection();
	}
}
