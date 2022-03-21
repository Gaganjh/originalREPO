package com.manulife.pension.ps.web.tools;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
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
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;
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
@SessionAttributes({"submissionPaymentForm"})

public class SubmissionPaymentController extends AbstractSubmitController {

	@ModelAttribute("submissionPaymentForm") 
	public SubmissionUploadForm populateForm() 
	{
		return new SubmissionUploadForm();
		}
	
	
	
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/tools/submissionPayment.jsp");
		forwards.put( "tools","redirect:/do/tools/toolsMenu/");
		forwards.put("confirm","redirect:/do/tools/makePayment/?action=confirm");
		forwards.put( "refresh","redirect:/do/tools/makePayment/?action=refresh");
		forwards.put("confirmPage","/tools/submissionPaymentConfirmation.jsp");
		forwards.put("subHistory","redirect:/do/tools/submissionHistory/");
	}

	/**
	 * Constructor.
	 */
	public SubmissionPaymentController() {
		super(SubmissionPaymentController.class);
	}
	
	@RequestMapping(value ="/makePayment/",  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("submissionPaymentForm") SubmissionUploadForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}
		
		String validationResult = validate(actionForm, request);
		if(validationResult!=null){
			return validationResult;
		}
		

		UserProfile userProfile = getUserProfile(request);

		// lets check the permissions
		if(
				!userProfile.isInternalUser() && !userProfile.isSubmissionAccess()
		) {
			return forwards.get( TOOLS);
		}

		if (
				userProfile.getContractProfile().getIfileConfig() == null ||
						actionForm.getPaymentInfo() == null
		) {
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

		actionForm.clear( request);

		//saveToken(request);
		/**
		 * totalContribution sessiocomes from
		 * \source\com\manulife\pension\ps\web\tools\ViewContributionDetailsNewAction.java
		 */
		BigDecimal totalContribution=(BigDecimal)
		request.getSession().getAttribute("totalContribution");
		if(totalContribution==null){
			totalContribution=new BigDecimal(0.00);
		}
		//set contribution total into SubmissionUploadForm
		actionForm.setContributionTotal(totalContribution);

		populateForm(actionForm, userProfile);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get("input");
	}

	/**
	 * Performs the Send action.
	 *
	 * @param bindingResult
	 * @param form
	 * @param request
	 * @param response
	 * @return @throws
	 *         SystemException
	 * @throws ServletException
	 */
	@RequestMapping(value ="/makePayment/", params= {"actionLabel=send"}, method =  {RequestMethod.POST}) 
	public String doSend (@Valid @ModelAttribute("submissionPaymentForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> doSend");
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	        }
		}
	
		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}

		// ensure year is 4 digits as the date parser doesn't do this for us
		if (!validateYear(form.getRequestEffectiveDate())) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_VALID_EFFECTIVE_DATE, new Object [] {}));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		// now process the request
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

			SubmissionUploadDetailBean det = new SubmissionUploadDetailBean(result);
			// override the name format stored in the database (FN LN) with a different one for the confirmation (LN, FN)
			det.setSender(userProfile.getPrincipal().getLastName().trim() + ", " + userProfile.getPrincipal().getFirstName());

			SessionHelper.setPaymentUploadDetails(request, det);

			//resetToken(request);

			//we now have at least one item for history
			userProfile.getContractProfile().getIfileConfig().setCurrentContractHasUploadHistory(true);

		} catch (UploadFileCannotFoundOrEmptyException e) {
			// shouldn't throw this exception as we're not uploading a file here but its declared
			Collection errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_CANNOT_CREATE_PAYMENT));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		} catch (SystemException e) {
			Collection errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.SUBMISSION_CANNOT_CREATE_PAYMENT));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(REFRESH);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- doSend");

		return forwards.get(CONFIRMATION);
	}

	protected void populateForm(AutoForm form, UserProfile userProfile)
			throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateForm");

		
		SubmissionUploadForm theForm = (SubmissionUploadForm) form;
		Contract contract = userProfile.getCurrentContract();

		theForm.setDisplayPaymentInstructionSection(
				!contract.getStatus().equals(Contract.STATUS_CONTRACT_DISCONTINUED)
                && !userProfile.isBeforeCAStatusAccessOnly()
				&& (
					userProfile.isAllowedDirectDebit()
					|| userProfile.isAllowedCashAccount()
				)
		);

		Calendar calendar = adjustDate4pm(null);

		theForm.setCashAccountPresent(userProfile.getContractProfile()
				.getIfileConfig().isCashAccountPresent());
		theForm.setAccounts(userProfile.getContractProfile().getIfileConfig()
				.getAccounts());

		/**
		 * setting the contribution total if user have single
		 * division
		 */
		ArrayList accounts=(ArrayList)theForm.getAccounts();
		if(accounts!=null){
		boolean multipleDivisionStatus=isMultipleDivision(accounts);
		if(!multipleDivisionStatus){

		java.util.Iterator iterator = accounts.iterator();
		int j=0;
		while(iterator.hasNext()){
			PaymentAccountBean paymentAccountBean= (PaymentAccountBean)iterator.next();
		    if(paymentAccountBean.getType().equals("D")){
		    	theForm.setAmounts(j,theForm.getContributionTotal()+"");
		    }
			j++;
			}
		}
		}


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
			if ( msDiff < PsProperties.getNYStockClosureTimeLimit() * 60 * 1000) {
				SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
				theForm.setMarketClose("The NYSE will close at "
							+ sdf.format(marketClose)
							+ " ET. For same day processing, your submission must be received in good order prior to that time."
						);
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

		for(int i = 0; i < DELTA_END_DAYS; i++) {
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

		ContractPaymentInfoVO paymentInfoVO	= theForm.getPaymentInfo();

		// Decide whether or not to show the bill payment and temporary credit section
		IFileConfig iFileConfig = userProfile.getContractProfile().getIfileConfig();

		theForm.setDisplayBillPaymentSection(false);
		theForm.setDisplayTemporaryCreditSection(false);

		if(iFileConfig != null && iFileConfig.getAccounts() != null && iFileConfig.getAccounts().size() != 0) {
			if(paymentInfoVO != null && paymentInfoVO.getOutstandingBillPayment().doubleValue() > 0d)
				theForm.setDisplayBillPaymentSection(true);

			if(paymentInfoVO != null && paymentInfoVO.getOutstandingTemporaryCredit().doubleValue() > 0d)
				theForm.setDisplayTemporaryCreditSection(true);
		}



		if (logger.isDebugEnabled())
			logger.debug("exit <- populateForm");
	}


	/**
	 * retun true if user have more than one division
	 * @param accounts
	 * @return
	 */
	public boolean isMultipleDivision(ArrayList accounts){
	java.util.Iterator iterator = accounts.iterator();

	boolean multipleDivisionStatus=false;
	int count=0;
	while(iterator.hasNext()){
		PaymentAccountBean paymentAccountBean= (PaymentAccountBean)iterator.next();
	    if(paymentAccountBean.getType().equals("D")){
	    	count=count+1;
	    }
	    if(count>1){
	    	multipleDivisionStatus=true;
	    	break;
	    }

		}
	return multipleDivisionStatus;
	}//end of isMultipleDivision

	/**
	 * Checks whether we're in the right state.
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

	public Collection doValidate(  ActionForm form,
			HttpServletRequest request) {
		// This code has been changed and added to Validate form and request
		// against penetration attack, prior to other validations
		SubmissionUploadForm theForm = (SubmissionUploadForm) form;
		
		Collection errors = super.doValidate( form, request);

		if (errors.size() > 0) {
			//repopulate form if any errors
			UserProfile userProfile = getUserProfile(request);

			try {
				populateForm(theForm, userProfile);
			} catch (SystemException e) {
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,e);
				errors.add(new GenericException(
						ErrorCodes.SUBMISSION_CANNOT_CREATE_PAYMENT));
			}
		}


		return errors;
	}

	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
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
		.append(" Payment effective date: ")
		.append(result.getRequestedPaymentEffectiveDate())
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
	protected GFTUploadDetail processUploadRequest(SubmissionUploadForm form, UserProfile userProfile)
	throws SystemException, UploadFileCannotFoundOrEmptyException {

		GFTUploadDetail gftUploadDetail = getGFTUploadDetail(userProfile);

		UploadInfoRequest uploadInfoRequest = new UploadInfoRequest();

		uploadInfoRequest.setUploadDetail(gftUploadDetail);

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
	@RequestMapping(value ="/makePayment/", params= {"action=confirm"}, method =  {RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("submissionPaymentForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doConfirm");
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		
		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}
		
		String forward = super.doConfirm(form, request,response);

		// lets check whether the confirm object is in the session
		if (null == request.getSession(false).getAttribute(Constants.PAYMENT_UPLOAD_HISTORY_DETAIL_DATA)) {
		    forward = forwards.get(SUBMISSION_HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forwards.get(forward);
	}
	
	@RequestMapping(value ="/makePayment/", params= {"action=refresh"}, method =  {RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("submissionPaymentForm") SubmissionUploadForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doRefresh");
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		
		String validationResult = validate(form, request);
		if(validationResult!=null){
			return validationResult;
		}
		
		String forward = super.doRefresh(form, request,response);

		// lets check whether the confirm object is in the session
		if (null == request.getSession(false).getAttribute(Constants.PAYMENT_UPLOAD_HISTORY_DETAIL_DATA)) {
		    forward = forwards.get(SUBMISSION_HISTORY);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doRefresh");
		}

		return forward;
	}

}
