package com.manulife.pension.ps.web.transaction;


import com.manulife.pension.platform.web.report.BaseReportForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.manulife.pension.platform.web.util.Ssn;


/**
 * SystematicWithdrawReportForm 
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
	private String asOfDate=null;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;

	private List statusList = new ArrayList();
	private List typeList = new ArrayList();
	
	/**
	 * Constructor. 
	 */
	public SystematicWithdrawReportForm() {
		super();
	}

	
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
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
	
   
}
