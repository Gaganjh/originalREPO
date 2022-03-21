package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.employee.eligibility.constants.EmployeeEligibilityAssessmentReqConstants;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO.ChangeUserInfo;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

 /**
  * This action handles the creation of the EmployeeEnrollmentSummaryReport. It will
  * also create the employee enrollment summary download.
  *  
  * @author patuadr
  */
 public abstract class EmployeeEnrollmentSummaryBaseController extends ReportController { 
    
    public static final String NA = "N/A";
    public static final String ENGLISH = "EN";
    public static final String ENGLISH_DISPLAY = "English";
    public static final String SPANISH = "SP";
    public static final String SPANISH_DISPLAY = "Spanish";
    public static final String BLANK = "";
    public static final String NOT_ENTERED = "Not Entered";
    
    public static final String OPT_OUT_REPORT = "optOut";
    public static final String ELIGIBILITY_REPORT = "allDetails";
    public static final String REPORT_TYPE = "reportType";
    public static final String SOURCE = "source";
    public static final String ELIGIBILITY_REPORTS = "eligibilityReports";
    public static final String OPT_OUT_REPORT_NAME = "Download 'post opt out date changes' report";
    public static final String ELIGIBILITY_REPORT_NAME = "Download eligibility report";   
    public final static String JOHN_HANCOCK_REPRESENTATIVE = "John Hancock Representative";
    public final static String EMPLOYEE = "Employee";
    public final static String DATE_PATTERN = "MMM/dd/yyyy";
    public final static String PENDING_ENROLLMENT = "Pending Enrollment";
    public final static String ELIGIBILITY_REPORT_CSV = "eligibilityReport";
    public final static String OPT_OUT_REPORT_CSV = "optOutReport";
    protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private final static SimpleDateFormat SP_DATE_FORMAT = new SimpleDateFormat("MMM d yyyy");
    private final static String NO_RESULT_TEXT = "Curently there are no changes to display.";
    private final static DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("###.###");
    // Keeps a map for each execution thread with employees with validation errors
    protected static ThreadLocal<HashMap<String, EmployeeSummaryDetails>> employeesWithValidationErrors = new ThreadLocal<HashMap<String, EmployeeSummaryDetails>>() {
        protected synchronized HashMap<String, EmployeeSummaryDetails> initialValue() {
            return new HashMap<String, EmployeeSummaryDetails>();
        }
    };
    
    private synchronized String formatPercentageFormatter(Double value) {
        return PERCENTAGE_FORMATTER.format(value);
    }
    
    private static Map<String, String> SourceMap = new HashMap<String, String>();
    private static String PSDescription = "Website";
    private static String PADescription = "Participant Website";
    private static String LPDescription = "Administration";
    private static String FileSubmission = "Census File";
    
    static {
        SourceMap.put("PS", PSDescription);
        SourceMap.put("PA", PADescription);
        SourceMap.put("LP", LPDescription);
    }
    
 	/**
	 * Constructor for EmployeeEnrollmentSummaryReportAction
	 */
	public EmployeeEnrollmentSummaryBaseController(Class derivedClass) {
		super(derivedClass);
	}
 
 	/**
	 * @see ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}
    
 	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return EmployeeEnrollmentSummaryReportData.DEFAULT_SORT;
	}
    
 	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return EmployeeEnrollmentSummaryReportData.REPORT_ID;
	}
        
    
    @Override
    protected String getReportName() {
        // Not used but required by abstract base. Defaults to ELIGIBILITY_REPORT_NAME
        return ELIGIBILITY_REPORT_NAME;
    }

    protected String getFileName(HttpServletRequest request) {
        String fileName = "";
        String date = "MM dd yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        date = dateFormat.format(new Date());
        
        // Identify the type of report
        if(ELIGIBILITY_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
            /*fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
                " " + OPT_OUT_REPORT_NAME + " " + date + " " + CSV_EXTENSION;*/
        	fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
            " " + ELIGIBILITY_REPORT_NAME + " " + date + CSV_EXTENSION;
        } /*else {        
            fileName = getUserProfile(request).getCurrentContract().getContractNumber() +
                " " + ELIGIBILITY_REPORT_NAME + " " + date + " " + CSV_EXTENSION;
        }*/
        // Replace spaces with underscores
        return fileName.replaceAll("\\ ", "_");
    }
    
    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getPageSize(javax.servlet.http.HttpServletRequest)
     */
    protected int getPageSize(HttpServletRequest request) {
        UserProfile profile = getUserProfile(request);
        return profile.getPreferences()
                .getInt(UserPreferenceKeys.REPORT_PAGE_SIZE,
                        super.getPageSize(request));
    }
    
 	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form,
			HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }
        
        // If is Opt_out override the report_ID
        // The below code is commented because the opt out report is handled separately.
        /*if(OPT_OUT_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
            criteria.setReportId(EmployeeEnrollmentSummaryReportData.OPT_OUT_REPORT_ID);
        }*/

        // default sort criteria
        // this is already set in the super

        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();

        criteria.addFilter(EmployeeEnrollmentSummaryReportData.FILTER_CONTRACT_NUMBER, 
                Integer.toString(currentContract.getContractNumber()));
        
        //This criteria is added to distinguish betwween online report and csv report
        if (ELIGIBILITY_REPORT.equals(request.getParameter(REPORT_TYPE))
				&& DOWNLOAD_TASK.equals(getTask(request))) {
			criteria.addFilter(
					EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE,
					ELIGIBILITY_REPORT_CSV);
			if(ELIGIBILITY_REPORTS.equalsIgnoreCase(request.getParameter(SOURCE))){
			    criteria.addFilter(
					EmployeeEnrollmentSummaryReportData.SOURCE_PAGE,
					ELIGIBILITY_REPORTS);
			}
		} /*else if (OPT_OUT_REPORT.equalsIgnoreCase(request
				.getParameter(REPORT_TYPE))) {
			criteria.addFilter(
					EmployeeEnrollmentSummaryReportData.FILTER_REPORT_TYPE,
					OPT_OUT_REPORT_CSV);
		}*/

        EmployeeEnrollmentSummaryReportForm psform = (EmployeeEnrollmentSummaryReportForm) form;

        criteria.addFilter(EmployeeEnrollmentSummaryReportData.FILTER_AUTO_ENROLL_ON, 
                Boolean.valueOf(psform.isAutoEnrollmentEnabled()));
        criteria.addFilter(EmployeeEnrollmentSummaryReportData.FILTER_ELIGIBILITY_CALC_ON, 
                Boolean.valueOf(psform.isEligibiltyCalcOn()));
        
        //Get the filterCriteriaVo from session
        FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
		String task = getTask(request);
		
		if(filterCriteriaVo == null ){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		if (task.equals(DEFAULT_TASK)) {
			//If the task is default then reset the page no and sort details that are cached in deferral tab.
			filterCriteriaVo.clearDeferralSortDetails();
		} else if (task.equals(FILTER_TASK)) {
			//If the task is filter then reset the page no and sort details that are cached in eligibility tab.
			filterCriteriaVo.clearEligibilitySortDetails();
		}
		
		//Populate the filter criterias
		if(ELIGIBILITY_REPORTS.equalsIgnoreCase(request.getParameter(SOURCE))){
		    
		    if(psform.getMoneyTypes()!= null && psform.getMoneyTypes().size() > 0){
			String moneyTypes = "";
			
				for(int index = 0; index < psform.getMoneyTypes().size(); index++ ){
					if(index == 0){
						moneyTypes = moneyTypes + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
					}else{
						moneyTypes = moneyTypes + ", " + "'"+psform.getMoneyTypes().get(index).getValue()+"'";
					}
				}
				filterCriteriaVo.setMoneyTypeFilter("N");
			
			
			filterCriteriaVo.setMoneyTypeSelected(moneyTypes);
			criteria.addFilter(
					EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
					filterCriteriaVo.getMoneyTypeSelected());
			criteria.addFilter(
				EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPE_SELECTED,
				filterCriteriaVo.getMoneyTypeFilter());
			
		    }else{
			filterCriteriaVo.setMoneyTypeSelected("''");
			criteria.addFilter(
					EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPES,
					filterCriteriaVo.getMoneyTypeSelected());
			filterCriteriaVo.setMoneyTypeFilter("N");
			criteria.addFilter(
				EmployeeEnrollmentSummaryReportData.FILTER_MONEY_TYPE_SELECTED,
				filterCriteriaVo.getMoneyTypeFilter());
		    }
		}else{
		    
		    CensusInfoFilterCriteriaHelper.populateEligibilityTabFilterCriteria(task, filterCriteriaVo, psform, criteria);
		}
		
		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
        
        // if external user, don't display Cancelled employees
        criteria.setExternalUser(userProfile.getRole().isExternalUser());
        
        if (logger.isDebugEnabled()) {
            logger.debug("criteria= " + criteria);
            logger.debug("exit <- populateReportCriteria");
        }
    
    }
	
	protected void populateReportForm( 
            BaseReportForm reportForm, HttpServletRequest request) {
 		
 		super.populateReportForm( reportForm, request);

 		String task = getTask(request);
 		
        //Get the filterCriteriaVo object from session
        FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
 		
		if(filterCriteriaVo == null){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
 		if(task.equals(DEFAULT_TASK)){
 			
 			//If it is a initial search, sort by warning
 			if (filterCriteriaVo.getEligibilitySortDirection() == null
					&& filterCriteriaVo.getEligibilitySortField() == null) {

 				reportForm.setSortDirection(ReportSort.DESC_DIRECTION);
 				reportForm.setSortField(EmployeeEnrollmentSummaryReportData.WARNING_IND_FIELD);
			}else {
				
				// Else load the cached data
				reportForm.setSortDirection(filterCriteriaVo.getEligibilitySortDirection());
 				reportForm.setSortField(filterCriteriaVo.getEligibilitySortField());
			}
 			
 			reportForm.setPageNumber(filterCriteriaVo.getEligibilityPageNumber());
 		} 
 		
 		if (task.equals(RESET_TASK)) {
			reportForm.setSortDirection(ReportSort.DESC_DIRECTION);
			reportForm.setSortField(EmployeeEnrollmentSummaryReportData.WARNING_IND_FIELD);
		}
 		
 		//If the task is sort task then cache the sorting details
 		if(task.equals(SORT_TASK)){
 			reportForm.setPageNumber(1);
 			filterCriteriaVo.setEligibilitySortDirection(reportForm.getSortDirection());
 			filterCriteriaVo.setEligibilitySortField(reportForm.getSortField());
 		}
 		
 		// If the task is page task then cache the page number
 		if (task.equals(PAGE_TASK)){
 			filterCriteriaVo.setEligibilityPageNumber(reportForm.getPageNumber());
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
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public String execute(
			
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException
	{
	    UserProfile userProfile = SessionHelper.getUserProfile(request);
        
        // check for selected access - same as Census Summary page
        if (userProfile.isSelectedAccess()) {
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        // check if contract is discontinued - same as Census Summary page
        if (userProfile.getCurrentContract().isDiscontinued()) {
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        String mapping=request.getParameter("mapping");
		if ("POST".equalsIgnoreCase(request.getMethod()) ) {
			// do a refresh so that there's no problem using the back button
			ControllerForward forward = new ControllerForward("refresh", 
					"/do" + mapping + "?task=" + getTask(request),
					true);
		    if(logger.isDebugEnabled()) {
			    logger.debug("forward = " + forward);
		    }
			return forward.getPath();
		}
		
			
		return null;
	}
     
    /**
     * 
     * @param reportForm
     * @param report
     * @param request
     * @return
     */
   /* protected byte[] getOptOutDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {        

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getOptOutDownloadData");
        }        
        
        EmployeeEnrollmentSummaryReportData summaryReport = (EmployeeEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
        EmployeeEnrollmentSummaryReportForm form = (EmployeeEnrollmentSummaryReportForm)reportForm;
        StringBuffer buffer = new StringBuffer();
        
        // If there are no changes, Then display static text in Post Opt Out Date Changes Report
        if(summaryReport.getDetails().size() == 0){
        	buffer.append(NO_RESULT_TEXT);
        	return buffer.toString().getBytes();
        }
        
        // Display number of employees
        int numberOfEmployees = summaryReport.getTotalCount();
        int numberOfChanges = summaryReport.getNumberOfChanges();
        
        Contract currentContract = getUserProfile(request).getCurrentContract();
        
        List<String> dayMonth = new ArrayList<String>();
        
        createDayMonthArray(request, dayMonth);
        UserProfile user = getUserProfile(request);
        
        if(summaryReport.getDetails().size() == 0){
        	buffer.append("Curently there are no details to display.");
        	return buffer.toString().getBytes();
        }
        // Title
        buffer.append("Download post 'opt-out date' changes Report").append(LINE_BREAK);
        // Contract #, Contract name
        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);
        buffer.append("Actual Date of Download").append(COMMA).append(
                DateRender.formatByPattern(new Date(), "", DATE_PATTERN)).append(LINE_BREAK);

        Date ped = form.getNextPED();
        Date optoutDeadline = form.getNextOptOut();

        if(new Date().after(optoutDeadline)) {
            buffer.append("Changes for Plan Entry Date").append(COMMA).append(
                    DateRender.formatByPattern(form.getNextPED(), "", DATE_PATTERN)).append(LINE_BREAK);
            buffer.append("Changes for Opt-out deadline").append(COMMA).append(
                    DateRender.formatByPattern(form.getNextOptOut(), "", DATE_PATTERN)).append(LINE_BREAK);
        } else {
            // The previous PED and OOD has to be shown 
            Calendar cal = new GregorianCalendar();
            cal.setTime(ped);
            int freq = form.getFrequency();
            cal.roll(Calendar.MONTH, -freq);
            
            Date previousPED = cal.getTime(); 
            
            cal.roll(Calendar.DAY_OF_YEAR, -form.getOOD());
            
            Date previousOOD =  cal.getTime(); 
            
            buffer.append("Changes for Plan Entry Date").append(COMMA).append(
                    DateRender.formatByPattern(previousPED, "", DATE_PATTERN)).append(LINE_BREAK);
            buffer.append("Changes for Opt-out deadline").append(COMMA).append(
                    DateRender.formatByPattern(previousOOD, "", DATE_PATTERN)).append(LINE_BREAK);
        }

        
        buffer.append("Total Number of Employees on file:  ").append(numberOfEmployees).append(LINE_BREAK);   
//        buffer.append("Total number of changes listed in the report:  ").append(numberOfChanges).append(LINE_BREAK);   
        buffer.append("This information is accurate as of ").append(form.getCurrentDate())
                .append( "for the period ending ").append(
                DateRender.formatByPattern(form.getNextPED(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
        
        buffer.append(LINE_BREAK);
        // Column headings
        buffer.append("Employee Last Name").append(COMMA)
        .append("Employee First Name").append(COMMA)
        .append("Employee Middle Initial").append(COMMA)
        .append("SSN").append(COMMA)
        .append("Date of Birth").append(COMMA);
        
        if(form.getHasPayrollNumberFeature()) {
            buffer.append("Employee Identification Number").append(COMMA);
        }
        if(form.getHasDivisionFeature()) {
            buffer.append("Division").append(COMMA);
        }
        
        buffer
        .append("Enrollment status").append(COMMA)
        .append("Enrollment method").append(COMMA)
        .append("Enrollment processing date").append(COMMA)
        .append("Applicable Plan Entry Date").append(COMMA)
//        .append("Contribution status").append(COMMA)
        .append("Eligibility Indicator").append(COMMA)
        .append("Eligibility Date").append(COMMA)
        .append("Opt-out Indicator").append(COMMA)
        .append("Opt-out changed date").append(COMMA)
        .append("Opt-out By").append(COMMA)
        .append("Opt-out Source").append(COMMA);
 
        if(currentContract.hasRoth()) {
            buffer
            .append("Before Tax Deferral (%) at Enrollment").append(COMMA)
            .append("Before Tax Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Designated Roth Deferral (%) at Enrollment").append(COMMA)
            .append("Designated Roth Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral)").append(COMMA)
            .append("Designated Roth Deferral (%)").append(COMMA)
            .append("Designated Roth Flat ($) Deferral)").append(COMMA);
        } else {
            buffer
            .append("Before Tax Deferral (%) at Enrollment").append(COMMA)
            .append("Before Tax Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral)").append(COMMA);
        }
        
        buffer.append(LINE_BREAK);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }     
        
        //loop through details
        Iterator iterator = summaryReport.getDetails().iterator();
        while (iterator.hasNext()) {
                EmployeeSummaryDetails theItem = (EmployeeSummaryDetails) iterator.next();
                
                List<String> columnNames = buildListOfColumnNames(theItem.getStatusChanges()); 
                    
                buffer.append(LINE_BREAK);
                
                appendFieldChangedIndicator(EmployeeEnrollmentSummaryReportData.LAST_NAME_COLUMN, columnNames, buffer);
                buffer.append(processString(theItem.getLastName())).append(COMMA);
                buffer.append(processString(theItem.getFirstName())).append(COMMA);
                buffer.append(processString(theItem.getMiddleInitial())).append(COMMA);
                buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA); 
                buffer.append(theItem.getBirthDate() == null 
                              ? "" 
                              : DateRender.formatByPattern(theItem.getBirthDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
                      .append(COMMA);             
                
                if(form.getHasPayrollNumberFeature()) {                  
                    buffer.append(processString(theItem.getEmployerDesignatedID()));                    
                    buffer.append(COMMA);
                }
                
                if(form.getHasDivisionFeature()) {
                    buffer.append(processString(theItem.getDivision()));
                    buffer.append(COMMA);
                }
                
                appendFieldChangedIndicator(EmployeeEnrollmentSummaryReportData.ENROLLMENT_STATUS_COLUMN, columnNames, buffer);
                buffer.append(processString(theItem.getEnrollmentStatus())).append(COMMA);
                
                buffer.append(processString(theItem.getEnrollmentMethod())).append(COMMA);
                buffer.append(DateRender.formatByPattern(theItem.getEnrollmentProcessedDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                buffer.append(DateRender.formatByPattern(applicablePEDProcessing(theItem, dayMonth), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
                
//                buffer.append(processString(theItem.getContributionStatus())).append(COMMA);
                
                appendFieldChangedIndicator(EmployeeEnrollmentSummaryReportData.ELIGIBLE_TO_ENROLL_COLUMN, columnNames, buffer);
                if (theItem.getEligibleToEnroll() != null){    
                    buffer.append(processString(theItem.getEligibleToEnroll()));
                }
                buffer.append(COMMA);
                
                appendFieldChangedIndicator(EmployeeEnrollmentSummaryReportData.ELIGIBILITY_DATE_COLUMN, columnNames, buffer);
                buffer.append(processString(theItem.getEligibilityDate())).append(COMMA);
                
                appendFieldChangedIndicator(EmployeeEnrollmentSummaryReportData.OPT_OUT_COLUMN, columnNames, buffer);
                
                if(theItem.getAutoEnrollOptOutInd() != null && "Y".equalsIgnoreCase(theItem.getAutoEnrollOptOutInd())) {
                    buffer.append("Yes").append(COMMA);
                } else {
                    buffer.append("No").append(COMMA);
                }

                EmployeeChangeHistoryVO history = theItem.getOptOutHistory();
                if(history != null) {
                    if(history.getCurrentUpdatedTs()!= null) {                        
                        buffer.append(
                            DateRender.formatByPattern(new Date(history.getCurrentUpdatedTs().getTime()), "", RenderConstants.MEDIUM_MDY_SLASHED));
                    }
                    buffer.append(COMMA);
                    buffer.append(processString(getUserName(history, request))).append(COMMA);
                    buffer.append(processString(getSource(history))).append(COMMA);
                } else {
                    buffer.append(COMMA).append(COMMA).append(COMMA);
                }
                
                Double beforeTaxPctAtEnroll = null;
                Double beforeTaxAmtAtEnroll = null;
                Double afterTaxPctAtEnroll = null;
                Double afterTaxAmtAtEnroll = null;
                Double beforeTaxPct = null;
                Double beforeTaxAmt = null;
                Double afterTaxPct = null;
                Double afterTaxAmt = null;
                
                if(currentContract.hasRoth()) {
                    try {
                        beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }                    
                    try {
                        beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxPctAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentAfterTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxAmtAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentAfterTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }                    
                    try {
                        beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                                new Double(theItem.getBeforeTaxDeferralPct()):getCalculatedPct(theItem);
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
                        afterTaxPct = theItem.getAfterTaxDeferralPct() != null?
                                new Double(theItem.getAfterTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxAmt = theItem.getAfterTaxDeferralAmt() != null?
                                new Double(theItem.getAfterTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                            
                    if(beforeTaxPctAtEnroll != null) {
                        buffer.append(NumberRender.formatByPattern(beforeTaxPctAtEnroll, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(beforeTaxAmtAtEnroll != null) {
                        buffer.append(beforeTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA);
                    if(afterTaxPctAtEnroll != null) {
                        buffer.append(NumberRender.formatByPattern(afterTaxPctAtEnroll, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(afterTaxAmtAtEnroll != null) {
                        buffer.append(afterTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA);
                    if(beforeTaxPct != null) {
                        buffer.append(NumberRender.formatByPattern(beforeTaxPct, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(beforeTaxAmt != null) {
                        buffer.append(beforeTaxAmt);
                    }
                    buffer.append(COMMA);
                    if(afterTaxPct != null) {
                        buffer.append(NumberRender.formatByPattern(afterTaxPct, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(afterTaxAmt != null) {
                        buffer.append(afterTaxAmt);
                    }
                    buffer.append(COMMA);
                } else {
                    try {
                        beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                                new Double(theItem.getBeforeTaxDeferralPct()):getCalculatedPct(theItem);
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    
                    if(beforeTaxPctAtEnroll != null) {
                        buffer.append(NumberRender.formatByPattern(beforeTaxPctAtEnroll, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(beforeTaxAmtAtEnroll != null) {
                        buffer.append(beforeTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA);
                    if(beforeTaxPct != null) {
                        buffer.append(NumberRender.formatByPattern(beforeTaxPct, "", "##.###"));
                    }
                    buffer.append(COMMA);
                    if(beforeTaxAmt != null) {
                        buffer.append(beforeTaxAmt);
                    }
                    buffer.append(COMMA);
                }
  
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getOptOutDownloadData");
        }

        return buffer.toString().getBytes();
    }*/
    
    /**
     * Utility that checks if the column name exists in the list
     * If it does it appends a '(*)' to buffer
     * 
     * @param columnName
     * @param columnNames
     * @param buffer
     */
    private void appendFieldChangedIndicator(String columnName, List<String> columnNames, StringBuffer buffer) {
        if(columnNames != null && !columnNames.isEmpty() && columnName != null) {
            if(columnNames.contains(columnName.toUpperCase())) {
                buffer.append("(*)");
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
     * Builds a list of column names that were changed for this employee
     * 
     * @param list
     * @return
     */
    private List<String> buildListOfColumnNames(List<EmployeeChangeHistoryVO> list) {
        
        List<String> names = new ArrayList<String>();
        
        if(list != null && !list.isEmpty()) {
            for (EmployeeChangeHistoryVO element : list) {
                names.add(element.getColumnName().toUpperCase());
            }
        }
        
        return names;
    }
    
    /**
     * 
     * @param reportForm
     * @param report
     * @param request
     * @return
     */
    protected byte[] getEligibilityDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request)throws SystemException {        

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getEligibilityDownloadData");
        }        
        
        EmployeeEnrollmentSummaryReportData summaryReport = (EmployeeEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
        EmployeeEnrollmentSummaryReportForm form = (EmployeeEnrollmentSummaryReportForm)reportForm;
        StringBuffer buffer = new StringBuffer();
        String fromDate = null;
        String toDate = null;
        int numberOfEmployees = summaryReport.getTotalNumberOfEmployees();        
        Contract currentContract = getUserProfile(request).getCurrentContract();
        List<String> dayMonth = new ArrayList<String>();
        
        createDayMonthArray(request, dayMonth);
        
            fromDate = form.getFromPED();
            toDate = form.getToPED();
                
        // Title
        buffer.append("Download Eligibility Report").append(LINE_BREAK);
        // Contract #, Contract name
        buffer.append("Contract").append(COMMA).append(
                currentContract.getContractNumber()).append(COMMA).append(
                currentContract.getCompanyName()).append(LINE_BREAK);
        buffer.append("Actual Date of Download").append(COMMA).append(
                DateRender.formatByPattern(new Date(), "", DATE_PATTERN)).append(LINE_BREAK);
        if (form.isEZstartOn()) {
        	
        	try{
				Content message = null;
				message = ContentCacheManager.getInstance().getContentById(
								ContentConstants.NEXT_PLAN_ENTRY_DATE_LABEL,
								ContentTypeManager.instance().MESSAGE);
				String[] params = { form.getEedefShortName() };
				
				buffer.append(ContentUtility.getContentAttribute(message, "text",null,params))
						.append(COMMA);
						
			}catch (ContentException exp) {
				throw new SystemException(exp, getClass().getName(),
						"getDownloadData", "Something wrong with CMA");
			}
        	
	        buffer.append(
	                DateRender.formatByPattern(form.getNextPED(), "", DATE_PATTERN)).append(LINE_BREAK);
	        buffer.append("Next Opt-out deadline").append(COMMA).append(
	                DateRender.formatByPattern(form.getNextOptOut(), "", DATE_PATTERN)).append(LINE_BREAK);
        }
        buffer.append("Total Number of Employees on file: ").append(numberOfEmployees)
         .append(" ( If search filters were applied; this may not include full list of employees on record.)")
         .append(LINE_BREAK);
        if (form.isEZstartOn() || form.isEligibiltyCalcOn()) {
	        
	        buffer.append("From Plan Entry Date,To Plan Entry Date");
	        buffer.append(LINE_BREAK);
	        if(fromDate != null && toDate != null) {
	            buffer.append(fromDate).append(COMMA).append(toDate).append(LINE_BREAK);
	        }
	        buffer.append("Enrollment processing date from,Enrollment processing date to");
	        buffer.append(LINE_BREAK);
        	String enrollFrom = form.getEnrolledFrom();
        	String enrollTo = form.getEnrolledTo();
        	if ((enrollFrom != null && enrollFrom.trim().length() >0) &&
        	    (enrollTo != null && enrollTo.trim().length()> 0)) {
        		buffer.append(enrollFrom).append(COMMA).append(enrollTo).append(LINE_BREAK);
        	}
        } else { // EZstart and EC are off.
	        buffer.append("Enrollment processing date from,Enrollment processing date to");
	        buffer.append(LINE_BREAK);
        	String enrollFrom = form.getEnrolledFrom();
        	String enrollTo = form.getEnrolledTo();
        	if (!StringUtils.isEmpty(enrollFrom) && !StringUtils.isEmpty(enrollTo)){
        		buffer.append(enrollFrom).append(COMMA).append(enrollTo).append(LINE_BREAK);
        	}
        }
        if(form.isEligibiltyCalcOn()){
            
            String moneyType = "";
            for(com.manulife.pension.service.environment.valueobject.LabelValueBean bean : summaryReport.getMoneyTypes()){
        	if(bean.getValue().equalsIgnoreCase(form.getMoneyTypeSelected())){
        	    moneyType = bean.getLabel();
        	}
            }
             moneyType = "All".equalsIgnoreCase(form.getMoneyTypeSelected()) || (form.getMoneyTypeSelected() == null)?"All":moneyType;
            buffer.append("Money Type : "+moneyType).append(LINE_BREAK);
        }
        buffer.append(LINE_BREAK);
        try{
			Content message = null;
			if(form.isEZstartOn() && !form.isEligibiltyCalcOn()){
				  message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.ELIGIBILITY_REPORT_DISCLAIMER_EZ_ON_EC_OFF,
							ContentTypeManager.instance().MESSAGE);
			}else if(!form.isEZstartOn() && form.isEligibiltyCalcOn()){
				  message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.ELIGIBILITY_REPORT_DISCLAIMER_EZ_OFF_EC_ON,
							ContentTypeManager.instance().MESSAGE);
			}else if(form.isEZstartOn() && form.isEligibiltyCalcOn()){
				  message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.ELIGIBILITY_REPORT_DISCLAIMER_EZ_ON_EC_ON,
							ContentTypeManager.instance().MESSAGE);
			}else if(!form.isEZstartOn() && !form.isEligibiltyCalcOn()){
				  message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.ELIGIBILITY_REPORT_DISCLAIMER_EZ_OFF_EC_OFF,
							ContentTypeManager.instance().MESSAGE);
			}

			String contentMessage = ContentUtility.getContentAttribute(message, "text");
			
			contentMessage = contentMessage == null ? "":contentMessage;
			
			buffer.append("\""+contentMessage + "\"").append(LINE_BREAK);
					
		}catch (ContentException exp) {
			throw new SystemException(exp, getClass().getName(),
					"getDownloadData", "Something wrong with CMA");
		}
		buffer.append(LINE_BREAK);
        // Column headings
        buffer.append("Employee Last Name").append(COMMA)        
        .append("Employee First Name").append(COMMA)
        .append("Employee Middle Initial").append(COMMA)
        .append("SSN").append(COMMA)
        .append("Date of Birth").append(COMMA)
        .append("Hire Date").append(COMMA);
        if(form.getHasPayrollNumberFeature()) {
            buffer.append("Employee Identification Number").append(COMMA);
        }
        if(form.getHasDivisionFeature()) {
            buffer.append("Division").append(COMMA);
        }
        
        buffer.append("Enrollment status").append(COMMA)
        .append("Enrollment method").append(COMMA)
        .append("Enrollment processing date").append(COMMA);
        
        if(form.isEligibiltyCalcOn() && !(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))){
        	buffer.append("LTPTAssessYr").append(COMMA);
        }
      //  .append("Applicable Plan Entry Date").append(COMMA)
//        .append("Contribution status").append(COMMA)
        buffer.append("Eligibility Indicator").append(COMMA);
        String tempEligibilityReport = "";
        if(summaryReport.getMoneyTypes()!= null && summaryReport.getMoneyTypes().size() > 0){
        	for(com.manulife.pension.service.environment.valueobject.LabelValueBean moneyType:summaryReport.getMoneyTypes()){
        		tempEligibilityReport = tempEligibilityReport + "Eligibility Date - "+ moneyType.getLabel()+
        		",Plan Entry Date - "+ moneyType.getLabel();
        		
        		if(form.isEligibiltyCalcOn()){
        		    tempEligibilityReport = tempEligibilityReport + ",Calculation override - "+ moneyType.getLabel()+
        		    	",Period of Service From Date - "+ moneyType.getLabel()+
        		    	",Period of Service To Date - "+ moneyType.getLabel()+
        		    	",Period type - "+ moneyType.getLabel()+
        		    	",Period Hours Worked - "+ moneyType.getLabel()+
        		    	",Period Hours Effective Date - "+ moneyType.getLabel()+
        		    	" ," ;
        		}else{
        		    tempEligibilityReport = tempEligibilityReport + ",";
        		}
        	}
        }else{
        	tempEligibilityReport= "Eligibility Date,";
        }
        buffer.append(tempEligibilityReport);
        
        
        if (form.isEZstartOn()) {
	        buffer.append("Opt-out Indicator").append(COMMA);
	        /*.append("Opt-out changed date").append(COMMA)
	        .append("Opt-out By").append(COMMA)
	        .append("Opt-out Source").append(COMMA);*/
        	if(currentContract.isDMContract())
            {
            buffer.append("Enrollment Kit mailing date").append(COMMA);
        	}
        }
        buffer.append("Enrollment material language").append(COMMA);
        
        if(currentContract.hasRoth()) {
            buffer
            .append("Before Tax Deferral (%) at Enrollment").append(COMMA)
            .append("Before Tax Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Designated Roth Deferral (%) at Enrollment").append(COMMA)
            .append("Designated Roth Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral)").append(COMMA)
            .append("Designated Roth Deferral (%)").append(COMMA)
            .append("Designated Roth  Flat ($) Deferral)").append(COMMA);
        } else {
            buffer
            .append("Before Tax Deferral (%) at Enrollment").append(COMMA)
            .append("Before Tax Flat ($) Deferral) at Enrollment").append(COMMA)
            .append("Before Tax Deferral (%)").append(COMMA)
            .append("Before Tax Flat ($) Deferral)").append(COMMA);
        }
        
        buffer.append(LINE_BREAK);
	    if(summaryReport.getMoneyTypes()!= null && summaryReport.getMoneyTypes().size() > 0){}
        
        UserProfile user = getUserProfile (request);
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        } 
        //loop through details
        
        Map<BigDecimal, List<EmployeePlanEntryVO>> partTimeEmployeePlanEntryMap = EmployeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getAllPartTimeEmployeePlanEntryList(
						getUserProfile(request).getCurrentContract().getContractNumber(), null);
        
        if(summaryReport.getDetails() != null){
        
            Iterator iterator = summaryReport.getDetails().iterator();
            while (iterator.hasNext()) {
                EmployeeSummaryDetails theItem = (EmployeeSummaryDetails) iterator.next();
                
                buffer.append(LINE_BREAK);
                int colCount = 0;
                
                buffer.append(processString(theItem.getLastName())).append(COMMA); colCount++;
                buffer.append(processString(theItem.getFirstName())).append(COMMA); colCount++;
                buffer.append(processString(theItem.getMiddleInitial())).append(COMMA); colCount++;
                buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA); 
                colCount++;                        
    
                buffer.append(theItem.getBirthDate() == null 
                              ? "" 
                              : DateRender.formatByPattern(theItem.getBirthDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
                      .append(COMMA); colCount++; 
               buffer.append(theItem.getHireDate() == null 
                      ? "" 
                      : DateRender.formatByPattern(theItem.getHireDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
               .append(COMMA); colCount++; 
                
                if(form.getHasPayrollNumberFeature()) {                 
                    buffer.append(processString(theItem.getEmployerDesignatedID()));
                    buffer.append(COMMA); colCount++;
                }
                if(form.getHasDivisionFeature()) {
                    buffer.append(processString(theItem.getDivision()));
                    buffer.append(COMMA); colCount++;
                }

                buffer.append(processString(theItem.getEnrollmentStatus())).append(COMMA); colCount++;
                buffer.append(processString(theItem.getEnrollmentMethod())).append(COMMA); colCount++;
                buffer.append(DateRender.formatByPattern(theItem.getEnrollmentProcessedDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA); colCount++; 
          //      buffer.append(DateRender.formatByPattern(applicablePEDProcessing(theItem, dayMonth), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA); colCount++;
                
//              buffer.append(processString(theItem.getContributionStatus())).append(COMMA);
                if(form.isEligibiltyCalcOn() && !(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))){
                	if(theItem.getHireDate()!=null && (CensusConstants.EMPLOYMENT_STATUS_ACTIVE.equalsIgnoreCase(theItem.getEmploymentStatus()) 
            				|| StringUtils.isBlank(theItem.getEmploymentStatus()))) {
		                int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
								.evaluateLongTermPartTimeAssessmentYearReport(partTimeEmployeePlanEntryMap,
										Integer.parseInt(theItem.getProfileId()), null);
						// display blank if returns 0
						if (longTermPartTimeAssessmentYear >= EmployeeEligibilityAssessmentReqConstants.DEFAULT_LONG_TERM_PART_TIME_ASSESSMENT_YEAR) {
							buffer.append(longTermPartTimeAssessmentYear).append(COMMA);
						}
						else {
							buffer.append(BLANK).append(COMMA);
		                }
                	}
                	else {
						buffer.append(BLANK).append(COMMA);
	                }
                }
                if (theItem.getEligibleToEnroll() != null){    
                    String eligibleToEnroll = theItem.getEligibleToEnroll();
                    buffer.append(processString("Yes".equalsIgnoreCase(eligibleToEnroll)?"Y":("No".equalsIgnoreCase(eligibleToEnroll)?"N":"")));
                }
                buffer.append(COMMA); colCount++;
                
                if(summaryReport.getMoneyTypes()!= null && summaryReport.getMoneyTypes().size() > 0){
                	for(String eligibilityData : theItem.getEligibilityData()){
                		buffer.append(eligibilityData).append(COMMA);
                		if(form.isEligibiltyCalcOn()){
                		    colCount =colCount +8;
                		}else{
                		    colCount =colCount +2;
                		}
                	}
                }else{
                    String eligibilityDate = "mm/dd/yyyy".equalsIgnoreCase(theItem.getEligibilityDate())?""
                	    			:theItem.getEligibilityDate();
                				
                	buffer.append(eligibilityDate).append(COMMA); colCount++;
                }
                
                if (form.isEZstartOn()) {
	                if("Y".equalsIgnoreCase(theItem.getAutoEnrollOptOutInd())) {
	                    buffer.append("Y").append(COMMA);
	                } else if("N".equalsIgnoreCase(theItem.getAutoEnrollOptOutInd())) {
	                    buffer.append("N").append(COMMA);
	                } else{
	                    buffer.append("").append(COMMA);
	                }
	                colCount++;
	                
	                /*EmployeeChangeHistoryVO history = theItem.getOptOutHistory();
	                if(history != null) {
	                    if(history.getCurrentUpdatedTs()!= null) {                        
	                        buffer.append(
	                            DateRender.formatByPattern(new Date(history.getCurrentUpdatedTs().getTime()), "", RenderConstants.MEDIUM_MDY_SLASHED));
	                    }
	                    buffer.append(COMMA); colCount++;
	                    buffer.append(processString(getUserName(history, request))).append(COMMA); colCount++;
	                    buffer.append(processString(getSource(history))).append(COMMA); colCount++;
	                } else {
	                    buffer.append(COMMA).append(COMMA).append(COMMA); colCount= colCount+3;
	                }*/
               if(currentContract.isDMContract())
                {
                 	buffer.append(processString(theItem.getMailingDate())).append(COMMA);
                 	colCount++;
                }
              
                }
                if(ENGLISH.equalsIgnoreCase(theItem.getLanguageInd())){
                    buffer.append(processString(ENGLISH_DISPLAY)).append(COMMA);
                }
                else if (SPANISH.equalsIgnoreCase(theItem.getLanguageInd())){
                    buffer.append(processString(SPANISH_DISPLAY)).append(COMMA);
                }
                else{ 
                    buffer.append(processString(BLANK)).append(COMMA);
                }
                colCount++;
                
                Double beforeTaxPctAtEnroll = null;
                Double beforeTaxAmtAtEnroll = null;
                Double afterTaxPctAtEnroll = null;
                Double afterTaxAmtAtEnroll = null;
                Double beforeTaxPct = null;
                Double beforeTaxAmt = null;
                Double afterTaxPct = null;
                Double afterTaxAmt = null;
                
                if(currentContract.hasRoth()) {
                    try {
                        beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }                    
                    try {
                        beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxPctAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentAfterTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxAmtAtEnroll = theItem.getAtEnrollmentAfterTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentAfterTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }                    
                    try {
                        beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                                new Double(theItem.getBeforeTaxDeferralPct()):getCalculatedPct(theItem);
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
                        afterTaxPct = theItem.getAfterTaxDeferralPct() != null?
                                new Double(theItem.getAfterTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        afterTaxAmt = theItem.getAfterTaxDeferralAmt() != null?
                                new Double(theItem.getAfterTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    
                    if(beforeTaxPctAtEnroll != null) {
                        buffer.append(formatPercentageFormatter(beforeTaxPctAtEnroll));
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxAmtAtEnroll != null) {
                        buffer.append(beforeTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA); colCount++;
                    if(afterTaxPctAtEnroll != null) {
                        buffer.append(formatPercentageFormatter(afterTaxPctAtEnroll));
                    }
                    buffer.append(COMMA); colCount++;
                    if(afterTaxAmtAtEnroll != null) {
                        buffer.append(afterTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxPct != null) {
                        buffer.append(formatPercentageFormatter(beforeTaxPct));
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxAmt != null) {
                        buffer.append(beforeTaxAmt);
                    }
                    buffer.append(COMMA); colCount++;
                    if(afterTaxPct != null) {
                        buffer.append(formatPercentageFormatter(afterTaxPct));
                    }
                    buffer.append(COMMA); colCount++;
                    if(afterTaxAmt != null) {
                        buffer.append(afterTaxAmt);
                    }
                    buffer.append(COMMA); colCount++;
                } else {
                    try {
                        beforeTaxPctAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralPct() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralPct()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxAmtAtEnroll = theItem.getAtEnrollmentBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getAtEnrollmentBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxPct = theItem.getBeforeTaxDeferralPct() != null?
                                new Double(theItem.getBeforeTaxDeferralPct()):getCalculatedPct(theItem);
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    try {
                        beforeTaxAmt = theItem.getBeforeTaxDeferralAmt() != null?
                                new Double(theItem.getBeforeTaxDeferralAmt()):null;
                    } catch (NumberFormatException e) {
                        // it should never happen, assume null
                    }
                    
                    if(beforeTaxPctAtEnroll != null) {
                        buffer.append(formatPercentageFormatter(beforeTaxPctAtEnroll));
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxAmtAtEnroll != null) {
                        buffer.append(beforeTaxAmtAtEnroll);
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxPct != null) {
                        buffer.append(formatPercentageFormatter(beforeTaxPct));
                    }
                    buffer.append(COMMA); colCount++;
                    if(beforeTaxAmt != null) {
                        buffer.append(beforeTaxAmt);
                    }
                    buffer.append(COMMA); colCount++;
                }
                
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEligibilityDownloadData");
        }

        return buffer.toString().getBytes();
    }
    
    /**
     * Returns the contract default deferral % if the status is 'Pending Enrollment' 
     * The stored procedure is passing the contract default deferral % if the status 
     * is 'Pending Enrollment' in the contributionPct property
     * 
     * @param theItem
     * @return
     */
    private Double getCalculatedPct(EmployeeSummaryDetails theItem) {
        Double beforeTaxPct = null;
        
        if(PENDING_ENROLLMENT.equalsIgnoreCase(theItem.getEnrollmentStatus())) {
            try {
                beforeTaxPct = theItem.getContributionPct() != null?
                        new Double(theItem.getContributionPct()):null;
            } catch (NumberFormatException e) {
                // it should never happen, assume null
            }
        }
        
        return beforeTaxPct;
    }
    
    /**
     * Gets the labels from session and creates a list of String like "JAN 21", "FEB 21",....
     * or whatever the case is based on frequency and PED
     * 
     * @param request
     * @param dayMonth
     */
    private void createDayMonthArray(HttpServletRequest request, List<String> dayMonth) {
        List<LabelValueBean> labels = (List<LabelValueBean>)request.getSession().getAttribute(CensusConstants.PED_MONTH_DAY_LIST);
        // Create a list of String in the format "MMM d"
        if(labels != null) {
            for (LabelValueBean bean : labels) {
                if(bean != null && bean.getValue() != null) {
                    dayMonth.add(bean.getValue().toUpperCase().trim());
                }
            }
        }
    }
    
    /**
     * Applies EES.23.2 for Enrolled employees because the logic  for EES.23.1 has
     * been applied on the stored procedure 
     * 
     * @param employeeDetail
     * @return
     */
    private Date applicablePEDProcessing(EmployeeSummaryDetails employeeDetail, List<String> dayMonth) {
        Date aPED = null;
        
        if(employeeDetail != null) {
            if(employeeDetail.isParticipantInd()) {
                // Check if it is matching a PED
                if(isValidAgainstPED(employeeDetail, dayMonth)) {
                    return employeeDetail.getEnrollmentProcessedDate();
                } else {
                    return getNextPED(employeeDetail);
                }
                
            } else {
                aPED = employeeDetail.getApplicablePlanEntryDate(); 
            }
            
            // new rule for AEE1
            if("NT".equals(employeeDetail.getParticipantStatusCode()) && "Y".equals(employeeDetail.getAutoEnrollOptOutInd()))
            {
            	aPED = null;
            }
                   
        }
        
        return aPED;
    }
    
    /**
     * Checks if the processing date matches a valid PED
     * 
     * @param employeeDetail
     * @return
     */
    private boolean isValidAgainstPED(EmployeeSummaryDetails employeeDetail, List<String> dayMonth) {
        Date processedDate = employeeDetail.getEnrollmentProcessedDate();
        
        if(processedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
            String dayMonthProcessed = dateFormat.format(processedDate).trim().toUpperCase();
            
            if(dayMonth.contains(dayMonthProcessed)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns next PED or null
     * 
     * @param employeeDetail
     * @return
     */
    private Date getNextPED(EmployeeSummaryDetails employeeDetail) {
        try {
            if(employeeDetail.getEligibilityDateAsDate() != null) {
                return ContractServiceDelegate.getInstance().getNextPlanEntryDate(
                        1, employeeDetail.getEligibilityDateAsDate());
            }
        } catch (Exception e) {
            // Not much can be done at this point
            logger.error(e);
        } 
        
        return null;
    }
    
    /**
     * Utility method that builds the source channel as per DFS
     * 
     * @param value
     * @return
     */
    private String getSource(EmployeeChangeHistoryVO value) {
        String source = value.getSourceChannelCode();

        if (source != null) {
            String sysDesc = SourceMap.get(source.trim().toUpperCase());
            return sysDesc == null ? FileSubmission : sysDesc;
        } else {
            return "";
        }
    }

    /**
     * Utility method that builds the user name as per DFS 
     * 
     * @param value
     * @param request
     * @return
     */
    private String getUserName(EmployeeChangeHistoryVO value, HttpServletRequest request) {
        UserProfile loginUser = SessionHelper.getUserProfile(request);
        ChangeUserInfo userInfo = value.getCurrentUser();
        if (loginUser.isInternalUser()) {
            if (isInternalUser(userInfo)) {
                if (!StringUtils.isEmpty(userInfo.getUserId())) {
                    return userInfo.getUserId() + ", " + JOHN_HANCOCK_REPRESENTATIVE;
                } else {
                    return StringUtils.trimToEmpty(userInfo.getLastName()) + ", "
                            + StringUtils.trimToEmpty(userInfo.getFirstName()) + ", "
                            + JOHN_HANCOCK_REPRESENTATIVE;
                }
            } else {
                return StringUtils.trimToEmpty(userInfo.getLastName()) + ", "
                        + StringUtils.trimToEmpty(userInfo.getFirstName());
            }
        } else {
            if (isInternalUser(userInfo)) {
                return JOHN_HANCOCK_REPRESENTATIVE;
            } else {
                String source = value.getSourceChannelCode();
                if ("PA".equals(source)) {
                    return EMPLOYEE;
                } else {
                    return StringUtils.trimToEmpty(userInfo.getLastName()) + ", "
                    + StringUtils.trimToEmpty(userInfo.getFirstName());
                }
            }
        }
    }

    /**
     * Utility method that checks if the change was performed by an internal user
     * 
     * @param user
     * @return
     */
    private boolean isInternalUser(ChangeUserInfo user) {
        String userIdType = user.getUserIdType();
        if (UserIdType.CAR.equals(userIdType) ||
                UserIdType.UP_INTERNAL.equals(userIdType)
            ) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @see ReportController#getDownloadData(PrintWriter, BaseReportForm,
     *      ReportData)
     */
    protected byte[] getDownloadData(
            BaseReportForm reportForm, ReportData report,
            HttpServletRequest request)throws SystemException {        

        byte[] bytes = null;
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData");
        }
        
        // Identify the type of report
       /* if(OPT_OUT_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))){
            bytes = getOptOutDownloadData(reportForm, report, request);
        } else { */       
            bytes = getEligibilityDownloadData(reportForm, report, request);
       // }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDownloadData");
        }

        return bytes;
    }
 }

