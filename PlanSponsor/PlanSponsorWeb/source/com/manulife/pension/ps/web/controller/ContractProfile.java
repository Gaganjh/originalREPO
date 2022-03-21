package com.manulife.pension.ps.web.controller;

import java.io.Serializable;
import java.util.List;

//import com.manulife.pension.ps.web.notification.NotificationList;
//import com.manulife.pension.ps.web.notification.RecentNotificationList;
import com.manulife.pension.ps.web.tools.IFileConfig;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.StaticHelperClass;

/**
 * This class stores the current contract and all the information related
 * to the contract. An instance of this class is stored in UserProfile which
 * in turn is stored in the session. When user switches contract, an instance
 * of this object is recreated.
 * 
 * @author Charles Chan
 */
public class ContractProfile implements Serializable {

	private Contract contract;

	private IFileConfig ifileConfig;
	//MC2 - notifications obsolete
//	private RecentNotificationList recentNotificationList;

//	private NotificationList notificationList;
	
	private List quickReportList;
	
	private Boolean showManageTpaFirmLink;

    private Boolean showSubmissionHistoryJustMineFilter;
    
    private String statusChangesForPastPED = "";
    
	/**
	 * Constructor.
	 */
	public ContractProfile(Contract contract) {
		super();
		this.contract = contract;
	}

	/**
	 * @return Returns the contract.
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            The contract to set.
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return Returns the ifileConfig.
	 */
	public IFileConfig getIfileConfig() {
		return ifileConfig;
	}

	/**
	 * @param ifileConfig
	 *            The ifileConfig to set.
	 */
	public void setIfileConfig(IFileConfig ifileConfig) {
		this.ifileConfig = ifileConfig;
	}
	
	/**
	 * @return Returns the notificationList.
	 */
/*	public NotificationList getNotificationList() {
		return notificationList;
	}
*/
	/**
	 * @param notificationList The notificationList to set.
	 */
/*	public void setNotificationList(NotificationList notificationList) {
		this.notificationList = notificationList;
	}
*/
	/**
	 * @return Returns the recentNotificationList.
	 */
/*	public RecentNotificationList getRecentNotificationList() {
		return recentNotificationList;
	}
*/
	/**
	 * @param recentNotificationList The recentNotificationList to set.
	 */
/*	public void setRecentNotificationList(
			RecentNotificationList recentNotificationList) {
		this.recentNotificationList = recentNotificationList;
	}
*/	
	/**
	 * @return Returns the quickReportList.
	 */
	public List getQuickReportList() {
		return quickReportList;
	}

	/**
	 * @param quickReportList The quickReportList to set.
	 */
	public void setQuickReportList(List quickReportList) {
		this.quickReportList = quickReportList;
	}
	
	public String toString()
	{
		return StaticHelperClass.toString(this);
	}
	
    /**
     * @return Returns the showManageTpaFirmLink.
     */
    public Boolean getShowManageTpaFirmLink() {
        return showManageTpaFirmLink;
    }

    /**
     * @param showManageTpaFirmLink The showManageTpaFirmLink to set.
     */
    public void setShowManageTpaFirmLink(Boolean showManageTpaFirmLink) {
        this.showManageTpaFirmLink = showManageTpaFirmLink;
    }
    
    public Boolean getShowSubmissionHistoryJustMineFilter() {
        return showSubmissionHistoryJustMineFilter;
    }

    public void setShowSubmissionHistoryJustMineFilter(
            Boolean showSubmissionHistoryJustMineFilter) {
        this.showSubmissionHistoryJustMineFilter = showSubmissionHistoryJustMineFilter;
    }

	public String getStatusChangesForPastPED() {
		return statusChangesForPastPED;
	}

	public void setStatusChangesForPastPED(String statusChangesForPastPED) {
		this.statusChangesForPastPED = statusChangesForPastPED;
	}
    
    
}