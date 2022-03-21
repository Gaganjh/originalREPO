
package com.manulife.pension.ps.web.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabReportData;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUHistoryTabVO;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.SDUHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.awd.util.AwdHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.PayrollAdministrator;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;

/**
 * Action class for populating the submission history page.
 *
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value="/tools/secureDocumentUpload")
@SessionAttributes({"sduHistoryTabForm"})

public class SDUHistoryTabController extends ReportController {

	@ModelAttribute("sduHistoryTabForm")
	public SDUHistoryTabForm populateForm() {
		return new SDUHistoryTabForm();
	}	

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/tools/sduHistoryTab.jsp");
		forwards.put("sort","/tools/sduHistoryTab.jsp");
		forwards.put("filter","/tools/sduHistoryTab.jsp");
		forwards.put("page","/tools/sduHistoryTab.jsp");
		forwards.put("print","/tools/sduHistoryTab.jsp");
		forwards.put("input","/tools/sduHistoryTab.jsp");
	}
	
    private static final Map<String,String> fieldToColumnMap = new HashMap<String,String>();
    static {
        fieldToColumnMap.put("submissionTs", "submissionTs");
        fieldToColumnMap.put("submissionId", "submissionId");
        fieldToColumnMap.put("fileName", "fileName");
        fieldToColumnMap.put("clientUserName", "clientUserName");
        fieldToColumnMap.put("clientUserRole", "clientUserRole");
        fieldToColumnMap.put("submissionStatus", "submissionStatus");
    }

    private BaseEnvironment baseEnvironment= new BaseEnvironment();    
	private static final String DEFAULT_SORT_FIELD = SDUHistoryTabReportData.SORT_SUBMISSION_DATE;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	public static final String JUST_MINE_FILTER = "justMineFilter";
	public static final String JUST_MINE = "justMine";
	public static final String HISTORY_RESULTS="reportBean";
	private static final String DOWNLOAD_COLUMN_HEADING_SUBMISSIONS = "Submission #,Submission date/time,Document name,Submitted by,Role,Upload status";
	private static final String RECEIVED_DATE_MASK = "MM/dd/yyyy hh:mm:ssa";
	private static final String SDU_HISTORY_TAB_FORM="sduHistoryTabForm";	


	/**
	 * constructor
	 */
	public SDUHistoryTabController() {
		super(SDUHistoryTabController.class);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return SDUHistoryTabReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return SDUHistoryTabReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	
	/**
	 * This method is called to populate a report criteria from the report action
	 * form and the request. It is called right before getReportData is called.
	 */
	protected void populateReportCriteria(ReportCriteria reportCriteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		SDUHistoryTabForm sduHistoryTabForm = (SDUHistoryTabForm) form;
		
		// get the user profile object
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_CONTRACT_NO, currentContract.getContractNumber());		
		reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_START_DATE, sduHistoryTabForm.getFilterStartDate());
		reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_END_DATE, sduHistoryTabForm.getFilterEndDate());
		reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_SDU_CLIENT_ID,SDUConstants.SDU_PS_CLIENT_ID);
		reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_SUBMISSION_ID, 0l);
		
		if (sduHistoryTabForm.isJustMine()) {
			reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_USER_ID,
					String.valueOf(userProfile.getPrincipal().getProfileId()));			
		}
		else {
			reportCriteria.addFilter(SDUHistoryTabReportData.FILTER_USER_ID,"");
		}
		
		if (SDUHistoryTabForm.isFieldSet(sduHistoryTabForm.getPageNumber())) {
			reportCriteria.setPageNumber(sduHistoryTabForm.getPageNumber());
		}			
		//populateSortCriteria(reportCriteria, form);
	}

	/**
	 * This is the method to be extended for validation.
	 *
	 * @param mapping
	 *            TODO
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 *
	 * @return Error Collection
	 */
	@SuppressWarnings("rawtypes")
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {

		SDUHistoryTabForm sduHistoryTabForm = (SDUHistoryTabForm) form;
		Collection errors = new ArrayList();

		Date fromDate = null;
		Date toDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);

		// validating the fields based on the task 
		if (FILTER_TASK.equals(request.getParameter(TASK_KEY)) 
				|| PRINT_TASK.equals(request.getParameter(TASK_KEY)) 
				|| DOWNLOAD_TASK.equals(request.getParameter(TASK_KEY))) {
			
			try {

				
				if (StringUtils.isNotEmpty(sduHistoryTabForm.getFilterStartDate())) {
					fromDate = simpleDateFormat.parse(sduHistoryTabForm.getFilterStartDate());
					
					//Validating the from date for the below scenario
					//scenario 1:if date is enter as 13/01/2010 
					//scenario 2:if date is enter as 4/31/2010 
					//scenario 3:if date is enter as 06/30/20J0 
					//scenario 4:if date is enter as 01/00/2010
					//scenario 5:if date is enter as 01/01/0000
					//for all those scenario's ,throw parse exception with message as "Invalid Date"
					if (! simpleDateFormat.format(fromDate).equals(sduHistoryTabForm.getFilterStartDate())) {
						throw new ParseException("Invalid Date", 0);
					}
					
				} 
				else {
					// from date is blank so add error
					errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
				}
				
				if (StringUtils.isNotEmpty(sduHistoryTabForm.getFilterEndDate())) {
					toDate = simpleDateFormat.parse(sduHistoryTabForm.getFilterEndDate());
					//Validating the from and to dates  for the below scenario
					//scenario 1:if date is enter as 13/01/2010 
					//scenario 2:if date is enter as 4/31/2010 
					//scenario 3:if date is enter as 06/30/20J0 
					//scenario 4:if date is enter as 01/00/2010
					//scenario 5:if date is enter as 01/01/0000
					//for all those scenario's ,throw parse exception with message as "Invalid Date"
					if ( ! simpleDateFormat.format(toDate).equals(sduHistoryTabForm.getFilterEndDate())) {
						throw new ParseException("Invalid Date", 0);
					}
					
				}
				else {
					// To date is blank so add error
					errors.add(new GenericException(ErrorCodes.TO_DATE_EMPTY));
				}
				
				//from date before 24 months 
				if ((!StringUtils.isEmpty(sduHistoryTabForm.getFilterStartDate())) && fromDate != null) {
					Calendar calEarliestDate = Calendar.getInstance();
					calEarliestDate.add(Calendar.MONTH, -24);
					Calendar calFromDate = Calendar.getInstance();

					calFromDate.setTime(fromDate);

					if (calFromDate.before(calEarliestDate)) {
						errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
						// validFromDateRange = false;
					}
				}

				// validate the start and end dates
				if (fromDate != null && toDate != null && fromDate.after(toDate)) {
					errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
				}
				
				Date today = new Date();
				// enddate is after today
				if (toDate != null && toDate.after(today)) {
					errors.add(new GenericException(ErrorCodes.TO_DATE_AFTER_TODAY));
				}

			} catch(ParseException pe) {
				// From date or To date is invalid
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
			} 
		}

		if (!errors.isEmpty()) {
			SDUHistoryTabReportData emptyReport = new SDUHistoryTabReportData(null, 0);			
			emptyReport.setDetails(new ArrayList());
			emptyReport.setReportCriteria( new ReportCriteria(null));
			request.setAttribute(Constants.REPORT_BEAN, emptyReport);
		}
		return errors;
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		
		if (form.getSortField() != null) {			
			//Insert Primary sort
			criteria.insertSort(form.getSortField(), form.getSortDirection());
			
			// if sorting by type, name or status, secondary sort will be desc submission
			// number
			if (form.getSortField().equals(SDUHistoryTabReportData.SORT_SUBMISSION_ID)) {				
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_DATE, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.ASC_DIRECTION);	
			} 
			else if (form.getSortField().equals(SDUHistoryTabReportData.SORT_SUBMISSION_DATE)){
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_ID, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.ASC_DIRECTION);		
			}
			else if (form.getSortField().equals(SDUHistoryTabReportData.SORT_DOCUMENT_NAME)){
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_DATE, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_ID, ReportSort.DESC_DIRECTION);		
			}
			else if (form.getSortField().equals(SDUHistoryTabReportData.SORT_SUBMITTER_NAME)){
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_DATE, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.ASC_DIRECTION);		
			}
			else if (form.getSortField().equals(SDUHistoryTabReportData.SORT_SUBMITTER_ROLE)){
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_DATE, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.ASC_DIRECTION);		
			}
			else if (form.getSortField().equals(SDUHistoryTabReportData.SORT_SUBMISSION_STATUS)){
				//Insert Secondary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_DATE, ReportSort.DESC_DIRECTION);
				//Insert Tertiary sort
				criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.ASC_DIRECTION);		
			}
		}
		else {
			//Insert Primary sort
			criteria.insertSort(getDefaultSort(), getDefaultSortDirection());	
			//Insert Secondary sort
			criteria.insertSort(SDUHistoryTabReportData.SORT_SUBMISSION_ID, ReportSort.DESC_DIRECTION);
			//Insert Tertiary sort
			criteria.insertSort(SDUHistoryTabReportData.SORT_DOCUMENT_NAME, ReportSort.DESC_DIRECTION);
			form.setSortField(DEFAULT_SORT_FIELD);
			form.setSortDirection(DEFAULT_SORT_DIRECTION);
		}
	}

	public String doCommon(BaseReportForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		FunctionalLogger.INSTANCE.log("Submission Document History", getUserProfile(request), getClass(),
				getMethodName(actionForm, request));

		String result =  super.doCommon(actionForm, request, response);
		return result;
	}

	
	@RequestMapping(value ="/history/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("sduHistoryTabForm") SDUHistoryTabForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			SDUHistoryTabReportData report = new SDUHistoryTabReportData(null,0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(actionForm, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		
		String validationResult = validate(actionForm, request)	;
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}		
				
		// reset the form in the session if any
		// this will ensure that the user always sees
		// the default view of the report
		if (!StringUtils.equals(getTask(request), PRINT_TASK)) {
		actionForm = (SDUHistoryTabForm) resetForm(actionForm, request);
		}
		request.getSession(false).removeAttribute(SDU_HISTORY_TAB_FORM);			
			
		String forward = doCommon(actionForm, request, response);			

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		
		postExecute(actionForm, request, response);		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		
	}

	/**
	 * This method populates a default form when the report page is first brought
	 * up. This method is called before populateReportCriteria() to allow default
	 * sort and other criteria to be set properly.
	 *
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(ActionMapping,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm(reportForm, request);
		UserProfile userProfile = getUserProfile(request);			
		SDUHistoryTabForm sduHistoryTabForm = (SDUHistoryTabForm) reportForm;		
		if ( StringUtils.equals(getTask(request), DEFAULT_TASK)) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);		
			Calendar cal = Calendar.getInstance();
			Date endDate = cal.getTime();
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			Date startDate =cal.getTime();
			sduHistoryTabForm.setFilterStartDate(AwdHelper.getFormattedDate(startDate,sdf));		
			sduHistoryTabForm.setFilterEndDate(AwdHelper.getFormattedDate(endDate,sdf));
			sduHistoryTabForm.setPageNumber(1);
			}
		else {
			//
		}
		//Disable but keep the just mine checkbox in checked state when the user is Payroll Admin user	
		if (userProfile.getRole() instanceof PayrollAdministrator) {
			sduHistoryTabForm.setJustMineFilter(true);
			sduHistoryTabForm.setJustMine(true);
		}
		else {
			sduHistoryTabForm.setJustMineFilter(false);
		}
		
	}
	
	/** 
	 * get the sort criteria and convert it into string
	 * eg. "&sort=submissionTs,asc&sort=submissionId,desc&sort=fileName,asc"
	*/
	private static String createSortPhrase(final ReportCriteria criteria) {
        StringBuffer result = new StringBuffer();
        Iterator<ReportSort> sorts = criteria.getSorts().iterator();        
        while(sorts.hasNext()) {
            ReportSort sort = (ReportSort) sorts.next();
            result.append("&sort=");
            result.append(fieldToColumnMap.get(sort.getSortField()));
            result.append(',');
            
            /*
             * This if block will reverse the sort direction for SubmissionStatus field as a 
            workaround to solve the issue with custom status mapping 
            */
            if (StringUtils.equals(fieldToColumnMap.get(sort.getSortField()),
            		SDUHistoryTabReportData.SORT_SUBMISSION_STATUS) ){            	

                if(StringUtils.equals(sort.getSortDirection(), ReportSort.DESC_DIRECTION)) {
                	result.append(ReportSort.ASC_DIRECTION.toLowerCase());
                }
                else {
                	result.append(ReportSort.DESC_DIRECTION.toLowerCase());
                }                
            }
            else {
                result.append(sort.getSortDirection().toLowerCase());            	
            }
        }
        return result.toString();     
        }
	
	
	 protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
	            throws SystemException, ReportServiceException {
			StopWatch stopWatch = new StopWatch();
			try {

				stopWatch.start();
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("entry -> getReportData");
			}			
			
			ReportData bean = getSubmissionHistory(reportCriteria,request);
			
			try {

				stopWatch.stop();
				if (logger.isInfoEnabled()) {
					logger.info("Report Id [" + reportId + "] criteria ["
							+ reportCriteria + "] timing: " + stopWatch.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("exit <- getReportData");
			}
	        
	        return bean;
	    }
	
	/** 
	 * Pass clientUser as null when the clientUser field should not be included in filter criteria
	 * Pass submissionId= 0 when the submissionId field should not be included in filter criteria
	 * pageNumber should always start from 0
	 */	
	private ReportData getSubmissionHistory(ReportCriteria reportCriteria,HttpServletRequest request) throws SystemException {
		
		String edgeGatewayURL = SDUHelper.getApigeeEdgeProxyURL();
		 int defaultPageSize = new Integer(CommonEnvironment.getInstance()
	                .getDefaultPageSize()).intValue();
		PagedResources<SDUHistoryTabVO> secureDocumentUploadHistoryPage=null;		
				
		SDUHistoryTabReportData reportData = new SDUHistoryTabReportData(reportCriteria,0);		
		
		try {		
			
			int contractId = (int) reportCriteria.getFilterValue(SDUHistoryTabReportData.FILTER_CONTRACT_NO);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
			Date submissionTsFrom = AwdHelper.getParsedDate(String.valueOf(
					reportCriteria.getFilterValue(SDUHistoryTabReportData.FILTER_START_DATE)), sdf);
			Date submissionTsTo= AwdHelper.getParsedDate(String.valueOf(
					reportCriteria.getFilterValue(SDUHistoryTabReportData.FILTER_END_DATE)), sdf);
			String clientUser= (String) reportCriteria.getFilterValue(SDUHistoryTabReportData.FILTER_USER_ID);
			int pageNumber=0; 
			pageNumber= reportCriteria.getPageNumber()-1;
			
			String sortCriteria = createSortPhrase(reportCriteria);		
			if(sortCriteria==null ||  sortCriteria.isEmpty()) {
				sortCriteria = "&sort=submissionTs,asc&sort=submissionId,desc&sort=fileName,asc";
			}				
			String endpointURL = edgeGatewayURL + SDUConstants.SDU_SUBMISSION_HISTORY_ENDPOINT + "?page=" + pageNumber + "&size=" + defaultPageSize + sortCriteria ;		

			JsonFactory nodeFactory = new JsonFactory();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			RestTemplate restTemplate = new RestTemplate();	
			boolean pagedResults=true;
			if ( StringUtils.equals(getTask(request), DOWNLOAD_TASK) || StringUtils.equals(getTask(request), PRINT_TASK)) {
				pagedResults=false;
			}
			
			JsonGenerator payload = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
			payload.writeStartObject();
			payload.writeStringField("clientId", SDUConstants.SDU_PS_CLIENT_ID);
			payload.writeNumberField("clientContract", contractId);			
			
			
			SimpleDateFormat rdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			payload.writeStringField("submissionTsFrom", ((AwdHelper.getFormattedDate(submissionTsFrom,rdf))+"T00:00:00.000-0400"));
			payload.writeStringField("submissionTsTo",((AwdHelper.getFormattedDate(submissionTsTo,rdf))+"T23:59:59.000-0400"));		
			payload.writeNumberField("submissionId", 0);					
			if (clientUser!= null && !clientUser.isEmpty()) {
				payload.writeStringField("clientUser", clientUser );				
			} 
			else {
				payload.writeStringField("clientUser", "" );
			}
			payload.writeBooleanField("pagedResults",pagedResults);
			payload.writeEndObject();
			payload.close();
			String jsonBody = payload.getOutputTarget().toString();
			
		   
		    HttpEntity<String> requestBody = new HttpEntity<>(jsonBody,SDUHelper.getHttpHeaders());
		    ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String jsonString = mapper.writeValueAsString(requestBody);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Json String : " + jsonString);
			}			
			
			
			if ( StringUtils.equals(getTask(request), DOWNLOAD_TASK) || StringUtils.equals(getTask(request), PRINT_TASK)) {
			
				ResponseEntity<List<SDUHistoryTabVO>> responses = restTemplate.exchange(endpointURL,HttpMethod.POST,requestBody, new ParameterizedTypeReference<List<SDUHistoryTabVO>>() {});
			    if (responses.getStatusCode() == HttpStatus.OK) {
		        	
		        	if (logger.isDebugEnabled()) {
						logger.debug("Response received from Submission History endpoint, Status code:" + responses.getStatusCode());
					}	        	
		        	//secureDocumentUploadHistoryPage = ;	

		        	if(responses.getBody() != null) {
		    	    		//PageMetadata pageMetaData = secureDocumentUploadHistoryPage.getMetadata();
		    	    
		    	    		Collection<SDUHistoryTabVO> secureDocumentUploadHistoryList = responses.getBody();
		    	    		reportData.setTotalCount((int) secureDocumentUploadHistoryList.size());
		    	    		reportData.setTotalItems((int) secureDocumentUploadHistoryList.size());
		    	    		reportData.setDetails(secureDocumentUploadHistoryList);
		    	    		if (logger.isDebugEnabled()) {
		    	    			logger.debug("Total Items in current page : " + secureDocumentUploadHistoryList.size());
		    	    		}
		    	    		
		    	    	}
		    	    	else {
		    	    		if (logger.isDebugEnabled()) {
		    	    			logger.debug("Response is received as null from Submission History endpoint");
		    	    		}	    	    		
		    	    	}  

		        }        
		        else {
		        	if (logger.isDebugEnabled()) {
						logger.debug("Error in getting response from Submission History endpoint, Status code:" + responses.getStatusCode());
					}	        	
		        }
			}
			else {
			ResponseEntity<PagedResources<SDUHistoryTabVO>> responses = restTemplate.exchange(endpointURL,HttpMethod.POST,requestBody, new ParameterizedTypeReference<PagedResources<SDUHistoryTabVO>>() {});
						
	        // Code = 200.
	        if (responses.getStatusCode() == HttpStatus.OK) {
	        	
	        	if (logger.isDebugEnabled()) {
					logger.debug("Response received from Submission History endpoint, Status code:" + responses.getStatusCode());
				}	        	
	        	secureDocumentUploadHistoryPage = responses.getBody();	

	        	if(secureDocumentUploadHistoryPage != null) {
	    	    		PageMetadata pageMetaData = secureDocumentUploadHistoryPage.getMetadata();
	    	    		if(pageMetaData!=null) {
	    	    			if (logger.isDebugEnabled()) {
		    	    			logger.debug("Items requested per page : " + pageMetaData.getSize());	            		
		    	    			logger.debug("Current page  : " + pageMetaData.getNumber());
		    	    			logger.debug("Total page(s) : " + pageMetaData.getTotalPages()); 
		    	    			logger.debug("Total Items : " + pageMetaData.getTotalElements());
	    	    			}	  	        		
	    	        		reportData.setTotalPages((int) pageMetaData.getTotalPages());
	    	        		reportData.setTotalCount((int) pageMetaData.getTotalElements());		    	    			    	        			    	        		    	        		
	    	        	}        	
	    	    		else {
	    	    			if (logger.isDebugEnabled()) {
	    	    				logger.debug("Response MetaData is received as null from Submission History endpoint");
	    	    			}
	    	    		}
	    	    		Collection<SDUHistoryTabVO> secureDocumentUploadHistoryList = secureDocumentUploadHistoryPage.getContent();
	    	    		reportData.setTotalItems((int) secureDocumentUploadHistoryList.size());
	    	    		reportData.setDetails(secureDocumentUploadHistoryList);
	    	    		if (logger.isDebugEnabled()) {
	    	    			logger.debug("Total Items in current page : " + secureDocumentUploadHistoryList.size());
	    	    		}
	    	    		
	    	    	}
	    	    	else {
	    	    		if (logger.isDebugEnabled()) {
	    	    			logger.debug("Response is received as null from Submission History endpoint");
	    	    		}	    	    		
	    	    	}  

	        }        
	        else {
	        	if (logger.isDebugEnabled()) {
					logger.debug("Error in getting response from Submission History endpoint, Status code:" + responses.getStatusCode());
				}	        	
	        }
	        
			} 
		}
		
		catch (JsonProcessingException jpe) {
			logger.error("Json request: "+jpe.getMessage());
			throw new SystemException("Json request: "+jpe.getMessage());
		}
		catch (Exception e) {			
			logger.error("Exception in microservice call: "+ e.getStackTrace());
			throw new SystemException("Exception in microservice call: "+ e.getStackTrace());
		}	
		return reportData;	
	}	
	
	@SuppressWarnings("deprecation")
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		ReportData reportData =  report;
		StringBuffer buffer = new StringBuffer();
		try {

			// print Contract Number / Name
			SDUHistoryTabForm sduHistoryTabForm= (SDUHistoryTabForm)reportForm;
			UserProfile up = getUserProfile(request);

			buffer.append("Contract").append(COMMA).append(up.getCurrentContract().getContractNumber()).append(COMMA)
					.append(up.getCurrentContract().getCompanyName());

			buffer.append(LINE_BREAK);			
			buffer.append("From date").append(COMMA);
			buffer.append(sduHistoryTabForm.getFilterStartDate()).append(COMMA);
			buffer.append("To date").append(COMMA);
			buffer.append(sduHistoryTabForm.getFilterEndDate()).append(COMMA);
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);	
			buffer.append(LINE_BREAK);	
			buffer.append(DOWNLOAD_COLUMN_HEADING_SUBMISSIONS);
			buffer.append(LINE_BREAK);

			// Secure Document Upload - history Detail Data
			if (reportData.getDetails() != null && reportData.getDetails().size() > 0) {
				for (Iterator<SDUHistoryTabVO> it = reportData.getDetails().iterator(); it.hasNext();) {
					SDUHistoryTabVO item = (SDUHistoryTabVO) it.next();

					// get and format the received date
					//new Date(timestamp.getTime())
					Timestamp newtimeStamp=item.getSubmissionTs();
					buffer.append(getCsvString(item.getSubmissionId())).append(COMMA);
					//RenderConstants.LONG_TIMESTAMP_MDY_SLASHED					
					buffer.append(getCsvString(DateRender.formatByPattern(newtimeStamp, " ",RECEIVED_DATE_MASK))).append(COMMA);					
					buffer.append(getCsvString(item.getFileName())).append(COMMA);
					buffer.append(getCsvString(item.getClientUserName())).append(COMMA);
					buffer.append(getCsvString(item.getClientUserRole())).append(COMMA);
					buffer.append(getCsvString(item.getSubmissionStatus())).append(COMMA);
					buffer.append(LINE_BREAK);
				}
			} else {

				// no records found: show the message
				
			}
		} catch (Throwable t) {
			throw new SystemException(t, getClass().getName(), "populateDownloadData", "Something wrong with CMA");
		}
		return buffer.toString().getBytes();	
	}
	
	@RequestMapping(value = "/history/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("sduHistoryTabForm") SDUHistoryTabForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request)	;
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		//Clear the sorting so that when the user does a search the default sort will be applied.
		form.setSortField(null);
		form.setSortDirection(null);
		String forward = super.doFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	@RequestMapping(value = "/history/", params = {"task=page" }, method = {RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("sduHistoryTabForm") SDUHistoryTabForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doPage(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/history/", params = {"task=sort"}, method = {RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("sduHistoryTabForm") SDUHistoryTabForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				postExecute(form, request, response);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doSort(form, request, response);
		
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/history/", params = {"task=download"}, method = {RequestMethod.POST ,RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("sduHistoryTabForm") SDUHistoryTabForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String validationResult = validate(form, request);
		if(StringUtils.isNotBlank(validationResult)){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult);
		}
		String forward = super.doDownload(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	@Autowired
	private PSValidatorFWInput  psValidatorFWInput;

    @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	} 
    
    
    protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		UserProfile userProfile = getUserProfile(request);
		int contractId = userProfile.getCurrentContract().getContractNumber();
		return "SubmitDocumentHistory_" + String.valueOf(contractId)+ CSV_EXTENSION;
	}
	
}