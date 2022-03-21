package com.manulife.pension.service.distribution.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

import com.manulife.pension.service.environment.valueobject.StateTaxVO;

public interface Recipient {
	
	public Integer getCreatedById();
	public void setCreatedById(final Integer createdById);
    
	public Timestamp getCreated();
	public void setCreated(final Timestamp created);
    
	public Integer getLastUpdatedById();
	public void setLastUpdatedById(final Integer lastUpdatedById);
    
	public Timestamp getLastUpdated();
    public void setLastUpdated(final Timestamp lastUpdated);

    
    public int getRecipientNo();
    public void setRecipientNo(int recipientNo);

    public String getFirstName();
    public void setFirstName(String firstName);

    public String getLastName();
    public void setLastName(String lastName);

    public String getOrganizationName();
    public void setOrganizationName(String organizationName);

    public Boolean getUsCitizenInd();
    public void setUsCitizenInd(Boolean usCitizenInd);

    public String getStateOfResidenceCode();
    public void setStateOfResidenceCode(String stateOfResidence);

    public String getShareTypeCode();
    public void setShareTypeCode(String shareTypeCode);

    public BigDecimal getFederalTaxPercent();
    public void setFederalTaxPercent(BigDecimal federalTaxPercent);

    public BigDecimal getStateTaxPercent();
    public void setStateTaxPercent(BigDecimal stateTaxPercent);

    public String getStateTaxTypeCode();
    public void setStateTaxTypeCode(String stateTaxTypeCode);

    public String getTaxpayerIdentTypeCode();
    public void setTaxpayerIdentTypeCode(String taxpayerIdentTypeCode);

    public String getTaxpayerIdentNo();
    public void setTaxpayerIdentNo(String taxpayerIdentNo);

    public DistributionAddress getAddress();
    public void setAddress(DistributionAddress address);

    public Collection<Payee> getPayees();
    public void setPayees(Collection<Payee> payees);
    
    public StateTaxVO getStateTaxVo();    
    public void setStateTaxVo(StateTaxVO stateTaxVo);
    
	public void setSubmissionId(Integer testSubmissionId);
	public Integer getSubmissionId();
	
	public void setShareValue(BigDecimal setScale);
	public BigDecimal getShareValue();
	
}
