package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;
import static com.manulife.pension.service.contract.util.ServiceFeatureConstants.YES;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;
/**
 * This class is action class for the Historical Eligibility Report.
 * 
 * @author Ramamohan Gulla
 *
 */
@Controller
@RequestMapping( value = "/census")
@SessionAttributes({"eligibilityReportsForm"})

public class HistoricalEligibilityReportController extends ReportController{

	@ModelAttribute("eligibilityReportsForm") 
	public EligibilityReportsForm populateForm() 
	{
		return new EligibilityReportsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
	forwards.put("default","/census/historicalEligibilityReport.jsp");
	forwards.put("staging","/census/eligibilityReports.jsp");
	}

	public static final String HISTORICAL_ELIGIBILITY_REPORT = "historicalReport";
	public static final String REPORT_TYPE = "reportType";
	public static final String HIST_ELIGIBILITY_REPORT_NAME = "Historical eligibility report";
	public static final String ELIGIBILITY_REPORT_NAME = "Download eligibility report";
	
	/**
	 * SimpleDateFormat is converted to FastDateFormat to make it thread safe
	 */
	FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy");
	FastDateFormat fullMonthFormat = FastDateFormat.getInstance("MMMM dd,yyyy"); 
	
	private static final String className = HistoricalEligibilityReportController.class.getName();

      @Override
      protected String getDefaultSort() {
	return EligibilityReportData.DEFAULT_SORT;
      }

      @Override
      protected String getDefaultSortDirection() {
	return ReportSort.ASC_DIRECTION;
      }

      @Override
      protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		 byte[] bytes = null;
	        
	     if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getDownloadData");
	     }
	        
	     // Identify the type of report
	     if(HISTORICAL_ELIGIBILITY_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
	           bytes = getHistEligibilityDownloadData(reportForm, report, request);
	     }      
	        
	     if (logger.isDebugEnabled()) {
	          logger.debug("exit <- getDownloadData");
	     }

	     return bytes;
     }

     @Override
     protected String getReportId() {
	return EligibilityReportData.REPORT_ID;
     }

     @Override
     protected String getReportName() {
	    return ELIGIBILITY_REPORT_NAME;
     }

     @Override
     protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {


            if (logger.isDebugEnabled()) {
                logger.debug("entry -> populateReportCriteria");
            }
        
            UserProfile userProfile = getUserProfile(request);
            Contract currentContract = userProfile.getCurrentContract();
            EligibilityReportsForm psform = (EligibilityReportsForm) form;
    
            criteria.addFilter(EligibilityReportData.FILTER_CONTRACT_NUMBER, 
                    Integer.toString(currentContract.getContractNumber()));
        
            if (HISTORICAL_ELIGIBILITY_REPORT.equals(request.getParameter(REPORT_TYPE))
    				&& DOWNLOAD_TASK.equals(getTask(request))) {
    			criteria.addFilter(
    					EligibilityReportData.FILTER_REPORT_TYPE,
    					HISTORICAL_ELIGIBILITY_REPORT);
    			criteria.addFilter(EligibilityReportData.SNAPSHOT_DATE,psform.getHistroyPlanEntryDate() );
    			
    			Collection<PlanEntryRequirementDetailsVO> planEntryRequirementVOList = EligibilityServiceDelegate.getInstance(PS_APPLICATION_ID)
    					.getPlanEntryRequirementForPlan(currentContract.getContractNumber());
    			
				if (!(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))) {
					planEntryRequirementVOList.stream().filter(vo -> YES.equals(vo.getPartTimeEligibilityInd()))
							.forEach(vo -> criteria.addFilter(
									EligibilityReportData.FILTER_SHOW_LTPT_INFO_INDICATOR + vo.getMoneyTypeId(), YES));
				}
    	    } 
        
            //Get the filterCriteriaVo from session
            FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
            if(filterCriteriaVo == null ){
		
        	filterCriteriaVo = new FilterCriteriaVo();
            }
		
            // set filterCriteriaVo back to session
            SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
        
            // if external user, don't display Canceled employees
            criteria.setExternalUser(userProfile.getRole().isExternalUser());
            
            if (logger.isDebugEnabled()) {
                logger.debug("criteria= " + criteria);
                logger.debug("exit <- populateReportCriteria");
            }
		
	}
	
     /**
      * This method is called before the historical eligibility page is shown.
      * Also this method used to get the snapshot dates to populate in the drop down.
      */
     @RequestMapping( value="/historicalEligibilityReport/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
     public String doDefault(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     throws IOException,ServletException, SystemException {
    	 if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
 	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	       }
 	       
 		}
		
		UserProfile userProfile = getUserProfile(request);
		EligibilityReportsForm form = (EligibilityReportsForm)actionForm;
		int contractId = userProfile.getCurrentContract().getContractNumber();
		
		 Map csfMap = null;
		 ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		 try {
		     csfMap = service.getContractServiceFeatures(contractId);
		 } catch (ApplicationException ae) {
		     throw new SystemException(ae, className, "loadContractServiceFeatureData", ae
			     .getDisplayMessage());
		 }
		 
		// Get the frequency for the money types for the Contract.
			
		 EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);
		     
		 List<LabelValueBean> moneyTypes = new ArrayList<LabelValueBean>();
		 ContractServiceFeature aeCSF = (ContractServiceFeature) csfMap
			.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		 ContractServiceFeature eligibilityCalculationCSF = (ContractServiceFeature) csfMap
			.get(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
		
		 if ((eligibilityCalculationCSF == null || !YES.equals(eligibilityCalculationCSF.getValue())) &&
			     (aeCSF == null || !YES.equals(aeCSF.getValue()))) {
		             
			 return forwards.get(HOMEPAGE_FINDER_FORWARD_REDIRECT);
		 }
		 
		/* if(form.getMoneyTypes()==null ||form.getMoneyTypes().size() ==0 ){
		 
		     if (eligibilityCalculationCSF != null &&  !"N".equalsIgnoreCase(eligibilityCalculationCSF.getValue()) ) {*/
			
	    	     	/*// get the money types for the contract
			
			    MoneyTypeVO moneyTypeVO = null;
			    List<MoneyTypeVO> contractMoneyTypesVoList = service
						.getContractMoneyTypes(contractId, true);

			    // get money types for the contract with EC on
			    List<String> moneyTypesList = eligibilityServiceDelegate
						.getMoneyTypesFromEligibilitySnapshot(contractId);
			    
			    Collections.sort(moneyTypesList);
			    
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
				moneyTypes.add(new LabelValueBean(moneyTypeVO.getContractShortName(),moneyTypeVO.getId()));
			    }*/
			 
			    moneyTypes =   eligibilityServiceDelegate
				.getMoneyTypesFromEligibilitySnapshot(contractId);
			    
			    
			
		    /* }else  if(aeCSF != null && "Y".equalsIgnoreCase(aeCSF.getValue())){

			 moneyTypes.add(new LabelValueBean("EEDEF","EEDEF"));
			
		     }*/
		 
		 	
		     // sort the money types based on alphabetical order of money source
		     // short names
		
		     Collections.sort(moneyTypes, new Comparator<LabelValueBean>() {
			 public int compare(LabelValueBean vo1, LabelValueBean vo2) {
			     return vo1.getLabel().compareTo(
				 vo2.getLabel());
		     }
		     });
		    
		     form.setMoneyTypes(moneyTypes);
		     
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
		 
		 //}
			
		List<LabelValueBean> historyEligibilityDates = eligibilityServiceDelegate.getHistoryPlanEntryDates(contractId);
		
		List <String> histDates = new ArrayList<String>();
		
		for(LabelValueBean  lvb : historyEligibilityDates){
		    histDates.add(lvb.getLabel());
		}
		form.setHistDatesAndCreatedTS(historyEligibilityDates);
		form.setHistoryPlanEntryDatesList(histDates);
			
		
		return forwards.get("default");

     }
	
     /**
      * This method is used to generate the Historical Eligibility report in CSV format.
      * 
      * @param reportForm
      * @param report
      * @param request
      * @return
      * @throws SystemException
      */
     protected byte[] getHistEligibilityDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request)throws SystemException {    
	  
        EligibilityReportData histReport = (EligibilityReportData)request.getAttribute(Constants.REPORT_BEAN);
	  List<EmployeeSummaryDetails> empDetails = (List<EmployeeSummaryDetails>)histReport.getDetails();
		Map<String,String> eligibilityReportData = (Map<String,String>)histReport.getEligibiltyHistoryReportMap();
		EligibilityReportsForm form = (EligibilityReportsForm)reportForm;
		histReport.setMoneyTypes(form.getMoneyTypes());
		for(int emp = 0; emp < empDetails.size(); emp++){
			
		    List<String> list = new ArrayList<String>();
		    List<String> eligibilityDataList = new ArrayList<String>();
		    String eligibilityData ="";
			
		     for(int mt = 0; mt < histReport.getMoneyTypes().size(); mt++){
		
			 // For Eligibility report
			 if(eligibilityReportData.get(empDetails.get(emp).getProfileId()+histReport.getMoneyTypes().get(mt).getValue())!= null){
			     eligibilityData = eligibilityReportData.get(empDetails.get(emp).getProfileId()+histReport.getMoneyTypes().get(mt).getValue()).toString();
			     eligibilityDataList.add(eligibilityData);
						
			 }else{
			     eligibilityData = ",,,,,,,";
			     eligibilityDataList.add(eligibilityData);
			 }
				
		     }
		     empDetails.get(emp).setPlanEntryDates(list);
		     empDetails.get(emp).setEligibilityData(eligibilityDataList);
		}
	  
		  
	  
	  StringBuffer buffer = new StringBuffer();
	  UserProfile user = getUserProfile(request);
	  Contract currentContract = user.getCurrentContract();
	  
	  Collection<PlanEntryRequirementDetailsVO> planEntryRequirementVOList = EligibilityServiceDelegate.getInstance(PS_APPLICATION_ID)
				.getPlanEntryRequirementForPlan(currentContract.getContractNumber());
	  
	  String snapshotTakenDate = "";
	  
	  for(LabelValueBean lvb : form.getHistDatesAndCreatedTS()){
	      
	      if(lvb.getLabel().equalsIgnoreCase(form.getHistroyPlanEntryDate())){
		  snapshotTakenDate = lvb.getValue();
	      }
	  }
	  Date snapshotDate = null;
	  try{
	      snapshotDate =  fullMonthFormat.parse(form.getHistroyPlanEntryDate());
	  }catch(ParseException pe){
	        	// No parse exception would occur here.
	  }
	  
	  String snapShotDateString = dateFormat.format(snapshotDate);
	  // Title
        buffer.append("Historical Eligibility Report").append(LINE_BREAK);
        // Contract #, Contract name
        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);
        buffer.append("Historical Snapshot Taken on").append(COMMA).append(snapshotTakenDate).append(LINE_BREAK);
        buffer.append("For Plan Entry Date").append(COMMA).append(snapShotDateString).append(LINE_BREAK);
        buffer.append("Money Type: ALL").append(LINE_BREAK).append(LINE_BREAK);
        
          try{
		Content message = null;
		if(!form.isEZstartOn() && form.isEligibiltyCalcOn()){
		    message = ContentCacheManager.getInstance().getContentById(
				ContentConstants.HISTORICAL_ELIGIBILITY_REPORT_EC_Y_AND_AE_N,
				ContentTypeManager.instance().MESSAGE);
		}else if(form.isEZstartOn() && !form.isEligibiltyCalcOn()){
		    message = ContentCacheManager.getInstance().getContentById(
				ContentConstants.HISTORICAL_ELIGIBILITY_REPORT_EC_N_AND_AE_Y,
				ContentTypeManager.instance().MESSAGE);
		}else if(form.isEZstartOn() && form.isEligibiltyCalcOn()){
		    message = ContentCacheManager.getInstance().getContentById(
				ContentConstants.HISTORICAL_ELIGIBILITY_REPORT_EC_Y_AND_AE_Y,
				ContentTypeManager.instance().MESSAGE);
		}

		String contentMessage = ContentUtility.getContentAttribute(message, "text");
		
		contentMessage = contentMessage == null ? "":contentMessage;
		
		buffer.append("\""+contentMessage + "\"").append(LINE_BREAK);
					
	  }catch (ContentException exp) {
		throw new SystemException(exp, getClass().getName(),
					"getDownloadData", "Something wrong with CMA");
	  }
          
		boolean displayMessageLTPT = false;
		if (!(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))) {
			for (int mt = 0; mt < histReport.getMoneyTypes().size(); mt++) {

				final int i = mt;

				if (planEntryRequirementVOList.stream()
						.anyMatch(vo -> (StringUtils.equalsIgnoreCase(vo.getMoneyTypeId(),
								histReport.getMoneyTypes().get(i).getValue())
								&& YES.equals(vo.getPartTimeEligibilityInd())))) {

					displayMessageLTPT = true;
					break;
				}

			}
		}

		if (displayMessageLTPT) {
			buffer.append(
					"\"The Long-Term Part-Time (LTPT) eligibility indicator is based on the employee's initial eligibility qualification.\"")
					.append(LINE_BREAK);
		}
          
	  buffer.append(LINE_BREAK);
          buffer.append("Employee Last Name").append(COMMA)
              .append("Employee First Name").append(COMMA)
              .append("Employee Middle Initial").append(COMMA)
              .append("SSN").append(COMMA)
              .append("Date of Birth").append(COMMA)
              .append("Hire Date").append(COMMA);
        	 if(form.isHasPayrollNumberFeature()){
        		 buffer.append("Employee ID").append(COMMA);
        	 }
        	 if(form.isHasDivisionFeature()){
        		 buffer.append("Division").append(COMMA);
        	 }
        	 
        	 buffer.append("Employment Status").append(COMMA)
        	 .append("Employment Status Effective Date").append(COMMA);
        	 
        	 buffer.append("Enrollment Status").append(COMMA)
              .append("Enrollment Method").append(COMMA)
              .append("Enrollment Processing Date").append(COMMA)
              .append("Eligible to Participate").append(COMMA);
        
        for(int mt = 0; mt < histReport.getMoneyTypes().size(); mt++){
        	
        	String moneyTypeLabel = null;
        	
        	if (StringUtils.isNotBlank(histReport.getMoneyTypes().get(mt).getLabel())){
        		moneyTypeLabel = histReport.getMoneyTypes().get(mt).getLabel();
        	} else {
        		moneyTypeLabel = histReport.getMoneyTypes().get(mt).getValue();
        	}
        	
        	buffer.append("Eligibility Date - "+moneyTypeLabel).append(COMMA);
        	
        	if(!(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))){
        		
        		final int i = mt;
        		final String moneyTypeLabelCopy = moneyTypeLabel;
				planEntryRequirementVOList.stream().forEach(vo -> {
					if (StringUtils.equalsIgnoreCase(vo.getMoneyTypeId(), histReport.getMoneyTypes().get(i).getValue())
							&& YES.equals(vo.getPartTimeEligibilityInd())) {
						buffer.append("LTPT Qualified - " + moneyTypeLabelCopy).append(COMMA);
					}
				});
        	}
        	
        	buffer.append("Plan Entry Date - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Calculation Override - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Period of Service From Date - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Period of Service To Date - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Type of Period - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Period Hours Worked - "+moneyTypeLabel).append(COMMA);
        	buffer.append("Period Hours Effective Date - "+moneyTypeLabel).append(COMMA);
        }
        buffer.append("Opt-out Indicator");
        buffer.append(LINE_BREAK).append(LINE_BREAK);
        
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }   
        
        for(int det=0; det <empDetails.size(); det++){
        	
        	buffer.append(escapeField(empDetails.get(det).getLastName())).append(COMMA);
        	buffer.append(escapeField(empDetails.get(det).getFirstName())).append(COMMA);
        	buffer.append(empDetails.get(det).getMiddleInitial() == null ? ""
        			: escapeField(empDetails.get(det).getMiddleInitial())).append(COMMA);
                
        	buffer.append(SSNRender.format(empDetails.get(det).getSsn(),null, maskSsnFlag)).append(COMMA);
        	
        	buffer.append(empDetails.get(det).getBirthDate() == null ? ""
                            : DateRender.formatByPattern(empDetails.get(det).getBirthDate(), "", 
                            		RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
        	buffer.append(empDetails.get(det).getHireDate() == null ? ""
                    : DateRender.formatByPattern(empDetails.get(det).getHireDate(), "", 
                    		RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
        	if(form.isHasPayrollNumberFeature()){
        		buffer.append(empDetails.get(det).getEmployerDesignatedID() == null ? "" 
        			:empDetails.get(det).getEmployerDesignatedID() ).append(COMMA);
        	}
        	if(form.isHasDivisionFeature()){
       		 buffer.append(empDetails.get(det).getDivision() == null ? "" 
       			: escapeField(empDetails.get(det).getDivision())).append(COMMA);
        	}
        	
        	buffer.append(empDetails.get(det).getEnrollmentStatus() == null ? ""
        			: empDetails.get(det).getEmploymentStatus()).append(COMMA);
        	buffer.append(empDetails.get(det).getHireDate() == null ? ""
                    : DateRender.formatByPattern(empDetails.get(det).getEmploymentStatusEffDate(), "", 
                    		RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
        	
        	buffer.append(empDetails.get(det).getEnrollmentStatus() == null ? ""
        			: empDetails.get(det).getEnrollmentStatus()).append(COMMA);
        	buffer.append(empDetails.get(det).getEnrollmentMethod() == null ? ""
        			: empDetails.get(det).getEnrollmentMethod()).append(COMMA);
        	buffer.append(empDetails.get(det).getEnrollmentProcessedDate() == null ? ""
        			: empDetails.get(det).getEnrollmentProcessedDate()).append(COMMA);
        	buffer.append(empDetails.get(det).getEligibleToEnroll() == null ? ""
        			: empDetails.get(det).getEligibleToEnroll()).append(COMMA);
        	 for(int mt = 0; mt < histReport.getMoneyTypes().size(); mt++){
        		 buffer.append(empDetails.get(det).getEligibilityData().get(mt)).append(COMMA);
        	 }
        	buffer.append(empDetails.get(det).getAutoEnrollOptOutInd() == null ? ""
        			: empDetails.get(det).getAutoEnrollOptOutInd());
        	buffer.append(LINE_BREAK);
        }
	  
	  
	  return buffer.toString().getBytes();
     }
	
     /**
      *  This method is used to get the file name.
      *  The file name format is : 
      *  	contract number_Type Of Report_date of report creating in mm dd yyyy format.csv
      */
     protected String getFileName(HttpServletRequest request) {
	 
	 String fileName = "";
	 String date = "MM dd yyyy";
	 SimpleDateFormat dateFormat = new SimpleDateFormat(date);
	 	date = dateFormat.format(new Date());
	        
	 // Identify the type of report
	 if(HISTORICAL_ELIGIBILITY_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
	           
	     fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
	     " " + HIST_ELIGIBILITY_REPORT_NAME + " " + date + CSV_EXTENSION;
	       	
	 }
	 
	 // Replace spaces with underscores
	 
	 return fileName.replaceAll("\\ ", "_");
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
	
	
	/**
	 * Adding escape field if any comma character found in a String
	 * 
	 * @param field
	 * @return
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {

			StringBuffer newField = new StringBuffer();
			newField = newField.append(QUOTE).append(field).append(QUOTE);
			return newField.toString();

		} else {
			return field;
		}
	}
	
	
	@RequestMapping(value = "/historicalEligibilityReport/", params = {"task=filter"},method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/historicalEligibilityReport/", params = {"task=page"}, method ={RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/historicalEligibilityReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value ="/historicalEligibilityReport/", params = {"task=download"}, method = {RequestMethod.POST})
	public String doDownload(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/historicalEligibilityReport/", params = {"task=dowanloadAll"}, method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
