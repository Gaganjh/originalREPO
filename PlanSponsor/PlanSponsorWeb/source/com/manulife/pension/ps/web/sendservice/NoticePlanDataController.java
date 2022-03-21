package com.manulife.pension.ps.web.sendservice;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility;
import com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.notices.valueobject.AutomaticContributionVO;
import com.manulife.pension.service.notices.valueobject.ContributionsAndDistributionsVO;
import com.manulife.pension.service.notices.valueobject.InvestmentInformationVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanDataVO;
import com.manulife.pension.service.notices.valueobject.SafeHarborVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

public class NoticePlanDataController {
    
	private static final Logger logger = Logger.getLogger(NoticePlanDataController.class);
	private static final String DATE_PATTERN = "MM/dd/yyyy";
	public static final String SHORT_MDY_DATE_FORMAT = "MM/dd";
	public static final FastDateFormat dateFormat = FastDateFormat
				.getInstance(SHORT_MDY_DATE_FORMAT);
	private static String automaticContributionDataCompleted = Constants.YES;
	 
	 /**
     * To set the tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form, selectedTab
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setValuesToForm(NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO, NoticePlanDataForm form, String selectedTab) throws SystemException{
    	logger.info("NoticePlanDataController setValuesToForm starts...");
    	if(Constants.SAFE_HARBOR.equalsIgnoreCase(selectedTab)){
            form = setSafeHarborValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        }
        else if(Constants.CONTRIBUTION_AND_DISTRIBUTION.equalsIgnoreCase(selectedTab)){
            form = setContriAndDistriValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        }        
        else if(Constants.AUTOMATIC_CONTRIBUTION.equalsIgnoreCase(selectedTab)){
        	form = setSafeHarborValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
            form = setAutoContributionValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        }
        else if(Constants.INVESTMENT_INFO.equalsIgnoreCase(selectedTab)){
            form = setInvInfoValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        }
        else{
        	form = setSafeHarborValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        	form = setContriAndDistriValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        	form = setAutoContributionValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);
        	form = setInvInfoValuesToForm(form, noticePlanDataVO, noticePlanCommonVO);        	
        }
    	logger.info("NoticePlanDataController setValuesToForm ends...");
        return form;
    }
    
    
    /**
     * This method will set the SH form values to SafeHarborVO to save in the database
     * @param form
     * @return SafeHarborVO
     * 
     */
    public SafeHarborVO setFormDataToSafeHarborVO(NoticePlanDataForm form) throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToSafeHarborVO starts...");
          SafeHarborVO safeHarborVO=new SafeHarborVO();
          if(form!=null){
              //SH type
        	  if(!PlanDataWebUtility.isNullOrEmpty(form.getPlanHasSafeHarborMatchOrNonElective())){
                  safeHarborVO.setPlanHasSafeHarborMatchOrNonElective(form.getPlanHasSafeHarborMatchOrNonElective().trim());
              }
              
              //SH match Contribution row1 pct1
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct1())){
                  safeHarborVO.setMatchContributionContribPct1(new BigDecimal(form.getMatchContributionContribPct1().trim()));
              }
              
              //if match contribution row2 pct1 values are empty/null or 0.0, set value = 0.0
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2())){
                  if(new BigDecimal(Double.parseDouble(form.getMatchContributionContribPct2())).compareTo(BigDecimal.ZERO) == 0){
                      safeHarborVO.setMatchContributionContribPct2(BigDecimal.ZERO);
                  }
                  else{
                      safeHarborVO.setMatchContributionContribPct2(new BigDecimal(form.getMatchContributionContribPct2().trim())); 
                  }
              }
              else{
                  safeHarborVO.setMatchContributionContribPct2(BigDecimal.ZERO);
              }
              
              //SH match Contribution row1 pct2
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct1())){
                  safeHarborVO.setMatchContributionMatchPct1(new BigDecimal(form.getMatchContributionMatchPct1().trim()));
              }
              
              //if match contribution row2 pct2 values are empty/null or 0.0, set value = 0.0
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())){
                  if(new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct2())).compareTo(BigDecimal.ZERO) == 0){
                      safeHarborVO.setMatchContributionMatchPct2(BigDecimal.ZERO);
                  }
                  else{
                      safeHarborVO.setMatchContributionMatchPct2(new BigDecimal(form.getMatchContributionMatchPct2().trim())); 
                  }
              }
              else{
                  safeHarborVO.setMatchContributionMatchPct2(BigDecimal.ZERO);
              }
              
              //SH match Contribution applies to
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchAppliesToContrib())){
                  safeHarborVO.setMatchAppliesToContrib(form.getMatchAppliesToContrib().trim());
              }
              
              //SH match another Plan
              if(!PlanDataWebUtility.isNullOrEmpty(form.getMatchContributionToAnotherPlan())){
                  safeHarborVO.setMatchContributionToAnotherPlan(form.getMatchContributionToAnotherPlan().trim());
                  if(Constants.YES.equals(form.getMatchContributionToAnotherPlan().trim())){
                      safeHarborVO.setMatchContributionOtherPlanName(form.getMatchContributionOtherPlanName());
                  }
                  else{
                      safeHarborVO.setMatchContributionOtherPlanName(null);
                  }
              }
              
              if(!PlanDataWebUtility.isNullOrEmpty(form.getSafeHarborAppliesToRoth())){
                  safeHarborVO.setSafeHarborAppliesToRoth(form.getSafeHarborAppliesToRoth().trim());
              }
              if(!PlanDataWebUtility.isNullOrEmpty(form.getsHAppliesToCatchUpContributions())){
                  safeHarborVO.setsHAppliesToCatchUpContributions(form.getsHAppliesToCatchUpContributions().trim());
              }
              
              //Non-elective contribution option
              if(!PlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOption())){
                  safeHarborVO.setNonElectiveContribOption(form.getNonElectiveContribOption().trim());
              }
              
              //if non elective contribution pct value is empty/null or 0.0, set value = 0.0
              if(!PlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContributionPct())){
                  if(new BigDecimal(Double.parseDouble(form.getNonElectiveContributionPct())).compareTo(BigDecimal.ZERO) == 0){
                      safeHarborVO.setNonElectiveContributionPct(BigDecimal.ZERO);
                  }
                  safeHarborVO.setNonElectiveContributionPct(new BigDecimal(form.getNonElectiveContributionPct().trim()));
              }else{
                  safeHarborVO.setNonElectiveContributionPct(BigDecimal.ZERO);
              }
              
              //NE contribution applies to Employees
              if(!PlanDataWebUtility.isNullOrEmpty(form.getNonElectiveAppliesToContrib())){
                  safeHarborVO.setNonElectiveAppliesToContrib(form.getNonElectiveAppliesToContrib().trim());
              }
              if(!PlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOtherPlan())){
                  safeHarborVO.setNonElectiveContribOtherPlan(form.getNonElectiveContribOtherPlan().trim());
                  if(Constants.YES.equals(form.getNonElectiveContribOtherPlan().trim())){
                      safeHarborVO.setNonElectiveContribOtherPlanName(form.getSHNonElectivePlanName());
                  }
                  else{
                      safeHarborVO.setNonElectiveContribOtherPlanName(null);
                  }
              }
              
              //Additional Employer Contribution              
              if(!PlanDataWebUtility.isNullOrEmpty(form.getPlanHasAdditionalEC())){
                  safeHarborVO.setPlanHasAdditionalEmpContribution(form.getPlanHasAdditionalEC().trim());
                  if(Constants.YES.equals(form.getPlanHasAdditionalEC().trim())){
                      safeHarborVO.setAdditionalEmpContribSPDDescription(form.getSummaryPlanDesc());
                  }
                  else{
                      safeHarborVO.setAdditionalEmpContribSPDDescription(null);
                  }
              }
              
              safeHarborVO.setDataCompleteInd(form.getSafeHarborDataCompleteInd());
              safeHarborVO.setLastUpdatedTimeStamp(new Date());
          }
          logger.info("NoticePlanDataController setFormDataToSafeHarborVO ends...");
          return safeHarborVO;
    }
    
    

    /**
     * Method used to copy form data to  ContributionsAndDistributionsVO
     * @param form
     * @return ContributionsAndDistributionsVO
     */
    public ContributionsAndDistributionsVO setFormDataToContributionsAndDistributionsVO(NoticePlanDataForm form,NoticePlanCommonVO noticePlanCommonVO) throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToContributionsAndDistributionsVO starts...");
    	// Map form field values to ContributionsAndDistributionsVO
    	ContributionsAndDistributionsVO contributionsAndDistributionsVO=new ContributionsAndDistributionsVO();
    	if(!PlanDataWebUtility.isNull(form.getMaxEmployeeBeforeTaxDeferralAmt()))
    	contributionsAndDistributionsVO.setMaxEmployeeBeforeTaxDeferralAmt(form.getMaxEmployeeBeforeTaxDeferralAmt());
    	if(!PlanDataWebUtility.isNull(form.getMaxEmployeeBeforeTaxDeferralPct()))
        contributionsAndDistributionsVO.setMaxEmployeeBeforeTaxDeferralPct(form.getMaxEmployeeBeforeTaxDeferralPct());
    	if(!PlanDataWebUtility.isNullOrEmpty(form.getPlanAllowRothDeferrals()))
        contributionsAndDistributionsVO.setPlanAllowRothDeferrals(form.getPlanAllowRothDeferrals().trim());
        if((!PlanDataWebUtility.isNullOrEmpty(form.getPlanAllowRothDeferrals()))&&(form.getPlanAllowRothDeferrals().equalsIgnoreCase(Constants.YES)) )
        {
        	  if(!PlanDataWebUtility.isNull(form.getMaxEmployeeRothDeferralsAmt()))
        	  contributionsAndDistributionsVO.setMaxEmployeeRothDeferralsAmt(form.getMaxEmployeeRothDeferralsAmt());
        	  if(!PlanDataWebUtility.isNull(form.getMaxEmployeeRothDeferralsPct()))
        	  contributionsAndDistributionsVO.setMaxEmployeeRothDeferralsPct(form.getMaxEmployeeRothDeferralsPct());
        }else
        {
        	  contributionsAndDistributionsVO.setMaxEmployeeRothDeferralsAmt(null);
              contributionsAndDistributionsVO.setMaxEmployeeRothDeferralsPct(null);
        }
        if(!PlanDataWebUtility.isNull(form.getContirbutionRestirictionOnHardships()))
	    contributionsAndDistributionsVO.setContirbutionRestirictionOnHardships(form.getContirbutionRestirictionOnHardships());    
        if(!PlanDataWebUtility.isNullOrEmpty(form.getSpdEmployeeContributionRef()))
        contributionsAndDistributionsVO.setSpdEmployeeContributionRef(form.getSpdEmployeeContributionRef().trim());
        if(!PlanDataWebUtility.isNullOrEmpty(form.getPlanAllowsInServiceWithdrawals()))
        contributionsAndDistributionsVO.setPlanAllowsInServiceWithdrawals(form.getPlanAllowsInServiceWithdrawals().trim());           
        if(!PlanDataWebUtility.isNullOrEmpty(form.getContriAndDistriDataCompleteInd()))
        contributionsAndDistributionsVO.setDataCompleteInd(form.getContriAndDistriDataCompleteInd().trim());
      
        contributionsAndDistributionsVO.setLastUpdatedTimeStamp(new Date());
        logger.info("NoticePlanDataController setFormDataToContributionsAndDistributionsVO ends...");   
    	return contributionsAndDistributionsVO;
    }
    /**
     * Method to clear the contributions values from form
     * @param form
     * @return
     */
    
    public NoticePlanDataForm clearContributionsValuesFromForm(NoticePlanDataForm form){
    	  
    	form.setSpdEmployeeContributionRef(null);
    	form.setPlanAllowRothDeferrals(null);
    	form.setPlanAllowsInServiceWithdrawals(null);
    	form.setContirbutionRestirictionOnHardships(null);
        return form;
    }
    
    
    
    /**
     * Method used to copy form data to eaca AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToEACAAutomaticContributionVO(NoticePlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException
    {
    	 logger.info("NoticePlanDataController setFormDataToEACAAutomaticContributionVO starts...");
    	 if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().trim().equalsIgnoreCase(Constants.EACA))
	     {
	       if(!PlanDataWebUtility.isNull(form.getAutomaticContributionDays()))
	        {	        
	    	   if(form.getAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
	    	   {
	    		if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDaysOther()))
	  			{
	    			automaticContributionVO.seteACAContributionDays(Integer.parseInt(form.getAutomaticContributionDaysOther()));	
	  			}
	    		
	    	   }else
	    	   {
	    		   automaticContributionVO.seteACAContributionDays(Integer.parseInt(form.getAutomaticContributionDays()));
	    	   }
	       }
	       
	       if(!PlanDataWebUtility.isNullOrEmpty(form.getEacaPlanHasAutoContribWD())){
	    	   if(Constants.YES.equalsIgnoreCase(form.getEacaPlanHasAutoContribWD().trim())){
	    		   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(Constants.YES);
	    	   }
	    	   else if(Constants.NO.equalsIgnoreCase(form.getEacaPlanHasAutoContribWD().trim())){
	    		   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(Constants.NO);
	    		   automaticContributionVO.seteACAContributionDays(null);
	    	   }  
	       }
	       else{
	    	   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(null);
	    	   automaticContributionVO.seteACAContributionDays(null);
	       }
	       
	       if(!PlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()))
	        {
	    	automaticContributionVO.seteACAPlanHasEmpoyerContribution(form.getEmployerContributions().trim());
	    	if(form.getEmployerContributions().equalsIgnoreCase(Constants.YES))
	    	{
	        if(!PlanDataWebUtility.isNullOrEmpty(form.getSpdEmployerContributionRef()))
	        {
	    	automaticContributionVO.seteACASPDEmployeeContRef(form.getSpdEmployerContributionRef().trim());
	        }
	       }
	      }
	      
	    }
    	 logger.info("NoticePlanDataController setFormDataToEACAAutomaticContributionVO ends...");
    	return automaticContributionVO;
    }
    /**
     * Method used to copy form data to  qaca AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToQACAAutomaticContributionVO(NoticePlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToQACAAutomaticContributionVO starts...");
    	if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().trim().equalsIgnoreCase(Constants.QACA))
        {
    		
    		// QACA SH Match section
     	       if(!PlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct1()))
   	        {
     	    	automaticContributionVO.setqACASHMACMatchContributionContribPct1(form.getqACAMatchContributionContribPct1());
   	        }
     	       if(!PlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))   	    	   
   	        {
     	    	 if(form.getqACAMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0){
     	    		automaticContributionVO.setqACASHMACMatchContributionContribPct2(BigDecimal.ZERO);
               }
               else{
              
     	    	automaticContributionVO.setqACASHMACMatchContributionContribPct2(form.getqACAMatchContributionContribPct2());
               }
   	        }else
   	        {
   	        	automaticContributionVO.setqACASHMACMatchContributionContribPct2(BigDecimal.ZERO);
   	        }
     	       if(!PlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1()))
   	        {
     	    	automaticContributionVO.setqACASHMACMatchContributionMatchPct1(form.getqACAMatchContributionMatchPct1());
   	        }
   	  	   if(!PlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))
   	       {
   	  		 if(form.getqACAMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0){
      	    		automaticContributionVO.setqACASHMACMatchContributionMatchPct2(BigDecimal.ZERO);
                }
                else{
   	  		automaticContributionVO.setqACASHMACMatchContributionMatchPct2(form.getqACAMatchContributionMatchPct2());
                }
   	       }else
   	       {
   	    	  automaticContributionVO.setqACASHMACMatchContributionMatchPct2(BigDecimal.ZERO); 
   	       }
   	  	  if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
   	       {
   	  		automaticContributionVO.setqACAMatchingContribOtherPlan(form.getqACAMatchContributionToAnotherPlan().trim());
   	       }
   	  	  if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
   	       {
   	  		automaticContributionVO.setqACAMatchingContribOtherPlan(form.getqACAMatchContributionToAnotherPlan().trim());
   	  		if(form.getqACAMatchContributionToAnotherPlan().trim().equalsIgnoreCase(Constants.YES))
   	  		{
   	  	  if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionOtherPlanName()))
  	       {
  	  		automaticContributionVO.setqACAMatchingContribOtherPlanName(form.getqACAMatchContributionOtherPlanName().trim());
  	       }
   	  		}
   	       }
   	  	
   	  	if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
  	       {
   	  		if(form.getqACAAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
     	    	   {
   	  			if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDaysOther()))
   	  			{
     	    		automaticContributionVO.setQACAAutoContribWithdrawaldays(Integer.parseInt(form.getqACAAutomaticContributionDaysOther()));
   	  			}
     	    	   }else
     	    	   {
     	    		automaticContributionVO.setQACAAutoContribWithdrawaldays(Integer.parseInt(form.getqACAAutomaticContributionDays()));
     	    	   }
   	      
  	       }
   	  	if(!PlanDataWebUtility.isNullOrEmpty(form.getqACASafeHarborAppliesToRoth()))
   	       {
   	  		automaticContributionVO.setqACAIsSHMatchToRoth(form.getqACASafeHarborAppliesToRoth().trim());
   	       }
   	  	if(!PlanDataWebUtility.isNullOrEmpty(form.getqACASHAppliesToCatchUpContributions()))
   	       {
   	  		automaticContributionVO.setqACAIsSHMatchToCatchUp(form.getqACASHAppliesToCatchUpContributions().trim());
   	       }
   	  	// QACA SH Non elective contribution section
   	  	if(!PlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
   	       {
   	  	 if(form.getqACANonElectiveContributionPct().compareTo(BigDecimal.ZERO) == 0){
  	    		automaticContributionVO.setqACANonElectiveContributionPct(BigDecimal.ZERO);
            }
            else{
   	  		automaticContributionVO.setqACANonElectiveContributionPct(form.getqACANonElectiveContributionPct());
            }
   	       }else
   	       {
   	    	  automaticContributionVO.setqACANonElectiveContributionPct(BigDecimal.ZERO);  
   	       }
   	  	if(!PlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
   	       {
   	  		automaticContributionVO.setqACANonElectiveContributionPct(form.getqACANonElectiveContributionPct());
   	       }
   	  	if(!PlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveAppliesToContrib()))
   	       {
   	  		automaticContributionVO.setqACANonElectiveContributionOptions(form.getqACANonElectiveAppliesToContrib());
   	       }
   	  	
   		if(!PlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan()))
   	       {
   			automaticContributionVO.setqACANonElectiveContribOtherPlan(form.getqACANonElectiveContribOtherPlan().trim());
   			if(form.getqACANonElectiveContribOtherPlan().trim().equalsIgnoreCase(Constants.YES))
   			{
   			if(!PlanDataWebUtility.isNullOrEmpty(form.getqACASHNonElectivePlanName()))
    	       {
    			automaticContributionVO.setqACANonElectiveContribOtherPlanName(form.getqACASHNonElectivePlanName());
    	       }
   			}
   	       }
   		
   		if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
   	       {
   			//automaticContributionVO.setqACAisShmacOrShnec(form.getqACAPlanHasSafeHarborMatchOrNonElective());
   			automaticContributionVO.setqACAMatchOrNonElectiveCode(form.getqACAPlanHasSafeHarborMatchOrNonElective());
   	       }
   		if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC()))
   	       {
   			automaticContributionVO.setqACAAutomaticContribWithdrawal(form.getqACAPlanHasAdditionalEC().trim());
   	       }
   		if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()))
   	       {
   			automaticContributionVO.setqACAPlanHasEmpoyerContribution(form.getqACAPlanHasAdditionalECon().trim());
   			if(form.getqACAPlanHasAdditionalECon().trim().equalsIgnoreCase(Constants.YES))
   			{
   			if(!PlanDataWebUtility.isNullOrEmpty(form.getqACASummaryPlanDesc()))
    	       {
    			automaticContributionVO.setqACASPDEmpContribReference(form.getqACASummaryPlanDesc().trim());
    	       }
   			}
   	       }
   		if(!PlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
   	       {
   			automaticContributionVO.setQacaFullyVested(form.getqACASHMatchVesting().trim());
   			if(form.getqACASHMatchVesting().trim().equalsIgnoreCase(Constants.NO))
   			{
   			if(!PlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))
    	       {
    			automaticContributionVO.setqACAVestingLessThan1YearPct(form.getqACASHMatchVestingPct1());
    	       }
    		if(!PlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2()))
    	       {
    			automaticContributionVO.setaACAVesting1To2YearPct(form.getqACASHMatchVestingPct2());
    	       }
   			}
   	       }
   		
   	   if(!PlanDataWebUtility.isNullOrEmpty(form.getqACAArrangementOptions()))
       {
		automaticContributionVO.setqACAArrangementOptions(form.getqACAArrangementOptions());
       }

   	   }
    	logger.info("NoticePlanDataController setFormDataToQACAAutomaticContributionVO ends..."); 
      
    	return automaticContributionVO;
    }
    /**
     * Method used to copy form data to  AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToAutomaticContributionVO(NoticePlanDataForm form)throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToAutomaticContributionVO starts...");
    	// Map form field values to AutomaticContributionVO
    	AutomaticContributionVO automaticContributionVO=new AutomaticContributionVO();
    	
    	 if(!PlanDataWebUtility.isNull(form))
   	    {
   	        if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType()))
   	        {
   	        	automaticContributionVO.setAutomaticEnrollmentType(form.getAutomaticContributionProvisionType().trim());
   	        }
   	     if(!PlanDataWebUtility.isNull(form.getAutomaticContributionFeature1())&&form.getAutomaticContributionFeature1().equalsIgnoreCase("1"))
	        {
   	        if(!PlanDataWebUtility.isNull(form.getContributionFeature1Pct()))
   	        {
   	        	automaticContributionVO.setaCAContibLessPercentage(form.getContributionFeature1Pct());
   	        }
	        }else
	        {
	        	automaticContributionVO.setaCAContibLessPercentage(null);	
	        }
   	  if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature2())&&form.getAutomaticContributionFeature2().equalsIgnoreCase("2"))
      {
   	        if(!PlanDataWebUtility.isNullOrEmpty(form.getContributionFeature2Date()))   	        	
   	        {
   	        	
   	   	     DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
   	   	     Date currentDate;
			try {
				currentDate = format.parse(form.getContributionFeature2Date());
				automaticContributionVO.setaCAHiredAfterDate(currentDate);
			} catch (ParseException e) {
				logger.error("ParseException "+e.getMessage());
			}
   	        	
   	        }
      }else
      {
    	  automaticContributionVO.setaCAHiredAfterDate(null); 
      }
   	  if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature3())&&form.getAutomaticContributionFeature3().equalsIgnoreCase("3"))
 	    {
 	        if(!PlanDataWebUtility.isNullOrEmpty(form.getContributionFeature3SummaryText()))
	        {
	        	automaticContributionVO.setaCAAppliesToCustom(form.getContributionFeature3SummaryText());
	        }
 	    }else
 	    {
 	    	automaticContributionVO.setaCAAppliesToCustom(null);
 	    }
   	
   	        if(!PlanDataWebUtility.isNullOrEmpty(form.getAciAllowed()))
	        {	
	        	//automaticContributionVO.setaCAContributionIncrease(form.getAciAllowed().trim());
	        	if(form.getAciAllowed().equalsIgnoreCase(Constants.YES))
	        	{
	        	  if(!PlanDataWebUtility.isNull(form.getAnnualIncrease()))
	     	        {
	        		  // TODO Need to do the same changes from plan data to send service
	     	        //	automaticContributionVO.setaCAAnnualIncreasePct(form.getAnnualIncrease());
	     	        }

	     	        if(!PlanDataWebUtility.isNull(form.getMaxAutomaticIncrease()))
	     	        {
		        		  // TODO Need to do the same changes from plan data to send service
	     	        //	automaticContributionVO.setaCAAnnualIncreaseMaxPct(form.getMaxAutomaticIncrease());
	     	        }
	     	        
					if (!PlanDataWebUtility.isNullOrEmpty(form
							.getAciApplyDate())) {
						Calendar localCalendar = Calendar.getInstance(TimeZone
								.getDefault());
						String presentYear = String.valueOf(localCalendar
								.get(Calendar.YEAR));
						String aciDate = form.getAciApplyDate().concat("/")
								.concat(presentYear);
						DateFormat format = new SimpleDateFormat(DATE_PATTERN);

						try {
							Date curyear = format.parse(aciDate);
							 // TODO Need to do the same changes from plan data to send service
							//automaticContributionVO.setaCAannualIncreaseDate(curyear);
						} catch (ParseException e) {
							logger.error("ParseException " + e.getMessage());
						}
					}
	        	}
	        }
   	     // EACA fields
   	     automaticContributionVO=setFormDataToEACAAutomaticContributionVO(form,automaticContributionVO);
   	    // QACA fields 
   	     automaticContributionVO=setFormDataToQACAAutomaticContributionVO(form,automaticContributionVO);
   	  	if(!PlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDataCompleteInd()))
	    {
			automaticContributionVO.setDataCompleteInd(form.getAutomaticContributionDataCompleteInd());
	    } 
         automaticContributionVO.setLastUpdatedTimeStamp(new Date());
   	    }
    	 logger.info("NoticePlanDataController setFormDataToAutomaticContributionVO ends...");
    	return automaticContributionVO;
    }
    
    
    

    
    
    
    /**
     * To set the Contribution and Distribution tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setContriAndDistriValuesToForm(NoticePlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    	logger.info("NoticePlanDataController setContriAndDistriValuesToForm starts...");
        // Contributions and distributions Tab value mapping to form from ContributionsAndDistributionsVO
        ContributionsAndDistributionsVO contributionsAndDistributionsVO=noticePlanDataVO.getContributionsAndDistributionsVO();         
        if(!PlanDataWebUtility.isNull(contributionsAndDistributionsVO))
        {
        	
        	if(!PlanDataWebUtility.isNull(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships()))
            {
                form.setContirbutionRestirictionOnHardships(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships());
            }
            if(!PlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getSpdEmployeeContributionRef()))
            {
                form.setSpdEmployeeContributionRef(contributionsAndDistributionsVO.getSpdEmployeeContributionRef().trim());
            }
            if(!PlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getPlanAllowRothDeferrals()))
            {
                form.setPlanAllowRothDeferrals(contributionsAndDistributionsVO.getPlanAllowRothDeferrals().trim());
            }
            if(!PlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getPlanAllowsInServiceWithdrawals()))
            {
                form.setPlanAllowsInServiceWithdrawals(contributionsAndDistributionsVO.getPlanAllowsInServiceWithdrawals());
            }
            if(PlanDataWebUtility.isNull(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships()))
            {

                form.setContirbutionRestirictionOnHardships(6);
            }else
            {
                form.setContirbutionRestirictionOnHardships(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships());
            }
              
           if(!PlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getDataCompleteInd()))
            {
                form.setContriAndDistriDataCompleteInd(contributionsAndDistributionsVO.getDataCompleteInd().trim());
            }
        }else
        {
            form.setContirbutionRestirictionOnHardships(6);
             
        }
        logger.info("NoticePlanDataController setContriAndDistriValuesToForm ends...");
        return form;
    }
    
    
    /**
     * To set the SH tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setSafeHarborValuesToForm(NoticePlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    	logger.info("NoticePlanDataController setSafeHarborValuesToForm starts...");
      //setting SafeHarbor VO values to form
        SafeHarborVO safeHarborVO = noticePlanDataVO.getSafeHarborVO();
        if(noticePlanDataVO.getSafeHarborVO() != null){
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasSafeHarborMatchOrNonElective())){
                form.setPlanHasSafeHarborMatchOrNonElective(safeHarborVO.getPlanHasSafeHarborMatchOrNonElective().trim());
            }
           
            //setting Match values - Contribution Pct 1
            if(safeHarborVO.getMatchContributionContribPct1()!=null){
                form.setMatchContributionContribPct1(safeHarborVO.getMatchContributionContribPct1().toString());
            }
            else{
                form.setMatchContributionContribPct1(new BigDecimal(100).toString());
            }

            //Contribution Match Pct 1
            if(safeHarborVO.getMatchContributionMatchPct1()!=null){
                form.setMatchContributionMatchPct1(safeHarborVO.getMatchContributionMatchPct1().toString());
            }else{
                form.setMatchContributionMatchPct1(new BigDecimal(3).toString());
            }

            //Contribution Pct 2
            if(safeHarborVO.getMatchContributionContribPct2()!=null){
                if(safeHarborVO.getMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0){
                    form.setMatchContributionContribPct2(StringUtils.EMPTY);
                }
                else{
                    form.setMatchContributionContribPct2(safeHarborVO.getMatchContributionContribPct2().toString()); 
                }
                
            }
            else{
                form.setMatchContributionContribPct2(new BigDecimal(50).toString());
            }

            //Contribution Match Pct 2
            if(safeHarborVO.getMatchContributionMatchPct2()!=null){
                if(safeHarborVO.getMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0){
                    form.setMatchContributionMatchPct2(StringUtils.EMPTY);
                }
                else{
                    form.setMatchContributionMatchPct2(safeHarborVO.getMatchContributionMatchPct2().toString());
                }
                
            }
            else{
                form.setMatchContributionMatchPct2(new BigDecimal(2).toString());
            }            
            
            //SHContributionAppliesToEmployees
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchAppliesToContrib())){
                form.setMatchAppliesToContrib(safeHarborVO.getMatchAppliesToContrib().trim());
            }

            //MatchContributionToOtherplan
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchContributionToAnotherPlan())){
                form.setMatchContributionToAnotherPlan(safeHarborVO.getMatchContributionToAnotherPlan().trim());
            }
            else{
                form.setMatchContributionToAnotherPlan(Constants.NO);
            }

            //MatchContributionOtherPlanName
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchContributionOtherPlanName())){
                form.setMatchContributionOtherPlanName(safeHarborVO.getMatchContributionOtherPlanName().trim());
            }

            //SHappliesToRoth
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getSafeHarborAppliesToRoth())){
                form.setSafeHarborAppliesToRoth(safeHarborVO.getSafeHarborAppliesToRoth().trim());
            }

            //SHappliesToCatchUpContributions
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getsHAppliesToCatchUpContributions())){
                form.setsHAppliesToCatchUpContributions(safeHarborVO.getsHAppliesToCatchUpContributions().trim());
            }

            //setting NE values
            //SHNEContribution option
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOption())){
                form.setNonElectiveContribOption(safeHarborVO.getNonElectiveContribOption().trim());
            }

            //Non-Elective Percent
            if(safeHarborVO.getNonElectiveContributionPct() != null){
                if(safeHarborVO.getNonElectiveContributionPct().compareTo(BigDecimal.ZERO) == 0){
                    form.setNonElectiveContributionPct(StringUtils.EMPTY);
                }
                else{
                    form.setNonElectiveContributionPct(safeHarborVO.getNonElectiveContributionPct().toString());
                }
                
            }
            else{
                form.setNonElectiveContributionPct(new BigDecimal(3).toString());
            }

            //SHNEContributionAppliesToEmployees
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveAppliesToContrib())){
                form.setNonElectiveAppliesToContrib(safeHarborVO.getNonElectiveAppliesToContrib().trim());
            }

            //SHNEContributionToOtherPlan
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOtherPlan())){
                form.setNonElectiveContribOtherPlan(safeHarborVO.getNonElectiveContribOtherPlan().trim());
            }
            else{
                form.setNonElectiveContribOtherPlan(Constants.NO);
            }

            //SHNEContributionOtherPlanName
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOtherPlanName())){
                form.setSHNonElectivePlanName(safeHarborVO.getNonElectiveContribOtherPlanName().trim());
            }
            
            //SHNEContributionApplicableToPlan
			if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp())) {
				if(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.YES)) {
					form.setEnablePlanYearEndDateAndPercentageComp(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp());
					if(!PlanDataWebUtility.isNull(safeHarborVO.getContributionApplicableToPlanDate())) {
						Date effectiveDate = safeHarborVO.getContributionApplicableToPlanDate();
						form.setContributionApplicableToPlanDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
					}
					else {
						form.setContributionApplicableToPlanDate(null);
					}
					if(!PlanDataWebUtility.isNull(safeHarborVO.getContributionApplicableToPlanPct())) {
						form.setContributionApplicableToPlanPct(safeHarborVO.getContributionApplicableToPlanPct());
					}
					else {
						form.setContributionApplicableToPlanPct(null);
					}
				}
				else if(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.NO)) {
					form.setEnablePlanYearEndDateAndPercentageComp(Constants.NO);
					form.setContributionApplicableToPlanDate(null);
					form.setContributionApplicableToPlanPct(null);
				}
			}
			else {
				form.setEnablePlanYearEndDateAndPercentageComp(null);
				form.setContributionApplicableToPlanDate(null);
				form.setContributionApplicableToPlanPct(null);
			}
			
			//SH applies to Automatic contribution arrangements
			//CR011 START
			if(!PlanDataWebUtility.isNull(noticePlanCommonVO)&&!PlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
			{
				Date effectiveDate = noticePlanCommonVO.getAutomaticContributionEffectiveDate();
				form.setEffectiveDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
			}
			
			if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasSHACA())){
				form.setPlanHasSHACA(safeHarborVO.getPlanHasSHACA().trim());
			}else{
				form.setPlanHasSHACA(Constants.NO);
			}
			
			if(!PlanDataWebUtility.isNull(safeHarborVO.getShContributionFeature1Pct())){
				form.setShContributionFeature1Pct(safeHarborVO.getShContributionFeature1Pct());
				form.setShContributionFeature1PctMissing(true);
			}else{
				form.setShContributionFeature1PctMissing(false);
			}
			
			if(!PlanDataWebUtility.isNull(safeHarborVO.getShContributionFeature2Date())) {
				Date effectiveDate = safeHarborVO.getShContributionFeature2Date();
				form.setShContributionFeature2Date(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
				form.setShContributionFeature2DateIdMissing(true);
			}
			else {
				form.setShContributionFeature2Date(null);
				form.setShContributionFeature2DateIdMissing(false);
			}
			
			if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getShContributionFeature3SummaryText())){
				form.setShContributionFeature3SummaryText(safeHarborVO.getShContributionFeature3SummaryText().trim());
				form.setShContributionFeature3SummaryTextMissing(true);
			}else{
				form.setShContributionFeature3SummaryTextMissing(false);
			}
			
			if(!PlanDataWebUtility.isNull(safeHarborVO.getShACAAnnualIncreaseType()))
			{
				form.setShACAAnnualIncreaseType(safeHarborVO.getShACAAnnualIncreaseType().trim());
			}
			
			if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getSHAutoContributionWD())){
				form.setSHAutoContributionWD(safeHarborVO.getSHAutoContributionWD().trim());
			}
			
			if(!PlanDataWebUtility.isZero(safeHarborVO.getSHAutomaticContributionDays()))
    		{    
    			if(safeHarborVO.getSHAutomaticContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_90)
    					||safeHarborVO.getSHAutomaticContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_60)
    					||safeHarborVO.getSHAutomaticContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_30))
    			{   
    				form.setSHAutomaticContributionDays(Integer.toString(safeHarborVO.getSHAutomaticContributionDays()));
    			}else
    			{
    				form.setSHAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_OTHER);
    				if(safeHarborVO.getSHAutomaticContributionDays()!=0)
    				{
    					form.setSHAutomaticContributionDaysOther(Integer.toString(safeHarborVO.getSHAutomaticContributionDays()));
    				}else
    				{
    					form.setSHAutomaticContributionDaysOther(null);	
    				}
    			}
    		}else
    		{
    			form.setSHAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_90);
    		}
			//CR011 END
            
            //SH applies to AdditionalEmployeeContribution
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasAdditionalEmpContribution())){
                form.setPlanHasAdditionalEC(safeHarborVO.getPlanHasAdditionalEmpContribution().trim());
            }
            
            //SH applies to AdditionalEmployeeContribution
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getAdditionalEmpContribSPDDescription())){
                form.setSummaryPlanDesc(safeHarborVO.getAdditionalEmpContribSPDDescription().trim());
            }
            
            if(!PlanDataWebUtility.isNullOrEmpty(safeHarborVO.getDataCompleteInd())){
                form.setSafeHarborDataCompleteInd(safeHarborVO.getDataCompleteInd());
            }
            
            if(!PlanDataWebUtility.isNull(noticePlanCommonVO.getVestingSchedules())){
                form.setExcludeCount(noticePlanCommonVO.getVestingSchedules().size());
            }
        }
        else{
            //When there is no data in DB, set the default values
            form.setMatchContributionContribPct1(new BigDecimal(100).toString());
            form.setMatchContributionMatchPct1(new BigDecimal(3).toString());
            form.setMatchContributionContribPct2(new BigDecimal(50).toString());
            form.setMatchContributionMatchPct2(new BigDecimal(2).toString());
            form.setMatchAppliesToContrib(Constants.ALL_ELIGIBLE_EMP);
            form.setMatchContributionToAnotherPlan(Constants.NO);
            form.setNonElectiveContributionPct(new BigDecimal(3).toString());
            form.setNonElectiveAppliesToContrib(Constants.ALL_ELIGIBLE_EMP);
            form.setNonElectiveContribOtherPlan(Constants.NO);
            form.setSafeHarborDataCompleteInd(Constants.NO);
        }
        logger.info("NoticePlanDataController setSafeHarborValuesToForm ends...");
        return form;    
    }
    
    /**
     * To set the eaca auto tab data values from database to form
     * @param automaticContributionVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setEACAValuesToForm(NoticePlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException{
    	logger.info("NoticePlanDataController setEACAValuesToForm starts...");
    	if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals())){
			if(Constants.YES.equalsIgnoreCase(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals().trim())){
				form.setEacaPlanHasAutoContribWD(Constants.YES);
			}
			else if(Constants.NO.equalsIgnoreCase(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals().trim())){
				form.setEacaPlanHasAutoContribWD(Constants.NO);
			}
			
		}
        if(!PlanDataWebUtility.isNull(automaticContributionVO.geteACAContributionDays()))
        {    
           if(automaticContributionVO.geteACAContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_90)||automaticContributionVO.geteACAContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_60)||automaticContributionVO.geteACAContributionDays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_30))
           {   
            form.setAutomaticContributionDays(Integer.toString(automaticContributionVO.geteACAContributionDays()));
           }else
           {
            form.setAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_OTHER);
            if(automaticContributionVO.geteACAContributionDays()!=0)
            {
            form.setAutomaticContributionDaysOther(Integer.toString(automaticContributionVO.geteACAContributionDays()));
            }else
            {
            	form.setAutomaticContributionDaysOther(null);	
            }
           }
        }else
        {
             form.setAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_90);
        }
       if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACAPlanHasEmpoyerContribution()))
        {
            form.setEmployerContributions(automaticContributionVO.geteACAPlanHasEmpoyerContribution().trim());
        }
       if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACASPDEmployeeContRef()))
        {
            form.setSpdEmployerContributionRef(automaticContributionVO.geteACASPDEmployeeContRef().trim());
        }
       logger.info("NoticePlanDataController setEACAValuesToForm ends...");
    	return form;
    }
   
    /**
     * To set the qaca auto tab data values from database to form
     * @param automaticContributionVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setQACAValuesToForm(NoticePlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException{
    	logger.info("NoticePlanDataController setQACAValuesToForm starts...");
    	// QACA SH Match contribution values
      /*  if(!PlanDataWebUtility.isNull(automaticContributionVO.getaCAannualIncreaseDate()))
        {
            Date aciApplyDate = automaticContributionVO.getaCAannualIncreaseDate();
            form.setAciApplyDate(null!=aciApplyDate?new SimpleDateFormat(SHORT_MDY_DATE_FORMAT).format(aciApplyDate):"");
        }*/
        if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct1()))
        {
           form.setqACAMatchContributionContribPct1(automaticContributionVO.getqACASHMACMatchContributionContribPct1());
        }else
        {
     	   form.setqACAMatchContributionContribPct1(new BigDecimal(100));  
        }
        if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct2()))
        {
        if(automaticContributionVO.getqACASHMACMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0){
            form.setqACAMatchContributionContribPct2(null);
        }else
        {
           form.setqACAMatchContributionContribPct2(automaticContributionVO.getqACASHMACMatchContributionContribPct2());
        }
        }else
        {
            form.setqACAMatchContributionContribPct2(new BigDecimal(50));   
        }
        if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct1()))
        {
           form.setqACAMatchContributionMatchPct1(automaticContributionVO.getqACASHMACMatchContributionMatchPct1());
           if((!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct2())&&(automaticContributionVO.getqACASHMACMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 1)) ||((!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct2()))&&automaticContributionVO.getqACASHMACMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 1))
           {                
    		   form.setqACAMatchContributionMatchPct1Value(automaticContributionVO.getqACASHMACMatchContributionMatchPct1());
           }else
           {
        	   if(automaticContributionVO.getqACASHMACMatchContributionMatchPct1().compareTo(new BigDecimal(4))==-1)
               {
        		   form.setqACAMatchContributionMatchPct1Value(automaticContributionVO.getqACASHMACMatchContributionMatchPct1());
               }
        		   
           }
        }else
        {
            form.setqACAMatchContributionMatchPct1(new BigDecimal(1));  
            form.setqACAMatchContributionMatchPct1Value(new BigDecimal(1));
        }
        if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct2()))
        {
         if(automaticContributionVO.getqACASHMACMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0){
             form.setqACAMatchContributionMatchPct2(null);
         }else
         {
         	form.setqACAMatchContributionMatchPct2(automaticContributionVO.getqACASHMACMatchContributionMatchPct2());
         }
	    }else
	    {
	     	  form.setqACAMatchContributionMatchPct2(new BigDecimal(6)); 
	    }
        if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchingContribOtherPlan()))
        {
     	 form.setqACAMatchContributionToAnotherPlan(automaticContributionVO.getqACAMatchingContribOtherPlan().trim());
        }
        else
       {
          form.setqACAMatchContributionToAnotherPlan(Constants.NO);
       }
      if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchingContribOtherPlanName()))
       {
          form.setqACAMatchContributionOtherPlanName(automaticContributionVO.getqACAMatchingContribOtherPlanName().trim());
       }
      if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAIsSHMatchToRoth()))
       {
          form.setqACASafeHarborAppliesToRoth(automaticContributionVO.getqACAIsSHMatchToRoth().trim());
       }
       if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAIsSHMatchToCatchUp()))
       {
          form.setqACASHAppliesToCatchUpContributions(automaticContributionVO.getqACAIsSHMatchToCatchUp().trim());
       }
    
       // set QACA Non elective contribution fields 
       if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACANonElectiveContributionPct()))
       {
          form.setqACANonElectiveContributionPct(automaticContributionVO.getqACANonElectiveContributionPct());
       }else
       {
         form.setqACANonElectiveContributionPct(new BigDecimal(3));
       }
      if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACANonElectiveContributionPct()))
       {
         if(automaticContributionVO.getqACANonElectiveContributionPct().compareTo(BigDecimal.ZERO) == 0){
             form.setqACANonElectiveContributionPct(null);
         }else
         {
          form.setqACANonElectiveContributionPct(automaticContributionVO.getqACANonElectiveContributionPct());
         }
       }else
       {
         form.setqACANonElectiveContributionPct(new BigDecimal(3));  
       }
      if(!PlanDataWebUtility.isNull(automaticContributionVO.getQACAAutoContribWithdrawaldays()))
      {    
       if(automaticContributionVO.getQACAAutoContribWithdrawaldays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_90)||automaticContributionVO.getQACAAutoContribWithdrawaldays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_60)||automaticContributionVO.getQACAAutoContribWithdrawaldays()==Integer.parseInt(Constants.CONTRIBUTIONS_DAYS_30))
       {   
        form.setqACAAutomaticContributionDays(Integer.toString(automaticContributionVO.getQACAAutoContribWithdrawaldays()));
       }else
       {
         form.setqACAAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_OTHER);
         if(automaticContributionVO.getQACAAutoContribWithdrawaldays()!=0)
         {
         form.setqACAAutomaticContributionDaysOther(Integer.toString(automaticContributionVO.getQACAAutoContribWithdrawaldays()));
         }else
         {
        	 form.setqACAAutomaticContributionDaysOther(null); 
         }
       }
      }else
      {
        form.setqACAAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_90);
      }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContributionOptions()))
       {
          form.setqACANonElectiveAppliesToContrib(automaticContributionVO.getqACANonElectiveContributionOptions());
       }
    
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContribOtherPlan()))
       {
          form.setqACANonElectiveContribOtherPlan(automaticContributionVO.getqACANonElectiveContribOtherPlan().trim());
       }else
       {
         form.setqACANonElectiveContribOtherPlan(Constants.NO);  
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContribOtherPlanName()))
       {
          form.setqACASHNonElectivePlanName(automaticContributionVO.getqACANonElectiveContribOtherPlanName());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchOrNonElectiveCode()))
       {
          form.setqACAPlanHasSafeHarborMatchOrNonElective(automaticContributionVO.getqACAMatchOrNonElectiveCode());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAAutomaticContribWithdrawal()))
       {
          form.setqACAPlanHasAdditionalEC(automaticContributionVO.getqACAAutomaticContribWithdrawal().trim());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAPlanHasEmpoyerContribution()))
       {
          form.setqACAPlanHasAdditionalECon(automaticContributionVO.getqACAPlanHasEmpoyerContribution().trim());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getQacaFullyVested()))
       {
          form.setqACASHMatchVesting(automaticContributionVO.getQacaFullyVested().trim());
       }
    if(!PlanDataWebUtility.isNull(automaticContributionVO.getqACAVestingLessThan1YearPct()))
       {
    	/* if(automaticContributionVO.getqACAVestingLessThan1YearPct().compareTo(BigDecimal.ZERO)==0)
    	 {
    		 form.setqACASHMatchVestingPct1(null); 
    	 }else
    	 {*/
          form.setqACASHMatchVestingPct1(automaticContributionVO.getqACAVestingLessThan1YearPct());
    	 //}
       }else
       {
    	   form.setqACASHMatchVestingPct1(null);
       }
    if(!PlanDataWebUtility.isNull(automaticContributionVO.getaACAVesting1To2YearPct()))
       {
    		/*if(automaticContributionVO.getaACAVesting1To2YearPct().compareTo(BigDecimal.ZERO)==0)
    		{
    			form.setqACASHMatchVestingPct2(null);
    		}else
    		{*/
    			form.setqACASHMatchVestingPct2(automaticContributionVO.getaACAVesting1To2YearPct());	
    		//}
       }else
       {
    	   form.setqACASHMatchVestingPct2(null);
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACASPDEmpContribReference()))
       {
          form.setqACASummaryPlanDesc(automaticContributionVO.getqACASPDEmpContribReference().trim());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAArrangementOptions()))
       {
          form.setqACAArrangementOptions(automaticContributionVO.getqACAArrangementOptions());
       }
    if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getDataCompleteInd()))
       {
          form.setAutomaticContributionDataCompleteInd(automaticContributionVO.getDataCompleteInd());
       } 
    logger.info("NoticePlanDataController setQACAValuesToForm ends...");
    	return form;
    }
   
    
    /**
     * To set the Automatic Contribution tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setAutoContributionValuesToForm(NoticePlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    	logger.info("NoticePlanDataController setAutoContributionValuesToForm starts...");
           // Automatic Contribution Tab value mapping to form from AutomaticContributionVO
    	AutomaticContributionVO automaticContributionVO=noticePlanDataVO.getAutomaticEnrollmentVO();
		if(!PlanDataWebUtility.isNull(automaticContributionVO))
		{
			if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getAutomaticEnrollmentType()))
			{
				form.setAutomaticContributionProvisionType(automaticContributionVO.getAutomaticEnrollmentType().trim());
			}
			if(!PlanDataWebUtility.isNull(automaticContributionVO.getaCAContibLessPercentage()))
			{
				form.setContributionFeature1Pct(automaticContributionVO.getaCAContibLessPercentage());
				//form.setAutomaticContributionFeature1("1");
			}
			if(!PlanDataWebUtility.isNull(automaticContributionVO.getaCAHiredAfterDate()))
			{
				form.setContributionFeature2Date(new SimpleDateFormat(DATE_PATTERN).format(automaticContributionVO.getaCAHiredAfterDate()));
				//form.setAutomaticContributionFeature2("2");
			}
			if(!PlanDataWebUtility.isNull(automaticContributionVO.getAcaAnnualIncreaseType()))
			{
				form.setAcaAnnualIncreaseType(automaticContributionVO.getAcaAnnualIncreaseType().trim());
			}
			if(!PlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getaCAAppliesToCustom()))
			{
				form.setContributionFeature3SummaryText(automaticContributionVO.getaCAAppliesToCustom().trim());
				//form.setAutomaticContributionFeature3("3");
			}
			// set values for eaca fields
			form=setEACAValuesToForm(form,automaticContributionVO);
			// set values for qaca fields
			if(!PlanDataWebUtility.isNull(noticePlanCommonVO)&&!PlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
			{
				Date effectiveDate = noticePlanCommonVO.getAutomaticContributionEffectiveDate();
				form.setEffectiveDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
			}
			form=setQACAValuesToForm(form,automaticContributionVO);
			// set values for vesting
			if(!PlanDataWebUtility.isNull(noticePlanCommonVO.getVestingSchedules())){
				form.setExcludeCount(noticePlanCommonVO.getVestingSchedules().size());
			}

		}else
		{

			form.setqACAMatchContributionContribPct1(new BigDecimal(100));
			form.setqACAMatchContributionContribPct2(new BigDecimal(50));
			form.setqACAMatchContributionMatchPct1(new BigDecimal(1));
			form.setqACAMatchContributionMatchPct1Value(form.getqACAMatchContributionMatchPct1());
			// form.setqACAMatchContributionMatchPct1Value(new BigDecimal(3));
			form.setqACAMatchContributionMatchPct2(new BigDecimal(6));
			form.setqACANonElectiveContributionPct(new BigDecimal(3));
			form.setqACAMatchContributionToAnotherPlan(Constants.NO);
			form.setqACANonElectiveContribOtherPlan(Constants.NO);
			form.setAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_90);
			form.setqACAAutomaticContributionDays(Constants.CONTRIBUTIONS_DAYS_90);
			form.setEacaPlanHasAutoContribWD(StringUtils.EMPTY);

			if(!PlanDataWebUtility.isNull(noticePlanCommonVO)&&!PlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
			{
				Date effectiveDate = noticePlanCommonVO.getAutomaticContributionEffectiveDate();
				form.setEffectiveDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
			}

		}
		logger.info("NoticePlanDataController setAutoContributionValuesToForm ends...");
		return form;
    }
    
    
    /**
     * To set the Investment Info tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form
     * @return TabPlanDataForm
     * 
     */
    public NoticePlanDataForm setInvInfoValuesToForm(NoticePlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    	logger.info("NoticePlanDataController setInvInfoValuesToForm starts...");
        //Investment Information  
        InvestmentInformationVO investmentInformationVO = noticePlanDataVO.getInvestmentInformationVO();
        if(investmentInformationVO!=null){
            //Is DIO a QDIA
            if(!PlanDataWebUtility.isNullOrEmpty(investmentInformationVO.getdIOisQDIA())){
                if(Constants.YES.equalsIgnoreCase(investmentInformationVO.getdIOisQDIA().trim())){
                    form.setdIOisQDIA(Constants.YES);
                }
                else if(Constants.NO.equalsIgnoreCase(investmentInformationVO.getdIOisQDIA().trim())){
                    form.setdIOisQDIA(Constants.NO);
                }
            }

            //QDIA FeeRestriction On TransferOutDays
            if(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays()!=null && investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays()!=""){
                if(Constants.TRANSFER_OUT_DAYS_30.equalsIgnoreCase(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim()) 
                        || Constants.TRANSFER_OUT_DAYS_60.equalsIgnoreCase(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim())
                        || Constants.TRANSFER_OUT_DAYS_90.equalsIgnoreCase(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim())){
                    form.setTransferOutDaysCode(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim());
                    form.setTransferOutDaysCustom(StringUtils.EMPTY);
                }
                else{
                    form.setTransferOutDaysCode(Constants.TRANSFER_OUT_DAYS_CUSTOM_CODE);
                    if("0".equalsIgnoreCase(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim())){
                        form.setTransferOutDaysCustom(StringUtils.EMPTY); 
                    }
                    else{
                        form.setTransferOutDaysCustom(investmentInformationVO.getqDIAFeeRestrictionOnTransferOutDays().trim());
                    }
                }
            }
            else{
                form.setTransferOutDaysCode(Constants.TRANSFER_OUT_DAYS_90);
                form.setTransferOutDaysCustom(StringUtils.EMPTY);
            }            
                        
            if(!PlanDataWebUtility.isNullOrEmpty(investmentInformationVO.getDataCompleteInd())){
                form.setInvInfoDataCompleteInd(investmentInformationVO.getDataCompleteInd());
            }
        }
        else{
            form.setTransferOutDaysCode(Constants.TRANSFER_OUT_DAYS_90);
            form.setTransferOutDaysCustom(StringUtils.EMPTY);
            form.setInvInfoDataCompleteInd(Constants.NO); 
        }
        logger.info("NoticePlanDataController setInvInfoValuesToForm ends...");
        return form;
    }
    
    
    /**
     * method to validate the PIF field data for all tabs
     * @param form
     * @return
     */
    
    public List<GenericException> validatePIFData(NoticePlanCommonVO noticePlanCommonVO,NoticePlanDataForm form){
        List<GenericException> errors = new ArrayList<GenericException>();
       
        
        if (PlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxPercent()) && PlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxAmount()) &&
        		(PlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralIrsApplies()) || noticePlanCommonVO.getDeferralIrsApplies().equals(Constants.FALSE)))
        {
            errors.add(new ValidationError("maxEmployeeBeforeTaxDeferralPct"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }        
        if((PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())))
        {
        	errors.add(new ValidationError("contributionMoneyType"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }else
        {
	       	if((PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&((!PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())&& (!noticePlanCommonVO.getMoneyTypeFreequency().equalsIgnoreCase(Constants.UNSPECIFIED)))))
	        {
	       		errors.add(new ValidationError("contributionMoneyType"
	                    , ErrorCodes.PENDING_PIF_COMPLETION,
	                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));
	        }
	       	if(((!PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&(!noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED)))&&(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())))
	        {
	       		errors.add(new ValidationError("moneyTypeFreequency"
	                    , ErrorCodes.PENDING_PIF_COMPLETION,
	                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));
	        }
	       	if(Constants.UNSPECIFIED.equalsIgnoreCase(noticePlanCommonVO.getContributionMoneyType()))
	       	{
	       		errors.add(new ValidationError("contributionMoneyType"
	                    , ErrorCodes.PENDING_PIF_COMPLETION,
	                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));	
	       	}
        }
        if(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEntryFreequencyDate()))
        {
            errors.add(new ValidationError("planEntryFreequencyDate"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }        
        if(!((!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getPlanEmployeeDeferralElection()))
        		&& (!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()))
        			&& StringUtils.isNotBlank(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()) 
        				&& (!noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode().equalsIgnoreCase(Constants.UNSPECIFIED))))
        {
            errors.add(new ValidationError("planEmployeeDeferralElection"
                    , ErrorCodes.UNSPECIFIED,
                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }
        
    	// Update the following code to combine them into one error condition for catchUpContributionsAllowedError
        boolean catchUpContributionsAllowedError=false;
        if( PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getCatchUpContributionsAllowed()) ){
            catchUpContributionsAllowedError=true;
        }
        else if(!noticePlanCommonVO.getCatchUpContributionsAllowed().equalsIgnoreCase("Y") && 
                !noticePlanCommonVO.getCatchUpContributionsAllowed().equalsIgnoreCase("N"))
        {
            catchUpContributionsAllowedError=true;
        }
        if(catchUpContributionsAllowedError){

            errors.add(new ValidationError("catchUpContributionsAllowed"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }
        if(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))
        {
            errors.add(new ValidationError("planAllowLoans"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning, Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }else
        {
        	if(noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.UNSPECIFIED))
        	{
        		  errors.add(new ValidationError("planAllowLoans"
                          , ErrorCodes.PENDING_PIF_COMPLETION,
                          Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        	}
        }
        if((!PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))&&(noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.YES))&&(PlanDataWebUtility.isNull(noticePlanCommonVO.getLoanAmountAllowed())) && (PlanDataWebUtility.isNull(noticePlanCommonVO.getLoanPercentAllowed())))
        {
            errors.add(new ValidationError("loanPercentAllowed"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }
        if(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowRolloverContribution()))
        {
            errors.add(new ValidationError("isRolloverContribution"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }else
        {
        	if(noticePlanCommonVO.getPlanAllowRolloverContribution().equalsIgnoreCase(Constants.UNSPECIFIED))
        	{
        		  errors.add(new ValidationError("isRolloverContribution"
                          , ErrorCodes.PENDING_PIF_COMPLETION,
                          Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        	}
        }
        
        if(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal()))
        {
            errors.add(new ValidationError("isHardshipWithdrawal"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        }else
        {
        	if(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal().equalsIgnoreCase(Constants.UNSPECIFIED))
        	{
        		  errors.add(new ValidationError("isHardshipWithdrawal"
                          , ErrorCodes.PENDING_PIF_COMPLETION,
                          Type.warning,Constants.CONTRIBUTION_AND_DISTRIBUTION));
        	}
        }
        //Safe Harbor
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSHACA()) && Constants.YES.equals(form.getPlanHasSHACA().trim())){
	        if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))
	        {
	            errors.add(new ValidationError("defaultDeferralPercentageSH"
	                    , ErrorCodes.PENDING_PIF_COMPLETION,
	                    Type.warning, Constants.SAFE_HARBOUR));
	        }
	        if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
	        {
	            errors.add(new ValidationError("effectiveDateSH"
	                    , ErrorCodes.PENDING_PIF_COMPLETION,
	                    Type.warning, Constants.SAFE_HARBOUR));
	        }
        }
        //Auto tab PIF validation:
        if (PlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))
        {
            errors.add(new ValidationError("defaultDeferralPercentage"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning, Constants.AUTOMATIC_CONTRIBUTION));
        }
        if (PlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
        {
            errors.add(new ValidationError("effectiveDate"
                    , ErrorCodes.PENDING_PIF_COMPLETION,
                    Type.warning, Constants.AUTOMATIC_CONTRIBUTION));
        }
        
        if(PlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed()))
        {
        	errors.add(new ValidationError("aciAllowed"
					, ErrorCodes.PENDING_PIF_COMPLETION,
					Type.warning));                	
        }
        if(!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed()) && noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.UNSPECIFIED))
        {
        	errors.add(new ValidationError("aciAllowedUnspecified"
					, ErrorCodes.ACI_UNSPECIFIED,
					Type.warning));                	
        }
        if(!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed())&&noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.YES))
        {
        		
        	if(TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAnnualApplyDate()))
        	{
        	errors.add(new ValidationError("aciApplyDate"
					, ErrorCodes.PENDING_PIF_COMPLETION,
					Type.warning));                	
        	}
                     	
        	if(TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultIncreasePercent()))
			{
        		errors.add(new ValidationError("annualIncrease"
			, ErrorCodes.PENDING_PIF_COMPLETION,
			Type.warning));
        		
			}else if((!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultIncreasePercent()))&&noticePlanCommonVO.getDefaultIncreasePercent().compareTo(BigDecimal.ZERO) == 0)
			{
				errors.add(new ValidationError("annualIncrease"
						, ErrorCodes.PENDING_PIF_COMPLETION,
						Type.warning));
			}                	
        	if(TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultAutoIncreaseMaxPercent()))
			{
        		errors.add(new ValidationError("maxAutomaticIncrease"
			, ErrorCodes.PENDING_PIF_COMPLETION,
			Type.warning));                		
			}else if((!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultAutoIncreaseMaxPercent()))&&noticePlanCommonVO.getDefaultAutoIncreaseMaxPercent().compareTo(BigDecimal.ZERO) == 0)
			{
				errors.add(new ValidationError("maxAutomaticIncrease"
						, ErrorCodes.PENDING_PIF_COMPLETION,
						Type.warning));
			}
        }
        
         return errors;
        
    }
    
    /**
	 * Method to validate for PIF data in Contact Information Tab
	 * @param noticePlanCommonVO
	 * @param pifDataerrors
	 */
	public void validateContactAddress(NoticePlanCommonVO noticePlanCommonVO,
			List<GenericException> pifDataerrors) {
		if( StringUtils.isBlank(noticePlanCommonVO.getPrimaryContactName()) || noticePlanCommonVO.getPrimaryContactName()==null){
			pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		            "primaryContactName").toString()
		            , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
		            Type.warning,Constants.CONTACT_INFORMATION));  
		} 
		if( StringUtils.isBlank(noticePlanCommonVO.getPlanSponsorMailingAddress()) || noticePlanCommonVO.getPlanSponsorMailingAddress()==null){
			pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		            "planSponsorMailingAddress").toString()
		            , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
		            Type.warning,Constants.CONTACT_INFORMATION));
		}				
		if( StringUtils.isBlank(noticePlanCommonVO.getPlanSponsorCitySateZip()) || noticePlanCommonVO.getPlanSponsorCitySateZip()==null) {
				pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		                "planSponsorCitySateZip").toString()
		                , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
		                Type.warning,Constants.CONTACT_INFORMATION));   
		}		
	}
	
	
	/**
	 * Method to validate for PIF data in Summary Tab
	 * @param noticePlanCommonVO
	 * @param pifDataerrors
	 */	
	public void validateSummaryTab(NoticePlanCommonVO noticePlanCommonVO, List<GenericException> pifDataerrors) {
		if(StringUtils.isBlank(noticePlanCommonVO.getPlanName()) || noticePlanCommonVO.getPlanName()==null){
			pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		            "planName").toString()
		            , ErrorCodes.PENDING_PIF_COMPLETION,
		            Type.warning,Constants.SUMMARY));  
		}
		if(noticePlanCommonVO.getPlanYearEnd()==null || noticePlanCommonVO.getPlanYearEnd().getData()==null || StringUtils.isBlank(noticePlanCommonVO.getPlanYearEnd().getData())){
			pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		            "planYearEnd.data").toString()
		            , ErrorCodes.PENDING_PIF_COMPLETION,
		            Type.warning,Constants.SUMMARY));  
		}
		
		if(StringUtils.isBlank(noticePlanCommonVO.getContractNumber()) || noticePlanCommonVO.getContractNumber()==null){
			pifDataerrors.add(new ValidationError(new StringBuffer("noticePlanCommonVO").append(GraphLocation.SEPARATOR).append(
		            "contractNumber").toString()
		            , ErrorCodes.PENDING_PIF_COMPLETION,
		            Type.warning,Constants.SUMMARY));
				
		}	
	}		
		
}
   
