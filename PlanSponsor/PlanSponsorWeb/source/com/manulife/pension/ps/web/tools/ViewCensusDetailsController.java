package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.DataCheckerServiceDelegate;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessDataCheckerException;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.CensusDetailsHelper;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * @author parkand
 * 
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"viewCensusDetailsForm"})

public class ViewCensusDetailsController extends ReportController {
	
	@ModelAttribute("viewCensusDetailsForm")
	public ViewCensusDetailsForm populateForm() 
	{
		return new ViewCensusDetailsForm();
		}
	private static final String DEFAULT_SORT_FIELD = CensusSubmissionReportData.SORT_RECORD_NUMBER;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int DEFAULT_PAGE_SIZE = 35;
	
	private static final String SUBMIT_ACTION = "submit";
    private static final String HISTORY_ACTION = "history";
	private static final String SUBMIT_ACTION_LABEL = "submission history";
	private static final String DISCARD_ACTION = "discard";
	private static final String AFTER_DISCARD_ACTION = "afterDiscard";
	private static final String TOOLS_ACTION = "tools";
	
	private static final String ADDRESS_TYPE = "A";
    private static final String CENSUS_TYPE = "E";
	private static final String CANCELLED_STATUS = "A5";
	
	private static final String VIEW_MODE="v";
	
	protected static final String DOWNLOAD_COLUMN_HEADING = "cens.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country," +
    "StateRes,ERProvEmail,Division,BirthDate,HireDate,EmplStat,EmplStatDate,EligInd,EligDate,OptOutInd,YTDHrs,PlanYTDComp,YTDHrsWkCompDt," +
    "BaseSalary,BfTxDefPct,DesigRothPct,BfTxFltDoDef,DesigRothAmt";
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{		
		  forwards.put("input","redirct:/do/tools/submissionHistory/");
		  forwards.put("default","/tools/viewCensusDetails.jsp");
          forwards.put("sort","/tools/viewCensusDetails.jsp");
          forwards.put("filter","/tools/viewCensusDetails.jsp");
          forwards.put("reset","/tools/viewCensusDetails.jsp");
          forwards.put("page","/tools/viewCensusDetails.jsp");
          forwards.put("print","/tools/viewCensusDetails.jsp");
          forwards.put("submit","redirect:/do/tools/submissionHistory/");
          forwards.put("lockError","redirect:/do/tools/submissionHistory/");
          forwards.put("history","redirect:/do/tools/submissionHistory/");
          forwards.put("tools","redirect:/do/tools/toolsMenu/");
          forwards.put("afterDiscard","redirect:/do/tools/viewCensusDetails/");
	}
	
	

	/**
	 * Constructor.
	 */
	public ViewCensusDetailsController() {
		super(ViewCensusDetailsController.class);
	}

	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
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
    
 

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.pension.ps.web.report.BaseReportForm, com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

        UserProfile user = getUserProfile(request);
		if (!user.isAllowedUploadSubmissions() && !user.getRole().isTPA()) {
			throw new SystemException(
					"User is not granted download permission.");
		}

		StringBuffer buffer = new StringBuffer();
		
		CensusSubmissionReportData reportData = (CensusSubmissionReportData)report;
        CensusDetailsHelper helper = new CensusDetailsHelper();
        helper.setUserInfo(SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal()));
        
		int contractNumber = reportData.getContractNumber();
		buffer.append(DOWNLOAD_COLUMN_HEADING);
        
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			CensusSubmissionItem theItem = (CensusSubmissionItem) iterator.next();
            
            buffer.append("cens.d").append(COMMA);
			buffer.append(contractNumber).append(COMMA);
			// get the UserProfile
			UserProfile userProfile = getUserProfile(request);
			if (userProfile.getRole().isTPA()
					&& !userProfile.isAllowedDownloadReport()) {
	        	buffer.append(SSNRender.format(theItem.getSsn(), null, true)).append(COMMA);
	        }else{
	        	buffer.append(SSNRender.format(theItem.getSsn(), null, false)).append(COMMA);
	        }
            
            if (theItem.getFirstName() != null)
                buffer.append(theItem.getFirstName());
            buffer.append(COMMA);
            
            if (theItem.getLastName() != null)
                buffer.append(theItem.getLastName());
            buffer.append(COMMA);
            
            if (theItem.getMiddleInitial() != null)
                buffer.append(theItem.getMiddleInitial());
            buffer.append(COMMA);
            
            if (theItem.getNamePrefix() != null)
                buffer.append(theItem.getNamePrefix());
            buffer.append(COMMA);	
            
            if (theItem.getEmployeeNumber() != null && theItem.getEmployeeNumber().trim().length() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i=0; i<(9-theItem.getEmployeeNumber().trim().length());i++) {
                    sb.append("0");
                }
                buffer.append(sb.toString());
                buffer.append(theItem.getEmployeeNumber().trim());
            }
            buffer.append(COMMA);
			
			if (theItem.getAddressLine1() != null) 
				buffer.append(escapeField(theItem.getAddressLine1().trim()));
			buffer.append(COMMA);
			
			if (theItem.getAddressLine2() != null) 
				buffer.append(escapeField(theItem.getAddressLine2().trim()));
			buffer.append(COMMA);
			
			if (theItem.getCity() != null) 
				buffer.append(escapeField(theItem.getCity().trim()));
			buffer.append(COMMA);

			if (theItem.getStateCode() != null) 
				buffer.append(escapeField(theItem.getStateCode().trim()));
			buffer.append(COMMA);			

            if (theItem.getZipCode() != null) {
                String zipCode = StringUtils.trim(theItem.getZipCode());
                if (zipCode.length() > 0) {
                	buffer.append(zipCode.toUpperCase());
                }
            }
            buffer.append(COMMA);
							
			if (theItem.getCountry() != null)
				buffer.append(escapeField(theItem.getCountry().trim()));
            buffer.append(COMMA);
            
            if (theItem.getStateOfResidence() != null)
                buffer.append(escapeField(theItem.getStateOfResidence().trim()));
            buffer.append(COMMA);
            
            if (theItem.getEmployeeProvidedEmail() != null)
                buffer.append(escapeField(theItem.getEmployeeProvidedEmail().trim()));
            buffer.append(COMMA);
            
            if (theItem.getDivision() != null)
                buffer.append(escapeField(theItem.getDivision().trim()));
            buffer.append(COMMA);
            
            buffer.append(CensusDetailsHelper.formatDateForWeb(theItem.getBirthDate())).append(COMMA);
            buffer.append(CensusDetailsHelper.formatDateForWeb(theItem.getHireDate())).append(COMMA);
            
            if (theItem.getEmployeeStatus() != null)
                buffer.append(escapeField(theItem.getEmployeeStatus().trim()));
            buffer.append(COMMA);
            
            buffer.append(CensusDetailsHelper.formatDateForWeb(theItem.getEmployeeStatusDate())).append(COMMA);
            
            if (theItem.getEligibilityIndicator() != null)
                buffer.append(escapeField(theItem.getEligibilityIndicator().trim()));
            buffer.append(COMMA);
            
            buffer.append(CensusDetailsHelper.formatDateForWeb(theItem.getEligibilityDate())).append(COMMA);
            
            if (theItem.getOptOutIndicator() != null)
                buffer.append(escapeField(theItem.getOptOutIndicator().trim()));
            buffer.append(COMMA);

            if (theItem.getPlanYTDHoursWorked() != null)
                buffer.append(escapeField(NumberRender.formatByType(theItem.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
            buffer.append(COMMA);  
            
            buffer.append(escapeField(helper.getPlanYTDCompensation(reportData, theItem, user, false, false))).append(COMMA);
            buffer.append(CensusDetailsHelper.formatDateForWeb(theItem.getPlanYTDHoursWorkedEffDate())).append(COMMA);
            
            //buffer.append(escapeField(NumberRender.formatByType(theItem.getPreviousYearsOfService(), "", RenderConstants.DECIMAL_TYPE))).append(COMMA);
            buffer.append(escapeField(helper.getAnnualBaseSalary(reportData, theItem, user, false, false))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByPattern(theItem.getBeforeTaxDeferralPerc(), "", "###.000", 3, BigDecimal.ROUND_DOWN))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByPattern(theItem.getDesigRothDeferralPerc(), "", "###.000", 3, BigDecimal.ROUND_DOWN))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByType(theItem.getBeforeTaxDeferralAmt(), "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByType(theItem.getDesigRothDeferralAmt(), "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}


	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return CensusSubmissionReportData.REPORT_ID;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return CensusSubmissionReportData.REPORT_NAME;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		// get the user profile object 
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(
				CensusSubmissionReportData.FILTER_CONTRACT_NO,
				new Integer(currentContract.getContractNumber())
		);
		
		ViewCensusDetailsForm theForm = (ViewCensusDetailsForm) form;
		
		Integer id = new Integer(Integer.parseInt(theForm.getSubNo()));
		criteria.addFilter(CensusSubmissionReportData.FILTER_SUBMISSION_NO,id);
	}
    
	 
	public String doCommon(BaseReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
        final String submissionNumber = ((ViewCensusDetailsForm)actionForm).getSubNo();
        if (StringUtils.isNotBlank(submissionNumber)) {
            
            FunctionalLogger.INSTANCE.log("View Submission - Census", getUserProfile(request), submissionNumber, getClass(), getMethodName( actionForm, request));

        }
        
        return super.doCommon( actionForm, request, response);
        
    }
	
    public String doFilter (ViewCensusDetailsForm actionForm, HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		  
    	String forward=super.doFilter( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	
	   }
	
	   @RequestMapping(value ="/viewCensusDetails/", params={"task=page"}, method =  {RequestMethod.GET}) 
	   public String doPage (@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	        	}
	        }
		   String forward=super.doPage( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }

	   @RequestMapping(value ="/viewCensusDetails/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	   public String doSort (@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	        	}
	        }
		   String forward=super.doSort( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }

	   @RequestMapping(value ="/viewCensusDetails/" ,params={"task=print"}, method =  {RequestMethod.GET}) 
	   public String doPrint (@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
		   if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	        	}
	        }
	   
		   String forward=super.doPrint( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	   }
	
	
	@RequestMapping(value ="/viewCensusDetails/", method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }

	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}

        int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber();
        boolean isEditMode = ((ViewCensusDetailsForm)actionForm).isEditMode();
        
        // get the UserProfile
		UserProfile userProfile = getUserProfile(request);

        // validate that the user has the required permissions for this action
		if (!(userProfile.isSubmissionAccess() || userProfile.isInternalUser() || 
			 (isEditMode && userProfile.isAllowedUploadSubmissions())) ) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.EMPLOYEE_INVALID_PERMISSION));
            setErrorsInRequest(request, errors);
			return forwards.get(TOOLS_ACTION);
		}
        
		// check if discard action
		if (DISCARD_ACTION.equals(((ViewCensusDetailsForm)actionForm).getAction())) {
			return doDiscard(actionForm,bindingResult,request,response);
		}
	
		// check if submit action
		if (SUBMIT_ACTION_LABEL.equals(((ViewCensusDetailsForm)actionForm).getAction())) {
			return doSubmit(actionForm,bindingResult,request,response);
		}

		// get the submissionhistoryitem
		SubmissionHistoryItem submissionCase = null;
		try {
			int submissionNumber = Integer.parseInt(((ViewCensusDetailsForm)actionForm).getSubNo());
            // submissionCase can be of Address Type or Census Type
			submissionCase = SubmissionServiceDelegate.getInstance().getSubmissionCase(submissionNumber, contractNumber, ADDRESS_TYPE);
            if (submissionCase == null) {
                submissionCase = SubmissionServiceDelegate.getInstance().getSubmissionCase(submissionNumber, contractNumber, CENSUS_TYPE);
            }
		} catch (NumberFormatException nfe) {
			// the submission number is not supplied - must be a bad bookmark
			// return the user to the submission history page
			return forwards.get(HISTORY_ACTION);
		}
		
		if (submissionCase == null) {
			// case not found
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get( HISTORY_ACTION);
		}
        
        boolean isLocked = !submissionCase.isLockAvailable(String.valueOf(userProfile.getPrincipal().getProfileId()));
        // if user clicked on Edit button, but submission is locked by another user,
        // display message on submission history page
        if (isEditMode && isLocked) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_CASE_LOCKED));
            setErrorsInSession(request, errors);
            return forwards.get( HISTORY_ACTION);
        }
		
		// check if the user has permission to even view this submission
		String profileId = (new Long(userProfile.getPrincipal().getProfileId())).toString();
		if (!userProfile.isAllowedToViewAllSubmissions() && !profileId.equals(submissionCase.getSubmitterID())) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.ADDRESS_INVALID_PERMISSION));
            setErrorsInRequest(request, errors);
			return forwards.get(TOOLS_ACTION);
		}
		
		// check if the submission case has status code 99 (cancelled), in which case we want to immediately redirect back to the submission history page
		if (CANCELLED_STATUS.equals(submissionCase.getSystemStatus())) {
			Collection cancelledError = new ArrayList(1);
			cancelledError.add(new ValidationError("CANCELLED", ErrorCodes.SUBMISSION_CANCELLED));
			setErrorsInSession(request,cancelledError);
			return forwards.get(SUBMIT_ACTION);
		}

		// validate
		SubmissionHistoryItemActionHelper actionHelper = SubmissionHistoryItemActionHelper.getInstance();
		boolean isEditAllowed = actionHelper.isEditAllowed(submissionCase,userProfile);
		boolean isViewAllowed = actionHelper.isViewAllowed(submissionCase,userProfile);
        
        // check if download allowed
        TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractNumber);
        boolean isDownloadAllowed = (  (userProfile.isInternalUser() && userProfile.isAllowedUpdateCensusData()) || 
                                    (userProfile.getRole().isExternalUser() && !userProfile.getRole().isTPA() && 
                                     userProfile.isAllowedDownloadReport()) ||
                                    (userProfile.getRole().isExternalUser() && userProfile.getRole().isTPA())
                                 );
       
		
		// if edit is not allowed, but somehow we managed to get into edit mode (e.g., the user manually entered the URL or bookmarked the page)
		// switch to view mode (if allowed)
		if ((!isEditAllowed && isEditMode)) {
			if (isViewAllowed) {
				isEditMode = false;
				((ViewCensusDetailsForm)actionForm).setMode(VIEW_MODE);
			} else {
				// view not allowed either - kick back to submission history page
				return forwards.get(SUBMIT_ACTION);
			}
		}
		
		// if locked only
		if (submissionCase.isLocked() ){
			// if the case is locked,  display who has the lock
			Lock lock = SubmissionServiceDelegate.getInstance().checkLock(submissionCase,true);
			submissionCase.setLock(lock);
		}
		
		// place the submission history item in the session
		request.getSession(false).setAttribute(Constants.SUBMISSION_CASE_DATA,submissionCase);
        
        String forward = doFilter( actionForm, request, response);
        
		// create the census details helper and place it in the session
		CensusDetailsHelper helper = (CensusDetailsHelper)request.getSession(false).getAttribute(Constants.CENSUS_DETAILS_HELPER);
		if (helper == null) {
			helper = new CensusDetailsHelper();
            helper.setUserInfo(SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal()));
		}
        
        CensusSubmissionReportData reportData = (CensusSubmissionReportData)request.getAttribute(Constants.REPORT_BEAN);
        
        helper.setErrorFlag(SubmissionErrorHelper.getNumberOfErrorsAndEditableWarnings(reportData.getErrors()) > 0);
		helper.setLocked(isLocked);
		helper.setPermissable(isEditAllowed);
		helper.setEditMode(isEditMode);
        helper.setAllowedToDownload(isDownloadAllowed);
        
		request.getSession(false).setAttribute(Constants.CENSUS_DETAILS_HELPER,helper);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	public String doDiscard (ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDiscard");
		}

		// clean up
		((ViewCensusDetailsForm)actionForm).setAction("");

		UserProfile profile = getUserProfile(request);
		ViewCensusDetailsForm form = (ViewCensusDetailsForm)actionForm;
		int submissionId = Integer.parseInt(form.getSubNo());
		int contractId = profile.getCurrentContract().getContractNumber();
		
		// invoke DataChecker service to remove syntax error files
		try {
			DataCheckerServiceDelegate.getInstance().deleteAllSyntacticProblemFiles(submissionId, contractId, CENSUS_TYPE);
		} catch (UnableToAccessDataCheckerException ex) {
			Collection errors = new ArrayList();
			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,profile.getPrincipal().getProfileId(),profile.getPrincipal().getUserName(),ex);
			errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER_FOR_DISCARD,
					new String[] {Integer.toString(submissionId)} ));
			setErrorsInSession(request, errors);
			return forwards.get(HISTORY_ACTION);
		}	

		// set the flag to 'N' regardless of whether files were successfully deleted
		SubmissionServiceDelegate.getInstance().discardCensusRecordsWithSyntaxErrors(submissionId,contractId,String.valueOf(profile.getPrincipal().getProfileId()));
		
		String forward = forwards.get(AFTER_DISCARD_ACTION);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDiscard");
		}

		// add a message to be displayed to the user indicating that records were discarded
		Collection errors = new ArrayList();
		errors.add(new GenericException(ErrorCodes.ADDRESS_DISCARDED_MESSAGE));
		setErrorsInSession(request, errors);

		return forward;
	}
	public String doSubmit (ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }

	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSubmit");
		}
		
		// clean up
		((ViewCensusDetailsForm)actionForm).setAction("");
		request.getSession(false).removeAttribute(Constants.CENSUS_DETAILS_HELPER);
		
		// recompute the case level process status code
		
		String forward = forwards.get(SUBMIT_ACTION);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSubmit");
		}
		return forward;
	}
	
	@RequestMapping(value ="/viewCensusDetails/", params={"task=reset"}  , method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }

   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doReset");
        }
        
        // clean up
        ((ViewCensusDetailsForm)actionForm).setAction("");
        ((ViewCensusDetailsForm)actionForm).clear();
              
        String forward = doCommon( actionForm, request, response);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }
        return forward;
    }
    
	private String escapeField(String field)
	{
		if(field.indexOf(",") != -1 )
		{
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		}
		else
		{
			return field;
		}
	}

	// need to override this method so we can customize the filename
	@RequestMapping(value ="/viewCensusDetails/", params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("viewCensusDetailsForm") ViewCensusDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
             
        	}
        }
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}

		doCommon( actionForm, request, response);

		ReportData report = (ReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		byte[] downloadData = getDownloadData(actionForm, report, request);
		
		StringBuffer filename = new StringBuffer("Census_Submision_for_");
		filename.append(getUserProfile(request).getCurrentContract().getContractNumber());
		filename.append("_for_");
		SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
		String date = formatter.format(new Date());
		filename.append(date);
		filename.append(".csv");

		streamDownloadData(request, response, getContentType(),
				filename.toString(), downloadData);

		/**
		 * No need to forward to any other JSP or action. Returns null will make
		 * Struts to return controls back to server immediately.
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}
		return null;
	}
    
    /**
     * Populates the empty report form with default parameters
     */
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
                || task.equals(DOWNLOAD_TASK)) {
            reportForm.setPageNumber(1);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportForm");
        }
    }
    
    /**
     * Populate sort criteria in the criteria object using the given FORM. 
     * Default sort:
     * - all records with errors
     * - all records with warnings
     * - clean records
     * 
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            if (!form.getSortField().equals(getDefaultSort())) {
                criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
            }
        } 
    }
    
    /**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations .
	 * 
	 */

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
