package com.manulife.pension.ps.web.noticemanager;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.DocumentServiceDelegate;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.DocumentInfo;
import com.manulife.pension.documents.model.PlanNoticeDocumentInfo;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.util.MerillInteractionNoticeManager;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails.UserIdType;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.template.valueobject.DocumentData;
import com.manulife.pension.service.template.valueobject.RequestCriteria;
import com.manulife.pension.util.content.GenericException;
import javax.ejb.EJBException;
import org.apache.log4j.Category;
import com.manulife.pension.util.log.ServiceLogRecord;


/**
 * Build your Package action to prepare the package for merrill
 * @author kumarye
 * 
 *
 */
@Controller
@RequestMapping(value="/noticemanager")
@SessionAttributes({"buildYourPackageForm"})

public class BuildYourPackageNoticeManagerController extends ReportController {

	@ModelAttribute("buildYourPackageForm") 
	public BuildYourPackageNoticeManagerForm populateForm() 
	{
		return new BuildYourPackageNoticeManagerForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/noticemanager/buildYourPackage.jsp");
		forwards.put("default","/noticemanager/buildYourPackage.jsp");
		forwards.put("buildyourpackage","/noticemanager/buildYourPackage.jsp");
		forwards.put("uploadAndShare","redirect:/do/noticemanager/uploadandsharepages/");
		forwards.put("secureHomePage","redirect:/do/home/homePage/");
		forwards.put("termsandconditions","redirect:/do/noticemanager/termsandconditions/");}

	private static final Logger logger = Logger.getLogger(MerillInteractionNoticeManager.class);
	private static final String ORDER_ACTION_LABLE = "order";
	private static final String DOWNLOAD_ACTION_LABLE = "download";	
	private static final String BUILD_YOUR_PACKAGE_ACTION = "buildyourpackage";
	private static final String BLANK = "";	
	private static final String COMMA = ",";
    private static final String GENERATED_PDF ="isReportGenerated";
    private static final String PDF_GENERATED = "pdfGenerated";
    private static final String PDF_NOT_GENERATED = "pdfNotGenerated";
    private static final String ERRORS_INDICATOR = "errorsIndicator";
    private static final String EREPORTS_FAILURE = "ereportsFailure";
    private static final String MERGE_FAILURE = "mergeFailure";
    private static final String ICC_DOCUMENT_NAME = "icc";
    private static final String PLAN_INVESTMENT_DOCUMENT_NAME = "planinvestment";
    private static final String PLAN_HIGHLIGHTS_DOCUMENT_NAME = "planhighlight";
    private static final String BUILD_YOUR_REPORT_DATA = "buildReportData";
    private static final String GENERATED_CENSUS_NAME ="isCensusGenerated";
    private static final String CENSUS_FILE_TYPE_NEW = "new";
    private static final String CENSUS_FILE_TYPE_EXISTING = "existing";
    private static final String CENSUS_FILE_TYPE_EXTENSION_XLS = ".xls";
    private static final String CENSUS_FILE_TYPE_EXTENSION_XLSX = ".xlsx";
    private static final String CENSUS_FILE_TYPE_EXTENSION_CSV = ".csv";
    private static final String DOCUMENT_FILE_NAME = "documentFileName";
    private static final String ACTION_PERFORMED = "actionPerformed";    
    private static final String UPLOAD_FINDER_FORWARD_REDIRECT = "uploadAndShare";    
    private static final String BUILD_FORM_SUBMITTED_TO_MERRILL = "buildFormMerrill";
	private static final String CUSTOM_SORT_ARROW = "up";
	private static final String GENERATED_EMPLOY_NAME ="isEmployGenerated";
	private static final String EMPLOYEE_FILE_TYPE_EXISTING = "employee";
	private static final String ELIGIBLE_EMPLOYEE_COLUMN_HEADING = "FirstName,LastName,Address1,Address2,City,State,ZipCode,Country";
    
    /**
     * Required variables for generating planNotice byte array
     */
	private static final String TOP_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_404_LOGO_98X40.jpg";
	private static final String BOTTOM_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_blue.jpg";
	private static final Integer inputDocumentFormat = new Integer(1);
	private static final String PLAN_VO_LIST_LENGTH = "planVoListLenght";
	private static final String LOGGED = "BUILD_YOUR_PACKAGE";
	private Category interactionLog = Category.getInstance(ServiceLogRecord.class);
	private ServiceLogRecord record = new ServiceLogRecord("doOrder");
	private Category generalLog = Category.getInstance(getClass());
	
	@RequestMapping(value ="/buildyourpackage/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	 * This method will identify the user selected JH and Custom documents and 
	 * merges them using document service and keeps the generated PDF in to session for further download. 
	 * 
	 * @param form
	 *            BuildYourPackageNoticeManagerForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param responseC:
	 *            HttpServletResponse objects reference
	 * 
	 * @return String
	 */
	@RequestMapping(value ="/buildyourpackage/",params= {"task=download"}, method =  {RequestMethod.POST}) 
	public String doDownload (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		
		
		form.setErrorsInd(false);
		Collection errors = validateForm( form, request);	
		if(errors.size()>0){
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			form.setErrorsInd(true);
            setDownloadErrorsInSession(request, errors);
			try {
				response.getWriter().write(ERRORS_INDICATOR);
				return null;
			} catch (IOException e) {
				throw new SystemException(e, "Unable to write the response back - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
			}
		}
		//Updating the order user has sorted
		String documentorderstring[] = form.getDocumentsOrder().split(COMMA);
	    List<Integer> documentOrderList = new ArrayList<Integer>();
		for(int documentIdIndex = 0; documentIdIndex< documentorderstring.length; documentIdIndex++){
			if(!documentorderstring[documentIdIndex].isEmpty()){ 
				documentOrderList.add(new Integer(documentorderstring[documentIdIndex]));
			}
		}
		PlanNoticeDocumentServiceDelegate.getInstance().updateCustomPlanNoticeDocumentOrder(documentOrderList);
		
		errors = generateMergedPDF( response, request,  form);
		if(errors.size()>0){
            try {
    			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
    			form.setErrorsInd(true);
                setDownloadErrorsInSession(request, null);
				response.getWriter().write(ERRORS_INDICATOR);
			} catch (IOException e) {
				throw new SystemException(e, "Unable to write the response back - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
			}
		}
		// This loop would be executed when we get a null errors object, this is written for ereports  lookup and dofument merge failure
		// As per business, Since we have to show new a custom document deleted error this will not execute 
		else if(mergedPDF == null){
			form.setErrorsInd(false);
			try {
				if(form.getEreportError()){
					response.getWriter().write(EREPORTS_FAILURE);
				}else{
					response.getWriter().write(MERGE_FAILURE);
				}
			} catch (IOException e) {
				throw new SystemException(e, "Unable to write the response back - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
			}
		}
		else{
			try {
				form.setErrorsInd(false);
				UserProfile userProfile = SessionHelper.getUserProfile(request);
				int contractId = userProfile.getCurrentContract().getContractNumber();
				String documentName = "bundle_"+ contractId + ".pdf";
				HttpSession session = request.getSession(false);
				response.getWriter().write(PDF_GENERATED);
				session.setAttribute(GENERATED_PDF, new Handle(mergedPDF));
				session.setAttribute(DOCUMENT_FILE_NAME, documentName);
				String userAction = CommonConstants.NOTICE_DOWNLOAD;
				BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
				PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			} catch (Exception e) {
				try {
					response.getWriter().write(PDF_NOT_GENERATED);
				} catch (IOException e1) {
					throw new SystemException(e, "Unable to write the response back - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
					
				}
				request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
				form.setErrorsInd(true);
	            setDownloadErrorsInSession(request, null);
			}
		}
		// setting the errors finally after 
		request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
		setDownloadErrorsInSession(request, errors);
		request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
		return null;

	}

	/**
	 * This method will get the eligibility employee address file related records and it downloads csv file
	 * 
	 * @param form
	 *            BuildYourPackageNoticeManagerForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param responseC:
	 *            HttpServletResponse objects reference
	 * 
	 * @return String
	 */
	
	@RequestMapping(value = "/buildyourpackage/", params = { "task=downloadEligibleEmployeeAdressFiles" }, method = { RequestMethod.GET})
	public String doDownloadEligibleEmployeeAdressFiles(@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 

		StringBuffer buildYourPackageNoticeManagerBuffer = new StringBuffer();
		buildYourPackageNoticeManagerBuffer.append(ELIGIBLE_EMPLOYEE_COLUMN_HEADING);
		buildYourPackageNoticeManagerBuffer.append("\n");
				
		List<EmployeeEligibleVO> eligibleEmployeeAddressList =
				PlanNoticeDocumentServiceDelegate.getInstance().getEligibleEmployeeDetails(getUserProfile(request).getCurrentContract().getContractNumber());

		if (!eligibleEmployeeAddressList.isEmpty() && eligibleEmployeeAddressList.size() > 0) {
			for (EmployeeEligibleVO participantAddressVO : eligibleEmployeeAddressList) {

				buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantFirstName())).append(COMMA);
				buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantLastName())).append(COMMA);

				if (participantAddressVO.getParticiapantAddressLine1() != null)
					buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantAddressLine1().trim()));
				buildYourPackageNoticeManagerBuffer.append(COMMA);

				if (participantAddressVO.getParticiapantAddressLine2() != null)
					buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantAddressLine2().trim()));
				buildYourPackageNoticeManagerBuffer.append(COMMA);

				if (participantAddressVO.getParticiapantCity() != null)
					buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantCity().trim()));
				buildYourPackageNoticeManagerBuffer.append(COMMA);

				if (participantAddressVO.getParticiapantState() != null)
					buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantState().trim()));
				buildYourPackageNoticeManagerBuffer.append(COMMA);

				if (participantAddressVO.getParticiapantZip() != null) {
					String zipCode = StringUtils.trim(participantAddressVO.getParticiapantZip());
					if (zipCode.length() > 0) {
						buildYourPackageNoticeManagerBuffer.append(zipCode.toUpperCase());
					}
				}

				buildYourPackageNoticeManagerBuffer.append(COMMA);

				if (participantAddressVO.getParticiapantCountry() != null)
					buildYourPackageNoticeManagerBuffer.append(escapeField(participantAddressVO.getParticiapantCountry().trim()));
				buildYourPackageNoticeManagerBuffer.append(COMMA);
				buildYourPackageNoticeManagerBuffer.append("\n");

			}
		} 

		setFileToResponse(request, response,
				"Eligible_Employee_Address_" + getUserProfile(request).getCurrentContract().getContractNumber()
						+"_"+new SimpleDateFormat("MMddyyyy").format(new Date()) + ".csv",
				buildYourPackageNoticeManagerBuffer.toString().getBytes(), "text/csv");

		return null;

	}
	
	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
	
	/**
	 * Writes the byte[] to the response stream
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param downloadData
	 * @param contentType
	 * 
	 * @throws SystemException
	 */
	
	private void setFileToResponse(HttpServletRequest request, HttpServletResponse response, String fileName,
			byte[] downloadData, String contentType) throws SystemException {
		logDebug("entry -> setFileToResponse");
		response.setHeader("Cache-Control", "must-revalidate");
		response.setContentType(contentType);

		String userAgent = request.getHeader("User-Agent");

		if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		}

		response.setContentLength(downloadData.length);
		try {
			response.getOutputStream().write(downloadData);
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing downloadData.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException, "Exception closing output stream.");
			}
		}
		logDebug("exit <- setFileToResponse");
	}
	

	/**
	 * This is the method executed to place the order to Merill.
	 * @param form
	 *            BuildYourPackageNoticeManagerForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param response
	 *            HttpServletResponse objects reference
	 * 
	 * @return String
	 * @throws Exception 
	 * @throws ClientProtocolException 
	 */
	
	
	
	@RequestMapping(value ="/buildyourpackage/",params= {"task=order"},method =  {RequestMethod.POST}) 
	public String doOrder (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		
		byte[] generatedCensusFile = null;
		
		Collection errors = validateForm( form, request);	
		if(errors.size()>0){
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			form.setErrorsInd(true);
            setDownloadErrorsInSession(request, null);
            return forwards.get("input");
            		
		}
		//Updating the order user has sorted
		String documentorderstring[] = form.getDocumentsOrder().split(COMMA);
        List<Integer> documentOrderList = new ArrayList<Integer>();
		for(int documentIdIndex = 0; documentIdIndex< documentorderstring.length; documentIdIndex++){
			if(!documentorderstring[documentIdIndex].isEmpty()){
				documentOrderList.add(new Integer(documentorderstring[documentIdIndex]));
			}
		}
		
		// census file generation and merging file into session
		errors = generateMergedPDF( response, request,  form);
		// check whether any exception occurred while  generating document.
		if(errors.size()>0){
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			form.setErrorsInd(true);
            setDownloadErrorsInSession(request, null);
            return forwards.get("input");
		}
		// This code will be executed when a merge failure happens. Error message will be poped out to the user in font end using the JS which will be executed if the below indicator is set
		// As per new business requirement, Since we have to show new a custom document deleted error hence this will not be execute
		else if(mergedPDF == null){
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			form.setErrorsInd(false);
            setDownloadErrorsInSession(request, null);
			if(!form.getEreportError()){
				form.setMergeError(true);
			}
		}
		else{
			String addressFileOptionSelection = "";
			HttpSession session = request.getSession(false);
			String addressFileName = "addressFile.csv";
			// Fetching the generated census in sessionif the user specifies to get the existing census information
			if(CENSUS_FILE_TYPE_EXISTING.equals(form.getFileType())){
				generatedCensusFile = (byte[]) session.getAttribute(GENERATED_CENSUS_NAME);
				addressFileOptionSelection = "Census";
			}else if(EMPLOYEE_FILE_TYPE_EXISTING.equals(form.getFileType())){
				generatedCensusFile = (byte[]) session.getAttribute(GENERATED_EMPLOY_NAME);
				addressFileOptionSelection = "Eligible";
			}else{
				generatedCensusFile = form.getCensusFile().getBytes();
				addressFileName = form.getCensusFile().getOriginalFilename();
				addressFileOptionSelection = "Upload";
			}
			PlanNoticeDocumentServiceDelegate.getInstance().updateCustomPlanNoticeDocumentOrder(documentOrderList);
			PlanNoticeMailingOrderVO planNoticeMailingOrderVO = new PlanNoticeMailingOrderVO();
			UserProfile userProfile = SessionHelper.getUserProfile(request);
			int contractId = userProfile.getCurrentContract().getContractNumber();
			
			planNoticeMailingOrderVO.setOrderNumber(PlanNoticeDocumentServiceDelegate.getInstance().getMaxDocumentOrder());
			planNoticeMailingOrderVO.setContractId(contractId);
			planNoticeMailingOrderVO.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId()));
			planNoticeMailingOrderVO.setMailingName(userProfile.getEmail());
			planNoticeMailingOrderVO.setColorPrintInd("Y");
			planNoticeMailingOrderVO.setTotalPageCount(form.getCountOfPagesInPDFGenerated());
			planNoticeMailingOrderVO.setNoOfParticipant(form.getCountOfParticipantInCensusGenerated());
			planNoticeMailingOrderVO.setTotalMailingCost(new BigDecimal(0));
			LookupDescription planNoticeMailingOrderStatus = new LookupDescription();
			planNoticeMailingOrderStatus.setLookupCode("IN");
			planNoticeMailingOrderVO.setPlanNoticeMailingOrderStatus(planNoticeMailingOrderStatus );
			planNoticeMailingOrderVO.setMailingName(form.getMailingName());
			planNoticeMailingOrderVO.setAdressFileOption(addressFileOptionSelection);
			// getting address from contract service 
			Collection<Address> contractAddresses = ContractServiceDelegate
			.getInstance().getContractAddresses(contractId);
			
			String mailAddressBuffer = BLANK;
			if(contractAddresses != null) {
		        for (Address address : contractAddresses) {
		        	if(!address.getType().getCode().equals("M")){
		        		continue;
		        	}
		        	if (!StringUtils.isBlank(address.getLine1())) {
		        		mailAddressBuffer = address.getLine1()+", ";
		        	}
		            if (StringUtils.isNotBlank(address.getLine2())) {
		            	mailAddressBuffer +=  address.getLine2()+", " ;
		            }
		            
		            if (StringUtils.isNotBlank(address.getCity())) {
		            	mailAddressBuffer += address.getCity();
		            }
		            if (StringUtils.isNotBlank(address.getStateCode())) {
		            	mailAddressBuffer += " " + address.getStateCode();
		            }
		            if (StringUtils.isNotBlank(address.getZipCode())) {
		            	mailAddressBuffer += " " + address.getZipCode();
		            }
		            break;
		        }
	    	}
			planNoticeMailingOrderVO.setMailingAddress(mailAddressBuffer);
			PlanNoticeDocumentServiceDelegate.getInstance().insertContractNoticeMailingOrder(planNoticeMailingOrderVO);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
			logInteraction(userProfile, BuildYourPackageNoticeManagerController.class.getName() + ":doOrder",
					"USERNAME: " + userProfile.getPrincipal().getUserName() + ", ORDER_ID: "
							+ planNoticeMailingOrderVO.getOrderNumber() + ", UPDATED_TS: "
							+ dateFormatter.format(new Date()));
			try{
				session.setAttribute(BUILD_FORM_SUBMITTED_TO_MERRILL, form);
				Collection merrillErrors = MerillInteractionNoticeManager.getNoticeManagerUploadedAccessPermission(request,  response,mergedPDF, generatedCensusFile, userProfile, planNoticeMailingOrderVO,addressFileName);
				logger.error("Merrill status in BYP Action: " + (merrillErrors.size() > 0));
				if(merrillErrors.size() > 0){
					planNoticeMailingOrderStatus.setLookupCode("ER");
					planNoticeMailingOrderVO.setPlanNoticeMailingOrderStatus(planNoticeMailingOrderStatus );
					PlanNoticeDocumentServiceDelegate.getInstance().updateContractNoticeMailingOrderStatus(planNoticeMailingOrderVO);
					
					request.setAttribute(PsBaseUtil.ERROR_KEY, merrillErrors);
					form.setErrorsInd(true);
					postExecute(form, request, response);
					return forwards.get(BUILD_YOUR_PACKAGE_ACTION);
				} else{
					form.setErrorsInd(false);
				}
			}
			catch (Exception e) {
				errors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
                setErrorsInRequest(request, errors);
                form.setErrorsInd(true);
                postExecute(form, request, response);
				return forwards.get(BUILD_YOUR_PACKAGE_ACTION);
			}
		}
	    return null;
	}
	


	/**
	 * To perform sort action on clicking the sort arrow buttons 
	 * @param form
	 * @param request
	 * @param response
	 * @return string
	 * @throws SystemException
	 */
	@RequestMapping(value ="/buildyourpackage/",params= {"task=customSort"},method =  {RequestMethod.POST}) 
	public String doCustomSort (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		PlanDocumentReportData reportData = null;
		
		form.setSortAppliedInd(true);
		Integer documentId = form.getDocumentId();
		int documentIndex=0;
		String sortTypeArrowAndId = form.getSortTypeArrow();
		String sortTypeArrow = sortTypeArrowAndId.split(",")[0];
		documentId = new Integer (sortTypeArrowAndId.split(",")[1]);
		reportData = form.getReport();
		List<PlanNoticeDocumentVO> planVoList = new ArrayList<PlanNoticeDocumentVO>();
		planVoList.addAll(reportData.getCustomPlanNoticeDocuments());
		for(PlanNoticeDocumentVO planVo : planVoList){
			if(planVo.getDocumentId().equals(documentId)){
				documentIndex =	planVoList.indexOf(planVo);
			}
		}
		if(CUSTOM_SORT_ARROW.equalsIgnoreCase(sortTypeArrow)){
			if(documentIndex !=0){
				Collections.swap(planVoList, documentIndex, documentIndex - 1);
			}
		} else{
			if(documentIndex != planVoList.size()-1){
				Collections.swap(planVoList, documentIndex, documentIndex + 1);
			}
		}
		form.setSortAppliedInd(true);
		reportData.setCustomPlanNoticeDocuments(planVoList);
		form.setReport(reportData);
		request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
		postExecute(form, request, response);
		return forwards.get(BUILD_YOUR_PACKAGE_ACTION);
	}

	

	

	/**
	 * This method handles fetching of the individual statements from the service and open them in PDF format 
	 * as attachment in the defined filename
	 * 
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return String
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/buildyourpackage/",params= {"task=fetchPdf"},method =  {RequestMethod.GET}) 
	public String doFetchPdf (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		
		HttpSession session = request.getSession(false);
		byte [] download = null;
		if(session.getAttribute(GENERATED_PDF) != null) {
			download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
			session.setAttribute(GENERATED_PDF, null);
		}else {
			doDownload( form, request, response);
			download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
			session.setAttribute(GENERATED_PDF, null);
		}
		if (download != null && download.length > 0) {
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", (String) session.getAttribute(DOCUMENT_FILE_NAME), download);
		}	
		
		
		return null;
	}

	

	/**
	 * This method populates the errors set
	 * 
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/buildyourpackage/",params= {"task=populateErrors"}, method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doPopulateErrors (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
 
		request.setAttribute(PsBaseUtil.ERROR_KEY, request.getSession(false).getAttribute(PsBaseUtil.ERROR_KEY));
		request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, null);
		postExecute(form, request, response);
		return forwards.get("input");
	}

	/**
	 * This is the method executed to reset the current data in the session.
	 * 
	 * @param reportForm
	 *            BaseReportForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param response
	 *            HttpServletResponse objects reference
	 * 
	 * @return String
	 */
	@RequestMapping(value ="/buildyourpackage/", params= {"task=reset"}, method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
			
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		
		form.reset();
		postExecute(form, request, response);
	    return doCommon(form, request,response);

	}

	/**
	 * This is the method executed to reset the current data in the session.
	 * 

	 * @param reportForm
	 *            BaseReportForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param response
	 *            HttpServletResponse objects reference
	 * 
	 * @return String
	 */
	@RequestMapping(value ="/buildyourpackage/",params= {"task=termsOfUse"},  method =  {RequestMethod.POST}) 
	public String doTermsOfUse (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		HttpSession session = request.getSession(false);
		session.setAttribute(ACTION_PERFORMED, "build");
		postExecute(form, request, response);
	    return forwards.get("termsandconditions");
	}

	/**
	 * This method populates the errors set
	 * 
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/buildyourpackage/",params= {"task=submitToSession"},method =  {RequestMethod.POST}) 
	public String doSubmitToSession (@Valid @ModelAttribute("buildYourPackageForm") BuildYourPackageNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
        	}
        }  
		try {
			response.getWriter().write("submitted");
		} catch (IOException e) {
			throw new SystemException(e, "Unable to write the response back - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
		}
		return null;
	}
	/**
	 * This method will validate the submission of the download request and the place order request. 
	 *  
	 *           
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection
	 * @throws SystemException 
	 */
	@SuppressWarnings("unchecked")
	protected Collection validateForm(ActionForm form,
			HttpServletRequest request) throws SystemException {
		/**
		 * Errors will get set in this method
		 * But the mechanism is different as we are using Ajax to generate document.
		 * Firstly background Ajax call would be made to doFetchParticipantAddress and DoDownload which will generate the merged document
		 * While generating if something goes wrong we have to show the error message to user which would be done using the indicator. 
		 */
		
		Collection errors = super.doValidate(form, request);
		BuildYourPackageNoticeManagerForm buildyourpackageform = (BuildYourPackageNoticeManagerForm) form;		
		if (buildyourpackageform.getTask().equals(DOWNLOAD_ACTION_LABLE)) {
			if(buildyourpackageform.getDocumentsSelected().isEmpty() && buildyourpackageform.getDocumentsSelectedJH().isEmpty()){
					errors.add(new GenericException(
							ErrorCodes.NMC_BUILD_PLEASE_SELECT_DOCUMENT_TO_DOWNLOAD));
			} else {
				try {
					if(!buildyourpackageform.getDocumentsSelected().isEmpty() && !validateCustomPlanDocument(buildyourpackageform.getDocumentsSelected())){
						errors.add(new GenericException(
								ErrorCodes.NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD));
					}
					UserProfile userProfile = getUserProfile(request);
					if(!buildyourpackageform.getDocumentsSelectedJH().isEmpty() && !NoticeManagerUtility.validateJHDocumentsWhileMerging(buildyourpackageform.getDocumentsSelectedJH(), userProfile)){
						errors.add(new GenericException(
								ErrorCodes.NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER));
					}
				} catch (SystemException e) {
					throw new SystemException(e, 
							"Something went wrong while validating document presence -  Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
				}
			
			}
		}
		if (buildyourpackageform.getTask().equals(ORDER_ACTION_LABLE)) {
			if(!buildyourpackageform.getCensusInfoPresent() && !(CENSUS_FILE_TYPE_NEW.equals(buildyourpackageform.getFileType())
																	||BLANK.equals(buildyourpackageform.getFileType()))){
				errors.add(new GenericException(
						ErrorCodes.NMC_BUILD_NO_CONTACT_INFO_PRESENT));
			}
			if(buildyourpackageform.getDocumentsSelected().isEmpty() && buildyourpackageform.getDocumentsSelectedJH().isEmpty()){
				errors.add(new GenericException(
						ErrorCodes.NMC_BUILD_PLEASE_SELECT_DOCUMENT_TO_DOWNLOAD));
			}else{
				try {
					if(!buildyourpackageform.getDocumentsSelected().isEmpty() && !validateCustomPlanDocument(buildyourpackageform.getDocumentsSelected())){
						errors.add(new GenericException(
								ErrorCodes.NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD));
					}
					UserProfile userProfile = getUserProfile(request);
					if(!buildyourpackageform.getDocumentsSelectedJH().isEmpty() && !NoticeManagerUtility.validateJHDocumentsWhileMerging(buildyourpackageform.getDocumentsSelectedJH(), userProfile)){
						errors.add(new GenericException(
								ErrorCodes.NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER));
					}
				} catch (SystemException e) {
					throw new SystemException(e, 
							"Something went wrong while validating document presence -  Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
				}
			}
			if(buildyourpackageform.getMailingName().isEmpty()){
				errors.add(new GenericException(
						ErrorCodes.NMC_BUILD_CENSUS_MAILING_NAME_EMPTY));
			}
			if(buildyourpackageform.getFileType().equals(BLANK)){
				errors.add(new GenericException(
						ErrorCodes.NMC_BUILD_CENSUS_UPLOAD_FILE_NOT_SELECTED));
			}
			else if(buildyourpackageform.getFileType().equals(CENSUS_FILE_TYPE_NEW)){
				Boolean censusfileNewErrorsIsNotSet = true;
				if(buildyourpackageform.getCensusFile() == null || buildyourpackageform.getCensusFile().getSize() == 0){
					errors.add(new GenericException(
							ErrorCodes.NMC_BUILD_CENSUS_UPLOAD_FILE_EMPTY));
					censusfileNewErrorsIsNotSet = false;
				}
				else if(!buildyourpackageform.getCensusFile().getOriginalFilename().endsWith(CENSUS_FILE_TYPE_EXTENSION_XLS)
						&& !buildyourpackageform.getCensusFile().getOriginalFilename().endsWith(CENSUS_FILE_TYPE_EXTENSION_XLSX)
						&& !buildyourpackageform.getCensusFile().getOriginalFilename().endsWith(CENSUS_FILE_TYPE_EXTENSION_CSV)
				){
					errors.add(new GenericException(
							ErrorCodes.NMC_BUILD_CENSUS_UPLOAD_FILE_FORMAT));
					censusfileNewErrorsIsNotSet = false;
				}
				// Adding error when census file is selected and valid but still their are some other errors
				// Due to which we have to show the user to select the census file again
				if (errors.size() > 0 && censusfileNewErrorsIsNotSet ) {
					errors.add(new GenericException(
							ErrorCodes.NMC_BUILD_CENSUS_UPLOAD_FILE_EMPTY));
				}
			}
		}
		
		if (errors.size() > 0) {
			buildyourpackageform.setErrorsInd(true);
		}else{
			buildyourpackageform.setErrorsInd(false);
		}
		return errors;
	}
	
	
	
	/**
	 * This method will validate the submission of the download request and the place order request. 
	 *  
	 *            
	 * @param form
	 *            BuildYourPackageNoticeManagerForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection
	 * @throws SystemException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings("unchecked")
	protected Collection generateMergedPDF(HttpServletResponse response, HttpServletRequest request, 
			BuildYourPackageNoticeManagerForm formData) throws NumberFormatException, SystemException {
		
		// If errors has not been raised continue with code
		Collection errors = new ArrayList();
		DocumentFileOutput documentFileOutput = null;
		PlanNoticeDocumentVO planNoticeDocumentVO = null;
		DocumentInfo documentInfo = null;
		List<DocumentData> documentList= new ArrayList<DocumentData>();
		HashMap<Integer,byte[]> mergedDocument = null;
		byte[] downloadData = null;
		
		RequestCriteria requestCriteria = new RequestCriteria();
		List<Integer> outputFormat = new ArrayList<Integer>();
		if(!formData.getDocumentsSelected().isEmpty()){
			String documentSelectedstring[] = formData.getDocumentsSelected().split(COMMA);
	        for(int documentIdIndex = 0; documentIdIndex< documentSelectedstring.length; documentIdIndex++){
				planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.getInstance().getCustomPlanNoticeDocumentInfo(
						new Integer(documentSelectedstring[documentIdIndex]));
				if (planNoticeDocumentVO.getDocumentFileName() != null) {
					UserProfile userprofile=SessionHelper.getUserProfile(request);
					Integer contractId = userprofile.getCurrentContract().getContractNumber();
					documentInfo = new PlanNoticeDocumentInfo(String.valueOf(contractId),planNoticeDocumentVO.getDocumentFileName(), BLANK);
					try {
						documentFileOutput = EReportsServiceDelegate.getInstance().getDocument(documentInfo);
						downloadData = documentFileOutput.getReportFragment();
					} catch (ServiceUnavailableException e) {
						logger.error("Ereports service un available - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
						errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD));
						formData.setEreportError(true);
						return errors;
					}catch (Exception e) {
						formData.setDownloadExceptionCached(true);
						errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD));
						return errors;
					}
					if (downloadData != null && downloadData.length > 0) {
						documentList.add(new DocumentData(downloadData, inputDocumentFormat,documentList.size()+1));
					}
				}
			}
		}
		if(!formData.getDocumentsSelectedJH().isEmpty()){
	        String documentSelectedJHstring[] = formData.getDocumentsSelectedJH().split(COMMA);
	        for(int documentJHIdIndex = 0; documentJHIdIndex< documentSelectedJHstring.length; documentJHIdIndex++){
	        	if(documentSelectedJHstring[documentJHIdIndex].equals(PLAN_HIGHLIGHTS_DOCUMENT_NAME)){
	        		UserProfile userprofile=SessionHelper.getUserProfile(request);
					try{
					downloadData =  NoticeManagerUtility.doGetSphPdf(formData,request,response,userprofile);
					} catch (Exception e) {
						logger.error("Fetching "+ PLAN_HIGHLIGHTS_DOCUMENT_NAME + "failed  - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
						errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER));
						return errors;
					} 
					if (downloadData != null && downloadData.length > 0) {
						documentList.add(new DocumentData(downloadData, inputDocumentFormat,documentList.size()+1));
					}
	        	}
	        	if(documentSelectedJHstring[documentJHIdIndex].equals(ICC_DOCUMENT_NAME)){
	        		UserProfile userprofile=SessionHelper.getUserProfile(request);
					Integer contractId = userprofile.getCurrentContract().getContractNumber();
					FeeDisclosureUserDetails context = getfeeDisclosureUserDetails(userprofile);
					
					UserAccess access = null;
					if(userprofile.isTPA()){
						access = UserAccess.TPA;
					}else if (userprofile.isInternalUser()){
						access = UserAccess.INTERNAL_USER;
					}else if (userprofile.getRole().isPlanSponsor()){
						access = UserAccess.PSW;
					}
					try {
						downloadData =  FundServiceDelegate.getInstance().getIccFileStream(
						            contractId,
						            TOP_LOGO_IMAGE_PATH,
						            BOTTOM_LOGO_IMAGE_PATH,
						            context, access);
					} catch (Exception e) {
						logger.error("Fetching "+ ICC_DOCUMENT_NAME + " failed - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
						errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER));
						return errors;
					} 
					if (downloadData != null && downloadData.length > 0) {
						documentList.add(new DocumentData(downloadData, inputDocumentFormat,documentList.size()+1));
					}
	        	}
	        	if(documentSelectedJHstring[documentJHIdIndex].equals(PLAN_INVESTMENT_DOCUMENT_NAME)){
			        UserProfile userProfile = SessionHelper.getUserProfile(request);
					int contractId = userProfile.getCurrentContract().getContractNumber();
					FeeDisclosureUserDetails context = getfeeDisclosureUserDetails(userProfile);
					
					UserAccess access = null;
					if(userProfile.isTPA()){
						access = UserAccess.TPA;
					}else if (userProfile.isInternalUser()){
						access = UserAccess.INTERNAL_USER;
					}else if (userProfile.getRole().isPlanSponsor()){
						access = UserAccess.PSW;
					}
					try{
					downloadData =  FundServiceDelegate.getInstance().getPlanAndInvestmentNoticeFileStream(
			                contractId, TOP_LOGO_IMAGE_PATH, BOTTOM_LOGO_IMAGE_PATH, context, access);
					} catch (Exception e) {
						logger.error("Fetching "+ PLAN_INVESTMENT_DOCUMENT_NAME + "failed - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
						errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_JH_DOCUMENT_DELETED_ORDER));
						return errors;
					} 
					if (downloadData != null && downloadData.length > 0) {
						documentList.add(new DocumentData(downloadData, inputDocumentFormat,documentList.size()+1));
					}
	        	}
	        }
		}
		if(documentList.size()>0){
			outputFormat.add(1);
		}
		requestCriteria.setOutputFormat(outputFormat);
		requestCriteria.setDocumentList(documentList);
		
		// Merging the documents selected/collected
		try {
			mergedDocument = DocumentServiceDelegate.getInstance().generateNoticeDocument(requestCriteria);
		} catch (Exception e) {
			logger.error("Failed merging the documents - Contract Number ["+ getUserProfile(request).getCurrentContract().getContractNumber() + "]");
			errors.add(new GenericException(ErrorCodes.NMC_BUILD_SELECTED_CUSTOM_DOCUMENT_DELETED_DOWNLOAD));
			mergedPDF = null;
			return errors;
		}
		for(Integer key : mergedDocument.keySet()){
			mergedPDF = mergedDocument.get(key);
			formData.setCountOfPagesInPDFGenerated(key);
		}
		return errors;
	}	
	
	/**
	 * This is the method executed as default action which would load the report to be displayed.
	 * 
	 * @param reportForm
	 *            BaseReportForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param response
	 *            HttpServletResponse objects reference
	 * 
	 * @return String forward URL,
	 */
	 
	public String doCommon( BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		NoticeManagerUtility.getNoticeManagerTabSelection(userProfile, request, reportForm);
		BuildYourPackageNoticeManagerForm actionForm = (BuildYourPackageNoticeManagerForm) reportForm;
		// resetting values 
		actionForm.setSortAppliedInd(false);
		actionForm.setEreportError(false);
		actionForm.setMergeError(false);
		actionForm.setDownloadExceptionCached(false);
		
		actionForm.setDownloadExceptionCached(false);
		actionForm.setTermsOfUse(PlanNoticeDocumentServiceDelegate.getInstance().getTermsAndAcceptanceInd(new BigDecimal(userProfile.getPrincipal().getProfileId())));
		if(request.getParameter("fromPage") != null && request.getParameter("fromPage").equals("terms")){
			actionForm.setFromPage("terms");
			return forwards.get(BUILD_YOUR_PACKAGE_ACTION);
		}
		actionForm.setUploadAndSharePageInd(false);
		if(!actionForm.getUploadAndShareTab()){
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		
		if(!actionForm.getBuildYourPackageNPTab() || !actionForm.getBuildYourPackageNPTab() ){
			return forwards.get(UPLOAD_FINDER_FORWARD_REDIRECT);
		}
		if("true".equals(request.getSession(false).getAttribute("OrderStatusPageInd")) ){
			actionForm.reset();
			request.getSession(false).setAttribute("OrderStatusPageInd", "false");
		}
		super.doCommon(actionForm, request,response);
		actionForm.setDownloadExceptionCached(false);
		PlanDocumentReportData reportData = (PlanDocumentReportData) request
		.getAttribute(CommonConstants.REPORT_BEAN);
		actionForm.setReport(reportData);
		NoticeManagerUtility.get404a5JHDocumentAccessPermission(userProfile, actionForm);
		NoticeManagerUtility.getPlanHighlightDocument(userProfile, actionForm);
		reportData.setJhPlanNoticeDocuments(NoticeManagerUtility.addJhNoticeDocumentVO(userProfile, actionForm, true));
		request.getSession(false).setAttribute(BUILD_YOUR_REPORT_DATA, reportData);
		if(reportData.getJhPlanNoticeDocuments().size() == 0 && reportData.getCustomPlanNoticeDocuments().size() == 0){
			return forwards.get(UPLOAD_FINDER_FORWARD_REDIRECT);
		}
	
		actionForm.setInternalUser(userProfile.getRole().isInternalUser());
			boolean noticeManagerAccessPermission = NoticeManagerUtility
					.getNoticeManagerUserAccessPermission( userProfile);
			actionForm.setNoticeManagerAccessPermissions(noticeManagerAccessPermission);
		request.setAttribute(PLAN_VO_LIST_LENGTH, reportData.getCustomPlanNoticeDocuments().size()-1);
		if(!reportData.getCustomPlanNoticeDocuments().equals(null)){
			if(reportData.getCustomPlanNoticeDocuments().size() > 1){
				actionForm.setCustomSortDisplayInd(true);
			}
			if(reportData.getCustomPlanNoticeDocuments().size() != 0){
				actionForm.setCustomLastDocumentId(reportData.getCustomPlanNoticeDocuments().get(reportData.getCustomPlanNoticeDocuments().size()-1).getDocumentId());
			}else{
				actionForm.setCustomLastDocumentId(0);				
			}
		}
		
	    return forwards.get(BUILD_YOUR_PACKAGE_ACTION);

	}
 
	
	
	/* 
	 * Overridden method from avoiding the reset of data present in session
	 * If Error raises in BYP page
	 
	@Override
    protected BaseReportForm resetForm(
            BaseReportForm reportForm, HttpServletRequest request)
            throws SystemException {
        return reportForm;
    }*/
	byte[] mergedPDF = null;
	
	
	
	/**
	 * Class to handle the byte array object in session scope
	 */
	public static class Handle implements Serializable{
		private static final long serialVersionUID = 1L;

		Handle(byte[] array){
			this.byteArray = array;
		}
		private byte[] byteArray;
		/**
		 * @return the array
		 */
		public byte[] getByteArray() {
			return byteArray;
		}
	}
	
	


	/**
	 * Supports PDF generation for JH document
	 * @param userProfile
	 * @return
	 */
	public FeeDisclosureUserDetails getfeeDisclosureUserDetails(UserProfile userProfile) {
		FeeDisclosureUserDetails userDetails = new FeeDisclosureUserDetails();
		userDetails.setUserRoleCode(userProfile.getPrincipal().getRole().toString());
		userDetails.setApplicationId("PSW");
		userDetails.setRequestedTime(new Date().getTime());
		if(userProfile.isInternalUser()){
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getUserName()));
			userDetails.setUserFirstName(userProfile.getPrincipal().getFirstName());
			userDetails.setUserLastName(userProfile.getPrincipal().getLastName());
		} else {
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}
		return userDetails;
	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(PlanDocumentReportData.FILTER_CONTRACT_NUMBER,userProfile.getCurrentContract().getContractNumber());
		request.getParameter(TASK_KEY);
		criteria.addFilter(PlanDocumentReportData.FILTER_TASK,PlanDocumentReportData.TASK_PRINT);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	
	@Override
	protected String getReportId() {
		return PlanDocumentReportData.REPORT_ID;	
	}

	@Override
	protected String getReportName() {
		return null;
	}

	@Override
	protected String getDefaultSort() {
		return null;
	}

	@Override
	protected String getDefaultSortDirection() {
		return null;
	}


	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}
	

	public boolean validateCustomPlanDocument(String selectedDocuments) throws SystemException  {
		String documentSelectedstring[] = selectedDocuments.split(COMMA);
        for(int documentIdIndex = 0; documentIdIndex< documentSelectedstring.length; documentIdIndex++){
        	
        	if(!PlanNoticeDocumentServiceDelegate.getInstance()
			.getCustomDocumentPresence(new Integer(documentSelectedstring[documentIdIndex]))){
        		return false;
        	}
        }
	return true;
	}
	
	/**
	 * Adds the page log information when a user visits the page.
	 * @param form
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	  protected void postExecute(  ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws ServletException, IOException, SystemException {
		  
		  HttpSession session = request.getSession(false);
		  super.postExecute( form, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId =userProfile.getCurrentContract().getContractNumber();
		  String userAction =CommonConstants.BUILD_YOUR_PACKAGE_PAGE;
		  if(session.getAttribute(LOGGED)==null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			  session.setAttribute(LOGGED, "VISITED");
		  }
	  }
	  

		/**
		 * Method has been over ridden to avoid the existing errors to be added to current flow
		 * @param request
		 * @param errors
		 */
		public void setDownloadErrorsInSession(final HttpServletRequest request,
				final Collection<GenericException> errors) {
			if (errors != null) {
				request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
			}
		}
		
	/**
	 * this method is used to save the order number in the EL_TRANSACTION table
	 * 
	 * @param userProfile
	 * @param methodName
	 * @param data
	 */

	private void logInteraction(UserProfile userProfile, String methodName, String data) {

		try {
			ServiceLogRecord record = (ServiceLogRecord) this.record.clone();
			record.setMethodName(methodName);
			record.setData(data);
			record.setDate(new Date());
			record.setPrincipalName(userProfile.getPrincipal().getUserName());
			record.setUserIdentity(String.valueOf(userProfile.getPrincipal().getProfileId()));
			this.interactionLog.error(record);
		} catch (CloneNotSupportedException e) {
			this.generalLog.error(e);
			throw new EJBException(e.toString());
		}
	}

		/**
		 * This code has been changed and added to Validate form and request against
		 * penetration attack, prior to other validations.
		 */

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
