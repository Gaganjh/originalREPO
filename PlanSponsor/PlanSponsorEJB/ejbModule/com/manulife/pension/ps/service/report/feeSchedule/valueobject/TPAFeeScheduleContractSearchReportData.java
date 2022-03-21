package com.manulife.pension.ps.service.report.feeSchedule.valueobject;

import com.manulife.pension.ps.service.report.feeSchedule.reporthandler.TPAFeeScheduleContractSearchReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;


public class TPAFeeScheduleContractSearchReportData extends ReportData{
	
	public static final String REPORT_ID = TPAFeeScheduleContractSearchReportHandler.class.getName(); 
	public static final String REPORT_NAME = "selectContractReport"; 
	public static final String FILTER_CLIENT_ID = "clientId";
	public static final String FILTER_SITE_LOCATION = "siteLocation";
	public static final String FILTER_DI_DURATION = "DIDuration";
	public static final String FILTER_USER_ROLE = "role";
	public static final String FILTER_SEARCH_STRING = "searchString";
	public static final String FILTER_TPA_FIRM_ID = "tpaFirmId";
	public static final String FILTER_MASTER_TPA_FIRM_IDS = "masterTPAFirmIds";
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_CONTRACT_NAME = "contractName";
	public static final String FILTER_COMPANY_CODE = "companyCode";
	
	public static final String SORT_CONTRACT_NAME = "contractName";
	public static final String SORT_CONTRACT_NUMBER = "contractId";
	public static final String SORT_FEE_SCHEDULE = "feeSchedule";
	public static final String SORT_CREATED_USER ="createdUser";
	public static final String SORT_CREATED_TS = "createdTS";
	
	private boolean contractExistsOnTheOtherFirm = false;
	private int otherFirmIdWhereContractExist;
	private int totalCountOfContracts;
	private boolean invalidContractSearch = false;
	
	public TPAFeeScheduleContractSearchReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	

	public boolean isContractExistsOnTheOtherFirm() {
		return contractExistsOnTheOtherFirm;
	}


	public void setContractExistsOnTheOtherFirm(boolean contractExistsOnTheOtherFirm) {
		this.contractExistsOnTheOtherFirm = contractExistsOnTheOtherFirm;
	}


	public int getTotalCountOfContracts() {
		return totalCountOfContracts;
	}

	public void setTotalCountOfContracts(int totalCountOfContracts) {
		this.totalCountOfContracts = totalCountOfContracts;
	}

	public boolean isInvalidContractSearch() {
		return invalidContractSearch;
	}

	public void setInvalidContractSearch(boolean invalidContractSearch) {
		this.invalidContractSearch = invalidContractSearch;
	}


	public int getOtherFirmIdWhereContractExist() {
		return otherFirmIdWhereContractExist;
	}

	public void setOtherFirmIdWhereContractExist(int otherFirmIdWhereContractExist) {
		this.otherFirmIdWhereContractExist = otherFirmIdWhereContractExist;
	}
	
	public boolean isContractCountBelowMaxLimit(){
		if(getTotalCount() < 50){
			return true;
		}
		return false;
	}
}
