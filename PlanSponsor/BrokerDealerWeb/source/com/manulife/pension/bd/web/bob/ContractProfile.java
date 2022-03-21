package com.manulife.pension.bd.web.bob;

import java.io.Serializable;
import java.util.List;

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