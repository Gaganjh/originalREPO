package com.manulife.pension.platform.web.secureDocumentUpload;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.service.contract.valueobject.SDUContractInfoVO;



public class SDUViewTabForm extends SDUReportForm {

	private static final long serialVersionUID = 1L;
	private static final String NO_VALUE_INDICATOR = "-1";
	private boolean displayViewTab=false;
	private boolean allowFileDelete=false;
	private boolean allowFileShare=false;
	private boolean justMine=false;
	private String filterExpiryDate;
	private String oAuthToken;
	private String apigeeProxyURL;
	private String downloadedByUserId;
	private String downloadedByUserName;
	private String downloadedByUserRole;
	private boolean isPendingContract = false;
	private SDUContractInfoVO sDUContractInfoVO = null;

	public String getoAuthToken() {
		return oAuthToken;
	}

	public void setoAuthToken(String oAuthToken) {
		this.oAuthToken = oAuthToken;
	}

	public String getApigeeProxyURL() {
		return apigeeProxyURL;
	}

	public void setApigeeProxyURL(String apigeeProxyURL) {
		this.apigeeProxyURL = apigeeProxyURL;
	}

	public SDUViewTabForm() {
		super();
	}
	
	public boolean isDisplayViewTab() {
		return displayViewTab;
	}

	public void setDisplayViewTab(boolean displayViewTab) {
		this.displayViewTab = displayViewTab;
	}

	public boolean isAllowFileDelete() {
		return allowFileDelete;
	}

	public void setAllowFileDelete(boolean allowFileDelete) {
		this.allowFileDelete = allowFileDelete;
	}
	
	public boolean isAllowFileShare() {
		return allowFileShare;
	}

	public void setAllowFileShare(boolean allowFileShare) {
		this.allowFileShare = allowFileShare;
	}

	public boolean isJustMine() {
		return justMine;
	}

	public void setJustMine(boolean justMine) {
		this.justMine = justMine;
	}
	
	public void reset( HttpServletRequest arg1) {
		displayViewTab = false;
		allowFileDelete=false;
		justMine=false;
	}

	public String getFilterExpiryDate() {
		return filterExpiryDate;
	}

	public void setFilterExpiryDate(String filterExpiryDate) {
		this.filterExpiryDate = filterExpiryDate;
	}


	/**
	 * Convenience method for testing if a certain field has been set.
	 *
	 * @param value - object to test
	 * @return true is the value passed in is not null and different from the no-value place holder
	 */
	public static boolean isFieldSet(Object value) {
		return value != null && !value.equals(NO_VALUE_INDICATOR);
	}

	public String getDownloadedByUserId() {
		return downloadedByUserId;
	}

	public void setDownloadedByUserId(String downloadedByUserId) {
		this.downloadedByUserId = downloadedByUserId;
	}

	public String getDownloadedByUserName() {
		return downloadedByUserName;
	}

	public void setDownloadedByUserName(String downloadedByUserName) {
		this.downloadedByUserName = downloadedByUserName;
	}

	public String getDownloadedByUserRole() {
		return downloadedByUserRole;
	}

	public void setDownloadedByUserRole(String downloadedByUserRole) {
		this.downloadedByUserRole = downloadedByUserRole;
	}
	public boolean isPendingContract() {
		return isPendingContract;
	}
	public void setPendingContract(boolean isPendingContract) {
		this.isPendingContract = isPendingContract;
	}
	public SDUContractInfoVO getSDUContractInfoVO() {
		return sDUContractInfoVO;
	}
	public void setSDUContractInfoVO(SDUContractInfoVO sDUContractInfoVO) {
		this.sDUContractInfoVO = sDUContractInfoVO;
	}
}