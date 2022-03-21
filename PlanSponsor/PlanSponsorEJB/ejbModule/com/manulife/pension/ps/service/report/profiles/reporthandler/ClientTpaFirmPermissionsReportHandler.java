package com.manulife.pension.ps.service.report.profiles.reporthandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.profiles.valueobject.ClientTpaFirmPermissionsReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.dao.SearchDAO;
import com.manulife.pension.service.security.tpa.dao.TPADAO;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;


/**
 * @author marcest
 *
 */
public class ClientTpaFirmPermissionsReportHandler implements ReportHandler {
	
	public static final String REPORT_ID = ClientTpaFirmPermissionsReportHandler.class.getName(); 
	public static final String REPORT_NAME = "ClientAuthorizationofTPAFirmPermissions";
    
	/* (non-Javadoc)
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException, ReportServiceException {
		ClientTpaFirmPermissionsReportData data = new ClientTpaFirmPermissionsReportData();
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		Map <Integer, TPAFirmInfo> firmInfosByFirmId = new HashMap<Integer, TPAFirmInfo>();
		List<TPAFirmInfo> allFirmInfos = new ArrayList<TPAFirmInfo>();

		List<Integer> contractIds = (List<Integer>) reportCriteria
				.getFilterValue(ClientTpaFirmPermissionsReportData.FILTER_CONTRACT_IDS);

		for (Integer contractId : contractIds) {
			
			TPAFirmInfo tpaFirmInfo = TPADAO.getFirmInfoByContractId(contractId);
			if (tpaFirmInfo != null)
			{
				allFirmInfos.add(tpaFirmInfo);
				Integer tpaFirmId = Integer.valueOf(Integer.valueOf(tpaFirmInfo.getId()));
				
				if (!firmInfosByFirmId.containsKey(tpaFirmId))
				{
					firmInfosByFirmId.put(tpaFirmId, tpaFirmInfo);
				}
			}
		}
		
		if (firmInfosByFirmId.size() > 0)
		{
			userInfos = SearchDAO.searchTpaUserFirmPermissions(firmInfosByFirmId.values());
		}

		data.setReportCriteria(reportCriteria);
		data.setUserInfos(userInfos);
		data.setFirmInfos(allFirmInfos);

		return data;
	}
    
}
