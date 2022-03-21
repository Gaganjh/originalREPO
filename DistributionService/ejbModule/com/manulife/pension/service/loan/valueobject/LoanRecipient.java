package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.StateTaxVO;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;

public class LoanRecipient extends BaseSerializableCloneableObject implements Recipient {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int recipientNo;
    private String firstName;
    private String lastName;
    private String organizationName;
    private Boolean usCitizenInd;
    private String stateOfResidenceCode;
    private String shareTypeCode;
    private BigDecimal shareValue;
    private BigDecimal federalTaxPercent;
    private BigDecimal stateTaxPercent;
    private String stateTaxTypeCode;
    private String taxpayerIdentTypeCode;
    private String taxpayerIdentNo;
    private DistributionAddress address = new Address();
    private Collection<Payee> payees = new ArrayList<Payee>(0);
    private StateTaxVO stateTaxVo;
    private Integer submissionId;
	
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;
	public int getRecipientNo() {
		return recipientNo;
	}
	public void setRecipientNo(int recipientNo) {
		this.recipientNo = recipientNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public Boolean getUsCitizenInd() {
		return usCitizenInd;
	}
	public void setUsCitizenInd(Boolean usCitizenInd) {
		this.usCitizenInd = usCitizenInd;
	}
	public String getStateOfResidenceCode() {
		return stateOfResidenceCode;
	}
	public void setStateOfResidenceCode(String stateOfResidenceCode) {
		this.stateOfResidenceCode = stateOfResidenceCode;
	}
	public String getShareTypeCode() {
		return shareTypeCode;
	}
	public void setShareTypeCode(String shareTypeCode) {
		this.shareTypeCode = shareTypeCode;
	}
	public BigDecimal getShareValue() {
		return shareValue;
	}
	public void setShareValue(BigDecimal shareValue) {
		this.shareValue = shareValue;
	}
	public BigDecimal getFederalTaxPercent() {
		return federalTaxPercent;
	}
	public void setFederalTaxPercent(BigDecimal federalTaxPercent) {
		this.federalTaxPercent = federalTaxPercent;
	}
	public BigDecimal getStateTaxPercent() {
		return stateTaxPercent;
	}
	public void setStateTaxPercent(BigDecimal stateTaxPercent) {
		this.stateTaxPercent = stateTaxPercent;
	}
	public String getStateTaxTypeCode() {
		return stateTaxTypeCode;
	}
	public void setStateTaxTypeCode(String stateTaxTypeCode) {
		this.stateTaxTypeCode = stateTaxTypeCode;
	}
	public String getTaxpayerIdentTypeCode() {
		return taxpayerIdentTypeCode;
	}
	public void setTaxpayerIdentTypeCode(String taxpayerIdentTypeCode) {
		this.taxpayerIdentTypeCode = taxpayerIdentTypeCode;
	}
	public String getTaxpayerIdentNo() {
		return taxpayerIdentNo;
	}
	public void setTaxpayerIdentNo(String taxpayerIdentNo) {
		this.taxpayerIdentNo = taxpayerIdentNo;
	}
	public DistributionAddress getAddress() {
		return address;
	}
	public void setAddress(DistributionAddress address) {
		this.address = address;
	}
	public Collection<Payee> getPayees() {
		return payees;
	}
	public void setPayees(Collection<Payee> payees) {
		this.payees = payees;
	}
	public StateTaxVO getStateTaxVo() {
		return stateTaxVo;
	}
	public void setStateTaxVo(StateTaxVO stateTaxVo) {
		this.stateTaxVo = stateTaxVo;
	}
	public Integer getCreatedById() {
		return createdById;
	}
	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}
	public Integer getLastUpdatedById() {
		return lastUpdatedById;
	}
	public void setLastUpdatedById(Integer lastUpdatedById) {
		this.lastUpdatedById = lastUpdatedById;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Integer getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}	    

}
