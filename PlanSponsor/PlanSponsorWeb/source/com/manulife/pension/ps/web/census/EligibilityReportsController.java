package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;
import static com.manulife.pension.service.contract.util.ServiceFeatureConstants.YES;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.employee.valueobject.EmployeeChangeHistoryVO;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class is action class for the Eligibility Reports.
 * 
 * @author Ramamohan Gulla
 *
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({ "eligibilityReportsForm" })

public class EligibilityReportsController extends ReportController {

	@ModelAttribute("eligibilityReportsForm")
	public EligibilityReportsForm populateForm() {
		return new EligibilityReportsForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("default","/census/eligibilityReports.jsp");
		forwards.put("print","/census/eligibilityReports.jsp");
	}

	private static final String className = EligibilityReportsController.class.getName();

	public static final String COMP_PERIOD_CHANGE_REPORT = "compPeriodChangeReport";
	public static final String REPORT_TYPE = "reportType";
	public static final String ELIGIBILITY_REPORT_NAME = "Download eligibility report";
	public static final String COMPUTATION_PERIOD_REPORT_NAME = "Computation period report";
	public static final String NO_CHANGE = "No change";
	private static final String ELIGIBILITY_DATE_COLUMN_NAME = "_ELIGIBILITY_DATE";
	private static final String PLAN_ENTRY_DATE_COLUMN_NAME = "_PLAN_ENTRY_DATE";
	private static final String COMP_PERIOD_END_DATE_COLUMN_NAME = "_COMPUTATION_PERIOD_END_DATE";

	
	//BOTH GET/POST REQUIRED VERIFRED THE CSRF. 
	@RequestMapping( value="/employeeEligibilityReports/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
 	
		UserProfile userProfile = getUserProfile(request);
		EligibilityReportsForm form = (EligibilityReportsForm) actionForm;
		int contractId = userProfile.getCurrentContract().getContractNumber();
		Map csfMap = null;
		ContractServiceDelegate service = ContractServiceDelegate.getInstance();
		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);

		try {
			csfMap = service.getContractServiceFeatures(contractId);

		} catch (ApplicationException ae) {
			throw new SystemException(ae, className, "loadContractServiceFeatureData", ae.getDisplayMessage());
		}

		// Set eligibility calculation request values

		List<LabelValueBean> moneyTypes = new ArrayList<LabelValueBean>();

		ContractServiceFeature eligibilityCalculationCSF = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
		ContractServiceFeature aeCSF = (ContractServiceFeature) csfMap
				.get(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
		Map<String, String> moneyTypeIdName = new TreeMap<String, String>();

		try {

			form.setEZstartOn(DeferralUtils.isEZstartOn(contractId));
			form.setEligibiltyCalcOn(DeferralUtils.isEligibilityCalcOn(contractId));

		} catch (ApplicationException ae) {
			throw new SystemException(ae, "Problem get CSF value for ezstart");
		}

		List<MoneyTypeVO> contractMoneyTypesVoList = service.getContractMoneyTypes(contractId, true);

		if (eligibilityCalculationCSF != null && !"N".equalsIgnoreCase(eligibilityCalculationCSF.getValue())) {

			// get the money types for the contract

			MoneyTypeVO moneyTypeVO = null;

			// get money types for the contract with EC on
			List<String> moneyTypesList = eligibilityServiceDelegate.getMoneyTypesWithEligibilityService(contractId);

			Collections.sort(moneyTypesList);

			for (String moneyTypeId : moneyTypesList) {

				moneyTypeId = StringUtils.trimToEmpty(moneyTypeId);

				// get MoneyTypeVO for money type id
				moneyTypeVO = getMoneyTypeDetailsVO(moneyTypeId, contractMoneyTypesVoList);

				// this is not contract money type
				// hence move to next money type
				if (moneyTypeVO == null) {
					continue;
				}

				if ("EEDEF".equalsIgnoreCase(moneyTypeVO.getId())) {
					form.setEedefShortName(moneyTypeVO.getContractShortName());
				}

				moneyTypes.add(new LabelValueBean(moneyTypeVO.getContractShortName(), moneyTypeVO.getId()));
				moneyTypeIdName.put(moneyTypeVO.getId(), moneyTypeVO.getContractShortName());
			}

		} else if (aeCSF != null && "Y".equalsIgnoreCase(aeCSF.getValue())) {

			for (MoneyTypeVO vo : contractMoneyTypesVoList) {

				if ("EEDEF".equalsIgnoreCase(vo.getId())) {

					moneyTypes.add(new LabelValueBean(vo.getContractShortName(), vo.getId()));
					moneyTypeIdName.put(vo.getId(), vo.getContractShortName());
					form.setEedefShortName(vo.getContractShortName());

				}

			}

		}

		// sort the money types based on alphabetical order of money source
		// short names

		Collections.sort(moneyTypes, new Comparator<LabelValueBean>() {
			public int compare(LabelValueBean vo1, LabelValueBean vo2) {
				return vo1.getLabel().compareTo(vo2.getLabel());
			}
		});

		form.setMoneyTypes(moneyTypes);

		// gets the list of PlanEntryRequirementDetailsVO for the contract

		Collection<PlanEntryRequirementDetailsVO> planEntryRequirementVOList = eligibilityServiceDelegate
				.getPlanEntryRequirementForPlan(contractId);

		PlanEntryRequirementDetailsVO perVO[] = (PlanEntryRequirementDetailsVO[]) planEntryRequirementVOList
				.toArray(new PlanEntryRequirementDetailsVO[0]);

		Map<String, PlanEntryRequirementDetailsVO> map = new HashMap<String, PlanEntryRequirementDetailsVO>();
		List<LabelValueBean> moneyTypesHoS = new ArrayList<LabelValueBean>();

		// To get the money types with only Hours Of Service as the Service Credit
		// Method
		// for computation period change report.

		for (int i = 0; i < perVO.length; i++) {

			if ("H".equalsIgnoreCase(perVO[i].getServiceCreditMethodInd())) {

				map.put(perVO[i].getMoneyTypeId(), perVO[i]);
				if (moneyTypeIdName.get(perVO[i].getMoneyTypeId()) != null) {
					moneyTypesHoS.add(new LabelValueBean(moneyTypeIdName.get(perVO[i].getMoneyTypeId()),
							perVO[i].getMoneyTypeId()));
				}

			}

		}

		form.setMoneyTypesHoS(moneyTypesHoS);

		if (userProfile.getCurrentContract().hasSpecialSortCategoryInd()) {
			form.setHasDivisionFeature(true);
		} else {
			form.setHasDivisionFeature(false);
		}

		if (Constants.EMPLOYEE_ID_SORT_OPTION_CODE
				.equalsIgnoreCase(userProfile.getCurrentContract().getParticipantSortOptionCode())) {
			form.setHasPayrollNumberFeature(true);
		} else {
			form.setHasPayrollNumberFeature(false);
		}

		if ((eligibilityCalculationCSF == null || !YES.equals(eligibilityCalculationCSF.getValue()))
				&& (aeCSF == null || !YES.equals(aeCSF.getValue()))) {
			// return mapping.findForward(HOMEPAGE_FINDER_FORWARD_REDIRECT);
			return HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}

		// return mapping.findForward("default");
		return forwards.get("default");

	}

	/**
	 * @see ReportController#getDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {

		byte[] bytes = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		// Identify the type of report
		if (COMP_PERIOD_CHANGE_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
			bytes = getCompPeriodChangeDownloadData(reportForm, report, request);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}

		return bytes;
	}

	/**
	 * This method is used to generate the CSV report for Computation Period change
	 * report.
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @return
	 */
	protected byte[] getCompPeriodChangeDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) throws SystemException {

		EligibilityReportData compPeriodReport = (EligibilityReportData) request.getAttribute(Constants.REPORT_BEAN);
		List<EmployeeSummaryDetails> empDetails = (List<EmployeeSummaryDetails>) compPeriodReport.getDetails();
		EligibilityReportsForm form = (EligibilityReportsForm) reportForm;
		compPeriodReport.setMoneyTypes(form.getMoneyTypesHoS());

		StringBuffer buffer = new StringBuffer();
		UserProfile user = getUserProfile(request);
		Contract currentContract = user.getCurrentContract();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		if (empDetails.size() > 0) {

			// Title
			buffer.append("Computation Period Report").append(LINE_BREAK);
			// Contract #, Contract name
			buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
					.append(currentContract.getCompanyName()).append(LINE_BREAK);
			buffer.append("Actual Date of download").append(COMMA).append(dateFormat.format(new Date()))
					.append(LINE_BREAK);

			try {

				Content message = null;
				if (!form.isEZstartOn() && form.isEligibiltyCalcOn()) {
					message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.COMPUTATION_PERIOD_CHANGE_REPORT_EC_ONLY,
							ContentTypeManager.instance().MESSAGE);
				}

				if (form.isEZstartOn() && form.isEligibiltyCalcOn()) {

					message = ContentCacheManager.getInstance().getContentById(
							ContentConstants.COMPUTATION_PERIOD_CHANGE_REPORT_EC_AND_AE,
							ContentTypeManager.instance().MESSAGE);
				}

				String contentMessage = ContentUtility.getContentAttribute(message, "text");

				contentMessage = contentMessage == null ? "" : contentMessage;

				buffer.append("\"" + contentMessage + "\"").append(LINE_BREAK).append(LINE_BREAK);
				buffer.append(
						"*Please see the Eligibility Information page or Eligibility report download for more detail.")
						.append(LINE_BREAK);
			} catch (ContentException exp) {
				throw new SystemException(exp, getClass().getName(), "getDownloadData", "Something wrong with CMA");
			}

			buffer.append(LINE_BREAK);

			// Column Headers
			buffer.append("Employee Last Name").append(COMMA).append("Employee First Name").append(COMMA)
					.append("Employee Middle Initial").append(COMMA).append("SSN").append(COMMA).append("Date of Birth")
					.append(COMMA);

			if (form.isHasPayrollNumberFeature()) {
				buffer.append("Employee ID").append(COMMA);
			}

			if (form.isHasDivisionFeature()) {
				buffer.append("Division").append(COMMA);
			}

			buffer.append("Date of Change").append(COMMA);
			buffer.append("Time of Change").append(COMMA);
			buffer.append("Money Type").append(COMMA);
			buffer.append("New Eligibility Date ").append(COMMA);
			buffer.append("New Plan Entry Date ").append(COMMA);
			buffer.append("Previous Eligibility Date ").append(COMMA);
			buffer.append("Previous Plan Entry Date ").append(COMMA);
			buffer.append("Computation Period End Date ").append(COMMA);
			buffer.append(LINE_BREAK).append(LINE_BREAK);

			// SSE S024 determine whether the ssn should be masked on the csv
			// report
			boolean maskSsnFlag = true;// set the mask ssn flag to true as a default

			try {
				maskSsnFlag = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber());

			} catch (SystemException se) {
				logger.error(se);
				// log exception and output blank ssn
			}

			Map<Integer, Map<Timestamp, List<EmployeeChangeHistoryVO>>> changeHistoryMap = compPeriodReport
					.getChangeHistoryMap();

			// Iterate through the details
			for (int det = 0; det < empDetails.size(); det++) {

				Map<Timestamp, List<EmployeeChangeHistoryVO>> changeHistoryVOs = changeHistoryMap
						.get(Integer.valueOf(empDetails.get(det).getProfileId()));
				List<Timestamp> changeKeySet = new ArrayList<Timestamp>(changeHistoryVOs.keySet());

				// Sort by Timestamp
				Collections.sort(changeKeySet);
				Collections.reverse(changeKeySet);

				for (Timestamp timestamp : changeKeySet) {

					String compPeriodChangedDate = DateRender.formatByPattern(timestamp, "",
							RenderConstants.MEDIUM_YMD_DASHED);

					// Group the changes by date
					if (compPeriodChangedDate.equals(empDetails.get(det).getCompPeriodChangeDate())) {
						buffer.append(escapeField(empDetails.get(det).getLastName())).append(COMMA);
						buffer.append(escapeField(empDetails.get(det).getFirstName())).append(COMMA);
						buffer.append(empDetails.get(det).getMiddleInitial() == null ? ""
								: escapeField(empDetails.get(det).getMiddleInitial())).append(COMMA);

						buffer.append(SSNRender.format(empDetails.get(det).getSsn(), null, maskSsnFlag)).append(COMMA);
						buffer.append(empDetails.get(det).getBirthDate() == null ? ""
								: DateRender.formatByPattern(empDetails.get(det).getBirthDate(), "",
										RenderConstants.MEDIUM_MDY_SLASHED))
								.append(COMMA);

						if (form.isHasPayrollNumberFeature()) {

							buffer.append(empDetails.get(det).getEmployerDesignatedID() == null ? ""
									: empDetails.get(det).getEmployerDesignatedID()).append(COMMA);
						}

						if (form.isHasDivisionFeature()) {
							buffer.append(empDetails.get(det).getDivision() == null ? ""
									: escapeField(empDetails.get(det).getDivision())).append(COMMA);
						}

						buffer.append(new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED).format(timestamp))
								.append(COMMA);

						SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
						buffer.append(" " + dateFormatter.format(timestamp)).append(COMMA);

						String currentEligibilityDate = null;
						String previousEligibilityDate = null;
						String previousPlanEntryDate = null;
						String currentPlanEntryDate = null;
						String compPeriodEndDate = null;
						String moneyTypeId = null;

						// Retrieve the eligibility and plan entry date details
						for (EmployeeChangeHistoryVO vo : changeHistoryVOs.get(timestamp)) {

							if (moneyTypeId == null) {
								moneyTypeId = getMoneyType(vo.getColumnName());
							}

							if (vo.getColumnName().contains(ELIGIBILITY_DATE_COLUMN_NAME)) {
								currentEligibilityDate = vo.getCurrentValue();
								previousEligibilityDate = vo.getPreviousValue();
							}

							if (vo.getColumnName().contains(PLAN_ENTRY_DATE_COLUMN_NAME)) {
								currentPlanEntryDate = vo.getCurrentValue();
								previousPlanEntryDate = vo.getPreviousValue();
							}

							if (vo.getColumnName().contains(COMP_PERIOD_END_DATE_COLUMN_NAME)) {
								compPeriodEndDate = vo.getCurrentValue();
							}
						}

						for (LabelValueBean labelValueBean : compPeriodReport.getMoneyTypes()) {
							if (moneyTypeId.equals(labelValueBean.getValue())) {
								buffer.append(labelValueBean.getLabel()).append(COMMA);
							}
						}

						if (currentEligibilityDate != null) {
							buffer.append(currentEligibilityDate).append(COMMA);
						} else {
							buffer.append(NO_CHANGE + ",");
						}

						if (currentPlanEntryDate != null) {
							buffer.append(currentPlanEntryDate).append(COMMA);
						} else {
							buffer.append(NO_CHANGE + ",");
						}

						if (previousEligibilityDate != null) {
							buffer.append(previousEligibilityDate).append(COMMA);
						} else {
							buffer.append(NO_CHANGE + ",");
						}

						if (previousPlanEntryDate != null) {
							buffer.append(previousPlanEntryDate).append(COMMA);
						} else {
							buffer.append(NO_CHANGE + ",");
						}

						if (compPeriodEndDate != null) {
							buffer.append(compPeriodEndDate).append(COMMA);
						} else {
							buffer.append(NO_CHANGE + ",");
						}

						buffer.append(LINE_BREAK);
					}
				}
			}
		} else {
			buffer.append("Currently there are no computation period changes to display");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * To get the default sort field.
	 * 
	 * @return String
	 */
	protected String getDefaultSort() {
		return EligibilityReportData.DEFAULT_SORT;
	}

	/**
	 * To get the default sort direction.
	 * 
	 * @return String
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * To get the report for the report.
	 * 
	 * @return String
	 */
	protected String getReportId() {
		return EligibilityReportData.REPORT_ID;
	}

	/**
	 * To get the report name.
	 * 
	 * @return String
	 */
	protected String getReportName() {
		return ELIGIBILITY_REPORT_NAME;
	}

	/**
	 * This method is used to set the report criteria.
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EligibilityReportsForm psform = (EligibilityReportsForm) form;

		criteria.addFilter(EligibilityReportData.FILTER_CONTRACT_NUMBER,
				Integer.toString(currentContract.getContractNumber()));

		// This criteria is added to distinguish between online report and csv report
		if (COMP_PERIOD_CHANGE_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
			criteria.addFilter(EligibilityReportData.FILTER_REPORT_TYPE, COMP_PERIOD_CHANGE_REPORT);
			String moneyTypesString = "";
			for (LabelValueBean moneyType : psform.getMoneyTypes()) {
				moneyTypesString = moneyTypesString + moneyType.getValue() + ",";
			}
			criteria.addFilter(EligibilityReportData.FILTER_MONEY_TYPES, moneyTypesString);
		}

		// Get the filterCriteriaVo from session
		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);

		if (filterCriteriaVo == null) {
			filterCriteriaVo = new FilterCriteriaVo();
		}

		// set filterCriteriaVo back to session
		SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);

		// if external user, don't display Canceled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}

	}

	/**
	 * This method is used to get the file name. The file name format is : contract
	 * number_Type Of Report_date of report creating in mm dd yyyy format.csv
	 */
	protected String getFileName(HttpServletRequest request) {
		String fileName = "";
		String date = "MM dd yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		date = dateFormat.format(new Date());

		// Identify the type of report
		if (COMP_PERIOD_CHANGE_REPORT.equalsIgnoreCase(request.getParameter(REPORT_TYPE))) {
			fileName = getUserProfile(request).getCurrentContract().getContractNumber() + " "
					+ COMPUTATION_PERIOD_REPORT_NAME + " " + date + CSV_EXTENSION;
		}

		// Replace spaces with underscores
		return fileName.replaceAll("\\ ", "_");
	}

	/**
	 * get the MoneyTypeVO for the given money type id
	 * 
	 * @param moneyTypeId
	 * @param moneyTypeVOList
	 * @return MoneyTypeVO
	 */
	private MoneyTypeVO getMoneyTypeDetailsVO(String moneyTypeId, Collection<MoneyTypeVO> moneyTypeVOList) {
		for (MoneyTypeVO moneyTypeVO : moneyTypeVOList) {
			if (StringUtils.equals(moneyTypeId, StringUtils.trimToEmpty(moneyTypeVO.getId()))) {
				return moneyTypeVO;
			}
		}
		return null;
	}

	/**
	 * Retrieves money type id from the column name
	 * 
	 * @param columneName
	 * @return
	 */
	private String getMoneyType(String columneName) {
		String array[] = columneName.split("_");
		return array[0];
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

	
	@RequestMapping( value="/employeeEligibilityReports/", params = {"task=filter" }, method = {
			RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value="/employeeEligibilityReports/", params = {"task=page" }, method = {
			RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping( value="/employeeEligibilityReports/",params = {"task=sort" }, method = {
			 RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value="/employeeEligibilityReports/", params= {"task=download"}, method = {
			 RequestMethod.GET ,RequestMethod.POST})
	public String doDownload(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	@RequestMapping(value="/employeeEligibilityReports/", params = {"task=downloadAll"}, method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	       
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	
	
	
	
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;
@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}
