/**
 * 
 */
package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.service.report.profiles.reporthandler.BusinessTeamCodeLookup;
import com.manulife.pension.ps.service.report.profiles.reporthandler.TpafirmManagementReportHandler;
import com.manulife.pension.ps.service.report.profiles.valueobject.TpafirmManagementReportData;
import com.manulife.pension.ps.service.report.profiles.valueobject.UserManagementChangesExternalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SearchContractController;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.SecurityChangeHistory;
import com.manulife.pension.util.content.GenericException;

/**
 * @author marcest
 * 
 */
@Controller
@RequestMapping( value ="/profiles")
@SessionAttributes({"tpafirmManagementReportForm"})

public class TpafirmManagementReportController extends SecurityReportController {

	@ModelAttribute("tpafirmManagementReportForm") public TpafirmManagementReportForm populateForm() {return new TpafirmManagementReportForm();}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/profiles/tpafirmManagementReport.jsp");
		forwards.put("default","/profiles/tpafirmManagementReport.jsp");
		forwards.put("save","/profiles/tpafirmManagementReport.jsp");
		forwards.put("sort","/profiles/tpafirmManagementReport.jsp");
		forwards.put("page","/profiles/tpafirmManagementReport.jsp");
		forwards.put("print","/profiles/tpafirmManagementReport.jsp"); 
		forwards.put("filter","/profiles/tpafirmManagementReport.jsp");
	}


	public static final String REPORT_TITLE = "John Hancock USA - TPA firm report";

	protected static final String DOWNLOAD_COLUMN_HEADING = "Teamcode,JH Rep, Contract number, Contract Name, TPA firm ID, TPA firm name, Changed by, Changed by role, Date of change, Item, Old value, New value";


//TODO : do we need this really?
		  protected String doCommon(
				  TpafirmManagementReportForm reportForm, HttpServletRequest request,
		            HttpServletResponse response) throws
		            SystemException {
		validate(reportForm, request);

		String returnForward = super.doCommon( reportForm, request, response);

		ReportData report = (ReportData) request.getAttribute(Constants.REPORT_BEAN);

		return returnForward;
	}


	@RequestMapping(value ="/tpafirmManagementReport/" , method =  {RequestMethod.POST, RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		
		String forward=super.doDefault( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/tpafirmManagementReport/" ,params={"task=filter"}  , method =  {RequestMethod.POST}) 
	public String doFilter (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		String forward=super.doFilter( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/tpafirmManagementReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		String forward=super.doPage( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/tpafirmManagementReport/" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		String forward=super.doSort( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/tpafirmManagementReport/" ,params={"task=download"}  , method =  {RequestMethod.GET})	
	public String doDownload (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		String forward=super.doDownload( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value ="/tpafirmManagementReport/" ,params={"task=downloadAll"}  , method =  {RequestMethod.GET})
	public String doDownloadAll (@Valid @ModelAttribute("tpafirmManagementReportForm") TpafirmManagementReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		String formErrors = validate(form, request);
		if(formErrors!=null){
			return StringUtils.contains(formErrors,'/')?formErrors:forwards.get(formErrors);
		}
		String forward=super.doDownloadAll( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}  


	private String internalUsers = "'PCR','ICC','PIM','PLC','PRM','BIU','PSC','SAD','PTL','UAL','BGC','PTC'";

	private String externalUsers = "'ADC','AUS','ECU','INC','PPY','PSU','TPA','TUM','TRT'";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	@Override
	protected String getDefaultSort() {
		return TpafirmManagementReportData.SORT_TEAM_CODE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.pension.ps.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		TpafirmManagementReportForm form = (TpafirmManagementReportForm) reportForm;

		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		// As of and total count
		buffer.append(REPORT_TITLE).append(LINE_BREAK);

		// filters used
		// changed by
		if (!StringUtils.isEmpty(form.getChangedBy())) {
			buffer.append("Changed by (last name), ").append(form.getChangedBy())
			.append(LINE_BREAK);
		}
		// contract number
		if (!StringUtils.isEmpty(form.getContractNumber())) {
			buffer.append("Contract number, ").append(form.getContractNumber()).append(LINE_BREAK);
		}
		// from date
		if (!StringUtils.isEmpty(form.getFromDate())) {
			buffer.append("From date:, ").append(form.getFromDate()).append(LINE_BREAK);
		}
		// to date
		if (!StringUtils.isEmpty(form.getToDate())) {
			buffer.append("To date:, ").append(form.getToDate()).append(LINE_BREAK);
		}
		// team code
		if (!StringUtils.isEmpty(form.getTeamCode())) {
			buffer.append("Team code, ").append(form.getTeamCode()).append(LINE_BREAK);
		}
		// change by user type
		if (!StringUtils.isEmpty(form.getUserType())) {
			buffer.append("Change by user type, ").append(form.getUserType()).append(LINE_BREAK);
		}

		// heading and records
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			SecurityChangeHistory theItem = (SecurityChangeHistory) iterator.next();
			buffer.append(theItem.getTeamCode() == null ? Constants.EMPTY_STRING : theItem.getTeamCode()).append(COMMA);
			buffer.append(escapeField(theItem.getJhRepName())).append(COMMA);
			buffer.append(theItem.getContractNumber()).append(COMMA);
			buffer.append(escapeField(theItem.getContractName())).append(COMMA);
			buffer.append(theItem.getTpafirmId()).append(COMMA);
			buffer.append(escapeField(theItem.getTpafirmName())).append(COMMA);
			buffer.append(
					escapeField(theItem.getChangedByLastName() + ","
							+ theItem.getChangedByFirstName())).append(COMMA);
			buffer.append(theItem.getChangedByRoleCode() == null ? Constants.EMPTY_STRING : theItem.getChangedByRoleCode()).append(COMMA);
			buffer.append(SecurityReportController.dateFormatter(theItem.getCreatedTs())).append(COMMA);
			buffer.append(theItem.getItemName() == null ? Constants.EMPTY_STRING : theItem.getItemName()).append(COMMA);
			buffer.append(escapeField(theItem.getOldValue())).append(COMMA);
			buffer.append(escapeField(theItem.getNewValue())).append(COMMA);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	@Override
	protected String getReportId() {
		return TpafirmManagementReportHandler.REPORT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	@Override
	protected String getReportName() {
		return TpafirmManagementReportHandler.REPORT_NAME;
	}

	/**
	 * populate report action form
	 */
	protected void populateReportForm( BaseReportForm reportForm,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		super.populateReportForm( reportForm, request);
		TpafirmManagementReportForm form = (TpafirmManagementReportForm) reportForm;
		if (getTask(request).equals(DEFAULT_TASK)) {
			form.setFromDate(getPreviousDay());
			form.setToDate(getPreviousDay());
			form.setTeamCode("All");
			form.setContractNumber(null);
			form.setChangedBy(null);

		}
		form.setTeamCodeList(buildTeamCodeList());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {
		TpafirmManagementReportForm form = (TpafirmManagementReportForm) actionForm;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		if (StringUtils.isNotBlank(form.getAction())) {
			criteria.addFilter(TpafirmManagementReportData.FILTER_ACTION, form.getAction());
		}
		if (form.getTeamCode() != null && !"All".equals(form.getTeamCode())) {
			criteria.addFilter(TpafirmManagementReportData.FILTER_TEAM_CODE, form.getTeamCode());
		}
		if (StringUtils.isNotBlank(form.getContractNumber())) {
			Integer contractNum = new Integer(form.getContractNumber());
			criteria.addFilter(TpafirmManagementReportData.FILTER_CONTRACT_NUMBER, contractNum);
		}
		if (StringUtils.isNotBlank(form.getFromDate())) {
			Date startDate;
			try {
				startDate = dateFormat.parse(form.getFromDate());
			} catch (ParseException e) {
				throw new SystemException(e, "invalid date");
			}
			criteria
			.addFilter(UserManagementChangesExternalReportData.FILTER_START_DATE, startDate);
		}
		if (StringUtils.isNotBlank(form.getToDate())) {
			Date endDate;
			try {
				endDate = dateFormat.parse(form.getToDate());
			} catch (ParseException e) {
				throw new SystemException(e, "invalid date");
			}
			criteria.addFilter(UserManagementChangesExternalReportData.FILTER_END_DATE, endDate);
		}
		if (StringUtils.isNotBlank(form.getChangedBy())) {
			criteria.addFilter(TpafirmManagementReportData.FILTER_LAST_NAME, form.getChangedBy());
		}
		if (StringUtils.isNotBlank(form.getUserType())) {
			if (form.getUserType().equals("All")) {
				criteria.addFilter(TpafirmManagementReportData.FILTER_ROLES, null);
			} else if (form.getUserType().equals("Internal")) {
				criteria.addFilter(TpafirmManagementReportData.FILTER_ROLES, internalUsers);
			} else if (form.getUserType().equals("External")) {
				criteria.addFilter(TpafirmManagementReportData.FILTER_ROLES, externalUsers);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#populateSortCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.BaseReportForm)
	 */
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		if (StringUtils.isEmpty(form.getSortField()) || getDefaultSort().equals(form.getSortField())) {
			// tpr13
			criteria.insertSort(getDefaultSort(), form.getSortDirection());
			criteria.insertSort(TpafirmManagementReportData.SORT_CONTRACT_NUMBER, ReportSort.ASC_DIRECTION);
			criteria.insertSort(TpafirmManagementReportData.SORT_DATE, ReportSort.DESC_DIRECTION);
		} else {
			super.populateSortCriteria(criteria, form);
		}
	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected String validate( ActionForm actionForm,
			HttpServletRequest request) {

		Collection errors = doValidate( actionForm, request);
		/*
		 * Errors are stored in the session.
		 */
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			TpafirmManagementReportData report = new TpafirmManagementReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			return "input";
		}

		return null;
	}

	@Override
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		//This code has been changed and added  to Validate form and request 
		//against penetration attack, prior to other validations as part of the CL#137697.

		Collection errors = super.doValidate( form, request);
		TpafirmManagementReportForm sForm = (TpafirmManagementReportForm) form;
		String lastName = sForm.getChangedBy();

		String task = getTask(request);
		if (FILTER_TASK.equals(task) || SORT_TASK.equals(task) || DOWNLOAD_TASK.equals(task)) {

			if (!StringUtils.isEmpty(lastName) && !Pattern.matches("[a-zA-Z0-9' ]*", lastName)) {
				errors.add(new GenericException(7085));
			}
			if ( StringUtils.isNotEmpty(sForm.getContractNumber())) {
				// General contract number rule SCR 35
				boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance().validate(
						BusinessConversionForm.FIELD_CONTRACT_NUMBER, errors,
						sForm.getContractNumber());

				if (isValidFormat) {
					UserProfile profile = getUserProfile(request);
					UserRole role = profile.getRole();
					int diDuration=SearchContractController.DI_DURATION_24_MONTH;
					try {
						diDuration = EnvironmentServiceDelegate.getInstance()
								.retrieveContractDiDuration(role, 0,null);
					} catch (SystemException e1) {
						//TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//check to make sure contract exists.
					Contract c = null;
					try {
						c = ContractServiceDelegate.getInstance().getContractDetails(new Integer(sForm.getContractNumber()), diDuration);
						if ( c == null ) {
							errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
						}
					} catch (ContractNotExistException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					} catch (NumberFormatException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					} catch (SystemException e) {
						errors.add(new GenericException(ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				}
			}
			// check date of change
			validateDates(sForm.getFromDate(), sForm.getToDate(), errors);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
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
	// dummy data for team code list
	private List<LabelValueBean> buildTeamCodeList() {
		List<LabelValueBean> codes = new ArrayList<LabelValueBean>();
		codes.add(new LabelValueBean("All", "All"));
		codes.add(new LabelValueBean("No team code", ""));
		for (String code : getBusinessTeamCodes()) {
			codes.add(new LabelValueBean(code, code));
		}
		return codes;
	}

	private Collection<String> getBusinessTeamCodes() {
		Collection<String> codes = null;

		try {
			codes = BusinessTeamCodeLookup.getBusinessTeamCodes();
		} catch (SystemException se) {
			logger.error(se);
		}
		return codes;
	}

	/**
	 * Get previous day
	 * 
	 * @return
	 */
	private String getPreviousDay() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		cl.add(Calendar.DAY_OF_MONTH, -1);
		return SecurityReportController.dateFormatter(new Date(cl.getTimeInMillis()));
	}

	private String escapeField(String field) {
		if(StringUtils.isEmpty(field)) {
			return StringUtils.EMPTY;
		} else if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}

}
