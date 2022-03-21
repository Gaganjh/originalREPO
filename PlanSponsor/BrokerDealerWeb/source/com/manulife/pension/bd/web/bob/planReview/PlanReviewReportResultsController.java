package com.manulife.pension.bd.web.bob.planReview;
//package com.manulife.pension.bd.web.bob.planReview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import org.apache.commons.lang3.time.FastDateFormat;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.planReview.sort.PlanReviewReportResultPageColumn;
import com.manulife.pension.bd.web.bob.planReview.util.GetPlanReviewDocumentUtility;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDefault;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.planReview.util.PlanReviewRequestProperties;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.util.PlanReviewConstants.ActivitySatusCode;
import com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This is the action class for the PlanReviewResults page.
 * 
 * @author Syntel.
 * 
 */
@Controller
@RequestMapping(value ="/bob/contract")
@SessionAttributes({"planReviewResultForm"})

public class PlanReviewReportResultsController extends BasePlanReviewReportController {
	@ModelAttribute("planReviewResultForm") 
	public PlanReviewResultForm populateForm() 
	{
		return new PlanReviewResultForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/planReview/resultsIndividualPlanReviewReport.jsp");
		forwards.put("default","/contract/planReview/resultsIndividualPlanReviewReport.jsp");
		forwards.put("print","redirect:/do/bob/contract/planReview/Print/");
		forwards.put("contractReviewReportStep1Page","redirect:/do/bob/contract/planReview/");
		forwards.put("history","redirect:/do/bob/contract/planReview/History/");
		forwards.put("planReviewRequest","redirect:/do/bob/contract/planReview/");
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("openPdfWindow","/WEB-INF/planReview/common/openPlanReviewResultsPDF.jsp");
		forwards.put("sort","/contract/planReview/resultsBobPlanReviewReports.jsp");
		forwards.put("backToMainHistory","/do/bob/contract/planReview/History/?task=navigatedFromDetails");
		
				}
	
	
	
	protected static final Logger logger = Logger.getLogger(PlanReviewReportResultsController.class);

	private static final String STATUS_IN_PROGRESS = "In Progress";
	private static final String STATUS_COMPLETED = "Complete";
	private static final String STATUS_RESUBMITTED = "Resubmitted";
	private static final String STATUS_IN_COMPLETE = "Incomplete";
	
	private static final String STATUS_COMPLETED_IMAGE =  "/assets/unmanaged/images/icon_done.gif";
	private static final String STATUS_IN_PROGRESS_IMAGE = "/assets/unmanaged/images/ajax-wait-indicator.gif";
	private static final String STATUS_IN_COMPLETE_IMAGE = "/assets/unmanaged/images/error.gif";
	private static final String STATUS_RESUBMITTED_IMAGE = "/assets/unmanaged/images/warning2.gif";
	
	private static final String FORMAT_DATE_EXTRA_LONG_MDY ="MM/dd/yyyy hh:mm:ss a 'ET'";
	
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
	protected static final FastDateFormat LONG_MDY_FORMATTER_REQUESTED_DATE = FastDateFormat
			.getInstance(FORMAT_DATE_EXTRA_LONG_MDY);
	/**
	 * Constructor of the class.
	 */
	public PlanReviewReportResultsController() {
		super(PlanReviewReportResultsController.class);
	}

	/**
	 * This method will used to implement the result data functionality.
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
	@RequestMapping(value ={"/planReview/Results/"},  method ={RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm actionForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault() in PlanReviewReportResultsAction");
		}
		 forward = super.doCommon( actionForm, request,
				response);
		HttpSession session = request.getSession(false);
		String planReviewRequestId = String.valueOf( session.getAttribute(
				BDConstants.PLAN_REVIEW_REQUEST_ID));
		
		if (StringUtils.isBlank(planReviewRequestId)
				|| !StringUtils.isNumeric(planReviewRequestId)) {

			return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE);
		}
		
		// removed the session attribute from the session.
		session.removeAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID);
		
		List<Integer> contractsWhichAlereadyReachedLimit = (List<Integer>) session
				.getAttribute(BDConstants.CONTRACTS_WHICH_ALEREADY_REACHED_LIMIT);
		
		if (contractsWhichAlereadyReachedLimit != null
				&& contractsWhichAlereadyReachedLimit.size() > 0) {
			StringBuffer contractIdStr = new StringBuffer();
			for (Integer contractId : contractsWhichAlereadyReachedLimit) {
				contractIdStr.append(contractId + ", &nbsp;");
			}
			String message = contractIdStr.substring(0,
					contractIdStr.lastIndexOf(","));
			Object[] messageArg = { message };

			ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
			errorMessages
					.add(new ValidationError(
							"contractsWhichAlereadyReachedLimit",
							BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
							messageArg));
			setErrorsInSession(request, errorMessages);
		}
		
		session
		.removeAttribute(BDConstants.CONTRACTS_WHICH_ALEREADY_REACHED_LIMIT);

		boolean isPublishRequest = true;

		PlanReviewRequestVO planReviewRequestVo = (PlanReviewRequestVO) PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.retrievePlanReviewRequestDetails(
						planReviewRequestId, null, isPublishRequest);

		List<PlanReviewReportUIHolder> resultList = populateDisplayPlanReviewReport(planReviewRequestVo);

		
		actionForm.setPlanReviewRequestId(planReviewRequestId);
		actionForm.setContractResultVOList(resultList);
		if (planReviewRequestVo.getCreatedTS() != null) {
			actionForm.setReportMonthEndDate(String
					.valueOf(LONG_MDY_FORMATTER_REQUESTED_DATE
							.format(planReviewRequestVo.getCreatedTS())));
		}
		
		Collections.sort(actionForm.getContractResultVOList(), PlanReviewReportResultPageColumn
				.getContractReviewReportResultPageColumn(getDefaultSort())
				.getComparatorInstance(getDefaultSortDirection()));
		
		int requestProcessed = 0;
		actionForm.setIncompleteErrorMessage(false);
		for(PlanReviewReportUIHolder uiHolder : resultList) {
			
			if(StringUtils.equalsIgnoreCase(STATUS_COMPLETED, uiHolder.getStatus())) {
				++requestProcessed;
			}
			if(StringUtils.equalsIgnoreCase(STATUS_IN_COMPLETE, uiHolder.getStatus())) {
				actionForm.setIncompleteErrorMessage(true);
			}
		}
		
		actionForm.setRequestsProcessed(requestProcessed);
		
		actionForm.setAllPlanReviewReportPdfsSelected(false);
		actionForm.setAllExeSummaryPdfsSelected(false);;
		
		Boolean isRequestFromHistory = (Boolean) session
				.getAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT);

		if (isRequestFromHistory != null) {
			actionForm
					.setRequestFromHistory(isRequestFromHistory);
			session.removeAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT);
		} else {
			actionForm.setRequestFromHistory(false);
		}

		request.setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE,
				forwards.get("input"));

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault() in PlanReviewReportResultsAction.");
		}
		
		actionForm.setErrorExists(Boolean.TRUE);
		
		return forwards.get(forward);
	}

	/**
	 * This method is called to checks PDF Request for every 20 sec's available
	 * contracts in the Report Table under the Request Status in
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
	@RequestMapping(value ={"/planReview/Results/"},params={"task=checkPlanReviewRequestStatus"}, method =  {RequestMethod.GET}) 
	public String doCheckPlanReviewRequestStatus (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCheckPlanReviewRequestStatus() in PlanReviewReportResultsAction");
		}

		String contractJsonObj = request.getParameter("jsonObj");

		JsonParser parser = new JsonParser();
		JsonArray contractArray = (JsonArray) parser.parse("["
				+ contractJsonObj + "]");

		ArrayList<String> responseTextArray = new ArrayList<String>();

		String planReviewRequestId = form
				.getPlanReviewRequestId();

		if (StringUtils.isBlank(planReviewRequestId)
				|| !StringUtils.isNumeric(planReviewRequestId)) {
			return null;
		}

		List<ActivityVo> refreshedAcitvityVoList = PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.getActivityStatusDetailsForRequestId(
						Integer.valueOf(planReviewRequestId));

		for (int index = 0; index < contractArray.size(); index++) {

			JsonObject contractObject = (JsonObject) contractArray.get(index);

			String contractId = (contractObject.get("ContractId"))
					.getAsString();
			String status = (contractObject.get("Status")).getAsString();
			String styleId = (contractObject.get("StyleId")).getAsString();

			if (STATUS_IN_PROGRESS.equalsIgnoreCase(status)) {

				for (ActivityVo activityVo : refreshedAcitvityVoList) {

					if (Integer.valueOf(contractId).equals(
							activityVo.getContractId())) {

						String responseText = "{";

						if ((ActivitySatusCode.OK).equals(activityVo
								.getStatusCode())) {

							status = STATUS_COMPLETED;

						} else if ((ActivitySatusCode.FAILED).equals(activityVo
								.getStatusCode())) {

							status = STATUS_IN_COMPLETE;

						} else {

							status = STATUS_IN_PROGRESS;
						}

						contractId = String.valueOf(activityVo.getContractId());

						// Creating a response object back into JSON form DAO
						// call
						responseText += "\"ContractId\":\"" + contractId.trim()
								+ "\",\"Status\":\"" + status.trim()
								+ "\",\"StyleId\":\"" + styleId.trim() + "\"";
						responseText += "}";
						responseTextArray.add(responseText.trim());

					}
				}
			}
		}
		// Sending the response back to AJAX call
		response.setContentType("text/html");
		PrintWriter out;

		try {
			out = response.getWriter();
		} catch (IOException exception) {
			throw new SystemException(exception,
					"IOException occured while checking the status of this RequestId "
							+ form
									.getPlanReviewRequestId());
		}

		out.print(responseTextArray);
		out.flush();

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCheckPlanReviewRequestStatus()in PlanReviewReportResultsAction.");
		}

		return null;
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
	@RequestMapping(value ={"/planReview/Results/"}, params={"task=viewPdf"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doViewPdf (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doViewPdf");
		}

		ArrayList<PlanReviewReportUIHolder> planReviewRecords = (ArrayList<PlanReviewReportUIHolder>) form
				.getContractResultVOList();
		ArrayList<PlanReviewReportUIHolder> viewpdfrecordlist = new ArrayList<PlanReviewReportUIHolder>();
		String contractId = request.getParameter("contract");
		String viewPdfRequestType = request.getParameter("type");

		if (StringUtils.isBlank(contractId)
				|| StringUtils.isBlank(viewPdfRequestType)
				|| !StringUtils.isNumeric(contractId)) {

			return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE);
		}

		ByteArrayOutputStream pdfOutStream = null;
		String documentTitle = StringUtils.EMPTY;
		try {

			PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
					contractId, planReviewRecords);

			if (planReviewReportUIHolder != null) {

				viewpdfrecordlist.add(planReviewReportUIHolder);

				PlanReviewDocumentType planReviewDocumentType = PlanReviewDocumentType
						.getPlanReviewDocumentType(viewPdfRequestType);

				PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
						Collections.unmodifiableList(viewpdfrecordlist), request, planReviewDocumentType);

				if (document != null) {
					
					PlanReviewReportUIHolder uiHolder = planReviewReportUIHolder;

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
							+ planReviewReportUIHolder.getPublishDocumentActivityId()
							+", PDF type: " + planReviewDocumentType.getDocumentTypeCode());
					
					return null;
				}

			} else {
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
					"Exception writing pdfData for the [RequestId, contractNumber, PdfType] is: ["
							+ form
									.getPlanReviewRequestId() + ", " + contractId + ", "
							+ viewPdfRequestType + "]");
		} finally {
			try {
				response.getOutputStream().close();

				if (pdfOutStream != null) {
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
	@RequestMapping(value ={"/planReview/Results/"}, params={"task=downloadPdf"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownloadPdf (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownloadPdf");
		}

		ArrayList<PlanReviewReportUIHolder> planReviewRecords = (ArrayList<PlanReviewReportUIHolder>) form
				.getContractResultVOList();
		ArrayList<PlanReviewReportUIHolder> viewpdfrecordlist = new ArrayList<PlanReviewReportUIHolder>();
		String contractId = String.valueOf(form.getDownloadContractId());
		String viewPdfRequestType = form.getType();
		
		HttpSession session = request.getSession(false);
    	session.setAttribute(IS_DOWNLOAD_PDF_GENERATED, false);

		if (StringUtils.isBlank(contractId)
				|| StringUtils.isBlank(viewPdfRequestType)
				|| !StringUtils.isNumeric(contractId)) {

			return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE);
		}
		
		ByteArrayOutputStream pdfOutStream = null;

		try {

			PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
					contractId, planReviewRecords);
			
			String documentTitle = StringUtils.EMPTY;

			if (planReviewReportUIHolder != null) {

				viewpdfrecordlist.add(planReviewReportUIHolder);

				PlanReviewDocumentType planReviewDocumentType = PlanReviewDocumentType
						.getPlanReviewDocumentType(viewPdfRequestType);

				PlanReviewReportDocumentVo document = new GetPlanReviewDocumentUtility().getPlanReviewDocument(
						Collections.unmodifiableList(viewpdfrecordlist), request, planReviewDocumentType);

				if (document != null) {
					
					PlanReviewReportUIHolder uiHolder = planReviewReportUIHolder;

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
								+ planReviewReportUIHolder.getPublishDocumentActivityId()
								+", PDF type: " + planReviewDocumentType.getDocumentTypeCode());
					}

				} else {
					
					logger.error("Invalid PDF Document requested for publish active Id: " 
							+ planReviewReportUIHolder.getPublishDocumentActivityId()
							+", PDF type: " + planReviewDocumentType.getDocumentTypeCode());
					
					return null;
				}

			} else {
				return null;
			}
			

			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + documentTitle +".pdf\"");
			response.setContentType("application/pdf");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
			response.setHeader("Pragma", "no-cache");
			response.setContentLength(pdfOutStream.size());
			
			session.setAttribute(IS_DOWNLOAD_PDF_GENERATED, true);
			
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();

		} catch (IOException exception) {
			
			session.setAttribute(IS_DOWNLOAD_PDF_GENERATED, true);
			
			throw new SystemException(exception,
					"Exception while DownloadpdfData for the [RequestId, contractNumber, PdfType] is: ["
							+ form
									.getPlanReviewRequestId() + ", " + contractId + ", "
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
	@RequestMapping(value ={"/planReview/Results/"}, params={"task=downloadSelectedPdf"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownloadSelectedPdf (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownloadSelectedPdf");
		}
		
		HttpSession session = request.getSession(false);
    	session.setAttribute(IS_DOWNLOAD_SELECTED_PDF_GENERATED, false);
		
		

		List<PlanReviewReportUIHolder> selectedRecordList = Collections.unmodifiableList(form
				.getContractResultVOList());

		List<PlanReviewReportUIHolder> downloadPlanReviewReportPdfs = new ArrayList<PlanReviewReportUIHolder>();
		List<PlanReviewReportUIHolder> downloadExeSummaryPdfs = new ArrayList<PlanReviewReportUIHolder>();
		
		ByteArrayOutputStream zipOutputStream = null;
		
		String fileServerPath = PlanReviewRequestProperties
				.get(PlanReviewRequestProperties.FILE_SERVER_PATH)
				+ PlanReviewRequestProperties
						.get(PlanReviewRequestProperties.PREVIEW_IMAGES_FOLDER);
		
		try {
			
			Map<String, File> filemap = new HashMap<String, File>();
			
			// Block to trigger Download PDF for Plan Review Reports
			// Replace with Plan Review Reports specific webservice Logic
			if (form.isAllPlanReviewReportPdfsSelected()) {
				
				for (PlanReviewReportUIHolder planReviewReportUIHolder : selectedRecordList) {
					if (StringUtils.equalsIgnoreCase(STATUS_COMPLETED, planReviewReportUIHolder.getStatus())) {
						
						downloadPlanReviewReportPdfs
						.add(planReviewReportUIHolder);
					}
				}
				
			} else {
				for (PlanReviewReportUIHolder planReviewReportUIHolder : selectedRecordList) {
					if (planReviewReportUIHolder
							.isPlanReviewReportPdfsSelected()) {
						
						downloadPlanReviewReportPdfs
								.add(planReviewReportUIHolder);
					}
				}
			}
			
			if (downloadPlanReviewReportPdfs != null || !downloadPlanReviewReportPdfs.isEmpty()) {
				
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
			if (form.isAllExeSummaryPdfsSelected()) {
				for (PlanReviewReportUIHolder planReviewReportUIHolder : selectedRecordList) {
					if (StringUtils.equalsIgnoreCase(STATUS_COMPLETED, planReviewReportUIHolder.getStatus())) {
						
						downloadExeSummaryPdfs
						.add(planReviewReportUIHolder);
					}
				}
				
			} else {

				for (PlanReviewReportUIHolder planReviewReportVO : selectedRecordList) {
					if (planReviewReportVO.isExeSummaryPdfsSelected()) {
						
						downloadExeSummaryPdfs.add(planReviewReportVO);
					}
				}
			}
			
			// Trigger Webservice call and replace below PDF files
			// with PDF files received in Webservice response
			if (downloadExeSummaryPdfs != null || !downloadExeSummaryPdfs.isEmpty()) {
				
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
							+ form
									.getPlanReviewRequestId());
			
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
	@RequestMapping(value = {"/planReview/Results/"},params={"task=print"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrint() in PlanReviewReportResultsAction");
		}
		
		
		
		HttpSession session = request.getSession(false);
		
		session.setAttribute(
				BDConstants.PLAN_REVIEW_REQUEST_ID, form.getPlanReviewRequestId());
		
		super.setRegularPageNavigation(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doPrint() in PlanReviewReportResultsAction.");
		}
		return forwards.get(getTask(request));
	}

	/**
	 * This method will redirect to the History Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = {"/planReview/Results/"}, params={"task=history"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistory (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistory() in PlanReviewReportResultsAction");
		}

		super.setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doHistory() in PlanReviewReportResultsAction.");
		}
		return forwards.get(getTask(request));
	}

	/**
	 * This method will redirect to the History Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = {"/planReview/Results/"}, params={"task=planReviewRequest"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPlanReviewRequest (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistory() in PlanReviewReportResultsAction");
		}

		HttpSession session = request.getSession(false);

		session.setAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT, Boolean.TRUE);
		
		super.setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doHistory() in PlanReviewReportResultsAction.");
		}
		return forwards.get( getTask(request));
	}
	@RequestMapping(value ={"/planReview/Results/"}, params={"task=backToMainHistory"} , method =  {RequestMethod.POST}) 
	public String doBackToMainHistory (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
		
	}
	@RequestMapping(value={"/planReview/Results/"}, params={"action=navigatedFromResults"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doNavigatedFromResults (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
		
	}
	@RequestMapping(value={"/planReview/Results/"}, params={"task=openPdfWindow"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doOpenPdfWindow (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {	
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
	
		
		String contractId = request.getParameter("contract");
		String viewPdfRequestType = request.getParameter("type");
		//TODO now i set the value of planReviewLevel to bob , need to take care 
		
		
		request.setAttribute("contract", contractId);
		request.setAttribute("type", viewPdfRequestType);
		//TODO: To be reviewed
		request.setAttribute("planReviewLevel", "contract");
		
		
		setRegularPageNavigation(request);
		return forwards.get(getTask(request));
		
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
			populateStatusDetails(activityVo
					.getStatusCode(), planReviewReportUIHolder);
			
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

	private void populateStatusDetails(ActivitySatusCode activityStatusCode,
			PlanReviewReportUIHolder planReviewReportUIHolder) {

		String status = StringUtils.EMPTY;
		String statusImage = StringUtils.EMPTY;

		if (ActivitySatusCode.OK.equals(activityStatusCode)) {
			status = STATUS_COMPLETED;
			statusImage = STATUS_COMPLETED_IMAGE;
		} else if (ActivitySatusCode.FAILED.equals(activityStatusCode)) {
			status = STATUS_IN_COMPLETE;
			statusImage = STATUS_IN_COMPLETE_IMAGE;
			planReviewReportUIHolder.setStatusImageTitle("This request is incomplete. Our support team has been notified and is working to resolve the issue. For support, please email jhplanreview@jhancock.com or call 1-877-346-8378.");
		} else if (ActivitySatusCode.RESUBMITTED.equals(activityStatusCode)) {
			status = STATUS_RESUBMITTED;
			statusImage = STATUS_RESUBMITTED_IMAGE;
			planReviewReportUIHolder.setStatusImageTitle("The plan review request for this contract has been resubmitted.  The new request can be found in your history.");
		} else if (ActivitySatusCode.PENDING.equals(activityStatusCode)){
			status = STATUS_IN_PROGRESS;
			statusImage = STATUS_IN_PROGRESS_IMAGE;
		}

		planReviewReportUIHolder.setStatus(status);
		planReviewReportUIHolder.setStatusImage(statusImage);

	}
	
	/** This method will check whether Plan Review Report or executive summary is generated or not.
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
	@RequestMapping(value = {"/planReview/Results/"}, params={"task=checkDownloadPdfGenerated"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCheckDownloadPdfGenerated (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
    
    /** This method will check whether Plan Review Report or executive summary is generated or not.
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
	
	@RequestMapping(value = {"/planReview/Results/"}, params={"task=checkDownloadSelectedPdfGenerated"} , method =  {RequestMethod.GET}) 
	public String doCheckDownloadSelectedPdfGenerated (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
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
	 
	 
	@RequestMapping(value = {"/planReview/Results/"},params={"task=filter"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
			}
		}
		 forward=super.doFilter( form, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value = {"/planReview/Results/"} ,params={"task=page"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("planReviewResultForm") PlanReviewResultForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
			}
		}
		 forward=super.doPage( form, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
    /**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	   private BDValidatorFWDefault  bdValidatorFWDefault;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWDefault);
	}
}