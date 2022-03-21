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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
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
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.FileUploadServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.tools.exception.UploadFileExceedsMaxSizeException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

/**
 * 
 * Handles the vesting submission upload process. 
 * 
 * @author Diana Macean
 * 
 */
@Controller
@RequestMapping(value="/tools")
@SessionAttributes({"submissionVestingForm"})

public class SubmissionVestingController extends AbstractSubmitController {

	@ModelAttribute("submissionVestingForm") 
	public  SubmissionVestingForm populateForm() 
	
	{
		return new SubmissionVestingForm();
	}
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put(INPUT,"/tools/submissionVesting.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/");
		forwards.put("confirm","redirect:/do/tools/vestingUpload/?action=confirm");
		forwards.put("refresh","redirect:/do/tools/vestingUpload/?action=refresh"); 
		forwards.put("confirmPage","/tools/submissionVestingConfirmation.jsp");
		forwards.put("subHistory","redirect:/do/tools/submissionHistory/");}

	/**
	 * Constructor.
	 */
	public SubmissionVestingController() {
		super(SubmissionVestingController.class);
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping, AutoForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/vestingUpload/",  method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("submissionVestingForm") SubmissionVestingForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		if (userProfile.getContractProfile().getIfileConfig() == null) {
			try {
				setupUserProfile(userProfile, actionForm);
			} catch (UnableToAccessIFileException ex) {
				Collection errors = new ArrayList();
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
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

		
	//	saveToken(request);

		populateForm(actionForm, userProfile);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get(INPUT);
		}

	/**
	 * Performs the send action.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return @throws
	 *         SystemException
	 * @throws ServletException
	 */
	@RequestMapping(value ="/vestingUpload/" ,params={"actionLabel=send"}, method =  {RequestMethod.POST}) 
	public String doSend (@Valid @ModelAttribute("submissionVestingForm") SubmissionVestingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			//put data into form
			endTime = new Date();
			// log the file upload event
			logUploadEvent(result, startTime, endTime, userProfile.getPrincipal(), "doSubmit");
			
			FileUploadDetailBean det = new FileUploadDetailBean(result);
			// override the name format stored in the database (FN LN) with a different one for the confirmation (LN, FN)
			det.setSender(userProfile.getPrincipal().getLastName().trim() + ", " + userProfile.getPrincipal().getFirstName());


			SessionHelper.setFileUploadDetails(request, det);

			//resetToken(request);

			//we now have at least one item for history
			userProfile.getContractProfile().getIfileConfig().setCurrentContractHasUploadHistory(true);

		} catch (UploadFileCannotFoundOrEmptyException ex) {
			Collection errors = new ArrayList();
			MultipartFile file = form.getUploadFile();
			String fname = "";
			if (file != null)
				fname = file.getOriginalFilename();
			// log error in mrl

			LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ex);
			errors.add(new GenericException(ErrorCodes.UPLOAD_FILE_EMPTY,
					new String[] { fname }));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		} catch (SystemException e) {
			Collection errors = new ArrayList();
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
	@RequestMapping(value ="/vestingUpload/", params={"action=confirm"} , method =  {RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("submissionVestingForm") SubmissionVestingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		validate(form, request);
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(INPUT);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doConfirm");
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
	
	@RequestMapping(value ="/vestingUpload/",params= {"action=refresh"}, method =  {RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("submissionVestingForm") SubmissionVestingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

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

		//determine if we have upload history for this account
		String contractId = Integer.toString(userProfile.getCurrentContract()
				.getContractNumber());

		//set it up in userProfile
		userProfile.getContractProfile().setIfileConfig(ifileConfig);

		if (logger.isDebugEnabled())
			logger.debug("exit <- setupUserProfile");
	}

	protected void populateForm(AutoForm form, UserProfile userProfile)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");
		
		SubmissionVestingForm theForm = (SubmissionVestingForm) form;
        
        int contractNumber = userProfile.getCurrentContract().getContractNumber();
        
        // lets check the permissions
        TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractNumber);
        
        theForm.setDisplayFileUploadSection( 
            ((userProfile.isInternalUser() && userProfile.isAllowedSubmitUpdateVesting()) || 
             (userProfile.getRole().isExternalUser() && userProfile.getRole().isTPA()  && 
              userProfile.isAllowedSubmitUpdateVesting())) &&
             (CensusUtils.isVestingEnabled(contractNumber)) 
        );
        
        // if Vesting CSF is set to "calculated", populate/display vesting schedule
        if (CensusUtils.isVestingCalculated(contractNumber)) {  
            
            // Get vesting information
            final Integer contractId = userProfile.getCurrentContract().getContractNumber();
            final Collection<VestingSchedule> vestingSchedules = ContractServiceDelegate.getInstance().loadVestingSchedules(contractId);
            theForm.setVestingSchedules(vestingSchedules);
            theForm.setDisplaySchedule(true); 
        } else {
            theForm.setDisplaySchedule(false);
        }

		Calendar calendar = adjustDate4pm(null);
        
        theForm.setEmail(SecurityServiceDelegate.getInstance().getUserInfo(userProfile.getPrincipal()).getEmail());

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateForm");
	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {

		SubmissionVestingForm form = (SubmissionVestingForm) actionForm;

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

		Collection errors = doValidate( form, request);

		/*
		 * Errors are stored in the session so that our REDIRECT can look up the
		 * errors.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		return null;
	}

	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	

	  public Collection doValidate(ActionForm form, HttpServletRequest request) {
			// This code has been changed and added to
			// Validate form and request against penetration attack, prior to other validations.
			SubmissionVestingForm theForm = (SubmissionVestingForm) form;
			Collection errors = super.doValidate(form, request);

			if (theForm.isSendAction()) {

				//retrieve the file representation
				MultipartFile file = theForm.getUploadFile();
				UserProfile userProfile = getUserProfile(request);
				String fname = "";

				fname = file.getOriginalFilename();
				//retrieve the file size
				if (file == null || file.getSize() == 0) {
					//log the error in mrl
					UploadFileCannotFoundOrEmptyException ae =new UploadFileCannotFoundOrEmptyException(
							"SubmissionUploadAction", "doValidate "," file: " + 
							fname + " is empty ");
					LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ae);	
					// display the error on the page
					errors.add(new GenericException(
							ErrorCodes.UPLOAD_FILE_EMPTY,
							new String[] { file.getOriginalFilename() }));
				} else if (file.getSize() > Environment.getInstance()
						.getMaxUploadFileSizeKbytes() * 1024) {
					//log the error in mrl

					UploadFileExceedsMaxSizeException ae =new UploadFileExceedsMaxSizeException(
							"SubmissionUploadAction", "doValidate "," file: " + 
							fname + " exceeds max size ");
					LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ae);
					// display the error on the page
					errors.add(new GenericException(
							ErrorCodes.UPLOAD_FILE_EXCEEDS_MAX_SIZE,
							new String[] { fname }));
					//file.destroy();
				} else if (fname.length() > MAX_FILE_NAME_LENGTH) { 
					// display the error on the page
					errors.add(new GenericException(
							ErrorCodes.SUBMISSION_FILE_NAME_TOO_LONG, 
							new String[] { file.getOriginalFilename() }));
				}
			}

			if (errors.size() > 0) {
				//repopulate form if any errors
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
	protected String prepareLogData(GFTUploadDetail result,Date startTime, Date endTime) {
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
	 * Processes the Upload submission
	 * @param form
	 * @param userProfile
	 * @throws SystemException
	 * @throws UploadFileCannotFoundOrEmptyException
	 */
	protected GFTUploadDetail processUploadRequest(AutoForm form, UserProfile userProfile)
	throws SystemException, UploadFileCannotFoundOrEmptyException {

		SubmissionVestingForm theForm = (SubmissionVestingForm) form;

		GFTUploadDetail gftUploadDetail = getGFTUploadDetail(userProfile);
        
        // store email from upload page
        gftUploadDetail.setNotificationEmailAddress(theForm.getEmail());
			
		UploadInfoRequest uploadInfoRequest = new UploadInfoRequest();
		
		uploadInfoRequest.setUploadDetail(gftUploadDetail);
	
		MultipartFile file = theForm.getUploadFile();
		InputStream inputStream = null;
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
			try {
				inputStream = file.getInputStream();
			} catch(IOException e){
				throw new SystemException(e, SubmissionVestingController.class.getName(), "processUploadRequest", "");
			}
	
			gftUploadDetail.setFileName(filterFileName(file.getOriginalFilename()));
	
			//first upload file
			UploadFileResponse uploadFileResponse = FileUploadServiceDelegate.getInstance().uploadFile(inputStream, gftUploadDetail);
	
			//do we have to do that if we send the whole file ?
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
				SystemException se = new SystemException(e, getClass()
						.getName(), "processUploadRequest", e.getMessage());
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			}
		}
	
		UploadInfoResponse infoResponse = FileUploadServiceDelegate.getInstance().storeUploadInformation(uploadInfoRequest);
		gftUploadDetail.setReceivedDate(infoResponse.getReceivedDate());
		gftUploadDetail.setSubmissionId(infoResponse.getSubmissionId());
		return gftUploadDetail;
	}
	
}
