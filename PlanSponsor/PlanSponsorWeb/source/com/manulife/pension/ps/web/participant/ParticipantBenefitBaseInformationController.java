package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.LIADisplayHelper;
import com.manulife.pension.ps.service.delegate.AccountServiceDelegateAdaptor;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantBenefitBaseDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.BenefitBaseBatchStatus;
import com.manulife.pension.service.account.valueobject.GiflMarketValue;
import com.manulife.pension.service.account.valueobject.ParticipantGiflData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.service.employee.valueobject.TradeRestriction;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationDataItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.ParticipantBenefitBaseInformationReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

@Controller
@RequestMapping(value = "/participant")
@SessionAttributes({ "participantBenefitBaseInformationForm" })

public class ParticipantBenefitBaseInformationController extends ReportController {

	@ModelAttribute("participantBenefitBaseInformationForm")
	public ParticipantBenefitBaseInformationForm populateForm() {
		return new ParticipantBenefitBaseInformationForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/participant/participantBenefitBaseInformation.jsp");
		forwards.put("default","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("sort","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("filter","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("page","/participant/participantBenefitBaseInformation.jsp");
		forwards.put("print","/participant/participantBenefitBaseInformation.jsp");
	}

	protected static EmployeeServiceDelegate employeeServiceDelegate = EmployeeServiceDelegate
			.getInstance(Constants.PS_APPLICATION_ID);

	private static Logger logger = Logger.getLogger(ParticipantBenefitBaseInformationController.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			ParticipantBenefitBaseInformationForm.FORMAT_DATE_SHORT_MDY);

	private static final String[] DOWNLOAD_COLUMN_HEADINGS = new String[] { "Transaction effective date",
			"Associated transaction number", "Transaction type", "Market value before transaction($)",
			"Transaction amount($)", "Benefit base change($)", "Resulting benefit base($)", "MHP reset", "SIL" };

	private static final String AMOUNT_FORMAT = "##0.00";

	private static final String CSV_HEADER_FROM_DATE = "From date";

	private static final String CSV_HEADER_TO_DATE = "To date";

	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";

	public static final String ALL_TYPES = "ALL";

	public static final String ZERO_DOLLAR = "0.00";

	public static final String CHECK_ZERO = "0";

	public static final String CHECK_ONE = "1";

	public static final String ZERO = "0";

	public static final String YES = "YES";

	public static final int DECIMAL_DIGITS_TWO = 2;

	static {
		sdf.setLenient(false);
	}

	// synchronized method to avoid race condition.
	protected static synchronized Date DateParser(String value) throws ParseException {
		return sdf.parse(value);
	}

	/**
	 * Constructor.
	 */
	public ParticipantBenefitBaseInformationController() {
		super(ParticipantBenefitBaseInformationController.class);
	}

	/**
	 * This is an overridden method. This method handles the book marking
	 * scenarios. This will check if PPT GIFL selection date is earlier then
	 * Contract GIFL selection date and redirect to home page if this condition
	 * is true. This logic is already exist in FRW and implementing in PSW as
	 * part of GIFL P3C.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 *             , IOException, SystemException
	 * @return ActionForward
	 * 
	 */
	public String preExecute(ParticipantBenefitBaseInformationForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> preExecute");
		}
		UserProfile profile = getUserProfile(request);
		String profileId = request.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID);
		if (getTask(request).equals(DEFAULT_TASK)) {
			if (StringUtils.isBlank(profileId) || !StringUtils.isNumeric(profileId)) {
				throw new SystemException("preExecute -> Profile Id is not present in the request.");
			}
			int contractNumber = profile.getCurrentContract().getContractNumber();
			// get the participant id
			int participantId = ParticipantServiceDelegate.getInstance()
					.getParticipantIdByProfileId(Long.valueOf(profileId), contractNumber);
			// get the participant GIFL data.
			ParticipantGiflData participantGiflData = AccountServiceDelegate.getInstance()
					.getParticipantGiflDetails(String.valueOf(contractNumber), String.valueOf(participantId));
			// This means PPT has never selected GIFL or PPT GIFL selection date
			// is
			// earlier then Contract GIFL selection date. This condition may be
			// true when PPT access this link through bookmark. Since in this
			// condition PPT can not see BB link in home page and Account
			// summary
			// page
			if (participantGiflData == null || participantGiflData.getGiflSelectionDate()
					.before(profile.getCurrentContract().getContractGiflSelectionDate())) {
				return forwards.get(Constants.HOME_URL);
			}
		}
		return super.preExecute(form, request, response);
	}

	@RequestMapping(value = "/participantBenefitBaseInformation/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefualt(
			@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			request.setAttribute(Constants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(Constants.ACCOUNT_DETAILS,
            		new ParticipantGiflData());
            request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
            return forwards.get("input");
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	}
	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=filter" }, method = {RequestMethod.POST, RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			request.setAttribute(Constants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(Constants.ACCOUNT_DETAILS,
            		new ParticipantGiflData());
            request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
            return forwards.get("input");
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}

	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=page" }, method = {RequestMethod.POST, RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			request.setAttribute(Constants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(Constants.ACCOUNT_DETAILS,
            		new ParticipantGiflData());
            request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
            return forwards.get("input");
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}
	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=print" }, method = {RequestMethod.POST, RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			request.setAttribute(Constants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(Constants.ACCOUNT_DETAILS,
            		new ParticipantGiflData());
            request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
            return forwards.get("input");
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}

	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=sort"}, method = {RequestMethod.POST, RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			}
			request.setAttribute(Constants.BENEFIT_DETAILS,
                    new ParticipantBenefitBaseDetails());
            request.setAttribute(Constants.ACCOUNT_DETAILS,
            		new ParticipantGiflData());
            request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
            request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
            return forwards.get("input");
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}

	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=download" }, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				doCommon(form, request, response);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
			
		}String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}

	@RequestMapping(value = "/participantBenefitBaseInformation/", params = {"task=downloadAll" }, method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("participantBenefitBaseInformationForm") ParticipantBenefitBaseInformationForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				doCommon(form, request, response);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
				}
		}
		
		String forwarPreExecute=preExecute(form, request, response);
		if(forwarPreExecute!=null){
			return forwards.get("forwarPreExecute");
		}
		Collection formErrors = doValidate(form, request);

		if (!formErrors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, formErrors);
			return forwards.get("input");
		}
		else{
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Collection doValidate( ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate()");
		}


		Collection<GenericException> errors = super.doValidate( participantBenefitBaseInformationForm,
				request);

		ArrayList<GenericException> transactionErrors = new ArrayList<GenericException>();

		// Get the task for From date and To date in Transaction history
		if (getTask(request).equals(FILTER_TASK)) {
			transactionErrors = (ArrayList<GenericException>) validateTransactionHistory(
					participantBenefitBaseInformationForm, request);
		}

		errors.addAll(transactionErrors);

		try {

			if (request
					.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID) != null) {
				participantBenefitBaseInformationForm
						.setProfileId((String) request
								.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
			}
			retrieveParticipantDetails(participantBenefitBaseInformationForm
					.getProfileId(), request);
			getParticipantBenefitBaseAccountDetails((BaseReportForm) participantBenefitBaseInformationForm,
					request);
			getParticipantLIADetails(participantBenefitBaseInformationForm
					.getProfileId(), request);
			if (transactionErrors.size() == 0) {
				doCommon( (BaseReportForm) participantBenefitBaseInformationForm, request, null);
			}

		} catch (SystemException se) {
			request.setAttribute(Constants.BENEFIT_DETAILS,
					new ParticipantBenefitBaseDetails());
			request.setAttribute(Constants.ACCOUNT_DETAILS,
					new ParticipantGiflData());
			request.setAttribute(Constants.LIA_DETAILS,
					new LifeIncomeAmountDetailsVO());
			
			// Log the system exception.
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);

			// Show user friendly message.
			errors.clear();
			errors.add(new GenericException(ErrorCodes.TECHNICAL_DIFFICULTIES));
			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, YES);
		}
		return errors;
	}

	/**
	 * Validate the input form.The search field must not be empty. To and From
	 * dates must be valid format. FROM date must be less than TO date.
	 * 
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @return Collection
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

	/**
	 * Validate the From date and the To Date in the Transaction History Section
	 * 
	 * @param participantBenefitBaseInformationForm
	 *            ParticipantBenefitBaseInformationForm
	 * @param request
	 *            HttpServletRequest
	 * @param mapping
	 *            ActionMapping
	 * @return Collection<GenericException>
	 */
	private Collection<GenericException> validateTransactionHistory(
			ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm, HttpServletRequest request) {

		ArrayList<GenericException> errors = new ArrayList<GenericException>();

		Date fromDate = new Date();
		Date toDate = new Date();
		boolean validDates = false;
		boolean validFromDate = false;
		boolean validFromDateRange = false;
		Date currentDate = getMax24MonthsCutOffDate();

		// FROM date empty
		if ((StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))) {

			errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
		}

		if ((StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))
				&& (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))) {

			errors.add(new GenericException(ErrorCodes.TO_DATE_EMPTY));
		}

		// Both dates empty
		if ((StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))) {

			errors.add(new GenericException(ErrorCodes.BOTH_DATES_EMPTY));
		}

		// Valid date format
		if ((participantBenefitBaseInformationForm.getToDate().trim().length() > 0)
				&& (participantBenefitBaseInformationForm.getFromDate().trim().length() > 0)) {

			try {
				fromDate = validateDateFormat(participantBenefitBaseInformationForm.getFromDate());
				toDate = validateDateFormat(participantBenefitBaseInformationForm.getToDate());
				validDates = true;
				validFromDate = true;
			} catch (Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validDates = false;
				validFromDate = false;
			}
		}

		// Empty FROM date, invalid TO date
		if ((StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (participantBenefitBaseInformationForm.getToDate().trim().length() > 0)) {

			try {
				toDate = validateDateFormat(participantBenefitBaseInformationForm.getToDate());
				validDates = true;
			} catch (Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validDates = false;
			}
		}

		// Invalid FROM date format, empty TO date
		if ((participantBenefitBaseInformationForm.getFromDate().trim().length() > 0)
				&& (participantBenefitBaseInformationForm.getToDate().trim().length() == 0)) {

			try {
				fromDate = validateDateFormat(participantBenefitBaseInformationForm.getFromDate());
				validFromDate = true;

			} catch (Exception e) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE));
				validFromDate = false;
			}
		}

		// Valid FROM date, empty TO date, and FROM date greater than
		// default TO date
		if (validFromDate && (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))) {

			if (fromDate.after(toDate)) {
				errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
				validFromDateRange = false;
			} else {
				validFromDateRange = true;
			}
		}

		// Valid FROM date, empty TO date, and FROM date not within the
		// last 24 months of default TO date
		if (validFromDate && (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))) {
			if (fromDate.before(currentDate)) {
				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
				validFromDateRange = false;
			} else {
				validFromDateRange = true;
			}
		}

		// If from date valid, date range valid, and to date is empty,
		// then set default TO Date
		if (validFromDateRange && (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
				&& (StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate()))) {
			Calendar cal = Calendar.getInstance();
			// cal.setTime(asOfDate);
			participantBenefitBaseInformationForm
					.setToDate(DateRender.formatByPattern(cal.getTime(), "", RenderConstants.MEDIUM_MDY_SLASHED));
		}

		// From date must be earlier than To date
		if (validDates
				&& ((!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
						&& (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate())))
				&& (fromDate.after(toDate))) {
			errors.add(new GenericException(ErrorCodes.FROM_DATE_AFTER_TO));
		}

		// From date outside 24 month range
		if (validDates
				&& ((!StringUtils.isEmpty(participantBenefitBaseInformationForm.getFromDate()))
						&& (!StringUtils.isEmpty(participantBenefitBaseInformationForm.getToDate())))
				&& (fromDate.before(currentDate))) {
			if (request.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PARTICIPANT_ID) != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				participantBenefitBaseInformationForm
						.setFromDate(DateRender.formatByPattern(cal.getTime(), "", RenderConstants.MEDIUM_MDY_SLASHED));
			} else {
				errors.add(new GenericException(ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
			}
		}

		return errors;
	}

	/**
	 * This method is used for validating Date
	 * 
	 * @param dateString
	 *            String
	 * @return Date
	 * @throws ParseException
	 */
	private Date validateDateFormat(String dateString) throws ParseException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateDateFormat");
		}
		Date validDate = null;
		if (!((dateString.trim().length() == 10) && (dateString.substring(2, 3).equals("/"))
				&& (dateString.substring(5, 6)).equals("/"))) {
			throw new ParseException("invalid date format", 0);
		}
		String month = dateString.substring(0, 2);
		String day = dateString.substring(3, 5);
		String year = dateString.substring(6, 10);
		try {
			Integer.parseInt(year);
			if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
				throw new ParseException("invalid month", 0);

			if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)
				throw new ParseException("invalid day", 0);

			if (Integer.parseInt(day) == 29 && (Integer.parseInt(month) == 2) && (Integer.parseInt(year) % 4 > 0))
				throw new ParseException("invalid day", 0);

		} catch (Exception e) {
			throw new ParseException("invalid date format", 0);
		}

		validDate = DateParser(dateString);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- validateDateFormat");
		}
		return validDate;
	}

	/**
	 * This is used for the validation of 24 months period
	 * 
	 * @return Date
	 */
	private Date getMax24MonthsCutOffDate() {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * This is method is used for specifying the default sort criteria
	 * 
	 * @return String
	 */
	protected String getDefaultSort() {
		return ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE;
	}

	/**
	 * This is method is used for specifying the default sort direction
	 * 
	 * @return String
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	/**
	 * This method is used for getting the data for download csv file
	 * 
	 * @param reportForm
	 * @parm report
	 * @parm request
	 * @return byte[]
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		ParticipantBenefitBaseInformationReportData data = (ParticipantBenefitBaseInformationReportData) report;
		StringBuffer buffer = new StringBuffer();

		ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		Date fromDate = new Date();
		Date toDate = new Date();
		Date asOfDate = theForm.getAsOfDate();
		boolean internalUser = userProfile.isInternalUser();
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

		ParticipantBenefitBaseDetails participantBenefitBaseDetails = (ParticipantBenefitBaseDetails) request
				.getAttribute(Constants.BENEFIT_DETAILS);

		ParticipantGiflData participantGiflData = (ParticipantGiflData) request.getAttribute(Constants.ACCOUNT_DETAILS);

		// To display Trade Restriction Date in CSV file for GIFL version 3
		TradeRestriction tradeRestriction = (TradeRestriction) request.getAttribute(Constants.TRADING_RESTRICTION);

		String strDeselectionDate = DateRender.formatByPattern(participantGiflData.getGiflDeselectionDate(), "",
				RenderConstants.MEDIUM_YMD_DASHED);

		String strActivationDate = DateRender.formatByPattern(participantGiflData.getGiflActivationDate(), "",
				RenderConstants.MEDIUM_YMD_DASHED);

		String strLastStepUpDate = DateRender.formatByPattern(participantGiflData.getGiflLastStepUpDate(), "",
				RenderConstants.MEDIUM_YMD_DASHED);

		String strTradingRestricationDate = DateRender.formatByPattern(tradeRestriction.getTradeRestrictionEndDate(),
				"", RenderConstants.MEDIUM_YMD_DASHED);
		String giflVersion = currentContract.getGiflVersion();
		String showTradingExpirationDate = Constants.NO;

		// Trading Expiration date will only be displayed if the this date is in
		// effect and it should be after GIFL selection activation date
		if (tradeRestriction != null && tradeRestriction.isTradeRestrictionInEffect()
				&& tradeRestriction.getTradeRestrictionStartDate().after(participantGiflData.getGiflSelectionDate())) {
			showTradingExpirationDate = Constants.YES;
		}

		// GIFL 2A : Benefit base batch out of date warning message
		if (Constants.YES.equals(theForm.getBbBatchDateLessThenETL())) {
			try {
				Content message = null;
				message = ContentCacheManager.getInstance().getContentById(
						ContentConstants.MISCELLANEOUS_BENEFIT_BASE_BATCH_OUT_OF_DATE,
						ContentTypeManager.instance().MESSAGE);

				buffer.append(ContentUtility.getContentAttribute(message, "text")).append(LINE_BREAK);

			} catch (ContentException exp) {
				throw new SystemException(exp, "Something wrong with CMA");
			}
		}

		// GIFL LIA : Lifetime Income Amount selected warning message
		if (theForm.isShowLIADetailsSection()) {
			String liaMessage = getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_BENEFIT_BASE_LIA_MESSAGE);
			buffer.append(QUOTE).append(liaMessage).append(QUOTE).append(LINE_BREAK);
		}

		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		// Get dates for display
		buffer.append("As of,").append(DateRender.formatByPattern(asOfDate, "", RenderConstants.MEDIUM_MDY_SLASHED))
				.append(LINE_BREAK).append(LINE_BREAK);

		try {
			fromDate = format.parse(theForm.getFromDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"ParseException in fromDate getDownloadData()" + " ParticipantBenefitBaseInformationAction:",
						parseException);
			}
		}
		try {
			toDate = format.parse(theForm.getToDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"ParseException in fromDate getDownloadData()" + " ParticipantBenefitBaseInformationAction:",
						parseException);
			}
		}
		// SSE024, mask ssn if no download report full ssn permission
		boolean maskSSN = true;
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(userProfile, currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}
		try {
			// Retrieving participant summary details
			buffer.append("Last name,First name,Middle Initial,SSN,");

			if (participantBenefitBaseDetails.getDateOfBirth() != null) {
				buffer.append("Birth date,");
			}
			buffer.append("Benefit base,");
			buffer.append("Market value,");

			buffer.append(LINE_BREAK);
			buffer.append(QUOTE).append(participantBenefitBaseDetails.getLastName()).append(QUOTE).append(COMMA);
			buffer.append(QUOTE).append(participantBenefitBaseDetails.getFirstName()).append(QUOTE).append(COMMA);
			if (participantBenefitBaseDetails.getMiddleInitial() != null) {
				buffer.append(QUOTE).append(participantBenefitBaseDetails.getMiddleInitial()).append(QUOTE)
						.append(COMMA);
			} else {
				buffer.append(QUOTE).append("").append(QUOTE).append(COMMA);
			}
			buffer.append(SSNRender.format(new String(participantBenefitBaseDetails.getSsn()), null, maskSSN))
					.append(COMMA);
			// Checking for birthdate default condition
			if (participantBenefitBaseDetails.getDateOfBirth() != null)
				buffer.append(participantBenefitBaseDetails.getDateOfBirth()).append(COMMA);

			buffer.append(NumberRender.formatByPattern(participantGiflData.getGiflBenefitBaseAmt(), ZERO_AMOUNT_STRING,
					AMOUNT_FORMAT)).append(COMMA);

			if (!strDeselectionDate.equals(Constants.DEFAULT_DATE)) {
				buffer.append(ZERO_AMOUNT_STRING).append(COMMA);
			} else {
				buffer.append(NumberRender.formatByPattern(participantBenefitBaseDetails.getMarketValueGoFunds(),
						ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			}

			buffer.append(LINE_BREAK).append(LINE_BREAK);

			buffer.append("Selection date,");

			if (!strDeselectionDate.equals(Constants.DEFAULT_DATE)) {
				buffer.append("Deactivation date, ");
			}
			buffer.append("Activation date,");
			// This should be hide for valid LIA anniversary date
			if (!theForm.isShowLIADetailsSection()) {
				buffer.append("Anniversary Date,");
			}
			buffer.append("Holding period expiry date,");

			// Added new Fields which should be displayed if the contract has
			// GIFL version 3 features

			// Trading Expiration date will only be displayed if the this date
			// is in effect and it should be after GIFL selection activation
			// date
			if (Constants.YES.equals(showTradingExpirationDate)) {
				buffer.append("Trading restriction expiry date,");
			}
			if (Constants.GIFL_VERSION_03.equals(giflVersion)) {
				buffer.append("Rate for last Income Enhancement,");
				buffer.append("Last Income Enhancement date,");
				buffer.append("Value changed at last Income Enhancement");
			} else {
				buffer.append("Last step-up date,");
				buffer.append("Value changed at last Step-Up");
			}
			buffer.append(LINE_BREAK);

			buffer.append(DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(), "",
					RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

			// Check for end date is defalut or not
			if (!strDeselectionDate.equals(Constants.DEFAULT_DATE))
				buffer.append(DateRender.formatByPattern(participantGiflData.getGiflDeselectionDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

			// Check for active date conditions
			if (!strDeselectionDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(Constants.NA).append(COMMA);

			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& strActivationDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(Constants.AWAITING_DEPOSIT).append(COMMA);

			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& !strActivationDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(DateRender.formatByPattern(participantGiflData.getGiflActivationDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			}
			// This should be hide for valid LIA anniversary date
			if (!theForm.isShowLIADetailsSection()) {
				// To display the anniversary date in date format
				if (!strDeselectionDate.equals(Constants.DEFAULT_DATE)) {

					buffer.append(Constants.NA).append(COMMA);

				} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
						&& strActivationDate.equals(Constants.DEFAULT_DATE)) {

					buffer.append(Constants.AWAITING_DEPOSIT).append(COMMA);

				} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
						&& !strActivationDate.equals(Constants.DEFAULT_DATE)) {

					buffer.append(DateRender.formatByPattern(participantGiflData.getGiflNextStepUpDate(), "",
							RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
				}
			}
			// Check for Holding period expiry date conditions
			if (!strDeselectionDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(Constants.NA).append(COMMA);
			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& strActivationDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(Constants.AWAITING_DEPOSIT).append(COMMA);
			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& !strActivationDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(DateRender.formatByPattern(participantGiflData.getGiflHoldingPeriodExpDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			}

			// Check for Trading Restriction Expiry Date conditions
			if (Constants.YES.equals(showTradingExpirationDate)) {
				if (Constants.DEFAULT_DATE.equals(strTradingRestricationDate)) {
					buffer.append(Constants.NA).append(COMMA);
				} else {
					buffer.append(strTradingRestricationDate).append(COMMA);
				}
			}

			if (Constants.GIFL_VERSION_03.equals(giflVersion)) {
				// Display Rate for last Income Enhancement Amount
				buffer.append(participantGiflData.getDisplayRateForLastIncomeEnhancement()).append(COMMA);
			}

			// Check for Last step up date conditions
			if (Constants.DEFAULT_DATE.equals(strLastStepUpDate)) {
				buffer.append(Constants.NA).append(COMMA);
			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& !strLastStepUpDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(DateRender.formatByPattern(participantGiflData.getGiflLastStepUpDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			} else { // For the scenario, GIFL is deselected and atleast one
						// step-up had occured

				buffer.append(DateRender.formatByPattern(participantGiflData.getGiflLastStepUpDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
			}

			// Check for Value changed at Last step up date conditions
			if (Constants.DEFAULT_DATE.equals(strLastStepUpDate)) {

				buffer.append(Constants.NA).append(COMMA);
			} else if (strDeselectionDate.equals(Constants.DEFAULT_DATE)
					&& !strLastStepUpDate.equals(Constants.DEFAULT_DATE)) {

				buffer.append(NumberRender.formatByPattern(participantGiflData.getGiflLastStepUpChangeAmt(),
						ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			} else { // For the scenario, GIFL is deselected and atleast one
						// step-up had occured

				buffer.append(NumberRender.formatByPattern(participantGiflData.getGiflLastStepUpChangeAmt(),
						ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			}

			buffer.append(LINE_BREAK).append(LINE_BREAK);

			if (theForm.isShowLIADetailsSection()) {
				// Get dates for display
				buffer.append(getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_LIA_SELECTION_DATE_FIELD_LABEL))
						.append(COMMA);
				buffer.append(getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_LIA_SPOUSAL_OPTION_FIELD_LABEL))
						.append(COMMA);
				buffer.append(getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_LIA_PERCENTAGE_FIELD_LABEL))
						.append(COMMA);
				buffer.append(getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_ANNUAL_LIA_AMOUNT_FIELD_LABEL))
						.append(COMMA);
				buffer.append(
						getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_LIA_PAYMENT_FREQUENCY_FIELD_LABEL))
						.append(COMMA);
				buffer.append(
						getContentMessageByCmaKey(ContentConstants.MISCELLANEOUS_LIA_ANNIVERSARY_DATE_FIELD_LABEL));
				buffer.append(LINE_BREAK);

				buffer.append(DateRender.formatByPattern(theForm.getLiaSelectionDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
				buffer.append(QUOTE).append(theForm.getLiaIndividualOrSpousalOption()).append(QUOTE).append(COMMA);

				buffer.append(QUOTE).append(theForm.getLiaPercentage()).append(QUOTE).append(COMMA);

				if (theForm.getLiaAnnualAmount() != null) {
					buffer.append(NumberRender.formatByPattern(theForm.getLiaAnnualAmount(), ZERO_AMOUNT_STRING,
							AMOUNT_FORMAT));
				}
				buffer.append(COMMA);
				buffer.append(QUOTE).append(theForm.getLiaFrequencyCode()).append(QUOTE);

				if (theForm.getLiaPeriodicAmt() != null) {
					buffer.append(" - ").append(NumberRender.formatByPattern(theForm.getLiaPeriodicAmt(),
							ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
				}
				buffer.append(COMMA);
				buffer.append(DateRender.formatByPattern(theForm.getLiaAnniversaryDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA).append(LINE_BREAK).append(LINE_BREAK);
			}
			String fromDateStr = DateRender.format(fromDate, RenderConstants.MEDIUM_MDY_SLASHED);

			String toDateStr = DateRender.format(toDate, RenderConstants.MEDIUM_MDY_SLASHED);

			buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
			buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
			buffer.append(LINE_BREAK);
			buffer.append(fromDateStr).append(COMMA);
			buffer.append(toDateStr).append(LINE_BREAK);

			buffer.append(LINE_BREAK);

			if (data.getDetails().size() == 0) {
				Content message = null;
				message = ContentCacheManager.getInstance().getContentById(
						ContentConstants.MESSAGE_NO_HISTORY_TRANSACTION_FOR_DATE_SELECTED,
						ContentTypeManager.instance().MESSAGE);

				buffer.append(ContentUtility.getContentAttribute(message, "text")).append(LINE_BREAK);
			} else {
				for (int i = 0; i < DOWNLOAD_COLUMN_HEADINGS.length; i++) {
					// Columns other than SIL
					if (!DOWNLOAD_COLUMN_HEADINGS[i].equals("SIL")) {
						buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
						// SIL column will be shown only to internal users.
					} else if (internalUser) {
						buffer.append(DOWNLOAD_COLUMN_HEADINGS[i]);
					}
					if (i != DOWNLOAD_COLUMN_HEADINGS.length - 1)
						buffer.append(COMMA);
				}
			}
			buffer.append(LINE_BREAK);
			Iterator iterator = report.getDetails().iterator();
			while (iterator.hasNext()) {
				ParticipantBenefitBaseInformationDataItem theItem = (ParticipantBenefitBaseInformationDataItem) iterator
						.next();
				String effectiveTransactionDate = DateRender.format(theItem.getTransactionEffectiveDate(), null);
				String associatedTransactionNumber = "";
				if (!theItem.getTransactionNumber().equals("0"))
					associatedTransactionNumber = theItem.getTransactionNumber().toString();
				String transactionTypeDesc = theItem.getTransactionType();
				buffer.append(effectiveTransactionDate).append(COMMA);
				buffer.append(associatedTransactionNumber).append(COMMA);
				buffer.append(transactionTypeDesc).append(COMMA);

				// In CSV files, across the applications, amount values are
				// displayed without zeros after the decimal
				// point. (Ex. 1200.00 as 1200). The amount strings in the BB
				// Report are formatted with commas so
				// they will be displayed with the zeros in csv files. To keep
				// this report consistent with other
				// reports in ezk and psw application we have to remove the
				// commas.

				buffer.append(getCsvString(removeCommas(theItem.getMarketValueBeforeTransaction()))).append(COMMA);

				buffer.append(getCsvString(removeCommas(theItem.getTransactionAmount()))).append(COMMA);
				buffer.append(getCsvString(removeCommas(theItem.getBenefitBaseChangeAmount()))).append(COMMA);
				buffer.append(getCsvString(removeCommas(NumberRender.formatByType(theItem.getBenefitBaseAmount(),
						"0.00", "d", DECIMAL_DIGITS_TWO, BigDecimal.ROUND_UP, 1)))).append(COMMA);
				buffer.append(theItem.getHoldingPeriodInd()).append(COMMA);
				// SIL number will not be shown to the external users
				if (internalUser) {
					buffer.append(theItem.getValidatedSILNumber()).append(COMMA);
				}
				buffer.append(LINE_BREAK);
			}
		} catch (ContentException exp) {
			throw new SystemException(exp, "Something wrong with CMA");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * This method is used to remove commas in a string.
	 * 
	 * @param value
	 * 
	 * @return String a commaless string
	 */
	private String removeCommas(String value) {
		if (value != null && value.indexOf(",") != -1) {
			value = value.replaceAll(",", "");
		}
		return value;
	}

	/**
	 * Set sorting criteria
	 * 
	 * @param criteria
	 * @param form
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateSortCriteria");
		}

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);

		// Sort by descending (ascending) Effective transaction date
		if (sortField.equals(ParticipantBenefitBaseInformationReportData.SORT_FIELD_EFFECTIVE_DATE)) {
			criteria.insertSort(ParticipantBenefitBaseInformationReportData.SORT_FIELD_SEQNO, sortDirection);

			criteria.insertSort(ParticipantBenefitBaseInformationReportData.SORT_FIELD_TRANSACTION_NO, sortDirection);

		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateSortCriteria");
		}
	}

	/**
	 * To get the Participant's benefit base account details
	 * 
	 * @param reportForm
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 * @throws SystemException
	 */

	private void getParticipantBenefitBaseAccountDetails(BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantBenefitBaseAccountDetails");
		}
		int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber();

		ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

		int participantIdInt = ParticipantServiceDelegate.getInstance()
				.getParticipantIdByProfileId(Long.parseLong(theForm.getProfileId()), contractNumber);
		theForm.setParticipantId(String.valueOf(participantIdInt));
		theForm.setProposalNumber(getUserProfile(request).getCurrentContract().getProposalNumber());

		ParticipantGiflData participantGiflData = AccountServiceDelegate.getInstance()
				.getParticipantGiflDetails(String.valueOf(contractNumber), theForm.getParticipantId());
		if (participantGiflData == null) {
			StringBuffer exceptionMessage = new StringBuffer();
			exceptionMessage.append("Participant belongs to contractId:");
			exceptionMessage.append(contractNumber);
			exceptionMessage.append(" ,profileId:");
			exceptionMessage.append(theForm.getProfileId());
			exceptionMessage.append(" has never selected gateway");

			throw new SystemException(exceptionMessage.toString());
		}

		participantGiflData.setParticipantId(new BigDecimal(theForm.getParticipantId()));

		// set the default from date
		if (theForm.getFromDate() == null) {
			theForm.setFromDate(getDefaultFromDate(participantGiflData.getGiflSelectionDate()));
		}

		// set the gifl selection date as participant's latest GIFL selection
		// date
		theForm.setGiflSelectionDate(DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(), "",
				RenderConstants.MEDIUM_MDY_SLASHED));

		// Get the Trade Restriction Expiration Date and whether this date is in
		// effect
		try {
			request.setAttribute(Constants.TRADING_RESTRICTION, getTradingRestriction(Long.valueOf(participantIdInt),
					Integer.valueOf(getUserProfile(request).getCurrentContract().getProposalNumber())));
		} catch (Exception e) {
			logger.error(
					"ParticipantBenefitBaseInformationAction -> getParticipantBenefitBaseAccountDetails -> getTradingRestriction");
			throw new SystemException(e.getMessage());
		}

		// Set the GIFL data object in the request
		request.setAttribute(Constants.ACCOUNT_DETAILS, participantGiflData);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- participantGiflData");
		}

	}

	/**
	 * To popluate report action form.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param reportForm
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 * @throws SystemException
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		super.populateReportForm(reportForm, request);

		ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) reportForm;

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		ContractDatesVO contractDatesVO = currentContract.getContractDates();

		ParticipantBenefitBaseDetails benefitDetails = (ParticipantBenefitBaseDetails) request
				.getAttribute(Constants.BENEFIT_DETAILS);
		if (benefitDetails != null) {
			theForm.setAsOfDate(benefitDetails.getAsOfDate());
		}
		// make do with cached rundate in absence of live value
		if (theForm.getAsOfDate() == null) {
			theForm.setAsOfDate(contractDatesVO.getAsOfDate());
		}

		ParticipantGiflData participantGiflData = (ParticipantGiflData) request.getAttribute(Constants.ACCOUNT_DETAILS);

		Date bbBatchRundate = theForm.getAsOfDate(); // used by date range and
														// footnote logic,
														// default to as of date
														// if not obtainable
		BenefitBaseBatchStatus benefitBaseBatchStatus = null;
		try {
			AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();
			benefitBaseBatchStatus = asd.getBenefitBaseBatchStatusDetails(Constants.PS_APPLICATION_ID);
			if (benefitBaseBatchStatus != null && benefitBaseBatchStatus.getCycleDate() != null) {
				bbBatchRundate = benefitBaseBatchStatus.getCycleDate();
			}
		} catch (Exception sue) {
			logger.error(
					"ParticipantBenefitBaseInformationAction -> populateReportForm -> getBenefitBaseBatchStatusDetails");
		}

		// Set default FROM and TO dates
		if (theForm.getToDate() == null || theForm.getFromDate() == null) {

			try {
				theForm.setToDate(DateRender.formatByPattern(bbBatchRundate, "", RenderConstants.MEDIUM_MDY_SLASHED));

				theForm.setFromDate(getDefaultFromDate(bbBatchRundate));

			} catch (Exception exception) {
				exception.getCause().getMessage();
			}
		}

		if (participantGiflData != null) {

			// checking for benefit base batch out of date status
			if (benefitBaseBatchStatus != null
					&& Constants.BB_BATCH_STATUS.equals(benefitBaseBatchStatus.getBenefitBaseBatchStatus())) {
				theForm.setBbBatchDateLessThenETL(Constants.YES);
			} else {
				theForm.setBbBatchDateLessThenETL(Constants.NO);
			}

			String strLastStepUpDate = DateRender.formatByPattern(participantGiflData.getGiflLastStepUpDate(), "",
					RenderConstants.MEDIUM_YMD_DASHED);

			// To set the showFootnote flag
			if (!strLastStepUpDate.equals(Constants.DEFAULT_DATE)
					&& participantGiflData.getGiflLastStepUpDate().compareTo(bbBatchRundate) > 0) {
				theForm.setShowFootnote("Y");
			} else {
				theForm.setShowFootnote("N");
			}

			// trade restriction information needs to be re-populated in form
			// here
			TradeRestriction tradingRestriction = (TradeRestriction) request
					.getAttribute(Constants.TRADING_RESTRICTION);
			// Trading Expiration date will only be displayed if the this date
			// is in effect and it should be after GIFL selection activation
			// date
			if (tradingRestriction.isTradeRestrictionInEffect() && tradingRestriction.getTradeRestrictionStartDate()
					.after(participantGiflData.getGiflSelectionDate())) {
				theForm.setShowTradingExpirationDate(Constants.YES);
				String tradingRestrictionExpDate = DateRender.formatByPattern(
						tradingRestriction.getTradeRestrictionEndDate(), "", RenderConstants.MEDIUM_YMD_DASHED);
				if (Constants.DEFAULT_DATE.equals(tradingRestrictionExpDate) || "".equals(tradingRestrictionExpDate)) {
					tradingRestrictionExpDate = Constants.NA;
				} else {
					tradingRestrictionExpDate = DateRender.formatByPattern(
							tradingRestriction.getTradeRestrictionEndDate(), "", RenderConstants.EXTRA_LONG_MDY);
				}
				theForm.setDisplayTradingExpirationDate(tradingRestrictionExpDate);
			} else {
				theForm.setShowTradingExpirationDate(Constants.NO);
			}

			// Set Gifl Selection date
			if (theForm.getGiflSelectionDate() == null) {
				theForm.setGiflSelectionDate(DateRender.formatByPattern(participantGiflData.getGiflSelectionDate(), "",
						RenderConstants.MEDIUM_MDY_SLASHED));
			}
		}
		// populating LIA details to reportFrorm
		LifeIncomeAmountDetailsVO lifeIncomeAmountDetails = (LifeIncomeAmountDetailsVO) request
				.getAttribute(Constants.LIA_DETAILS);
		populateParticipantLIADetailsToForm(theForm, lifeIncomeAmountDetails);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}

	/**
	 * This method is used for getting ReportId
	 * 
	 * @return ReportId String
	 */
	protected String getReportId() {
		return ParticipantBenefitBaseInformationReportData.REPORT_ID;
	}

	/**
	 * This method is used for getting ReportName
	 * 
	 * @return ReportName String
	 */
	protected String getReportName() {
		return ParticipantBenefitBaseInformationReportData.REPORT_NAME;
	}

	/**
	 * This method is used for specifying the ReportCriteria
	 * 
	 * @param criteria
	 *            ReportCriteria
	 * @param reportForm
	 *            BaseReportForm
	 * @param request
	 *            HttpServletRequest
	 * @throws SystemException
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		Boolean requiredReportData;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		ParticipantBenefitBaseInformationForm theForm = (ParticipantBenefitBaseInformationForm) form;

		request.setAttribute(Constants.PARTICIPANT_ID, theForm.getParticipantId());

		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

		if (theForm.getProfileId() == null) {
			theForm.setProfileId(request.getParameter(ParticipantBenefitBaseInformationForm.PARAMETER_KEY_PROFILE_ID));
		}
		// This logic is modified, since the variable ParticipantGiflData needs
		// to be removed from instance scope
		if (theForm.getParticipantId() == null || theForm.getParticipantId().length() == 0) {
			ParticipantGiflData participantGiflData = (ParticipantGiflData) request
					.getAttribute(Constants.ACCOUNT_DETAILS);
			theForm.setParticipantId(participantGiflData.getParticipantId().toString());
		}

		if (theForm.getProposalNumber() == null || theForm.getProposalNumber().length() == 0) {

			String proposalNumber = getUserProfile(request).getCurrentContract().getProposalNumber();
			theForm.setProposalNumber(proposalNumber);
		}

		// Get the from Date
		if (!StringUtils.isEmpty(theForm.getFromDate())) {
			try {

				Date fromDate = format.parse(theForm.getFromDate());
				criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_FROM_DATE, fromDate);
			} catch (ParseException parseException) {
				if (logger.isDebugEnabled()) {
					logger.debug("ParseException in fromDate " + "populateReportCriteria() "
							+ "ParticipantBenefitBaseInformationAction:", parseException);
				}
			}
		}

		// Get the to Date
		if (!StringUtils.isEmpty(theForm.getToDate())) {
			try {

				Date toDate = format.parse(theForm.getToDate());
				criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_TO_DATE, toDate);
			} catch (ParseException parseException) {
				if (logger.isDebugEnabled()) {
					logger.debug("ParseException in toDate populateReportCriteria() "
							+ "ParticipantBenefitBaseInformationAction:", parseException);
				}
			}
		}

		// Get the Gifl Selection Date
		if (!StringUtils.isEmpty(theForm.getGiflSelectionDate())) {
			try {

				Date giflSelectionDate = format.parse(theForm.getGiflSelectionDate());
				criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_GIFL_SELECTION_DATE,
						giflSelectionDate);
			} catch (ParseException parseException) {
				if (logger.isDebugEnabled()) {
					logger.debug("ParseException in toDate populateReportCriteria() while parsing giflSelectionDate"
							+ "ParticipantBenefitBaseInformationAction:", parseException);
				}
			}
		}

		// Report will be shown to both internal and external user. If we want
		// to restrict that we have to set this
		// values conditionally based on user type.
		requiredReportData = new Boolean(true);

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_TRANSACTION_DETAILS_ON_USER,
				requiredReportData);
		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_AS_OF_DATE, theForm.getAsOfDate());

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_PROFILE_ID, theForm.getProfileId());

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_PRTID, theForm.getParticipantId());

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_CONTRACT_NUMBER,
				String.valueOf(getUserProfile(request).getCurrentContract().getContractNumber()));

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.FILTER_PROPOSAL_NUMBER,
				theForm.getProposalNumber());

		criteria.addFilter(ParticipantBenefitBaseInformationReportData.APPLICATION_ID, Constants.PS_APPLICATION_ID);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * This method is used for retrieving the participant details
	 * 
	 * @param contractNumber
	 *            int
	 * @param profileId
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @throws SystemException
	 */
	public void retrieveParticipantDetails(String profileId, HttpServletRequest request) throws SystemException {

		ParticipantBenefitBaseDetails participantBenefitBaseDetails = new ParticipantBenefitBaseDetails();

		UserProfile userProfile = getUserProfile(request);
		int contractNumber = userProfile.getCurrentContract().getContractNumber();

		int participantId = ParticipantServiceDelegate.getInstance()
				.getParticipantIdByProfileId(Long.parseLong(profileId), contractNumber);

		// Super User Role facked for retrieving the account balance for all
		// users.
		// Getting Employee related information like firstName,lastName, SSN,
		// D.O.B
		Employee employee = employeeServiceDelegate.getEmployeeByProfileId(Long.parseLong(profileId), contractNumber,
				null);

		String contractNo = new Integer(contractNumber).toString();

		EmployeeDetailVO employeeDetailVO = employee.getEmployeeDetailVO();

		if (employeeDetailVO == null) {
			throw new SystemException("Problem retrieving the participant details for profileId: " + profileId
					+ "; ContractId:" + contractNumber);
		}

		participantBenefitBaseDetails.setFirstName(employeeDetailVO.getFirstName());

		participantBenefitBaseDetails.setLastName(employeeDetailVO.getLastName());

		participantBenefitBaseDetails.setMiddleInitial(employeeDetailVO.getMiddleInitial());

		participantBenefitBaseDetails.setSsn(employeeDetailVO.getSocialSecurityNumber());

		participantBenefitBaseDetails.setDateOfBirth(employeeDetailVO.getBirthDate());

		// To get the market value for GO funds
		GiflMarketValue gmv = null;
		try {
			gmv = AccountServiceDelegate.getInstance().getParticipantGiflMarketValue(String.valueOf(participantId),
					String.valueOf(contractNumber));
		} catch (Exception exception) {
			throw new SystemException("Unable to retrieve market value for the profileId:" + profileId
					+ "; Contract Number:" + contractNo);
		}

		participantBenefitBaseDetails.setMarketValueGoFunds(gmv.getMarketValue());
		participantBenefitBaseDetails.setAsOfDate(gmv.getAsOfDate());

		request.setAttribute(Constants.BENEFIT_DETAILS, participantBenefitBaseDetails);

	}

	/**
	 * Check if the profileId was provided and if not retrieve using the
	 * participantId
	 * 
	 * @param participantBenefitBaseInformationForm
	 *            ParticipantBenefitBaseInformationForm
	 * @throws SystemException
	 */
	public void populateProfileId(ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm)
			throws SystemException {

		if (participantBenefitBaseInformationForm.getProfileId() == null
				|| participantBenefitBaseInformationForm.getProfileId().length() == 0) {

			if (participantBenefitBaseInformationForm.getParticipantId() != null
					&& participantBenefitBaseInformationForm.getParticipantId().length() > 0) {

				AccountServiceDelegateAdaptor asd = new AccountServiceDelegateAdaptor();

				participantBenefitBaseInformationForm.setProfileId(
						asd.getProfileIdByParticipantId(participantBenefitBaseInformationForm.getParticipantId()));
			}

			if (participantBenefitBaseInformationForm.getProfileId() == null
					|| participantBenefitBaseInformationForm.getProfileId().length() == 0) {
				Exception ex = new Exception("Failed to get the profileId");
				throw new SystemException(ex,
						"Failed to get profileId on form " + participantBenefitBaseInformationForm.toString());
			}
		}
	}

	/**
	 * To calculate the default from date
	 * 
	 * @param lastETLRunDate
	 *            Date
	 * @return String
	 */
	public String getDefaultFromDate(Date lastETLRunDate) {

		// Create a Calendar object for the last ETL run date
		Calendar lastETLCalendar = Calendar.getInstance();
		lastETLCalendar.setTime(lastETLRunDate);

		// Subtracting one month from the last ETL run date
		lastETLCalendar.add(Calendar.MONTH, -1);

		// Return the calculated default from date as a String
		return DateRender.formatByPattern(lastETLCalendar.getTime(), "", RenderConstants.MEDIUM_MDY_SLASHED);
	}

	/**
	 * This method is used to get the Trade Restriction message if any for the
	 * given participant
	 * 
	 * @param request
	 * @return TradeRestriction a tradeRestriction object with effective trade
	 *         restriction details
	 * @throws Exception
	 */
	private TradeRestriction getTradingRestriction(Long participantId, Integer proposalNumber) throws Exception {
		TradeRestriction tradeRestriction = employeeServiceDelegate.getGatewayTradeRestriction(participantId,
				proposalNumber, AccountServiceDelegate.getInstance().getNextNYSEClosureDate(null));
		return tradeRestriction;
	}

	/**
	 * to retrieve the Life Income Amount Details for the Participant
	 * 
	 * @param profileId
	 * @param request
	 * @return
	 */
	private void getParticipantLIADetails(String profileId, HttpServletRequest request) throws SystemException {
		LifeIncomeAmountDetailsVO liaDetails = ContractServiceDelegate.getInstance()
				.getLIADetailsByProfileId(profileId);
		request.setAttribute(Constants.LIA_DETAILS, liaDetails);

	}

	/**
	 * to populate the Life Income Amount Details for the Participant
	 * 
	 * @param profileId
	 * @param request
	 * @return
	 */
	private void populateParticipantLIADetailsToForm(
			ParticipantBenefitBaseInformationForm participantBenefitBaseInformationForm,
			LifeIncomeAmountDetailsVO liaDetails) {
		participantBenefitBaseInformationForm
				.setShowLIADetailsSection(LIADisplayHelper.isShowLIADetailsSection(liaDetails.getAnniversaryDate()));
		if (participantBenefitBaseInformationForm.isShowLIADetailsSection()) {
			participantBenefitBaseInformationForm.setLiaSelectionDate(liaDetails.getEffectiveDate());
			participantBenefitBaseInformationForm.setLiaAnnualAmount(liaDetails.getAnnualAmount());
			participantBenefitBaseInformationForm
					.setLiaFrequencyCode(LIADisplayHelper.getDisplayFrequencyCode(liaDetails.getFrequencyCode()));
			participantBenefitBaseInformationForm.setLiaIndividualOrSpousalOption(
					LIADisplayHelper.getDisplayIndividualOrSpousalOption(liaDetails.getSpousalOptionInd()));
			participantBenefitBaseInformationForm
					.setLiaPercentage(LIADisplayHelper.getFormatedPercentage(liaDetails.getShare()));
			participantBenefitBaseInformationForm.setLiaPeriodicAmt(liaDetails.getPeriodicAmt());
			participantBenefitBaseInformationForm.setLiaAnniversaryDate(liaDetails.getAnniversaryDate());
		}

	}

	/**
	 * to get the CMA text by cma key
	 * 
	 * @param cmaKey
	 * @return String
	 */
	private String getContentMessageByCmaKey(int cmaKey) throws SystemException {
		Content message = null;
		try {
			message = ContentCacheManager.getInstance().getContentById(cmaKey,
					ContentTypeManager.instance().MISCELLANEOUS);

		} catch (ContentException exception) {
			throw new SystemException(exception.getMessage());
		}

		return ContentUtility.getContentAttribute(message, "text");
	}
}