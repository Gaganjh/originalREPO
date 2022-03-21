package com.manulife.pension.ps.web.noticereports;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.manulife.pension.ps.service.report.noticereports.reporthandler.UploadAndShareReportHandler;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportSourceOfUploadShareVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportTopTenDocumentNamesVO;
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
import com.manulife.util.piechart.PieChartBean;


@Controller
@RequestMapping( value ="/noticereports")
@SessionAttributes({"alertsReportForm"})

public class UploadAndShareReportController extends ReportController {
	@ModelAttribute("alertsReportForm") 
	public AlertsReportForm populateForm()
{
		return new AlertsReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/noticereports/uploadAndShareReport.jsp");
		forwards.put("default","/noticereports/uploadAndShareReport.jsp");
		forwards.put("search","/noticereports/uploadAndShareReport.jsp");
		forwards.put("uploadAndShareReportPage","/noticereports/uploadAndShareReport.jsp");
		forwards.put("print","/noticereports/uploadAndShareReport.jsp");}

	
	private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	public static final String REPORT_NAME = "reportName";

	public static final String REPORT_TITLE = "Upload and Share as of";

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private FastDateFormat simpleDateFormat = FastDateFormat.getInstance("MM/dd/yyyy", Locale.US);
	
	private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

	private static final String WEDGE1 = "wedge1";

	private static final String WEDGE2 = "wedge2";

	private static final String WEDGE3 = "wedge3";

	private static final String WEDGE4 = "wedge4";

	private static final String WEDGE5 = "wedge5";

	public static final String WEDGE_FONT_COLOR = "#FFFFFF";

	public static final String UPLOAD_SHARE_NEW_BUSINESS_COLOR_WEDGE_LABEL = "#4F81BD";

	public static final String UPLOAD_SHARE_INFORCE_COLOR_WEDGE_LABEL = "#C0504D";

	public static final String UPLOAD_SHARE_TPA_COLOR_WEDGE_LABEL = "#9BBB59";

	public static final String UPLOAD_SHARE_INTERMEDIARY_CONTACT_COLOR_WEDGE_LABEL = "#8064A2";

	public static final String UPLOAD_SHARE_TOTAL_CARE_COLOR_WEDGE_LABEL = "#4BACC6";

	public static final String UPLOAD_AND_SHARE_FREQUENCY_PIECHART = "pieChart";

	public static final String UPLOAD_AND_SHARE_FREQUENCY_SHARE_PIECHART = "sharePieChart";

	public static final String SINGLE_SPACE_SYMBOL = " ";

	public static final String VIEWING_PREFERENCE = "1";

	public static final int NUMBER_0 = 0;
	
	public static final String BLANK = "";
	
	public static final String DOT = ".";

	public static final String COLOR_BORDER = "#EAEAEA";

	/** Method to get the report data after validation. 
	 * @param mapping
	 * @param request
	 * @param reportForm
	 * @param response
	 * @return ActionForward
	 */
	 
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		List<UploadAndShareReportTopTenDocumentNamesVO> leftColumnList = new ArrayList<UploadAndShareReportTopTenDocumentNamesVO>();
		List<UploadAndShareReportTopTenDocumentNamesVO> rightColumnList = new ArrayList<UploadAndShareReportTopTenDocumentNamesVO>();

		AlertsReportForm alertform = (AlertsReportForm) reportForm;
		Collection<GenericException> errors = super.doValidate(reportForm, request);
		super.doCommon(reportForm, request, response);

		UploadAndShareReportData reportData = (UploadAndShareReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);

		@SuppressWarnings("unused")
		PieChartBean uploadandShareFrequencyPieChartBean = getUploadandShareFrequencyPieChartBean(reportData, request,
				alertform);

		// Populate the top ten docs uploaded into 2 different lists so as to
		// display it in jsp in two different columns.
		if (errors.isEmpty()) {
			populateTopTenDocumentsUploaded(leftColumnList, rightColumnList, alertform, reportData);

		}
		return forwards.get("uploadAndShareReportPage");

	}

	/** Method to populate the Top Ten Documents Uploaded List
	 * @param leftColumnList
	 * @param rightColumnList
	 * @param alertform
	 * @param reportData
	 */
	private void populateTopTenDocumentsUploaded(
			List<UploadAndShareReportTopTenDocumentNamesVO> leftColumnList,
			List<UploadAndShareReportTopTenDocumentNamesVO> rightColumnList,
			AlertsReportForm alertform,
			UploadAndShareReportData reportData) {
		int count=0;
		
		for(UploadAndShareReportTopTenDocumentNamesVO vo : reportData.getTopTenDocumentNamesList()){
			 count++;
			if(count<=5){
				if(vo.getDocumentName()!=null || vo.getDocumentPercentage()!=null){
					
					leftColumnList.add(new UploadAndShareReportTopTenDocumentNamesVO(vo.getDocumentName(), vo.getDocumentPercentage()));	
				}
			}
			else{
				if(vo.getDocumentName()!=null || vo.getDocumentPercentage()!=null){
					
					rightColumnList.add(new UploadAndShareReportTopTenDocumentNamesVO(vo.getDocumentName(), vo.getDocumentPercentage()));	
				}
			}
		}
		if(count>0){
		if( count<=5){
			for(int i=count+1;i<=5;i++){
				leftColumnList.add(new UploadAndShareReportTopTenDocumentNamesVO(BLANK,BigDecimal.ZERO));
			}
			for(int i=1;i<=5;i++){
				rightColumnList.add(new UploadAndShareReportTopTenDocumentNamesVO(BLANK,BigDecimal.ZERO));
			}
		}
		else{
			for(int i=count+1;i<=10;i++){
				rightColumnList.add(new UploadAndShareReportTopTenDocumentNamesVO(BLANK,BigDecimal.ZERO));
			}
		}
		}
		
		alertform.setTopFiveUploadedDocList(leftColumnList);
		alertform.setSixToTenUploadedDocList(rightColumnList);
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
                    throw new ParseException("Invalid Date", 0);
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
                    throw new ParseException("Invalid Date", 0);
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
					UserProfile profile =getUserProfile(request);
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
	@RequestMapping(value ="/uploadAndShareReport/" ,params={"task=search"}   , method =  {RequestMethod.POST}) 
	public String doSearch (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		Collection<GenericException> errors = doValidate(request, form);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
			form = (AlertsReportForm) form;
			// Removing the documents if errors exists.
			form.setTopFiveUploadedDocList(null);
			form.setSixToTenUploadedDocList(null);
			request.setAttribute(Constants.REPORT_BEAN, report);
			return forwards.get("input");
		}
		String forward = doCommon(form, request, response);
		return forward;
	}
	
	/**
	 * Method to override default download name. 
	 * 
	 * @param HttpServletRequest
	 * @return String
	 */
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		UploadAndShareReportData uploadAndShareReportData = new UploadAndShareReportData(); 
		return getReportName()+SINGLE_SPACE_SYMBOL+uploadAndShareReportData.getCurrentDate()+ CSV_EXTENSION;
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
	@RequestMapping(value ="/uploadAndShareReport/", params={"task=reset"} , method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection<GenericException> errors = super.doValidate(form, request);

		form.setFromDate(form.getFromDefaultDate());
		form.setToDate(form.getDefaultDate());
		form.setContractNumber(BLANK);
		doCommon(form, request, response);
		return forwards.get("default");

	}

	@RequestMapping(value ="/uploadAndShareReport/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException { 
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
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

		
		
	@RequestMapping(value = "/uploadAndShareReport/", params = { "task=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
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
	    
	@RequestMapping(value = "/uploadAndShareReport/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
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
  	
	@RequestMapping(value = "/uploadAndShareReport/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/uploadAndShareReport/", params = {"task=download" }, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			UploadAndShareReportData report = new UploadAndShareReportData();
			report.setReportCriteria(new ReportCriteria(UploadAndShareReportHandler.REPORT_ID));
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
	    	
	/**
	 * Method to download the data to CSV file
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
					throws SystemException {
		AlertsReportForm form = (AlertsReportForm) reportForm;
		UploadAndShareReportData uploadAndShareReportData = (UploadAndShareReportData) report;
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(REPORT_TITLE).append(SINGLE_SPACE_SYMBOL).append(uploadAndShareReportData.getCurrentDate()).append("\"").append(LINE_BREAK);
		buffer.append("Contract Number").append(COMMA).append(StringUtils.trimToEmpty(form.getContractNumber()))
		.append(LINE_BREAK);
		buffer.append("From Date").append(COMMA).append(form.getFromDate())
		.append(LINE_BREAK);
		buffer.append("To Date").append(COMMA).append(form.getToDate())
		.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("User Statistics").append(LINE_BREAK);
		buffer.append("No. of contracts using the service").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfContractsUsingService()).append(COMMA);
		buffer.append("Terms of use: Users that accept").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfUsersAcceptedTermsOfUse()).append(LINE_BREAK);
		buffer.append("No. of users using the service").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfUsersUsingService()).append(COMMA);
		buffer.append("Terms of use: Users that did not accept").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfUsersNotAcceptedTermsOfUse()).append(LINE_BREAK);
		buffer.append("Percentage of contracts using share").append(COMMA)
		.append(uploadAndShareReportData.getPercentageOfContractsUsingShare()).append(Constants.PERCENT).append(COMMA);
		buffer.append("Average no. of documents uploaded per contract").append(COMMA)
		.append(uploadAndShareReportData.getAvgNumberOfDocumentsUploadedPerContract()).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Document Statistics").append(LINE_BREAK);
		buffer.append("No. of documents added").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfDocumentsUploaded()).append(COMMA);
		buffer.append("No. of documents deleted").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfDocumentsDeleted()).append(LINE_BREAK);
		buffer.append("No. of documents renamed").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfDocumentsRenamed()).append(COMMA);
		buffer.append("No. of documents changed and replaced").append(COMMA)
		.append(uploadAndShareReportData.getNumberDocumentsChangedAndReplaced()).append(LINE_BREAK);
		buffer.append("No. of documents replaced").append(COMMA)
		.append(uploadAndShareReportData.getNumberOfDocumentsReplaced()).append(COMMA);
		buffer.append("Percentage of documents shared with participants").append(COMMA)
		.append(uploadAndShareReportData.getPercentageOfDocumentsSharedWithParticipants()).append(Constants.PERCENT).append(LINE_BREAK);
		
		if(!uploadAndShareReportData.getTopTenDocumentNamesList().isEmpty()){
			buffer.append("Top 10 document names uploaded by users").append(LINE_BREAK);
		}
		int count=1;
		for(UploadAndShareReportTopTenDocumentNamesVO vo : uploadAndShareReportData.getTopTenDocumentNamesList()){
			buffer.append(count).append(DOT).append(COMMA).append(vo.getDocumentName()).append(COMMA).append(vo.getDocumentPercentage()).append(Constants.PERCENT).append(LINE_BREAK);
			count++;
		}

		form.setTask(null);
		return buffer.toString().getBytes();

	}
	
	/**
	 * Method to create the search criteria
	 * 
	 * @param ReportCriteria
	 * @param BaseReportForm
	 * @param HttpServletRequest
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
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
			if (contractNumber != null && !contractNumber.trim().equals(BLANK)) {

				try {
					criteria.addFilter(
							UploadAndShareReportData.FILTER_CONTRACT_NUMBER,
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
						UploadAndShareReportData.FILTER_FROM_DATE, fromDate);

				Date toDate = simpleDateFormat.parse(alertForm.getToDate());
				criteria.addFilter(UploadAndShareReportData.FILTER_TO_DATE,
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
				.getSession().getAttribute("alertsReportForm");

		if (alertForm != null && alertForm.getTask() != null) {
			task = alertForm.getTask();
		} else {
			task = DEFAULT_TASK;
		}
		return task;
	}
	
	/**
	 * Get the Report Id
	 */
	@Override
	protected String getReportId() {
		return UploadAndShareReportHandler.REPORT_ID;
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return UploadAndShareReportData.REPORT_NAME;
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
	 * Method to create the pie chart properties
	 */
	private PieChartBean createDefaultPieChartBean() {
		PieChartBean pieChart = new PieChartBean();
		pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
		pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
		pieChart.setBorderColor(COLOR_BORDER);
		pieChart.setShowWedgeLabels(true);
		pieChart.setUsePercentsAsWedgeLabels(true);
		pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
		pieChart.setBorderWidth((float) 1.5);
		pieChart.setWedgeLabelOffset(75);
		pieChart.setFontSize(10);
		pieChart.setFontBold(true);
		pieChart.setDrawBorders(true);
		pieChart.setWedgeLabelExtrusion(35);
		pieChart.setWedgeLabelExtrusionThreshold(0);
		pieChart.setWedgeLabelExtrusionColor("#000000");
		return pieChart;
	}

	 /**
	 * Method to get the pie chart filled with the values obtained from reportData
	 * @param reportData
	 * @param request 
	 * @param alertform 
	 * @return pieChart
	 */
	private PieChartBean getUploadandShareFrequencyPieChartBean(UploadAndShareReportData reportData, HttpServletRequest request, AlertsReportForm alertform) {
		PieChartBean pieChart = createDefaultPieChartBean();

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getUploadandShareFrequencyPieChartBean");
		}

		List<UploadAndShareReportSourceOfUploadShareVO> freqList = reportData.getSourceOfUploadList();
		List<UploadAndShareReportSourceOfUploadShareVO> freqenceList = reportData.getSourceOfShareList();
		boolean isPieChart = false;

		if (freqList.size() > 0) {

			String[] wedgesArr = { WEDGE1, WEDGE2, WEDGE3, WEDGE4, WEDGE5 };
			int wedgeCount = 0;
			int nonEmptyWedgeCount = 0;
			for (UploadAndShareReportSourceOfUploadShareVO vo : freqList) {
				if(vo!=null && vo.getPercentageUpload().intValue() > 0){
					pieChart.addPieWedge( wedgesArr[wedgeCount++],Integer.valueOf(vo.getPercentageUpload().intValue()),getWedgeColor(vo.getUserCategory())
							,SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
					nonEmptyWedgeCount++;
					isPieChart = true;
				}
			}
			if(nonEmptyWedgeCount > 0){
				alertform.setSourceOfUploadListInd(true);
			}else{
				alertform.setSourceOfUploadListInd(false);
			}
		}


		if (isPieChart) {

			pieChart.setAppletWidth(100);
			pieChart.setAppletHeight(115);

			pieChart.setPieWidth(90);
			pieChart.setPieHeight(90);
			request.setAttribute(UPLOAD_AND_SHARE_FREQUENCY_PIECHART, pieChart);
		}
		pieChart = createDefaultPieChartBean();
		if (freqenceList.size() > 0) {

			String[] wedgesArr = { WEDGE1, WEDGE2, WEDGE3, WEDGE4, WEDGE5 };
			int wedgeCount = 0;
			int nonEmptyWedgeCount = 0;
			for (UploadAndShareReportSourceOfUploadShareVO vo : freqenceList) {
				if(vo!=null && vo.getPercentageUpload().intValue() > 0){
					pieChart.addPieWedge( wedgesArr[wedgeCount++],Integer.valueOf(vo.getPercentageUpload().intValue()),getWedgeColor(vo.getUserCategory())
							,SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
					alertform.setSourceOfShareListInd(false);
					nonEmptyWedgeCount++;
					isPieChart = true;
				}
			}
			if(nonEmptyWedgeCount > 0){
				alertform.setSourceOfShareListInd(true);
			}else{
				alertform.setSourceOfShareListInd(false);
			}
		}

		if (isPieChart) {

			pieChart.setAppletWidth(100);
			pieChart.setAppletHeight(115);

			pieChart.setPieWidth(90);
			pieChart.setPieHeight(90);
			request.setAttribute(UPLOAD_AND_SHARE_FREQUENCY_SHARE_PIECHART, pieChart);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getUploadandShareFrequencyPieChartBean");
		}
		return pieChart;
	}

	/**
	 * Method to get the Wedge color for the pie chart
	 * @param frequencyName
	 * @return String
	 */
	private String getWedgeColor(String frequnecyName) {

		if (frequnecyName.equalsIgnoreCase(UploadAndShareReportData.PS_NEW_BUSINESS_USER_CATEGORY)) {
			return UPLOAD_SHARE_NEW_BUSINESS_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(UploadAndShareReportData.PS_INFORCE_USER_CATEGORY)) {
			return UPLOAD_SHARE_INFORCE_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(UploadAndShareReportData.TPA_USER_CATEGORY)) {
			return UPLOAD_SHARE_TPA_COLOR_WEDGE_LABEL;
		} else if (frequnecyName.equalsIgnoreCase(UploadAndShareReportData.INTERMEDIARY_CONTACT_USER_CATEGORY)) {
			return UPLOAD_SHARE_INTERMEDIARY_CONTACT_COLOR_WEDGE_LABEL;
		} else if (UploadAndShareReportData.TOTAL_CARE_USER_CATEGORY.equalsIgnoreCase(frequnecyName) ){
			return UPLOAD_SHARE_TOTAL_CARE_COLOR_WEDGE_LABEL;
		}

		return null;
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
