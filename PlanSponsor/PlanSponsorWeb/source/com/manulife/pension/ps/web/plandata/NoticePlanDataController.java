package com.manulife.pension.ps.web.plandata;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility;
import com.manulife.pension.service.notices.valueobject.AutomaticContributionVO;
import com.manulife.pension.service.notices.valueobject.ContributionsAndDistributionsVO;
import com.manulife.pension.service.notices.valueobject.InvestmentInformationVO;
import com.manulife.pension.service.notices.valueobject.NoticeDataTransactionHistoryVO;
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
	public 	static final String CONTRIBUTION_MONEY_TYPE="contributionMoneyType";
	public static final FastDateFormat dateFormat = FastDateFormat
				.getInstance(SHORT_MDY_DATE_FORMAT);
	private static String automaticContributionDataCompleted = Constants.YES;
	protected static final String QACA = "QACA";
    protected static final String EACA = "EACA";
    protected static final String ACA = "ACA";
	 
	 /**
     * To set the tab data values from database to form
     * @param noticePlanDataVO, noticePlanCommonVO, form, selectedTab
     * @return TabPlanDataForm
     * 
     */
    public TabPlanDataForm setValuesToForm(NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO, TabPlanDataForm form, String selectedTab) throws SystemException{
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
        else if(Constants.ALL_TABS.equalsIgnoreCase(selectedTab))
        {
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
    public SafeHarborVO setFormDataToSafeHarborVO(TabPlanDataForm form, SafeHarborVO shVOFromSession) throws SystemException
    {
        logger.info("NoticePlanDataController setFormDataToSafeHarborVO starts...");        
        SafeHarborVO safeHarborVO=new SafeHarborVO();
          if(form!=null){
              if(shVOFromSession!=null){
                  safeHarborVO = new SafeHarborVO(shVOFromSession);
              }
              safeHarborVO.setPlanHasSafeHarborMatchOrNonElective(form.getPlanHasSafeHarborMatchOrNonElective());
              //SH type
              if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSafeHarborMatchOrNonElective())){
                  safeHarborVO.setPlanHasSafeHarborMatchOrNonElective(form.getPlanHasSafeHarborMatchOrNonElective().trim());
                  if(Constants.SH_MATCH.equalsIgnoreCase(form.getPlanHasSafeHarborMatchOrNonElective().trim())){
                    //SH match Contribution row1 pct1
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct1())){
                          safeHarborVO.setMatchContributionContribPct1(new BigDecimal(form.getMatchContributionContribPct1().trim()));
                      }
                      
                      //if match contribution row2 pct1 values are empty/null or 0.0, set value = 0.0
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2())){
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
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct1())){
                          safeHarborVO.setMatchContributionMatchPct1(new BigDecimal(form.getMatchContributionMatchPct1().trim()));
                      }
                      
                      //if match contribution row2 pct2 values are empty/null or 0.0, set value = 0.0
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())){
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
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchAppliesToContrib())){
                          safeHarborVO.setMatchAppliesToContrib(form.getMatchAppliesToContrib().trim());
                      }
                      
                      //SH match another Plan
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionToAnotherPlan())){
                          safeHarborVO.setMatchContributionToAnotherPlan(form.getMatchContributionToAnotherPlan().trim());
                          if(Constants.YES.equals(form.getMatchContributionToAnotherPlan().trim())){
                              safeHarborVO.setMatchContributionOtherPlanName(form.getMatchContributionOtherPlanName());
                          }
                          else{
                              safeHarborVO.setMatchContributionOtherPlanName(null);
                          }
                      }
                      
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSafeHarborAppliesToRoth())){
                          safeHarborVO.setSafeHarborAppliesToRoth(form.getSafeHarborAppliesToRoth().trim());
                      }
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getsHAppliesToCatchUpContributions())){
                          safeHarborVO.setsHAppliesToCatchUpContributions(form.getsHAppliesToCatchUpContributions().trim());
                      }
                      safeHarborVO.setNonElectiveContribOption(null);
                      safeHarborVO.setNonElectiveContributionPct(null);
                      safeHarborVO.setNonElectiveAppliesToContrib(null);
                      safeHarborVO.setNonElectiveContribOtherPlan(null);
                      safeHarborVO.setNonElectiveContribOtherPlanName(null);
                      safeHarborVO.setEnablePlanYearEndDateAndPercentageComp(null);
                      safeHarborVO.setContributionApplicableToPlanDate(null);
                	  safeHarborVO.setContributionApplicableToPlanPct(null);

                  }
                  else if(Constants.SH_NON_ELECTIVE.equalsIgnoreCase(form.getPlanHasSafeHarborMatchOrNonElective().trim())){
                    //Non-elective contribution option
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOption())){
                          safeHarborVO.setNonElectiveContribOption(form.getNonElectiveContribOption().trim());
                      }
                      
                      //if non elective contribution pct value is empty/null or 0.0, set value = 0.0
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContributionPct())){
                          if(new BigDecimal(Double.parseDouble(form.getNonElectiveContributionPct())).compareTo(BigDecimal.ZERO) == 0){
                              safeHarborVO.setNonElectiveContributionPct(BigDecimal.ZERO);
                          }
                          safeHarborVO.setNonElectiveContributionPct(new BigDecimal(form.getNonElectiveContributionPct().trim()));
                      }else{
                          safeHarborVO.setNonElectiveContributionPct(BigDecimal.ZERO);
                      }
                      
                      //NE contribution applies to Employees
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveAppliesToContrib())){
                          safeHarborVO.setNonElectiveAppliesToContrib(form.getNonElectiveAppliesToContrib().trim());
                      }
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOtherPlan())){
                          safeHarborVO.setNonElectiveContribOtherPlan(form.getNonElectiveContribOtherPlan().trim());
                          if(Constants.YES.equals(form.getNonElectiveContribOtherPlan().trim())){
                              safeHarborVO.setNonElectiveContribOtherPlanName(form.getSHNonElectivePlanName());
                          }
                          else{
                              safeHarborVO.setNonElectiveContribOtherPlanName(null);
                          }
                      }
                      
                      //NE contribution applicable to Plan
                      if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEnablePlanYearEndDateAndPercentageComp()) && form.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.YES)) {
                    	  
                    	  safeHarborVO.setEnablePlanYearEndDateAndPercentageComp(Constants.YES);
                    	  
                    	  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionApplicableToPlanDate())) {
                    		  DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
                    		  Date currentDate;
                    		  try {
                    			  currentDate = format.parse(form.getContributionApplicableToPlanDate());
                    			  safeHarborVO.setContributionApplicableToPlanDate(currentDate);
                    		  } catch (ParseException e) {
                    			  logger.error("ParseException "+e.getMessage());
                    		  }
                    	  }
                    	  else {
                    		  safeHarborVO.setContributionApplicableToPlanDate(null);
                    	  }
                    	  
                    	  if(!TPAPlanDataWebUtility.isNull(form.getContributionApplicableToPlanPct())) {
                    		  safeHarborVO.setContributionApplicableToPlanPct(form.getContributionApplicableToPlanPct());
                    	  }
                    	  else {
                    		  safeHarborVO.setContributionApplicableToPlanPct(null);
                    	  }
                      }
                      else {
                    	  safeHarborVO.setEnablePlanYearEndDateAndPercentageComp(Constants.NO);
                    	  safeHarborVO.setContributionApplicableToPlanDate(null);
                    	  safeHarborVO.setContributionApplicableToPlanPct(null);
                      }
                      
                      safeHarborVO.setMatchContributionContribPct1(null);
                      safeHarborVO.setMatchContributionContribPct2(null);
                      safeHarborVO.setMatchContributionMatchPct1(null);
                      safeHarborVO.setMatchContributionMatchPct2(null);
                      safeHarborVO.setMatchAppliesToContrib(null);
                      safeHarborVO.setMatchContributionToAnotherPlan(null);
                      safeHarborVO.setMatchContributionOtherPlanName(null);
                      safeHarborVO.setSafeHarborAppliesToRoth(null);
                      safeHarborVO.setsHAppliesToCatchUpContributions(null);
                      
                  }
              }
              
              //Plan allow for an automatic contribution arrangements
              if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSHACA())){
            	  safeHarborVO.setPlanHasSHACA(form.getPlanHasSHACA().trim());
            	   if(Constants.YES.equals(form.getPlanHasSHACA().trim())){
		            
            		   if(!TPAPlanDataWebUtility.isNull(form.getsHautomaticContributionFeature1()) && ("1").equalsIgnoreCase(form.getsHautomaticContributionFeature1()) ){
            			   if(!TPAPlanDataWebUtility.isNull(form.getShContributionFeature1Pct())){
		                       safeHarborVO.setShContributionFeature1Pct(form.getShContributionFeature1Pct());
        		   			}
            		   }else{
            			   safeHarborVO.setShContributionFeature1Pct(null);
            		   }
            		   
					  if(!TPAPlanDataWebUtility.isNull(form.getsHautomaticContributionFeature2())&& ("2").equalsIgnoreCase(form.getsHautomaticContributionFeature2())){
								  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getShContributionFeature2Date())) {
			                 		  DateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
			                 		  Date currentDate;
		                 		  	try {
		                 			  currentDate = format.parse(form.getShContributionFeature2Date());
		                 			  safeHarborVO.setShContributionFeature2Date(currentDate);
		                 		  		} catch (ParseException e) {
		                 			  logger.error("ParseException "+e.getMessage());
		                 		  		}
					                  }else
					                  {
					                 		  safeHarborVO.setShContributionFeature2Date(null);
					                  }
        		   		
					            			   
					      }else{
	                 		  safeHarborVO.setShContributionFeature2Date(null);
					           }
					  
					  if(!TPAPlanDataWebUtility.isNull(form.getsHautomaticContributionFeature3())&& ("3").equalsIgnoreCase(form.getsHautomaticContributionFeature3())){
						  safeHarborVO.setShContributionFeature3SummaryText(form.getShContributionFeature3SummaryText());						   
					  }else{
						  safeHarborVO.setShContributionFeature3SummaryText(null);
						  }
					  
            		   		
            		       safeHarborVO.setSHAutoContributionWD(form.getSHAutoContributionWD());
	            		 if(!TPAPlanDataWebUtility.isNull(form.getShACAAnnualIncreaseType()))
		           	        {	   
	            			 safeHarborVO.setShACAAnnualIncreaseType(form.getShACAAnnualIncreaseType().trim());
		           	        }
              	   
            		   
            		   if(Constants.YES.equals(form.getSHAutoContributionWD().trim())){
            			   
            			   if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutomaticContributionDays()))
            	  	       {
            	   	  		if(form.getSHAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
            	     	    	   {
    		        	   	  			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutomaticContributionDays()))
    		        	   	  			{
    		        	   	  			safeHarborVO.setSHAutomaticContributionDays(Integer.parseInt(form.getSHAutomaticContributionDaysOther()));
    		        	   	  			}else
    		        	   	  			{
    		        	   	  			safeHarborVO.setSHAutomaticContributionDays(0);
    		        	   	  			}
            	     	    	   }else
            	     	    	   {
            	     	    		  safeHarborVO.setSHAutomaticContributionDays(Integer.parseInt(form.getSHAutomaticContributionDays()));
            	     	    	   }
            	   	      
            	  	       }
            			}
            		  
            		            		   
            	   }else{
            		  
            		   safeHarborVO.setShContributionFeature1Pct(null);
            		   safeHarborVO.setShContributionFeature2Date(null);
            		   safeHarborVO.setShContributionFeature3SummaryText(null);
            		   safeHarborVO.setSHAutoContributionWD(null);
            		   safeHarborVO.setSHAutomaticContributionDays(0);
            		   safeHarborVO.setShACAAnnualIncreaseType("1");
            	   }
            	  
              }
              
              
              //Additional Employer Contribution 
              if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasAdditionalEC())){
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
     * This method will set the Investment Information form values to InvestmentInformationVO to save in the database
     * @param form
     * @return InvestmentInformationVO
     * 
     */
    public InvestmentInformationVO setFormDataToInvInfoVO(TabPlanDataForm form) throws SystemException{
        logger.info("NoticePlanDataController setFormDataToInvInfoVO starts...");
        InvestmentInformationVO invInfoVO = new InvestmentInformationVO();
        if(form!=null){
            if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getdIOisQDIA())){
                invInfoVO.setdIOisQDIA(form.getdIOisQDIA().trim());
                if(Constants.YES.equalsIgnoreCase(form.getdIOisQDIA().trim())){
                    if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getTransferOutDaysCode())){
                        if(Constants.TRANSFER_OUT_DAYS_CUSTOM_CODE.equals(form.getTransferOutDaysCode().trim())){
                            if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getTransferOutDaysCustom())){
                                invInfoVO.setqDIAFeeRestrictionOnTransferOutDays(form.getTransferOutDaysCustom().trim());
                            }
                            else{
                                invInfoVO.setqDIAFeeRestrictionOnTransferOutDays("0");
                            }
                        }
                        else{
                            invInfoVO.setqDIAFeeRestrictionOnTransferOutDays(form.getTransferOutDaysCode().trim());
                        }
                    }
                }
                else if(Constants.NO.equalsIgnoreCase(form.getdIOisQDIA().trim())){
                    invInfoVO.setqDIAFeeRestrictionOnTransferOutDays(null);
                }
                else{
                    invInfoVO.setqDIAFeeRestrictionOnTransferOutDays(null);
                }
            }
            invInfoVO.setDataCompleteInd(form.getInvInfoDataCompleteInd());
            invInfoVO.setLastUpdatedTimeStamp(new Date());
        }
        logger.info("NoticePlanDataController setFormDataToInvInfoVO ends...");
        return invInfoVO;
    }
    
    
    /**
     * To clear the Safe Harbor values from form
     * @param form
     * @return TabPlanDataForm
     */
    public TabPlanDataForm clearSHValuesFromForm(TabPlanDataForm form){
        form.setPlanHasSafeHarborMatchOrNonElective(null);
        form.setMatchContributionContribPct1(null);
        form.setMatchContributionContribPct2(null);
        form.setMatchContributionMatchPct1(null);
        form.setMatchContributionMatchPct2(null);
        form.setMatchAppliesToContrib(null);
        form.setMatchContributionToAnotherPlan(null);
        form.setMatchContributionOtherPlanName(null);
        form.setSafeHarborAppliesToRoth(null);
        form.setsHAppliesToCatchUpContributions(null);
        form.setNonElectiveContribOption(null);
        form.setNonElectiveContributionPct(null);
        form.setNonElectiveAppliesToContrib(null);
        form.setNonElectiveContribOtherPlan(null);
        form.setSHNonElectivePlanName(null);
        form.setPlanHasAdditionalEC(null);
        form.setSummaryPlanDesc(null);
        form.setSafeHarborDataCompleteInd(null);
        form.setNoticePlanDataVO(null);
        //CR011 START
        form.setPlanHasSHACA(null);
        form.setSHAutoContributionWD(null);
        form.setSHAutomaticContributionDays(null);
        form.setSHAutomaticContributionDaysOther(null);
        form.setsHautomaticContributionFeature1("0");
        form.setsHautomaticContributionFeature2("0");
        form.setsHautomaticContributionFeature3("0");
        form.setShContributionFeature1Pct(null);
        form.setShContributionFeature2Date(null);
        form.setShContributionFeature3SummaryText(null);
        form.setShACAAnnualIncreaseType(null);
        form.setEffectiveDate(null);
        //CRO11 END
        form.setDirty("false");
        return form;
    }
    
    /**
     * To clear the Investment Information values from form
     * @param form
     * @return TabPlanDataForm
     */
    public TabPlanDataForm clearInvInfoValuesFromForm(TabPlanDataForm form){
        form.setdIOisQDIA(null);
        form.setTransferOutDaysCode(null);
        form.setTransferOutDaysCustom(null);
        form.setInvInfoDataCompleteInd(null);
        form.setDirty("false");
        return form;
    }

    
    public Collection<NoticeDataTransactionHistoryVO> compareBeans(Object newBean, Object oldBean,String profileId,Integer contractNumber) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SystemException {
    	logger.info("NoticePlanDataController compareBeans starts...");
    	if(null!=newBean&&null!=oldBean)
    	{
    	if( newBean.getClass() != oldBean.getClass()  ) {
    	throw new IllegalArgumentException("The beans must be from the same Class!");
    	}
    	}
    	Collection<NoticeDataTransactionHistoryVO> historyData = new ArrayList<NoticeDataTransactionHistoryVO>();
    	
    	
    	Iterator keysIt=null; 
    	if(null!=newBean)
    	{
    	keysIt= BeanUtils.describe(newBean).keySet().iterator();
    	while (keysIt.hasNext()) {
    		NoticeDataTransactionHistoryVO historyVO=new NoticeDataTransactionHistoryVO();
    	
    	String key = (String) keysIt.next();
    	Object oldValue=null;
    	Object newValue=null;
    	if(null!=oldBean)
    	{
    	oldValue = PropertyUtils.getProperty(oldBean, key);    	
    	if(null!=oldValue && oldValue instanceof String) {
    		oldValue = ((String) oldValue).trim();
    	}
    	}
    	newValue = PropertyUtils.getProperty(newBean, key);
    	if(null!=newValue && newValue instanceof String) {
    		newValue = ((String) newValue).trim();
    	}
    	if( oldValue != newValue ) {// Ignores when both are "null"
    	if( ( (oldValue != null) && !oldValue.equals(newValue) )
    	 || ( (newValue != null) && !newValue.equals(oldValue) ) ) {
    		if(null!=newValue && newValue instanceof Collection)
    				{
    			if(newValue instanceof UnmodifiableCollection )
    			{
    			UnmodifiableCollection currentValue=(UnmodifiableCollection)newValue;
    			if(null!=currentValue&&currentValue.isEmpty())
    			{}
    			}
    				}else if(newValue instanceof ArrayList)
    				{
    					List currentListValue=(ArrayList)newValue;
    					if(null!=currentListValue&&(!currentListValue.isEmpty()))
    					{
    						Object[] listValues=currentListValue.toArray();
    						StringBuffer s=new StringBuffer();
    						for(Object obj:listValues)
    						{
    							s.append(obj.toString());
    						}
    						historyVO.setUpdatedByProfileId(profileId);
        		    		historyVO.setContractId(contractNumber.intValue());
        		historyVO.setFieldName(key);
        		if(s!=null){
        		    historyVO.setFieldValue(s.toString());

        		}	
            		historyVO.setEntryType("U"); 
    					}
    				}else
    				{
    					historyVO.setUpdatedByProfileId(profileId);
    		    		historyVO.setContractId(contractNumber.intValue());
    		historyVO.setFieldName(key);
    		if(newValue!=null){
    		    historyVO.setFieldValue(newValue.toString());
    		}
    		else{
    		    historyVO.setFieldValue(null);
    		}
    		
    		historyVO.setEntryType("U");  // Update  		
    				}
    	} 
    	if(  (oldValue == null) && ( (newValue != null) && (!newValue.equals(oldValue) )) ) {
    		historyVO.setUpdatedByProfileId(profileId);
    		historyVO.setContractId(contractNumber.intValue());
    	    		historyVO.setFieldName(key);
    	    		historyVO.setFieldValue(newValue.toString());
    	    		historyVO.setEntryType("I");   // Insert 	    	
    	    	}
    	}
    	if(null!=historyVO&&historyVO.getFieldName()!=null)
       	historyData.add(historyVO);
    	}
    	}
    	logger.info("NoticePlanDataController compareBeans ends...");
    	return historyData;
    	}
    /**
     * Method used to copy form data to  ContributionsAndDistributionsVO
     * @param form
     * @return ContributionsAndDistributionsVO
     */
    public ContributionsAndDistributionsVO setFormDataToContributionsAndDistributionsVO(TabPlanDataForm form,NoticePlanCommonVO noticePlanCommonVO) throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToContributionsAndDistributionsVO starts...");
    	// Map form field values to ContributionsAndDistributionsVO
    	ContributionsAndDistributionsVO contributionsAndDistributionsVO=new ContributionsAndDistributionsVO();
    	
    	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanAllowRothDeferrals())){
        contributionsAndDistributionsVO.setPlanAllowRothDeferrals(form.getPlanAllowRothDeferrals().trim());
    	}
        if(!TPAPlanDataWebUtility.isNull(form.getContirbutionRestirictionOnHardships())){
	    contributionsAndDistributionsVO.setContirbutionRestirictionOnHardships(form.getContirbutionRestirictionOnHardships());   
        }
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSpdEmployeeContributionRef())){
        contributionsAndDistributionsVO.setSpdEmployeeContributionRef(form.getSpdEmployeeContributionRef().trim());
        }
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanAllowsInServiceWithdrawals())){
        contributionsAndDistributionsVO.setPlanAllowsInServiceWithdrawals(form.getPlanAllowsInServiceWithdrawals().trim());     
        }
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContriAndDistriDataCompleteInd())){
        contributionsAndDistributionsVO.setDataCompleteInd(form.getContriAndDistriDataCompleteInd().trim());
        }
      
        contributionsAndDistributionsVO.setLastUpdatedTimeStamp(new Date());
        logger.info("NoticePlanDataController setFormDataToContributionsAndDistributionsVO ends...");   
    	return contributionsAndDistributionsVO;
    }
    /**
     * Method to clear the contributions values from form
     * @param form
     * @return
     */
    
    public TabPlanDataForm clearContributionsValuesFromForm(TabPlanDataForm form){
    	 
    	form.setSpdEmployeeContributionRef(null);
    	form.setPlanAllowRothDeferrals(null);
    	form.setPlanAllowsInServiceWithdrawals(null);
    	form.setContirbutionRestirictionOnHardships(null);
    	form.setDirty("false");     
        return form;
    }
    
    
    /**
     * method to validate the PIF field data for all tabs
     * @param form
     * @return
     */
    
    public static List<GenericException> validateTabData(NoticePlanCommonVO noticePlanCommonVO,NoticePlanDataVO noticePlanDataVO,String toTab, TabPlanDataForm form, String buttonClicked) throws SystemException{
    	logger.info("NoticePlanDataController validateTabData starts...");
        List<GenericException> errors = new ArrayList<GenericException>();
        form.setPendingPIFCompletion(Boolean.FALSE); 
        
        if(Constants.SUMMARY.equalsIgnoreCase(toTab)) {
        	logger.info("NoticePlanDataController validate pif data for summary tab starts...");
        	if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanName()) ||
        			TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanYearEnd().getData()) ||
        			TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContractNumber())) {
        		form.setPendingPIFCompletion(Boolean.TRUE);
        	}
        	if(form.isPendingPIFCompletion()) {
            	if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanName()))
       			 {
            		    errors.add(new ValidationError("planName"
                     , ErrorCodes.PENDING_PIF_COMPLETION,
                     Type.warning));
       			 }
            		if(null == noticePlanCommonVO.getPlanYearEnd() || TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanYearEnd().getData()))
       			 {
            		    errors.add(new ValidationError("planYearEndDate"
                     , ErrorCodes.PENDING_PIF_COMPLETION,
                     Type.warning));
       			 }
            		if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContractNumber()))
       			 {
            		    errors.add(new ValidationError("contractNumber"
                     , ErrorCodes.PENDING_PIF_COMPLETION,
                     Type.warning));
       			 }
            }
        	logger.info("NoticePlanDataController validate pif data for summary tab ends...");
        }
        else if(Constants.CONTRIBUTION_AND_DISTRIBUTION.equalsIgnoreCase(toTab))
         {   
        	logger.info("NoticePlanDataController validate pif data for Contribution and Distribution tab starts...");
        	
        	      	
            if ((TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxPercent()) && TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxAmount()) && 
            			(TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralIrsApplies()) || noticePlanCommonVO.getDeferralIrsApplies().equals(Constants.FALSE)) ) ||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency()))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEntryFreequencyDate()))||
            		(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getPlanEmployeeDeferralElection()) && TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getCatchUpContributionsAllowed()))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))||
            		((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))&&(noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.YES))&&((TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getLoanAmountAllowed())) || (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getLoanPercentAllowed()))))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowRolloverContribution()))||
            		(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal())) || ((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&& (noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED))) ||
            		(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency()))&& (noticePlanCommonVO.getMoneyTypeFreequency().equalsIgnoreCase(Constants.UNSPECIFIED)))) ||
            		(((!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getPlanEmployeeDeferralElection()))&& (!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode())) && (noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode().equalsIgnoreCase(Constants.UNSPECIFIED)))) ||
            		(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getCatchUpContributionsAllowed()))&& (noticePlanCommonVO.getCatchUpContributionsAllowed().equalsIgnoreCase(Constants.UNSPECIFIED)))) ||
            		(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowRolloverContribution()))&& (noticePlanCommonVO.getPlanAllowRolloverContribution().equalsIgnoreCase(Constants.UNSPECIFIED)))) ||
            		(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))&& (noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.UNSPECIFIED)))) ||
            		(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal()))&& (noticePlanCommonVO.getPlanAllowsHardshipWithdrawal().equalsIgnoreCase(Constants.UNSPECIFIED))))
            		)
            {
                form.setPendingPIFCompletion(Boolean.TRUE);                 
            }
            if(form.isPendingPIFCompletion())
            {
            	if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxPercent()) && TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralMaxAmount()) &&
            			(TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDeferralIrsApplies()) || noticePlanCommonVO.getDeferralIrsApplies().equals(Constants.FALSE)))
                {
                    errors.add(new ValidationError("maxEmployeeBeforeTaxDeferralPct"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
            	
                if((TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())))
                {
                    errors.add(new ValidationError(CONTRIBUTION_MONEY_TYPE
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }else
                {
               	if((TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&(!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())&& (!noticePlanCommonVO.getMoneyTypeFreequency().equalsIgnoreCase(Constants.UNSPECIFIED))))
                {
                    errors.add(new ValidationError(CONTRIBUTION_MONEY_TYPE
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
               	if(((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getContributionMoneyType()))&&(!noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED)))&&(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getMoneyTypeFreequency())))
                {
                    errors.add(new ValidationError("moneyTypeFreequency"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
               	if(noticePlanCommonVO.getContributionMoneyType().equalsIgnoreCase(Constants.UNSPECIFIED))
               	{
               	 errors.add(new ValidationError(CONTRIBUTION_MONEY_TYPE
                         , ErrorCodes.PENDING_PIF_COMPLETION,
                         Type.warning));	
               	}
                }
                if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEntryFreequencyDate()))
                {
                    errors.add(new ValidationError("planEntryFreequencyDate"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
                if(!((!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getPlanEmployeeDeferralElection()))
                		&& (!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()))
                			&& StringUtils.isNotBlank(noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode()) 
                				&& (!noticePlanCommonVO.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode().equalsIgnoreCase(Constants.UNSPECIFIED))))
                {
                    errors.add(new ValidationError("planEmployeeDeferralElection"
                            , ErrorCodes.UNSPECIFIED,
                            Type.warning));
                }
               // Update the following code to combine them into one error condition for catchUpContributionsAllowedError
                boolean catchUpContributionsAllowedError=false;
                if( TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getCatchUpContributionsAllowed()) ){
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
                            Type.warning));
                }
                if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))
                {
                    errors.add(new ValidationError("planAllowLoans"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }else
                {
                	if(noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.UNSPECIFIED))
                	{
                		  errors.add(new ValidationError("planAllowLoans"
                                  , ErrorCodes.PENDING_PIF_COMPLETION,
                                  Type.warning));
                	}
                }
                if((!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowLoans()))&&(noticePlanCommonVO.getPlanAllowLoans().equalsIgnoreCase(Constants.YES))&&((TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getLoanAmountAllowed())) || (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getLoanPercentAllowed()))))
                {
                    errors.add(new ValidationError("loanPercentAllowed"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
                if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowRolloverContribution()))
                {
                    errors.add(new ValidationError("isRolloverContribution"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }else
                {
                	if(noticePlanCommonVO.getPlanAllowRolloverContribution().equalsIgnoreCase(Constants.UNSPECIFIED))
                	{
                		  errors.add(new ValidationError("isRolloverContribution"
                                  , ErrorCodes.PENDING_PIF_COMPLETION,
                                  Type.warning));
                	}
                }
                
                if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal()))
                {
                    errors.add(new ValidationError("isHardshipWithdrawal"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }else
                {
                	if(noticePlanCommonVO.getPlanAllowsHardshipWithdrawal().equalsIgnoreCase(Constants.UNSPECIFIED))
                	{
                		  errors.add(new ValidationError("isHardshipWithdrawal"
                                  , ErrorCodes.PENDING_PIF_COMPLETION,
                                  Type.warning));
                	}
                }
            }
            logger.info("NoticePlanDataController validate pif data for Contribution and Distribution tab ends...");
            //When the page loads for the first time check for table records. If there are no records, do not validate transactional data 
            if("Save".equalsIgnoreCase(buttonClicked)){
                errors.addAll(validateContributionAndDistribution(form, noticePlanCommonVO));
            }
            else{
                if(!TPAPlanDataWebUtility.isNull(noticePlanDataVO)&&!TPAPlanDataWebUtility.isNull(noticePlanDataVO.getContributionsAndDistributionsVO())&&!noticePlanDataVO.getContributionsAndDistributionsVO().isInitialLoad())
                {
                    errors.addAll(validateContributionAndDistribution(form, noticePlanCommonVO));
                }
            }
         }
        // Automatic Contribution data validation
        else if(Constants.AUTOMATIC_CONTRIBUTION.equalsIgnoreCase(toTab))
        {
        	logger.info("NoticePlanDataController validate pif data for automatic contribution tab starts...");
            if ((TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))||
                    (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate())) ||
                    (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAciAllowed()))|| (noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.UNSPECIFIED))) 
            {
                form.setPendingPIFCompletion(Boolean.TRUE);                 
            }
            if(form.isPendingPIFCompletion())
            {
                if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))
                {
                    errors.add(new ValidationError("defaultDeferralPercentage"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
                if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
                {
                    errors.add(new ValidationError("effectiveDate"
                            , ErrorCodes.PENDING_PIF_COMPLETION,
                            Type.warning));
                }
                if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed()))
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
               

            }
            logger.info("NoticePlanDataController validate pif data for automatic contribution tab ends...");
            
            // Add warning for incomplete vesting schedules
            if(form.getVestingScheduleCompletion().contains(false)) {
            	int count = 0;
            	for(Boolean vestingComplete : form.getVestingScheduleCompletion()) {
            		if(!vestingComplete && ((Constants.QACA.equalsIgnoreCase(form.getAutomaticContributionProvisionType()) && Constants.YES.equalsIgnoreCase(form.getqACAPlanHasAdditionalECon()))
            				|| (Constants.EACA.equalsIgnoreCase(form.getAutomaticContributionProvisionType()) && Constants.YES.equalsIgnoreCase(form.getEmployerContributions())))) {
            			errors.add(new ValidationError("acVestingIncomplete"+count
                                , ErrorCodes.INCOMPLETE_VESTING_SCHEDULE,
                                Type.warning));
            		}
            		count++;
            	}
            }
            
            //When the page loads for the first time check for table records. If there are no records, do not validate transactional data
            if("Save".equalsIgnoreCase(buttonClicked)){
                errors.addAll(validateAutomaticContribution(form, noticePlanCommonVO, noticePlanDataVO, true, true));
            }
            else{
                if(!TPAPlanDataWebUtility.isNull(noticePlanDataVO)&&!TPAPlanDataWebUtility.isNull(noticePlanDataVO.getAutomaticEnrollmentVO())&&!noticePlanDataVO.getAutomaticEnrollmentVO().isInitialLoad())
                {
                	boolean eACAFlag = Constants.EACA.equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().getAutomaticEnrollmentType());
                	boolean qACAFlag = Constants.QACA.equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().getAutomaticEnrollmentType());
                    errors.addAll(validateAutomaticContribution(form, noticePlanCommonVO, noticePlanDataVO, eACAFlag, qACAFlag));
                } 
            }       

        }
        //Safe Harbor data validation
        else if(Constants.SAFE_HARBOR.equalsIgnoreCase(toTab))
        {
        	logger.info("NoticePlanDataController validate pif data for safe harbor tab starts...");
            //SH PIF data 
        	//CR011 START
        	 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSHACA()) && Constants.YES.equals(form.getPlanHasSHACA().trim())){
        		 if ((TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))||
                         (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate())) ||
                         (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAciAllowed()))|| (noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.UNSPECIFIED))) 
                 {
                     form.setPendingPIFCompletion(Boolean.TRUE);                 
                 }
                 if(form.isPendingPIFCompletion())
                 {
                     if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getDefaultDeferralPercentage()))
                     {
                         errors.add(new ValidationError("defaultDeferralPercentage"
                                 , ErrorCodes.PENDING_PIF_COMPLETION,
                                 Type.warning));
                     }
                     if (TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
                     {
                         errors.add(new ValidationError("effectiveDate"
                                 , ErrorCodes.PENDING_PIF_COMPLETION,
                                 Type.warning));
                     }
                     if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed()))
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
                 }   
        	 }
        	//CR011 END
        	
            logger.info("NoticePlanDataController validate pif data for safe harbor tab ends...");
            
            // Add warning for incomplete vesting schedules
            if(form.getVestingScheduleCompletion().contains(false)) {
            	int count = 0;
            	for(Boolean vestingComplete : form.getVestingScheduleCompletion()) {
            		if(!vestingComplete && Constants.YES.equalsIgnoreCase(form.getPlanHasAdditionalEC())) {
            			errors.add(new ValidationError("shVestingIncomplete"+count
                                , ErrorCodes.INCOMPLETE_VESTING_SCHEDULE,
                                Type.warning));
            		}
            		count++;
            	}
            }
            
            if("Save".equalsIgnoreCase(buttonClicked)){
                errors.addAll(validateSafeHarborFormData(form, noticePlanCommonVO, noticePlanDataVO));
            }
            else{
                //Perform transaction data validation only when there are records in DB
                if(!TPAPlanDataWebUtility.isNull(noticePlanDataVO)&&!TPAPlanDataWebUtility.isNull(noticePlanDataVO.getSafeHarborVO())&&noticePlanDataVO.getSafeHarborVO().isInitialLoad()){
                    errors.addAll(validateSafeHarborFormData(form, noticePlanCommonVO, noticePlanDataVO));
                }
            }
        }
        //Investment Information Transaction data
        else if(Constants.INVESTMENT_INFO.equalsIgnoreCase(toTab))
        	
        {
            //When the page loads for the first time check for table records. If there are no records, do not validate transaction data
            if("Save".equalsIgnoreCase(buttonClicked)){
                errors.addAll(validateInvestmentInfoFormData(form));
            }
            else{
                //Perform transaction data validation only when there are records in DB
                if(!TPAPlanDataWebUtility.isNull(noticePlanDataVO)&&!TPAPlanDataWebUtility.isNull(noticePlanDataVO.getInvestmentInformationVO())&&noticePlanDataVO.getInvestmentInformationVO().isInitialLoad()){
                    errors.addAll(validateInvestmentInfoFormData(form));
                } 
            }             
        }
        else if(Constants.CONTACT_INFORMATION.equalsIgnoreCase(toTab)) {
        	logger.info("NoticePlanDataController validate pif data for contact information tab starts...");
            if(noticePlanCommonVO!=null && (TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPrimaryContactName())
                    || TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorMailingAddress())
                    || TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorCitySateZip()))) {
                form.setPendingPIFCompletion(Boolean.TRUE);
            }
            if(form.isPendingPIFCompletion()) {
            	if(noticePlanCommonVO!=null){
            		if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPrimaryContactName())) {
                		errors.add(new ValidationError("psNameCI"
                                , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
                                Type.warning));
                	}
                	if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorMailingAddress())) {
                		errors.add(new ValidationError("psMailingAddrCI"
                                , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
                                Type.warning));
                	}
                	if(TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getPlanSponsorCitySateZip())) {
                		errors.add(new ValidationError("psCityStateZipCI"
                                , ErrorCodes.CONTACT_INFORMATION_PENDING_COMPLETION,
                                Type.warning));
                	}
            	}            	
                
            }
            logger.info("NoticePlanDataController validate pif data for contact information tab ends...");
        }
        return errors;
        
    }
    
    
    /**
     * Method used to save QACA and EACA if value already in DB and if ACA is selected
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToEacaAndQacaAutomaticContributionVO(TabPlanDataForm form,AutomaticContributionVO automaticContributionVO ,AutomaticContributionVO oldAutomaticContributionVO)throws SystemException
    {
    	 logger.info("NoticePlanDataController setFormDataToEacaAndQacaAutomaticContributionVO start...");
    	/*automaticContributionVO.setqACAArrangementOptions(oldAutomaticContributionVO.getqACAArrangementOptions());
    	automaticContributionVO.setqACAAutomaticContribWithdrawal(oldAutomaticContributionVO.getqACAAutomaticContribWithdrawal());
    	automaticContributionVO.setqACAisShmacOrShnec(oldAutomaticContributionVO.getqACAisShmacOrShnec());
    	automaticContributionVO.setqACAIsSHMatchToCatchUp(oldAutomaticContributionVO.getqACAIsSHMatchToCatchUp());
    	automaticContributionVO.setqACAIsSHMatchToRoth(oldAutomaticContributionVO.getqACAIsSHMatchToRoth());
    	automaticContributionVO.setqACAMatchingContribOtherPlan(oldAutomaticContributionVO.getqACAMatchingContribOtherPlan());
    	automaticContributionVO.setqACAMatchingContribOtherPlanName(oldAutomaticContributionVO.getqACAMatchingContribOtherPlanName());
    	automaticContributionVO.setqACAMatchOrNonElectiveCode(oldAutomaticContributionVO.getqACAMatchOrNonElectiveCode());
    	automaticContributionVO.setqACANonElectiveContribOtherPlan(oldAutomaticContributionVO.getqACANonElectiveContribOtherPlan());
    	automaticContributionVO.setqACANonElectiveContribOtherPlanName(oldAutomaticContributionVO.getqACANonElectiveContribOtherPlanName());
    	automaticContributionVO.setqACANonElectiveContributionOptions(oldAutomaticContributionVO.getqACANonElectiveContributionOptions());
    	automaticContributionVO.setqACANonElectiveContributionPct(oldAutomaticContributionVO.getqACANonElectiveContributionPct());
    	automaticContributionVO.setqACAPlanHasEmpoyerContribution(oldAutomaticContributionVO.getqACAPlanHasEmpoyerContribution());
    	automaticContributionVO.setqACASHMACMatchContributionContribPct1(oldAutomaticContributionVO.getqACASHMACMatchContributionContribPct1());
    	automaticContributionVO.setqACASHMACMatchContributionContribPct2(oldAutomaticContributionVO.getqACASHMACMatchContributionContribPct2());
    	automaticContributionVO.setqACASHMACMatchContributionMatchPct1(oldAutomaticContributionVO.getqACASHMACMatchContributionMatchPct1());
    	automaticContributionVO.setqACASHMACMatchContributionMatchPct2(oldAutomaticContributionVO.getqACASHMACMatchContributionMatchPct2());
    	automaticContributionVO.setqACASPDEmpContribReference(oldAutomaticContributionVO.getqACASPDEmpContribReference());
    	automaticContributionVO.setqACAVestingGreaterThan2YearPct(oldAutomaticContributionVO.getqACAVestingGreaterThan2YearPct());
    	automaticContributionVO.setQACAAutoContribWithdrawaldays(oldAutomaticContributionVO.getQACAAutoContribWithdrawaldays());
    	automaticContributionVO.setQacaFullyVested(oldAutomaticContributionVO.getQacaFullyVested());
    	automaticContributionVO.seteACAContributionDays(oldAutomaticContributionVO.geteACAContributionDays());
   		automaticContributionVO.seteACAPlanHasEmpoyerContribution(oldAutomaticContributionVO.geteACAPlanHasEmpoyerContribution());
   		automaticContributionVO.seteACASPDEmployeeContRef(oldAutomaticContributionVO.geteACASPDEmployeeContRef());*/
   		
   		automaticContributionVO.setqACAArrangementOptions(null);
    	automaticContributionVO.setqACAAutomaticContribWithdrawal(null);
    	automaticContributionVO.setqACAisShmacOrShnec(null);
    	automaticContributionVO.setqACAIsSHMatchToCatchUp(null);
    	automaticContributionVO.setqACAIsSHMatchToRoth(null);
    	automaticContributionVO.setqACAMatchingContribOtherPlan(null);
    	automaticContributionVO.setqACAMatchingContribOtherPlanName(null);
    	automaticContributionVO.setqACAMatchOrNonElectiveCode(null);
    	automaticContributionVO.setqACANonElectiveContribOtherPlan(null);
    	automaticContributionVO.setqACANonElectiveContribOtherPlanName(null);
    	automaticContributionVO.setqACANonElectiveContributionOptions(null);
    	automaticContributionVO.setqACANonElectiveContributionPct(null);
    	automaticContributionVO.setqACAPlanHasEmpoyerContribution(null);
    	automaticContributionVO.setqACASHMACMatchContributionContribPct1(null);
    	automaticContributionVO.setqACASHMACMatchContributionContribPct2(null);
    	automaticContributionVO.setqACASHMACMatchContributionMatchPct1(null);
    	automaticContributionVO.setqACASHMACMatchContributionMatchPct2(null);
    	automaticContributionVO.setqACASPDEmpContribReference(null);
    	automaticContributionVO.setqACAVestingGreaterThan2YearPct(null);
    	automaticContributionVO.setQACAAutoContribWithdrawaldays(null);
    	automaticContributionVO.setQacaFullyVested(null);
    	automaticContributionVO.seteACAContributionDays(null);
   		automaticContributionVO.seteACAPlanHasEmpoyerContribution(null);
   		automaticContributionVO.seteACASPDEmployeeContRef(null);
   		automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(null);

	 logger.info("NoticePlanDataController setFormDataToEacaAndQacaAutomaticContributionVO ends...");
	return automaticContributionVO;

    	
    }
    /**
     * Method used to copy form data to eaca AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToEACAAutomaticContributionVO(TabPlanDataForm form,AutomaticContributionVO automaticContributionVO ,AutomaticContributionVO oldAutomaticContributionVO)throws SystemException
    {
    	 logger.info("NoticePlanDataController setFormDataToEACAAutomaticContributionVO starts...");
    	 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().trim().equalsIgnoreCase(Constants.EACA))
	     {
	       if(!TPAPlanDataWebUtility.isNull(form.getAutomaticContributionDays()))
	        {	        
	    	   if(form.getAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
	    	   {
	    		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDaysOther()))
	  			{
	    			automaticContributionVO.seteACAContributionDays(Integer.parseInt(form.getAutomaticContributionDaysOther()));	
	  			}else
	  			{
	  				automaticContributionVO.seteACAContributionDays(0);
	  			}
	    		
	    	   }else
	    	   {
	    		   automaticContributionVO.seteACAContributionDays(Integer.parseInt(form.getAutomaticContributionDays()));
	    	   }
	       }
	       if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutoContributionWD())){
	    	   if(Constants.YES.equalsIgnoreCase(form.getAutoContributionWD().trim())){
	    		   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(Constants.YES);
	    	   }
	    	   else if(Constants.NO.equalsIgnoreCase(form.getAutoContributionWD().trim())){
	    		   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(Constants.NO);
	    		   automaticContributionVO.seteACAContributionDays(null);
	    	   }  
	       }
	       else{
	    	   automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(null);
	    	   automaticContributionVO.seteACAContributionDays(null);
	       }
	       
	       if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()))
	        {
	    	automaticContributionVO.seteACAPlanHasEmpoyerContribution(form.getEmployerContributions().trim());
	    	if(form.getEmployerContributions().equalsIgnoreCase(Constants.YES))
	    	{
	        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSpdEmployerContributionRef()))
	        {
	    	automaticContributionVO.seteACASPDEmployeeContRef(form.getSpdEmployerContributionRef().trim());
	        }
	       }
	      }   	
	    	automaticContributionVO.setqACAArrangementOptions(null);
	    	automaticContributionVO.setqACAAutomaticContribWithdrawal(null);
	    	automaticContributionVO.setqACAisShmacOrShnec(null);
	    	automaticContributionVO.setqACAIsSHMatchToCatchUp(null);
	    	automaticContributionVO.setqACAIsSHMatchToRoth(null);
	    	automaticContributionVO.setqACAMatchingContribOtherPlan(null);
	    	automaticContributionVO.setqACAMatchingContribOtherPlanName(null);
	    	automaticContributionVO.setqACAMatchOrNonElectiveCode(null);
	    	automaticContributionVO.setqACANonElectiveContribOtherPlan(null);
	    	automaticContributionVO.setqACANonElectiveContribOtherPlanName(null);
	    	automaticContributionVO.setqACANonElectiveContributionOptions(null);
	    	automaticContributionVO.setqACANonElectiveContributionPct(null);
	    	automaticContributionVO.setqACAPlanHasEmpoyerContribution(null);
	    	automaticContributionVO.setqACASHMACMatchContributionContribPct1(null);
	    	automaticContributionVO.setqACASHMACMatchContributionContribPct2(null);
	    	automaticContributionVO.setqACASHMACMatchContributionMatchPct1(null);
	    	automaticContributionVO.setqACASHMACMatchContributionMatchPct2(null);
	    	automaticContributionVO.setqACASPDEmpContribReference(null);
	    	automaticContributionVO.setqACAVestingGreaterThan2YearPct(null);
	    	automaticContributionVO.setQACAAutoContribWithdrawaldays(null);
	    	automaticContributionVO.setQacaFullyVested(null);
	    	
	    }
    	 logger.info("NoticePlanDataController setFormDataToEACAAutomaticContributionVO ends...");
    	return automaticContributionVO;
    }
    /**
     * Method used to copy form data to  qaca AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToQACAAutomaticContributionVO(TabPlanDataForm form,AutomaticContributionVO automaticContributionVO, AutomaticContributionVO oldAutomaticContributionVO)throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToQACAAutomaticContributionVO starts...");
    	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().trim().equalsIgnoreCase(Constants.QACA))
        {
    		
    		// QACA SH Match section
    		
    		 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
    	       {
    			if(form.getqACAPlanHasSafeHarborMatchOrNonElective().equalsIgnoreCase(Constants.QACA_SHMAC))
    			{
    				
    		   if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct1()))
   	        {
     	    	automaticContributionVO.setqACASHMACMatchContributionContribPct1(form.getqACAMatchContributionContribPct1());
   	        }
     	       if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))   	    	   
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
     	       if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1()))
   	        {
     	    	automaticContributionVO.setqACASHMACMatchContributionMatchPct1(form.getqACAMatchContributionMatchPct1());
   	        }
   	  	   if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))
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
   	  	  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
   	       {
   	  		automaticContributionVO.setqACAMatchingContribOtherPlan(form.getqACAMatchContributionToAnotherPlan().trim());
   	       }
   	  	  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
   	       {
   	  		automaticContributionVO.setqACAMatchingContribOtherPlan(form.getqACAMatchContributionToAnotherPlan().trim());
   	  		if(form.getqACAMatchContributionToAnotherPlan().trim().equalsIgnoreCase(Constants.YES))
   	  		{
   	  	  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionOtherPlanName()))
  	       {
  	  		automaticContributionVO.setqACAMatchingContribOtherPlanName(form.getqACAMatchContributionOtherPlanName().trim());
  	       }
   	  		}
   	       }
   	  	  
   	  	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASafeHarborAppliesToRoth()))
	       {
	  		automaticContributionVO.setqACAIsSHMatchToRoth(form.getqACASafeHarborAppliesToRoth().trim());
	       }
	  	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHAppliesToCatchUpContributions()))
	       {
	  		automaticContributionVO.setqACAIsSHMatchToCatchUp(form.getqACASHAppliesToCatchUpContributions().trim());
	       }
	  	
	  	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
	       {
			automaticContributionVO.setQacaFullyVested(form.getqACASHMatchVesting().trim());
			if(form.getqACASHMatchVesting().trim().equalsIgnoreCase(Constants.NO))
			{
				if(!TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))
				{
					automaticContributionVO.setqACAVestingLessThan1YearPct(form.getqACASHMatchVestingPct1());
				}
				if(!TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2()))
				{
					automaticContributionVO.setaACAVesting1To2YearPct(form.getqACASHMatchVestingPct2());
				}
			}
	       }
		
	  	 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAArrangementOptions()))
	       {
			automaticContributionVO.setqACAArrangementOptions(form.getqACAArrangementOptions());
	       }
 		automaticContributionVO.setqACANonElectiveContributionPct(null);
 		automaticContributionVO.setqACANonElectiveContributionOptions(null);
 		automaticContributionVO.setqACANonElectiveContribOtherPlan(null);
 		automaticContributionVO.setqACANonElectiveContribOtherPlanName(null);

	   	  
    			}
    			
    			if(Constants.QACA_SHNEC.equalsIgnoreCase(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
    			{
    				// QACA SH Non elective contribution section
    		   	  	if(!TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
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
    		   	  /*	if(!TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
    		   	       {
    		   	  		automaticContributionVO.setqACANonElectiveContributionPct(form.getqACANonElectiveContributionPct());
    		   	       }
    		   	  */	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveAppliesToContrib()))
    		   	       {
    		   	  		automaticContributionVO.setqACANonElectiveContributionOptions(form.getqACANonElectiveAppliesToContrib());
    		   	       }
    		   	  	
    		   		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan()))
    		   	       {
    		   			automaticContributionVO.setqACANonElectiveContribOtherPlan(form.getqACANonElectiveContribOtherPlan().trim());
    		   			if(form.getqACANonElectiveContribOtherPlan().trim().equalsIgnoreCase(Constants.YES))
    		   			{
    		   			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHNonElectivePlanName()))
    		    	       {
    		    			automaticContributionVO.setqACANonElectiveContribOtherPlanName(form.getqACASHNonElectivePlanName());
    		    	       }
    		   			}
    		   	       }
    		   		
    		   		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
    			       {
    					automaticContributionVO.setQacaFullyVested(form.getqACASHMatchVesting().trim());
    					if(form.getqACASHMatchVesting().trim().equalsIgnoreCase(Constants.NO))
    					{
    					if(!TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))
    		 	       {
    		 			automaticContributionVO.setqACAVestingLessThan1YearPct(form.getqACASHMatchVestingPct1());
    		 	       }
    		 		if(!TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2()))
    		 	       {
    		 			automaticContributionVO.setaACAVesting1To2YearPct(form.getqACASHMatchVestingPct2());
    		 	       }
    					}
    			       }
    		   		
    		   		automaticContributionVO.setqACASHMACMatchContributionContribPct1(null);
    	    		automaticContributionVO.setqACASHMACMatchContributionContribPct2(null);
    	    		automaticContributionVO.setqACASHMACMatchContributionMatchPct1(null);
    	    		automaticContributionVO.setqACASHMACMatchContributionMatchPct2(null);
    	    		automaticContributionVO.setqACAMatchingContribOtherPlan(null);
    	    		automaticContributionVO.setqACAMatchingContribOtherPlanName(null);
    	    		automaticContributionVO.setqACAIsSHMatchToRoth(null);
    	    		automaticContributionVO.setqACAIsSHMatchToCatchUp(null);
    	    		/*automaticContributionVO.setQacaFullyVested(null);
    	    		automaticContributionVO.setqACAVestingLessThan1YearPct(null);
    	    		automaticContributionVO.setaACAVesting1To2YearPct(null);*/
    		 
    			}
    	       }
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
    	  	       {
    	   	  		if(form.getqACAAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    	     	    	   {
    	   	  			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDaysOther()))
    	   	  			{
    	     	    		automaticContributionVO.setQACAAutoContribWithdrawaldays(Integer.parseInt(form.getqACAAutomaticContributionDaysOther()));
    	   	  			}else
    	   	  			{
    	   	  				automaticContributionVO.setQACAAutoContribWithdrawaldays(0);
    	   	  			}
    	     	    	   }else
    	     	    	   {
    	     	    		automaticContributionVO.setQACAAutoContribWithdrawaldays(Integer.parseInt(form.getqACAAutomaticContributionDays()));
    	     	    	   }
    	   	      
    	  	       }
    			
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
    	   	       {
    	   			//automaticContributionVO.setqACAisShmacOrShnec(form.getqACAPlanHasSafeHarborMatchOrNonElective());
    	   			automaticContributionVO.setqACAMatchOrNonElectiveCode(form.getqACAPlanHasSafeHarborMatchOrNonElective());
    	   	       }
    	   		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC()))
    	   	       {
    	   			automaticContributionVO.setqACAAutomaticContribWithdrawal(form.getqACAPlanHasAdditionalEC().trim());
    	   	       }
    	   		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()))
    	   	       {
    	   			automaticContributionVO.setqACAPlanHasEmpoyerContribution(form.getqACAPlanHasAdditionalECon().trim());
    	   			if(form.getqACAPlanHasAdditionalECon().trim().equalsIgnoreCase(Constants.YES))
    	   			{
    	   			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASummaryPlanDesc()))
    	    	       {
    	    			automaticContributionVO.setqACASPDEmpContribReference(form.getqACASummaryPlanDesc().trim());
    	    	       }
    	   			}
    	   	       }
    	   		
    	   	  	
     	    
   	  	  		
   		
   	  
   	   
   		/*automaticContributionVO.seteACAContributionDays(oldAutomaticContributionVO.geteACAContributionDays());
   		automaticContributionVO.seteACAPlanHasEmpoyerContribution(oldAutomaticContributionVO.geteACAPlanHasEmpoyerContribution());
   		automaticContributionVO.seteACASPDEmployeeContRef(oldAutomaticContributionVO.geteACASPDEmployeeContRef());
   		*/
   	    automaticContributionVO.seteACAContributionDays(null);
   		automaticContributionVO.seteACAPlanHasEmpoyerContribution(null);
   		automaticContributionVO.seteACASPDEmployeeContRef(null);
   		automaticContributionVO.seteACAPlanHasAutoContributionWithdrawals(null);
   	   }
    	logger.info("NoticePlanDataController setFormDataToQACAAutomaticContributionVO ends..."); 
      
    	return automaticContributionVO;
    }
    /**
     * Method used to copy form data to  AutomaticContributionVO
     * @param form
     * @return AutomaticContributionVO
     */
    public AutomaticContributionVO setFormDataToAutomaticContributionVO(TabPlanDataForm form)throws SystemException
    {
    	logger.info("NoticePlanDataController setFormDataToAutomaticContributionVO starts...");
    	// Map form field values to AutomaticContributionVO
    	AutomaticContributionVO automaticContributionVO=new AutomaticContributionVO();
    	AutomaticContributionVO oldAutomaticContributionVO = form.getOldAutomaticContributionVO();
    	
    	 if(!TPAPlanDataWebUtility.isNull(form))
   	    {
   	        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType()))
   	        {
   	        	automaticContributionVO.setAutomaticEnrollmentType(form.getAutomaticContributionProvisionType().trim());
   	        }
   	        if(!TPAPlanDataWebUtility.isNull(form.getAutomaticContributionFeature1())&&form.getAutomaticContributionFeature1().equalsIgnoreCase("1"))
	        {
   	        	automaticContributionVO.setaCAContibLessPercentage(form.getContributionFeature1Pct());
	        }
   	       else
   	       {
   	    	   automaticContributionVO.setaCAContibLessPercentage(null);
   	       }
	   	  if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature2())&&form.getAutomaticContributionFeature2().equalsIgnoreCase("2"))
	      {
	   	        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionFeature2Date()))   	        	
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
	      }
	   	 else
	     {
	    	  automaticContributionVO.setaCAHiredAfterDate(null); 
	     }
	   	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature3())&&form.getAutomaticContributionFeature3().equalsIgnoreCase("3"))
	 	{
		       	automaticContributionVO.setaCAAppliesToCustom(form.getContributionFeature3SummaryText());
	 	}
	   	else
	    {
	    	  automaticContributionVO.setaCAAppliesToCustom(null); 
	    }
	    if(!TPAPlanDataWebUtility.isNull(form.getAcaAnnualIncreaseType()))
	        {	   
	    	  automaticContributionVO.setAcaAnnualIncreaseType(form.getAcaAnnualIncreaseType().trim());
	        }
   	   
   	     if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,ACA)){ 
   	   	     // EACA fields
   	   	     automaticContributionVO=setFormDataToEacaAndQacaAutomaticContributionVO(form,automaticContributionVO,oldAutomaticContributionVO);
   	   	     	    }   
   	     
   	     if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,EACA)){ 
   	     // EACA fields
   	     automaticContributionVO=setFormDataToEACAAutomaticContributionVO(form,automaticContributionVO,oldAutomaticContributionVO);
   	     	    } 
   	  if (StringUtils.equals(form.getAutomaticContributionProvisionType() ,QACA)){ 
   	        // QACA fields 
   	     automaticContributionVO=setFormDataToQACAAutomaticContributionVO(form,automaticContributionVO,oldAutomaticContributionVO);
   	     }
   	  	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDataCompleteInd()))
	    {
			automaticContributionVO.setDataCompleteInd(form.getAutomaticContributionDataCompleteInd());
	    } 
         automaticContributionVO.setLastUpdatedTimeStamp(new Date());
   	    }
    	 logger.info("NoticePlanDataController setFormDataToAutomaticContributionVO ends...");
    	return automaticContributionVO;
    }
    
    public TabPlanDataForm clearAutomaticContributionValuesFromForm(TabPlanDataForm form){
    	form.setAutomaticContributionProvisionType(null);
    	form.setAutomaticContributionProvisionTypeHidden(null);
    	form.setAutomaticContributionFeature1("0");
    	form.setAutomaticContributionFeature2("0");
    	form.setAutomaticContributionFeature3("0");
    	form.setContributionFeature1Pct(null);
    	form.setContributionFeature2Date(null);
    	form.setContributionFeature3SummaryText(null);
    	form.setEffectiveDate(null);
        form.setAutomaticContributionDays(null);
        form.setAutomaticContributionDaysOther(null);
        form.setEmployerContributions(null);
        form.setSpdEmployerContributionRef(null);
        form.setqACAPlanHasSafeHarborMatchOrNonElective(null);
        form.setqACAArrangementOptions(null);
        form.setqACAMatchContributionContribPct1(null);
        form.setqACAMatchContributionContribPct2(null);
        form.setqACAMatchContributionMatchPct1(null);
        form.setqACAMatchContributionMatchPct1Value(null);
        form.setqACAMatchContributionMatchPct2(null);
        form.setqACAMatchContributionToAnotherPlan(null);
        form.setqACAMatchContributionOtherPlanName(null);
        form.setqACASafeHarborAppliesToRoth(null);
        form.setqACASHAppliesToCatchUpContributions(null);
        form.setqACAAutomaticContributionDays(null);
        form.setqACAAutomaticContributionDaysOther(null);
        form.setqACANonElectiveContributionPct(null);
        form.setqACANonElectiveAppliesToContrib(null);
        form.setqACANonElectiveContribOtherPlan(null);
        form.setSHNonElectivePlanName(null);
        form.setqACAPlanHasAdditionalEC(null);
        form.setqACAPlanHasAdditionalECon(null);
        form.setqACASHMatchVesting(null);
        form.setqACASummaryPlanDesc(null);
        form.setqACASHMatchVestingPct1(null);
        form.setqACASHMatchVestingPct2(null);
        form.setNoticePlanDataVO(null);        
        form.setAcaAnnualIncreaseType(null);
        form.setAutoContributionWD(null);
        form.setDirty("false");
        return form;
    }
    
    /**
     * method to validate the ContributionAndDistribution tab field data
     * @param form
     * @return
     */
    public static List<GenericException> validateContributionAndDistribution(TabPlanDataForm form,NoticePlanCommonVO noticePlanCommonVO)throws SystemException 
    {
    	logger.info("NoticePlanDataController validateContributionAndDistribution starts...");
    	 List<GenericException> errors = new ArrayList<GenericException>();
    	 String contriAndDistriDataCompleteInd = Constants.YES;        
         	    	
			if (TPAPlanDataWebUtility.isNull(form.getPlanAllowRothDeferrals()))
			{				
				errors.add(new ValidationError("planAllowRothDeferrals"
						, ErrorCodes.MISSING_VALUES,
						Type.warning));
				contriAndDistriDataCompleteInd = Constants.NO;
			}
			if (TPAPlanDataWebUtility.isNull(form.getPlanAllowsInServiceWithdrawals()))
			{				
				errors.add(new ValidationError("planAllowsInServiceWithdrawals"
						, ErrorCodes.MISSING_VALUES,
						Type.warning));
				contriAndDistriDataCompleteInd = Constants.NO;
			}
			
			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSpdEmployeeContributionRef()))
			{				
				errors.add(new ValidationError("spdEmployeeContributionRef"
						, ErrorCodes.MISSING_VALUES,
						Type.warning));
				contriAndDistriDataCompleteInd = Constants.NO;
			}
			
			if(TPAPlanDataWebUtility.isNull(form.getContirbutionRestirictionOnHardships()))
			{
				
				errors.add(new ValidationError("contirbutionRestirictionOnHardships"
						, ErrorCodes.MISSING_VALUES,
						Type.warning));		
				contriAndDistriDataCompleteInd = Constants.NO;			
			}
			
			if(CsfConstants.NOTICE_OPT_SH.equalsIgnoreCase(form.getNoticeTypeSelected()) || CsfConstants.NOTICE_OPT_SH_QDIA.equalsIgnoreCase(form.getNoticeTypeSelected()) ||
					((CsfConstants.NOTICE_OPT_AUTO.equalsIgnoreCase(form.getNoticeTypeSelected()) || CsfConstants.NOTICE_OPT_AUTO_QDIA.equalsIgnoreCase(form.getNoticeTypeSelected())) && !"ACA".equalsIgnoreCase(form.getAutomaticContributionProvisionType()))) {
	     		if(Constants.NO.equalsIgnoreCase(contriAndDistriDataCompleteInd)) {
	     			errors.add(new ValidationError("autoNoticeSelected"
							, ErrorCodes.AUTO_NOTICE_SELECTED,
							Type.error));	
	     		}
	     	}
			
         form.setContriAndDistriDataCompleteInd(contriAndDistriDataCompleteInd);
         logger.info("NoticePlanDataController validateContributionAndDistribution ends...");
         return errors;
    }
    
    
    /**
     * To validate the Investment Information form data and add appropriate error messages
     * @param form
     * @return List
     */
    public static List<GenericException> validateInvestmentInfoFormData(TabPlanDataForm form)throws SystemException {
        logger.info("NoticePlanDataController validateInvestmentInfoFormData starts...");
        List<GenericException> errors = new ArrayList<GenericException>();
        String invInfoDataCompletedInd = Constants.YES;
        
        if(TPAPlanDataWebUtility.isNullOrEmpty(form.getdIOisQDIA())) {
            invInfoDataCompletedInd = Constants.NO;
            errors.add(new ValidationError("dIOisQDIA", ErrorCodes.MISSING_VALUES, Type.warning));
        }else if(Constants.YES.equalsIgnoreCase(form.getdIOisQDIA())){
            if(Constants.TRANSFER_OUT_DAYS_CUSTOM_CODE.equalsIgnoreCase(form.getTransferOutDaysCode())){
                if(TPAPlanDataWebUtility.isNullOrEmpty(form.getTransferOutDaysCustom())){
                    invInfoDataCompletedInd = Constants.NO;
                    errors.add(new ValidationError("transferOutDaysCustom", ErrorCodes.MISSING_VALUES, Type.error));
                }
            }
        }
        else if(Constants.NO.equalsIgnoreCase(form.getdIOisQDIA())) {
        	if(CsfConstants.NOTICE_OPT_QDIA.equalsIgnoreCase(form.getNoticeTypeSelected()) || CsfConstants.NOTICE_OPT_AUTO_QDIA.equalsIgnoreCase(form.getNoticeTypeSelected()) || CsfConstants.NOTICE_OPT_SH_QDIA.equalsIgnoreCase(form.getNoticeTypeSelected())) {
        		errors.add(new ValidationError("qdiaNoticeSelected", ErrorCodes.QDIA_NOTICE_SELECTED, Type.error));
        	}
        }
        form.setInvInfoDataCompleteInd(invInfoDataCompletedInd);
        logger.info("NoticePlanDataController validateInvestmentInfoFormData ends...");
        return errors;
    }

    
    /**
     * method to validate the Safe Harbor form data and add appropriate error messages
     * @param form
     * @param noticePlanDataVO TODO
     * @return List
     */
    public static List<GenericException> validateSafeHarborFormData(TabPlanDataForm form, NoticePlanCommonVO commonDataVO, NoticePlanDataVO noticePlanDataVO)throws SystemException{
    	logger.info("NoticePlanDataController validateSafeHarborFormData starts...");
        List<GenericException> errors = new ArrayList<GenericException>();
        String safeHarborDataCompleteInd = Constants.YES;
        
        
        if ((!TPAPlanDataWebUtility.isNullOrEmpty(form.getNoticeServiceInd()) && CsfConstants.CSF_YES.equalsIgnoreCase(form
				.getNoticeServiceInd()))
				&& (!TPAPlanDataWebUtility.isNullOrEmpty(form
						.getNoticeTypeSelected()) &&( CsfConstants.NOTICE_OPT_SH.equalsIgnoreCase(form
						.getNoticeTypeSelected())||CsfConstants.NOTICE_OPT_SH_QDIA.equalsIgnoreCase(form
														.getNoticeTypeSelected()))))
		{
        	if(Constants.NO.equalsIgnoreCase(form.getContriAndDistriDataCompleteInd())) {
        		errors.add(new ValidationError("safeHarborEnablePlanYearEndDatePerComp"
                        , ErrorCodes.MISSING_VALUES,
                        Type.error));
        	}
             
             if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEnablePlanYearEndDateAndPercentageComp()) && form.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.YES)) {
             	
             	if(TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionApplicableToPlanDate()) || TPAPlanDataWebUtility.isNull(form.getContributionApplicableToPlanPct())) {
             		errors.add(new ValidationError("safeHarborEnablePlanYearEndDatePerComp"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
             	}
             }
             
             //SH type
             if(TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSafeHarborMatchOrNonElective())){
                 errors.add(new ValidationError("planHasSafeHarborMatchOrNonElective"
                         , ErrorCodes.MISSING_VALUES,
                         Type.error));
                 safeHarborDataCompleteInd = Constants.NO;
             }
             //If SH type is Match, then below fields are mandatory
             else if(Constants.SH_MATCH.equals(form.getPlanHasSafeHarborMatchOrNonElective())){
                 //If the row1pct2 is <4 and if the row2 pct values are null/empty or 0.0, error mess is displayed
                 if(new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct1())).compareTo(new BigDecimal(4)) == -1){
                     if(TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2()) || TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())){
                         errors.add(new ValidationError("matchContributionPctFormula"
                                 , ErrorCodes.MISSING_VALUES,
                                 Type.error));
                         safeHarborDataCompleteInd = Constants.NO;
                     }
                     else if(new BigDecimal(Double.parseDouble(form.getMatchContributionContribPct2())).compareTo(BigDecimal.ZERO) == 0
                                 || new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct2())).compareTo(BigDecimal.ZERO) == 0){
                         errors.add(new ValidationError("matchContributionPctFormula"
                                 , ErrorCodes.MISSING_VALUES,
                                 Type.error));
                         safeHarborDataCompleteInd = Constants.NO;
                     }
                 }else
                 {
                 	
                 	  if((TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2())||(new BigDecimal(Double.parseDouble(form.getMatchContributionContribPct2())).compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2()))){
                           errors.add(new ValidationError("matchContributionPctFormula"
                                   , ErrorCodes.MISSING_VALUES,
                                   Type.error));
                           safeHarborDataCompleteInd = Constants.NO;
                       }
                       
                 	  if((TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())||(new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct2())).compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2()))){
                           errors.add(new ValidationError("matchContributionPctFormula"
                                   , ErrorCodes.MISSING_VALUES,
                                   Type.error));
                           safeHarborDataCompleteInd = Constants.NO;
                       }
                 }
                 
                 //If SH match another Plan is Yes and if the plan name is empty/null, warning mess is displayed
                 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionToAnotherPlan())){
                     if(Constants.YES.equals(form.getMatchContributionToAnotherPlan().trim())){
                         if(TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionOtherPlanName())){
                             errors.add(new ValidationError("matchContributionToAnotherPlanName"
                                     , ErrorCodes.SH_MATCH_ANOTHER_PLAN_NAME,
                                     Type.error));
                             safeHarborDataCompleteInd = Constants.NO;
                         } 
                     }
                 }

                 //Roth contribution
                 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSafeHarborAppliesToRoth())){
                     errors.add(new ValidationError("safeHarborAppliesToRoth"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
                 }
                 
                 //Catchup contribution
                 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getsHAppliesToCatchUpContributions())){
                     errors.add(new ValidationError("sHAppliesToCatchUpContributions"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
                 }
                 
             }
             
             //If SH type is Non-elective, then below fields are mandatory
             else if(Constants.SH_NON_ELECTIVE.equals(form.getPlanHasSafeHarborMatchOrNonElective())){
                 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOption())){
                     errors.add(new ValidationError("nonElectiveContribOption"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
                 }            
                 
                 //if the non-elective pct values are null/empty or 0.0, warning mess is displayed
                 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContributionPct())){
                     errors.add(new ValidationError("nonElectiveContributionPct"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
                 }
                 else if(new BigDecimal(Double.parseDouble(form.getNonElectiveContributionPct())).compareTo(BigDecimal.ZERO) == 0){
                     errors.add(new ValidationError("nonElectiveContributionPct"
                             , ErrorCodes.MISSING_VALUES,
                             Type.error));
                     safeHarborDataCompleteInd = Constants.NO;
                 }            
                 
                 //If SH NE another Plan is Yes and if the plan name is empty/null, warning mess is displayed
                 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOtherPlan())){
                     if(Constants.YES.equals(form.getNonElectiveContribOtherPlan())){
                         if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHNonElectivePlanName())){
                             errors.add(new ValidationError("sHNonElectivePlanName"
                                     , ErrorCodes.SH_NE_ANOTHER_PLAN_NAME,
                                     Type.error));
                             safeHarborDataCompleteInd = Constants.NO;
                         } 
                     }
                 }
             }
             
             ////TODO Plan allows for Automatic contribution arrangements -
     		
             if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSHACA())){
    			 if(Constants.YES.equals(form.getPlanHasSHACA()) ){
    				  
    				 if( !TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature1())  &&  !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature1()) &&
     						TPAPlanDataWebUtility.isNull(form.getShContributionFeature1Pct())) 
     				{
    					if(TPAPlanDataWebUtility.isNull(form.getShContributionFeature1Pct())  ){
     					errors.add(new ValidationError("shContributionFeature1Pct"
     							, ErrorCodes.MISSING_VALUES,
     							Type.error));
     					safeHarborDataCompleteInd = Constants.NO;
       				form.setShContributionFeature1PctMissing(true);

    					}else{
    	    				form.setShContributionFeature1PctMissing(false);

    					}
    					
     				}
     				
     				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature2()) && !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature2()) &&
     						TPAPlanDataWebUtility.isNullOrEmpty(form.getShContributionFeature2Date()))
     				{
     					errors.add(new ValidationError("shContributionFeature2Date"
     							, ErrorCodes.MISSING_VALUES,
     							Type.error));
     					safeHarborDataCompleteInd = Constants.NO;
     					form.setShContributionFeature2DateIdMissing(true);

    				}else{
        				form.setShContributionFeature2DateIdMissing(false);

    				}
     				
     				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature3()) && !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature3()) && 
     						TPAPlanDataWebUtility.isNullOrEmpty(form.getShContributionFeature3SummaryText()))
     				{
     					errors.add(new ValidationError("shContributionFeature3SummaryText"
     							, ErrorCodes.MISSING_VALUES,
     							Type.error));
     					safeHarborDataCompleteInd = Constants.NO;
     					form.setShContributionFeature3SummaryTextMissing(true);

    				}else{
        				form.setShContributionFeature3SummaryTextMissing(false);

    				}
     				
     				if(!TPAPlanDataWebUtility.isNullOrEmpty(commonDataVO.getAciAllowed()) && commonDataVO.getAciAllowed().equalsIgnoreCase(Constants.YES))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getShACAAnnualIncreaseType()))
    					{
    						errors.add(new ValidationError("aciApplyDate"
    								, ErrorCodes.ANNUAL_INCREASE_UNSELECTED,
    								Type.error));
    					} 
    				}
    				 
    				 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutoContributionWD())){
    	                    errors.add(new ValidationError("SHAutoContributionWD"
    	                            , ErrorCodes.MISSING_VALUES,
    	                            Type.error));
    	                    safeHarborDataCompleteInd = Constants.NO;
    	                } 
    				 
    				 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutoContributionWD()) && form.getSHAutoContributionWD().equalsIgnoreCase(Constants.YES) 
    						   && form.getSHAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    				 {
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutomaticContributionDaysOther()))
     					{
     						errors.add(new ValidationError("SHAutomaticContributionDaysOther"
     								, ErrorCodes.MISSING_VALUES,
     								Type.error));
     					} 
 	                } 
    			 }
    			
    		}
    		
     		
     		
             //TODO Additional Employee Contribution desc is mandatory - No Mandatory field validation is given in DFS. Need to get confirm
             if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasAdditionalEC())){
                 if(Constants.YES.equals(form.getPlanHasAdditionalEC())){
                     if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSummaryPlanDesc())){
                         errors.add(new ValidationError("summaryPlanDesc"
                                 , ErrorCodes.MISSING_VALUES,
                                 Type.error));
                         safeHarborDataCompleteInd = Constants.NO;
                     } 
                 }
             }
             //TODO No Mandatory field validation is given in DFS. Need to get confirm
             else {
                 errors.add(new ValidationError("PlanHasAdditionalEmpContribution"
                         , ErrorCodes.MISSING_VALUES,
                         Type.error));
                 safeHarborDataCompleteInd = Constants.NO;
             }
             //TODO replace with cma key for tab 2 completion
             if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContriAndDistriDataCompleteInd())&& form.getContriAndDistriDataCompleteInd().equalsIgnoreCase(Constants.NO))
     		{
         			automaticContributionDataCompleted = Constants.NO;
         		errors.add(new ValidationError("contributionDataCompleted"
     		, ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN,
     		Type.error));
     		}
	
		}else
		{
        
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEnablePlanYearEndDateAndPercentageComp()) && form.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.YES)) {
        	if(TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionApplicableToPlanDate()) || TPAPlanDataWebUtility.isNull(form.getContributionApplicableToPlanPct())) {
        		errors.add(new ValidationError("safeHarborEnablePlanYearEndDatePerComp"
                        , ErrorCodes.MISSING_VALUES,
                        Type.error));
                safeHarborDataCompleteInd = Constants.NO;
        	}
        }
        
        //SH type
        if(TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSafeHarborMatchOrNonElective())){
            errors.add(new ValidationError("planHasSafeHarborMatchOrNonElective"
                    , ErrorCodes.MISSING_VALUES,
                    Type.warning));
            safeHarborDataCompleteInd = Constants.NO;
        }
        //If SH type is Match, then below fields are mandatory
        else if(Constants.SH_MATCH.equals(form.getPlanHasSafeHarborMatchOrNonElective())){
            //If the row1pct2 is <4 and if the row2 pct values are null/empty or 0.0, error mess is displayed
            if(new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct1())).compareTo(new BigDecimal(4)) == -1){
                if(TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2()) || TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())){
                    errors.add(new ValidationError("matchContributionPctFormula"
                            , ErrorCodes.MISSING_VALUES,
                            Type.error));
                    safeHarborDataCompleteInd = Constants.NO;
                }
                else if(new BigDecimal(Double.parseDouble(form.getMatchContributionContribPct2())).compareTo(BigDecimal.ZERO) == 0
                            || new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct2())).compareTo(BigDecimal.ZERO) == 0){
                    errors.add(new ValidationError("matchContributionPctFormula"
                            , ErrorCodes.MISSING_VALUES,
                            Type.error));
                    safeHarborDataCompleteInd = Constants.NO;
                }
            }else
            {
            	
            	  if((TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2())||(new BigDecimal(Double.parseDouble(form.getMatchContributionContribPct2())).compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2()))){
                      errors.add(new ValidationError("matchContributionPctFormula"
                              , ErrorCodes.MISSING_VALUES,
                              Type.error));
                      safeHarborDataCompleteInd = Constants.NO;
                  }
                  
            	  if((TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionMatchPct2())||(new BigDecimal(Double.parseDouble(form.getMatchContributionMatchPct2())).compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionContribPct2()))){
                      errors.add(new ValidationError("matchContributionPctFormula"
                              , ErrorCodes.MISSING_VALUES,
                              Type.error));
                      safeHarborDataCompleteInd = Constants.NO;
                  }
            }
            
            //If SH match another Plan is Yes and if the plan name is empty/null, warning mess is displayed
            if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionToAnotherPlan())){
                if(Constants.YES.equals(form.getMatchContributionToAnotherPlan().trim())){
                    if(TPAPlanDataWebUtility.isNullOrEmpty(form.getMatchContributionOtherPlanName())){
                        errors.add(new ValidationError("matchContributionToAnotherPlanName"
                                , ErrorCodes.SH_MATCH_ANOTHER_PLAN_NAME,
                                Type.error));
                        safeHarborDataCompleteInd = Constants.NO;
                    } 
                }
            }

            //Roth contribution
            if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSafeHarborAppliesToRoth())){
                errors.add(new ValidationError("safeHarborAppliesToRoth"
                        , ErrorCodes.MISSING_VALUES,
                        Type.warning));
                safeHarborDataCompleteInd = Constants.NO;
            }
            
            //Catchup contribution
            if(TPAPlanDataWebUtility.isNullOrEmpty(form.getsHAppliesToCatchUpContributions())){
                errors.add(new ValidationError("sHAppliesToCatchUpContributions"
                        , ErrorCodes.MISSING_VALUES,
                        Type.warning));
                safeHarborDataCompleteInd = Constants.NO;
            }
            
        }
        
        //If SH type is Non-elective, then below fields are mandatory
        else if(Constants.SH_NON_ELECTIVE.equals(form.getPlanHasSafeHarborMatchOrNonElective())){
            if(TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOption())){
                errors.add(new ValidationError("nonElectiveContribOption"
                        , ErrorCodes.MISSING_VALUES,
                        Type.error));
                safeHarborDataCompleteInd = Constants.NO;
            }            
            
            //if the non-elective pct values are null/empty or 0.0, warning mess is displayed
            if(TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContributionPct())){
                errors.add(new ValidationError("nonElectiveContributionPct"
                        , ErrorCodes.MISSING_VALUES,
                        Type.error));
                safeHarborDataCompleteInd = Constants.NO;
            }
            else if(new BigDecimal(Double.parseDouble(form.getNonElectiveContributionPct())).compareTo(BigDecimal.ZERO) == 0){
                errors.add(new ValidationError("nonElectiveContributionPct"
                        , ErrorCodes.MISSING_VALUES,
                        Type.error));
                safeHarborDataCompleteInd = Constants.NO;
            }            
            
            //If SH NE another Plan is Yes and if the plan name is empty/null, warning mess is displayed
            if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getNonElectiveContribOtherPlan())){
                if(Constants.YES.equals(form.getNonElectiveContribOtherPlan())){
                    if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHNonElectivePlanName())){
                        errors.add(new ValidationError("sHNonElectivePlanName"
                                , ErrorCodes.SH_NE_ANOTHER_PLAN_NAME,
                                Type.error));
                        safeHarborDataCompleteInd = Constants.NO;
                    } 
                }
            }
        }
        

        ////TODO Plan allows for Automatic contribution arrangements -
        boolean planAllowForACAIndicator = true;
      		if(null!=noticePlanDataVO && null!=noticePlanDataVO.getSafeHarborVO()){
      			planAllowForACAIndicator = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getSafeHarborVO().getPlanHasSHACA());
      		}
		
		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasSHACA()) && planAllowForACAIndicator){
			 if(Constants.YES.equals(form.getPlanHasSHACA()) ){
				  
				 if( !TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature1())  &&  !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature1()) &&
 						TPAPlanDataWebUtility.isNull(form.getShContributionFeature1Pct())) 
 				{
					if(TPAPlanDataWebUtility.isNull(form.getShContributionFeature1Pct()) ){
 					errors.add(new ValidationError("shContributionFeature1Pct"
 							, ErrorCodes.MISSING_VALUES,
 							Type.error));
 					safeHarborDataCompleteInd = Constants.NO;
   				form.setShContributionFeature1PctMissing(true);

					}else{
	    				form.setShContributionFeature1PctMissing(false);

					}
					
 				}
 				
 				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature2()) && !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature2()) &&
 						TPAPlanDataWebUtility.isNullOrEmpty(form.getShContributionFeature2Date()))
 				{
 					errors.add(new ValidationError("shContributionFeature2Date"
 							, ErrorCodes.MISSING_VALUES,
 							Type.error));
 					safeHarborDataCompleteInd = Constants.NO;
 					form.setShContributionFeature2DateIdMissing(true);

				}else{
    				form.setShContributionFeature2DateIdMissing(false);

				}
 				
 				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getsHautomaticContributionFeature3()) && !("0").equalsIgnoreCase(form.getsHautomaticContributionFeature3()) && 
 						TPAPlanDataWebUtility.isNullOrEmpty(form.getShContributionFeature3SummaryText()))
 				{
 					errors.add(new ValidationError("shContributionFeature3SummaryText"
 							, ErrorCodes.MISSING_VALUES,
 							Type.error));
 					safeHarborDataCompleteInd = Constants.NO;
 					form.setShContributionFeature3SummaryTextMissing(true);

				}else{
    				form.setShContributionFeature3SummaryTextMissing(false);

				}
 				
				 
				 if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutoContributionWD())){
	                    errors.add(new ValidationError("SHAutoContributionWD"
	                            , ErrorCodes.MISSING_VALUES,
	                            Type.error));
	                    safeHarborDataCompleteInd = Constants.NO;
	                } 
				 
				 if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutoContributionWD()) && form.getSHAutoContributionWD().equalsIgnoreCase(Constants.YES) 
						   && form.getSHAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
				 {
					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSHAutomaticContributionDaysOther()))
					{
						errors.add(new ValidationError("SHAutomaticContributionDaysOther"
								, ErrorCodes.MISSING_VALUES,
								Type.error));
					} 
				 } 
				 
				 if(!TPAPlanDataWebUtility.isNullOrEmpty(commonDataVO.getAciAllowed()) && commonDataVO.getAciAllowed().equalsIgnoreCase(Constants.YES))
					{
						if(TPAPlanDataWebUtility.isNullOrEmpty(form.getShACAAnnualIncreaseType()))
						{
							errors.add(new ValidationError("aciApplyDate"
									, ErrorCodes.ANNUAL_INCREASE_UNSELECTED,
									Type.error));
						} 
					}
			 }
						
		}
		
		
    		
        
        boolean summaryPlanInd = true;
		if(null!=noticePlanDataVO && null!=noticePlanDataVO.getSafeHarborVO()){
			summaryPlanInd = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getSafeHarborVO().getPlanHasAdditionalEmpContribution());
		}
		
		
        //TODO Additional Employee Contribution desc is mandatory - No Mandatory field validation is given in DFS. Need to get confirm
        if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getPlanHasAdditionalEC())){
            if(Constants.YES.equals(form.getPlanHasAdditionalEC()) && summaryPlanInd){
                if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSummaryPlanDesc())){
                    errors.add(new ValidationError("summaryPlanDesc"
                            , ErrorCodes.MISSING_VALUES,
                            Type.error));
                    safeHarborDataCompleteInd = Constants.NO;
                } 
                if(form.isShEnablePopUpForEmployerContributions()) {
                	safeHarborDataCompleteInd = Constants.NO;
            		errors.add(new ValidationError("safeHarborEmployerContributionYes"
                            , ErrorCodes.SH_ADDN_CONTR_YES,
                            Type.error));
            	}
            }
        }
        //TODO No Mandatory field validation is given in DFS. Need to get confirm
        else {
            errors.add(new ValidationError("PlanHasAdditionalEmpContribution"
                    , ErrorCodes.MISSING_VALUES,
                    Type.warning));
            safeHarborDataCompleteInd = Constants.NO;
        }
        
		}
    
        form.setSafeHarborDataCompleteInd(safeHarborDataCompleteInd);
        logger.info("NoticePlanDataController validateSafeHarborFormData ends...");
        return errors;
    }
    /**
     * method to validate the EACA AutomaticDistribution tab field data
     * @param form
     * @param commonDataVO TODO
     * @param noticePlanDataVO TODO
     * @return
     */
    public static List<GenericException> validateEACAAutomaticContribution(TabPlanDataForm form,List<GenericException> errors, NoticePlanCommonVO commonDataVO, NoticePlanDataVO noticePlanDataVO)throws SystemException
    {
    	logger.info("NoticePlanDataController validateEACAAutomaticContribution starts...");
    	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().equalsIgnoreCase(Constants.EACA) )
    	{

    		if ((!TPAPlanDataWebUtility.isNullOrEmpty(form.getNoticeServiceInd()) && form
    				.getNoticeServiceInd().equalsIgnoreCase(CsfConstants.CSF_YES))
    				&& (!TPAPlanDataWebUtility.isNullOrEmpty(form
    						.getNoticeTypeSelected()) && (form
    								.getNoticeTypeSelected().equalsIgnoreCase(
    										CsfConstants.NOTICE_OPT_AUTO))||(form
    												.getNoticeTypeSelected().equalsIgnoreCase(
    														CsfConstants.NOTICE_OPT_AUTO_QDIA))))
    		{
    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDays()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("automaticContributionDays"
    						, ErrorCodes.MISSING_VALUES,
    						Type.error));
    			}
    			if(!TPAPlanDataWebUtility.isNull(form.getAutomaticContributionDays()))
    			{	        
    				if(form.getAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDaysOther()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("automaticContributionDaysOther"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));	
    					}

    				}
    			}
    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("employerContributions"
    						, ErrorCodes.MISSING_VALUES,
    						Type.error));
    			}
    			
    			boolean summaryPlanInd = true;
				if(null!=noticePlanDataVO && null!=noticePlanDataVO.getAutomaticEnrollmentVO()){
					summaryPlanInd = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().geteACAPlanHasEmpoyerContribution());
				}

    			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()) && (Constants.YES).equalsIgnoreCase(form.getEmployerContributions()) && summaryPlanInd)
    			{
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSpdEmployerContributionRef()))
    				{	
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("spdEmployerContributionRef"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    				}

    				if(form.isEacaEnablePopUpForEmployerContributions()) {
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("eacaAutoContribYes"
    							, ErrorCodes.EACA_EMP_CONTRIB,
    							Type.error));
    				}
    			}
    			
    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutoContributionWD()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("autoContributionWD"
    						, ErrorCodes.MISSING_VALUES,
    						Type.error));
    			}
    		}else
    		{

    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDays()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("automaticContributionDays"
    						, ErrorCodes.MISSING_VALUES,
    						Type.warning));
    			}
    			if(!TPAPlanDataWebUtility.isNull(form.getAutomaticContributionDays()))
    			{	        
    				if(form.getAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionDaysOther()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("automaticContributionDaysOther"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));	
    					}

    				}
    			}
    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("employerContributions"
    						, ErrorCodes.MISSING_VALUES,
    						Type.warning));
    			}
    			
    			boolean summaryPlanInd = true;
				if(null!=noticePlanDataVO && null!=noticePlanDataVO.getAutomaticEnrollmentVO()){
					summaryPlanInd = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().geteACAPlanHasEmpoyerContribution());
				}

    			if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getEmployerContributions()) && (Constants.YES).equalsIgnoreCase(form.getEmployerContributions()) && summaryPlanInd)
    			{
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getSpdEmployerContributionRef()))
    				{	
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("spdEmployerContributionRef"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    				}

    				if(form.isEacaEnablePopUpForEmployerContributions()) {
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("eacaAutoContribYes"
    							, ErrorCodes.EACA_EMP_CONTRIB,
    							Type.error));
    				}
    			}
    			if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutoContributionWD()))
    			{
    				automaticContributionDataCompleted = Constants.NO;
    				errors.add(new ValidationError("autoContributionWD"
    						, ErrorCodes.MISSING_VALUES,
    						Type.warning));
    			}
    			/*if(noticePlanCommonVO.getVestingSchedules().size() == form.getExcludeCount()){
            errors.add(new ValidationError("vesting"
                    , ErrorCodes.INDICATE_ATLEAST_ONE_MONEY_TYPE_FOR_VESTING_SCHEDULE,
                    Type.error));

        }*/
    			//TODO replace with cma key for tab 2 completion
    			/*	if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContriAndDistriDataCompleteInd())&& form.getContriAndDistriDataCompleteInd().equalsIgnoreCase(Constants.NO))
		{
    			automaticContributionDataCompleted = Constants.NO;
    		errors.add(new ValidationError("contributionDataCompleted"
		, ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN,
		Type.warning));
		}	
    			 */		}
        }
    	logger.info("NoticePlanDataController validateEACAAutomaticContribution ends...");
    	return errors;
    }
    	/**
    	 * method to validate the QACA AutomaticDistribution tab field data
    	 * @param form
    	 * @param commonDataVO TODO
    	 * @param noticePlanDataVO TODO
    	 * @return
    	 */
    	public static List<GenericException> validateQACAAutomaticContribution(TabPlanDataForm form,List<GenericException> errors, NoticePlanCommonVO commonDataVO, NoticePlanDataVO noticePlanDataVO)throws SystemException
    	{
    		logger.info("NoticePlanDataController validateQACAAutomaticContribution starts...");
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType())&&form.getAutomaticContributionProvisionType().equalsIgnoreCase(Constants.QACA) )
    		{	

    			if ((!TPAPlanDataWebUtility.isNullOrEmpty(form.getNoticeServiceInd()) && CsfConstants.CSF_YES.equalsIgnoreCase(form
    					.getNoticeServiceInd()))
    					&& (!TPAPlanDataWebUtility.isNullOrEmpty(form
    							.getNoticeTypeSelected()) && (
										CsfConstants.NOTICE_OPT_AUTO.equalsIgnoreCase(form
    									.getNoticeTypeSelected())||
												CsfConstants.NOTICE_OPT_AUTO_QDIA.equalsIgnoreCase(form
    													.getNoticeTypeSelected()))))
    			{

    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
    				{	
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasSafeHarborMatchOrNonElective"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    				}
    				//QACA SH Match Vesting validation
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective())&&form.getqACAPlanHasSafeHarborMatchOrNonElective().equalsIgnoreCase(Constants.QACA_SHMAC))
    				{	
    					if(TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct1()) || TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAMatchContributionContribPct1"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));

    					}
    					//If the row1pct2 is <4 and if the row2 pct values are null/empty or 0.0, warning mess is displayed
    					if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1())&&form.getqACAMatchContributionMatchPct1().compareTo(new BigDecimal(4)) == -1){
    						if(TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()) || TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}else if((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2())&&form.getqACAMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0)
    								|| ((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))&&form.getqACAMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0)){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}else
    					{
    						if((TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2())||(form.getqACAMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}

    						if((TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())||(form.getqACAMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}
    					if((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))&&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())))
    					{
    						if(form.getqACAMatchContributionMatchPct2().compareTo(form.getqACAMatchContributionMatchPct1())==-1)
    						{
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.QACA_MATCH_CONTRIB_ROW2,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHMatchVesting"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting())&&form.getqACASHMatchVesting().equalsIgnoreCase(Constants.NO))
    					{
    						//if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1())||(form.getqACASHMatchVestingPct1().compareTo(BigDecimal.ZERO)==0))&&(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())||(form.getqACASHMatchVestingPct2().compareTo(BigDecimal.ZERO)==0)))
    						// TODO replace new cma key for TPDD.305
    						if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))||(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())))
    						{        				 
    							errors.add(new ValidationError("qACASHMatchVestingPct1"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));

    						}    			

    					}
    					if(TPAPlanDataWebUtility.isNull(form.getqACAArrangementOptions()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAArrangementOptions"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAMatchContributionToAnotherPlan"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}

    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan())&& form.getqACAMatchContributionToAnotherPlan().equalsIgnoreCase(Constants.YES))
    					{
    						if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionOtherPlanName()))
    						{	 
    							automaticContributionDataCompleted = Constants.NO;
    							errors.add(new ValidationError("qACAMatchContributionOtherPlanName"
    									, ErrorCodes.QACA_MATCH_ANOTHER_PLAN_NAME,
    									Type.error));
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASafeHarborAppliesToRoth()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASafeHarborAppliesToRoth"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHAppliesToCatchUpContributions()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHAppliesToCatchUpContributions"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}


    				}
    				//QACA SH Non Elective validation
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective())&&form.getqACAPlanHasSafeHarborMatchOrNonElective().equalsIgnoreCase(Constants.QACA_SHNEC))
    				{
    					if(TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveContributionPct"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}else if((!TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct())&&form.getqACANonElectiveContributionPct().compareTo(BigDecimal.ZERO) == 0))
    					{
    						errors.add(new ValidationError("qACANonElectiveContributionPct"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    						automaticContributionDataCompleted = Constants.NO;
    					}
    					if(TPAPlanDataWebUtility.isNull(form.getqACANonElectiveAppliesToContrib()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveAppliesToContrib"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveContribOtherPlan"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan())&&form.getqACANonElectiveContribOtherPlan().equalsIgnoreCase(Constants.YES) )
    					{	
    						if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHNonElectivePlanName()))
    						{	
    							automaticContributionDataCompleted = Constants.NO;
    							errors.add(new ValidationError("qACASHNonElectivePlanName"
    									, ErrorCodes.QACA_NE_ANOTHER_PLAN_NAME,
    									Type.error));
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHMatchVesting"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting())&&form.getqACASHMatchVesting().equalsIgnoreCase(Constants.NO))
    					{
    						//if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1())||(form.getqACASHMatchVestingPct1().compareTo(BigDecimal.ZERO)==0))&&(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())||(form.getqACASHMatchVestingPct2().compareTo(BigDecimal.ZERO)==0)))
    						// TODO replace new cma key for TPDD.305
    						if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))||(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())))
    						{        				 
    							errors.add(new ValidationError("qACASHMatchVestingPct1"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));

    						}    			

    					}
    				}
    				//QACA SH Match or NonElective Validation
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC()))
    				{
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasAdditionalEC"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    				}
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC())&&form.getqACAPlanHasAdditionalEC().equalsIgnoreCase(Constants.YES))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAAutomaticContributionDays"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
    					{
    						if(form.getqACAAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    						{
    							if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDaysOther()))
    							{
    								automaticContributionDataCompleted = Constants.NO;
    								errors.add(new ValidationError("qACAAutomaticContributionDaysOther"
    										, ErrorCodes.MISSING_VALUES,
    										Type.error));
    							}
    						}

    					}


    				}
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()))
    				{
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasAdditionalECon"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    				}
    				boolean summaryPlanInd = true;
    				if(null!=noticePlanDataVO && null!=noticePlanDataVO.getAutomaticEnrollmentVO()){
    					summaryPlanInd = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().getqACAPlanHasEmpoyerContribution());
    				}

    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()) && Constants.YES.equalsIgnoreCase(form.getqACAPlanHasAdditionalECon()) && summaryPlanInd)
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASummaryPlanDesc()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASummaryPlanDesc"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(form.isQacaEnablePopUpForEmployerContributions()) {
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qacaAutoContribYes"
    								, ErrorCodes.QACA_EMP_CONTRIB,
    								Type.error));
    					}
    				}
    				
    				//TODO replace with cma key for tab 2 completion
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getContriAndDistriDataCompleteInd())&& form.getContriAndDistriDataCompleteInd().equalsIgnoreCase(Constants.NO))
    				{
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("contributionDataCompleted"
    							, ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN,
    							Type.error));
    				}

    			}else
    			{


    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective()))
    				{	
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasSafeHarborMatchOrNonElective"
    							, ErrorCodes.MISSING_VALUES,
    							Type.warning));
    				}
    				//QACA SH Match Vesting validation
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective())&&form.getqACAPlanHasSafeHarborMatchOrNonElective().equalsIgnoreCase(Constants.QACA_SHMAC))
    				{	
    					if(TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct1()) || TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAMatchContributionContribPct1"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));

    					}
    					//If the row1pct2 is <4 and if the row2 pct values are null/empty or 0.0, warning mess is displayed
    					if(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct1())&&form.getqACAMatchContributionMatchPct1().compareTo(new BigDecimal(4)) == -1){
    						if(TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()) || TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}else if((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2())&&form.getqACAMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0)
    								|| ((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))&&form.getqACAMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0)){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}else
    					{
    						if((TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2())||(form.getqACAMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2()))){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}

    						if((TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())||(form.getqACAMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 0)) &&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))){
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}
    					if((!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionContribPct2()))&&(!TPAPlanDataWebUtility.isNull(form.getqACAMatchContributionMatchPct2())))
    					{
    						if(form.getqACAMatchContributionMatchPct2().compareTo(form.getqACAMatchContributionMatchPct1())==-1)
    						{
    							errors.add(new ValidationError("qACAMatchContributionContribPct2"
    									, ErrorCodes.QACA_MATCH_CONTRIB_ROW2,
    									Type.error));
    							automaticContributionDataCompleted = Constants.NO;
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHMatchVesting"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting())&&form.getqACASHMatchVesting().equalsIgnoreCase(Constants.NO))
    					{
    						//if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1())||(form.getqACASHMatchVestingPct1().compareTo(BigDecimal.ZERO)==0))&&(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())||(form.getqACASHMatchVestingPct2().compareTo(BigDecimal.ZERO)==0)))
    						// TODO replace new cma key for TPDD.305
    						if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))||(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())))
    						{        				 
    							errors.add(new ValidationError("qACASHMatchVestingPct1"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));

    						}    			

    					}
    					if(TPAPlanDataWebUtility.isNull(form.getqACAArrangementOptions()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAArrangementOptions"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAMatchContributionToAnotherPlan"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}

    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionToAnotherPlan())&& form.getqACAMatchContributionToAnotherPlan().equalsIgnoreCase(Constants.YES))
    					{
    						if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAMatchContributionOtherPlanName()))
    						{	 
    							automaticContributionDataCompleted = Constants.NO;
    							errors.add(new ValidationError("qACAMatchContributionOtherPlanName"
    									, ErrorCodes.QACA_MATCH_ANOTHER_PLAN_NAME,
    									Type.error));
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASafeHarborAppliesToRoth()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASafeHarborAppliesToRoth"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHAppliesToCatchUpContributions()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHAppliesToCatchUpContributions"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}


    				}
    				//QACA SH Non Elective validation
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasSafeHarborMatchOrNonElective())&&form.getqACAPlanHasSafeHarborMatchOrNonElective().equalsIgnoreCase(Constants.QACA_SHNEC))
    				{
    					if(TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveContributionPct"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}else if((!TPAPlanDataWebUtility.isNull(form.getqACANonElectiveContributionPct())&&form.getqACANonElectiveContributionPct().compareTo(BigDecimal.ZERO) == 0))
    					{
    						errors.add(new ValidationError("qACANonElectiveContributionPct"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    						automaticContributionDataCompleted = Constants.NO;
    					}
    					if(TPAPlanDataWebUtility.isNull(form.getqACANonElectiveAppliesToContrib()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveAppliesToContrib"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan()))
    					{	
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACANonElectiveContribOtherPlan"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACANonElectiveContribOtherPlan())&&form.getqACANonElectiveContribOtherPlan().equalsIgnoreCase(Constants.YES) )
    					{	
    						if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHNonElectivePlanName()))
    						{	
    							automaticContributionDataCompleted = Constants.NO;
    							errors.add(new ValidationError("qACASHNonElectivePlanName"
    									, ErrorCodes.QACA_NE_ANOTHER_PLAN_NAME,
    									Type.error));
    						}
    					}
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASHMatchVesting"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASHMatchVesting())&&form.getqACASHMatchVesting().equalsIgnoreCase(Constants.NO))
    					{
    						//if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1())||(form.getqACASHMatchVestingPct1().compareTo(BigDecimal.ZERO)==0))&&(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())||(form.getqACASHMatchVestingPct2().compareTo(BigDecimal.ZERO)==0)))
    						// TODO replace new cma key for TPDD.305
    						if((TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct1()))||(TPAPlanDataWebUtility.isNull(form.getqACASHMatchVestingPct2())))
    						{        				 
    							errors.add(new ValidationError("qACASHMatchVestingPct1"
    									, ErrorCodes.MISSING_VALUES,
    									Type.error));

    						}    			

    					}
    				}
    				//QACA SH Match or NonElective Validation
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC()))
    				{
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasAdditionalEC"
    							, ErrorCodes.MISSING_VALUES,
    							Type.warning));
    				}
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalEC())&&form.getqACAPlanHasAdditionalEC().equalsIgnoreCase(Constants.YES))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACAAutomaticContributionDays"
    								, ErrorCodes.MISSING_VALUES,
    								Type.warning));
    					}
    					if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDays()))
    					{
    						if(form.getqACAAutomaticContributionDays().equalsIgnoreCase(Constants.CONTRIBUTIONS_DAYS_OTHER))
    						{
    							if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAAutomaticContributionDaysOther()))
    							{
    								automaticContributionDataCompleted = Constants.NO;
    								errors.add(new ValidationError("qACAAutomaticContributionDaysOther"
    										, ErrorCodes.MISSING_VALUES,
    										Type.error));
    							}
    						}

    					}


    				}
    				if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()))
    				{
    					automaticContributionDataCompleted = Constants.NO;
    					errors.add(new ValidationError("qACAPlanHasAdditionalECon"
    							, ErrorCodes.MISSING_VALUES,
    							Type.warning));
    				}
    				boolean summaryPlanInd = true;
    				if(null!=noticePlanDataVO && null!=noticePlanDataVO.getAutomaticEnrollmentVO()){
    					summaryPlanInd = (Constants.YES).equalsIgnoreCase(noticePlanDataVO.getAutomaticEnrollmentVO().getqACAPlanHasEmpoyerContribution());
    				}

    				if(!TPAPlanDataWebUtility.isNullOrEmpty(form.getqACAPlanHasAdditionalECon()) && (Constants.YES).equalsIgnoreCase(form.getqACAPlanHasAdditionalECon()) && summaryPlanInd)
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getqACASummaryPlanDesc()))
    					{
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qACASummaryPlanDesc"
    								, ErrorCodes.MISSING_VALUES,
    								Type.error));
    					}
    					if(form.isQacaEnablePopUpForEmployerContributions()) {
    						automaticContributionDataCompleted = Constants.NO;
    						errors.add(new ValidationError("qacaAutoContribYes"
    								, ErrorCodes.QACA_EMP_CONTRIB,
    								Type.error));
    					}
    				}
    			}
    		}
    		
    		logger.info("NoticePlanDataController validateQACAAutomaticContribution ends...");
    		return errors;
    	}
    	/**
    	 * method to validate the AutomaticDistribution tab field data
    	 * @param form
    	 * @param noticePlanDataVO TODO
    	 * @return
    	 */
    	public static List<GenericException> validateAutomaticContribution(TabPlanDataForm form,NoticePlanCommonVO noticePlanCommonVO,NoticePlanDataVO noticePlanDataVO, boolean eACAFlag, boolean qACAFlag)throws SystemException
    	{   
    		logger.info("NoticePlanDataController validateAutomaticContribution starts...");
    		List<GenericException> errors = new ArrayList<GenericException>();
    		automaticContributionDataCompleted = Constants.YES;
    		if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionProvisionType()))
    		{
    			errors.add(new ValidationError("automaticContributionProvisionType"
    					, ErrorCodes.AUTOMATIC_CONTRIBUTION_PROVISION_TYPE_NOT_SELECTED,
    					Type.error));
    		}else
    		{
    			if ((!TPAPlanDataWebUtility.isNullOrEmpty(form.getNoticeServiceInd()) && form
    					.getNoticeServiceInd().equalsIgnoreCase(CsfConstants.CSF_YES))
    					&& (!TPAPlanDataWebUtility.isNullOrEmpty(form
    							.getNoticeTypeSelected()) && (
										CsfConstants.NOTICE_OPT_AUTO.equalsIgnoreCase(form
    									.getNoticeTypeSelected())||
										CsfConstants.NOTICE_OPT_AUTO_QDIA.equalsIgnoreCase(form
    													.getNoticeTypeSelected()))))
    			{
    				if(form.isContributionFeature1PctMissing() && !TPAPlanDataWebUtility.isNull(form.getAutomaticContributionFeature1())&&"1".equalsIgnoreCase(form.getAutomaticContributionFeature1()) && 
    						TPAPlanDataWebUtility.isNull(form.getContributionFeature1Pct())) 
    				{
    					errors.add(new ValidationError("contributionFeature1Pct"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					automaticContributionDataCompleted = Constants.NO;
    					form.setContributionFeature1PctMissing(true);
    				}
    				else{
    					form.setContributionFeature1PctMissing(false);
    				}
    				if(form.isContributionFeature2DateIdMissing() && !TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature2())&&"2".equalsIgnoreCase(form.getAutomaticContributionFeature2()) &&
    						TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionFeature2Date()))
    				{
    					errors.add(new ValidationError("contributionFeature2Date"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					form.setContributionFeature2DateIdMissing(true);
    					automaticContributionDataCompleted = Constants.NO;
    				}
    				else{
    					form.setContributionFeature2DateIdMissing(false);
    				}
    				if(form.isContributionFeature3SummaryTextMissing() && !TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature3())&&form.getAutomaticContributionFeature3().equalsIgnoreCase("3") && 
    						TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionFeature3SummaryText()))
    				{
    					errors.add(new ValidationError("contributionFeature3SummaryText"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					form.setContributionFeature3SummaryTextMissing(true);
    					automaticContributionDataCompleted = Constants.NO;
    				}
    				else{
    					form.setContributionFeature3SummaryTextMissing(false);
    				}
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed())&&noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.YES))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAcaAnnualIncreaseType()))
    					{
    						errors.add(new ValidationError("aciApplyDate"
    								, ErrorCodes.ANNUAL_INCREASE_UNSELECTED,
    								Type.error));
    					} 
    				}

    			


    			}else
    			{
    				if(form.isContributionFeature1PctMissing() && !TPAPlanDataWebUtility.isNull(form.getAutomaticContributionFeature1())&&form.getAutomaticContributionFeature1().equalsIgnoreCase("1") && 
    						TPAPlanDataWebUtility.isNull(form.getContributionFeature1Pct())) 
    				{
    					errors.add(new ValidationError("contributionFeature1Pct"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					form.setContributionFeature1PctMissing(true);
    					automaticContributionDataCompleted = Constants.NO;
    				}
    				else{
    					form.setContributionFeature1PctMissing(false);
    				}
    				if(form.isContributionFeature2DateIdMissing() && !TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature2())&&form.getAutomaticContributionFeature2().equalsIgnoreCase("2") &&
    						TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionFeature2Date()))
    				{
    					errors.add(new ValidationError("contributionFeature2Date"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					form.setContributionFeature2DateIdMissing(true);
    					automaticContributionDataCompleted = Constants.NO;
    				}
    				else{
    					form.setContributionFeature2DateIdMissing(false);
    				}
    				if(form.isContributionFeature3SummaryTextMissing() && !TPAPlanDataWebUtility.isNullOrEmpty(form.getAutomaticContributionFeature3())&&form.getAutomaticContributionFeature3().equalsIgnoreCase("3") && 
    						TPAPlanDataWebUtility.isNullOrEmpty(form.getContributionFeature3SummaryText()))
    				{
    					errors.add(new ValidationError("contributionFeature3SummaryText"
    							, ErrorCodes.MISSING_VALUES,
    							Type.error));
    					form.setContributionFeature3SummaryTextMissing(true);
    					automaticContributionDataCompleted = Constants.NO;
    				}
    				else{
    					form.setContributionFeature3SummaryTextMissing(false);
    				}
    				if(!TPAPlanDataWebUtility.isNullOrEmpty(noticePlanCommonVO.getAciAllowed())&&noticePlanCommonVO.getAciAllowed().equalsIgnoreCase(Constants.YES))
    				{
    					if(TPAPlanDataWebUtility.isNullOrEmpty(form.getAcaAnnualIncreaseType()))
    					{
    						errors.add(new ValidationError("aciApplyDate"
    								, ErrorCodes.ANNUAL_INCREASE_UNSELECTED,
    								Type.error));
    					} 
    				}




    			}


    			//EACA validation
    			if(eACAFlag) {
    				errors=validateEACAAutomaticContribution(form, errors, noticePlanCommonVO, noticePlanDataVO);
    			}
    			//QACA validation
    			if(qACAFlag) {
    				errors=validateQACAAutomaticContribution(form, errors, noticePlanCommonVO, noticePlanDataVO);
    			}
    		}   
    		form.setAutomaticContributionDataCompleteInd(automaticContributionDataCompleted);
    		logger.info("NoticePlanDataController validateAutomaticContribution ends...");
    		return errors;
    	}


    	public void clearTabValuesFromForm(TabPlanDataForm form, String selectedTab){
    		if(Constants.ALL_TABS.equalsIgnoreCase(selectedTab))
    		{
    			clearSHValuesFromForm(form);
    			clearContributionsValuesFromForm(form);
    			clearAutomaticContributionValuesFromForm(form);
    			clearInvInfoValuesFromForm(form);
    		}else if(Constants.SAFE_HARBOR.equalsIgnoreCase(selectedTab)){
    			clearSHValuesFromForm(form);
    		}
    		else if(Constants.CONTRIBUTION_AND_DISTRIBUTION.equalsIgnoreCase(selectedTab)){
    			clearContributionsValuesFromForm(form);
    		}        
    		else if(Constants.AUTOMATIC_CONTRIBUTION.equalsIgnoreCase(selectedTab)){
    			clearAutomaticContributionValuesFromForm(form);
    		}
    		else if(Constants.INVESTMENT_INFO.equalsIgnoreCase(selectedTab)){
    			clearInvInfoValuesFromForm(form);
    		}  

    	}


    	/**
    	 * To set the Contribution and Distribution tab data values from database to form
    	 * @param noticePlanDataVO, noticePlanCommonVO, form
    	 * @return TabPlanDataForm
    	 * 
    	 */
    	public TabPlanDataForm setContriAndDistriValuesToForm(TabPlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    		logger.info("NoticePlanDataController setContriAndDistriValuesToForm starts...");
    		// Contributions and distributions Tab value mapping to form from ContributionsAndDistributionsVO
    		ContributionsAndDistributionsVO contributionsAndDistributionsVO=noticePlanDataVO.getContributionsAndDistributionsVO();         
    		if(!TPAPlanDataWebUtility.isNull(contributionsAndDistributionsVO))
    		{
    			
    		 	if(!TPAPlanDataWebUtility.isNull(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships()))
    			{
    				form.setContirbutionRestirictionOnHardships(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships());
    			}
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getSpdEmployeeContributionRef()))
    			{
    				form.setSpdEmployeeContributionRef(contributionsAndDistributionsVO.getSpdEmployeeContributionRef().trim());
    			}
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getPlanAllowRothDeferrals()))
    			{
    				form.setPlanAllowRothDeferrals(contributionsAndDistributionsVO.getPlanAllowRothDeferrals().trim());
    			}
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getPlanAllowsInServiceWithdrawals()))
    			{
    				form.setPlanAllowsInServiceWithdrawals(contributionsAndDistributionsVO.getPlanAllowsInServiceWithdrawals());
    			}
    			if((TPAPlanDataWebUtility.isNull(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships())))
    			{
    				form.setContirbutionRestirictionOnHardships(6);
    			}else
    			{
    				form.setContirbutionRestirictionOnHardships(contributionsAndDistributionsVO.getContirbutionRestirictionOnHardships());
    			}

    			if(!TPAPlanDataWebUtility.isNullOrEmpty(contributionsAndDistributionsVO.getDataCompleteInd()))
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
    	public TabPlanDataForm setSafeHarborValuesToForm(TabPlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    		logger.info("NoticePlanDataController setSafeHarborValuesToForm starts...");
    		//setting SafeHarbor VO values to form
    		SafeHarborVO safeHarborVO = noticePlanDataVO.getSafeHarborVO();
    		if(noticePlanDataVO.getSafeHarborVO() != null){
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasSafeHarborMatchOrNonElective())){
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
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchAppliesToContrib())){
    				form.setMatchAppliesToContrib(safeHarborVO.getMatchAppliesToContrib().trim());
    			}

    			//MatchContributionToOtherplan
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchContributionToAnotherPlan())){
    				form.setMatchContributionToAnotherPlan(safeHarborVO.getMatchContributionToAnotherPlan().trim());
    			}
    			else{
    				form.setMatchContributionToAnotherPlan(Constants.NO);
    			}

    			//MatchContributionOtherPlanName
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getMatchContributionOtherPlanName())){
    				form.setMatchContributionOtherPlanName(safeHarborVO.getMatchContributionOtherPlanName().trim());
    			}

    			//SHappliesToRoth
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getSafeHarborAppliesToRoth())){
    				form.setSafeHarborAppliesToRoth(safeHarborVO.getSafeHarborAppliesToRoth().trim());
    			}

    			//SHappliesToCatchUpContributions
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getsHAppliesToCatchUpContributions())){
    				form.setsHAppliesToCatchUpContributions(safeHarborVO.getsHAppliesToCatchUpContributions().trim());
    			}

    			//setting NE values
    			//SHNEContribution option
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOption())){
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
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveAppliesToContrib())){
    				form.setNonElectiveAppliesToContrib(safeHarborVO.getNonElectiveAppliesToContrib().trim());
    			}

    			//SHNEContributionToOtherPlan
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOtherPlan())){
    				form.setNonElectiveContribOtherPlan(safeHarborVO.getNonElectiveContribOtherPlan().trim());
    			}
    			else{
    				form.setNonElectiveContribOtherPlan(Constants.NO);
    			}

    			//SHNEContributionOtherPlanName
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getNonElectiveContribOtherPlanName())){
    				form.setSHNonElectivePlanName(safeHarborVO.getNonElectiveContribOtherPlanName().trim());
    			}

    			//SHNEContributionApplicableToPlan
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp())) {
    				if(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp().equals(Constants.YES)) {
    					form.setEnablePlanYearEndDateAndPercentageComp(safeHarborVO.getEnablePlanYearEndDateAndPercentageComp());
    					if(!TPAPlanDataWebUtility.isNull(safeHarborVO.getContributionApplicableToPlanDate())) {
    						Date effectiveDate = safeHarborVO.getContributionApplicableToPlanDate();
    						form.setContributionApplicableToPlanDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
    					}
    					else {
    						form.setContributionApplicableToPlanDate(null);
    					}
    					if(!TPAPlanDataWebUtility.isNull(safeHarborVO.getContributionApplicableToPlanPct())) {
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
    			if(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO)&&!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
    			{
    				Date effectiveDate = noticePlanCommonVO.getAutomaticContributionEffectiveDate();
    				form.setEffectiveDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
    			}
    			
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasSHACA())){
    				form.setPlanHasSHACA(safeHarborVO.getPlanHasSHACA().trim());
    			}else{
    				form.setPlanHasSHACA(Constants.NO);
    			}
    			
    			if(!TPAPlanDataWebUtility.isNull(safeHarborVO.getShContributionFeature1Pct())){
    				form.setShContributionFeature1Pct(safeHarborVO.getShContributionFeature1Pct());
    				form.setShContributionFeature1PctMissing(true);
    			}else{
    				form.setShContributionFeature1PctMissing(false);
    			}
    			
    			if(!TPAPlanDataWebUtility.isNull(safeHarborVO.getShContributionFeature2Date())) {
					Date effectiveDate = safeHarborVO.getShContributionFeature2Date();
					form.setShContributionFeature2Date(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
					form.setShContributionFeature2DateIdMissing(true);
				}
				else {
					form.setShContributionFeature2Date(null);
					form.setShContributionFeature2DateIdMissing(false);
				}
    			
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getShContributionFeature3SummaryText())){
    				form.setShContributionFeature3SummaryText(safeHarborVO.getShContributionFeature3SummaryText().trim());
    				form.setShContributionFeature3SummaryTextMissing(true);
    			}else{
    				form.setShContributionFeature3SummaryTextMissing(false);
    			}
    			
    			if(!TPAPlanDataWebUtility.isNull(safeHarborVO.getShACAAnnualIncreaseType()))
    			{
    				form.setShACAAnnualIncreaseType(safeHarborVO.getShACAAnnualIncreaseType().trim());
    			}
    			
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getSHAutoContributionWD())){
    				form.setSHAutoContributionWD(safeHarborVO.getSHAutoContributionWD().trim());
    			}
    			
    			if(!TPAPlanDataWebUtility.isZero(safeHarborVO.getSHAutomaticContributionDays()))
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
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getPlanHasAdditionalEmpContribution())){
    				form.setPlanHasAdditionalEC(safeHarborVO.getPlanHasAdditionalEmpContribution().trim());
    			}

    			//SH applies to AdditionalEmployeeContribution
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getAdditionalEmpContribSPDDescription())){
    				form.setSummaryPlanDesc(safeHarborVO.getAdditionalEmpContribSPDDescription().trim());
    			}
    			
       			if(!TPAPlanDataWebUtility.isNullOrEmpty(safeHarborVO.getDataCompleteInd())){
    				form.setSafeHarborDataCompleteInd(safeHarborVO.getDataCompleteInd());
    			}

    			if(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getVestingSchedules())){
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
    			form.setPlanHasSHACA(Constants.NO);
    			
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
    	public TabPlanDataForm setEACAValuesToForm(TabPlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException{
    		logger.info("NoticePlanDataController setEACAValuesToForm starts...");
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals())){
    			if(Constants.YES.equalsIgnoreCase(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals().trim())){
    				form.setAutoContributionWD(Constants.YES);
    			}
    			else if(Constants.NO.equalsIgnoreCase(automaticContributionVO.geteACAPlanHasAutoContributionWithdrawals().trim())){
    				form.setAutoContributionWD(Constants.NO);
    			}
    			
    		}
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.geteACAContributionDays()))
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
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACAPlanHasEmpoyerContribution()))
    		{
    			form.setEmployerContributions(automaticContributionVO.geteACAPlanHasEmpoyerContribution().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.geteACASPDEmployeeContRef()))
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
    	public TabPlanDataForm setQACAValuesToForm(TabPlanDataForm form,AutomaticContributionVO automaticContributionVO)throws SystemException{
    		logger.info("NoticePlanDataController setQACAValuesToForm starts...");
    		// QACA SH Match contribution values
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct1()))
    		{
    			form.setqACAMatchContributionContribPct1(automaticContributionVO.getqACASHMACMatchContributionContribPct1());
    		}else
    		{
    			form.setqACAMatchContributionContribPct1(new BigDecimal(100));  
    		}
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct2()))
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
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct1()))
    		{
    			form.setqACAMatchContributionMatchPct1(automaticContributionVO.getqACASHMACMatchContributionMatchPct1());

    			if((!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct2())&&(automaticContributionVO.getqACASHMACMatchContributionMatchPct2().compareTo(BigDecimal.ZERO) == 1)) ||((!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionContribPct2()))&&automaticContributionVO.getqACASHMACMatchContributionContribPct2().compareTo(BigDecimal.ZERO) == 1))
    			{                
    				form.setqACAMatchContributionMatchPct1Value(form.getqACAMatchContributionMatchPct1());
    			}else
    			{
    				if(automaticContributionVO.getqACASHMACMatchContributionMatchPct1().compareTo(new BigDecimal(4))==-1)
    				{
    					form.setqACAMatchContributionMatchPct1Value(form.getqACAMatchContributionMatchPct1());
    				}

    			}

    		}else
    		{
    			form.setqACAMatchContributionMatchPct1(new BigDecimal(1));  
    			form.setqACAMatchContributionMatchPct1Value(form.getqACAMatchContributionMatchPct1());
    		}
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACASHMACMatchContributionMatchPct2()))
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
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchingContribOtherPlan()))
    		{
    			form.setqACAMatchContributionToAnotherPlan(automaticContributionVO.getqACAMatchingContribOtherPlan().trim());
    		}
    		else
    		{
    			form.setqACAMatchContributionToAnotherPlan(Constants.NO);
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchingContribOtherPlanName()))
    		{
    			form.setqACAMatchContributionOtherPlanName(automaticContributionVO.getqACAMatchingContribOtherPlanName().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAIsSHMatchToRoth()))
    		{
    			form.setqACASafeHarborAppliesToRoth(automaticContributionVO.getqACAIsSHMatchToRoth().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAIsSHMatchToCatchUp()))
    		{
    			form.setqACASHAppliesToCatchUpContributions(automaticContributionVO.getqACAIsSHMatchToCatchUp().trim());
    		}

    		// set QACA Non elective contribution fields 
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACANonElectiveContributionPct()))
    		{
    			form.setqACANonElectiveContributionPct(automaticContributionVO.getqACANonElectiveContributionPct());
    		}else
    		{
    			form.setqACANonElectiveContributionPct(new BigDecimal(3));
    		}
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACANonElectiveContributionPct()))
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
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getQACAAutoContribWithdrawaldays()))
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
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContributionOptions()))
    		{
    			form.setqACANonElectiveAppliesToContrib(automaticContributionVO.getqACANonElectiveContributionOptions());
    		}

    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContribOtherPlan()))
    		{
    			form.setqACANonElectiveContribOtherPlan(automaticContributionVO.getqACANonElectiveContribOtherPlan().trim());
    		}else
    		{
    			form.setqACANonElectiveContribOtherPlan(Constants.NO);  
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACANonElectiveContribOtherPlanName()))
    		{
    			form.setqACASHNonElectivePlanName(automaticContributionVO.getqACANonElectiveContribOtherPlanName());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAMatchOrNonElectiveCode()))
    		{
    			form.setqACAPlanHasSafeHarborMatchOrNonElective(automaticContributionVO.getqACAMatchOrNonElectiveCode());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAAutomaticContribWithdrawal()))
    		{
    			form.setqACAPlanHasAdditionalEC(automaticContributionVO.getqACAAutomaticContribWithdrawal().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAPlanHasEmpoyerContribution()))
    		{
    			form.setqACAPlanHasAdditionalECon(automaticContributionVO.getqACAPlanHasEmpoyerContribution().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getQacaFullyVested()))
    		{
    			form.setqACASHMatchVesting(automaticContributionVO.getQacaFullyVested().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getqACAVestingLessThan1YearPct()))
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
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getaACAVesting1To2YearPct()))
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
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACASPDEmpContribReference()))
    		{
    			form.setqACASummaryPlanDesc(automaticContributionVO.getqACASPDEmpContribReference().trim());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getqACAArrangementOptions()))
    		{
    			form.setqACAArrangementOptions(automaticContributionVO.getqACAArrangementOptions());
    		}
    		if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getDataCompleteInd()))
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
    	public TabPlanDataForm setAutoContributionValuesToForm(TabPlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    		logger.info("NoticePlanDataController setAutoContributionValuesToForm starts...");
    		// Automatic Contribution Tab value mapping to form from AutomaticContributionVO
    		AutomaticContributionVO automaticContributionVO=noticePlanDataVO.getAutomaticEnrollmentVO();
    		if(!TPAPlanDataWebUtility.isNull(automaticContributionVO))
    		{
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getAutomaticEnrollmentType()))
    			{
    				form.setAutomaticContributionProvisionType(automaticContributionVO.getAutomaticEnrollmentType().trim());
    				form.setAutomaticContributionProvisionTypeHidden(automaticContributionVO.getAutomaticEnrollmentType().trim());
    			}
    			if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getaCAContibLessPercentage()))
    			{
    				form.setContributionFeature1Pct(automaticContributionVO.getaCAContibLessPercentage());
    				//form.setAutomaticContributionFeature1("1");
    			}
    			if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getaCAHiredAfterDate()))
    			{
    				form.setContributionFeature2Date(new SimpleDateFormat(DATE_PATTERN).format(automaticContributionVO.getaCAHiredAfterDate()));
    				//form.setAutomaticContributionFeature2("2");
    			}
    			if(!TPAPlanDataWebUtility.isNull(automaticContributionVO.getAcaAnnualIncreaseType()))
    			{
    				form.setAcaAnnualIncreaseType(automaticContributionVO.getAcaAnnualIncreaseType().trim());
    			}
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(automaticContributionVO.getaCAAppliesToCustom()))
    			{
    				form.setContributionFeature3SummaryText(automaticContributionVO.getaCAAppliesToCustom().trim());
    				//form.setAutomaticContributionFeature3("3");
    			}
    			// set values for eaca fields
    			form=setEACAValuesToForm(form,automaticContributionVO);
    			// set values for qaca fields
    			if(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO)&&!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
    			{
    				Date effectiveDate = noticePlanCommonVO.getAutomaticContributionEffectiveDate();
    				form.setEffectiveDate(null!=effectiveDate?new SimpleDateFormat(DATE_PATTERN).format(effectiveDate):"");
    			}
    			form=setQACAValuesToForm(form,automaticContributionVO);
    			// set values for vesting
    			if(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getVestingSchedules())){
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
    			form.setAutoContributionWD(StringUtils.EMPTY);
    			if(!TPAPlanDataWebUtility.isNull(noticePlanCommonVO)&&!TPAPlanDataWebUtility.isNull(noticePlanCommonVO.getAutomaticContributionEffectiveDate()))
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
    	public TabPlanDataForm setInvInfoValuesToForm(TabPlanDataForm form, NoticePlanDataVO noticePlanDataVO, NoticePlanCommonVO noticePlanCommonVO)throws SystemException{
    		logger.info("NoticePlanDataController setInvInfoValuesToForm starts...");
    		//Investment Information  
    		InvestmentInformationVO investmentInformationVO = noticePlanDataVO.getInvestmentInformationVO();
    		if(investmentInformationVO!=null){
    			//Is DIO a QDIA
    			if(!TPAPlanDataWebUtility.isNullOrEmpty(investmentInformationVO.getdIOisQDIA())){
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

    			if(!TPAPlanDataWebUtility.isNullOrEmpty(investmentInformationVO.getDataCompleteInd())){
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

}
   
