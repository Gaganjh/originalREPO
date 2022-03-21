package com.manulife.pension.service.withdrawal.valueobject;

import java.math.BigDecimal;

import org.apache.commons.collections.CollectionUtils;

/**
 * WithdrawalRequestMoneyType is the value object for the money types within a withdrawal request.
 * 
 * @author Paul_Glenn
 * @author Chris_Shin
 * @version 1.4 2006/09/05 14:25:33
 */
public class WithdrawalRequestMoneyType extends BaseWithdrawal {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * CATEGORY_CODE_EMPLOYEE contains the category code for employee money types.
     */
    public static final String CATEGORY_CODE_EMPLOYEE = "EE";

    /**
     * CATEGORY_CODE_EMPLOYER contains the category code for employer money types.
     */
    public static final String CATEGORY_CODE_EMPLOYER = "ER";

    public static final int VESTING_PERCENTAGE_SCALE = 3;

    public static final int WITHDRAWAL_PERCENTAGE_SCALE = 2;

    public static final int VESTING_PERCENTAGE_MINIMUM = 0;

    public static final int VESTING_PERCENTAGE_MAXIMUM = 100;

    public static final BigDecimal PERCENTAGE_FIELD_LIMIT_MAXIMUM = new BigDecimal(100);

    public static final int REQUESTED_PERCENTAGE_MINIMUM = 0;

    public static final int REQUESTED_PERCENTAGE_MAXIMUM = 100;

    /**
     * Minimum value for withdrawal amounts.
     */
    public static final BigDecimal REQUESTED_AMOUNT_MINIMUM = new BigDecimal("0.00");

    /**
     * Maximum value for withdrawal amounts.
     */
    public static final BigDecimal REQUESTED_AMOUNT_MAXIMUM = new BigDecimal("99999999999.99");

    private String moneyTypeId;

    private String moneyTypeName;

    private String moneyTypeAliasId;

    private String moneyTypeCategoryCode;

    // This field is calculated and not persisted.
    private BigDecimal availableWithdrawalAmount;

    private BigDecimal totalBalance;

    private BigDecimal vestingPercentage;

    private Boolean vestingPercentageUpdateable;

    private BigDecimal withdrawalAmount;

    private BigDecimal withdrawalPercentage;
    
    private Boolean hardshipFlag;
    
   	private Boolean hardshipAvaliableamountFlag = false;
   	
   	private Boolean hardshipAvaliableamountEedefFlag = false;
   	
   	private boolean eedefFlag;

    /**
     * Indicates if the money type instance is Rollover.
     */
    private Boolean isRolloverMoneyType;

    /**
     * Indicates if the money type instance is Voluntary Contribution.
     */
    private Boolean isVoluntaryContributionMoneyType;

    /**
     * Indicates if the money type instance is Pre-1987.
     */
    private Boolean isPre1987MoneyType;

    /**
     * Indicates if the vesting engine returned null for this money type
     */
    private BigDecimal vestingEngineValue;
    
    public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
    private String rothMoneyTypeInd;
	public String getRothMoneyTypeInd() {
		return rothMoneyTypeInd;
	}

	public void setRothMoneyTypeInd(String rothMoneyTypeInd) {
		this.rothMoneyTypeInd = rothMoneyTypeInd;
	}
	private String taxType;
    
	private BigDecimal availableHarshipAmount;
    /**
     * Constructor for WithdrawalRequestMoneyType.
     */
    public WithdrawalRequestMoneyType() {
        super();
    }

    /**
     * @return the availableWithdrawalAmount
     */
    public BigDecimal getAvailableWithdrawalAmount() {
        return availableWithdrawalAmount;
    }

	/**
     * @param availableWithdrawalAmount the availableWithdrawalAmount to set
     */
    public void setAvailableWithdrawalAmount(final BigDecimal availableWithdrawalAmount) {
        this.availableWithdrawalAmount = availableWithdrawalAmount;
    }

    /**
     * @return the moneyTypeCategoryCode
     */
    public String getMoneyTypeCategoryCode() {
        return moneyTypeCategoryCode;
    }

    /**
     * @param moneyTypeCategoryCode the moneyTypeCategoryCode to set
     */
    public void setMoneyTypeCategoryCode(final String moneyTypeCategoryCode) {
        this.moneyTypeCategoryCode = moneyTypeCategoryCode;
    }

    /**
     * @return the moneyTypeId
     */
    public String getMoneyTypeId() {
        return moneyTypeId;
    }

    /**
     * @param moneyTypeId the moneyTypeId to set
     */
    public void setMoneyTypeId(final String moneyTypeId) {
        this.moneyTypeId = moneyTypeId;
    }

    /**
     * @return the moneyTypeName
     */
    public String getMoneyTypeName() {
        return moneyTypeName;
    }

    /**
     * @param moneyTypeName the moneyTypeName to set
     */
    public void setMoneyTypeName(final String moneyTypeName) {
        this.moneyTypeName = moneyTypeName;
    }

    public String getMoneyTypeAliasId() {
        return moneyTypeAliasId;
    }

    public void setMoneyTypeAliasId(String moneyTypeAliasId) {
        this.moneyTypeAliasId = moneyTypeAliasId;
    }

    /**
     * @return the totalBalance
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance the totalBalance to set
     */
    public void setTotalBalance(final BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * @return the vestingPercentage
     */
    public BigDecimal getVestingPercentage() {
        return vestingPercentage;
    }

    /**
     * @param vestingPercentage the vestingPercentage to set
     */
    public void setVestingPercentage(final BigDecimal vestingPercentage) {
        this.vestingPercentage = vestingPercentage;
    }

    /**
     * @return the withdrawalAmount
     */
    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    /**
     * @param withdrawalAmount the withdrawalAmount to set
     */
    public void setWithdrawalAmount(final BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    /**
     * @return the withdrawalPercentage
     */
    public BigDecimal getWithdrawalPercentage() {
        return withdrawalPercentage;
    }

    /**
     * @param withdrawalPercentage the withdrawalPercentage to set
     */
    public void setWithdrawalPercentage(final BigDecimal withdrawalPercentage) {
        this.withdrawalPercentage = withdrawalPercentage;
    }

    /**
     * Checks if the money type is Pre-1987.
     * 
     * @return True if the money type is Pre-1987
     */
    public Boolean getIsPre1987MoneyType() {
        return isPre1987MoneyType;
    }

    /**
     * Sets the money type Pre-1987 flag.
     * 
     * @param isPre1987MoneyType Boolean True if the money type is Pre-1987.
     */
    public void setIsPre1987MoneyType(final Boolean isPre1987MoneyType) {
        this.isPre1987MoneyType = isPre1987MoneyType;
    }

    /**
     * Checks if the money type is Rollover.
     * 
     * @return True if the money type is Rollover
     */
    public Boolean getIsRolloverMoneyType() {
        return isRolloverMoneyType;
    }

    /**
     * Sets the money type Rollover flag.
     * 
     * @param isRolloverMoneyType - True if the money type is Rollover.
     */
    public void setIsRolloverMoneyType(final Boolean isRolloverMoneyType) {
        this.isRolloverMoneyType = isRolloverMoneyType;
    }

    /**
     * Checks if the money type is Voluntary Contribution.
     * 
     * @return True if the money type is Voluntary Contribution.
     */
    public Boolean getIsVoluntaryContributionMoneyType() {
        return isVoluntaryContributionMoneyType;
    }

    /**
     * Sets the money type Voluntary Contribution flag.
     * 
     * @param isVoluntaryContributionMoneyType - True if the money type is Voluntary Contribution.
     */
    public void setIsVoluntaryContributionMoneyType(final Boolean isVoluntaryContributionMoneyType) {
        this.isVoluntaryContributionMoneyType = isVoluntaryContributionMoneyType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doErrorCodesExist() {

        // Check base error codes
        if (CollectionUtils.isNotEmpty(getErrorCodes())) {
            return true;
        }

        // No errors exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doWarningCodesExist() {

        // Check base warning codes
        if (CollectionUtils.isNotEmpty(getWarningCodes())) {
            return true;
        }

        // No warnings exist
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean doAlertCodesExist() {

        // Check base alert codes
        if (CollectionUtils.isNotEmpty(getAlertCodes())) {
            return true;
        }

        // No alerts exist
        return false;
    }

    /**
     * @return true if you can update teh vesting percent
     */
    public Boolean getVestingPercentageUpdateable() {
        return vestingPercentageUpdateable;
    }

    /**
     * @param vestingPercentageUpdateable indicator if you can update the vesting percentage
     */
    public void setVestingPercentageUpdateable(final Boolean vestingPercentageUpdateable) {
        this.vestingPercentageUpdateable = vestingPercentageUpdateable;
    }

    public BigDecimal getVestingEngineValue() {
        return vestingEngineValue;
    }

    public void setVestingEngineValue(BigDecimal vestingEngineValue) {
        this.vestingEngineValue = vestingEngineValue;
    }

	public BigDecimal getAvailableHarshipAmount() {
		return availableHarshipAmount;
	}

	public void setAvailableHarshipAmount(BigDecimal availableHarshipAmount) {
		this.availableHarshipAmount = availableHarshipAmount;
	}

	 /**
	 * @return the hardshipFlag
	 */
	public Boolean getHardshipFlag() {
		return hardshipFlag;
	}

	/**
	 * @param hardshipFlag the hardshipFlag to set
	 */
	public void setHardshipFlag(Boolean hardshipFlag) {
		this.hardshipFlag = hardshipFlag;
	}
	 /**
	 * @return the hardshipAvaliableamountFlag
	 */
	public Boolean getHardshipAvaliableamountFlag() {
		return hardshipAvaliableamountFlag;
	}

	/**
	 * @param hardshipAvaliableamountFlag the hardshipAvaliableamountFlag to set
	 */
	public void setHardshipAvaliableamountFlag(Boolean hardshipAvaliableamountFlag) {
		this.hardshipAvaliableamountFlag = hardshipAvaliableamountFlag;
	}
	
	public Boolean getHardshipAvaliableamountEedefFlag() {
		return hardshipAvaliableamountEedefFlag;
	}

	public void setHardshipAvaliableamountEedefFlag(Boolean hardshipAvaliableamountEedefFlag) {
		this.hardshipAvaliableamountEedefFlag = hardshipAvaliableamountEedefFlag;
	}

	public boolean isEedefFlag() {
		return eedefFlag;
	}

	public void setEedefFlag(boolean eedefFlag) {
		this.eedefFlag = eedefFlag;
	}
    
	
}
