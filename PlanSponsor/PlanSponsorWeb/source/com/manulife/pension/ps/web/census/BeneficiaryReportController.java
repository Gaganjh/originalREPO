package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.reporthandler.BeneficiaryReportHandler;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignation;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryDesignationData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiaryReportData;
import com.manulife.pension.service.beneficiary.valueobject.BeneficiarySet;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * Action class to perform download beneficiary report activities.
 * 
 * @author Vellaisamy S
 *
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"censusSummaryReportForm"})

public class BeneficiaryReportController extends ReportController {
	
	@ModelAttribute("censusSummaryReportForm")
	public CensusSummaryReportForm populateForm() {
		return new CensusSummaryReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	
	static {
		forwards.put("input","/census/censusSummaryReport.jsp");
		forwards.put("default","/census/censusSummaryReport.jsp");
		forwards.put("download","/census/censusSummaryReport.jsp");
	}

	public static final String DEFAULT_SORT = "lastName";
	public static final String TEMPLATE_REPORT_ID = BeneficiaryReportHandler.class.getName();
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

	/**
	 * Constructor for BeneficiaryReportAction
	 */
	public BeneficiaryReportController() {
		super(BeneficiaryReportController.class);
	}

	/**
	 * Returns the unique report Id for this report.
	 * 
	 * @return TEMPLATE_REPORT_ID
	 */
	@Override
	protected String getReportId() {
		return TEMPLATE_REPORT_ID;
	}

	/**
	 * Returns the unique report name for this report.
	 * 
	 * @return REPORT_NAME
	 */
	@Override
	protected String getReportName() {
		return BeneficiaryReportData.REPORT_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getFileName
	 * (javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String getFileName(HttpServletRequest request) {
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}

		return "Beneficiary_Report_for_" + getUserProfile(request).getCurrentContract().getContractNumber() + "_for_"
				+ dateString + CSV_EXTENSION;
	}

	/**
	 * Returns default sort element of this report.
	 * 
	 * @return DEFAULT_SORT
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}

	/**
	 * Returns default sort direction of this report.
	 * 
	 * @return ASC_DIRECTION
	 */
	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * Method will return the beneficiary download report data.
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		final StopWatch stopWatch = new StopWatch();
		if (logger.isInfoEnabled()) {
			logger.info("getDownloadData - starting timer.");
		}
		stopWatch.start();

		Map<String, String> relationToParticipantMap = new HashMap<String, String>();

		UserProfile user = getUserProfile(request);
		Contract currentContract = user.getCurrentContract();
		MutableContent content = null;
		String legalDisclaimer = "";

		BeneficiaryReportData beneficiaryReportData = (BeneficiaryReportData) report;

		String todayDate = DateRender.formatByPattern(Calendar.getInstance().getTime(), StringUtils.EMPTY,
				RenderConstants.MEDIUM_MDY_SLASHED);

		// Invoke environment Service to get relationship Map
		EnvironmentServiceDelegate environmentService = EnvironmentServiceDelegate.getInstance();
		relationToParticipantMap = environmentService.getRelationshipToParticipant();

		// Getting the legal disclaimer content from Content DB for given content id.
		try {
			// Get the Content information using ContentKey
			content = (MutableContent) BrowseServiceDelegate.getInstance()
					.findContentByKey(Constants.BENEFICIAY_DESIGNATION_LEGAL_TEXT_CONTENT_KEY);
		} catch (ContentException e) {

			logger.error("Unexpected System exception when calling " + "BrowseServiceDelegate.findContentByKey()");

			throw new SystemException("Unexpected System exception when calling "
					+ "BrowseServiceDelegate.findContentByKey() for content key "
					+ Constants.BENEFICIAY_DESIGNATION_LEGAL_TEXT_CONTENT_KEY + " " + e.toString());
		}

		if (content != null) {

			// get the content id based on the location of the company.
			String context = Constants.NY_CONTEXT_TEXT;
			if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA.equals(currentContract.getCompanyCode())) {
				context = Constants.US_CONTEXT_TEXT;
			}
			legalDisclaimer = getLegalDesclimar(content, context);
		}

		boolean divisionAllowed = isDivisionAllowed(currentContract);
		boolean employeeIdAllowed = isEmployeeIdAllowed(currentContract);

		StringBuilder buffer = new StringBuilder();

		buffer.append("Contract");
		buffer.append(COMMA);
		buffer.append(COMMA);
		buffer.append(currentContract.getContractNumber());
		buffer.append(COMMA);
		buffer.append(escapeField(currentContract.getCompanyName()));
		buffer.append(LINE_BREAK);
		buffer.append("Report Name");
		buffer.append(COMMA);
		buffer.append(COMMA);
		buffer.append("Beneficiary Designation");
		buffer.append(LINE_BREAK);
		buffer.append("Report download date");
		buffer.append(COMMA);
		buffer.append(COMMA);
		buffer.append(todayDate);
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(escapeField(legalDisclaimer));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		getHeadingColumn(buffer, currentContract, divisionAllowed, employeeIdAllowed);
		buffer.append(LINE_BREAK);

		boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
		try {
			maskSsnFlag = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
			// log exception and output blank ssn
		}
		if (beneficiaryReportData != null && beneficiaryReportData.getDetails() != null) {
			Iterator iterator = beneficiaryReportData.getDetails().iterator();
			Map<String, BeneficiaryDesignationData> beneficiaryReportMap = beneficiaryReportData
					.getBeneficiaryReportMap();
			List<EmployeeData> employeeDataList = new ArrayList<EmployeeData>();

			while (iterator.hasNext()) {

				EmployeeData employeeData = (EmployeeData) iterator.next();

				String profileId = employeeData.getProfileId().toString();
				BeneficiaryDesignationData beneficiaryDesignationData = beneficiaryReportMap.get(profileId);

				if (beneficiaryDesignationData != null) {

					BeneficiarySet beneficiarySet = beneficiaryDesignationData.getBeneficiarySet();
					List<BeneficiaryDesignation> beneficiaryDesignationList = beneficiaryDesignationData
							.getBeneficiaryDesignationList();

					String lastUpdatedDate = StringUtils.EMPTY;
					String lastUpdateSource = StringUtils.EMPTY;

					if (beneficiarySet != null) {

						lastUpdatedDate = DateRender.formatByPattern(beneficiarySet.getLastUpdatedDate(),
								StringUtils.EMPTY, RenderConstants.MEDIUM_MDY_SLASHED);

						lastUpdateSource = getLastUpdatedSource(beneficiarySet);
					}

					if (beneficiaryDesignationList.size() > 0) {
						for (BeneficiaryDesignation beneficiaryDesignation : beneficiaryDesignationList) {

							buffer.append(lastUpdatedDate).append(COMMA);
							includeEmployeeDetails(buffer, employeeData, maskSsnFlag, divisionAllowed,
									employeeIdAllowed);

							if (BeneficiaryDesignationData.PRIMARY_BENEFICIARY_TYPE_CODE
									.equalsIgnoreCase(beneficiaryDesignation.getBeneficiaryTypeCode().trim())) {
								buffer.append("Primary").append(COMMA);
							} else if (BeneficiaryDesignationData.CONTINGENT_BENEFICIARY_TYPE_CODE
									.equalsIgnoreCase(beneficiaryDesignation.getBeneficiaryTypeCode().trim())) {
								buffer.append("Contingent").append(COMMA);
							}
							String relationshipCode = beneficiaryDesignation.getRelationshipCode() != null
									? beneficiaryDesignation.getRelationshipCode().trim()
									: StringUtils.EMPTY;

							if (!StringUtils.EMPTY.equals(relationshipCode)) {
								String relationDesc = relationToParticipantMap.get(relationshipCode);
								if (BeneficiaryReportData.OTHER_RELATIONSHIP_DESC.equalsIgnoreCase(relationDesc)) {
									buffer.append(escapeField(beneficiaryDesignation.getOtherRelationshipDesc()))
											.append(COMMA);
								} else {
									buffer.append(escapeField(relationDesc)).append(COMMA);
								}
							}
							buffer.append(escapeField(beneficiaryDesignation.getLastName())).append(COMMA);
							buffer.append(escapeField(beneficiaryDesignation.getFirstName())).append(COMMA);

							String sharePct = NumberRender.formatByPattern(beneficiaryDesignation.getSharePct(), null,
									Constants.DECIMAL_PATTERN, 2, BigDecimal.ROUND_HALF_UP);

							buffer.append(sharePct).append(COMMA);
							buffer.append(escapeField(lastUpdateSource)).append(LINE_BREAK);
						}
					} else {

						buffer.append(lastUpdatedDate).append(COMMA);
						includeEmployeeDetails(buffer, employeeData, maskSsnFlag, divisionAllowed, employeeIdAllowed);
						buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
						buffer.append(escapeField(lastUpdateSource)).append(LINE_BREAK);
					}
				} else {
					employeeDataList.add(employeeData);
				}
			}

			if (employeeDataList.size() > 0) {

				for (EmployeeData employeeData : employeeDataList) {

					buffer.append(COMMA);
					includeEmployeeDetails(buffer, employeeData, maskSsnFlag, divisionAllowed, employeeIdAllowed);
					buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
					buffer.append(LINE_BREAK);
				}
			}
		}

		return buffer.toString().getBytes();
	}

	/**
	 * Method used to populate report search criteria for download report.
	 * 
	 * @param criteria
	 * @param form
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		String task = getTask(request);

		// for download task do not process warnings
		if (task.equals(DOWNLOAD_TASK)) {
			criteria.setReportId(TEMPLATE_REPORT_ID);
		}

		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));

		CensusSummaryReportForm psform = (CensusSummaryReportForm) form;

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		// If the task is default then reset the page no and sort details that
		// are cached in eligibility tab and deferral tab.
		if (task.equals(DEFAULT_TASK)) {
			filterCriteriaVo.clearDeferralSortDetails();
			filterCriteriaVo.clearEligibilitySortDetails();
		}

		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateCensusSummaryTabFilterCriteria(task, filterCriteriaVo, psform, criteria);

		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		// if external user, don't display Cancelled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}

	}

	/**
	 * Method used to populate form variable allowedToDownloadBeneficiaryReport and
	 * forward action to next step.
	 * 
	 */
	 protected String doCommon(
			 CensusSummaryReportForm form, HttpServletRequest request,
	            HttpServletResponse response) throws
	            SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		final StopWatch stopWatch = new StopWatch();
		if (logger.isInfoEnabled()) {
			logger.info("doCommon - starting timer.");
		}
		stopWatch.start();

		boolean isOBDSEnabled = false;

		form.setSortField("ssn");
		form.setSortDirection(ReportSort.ASC_DIRECTION);

		String forward = super.doCommon(form, request, response);

		UserProfile userProfile = getUserProfile(request);

		Contract currentContract = userProfile.getCurrentContract();

		if (DOWNLOAD_TASK.equals(getTask(request))) {

			FunctionalLogger.INSTANCE.log("Download beneficiary report", userProfile, getClass(),
					getMethodName(form, request));

		}

		try {

			ContractServiceFeature csf = ContractServiceDelegate.getInstance().getContractServiceFeature(
					currentContract.getContractNumber(), ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);

			isOBDSEnabled = ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();

		} catch (ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}

		form.setAllowedToDownloadBeneficiaryReport(isOBDSEnabled);

		stopWatch.stop();
		if (logger.isInfoEnabled()) {
			logger.info(new StringBuffer("doCommon - stoping timer - time duration [").append(stopWatch.toString())
					.append("]").toString());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}

		return forward;
	}

	/**
	 * Method used to prepare heading columns for beneficiary download report
	 * 
	 * @param buffer
	 * @param currentContract
	 */
	private void getHeadingColumn(StringBuilder buffer, Contract currentContract, boolean isDivisionAllowed,
			boolean isEmployeeIdAllowed) {

		buffer.append(BeneficiaryReportData.LAST_UPDATED_DATE).append(COMMA);

		if (isDivisionAllowed) {
			buffer.append(BeneficiaryReportData.DIVISION).append(COMMA);
		}

		if (isEmployeeIdAllowed) {
			buffer.append(BeneficiaryReportData.EMPLOYEE_ID).append(COMMA);
		}

		buffer.append(BeneficiaryReportData.EMPLOYEE_LAST_NAME).append(COMMA);
		buffer.append(BeneficiaryReportData.EMPLOYEE_FIRST_NAME).append(COMMA);
		buffer.append(BeneficiaryReportData.EMPLOYEE_MIDDLE_NAME).append(COMMA);
		buffer.append(BeneficiaryReportData.PARTICIPANT_SSN).append(COMMA);
		buffer.append(BeneficiaryReportData.BENEFICIARY_TYPE).append(COMMA);
		buffer.append(BeneficiaryReportData.PARTICIPANT_RELATIOSHIP).append(COMMA);
		buffer.append(BeneficiaryReportData.BENEFICIARY_LAST_NAME).append(COMMA);
		buffer.append(BeneficiaryReportData.BENEFICIARY_FIRST_NAME).append(COMMA);
		buffer.append(BeneficiaryReportData.BENEFICIARY_PERCT_SHARE).append(COMMA);
		buffer.append(BeneficiaryReportData.LAST_UPDATED_SOURCE).append(COMMA);
	}

	/**
	 * Method used to find last updated source for beneficiary detail
	 * 
	 * @param beneficiarySet
	 * @return lastUpdated source
	 */
	private String getLastUpdatedSource(BeneficiarySet beneficiarySet) {

		if (BeneficiaryDesignationData.PW_USER_ID_TYPE.equalsIgnoreCase(beneficiarySet.getCreatedUserIdType())
				&& BeneficiaryDesignationData.BM_SOURCE_FUNCTION_CODE
						.equalsIgnoreCase(beneficiarySet.getSourceFunctionCode())) {
			return BeneficiaryDesignationData.PW_SOURCE_LAST_UPDATED_BY;
		} else if (BeneficiaryDesignationData.PWI_USER_ID_TYPE
				.equalsIgnoreCase(beneficiarySet.getCreatedUserIdType())) {
			String lastUpdatedSource = BeneficiaryDesignationData.PSWE_SOURCE_LAST_UPDATED_BY + COMMA + WHITE_SPACE_CHAR
					+ BeneficiaryDesignationData.PSWI_SOURCE_LAST_UPDATED_BY;
			return lastUpdatedSource;
		} else if (BeneficiaryDesignationData.PWE_USER_ID_TYPE
				.equalsIgnoreCase(beneficiarySet.getCreatedUserIdType())) {
			return BeneficiaryDesignationData.PSWE_SOURCE_LAST_UPDATED_BY;
		} else if (BeneficiaryDesignationData.OEE_SOURCE_FUNCTION_CODE
				.equalsIgnoreCase(beneficiarySet.getSourceFunctionCode())
				|| BeneficiaryDesignationData.IEC_SOURCE_FUNCTION_CODE
						.equalsIgnoreCase(beneficiarySet.getSourceFunctionCode())) {
			return BeneficiaryDesignationData.OEE_SOURCE_LAST_UPDATED_BY;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Get the legal text from content object based on location value, to add into
	 * the download report.
	 * 
	 * @param content
	 * @param location
	 * @return legalDesclimar
	 */
	private String getLegalDesclimar(MutableContent content, String context) {

		String legalDesclimar = null;

		ContentText[] contentTexts = content.getAllContentTexts();
		if (contentTexts != null) {
			for (int i = 0; i < contentTexts.length; i++) {
				ContentText contentText = contentTexts[i];
				if (context.equals(contentText.getContext())) {
					legalDesclimar = contentText.getText();
					break;
				}
			}
		}

		return legalDesclimar;

	}

	/**
	 * Method used to include employee details into the beneficiary report.
	 * 
	 * @param buffer
	 * @param employeeData
	 * @param maskSsnFlag
	 */
	private void includeEmployeeDetails(StringBuilder buffer, EmployeeData employeeData, boolean maskSsnFlag,
			boolean isDivisionAllowed, boolean isEmployeeIdAllowed) {

		if (isDivisionAllowed) {
			buffer.append(escapeField(employeeData.getDivision())).append(COMMA);
		}
		if (isEmployeeIdAllowed) {
			buffer.append(employeeData.getEmployeeNumber()).append(COMMA);
		}

		buffer.append(escapeField(employeeData.getLastName())).append(COMMA);
		buffer.append(escapeField(employeeData.getFirstName())).append(COMMA);
		if (StringUtils.isNotEmpty(employeeData.getMiddleInit())) {
			buffer.append(escapeField(employeeData.getMiddleInit()));
		}
		buffer.append(COMMA);
		buffer.append(SSNRender.format(employeeData.getSsn(), null, maskSsnFlag)).append(COMMA);
	}

	/**
	 * Adding escape field if any comma character found in a String
	 * 
	 * @param field
	 * @return
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {

			StringBuffer newField = new StringBuffer();
			newField = newField.append(QUOTE).append(field).append(QUOTE);
			return newField.toString();

		} else {
			return field;
		}
	}

	/**
	 * This method used to check whether the Division column is available or not for
	 * the current contract.
	 * 
	 * @param currentContract
	 * @return
	 */
	private boolean isDivisionAllowed(Contract currentContract) {

		boolean divisionAllowed = false;
		if (currentContract.hasSpecialSortCategoryInd()) {
			divisionAllowed = true;
		}
		return divisionAllowed;
	}

	/**
	 * This method used to check whether the EmployeeId column is available or not
	 * for the current contract.
	 * 
	 * @param currentContract
	 * @return
	 */
	private boolean isEmployeeIdAllowed(Contract currentContract) {

		boolean employeeIdAllowed = false;
		if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE.equalsIgnoreCase(currentContract.getParticipantSortOptionCode())) {
			employeeIdAllowed = true;
		}
		return employeeIdAllowed;
	}

	@RequestMapping(value = "/beneficiaryReport/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm actionForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/beneficiaryReport/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/beneficiaryReport/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
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

	@RequestMapping(value = "/beneficiaryReport/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
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

	@RequestMapping(value = "/beneficiaryReport/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/beneficiaryReport/", params = {"task=dowanloadAll" }, method = {RequestMethod.GET})
	public String doDownloadAll(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	/**/
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
