package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData.FilterSections;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractValidationDetail;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.ArrayUtility;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.converter.ConverterHelper;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * 
 * Action class to handle 404a5 Notice Info History page
 * 
 * @author Siby Thomas
 *
 */
@Controller
@RequestMapping( value = "/view404a5NoticeInfoChangeHistory/")
@SessionAttributes({"noticeInfo404a5ChangeHistoryForm"})

public class NoticeInfo404a5ChangeHistoryReportController extends ReportController {
	@ModelAttribute("noticeInfo404a5ChangeHistoryForm")
	public NoticeInfo404a5ChangeHistoryReportForm populateForm()
	{
		return new NoticeInfo404a5ChangeHistoryReportForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static
	{
		forwards.put("input","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put("default","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put("sort","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put("filter","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put( "page","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put( "print","/fee/404a5NoticeChangeHistory.jsp");
		forwards.put( "back","redirect:/do/view404a5NoticeInfo/");
		}
	

	
	private static final String DOWNLOAD_COLUMN_HEADING_EXTERNAL_NOT_TPA = "Date & Time, User,Type, Value,Special Notes ";
	private static final String DOWNLOAD_COLUMN_HEADING_INTERNAL_AND_TPA = "Date & Time, User,Type, Value, Standard Schedule Applied, Special Notes ";
	private static final String CSV_HEADER_FROM_DATE = "Request from";
	private static final String CSV_HEADER_TO_DATE = "Request to ";
	private static final String CSV_HEADER_USER_ID = "User";
	private static final String CSV_HEADER_FEE_TYPE = "Type";
	private static final String STANDARD_SCHEDULE_APPLIED = "Standard Schedule Applied";
	private static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	private static final String FORMAT_DATE_EXTRA_LONG_MDY = "MM/dd/yyyy hh:mm:ss a";
	/**
	 * SimpleDateFormat is converted to FastDateFormat to make it thread safe
	 */
	private static final FastDateFormat formattedChangedDate = FastDateFormat.getInstance(FORMAT_DATE_EXTRA_LONG_MDY);  
	
	private static final String REPORT_NAME = "NoticeInfoChangeHistory";
	

	
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		
		UserProfile userProfile = getUserProfile(request);
		Access404a5 contractAccess = userProfile.getAccess404a5();

		if (contractAccess.getAccess(Facility._404a5_NOTICE_INFO) == null) {
			return forwards.get(Constants.HOME_URL);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> preExecute");
		}
		return super.preExecute( form, request, response);
	}
	 
	public String doCommon( BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
	
		String forward = super.doCommon( form, request,
				response);
		
		// to enable the sort image for CREATED_TS column while default sorting.
		if(ContractFeeScheduleChangeHistoryReportData.SORT_DEFAULT.equals(form.getSortField())){
			form.setSortField(ContractFeeScheduleChangeHistoryReportData.SORT_CHANGE_DATE);
		}

		return forward;
	}
	
    /**
     * reset form  to default values
     * 
     * @param mapping
     * @param reportForm
     * @param request
     * @throws SystemException
     * 
     */
	@Override
	protected BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		
		reportForm = super.resetForm( reportForm, request);
		
		NoticeInfo404a5ChangeHistoryReportForm form = (NoticeInfo404a5ChangeHistoryReportForm) reportForm;
		
		form.setSelectedFeeType(StringUtils.EMPTY);
		form.setStandardScheduleApplied(StringUtils.EMPTY);
		form.setSelectedUserId(StringUtils.EMPTY);
		form.setToDate(DateRender.formatByPattern(getDefaultToDate(), "", RenderConstants.MEDIUM_MDY_SLASHED));
		form.setFromDate(DateRender.formatByPattern(getDefaultFromDate(), "", RenderConstants.MEDIUM_MDY_SLASHED));
		
        FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		UserProfile userProfile = getUserProfile(request);
	    Contract currentContract = userProfile.getCurrentContract();
	    int contractId = currentContract.getContractNumber();
	    
	    TPAFirmInfo tpaFirmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
	    form.setTpaId(tpaFirmInfo != null ? tpaFirmInfo.getId() : 0);
		
	    // if TPA user, get only history for that TPA
	    FeeScheduleHistoryDetails historyDetails = null;
	    if(getUserProfile(request).getRole().isTPA()) {
	    	historyDetails = feeServiceDelegate.getFeeScheduleChangeHistoryDetails(contractId,
		    		form.getTpaId(), FeeScheduleType.Notice404a5Info);
		    form.setHistoryDetails(historyDetails);
	    } else {
	    	historyDetails = feeServiceDelegate.getFeeScheduleChangeHistoryDetails(contractId,
		    		0, FeeScheduleType.Notice404a5Info);
		    form.setHistoryDetails(historyDetails);
	    }
	    
	    
	    Map<String, String> allUsers = new HashMap<String, String>();
	    
		if (userProfile.getRole().isExternalUser()) {
			for (Entry<Integer, String> user : historyDetails.getUpdatedUserDetails().entrySet()) {
				if (Integer.parseInt(Constants.SYSTEM_USER_PROFILE_ID) == user.getKey()) {
					allUsers.put(String.valueOf(user.getKey()), Constants.ADMINISTRATION);
				} else {
					UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserProfileByProfileId(new Long(user.getKey()));
					if (userInfo.getRole().isInternalUser()) {
						allUsers.put(String.valueOf(user.getKey()), Constants.JOHN_HANCOCK_REPRESENTATIVE);
					} else {
						 allUsers.put(String.valueOf(user.getKey()), user.getValue());
					}
				}
			}
		} else {
			for (Entry<Integer, String> user : historyDetails.getUpdatedUserDetails().entrySet()) {
				if (Integer.parseInt(Constants.SYSTEM_USER_PROFILE_ID) == user
						.getKey()) {
					allUsers.put(String.valueOf(user.getKey()),
							Constants.ADMINISTRATION);
				} else {
					allUsers.put(String.valueOf(user.getKey()), user.getValue());
				}
			}
	    	/* for(Entry<Integer, String>  user : historyDetails.getUpdatedUserDetails().entrySet()) {
	    		 allUsers.put(String.valueOf(user.getKey()), user.getValue());
	    		 displayNames.put(String.valueOf(user.getKey()), user.getValue());
	 	    }*/
	    }
		
	    form.setAllUsers(allUsers);
	    
	    
	    // set fee types
	    Map<FeeCategoryCode, LinkedHashSet<DeCodeVO>> feeTypes = historyDetails.getUpdatedFeeTypes();

		form.setTpaStandardFees(feeTypes.get(FeeCategoryCode.TPA_PRE_DEFINED));
		form.setTpaCustomFees(feeTypes.get(FeeCategoryCode.TPA_NON_STANDARD));
		form.setNonTpaFees(feeTypes.get(FeeCategoryCode.NON_TPA));
		form.setPbaStandardFees(feeTypes.get(FeeCategoryCode.PBA_STANDARD));
		form.setPbaCustomFees(feeTypes.get(FeeCategoryCode.PBA_CUSTOM));
		
		LinkedList<DeCodeVO> customFees = new LinkedList<DeCodeVO>();
		customFees.addAll(form.getTpaCustomFees());
		customFees.addAll(form.getNonTpaFees());
		Collections.sort(customFees, new Comparator<DeCodeVO>() {
			@Override
			public int compare(DeCodeVO object1, DeCodeVO object2) {
				return object1.getDescription().compareToIgnoreCase(object2.getDescription());
			}
		});
		form.setCustomFees(customFees);
		
		form.setPlanProvisonHistory(ContractServiceDelegate.getInstance().getContractValidationDetailHistory(
				contractId, ArrayUtility.toUnsortedSet(ContractValidationDetail.values())));
		
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
	   // binder.addValidators(noticeInfo404a5ChangeHistoryReportValidator);
	}

	@Override
	protected String getDefaultSort() {
		return ContractFeeScheduleChangeHistoryReportData.SORT_DEFAULT;
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

		ContractFeeScheduleChangeHistoryReportData data = (ContractFeeScheduleChangeHistoryReportData) report;
		
		StringBuffer buffer = new StringBuffer();

		NoticeInfo404a5ChangeHistoryReportForm theForm = (NoticeInfo404a5ChangeHistoryReportForm) reportForm;

		Date fromDate = new Date();
		Date toDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
		
		UserProfile loggedInUser = getUserProfile(request);
		 

		// Get dates for display
		buffer.append("404a-5 Notice Information Tool Change History as of,")
				.append(QUOTE)
				.append(DateRender.formatByPattern(new Date(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(QUOTE)
				.append(LINE_BREAK).append(LINE_BREAK);

		try {
			fromDate = format.parse(theForm.getFromDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in fromDate getDownloadData()"
						+ " TpaStandardFeeScheduleChangeHistoryReportAction:",
						parseException);
				throw new SystemException("Invalid from Date");
			}
		}
		try {
			toDate = format.parse(theForm.getToDate());
		} catch (ParseException parseException) {
			if (logger.isDebugEnabled()) {
				logger.debug("ParseException in ToDate getDownloadData()"
						+ " TpaStandardFeeScheduleChangeHistoryReportAction:",
						parseException);
				throw new SystemException("Invalid To Date");
			}
		}

		String fromDateStr = DateRender.format(fromDate, RenderConstants.MEDIUM_MDY_SLASHED);
		String toDateStr = DateRender.format(toDate, RenderConstants.MEDIUM_MDY_SLASHED);
		
		String userName = StringUtils.EMPTY;
		if(StringUtils.isNotEmpty(theForm.getSelectedUserId())) {
			if ((Constants.JOHN_HANCOCK_REPRESENTATIVE).equals(theForm
					.getSelectedUserId())) {
				userName =StringUtils.trimToEmpty(theForm.getSelectedUserId());
			} else {
				userName = theForm.getAllUsers().get(
						StringUtils.trimToEmpty(theForm.getSelectedUserId()));
			}
		}
		
		String feeType = StringUtils.trimToEmpty(theForm.getSelectedFeeType());


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
		if(StringUtils.isNotEmpty(feeType)) {
			buffer.append(theForm.getFeeDescription(feeType));
		}
		buffer.append(LINE_BREAK);
		if(loggedInUser.getRole().isInternalUser() 
				|| loggedInUser.getRole().isTPA()) {
		   buffer.append(STANDARD_SCHEDULE_APPLIED).append(COMMA);
		   buffer.append(StringUtils.trimToEmpty(theForm.getStandardScheduleApplied()));
		   buffer.append(LINE_BREAK);
		}
		buffer.append(LINE_BREAK);
		if(loggedInUser.getRole().isInternalUser() 
				|| loggedInUser.getRole().isTPA()) {
			buffer.append(DOWNLOAD_COLUMN_HEADING_INTERNAL_AND_TPA);
		} else {
			buffer.append(DOWNLOAD_COLUMN_HEADING_EXTERNAL_NOT_TPA);
		}
		buffer.append(LINE_BREAK);

		@SuppressWarnings("unchecked")
		Iterator<FeeScheduleChangeItem> iterator = data.getDetails().iterator();

		// Code to display the result table with the details of Change History
		while (iterator.hasNext()) {

			FeeScheduleChangeItem theItem = (FeeScheduleChangeItem) iterator.next();

			buffer.append(formattedChangedDate.format(theItem.getChangedDate()));
			buffer.append(COMMA);

			buffer.append(escapeField(theItem.getUserName()));
			buffer.append(COMMA);

			buffer.append(escapeField(theItem.getChangedType()));
			buffer.append(COMMA);

			buffer.append(escapeField(theItem.getFormttedValue()));
			buffer.append(COMMA);

			if(loggedInUser.getRole().isInternalUser() 
					|| loggedInUser.getRole().isTPA()) {
				buffer.append(theItem.getStandardScheduleAppliedValue());
				buffer.append(COMMA);
			}
			
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
		if(StringUtils.isEmpty(field)) {
			return StringUtils.EMPTY;
		}
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
		return ContractFeeScheduleChangeHistoryReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm reportform, HttpServletRequest request)
			throws SystemException {
		
		NoticeInfo404a5ChangeHistoryReportForm form = (NoticeInfo404a5ChangeHistoryReportForm) reportform;
		
		// set sections to filter with
		List<FilterSections> sections = new ArrayList<FilterSections>();	
		sections.add(FilterSections.DimSection);
		sections.add(FilterSections.FeeSection);
		sections.add(FilterSections.PbaDetailsSection);
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_SECTION, sections);
		
		// set fee types to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_GET_NON_TPA_FEE, true);
		
		// if fee type selected, don't search DIM section, add the filtered fee type
		if (!StringUtils.isEmpty(form.getSelectedFeeType())) {
			sections.remove(FilterSections.DimSection);
			sections.remove(FilterSections.PbaDetailsSection);
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE, form.getSelectedFeeType());
		}
		
		int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber();
		
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_HISTORY, form.getHistoryDetails().getFirmDetails());
		
		// set contract id to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_CONTRACT_ID, contractNumber);

		// set to date to filter with
		if (!StringUtils.isEmpty(form.getToDate())) {
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TO_DATE, getDate(form.getToDate()));
		}

		// set from date to filter with
		if (!StringUtils.isEmpty(form.getFromDate())) {
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE, getDate(form.getFromDate()));
		}

		// set std schedule ind
	   if (!StringUtils.isEmpty(form.getStandardScheduleApplied())) {
		    sections.remove(FilterSections.DimSection);
		    sections.remove(FilterSections.PbaDetailsSection);
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_STD_SCHEDULE_APPLIED_IND, form.getStandardScheduleApplied().trim());
		}
	   
	   criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_PLAN_PROVISION_HISTORY,
				form.getPlanProvisonHistory());
		
		// set users to filter with
		if (StringUtils.isEmpty(form.getSelectedUserId())) {
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME, form.getAllUsers());
		} else {
			String userId = form.getSelectedUserId();
			final Map<String, String> searchUsers = new HashMap<String, String>();
			if(userId.equals(Constants.ADMINISTRATION)) {
				for(Entry<String, String> entry : form.getAllUsers().entrySet()) {
					if(entry.getValue().equals(Constants.ADMINISTRATION)) {
						searchUsers.put(entry.getKey(), entry.getValue());
					}
				}
			} else if (userId.equals(Constants.JOHN_HANCOCK_REPRESENTATIVE)){
				for(Entry<String, String> entry : form.getAllUsers().entrySet()) {
					if(entry.getValue().equals(Constants.JOHN_HANCOCK_REPRESENTATIVE)) {
						searchUsers.put(entry.getKey(), entry.getValue());
					}
				}
			} else {
				String userName = form.getAllUsers().get(userId);
				searchUsers.put(userId, userName);
			}
			criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME, searchUsers);
			
		}
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
/*	TODO need to handle it for handler method
	@RequestMapping(value ="/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String execute(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryReportForm") NoticeInfo404a5ChangeHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		NoticeInfo404a5ChangeHistoryReportForm form = (NoticeInfo404a5ChangeHistoryReportForm) reportForm;
		if (StringUtils.isBlank(form.getSelectedFeeType())) {
			form.setSelectedFeeType(StringUtils.EMPTY);
		}
	}


	private Date getDefaultToDate() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		return cal.getTime();

	}

	private Date getDefaultFromDate() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}
	
	
	@RequestMapping(  method =  {RequestMethod.GET , RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
	
		 forward =  super.doDefault( form, request, response);
		
		ContractFeeScheduleChangeHistoryReportData report = (ContractFeeScheduleChangeHistoryReportData)
				request.getAttribute(CommonConstants.REPORT_BEAN);
		
		
		Map<String, String> displayNames = new HashMap<String, String>();
		for(FeeScheduleChangeItem item : (ArrayList<FeeScheduleChangeItem>) report.getDetails()) {
			if(StringUtils.equals(Constants.JOHN_HANCOCK_REPRESENTATIVE, item.getUserName())) {
				displayNames.put(Constants.JOHN_HANCOCK_REPRESENTATIVE, Constants.JOHN_HANCOCK_REPRESENTATIVE);
			} else {
				displayNames.put(item.getUserId(), item.getUserName());
			}
		}
		form.setDisplayNames(displayNames);
		
		return forwards.get(forward);
	}
	@RequestMapping(params = { "task=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

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
		
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
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
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
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
		
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
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
		
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(params = { "task=downloadAll" }, method = { RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
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
		
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(params = { "task=back" }, method = { RequestMethod.POST })
	public String doBack(@Valid @ModelAttribute("noticeInfo404a5ChangeHistoryForm") NoticeInfo404a5ChangeHistoryReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

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
		
		String errors = validate(form,request);
		
		if(errors!=null)
			return forwards.get("input");
		
		
		
		return forwards.get("back");
	}
	
	protected Collection<GenericException> doValidate (
			final ActionForm form,
			final HttpServletRequest request) {
		
				
		final Collection<GenericException> errors = new ArrayList<GenericException>();

		NoticeInfo404a5ChangeHistoryReportForm theForm = (NoticeInfo404a5ChangeHistoryReportForm) form;

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
				errors.add(new GenericException(ErrorCodes.INVALID_FROM_DATE_FORMAT));
			}
		} else {
			errors.add(new GenericException(ErrorCodes.FROM_DATE_EMPTY));
		}

		if (StringUtils.isNotBlank(toDateStr)) {
			try {
				toDate = dateFormat.parse(toDateStr);
			} catch (ParseException e) {
				errors.add(new GenericException(ErrorCodes.INVALID_TO_DATE_FORMAT));
			}
		} else {
			errors.add(new GenericException(ErrorCodes.TO_DATE_EMPTY));
		}

		if (fromDate != null && toDate != null) {
			if (fromDate.after(toDate)) {
				errors.add(new GenericException(ErrorCodes.FROM_DATE_GREATER_THAN_TO_DATE));
			}
		}
		return errors;
	}
}
