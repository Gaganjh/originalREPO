package com.manulife.pension.ps.web.sendservice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanDataVO;


/**
 * 
 * @author krishta
 *
 */
public class NoticePlanDataForm extends AutoForm {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    // This contains the data for the dropdown lists.
    // Each list in the map is keyed from constants in CodeLookupCache.
    private Map<?, ?> lookupData;
    private List matchAppliesToContribList = new ArrayList();
   
    
    private Integer contractId = null;
    
    
    // TPA details
	private String tpaFirmName;
	private Integer tpaFirmId;
	private String contractName;
	private String acknowledgmentText;
	
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
    private String automaticContributionFeature1="";
    private String automaticContributionFeature2="";
    private String automaticContributionFeature3="";
    private BigDecimal contributionFeature1Pct;
    private String contributionFeature2Date;
    private String contributionFeature3SummaryText;
    private String effectiveDate;
    private Integer deferralContributionsBegin;
    private String deferralContributionsBeginOtherText;
    private String aciAllowed;
    private String aciApplyDate;
    private BigDecimal annualIncrease;
    private BigDecimal maxAutomaticIncrease;
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
    
    

	private String nonElectiveContribOption;
    private String  nonElectiveContributionPct;
    private String nonElectiveAppliesToContrib;
    private String nonElectiveContribOtherPlan;
    private String SHNonElectivePlanName;
    private String planHasAdditionalEC;
    private String summaryPlanDesc;
    
    //CR011 Additional SafeHarbor fields
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
    
    private String[] excludedMoneyTypename;
    private int excludeCount;
    
    
    private Collection<MoneyTypeExcludeObject> moneyTypeExcludeObject;
    private boolean pendingPIFCompletion;  
    private boolean contributionFeature1PctMissing;
    private boolean contributionFeature2DateIdMissing;
    private boolean contributionFeature3SummaryTextMissing;

    private String enablePlanYearEndDateAndPercentageComp;
    private String contributionApplicableToPlanDate;
    
    private String acaAnnualIncreaseType;
    private BigDecimal contributionApplicableToPlanPct;
    private String eacaPlanHasAutoContribWD;
    
    private boolean eligibleNotice;
    
    /**
     * Default constructor.
     */
    public NoticePlanDataForm() {
        super();
    }

    /**
     * @return the lookupData
     */
    public Map<?, ?> getLookupData() {
        return lookupData;
    }

    /**
     * @param lookupData the lookupData to set
     */
    public void setLookupData(Map<?, ?> lookupData) {
        this.lookupData = lookupData;
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

	/**
	 * @return the acknowledgmentText
	 */
	public String getAcknowledgmentText() {
		return acknowledgmentText;
	}

	/**
	 * @param acknowledgmentText the acknowledgmentText to set
	 */
	public void setAcknowledgmentText(String acknowledgmentText) {
		this.acknowledgmentText = acknowledgmentText;
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
	 * @return the noticePlanDataVO
	 */
	public NoticePlanDataVO getNoticePlanDataVO() {
		return noticePlanDataVO;
	}

	/**
	 * @param noticePlanDataVO the noticePlanDataVO to set
	 */
	public void setNoticePlanDataVO(NoticePlanDataVO noticePlanDataVO) {
		this.noticePlanDataVO = noticePlanDataVO;
	}

	/**
	 * @return the noticePlanCommonVO
	 */
	public NoticePlanCommonVO getNoticePlanCommonVO() {
		return noticePlanCommonVO;
	}

	/**
	 * @param noticePlanCommonVO the noticePlanCommonVO to set
	 */
	public void setNoticePlanCommonVO(NoticePlanCommonVO noticePlanCommonVO) {
		this.noticePlanCommonVO = noticePlanCommonVO;
	}

	/**
	 * @return the buttonClicked
	 */
	public String getButtonClicked() {
		return buttonClicked;
	}

	/**
	 * @param buttonClicked the buttonClicked to set
	 */
	public void setButtonClicked(String buttonClicked) {
		this.buttonClicked = buttonClicked;
	}

	/**
	 * @return the transferOutDaysCode
	 */
	public String getTransferOutDaysCode() {
		return transferOutDaysCode;
	}

	/**
	 * @param transferOutDaysCode the transferOutDaysCode to set
	 */
	public void setTransferOutDaysCode(String transferOutDaysCode) {
		this.transferOutDaysCode = transferOutDaysCode;
	}

	/**
	 * @return the transferOutDaysCustom
	 */
	public String getTransferOutDaysCustom() {
		return transferOutDaysCustom;
	}

	/**
	 * @param transferOutDaysCustom the transferOutDaysCustom to set
	 */
	public void setTransferOutDaysCustom(String transferOutDaysCustom) {
		this.transferOutDaysCustom = transferOutDaysCustom;
	}

	/**
	 * @return the invInfoDataCompleteInd
	 */
	public String getInvInfoDataCompleteInd() {
		return invInfoDataCompleteInd;
	}

	/**
	 * @param invInfoDataCompleteInd the invInfoDataCompleteInd to set
	 */
	public void setInvInfoDataCompleteInd(String invInfoDataCompleteInd) {
		this.invInfoDataCompleteInd = invInfoDataCompleteInd;
	}

	/**
	 * @return the safeHarborDataCompleteInd
	 */
	public String getSafeHarborDataCompleteInd() {
		return safeHarborDataCompleteInd;
	}

	/**
	 * @param safeHarborDataCompleteInd the safeHarborDataCompleteInd to set
	 */
	public void setSafeHarborDataCompleteInd(String safeHarborDataCompleteInd) {
		this.safeHarborDataCompleteInd = safeHarborDataCompleteInd;
	}

	/**
	 * @return the contriAndDistriDataCompleteInd
	 */
	public String getContriAndDistriDataCompleteInd() {
		return contriAndDistriDataCompleteInd;
	}

	/**
	 * @param contriAndDistriDataCompleteInd the contriAndDistriDataCompleteInd to set
	 */
	public void setContriAndDistriDataCompleteInd(
			String contriAndDistriDataCompleteInd) {
		this.contriAndDistriDataCompleteInd = contriAndDistriDataCompleteInd;
	}

	/**
	 * @return the maxEmployeeBeforeTaxDeferralPct
	 */
	public BigDecimal getMaxEmployeeBeforeTaxDeferralPct() {
		return maxEmployeeBeforeTaxDeferralPct;
	}

	/**
	 * @param maxEmployeeBeforeTaxDeferralPct the maxEmployeeBeforeTaxDeferralPct to set
	 */
	public void setMaxEmployeeBeforeTaxDeferralPct(
			BigDecimal maxEmployeeBeforeTaxDeferralPct) {
		this.maxEmployeeBeforeTaxDeferralPct = maxEmployeeBeforeTaxDeferralPct;
	}

	/**
	 * @return the maxEmployeeBeforeTaxDeferralAmt
	 */
	public BigDecimal getMaxEmployeeBeforeTaxDeferralAmt() {
		return maxEmployeeBeforeTaxDeferralAmt;
	}

	/**
	 * @param maxEmployeeBeforeTaxDeferralAmt the maxEmployeeBeforeTaxDeferralAmt to set
	 */
	public void setMaxEmployeeBeforeTaxDeferralAmt(
			BigDecimal maxEmployeeBeforeTaxDeferralAmt) {
		this.maxEmployeeBeforeTaxDeferralAmt = maxEmployeeBeforeTaxDeferralAmt;
	}

	/**
	 * @return the maxEmployeeRothDeferralsPct
	 */
	public BigDecimal getMaxEmployeeRothDeferralsPct() {
		return maxEmployeeRothDeferralsPct;
	}

	/**
	 * @param maxEmployeeRothDeferralsPct the maxEmployeeRothDeferralsPct to set
	 */
	public void setMaxEmployeeRothDeferralsPct(
			BigDecimal maxEmployeeRothDeferralsPct) {
		this.maxEmployeeRothDeferralsPct = maxEmployeeRothDeferralsPct;
	}

	/**
	 * @return the maxEmployeeRothDeferralsAmt
	 */
	public BigDecimal getMaxEmployeeRothDeferralsAmt() {
		return maxEmployeeRothDeferralsAmt;
	}

	/**
	 * @param maxEmployeeRothDeferralsAmt the maxEmployeeRothDeferralsAmt to set
	 */
	public void setMaxEmployeeRothDeferralsAmt(
			BigDecimal maxEmployeeRothDeferralsAmt) {
		this.maxEmployeeRothDeferralsAmt = maxEmployeeRothDeferralsAmt;
	}

	/**
	 * @return the spdEmployeeContributionRef
	 */
	public String getSpdEmployeeContributionRef() {
		return spdEmployeeContributionRef;
	}

	/**
	 * @param spdEmployeeContributionRef the spdEmployeeContributionRef to set
	 */
	public void setSpdEmployeeContributionRef(String spdEmployeeContributionRef) {
		this.spdEmployeeContributionRef = spdEmployeeContributionRef;
	}

	/**
	 * @return the contirbutionRestirictionOnHardships
	 */
	public Integer getContirbutionRestirictionOnHardships() {
		return contirbutionRestirictionOnHardships;
	}

	/**
	 * @param contirbutionRestirictionOnHardships the contirbutionRestirictionOnHardships to set
	 */
	public void setContirbutionRestirictionOnHardships(
			Integer contirbutionRestirictionOnHardships) {
		this.contirbutionRestirictionOnHardships = contirbutionRestirictionOnHardships;
	}

	/**
	 * @return the planAllowRothDeferrals
	 */
	public String getPlanAllowRothDeferrals() {
		return planAllowRothDeferrals;
	}

	/**
	 * @param planAllowRothDeferrals the planAllowRothDeferrals to set
	 */
	public void setPlanAllowRothDeferrals(String planAllowRothDeferrals) {
		this.planAllowRothDeferrals = planAllowRothDeferrals;
	}

	/**
	 * @return the planAllowsInServiceWithdrawals
	 */
	public String getPlanAllowsInServiceWithdrawals() {
		return planAllowsInServiceWithdrawals;
	}

	/**
	 * @param planAllowsInServiceWithdrawals the planAllowsInServiceWithdrawals to set
	 */
	public void setPlanAllowsInServiceWithdrawals(
			String planAllowsInServiceWithdrawals) {
		this.planAllowsInServiceWithdrawals = planAllowsInServiceWithdrawals;
	}

	/**
	 * @return the contribtionsDistributionComplete
	 */
	public String getContribtionsDistributionComplete() {
		return contribtionsDistributionComplete;
	}

	/**
	 * @param contribtionsDistributionComplete the contribtionsDistributionComplete to set
	 */
	public void setContribtionsDistributionComplete(
			String contribtionsDistributionComplete) {
		this.contribtionsDistributionComplete = contribtionsDistributionComplete;
	}

	/**
	 * @return the automaticContributionDataCompleteInd
	 */
	public String getAutomaticContributionDataCompleteInd() {
		return automaticContributionDataCompleteInd;
	}

	/**
	 * @param automaticContributionDataCompleteInd the automaticContributionDataCompleteInd to set
	 */
	public void setAutomaticContributionDataCompleteInd(
			String automaticContributionDataCompleteInd) {
		this.automaticContributionDataCompleteInd = automaticContributionDataCompleteInd;
	}

	/**
	 * @return the automaticContributionProvisionType
	 */
	public String getAutomaticContributionProvisionType() {
		return automaticContributionProvisionType;
	}

	/**
	 * @param automaticContributionProvisionType the automaticContributionProvisionType to set
	 */
	public void setAutomaticContributionProvisionType(
			String automaticContributionProvisionType) {
		this.automaticContributionProvisionType = automaticContributionProvisionType;
	}

	/**
	 * @return the automaticContributionFeature1
	 */
	public String getAutomaticContributionFeature1() {
		return automaticContributionFeature1;
	}

	/**
	 * @param automaticContributionFeature1 the automaticContributionFeature1 to set
	 */
	public void setAutomaticContributionFeature1(
			String automaticContributionFeature1) {
		this.automaticContributionFeature1 = automaticContributionFeature1;
	}

	/**
	 * @return the automaticContributionFeature2
	 */
	public String getAutomaticContributionFeature2() {
		return automaticContributionFeature2;
	}

	/**
	 * @param automaticContributionFeature2 the automaticContributionFeature2 to set
	 */
	public void setAutomaticContributionFeature2(
			String automaticContributionFeature2) {
		this.automaticContributionFeature2 = automaticContributionFeature2;
	}

	/**
	 * @return the automaticContributionFeature3
	 */
	public String getAutomaticContributionFeature3() {
		return automaticContributionFeature3;
	}

	/**
	 * @param automaticContributionFeature3 the automaticContributionFeature3 to set
	 */
	public void setAutomaticContributionFeature3(
			String automaticContributionFeature3) {
		this.automaticContributionFeature3 = automaticContributionFeature3;
	}

	/**
	 * @return the contributionFeature1Pct
	 */
	public BigDecimal getContributionFeature1Pct() {
		return contributionFeature1Pct;
	}

	/**
	 * @param contributionFeature1Pct the contributionFeature1Pct to set
	 */
	public void setContributionFeature1Pct(BigDecimal contributionFeature1Pct) {
		this.contributionFeature1Pct = contributionFeature1Pct;
	}

	/**
	 * @return the contributionFeature2Date
	 */
	public String getContributionFeature2Date() {
		return contributionFeature2Date;
	}

	/**
	 * @param contributionFeature2Date the contributionFeature2Date to set
	 */
	public void setContributionFeature2Date(String contributionFeature2Date) {
		this.contributionFeature2Date = contributionFeature2Date;
	}

	/**
	 * @return the contributionFeature3SummaryText
	 */
	public String getContributionFeature3SummaryText() {
		return contributionFeature3SummaryText;
	}

	/**
	 * @param contributionFeature3SummaryText the contributionFeature3SummaryText to set
	 */
	public void setContributionFeature3SummaryText(
			String contributionFeature3SummaryText) {
		this.contributionFeature3SummaryText = contributionFeature3SummaryText;
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the deferralContributionsBegin
	 */
	public Integer getDeferralContributionsBegin() {
		return deferralContributionsBegin;
	}

	/**
	 * @param deferralContributionsBegin the deferralContributionsBegin to set
	 */
	public void setDeferralContributionsBegin(Integer deferralContributionsBegin) {
		this.deferralContributionsBegin = deferralContributionsBegin;
	}

	/**
	 * @return the deferralContributionsBeginOtherText
	 */
	public String getDeferralContributionsBeginOtherText() {
		return deferralContributionsBeginOtherText;
	}

	/**
	 * @param deferralContributionsBeginOtherText the deferralContributionsBeginOtherText to set
	 */
	public void setDeferralContributionsBeginOtherText(
			String deferralContributionsBeginOtherText) {
		this.deferralContributionsBeginOtherText = deferralContributionsBeginOtherText;
	}

	/**
	 * @return the aciAllowed
	 */
	public String getAciAllowed() {
		return aciAllowed;
	}

	/**
	 * @param aciAllowed the aciAllowed to set
	 */
	public void setAciAllowed(String aciAllowed) {
		this.aciAllowed = aciAllowed;
	}

	/**
	 * @return the aciApplyDate
	 */
	public String getAciApplyDate() {
		return aciApplyDate;
	}

	/**
	 * @param aciApplyDate the aciApplyDate to set
	 */
	public void setAciApplyDate(String aciApplyDate) {
		this.aciApplyDate = aciApplyDate;
	}

	/**
	 * @return the annualIncrease
	 */
	public BigDecimal getAnnualIncrease() {
		return annualIncrease;
	}

	/**
	 * @param annualIncrease the annualIncrease to set
	 */
	public void setAnnualIncrease(BigDecimal annualIncrease) {
		this.annualIncrease = annualIncrease;
	}

	/**
	 * @return the maxAutomaticIncrease
	 */
	public BigDecimal getMaxAutomaticIncrease() {
		return maxAutomaticIncrease;
	}

	/**
	 * @param maxAutomaticIncrease the maxAutomaticIncrease to set
	 */
	public void setMaxAutomaticIncrease(BigDecimal maxAutomaticIncrease) {
		this.maxAutomaticIncrease = maxAutomaticIncrease;
	}

	/**
	 * @return the automaticContributionDays
	 */
	public String getAutomaticContributionDays() {
		return automaticContributionDays;
	}

	/**
	 * @param automaticContributionDays the automaticContributionDays to set
	 */
	public void setAutomaticContributionDays(String automaticContributionDays) {
		this.automaticContributionDays = automaticContributionDays;
	}

	/**
	 * @return the automaticContributionDaysOther
	 */
	public String getAutomaticContributionDaysOther() {
		return automaticContributionDaysOther;
	}

	/**
	 * @param automaticContributionDaysOther the automaticContributionDaysOther to set
	 */
	public void setAutomaticContributionDaysOther(
			String automaticContributionDaysOther) {
		this.automaticContributionDaysOther = automaticContributionDaysOther;
	}

	/**
	 * @return the employerContributions
	 */
	public String getEmployerContributions() {
		return employerContributions;
	}

	/**
	 * @param employerContributions the employerContributions to set
	 */
	public void setEmployerContributions(String employerContributions) {
		this.employerContributions = employerContributions;
	}

	/**
	 * @return the spdEmployerContributionRef
	 */
	public String getSpdEmployerContributionRef() {
		return spdEmployerContributionRef;
	}

	/**
	 * @param spdEmployerContributionRef the spdEmployerContributionRef to set
	 */
	public void setSpdEmployerContributionRef(String spdEmployerContributionRef) {
		this.spdEmployerContributionRef = spdEmployerContributionRef;
	}

	/**
	 * @return the qACAPlanHasSafeHarborMatchOrNonElective
	 */
	public String getqACAPlanHasSafeHarborMatchOrNonElective() {
		return qACAPlanHasSafeHarborMatchOrNonElective;
	}

	/**
	 * @param qACAPlanHasSafeHarborMatchOrNonElective the qACAPlanHasSafeHarborMatchOrNonElective to set
	 */
	public void setqACAPlanHasSafeHarborMatchOrNonElective(
			String qACAPlanHasSafeHarborMatchOrNonElective) {
		this.qACAPlanHasSafeHarborMatchOrNonElective = qACAPlanHasSafeHarborMatchOrNonElective;
	}

	/**
	 * @return the qACAArrangementOptions
	 */
	public String getqACAArrangementOptions() {
		return qACAArrangementOptions;
	}

	/**
	 * @param qACAArrangementOptions the qACAArrangementOptions to set
	 */
	public void setqACAArrangementOptions(String qACAArrangementOptions) {
		this.qACAArrangementOptions = qACAArrangementOptions;
	}

	/**
	 * @return the qACAMatchContributionContribPct1
	 */
	public BigDecimal getqACAMatchContributionContribPct1() {
		return qACAMatchContributionContribPct1;
	}

	/**
	 * @param qACAMatchContributionContribPct1 the qACAMatchContributionContribPct1 to set
	 */
	public void setqACAMatchContributionContribPct1(
			BigDecimal qACAMatchContributionContribPct1) {
		this.qACAMatchContributionContribPct1 = qACAMatchContributionContribPct1;
	}

	/**
	 * @return the qACAMatchContributionMatchPct1
	 */
	public BigDecimal getqACAMatchContributionMatchPct1() {
		return qACAMatchContributionMatchPct1;
	}

	/**
	 * @param qACAMatchContributionMatchPct1 the qACAMatchContributionMatchPct1 to set
	 */
	public void setqACAMatchContributionMatchPct1(
			BigDecimal qACAMatchContributionMatchPct1) {
		this.qACAMatchContributionMatchPct1 = qACAMatchContributionMatchPct1;
	}

	/**
	 * @return the qACAMatchContributionMatchPct1Value
	 */
	public BigDecimal getqACAMatchContributionMatchPct1Value() {
		return qACAMatchContributionMatchPct1Value;
	}

	/**
	 * @param qACAMatchContributionMatchPct1Value the qACAMatchContributionMatchPct1Value to set
	 */
	public void setqACAMatchContributionMatchPct1Value(
			BigDecimal qACAMatchContributionMatchPct1Value) {
		this.qACAMatchContributionMatchPct1Value = qACAMatchContributionMatchPct1Value;
	}

	/**
	 * @return the qACAMatchContributionContribPct2
	 */
	public BigDecimal getqACAMatchContributionContribPct2() {
		return qACAMatchContributionContribPct2;
	}

	/**
	 * @param qACAMatchContributionContribPct2 the qACAMatchContributionContribPct2 to set
	 */
	public void setqACAMatchContributionContribPct2(
			BigDecimal qACAMatchContributionContribPct2) {
		this.qACAMatchContributionContribPct2 = qACAMatchContributionContribPct2;
	}

	/**
	 * @return the qACAMatchContributionMatchPct2
	 */
	public BigDecimal getqACAMatchContributionMatchPct2() {
		return qACAMatchContributionMatchPct2;
	}

	/**
	 * @param qACAMatchContributionMatchPct2 the qACAMatchContributionMatchPct2 to set
	 */
	public void setqACAMatchContributionMatchPct2(
			BigDecimal qACAMatchContributionMatchPct2) {
		this.qACAMatchContributionMatchPct2 = qACAMatchContributionMatchPct2;
	}

	/**
	 * @return the qACAMatchContributionToAnotherPlan
	 */
	public String getqACAMatchContributionToAnotherPlan() {
		return qACAMatchContributionToAnotherPlan;
	}

	/**
	 * @param qACAMatchContributionToAnotherPlan the qACAMatchContributionToAnotherPlan to set
	 */
	public void setqACAMatchContributionToAnotherPlan(
			String qACAMatchContributionToAnotherPlan) {
		this.qACAMatchContributionToAnotherPlan = qACAMatchContributionToAnotherPlan;
	}

	/**
	 * @return the qACAMatchContributionOtherPlanName
	 */
	public String getqACAMatchContributionOtherPlanName() {
		return qACAMatchContributionOtherPlanName;
	}

	/**
	 * @param qACAMatchContributionOtherPlanName the qACAMatchContributionOtherPlanName to set
	 */
	public void setqACAMatchContributionOtherPlanName(
			String qACAMatchContributionOtherPlanName) {
		this.qACAMatchContributionOtherPlanName = qACAMatchContributionOtherPlanName;
	}

	/**
	 * @return the qACASafeHarborAppliesToRoth
	 */
	public String getqACASafeHarborAppliesToRoth() {
		return qACASafeHarborAppliesToRoth;
	}

	/**
	 * @param qACASafeHarborAppliesToRoth the qACASafeHarborAppliesToRoth to set
	 */
	public void setqACASafeHarborAppliesToRoth(String qACASafeHarborAppliesToRoth) {
		this.qACASafeHarborAppliesToRoth = qACASafeHarborAppliesToRoth;
	}

	/**
	 * @return the qACASHAppliesToCatchUpContributions
	 */
	public String getqACASHAppliesToCatchUpContributions() {
		return qACASHAppliesToCatchUpContributions;
	}

	/**
	 * @param qACASHAppliesToCatchUpContributions the qACASHAppliesToCatchUpContributions to set
	 */
	public void setqACASHAppliesToCatchUpContributions(
			String qACASHAppliesToCatchUpContributions) {
		this.qACASHAppliesToCatchUpContributions = qACASHAppliesToCatchUpContributions;
	}

	/**
	 * @return the qACAAutomaticContributionDays
	 */
	public String getqACAAutomaticContributionDays() {
		return qACAAutomaticContributionDays;
	}

	/**
	 * @param qACAAutomaticContributionDays the qACAAutomaticContributionDays to set
	 */
	public void setqACAAutomaticContributionDays(
			String qACAAutomaticContributionDays) {
		this.qACAAutomaticContributionDays = qACAAutomaticContributionDays;
	}

	/**
	 * @return the qACAAutomaticContributionDaysOther
	 */
	public String getqACAAutomaticContributionDaysOther() {
		return qACAAutomaticContributionDaysOther;
	}

	/**
	 * @param qACAAutomaticContributionDaysOther the qACAAutomaticContributionDaysOther to set
	 */
	public void setqACAAutomaticContributionDaysOther(
			String qACAAutomaticContributionDaysOther) {
		this.qACAAutomaticContributionDaysOther = qACAAutomaticContributionDaysOther;
	}

	/**
	 * @return the qACANonElectiveContributionPct
	 */
	public BigDecimal getqACANonElectiveContributionPct() {
		return qACANonElectiveContributionPct;
	}

	/**
	 * @param qACANonElectiveContributionPct the qACANonElectiveContributionPct to set
	 */
	public void setqACANonElectiveContributionPct(
			BigDecimal qACANonElectiveContributionPct) {
		this.qACANonElectiveContributionPct = qACANonElectiveContributionPct;
	}

	/**
	 * @return the qACANonElectiveAppliesToContrib
	 */
	public String getqACANonElectiveAppliesToContrib() {
		return qACANonElectiveAppliesToContrib;
	}

	/**
	 * @param qACANonElectiveAppliesToContrib the qACANonElectiveAppliesToContrib to set
	 */
	public void setqACANonElectiveAppliesToContrib(
			String qACANonElectiveAppliesToContrib) {
		this.qACANonElectiveAppliesToContrib = qACANonElectiveAppliesToContrib;
	}

	/**
	 * @return the qACANonElectiveContribOtherPlan
	 */
	public String getqACANonElectiveContribOtherPlan() {
		return qACANonElectiveContribOtherPlan;
	}

	/**
	 * @param qACANonElectiveContribOtherPlan the qACANonElectiveContribOtherPlan to set
	 */
	public void setqACANonElectiveContribOtherPlan(
			String qACANonElectiveContribOtherPlan) {
		this.qACANonElectiveContribOtherPlan = qACANonElectiveContribOtherPlan;
	}

	/**
	 * @return the qACASHNonElectivePlanName
	 */
	public String getqACASHNonElectivePlanName() {
		return qACASHNonElectivePlanName;
	}

	/**
	 * @param qACASHNonElectivePlanName the qACASHNonElectivePlanName to set
	 */
	public void setqACASHNonElectivePlanName(String qACASHNonElectivePlanName) {
		this.qACASHNonElectivePlanName = qACASHNonElectivePlanName;
	}

	/**
	 * @return the qACAPlanHasAdditionalEC
	 */
	public String getqACAPlanHasAdditionalEC() {
		return qACAPlanHasAdditionalEC;
	}

	/**
	 * @param qACAPlanHasAdditionalEC the qACAPlanHasAdditionalEC to set
	 */
	public void setqACAPlanHasAdditionalEC(String qACAPlanHasAdditionalEC) {
		this.qACAPlanHasAdditionalEC = qACAPlanHasAdditionalEC;
	}

	/**
	 * @return the qACAPlanHasAdditionalECon
	 */
	public String getqACAPlanHasAdditionalECon() {
		return qACAPlanHasAdditionalECon;
	}

	/**
	 * @param qACAPlanHasAdditionalECon the qACAPlanHasAdditionalECon to set
	 */
	public void setqACAPlanHasAdditionalECon(String qACAPlanHasAdditionalECon) {
		this.qACAPlanHasAdditionalECon = qACAPlanHasAdditionalECon;
	}

	/**
	 * @return the qACASHMatchVesting
	 */
	public String getqACASHMatchVesting() {
		return qACASHMatchVesting;
	}

	/**
	 * @param qACASHMatchVesting the qACASHMatchVesting to set
	 */
	public void setqACASHMatchVesting(String qACASHMatchVesting) {
		this.qACASHMatchVesting = qACASHMatchVesting;
	}

	/**
	 * @return the qACASummaryPlanDesc
	 */
	public String getqACASummaryPlanDesc() {
		return qACASummaryPlanDesc;
	}

	/**
	 * @param qACASummaryPlanDesc the qACASummaryPlanDesc to set
	 */
	public void setqACASummaryPlanDesc(String qACASummaryPlanDesc) {
		this.qACASummaryPlanDesc = qACASummaryPlanDesc;
	}

	/**
	 * @return the qACASHMatchVestingPct1
	 */
	public BigDecimal getqACASHMatchVestingPct1() {
		return qACASHMatchVestingPct1;
	}

	/**
	 * @param qACASHMatchVestingPct1 the qACASHMatchVestingPct1 to set
	 */
	public void setqACASHMatchVestingPct1(BigDecimal qACASHMatchVestingPct1) {
		this.qACASHMatchVestingPct1 = qACASHMatchVestingPct1;
	}

	/**
	 * @return the qACASHMatchVestingPct2
	 */
	public BigDecimal getqACASHMatchVestingPct2() {
		return qACASHMatchVestingPct2;
	}

	/**
	 * @param qACASHMatchVestingPct2 the qACASHMatchVestingPct2 to set
	 */
	public void setqACASHMatchVestingPct2(BigDecimal qACASHMatchVestingPct2) {
		this.qACASHMatchVestingPct2 = qACASHMatchVestingPct2;
	}

	/**
	 * @return the planHasSafeHarborMatchOrNonElective
	 */
	public String getPlanHasSafeHarborMatchOrNonElective() {
		return planHasSafeHarborMatchOrNonElective;
	}

	/**
	 * @param planHasSafeHarborMatchOrNonElective the planHasSafeHarborMatchOrNonElective to set
	 */
	public void setPlanHasSafeHarborMatchOrNonElective(
			String planHasSafeHarborMatchOrNonElective) {
		this.planHasSafeHarborMatchOrNonElective = planHasSafeHarborMatchOrNonElective;
	}

	/**
	 * @return the matchContributionContribPct1
	 */
	public String getMatchContributionContribPct1() {
		return matchContributionContribPct1;
	}

	/**
	 * @param matchContributionContribPct1 the matchContributionContribPct1 to set
	 */
	public void setMatchContributionContribPct1(String matchContributionContribPct1) {
		this.matchContributionContribPct1 = matchContributionContribPct1;
	}

	/**
	 * @return the matchContributionMatchPct1
	 */
	public String getMatchContributionMatchPct1() {
		return matchContributionMatchPct1;
	}

	/**
	 * @param matchContributionMatchPct1 the matchContributionMatchPct1 to set
	 */
	public void setMatchContributionMatchPct1(String matchContributionMatchPct1) {
		this.matchContributionMatchPct1 = matchContributionMatchPct1;
	}

	/**
	 * @return the matchContributionContribPct2
	 */
	public String getMatchContributionContribPct2() {
		return matchContributionContribPct2;
	}

	/**
	 * @param matchContributionContribPct2 the matchContributionContribPct2 to set
	 */
	public void setMatchContributionContribPct2(String matchContributionContribPct2) {
		this.matchContributionContribPct2 = matchContributionContribPct2;
	}

	/**
	 * @return the matchContributionMatchPct2
	 */
	public String getMatchContributionMatchPct2() {
		return matchContributionMatchPct2;
	}

	/**
	 * @param matchContributionMatchPct2 the matchContributionMatchPct2 to set
	 */
	public void setMatchContributionMatchPct2(String matchContributionMatchPct2) {
		this.matchContributionMatchPct2 = matchContributionMatchPct2;
	}

	/**
	 * @return the matchAppliesToContrib
	 */
	public String getMatchAppliesToContrib() {
		return matchAppliesToContrib;
	}

	/**
	 * @param matchAppliesToContrib the matchAppliesToContrib to set
	 */
	public void setMatchAppliesToContrib(String matchAppliesToContrib) {
		this.matchAppliesToContrib = matchAppliesToContrib;
	}

	/**
	 * @return the matchContributionToAnotherPlan
	 */
	public String getMatchContributionToAnotherPlan() {
		return matchContributionToAnotherPlan;
	}

	/**
	 * @param matchContributionToAnotherPlan the matchContributionToAnotherPlan to set
	 */
	public void setMatchContributionToAnotherPlan(
			String matchContributionToAnotherPlan) {
		this.matchContributionToAnotherPlan = matchContributionToAnotherPlan;
	}

	/**
	 * @return the matchContributionOtherPlanName
	 */
	public String getMatchContributionOtherPlanName() {
		return matchContributionOtherPlanName;
	}

	/**
	 * @param matchContributionOtherPlanName the matchContributionOtherPlanName to set
	 */
	public void setMatchContributionOtherPlanName(
			String matchContributionOtherPlanName) {
		this.matchContributionOtherPlanName = matchContributionOtherPlanName;
	}

	/**
	 * @return the safeHarborAppliesToRoth
	 */
	public String getSafeHarborAppliesToRoth() {
		return safeHarborAppliesToRoth;
	}

	/**
	 * @param safeHarborAppliesToRoth the safeHarborAppliesToRoth to set
	 */
	public void setSafeHarborAppliesToRoth(String safeHarborAppliesToRoth) {
		this.safeHarborAppliesToRoth = safeHarborAppliesToRoth;
	}

	/**
	 * @return the sHAppliesToCatchUpContributions
	 */
	public String getsHAppliesToCatchUpContributions() {
		return sHAppliesToCatchUpContributions;
	}

	/**
	 * @param sHAppliesToCatchUpContributions the sHAppliesToCatchUpContributions to set
	 */
	public void setsHAppliesToCatchUpContributions(
			String sHAppliesToCatchUpContributions) {
		this.sHAppliesToCatchUpContributions = sHAppliesToCatchUpContributions;
	}

	/**
	 * @return the nonElectiveContribOption
	 */
	public String getNonElectiveContribOption() {
		return nonElectiveContribOption;
	}

	/**
	 * @param nonElectiveContribOption the nonElectiveContribOption to set
	 */
	public void setNonElectiveContribOption(String nonElectiveContribOption) {
		this.nonElectiveContribOption = nonElectiveContribOption;
	}

	/**
	 * @return the nonElectiveContributionPct
	 */
	public String getNonElectiveContributionPct() {
		return nonElectiveContributionPct;
	}

	/**
	 * @param nonElectiveContributionPct the nonElectiveContributionPct to set
	 */
	public void setNonElectiveContributionPct(String nonElectiveContributionPct) {
		this.nonElectiveContributionPct = nonElectiveContributionPct;
	}

	/**
	 * @return the nonElectiveAppliesToContrib
	 */
	public String getNonElectiveAppliesToContrib() {
		return nonElectiveAppliesToContrib;
	}

	/**
	 * @param nonElectiveAppliesToContrib the nonElectiveAppliesToContrib to set
	 */
	public void setNonElectiveAppliesToContrib(String nonElectiveAppliesToContrib) {
		this.nonElectiveAppliesToContrib = nonElectiveAppliesToContrib;
	}

	/**
	 * @return the nonElectiveContribOtherPlan
	 */
	public String getNonElectiveContribOtherPlan() {
		return nonElectiveContribOtherPlan;
	}

	/**
	 * @param nonElectiveContribOtherPlan the nonElectiveContribOtherPlan to set
	 */
	public void setNonElectiveContribOtherPlan(String nonElectiveContribOtherPlan) {
		this.nonElectiveContribOtherPlan = nonElectiveContribOtherPlan;
	}

	/**
	 * @return the sHNonElectivePlanName
	 */
	public String getSHNonElectivePlanName() {
		return SHNonElectivePlanName;
	}

	/**
	 * @param sHNonElectivePlanName the sHNonElectivePlanName to set
	 */
	public void setSHNonElectivePlanName(String sHNonElectivePlanName) {
		SHNonElectivePlanName = sHNonElectivePlanName;
	}

	/**
	 * @return the planHasAdditionalEC
	 */
	public String getPlanHasAdditionalEC() {
		return planHasAdditionalEC;
	}

	/**
	 * @param planHasAdditionalEC the planHasAdditionalEC to set
	 */
	public void setPlanHasAdditionalEC(String planHasAdditionalEC) {
		this.planHasAdditionalEC = planHasAdditionalEC;
	}

	/**
	 * @return the summaryPlanDesc
	 */
	public String getSummaryPlanDesc() {
		return summaryPlanDesc;
	}

	/**
	 * @param summaryPlanDesc the summaryPlanDesc to set
	 */
	public void setSummaryPlanDesc(String summaryPlanDesc) {
		this.summaryPlanDesc = summaryPlanDesc;
	}

	/**
	 * @return the excludedMoneyTypename
	 */
	public String[] getExcludedMoneyTypename() {
		return excludedMoneyTypename;
	}

	/**
	 * @param excludedMoneyTypename the excludedMoneyTypename to set
	 */
	public void setExcludedMoneyTypename(String[] excludedMoneyTypename) {
		this.excludedMoneyTypename = excludedMoneyTypename;
	}

	/**
	 * @return the excludeCount
	 */
	public int getExcludeCount() {
		return excludeCount;
	}

	/**
	 * @param excludeCount the excludeCount to set
	 */
	public void setExcludeCount(int excludeCount) {
		this.excludeCount = excludeCount;
	}

	/**
	 * @return the pendingPIFCompletion
	 */
	public boolean isPendingPIFCompletion() {
		return pendingPIFCompletion;
	}

	/**
	 * @param pendingPIFCompletion the pendingPIFCompletion to set
	 */
	public void setPendingPIFCompletion(boolean pendingPIFCompletion) {
		this.pendingPIFCompletion = pendingPIFCompletion;
	}

	/**
	 * @return the dIOisQDIA
	 */
	public String getdIOisQDIA() {
		return dIOisQDIA;
	}

	/**
	 * @param dIOisQDIA the dIOisQDIA to set
	 */
	public void setdIOisQDIA(String dIOisQDIA) {
		this.dIOisQDIA = dIOisQDIA;
	}

	/**
	 * @return the qDIAFeeRestrictionOnTransferOutDays
	 */
	public String getqDIAFeeRestrictionOnTransferOutDays() {
		return qDIAFeeRestrictionOnTransferOutDays;
	}

	/**
	 * @param qDIAFeeRestrictionOnTransferOutDays the qDIAFeeRestrictionOnTransferOutDays to set
	 */
	public void setqDIAFeeRestrictionOnTransferOutDays(
			String qDIAFeeRestrictionOnTransferOutDays) {
		this.qDIAFeeRestrictionOnTransferOutDays = qDIAFeeRestrictionOnTransferOutDays;
	}

	/**
	 * @return the moneyTypeExcludeObject
	 */
	public Collection<MoneyTypeExcludeObject> getMoneyTypeExcludeObject() {
		return moneyTypeExcludeObject;
	}

	/**
	 * @param moneyTypeExcludeObject the moneyTypeExcludeObject to set
	 */
	public void setMoneyTypeExcludeObject(
			Collection<MoneyTypeExcludeObject> moneyTypeExcludeObject) {
		this.moneyTypeExcludeObject = moneyTypeExcludeObject;
	}

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
	
	public String getEnablePlanYearEndDateAndPercentageComp() {
		return enablePlanYearEndDateAndPercentageComp;
	}

	public void setEnablePlanYearEndDateAndPercentageComp(
			String enablePlanYearEndDateAndPercentageComp) {
		this.enablePlanYearEndDateAndPercentageComp = enablePlanYearEndDateAndPercentageComp;
	}
	
	public String getContributionApplicableToPlanDate() {
		return contributionApplicableToPlanDate;
	}

	public void setContributionApplicableToPlanDate(
			String contributionApplicableToPlanDate) {
		this.contributionApplicableToPlanDate = contributionApplicableToPlanDate;
	}
	
	public String getAcaAnnualIncreaseType() {
		return acaAnnualIncreaseType;
	}

	public void setAcaAnnualIncreaseType(String acaAnnualIncreaseType) {
		this.acaAnnualIncreaseType = acaAnnualIncreaseType;
	}

	public BigDecimal getContributionApplicableToPlanPct() {
		return contributionApplicableToPlanPct;
	}

	public void setContributionApplicableToPlanPct(
			BigDecimal contributionApplicableToPlanPct) {
		this.contributionApplicableToPlanPct = contributionApplicableToPlanPct;
	}

	public String getEacaPlanHasAutoContribWD() {
		return eacaPlanHasAutoContribWD;
	}

	public void setEacaPlanHasAutoContribWD(String eacaPlanHasAutoContribWD) {
		this.eacaPlanHasAutoContribWD = eacaPlanHasAutoContribWD;
	}

	public boolean isEligibleNotice() {
		return eligibleNotice;
	}

	public void setEligibleNotice(boolean eligibleNotice) {
		this.eligibleNotice = eligibleNotice;
	}

	public Boolean getDeferralIrsApplies() {
		return deferralIrsApplies;
	}

	public void setDeferralIrsApplies(Boolean deferralIrsApplies) {
		this.deferralIrsApplies = deferralIrsApplies;
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

	public List getMatchAppliesToContribList() {
		return matchAppliesToContribList;
	}

	public void setMatchAppliesToContribList(List matchAppliesToContribList) {
		this.matchAppliesToContribList = matchAppliesToContribList;
	}
	
   
}