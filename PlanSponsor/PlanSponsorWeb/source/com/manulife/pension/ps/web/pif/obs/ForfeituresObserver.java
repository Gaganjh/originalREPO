package com.manulife.pension.ps.web.pif.obs;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.ForfeituresOptionVO;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoForfeituresVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer class for Forfeitures plan information data
 * @author 	rajenra
 */
public class ForfeituresObserver implements Observer {
	
	private PlanInfoForfeituresVO forfeitures = new PlanInfoForfeituresVO();
	
	

	/**
	 * @return the forfeitures
	 */
	public PlanInfoForfeituresVO getForfeitures() {
		return forfeitures;
	}

	/**
	 * @param forfeitures
	 */
	public ForfeituresObserver(PlanInfoForfeituresVO forfeitures) {
		super();
		this.forfeitures = forfeitures;
	}

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        // Convert unvested money options
        String[] unvestedMoneyOptions = new String[planInfoVO.getForfeitures().getForfeituresOptions().size()];
		for ( int i = 0 ;  i <  planInfoVO.getForfeitures().getForfeituresOptions().size() ; i++  ) {			
			unvestedMoneyOptions[i] = planInfoVO.getForfeitures().getForfeituresOptions().get(i).getSelectOption();			
		}
		uiVO.setSelectedUnvestedMoneyOptions(unvestedMoneyOptions);

	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        // Convert unvested money options and default unvested money options
        List<ForfeituresOptionVO> forfeituresOptions = planInfoVO.getForfeitures().getForfeituresOptions();
        String defaultOption = planInfoVO.getForfeitures().getDefaultOption();
        
        if (!ArrayUtils.isEmpty(uiVO.getSelectedUnvestedMoneyOptions())) {  
    		for ( int i = 0 ;  i <  forfeituresOptions.size() ; i++  ) {
    			if(!StringUtils.isEmpty(defaultOption) && !defaultOption.equals(
						planInfoVO.getForfeitures().getForfeituresOptions().get(i).getAvailableOptionsCode())){
    				forfeituresOptions.get(i).setSelectOption(StringUtils.EMPTY);
    			}
    		}        	
    		for ( int i = 0 ;  i <  forfeituresOptions.size() ; i++  ) {	    			
    			for(int j = 0; j < uiVO.getSelectedUnvestedMoneyOptions().length ; j++){
    				if(uiVO.getSelectedUnvestedMoneyOptions()[j] != null && 
    						uiVO.getSelectedUnvestedMoneyOptions()[j].equals(
    								planInfoVO.getForfeitures().getForfeituresOptions().get(i).getAvailableOptionsCode())){
            			forfeituresOptions.get(i).setSelectOption(uiVO.getSelectedUnvestedMoneyOptions()[j]);
    				}
    			}    			
    		}
        }
        planInfoVO.getForfeitures().setForfeituresOptions(forfeituresOptions); 
	}

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
	public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
		
	}

}
