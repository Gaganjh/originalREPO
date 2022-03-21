package com.manulife.pension.bd.web.bob.secureDocumentUpload;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDPdfHelper;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionClientDataVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmissionMetaDataVO;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUSubmitTabForm;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.SDUHelper;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractEnrollmentVO;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;


@Controller
@RequestMapping(value ="/bob/secureDocumentUpload")
@SessionAttributes({"sduSubmitTabForm"})
public class SDUSubmitTabController extends BDController {
	
	@ModelAttribute("sduSubmitTabForm")
	public SDUSubmitTabForm populateForm() {
		return new SDUSubmitTabForm();
	}
	
	protected static Logger logger;
	private static final String SDU_SUBMIT_TAB_FORM="sduSubmitTabForm";	
	protected static final String TASK_KEY = "task";
    protected static final String PRINT_TASK = "print";
    protected static final String DEFAULT_TASK = "default";    
    private static final String XSLT_FILE_KEY_NAME = "SDUSubmitTabReport.XSLFile";
	
	public static Map<String, String> forwards = new HashMap<>();
	static {
		logger = Logger.getLogger(SDUSubmitTabController.class);
		forwards.put("default", "/secureDocumentUpload/sduSubmitTab.jsp");
		forwards.put("input", "/secureDocumentUpload/sduSubmitTab.jsp");
		forwards.put("submit","/secureDocumentUpload/sduSubmitTab.jsp");
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
		
		@RequestMapping(value = {"/submit/error/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
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
		
		
		@RequestMapping(value = "/token/", method ={RequestMethod.POST}) 
		public String getAuthToken(@ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException
		 {	
			if (BDSessionHelper.getUserProfile(request)!=null) {
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
		
		@RequestMapping(value ="/submit/getSubmissionMetaData/" , method =  {RequestMethod.POST}) 
	    public String getSubmissionMetaData ( @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException 
	     {
			if (BDSessionHelper.getUserProfile(request)!=null) {
				response.setHeader("Cache-Control", "must-revalidate");
				response.setContentType("application/json");				
				new Gson().toJson(populateSubmissionMetaData(request), response.getWriter());
				return null; 
			}
			else {
				return forwards.get("default");			
			}		
	    }
		

		protected void populateForm(SDUSubmitTabForm theForm, HttpServletRequest request)	throws SystemException {			
			if (logger.isDebugEnabled())
				logger.debug("entry -> populateForm");
			BobContext bob = BDSessionHelper.getBobContext(request);
			BDUserProfile userProfile = getUserProfile(request);					
			theForm.setPendingContract(SDUHelper.isPreActiveContract(bob.getCurrentContract().getStatus()));			
			boolean displayFileUpload=true;
			if(userProfile.getBDPrincipal().getBDUserRole().getRoleType().isInternal() || userProfile.isInMimic()) {
				displayFileUpload=false;
			}
			theForm.setDisplayFileUploadSection(displayFileUpload);		
			theForm.setWidgetEndpointURL(SDUHelper.getApigeeEdgeProxyURL());		
			
			if (logger.isDebugEnabled())
				logger.debug("exit <- populateForm");
		}
		
		@RequestMapping(value = {"/submit/"}, method ={RequestMethod.GET,RequestMethod.POST}) 
		public String doExecute(@Valid @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
			//BDUserProfile userProfile = getUserProfile(request);
			theForm.reset(request);
			request.getSession().removeAttribute(SDU_SUBMIT_TAB_FORM);
			populateForm(theForm, request);
			request.getSession().setAttribute(SDU_SUBMIT_TAB_FORM, theForm);
			if (logger.isDebugEnabled()) {
				logger.debug("exit <- doDefault");
			}					
			return forwards.get(CommonConstants.INPUT);		
		}
	
		protected SDUSubmissionMetaDataVO populateSubmissionMetaData(HttpServletRequest request) throws SystemException {
			
			BDUserProfile userProfile = getUserProfile(request);
			BobContext bob = BDSessionHelper.getBobContext(request) ;
			
			SDUSubmissionMetaDataVO sduSubmissionMetaDataVO = new SDUSubmissionMetaDataVO();
			int contractId = bob.getCurrentContract().getContractNumber();
			sduSubmissionMetaDataVO.setClientContract(String.valueOf(contractId));
			sduSubmissionMetaDataVO.setClientContractName(StringUtils.trim(bob.getCurrentContract().getCompanyName()));
			sduSubmissionMetaDataVO.setClientContractStatus(bob.getCurrentContract().getStatus());
			
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
			sduSubmissionClientDataVO.setClientUser(userProfile.getBDPrincipal().getFirstName()+" "+userProfile.getBDPrincipal().getLastName());			
			sduSubmissionClientDataVO.setSubmitterRole(BDUserRoleDisplayNameUtil.getInstance()
					.getDisplayName(userProfile.getBDPrincipal().getBDUserRole().getRoleType()));
			sduSubmissionClientDataVO.setSubmissionSource(SDUConstants.SDU_BD_SUBMISSION_SOURCE);
			ContactVO contactVO=ContractServiceDelegate.getInstance().getContactsDetail(contractId);
			if(null!=contactVO && null != contactVO.getName()) {
				sduSubmissionClientDataVO.setClientAccountRepresentative(StringUtils.trim(contactVO.getName()));
			}else {
				sduSubmissionClientDataVO.setClientAccountRepresentative(StringUtils.EMPTY);	
			}
			sduSubmissionClientDataVO.setClientApplicationName(SDUConstants.SDU_BD_CLIENT_APP_NAME);
			Gson gson = new GsonBuilder().create();		
			sduSubmissionMetaDataVO.setClientData(gson.toJson(sduSubmissionClientDataVO));
			sduSubmissionMetaDataVO.setClientId(SDUConstants.SDU_BD_CLIENT_ID);
			sduSubmissionMetaDataVO.setClientUser(String.valueOf(userProfile.getBDPrincipal().getProfileId()));
			sduSubmissionMetaDataVO.setClientUserName(sduSubmissionClientDataVO.getClientUser());
			sduSubmissionMetaDataVO.setClientUserRole(sduSubmissionClientDataVO.getSubmitterRole());
			sduSubmissionMetaDataVO.setShareable(false);
			sduSubmissionMetaDataVO.setShareInfo("");
			sduSubmissionMetaDataVO.setSubmissionDesc("");
			sduSubmissionMetaDataVO.setSubmissionStatus("staged");
			sduSubmissionMetaDataVO.setTargetSystem("AWD");	
			return sduSubmissionMetaDataVO;		
		}

	/**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 * 
	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
	@RequestMapping(value = "/submit/", params = { "task=print" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String doPrint(@ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm theForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException, IOException {
		if (BDSessionHelper.getUserProfile(request) != null) {
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
		} else {			
			return forwards.get("default");
		}
	}
	 
	 @RequestMapping(value = "/submit/",params = {"task=printPDF"}, method = {RequestMethod.GET})
		public String doPrintPDF(@Valid @ModelAttribute("sduSubmitTabForm") SDUSubmitTabForm form,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
		 	if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
				}
			}
			String forward = super.doPrintPDF(form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}	 
	
 	
	 	@Override
	    @SuppressWarnings("unchecked")
		 public Object prepareXMLFromReport(BaseReportForm form, ReportData report,
		            HttpServletRequest request) throws ParserConfigurationException {
		
	        PDFDocument doc = new PDFDocument();
	        SDUSubmitTabForm reportForm = (SDUSubmitTabForm)form;

	        // Gets layout page for transactionHistoryReport.jsp
	        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.SDU_SUBMIT_PATH, request);

	        Element rootElement = doc.createRootElement(BDPdfConstants.SUBMIT_TAB_DETAILS);

	        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
	        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
	        StringBuilder ContractInfo=new StringBuilder();
	        ContractInfo.append(String.valueOf(currentContract.getContractNumber()));
	        ContractInfo.append(" ");
	        ContractInfo.append(currentContract.getCompanyName());
	   
	        setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);
	        
	        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.PAGE_NAME, null);
	        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
	        String IntroTxt = StringUtils.EMPTY;
	        try {
				Miscellaneous content = (Miscellaneous) ContentCacheManager
						.getInstance().getContentById(BDContentConstants.SDU_SUBMIT_TAB_INTRO,
								ContentTypeManager.instance().MISCELLANEOUS);
				if (content != null) {
					IntroTxt = content.getText();
				}
			} catch (ContentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        PdfHelper.convertIntoDOM(BDPdfConstants.INTRO2_TEXT, rootElement, doc, IntroTxt);
	        String conformationJson= reportForm.getSubmissionConfirmationJson();
		       JsonObject jobj = new Gson().fromJson(conformationJson, JsonObject.class);
	        
		if (!conformationJson.isEmpty()) {

			// Transaction Details - start
			Element txnDetailsElement = doc.createElement(BDPdfConstants.TXN_DETAILS);
			Element txnDetailElement;

			txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
			// Sets main report.
			setReportDetailsXMLElements(doc, txnDetailElement, jobj);
			doc.appendElement(txnDetailsElement, txnDetailElement);

			doc.appendElement(rootElement, txnDetailsElement);
			// Transaction Details - end
		}
		
	       	 
		// Transaction Details - start
		Element txnDetailsElement1 = doc.createElement(BDPdfConstants.CONTRACT_TXN_DETAILS);
		Element txnDetailElement1;

		txnDetailElement1 = doc.createElement(BDPdfConstants.CONTRACT_TXN_DETAIL);
		// Sets main report.
		doc.appendTextNode(txnDetailElement1, BDPdfConstants.CONTRACT_NUM, ContractInfo.toString());
		doc.appendElement(txnDetailsElement1, txnDetailElement1);

		doc.appendElement(rootElement, txnDetailsElement1);
		// Transaction Details - end

		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		return doc.getDocument();

	    }
	 
	 private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement,
	    		JsonObject theItem) {
		 StringBuilder contractInfo=new StringBuilder();
	        if (theItem != null) {
	        	contractInfo.append(theItem.get("contractNumber").getAsString()).append(" ");
	        	contractInfo.append(theItem.get("companyName").getAsString());
	        	
	        	doc.appendTextNode(txnDetailElement, BDPdfConstants.CONTRACT_NO,contractInfo.toString());
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.SUBMISSION_NUMBER, theItem.get("submissionNumber").getAsString()
	                    		);
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.SUBMISSION_TIME, theItem.get("submissionTime").getAsString());
	            
	            doc.appendTextNode(txnDetailElement, BDPdfConstants.SUBMITTER_NAME,theItem.get("submitterName").getAsString());
	            
	            JsonArray jsonArray =theItem.get("fileNames").getAsJsonArray();	            
	           
	            for (int i = 0; i < jsonArray.size(); i++) {
	        		String  geometryElement = jsonArray.get(i).getAsString();	        		
	        		doc.appendTextNode(txnDetailElement, BDPdfConstants.SUBMISSION_FILES,geometryElement);
	        	}
	            
	        }
	    }
	 protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
	    	 ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    		HttpServletRequest req = attr.getRequest();
	    	BDLayoutBean bean = ApplicationHelper.getLayoutStore(req.getServletContext()).getLayoutBean(id, request);
	        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
	        return layoutPageBean;
	    }
	 @Override
	 public String getXSLTFileName() {
	        return XSLT_FILE_KEY_NAME;
	    }
	 
	 protected void setFooterXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
	            Element rootElement, HttpServletRequest request, String[]... params) {
	        Location location = ApplicationHelper.getRequestContentLocation(request);
	        BDPdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
	                BDContentConstants.BD_GLOBAL_DISCLOSURE, params);

	    }
	
	 
		
}