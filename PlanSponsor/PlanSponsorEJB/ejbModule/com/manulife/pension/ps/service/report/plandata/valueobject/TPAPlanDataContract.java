package com.manulife.pension.ps.service.report.plandata.valueobject;

import java.io.Serializable;
import java.util.Date;

public class TPAPlanDataContract implements Serializable {

	private static final long serialVersionUID = 1L;

	private String contractName;
	private int contractId;
	private int tpaFirmId;
	private Date createdTS;
	private String createdUser;
	private String feeSchedule;
	private String serviceSelected;
	
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public int getTpaFirmId() {
		return tpaFirmId;
	}

	public void setTpaFirmId(int tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

	public Date getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getFeeSchedule() {
		return feeSchedule;
	}

	public void setFeeSchedule(String feeSchedule) {
		this.feeSchedule = feeSchedule;
	}

	public String getServiceSelected() {
		return serviceSelected;
	}

	public void setServiceSelected(String serviceSelected) {
		this.serviceSelected = serviceSelected;
	}
	
}
