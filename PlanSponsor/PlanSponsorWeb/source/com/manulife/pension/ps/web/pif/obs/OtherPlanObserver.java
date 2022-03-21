package com.manulife.pension.ps.web.pif.obs;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.pif.util.PIFConstants;
import com.manulife.pension.service.pif.valueobject.PlanOtherInfoVO ;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer class for Other plan information data
 * @author 	rajenra
 */
public class OtherPlanObserver implements Observer {
	
	private PlanOtherInfoVO planOtherInfoVO = new PlanOtherInfoVO();

	/**
	 * The constructor
	 * @param planOtherInfoVO
	 */
	 public OtherPlanObserver(PlanOtherInfoVO planOtherInfoVO){
		 this. planOtherInfoVO =  planOtherInfoVO;
	 }
	 
	/**
	 * @return the planOtherInfoVO
	 */	 
	 public PlanOtherInfoVO getPlanOtherInfoVO(){
		 return this.planOtherInfoVO;
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
		
		if(PIFConstants.NO.equals(planVO.getOtherInformation().getQdiaRestrictionImposed())){
			planVO.getOtherInformation().setQdiaRestrictionDetails(StringUtils.EMPTY);
		}
	}

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
	public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
		
	}

}
