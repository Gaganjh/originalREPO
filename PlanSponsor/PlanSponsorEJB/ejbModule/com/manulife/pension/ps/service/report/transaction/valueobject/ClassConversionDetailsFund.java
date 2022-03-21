package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;

import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.account.valueobject.ValueObject;
import com.manulife.util.render.NumberRender;

public class ClassConversionDetailsFund extends TransactionDetailsFund implements ValueObject, Comparable, Cloneable {
	private BigDecimal toAmount;
	private BigDecimal toUnitValue;
	private BigDecimal toNumberOfUnits;
	    
    public ClassConversionDetailsFund(
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
            String rateType,
            BigDecimal toAmount,
            BigDecimal toUnitValue,
            BigDecimal toNumberOfUnits) {
       
        super(fundId,
              fundName,
              fundType,
              moneyTypeDescription,
              amount,
              percentage,
              unitValue,
              numberOfUnits,
              comments,
              selectedFlag,
              sortNumber,
              riskCategoryCode,
              rateType);
         	  
        setToAmount(toAmount);
        setToUnitValue(toUnitValue);
        setToNumberOfUnits(toNumberOfUnits);
    }
	
	public boolean equals(Object object) {
		
		if (object == this) {
			return true;
		}
		
		if (object == null) {
			return false;
		}
		if (!(object instanceof ClassConversionDetailsFund)) {
			return false;
		}
		ClassConversionDetailsFund that = (ClassConversionDetailsFund) object;
			
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
        ClassConversionDetailsFund that = (ClassConversionDetailsFund) obj;
		return this.getSortNumber() - that.getSortNumber();
	}
	
	public String getDisplayToNumberOfUnits() {
		return NumberRender.formatByPattern(toNumberOfUnits,
				DEFAULT_HYPHEN_STRING, 
				UNIT_NUMBER_PATTERN, 
				6, 
				BigDecimal.ROUND_HALF_DOWN);
	}

	public String getDisplayToUnitValue() {
		return NumberRender.formatByPattern(toUnitValue,
				DEFAULT_HYPHEN_STRING, 
				UNIT_VALUE_FORMAT_PATTERN, 
				2, 
				BigDecimal.ROUND_HALF_DOWN);
	}

	/**
	 * Gets the amount
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getToAmount() {
		return toAmount;
	}
	/**
	 * Sets the amount
	 * @param amount The amount to set
	 */
	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	/**
	 * Gets the numberOfUnits
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getToNumberOfUnits() {
		return toNumberOfUnits;
	}
	/**
	 * Sets the numberOfUnits
	 * @param numberOfUnits The numberOfUnits to set
	 */
	public void setToNumberOfUnits(BigDecimal toNumberOfUnits) {
		this.toNumberOfUnits = toNumberOfUnits;
		if (this.toNumberOfUnits != null) {
			this.toNumberOfUnits.setScale(6, BigDecimal.ROUND_HALF_UP);
		}
	}

	/**
	 * Gets the unitValue
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getToUnitValue() {
		return toUnitValue;
	}
	/**
	 * Sets the unitValue
	 * @param unitValue The unitValue to set
	 */
	public void setToUnitValue(BigDecimal toUnitValue) {
		this.toUnitValue = toUnitValue;
		if (this.toUnitValue != null) {
			this.toUnitValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}

}