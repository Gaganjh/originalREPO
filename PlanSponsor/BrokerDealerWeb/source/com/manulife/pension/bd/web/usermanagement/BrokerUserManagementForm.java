package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.UserManagementAccessRules;
import com.manulife.pension.service.security.bd.UserManagementAccessRules.UsermanagementOperation;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;

public class BrokerUserManagementForm extends AbstractUserManagementForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String FIELD_FIRMS = "firms";
	
	private String firmListStr;
	
	private String firmPermissionsListStr;

    private List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(0);

    private String selectedFirmId;

    private String selectedFirmName;
    private boolean updateBrokerSuccess = false;

	private ExtendedBrokerUserProfile brokerUserProfile;

	private long selectedPartyId;
	private long selectedAssistantId;

	@Override
	protected ExtendedBDExtUserProfile getExtUserProfile() {
		return brokerUserProfile;
	}

	/**
	 * Return the display string for profile status
	 * 
	 * @return
	 */
	public String getProfileStatus() {
		if (brokerUserProfile != null) {
			return UserManagementHelper.getSelfRegisteredProfileStatusMap()
					.get(brokerUserProfile.getProfileStatus());
		} else {
			return null;
		}
	}

	public ExtendedBrokerUserProfile getBrokerUserProfile() {
		return brokerUserProfile;
	}

	public void setBrokerUserProfile(ExtendedBrokerUserProfile brokerUserProfile) {
		this.brokerUserProfile = brokerUserProfile;
	}

	public long getSelectedPartyId() {
		return selectedPartyId;
	}

	public void setSelectedPartyId(long selectedPartyId) {
		this.selectedPartyId = selectedPartyId;
	}

	public long getSelectedAssistantId() {
		return selectedAssistantId;
	}

	public void setSelectedAssistantId(long selectedAssistantId) {
		this.selectedAssistantId = selectedAssistantId;
	}

	/**
	 * Check if remove broker entity is allowed. It is not allowed when only one
	 * broker entity association exists;
	 * 
	 * @return
	 */
	public boolean isRemoveBrokerEntityAllowed() {
		return UserManagementAccessRules.getInstance().isOperationAllowed(
				internalUserRole, BDUserRoleType.FinancialRep,
				UsermanagementOperation.Edit)
				&& ((brokerUserProfile.getActiveBrokerEntities().size() + brokerUserProfile
						.getPendingBrokerEntities().size()) > 1);
	}

	public String getSelectedFirmId() {
		return selectedFirmId;
	}

	public void setSelectedFirmId(String selectedFirmId) {
		this.selectedFirmId = selectedFirmId;
	}

	public String getSelectedFirmName() {
		return selectedFirmName;
	}

	public void setSelectedFirmName(String selectedFirmName) {
		this.selectedFirmName = selectedFirmName;
	}

	public List<BrokerDealerFirm> getFirms() {
		return firms;
	}

	public void setFirms(List<BrokerDealerFirm> firms) {
		this.firms = firms;
	}

	public String getFirmListStr() {
		return firmListStr;
	}

	public void setFirmListStr(String firmListStr) {
		this.firmListStr = firmListStr;
	}

	public String getFirmPermissionsListStr() {
		return firmPermissionsListStr;
	}

	public void setFirmPermissionsListStr(String firmPermissionsListStr) {
		this.firmPermissionsListStr = firmPermissionsListStr;
	}

	public boolean isUpdateBrokerSuccess() {
		return updateBrokerSuccess;
	}

	public void setUpdateBrokerSuccess(boolean updateBrokerSuccess) {
		this.updateBrokerSuccess = updateBrokerSuccess;
	}
	
}
