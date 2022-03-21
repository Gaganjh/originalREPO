package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanMoneyType extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;
	private BigDecimal accountBalance;
	private BigDecimal loanBalance;
	private String moneyTypeId;
	private String contractMoneyTypeLongName;
	private String contractMoneyTypeShortName;
	private String moneyTypeAliasId;
	private String moneyTypeCategoryCode;
	private BigDecimal vestingPercentage;
	private boolean excludeIndicator;
	private boolean vestingPercentageUpdateable;
	private Integer createdById;
	private Integer lastUpdatedById;
	private Timestamp created;
	private Timestamp lastUpdated;

	// Indicates if the money type is a valid contract money type. 
	private boolean aContractMoneyType = true;

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

	public String getContractMoneyTypeLongName() {
		return contractMoneyTypeLongName;
	}

	public void setContractMoneyTypeLongName(String contractMoneyTypeLongName) {
		this.contractMoneyTypeLongName = contractMoneyTypeLongName;
	}

	public String getMoneyTypeAliasId() {
		return moneyTypeAliasId;
	}

	public void setMoneyTypeAliasId(String moneyTypeAliasId) {
		this.moneyTypeAliasId = moneyTypeAliasId;
	}

	public String getMoneyTypeCategoryCode() {
		return moneyTypeCategoryCode;
	}

	public void setMoneyTypeCategoryCode(String moneyTypeCategoryCode) {
		this.moneyTypeCategoryCode = moneyTypeCategoryCode;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getMoneyTypeId() {
		return moneyTypeId;
	}

	public void setMoneyTypeId(String moneyTypeId) {
		this.moneyTypeId = moneyTypeId;
	}

	public BigDecimal getVestingPercentage() {
		return vestingPercentage;
	}

	public void setVestingPercentage(BigDecimal vestingPercentage) {
		this.vestingPercentage = vestingPercentage;
	}

	public boolean getExcludeIndicator() {
		return excludeIndicator;
	}

	public void setExcludeIndicator(boolean excludeIndicator) {
		this.excludeIndicator = excludeIndicator;
	}

	public boolean isVestingPercentageUpdateable() {
		return vestingPercentageUpdateable;
	}

	public void setVestingPercentageUpdateable(boolean vestingPercentageUpdateable) {
		this.vestingPercentageUpdateable = vestingPercentageUpdateable;
	}

	public String getContractMoneyTypeShortName() {
		return contractMoneyTypeShortName;
	}

	public void setContractMoneyTypeShortName(String contractMoneyTypeShortName) {
		this.contractMoneyTypeShortName = contractMoneyTypeShortName;
	}

    public boolean isAContractMoneyType() {
        return aContractMoneyType;
    }

    public void setAContractMoneyType(boolean aContractMoneyType) {
        this.aContractMoneyType = aContractMoneyType;
    }

	public BigDecimal getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(BigDecimal loanBalance) {
		this.loanBalance = loanBalance;
	}
}
