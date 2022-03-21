package com.manulife.pension.ps.web.home;

import java.util.Set;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;

public class SearchContractForm extends ReportForm {

	private String contractNumber;
	
	private boolean showButton = false;
	private boolean runStoredProc;
	
	/**
	 * Gets the showButton
	 * @return Returns a boolean
	 */
	public boolean getShowButton() {
		return showButton;
	}
	/**
	 * Sets the showButton
	 * @param showButton The showButton to set
	 */
	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}

	
	public SearchContractForm() {
	}

	/**
	 * Gets the contractNumber
	 * 
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber == null ? "" : contractNumber.trim();
	}

	/**
	 * Sets the contractNumber
	 * 
	 * @param contractNumber
	 *            The contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * Gets the runStoredProc
	 * @return Returns a boolean
	 */
	public boolean getRunStoredProc() {
		return runStoredProc;
	}
	/**
	 * Sets the runStoredProc
	 * @param runStoredProc The runStoredProc to set
	 */
	public void setRunStoredProc(boolean runStoredProc) {
		this.runStoredProc = runStoredProc;
	}

	public boolean isRestricted(UserInfo userInfo, int contractNumber) {

		if (userInfo.getRole() instanceof InternalUser ||
			userInfo.getRole() instanceof ThirdPartyAdministrator) {
			return false;
		}
		ContractPermission permission = userInfo.getContractPermission(contractNumber);
		return permission == null || permission.getRole() instanceof PayrollAdministrator;
	}

	public boolean isMCAvailable(UserProfile userProfile) {
		Set<Integer> contracts = userProfile.getMessageCenterAccessibleContracts();
		return contracts != null && !contracts.isEmpty();
	}
	
}
