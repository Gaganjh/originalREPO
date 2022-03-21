package com.manulife.pension.ps.web.tpabob;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.tpabob.util.TPABobColumnInfo;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * This class is the Action Form for TPABlockOfBusinessAction.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessForm extends ReportForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String asOfDateSelected;
    
    private boolean isDefaultDateSelected;

    // This will store the filtering criteria created, so that later, it can be used to show
    // "filters used" in CSV, PDF.
    private ReportCriteria filteringCriteriaSaved;

    // These will hold the filter values
    private String contractName;

    private String contractNumber;

    private String financialRepNameOrOrgName;

    private String carName;

    // This will hold the current Tab we are currently in.
    private String currentTab;
    
    private Map<String, TPABobColumnInfo> applicableColumnsForCurrentTab;
    
    // This will hold the contract number that the user clicked in TPA BOB page.
    private String contractNumberSelected;
    
    // This variable will hold the tpa UserID recieved thru request parameter. This happens when a
    // Internal user navigates to TPA BOB page thru TPA-select page.
    private String tpaUserIDSelected;
    
    // This will hold the checkbox indicator for CSV Download Page.
    private boolean contractNameCheckBoxInd;

    private boolean contractNumberCheckBoxInd;

    private boolean tpaFirmIDCheckBoxInd;

    private boolean brokerNameCheckBoxInd;

    private boolean jhClientRepNameCheckBoxInd;

    private boolean contractStatusCheckBoxInd;

    private boolean contractEffDtCheckBoxInd;

    private boolean livesCheckBoxInd;

    private boolean allocatedAssetsCheckBoxInd;

    private boolean loanAssetsCheckBoxInd;

    private boolean cashAccountBalanceCheckBoxInd;

    private boolean pbaAssetsCheckBoxInd;

    private boolean totalContractAssetsCheckBoxInd;

    private boolean ezStartCheckBoxInd;

    private boolean ezIncreaseCheckBoxInd;

    private boolean directMailCheckBoxInd;
    
    private boolean sendServiceCheckBoxInd;
    
    private boolean johnHancockPassiveTrusteeCheckBoxInd;

    private boolean giflCheckBoxInd;
    
    private boolean managedAccountCheckBoxInd;

    private boolean pamCheckBoxInd;                                                        
    
	private boolean onlineBeneficiaryDsgnCheckBoxInd;

	private boolean onlineWithdrawalsCheckBoxInd;
    

    private boolean sysWithdrawalCheckBoxInd;

    private boolean vestingPercentageCheckBoxInd;

    private boolean vestingOnStatementsCheckBoxInd;
    
    private boolean permittedDisparityCheckBoxInd;

    private boolean fswCheckBoxInd;

    private boolean dioCheckBoxInd;
    
    private boolean tpaSigningAuthorityCheckBoxInd;

    private boolean cowCheckBoxInd;
    
    private boolean payRollPathCheckBoxInd; 
    
    private boolean planHighlightsCheckBoxInd; 
    
    private boolean planHighlightsReviewedCheckBoxInd; 
    
    private boolean installmentsCheckBoxInd;
    
    private boolean payrollFeedbackCheckBoxInd;
    
    private boolean sfcCheckBoxInd;
    
    private boolean pepCheckBoxInd;
    
    public String getAsOfDateSelected() {
        return asOfDateSelected;
    }

    public void setAsOfDateSelected(String asOfDateSelected) {
        this.asOfDateSelected = asOfDateSelected;
    }

    /**
     * Get the From Date in Date Format.
     * 
     * @return - fromDate in Date format.
     */
    public Date getSelectedDateInDateFormat() {
        Date asOfDate = new Date(Long.parseLong(asOfDateSelected));
        return asOfDate;
    }

    /**
     * This method checks if the current date selected is the default date or not..
     * 
     * @return
     */
    public boolean getIsDefaultDateSelected() {
        return isDefaultDateSelected;
    }

    public void setIsDefaultDateSelected(boolean isDefaultDateSelected) {
        this.isDefaultDateSelected = isDefaultDateSelected;
    }

    public ReportCriteria getFilteringCriteriaSaved() {
        return filteringCriteriaSaved;
    }

    public void setFilteringCriteriaSaved(ReportCriteria filteringCriteriaSaved) {
        this.filteringCriteriaSaved = filteringCriteriaSaved;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFinancialRepNameOrOrgName() {
        return financialRepNameOrOrgName;
    }

    public void setFinancialRepNameOrOrgName(String financialRepNameOrOrgName) {
        this.financialRepNameOrOrgName = financialRepNameOrOrgName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, TPABobColumnInfo> getApplicableColumnsForCurrentTab() {
        return applicableColumnsForCurrentTab;
    }

    public void setApplicableColumnsForCurrentTab(
            Map<String, TPABobColumnInfo> applicableColumnsForCurrentTab) {
        this.applicableColumnsForCurrentTab = applicableColumnsForCurrentTab;
    }

    public String getContractNumberSelected() {
        return contractNumberSelected;
    }

    public void setContractNumberSelected(String contractNumberSelected) {
        this.contractNumberSelected = contractNumberSelected;
    }

    public boolean isContractNameCheckBoxInd() {
        return contractNameCheckBoxInd;
    }

    public void setContractNameCheckBoxInd(boolean contractNameCheckBoxInd) {
        this.contractNameCheckBoxInd = contractNameCheckBoxInd;
    }

    public boolean isContractNumberCheckBoxInd() {
        return contractNumberCheckBoxInd;
    }

    public void setContractNumberCheckBoxInd(boolean contractNumberCheckBoxInd) {
        this.contractNumberCheckBoxInd = contractNumberCheckBoxInd;
    }

    public boolean isTpaFirmIDCheckBoxInd() {
        return tpaFirmIDCheckBoxInd;
    }

    public void setTpaFirmIDCheckBoxInd(boolean tpaFirmIDCheckBoxInd) {
        this.tpaFirmIDCheckBoxInd = tpaFirmIDCheckBoxInd;
    }

    public boolean isBrokerNameCheckBoxInd() {
        return brokerNameCheckBoxInd;
    }

    public void setBrokerNameCheckBoxInd(boolean brokerNameCheckBoxInd) {
        this.brokerNameCheckBoxInd = brokerNameCheckBoxInd;
    }

    public boolean isJhClientRepNameCheckBoxInd() {
        return jhClientRepNameCheckBoxInd;
    }

    public void setJhClientRepNameCheckBoxInd(boolean jhClientRepNameCheckBoxInd) {
        this.jhClientRepNameCheckBoxInd = jhClientRepNameCheckBoxInd;
    }

    public boolean isContractStatusCheckBoxInd() {
        return contractStatusCheckBoxInd;
    }

    public void setContractStatusCheckBoxInd(boolean contractStatusCheckBoxInd) {
        this.contractStatusCheckBoxInd = contractStatusCheckBoxInd;
    }

    public boolean isContractEffDtCheckBoxInd() {
        return contractEffDtCheckBoxInd;
    }

    public void setContractEffDtCheckBoxInd(boolean contractEffDtCheckBoxInd) {
        this.contractEffDtCheckBoxInd = contractEffDtCheckBoxInd;
    }

    public boolean isLivesCheckBoxInd() {
        return livesCheckBoxInd;
    }

    public void setLivesCheckBoxInd(boolean livesCheckBoxInd) {
        this.livesCheckBoxInd = livesCheckBoxInd;
    }

    public boolean isAllocatedAssetsCheckBoxInd() {
        return allocatedAssetsCheckBoxInd;
    }

    public void setAllocatedAssetsCheckBoxInd(boolean allocatedAssetsCheckBoxInd) {
        this.allocatedAssetsCheckBoxInd = allocatedAssetsCheckBoxInd;
    }

    public boolean isLoanAssetsCheckBoxInd() {
        return loanAssetsCheckBoxInd;
    }

    public void setLoanAssetsCheckBoxInd(boolean loanAssetsCheckBoxInd) {
        this.loanAssetsCheckBoxInd = loanAssetsCheckBoxInd;
    }

    public boolean isCashAccountBalanceCheckBoxInd() {
        return cashAccountBalanceCheckBoxInd;
    }

    public void setCashAccountBalanceCheckBoxInd(boolean cashAccountBalanceCheckBoxInd) {
        this.cashAccountBalanceCheckBoxInd = cashAccountBalanceCheckBoxInd;
    }

    public boolean isPbaAssetsCheckBoxInd() {
        return pbaAssetsCheckBoxInd;
    }

    public void setPbaAssetsCheckBoxInd(boolean pbaAssetsCheckBoxInd) {
        this.pbaAssetsCheckBoxInd = pbaAssetsCheckBoxInd;
    }

    public boolean isTotalContractAssetsCheckBoxInd() {
        return totalContractAssetsCheckBoxInd;
    }

    public void setTotalContractAssetsCheckBoxInd(boolean totalContractAssetsCheckBoxInd) {
        this.totalContractAssetsCheckBoxInd = totalContractAssetsCheckBoxInd;
    }

    public boolean isEzStartCheckBoxInd() {
        return ezStartCheckBoxInd;
    }

    public void setEzStartCheckBoxInd(boolean ezStartCheckBoxInd) {
        this.ezStartCheckBoxInd = ezStartCheckBoxInd;
    }

    public boolean isEzIncreaseCheckBoxInd() {
        return ezIncreaseCheckBoxInd;
    }

    public void setEzIncreaseCheckBoxInd(boolean ezIncreaseCheckBoxInd) {
        this.ezIncreaseCheckBoxInd = ezIncreaseCheckBoxInd;
    }

    public boolean isDirectMailCheckBoxInd() {
        return directMailCheckBoxInd;
    }

    public void setDirectMailCheckBoxInd(boolean directMailCheckBoxInd) {
        this.directMailCheckBoxInd = directMailCheckBoxInd;
    }

    public boolean isGiflCheckBoxInd() {
        return giflCheckBoxInd;
    }

    public void setGiflCheckBoxInd(boolean giflCheckBoxInd) {
        this.giflCheckBoxInd = giflCheckBoxInd;
    }

    public boolean isManagedAccountCheckBoxInd() {
        return managedAccountCheckBoxInd;
    }

    public void setManagedAccountCheckBoxInd(boolean managedAccountCheckBoxInd) {
        this.managedAccountCheckBoxInd = managedAccountCheckBoxInd;
    }
    public boolean isPamCheckBoxInd() {
        return pamCheckBoxInd;
    }

    public void setPamCheckBoxInd(boolean pamCheckBoxInd) {
        this.pamCheckBoxInd = pamCheckBoxInd;
    }
    
    public boolean isOnlineBeneficiaryDsgnCheckBoxInd() {
		return onlineBeneficiaryDsgnCheckBoxInd;
	}

	public void setOnlineBeneficiaryDsgnCheckBoxInd(
			boolean onlineBeneficiaryDsgnCheckBoxInd) {
		this.onlineBeneficiaryDsgnCheckBoxInd = onlineBeneficiaryDsgnCheckBoxInd;
	}

	public boolean isTpaSigningAuthorityCheckBoxInd() {
		return tpaSigningAuthorityCheckBoxInd;
	}

	public void setTpaSigningAuthorityCheckBoxInd(
			boolean tpaSigningAuthorityCheckBoxInd) {
		this.tpaSigningAuthorityCheckBoxInd = tpaSigningAuthorityCheckBoxInd;
	}

	public boolean isPayRollPathCheckBoxInd() {
		return payRollPathCheckBoxInd;
	}

	public void setPayRollPathCheckBoxInd(boolean payRollPathCheckBoxInd) {
		this.payRollPathCheckBoxInd = payRollPathCheckBoxInd;
	}

	public boolean isPlanHighlightsCheckBoxInd() {
		return planHighlightsCheckBoxInd;
	}

	public void setPlanHighlightsCheckBoxInd(boolean planHighlightsCheckBoxInd) {
		this.planHighlightsCheckBoxInd = planHighlightsCheckBoxInd;
	}

	public boolean isPlanHighlightsReviewedCheckBoxInd() {
		return planHighlightsReviewedCheckBoxInd;
	}

	public void setPlanHighlightsReviewedCheckBoxInd(
			boolean planHighlightsReviewedCheckBoxInd) {
		this.planHighlightsReviewedCheckBoxInd = planHighlightsReviewedCheckBoxInd;
	}

	public boolean isOnlineWithdrawalsCheckBoxInd() {
        return onlineWithdrawalsCheckBoxInd;
    }


	public void setOnlineWithdrawalsCheckBoxInd(boolean onlineWithdrawalsCheckBoxInd) {
        this.onlineWithdrawalsCheckBoxInd = onlineWithdrawalsCheckBoxInd;
    }
	

	public boolean isSysWithdrawalCheckBoxInd() {
		return sysWithdrawalCheckBoxInd;
	}

	public void setSysWithdrawalCheckBoxInd(boolean sysWithdrawalCheckBoxInd) {
		this.sysWithdrawalCheckBoxInd = sysWithdrawalCheckBoxInd;
	}
    
    public boolean isVestingPercentageCheckBoxInd() {
        return vestingPercentageCheckBoxInd;
    }

    public void setVestingPercentageCheckBoxInd(boolean vestingPercentageCheckBoxInd) {
        this.vestingPercentageCheckBoxInd = vestingPercentageCheckBoxInd;
    }

    public boolean isVestingOnStatementsCheckBoxInd() {
        return vestingOnStatementsCheckBoxInd;
    }

    public void setVestingOnStatementsCheckBoxInd(boolean vestingOnStatementsCheckBoxInd) {
        this.vestingOnStatementsCheckBoxInd = vestingOnStatementsCheckBoxInd;
    }

    public boolean isPermittedDisparityCheckBoxInd() {
        return permittedDisparityCheckBoxInd;
    }

    public void setPermittedDisparityCheckBoxInd(boolean permittedDisparityCheckBoxInd) {
        this.permittedDisparityCheckBoxInd = permittedDisparityCheckBoxInd;
    }

    public boolean isFswCheckBoxInd() {
        return fswCheckBoxInd;
    }

    public void setFswCheckBoxInd(boolean fswCheckBoxInd) {
        this.fswCheckBoxInd = fswCheckBoxInd;
    }

    public boolean isDioCheckBoxInd() {
        return dioCheckBoxInd;
    }

    public void setDioCheckBoxInd(boolean dioCheckBoxInd) {
        this.dioCheckBoxInd = dioCheckBoxInd;
    }
    
    public boolean isCowCheckBoxInd() {
        return cowCheckBoxInd;
    }

    public void setCowCheckBoxInd(boolean cowCheckBoxInd) {
        this.cowCheckBoxInd = cowCheckBoxInd;
    }
    
    public String getTpaUserIDSelected() {
        return tpaUserIDSelected;
    }

    public void setTpaUserIDSelected(String tpaUserIDSelected) {
        this.tpaUserIDSelected = tpaUserIDSelected;
    }
    
    /**
	 * @return the sendServiceCheckBoxInd
	 */
	public boolean isSendServiceCheckBoxInd() {
		return sendServiceCheckBoxInd;
	}

	/**
	 * @param sendServiceCheckBoxInd the sendServiceCheckBoxInd to set
	 */
	public void setSendServiceCheckBoxInd(boolean sendServiceCheckBoxInd) {
		this.sendServiceCheckBoxInd = sendServiceCheckBoxInd;
	}

	public void setJohnHancockPassiveTrusteeCheckBoxInd(
			boolean johnHancockPassiveTrusteeCheckBoxInd) {
		this.johnHancockPassiveTrusteeCheckBoxInd = johnHancockPassiveTrusteeCheckBoxInd;
	}

	public boolean isJohnHancockPassiveTrusteeCheckBoxInd() {
		return johnHancockPassiveTrusteeCheckBoxInd;
	}
	
	
	
	public boolean isInstallmentsCheckBoxInd() {
		return installmentsCheckBoxInd;
	}

	public void setInstallmentsCheckBoxInd(boolean installmentsCheckBoxInd) {
		this.installmentsCheckBoxInd = installmentsCheckBoxInd;
	}
	
	public boolean isPayrollFeedbackCheckBoxInd() {
		return payrollFeedbackCheckBoxInd;
	}

	public void setPayrollFeedbackCheckBoxInd(boolean payrollFeedbackCheckBoxInd) {
		this.payrollFeedbackCheckBoxInd = payrollFeedbackCheckBoxInd;
	}

	//RPSSO-124653 Start
	public boolean isSfcCheckBoxInd() {
		return sfcCheckBoxInd;
	}

	public void setSfcCheckBoxInd(boolean sfcCheckBoxInd) {
		this.sfcCheckBoxInd = sfcCheckBoxInd;
	}

	public boolean isPepCheckBoxInd() {
		return pepCheckBoxInd;
	}

	public void setPepCheckBoxInd(boolean pepCheckBoxInd) {
		this.pepCheckBoxInd = pepCheckBoxInd;
	}
	//RPSSO-124653 End
	/**
     * This method sets all the checkboxes selected.
     */
    public void setAllColumnsChecked() {
        contractNameCheckBoxInd = true;
        contractNumberCheckBoxInd = true;
        tpaFirmIDCheckBoxInd = true;
        brokerNameCheckBoxInd = true;
        jhClientRepNameCheckBoxInd = true;
        contractStatusCheckBoxInd = true;
        contractEffDtCheckBoxInd = true;
        livesCheckBoxInd = true;
        allocatedAssetsCheckBoxInd = true;
        loanAssetsCheckBoxInd = true;
        cashAccountBalanceCheckBoxInd = true;
        pbaAssetsCheckBoxInd = true;
        totalContractAssetsCheckBoxInd = true;
        ezStartCheckBoxInd = true;
        ezIncreaseCheckBoxInd = true;
        directMailCheckBoxInd = true;
        sendServiceCheckBoxInd = true;
        johnHancockPassiveTrusteeCheckBoxInd = true;
        giflCheckBoxInd = true;
        managedAccountCheckBoxInd =true;
        pamCheckBoxInd = true;
        onlineBeneficiaryDsgnCheckBoxInd = true;
        onlineWithdrawalsCheckBoxInd = true;
        sysWithdrawalCheckBoxInd = true;
        vestingPercentageCheckBoxInd = true;
        vestingOnStatementsCheckBoxInd = true;
        permittedDisparityCheckBoxInd = true;
        fswCheckBoxInd = true;
        dioCheckBoxInd = true;
        tpaSigningAuthorityCheckBoxInd = true;
        cowCheckBoxInd = true;
        payRollPathCheckBoxInd = true; 
        planHighlightsCheckBoxInd = true; 
        planHighlightsReviewedCheckBoxInd = true;
        installmentsCheckBoxInd = true;
        payrollFeedbackCheckBoxInd = true;
        //RPSSO-124653 Start
        sfcCheckBoxInd=true;
        pepCheckBoxInd=true;
        //RPSSO-124653 End
    }

    /**
     * Reset the required variables
     */
    public void reset( HttpServletRequest request) {
        super.reset( request);
        contractNameCheckBoxInd = true; // This will be always checked as true.
        contractNumberCheckBoxInd = true; // This will be always checked as true.
        tpaFirmIDCheckBoxInd = false;
        brokerNameCheckBoxInd = false;
        jhClientRepNameCheckBoxInd = false;
        contractStatusCheckBoxInd = false;
        contractEffDtCheckBoxInd = false;
        livesCheckBoxInd = false;
        allocatedAssetsCheckBoxInd = false;
        loanAssetsCheckBoxInd = false;
        cashAccountBalanceCheckBoxInd = false;
        pbaAssetsCheckBoxInd = false;
        totalContractAssetsCheckBoxInd = false;
        ezStartCheckBoxInd = false;
        ezIncreaseCheckBoxInd = false;
        directMailCheckBoxInd = false;
        sendServiceCheckBoxInd = false;
        johnHancockPassiveTrusteeCheckBoxInd = false;
        giflCheckBoxInd = false;
        pamCheckBoxInd = false;
        onlineBeneficiaryDsgnCheckBoxInd = false;
        onlineWithdrawalsCheckBoxInd = false;
        sysWithdrawalCheckBoxInd = false;
        vestingPercentageCheckBoxInd = false;
        vestingOnStatementsCheckBoxInd = false;
        permittedDisparityCheckBoxInd = false;
        fswCheckBoxInd = false;
        dioCheckBoxInd = false;
        tpaSigningAuthorityCheckBoxInd = false;
        cowCheckBoxInd = false;
        payRollPathCheckBoxInd = false; 
        planHighlightsCheckBoxInd = false; 
        planHighlightsReviewedCheckBoxInd = false; 
        installmentsCheckBoxInd = false;
        contractNumberSelected = "";
        payrollFeedbackCheckBoxInd = false;
        //RPSSO-124653 Start
        sfcCheckBoxInd=true;
        pepCheckBoxInd=true;
        //RPSSO-124653 End
    }

}
