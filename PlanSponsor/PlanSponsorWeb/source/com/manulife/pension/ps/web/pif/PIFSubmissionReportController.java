package com.manulife.pension.ps.web.pif;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.service.pif.report.valueobject.PIFSubmissionReportData;
import com.manulife.pension.service.pif.util.PIFConstants;
import com.manulife.pension.service.pif.util.PIFConstants.ProcessStatus;
import com.manulife.pension.service.pif.valueobject.PlanInfoSubmissionHistoryVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;

/**
 * This Action class handles plan information submission report page related activities
 * 
 * @author Vivek Lingesan
 */
@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"pifSubmissionReportForm","pifDataForm","deletePIFDataForm"})

public class PIFSubmissionReportController extends ReportController {

	@ModelAttribute("pifSubmissionReportForm") 
	public PIFSubmissionReportForm populateForm()
	{
		return new  PIFSubmissionReportForm();
	}

	@ModelAttribute("pifDataForm") 
	public PIFDataForm populatePifDataForm() 
	{
		return new PIFDataForm();
		}
	
	@ModelAttribute("deletePIFDataForm")
	public DeletePIFDataForm populateDeletePIFDataForm() 
	{
		return new DeletePIFDataForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	public static final String PLAN_SUBMISSION = "/contract/pic/plansubmission.jsp";
	static{
		forwards.put("input", PLAN_SUBMISSION);
		forwards.put("default",PLAN_SUBMISSION);
		forwards.put("sort", PLAN_SUBMISSION);
		forwards.put("print", PLAN_SUBMISSION);
		forwards.put("page", PLAN_SUBMISSION );
		forwards.put("error", PLAN_SUBMISSION ); 
		forwards.put("lock","/WEB-INF/contract/pic/pifLockMessage.jsp");
	}

	
	private static final String SHOW_LINK = "showLink";
	private static final int SUBMISSION_PAGE_SIZE = 25;
    protected static final String VALIDATE_LOCK = "lock";
    protected static final String SUBMISSION_ID = "submissionId";
    protected static final String REFRESH_ID = "id";
	private static final String SUBMITTED_STATUS = "I1";    
	private String contractStatus = "N";
	
	
	/**
	 * Constructor.
	 */
	public PIFSubmissionReportController() {
		super(PIFSubmissionReportController.class);
	}

	/**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger.getLogger(PIFSubmissionReportController.class);
    
	/**
	 * Overwritten method of superclass
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
    @RequestMapping(value ="/pic/plansubmission/",  method = {RequestMethod.GET,RequestMethod.POST}) 
    public String doDefault(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {

    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
    	
    	
    	String forward = null;
    	List<GenericException> errors = new ArrayList<GenericException>();  	
    	request.setAttribute(CommonConstants.REPORT_BEAN, new PIFSubmissionReportData(null, 0));
        // Check for Error Messages.
    	ReportCriteria criteria = getReportCriteria(getReportId(), actionForm, request);
        boolean isErrorMsgPresent = checkForErrorConditions(criteria, actionForm, request);
        
        if (isErrorMsgPresent) {
            logger.debug("exit -> doCommon() in PIFSubmissionReportAction. Error Messages Found.");
            request.setAttribute(CommonConstants.IS_ERROR, true);
            return forwards.get("input");
        }
    	try {
    		forward = super.doCommon( actionForm, request, response);
    		Integer contractId = (Integer) criteria.getFilterValue(Constants.CON_NUMBER);
        	if(contractId != null){
        		request.setAttribute(SHOW_LINK, showAddLink(criteria, request, contractId));
        	}
        } catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(CommonConstants.PS_APPLICATION_ID,e);
			// Show user friendly message.
			errors.add(new GenericException(CommonErrorCodes.TECHNICAL_DIFFICULTIES));
            request.setAttribute(CommonConstants.IS_ERROR, true);
			setErrorsInRequest(request, errors); 
			forward = forwards.get("input");
		}
        return forwards.get(forward);
    }
    
     @RequestMapping(value = "/pic/plansubmission/", params = {"Submit=search"}, method = {RequestMethod.POST})
	public String doSearch(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
    	 return doDefault(actionForm, bindingResult, request, response);
	}
     
     @RequestMapping(value = "/pic/plansubmission/", params = {"reset=reset"}, method = {RequestMethod.POST})
 	public String doReset(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm,
 			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
 			throws SystemException {
 		return doDefault(actionForm, bindingResult, request, response);
 	}
     
   	@RequestMapping(value = "/pic/plansubmission/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doSort(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
   	
   	@RequestMapping(value = "/pic/plansubmission/", params = {"task=page"}, method = {
			RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPage(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    

	@RequestMapping(value = "/pic/plansubmission/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
																											// input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		String forward = super.doPrint(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    
	/**
	 * This method is used to validate whether the edit page is currently locked by any user or not
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
    @RequestMapping(value ="/pic/plansubmission/", params = {"task=validateLock"}, method = {RequestMethod.GET}) 
    public String doValidateLock (@Valid @ModelAttribute("pifSubmissionReportForm") PIFSubmissionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {


    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
    	
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		long profileId = userProfile.getPrincipal().getProfileId();
		String submissionId = request.getParameter(SUBMISSION_ID);
		
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		if(!StringUtils.isBlank(submissionId)){
			if(!userProfile.isInternalUser()) {
				Lock lock = LockServiceDelegate.getInstance().getLockInfo(LockHelper.TPA_PLAN_LOCK_NAME,
	                LockHelper.TPA_PLAN_LOCK_NAME + submissionId);
				// If any one has lock on this submission id, and the user id different 
		        if (lock != null) {
		        	if(lock.getLockUserProfileId() != profileId) {
		        		
		        		boolean lockResult = obtainLock(Integer.valueOf(submissionId), request);
		        		if(!lockResult){
			        		String userName = ContractServiceDelegate.getInstance().getUserName(
			        				lock.getLockUserProfileId());
			        		Object[] params = {userName};
			        		GenericException exception = new GenericException(ErrorCodes.DRAFT_IS_CURRENTLY_BEING_EDITED,params);
			                errorMessages.add(exception);		        			
		        		}
		        	}
	        		
	        	}
	        	
	        }
		}	
        if (errorMessages.size() > 0) {
            request.setAttribute(Environment.getInstance().getErrorKey(), errorMessages);
        }
        return forwards.get(VALIDATE_LOCK);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	@Override
	protected String getDefaultSort() {
        return PIFSubmissionReportData.DEFAULT_SORT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDownloadData()
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	@Override
	protected String getReportId() {
		return PIFSubmissionReportData.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	@Override
	protected String getReportName() {
		return PIFSubmissionReportData.REPORT_NAME;
	}
	
	/**
	 * This method is called to populate a report criteria from the report
	 * action form and the request. It is called right before getReportData is
	 * called.
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportController#populateReportCriteria
	 * (com.manulife.pension.service.report.valueobject.ReportCriteria,
	 * com.manulife.pension.ps.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		PIFSubmissionReportForm submissionReportForm = 
			(PIFSubmissionReportForm) reportForm;
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
        if (userProfile == null) {
            throw new SystemException("UserProfile is null");
        }
        
        addUserProfileRelatedFilterCriteria(userProfile, criteria, submissionReportForm, request);
        if(request.getParameter(REFRESH_ID) != null){
        	submissionReportForm.resetForm();
        }
	    // Contract number filter
        if (!StringUtils.isBlank(submissionReportForm.getContractNumber())) {
           if (StringUtils.isNumeric(submissionReportForm.getContractNumber().trim())) {
        		criteria.addFilter(PIFSubmissionReportData.FILTER_CONTRACT_ID,
        				Integer.valueOf(submissionReportForm.getContractNumber().trim()));
            }
        }
		// Submission Status filter
		criteria.addFilter(PIFSubmissionReportData.FILTER_SUBMISSION_STATUS,
				submissionReportForm.getSubmissionStatus());
		
		// Contract name filter
		criteria.addFilter(PIFSubmissionReportData.FILTER_CONTRACT_NAME,
				submissionReportForm.getContractName());
		//Site location - either US or NY
		criteria.addFilter(PIFSubmissionReportData.FILTER_SITE_LOCATION, Environment
                .getInstance().getDBSiteLocation());
		// task filter
		String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        criteria.addFilter(PIFSubmissionReportData.FILTER_TASK, task);
	}
	
    /**
     * Add userProfile specific filter criteria such as UserProfileID, userRole.
     * @param userProfile
     * @param criteria
     * @param reportForm
     * @param request
     * @throws SystemException
     */
    private void addUserProfileRelatedFilterCriteria(UserProfile userProfile,
            ReportCriteria criteria, PIFSubmissionReportForm reportForm, HttpServletRequest request)
            throws SystemException {

        logger.debug("entry -> addUserProfileRelatedFilterCriteria()");
        
        String task = getTask(request);
        
        //Validate against print task, if not, set the submission page size. issue no.102
        if (task.equals(PRINT_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        }else{
        	criteria.setPageSize(SUBMISSION_PAGE_SIZE);
        }
        if (userProfile.isInternalUser()) {
            // Internal User Profile ID
            String internalUserProfileID = getFilterValue(PIFSubmissionReportData.FILTER_USER_PROFILE_ID,
                    reportForm, userProfile, request);
            if (internalUserProfileID != null) {
                addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_USER_PROFILE_ID, Long
                        .valueOf(internalUserProfileID));
            }
            // Internal User Role
            addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_USER_ROLE, getFilterValue(
            		PIFSubmissionReportData.FILTER_USER_ROLE, reportForm, userProfile, request));

            // TPA User Profile ID
            String tpauserProfileID = getFilterValue(PIFSubmissionReportData.FILTER_MIMIC_USER_PROFILE_ID,
                    reportForm, userProfile, request);
            if (tpauserProfileID != null) {
                addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_MIMIC_USER_PROFILE_ID, Long
                        .valueOf(tpauserProfileID));
            }
            // TPA User Role
            addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_MIMIC_USER_ROLE, getFilterValue(
            		PIFSubmissionReportData.FILTER_MIMIC_USER_ROLE, reportForm, userProfile, request));
        } else {
            // TPA User Profile ID
            String userProfileID = getFilterValue(PIFSubmissionReportData.FILTER_USER_PROFILE_ID,
                    reportForm, userProfile, request);
            if (userProfileID != null) {
                addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_USER_PROFILE_ID, Long
                        .valueOf(userProfileID));
            }
            // TPA User Role
            addFilterCriteria(criteria, PIFSubmissionReportData.FILTER_USER_ROLE, getFilterValue(
            		PIFSubmissionReportData.FILTER_USER_ROLE, reportForm, userProfile, request));
        }
        		
        logger.debug("exit -> addUserProfileRelatedFilterCriteria()");
    }
    
    /**
     * This method returns the Filter value submitted by the user, given the filter name.
     * 
     * @param filterID - the filter name
     * @param reportForm - PIFSubmissionReportForm object.
     * @param userProfile - UserProfile object.
     * @param request - the HttpServletRequest object.
     * @return - the filter value.
     * @throws SystemException
     */
    private String getFilterValue(String filterID, PIFSubmissionReportForm reportForm,
            UserProfile userProfile, HttpServletRequest request) {

    	logger.debug("inside getFilterValue()");

    	try {
    		
            if (PIFSubmissionReportData.FILTER_USER_PROFILE_ID.equals(filterID)) {
                return String.valueOf(userProfile.getPrincipal().getProfileId());
            } else if (PIFSubmissionReportData.FILTER_USER_ROLE.equals(filterID)) {
                // If Internal user, return his user Role, else return 'TPA'
                if (userProfile.isInternalUser()) {
                    return userProfile.getRole().getRoleId();
                }
				return Constants.TPA_ROLE;
            } else if (PIFSubmissionReportData.FILTER_MIMIC_USER_PROFILE_ID.equals(filterID)) {
                // Only internal user will call use this Mimic function. Return the TPA UserProfile
                // ID of the user on which the Internal User had clicked in "Search / Select TPA"
                // page.
                if (userProfile.getRole().isInternalUser()) {
        			UserInfo tpaUserInfo=(UserInfo)request.getSession(false).getAttribute(
        					Constants.TPA_USER_INFO);
        			if(tpaUserInfo!= null) {
        				return Long.valueOf(tpaUserInfo.getProfileId()).toString();
        			}
                }
            } else if (PIFSubmissionReportData.FILTER_MIMIC_USER_ROLE.equals(filterID)) {
                // Only internal user will call use this Mimic function.
                if (userProfile.getRole().isInternalUser()) {
                    return Constants.TPA_ROLE;
                }
            }
            //TODO: PA why are we handling the Runtime exception like this?
        } catch (NullPointerException ne) {
            // Do Nothing.
        }
        return null;
    }
    
    /**
     * This method checks for those conditions where we need to display a Error message to the user.
     * The Error conditions could have been checked in doValidate() method, but there are few clean
     * up activities that were not being performed until and unless we come to
     * doDefault()/doCommon() method. Hence, the doValidate() method is not being used to show the
     * Error conditions.
     * 
     * @param criteria
     * @param reportForm
     * @param request
     * @return - boolean value indicating if the error messages need to be shown or not.
     * @throws SystemException 
     */
    private boolean checkForErrorConditions(ReportCriteria criteria, 
    		BaseReportForm reportForm, HttpServletRequest request) throws SystemException {

    	PIFSubmissionReportForm submissionReportForm = 
			(PIFSubmissionReportForm) reportForm;
    	boolean isErrorMsgPresent = false;
    	Integer contractId = (Integer) criteria.getFilterValue(PIFSubmissionReportData.FILTER_CONTRACT_ID);
    	String contractName = (String) criteria.getFilterValue(PIFSubmissionReportData.FILTER_CONTRACT_NAME);
        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
        
        //Validate contract name length >0 & <3
        if(StringUtils.trimToNull(submissionReportForm.getContractName()) != null){
        	if (!isValidContractName(contractName)){
                ValidationError exception = new ValidationError(
                        Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
                errorMessages.add(exception);
            }
        }
        if (!StringUtils.isBlank(submissionReportForm.getContractNumber())) {
            if (!StringUtils.isNumeric(submissionReportForm.getContractNumber().trim())) {
                ValidationError exception = new ValidationError(
                        Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.CONTRACT_NUMBER_INVALID_FORMAT);
                errorMessages.add(exception);
            } 
        }
        if(errorMessages.isEmpty()){
            if(contractId != null || StringUtils.trimToNull(contractName) != null){
            	List<ContractVO> contractVOs = new ArrayList<ContractVO>();
    	    	contractVOs = ContractServiceDelegate.getInstance().getTPAFirmContract(criteria);
    	    	if(CollectionUtils.isNotEmpty(contractVOs)){
    	    		if(contractVOs.size() == 1){
    	    			ContractVO contractVO = contractVOs.get(0);
    	    			//Get contractid from ContractVO, when contractname alone is entered in the filter criteria
    	    			if(contractId == null){
    	    				contractId = contractVO.getContractNumber();
    	    			}
    	    			if(contractId != null){
    	    				if (!isValidTPAContract(contractVO)){
    	    		            ValidationError exception = new ValidationError(
    	    		                    Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.NO_MATCH_FOR_CONTRACT_INFORMATION);
    	    		            errorMessages.add(exception);
    	    		        } else if (!isValidContractStatus(contractVO)){
    	    		            ValidationError exception = new ValidationError(
    	    		                    Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.CONTRACT_NUMBER_INVALID_STATUS);
    	    		            errorMessages.add(exception);
    	    		        } else if (ContractServiceDelegate.getInstance().isDefinedBenefitContract(contractId)){
    	    		            ValidationError exception = new ValidationError(
    	    		                    Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.DB_CONTRACT_NUMBER);
    	    		            errorMessages.add(exception);
    	    		        }
    	    				//Contract name and contract id validation (whether it belongs to same contract or not)
    	    		        else if(contractId != null && (contractName != null && !StringUtils.equals(contractName, PIFConstants.EMPTY))){
    	    		        	if(!isValidContractNameAndId(contractName, contractVO)){
    	    		        		//No errror message should be displayed in this scenario
    	    		        		isErrorMsgPresent = true;
    	    			        }
    	    		        }
    	    				submissionReportForm.setContractId(contractId.toString());
    		    	        criteria.addFilter(Constants.CON_NUMBER, contractId);
    	    			}
    	    		}else{
    	    			 ValidationError exception = new ValidationError(
    		                        Constants.TPA_CONTRACT_NAME_SORT_FIELD, ErrorCodes.CONTRACT_NUMBER_NAME_INVALID);
    		             errorMessages.add(exception);
    	    		}
    	    	}else if(contractVOs == null){
    	            ValidationError exception = new ValidationError(
    	                    Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ErrorCodes.NO_MATCH_FOR_CONTRACT_INFORMATION);
    	            errorMessages.add(exception);
    	    	}
            }        	
        }
        if (!errorMessages.isEmpty()) {
            isErrorMsgPresent = true;
            setErrorsInRequest(request, errorMessages);
        }
        return isErrorMsgPresent;
    }
    
    /**
     * Checks whether the given contract id is valid for TPA & validate the contract status effective date 
     * 		for DI contracts.
     * @param contractVO
     * @return true if it is valid TPA contract
     * @throws SystemException
     */
    private boolean isValidTPAContract(ContractVO contractVO) throws SystemException{
    	boolean validStatus = false;
    	if(contractVO != null){
    		if(StringUtils.equals(contractVO.getContractStatusCode(), PIFConstants.DI_STATUS)){
    			contractStatus = PIFConstants.YES;
				Date currentDate=new Date();  
				Date contractEffectiveDate = contractVO.getContractStatusEffectiveDate();
				Calendar calendarDB = Calendar.getInstance();
				calendarDB.setTime(contractEffectiveDate);
				Calendar calendarCurrent = Calendar.getInstance();
				calendarCurrent.setTime(currentDate);
				calendarDB.add(Calendar.YEAR, 2);
				if(calendarDB.equals(calendarCurrent) || calendarDB.after(calendarCurrent)){
					validStatus = true;
				} else{
					validStatus = false;
				}
			}else{
				validStatus = true;
			}
    	}
    	return validStatus;
    }
    
    /**
     * Checks whether the given contract id is valid for TPA
     * @param contractVO
     * @return true if it is valid TPA contract
     * @throws SystemException
     */
     private boolean isValidContractStatus(ContractVO contractVO) throws SystemException {
    	String contractStatus = contractVO.getContractStatusCode();
    	for(String status : PIFConstants.validContractStatus){
    		if(StringUtils.equals(status, contractStatus)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks whether the given contract id and contract name belongs to same contract.
     * @param contractVO	--	ContractVO object
     * @return	boolean		
     * @throws SystemException
     */
    private boolean isValidContractNameAndId(String contractName, ContractVO contractVO) throws SystemException{
    	
    	if(StringUtils.containsIgnoreCase(contractVO.getContractName().trim(), contractName.trim())){
			return true;
		} else {
			return false;
		}
    }
    
    /**
     * Checks whether the given contract name is valid to search.
     * @param contractVO	--	ContractVO object
     * @return	boolean		
     * @throws SystemException
     */
    private boolean isValidContractName(String contractName) throws SystemException{
    	
    	 if (StringUtils.trimToNull(contractName) != null
                 && StringUtils.trimToNull(contractName).length() < 3) {
             return false;
         }else{
        	 return true;
         }
    }
    
    /**
     * Sets the flag to show or hide the add plan information link
     * @param reportForm
     * @param request
     * @return true if valid to show the link
     */
    @SuppressWarnings("unchecked")
    private boolean showAddLink(ReportCriteria criteria, HttpServletRequest request, Integer contractId) throws SystemException{
    	
    	boolean showFlag = false;    	
    	if(contractId != null){    		
    		ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
    		if(report != null ){
	    		Collection<PlanInfoSubmissionHistoryVO> details = (Collection<PlanInfoSubmissionHistoryVO>)
	    			report.getDetails();
	    		if(CollectionUtils.isNotEmpty(details)){
	    			showFlag = true;
		    		for(PlanInfoSubmissionHistoryVO submissionHistory : details){
		    			if(StringUtils.equals(ProcessStatus.DRAFT.getCode(), 
		    					submissionHistory.getProcessStatusCode())) {
		    				showFlag = false;
		    				break;
		    			}
		    		}
	    		} else {
	    			showFlag = true;
	    		}	
    		} else {
    			showFlag = true;
    		}	
    		
    		//Validate the draft status for the given contract id, while "Submitted" is selected in the 
    		//Submission Status filter
    		String submissionStatus = (String) criteria.getFilterValue(PIFSubmissionReportData.FILTER_SUBMISSION_STATUS);
    		if(submissionStatus != null && StringUtils.equals(submissionStatus, SUBMITTED_STATUS)){
    			if(showFlag){
    				boolean draftInDB = ContractServiceDelegate.getInstance().getDraftStatus(contractId);
	    			 if(draftInDB){
	    				 showFlag = false;
	    			 }
    			}
    		}
    		//Validate the DI Contract Status - No need to display link for DI
    		if(contractStatus.equalsIgnoreCase(PIFConstants.YES)){
    			showFlag = false;
    			contractStatus = PIFConstants.NO;
    		}
    	} 
    	else {
    		showFlag = false;
		}	
    	return showFlag;
    }
    
    /**
	 * @see ReportController#doCommon(ActionMapping, ReportForm, HttpServletRequest, HttpServletResponse)
	 */
   
    public String doCommon ( BaseReportForm actionform,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {

		String forward = null;
		// Check for Error Messages to be displayed in print report
		ReportCriteria criteria = getReportCriteria(getReportId(), actionform, request);
		checkForErrorConditions(criteria, actionform, request);
        try {
			forward = super.doCommon( actionform, request, response);
		} catch (SystemException e) {
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
			// Show user friendly message.
			List errors = new ArrayList();
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			setErrorsInRequest(request, errors);
			forward = forwards.get("input");
		}
		return forward;
	}
	
    /**
     * Attempts to obtain a lock for the specified plan submission.
     * 
     * @param submissionId The plan that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully obtained, false otherwise.
     */
    protected boolean obtainLock(final Integer submissionId, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("obtainLock> Obtaining lock for plan submission [").append(
            		submissionId).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.TPA_PLAN_LOCK_NAME,
                LockHelper.TPA_PLAN_LOCK_NAME + submissionId,
                getUserProfile(request).getPrincipal().getProfileId());
    }
    
    /**
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseController#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}