package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.CensusErrorCodes;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.EligibilityValidationErrors;
import com.manulife.pension.ps.web.census.util.EmployeeEligibilitySummaryValidationRules;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants;
import com.manulife.pension.service.eligibility.valueobject.EligibilityRequestVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

 /**
  * This action handles the creation of the EmployeeEnrollmentSummaryReport page. It will
  * also create the employee enrollment summary download.
  *  
  * @author patuadr
  */

@Controller
@RequestMapping( value ="/census")
@SessionAttributes({"employeeEnrollmentSummaryReportForm"})

 public final class EmployeeEnrollmentSummaryReportController extends EmployeeEnrollmentSummaryBaseController {
	
	@ModelAttribute("employeeEnrollmentSummaryReportForm") 
	public EmployeeEnrollmentSummaryReportForm populateForm()
	{
		return new EmployeeEnrollmentSummaryReportForm();
		
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("sort","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("filter","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("page","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("print","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("save","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("reset","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("cancel","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("calculate","/census/employeeEnrollmentSummaryReport.jsp");
        forwards.put("error","/WEB-INF/census/employeeEnrollmentSummaryReport.jsp");
	} 
	
    protected static EmployeeServiceDelegate serviceFacade = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
   private static final FastDateFormat DATE_FORMAT = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
    private static final FastDateFormat DATE_FORMAT_DB = ContractDateHelper.getDateFormatter("yyyy-MM-dd");
    private static final String CALCULATE_TASK = "calculate";
    
    private static final String className = EmployeeEnrollmentSummaryReportController.class.getName();
    
    // Keeps a map for each execution thread with employees with validation errors
    protected static ThreadLocal<HashMap<String, EmployeeSummaryDetails>> employeesWithValidationErrors = new ThreadLocal<HashMap<String, EmployeeSummaryDetails>>() {
        protected synchronized HashMap<String, EmployeeSummaryDetails> initialValue() {
            return new HashMap<String, EmployeeSummaryDetails>();
        }
    };
    
 	/**
	 * Constructor for EmployeeEnrollmentSummaryReportAction
	 */
	public EmployeeEnrollmentSummaryReportController() {
		super(EmployeeEnrollmentSummaryReportController.class);
	}
     	
	@RequestMapping(value="/employeeEnrollmentSummary", method =  {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		} 
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}	
	
	@RequestMapping(value="/employeeEnrollmentSummary",params = {"task=filter"}, method =  {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		} 
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}	
	
	
	@RequestMapping(value="/employeeEnrollmentSummary",params = {"task=page"}, method =  {RequestMethod.GET,RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		} 
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	@RequestMapping(value="/employeeEnrollmentSummary",params = {"task=sort"}, method =  {RequestMethod.GET,RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		} 
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
	
	
	
	
	@RequestMapping(value="/employeeEnrollmentSummary", params = {"task=download"},method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("defualt");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
	@RequestMapping(value="/employeeEnrollmentSummary",params= {"task=save"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    	   	public String doSave(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	   	throws IOException,ServletException, SystemException {
		String forward;
    		 doValidate(form, request);
    		 if(bindingResult.hasErrors()){
    			 request.removeAttribute(CommonConstants.ERROR_KEY);
    				populateReportForm( form, request);
    				
    	            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
    				request.setAttribute(Constants.REPORT_BEAN, reportData);
    			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    			 if(errDirect!=null){
    				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
    			 }
    		 }
    	
    	
        if (logger.isDebugEnabled()) {
            logger.info("entry <- doSave");
        }
        
       
                
        request.removeAttribute("validationErrors");
        
        List<ValidationError> errors = validateAndUpdateList(form, request);
        
        if (logger.isDebugEnabled()) {
            logger.info("entry <- doSave");
        }
        
        if (errors.size() > 0) {
            setErrors(request, errors);
            //return mapping.getInputForward();
            forward= doCommon( form, request,response);
        } else {     
            form.clear( request);
            forward= doCommon( form, request,response);
        }    
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward);

    }
    	 public String doCommon(
    				BaseReportForm reportForm, HttpServletRequest request,
    				HttpServletResponse response) throws SystemException 
    		{
    			if (logger.isDebugEnabled()) {
    				logger.debug("entry -> doCommon");
    			}
    	 				
    			EmployeeEnrollmentSummaryReportForm form = (EmployeeEnrollmentSummaryReportForm)reportForm;
    	        UserProfile userProfile = getUserProfile(request);
        
        if (DOWNLOAD_TASK.equals(getTask(request))) {
            
            FunctionalLogger.INSTANCE.log("Download Eligibility Report", userProfile, getClass(), getMethodName( form, request));
            
        } else {
            
            FunctionalLogger.INSTANCE.log("Eligibility Tab", userProfile, getClass(), getMethodName( form, request));
            
        }
        
        int contractId = userProfile.getCurrentContract().getContractNumber();
        
        // Set auto enrollment enabled, has to be done before the call to the stored procedure is done
        try {
            form.setAutoEnrollmentEnabled(CensusUtils.isAutoEnrollmentEnabled(
                userProfile.getCurrentContract().getContractNumber()));
        } catch (Exception e) {
            form.setAutoEnrollmentEnabled(false);
            logger.error("Contract Service delegate exception", e);
        }
        
        // set permission flag for deferral tab
        boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
        form.setAllowedToAccessDeferralTab(allowedToAccessDeferrals);
        
        try {
        	form.setEZstartOn(DeferralUtils.isEZstartOn(userProfile.getCurrentContract().getContractNumber()));
        	form.setEligibiltyCalcOn(DeferralUtils.isEligibilityCalcOn(userProfile.getCurrentContract().getContractNumber()));
        } catch(ApplicationException ae) {
        	throw new SystemException(ae, "Problem get CSF value for ezstart");
        }
        
        
        Map csfMap = null;
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csfMap = service.getContractServiceFeatures(userProfile.getCurrentContract().getContractNumber());
        } catch (ApplicationException ae) {
            throw new SystemException(ae, className, "loadContractServiceFeatureData", ae
                    .getDisplayMessage());
        }

       

     // Set eligibility calculation request values
	List<LabelValueBean> moneyTypes = new ArrayList<LabelValueBean>();
	ContractServiceFeature eligibilityCalculationCSF = (ContractServiceFeature) csfMap
			.get(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
	
	EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
			.getInstance(PS_APPLICATION_ID);
	
	ContractServiceFeature aeCSF = (ContractServiceFeature) csfMap
	.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
	
	List<MoneyTypeVO> contractMoneyTypesVoList = service
	.getContractMoneyTypes(contractId, true);
	
	if (eligibilityCalculationCSF != null &&  !"N".equalsIgnoreCase(eligibilityCalculationCSF.getValue()) ) {
				
	   
	    // get the money types for the contract
		
	    MoneyTypeVO moneyTypeVO = null;
	    
	    // get money types for the contract with EC on
	    List<String> moneyTypesList = eligibilityServiceDelegate
				.getMoneyTypesWithEligibilityService(contractId);
	    
	    for (String moneyTypeId : moneyTypesList) {
		
		moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);

		// get MoneyTypeVO for money type id
		moneyTypeVO = getMoneyTypeDetailsVO(moneyTypeId,
				contractMoneyTypesVoList);

		// this is not contract money type
		// hence move to next money type
		if (moneyTypeVO == null) {
			continue;
		}
		
		if("EEDEF".equalsIgnoreCase(moneyTypeVO.getId())){
		    form.setEedefShortName(moneyTypeVO.getContractShortName());
		}
		moneyTypes.add(new LabelValueBean(moneyTypeVO.getContractShortName(),moneyTypeVO.getId()));
	    }
	    
	    
	
	}else  if(aeCSF != null && "Y".equalsIgnoreCase(aeCSF.getValue())){
	    
	    for(MoneyTypeVO vo : contractMoneyTypesVoList){
		if("EEDEF".equalsIgnoreCase(vo.getId())){
		    moneyTypes.add(new LabelValueBean(vo.getContractShortName(),vo.getId()));
		    form.setEedefShortName(vo.getContractShortName());
		}
	    }
	    
			
	}
	
	 // sort the money types based on alphabetical order of money source
	// short names
	
	 Collections.sort(moneyTypes, new Comparator<LabelValueBean>() {
	     public int compare(LabelValueBean vo1, LabelValueBean vo2) {
		 return vo1.getLabel().compareTo(
			 vo2.getLabel());
	     }
	 });
	form.setMoneyTypes(moneyTypes);
        
        
        String forward = super.doCommon( reportForm, request,response);
        
		//get the Report object		
        EmployeeEnrollmentSummaryReportData report = (EmployeeEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);		
		//get the report details		
		List<EmployeeSummaryDetails> details = (List<EmployeeSummaryDetails>)report.getDetails();
               
		// As long as there are details, determine if columns are hidden (payroll and division)
		if (details.size() > 0) { 
			
			//Getting the warning messages from content and setting it in "detail" object for displaying in JSP
			for (EmployeeSummaryDetails detail : details) {
			
				if (detail.getWarnings().hasAnyWarnings() || detail.hasAlerts()) {
					if (detail.getWarnings().hasAllWarnings()
							&& getWarningMessage(ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING));
					} else if (detail.getWarnings()
							.hasEligibilityIndicatorWarning()
							&& getWarningMessage(ContentConstants.BLANK_ELIGIBILITY_WARNING) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.BLANK_ELIGIBILITY_WARNING));
					} else if (detail.getWarnings().hasEligibilityDateWarning()
							&& getWarningMessage(ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING));
					} else if (detail.getWarnings()
							.hasMissingBirthDateWarning()
							&& getWarningMessage(ContentConstants.WARNING_MISSING_BIRTHDATE_DM) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.WARNING_MISSING_BIRTHDATE_DM));
					} else if (detail.getWarnings()
							.hasMissingEmployeeAddressWarning()
							&& getWarningMessage(ContentConstants.WARNING_MISSING_EMPLOYEE_ADDRESS) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.WARNING_MISSING_EMPLOYEE_ADDRESS));
					} else if (detail.getWarnings()
							.hasMissingBirthDateAndEmployeeAddressWarning()
							&& getWarningMessage(ContentConstants.WARNING_MISSING_BIRTHDATE_AND_EMPLOYEE_ADDRESS) != null) {
						detail.getWarnings()
								.setWarningContent(
										getWarningMessage(ContentConstants.WARNING_MISSING_BIRTHDATE_AND_EMPLOYEE_ADDRESS));
					}
				}
			}
			
			ContractProfileVO contractProfile= getContractProfile(userProfile.getCurrentContract().getContractNumber(),request);
			
			boolean isAEAndDMEnabled = false;
			
			try{
				isAEAndDMEnabled = userProfile.getCurrentContract().isDMContract()&& (DeferralUtils.isEZstartOn(userProfile.getCurrentContract().getContractNumber()));
			if (isAEAndDMEnabled)
				form.setShowDM(true);
	        } catch(ApplicationException ae) {
	        	throw new SystemException(ae, "Problem get CSF value for ezstart");
	        }		
			try{
		        if((userProfile.getCurrentContract().isDMContract()) ||(DeferralUtils.isEZstartOn(userProfile.getCurrentContract().getContractNumber()))){
		        	form.setShowSaveAndCancelButtons(true);
		        }
			}catch(ApplicationException ae) {
	        	throw new SystemException(ae, "Problem get CSF value for ezstart");
	        }	
	        
            if (userProfile.getCurrentContract().hasSpecialSortCategoryInd()) { 
				form.setHasDivisionFeature(true);
			} else {
				form.setHasDivisionFeature(false);
			}	
            
            if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE.equalsIgnoreCase(
                    userProfile.getCurrentContract().getParticipantSortOptionCode())) {            
				form.setHasPayrollNumberFeature(true);
			} else {
				form.setHasPayrollNumberFeature(false);
			}
		}	
		
		/*  Added For EC project. -- START --  */
		
		/*if(form.getMoneyTypeSelected()!= null && !"All".equalsIgnoreCase(form.getMoneyTypeSelected())){
		    
		    List<LabelValueBean> moneyType = new ArrayList<LabelValueBean>();
		    	for(int mt = 0; mt < moneyTypes.size(); mt++){
		    	    
        		    if(moneyTypes.get(mt).getValue().equalsIgnoreCase(form.getMoneyTypeSelected())){
        		    
        			moneyType.add(new LabelValueBean(moneyTypes.get(mt).getLabel(),moneyTypes.get(mt).getValue()));
        			
        		    }
		    	}
			
			report.setMoneyTypes(moneyType);
		}else{*/
			report.setMoneyTypes(moneyTypes);
		//}
		
		List<EmployeeSummaryDetails> empDetails = (List<EmployeeSummaryDetails>)report.getDetails();
		Map<String,Date> planEntryDates = (Map<String,Date>)report.getPlanEntryDatesMap();
		Map<String,String> eligibilityReportData = (Map<String,String>)report.getEligibiltyReportMap();
		for(int emp = 0; emp < empDetails.size(); emp++){
			List<String> list = new ArrayList<String>();
			List<String> eligibilityDataList = new ArrayList<String>();
			String eligibilityData ="";
			
			for(int mt = 0; mt < report.getMoneyTypes().size(); mt++){
			    
			    if(form.getMoneyTypeSelected()== null || "All".equals(form.getMoneyTypeSelected())){
				// For Plan Entry Dates
				if(planEntryDates.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue())!= null){
					String planEntryDate = planEntryDates.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue()).toString();
						try{
							planEntryDate = DATE_FORMAT.format(DATE_FORMAT_DB.parse(planEntryDate));
						}catch(ParseException exception){
							
						}
					list.add(planEntryDate);
				}else{
					list.add("");
				}
				// For Eligibility report
				if(eligibilityReportData.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue())!= null){
					eligibilityData = eligibilityReportData.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue()).toString();
					eligibilityDataList.add(eligibilityData);
						
				}else{
				    	
					eligibilityData = ",,,,,,,";
					if(form.isEZstartOn() && !form.isEligibiltyCalcOn()){
					    eligibilityData = ",";
					}
					eligibilityDataList.add(eligibilityData);
				}
			    }else{
				
				
				// For Plan Entry Dates
				//if(report.getMoneyTypes().get(mt).getValue().equals(form.getMoneyTypeSelected())){
				    if(planEntryDates.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue())!= null){
					String planEntryDate = planEntryDates.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue()).toString();
						try{
							planEntryDate = DATE_FORMAT.format(DATE_FORMAT_DB.parse(planEntryDate));
						}catch(ParseException exception){
							
						}
					list.add(planEntryDate);
				    }else{
					list.add("");
				    }
				/*}else{
					list.add("");
				}*/
				// For Eligibility report
				//if(report.getMoneyTypes().get(mt).getValue().equals(form.getMoneyTypeSelected())){
				    if(eligibilityReportData.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue())!= null){
					eligibilityData = eligibilityReportData.get(empDetails.get(emp).getProfileId()+report.getMoneyTypes().get(mt).getValue()).toString();
					eligibilityDataList.add(eligibilityData);
				  //  }
						
				}else{
					eligibilityData = ",,,,,,,";
					if(form.isEZstartOn() && !form.isEligibiltyCalcOn()){
					    eligibilityData = ",";
					}
					eligibilityDataList.add(eligibilityData);
				}
				
				
				
			    }
				
			}
			empDetails.get(emp).setPlanEntryDates(list);
			empDetails.get(emp).setEligibilityData(eligibilityDataList);
		}
		
		
		EligibilityServiceDelegate eligibilityDelegate=EligibilityServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
    	Collection<EligibilityRequestVO> eligibilityRequestVoCollection=eligibilityDelegate.getPendingPlanLevelOrEmployeeLevelRequests(userProfile.getCurrentContract().getContractNumber(),0);
    	
    	if((form.isEZstartOn() || form.isEligibiltyCalcOn())&& eligibilityRequestVoCollection != null && eligibilityRequestVoCollection.size() > 0){
    		form.setPendingEligibilityCalculationRequest(true);
       	}else{
       	    	form.setPendingEligibilityCalculationRequest(false);
       	}
    	
		
    	// To show the Calculate button in the page.
    	if((userProfile.isInternalUser())&&(form.isEZstartOn()||form.isEligibiltyCalcOn()) ){
    		form.setShowCalculateButton(true);
    	}
    	
		
		/*  Added for EC project. -- END --    */
		
        
        // Don't want to clone or apply business rules if the action is download report or show
        // print friendly
        if (!PRINT_TASK.equals(getTask(request)) && !DOWNLOAD_TASK.equals(getTask(request))) {
            applyBusinessRulesForValidationErrors(details);
        }
        
        addAttributes(request, form, report);
        
        // Sort the report list only if there are errors 
        // and put the form items in the same order so they match
        // with the IDs for track changes in the JSP
        if(hasErrors(request)) {
            sortReportItemsByErrorsAndWarnings(details, form, request);
        }
        
        // Don't want to clone or apply business rules if the action is download report or show
        // print friendly
        if (!PRINT_TASK.equals(getTask(request)) && !DOWNLOAD_TASK.equals(getTask(request))) {           
            // Add the latest list to the form    
            if(!hasErrors(request)) {
                form.setTheItemList(details);
                form.storeClonedForm();
            }
        }
        
        if(CALCULATE_TASK.equals(getTask(request))){
            form.setFromCalculateButton(false);
        }
        
        
		if (form.getIsSearch() || form.getIsInitialSearch()){
			form.setIsInitialSearch(false);
		}
        
		// The map is cleared every time this execution path is initiated
        employeesWithValidationErrors.get().clear();
        
		if (logger.isDebugEnabled()) { 
			logger.debug("exit <- doCommon");
		}
		return forward;
	}
 	
	 /**
	 * This method is called when reset button is clicked
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
    
  @RequestMapping(value="/employeeEnrollmentSummary", params= {"task=reset"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
  public String doReset(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 throws IOException,ServletException, SystemException { 
	  doValidate(form, request);
	  if(bindingResult.hasErrors()){
			 request.removeAttribute(CommonConstants.ERROR_KEY);
				populateReportForm( form, request);
	            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			 }
		 }
	 
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}
		
		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
		//Reset the session object for remebering filter criteria
		if(filterCriteriaVo != null){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
		
		//Reset the form bean
		super.resetForm( form, request);

		String forward = doCommon( form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		//return forward;
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
  
  
	
	/**
	 * This method is called when cancel button is clicked
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
  
  @RequestMapping(value="/employeeEnrollmentSummary", params= {"task=cancel"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
  public String doCancel(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 throws IOException,ServletException, SystemException {
	  doValidate(form, request);
	  if(bindingResult.hasErrors()){
			 request.removeAttribute(CommonConstants.ERROR_KEY);
				populateReportForm( form, request);
	            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			 }
		 }
	
  
	
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}
		
		String forward = doCommon( form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCancel");
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
  @RequestMapping( value="/employeeEnrollmentSummary", params= {"task=calculate"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
  public String doCalculate(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 throws IOException,ServletException, SystemException {
	  doValidate(form, request);  
	  if(bindingResult.hasErrors()){
			 request.removeAttribute(CommonConstants.ERROR_KEY);
				populateReportForm( form, request);
	            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			 }
		 }
	
 
        if (logger.isDebugEnabled()) {
            logger.info("entry <- doCalculate");
        }
        
        
        
        UserProfile userProfile = getUserProfile(request);
        int contractId = userProfile.getCurrentContract().getContractNumber();
        
        String userIdType="";
		if(userProfile.isInternalUser()){
			userIdType=Constants.INTERNAL_USER_ID_TYPE;
        }else {
        	userIdType=Constants.EXTERNAL_USER_ID_TYPE; 
        }
      
	        EligibilityServiceDelegate eligibilityDelegate = EligibilityServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
	        
	       
	        if(form.isFromCalculateButton()){
	        
        	        if(form.isEZstartOn()&& !form.isEligibiltyCalcOn()){
        	        	eligibilityDelegate.calculatePEDForPlan(contractId, "EEDEF", EligibilityRequestConstants.RequestCauseTypeCode.MANUAL_CALC_BUTTON,
        	        		EligibilityRequestConstants.SourceChannelCode.PLAN_SPONSOR, EligibilityRequestConstants.SourceFunctionCode.EEE, Long.toString(userProfile.getPrincipal().getProfileId()), userIdType);
        	        }else{
        	        	eligibilityDelegate.calculateEligibilityForPlan(contractId, null, EligibilityRequestConstants.RequestCauseTypeCode.MANUAL_CALC_BUTTON,
        	        		EligibilityRequestConstants.SourceChannelCode.PLAN_SPONSOR, EligibilityRequestConstants.SourceFunctionCode.EEE, Long.toString(userProfile.getPrincipal().getProfileId()), userIdType);
        	        }
        	       
	        }
	        
        String forward= doCommon( form, request,response);
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
    }
 	
 	/**
 	 * Gets the warning message from content database
 	 * 
 	 * @param contentId
 	 * @return
 	 */
 	private String getWarningMessage(int contentId) {
		String warning = null;
		try {
			Content message = ContentCacheManager.getInstance().getContentById(
					contentId, ContentTypeManager.instance().MESSAGE);

			warning = ContentUtility.getContentAttribute(message, "text");
		} catch (ContentException e1) {
			logger.error("Content Exception", e1);
		}
		return warning;
	}
    
    /**
     * Break the natural sort and keep the errors and warnings at the top of the list
     * 
     * From the report data create a new list with elements with errors at the top,
     * followed by warnings and clean elements at the end
     * 
     * The items from the form should be reordered using the same order as in the previous list, 
     * because on the screen, data is populated from the form elements for the elements that are 
     * edited and in process of saving, but they haven't been saved because of validation errors
     * 
     * The Service Warnings have been removed from the list as requested by the business
     * 
     * @param details - the list retrieved from the database
     * @param form - the action form that contains a list with elements similar with the ones from 
     *              database but the editable fields might contain different data
     * @param request
     */
    private void sortReportItemsByErrorsAndWarnings(List<EmployeeSummaryDetails> details,
            EmployeeEnrollmentSummaryReportForm form,
            HttpServletRequest request) {
        List<EmployeeSummaryDetails> elementsWithErrors = new ArrayList<EmployeeSummaryDetails>();  
        List<EmployeeSummaryDetails> elementsWithWarnings = new ArrayList<EmployeeSummaryDetails>();
//        List<EmployeeSummaryDetails> elementsWithServiceWarnings = new ArrayList<EmployeeSummaryDetails>();
        List<EmployeeSummaryDetails> cleanElements = new ArrayList<EmployeeSummaryDetails>();
//        List<ValidationError> serviceWarningsForEmployeesWithErrors = new ArrayList<ValidationError>(); 
        
        for (EmployeeSummaryDetails element : details) 
        {
   	
            if(element.hasErrors()) {
                elementsWithErrors.add(element);
//                if(element.getWarnings().hasAnyWarnings()) {                    
//                    addWarningsToList(serviceWarningsForEmployeesWithErrors, element);
//                }
            } else if(element.hasWarnings()) {
                elementsWithWarnings.add(element);
//                if(element.getWarnings().hasAnyWarnings()) {
//                    addWarningsToList(serviceWarningsForEmployeesWithErrors, element);
//                }
            } 
//            else if(element.getWarnings().hasAnyWarnings()) {
//                elementsWithServiceWarnings.add(element);
//            } 
            else {
                cleanElements.add(element);
            }
        }
        
        // Add the service warnings to the list of errors in the request
//        addToErrors(request, serviceWarningsForEmployeesWithErrors); 
        
        // Create the reordered list of employees starting with errors, 
        // followed by service warnings and clean elements and sort them 
        // by last name, first name and middle initial in this order
        Collections.sort(elementsWithErrors, new EmployeeSummaryDetails.EmployeeComparator());
        Collections.sort(elementsWithWarnings, new EmployeeSummaryDetails.EmployeeComparator());
//        Collections.sort(elementsWithServiceWarnings, new EmployeeSummaryDetails.EmployeeComparator());
        
        elementsWithErrors.addAll(elementsWithWarnings);
//        elementsWithErrors.addAll(elementsWithServiceWarnings);
        elementsWithErrors.addAll(cleanElements);        
        
        details.clear();
        details.addAll(elementsWithErrors);
        
        List<EmployeeSummaryDetails> formElements = new ArrayList<EmployeeSummaryDetails>();
        
        for (EmployeeSummaryDetails element : details) {
            for (EmployeeSummaryDetails formItem : form.getTheItemList()) {
                if(formItem.getProfileId().equalsIgnoreCase(element.getProfileId())) {
                    formElements.add(formItem);
                    break;
                }
            }
        }
        
        // Replace the old list with the new one
        // with same elements but reordered
        form.setTheItemList(formElements);
    }

    /**
     * Adds all the service warnings to the list
     * 
     * @param serviceWarningsForEmployeesWithErrors
     * @param element
     * @deprecated
     */
    private void addWarningsToList(List<ValidationError> serviceWarningsForEmployeesWithErrors, EmployeeSummaryDetails element) {
        String[] str = null;
        if(element.getWarnings().hasAllWarnings()) {
            str = new String[1];
            str[0] = EmployeeEligibilitySummaryValidationRules.ELIGIBLE_TO_ENROLL;
            serviceWarningsForEmployeesWithErrors.add(
                    new ValidationError(
                            str, 
                            CensusErrorCodes.BlankEligibilityInd,
                            EmployeeEligibilitySummaryValidationRules.getEmployeeName(element),
                            Type.warning));
            str = new String[1];
            str[0] = EmployeeEligibilitySummaryValidationRules.ELIGIBILITY_DATE;
            serviceWarningsForEmployeesWithErrors.add(
                    new ValidationError(
                            str, 
                            CensusErrorCodes.MissingEligibilityDateForNonPpt,
                            EmployeeEligibilitySummaryValidationRules.getEmployeeName(element),
                            Type.warning));            
        } else if(element.getWarnings().hasEligibilityIndicatorWarning()) {
            str = new String[1];
            str[0] = EmployeeEligibilitySummaryValidationRules.ELIGIBLE_TO_ENROLL;
            serviceWarningsForEmployeesWithErrors.add(
                    new ValidationError(
                            str, 
                            CensusErrorCodes.BlankEligibilityInd,
                            EmployeeEligibilitySummaryValidationRules.getEmployeeName(element),
                            Type.warning));
        } else if(element.getWarnings().hasEligibilityDateWarning()) {
            str = new String[1];
            str[0] = EmployeeEligibilitySummaryValidationRules.ELIGIBILITY_DATE;
            serviceWarningsForEmployeesWithErrors.add(
                    new ValidationError(
                            str, 
                            CensusErrorCodes.MissingEligibilityDateForNonPpt,
                            EmployeeEligibilitySummaryValidationRules.getEmployeeName(element),
                            Type.warning));   
        }
    }
    
    /**
     * Utility method that checks validation errors for each element and sets them 
     * in the report list
     * 
     * @param details
     */
    private void applyBusinessRulesForValidationErrors(List<EmployeeSummaryDetails> details) {        
        for (EmployeeSummaryDetails element:details) {
            EmployeeSummaryDetails employee = employeesWithValidationErrors.get().get(element.getProfileId());
            
            if(employee != null) {
                element.setEligibilityIndicatorStatus(employee.getEligibilityIndicatorStatus());
                element.setEligibilityDateStatus(employee.getEligibilityDateStatus());
                element.setOptOutStatus(employee.getOptOutStatus());
                element.setDeferralStatus(employee.getDeferralStatus());
            }
        }
    }    
    
    /**
     * Utility method to populate the request
     * 
     * @param request
     * @throws SystemException 
     * @throws SystemException
     */
    private void addAttributes(
            HttpServletRequest request, 
            EmployeeEnrollmentSummaryReportForm form, 
            EmployeeEnrollmentSummaryReportData report) throws SystemException 
    {
             
        UserProfile userProfile = getUserProfile(request);
        int contractId = getUserProfile(request).getCurrentContract().getContractNumber();
        
        //Show the OptOut Report link only if AE is enabled.
        // The below code is commented because the optout report is moved out of this page.
        /*if(form.isAutoEnrollmentEnabled()) {
            form.setShowOptOutReport(true);
        } else {
            form.setShowOptOutReport(false);
        }*/
        
        // Populate list of segments for the dropdown
        String task = getTask(request);
        // Populate list of segments for the dropdown
        String segmentlist =(String)request.getParameter("segment");
        if(segmentlist!=null && !task.equalsIgnoreCase("reset"))
        {
        form.setSegment(segmentlist);
        }
        form.setSegmentList(CensusLookups.getInstance().getSegments());
        
        // Set permission flag for editing        
        form.setAllowedToEdit(userProfile.isAllowedUpdateCensusData()
                && !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract()
                        .getStatus()));
        
        form.setAllowedToAccessEligibTab(true);
        
        // set permission flag for vesting tab
        form.setAllowedToAccessVestingTab(
                CensusUtils.isVestingEnabled(contractId) && 
                !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                TODO &&
                userProfile.getCurrentContract().isContractAllocated()*/);
        
        // Set view enrollment stats
        form.setAllowedViewEnrollmentStats(!userProfile.isWelcomePageAccessOnly());
                       
        TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
        // Set permission flag for download census report
        form.setAllowedToDownloadCensus(  
                (userProfile.isInternalUser() && 
                        userProfile.isAllowedUpdateCensusData()) || 
                (userProfile.getRole().isExternalUser())); //&& 
                       // !userProfile.getRole().isTPA() && 
                       // userProfile.isAllowedDownloadReport()) ||
                //(userProfile.getRole().isExternalUser() && 
               //         userProfile.getRole().isTPA()  && 
              //          userProfile.isAllowedDownloadReport() && 
               //         firmInfo.getContractPermission().isReportDownload()));
        
        //LS. SSE S024 display the 
        form.setAllowedToDownload(  
                userProfile.isInternalUser()|| 
                //(userProfile.getRole().isPlanSponsor() && 
                 //       userProfile.isAllowedDownloadReport()) ||
                (userProfile.getRole().isExternalUser())); //&& 
                     //   !userProfile.getRole().isTPA() && 
                    //    userProfile.isAllowedDownloadReport()) ||
               // (userProfile.getRole().isExternalUser() && 
                //        userProfile.getRole().isTPA()  && 
                //        userProfile.isAllowedDownloadReport() && 
                //        firmInfo.getContractPermission().isReportDownload())
            // );
        
        if(CensusUtils.isAutoEnrollmentEnabled(contractId)) {
            request.getSession().setAttribute(CensusConstants.PED_YEAR_LIST, CensusUtils.getPEDYears());
            
            try { 
                request.getSession().setAttribute(CensusConstants.PED_MONTH_DAY_LIST, CensusUtils.getPEDMonthAndDay(contractId));            
            } catch (Exception e) {
              logger.error("Contract Service delegate exception", e);
              // Set all attributes retrieved from CSF to null
              request.getSession().setAttribute(CensusConstants.PED_MONTH_DAY_LIST, new ArrayList());
            } 
            
            try { 
                form.setNextPED(CensusUtils.getNextPlanEntryDateAsDate(contractId));            
            } catch (Exception e) {
              logger.error("Contract Service delegate exception", e);
              // Set all attributes retrieved from CSF to null
              form.setNextPED(null);
            }     
            
            try { 
                form.setNextOptOut(CensusUtils.getOptOutDeadlineAsDate(contractId));            
            } catch (Exception e) {
              logger.error("Contract Service delegate exception", e);
              // Set all attributes retrieved from CSF to null
              form.setNextOptOut(null);
            }
            
            try { 
                form.setFrequency(CensusUtils.getFrequency(contractId));            
            } catch (Exception e) {
              logger.error("Contract Service delegate exception", e);
              // Consider yearly
              form.setFrequency(12);
            }
            
            try { 
                form.setOOD(CensusUtils.getOOD(contractId));            
            } catch (Exception e) {
              logger.error("Contract Service delegate exception", e);
              // Consider 0
              form.setOOD(0);
            }
        } else {
            request.getSession().setAttribute(CensusConstants.PED_YEAR_LIST, new ArrayList<LabelValueBean>());
            request.getSession().setAttribute(CensusConstants.PED_MONTH_DAY_LIST, new ArrayList<LabelValueBean>());   
            form.setNextPED(null);
            form.setNextOptOut(null);
            // Consider yearly
            form.setFrequency(12);
            // Consider 0
            form.setOOD(0);
        }
    }

 	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@SuppressWarnings("rawtypes")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
	    if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }
	    EmployeeEnrollmentSummaryReportForm theForm = (EmployeeEnrollmentSummaryReportForm) form;
	    //This code has been changed and added  to Validate form and request 
	    //against penetration attack, prior to other validations as part of the CL#137697.
    	
		Collection errors = super.doValidate( form, request);
 		if (getTask(request).equals(FILTER_TASK)) {
			if (theForm.getNamePhrase() != null && theForm.getNamePhrase().trim().length() > 0) {
				/*NameRule.getLastNameInstance().validate(
						EmployeeEnrollmentSummaryReportForm.FIELD_LAST_NAME, errors, 
                        theForm.getNamePhrase());
                */
			}
            
			if (theForm.getSsn() != null && !theForm.getSsn().isEmpty()) {
				// SSN Number mandatory and standards must be met
				SsnRule.getInstance().validate(
					EmployeeEnrollmentSummaryReportForm.FIELD_SSN,
					errors, 
					theForm.getSsn()
				);
			}
            
            // From date and To date validation
			if (StringUtils.isEmpty(theForm.getFromPED()) &&
            		!StringUtils.isEmpty(theForm.getToPED())) {
            		 errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
            	}
            
            	if ( !StringUtils.isEmpty(theForm.getFromPED()) && StringUtils.isEmpty(theForm.getToPED())){
            		 // EES.375
            		Date now = new Date(System.currentTimeMillis());
            		String currentDate = DATE_FORMAT.format(now);
            		theForm.setToPED(currentDate);
            	}
            	
            	try {
            		Date fromDate = null;
            		if (!StringUtils.isEmpty(theForm.getFromPED())) {
            			fromDate = DATE_FORMAT.parse(theForm.getFromPED());
            		}
            		Date toDate = null;
            		if (!StringUtils.isEmpty(theForm.getToPED())) {
            			toDate = DATE_FORMAT.parse(theForm.getToPED());
            		}
            		if (fromDate !=null) {
	                    if (fromDate.after(toDate)) {
	                        errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	                    }
	                    
	                    Calendar calFromDate = Calendar.getInstance();
	                    calFromDate.setTime(fromDate);
	                    
	    				Calendar calEarliestDate = Calendar.getInstance();
	    				calEarliestDate.add(Calendar.MONTH, -24);
	    				if (calFromDate.before(calEarliestDate)) {
	           				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	    				}
            		}
            	} catch(ParseException pe) {
            		errors.add(new GenericException(ErrorCodes.INVALID_DATE));
            	} 
            
         //   if (theForm.isEZstartOn()==false) { // aci2 stuff, EES.374
            	if ( StringUtils.isEmpty(theForm.getEnrolledFrom()) && !StringUtils.isEmpty(theForm.getEnrolledTo()) ){
            		 errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
            	}
            
            	if (!StringUtils.isEmpty(theForm.getEnrolledFrom()) && StringUtils.isEmpty(theForm.getEnrolledTo())){
            		Date now = new Date(System.currentTimeMillis());
            		String currentDate = DATE_FORMAT.format(now);
            		theForm.setEnrolledTo(currentDate);
            	}
            	
            	try {
            		Date fromDate = null;
            		if (!StringUtils.isEmpty(theForm.getEnrolledFrom())) {
            			fromDate = DATE_FORMAT.parse(theForm.getEnrolledFrom());
            		}
            		Date toDate = null;
            		if (!StringUtils.isEmpty(theForm.getEnrolledTo())) {
            			toDate = DATE_FORMAT.parse(theForm.getEnrolledTo());
            		}
            		if (fromDate !=null) {
	                    if (fromDate.after(toDate)) {
	                        errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
	                    }
	                    
	                    Calendar calFromDate = Calendar.getInstance();
	                    calFromDate.setTime(fromDate);
	                    
	    				Calendar calEarliestDate = Calendar.getInstance();
	    				calEarliestDate.add(Calendar.MONTH, -24);
	    				if (calFromDate.before(calEarliestDate)) {
	           				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
	    				}
            		}
            	} catch(ParseException pe) {
            		errors.add(new GenericException(ErrorCodes.INVALID_DATE));
            	}
            	
            //}
		}
	
 		if (errors.size() > 0) {
			populateReportForm( theForm, request);
			SessionHelper.setErrorsInSession(request, errors);
            EmployeeEnrollmentSummaryReportData reportData = new EmployeeEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}
 	    if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
 		return errors;
 	}
    
    /**
     * Check if the format is correct and update
     * 
     * @param reportForm
     * @param request
     * @param employeesWithValidationErrors
     * 
     * @return a list of errors 
     * @throws SystemException 
     */
    protected List<ValidationError> validateAndUpdateList(EmployeeEnrollmentSummaryReportForm reportForm, HttpServletRequest request) throws SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        List<ValidationError> errorsPerEmployee = new ArrayList<ValidationError>();
        Employee employee = null;  
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        
        HashMap<String, EmployeeSummaryDetails> initialMap = createInitialHash(((EmployeeEnrollmentSummaryReportForm)reportForm.getClonedForm()).getTheItemList());
        
        // Clear the list of employees with errors
        // The map is cleared every time this execution path is initiated
        employeesWithValidationErrors.get().clear();
        
        for (EmployeeSummaryDetails element : reportForm.getTheItemList()) {
            if(employeeNeedsUpdate(element, initialMap)) {
                employee = serviceFacade.getEmployeeByProfileId(Long.parseLong(element.getProfileId()), 
                        userProfile.getCurrentContract().getContractNumber(), null);  
                
                // before validating, take the latest birth date from the database
                if (employee.getEmployeeDetailVO() != null) {
                    element.setBirthDate(employee.getEmployeeDetailVO().getBirthDate());
                }
                
                if (reportForm.validate(errorsPerEmployee, element)) {                        
 
                	reportForm.updateEmployee(employee, element, getUserProfile(request));                      
                  	serviceFacade.updateEmployee(employee); 
                }
                
                if(errorsPerEmployee != null && errorsPerEmployee.size() > 0) {
                    errors.addAll(errorsPerEmployee);
                    
                    // Clear the temporary list for re-use
                    errorsPerEmployee.clear();
                    
                    // Add the employee to the list of employees with errors
                    employeesWithValidationErrors.get().put(element.getProfileId(), element);
                }
            }
        }
        
        return errors;            
     }
    
    /**
     * Populate sort criteria in the criteria object using the given FORM. The
     * default implementation inserts the FORM's sort field and sort direction.
     * Different secondary sorting is required for EmployeeID, Enrollment Status,
     * Enrollment Method and Eligibility Ind.
     * 
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        EmployeeEnrollmentSummaryReportForm reportForm = (EmployeeEnrollmentSummaryReportForm)form;
        
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            if (!form.getSortField().equals(getDefaultSort())) { 
                if (!form.getSortField().equalsIgnoreCase(EmployeeEnrollmentSummaryReportData.EMPLOYEE_ID_FIELD) &&
                    !form.getSortField().equalsIgnoreCase(EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_FIELD) &&
                    !form.getSortField().equalsIgnoreCase(EmployeeEnrollmentSummaryReportData.ENROLLMENT_METHOD_FIELD) &&
                    !form.getSortField().equalsIgnoreCase(EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_FIELD)) {
                    criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
                } else {
                    criteria.insertSort(EmployeeEnrollmentSummaryReportData.PROCESSING_DATE_FIELD, ReportSort.DESC_DIRECTION);
                }
            }
        }
    }
        
    /**
     * Populate a map for easier look-up 
     * The map is populated with the elements from cloned form. These are the
     * initial values as they were returned from the database
     * 
     * @param initialList
     */
    private HashMap<String, EmployeeSummaryDetails> createInitialHash(List<EmployeeSummaryDetails> initialList) {
        HashMap<String, EmployeeSummaryDetails> initialMap =  new HashMap<String, EmployeeSummaryDetails>();
        
        for (EmployeeSummaryDetails element : initialList) {
            initialMap.put(element.getProfileId(), element);
        }
        
        return initialMap;
    }
    
    /**
     * Checks if the details changed
     * <code>EmployeeSummaryDetails</code> equals and hashCode are implemented to take into 
     * consideration just the editable properties from <code>EmployeeSummaryDetails</code>
     * 
     * @param currentDetails
     * @param initialList
     * @return
     */
    private boolean employeeNeedsUpdate(EmployeeSummaryDetails currentDetails, HashMap<String, EmployeeSummaryDetails> initialMap) {
        
        if(currentDetails == null || currentDetails.equals(initialMap.get(currentDetails.getProfileId()))){
            return false;
        }
        
        return true;
    }
    
    /**
     * Utility method used to save into the request the errors and warnings resulted 
     * while saving the updated elements 
     * 
     * @param request
     * @param errors
     */
    protected void setErrors(HttpServletRequest request, List<ValidationError> errors) {
        if (errors != null && errors.size() > 0) {
            request.setAttribute("validationErrors", new EligibilityValidationErrors(errors));
        }
        super.setErrorsInRequest(request, errors);
    }
    
    /**
     * Utility method that checks if there are errors in the request
     * 
     * @param request
     * @return
     */
    protected boolean hasErrors(HttpServletRequest request) {
        EligibilityValidationErrors errors = (EligibilityValidationErrors)request.getAttribute("validationErrors");
        
        if (errors != null && errors.getErrors()!= null && errors.getErrors().size() > 0) {
            return true;
        }

        return false;
    }
    
    /**
     * Utility method that adds new errors/warnings to the existing ones in the request
     * 
     * @param request
     * @return
     */
    protected void addToErrors(HttpServletRequest request, List<ValidationError> newErrors) {
        EligibilityValidationErrors errors = (EligibilityValidationErrors)request.getAttribute("validationErrors");
        
        if (errors != null && errors.getErrors()!= null) {
            errors.getErrors().addAll(newErrors);
        }
    }

    protected ContractProfileVO getContractProfile(int contractId, HttpServletRequest request) {
        ContractProfileVO contractProfileVO = (ContractProfileVO)request.getAttribute(Constants.CONTRACT_PROFILE);
        if (contractProfileVO == null)
        {
        try {
            contractProfileVO = ContractServiceDelegate.getInstance().getContractProfileDetails(
					contractId, Environment.getInstance().getSiteLocation());
        } catch (SystemException e) {
            logger.error(e);
        }
        request.setAttribute(Constants.CONTRACT_PROFILE, contractProfileVO);
        }
        return contractProfileVO;
    }
    
    /**
     * get the MoneyTypeVO for the given money type id
     * 
     * @param moneyTypeId
     * @param moneyTypeVOList
     * @return MoneyTypeVO
     */
    private MoneyTypeVO getMoneyTypeDetailsVO(String moneyTypeId,
			Collection<MoneyTypeVO> moneyTypeVOList) {
	
	for (MoneyTypeVO moneyTypeVO : moneyTypeVOList) {
	    if (StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(moneyTypeVO.getId()))) {
		return moneyTypeVO;
	    }
	}
	return null;
	
    }
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @Autowired
    private EmployeeEnrollmentSummaryReportValidator employeeEnrollmentSummaryReportValidator;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(employeeEnrollmentSummaryReportValidator);
	}
 }

