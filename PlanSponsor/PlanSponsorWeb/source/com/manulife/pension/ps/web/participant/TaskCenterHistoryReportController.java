package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryDetails;
import com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;

/**
 * TaskCenterTasksReportAction is used to retrieve
 * task center tasks data for a specific contract.
 * 
 * 
 * @author Glen Lalonde
 */
@Controller
@RequestMapping(value ="/participant")
@SessionAttributes({"taskCenterHistoryReportForm"})

public class TaskCenterHistoryReportController extends ReportController {

	@ModelAttribute("taskCenterHistoryReportForm") 
	public  TaskCenterHistoryReportForm populateForm() 
	{
		return new  TaskCenterHistoryReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/taskCenterHistoryReport.jsp"); 
		forwards.put("default","/participant/taskCenterHistoryReport.jsp");
		forwards.put("sort","/participant/taskCenterHistoryReport.jsp"); 
		forwards.put("filter","/participant/taskCenterHistoryReport.jsp");
		forwards.put("page","/participant/taskCenterHistoryReport.jsp");
		forwards.put("print","/participant/taskCenterHistoryReport.jsp"); 
		forwards.put("save","/participant/taskCenterHistoryReport.jsp");
		forwards.put("historyPrint","/participant/taskCenterHistoryPrintReport.jsp");
}

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static FastDateFormat SIMPLE_DATE_FORMAT = FastDateFormat.getInstance("MM/dd/yyyy");
	
 	private static List EMPTY_LIST = new LinkedList();
	private static String TASKS_REPORT_NAME = "Task Deferral History rprt";

 	
	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		TaskCenterHistoryReportForm tcform = (TaskCenterHistoryReportForm) form;

		// always need the contractId
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(TaskCenterHistoryReportData.FILTER_CONTRACTID, new Integer(userProfile
				.getCurrentContract().getContractNumber()));

		tcform.setContractNumber(String.valueOf(userProfile.getCurrentContract().getContractNumber()));
		
		boolean dateRangeSet = false;
		boolean filterAttributeSet = false;
 		
		if(tcform.getLastName() !=null && tcform.getLastName().length() > 0) {
			filterAttributeSet = true;
	        criteria.addFilter(TaskCenterHistoryReportData.FILTER_LAST_NAME, tcform.getLastName());
		}

		if(!tcform.getSsn().isEmpty()) {
			filterAttributeSet = true;
	        criteria.addFilter(TaskCenterHistoryReportData.FILTER_SSN, tcform.getSsn().toString());
		}
		
		if (tcform.getDivision() !=null && tcform.getDivision().trim().length() >0 ) {
			filterAttributeSet = true;
			criteria.addFilter(TaskCenterHistoryReportData.FILTER_DIVISION, tcform.getDivision().trim());
		}
		
		if (tcform.getFromDate() != null && tcform.getFromDate().trim().length() >0 && 
		     tcform.getToDate() != null && tcform.getToDate().trim().length()>0) {
			dateRangeSet = true;
			criteria.addFilter(TaskCenterHistoryReportData.FILTER_FROM_DATE, tcform.getFromDate().trim());
			criteria.addFilter(TaskCenterHistoryReportData.FILTER_END_DATE, tcform.getToDate().trim());
		}

		// set default date range as per [8.4.2/8.4.3]
		if (filterAttributeSet && (dateRangeSet == false)) {
			criteria.addFilter(TaskCenterHistoryReportData.FILTER_FROM_DATE, getCurrentDate(-24));
			criteria.addFilter(TaskCenterHistoryReportData.FILTER_END_DATE, getCurrentDate(0));
		}
		
        if (userProfile.isInternalUser()==false) {
        	criteria.addFilter(TaskCenterHistoryReportData.FILTER_EXTERNAL_USER_VIEW, "Y"); // [5.7]
        }
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <-  populateReportCriteria");
		}
	}

	
	@RequestMapping(value ="/taskCenterHistory", params={"task=historyPrint"} , method =  {RequestMethod.GET}) 
	public String doHistoryPrint(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistoryPrint");
		}

		String forward = "historyPrint";
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doHistoryPrint");
		}

		return forwards.get(forward);
	}
	@RequestMapping(value ="/taskCenterHistory", params={"task=download"} , method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}

		String forward = super.doDownload(actionForm, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterHistory", params={"task=sort"} , method =  {RequestMethod.GET}) 
	public String doSort(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSort");
		}

		String forward = super.doSort(actionForm, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSort");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterHistory", params={"task=filter"} , method =  {RequestMethod.GET}) 
	public String doFilter(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doFilter");
		}

		String forward = super.doFilter(actionForm, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doFilter");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterHistory", params={"task=print"} , method =  {RequestMethod.GET}) 
	public String doPrint(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrint");
		}

		String forward = super.doPrint(actionForm, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPrint");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterHistory", params={"task=page"} , method =  {RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPage");
		}

		String forward = super.doPage(actionForm, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPage");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/taskCenterHistory", params={"task=save"} , method =  {RequestMethod.GET}) 
	public String doSave(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       	populateReportForm(actionForm, request);
			TaskCenterHistoryReportData reportData = actionForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSave");
		}

		String forward =  "save";
		request.setAttribute(Constants.REPORT_BEAN, ((TaskCenterHistoryReportForm)actionForm).getReport()); // make jsp happy
		
		Iterator<TaskCenterHistoryDetails> it = ((TaskCenterHistoryReportForm) actionForm).getDetailsList().iterator();
		Boolean print = Boolean.FALSE;
		while(it.hasNext()) {
			TaskCenterHistoryDetails detailsItem = it.next();
			if (detailsItem.isPrint()) {
				print = Boolean.TRUE;
				request.setAttribute("openPrintWindow", "true"); // signal jsp to open the print page
				break; // found one
			}
		}
		
		request.setAttribute("enablePrint", print);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSave");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	
	
	// get the current date( mm/dd/yyyy format) with # of months offset by arg
	private String getCurrentDate(int monthOffset) {
		Calendar cal = Calendar.getInstance();
		if (monthOffset != 0) cal.add(Calendar.MONTH, monthOffset);
		return SIMPLE_DATE_FORMAT.format(cal.getTime());
	}
	
	
	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TaskCenterHistoryReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TaskCenterHistoryReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "lastName";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	
	// as per [8.6.1]
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

		TaskCenterHistoryReportForm tcForm = (TaskCenterHistoryReportForm)reportForm;
		List<TaskCenterHistoryDetails> detailsList = tcForm.getDetailsList();
		UserProfile user = getUserProfile(request);
		ProtectedStringBuffer buffer = new ProtectedStringBuffer();
		
		buffer.append("Contract number").append(COMMA).append(tcForm.getContractNumber()).append(LINE_BREAK);
		buffer.append("Download Date").append(COMMA).append(SIMPLE_DATE_FORMAT.format(System.currentTimeMillis())).append(LINE_BREAK);
		buffer.append("# of Requests").append(COMMA).append(detailsList.size()).append(LINE_BREAK);
		buffer.append("Last Name").append(COMMA).append(tcForm.getLastName()).append(LINE_BREAK);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, user.getCurrentContract().getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        } 
        if (!tcForm.getSsn().isEmpty())
		buffer.append("SSN").append(COMMA).append(SSNRender.format(tcForm.getSsn().toString(), null, maskSsnFlag)).append(LINE_BREAK);
        else
    		buffer.append("SSN").append(COMMA).append("").append(LINE_BREAK);	
        buffer.append(LINE_BREAK);
		
		buffer.append("Last Name,First Name,Middle Initial,SSN, ");
		if (tcForm.hasPayrollNumberFeature()) {
			buffer.append("Employee Identification Number,");
		}
		if (tcForm.hasDivisionFeature()) {
			buffer.append("Division,");
		}
        buffer.append("Type,Initiated,Action Taken,Details,Remarks");
	    
		buffer.append(LINE_BREAK);
		
		String autoOrSignup = ContractServiceDelegate.getInstance().determineSignUpMethod(user.getCurrentContract().getContractNumber());
		
		for(TaskCenterHistoryDetails details : detailsList) {
			buffer.append(details.getLastName()).append(COMMA);
			buffer.append(details.getFirstName()).append(COMMA);
			buffer.append(getNotNull(details.getMiddleInitial())).append(COMMA);
			if(details.getSSNRaw() !=null && details.getSSNRaw().length()>0)
				
				buffer.append(SSNRender.format(details.getSSNRaw(), "", maskSsnFlag)).append(COMMA);
			else
		          buffer.append("").append(COMMA);
			if (tcForm.hasPayrollNumberFeature()) {
				buffer.append(details.getEmployeeId()).append(COMMA);
			}
			if (tcForm.hasDivisionFeature()) {
				buffer.append(details.getDivision()).append(COMMA);
			}
			buffer.append(details.getType(autoOrSignup)).append(COMMA);
			buffer.append(details.getInitiatedForDownload()).append(COMMA);
			buffer.append(details.getActionDateForDownload()).append(COMMA);
			buffer.append(details.getDetailsForDownload()).append(COMMA);
			buffer.append(escapeField(details.getRemarks()));
			
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
	
	
	// don't want excel to think the , is the next field
	private String escapeField(String field) {
		if(field.indexOf(",") != -1 ) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else 	{
			return field;
		}
	}
	
	
	// [8.4.1] if no criteria, don't do lookup
	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportData bean = null;
		if (reportCriteria.getFilters().size() > 1) {
			ReportServiceDelegate service = ReportServiceDelegate.getInstance();
			bean = service.getReportData(reportCriteria);
		} else {
			bean = new TaskCenterHistoryReportData(reportCriteria, 0);
			bean.setDetails(EMPTY_LIST);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;
	}
	 
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		 
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		String forward = super.doCommon( reportForm, request, response);
		TaskCenterHistoryReportForm form = (TaskCenterHistoryReportForm)reportForm;

		TaskCenterHistoryReportData report = (TaskCenterHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);
//        form.storeClonedForm();
		
		UserProfile userProfile = getUserProfile(request);
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
        		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
	
		request.setAttribute("enablePrint", Boolean.FALSE);
		
		return forward;
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

	
	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired TaskCenterHistoryReportValidator taskCenterHistoryReportValidator;

	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(taskCenterHistoryReportValidator);
	}
	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value ="/taskCenterHistory",method =  {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	       }
	       populateReportForm(form, request);
			TaskCenterHistoryReportData reportData = form.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.setAttribute("enablePrint", Boolean.FALSE);
			return forwards.get("input");//if input forward not //available, provided default
	       }
		String forward=super.doDefault(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/taskCenterHistory",method =  {RequestMethod.POST})
	public String execute(@Valid @ModelAttribute("taskCenterHistoryReportForm") TaskCenterHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

}
