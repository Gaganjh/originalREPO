package com.manulife.pension.ps.web.ipitool;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.list.GrowthList;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSummaryField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;

public class IpiToolForm extends AutoForm {
    
    private static final long serialVersionUID = 1L;
    
    private static final Map<String, IpiToolField> IPI_TOOL_FIELD_MAP;
    
    private String assetChargeRm;
    private String otherChargeRm;
    private String detailedParticipantStmtRm;
    
    private String contractStmtRm;
    
    private String blendedAssetCharge;
    private String monthlyParticipantFee;
    private String annualizedParticipantFee;
    private String marketValueEqualzer;
    private String conditionalRecordkeepingFee;
    private String flatAssetChargeAdjustment;
    private String legacyAdjustments;
    private String contractLevelDetailedStatementFee;
    private String discontinuanceFee;
    
    private String depositBasedService;
    private String depositBased2ndYearRecurring;
    private String abSerc;
    private String assetBased2ndYearRecurring;
    private String assetBasedAllYear;
    private String monthlyTPAParticipantFee;
    private String annualizedTPAAssetBasedFee;
    private String annualizedTPAFee;
    private String annualizedRIAFee;
    private String annualizedTrusteeFee;
    private String myAction = StringUtils.EMPTY;
    private boolean isBacChanged;
    private boolean isDiChanged;
    private String dioDiscount;
    


    static {
        
        final HashMap<String, IpiToolField> innerMap = new HashMap<String, IpiToolField>();
        reverseMap(innerMap, IpiToolRecoveryMethodField.values());
        reverseMap(innerMap, IpiToolJhChargeField.values());
        reverseMap(innerMap, IpiToolSalesAndServiceChargeField.values());
        reverseMap(innerMap, IpiToolTrusteeChargesField.values());
        reverseMap(innerMap, IpiToolSummaryField.values());
        reverseMap(innerMap, IpiToolStatementTypeField.values());
        IPI_TOOL_FIELD_MAP = Collections.unmodifiableMap(innerMap);
        
    }
    
    private static void reverseMap(Map<String, IpiToolField> map, IpiToolField[] componentSpec) {
        
        for (IpiToolField field : componentSpec) {
            IpiToolField previousField = map.put(field.getFieldSpec().getFormName(), field);
            if (previousField != null) {
                throw new IllegalStateException(
                        "Two mappings for '" + field.getFieldSpec().getFormName() +
                        "': " + previousField.toString() +
                        " and " + field.toString());
            }
        }
        
    }
    
    private final Map<IpiToolField<?>, String> currentFieldValues = new HashMap<IpiToolField<?>, String>();
    private final Map<IpiToolField<?>, String> inputFieldValues = new HashMap<IpiToolField<?>, String>();
    
    private final List currentBacScale = 
    	LazyList.decorate(new GrowthList(), new BacFactory());
    
	private final List inputBacScale =
        LazyList.decorate(new GrowthList(), new BacFactory());
        
        
    private final List currentDiScale = new GrowthList();
    private final List inputDiScale = new GrowthList();
    
    public Map<IpiToolField<?>, String> getCurrentFieldValueMap() { return Collections.unmodifiableMap(currentFieldValues); }
    public Map<IpiToolField<?>, String> getInputFieldValueMap() { return Collections.unmodifiableMap(inputFieldValues); }
    public List getCurrentBacScale() { return currentBacScale; }
    public List getInputBacScale() { return inputBacScale; }
    public List getCurrentDiScale() { return currentDiScale; }
    public List getInputDiScale() { return inputDiScale; }
    
    public void setCurrentFieldValue(String fieldName, String value) {
        if (! StringUtils.isBlank(value)) {
            currentFieldValues.put(IPI_TOOL_FIELD_MAP.get(fieldName), value);
        }
    }
    public String getCurrentFieldValue(String fieldName) { return currentFieldValues.get(IPI_TOOL_FIELD_MAP.get(fieldName)); }

    public void setInputFieldValue(String fieldName, String value) {
            inputFieldValues.put(IPI_TOOL_FIELD_MAP.get(fieldName), value);
    }
    public String getInputFieldValue(String fieldName) { return inputFieldValues.get(IPI_TOOL_FIELD_MAP.get(fieldName)); }
    
    public void setCurrentBacScaleLine(int index, BasicAssetChargeLine bacLine) { currentBacScale.set(index,  bacLine); }
    public BasicAssetChargeLine getCurrentBacScaleLine(int index) { return (BasicAssetChargeLine) currentBacScale.get(index); }
    
    public void setInputBacScaleLine(int index, BasicAssetChargeLine bacLine) { inputBacScale.set(index,  bacLine); }
    public BasicAssetChargeLine getInputBacScaleLine(int index) { return (BasicAssetChargeLine) inputBacScale.get(index); }
    
    public void setCurrentDiScaleLine(int index, String charge) { currentDiScale.set(index,  charge); }
    public String getCurrentDiScaleLine(int index) { return (String) currentDiScale.get(index); }
    
    public void setInputDiScaleLine(int index, String charge) { inputDiScale.set(index,  charge); }
    public String getInputDiScaleLine(int index) { return (String) inputDiScale.get(index); }
    
    
    
	public String getAssetChargeRm() {
		return assetChargeRm;
	}
	public void setAssetChargeRm(String assetChargeRm) {
		this.assetChargeRm = assetChargeRm;
		setInputFieldValue("assetChargeRm",assetChargeRm);
	}
	public String getOtherChargeRm() {
		return otherChargeRm;
	}
	public void setOtherChargeRm(String otherChargeRm) {
		this.otherChargeRm = otherChargeRm;
		setInputFieldValue("otherChargeRm",otherChargeRm);
	}
	public String getDetailedParticipantStmtRm() {
		return detailedParticipantStmtRm;
	}
	public void setDetailedParticipantStmtRm(String detailedParticipantStmtRm) {
		this.detailedParticipantStmtRm = detailedParticipantStmtRm;
		setInputFieldValue("detailedParticipantStmtRm",detailedParticipantStmtRm);
	}
	public String getContractStmtRm() {
		return contractStmtRm;
	}
	public void setContractStmtRm(String contractStmtRm) {
		this.contractStmtRm = contractStmtRm;
		setInputFieldValue("contractStmtRm",contractStmtRm);
	}
	public String getBlendedAssetCharge() {
		return blendedAssetCharge;
	}
	public void setBlendedAssetCharge(String blendedAssetCharge) {
		this.blendedAssetCharge = blendedAssetCharge;
		setInputFieldValue("blendedAssetCharge",blendedAssetCharge);
	}
	public String getMonthlyParticipantFee() {
		return monthlyParticipantFee;
	}
	public void setMonthlyParticipantFee(String monthlyParticipantFee) {
		this.monthlyParticipantFee = monthlyParticipantFee;
		setInputFieldValue("monthlyParticipantFee",monthlyParticipantFee);
	}
	public String getAnnualizedParticipantFee() {
		return annualizedParticipantFee;
	}
	public void setAnnualizedParticipantFee(String annualizedParticipantFee) {
		this.annualizedParticipantFee = annualizedParticipantFee;
		setInputFieldValue("annualizedParticipantFee",annualizedParticipantFee);
	}
	public String getMarketValueEqualzer() {
		return marketValueEqualzer;
	}
	public void setMarketValueEqualzer(String marketValueEqualzer) {
		this.marketValueEqualzer = marketValueEqualzer;
		setInputFieldValue("marketValueEqualzer",marketValueEqualzer);
	}
	public String getConditionalRecordkeepingFee() {
		return conditionalRecordkeepingFee;
	}
	public void setConditionalRecordkeepingFee(String conditionalRecordkeepingFee) {
		this.conditionalRecordkeepingFee = conditionalRecordkeepingFee;
		setInputFieldValue("conditionalRecordkeepingFee",conditionalRecordkeepingFee);
	}
	public String getFlatAssetChargeAdjustment() {
		return flatAssetChargeAdjustment;
	}
	public void setFlatAssetChargeAdjustment(String flatAssetChargeAdjustment) {
		this.flatAssetChargeAdjustment = flatAssetChargeAdjustment;
		setInputFieldValue("flatAssetChargeAdjustment",flatAssetChargeAdjustment);
	}
	public String getLegacyAdjustments() {
		return legacyAdjustments;
	}
	public void setLegacyAdjustments(String legacyAdjustments) {
		this.legacyAdjustments = legacyAdjustments;
		setInputFieldValue("legacyAdjustments",legacyAdjustments);
	}
	public String getContractLevelDetailedStatementFee() {
		return contractLevelDetailedStatementFee;
	}
	public void setContractLevelDetailedStatementFee(
			String contractLevelDetailedStatementFee) {
		this.contractLevelDetailedStatementFee = contractLevelDetailedStatementFee;
		setInputFieldValue("contractLevelDetailedStatementFee",contractLevelDetailedStatementFee);
	}
	
	public String getDiscontinuanceFee() {
		return discontinuanceFee;
	}
	public void setDiscontinuanceFee(String discontinuanceFee) {
		this.discontinuanceFee = discontinuanceFee;
		setInputFieldValue("discontinuanceFee",discontinuanceFee);
	}
	public String getDepositBasedService() {
		return depositBasedService;
	}
	public void setDepositBasedService(String depositBasedService) {
		this.depositBasedService = depositBasedService;
		setInputFieldValue("depositBasedService",depositBasedService);
	}
	public String getDepositBased2ndYearRecurring() {
		return depositBased2ndYearRecurring;
	}
	public void setDepositBased2ndYearRecurring(String depositBased2ndYearRecurring) {
		this.depositBased2ndYearRecurring = depositBased2ndYearRecurring;
		setInputFieldValue("depositBased2ndYearRecurring",depositBased2ndYearRecurring);
	}
	public String getAbSerc() {
		return abSerc;
	}
	public void setAbSerc(String abSerc) {
		this.abSerc = abSerc;
		setInputFieldValue("abSerc",abSerc);
	}
	public String getAssetBased2ndYearRecurring() {
		return assetBased2ndYearRecurring;
	}
	public void setAssetBased2ndYearRecurring(String assetBased2ndYearRecurring) {
		this.assetBased2ndYearRecurring = assetBased2ndYearRecurring;
		setInputFieldValue("assetBased2ndYearRecurring",assetBased2ndYearRecurring);
	}
	public String getAssetBasedAllYear() {
		return assetBasedAllYear;
	}
	public void setAssetBasedAllYear(String assetBasedAllYear) {
		this.assetBasedAllYear = assetBasedAllYear;
		setInputFieldValue("assetBasedAllYear",assetBasedAllYear);
	}
	public String getMonthlyTPAParticipantFee() {
		return monthlyTPAParticipantFee;
	}
	public void setMonthlyTPAParticipantFee(String monthlyTPAParticipantFee) {
		this.monthlyTPAParticipantFee = monthlyTPAParticipantFee;
		setInputFieldValue("monthlyTPAParticipantFee",monthlyTPAParticipantFee);
	}
	public String getAnnualizedTPAAssetBasedFee() {
		return annualizedTPAAssetBasedFee;
	}
	public void setAnnualizedTPAAssetBasedFee(String annualizedTPAAssetBasedFee) {
		this.annualizedTPAAssetBasedFee = annualizedTPAAssetBasedFee;
		setInputFieldValue("annualizedTPAAssetBasedFee",annualizedTPAAssetBasedFee);
	}
	public String getAnnualizedTPAFee() {
		return annualizedTPAFee;
	}
	public void setAnnualizedTPAFee(String annualizedTPAFee) {
		this.annualizedTPAFee = annualizedTPAFee;
		setInputFieldValue("annualizedTPAFee",annualizedTPAFee);
	}
	public String getAnnualizedRIAFee() {
		return annualizedRIAFee;
	}
	public void setAnnualizedRIAFee(String annualizedRIAFee) {
		this.annualizedRIAFee = annualizedRIAFee;
		setInputFieldValue("annualizedRIAFee",annualizedRIAFee);
	}
	public String getAnnualizedTrusteeFee() {
		return annualizedTrusteeFee;
	}
	public void setAnnualizedTrusteeFee(String annualizedTrusteeFee) {
		this.annualizedTrusteeFee = annualizedTrusteeFee;
		setInputFieldValue("annualizedTrusteeFee",annualizedTrusteeFee);
	}
	public String getMyAction() {
		return myAction;
	}
	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}
	public boolean isBacChanged() {
		return isBacChanged;
	}
	public void setBacChanged(boolean isBacChanged) {
		this.isBacChanged = isBacChanged;
	}
	public boolean isDiChanged() {
		return isDiChanged;
	}
	public void setDiChanged(boolean isDiChanged) {
		this.isDiChanged = isDiChanged;
	}

   public String getDioDiscount() {
        return dioDiscount;
    }
    public void setDioDiscount(String dioDiscount) {
        this.dioDiscount = dioDiscount;
    }
    
}
