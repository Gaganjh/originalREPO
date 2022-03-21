package com.manulife.pension.ps.web.noticemanager;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * 
 * Changes History to display the historical detail of custom document edited
 * @author Tamilarasu k
 *
 */
@Controller
@RequestMapping( value ="/noticemanager")
@SessionAttributes({"contractnotifychangehistory"})

public class ContractChangeHistoryNoticeManagerController extends ReportController {
	@ModelAttribute("contractnotifychangehistory") 
	public ContractChangeHistoryNoticeManagerForm populateForm()
	{
		return new ContractChangeHistoryNoticeManagerForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/noticemanager/contractNoticeManagerChangeHistory.jsp");
		forwards.put("default","/noticemanager/contractNoticeManagerChangeHistory.jsp");
		forwards.put("back","redirect:/do/noticemanager/uploadandsharepages/");
		}

	
	private static final String LOGGED = "CONTRACT_NOTICE";
	private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private FastDateFormat fastDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy", Locale.US);
	private FastDateFormat searchDateFormat = ContractDateHelper.getDateFormatterLocale("MM/dd/yyyy hh:mm:ss", Locale.US);

	@RequestMapping(value ="/contractnoticechangehistory/" ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		String forward = super.doDefault(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
	/**
	 * This is the method executed to forward back to the previous page
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contractnoticechangehistory/",params={"task=back"}, method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
					
		Map params = request.getParameterMap();
		params.remove(TASK_KEY);
		postExecute(form, request, response);
		return forwards.get("back");

	}

	/**
	 * This is the method executed to search based on the user credentials
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value ="/contractnoticechangehistory/", params={"task=search"} , method =  {RequestMethod.POST}) 
	public String doSearch (@Valid @ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }

		//ContractChangeHistoryNoticeManagerForm histform=(ContractChangeHistoryNoticeManagerForm)reportForm;
		Collection<GenericException> errors=doValidate(request,form);
		if (!errors.isEmpty()) {

			setErrorsInRequest(request, errors);
			request.setAttribute("displayDates", "true");   
			return forwards.get("input");
		}

		String forward = doCommon(form,request,response);
		postExecute(form, request, response);
		return forward;

	}

	/**
	 * This is the method executed to reset the informations back to default values 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/contractnoticechangehistory/",params={"task=reset"},method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		Collection<GenericException> errors = super.doValidate( form, request);
		
		form.setUserId(null);
		form.setActionType(null);
		form.setDocumentName(null);
		form.setUserName(null);

		if(form.getDefaultFromDate()!=null && form.getDefaultToDate()!=null)
		{
			form.setFromDate(form.getDefaultFromDate());
			form.setToDate(form.getDefaultToDate());
		}else{		
			form.setFromDate("");
		}
		doCommon(form,request,response);
		postExecute(form, request, response);
		return forwards.get("default");

	}

	/**
	 * This is the method returns the list of users JSON object
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/contractnoticechangehistory/", params={"task=fetchUsers"},method =  {RequestMethod.POST}) 
	public String doFetchUsers (@Valid @ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		Map params = request.getParameterMap();
		params.remove(TASK_KEY);
		
		form.setTask(null);
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		Timestamp fromDate = null;
		Timestamp toDate = null;
		if(request.getParameter("toDate") != null ){
			Date toDateSelected;
			try {
				toDateSelected = (Date) searchDateFormat.parse(request.getParameter("toDate") + " 23:59:59");
				Calendar toCal = Calendar.getInstance();
				toDate = new Timestamp(toCal.getTimeInMillis());
				if(toDateSelected != null){
					toDate = new Timestamp(toDateSelected.getTime());
				}
			} catch (ParseException e) {
				logger.error(" Exception occured while executing the doFetchUsers method where trying to parsing the ToDate" + toDate);
			}
		}
		if(request.getParameter("fromDate") != null ){
			Date toDateSelected;
			try {
				toDateSelected = (Date) searchDateFormat.parse(request.getParameter("fromDate") + " 23:59:59");
				Calendar toCal = Calendar.getInstance();
				fromDate = new Timestamp(toCal.getTimeInMillis());
				if(toDateSelected != null){
					fromDate = new Timestamp(toDateSelected.getTime());
				}
			} catch (ParseException e) {
				logger.error(" Exception occured while executing the doFetchUsers method where trying to parsing the fromDate " + fromDate );
			}
		}
		LinkedHashMap<BigDecimal,String> noticeChangedUserDetails = PlanNoticeDocumentServiceDelegate.getInstance().getContractNoticeUpdatedUserDetails(contractId, fromDate, toDate);
		try {
			response.getWriter().write(noticeChangedUserDetails.toString());
		} catch (IOException e) {
			logger.error(" Exception occured while executing the doFetchUsers method where trying to write noticeChangedUserDetails into response object ");
		}
		postExecute(form, request, response);
		return null;
	}
	
	 @RequestMapping(value ="/contractnoticechangehistory/" , params={"task=page"}, method =  {RequestMethod.GET}) 
		public String doPage (@ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			String forward = super.doPage(form, request, response);
			postExecute(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

		}
	 
	 @RequestMapping(value ="/contractnoticechangehistory/" , params={"task=sort"}, method =  {RequestMethod.GET}) 
		public String doSort (@ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
			String forward = super.doSort(form, request, response);
			postExecute(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

		}
	 @RequestMapping(value ="/contractnoticechangehistory/" , params={"task=print"}, method =  {RequestMethod.GET}) 
		public String doPrint (@ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
			String forward = super.doPrint(form, request, response);
			postExecute(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

		}
	 @RequestMapping(value ="/contractnoticechangehistory/" , params={"task=download"}, method =  {RequestMethod.GET}) 
		public String doDownload (@ModelAttribute("contractnotifychangehistory") ContractChangeHistoryNoticeManagerForm form,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
			String forward = super.doDownload(form, request, response);
			postExecute(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

		}
	/**
	 * Retrieve the change history detail based on the default filter inputs
	 */
	 
	public String doCommon(BaseReportForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		try {
			if(NoticeManagerUtility.validateContractRestriction(userProfile.getCurrentContract()) 
				|| NoticeManagerUtility.validateProductRestriction(userProfile.getCurrentContract())
				 || NoticeManagerUtility.validateDIStatus(userProfile.getCurrentContract(), userProfile.getRole())){
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		} catch (ContractDoesNotExistException e) {
			throw new SystemException(e ,"Exception while retrieving the contract details ");
		}
		Collection<GenericException> errors = super.doValidate(actionForm, request);
		populateDefaultDates(request, errors, actionForm);
		super.doCommon(actionForm,request,response);
		PlanDocumentHistoryReportData report = (PlanDocumentHistoryReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
		ContractChangeHistoryNoticeManagerForm histform =(ContractChangeHistoryNoticeManagerForm)actionForm;
		histform.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId())); 
		histform.setPlanNoticeDocumentChangeTypes(report.getPlanNoticeDocumentChangeTypes());
		histform.setUserProfileDetails(report.getUserProfileDetails());
		return forwards.get("default");

	}

	/**This is the method executed to validate all the user credentials
	 * @param request
	 * @param errors
	 * @param histform	 
	 */

	@SuppressWarnings("unchecked")
	public Collection doValidate(HttpServletRequest request,ContractChangeHistoryNoticeManagerForm form) throws SystemException {
		ContractChangeHistoryNoticeManagerForm histform=(ContractChangeHistoryNoticeManagerForm)form;
		Collection errors = super.doValidate( histform, request);
		
		try {
			Date fromDate = null;
			if (StringUtils.isNotBlank(histform.getFromDate())) {

				fromDate = fastDateFormat.parse(histform.getFromDate());
				if (! fastDateFormat.format(fromDate).equals(histform.getFromDate())) {
					throw new ParseException("Invalid Date", 0);

				}

			} else {
				// from date is blank so add error
				errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY_HISTORY));
			}

			Date toDate = null;
			if (StringUtils.isNotBlank(histform.getToDate())) {
				toDate = fastDateFormat.parse(histform.getToDate());
				if ( ! fastDateFormat.format(toDate).equals(histform.getToDate())) {
					throw new ParseException("Invalid Date", 0);
				}

			}else {
				// to date is blank so add error
				errors.add(new GenericException(ErrorCodes.TO_DATE_EMPTY_HISTORY));
			}

			if (fromDate != null) {

				

				if (toDate != null) {
					if (fromDate.after(toDate)) {
						// from date is > to date, so add error
						errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO_HISTORY));
					}

				}		
			}
		} catch(ParseException pe) {
			// From date or To date is invalid
			errors.add(new GenericException(ErrorCodes.INVALID_DATE_HISTORY));
		}
		
		return errors;


	}


	/**
	 * This is the method executed to populate the default from date and to date 
	 * if date isn't provided by the user. 
	 * @param request
	 * @param errors
	 * @param actionForm
	 * @throws SystemException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateDefaultDates(HttpServletRequest request,
			Collection errors, BaseReportForm actionForm) throws SystemException {
		ContractChangeHistoryNoticeManagerForm histform=(ContractChangeHistoryNoticeManagerForm)actionForm;



		if(StringUtils.isBlank(histform.getFromDate())){
			Date fromDate = null;



			Date toDate = null;

			try {
				toDate = ContractDateHelper.determineChangeHistoryToDate();
				histform.setDefaultToDate(fastDateFormat.format(toDate));

			} catch (SystemException e) {
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException("Exception occured while calculating Dates");
			} 			

			try {


				fromDate = ContractDateHelper.determineChangeHistoryFromDate();

				histform.setDefaultFromDate(fastDateFormat.format(fromDate));

			} catch (SystemException e) {
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException("Exception occured while calculating Dates");
			}		


			histform.setToDate(fastDateFormat.format(toDate));
			histform.setFromDate(fastDateFormat.format(fromDate));
		}
	}


	/**
	 * Get the report Id
	 */
	@Override
	protected String getReportId() {
		return PlanDocumentHistoryReportData.REPORT_ID;	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return PlanDocumentHistoryReportData.REPORT_NAME;
	}

	/**
	 * Get  the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		UserProfile userProfile = getUserProfile(request);

		Integer contract=userProfile.getCurrentContract().getContractNumber();

		Timestamp currentTimestamp =  new Timestamp(System.currentTimeMillis());
		return "["+contract+"]"+" "+getReportName()+" "+currentTimestamp + CSV_EXTENSION;
	}
	/**
	 * Method to get the Content Message for the provided content Id.
	 * 
	 * @param int contentId
	 * @return String content text
	 * @throws SystemException
	 */
	private static String getMessage(int contetentId) throws SystemException {
		String text = null;
		try {

			Miscellaneous message = (Miscellaneous) (ContentCacheManager.getInstance()).getContentById(
					contetentId, ContentTypeManager.instance().MISCELLANEOUS);
			if(message != null)
				text =  message.getText();

		} catch (ContentException e) {
			throw new SystemException (e.toString() +
					"ContractChangeHistoryNoticeManagerAction" + "getMessage" + "error getting error text");
		}
		return text;
	}
	/**
	 * Method to download the data to CSV file
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) throws SystemException {

		ContractChangeHistoryNoticeManagerForm form  = (ContractChangeHistoryNoticeManagerForm) reportForm;
		PlanDocumentHistoryReportData planDocumentHistoryReportData = (PlanDocumentHistoryReportData)report;


		StringBuffer buffer = new StringBuffer();
		buffer.append("Change History as of").append(" ").append(StringUtils.remove(form.getTodayDate(), ",")).append(LINE_BREAK);		
		buffer.append("From Date").append(COMMA).append(form.getFromDate()).append(LINE_BREAK);
		buffer.append("To Date").append(COMMA).append(form.getToDate()).append(LINE_BREAK);

		if (StringUtils.isNotBlank(
				form.getUserName())) {
			buffer.append("Username").append(COMMA).append(form.getUserName().toUpperCase()).append(LINE_BREAK);

		}else
		{
			buffer.append("Username").append(COMMA).append(" ").append(LINE_BREAK);
		}


		List<LookupDescription> documentChangeTypeCode = planDocumentHistoryReportData.getPlanNoticeDocumentChangeTypes();
		Boolean usernameErrorIndicator = false;

		if (StringUtils.isNotBlank(
				form.getActionType())) {
			for(LookupDescription lookupDescription : documentChangeTypeCode){


				if(StringUtils.equals(lookupDescription.getLookupCode().trim(), form.getActionType().trim())){

					if(form.getActionType().trim().equals("CHNG")){
						buffer.append("Action").append(COMMA).append("Changed").append(LINE_BREAK);

					}


					if(form.getActionType().trim().equals("CHRP")){
						buffer.append("Action").append(COMMA).append("Change &and Replaced").append(LINE_BREAK);

					}
					if(form.getActionType().trim().equals("DEL")){
						buffer.append("Action").append(COMMA).append("Deleted").append(LINE_BREAK);

					}
					if(form.getActionType().trim().equals("REPL")){
						buffer.append("Action").append(COMMA).append("Replaced").append(LINE_BREAK);

					}
					if(form.getActionType().trim().equals("UPLD")){
						buffer.append("Action").append(COMMA).append("Uploaded").append(LINE_BREAK);

					}
				}else
				{
					usernameErrorIndicator=true;
				}
			}
		}else{
			buffer.append("Action").append(COMMA).append("").append(LINE_BREAK);		
		}
		if (StringUtils.isNotBlank(
				form.getDocumentName())) {
			buffer.append("DocumentName").append(COMMA).append(form.getDocumentName()).append(LINE_BREAK);

		} else {
			buffer.append("DocumentName").append(COMMA).append(" ").append(LINE_BREAK);
		}
		buffer.append("Date & time").append(COMMA);
		buffer.append("User name").append(COMMA);
		buffer.append("Action").append(COMMA);
		buffer.append("Document name").append(COMMA);
		buffer.append("Changed document name").append(COMMA);
		buffer.append("PPT website option").append(COMMA);

		buffer.append(LINE_BREAK);


		List<PlanNoticeDocumentChangeHistoryVO> planhistoryList =null;
		PlanDocumentHistoryReportData reportData = (PlanDocumentHistoryReportData)report;

		planhistoryList = reportData.getPlanNoticeDocumentChangeHistorys();


		if(planhistoryList!=null && planhistoryList.size()>0){
			DateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy hh:mm:ss a");
			for(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistory:planhistoryList){
				buffer.append(" ").append(dateFormat.format(planNoticeDocumentChangeHistory.getChangedDate()))
				.append(COMMA);
				buffer.append(StringUtils.remove(planNoticeDocumentChangeHistory.getChangedUserName(), ","))
				.append(COMMA);
				buffer.append(planNoticeDocumentChangeHistory.getPlanNoticeDocumentChangeTypeDetail().getLookupDesc())
				.append(COMMA);
				if(StringUtils.isBlank(planNoticeDocumentChangeHistory.getReplacedfileName())){
					buffer.append(planNoticeDocumentChangeHistory.getDocumentName())
					.append(COMMA);
					buffer.append(planNoticeDocumentChangeHistory.getReplacedfileName())
					.append(COMMA);
				}else{
					
					buffer.append(planNoticeDocumentChangeHistory.getReplacedfileName())
					.append(COMMA);
					buffer.append(planNoticeDocumentChangeHistory.getDocumentName())
					.append(COMMA);
				}
				buffer.append(planNoticeDocumentChangeHistory.getChangedPPT())
				.append(COMMA);
				buffer.append(LINE_BREAK);
				// Buffered data return as byte.

			}

		}else
		{
			buffer.append(getMessage(ContentConstants.MESSAGE_NO_PENDING_TRANSACTION_HISTORY_FOR_DATE_SELECTED)).append(LINE_BREAK);
		}
		return buffer.toString().getBytes();
	}


	/**
	 * Participant id ,transaction number,contract id added in 
	 * report criteria object.
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */

	@SuppressWarnings("rawtypes")
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm,
			HttpServletRequest request)	throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		ContractChangeHistoryNoticeManagerForm contractNoticeChangeHistoryForm = 
				(ContractChangeHistoryNoticeManagerForm) actionForm;

		UserProfile userProfile = getUserProfile(request);

		Contract currentContract = userProfile.getCurrentContract();

		// add the contract number to the filter criteria, which will be
		// used to execute the SQLs
		criteria.addFilter(
				PlanDocumentHistoryReportData.FILTER_CONTRACT_NUMBER,
				currentContract.getContractNumber());

		// add the from & to date to the report criteria
		if (contractNoticeChangeHistoryForm != null ) {

			try {

				// if the from date is available in the form, then set the
				// from date to the criteria map
				if (StringUtils.isNotBlank(
						contractNoticeChangeHistoryForm.getFromDate())) {

					Date fromDate = fastDateFormat.parse(
							contractNoticeChangeHistoryForm.getFromDate());
					criteria.addFilter(
							PlanDocumentHistoryReportData.FILTER_FROM_DATE, 
							fromDate);
				}
				if (StringUtils.isNotBlank(
						contractNoticeChangeHistoryForm.getUserName())) {

					String username=contractNoticeChangeHistoryForm.getUserName().toUpperCase();

					criteria.addFilter(
							PlanDocumentHistoryReportData.FILTER_USER_NAME, 
							username);
				}

				if (StringUtils.isNotBlank(
						contractNoticeChangeHistoryForm.getDocumentName())) {

					String documentname=contractNoticeChangeHistoryForm.getDocumentName();			

					criteria.addFilter(
							PlanDocumentHistoryReportData.DOCUMENT_NAME, 
							documentname);
				}
				// if the To date is available in the form, then set the
				// from date to the criteria map
				if (StringUtils.isNotBlank(
						contractNoticeChangeHistoryForm.getToDate())) {

					Date toDate = searchDateFormat.parse(
							contractNoticeChangeHistoryForm.getToDate()+" 23:59:59");

					criteria.addFilter(
							PlanDocumentHistoryReportData.FILTER_TO_DATE,
							toDate);
				}
				String actionType = contractNoticeChangeHistoryForm.getActionType();
				if (StringUtils.isNotBlank(actionType)) {

					criteria.addFilter(
							PlanDocumentHistoryReportData.FILTER_ACTION_CHANGE,
							actionType);
				}

				Integer selectedUserId = contractNoticeChangeHistoryForm.getUserId();
				if (selectedUserId!=null && selectedUserId > 0) {

					criteria.addFilter(
							PlanDocumentHistoryReportData.FILTER_USER_PROFILE_ID,
							String.valueOf(selectedUserId));
				}

			} catch (ParseException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException("Exception occured while calculating Dates");
			}
		}

		String task = getTask(request);

		// If it is a download task then set the required filter variable
		if(DOWNLOAD_TASK.equals(task)){
			criteria.addFilter(PlanDocumentHistoryReportData.FILTER_TASK, 
					PlanDocumentHistoryReportData.TASK_DOWNLOAD);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}		
	}


	/**
	 * Gets the current task for this request.
	 *
	 * @param request
	 *            The current request object.
	 * @return The task for this request.
	 */

	protected String getTask(HttpServletRequest request) {
		String task = null;
		ContractChangeHistoryNoticeManagerForm contractNoticeChangeHistoryForm = 
				(ContractChangeHistoryNoticeManagerForm) request.getSession().getAttribute("contractnotifychangehistory");


		if (contractNoticeChangeHistoryForm != null
				&& StringUtils.isNotBlank(contractNoticeChangeHistoryForm.getTask())) {
			task = contractNoticeChangeHistoryForm.getTask() ;
		} else {
			task = DEFAULT_TASK;
		}

		return task;
	}


	/**
	 * If it's sort by Date, secondary sort is transaction number. 
	 * If it's sort by number, secondary sort is date.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(
				PlanDocumentHistoryReportData.ACTION_DATE_FIELD)) {

			// secondary sort is by date
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);

			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.DOCUMENT_NAME,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
					sortDirection);

		} else if (sortField.equals(
				PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD)) {

			// secondary sort is by date

			criteria.insertSort(
					PlanDocumentHistoryReportData.DOCUMENT_NAME,
					sortDirection);

			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
					sortDirection);

		}else if (sortField.equals(
				PlanDocumentHistoryReportData.DOCUMENT_NAME)) {

			// secondary sort is by date
			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
					sortDirection);

		}
		else if (sortField.equals(
				PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD)) {

			// secondary sort is by date
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);

			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.DOCUMENT_NAME,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
					sortDirection);
		}else if (sortField.equals(
				PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD)) {

			// secondary sort is by date
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);

			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.DOCUMENT_NAME,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
					sortDirection);
			
			criteria.insertSort(
					PlanDocumentHistoryReportData.POST_TO_PPT_FIELD,
					sortDirection);
		}else if (sortField.equals(
				PlanDocumentHistoryReportData.POST_TO_PPT_FIELD)) {

			// secondary sort is by date
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_FIRST_NAME_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.USER_LAST_NAME_FIELD,
					sortDirection);

			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_TAKEN_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.DOCUMENT_NAME,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.ACTION_DATE_FIELD,
					sortDirection);
			criteria.insertSort(
					PlanDocumentHistoryReportData.REVISED_DOCUMENT_FIELD,
					sortDirection);
			
		}
		

	}


	/**
	 * Adds the page log information
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	  protected void postExecute(ContractChangeHistoryNoticeManagerForm reportForm, HttpServletRequest request, 
				HttpServletResponse response) throws ServletException, IOException, SystemException {
		  

		  HttpSession session = request.getSession(false);
		  super.postExecute( reportForm, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId =userProfile.getCurrentContract().getContractNumber();
		  String userAction = CommonConstants.CHANGE_HISTORY_PAGE;
		  if(session.getAttribute(LOGGED)==null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			  session.setAttribute(LOGGED, "VISITED");
		  }
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
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
