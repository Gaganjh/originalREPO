package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsEmailNotificationVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUShareDocumentsForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionClientDataVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionMetaDataVO;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.SDUEmailNotificationHelper;
import com.manulife.pension.platform.web.util.SDUHelper;
import com.manulife.pension.ps.service.withdrawal.email.EmailGeneratorException;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.ContractEnrollmentVO;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.CAR;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.SuperCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.valueobject.RegisteredExternalUserInfoVO;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.email.EmailMessageException;
import com.manulife.pension.service.security.role.PFSRelationshipManager;
/**
 * 
 * Handles the submission of a Document
 * 
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"sduShareDocumentsForm"})
public class SDUShareDocumentsController extends PsAutoController implements CommonConstants {

	@ModelAttribute("sduShareDocumentsForm")
	public SDUShareDocumentsForm populateForm() {
		return new SDUShareDocumentsForm();
	}
	
	protected static Logger logger;
	private static final String SDU_SUBMIT_TAB_FORM="sduShareDocumentsForm";
	protected static final String SEARCH_KEY = "like";
	protected static final String DEFAULT_SEARCH = "%%";
	protected static final String TASK_KEY = "task";
    protected static final String PRINT_TASK = "print";
    protected static final String DEFAULT_TASK = "default";
    private BaseEnvironment baseEnvironment= new BaseEnvironment();
	
	public static Map<String, String> forwards = new HashMap<>();
	static {
		logger = Logger.getLogger(SDUShareDocumentsController.class);
		forwards.put("default", "/tools/sduShareDocuments.jsp");
		forwards.put("input", "/tools/sduShareDocuments.jsp");
		forwards.put("print", "/tools/sduShareDocuments.jsp");
		forwards.put("view", "redirect:/do/tools/secureDocumentUpload/view/");
		forwards.put("tools", "redirect:/do/tools/toolsMenu/");
		forwards.put("systemErrorPage","/error.jsp");
	}

	/**
	 * Constructor.
	 */
	public SDUShareDocumentsController() {
		super(SDUShareDocumentsController.class);		
	}
	
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }
    
    protected String getSearchText(HttpServletRequest request) {
        String searchText = request.getParameter(SEARCH_KEY);
        if (searchText == null) {
        	searchText = DEFAULT_SEARCH;
        }
        else {        	
        	searchText = "%"+StringUtils.replace(StringUtils.upperCase(searchText), " ", "%")+"%";
        }
        return searchText;
    }
	
    /**
     * This controller is to redirect the user to correct url do/tools/secureDocumentUpload/submit when the user enters the submit page using the bookmark
     * @param theForm
     * @param request
     * @param response
     */
	
	@RequestMapping(value = {"/secureDocumentUpload/shareDocuments/error/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doRedirectToError(@ModelAttribute("sduShareDocumentsForm") SDUShareDocumentsForm theForm, HttpServletRequest request,HttpServletResponse response) 
	 {
		return forwards.get(SYSTEM_ERROR_PAGE);		
	}
	
	
	@RequestMapping(value ="/secureDocumentUpload/shareDocuments/getSubmissionMetaData/" , method =  {RequestMethod.POST}) 
    public String getSubmissionMetaData ( @ModelAttribute("sduShareDocumentsForm") SDUShareDocumentsForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException 
     {
		if (SessionHelper.getUserProfile(request)!=null) {
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("application/json");
			UserProfile userProfile = getUserProfile(request);
			theForm.setSduSubmissionMetaDataVO(populateSubmissionMetaData(userProfile,theForm));
			new Gson().toJson(theForm.getSduSubmissionMetaDataVO(), response.getWriter());
			return null; 
		}
		else {
			return forwards.get(INPUT);			
		}		
    }
	

	
	@RequestMapping(value = "/secureDocumentUpload/shareDocuments/sendEmailNotification/", method = {
			RequestMethod.POST })
	public String sendEmailNotification(@ModelAttribute("sduShareDocumentsForm") SDUShareDocumentsForm theForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, EmailGeneratorException, EmailMessageException {
		if (SessionHelper.getUserProfile(request) != null) {	
			if(theForm.getSduSubmissionMetaDataVO() !=null && theForm.getSendEmail()) {
				Location siteLocation = CommonConstants.SITEMODE_NY.equals(CommonEnvironment.getInstance().getSiteLocation()) ? Location.NEW_YORK : Location.USA;
				List<SDUShareDocumentsEmailNotificationVO> sduShareDocumentsEmailNotificationVOList=SDUEmailNotificationHelper.getInstance().sendEmail(theForm.getSubmissionId(), theForm.getSduSubmissionMetaDataVO(),"ps",siteLocation);			
				if(null!=sduShareDocumentsEmailNotificationVOList && !sduShareDocumentsEmailNotificationVOList.isEmpty()) {
					logger.debug("# of SDU share Emails sent : " + sduShareDocumentsEmailNotificationVOList.size());
					updateEmailNotificationInfo(sduShareDocumentsEmailNotificationVOList);
				}
			}			
			return forwards.get("view");
		} else {
			return forwards.get(INPUT);
		}
	}
	
	protected SDUSubmissionMetaDataVO populateSubmissionMetaData(UserProfile userProfile, SDUShareDocumentsForm theForm ) throws SystemException {
		
		SDUSubmissionMetaDataVO sduSubmissionMetaDataVO = new SDUSubmissionMetaDataVO();
		int contractId = userProfile.getContractProfile().getContract().getContractNumber();
		sduSubmissionMetaDataVO.setClientContract(String.valueOf(contractId));
		sduSubmissionMetaDataVO.setClientContractName(StringUtils.trim(userProfile.getContractProfile().getContract().getCompanyName()));
		sduSubmissionMetaDataVO.setClientContractStatus(userProfile.getContractProfile().getContract().getStatus());
		
		SDUSubmissionClientDataVO sduSubmissionClientDataVO = new SDUSubmissionClientDataVO();
		Date submissionDate = Calendar.getInstance().getTime();
		sduSubmissionClientDataVO.setAfterMarketIndicator(SDUHelper.getAfterMarketIndicator(submissionDate));
		sduSubmissionClientDataVO.setContractStatus(sduSubmissionMetaDataVO.getClientContractStatus());
		sduSubmissionClientDataVO.setCompanyName(sduSubmissionMetaDataVO.getClientContractName());	
		
		String businessStatusCode=null;
		sduSubmissionClientDataVO.setBusinessCode("New Business");//default value
		ContractEnrollmentVO contractEnrollmentVO = ContractServiceDelegate.getInstance().getContractInfoForEnrollment(contractId);
		if(null!=contractEnrollmentVO){
			businessStatusCode = contractEnrollmentVO.getBusinessStatusCode();
			if(null!=businessStatusCode && businessStatusCode.trim().length()>0){
				sduSubmissionClientDataVO.setBusinessCode(businessStatusCode.equalsIgnoreCase("O") ? "Inforce" : "New Business");
			}
		}
		sduSubmissionClientDataVO.setClientUser(userProfile.getPrincipal().getFirstName()+" "+userProfile.getPrincipal().getLastName());
		sduSubmissionClientDataVO.setSubmitterRole(userProfile.getPrincipal().getRole().getDisplayName());
		sduSubmissionClientDataVO.setSubmissionSource(SDUConstants.SDU_PS_SUBMISSION_SOURCE);
		ContactVO contactVO=ContractServiceDelegate.getInstance().getContactsDetail(contractId);
		if(null!=contactVO && null != contactVO.getName()) {
		sduSubmissionClientDataVO.setClientAccountRepresentative(StringUtils.trim(contactVO.getName()));
		}else {
			sduSubmissionClientDataVO.setClientAccountRepresentative(StringUtils.EMPTY);	
		}
		sduSubmissionClientDataVO.setClientApplicationName(SDUConstants.SDU_PS_CLIENT_APP_NAME);
		Gson gson = new GsonBuilder().create();		
		sduSubmissionMetaDataVO.setClientData(gson.toJson(sduSubmissionClientDataVO));
		sduSubmissionMetaDataVO.setClientId(SDUConstants.SDU_PS_CLIENT_ID);
		sduSubmissionMetaDataVO.setClientUser(String.valueOf(userProfile.getPrincipal().getProfileId()));
		sduSubmissionMetaDataVO.setClientUserName(sduSubmissionClientDataVO.getClientUser());
		sduSubmissionMetaDataVO.setClientUserRole(sduSubmissionClientDataVO.getSubmitterRole());
		sduSubmissionMetaDataVO.setShareable(true);		
		sduSubmissionMetaDataVO.setShareInfo(theForm.getShareInfoJson());
		String comments = theForm.getSubmissionComments();
		String encodedComments = Base64.getEncoder().encodeToString(comments.getBytes());
		sduSubmissionMetaDataVO.setSubmissionDesc(encodedComments);
		sduSubmissionMetaDataVO.setSubmissionStatus("completed");
		sduSubmissionMetaDataVO.setTargetSystem("Azure-Storage");		
		return sduSubmissionMetaDataVO;		
	}
	
	
	@RequestMapping(value = {"/secureDocumentUpload/shareDocuments/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("sduShareDocumentsForm") SDUShareDocumentsForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
	       }
		}
		
		String validationResult = validate(theForm, request);
		if(validationResult!=null){
			return StringUtils.contains(validationResult,'/')?validationResult:forwards.get(validationResult); 
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		
		UserProfile userProfile = getUserProfile(request);
		theForm.reset(request);
		request.getSession().removeAttribute(SDU_SUBMIT_TAB_FORM);
		populateForm(theForm, userProfile);
		request.getSession().setAttribute(SDU_SUBMIT_TAB_FORM, theForm);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		return forwards.get(INPUT);		
	}
	
    
	@RequestMapping(value ="/secureDocumentUpload/shareDocuments/" ,params={"task=search"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doSearch ( @ModelAttribute("sduShareDocumentsForm") SDUShareDocumentsForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException 
     {
		if (SessionHelper.getUserProfile(request)!=null) {
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("application/json");
			UserProfile userProfile = getUserProfile(request);
			int contractId = userProfile.getContractProfile().getContract().getContractNumber();
			
			List<RegisteredExternalUserInfoVO> allContactList = SecurityServiceDelegate.getInstance().getAllContactsforSDU(contractId, SDUConstants.SDU_PSW, getSearchText(request));
			new Gson().toJson(allContactList, response.getWriter());
			return null; 
		}
		else {
			return forwards.get(INPUT);			
		}
		
    }
	
	protected void populateForm(AutoForm form, UserProfile userProfile)	throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");
		boolean fileshareUser =false;
		if (userProfile.getRole() instanceof CAR || userProfile.getRole() instanceof TeamLead
				|| userProfile.getRole() instanceof BundledGaCAR
				|| userProfile.getRole() instanceof RelationshipManager
				|| userProfile.getRole() instanceof PFSRelationshipManager
				|| userProfile.getRole() instanceof SuperCAR) {
			
			fileshareUser=true;
		}
				
		SDUShareDocumentsForm theForm = (SDUShareDocumentsForm) form;
		theForm.setDisplayFileUploadSection(fileshareUser);		
		theForm.setSubmissionComments("");	
		theForm.setWidgetEndpointURL(baseEnvironment.getNamingVariable(
				SDUConstants.APIGEE_EDGE_PROXY_URL, null));
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateForm");
	}
	
	protected void updateEmailNotificationInfo(
			List<SDUShareDocumentsEmailNotificationVO> sduShareDocumentsEmailNotificationVOList) throws SystemException {
		String edgeGatewayURL = SDUHelper.getApigeeEdgeProxyURL();

		try {
			String endpointURL = edgeGatewayURL + SDUConstants.SDU_UPDATE_EMAIL_NOTIFCATION_INFO + "/"
					+ SDUConstants.SDU_PS_CLIENT_ID;

			RestTemplate restTemplate = new RestTemplate();
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String payload = mapper.writeValueAsString(sduShareDocumentsEmailNotificationVOList);

			HttpEntity<String> requestBody = new HttpEntity<>(payload, SDUHelper.getHttpHeaders());

			if (logger.isDebugEnabled()) {
				logger.debug("Json Payload : " + payload);
			}

			ResponseEntity<List<SDUShareDocumentsEmailNotificationVO>> responses = restTemplate.exchange(endpointURL,
					HttpMethod.PUT, requestBody,
					new ParameterizedTypeReference<List<SDUShareDocumentsEmailNotificationVO>>() {
					});
			if (responses.getStatusCode() == HttpStatus.OK) {

				if (logger.isDebugEnabled()) {
					logger.debug("Response received from updateEmailNotificationInfo endpoint, Status code:"
							+ responses.getStatusCode());
				}

				if (responses.getBody() != null) {

					Collection<SDUShareDocumentsEmailNotificationVO> sduShareDocumentsEmailNotificationResponse = responses
							.getBody();

					if (null != sduShareDocumentsEmailNotificationResponse) {
						for (SDUShareDocumentsEmailNotificationVO sduShareDocumentsEmailNotificationVO : sduShareDocumentsEmailNotificationResponse) {
							if (!sduShareDocumentsEmailNotificationVO.isUpdateSucceed()) {
								String reponse = mapper.writeValueAsString(sduShareDocumentsEmailNotificationVO);
								logger.error(
										"Error : SDU Email is sent, but the timestamp is not updated in database [RESPONSE JSON]: "
												+ reponse);
							}

						}
					}

				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Response is received as null from updateEmailNotificationInfo endpoint");
					}
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Error in getting response from updateEmailNotificationInfo endpoint, Status code:"
							+ responses.getStatusCode());
				}
			}

		}catch (JsonProcessingException jpe) {
			logger.error("Json request: " + jpe.getMessage());
			throw new SystemException("Json request: " + jpe.getMessage());
		} catch (Exception e) {
			logger.error("Exception in microservice call: " + e.getStackTrace());
			throw new SystemException("Exception in microservice call: " + e.getStackTrace());
		}
	
	}
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}