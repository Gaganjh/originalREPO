package com.manulife.pension.bd.web.bob.planReview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.bob.planReview.util.GetPlanReviewDocumentUtility;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.planReview.report.PlanReviewReportPrintHistoryDetailsReportData;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.BusinessUnit;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.Division;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentFormat;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentStatus;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.PartyIdentifier;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductSubType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductType;
import com.manulife.pension.service.planReview.util.PlanReviewRequestProperties;
import com.manulife.pension.service.planReview.valueobject.ActivityEventVo;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewHistoryDetailsReportItem;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentPackage;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.PlanReviewConstants;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventStatus;
import com.manulife.pension.util.PlanReviewConstants.ActivitySatusCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityTypeCode;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"planReviewHistoryDetailsReportForm"})

public class PlanReviewReportPrintHistoryDetailsController extends BasePlanReviewHistoryReportController {
	@ModelAttribute("planReviewHistoryDetailsReportForm") public PlanReviewHistoryDetailsReportForm populateForm() {return new PlanReviewHistoryDetailsReportForm();}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/planReview/bobHistoryDetails.jsp");
		forwards.put("default","/planReview/bobHistoryDetails.jsp" );
		forwards.put("planReviewRequest","redirect:/do/bob/planReview/");
		forwards.put("viewDisable","/planReview/bobHistoryDetails");
		forwards.put("printRequest","/planReview/bobHistoryDetails.jsp");
		forwards.put("shippingDetails","/WEB-INF/planReview/common/reportShippingDetails.jsp");
		forwards.put("showViewDisableReport","/WEB-INF/planReview/common/deletePlanReviewReportOverlay.jsp");
		forwards.put("deletePlanReviewReport","redirect:/do/bob/planReview/History/?task=navigatedFromDetails");
		forwards.put("sort","/planReview/bobHistoryDetails.jsp");
		forwards.put("contractReviewReportStep1Page","redirect:/do/bob/planReview/"); 
		forwards.put("reSubmitPlanReviewPrintRequest","/planReview/bobHistoryDetails.jsp");
		forwards.put("print","redirect:/do/bob/planReview/Print/");
		forwards.put("planReviewRequest","redirect:/do/bob/planReview/");
		forwards.put("openPdfWindow","/WEB-INF/planReview/common/openPlanReviewHistoryDetailsPDF.jsp");
		forwards.put("filter","/planReview/bobHistoryDetails");
		forwards.put("page","/planReview/bobHistoryDetails.jsp");
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("backToMainHistory","redirect:/do/bob/planReview/History/?task=navigatedFromDetails");
	}

	private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

	private ServiceLogRecord logRecord = new ServiceLogRecord("PlanReviewReportPrintHistoryDetailsAction");
		
	private static final String IS_DOWNLOAD_PDF_GENERATED = "isDownloadPdfGenerated";
	private static final String IS_DOWNLOAD_SELECTED_PDF_GENERATED = "isDownloadSelectedPdfGenerated";
	
	protected static final FastDateFormat dashedMdydateFormat = FastDateFormat
			.getInstance("MM-dd-yyyy-HH-mm");
	protected static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	protected static final FastDateFormat dashedMmmdydateFormat = FastDateFormat
			.getInstance("MMM-dd-yyyy");
	protected static final FastDateFormat dashedMmmdydateAndTimeFormat = FastDateFormat
			.getInstance("MMM-dd-yyyy-HH-mm-ss");
	
	protected static final String FORWARD_HOME_PAGE = "homePage";
	
	protected static final Logger logger = Logger.getLogger(PlanReviewReportPrintHistoryDetailsController.class);
	
	public PlanReviewReportPrintHistoryDetailsController() {
		super(PlanReviewReportPrintHistoryDetailsController.class);
	}
	
	String params= "bob";
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		String forward = super.preExecute( form, request,
				response);
		PlanReviewHistoryDetailsReportForm planReviewHistoryDetailsReportForm = (PlanReviewHistoryDetailsReportForm) form;
		if (forward == null) {
			
			String requestHistoryDetailsProperty = planReviewHistoryDetailsReportForm.getRequestHistoryDetailsReport();
			
			if (!(StringUtils.equals(DEFAULT_TASK, getTask(request)) || StringUtils
					.equals("showViewDisableReport", getTask(request)))) {

				if (StringUtils.isBlank(requestHistoryDetailsProperty)
						|| !BooleanUtils.isTrue(Boolean
								.valueOf(requestHistoryDetailsProperty))) {

					return forwards.get(FORWARD_HOME_PAGE);
				}
			}

			boolean isContractLevel = BDConstants.PR_CONTRACT_LEVEL_PARAMETER.equalsIgnoreCase(params);

			request.setAttribute(
					BDConstants.PR_CONTRACT_LEVEL_REQUEST_PARAMETER,
					Boolean.valueOf(isContractLevel));

			// executing for contract level history
			if (isContractLevel) {
				BobContext bobContext = getBobContext(request);

				if (bobContext == null
						|| bobContext.getCurrentContract() == null) {
					// navigate back to BOB main page. 
					return forwards.get(FORWARD_HOME_PAGE);
				}
			}
		}

		return forward;
	}

	@RequestMapping(value ="/planReview/HistoryDetails/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			forward = super.doDefault( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/planReview/HistoryDetails/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form,
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
		
			forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planReview/HistoryDetails/", params = {"task=page"}, method = {RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form,
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
			forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planReview/HistoryDetails/", params = {"task=sort"}, method = {RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form,
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
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forwards.get(forward) : forwards.get(forward);
	}

	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		PlanReviewHistoryDetailsReportForm historyDetailsReportForm = (PlanReviewHistoryDetailsReportForm) reportForm;
		
		HttpSession session = request.getSession(false);
		
		String planReviewActivityId = (String) session
				.getAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID);
		String planReviewRequestId = (String) session
				.getAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID);
		
		if (StringUtils.isBlank(planReviewActivityId)) {
			planReviewActivityId = historyDetailsReportForm
					.getSelectedPlanReviewActivityId();
		} else {
			historyDetailsReportForm
				.setSelectedPlanReviewActivityId(planReviewActivityId);
			session.removeAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID);
		}
		
		if (StringUtils.isBlank(planReviewRequestId)) {
			planReviewRequestId = historyDetailsReportForm
					.getSelectedPlanReviewRequestId();
		} else {
			historyDetailsReportForm
				.setSelectedPlanReviewRequestId(planReviewRequestId);
			session.removeAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID);
		}


		if ((StringUtils.isBlank(planReviewActivityId) || !StringUtils.isNumeric(planReviewActivityId)) 
				|| (StringUtils.isBlank(planReviewRequestId)|| !StringUtils.isNumeric(planReviewRequestId))) {
			// navigate back to BOB main page. 
			return forwards.get(FORWARD_HOME_PAGE);
		}
		
		String forward = super.doCommon( reportForm, request,
				response);

		PlanReviewReportPrintHistoryDetailsReportData reportData = (PlanReviewReportPrintHistoryDetailsReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		
		if( reportData != null ) {
			historyDetailsReportForm
					.setHistoryDetails((List<PlanReviewHistoryDetailsReportItem>) reportData
							.getDetails());
		}
		
		List<PlanReviewReportUIHolder> uiHolders = historyDetailsReportForm.getUiHolders();
		
		if(uiHolders == null || uiHolders.isEmpty() || uiHolders.size() > 1) {
			// This page should show one Plan Review Report details for the activity
			// if not, navigate back to BOB main page. 
			return forwards.get(FORWARD_HOME_PAGE);
		}
		

		if (StringUtils.equals(DEFAULT_TASK, getTask(request))) {
			historyDetailsReportForm
					.setViewDisableReason(StringUtils.EMPTY);
			historyDetailsReportForm
					.setViewDisableReasonMap(PlanReviewReportUtils
							.getViewDisableReasonMap());
		}

		return forward;
	}
	
	
	/**
	 * This method will redirect to the PrintReport Page when the user is
	 * clicked on Requested Print Copies in I Want... section in Results page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=print"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrint(@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
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
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrint() in PlanReviewReportPrintHistoryDetailsAction");
		}
		
		
		request.getSession(false).setAttribute(
				BDConstants.PLAN_REVIEW_ACTIVITY_ID,
				actionForm.getSelectedPlanReviewActivityId());

		super.setRegularPageNavigation(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doPrint() in PlanReviewReportPrintHistoryDetailsAction.");
		}
		return forwards.get( getTask(request));
	}
	
	
	/**
	 * This method is called to Resubmit the user print request contracts in the
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
	@RequestMapping(value ="/planReview/HistoryDetails/" ,params={"task=reSubmitPlanReviewPrintRequest"}   , method =  {RequestMethod.POST}) 
	public String doReSubmitPlanReviewPrintRequest (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReSubmitPlanReviewRequestPrint() in PlanReviewReportPrintHistoryDetailsAction");
		}

		
		PlanReviewRequestVO planReviewRequest = PlanReviewReportUtils
				.populateRequestDetails(request, RequestTypeCode.PRINT_DOC);
		
		String printActivityId = form
				.getSelectedPrintActivityId();
		String contractId = form
				.getSelectedPlanReviewContractId();
		
		String publishActivityId = form.getSelectedPlanReviewActivityId();
		
		
		if(StringUtils.isBlank(printActivityId) || !StringUtils.isNumeric(printActivityId)) {
			// navigate back to BOB main page. DONE
			return forwards.get(FORWARD_HOME_PAGE);
		}
		
		if(StringUtils.isBlank(publishActivityId) || !StringUtils.isNumeric(publishActivityId)) {
			// navigate back to BOB main page. DONE
			return forwards.get(FORWARD_HOME_PAGE);
		}
		
		// This method is used to store the Report date when user Resubmits the
		// Request.
		// clicks on GenerateReport.

		PlanReviewRequestVO printPlanReviewRequest = PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.reSubmitPlanReviewPrintRequest(planReviewRequest,
						Integer.valueOf(contractId),
						Integer.valueOf(printActivityId));
		
		//CR9 Changes
		//When the user clicks on the Next button the 
		//system will determine for each contract row selected on the Step 1 page, 
		//if any contract rows meet the Allowed Request Limit Condition for PDF requests,
		
		if (printPlanReviewRequest.isContractReachedMaxLimit()) {

			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new ValidationError("ALLOWED_PLAN_REVIEW_REQUEST_LIMIT",
					BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
					new Object[] { contractId }));

			setErrorsInRequest(request, errors);
			
			doCommon( form, request, response);
			
			return forwards.get("input");

		} else {

			BDUserProfile userProfile = getUserProfile(request);

			long userProfileId = userProfile.getBDPrincipal().getProfileId();

			PlanReviewRequestVO getPlanReviewDocListRequest = PlanReviewReportUtils
					.populateRequestDetails(request,
							RequestTypeCode.GET_DOC_LIST);

			List<ActivityVo> activityList = new ArrayList<ActivityVo>();

			Date reportMonthEndDate = null;

			for (ActivityVo printActivity : printPlanReviewRequest
					.getActivityVoList()) {

				ActivityVo activity = new ActivityVo();
				activity.setContractId(printActivity.getContractId());
				activity.setTypeCode(ActivityTypeCode.GET_DOC_LIST);
				activity.setStatusCode(ActivitySatusCode.PENDING);

				PrintDocumentPackgeVo printPackage = (PrintDocumentPackgeVo) printActivity
						.getDocumentPackageVo();

				reportMonthEndDate = printPackage.getPeriodEndDate();

				populateActivityEvent(activity,
						ActivityEventCode.GET_DOC_LIST_START,
						ActivityEventStatus.OK, "OK");

				activityList.add(activity);
			}

			getPlanReviewDocListRequest.setActivityVoList(activityList);

			// insert getdocList
			getPlanReviewDocListRequest = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.insertGetDocumentListRequestDetails(
							getPlanReviewDocListRequest);

			populateResubmitTibcoGetDocumentListProperties(
					getPlanReviewDocListRequest, request);

			for (ActivityVo getDocListActivity : getPlanReviewDocListRequest
					.getActivityVoList()) {

				// TIBCO GET DOC LIST call
				PlanReviewReportDocumentPackage planReviewReportDocPackage = getPlanReviewDocumentList(
						String.valueOf(userProfileId), getDocListActivity,
						publishActivityId, reportMonthEndDate);

				ActivityVo printActivity = null;

				for (ActivityVo activity : printPlanReviewRequest
						.getActivityVoList()) {

					if (StringUtils.equals(
							String.valueOf(getDocListActivity.getContractId()),
							String.valueOf(activity.getContractId()))) {

						printActivity = activity;
					}
				}

				if (printActivity == null) {
					logger.error("Invalid print request. getPlanReviewDocListRequest =["
							+ getPlanReviewDocListRequest
							+ "],   printPlanReviewRequest=["
							+ printPlanReviewRequest + "]");
					continue;
				}

				
				if (planReviewReportDocPackage != null) {
					
					boolean isHavingPrintDocument = false;
					String dstDocumentID = StringUtils.EMPTY;
					for (PlanReviewReportDocumentVo document : planReviewReportDocPackage.getPlanReviewReportDocumentVoList()) {

						if (StringUtils.equalsIgnoreCase(
								PlanReviewDocumentType.PLAN_REVIEW_HIGH_RESOLUTION
										.getDocumentTypeCode(), document
										.getDocumentType())) {
							isHavingPrintDocument = true;
							dstDocumentID = document.getDocumentId();
							break;
						}
					}
					
					if(isHavingPrintDocument) {
						
						printActivity.setDstoDocumentPackage(planReviewReportDocPackage);
						
						// record the activity event to OK
						PlanReviewServiceDelegate.getInstance(
								new BaseEnvironment().getApplicationId())
								.recordPlanReveiwActivityEvent(
										printActivity.getActivityId(),
										new ActivityEventVo(EventSourceCode.RPS,
												ActivityEventCode.OPEN_PRINT,
												ActivityEventStatus.OK,
												"OK"));
						
						// Trigger webservice TIBCO PRINT DOC for Contract Review Report PDF
						callPrintPlanReviewDocument(printActivity, String.valueOf(userProfileId), dstDocumentID);
						
					} else {
						
						// record the activity event to fail
						PlanReviewServiceDelegate.getInstance(
								new BaseEnvironment().getApplicationId())
								.recordPlanReveiwActivityEvent(
										printActivity.getActivityId(),
										new ActivityEventVo(EventSourceCode.RPS,
												ActivityEventCode.OPEN_PRINT,
												ActivityEventStatus.FAILED,
												"Failed, since no print doc found in GetDocList response."));
					}
				} else {
					
					// record the activity event to fail
					PlanReviewServiceDelegate.getInstance(
							new BaseEnvironment().getApplicationId())
							.recordPlanReveiwActivityEvent(
									printActivity.getActivityId(),
									new ActivityEventVo(EventSourceCode.RPS,
											ActivityEventCode.OPEN_PRINT,
											ActivityEventStatus.FAILED,
											"Failed, since no documents found in GetDocList response."));
					
				}
			}

		}
		
		//log the resubmit activity
		logActivityData(printPlanReviewRequest, request, printActivityId, form);

		setRegularPageNavigation(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doReSubmitRequest() in PlanReviewReportPrintHistoryDetailsAction.");
		}
		
		forward = doCommon( form, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 		
	}

	
	@Override
	protected String getDefaultSort() {
		return PlanReviewReportPrintHistoryDetailsReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected String getReportId() {
		return PlanReviewReportPrintHistoryDetailsReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return PlanReviewReportPrintHistoryDetailsReportData.REPORT_NAME;
	}

	
	@Override
	protected void populateReportForm( BaseReportForm reportForm, 
			HttpServletRequest request) {
		
		super.populateReportForm( reportForm, request);
		
		PlanReviewHistoryDetailsReportForm historyDetailsReportForm = (PlanReviewHistoryDetailsReportForm) reportForm;
		
		String planReviewActivityId = historyDetailsReportForm
				.getSelectedPlanReviewActivityId();
		String planReviewRequestId = historyDetailsReportForm
				.getSelectedPlanReviewRequestId();

		boolean isPublishRequest = true;
		
		try {
			
			PlanReviewRequestVO planReviewRequestVo = (PlanReviewRequestVO) PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.retrievePlanReviewRequestDetails(
							planReviewRequestId, planReviewActivityId, isPublishRequest);
			
			List<PlanReviewReportUIHolder> uiHolders = populateDisplayPlanReviewReport(planReviewRequestVo);
			
			historyDetailsReportForm.setUiHolders(uiHolders);
			
		} catch (SystemException exception) {
			throw new RuntimeException("Exception Occured while retrievePlanReviewRequestDetails for "
					+ "planReviewRequestId: " + planReviewRequestId
					+ ", planReviewActivityId: " + planReviewActivityId, exception);
		}
		
	};
	
	/**
	 * 
	 * @param planReviewRecordVo
	 * @return
	 */
	private List<PlanReviewReportUIHolder> populateDisplayPlanReviewReport(
			PlanReviewRequestVO planReviewRecordVo) {

		List<PlanReviewReportUIHolder> planResultList = new ArrayList<PlanReviewReportUIHolder>();
		List<ActivityVo> activityVoList = planReviewRecordVo
				.getActivityVoList();

		for (ActivityVo activityVo : activityVoList) {
			
			PlanReviewReportUIHolder planReviewReportUIHolder = new PlanReviewReportUIHolder();
			
			planReviewReportUIHolder.setActivityId(activityVo.getActivityId());
			planReviewReportUIHolder.setPublishDocumentActivityId(activityVo
					.getActivityId());
			planReviewReportUIHolder.setContractNumber(activityVo
					.getContractId());
			planReviewReportUIHolder.setCreatedTimeStamap(activityVo
					.getCreatedTS());
			planReviewReportUIHolder.setContractName(activityVo
					.getContractName());
			
			PublishDocumentPackageVo docpackageVo = (PublishDocumentPackageVo) activityVo
					.getDocumentPackageVo();
			planReviewReportUIHolder.setSelectedReportMonthEndDate(DateRender
					.formatByPattern(docpackageVo.getPeriodEndDate(), null,
							RenderConstants.MEDIUM_YMD_DASHED,
							RenderConstants.MEDIUM_MDY_SLASHED));
			
			planReviewReportUIHolder.setDocumentPakgId(activityVo.getActivityId());
			planReviewReportUIHolder.setAgencySellerId(planReviewRecordVo.getBrokerSellerId());
			
			planResultList.add(planReviewReportUIHolder);
			
		}

		return planResultList;
	}
	
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		PlanReviewHistoryDetailsReportForm historyDetailsReportForm = (PlanReviewHistoryDetailsReportForm) reportForm;

		criteria.addFilter(
				PlanReviewReportPrintHistoryDetailsReportData.FILTER_PLAN_REVIEW_ACTIVITY_ID,
				historyDetailsReportForm
						.getSelectedPlanReviewActivityId());

		criteria.addFilter(
				PlanReviewReportPrintHistoryDetailsReportData.FILTER_PLAN_REVIEW_REQUEST_ID,
				historyDetailsReportForm
						.getSelectedPlanReviewRequestId());

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		//extternal use
		if (!userProfile.isInternalUser()) {
			criteria.addFilter(
					PlanReviewReportPrintHistoryDetailsReportData.FILTER_BROKER_ID,
					getBrokerId(request));
			criteria.addFilter(
					PlanReviewReportPrintHistoryDetailsReportData.FILTER_IS_EXTERNAL_USER,
					Boolean.TRUE);
		}else {
			// internal user
			criteria.addFilter(
					PlanReviewReportPrintHistoryDetailsReportData.FILTER_IS_EXTERNAL_USER,
					Boolean.FALSE);

		}

	}
	
	/**
     * This method is called when the user clicks on "CSV" button. This method generates the CSV
     * file to be given back to the user.
     */
	@Override
	protected byte[] getDownloadData(BaseReportForm form,
			ReportData report, HttpServletRequest request)
			throws SystemException {
        return null;
	}
	
	
	/**
	 * This method is called user clicks on shipping details in History Detail
	 * page
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=backToMainHistory"} , method =  {RequestMethod.POST}) 
	public String doBackToMainHistory (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
		
	}
	
	/**
	 * This method is called user clicks on shipping details in History Detail
	 * page
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=shippingDetails"} , method =  {RequestMethod.POST}) 
	public String doShippingDetails (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		

		String jsonObj = request.getParameter("jsonObj");

		if (StringUtils.isNotEmpty(jsonObj)) {

			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(jsonObj);

			String printActivityId = obj.get("printActivityId").getAsString();

			if (StringUtils.isBlank(printActivityId)
					|| !StringUtils.isNumeric(printActivityId)) {
				return null;
			} else {

				List<PlanReviewHistoryDetailsReportItem> historyDetails = form
						.getHistoryDetails();
				
				if(historyDetails == null || historyDetails.isEmpty()) {
					return null;
				}

				PlanReviewHistoryDetailsReportItem item = new PlanReviewHistoryDetailsReportItem();
				item.setPrintActivityId(Integer.parseInt(printActivityId));

				int index = historyDetails.indexOf(item);

				if (index == -1) {
					return null;
				}

				item = historyDetails.get(index);
				
				ShippingVO shippingAddressDetails = item.getShippingAddressDetails();
				form.setShippingAddressDetails(shippingAddressDetails);
				
				return forwards.get( getTask(request));
			}

		} else {
			return null;
		}
	}
	
	
	/**
	 * Method to show the View Disable Overlay
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=showViewDisableReport"}, method =  {RequestMethod.POST}) 
	public String doShowViewDisableReport (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		return forwards.get( getTask(request));
	}
    
	/**
	 * The doViewDisable method has been written to insert the record in PLAN_REVIEW_DSTO_DOCUMENT_STATUS_HISTORY Table
	 * and disable the record for future
	 *  
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=deletePlanReviewReport"}, method =  {RequestMethod.POST}) 
	public String doDeletePlanReviewReport (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		
		BDUserProfile userProfile = getUserProfile(request);

		long userProfileId = userProfile.getBDPrincipal().getProfileId();

		if ((!userProfile.isInternalUser()) && userProfile.isInMimic()) {

			BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
					.getMimckingUserProfile(request);
			userProfileId = mimickingUserProfile.getBDPrincipal()
					.getProfileId();
		}

		String planReviewActivityId = form
				.getSelectedPlanReviewActivityId();

		if (StringUtils.isBlank(planReviewActivityId)
				|| !StringUtils.isNumeric(planReviewActivityId)) {
			// navigate back to BOB main page. DONE
			return forwards.get(FORWARD_HOME_PAGE);
		}

		Map<String, String> viewDisableReasonMap = form
				.getViewDisableReasonMap();

		String viewDisableReason = StringUtils.EMPTY;
		if (userProfile.isInternalUser() || userProfile.isInMimic()) {
			viewDisableReason = viewDisableReasonMap
					.get(form.getViewDisableReason());

		}
		
		PlanReviewServiceDelegate.getInstance(
				Environment.getInstance().getApplicationId())
				.deletePlanReviewReport(Integer.parseInt(planReviewActivityId),
						userProfileId, viewDisableReason);
		
		// set to default.
		form.setViewDisableReason(StringUtils.EMPTY);;
		
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
	}
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=planReviewRequest"}, method =  {RequestMethod.POST}) 
	public String doPlanReviewRequest (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
	
		HttpSession session = request.getSession(false);

		session.setAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT, Boolean.TRUE);
		
		setRegularPageNavigation(request);
		
		return forwards.get(getTask(request));
	}
	
	
	
	/**
	 * This method called when the user clicks on ViewPdf image icon in Results
	 * page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=viewPdf"}, method =  {RequestMethod.POST}) 
	public String doViewPdf (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doViewPdf");
		}

		

		ArrayList<PlanReviewReportUIHolder> planReviewRecords = (ArrayList<PlanReviewReportUIHolder>) form
				.getUiHolders();

		String activityId = form.getSelectedPlanReviewActivityId();
		String viewPdfRequestType = form.getType();

		if (StringUtils.isBlank(activityId)
				|| StringUtils.isBlank(viewPdfRequestType)
				|| !StringUtils.isNumeric(activityId)) {

			return forwards.get(FORWARD_HOME_PAGE);
		}

		ByteArrayOutputStream pdfOutStream = null;
		String documentTitle = StringUtils.EMPTY;
		try {

			PlanReviewDocumentType planReviewDocumentType = PlanReviewDocumentType
					.getPlanReviewDocumentType(viewPdfRequestType);

				PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
						Collections.unmodifiableList(planReviewRecords), request, planReviewDocumentType);

				if (document != null) {

					PlanReviewReportUIHolder uiHolder = null;

					for (PlanReviewReportUIHolder report : planReviewRecords) {
						uiHolder = report;
					}
					
					if(uiHolder == null) {
						return null;
					}

					Date reportMonthEndDate = null;

					try {
						reportMonthEndDate = SHORT_MDY_FORMATTER.parse(uiHolder
								.getSelectedReportMonthEndDate());
					} catch (ParseException e) {
						throw new SystemException(e,
								"Invalid Plan Reivew Report Month-End Date: "
										+ uiHolder.getSelectedReportMonthEndDate());
					}

					if (PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY
							.equals(planReviewDocumentType)) {

						documentTitle = "ES-"
								+ uiHolder.getContractNumber()
								+ "_"
								+ dashedMmmdydateFormat.format(reportMonthEndDate)
								+ "_Requested-"
								+ dashedMmmdydateAndTimeFormat.format(uiHolder
										.getCreatedTimeStamap());

					} else {

						documentTitle = "PRR-"
								+ uiHolder.getContractNumber()
								+ "_"
								+ dashedMmmdydateFormat.format(reportMonthEndDate)
								+ "_Requested-"
								+ dashedMmmdydateAndTimeFormat.format(uiHolder
										.getCreatedTimeStamap());
					}
					
					pdfOutStream = new ByteArrayOutputStream(document.getDocument().length);
					pdfOutStream.write(document.getDocument(), 0, document.getDocument().length);
					
					// freeing memory
					document.setDocument(null);
					
				} else {
					
					logger.error("Invalid PDF Document requested for publish active Id: " 
							+ activityId
							+", PDF type: " + planReviewDocumentType.getDocumentTypeCode());
					
					return null;
				}

			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"" + documentTitle + "\"");
			response.setContentLength(pdfOutStream.size());

			ServletOutputStream outputStream = response.getOutputStream();
			pdfOutStream.writeTo(outputStream);
			outputStream.flush();

		} catch (IOException ioException) {
			throw new SystemException(ioException,
					"Exception writing pdfData for the [RequestId, ActivityId, PdfType] is: ["
							+ form
									.getSelectedPlanReviewRequestId() + ", " 
							+ form.getSelectedPlanReviewActivityId() + ", "
							+ viewPdfRequestType + "]");
		} finally {
			try {
				response.getOutputStream().close();

				if (pdfOutStream != null) {
					pdfOutStream.close();
				}

			} catch (IOException ioException) {
				logger.error("Exception writing pdfData.", ioException);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doViewPdf");
		}

		return null;
	}
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=openPdfWindow"}, method =  {RequestMethod.POST}) 
	public String doOpenPdfWindow (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		
		String selectedPlanReviewActivityId = form.getSelectedPlanReviewActivityId();
		String viewPdfRequestType = form.getType();
		
		request.setAttribute("selectedPlanReviewActivityId", selectedPlanReviewActivityId);
		request.setAttribute("type", viewPdfRequestType);
		request.setAttribute("planReviewLevel", "bob");
		
		
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
		
	}

	/**
	 * This method is invokes when the user clicks on download pdf image icon in
	 * Results page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return null so that Struts will not try to forward to another page.
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=downloadPdf"}, method =  {RequestMethod.POST}) 
	public String doDownloadPdf (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownloadPdf");
		}

		

		ArrayList<PlanReviewReportUIHolder> planReviewRecords = (ArrayList<PlanReviewReportUIHolder>) form
				.getUiHolders();

		String activityId = form.getSelectedPlanReviewActivityId();
		String viewPdfRequestType = form.getType();

		if (StringUtils.isBlank(activityId)
				|| StringUtils.isBlank(viewPdfRequestType)
				|| !StringUtils.isNumeric(activityId)) {

			return forwards.get(FORWARD_HOME_PAGE);
		}
		
		ByteArrayOutputStream pdfOutStream = null;
		
		HttpSession session = request.getSession(false);
    	session.setAttribute(IS_DOWNLOAD_PDF_GENERATED, false);

		try {

			PlanReviewDocumentType planReviewDocumentType = PlanReviewDocumentType
					.getPlanReviewDocumentType(viewPdfRequestType);

			PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
					Collections.unmodifiableList(planReviewRecords), request, planReviewDocumentType);

			if (document == null) {

				logger.error("Invalid PDF Document requested for publish active Id: "
						+ activityId
						+ ", PDF type: "
						+ planReviewDocumentType.getDocumentTypeCode());

				return null;
			}

			PlanReviewReportUIHolder uiHolder = null;

			for (PlanReviewReportUIHolder report : planReviewRecords) {
				uiHolder = report;
			}
			
			if(uiHolder == null) {
				return null;
			}

			String documentTitle = StringUtils.EMPTY;

			Date reportMonthEndDate = null;

			try {
				reportMonthEndDate = SHORT_MDY_FORMATTER.parse(uiHolder
						.getSelectedReportMonthEndDate());
			} catch (ParseException e) {
				throw new SystemException(e,
						"Invalid Plan Reivew Report Month-End Date: "
								+ uiHolder.getSelectedReportMonthEndDate());
			}

			if (PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY
					.equals(planReviewDocumentType)) {

				documentTitle = "ES-"
						+ uiHolder.getContractNumber()
						+ "_"
						+ dashedMmmdydateFormat.format(reportMonthEndDate)
						+ "_Requested-"
						+ dashedMmmdydateAndTimeFormat.format(uiHolder
								.getCreatedTimeStamap());

			} else {

				documentTitle = "PRR-"
						+ uiHolder.getContractNumber()
						+ "_"
						+ dashedMmmdydateFormat.format(reportMonthEndDate)
						+ "_Requested-"
						+ dashedMmmdydateAndTimeFormat.format(uiHolder
								.getCreatedTimeStamap());
			}
			
			if(document.getDocument() != null) {
				pdfOutStream = new ByteArrayOutputStream(document.getDocument().length);
				pdfOutStream.write(document.getDocument(), 0, document.getDocument().length);
				
				// freeing memory
				document.setDocument(null);
				
			} else {
				
				logger.error("Invalid PDF Document requested for publish active Id: "
						+ activityId
						+ ", PDF type: "
						+ planReviewDocumentType.getDocumentTypeCode());
			}
			
			
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + documentTitle +".pdf\"");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/pdf");
			response.setContentLength(pdfOutStream.size());
			
			session.setAttribute(IS_DOWNLOAD_PDF_GENERATED, true);
			
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
			
		} catch (IOException exception) {
			
			throw new SystemException(exception,
					"Exception writing pdfData for the [RequestId, ActivityId, PdfType] is: ["
							+ form
									.getSelectedPlanReviewRequestId() + ", " 
							+ form.getSelectedPlanReviewActivityId() + ", "
							+ viewPdfRequestType + "]");
			
		} finally {
			try {
				response.getOutputStream().close();
				
				if(pdfOutStream != null){
					pdfOutStream.close();
				}
			} catch (IOException ioException) {
				logger.error("Exception while closing resources.", ioException);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doViewPdf");
		}
		return null;
	}
	
	/**
	 * Store the file names in server location.
	 * 
	 * @param fileName
	 * @param output
	 * @throws SystemException
	 */
	public void saveFileOnReportServer(String fileName,
			byte[] output) throws SystemException {

		FileOutputStream fos = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {

				if(!file.getParentFile().mkdirs()) {
					logger.error("Could not able make parent dirs. " + file.getParentFile().getAbsolutePath());
				}
				
				if(!file.createNewFile()) {
					logger.error("Could not able to create file. " + file.getAbsolutePath());
				}
			}
			fos = new FileOutputStream(file);
			fos.write(output);
			fos.flush();

		} catch (Exception e) {
			logger.error("Error occured while saving file: " + fileName , e);
			throw new SystemException(e,
					"Error occured while saving file: " + fileName + " with exception : "
							+ e.getMessage());
		} finally {

			try {

				if (fos != null) {
					fos.close();
				}

			} catch (IOException exception) {
				logger.error("Error occured while closing resources for file: " + fileName , exception);
			}
		}
	}
	
	

	/**
	 * This method is invoke when the user click the DownloadSelected button in
	 * Result page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return null so that Struts will not try to forward to another page.
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/HistoryDetails/", params={"task=downloadSelectedPdf"}, method =  {RequestMethod.POST}) 
	public String doDownloadSelectedPdf (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownloadSelectedPdf");
		}
		
		

		HttpSession session = request.getSession(false);
    	session.setAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED, false);
		
		ArrayList<PlanReviewReportUIHolder> planReviewRecords = (ArrayList<PlanReviewReportUIHolder>) form
				.getUiHolders();

		List<PlanReviewReportUIHolder> downloadPlanReviewReportPdfs = new ArrayList<PlanReviewReportUIHolder>();
		List<PlanReviewReportUIHolder> downloadExeSummaryPdfs = null;
		
		ByteArrayOutputStream zipOutputStream = null;
		
		try {
			
			Map<String, File> filemap = new HashMap<String, File>();
			
			String fileServerPath = PlanReviewRequestProperties
					.get(PlanReviewRequestProperties.FILE_SERVER_PATH)
					+ PlanReviewRequestProperties
							.get(PlanReviewRequestProperties.PREVIEW_IMAGES_FOLDER);
			
			// Block to trigger Download PDF for Plan Review Reports
			// Replace with Plan Review Reports specific webservice Logic
			if (form.isDownloadPlanReviewReportIndicator()) {
				
				downloadPlanReviewReportPdfs = planReviewRecords;
			}
			
			if (downloadPlanReviewReportPdfs != null) {
				
				for(PlanReviewReportUIHolder planReviewReportUIHolder : Collections.unmodifiableList(downloadPlanReviewReportPdfs)) {
				
					List<PlanReviewReportUIHolder> uiHolders = new ArrayList<PlanReviewReportUIHolder>();
					uiHolders.add(planReviewReportUIHolder);
					
					PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
							Collections.unmodifiableList(uiHolders), request, PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION);

					if (document != null) {
						
						String documentTitle = StringUtils.EMPTY;
						
						Date reportMonthEndDate = null;

						try {
							reportMonthEndDate = SHORT_MDY_FORMATTER.parse(planReviewReportUIHolder
									.getSelectedReportMonthEndDate());
						} catch (ParseException e) {
							throw new SystemException(e,
									"Invalid Plan Reivew Report Month-End Date: "
											+ planReviewReportUIHolder.getSelectedReportMonthEndDate());
						}
						
						documentTitle = "PRR-" + planReviewReportUIHolder.getContractNumber() + "_" 
								+ dashedMmmdydateFormat.format(reportMonthEndDate)
								+ "_Requested-" + dashedMmmdydateAndTimeFormat.format(planReviewReportUIHolder.getCreatedTimeStamap());
							
							
						String pdfFilePath = fileServerPath 
								+ document.getActivityId() + "_" +new Date().getTime() + File.separator
								+ documentTitle+".pdf";
						
						saveFileOnReportServer(pdfFilePath, document.getDocument());
						
						filemap.put(documentTitle + ".pdf", new File(pdfFilePath));
						
						//setting null to free out memory
						document.setDocument(null);
					
					} else {
						logger.error("Invalid PDF Document requested for publish active Id: " 
								+ planReviewReportUIHolder.getPublishDocumentActivityId()
								+", PDF type: " + PlanReviewDocumentType.PLAN_REVIEW_LOW_RESOLUTION.getDocumentTypeCode());
					}
				}
			}
			
			// logic to download full executive summary pdf
			if (form.isDownloadExcecutiveSummaryIndicator()) {
				
				downloadExeSummaryPdfs = planReviewRecords;
			}
			
			// Trigger Webservice call and replace below PDF files
			// with PDF files received in Webservice response
			if (downloadExeSummaryPdfs != null) {
				
				for(PlanReviewReportUIHolder planReviewReportUIHolder : downloadExeSummaryPdfs) {
					
					List<PlanReviewReportUIHolder> uiHolders = new ArrayList<PlanReviewReportUIHolder>();
					uiHolders.add(planReviewReportUIHolder);
					
					PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
							Collections.unmodifiableList(uiHolders), request, PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY);

					if (document != null) {
						
						
						String documentTitle = StringUtils.EMPTY;
						
						Date reportMonthEndDate = null;

						try {
							reportMonthEndDate = SHORT_MDY_FORMATTER.parse(planReviewReportUIHolder
									.getSelectedReportMonthEndDate());
						} catch (ParseException e) {
							throw new SystemException(e,
									"Invalid Plan Reivew Report Month-End Date: "
											+ planReviewReportUIHolder.getSelectedReportMonthEndDate());
						}
							
						documentTitle = "ES-" + planReviewReportUIHolder.getContractNumber() + "_" 
						+ dashedMmmdydateFormat.format(reportMonthEndDate)
						+ "_Requested-" + dashedMmmdydateAndTimeFormat.format(planReviewReportUIHolder.getCreatedTimeStamap());
						
						String pdfFilePath = fileServerPath 
								+ document.getActivityId() + "_" +new Date().getTime() + File.separator
								+ documentTitle+".pdf";
						
						saveFileOnReportServer(pdfFilePath, document.getDocument());
						
						filemap.put(documentTitle + ".pdf", new File(pdfFilePath));
						
						//setting Document bytes to null to free out memory
						document.setDocument(null);
					
					} else {
						logger.error("Invalid PDF Document requested for publish active Id: " 
								+ planReviewReportUIHolder.getPublishDocumentActivityId()
								+", PDF type: " + PlanReviewDocumentType.PLAN_REVIEW_EXECUTIVE_SUMMARY.getDocumentTypeCode());
					}
				}
			}

			if (!filemap.isEmpty()) {
				zipOutputStream = createZipFileStream(filemap);
			}
			
			if(zipOutputStream != null) {
					
				ServletOutputStream sos;
				sos = response.getOutputStream();
				Date date = new Date(System.currentTimeMillis());
				String formattedDate = dashedMdydateFormat.format(date);
				
				response.setContentType("application/zip");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"PLAN_REVIEW_REPORTS_" + formattedDate
								+ ".zip\"");
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setContentLength(zipOutputStream.size());
				
				session.setAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED, true);
				
				zipOutputStream.writeTo(sos);
				sos.flush();
				
			}
		
		} catch (IOException e) {
			
			session.setAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED, true);
			
			throw new SystemException(
					"Exception in Downloadselectedallpdfs for RequestId is:"
							+ form.getSelectedPlanReviewActivityId());
			
		} finally {
			try {
				response.getOutputStream().close();
				
				if(zipOutputStream != null){
					zipOutputStream.close();
				}
			
			} catch (IOException ioException) {
				logger.error("Exception writing pdfData.", ioException);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doDownloadSelectedPdf");
		}
		
		return null;
	}
	
	/**
	 * This method is used to create Zip file
	 * 
	 * @param fileNames
	 * @param zipFileName
	 * @throws SystemException
	 */
	public ByteArrayOutputStream createZipFileStream(Map<String, File> fileList)
			throws SystemException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(baos);
		
		try {
			for (Entry<String, File> fileInfo : fileList.entrySet()) {
				
				String fileName = fileInfo.getKey();
				File document = fileInfo.getValue();
				
				out.putNextEntry(new ZipEntry(fileName));
				
				InputStream is = null;
				
				try {

					is = new FileInputStream(document);
					out.write(IOUtils.toByteArray(is));
					
				} catch (IOException e) {
					throw new RuntimeException("Error Occured while reading file: "
							+ document.getAbsolutePath(), e);
				} finally {
					try {
						if(is != null) {
							is.close();
						}
					} catch (IOException e) {
						logger.error("Error Occured while closing input stream for file: "
							+ document.getAbsolutePath(), e);
					}
				}
				
				out.closeEntry();
			}
			out.flush();
		} catch (Exception e) {
			throw new SystemException("Problem occured while Zipping the file."
					+ e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException exception) {
					logger.error(
							"Problem occured while closing the ZipOutputStream",
							exception);
					throw new SystemException(exception,
							"Problem occured while closing the ZipOutputStream: "
									+ exception.getMessage());
				}
			}
		}

		return baos;
	}
	
	protected PlanReviewReportDocumentPackage getPlanReviewDocumentList(
			String userProfileId, ActivityVo getDocListActivity,
			String publishDocActivityId, Date publishDocRequestedDate)
			throws SystemException {

		try {
			
			return PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.getPlanReviewDocumentList(userProfileId,
							getDocListActivity, publishDocActivityId,
							publishDocRequestedDate);
		
		} catch (Exception exception) {

			logger.error(
					"Exception occured for PlanReview getDocumentList request: ["
							+ getDocListActivity.toString()
							+ "] for publishDocActivityId: "
							+ publishDocActivityId, exception);

			// record the activity event to fail
			PlanReviewServiceDelegate.getInstance(
					new BaseEnvironment().getApplicationId())
					.recordPlanReveiwActivityEvent(
							getDocListActivity.getActivityId(),
							new ActivityEventVo(EventSourceCode.RPS,
									ActivityEventCode.GET_DOC_LIST_END,
									ActivityEventStatus.FAILED,
									StringUtils.substring(exception.getMessage(), 0, 254)));
		}

		return null;
	}
	
	protected void populateResubmitTibcoGetDocumentListProperties(
			PlanReviewRequestVO planReviewRequestVo, HttpServletRequest request) throws SystemException{
		
		for (ActivityVo actVo : planReviewRequestVo.getActivityVoList()) {
			
			actVo.setDivision(Division.USA);
			actVo.setBusinessUnit(BusinessUnit.RPS);
			actVo.setProductType(ProductType.LIFEINSURANCE);
			actVo.setProductSubType(ProductSubType.VARIABLELIFEINSURANCE);
			
			Map<PartyIdentifier, String> partyIdentifiersMap = new HashMap<PartyIdentifier, String>();

			
			partyIdentifiersMap.put(PartyIdentifier.CONTRACTNUMBER, String.valueOf(actVo.getContractId()));
			partyIdentifiersMap.put(PartyIdentifier.BROKERID,
					getBrokerId(request));
			partyIdentifiersMap.put(PartyIdentifier.USERID, "rpsuser");
			
			actVo.setPartyIdentifier(partyIdentifiersMap);
			
			actVo.setDocumentFormat(DocumentFormat.PDF);
			actVo.setDocumentType(DocumentType.PLANREVIEW);
			actVo.setDocumentStatus(DocumentStatus.ACTIVE);
			
		}
	}
	
	/**
	 * This method is used to populate populateActivityEvent data.
	 * 
	 * @param PlanReviewReportUIHolder
	 * @param ActivityVo
	 * @throws SystemException
	 */
	protected void populateActivityEvent(ActivityVo activity,
			ActivityEventCode activityEventCode,
			ActivityEventStatus eventStatus, String statusMessage) {
		
		List<ActivityEventVo> activityEventVoList = new ArrayList<ActivityEventVo>();
		ActivityEventVo activityEventVo = new ActivityEventVo();
		activityEventVo.setActivityEventCode(activityEventCode);
		activityEventVo.setActivityEventSourceCode(EventSourceCode.RPS);
		activityEventVo.setActivityEventStatus(eventStatus);
		activityEventVo.setStatusMessage(statusMessage);
		activityEventVo.setCreatedTimeStamp(new Timestamp(System
				.currentTimeMillis()));
		activityEventVoList.add(activityEventVo);
		activity.setActivityEventVoList(activityEventVoList);

	}
	
	private void callPrintPlanReviewDocument(ActivityVo printActivity,
			String userProfileId, String dstDocumentID) throws SystemException {

		populateTibcoPrintDocumentProperties(printActivity, dstDocumentID);

		try {
			PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.callPrintPlanReviewDocument(printActivity, userProfileId);
		} catch (Exception exception) {

			logger.error("Exception occured for PlanReview Print request: ["
					+ printActivity.toString() + "] for publishDocActivityId: "
					+ printActivity, exception);

			// record the activity event to fail
			PlanReviewServiceDelegate.getInstance(
					new BaseEnvironment().getApplicationId())
					.recordPlanReveiwActivityEvent(
							printActivity.getActivityId(),
							new ActivityEventVo(EventSourceCode.RPS,
									ActivityEventCode.SEND_PCF_PRINT,
									ActivityEventStatus.FAILED,
									StringUtils.substring(exception.getMessage(), 0, 254)));
		}
	}
	
	private void populateTibcoPrintDocumentProperties(ActivityVo activity, String dstDocumentID) {
		
		activity.setDivision(Division.USA);
		activity.setBusinessUnit(BusinessUnit.RPS);
		activity.setProductType(ProductType.ANNUITIES);
		activity.setProductSubType(ProductSubType.VARIABLELIFEINSURANCE);
		activity.setDocumentId(dstDocumentID);
		activity.setDocumentFormat(DocumentFormat.PDF);
		activity.setDocumentType(DocumentType.REGULAR);
		activity.setDocumentStatus(DocumentStatus.ACTIVE);
	}
	
	private void logActivityData(PlanReviewRequestVO planReviewRequestVo,
			HttpServletRequest request, String originalActivityID, PlanReviewHistoryDetailsReportForm detailsForm) {

		StringBuffer logData = new StringBuffer();

		logData.append(PlanReviewConstants.LOG_USER_PROFILE_ID)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(planReviewRequestVo.getUserProfileid())
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		logData.append(PlanReviewConstants.LOG_REQUESTED_DATE)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(new Date(planReviewRequestVo
						.getCreatedTS().getTime()))
				.append(BDConstants.SEMICOLON_SYMBOL)
				.append(BDConstants.SINGLE_SPACE_SYMBOL);

		logData.append(PlanReviewConstants.LOG_ACTION)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append("Plan Review Print Request Resubmission")
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
					.append(detailsForm.getSelectedPlanReviewContractName())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);

			PrintDocumentPackgeVo printPackage = (PrintDocumentPackgeVo) activity
					.getDocumentPackageVo();

			logData.append(PlanReviewConstants.LOG_MONTH_END_DATE)
					.append(BDConstants.SINGLE_SPACE_SYMBOL)
					.append(printPackage.getPeriodEndDate())
					.append(BDConstants.SEMICOLON_SYMBOL)
					.append(BDConstants.SINGLE_SPACE_SYMBOL);
		}

		logData.append(PlanReviewConstants.LOG_ORIGINAL_REQUESTED_DATE)
				.append(BDConstants.SINGLE_SPACE_SYMBOL)
				.append(detailsForm.getSelectedPlanReviewRequestedTS())
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
				"doReSubmitPlanReviewPrintRequest", logData.toString(), userProfile,
				logger, interactionLog, logRecord);
	}


	/** This method will check whether Plan Review Report or executive summary is 
     *  generated or not when the Download Icon is clicked.
     * 	If report generation is complete then response status code is set as 400(request fails)
     * 	This causes the AJAX request to fail and in the failure event waiting message will be closed.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException 
     */
	 @RequestMapping(value ="/planReview/HistoryDetails/", params={"task=checkDownloadPdfGenerated"} , method =  {RequestMethod.GET}) 
	    public String doCheckDownloadPdfGenerated (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    	if(request.getSession(false).getAttribute(IS_DOWNLOAD_PDF_GENERATED) != null) {
	        boolean isReportGenerated = (Boolean)request.getSession(false).getAttribute(IS_DOWNLOAD_PDF_GENERATED);
	        if (isReportGenerated) {
	        	response.setStatus(400);
	        }
    	}
        return null;

    }
    
    /** This method will check whether Plan Review Report or executive summary is 
     *  generated or not when the Download selected button is clicked.
     * 	If report generation is complete then response status code is set as 400(request fails)
     * 	This causes the AJAX request to fail and in the failure event waiting message will be closed.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException 
     */
    @RequestMapping(value ="/planReview/HistoryDetails/", params={"task=checkDownloadSelectedPdfGenerated"} , method =  {RequestMethod.GET}) 
    public String doCheckDownloadSelectedPdfGenerated (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    	if(request.getSession(false).getAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED) != null) {
	        boolean isReportGenerated = (Boolean)request.getSession(false).getAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED);
	        if (isReportGenerated) {
	        	response.setStatus(400); 
	        }
    	}
        return null;

    }
    
    /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
    
    /**
	 * This method will redirect to the CSRF Error Page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	    @RequestMapping(value ="/planReview/HistoryDetails/", params={"task=csrfError"} , method =  {RequestMethod.POST}) 
	    public String doCsrfError (@Valid @ModelAttribute("planReviewHistoryDetailsReportForm") PlanReviewHistoryDetailsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
	    	String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }

		return forwards.get("csrfErrorPage");
	}
	
    /** avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     */
   /* @Override
	protected boolean isTokenRequired(String action) {
		return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "Default")) ? true: false;
	}
*/
    /**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ezk.web.BaseAction#isTokenValidatorEnabled(java
	 * .lang.String)
	 */
	/*@Override
	protected boolean isTokenValidatorEnabled(String action) {

		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "DeletePlanReviewReport"))?true: false;
	
	}*/
    
}
