package com.manulife.pension.ps.web.pif.obs;

import java.util.List;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoMoneyTypeVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer class for MoneyType plan information data
 * @author 	rajenra
 */
public class MoneyTypeObserver implements Observer {
	
	private PlanInfoMoneyTypeVO pifMoneyType = new PlanInfoMoneyTypeVO();
	 
	 public MoneyTypeObserver(PlanInfoMoneyTypeVO pifMoneyType){
		 this. pifMoneyType =  pifMoneyType;
	 }
	 
	 public PlanInfoMoneyTypeVO getPlanInfoMoneyTypeVO(){
		 return this.pifMoneyType;
	 }

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planVO) {
		// TODO Auto-generated method stub

	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planVO) {
		// TODO Auto-generated method stub

	}

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
	public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
		
	}

}
