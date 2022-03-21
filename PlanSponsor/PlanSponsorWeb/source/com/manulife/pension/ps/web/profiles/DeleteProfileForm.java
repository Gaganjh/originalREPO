package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



public class DeleteProfileForm extends UserProfileForm {
	private static final long serialVersionUID = 5832114225234138252L;

	private String employeeNumber;
	
	private String tpaFirmId;
	private List<TpaFirm> tpaFirms = new ArrayList<TpaFirm>();

	private String ifileLastContractList;
	private String lastTpaFirmsRegisteredUserList;
	private boolean isLastPSEUM = false;
	private String lastPseumContractList;
	// Added to show new warnings for TPA rewrite project
    private String lastIloansEmailFirmsList;
    private String lastIloansEmailAndTpaStaffPlanFirmsList;
	private boolean isLastTUM = false;
	private String lastTUMFirmtList;
	private String participantRole;

	private boolean internalUser = false;

	private String brokerDealerSiteRole;
	
	private String rvpDisplayName;
	
	private String rmDisplayName;
	
	private String licenceVerified;
	
	private String accessIPIHypotheticalTool;

	private String access408DisclosureRegen;

	private String planSponsorSiteRole = AccessLevelHelper.NO_ACCESS;
	
	/**
	 * @return Returns the employeeNumber
	 */
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * @param employeeNumber
	 *            The employeeNumber to set.
	 */
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/**
	 * @return Returns the tpaFirmToAdd.
	 */
	public String getTpaFirmId() {
		return tpaFirmId;
	}

	/**
	 * @param tpaFirmToAdd
	 *            The tpaFirmToAdd to set.
	 */
	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = trimString(tpaFirmId);
	}

	public TpaFirm getTpaFirm(int index) {
		while (index > tpaFirms.size() - 1) {
			tpaFirms.add(new TpaFirm());
		}
		return (TpaFirm) tpaFirms.get(index);
	}

	public List getTpaFirms() {
		return tpaFirms;
	}

	/**
	 * @param ifileLastContractList
	 *            The ifileLastContractList to set.
	 */
	public void setIfileLastContractList(String ifileLastContractList) {
		this.ifileLastContractList = ifileLastContractList;
	}

	public String getIfileLastContractList() {
		return this.ifileLastContractList;
	}

	/**
	 * @param lastTpaFirmsRegisteredUserList
	 *            The lastTpaFirmsRegisteredUserList to set.
	 */
	public void setLastTpaFirmsRegisteredUserList(String lastTpaFirmsRegisteredUserList) {
		this.lastTpaFirmsRegisteredUserList = lastTpaFirmsRegisteredUserList;
	}

	public String getLastTpaFirmsRegisteredUserList() {
		return this.lastTpaFirmsRegisteredUserList;
	}

	/**
	 * @return Returns the boolean
	 */
	public boolean isLastPSEUM() {
		return this.isLastPSEUM;
	}

	/**
	 * @param boolean
	 *            The isLastPSEUM to set.
	 */
	public void setIsLastPSEUM(boolean isLast) {
		this.isLastPSEUM = isLast;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
		super.clear( request);
		this.employeeNumber = null;	
		this.tpaFirmId = null;
		this.tpaFirms.clear();
		this.isLastPSEUM = false;
		this.ifileLastContractList = null;
		this.lastTpaFirmsRegisteredUserList = null;
	}



	/**
	 * Gets the lastPseumContractList
	 * @return Returns a String
	 */
	public String getLastPseumContractList() {
		return lastPseumContractList;
	}
	/**
	 * Sets the lastPseumContractList
	 * @param lastPseumContractList The lastPseumContractList to set
	 */
	public void setLastPseumContractList(String lastPseumContractList) {
		this.lastPseumContractList = lastPseumContractList;
	}


	/**
	 * @return Returns the lastIloansEmailAndTpaStaffPlanFirmsList.
	 */
	public String getLastIloansEmailAndTpaStaffPlanFirmsList() {
		return lastIloansEmailAndTpaStaffPlanFirmsList;
	}
	/**
	 * @param lastIloansEmailAndTpaStaffPlanFirmsList The lastIloansEmailAndTpaStaffPlanFirmsList to set.
	 */
	public void setLastIloansEmailAndTpaStaffPlanFirmsList(
			String lastIloansEmailAndTpaStaffPlanFirmsList) {
		this.lastIloansEmailAndTpaStaffPlanFirmsList = lastIloansEmailAndTpaStaffPlanFirmsList;
	}

	/**
	 * @param lastTUMFirmtList The lastTUMFirmtList to set.
	 */
	public void setLastTUMFirmtList(String lastTUMFirmtList) {
		this.lastTUMFirmtList = lastTUMFirmtList;
	}

	/**
	 * @return Returns the lastTUMFirmtList.
	 */
	public String getLastTUMFirmtList() {
		return lastTUMFirmtList;
	}

	/**
	 * @param isLastTUM The isLastTUM to set.
	 */
	public void setLastTUM(boolean isLastTUM) {
		this.isLastTUM = isLastTUM;
	}

	/**
	 * @return Returns the isLastTUM.
	 */
	public boolean isLastTUM() {
		return isLastTUM;
	}

	/**
	 * @return the internalUser
	 */
	public boolean isInternalUser() {
		return internalUser;
	}

	/**
	 * @param internalUser the internalUser to set
	 */
	public void setInternalUser(boolean internalUser) {
		this.internalUser = internalUser;
	}

    /**
     * @return the lastIloansEmailFirmsList
     */
    public String getLastIloansEmailFirmsList() {
        return lastIloansEmailFirmsList;
    }

    /**
     * @param lastIloansEmailFirmsList the lastIloansEmailFirmsList to set
     */
    public void setLastIloansEmailFirmsList(String lastIloansEmailFirmsList) {
        this.lastIloansEmailFirmsList = lastIloansEmailFirmsList;
    }

	/**
	 * @return the participantRole
	 */
	public String getParticipantRole() {
		return participantRole;
	}

	/**
	 * @param participantRole the participantRole to set
	 */
	public void setParticipantRole(String participantRole) {
		this.participantRole = participantRole;
		if (this.participantRole != null) {
			this.participantRole = this.participantRole.trim();
		}
	}
	public String getBrokerDealerSiteRole() {
		return brokerDealerSiteRole;
	}

	public void setBrokerDealerSiteRole(String brokerDealerSiteRole) {
		this.brokerDealerSiteRole = brokerDealerSiteRole;
	}

	public String getRvpDisplayName() {
		return rvpDisplayName;
	}

	public void setRvpDisplayName(String rvpDisplayName) {
		this.rvpDisplayName = rvpDisplayName;
	}
	
	public String getRmDisplayName() {
		return rmDisplayName;
	}

	public void setRmDisplayName(String rmDisplayName) {
		this.rmDisplayName = rmDisplayName;
	}

	public String getBdLicenceVerified() {
		return licenceVerified;
	}

	public void setBdLicenceVerified(String licenceVerified) {
		this.licenceVerified = licenceVerified;
	}

	public String getAccessIPIHypotheticalTool() {
		return accessIPIHypotheticalTool;
	}

	public void setAccessIPIHypotheticalTool(String accessIPIHypotheticalTool) {
		this.accessIPIHypotheticalTool = accessIPIHypotheticalTool;
	}

	public String getAccess408DisclosureRegen() {
		return access408DisclosureRegen;
	}

	public void setAccess408DisclosureRegen(String access408DisclosureRegen) {
		this.access408DisclosureRegen = access408DisclosureRegen;
	}

	public String getPlanSponsorSiteRole() {
		return planSponsorSiteRole;
	}

	public void setPlanSponsorSiteRole(String planSponsorSiteRole) {
		this.planSponsorSiteRole = planSponsorSiteRole;
	}

}

