package com.manulife.pension.ps.web.withdrawal;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;

public class SearchParticipantRequestWithdrawalForm extends ReportForm {

    public static final int SSN_ONE_LENGTH = 3;

    public static final int SSN_TWO_LENGTH = 2;

    public static final int SSN_THREE_LENGTH = 4;

    public static final int CONTRACT_NUMBER_LENGTH = 5;

    public static final int CONTRACT_NUMBER_LENGTH_MAXIMUM = 7;
    
    private static final long serialVersionUID = 1L;

    private String filterParticipantLastName;

    private String filterContractId;
    
    private Integer selectedContractId;
    
    private String selectedContractName;

    private String ssnOne;

    private String ssnTwo;

    private String ssnThree;
    
    private String csfType ;
    
    private boolean showOldILoansLink;

    public SearchParticipantRequestWithdrawalForm() {
        super();
    }

    public String getFilterParticipantLastName() {
        return filterParticipantLastName;
    }

    public void setFilterParticipantLastName(String filterParticipantLastName) {
        this.filterParticipantLastName = filterParticipantLastName;
    }

    public String getFilterContractId() {
        return filterContractId;
    }

    public void setFilterContractId(String filterContractId) {
        this.filterContractId = filterContractId;
    }

    public Ssn getSsn() {
        Ssn ssnTemp = new Ssn();
        ssnTemp.setDigits(0, ssnOne);
        ssnTemp.setDigits(1, ssnTwo);
        ssnTemp.setDigits(2, ssnThree);
        return ssnTemp;
    }

    public String getSsnOne() {
        return ssnOne;
    }

    public void setSsnOne(String ssnOne) {
        this.ssnOne = ssnOne;
    }

    public String getSsnThree() {
        return ssnThree;
    }

    public void setSsnThree(String ssnThree) {
        this.ssnThree = ssnThree;
    }

    public String getSsnTwo() {
        return ssnTwo;
    }

    public void setSsnTwo(String ssnTwo) {
        this.ssnTwo = ssnTwo;
    }

    /**
     * Populates the fields of a form from a similar one. Care should be taken because the values
     * coming from the page will be overwritten
     */
    public void copyFrom(SearchParticipantRequestWithdrawalForm other) {
        this.filterParticipantLastName = other.filterParticipantLastName;
        this.filterContractId = other.filterContractId;
        this.ssnOne = other.ssnOne;
        this.ssnTwo = other.ssnTwo;
        this.ssnThree = other.ssnThree;
    }

    /**
     * Queries if a message about no search data should be displayed based on the existence of the
     * search parameter data.
     * 
     * @return boolean - True if the search parameters are all blank.
     */
    public boolean getShowNoSearchDataMessage() {
        return (StringUtils.isBlank(filterContractId)
                && StringUtils.isBlank(filterParticipantLastName) && StringUtils.isBlank(ssnOne)
                && StringUtils.isBlank(ssnTwo) && StringUtils.isBlank(ssnThree));
    }

    /**
     * @return the selectedContractId
     */
    public Integer getSelectedContractId() {
        return selectedContractId;
    }

    /**
     * @param selectedContractId the selectedContractId to set
     */
    public void setSelectedContractId(final Integer selectedContractId) {
        this.selectedContractId = selectedContractId;
    }

    /**
     * @return the selectedContractName
     */
    public String getSelectedContractName() {
        return selectedContractName;
    }

    /**
     * @param selectedContractName the selectedContractName to set
     */
    public void setSelectedContractName(final String selectedContractName) {
        this.selectedContractName = selectedContractName;
    }

	/**
	 * @return the csfType
	 */
	public String getCsfType() {
		return csfType;
	}

	/**
	 * @param csfType the csfType to set
	 */
	public void setCsfType(String csfType) {
		this.csfType = csfType;
	}

	/**
	 * @return the showOldILoansLink
	 */
	public boolean isShowOldILoansLink() {
		return showOldILoansLink;
	}

	/**
	 * @param showOldILoansLink the showOldILoansLink to set
	 */
	public void setShowOldILoansLink(boolean showOldILoansLink) {
		this.showOldILoansLink = showOldILoansLink;
	}
}
