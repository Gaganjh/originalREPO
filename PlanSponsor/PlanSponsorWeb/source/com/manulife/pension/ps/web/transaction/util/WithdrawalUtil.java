package com.manulife.pension.ps.web.transaction.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.SystematicWithdrawalReportUtility.WDStatusCodes;
import com.manulife.pension.service.contract.util.SystematicWithdrawalReportUtility.WDTypeCodes;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
/**
 * WithdrawalUtil  class - This class is used to get the information
 * for the Systematioc Withdrawls of the specified contracts in PSW Transactions page.
 * 
 * @author Pavan Kumar 
 */
public class WithdrawalUtil {

	private static final Logger log = Logger.getLogger(WithdrawalUtil.class);

	public static final String WDStatus = "WDStatus";
	public static final String WDType = "WDType";
	public static final String WithdrawalStatusWithoutC = "withdrawalStatusWithoutC";

	private List<LabelValueBean> wdStatusList = new ArrayList<LabelValueBean>();
	
	private List<LabelValueBean> wdTypeList = new ArrayList<LabelValueBean>();
	
	private List<LabelValueBean> withdrawalStatusListWithoutC = new ArrayList<LabelValueBean>();

	private static WithdrawalUtil instance = new WithdrawalUtil();

	private Map<String, List<LabelValueBean>> lookupMap = new HashMap<String, List<LabelValueBean>>();
	/**
	 * Constructor for WithdrawalUtil.
	 */
	public WithdrawalUtil() {
		loadWithdrawalStatuses();
		loadWithdrawalTypes();
		
		lookupMap.put(WDStatus, getWdStatusList());
		lookupMap.put(WDType, getWdTypeList());
		lookupMap.put(WithdrawalStatusWithoutC,
				getWithdrawalStatusListWithoutC());
	}

	public static WithdrawalUtil getInstance() {
		return instance;
	}

	

	private void loadWithdrawalStatuses() {

		Map<String, String> wdStatus = new HashMap<String, String>();
		for (WDStatusCodes wdStatusCodes : WDStatusCodes.values()) {
			wdStatus.put(wdStatusCodes.name(), wdStatusCodes.getCode());
					
		}
		TreeSet<String> sortedSet = new TreeSet<String>(wdStatus.keySet());
		Iterator<String> iter = sortedSet.iterator();
		while (iter.hasNext()) {
			String key =  iter.next();
			String value =  wdStatus.get(key);
			wdStatusList.add(new LabelValueBean(value, key));
		}
		wdStatusList = Collections.unmodifiableList(wdStatusList);

		List<LabelValueBean> statuses = wdStatusList;
		for (LabelValueBean bean : statuses) {
			if (!"C".equals(bean.getValue())) {
				withdrawalStatusListWithoutC.add(bean);
			}
		}
		wdStatusList = Collections.unmodifiableList(wdStatusList);
	}
	
	private void loadWithdrawalTypes() {

		Map<String, String> wdTypes = new HashMap<String, String>();
		for (WDTypeCodes wdStatusCodes : WDTypeCodes.values()) {
			wdTypes.put(wdStatusCodes.name(), wdStatusCodes.getCode());
					
		}
		TreeSet<String> sortedSet = new TreeSet<String>(wdTypes.keySet());
		Iterator<String> iter = sortedSet.iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			String value =  wdTypes.get(key);
			wdTypeList.add(new LabelValueBean(value, key));
		}
		wdTypeList = Collections.unmodifiableList(wdTypeList);

		List<LabelValueBean> statuses = wdTypeList;
		for (LabelValueBean bean : statuses) {
			if (!"C".equals(bean.getValue())) {
				withdrawalStatusListWithoutC.add(bean);
			}
		}
		wdTypeList = Collections.unmodifiableList(wdTypeList);
	}

	public List<LabelValueBean> getWdStatusList() {
		return wdStatusList;
	}

	public void setWdStatusList(List<LabelValueBean> wdStatusList) {
		this.wdStatusList = wdStatusList;
	}

	public static String getWdstatus() {
		return WDStatus;
	}

	public List<LabelValueBean> getWithdrawalStatusListWithoutC() {
		return withdrawalStatusListWithoutC;
	}

	public void setWithdrawalStatusListWithoutC(
			List<LabelValueBean> withdrawalStatusListWithoutC) {
		this.withdrawalStatusListWithoutC = withdrawalStatusListWithoutC;
	}

	public List<LabelValueBean> getWdTypeList() {
		return wdTypeList;
	}

	public void setWdTypeList(List<LabelValueBean> wdTypeList) {
		this.wdTypeList = wdTypeList;
	}

	public static String getWdtype() {
		return WDType;
	}

}
