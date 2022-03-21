package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.TaskCenterTasksReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;

/**
 * TaskCenterTasksReportAction is used to retrieve
 * task center tasks data for a specific contract.
 * 
 * @author Glen Lalonde
 */
@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"taskCenterTasksReportForm"})
public class TaskCenterTasksReportController extends ReportController {
	
		@ModelAttribute("taskCenterTasksReportForm") 
		public  TaskCenterTasksReportForm populateForm()
		{
			return new  TaskCenterTasksReportForm();
			}	
		public static HashMap<String,String> forwards = new HashMap<String,String>();
		static{
			forwards.put("input","/participant/taskCenterTasksReport.jsp");
			forwards.put("default","/participant/taskCenterTasksReport.jsp");
			forwards.put("sort","/participant/taskCenterTasksReport.jsp");
			forwards.put("filter","/participant/taskCenterTasksReport.jsp");
			forwards.put("page","/participant/taskCenterTasksReport.jsp");
			forwards.put("print","/participant/taskCenterTasksReport.jsp");
			forwards.put("save","/participant/taskCenterTasksReport.jsp");
			}


	private static final int DUMMY_YEAR = 1970;
	private static List EMPTY_LIST = new LinkedList();
	private static String TASKS_REPORT_NAME = "Task Deferral rprt";
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("MM/dd/yyyy");
	
 	private static String FILTER_TASK = "filter";
	//CL119971 fix - Deferral task report not displaying zero - start
	private static String DEFERRAL_TASK_ZERO = "0";
	//CL119971 fix - Deferral task report not displaying zero - end

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		TaskCenterTasksReportForm tcform = (TaskCenterTasksReportForm) form;
		
		// always need the contractId
		UserProfile userProfile = getUserProfile(request);
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		
		PlanDataLite planData = ContractServiceDelegate.getInstance().getPlanDataLight(contractNumber);

		PlanEmployeeDeferralElection planEmployeeDeferralElection = planData.getPlanEmployeeDeferralElection();
		Date upComingAdHocChangeDate =  PlanEmployeeDeferralElection.getUpComingAdHocChnageDate(
							planData.getPlanYearEnd(),
							planEmployeeDeferralElection.getEmployeeDeferralElectionCode(),
							planEmployeeDeferralElection.getEmployeeDeferralElectionSelectedDay(),
							planEmployeeDeferralElection.getEmployeeDeferralElectionSelectedMonths());

		Map csfMap;
		try {
			csfMap = ContractServiceDelegate.getInstance().getContractServiceFeatures(contractNumber);
		} catch (ApplicationException e) {
			logger.error(e);
			throw new SystemException("Exception occurred while retriving ContractServiceFeatures" + e);
		}
		
		// Get the AE related CSF attributes
		ContractServiceFeature csf = 
			(ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);
		
		String optOutDays = null;
		if (csf != null) {
			// Get the opt-out days
			optOutDays = csf.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
		}

		// To check whether current date is in the Ad-hoc Freeze Period or not
		//boolean isAdhocFreezePeriod = ContractServiceFeatureUtil
		//		.isAdhocChangeRequestPeriod(upComingAdHocChangeDate,
		//				optOutDays, planEmployeeDeferralElection
		//						.getEmployeeDeferralElectionCode());
		//if (isAdhocFreezePeriod) {

			criteria.addFilter(TaskCenterTasksReportData.FILTER_CONTRACTID, new Integer(userProfile
					.getCurrentContract().getContractNumber()));
			
			criteria.addFilter(TaskCenterTasksReportData.ADHOC_FREZZE_PERIOD, true);

			tcform.setContractNumber(String.valueOf(userProfile.getCurrentContract().getContractNumber())); // setup for use on download
	 		
			// next condition is for the Participant Account, net ee Deferral page alert linking to task center support
			if (request.getParameter("profileId") !=null) {
				criteria.addFilter(TaskCenterTasksReportData.FILTER_PROFILE_ID, request.getParameter("profileId"));
			}
			
			if(tcform.getLastName() !=null && tcform.getLastName().length() > 0) {
		        criteria.addFilter(TaskCenterTasksReportData.FILTER_LAST_NAME, tcform.getLastName());
			}
	
			if(!tcform.getSsn().isEmpty()) {
		        criteria.addFilter(TaskCenterTasksReportData.FILTER_SSN, tcform.getSsn().toString());
			}
			
			if (tcform.getDivision() !=null && tcform.getDivision().trim().length() >0 ) {
				criteria.addFilter(TaskCenterTasksReportData.FILTER_DIVISION, tcform.getDivision().trim());
			}
			
			// processed time is used to recently processed records along with PA records on summary screen
			if (tcform.getProcessedTimestamp() == null || getTask(request).equals(FILTER_TASK)) { // reset when search hit
				// don't want to use a null value since it would match on all the records not setup
				// so just use a value that will match nothing.
				long currentTime = System.currentTimeMillis();
				tcform.setProcessedTimestamp(currentTime);
				criteria.addFilter(TaskCenterTasksReportData.FILTER_PROCESSED_TS, new java.sql.Timestamp(currentTime));			
			} else {
				criteria.addFilter(TaskCenterTasksReportData.FILTER_PROCESSED_TS, new java.sql.Timestamp(tcform.getProcessedTimestamp()));
			}
			
	        if (userProfile.isInternalUser()==false) {
	        	criteria.addFilter(TaskCenterTasksReportData.FILTER_EXTERNAL_USER_VIEW, "Y"); // [5.7]
	        }
		//}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-  populateReportCriteria");
		}
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TaskCenterTasksReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TaskCenterTasksReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "default";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

    
	// as per [8.5.1]
    protected String getFileName(HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM dd yyyy");
        String date = dateFormat.format(new Date());

        String fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
                " " + TASKS_REPORT_NAME + " " + date + CSV_EXTENSION;
        
        return fileName.replaceAll("\\ ", "_");  // Replace spaces with underscores
    }

	
	/**
	 * Called by framework to gen excel (csv) data
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		TaskCenterTasksReportForm tcForm = (TaskCenterTasksReportForm)reportForm;
		List<TaskCenterTasksDetails> detailsList = tcForm.getDetailsList();
		UserProfile user = getUserProfile(request);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, user.getCurrentContract().getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        } 
		ProtectedStringBuffer buffer = new ProtectedStringBuffer();
		buffer.append("Contract number").append(COMMA).append(tcForm.getContractNumber()).append(LINE_BREAK);
		buffer.append("Download Date").append(COMMA).append(SHORT_DATE_FORMAT.format(System.currentTimeMillis())).append(LINE_BREAK);
		buffer.append("# of Requests").append(COMMA).append(detailsList.size()).append(LINE_BREAK);
		buffer.append("Last Name").append(COMMA).append(getNotNull(tcForm.getLastName())).append(LINE_BREAK);
 		if (!tcForm.getSsn().isEmpty())
		buffer.append("SSN").append(COMMA).append(SSNRender.format(tcForm.getSsn().toString(),null, maskSsnFlag)).append(LINE_BREAK);
 		else buffer.append("SSN").append(COMMA).append("").append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
		buffer.append("Last Name,First Name,Middle Initial,SSN,");
		if (tcForm.hasPayrollNumberFeature()) {
			buffer.append("Employee Identification Number,");
		}
		if (tcForm.hasDivisionFeature()) {
			buffer.append("Division,");
		}
		buffer.append("Before tax dollar,Before tax percent,Roth 401(k) dollar,Roth 401(k) percent,Processed Status,Alert/Warning Flag");
		buffer.append(LINE_BREAK);
		
		for(TaskCenterTasksDetails details : detailsList) {
			//CL114799 additional fix - escape comma for fName,lName fields  
			buffer = escapeField(details.getLastName(),buffer);
			buffer = escapeField(details.getFirstName(),buffer);
			buffer.append(getNotNull(details.getMiddleInitial())).append(COMMA);
			buffer.append(SSNRender.format(details.getSSNRaw(),null, maskSsnFlag)).append(COMMA);
			if (tcForm.hasPayrollNumberFeature()) {
				buffer.append(details.getEmployeeId()).append(COMMA); 
			}
			if (tcForm.hasDivisionFeature()) {
				//CL114799 fix - Displaying wrong column values 
				 String division = details.getDivision();
				 buffer = escapeField(division,buffer);

			}
			
			if (details.isRoth()==false) {
			    buffer.append(encodeDownloadValue(details.getContribAmt())).append(COMMA);
			    buffer.append(encodeDownloadValue(details.getContribPct())).append(COMMA);
			    buffer.append(COMMA).append(COMMA);
			} else {
			    buffer.append(COMMA).append(COMMA);
			    buffer.append(encodeDownloadValue(details.getContribAmt())).append(COMMA);
			    buffer.append(encodeDownloadValue(details.getContribPct())).append(COMMA);				
			}
			//CL114799 additional fix - escape comma for status  
			buffer = escapeField(details.getStatusForDisplay(),buffer);
			buffer.append((details.hasAlert() || details.hasWarning()) ? "Y" : ""); // alert/warn flag
			
			buffer.append(LINE_BREAK);
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}
	
	
	private String getNotNull(String value) {
		if (value == null) return "";
		return value;
	}

	private  String encodeDownloadValue(BigDecimal value) {
		//CL119971 fix - Deferral task report not displaying zero - start
		if (value == null ) {
			return "";
		} else if(value.doubleValue() == 0.0){
			return DEFERRAL_TASK_ZERO;
		//CL119971 fix - Deferral task report not displaying zero - end
		} else {
			return value.toString();
		}

	}
		
	@RequestMapping(value ="/taskCenterTasks" ,params={"actionValue=save"}, method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm( form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
	       }
           
        if (logger.isDebugEnabled()) {
            logger.info("entry <- doSave");
        }
        
       // TaskCenterTasksReportForm form = (TaskCenterTasksReportForm)reportForm;
                
        request.removeAttribute("validationErrors");
       
        // quick check for decline without Remarks section on screen, if so process[8.9.3]
        if (form.isShowRemarks() == false) {
            for (TaskCenterTasksDetails detailItem : form.getDetailsList()) {            	
            	if (detailItem.isDecline()) { // did user select a checkbox
            		form.setShowRemarks(true);
                    request.setAttribute(Constants.REPORT_BEAN, form.getReport());
                    request.setAttribute("disableButtons", "");
                    return forwards.get("save");  // NOTE: abort processing and return        	
            	}
            }
        } else {
        	// [8.9.4]
        	boolean found = false;
            for (TaskCenterTasksDetails detailItem : form.getDetailsList()) {
            	if (detailItem.isDecline()) { // did user select a decline checkbox
            		found = true;
            		break;
            	}
            }
        	if (!found) form.setShowRemarks(false);
        }
                
        List<ValidationError> errors = validateAndProcess(form, request); 
                
        if (errors.size() > 0) {
            setErrors(request, errors);
//            return doCommon( reportForm, request,response);
            request.setAttribute(Constants.REPORT_BEAN, form.getReport());
            request.setAttribute("disableButtons", "");
            return forwards.get("save"); 
        } else {     
        	form.setShowRemarks(false); // completed with no errors
           // form.clear( request); TODO: Glen, not sure why he was using cloneable form stuff?
            String forward=doCommon( form, request,response);

            return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }        
    }
	@RequestMapping(value ="/taskCenterTasks" ,params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
       
    	if (logger.isDebugEnabled()) {
        logger.info("entry <- doDownload");
    	}
        String forward = super.doDownload(form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/taskCenterTasks" ,params={"task=sort"}, method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
       
    	if (logger.isDebugEnabled()) {
        logger.info("entry <- doSort");
    	}
        String forward = super.doSort(form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSort");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterTasks" ,params={"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
       
    	if (logger.isDebugEnabled()) {
        logger.info("entry <- doFilter");
    	}
        String forward = super.doFilter(form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doFilter");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterTasks" ,params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
       
    	if (logger.isDebugEnabled()) {
        logger.info("entry <- doPage");
    	}
        String forward = super.doPage(form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPage");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterTasks" ,params={"task=print"}, method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(form, request);
			TaskCenterTasksReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
       
    	if (logger.isDebugEnabled()) {
        logger.info("entry <- doPrint");
    	}
        String forward = super.doPrint(form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPrint");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}	
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		 
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		String forward = super.doCommon( reportForm, request, response);
		
		TaskCenterTasksReportData report = (TaskCenterTasksReportData)request.getAttribute(Constants.REPORT_BEAN);
		
		TaskCenterTasksReportForm tctReportForm = (TaskCenterTasksReportForm)reportForm;
		
		tctReportForm.setReport(report);
		tctReportForm.storeClonedForm();
        tctReportForm.setShowActionButtons(SessionHelper.getUserProfile(request).isAllowedUpdateCensusData());
        
		// special case of linking for participant account, net ee deferral tab
		if (request.getParameter("profileId") != null && report.getDetails().size()>0) {
			TaskCenterTasksDetails details = (TaskCenterTasksDetails)report.getDetails().iterator().next();
			String ssn = details.getSSNRaw();
			tctReportForm.setSsnOne(ssn.substring(0, 3));
			tctReportForm.setSsnTwo(ssn.substring (3,5));
			tctReportForm.setSsnThree(ssn.substring(5));
		}
        
		UserProfile userProfile = getUserProfile(request);
        if (userProfile.getCurrentContract().hasSpecialSortCategoryInd()) { 
        	tctReportForm.setHasDivisionFeature(true);
		} else {
			tctReportForm.setHasDivisionFeature(false);
		}	
        
        if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE.equalsIgnoreCase(
                userProfile.getCurrentContract().getParticipantSortOptionCode())) {            
        	tctReportForm.setHasPayrollNumberFeature(true);
		} else {
			tctReportForm.setHasPayrollNumberFeature(false);
		}

		request.setAttribute("disableButtons", "disabled"); // disable cancel/save
		
		if (FILTER_TASK.equalsIgnoreCase(getTask(request))) {
			tctReportForm.setShowRemarks(false); // defect 6568
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		
		return forward;
	}	

	
	/**
	 * save validation and processing
	 * NOTE: records with approve/decline which pass validation WILL be processed, even if
	 *       other records are in error(partical processing is done)
	 */
	private List<ValidationError> validateAndProcess(TaskCenterTasksReportForm reportForm, 
			                                         HttpServletRequest request)  throws SystemException { 
		ParticipantServiceDelegate psDelegate = ParticipantServiceDelegate.getInstance();
        List<ValidationError> errors = new ArrayList<ValidationError>();
        UserProfile userProfile = getUserProfile(request);
        String contractNumber = String.valueOf(userProfile.getCurrentContract().getContractNumber());
        
        if (reportForm.getRemarks().trim().length() > 750) { // 4.10.2
            ValidationError validationError = 
           	    new ValidationError("remarks", ErrorCodes.REMARKS_TOO_LONG); 
            errors.add(validationError);
        }
                
        if (errors.size()>0) return errors;
        
        String remarks = reportForm.getRemarks().trim();
        for (TaskCenterTasksDetails detailItem : reportForm.getDetailsList()) {
        	boolean itemInError = false;
        	detailItem.clearFieldsInError();
        	
        	try {
	        	if (detailItem.isDecline()) { // did user select a checkbox 
	        		// 8.9.x business rule.
	        		if (remarks.length() == 0) { // 4.10.1
	        			itemInError = true;
	                    ValidationError validationError = new ValidationError("decline", ErrorCodes.REMARK_MISSING);
	        			errors.add(validationError);
	        			detailItem.setFieldInError("decline"); // signal jsp
	        		}
	        		
	        		if (!itemInError) {
	        			// always set the current timestamp
	        			reportForm.setProcessedTimestamp(new Long(System.currentTimeMillis()));
	        				        			
	        			psDelegate.declineACIRequest(contractNumber, detailItem.getProfileId(),
	        					                     detailItem.getCreatedTS(), detailItem.getInstructionNo(),
	        					                     remarks, userProfile.getPrincipal().getProfileId(), 
	        					                     userProfile.isInternalUser(),
	        					                     reportForm.getProcessedTimestamp(), detailItem.isADHocRequest());
	        		}
	        	}
	        		
	        	// 4.8.2 validation
	        	if (detailItem.isApprove()) {
	        		if (detailItem.isAccountHolder() == false) {
	        			itemInError = true;
	        			String userName = detailItem.getFirstName() + " "+ detailItem.getLastName();
	                    ValidationError validationError = 
	                   	   new ValidationError("approve", ErrorCodes.APPROVE_NON_ACCOUNT_HOLDER, new Object[] {userName});
	                    errors.add(validationError);
	                    detailItem.setFieldInError("approve"); // signal jsp
	        		}
	        		
	        		if (!itemInError) {
	        			//always set the current timestamp
	        			reportForm.setProcessedTimestamp(new Long(System.currentTimeMillis()));
	        			
	        			
	        			psDelegate.approveACIRequest(contractNumber, detailItem.getProfileId(),
	        					                     detailItem.getCreatedTS(), detailItem.getInstructionNo(),
	        					                     userProfile.getPrincipal().getProfileId(), 
	        					                     userProfile.isInternalUser(), reportForm.getProcessedTimestamp());
	        		}
	        	}    
        	} catch(Exception e) {
        		e.printStackTrace();
        		// ignore/handle here so rest of list can be processed, don't abort 
        	}
        }
        
        
        ContractServiceDelegate.getInstance().createAdhocAndACIDeferralChangeEvents(Integer.valueOf(contractNumber));

        return errors;
	}


	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value ="/taskCenterTasks" ,method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			populateReportForm(actionform, request);
			TaskCenterTasksReportData reportData = actionform.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("disableButtons", "disabled");
			return forwards.get("input");
		}
		String forward=super.doDefault(actionform, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterTasks" ,method =  {RequestMethod.POST}) 
	public String execute (@Valid @ModelAttribute("taskCenterTasksReportForm") TaskCenterTasksReportForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
        
        // check for selected access
        if (userProfile.isSelectedAccess()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        // check if contract is discontinued
        if (userProfile.getCurrentContract().isDiscontinued()) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();
	}

	
    protected void setErrors(HttpServletRequest request, List<ValidationError> errors) {
        if (errors != null && errors.size() > 0) {
            request.setAttribute("validationErrors", new TaskCenterValidationErrors(errors));
        }
        super.setErrorsInRequest(request, errors);
    }
    
    private static Calendar clearTimeOnCalendar(Calendar cal){
		if(cal != null){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}
		return cal;
	}
	
	//CL114799 fix - Displaying wrong column values - start
	/**
     * Method to display comma(,) in downloaded csv report.
     * @param value String
     * @param buffer StringBuffer
     * @return buffer StringBuffer
	 */
	private ProtectedStringBuffer escapeField(String value, ProtectedStringBuffer buffer) { 
		if(value.indexOf(COMMA) != -1 ){
			String DOUBLE_QUOTES = "\"";
			buffer.append(DOUBLE_QUOTES).append(value).append(DOUBLE_QUOTES).append(COMMA);
		}else{
			buffer.append(value).append(COMMA);
		}

		return buffer;
	}
	 protected void populateReportForm(
	            BaseReportForm reportForm, HttpServletRequest request) {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> populateReportForm");
	        }

	        String task = getTask(request);

	        /*
	         * Reset page number properly.
	         */
	        if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK)
	                || task.equals(FILTER_TASK) || task.equals(PRINT_TASK)
	                || task.equals(DOWNLOAD_TASK)
	                || task.equals(PRINT_PDF_TASK) || task.equals(DOWNLOAD_ALL_TASK)) {
	            reportForm.setPageNumber(1);
	        }

	        /*
	         * Set default sort if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
	                || reportForm.getSortDirection().length() == 0) {
	            reportForm.setSortDirection(getDefaultSortDirection());
	        }

	        /*
	         * Set default sort direction if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
	                || reportForm.getSortField().length() == 0) {
	            reportForm.setSortField(getDefaultSort());
	        }

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <- populateReportForm");
	        }
	    }
	//CL114799 fix - Displaying wrong column values - end
	@Autowired
	private PSValidatorFWInput  psValidatorFWInput;
	@Autowired
	private TaskCenterTasksReportValidator taskCenterTasksReportValidator;
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(taskCenterTasksReportValidator);
	}
}
