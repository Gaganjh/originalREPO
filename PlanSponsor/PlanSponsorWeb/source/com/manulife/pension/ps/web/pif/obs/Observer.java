package com.manulife.pension.ps.web.pif.obs;

import java.util.List;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer Interface for plan information data
 * @author 	rajenra
 */
public interface Observer {
	/**
	 * Method to convert PIFDataUi to PlanInfo object. 
	 * This method will iterate through all the registered observers and update the PIFDataUi to PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planVO);
	
	/**
	 * Method to convert PIFDataUi from PlanInfo object.
	 * This method will iterate through all the registered observers and update the PIFDataUi from PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planVO);
	
	/**
	 * Method to convert PIFDataUi to PlanInfo object. 
	 * This method will iterate through all the registered observers and update the PIFDataUi to PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void update(List<PIFMoneyType> list);
}
