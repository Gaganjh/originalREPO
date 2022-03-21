package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanPlanData extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	public static final String VESTING_SERVICE_CREDITING_METHOD_UNSPECIFIED = "U";
	
	private String planLegalName;
	private String contractName;
	private BigDecimal maximumLoanAmount;
	private BigDecimal minimumLoanAmount;
	private BigDecimal maximumLoanPercentage;
	private Integer maxNumberOfOutstandingLoans;
	private BigDecimal loanInterestRateOverPrime;
	private String spousalConsentReqdInd;
	private String payrollFrequency;
	private boolean noMoneyTypeAllowedForLoan;
	private boolean usingDefaultInterestRateOverPrime;
	private boolean usingDefaultMaximumAmortizationYears;
	private boolean usingDefaultMinimumLoanAmount;
	private boolean usingDefaultMaximumLoanAmount;
	private boolean usingDefaultMaxNumberOfOutstandingLoans;
	private boolean usingDefaultMaximumLoanPercentage;
	private Map<String, Integer> maximumAmortizationYearsMap = new HashMap<String, Integer>();
	private String vestingServiceCreditMethod;
    private String vestingServiceFeature;

	// Contract Data
	private String contractStatusCode;
	private BigDecimal contractLoanSetupFeeAmount;
	private BigDecimal contractLoanMonthlyFlatFee;
	private BigDecimal contractLoanExpenseMarginPct;
	private Integer thirdPartyAdminId;
	private String manulifeCompanyId;
	private boolean allMoneyTypesFullyVested;
	private List<LoanTypeVO> loanTypeList = new ArrayList<LoanTypeVO>() ;
	
	public String getContractStatusCode() {
		return contractStatusCode;
	}

	public void setContractStatusCode(String contractStatusCode) {
		this.contractStatusCode = contractStatusCode;
	}

	public boolean isNoMoneyTypeAllowedForLoan() {
		return noMoneyTypeAllowedForLoan;
	}

	public void setNoMoneyTypeAllowedForLoan(boolean noMoneyTypeAllowedForLoan) {
		this.noMoneyTypeAllowedForLoan = noMoneyTypeAllowedForLoan;
	}

	public BigDecimal getMaximumLoanAmount() {
		return maximumLoanAmount;
	}

	public void setMaximumLoanAmount(BigDecimal maximumLoanAmount) {
		this.maximumLoanAmount = maximumLoanAmount;
	}

	public BigDecimal getMinimumLoanAmount() {
		return minimumLoanAmount;
	}

	public void setMinimumLoanAmount(BigDecimal minimumLoanAmount) {
		this.minimumLoanAmount = minimumLoanAmount;
	}

	public BigDecimal getMaximumLoanPercentage() {
		return maximumLoanPercentage;
	}

	public void setMaximumLoanPercentage(BigDecimal maximumLoanPercentage) {
		this.maximumLoanPercentage = maximumLoanPercentage;
	}

	public Integer getMaxNumberOfOutstandingLoans() {
		return maxNumberOfOutstandingLoans;
	}

	public void setMaxNumberOfOutstandingLoans(
			Integer maximumNumberOfOutstandingLoans) {
		this.maxNumberOfOutstandingLoans = maximumNumberOfOutstandingLoans;
	}

	public BigDecimal getLoanInterestRateOverPrime() {
		return loanInterestRateOverPrime;
	}

	public void setLoanInterestRateOverPrime(
			BigDecimal loanInterestRateOverPrime) {
		this.loanInterestRateOverPrime = loanInterestRateOverPrime;
	}

	public String getSpousalConsentReqdInd() {
		return spousalConsentReqdInd;
	}

	public void setSpousalConsentReqdInd(String spousalConsentRequiredInd) {
		this.spousalConsentReqdInd = spousalConsentRequiredInd;
	}

	public String getPayrollFrequency() {
		return payrollFrequency;
	}

	public void setPayrollFrequency(String payrollFrequency) {
		this.payrollFrequency = payrollFrequency;
	}

	public Map<String, Integer> getMaximumAmortizationYearsMap() {
		return maximumAmortizationYearsMap;
	}

	public void setMaximumAmortizationYearsMap(
			Map<String, Integer> maximumAmortizationPeriodMap) {
		this.maximumAmortizationYearsMap = maximumAmortizationPeriodMap;
	}

	public Integer getMaximumAmortizationYears(String type) {
		return maximumAmortizationYearsMap.get(type);
	}

	public void setMaximumAmortizationYears(String type, Integer period) {
		maximumAmortizationYearsMap.put(type, period);
	}

	public boolean isUsingDefaultInterestRateOverPrime() {
		return usingDefaultInterestRateOverPrime;
	}

	public void setUsingDefaultInterestRateOverPrime(
			boolean usingDefaultInterestRateOverPrime) {
		this.usingDefaultInterestRateOverPrime = usingDefaultInterestRateOverPrime;
	}

	public boolean isUsingDefaultMaximumAmortizationYears() {
		return usingDefaultMaximumAmortizationYears;
	}

	public void setUsingDefaultMaximumAmortizationYears(
			boolean usingDefaultMaximumAmortizationYears) {
		this.usingDefaultMaximumAmortizationYears = usingDefaultMaximumAmortizationYears;
	}

	public boolean isUsingDefaultMinimumLoanAmount() {
		return usingDefaultMinimumLoanAmount;
	}

	public void setUsingDefaultMinimumLoanAmount(
			boolean usingDefaultMinimumLoanAmount) {
		this.usingDefaultMinimumLoanAmount = usingDefaultMinimumLoanAmount;
	}

	public boolean isUsingDefaultMaximumLoanAmount() {
		return usingDefaultMaximumLoanAmount;
	}

	public void setUsingDefaultMaximumLoanAmount(
			boolean usingDefaultMaximumLoanAmount) {
		this.usingDefaultMaximumLoanAmount = usingDefaultMaximumLoanAmount;
	}

	public boolean isUsingDefaultMaxNumberOfOutstandingLoans() {
		return usingDefaultMaxNumberOfOutstandingLoans;
	}

	public void setUsingDefaultMaxNumberOfOutstandingLoans(
			boolean usingDefaultMaxNumberOfOutstandingLoans) {
		this.usingDefaultMaxNumberOfOutstandingLoans = usingDefaultMaxNumberOfOutstandingLoans;
	}

	public boolean isUsingDefaultMaximumLoanPercentage() {
		return usingDefaultMaximumLoanPercentage;
	}

	public void setUsingDefaultMaximumLoanPercentage(
			boolean usingDefaultMaximumLoanPercentage) {
		this.usingDefaultMaximumLoanPercentage = usingDefaultMaximumLoanPercentage;
	}

	public BigDecimal getContractLoanSetupFeeAmount() {
		return contractLoanSetupFeeAmount;
	}

	public void setContractLoanSetupFeeAmount(
			BigDecimal contractLoanSetupFeeAmount) {
		this.contractLoanSetupFeeAmount = contractLoanSetupFeeAmount;
	}

	public BigDecimal getContractLoanMonthlyFlatFee() {
		return contractLoanMonthlyFlatFee;
	}

	public void setContractLoanMonthlyFlatFee(
			BigDecimal contractLoanMonthlyFlatFee) {
		this.contractLoanMonthlyFlatFee = contractLoanMonthlyFlatFee;
	}

	public BigDecimal getContractLoanExpenseMarginPct() {
		return contractLoanExpenseMarginPct;
	}

	public void setContractLoanExpenseMarginPct(
			BigDecimal contractLoanExpenseMarginPct) {
		this.contractLoanExpenseMarginPct = contractLoanExpenseMarginPct;
	}

	public String getPlanLegalName() {
		return planLegalName;
	}

	public void setPlanLegalName(String planName) {
		this.planLegalName = planName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

    public Integer getThirdPartyAdminId() {
        return thirdPartyAdminId;
    }

    public void setThirdPartyAdminId(Integer thirdPartyAdminId) {
        this.thirdPartyAdminId = thirdPartyAdminId;
    }

	public String getManulifeCompanyId() {
		return manulifeCompanyId;
	}

	public void setManulifeCompanyId(String manulifeCompanyId) {
		this.manulifeCompanyId = manulifeCompanyId;
	}

    public String getVestingServiceCreditMethod() {
        return vestingServiceCreditMethod;
    }

    public void setVestingServiceCreditMethod(String vestingServiceCreditMethodInd) {
        this.vestingServiceCreditMethod = vestingServiceCreditMethodInd;
    }
    
    public boolean isVestingServiceCreditMethodUnspecified() {
        if (this.vestingServiceCreditMethod == null ||
                LoanPlanData.VESTING_SERVICE_CREDITING_METHOD_UNSPECIFIED.
                    equals(this.vestingServiceCreditMethod)) {
            return true;
        } else {
            return false;
        }
    }

    public String getVestingServiceFeature() {
        return vestingServiceFeature;
    }

    public void setVestingServiceFeature(String vestingServiceFeature) {
        this.vestingServiceFeature = vestingServiceFeature;
    }

    public boolean isAllMoneyTypesFullyVested() {
        return allMoneyTypesFullyVested;
    }

    public void setAllMoneyTypesFullyVested(boolean allMoneyTypesFullyVested) {
        this.allMoneyTypesFullyVested = allMoneyTypesFullyVested;
    }
    
    public List<LoanTypeVO> getLoanTypeList() {
		return loanTypeList;
	}

	public void setLoanTypeList(List<LoanTypeVO> loanTypeList) {
		this.loanTypeList = loanTypeList;
	}
}
