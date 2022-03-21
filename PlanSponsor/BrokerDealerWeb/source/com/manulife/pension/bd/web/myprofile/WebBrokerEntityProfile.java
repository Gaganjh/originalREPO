package com.manulife.pension.bd.web.myprofile;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.BrokerEntityAddress;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.broker.valueobject.ProducerCodeInfo;
import com.manulife.pension.service.broker.valueobject.impl.BrokerEntityExtendedProfileImpl;

public class WebBrokerEntityProfile implements BrokerEntityExtendedProfile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isSsn;
	private boolean showPersonalName;
	private boolean showOrgName;
	private BrokerEntityExtendedProfileImpl delegate;
	
	public WebBrokerEntityProfile(BrokerEntityExtendedProfileImpl profile) {
		delegate = profile;
		isSsn = BDWebCommonUtils.isSsn(profile.getSsnTaxId());
		showOrgName = !StringUtils.isEmpty(profile.getOrgName()) || !isSsn;
		showPersonalName = !StringUtils.isEmpty(profile.getFirstName())
				|| !StringUtils.isEmpty(profile.getMiddleInit())
				|| !StringUtils.isEmpty(profile.getLastName()) || isSsn;
	}

	public boolean isSsn() {
		return isSsn;
	}

	public boolean isShowPersonalName() {
		return showPersonalName;
	}

	public boolean isShowOrgName() {
		return showOrgName;
	}

	public List<BrokerDealerFirm> getBrokerDealerFirms() {
		return delegate.getBrokerDealerFirms();
	}

	public String getCellPhoneNum() {
		return delegate.getCellPhoneNum();
	}

	public String getEmailAddress() {
		return delegate.getEmailAddress();
	}

	public String getFaxNum() {
		return delegate.getFaxNum();
	}

	public String getFirstName() {
		return delegate.getFirstName();
	}

	public BrokerEntityAddress getHomeAddress() {
		return delegate.getHomeAddress();
	}

	public long getId() {
		return delegate.getId();
	}

	public String getLastName() {
		return delegate.getLastName();
	}

	public BrokerEntityAddress getMailingAddress() {
		return delegate.getMailingAddress();
	}

	public String getMiddleInit() {
		return delegate.getMiddleInit();
	}

	public String getOrgName() {
		return delegate.getOrgName();
	}

	public String getPhoneNum() {
		return delegate.getPhoneNum();
	}

	public List<ProducerCodeInfo> getProducerCodes() {
		return delegate.getProducerCodes();
	}

	public String getSsnTaxId() {
		return delegate.getSsnTaxId();
	}

	public PartyStatus getStatus() {
		return delegate.getStatus();
	}

	public PartyType getType() {
		return delegate.getType();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public void setBrokerDealerFirms(List<BrokerDealerFirm> brokerDealerFirms) {
		delegate.setBrokerDealerFirms(brokerDealerFirms);
	}
	
	public void setRiaFirms(List<BrokerDealerFirm> riaFirms) {
		delegate.setRiaFirms(riaFirms);
	}

	public void setCellPhoneNum(String cellPhoneNum) {
		delegate.setCellPhoneNum(StringUtils.trimToEmpty(cellPhoneNum));
	}

	public void setEmailAddress(String emailAddress) {
		delegate.setEmailAddress(StringUtils.trimToEmpty(emailAddress));
	}

	public void setFaxNum(String faxNum) {
		delegate.setFaxNum(StringUtils.trimToEmpty(faxNum));
	}

	public void setFirstName(String firstName) {
		delegate.setFirstName(StringUtils.trimToEmpty(firstName));
	}

	public void setHomeAddress(BrokerEntityAddress homeAddress) {
		delegate.setHomeAddress(homeAddress);
	}

	public void setId(long id) {
		delegate.setId(id);
	}

	public void setLastName(String lastName) {
		delegate.setLastName(StringUtils.trimToEmpty(lastName));
	}

	public void setMailingAddress(BrokerEntityAddress mailingAddress) {
		delegate.setMailingAddress(mailingAddress);
	}

	public void setMiddleInit(String middleInit) {
		delegate.setMiddleInit(StringUtils.trimToEmpty(middleInit));
	}

	public void setOrgName(String orgName) {
		delegate.setOrgName(StringUtils.trimToEmpty(orgName));
	}

	public void setPhoneNum(String phoneNum) {
		delegate.setPhoneNum(StringUtils.trimToEmpty(phoneNum));
	}

	public void setProducerCodes(List<ProducerCodeInfo> producerCodes) {
		delegate.setProducerCodes(producerCodes);
	}

	public void setSsnTaxId(String ssnTaxId) {
		delegate.setSsnTaxId(StringUtils.trimToEmpty(ssnTaxId));
	}

	public void setStatus(PartyStatus status) {
		delegate.setStatus(status);
	}

	public void setType(PartyType type) {
		delegate.setType(type);
	}

	public BrokerEntityExtendedProfile getDelegate() {		
		return delegate;
	}

	@Override
	public List<BrokerDealerFirm> getRiaFirms() {
		return delegate.getRiaFirms();
	}
}
