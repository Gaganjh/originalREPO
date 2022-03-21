package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.EligibilityValidationErrors;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

 /**
  * This action handles the creation of the DeferralReport page. It will
  * also create the deferral summary download.
  *
  * @author patuadr
  */
@Controller
@RequestMapping( value = "/census")
@SessionAttributes({"deferralReportForm"})

 public final class DeferralReportController extends ReportController {
	@ModelAttribute("deferralReportForm") 
	public  DeferralReportForm populateForm() 
	{
		return new  DeferralReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/census/deferralReport.jsp"); 
		forwards.put("default","/census/deferralReport.jsp");
		forwards.put("sort","/census/deferralReport.jsp"); 
		forwards.put("filter","/census/deferralReport.jsp");
		forwards.put("page","/census/deferralReport.jsp"); 
		forwards.put("print","/census/deferralReport.jsp");
		forwards.put("save","/census/deferralReport.jsp");
		forwards.put("continue","/census/deferralReport.jsp");
		forwards.put("reset","/census/deferralReport.jsp");}
	
     public static final String DEFERRAL_PAGE_REPORT = "pageReport";
     public static final String REPORT_TYPE = "reportType";
     public final static String DATE_PATTERN = "MMM/dd/yyyy";
     public final static String SOURCE_FUNCTION_CODE = "ACI";
     public final static String EVENT_TYPE_CODE = "UNS";
     private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("###.###");
     
     protected static EmployeeServiceDelegate serviceFacade = EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
     // Keeps a map for each execution thread with employees with validation errors
     protected static ThreadLocal<HashMap<String, DeferralDetails>> employeesWithValidationErrors = new ThreadLocal<HashMap<String, DeferralDetails>>() {
         protected synchronized HashMap<String, DeferralDetails> initialValue() {
            return new HashMap<String, DeferralDetails>();
        }
    };
    public synchronized String formatPercentageFormatter(Double value) {
        return PERCENTAGE_FORMATTER.format(value);
    }


 	/**
	 * Constructor for EmployeeEnrollmentSummaryReportAction
	 */
	public DeferralReportController() {
		super(DeferralReportController.class);
	}
	
	@RequestMapping(value ="/deferral", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward=super.doDefault( form, request, response);
		
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/deferral", params = {"task=page"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward=super.doPage( form, request, response);
		
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	
	
	
	@RequestMapping(value ="/deferral", params = {"task=download"},method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward=super.doDownload( form, request, response);
		
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/deferral", params = {"task=print"},method =  {RequestMethod.GET}) 
	public String doPrint(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward=super.doPrint( form, request, response);
		
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	
	@RequestMapping(value ="/deferral", params = {"task=sort"},method =  {RequestMethod.GET}) 
	public String doSort(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward=super.doSort( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/deferral", params = {"task=filter"},method =  {RequestMethod.POST}) 
	public String doFilter (@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		//validate(form, request);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		//form.setSegment("");
		String forward=super.doFilter( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }

	@RequestMapping(value ="/deferral", params={"task=save"} , method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=null;
		if(bindingResult.hasErrors()){
			DeferralReportData reportData = new DeferralReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
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
            
            forward= doCommon( form, request,response);
        } else {
            form.clear( request);
            forward= doCommon( form, request,response);
        }

        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

	 
	@SuppressWarnings("unchecked")
 	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		DeferralReportForm form = (DeferralReportForm)reportForm;

		
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        
        if (currentContract == null || !isAllowedPageAccess(userProfile, currentContract)) {
            return forwards.get("homePageFinder");
        }
        
        if (DOWNLOAD_TASK.equals(getTask(request))) {
            
            FunctionalLogger.INSTANCE.log("Download Deferral Report", userProfile, getClass(), getMethodName( form, request));
            
        } else {
            
            FunctionalLogger.INSTANCE.log("Deferrals Page", userProfile, getClass(), getMethodName( form, request));
            
        }
        
        int contractId = userProfile.getCurrentContract().getContractNumber();
        
        // set permission flag for eligibility tab
        boolean isEZstartOn = CensusUtils.isAutoEnrollmentEnabled(contractId);
        boolean isEnabled = userProfile.isInternalUser() || isEZstartOn;
        
        form.setAllowedToAccessEligibTab(isEnabled);
        form.setEzStartOn(isEZstartOn);
        
        // set permission flag for vesting tab
        form.setAllowedToAccessVestingTab(
                CensusUtils.isVestingEnabled(contractId) && 
                !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                TODO &&
                userProfile.getCurrentContract().isContractAllocated()*/);

        form.setTermsAndConditionsAccepted(getTermsAndConditionsAccepted(request));
        if(!getTask(request).equalsIgnoreCase("reset")){
        String segment="segment";
        String segmentform = request.getParameter(segment);
        form.setSegment(segmentform);
        }
        
        try {
            DeferralUtils.setContractServiceFeatureValues(
                    contractId,
                    form);
        } catch (Exception e) {
            // Assume AE off and ACI off
            form.setAutoEnrollmentEnabled(false);
            form.setACIOff(true);
            logger.error("Contract Service delegate exception", e);
        }

        String forward = super.doCommon( form, request,response);

		//get the Report object
        DeferralReportData report = (DeferralReportData)request.getAttribute(Constants.REPORT_BEAN);

        if(report != null && report.getTotalCount() > 0) {
    		//get the report details
    		List<DeferralDetails> details = (List<DeferralDetails>)report.getDetails();

    		// As long as there are details, determine if some columns should be hidden
    		if (details.size() > 0) {
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

    		// Don't want to clone or apply business rules if the action is download report or show
            // print friendly
            if (!PRINT_TASK.equals(getTask(request)) && !DOWNLOAD_TASK.equals(getTask(request))) {
                applyBusinessRulesForValidationErrors(details);
            }

            // Sort the report list only if there are errors
            // and put the form items in the same order so they match
            // with the IDs for track changes in the JSP
            if(hasErrors(request)) {
                sortReportItemsByErrorsAndWarnings(details, form, request);
            }

            // Calculate just if there are no errors
            if(!hasErrors(request)) {
                form.setTheItemList(details);
                form.applyBusinessRulesForDerivedData();
            }            
                     
            form.setLoadedOnce(true);
            // Test if there are errors and it is still the first search
            if(hasErrors(request) && form.isInitialSearch()) {
                form.setInitialSearch(true);
            } else if(form.isLoadedOnce()) { // Check if the page has been displayed before or is the first call
                form.setInitialSearch(false);
            } else {
                form.setInitialSearch(true);
            }

            
            
            // Don't want to clone if the action is download report or show
            // print friendly
            if (!PRINT_TASK.equals(getTask(request)) && !DOWNLOAD_TASK.equals(getTask(request))) {
                // Add the latest list to the form
                if(!hasErrors(request)) {
                    form.storeClonedForm();
                }
            }
        }

        addAttributes(request, form);

		// The map is cleared every time this execution path is initiated
        employeesWithValidationErrors.get().clear();

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}
 	
 	@Override
 	protected void populateReportForm( 
            BaseReportForm reportForm, HttpServletRequest request) {
 		
 		String task = getTask(request);
 		
        //Get the filterCriteriaVo object from session
        FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
 		
		if(filterCriteriaVo == null){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
 		if(task.equals(DEFAULT_TASK)){
 			
 			//If it is a initial search, sort by warning
 			if (filterCriteriaVo.getDeferralSortDirection() == null
					&& filterCriteriaVo.getDeferralSortField() == null) {

 				reportForm.setSortDirection(getDefaultSortDirection());
 				reportForm.setSortField(getDefaultSort());
			}else {
				
				// Else load the cached data
				reportForm.setSortDirection(filterCriteriaVo.getDeferralSortDirection());
 				reportForm.setSortField(filterCriteriaVo.getDeferralSortField());
			}
 			
 			reportForm.setPageNumber(filterCriteriaVo.getDeferralPageNumber());
 		} 
 		
 		if (task.equals(RESET_TASK)) {
			reportForm.setSortDirection(getDefaultSortDirection());
			reportForm.setSortField(getDefaultSort());
		}
 		
 		//If the task is sort task then cache the sorting details
 		if(task.equals(SORT_TASK)){
 			reportForm.setPageNumber(1);
 			filterCriteriaVo.setDeferralSortDirection(reportForm.getSortDirection());
 			filterCriteriaVo.setDeferralSortField(reportForm.getSortField());
 		}
 		
 		// If the task is page task then cache the page number
 		if (task.equals(PAGE_TASK)){
 			filterCriteriaVo.setDeferralPageNumber(reportForm.getPageNumber());
 		}
 		
 		// If the task is filter task then use the default sort criteria
		if (task.equals(FILTER_TASK)) {
			reportForm.setPageNumber(1);
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		
		//If the task is print task then set the page number as 1
		if (task.equals(PRINT_TASK) || task.equals(DOWNLOAD_TASK)) {
			reportForm.setPageNumber(1);
		}
		
		//Place the object back in session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
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
 	@RequestMapping(value ="/deferral", params={"task=reset"} , method =  {RequestMethod.POST}) 
	public String doReset(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
 		if(bindingResult.hasErrors()){
 			DeferralReportData reportData = new DeferralReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
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
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
 	
 	// ACI2, did user accept use(confirmed message) yet or not?
 	private boolean getTermsAndConditionsAccepted(HttpServletRequest request) throws SystemException {
        UserProfile userProfile = getUserProfile(request);
        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();    
        UserInfo userInfo = service.getUserInfo(userProfile.getPrincipal());
        
        return service.checkUserProfileDisclaimerTS(userInfo);
 	}
 	
 	@RequestMapping(value ="/deferral", params={"task=continue"} , method =  {RequestMethod.POST}) 
	public String doContinue(@Valid @ModelAttribute("deferralReportForm") DeferralReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
 		if(bindingResult.hasErrors()){
 			DeferralReportData reportData = new DeferralReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
 	
 		// update user with continue date.
        UserProfile userProfile = getUserProfile(request);
        SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();    
        UserInfo userInfo = service.getUserInfo(userProfile.getPrincipal());
        
        service.updateUserDisclaimerTS(userProfile.getPrincipal(), userInfo);
        
 		String forward= doCommon( form, request,response); // go back to page
 		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	}
 	
 	
    /**
     * Utility method that checks validation errors for each element and sets them
     * in the report list
     *
     * @param details
     */
    private void applyBusinessRulesForValidationErrors(List<DeferralDetails> details) {
        for (DeferralDetails element:details) {
            DeferralDetails employee = employeesWithValidationErrors.get().get(element.getProfileId());

            if(employee != null) {
                element.setAutoIncreaseFlagStatus(employee.getAutoIncreaseFlagStatus());
                element.setNextADIYearStatus(employee.getNextADIYearStatus());
                element.setTypeStatus(employee.getTypeStatus());
                element.setIncreaseStatus(employee.getIncreaseStatus());
                element.setLimitStatus(employee.getLimitStatus());
                element.setWarnings(employee.getWarnings());
                element.setIncreaseType(employee.getIncreaseType());
                element.setErEeLimitMessage(employee.getErEeLimitMessage());
                element.setAutoIncreaseChanged(employee.isAutoIncreaseChanged());
                element.setIncrease(employee.getIncrease());
                element.setLimit(employee.getLimit());
                element.setAciSettingsInd(employee.getAciSettingsInd());
                element.setDateNextADI(employee.getDateNextADI());
                element.setNextADIYear(employee.getNextADIYear());
                element.setNextAD(employee.getNextAD());
                element.setNextADIMonthDay(employee.getNextADIMonthDay());                
            }
        }
    }

    /**
     * Break the natural sort and keep the errors and alerts at the top of the list
     *
     * From the report data create a new list with elements with errors at the top,
     * followed by alerts and clean elements at the end
     *
     * The items from the form should be reordered using the same order as in the previous list,
     * because on the screen, data is populated from the form elements for the elements that are
     * edited and in process of saving, but they haven't been saved because of validation errors
     *
     * @param details - the list retrieved from the database
     * @param form - the action form that contains a list with elements similar with the ones from
     *              database but the editable fields might contain different data
     * @param request
     */
    private void sortReportItemsByErrorsAndWarnings(List<DeferralDetails> details,
            DeferralReportForm form,
            HttpServletRequest request) {
        List<DeferralDetails> elementsWithErrors = new ArrayList<DeferralDetails>();
        List<DeferralDetails> elementsWithAlerts = new ArrayList<DeferralDetails>();
        List<DeferralDetails> elementsWithWarnings = new ArrayList<DeferralDetails>();
        List<DeferralDetails> cleanElements = new ArrayList<DeferralDetails>();

        for (DeferralDetails element : details) {
            // Errors take precedence
            if(element.hasErrors()) {
                elementsWithErrors.add(element);
            } else if(element.hasAlert()) {
                elementsWithAlerts.add(element);
            } else if(element.hasWarnings()) {
                elementsWithWarnings.add(element); 
            } else {
                cleanElements.add(element);
            }
        }

        // Create the reordered list of employees starting with errors,
        // followed by warnings, alerts and clean elements and sort them
        // by last name, first name and middle initial in this order
        Collections.sort(elementsWithErrors, new DeferralDetails.EmployeeComparator());
        Collections.sort(elementsWithWarnings, new DeferralDetails.EmployeeComparator());
        Collections.sort(elementsWithAlerts, new DeferralDetails.EmployeeComparator());

        elementsWithErrors.addAll(elementsWithAlerts);
        elementsWithErrors.addAll(elementsWithWarnings);
        elementsWithErrors.addAll(cleanElements);

        details.clear();
        details.addAll(elementsWithErrors);
        form.setTheItemList(details);
        form.applyBusinessRulesForDerivedData();

        List<DeferralDetails> formElements = new ArrayList<DeferralDetails>();

        for (DeferralDetails element : details) {
            for (DeferralDetails formItem : form.getTheItemList()) {
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
     * Utility method to populate the request
     *
     * @param request
     * @throws SystemException
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    private void addAttributes(
            HttpServletRequest request,
            DeferralReportForm form) throws SystemException
    {

        UserProfile userProfile = getUserProfile(request);

        // Populate list of segments for the dropdown
        form.setSegmentList(CensusLookups.getInstance().getSegments());

        // Set permission flag for editing
        form.setAllowedToEdit(userProfile.isAllowedUpdateCensusData()
                && !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract()
                        .getStatus()));

        // If external user, do not display Cancelled status in the dropdown
        List employeeStatusList = null;
        if (userProfile.isInternalUser()) {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatuses();
        } else {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatusesWithoutC();
        }
        form.setStatusList(employeeStatusList);

        form.setRoth(getUserProfile(request).getCurrentContract().hasRoth());

        // Set permission flag for download census report
       
        form.setAllowedToDownloadCensus(
                (userProfile.isInternalUser() &&
                        userProfile.isAllowedUpdateCensusData()) ||
                (userProfile.getRole().isExternalUser()));
//      SSE S024 allow to download report, but mask the ssn if required
                // && userProfile.isAllowedDownloadReport()));
        
//      SSE S024 allow to download report, but mask the ssn if required
        form.setAllowedToDownload(userProfile.isInternalUser() ||
       userProfile.getRole().isPlanSponsor() 
    		   //&& userProfile.isAllowedDownloadReport())
       ||(userProfile.getRole().isExternalUser())); 
    	//&& userProfile.isAllowedDownloadReport()));
    }

 	/**
	 * Validate the input form. The search field must not be empty.
	 *
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
    
    @Autowired 
    private DeferralReportValidator deferralReportValidator;
    
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
     @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(deferralReportValidator);
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
    protected List<ValidationError> validateAndUpdateList(DeferralReportForm reportForm, HttpServletRequest request) throws SystemException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        List<ValidationError> errorsPerEmployee = new ArrayList<ValidationError>();
        Employee employee = null;
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        DeferralReportForm clonedForm = (DeferralReportForm)reportForm.getClonedForm();
        List<DeferralDetails> listFromClone = clonedForm.getTheItemList();

        HashMap<String, DeferralDetails> initialMap = createInitialHash(listFromClone);

        // Clear the list of employees with errors
        // The map is cleared every time this execution path is initiated
        employeesWithValidationErrors.get().clear();

        for (DeferralDetails element : reportForm.getTheItemList()) {
        	elementAdjust(element);
        	
            if(employeeNeedsUpdate(element, initialMap)) {
                if (reportForm.validate(errorsPerEmployee, element)) {
                    employee = serviceFacade.getEmployeeByProfileId(Long.parseLong(element.getProfileId()),
                            userProfile.getCurrentContract().getContractNumber(), null);
                    reportForm.updateEmployee(employee, element,
                            initialMap.get(element.getProfileId()), getUserProfile(request));
                    serviceFacade.updateEmployee(employee, EVENT_TYPE_CODE,  SOURCE_FUNCTION_CODE);
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

     // GUI does a setup of the year so that if the user turns it on, you get the year.
     // code later things this was a user change, even though element was disabled. Adjust
     // it back so code does not think the element changed.
     private void elementAdjust(DeferralDetails theElement) {
    	 if (theElement.getAciSettingsInd() != null && theElement.getAciSettingsInd().trim().length() == 0) {
    		 theElement.setNextADIYear(null);
    	 }
     }
    

    /**
     * Populate sort criteria in the criteria object using the given FORM. The
     * default implementation inserts the FORM's sort field and sort direction.
     *
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     * @throws SystemException 
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        // Bring the warnings first if the page is first loaded
    	if(DeferralReportData.DEFAULT_SORT.equals(form.getSortField())) {
    	   	criteria.insertSort(DeferralReportData.ALERT_FIELD, ReportSort.DESC_DIRECTION);
            criteria.insertSort(DeferralReportData.WARNING_FIELD, ReportSort.DESC_DIRECTION);
            
            criteria.insertSort("lastName", form.getSortDirection());            
        } else {
	        if (form.getSortField() != null) {
	            criteria.insertSort(form.getSortField(), form.getSortDirection());
	            if (!form.getSortField().equals(getDefaultSort())) {
	                criteria.insertSort("lastName", getDefaultSortDirection());
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
    private HashMap<String, DeferralDetails> createInitialHash(List<DeferralDetails> initialList) {
        HashMap<String, DeferralDetails> initialMap =  new HashMap<String, DeferralDetails>();

        for (DeferralDetails element : initialList) {
            initialMap.put(element.getProfileId(), element);
        }

        return initialMap;
    }

    /**
     * Checks if the details changed
     * <code>DeferralDetails</code> equals and hashCode are implemented to take into
     * consideration just the editable properties from <code>DeferralDetails</code>
     *
     * @param currentDetails
     * @param initialList
     * @return
     */
    private boolean employeeNeedsUpdate(DeferralDetails currentDetails, HashMap<String, DeferralDetails> initialMap) {

        if(currentDetails == null || 
           initialMap.get(currentDetails.getProfileId()) == null || 
           currentDetails.equals(initialMap.get(currentDetails.getProfileId()))){
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

    @Override
    protected String getDefaultSort() {
        return DeferralReportData.DEFAULT_SORT;
    }

    @Override
    protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;
    }

    @Override
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {
        byte[] bytes = null;

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData");
        }

        // Identify the type of report
        if(PAGE_TASK.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
            bytes = getPageDownloadData(reportForm, report, request);
        } else {
            bytes = getAllDownloadData(reportForm, report, request);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDownloadData");
        }

        return bytes;

    }

    protected byte[] getPageDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPageDownloadData");
        }

        DeferralReportData summaryReport = (DeferralReportData)request.getAttribute(Constants.REPORT_BEAN);
        DeferralReportForm form = (DeferralReportForm)reportForm;
        StringBuffer buffer = new StringBuffer();

        // Display number of employees
        int numberOfEmployees = summaryReport.getTotalCount();

        Contract currentContract = getUserProfile(request).getCurrentContract();


        // Title
        buffer.append("Download Deferral Report").append(LINE_BREAK);
        // Contract #, Contract name
        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);
        buffer.append("Actual Date of Download").append(COMMA).append(
                DateRender.formatByPattern(new Date(), "", DATE_PATTERN)).append(LINE_BREAK);

        StringBuffer planLimitBuffer = new StringBuffer();

        if(Constants.DEFERRAL_TYPE_PERCENT.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Plan limit is ");
            planLimitBuffer.append(form.getPlanLimitPercent());
            planLimitBuffer.append("%");
        } else if(Constants.DEFERRAL_TYPE_DOLLAR.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Plan limit is ");
            planLimitBuffer.append("$");
            planLimitBuffer.append(form.getPlanLimitAmount());
        } else if(Constants.DEFERRAL_TYPE_EITHER.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Your plan max is ");
            planLimitBuffer.append(form.getPlanLimitPercent());
            planLimitBuffer.append("%");
            planLimitBuffer.append("or $");
            planLimitBuffer.append(form.getPlanLimitAmount());
        } else {
            planLimitBuffer.append("Not on file");
        }

        buffer.append(planLimitBuffer.toString()).append(LINE_BREAK);
        buffer.append(DeferralUtils.getDefaultDeferralLimitMessage(form)).append(LINE_BREAK);
        buffer.append(DeferralUtils.getDefaultDeferralIncreaseMessage(form)).append(LINE_BREAK);

        buffer.append("Total Number of Employees on file:  ").append(numberOfEmployees).append(LINE_BREAK);

        buffer.append(LINE_BREAK);
        // Column headings
        buffer.append("Employee Last Name").append(COMMA)
        .append("Employee First Name").append(COMMA)
        .append("Employee Middle Initial").append(COMMA)
        .append("SSN").append(COMMA);

        if(form.getHasPayrollNumberFeature()) {
            buffer.append("Employee Identification Number").append(COMMA);
        }
        if(form.getHasDivisionFeature()) {
            buffer.append("Division").append(COMMA);
        }

        if(currentContract.hasRoth()) {
            buffer
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral").append(COMMA)
            .append("Designated Roth 401(k) deferral (%)").append(COMMA)
            .append("Designated Roth Flat 401(k) flat ($) deferral").append(COMMA);
        } else {
            buffer
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral").append(COMMA);
        }
        
        buffer.append("Deferral last update date").append(COMMA);
        
        if(!form.isACIOff()) {
            buffer
            .append("Scheduled deferral increase").append(COMMA)
            .append("Date of next increase").append(COMMA)
            .append("EE or ER limit reached").append(COMMA)
            .append("Type").append(COMMA)
            .append("Increase amount").append(COMMA)
            .append("Limit").append(COMMA);
        }
        
        buffer.append("Alert text").append(COMMA);
        buffer.append("Alert/Warning").append(COMMA);

        buffer.append(LINE_BREAK);
        UserProfile user = getUserProfile(request);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        } 
        // Loop through details
        for (DeferralDetails theItem:form.getTheItemList()) {
            buffer.append(LINE_BREAK);

            buffer.append(processString(theItem.getLastName())).append(COMMA);
            buffer.append(processString(theItem.getFirstName())).append(COMMA);
            buffer.append(processString(theItem.getMiddleInitial())).append(COMMA);          
            buffer.append(SSNRender.format(theItem.getSsn(),null, maskSsnFlag)).append(COMMA);

            if(form.getHasPayrollNumberFeature()) {
                buffer.append(processString(theItem.getEmployerDesignatedID()));
                buffer.append(COMMA);
            }

            if(form.getHasDivisionFeature()) {
                buffer.append(processString(theItem.getDivision()));
                buffer.append(COMMA);
            }

            Double beforeTaxPct = null;
            Double beforeTaxAmt = null;
            Double rothTaxPct = null;
            Double rothTaxAmt = null;

            if(currentContract.hasRoth()) {
                try {
                    beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                            new Double(theItem.getBeforeTaxDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                            new Double(theItem.getBeforeTaxDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    rothTaxPct = theItem.getDesignatedRothDeferralPct() != null?
                            new Double(theItem.getDesignatedRothDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    rothTaxAmt = theItem.getDesignatedRothDeferralAmt()!= null?
                            new Double(theItem.getDesignatedRothDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }

                if(beforeTaxPct != null) {
                    beforeTaxPct = beforeTaxPct*100;
                    buffer.append(formatPercentageFormatter(beforeTaxPct));
                }
                buffer.append(COMMA);
                if(beforeTaxAmt != null) {
                    buffer.append(beforeTaxAmt);
                }
                buffer.append(COMMA);
                if(rothTaxPct != null) {
                    rothTaxPct = rothTaxPct*100;
                    buffer.append(formatPercentageFormatter(rothTaxPct));
                }
                buffer.append(COMMA);
                if(rothTaxAmt != null) {
                    buffer.append(rothTaxAmt);
                }
                buffer.append(COMMA);
            } else {
                try {
                    beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                            new Double(theItem.getBeforeTaxDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                            new Double(theItem.getBeforeTaxDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }

                if(beforeTaxPct != null) {
                	beforeTaxPct = beforeTaxPct*100;
                    buffer.append(formatPercentageFormatter(beforeTaxPct));
                }
                buffer.append(COMMA);
                if(beforeTaxAmt != null) {
                    buffer.append(beforeTaxAmt);
                }
                buffer.append(COMMA);
            }

            buffer.append(theItem.getLastDeferralUpdatedTs()).append(COMMA);
            
            if(!form.isACIOff()) {
                buffer.append(processString(theItem.getAciSettingsInd())).append(" ");
                buffer.append(processString(theItem.getAutoIncreaseLimitAlert())).append(COMMA);

                buffer.append(DateRender.formatByPattern(theItem.getDateNextADI(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(processString(theItem.getErEeLimitMessage())).append(COMMA);
                buffer.append(processString(theItem.getIncreaseType())).append(COMMA);
                buffer.append(processString(theItem.getIncrease())).append(COMMA);
                buffer.append(processString(theItem.getLimit())).append(COMMA);
            }

            if(theItem.hasAlert()) {
                buffer.append("Click here to go to task center").append(COMMA);
            } else {
                buffer.append(COMMA);
            }
            
            if(theItem.hasAlert() || theItem.hasWarnings()) {
                buffer.append(Constants.YES).append(COMMA);
            } else {
                buffer.append(COMMA);
            }
        }


        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getPageDownloadData");
        }

        return buffer.toString().getBytes();
    }


    protected byte[] getAllDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPageDownloadData");
        }

        DeferralReportData summaryReport = (DeferralReportData)request.getAttribute(Constants.REPORT_BEAN);
        DeferralReportForm form = (DeferralReportForm)reportForm;
        StringBuffer buffer = new StringBuffer();

        // Display number of employees
        int numberOfEmployees = summaryReport.getTotalCount();

        Contract currentContract = getUserProfile(request).getCurrentContract();


        // Title
        buffer.append("Download Report").append(LINE_BREAK);
        // Contract #, Contract name
        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);
        buffer.append("Actual Date of Download").append(COMMA).append(
                DateRender.formatByPattern(new Date(), "", DATE_PATTERN)).append(LINE_BREAK);

        StringBuffer planLimitBuffer = new StringBuffer();

        if(Constants.DEFERRAL_TYPE_PERCENT.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Plan Deferral limit max is ");
            planLimitBuffer.append(form.getPlanLimitPercent());
            planLimitBuffer.append("%");
        } else if(Constants.DEFERRAL_TYPE_DOLLAR.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Plan Deferral limit max is ");
            planLimitBuffer.append("$");
            planLimitBuffer.append(form.getPlanLimitAmount());
        } else if(Constants.DEFERRAL_TYPE_EITHER.equals(form.getPlanDeferralType())) {
            planLimitBuffer.append("Plan Deferral limit max is ");
            planLimitBuffer.append(form.getPlanLimitPercent());
            planLimitBuffer.append("%");
            planLimitBuffer.append("or $");
            planLimitBuffer.append(form.getPlanLimitAmount());
        } else {
            planLimitBuffer.append("Not on file");
        }

        buffer.append(planLimitBuffer.toString()).append(LINE_BREAK);
        buffer.append(DeferralUtils.getDefaultDeferralLimitMessage(form)).append(LINE_BREAK);
        buffer.append(DeferralUtils.getDefaultDeferralIncreaseMessage(form)).append(LINE_BREAK);

        buffer.append("Total Number of Employees on file:  ").append(numberOfEmployees).append(LINE_BREAK);

        buffer.append(LINE_BREAK);
        // Column headings
        buffer.append("Employee Last Name").append(COMMA)
        .append("Employee First Name").append(COMMA)
        .append("Employee Middle Initial").append(COMMA)
        .append("SSN").append(COMMA);

        if(form.getHasPayrollNumberFeature()) {
            buffer.append("Employee Identification Number").append(COMMA);
        }
        if(form.getHasDivisionFeature()) {
            buffer.append("Division").append(COMMA);
        }

        if(currentContract.hasRoth()) {
            buffer
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral").append(COMMA)
            .append("Designated Roth 401(k) deferral (%)").append(COMMA)
            .append("Designated Roth Flat 401(k) flat ($) deferral").append(COMMA);
        } else {
            buffer
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral").append(COMMA);
        }
        buffer.append("Deferral last update date").append(COMMA);
        
        if(!form.isACIOff()) {
        	
        	String serviceName = StringUtils.EMPTY;
        	if(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(form.getAciSignupMethod())
        			|| ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod())) {
        		serviceName = "Scheduled deferral increase";
        	}
        	
            buffer
            .append(serviceName).append(COMMA)
            .append("Date of next increase").append(COMMA)
            .append("EE or ER limit reached").append(COMMA)
            .append("Type").append(COMMA)
            .append("Increase amount").append(COMMA)
            .append("Limit").append(COMMA);
        }
        
        buffer.append("Alert text").append(COMMA);
        buffer.append("Alert/Warning").append(COMMA);

        buffer.append(LINE_BREAK);
        UserProfile user = getUserProfile(request);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        } 
        // Loop through details
        for (DeferralDetails theItem:form.getTheItemList()) {
            buffer.append(LINE_BREAK);

            buffer.append(processString(theItem.getLastName())).append(COMMA);
            buffer.append(processString(theItem.getFirstName())).append(COMMA);
            buffer.append(processString(theItem.getMiddleInitial())).append(COMMA);
            buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);

            if(form.getHasPayrollNumberFeature()) {
                buffer.append(processString(theItem.getEmployerDesignatedID()));
                buffer.append(COMMA);
            }

            if(form.getHasDivisionFeature()) {
                buffer.append(processString(theItem.getDivision()));
                buffer.append(COMMA);
            }

            Double beforeTaxPct = null;
            Double beforeTaxAmt = null;
            Double rothTaxPct = null;
            Double rothTaxAmt = null;

            if(currentContract.hasRoth()) {
                try {
                    beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                            new Double(theItem.getBeforeTaxDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                            new Double(theItem.getBeforeTaxDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    rothTaxPct = theItem.getDesignatedRothDeferralPct() != null?
                            new Double(theItem.getDesignatedRothDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    rothTaxAmt = theItem.getDesignatedRothDeferralAmt()!= null?
                            new Double(theItem.getDesignatedRothDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }

                if(beforeTaxPct != null) {
                    beforeTaxPct = beforeTaxPct*100;
                    buffer.append(formatPercentageFormatter(beforeTaxPct));
                }
                buffer.append(COMMA);
                if(beforeTaxAmt != null) {
                    buffer.append(beforeTaxAmt);
                }
                buffer.append(COMMA);
                if(rothTaxPct != null) {
                    rothTaxPct = rothTaxPct*100;
                    buffer.append(formatPercentageFormatter(rothTaxPct));
                }
                buffer.append(COMMA);
                if(rothTaxAmt != null) {
                    buffer.append(rothTaxAmt);
                }
                buffer.append(COMMA);
            } else {
                try {
                    beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                            new Double(theItem.getBeforeTaxDeferralPct()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }
                try {
                    beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                            new Double(theItem.getBeforeTaxDeferralAmt()):null;
                } catch (NumberFormatException e) {
                    // it should never happen, assume null
                }

                if(beforeTaxPct != null) {
                	beforeTaxPct = beforeTaxPct*100;
                    buffer.append(formatPercentageFormatter(beforeTaxPct));
                }
                buffer.append(COMMA);
                if(beforeTaxAmt != null) {
                    buffer.append(beforeTaxAmt);
                }
                buffer.append(COMMA);
            }
            
            buffer.append(theItem.getLastDeferralUpdatedTs()).append(COMMA);
            
            if(!form.isACIOff()) {
                buffer.append(processString(theItem.getAciSettingsInd())).append(" ");
                buffer.append(processString(theItem.getAutoIncreaseLimitAlert())).append(COMMA);

                buffer.append(DateRender.formatByPattern(theItem.getDateNextADI(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(processString(theItem.getErEeLimitMessage())).append(COMMA);
                buffer.append(processString(theItem.getIncreaseType())).append(COMMA);
                buffer.append(processString(theItem.getIncrease())).append(COMMA);
                buffer.append(processString(theItem.getLimit())).append(COMMA);
            }

            if(theItem.hasAlert()) {
                buffer.append("Click here to go to task center").append(COMMA);
            } else {
                buffer.append(COMMA);
            }
            
            if(theItem.hasAlert() || theItem.hasWarnings()) {
                buffer.append(Constants.YES).append(COMMA);
            } else {
                buffer.append(COMMA);
            }
        }


        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getPageDownloadData");
        }

        return buffer.toString().getBytes();
    }

    @Override
    protected String getReportId() {
        return DeferralReportData.REPORT_ID;
    }

    @Override
    protected String getReportName() {
        return null;
    }

    protected String getFileName(HttpServletRequest request) {
        String fileName = "";
        String date = "MM dd yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        date = dateFormat.format(new Date());

        fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
            "_Deferral_rprt_" + date + CSV_EXTENSION;

        // Replace spaces with underscores
        return fileName.replaceAll("\\ ", "_");
    }

    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }

        // default sort criteria
        // this is already set in the super

        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();

        criteria.addFilter(DeferralReportData.FILTER_CONTRACT_NUMBER,
                Integer.toString(currentContract.getContractNumber()));

        DeferralReportForm psform = (DeferralReportForm) form;
        
        // Get the filter object from session.
        FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
		String task = getTask(request);
		
		if(filterCriteriaVo == null ){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		if (task.equals(DEFAULT_TASK)) {
			// If the task is default then reset the page no and sort detains that are cached in eligibility tab.
			filterCriteriaVo.clearEligibilitySortDetails();
		} else if (task.equals(FILTER_TASK)) {
			// If the task is filter then reset the page no and sort details that are cached in deferral tab.
			filterCriteriaVo.clearDeferralSortDetails();
		}
		
		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateDefferelTabFilterCriteria(task, filterCriteriaVo, psform, criteria);
		
		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
      
        criteria.addFilter(DeferralReportData.FILTER_AUTO_ENROLL_ON,
                Boolean.valueOf(psform.isAutoEnrollmentEnabled()));

        if(DOWNLOAD_TASK.equals(getTask(request)) &&
                PAGE_TASK.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
            criteria.setPageSize(getPageSize(request));
        }

        criteria.setExternalUser(userProfile.getRole().isExternalUser());
        if (logger.isDebugEnabled()) {
            logger.debug("criteria= " + criteria);
            logger.debug("exit <- populateReportCriteria");
        }
        
        // Inject the isAdHocFreezeperiod into the form for displaying alert image in the deferral page.
    	// get contract number from criteria
        
        DeferralReportForm deferralForm = (DeferralReportForm)form;
		if(deferralForm.getPlanEmpDefElection() != null){
			PlanEmployeeDeferralElection planDef = deferralForm.getPlanEmpDefElection();
			if(planDef != null){				
				Date upComingChangeDate = PlanEmployeeDeferralElection.getUpComingAdHocChnageDate(deferralForm.getPlanYearEnd(), planDef.getEmployeeDeferralElectionCode(), planDef.getEmployeeDeferralElectionSelectedDay(), planDef.getEmployeeDeferralElectionSelectedMonths());
				boolean adHocFreezePeriod = PlanEmployeeDeferralElection.isAdhocChangeRequestPeriod(upComingChangeDate, deferralForm.getOptOutDays());
				criteria.setAdHocFreezePeriod(adHocFreezePeriod);
			}				
		}
    }

    /**
     * Utility method that prepares strings to be displayed
     *
     * @param field
     * @return
     */
    private String processString(String field) {
        if(field == null || "mm/dd/yyyy".equalsIgnoreCase(field)) {
            return "";
        } else {
            return field.trim().replaceAll("\\,", " ");
        }
    }
    
    /**
     * Validates whether the internal/external users have access to Deferrals page,
     * based on the contract status
     * 
     * @param userProfile
     * @param currentContract
     * @return TRUE - if the contract status is in any of these
     * 					'PS', 'PC', 'DC', 'CA', 'AC', 'CF', 'DI'
     */
    private boolean isAllowedPageAccess(UserProfile userProfile, Contract currentContract) {
        boolean canAccessPage = false;
        // Both the internal & external users can view deferrals for 
        // contract status PC, DC, PS, CA, AC, CF, DI
        if (currentContract.getStatus().equals("PS")
                || currentContract.getStatus().equals("DC")
                || currentContract.getStatus().equals("PC")
                || currentContract.getStatus().equals("AC")
                || currentContract.getStatus().equals("DI")
                || currentContract.getStatus().equals("CF")
                || currentContract.getStatus().equals("CA")) {
            // good
            canAccessPage = true;
        } else {
        	if (userProfile.isInternalUser()) {
        		logger.warn("Internal user cannot view/edit Contract status"
                        + currentContract.getStatus());	
        	} else {
        		logger.warn("External user cannot view Contract status"
                        + currentContract.getStatus());
        	}
            
        }

        return canAccessPage;
    }
 }
