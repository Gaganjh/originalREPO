package com.manulife.pension.ps.web.pif.obs;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observable Interface for plan information data
 * @author 	rajenra
 */
public interface Observable {
	
	  public void addObserver(Observer obsrNewObserver);
	  public void removeObserver(Observer obsrToRemove);
	/**
	 * Method to convert PIFDataUi from PlanInfo object.
	 * This method will iterate through all the registered observers and update the PIFDataUi from PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	  public void notifyFromBeanObservers(PIFDataUi uiVO, PlanInfoVO planVO);
	/**
	 * Method to convert PIFDataUi to PlanInfo object. 
	 * This method will iterate through all the registered observers and update the PIFDataUi to PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	  public void notifyToBeanObservers(PIFDataUi uiVO, PlanInfoVO planVO);

}
