package com.manulife.pension.service.distribution.valueobject;

import java.io.Serializable;

import com.manulife.pension.service.withdrawal.valueobject.Address;

/**
 * Class used to perform address change activity performed by the participant.
 * 
 * @author Vasanth Balaji
 *
 */
public class AtRiskAddressChangeVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private Integer approvalUpdatedProfileId = null;
	private String approvalUpdatedUserIdType = null;
	private Address approvalAddress = null;
	private String createdUserFistName = null;
	private String createdUserLastName = null;
	
	
	
	/**
	 * @return the createdUserFistName
	 */
	public final String getCreatedUserFistName() {
		return createdUserFistName;
	}
	/**
	 * @param createdUserFistName the createdUserFistName to set
	 */
	public final void setCreatedUserFistName(String createdUserFistName) {
		this.createdUserFistName = createdUserFistName;
	}
	/**
	 * @return the createdUserLastName
	 */
	public final String getCreatedUserLastName() {
		return createdUserLastName;
	}
	/**
	 * @param createdUserLastName the createdUserLastName to set
	 */
	public final void setCreatedUserLastName(String createdUserLastName) {
		this.createdUserLastName = createdUserLastName;
	}
	
	public Address getApprovalAddress() {
		return approvalAddress;
	}
	public void setApprovalAddress(Address approvalAddress) {
		this.approvalAddress = approvalAddress;
	}
	public Integer getApprovalUpdatedProfileId() {
		return approvalUpdatedProfileId;
	}
	public void setApprovalUpdatedProfileId(Integer approvalUpdatedProfileId) {
		this.approvalUpdatedProfileId = approvalUpdatedProfileId;
	}
	public String getApprovalUpdatedUserIdType() {
		return approvalUpdatedUserIdType;
	}
	public void setApprovalUpdatedUserIdType(String approvalUpdatedUserIdType) {
		this.approvalUpdatedUserIdType = approvalUpdatedUserIdType;
	}
	
	
	
	
	

}
