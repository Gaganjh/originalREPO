package com.manulife.pension.ps.service.withdrawal.valueobject;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.withdrawal.util.LoanAndWithdrawalItemComparator;
import com.manulife.pension.ps.service.withdrawal.util.SearchParticipantWithdrawalRequestComparator;
import com.manulife.pension.ps.service.withdrawal.util.SearchParticipantWithdrawalRequestNameComparator;
import com.manulife.pension.ps.service.withdrawal.util.SearchParticipantWithdrawalRequestSsnComparator;

/**
 * This class represents one row of data to be dispayed on the Search Participant report.
 * 
 * @author KUTHIHA
 * 
 */

public class SearchParticipantWithdrawalRequestItem implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private static final String DESCENDING_INDICATOR = "DESC";
    
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String ssn;
    private int profileId;
    private Integer contractNumber;
    private String contractName;
    private Date birthDate;
    private Date hireDate;
    
    private static Map comparators = new HashMap();
    static {
        comparators.put(SearchParticipantWithdrawalReportData.SORT_PARTICIPANT_NAME,
                        new SearchParticipantWithdrawalRequestNameComparator());
        comparators.put(SearchParticipantWithdrawalReportData.SORT_SSN,
                new SearchParticipantWithdrawalRequestSsnComparator());
      
    }
    
    public SearchParticipantWithdrawalRequestItem () {
        
    }
 
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFirstName() {
        return firstName;
    }

	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
	
    public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	

    public String getLastName() {
        return lastName;
    }
    
    public String getParticipantFullName() {
        return StringUtils.trim(lastName) + ", " + 
               StringUtils.trim(firstName) + " " +
        		((StringUtils.equals(middleInitial, null) 
        		  || StringUtils.equals(middleInitial, StringUtils.EMPTY)) 
        		 ? "" : middleInitial.trim());
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
        public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
    
    /**
     * Returns a compartor for the given sort field and direction
     * @param sortField the sort field
     * @param sortDirection the sort direction ASC or DESC
     * @return the Compartor
     */
    public static Comparator getComparator(String sortField, String sortDirection) {
        SearchParticipantWithdrawalRequestComparator comparator =
            comparators.containsKey(sortField) ? (SearchParticipantWithdrawalRequestComparator) comparators.get(sortField) :
                                                 (SearchParticipantWithdrawalRequestComparator) comparators.get(SearchParticipantWithdrawalReportData.SORT_PARTICIPANT_NAME);
        comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
        return comparator;
    }

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
   

}
