package com.manulife.pension.ps.web.pif.obs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.pif.util.PIFAllowableMoneyTypesVOComparator;
import com.manulife.pension.service.pif.valueobject.PIFAllowableMoneyTypesVO;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoLoansVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;

/**
 * Observer class for Loans plan information data
 * @author 	rajenra
 */
public class LoansObserver implements Observer {

	 private PlanInfoLoansVO loans = new PlanInfoLoansVO(); 
	 
	/**
	 * @return the loans
	 */
	public PlanInfoLoansVO getLoans() {
		return loans;
	}

	/**
	 * the constructor
	 * @param PlanInfoLoansVO
	 */
	public LoansObserver(PlanInfoLoansVO loans) {
		super();
		this.loans = loans;
	}

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
        // Convert maximum loan percentage
        if (planInfoVO.getLoans().getMaximumLoanPercentage() != null) {
        	uiVO.setMaximumLoanPercentage(PlanData.formatBigDecimalPercentageFormatter(planInfoVO.getLoans()
                    .getMaximumLoanPercentage()));
        }

        // Convert loan interest rate above prime
        if (planInfoVO.getLoans().getLoanInterestRateAbovePrime() != null) {
        	uiVO.setLoanInterestRateAbovePrime(PlanData.formatBigDecimalPercentageFormatter(planInfoVO.getLoans()
                    .getLoanInterestRateAbovePrime()));
        }

        if (planInfoVO.getLoans().getMaximumNumberOfOutstandingLoans() != null) {
        	uiVO.setMaximumNumberOfOutstandingLoans(planInfoVO.getLoans()
                    .getMaximumNumberOfOutstandingLoans() == 0 ? 
        			 StringUtils.EMPTY : Integer.toString(planInfoVO.getLoans()
                             .getMaximumNumberOfOutstandingLoans().intValue()));
        }

	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
		
        if (StringUtils.isEmpty(uiVO.getMaximumNumberOfOutstandingLoans())) {
        	planInfoVO.getLoans().setMaximumNumberOfOutstandingLoans(0) ;
        }else{
        	planInfoVO.getLoans().setMaximumNumberOfOutstandingLoans(Integer.valueOf(uiVO.getMaximumNumberOfOutstandingLoans())) ;        	
        }
		
		if(uiVO.isSave()){
	    	//iterate the given list of loansList and remove all the 
			// unselected MoneyType in the Money type tab. Only selected money types are saved. 
			List<PIFAllowableMoneyTypesVO> loansList = planInfoVO.getLoans().getAllowedMoneyTypesForLoans();
			List<PIFAllowableMoneyTypesVO> loansSelectedList =  new ArrayList<PIFAllowableMoneyTypesVO>();	
			
			//Initial PIFAllowableMoneyTypesVO
			List<PIFAllowableMoneyTypesVO> initialLoansList = uiVO.getInitialPlanInfoVO().getLoans().getAllowedMoneyTypesForLoans();
			for(PIFAllowableMoneyTypesVO loansObj: loansList){
				//Check the money type that is available in initial PIFAllowableMoneyTypesVO.
				int index = initialLoansList.indexOf(loansObj);				
				if(index != -1){
					//If money type that is available in initial PIFAllowableMoneyTypesVO, No Need to check for selectedMoneyType
					loansSelectedList.add(loansObj);	
				}else{
					//Add only the money types that are selected in money type tab.
					if(loansObj.getSelectedMoneyType() != null && loansObj.getSelectedMoneyType()){
						loansSelectedList.add(loansObj);		
					}						
				}
			}			
			planInfoVO.getLoans().setAllowedMoneyTypesForLoans(loansSelectedList);
		}

	}

    /**
     * populate the selected Money Types in money type tab
     * 
     * @param List<PIFMoneyType>
     */
    private void createAllowedMoneyTypesForLoansTab(List<PIFMoneyType> permittedMoneyTypes){
    	
    	//List<PIFMoneyType> permittedMoneyTypes = getPermittedMoneyTypes();
    	List<PIFAllowableMoneyTypesVO> pifAllowableMoneyTypes = getLoans().getAllowedMoneyTypesForLoans();
    	
    	//iterate the given list of permittedMoneyTypes and set the selectedMoneyType if it is selected in the Money type tab
		for(PIFMoneyType pifMoneyType: permittedMoneyTypes){
				
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
		Collections.sort(pifAllowableMoneyTypes, new PIFAllowableMoneyTypesVOComparator(permittedMoneyTypes));    	
    }	

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
    public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate PlanInfoLoanssVO VO 
    	createAllowedMoneyTypesForLoansTab(list);
    	
	}

}
