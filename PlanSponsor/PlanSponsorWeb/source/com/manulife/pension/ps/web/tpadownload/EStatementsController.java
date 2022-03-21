package com.manulife.pension.ps.web.tpadownload;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import com.manulife.pension.platform.web.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.bos.ereports.common.ReportFileNotFoundException;
import com.manulife.pension.lp.model.ereports.ReportFileRequest;
import com.manulife.pension.lp.model.ereports.ReportFileResponse;
import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.contract.ContractStatementsHelper;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;

@Controller
@RequestMapping(value = "/tpadownload")
@SessionAttributes({ "eStatementsForm" })

public class EStatementsController extends ReportController {

	@ModelAttribute("eStatementsForm")
	public EStatementsForm populateForm() {
		return new EStatementsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/tpadownload/eStatements.jsp");
		forwards.put("default", "/tpadownload/eStatements.jsp");
		forwards.put("sort", "/tpadownload/eStatements.jsp");
		forwards.put("filter", "/tpadownload/eStatements.jsp");
		forwards.put("page", "/tpadownload/eStatements.jsp");
		forwards.put("print", "/tpadownload/eStatements.jsp");
		forwards.put("secondaryWindowError", "/WEB-INF/global/secondaryWindowError.jsp");
	}

	private static final String CLIENT_USER_TYPE = "CLIENT";
	private static final String CLIENTID_AND_FILENAME = "clientIdAndFileName";
	private static final String DOWNLOAD_FILES_TASK = "downloadZipFile";
	private static final String NO_CHECKBOX_SELECTED_IND = "N/A";
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		EStatementsForm form = (EStatementsForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		String filterContractName = form.getFilterContractName();
		String filterContractNumber = form.getFilterContractNumber();
		String filterCorrected = form.getFilterCorrected();
		String filterStatementType = form.getFilterStatementType();
		String filterReportEndDateFrom = form.getFilterReportEndDateFrom();
		String filterReportEndDateTo = form.getFilterReportEndDateTo();
		String filterIsYearEnd = form.getFilterIsYearEnd();

		UserProfile userProfile = getUserProfile(request);

		String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String location = Environment.getInstance().getDBSiteLocation();

		criteria.addFilter(EStatementsReportData.FILTER_PROFILE_ID, profileId);
		criteria.addFilter(EStatementsReportData.FILTER_SITE_LOCATION, location);
		criteria.addFilter(EStatementsReportData.FILTER_CONTRACT_NAME, filterContractName);
		criteria.addFilter(EStatementsReportData.FILTER_CONTRACT_NUMBER, filterContractNumber);
		criteria.addFilter(EStatementsReportData.FILTER_CORRECTED_CODE, filterCorrected);
		criteria.addFilter(EStatementsReportData.FILTER_STATEMENT_TYPE_CODE, filterStatementType);
		criteria.addFilter(EStatementsReportData.FILTER_YEAR_END, filterIsYearEnd);

		if (filterReportEndDateFrom == null) {
			criteria.addFilter(EStatementsReportData.FILTER_REPORT_END_DATE_FROM, form.getReportDateFromList().get(12));
			form.setFilterReportEndDateFrom(Long.toString(((Date) form.getReportDateFromList().get(12)).getTime()));
		} else {
			criteria.addFilter(EStatementsReportData.FILTER_REPORT_END_DATE_FROM,
					new Date(Long.parseLong(filterReportEndDateFrom)));
			form.setFilterReportEndDateFrom(filterReportEndDateFrom);
		}

		if (filterReportEndDateTo == null) {
			criteria.addFilter(EStatementsReportData.FILTER_REPORT_END_DATE_TO, form.getReportDateToList().get(0));
			form.setFilterReportEndDateTo(Long.toString(((Date) form.getReportDateToList().get(0)).getTime()));
		} else {
			criteria.addFilter(EStatementsReportData.FILTER_REPORT_END_DATE_TO,
					new Date(Long.parseLong(filterReportEndDateTo)));
			form.setFilterReportEndDateTo(filterReportEndDateTo);
		}

		criteria.addFilter(EStatementsReportData.FILTER_YEAR_END, filterIsYearEnd);

		criteria.setPageSize(getPageSize(request));

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	public EStatementsController() {
		super(EStatementsController.class);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return EStatementsReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return EStatementsReportData.REPORT_NAME;
	}

	  protected String doCommon(
	            BaseReportForm reportForm, HttpServletRequest request,
	            HttpServletResponse response) throws
	            SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		EStatementsForm form =(EStatementsForm)reportForm;
		String forward = super.doCommon(reportForm, request, response);

		EStatementsReportData report = (EStatementsReportData) request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	protected String getDefaultSort() {
		return EStatementsReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return EStatementsReportData.DEFAULT_SORT_ORDER;
	}

	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {
		return null;
	}

	@RequestMapping(value = "/eStatements/", params= {"task=downloadFile"},method = { RequestMethod.GET })
	public String doDownloadFile(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
	
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		Collection errors = doValidate(form, request);

		if(errors.size() >0){
			return forwards.get("input");
		}

		if (logger.isDebugEnabled())
			logger.debug("entry -> downloadFile");


		String sitemode = Environment.getInstance().getSiteLocation();
		String clientIdAndFileName = request.getParameter(CLIENTID_AND_FILENAME);

		// logg the action
		if (logger.isDebugEnabled()) {
			logger.debug(clientIdAndFileName);
		}

		try {
			StringTokenizer tokenizer = new StringTokenizer(clientIdAndFileName, ":");
			String clientId = tokenizer.nextToken();
			String fileName = tokenizer.nextToken();

			ReportInfo reportInfo = ContractStatementsHelper.parsePdfFileName(fileName);
			if (logger.isDebugEnabled()) {
				logger.debug(reportInfo);
			}

			ReportFileResponse fileResponse = getReport(clientId, sitemode, reportInfo);
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setContentType(Constants.MIME_TYPE_PDF);
			response.setContentLength(fileResponse.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileResponse.getReportFragment());
			out.close();

		} catch (ReportFileNotFoundException e) {
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (ParseException e) {
			throw new SystemException(e, getClass().getName(), "doDownloadFile", "Failed to get report. Site mode ["
					+ sitemode + "] clientIdAndfileName [" + clientIdAndFileName + "]");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- downloadFile");

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN,
					LayoutBeanRepository.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/eStatements/", params = { "task=downloadZipFile" },method = { RequestMethod.POST })
	public String doDownloadZipFile(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("entry -> doDownloadZipFile");
		Collection errors = doValidate(form, request);
		if(!errors.isEmpty()){
			return forwards.get("input");
		}

		String sitemode = Environment.getInstance().getSiteLocation();

		try {
			String[] selectedStatements = form.getSelectedStatements();
			ReportInfo[] reportInfos = new ReportInfo[1];

			response.setContentType("application/octet-stream");
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Content-Disposition", "attachment; filename=\"EStatements_"
					+ DateRender.formatByPattern(new Date(), "", "yyyyMMdd_HHmmss") + ".zip\"");

			CheckedOutputStream csum = new CheckedOutputStream(response.getOutputStream(), new Adler32());

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));

			for (int i = 0; i < selectedStatements.length; i++) {

				if (!NO_CHECKBOX_SELECTED_IND.equals(selectedStatements[i])) {
					StringTokenizer tokenizer = new StringTokenizer(selectedStatements[i], ":");
					String clientId = tokenizer.nextToken();
					String fileName = tokenizer.nextToken();

					ReportInfo reportInfo = ContractStatementsHelper.parsePdfFileName(fileName);
					if (logger.isDebugEnabled()) {
						logger.debug(reportInfo);
					}

					ReportFileResponse fileResponse = getReport(clientId, sitemode, reportInfo);

					out.putNextEntry(new ZipEntry(fileName));
					out.write(fileResponse.getReportFragment());
				}
			}

			out.close();

		} catch (ReportFileNotFoundException e) {
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (ParseException e) {
			throw new SystemException(e, getClass().getName(), "doDownloadZipFile", "Failed to get report. Site mode ["
					+ sitemode + "] selectedStatements [" + form.getSelectedStatements() + "]");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- doDownloadZipFile");

		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN,
					LayoutBeanRepository.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}

	/**
	 * @see PsAction#execute(ActionMapping, Form, HttpServletRequest,
	 *      HttpServletResponse)
	 *//*
	@RequestMapping(value = "/eStatements/", method = { RequestMethod.POST, RequestMethod.GET })
	public String execute(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> execute");

		SessionHelper.setErrorsInSession(request, new ArrayList(0));
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// do a refresh so that there's no problem using tha back button
			String forward = new UrlPathHelper().getPathWithinApplication(request);
			if (logger.isDebugEnabled()) {
				logger.debug("exit -> execute with forward = " + forward);
			}
			return forward;
		}

		String forward = super.execute(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> execute with forwardPath=" + forward);
		}

		return forward;
	}
*/
	/**
	 * Invokes the default task (the initial page). It uses the common workflow with
	 * validateForm set to true.
	 * 
	 * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 */
	@RequestMapping(value = "/eStatements/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(errors.size() >0){
			return forwards.get("input");
		}
		if (logger.isDebugEnabled())
			logger.debug("entry -> doDefault");

		String forward = doCommon(form, request, response);

		if (logger.isDebugEnabled())
			logger.debug("exit <- doDefault");

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/eStatements/", params = { "task=filter" }, method = { RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(!errors.isEmpty()){
			return forwards.get("input");
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/eStatements/", params = {  "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(!errors.isEmpty()){
			return forwards.get("input");
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/eStatements/", params = {  "task=print" }, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);
		if (errors.size() > 0) {
		return 	forwards.get("input");
		}
		String forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/eStatements/", params = {  "task=sort" }, method = {RequestMethod.POST })
	public String doSort(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(!errors.isEmpty()){
			return forwards.get("input");
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/eStatements/", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(!errors.isEmpty()){
			return forwards.get("input");
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/eStatements/", params = { "task=dowanloadAll" }, method = {
			RequestMethod.POST })
	public String doDownloadAll(@Valid @ModelAttribute("eStatementsForm") EStatementsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			EStatementsReportData report = new EStatementsReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// forward
																											// not
																											// //available,
																											// provided
																											// default
			}
		}
		Collection errors = doValidate(form, request);

		if(!errors.isEmpty()){
			return forwards.get("input");
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected Collection doValidate(ActionForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		Collection errors = super.doValidate(form, request);
		EStatementsForm theForm = (EStatementsForm) form;
		String filterContractNumber = theForm.getFilterContractNumber();
		String filterReportEndDateFrom = theForm.getFilterReportEndDateFrom();
		String filterReportEndDateTo = theForm.getFilterReportEndDateTo();
		boolean redisplayPreviousData = false;

		if (getTask(request).equals(FILTER_TASK) || getTask(request).equals(PRINT_TASK)
				|| getTask(request).equals(SORT_TASK)) {
			if (doContractNumberValidator(filterContractNumber)) {
				errors.add(new GenericException(ErrorCodes.ESTATEMENTS_CONTRACT_NUMBER_IS_NOT_VALID));
			}

			if (Long.parseLong(filterReportEndDateFrom) > Long.parseLong(filterReportEndDateTo)) {
				errors.add(new GenericException(ErrorCodes.INVALID_DATE_RANGE));
			}
		} else if (getTask(request).equals(DOWNLOAD_FILES_TASK)) {

			String firstCheckBoxSelected = theForm.getSelectedStatements()[0];
			int selectedCheckBoxCount = theForm.getSelectedFilenameArrayList().size();

			if (selectedCheckBoxCount == 1 && firstCheckBoxSelected.equals(NO_CHECKBOX_SELECTED_IND)) {
				errors.add(new GenericException(ErrorCodes.ESTATEMENTS_NO_CHECK_BOX_SELECTED));
				redisplayPreviousData = true;
			}

		}

		if (errors.size() > 0) {
			populateReportForm(theForm, request);
			SessionHelper.setErrorsInSession(request, errors);
			if (!redisplayPreviousData) {
				EStatementsReportData reportData = new EStatementsReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			} else {
				request.setAttribute(Constants.REPORT_BEAN, theForm.getReport());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}

		return errors;

	}

	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		super.populateReportForm(reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}

		if (task.equals(DEFAULT_TASK)) {
			ArrayList reportDateFromList = new ArrayList();
			ArrayList reportDateToList = new ArrayList();

			int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

			Calendar.getInstance().clear();

			Calendar calendarFrom = Calendar.getInstance();
			calendarFrom.set(Calendar.DAY_OF_MONTH, 1);
			calendarFrom.clear(Calendar.HOUR);
			calendarFrom.clear(Calendar.MINUTE);
			calendarFrom.clear(Calendar.SECOND);
			calendarFrom.clear(Calendar.MILLISECOND);

			reportDateFromList.add(calendarFrom.getTime());

			Calendar calendarTo = Calendar.getInstance();
			calendarTo.set(Calendar.DAY_OF_MONTH, maxDay);
			calendarTo.clear(Calendar.HOUR);
			calendarTo.clear(Calendar.MINUTE);
			calendarTo.clear(Calendar.SECOND);
			calendarTo.clear(Calendar.MILLISECOND);

			reportDateToList.add(calendarTo.getTime());

			for (int i = 0; i < 24; i++) {
				calendarFrom.add(Calendar.MONTH, -1);
				calendarTo.add(Calendar.MONTH, -1);
				maxDay = calendarTo.getActualMaximum(Calendar.DAY_OF_MONTH);
				calendarTo.set(Calendar.DATE, maxDay);

				reportDateFromList.add(calendarFrom.getTime());
				reportDateToList.add(calendarTo.getTime());
			}

			EStatementsForm eStatementsForm = (EStatementsForm) reportForm;
			eStatementsForm.setReportDateFromList(reportDateFromList);
			eStatementsForm.setReportDateToList(reportDateToList);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}

	}

	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportData data = super.getReportData(reportId, reportCriteria, request);

		if (data.getDetails().size() > getPageSize(request)) {
			Collection errors = new ArrayList();
			String[] errorParams = new String[1];
			errorParams[0] = Integer.toString(getPageSize(request));
			errors.add(new GenericException(ErrorCodes.ESTATEMENTS_TOO_MANY_RECORDS, errorParams));
			SessionHelper.setErrorsInSession(request, errors);
			data.setTotalCount(getPageSize(request));
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
		return data;
	}

	/**
	 * @param request
	 * 
	 * @return The number of report items per page.
	 */
	protected int getPageSize(HttpServletRequest request) {
		// Return the page size from web.xml
		return new Integer(Environment.getInstance().getEStatementsPageSize()).intValue();
	}

	public ReportFileResponse getReport(String clientId, String sitemode, ReportInfo reportInfo)
			throws SystemException, ServiceUnavailableException, ReportFileNotFoundException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();

		// prepares request parameters
		ReportFileRequest reportRequest = new ReportFileRequest();
		reportRequest.setCompanyCode(sitemode.equals(Constants.SITEMODE_USA) ? RequestConstants.COMPANY_CODE_USA
				: RequestConstants.COMPANY_CODE_NY);
		reportRequest.setCompressFile(false);
		reportRequest.setReportKeys(new ReportInfo[] { reportInfo });
		reportRequest.setStaffPlanAccessAllowed(false);
		reportRequest.setClientId(clientId);
		reportRequest.setUserType(CLIENT_USER_TYPE);

		return delegate.getReportFile(reportRequest);

	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */

	/*
	 * @SuppressWarnings({ "rawtypes" }) public ActionForward validate(ActionMapping
	 * mapping, Form form, HttpServletRequest request) { Collection penErrors
	 * = PsValidation.doValidatePenTestAutoAction(form, mapping, request,
	 * CommonConstants.INPUT); if (penErrors != null && penErrors.size() > 0) {
	 * EStatementsReportData report = new EStatementsReportData(null,0);
	 * request.setAttribute(Constants.REPORT_BEAN, report); return
	 * mapping.getInputForward(); } return super.validate(mapping, form, request); }
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}