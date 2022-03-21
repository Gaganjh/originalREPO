package com.manulife.pension.ps.web.pif.obs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.util.PIFAllowableMoneyTypesVOComparator;
import com.manulife.pension.service.pif.valueobject.PIFAllowableMoneyTypesVO;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoWithdrawalsVO;

/**
 * Observer class for Withdrawals plan information data
 * @author 	rajenra
 */
public class WithdrawalsObserver implements Observer {

	 private PlanInfoWithdrawalsVO withdrawals = new PlanInfoWithdrawalsVO();

	/**
	 * The constructor
	 * @param withdrawals
	 */	 
	 public WithdrawalsObserver(PlanInfoWithdrawalsVO withdrawals){
		 this. withdrawals =  withdrawals;
	 }

	/**
	 * @return the withdrawals
	 */
	 public PlanInfoWithdrawalsVO getPlanInfoWithdrawalsVO(){
		 return this.withdrawals;
	 }
	 
	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        // Convert allowable money types for withdrawals
		//createAllowedMoneyTypesForWithdrawalTab(planInfoVO.getWithdrawals().getAllowedMoneyTypesForHardship());	
    	
    	//Convert the format of minmum withdrwalAmount to "#,##0" format
        if (planInfoVO.getWithdrawals().getWithdrawalDistributionMethod().getMinWithdrawalAmount() != null) {
        	uiVO.setPartialWithdrawalMinimumWithdrawalAmount(planInfoVO.getWithdrawals().getWithdrawalDistributionMethod()
							.getMinWithdrawalAmount().toString());
		}

	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {

		//Set the minimum withdrawlAmount 
		if (planInfoVO.getWithdrawals().getWithdrawalDistributionMethod()
				.getPartialWithdrawalIndicator() != null
				&& !planInfoVO.getWithdrawals().getWithdrawalDistributionMethod()
						.getPartialWithdrawalIndicator()) {
			
			planInfoVO.getWithdrawals().getWithdrawalDistributionMethod().setMinWithdrawalAmount(
					null);
			
			if(uiVO.getInitialPlanInfoVO() != null){
				Integer minwithdrawalAmt = uiVO.getInitialPlanInfoVO().getWithdrawals().getWithdrawalDistributionMethod().getMinWithdrawalAmount();
				if(minwithdrawalAmt != null && minwithdrawalAmt == 0){
					planInfoVO.getWithdrawals().getWithdrawalDistributionMethod().setMinWithdrawalAmount(new Integer("0"));
				}				
			}

			uiVO.setPartialWithdrawalMinimumWithdrawalAmount(StringUtils.EMPTY);
		} else {
			Integer minWithdrawalAmount = null;
			if (!StringUtils
					.isBlank(uiVO.getPartialWithdrawalMinimumWithdrawalAmount())) {
				minWithdrawalAmount = new Integer(
						uiVO.getPartialWithdrawalMinimumWithdrawalAmount().replace(
								",", StringUtils.EMPTY).replace(".00", StringUtils.EMPTY));
			}
			planInfoVO.getWithdrawals().getWithdrawalDistributionMethod().setMinWithdrawalAmount(
					minWithdrawalAmount);
		}

		if(uiVO.isSave()){
	    	//iterate the given list of HardshipList and remove all the 
			// unselected MoneyType in the Money type tab. Only selected money types are saved. 
			List<PIFAllowableMoneyTypesVO> hardshipList = planInfoVO.getWithdrawals().getAllowedMoneyTypesForHardship();
			List<PIFAllowableMoneyTypesVO> hardshipSelectedList =  new ArrayList<PIFAllowableMoneyTypesVO>();
			
			//Initial PIFAllowableMoneyTypesVO
			List<PIFAllowableMoneyTypesVO> initialHardshipList = 
				uiVO.getInitialPlanInfoVO().getWithdrawals().getAllowedMoneyTypesForHardship();
			for(PIFAllowableMoneyTypesVO hardshipObj: hardshipList){
				//Check the money type that is available in initial PIFAllowableMoneyTypesVO.
				int index = initialHardshipList.indexOf(hardshipObj);				
				if(index != -1){
					//If money type that is available in initial PIFAllowableMoneyTypesVO, No Need to check for selectedMoneyType
					hardshipSelectedList.add(hardshipObj);	
				}else{
					//Add only the money types that are selected in money type tab.
					if(hardshipObj.getSelectedMoneyType() != null && hardshipObj.getSelectedMoneyType()){
						hardshipSelectedList.add(hardshipObj);		
					}						
				}
			}
			planInfoVO.getWithdrawals().setAllowedMoneyTypesForHardship(hardshipSelectedList);
		}
		
	}
	
    /**
     * populate the selected Money Types in money type tab
     * 
     * @param List<PIFMoneyType>
     */
    private void createAllowedMoneyTypesForWithdrawalTab(List<PIFMoneyType> permittedMoneyTypes){
    	
    	//List<PIFMoneyType> permittedMoneyTypes = getPermittedMoneyTypes();
    	List<PIFAllowableMoneyTypesVO> pifAllowableMoneyTypes = getPlanInfoWithdrawalsVO().getAllowedMoneyTypesForHardship();
    	
    	//iterate the given list of permittedMoneyTypes and set the selectedMoneyType if it is selected in the Money type tab
		for(PIFMoneyType pifMoneyType: permittedMoneyTypes){
			if(pifMoneyType.getWithdrawalIndicator()){
				
				PIFAllowableMoneyTypesVO allowableMoneyTypesVO = new PIFAllowableMoneyTypesVO();
				allowableMoneyTypesVO.setMoneyTypeId(pifMoneyType.getMoneyTypeCode());
				allowableMoneyTypesVO.setMoneyTypeShortName(pifMoneyType.getShortName());
				int index = pifAllowableMoneyTypes.indexOf(allowableMoneyTypesVO);
				
				if(index != -1){
					//If money type already exist in the Eligibility Requirements List
					PIFAllowableMoneyTypesVO allowableMoneyTypes = pifAllowableMoneyTypes.get(index);
					allowableMoneyTypes.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
				}else {
					allowableMoneyTypesVO.setSelectedMoneyType(pifMoneyType.getSelectedIndicator());
					allowableMoneyTypesVO.setMoneyTypeLongName(pifMoneyType.getLongName());
					pifAllowableMoneyTypes.add(allowableMoneyTypesVO);
				}					
		
			}
		}
		Collections.sort(pifAllowableMoneyTypes, new PIFAllowableMoneyTypesVOComparator(permittedMoneyTypes));    	
    	
    }		

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
    public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate PlanInfoWithdrawalsVO VO 
    	createAllowedMoneyTypesForWithdrawalTab(list);
    	
	}    

}
