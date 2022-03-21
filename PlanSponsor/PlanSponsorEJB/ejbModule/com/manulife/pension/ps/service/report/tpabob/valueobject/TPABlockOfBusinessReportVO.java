package com.manulife.pension.ps.service.report.tpabob.valueobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;

/**
 * This class will hold the TPA Block Of Business report information to be shown in the Main Report.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessReportVO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String contractName;

    private int contractNumber;
    
    private Date contractPlanYearEnd;

    private BigDecimal expectedFirstYearAssets;

    private BigDecimal expectedNumOfLives;

    private BigDecimal numOfLives;
    
    private BigDecimal totalAssets;
    
    private BigDecimal transferredAssetsPriorToCharges;

    private Date discontinuanceDate;

    // This is not a column that we show. But this is needed when showing the NumOfLives in the
    // online screen.
    private String productType;
    
    // A Contract can be associated to multiple financial Rep's.
    private String financialRepName;
    
    private String carName;

    // Extra CSV related columns:
    private String tpaFirmID;

    private String contractStatus;

    private Date contractEffectiveDate;

    private BigDecimal allocatedAssets;

    private BigDecimal loanAssets;

    private BigDecimal cashAccountBalance;

    private BigDecimal pbaAssets;

    private String ezStart;

    private String ezIncrease;

    private String directMail;

    private String gifl;

    private String managedAccountServiceTypeDesc;
    
    private String pam;
    
    private String onlineBeneficiaryDesignation;

    private String onlineWithdrawals;
    
    private String sysWithdrawals;

    private String vestingPercentage;

    private String vestingOnStatements;

    private String permittedDisparity;

    private String fsw;
    
    private String sendService;
    
    private String johnHancockPassiveTrusteeCode;
    
    private ArrayList<DefaultInvestmentFundVO> dio;
   
	private String tpaSigningAuthority;
    
    private String cow;
    
    private String payRollPath; 
    
    private String payrollFeedback;
    
    private String planHighlights; 
    
    private String planHighlightsReviewed; 
    
    private String installments;
    
    private SmallPlanFeature smallPlanOptionCode;  
    
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getContractPlanYearEnd() {
        return contractPlanYearEnd;
    }

    public void setContractPlanYearEnd(Date contractPlanYearEnd) {
        this.contractPlanYearEnd = contractPlanYearEnd;
    }

    public BigDecimal getExpectedFirstYearAssets() {
        return expectedFirstYearAssets == null ? new BigDecimal("0.0") : expectedFirstYearAssets;
    }

    public void setExpectedFirstYearAssets(BigDecimal expectedFirstYearAssets) {
        this.expectedFirstYearAssets = expectedFirstYearAssets;
    }

    public BigDecimal getExpectedNumOfLives() {
        return expectedNumOfLives == null ? new BigDecimal("0.0") : expectedNumOfLives;
    }

    public void setExpectedNumOfLives(BigDecimal expectedNumOfLives) {
        this.expectedNumOfLives = expectedNumOfLives;
    }

    public BigDecimal getNumOfLives() {
        return numOfLives == null ? new BigDecimal("0.0") : numOfLives;
    }

    public void setNumOfLives(BigDecimal numOfLives) {
        this.numOfLives = numOfLives;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets == null ? new BigDecimal("0.0") : totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTransferredAssetsPriorToCharges() {
        return transferredAssetsPriorToCharges == null ? new BigDecimal("0.0")
                : transferredAssetsPriorToCharges;
    }

    public void setTransferredAssetsPriorToCharges(BigDecimal transferredAssetsPriorToCharges) {
        this.transferredAssetsPriorToCharges = transferredAssetsPriorToCharges;
    }

    public Date getDiscontinuanceDate() {
        return discontinuanceDate;
    }

    public void setDiscontinuanceDate(Date discontinuanceDate) {
        this.discontinuanceDate = discontinuanceDate;
    }

    public String getFinancialRepName() {
        return financialRepName;
    }

    public void setFinancialRepName(String financialRepName) {
        this.financialRepName = financialRepName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getTpaFirmID() {
        return tpaFirmID;
    }

    public void setTpaFirmID(String tpaFirmID) {
        this.tpaFirmID = tpaFirmID;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(Date contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    public BigDecimal getAllocatedAssets() {
        return allocatedAssets == null ? new BigDecimal("0.0") : allocatedAssets;
    }

    public void setAllocatedAssets(BigDecimal allocatedAssets) {
        this.allocatedAssets = allocatedAssets;
    }

    public BigDecimal getLoanAssets() {
        return loanAssets == null ? new BigDecimal("0.0") : loanAssets;
    }

    public void setLoanAssets(BigDecimal loanAssets) {
        this.loanAssets = loanAssets;
    }

    public BigDecimal getCashAccountBalance() {
        return cashAccountBalance == null ? new BigDecimal("0.0") : cashAccountBalance;
    }

    public void setCashAccountBalance(BigDecimal cashAccountBalance) {
        this.cashAccountBalance = cashAccountBalance;
    }

    public BigDecimal getPbaAssets() {
        return pbaAssets == null ? new BigDecimal("0.0") : pbaAssets;
    }

    public void setPbaAssets(BigDecimal pbaAssets) {
        this.pbaAssets = pbaAssets;
    }

    public String getEzStart() {
        return ezStart;
    }

    public void setEzStart(String ezStart) {
        this.ezStart = ezStart;
    }

    public String getEzIncrease() {
        return ezIncrease;
    }

    public void setEzIncrease(String ezIncrease) {
        this.ezIncrease = ezIncrease;
    }

    public String getDirectMail() {
        return directMail;
    }

    public void setDirectMail(String directMail) {
        this.directMail = directMail;
    }

    public String getGifl() {
        return gifl;
    }

    public void setGifl(String gifl) {
        this.gifl = gifl;
    }

    public String getPam() {
        return pam;
    }

    public void setPam(String pam) {
        this.pam = pam;
    }

    public String getOnlineWithdrawals() {
        return onlineWithdrawals;
    }

    public void setOnlineWithdrawals(String onlineWithdrawals) {
        this.onlineWithdrawals = onlineWithdrawals;
    }

    public String getVestingPercentage() {
        return vestingPercentage;
    }

    public void setVestingPercentage(String vestingPercentage) {
        this.vestingPercentage = vestingPercentage;
    }

    public String getVestingOnStatements() {
        return vestingOnStatements;
    }

    public void setVestingOnStatements(String vestingOnStatements) {
        this.vestingOnStatements = vestingOnStatements;
    }

    public String getPermittedDisparity() {
        return permittedDisparity;
    }

    public void setPermittedDisparity(String permittedDisparity) {
        this.permittedDisparity = permittedDisparity;
    }

    public String getFsw() {
        return fsw;
    }

    public void setFsw(String fsw) {
        this.fsw = fsw;
    }

    public ArrayList<DefaultInvestmentFundVO> getDio() {
        return dio;
    }

    public void setDio(ArrayList<DefaultInvestmentFundVO> dio) {
        this.dio = dio;
    }

    public String getCow() {
        return cow;
    }

    public void setCow(String cow) {
        this.cow = cow;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

	public String getSysWithdrawals() {
		return sysWithdrawals;
	}

	public void setSysWithdrawals(String sysWithdrawals) {
		this.sysWithdrawals = sysWithdrawals;
	}

	/**
	 * @return the sendService
	 */
	public String getSendService() {
		return sendService;
	}

	/**
	 * @param sendService the sendService to set
	 */
	public void setSendService(String sendService) {
		this.sendService = sendService;
	}
	 

	public void setJohnHancockPassiveTrusteeCode(
			String johnHancockPassiveTrusteeCode) {
		this.johnHancockPassiveTrusteeCode = johnHancockPassiveTrusteeCode;
	}
	public String getTpaSigningAuthority() {
			return tpaSigningAuthority;
		}

		public void setTpaSigningAuthority(String tpaSigningAuthority) {
			this.tpaSigningAuthority = tpaSigningAuthority;
		}

		public String getPayRollPath() {
			return payRollPath;
		}
		public String getPayrollFeedback() {
			return payrollFeedback;
		}

		public void setPayrollFeedback(String payrollFeedback) {
			this.payrollFeedback = payrollFeedback;
		}

		public String getOnlineBeneficiaryDesignation() {
			return onlineBeneficiaryDesignation;
		}

		public void setOnlineBeneficiaryDesignation(String onlineBeneficiaryDesignation) {
			this.onlineBeneficiaryDesignation = onlineBeneficiaryDesignation;
		}

		public void setPayRollPath(String payRollPath) {
			this.payRollPath = payRollPath;
		}

		public String getPlanHighlights() {
			return planHighlights;
		}

		public void setPlanHighlights(String planHighlights) {
			this.planHighlights = planHighlights;
		}

		public String getPlanHighlightsReviewed() {
			return planHighlightsReviewed;
		}

		public void setPlanHighlightsReviewed(String planHighlightsReviewed) {
			this.planHighlightsReviewed = planHighlightsReviewed;
		}

	public String getJohnHancockPassiveTrusteeCode() {
		return johnHancockPassiveTrusteeCode;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public String getManagedAccountServiceTypeDesc() {
		return managedAccountServiceTypeDesc;
	}

	public void setManagedAccountServiceTypeDesc(String managedAccountServiceTypeDesc) {
		this.managedAccountServiceTypeDesc = managedAccountServiceTypeDesc;
	}

	public SmallPlanFeature getSmallPlanOptionCode() {
		return smallPlanOptionCode;
	}

	public void setSmallPlanOptionCode(SmallPlanFeature smallPlanOptionCode) {
		this.smallPlanOptionCode = smallPlanOptionCode;
	}
}
