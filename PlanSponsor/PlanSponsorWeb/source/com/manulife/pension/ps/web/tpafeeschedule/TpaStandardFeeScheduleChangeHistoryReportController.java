package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TpaStandardFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.converter.ConverterHelper;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;


@Controller
@RequestMapping( value ="/changeHistoryTpaStandardFeeSchedule/")
@SessionAttributes({"tpaStandardFeeScheduleChangeHistoryReportForm"})

public class TpaStandardFeeScheduleChangeHistoryReportController extends
		ReportController {
	@ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") 
	public TpaStandardFeeScheduleChangeHistoryReportForm populateForm() 
	{
		return new TpaStandardFeeScheduleChangeHistoryReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp");
		forwards.put("default","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp");
		forwards.put("sort","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp");
		forwards.put("filter","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp");
		forwards.put("page","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp");
		forwards.put("print","/tpafeeschedule/tpaStandardFeeScheduleChangeHistory.jsp" );
		forwards.put("viewStandardSchedule" ,"redirect:/do/viewTpaStandardFeeSchedule/");
	}

	
	private static final String DOWNLOAD_COLUMN_HEADING = "Date & Time, User,Type, Value,Special Notes ";

	private static final String CSV_HEADER_FROM_DATE = "Request from";

	private static final String CSV_HEADER_TO_DATE = "Request to ";

	private static final String CSV_HEADER_USER_ID = "User";

	private static final String CSV_HEADER_FEE_TYPE = "Type";

	public static final String FORMAT_DATE_EXTRA_LONG_MDY = "MM/dd/yyyy hh:mm:ss a";
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat formattedChangedDate = FastDateFormat.getInstance(FORMAT_DATE_EXTRA_LONG_MDY);
	
	public static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION = "viewStandardSchedule";

	protected String preExecute ( TpaStandardFeeScheduleChangeHistoryReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		String forward = super.preExecute( form, request,
				response);
		if (forward == null) {

			TpaStandardFeeScheduleChangeHistoryReportForm changeHistoryForm = (TpaStandardFeeScheduleChangeHistoryReportForm) form;

			if (request.getSession().getAttribute(
					Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID) != null) {
				String selectedTpaFiemId = (String) request.getSession()
						.getAttribute(
								Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID);
				changeHistoryForm.setTpaFirmId(selectedTpaFiemId);
				request.getSession().removeAttribute(
						Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID);
			}

			if (changeHistoryForm.getTpaFirmId() == null) {
				return Constants.HOMEPAGE_FINDER_FORWARD;
			}

			UserProfile userProfile = getUserProfile(request);
			UserInfo userInfo = SecurityServiceDelegate.getInstance()
					.getUserInfo(userProfile.getPrincipal());
			// If TPA is not having TPA User Manager permissions redirect to
			// Select TPA firm page
			if (userProfile.getRole().isTPA() && !isTpaUserManager(userInfo, changeHistoryForm)) {
				return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION);
			}
			
		}
		return forward;
	}
	
	@Override
	protected String doCommon(BaseReportForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		String forward = super.doCommon( form, request,
				response);

		TpaStandardFeeScheduleChangeHistoryReportData reportData = (TpaStandardFeeScheduleChangeHistoryReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		TpaStandardFeeScheduleChangeHistoryReportForm reportForm = (TpaStandardFeeScheduleChangeHistoryReportForm)form;
		populateReportFormFromReportData(reportData, reportForm);
		if(StringUtils.isBlank(forward)) {
			forward = "input";
		}
		// to enable the sort image for CREATED_TS column while default sorting.
		if(TpaStandardFeeScheduleChangeHistoryReportData.SORT_DEFAULT.equals(form.getSortField())){
			form.setSortField(TpaStandardFeeScheduleChangeHistoryReportData.SORT_CREATED_TS);
		}

		return forward;
	}

	protected BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {

		TpaStandardFeeScheduleChangeHistoryReportForm form = (TpaStandardFeeScheduleChangeHistoryReportForm) reportForm;
		form.setFeeType(StringUtils.EMPTY);
		form.setUserName(StringUtils.EMPTY);
		form.setToDate(DateRender.formatByPattern(getDefaultToDate(), "",
				RenderConstants.MEDIUM_MDY_SLASHED));
		form.setFromDate(DateRender.formatByPattern(getDefaultFromDate(), "",
				RenderConstants.MEDIUM_MDY_SLASHED));

		return form;
	}

	/**
	 * This is the method to be extended for validation.
	 * 
	 * @return Error Collection
	 */
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder 
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Collection<GenericException> doValidate(
			 final ActionForm form,
			final HttpServletRequest request) {
		TpaStandardFeeScheduleChangeHistoryReportForm theForm = (TpaStandardFeeScheduleChangeHistoryReportForm) form;
		/*
		 * This code has been changed and added to Validate form and
		 * request against penetration attack, prior to other validations.
		 */
		
		
		final Collection<GenericException> errors = new ArrayList<GenericException>(
				0);

		// do not validate since the filters are going to be reset
		if (getTask(request).equalsIgnoreCase(DEFAULT_TASK)) {
			return errors;
		}

		Date fromDate = null;
		Date toDate = null;
		final DateFormat dateFormat = ConverterHelper.getDefaultDateFormat();
		dateFormat.setLenient(false);
		final String fromDateStr = StringUtils.trim(theForm.getFromDate());
		final String toDateStr = StringUtils.trim(theForm.getToDate());

		theForm.setFromDate(fromDateStr);
		theForm.setToDate(toDateStr);

		if (StringUtils.isNotBlank(fromDateStr)) {
			try {
				fromDate = dateFormat.parse(fromDateStr);
			} catch (ParseException parseException) {
				errors.add(new GenericException(ErrorCodes.CHANGE_HISTORY_TPA_STANDARD_SCHEDULE_PAGE_FROM_DATE_INVALID_FORMAT));
			}
		} else {
			errors.add(new ValidationError("fromDate",
					ErrorCodes.FROM_DATE_EMPTY));
		}

		if (StringUtils.isNotBlank(toDateStr)) {
			try {
				toDate = dateFormat.parse(toDateStr);
			} catch (ParseException e) {
				errors.add(new GenericException(ErrorCodes.CHANGE_HISTORY_TPA_STANDARD_SCHEDULE_PAGE_TO_DATE_INVALID_FORMAT));
			}
		} else {
			errors.add(new GenericException(ErrorCodes.TO_DATE_EMPTY));
		}

		if (fromDate != null && toDate != null) {
			if (fromDate.after(toDate)) {
				errors.add(new GenericException(ErrorCodes.CHANGE_HISTORY_STANDARD_SCHEDULE_PAGE_FROM_DATE_GREATER_THAN_TO_DATE));
			}
		}

		return errors;
	}

	@Override
	protected String getDefaultSort() {
		return TpaStandardFeeScheduleChangeHistoryReportData.SORT_DEFAULT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	/**
	 * This method is used for getting the data for download csv file
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return byte[]
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		TpaStandardFeeScheduleChangeHistoryReportData data = (TpaStandardFeeScheduleChangeHistoryReportData) report;
		Map<String, String> standardFeeTypes = data.getStandardFeeTypes();
		Map<String, String> nonStandardFeeTypes = data.getNonStandardFeeTypes();
		String feeDescription = StringUtils.EMPTY;

		StringBuffer buffer = new StringBuffer();

		TpaStandardFeeScheduleChangeHistoryReportForm theForm = (TpaStandardFeeScheduleChangeHistoryReportForm) reportForm;

		Date fromDate = new Date();
		Date toDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);

		// Get the title for display
		buffer.append("Standard Schedule Change History as of ,").append(QUOTE)
				.append(
						DateRender.formatByPattern(new Date(), "",
								RenderConstants.MEDIUM_MDY_SLASHED)).append(QUOTE)
				.append(LINE_BREAK).append(LINE_BREAK);

		try {
			fromDate = format.parse(theForm.getFromDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in fromDate getDownloadData()"
						+ " TpaStandardFeeScheduleChangeHistoryReportAction:",
						parseException);
			}
			throw new SystemException("Invalid from Date");
		}
		
		try {
			toDate = format.parse(theForm.getToDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in ToDate getDownloadData()"
						+ " TpaStandardFeeScheduleChangeHistoryReportAction:",
						parseException);
			}
			throw new SystemException("Invalid To Date");
		}

		String fromDateStr = DateRender.format(fromDate,
				RenderConstants.MEDIUM_MDY_SLASHED);

		String toDateStr = DateRender.format(toDate,
				RenderConstants.MEDIUM_MDY_SLASHED);
		
		String userName = StringUtils.trimToEmpty(data.getUserNames().get(theForm.getUserName()));
		String feeType = StringUtils.trimToEmpty(theForm.getFeeType());

		if (StringUtils.isNotBlank(feeType)) {
			if (standardFeeTypes.containsKey(feeType)) {
				feeDescription = standardFeeTypes.get(feeType);
			} else if (nonStandardFeeTypes.containsKey(feeType)) {
				feeDescription = nonStandardFeeTypes.get(feeType);
			}

		}

		// Get the field labels and set values from form to display
		
		buffer.append(CSV_HEADER_FROM_DATE).append(COMMA);
		buffer.append(fromDateStr).append(COMMA);
		buffer.append(LINE_BREAK);
		buffer.append(CSV_HEADER_TO_DATE).append(COMMA);
		buffer.append(toDateStr).append(COMMA);
		buffer.append(LINE_BREAK);
		buffer.append(CSV_HEADER_USER_ID).append(COMMA);
		buffer.append(userName);
		buffer.append(LINE_BREAK);
		buffer.append(CSV_HEADER_FEE_TYPE).append(COMMA);
		buffer.append(feeDescription).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator<FeeScheduleChangeItem> iterator = data.getDetails().iterator();

		// Code to display the result table with the details of Change History
		while (iterator.hasNext()) {

			FeeScheduleChangeItem theItem = (FeeScheduleChangeItem) iterator.next();

			buffer.append(escapeField(formattedChangedDate.format(theItem.getChangedDate())));
			buffer.append(COMMA);
			
			buffer.append(escapeField(theItem.getUserName()));
			buffer.append(COMMA);

			buffer.append(escapeField(theItem.getChangedType()));
			buffer.append(COMMA);

			buffer.append(escapeField(theItem.getFormttedValue()));
			buffer.append(COMMA);

			buffer.append(theItem.getSpecialNotes());
			buffer.append(COMMA);
			buffer.append(LINE_BREAK);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return buffer.toString().getBytes();

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

	@Override
	protected String getReportId() {
		return TpaStandardFeeScheduleChangeHistoryReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return TpaStandardFeeScheduleChangeHistoryReportData.REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm reportform, HttpServletRequest request)
			throws SystemException {
		TpaStandardFeeScheduleChangeHistoryReportForm form = (TpaStandardFeeScheduleChangeHistoryReportForm) reportform;

		criteria
				.addFilter(
						TpaStandardFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_ID,
						Integer.parseInt(form.getTpaFirmId()));

		if (!StringUtils.isEmpty(form.getToDate())) {

			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_TO_DATE,
							getDate(form.getToDate()));
		} else {
			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_TO_DATE,
							getDefaultToDate());
			form.setToDate(DateRender.formatByPattern(getDefaultToDate(), "",
					RenderConstants.MEDIUM_MDY_SLASHED));
		}

		if (!StringUtils.isEmpty(form.getFromDate())) {

			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE,
							getDate(form.getFromDate()));
		} else {
			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE,
							getDefaultFromDate());
			form.setFromDate(DateRender.formatByPattern(getDefaultFromDate(),
					"", RenderConstants.MEDIUM_MDY_SLASHED));
		}

		if (!StringUtils.isEmpty(form.getUserName())) {
			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_USER_NAME,
							form.getUserName());
		}

		if (!(StringUtils.isEmpty(form.getFeeType())
				|| StringUtils.equals("-1", form.getFeeType()))) {
			criteria
					.addFilter(
							TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE,
							form.getFeeType());
		}

	}

	private void populateReportFormFromReportData(
			TpaStandardFeeScheduleChangeHistoryReportData reportData,
			TpaStandardFeeScheduleChangeHistoryReportForm form) {

		form.setAsOfDate(new Date().toString());
		if (StringUtils.equals("-1", form.getFeeType())) {
			form.setFeeType(StringUtils.EMPTY);
		}

		form.setUserNames(reportData.getUserNames());

		Map<String, String> feeTypes = new LinkedHashMap<String, String>();
		feeTypes.putAll(reportData.getStandardFeeTypes());
		if (!reportData.getNonStandardFeeTypes().isEmpty()) {
			feeTypes.put("-1",
					"-----------------------------------------------------------");
			feeTypes.putAll(reportData.getNonStandardFeeTypes());
		}

		form.setFeeTypes(feeTypes);

	}

	private Date getDate(String dateString) throws SystemException {
		final DateFormat dateFormat = ConverterHelper.getDefaultDateFormat();
		Date date = null;

		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			throw new SystemException("Invalid input Date");
		}

		return date;
	}

	/**
	 * @see PsAction#execute(ActionMapping, Form, HttpServletRequest,
	 *      HttpServletResponse)
	 */
/*	@RequestMapping( method =  {RequestMethod.POST,RequestMethod.GET}) 
	protected String execute (@Valid @ModelAttribute("TpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
	
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// do a refresh so that there's no problem using tha back button
			String forward = new UrlPathHelper().getPathWithinApplication(request);
			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward;
		}

		return super.execute( form, request, response);
	}
*/
	/**
	 * Method to populate Report Action form
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	private Date getDefaultToDate() {
		Calendar cal = new GregorianCalendar();
		return cal.getTime();

	}

	private Date getDefaultFromDate() {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}
	
	 /**
	 * Method to check if the User has Tpa User Manager permissions
	 * @param userInfo
     * @param tpaStandardFeeScheduleForm
     * @return isTpaUserManager
	 * @throws SystemException
	 */ 
 	
	boolean isTpaUserManager(UserInfo userInfo,
			TpaStandardFeeScheduleChangeHistoryReportForm tpaStandardFeeScheduleChangeHistoryReportForm)
			throws SystemException {
		boolean isTpaUserManager = false;

		for (TPAFirmInfo tpaFirm : userInfo.getTpaFirmsAsCollection()) {
			if (StringUtils.equals(tpaStandardFeeScheduleChangeHistoryReportForm.getTpaFirmId(), String.valueOf(tpaFirm
					.getId()))) {
				Collection<PermissionType> permissions = tpaFirm
						.getContractPermission().getRole().getPermissions();
				for (PermissionType perm : permissions) {
					if (StringUtils
							.equals(
									PermissionType.getPermissionCode(perm),
									PermissionType
											.getPermissionCode(PermissionType.MANAGE_TPA_USERS))) {
						isTpaUserManager = true;
						break;
					}
				}

				break;
			}
		}
		return isTpaUserManager;
	}

	@RequestMapping(params = { "task=filter" }, method = { RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection<GenericException> errors = 	doValidate(form,request);
		if(!errors.isEmpty()) {
			return forwards.get("input");
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("TpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping( method = { RequestMethod.GET , RequestMethod.POST })
	public String doDefault(@Valid @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = {  "task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=download" }, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws  SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=print" }, method = { RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws  SystemException, IOException, ServletException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(params = { "task=back" }, method = { RequestMethod.POST})
	public String doBack( @ModelAttribute("tpaStandardFeeScheduleChangeHistoryReportForm") TpaStandardFeeScheduleChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws  SystemException, IOException, ServletException {
		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION);
	}
}
