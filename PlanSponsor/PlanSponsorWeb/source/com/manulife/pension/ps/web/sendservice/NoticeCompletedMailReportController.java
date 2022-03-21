/**
 * 
 */
package com.manulife.pension.ps.web.sendservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.NoticeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.sendservice.valueobject.SendServiceReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.service.common.enumeration.NoticeEnums;
import com.manulife.pension.service.common.exception.ErrorMessages;
import com.manulife.pension.service.common.valueobject.ReportCriteria;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.request.valueobject.NoticeRequest;
import com.manulife.pension.service.request.valueobject.NoticeRequestHistory;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * @author krishta
 *
 */
@Controller
@RequestMapping(value ="/sendservice")

public class NoticeCompletedMailReportController extends ReportController {
	@ModelAttribute("noticeCompletedMailReportForm")
	public NoticeCompletedMailReportForm populateForm() 
	{
		return new NoticeCompletedMailReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/sendservice/noticecompletedmailreport.jsp");
		forwards.put("default", "/sendservice/noticecompletedmailreport.jsp");
		forwards.put("active", "/do/sendservice/activeMail/");
		forwards.put("planDataView", "/do/sendservice/planData/");
	}
	
	
	public static final String DEFAULT_SORT_FIELD = NoticeRequest.SORT_FIELD_APPL_DATE;
	public static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
    public static final String HOMEPAGE="secureHomePage";
	private static final String PDF_GENERATED = "pdfGenerated";
    private static final String PDF_NOT_GENERATED = "pdfNotGenerated";
    private static final String GENERATED_PDF ="isReportGenerated";
    private static final String  CSV_GENERATED = "csvGenerated";
    private static final String CSV_NOT_GENERATED = "csvNotGenerated";
    private static final String GENERATED_CSV ="isCsvReportGenerated";
    public static final String DEFAULT = "default";
	@Override
	public String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}


	@Override
	public String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}


	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BaseAutoAction#doDefault(org.apache.struts.action.ActionMapping, com.manulife.pension.platform.web.controller.BaseReportForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	 
	public String doCommon(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {
		NoticeCompletedMailReportForm completedMailReportForm = (NoticeCompletedMailReportForm)reportForm;
		
		UserProfile userProfile = getUserProfile(request);
		if(!userProfile.isSendServiceAccessible()) {
		   return forwards.get(HOMEPAGE);
		}
		NoticeServiceDelegate noticeServiceDelegate = NoticeServiceDelegate.getInstance();
		ReportCriteria reportCriteria = new ReportCriteria("");
		SendServiceReportData reportData=new SendServiceReportData(reportCriteria,0);
		populateReportForm( reportForm, request);
		populateReportCriteria(reportCriteria, reportForm, request);
		populateSortCriteria(reportCriteria, reportForm);
		List<NoticeRequest> completedMailList = noticeServiceDelegate.getCompletedMailNotice(reportCriteria);
		if(completedMailList!=null){
			reportData.setTotalCount(completedMailList.size());
			completedMailReportForm.setNoticeRequestList(page(completedMailList, reportCriteria));
		}
		request.setAttribute(Constants.REPORT_BEAN, reportData);
		return forwards.get("input");
	}
	
	@RequestMapping(value ="/completedMail/",  method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doRefresh");
        }
        //request.getSession().setAttribute("actionForm", form);
        String forward = super.doDefault(form, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doRefresh");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	@RequestMapping(value ="/completedMail/",params={"task=sort"}, method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSort( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
	@RequestMapping(value ="/completedMail/" ,params={"task=updateNotice"}, method =  {RequestMethod.POST}) 
	public String doUpdateNotice (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		NoticeServiceDelegate noticeServiceDelegate = 	NoticeServiceDelegate.getInstance();
		
		Integer trackingNumber = form.getSelectedTrackingNumber();
		UserProfile userProfile = getUserProfile(request);
		NoticeRequest noticeRequest  = noticeServiceDelegate.getNoticeByOrderNo(trackingNumber);
		noticeRequest.setOrderNo(0);
		noticeRequest.setOriginalOrderNumber(trackingNumber);
		noticeRequest.setNoticeStatus("UP");
		noticeRequest.setUpdatedProfileId(userProfile.getPrincipal().getProfileId());
		noticeRequest.setNoticeFileName(StringUtils.EMPTY);
		noticeRequest.setNoticeControlFileName(StringUtils.EMPTY);
		noticeRequest.setMerrillStatusFileName(StringUtils.EMPTY);
		NoticeRequestHistory noticeRequestHistory = new NoticeRequestHistory();
		noticeRequestHistory.setNoticeStatusCode("UP");
		noticeRequestHistory.setAdvanceNotificationStatusCode(NoticeEnums.AdvancedNotificationStatus.NOT_REQUIRED.getStatusCode());
		//TODO Need to pass the regeneration code
		noticeRequestHistory.setNoticeStatusReasonCode("UPDATE");
		noticeRequestHistory.setCreatedProfileId(userProfile.getPrincipal().getProfileId());
		noticeRequest.setNoticeRequestHistory(noticeRequestHistory);
		noticeServiceDelegate.insertNoticeRequestDetails(noticeRequest);
		return doCommon( form, request, response);
	}
	
	@RequestMapping(value ="/completedMail/",params={"task=previewNoticeDocument"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPreviewNoticeDocument (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		List<GenericException> errors = new ArrayList<GenericException>();
		NoticeServiceDelegate noticeServiceDelegate = 	NoticeServiceDelegate.getInstance();
		
		Integer trackingNumber = form.getSelectedTrackingNumber();
		try{
		if(trackingNumber!=null){
			NoticeRequest noticeRequest  = noticeServiceDelegate.getNoticeByOrderNo(trackingNumber);
			boolean isPreviewNotice =  false;
				String fileFormat = "pdf";
				StringBuffer documentfileName = prepareDownloadFileName(
						noticeRequest, fileFormat,isPreviewNotice);
			noticeRequest = noticeServiceDelegate.generateNoticeForPreviewPage(noticeRequest, true,isPreviewNotice);
			byte[] pdfData = noticeRequest.getNoticeDocumentVo().getPdfContent();
			response.getWriter().write(PDF_GENERATED);
			request.getSession(false).setAttribute(GENERATED_PDF, new Handle(pdfData,documentfileName.toString()));
			//session.setAttribute(DOCUMENT_FILE_NAME, documentName);
		}
		}
		catch(SystemException e){
			if(getDataError().contains(Integer.valueOf(e.getMessage())))
			{
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());
			errors.add(new ValidationError("Data Issue"               //DataIssue//
									, ErrorCodes.ERROR_DATA_ISSUE,
									Type.error));
			}
			if(getTechnicalError().contains(Integer.valueOf(e.getMessage())))
			{
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());		
				errors.add(new ValidationError("Technical Issue"  //TechnicalIssue//
									, ErrorCodes.ERROR_TECHNICAL_ISSUE,
									Type.error));
			}
		}
	if(errors.size()>0)
	{
		setErrorsInSession(request, errors);
	}
		
		return null;
	}
	@RequestMapping(value ="/completedMail/",params={"task=previewEmployeeList"},method ={RequestMethod.POST,RequestMethod.GET}) 
	public String doPreviewEmployeeList (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException, SystemException {

		List<GenericException> errors = new ArrayList<GenericException>();
		NoticeServiceDelegate noticeServiceDelegate = NoticeServiceDelegate.getInstance();

		Integer trackingNumber = form.getSelectedTrackingNumber();
		try {
			if (trackingNumber != null) {
				NoticeRequest noticeRequest = noticeServiceDelegate.getNoticeByOrderNo(trackingNumber);
				String fileFormat = "csv";
				boolean isPreviewNotice = false;
				StringBuffer documentfileName = prepareDownloadFileNameCsv(noticeRequest, fileFormat, isPreviewNotice);
				noticeRequest = noticeServiceDelegate.generateNoticeForPreviewPage(noticeRequest, false,
						isPreviewNotice);
				if (noticeRequest != null && noticeRequest.getNoticeDocumentVo() != null) {
					byte[] csvContent = noticeRequest.getNoticeDocumentVo().getCsvContent();
					response.getWriter().write(CSV_GENERATED);
					request.getSession(false).setAttribute(GENERATED_CSV,
							new Handle(csvContent, documentfileName.toString()));
					System.out.println("csvContent:" + csvContent);
				}

			}
		} catch (SystemException e) {
			if (getDataError().contains(Integer.valueOf(e.getMessage()))) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());
				errors.add(new ValidationError("Data Issue" // DataIssue//
						, ErrorCodes.ERROR_DATA_ISSUE, Type.error));
			}
			if (getTechnicalError().contains(Integer.valueOf(e.getMessage()))) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());
				errors.add(new ValidationError("Technical Issue" // TechnicalIssue//
						, ErrorCodes.ERROR_TECHNICAL_ISSUE, Type.error));
			}
		}
		if (errors.size() > 0) {
			setErrorsInSession(request, errors);
		}
		return null;
	}
	

	/**
	 * @param noticeRequest
	 * @param fileFormat
	 * @return
	 */
	private StringBuffer prepareDownloadFileName(NoticeRequest noticeRequest,
			String fileFormat,Boolean isPreviewNotice) {
		StringBuffer documentfileName = new StringBuffer();
		//TODO format of CSV <CN#>_<Notice>_<MM_DD_YYYY>.csv
		documentfileName.append(noticeRequest.getContractId());
		documentfileName.append("_");
		documentfileName.append(noticeRequest.getNoticeTypeCode());//TODO need to add describition
		documentfileName.append("_");
		if(isPreviewNotice){
			documentfileName.append(getFormattedDateForFileName(new Date()));
		}
		else{
			documentfileName.append(getFormattedDateForFileName(noticeRequest.getNoticeRequestHistory().getNoticeStatusDate()));
		}
		documentfileName.append(".");
		documentfileName.append(fileFormat);//TODO Apply the date format
		return documentfileName;
	}
	/**
	 * @param noticeRequest
	 * @param fileFormatCsv
	 * @return
	 */
	private StringBuffer prepareDownloadFileNameCsv(NoticeRequest noticeRequest,
			String fileFormat,Boolean isPreviewNotice) {
		StringBuffer documentfileName = new StringBuffer();
		documentfileName.append(noticeRequest.getContractId());
		documentfileName.append("_");
		documentfileName.append("EF");
		documentfileName.append("_");
		documentfileName.append(noticeRequest.getNoticeTypeCode());//TODO need to add describition
		documentfileName.append("_");
		if(isPreviewNotice){
			documentfileName.append(getFormattedDateForFileName(new Date()));
		}
		else{
			documentfileName.append(getFormattedDateForFileName(noticeRequest.getNoticeRequestHistory().getNoticeStatusDate()));
		}
		documentfileName.append(".");
		documentfileName.append(fileFormat);//TODO Apply the date format
		return documentfileName;
	}

	@Override
	protected String getReportId() {
		//Exact report framework is not used here and not using the report id
		return null;
	}

	@Override
	protected String getReportName() {
		//Exact report framework is not used here and not using the report id
		return null;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		//Exact report framework is not used here and not using the report id
		return null;
	}

	@Override
	protected void populateReportCriteria(
			com.manulife.pension.service.report.valueobject.ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		//Exact report framework is not used here and not using the report id
		
	}
	
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {


		UserProfile userProfile = getUserProfile(request);

		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(NoticeRequest.FILTER_CONTRACT_NUMBER,
				new Integer(currentContract.getContractNumber()));
		
		criteria.addFilter(NoticeRequest.FILTER_FIELD_SELECTION_IND,
				"Y");
		criteria.setPageNumber(form.getPageNumber());
		criteria.setPageSize(getPageSize(request));
	}
		
	
	 protected void populateSortCriteria(ReportCriteria criteria,
	            BaseReportForm form) {
		 String sortField = form.getSortField();
			String sortDirection = form.getSortDirection();

			criteria.insertSort(sortField, sortDirection);
			if (NoticeRequest.SORT_FIELD_APPL_DATE
					.equals(sortField)) {
				criteria.insertSort(NoticeRequest.SORT_FIELD_STATUS,
						sortDirection);
				criteria.insertSort(NoticeRequest.SORT_FIELD_STATUS_DATE,
						sortDirection);
			} else if (NoticeRequest.SORT_FIELD_STATUS
					.equals(sortField)) {
				criteria.insertSort(NoticeRequest.SORT_FIELD_APPL_DATE,
						sortDirection);
				criteria.insertSort(NoticeRequest.SORT_FIELD_STATUS_DATE,
						sortDirection);
			}
			 else if (NoticeRequest.SORT_FIELD_STATUS_DATE
						.equals(sortField)) {
					criteria.insertSort(NoticeRequest.SORT_FIELD_APPL_DATE,
							sortDirection);
					criteria.insertSort(NoticeRequest.SORT_FIELD_STATUS,
							sortDirection);
				}
		 
	 }
	 
	 protected void populateReportForm(
	            BaseReportForm reportForm, HttpServletRequest request) {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> populateReportForm");
	        }

	        String task = getTask(request);

	        /*
	         * Set default sort if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
	                || reportForm.getSortDirection().length() == 0) {
	            reportForm.setSortDirection(getDefaultSortDirection());
	        }

	        /*
	         * Set default sort direction if we're in default task.
	         */
	        if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
	                || reportForm.getSortField().length() == 0) {
	            reportForm.setSortField(getDefaultSort());
	        }

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <- populateReportForm");
	        }
	    }

	 
	 /**
		 * This method handles fetching of the individual statements from the service and open them in PDF format 
		 * as attachment in the defined filename
		 * 
		 * @param mapping
		 * @param actionForm
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 * @throws SystemException
		 */
	 @RequestMapping(value ="/completedMail/",params={"task=fetchPdf"},method ={RequestMethod.GET}) 
	 public String doFetchPdf (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	 throws IOException,ServletException, SystemException {

			
			HttpSession session = request.getSession(false);
			byte [] download = null;
			String downloadFileName = null;
			if(session.getAttribute(GENERATED_PDF) != null) {
				download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
				downloadFileName = ((Handle)session.getAttribute(GENERATED_PDF)).getDocumentFileName();
				session.setAttribute(GENERATED_PDF, null);
			}else {
				doPreviewNoticeDocument(form, bindingResult, request, response);
				download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
				downloadFileName = ((Handle)session.getAttribute(GENERATED_PDF)).getDocumentFileName();
				session.setAttribute(GENERATED_PDF, null);
			}
			if (download != null && download.length > 0) {
				BaseReportController.streamDownloadData(request, response,
						"application/pdf", downloadFileName, download);
			}	
			
			
			return null;
		}
		

		/**
		 * This method handles fetching of the individual statements from the service and open them in PDF format 
		 * as attachment in the defined filename
		 * 
		 * @param mapping
		 * @param actionForm
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 * @throws SystemException
		 */
		@RequestMapping(value ="/completedMail/",params={"task=fetchCSV"}, method = {RequestMethod.GET}) 
		public String doFetchCSV (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {

			
			HttpSession session = request.getSession(false);
			byte [] download = null;
			String downloadFileName = null;
			if(session.getAttribute(GENERATED_CSV) != null) {
				download = ((Handle)session.getAttribute(GENERATED_CSV)).getByteArray();
				downloadFileName = ((Handle)session.getAttribute(GENERATED_CSV)).getDocumentFileName();
				session.setAttribute(GENERATED_CSV, null);
			}else {
				doPreviewEmployeeList( form, bindingResult, request, response);
				download = ((Handle)session.getAttribute(GENERATED_CSV)).getByteArray();
				downloadFileName = ((Handle)session.getAttribute(GENERATED_CSV)).getDocumentFileName();
				session.setAttribute(GENERATED_CSV, null);
			}
			if (download != null && download.length > 0) {

				BaseReportController.streamDownloadData(request, response,
						getContentType(),downloadFileName, download);
			}	
			
			
			return null;
		}
		
		
		/**
		 * Class to handle the byte array object in session scope
		 */
		public static class Handle implements Serializable{
			private static final long serialVersionUID = 1L;

			Handle(byte[] array){
				this.byteArray = array;
			}
			Handle(byte[] array,String documentFileName ){
				this.byteArray = array;
				this.documentFileName = documentFileName;
			}
			private byte[] byteArray;
			private String documentFileName;
			/**
			 * @return the array
			 */
			public byte[] getByteArray() {
				return byteArray;
			}
			/**
			 * @return the documentFileName
			 */
			public String getDocumentFileName() {
				return documentFileName;
			}
			
			
		}
		
		public List<Integer> getDataError()
		{
			Map<Integer, List<Integer>> dataErrorHashMap = new HashMap<Integer, List<Integer>>();
			List<Integer> dataError = new ArrayList<Integer>(Arrays.asList(
					ErrorMessages.Error.MISSING_FIELD.getCode(),
					ErrorMessages.Error.UN_UPDATE_KEYWORD.getCode(),
					ErrorMessages.Error.UN_FETCH_DATA.getCode(),//tech or data
					ErrorMessages.Error.UN_PARSE_DATE.getCode(),
					ErrorMessages.Error.UN_FETCH_RETURN_ADDRESS.getCode(),//tech or data
					ErrorMessages.Error.UN_FETCH_PARTICIPANT_DETAILS.getCode(),//tech or data
					ErrorMessages.Error.UN_GENERATE_404_DOC.getCode(),//tech or data
					ErrorMessages.Error.UN_CONV_PRINT_REQ_TO_XML.getCode(),
					ErrorMessages.Error.UN_CONV_MAILLIST_TO_XML.getCode(),
					ErrorMessages.Error.INCORRECT_TIME.getCode(),
					ErrorMessages.Error.NOTICEFIEL_OR_ADDRESSFILE_OR_PRINTORDERFILE_NOTNULL.getCode(),//need to confirm
					ErrorMessages.Error.UN_CREATE_ADDRESSDETAILS.getCode(),//need to confirm
					ErrorMessages.Error.INITSTATUS_NO_ORDERS.getCode(),//need to confirm
					ErrorMessages.Error.EREPORTS_NOTICEPDF_OR_ADDRESSXML_OR_CONTRACTXML_BLANK.getCode(),//need to confirm
					ErrorMessages.Error.EREPORTS_ADDRESSXML_BLANK.getCode(),//need to confirm
					ErrorMessages.Error.EREPORTS_CONTRACTXML_BLANK.getCode(),//need to confirm
					ErrorMessages.Error.ZIP_FAILED.getCode(),//need to confirm
					ErrorMessages.Error.UN_INITIALIZE_PARSER.getCode(),//data
					ErrorMessages.Error.UN_READ_INPUT_STREAM.getCode(),//data
					ErrorMessages.Error.UN_PARSE_XML_STREAM.getCode(),//data
					ErrorMessages.Error.DOCUMENT_NOT_AVAIL.getCode(),//data
					ErrorMessages.Error.EREPORT_CONTROLFILE_SAVE_FAILED.getCode()));//need to confirm
			dataErrorHashMap.put(3140,dataError);//data issue
			dataError=dataErrorHashMap.get(3140);
			return dataError;
		}
		
		public List<Integer> getTechnicalError()
		{
			Map<Integer, List<Integer>> technicalErrorHashMap = new HashMap<Integer, List<Integer>>();
			List<Integer> technicalError = new ArrayList<Integer>(Arrays.asList(
					ErrorMessages.Error.TEMPLATE_NOT_AVAIL.getCode(),
					ErrorMessages.Error.EREPORTS_REMOTE_ERROR.getCode()
					,ErrorMessages.Error.EREPORTS_CALL_FAILED.getCode(),
					ErrorMessages.Error.EREPORT_DOC_SAVE_FAILED.getCode(),
					ErrorMessages.Error.CONTROLFILE_GEN_FAILED.getCode(),
					ErrorMessages.Error.FILENAME_CREATION_FAIL.getCode(),//need to confirm
					ErrorMessages.Error.UN_UPDATE_ADV_NOTIFICATION_STATUS.getCode(),//data or tech
					ErrorMessages.Error.UN_UPDATE_NOTICE_STATUS.getCode(),//data or tech
					ErrorMessages.Error.UN_INSERT_NOTICE_REQ_DETAILS.getCode(),//data or tech
					ErrorMessages.Error.UN_UPDATE_NOTICE_FILE_DETAILS.getCode(),//data or tech
					ErrorMessages.Error.UN_UPDATE_NOTICE_ERROR.getCode(),//data or tech
					ErrorMessages.Error.INVALID_ACTION.getCode(),
					ErrorMessages.Error.GENERAL_DOCUMENT_EXCEPTION.getCode(),
					ErrorMessages.Error.WRONG_FILEFORMAT_NOTICEFILE.getCode(),//need to confirm
					ErrorMessages.Error.WRONG_FILEFORMAT_ADDRESSFILE.getCode(),//need to confirm
					ErrorMessages.Error.WRONG_FILEFORMAT_PRINTORDERFILE.getCode(),//need to confirm
					ErrorMessages.Error.UN_GET_CONVERSION_TRIGGER_NOTICE_STATUS.getCode(),
					ErrorMessages.Error.PBM_GETTING_CONTRACT_SERVICE_FEATURE.getCode(),//tech or data
					ErrorMessages.Error.ADDRESS_FILE_NULL.getCode(),
					ErrorMessages.Error.PRINT_ORDER_FILE_NULL.getCode(),
					ErrorMessages.Error.EREPORTS_FAILED.getCode(),
					ErrorMessages.Error.NOTICE_FILE_NULL.getCode(),
					ErrorMessages.Error.NOTICE_FILE_NOT_IN_ARCHIVE.getCode(),//need to confirm
					ErrorMessages.Error.CONTROL_FILE_NOT_IN_ARCHIVE.getCode(),//need to confirm
					ErrorMessages.Error.PDF_NOT_AVAIL.getCode(),
					ErrorMessages.Error.FILE_ERROR_MERRILL_STATUS.getCode()));
			technicalErrorHashMap.put(3141,technicalError);
			technicalError=technicalErrorHashMap.get(3141);
			return technicalError;
		}
		@RequestMapping(value ="/completedMail/", params={"task=errorReport"}, method =  {RequestMethod.GET}) 
		public String doErrorReport (@Valid @ModelAttribute("noticeCompletedMailReportForm") NoticeCompletedMailReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
			doCommon( form, request, response);
			return forwards.get(DEFAULT);
			
		}
		private static List<NoticeRequest> page(List<NoticeRequest> items, ReportCriteria criteria) {
			List<NoticeRequest> pageDetails = new ArrayList<NoticeRequest>();
			if (items != null) {
				if (criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
					pageDetails.addAll(items);
				} else {
					for (int i = criteria.getStartIndex() - 1; i < criteria
							.getPageNumber() * criteria.getPageSize()
							&& i < items.size(); i++) {
						pageDetails.add(items.get(i));
					}
				}
			}
			return pageDetails;
		}
		
		private String getFormattedDateForFileName(Date date) {
			DateFormat formatMonth = new SimpleDateFormat("MM");
			DateFormat formatDay = new SimpleDateFormat("dd");
			DateFormat formatYear = new SimpleDateFormat("yyyy");

			String fullDate = formatMonth.format(date) + "_"
					+ formatDay.format(date) + "_" + formatYear.format(date);

			return fullDate;
		}

}
