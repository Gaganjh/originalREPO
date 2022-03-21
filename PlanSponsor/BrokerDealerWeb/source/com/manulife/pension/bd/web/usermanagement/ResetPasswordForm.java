package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.ProducerCodeInfo;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerAssistantUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBrokerUserProfile;

public class ResetPasswordForm extends AutoForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String emailAddress;
	private String userRole;
	private List<BrokerDealerFirm> firms;
	private List<BrokerDealerFirm> riaFirms;
	private String parentUserName;
	private List<ProducerCodeInfo> producerCodes;
	private String accessCode;
	private boolean disabled = false;
	private boolean success = false;

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public void populate(ExtendedBDExtUserProfile user) {
		firstName = user.getFirstName();
		lastName = user.getLastName();
		emailAddress = user.getCommunicationEmailAddress();
		userRole = BDUserRoleDisplayNameUtil.getInstance().getDisplayName(
				user.getRoleType());
		if (BDUserRoleType.FinancialRepAssistant.compareTo(user.getRoleType()) == 0) {
			ExtendedBrokerAssistantUserProfile assistant = (ExtendedBrokerAssistantUserProfile) user;
			ExtendedBDExtUserProfile parentBroker = assistant.getParentBroker();
			parentUserName = parentBroker.getFirstName() + " "
					+ parentBroker.getLastName();
		} else {
			parentUserName = null;
		}
		producerCodes = null;
		firms = null;
		riaFirms = null;

		if (BDUserRoleType.FinancialRep.compareTo(user.getRoleType()) == 0) {
			ExtendedBrokerUserProfile broker = (ExtendedBrokerUserProfile) user;
			producerCodes = new ArrayList<ProducerCodeInfo>();
			if (broker.getActiveProducerCodes() != null) {
				producerCodes.addAll(broker.getActiveProducerCodes());
			}
			if (broker.getPendingProducerCodes() != null) {
				producerCodes.addAll(broker.getPendingProducerCodes());
			}
			Collections.sort(producerCodes, new Comparator<ProducerCodeInfo>() {
				public int compare(ProducerCodeInfo o1, ProducerCodeInfo o2) {
					long id1 = o1.getId();
					long id2 = o2.getId();
					if (id1 < id2)
						return -1;
					else if (id1 > id2)
						return 1;
					else
						return 0;

				}
			});
		}

		firms = new ArrayList<BrokerDealerFirm>();
		if (user.getBrokerDealerFirms() != null) {
			firms.addAll(user.getBrokerDealerFirms());
		}
		
		riaFirms = new ArrayList<BrokerDealerFirm>();
		if(user.getRiaFirms() != null){
			riaFirms.addAll(user.getRiaFirms());
		}

		ExtendedBrokerUserProfile brokerUser = null;
		// add pending ones for broker users
		if (user.getRoleType().compareTo(BDUserRoleType.FinancialRep) == 0) {
			brokerUser = (ExtendedBrokerUserProfile) user;
		} else if (user.getRoleType().compareTo(
				BDUserRoleType.FinancialRepAssistant) == 0) {
			brokerUser = ((ExtendedBrokerAssistantUserProfile) user)
					.getParentBroker();
		}
		if (brokerUser != null) {
			firms.addAll(brokerUser.getPendingBrokerDealerFirms());
		}
		UserManagementHelper.sortFirmsByName(firms);
		UserManagementHelper.sortFirmsByName(riaFirms);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getUserRole() {
		return userRole;
	}

	public String getParentUserName() {
		return parentUserName;
	}

	public List<BrokerDealerFirm> getFirms() {
		return firms;
	}
	
	public List<BrokerDealerFirm> getRiaFirms() {
		return riaFirms;
	}

	public List<ProducerCodeInfo> getProducerCodes() {
		return producerCodes;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
