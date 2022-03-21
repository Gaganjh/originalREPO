package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.common.util.log.OnlineSubmissionEventLog;
import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.DataCheckerServiceDelegate;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessDataCheckerException;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.tools.util.LongTermPartTimeDetailsHelper;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.stp.datachecker.ICon2DataProblem;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.SSNRender;

/**
 *
 * @author bobbave
 *
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"longTermPartTimeDetailsForm"})
public class EditLongTermPartTimeDetailsController extends ReportController {

	@ModelAttribute("longTermPartTimeDetailsForm") 
	public LongTermPartTimeDetailsForm populateForm() {
		return new LongTermPartTimeDetailsForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{forwards.put("input","/tools/viewLongTermPartTimeDetails.jsp"); 
	forwards.put("tools","redirect:/do/tools/toolsMenu/");
	forwards.put("default","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("undo","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("sort","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("filter","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("page","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("print","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("error","/tools/editLongTermPartTimeDetails.jsp");
	forwards.put("addParticipant","redirect:/do/tools/addParticipant");
	forwards.put("refresh","redirect:/do/tools/editLongTermPartTimeDetails/?task=refresh"); 
	forwards.put("cancel","redirect:/do/tools/submissionHistory/");
	forwards.put("editLongTermPartTimeDetails","redirect:/do/tools/editLongTermPartTimeDetails/"); 
	forwards.put("confirm","redirect:/do/tools/editLongTermPartTimeDetails/?task=confirm");
	forwards.put("confirmPage","/tools/editLongTermPartTimeConfirmation.jsp");
	forwards.put("history","redirect:/do/tools/submissionHistory/"); 
	forwards.put("prepareAddParticipant","redirect:/do/tools/editLongTermPartTimeDetails/?task=prepareAddParticipant");
	forwards.put("setReportPageSize","redirect:/do/tools/editLongTermPartTimeDetails/?task=setReportPageSize");
	forwards.put("reset","/tools/editLongTermPartTimeDetails.jsp");}
	
	 public static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MMddyyyy");
	 public static final FastDateFormat SLASH_DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");

	private static final String PAGENUMBER = "pageNumber";
	public static final NumberFormat FORMATTER = new DecimalFormat("00");
	public static final Integer ZERO = new Integer(0);
	public static final BigDecimal BIG_ZERO = new BigDecimal("0").setScale(2);
	public static final BigDecimal ONE_BILLION = new BigDecimal("1000000000.00");
	public static final Integer NINETY_NINE = new Integer(99);
	private static final String CANCEL = "cancel";
	private static final String SUBNO = "subNo";
	private static final String REFRESH = "refresh";
    private static final String ADD_PARTICIPANT = "addParticipant";
	private static final String CONFIRMATION_PAGE = "confirmPage";
	private static String DEFAULT_SORT = LongTermPartTimeParticipant.SORT_RECORD_NUMBER;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static int BANK_CLOSE_HOUR = 16;
	private static int MAX_ERROR_LEVEL = 20;
	private static int BANK_CLOSE_MINUTE = 0;
	
	private static final String TOOLS = "tools";
	private static final String SUBMISSION_TYPE_LTPT = SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT;
	private static final String HISTORY = "history";
	public static final String VESTING_SIZE_LIMIT_KEY = "copy.size.limit";
	protected static final String VESTING_SIZE_LIMIT = System.getProperty(VESTING_SIZE_LIMIT_KEY, "1500");
	protected static final String USER_TYPE_INTERNAL = "I";
    private static final String ACTION_PATH_VIEW_LTPT_DETAILS = "redirect:/do/tools/viewLongTermPartTimeDetails/";

	/**
	 * Constructor.
	 */
	public EditLongTermPartTimeDetailsController() {
		super(EditLongTermPartTimeDetailsController.class);
	}

	@RequestMapping(value ="/editLongTermPartTimeDetails/",params={"task=prepareAddParticipant"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrepareAddParticipant (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrepareAddParticipant");
		}

		form.setShowConfirmDialog(false);
		request.getSession(false).setAttribute(SUBNO, form.getSubNo());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPrepareAddParticipant");
		}

		return forwards.get(ADD_PARTICIPANT);

	}

	@RequestMapping(value ="/editLongTermPartTimeDetails/",params={"task=undo"},method ={RequestMethod.POST,RequestMethod.GET}) 
	public String doUndo (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUndo");
		}

		// clear the form
		form.clear(request);
		form.setShowConfirmDialog(false);

		// note: refresh of lock done in doCommon, so don't
		//       bother here
		String forward = doCommon(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doUndo");
		}

		return forward;
	}
	
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=submission history"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSubmissionHistory (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doHistory(form, bindingResult, request, response);
    	
    }
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params={"task=history"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistory (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistory");
		}
		
		LongTermPartTimeDetailsReportData theReport = form.getTheReport();

		boolean unlocked = LockManager.getInstance(request.getSession(false)).release(theReport.getLongTermPartTimeData());
		if (!unlocked) {
			logger.debug("problem unlocking");
		}

		// clear the form
		form.setSubNo(null);
		form.clear( request);
		form.setShowConfirmDialog(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doHistory");
		}

		return forwards.get(HISTORY);
	}
	
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=cancel submission"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancelSubmission (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doCancel(form, bindingResult, request, response);
    	
    }
	
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params={"task=cancel"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCancel (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
	
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCancel");
        }
       
        LongTermPartTimeDetailsReportData theReport = form.getTheReport();
        int submissionId = new Integer(form.getSubNo()).intValue();

        // cancel submission
        UserProfile userProfile = getUserProfile(request);
        int contract = userProfile.getCurrentContract().getContractNumber();
        long userProfileId = userProfile.getPrincipal().getProfileId();
        SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
        submissionService.cancelSubmission(submissionId, contract,
                SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT, userProfileId);

        // invoke the datachecker RMI service synchronously
        int errorLevel = 0;
        try {
            errorLevel = DataCheckerServiceDelegate.getInstance().deleteAllDataCheckProblemFiles(submissionId,
                    contract, SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT);
        } catch (UnableToAccessDataCheckerException ex) {
            Collection errors = new ArrayList();
            LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
            errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER,
                    new String[] {Integer.toString(submissionId)} ));
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        }


        try {
            errorLevel = DataCheckerServiceDelegate.getInstance().deleteAllSyntacticProblemFiles(submissionId,
                    contract, SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT);
        } catch (UnableToAccessDataCheckerException ex) {
            Collection errors = new ArrayList();
            LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
            errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER,
                    new String[] {Integer.toString(submissionId)} ));
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        }

        boolean unlocked = LockManager.getInstance(request.getSession(false)).release(theReport.getLongTermPartTimeData());
        if (!unlocked) {
            logger.debug("problem unlocking");
        }

        // clear the form
        form.setSubNo(null);
        form.clear(request);
        form.setShowConfirmDialog(false);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCancel");
        }

        return forwards.get(CANCEL);
    }
	@RequestMapping(value ="/editLongTermPartTimeDetails/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doRefresh");
		}

			return doDefault(form, request, response);
		// the framework seems to lose the page number, so I restore it from the value I stored earlier
		
	}
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params={"task=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doRefresh");
		}

		if(request.getParameter(PAGENUMBER) != null && request.getParameter(PAGENUMBER).length() != 0) {
			return doDefault(form, request, response);
		}
		// the framework seems to lose the page number, so I restore it from the value I stored earlier
		
		form.setPageNumber(form.getMyOwnPageNumber());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doRefresh");
		}

		return forwards.get("input");	}

	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=save"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSave");
		}

        // setting this to true will cause all records with empty perc to be cleaned up;
        // as we only want this to happen on submit, always set to false
		form.setResubmit(false);

		String forward = saveData(form, bindingResult, request, response);
		/*
         * Resets the token after the form is cleared.
         */
		if (!forward.equals(forwards.get(REFRESH))) {
			//resetToken(request);
		}

		String newForward = form.getForwardFromSave();
		if (forward.equals(forwards.get("editLongTermPartTimeDetails"))
				&& !newForward.equals("")) {
			// if we get the expected result, forward to the requested task (if any)
			form.setForwardFromSave("");

			forward = forwards.get(newForward);
		} else {
			// otherwise, send user to wherever the forward says
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSave");
		}

		return forward;
	}
	@RequestMapping(value ="/editLongTermPartTimeDetails/",  params= {"task=saveForSubmit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSaveForSubmit (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSaveForSubmit");
		}

		// setting this to true will cause all records with empty perc to be cleaned up;
		form.setResubmit(true);

		String forward = saveData(form, bindingResult, request, response);
		/*
         * Resets the token after the form is cleared.
         */
		if (!forward.equals(forwards.get(REFRESH))) {
			//resetToken(request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSaveForSubmit");
		}

		return forward;

	}

	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=saveData"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	private String saveData (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		Date startTime = new Date();

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get( TOOLS);
		}

		form.setNoPermission(false);

		// reset if they've seen the confirm dialog already
		form.setShowConfirmDialog(false);

		int submissionId = Integer.valueOf(form.getSubNo()).intValue();
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		String userId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String processorUserId = "";
		if (userProfile.isInternalUser()) {
			processorUserId = String.valueOf(userProfile.getName());
		} else {
			processorUserId = String.valueOf(userProfile.getPrincipal().getProfileId());
		}
		String userName = userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName();
		String userType = isTPARole(userProfile)? GFTUploadDetail.USER_TYPE_TPA : GFTUploadDetail.USER_TYPE_CLIENT;
		if (userProfile.isInternalUser()) {
			userType = USER_TYPE_INTERNAL;
		}
		String userTypeID = userProfile.getClientId();

		//TODO: this should be client name
		String userTypeName = "";

		// get the existing complete LongTermPartTime detail item
		SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
		LongTermPartTimeDetailItem longTermPartTimeDetailItem =
			submissionService.getLongTermPartTimeDetails(submissionId, contractNumber, new ReportCriteria(""));

		// make changes to the existing item based on the changes in the form
		saveLongTermPartTimeYears(longTermPartTimeDetailItem, form);
		
		// call the saveLongTermPartTimeDetails method to persist the changes
		Integer participantCount = submissionService.saveLongTermPartTimeDetails(userId,
				userName, userType, userTypeID, userTypeName, processorUserId,
				longTermPartTimeDetailItem, form.isResubmit());

		// log the file upload event
		Date endTime = new Date();
		logEvent(form, userProfile, startTime, endTime, "saveData");

		/*
		 * If the participant count reaches 0, we can no longer edit this LTPT submission,
		 * so we redirect the user to the view page.
		 */
		if (participantCount != null && participantCount == 0) {
			ControllerRedirect redirect = new ControllerRedirect(ACTION_PATH_VIEW_LTPT_DETAILS);
			redirect.addParameter(SUBNO, longTermPartTimeDetailItem.getSubmissionId());
			return redirect.getPath();
		}
		
        // forward to the jsp
		return forwards.get("editLongTermPartTimeDetails");
	}

	private void saveLongTermPartTimeYears(LongTermPartTimeDetailItem longTermPartTimeDetailItem,
			LongTermPartTimeDetailsForm theForm) {

		Iterator ltptData = longTermPartTimeDetailItem.getSubmissionParticipants().iterator();
		while (ltptData.hasNext()) {
			LongTermPartTimeParticipant longTermPartTimeParticipant = (LongTermPartTimeParticipant) ltptData.next();

			Map employerIds = theForm.getEmployerIdsMap();
			Map recordNumbers = theForm.getRecordNumbersMap();
			List deleteBoxes = theForm.getDeleteBoxesMap();
			List ltptYears = theForm.getLongTermPartTimeAssessmentYearsMap();

			Iterator rows = employerIds.keySet().iterator();
			while (rows.hasNext()) {
				Integer row = (Integer) rows.next();
				String employerId = (String) employerIds.get(row);
				Integer recordNumber = (Integer) recordNumbers.get(row);
				Boolean deleteFlag = (Boolean) deleteBoxes.get(row);
				String ltptYear = (String) ltptYears.get(row);
				
				// get the participant
				LongTermPartTimeParticipant participant = getParticipant(employerId, recordNumber,
						longTermPartTimeDetailItem);

				// update the long term part time assessment year from the page
				for (int i = 0; i < ltptYears.size(); i++) {
					if (participant != null && ltptYear != longTermPartTimeParticipant.getLongTermPartTimeAssessmentYear()) {
						participant.setLongTermPartTimeAssessmentYear(ltptYear);
					}
				}
				
				if (participant != null && deleteFlag) {
					// flag this participant for deletion
					participant.setMarkedForDelete(true);
				}

			}
		}
	}

    @RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=re-submit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doReSubmit (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doSubmit(form, bindingResult, request, response);
    
    }
    @RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=submit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSubmit (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSubmit");
		}

		UserProfile userProfile = getUserProfile(request);
		        
        // get the contract number
        int contractNumber = userProfile.getCurrentContract().getContractNumber();
        
        // get the submission ID
        int submissionId = Integer.valueOf(form.getSubNo()).intValue();
        
		// first save any changes
		String forward = doSaveForSubmit( form, bindingResult, request, response);

		// an error was found in the save, so don't continue
		// redisplay the edit page with the error
		if (forward.equals(forwards.get(REFRESH))) {
			return forward;
		}

		form.clear(request);

		ReportCriteria criteria = new ReportCriteria(LongTermPartTimeDetailsReportData.REPORT_ID);

		criteria.addFilter(
				LongTermPartTimeDetailsReportData.FILTER_CONTRACT_NUMBER,
				new Integer(contractNumber)
		);

		criteria.addFilter(
				LongTermPartTimeDetailsReportData.FILTER_SUBMISSION_ID,
				new Integer(submissionId)
		);

		// get the existing complete LTPT detail item
		LongTermPartTimeDetailsReportData bean = null;
		ReportServiceDelegate service = null;
		try{
			service = ReportServiceDelegate.getInstance();
			bean = (LongTermPartTimeDetailsReportData) service.getReportData(criteria);
			request.setAttribute(Constants.REPORT_BEAN, bean);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit vesting item");
		}

		LongTermPartTimeDetailItem longTermPartTimeDetail = bean.getLongTermPartTimeData();

		// invoke the data checker to check the max error level SP4.2.29.2
		int errorLevel = 0;
		// invoke the datachecker RMI service synchronously
		ICon2DataProblem dataProblem = null;
		try {
			dataProblem = DataCheckerServiceDelegate.getInstance().checkData(
					Integer.toString(longTermPartTimeDetail.getTransmissionId()),
					Integer.toString(contractNumber), SUBMISSION_TYPE_LTPT);
		} catch (UnableToAccessDataCheckerException ex) {
			Collection errors = new ArrayList();
			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
			errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER,
					new String[] {Integer.toString(submissionId)} ));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(HISTORY);
		}

		// depending on the error level > error tolerance then reload the edit page
		errorLevel = dataProblem.getErrorLevel();
		if (errorLevel >= MAX_ERROR_LEVEL) {
			form.setShowConfirmDialog(false);
			return forwards.get("editLongTermPartTimeDetails");
		}

		// get the LTPT detail item after the datachecker has run
		try{
			if (service == null) {
				service = ReportServiceDelegate.getInstance();
			}
			bean = (LongTermPartTimeDetailsReportData) service.getReportData(criteria);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit LTPT item");
		}

        // get the latest LTPT data
		longTermPartTimeDetail = bean.getLongTermPartTimeData();
        
		// error level < error tolerance, forward to the confirmation page
		String userId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String userName = userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName();
		String clientId = userProfile.getClientId();

		// populate a bean for display on the confirmation page
		EditLongTermPartTimeDetailBean confirmBean = new EditLongTermPartTimeDetailBean();

		confirmBean.setConfirmationNumber(longTermPartTimeDetail.getSubmissionId().toString());
		// the format of the sender name is LN, FN for the confirmation
		confirmBean.setSender(userProfile.getPrincipal().getLastName() + ", " + userProfile.getPrincipal().getFirstName());
        // displaying the initial LTPT submission date, not the resubmit date
		confirmBean.setReceivedDate(longTermPartTimeDetail.getSubmissionDate());
		confirmBean.setNumberOfRecords(String.valueOf(longTermPartTimeDetail.getNumberOfRecords()));
		confirmBean.setSubmissionType("LTPT");

		SessionHelper.setEditLongTermPartTimeDetails(request, confirmBean);

		request.getSession(false).setAttribute(Constants.REPORT_BEAN, bean);


		Date startTime = new Date();

		// invoke the datachecker RMI service synchronously to set the status and backup the data
		try {
			DataCheckerServiceDelegate.getInstance().dataCheckWrapUp(Integer.toString(longTermPartTimeDetail.getTransmissionId()),
				Integer.toString(contractNumber), SUBMISSION_TYPE_LTPT, dataProblem, userId, userName);
		} catch (UnableToAccessDataCheckerException ex) {
			Collection errors = new ArrayList();
			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
			errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER,
					new String[] {Integer.toString(submissionId)} ));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(HISTORY);
		}

		//	log the file upload event
		Date endTime = new Date();
		logEvent(confirmBean, userProfile, startTime, endTime, "doSubmit");

		forward = forwards.get("confirm");

		// check lock and then release it
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(longTermPartTimeDetail,true);
		if (lock != null) {
			longTermPartTimeDetail.setLock(lock);
			boolean unlocked = LockManager.getInstance(request.getSession(false)).release(longTermPartTimeDetail);
			if (!unlocked) {
				logger.debug("problem unlocking");
			}
		} else {
			logger.debug("lock is null!");
		}

		//resetToken(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSubmit");
		}

		// forward to the jsp
		return forward;
	}

    @RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=setReportPageSize"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSetReportPageSize (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
   
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSetReportPageSize");
		}

		/*
	     * Resets the token after the form is cleared.
	     */
		//resetToken(request);

	    UserProfile profile = getUserProfile(request);

	    String newPageSizeString = (String)request.getParameter("newPageSize");
	    Integer newPageSize = new Integer(newPageSizeString);
	    if (newPageSize == null || newPageSize.intValue() == 0) {
	        return null;
	    }

	    profile.getPreferences().put(UserPreferenceKeys.REPORT_PAGE_SIZE,newPageSizeString);

	    SecurityServiceDelegate.getInstance().updateUserPreference(
	            profile.getPrincipal(), UserPreferenceKeys.REPORT_PAGE_SIZE,
	            newPageSizeString);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSetReportPageSize");
		}

	    return forwards.get(Constants.RELOAD_PAGE_RESET_PAGE_NUMBER_FORWARD);

	}

	/**
	 * Forward to the confirmation page. This action is needed because it's the
	 * result of a REDIRECT after a POST.
	 *
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=confirm"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doConfirm (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doConfirm");
		}


		String forward =  forwards.get(CONFIRMATION_PAGE);

		UserProfile userProfile = getUserProfile(request);

		// let's check the permissions
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			forward =   forwards.get(TOOLS);
		}

		// let's check whether the confirm object and report bean are in the session
		if (null == request.getSession(false).getAttribute(Constants.EDIT_LONG_TERM_PART_TIME_CONFIRM_DETAIL_DATA)
				|| null == request.getSession(false).getAttribute(Constants.REPORT_BEAN)
				|| !(request.getSession(false).getAttribute(Constants.REPORT_BEAN) instanceof LongTermPartTimeDetailsReportData)) {

			forward = forwards.get(HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forward;
	}

	protected LongTermPartTimeDetailsForm resetForm(
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		try {
			// we'll do our own custom reset
			// we need to save the submission number
			
			form.clear();
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),	"resetForm", "exception in resetting the form");
		}

		return (LongTermPartTimeDetailsForm) form;
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

        // Reset page number properly.
         
        if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK)
                || task.equals(FILTER_TASK) || task.equals(PRINT_TASK)
                || task.equals(DOWNLOAD_TASK)) {
            reportForm.setPageNumber(1);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportForm");
        }
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
		return LongTermPartTimeDetailsReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return LongTermPartTimeDetailsReportData.REPORT_NAME;
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

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// get the user profile object
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        LongTermPartTimeDetailsForm longTermPartTimeDetailsForm = (LongTermPartTimeDetailsForm) form;

		criteria.addFilter(
				LongTermPartTimeDetailsReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber())
		);

		String subNo = request.getParameter(SUBNO);
		// see if we're doing the default get where the submission number is from the request
		if (subNo != null && subNo.length() != 0) {
			longTermPartTimeDetailsForm.setShowConfirmDialog(false);
		}
		// we expect the submission number to be present in the request.
		// if not, get it from the session form
		if (subNo == null || subNo.length() == 0)
			subNo = (String)longTermPartTimeDetailsForm.getSubNo();

		// if not, get it from the session
		if (subNo == null || subNo.length() == 0) {
			subNo = (String)request.getSession(false).getAttribute(SUBNO);
		}

		// the first time or if we change submissions, don't show the confirmation dialog
		if (longTermPartTimeDetailsForm.getSubNo() == null || !longTermPartTimeDetailsForm.getSubNo().equals(subNo)) {
			longTermPartTimeDetailsForm.setShowConfirmDialog(false);
		}

		longTermPartTimeDetailsForm.setSubNo(subNo);
		criteria.addFilter(
				LongTermPartTimeDetailsReportData.FILTER_SUBMISSION_ID,
				new Integer(longTermPartTimeDetailsForm.getSubNo())
		);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#doCommon(org.apache.struts.action.ActionMapping, com.manulife.pension.ps.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 
	protected String doCommon (BaseReportForm reportForm,  HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		LongTermPartTimeDetailsForm form = (LongTermPartTimeDetailsForm) reportForm;
		/*
         * Save the token for this form. We have to validate this token when we
         * save so that duplicate submits are avoided.
         */
		String task = getTask(request);

		if (!"download".equals(task)) {
			//saveToken(request);
		}

        // lets check the permissions
		UserProfile userProfile = getUserProfile(request);

		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}
		
        form.setNoPermission(false);

		String subNo = request.getParameter(SUBNO);
		// we expect the submission number to be present in the request.
		// if not, get it from the session form
		if (subNo == null || subNo.length() == 0)
			subNo = (String)form.getSubNo();

		// if not, get it from the session
		if (subNo == null || subNo.length() == 0)
			subNo = (String)request.getSession(false).getAttribute(SUBNO);

		// due to some weird bookmarking this may actually not be found, so send user to submission history page
		if (null == subNo) {
			return forwards.get(HISTORY);
		}

		String forward = super.doCommon( form, request, response);
		LongTermPartTimeDetailsReportData theReport = (LongTermPartTimeDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
		LongTermPartTimeDetailItem theItem = null;

        if (theReport == null) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        } else {
            theItem = theReport.getLongTermPartTimeData();
        }

		if (!userProfile.isAllowedToViewAllSubmissions()) {
			long submitterId = 0;
		    try {
		    	submitterId = Long.parseLong(theItem.getSubmitterID());
		    } catch (NumberFormatException e) {
				return forwards.get(HISTORY);
		    }
		    if (userProfile.getPrincipal().getProfileId() != submitterId) {
		    	return forwards.get(HISTORY);
		    }
		}

		form.setTheReport(theReport);

		// lock the submission case
		boolean locked = LockManager.getInstance(request.getSession(false)).lock(theReport.getLongTermPartTimeData(), String.valueOf(userProfile.getPrincipal().getProfileId()));
		if (!locked) {
			// cannot obtain a lock, generate an error and return the user to the submission history page
			Collection lockError = new ArrayList(1);
			lockError.add(new ValidationError("LOCKED", ErrorCodes.SUBMISSION_CASE_LOCKED));
			SessionHelper.setErrorsInSession(request, lockError);
			return forwards.get(HISTORY);
		}

		if (
				userProfile.getContractProfile().getIfileConfig() == null
		) {
			try {
				setupUserProfile(userProfile, form, request);
			} catch (UnableToAccessIFileException ex) {
				Collection errors = new ArrayList();
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
				errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_IFILE));
				SessionHelper.setErrorsInSession(request, errors);
				return forwards.get(HISTORY);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug(StaticHelperClass.toString(userProfile.getContractProfile().getIfileConfig()));
		}

		try {
			populateForm(userProfile, form, request);
		} catch (SystemException ex) {
			Collection errors = new ArrayList();
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,ex);
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(HISTORY);
		}

		// clone the form for track changes
        form.storeClonedForm();

		// clear any session attributes we don't want any more
		request.getSession(false).removeAttribute(SUBNO);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		
	}
	/**
	 * @see ReportController#getPageSize(HttpServletRequest)
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
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadData");
        }

        StringBuffer buffer = new StringBuffer();
        LongTermPartTimeDetailsReportData reportData = (LongTermPartTimeDetailsReportData) report;

        // Fill in the header
        Iterator columnLabels = reportData.getColumnLabels().iterator();
        while (columnLabels.hasNext()) {
            buffer.append(columnLabels.next());
            if (columnLabels.hasNext()) {
                buffer.append(COMMA);
            }
        }

        Iterator items = report.getDetails().iterator();
        while (items.hasNext()) {
            buffer.append(LINE_BREAK);
            // only populate transaction code 
            // if standard LongTermPartTime file was submitted (no TPA system name available)
            if (reportData.getLongTermPartTimeData().getTpaSystemName() == null) {
                appendBuffer(buffer,reportData.getTransactionNumber()).append(COMMA);
            }
            appendBuffer(buffer, reportData.getContractNumber()).append(COMMA);

            LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant) items.next();
            if (participant.getSsn() != null) {
                buffer.append(SSNRender.format(participant.getSsn(), "", false));
            }
            buffer.append(COMMA);
            
            appendBuffer(buffer, participant.getFirstName()).append(COMMA);
            appendBuffer(buffer, participant.getLastName()).append(COMMA);
            appendBuffer(buffer, participant.getMiddleInitial()).append(COMMA);
            appendBuffer(buffer, participant.getLongTermPartTimeAssessmentYear()).append(COMMA);
            
        } // end while

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDownloadData");
        }

        return buffer.toString().getBytes();
    }


	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet.http.HttpServletRequest)
	 */
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
        String dateString = null;
        synchronized (DATE_FORMATTER) {
            dateString = DATE_FORMATTER.format(new Date());
        }
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		return "LongTermPartTime_Submission_for_" + String.valueOf(contractId)
				+ "_for_" + dateString + CSV_EXTENSION;
	}

	protected void setupUserProfile(UserProfile userProfile, LongTermPartTimeDetailsForm form, HttpServletRequest request)
	throws SystemException, UnableToAccessIFileException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> setupUserProfile");
		}

		IFileConfig ifileConfig = new IFileConfig();

		Calendar calendar = adjustDate4pm(null);

		int currentYear = calendar.get(Calendar.YEAR);

		//set it up for the 1st time

		//setup years
		ArrayList years = new ArrayList();

		years.add(String.valueOf(currentYear));
		calendar.add(Calendar.DATE, 15);
		if (calendar.get(Calendar.YEAR) > currentYear) {
			years.add(String.valueOf(currentYear));
		}
		ifileConfig.setYears(years);

		//determine if we have upload history for this account
		String contractId = Integer.toString(userProfile.getCurrentContract()
				.getContractNumber());

		//set it up in userProfile
		userProfile.getContractProfile().setIfileConfig(ifileConfig);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- setupUserProfile");
		}
	}

	protected static Calendar adjustDate4pm(Calendar calendar) {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}

		calendar.add(Calendar.MINUTE, 60-BANK_CLOSE_MINUTE); //60 minutes
		calendar.add(Calendar.HOUR_OF_DAY, 23-BANK_CLOSE_HOUR); //23 hrs, + 60 minutes
		return calendar;
	}

	protected void populateForm(UserProfile userProfile, LongTermPartTimeDetailsForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateForm");
		}
		
		LongTermPartTimeDetailsReportData theReport = form.getTheReport();
		LongTermPartTimeDetailItem theItem = theReport.getLongTermPartTimeData();

		// fill in the columns for LTPT
		List<Boolean> deleteBoxes =  new ArrayList<>();
		Map<Integer, String> employerIds = new HashMap<>();
		Map<Integer, Integer> recordNumbers = new HashMap<>();
		List<String> longTermPartTimeAssessmentYears = new ArrayList<>();
        
		Iterator<?> participants = theReport.getDetails().iterator();
		int row = 0;

		while (participants.hasNext()) {
			LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant) participants.next();

			if (!participant.isIgnored()) {
				deleteBoxes.add(new Integer(row), false);
				employerIds.put(new Integer(row), participant.getEmployerDesignatedId());
				recordNumbers.put(new Integer(row), new Integer(participant.getRecordNumber()));
				longTermPartTimeAssessmentYears.add(new Integer(row), participant.getLongTermPartTimeAssessmentYear());

			} else {
				// This else block is created to handle Array index for these below ArrayList
				// collections
				deleteBoxes.add(new Integer(row), null);
				longTermPartTimeAssessmentYears.add(new Integer(row), participant.getLongTermPartTimeAssessmentYear());
			}
			row++;
		}
		form.setDeleteBoxesMap(deleteBoxes);
		form.setEmployerIdsMap(employerIds);
		form.setRecordNumbersMap(recordNumbers);
		form.setLongTermPartTimeAssessmentYearsMap(longTermPartTimeAssessmentYears);
		
		// allow to download only if submission status is complete,
        // user has "Submit/Update LongTermPartTime" permission and total count > 0
		LongTermPartTimeDetailsHelper longTermPartTimeHelper = new LongTermPartTimeDetailsHelper();
        boolean allowedToDownload = LongTermPartTimeDetailsHelper.STATUS_COMPLETE.equals(theItem.getSystemStatus()) &&
                                    userProfile.isAllowedSubmitUpdateVesting() && (theReport.getTotalCount() > 0);
        longTermPartTimeHelper.setAllowedToDownload(allowedToDownload);

        request.getSession(false).setAttribute(Constants.LONG_TERM_PART_TIME_DETAILS_HELPER,longTermPartTimeHelper);

        form.setViewMode(false);
        
        // get error level
        int errorLevel = 0;
        try {
            errorLevel = Integer.parseInt(theReport.getLongTermPartTimeData().getErrorLevel());
        } catch ( NumberFormatException nfe){
            // the string does not have the appropriate format
        }
        if (LongTermPartTimeDetailsHelper.STATUS_SYNTACTIC_CHECK_FAILED.equals(theItem.getSystemStatus()) ||
                (theReport.getTotalCount() == 0)  ||  errorLevel >= 35 ) {
               form.setViewMode(true);
        }
        
        // do not mask SSN in edit screen
        form.setMaskSSN(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateForm");
		}
	}

	/**
	 * Checks whether we're in the right state.
	 *
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		LongTermPartTimeDetailsForm form = (LongTermPartTimeDetailsForm) actionForm;
		// This code has been changed and added to
		// Validate form and request against penetration attack, prior to other validations.
		
		/*
		 * If this is a save action, we should compare the token and make sure
		 * it's still valid. Token is initialized in the doDefault() method and
		 * reset in the doSave() method.
		 */
		if (
				form.isSaveAction(getTask(request)) ||
				form.isSubmitAction(getTask(request)))
		 {
			/*if (!isTokenValid(request)) {
				return forwards.get(TOOLS);
			}*/
		}

		Collection errors = doValidate( form, request);

		/*
		 * Errors are stored in the session so that our REDIRECT can look up the
		 * errors.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		return null;
	}
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	 
	private static LongTermPartTimeParticipant getParticipant(String employerId, Integer recordNumber, LongTermPartTimeDetailItem longTermPartTimeItem) {
		if (employerId == null || recordNumber == null || longTermPartTimeItem == null) return null;

		LongTermPartTimeParticipant returnedParticipant = null;
		Iterator participants = longTermPartTimeItem.getSubmissionParticipants().iterator();

		while (participants.hasNext()) {
			LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant) participants.next();
			if (employerId.equals(participant.getEmployerDesignatedId())
				&& participant.getRecordNumber() == recordNumber.intValue()) {
				returnedParticipant = participant;
				break;
			}
		}
		return returnedParticipant;
	}

	/**
	 * @return boolean
	 */
	private static boolean isTPARole(UserProfile userProfile) {
		 return userProfile.getPrincipal().getRole() instanceof ThirdPartyAdministrator;
	}

	/**
	 * Returns the bigdecimal version of a string set to the expected scale
	 * @param value
	 * @return
	 */
	private static BigDecimal getBigDecimal(String value) {
		String originalValue = value;
		BigDecimal bigValue;
		if(value == null) {
			return new BigDecimal(ZERO_AMOUNT_STRING).setScale(2);
		}
		boolean negativeValue = false;
		if (value.indexOf("-") != -1) {
			value = value.substring(value.indexOf("-")+1);
			negativeValue = true;
		} else if (value.indexOf("(") != -1) {
			value = value.substring(value.indexOf("(")+1, value.indexOf(")"));
			negativeValue = true;
		}
		if (value == null || value.trim().equals("")) {
			return new BigDecimal(ZERO_AMOUNT_STRING).setScale(2);
		}
		StringTokenizer valueTokenizer = new StringTokenizer(value.trim(),",");
		StringBuffer valueBuffer = new StringBuffer();
		while (valueTokenizer.hasMoreTokens()) {
			valueBuffer.append(valueTokenizer.nextToken());
		}
		try {
			bigValue = new BigDecimal(valueBuffer.toString()).setScale(2,BigDecimal.ROUND_HALF_UP);
			if (negativeValue) bigValue = bigValue.negate();
		} catch (NumberFormatException e) {
			System.out.println("EditLongTermPartTimeDetails.getBigDecimal threw NumberFormatException trying to parse ["
					+ originalValue + "]");
			throw e;
		}
		return bigValue;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getTask(javax.servlet.http.HttpServletRequest)
	 */
	protected String getTask(HttpServletRequest request) {
		// if its resubmit.. map it to submit task
		String task = super.getTask(request);
		if (task.equals("re-submit"))
			task = "submit";
        else if (task.equals("cancel submission"))
            task = "cancel";
        else if (task.equals("submission history"))
            task = "history";
		return task;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.controller.PsAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	 */
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		Collection errors = super.doValidate( form, request);
		// TODO check any additional server side validation
		if(logger.isDebugEnabled()) {
		    logger.debug("entry -> doValidate");
	    }

	    LongTermPartTimeDetailsForm registerForm = (LongTermPartTimeDetailsForm) form;

		if(logger.isDebugEnabled()) {
		    logger.debug("exit <- doValidate");
	    }
		return errors;

	}

	private void incrementNumberOfCopyWarnings(HttpServletRequest request) {
		Integer numCopyWarnings = (Integer)request.getSession(false).getAttribute(Constants.NUM_COPY_WARNINGS);
		int n = 0;
		if (numCopyWarnings != null) {
			n = numCopyWarnings.intValue();
		}
		numCopyWarnings = new Integer(n+1);
		request.getSession(false).removeAttribute(Constants.NUM_COPY_WARNINGS);
		request.getSession(false).setAttribute(Constants.NUM_COPY_WARNINGS,numCopyWarnings);
	}

	protected void logEvent(EditLongTermPartTimeDetailBean confirmBean, UserProfile userProfile, Date startTime,
			Date endTime, String methodName) {


		String logData = prepareLogData(confirmBean, userProfile, startTime, endTime);
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName, logData,
				OnlineSubmissionEventLog.class);
	}

	protected void logEvent(LongTermPartTimeDetailsForm theForm, UserProfile userProfile, Date startTime,
			Date endTime, String methodName) {

		String logData = "";
		try {
			logData = prepareLogData(theForm, userProfile, startTime, endTime);
		} catch (SystemException se) {
			throw ExceptionHandlerUtility.wrap(se);
		}
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName,
				logData, OnlineSubmissionEventLog.class);
	}

	protected String prepareLogData(EditLongTermPartTimeDetailBean confirmBean, UserProfile userProfile, Date startTime,
			Date endTime) {

		StringBuffer logData = new StringBuffer()
		.append("Confirmation number: ")
		.append(confirmBean.getConfirmationNumber())
		.append(" Contract number: ")
		.append(userProfile.getCurrentContract().getContractNumber())
		.append(" Username : ")
		.append(confirmBean.getSender());

		logData.append(" Submission start time : ")
		.append(startTime.toString())
		.append(" Submission end time: ")
		.append(endTime.toString());

		return logData.toString();
	}
    
    private StringBuffer appendBuffer(StringBuffer buffer, Object o) {
        if (o != null) {
            buffer.append(o);
        }
        return buffer;
    }

	protected String prepareLogData(LongTermPartTimeDetailsForm theForm, UserProfile userProfile, Date startTime,
			Date endTime) throws SystemException {


		StringBuffer logData = new StringBuffer()
		.append("Confirmation number: ")
		.append(theForm.getSubNo())
		.append(" Contract number: ")
		.append(userProfile.getCurrentContract().getContractNumber())
		.append(" Username : ")
		.append(userProfile.getPrincipal().getLastName())
		.append(", ")
		.append(userProfile.getPrincipal().getFirstName());

		logData.append(" Action start time : ")
		.append(startTime.toString())
		.append(" Action end time: ")
		.append(endTime.toString());

		return logData.toString();
	}

	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=reset"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doReset (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doReset");
        }

        UserProfile userProfile = getUserProfile(request);
        

        // clear the form
        form.clear( request);

        // note: refresh of lock done in doCommon, so don't
        //       bother here
        String forward = doCommon( form, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }
        return forward;
    }
	
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doReset");
        }

        UserProfile userProfile = getUserProfile(request);
        

        // clear the form
        /* form.clear( request);*/

        // note: refresh of lock done in doCommon, so don't
        //       bother here
        String forward = doCommon( form, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }
        return forward;
    }
	
	@RequestMapping(value ="/editLongTermPartTimeDetails/",params= {"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("longTermPartTimeDetailsForm") LongTermPartTimeDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doReset");
        }

        UserProfile userProfile = getUserProfile(request);
        

       // note: refresh of lock done in doCommon, so don't
        //       bother here
        String forward = doCommon( form, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }
        return forward;
    }
}
