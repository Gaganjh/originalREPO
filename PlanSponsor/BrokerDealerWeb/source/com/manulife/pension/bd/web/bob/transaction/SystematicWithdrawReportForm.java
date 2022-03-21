package com.manulife.pension.bd.web.bob.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.Ssn;

/**
 * A simple form to handle the from date and to date drop down.
 * 
 * @author Angel Petricia
 */
public class SystematicWithdrawReportForm extends BaseReportForm {
	   private static final long serialVersionUID = 1L;
	private String searchByType;
	private boolean isAdvanceSearch;
	
	private String wdStatus;
	
	private String wdType;
	
	private String participantName;
	private String showCustomizeFilter = "";


	private String customWDStatus;
	
	private String customWDType;
	
	private String customparticipantName;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private String quickFilterBy;
	private String quickFilterNamePhrase = null;
	
	
	private String customSsnOne;
	private String customSsnTwo;
	private String customSsnThree;
	private String asOfDate=null;
	
	private List statusList = new ArrayList();
	private List typeList = new ArrayList();
	
	public String getCustomSsnOne() {
		return customSsnOne;
	}

	public void setCustomSsnOne(String customSsnOne) {
		this.customSsnOne = customSsnOne;
	}

	public String getShowCustomizeFilter() {
		return showCustomizeFilter;
	}

	public void setShowCustomizeFilter(String showCustomizeFilter) {
		this.showCustomizeFilter = showCustomizeFilter;
	}

	public String getCustomSsnTwo() {
		return customSsnTwo;
	}

	public void setCustomSsnTwo(String customSsnTwo) {
		this.customSsnTwo = customSsnTwo;
	}



	public String getCustomSsnThree() {
		return customSsnThree;
	}



	public void setCustomSsnThree(String customSsnThree) {
		this.customSsnThree = customSsnThree;
	}

	/**
	 * Constructor. 
	 */
	public SystematicWithdrawReportForm() {
		super();
	}

	
	
	public String getQuickFilterBy() {
		return quickFilterBy;
	}

	public void setQuickFilterBy(String quickFilterBy) {
		this.quickFilterBy = quickFilterBy;
	}

	private HashMap<String, String>searchCriteria;
	
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	public Ssn getCustomSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,customSsnOne);
		ssnTemp.setDigits(1,customSsnTwo);
		ssnTemp.setDigits(2,customSsnThree);
		return ssnTemp;
	}
	
	public HashMap getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(HashMap searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public boolean isAdvanceSearch() {
		return isAdvanceSearch;
	}

	public void setAdvanceSearch(boolean isAdvanceSearch) {
		this.isAdvanceSearch = isAdvanceSearch;
	}

	public String getWdStatus() {
		return wdStatus;
	}

	public void setWdStatus(String wdStatus) {
		this.wdStatus = wdStatus;
	}

	public String getWdType() {
		return wdType;
	}

	public void setWdType(String wdType) {
		this.wdType = wdType;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public Ssn getParticipantSSN() {
		return getSsn();
	}

	/**
	 * @return Returns the searchByType.
	 */
	public String getSearchByType() {
		return searchByType;
	}


	public String getCustomWDStatus() {
		return customWDStatus;
	}

	public void setCustomWDStatus(String customWDStatus) {
		this.customWDStatus = customWDStatus;
	}

	public String getCustomWDType() {
		return customWDType;
	}

	public void setCustomWDType(String customWDType) {
		this.customWDType = customWDType;
	}

	public String getCustomparticipantName() {
		return customparticipantName;
	}

	public void setCustomparticipantName(String customparticipantName) {
		this.customparticipantName = customparticipantName;
	}

	public String getSsnOne() {
		return ssnOne;
	}

	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}

	public String getSsnTwo() {
		return ssnTwo;
	}

	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}

	public String getSsnThree() {
		return ssnThree;
	}

	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

	public void setSearchByType(String searchByType) {
		this.searchByType = searchByType;
	}
	public String getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}



	public List getStatusList() {
		return statusList;
	}

	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}

	public List getTypeList() {
		return typeList;
	}

	public void setTypeList(List typeList) {
		this.typeList = typeList;
	}

	public String getQuickFilterNamePhrase() {
		return quickFilterNamePhrase;
	}

	public void setQuickFilterNamePhrase(String quickFilterNamePhrase) {
		this.quickFilterNamePhrase = quickFilterNamePhrase;
	} 
	
}
