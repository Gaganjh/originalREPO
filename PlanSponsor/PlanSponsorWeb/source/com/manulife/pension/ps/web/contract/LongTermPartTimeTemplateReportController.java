package com.manulife.pension.ps.web.contract;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCensusTemp;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants.PartTimeQulificationYear;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.eligibility.valueobject.EmployeePlanEntryVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.SSNRender;

/**
 * This class models the action of retrieving and presenting a long term part time template for a contract.
 * 
 * @author sedyasu
 */

@Controller
@RequestMapping( value = "/contract")
public class LongTermPartTimeTemplateReportController extends ReportController {

	@ModelAttribute("longTermPartTimeInformationTemplateForm")
	public ReportForm populateForm() {
		return new ReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("longTermTemp", "/tools/toolsMenu.jsp");
		forwards.put("homepath","redirect:/do/home/homePageFinder/");
		
	}

	public static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("00");
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

	protected static final String DOWNLOAD_COLUMN_HEADING = "elig.h10,Cont#,SSN#,FirstName,LastName,Initial,LTPTAssessYr";

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract.getContractNumber()));

		// if external user, don't display Cancelled employees
		criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	@RequestMapping(value = "/longTermPartTimeInformationTemplate", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("longTermPartTimeInformationTemplateForm") ReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response) throws SystemException {
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract= userProfile.getCurrentContract();
		int contractId = currentContract.getContractNumber();
		
		if (bindingResult.hasErrors()) {
			try {
				request.setAttribute(Constants.REQUEST_TYPE, WithdrawalWebUtil.getTypeOfRequest(userProfile));
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("longTermTemp");
			}
		}
		try
		{
			ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
			ContractServiceFeature eligibilityCalculationCSF = delegate.getContractServiceFeature(contractId,ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
			
			if((eligibilityCalculationCSF != null && ServiceFeatureConstants.YES.equals(eligibilityCalculationCSF.getValue())) 
					&& (currentContract==null || Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))){
				return forwards.get("homepath");
			}
		}
		catch (ApplicationException ae) {
            throw new SystemException(ae.getMessage());
        }
		
		if(!userProfile.isAllowedUploadSubmissions()) {
			return forwards.get("homepath");
		}
		
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return CensusSummaryReportData.TEMPLATE_REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return CensusSummaryReportData.TEMPLATE_REPORT_NAME;
	}

	protected String getDefaultSort() {
		return CensusSummaryReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getFileName()
	 */
	protected String getFileName(HttpServletRequest request) {
		String dateString = null;
		synchronized (DATE_FORMATTER) {
			dateString = DATE_FORMATTER.format(new Date());
		}
		return "LTPT_information_report_for_" + getUserProfile(request).getCurrentContract().getContractNumber() + "for"
				+ dateString + CSV_EXTENSION;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		FunctionalLogger.INSTANCE.log("Download Long Term Part Time Information Template", userProfile, getClass(),
				getMethodName(reportForm, request));

		boolean isECEnabled = false;
		try {

			ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate.getInstance()
					.getContractServiceFeature(currentContract.getContractNumber(),ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
			isECEnabled = ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();
		} catch (ApplicationException ae) {
			throw new SystemException(ae.getMessage());
		}

		StringBuffer buffer = new StringBuffer();

		// heading and records
		buffer.append(DOWNLOAD_COLUMN_HEADING);
		
		Map<BigDecimal, List<EmployeePlanEntryVO>> partTimeEmployeePlanEntryMap = EmployeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID).getAllPartTimeEmployeePlanEntryList(
						getUserProfile(request).getCurrentContract().getContractNumber(), null);
		
		if (!userProfile.isSelectedAccess() && isECEnabled) {
			Iterator<?> iterator = report.getDetails().iterator();
			while (iterator.hasNext()) {
				CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();
				// employee status is not terminated.
				if(theItem.getHireDate()!=null) {
					if(CensusConstants.EMPLOYMENT_STATUS_ACTIVE.equalsIgnoreCase(theItem.getStatus()) 
							|| StringUtils.isBlank(theItem.getStatus())) {
						int longTermPartTimeAssessmentYear = LongTermPartTimeAssessmentUtil.getInstance()
								.evaluateLongTermPartTimeAssessmentYearReport(partTimeEmployeePlanEntryMap,
										Integer.parseInt(theItem.getProfileId()), null);
						
						// display blank if returns 0
						if (longTermPartTimeAssessmentYear >= PartTimeQulificationYear.YEAR_1.getQulificationYear()) {
							buffer.append(LINE_BREAK);
							buffer.append("elig.d").append(COMMA);
							buffer.append(currentContract.getContractNumber()).append(COMMA);
							buffer.append(SSNRender.format(theItem.getSsn(), null, false)).append(COMMA);
							buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
							buffer.append(escapeField(theItem.getLastName())).append(COMMA);
		
							if (theItem.getMiddleInitial() != null) {
								buffer.append(COMMA);
							}
							buffer.append(COMMA);
							buffer.append(longTermPartTimeAssessmentYear);
						}
					}
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations
	 */

	@Autowired
	private PSValidatorFWCensusTemp psValidatorFWCensusTemp;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWCensusTemp);
	}
}
