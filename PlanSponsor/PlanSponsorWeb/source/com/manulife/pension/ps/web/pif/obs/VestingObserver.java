package com.manulife.pension.ps.web.pif.obs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.contract.valueobject.ScheduleAmount;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.pif.util.VestingScheduleComparator;
import com.manulife.pension.service.pif.util.PIFConstants.MoneyGroup;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVestingVO;

/**
 * Observer class for Vesting plan information data
 * @author 	rajenra
 */
public class VestingObserver implements Observer {
	
	 private PlanInfoVestingVO vesting = new PlanInfoVestingVO();

	/**
	 * The constructor
	 * @param vesting
	 */
	 public VestingObserver(PlanInfoVestingVO vesting){
		 this. vesting =  vesting;
	 }

	/**
	 * @return the vesting
	 */
	 public PlanInfoVestingVO getPlanInfoVestingVO(){
		 return this.vesting;
	 }

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        
	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        
		if(uiVO.isSave()){
	    	//iterate the given list of vestingScheduleList and remove all the 
			// unselected MoneyType in the Money type tab. Only selected money types are saved. 
			List<VestingSchedule> vestingScheduleList = planInfoVO.getVesting().getVestingSchedules();
			List<VestingSchedule> vestingScheduleSelectedList =  new ArrayList<VestingSchedule>();	
			for(VestingSchedule vestingSchedule: vestingScheduleList){
				if(vestingSchedule.getSelectedMoneyType() != null && vestingSchedule.getSelectedMoneyType()){
					vestingScheduleSelectedList.add(vestingSchedule);		
				}
			
			}
			planInfoVO.getVesting().setVestingSchedules(vestingScheduleSelectedList);
		}	
	}
	
    /**
     * populate the selected Money Types in money type tab
     * 
     * @param List<PIFMoneyType>
     */
    private void createAllowedMoneyTypesForVestingSchedule(List<PIFMoneyType> permittedMoneyTypes){
    	

		//List<PIFMoneyType> permittedMoneyTypes = getPermittedMoneyTypes();
    	List<VestingSchedule> vestingSchedulesList = getPlanInfoVestingVO().getVestingSchedules();
    	List<PIFMoneyType> permittedEmployerMoneyTypes = new ArrayList<PIFMoneyType>();
    	for(PIFMoneyType pifMoneyType: permittedMoneyTypes){
    		String category = pifMoneyType.getCategory();
    		if(category != null && category.equalsIgnoreCase(MoneyGroup.EMPLOYER.getGroup())){
    			permittedEmployerMoneyTypes.add(pifMoneyType);
    		}
    	}
    	
    	//iterate the given list of permittedMoneyTypes and set the selectedMoneyType if it is selected in the Money type tab
		for(PIFMoneyType pifMoneyType: permittedEmployerMoneyTypes){
			
			VestingSchedule vestingScheduleVO = new VestingSchedule();
			vestingScheduleVO.setMoneyTypeId(pifMoneyType.getMoneyTypeCode());
			vestingScheduleVO.setMoneyTypeShortName(pifMoneyType.getShortName());
			int index = vestingSchedulesList.indexOf(vestingScheduleVO);
			
			if(index != -1){
				//If money type already exist in the Excluded Employee List
				VestingSchedule vestingSchedule = vestingSchedulesList.get(index);
				vestingSchedule.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
			}else {
				vestingScheduleVO.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
				vestingScheduleVO.setMoneyTypeLongName(pifMoneyType.getLongName());
	            Collection<ScheduleAmount> amounts = new ArrayList<ScheduleAmount>();
	            for(int i=0; i<=6; i++){
                    final ScheduleAmount amount = new ScheduleAmount(null);
                    amounts.add(amount);
	            }
	            vestingScheduleVO.setSchedules(amounts);
				vestingSchedulesList.add(vestingScheduleVO);
			}
		}
		Collections.sort(vestingSchedulesList, new VestingScheduleComparator(permittedMoneyTypes));		
    }	

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
    public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
        // Load money type eligibility criteria money type
    	createAllowedMoneyTypesForVestingSchedule(list);
    	
	}

}
