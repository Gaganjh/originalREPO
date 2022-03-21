package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.ValueObject;
import com.manulife.util.render.NumberRender;

public class TransactionDetailsFund extends Fund implements ValueObject, Comparable, Cloneable {
	
	protected static final String DEFAULT_HYPHEN_STRING = " ";
	protected static final String UNIT_VALUE_FORMAT_PATTERN = "########0.00";
	protected static final String UNIT_NUMBER_PATTERN = "########0.000000";
	private static final String GA_ACCOUNT = "GA";
	private static final String PBA_ACCOUNT = "PBA";
	
	private String moneyTypeDescription;
	private BigDecimal amount;
	private BigDecimal percentage;
	private BigDecimal unitValue;
	private BigDecimal numberOfUnits;
	private String comments;

	private BigDecimal employeeAmount;
	private BigDecimal employeePercentage;
	private BigDecimal employerAmount;
	private BigDecimal employerPercentage;
	    
    public TransactionDetailsFund(
    		String fundId,
            String fundName,
            String fundType,
            String moneyTypeDescription,
            BigDecimal amount,
            BigDecimal percentage,
            BigDecimal unitValue,
            BigDecimal numberOfUnits,
            String comments,
            boolean selectedFlag,
            int sortNumber,
            String riskCategoryCode,
            String rateType) {
       
        super(fundId,
         	  fundName,
         	  fundType,
         	  selectedFlag,
         	  sortNumber,
         	  riskCategoryCode,
         	  rateType);
         	  
        setMoneyTypeDescription(moneyTypeDescription);
        setAmount(amount);
        setPercentage(percentage);
        setUnitValue(unitValue);
        setNumberOfUnits(numberOfUnits);
        setComments(comments);
        setAmount(amount);
    }
	
	public boolean equals(Object object) {
		
		if (object == this) {
			return true;
		}
		
		if (object == null) {
			return false;
		}
		if (!(object instanceof TransactionDetailsFund)) {
			return false;
		}
		TransactionDetailsFund that = (TransactionDetailsFund) object;
			
		if ((this.getId() == null && that.getId() != null)
				|| (this.getId() != null 
					&& !(this.getId().equals(that.getId())))) {
			return false;
		}
			
		if ((this.getName() == null && that.getName() != null)
				|| (this.getName() != null 
					&& !(this.getName().equals(that.getName())))) {
			return false;
		}
			
		if ((this.getType() == null && that.getType() != null)
				|| (this.getType() != null 
					&& !(this.getType().equals(that.getType())))) {
			return false;
		}
/*			
		if ((this.getEmployeeContributionPercent() == null && that.getEmployeeContributionPercent() != null)
				|| (this.getEmployeeContributionPercent() != null 
					&& !(this.getEmployeeContributionPercent().equals(that.getEmployeeContributionPercent())))) {
			return false;
		}
		
		if ((this.getEmployerContributionPercent() == null && that.getEmployerContributionPercent() != null)
				|| (this.getEmployerContributionPercent() != null 
					&& !(this.getEmployerContributionPercent().equals(that.getEmployerContributionPercent())))) {
			return false;
		}
*/		
		if (this.getSortNumber() != that.getSortNumber()) {
			return false;
		}

		return true;			 
	}
	
	public int hashCode() {
		return getId() == null ? 0 : getId().hashCode();
	}
	
	public int compareTo(Object obj) {
        TransactionDetailsFund that = (TransactionDetailsFund) obj;
		return this.getSortNumber() - that.getSortNumber();
	}
	
	public String getDisplayNumberOfUnits() {
		return NumberRender.formatByPattern(numberOfUnits,
				DEFAULT_HYPHEN_STRING, 
				UNIT_NUMBER_PATTERN, 
				6, 
				BigDecimal.ROUND_HALF_DOWN);
	}

	public String getDisplayUnitValue() {
		return NumberRender.formatByPattern(unitValue,
				DEFAULT_HYPHEN_STRING, 
				UNIT_VALUE_FORMAT_PATTERN, 
				2, 
				BigDecimal.ROUND_HALF_DOWN);
	}
	
	/**
	 * Gets the moneyTypeDescription
	 * @return Returns a String
	 */
	public String getMoneyTypeDescription() {
		return moneyTypeDescription;
	}
	/**
	 * Sets the moneyTypeDescription
	 * @param moneyTypeDescription The moneyTypeDescription to set
	 */
	public void setMoneyTypeDescription(String moneyTypeDescription) {
		this.moneyTypeDescription = moneyTypeDescription;
	}

	/**
	 * Gets the amount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * Sets the amount
	 * @param amount The amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Gets the comments
	 * @return Returns a String
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * Sets the comments
	 * @param comments The comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the numberOfUnits
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getNumberOfUnits() {
		return numberOfUnits;
	}
	/**
	 * Sets the numberOfUnits
	 * @param numberOfUnits The numberOfUnits to set
	 */
	public void setNumberOfUnits(BigDecimal numberOfUnits) {
		this.numberOfUnits = numberOfUnits;
		if (this.numberOfUnits != null) {
			this.numberOfUnits.setScale(6, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * Gets the unitValue
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getUnitValue() {
		return unitValue;
	}
	/**
	 * Sets the unitValue
	 * @param unitValue The unitValue to set
	 */
	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
		if (this.unitValue != null) {
			this.unitValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * Gets the percentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getPercentage() {
		return percentage;
	}
	/**
	 * Sets the percentage
	 * @param percentage The percentage to set
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
		if (percentage != null) {
			this.percentage.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}



	/**
	 * Gets the employeeAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployeeAmount() {
		return employeeAmount;
	}
	/**
	 * Sets the employeeAmount
	 * @param employeeAmount The employeeAmount to set
	 */
	public void setEmployeeAmount(BigDecimal employeeAmount) {
		this.employeeAmount = employeeAmount;
	}

	/**
	 * Gets the employeePercentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployeePercentage() {
		return employeePercentage;
	}
	/**
	 * Sets the employeePercentage
	 * @param employeePercentage The employeePercentage to set
	 */
	public void setEmployeePercentage(BigDecimal employeePercentage) {
		this.employeePercentage = employeePercentage;
	}

	/**
	 * Gets the employerAmount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployerAmount() {
		return employerAmount;
	}
	/**
	 * Sets the employerAmount
	 * @param employerAmount The employerAmount to set
	 */
	public void setEmployerAmount(BigDecimal employerAmount) {
		this.employerAmount = employerAmount;
	}

	/**
	 * Gets the employerPercentage
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getEmployerPercentage() {
		return employerPercentage;
	}
	/**
	 * Sets the employerPercentage
	 * @param employerPercentage The employerPercentage to set
	 */
	public void setEmployerPercentage(BigDecimal employerPercentage) {
		this.employerPercentage = employerPercentage;
	}

	public boolean isGuaranteedAccount() {		
		return getType().equals(GA_ACCOUNT) ? true : false;
	}
	
	public boolean isPBAAccount() {		
		return getType().equals(Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT) ? true : false;
	}
	
	public boolean displayNumberOfUnits() {
		if (isGuaranteedAccount() || isPBAAccount()) {
			return false;
		}
		return true;
	}
	public boolean displayUnitValue() {
		if (isPBAAccount()) {
			return false;
		}
		return true;
	}

}