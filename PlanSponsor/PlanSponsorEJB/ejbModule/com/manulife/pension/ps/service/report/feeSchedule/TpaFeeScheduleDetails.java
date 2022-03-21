package com.manulife.pension.ps.service.report.feeSchedule;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData.FilterSections;
import com.manulife.pension.service.contract.valueobject.ContractValidationDetail;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.ArrayUtility;
import com.manulife.pension.util.Pair;

public class TpaFeeScheduleDetails {
	
	
	private TpaFeeScheduleDetails(){
		
	}
	
	@SuppressWarnings("unchecked")
	public static Pair<String, Timestamp> getLastUpdateTpaCustomScheduleDetails(int contractId, int tpaFirmId) throws SystemException {
		Pair<String, Timestamp> value = new Pair<String, Timestamp>(null, null);;
		try {
			ContractFeeScheduleChangeHistoryReportData reportData = (ContractFeeScheduleChangeHistoryReportData) 
					ReportServiceDelegate.getInstance().getReportData(getReportCriteria(contractId, tpaFirmId));
			for(FeeScheduleChangeItem item : (List<FeeScheduleChangeItem>) reportData.getDetails()) {
				value =  new Pair<String, Timestamp>(item.getUserName(), item.getChangedDate());
				break;
			}
		} catch (ReportServiceException e) {
			throw new SystemException(e, "Exception thown in setLastUpdateDetails()");
		}
		
		return value;
		
	}
	
	private static ReportCriteria getReportCriteria(int contractId, int tpaFirmId) throws SystemException {
		
        ReportCriteria criteria = new ReportCriteria(ContractFeeScheduleChangeHistoryReportData.REPORT_ID);
		
		criteria.setPageNumber(1);
		
		criteria.setPageSize(1);
		
		criteria.insertSort(ContractFeeScheduleChangeHistoryReportData.SORT_CHANGE_DATE, "DESC");
		
		// set sections to filter with
		List<FilterSections> sections = new ArrayList<FilterSections>();	
		sections.add(FilterSections.FeeSection);
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_SECTION, sections);
		
		
		int contractNumber = Integer.valueOf(contractId);
		
		// set contract id to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_CONTRACT_ID, contractNumber);
		
		FeeScheduleHistoryDetails historyDetails = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
				.getFeeScheduleChangeHistoryDetails(contractNumber, tpaFirmId, FeeScheduleType.CustomizedFeeSchedule);
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_HISTORY, historyDetails.getFirmDetails());
		
		// set to date to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TO_DATE, new Date());

		// set from date to filter with
		Calendar fromDate  = Calendar.getInstance();
		fromDate.set(Calendar.YEAR, (fromDate.get(Calendar.YEAR) - 2) );
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE, fromDate.getTime());
		
		HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planHistory = 
				ContractServiceDelegate.getInstance().getContractValidationDetailHistory(
						contractNumber, ArrayUtility.toUnsortedSet(ContractValidationDetail.values()));
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_PLAN_PROVISION_HISTORY,
				planHistory);
		
		
		// set user names
	    Map<String, String> allUsers = new HashMap<String, String>();
		for (Entry<Integer, String> user : historyDetails.getUpdatedUserDetails().entrySet()) {
			if (Integer.parseInt(Constants.SYSTEM_USER_PROFILE_ID) == user.getKey()) {
				allUsers.put(String.valueOf(user.getKey()), Constants.ADMINISTRATION);
			} else {
				UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserProfileByProfileId(new Long(user.getKey()));
				if (userInfo.getRole().isInternalUser()) {
					allUsers.put(String.valueOf(user.getKey()), Constants.JOHN_HANCOCK_REPRESENTATIVE);
				} else {
					 allUsers.put(String.valueOf(user.getKey()), user.getValue());
				}
			}
		}
		// set users to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME, allUsers);
		
		return criteria;
	}

}
