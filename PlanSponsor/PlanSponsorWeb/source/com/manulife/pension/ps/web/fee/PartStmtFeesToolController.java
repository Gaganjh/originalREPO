package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.util.PartStmtFeesContent;
import com.manulife.pension.ps.web.fee.util.PartStmtFeesDescription;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWPartStmt;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fee.util.exception.NoStatementEndDatesException;
import com.manulife.pension.service.fee.util.exception.ParticipantNotFoundException;
import com.manulife.pension.service.fee.valueobject.PartStmtFees.RequestStatus;
import com.manulife.pension.service.fee.valueobject.PartStmtFeesDetailData;
import com.manulife.pension.service.fee.valueobject.PartStmtFeesSummaryData;
import com.manulife.pension.service.fee.valueobject.Participant;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.SSNRender;

/**
 * This is action class is used to creating a Fees Reconciliation tool.
 * 
 * @author Eswar
 * 
 */
@Controller
@RequestMapping(value = "/fee")
@SessionAttributes({"partStmtFeesToolForm"})
public class PartStmtFeesToolController extends ReportController {

	@ModelAttribute("partStmtFeesToolForm")
	public PartStmtFeesToolForm populateForm() {
		return new PartStmtFeesToolForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("partStmtFeesTool","/fee/partStmtFeesTool.jsp");
	}

	private static final String PARTSTMTFEESTOOL = "partStmtFeesTool";
	private static final String BLANK = " ";
	private static final DateFormat DATE_yyyymmdd = new SimpleDateFormat("yyyyMMdd");
	private static final DateFormat DATE_MMMDDYYYY = new SimpleDateFormat("MMM/dd/yyyy");
	private static final DateFormat DATE_MMDDYYYY = new SimpleDateFormat("MM/dd/yyyy");
	private static final String PPT_REPORT = "PPT";
	private static final String SUMMARY_INFO_LEVEL = "Summary";
	private static final String PARTY_INFO_LEVEL = "Party";
	private static final String CONTRACT = "Contract number";
	private static final String STATEMENT_PERIOD = "Statement period end date";
	private static final String REPORT_DATE = "Report date";
	private static final String LEGEND = "Legend";

	private static final String SUMMARY_INFORMATION_LEVEL = "Summary information level";
	private static final String PARTY_INFORMATION_LEVEL = "Party information level";
	private static final String DETAILED_INFORMATION_LEVEL = "Detailed information level";

	private static final String CONTRACT_SUMMARY_INFORMATION_LEVEL = "contractSummary";
	private static final String CONTRACT_PARTY_INFORMATION_LEVEL = "contractParty";
	private static final String CONTRACT_DETAILED_INFORMATION_LEVEL = "contractDetailed";

	private static final String GENERAL_ADMINISTRATIVE_CHARGES = "General administrative charges:";
	private static final String INDIVIDUAL_CHARGES = "Individual charges:";
	private static final String CONTRACT_GENERAL_ADMINISTRATIVE_CHARGES = "General administrative charges";
	private static final String CONTRACT_INDIVIDUAL_CHARGES = "Individual charges";
	private static final String LAST_NAME = "Last name";
	private static final String FIRST_NAME = "First name";
	private static final String SSN = "SSN";
	private static final String UNAVAILABLE = "Unavailable";
	private static final String TOTAL = "Total";
	private static final String TOTAL_PARTICIPANT_FEES = "totalParticipantFees";
	private static final String PARTICIPANT_REPORT_NAME = "_participant_statement_report_";
	private static final String CONTRACT_REPORT_NAME = "_contract_statement_report_";

	/**
	 * constructor
	 */
	public PartStmtFeesToolController() {
		super(PartStmtFeesToolController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
	 */
	@Override
	protected String getDefaultSortDirection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	@Override
	protected String getReportName() {
		return null;
	}

	/**
	 * This method is used to get CMA value.
	 * 
	 * @param key
	 * 
	 * @param location
	 * 
	 * @return String
	 */
	private String getContent(int key, Location location) {
		return ContentHelper.getContentText(key, ContentTypeManager.instance().MISCELLANEOUS, location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#execute()
	 */
	/*
	 * TODo need to handle it differently
	 * 
	 * @RequestMapping(value ="/partStmtFeesTo" , method =
	 * {RequestMethod.POST,RequestMethod.GET}) public String
	 * execute(@Valid @ModelAttribute("partStmtFeesToolForm")
	 * PartStmtFeesToolForm actionForm, BindingResult
	 * bindingResult,HttpServletRequest request,HttpServletResponse response)
	 * throws IOException,ServletException, SystemException {
	 * 
	 * if ("POST".equalsIgnoreCase(request.getMethod())) { String mappedPath =
	 * new UrlPathHelper().getPathWithinApplication(request) ; String forward =
	 * new UrlPathHelper().getPathWithinApplication(request); if
	 * (logger.isDebugEnabled()) { logger.debug("forward = " + forward); }
	 * return forward; }
	 * 
	 * return super.execute( actionForm, request, response); }
	 */
	
	@RequestMapping(value = "/partStmtFeesTool/", method = { RequestMethod.GET, RequestMethod.POST })
	public String doDefault(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=save" }, method = { RequestMethod.POST })
	public String doSave(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=filter" }, method = { RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doFilter(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=page" }, method = { RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doPage(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doSort(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=download" }, method = { RequestMethod.POST })
	public String doDownload(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doDownload(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=downloadAll" }, method = { RequestMethod.POST })
	public String doDownloadAll(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}else{
                return forwards.get(PARTSTMTFEESTOOL); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doDownload(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This method basically performs the following tasks:
	 * <ol>
	 * <li>Populates a report action form to its default state.
	 * (PartStmtFeesToolForm)</li>
	 * <li>If validateForm is true, invoke the validate() method on the action
	 * form.</li>
	 * <li>Populates a report criteria based on the action form.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * 
	 */

	public String doCommon(BaseReportForm reportForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		PartStmtFeesToolForm form = (PartStmtFeesToolForm) reportForm;
		if (form.getReportType() == null) {
			form.setReportType(PPT_REPORT);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon");
		}
		// return mapping.findForward(PARTSTMTFEESTOOL);
		return PARTSTMTFEESTOOL;
	}

	/**
	 * This method is used to get Participant Statement Period EndDates
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=search" }, method = { RequestMethod.POST })
	public String doSearch(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSearch");
		}
		PartStmtFeesSummaryData summary = null;
		// PartStmtFeesToolForm form = (PartStmtFeesToolForm)reportForm;
		form.setSearchResultAllowed(false);
		form.setStatementEndDatesAvailable(true);
		String ssn = form.getSsn();
		String reportType = form.getReportType();
		form.setSelectedStmtDate("");
		form.setDownloadAllowed(false);
		Collection<GenericException> errors = new ArrayList<GenericException>();

		if (StringUtils.equals(reportType, PPT_REPORT)) {
			SsnRule.getInstance().validate(SSN, errors, ssn);
			if (CollectionUtils.isNotEmpty(errors)) {
				SessionHelper.setErrorsInSession(request, errors);
				// return mapping.findForward(PARTSTMTFEESTOOL);
				return forwards.get(PARTSTMTFEESTOOL);
			}
		}
		Participant participant = null;
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		if (StringUtils.equals(reportType, PPT_REPORT)) {
			try {
				participant = getFeeService().validateParticipant(contractNumber, ssn);
			} catch (ParticipantNotFoundException e) {
				errors.add(new GenericException(ErrorCodes.PPT_NO_PARTICIPANT));
				SessionHelper.setErrorsInSession(request, errors);
				// return mapping.findForward(PARTSTMTFEESTOOL);
				return forwards.get(PARTSTMTFEESTOOL);
			}
			StringBuffer name = new StringBuffer();
			name.append(participant.getLastName()).append(COMMA).append(BLANK).append(participant.getFirstName());
			form.setName(name.toString());
			form.setFirstName(participant.getFirstName());
			form.setLastName(participant.getLastName());
		}

		try {
			BigDecimal participantId = participant != null ? participant.getParticipantId() : BigDecimal.ZERO;
			form.setParticipantId(participantId);
			summary = getFeeService().getPartStmtPeriodEndDates(contractNumber, participantId, reportType);
		} catch (NoStatementEndDatesException e) {
			if (StringUtils.equals(reportType, PPT_REPORT)) {
				form.setStatementEndDatesAvailable(false);
				form.setSearchResultAllowed(true);
			}
			errors.add(new GenericException(ErrorCodes.PPT_NO_STATEMENT));
			SessionHelper.setErrorsInSession(request, errors);
			// return mapping.findForward(PARTSTMTFEESTOOL);
			return forwards.get(PARTSTMTFEESTOOL);
		}
		form.setStmtPeriodEndDates(summary.getStmtEndDates());
		form.setSearchResultAllowed(true);
		form.setInfoLevel(PARTY_INFO_LEVEL);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSearch");
		}
		// return mapping.findForward(PARTSTMTFEESTOOL);
		return forwards.get(PARTSTMTFEESTOOL);
	}

	/**
	 * This method is used to reset the from data.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=reset" }, method = { RequestMethod.GET })
	public String doReset(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}
		// PartStmtFeesToolForm form = (PartStmtFeesToolForm)reportForm;
		form.setSsn1(StringUtils.EMPTY);
		form.setSsn2(StringUtils.EMPTY);
		form.setSsn3(StringUtils.EMPTY);
		form.setReportType(PPT_REPORT);
		form.setParticipantId(BigDecimal.ZERO);
		form.setSearchResultAllowed(false);
		form.setDownloadAllowed(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doReset");
		}
		// return mapping.findForward(PARTSTMTFEESTOOL);
		return PARTSTMTFEESTOOL;
	}

	/**
	 * This method is used to get Participant statement Report and Contract
	 * statement Report information
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value = "/partStmtFeesTool/", params = { "task=submit" }, method = { RequestMethod.POST })
	public String doSubmit(@Valid @ModelAttribute("partStmtFeesToolForm") PartStmtFeesToolForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get(PARTSTMTFEESTOOL);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSubmit");
		}
		// PartStmtFeesToolForm form = (PartStmtFeesToolForm)reportForm;
		String reportType = form.getReportType();
		form.setDownloadAllowed(false);
		form.setOpsInvestgation(false);
		form.setRequestSuccess(false);
		String siteMode = CommonEnvironment.getInstance().getSiteLocation();
		Location location = StringUtils.equals(siteMode, Constants.SITEMODE_USA) ? Location.US : Location.NEW_YORK;
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		BigDecimal participantId = form.getParticipantId();
		Date statementPeriodEndDate = new Date(Long.parseLong(form.getSelectedStmtDate()));
		PartStmtFeesSummaryData data = getFeeService().getPartStmtFees(contractNumber, participantId,
				statementPeriodEndDate);
		Date reportDate = data.getApolloRequestProcessedDate();
		List<PartStmtFeesDetailData> listData = data.getStmtFees();
		// if request in progress
		if (StringUtils.equals(RequestStatus.IN_PROGRESS.getCode(), data.getRequestStatus().getCode())) {
			form.setRequestStatus(getContent(ContentConstants.PPT_IN_PROGRESS, location));
		} else {
			// if Participant request in OPS investigation
			if (StringUtils.equals(reportType, PPT_REPORT)) {
				if (StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(), data.getRequestStatus().getCode())) {
					form.setOpsInvestgation(true);
					if (userProfile.getRole() instanceof BundledGaCAR || (userProfile.getRole() instanceof TeamLead)
							|| userProfile.isSuperCar()) {
						form.setRequestStatus(getContent(ContentConstants.PPT_OPS_INVESTIGATION_DOWNLOAD, location));
					} else {
						form.setRequestStatus(getContent(ContentConstants.PPT_OPS_INVESTIGATION, location));
					}

				} else {
					form.setRequestSuccess(true);
					form.setRequestStatus(getContent(ContentConstants.PPT_REQUEST_SUCCESS, location));
				}

			} else {
				// if contract request in OPS investigation
				if (StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(), data.getRequestStatus().getCode())) {
					form.setOpsInvestgation(true);
					Object[] dateParam = null;
					synchronized (reportDate) {
						dateParam = new Object[] { data.getOpsInvestigationCount(), listData.size(),
								DATE_MMDDYYYY.format(reportDate) };
					}

					String requestSuccess = null;
					if (userProfile.getRole() instanceof BundledGaCAR || (userProfile.getRole() instanceof TeamLead)
							|| userProfile.isSuperCar()) {
						requestSuccess = StringUtility.substituteParams(
								getContent(ContentConstants.CONTRACT_OPS_INVESTIGATION_DOWNLOAD, location), dateParam);
					} else {
						requestSuccess = StringUtility.substituteParams(
								getContent(ContentConstants.CONTRACT_OPS_INVESTIGATION, location), dateParam);
					}
					form.setRequestStatus(requestSuccess);
				} else {
					form.setRequestSuccess(true);
					form.setRequestStatus(getContent(ContentConstants.PPT_CONTRACT_REQUEST_SUCCESS, location));
				}
			}
		}
		form.setPartStmtFeesSummaryData(data);
		form.setDownloadAllowed(true);
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSubmit");
		}
		// return mapping.findForward(PARTSTMTFEESTOOL);
		return forwards.get(PARTSTMTFEESTOOL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.report.BaseReportAction#getDownloadData
	 * (com.manulife.pension.platform.web.report.BaseReportForm,
	 * com.manulife.pension.service.report.valueobject.ReportData,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		StringBuffer buffer = null;
		PartStmtFeesToolForm form = (PartStmtFeesToolForm) reportForm;
		String reportType = form.getReportType();
		String infoLevel = form.getInfoLevel();
		Date selectedStmtDate = new Date(Long.parseLong(form.getSelectedStmtDate()));
		PartStmtFeesSummaryData data = form.getPartStmtFeesSummaryData();
		Date reportDate = data.getApolloRequestProcessedDate();
		String partStmtDate = null;
		String reportDisplayDate = null;
		synchronized (selectedStmtDate) {
			partStmtDate = DATE_MMMDDYYYY.format(selectedStmtDate);
		}
		synchronized (reportDate) {
			reportDisplayDate = DATE_MMMDDYYYY.format(reportDate);
		}

		if (StringUtils.equals(reportType, PPT_REPORT)) {
			buffer = getParticipantSummaryReport(data, infoLevel, partStmtDate, reportDisplayDate, request, form);
		} else {
			buffer = getContractReport(data, infoLevel, partStmtDate, reportDisplayDate, request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> getDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.report.BaseReportAction#getFileName(com
	 * .manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String getFileName(final BaseReportForm reportForm, final HttpServletRequest request) {

		PartStmtFeesToolForm form = (PartStmtFeesToolForm) reportForm;
		String reportType = form.getReportType();
		Date selectedStmtDate = new Date(Long.parseLong(form.getSelectedStmtDate()));
		String partStmtDate = null;
		synchronized (selectedStmtDate) {
			partStmtDate = DATE_yyyymmdd.format(selectedStmtDate);
		}
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		StringBuffer name = new StringBuffer();
		name.append(contractNumber);
		if (StringUtils.equals(reportType, PPT_REPORT)) {
			name.append(PARTICIPANT_REPORT_NAME);
		} else {
			name.append(CONTRACT_REPORT_NAME);
		}
		name.append(partStmtDate);
		name.append(CSV_EXTENSION);
		return name.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria()
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

	}

	/**
	 * This method is used to get fee service
	 * 
	 * @return fee Service
	 */
	private FeeServiceDelegate getFeeService() {
		return FeeServiceDelegate.getInstance(Environment.getInstance().getAppId());
	}

	/**
	 * This method is used to create Participant Report
	 * 
	 * @param data
	 * @param infoLevel
	 * @param statementEndDate
	 * @param reportDate
	 * @param request
	 * @param form
	 * 
	 * @return report
	 * 
	 * @throws SystemException
	 */
	private StringBuffer getParticipantSummaryReport(PartStmtFeesSummaryData data, String infoLevel,
			String statementEndDate, String reportDate, HttpServletRequest request, PartStmtFeesToolForm form)
			throws SystemException {
		StringBuffer buffer = new StringBuffer();
		List<String> gacContent = null;
		String reportDescription = null;
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		Location location = CommonConstants.COMPANY_ID_US.equalsIgnoreCase(currentContract.getCompanyCode())
				? Location.US : Location.NEW_YORK;
		buffer.append(CONTRACT).append(COMMA).append(contractNumber).append(LINE_BREAK)
				.append(getCsvString(currentContract.getCompanyName())).append(LINE_BREAK).append(STATEMENT_PERIOD)
				.append(COMMA).append(statementEndDate).append(LINE_BREAK).append(REPORT_DATE).append(COMMA)
				.append(reportDate).append(LINE_BREAK);

		if (infoLevel.equalsIgnoreCase(SUMMARY_INFO_LEVEL)) {
			gacContent = PartStmtFeesContent.getInstance().getParticipantSummaryGACList();
			reportDescription = SUMMARY_INFORMATION_LEVEL;
		} else if (infoLevel.equalsIgnoreCase(PARTY_INFO_LEVEL)) {
			gacContent = PartStmtFeesContent.getInstance().getParticipantPartyGACList();
			reportDescription = PARTY_INFORMATION_LEVEL;
		} else {
			gacContent = PartStmtFeesContent.getInstance().getParticipantDetailGACList();
			reportDescription = DETAILED_INFORMATION_LEVEL;
		}
		if (!infoLevel.equalsIgnoreCase(PARTY_INFO_LEVEL)) {
			buffer.append(reportDescription).append(LINE_BREAK);
		}

		buffer.append(LINE_BREAK).append(LAST_NAME).append(COMMA).append(FIRST_NAME).append(COMMA).append(LINE_BREAK)
				.append(getCsvString(form.getLastName())).append(COMMA).append(getCsvString(form.getFirstName()))
				.append(COMMA).append(LINE_BREAK);

		boolean noData = StringUtils.equals(RequestStatus.NO_DATA.getCode(), data.getRequestStatus().getCode());
		if (!noData) {
			boolean administrativeChargesAvailable = false;
			boolean individualChargesAvailable = false;
			boolean otherChargesAvailable = false;
			boolean isOpsInvestigation = StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(),
					data.getRequestStatus().getCode());
			List<PartStmtFeesDetailData> listData = data.getStmtFees();
			Map<String, PartStmtFeesDescription> content = PartStmtFeesContent.getInstance().getFeeContent();
			PartStmtFeesDetailData feeData = listData.get(0);
			List<String> opsList = PartStmtFeesContent.getInstance().getOpsInvestigation();
			List<String> individualChargesContent = PartStmtFeesContent.getInstance().getIndividualChargesList();
			List<String> otherChargesContent = PartStmtFeesContent.getInstance().getOtherChargesList();
			Class<PartStmtFeesDetailData> clasz = PartStmtFeesDetailData.class;
			BigDecimal fee = null;
			// check whether administrative Charges are Available
			if (isOpsInvestigation) {
				administrativeChargesAvailable = true;
			} else {
				for (String item : gacContent) {
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						administrativeChargesAvailable = true;
						break;
					}
				}
			}

			// check whether individual Charges are Available
			for (String item : individualChargesContent) {
				fee = getFee(getMethodName(item), feeData, clasz);
				if (!isAmountZero(fee)) {
					individualChargesAvailable = true;
					break;
				}
			}

			// check whether individual Charges are Available
			for (String item : otherChargesContent) {
				fee = getFee(getMethodName(item), feeData, clasz);
				if (!isAmountZero(fee)) {
					otherChargesAvailable = true;
					break;
				}
			}

			// Display General administrative charges
			if (administrativeChargesAvailable) {
				buffer.append(LINE_BREAK);
				buffer.append(GENERAL_ADMINISTRATIVE_CHARGES).append(LINE_BREAK);
				if (isOpsInvestigation) {
					for (String item : gacContent) {
						if (opsList.contains(item)) {
							buffer.append(content.get(item).getLongDescription()).append(COMMA);
							buffer.append(UNAVAILABLE).append(LINE_BREAK);
						} else {
							fee = getFee(getMethodName(item), feeData, clasz);
							if (!isAmountZero(fee)) {
								buffer.append(content.get(item).getLongDescription()).append(COMMA);
								buffer.append(displayAmount(fee)).append(LINE_BREAK);
							}
						}
					}
				} else {
					for (String item : gacContent) {
						fee = getFee(getMethodName(item), feeData, clasz);
						if (!isAmountZero(fee)) {
							buffer.append(content.get(item).getLongDescription()).append(COMMA);
							buffer.append(displayAmount(fee)).append(LINE_BREAK);
						}
					}

				}
			}

			// Display Individual Charges
			if (individualChargesAvailable) {
				buffer.append(LINE_BREAK);
				buffer.append(INDIVIDUAL_CHARGES).append(LINE_BREAK);
				for (String item : individualChargesContent) {
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						buffer.append(content.get(item).getLongDescription()).append(COMMA);
						buffer.append(displayAmount(fee)).append(LINE_BREAK);
					}
				}
			}

			// Other charges Credit and Plan Credit
			if (otherChargesAvailable) {
				buffer.append(LINE_BREAK);
				if (!individualChargesAvailable) {
					buffer.append(INDIVIDUAL_CHARGES).append(LINE_BREAK);
				}
				for (String item : otherChargesContent) {
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						buffer.append(content.get(item).getLongDescription()).append(COMMA);
						buffer.append(displayAmount(fee)).append(LINE_BREAK);
					}
				}
			}
			// Pt Total
			buffer.append(LINE_BREAK);
			buffer.append(TOTAL).append(COMMA)
					.append(displayAmount(getFee(getMethodName(TOTAL_PARTICIPANT_FEES), feeData, clasz)))
					.append(LINE_BREAK);
			// Disclaimer
			buffer.append(LINE_BREAK);
			buffer.append(getCsvString(getDisclaimer(ContentConstants.PPT_DISCLAIMER, location))).append(LINE_BREAK);
		} else {
			// No data Disclaimer
			buffer.append(LINE_BREAK);
			buffer.append(getCsvString(getDisclaimer(ContentConstants.PPT_DISCLAIMER_NO_DATA, location)))
					.append(LINE_BREAK);
		}
		return buffer;
	}

	/**
	 * This method is used to create Contract Report
	 * 
	 * @param data
	 * @param infoLevel
	 * @param statementEndDate
	 * @param reportDate
	 * @param request
	 * 
	 * @return Report
	 * 
	 * @throws SystemException
	 */
	private StringBuffer getContractReport(PartStmtFeesSummaryData data, String infoLevel, String statementEndDate,
			String reportDate, HttpServletRequest request) throws SystemException {
		Participant ppt = null;
		String reportDescription = null;
		List<String> gacList = null;
		List<String> reportLegend = null;
		StringBuffer buffer = new StringBuffer();
		List<PartStmtFeesDetailData> feeDataList = data.getStmtFees();
		Map<String, PartStmtFeesDescription> content = PartStmtFeesContent.getInstance().getFeeContent();
		List<String> individualChargesList = PartStmtFeesContent.getInstance().getIndividualChargesList();
		List<String> otherChargesList = PartStmtFeesContent.getInstance().getOtherChargesList();
		List<String> opsList = PartStmtFeesContent.getInstance().getOpsInvestigation();
		UserProfile userProfile = getUserProfile(request);

		if (infoLevel.equalsIgnoreCase(SUMMARY_INFO_LEVEL)) {
			gacList = PartStmtFeesContent.getInstance().getContractSummaryGACList();
			reportLegend = PartStmtFeesContent.getInstance().getSummaryReportLegend();
			reportDescription = content.get(CONTRACT_SUMMARY_INFORMATION_LEVEL).getLongDescription();
		} else if (infoLevel.equalsIgnoreCase(PARTY_INFO_LEVEL)) {
			gacList = PartStmtFeesContent.getInstance().getContractPartyGACList();
			reportDescription = content.get(CONTRACT_PARTY_INFORMATION_LEVEL).getLongDescription();
		} else {
			gacList = PartStmtFeesContent.getInstance().getContractDetailGACList();
			reportLegend = PartStmtFeesContent.getInstance().getDetailReportLegend();
			reportDescription = content.get(CONTRACT_DETAILED_INFORMATION_LEVEL).getLongDescription();
		}

		Contract currentContract = userProfile.getCurrentContract();
		int contractNumber = currentContract.getContractNumber();
		Location location = CommonConstants.COMPANY_ID_US.equalsIgnoreCase(currentContract.getCompanyCode())
				? Location.US : Location.NEW_YORK;

		boolean noData = StringUtils.equals(RequestStatus.NO_DATA.getCode(), data.getRequestStatus().getCode());

		buffer.append(CONTRACT).append(COMMA).append(contractNumber).append(LINE_BREAK)
				.append(getCsvString(currentContract.getCompanyName())).append(LINE_BREAK).append(STATEMENT_PERIOD)
				.append(COMMA).append(statementEndDate).append(LINE_BREAK).append(REPORT_DATE).append(COMMA)
				.append(reportDate).append(LINE_BREAK).append(getCsvString(reportDescription)).append(LINE_BREAK)
				.append(LINE_BREAK);

		if (!noData) {
			// calculate fee total
			Class<PartStmtFeesDetailData> clasz = PartStmtFeesDetailData.class;
			List<String> newGacList = new ArrayList<String>();
			List<String> newIndividualChargesList = new ArrayList<String>();
			List<String> newOtherChargesList = new ArrayList<String>();
			Map<String, Double> feesTotal = new HashMap<String, Double>();
			double total = 0;
			BigDecimal fee = null;
			NextItem: for (String item : gacList) {
				total = 0;
				fee = BigDecimal.ZERO;
				boolean isFeeAvailable = false;
				for (PartStmtFeesDetailData feeData : feeDataList) {
					if (StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(),
							feeData.getRequestStatus().getCode())) {
						if (opsList.contains(item)) {
							newGacList.add(item);
							continue NextItem;
						}
					}
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						isFeeAvailable = true;
					}
					total = total + fee.doubleValue();
				}
				if (isFeeAvailable) {
					newGacList.add(item);
					feesTotal.put(item, total);
				}
			}

			for (String item : individualChargesList) {
				total = 0;
				fee = BigDecimal.ZERO;
				boolean isFeeAvailable = false;
				for (PartStmtFeesDetailData feeData : feeDataList) {
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						isFeeAvailable = true;
					}
					total = total + fee.doubleValue();
				}
				if (isFeeAvailable) {
					newIndividualChargesList.add(item);
					feesTotal.put(item, total);
				}
			}

			// Other charger
			for (String item : otherChargesList) {
				total = 0;
				fee = BigDecimal.ZERO;
				boolean isFeeAvailable = false;
				for (PartStmtFeesDetailData feeData : feeDataList) {
					fee = getFee(getMethodName(item), feeData, clasz);
					if (!isAmountZero(fee)) {
						isFeeAvailable = true;
					}
					total = total + fee.doubleValue();
				}
				if (isFeeAvailable) {
					newOtherChargesList.add(item);
					feesTotal.put(item, total);
				}
			}

			// Legend
			if (!infoLevel.equalsIgnoreCase(PARTY_INFO_LEVEL)) {
				buffer.append(LEGEND).append(LINE_BREAK);
				for (String legend : reportLegend) {
					buffer.append(content.get(legend).getShortDescription()).append(COMMA)
							.append(content.get(legend).getLongDescription()).append(COMMA).append(LINE_BREAK);
				}
				buffer.append(LINE_BREAK);
			}

			// Group header
			buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA);
			int gacCount = newGacList.size();
			if (gacCount != 1) {
				buffer.append(CONTRACT_GENERAL_ADMINISTRATIVE_CHARGES);
				for (int i = 1; i < gacCount; i++) {
					buffer.append(COMMA);
				}
			}
			int individualChargesCount = newIndividualChargesList.size();
			int otherChargesCount = newOtherChargesList.size();
			if (individualChargesCount != 0 || otherChargesCount != 0) {
				buffer.append(CONTRACT_INDIVIDUAL_CHARGES).append(COMMA);
			}
			buffer.append(LINE_BREAK);

			// table header
			buffer.append(LAST_NAME).append(COMMA).append(FIRST_NAME).append(COMMA).append(SSN).append(COMMA);
			// GAC Headers
			for (String item : newGacList) {
				buffer.append(content.get(item).getShortDescription()).append(COMMA);
			}
			// IndividualCharges Headers
			for (String item : newIndividualChargesList) {
				buffer.append(content.get(item).getShortDescription()).append(COMMA);
			}
			// OtherCharges Headers
			for (String item : newOtherChargesList) {
				buffer.append(content.get(item).getShortDescription()).append(COMMA);
			}
			buffer.append(LINE_BREAK);

			// Participant charges
			for (PartStmtFeesDetailData feeData : feeDataList) {
				ppt = feeData.getParticipant();
				buffer.append(getCsvString(ppt.getLastName())).append(COMMA).append(getCsvString(ppt.getFirstName()))
						.append(COMMA).append(SSNRender.format(ppt.getSsn(), null, true)).append(COMMA);

				// General administrative charges
				boolean isOpsInvestigation = StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(),
						feeData.getRequestStatus().getCode());
				if (isOpsInvestigation) {
					for (String item : newGacList) {
						if (opsList.contains(item)) {
							buffer.append(UNAVAILABLE).append(COMMA);
						} else {
							buffer.append(displayAmount(getFee(getMethodName(item), feeData, clasz))).append(COMMA);
						}
					}
				} else {
					for (String item : newGacList) {
						buffer.append(displayAmount(getFee(getMethodName(item), feeData, clasz))).append(COMMA);
					}
				}
				// Individual Charges
				for (String item : newIndividualChargesList) {
					buffer.append(displayAmount(getFee(getMethodName(item), feeData, clasz))).append(COMMA);
				}

				// OtherCharges Charges
				for (String item : newOtherChargesList) {
					buffer.append(displayAmount(getFee(getMethodName(item), feeData, clasz))).append(COMMA);
				}
				buffer.append(LINE_BREAK);
			}
			buffer.append(LINE_BREAK);

			// Total
			buffer.append(TOTAL).append(COMMA).append(COMMA).append(COMMA);
			boolean isOpsInvestigation = StringUtils.equals(RequestStatus.OPS_INVESTIGATION.getCode(),
					data.getRequestStatus().getCode());
			if (isOpsInvestigation) {
				for (String item : newGacList) {
					if (opsList.contains(item)) {
						buffer.append(UNAVAILABLE).append(COMMA);
					} else {
						buffer.append(displayAmount(feesTotal.get(item))).append(COMMA);
					}
				}
			} else {
				for (String item : newGacList) {
					buffer.append(displayAmount(feesTotal.get(item))).append(COMMA);
				}
			}

			for (String item : newIndividualChargesList) {
				buffer.append(displayAmount(feesTotal.get(item))).append(COMMA);
			}

			for (String item : newOtherChargesList) {
				buffer.append(displayAmount(feesTotal.get(item))).append(COMMA);
			}
			buffer.append(LINE_BREAK).append(LINE_BREAK);
			// Disclaimer
			buffer.append(getCsvString(getDisclaimer(ContentConstants.PPT_DISCLAIMER, location))).append(LINE_BREAK);
		} else {
			// No data Disclaimer
			buffer.append(getCsvString(getDisclaimer(ContentConstants.PPT_DISCLAIMER_NO_DATA, location)))
					.append(LINE_BREAK);
		}
		return buffer;
	}

	/**
	 * This is used to get Disclaimer of the report.
	 * 
	 * @param cmaKey
	 * @param location
	 * 
	 * @return String
	 */
	private String getDisclaimer(int cmaKey, Location location) {
		return ContentHelper.getContentText(cmaKey, ContentTypeManager.instance().MISCELLANEOUS, location);
	}

	/**
	 * This is used to construct the method name
	 * 
	 * @param value
	 * @return String
	 */
	private static String getMethodName(String value) {

		return "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	/**
	 * This method is used to invoke methods of PartStmtFeesDetailData using
	 * reflection.
	 * 
	 * @param methodName
	 * @param vo
	 * @param clasz
	 * 
	 * @return fee
	 * 
	 * @throws SystemException
	 */
	private BigDecimal getFee(String methodName, PartStmtFeesDetailData vo, Class<PartStmtFeesDetailData> clasz)
			throws SystemException {

		BigDecimal fee = null;
		try {
			Method method = clasz.getDeclaredMethod(methodName);
			fee = (BigDecimal) method.invoke(vo, new Object[] {});
		} catch (NoSuchMethodException e) {
			throw new SystemException("This method is not exist" + methodName);
		} catch (InvocationTargetException e) {
			throw new SystemException("Invocation exception " + e);
		} catch (IllegalAccessException e) {
			throw new SystemException("This method does not having access" + methodName);
		}
		return fee;
	}

	/**
	 * Check if BigDecimal value is zero.
	 * 
	 * @param value
	 * 
	 * @return boolean
	 */
	private boolean isAmountZero(BigDecimal value) {
		if (value.compareTo(new BigDecimal(0)) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method is used to format the value.
	 * 
	 * @param value
	 * 
	 * @return String
	 */
	private String displayAmount(Object value) {
		String temp = NumberRender.formatByPattern(value, ZERO_AMOUNT_STRING, "#########.##;-#########.##", 2,
				BigDecimal.ROUND_HALF_UP);
		int posDec = temp.indexOf(".");
		StringBuffer displayValue = new StringBuffer(temp);

		if (posDec == -1) {
			displayValue.append(".00");
		} else {
			String decPart = temp.substring(posDec + 1);
			if (decPart != null && decPart.length() < 2) {
				for (int i = decPart.length(); i < 2; i++) {
					displayValue.append("0");
				}
			}
		}

		return displayValue.toString();
	}

	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */

	@Autowired
	private PSValidatorFWPartStmt psValidatorFWPartStmt;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWPartStmt);
	}

}
