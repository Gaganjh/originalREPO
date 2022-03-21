package com.manulife.pension.ps.web.contract;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;
import com.manulife.pension.ps.web.plan.MoneyTypeEligibilityCriterionUi;
import com.manulife.pension.ps.web.plan.MoneyTypeExcludedEmployeeUi;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.WithdrawalDistributionMethod;

public class PlanDataForm extends PsAutoActionLabelForm {

    public static final String RESET_CHECKBOXES = "reset_checkboxes";

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Modes for mode specific functions.
     */
    private enum Mode {
        VIEW, EDIT, CONFIRM;
    };

    private Mode mode = Mode.VIEW;

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(PlanDataForm.class);

    // The value object - use fields directly for types that don't need conversion.
    private PlanDataUi planDataUi;

    // The value object that will be saved
    private PlanDataUi modifiedPlanDataUi;

    // This contains the data for the dropdown lists.
    // Each list in the map is keyed from constants in CodeLookupCache.
    private Map lookupData;

    private String resetDeclarations;

    private boolean userCanEdit = false;

    private boolean userCanViewHistory = false;

    private String dirty = "false";
    private boolean allowOnlineLoans = false;
    
    private boolean installmentsIndicator = false; 
    
    
   

	/**
     * Default constructor.
     */
    public PlanDataForm() {
        super();
    }

    /**
     * @return the planDataUi
     */
    public PlanDataUi getPlanDataUi() {
        return planDataUi;
    }

    /**
     * @param planDataUi the planDataUi to set
     */
    public void setPlanDataUi(PlanDataUi planDataUi) {
        this.planDataUi = planDataUi;
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
     * @return the modifiedPlanDataUi
     */
    public PlanDataUi getModifiedPlanDataUi() {
        return modifiedPlanDataUi;
    }

    /**
     * @param modifiedPlanDataUi the modifiedPlanDataUi to set
     */
    public void setModifiedPlanDataUi(PlanDataUi modifiedPlanDataUi) {
        this.modifiedPlanDataUi = modifiedPlanDataUi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset( final HttpServletRequest httpServletRequest) {

        final String resetCheckBoxes = httpServletRequest.getParameter(RESET_CHECKBOXES);
        if (!StringUtils.isEmpty(resetCheckBoxes)) {
            // Reset the various checkboxes
            if (planDataUi != null) {
                planDataUi.setSelectedWithdrawalReasons(ArrayUtils.EMPTY_STRING_ARRAY);
                planDataUi
                        .setSelectedAllowableMoneyTypesForWithdrawals(ArrayUtils.EMPTY_STRING_ARRAY);
                planDataUi.setSelectedAllowableMoneyTypesForLoans(ArrayUtils.EMPTY_STRING_ARRAY);
                planDataUi.setSelectedUnvestedMoneyOptions(ArrayUtils.EMPTY_STRING_ARRAY);
                planDataUi.getPlanData().setAllowMandatoryDistributions(false);
                planDataUi.getPlanData().setAllowQualifiedBirthOrAdoptionDistribution(false);
                planDataUi.getPlanData().getWithdrawalDistributionMethod().setLumpSumIndicator(false);
                planDataUi.getPlanData().getWithdrawalDistributionMethod().setInstallmentsIndicator(false);
                planDataUi.getPlanData().getWithdrawalDistributionMethod().setAnnuityIndicator(false);
                planDataUi.getPlanData().getWithdrawalDistributionMethod().setPartialWithdrawalIndicator(false);
                planDataUi.getPlanData().getWithdrawalDistributionMethod().setOtherIndicator(false);
                
                for (MoneyTypeEligibilityCriterionUi criterion: planDataUi.getMoneyTypeEligibilityCriteria()) {
                    criterion.setImmediateEligibilityIndicator(false);
                    criterion.setPartTimeEligibilityIndicator(false);
                }
                for (MoneyTypeExcludedEmployeeUi excludedEmployee: planDataUi.getMoneyTypeExcludedEmployees()) {
                    excludedEmployee.setUnion(false);
                    excludedEmployee.setNonResidentAliens(false);
                    excludedEmployee.setHighlyCompensated(false);
                    excludedEmployee.setLeased(false);
                    excludedEmployee.setOther(false);
                } 
             }
        }
        super.reset( httpServletRequest);
    }

    /**
     * @return the resetDeclarations
     */
    public String getResetDeclarations() {
        return resetDeclarations;
    }

    /**
     * @param resetDeclarations the resetDeclarations to set
     */
    public void setResetDeclarations(String resetDeclarations) {
        this.resetDeclarations = resetDeclarations;
    }

    /**
     * Sets the mode to be view.
     */
    public void setViewMode() {
        mode = Mode.VIEW;
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
     * Queries if the mode is view.
     */
    public boolean isViewMode() {
        return mode == Mode.VIEW;
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
     * @return the userCanEdit
     */
    public boolean getUserCanEdit() {
        return userCanEdit;
    }

    /**
     * @param userCanEdit the userCanEdit to set
     */
    public void setUserCanEdit(final boolean userCanEdit) {
        this.userCanEdit = userCanEdit;
    }

    /**
     * @return the userCanViewHistory
     */
    public boolean getUserCanViewHistory() {
        return userCanViewHistory;
    }

    /**
     * @param userCanViewHistory the userCanViewHistory to set
     */
    public void setUserCanViewHistory(final boolean userCanViewHistory) {
        this.userCanViewHistory = userCanViewHistory;
    }
    
    /**
     * @return returns "true" if the form is dirty
     */
    public String getDirty() {
        return dirty;
    }

    /**
     * @param dirty
     */
    public void setDirty(final String dirty) {
        this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString() : Boolean.FALSE
                .toString();
    }

	public boolean isAllowOnlineLoans() {
		return allowOnlineLoans;
	}

	public void setAllowOnlineLoans(boolean allowOnlineLoans) {
		this.allowOnlineLoans = allowOnlineLoans;
	}

	public boolean isInstallmentsIndicator() {
		return installmentsIndicator;
	}

	public void setInstallmentsIndicator(boolean installmentsIndicator) {
		this.installmentsIndicator = installmentsIndicator;
	}
}
