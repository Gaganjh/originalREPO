package com.manulife.pension.bd.web.bob.planReview;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ContractSearchUtility;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.util.ProtectedStringBuffer;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.planReview.report.PlanReviewHistorySummaryReportData;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewCoverImageDetails;
import com.manulife.pension.service.planReview.valueobject.PlanReviewHistorySummaryReportItem;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.DateComparator;
import com.manulife.pension.util.PlanReviewConstants;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Plan Review History Summary Report Action
 * 
 * @author akarave
 * 
 */ 
@Controller
@RequestMapping( value = "/bob")
@SessionAttributes({"planReviewReportHistoryForm"})

public class PlanReviewHistorySummaryReportController extends
		BasePlanReviewHistoryReportController {
	@ModelAttribute("planReviewReportHistoryForm") 
	public PlanReviewReportHistoryForm populateForm() 
	{
		return new PlanReviewReportHistoryForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("default","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("sort","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("navigatedFromDetails","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("filter","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("reset","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("contractReviewReportStep1Page","redirect:/do/bob/planReview/");
		forwards.put("ViewPlanReviewReportFromHistory","redirect:/do/bob/planReview/Results/?task=default");
		forwards.put("historyReportDetails","redirect:/do/bob/planReview/HistoryDetails/");
		forwards.put("planReviewRequest","redirect:/do/bob/planReview/");
		forwards.put("reSubmitPlanReviewRequest","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("page","/planReview/bobHistorySummaryPlanReviewReport.jsp");
		forwards.put("viewDisable","/planReview/bobHistoryDetails.jsp");
		forwards.put("homePage","redirect:/do/home/");
		
		}
	       
	
	protected static final Logger logger = Logger.getLogger(PlanReviewHistorySummaryReportController.class);
	
	private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

	private ServiceLogRecord logRecord = new ServiceLogRecord("PlanReviewHistorySummaryReportAction");
	
	public PlanReviewHistorySummaryReportController() {
		super(PlanReviewHistorySummaryReportController.class);
	}

	private int[] DATE_COMPARISON_FIELDS = new int[] { Calendar.YEAR,
			Calendar.MONTH, Calendar.DATE };
	public static final String FORMAT_DATE_EXTRA_LONG_MDY ="MM/dd/yyyy hh:mm:ss a 'ET'";
	public static final String FORMAT_DATE_LONG_MDY = "MMddyyyy_HHmmSS";
	protected static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	protected static final String FORMAT_DATE_SHORT_YMD = "yyyy-MM-dd";
	protected static final String FORMAT_DATE_SHORT_MDY_HH_MM = "MM/dd/yyyy HH:mm"; // db
																					// uses
																					// timestamp
																					// not
																					// date
	public static final FastDateFormat LONG_MDY_FORMATTER = FastDateFormat
			.getInstance(FORMAT_DATE_LONG_MDY);
	public static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(FORMAT_DATE_SHORT_MDY);
	public static final FastDateFormat SHORT_YMD_FORMATTER = FastDateFormat
			.getInstance(FORMAT_DATE_SHORT_YMD);
	public static final FastDateFormat LONG_MDY_FORMATTER_REQUESTED_DATE = FastDateFormat
			.getInstance(FORMAT_DATE_EXTRA_LONG_MDY);

	String params= "bob";//TODO
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		//TODO
		String forward = super.preExecute( form, request,
				response);

		if (forward == null) {

			PlanReviewReportHistoryForm historyForm = (PlanReviewReportHistoryForm) form;
			boolean isContractLevel = BDConstants.PR_CONTRACT_LEVEL_PARAMETER.equalsIgnoreCase(params);

			if(StringUtils.equals(DEFAULT_TASK, getTask(request)) || StringUtils.equals(RESET_TASK, getTask(request))){
				// clear the inputs
				historyForm.setFilterPrintConfirmNumber(StringUtils.EMPTY);
				historyForm.setFilterReportMonthEndDate(StringUtils.EMPTY);
				historyForm.setFilterRequestedFromDate(StringUtils.EMPTY);
				historyForm.setFilterRequestedToDate(StringUtils.EMPTY);
				historyForm.setFilterSearchByField(StringUtils.EMPTY);
				historyForm.setFilterSearchByFieldValue(StringUtils.EMPTY);
				
				historyForm.setPrintConfirmNumber(StringUtils.EMPTY);
				historyForm.setSelectedReportMonthEndDate(StringUtils.EMPTY);
				historyForm.setRequestedFromDate(StringUtils.EMPTY);
				historyForm.setRequestedToDate(StringUtils.EMPTY);
				historyForm.setSelectedSearchByField(StringUtils.EMPTY);
				
				if (isContractLevel) {
					BobContext bobContext = getBobContext(request);
					
					if (bobContext == null
							|| bobContext.getCurrentContract() == null) {
						// navigate back to BOB main page. DONE
						return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_BOB_PAGE);
					}
					
					Contract currentContract = bobContext.getCurrentContract();

					String contractNumber = String.valueOf(currentContract
							.getContractNumber());
					
					if(StringUtils.equals(DEFAULT_TASK, getTask(request))){
					historyForm.setSelectedSearchByFieldValue(contractNumber);
					}
				} else {
					historyForm
							.setSelectedSearchByFieldValue(StringUtils.EMPTY);
				}
			}
			
			request.setAttribute(
					BDConstants.PR_CONTRACT_LEVEL_REQUEST_PARAMETER,
					Boolean.valueOf(isContractLevel));

			// executing for contract level history
			if (isContractLevel) {
				BobContext bobContext = getBobContext(request);

				if (bobContext == null
						|| bobContext.getCurrentContract() == null) {
					// navigate back to BOB main page. DONE
					return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_BOB_PAGE);
				}

				Contract currentContract = bobContext.getCurrentContract();

				String contractNumber = String.valueOf(currentContract
						.getContractNumber());

				if(!StringUtils.equalsIgnoreCase("planReviewRequest", getTask(request))){
					if (StringUtils.isNotBlank(historyForm
							.getSelectedSearchByFieldValue())
							&& !StringUtils.equals(contractNumber,
									historyForm
											.getSelectedSearchByFieldValue())) {
						
						// change the BOB Contract page.
						BobContextUtils.setUpBobContext(
								historyForm.getSelectedSearchByFieldValue(),
								request);
	
						BobContext bob = getBobContext(request);
	
						if (bob == null || bob.getCurrentContract() == null) {
							
							ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
							errorMessages
									.add(new GenericException(
											BDErrorCodes.HISTORY_CONTRACT_NOT_FOUND_IN_BOB_CONTEXT));
	
							setErrorsInRequest(request, errorMessages);
							BobContextUtils
									.setUpBobContext(contractNumber, request);
	
							forward = forwards.get("input");
						}
					}
				}
			}
		}

		return forward;
	}

	@RequestMapping(value ="/planReview/History/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		
			forward = super.doDefault( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/planReview/History/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		
			 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	
	}

	@RequestMapping(value = "/planReview/History/", params = {"task=page"}, method = {RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
			 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planReview/History/", params = {"task=sort"}, method = {RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
			 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/planReview/History/", params={"task=download"} , method =  {RequestMethod.POST}) 
	public String doDownload(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
			 forward  = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
	}

	@Override
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		PlanReviewReportHistoryForm form = (PlanReviewReportHistoryForm) reportForm;

		String forward = null;
		
		boolean isContractLevel = BDConstants.PR_CONTRACT_LEVEL_PARAMETER
				.equalsIgnoreCase(params);
		
		if (!"navigatedFromDetails".equalsIgnoreCase(getTask(request))) {
		
			String requestHistorySummaryProperty = request
					.getParameter("requestHistorySummaryReport");

			if (StringUtils.isBlank(requestHistorySummaryProperty)
					|| !BooleanUtils.isTrue(Boolean
							.valueOf(requestHistorySummaryProperty))) {

				form.setRequestedMonthendDateList(getPlanReviewHistoryPeriodEndDates());
				populateReportForm( reportForm, request);
				
				// If it is from BOB level forward to history page
				if(!isContractLevel) {
					forward = forwards.get(getTask(request));
	
					return forward;
				}
			}
		}
		
		if (StringUtils.equalsIgnoreCase(FILTER_TASK, getTask(request))) {
			
			populateReportFormFilterProperties(form);
		}
		
		if (!isContractLevel) {
			
			if (StringUtils.isBlank(form.getSelectedSearchByField())
					&& StringUtils
							.isBlank(form.getSelectedSearchByFieldValue())
					&& StringUtils
							.isBlank(form.getSelectedReportMonthEndDate())
					&& StringUtils.isBlank(form.getPrintConfirmNumber())
					&& StringUtils.isBlank(form.getRequestedFromDate())
					&& StringUtils.isBlank(form.getRequestedToDate())) {
				
				// If all the search parameters are blank, Information message "Enter search criteria to display reports" will be displayed.
				return forwards.get(getTask(request));

			}
			
			if (form.getFilterRequestedFromDate() == null) {
				return forwards.get(getTask(request));
			}
		}
		
		forward = super.doCommon( reportForm, request, response);
		PlanReviewHistorySummaryReportData reportData = (PlanReviewHistorySummaryReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		populateReportFormFromReportData(reportData, form);

		return forward;
	}

	private void populateReportFormFilterProperties(
			PlanReviewReportHistoryForm historySummaryReportForm) {

		historySummaryReportForm
				.setFilterSearchByField(historySummaryReportForm
						.getSelectedSearchByField());
		historySummaryReportForm
				.setFilterSearchByFieldValue(historySummaryReportForm
						.getSelectedSearchByFieldValue());
		historySummaryReportForm
				.setFilterReportMonthEndDate(historySummaryReportForm
						.getSelectedReportMonthEndDate());
		historySummaryReportForm
				.setFilterRequestedFromDate(historySummaryReportForm
						.getRequestedFromDate());
		historySummaryReportForm
				.setFilterRequestedToDate(historySummaryReportForm
						.getRequestedToDate());
		historySummaryReportForm
				.setFilterPrintConfirmNumber(historySummaryReportForm
						.getPrintConfirmNumber());
	}
	
	private void populateReportFormFromReportData(
			PlanReviewHistorySummaryReportData reportData,
			PlanReviewReportHistoryForm form) {
		// TODO
	}
	

	@RequestMapping(value ="/planReview/History/", params={"task=navigatedFromDetails"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doNavigatedFromDetails(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		 forward = doCommon( actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
		
	}
	@RequestMapping(value ="/planReview/History/", params={"task=navigatedFromResults"} , method =  {RequestMethod.POST}) 
	public String doNavigatedFromResults(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		 forward = doCommon( actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
	}
	
	/**
	 * 
	 * TODO - verify whether this method requires?
	 * 
	 * The doViewPlanReviewReportFromHistory method has been written to redirect
	 * to result page
	 * 
	 */
	@RequestMapping(value ="/planReview/History/", params={"task=ViewPlanReviewReportFromHistory"} , method =  {RequestMethod.POST}) 
	public String doViewPlanReviewReportFromHistory(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		String task = request.getParameter(TASK_KEY);
		
		HttpSession session = request.getSession(false);
		// form.setRequestFromHistory(Boolean.TRUE);

		session.setAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID,
				form.getSelectedPlanReviewRequestId());

		session.setAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT, true);

		setRegularPageNavigation(request);

		return forwards.get(task);
	}
	@RequestMapping(value ="/planReview/History/", params={"task=planReviewRequest"} , method =  {RequestMethod.POST}) 
	public String doPlanReviewRequest(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		HttpSession session = request.getSession(false);

		session.setAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT, Boolean.TRUE);

		setRegularPageNavigation(request);

		return forwards.get(getTask(request));
	}
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	 
	protected Collection doValidate(ActionForm form,
			HttpServletRequest request) {
		
		
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		String task = request.getParameter(TASK_KEY);

		// do not validate since the filters are going to be reset
		if (!FILTER_TASK.equalsIgnoreCase(task)) {
			return errorMessages;
		}

		errorMessages = (ArrayList<GenericException>) super.doValidate(
				form, request);
		PlanReviewReportHistoryForm reportForm = (PlanReviewReportHistoryForm) form;
		validateSearchParameters(errorMessages, reportForm, request, "bob");
		return errorMessages;

	}
	
	@Override
	protected String getDefaultSort() {
		return PlanReviewHistorySummaryReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected String getReportId() {
		return PlanReviewHistorySummaryReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return PlanReviewHistorySummaryReportData.REPORT_NAME;
	}

	@Override
	protected String getFileName(HttpServletRequest request) {
        // defaults to .csv extension
		Boolean isContractLevel = (Boolean) request
				.getAttribute(BDConstants.PR_CONTRACT_LEVEL_REQUEST_PARAMETER);
		String formattedDate = LONG_MDY_FORMATTER.format(new Date());
		if(isContractLevel){
			BobContext bobContext = getBobContext(request);
			Contract currentContract = bobContext.getCurrentContract();
			String contractNumber = String.valueOf(currentContract
					.getContractNumber());
			return getReportName()+"_"+contractNumber+"_"+formattedDate+ CSV_EXTENSION;
		}
		else{
			return getReportName()+"_"+formattedDate+ CSV_EXTENSION;
		}
    }
	
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm reportform, HttpServletRequest request)
			throws SystemException {

		PlanReviewReportHistoryForm historySummaryReportForm = (PlanReviewReportHistoryForm) reportform;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		String brokerId = getBrokerId(request);

		if (StringUtils.isNotBlank(brokerId)) {
			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_BROKER_ID,
					brokerId);
		}

		Boolean isContractLevel = false;

		if (isContractLevel != null && BooleanUtils.isFalse(isContractLevel)) {
			if (userProfile.isInternalUser()) {
				// RVP User's BOB
				if (userProfile.getRole() instanceof BDRvp
						|| BDUserRoleType.RVP.equals(userProfile.getRole()
								.getRoleType())) {

					criteria.addFilter(
							PlanReviewHistorySummaryReportData.FILTER_BOB_CONTRACTS,
							getCurrentBOBContractList(request, userProfile));
				}

			} else {
				// external user
				if (userProfile.getRole() instanceof BDFinancialRep
						|| userProfile.getRole() instanceof BDFinancialRepAssistant) {

					criteria.addFilter(
							PlanReviewHistorySummaryReportData.FILTER_BOB_CONTRACTS,
							getCurrentBOBContractList(request, userProfile));
				} else {
					// means a user other than Financial Rep Level 2 or
					// Assistant
					// trying to request
					throw new SystemException(
							"Illegal Access to Plan Review Request pages by External user: "
									+ userProfile);
				}
			}
		}

		Boolean isPlanReviewAdminUser = Boolean.FALSE;

		if (userProfile.isInternalUser()) {

			isPlanReviewAdminUser = isPlanReviewAdminUser(request);
		}

		criteria.addFilter(
				PlanReviewHistorySummaryReportData.FILTER_IS_PLAN_REVIEW_ADMIN_USER,
				isPlanReviewAdminUser);

		if (!userProfile.isInternalUser()) {

			// external user
			if (userProfile.getRole() instanceof BDFinancialRep
					|| userProfile.getRole() instanceof BDFinancialRepAssistant) {

				criteria.addFilter(
						PlanReviewHistorySummaryReportData.FILTER_IS_EXTERNAL_USER,
						Boolean.TRUE);
			} else {
				// means a user other than Financial Rep Level 2 or Assistant
				// trying to request
				throw new SystemException(
						"Illegal Access to Plan Review Request pages by External user: "
								+ userProfile);
			}

		} else {
			// internal user
			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_IS_EXTERNAL_USER,
					Boolean.FALSE);
		}
		
		if (userProfile.getRole() instanceof BDRvp) {
			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_IS_RVP_USER,
					Boolean.TRUE);
		} else {
			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_IS_RVP_USER,
					Boolean.FALSE);
		}
		
		criteria.addFilter(
				PlanReviewHistorySummaryReportData.FILTER_IS_BOB_PLAN_REVIEW_LEVEL,
				Boolean.valueOf(!isContractLevel));
		
		if (!isContractLevel){
			if (historySummaryReportForm.getFilterSearchByFieldValue() != null
					&& StringUtils.isNotBlank(historySummaryReportForm.getFilterSearchByFieldValue())) {
				if (StringUtils.equals(historySummaryReportForm.getFilterSearchByField(),
						BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NUMBER)) {
					criteria.addFilter(
							PlanReviewHistorySummaryReportData.FILTER_CONTRACT_NUMBER,
							StringUtils.trimToEmpty(historySummaryReportForm.getFilterSearchByFieldValue()));
				} else {
					criteria.addFilter(
							PlanReviewHistorySummaryReportData.FILTER_CONTRACT_NAME,
							historySummaryReportForm.getFilterSearchByFieldValue());
				}
	
			}
		}
		else {
			if (historySummaryReportForm.getFilterSearchByFieldValue() != null
					&& StringUtils.isNotBlank(historySummaryReportForm.getFilterSearchByFieldValue())) {
				criteria.addFilter(
						PlanReviewHistorySummaryReportData.FILTER_CONTRACT_NUMBER,
						StringUtils.trimToEmpty(historySummaryReportForm.getFilterSearchByFieldValue()));
			}	
		}

		Date fromDate = null;
		if (StringUtils.isNotBlank(historySummaryReportForm.getFilterRequestedFromDate())) {

			try {
				fromDate = SHORT_MDY_FORMATTER.parse(historySummaryReportForm
						.getFilterRequestedFromDate());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid Date: "
						+ historySummaryReportForm.getFilterRequestedFromDate());
			}

		} else {
			// set default from Date DONE
			fromDate = getDefaultFromDate();
		}

		criteria.addFilter(PlanReviewHistorySummaryReportData.FILTER_FROM_DATE,
				SHORT_YMD_FORMATTER.format(fromDate));

		Date todDate = null;
		if (StringUtils.isNotBlank(historySummaryReportForm.getFilterRequestedToDate())) {

			try {
				todDate = SHORT_MDY_FORMATTER.parse(historySummaryReportForm.getFilterRequestedToDate());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid Date: "
						+ historySummaryReportForm.getFilterRequestedToDate());
			}

		} else {
			// set default to Date DONE
			todDate = getDefaultToDate();
		}

		criteria.addFilter(PlanReviewHistorySummaryReportData.FILTER_TO_DATE,
				SHORT_YMD_FORMATTER.format(todDate));

		if (StringUtils.isNotEmpty(historySummaryReportForm.getFilterReportMonthEndDate())) {

			Date monthEndDate = null;
			try {
				monthEndDate = SHORT_MDY_FORMATTER.parse(historySummaryReportForm
						.getFilterReportMonthEndDate());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Invalid Date: "
						+ historySummaryReportForm.getRequestedToDate());
			}

			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_REPORT_MONTHEND_DATE,
					SHORT_YMD_FORMATTER.format(monthEndDate));
		}

		if (StringUtils.isNotEmpty(historySummaryReportForm.getFilterPrintConfirmNumber())) {
			criteria.addFilter(
					PlanReviewHistorySummaryReportData.FILTER_PRINT_CONFIRM_NUMBER,
					StringUtils.trimToEmpty(historySummaryReportForm.getFilterPrintConfirmNumber()));
		}
	}

	private String getCurrentBOBContractList(HttpServletRequest request,
			BDUserProfile userProfile) throws SystemException {

		StringBuffer sb = new StringBuffer();

		List<String> contractStatusList = new ArrayList<String>();
		contractStatusList.add("AC");

		boolean includeIAStatus = false;
		List<ContractVO> contractVOList = ContractSearchUtility
				.getBOBContractDetails(request, userProfile,
						contractStatusList, includeIAStatus);
		if (contractVOList != null && !contractVOList.isEmpty()) {
			for (ContractVO vo : contractVOList) {
				sb.append(vo.getContractNumber());
				sb.append(", ");
			}
		} else {
			return "0";
		}

		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		} else {
			return "0";
		}

		return sb.toString();
	}

	/**
	 * Method to populate Report Action form
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		PlanReviewReportHistoryForm form = (PlanReviewReportHistoryForm) reportForm;
		super.populateReportForm( form, request);

		String task = getTask(request);

		if (DEFAULT_TASK.equalsIgnoreCase(task)) {

			boolean isContractLevel = BDConstants.PR_CONTRACT_LEVEL_PARAMETER.equalsIgnoreCase(params);

			BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

			form.setExternalUserView(!userProfile.isInternalUser());

			String filterContractNumber = form.getSelectedSearchByFieldValue();

			if (isContractLevel) {

				BobContext bobContext = getBobContext(request);
				Contract currentContract = bobContext.getCurrentContract();
				String contractNumber = String.valueOf(currentContract
						.getContractNumber());
				
				form.setSelectedSearchByField(BDConstants.PR_HISTORY_SEARCH_BY_CONTRACT_NUMBER);

				if (StringUtils.isEmpty(filterContractNumber)) {
					form.setSelectedSearchByFieldValue(contractNumber);
				} else {
					form.setSelectedSearchByFieldValue(filterContractNumber);
				}
				
					form.setSelectedReportMonthEndDate(StringUtils.EMPTY);
				
				populateReportFormFilterProperties(form);
				
			} else {
				form.setSelectedSearchByFieldValue(filterContractNumber);
			}
		}
	}

	private Date getDefaultToDate() {
		Calendar cal = new GregorianCalendar();
		return cal.getTime();

	}
	
	/**
	 * Returns Recent (Max) Date string from list of LabelValueBean
	 * 
	 * @param periodEndingDates
	 * @return
	 */
	public static String getRecentPeriodEndingDate(
			List<LabelValueBean> periodEndingDates) {

		LabelValueBean maxLabelValueBean = Collections.max(
				periodEndingDates, new Comparator<LabelValueBean>() {

					@Override
					public int compare(LabelValueBean object1,
							LabelValueBean object2) {

						if (object1 == null) {
							return -1;
						} else if (object2 == null) {
							return 1;
						}

						SimpleDateFormat formatter = new SimpleDateFormat(
								"MM/dd/yyyy");

						Date date01 = null;
						Date date02 = null;
						try {

							date01 = formatter.parse(object1
									.getValue());
							date02 = formatter.parse(object2
									.getValue());
						} catch (ParseException exception) {
							throw new IllegalArgumentException(
									"Exception occured while Parsing the one of the dates: "
											+ String.valueOf(object1
													.getValue()
													.toString())
											+ " and "
											+ String.valueOf(object2
													.getValue()
													.toString()));
						}

						return date01.compareTo(date02);
					}

				});

		return maxLabelValueBean.getValue();
	}

	private Date getDefaultFromDate() {
		Calendar defaultFromDateCal = new GregorianCalendar();
		defaultFromDateCal.setTime(new java.util.Date());
		defaultFromDateCal.add(Calendar.MONTH,
				BDConstants.DEFAULT_HISTORY_FROM_DATE_LESS_IN_MONTHS);
		Date defaultFromDate = new Date(defaultFromDateCal.getTimeInMillis());
		return defaultFromDate;

	}

	/**
	 * This method is called when the user clicks on "CSV" button. This method
	 * generates the CSV file to be given back to the user.
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm form,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData().");
		}

		PlanReviewReportHistoryForm reportHistoryForm = (PlanReviewReportHistoryForm) form;

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		BDUserRole userRole = userProfile.getRole();

		PlanReviewHistorySummaryReportData reportData = (PlanReviewHistorySummaryReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		String buff = buildCsvString(userProfile, userRole, reportData,
				reportHistoryForm, request);

		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> getDownloadData().");
		}
		return buff.getBytes();
	}

	/**
	 * This method is called when user clicks on PDF Type link on Bob-History
	 * page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 *             +
	 */
	@RequestMapping(value ="/planReview/History/", params={"task=historyReportDetails"} , method =  {RequestMethod.POST}) 
	public String doHistoryReportDetails(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
        Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		HttpSession session = request.getSession(false);

		session.setAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID,
				form
						.getSelectedPlanReviewActivityId());
		session.setAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID,
				form
						.getSelectedPlanReviewRequestId());
		
		setRegularPageNavigation(request);

		return forwards.get( getTask(request));
	}

	private String buildCsvString(BDUserProfile userProfile,
			BDUserRole userRole, PlanReviewHistorySummaryReportData reportData,
			PlanReviewReportHistoryForm reportForm,
			HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadDataForTab().");
		}
		
		Boolean isContractLevel = (Boolean) request
				.getAttribute(BDConstants.PR_CONTRACT_LEVEL_REQUEST_PARAMETER);
		
		ProtectedStringBuffer buff = new ProtectedStringBuffer(255);

		// information above the report
		buff.append("Plan Review Report History").append(LINE_BREAK);
		if(isContractLevel)
		{
			buff.append("Contract Number")
			.append(",")
			.append(StringUtils.trimToEmpty(Encode.forJava(reportForm
					.getSelectedSearchByFieldValue()))).append(LINE_BREAK);			
		}else{
		buff.append("Search by").append(",")
				.append(Encode.forJava(reportForm.getSelectedSearchByField()))
				.append(LINE_BREAK);
		buff.append("Value")
				.append(",")
				.append(StringUtils.trimToEmpty(Encode.forJava(reportForm
						.getSelectedSearchByFieldValue()))).append(LINE_BREAK);
		}
		buff.append("Report Month End")
				.append(",")
				.append(StringUtils.trimToEmpty(Encode.forJava(reportForm
						.getSelectedReportMonthEndDate()))).append(LINE_BREAK);
		buff.append("Requested From")
				.append(",")
				.append(StringUtils.trimToEmpty(Encode.forJava(reportForm
						.getRequestedFromDate()))).append(LINE_BREAK);
		buff.append("Requested To")
				.append(",")
				.append(StringUtils.trimToEmpty(Encode.forJava(reportForm.getRequestedToDate())))
				.append(LINE_BREAK);
		buff.append("Print Confirmation No")
				.append(",")
				.append(StringUtils.trimToEmpty(Encode.forJava(reportForm
						.getPrintConfirmNumber()))).append(LINE_BREAK);
		buff.append(LINE_BREAK);
		buff.append(LINE_BREAK);

		buff.append("Requested Date").append(",").append("Contract Name")
				.append(",").append("Contract Number").append(",")
				.append("Month End").append(",").append("Requested By");
		if(userProfile.isInternalUser()){
				buff.append(",").append("Requested For");
		}
		// TODO
																	// Requested
																	// For
																	// changes.
				buff.append(",").append("Status").append(",").append("Output Type");
		buff.append(LINE_BREAK);

		if (isContractLevel){
			if ( StringUtils.isBlank(reportForm.getSelectedSearchByFieldValue())
					&& StringUtils.isBlank(reportForm.getSelectedReportMonthEndDate())
					&& StringUtils.isBlank(reportForm.getPrintConfirmNumber())
					&& StringUtils.isBlank(reportForm.getRequestedFromDate())
					&& StringUtils.isBlank(reportForm.getRequestedToDate())) {
				
				String[] messageColl = null;
		        try {
		        
		            messageColl = getMessagesToDisplay(request , BDContentConstants.PLAN_REVIEW_ENTER_SEARCH_CRITERIA);
		        } catch (ContentException e) {
		            throw new SystemException(e, getClass().getName(), "populateDownloadDataForTab",
		                    "Something wrong with CMA");
		        }
		        if (messageColl != null && messageColl.length > 0) {
		            for (String message : messageColl) {
		                buff.append(getCsvString(message)).append(LINE_BREAK);
		            }
		            return buff.toString();
		        }
			}
		}  
		
		if (reportData != null) {
			
			Collection<PlanReviewHistorySummaryReportItem> details = (Collection<PlanReviewHistorySummaryReportItem>) reportData
					.getDetails();

			if (details.isEmpty()) {
				
				String[] messageColl = null;
		        try {
		        
		            messageColl = getMessagesToDisplay(request , BDContentConstants.PLAN_REVIEW_HISTORY_NO_RECORDS_TO_DISPLAY_MESSAGE);
		        } catch (ContentException e) {
		            throw new SystemException(e, getClass().getName(), "populateDownloadDataForTab",
		                    "Something wrong with CMA");
		        }
		        if (messageColl != null && messageColl.length > 0) {
		            for (String message : messageColl) {
		                buff.append(getCsvString(message)).append(LINE_BREAK);
		            }
		        }
			}

			for (PlanReviewHistorySummaryReportItem historySummaryRecord : details) {
				String contractID = historySummaryRecord.getContractId()
						.toString().replaceAll("\\s+", "");
				buff.append(LONG_MDY_FORMATTER_REQUESTED_DATE.format(historySummaryRecord.getRequestedDate()))
						.append(",")
						.append(StringUtils.trimToEmpty(historySummaryRecord.getContractName()))
						.append(",")
						.append(contractID)
						.append(",")
						.append(historySummaryRecord.getMonthEndDate())
						.append(",")
						.append(StringUtils.trimToEmpty(historySummaryRecord.getRequestedByUserName()));
				if(userProfile.isInternalUser()){
					buff.append(",").append(StringUtils.trimToEmpty(historySummaryRecord.getRequestedForUserName()));
				}
				buff.append(",").append(historySummaryRecord.getStatus())
						.append(",")
						.append(historySummaryRecord.getOutputType());
				buff.append(LINE_BREAK);
			}
		} else {
			String[] messageColl = null;
	        try {
	        
	            messageColl = getMessagesToDisplay(request , BDContentConstants.PLAN_REVIEW_ENTER_SEARCH_CRITERIA);
	        } catch (ContentException e) {
	            throw new SystemException(e, getClass().getName(), "populateDownloadDataForTab",
	                    "Something wrong with CMA");
	        }
	        if (messageColl != null && messageColl.length > 0) {
	            for (String message : messageColl) {
	                buff.append(getCsvString(message)).append(LINE_BREAK);
	            }
	        }
		}

		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateDownloadDataForTab().");
		}
		return buff.toString();

	}
	
	 /**
     * This method gets a string array of messages to be displayed in CSV file.
     * 
     * @param request - the HttpServletRequest object.
     * @return - A String array of messages.
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    private String[] getMessagesToDisplay(HttpServletRequest request, int infoMsg) throws ContentException {
        
        String[] messageColl = null;
        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
        /*ArrayList<GenericException> infoMessages = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);*/
        
        GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
        		infoMsg, ContentTypeManager.instance().MISCELLANEOUS,false);
        infoMessages.add(exception);

        if (infoMessages != null) {
            messageColl = ContentHelper.getMessagesUsingContentType(infoMessages);
        }
        return messageColl;
    }

	private void validateSearchParameters(
			ArrayList<GenericException> errorsList,
			PlanReviewReportHistoryForm reportForm,
			HttpServletRequest request, String planReviewLevel) {
		
		if (!StringUtils.equals(planReviewLevel,
				BDConstants.PR_CONTRACT_LEVEL_PARAMETER)) {
			// bob level

			if (StringUtils.equals(reportForm.getSelectedSearchByField(),
					BDConstants.FILTER_CONTRACT_NUMBER_SLEECTED)
					&& StringUtils.isEmpty(reportForm
							.getSelectedSearchByFieldValue())) {
				errorsList.add(new ValidationError("contractNumber",
						BDErrorCodes.HISTORY_EMPTY_CONTRACT_NUMBER,
						ValidationError.Type.error));
			}
			
			if (StringUtils.equals(reportForm.getSelectedSearchByField(),
					BDConstants.FILTER_CONTRACT_NAME_SLEECTED)
					&& (StringUtils.isEmpty(reportForm
							.getSelectedSearchByFieldValue()) || reportForm
							.getSelectedSearchByFieldValue().length() < 3)) {

				errorsList.add(new ValidationError("contractName",
						BDErrorCodes.HISTORY_CONTRACT_NAME_LESS_CHAR,
						ValidationError.Type.error));
			}
		} else {
			// Contract level
			
			if (StringUtils.isEmpty(reportForm
							.getSelectedSearchByFieldValue())) {
				errorsList.add(new ValidationError("contractNumber",
						BDErrorCodes.HISTORY_EMPTY_CONTRACT_NUMBER,
						ValidationError.Type.error));
			}
			
		}
		
		
		if (reportForm.getRequestedFromDate() != null
				|| reportForm.getRequestedToDate() != null) {

			Date fromDate = null;
			Date toDate = null;
			
			if (StringUtils.isNotEmpty(reportForm.getRequestedFromDate())) {
				
				try {
					fromDate = SHORT_MDY_FORMATTER.parse(reportForm
							.getRequestedFromDate());
				} catch (ParseException e) {
					throw new IllegalArgumentException("Invalid Date: "
							+ reportForm.getRequestedFromDate());
				}

				if (validateDate(fromDate)) {
					errorsList.add(new ValidationError("historyDate",
							BDErrorCodes.HISTORY_DATE_RANGE_EXCEED_MESSAGE,
							ValidationError.Type.error));
				}
			} else {
				// set the fromDate as per the DFS DONE
				fromDate = getDefaultFromDate();
			}

			if (StringUtils.isNotEmpty(reportForm.getRequestedToDate())) {

				try {
					toDate = SHORT_MDY_FORMATTER.parse(reportForm
							.getRequestedToDate());
				} catch (ParseException e) {
					throw new IllegalArgumentException("Invalid Date: "
							+ reportForm.getRequestedToDate());
				}

				if (validateDate(toDate)) {
					errorsList.add(new ValidationError("historyDate",
							BDErrorCodes.HISTORY_DATE_RANGE_EXCEED_MESSAGE,
							ValidationError.Type.error));
				}
			} else {
				// set the toDate as per the DFS DONE
				toDate = getDefaultToDate();
			}

			if (DateComparator
					.compare(fromDate, toDate, DATE_COMPARISON_FIELDS) > 0) {
				errorsList.add(new ValidationError("fromDate",
						BDErrorCodes.HISTORY_FROMDATE_MORE_THAN_TODATE,
						ValidationError.Type.error));
			}
		}

	}

	private boolean validateDate(Date givenDate) {
		Calendar cutoffCal = Calendar.getInstance();
		cutoffCal.setTime(new Date());
		cutoffCal.add(Calendar.MONTH, -1
				* BDConstants.PR_HISTORY_MAX_LATEST_MONTH_END_DATES_DROP_DOWN);
		Date cutOffDate = cutoffCal.getTime();
		boolean isValid = false;
		if (DateComparator.compare(givenDate, cutOffDate,
				DATE_COMPARISON_FIELDS) < 0) {
			isValid = true;
		}
		return isValid;
	}

	/*
	 * DFS Update The valid date values displayed in the Report Month End drop
	 * down list will include up to the latest 52 Month End date values
	 * available on the Plan Review Reports Month End Validation table
	 */
	private List<LabelValueBean> getPlanReviewHistoryPeriodEndDates()
			throws SystemException {
		return PlanReviewServiceDelegate.getInstance(
				Environment.getInstance().getApplicationId())
				.getPlanReviewHistoryPeriodEndDates();
	}
	@RequestMapping(value ="/planReview/History/", params={"task=reset"} , method =  {RequestMethod.POST}) 
	public String doReset(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		
		if (logger.isDebugEnabled()) {
            logger.debug("entry -> doReset");
        }
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		 forward  = forwards.get("input");
	
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doReset");
        }

        return forward;
	}

		

	/**
	 * This method is called to Resubmit the user request contracts in the
	 * Report Table under the Request Status in complete redirected to
	 * PlanReviewResults page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/History/", params={"task=reSubmitPlanReviewRequest"} , method =  {RequestMethod.POST}) 
	public String doReSubmitPlanReviewRequest(@Valid @ModelAttribute("planReviewReportHistoryForm") PlanReviewReportHistoryForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReSubmitRequest() in PlanReviewReportHistoryAction");
		}

		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        Collection validationErrors = doValidate(form, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInRequest(request, validationErrors);
			return forwards.get("input");
		}
		PlanReviewRequestVO planReviewRequest = PlanReviewReportUtils
				.populateRequestDetails(request, RequestTypeCode.PUBLISH);
		String activityId = form
				.getSelectedPlanReviewActivityId();
		String contractId = form
				.getSelectedPlanReviewContractId();
		
		if(StringUtils.isBlank(activityId) || !StringUtils.isNumeric(activityId)) {
			// navigate back to BOB main page. DONE
			return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_BOB_PAGE);
		}
		
		// This method is used to store the Report date when user Resubmits the
		// Request.
		// clicks on GenerateReport.
		PlanReviewRequestVO planReviewRequestVo = PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.reSubmitPlanReviewRequest(planReviewRequest,
						Integer.valueOf(contractId),
						Integer.valueOf(activityId));
		
		//CR9 Changes
		//When the user clicks on the Next button the 
		//system will determine for each contract row selected on the Step 1 page, 
		//if any contract rows meet the Allowed Request Limit Condition for PDF requests,
		if (planReviewRequestVo.isContractReachedMaxLimit()) {
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new ValidationError("ALLOWED_PLAN_REVIEW_REQUEST_LIMIT",
					BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
					new Object[] { contractId }));
			setErrorsInRequest(request, errors);
			
			doCommon( form, request, response);
			return forwards.get("input");

		}
		
		List<PlanReviewReportUIHolder> requestedPlanReviewReports = getPlanReviewReportUIHolderList(
				planReviewRequestVo, request);
		request.getSession().setAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID,
				planReviewRequestVo.getRequestId());

		// MDB call to make PUBLISH webservice call
		PlanReviewReportUtils.firePlanReviewRequestEvents(planReviewRequestVo,
				requestedPlanReviewReports, request);

		logActivityData(planReviewRequestVo, request, activityId, form);
		
		// Logic for regular navigation
		setRegularPageNavigation(request);
		
		request.getSession(false).setAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID,
				form
						.getSelectedPlanReviewActivityId());

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doReSubmitRequest() in PlanReviewReportHistoryAction.");
		}
		
		forward= doCommon( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	private void logActivityData(PlanReviewRequestVO planReviewRequestVo,
			HttpServletRequest request, String originalActivityID, PlanReviewReportHistoryForm planReviewReportHistoryForm) {

		StringBuffer logData = new StringBuffer();

		logData.append(PlanReviewConstants.LOG_USER_PROFILE_ID)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(planReviewRequestVo.getUserProfileid())
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		logData.append(PlanReviewConstants.LOG_REQUESTED_DATE)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(DateRender.formatByPattern(new Date(planReviewRequestVo
						.getCreatedTS().getTime()), StringUtils.EMPTY,
						RenderConstants.LONG_TIMESTAMP_MDY_SLASHED))
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		logData.append(PlanReviewConstants.LOG_ACTION)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append("Plan Review Report Resubmission")
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		List<ActivityVo> activityList = planReviewRequestVo.getActivityVoList();

		for (ActivityVo activity : activityList) {

			logData.append(PlanReviewConstants.LOG_CONTRACT_NUMBER)
					.append(BDConstants.SINGLE_SPACE_SYMBOL)
					.append(activity.getContractId())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);

			logData.append(PlanReviewConstants.LOG_CONTRACT_NAME)
					.append(BDConstants.SINGLE_SPACE_SYMBOL)
					.append(planReviewReportHistoryForm.getSelectedPlanReviewContractName())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);

			PublishDocumentPackageVo DocPackage = (PublishDocumentPackageVo) activity
					.getDocumentPackageVo();

			logData.append(PlanReviewConstants.LOG_MONTH_END_DATE)
					.append(BDConstants.SINGLE_SPACE_SYMBOL)
					.append(DocPackage.getPeriodEndDate())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);
		}

		logData.append(PlanReviewConstants.LOG_ORIGINAL_REQUESTED_DATE)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(planReviewReportHistoryForm.getSelectedPlanReviewRequestedTS())
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		for (ActivityVo activity : activityList) {
			logData.append("Activity ID=")
					.append(BDConstants.SINGLE_SPACE_SYMBOL)
					.append(activity.getActivityId())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);
		}

		logData.append("Original ActivityID=")
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(originalActivityID)
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		PlanReviewReportUtils.logPlanReviewResubmitActivity(
				"doReSubmitPlanReviewRequest", logData.toString(), userProfile,
				logger, interactionLog, logRecord);
	}

	private List<PlanReviewReportUIHolder> getPlanReviewReportUIHolderList(
			PlanReviewRequestVO planReviewRequestVo, HttpServletRequest request)
			throws SystemException {
		List<PlanReviewReportUIHolder> uIHolderList = new ArrayList<PlanReviewReportUIHolder>();
		List<PlanReviewCoverImageDetails> coverImageDetails = PlanReviewReportUtils
				.getPlanReviewCoverPageImageList(request);
		for (ActivityVo activity : planReviewRequestVo.getActivityVoList()) {
			PlanReviewReportUIHolder uIHolder = new PlanReviewReportUIHolder();
			PublishDocumentPackageVo documentPackage = (PublishDocumentPackageVo) activity
					.getDocumentPackageVo();

			if (documentPackage.isCmaSelectedCoverPageImageInd()) {
				uIHolder.setCmaCoverPageImage(new CoverPageImage(
						getCMACoverPageImage(documentPackage
								.getHighResDefaultCmaCoverPageImageId(),
								coverImageDetails)));
			}
			uIHolder.setActivityId(activity.getActivityId());
			uIHolder.setContractNumber(activity.getContractId());
			uIHolderList.add(uIHolder);
		}
		return uIHolderList;
	}

	private PlanReviewCoverImageDetails getCMACoverPageImage(
			int defaultCmaCoverPageImageId,
			List<PlanReviewCoverImageDetails> coverImageDetails) {
		for (PlanReviewCoverImageDetails planReviewCoverImageDetails : coverImageDetails) {
			if (Integer.valueOf(
					planReviewCoverImageDetails.getHighResolutionImageCmaKey())
					.equals(defaultCmaCoverPageImageId)) {
				return planReviewCoverImageDetails;
			}
		}
		return null;
	}

	
}
