package com.manulife.pension.ps.web.census;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.employee.valueobject.ApplyLTPTCreditingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeHireDateInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingExplanation;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.pension.service.vesting.VestingRetriever;
import com.manulife.pension.service.vesting.util.PlanYear;

/**
 * Vesting information holds all the plan level and employee level vesting information.
 * 
 * @author guweigu
 */
public class VestingInformation implements Serializable {

    public static final String CREDITING_METHOD_HOURS_OF_SERVICE = VestingConstants.
    CreditingMethod.HOURS_OF_SERVICE;
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");

	private static final Map<String, String> creditingMethodDescriptions = new HashMap<String, String>();
	static {
		creditingMethodDescriptions.put(
				VestingConstants.CreditingMethod.HOURS_OF_SERVICE,
				"Hours of service");
		creditingMethodDescriptions.put(
				VestingConstants.CreditingMethod.ELAPSED_TIME, "Elapsed time");
		creditingMethodDescriptions.put(
				VestingConstants.CreditingMethod.UNSPECIFIED, "Unspecified");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private String employeeFirstName;

	private String employeeLastName;

	private String employeeMiddleInit;

	private String employeeSSN;
    
	private Date asOfDate;
    
	private EmployeeVestingInformation employeeVestingInformation;

    private EmployeeHireDateInfo employeeHireDateInfo;
    
    private Map<VestingType,EmployeeVestingInfo> vestingParamInfo = 
            new HashMap<VestingType,EmployeeVestingInfo>();
    
    private Map<Integer, VestingInputDescription> vestingEffectiveInput =
            new HashMap<Integer, VestingInputDescription>();

	private List<MoneyTypeVO> moneyTypes;

	private boolean fullyVestedOnRetirement;
	private boolean fullyVestedOnEarlyRetirement;
	private boolean fullyVestedOnDisability;
	private boolean fullyVestedOnDeath;
	
    private Integer planHoursOfService;
       
	private Collection<VestingSchedule> vestingSchedules = new ArrayList<VestingSchedule>();

    private Boolean partialParticipantStatus;
    
    private Date planLastUpdatedDate;
    
    private List<Integer> explanation;

    private ApplyLTPTCreditingInfo applyLTPTCreditingInfo;

    public List<Integer> getExplanation() {
        return explanation;
    }

    public void setExplanation(List<Integer> explanation) {
        this.explanation = explanation;
    }

    /**
     * @return the vestingSchedules
     */
    public Collection<VestingSchedule> getVestingSchedules() {
        return vestingSchedules;
    }

    /**
     * @param vestingSchedules the vestingSchedules to set
     */
    public void setVestingSchedules(final Collection<VestingSchedule> vestingSchedules) {
        this.vestingSchedules = vestingSchedules;
    }

	public EmployeeVestingInformation getEmployeeVestingInformation() {
		return employeeVestingInformation;
	}

	public void setEmployeeVestingInformation(
			EmployeeVestingInformation employeeVestingInformation) {
		this.employeeVestingInformation = employeeVestingInformation;
	}

	public List<MoneyTypeVO> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<MoneyTypeVO> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeMiddleInit() {
		return employeeMiddleInit;
	}

	public void setEmployeeMiddleInit(String employeeMiddleInit) {
		this.employeeMiddleInit = employeeMiddleInit;
	}

	public String getEmployeeSSN() {
		return employeeSSN;
	}

	public void setEmployeeSSN(String employeeSSN) {
		this.employeeSSN = employeeSSN;
	}

	public String getCreditingMethodDescription() {
		if (employeeVestingInformation == null || employeeVestingInformation.getRetrievalDetails()== null) {
			return "";
		} else {
			return creditingMethodDescriptions.get(employeeVestingInformation
					.getRetrievalDetails().getCreditingMethod());
		}
	}

	public boolean isFullyVestedOnDeath() {
		return fullyVestedOnDeath;
	}

	public void setFullyVestedOnDeath(Boolean fullyVestedOnDeath) {
		this.fullyVestedOnDeath = fullyVestedOnDeath == null ? false : fullyVestedOnDeath;
	}

	public boolean isFullyVestedOnDisability() {
		return fullyVestedOnDisability;
	}

	public void setFullyVestedOnDisability(Boolean fullyVestedOnDisability) {
		this.fullyVestedOnDisability = fullyVestedOnDisability == null ? false : fullyVestedOnDisability;
	}

	public boolean isFullyVestedOnEarlyRetirement() {
		return fullyVestedOnEarlyRetirement;
	}

	public void setFullyVestedOnEarlyRetirement(Boolean fullyVestedOnEarlyRetirement) {
		this.fullyVestedOnEarlyRetirement = fullyVestedOnEarlyRetirement == null ? false : fullyVestedOnEarlyRetirement;
	}

	public boolean isFullyVestedOnRetirement() {
		return fullyVestedOnRetirement;
	}

	public void setFullyVestedOnRetirement(Boolean fullyVestedOnRetirement) {
		this.fullyVestedOnRetirement = fullyVestedOnRetirement == null ? false : fullyVestedOnRetirement;
	}

    public Boolean getPartialParticipantStatus() {
        return partialParticipantStatus;
    }

    public void setPartialParticipantStatus(Boolean partialParticipantStatus) {
        this.partialParticipantStatus = partialParticipantStatus;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Map getVestingParamInfo() {
        return vestingParamInfo;
    }

    public void setVestingParamInfo(Map vestingParamInfo) {
        this.vestingParamInfo = vestingParamInfo;
    }

    /**
     * @return Integer - The planHoursOfService.
     */
    public Integer getPlanHoursOfService() {
        return planHoursOfService;
    }

    /**
     * @param planHoursOfService - The planHoursOfService to set.
     */
    public void setPlanHoursOfService(final Integer planHoursOfService) {
        this.planHoursOfService = planHoursOfService;
    }

    /**
     * @return - EmployeeVestingInfo object for the VYOS parameter
     */
    public EmployeeVestingInfo getVestingParamVYOS() {
        return (EmployeeVestingInfo) this.getVestingParamInfo().get(VestingType.VYOS);
    }

    /**
     * @return - EmployeeVestingInfo object for the Fully Vested parameter
     */
    public EmployeeVestingInfo getVestingParamFullyVested() {
        return (EmployeeVestingInfo) this.getVestingParamInfo().get(VestingType.FULLY_VESTED_IND);
    }
    
    /**
     * @return - EmployeeVestingInfo object for the employment status parameter
     */
    public EmployeeVestingInfo getVestingParamStatus() {
        return (EmployeeVestingInfo) this.getVestingParamInfo().get(VestingType.EMPLOYMENT_STATUS);
    }

    public VestingInputDescription getVestingEffectiveInput(int parameter) {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(parameter);
    }

    public void setVestingEffectiveInput(int parameter, VestingInputDescription vestingInput) {
        this.getVestingEffectiveInput().put(parameter, vestingInput);
    }
    
    public void storeEffectiveInputs(EmployeeVestingInformation evi){
        
        for (int parameter=0; parameter < 9; parameter++) {
            VestingInputDescription effectiveInput = null;
            // If the parameter is HIRE_DATE, use high_date to retrieve the latest submitted value
            if (parameter == VestingRetrievalDetails.PARAMETER_RAW_HIRE_DATE) {
                effectiveInput = employeeVestingInformation.getRetrievalDetails()
                        .getEffectiveInput(parameter, VestingConstants.HIGH_DATE);
            } else {
                effectiveInput = employeeVestingInformation.getRetrievalDetails()
                        .getEffectiveInput(parameter);
            }
            
            if (effectiveInput == null) {
                effectiveInput = new PswVestingInputDescriptionImpl(parameter);
            }
            this.getVestingEffectiveInput().put(parameter, effectiveInput);
        }
    }
    
    public VestingInputDescription getEmploymentStatusDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_RAW_EMPLOYMENT_STATUS);
    }
    
    public Date getEmploymentStatusPlanYearEnd() {
        
        Date pye = null;
        
        VestingInputDescription cpye = getCurrentPlanYearEndDescription();
        VestingInputDescription status = getEmploymentStatusDescription();
        
        if (status != null && cpye != null) {
            pye = new PlanYear(cpye.getDateValue(), status.getEffectiveDate()).getEndDate().getTime();
        }
        
        return pye;
        
    }
    
    public VestingInputDescription getFullyVestedDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_RAW_FULLY_VESTED);
    }
    
    public VestingInputDescription getHireDateDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_RAW_HIRE_DATE);
    }
    
    public VestingInputDescription getVyosDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE);
    }
    
    public VestingInputDescription getPlanYTDHoursDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_RAW_PLAN_YTD_HOURS_OF_SERVICE);
    }
    
    public VestingInputDescription getServiceCreditedDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_CALCULATED_SERVICE_CREDITED_CURRENT_YEAR);
    }
    
    public VestingInputDescription getCompletedYearsDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_CALCULATED_COMPLETED_YEARS_OF_SERVICE);
    }
    
    public VestingInputDescription getCurrentPlanYearEndDescription() {
        return (VestingInputDescription) this.getVestingEffectiveInput().get(VestingRetrievalDetails.PARAMETER_CALCULATED_CURRENT_PLAN_YEAR_END_DATE);
    }

    public Map<Integer, VestingInputDescription> getVestingEffectiveInput() {
        return vestingEffectiveInput;
    }

    public void setVestingEffectiveInput(Map<Integer, VestingInputDescription> vestingEffectiveInput) {
        this.vestingEffectiveInput = vestingEffectiveInput;
    }

    public Date getPlanLastUpdatedDate() {
        return planLastUpdatedDate;
    }

    public void setPlanLastUpdatedDate(Date planLastUpdatedDate) {
        this.planLastUpdatedDate = planLastUpdatedDate;
    }

    public String getShortHireDate() {
        VestingInputDescription hd = getHireDateDescription();
        
        if (hd != null && hd.getDateValue() != null) {
            return DATE_FORMATTER.format(hd.getDateValue()).substring(0, 5);
        }
        return "";
    }

    /**
     * 'Service Credited This Year' and 'Completed years of service' fields are not displayed when
     * the actual calculation date is equal to the VYOS effective date for a non-blank submitted
     * VYOS.
     * 
     * @return boolean - True if the calculated columns should be shown, false otherwise.
     */
    public boolean getShowCalculatedColumns() {
        VestingInputDescription vyos = getVyosDescription();
        VestingInputDescription cyos = getCompletedYearsDescription();

        // Added checks for VYOS and CYOS that might not be set. We only check if they're set and
        // set as 'used'.
        if ((vyos != null) && (vyos.getEffectiveDate() != null) && (vyos.getIntegerValue() != null)
                && (vyos.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED)
                && (cyos != null) && (cyos.getEffectiveDate() != null)
                && (DateUtils.isSameDay(vyos.getEffectiveDate(), cyos.getEffectiveDate()))) {
            return false;
        }
        
        return true;
    }

    /**
     * This method determines if a section of the page is displayed or shown for fully vested
     * results. The page also displays a different section if this is displaying as fully vested.
     * 
     * @return boolean - True if the display should treat this as fully vested, false otherwise.
     */
    public boolean getDisplayAsFullyVested() {
        
        // Check if we're explicitly marked as fully vested (with the indicator).
        final VestingInputDescription vestingInputDescription = 
        getFullyVestedDescription();
        if (BooleanUtils.isTrue(vestingInputDescription.getBooleanValue())) {
            return true;
        } // fi
        
        // Next check if the Employee Explanation is fully vested. This will happen
        // in the case of withdrawals, where the reason, or the participant status
        // makes it fully vested.
        if ((getEmployeeVestingInformation() != null)
                && (getEmployeeVestingInformation().getRetrievalDetails() != null)
                && (getEmployeeVestingInformation().getRetrievalDetails().getExplanation(
                        VestingRetrievalDetails.EXPLANATION_EMPLOYEE_FULLY_VESTED) != null)) {
            
            final VestingExplanation vestingExplanation = getEmployeeVestingInformation()
                    .getRetrievalDetails().getExplanation(
                            VestingRetrievalDetails.EXPLANATION_EMPLOYEE_FULLY_VESTED);
        
            if (StringUtils.isNotBlank(vestingExplanation.getValue())) {
                return true;
            } // fi
        } // fi
        return false;
    }
    
    public EmployeeHireDateInfo getEmployeeHireDateInfo() {
        return employeeHireDateInfo;
    }

    public void setEmployeeHireDateInfo(EmployeeHireDateInfo employeeHireDateInfo) {
        this.employeeHireDateInfo = employeeHireDateInfo;
    }

	/**
	 * @return the applyLTPTCreditingInfo
	 */
	public ApplyLTPTCreditingInfo getApplyLTPTCreditingInfo() {
		return applyLTPTCreditingInfo;
	}

	/**
	 * @param applyLTPTCreditingInfo the applyLTPTCreditingInfo to set
	 */
	public void setApplyLTPTCreditingInfo(ApplyLTPTCreditingInfo applyLTPTCreditingInfo) {
		this.applyLTPTCreditingInfo = applyLTPTCreditingInfo;
	}

}
