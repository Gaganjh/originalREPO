package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.submission.valueobject.AddParticipantReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipant;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;


/**
 * 
 * @author Jim Adamthwaite
 * 
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"addParticipantForm"})

public class AddParticipantController extends ReportController {
	
	
	@ModelAttribute("addParticipantForm") 
	public AddParticipantForm populateForm() {
		return new AddParticipantForm();
		}	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tools/addParticipant.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");	
		forwards.put("default","/tools/addParticipant.jsp");
		forwards.put("refresh","/tools/addParticipant.jsp");
		forwards.put("sort","/tools/addParticipant.jsp");
		forwards.put("cancel","redirect:/do/tools/editContribution/");
		forwards.put("edit","redirect:/do/tools/editContribution/");
		forwards.put("home","redirect:/do/tools/toolsMenu/");
		forwards.put("history","redirect:/do/tools/submissionHistory/");
		forwards.put("createParticipant","redirect:/do/tools/editContribution/");
				}
	
    
	private static String DEFAULT_SORT = AddableParticipant.SORT_NAME;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static String FORWARD_TO_EDIT = "edit";
	private static String FORWARD_TO_HOME = "home";
	private static final String DEFAULT = "default";
	private static final String REFRESH = "refresh";
	private static final String TOOLS = "tools";
	private static final String HISTORY = "history";
	private static final String EMPLOYEE_ID_SORT_OPTION = "EE";
	private static final String DASH = "-";
	private static final String COPIED_STATUS = "97";
	private static final String DRAFT_STATUS = "14";

	/**
	 * Constructor.
	 */
	public AddParticipantController() {
		super(AddParticipantController.class);
	}

	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return AddParticipantReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return AddParticipantReportData.REPORT_NAME;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateReportCriteria");

		AddParticipantForm sessionForm 
		= (AddParticipantForm)request.getSession(false).getAttribute("addParticipantForm");		

		String temp = request.getParameter("sortField");
		if (temp != null && temp.length()!=0) sessionForm.setSortField(temp);	

		temp = request.getParameter("sortDirection");
		if (temp != null && temp.length()!=0) sessionForm.setSortDirection(temp);	

		// get the user profile object 
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = null;
		if (userProfile != null) {
			currentContract = userProfile.getCurrentContract();
		}
		if (currentContract == null) {
			throw new SystemException("UserProfile not set up correctly for AddParticipantAction");
		}
		AddParticipantForm addParticipantForm = (AddParticipantForm) form;
		addParticipantForm.clear( request);

		criteria.addFilter(
				AddParticipantReportData.FILTER_FIELD_1,
				new Integer(currentContract.getContractNumber())
		);
		
		// we expect the tracking number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0) 
			subNo = addParticipantForm.getSubNo();
		if (subNo == null || subNo.length() == 0) 
			subNo = (String)request.getSession().getAttribute("subNo");
		if (subNo == null) {
			throw new SystemException("subNo not found for AddParticipantAction");
		}

		addParticipantForm.setSubNo(subNo);
		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_2,
				new Integer(addParticipantForm.getSubNo())
		);

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateReportCriteria");

	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#doCommon(org.apache.struts.action.ActionMapping, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 
	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> doCommon");

		AddParticipantForm actionForm =(AddParticipantForm)reportForm;

        // lets check the permissions
		UserProfile userProfile = getUserProfile(request);
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}

		String contractStatus = userProfile.getCurrentContract().getStatus();
		if(userProfile.isSubmissionAccess() && 
		        userProfile.isAllowedUploadSubmissions() && 
				!Contract.STATUS_CONTRACT_DISCONTINUED.equals(contractStatus) 
		) {
			// okay, user has access
		} else {
			return  forwards.get(HISTORY);
		}

		// we expect the tracking number to be present in the request.
		String subNo = request.getParameter("subNo");
		if (subNo == null || subNo.length() == 0) 
			subNo = actionForm.getSubNo();
		if (subNo == null || subNo.length() == 0) 
			subNo = (String)request.getSession().getAttribute("subNo");
		// due to some wierd bookmarking this may acually not be found, so send user to submission history page
		if (null == subNo) {
			return  forwards.get(HISTORY);
		}

		String forward = super.doCommon( actionForm, request, response);
		AddParticipantReportData theReport = (AddParticipantReportData)request.getAttribute(Constants.REPORT_BEAN);
		actionForm.setTheReport(theReport);
		
		// if the lock is active, refresh it; 
		// othewise we shouldn't be here, so return to submission history 
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(theReport,true);
		if (lock != null && lock.isActive()) {
			theReport.setLock(lock);
			LockManager.getInstance(request.getSession(false)).refresh(theReport,String.valueOf(userProfile.getPrincipal().getProfileId()));
		} else {
			forward = FORWARD_TO_HOME;
		}
		
		// edit isn't allowed for copied status, but 
		// assuming we're already in edit, change the
		// status to draft for the purpose of this check
		String systemStatus = theReport.getSystemStatus();
		if (systemStatus.equals(COPIED_STATUS)) {
			systemStatus = DRAFT_STATUS;
		}
		boolean isEditAllowed = SubmissionHistoryItemActionHelper.getInstance().isEditAllowed("C", 
				systemStatus, true, userProfile);

		if (!isEditAllowed) {
			forward = forwards.get(FORWARD_TO_HOME);
		}

		////TODO saveToken(request);
		
		// if all participants are already selected, set the create participant indicator
		// in the form on so the jsp will add a blank entry to the top of the participant list
		int includedCount = 0;
		Integer row = new Integer(0);
		for (Iterator itr = theReport.getDetails().iterator(); itr.hasNext(); ) {
			AddableParticipant participant = (AddableParticipant)itr.next();
			if (null == participant.getIncludedInSubmission()) {
				actionForm.getAddBoxes().add(row, true);
				includedCount++;
			} else {
				actionForm.getAddBoxes().add(row, false);
			}
			row = (new Integer(row.intValue() + 1));
		}
		if (includedCount == theReport.getDetails().size()) {
			actionForm.setCreateIndicator("Y");
		}	
		actionForm.storeClonedForm();

		if (logger.isDebugEnabled())
			logger.debug("exit <- doCommon");
		
		return forward; 
	}

	/**
	 * @see ReportController#getPageSize(HttpServletRequest)
	 */
	protected int getPageSize(HttpServletRequest request) {
		// this page displays as a single list of participants
		return ReportCriteria.NOLIMIT_PAGE_SIZE;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		//no-op - this report isn't downloadable
		
		return null;
	}
	@RequestMapping(value ="/addParticipant/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
	   String forward=super.doFilter( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
			
	@RequestMapping(value ="/addParticipant/",params={"action=filter"}  , method =  {RequestMethod.POST}) 
	public String doFilter (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		 String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
			
	@RequestMapping(value ="/addParticipant/", params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		 String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	@RequestMapping(value ="/addParticipant/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		 String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	@RequestMapping(value ="/addParticipant/",params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		 String forward=super.doFilter( actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	
	
	/**
	 * Performs the save action.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return @throws
	 *         SystemException
	 */
	@RequestMapping(value ="/addParticipant/" ,params={"task=save"} ,method ={RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("addParticipantForm") AddParticipantForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	
		if (logger.isDebugEnabled())
			logger.debug("entry -> doSave");

		// validate and add the participants sent in the request
		String forward = addParticipants( actionForm, request, response, FORWARD_TO_EDIT);
		
		////TODO resetToken(request);
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- doSave");
			
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	/**
	 * Performs the add selected and refresh page with empty create participant row.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * @throws ServletException
	 */
	@RequestMapping(value ="/addParticipant/",params={"task=insertParticipant"},method ={RequestMethod.POST}) 
	public String doInsertParticipant (@Valid @ModelAttribute("addParticipantForm") AddParticipantForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("ïnput");//if input forward not //available, provided default
        	}
        }
	
		if (logger.isDebugEnabled())
			logger.debug("entry -> doInsertParticipant");

		// validate and add the participants sent in the request
		String forward = addParticipants(form, request, response, REFRESH);

		AddParticipantForm theForm = (AddParticipantForm) form;

		// if add was successful, refresh data from database for added participants
		if (forward.equals(REFRESH)) {
			forward = DEFAULT;
			doCommon( form, request, response);
			// tell the page to display with the create participant row displayed with empty data
			theForm.setCreateIndicator("Y");
			theForm.setCreateParticipantId("");
			theForm.setCreateParticipantName("");
		}
		
		//set the tracking number into the session so we can return to the same edit contribution page
		request.getSession().setAttribute("subNo", theForm.getSubNo());
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- doInsertParticipant");
			
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	private String addParticipants( BaseReportForm form,
			HttpServletRequest request, HttpServletResponse response, String defaultForward)
			throws SystemException {
		/*
		 * Compare the token and make sure it's still valid. 
		 * Token is initialized in the doCommon() method and
		 * reset later in this method.
		 */
		/*if (!isTokenValid(request)) {
			return TOOLS;
		}*/
		
		Collection errors = new ArrayList();
		AddParticipantForm theForm = (AddParticipantForm) form;
		AddParticipantReportData reportData = theForm.getTheReport();
		
		UserProfile userProfile = getUserProfile(request);
		String userId = String.valueOf(userProfile.getPrincipal().getProfileId());
		
		// find out which participants to add
		List addBoxes = theForm.getAddBoxes();
		ArrayList<AddableParticipant> participantsToAdd = new ArrayList<AddableParticipant>();
		Integer row = new Integer(0);
		for (Iterator itr = theForm.getTheReport().getDetails().iterator(); itr.hasNext(); ) {
			AddableParticipant participant = (AddableParticipant)itr.next();
			boolean addFlag = (boolean) addBoxes.get(row);
			if (addFlag &&  null != participant.getIncludedInSubmission()) {
				participantsToAdd.add(participant);
			}
			row = new Integer(row.intValue() + 1);
		}
		
		String newName = theForm.getCreateParticipantName();
		String newIdentifier = theForm.getCreateParticipantId();
		if (null != newIdentifier && !newIdentifier.equals("") && !EMPLOYEE_ID_SORT_OPTION.equals(theForm.getTheReport().getParticipantSortOption())) {
			newIdentifier = stripDashes(newIdentifier);
		}	
		String newSelected = request.getParameter("newSelected");
		if (null != newSelected && newSelected.equals("1")) {
			if (null != newName && !newName.equals("") && null != newIdentifier && !newIdentifier.equals("")) {
				AddableParticipant newParticipant = new AddableParticipant(newIdentifier, newName, Boolean.TRUE);
				for (Iterator pitr = theForm.getTheReport().getDetails().iterator(); pitr.hasNext(); ) {
					AddableParticipant participant = (AddableParticipant)pitr.next();
					if (participant.getIdentifier().equals(newIdentifier)
							&& !participant.getName().equals(newName)) {
						errors.add(new GenericException(
								ErrorCodes.DUPLICATE_PARTICIPANT_IDENTIFIER));
						
						/*
						 * Errors are stored in the session so that our REDIRECT can look up the
						 * errors.
						 */
						SessionHelper.setErrorsInSession(request, errors);
						theForm.setCreateIndicator("Y");
						form = theForm;
						return DEFAULT;
						
					}
				}
				if (newName.matches(".*\\d+.*")) {
					errors.add(new GenericException(
							ErrorCodes.PARTICIPANT_NAME_IS_ALPHA_NUMERIC));

					/*
					 * Errors are stored in the session so that our REDIRECT can
					 * look up the errors.
					 */
					SessionHelper.setErrorsInSession(request, errors);
					theForm.setCreateIndicator("Y");
					form = theForm;
					return DEFAULT;

				}
				participantsToAdd.add(newParticipant);
			} else {
				throw new SystemException(this.getClass().getName() + ".addParticipants() - Problem occurred with null identifier or name trying to add new participant for " 
						+ " submissionId: " + theForm.getTheReport().getSubmissionId() + " and contract:  " + theForm.getTheReport().getContractNumber());
			}
		}
		
		// if any to add, ask the submission service delegate to do so
		if (participantsToAdd.size() > 0) {
			SubmissionServiceDelegate.getInstance().addPartcipantsToContribution(
					theForm.getTheReport().getSubmissionId().intValue(), 
					theForm.getTheReport().getContractNumber(), 
					userId,
					participantsToAdd);
		}
		
		//set the tracking number into the session so we can return to the same edit contribution page
		request.getSession().setAttribute("subNo", theForm.getSubNo());
		
		return defaultForward;
		
	}

	private String stripDashes(String ssnWithDashes) {
		StringBuffer ssnWithoutDashes = new StringBuffer();
		for (int i = 0; i < ssnWithDashes.length(); i++) {
			if (!ssnWithDashes.substring(i, i+1).equals(DASH)) {
				ssnWithoutDashes.append(ssnWithDashes.charAt(i));
			}
		}
		return ssnWithoutDashes.toString();
	}


    // override the super class's implementation
    // Use BeanUtils to ignore the index property copy problem
    protected BaseReportForm resetForm( BaseReportForm reportForm, HttpServletRequest request) throws SystemException {
        try {
            BaseReportForm blankForm = (BaseReportForm) reportForm
                    .getClass().newInstance();
            BeanUtils.copyProperties(reportForm, blankForm);
           // reportForm.reset(request);
        } catch (Exception e) {
            throw new SystemException(e, this.getClass().getName(),
                    "resetForm", "exception in resetting the form");
        }

        return reportForm;
    }
    
    /**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations .
	 * 
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}