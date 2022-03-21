package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
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
import com.manulife.pension.ps.web.PsProperties;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.FileUploadServiceDelegate;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessIFileException;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.tools.exception.UploadFileExceedsMaxSizeException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.contract.valueobject.StatementPairVO;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;

/**
 *
 * Handles the submission upload process. This is a modified version of the
 * FileUploadAction.
 *
 * @author Tony Tomasone
 *
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"submissionUploadForm"})

public class SubmissionComboUploadController extends AbstractSubmitController {

	@ModelAttribute("submissionUploadForm") 
	public SubmissionUploadForm populateForm() 
	{
		return new SubmissionUploadForm();
	}
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put(INPUT,"/tools/submissionComboUpload.jsp");
		forwards.put("tools","redirect:/do/tools/toolsMenu/" );
		forwards.put("confirm","redirect:/do/tools/submissionComboUpload/?action=confirm");
		forwards.put("refresh","redirect:/do/tools/submissionComboUpload/?action=refresh");
		forwards.put("confirmPage","/tools/submissionComboUploadConfirmation.jsp" );
		forwards.put("subHistory","redirect:/do/tools/submissionComboUpload/");
	}

	
	private static final Date highDate = new GregorianCalendar(9999,Calendar.DECEMBER,31).getTime();

	/**
	 * Constructor.
	 */
	public SubmissionComboUploadController() {
		super(SubmissionComboUploadController.class);
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping, AutoForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/submissionComboUpload/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("submissionUploadForm") SubmissionUploadForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}

		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
			}
		}
	   
	   String validationResult = validate(actionForm, request);
		if(validationResult!=null){
			return validationResult;
		}
	   UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()) {
			return forwards.get( TOOLS);
		}

		if (userProfile.getContractProfile().getIfileConfig() == null ||
				actionForm.getPaymentInfo() == null) {
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
			logger.debug(StaticHelperClass.toString(userProfile.getContractProfile().getIfileConfig()));
		}

		actionForm.clear(request);

		//saveToken(request);

		try {
			populateForm(actionForm, userProfile);
		} catch (SystemException e) {
			Collection errors = new ArrayList();
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, e);
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(INPUT);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get(INPUT);
	}

	/**
	 * Performs the Send action.
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return @throws
	 *         SystemException
	 * @throws ServletException
	 */
	@RequestMapping(value ="/submissionComboUpload/",params= {"actionLabel=send"}, method =  {RequestMethod.POST}) 
	public String doSend (@Valid @ModelAttribute("submissionUploadForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> doSend");
		
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
		// now process the request
		

		// ensure year is 4 digits as the date parser doesn't do this for us
		if (!validateYear(form.getRequestEffectiveDate())) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_VALID_EFFECTIVE_DATE, new Object [] {}));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		SubmissionUploadDetailBean det = null;
		UserProfile userProfile = getUserProfile(request);
		Date startTime = null;
		Date endTime = null;

		try {
			startTime = new Date();
			GFTUploadDetail result = processUploadRequest(form, userProfile);
			//put data into form
			endTime = new Date();
			// log the file upload event
			logUploadEvent(result, startTime, endTime, userProfile.getPrincipal(), "doSend");

			det = new SubmissionUploadDetailBean(result);
			// override the name format stored in the database (FN LN) with a different one for the confirmation (LN, FN)
			det.setSender(userProfile.getPrincipal().getLastName().trim() + ", " + userProfile.getPrincipal().getFirstName());

			if(form.getLastPayroll() == null)
				det.setDisplayGenerateStatementSection(false);
			else
				det.setDisplayGenerateStatementSection(true);

			SessionHelper.setSubmissionUploadDetails(request, det);

			//resetToken(request);

			//we now have at least one item for history
			userProfile.getContractProfile().getIfileConfig().setCurrentContractHasUploadHistory(true);

			//quick fix to put the statements on the page
			ContractServiceDelegate contractService = ContractServiceDelegate.getInstance();

			Contract contract = userProfile.getCurrentContract();

			ContractStatementInfoVO statementInfoVO
				= contractService.getContractStatementInfo(contract.getContractNumber());

			if (statementInfoVO.isStatementsOutstanding()) {
				List statementDates = contractService.getStatementDates(contract.getContractNumber());
				for (int i = 0; i < statementDates.size(); i++) {
					StatementPairVO statementPair = (StatementPairVO)statementDates.get(i);
					// if the start date of the statment period is the dummy date, set the start date to
					// contract effective date
					if (statementPair.getStartDate().equals(highDate)) {
						statementPair.setStartDate(contract.getEffectiveDate());
					}
				}

				statementInfoVO.setStatementDates(statementDates);
				det.setStatementDates(statementInfoVO.getStatementDates());
			}

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
			logger.debug("exit <- doSend");

		if(det != null
				&& ((det.getBillTotal()+det.getCreditTotal()+det.getContributionTotal()) == 0.0d)
				&& (form.getFileType().equals("C")|| form.getFileType().equals("X"))
				&& !userProfile.isInternalUser()) {
			request.getSession(false).setAttribute("displayWarning", new Boolean(true));
		}
		return forwards.get(CONFIRMATION);
	}
	
	protected void populateForm(AutoForm form, UserProfile userProfile)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");

		
		Contract contract = userProfile.getCurrentContract();
		SubmissionUploadForm theForm = (SubmissionUploadForm) form;
		theForm.setDisplayFileUploadSection(
				!contract.getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED)
				&& userProfile.isAllowedUploadSubmissions()
                && !userProfile.isBeforeCAStatusAccessOnly());

		theForm.setDisplayPaymentInstructionSection(!contract.getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED)
                && !userProfile.isBeforeCAStatusAccessOnly()
				&& (userProfile.isAllowedDirectDebit() || userProfile.isAllowedCashAccount()));

		Calendar calendar = adjustDate4pm(null);
		theForm.setCashAccountPresent(userProfile.getContractProfile()
				.getIfileConfig().isCashAccountPresent());
		theForm.setAccounts(userProfile.getContractProfile().getIfileConfig()
				.getAccounts());

		Date marketClose = null;
		try {
			marketClose = AccountServiceDelegate.getInstance().getNextNYSEClosureDateIgnoringEmergencyClosure(null);
		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"populateForm", "AccountException occurred while getting the NYSE close datetime.");
			throw se;
		}

		// set the NYSE close message
		Date currentDate = Calendar.getInstance().getTime();
		theForm.setMarketClose("");
		if (marketClose != null && marketClose.after(currentDate)) {
			long msDiff = marketClose.getTime() - currentDate.getTime();
			// less than 30 min to market close (30 * 60 * 1000)
			if (msDiff < PsProperties.getNYStockClosureTimeLimit() * 60 * 1000) {
				SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
				theForm.setMarketClose("The NYSE will close at " + sdf.format(marketClose)
						+ " ET. For same day processing, your submission must be received in good order prior to that time.");
			}
		}

		GregorianCalendar calMarketClose = new GregorianCalendar();
		calMarketClose.setTime(marketClose);
		calMarketClose.set(Calendar.HOUR_OF_DAY,0);
		calMarketClose.set(Calendar.MINUTE,0);
		calMarketClose.set(Calendar.SECOND,0);
		calMarketClose.set(Calendar.MILLISECOND,0);

		SimpleDateFormat slashSDF = new SimpleDateFormat("MM/dd/yyyy");
		theForm.setRequestEffectiveDate(slashSDF.format(calMarketClose.getTime()));
		theForm.setDefaultEffectiveDate(calMarketClose.getTime());

		SimpleDateFormat fullSDF = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
		// set the calendar start date and end date range
		theForm.setCalendarStartDate(fullSDF.format(calMarketClose.getTime()));
		GregorianCalendar calEndDate = new GregorianCalendar();
		calEndDate.setTime(marketClose);
		calEndDate.add(Calendar.DATE, DELTA_END_DAYS);
		theForm.setCalendarEndDate(fullSDF.format(calEndDate.getTime()));

		Date [] dates = new Date[DELTA_END_DAYS];

		for (int i = 0; i < DELTA_END_DAYS; i++) {
			dates[i] = calMarketClose.getTime();
			calMarketClose.add(Calendar.DATE, 1);
		}

		// filter out the dates that are not NYSE valid market trading days
		try {
			dates = AccountServiceDelegate.getInstance().getFilteredNYSEClosureDatesIgnoringEmergencyClosure(null,dates);
			theForm.setAllowedMarketDates(dates);
		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(),
					"populateForm", "AccountException occurred while getting the NYSE filtered dates.");
			throw se;
		}

        if (theForm.isDisplayFileUploadSection()) {
    		// Get the statement info for the contract
    		ContractServiceDelegate contractService = ContractServiceDelegate.getInstance();

    		ContractStatementInfoVO statementInfoVO
    			= contractService.getContractStatementInfo(contract.getContractNumber());

    		if (statementInfoVO.isStatementsOutstanding()) {
    			List statementDates = contractService.getStatementDates(contract.getContractNumber());
    			// select a maximum of four statements for display
    			int maximumStatementCount = 4;
    			if (statementDates.size() < maximumStatementCount) {
    				maximumStatementCount = statementDates.size();
    			}
    			List revisedStatementDates = new ArrayList();
    			for (int i = 0; i < maximumStatementCount; i++) {
    				StatementPairVO statementPair = (StatementPairVO)statementDates.get(i);
    				// if the start date of the statment period is the dummy date, set the start date to
    				// contract effective date
    				if (statementPair.getStartDate().equals(highDate)) {
    					statementPair.setStartDate(contract.getEffectiveDate());
    				}
    				revisedStatementDates.add(statementPair);
    			}

    			statementInfoVO.setStatementDates(revisedStatementDates);
    			theForm.setDisplayGenerateStatementSection(true);
    			theForm.setStatementDates(statementInfoVO.getStatementDates());
    		}
        }

		ContractPaymentInfoVO paymentInfoVO	= theForm.getPaymentInfo();

		// Decide whether or not to show the bill payment and temporary credit section
		IFileConfig iFileConfig = userProfile.getContractProfile().getIfileConfig();

		theForm.setDisplayBillPaymentSection(false);
		theForm.setDisplayTemporaryCreditSection(false);

		if (iFileConfig != null && iFileConfig.getAccounts() != null && iFileConfig.getAccounts().size() != 0) {
			if(paymentInfoVO != null && paymentInfoVO.getOutstandingBillPayment().doubleValue() > 0d)
				theForm.setDisplayBillPaymentSection(true);

			if(paymentInfoVO != null && paymentInfoVO.getOutstandingTemporaryCredit().doubleValue() > 0d)
				theForm.setDisplayTemporaryCreditSection(true);
		}
		
		if (SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal()) != null) {
			theForm.setEmail(SecurityServiceDelegate.getInstance().getUserInfo(
					userProfile.getPrincipal()).getEmail());
		}

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

		SubmissionUploadForm form = (SubmissionUploadForm) actionForm;

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

	public Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		// This code has been changed and added to
		// Validate form and request against penetration attack, prior to other validations 
		SubmissionUploadForm theForm = (SubmissionUploadForm) form;
		
		Collection errors = super.doValidate( form, request);

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
				//TODO need to find spring equivalent to destroy the file file.destroy();
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
	private GFTUploadDetail processUploadRequest(SubmissionUploadForm form, UserProfile userProfile)
	throws SystemException, UploadFileCannotFoundOrEmptyException {

		GFTUploadDetail gftUploadDetail = getGFTUploadDetail(userProfile);
		
		// store email from upload page
        gftUploadDetail.setNotificationEmailAddress(form.getEmail());

		UploadInfoRequest uploadInfoRequest = new UploadInfoRequest();

		uploadInfoRequest.setUploadDetail(gftUploadDetail);

		MultipartFile file = form.getUploadFile();
		InputStream inputStream = null;
		if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
			try {
				inputStream = file.getInputStream();
			} catch(IOException e){
				throw new SystemException(e, SubmissionUploadController.class.getName(), "processUploadRequest", "");
			}

			gftUploadDetail.setFileName(filterFileName(file.getOriginalFilename()));

			//first upload file
			UploadFileResponse uploadFileResponse = FileUploadServiceDelegate.getInstance().uploadFile(inputStream, gftUploadDetail);

			//do we have to do that if we send the whole file ?
			uploadInfoRequest.setTempFileName(uploadFileResponse.getTempFileName());
			gftUploadDetail.setSubmissionId(uploadFileResponse.getSubmissionId());

			if (gftUploadDetail.getFileSizeInBytes() > 0) {
				gftUploadDetail.setSubmissionTypeCode(form.getFileType());
				// get the generate statement flag
				if (form.getLastPayroll() != null && form.getLastPayroll().length() != 0) {
					if ("C".equals(form.getLastPayroll()))
						gftUploadDetail.setLastPayrollForQuarter(new Boolean(true));
					else if ("S".equals(form.getLastPayroll()))
						gftUploadDetail.setLastPayrollForQuarter(new Boolean(false));
					else
						gftUploadDetail.setLastPayrollForQuarter(null);
				}	else
					gftUploadDetail.setLastPayrollForQuarter(null);
			}

			//do cleanup
			try {
				inputStream.close();
				//TODO need to find spring equivalent to destroy the file file.destroy();
				form.setUploadFile(null);
			} catch(IOException e) {
				SystemException se = new SystemException(e, getClass()
						.getName(), "processUploadRequest", e.getMessage());
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			}
		}

		//now try payment instructions
		gftUploadDetail.setPaymentInstructions(retrievePaymentInstructions(form, userProfile));
		if (gftUploadDetail.getPaymentTotalAmount().isPositive()) {
			gftUploadDetail.setRequestedPaymentEffectiveDate(retrieveRequestedEffectiveDate(form, userProfile));
		}

		UploadInfoResponse infoResponse = FileUploadServiceDelegate.getInstance().storeUploadInformation(uploadInfoRequest);
		gftUploadDetail.setReceivedDate(infoResponse.getReceivedDate());
		gftUploadDetail.setSubmissionId(infoResponse.getSubmissionId());
		return gftUploadDetail;
	}
	
	@RequestMapping(value ="/submissionComboUpload/", params= {"action=confirm"}, method ={RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("submissionUploadForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doConfirm");
		}
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
		if (null == request.getSession(false).getAttribute(Constants.SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA)) {
		    forward = forwards.get(SUBMISSION_HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}
		return forwards.get(forward);
	}
	@RequestMapping(value ="/submissionComboUpload/",params= {"action=refresh"}, method =  {RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("submissionUploadForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
