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
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.PlanNoticeDocumentInfo;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.noticemanager.EditContractDocumentNoticeManagerForm.DocumentChangeTypeCode;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;




/**
 * @author grandan
 * 
 * This Class will allow the Users for editing the name of Custom 
 * document and replace a custom document with a new version document 
 * and change the PostToPpt Indicator value as Yes or No
 *  
 */

@Controller
@RequestMapping(value = "/noticemanager")

public class EditContractDocumentNoticeManagerController extends PsAutoController {

	@ModelAttribute("editCustomPlanDocumentForm")
	public EditContractDocumentNoticeManagerForm populateForm() {
		return new EditContractDocumentNoticeManagerForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/noticemanager/editContractDocument.jsp");
		forwards.put("default", "/noticemanager/editContractDocument.jsp");
		forwards.put("editnotice", "redirect:/do/noticemanager/editCustomPlanDocument/");
		forwards.put("uploadandshare", "redirect:/do/noticemanager/uploadandsharepages/");
	}
	

	/**
	 * Constructor
	 * 
	 * 
	 */
	public EditContractDocumentNoticeManagerController() {
		super(EditContractDocumentNoticeManagerController.class);
	}
	/**
	 * Default Action which will checks whether the NoticeManagerAccessPermission
	 * is there or not and Access Criteria Web Functions for the particular user profile 
	 * and  retrieve the values of custom document name and PostToppt Indicator values 
	 * from the table and displays in the page while loading the Page itself
	 * 
	 * 
	 * 
	 * 
	 * @param form
	 *            EditContractDocumentNoticeManagerForm
	 * @param request
	 *            HttpServletRequest     
	 * @param response
	 *            HttpServletResponse     
	 * @return String - Forward url
	 * 
	 */
	@RequestMapping(value = "/editCustomPlanDocument/", method = { RequestMethod.GET })
	public String doDefault(
			@Valid @ModelAttribute("editCustomPlanDocumentForm") EditContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		// Getting the DocumentId from the Session
		Object requestedId = request.getSession().getAttribute("documentId");
		UserProfile userProfile = getUserProfile(request);
		// Checking whether internal user or not
		if (userProfile.getRole().isInternalUser()) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		Contract contract = userProfile.getCurrentContract();
		try {
			// Checking whether the DocumentId is null and Common Functional
			// Specification is true or not
			// based on the value redirect to home page or Edit Page
			if (requestedId == null && (NoticeManagerUtility.validateProductRestriction(contract)
					|| NoticeManagerUtility.validateContractRestriction(contract)
					|| NoticeManagerUtility.validateDIStatus(contract, userProfile.getRole()))) {
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
			// checking whether the DocumentId is null or AccessToNoticeManager
			// Permission is allowed
			else if (requestedId == null || !userProfile.isNoticeManagerAccessAllowed()) {
				return forwards.get(Constants.UPLOAD_ACTION);
			}
		} catch (ContractDoesNotExistException e) {
			new SystemException(e, "Contract selected buy the user not exist.please select another Contract Number");
		}
		Integer documentId = (Integer) requestedId;
		planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.getInstance()
				.getCustomPlanNoticeDocumentInfo(documentId);
		if (planNoticeDocumentVO == null || planNoticeDocumentVO.getDocumentId() == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		// Retrieve the values from the DataBase

		form.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId()));
		form.setDocumentName(planNoticeDocumentVO.getDocumentName());
		form.setDocumentId(planNoticeDocumentVO.getDocumentId());
		if ((planNoticeDocumentVO.getPostToPptInd()).equals(CommonConstants.YES)) {
			form.setPostToPptIndicator(CommonConstants.YES_INDICATOR);
		} else {
			form.setPostToPptIndicator(CommonConstants.NO_INDICATOR);
		}
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentHistoryVO = null;
		planNoticeDocumentHistoryVO = PlanNoticeDocumentServiceDelegate.getInstance()
				.getCustomPlanNoticeDocumentHistory(contract.getContractNumber(),
						planNoticeDocumentVO.getDocumentId());
		if (null != planNoticeDocumentHistoryVO) {
			planNoticeDocumentHistoryVO.setCrossedTwelveMonthRule(NoticeManagerUtility
					.checkTwelveMonthRuleForDocument(planNoticeDocumentVO.getPostToPptInd(), planNoticeDocumentHistoryVO));
			form.setCrossedTwelveMonthRule(planNoticeDocumentHistoryVO.isCrossedTwelveMonthRule());
			form.setDocAvailableUntilDate(NoticeManagerUtility.getDocAvailableUntilDate(planNoticeDocumentHistoryVO.getChangedDatePlusOneYear()));
		}
		postExecute(form, request, response);
		return forwards.get(Constants.EDIT_DEFAULT_ACTION);
	}
	/**
     * ActionPerformed on submit which will save the information either changing the 
     * name of the custom document and replacing the custom document with a new version 
     * which will save the new uploaded custom document through eReports in a definite 
     * order and PostTopptIndicator value Yes or No all edited information gets reflected in 
     * table with both update and insert Query in respective tables  
     * 
     * 
     * 

     * @param form
     *            EditContractDocumentNoticeManagerForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String Forward url
     * 
     */
	@RequestMapping(value ="/editCustomPlanDocument/", params={"action=save"} , method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("editCustomPlanDocumentForm") EditContractDocumentNoticeManagerForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection errors = doValidate(actionForm, request);
		if (errors.size() > 0) {
			return forwards.get("input");
		}
		
		//Initializing the values
		String documentChangeTypeCode = StringUtils.EMPTY;
		String documentChangeTypeDesc = StringUtils.EMPTY;
		
		UserProfile userprofile = SessionHelper.getUserProfile(request);
		int contractId = userprofile.getCurrentContract().getContractNumber();	
		
		BigDecimal profileId = new BigDecimal(userprofile.getPrincipal().getProfileId());
		Integer documentId = actionForm.getDocumentId();
		
		//Fetches the Document Details
		PlanNoticeDocumentVO existingNoticeRecord = PlanNoticeDocumentServiceDelegate.getInstance().getCustomPlanNoticeDocumentInfo(documentId);
		Integer versionCount=existingNoticeRecord.getVersionNumber();
		MultipartFile uploadFile = actionForm.getDocumentFileName();
		String documentName = actionForm.getDocumentName();
		String postToPptInd = actionForm.getPostToPptIndicator();
		String documentFileName =  StringUtils.EMPTY;
		PlanNoticeDocumentVO planNoticeDocumentDetail = new PlanNoticeDocumentVO();
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeDetail = new PlanNoticeDocumentChangeHistoryVO();
		Boolean isUploadChanged = false;
		Boolean isDocumentNameChanged = false;
		Boolean isPPTIndChanged= false;
		
		//Checking documentFile is null if not replace the File
		if(uploadFile != null && StringUtils.isNotBlank(uploadFile.getOriginalFilename())){
		//	documentFileName = savePlanNoticeDocument(editCustomPlanDocumentForm,uploadFile.getFileData(),contractId);
			planNoticeDocumentDetail.setDocumentFileName(documentFileName);
			isUploadChanged = true;
		}
		//Checking documentName is null  and replace the name
		if(StringUtils.isNotBlank(documentName)){
			if(!documentName.equals(existingNoticeRecord.getDocumentName().trim())){
				planNoticeDocumentDetail.setDocumentName(documentName);
				isDocumentNameChanged = true;
			}
		}
		//checking the PostToPpt Indicator value is null or Yes  or No
		if(StringUtils.isNotBlank(postToPptInd)){

			if(actionForm.getPostToPptIndicator().equalsIgnoreCase(Constants.YES_INDICATOR)
					&& !(CommonConstants.YES).equals(existingNoticeRecord.getPostToPptInd().trim())){
				planNoticeDocumentDetail.setPostToPptInd(CommonConstants.YES);	
				planNoticeDocumentChangeDetail.setChangedPPT(CommonConstants.YES);
				isPPTIndChanged = true;
				
			}else if(actionForm.getPostToPptIndicator().equalsIgnoreCase(Constants.NO_INDICATOR)
					&&!(CommonConstants.NO).equals(existingNoticeRecord.getPostToPptInd().trim())){
				planNoticeDocumentDetail.setPostToPptInd(CommonConstants.NO);
				planNoticeDocumentChangeDetail.setChangedPPT(CommonConstants.NO);
				isPPTIndChanged = true;
				}
			
		}
		//Checking the documentChangeTypeCode 
		if(isDocumentNameChanged){
			documentChangeTypeCode = DocumentChangeTypeCode.CHNGN.toString();
			documentChangeTypeDesc = DocumentChangeTypeCode.CHNGN.getChangeTypeCode();
		}
		if(isPPTIndChanged){
			documentChangeTypeCode = DocumentChangeTypeCode.CHNGP.toString();
			documentChangeTypeDesc = DocumentChangeTypeCode.CHNGP.getChangeTypeCode();
		}
		
		if(isUploadChanged){
			documentChangeTypeCode =  DocumentChangeTypeCode.REPL.toString();
			documentChangeTypeDesc =  DocumentChangeTypeCode.REPL.getChangeTypeCode();
		}
		
		if(isUploadChanged && isDocumentNameChanged){
			documentChangeTypeCode =  DocumentChangeTypeCode.CHRPN.toString();
			documentChangeTypeDesc =  DocumentChangeTypeCode.CHRPN.getChangeTypeCode();
		}
		if(isDocumentNameChanged && isPPTIndChanged){
			documentChangeTypeCode =  DocumentChangeTypeCode.CHNGB.toString();
			documentChangeTypeDesc =  DocumentChangeTypeCode.CHNGB.getChangeTypeCode();
		}
		if(isPPTIndChanged && isUploadChanged){
			documentChangeTypeCode =  DocumentChangeTypeCode.CHRPP.toString();
			documentChangeTypeDesc =  DocumentChangeTypeCode.CHRPP.getChangeTypeCode();
		}
		if(isUploadChanged && isPPTIndChanged && isDocumentNameChanged){
			documentChangeTypeCode =  DocumentChangeTypeCode.CHRPB.toString();
			documentChangeTypeDesc =  DocumentChangeTypeCode.CHRPB.getChangeTypeCode();
		}

		planNoticeDocumentDetail.setDocumentId(documentId);
		planNoticeDocumentDetail.setContractId(contractId);
		planNoticeDocumentDetail.setDocumentLocked(false);
	    // create PlanNoticeDocumentChangeHistoryVO
		
		planNoticeDocumentChangeDetail.setDocumentId(documentId);
		planNoticeDocumentChangeDetail.setChangedProfileId(profileId);
		planNoticeDocumentChangeDetail.setContractId(contractId);
		planNoticeDocumentChangeDetail.setDocumentName(documentName);
		
		planNoticeDocumentChangeDetail.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription(documentChangeTypeCode, documentChangeTypeDesc));
	    planNoticeDocumentDetail.setPlanNoticeDocumentChangeDetail(planNoticeDocumentChangeDetail);
		
	    //Checking whether the documentName is changed or not if changed getting the 
	    //documentName from  select query and inserting in to Value Object
		if(planNoticeDocumentDetail.getDocumentName() != null)
			planNoticeDocumentChangeDetail.setPreviousdocumentName(existingNoticeRecord.getDocumentName());
		else
			planNoticeDocumentChangeDetail.setPreviousdocumentName("");
		
		if(StringUtils.isBlank(planNoticeDocumentDetail.getPostToPptInd())){
			planNoticeDocumentDetail.setPostToPptInd(existingNoticeRecord.getPostToPptInd());	
			planNoticeDocumentChangeDetail.setChangedPPT(existingNoticeRecord.getPostToPptInd());
		}
		
		//checking whether the user has performed the operations like replacing the custom document or 
		//combination of both replacing the custom document and PPT Indicator or combination of replacing 
		//the custom document and documentName Changed then the Operations performed are Inserting in to 
		//Contract Notice document and Contract Notice Document Change Log with version Number Incremented 
		//and updating the Soft Delegate Indicator value.
		if(documentChangeTypeCode.equals("REPL") || documentChangeTypeCode.equals("CHRPN")|| 
				documentChangeTypeCode.equals("CHRPP") ){
			planNoticeDocumentDetail.setDocumentName(documentName);
			planNoticeDocumentDetail.setDocumentFileName(documentFileName);
			planNoticeDocumentDetail.setVersionNumber(versionCount+1);
			planNoticeDocumentChangeDetail.setVersionNumber(versionCount+1);
		    PlanNoticeDocumentServiceDelegate.getInstance().updateSoftDelgateIndicator(planNoticeDocumentDetail);
			PlanNoticeDocumentServiceDelegate.getInstance().addCustomPlanNoticeDocument(planNoticeDocumentDetail);
			PlanNoticeDocumentServiceDelegate.getInstance().insertCustomPlanNoticeDocumentLogs(planNoticeDocumentChangeDetail);
		
			}
		//checking whether the User has Performed the Operations like Changing the documentName
		//or a combination of both changes i.e documentName Change along with PPt Indicator Changed
		//or PPt Indicator alone Changed then the Operations Performed are Not updating the Version
		//Number only updating the Contract Notice Document table and inserting in Contract Notice 
		//Document Name Change Log
		if(documentChangeTypeCode.equals("CHNGN") || documentChangeTypeCode.equals("CHNGP") ||
				documentChangeTypeCode.equals("CHNGB") ){
			planNoticeDocumentDetail.setDocumentName(documentName);
			if(existingNoticeRecord.isSoftDelIndicator().equals("N")){
				planNoticeDocumentDetail.setVersionNumber(versionCount);
				planNoticeDocumentChangeDetail.setVersionNumber(versionCount);
			}else{
			planNoticeDocumentDetail.setVersionNumber(versionCount+1);
			planNoticeDocumentChangeDetail.setVersionNumber(versionCount+1);
			}
			PlanNoticeDocumentServiceDelegate.getInstance().editCustomPlanNoticeDocument(planNoticeDocumentDetail);
			
		}
		//when the user has Performed all the Changes then going to update the  Contract Notice Document 
		//with updated values of documentName and PPt indicator and insert in the Contract Notice Document
		//change Log 
		if(documentChangeTypeCode.equals("CHRPB")){
			planNoticeDocumentDetail.setDocumentName(documentName);
			planNoticeDocumentDetail.setVersionNumber(versionCount+1);
		    planNoticeDocumentChangeDetail.setVersionNumber(versionCount+1);
		    PlanNoticeDocumentServiceDelegate.getInstance().updateSoftDelgateIndicator(planNoticeDocumentDetail);
			PlanNoticeDocumentServiceDelegate.getInstance().addCustomPlanNoticeDocument(planNoticeDocumentDetail);
			PlanNoticeDocumentServiceDelegate.getInstance().editCustomPlanNoticeDocument(planNoticeDocumentDetail);
		}
		Object requestedId = request.getSession().getAttribute("documentId");
	    LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK,requestedId.toString());
	    postExecute(actionForm, request, response);
		return forwards.get(Constants.UPLOAD_ACTION);
	}
	 
	/**
     * This method will helps the user to Navigate to UploadandSharePage
     * when the User decides not to change any custom document
     * 
     * 
     * @param form
     *            EditContractDocumentNoticeManagerForm
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String
     * 
     */

	@RequestMapping(value = "/editCustomPlanDocument/", params = { "action=cancel" }, method = { RequestMethod.POST })
	public String doCancel(
			@Valid @ModelAttribute("editCustomPlanDocumentForm") EditContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Object requestedId = request.getSession().getAttribute("documentId");
		if (requestedId != null) {
			LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK, requestedId.toString());
		}
		postExecute(form, request, response);
		return forwards.get(Constants.UPLOAD_ACTION);
	}
	/**
     * This method is used to release the lock for particular custom document if the user leave from Edit page 
     * without performing any operation
     * 
     * @param request
     *            HttpServletRequest     
     * @param response
     *            HttpServletResponse     
     * @return String
     * 
     */

	@RequestMapping(value = "/editCustomPlanDocument/", params = { "action=releaseLock" }, method = {
			RequestMethod.POST })
	public String doReleaseLock(
			@Valid @ModelAttribute("editCustomPlanDocumentForm") EditContractDocumentNoticeManagerForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Lock isDocumentlocked = null;
		Object requestedId = request.getSession().getAttribute("documentId");
		if (requestedId != null) {
			isDocumentlocked = LockServiceDelegate.getInstance().getLockInfo(Constants.CUSTOM_DOCUMENT_LOCK,
					requestedId.toString());
		}
		if (isDocumentlocked != null) {
			LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK, requestedId.toString());
		}
		return null;
	}
	
	@RequestMapping(value = "/editCustomPlanDocument/", params = { "action=printPDF" }, method = { RequestMethod.POST })
	public String doPrintPDF(
			@Valid @ModelAttribute("editCustomPlanDocumentForm") EditContractDocumentNoticeManagerForm form,
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
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	 /**
     * This method will save the document in server through eReports 
     * which will save the uploaded document in defined Folder Structure
     * 
     * 
     * @param addCustomplanDocumentForm
     *            AddCustomplanDocumentForm
     * @param fileInfo
     *            byte[]     
     * @return string
     * 
     */
    public String savePlanNoticeDocument(EditContractDocumentNoticeManagerForm editCustomPlanDocumentForm, byte[] fileInfo, Integer contractId)
	throws SystemException, ServiceUnavailableException {

		DocumentFileOutput documentFileOutput = new DocumentFileOutput();
		String documentFileName = getDocumentFileName(editCustomPlanDocumentForm);
		documentFileOutput.setDocument(new PlanNoticeDocumentInfo(String.valueOf(contractId),documentFileName ,  "key"));
		documentFileOutput.setLength(fileInfo.length);
		documentFileOutput.setReportFragment(fileInfo);
		try {
			EReportsServiceDelegate.getInstance().saveDocument(documentFileOutput);
					
		} catch (SystemException systemException) {
			throw new SystemException(systemException,"Document save failed in Ereoprts");
		}
		return documentFileName;
	}
	/**
	 * This method will validate the submitted form and returns errors collection if any 
	 * of the specified conditions is not satisfied. 
	 * 
	 * @param form
	 *            Form     
	 * @return Collection
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
	    Collection errors = super.doValidate( form, request);
	   
	    
		EditContractDocumentNoticeManagerForm editCustomPlanDocumentForm=(EditContractDocumentNoticeManagerForm)form;
		if(editCustomPlanDocumentForm.getAction().equals("save")){
			MultipartFile uploadedFile  = editCustomPlanDocumentForm.getDocumentFileName();
			String documentName = editCustomPlanDocumentForm.getDocumentName();
			UserProfile userProfile = getUserProfile(request);
			Integer ContractId = userProfile.getCurrentContract().getContractNumber();
			String documentFileName = "";
			documentFileName = uploadedFile.getOriginalFilename();
			
			//checking and return error if non-printable characters 
			if (documentName != null) {
				Pattern pattern = Pattern.compile(Constants.REGEX_FILE_NAME_VALIDATION);
			    Matcher matcher = pattern.matcher(documentName);
			    boolean isMatch = !matcher.matches();
			    if (isMatch) {
			    	errors.add(new GenericException(
							ErrorCodes.NMC_ADD_NON_PRINTABLE_CHARACTERS));
			    }
				for (int i = 0; i < documentName.length(); i++) {
					char c = documentName.charAt(i);
					int a = (int) c;

					if (a < Constants.ASCII_MIN || a > Constants.ASCII_MAX) {
						errors.add(new GenericException(
								ErrorCodes.NMC_EDIT_NON_PRINTABLE_CHARACTERS));
						break;
					}
				}
			}
			
			Integer documentId = editCustomPlanDocumentForm.getDocumentId();
			  PlanNoticeDocumentVO existingNoticeRecord = null;
			  if(documentId!=null){
				  try {
					  existingNoticeRecord = PlanNoticeDocumentServiceDelegate.getInstance().getCustomPlanNoticeDocumentInfo(documentId);
				  } catch (SystemException e) {

					  errors.add(new GenericException(ErrorCodes.NMC_EDIT_NON_PRINTABLE_CHARACTERS ));;
				  }
			  }else{
						errors.add(new GenericException(ErrorCodes.NMC_EDIT_NON_PRINTABLE_CHARACTERS ));;
				 }
			//checking and return error if document name is empty
			if( documentName.isEmpty()){
				errors.add(new GenericException(
						ErrorCodes.NMC_EDIT_FILE_NAME_EMPTY));
			}
			if(documentName.length()> Constants.MAX_FILE_NAME_LENGTH){
				errors.add(new GenericException(ErrorCodes.NMC_EDIT_MAX_DOCUEMNT_LENGTH));
			}
		       //checking and return error if name of the Document already Exists
			else if(StringUtils.isNotBlank(documentName)&&!documentName.equals(existingNoticeRecord.getDocumentName().trim())){
				try {
					if(PlanNoticeDocumentServiceDelegate.getInstance().isNoticeDocumentNameExists(documentName,ContractId)){
						errors.add(new GenericException(ErrorCodes.NMC_EDIT_DOCUMENT_ALREADY_EXISTS ));
					}
				} 
				catch (SystemException e) {
					logger.error(
							"verifying the name of the Custom docuemnt already exist or not",
							e);
				}
			}
			if(uploadedFile != null && StringUtils.isNotBlank(uploadedFile.getOriginalFilename()) ){ 
				if(uploadedFile.getOriginalFilename().endsWith(Constants.NMC_PDF_READER) ||
						uploadedFile.getOriginalFilename().endsWith(Constants.NMC_CAPS_PDF_READER)){
					//File trying to upload is password protected
					PDDocument doc = null;
					try {
						doc = PDDocument.load(uploadedFile.getInputStream());
					} catch (FileNotFoundException e1) {
						errors.add(new GenericException(
								ErrorCodes.NMC_EDIT_FILE_NOT_EXISTS_IN_LOCATION));
					} catch (IOException e1) {
						errors.add(new GenericException(
								ErrorCodes.NMC_EDIT_FILE_NOT_EXISTS_IN_LOCATION));
					}
					if(doc!=null &&  doc.isEncrypted() )   {
						errors.add(new GenericException(
								ErrorCodes.NMC_EDIT_FILE_PASSWORD_PROTECTED));
					}else if(doc!=null){
						try {
							int defaultWidth = 612;
							int defaultHeight = 792;
							int defaultWidthOptional = 792;
							int defaultHeightOptional = 612;
							PdfReader reader = new PdfReader(uploadedFile.getBytes());
							for (int count  = 1;count<=doc.getNumberOfPages();count++){
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
							try {
								if(doc != null)
									doc.close();
							} catch (IOException ioException) {
								if (logger.isDebugEnabled()) {
					                logger.debug("Unable to close the document", ioException);
					            }
							}
						}
					}

				}else{
					errors.add(new GenericException(
							ErrorCodes.NMC_EDIT_FILE_TO_BE_PDF));
				}
			}
			if (errors.size() == 0) {
				

				MultipartFile documentFile = editCustomPlanDocumentForm.getDocumentFileName();
				String postToPptInd = editCustomPlanDocumentForm.getPostToPptIndicator();

				if((documentFile != null && StringUtils.isNotBlank(documentFile.getOriginalFilename()))
						||StringUtils.isNotBlank(documentName)&&!documentName.equals(existingNoticeRecord.getDocumentName().trim())
						||(StringUtils.isNotBlank(postToPptInd) && ((editCustomPlanDocumentForm.getPostToPptIndicator().equalsIgnoreCase(Constants.YES_INDICATOR)
								&& !(CommonConstants.YES).equals(existingNoticeRecord.getPostToPptInd().trim()))
								||(editCustomPlanDocumentForm.getPostToPptIndicator().equalsIgnoreCase(Constants.NO_INDICATOR)
										&&!(CommonConstants.NO).equals(existingNoticeRecord.getPostToPptInd().trim())))))
				{
					//There is changes in document detail and that need to be updated
				}else{
					// Need to updated  with proper error code to display the message saying there is change in data and therefore no updates required.
					
				}


			}
			if (errors.size() > 0) {
				SessionHelper.setErrorsInSession(request, errors);
			}
		} 
		
			return errors;
	}

	
	
	/**
	 * Adds the page log information
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	  protected void postExecute( ActionForm reportForm, HttpServletRequest request, 
				HttpServletResponse response) throws ServletException, IOException, SystemException {
		  
		  Object requestedId = request.getSession().getAttribute("documentId");  
		  HttpSession session = request.getSession(false);
		  super.postExecute( reportForm, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId =userProfile.getCurrentContract().getContractNumber();
		  String userAction =CommonConstants.EDIT_PAGE;
		 // EditContractDocumentNoticeManagerForm editNoticeForm = (EditContractDocumentNoticeManagerForm) reportForm;
		  if(session.getAttribute(Constants.EDIT_LOGGED)==null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			  session.setAttribute(Constants.EDIT_LOGGED, "VISITED");
		  }
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
	protected String getDocumentFileName(EditContractDocumentNoticeManagerForm editCustomPlanDocumentForm)
		throws SystemException {
		
		Date date = new Date();
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    return editCustomPlanDocumentForm.getDocumentFileName()+ "_" + sdfDate.format(date) + ".pdf";
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

