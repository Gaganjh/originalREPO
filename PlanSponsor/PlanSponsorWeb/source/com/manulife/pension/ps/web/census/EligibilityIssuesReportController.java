package com.manulife.pension.ps.web.census;

import static com.manulife.pension.platform.web.CommonConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

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
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityIssuesReportVO;
import com.manulife.pension.ps.service.report.census.valueobject.EligibilityReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.SSNRender;

/**
 * Eligibility Issues CVS report will show all the ineligible employee for the
 * selected reported from to to date.
 * 
 * @author Saravanan Narayanasamy
 * 
 */
@Controller
@RequestMapping( value = "/census")
@SessionAttributes({"eligibilityReportsForm"})

public class EligibilityIssuesReportController extends ReportController {

	
	@ModelAttribute("eligibilityReportsForm")
	public EligibilityReportsForm populateForm()
	{
		return new EligibilityReportsForm();
		}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/census/eligibilityIssuesReports.jsp");
		forwards.put("default", "/census/eligibilityIssuesReports.jsp");
		forwards.put("staging", "/census/eligibilityReports.jsp");
	}

	public static final String SPACE_SEPARATOR = " ";
	
	private static final Logger logger = Logger
			.getLogger(EligibilityIssuesReportController.class);

	@Override
	protected String getDefaultSort() {
		return EligibilityReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@Override
	protected String getReportId() {
		return EligibilityReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return EligibilityReportData.ELIGIBILITY_ISSUES_REPORT_NAME;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData reportData, HttpServletRequest httpServletRequest)
			throws SystemException {

		final String methodName = "getDownloadData";
		LogUtility.logEntry(logger, methodName);

		UserProfile userProfile = getUserProfile(httpServletRequest);
		Contract currentContract = userProfile.getCurrentContract();

		byte[] bytes = null;

		// Identify the type of report
		if (EligibilityReportData.ELIGIBILITY_ISSUES_REPORT
				.equalsIgnoreCase(httpServletRequest
						.getParameter(EligibilityReportData.REPORT_TYPE))) {

			boolean maskSsnFlag = ReportDownloadHelper.isMaskedSsn(userProfile,
					currentContract.getContractNumber());

			bytes = getEligibilityIssuesDownloadData(reportForm,
					reportData, httpServletRequest, maskSsnFlag);
		}

		LogUtility.logExit(logger, methodName);

		return bytes;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm baseReportForm,
			HttpServletRequest request) throws SystemException {

		final String methodName = "populateReportCriteria";
		LogUtility.logEntry(logger, methodName);

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		EligibilityReportsForm eligibilityReportForm = (EligibilityReportsForm) baseReportForm;

		// Populate the report criterias
		criteria.addFilter(EligibilityReportData.FILTER_CONTRACT_NUMBER,
				Integer.toString(currentContract.getContractNumber()));
		criteria.addFilter(EligibilityReportData.FILTER_REPORT_TYPE,
				EligibilityReportData.ELIGIBILITY_ISSUES_REPORT);
		criteria.addFilter(EligibilityReportData.REPORTED_FROM_DATE,
				eligibilityReportForm.getReportedFromDate());
		criteria.addFilter(EligibilityReportData.REPORTED_TO_DATE,
				eligibilityReportForm.getReportedToDate());

		LogUtility.logExit(logger, methodName);
	}

	/**
	 * Eligibility issue download data options
	 * 
	 * @param baseReportForm
	 * @param reportData
	 * @param httpServletRequest
	 * @param maskSsnFlag
	 * @return
	 * @throws SystemException
	 */
	protected byte[] getEligibilityIssuesDownloadData(
			BaseReportForm baseReportForm, ReportData reportData,
			HttpServletRequest httpServletRequest, boolean maskSsnFlag)
			throws SystemException {

		final String methodName = "getEligibilityIssuesDownloadData";
		LogUtility.logEntry(logger, methodName);

		EligibilityReportData eligibilityReportData = (EligibilityReportData) httpServletRequest
				.getAttribute(Constants.REPORT_BEAN);
		EligibilityReportsForm eligibilityReportForm = (EligibilityReportsForm) baseReportForm;

		StringBuffer contentStringBuffer = new StringBuffer();

		// If there are no changes, Then display static text in Eligibility
		// Changes Report
		if (eligibilityReportData.getDetails().size() == 0) {
			contentStringBuffer.append(EligibilityReportData.NO_ISSUES_FOUND);
			return contentStringBuffer.toString().getBytes();
		}

		ContractMoneyType contractMoneyType = new ContractMoneyType();
		this.setReportElementsHeading(contentStringBuffer, httpServletRequest,
				eligibilityReportForm, contractMoneyType);
		this.setColumnHeadings(contentStringBuffer, eligibilityReportForm);

		this.loadReportData(maskSsnFlag, contentStringBuffer,
				eligibilityReportData.getEligibilityIssuesReportVOList(),
				eligibilityReportForm, contractMoneyType);

		LogUtility.logExit(logger, methodName);

		return contentStringBuffer.toString().getBytes();

	}

	/**
	 * 
	 * Method used to get the default value for eligibility issue report format
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 * @return ActionForward
	 */
	
	
	
	@RequestMapping( value = "/employeeEligibilityIssuesReports/", method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String forward="redirect:"+"/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	   	       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	       }
	      
		}
		UserProfile userProfile = getUserProfile(request);
		
		int contractId = userProfile.getCurrentContract().getContractNumber();

		boolean isAEServiceON = false;
		boolean isECServiceON = false;

		try {
			isAEServiceON = DeferralUtils.isEZstartOn(contractId);
			isECServiceON = DeferralUtils.isEligibilityCalcOn(contractId);

		} catch (ApplicationException applicationException) {
			throw new SystemException(applicationException,
					"Problem get CSF value for ezstart,ec");
		}

		form.setEZstartOn(isAEServiceON);
		form.setEligibiltyCalcOn(isECServiceON);

		if (!isAEServiceON && !isECServiceON) {

			
			return forwards.get(HOMEPAGE_FINDER_FORWARD_REDIRECT);
		}
		Collection<GenericException> errors = doValidate(form,request);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			 return forwards.get("input");
        
		}
		
		return forwards.get("default");
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	
	protected Collection doValidate(
			ActionForm actionForm, HttpServletRequest httpServletRequest) {

		GenericException genericException = null;
		Collection errorCollection = super.doValidate(
				actionForm, httpServletRequest);
		EligibilityReportsForm eligibilityReportsForm = (EligibilityReportsForm) actionForm;

		if (!EligibilityReportData.ELIGIBILITY_ISSUES_REPORT
				.equalsIgnoreCase(httpServletRequest
						.getParameter(EligibilityReportData.REPORT_TYPE))) {
			this.setDefaultFromAndToDate(eligibilityReportsForm);
			return errorCollection;
		}

		try {
			EligibilityReportData.SIMPLE_DATE_FORMAT.setLenient(false);
			Date fromDate = null;
			if (!StringUtils.isEmpty(eligibilityReportsForm
					.getReportedFromDate())) {
				fromDate = EligibilityReportData.dateParseMMDDYY(eligibilityReportsForm.getReportedFromDate());
			}

			Date toDate = null;
			if (!StringUtils
					.isEmpty(eligibilityReportsForm.getReportedToDate())) {
				toDate = EligibilityReportData.dateParseMMDDYY(eligibilityReportsForm.getReportedToDate());
			}

			if (fromDate != null) {
				
				Date currentDate = new Date();

				Calendar calFromDate = Calendar.getInstance();
				calFromDate.setTime(fromDate);

				Calendar calEarliestDate = Calendar.getInstance();
				calEarliestDate.add(Calendar.MONTH, -24);
				if (calFromDate.before(calEarliestDate) || calFromDate.after(Calendar.getInstance())) {
					genericException = new GenericException(
							ErrorCodes.FROM_DATE_BEFORE_24_MONTHS); // 2267
					errorCollection.add(genericException);
				}

				
				if (errorCollection.size() == 0 && fromDate.after(toDate)) {
					genericException = new GenericException(
							ErrorCodes.FROM_DATE_AFTER_TO); // 2266
					errorCollection.add(genericException);
				}
			} else {
				genericException = new GenericException(
						ErrorCodes.FROM_DATE_EMPTY); // 2265
				errorCollection.add(genericException);
			}

		} catch (ParseException parseException) {
			genericException = new GenericException(ErrorCodes.INVALID_DATE); // 2268
			errorCollection.add(genericException);
		}

		return errorCollection;
	}


	/**
	 * This method is used to get the file name. The file name format is :
	 * contract number_Type Of Report_date of report creating in mm dd yyyy
	 * format.csv
	 */
	protected String getFileName(HttpServletRequest request) {
		String fileName = "";
		String date = "MM dd yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date);
		date = simpleDateFormat.format(new Date());

		// Identify the type of report
		if (EligibilityReportData.ELIGIBILITY_ISSUES_REPORT
				.equalsIgnoreCase(request
						.getParameter(EligibilityReportData.REPORT_TYPE))) {
			fileName = getUserProfile(request).getCurrentContract()
					.getContractNumber()
					+ " "
					+ EligibilityReportData.ELIGIBILITY_ISSUES_REPORT_NAME
					+ " " + date + CSV_EXTENSION;
		}
		// Replace spaces with underscores
		return fileName.replaceAll("\\ ", "_");
	}

	/**
	 * Method used to append the content message of given content Id
	 * 
	 * @param contentId -
	 *            CMA content Id
	 * @param stringBuffer -
	 *            Buffer
	 * @throws SystemException
	 */
	private void appendContentMessage(int contentId, StringBuffer stringBuffer)
			throws SystemException {

		final String backwardSlash = "\"";
		final String text = "text";

		try {
			Content message = ContentCacheManager.getInstance().getContentById(
					contentId, ContentTypeManager.instance().MESSAGE);

			String contentMessage = ContentUtility.getContentAttribute(message,
					text);

			stringBuffer.append(
					backwardSlash + StringUtils.trimToEmpty(contentMessage)
							+ backwardSlash).append(LINE_BREAK);

		} catch (ContentException contentException) {
			throw new SystemException(contentException,
					"Unable to get the content from CMA");
		}
	}

	/**
	 * Method used to set the report elements Heading
	 * 
	 * @param contentStringBuffer
	 * @param httpServletRequest
	 * @param eligibilityReportForm
	 * @param contractMoneyTypeList 
	 * @throws SystemException
	 */
	private void setReportElementsHeading(StringBuffer contentStringBuffer,
			HttpServletRequest httpServletRequest,
			EligibilityReportsForm eligibilityReportForm, ContractMoneyType contractMoneyTypeList)
			throws SystemException {

		Contract currentContract = getUserProfile(httpServletRequest).getCurrentContract();

		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		
		contractMoneyTypeList.setContractMoneyTypeList(contractServiceDelegate.getContractMoneyTypes(currentContract
				.getContractNumber(), false));

		// Line 1(A1): Report Title
		contentStringBuffer
				.append(EligibilityReportData.ELIGIBILITY_ISSUES_REPORT_TITLE);
		contentStringBuffer.append(LINE_BREAK);

		// Line 2(B1) Contract
		contentStringBuffer.append(EligibilityReportData.CONTRACT);
		contentStringBuffer.append(COMMA);

		// Line 2(B2) Contract Number
		contentStringBuffer.append(currentContract.getContractNumber());
		contentStringBuffer.append(COMMA);

		// Line 2(C2) Contract Name
		String contractName = StringUtils.trimToEmpty(currentContract
				.getCompanyName());
		contractName = '"' + contractName + '"';
		contentStringBuffer.append(contractName);
		contentStringBuffer.append(LINE_BREAK);

		// Line 3(A3) Actual Date of Download
		contentStringBuffer.append(EligibilityReportData.ACTUAL_DATE_OF_DOWNLOAD);
		contentStringBuffer.append(COMMA);
		// Line 3(B3) Actual Date of Download value
		contentStringBuffer.append(DateRender.formatByPattern(new Date(), "",
				EligibilityReportData.DATE_PATTERN));
		contentStringBuffer.append(LINE_BREAK);

		// Line 4(A4) Report Period From
		contentStringBuffer.append(EligibilityReportData.REPORT_PERIOD_FROM);
		contentStringBuffer.append(COMMA);
		// Line 4(B4) Report Period From value
		contentStringBuffer.append(eligibilityReportForm.getReportedFromDate());
		contentStringBuffer.append(LINE_BREAK);

		// Line 5(A5) Report Period To
		contentStringBuffer.append(EligibilityReportData.REPORT_PERIOD_TO);
		contentStringBuffer.append(COMMA);
		// Line 5(B5) Report Period To value
		contentStringBuffer.append(eligibilityReportForm.getReportedToDate());
		contentStringBuffer.append(LINE_BREAK);

		// Line 6 Blank
		contentStringBuffer.append(LINE_BREAK);

		// Line 7 User Instruction
		this.appendContentMessage(
				ContentConstants.ELIGIBILITY_ISSUES_REPORT_USAGE_INSTRUCTIONS,
				contentStringBuffer);

		// Line 8 Blank
		contentStringBuffer.append(LINE_BREAK);

		// Line 9 Disclaimer Content
		if (eligibilityReportForm.isEZstartOn()
				&& eligibilityReportForm.isEligibiltyCalcOn()) {
			this
					.appendContentMessage(
							ContentConstants.ELIGIBILITY_ISSUES_REPORT_DISCLAIMER_EC_AE,
							contentStringBuffer);
		} else if (!eligibilityReportForm.isEZstartOn()
				&& eligibilityReportForm.isEligibiltyCalcOn()) {
			this.appendContentMessage(
					ContentConstants.ELIGIBILITY_ISSUES_REPORT_DISCLAIMER_EC,
					contentStringBuffer);
		} else if (eligibilityReportForm.isEZstartOn()
				&& !eligibilityReportForm.isEligibiltyCalcOn()) {
			this.appendContentMessage(
					ContentConstants.ELIGIBILITY_ISSUES_REPORT_DISCLAIMER_AE,
					contentStringBuffer);
		}

		// Line 10 Blank
		contentStringBuffer.append(LINE_BREAK);
	}

	/**
	 * Method used to set the column heading
	 * 
	 * @param contentStringBuffer
	 * @param eligibilityReportForm
	 */
	private void setColumnHeadings(StringBuffer contentStringBuffer,
			EligibilityReportsForm eligibilityReportForm) {
		// Line 11 Column headings
		contentStringBuffer.append(EligibilityReportData.EMPLOYEE_LAST_NAME);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.EMPLOYEE_FIRST_NAME);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.EMPLOYEE_MIDDLE_NAME);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.EMPLOYEE_SSN);
		contentStringBuffer.append(COMMA);

		if (eligibilityReportForm.isHasPayrollNumberFeature()) {
			contentStringBuffer.append(EligibilityReportData.EMPLOYEE_ID);
			contentStringBuffer.append(COMMA);
		}

		if (eligibilityReportForm.isHasDivisionFeature()) {
			contentStringBuffer.append(EligibilityReportData.DIVISION);
			contentStringBuffer.append(COMMA);
		}

		contentStringBuffer
				.append(EligibilityReportData.ELIGIBILITY_TO_PARTICIPATE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.MONEY_TYPE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.PLAN_ENTRY_DATE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.TRANSACTION_TYPE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.ENROLLMENT_METHOD);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.ENROLLMENT_EFFECTIVE_DATE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.TRANSACTION_DATE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.PAYROLL_APPLICABLE_DATE);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.SUBMISSION_NUMBER);
		contentStringBuffer.append(COMMA);
		contentStringBuffer.append(EligibilityReportData.TRANSACTION_NUMBER);
		contentStringBuffer.append(LINE_BREAK);
	}

	/**
	 * Method used to load the dynamic report data values
	 * 
	 * @param maskSsnFlag
	 * @param contentStringBuffer
	 * @param eligibilityIssuesReportVOList
	 * @param eligibilityReportForm
	 */
	private void loadReportData(boolean maskSsnFlag,
			StringBuffer contentStringBuffer,
			List<EligibilityIssuesReportVO> eligibilityIssuesReportVOList,
			EligibilityReportsForm eligibilityReportForm, ContractMoneyType contractMoneyType) {

		String enrollmentMethod = null;

		for (EligibilityIssuesReportVO eligibilityIssuesReportVO : eligibilityIssuesReportVOList) {
			// Line 12 Dynamic Value loading...
			contentStringBuffer.append(StringUtils
					.trimToEmpty(eligibilityIssuesReportVO.getLastName()));
			contentStringBuffer.append(COMMA);
			contentStringBuffer.append(StringUtils
					.trimToEmpty(eligibilityIssuesReportVO.getFirstName()));
			contentStringBuffer.append(COMMA);
			contentStringBuffer.append(StringUtils
					.trimToEmpty(eligibilityIssuesReportVO.getMiddleName()));
			contentStringBuffer.append(COMMA);

			contentStringBuffer.append(StringUtils.trimToEmpty(SSNRender
					.format(eligibilityIssuesReportVO.getSsn(), null,
							maskSsnFlag)));
			contentStringBuffer.append(COMMA);

			if (eligibilityReportForm.isHasPayrollNumberFeature()) {
				contentStringBuffer.append(StringUtils.trimToEmpty(eligibilityIssuesReportVO
						.getEmployeeId()));
				contentStringBuffer.append(COMMA);
			}

			if (eligibilityReportForm.isHasDivisionFeature()) {

				contentStringBuffer.append(StringUtils
						.trimToEmpty(eligibilityIssuesReportVO.getDivision()));
				contentStringBuffer.append(COMMA);
			}

			contentStringBuffer.append(StringUtils
					.trimToEmpty(eligibilityIssuesReportVO
							.getEligibilityIndicator()));
			contentStringBuffer.append(COMMA);
			contentStringBuffer.append(this.getMoneyTypeShortNameList(StringUtils
					.trimToEmpty(eligibilityIssuesReportVO.getMoneyTypes()),  contractMoneyType));
			contentStringBuffer.append(COMMA);

			if (eligibilityIssuesReportVO.getPlanEntryDate() != null) {
				contentStringBuffer.append(eligibilityIssuesReportVO.getPlanEntryDate());
				contentStringBuffer.append(COMMA);
			} else {
				contentStringBuffer.append(COMMA);
			}
			
			String transactionType = StringUtils
					.trimToEmpty(eligibilityIssuesReportVO.getTransactionType());
			contentStringBuffer.append(transactionType);
			contentStringBuffer.append(COMMA);

			enrollmentMethod = StringUtils
					.trimToEmpty(eligibilityIssuesReportVO
							.getEnrollmentMethod());

			if (EligibilityReportData.ENROLLMENT_METHOD_CODE_PAPER
					.equalsIgnoreCase(enrollmentMethod)) {
				contentStringBuffer
						.append(EligibilityReportData.ENROLLMENT_METHOD_PAPER);
			} else if (EligibilityReportData.ENROLLMENT_METHOD_CODE_ENROLLMENT
					.equalsIgnoreCase(enrollmentMethod)) {
				contentStringBuffer
						.append(EligibilityReportData.ENROLLMENT_METHOD_ENROLLMENT);
			} else {
				contentStringBuffer
						.append(EligibilityReportData.ENROLLMENT_METHOD_AUTO_ENROLLMENT);
			}

			contentStringBuffer.append(COMMA);
			contentStringBuffer.append(eligibilityIssuesReportVO
							.getEnrollmentEffectiveDate());
			contentStringBuffer.append(COMMA);

			contentStringBuffer.append(eligibilityIssuesReportVO
					.getTransactionDate());
			contentStringBuffer.append(COMMA);
			
			if (eligibilityIssuesReportVO.getPayrollApplicableDate() != null) {
				contentStringBuffer.append(eligibilityIssuesReportVO.getPayrollApplicableDate());
				contentStringBuffer.append(COMMA);
			} else {
				contentStringBuffer.append(COMMA);
			}
			
			contentStringBuffer.append(eligibilityIssuesReportVO
					.getSubmissionNumber());
			contentStringBuffer.append(COMMA);
			contentStringBuffer.append(eligibilityIssuesReportVO
					.getTransactionId());
			contentStringBuffer.append(LINE_BREAK);
		}
	}

	/**
	 * Method used to set the default from and to date
	 * 
	 * @param eligibilityReportsForm
	 */
	private void setDefaultFromAndToDate(
EligibilityReportsForm eligibilityReportsForm) {

		Calendar calendar = Calendar.getInstance();

		String toDate = EligibilityReportData.dateFormatMMDDYY(calendar
				.getTime());

		int firstDate = calendar.getActualMinimum(Calendar.DATE);
		calendar.set(Calendar.DATE, firstDate);

		String fromDate = EligibilityReportData.dateFormatMMDDYY(calendar
				.getTime());

		eligibilityReportsForm.setReportedFromDate(fromDate);
		eligibilityReportsForm.setReportedToDate(toDate);
	}

	/**
	 * Method used to get the money type short name for given money type Id's.
	 * If money type Id is not available in CSDB then use money type Id.
	 * 
	 * @param moneyTypeList
	 * @return tempMoneyTypeList
	 */
	private String getMoneyTypeShortNameList(String moneyTypeList, ContractMoneyType contractMoneyType) {

		boolean isValidMoneyType = false;
		String moneyTypeShortNameList = "";
		String moneyTypes[] = null;

		if (StringUtils.isNotBlank(moneyTypeList)) {
			moneyTypes = moneyTypeList.split(EligibilityIssuesReportController.SPACE_SEPARATOR);

			for (String moneyTypeId : moneyTypes) {
				isValidMoneyType = false;

				for (MoneyTypeVO moneyTypeVO :contractMoneyType.getContractMoneyTypeList()) {
					if (StringUtils.equalsIgnoreCase(moneyTypeId, moneyTypeVO.getId())) {
						if (StringUtils.isNotBlank(moneyTypeShortNameList)) {
							moneyTypeShortNameList = moneyTypeShortNameList + " " + moneyTypeVO.getContractShortName();
						} else {
							moneyTypeShortNameList = moneyTypeVO.getContractShortName();
						}

						isValidMoneyType = true;
						break;

					}
				}

				// If money type Id is not found in CSDB. Then use Money type Id
				// in the Money Type list
				if (!isValidMoneyType) {
					if (StringUtils.isNotBlank(moneyTypeShortNameList)) {
						moneyTypeShortNameList = moneyTypeShortNameList
								+ EligibilityIssuesReportController.SPACE_SEPARATOR + moneyTypeId;
					} else {
						moneyTypeShortNameList = moneyTypeId;
					}
				}
			}
		}

		return moneyTypeShortNameList;
	}
	
	/**
	 * 
	 * @author arugupu
	 *
	 */
	private static class ContractMoneyType {
		
		private List<MoneyTypeVO> contractMoneyTypeList = null;

		/**
		 * @return the contractMoneyTypeList
		 */
		public List<MoneyTypeVO> getContractMoneyTypeList() {
			return contractMoneyTypeList;
		}

		/**
		 * @param contractMoneyTypeList the contractMoneyTypeList to set
		 */
		public void setContractMoneyTypeList(List<MoneyTypeVO> contractMoneyTypeList) {
			this.contractMoneyTypeList = contractMoneyTypeList;
		}
		
		
	}
	
	
	@RequestMapping(value = "/employeeEligibilityIssuesReports/", params = {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward = "redirect:" + "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}

		}
		Collection<GenericException> errors = doValidate(form,request);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			 return forwards.get("input");
        
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/employeeEligibilityIssuesReports/", params = {"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward = "redirect:" + "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}

		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/employeeEligibilityIssuesReports/",params = {"task=sort" }, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward = "redirect:" + "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}

		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/employeeEligibilityIssuesReports/",params = {"task=download"},method = {RequestMethod.POST })
	public String doDownload(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward = "redirect:" + "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}

		}
		Collection<GenericException> errors = doValidate(form,request);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get("input");
           
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/employeeEligibilityIssuesReports/", params = {"task=dowanloadAll" },method = {RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("eligibilityReportsForm") EligibilityReportsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward = "redirect:" + "/do" + new UrlPathHelper().getPathWithinServletMapping(request);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}

		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
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
