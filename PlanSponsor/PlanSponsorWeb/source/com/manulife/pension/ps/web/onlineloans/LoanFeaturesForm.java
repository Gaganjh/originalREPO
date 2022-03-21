package com.manulife.pension.ps.web.onlineloans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.web.contract.PlanDataUi;
import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author Ranjith
 *
 */
public class LoanFeaturesForm extends PsForm {

	private PlanDataUi planDataUi;	
	private Integer tpaFirmId;
	private Map<String, Object> lookupData = new HashMap<String, Object>();
	private boolean participantCanInitiate = false;
	private boolean tpaFirmCanInitiate = false;
	private boolean tpaFirmCanReview = false;
	private boolean tpaFirmCanSign = false;
	private String tpaFirmName;
	private Collection<UserInfo> userInfoCollection ;

	

	/**
	 * @return the lookupData
	 */
	public Map<String, Object> getLookupData() {
		return lookupData;
	}

	/**
	 * @param lookupData the lookupData to set
	 */
	public void setLookupData(Map<String, Object> lookupData) {
		this.lookupData = lookupData;
	}

	/**
	 * @return the planDataUi
	 */
	public PlanDataUi getPlanDataUi() {
		return planDataUi;
	}

	/**
	 * @param planDataUi the planDataUi to set
	 */
	public void setPlanDataUi(PlanDataUi planDataUi) {
		this.planDataUi = planDataUi;
	}

	/**
	 * @return the participantCanInitiate
	 */
	public boolean isParticipantCanInitiate() {
		return participantCanInitiate;
	}

	/**
	 * @param participantCanInitiate the participantCanInitiate to set
	 */
	public void setParticipantCanInitiate(boolean participantCanInitiate) {
		this.participantCanInitiate = participantCanInitiate;
	}

	/**
	 * @return the tpaFirmCanInitiate
	 */
	public boolean isTpaFirmCanInitiate() {
		return tpaFirmCanInitiate;
	}

	/**
	 * @param tpaFirmCanInitiate the tpaFirmCanInitiate to set
	 */
	public void setTpaFirmCanInitiate(boolean tpaFirmCanInitiate) {
		this.tpaFirmCanInitiate = tpaFirmCanInitiate;
	}

	/**
	 * @return the tpaFirmCanReview
	 */
	public boolean isTpaFirmCanReview() {
		return tpaFirmCanReview;
	}

	/**
	 * @param tpaFirmCanReview the tpaFirmCanReview to set
	 */
	public void setTpaFirmCanReview(boolean tpaFirmCanReview) {
		this.tpaFirmCanReview = tpaFirmCanReview;
	}

	/**
	 * @return the tpaFirmCanSign
	 */
	public boolean isTpaFirmCanSign() {
		return tpaFirmCanSign;
	}

	/**
	 * @param tpaFirmCanSign the tpaFirmCanSign to set
	 */
	public void setTpaFirmCanSign(boolean tpaFirmCanSign) {
		this.tpaFirmCanSign = tpaFirmCanSign;
	}

	/**
	 * @return the tpaFirmName
	 */
	public String getTpaFirmName() {
		return tpaFirmName;
	}

	/**
	 * @param tpaFirmName the tpaFirmName to set
	 */
	public void setTpaFirmName(String tpaFirmName) {
		this.tpaFirmName = tpaFirmName;
	}

	/**
	 * @return the userInfoCollection
	 */
	public Collection<UserInfo> getUserInfoCollection() {
		return userInfoCollection;
	}

	/**
	 * @param userInfoCollection the userInfoCollection to set
	 */
	public void setUserInfoCollection(Collection<UserInfo> userInfoCollection) {
		this.userInfoCollection = userInfoCollection;
	}

	/**
	 * Gets the tpaFirmId
	 * 
	 * @return tpaFirmId
	 */
	public Integer getTpaFirmId() {
		return tpaFirmId;
	}
	
	/*
	 * Sets the tpaFirmId
	 */
	public void setTpaFirmId(Integer tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

	
	
}
