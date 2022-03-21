package com.manulife.pension.ps.web.plandata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;
import com.manulife.pension.service.notices.valueobject.AutomaticContributionVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanDataVO;

/**
 * PIFDataForm is submitted by the user and contains data for making 
 * an Plan Information action plan
 * @author 	Dheepa Poongol
 */
@SuppressWarnings("unchecked")
public class TabPlanDataForm extends PsAutoActionLabelForm {

    public static final String RESET_CHECKBOXES = "reset_checkboxes";
    
    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Modes for mode specific functions.
     */
    private enum Mode {
        EDIT, CONFIRM;
    };

    private Mode mode = Mode.CONFIRM;

    // This contains the data for the dropdown lists.
    // Each list in the map is keyed from constants in CodeLookupCache.
    private Map lookupData;
    
    private List matchAppliesToContribList = new ArrayList();

   
    private String dirty = "false";

    private String fromTab = null;
    private String toTab = null;
    private String selectedTab = null;
    
    private Integer contractId = null;
    
    
    // TPA details
	private String tpaFirmName;
	private Integer tpaFirmId;
	private String contractName;
		
	private NoticePlanDataVO noticePlanDataVO;
	private NoticePlanCommonVO noticePlanCommonVO;
	private String buttonClicked;
	
	//Investment Info details
	private String dIOisQDIA;
	private String transferOutDaysCode;
    private String transferOutDaysCustom;
    private String invInfoDataCompleteInd;
    private String safeHarborDataCompleteInd;
    
	// Contribution & distribution details
	private String contriAndDistriDataCompleteInd;
	private Boolean deferralIrsApplies;
	private BigDecimal maxEmployeeBeforeTaxDeferralPct;
	private BigDecimal maxEmployeeBeforeTaxDeferralAmt;	
	private BigDecimal maxEmployeeRothDeferralsPct; 
	private BigDecimal maxEmployeeRothDeferralsAmt;
	private String spdEmployeeContributionRef;
	private Integer contirbutionRestirictionOnHardships;
	private String planAllowRothDeferrals;
	
	//this represents number of months, default to 6 
	private String  planAllowsInServiceWithdrawals; // maps to Plan_Allows_In_Service_Withdrawals_Ind
	private String contribtionsDistributionComplete;
	
	    
    //Automatic Contribution
	private String automaticContributionDataCompleteInd;
    private String automaticContributionProvisionType;
    private String automaticContributionProvisionTypeHidden;
    private String automaticContributionFeature1="";
    private String automaticContributionFeature2="";
    private String automaticContributionFeature3="";
    private BigDecimal contributionFeature1Pct;
    private String contributionFeature2Date;
    private String contributionFeature3SummaryText;
    private String effectiveDate;
    private Integer deferralContributionsBegin;
    private String deferralContributionsBeginOtherText;    
    private String acaAnnualIncreaseType;
    private String automaticContributionDays;
    private String automaticContributionDaysOther;
    private String employerContributions;
    private String spdEmployerContributionRef;
    private String  qACAPlanHasSafeHarborMatchOrNonElective; 
    private String qACAArrangementOptions;
	private BigDecimal  qACAMatchContributionContribPct1; 
    private BigDecimal  qACAMatchContributionMatchPct1;
    private BigDecimal  qACAMatchContributionMatchPct1Value;
    private BigDecimal  qACAMatchContributionContribPct2; 
    private BigDecimal  qACAMatchContributionMatchPct2;
    private String qACAMatchContributionToAnotherPlan;
    private String qACAMatchContributionOtherPlanName;
    private String qACASafeHarborAppliesToRoth;
    private String qACASHAppliesToCatchUpContributions;
    private String qACAAutomaticContributionDays;
    private String qACAAutomaticContributionDaysOther;
    private boolean eacaEnablePopUpForEmployerContributions;
    private boolean qacaEnablePopUpForEmployerContributions;
    
    private BigDecimal  qACANonElectiveContributionPct;
    private String qACANonElectiveAppliesToContrib;
    private String qACANonElectiveContribOtherPlan;
    private String qACASHNonElectivePlanName;
    private String qACAPlanHasAdditionalEC;
    private String qACAPlanHasAdditionalECon;
    private String qACASHMatchVesting;
    private String qACASummaryPlanDesc;
    private BigDecimal qACASHMatchVestingPct1;
    private BigDecimal qACASHMatchVestingPct2;
    
    private String qDIAFeeRestrictionOnTransferOutDays; // maps to QDIA_Fee_Restriction_On_Transfer_Out_Days
	private AutomaticContributionVO oldAutomaticContributionVO = new AutomaticContributionVO();
	//safeHarbor fields
	private String  planHasSafeHarborMatchOrNonElective;  
	private String  matchContributionContribPct1; 
    private String  matchContributionMatchPct1;
    private String  matchContributionContribPct2; 
    private String  matchContributionMatchPct2;
    private String matchAppliesToContrib;
    private String matchContributionToAnotherPlan;
    private String matchContributionOtherPlanName;
    private String safeHarborAppliesToRoth;
    private String sHAppliesToCatchUpContributions;
    private boolean shDisableYesForAdditionalEmployerContributions;
    private String contributionApplicableToPlanDate;
    private BigDecimal contributionApplicableToPlanPct;
    private String enablePlanYearEndDateAndPercentageComp;
    private boolean shEnablePopUpForEmployerContributions;
	private String nonElectiveContribOption;
    private String  nonElectiveContributionPct;
    private String nonElectiveAppliesToContrib;
    private String nonElectiveContribOtherPlan;
    private String SHNonElectivePlanName;
    private String planHasAdditionalEC;
    private String summaryPlanDesc;
    
    //CR011
    private String planHasSHACA;
    private String sHautomaticContributionFeature1;
    private String sHautomaticContributionFeature2;
    private String sHautomaticContributionFeature3;
    private BigDecimal shContributionFeature1Pct;
    private String shContributionFeature2Date;
    private String shContributionFeature3SummaryText;
    private String SHAutoContributionWD;
    private String SHAutomaticContributionDays;
    private String SHAutomaticContributionDaysOther;
    private boolean shContributionFeature1PctMissing;
    private boolean shContributionFeature2DateIdMissing;
    private boolean shContributionFeature3SummaryTextMissing;
    private String shACAAnnualIncreaseType;
 
    //Vesting Schedule
    private List<Boolean> vestingScheduleCompletion;
    
    private String sortField;
    private String sortDirection;
    private int pageNumber = 1;
    private String reportId;
    private boolean pdfCapped = false;
    private int cappedRowsInPDF = 0;
    
    private String[] excludedMoneyTypename;
    private int excludeCount;
    private List<Boolean> excludeBox;
    
    //Notice Service
    private String noticeServiceInd;
    private String noticeTypeSelected;
    private String displayNoticeType;
    
    //CR009
    private String autoContributionWD;
    
    private Collection<MoneyTypeExcludeObject> moneyTypeExcludeObject;
    private boolean pendingPIFCompletion;    
    
    
    private boolean  contributionFeature1PctMissing;
    public boolean isContributionFeature1PctMissing() {
		return contributionFeature1PctMissing;
	}

	public void setContributionFeature1PctMissing(
			boolean contributionFeature1PctMissing) {
		this.contributionFeature1PctMissing = contributionFeature1PctMissing;
	}

	public boolean isContributionFeature2DateIdMissing() {
		return contributionFeature2DateIdMissing;
	}

	public void setContributionFeature2DateIdMissing(
			boolean contributionFeature2DateIdMissing) {
		this.contributionFeature2DateIdMissing = contributionFeature2DateIdMissing;
	}

	public boolean isContributionFeature3SummaryTextMissing() {
		return contributionFeature3SummaryTextMissing;
	}

	public void setContributionFeature3SummaryTextMissing(
			boolean contributionFeature3SummaryTextMissing) {
		this.contributionFeature3SummaryTextMissing = contributionFeature3SummaryTextMissing;
	}

	private boolean  contributionFeature2DateIdMissing;
    private boolean  contributionFeature3SummaryTextMissing;
    	


	public Collection<MoneyTypeExcludeObject> getMoneyTypeExcludeObject() {
		return moneyTypeExcludeObject;
	}

	public void setMoneyTypeExcludeObject(
			Collection<MoneyTypeExcludeObject> moneyTypeExcludeObject) {
		this.moneyTypeExcludeObject = moneyTypeExcludeObject;
	}

	public String getqDIAFeeRestrictionOnTransferOutDays() {
		return qDIAFeeRestrictionOnTransferOutDays;
	}

	public void setqDIAFeeRestrictionOnTransferOutDays(
			String qDIAFeeRestrictionOnTransferOutDays) {
		this.qDIAFeeRestrictionOnTransferOutDays = qDIAFeeRestrictionOnTransferOutDays;
	}

	public String getdIOisQDIA() {
		return dIOisQDIA;
	}

	public void setdIOisQDIA(String dIOisQDIA) {
		this.dIOisQDIA = dIOisQDIA;
	}


    /**
     * This method will return capped number of rows in PDF.
     * @return Returns the cappedRowsInPDF.
     */
    public int getCappedRowsInPDF() {
        return cappedRowsInPDF;
    }
    
    /**
     * This method sets capped number of rows in PDF.
     * @param cappedRowsInPDF The cappedRowsInPDF to set.
     */
    public void setCappedRowsInPDF(int cappedRowsInPDF) {
        this.cappedRowsInPDF = cappedRowsInPDF;
    }

    /**
     * This method will return true if the PDF has been capped.
     * @return boolean
     */
    public boolean getPdfCapped() {
        return pdfCapped;
    }
    
    /**
	 * This method sets if the PDF has been capped.
	 * @param pdfCapped
	 */
	public void setPdfCapped(boolean pdfCapped) {
		this.pdfCapped = pdfCapped;
	}

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @param reportId The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

 	/**
	 * Gets the pageNumber
	 * @return Returns a int
	 */
	public int getPageNumber() {
		return pageNumber;
	}
	/**
	 * Sets the pageNumber
	 * @param pageNumber The pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gets the sortField
	 * @return Returns a String
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * Sets the sortField
	 * @param sortField The sortField to set
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}


	/**
	 * Gets the sortDirection
	 * @return Returns a String
	 */
	public String getSortDirection() {
		return sortDirection;
	}
	/**
	 * Sets the sortDirection
	 * @param sortDirection The sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	
	/**
	 * Clears the report form. Since report form is stored in the session and
	 * is reused by many other actions, we must clear the parameters when
	 * we switch reports. 
	 */
	public void clear() {
		pageNumber = 1;
		sortField = null;
		sortDirection = null;
		reportId = null;
	}
    
    /**
     * Default constructor.
     */
    public TabPlanDataForm() {
        super();
    }

    public void reset( HttpServletRequest request) {
        buttonClicked = "";
        selectedTab="";
        toTab="";
    }
    
    /**
     * @return the lookupData
     */
    public Map getLookupData() {
        return lookupData;
    }

    /**
     * @param lookupData the lookupData to set
     */
    public void setLookupData(Map lookupData) {
        this.lookupData = lookupData;
    }

     

	/**
	 * @return the fromTab
	 */
	public String getFromTab() {
		return fromTab;
	}

	/**
	 * @param fromTab the fromTab to set
	 */
	public void setFromTab(String fromTab) {
		this.fromTab = fromTab;
	}

	/**
	 * @return the toTab
	 */
	public String getToTab() {
		return toTab;
	}

	/**
	 * @param toTab the toTab to set
	 */
	public void setToTab(String toTab) {
		this.toTab = toTab;
	}

	/**
	 * @return the selectedTab
	 */
	public String getSelectedTab() {
		return selectedTab;
	}

	/**
	 * @param selectedTab the selectedTab to set
	 */
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	

	/**
	 * @return the dirty
	 */
	public String getDirty() {
		return dirty;
	}



	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(String dirty) {
        this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString() : Boolean.FALSE
                .toString();
	}
	
    /**
     * Sets the mode to be edit.
     */
    public void setEditMode() {
        mode = Mode.EDIT;
    }

    /**
     * Sets the mode to be confirm.
     */
    public void setConfirmMode() {
        mode = Mode.CONFIRM;
    }

    /**
     * Queries if the mode is edit.
     */
    public boolean isEditMode() {
        return mode == Mode.EDIT;
    }

    /**
     * Queries if the mode is confirm.
     */
    public boolean isConfirmMode() {
        return mode == Mode.CONFIRM;
    }

	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	
   

	/**
	 * @return the tpaFirmName
	 */
	public String getTpaFirmName() {
		return tpaFirmName;
	}

	/**
	 * @param tpaFirmName the tpaFirmName to set
	 */
	public void setTpaFirmName(String tpaFirmName) {
		this.tpaFirmName = tpaFirmName;
	}

	/**
	 * @return the tpaFirmId
	 */
	public Integer getTpaFirmId() {
		return tpaFirmId;
	}

	/**
	 * @param tpaFirmId the tpaFirmId to set
	 */
	public void setTpaFirmId(Integer tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}

	/**
	 * @return the contractName
	 */
	public String getContractName() {
		return contractName;
	}

	/**
	 * @param contractName the contractName to set
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	public NoticePlanDataVO getNoticePlanDataVO() {
        return noticePlanDataVO;
    }

    public void setNoticePlanDataVO(NoticePlanDataVO noticePlanDataVO) {
        this.noticePlanDataVO = noticePlanDataVO;
    }
    
    
    public String getTransferOutDaysCode() {
        return transferOutDaysCode;
    }

    public void setTransferOutDaysCode(String transferOutDaysCode) {
        this.transferOutDaysCode = transferOutDaysCode;
    }


    public String getTransferOutDaysCustom() {
        return transferOutDaysCustom;
    }

    public void setTransferOutDaysCustom(String transferOutDaysCustom) {
        this.transferOutDaysCustom = transferOutDaysCustom;
    }

    public String getButtonClicked() {
        return buttonClicked;
    }


    public void setButtonClicked(String buttonClicked) {
        this.buttonClicked = buttonClicked;
    }
    
    public String getContriAndDistriDataCompleteInd() {
        return contriAndDistriDataCompleteInd;
    }

    public void setContriAndDistriDataCompleteInd(String contriAndDistriDataCompleteInd) {
        this.contriAndDistriDataCompleteInd = contriAndDistriDataCompleteInd;
    }

    public BigDecimal getMaxEmployeeBeforeTaxDeferralPct() {
		return maxEmployeeBeforeTaxDeferralPct;
	}

	public void setMaxEmployeeBeforeTaxDeferralPct(
			BigDecimal maxEmployeeBeforeTaxDeferralPct) {
		this.maxEmployeeBeforeTaxDeferralPct = maxEmployeeBeforeTaxDeferralPct;
	}

	public BigDecimal getMaxEmployeeBeforeTaxDeferralAmt() {
		return maxEmployeeBeforeTaxDeferralAmt;
	}

	public void setMaxEmployeeBeforeTaxDeferralAmt(
			BigDecimal maxEmployeeBeforeTaxDeferralAmt) {
		this.maxEmployeeBeforeTaxDeferralAmt = maxEmployeeBeforeTaxDeferralAmt;
	}

	public BigDecimal getMaxEmployeeRothDeferralsPct() {
		return maxEmployeeRothDeferralsPct;
	}

	public void setMaxEmployeeRothDeferralsPct(
			BigDecimal maxEmployeeRothDeferralsPct) {
		this.maxEmployeeRothDeferralsPct = maxEmployeeRothDeferralsPct;
	}

	public BigDecimal getMaxEmployeeRothDeferralsAmt() {
		return maxEmployeeRothDeferralsAmt;
	}

	public void setMaxEmployeeRothDeferralsAmt(
			BigDecimal maxEmployeeRothDeferralsAmt) {
		this.maxEmployeeRothDeferralsAmt = maxEmployeeRothDeferralsAmt;
	}

	public String getSpdEmployeeContributionRef() {
		return spdEmployeeContributionRef;
	}

	public void setSpdEmployeeContributionRef(String spdEmployeeContributionRef) {
		this.spdEmployeeContributionRef = spdEmployeeContributionRef;
	}

	public NoticePlanCommonVO getNoticePlanCommonVO() {
		return noticePlanCommonVO;
	}

	public void setNoticePlanCommonVO(NoticePlanCommonVO noticePlanCommonVO) {
		this.noticePlanCommonVO = noticePlanCommonVO;
	}

	public Integer getContirbutionRestirictionOnHardships() {
		return contirbutionRestirictionOnHardships;
	}

	public void setContirbutionRestirictionOnHardships(
			Integer contirbutionRestirictionOnHardships) {
		this.contirbutionRestirictionOnHardships = contirbutionRestirictionOnHardships;
	}

	public String getPlanAllowRothDeferrals() {
		return planAllowRothDeferrals;
	}

	public void setPlanAllowRothDeferrals(String planAllowRothDeferrals) {
		this.planAllowRothDeferrals = planAllowRothDeferrals;
	}

	public String getPlanAllowsInServiceWithdrawals() {
		return planAllowsInServiceWithdrawals;
	}

	public void setPlanAllowsInServiceWithdrawals(
			String planAllowsInServiceWithdrawals) {
		this.planAllowsInServiceWithdrawals = planAllowsInServiceWithdrawals;
	}

	public String getAutomaticContributionProvisionType() {
		return automaticContributionProvisionType;
	}

	public void setAutomaticContributionProvisionType(
			String automaticContributionProvisionType) {
		this.automaticContributionProvisionType = automaticContributionProvisionType;
	}

	public String getAutomaticContributionFeature2() {
		return automaticContributionFeature2;
	}

	public void setAutomaticContributionFeature2(
			String automaticContributionFeature2) {
		this.automaticContributionFeature2 = automaticContributionFeature2;
	}

	public String getAutomaticContributionFeature3() {
		return automaticContributionFeature3;
	}

	public void setAutomaticContributionFeature3(
			String automaticContributionFeature3) {
		this.automaticContributionFeature3 = automaticContributionFeature3;
	}

	public BigDecimal getContributionFeature1Pct() {
		return contributionFeature1Pct;
	}

	public void setContributionFeature1Pct(BigDecimal contributionFeature1Pct) {
		this.contributionFeature1Pct = contributionFeature1Pct;
	}

	public String getContributionFeature2Date() {
		return contributionFeature2Date;
	}

	public void setContributionFeature2Date(String contributionFeature2Date) {
		this.contributionFeature2Date = contributionFeature2Date;
	}

	public String getContributionFeature3SummaryText() {
		return contributionFeature3SummaryText;
	}

	public void setContributionFeature3SummaryText(
			String contributionFeature3SummaryText) {
		this.contributionFeature3SummaryText = contributionFeature3SummaryText;
	}

    public String getContribtionsDistributionComplete() {
        return contribtionsDistributionComplete;
    }

    public void setContribtionsDistributionComplete(String contribtionsDistributionComplete) {
        this.contribtionsDistributionComplete = contribtionsDistributionComplete;
    }    

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getDeferralContributionsBegin() {
		return deferralContributionsBegin;
	}

	public void setDeferralContributionsBegin(Integer deferralContributionsBegin) {
		this.deferralContributionsBegin = deferralContributionsBegin;
	}

	public String getDeferralContributionsBeginOtherText() {
		return deferralContributionsBeginOtherText;
	}

	public void setDeferralContributionsBeginOtherText(
			String deferralContributionsBeginOtherText) {
		this.deferralContributionsBeginOtherText = deferralContributionsBeginOtherText;
	}
	
	public String getAutomaticContributionDays() {
		return automaticContributionDays;
	}

	public void setAutomaticContributionDays(String automaticContributionDays) {
		this.automaticContributionDays = automaticContributionDays;
	}


	public String getAutomaticContributionDaysOther() {
		return automaticContributionDaysOther;
	}

	public void setAutomaticContributionDaysOther(
			String automaticContributionDaysOther) {
		this.automaticContributionDaysOther = automaticContributionDaysOther;
	}

	public String getEmployerContributions() {
		return employerContributions;
	}

	public void setEmployerContributions(String employerContributions) {
		this.employerContributions = employerContributions;
	}

	public String getSpdEmployerContributionRef() {
		return spdEmployerContributionRef;
	}

	public void setSpdEmployerContributionRef(String spdEmployerContributionRef) {
		this.spdEmployerContributionRef = spdEmployerContributionRef;
	}

    public String getPlanHasSafeHarborMatchOrNonElective() {
        return planHasSafeHarborMatchOrNonElective;
    }

    public void setPlanHasSafeHarborMatchOrNonElective(String planHasSafeHarborMatchOrNonElective) {
        this.planHasSafeHarborMatchOrNonElective = planHasSafeHarborMatchOrNonElective;
    }

    

    public String getMatchContributionContribPct1() {
        return matchContributionContribPct1;
    }

    public void setMatchContributionContribPct1(String matchContributionContribPct1) {
        this.matchContributionContribPct1 = matchContributionContribPct1;
    }

    public String getMatchContributionMatchPct1() {
        return matchContributionMatchPct1;
    }

    public void setMatchContributionMatchPct1(String matchContributionMatchPct1) {
        this.matchContributionMatchPct1 = matchContributionMatchPct1;
    }

    public String getMatchContributionContribPct2() {
        return matchContributionContribPct2;
    }

    public void setMatchContributionContribPct2(String matchContributionContribPct2) {
        this.matchContributionContribPct2 = matchContributionContribPct2;
    }

    public String getMatchContributionMatchPct2() {
        return matchContributionMatchPct2;
    }

    public void setMatchContributionMatchPct2(String matchContributionMatchPct2) {
        this.matchContributionMatchPct2 = matchContributionMatchPct2;
    }

    public String getMatchAppliesToContrib() {
        return matchAppliesToContrib;
    }

    public void setMatchAppliesToContrib(String matchAppliesToContrib) {
        this.matchAppliesToContrib = matchAppliesToContrib;
    }

    public String getMatchContributionToAnotherPlan() {
        return matchContributionToAnotherPlan;
    }

    public void setMatchContributionToAnotherPlan(String matchContributionToAnotherPlan) {
        this.matchContributionToAnotherPlan = matchContributionToAnotherPlan;
    }

    public String getMatchContributionOtherPlanName() {
        return matchContributionOtherPlanName;
    }

    public void setMatchContributionOtherPlanName(String matchContributionOtherPlanName) {
        this.matchContributionOtherPlanName = matchContributionOtherPlanName;
    }

    public String getSafeHarborAppliesToRoth() {
        return safeHarborAppliesToRoth;
    }

    public void setSafeHarborAppliesToRoth(String safeHarborAppliesToRoth) {
        this.safeHarborAppliesToRoth = safeHarborAppliesToRoth;
    }

    public String getsHAppliesToCatchUpContributions() {
        return sHAppliesToCatchUpContributions;
    }

    public void setsHAppliesToCatchUpContributions(String sHAppliesToCatchUpContributions) {
        this.sHAppliesToCatchUpContributions = sHAppliesToCatchUpContributions;
    }

    public String getNonElectiveContribOption() {
        return nonElectiveContribOption;
    }

    public void setNonElectiveContribOption(String nonElectiveContribOption) {
        this.nonElectiveContribOption = nonElectiveContribOption;
    }

    public String getNonElectiveContributionPct() {
        return nonElectiveContributionPct;
    }

    public void setNonElectiveContributionPct(String nonElectiveContributionPct) {
        this.nonElectiveContributionPct = nonElectiveContributionPct;
    }

    public String getNonElectiveAppliesToContrib() {
        return nonElectiveAppliesToContrib;
    }

    public void setNonElectiveAppliesToContrib(String nonElectiveAppliesToContrib) {
        this.nonElectiveAppliesToContrib = nonElectiveAppliesToContrib;
    }

    public String getNonElectiveContribOtherPlan() {
        return nonElectiveContribOtherPlan;
    }

    public void setNonElectiveContribOtherPlan(String nonElectiveContribOtherPlan) {
        this.nonElectiveContribOtherPlan = nonElectiveContribOtherPlan;
    }

    public String getSHNonElectivePlanName() {
        return SHNonElectivePlanName;
    }

    public void setSHNonElectivePlanName(String sHNonElectivePlanName) {
        SHNonElectivePlanName = sHNonElectivePlanName;
    }

    public String getPlanHasAdditionalEC() {
        return planHasAdditionalEC;
    }

    public void setPlanHasAdditionalEC(String planHasAdditionalEC) {
        this.planHasAdditionalEC = planHasAdditionalEC;
    }

    public String getSummaryPlanDesc() {
        return summaryPlanDesc;
    }

    public void setSummaryPlanDesc(String summaryPlanDesc) {
        this.summaryPlanDesc = summaryPlanDesc;
    }

	public String getqACAPlanHasSafeHarborMatchOrNonElective() {
		return qACAPlanHasSafeHarborMatchOrNonElective;
	}

	public void setqACAPlanHasSafeHarborMatchOrNonElective(
			String qACAPlanHasSafeHarborMatchOrNonElective) {
		this.qACAPlanHasSafeHarborMatchOrNonElective = qACAPlanHasSafeHarborMatchOrNonElective;
	}

	public BigDecimal getqACAMatchContributionContribPct1() {
		return qACAMatchContributionContribPct1;
	}

	public void setqACAMatchContributionContribPct1(
			BigDecimal qACAMatchContributionContribPct1) {
		this.qACAMatchContributionContribPct1 = qACAMatchContributionContribPct1;
	}

	public BigDecimal getqACAMatchContributionMatchPct1() {
		return qACAMatchContributionMatchPct1;
	}

	public void setqACAMatchContributionMatchPct1(
			BigDecimal qACAMatchContributionMatchPct1) {
		this.qACAMatchContributionMatchPct1 = qACAMatchContributionMatchPct1;
	}

	public BigDecimal getqACAMatchContributionContribPct2() {
		return qACAMatchContributionContribPct2;
	}

	public void setqACAMatchContributionContribPct2(
			BigDecimal qACAMatchContributionContribPct2) {
		this.qACAMatchContributionContribPct2 = qACAMatchContributionContribPct2;
	}

	public BigDecimal getqACAMatchContributionMatchPct2() {
		return qACAMatchContributionMatchPct2;
	}

	public void setqACAMatchContributionMatchPct2(
			BigDecimal qACAMatchContributionMatchPct2) {
		this.qACAMatchContributionMatchPct2 = qACAMatchContributionMatchPct2;
	}

	public String getqACAMatchContributionToAnotherPlan() {
		return qACAMatchContributionToAnotherPlan;
	}

	public void setqACAMatchContributionToAnotherPlan(
			String qACAMatchContributionToAnotherPlan) {
		this.qACAMatchContributionToAnotherPlan = qACAMatchContributionToAnotherPlan;
	}

	public String getqACAMatchContributionOtherPlanName() {
		return qACAMatchContributionOtherPlanName;
	}

	public void setqACAMatchContributionOtherPlanName(
			String qACAMatchContributionOtherPlanName) {
		this.qACAMatchContributionOtherPlanName = qACAMatchContributionOtherPlanName;
	}

	public String getqACASafeHarborAppliesToRoth() {
		return qACASafeHarborAppliesToRoth;
	}

	public void setqACASafeHarborAppliesToRoth(String qACASafeHarborAppliesToRoth) {
		this.qACASafeHarborAppliesToRoth = qACASafeHarborAppliesToRoth;
	}

	public String getqACASHAppliesToCatchUpContributions() {
		return qACASHAppliesToCatchUpContributions;
	}

	public void setqACASHAppliesToCatchUpContributions(
			String qACASHAppliesToCatchUpContributions) {
		this.qACASHAppliesToCatchUpContributions = qACASHAppliesToCatchUpContributions;
	}	
	public BigDecimal getqACANonElectiveContributionPct() {
		return qACANonElectiveContributionPct;
	}

	public void setqACANonElectiveContributionPct(
			BigDecimal qACANonElectiveContributionPct) {
		this.qACANonElectiveContributionPct = qACANonElectiveContributionPct;
	}

	public String getqACANonElectiveAppliesToContrib() {
		return qACANonElectiveAppliesToContrib;
	}

	public void setqACANonElectiveAppliesToContrib(
			String qACANonElectiveAppliesToContrib) {
		this.qACANonElectiveAppliesToContrib = qACANonElectiveAppliesToContrib;
	}

	public String getqACANonElectiveContribOtherPlan() {
		return qACANonElectiveContribOtherPlan;
	}

	public void setqACANonElectiveContribOtherPlan(
			String qACANonElectiveContribOtherPlan) {
		this.qACANonElectiveContribOtherPlan = qACANonElectiveContribOtherPlan;
	}

	public String getqACASHNonElectivePlanName() {
		return qACASHNonElectivePlanName;
	}

	public void setqACASHNonElectivePlanName(String qACASHNonElectivePlanName) {
		this.qACASHNonElectivePlanName = qACASHNonElectivePlanName;
	}

	public String getqACAPlanHasAdditionalEC() {
		return qACAPlanHasAdditionalEC;
	}

	public void setqACAPlanHasAdditionalEC(String qACAPlanHasAdditionalEC) {
		this.qACAPlanHasAdditionalEC = qACAPlanHasAdditionalEC;
	}

	public String getqACASummaryPlanDesc() {
		return qACASummaryPlanDesc;
	}

	public void setqACASummaryPlanDesc(String qACASummaryPlanDesc) {
		this.qACASummaryPlanDesc = qACASummaryPlanDesc;
	}

	public String getqACAAutomaticContributionDays() {
		return qACAAutomaticContributionDays;
	}

	public void setqACAAutomaticContributionDays(
			String qACAAutomaticContributionDays) {
		this.qACAAutomaticContributionDays = qACAAutomaticContributionDays;
	}

	public String getqACAAutomaticContributionDaysOther() {
		return qACAAutomaticContributionDaysOther;
	}

	public void setqACAAutomaticContributionDaysOther(
			String qACAAutomaticContributionDaysOther) {
		this.qACAAutomaticContributionDaysOther = qACAAutomaticContributionDaysOther;
	}

	public String getqACAPlanHasAdditionalECon() {
		return qACAPlanHasAdditionalECon;
	}

	public void setqACAPlanHasAdditionalECon(String qACAPlanHasAdditionalECon) {
		this.qACAPlanHasAdditionalECon = qACAPlanHasAdditionalECon;
	}

	public String getqACASHMatchVesting() {
		return qACASHMatchVesting;
	}

	public void setqACASHMatchVesting(String qACASHMatchVesting) {
		this.qACASHMatchVesting = qACASHMatchVesting;
	}

	public BigDecimal getqACASHMatchVestingPct1() {
		return qACASHMatchVestingPct1;
	}

	public void setqACASHMatchVestingPct1(BigDecimal qACASHMatchVestingPct1) {
		this.qACASHMatchVestingPct1 = qACASHMatchVestingPct1;
	}

	public BigDecimal getqACASHMatchVestingPct2() {
		return qACASHMatchVestingPct2;
	}

	public void setqACASHMatchVestingPct2(BigDecimal qACASHMatchVestingPct2) {
		this.qACASHMatchVestingPct2 = qACASHMatchVestingPct2;
	}

    public String getInvInfoDataCompleteInd() {
        return invInfoDataCompleteInd;
    }

    public void setInvInfoDataCompleteInd(String invInfoDataCompleteInd) {
        this.invInfoDataCompleteInd = invInfoDataCompleteInd;
    }

	public String getSafeHarborDataCompleteInd() {
        return safeHarborDataCompleteInd;
    }

    public void setSafeHarborDataCompleteInd(String safeHarborDataCompleteInd) {
        this.safeHarborDataCompleteInd = safeHarborDataCompleteInd;
    }

    public boolean isPendingPIFCompletion() {
		return pendingPIFCompletion;
	}

	public void setPendingPIFCompletion(boolean pendingPIFCompletion) {
		this.pendingPIFCompletion = pendingPIFCompletion;
	}

	public String getqACAArrangementOptions() {
		return qACAArrangementOptions;
	}

	public void setqACAArrangementOptions(String qACAArrangementOptions) {
		this.qACAArrangementOptions = qACAArrangementOptions;
	}	
	public String getAutomaticContributionDataCompleteInd() {
		return automaticContributionDataCompleteInd;
	}

	public void setAutomaticContributionDataCompleteInd(
			String automaticContributionDataCompleteInd) {
		this.automaticContributionDataCompleteInd = automaticContributionDataCompleteInd;
	}

	public BigDecimal getqACAMatchContributionMatchPct1Value() {
		return qACAMatchContributionMatchPct1Value;
	}

	public void setqACAMatchContributionMatchPct1Value(
			BigDecimal qACAMatchContributionMatchPct1Value) {
		this.qACAMatchContributionMatchPct1Value=getqACAMatchContributionMatchPct1();
		//this.qACAMatchContributionMatchPct1Value = qACAMatchContributionMatchPct1Value;
	}

	public String[] getExcludedMoneyTypename() {
		return excludedMoneyTypename;
	}

	public void setExcludedMoneyTypename(String[] excludedMoneyTypename) {
		this.excludedMoneyTypename = excludedMoneyTypename;
	}

	public int getExcludeCount() {
		return excludeCount;
	}

	public void setExcludeCount(int excludeCount) {
		this.excludeCount = excludeCount;
	}

	public String getAutomaticContributionFeature1() {
		return automaticContributionFeature1;
	}

	public void setAutomaticContributionFeature1(
			String automaticContributionFeature1) {
		this.automaticContributionFeature1 = automaticContributionFeature1;
	}

	public AutomaticContributionVO getOldAutomaticContributionVO() {
		return oldAutomaticContributionVO;
	}

	public void setOldAutomaticContributionVO(AutomaticContributionVO oldAutomaticContributionVO) {
		this.oldAutomaticContributionVO = oldAutomaticContributionVO;
	}

	public String getAutomaticContributionProvisionTypeHidden() {
		return automaticContributionProvisionTypeHidden;
	}

	public void setAutomaticContributionProvisionTypeHidden(
			String automaticContributionProvisionTypeHidden) {
		this.automaticContributionProvisionTypeHidden = automaticContributionProvisionTypeHidden;
	}

	public boolean isShDisableYesForAdditionalEmployerContributions() {
		return shDisableYesForAdditionalEmployerContributions;
	}

	public void setShDisableYesForAdditionalEmployerContributions(
			boolean shDisableYesForAdditionalEmployerContributions) {
		this.shDisableYesForAdditionalEmployerContributions = shDisableYesForAdditionalEmployerContributions;
	}

	public List<Boolean> getExcludeBox() {
		return excludeBox;
	}

	public void setExcludeBox(List<Boolean> excludeBox) {
		this.excludeBox = excludeBox;
	}

	public String getContributionApplicableToPlanDate() {
		return contributionApplicableToPlanDate;
	}

	public void setContributionApplicableToPlanDate(
			String contributionApplicableToPlanDate) {
		this.contributionApplicableToPlanDate = contributionApplicableToPlanDate;
	}

	public BigDecimal getContributionApplicableToPlanPct() {
		return contributionApplicableToPlanPct;
	}

	public void setContributionApplicableToPlanPct(
			BigDecimal contributionApplicableToPlanPct) {
		this.contributionApplicableToPlanPct = contributionApplicableToPlanPct;
	}

	public String getEnablePlanYearEndDateAndPercentageComp() {
		return enablePlanYearEndDateAndPercentageComp;
	}

	public void setEnablePlanYearEndDateAndPercentageComp(
			String enablePlanYearEndDateAndPercentageComp) {
		this.enablePlanYearEndDateAndPercentageComp = enablePlanYearEndDateAndPercentageComp;
	}

	public String getAcaAnnualIncreaseType() {
		return acaAnnualIncreaseType;
	}

	public void setAcaAnnualIncreaseType(String acaAnnualIncreaseType) {
		this.acaAnnualIncreaseType = acaAnnualIncreaseType;
	}

	public boolean isEacaEnablePopUpForEmployerContributions() {
		return eacaEnablePopUpForEmployerContributions;
	}

	public void setEacaEnablePopUpForEmployerContributions(
			boolean eacaEnablePopUpForEmployerContributions) {
		this.eacaEnablePopUpForEmployerContributions = eacaEnablePopUpForEmployerContributions;
	}

	public boolean isQacaEnablePopUpForEmployerContributions() {
		return qacaEnablePopUpForEmployerContributions;
	}

	public void setQacaEnablePopUpForEmployerContributions(
			boolean qacaEnablePopUpForEmployerContributions) {
		this.qacaEnablePopUpForEmployerContributions = qacaEnablePopUpForEmployerContributions;
	}

	public boolean isShEnablePopUpForEmployerContributions() {
		return shEnablePopUpForEmployerContributions;
	}

	public void setShEnablePopUpForEmployerContributions(
			boolean shEnablePopUpForEmployerContributions) {
		this.shEnablePopUpForEmployerContributions = shEnablePopUpForEmployerContributions;
	}

	public List<Boolean> getVestingScheduleCompletion() {
		return vestingScheduleCompletion;
	}

	public void setVestingScheduleCompletion(List<Boolean> vestingScheduleCompletion) {
		this.vestingScheduleCompletion = vestingScheduleCompletion;
	}

	public String getNoticeServiceInd() {
		return noticeServiceInd;
	}

	public void setNoticeServiceInd(String noticeServiceInd) {
		this.noticeServiceInd = noticeServiceInd;
	}

	public String getNoticeTypeSelected() {
		return noticeTypeSelected;
	}

	public void setNoticeTypeSelected(String noticeTypeSelected) {
		this.noticeTypeSelected = noticeTypeSelected;
	}

	public String getDisplayNoticeType() {
		return displayNoticeType;
	}

	public void setDisplayNoticeType(String displayNoticeType) {
		this.displayNoticeType = displayNoticeType;
	}

	public String getAutoContributionWD() {
		return autoContributionWD;
	}

	public void setAutoContributionWD(String autoContributionWD) {
		this.autoContributionWD = autoContributionWD;
	}

	public String getPlanHasSHACA() {
		return planHasSHACA;
	}

	public void setPlanHasSHACA(String planHasSHACA) {
		this.planHasSHACA = planHasSHACA;
	}

	public String getsHautomaticContributionFeature1() {
		return sHautomaticContributionFeature1;
	}

	public void setsHautomaticContributionFeature1(
			String sHautomaticContributionFeature1) {
		this.sHautomaticContributionFeature1 = sHautomaticContributionFeature1;
	}

	public String getsHautomaticContributionFeature2() {
		return sHautomaticContributionFeature2;
	}

	public void setsHautomaticContributionFeature2(
			String sHautomaticContributionFeature2) {
		this.sHautomaticContributionFeature2 = sHautomaticContributionFeature2;
	}

	public String getsHautomaticContributionFeature3() {
		return sHautomaticContributionFeature3;
	}

	public void setsHautomaticContributionFeature3(
			String sHautomaticContributionFeature3) {
		this.sHautomaticContributionFeature3 = sHautomaticContributionFeature3;
	}

	public BigDecimal getShContributionFeature1Pct() {
		return shContributionFeature1Pct;
	}

	public void setShContributionFeature1Pct(BigDecimal shContributionFeature1Pct) {
		this.shContributionFeature1Pct = shContributionFeature1Pct;
	}

	public String getShContributionFeature2Date() {
		return shContributionFeature2Date;
	}

	public void setShContributionFeature2Date(String shContributionFeature2Date) {
		this.shContributionFeature2Date = shContributionFeature2Date;
	}

	public String getShContributionFeature3SummaryText() {
		return shContributionFeature3SummaryText;
	}

	public void setShContributionFeature3SummaryText(
			String shContributionFeature3SummaryText) {
		this.shContributionFeature3SummaryText = shContributionFeature3SummaryText;
	}

	public String getSHAutoContributionWD() {
		return SHAutoContributionWD;
	}

	public void setSHAutoContributionWD(String sHAutoContributionWD) {
		SHAutoContributionWD = sHAutoContributionWD;
	}

	public String getSHAutomaticContributionDays() {
		return SHAutomaticContributionDays;
	}

	public void setSHAutomaticContributionDays(String sHAutomaticContributionDays) {
		SHAutomaticContributionDays = sHAutomaticContributionDays;
	}

	public String getSHAutomaticContributionDaysOther() {
		return SHAutomaticContributionDaysOther;
	}

	public void setSHAutomaticContributionDaysOther(
			String sHAutomaticContributionDaysOther) {
		SHAutomaticContributionDaysOther = sHAutomaticContributionDaysOther;
	}

	public boolean isShContributionFeature1PctMissing() {
		return shContributionFeature1PctMissing;
	}

	public void setShContributionFeature1PctMissing(
			boolean shContributionFeature1PctMissing) {
		this.shContributionFeature1PctMissing = shContributionFeature1PctMissing;
	}

	public boolean isShContributionFeature2DateIdMissing() {
		return shContributionFeature2DateIdMissing;
	}

	public void setShContributionFeature2DateIdMissing(
			boolean shContributionFeature2DateIdMissing) {
		this.shContributionFeature2DateIdMissing = shContributionFeature2DateIdMissing;
	}

	public boolean isShContributionFeature3SummaryTextMissing() {
		return shContributionFeature3SummaryTextMissing;
	}

	public void setShContributionFeature3SummaryTextMissing(
			boolean shContributionFeature3SummaryTextMissing) {
		this.shContributionFeature3SummaryTextMissing = shContributionFeature3SummaryTextMissing;
	}

	public String getShACAAnnualIncreaseType() {
		return shACAAnnualIncreaseType;
	}

	public void setShACAAnnualIncreaseType(String shACAAnnualIncreaseType) {
		this.shACAAnnualIncreaseType = shACAAnnualIncreaseType;
	}

	public Boolean getDeferralIrsApplies() {
		return deferralIrsApplies;
	}

	public void setDeferralIrsApplies(Boolean deferralIrsApplies) {
		this.deferralIrsApplies = deferralIrsApplies;
	}

	public List getMatchAppliesToContribList() {
		return matchAppliesToContribList;
	}

	public void setMatchAppliesToContribList(List matchAppliesToContribList) {
		this.matchAppliesToContribList = matchAppliesToContribList;
	}

	


	
}