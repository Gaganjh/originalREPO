package com.manulife.pension.ps.web.census;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.contract.valueobject.PlanDataHistoryVO;
import com.manulife.pension.service.employee.valueobject.ApplyLTPTCreditingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeFullyVestedIndicatorInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVYOSInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.vesting.VestingConstants;


/**
 * This action form stores the common information related to view/edit
 * vesting information page
 * 
 * @author maceadi
 * 
 */
public class VestingInformationForm extends AutoForm implements CloneableForm {

	private static final long serialVersionUID = 1L;

    /**
     * The source page id from where the user comes to this page
     */
    protected String source = CensusConstants.EMPLOYEE_SNAPSHOT_PAGE;
    
    protected String profileId;
    
    protected String asOfDate;
    
    protected String sourceAsOfDate;
    
    protected String fullyVestedInd;
    
    protected String fullyVestedEffectiveDate;
    
    protected String[] vyos;
    
    protected String[] vyosDate;
    
    protected String futurePlanYearEnd;
    
    private CloneableForm clonedForm;
    
    private String ltptCrediting;
    
    private java.sql.Date effectiveDate;
    
    private boolean plan457;
    
    private boolean displayApplyLTPTCreditingField = false;
    
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");
    
    public static final List planFieldNames = Arrays.asList(new String[] {
            PlanDataHistoryVO.FIELD_VESTING_100_FOR_DEATH_WITHDRAWALS,
            PlanDataHistoryVO.FIELD_VESTING_100_FOR_EARLY_RETIREMENT_WITHDRAWALS,
            PlanDataHistoryVO.FIELD_VESTING_100_FOR_NORMAL_RETIREMENT_WITHDRAWALS,
            PlanDataHistoryVO.FIELD_VESTING_100_FOR_PERMANENT_DISABILITY_WITHDRAWALS,
            PlanDataHistoryVO.FIELD_VESTING_SERVICE_CREDITING_METHOD,
            PlanDataHistoryVO.FIELD_VESTING_HOURS_OF_SERVICE,
            PlanDataHistoryVO.FIELD_VESTING_MONEY_TYPE_COMPLETE_SCHEDULE });

	/**
	 * Constructor for VestingInformationForm.
	 */
	public VestingInformationForm() {
		super();
	}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
    }

    public String getSourceAsOfDate() {
        return sourceAsOfDate;
    }

    public void setSourceAsOfDate(String sourceAsOfDate) {
        if (this.sourceAsOfDate == null) {
            this.sourceAsOfDate = sourceAsOfDate;
        }
    }
    
    public String[] getVyos() {
        return vyos;
    }

    public void setVyos(String[] vyos) {
        this.vyos = vyos;
    }
    
    public String[] getVyosDate() {
        return vyosDate;
    }

    public void setVyosDate(String[] vyosDate) {
        this.vyosDate = vyosDate;
    }
    
    public String getVyos(int index) {
        return vyos[index];
    }

    public void setVyos(int index, String vyos) {
        this.vyos[index] = vyos;
    }
    
    public String getVyosDate(int index) {
        return vyosDate[index];
    }

    public void setVyosDate(int index, String vyosDate) {
        this.vyosDate[index] = vyosDate;
    }
    
    /**
     * Clone all attributes in the form
     */
    public Object clone() {
        VestingInformationForm myClone = new VestingInformationForm();
        
        myClone.setVyos(vyos);
        myClone.setVyosDate(vyosDate);
        
        myClone.setFullyVestedInd(fullyVestedInd);
        myClone.setFullyVestedEffectiveDate(fullyVestedEffectiveDate);
        myClone.setLtptCrediting(ltptCrediting);
        
        return myClone;
    }
    
    public CloneableForm getClonedForm() {
        return clonedForm;
    }
    

    public void storeClonedForm() {
        clonedForm = (CloneableForm) clone();
    }

    public void clear( HttpServletRequest request) {}
    
    public String getFullyVestedEffectiveDate() {
        return fullyVestedEffectiveDate;
    }

    public void setFullyVestedEffectiveDate(String fullyVestedEffectiveDate) {
        this.fullyVestedEffectiveDate = fullyVestedEffectiveDate;
    }

    public String getFullyVestedInd() {
        return fullyVestedInd;
    }

    public void setFullyVestedInd(String fullyVestedInd) {
        this.fullyVestedInd = fullyVestedInd;
    }

    public String getFuturePlanYearEnd() {
        return futurePlanYearEnd;
    }

    public void setFuturePlanYearEnd(String futurePlanYearEnd) {
        this.futurePlanYearEnd = futurePlanYearEnd;
    }
    
    public String getLtptCrediting() {
		return ltptCrediting;
	}

	public void setLtptCrediting(String ltptCrediting) {
		this.ltptCrediting = ltptCrediting;
	}
	
	public boolean isPlan457() {
		return plan457;
	}

	public void setPlan457(boolean plan457) {
		this.plan457 = plan457;
	}
	
	public boolean isDisplayApplyLTPTCreditingField() {
		return displayApplyLTPTCreditingField;
	}

	public void setDisplayApplyLTPTCreditingField(boolean displayApplyLTPTCreditingField) {
		this.displayApplyLTPTCreditingField = displayApplyLTPTCreditingField;
	}
	
	/**
     * Populate form with vestingInformation data
     */
    public void populateForm(VestingInformation vestingInfo) {       
        String[] vyos = new String[3];
        String[] vyosDate = new String[3];
        
        // populate vyos fields
        for ( int i=0; i < vestingInfo.getVestingParamVYOS().getVyosList().size(); i++) {
            EmployeeVYOSInfo vyosInfo = vestingInfo.getVestingParamVYOS().getVyosList().get(i);
            // populate future plan year end date
            if (i==2) {
                Date futurePlanYearEnd = vyosInfo.getEffectiveDate();
                if (futurePlanYearEnd != null) {
                    this.setFuturePlanYearEnd(DATE_FORMATTER.format(futurePlanYearEnd));
                }
            }
            if (vyosInfo.getVyos() != null) {
                vyos[i] = vyosInfo.getVyos().toString();
            } else {
                // if null, display blank on UI
                vyos[i] = "";
            }
            
            vyosDate[i] = DATE_FORMATTER.format(vyosInfo.getEffectiveDate());
        }
        this.setVyos(vyos);
        this.setVyosDate(vyosDate);
        
        // populate fully vested fields
        EmployeeFullyVestedIndicatorInfo fullyVestedInfo = 
            vestingInfo.getVestingParamFullyVested().getFullyVestedIndicator();
        this.setFullyVestedInd(fullyVestedInfo.isFullyVested() ? "Y" : "N");
        if (fullyVestedInfo.isFullyVested()) {
            this.setFullyVestedEffectiveDate(DATE_FORMATTER.format(fullyVestedInfo.getEffectiveDate()));
        } else {
            this.setFullyVestedEffectiveDate("");
        }
        ApplyLTPTCreditingInfo applyLTPTCreditingInfo = vestingInfo.getApplyLTPTCreditingInfo();
        this.setLtptCrediting(applyLTPTCreditingInfo!=null ? applyLTPTCreditingInfo.getApplyLTPTCrediting() : "N");
        // Set the as of date to the one actually used when we calculated vesting.
        this.setAsOfDate(DATE_FORMATTER.format(vestingInfo.getAsOfDate()));
        
        // To check the vesting service feature and crediting method to display ApplyLTPTCrediting field  
        String vestingSericeFeature=vestingInfo.getEmployeeVestingInformation().getVestingServiceFeature();
        String vestingServiceCreditingMethod=vestingInfo.getEmployeeVestingInformation().getRetrievalDetails().getCreditingMethod();
        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(vestingSericeFeature) && !this.isPlan457()) {
        	if (VestingConstants.CreditingMethod.HOURS_OF_SERVICE.equalsIgnoreCase(vestingServiceCreditingMethod)) {
        			this.setDisplayApplyLTPTCreditingField(true);
        	}
        }
    }

    /**
     * Returns EmployeeVestingInfo for FullyVestedInfo object by ignoring effectiveDate thereby
     * returning whatever value is in CSDB, so that a fullyVestedInfo for future date could be
     * returned.
     * 
     * @param serviceFacade
     * @param userProfile
     * @return
     * @throws SystemException
     */
    protected EmployeeVestingInfo getFullyVestedInfoIgnoringDate(
            EmployeeServiceFacade serviceFacade, UserProfile userProfile,
            EmployeeVestingInfo currentVestingInfo) throws SystemException {

        // Retrieve fullyVestedIndicator again, but don't use any date so that we retrieve whatever
        // value is in CSDB (could be future dated as well)
        EmployeeVestingInfo fullyVestedInfo = serviceFacade.getEmployeeVestingInfo(new Long(
                profileId), userProfile.getCurrentContract().getContractNumber(), null,
                VestingType.FULLY_VESTED_IND, false, false, true);

        currentVestingInfo.setFullyVestedIndicator(fullyVestedInfo.getFullyVestedIndicator());
        return currentVestingInfo;
    }

    /**
     * Filter out Canceled statuses for external users
     * 
     * @param userProfile
     * @param info
     * @return EmployeeVestingInfo
     */
    protected EmployeeVestingInfo filterOutCanceledStatus(UserProfile userProfile, EmployeeVestingInfo info) {
        EmployeeVestingInfo result = info;
        EmployeeVestingInfo newInfo = new EmployeeVestingInfo(info.getProfileId(), info.getContractId());
        if (userProfile.getRole().isExternalUser()) {
            for (EmployeeStatusInfo statusInfo : info.getFullStatusList()) {
                if (!statusInfo.getStatus().equals(CensusConstants.EMPLOYMENT_STATUS_CANCEL)) {
                    newInfo.addEmploymentStatus(statusInfo);
                }
            }

            result = newInfo;
        }

        // If the filtered employmentStatus list is empty, and we do have a current status, put that
        // in the list, as we need to display the current applicable status 
        if (result.getFullStatusList().isEmpty() && info.getCurrentStatus() != null) {
            if (userProfile.getRole().isExternalUser()) {
                if (!info.getCurrentStatus().getStatus().equals(
                            CensusConstants.EMPLOYMENT_STATUS_CANCEL)) {
                    // Add the current status directly. Don't call addEmploymentStatus as the
                    // current won't be made a part of statusList
                    result.getStatusList().add(info.getCurrentStatus());
                }
            } else {
                // Add the current status directly. Don't call addEmploymentStatus as the
                // current won't be made a part of statusList
                result.getStatusList().add(info.getCurrentStatus());
            }
        }

        return result;
    }
    
    /**
     * Reset values in the form
     * 
     * @param mapping
     * @param request
     */
    public void reset( HttpServletRequest request) {
        super.reset( request);
        this.vyos = new String[3];
        this.vyosDate = new String[3];
        this.fullyVestedInd = "N";
        this.fullyVestedEffectiveDate = "";
        this.ltptCrediting="";
    }

    public static List getPlanFieldNames() {
        return planFieldNames;
    }
    
    
}

