package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.common.MoneyBean;
import com.manulife.pension.lp.model.gft.CashAccount;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.PaymentAccount;
import com.manulife.pension.lp.model.gft.PaymentInstruction;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.common.util.log.OnlineSubmissionEventLog;
import com.manulife.pension.ps.service.report.contract.valueobject.ContributionTemplateReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.CopiedSubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsProperties;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.DataCheckerServiceDelegate;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessDataCheckerException;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.StatementPairVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.util.FormatUtils;
import com.manulife.pension.stp.datachecker.ICon2DataProblem;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.NumberRender;

/**
 *
 * @author Tony Tomasone
 *
 */

@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"editContributionDetailsForm"})
public class EditContributionDetailsController extends ReportController {
	
	@ModelAttribute("editContributionDetailsForm")
	public  EditContributionDetailsForm populateForm()
	{
		return new  EditContributionDetailsForm();
		}
	public static Map<String,String> forwards = new HashMap<>();
	static{ 
		forwards.put("input","/tools/editContributionDetails.jsp");
		forwards.put("tools" ,"redirect:/do/tools/toolsMenu/" );
		forwards.put("default" ,"/tools/editContributionDetails.jsp" );
		forwards.put("undo" ,"/tools/editContributionDetails.jsp" );
		forwards.put("sort" ,"/tools/editContributionDetails.jsp" );
		forwards.put("filter" ,"/tools/editContributionDetails.jsp" );
		forwards.put("page" ,"/tools/editContributionDetails.jsp" );
		forwards.put("print" ,"/tools/editContributionDetails.jsp" );
		forwards.put("error" ,"/tools/editContributionDetails.jsp" );
		forwards.put("addParticipant" ,"redirect:/do/tools/addParticipant/" );
		forwards.put("refresh" ,"redirect:/do/tools/editContribution/?task=refresh" );
		forwards.put("cancel" ,"redirect:/do/tools/submissionHistory/" );
		forwards.put("editContribution" ,"redirect:/do/tools/editContribution/" );
		forwards.put("confirm" ,"redirect:/do/tools/editContribution/?task=confirm" );
		forwards.put("confirmPage" ,"/tools/editContributionConfirmation.jsp" );
		forwards.put("history" ,"redirect:/do/tools/submissionHistory/" );
		forwards.put("addMoneyType" ,"redirect:/do/tools/editContribution/?task=addMoneyType" );
		forwards.put("addLoan" ,"redirect:/do/tools/editContribution/?task=addLoan" );
		forwards.put("prepareAddParticipant" ,"redirect:/do/tools/editContribution/?task=prepareAddParticipant" );
		forwards.put("setReportPageSize" ,"/do/tools/editContribution/?task=setReportPageSize" );
		forwards.put("reloadPageResetPageNumber" ,"/WEB-INF/global/reloadPageResetPageNumber.jsp" );
	} 
	
	

	private static final String PAGENUMBER = "pageNumber";
	private static final String CREDIT_REMOVED_WARNING = "<br>"+Constants.WARNING_ICON+"Amounts previously saved for temporary credit epayments were removed as the temporary credit balance is zero or not available.";
	private static final String BILL_REMOVED_WARNING = "<br>"+Constants.WARNING_ICON+"Amounts previously saved for bill payment were removed as the bill payment balance is zero or not available.";
	private static final String ACCOUNT_REMOVED_WARNING = "<br>"+Constants.WARNING_ICON+"Amounts previously saved for accounts you do not have access to were removed.";
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
	private static final String CURRENCY_FORMAT_CODE = "c";
	private static final String ID_0 = "0";
	private static final String ID_99 = "99";
	private static String DEFAULT_SORT = SubmissionParticipant.SORT_RECORD_NUMBER;
	private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int MAX_WIDTH = 9999;
	private static int BANK_CLOSE_HOUR = 16;
	private static int MAX_ERROR_LEVEL = 30;
	private static int BANK_CLOSE_MINUTE = 0;
	//current day + 15
	private static final int DELTA_END_DAYS = 16;
	private static String DEFAULT_MONEY_DD_LABEL = "Select Type";
	private static String DEFAULT_MONEY_DD_ID = "-1";
	private static final String BASIS_ACCRUAL_CODE = "RB AC";
	private static final String BASIS_COMBO_CODE = "RB CO";
	private static final String BASIS_CASH_CODE = "RB CA";
	private static final int MINUS_THREE_MONTHS = -3;
	private static final String STATEMENTS_REQUESTED =
		"Participant statements for the relevant quarter(s) will be generated.";
	private static final String STATEMENTS_NOT_REQUESTED =
		"Participant statements will not be triggered at this time.";
	private static final String TOOLS = "tools";
	private static final String SUBMISSION_TYPE_CONTRIBUTION = "C";
	private static final String DRAFT_STATUS 	= "14";
	private static final String COPIED_STATUS 	= "97";
	private static final String ERROR_04_STATUS = "04";
	private static final String ERROR_05_STATUS = "05";
	private static final String ERROR_07_STATUS = "07";
	private static final String ERROR_09_STATUS = "09";
	private static final Date highDate = new GregorianCalendar(9999,Calendar.DECEMBER,31).getTime();
    private static final String HISTORY = "history";
	public static final String COPY_SIZE_LIMIT_KEY = "copy.size.limit";
	protected static final String COPY_SIZE_LIMIT = System.getProperty(COPY_SIZE_LIMIT_KEY, "1500");
	protected static final String USER_TYPE_INTERNAL = "I";


	/**
	 * Constructor.
	 */
	public EditContributionDetailsController() {
		super(EditContributionDetailsController.class);
	}

	@RequestMapping(value ="/editContribution/", params={"task=addMoneyType"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   	public String doAddMoneyType(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("editContribution");//if input forward not //available, provided default
	       }
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doAddMoneyType");
		}

		String forward = doCommon( actionForm, request, response);

		UserProfile userProfile = getUserProfile(request);
		
		actionForm.setShowConfirmDialog(false);
		ContributionDetailsReportData theReport = actionForm.getTheReport();

		// add a new money type column
		MoneyTypeVO moneyType = new MoneyTypeVO();
		moneyType.setContractShortName(DEFAULT_MONEY_DD_LABEL);
		String moneyTypeId = DEFAULT_MONEY_DD_ID;

		actionForm.getTheReport().getContributionData().getAllocationMoneyTypes().add(new MoneyTypeHeader(moneyTypeId, "0", moneyType));


		// fill in the columns for contributions and loans
		Iterator participants = actionForm.getTheReport().getDetails().iterator();
		int row = 0;
		while (participants.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant) participants.next();

			// setup all of the contribution money type amounts
			Iterator moneyTypes = actionForm.getTheReport().getContributionData().getAllocationMoneyTypes().iterator();
			int contColumn = 0;
			while (moneyTypes.hasNext()) {
				MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader) moneyTypes.next();
				BigDecimal amount = (java.math.BigDecimal) participant.getMoneyTypeAmounts().get(moneyTypeHeader.getKey());
				EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) actionForm.getContributionColumns(contColumn);
				rowVal.setRow(row,NumberRender.formatByPattern(amount,ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				contColumn++;
			}
			row++;
		}
		
		int moneyTypeSize = actionForm.getMoneyTypeColumnsMap().size();
		for(int i=0;i<actionForm.getContributionColumnsMap().size();i++){
			MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader) actionForm.getTheReport().getContributionData().getAllocationMoneyTypes().get(i);
			
			if (i < moneyTypeSize) {
				actionForm.getMoneyTypeColumnsMap().set(i,moneyTypeHeader.getMoneyType().getId());
			} else {
				actionForm.getMoneyTypeColumnsMap().add(i,moneyTypeHeader.getMoneyType().getId());
			}
		}

		actionForm.setHasChanged(true);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doAddMoneyType");
		}

		return forwards.get(DEFAULT);
	}

	
		@RequestMapping(value ="/editContribution/", params= {"task=addLoan"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	   	public String doAddLoan(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doAddLoan");
		}

		String forward = doCommon( actionForm, request, response);

		UserProfile userProfile = getUserProfile(request);
		
		actionForm.setShowConfirmDialog(false);
		ContributionDetailsReportData theReport = actionForm.getTheReport();

		// add a new loan column
		theReport.getContributionData().setMaximumNumberOfLoans(
				actionForm.getTheReport().getContributionData().getMaximumNumberOfLoans()+1
		);

		theReport.getContributionData().getLoanTotalValues().add(new BigDecimal(0d));

		// fill in the columns for contributions and loans
		Iterator participants = theReport.getDetails().iterator();
		int row = 0;
		while (participants.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant) participants.next();

			// setup all of theloans
			Iterator loans = participant.getLoanAmounts().entrySet().iterator();
			String rowValueString = null;
			String keyString = null;
			int i = 0;
			// first loop through the existing loans (if any) setting the column values to existing ids and amounts
			while (loans.hasNext()) {
				EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) actionForm.getLoanColumn(i);
				Map.Entry loanEntry= (Map.Entry) loans.next();
				keyString = (String) loanEntry.getKey();
 				keyString = keyString.substring(0,keyString.indexOf("/"));
				BigDecimal amount = (BigDecimal) loanEntry.getValue();
				rowValueString = NumberRender.formatByType(amount,ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false);
				rowVal.setRow(row,rowValueString);
				rowVal.setRowId(row,keyString);
				i++;
			}
			// then set any remaining columns to default values
			while (i < actionForm.getTheReport().getContributionData().getMaximumNumberOfLoans()) {
				EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) actionForm.getLoanColumn(i);
				rowVal.setRow(row,ZERO_AMOUNT_STRING);
				rowVal.setRowId(row,ID_99);
				i++;
			}
			row++;
		}

		actionForm.setHasChanged(true);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doAddLoan");
		}

		return forwards.get(DEFAULT);
	}

		
		@RequestMapping(value ="/editContribution/", params= {"task=prepareAddParticipant"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	   	public String doPrepareAddParticipant(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrepareAddParticipant");
		}

		
		actionForm.setShowConfirmDialog(false);

		request.getSession(false).setAttribute(SUBNO, actionForm.getSubNo());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doPrepareAddParticipant");
		}

		return forwards.get(ADD_PARTICIPANT);

	}
		@RequestMapping(value ="/editContribution/", params= {"task=undo"}, method =  {RequestMethod.POST}) 
	   	public String doUndo(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUndo");
		}


		UserProfile userProfile = getUserProfile(request);
		
		ContributionDetailsReportData theReport = actionForm.getTheReport();

		// clear the form
		actionForm.clear( request);
		actionForm.setShowConfirmDialog(false);

		// note: refresh of lock done in doCommon, so don't
		//       bother here
		String forward = doCommon( actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doUndo");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
		@RequestMapping(value ="/editContribution/", params= {"task=cancel"}, method =  {RequestMethod.POST}) 
	   	public String doCancel(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancel");
		}

		
		ContributionDetailsReportData theReport = actionForm.getTheReport();

		boolean unlocked = LockManager.getInstance(request.getSession(false)).release(theReport.getContributionData());
		if (!unlocked) {
			logger.debug("problem unlocking");
		}

        final StringBuffer message = new StringBuffer("[Online Submission]");
        message.append(" User cancelled submission ").append(actionForm.getSubNo());
        logger.error(message.toString());
		
		
		// clear the form
        actionForm.setSubNo(null);
        actionForm.clear( request);
        actionForm.setShowConfirmDialog(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCancel");
		}

		return forwards.get(CANCEL);
	}

		@RequestMapping(value ="/editContribution/", params= {"task=download"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	    public String doDownload (@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		               return forwards.get("editContribution");
		       }
			}
	    
	        String forward = super.doDownload(actionform, request, response);

	        return forwards.get(forward);
	    }
		
		@RequestMapping(value ="/editContribution/", params= {"task=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	   	public String doRefresh(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		              return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doRefresh");
		}

		if(request.getParameter(PAGENUMBER) != null && request.getParameter(PAGENUMBER).length() != 0) {
			return doDefault( actionForm, request, response);
		}
		// the framework seems to lose the page number, so I restore it from the value I stored earlier
		
		actionForm.setPageNumber(actionForm.getMyOwnPageNumber());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doRefresh");
		}

		return forwards.get("input");
	}

		@RequestMapping(value ="/editContribution/", params= {"task=save"}, method =  {RequestMethod.POST}) 
	   	public String doSave(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		                            return forwards.get("editContribution");//if input forward not //available, provided default
		       }
			}		
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSave");
		}

		String forward = saveData( actionForm, request, response);
		/*
         * Resets the token after the form is cleared.
         */
		if (!forward.equals(findForward(REFRESH))) {
			//resetToken(request);
		}
		
		
        final StringBuffer message = new StringBuffer("[Online Submission]");
        message.append(" User clicked Save on Submission ").append(actionForm.getSubNo());
        logger.error(message.toString());

		String newForward = actionForm.getForwardFromSave();
		if (forward.equals( forwards.get("editContribution"))
				&& !newForward.equals("")) {
			// if we get the expected result, forward to the requested task (if any)
			actionForm.setForwardFromSave("");

			forward = forwards.get(newForward);
		} else {
			// otherwise, send user to wherever the forward says
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSave");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	   	public String doSaveForSubmit(BaseReportForm reportForm,HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
				
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSaveForSubmit");
		}

		EditContributionDetailsForm actionForm = (EditContributionDetailsForm) reportForm;

		// setting this to true will cause all participants with no amounts to be cleaned up; as we only want this to
		// happen on submit, always set to false
		actionForm.setResubmit(false);

		String forward = saveData( actionForm, request, response);
		/*
         * Resets the token after the form is cleared.
         */
		if (!forward.equals(findForward(REFRESH))) {
			//resetToken(request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSaveForSubmit");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
		private String saveData(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	   	throws SystemException {
				
		Date startTime = new Date();

		// TODO break down this method - should be smaller
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}

		EditContributionDetailsForm actionForm = (EditContributionDetailsForm) reportForm;
		
		final StringBuffer message = new StringBuffer("[Online Submission]");
        message.append("Inside saveData for submission = ").append(actionForm.getSubNo());
        logger.error(message.toString());
        
		actionForm.setShowConfirmDialog(false);
		ContributionDetailsReportData theReport = actionForm.getTheReport();

		// ensure year is 4 digits as the date parser doesn't do this for us
		if (!AbstractSubmitController.validateYear(actionForm.getRequestEffectiveDate())
				|| !AbstractSubmitController.validateYear(actionForm.getPayrollEffectiveDate())) {
			Collection errors = new ArrayList();
			errors.add(new ValidationError("BADDATE",
					ErrorCodes.SUBMISSION_VALID_EFFECTIVE_DATE, new Object [] {}));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		// lets check the permissions
		if(
				userProfile.isInternalUser() ||
				!userProfile.isInternalUser() && userProfile.isSubmissionAccess() && userProfile.isAllowedUploadSubmissions()
		) {
			actionForm.setNoPermission(false);
		} else	{
			actionForm.setNoPermission(true);
			actionForm.setTheReport(new ContributionDetailsReportData(new ReportCriteria(""),0));
			return forwards.get("input");
		}

		// reset if they've seen the confirm dialog already
		actionForm.setShowConfirmDialog(false);

		int submissionId = Integer.valueOf(actionForm.getSubNo()).intValue();
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

		// get the existing complete contribution detail item
		SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
		ContributionDetailItem contribution =
			submissionService.getContributionDetails(submissionId, contractNumber);

		if (contribution.getSubmissionPaymentItem() == null)
			contribution.setSubmissionPaymentItem(new SubmissionPaymentItem());

		// if the contribution is currently copied, set it to draft
		if (contribution.getSystemStatus().equals(COPIED_STATUS))
			contribution.setSystemStatus(DRAFT_STATUS);

		// make changes to the existing item based on the changes in the form
		// first lets collection the contractEnterSection data
		// get the payroll date
		String payrollDateString = actionForm.getPayrollEffectiveDate();
		if (null == payrollDateString) {
			// I don't see how this could happen based on the edits in the javascript, but it has
			// happened in production, so if it does display an error
			Collection errors = new ArrayList();
			errors.add(new ValidationError("CONTRIBUTION_APPLICABLE_DATE", ErrorCodes.SUBMISSION_VALID_EFFECTIVE_DATE));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}
		Date payrollDate = null;
		try {
			payrollDate = sdf.parse(payrollDateString);
		} catch (ParseException e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"doSave", "Exception occurred while parsing the payroll date.");
			throw se;
		}
		contribution.setPayrollDate(payrollDate);

		// get the generate statement flag
		if (actionForm.getLastPayroll() != null && actionForm.getLastPayroll().length() != 0) {
			if (actionForm.getLastPayroll().equals("C"))
				contribution.setGenerateStatementsIndicator(new Boolean(true));
			else if (actionForm.getLastPayroll().equals("S"))
				contribution.setGenerateStatementsIndicator(new Boolean(false));
			else
				contribution.setGenerateStatementsIndicator(null);
		}	else
			contribution.setGenerateStatementsIndicator(null);


		// get the money source
		contribution.setMoneySourceID(actionForm.getMoneySourceID());

		try {
			savePaymentInfo(userProfile, contribution, actionForm, request);
		} catch (UnableToAccessIFileException e) {	
			Collection errors = new ArrayList();
			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),e);
			errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_IFILE));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(HISTORY);
		}

		// calculate the payment totals
		populatePaymentTotals(contribution.getSubmissionPaymentItem());

		saveMoneyTypes(contribution, actionForm);

		// reset errors
		Collection errors = new ArrayList();
		
		// some validations are not being caught by the javascript
		// check for negative loan repayments here
		Iterator prtItr = contribution.getSubmissionParticipants().iterator();
		while (prtItr.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant)prtItr.next();
			Map loanMap = participant.getLoanAmounts();
			if (!loanMap.isEmpty()) {
				Iterator loanItr = loanMap.entrySet().iterator();
				while (loanItr.hasNext()) {
					Map.Entry loanEntry = (Map.Entry) loanItr.next();
					BigDecimal loanAmount = (BigDecimal)loanEntry.getValue();
					// If any negative loan repayments are found:
					// 1. add an error to the session
					// 2. reset the value in the data model to zero
					if (loanAmount.compareTo(BIG_ZERO) < 0) {
						SessionHelper.removeErrorsInSession(request);
						errors.add(new ValidationError("TOTAL_AMOUNT", ErrorCodes.NEGATIVE_LOAN_REPAYMENT_AMOUNTS));
						SessionHelper.setErrorsInSession(request, errors);
						loanEntry.setValue(BIG_ZERO);
					}
				}
			}
		}
		
		// calculate the contribution totals
		boolean totalsTooLarge = populateContributionTotals(contribution);
		if ( totalsTooLarge ) {
			// clear this again since it might have been set above
			SessionHelper.removeErrorsInSession(request);	
			errors.add(new ValidationError("TOTAL_AMOUNT", ErrorCodes.SUBMISSION_MAX_TOTAL_AMOUNTS));
			setErrorsInSession(request, errors);
			// return the user to the where they came from with an error msg
			return forwards.get(REFRESH);
		}
		
		// call the saveContributionDetails method to persist the changes
		submissionService.saveContributionDetails(userId, userName, userType, userTypeID, userTypeName,
				processorUserId, contribution, actionForm.isResubmit());

		//	log the file upload event
		Date endTime = new Date();
		logEvent(actionForm, userProfile, startTime, endTime, "saveData");

		// forward to the jsp
		return forwards.get("editContribution");
	}

	private void saveMoneyTypes(ContributionDetailItem contribution, EditContributionDetailsForm theForm)
			throws SystemException {

		// make changes to the contribution allocations and loans
		// the first thing we need to do is detect changes in the money type drop down
		// so that we can apply it to all of the other participants not shown on the page
		// we need to modify any money type column changes accross all participants in the contribution
		Iterator allocationMoneyTypes = contribution.getAllocationMoneyTypes().iterator();
		List moneyTypeColumns = theForm.getMoneyTypeColumnsMap();
		int col = 0;
		// walk through the existing money types to see if they have been changed
		while (allocationMoneyTypes.hasNext()) {
			MoneyTypeHeader moneyType = (MoneyTypeHeader)allocationMoneyTypes.next();
			String moneyKey = moneyType.getKey();
			String moneyKeyID = moneyKey.substring(0,moneyKey.indexOf("/"));
			String occurrenceNo = moneyKey.substring(moneyKey.indexOf("/")+1, moneyKey.length());
			String selectedMoneyKeyID = (String) moneyTypeColumns.get(new Integer(col));
			// if an existing money type key has been changed then we
			// need to reapply this change to all participants
			if (null != selectedMoneyKeyID && !selectedMoneyKeyID.equals(moneyKeyID) && !selectedMoneyKeyID.equals(DEFAULT_MONEY_DD_ID)) {
				Iterator participants = contribution.getSubmissionParticipants().iterator();
				while (participants.hasNext()) {
					SubmissionParticipant participant = (SubmissionParticipant) participants.next();
					SortedMap moneyTypeAmounts = participant.getMoneyTypeAmounts();
					if (moneyTypeAmounts.get(moneyKey) != null) {
						// find the next available occurrence number for the selected money type
						int checkOccurrence = -1;
						Iterator checkMoneyTypes = contribution.getAllocationMoneyTypes().iterator();
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
						moneyTypeAmounts.put(
								selectedMoneyKeyID + "/" + Integer.toString(checkOccurrence+1).trim(),
								moneyTypeAmounts.get(moneyKey)
						);
						// remove the old changed one
						moneyTypeAmounts.remove(moneyKey);
						participant.setMoneyTypeAmounts(moneyTypeAmounts);
					}
				}
			}
			col++;
		}

		// list the occurence numbers of each column for later use
		Map highMoneyOcurrenceNumbers = new HashMap();
		//Iterator columns = moneyTypeColumns.keySet().iterator();
		List moneyOccurrenceNumbers = new ArrayList();
		for (int i = 0; i < moneyTypeColumns.size(); i++) {
			moneyOccurrenceNumbers.add(new Integer(0));
		}
		for (int i = 0; i < moneyTypeColumns.size(); i++) {
			Integer occurrenceNo = (Integer) highMoneyOcurrenceNumbers.get(moneyTypeColumns.get(i));
			if (occurrenceNo == null)
				occurrenceNo = new Integer(0);
			else
				occurrenceNo = new Integer(occurrenceNo.intValue() + 1);
			// add the new occurence number to the list and update the map
			highMoneyOcurrenceNumbers.put(moneyTypeColumns.get(i),occurrenceNo);
			moneyOccurrenceNumbers.set(i, occurrenceNo);

		}

		// then we need to add or replace any money amounts for only the participants shown on the page
		List contributionColumns = theForm.getContributionColumnsMap();
		List loanColumns = theForm.getLoanColumnsMap();
		Map employerIds = theForm.getEmployerIdsMap();
		Map recordNumbers = theForm.getRecordNumbersMap();
		List deleteBoxes = theForm.getDeleteBoxesMap();
		Iterator rows = employerIds.keySet().iterator();
		while (rows.hasNext()) {
			Integer row = (Integer) rows.next();
			String employerId = (String) employerIds.get(row);
			Integer recordNumber = (Integer) recordNumbers.get(row);
			Boolean deleteFlag = (Boolean) deleteBoxes.get(row);
			// get the participant
			SubmissionParticipant participant = getParticipant(employerId, recordNumber, contribution);

			// remove old amounts
			participant.setMoneyTypeAmounts(new TreeMap());
			// update the amounts with the new amounts from the page
			// first for contributionColumns
			//columns = contributionColumns.iterator();
			//while (columns.hasNext()) {
				for(int i=0;i<contributionColumns.size();i++){
				//Integer column = (Integer) columns.next();
				String keyId = (String) moneyTypeColumns.get(i);
				if (!keyId.equals(DEFAULT_MONEY_DD_ID)) {
					Integer ocurrenceNo = (Integer) moneyOccurrenceNumbers.get(i);
					String moneyKey = keyId + "/" + ocurrenceNo.toString();
					String value = ((EditContributionDetailsForm.RowVal)contributionColumns.get(i)).getRow(row.intValue());
					participant.getMoneyTypeAmounts().put(moneyKey, getBigDecimal(value));
				}
			}
			if (deleteFlag) {
				// flag this participant for deletetion
				participant.setMarkedForDelete(true);
			} else {

				// update the loanColumns
				Map loanOcurrenceNumbers = new HashMap();
				// empty out the loan repayment map as the occurrence numbers may change and we end up with
				// the same loan repayment under both the old and the new occurrence number
				participant.setLoanAmounts(new TreeMap());
				for (int i = 0; i < loanColumns.size(); i++) {
					Integer column = new Integer(i);
					String keyId = ((EditContributionDetailsForm.RowVal)loanColumns.get(column)).getRowId(row.intValue());
					Integer occurrenceNo = (Integer) loanOcurrenceNumbers.get(keyId);
					if (occurrenceNo == null)
						occurrenceNo = new Integer(0);
					else
						occurrenceNo = new Integer(occurrenceNo.intValue() + 1);
					// add the new occurence number to the map
					loanOcurrenceNumbers.put(keyId,occurrenceNo);

					Integer ocurrenceNo = (Integer) loanOcurrenceNumbers.get(keyId);

					String loanKey = keyId + "/" + ocurrenceNo.toString();
					String value = ((EditContributionDetailsForm.RowVal)loanColumns.get(column)).getRow(row.intValue());
					participant.getLoanAmounts().put(loanKey, getBigDecimal(value));
				}
			}
		}

	}

	private void savePaymentInfo(UserProfile userProfile, ContributionDetailItem contribution, 
			EditContributionDetailsForm theForm, HttpServletRequest request)
			throws SystemException, UnableToAccessIFileException {


		// get the existing complete contribution detail item
		Date paymentDate = null;
		UserInfo creatorInfo = null;
		try {
			synchronized (this){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				paymentDate = sdf.parse(theForm.getRequestEffectiveDate());
			}
		} catch (ParseException e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"doSave", "Exception occurred while parsing the payment requested effective date.");
			throw se;
		}
		// make changes to the payment details
		// make the changes only for external users
		// ignore the changes for Other user combinations
		if ( userProfile.isInternalUser()) {								
			int submissionId = Integer.valueOf(theForm.getSubNo()).intValue();
			String creatorId = SubmissionServiceDelegate.getInstance().getSubmissionCreatorId(submissionId);
			Long creatorProfileId = null;
			boolean isAutoloaderSubmission = false;
			if(null != creatorId){
				try{
					creatorProfileId = Long.valueOf(creatorId);
					creatorInfo = SecurityServiceDelegate.getInstance().getUserProfileByProfileId(creatorProfileId);
				} catch (NumberFormatException e) {
					String applicationCode = SubmissionServiceDelegate.getInstance().getApplicationCode(submissionId);
					isAutoloaderSubmission = StringUtils.equals("AL", applicationCode);
				}
				
				if(null != creatorInfo || isAutoloaderSubmission){
					if((isAutoloaderSubmission || creatorInfo.getRole().isExternalUser()) && !(DRAFT_STATUS.equals(contribution.getSystemStatus()))){						
						contribution.getSubmissionPaymentItem().setRequestedPaymentEffectiveDate(paymentDate);
					}
					else {
						contribution.getSubmissionPaymentItem().setRequestedPaymentEffectiveDate(null);
						contribution.getSubmissionPaymentItem().setPaymentInstructions(null);
					}
				}
				else {	
					// to log the submission id & creator id of the submission in the MRL database, if uInfo object is null.
					String logSubmissionData = "Missing user profile for the creator of this submission [ "
						+ " Submission ID: " + theForm.getSubNo()
						+ " Contract number: " + userProfile.getCurrentContract().getContractNumber()
						+ " Submission Creator Id : " + creatorId + " ]";
					AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), "savePaymentInfo()",
							logSubmissionData, OnlineSubmissionEventLog.class);
					
					contribution.getSubmissionPaymentItem().setRequestedPaymentEffectiveDate(null);
					contribution.getSubmissionPaymentItem().setPaymentInstructions(null);
				}		 
			}
			else {		
				contribution.getSubmissionPaymentItem().setRequestedPaymentEffectiveDate(null);
				contribution.getSubmissionPaymentItem().setPaymentInstructions(null);
			}
		}
		else {													
			contribution.getSubmissionPaymentItem().setRequestedPaymentEffectiveDate(paymentDate);
			contribution.getSubmissionPaymentItem().setPaymentInstructions(retrievePaymentInstructions(theForm, userProfile, request));
		}
	}
	
	@RequestMapping(value ="/editContribution/", params= {"task=re-submit"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   	public String doReSubmit(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws SystemException {
		return doSubmit(actionForm, bindingResult, request, response);
		
	}
	@RequestMapping(value ="/editContribution/", params= {"task=submit"}, method =  {RequestMethod.POST}) 
   	public String doSubmit(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("editContribution");//if input forward not //available, provided default
	       }
		}	
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSubmit");
		}

		UserProfile userProfile = getUserProfile(request);
		
		ContributionDetailsReportData theReport = actionForm.getTheReport();
		
		final StringBuffer message = new StringBuffer("[Online Submission]");
		message.append(" User clicked Submit.");
		message.append(" Contract = ").append(userProfile.getCurrentContract().getContractNumber());
		message.append(" Submission = ").append(actionForm.getSubNo());
		message.append(" Profile = ").append(userProfile.getPrincipal().getProfileId());
		
		logger.error(message.toString());
		
		// TODO break down this method - should be smaller
		// first save any changes
		String forward = doSaveForSubmit( actionForm, request, response);

		// an error was found in the save, so don't continue
		// redisplay the edit page with the error
		if (forward.equals(forwards.get(REFRESH))) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}

		boolean ignoreWarnings = actionForm.isIgnoreDataCheckWarnings();

		actionForm.clear( request);

		int submissionId = Integer.valueOf(actionForm.getSubNo()).intValue();

		int contractNumber = userProfile.getCurrentContract().getContractNumber();


		ReportCriteria criteria = new ReportCriteria(ContributionDetailsReportData.REPORT_ID);

		criteria.addFilter(
                ContributionTemplateReportData.FILTER_FIELD_1,
				new Integer(contractNumber)
		);

		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_2,
				new Integer(submissionId)
		);

		// get the existing complete contribution detail item
		ContributionDetailsReportData bean = null;
		ReportServiceDelegate service = null;
		try{
			service = ReportServiceDelegate.getInstance();
			bean = (ContributionDetailsReportData) service.getReportData(criteria);
			request.setAttribute(Constants.REPORT_BEAN, bean);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit contribution item");
		}

		ContributionDetailItem contribution = bean.getContributionData();

		// invoke the data checker to check the max error level SP4.2.29.2
		int errorLevel = 0;
		// invoke the datachecker RMI service synchronously
		ICon2DataProblem dataProblem = null;
		try {
			dataProblem = DataCheckerServiceDelegate.getInstance().checkData(
					Integer.toString(contribution.getTransmissionId()),
					Integer.toString(contractNumber), SUBMISSION_TYPE_CONTRIBUTION);
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
			actionForm.setShowConfirmDialog(false);
			return forwards.get("editContribution");
		}
		else if (errorLevel > 0 && !ignoreWarnings) {
			actionForm.setShowConfirmDialog(true);
			return forwards.get("editContribution");
		}

		// get the contribution detail item after the datachecker has run
		try{
			if (service == null) {
				service = ReportServiceDelegate.getInstance();
			}
			bean = (ContributionDetailsReportData) service.getReportData(criteria);
			request.setAttribute(Constants.REPORT_BEAN, bean);
		} catch (ReportServiceException e) {
			throw new SystemException(e, this.getClass().getName(),
					"doSubmit", "exception in getting the saved edit contribution item");
		}

		// error level < error tolerance forward to the confirmation page
		String userId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String userName = userProfile.getPrincipal().getFirstName() + " " + userProfile.getPrincipal().getLastName();
		String clientId = userProfile.getClientId();

		if (contribution.getSubmissionPaymentItem() == null)
			contribution.setSubmissionPaymentItem(new SubmissionPaymentItem());



		// populate a bean for display on the confirmation page
		EditContributionDetailBean confirmBean = new EditContributionDetailBean();

		//now try payment instructions
		confirmBean.setInstructions(contribution.getSubmissionPaymentItem().getPaymentInstructions());

		confirmBean.setConfirmationNumber(contribution.getSubmissionId().toString());
		// note the format of the sender name is LN, FN for the confirmation, but FN LN on the database
		confirmBean.setSender(userProfile.getPrincipal().getLastName() + ", " + userProfile.getPrincipal().getFirstName());
		confirmBean.setReceivedDate(contribution.getSubmissionDate());
		confirmBean.setPayrollDate(contribution.getPayrollDate());


		if (contribution.getGenerateStatementsIndicator() != null) {
			if (contribution.getGenerateStatementsIndicator().booleanValue())
				confirmBean.setGenerateStatementOption(STATEMENTS_REQUESTED);
			else
				confirmBean.setGenerateStatementOption(STATEMENTS_NOT_REQUESTED);
		}

		confirmBean.setRequestedEffectiveDate(contribution.getSubmissionPaymentItem().getRequestedPaymentEffectiveDate());
		confirmBean.setNumberOfParticipants(String.valueOf(contribution.getNumberOfParticipants()));
		confirmBean.setSubmissionType(MoneySourceDescription.getViewDescription(contribution.getMoneySourceID()));
		confirmBean.setTotalAllocations(
				NumberRender.formatByType(
						contribution.getEmployeeContributionTotal().add(contribution.getEmployerContributionTotal()),
						ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false)
		);
		confirmBean.setTotalContributions(
			NumberRender.formatByType(
					contribution.getContributionTotal(),
					ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false)
		);
		confirmBean.setTotalLoanRepayments(
				NumberRender.formatByType(
						contribution.getLoanRepaymentTotal(),
						ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false)
		);

		// set the money type totals for the contribution
		Collection moneyTotals = new ArrayList();
		Iterator moneyTypes = contribution.getAllocationMoneyTypes().iterator();
		while (moneyTypes.hasNext()) {
			MoneyTypeHeader moneyType = (MoneyTypeHeader) moneyTypes.next();
			String key = moneyType.getKey();
			String displayId = moneyType.getMoneyType().getContractShortName();
			BigDecimal typeTotal = (java.math.BigDecimal) contribution.getAllocationTotalValues().get(moneyType.getKey());
			// only show money types that have amounts
			if (!(typeTotal.compareTo(BIG_ZERO) == 0)) {
				String displayTotal = NumberRender.formatByType(typeTotal,ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false);
				moneyTotals.add(new LabelValueBean(displayTotal,displayId));
			}
		}

		confirmBean.setContributionsByMoneyType(moneyTotals);

		if (!actionForm.isNoPermission()
				&& userProfile.getContractProfile().getIfileConfig().isDirectDebitAccountPresent()
				&& actionForm.isDisplayMoreSections()) {
			confirmBean.setDisplayDebitFootnote(true);
		}
		//quick fix to put the statements on the page
		ContractServiceDelegate contractService = ContractServiceDelegate.getInstance();

		Contract contract = userProfile.getCurrentContract();

		ContractStatementInfoVO statementInfoVO
			= contractService.getContractStatementInfo(contract.getContractNumber());

		if (statementInfoVO.isStatementsOutstanding()) {
			List statementDates = contractService.getStatementDates(contract.getContractNumber());
			for (int i = 0; i < statementDates.size(); i++) {
				StatementPairVO statementPair = (StatementPairVO)statementDates.get(i);
				// if the start date of the statment period is the dummy date, set the start date to
				// contract effective date
				if (statementPair.getStartDate().equals(highDate)) {
					statementPair.setStartDate(contract.getEffectiveDate());
				}
			}

			statementInfoVO.setStatementDates(statementDates);
			confirmBean.setStatementDates(statementInfoVO.getStatementDates());
		}

		SessionHelper.setEditContributionDetails(request, confirmBean);

		request.getSession(false).setAttribute(Constants.REPORT_BEAN, bean);


		Date startTime = new Date();

		// call the cleanupContributionDetails method to delete unneeded data (zero amounts)
		String userType = isTPARole(userProfile)? GFTUploadDetail.USER_TYPE_TPA : GFTUploadDetail.USER_TYPE_CLIENT;
		if (userProfile.isInternalUser()) {
			userType = USER_TYPE_INTERNAL;
		}
		SubmissionServiceDelegate submissionService = SubmissionServiceDelegate.getInstance();
		submissionService.cleanupContributionDetails(new Integer(submissionId), new Integer(contractNumber), userId, userType);

		// invoke the datachecker RMI service synchronously to create the rollup rows, set the status and backup the data
		try {
			DataCheckerServiceDelegate.getInstance().backupSubmission(Integer.toString(contribution.getTransmissionId()),
				Integer.toString(contractNumber), SUBMISSION_TYPE_CONTRIBUTION, dataProblem, userId, userName,
				clientId, contribution.getSubmissionPaymentItem().getRequestedPaymentEffectiveDate(),
				userProfile.isInternalUser());
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


		if((contribution.getSubmissionPaymentItem().getPaymentInstructions() == null
					|| contribution.getSubmissionPaymentItem().getPaymentInstructions().length == 0)
				&& !userProfile.isInternalUser()
				&& (contribution.getMoneySourceID().equals("REG")
					||contribution.getMoneySourceID().equals("XTFR")))
		{
			request.getSession(false).setAttribute("displayWarning",new Boolean(true));
		}
		forward =forwards.get("confirm");

		// check lock and then release it
		Lock lock = SubmissionServiceDelegate.getInstance().checkLock(contribution,true);
		if (lock != null) {
			contribution.setLock(lock);
			boolean unlocked = LockManager.getInstance(request.getSession(false)).release(contribution);
			if (!unlocked) {
				logger.debug("problem unlocking");
			}
		} else {
			logger.debug("lock is null!");
		}

		//resetToken(request);

		// reset the effective date to ensure any subsequent
		// request starts with the default date
		actionForm.setRequestEffectiveDate(null);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSubmit");
		}

		// forward to the jsp
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/editContribution/", params= {"task=setReportPageSize"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   	public String doSetReportPageSize(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws  SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("editContribution");//if input forward not //available, provided default
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
	@RequestMapping(value ="/editContribution/", params= {"task=confirm"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
   	public String doConfirm(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws SystemException {
		validate(actionForm ,  request);
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("editContribution");//if input forward not //available, provided default
	       }
		}	
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doConfirm");
		}


		String forward = forwards.get( CONFIRMATION_PAGE);

		UserProfile userProfile = getUserProfile(request);

		// let's check the permissions
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			forward = forwards.get( TOOLS);
		}

		// let's check whether the confirm object and report bean are in the session
		if (null == request.getSession(false).getAttribute(Constants.EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA)
				|| null == request.getSession(false).getAttribute(Constants.REPORT_BEAN)
				|| !(request.getSession(false).getAttribute(Constants.REPORT_BEAN) instanceof ContributionDetailsReportData)) {

			forward = forwards.get(HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}


	protected BaseReportForm resetForm(BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		try {
			// we'll do our own custom reset
			// we need to save the tracking number
			
			reportForm.reset( request);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"resetForm", "exception in resetting the form");
		}

		return reportForm;
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
		return ContributionDetailsReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return ContributionDetailsReportData.REPORT_NAME;
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

		EditContributionDetailsForm sessionForm
				= (EditContributionDetailsForm)request.getSession(false).getAttribute("editContributionDetailsForm");
		String temp = request.getParameter("pageNumber");
		if (temp != null && temp.length()!=0) sessionForm.setPageNumber(Integer.parseInt(temp));

		temp = request.getParameter("sortField");
		if (temp != null && temp.length()!=0) sessionForm.setSortField(temp);

		temp = request.getParameter("sortDirection");
		if (temp != null && temp.length()!=0) sessionForm.setSortDirection(temp);

		// get the user profile object
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EditContributionDetailsForm contributionDetailsForm = (EditContributionDetailsForm) form;
		contributionDetailsForm.clear( request);

		// framework seems to lose the page number on refresh, so I store it myself for later use
		if (sessionForm != null) {
			contributionDetailsForm.setMyOwnPageNumber(sessionForm.getPageNumber());
		}

		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_1,
				new Integer(currentContract.getContractNumber())
		);

		String subNo = request.getParameter(SUBNO);
		// see if we're doing the default get where the submission number is from the request
		if (subNo != null && subNo.length() != 0) {
			contributionDetailsForm.setShowConfirmDialog(false);
		}
		// we expect the submission number to be present in the request.
		// if not, get it from the session form
		if (subNo == null || subNo.length() == 0)
			subNo = (String)contributionDetailsForm.getSubNo();

		// if not, get it from the session
		if (subNo == null || subNo.length() == 0) {
			subNo = (String)request.getSession(false).getAttribute(SUBNO);
		}

		CopiedSubmissionHistoryItem copiedItem = (CopiedSubmissionHistoryItem) request.getSession(false).getAttribute(Constants.COPIED_ITEM_KEY);

		if (copiedItem != null) {
			subNo = String.valueOf(copiedItem.getNewSubmissionId());
			request.getSession(false).removeAttribute(Constants.COPIED_ITEM_KEY);
		}

		if (subNo == null || subNo.length() == 0)
			subNo = contributionDetailsForm.getSubNo();

		// the first time or if we change submissions, don't show the confirmation dialog
		if (contributionDetailsForm.getSubNo() == null || !contributionDetailsForm.getSubNo().equals(subNo)) {
			contributionDetailsForm.setShowConfirmDialog(false);
		}
		contributionDetailsForm.setSubNo(subNo);
		criteria.addFilter(
				ContributionDetailsReportData.FILTER_FIELD_2,
				new Integer(contributionDetailsForm.getSubNo())
		);

		contributionDetailsForm.setViewMode(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}
	
	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
			long startTime = new GregorianCalendar().getTime().getTime();

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		EditContributionDetailsForm form = (EditContributionDetailsForm) reportForm;
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
		CopiedSubmissionHistoryItem copiedItem = (CopiedSubmissionHistoryItem) request.getSession(false).getAttribute(Constants.COPIED_ITEM_KEY);

		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get(TOOLS);
		}

		String contractStatus = userProfile.getCurrentContract().getStatus();
		if(userProfile.isSubmissionAccess() &&
		        userProfile.isAllowedUploadSubmissions() &&
                !userProfile.isBeforeCAStatusAccessOnly() &&
				!Contract.STATUS_CONTRACT_DISCONTINUED.equals(contractStatus)
		) {
			form.setNoPermission(false);
		} else {
			form.setNoPermission(true);
			form.setTheReport(new ContributionDetailsReportData(new ReportCriteria(""), 0));
			return forwards.get("input");
		}

		String subNo = request.getParameter(SUBNO);
		// we expect the submission number to be present in the request.
		// if not, get it from the session form
		if (subNo == null || subNo.length() == 0)
			subNo = (String)form.getSubNo();

		// if not, get it from the session
		if (subNo == null || subNo.length() == 0)
			subNo = (String)request.getSession(false).getAttribute(SUBNO);

		// if not, get it from the copied item
		if (copiedItem != null)
			subNo = String.valueOf(copiedItem.getNewSubmissionId());

		// due to some wierd bookmarking this may acually not be found, so send user to submission history page
		if (null == subNo) {
			return forwards.get(HISTORY);
		}

		boolean isParticipantCountTooBig = SubmissionServiceDelegate.getInstance().isParticipantCountTooBigForEdit(Integer.parseInt(subNo), userProfile.getCurrentContract().getContractNumber());
        if (isParticipantCountTooBig) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.CONTRIBUTION_ABOVE_SIZE_LIMIT,
					new String[] { COPY_SIZE_LIMIT }));

            setErrorsInSession(request, errors);
            return forwards.get(CANCEL);
        }

		String forward = super.doCommon(reportForm, request, response);
		ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
        ContributionDetailItem theItem = null;

        if (theReport == null) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.SUBMISSION_HAS_NO_VALID_DATA));
            setErrorsInSession(request, errors);
            return forwards.get(HISTORY);
        } else {
            theItem = theReport.getContributionData();
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

		if(
				!userProfile.isInternalUser() &&
				!(theItem.getSystemStatus().equals(DRAFT_STATUS) ||
						theItem.getSystemStatus().equals(COPIED_STATUS))
		) {
			form.setNoPermission(true);
			form.setTheReport(new ContributionDetailsReportData(new ReportCriteria(""),0));
			return forwards.get("input");
		}
		if (userProfile.isInternalUser() &&
				!(theItem.getSystemStatus().equals(ERROR_04_STATUS) ||
						theItem.getSystemStatus().equals(ERROR_05_STATUS) ||
						theItem.getSystemStatus().equals(ERROR_07_STATUS) ||
						theItem.getSystemStatus().equals(ERROR_09_STATUS) ||
						theItem.getSystemStatus().equals(DRAFT_STATUS) ||
						theItem.getSystemStatus().equals(COPIED_STATUS))) {
			form.setNoPermission(true);
			form.setTheReport(new ContributionDetailsReportData(new ReportCriteria(""),0));
			return forwards.get("input");
		}

		form.setTheReport(theReport);

		// lock the submission case
		boolean locked = LockManager.getInstance(request.getSession(false)).lock(theReport.getContributionData(), String.valueOf(userProfile.getPrincipal().getProfileId()));
		if (!locked) {
			// cannot obtain a lock, generate an error and return the user to the submission history page
			Collection lockError = new ArrayList(1);
			lockError.add(new ValidationError("LOCKED", ErrorCodes.SUBMISSION_CASE_LOCKED));
			SessionHelper.setErrorsInSession(request, lockError);
			return forwards.get(CANCEL);
		}

		if (
				userProfile.getContractProfile().getIfileConfig() == null ||
				form.getPaymentInfo() == null
		) {
			try {
				setupUserProfile(userProfile, form, request, true);
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

		form.storeClonedForm();

		// clear any session attributes we don't want any more
		// request.getSession().removeAttribute(CopyContributionDetailsAction.COPIED_ITEM_KEY);
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
		ContributionDetailsReportData reportData =
				(ContributionDetailsReportData) report;

		if (null == reportData) return new byte[0];

        boolean isDBContract = getUserProfile(request).getCurrentContract().isDefinedBenefitContract();        

		// Fill in the header
		Iterator columnLabels = reportData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
            String heading = (String)columnLabels.next();
            if (!isDBContract) { // normal case
                buffer.append(heading);
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
            } else {        
                if (heading.startsWith("SSN")) {
                    // skip
                } else if (isDBContract && heading.startsWith("Participant Name")) {
                    buffer.append("Name");
                    if (columnLabels.hasNext()) {
                        buffer.append(COMMA);
                    }
                } else {
                    buffer.append(heading);
                    if (columnLabels.hasNext()) {
                        buffer.append(COMMA);
                    }                                   
                }
            }
		}

		String formattedDate = reportData.getFormattedDate();
		request.setAttribute(Constants.FORMATTED_DATE, formattedDate);
		Integer trackingNumber = reportData.getSubmissionId();

		StringBuffer emptyContributionBuffer = null;

		Iterator items = report.getDetails().iterator();
		while (items.hasNext()) {
			buffer.append(LINE_BREAK);
			buffer.append(reportData.getTransactionNumber()).append(COMMA);
			buffer.append(reportData.getContractNumber()).append(COMMA);

			SubmissionParticipant participant = (SubmissionParticipant) items.next();
            if (isDBContract == false) {
				buffer.append(participant.getIdentifier()).append(COMMA);
            }            
			buffer.append(QUOTE).append(participant.getName()).append(QUOTE).append(COMMA);

			buffer.append(formattedDate);

			boolean moneyTypeExists = false;
			Map contributions = participant.getMoneyTypeAmounts();
			for (Iterator itr = reportData.getContributionData().getAllocationMoneyTypes().iterator();  itr.hasNext(); ){
				MoneyTypeHeader moneyType = (MoneyTypeHeader)itr.next();
				moneyTypeExists = false;
				// include only money types that are valid for the contract
				for (Iterator cmtitr = reportData.getContributionData().getContractMoneyTypes().iterator();
						cmtitr.hasNext() && !moneyTypeExists; ) {
					String contractMoneyTypeId = ((MoneyTypeVO)cmtitr.next()).getId().trim();
					if (moneyType.getMoneyTypeId().trim().equals(contractMoneyTypeId)) {
						moneyTypeExists = true;
					}
				}
				if (moneyTypeExists) {
					buffer.append(COMMA);

					String moneyTypeKey = moneyType.getKey();
					BigDecimal contributionAmount = (BigDecimal)contributions.get(moneyTypeKey);
					if (null != contributionAmount && (contributionAmount.compareTo(BIG_ZERO) > 0
							|| contributionAmount.compareTo(BIG_ZERO) < 0)) {
						String displayAmount = NumberRender.formatByPattern(contributionAmount,
								ZERO_AMOUNT_STRING, "#########.##;-#########.##", 2, BigDecimal.ROUND_UNNECESSARY);
						buffer.append(displayAmount);
					}
				}
			}

			// Loans (commas pre-pended)
			Iterator loans = participant.getLoanAmounts().keySet().iterator();
			Map loanMap = participant.getLoanAmounts();
			while (loans.hasNext()) {
				String loanKey = (String)loans.next();
				int endIndex = loanKey.indexOf("/");
				Integer loanId = new Integer(loanKey.substring(0, endIndex));
				String formattedLoanId = "";
				if (loanId.compareTo(ZERO) != 0 && loanId.compareTo(NINETY_NINE) != 0) {
					synchronized (FORMATTER) {
						formattedLoanId = FORMATTER.format(loanId.longValue());
					}
				}
				BigDecimal repaymentAmount = (BigDecimal)loanMap.get(loanKey);
				String displayAmount = "";
				if (null != repaymentAmount && repaymentAmount.compareTo(BIG_ZERO) > 0) {
					displayAmount = NumberRender.formatByPattern(repaymentAmount,
						ZERO_AMOUNT_STRING, "#########.##;-#########.##", 2, BigDecimal.ROUND_UNNECESSARY);
				}
				buffer.append(COMMA).append(formattedLoanId);
				buffer.append(COMMA).append(displayAmount);
			}

			// Fill-in the rest of the columns until max no. of loans per contract
			int actualLoanCount = participant.getLoanAmounts().size();
			if (actualLoanCount < reportData.getMaxLoanCount()) {
				for (int i = 0; i < (reportData.getMaxLoanCount() - actualLoanCount); i++) {
					buffer.append(COMMA).append(COMMA);
				}
			}
		}

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
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		//getting formatted date from request scope and appending as part of file name.
		//to fix the common log : 102700 
		Object attribute = request.getAttribute(Constants.FORMATTED_DATE);
		String formattedDate = attribute!=null? attribute.toString():"";
		//end common log fix
		return "Contribution_Template_for_" + String.valueOf(contractId)
				+ "_for_" + formattedDate + CSV_EXTENSION;
	}

	/**
	 * Adds payment data to the userProfile
	 * @param userProfile
	 * @param form
	 * @param request
	 * @param updateForm
	 * @throws SystemException
	 * @throws UnableToAccessIFileException
	 */
	protected void setupUserProfile(UserProfile userProfile, EditContributionDetailsForm form, 
			HttpServletRequest request, boolean updateForm)
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

		setupAccounts(userProfile, ifileConfig, form, request, updateForm);

		//determine if we have upload history for this account
		String contractId = Integer.toString(userProfile.getCurrentContract()
				.getContractNumber());

		//set it up in userProfile
		userProfile.getContractProfile().setIfileConfig(ifileConfig);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- setupUserProfile");
		}
	}

	/**
	 * Sets up the accounts and payments in the ifileConfig, optionally resetting the form
	 * values based on whether updateForm is true (first time through)
	 * @param userProfile
	 * @param ifileConfig
	 * @param form
	 * @param request
	 * @param updateForm
	 * @throws SystemException
	 */
	protected void setupAccounts(UserProfile userProfile, IFileConfig ifileConfig, 
			EditContributionDetailsForm form, HttpServletRequest request, boolean updateForm)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> setupAccounts");
		}

		EditContributionDetailsForm theForm = (EditContributionDetailsForm) form;

		ArrayList accounts = new ArrayList();
		ArrayList paymentAccounts = new ArrayList();

		//first reset old values
		ifileConfig.setCashAccountPresent(false);
		ifileConfig.setDirectDebitAccountPresent(false);
		ifileConfig.setPaymentAccounts(paymentAccounts);
		ifileConfig.setAccounts(accounts);

		ContractServiceDelegate service = ContractServiceDelegate.getInstance();

		ContractPaymentInfoVO paymentInfoVO
				= service.getContractPaymentInfo(userProfile.getCurrentContract().getContractNumber());
		//FOR TESTING ONLY
		//	ContractPaymentInfoVO paymentInfoVO = new ContractPaymentInfoVO();
		//	paymentInfoVO.setCashAccountAvailableBalance(new BigDecimal(100d));
		//	paymentInfoVO.setCashAccountTotalBalance(new BigDecimal(200d));
		//	paymentInfoVO.setOutstandingBillPayment(new BigDecimal(22d));
		//	paymentInfoVO.setOutstandingTemporaryCredit(new BigDecimal(320d));

		if (updateForm) {
			theForm.setPaymentInfo(paymentInfoVO);
		}
		
		//check permissions
		if (!(userProfile.isAllowedDirectDebit() || userProfile
				.isAllowedCashAccount()))
			return;

		// retrieve direct deposit accounts
		if (userProfile.isAllowedDirectDebit()) {
		    /*
		     * Setup direct debit accounts.
		     */
		    boolean hasDirectDebitAccounts = false;
		    List directDebitAccounts = userProfile.getRole().getDirectDebitAccounts();
		    for (Iterator it = directDebitAccounts.iterator(); it.hasNext();) {
		        DirectDebitAccount account = (DirectDebitAccount)it.next();
		        /*
		         * Only active accounts are considered.
		         */
		        if (account.isActive()) {
		            hasDirectDebitAccounts = true;
		            paymentAccounts.add(account);
		        }
		    }

			ifileConfig.setDirectDebitAccountPresent(hasDirectDebitAccounts);
		}


		// add a CashAccount if it is neccessary
		if (userProfile.isAllowedCashAccount()) {
			paymentAccounts.add(new CashAccount(Environment.getInstance().isNY()));
		}

		ArrayList instructionsAdded = new ArrayList();
		// map it
		for (int i = 0; i < paymentAccounts.size(); i++) {
			PaymentAccount paccount = (PaymentAccount) paymentAccounts.get(i);

			String instructionNumber = null;
			if (paccount instanceof DirectDebitAccount)
				instructionNumber = ((DirectDebitAccount) paccount).getInstructionNumber().trim();
			else
				instructionNumber = "0";


			PaymentAccountBean acc = new PaymentAccountBean(
							SubmissionUploadForm.PAYMENT_INSTRUCTION_ACCOUNT_PREFIX	+ String.valueOf(i),
							FormatUtils.formatAccountName(paccount, MAX_WIDTH), paccount.getAccountType(),
							ZERO_AMOUNT_STRING
			);
			accounts.add(acc);

			if (paccount instanceof CashAccount) {
				ifileConfig.setCashAccountPresent(true);
			}

			if (updateForm) {
				theForm.setAmounts(i,ZERO_AMOUNT_STRING);
				theForm.setBillAmounts(i,ZERO_AMOUNT_STRING);
				theForm.setCreditAmounts(i,ZERO_AMOUNT_STRING);
			}
			// add the actual payments made
			SubmissionPaymentItem payment = form.getTheReport().getContributionData().getSubmissionPaymentItem();
			if (payment != null) {
				PaymentInstruction[] instructions = payment.getPaymentInstructions();
				for (int j = 0; j < instructions.length; j++) {
					PaymentInstruction instruct = instructions[j];
					PaymentAccount account = instructions[j].getPaymentAccount();

					// for nonzero values we'll add it to the map
					if (!instruct.getAmount().getAmount().equals(new BigDecimal(0d))) {
						if (
								account instanceof DirectDebitAccount &&
								paccount instanceof DirectDebitAccount &&
								((DirectDebitAccount) account).getInstructionNumber().equals(((DirectDebitAccount) paccount).getInstructionNumber())
								||
								account instanceof CashAccount &&
								paccount instanceof CashAccount
							) {
							instructionsAdded.add(new Integer(j));
							if (updateForm) {
								if (instruct.getPurposeCode().equals(PaymentInstruction.CONTRIBUTION))
									theForm.setAmounts(i,NumberRender.formatByType(instruct.getAmount().getAmount(),ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false));
								else if (instruct.getPurposeCode().equals(PaymentInstruction.BILL_PAYMENT))
									theForm.setBillAmounts(i,NumberRender.formatByType(instruct.getAmount().getAmount(),ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false));
								else if (instruct.getPurposeCode().equals(PaymentInstruction.TEMPORARY_CREDIT))
									theForm.setCreditAmounts(i,NumberRender.formatByType(instruct.getAmount().getAmount(),ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false));
							}	
						}
					}
				}
			}
		}

		// determine which values in the original were dropped
		// add the actual payments made
		SubmissionPaymentItem payment = form.getTheReport().getContributionData().getSubmissionPaymentItem();
		if (payment != null) {
			boolean addWarning = false;
			PaymentInstruction[] instructions = payment.getPaymentInstructions();
			for (int j = 0; j < instructions.length; j++) {
				if (!instructionsAdded.contains(new Integer(j))) {
					// need to tell the user an instruction wasn't accounted for
					StringBuffer buff = new StringBuffer();
					if (
							instructions[j].getPurposeCode().equals(PaymentInstruction.CONTRIBUTION)
					)
						addWarning = true;
				}
			}
			if(addWarning && !userProfile.isInternalUser()) {
				theForm.setWarningMessage(theForm.getWarningMessage() + ACCOUNT_REMOVED_WARNING);
				incrementNumberOfCopyWarnings(request);
			}
		}


		//set it in user profile
		ifileConfig.setPaymentAccounts(paymentAccounts);
		ifileConfig.setAccounts(accounts);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- setupAccounts");
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

	protected void populateForm(UserProfile userProfile, EditContributionDetailsForm form, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateForm");
		}
		// TODO break down this method - should be smaller
		SimpleDateFormat slashSDF = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat fullSDF = new SimpleDateFormat("MMMMMMMMM dd, yyyy");

		ContributionDetailsReportData theReport = form.getTheReport();
		ContributionDetailItem theItem = theReport.getContributionData();

		Contract contract = userProfile.getCurrentContract();

		if(userProfile.isInternalUser()) {
			if (
					theReport == null ||
					theReport.getContributionData() == null ||
					theReport.getContributionData().getSubmissionPaymentItem() == null ||
					form.getPaymentDetails() == null ||
					theReport.getContributionData().getSystemStatus().equals(COPIED_STATUS)
			) {
				// don't show the payment section - nothing to show
				form.setDisplayPaymentInstructionSection(false);
			} else {
				form.setDisplayPaymentInstructionSection(true);
			}
		} else {
			form.setDisplayPaymentInstructionSection(
					!contract.getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED)
                    && !userProfile.isBeforeCAStatusAccessOnly()
					&& (
						userProfile.isAllowedDirectDebit()
						|| userProfile.isAllowedCashAccount()
					)
			);
		}

		form.setCashAccountPresent(userProfile.getContractProfile()
				.getIfileConfig().isCashAccountPresent());
		form.setAccounts(userProfile.getContractProfile().getIfileConfig()
				.getAccounts());

		Date marketClose = null;
		try {
			marketClose = AccountServiceDelegate.getInstance().getNextNYSEClosureDateIgnoringEmergencyClosure(null);
		} catch (Exception e2) {
			SystemException se = new SystemException(e2, this.getClass().getName(),
					"populateForm", "AccountException occurred while getting the NYSE close datetime.");
			throw se;
		}

		// set the NYSE close message
		Date currentDate = Calendar.getInstance().getTime();
		form.setMarketClose("");
		if (marketClose != null && marketClose.after(currentDate)) {
			long msDiff = marketClose.getTime() - currentDate.getTime();
			// less than 30 min to market close (30 * 60 * 1000)
			if ( msDiff < PsProperties.getNYStockClosureTimeLimit() * 60 * 1000) {
				SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
				form.setMarketClose("NYSE will close at "
							+ sdf.format(marketClose)
							+ " ET. All submissions must be submitted prior to that time."
						);
			}
		}


		GregorianCalendar calMarketClose = new GregorianCalendar();
		calMarketClose.setTime(marketClose);
		calMarketClose.set(Calendar.HOUR_OF_DAY,0);
		calMarketClose.set(Calendar.MINUTE,0);
		calMarketClose.set(Calendar.SECOND,0);
		calMarketClose.set(Calendar.MILLISECOND,0);

		if (
				form.getRequestEffectiveDate() == null ||
				form.getRequestEffectiveDate().length() == 0
		) {
			form.setRequestEffectiveDate(slashSDF.format(calMarketClose.getTime()));
		}

		if (theItem != null && theItem.getSubmissionPaymentItem() != null) {

			form.setRequestEffectiveDate(slashSDF.format(theItem.getSubmissionPaymentItem().getRequestedPaymentEffectiveDate()));
		}
		form.setDefaultEffectiveDate(calMarketClose.getTime());

		// set the calendar start date and end date range
		form.setCalendarStartDate(fullSDF.format(calMarketClose.getTime()));
		GregorianCalendar calEndDate = new GregorianCalendar();
		calEndDate.setTime(marketClose);
		calEndDate.add(Calendar.DATE, DELTA_END_DAYS - 1);
		form.setCalendarEndDate(fullSDF.format(calEndDate.getTime()));


		Date [] dates = new Date[DELTA_END_DAYS];

		for(int i = 0; i < DELTA_END_DAYS; i++) {
			dates[i] = calMarketClose.getTime();
			calMarketClose.add(Calendar.DATE, 1);
		}

		// filter out the dates that are not NYSE valid market trading days
		try {
			dates = AccountServiceDelegate.getInstance().getFilteredNYSEClosureDatesIgnoringEmergencyClosure(null,dates);
			form.setAllowedPaymentDates(dates);
		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"populateForm", "AccountException occurred while getting the NYSE filtered dates.");
			throw se;
		}

		// Get the statement info for the contract
		ContractServiceDelegate contractService = ContractServiceDelegate.getInstance();

		ContractStatementInfoVO statementInfoVO
				= contractService.getContractStatementInfo(contract.getContractNumber());

		if (statementInfoVO.isStatementsOutstanding()) {
			List statementDates = contractService.getStatementDates(contract.getContractNumber());
			// select a maximum of four statements for display
			int maximumStatementCount = 4;
			if (statementDates.size() < maximumStatementCount) {
				maximumStatementCount = statementDates.size();
			}
			List revisedStatementDates = new ArrayList();
			for (int i = 0; i < maximumStatementCount; i++) {
				StatementPairVO statementPair = (StatementPairVO)statementDates.get(i);
				// if the start date of the statment period is the dummy date, set the start date to
				// contract effective date
				if (statementPair.getStartDate().equals(highDate)) {
					statementPair.setStartDate(contract.getEffectiveDate());
				}
				revisedStatementDates.add(statementPair);
			}
			statementInfoVO.setStatementDates(revisedStatementDates);
			form.setDisplayGenerateStatementSection(true);
			form.setStatementDates(statementInfoVO.getStatementDates());
		}

		// get the allowed payroll dates for the payroll calendar
		form.setAllowedPayrollDates(
				getValidPayrollDates(
						marketClose,
						statementInfoVO,
						contract
					)
		);
		// set the payroll calendar start date and end date range
		form.setPayrollCalendarStartDate(fullSDF.format(
				form.getAllowedPayrollDates()[0]
		));
		form.setPayrollCalendarEndDate(fullSDF.format(
				form.getAllowedPayrollDates()[form.getAllowedPayrollDates().length - 1]
		));
		// set the current payroll date
		if (null != theItem.getPayrollDate()) {
			//form.setPayrollEffectiveDate(slashSDF.format(marketClose.getTime()));
			form.setPayrollEffectiveDate(slashSDF.format(theItem.getPayrollDate().getTime()));
		} else {
			form.setPayrollEffectiveDate("");
		}

		ContractPaymentInfoVO paymentInfoVO	= form.getPaymentInfo();

		// Decide whether or not to show the bill payment and temporary credit section
		IFileConfig iFileConfig = userProfile.getContractProfile().getIfileConfig();

		form.setDisplayBillPaymentSection(false);
		form.setDisplayTemporaryCreditSection(false);

		if(iFileConfig != null && iFileConfig.getAccounts() != null && iFileConfig.getAccounts().size() != 0) {
			if(paymentInfoVO != null && paymentInfoVO.getOutstandingBillPayment().doubleValue() > 0d)
				form.setDisplayBillPaymentSection(true);

			if(paymentInfoVO != null && paymentInfoVO.getOutstandingTemporaryCredit().doubleValue() > 0d)
				form.setDisplayTemporaryCreditSection(true);
		}

		// add warnings if credit or bill amounts exist but will not be shown, and then zero the amounts
		if (
				!userProfile.isInternalUser() &&
				!form.isDisplayBillPaymentSection() &&
				form.getBillAmountsMap() != null &&
				form.getBillAmountsMap().size() != 0

		) {
			BigDecimal billTotal = BIG_ZERO;
			for (Iterator billIterator = form.getBillAmountsMap().iterator(); billIterator.hasNext(); ) {
				billTotal = billTotal.add(getBigDecimal((String)billIterator.next()));
			}
			if (billTotal.compareTo(BIG_ZERO) > 0 ) {
				form.setWarningMessage(form.getWarningMessage() + BILL_REMOVED_WARNING);
				incrementNumberOfCopyWarnings(request);
				form.setBillAmountsMap(null);
			}
		}

		if (
				!userProfile.isInternalUser() &&
				!form.isDisplayTemporaryCreditSection() &&
				form.getCreditAmountsMap() != null &&
				form.getCreditAmountsMap().size() != 0
		) {
			BigDecimal creditTotal = BIG_ZERO;
			for (Iterator creditIterator = form.getCreditAmountsMap().iterator(); creditIterator.hasNext(); ) {
				creditTotal = creditTotal.add(getBigDecimal((String)creditIterator.next()));
			}
			if (creditTotal.compareTo(BIG_ZERO) > 0 ) {
				form.setWarningMessage(form.getWarningMessage() + CREDIT_REMOVED_WARNING);
				// increment the number of warnings
				incrementNumberOfCopyWarnings(request);
				form.setCreditAmountsMap(null);
			}
		}

		// setup the generate statement flag
		Boolean stmtIndicator = theItem.getGenerateStatementsIndicator();
		if (stmtIndicator != null) {
			if(stmtIndicator.booleanValue())
				form.setLastPayroll("C");
			else
				form.setLastPayroll("S");
		} else
			form.setLastPayroll("");

		// loans display requirements SP4.1.1.2
		if (
				!contract.isLoanFeature() &&
				(contract.getLoansTotalAmount() == null ||
						contract.getLoansTotalAmount().doubleValue() <= 0d) &&
				theItem.getLoanRepaymentTotal().compareTo(new BigDecimal(0d)) <= 0
		) form.setShowLoans(false);


		// we need to show at least one column if the showLoans is true
		if (theItem.getMaximumNumberOfLoans() == 0 && form.isShowLoans()) {
			theItem.setMaximumNumberOfLoans(1);
			theItem.getLoanTotalValues().add(new BigDecimal(0d));
		}


		// add a new money type column if one isn't present
		if (theItem.getAllocationMoneyTypes().size() == 0) {
			MoneyTypeVO moneyType = new MoneyTypeVO();
			// if there's only one money type use it
			if (theItem.getContractMoneyTypes().size() == 1) {
				moneyType.setContractShortName(((MoneyTypeVO)theItem.getContractMoneyTypes().get(0)).getContractShortName().trim());
				moneyType.setContractLongName(((MoneyTypeVO)theItem.getContractMoneyTypes().get(0)).getContractLongName().trim());
				String moneyTypeId = ((MoneyTypeVO)theItem.getContractMoneyTypes().get(0)).getId();
				theItem.getAllocationMoneyTypes().add(new MoneyTypeHeader(moneyTypeId, "0", moneyType));
			} else {
				// otherwise use the dummy
				moneyType.setContractShortName(DEFAULT_MONEY_DD_LABEL);
				moneyType.setContractLongName("");
				String moneyTypeId = DEFAULT_MONEY_DD_ID;
				theItem.getAllocationMoneyTypes().add(new MoneyTypeHeader(moneyTypeId, "0", moneyType));
			}
		}

		// fill in the columns for contributions and loans
		List deleteBoxes = new ArrayList();
		Map employerIds = new HashMap();
		Map recordNumbers = new HashMap();
		Iterator participants = theReport.getDetails().iterator();
		int row = 0;
		while (participants.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant) participants.next();
			deleteBoxes.add(new Integer(row),false);
			employerIds.put(new Integer(row),participant.getIdentifier());
			recordNumbers.put(new Integer(row), new Integer(participant.getRecordNumber()));
			// setup all of the contribution money type amounts
			Iterator moneyTypes = theItem.getAllocationMoneyTypes().iterator();
			int contColumn = 0;
			while (moneyTypes.hasNext()) {
				MoneyTypeHeader moneyType = (MoneyTypeHeader) moneyTypes.next();
				BigDecimal amount = (java.math.BigDecimal) participant.getMoneyTypeAmounts().get(moneyType.getKey());
				EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) form.getContributionColumns(contColumn);
				rowVal.setRow(row,NumberRender.formatByPattern(amount,ZERO_AMOUNT_STRING, NUMBER_FORMAT_PATTERN));
				contColumn++;
			}

			// setup all of the loans
			Iterator loans = participant.getLoanAmounts().entrySet().iterator();
			for (int i = 0; i < theItem.getMaximumNumberOfLoans(); i++) {

				while (loans.hasNext()) {
					EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) form.getLoanColumn(i);
					String rowValueString = null;
					String keyString = null;
					Map.Entry loanEntry = (Map.Entry) loans.next();
					keyString = (String) loanEntry.getKey();
					keyString = keyString.substring(0,keyString.indexOf("/"));
					BigDecimal amount = (BigDecimal) loanEntry.getValue();
					rowValueString = NumberRender.formatByType(amount,ZERO_AMOUNT_STRING, CURRENCY_FORMAT_CODE, false);
					rowVal.setRow(row,rowValueString);
					rowVal.setRowId(row,keyString);
					i++;
				}
				if (i < theItem.getMaximumNumberOfLoans())	{
					EditContributionDetailsForm.RowVal rowVal = (EditContributionDetailsForm.RowVal) form.getLoanColumn(i);
					rowVal.setRow(row,ZERO_AMOUNT_STRING);
					rowVal.setRowId(row,ID_99);
				}
			}
			row++;
		}
		form.setDeleteBoxesMap(deleteBoxes);
		form.setEmployerIdsMap(employerIds);
		form.setRecordNumbersMap(recordNumbers);

		// add the valid money souces for this contract to the form
		// note: we override the description in theItem.getContractMoneySources()
		//       as we use a different value for edit pages than view pages
		boolean moneySourceFound = false;
		form.setContractMoneySources(new ArrayList());
		if (null != theItem.getContractMoneySources().get(MoneySourceDescription.REGULAR_CODE)) {
			form.getContractMoneySources().add(new LabelValueBean(
					(String) MoneySourceDescription.getEditDescription(MoneySourceDescription.REGULAR_CODE),
					MoneySourceDescription.REGULAR_CODE));
			if (MoneySourceDescription.REGULAR_CODE.equals(theItem.getMoneySourceID())) {
				moneySourceFound = true;
			}
		}
		if (null != theItem.getContractMoneySources().get(MoneySourceDescription.EXTERNAL_TRANSFER_CODE)) {
			form.getContractMoneySources().add(new LabelValueBean(
					(String) MoneySourceDescription.getEditDescription(MoneySourceDescription.EXTERNAL_TRANSFER_CODE),
					MoneySourceDescription.EXTERNAL_TRANSFER_CODE));
			if (MoneySourceDescription.EXTERNAL_TRANSFER_CODE.equals(theItem.getMoneySourceID())) {
				moneySourceFound = true;
			}
		}
		// add the actual money source for this submission if it wasn't added above
		if (!moneySourceFound) {
			form.getContractMoneySources().add(new LabelValueBean(
					(String) MoneySourceDescription.getEditDescription(theItem.getMoneySourceID()),
					theItem.getMoneySourceID()));
		}

		form.setMoneySourceID(theItem.getMoneySourceID());

		// add the contract money types from the contributionDetailItem
		// to the form allowing for the select money type option at the top
		ArrayList formContractMoneyTypes = new ArrayList();
		if (theItem.getContractMoneyTypes().size() > 1) {	// only add a default money type if there's something to select from
			formContractMoneyTypes.add(new LabelValueBean(DEFAULT_MONEY_DD_LABEL,DEFAULT_MONEY_DD_ID));
		}
		Iterator contractMoneyTypes = theItem.getContractMoneyTypes().iterator();
		while (contractMoneyTypes.hasNext()) {
			MoneyTypeVO moneyType = (MoneyTypeVO)contractMoneyTypes.next();
			formContractMoneyTypes.add(new LabelValueBean(moneyType.getContractShortName().trim(),moneyType.getId().trim()));
		}
		form.setContractMoneyTypes(formContractMoneyTypes);

		// add the money type headers for each of the contribution columns
		List columns = form.getContributionColumnsMap();
		
		for(int i=0;i<columns.size();i++){
			MoneyTypeHeader moneyType = (MoneyTypeHeader) theItem.getAllocationMoneyTypes().get(i);
			form.getMoneyTypeColumnsMap().add(i,moneyType.getMoneyType().getId());
		}

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
	
	private static Date [] getValidPayrollDates (
			Date defaultInvestmentDate,
			ContractStatementInfoVO statementInfoVO,
			Contract currentContractVO) throws SystemException
	{
		Date [] outDates = null;

		Calendar calEndDate = new GregorianCalendar();
		calEndDate.setTime(defaultInvestmentDate);
		calEndDate.add(Calendar.DATE, DELTA_END_DAYS - 1);

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

	private PaymentInstruction[] retrievePaymentInstructions(EditContributionDetailsForm form, 
			UserProfile userProfile, HttpServletRequest request)
			throws SystemException, UnableToAccessIFileException {

		PaymentInstruction[] result = null;

		if (userProfile.isAllowedCashAccount() || userProfile.isAllowedDirectDebit()) {
			result = new PaymentInstruction[0];
			if ( userProfile.getContractProfile().getIfileConfig() == null ) {
				// we don't expect this to happen, but if it does, log the fact, and set up the user info
				// again, but don't update the form attributes since we want to retain what the user entered
				logger.error("EditContributionDetailsAction.retireivePaymentInstructions() - ifileConfig is null in UserProfile - will reinitialize it");
				setupUserProfile(userProfile, form, request, false);
			}
			List paymentAccounts = userProfile.getContractProfile().getIfileConfig().getPaymentAccounts();
			if (paymentAccounts != null && paymentAccounts.size() > 0) {
				List instructions = new ArrayList();

				for (int i=0; i<paymentAccounts.size(); i++) {
					BigDecimal contributionDoubleValue = getBigDecimal(form.getAmounts(i));
					BigDecimal billDoubleValue = getBigDecimal(form.getBillAmounts(i));
					BigDecimal creditDoubleValue = getBigDecimal(form.getCreditAmounts(i));
					if (!contributionDoubleValue.equals(BIG_ZERO)) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(contributionDoubleValue),PaymentInstruction.CONTRIBUTION));
					}
					if (!billDoubleValue.equals(BIG_ZERO)) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(billDoubleValue),PaymentInstruction.BILL_PAYMENT));
					}
					if (!creditDoubleValue.equals(BIG_ZERO)) {
						instructions.add(new PaymentInstruction((PaymentAccount)paymentAccounts.get(i), new MoneyBean(creditDoubleValue),PaymentInstruction.TEMPORARY_CREDIT));
					}
				}
				if (!instructions.isEmpty()) {
					result = new PaymentInstruction[instructions.size()];
					instructions.toArray(result);
				}
			}
		}
		return result;
	}

	private static void populatePaymentTotals(SubmissionPaymentItem paymentItem) {
		if (paymentItem == null) return;

		PaymentInstruction [] instructions = paymentItem.getPaymentInstructions();

		BigDecimal paymentTotal = new BigDecimal("0");
		BigDecimal totalContributionPayment = new BigDecimal("0");
		BigDecimal totalBillPayment = new BigDecimal("0");
		BigDecimal totalCreditPayment = new BigDecimal("0");

		if (paymentItem.getPaymentInstructions() != null) {
			for (int i = 0; i < instructions.length; i++) {
				if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.CONTRIBUTION))
					totalContributionPayment = totalContributionPayment.add(instructions[i].getAmount().getAmount());
				else if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.BILL_PAYMENT))
					totalBillPayment = totalBillPayment.add(instructions[i].getAmount().getAmount());
				else if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.TEMPORARY_CREDIT))
					totalCreditPayment = totalCreditPayment.add(instructions[i].getAmount().getAmount());
				paymentTotal = paymentTotal.add(instructions[i].getAmount().getAmount());
			}
		}

		paymentItem.setPaymentTotal(paymentTotal);
		paymentItem.setTotalContributionPayment(totalContributionPayment);
		paymentItem.setTotalBillPayment(totalBillPayment);
		paymentItem.setTotalCreditPayment(totalCreditPayment);
	}

	private static boolean populateContributionTotals(ContributionDetailItem contributionItem) {
		if (contributionItem == null) return false;
		Iterator participants = contributionItem.getSubmissionParticipants().iterator();

		BigDecimal contributionTotal = new BigDecimal("0");
		BigDecimal withdrawalTotal = new BigDecimal("0");
		BigDecimal loanRepaymentTotal = new BigDecimal("0");

		while (participants.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant) participants.next();
			if (participant.isMarkedForDelete())
				continue;

			if (participant.getLoanAmounts() != null) {
				Iterator loanAmounts = participant.getLoanAmounts().keySet().iterator();
				while (loanAmounts.hasNext()) {
					String loanKey = (String) loanAmounts.next();
					loanRepaymentTotal = loanRepaymentTotal.add((BigDecimal) participant.getLoanAmounts().get(loanKey));
				}
			}

			if(participant.getMoneyTypeAmounts() != null) {
				Iterator allocationAmounts = participant.getMoneyTypeAmounts().keySet().iterator();
				while (allocationAmounts.hasNext()) {
					String moneyKey = (String) allocationAmounts.next();
					BigDecimal allocationAmount = (BigDecimal) participant.getMoneyTypeAmounts().get(moneyKey);
					if(allocationAmount.compareTo(new BigDecimal(0d)) > 0)
						contributionTotal = contributionTotal.add(allocationAmount);
					else
						withdrawalTotal = withdrawalTotal.add(allocationAmount);
				}
			}
		}

		contributionItem.setContributionTotal(contributionTotal);
		contributionItem.setWithdrawlTotal(withdrawalTotal);
		contributionItem.setLoanRepaymentTotal(loanRepaymentTotal);

		if (contributionTotal.compareTo(ONE_BILLION) >=	0
				|| withdrawalTotal.compareTo(ONE_BILLION) >= 0
				|| loanRepaymentTotal.compareTo(ONE_BILLION) >= 0) {
			return true;
		}

		return false;
}

	private static SubmissionParticipant getParticipant(String employerId,
			Integer recordNumber, ContributionDetailItem contributionItem) {
		if (employerId == null || recordNumber == null || contributionItem == null) return null;

		SubmissionParticipant returnedParticipant = null;
		Iterator participants = contributionItem.getSubmissionParticipants().iterator();

		while (participants.hasNext()) {
			SubmissionParticipant participant = (SubmissionParticipant) participants.next();
			if (participant.getIdentifier().equals(employerId)
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
			System.out.println("EditContributionDetails.getBigDecimal threw NumberFormatException trying to parse ["
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
		return task;
	}
	
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
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

	protected void logEvent(EditContributionDetailBean confirmBean, UserProfile userProfile, Date startTime,
			Date endTime, String methodName) {


		String logData = prepareLogData(confirmBean, userProfile, startTime, endTime);
		AbstractSubmitController.logEvent(userProfile.getPrincipal(), this.getClass().getName(), methodName, logData,
				OnlineSubmissionEventLog.class);
	}

	protected void logEvent(EditContributionDetailsForm theForm, UserProfile userProfile, Date startTime,
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

	protected String prepareLogData(EditContributionDetailBean confirmBean, UserProfile userProfile, Date startTime,
			Date endTime) {

		StringBuffer logData = new StringBuffer()
		.append("Confirmation number: ")
		.append(confirmBean.getConfirmationNumber())
		.append(" Contract number: ")
		.append(userProfile.getCurrentContract().getContractNumber())
		.append(" Username : ")
		.append(confirmBean.getSender())
		.append( " Payroll date: ")
		.append(confirmBean.getPayrollDate())
		.append(" Total contribution amount: ")
		.append(confirmBean.getTotalContributions());
		if (!confirmBean.getPaymentInstructionsTotal().equals("0.00")) {
			logData.append(" Payment effective date: ")
			.append(confirmBean.getRequestedEffectiveDate())
			.append(" Payment amount: ")
			.append(confirmBean.getPaymentInstructionsTotal());
		}
		if (null != confirmBean.getGenerateStatementOption() && !confirmBean.getGenerateStatementOption().equals("")) {
			logData.append(" ");
			logData.append(confirmBean.getGenerateStatementOption());
		}
		logData.append(" Submission start time : ")
		.append(startTime.toString())
		.append(" Submission end time: ")
		.append(endTime.toString());

		return logData.toString();
	}

	protected String prepareLogData(EditContributionDetailsForm theForm, UserProfile userProfile, Date startTime,
			Date endTime) throws SystemException {

		Date payrollDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			payrollDate = sdf.parse(theForm.getPayrollEffectiveDate());
		} catch (ParseException e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"prepareLogData", "Exception occurred while parsing the payroll effective date.");
			throw se;
		}

		StringBuffer logData = new StringBuffer()
		.append("Confirmation number: ")
		.append(theForm.getSubNo())
		.append(" Contract number: ")
		.append(userProfile.getCurrentContract().getContractNumber())
		.append(" Username : ")
		.append(userProfile.getPrincipal().getLastName())
		.append(", ")
		.append(userProfile.getPrincipal().getFirstName())
		.append( " Payroll date: ")
		.append(payrollDate);
		if (theForm.getLastPayroll() != null && theForm.getLastPayroll().length() != 0) {
			if (theForm.getLastPayroll().equals("C")) {
				logData.append(" Participant statements requested");
			} else if (theForm.getLastPayroll().equals("S")) {
				logData.append(" Participant statements not requested");
			}
		}
		logData.append(" Action start time : ")
		.append(startTime.toString())
		.append(" Action end time: ")
		.append(endTime.toString());

		return logData.toString();
	}

	@RequestMapping(value ="/editContribution/",  method =  {RequestMethod.GET}) 
   	public String doDefualt(@Valid @ModelAttribute("editContributionDetailsForm") EditContributionDetailsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("editContribution");//if input forward not //available, provided default
	       }
		}
		String forward=super.doDefault( actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
	/**
     * Get the submit action name. The returned name will be processed to be ready to call the invokeMethod().
     */
    protected String getSubmitAction(HttpServletRequest request, ActionForm form) throws ServletException {
        String submitAction = super.getSubmitAction(request, form);
        EditContributionDetailsForm  actionform = (EditContributionDetailsForm) form;
        if(StringUtils.equals(submitAction, "Save") && StringUtils.equals(actionform.getForwardFromSave(),"setReportPageSize")){
        	return null;
        }
        return submitAction;
    }
}