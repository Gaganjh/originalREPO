package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.UploadFileResponse;
import com.manulife.pension.lp.model.gft.UploadInfoRequest;
import com.manulife.pension.lp.model.gft.UploadInfoResponse;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.FileUploadServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.tools.exception.UploadFileExceedsMaxSizeException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

/**
 * <p>
 * Action class to handle the Upload a Conversion file actions
 * This class extends <code>AbstractSubmitAction</code>.  
 * </p>
 * 
 * @author ramavel
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"submissionConversionFileForm"})

public class SubmissionConversionFileController extends AbstractSubmitController {
	
	@ModelAttribute("submissionConversionFileForm") 
	public SubmissionConversionFileForm populateForm() 
	{
		return new SubmissionConversionFileForm();
		}
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put(INPUT,"/tools/submissionConversion.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");
		forwards.put("confirm","redirect:/do/tools/uploadConversionFile/?action=confirm");
		forwards.put("refresh","redirect:/do/tools/uploadConversionFile/?action=refresh");
		forwards.put("confirmPage","/tools/submissionConversionConfirmation.jsp" );
		forwards.put("subHistory","redirect:/do/tools/submissionHistory/" );
	}

	/**
	 * Default Constructor
	 */
	public SubmissionConversionFileController() {
		super(SubmissionConversionFileController.class);
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(
	 * org.apache.struts.action.ActionMapping, AutoForm, 
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/uploadConversionFile/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("submissionConversionFileForm") SubmissionConversionFileForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		} 
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}

		String validationResult = validate(actionForm, request);
		if(validationResult!=null){
			return validationResult;
		}

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions, if there is no permission, forward him to 
		// Tools page
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			return forwards.get(TOOLS);
		}
		
		if (userProfile.getContractProfile().getIfileConfig() == null) {
			try {
				setupUserProfile(userProfile, actionForm);
			} catch (UnableToAccessIFileException ex) {
				Collection<GenericException> errors = new ArrayList<GenericException>();
				
				// Log the application exception
				LogUtility.logApplicationException(
						Constants.PS_APPLICATION_ID,
						userProfile.getPrincipal().getProfileId(),
						userProfile.getPrincipal().getUserName(),ex);
				
				errors.add(new GenericException(ErrorCodes.UNABLE_TO_ACCESS_IFILE));
				SessionHelper.setErrorsInSession(request, errors);
				return forwards.get(TOOLS);
			}	
		}

		if (logger.isDebugEnabled()) {
			logger.debug(StaticHelperClass.toString(userProfile
					.getContractProfile().getIfileConfig()));
		}
		
		actionForm.clear(request);
		
		//saveToken(request);

		populateForm(actionForm, userProfile);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get(INPUT);
	}

	/**
	 * Performs the submit action.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return @throws
	 *         SystemException
	 * @throws ServletException
	 */
	@RequestMapping(value ="/uploadConversionFile/" ,params={"actionLabel=submit"}   , method = {RequestMethod.POST}) 
	public String doSubmit (@Valid @ModelAttribute("submissionConversionFileForm") SubmissionConversionFileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> doSubmit");
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}

		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}
		
		
		UserProfile userProfile = getUserProfile(request);
		Date startTime = null;
		Date endTime = null;
		
		try {
			
			startTime = new Date();
			GFTUploadDetail result = processUploadRequest(form, userProfile);
			endTime = new Date();
			
			// log the file upload event
			logUploadEvent(result, startTime, endTime, 
					userProfile.getPrincipal(), "doSubmit");

			FileUploadDetailBean fileUploadDetailBean = 
				new FileUploadDetailBean(result);
			
			// override the name format stored in the database (FN LN) 
			// with a different one for the confirmation (LN, FN)
			fileUploadDetailBean.setSender(userProfile.getPrincipal().getLastName().trim() 
						+ ", " + userProfile.getPrincipal().getFirstName());

			SessionHelper.setFileUploadDetails(request, fileUploadDetailBean);

			//resetToken(request);

			//we now have at least one item for history
			userProfile.getContractProfile().getIfileConfig().setCurrentContractHasUploadHistory(true);

		} catch (UploadFileCannotFoundOrEmptyException ex) {
			Collection<GenericException> errors = new ArrayList<GenericException>();
			MultipartFile file = form.getUploadFile();
			String fileName = "";
			if (file != null)
				fileName = file.getOriginalFilename();
			// log error in mrl
			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,
					userProfile.getPrincipal().getProfileId(),
					userProfile.getPrincipal().getUserName(),ex);
			
			errors.add(new GenericException(ErrorCodes.UPLOAD_FILE_EMPTY,
					new String[] { fileName }));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		} catch (SystemException e) {
			Collection<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_SYSTEM_EXCEPTION, new Object [] {}));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- doSubmit");

		return forwards.get(CONFIRMATION);
	}

	/**
	 * Forward to the confirmation page. This action is needed because it's the
	 * result of a REDIRECT after a POST.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/uploadConversionFile/", params={"action=confirm"} , method =  {RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("submissionConversionFileForm") SubmissionConversionFileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> doConfirm");
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}

		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}

		String forward = super.doConfirm(form, request,response);

		// lets check whether the confirm object is in the session
		if (null == request.getSession(false).getAttribute(Constants.FILE_UPLOAD_DETAIL_DATA)) {
		    forward = forwards.get(SUBMISSION_HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forwards.get(forward);
	}
	
	@RequestMapping(value ="/uploadConversionFile/",params={"action=refresh"} , method = {RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("submissionConversionFileForm") SubmissionConversionFileForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> doRefresh");
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}

		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}
       String forward=super.doRefresh( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}

	/**
	 * Sets up the UserProfile
	 */
	@SuppressWarnings("unchecked")
	protected void setupUserProfile(UserProfile userProfile, AutoForm form)
		throws SystemException, UnableToAccessIFileException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> setupUserProfile");

		IFileConfig ifileConfig = new IFileConfig();

		Calendar calendar = adjustDate4pm(null);

		int currentYear = calendar.get(Calendar.YEAR);

		//set it up for the 1st time

		//setup years
		ArrayList years = new ArrayList();

		years.add(String.valueOf(currentYear));
		calendar.add(Calendar.DATE, 15);
		if (calendar.get(Calendar.YEAR) > currentYear) {
			years.add(String.valueOf(currentYear));
		}
		ifileConfig.setYears(years);

		//set it up in userProfile
		userProfile.getContractProfile().setIfileConfig(ifileConfig);

		if (logger.isDebugEnabled())
			logger.debug("exit <- setupUserProfile");
	}

	/**
	 * Sets the File upload Action property
	 *  
	 * @param form
	 * @param userProfile
	 * @throws SystemException
	 */
	protected void populateForm(AutoForm form, UserProfile userProfile)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");
		
		SubmissionConversionFileForm theForm = (SubmissionConversionFileForm) form;

		theForm.setDisplayFileUploadSection(
                userProfile.isAllowedUploadSubmissions()
				&& !userProfile.getCurrentContract().getStatus().equals(
						Contract.STATUS_CONTRACT_DISCONTINUED));

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateForm");
	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(
	 * 		org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		SubmissionConversionFileForm form = (SubmissionConversionFileForm) actionForm;

		/*
		 * If this is a save action, we should compare the token and make sure
		 * it's still valid. Token is initialized in the doDefault() method and
		 * reset in the doSave() method.
		 */
     	if (form.isSendAction()) {
			/*if (!isTokenValid(request)) {
				return forwards.get(TOOLS);
			}*/
		}

     	// Perform the validation
		Collection<GenericException> errors = doValidate( form, request);

		/*
		 * Errors are stored in the session so that 
		 * our REDIRECT can look up the errors.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		return null;
	}

	/**
	 * Validates the 
	 */
	@SuppressWarnings("unchecked")
	public Collection<GenericException> doValidate( 
			ActionForm form, HttpServletRequest request) {
		// This code has been changed and added to validate form and request
		// against penetration attack, prior to other validations 
		SubmissionConversionFileForm theForm = (SubmissionConversionFileForm) form;
		
		// Call the base class validate method
		Collection<GenericException> errors = 
			super.doValidate( form, request);
		
		if (theForm.isSendAction()) {

			UserProfile userProfile = getUserProfile(request);
			
			//retrieve the file representation
			MultipartFile file = theForm.getUploadFile();
			
			// get the file name
			String fileName = file.getOriginalFilename();
			
			// get the FileInformation value
			String fileInformation = theForm.getFileInforamtion();
			
			
		
            
			//retrieve the file size
			if (file == null || file.getSize() == 0) {
				//log the error in mrl
				UploadFileCannotFoundOrEmptyException ae =
					new UploadFileCannotFoundOrEmptyException(
						"SubmissionUploadAction", "doValidate "," file: " + 
						fileName + " is empty ");
				
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,
						userProfile.getPrincipal().getProfileId(),
						userProfile.getPrincipal().getUserName(),ae);	
				
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.UPLOAD_FILE_EMPTY,
						new String[] { file.getOriginalFilename() }));
				
			} else if (file.getSize() > Environment.getInstance()
					.getMaxUploadFileSizeKbytes() * 1024) {

				//log the error in mrl
				UploadFileExceedsMaxSizeException ae =new UploadFileExceedsMaxSizeException(
						"SubmissionUploadAction", "doValidate "," file: " + 
						fileName + " exceeds max size ");
				
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,
						userProfile.getPrincipal().getProfileId(),
						userProfile.getPrincipal().getUserName(),ae);
				
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.UPLOAD_FILE_EXCEEDS_MAX_SIZE,
						new String[] { fileName }));
				//TODO need to find spring equivalent to destroy the file file.destroy();
				
			} else if (fileName.length() > MAX_FILE_NAME_LENGTH) {
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.SUBMISSION_FILE_NAME_TOO_LONG,
						new String[] { file.getOriginalFilename() }));
			} else if(fileInformation !=null && fileInformation.length() > 500)
			{
				//display error on page for fileInformation details
				errors.add(new GenericException(
						ErrorCodes.FILE_INFORMATION_EXCEEDS_MAX_SIZE,
						new String[] { "FileInformation" }));
				
			}
		}

		if (errors.size() > 0) {
			//re-populate form if any errors
			UserProfile userProfile = getUserProfile(request);

			try {
				populateForm(theForm, userProfile);
			} catch (SystemException e) {
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
				errors.add(new GenericException(
						ErrorCodes.UPLOAD_FILE_PROBLEM));
			}
		}
		
		return errors;
	}

	/**
	 * Prepares the log data as a String
	 */
	protected String prepareLogData(GFTUploadDetail result,
			Date startTime, Date endTime) {
		
		StringBuffer logData = new StringBuffer()
		.append("Date/Time file request recieved: ")
		.append(result.getReceivedDate())
		.append(" Confirmation number: ")
		.append(result.getSubmissionId())
		.append(" Contract number: ")
		.append(result.getContractNumber())
		.append(" Contract name: ")
		.append(result.getContractName())
		.append(" Username : ")
		.append(result.getUserName())
		.append(" Original file name: ")
		.append(result.getFileName())
		.append(" Submission type: ")
		.append(result.getSubmissionTypeCode())
		.append(" Payment effective date: ")
		.append(result.getRequestedPaymentEffectiveDate())
		.append(" File size: ")
		.append(result.getFileSizeInBytes())
		.append(" Upload start time : ")
		.append(startTime.toString())
		.append(" Upload end time: ")
		.append(endTime.toString());
		 
		return logData.toString();
	}
	
	/**
	 * Processes the Upload submission for Conversion File Types
	 * 
	 * @param form
	 * @param userProfile
	 * @throws SystemException
	 * @throws UploadFileCannotFoundOrEmptyException
	 */
	protected GFTUploadDetail processUploadRequest(AutoForm form, UserProfile userProfile)
	throws SystemException, UploadFileCannotFoundOrEmptyException {

		SubmissionConversionFileForm theForm = (SubmissionConversionFileForm) form;

		GFTUploadDetail gftUploadDetail = getGFTUploadDetail(userProfile);

		UploadInfoRequest uploadInfoRequest = new UploadInfoRequest();

		uploadInfoRequest.setUploadDetail(gftUploadDetail);

		MultipartFile file = theForm.getUploadFile();
		InputStream inputStream = null;
		
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
			try {
				inputStream = file.getInputStream();
			} catch(IOException e){
				throw new SystemException(e, "An Exception occured in " 
						+ "SubmissionConversionFileAction. processUploadRequest. "
						+ e.getMessage());
			}
	
			gftUploadDetail.setFileName(filterFileName(file.getOriginalFilename()));
			
			// set the file Information Value
			gftUploadDetail.setFileInformation(theForm.getFileInforamtion());
	
			//first upload file
			UploadFileResponse uploadFileResponse = 
				FileUploadServiceDelegate.getInstance().uploadFile(inputStream, gftUploadDetail);
				uploadInfoRequest.setTempFileName(uploadFileResponse.getTempFileName());
			gftUploadDetail.setSubmissionId(uploadFileResponse.getSubmissionId());
	
			if (gftUploadDetail.getFileSizeInBytes() > 0) {
				gftUploadDetail.setSubmissionTypeCode(theForm.getFileType());
			}
	
			//do cleanup
			try {
				inputStream.close();
				//TODO need to find spring equivalent to destroy the file file.destroy();
			} catch(IOException e) {
				SystemException se = new SystemException(e,
						"An Exception occured in " 
						+ "SubmissionConversionFileAction.processUploadRequest()"
						+ e.getMessage());
				
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			}
		}
	
		// get the response
		UploadInfoResponse infoResponse = 
			FileUploadServiceDelegate.getInstance().storeUploadInformation(uploadInfoRequest);
		gftUploadDetail.setReceivedDate(infoResponse.getReceivedDate());
		gftUploadDetail.setSubmissionId(infoResponse.getSubmissionId());
		return gftUploadDetail;
	}
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
