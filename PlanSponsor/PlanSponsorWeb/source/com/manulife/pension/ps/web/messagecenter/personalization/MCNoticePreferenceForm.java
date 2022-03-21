package com.manulife.pension.ps.web.messagecenter.personalization;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.util.CloneableForm;


/**
 * Creating a alert for the user notices
 * @author Tamilarasu K
 *
 */
public class MCNoticePreferenceForm extends AutoForm implements
CloneableForm, MCConstants {


	private static final long serialVersionUID = 1L;
	private Integer contractId ;
	private BigDecimal profileId ;
	private String alertType = "";
	private String action="default";
	private List<UserNoticeManagerAlertVO> userNoticeManagerAlertList = null;
	private TreeMap<String,String> alertFrequency = new TreeMap<String,String>();
	private List<LookupDescription> alertFrequencyCodes = new ArrayList<LookupDescription>();
	private int id = 0;
	private int tempId = 0;
	private boolean alertMaxSize = false;
	private boolean alertsPageEnable = false;
	

	/**
	 * @return the tempId
	 */
	public int getTempId() {
		return tempId;
	}

	/**
	 * @param tempId the tempId to set
	 */
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	/**
	 * @return the alertMaxSize
	 */
	public boolean isAlertMaxSize() {
		return alertMaxSize;
	}
	/**
	 * @param alertMaxSize the alertMaxSize to set
	 */
	public void setAlertMaxSize(boolean alertMaxSize) {
		this.alertMaxSize = alertMaxSize;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the alertFrequency
	 */
	public TreeMap<String, String> getAlertFrequency() {
		return alertFrequency;
	}

	/**
	 * @param alertFrequency the alertFrequency to set
	 */
	public void setAlertFrequency(TreeMap<String, String> alertFrequency) {
		this.alertFrequency = alertFrequency;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * constructor	
	 */
	public MCNoticePreferenceForm() {
		userNoticeManagerAlertList = new ArrayList<UserNoticeManagerAlertVO>();
	}


	/**
	 * Get each   with the  Number 
	 * @param Number
	 * @return 
	 * @throws ParseException 
	 */
	public UserNoticeManagerAlertVO getUserNoticeManagerAlert(int index)  {
		UserNoticeManagerAlertVO userNoticeManagerAlert = userNoticeManagerAlertList.get(index);
		return userNoticeManagerAlert;
	}

	/**
	 * Set the   information
	 * @param Number
	 * @param benefeciaries
	 */
	public void setUserNoticeManagerAlert(int index,
			UserNoticeManagerAlertVO userNoticeManagerAlert) {
		userNoticeManagerAlertList.set(index, userNoticeManagerAlert);
	}



	/**
	 * Get the list of   records.
	 * Add a   to the list, if it list is empty.
	 * @return List of 
	 */
	public List<UserNoticeManagerAlertVO> getUserNoticeManagerAlertList() {
		return userNoticeManagerAlertList;
		}
	


	/**
	 * Set the list of   record
	 * @param 
	 */
	public void setUserNoticeManagerAlertList(
			List<UserNoticeManagerAlertVO> userNoticeManagerAlert) {
		this.userNoticeManagerAlertList = userNoticeManagerAlert;
	}

	/**
	 * Add an element to the  . This method will create a
	 * new  object, add it to the 
	 *  list and will return it.
	 * 
	 * @return the newly added element.
	 */
	public UserNoticeManagerAlertVO addUserNoticeManagerAlert() {
		UserNoticeManagerAlertVO userNoticeManagerAlert =
			new UserNoticeManagerAlertVO();
		userNoticeManagerAlert.setContractId(this.contractId);
		userNoticeManagerAlert.setProfileId(this.profileId);
		userNoticeManagerAlert.setAlertUrgencyName("N");
		userNoticeManagerAlert.setStringStartDate("");
		userNoticeManagerAlert.setAlertId(0);

		userNoticeManagerAlertList.add(userNoticeManagerAlert);
		return userNoticeManagerAlert;
	}

	public List<UserNoticeManagerAlertVO> deleteUserNoticeManagerAlert() {
		userNoticeManagerAlertList.remove(getId());
		return userNoticeManagerAlertList;
	}

	/**
	 * @return the alertType
	 */
	public String getAlertType() {
		return alertType;
	}

	/**
	 * @param alertType the alertType to set
	 */
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	private MCNoticePreferenceForm clonedForm;


	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public void storeClonedForm() {
		clonedForm = new MCNoticePreferenceForm();
	}

	public Object clone() {
		storeClonedForm();
		return clonedForm;
	}

	//Reset the alert row with default values
	public void flush(HttpServletRequest arg1) {


		userNoticeManagerAlertList.get(getId()).setAlertName("");
		userNoticeManagerAlertList.get(getId()).setStringStartDate("");
		userNoticeManagerAlertList.get(getId()).setAlertFrequenceCode("Select");
		userNoticeManagerAlertList.get(getId()).setAlertTimingCode("");
		userNoticeManagerAlertList.get(getId()).setAlertUrgencyName("N");
		alertType = null;

	}


	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}


	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}


	/**
	 * @return the profileId
	 */
	public BigDecimal getProfileId() {
		return profileId;
	}


	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}


	/**
	 * @return the alertFrequencyCodes
	 */
	public List<LookupDescription> getAlertFrequencyCodes() {
		return alertFrequencyCodes;
	}


	/**
	 * @param alertFrequencyCodes the alertFrequencyCodes to set
	 */
	public void setAlertFrequencyCodes(List<LookupDescription> alertFrequencyCodes) {
		this.alertFrequencyCodes = alertFrequencyCodes;
	}

	@Override
	public void clear( HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	public boolean isAlertsPageEnable() {
		return alertsPageEnable;
	}

	public void setAlertsPageEnable(boolean alertsPageEnable) {
		this.alertsPageEnable = alertsPageEnable;
	}


}
