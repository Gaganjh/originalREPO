package com.manulife.pension.ps.web.transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.EmployeeChangeHistoryACISettingsItem;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsACIReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.util.render.SSNRender;

/**
 * In support of transaction history Change EZincrease settings.
 * 
 * @author Glen Lalonde
 */
@Controller
@RequestMapping(value = "/transaction")
@SessionAttributes({"aciChangeDetailsForm"})

public class AciReportController extends ReportController {

	@ModelAttribute(" aciChangeDetailsForm")
	public AciReportForm populateForm() {
		return new AciReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/transaction/aciReport.jsp");
		forwards.put("default", "/transaction/aciReport.jsp");
		forwards.put("sort", "/transaction/aciReport.jsp");
		forwards.put("filter", "/transaction/aciReport.jsp");
		forwards.put("page", "/transaction/aciReport.jsp");
		forwards.put("print", "/transaction/aciRepor.jsp");
	}

	private static String DOWNLOAD_REPORT_NAME = "EZincreaseservicechangesReport";
	private static final String TRANSACTION_HISTORY = "TH";

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TransactionDetailsACIReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TransactionDetailsACIReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "lastName";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateReportCriteria");

		AciReportForm dcForm = (AciReportForm) form;

		int contractNumber = getUserProfile(request).getCurrentContract().getContractNumber();

		criteria.addFilter(TransactionDetailsACIReportData.FILTER_CONTRACTID, new Integer(contractNumber));
		criteria.addFilter(TransactionDetailsACIReportData.FILTER_PROFILE_ID, dcForm.getProfileId());
		criteria.addFilter(TransactionDetailsACIReportData.FILTER_TARGET_DATE, dcForm.getTransactionDate());
		criteria.addFilter(TransactionDetailsACIReportData.FILTER_SCREEN, TRANSACTION_HISTORY);
		criteria.addFilter(TransactionDetailsACIReportData.FILTER_APPLICATION_ID,
				TransactionDetailsACIReportData.PSW_APPLICATION_ID);

		if (getUserProfile(request).isInternalUser() == false) {
			criteria.addFilter(TransactionDetailsACIReportData.FILTER_EXTERNAL_USER_VIEW, "Y");
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <-  populateReportCriteria");
	}

	protected String getFileName(HttpServletRequest request) {
		String fileName = DOWNLOAD_REPORT_NAME + CSV_EXTENSION;

		return fileName.replaceAll("\\ ", "_"); // Replace spaces with underscores
	}

	/**
	 * Called by framework to gen excel (csv) data
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		// SSE S024 determine wheather the ssn should be masked on the csv report
		boolean maskSSN = true;// set the mask ssn flag to true as a default
		UserProfile user = getUserProfile(request);
		try {
			maskSSN = ReportDownloadHelper.isMaskedSsn(user, user.getCurrentContract().getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}
		StringBuffer buffer = new StringBuffer();

		AciReportForm aciForm = (AciReportForm) reportForm;
		ParticipantAccountDetailsVO detailsVO = (ParticipantAccountDetailsVO) request.getAttribute("details");

		buffer.append("JH EZincrease service change summary").append(LINE_BREAK);
		buffer.append("Transaction type").append(COMMA).append("Deferral update").append(LINE_BREAK);
		buffer.append("Name").append(COMMA)
				.append(escapeField(detailsVO.getLastName() + ", " + detailsVO.getFirstName())).append(LINE_BREAK);
		buffer.append("SSN").append(COMMA).append(SSNRender.format(detailsVO.getSsn(), "", maskSSN)).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		buffer.append("Details - " + aciForm.getTransactionDateFormatted()).append(LINE_BREAK);
		buffer.append("Item changed,Value before,Value after,Changed by").append(LINE_BREAK);

		Iterator it = aciForm.getReport().getDetails().iterator();

		while (it.hasNext()) {
			EmployeeChangeHistoryACISettingsItem item = (EmployeeChangeHistoryACISettingsItem) it.next();

			buffer.append(item.getItemChanged()).append(COMMA);
			buffer.append(escapeField(item.getValueBeforeForDownload())).append(COMMA);
			buffer.append(escapeField(item.getValueAfterForDownload())).append(COMMA);
			buffer.append(item.getChangedBy()).append(LINE_BREAK);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	// from ParticipantTransactionHistoryAction, jsp setup to pickup from request.
	public void populateParticipantDetails(String profileId, HttpServletRequest request) throws SystemException {

		ParticipantAccountDetailsVO participantDetailsVO = null;

		UserProfile userProfile = getUserProfile(request);
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		String productId = userProfile.getCurrentContract().getProductId();

		Principal principal = getUserProfile(request).getPrincipal();
		ParticipantAccountVO participantAccountVO = ParticipantServiceDelegate.getInstance()
				.getParticipantAccount(principal, contractNumber, productId, profileId, null, false, false);
		participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();

		request.setAttribute("details", participantDetailsVO); // needed by jsp.
	}

	// don't want excel to think the , is the next field
	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}

	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		ReportData bean = service.getReportData(reportCriteria);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;
	}

	public String doCommon(AciReportForm form,  HttpServletRequest request,
			HttpServletResponse response) throws  SystemException {
		

		if (logger.isDebugEnabled())
			logger.debug("entry -> doCommon");

		String forward = super.doCommon(form, request, response);

		// set values send from user selection on summary screen
		// setup on nav into page, then download link/print just picks up from form
		if (request.getParameter("profileId") != null) {
			form.setProfileId(request.getParameter("profileId"));
			form.setTransactionDate(request.getParameter("transactionDate"));
		}

		TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

		request.setAttribute("enablePrint", Boolean.FALSE);

		populateParticipantDetails(form.getProfileId(), request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}

		return forward;
	}

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
	public String preExecute( AciReportForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		/*if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(PsBaseAction.ERROR_KEY);
			if (errDirect != null) {
				request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if input
																											// default
			}
		}*/

		UserProfile userProfile = SessionHelper.getUserProfile(request);

		// check for selected access
		if (userProfile.isSelectedAccess()) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		// check if contract is discontinued
		if (userProfile.getCurrentContract().isDiscontinued()) {
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// do a refresh so that there's no problem using the back button
			String forward = new UrlPathHelper().getPathWithinApplication(request);
			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward;
		}

		return null;
	}

	@RequestMapping(value = "/aciReport/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																										// default
			}
		}
		 forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/aciReport/", params = {"task=filter"}, method = {RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																										// default
			}
		}
		 forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward ,'/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/aciReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
			}
		}
		 forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/aciReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																										// default
			}
		}
		 forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/aciReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
			}
		}
		 forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward ,'/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/aciReport/", params = { "task=dowanloadAll" }, method = { RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
			}
		}
		 forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/aciReport/", params = {"task=print"}, method = { RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("aciChangeDetailsForm") AciReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			TransactionDetailsACIReportData report = (TransactionDetailsACIReportData) request
					.getAttribute(CommonConstants.REPORT_BEAN);
			try {
				populateParticipantDetails(((AciReportForm) form).getProfileId(), request);
				request.setAttribute("enablePrint", Boolean.FALSE);
				request.setAttribute(CommonConstants.REPORT_BEAN, report);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");// if
																											// default
			}
		}
		 forward = super.doPrint(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}

}
