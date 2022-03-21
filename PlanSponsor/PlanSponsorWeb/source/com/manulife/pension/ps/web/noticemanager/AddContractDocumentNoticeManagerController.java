package com.manulife.pension.ps.web.noticemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.PlanNoticeDocumentInfo;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

/**
 * 
 * @author kumarye
 * 
 *         This Class will allows the User for adding the name of the custom
 *         document and upload a custom File and select the PostToPptIndicator
 *         value
 * 
 * 
 * 
 *
 */
@Controller
@RequestMapping(value = "/noticemanager")
@SessionAttributes({ "addCustomplanDocumentForm" })

public class AddContractDocumentNoticeManagerController extends PsAutoController {

	@ModelAttribute("addCustomplanDocumentForm")
	public AddContractDocumentNoticeManagerForm populateForm() {
		return new AddContractDocumentNoticeManagerForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/noticemanager/addcontractdocument.jsp");
		forwards.put("default", "/noticemanager/termsandconditions.jsp");
		forwards.put("add", "/noticemanager/addcontractdocument.jsp");
		forwards.put("uploadandShare", "redirect:/do/noticemanager/uploadandsharepages/");
		forwards.put("termsandconditions", "redirect:/do/noticemanager/termsandconditions/");
	}
	
	/**
	 * Constructor
	 * 
	 * 
	 */
	public AddContractDocumentNoticeManagerController() {
		super(AddContractDocumentNoticeManagerController.class);
	}
	
	/**
	 * Default Action which will checks whether the NoticeManagerAccessPermission
	 * is there or not for the particular user profile
	 * 
	 * @param form
	 *            AddContractDocumentNoticeManagerForm
	 * @param request
	 *            HttpServletRequest     
	 * @param response
	 *            HttpServletResponse     
	 * @return String forward URL
	 * 
	 */
	@RequestMapping(value = "/addcustomplandocument/", method = { RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("addCustomplanDocumentForm") AddContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input"); // default
			}
		}
		/*Collection errors = doValidate(form, request);
		if (errors.size() > 0) {
			return forwards.get("input");
		}*/

		populateDefaultForm(form);
		String navigatePath = (String) request.getSession().getAttribute("actionPerformed");
		UserProfile userProfile = getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		postExecute(form, request, response);
		// Checking whether internal user or not
		if (userProfile.getRole().isInternalUser()) {
			return forwards.get(Constants.UPLOAD_AND_SHARE_ACTION);
		}
		// Checking whether the Common Functional Specification is true or not
		try {
			if (StringUtils.isBlank(navigatePath) && (NoticeManagerUtility.validateProductRestriction(contract)
					|| NoticeManagerUtility.validateContractRestriction(contract)
					|| NoticeManagerUtility.validateDIStatus(contract, userProfile.getRole()))) {
				return forwards.get(Constants.UPLOAD_AND_SHARE_ACTION);
			}
			// checking whether the AccessToNoticeManager Permission is allowed
			else if (StringUtils.isBlank(navigatePath) || !userProfile.isNoticeManagerAccessAllowed()) {
				return forwards.get(Constants.UPLOAD_AND_SHARE_ACTION);
			}
		} catch (ContractDoesNotExistException e) {
			new SystemException(e, "Contract doesn't exception");
		}
		return forwards.get(Constants.ADD_ACTION);
	}

	/**
	 * This method will allow the User to navigate to the Upload and Share Page
	 * when user decides not to add any custom document
	 * 
	 * 
	 * @param form
	 *            AddContractDocumentNoticeManagerForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return String - forward URL
	 * 
	 */
	@RequestMapping(value = "/addcustomplandocument/", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("addCustomplanDocumentForm") AddContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		form.reset(request);
		postExecute(form, request, response);
		return forwards.get(Constants.UPLOAD_AND_SHARE_ACTION);
	}
	
	
	
	/**
     * Action performed on submit which will save the document and create two records in DB
     * 
     * 
     * @param form
     *            AddContractDocumentNoticeManagerForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String - forward Url
     * 
     */
	@RequestMapping(value = "/addcustomplandocument/", params = { "action=submit" }, method = { RequestMethod.POST })
	public String doSubmit(
			@Valid @ModelAttribute("addCustomplanDocumentForm") AddContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ServiceUnavailableException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection errors = doValidate(form, request);
		if (errors.size() > 0) {
			return forwards.get("input");
		}
		MultipartFile uploadDocument = form.getFile();
		savePlanNoticeDocument(request, form, uploadDocument.getBytes());
		populateDefaultForm(form);
		postExecute(form, request, response);
		return forwards.get(Constants.UPLOAD_AND_SHARE_ACTION);

	}
	

    /**
     * This method will save the document in server through eReports 
     * which will save the uploaded document in defined Folder Structure
     * name of the custom document and PostToppt Indicator value and insert 
     * the values of the respective changes in tables
     * 
     * @param addCustomplanDocumentForm
     *            AddCustomplanDocumentForm
     * @param fileInfo
     *            byte[]     
     * @return void
     * 
     */
	public void savePlanNoticeDocument(HttpServletRequest request, AddContractDocumentNoticeManagerForm addCustomplanDocumentForm, byte[] fileInfo)
		throws SystemException, ServiceUnavailableException {
		int documentId=0;
		UserProfile userprofile = SessionHelper.getUserProfile(request);
		Integer contractId = userprofile.getCurrentContract().getContractNumber();
		long profileId = userprofile.getPrincipal().getProfileId();
		String documentFileName = getDocumentFileName(addCustomplanDocumentForm);
		DocumentFileOutput documentFileOutput = new DocumentFileOutput();
		documentFileOutput.setDocument(new PlanNoticeDocumentInfo(String.valueOf(contractId),documentFileName,  "key"));
		documentFileOutput.setLength(fileInfo.length);
		documentFileOutput.setReportFragment(fileInfo);
		try {
			EReportsServiceDelegate.getInstance().saveDocument(
					documentFileOutput);
		} catch (SystemException systemException) {
			throw new SystemException(systemException,"Document save failed in Ereoprts");
		}
		
		// create PlanNoticeDocumentVO
		PlanNoticeDocumentVO planNoticeDocumentDetail = new PlanNoticeDocumentVO();
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeDetail = new PlanNoticeDocumentChangeHistoryVO(); 
		planNoticeDocumentDetail.setContractId(contractId);	
		planNoticeDocumentDetail.setDocumentFileName(documentFileName);
		planNoticeDocumentDetail.setDocumentName(addCustomplanDocumentForm.getFileName());
		planNoticeDocumentDetail.setVersionNumber(Constants.DOCUMENT_VERSION_NUMBER);
		planNoticeDocumentDetail.setDocumentLocked(false);
		planNoticeDocumentDetail.setIccDocument(true);
		planNoticeDocumentDetail.setJhDocument(false);
		if(addCustomplanDocumentForm.isPostToParticipantIndicator()) {
			planNoticeDocumentDetail.setPostToPptInd(CommonConstants.YES);
			planNoticeDocumentChangeDetail.setChangedPPT(CommonConstants.YES);
		}else{
			planNoticeDocumentDetail.setPostToPptInd(CommonConstants.NO);
			planNoticeDocumentChangeDetail.setChangedPPT(CommonConstants.NO);
		}
	
		planNoticeDocumentChangeDetail.setChangedProfileId(new BigDecimal(profileId));
		planNoticeDocumentChangeDetail.setContractId(contractId);
		planNoticeDocumentChangeDetail.setVersionNumber(Constants.DOCUMENT_VERSION_NUMBER);
		planNoticeDocumentChangeDetail.setDocumentName(addCustomplanDocumentForm.getFileName());
		planNoticeDocumentChangeDetail.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription("UPLD", "Document uploaded"));
		planNoticeDocumentDetail.setPlanNoticeDocumentChangeDetail(planNoticeDocumentChangeDetail);
		//calling below method to get the documentId Auto Incrementally
		documentId = PlanNoticeDocumentServiceDelegate.getInstance().getMaxDocumentId();
		planNoticeDocumentDetail.setDocumentId(documentId);
	    PlanNoticeDocumentServiceDelegate.getInstance().addCustomPlanNoticeDocument(planNoticeDocumentDetail);
	    planNoticeDocumentChangeDetail.setDocumentId(documentId);
		PlanNoticeDocumentServiceDelegate.getInstance().insertCustomPlanNoticeDocumentLogs(planNoticeDocumentChangeDetail);
		request.getSession().removeAttribute("actionPerformed");
		
	}


	

    /**
     * This method will reset the form after the save or cancel is performed
     * 
     * 
     * @param form
     *         	AutoForm	        
     * @return void
     * 
     */
	protected void populateDefaultForm(AutoForm form)
			throws SystemException {
		
		AddContractDocumentNoticeManagerForm theForm = (AddContractDocumentNoticeManagerForm) form;		
		theForm.setAction(Constants.DEFAULT_ACTION_LABEL);
		theForm.setFile(null);
		theForm.setFileName(null);
		theForm.setPostToParticipantIndicator(false);
		
	}

    /**
     * This method will return the formated document name to be saved
     * 
     * 
     * @param addCustomplanDocumentForm
     *            AddCustomplanDocumentForm     
     * @return String
     * 
     */
	
	
	protected String getDocumentFileName(AddContractDocumentNoticeManagerForm addCustomplanDocumentForm)
		throws SystemException {
		
		Date date = new Date();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    return addCustomplanDocumentForm.getFileName()+ "_" + sdfDate.format(date) + ".pdf";
	}
	/** Adds the page log information
	 * @param form
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	 
	protected String postExecute (AddContractDocumentNoticeManagerForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		  HttpSession session = request.getSession(false);
		  super.postExecute( form, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId = userProfile.getCurrentContract().getContractNumber();
		  String trackingName = CommonConstants.ADD_PAGE;
		  if(session.getAttribute(Constants.ADD_LOGGED)==null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId,profileId,trackingName);
			  session.setAttribute(Constants.ADD_LOGGED, "VISITED");
		  }
		return trackingName;

	  }
	
	 /**
     * This method will validate the submitted form and returns errors collection if any 
	 * of the specified conditions is not satisfied
     * 
     * @param form
     *            Form     
     * @return Collection
     * 
     */
	 @SuppressWarnings("unchecked")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		// Method Extended to get Super Class Collection Errors Object
		Collection errors = super.doValidate( form, request);
		 /**
		 * This code has been changed and added to Validate form and request against
		 * penetration attack, prior to other validations.
		 */
    	
		AddContractDocumentNoticeManagerForm theForm = (AddContractDocumentNoticeManagerForm) form;
		if (theForm.getAction().equals(Constants.SUBMIT_ACTION_LABEL)) {
			//retrieve the file representation
			MultipartFile uploadedDocument = theForm.getFile();
			UserProfile userProfile = getUserProfile(request);
			Integer contractId = userProfile.getCurrentContract().getContractNumber();
			String documentFileName = StringUtils.EMPTY;			
			documentFileName = theForm.getFileName();
			
			//checking and return error if non-printable characters 
			if (documentFileName != null) {
				Pattern pattern = Pattern.compile(Constants.REGEX_FILE_NAME_VALIDATION);
			    Matcher matcher = pattern.matcher(documentFileName);
			    boolean isMatch = !matcher.matches();
			    if (isMatch) {
			    	errors.add(new GenericException(
							ErrorCodes.NMC_ADD_NON_PRINTABLE_CHARACTERS));
			    }				
				for (int i = 0; i < documentFileName.length(); i++) {
					char c = documentFileName.charAt(i);
					int a = (int) c;

					if (a < Constants.ASCII_MIN || a > Constants.ASCII_MAX) {
						errors.add(new GenericException(
								ErrorCodes.NMC_ADD_NON_PRINTABLE_CHARACTERS));
						break;
					}
				}
			}
			try{				
				//retrieve the file size
				if (uploadedDocument == null || StringUtils.isBlank(uploadedDocument.getOriginalFilename())) {
					UploadFileCannotFoundOrEmptyException ae = new UploadFileCannotFoundOrEmptyException(
							"AddContractDocumentNoticeManagerAction", "doValidate "," file: " + 
							documentFileName + " is empty / no file uploaded ");
					LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ae);	
					errors.add(new GenericException(
							ErrorCodes.NMC_ADD_UPLOAD_FILE_EMPTY));
				}else if (StringUtils.isBlank(documentFileName)) {
					//checking and return error if document name is empty
					errors.add(new GenericException(
							ErrorCodes.NMC_ADD_DOCUMENT_NAME_EMPTY));
				}else if(PlanNoticeDocumentServiceDelegate.getInstance().isNoticeDocumentNameExists(documentFileName,contractId)){
					///checking and return error if name of the Document already Exists
					errors.add(new GenericException(
							ErrorCodes.NMC_ADD_NOTICE_ALREADY_EXISTS));
				}
				
				if(uploadedDocument != null && StringUtils.isNotBlank(uploadedDocument.getOriginalFilename())){
					//File trying to upload is password protected
					
					if(uploadedDocument.getOriginalFilename().endsWith(Constants.NMC_PDF_READER) || 
							uploadedDocument.getOriginalFilename().endsWith(Constants.NMC_CAPS_PDF_READER)){
					PDDocument document = null;
					try {
						document = PDDocument.load(uploadedDocument.getInputStream());
					}catch (FileNotFoundException e) {
						errors.add(new GenericException(
								ErrorCodes.NMC_ADD_FILE_NOT_EXISTS_IN_LOCATION));
					} catch (IOException e) {
						errors.add(new GenericException(
								ErrorCodes.NMC_ADD_FILE_NOT_EXISTS_IN_LOCATION));
					}
					if( document != null && document.isEncrypted() )   {
						errors.add(new GenericException(
								ErrorCodes.NMC_ADD_FILE_PASSWORD_PROTECTED));
					}else if(document!= null){

						try {
							int defaultWidth = 612;
							int defaultHeight = 792;
							int defaultWidthOptional = 792;
							int defaultHeightOptional = 612;
							
							PdfReader reader = new PdfReader(uploadedDocument.getBytes());
							for (int count  = 1;count <= document.getNumberOfPages();count++){
								Rectangle pageSize = reader.getPageSize(count);
								int pageWidth = (int) pageSize.getWidth();
								int pageHeight = (int) pageSize.getHeight();
								if(!((defaultWidth==pageWidth 
											&& defaultHeight==pageHeight)||(defaultWidthOptional==pageWidth 
													&& defaultHeightOptional==pageHeight))){
									errors.add(new GenericException(
											ErrorCodes.NMC_ADD_FILE_TO_BE_PDF));
									break;
								}
							}
							
							reader.close();							

						} catch (FileNotFoundException e) {
							errors.add(new GenericException(
									ErrorCodes.NMC_ADD_FILE_NOT_EXISTS_IN_LOCATION));
						} catch (IOException e) {
							errors.add(new GenericException(
									ErrorCodes.NMC_ADD_FILE_TO_BE_PDF));
						}finally{
							if( document != null)
								document.close();
						}
					}
					}
					else{
						errors.add(new GenericException(
								ErrorCodes.NMC_ADD_FILE_TO_BE_PDF));
					}
				}
				if (errors.size() > 0) {
					SessionHelper.setErrorsInSession(request, errors);
				}
			}
			catch (Exception exception) {
				errors.add(new GenericException(
						ErrorCodes.NMC_ADD_FILE_TO_BE_PDF));
			}
		}
        
		return errors;
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
}