package com.manulife.pension.ps.web.noticereports;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

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
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.ContractNumberNoMandatoryRule;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.ParticipantWebsiteReportHandler;
import com.manulife.pension.ps.service.report.noticereports.valueobject.ParticipantWebsiteReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.BusinessConversionForm;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping(value ="/noticereports")
@SessionAttributes({"alertsReportForm"})
public class ParticipantWebsiteReportController extends ReportController {
	@ModelAttribute("alertsReportForm") 
	public AlertsReportForm populateForm()
	{
		return new AlertsReportForm();
		
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/noticereports/participantWebsiteReport.jsp"); 
		forwards.put("default","/noticereports/participantWebsiteReport.jsp");
		forwards.put("search","/noticereports/participantWebsiteReport.jsp");
		forwards.put("participantWebsiteReportPage","/noticereports/participantWebsiteReport.jsp");
		forwards.put("print","/noticereports/participantWebsiteReport.jsp");}

	
	private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	public static final String REPORT_NAME = "reportName";

	/**
	 * CSV file title
	 */
	public static final String REPORT_TITLE = "Participant Website as of ";

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private FastDateFormat simpleDateFormat = FastDateFormat.getInstance(
				"MM/dd/yyyy", Locale.US);

	
	/** Method to get the report data after validation. 
	 * @param mapping
	 * @param request
	 * @param reportForm
	 * @param response
	 * @return ActionForward
	 */
	 
    public String doCommon( BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	doValidate(form, request);
		
		Collection<GenericException> errors = super.doValidate(
				form, request);
		super.doCommon( form, request, response);

		ParticipantWebsiteReportData reportData = (ParticipantWebsiteReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);

		return forwards.get(CommonConstants.PARTICIPANT_WEBSITE_REPORT_PAGE);
	}
	

	/** Method to validate the form fields 
	 * @param request
	 * @param mapping
	 * @param form
	 * @return errors
	 */

	@SuppressWarnings("unchecked")
	public Collection doValidate(
			HttpServletRequest request, AlertsReportForm form) {

		Collection errors = super.doValidate( form, request);
		
		// from date validation
        Date fromDate = null;
        Date toDate = null;
        try {
            if (StringUtils.isNotBlank(form.getFromDate())) {
                fromDate = simpleDateFormat.parse(form.getFromDate());
                if (!simpleDateFormat.format(fromDate).equals(form.getFromDate())) {
                    throw new ParseException(CommonConstants.INVALID_DATE, 0);
                }
            } else {
                errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
            }
        } catch (ParseException pe) {
            errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_INVALID));
        }

        // to date validation
        try {
            if (StringUtils.isNotBlank(form.getToDate())) {
                toDate = simpleDateFormat.parse(form.getToDate());
                if (!simpleDateFormat.format(toDate).equals(form.getToDate())) {
                    throw new ParseException(CommonConstants.INVALID_DATE, 0);
                }
            } else {
                errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
            }
        } catch (ParseException pe) {
            errors.add(new GenericException(ErrorCodes.NMC_CONTROL_REPORTS_TO_DATE_INVALID));
        }

        if (fromDate != null) {
            Calendar calFromDate = Calendar.getInstance();
            calFromDate.setTime(fromDate);
            if (toDate != null) {
                if (fromDate.after(toDate)) {
                    errors.add(new GenericException(
                            ErrorCodes.NMC_CONTROL_REPORTS_FROM_DATE_AFTER_TO_DATE));
                }
            }
        }
		// Validate Contract number if it is specified.
		if (form.getContractNumber() != null
				&& form.getContractNumber().length() > 0) {
			// General contract number rule SCR 35
			boolean isValidFormat = ContractNumberNoMandatoryRule.getInstance()
					.validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER,
							errors, form.getContractNumber());
			if (isValidFormat) {
				// check to make sure contract exists.
				Contract c = null;
				try {
					UserProfile profile = getUserProfile(request);
					c = ContractServiceDelegate.getInstance()
							.getContractDetails(
									new Integer(form.getContractNumber()),
									EnvironmentServiceDelegate.getInstance()
									.retrieveContractDiDuration(profile.getRole(), 0,null));
					if (c == null) {
						errors.add(new GenericException(
								ErrorCodes.CONTRACT_NUMBER_INVALID));
					}
				} catch (ContractNotExistException e) {
					errors.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (NumberFormatException e) {
					errors.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				} catch (SystemException e) {
					errors.add(new GenericException(
							ErrorCodes.CONTRACT_NUMBER_INVALID));
				}
			}
		}
		return errors;

	}

	/**
	 * Action Method for search functionality.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@RequestMapping(value ="/participantWebsiteReport/" ,params={"task=search"}   , method =  {RequestMethod.POST}) 
	public String doSearch (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
	Collection<GenericException> errors = doValidate( request,
				form);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
			report.setReportCriteria(new ReportCriteria(
					ParticipantWebsiteReportHandler.REPORT_ID));
			request.setAttribute(Constants.REPORT_BEAN, report);
			return forwards.get("input");
		}
		String forward = doCommon( form, request, response);
		return forward;
	}

	/**
	 * Action Method for reset functionality.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@RequestMapping(value ="/participantWebsiteReport/" ,params={"task=reset"}   , method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		Collection<GenericException> errors = super.doValidate(
				form, request);
		
		form.setFromDate(form.getFromDefaultDate());
		form.setToDate(form.getDefaultDate());
		form.setContractNumber("");
		doCommon( form, request, response);
		return forwards.get(CommonConstants.DEFAULT);

	}
	@RequestMapping(value ="/participantWebsiteReport/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
	       String forward=super.doDefault( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
		
		
	@RequestMapping(value = "/participantWebsiteReport/", params = { "task=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
 
	@RequestMapping(value = "/participantWebsiteReport/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	@RequestMapping(value = "/participantWebsiteReport/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	     	 }

	@RequestMapping(value = "/participantWebsiteReport/", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		ParticipantWebsiteReportData participantWebsiteReportData = new ParticipantWebsiteReportData(); 
		return getReportName()+" "+participantWebsiteReportData.getCurrentDate()+ CSV_EXTENSION;
	}    
	
	/**
	 * Method to download the data to CSV file
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		AlertsReportForm form = (AlertsReportForm) reportForm;
		ParticipantWebsiteReportData ParticipantWebsiteReportData = (ParticipantWebsiteReportData) report;

		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(REPORT_TITLE).append(" ").append(ParticipantWebsiteReportData.getCurrentDate()).append("\"").append(LINE_BREAK);
		buffer.append(CommonConstants.CONTRACT_NUMBER).append(COMMA).append(StringUtils.trimToEmpty(form.getContractNumber()))
		.append(LINE_BREAK);
		buffer.append(CommonConstants.CSV_HEADER_FROM_DATE).append(COMMA).append(form.getFromDate())
				.append(LINE_BREAK);
		buffer.append(CommonConstants.CSV_HEADER_TO_DATE).append(COMMA).append(form.getToDate())
				.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(CommonConstants.ALL_VISITS).append(COMMA)
		.append(CommonConstants.REPEAT_VISITORS).append(COMMA)
		.append(CommonConstants.UNIQUE_VISITORS).append(COMMA)
		.append(CommonConstants.MONTH_WITH_MOST_VISITS).append(COMMA)
		.append(CommonConstants.NUMBER_OF_DOCUMENT_VIEWS).append(LINE_BREAK);
		
		buffer.append(ParticipantWebsiteReportData.getTotalVisitorsCount()).append(COMMA)
				.append(ParticipantWebsiteReportData.getTotalRepeatVisitorsCount()).append(COMMA)
				.append(ParticipantWebsiteReportData.getTotalNewVisitorsCount()).append(COMMA);
		if((ParticipantWebsiteReportData.getMostVisitedMonth()==null) || 
				(ParticipantWebsiteReportData.getMostVisitedMonth()!=null && 
						ParticipantWebsiteReportData.getMostVisitedMonth().isEmpty())){
			buffer.append("N/A");
		}else{
		
			buffer.append(" "+ParticipantWebsiteReportData.getMostVisitedMonth().toString().substring(1, ParticipantWebsiteReportData.getMostVisitedMonth().toString().length() - 1));
		}
		buffer.append(COMMA);
		buffer.append(ParticipantWebsiteReportData.getTotalDocumentViewCount()).append(LINE_BREAK);
		
		form.setTask(null);
		return buffer.toString().getBytes();
	}
	
	/**
	 * Method to populate the report criteria
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("rawtypes")
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		AlertsReportForm alertForm = (AlertsReportForm) actionForm;

		// Add filters to the report criteria
		if (alertForm != null) {

			String contractNumber = alertForm.getContractNumber();
			if (contractNumber != null && !contractNumber.trim().equals("")) {

				try {
					criteria.addFilter(
							ParticipantWebsiteReportData.FILTER_CONTRACT_NUMBER,
							new Integer(contractNumber.trim()));
				} catch (NumberFormatException e) {
					List errors = new ArrayList();
					errors.add(new GenericException(
							ErrorCodes.TECHNICAL_DIFFICULTIES));
					setErrorsInRequest(request, errors);
					throw new SystemException(
							"Exception occured while parsing contract number.");
				}
			}

			try {

				Date fromDate = simpleDateFormat.parse(alertForm.getFromDate());
				criteria.addFilter(
						ParticipantWebsiteReportData.FILTER_FROM_DATE, fromDate);

				Date toDate = simpleDateFormat.parse(alertForm.getToDate());
				criteria.addFilter(ParticipantWebsiteReportData.FILTER_TO_DATE,
						toDate);

			} catch (ParseException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(
						ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException(
						"Exception occured while calculating Dates");
			}
		}

		String task = getTask(request);

		// If it is a download task then set the required filter variable
		if (DOWNLOAD_TASK.equals(task)) {
			criteria.addFilter(PlanDocumentHistoryReportData.FILTER_TASK,
					PlanDocumentHistoryReportData.TASK_DOWNLOAD);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * Gets the current task for this request.
	 * 
	 * @param request
	 *            The current request object.
	 * @return The task for this request.
	 */
	protected String getTask(HttpServletRequest request) {
		String task = null;
		AlertsReportForm alertForm = (AlertsReportForm) request
				.getSession().getAttribute(CommonConstants.ALERT_REPORT_ACTIONFORM);

		if (alertForm != null && alertForm.getTask() != null) {
			task = alertForm.getTask();
		} else {
			task = DEFAULT_TASK;
		}
		return task;
	}
	
	
	
	  
	/*@SuppressWarnings({ "rawtypes" })
	public String validate( Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		request.removeAttribute(CommonConstants.ERROR_RDRCT);
		if (penErrors != null && penErrors.size() > 0) {
			ParticipantWebsiteReportData report = new ParticipantWebsiteReportData();
            report.setReportCriteria(new ReportCriteria(ParticipantWebsiteReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			return mapping.getInputForward();
		}
		return super.validate( form, request);
	}*/


	/**
	 * Get the Report Id
	 */
	@Override
	protected String getReportId() {
		return ParticipantWebsiteReportHandler.REPORT_ID;
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return ParticipantWebsiteReportData.REPORT_NAME;
	}

	/**
	 * Get the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
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
