package com.manulife.pension.ps.web.tpadownload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;
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

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.tpadownload.dao.TedCurrentFilesDAO;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.StaticHelperClass;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;

@Controller
@RequestMapping(value ="/tpadownload")
@SessionAttributes({"tedCurrentFilesForm"})
public class TedCurrentFilesController extends ReportController {

	@ModelAttribute("tedCurrentFilesForm")
	public TedCurrentFilesForm populateForm() {
		return new TedCurrentFilesForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
        forwards.put("input", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("default", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("sort", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("filter", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("print", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("page", "/tpadownload/tedCurrentFiles.jsp");
        forwards.put("downloadFiles", "/tpadownload/tedFileDownloadServlet");
	}

	private static final String DOWNLOAD_FILES_TASK = "downloadFiles";
	
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
		binder.addValidators(tedCurrentFileValidator);
	}

	@Autowired 
	TedCurrentFileValidator tedCurrentFileValidator;

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm actionForm,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		TedCurrentFilesForm form = (TedCurrentFilesForm) actionForm;
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		String filterContractName = form.getFilterContractName();
		String filterContractNumber = form.getFilterContractNumber();
		String filterDownloadStatus = form.getFilterDownloadStatus();
		String filterCorrected = form.getFilterCorrected();
		String filterYearEnd = form.getFilterYearEnd();
		String filterPeriodEndDate = form.getFilterPeriodEndDate();

		UserProfile userProfile = getUserProfile(request);

		String profileId = String.valueOf(userProfile.getPrincipal().getProfileId());
		String location = Environment.getInstance().getDBSiteLocation();

		criteria.addFilter(TedCurrentFilesReportData.FILTER_PROFILE_ID, profileId);
		criteria.addFilter(TedCurrentFilesReportData.FILTER_SITE_LOCATION, location);
		// CL120161 fix - Single quote in SQL is not escaped properly for ContractName
		criteria.addFilter(TedCurrentFilesReportData.FILTER_CONTRACT_NAME,
				StringEscapeUtils.escapeSql(filterContractName));
		criteria.addFilter(TedCurrentFilesReportData.FILTER_CONTRACT_NUMBER, filterContractNumber);
		criteria.addFilter(TedCurrentFilesReportData.FILTER_CORRECTED, filterCorrected);
		criteria.addFilter(TedCurrentFilesReportData.FILTER_DOWNLOAD_STATUS, filterDownloadStatus);
		criteria.addFilter(TedCurrentFilesReportData.FILTER_YEAR_END, filterYearEnd);
		criteria.addFilter(TedCurrentFilesReportData.FILTER_PERIOD_END_DATE, filterPeriodEndDate);

		criteria.setPageSize(getPageSize(request));

		if (logger.isDebugEnabled()) {
			logger.debug("CRITERIA = " + StaticHelperClass.toString(criteria));
			logger.debug("exit <- populateReportCriteria");
		}
	}

	public TedCurrentFilesController() {
		super(TedCurrentFilesController.class);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TedCurrentFilesReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TedCurrentFilesReportData.REPORT_NAME;
	}

	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		TedCurrentFilesReportData currentData;
		Collection errors = new ArrayList();
		request.getSession(false).setAttribute(PsBaseUtil.ERROR_KEY, errors);
		;
		currentData = (TedCurrentFilesReportData) super.getReportData(reportId, reportCriteria, request);

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		if (currentData != null) {
			if (currentData.getErrorCode().equals(TedCurrentFilesReportData.RETURN_CODE_FTP_SERVER_DOWN)) {
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
		if (logger.isDebugEnabled()) {
			if (currentData == null) {
				logger.debug("currentData was NULL");
			} else {
				logger.debug("currentData size = " + currentData.getTotalCount());
			}
			logger.debug("exit <- getReportData");
		}
		return currentData;
	}

	
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
	        throws SystemException
	    {
	        if(logger.isDebugEnabled())
	        {
	            logger.debug("entry -> doCommon");
	        }
	        String forward = super.doCommon(reportForm, request, response);
	        TedCurrentFilesForm form = (TedCurrentFilesForm)reportForm;
	        TedCurrentFilesReportData report = (TedCurrentFilesReportData)request.getAttribute("reportBean");
	        form.setReport(report);
	        if(getTask(request).equalsIgnoreCase("print"))
	        {
	            request.getSession(false).setAttribute("errorKey", new ArrayList(0));
	        }
	        if(logger.isDebugEnabled())
	        {
	            logger.debug((new StringBuilder()).append("exit <- doCommon with forwardPath = ").append(forward).toString());
	        }
	        return forward;
	    }

	protected String getDefaultSort() {
		return TedCurrentFilesReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return TedCurrentFilesReportData.DEFAULT_SORT_ORDER;
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params ="task=filter", method = {
			RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String f = preExecute(form, request, response);
		if(StringUtils.isNotBlank(f)){
			return f;
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doFilter");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("** Clearing Selected Checkboxes **");
		}
		form.setSelectedIdentities(null);

		String forward = super.doFilter(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doFilter with forwardPath = " + forward);
		}

		return forwards.get(forward);
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params ="task=transferFiles", method = {
			RequestMethod.POST , RequestMethod.GET})
	public void doTransferFiles(@ModelAttribute("tedCurrentFilesForm")TedCurrentFilesForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doTransferFiles");
		}

		ArrayList identityArray;

		//TedCurrentFilesForm form = (TedCurrentFilesForm) reportForm;

		if (form.getSingleFileIdentity() != null && !form.getSingleFileIdentity().equals("")) {
			identityArray = new ArrayList();
			identityArray.add(form.getSingleFileIdentity().trim());
			form.setSingleFileIdentity("");
		} else {
			identityArray = form.getSelectedFilenameArrayList();
		}

		if (identityArray.size() > 0) {

			TedRequestedFiles requestedFiles = new TedRequestedFiles();

			ArrayList fileListing = new ArrayList(identityArray.size());
			for (int i = 0; i < identityArray.size(); i++) {
				String identity = (String) identityArray.get(i);
				if (!"N/A".equals(identity)) {
					String tpaid = identity.substring(0, 5);
					String contractNumber = null;
					String quarterEndDate = null;
					String filename = null;
					String yearEndInd = null;
					if (identity.charAt(Constants.FIVE_DIGIT_CONTRACT_CHAR_VALUE) == '-') {
						contractNumber = identity.substring(Constants.FIVE_DIGIT_CONTRACT_NUMBER_START_INDEX,
								Constants.FIVE_DIGIT_CONTRACT_NUMBER_START_INDEX + 5);
						quarterEndDate = identity.substring(Constants.FIVE_DIGIT_ENDDATE_START_INDEX,
								Constants.FIVE_DIGIT_ENDDATE_START_INDEX + 10);
						yearEndInd = identity.substring(Constants.FIVE_DIGIT_YEAREND_START_INDEX,
								Constants.FIVE_DIGIT_YEAREND_START_INDEX + 1);
						filename = identity.substring(Constants.FIVE_DIGIT_CONTRACT_FILE_INDEX);
					}
					if (identity.charAt(Constants.SIX_DIGIT_CONTRACT_CHAR_VALUE) == '-') {
						contractNumber = identity.substring(Constants.SIX_DIGIT_CONTRACT_NUMBER_START_INDEX,
								Constants.SIX_DIGIT_CONTRACT_NUMBER_START_INDEX + 6);
						quarterEndDate = identity.substring(Constants.SIX_DIGIT_ENDDATE_START_INDEX,
								Constants.SIX_DIGIT_ENDDATE_START_INDEX + 10);
						yearEndInd = identity.substring(Constants.SIX_DIGIT_YEAREND_START_INDEX,
								Constants.SIX_DIGIT_YEAREND_START_INDEX + 1);
						filename = identity.substring(Constants.SIX_DIGIT_CONTRACT_FILE_INDEX);
					}
					if (identity.charAt(Constants.SEVEN_DIGIT_CONTRACT_CHAR_VALUE) == '-') {
						contractNumber = identity.substring(Constants.SEVEN_DIGIT_CONTRACT_NUMBER_START_INDEX,
								Constants.SEVEN_DIGIT_CONTRACT_NUMBER_START_INDEX + 7);
						quarterEndDate = identity.substring(Constants.SEVEN_DIGIT_ENDDATE_START_INDEX,
								Constants.SEVEN_DIGIT_ENDDATE_START_INDEX + 10);
						yearEndInd = identity.substring(Constants.SEVEN_DIGIT_YEAREND_START_INDEX,
								Constants.SEVEN_DIGIT_YEAREND_START_INDEX + 1);
						filename = identity.substring(Constants.SEVEN_DIGIT_CONTRACT_FILE_INDEX);
					}
					TedRequestedFile requestedFile = new TedRequestedFile();
					requestedFile.setContractNumber(contractNumber);
					requestedFile.setFilename(filename);
					requestedFile.setTpaId(tpaid);
					requestedFile.setQuarterEndDate(quarterEndDate);
					requestedFile.setYearEndInd(yearEndInd);
					fileListing.add(requestedFile);
					try {
						// We update the indicator before the FTP ends as we need to refresh the
						// page
						// before the FTP actually sends, and all indicators must be updated upon
						// refresh.
						TedCurrentFilesDAO.updateDownloadInd(requestedFile.getTpaId(),
								requestedFile.getContractNumber(), requestedFile.getQuarterEndDate());
					} catch (SystemException e) {
						logger.error(e);
						// We don't want the update of the indicator to cause file download to fail.
						// We can log the error though.
					}
				}
			}

			requestedFiles.setFileListing(fileListing);
			if (logger.isDebugEnabled()) {
				logger.debug("Files Requested: " + StaticHelperClass.toString(fileListing));
			}

			requestedFiles.setDirectory(Environment.getInstance().getTedFileDownloadDirectory());
			request.setAttribute("requestedTedFiles", requestedFiles);

			ServletContext servletContext = request.getServletContext();
			RequestDispatcher rd = servletContext.getRequestDispatcher("/tpadownload/tedFileDownloadServlet");
			if (logger.isDebugEnabled()) {
				logger.debug("exit <- doTransferFiles (via forward to Download Servlet)");
			}
			form.setSelectedIdentities(null);
			rd.forward(request, response);
		}
	}

    public String preExecute(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
        {
            if(logger.isDebugEnabled())
            {
                logger.debug("entry -> execute");
            }
            request.getSession(false).setAttribute("errorKey", new ArrayList(0));
            if("POST".equalsIgnoreCase(request.getMethod()))
            {
                TedCurrentFilesForm form = (TedCurrentFilesForm)actionForm;
                String forward = null;
                if(form.getMyAction().equalsIgnoreCase("transferFiles"))
                {
                    forward = "redirect:/do/tpadownload/tedCurrentFilesReport/?task=transferFiles";
                } else
                {
                    forward = "redirect:/do/tpadownload/tedCurrentFilesReport/?task="+getTask(request);
                }
                if(logger.isDebugEnabled())
                {
                    logger.debug((new StringBuilder()).append("forward = ").append(forward).toString());
                }
                
                return forward;
            }
            if(logger.isDebugEnabled())
            {
                logger.debug((new StringBuilder()).append("exit -> execute with forwardPath=").append("").toString());
            }
            return null;
        }
    
	/**
	 * Invokes the default task (the initial page). It uses the common workflow with
	 * validateForm set to true.
	 * 
	 */
	@RequestMapping(value ="/tedCurrentFilesReport/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
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

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		 forward = doCommon(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault with forwardPath = " + forward);
		}

		return forwards.get("input");
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params = {  "task=page" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
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
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params = {  "task=sort" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
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
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params = { "task=download" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
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
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/tedCurrentFilesReport/", params = { "action=downloadAll",
			"task=downloadAll" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("tedCurrentFilesForm") TedCurrentFilesForm form,
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
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

}
