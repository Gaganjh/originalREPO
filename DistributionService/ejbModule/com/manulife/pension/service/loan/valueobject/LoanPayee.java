package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;

public class LoanPayee extends BaseSerializableCloneableObject implements Payee {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer recipientNo;
    private Integer payeeNo;
    private String firstName;
    private String lastName;
    private String organizationName;
    private String typeCode;
    private String reasonCode;
    private String paymentMethodCode;
    private String shareTypeCode;
    private BigDecimal shareValue;
    private String rolloverAccountNo;
    private String rolloverPlanName;
    private String irsDistCode;
    private Boolean mailCheckToAddress;
    private Boolean sendCheckByCourier;
    private String courierCompanyCode;
    private String courierNo;
    private Integer submissionId;

    private DistributionAddress address = new LoanAddress();

    private DistributionAddress defaultAddress = new LoanAddress();

    private PaymentInstruction paymentInstruction = new LoanPaymentInstruction();
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;	

	

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

	public Integer getRecipientNo() {
		return recipientNo;
	}

	public void setRecipientNo(Integer recipientNo) {
		this.recipientNo = recipientNo;
	}

	public Integer getPayeeNo() {
		return payeeNo;
	}

	public void setPayeeNo(Integer payeeNo) {
		this.payeeNo = payeeNo;
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

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
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

	public String getRolloverAccountNo() {
		return rolloverAccountNo;
	}

	public void setRolloverAccountNo(String rolloverAccountNo) {
		this.rolloverAccountNo = rolloverAccountNo;
	}

	public String getRolloverPlanName() {
		return rolloverPlanName;
	}

	public void setRolloverPlanName(String rolloverPlanName) {
		this.rolloverPlanName = rolloverPlanName;
	}

	public String getIrsDistCode() {
		return irsDistCode;
	}

	public void setIrsDistCode(String irsDistCode) {
		this.irsDistCode = irsDistCode;
	}

	public Boolean getMailCheckToAddress() {
		return mailCheckToAddress;
	}

	public void setMailCheckToAddress(Boolean mailCheckToAddress) {
		this.mailCheckToAddress = mailCheckToAddress;
	}

	public Boolean getSendCheckByCourier() {
		return sendCheckByCourier;
	}

	public void setSendCheckByCourier(Boolean sendCheckByCourier) {
		this.sendCheckByCourier = sendCheckByCourier;
	}

	public String getCourierCompanyCode() {
		return courierCompanyCode;
	}

	public void setCourierCompanyCode(String courierCompanyCode) {
		this.courierCompanyCode = courierCompanyCode;
	}

	public String getCourierNo() {
		return courierNo;
	}

	public void setCourierNo(String courierNo) {
		this.courierNo = courierNo;
	}

	public DistributionAddress getAddress() {
		return address;
	}

	public void setAddress(DistributionAddress address) {
		this.address = address;
	}

	public DistributionAddress getDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(DistributionAddress defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	public PaymentInstruction getPaymentInstruction() {
		return paymentInstruction;
	}

	public void setPaymentInstruction(PaymentInstruction paymentInstruction) {
		this.paymentInstruction = paymentInstruction;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	@Override
	public String getTaxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTaxes(String taxes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getParticipant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParticipant(String participant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRolloverType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRolloverType(String rolloverType) {
		// TODO Auto-generated method stub
		
	}

}
