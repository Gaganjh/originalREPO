package com.manulife.pension.ps.service.report.profiles.valueobject;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author marcest
 *
 */
public class ClientTpaFirmPermissionsReportData extends ReportData {
	
	public static final String FILTER_CONTRACT_IDS = "FILTER_CONTRACT_IDS";
	List<TPAFirmInfo> firmInfos = new ArrayList<TPAFirmInfo>();
	List<UserInfo> userInfos = new ArrayList<UserInfo>();
	public List<TPAFirmInfo> getFirmInfos() {
		return firmInfos;
	}
	public void setFirmInfos(List<TPAFirmInfo> firmInfos) {
		this.firmInfos = firmInfos;
	}
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}
}
