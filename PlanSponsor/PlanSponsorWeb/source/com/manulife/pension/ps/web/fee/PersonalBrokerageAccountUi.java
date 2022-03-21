package com.manulife.pension.ps.web.fee;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.AmountType;
import com.manulife.pension.service.fee.valueobject.PersonalBrokerageAccount;


/**
 * 
 * Value Object to store Personal Brokerage Account UI Details
 * 
 * @author Dheepa Poonagal
 *
 */

public class PersonalBrokerageAccountUi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String BLANK_SPACE = " ";
	public static final String ZERO_VALUE = "0.00";
	private static final String DOLLOR_FORMAT = "#,##0.00";
	public static final String DOLLAR ="D";
	public static final DecimalFormat DOLLOR_AMOUNT_FORMAT = new DecimalFormat(DOLLOR_FORMAT);
	public static final String ZERO_AMOUNT_STRING = "0";
	
	public enum PersonalBrokerageAccountUIFields {
			pbaProviderName,
			pbaPhoneAreaCode,
			pbaPhoneNumber,
			pbaPhonePrefix,
			pbaPhoneExt,		  
			pbaEmailAddress,
			pbaMinDeposit,
			pbaRestriction,
			pbaPhone;
	}
		
	// Manager Details
	private String pbaProviderName = StringUtils.EMPTY;
	private String pbaPhoneAreaCode = StringUtils.EMPTY;
	private String pbaPhoneNumber = StringUtils.EMPTY;
	private String pbaPhonePrefix = StringUtils.EMPTY;
	private String pbaPhoneExt = StringUtils.EMPTY;
	private String pbaEmailAddress = StringUtils.EMPTY;
	private String pbaMinDeposit = StringUtils.EMPTY;
	private String pbaRestriction = StringUtils.EMPTY;
	private boolean deleteInd = false;
	
	private Set<String> changedItems = new TreeSet<String>();
	
private PersonalBrokerageAccount actualPersonalBrokerageAccount = null;
	
	public PersonalBrokerageAccountUi(PersonalBrokerageAccount display, 
			PersonalBrokerageAccount actual) {
		if(display != null) {
			this.pbaProviderName = display.getPbaProviderName();
			this.pbaPhoneAreaCode = display.getPbaPhoneAreaCode();
			this.pbaPhonePrefix = display.getPbaPhonePrefix();
			this.pbaPhoneNumber = display.getPbaPhoneNum();
			this.pbaPhoneExt = display.getPbaPhoneExt();			
			this.pbaEmailAddress = display.getPbaEmailAddress();
			if(display.getPbaMinDeposit()!=null){
				this.pbaMinDeposit = String.valueOf(display.getPbaMinDeposit());
			}
			else{
				this.pbaMinDeposit = StringUtils.EMPTY;
			}
			if(StringUtils.isNotBlank(display.getPbaRestriction())){
				this.pbaRestriction = display.getPbaRestriction().trim();
			}
			else{
				this.pbaRestriction = Constants.NO;
			}
			this.deleteInd = display.isDeleted();			
		}
		if(actual != null) {
			this.actualPersonalBrokerageAccount = actual;
		}
	}
	
	public PersonalBrokerageAccount getEditedPersonalBrokerageAccount() {
		PersonalBrokerageAccount personalBrokerageAccount = new PersonalBrokerageAccount();
		personalBrokerageAccount.setPbaProviderName(getPbaProviderName());
		personalBrokerageAccount.setPbaPhoneAreaCode(getPbaPhoneAreaCode());
		personalBrokerageAccount.setPbaPhonePrefix(getPbaPhonePrefix());
		personalBrokerageAccount.setPbaPhoneNum(getPbaPhoneNumber());
    	personalBrokerageAccount.setPbaPhoneExt(getPbaPhoneExt());
    	personalBrokerageAccount.setPbaPhone(getPbaPhone());
    	personalBrokerageAccount.setPbaEmailAddress(getPbaEmailAddress());
    	if(getPbaMinDeposit()!=null && StringUtils.isNotEmpty(getPbaMinDeposit())){
    		personalBrokerageAccount.setPbaMinDeposit(new BigDecimal(getPbaMinDeposit()));
    	}    	
    	personalBrokerageAccount.setPbaRestriction(getPbaRestriction());
		return personalBrokerageAccount;
	}
	
	public PersonalBrokerageAccount getStoredPersonalBrokerageAccount() {
		return this.actualPersonalBrokerageAccount;
	}
	
	
	public Set<String> getChangedItems() {
		return changedItems;
	}
	public void setChangedItems(
			Set<String> changedItems) {
		this.changedItems = changedItems;
	}
	
	public static Set<String> getPbaFields() {
		Set<String> values = new TreeSet<String>();
		for(PersonalBrokerageAccountUIFields field : PersonalBrokerageAccountUIFields.values()) {
			values.add(field.name());
		}
		return values;
	}
	
	public boolean getItemChanged(String item) {
		return changedItems.contains(item);
	}
	
	public String getPbaProviderName() {
		return pbaProviderName;
	}
	public void setPbaProviderName(String pbaProviderName) {
		this.pbaProviderName = pbaProviderName;
	}
	public String getPbaPhoneAreaCode() {
		return pbaPhoneAreaCode;
	}
	public void setPbaPhoneAreaCode(String pbaPhoneAreaCode) {
		this.pbaPhoneAreaCode = pbaPhoneAreaCode;
	}
	public String getPbaPhoneNumber() {
		return pbaPhoneNumber;
	}
	public void setPbaPhoneNumber(String pbaPhoneNumber) {
		this.pbaPhoneNumber = pbaPhoneNumber;
	}
	public String getPbaPhonePrefix() {
		return pbaPhonePrefix;
	}
	public void setPbaPhonePrefix(String pbaPhonePrefix) {
		this.pbaPhonePrefix = pbaPhonePrefix;
	}
	public String getPbaPhoneExt() {
		return pbaPhoneExt;
	}
	public void setPbaPhoneExt(String pbaPhoneExt) {
		this.pbaPhoneExt = pbaPhoneExt;
	}
	public String getPbaEmailAddress() {
		return pbaEmailAddress;
	}
	public void setPbaEmailAddress(String pbaEmailAddress) {
		this.pbaEmailAddress = pbaEmailAddress;
	}

	public String getPbaMinDeposit() {
		return StringUtils.trimToEmpty(pbaMinDeposit);
	}

	public void setPbaMinDeposit(String pbaMinDeposit) {
		this.pbaMinDeposit = pbaMinDeposit;
	}

	public String getPbaRestriction() {
		return pbaRestriction;
	}
	public void setPbaRestriction(String pbaRestriction) {
		this.pbaRestriction = pbaRestriction;
	}
	
	

	public boolean isDeleteInd() {
		return deleteInd;
	}

	public void setDeleteInd(boolean deleteInd) {
		this.deleteInd = deleteInd;
	}
	
	public boolean getValueEmpty() {
		if(StringUtils.isEmpty(getPbaProviderName())
			&& StringUtils.isEmpty(this.getPbaPhoneAreaCode())
			&& StringUtils.isEmpty(this.getPbaPhonePrefix())
			&& StringUtils.isEmpty(this.getPbaPhoneNumber())
			&& StringUtils.isEmpty(this.getPbaPhoneExt())
			&& StringUtils.isEmpty(this.getPbaEmailAddress())
			&& StringUtils.isEmpty(this.getPbaMinDeposit())
			&& (StringUtils.isEmpty(this.getPbaRestriction()) || "N".equalsIgnoreCase(this.getPbaRestriction()))) {
				return true;
			}
		return false;
	}
		
	
	public String getPbaPhone() {
		StringBuilder phone = new StringBuilder();
		if(StringUtils.isNotEmpty(this.getPbaPhoneAreaCode())) {
			phone.append(this.getPbaPhoneAreaCode());
			phone.append(BLANK_SPACE);
		}
		if(StringUtils.isNotEmpty(this.getPbaPhonePrefix())) {
			phone.append(this.getPbaPhonePrefix());
			phone.append(BLANK_SPACE);
		}		
		if(StringUtils.isNotEmpty(this.getPbaPhoneNumber())) {
			phone.append(this.getPbaPhoneNumber());
		}
		return phone.toString();
	}
						
			
	public String getAmountValueFormatted() {
		String amountValue = this.getNonFormattedAmountValue();		
		String formattedValue = getDecimalFormatted(amountValue);		
		return AmountType.DOLLAR.getCode().concat(formattedValue);
	}
	

	public String getNonFormattedAmountValue() {
		String value = ZERO_VALUE;
		if (StringUtils.isNotBlank(this.pbaMinDeposit)) {
			value = StringUtils.replace(this.pbaMinDeposit, ",", "");
		}
		return value;
	}
	
	
	private String getDecimalFormatted(String amountValue){
		String value = StringUtils.EMPTY;
		if (StringUtils.isBlank(amountValue)) {
			amountValue = ZERO_AMOUNT_STRING;
		}		
		value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),DOLLOR_FORMAT));		
		return value;
	}
	
	public static synchronized String formatDecimalValue(BigDecimal value, String format) { 
        return new DecimalFormat(format).format(value); 
    }
	
}
