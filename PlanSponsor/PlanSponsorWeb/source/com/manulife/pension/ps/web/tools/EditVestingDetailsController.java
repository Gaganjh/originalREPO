package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.Predicate;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
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
import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.DataCheckerServiceDelegate;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessDataCheckerException;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.VestingDetailsForm.RowVal;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.tools.util.VestingDetailsHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.employee.valueobject.PlanYearEndInfo;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.stp.datachecker.ICon2DataProblem;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 *
 * @author Diana Macean
 *
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"vestingDetailsForm"})
public class EditVestingDetailsController extends ReportController {

	@ModelAttribute("vestingDetailsForm") 
	public VestingDetailsForm populateForm() 
	{
		return new VestingDetailsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{forwards.put("input","/tools/viewVestingDetails.jsp"); 
	forwards.put("tools","redirect:/do/tools/toolsMenu/");
	forwards.put("default","/tools/editVestingDetails.jsp");
	forwards.put("undo","/tools/editVestingDetails.jsp");
	forwards.put("sort","/tools/editVestingDetails.jsp");
	forwards.put("filter","/tools/editVestingDetails.jsp");
	forwards.put("page","/tools/editVestingDetails.jsp");
	forwards.put("print","/tools/editVestingDetails.jsp");
	forwards.put("error","/tools/editVestingDetails.jsp");
	forwards.put("addParticipant","redirect:/do/tools/addParticipant");
	forwards.put("refresh","redirect:/do/tools/editVesting/?task=refresh"); 
	forwards.put("cancel","redirect:/do/tools/submissionHistory/");
	forwards.put("editVesting","redirect:/do/tools/editVesting/"); 
	forwards.put("confirm","redirect:/do/tools/editVesting/?task=confirm");
	forwards.put("confirmPage","/tools/editVestingConfirmation.jsp");
	forwards.put("history","redirect:/do/tools/submissionHistory/"); 
	forwards.put("addMoneyType ","redirect:/do/tools/editVesting/?task=addMoneyType");
	forwards.put("prepareAddParticipant","redirect:/do/tools/editVesting/?task=prepareAddParticipant");
	forwards.put("setReportPageSize","redirect:/do/tools/editVesting/?task=setReportPageSize");
	forwards.put("reset","/tools/editVestingDetails.jsp");}
	
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
	private static final String FALSE = "false";
	private static final String REFRESH = "refresh";
	private static final String DEFAULT = "default";
    private static final String ADD_PARTICIPANT = "addParticipant";
	private static final String CONFIRMATION_PAGE = "confirmPage";
	private static final String NUMBER_FORMAT_PATTERN = "###,###,##0.00;-###,###,##0.00";
	private static String DEFAULT_SORT = VestingParticipant.SORT_RECORD_NUMBER;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static int BANK_CLOSE_HOUR = 16;
	private static int MAX_ERROR_LEVEL = 20;
	private static int BANK_CLOSE_MINUTE = 0;
	//current day + 15
	private static final int DELTA_END_DAYS = 16;
	private static String DEFAULT_MONEY_DD_LABEL = "Select Type";
	private static String DEFAULT_MONEY_DD_ID = "-1";
	private static final String BASIS_ACCRUAL_CODE = "RB AC";
	private static final String BASIS_COMBO_CODE = "RB CO";
	private static final String BASIS_CASH_CODE = "RB CA";
	private static final int MINUS_THREE_MONTHS = -3;
	private static final String TOOLS = "tools";
	private static final String SUBMISSION_TYPE_VESTING = GFTUploadDetail.SUBMISSION_TYPE_VESTING;
	private static final String HISTORY = "history";
	public static final String VESTING_SIZE_LIMIT_KEY = "copy.size.limit";
	protected static final String VESTING_SIZE_LIMIT = System.getProperty(VESTING_SIZE_LIMIT_KEY, "1500");
	protected static final String USER_TYPE_INTERNAL = "I";
    private static final String ERROR_COND_STRING_OK = "OK";
    private static final String ERROR_COND_STRING_ERROR = "ER";
    private static final String FIELD_SEPARATOR = ",";
    private static final String ACTION_PATH_VIEW_VESTING_DETAILS = "redirect:/do/tools/viewVesting/";

	/**
	 * Constructor.
	 */
	public EditVestingDetailsController() {
		super(EditVestingDetailsController.class);
	}
	@RequestMapping(value ="/editVesting/", params={"task=addMoneyType"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doAddMoneyType(@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		validate(actionForm, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doAddMoneyType");
		}

		String forward = doCommon( actionForm, request, response);

		UserProfile userProfile = getUserProfile(request);
		
		actionForm.setShowConfirmDialog(false);
		VestingDetailsReportData theReport = actionForm.getTheReport();

		// add a new money type column
		MoneyTypeVO moneyType = new MoneyTypeVO();
		moneyType.setContractShortName(DEFAULT_MONEY_DD_LABEL);
		String moneyTypeId = DEFAULT_MONEY_DD_ID;

		actionForm.getTheReport().getVestingData().getPercentageMoneyTypes().add(new MoneyTypeHeader(moneyTypeId, "0", moneyType));


		// fill in the columns for vestings and loans
		Iterator participants = actionForm.getTheReport().getDetails().iterator();
		int row = 0;
		while (participants.hasNext()) {
            VestingParticipant participant = (VestingParticipant) participants.next();

			// setup all of the vesting money type amounts
			Iterator moneyTypes = actionForm.getTheReport().getVestingData().getPercentageMoneyTypes().iterator();
			int contColumn = 0;
			while (moneyTypes.hasNext()) {
				MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader) moneyTypes.next();
				BigDecimal amount = (java.math.BigDecimal) participant.getMoneyTypePercentages().get(moneyTypeHeader.getKey());
				VestingDetailsForm.RowVal rowVal = (VestingDetailsForm.RowVal) actionForm.getVestingColumns(contColumn);
				rowVal.setRow(row,NumberRender.formatByPattern(amount,ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				contColumn++;
			}
			row++;
		}

		Iterator columns = actionForm.getVestingColumnsMap().iterator();
		while (columns.hasNext()) {
			Integer column = (Integer) columns.next();
			MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader) actionForm.getTheReport().getVestingData().getPercentageMoneyTypes().get(column.intValue());
			actionForm.getMoneyTypeColumns().put(column,moneyTypeHeader.getMoneyType().getId());
		}

		actionForm.setHasChanged(true);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doAddMoneyType");
		}

		return forwards.get(DEFAULT);
	}

	@RequestMapping(value ="/editVesting/",params={"task=prepareAddParticipant"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrepareAddParticipant (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

	@RequestMapping(value ="/editVesting/",params={"task=undo"},method ={RequestMethod.POST,RequestMethod.GET}) 
	public String doUndo (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	@RequestMapping(value ="/editVesting/",params= {"task=submission history"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSubmissionHistory (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doHistory(form, bindingResult, request, response);
    	
    
    }
	@RequestMapping(value ="/editVesting/",params={"task=history"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistory (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		
		VestingDetailsReportData theReport = form.getTheReport();

		boolean unlocked = LockManager.getInstance(request.getSession(false)).release(theReport.getVestingData());
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
	@RequestMapping(value ="/editVesting/",params= {"task=cancel submission"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancelSubmission (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doCancel(form, bindingResult, request, response);
    	
    
    }
	@RequestMapping(value ="/editVesting/",params={"task=cancel"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCancel (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

       
        VestingDetailsReportData theReport = form.getTheReport();
        int submissionId = new Integer(form.getSubNo()).intValue();

        // cancel submission
        UserProfile userProfile = getUserProfile(request);
        int contract = userProfile.getCurrentContract().getContractNumber();
        long userProfileId = userProfile.getPrincipal().getProfileId();
        SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
        submissionService.cancelSubmission(submissionId, contract,
                GFTUploadDetail.SUBMISSION_TYPE_VESTING, userProfileId);

        // invoke the datachecker RMI service synchronously
        int errorLevel = 0;
        try {
            errorLevel = DataCheckerServiceDelegate.getInstance().deleteAllDataCheckProblemFiles(submissionId,
                    contract, GFTUploadDetail.SUBMISSION_TYPE_VESTING);
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
                    contract, GFTUploadDetail.SUBMISSION_TYPE_VESTING);
        } catch (UnableToAccessDataCheckerException ex) {
            Collection errors = new ArrayList();
            LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
            errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_DATA_CHECKER,
                    new String[] {Integer.toString(submissionId)} ));
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        }

        boolean unlocked = LockManager.getInstance(request.getSession(false)).release(theReport.getVestingData());
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
	@RequestMapping(value ="/editVesting/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	@RequestMapping(value ="/editVesting/",params={"task=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

	@RequestMapping(value ="/editVesting/",params= {"task=save"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		if (forward.equals(forwards.get("editVesting"))
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
	@RequestMapping(value ="/editVesting/",  params= {"task=saveForSubmit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSaveForSubmit (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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


@RequestMapping(value ="/editVesting/",params= {"task=saveData"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
private String saveData (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		// get the existing complete vesting detail item
		SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
		VestingDetailItem vesting =
			submissionService.getVestingDetails(submissionId, contractNumber, new ReportCriteria(""));

		// make changes to the existing item based on the changes in the form
		saveMoneyTypes(vesting, form);

		// call the saveVestingDetails method to persist the changes
		Integer participantCount = submissionService.saveVestingDetails(userId,
				userName, userType, userTypeID, userTypeName, processorUserId,
				vesting, form.isResubmit());

		// log the file upload event
		Date endTime = new Date();
		logEvent(form, userProfile, startTime, endTime, "saveData");

		/*
		 * If the participant count reaches 0, we can no longer edit this vesting submission,
		 * so we redirect the user to the view page.
		 */
		if (participantCount != null && participantCount == 0) {
			ControllerRedirect redirect = new ControllerRedirect(ACTION_PATH_VIEW_VESTING_DETAILS);
			redirect.addParameter(SUBNO, vesting.getSubmissionId());
			return redirect.getPath();
		}
		
        // forward to the jsp
		return forwards.get("editVesting");
	}

	private void saveMoneyTypes(VestingDetailItem vesting, VestingDetailsForm theForm)
			throws SystemException {

		// make changes to the vesting percentage
		// the first thing we need to do is detect changes in the money type drop down
		// so that we can apply it to all of the other participants not shown on the page
		// we need to modify any money type column changes accross all participants in the vesting
		Iterator percentageMoneyTypes = vesting.getPercentageMoneyTypes().iterator();
		Map moneyTypeColumns = theForm.getMoneyTypeColumns();
		int col = 0;
		// walk through the existing money types to see if they have been changed
		while (percentageMoneyTypes.hasNext()) {
			MoneyTypeHeader moneyType = (MoneyTypeHeader)percentageMoneyTypes.next();
			String moneyKey = moneyType.getKey();
			String moneyKeyID = moneyKey.substring(0,moneyKey.indexOf("/"));
			String occurrenceNo = moneyKey.substring(moneyKey.indexOf("/")+1, moneyKey.length());
			String selectedMoneyKeyID = (String) moneyTypeColumns.get(new Integer(col));
			// if an existing money type key has been changed then we
			// need to reapply this change to all participants
			if (null != selectedMoneyKeyID && !selectedMoneyKeyID.equals(moneyKeyID) && !selectedMoneyKeyID.equals(DEFAULT_MONEY_DD_ID)) {
				Iterator participants = vesting.getSubmissionParticipants().iterator();
				while (participants.hasNext()) {
					VestingParticipant participant = (VestingParticipant) participants.next();
					SortedMap moneyTypePerc = participant.getMoneyTypePercentages();
                    SortedMap moneyTypeErr = participant.getMoneyTypeErrors();
					if (moneyTypePerc.get(moneyKey) != null) {
						// find the next available occurrence number for the selected money type
						int checkOccurrence = -1;
						Iterator checkMoneyTypes = vesting.getPercentageMoneyTypes().iterator();
						while (checkMoneyTypes.hasNext()) {
							MoneyTypeHeader moneyType2 = (MoneyTypeHeader)checkMoneyTypes.next();
							String moneyKey2 = moneyType2.getKey();
							String moneyKeyID2 = moneyKey2.substring(0,moneyKey2.indexOf("/"));
							int occurrenceNo2 = Integer.parseInt(moneyKey2.substring(moneyKey2.indexOf("/")+1, moneyKey2.length()));
							if (moneyKeyID2.equals(selectedMoneyKeyID) && occurrenceNo2 > checkOccurrence) {
								checkOccurrence = occurrenceNo2;
							}
						}
						// read it under the correct key
                        moneyTypePerc.put(
								selectedMoneyKeyID + "/" + Integer.toString(checkOccurrence+1).trim(),
                                moneyTypePerc.get(moneyKey)
						);
                        moneyTypeErr.put(
                                selectedMoneyKeyID + "/" + Integer.toString(checkOccurrence+1).trim(),
                                ERROR_COND_STRING_OK
                        );
						// remove the old changed one
                        moneyTypePerc.remove(moneyKey);
                        moneyTypeErr.remove(moneyKey);
						participant.setMoneyTypePercentages(moneyTypePerc);
                        participant.setMoneyTypeErrors(moneyTypeErr);
					}
				}
			}
			col++;
		}

		// list the occurence numbers of each column for later use
		Map highMoneyOcurrenceNumbers = new HashMap();
		Iterator columns = moneyTypeColumns.keySet().iterator();
		List moneyOccurrenceNumbers = new ArrayList();
		for (int i = 0; i < moneyTypeColumns.size(); i++) {
			moneyOccurrenceNumbers.add(new Integer(0));
		}
		while (columns.hasNext()) {
			Integer column = (Integer) columns.next();
			Integer occurrenceNo = (Integer) highMoneyOcurrenceNumbers.get(moneyTypeColumns.get(column));
			if (occurrenceNo == null)
				occurrenceNo = new Integer(0);
			else
				occurrenceNo = new Integer(occurrenceNo.intValue() + 1);
			// add the new occurence number to the list and update the map
			highMoneyOcurrenceNumbers.put(moneyTypeColumns.get(column),occurrenceNo);
			moneyOccurrenceNumbers.set(column.intValue(), occurrenceNo);

		}

		// then we need to add or replace any money percentages for only the participants shown on the page
		List vestingColumns = theForm.getVestingColumnsMap();
		Map employerIds = theForm.getEmployerIdsMap();
		Map recordNumbers = theForm.getRecordNumbersMap();
		List deleteBoxes = theForm.getDeleteBoxesMap();
        List vestingDates = theForm.getVestingDatesMap();
        List applyLTPTCreditings = theForm.getApplyLTPTCreditingsMap();
        List vestedYearsOfService = theForm.getVestedYearsOfServiceMap();
        
		Iterator rows = employerIds.keySet().iterator();
		while (rows.hasNext()) {
			Integer row = (Integer) rows.next();
			String employerId = (String) employerIds.get(row);
			Integer recordNumber = (Integer) recordNumbers.get(row);
			Boolean deleteFlag = (Boolean) deleteBoxes.get(row);
            String vestingDate = (String) vestingDates.get(row);
            String applyLTPTCrediting = (String) applyLTPTCreditings.get(row);
            String vyosValue = (vestedYearsOfService != null) ? (String) vestedYearsOfService.get(row) : null;
            
			// get the participant
			VestingParticipant participant = getParticipant(employerId, recordNumber, vesting);

			// remove old percentages/errors
			participant.setMoneyTypePercentages(new TreeMap());
            participant.setMoneyTypeErrors(new TreeMap());
			// update the perc with the new perc from the page
			columns = vestingColumns.iterator();
			for(int i=0;i<vestingColumns.size();i++){
				//Integer column = (Integer) columns.next();
				String keyId = (String) moneyTypeColumns.get(i);
				if (!keyId.equals(DEFAULT_MONEY_DD_ID)) {
					
					Integer ocurrenceNo = (Integer) moneyOccurrenceNumbers.get(i);
					String moneyKey = keyId + "/" + ocurrenceNo.toString();
					String value = ((VestingDetailsForm.RowVal)vestingColumns.get(i)).getRow(row.intValue());
					participant.getMoneyTypePercentages().put(moneyKey, value);
                    participant.getMoneyTypeErrors().put(moneyKey,ERROR_COND_STRING_OK);
				}
			}

            // update the percentage date, vyos and vyos date from the page
            // and strip off coresponding error message
            if (ServiceFeatureConstants.PROVIDED.equals(vesting.getVestingCSF())) {
                participant.setPercDate(VestingDetailsHelper.formatVestingDateStp(vestingDate));
                participant.setErrorCondString(stripErrorCondition(participant.getErrorCondString(),"vestingDate"));
            } else if (ServiceFeatureConstants.CALCULATED.equals(vesting.getVestingCSF())) {
                participant.setVyosDate(VestingDetailsHelper.formatVestingDateStp(vestingDate));
                participant.setVyos(vyosValue);
                participant.setErrorCondString(stripErrorCondition(participant.getErrorCondString(),"vyos"));
            }
            participant.setApplyLTPTCrediting(applyLTPTCrediting);

			if (deleteFlag) {
				// flag this participant for deletetion
				participant.setMarkedForDelete(true);
			}

		}

	}

    /*
     * Strip off given error condition string of any errors for given field
     */
    private String stripErrorCondition(String errorString, String field) {

        if (StringUtils.isEmpty(field))
            return errorString;

        String new_errorString = "";

        StringTokenizer st = new StringTokenizer(errorString,FIELD_SEPARATOR);
        int index = 0;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!token.contains(field)) {
                if (index == 0) {
                    new_errorString = new_errorString + token;
                } else {
                    new_errorString = new_errorString + "," + token;
                }
            }
            index++;
        }

        // if no other errors found, set it to OK
        if (new_errorString.equals(ERROR_COND_STRING_ERROR))
            new_errorString = ERROR_COND_STRING_OK;

        return new_errorString;
    }
    
    
    @RequestMapping(value ="/editVesting/",params= {"task=re-submit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doReSubmit (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
		return doSubmit(form, bindingResult, request, response);
    	
    
    }
    @RequestMapping(value ="/editVesting/",params= {"task=submit"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSubmit (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
        
		// check if Vesting CSF has changed
        final String currentVestingCSF = CensusUtils.getVestingCSF(contractNumber);
        final boolean isCSFChanged = (!(StringUtils.equals(currentVestingCSF, form.getVestingCSF())));
        
        // if the vesting CSF changed, we redirect the user to the view page.
        if (isCSFChanged) {
            ControllerRedirect redirect = new ControllerRedirect(ACTION_PATH_VIEW_VESTING_DETAILS);
            redirect.addParameter(SUBNO, submissionId);
            return redirect.getPath();
        }

		// first save any changes
		String forward = doSaveForSubmit( form, bindingResult, request, response);

		// an error was found in the save, so don't continue
		// redisplay the edit page with the error
		if (forward.equals(forwards.get(REFRESH))) {
			return forward;
		}

		form.clear(request);

		ReportCriteria criteria = new ReportCriteria(VestingDetailsReportData.REPORT_ID);

		criteria.addFilter(
                VestingDetailsReportData.FILTER_CONTRACT_NUMBER,
				new Integer(contractNumber)
		);

		criteria.addFilter(
                VestingDetailsReportData.FILTER_SUBMISSION_ID,
				new Integer(submissionId)
		);

		// get the existing complete vesting detail item
        VestingDetailsReportData bean = null;
		ReportServiceDelegate service = null;
		try{
			service = ReportServiceDelegate.getInstance();
			bean = (VestingDetailsReportData) service.getReportData(criteria);
			request.setAttribute(Constants.REPORT_BEAN, bean);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit vesting item");
		}

		VestingDetailItem vesting = bean.getVestingData();

		// invoke the data checker to check the max error level SP4.2.29.2
		int errorLevel = 0;
		// invoke the datachecker RMI service synchronously
		ICon2DataProblem dataProblem = null;
		try {
			dataProblem = DataCheckerServiceDelegate.getInstance().checkData(
					Integer.toString(vesting.getTransmissionId()),
					Integer.toString(contractNumber), SUBMISSION_TYPE_VESTING);
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
			return forwards.get("editVesting");
		}

		// get the vesting detail item after the datachecker has run
		try{
			if (service == null) {
				service = ReportServiceDelegate.getInstance();
			}
			bean = (VestingDetailsReportData) service.getReportData(criteria);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit vesting item");
		}

        // get the latest vesting data
        vesting = bean.getVestingData();
        
		// error level < error tolerance, forward to the confirmation page
		String userId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String userName = userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName();
		String clientId = userProfile.getClientId();

		// populate a bean for display on the confirmation page
		EditVestingDetailBean confirmBean = new EditVestingDetailBean();

		confirmBean.setConfirmationNumber(vesting.getSubmissionId().toString());
		// the format of the sender name is LN, FN for the confirmation
		confirmBean.setSender(userProfile.getPrincipal().getLastName() + ", " + userProfile.getPrincipal().getFirstName());
        // displaying the initial vesting submission date, not the resubmit date
		confirmBean.setReceivedDate(vesting.getSubmissionDate());
		confirmBean.setNumberOfRecords(String.valueOf(vesting.getNumberOfRecords()));
		confirmBean.setSubmissionType("Vesting");

		SessionHelper.setEditVestingDetails(request, confirmBean);

		request.getSession(false).setAttribute(Constants.REPORT_BEAN, bean);


		Date startTime = new Date();

		// invoke the datachecker RMI service synchronously to set the status and backup the data
		try {
			DataCheckerServiceDelegate.getInstance().dataCheckWrapUp(Integer.toString(vesting.getTransmissionId()),
				Integer.toString(contractNumber), SUBMISSION_TYPE_VESTING, dataProblem, userId, userName);
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
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(vesting,true);
		if (lock != null) {
			vesting.setLock(lock);
			boolean unlocked = LockManager.getInstance(request.getSession(false)).release(vesting);
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

    @RequestMapping(value ="/editVesting/",params= {"task=setReportPageSize"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSetReportPageSize (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

	    profile.getPreferences().put(UserPreferenceKeys.REPORT_PAGE_SIZE,
	    		newPageSizeString);

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
    @RequestMapping(value ="/editVesting/",params= {"task=confirm"},method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doConfirm (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		if (null == request.getSession(false).getAttribute(Constants.EDIT_VESTING_CONFIRM_DETAIL_DATA)
				|| null == request.getSession(false).getAttribute(Constants.REPORT_BEAN)
				|| !(request.getSession(false).getAttribute(Constants.REPORT_BEAN) instanceof VestingDetailsReportData)) {

			forward = forwards.get(HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forward;
	}


	protected VestingDetailsForm resetForm(
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		try {
			// we'll do our own custom reset
			// we need to save the submission number
			
			form.clear();
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"resetForm", "exception in resetting the form");
		}

		return (VestingDetailsForm) form;
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
		return VestingDetailsReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return VestingDetailsReportData.REPORT_NAME;
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

//       get the user profile object
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        VestingDetailsForm vestingDetailsForm = (VestingDetailsForm) form;

		criteria.addFilter(
				VestingDetailsReportData.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber())
		);

		String subNo = request.getParameter(SUBNO);
		// see if we're doing the default get where the submission number is from the request
		if (subNo != null && subNo.length() != 0) {
			vestingDetailsForm.setShowConfirmDialog(false);
		}
		// we expect the submission number to be present in the request.
		// if not, get it from the session form
		if (subNo == null || subNo.length() == 0)
			subNo = (String)vestingDetailsForm.getSubNo();

		// if not, get it from the session
		if (subNo == null || subNo.length() == 0) {
			subNo = (String)request.getSession(false).getAttribute(SUBNO);
		}

		// the first time or if we change submissions, don't show the confirmation dialog
		if (vestingDetailsForm.getSubNo() == null || !vestingDetailsForm.getSubNo().equals(subNo)) {
			vestingDetailsForm.setShowConfirmDialog(false);
		}

		vestingDetailsForm.setSubNo(subNo);
		criteria.addFilter(
				VestingDetailsReportData.FILTER_SUBMISSION_ID,
				new Integer(vestingDetailsForm.getSubNo())
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

		VestingDetailsForm form = (VestingDetailsForm) reportForm;
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

		boolean isParticipantCountTooBig = SubmissionServiceDelegate.getInstance().isParticipantCountTooBigForEdit(Integer.parseInt(subNo), userProfile.getCurrentContract().getContractNumber());
        if (isParticipantCountTooBig) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.VESTING_ABOVE_SIZE_LIMIT,
					new String[] { VESTING_SIZE_LIMIT }));

            setErrorsInSession(request, errors);
            return forwards.get( CANCEL);
        }

		String forward = super.doCommon( form, request, response);
		VestingDetailsReportData theReport = (VestingDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
        VestingDetailItem theItem = null;

        if (theReport == null) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        } else {
            theItem = theReport.getVestingData();
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
		boolean locked = LockManager.getInstance(request.getSession(false)).lock(theReport.getVestingData(), String.valueOf(userProfile.getPrincipal().getProfileId()));
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
        VestingDetailsReportData reportData = (VestingDetailsReportData) report;

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
            // if standard vesting file was submitted (no TPA system name available)
            if (reportData.getVestingData().getTpaSystemName() == null) {
                appendBuffer(buffer,reportData.getTransactionNumber()).append(COMMA);
            }
            appendBuffer(buffer, reportData.getContractNumber()).append(COMMA);

            VestingParticipant participant = (VestingParticipant) items.next();
            if (participant.getSsn() != null) {
                buffer.append(SSNRender.format(participant.getSsn(), "", false));
            }
            buffer.append(COMMA);
            
            appendBuffer(buffer, participant.getFirstName()).append(COMMA);
            appendBuffer(buffer, participant.getLastName()).append(COMMA);
            appendBuffer(buffer, participant.getMiddleInitial()).append(COMMA);
            appendBuffer(buffer, participant.getEmpId()).append(COMMA);

            buffer.append(VestingDetailsHelper.formatVestingDateForWeb(participant,reportData.getVestingData().getVestingCSF()));

            if (VestingConstants.VestingServiceFeature.CALCULATION.equals(reportData.getVestingData().getVestingCSF())) {
                
                buffer.append(COMMA);
                buffer.append(NumberRender.formatByType(participant.getVyos(), "", 
                        RenderConstants.INTEGER_TYPE));
                
            } else if (VestingConstants.VestingServiceFeature.COLLECTION.equals(reportData.getVestingData().getVestingCSF())) {
                
                // only download valid money types and percentages
                if (reportData.getVestingData().getCountOfValidMoneyTypes() > 0) {
                    buffer.append(COMMA);
                    
                    Map vestings = participant.getMoneyTypePercentages();
                    if (reportData.getVestingData().getPercentageMoneyTypes() != null
                        && reportData.getVestingData().getPercentageMoneyTypes().size() > 0) {
                        for (Iterator itr = reportData.getVestingData().getPercentageMoneyTypes().iterator();  itr.hasNext(); ){
                            MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
                            boolean moneyTypeExists = false;
                            for (Iterator cmtitr = reportData.getVestingData().getContractMoneyTypes().iterator();
                                    cmtitr.hasNext() && !moneyTypeExists; ) {
                                String contractMoneyType = ((MoneyTypeVO)cmtitr.next()).getContractShortName().trim();
                                if (moneyType.getMoneyType().getContractShortName().trim().equals(contractMoneyType)) {
                                    moneyTypeExists = true;
                                }
                            }
    
                            if (moneyTypeExists) {
                                String moneyTypeKey = moneyType.getKey();
                                String vestingAmount = (String)vestings.get(moneyTypeKey);
                                buffer.append(vestingAmount);
                            }
                            if (itr.hasNext()) {
                                buffer.append(COMMA);
                            }
                        }
                    }
                }
            }    
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
		return "Vesting_Submission_for_" + String.valueOf(contractId)
				+ "_for_" + dateString + CSV_EXTENSION;
	}


	protected void setupUserProfile(UserProfile userProfile, VestingDetailsForm form, HttpServletRequest request)
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

	protected void populateForm(UserProfile userProfile, VestingDetailsForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateForm");
		}
		
		VestingDetailsReportData theReport = form.getTheReport();
		VestingDetailItem theItem = theReport.getVestingData();

		Contract contract = userProfile.getCurrentContract();

		// fill in the columns for vestings
		List deleteBoxes =  new ArrayList();
		Map employerIds = new HashMap();
		Map recordNumbers = new HashMap();
        List vestingDates =  new ArrayList();
        List vestedYearsOfService = new ArrayList();
        List applyLTPTCreditings =  new ArrayList();
        
		Iterator participants = theReport.getDetails().iterator();
		int row = 0;
		String[] amountErrorCodes = new String[] { "VP", "VR", "VT", "VV", "C3", "A2" };
		List<String> codesToCheck = Arrays.asList(amountErrorCodes);

		while (participants.hasNext()) {
			VestingParticipant participant = (VestingParticipant) participants.next();
			int countColumn = 0;
			// setup all of the vesting money type percentages for csf = "provided"
			Iterator moneyTypes = theItem.getPercentageMoneyTypes().iterator();
            if (!participant.isIgnored()) {
    			deleteBoxes.add(new Integer(row),false);
    			employerIds.put(new Integer(row), participant.getEmployerDesignatedId());
    			recordNumbers.put(new Integer(row), new Integer(participant.getRecordNumber()));
                vestingDates.add(new Integer(row), VestingDetailsHelper.formatVestingDateForWeb(participant,theItem.getVestingCSF()));
                // setup vyos for csf = "calculated"
                vestedYearsOfService.add(new Integer(row), participant.getVyos());
                applyLTPTCreditings.add(new Integer(row), VestingDetailsHelper.checkApplyLTPTCreditingForWeb(participant));
                        
    			while (moneyTypes.hasNext()) {
    				MoneyTypeHeader moneyType = (MoneyTypeHeader) moneyTypes.next();
    				String amount = (String) participant.getMoneyTypePercentages().get(moneyType.getKey());
    				if (!StringUtils.isEmpty(amount)) {
    					boolean isError = SubmissionErrorHelper
    							.evaluateMatchedField(null, moneyType.getKey(),
    									participant.getRecordNumber(), theReport,
    									codesToCheck, new Predicate() {
    										public boolean evaluate(Object object) {
    											SubmissionError error = (SubmissionError) object;
    											return SubmissionErrorHelper
    													.isError(error);
    										}
    									});
    					//if (!isError) {
						try {
							BigDecimal number = new BigDecimal(amount);
							DecimalFormat df = new DecimalFormat("#.##############");
							amount = df.format(number);
						} catch(NumberFormatException e) {
							// do nothing. Display the amount as is.
						}
    					//}
    				}
    				VestingDetailsForm.RowVal rowVal = (VestingDetailsForm.RowVal) form.getVestingColumns(countColumn);
    				rowVal.setRow(row,amount);
    				countColumn++;
    			}
            }else{
            	//This else block is created to handle Array index for these below ArrayList collections
            	deleteBoxes.add(new Integer(row),null);
                vestingDates.add(new Integer(row), null);

                // setup vyos for csf = "calculated"
                vestedYearsOfService.add(new Integer(row), null);
                applyLTPTCreditings.add(new Integer(row), null);
                while (moneyTypes.hasNext()) {
                	MoneyTypeHeader moneyType = (MoneyTypeHeader) moneyTypes.next();
    				VestingDetailsForm.RowVal rowVal = (VestingDetailsForm.RowVal) form.getVestingColumns(countColumn);
    				rowVal.setRow(row,null);
    				countColumn++;
    			}
            }
            row++;
		}
		form.setDeleteBoxesMap(deleteBoxes);
		form.setEmployerIdsMap(employerIds);
		form.setRecordNumbersMap(recordNumbers);
        form.setVestingDatesMap(vestingDates);
        form.setApplyLTPTCreditingsMap(applyLTPTCreditings);
        form.setVestedYearsOfServiceMap(vestedYearsOfService);
        
		
		// allow to download only if submission status is complete,
        // user has "Submit/Update Vesting" permission and total count > 0
        VestingDetailsHelper vestingHelper = new VestingDetailsHelper();
        boolean allowedToDownload = VestingDetailsHelper.STATUS_COMPLETE.equals(theItem.getSystemStatus()) &&
                                    userProfile.isAllowedSubmitUpdateVesting() && (theReport.getTotalCount() > 0);
        vestingHelper.setAllowedToDownload(allowedToDownload);

        request.getSession(false).setAttribute(Constants.VESTING_DETAILS_HELPER,vestingHelper);

		// add the money type headers for each of the vesting columns
        form.getMoneyTypeColumns().clear();
		int columns = theItem.getPercentageMoneyTypes().size();
        int column = 0;
		while (column < columns) {
			MoneyTypeHeader moneyType = (MoneyTypeHeader) theItem.getPercentageMoneyTypes().get(column);
			form.getMoneyTypeColumns().put(column,moneyType.getMoneyType().getVestingIdentifier(theItem.getTpaSystemName()));
            column++;
		}
		
        form.setViewMode(false);
        // only Cancel Submission button is available if:
        // - submission status is SYNTACTIC_CHECK_FAILED or
        // - total record count is zero or
        // - datachecker error level >= 35 (fatal) or
        // - historical CSF not valid (different from "provided" or "calculated")
        // - actual live CSF changed from the historical CSF
        
        // get error level
        int errorLevel = 0;
        try {
            errorLevel = Integer.parseInt(theReport.getVestingData().getErrorLevel());
        } catch ( NumberFormatException nfe){
            // the string does not have the appropriate format
        }
        
        // check historical CSF
        boolean isValidHistoricalCSF = ServiceFeatureConstants.PROVIDED.equals(theItem.getVestingCSF()) ||
                                       ServiceFeatureConstants.CALCULATED.equals(theItem.getVestingCSF());
        form.setValidHistoricalCSF(isValidHistoricalCSF);
        form.setVestingCSF(theItem.getVestingCSF());
        form.setDisplayApplyLTPTCreditingField(false);
        
        if (VestingConstants.VestingServiceFeature.CALCULATION.equals(theItem.getVestingCSF())) {
        	if (VestingConstants.CreditingMethod.HOURS_OF_SERVICE.equalsIgnoreCase(theItem.getVestingCreditingMethod())) {
        		if(!Constants.PRODUCT_RA457.equalsIgnoreCase(contract.getProductId())){
        			form.setDisplayApplyLTPTCreditingField(true);
        		}
        	}
        }
        
        // if historical CSF invalid, do not display any details
        if (!isValidHistoricalCSF) {
            theReport.setDetails(new ArrayList());
        }
            
        // check if CSF changed
        final String currentVestingCSF = CensusUtils.getVestingCSF(contract.getContractNumber());
        final boolean isCSFChanged = (!(StringUtils.equals(currentVestingCSF, theItem.getVestingCSF())));

        if (VestingDetailsHelper.STATUS_SYNTACTIC_CHECK_FAILED.equals(theItem.getSystemStatus()) ||
             (theReport.getTotalCount() == 0)  ||
             errorLevel >= 35  ||
             !isValidHistoricalCSF ||
             isCSFChanged) {
            form.setViewMode(true);
        }
        
        // do not mask SSN in edit screen
        form.setMaskSSN(false);

        // find future plan year end
        //final PlanData planData = ContractServiceDelegate.getInstance()
        //.readPlanData(new Integer(contract.getContractNumber()), true);
        final PlanDataLite planDataLite = ContractServiceDelegate.getInstance()
        .getPlanDataLight(new Integer(contract.getContractNumber()));
        Date futurePlanYearEnd = new PlanYearEndInfo(planDataLite.getPlanYearEnd(), new Date()).getFuturePlanYearEnd();
        form.setFuturePlanYearEnd(SLASH_DATE_FORMATTER.format(futurePlanYearEnd));
        
        
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

		VestingDetailsForm form = (VestingDetailsForm) actionForm;
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
	   private PSValidatorFWInput  psValidatorFWInput;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	private static Date [] getValidPayrollDates (
			Date defaultInvestmentDate,
			ContractStatementInfoVO statementInfoVO,
			Contract currentContractVO) throws SystemException
	{
		Date [] outDates = null;

		Calendar calEndDate = new GregorianCalendar();
		calEndDate.setTime(defaultInvestmentDate);
		calEndDate.add(Calendar.DATE, DELTA_END_DAYS);

		// SP4.2.31.6
		Date minDate = currentContractVO.getEffectiveDate();
		// SP4.2.31.7
		Date maxDate = calEndDate.getTime();
		// SP4.2.31.1
		Calendar currentDate = new GregorianCalendar();
		Calendar planYearEnd = new GregorianCalendar();
		planYearEnd.setTime(statementInfoVO.getPlanReportingYearEndDate());

		if (currentDate.get(Calendar.MONTH) > planYearEnd.get(Calendar.MONTH))
			planYearEnd.set(Calendar.YEAR,currentDate.get(Calendar.YEAR));
		else
			planYearEnd.set(Calendar.YEAR,currentDate.get(Calendar.YEAR) - 1);

		Calendar firstDayOfLastQuarterOfPlanYearEnd = (Calendar) planYearEnd.clone();

		firstDayOfLastQuarterOfPlanYearEnd.add(Calendar.MONTH,MINUS_THREE_MONTHS);
		firstDayOfLastQuarterOfPlanYearEnd.add(Calendar.DATE,1);

		if (
				(
					// Accrual reporting basis
					statementInfoVO.getReportBasisCode().startsWith(BASIS_ACCRUAL_CODE) ||
					// Combo reporting basis
					statementInfoVO.getReportBasisCode().startsWith(BASIS_COMBO_CODE)
				) &&
				minDate.before(firstDayOfLastQuarterOfPlanYearEnd.getTime())
		) {
			minDate = firstDayOfLastQuarterOfPlanYearEnd.getTime();
		}

		// 4.2.31.5
		Calendar lastStatementEffectiveDate = new GregorianCalendar();
		lastStatementEffectiveDate.setTime(statementInfoVO.getLastStatementEndDate());
		Calendar lastStatementProductionDate = new GregorianCalendar();
		lastStatementProductionDate.setTime(statementInfoVO.getLastStatementProductionDate());
		if (
				(	// Accrual reporting basis
					statementInfoVO.getReportBasisCode().startsWith(BASIS_ACCRUAL_CODE)
						&&
					minDate.before(lastStatementEffectiveDate.getTime())
					  	&&
					lastStatementProductionDate.getTime().before(new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31, 0, 0).getTime())
				)
					||
				(	// Combo reporting basis
					statementInfoVO.getReportBasisCode().startsWith(BASIS_COMBO_CODE)
						&&
					minDate.before(lastStatementEffectiveDate.getTime())
						&&
					lastStatementEffectiveDate.getTime().compareTo(planYearEnd.getTime()) == 0)
						&&
					lastStatementProductionDate.getTime().before(new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31, 0, 0).getTime())
		) {
			Calendar dayAfterLastStatementEffectiveDate = new GregorianCalendar();
			dayAfterLastStatementEffectiveDate.setTime(statementInfoVO.getLastStatementEndDate());
			dayAfterLastStatementEffectiveDate.add(Calendar.DATE,1);
			minDate = dayAfterLastStatementEffectiveDate.getTime();
		}

		// follow similar logic for cash statements
		if (statementInfoVO.getReportBasisCode().startsWith(BASIS_CASH_CODE)) {
			Calendar defaultDate = new GregorianCalendar(9999, 11, 31);
			Calendar lastStatementEndDate = new GregorianCalendar();
			lastStatementEndDate.setTime(statementInfoVO.getLastStatementEndDate());
			Calendar adjustedLastStatementEndDate = new GregorianCalendar();
			adjustedLastStatementEndDate.setTime(lastStatementEndDate.getTime());
			adjustedLastStatementEndDate.add(Calendar.DATE, -90);
			if (adjustedLastStatementEndDate.getTime().before(defaultDate.getTime())
					&& adjustedLastStatementEndDate.getTime().after(minDate)
					&& adjustedLastStatementEndDate.getTime().before(maxDate)) {
				minDate = adjustedLastStatementEndDate.getTime();
			}
		}


		if (currentContractVO.getStatus().equals(Contract.STATUS_CONTRACT_APPROVED)) {
			Date contractApprovedMinDate = currentContractVO.getContractStatusEffectiveDate();
			if (currentContractVO.getMinimumCreditEffectiveDate().compareTo(contractApprovedMinDate) > 0
					&& currentContractVO.getMinimumCreditEffectiveDate().compareTo(new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31, 0, 0).getTime()) < 0) {
				contractApprovedMinDate = currentContractVO.getMinimumCreditEffectiveDate();
			}
			// SP4.2.31.2
			Calendar calMaxDate = new GregorianCalendar();
			calMaxDate.setTime(defaultInvestmentDate);
			calMaxDate.add(Calendar.DATE, 1); // add 1 day to allow the while loop below to work

			maxDate = calMaxDate.getTime();

			// SP4.2.31.3 & SP4.2.31.4
			if (minDate.before(contractApprovedMinDate)
					|| (minDate.compareTo(new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31, 0, 0).getTime()) == 0)) {
				minDate = contractApprovedMinDate;
			}
		}

		// create the unfiltered dates for the calendar
		ArrayList dates = new ArrayList();
		Calendar minCal = new GregorianCalendar();
		minCal.setTime(minDate);
		Calendar maxCal = new GregorianCalendar();
		maxCal.setTime(maxDate);
		while(minDate.before(maxDate) && minCal.before(maxCal)) {
			dates.add(minCal.getTime());
			minCal.add(Calendar.DATE, 1);
		}

		Date [] dateArray = (Date []) dates.toArray( new Date [0]);

		return dateArray;
	}



	private static VestingParticipant getParticipant(String employerId, Integer recordNumber, VestingDetailItem vestingItem) {
		if (employerId == null || recordNumber == null || vestingItem == null) return null;

        VestingParticipant returnedParticipant = null;
		Iterator participants = vestingItem.getSubmissionParticipants().iterator();

		while (participants.hasNext()) {
            VestingParticipant participant = (VestingParticipant) participants.next();
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
			System.out.println("EditVestingDetails.getBigDecimal threw NumberFormatException trying to parse ["
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

	    VestingDetailsForm registerForm = (VestingDetailsForm) form;

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

	protected void logEvent(EditVestingDetailBean confirmBean, UserProfile userProfile, Date startTime,
			Date endTime, String methodName) {


		String logData = prepareLogData(confirmBean, userProfile, startTime, endTime);
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName, logData,
				OnlineSubmissionEventLog.class);
	}

	protected void logEvent(VestingDetailsForm theForm, UserProfile userProfile, Date startTime,
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

	protected String prepareLogData(EditVestingDetailBean confirmBean, UserProfile userProfile, Date startTime,
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

	protected String prepareLogData(VestingDetailsForm theForm, UserProfile userProfile, Date startTime,
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

	@RequestMapping(value ="/editVesting/",params= {"task=reset"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doReset (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	@RequestMapping(value ="/editVesting/",params= {"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
/*        form.clear( request);*/

        // note: refresh of lock done in doCommon, so don't
        //       bother here
        String forward = doCommon( form, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }
        return forward;
    }
	
	@RequestMapping(value ="/editVesting/",params= {"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("vestingDetailsForm") VestingDetailsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
}
