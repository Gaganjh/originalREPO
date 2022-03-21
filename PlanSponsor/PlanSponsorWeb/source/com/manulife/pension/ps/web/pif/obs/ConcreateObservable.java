package com.manulife.pension.ps.web.pif.obs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observable class for plan information data
 * @author 	rajenra
 */
@SuppressWarnings("unchecked")
public class ConcreateObservable implements Observable {

	
	private ArrayList observers = new ArrayList();

	/**
	 * method to add the observers
	 * @param Observer
	 */
	public void addObserver(Observer obsrNewObserver) {
		if (!observers.contains(obsrNewObserver)) {
			observers.add(obsrNewObserver);	
		}
	}
	
	/**
	 * method to remove the observers
	 * @param Observer
	 */  
	public void removeObserver(Observer obsrToRemove) {
		observers.remove(obsrToRemove);
	}
	
	/**
	 * Method to convert PIFDataUi from PlanInfo object.
	 * This method will iterate through all the registered observers and update the PIFDataUi from PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void notifyFromBeanObservers(PIFDataUi uiVO, PlanInfoVO planVO) {
		Iterator elements = observers.iterator();
		while (elements.hasNext()) {
			((Observer)elements.next()).updateFromBean( uiVO,  planVO);
		}
		
	}
	
	/**
	 * Method to convert PIFDataUi to PlanInfo object. 
	 * This method will iterate through all the registered observers and update the PIFDataUi to PlanInfoVO object
	 * 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void notifyToBeanObservers(PIFDataUi uiVO, PlanInfoVO planVO) {
		Iterator elements = observers.iterator();
		while (elements.hasNext()) {
			((Observer)elements.next()).updateToBean( uiVO,  planVO);
		}
		
	}

	/**
	 * Method to Iterate through all registered Observers and update the Money type selection indicator value
	 * to all the Corresponding Value Objects. 
	 * 
	 * This method will be notified once there is any change in the Observable object.  
	 *  
	 * @param List<PIFMoneyType>
	 */
	public void update(List<PIFMoneyType> list) {
		Iterator elements = observers.iterator();
		while (elements.hasNext()) {
			((Observer)elements.next()).update(list);
		}
		
	}


}
