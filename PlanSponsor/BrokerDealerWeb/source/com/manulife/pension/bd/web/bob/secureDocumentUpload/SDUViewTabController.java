   
package com.manulife.pension.bd.web.bob.secureDocumentUpload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabReportData;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUViewTabVO;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.OAuthTokenGenerator;
import com.manulife.pension.platform.web.util.SDUHelper;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Action class for populating the SDU View page.
 *
 */
@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value="/bob/secureDocumentUpload")
@SessionAttributes({"sduViewTabForm"})

public class SDUViewTabController extends BDReportController {

	@ModelAttribute("sduViewTabForm")
	public SDUViewTabForm populateForm() {
		return new SDUViewTabForm();
	}	

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/secureDocumentUpload/sduViewTab.jsp");
		forwards.put("sort","/secureDocumentUpload/sduViewTab.jsp");
		forwards.put("page","/secureDocumentUpload/sduViewTab.jsp");
		forwards.put("input","/secureDocumentUpload/sduViewTab.jsp");
		forwards.put("systemErrorPage","/error.jsp");
	}
	
    private static final Map<String,String> fieldToColumnMap = new HashMap<String,String>();
    static {
        fieldToColumnMap.put("fileName", "fileName");
        fieldToColumnMap.put("clientUserName", "clientUserName");
        fieldToColumnMap.put("shareExpiryTs", "shareExpiryTs");
    }

    private BaseEnvironment baseEnvironment= new BaseEnvironment();    
	private static final String DEFAULT_SORT_FIELD = SDUViewTabReportData.SORT_EXPIRY_TS;
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
	public static final String DISPLAY_SDU_VIEW_TAB = "displayViewTab";
	public static final String ALLOW_FILE_DELETE = "allowFileDelete";
	public static final String SDU_VIEWTAB_RESULTS="reportBean";		

	/**
	 * constructor
	 */
	public SDUViewTabController() {
		super(SDUViewTabController.class);	
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return SDUViewTabReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return SDUViewTabReportData.REPORT_NAME;
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

		SDUViewTabForm sduViewTabForm = (SDUViewTabForm) form;
		
		reportCriteria.addFilter(SDUViewTabReportData.REQ_SDU_CLIENT_ID,SDUConstants.SDU_BD_CLIENT_ID);
		// get the user profile object
		BDUserProfile userProfile = getUserProfile(request);
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		reportCriteria.addFilter(SDUViewTabReportData.REQ_CONTRACT_NO, contractId);		
		
		if (sduViewTabForm.isJustMine()) {
			reportCriteria.addFilter(SDUViewTabReportData.REQ_SHARED_WITH_USER_ID,
					String.valueOf(userProfile.getBDPrincipal().getProfileId()));			
		}
		else {
			reportCriteria.addFilter(SDUViewTabReportData.REQ_SHARED_WITH_USER_ID,"");
		}
		
		if (SDUViewTabForm.isFieldSet(sduViewTabForm.getPageNumber())) {
			reportCriteria.setPageNumber(sduViewTabForm.getPageNumber());
		}			
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

		Collection errors = new ArrayList();

		// validating the fields based on the task 
		if (FILTER_TASK.equals(request.getParameter(TASK_KEY)) 
				|| PRINT_TASK.equals(request.getParameter(TASK_KEY)) 
				|| DOWNLOAD_TASK.equals(request.getParameter(TASK_KEY))) {
			
		}

		if (!errors.isEmpty()) {
			SDUViewTabReportData emptyReport = new SDUViewTabReportData(null, 0);			
			emptyReport.setDetails(new ArrayList());
			emptyReport.setReportCriteria( new ReportCriteria(null));
			request.setAttribute(BDConstants.REPORT_BEAN, emptyReport);
		}
		return errors;
	}

	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		
		if (form.getSortField() != null) {			
			//Insert Primary sort
			criteria.insertSort(form.getSortField(), form.getSortDirection());
			if (form.getSortField().equals(SDUViewTabReportData.SORT_EXPIRY_TS)) {				
				//Insert Secondary sort as file name
				criteria.insertSort(SDUViewTabReportData.SORT_FILE_NAME, ReportSort.ASC_DIRECTION);
			} 
			else if (form.getSortField().equals(SDUViewTabReportData.SORT_FILE_NAME)){
				//Insert Secondary sort as Expiry TS
				criteria.insertSort(SDUViewTabReportData.SORT_EXPIRY_TS, ReportSort.DESC_DIRECTION);
			}
			else if (form.getSortField().equals(SDUViewTabReportData.SORT_SHARED_BY_USER_NAME)){
				//Insert Secondary sort as Expiry Ts
				criteria.insertSort(SDUViewTabReportData.SORT_EXPIRY_TS, ReportSort.DESC_DIRECTION);
			}
		}
		else {
			//Default Sort
			//Insert Primary sort
			criteria.insertSort(getDefaultSort(), getDefaultSortDirection());	
			//Insert Secondary sort
			criteria.insertSort(SDUViewTabReportData.SORT_FILE_NAME, ReportSort.ASC_DIRECTION);
			form.setSortField(DEFAULT_SORT_FIELD);
			form.setSortDirection(DEFAULT_SORT_DIRECTION);
		}
	}

	public String doCommon(BaseReportForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		//FunctionalLogger.INSTANCE.log("SDU view tab History", getUserProfile(request), getClass(),
			//	getMethodName(actionForm, request));

		String result =  super.doCommon(actionForm, request, response);
		return result;
	}
	
	@RequestMapping(value ="/view/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("sduViewTabForm") SDUViewTabForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			SDUViewTabReportData report = new SDUViewTabReportData(null,0);
			request.setAttribute(SDUConstants.REPORT_BEAN, report);
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
		actionForm = (SDUViewTabForm) resetForm(actionForm, request);
		}
		request.getSession(false).removeAttribute("sduViewTabForm");			
			
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
		BobContext bob = BDSessionHelper.getBobContext(request);
		BDUserProfile userProfile = getUserProfile(request);
		SDUViewTabForm sduViewTabForm = (SDUViewTabForm) reportForm;
		String contractStatus = bob.getCurrentContract().getStatus();
		if ( StringUtils.equals(getTask(request), DEFAULT_TASK)) {		
			sduViewTabForm.setPageNumber(1);
			}
		else {
			//
		}
		

		//For external users(Excluding mimic), display only the files shared with them
		if(!userProfile.isInternalUser() && !userProfile.isInMimic()) {
			sduViewTabForm.setDisplayViewTab(true);
			sduViewTabForm.setAllowFileDelete(false);
			sduViewTabForm.setAllowFileShare(false);
			sduViewTabForm.setJustMine(true);
		}
		//For internal users and internal users mimicking external users, display all the files shared for the contract
		else if(userProfile.isInternalUser() || userProfile.isInMimic()) {
			sduViewTabForm.setDisplayViewTab(true);
			sduViewTabForm.setAllowFileDelete(true);
			sduViewTabForm.setAllowFileShare(true);
			sduViewTabForm.setJustMine(false);
		}
		//For all other users except above, block access to view the share history / share functionality
		else {
			sduViewTabForm.setDisplayViewTab(false);
			sduViewTabForm.setAllowFileDelete(false);
			sduViewTabForm.setAllowFileShare(false);
			sduViewTabForm.setJustMine(false);	
		}
		
	   if (userProfile.isInMimic()) {
            BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
                    .getMimckingUserProfile(request);
            sduViewTabForm.setDownloadedByUserId(String.valueOf(mimickingUserProfile.getBDPrincipal().getProfileId()));
            sduViewTabForm.setDownloadedByUserName(mimickingUserProfile.getBDPrincipal().getFirstName()+" "+mimickingUserProfile.getBDPrincipal().getLastName());
            sduViewTabForm.setDownloadedByUserRole(BDUserRoleDisplayNameUtil.getInstance()
					.getDisplayName(mimickingUserProfile.getBDPrincipal().getBDUserRole().getRoleType()));
            
        } else {
        	sduViewTabForm.setDownloadedByUserId(String.valueOf(userProfile.getBDPrincipal().getProfileId()));
        	sduViewTabForm.setDownloadedByUserName(userProfile.getBDPrincipal().getFirstName()+" "+userProfile.getBDPrincipal().getLastName());
            sduViewTabForm.setDownloadedByUserRole(BDUserRoleDisplayNameUtil.getInstance()
					.getDisplayName(userProfile.getBDPrincipal().getBDUserRole().getRoleType()));
        }
		
		try {
			sduViewTabForm.setoAuthToken(SDUHelper.getAccessToken());
		} catch (SystemException e) {			
			logger.error(e.getMessage());
		}
		sduViewTabForm.setApigeeProxyURL(baseEnvironment.getNamingVariable(
				SDUConstants.APIGEE_EDGE_PROXY_URL, null));		
		sduViewTabForm.setPendingContract(SDUHelper.isPreActiveContract(contractStatus));
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
            result.append(sort.getSortDirection().toLowerCase());               
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
			
			ReportData bean = getShareHistory(reportCriteria,request);
			
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
	private ReportData getShareHistory(ReportCriteria reportCriteria,HttpServletRequest request) throws SystemException {
		
		String edgeGatewayURL = SDUHelper.getApigeeEdgeProxyURL();
		 int defaultPageSize = new Integer(CommonEnvironment.getInstance()
	                .getDefaultPageSize()).intValue();
		PagedResources<SDUViewTabVO> sduViewTabPagedResource=null;		
				
		SDUViewTabReportData reportData = new SDUViewTabReportData(reportCriteria,0);		
		
		try {		
			int pageNumber=0; 
			pageNumber= reportCriteria.getPageNumber()-1;			
			String sortCriteria = createSortPhrase(reportCriteria);	
			
			if(sortCriteria==null ||  sortCriteria.isEmpty()) {
				sortCriteria = "&sort=shareExpiryTs,desc&sort=fileName,asc";
			}	
			
			String endpointURL = edgeGatewayURL + SDUConstants.SDU_SHARE_HISTORY_ENDPOINT + "?page=" + pageNumber + "&size=" + defaultPageSize + sortCriteria ;
			//String endpointURL = "https://manulife-operations-preprod-ext.apigee.net/v2/rps/sdu/ca/uat/shareHistory?page=0&size=35&sort=shareExpiryTs,asc&sort=fileName,desc";
			
			JsonFactory nodeFactory = new JsonFactory();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			RestTemplate restTemplate = new RestTemplate();	
			
			JsonGenerator payload = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
			payload.writeStartObject();
			payload.writeStringField(SDUViewTabReportData.REQ_SDU_CLIENT_ID, SDUConstants.SDU_BD_CLIENT_ID);
			payload.writeNumberField(SDUViewTabReportData.REQ_CONTRACT_NO, (int) reportCriteria.getFilterValue(SDUViewTabReportData.REQ_CONTRACT_NO) );
			payload.writeStringField(SDUViewTabReportData.REQ_SHARED_WITH_USER_ID, (String) reportCriteria.getFilterValue(SDUViewTabReportData.REQ_SHARED_WITH_USER_ID) );
			boolean pagedResults=true;
			if ( StringUtils.equals(getTask(request), DOWNLOAD_TASK) || StringUtils.equals(getTask(request), PRINT_TASK)) {
				pagedResults=false;
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
			
		ResponseEntity<PagedResources<SDUViewTabVO>> responses =restTemplate.exchange(endpointURL,HttpMethod.POST,requestBody, new ParameterizedTypeReference<PagedResources<SDUViewTabVO>>() {});
						
	        // Code = 200.
	        if (responses.getStatusCode() == HttpStatus.OK) {
	        	
	        	if (logger.isDebugEnabled()) {
					logger.debug("Response received from Share History endpoint, Status code:" + responses.getStatusCode());
				}	        	
	        	sduViewTabPagedResource = responses.getBody();	

	        	if(sduViewTabPagedResource != null) {
	    	    		PageMetadata pageMetaData = sduViewTabPagedResource.getMetadata();
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
	    	    		Collection<SDUViewTabVO> sduViewTabVOList = sduViewTabPagedResource.getContent();
	    	    		reportData.setTotalItems((int) sduViewTabVOList.size());
	    	    		reportData.setDetails(sduViewTabVOList);
	    	    		if (logger.isDebugEnabled()) {
	    	    			logger.debug("Total Items in current page : " + sduViewTabVOList.size());
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

	@RequestMapping(value = "/view/", params = {"task=page" }, method = {RequestMethod.POST})
	public String doPage(@Valid @ModelAttribute("sduViewTabForm") SDUViewTabForm form,
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
	
	
	@RequestMapping(value = {"/view/error/"}, method ={RequestMethod.GET}) 
	public String doRedirectToError(@ModelAttribute("sduViewTabForm") SDUViewTabForm theForm, HttpServletRequest request,HttpServletResponse response) 
	 {
		return forwards.get(SYSTEM_ERROR_PAGE);		
	}

	@RequestMapping(value = "/view/", params = {"task=sort"}, method = {RequestMethod.POST})
	public String doSort(@Valid @ModelAttribute("sduViewTabForm") SDUViewTabForm form,
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
	
	@Autowired
	private BDValidatorFWInput  bdValidatorFWInput;

    @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	} 
    
    
    protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();	
		return "SubmitDocumentHistory_" + String.valueOf(contractId)+ CSV_EXTENSION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}
	
}