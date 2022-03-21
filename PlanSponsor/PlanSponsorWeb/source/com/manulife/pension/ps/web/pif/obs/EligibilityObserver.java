/**
 * 
 */
package com.manulife.pension.ps.web.pif.obs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.pif.util.EligibilityRequirementsVOComparator;
import com.manulife.pension.service.pif.util.MoneyTypeExcludedEmployeeComparator;
import com.manulife.pension.service.pif.valueobject.EligibilityRequirementsVO;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoEligibilityVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;
import com.manulife.pension.service.plan.valueobject.MoneyTypeExcludedEmployee;
import com.manulife.pension.service.plan.valueobject.PlanDeferralLimits;

/**
 * Observer class for Eligibility plan information data
 * @author 	rajenra
 */
public class EligibilityObserver implements Observer {
	
	private static final String DATE_PATTERN = "MM/dd/yyyy";
	private PlanInfoEligibilityVO eligibility = new PlanInfoEligibilityVO();

	/**
	 * @return the eligibility
	 */
	public PlanInfoEligibilityVO getEligibility() {
		return eligibility;
	}

	/**
	 * @param eligibility
	 */
	public EligibilityObserver(PlanInfoEligibilityVO eligibility) {
		super();
		this.eligibility = eligibility;
	}

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
		 	
        if (planInfoVO.getEligibility().getDefaultDeferralPercentage() != null) {
        	uiVO.setDeferralPercentageForAutomaticEnrollment(PlanData.formatBigDecimalPercentageFormatter(planInfoVO.getEligibility()
							.getDefaultDeferralPercentage()));
		}		
       

        if (planInfoVO.getEligibility().getFirstPlanEntryDate() == null) {
            // Convert year end and default to Plan Year End if first plan entry date doesn't exist
        	//uiVO.setFirstPlanEntryDateString(getPlanYearEndPlusOneString(planInfoVO));
        	
        	uiVO.setFirstPlanEntryDateString(StringUtils.EMPTY);
        } else {
        	uiVO.setFirstPlanEntryDateString(planInfoVO.getEligibility().getFirstPlanEntryDate().getData());
        }
        
		if (planInfoVO.getEligibility().getAutomaticEnrollmentDate() != null) {
			uiVO.setAutomaticEnrollmentDate(new SimpleDateFormat(DATE_PATTERN)
					.format(planInfoVO.getEligibility().getAutomaticEnrollmentDate()));
		} 
		
		boolean foundEEDEF = false;
		boolean foundEEROT = false;
		for (EligibilityRequirementsVO moneyType : planInfoVO.getEligibility().getEligibilityRequirements()) {
			if(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL.equals(moneyType.getMoneyTypeId())) {
				foundEEDEF = true;
			}else if(ServiceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION.equals(moneyType.getMoneyTypeId())) {
				foundEEROT = true;
			}
			if(foundEEDEF && foundEEROT) {
				uiVO.setHasBothEEDEFAndEEROTMoneyTypes(true);
				break;
			}
		}		
	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
		
        // Convert year end
        if (StringUtils.isNotBlank(uiVO.getFirstPlanEntryDateString())) {
        	planInfoVO.getEligibility().setFirstPlanEntryDate(new DayOfYear(uiVO.getFirstPlanEntryDateString()));
        } else {
        	planInfoVO.getEligibility().setFirstPlanEntryDate(null);
        }
    	
		if (!PlanData.YES_CODE.equals(planInfoVO.getEligibility().getIsAutomaticEnrollmentAllowed())) {
			planInfoVO.getEligibility().setIsAutomaticContributionsWithdrawalsAllowed(PlanData.UNSPECIFIED_CODE);
			planInfoVO.getEligibility().setDefaultDeferralPercentage(null);
			planInfoVO.getEligibility().setAutomaticEnrollmentDate(null);
			// clear out values that were cleared from JavaScripts
			uiVO.setDeferralPercentageForAutomaticEnrollment(StringUtils.EMPTY);
			uiVO.setAutomaticEnrollmentDate(StringUtils.EMPTY);
		}else{
			planInfoVO.getEligibility().setDefaultDeferralPercentage(					
					StringUtils.isNotEmpty(uiVO.getDeferralPercentageForAutomaticEnrollment()) ? 
							(new BigDecimal(uiVO.getDeferralPercentageForAutomaticEnrollment()).setScale(PlanDeferralLimits.PERCENTAGE_SCALE, RoundingMode.FLOOR)) : null);			
			try {
				SimpleDateFormat sf = new SimpleDateFormat(DATE_PATTERN);
				if (StringUtils.isNotEmpty(uiVO.getAutomaticEnrollmentDate())) {					
					planInfoVO.getEligibility().setAutomaticEnrollmentDate(
							sf.parse(uiVO.getAutomaticEnrollmentDate()));

				} else {
					planInfoVO.getEligibility().setAutomaticEnrollmentDate(null);
				}
			} catch (ParseException e) {
				// todo - do something here.
			}
			
		}
		
		
		if(uiVO.isSave()){
	    	//iterate the given list of eligibilityRequirementsList and remove all the unselected MoneyType 
			// in the Money type tab. Only selected money types are saved.
			//Eligibility Requirements
	    	List<EligibilityRequirementsVO> eligibilityRequirementsList = getEligibility().getEligibilityRequirements();
	    	List<EligibilityRequirementsVO> eligibilityRequirementsSelectedList = new ArrayList<EligibilityRequirementsVO>();	
			for(EligibilityRequirementsVO requirementsVO: eligibilityRequirementsList){
				if(requirementsVO.getSelectedMoneyType() != null && requirementsVO.getSelectedMoneyType()){	
					eligibilityRequirementsSelectedList.add(requirementsVO);		
				}
			
			}
			getEligibility().setEligibilityRequirements(eligibilityRequirementsSelectedList);

	    	//iterate the given list of moneyTypeExcludedEmployeeList and remove all the unselected MoneyType 
			// in the Money type tab. Only selected money types are saved.
			//Excluded Employee
	    	List<MoneyTypeExcludedEmployee> moneyTypeExcludedEmployeeList = getEligibility().getMoneyTypeExcludedEmployees();
	    	List<MoneyTypeExcludedEmployee> moneyTypeExcludedEmployeeSelectedList = new ArrayList<MoneyTypeExcludedEmployee>();	
			for(MoneyTypeExcludedEmployee excludedEmployee: moneyTypeExcludedEmployeeList){
				if(excludedEmployee.getSelectedMoneyType() != null && excludedEmployee.getSelectedMoneyType()){
					moneyTypeExcludedEmployeeSelectedList.add(excludedEmployee);		
				}
			
			}
			getEligibility().setMoneyTypeExcludedEmployees(moneyTypeExcludedEmployeeSelectedList);
		}
	}
	
	  /**
     * Get the default first plan entry date
     * default is planYearEnd + 1 day
     * 
     * @param planData
     * @return a string of mm/dd 
     */
    private String getPlanYearEndPlusOneString(PlanInfoVO planInfoVO) {
        String returnString = "";
        DayOfYear planYearEnd = planInfoVO.getGeneralInformations().getPlanYearEnd();
        if(planYearEnd != null) {            
            final Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(planYearEnd.getAsDatePlusOneNonLeapYear());
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
            returnString = formatter.format(calendar.getTime());
        }
        return returnString;
    }
    
    /**
     * populate the selected Money Types in money type tab
     * 
     * @param List<PIFMoneyType> 
     */
    private void createAllowedMoneyTypesForEligibilityRequirements(List<PIFMoneyType> permittedMoneyTypes){

    	//List<PIFMoneyType> permittedMoneyTypes = getPermittedMoneyTypes();
    	List<EligibilityRequirementsVO> eligibilityRequirementsList = getEligibility().getEligibilityRequirements();
    	
    	//iterate the given list of permittedMoneyTypes and set the selectedMoneyType if it is selected in the Money type tab
		for(PIFMoneyType pifMoneyType: permittedMoneyTypes){
			if(pifMoneyType.getEligibility()){
				
				EligibilityRequirementsVO requirementsVO = new EligibilityRequirementsVO();
				requirementsVO.setMoneyTypeId(pifMoneyType.getMoneyTypeCode());
				requirementsVO.setContractMoneyTypeShortName(pifMoneyType.getShortName());
				int index = eligibilityRequirementsList.indexOf(requirementsVO);
				
				if(index != -1){
					//If money type already exist in the Eligibility Requirements List
					EligibilityRequirementsVO eligibilityRequirement = eligibilityRequirementsList.get(index);
					eligibilityRequirement.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
				}else {
					requirementsVO.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
					requirementsVO.setContractMoneyTypeLongName(pifMoneyType.getLongName());					
					eligibilityRequirementsList.add(requirementsVO);
				}			
			}
		}
		Collections.sort(eligibilityRequirementsList, new EligibilityRequirementsVOComparator(permittedMoneyTypes));
    }
    
    /**
     * populate the selected Money Types in money type tab
     * 
     * @param List<PIFMoneyType>
     */
    private void createAllowedMoneyTypesForEligibilityExcludedEmployees(List<PIFMoneyType> permittedMoneyTypes){

    	//List<PIFMoneyType> permittedMoneyTypes = getPermittedMoneyTypes();
    	List<MoneyTypeExcludedEmployee> moneyTypeExcludedEmployeeList = getEligibility().getMoneyTypeExcludedEmployees();
    	
    	//iterate the given list of permittedMoneyTypes and set the selectedMoneyType if it is selected in the Money type tab
		for(PIFMoneyType pifMoneyType: permittedMoneyTypes){
			if(pifMoneyType.getEligibility()){
				
				MoneyTypeExcludedEmployee moneyTypeExcludedEmployeeVO = new MoneyTypeExcludedEmployee();
				moneyTypeExcludedEmployeeVO.setMoneyTypeId(pifMoneyType.getMoneyTypeCode());
				moneyTypeExcludedEmployeeVO.setContractMoneyTypeShortName(pifMoneyType.getShortName());
				int index = moneyTypeExcludedEmployeeList.indexOf(moneyTypeExcludedEmployeeVO);
				
				if(index != -1){
					//If money type already exist in the Excluded Employee List
					MoneyTypeExcludedEmployee moneyTypeExcludedEmployee = moneyTypeExcludedEmployeeList.get(index);
					moneyTypeExcludedEmployee.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
				}else {
					moneyTypeExcludedEmployeeVO.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
					moneyTypeExcludedEmployeeVO.setContractMoneyTypeLongName(pifMoneyType.getLongName());
					moneyTypeExcludedEmployeeList.add(moneyTypeExcludedEmployeeVO);
				}
			}
		}
		Collections.sort(moneyTypeExcludedEmployeeList, new MoneyTypeExcludedEmployeeComparator(permittedMoneyTypes));
    }    
    
	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
    public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
        // Load money type eligibility criteria money type
    	createAllowedMoneyTypesForEligibilityRequirements(list);
    	
        // Load money type eligibility excluded employee money type
    	createAllowedMoneyTypesForEligibilityExcludedEmployees(list);
		
	}

}
