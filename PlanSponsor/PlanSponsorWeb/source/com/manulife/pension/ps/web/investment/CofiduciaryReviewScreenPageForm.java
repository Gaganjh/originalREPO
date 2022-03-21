package com.manulife.pension.ps.web.investment;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO;

public class CofiduciaryReviewScreenPageForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
	
	private String contractNumber;
	private boolean editable = false;
	private String optOutIndicator;
	private Timestamp createdTS;
	private Date scheduledDate;
	private String dirty = "false";
	private List<CofidFundRecommendVO> cofidFundRecommendDetails = new ArrayList();
	
	private String cofidMontEndDateStringValue = null;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public List<CofidFundRecommendVO> getCofidFundRecommendDetails() {
		return cofidFundRecommendDetails;
	}

	public void setCofidFundRecommendDetails(
			List<CofidFundRecommendVO> cofidFundRecommendDetails) {
		this.cofidFundRecommendDetails = cofidFundRecommendDetails;
	}

	public String getOptOutIndicator() {
		return optOutIndicator;
	}

	public void setOptOutIndicator(String optOutIndicator) {
		this.optOutIndicator = optOutIndicator;
	}

	public Timestamp getCreatedTS() {
		return createdTS;
	}

	public void setCreatedTS(Timestamp createdTS) {
		this.createdTS = createdTS;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getDirty() {
		return dirty;
	}

	public void setDirty(final String dirty) {
		this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString()
				: Boolean.FALSE.toString();
	}

	public String getCofidMontEndDateStringValue() {
		return cofidMontEndDateStringValue;
	}
	
	public Date getCofidMontEndDate() throws SystemException {
		
		
		if(StringUtils.isNotBlank(cofidMontEndDateStringValue)) {
			
			try {
				return DATE_FORMATTER.parse(cofidMontEndDateStringValue);
			} catch (ParseException e) {
				throw new SystemException("Invalid cofidMontEndDateStringValue: " + cofidMontEndDateStringValue);
			}
		}
		
		return null;
	}
	
	public void setCofidMontEndDate(Date cofidMontEndDate) {
		
		cofidMontEndDateStringValue = DATE_FORMATTER.format(cofidMontEndDate);
		
	}

}
