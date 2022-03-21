package com.manulife.pension.ps.web.tpadownload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;

@Controller
@RequestMapping(value ="/tpadownload")
@SessionAttributes({"tedHistoryFilesForm"})

public class TedHistoryFilesController extends ReportController {

	@ModelAttribute("tedHistoryFilesForm")
	public TedHistoryFilesForm populateForm() {
		return new TedHistoryFilesForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("default", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("sort", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("filter", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("print", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("page", "/tpadownload/tedHistoryFiles.jsp");
		forwards.put("downloadFiles", "/tpadownload/tedFileDownloadServlet");
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		// Not implemented for this report.
		return null;
	}

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(tedHistoryFileValidator);
	}

	@Autowired(required = true)
	TedHistoryFileValidator tedHistoryFileValidator;

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		TedHistoryFilesForm tForm = (TedHistoryFilesForm) form;
		String contractNumber = tForm.getContractNumber();

		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();
		UserProfile userProfile = getUserProfile(request);
		String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String location = Environment.getInstance().getDBSiteLocation();
		criteria.addFilter(TedHistoryFilesReportData.FILTER_PROFILE_ID, profileId);
		criteria.addFilter(TedHistoryFilesReportData.FILTER_SITE_LOCATION, location);

		if (contractNumber != null && contractNumber != "") {
			criteria.addFilter(TedHistoryFilesReportData.FILTER_CONTRACT_NUMBER, contractNumber);
		}
		criteria.setPageSize(getPageSize(request));
	}

	public TedHistoryFilesController() {
		super(TedHistoryFilesController.class);
	}

	/**
	 * Obtains the actual report data.
	 */

	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		Collection errors = new ArrayList();
		request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
		TedHistoryFilesReportData historyData;
		historyData = (TedHistoryFilesReportData) super.getReportData(reportId, reportCriteria, request);
		if (historyData != null) {
			if (historyData.getReturnCode().equals(TedHistoryFilesReportData.STATUS_FTP_SERVER_DOWN)) {
				String carPhone = Environment.getInstance().getGeneralCARPhoneNumber();

				try {
					Content content = ContentCacheManager.getInstance().getContentById(
							ContentConstants.PS_CONTACTS_ACCOUNT_REPRESENTATIVE_PHONE,
							ContentTypeManager.instance().MISCELLANEOUS);
					carPhone = (String) PropertyUtils.getProperty(content, "text");
				} catch (Exception e) {
				}

				Object[] phoneNumber = new String[] { carPhone };
				errors.add(new GenericException(ErrorCodes.TED_FTP_SERVER_DOWN, phoneNumber));
				request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
			}
		}
		return historyData;
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TedHistoryFilesReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TedHistoryFilesReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return TedHistoryFilesReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return TedHistoryFilesReportData.DEFAULT_SORT_ORDER;
	}

	boolean redisplayPreviousData = false;

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		String forward = super.doCommon(reportForm, request, response);
		TedHistoryFilesForm form = (TedHistoryFilesForm) reportForm;

		TedHistoryFilesReportData report = (TedHistoryFilesReportData) request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	@RequestMapping(value ="/tedHistoryFilesReport/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			if (!redisplayPreviousData) {
				TedHistoryFilesReportData reportData = new TedHistoryFilesReportData(null, -1);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/tedHistoryFilesReport/", params = { "task=filter" }, method = {RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm(form, request);
			if (!redisplayPreviousData) {
				TedHistoryFilesReportData reportData = new TedHistoryFilesReportData(null, -1);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
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
		}
		if (logger.isDebugEnabled()) {
			logger.debug("enter -> doFilter");
		}

		form.setSelectedFilenames(null);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doFilter ** Cleared selected file checkboxes **");
		}

		String forward = super.doFilter(form, request, response);
		
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/tedHistoryFilesReport/", params = { "task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			if (!redisplayPreviousData) {
				TedHistoryFilesReportData reportData = new TedHistoryFilesReportData(null, -1);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/tedHistoryFilesReport/", params = { "task=downloadFiles" }, method = {RequestMethod.GET })
	public void doDownloadFiles(@ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form , HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownloadFiles");
		}

			String contractNumber = (String) form.getContractNumber();
			request.setAttribute(Constants.REPORT_BEAN, form.getReport());

			ArrayList fileListFromForm = null;

			if (!StringUtils.isEmpty(form.getSingleFileIdentity())) {
				fileListFromForm = new ArrayList();
				fileListFromForm.add(form.getSingleFileIdentity().trim());
				form.setSingleFileIdentity(null);
			} else {
				fileListFromForm = form.getSelectedFilenameArrayList();
			}

			if (fileListFromForm != null && fileListFromForm.size() > 0) {

				TedRequestedFiles tedRequestedFiles = new TedRequestedFiles();

				ArrayList fileListing = new ArrayList(fileListFromForm.size());
				for (int i = 0; i < fileListFromForm.size(); i++) {
					if (!"N/A".equals(fileListFromForm.get(i))) {
						String filename = (String) fileListFromForm.get(i) + ".ZIP";
						TedRequestedFile requestedFile = new TedRequestedFile();
						requestedFile.setContractNumber(contractNumber);
						requestedFile.setFilename(filename);
						fileListing.add(requestedFile);
					}
				}
				tedRequestedFiles.setFileListing(fileListing);

				tedRequestedFiles.setDirectory(Environment.getInstance().getTedFileDownloadDirectory());
				request.setAttribute("requestedTedFiles", tedRequestedFiles);

				ServletContext servletContext = request.getServletContext();
				RequestDispatcher rd = servletContext.getRequestDispatcher("/tpadownload/tedFileDownloadServlet");
				if (logger.isDebugEnabled()) {
					logger.debug("exit <- doDownloadFiles VIA Downlaod Servlet");
				}
				rd.forward(request, response);
			}
		

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownloadFiles NOT VIA DOWNLOAD SERVLET!!!!!!! ");
		}
	}

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
	@RequestMapping(value ="/tedHistoryFilesReport/", method = { RequestMethod.POST })
	public String execute(@Valid @ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("enter -> execute");
		}
		request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, new ArrayList(0));
			// do a refresh so that there's no problem using tha back button

			if (form.getMyAction().equalsIgnoreCase("downloadFiles")) {
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=downloadFiles", true);
				return "redirect:" + forward.getPath();
			} else {
				ControllerForward forward = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
				return "redirect:" + forward.getPath();			}

			

		
	}

	@RequestMapping(value ="/tedHistoryFilesReport/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("tedHistoryFilesForm") TedHistoryFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		doValidate(form, request);
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			populateReportForm(form, request);
			if (!redisplayPreviousData) {
				TedHistoryFilesReportData reportData = new TedHistoryFilesReportData(null, -1);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
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
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}
