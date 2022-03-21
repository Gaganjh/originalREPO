package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionClientDataVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionMetaDataVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmitTabForm;
import com.manulife.pension.platform.web.util.SDUHelper;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.ContractEnrollmentVO;

/**
 * 
 * Handles the submission of a Document
 * 
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"sduSubmitTabForm"})
public class SDUSubmitTabController extends PsAutoController implements CommonConstants {

	@ModelAttribute("sduSubmitTabForm")
	public SDUSubmitTabForm populateForm() {
		return new SDUSubmitTabForm();
	}
	
	protected static Logger logger;
	private static final String SDU_SUBMIT_TAB_FORM="sduSubmitTabForm";	
	protected static final String TASK_KEY = "task";
    protected static final String PRINT_TASK = "print";
    protected static final String DEFAULT_TASK = "default";
	
	public static Map<String, String> forwards = new HashMap<>();
	static {
		logger = Logger.getLogger(SDUSubmitTabController.class);
		forwards.put("default", "/tools/sduSubmitTab.jsp");
		forwards.put("input", "/tools/sduSubmitTab.jsp");
		forwards.put("print", "/tools/sduSubmitTab.jsp");
		forwards.put("submitRedirect", "redirect:/do/tools/secureDocumentUpload/submit/");
		forwards.put("tools", "redirect:/do/tools/toolsMenu/");
		forwards.put("systemErrorPage","/error.jsp");
	}

	/**
	 * Constructor.
	 */
	public SDUSubmitTabController() {
		super(SDUSubmitTabController.class);		
	}
	
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }
	
    /**
     * This controller is to redirect the user to correct url do/tools/secureDocumentUpload/submit when the user enters the submit page using the bookmark
     * @param theForm
     * @param request
     * @param response
     */
	@RequestMapping(value = {"/sendDocumentUpload/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doRedirectToDefault(@ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, HttpServletRequest request,HttpServletResponse response) 
	 {
		return forwards.get("submitRedirect");		
	}
	
	@RequestMapping(value = {"/secureDocumentUpload/submit/error/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doRedirectToError(@ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, HttpServletRequest request,HttpServletResponse response) 
	 {
		return forwards.get(SYSTEM_ERROR_PAGE);		
	}
	
	/**
	 * controller for getting Oauth token via AJAX call
	 * @param theForm
	 * @param request
	 * @param response
	 * @return Token as text (response)
	 * @throws SystemException
	 * @throws IOException
	 */
	@RequestMapping(value = "/secureDocumentUpload/token/", method ={RequestMethod.POST}) 
	public String getAuthToken(@ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException
	 {	
		if (SessionHelper.getUserProfile(request)!=null) {
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("text/plain");
			byte[] jsonResultBytes = SDUHelper.getAccessToken().getBytes();
			response.setContentLength(jsonResultBytes.length);
			try {
	            response.getOutputStream().write(jsonResultBytes);
	        } catch (IOException ioException) {
	            throw new SystemException(ioException, "Exception in writing to response.");
	        } finally {
                response.getOutputStream().close();
	        }
			return null; 
		}
		else {
			 return forwards.get("default");
		}
	}
	
	@RequestMapping(value ="/secureDocumentUpload/submit/getSubmissionMetaData/" , method =  {RequestMethod.POST}) 
    public String getSubmissionMetaData ( @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException 
     {
		if (SessionHelper.getUserProfile(request)!=null) {
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("application/json");
			UserProfile userProfile = getUserProfile(request);
			new Gson().toJson(populateSubmissionMetaData(userProfile), response.getWriter());
			return null; 
		}
		else {
			return forwards.get(INPUT);			
		}		
    }
	
	@RequestMapping(value = {"/secureDocumentUpload/submit/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	@RequestMapping(value ="/secureDocumentUpload/submit/" ,params={"task=print"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
    public String doPrint ( @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException 
     {
		if (SessionHelper.getUserProfile(request)!=null && request.getParameter("printFriendly")==null) {
			response.setHeader("Cache-Control", "must-revalidate");
			response.setContentType("text/plain");
			byte[] jsonResultBytes = "Data set in form successfully.!".getBytes();
			response.setContentLength(jsonResultBytes.length);
			try {
	            response.getOutputStream().write(jsonResultBytes);
	        } catch (IOException ioException) {
	            throw new SystemException(ioException, "Exception in writing to response.");
	        } finally {
                response.getOutputStream().close();
	        }
			return null;
		}
		else {
			 return forwards.get(INPUT);
		}
		
    }
	
	protected SDUSubmissionMetaDataVO populateSubmissionMetaData(UserProfile userProfile) throws SystemException {
	
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
		sduSubmissionMetaDataVO.setShareable(false);
		sduSubmissionMetaDataVO.setShareInfo("");
		sduSubmissionMetaDataVO.setSubmissionDesc("");
		sduSubmissionMetaDataVO.setSubmissionStatus("staged");
		sduSubmissionMetaDataVO.setTargetSystem("AWD");		
		return sduSubmissionMetaDataVO;		
	}

	protected void populateForm(SDUSubmitTabForm theForm, UserProfile userProfile)	throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");

		boolean isExternalUser = userProfile.getPrincipal().getRole().isExternalUser();		
		theForm.setDisplayFileUploadSection(isExternalUser);		
		theForm.setWidgetEndpointURL(SDUHelper.getApigeeEdgeProxyURL());		
		
		if (logger.isDebugEnabled())
			logger.debug("exit <- populateForm");
	}	
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}