package com.manulife.pension.ps.web.pif.obs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanGeneralInfoVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer class for General plan information data
 * @author 	rajenra
 */
public class GeneralInfoObserver implements Observer {
	
	private static final String DATE_PATTERN = "MM/dd/yyyy";
	private static final int TAX_IDENTIFICATION_NUMBER_PART_1_LENGTH = 2;
	private static final String TAX_IDENTIFICATION_NUMBER_SPLITER = "-";
	
	private PlanGeneralInfoVO generalInformations = new PlanGeneralInfoVO();	 
	
	/**
	 * @return the generalInformations
	 */
	public PlanGeneralInfoVO getGeneralInformations() {
		return generalInformations;
	}

	/**
	 * @param generalInformations
	 */
	public GeneralInfoObserver(PlanGeneralInfoVO generalInformations) {
		super();
		this.generalInformations = generalInformations;
	}

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planVO) {
		
		// Convert employer tax identification number
		final String taxId = planVO.getGeneralInformations().getEmployerTaxIdentificationNumber();
        if (StringUtils.isNotBlank(taxId)) {
           	uiVO.setEmployerTaxIdentificationNumber(taxId);
        }	    	
        
		if (planVO.getGeneralInformations().getPlanEffectiveDate() != null) {
			uiVO.setPlanEffectiveDate(new SimpleDateFormat(DATE_PATTERN).format(planVO.getGeneralInformations().getPlanEffectiveDate()));
		}
		
        // Convert plan year end to String.
        if (planVO.getGeneralInformations().getPlanYearEnd() == null) {
        	uiVO.setPlanYearEndString(StringUtils.EMPTY);
        } else {
        	uiVO.setPlanYearEndString(planVO.getGeneralInformations().getPlanYearEnd().getData());
        }			
			
	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planVO) {
		
		// Convert employer tax identification number        
        final String taxId = uiVO.getEmployerTaxIdentificationNumber();       
        planVO.getGeneralInformations().setEmployerTaxIdentificationNumber(taxId);
        
		try {
			SimpleDateFormat sf = new SimpleDateFormat(DATE_PATTERN);
			if (StringUtils.isNotEmpty(uiVO.getPlanEffectiveDate())) {
				planVO.getGeneralInformations()
						.setPlanEffectiveDate(
								sf.parse(uiVO.getPlanEffectiveDate()));
			} else {
				planVO.getGeneralInformations().setPlanEffectiveDate(null);
			}
			
	        // Convert plan year end
	        if (StringUtils.isNotBlank(uiVO.getPlanYearEndString())) {
	        	planVO.getGeneralInformations().setPlanYearEnd(new DayOfYear(uiVO.getPlanYearEndString()));
	        } else {
	        	planVO.getGeneralInformations().setPlanYearEnd(null);
	        }			

		} catch (ParseException e) {
			// todo - do something here.
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
