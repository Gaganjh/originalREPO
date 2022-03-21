package com.manulife.pension.ps.web.pif;

import java.util.Map;

import com.manulife.pension.ps.web.controller.PsAutoActionLabelForm;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * PIFDataForm is submitted by the user and contains data for making 
 * an Plan Information action plan
 * @author 	rajenra
 */
@SuppressWarnings("unchecked")
public class PIFDataForm extends PsAutoActionLabelForm {

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

    private String resetDeclarations;
    private String dirty = "false";

    private String fromTab = null;
    private String toTab = null;
    private String selectedTab = null;
    
    private Integer contractId = null;
	private Integer submissionId = null;
    
    // The value object - use fields directly for types that don't need conversion.
    private PIFDataUi pifDataUi;
    
    private PlanInfoVO initialPlanInfoVO;
    
    private PlanInfoVO planInfoVO  = null;
    // TPA details
	private String tpaFirmName;
	private Integer tpaFirmId;
	private String contractName;
	private String acknowledgmentText;
	
	/**
     * Default constructor.
     */
    public PIFDataForm() {
        super();
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
	 * @return the pifDataUi
	 */
	public PIFDataUi getPifDataUi() {
		return pifDataUi;
	}

	/**
	 * @param pifDataUi the pifDataUi to set
	 */
	public void setPifDataUi(PIFDataUi pifDataUi) {
		this.pifDataUi = pifDataUi;
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
	 * @return the submissionId
	 */
	public Integer getSubmissionId() {
		return submissionId;
	}

	/**
	 * @param submissionId the submissionId to set
	 */
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	
	/**
	 * @return the initialPlanInfoVO
	 */
	public PlanInfoVO getInitialPlanInfoVO() {
		return initialPlanInfoVO;
	}

	/**
	 * @param initialPlanInfoVO the initialPlanInfoVO to set
	 */
	public void setInitialPlanInfoVO(PlanInfoVO initialPlanInfoVO) {
		this.initialPlanInfoVO = initialPlanInfoVO;
	}

	/**
	 * @return the planInfoVO
	 */
	public PlanInfoVO getPlanInfoVO() {
		return planInfoVO;
	}

	/**
	 * @param planInfoVO the planInfoVO to set
	 */
	public void setPlanInfoVO(PlanInfoVO planInfoVO) {
		this.planInfoVO = planInfoVO;
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

}