package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;

/**
 * Form for broker assistant user
 * 
 * @author guweigu
 * 
 */
public class BrokerAssistantUserManagementForm extends
		AbstractUserManagementForm {

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

	private ExtendedBrokerAssistantUserProfile assistantProfile;

	public ExtendedBrokerAssistantUserProfile getAssistantProfile() {
		return assistantProfile;
	}

	/**
	 * Set the assistant profile, only when initial is true, copy the assistant
	 * profile's first,last name and email address to form for updating
	 * 
	 * @param assistantProfile
	 * @param initial
	 */
	public void setAssistantProfile(
			ExtendedBrokerAssistantUserProfile assistantProfile) {
		this.assistantProfile = assistantProfile;
	}

	/**
	 * Return the display string for profile status
	 * 
	 * @return
	 */
	public String getProfileStatus() {
		if (assistantProfile != null) {
			return UserManagementHelper.getCreatedProfileStatusMap().get(
					assistantProfile.getProfileStatus());
		} else {
			return "";
		}
	}

	@Override
	protected ExtendedBDExtUserProfile getExtUserProfile() {
		return assistantProfile;
	}

	/**
	 * Utility methods to get the sorted BD firms from parent broker
	 * 
	 * @return
	 */
	public SortedSet<String> getBrokerDealerFirmNames() {
		List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>();
		if (assistantProfile != null) {
			ExtendedBrokerUserProfile broker = assistantProfile
					.getParentBroker();
			List<BrokerEntityAssoc> entities = broker.getActiveBrokerEntities();
			if (entities != null) {
				for (BrokerEntityAssoc entity : entities) {
					firms.addAll(entity.getBrokerEntity().getBrokerDealerFirms());
				}
			}
			entities = broker.getPendingBrokerEntities();
			if (entities != null) {
				for (BrokerEntityAssoc entity : entities) {
					firms.addAll(entity.getBrokerEntity().getBrokerDealerFirms());
				}
			}
		}

		return UserManagementHelper.getSortedBDFirmNames(firms);
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

	public List<BrokerDealerFirm> getFirms() {
		return firms;
	}

	public void setFirms(List<BrokerDealerFirm> firms) {
		this.firms = firms;
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
}
