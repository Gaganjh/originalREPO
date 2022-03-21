/**
 * 
 */
package com.manulife.pension.ps.web.census.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.DeferralSummaryDAO;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralStatisticsSummary;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.DeferralReportForm;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;

/**
 * A utility class providing Web tier utility methods for Deferral
 * 
 * @author Adrian Patru
 * 
 */
public class DeferralUtils {
	public static final int NUMBER_FULL_COLUMNS = 26;
	public static final int NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID = 24;
	public static final int NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID = 22;
	public static final int NUMBER_FULL_COLUMNS_ACI_OFF = 16;
	public static final int NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID_ACI_OFF = 14;
	public static final int NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID_ACI_OFF = 12;
	public static final String YES = "YES";
	public static final String YES_ACI = "Y";
	public static final String ACI_DEFAULTED_TO_YES = "DY";
	public static final String NO = "NO";
	public static final String AUTO_ENROLLMENT = "AE";
	public static final String INITIAL_ENROLLMENT_DATE = "IED";
	public static final String WEB_MESSAGE_TYPE_TPAFIRM = "TPA";
	public static final String WEB_MESSAGE_TYPE_CONTRACT_PSW = "PSW";
	public static final String OPT_OUT = "OOD";
	public static final String PS_CONTRACT_STATUS = "PS";
	public static final String PC_CONTRACT_STATUS = "PC";
	public static final String DC_CONTRACT_STATUS = "DC";
	public static final String ER_LIMIT_REACHED = "ER limit reached";
	public static final String DOLLAR = "$";
	public static final String PERCENT = "%";
	public static final String EITHER = "E";
	public static final String EE_LIMIT_REACHED = "EE limit reached";
	public static final String ELIGIBILITY_CALC = "EC";

	/*
	 * Gets the numberOfDisplayColumns
	 * 
	 * @return Returns a int
	 */
	public static int getNumberOfDisplayColumns(HttpServletRequest request) {
		DeferralReportForm form = (DeferralReportForm) request.getSession().getAttribute("deferralReportForm");

		if (form.isACIOff()) {
			if (request.getParameter("printFriendly") != null) {
				return getNumberOfColumnsForACIOFFNoAction(form);
			} else {
				return getNumberOfColumnsForACIOFF(form);
			}
		} else {
			if (request.getParameter("printFriendly") != null) {
				return getNumberOfColumnsForACIONNoAction(form);
			} else {
				return getNumberOfColumnsForACION(form);
			}
		}
	}

	/**
	 * Method that returns the required header based on conditions from request
	 * 
	 * @return
	 */
	public static String getTableHeader(HttpServletRequest request) {
		DeferralReportForm form = (DeferralReportForm) request.getSession().getAttribute("deferralReportForm");

		if (form.isACIOff()) {
			if (request.getParameter("printFriendly") != null) {
				return getTableHeaderForACIOFFNoAction(form);
			} else {
				return getTableHeaderForACIOFF(form);
			}
		} else {
			if (request.getParameter("printFriendly") != null) {
				return getTableHeaderForACIONNoAction(form);
			} else {
				return getTableHeaderForACION(form);
			}
		}
	}

	public static boolean isEligibilityCalcOn(int contractNumber) throws ApplicationException, SystemException {

		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
		ContractServiceFeature csf = delegate.getContractServiceFeature(contractNumber, ELIGIBILITY_CALC);

		if ((csf != null) && (YES_ACI.equalsIgnoreCase(csf.getValue()))) {
			return true;
		}

		return false;
	}

	public static int getNumberOfColumnsForACION(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID;
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID;
		}

		return NUMBER_FULL_COLUMNS;
	}

	public static String getTableHeaderForACION(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='29'></td><!--Action-->" + "<td width='1'></td>" + "<td width='176'></td><!--Name/SSN-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='60'></td><!--Deferral value-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Auto Increase-->" + "<td width='1'></td>" + "<td width='90'></td><!--Date of next ADI-->" + "<td width='1'></td>" + "<td width='50'></td><!--Type-->"
					+ "<td width='1'></td>" + "<td width='60'></td><!--Personal Increase amount-->" + "<td width='1'></td>" + "<td width='60'></td><!--Personal limit-->" + "<td width='1'></td>"
					+ "<td width='40'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='29'></td><!--Action-->" + "<td width='1'></td>" + "<td width='156'></td><!--Name/SSN-->" + "<td width='1'></td>"
					+ "<td width='49'></td><!--Employee ID or Division-->" + "<td width='1'></td>" + "<td width='60'></td><!--Deferral type-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Auto Increase-->" + "<td width='1'></td>"
					+ "<td width='90'></td><!--Date of next ADI-->" + "<td width='1'></td>" + "<td width='50'></td><!--Type-->" + "<td width='1'></td>"
					+ "<td width='50'></td><!--Personal Increase amount-->" + "<td width='1'></td>" + "<td width='50'></td><!--Personal limit-->" + "<td width='1'></td>"
					+ "<td width='30'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		return "<tr>" + "<td width='1'></td>" + "<td width='29'></td><!--Action-->" + "<td width='1'></td>" + "<td width='156'></td><!--Name/SSN-->" + "<td width='1'></td>"
				+ "<td width='49'></td><!--Employee ID-->" + "<td width='1'></td>" + "<td width='49'></td><!--Division-->" + "<td width='1'></td>" + "<td width='50'></td><!--Deferral type-->"
				+ "<td width='1'></td>" + "<td width='50'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='50'></td><!--Auto Increase-->" + "<td width='1'></td>"
				+ "<td width='80'></td><!--Date of next ADI-->" + "<td width='1'></td>" + "<td width='40'></td><!--Type-->" + "<td width='1'></td>"
				+ "<td width='50'></td><!--Personal Increase amount-->" + "<td width='1'></td>" + "<td width='50'></td><!--Personal limit-->" + "<td width='1'></td>"
				+ "<td width='30'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
	}

	public static int getNumberOfColumnsForACIONNoAction(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID - 2;
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID - 2;
		}

		return NUMBER_FULL_COLUMNS - 2;
	}

	public static String getTableHeaderForACIONNoAction(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='196'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='60'></td><!--Deferral type-->" + "<td width='1'></td>"
					+ "<td width='70'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Auto Increase-->" + "<td width='1'></td>"
					+ "<td width='90'></td><!--Date of next ADI-->" + "<td width='1'></td>" + "<td width='50'></td><!--Type-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Personal Increase amount-->" + "<td width='1'></td>" + "<td width='60'></td><!--Personal limit-->" + "<td width='1'></td>"
					+ "<td width='50'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='186'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='59'></td><!--Employee ID or Division-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='60'></td><!--Deferral value-->" + "<td width='1'></td>"
					+ "<td width='60'></td><!--Auto Increase-->" + "<td width='1'></td>" + "<td width='90'></td><!--Date of next ADI-->" + "<td width='1'></td>" + "<td width='40'></td><!--Type-->"
					+ "<td width='1'></td>" + "<td width='50'></td><!--Personal Increase amount-->" + "<td width='1'></td>" + "<td width='50'></td><!--Personal limit-->" + "<td width='1'></td>"
					+ "<td width='30'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		return "<tr>" + "<td width='1'></td>" + "<td width='186'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='49'></td><!--Employee ID-->" + "<td width='1'></td>"
				+ "<td width='49'></td><!--Division-->" + "<td width='1'></td>" + "<td width='50'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='50'></td><!--Deferral value-->"
				+ "<td width='1'></td>" + "<td width='50'></td><!--Auto Increase-->" + "<td width='1'></td>" + "<td width='80'></td><!--Date of next ADI-->" + "<td width='1'></td>"
				+ "<td width='40'></td><!--Type-->" + "<td width='1'></td>" + "<td width='50'></td><!--Personal Increase amount-->" + "<td width='1'></td>"
				+ "<td width='50'></td><!--Personal limit-->" + "<td width='1'></td>" + "<td width='30'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
	}

	public static int getNumberOfColumnsForACIOFF(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID_ACI_OFF;
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID_ACI_OFF;
		}

		return NUMBER_FULL_COLUMNS_ACI_OFF;
	}

	public static String getTableHeaderForACIOFF(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='30'></td><!--Action-->" + "<td width='1'></td>" + "<td width='400'></td><!--Name/SSN-->" + "<td width='1'></td>"
					+ "<td width='100'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='100'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Warning-->"
					+ "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='30'></td><!--Action-->" + "<td width='1'></td>" + "<td width='300'></td><!--Name/SSN-->" + "<td width='1'></td>"
					+ "<td width='99'></td><!--Employee ID or Division-->" + "<td width='1'></td>" + "<td width='100'></td><!--Deferral type-->" + "<td width='1'></td>"
					+ "<td width='100'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		return "<tr>" + "<td width='1'></td>" + "<td width='30'></td><!--Action-->" + "<td width='1'></td>" + "<td width='200'></td><!--Name/SSN-->" + "<td width='1'></td>"
				+ "<td width='99'></td><!--Employee ID-->" + "<td width='1'></td>" + "<td width='99'></td><!--Division-->" + "<td width='1'></td>" + "<td width='100'></td><!--Deferral type-->"
				+ "<td width='1'></td>" + "<td width='100'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>"
				+ "</tr>";
	}

	public static int getNumberOfColumnsForACIOFFNoAction(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_AND_EMPLOYEE_ID_ACI_OFF - 2;
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return NUMBER_COLUMNS_LESS_DIVISION_OR_EMPLOYEE_ID_ACI_OFF - 2;
		}

		return NUMBER_FULL_COLUMNS_ACI_OFF - 2;
	}

	public static String getTableHeaderForACIOFFNoAction(DeferralReportForm form) {
		if (!form.getHasDivisionFeature() && !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='400'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='100'></td><!--Deferral type-->" + "<td width='1'></td>"
					+ "<td width='131'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		if (!form.getHasDivisionFeature() || !form.getHasPayrollNumberFeature()) {
			return "<tr>" + "<td width='1'></td>" + "<td width='300'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='99'></td><!--Employee ID or Division-->" + "<td width='1'></td>"
					+ "<td width='100'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='131'></td><!--Deferral value-->" + "<td width='1'></td>" + "<td width='60'></td><!--Warning-->"
					+ "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
		}

		return "<tr>" + "<td width='1'></td>" + "<td width='200'></td><!--Name/SSN-->" + "<td width='1'></td>" + "<td width='99'></td><!--Employee ID-->" + "<td width='1'></td>"
				+ "<td width='99'></td><!--Division-->" + "<td width='1'></td>" + "<td width='100'></td><!--Deferral type-->" + "<td width='1'></td>" + "<td width='131'></td><!--Deferral value-->"
				+ "<td width='1'></td>" + "<td width='60'></td><!--Warning-->" + "<td width='4'></td>" + "<td width='1'></td>" + "</tr>";
	}

	/**
	 * Web utility returns HTML code for an error or warning icon
	 * 
	 * @param item
	 * @return
	 */
	public static String getAutoIncreaseFlagError(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getAutoIncreaseFlagStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	/**
	 * Web utility returns HTML code for an error or warning icon
	 * 
	 * @param item
	 * @return
	 */
	public static String getNextADIYearError(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getNextADIYearStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	/**
	 * Web utility returns HTML code for an error or warning icon
	 * 
	 * @param item
	 * @return
	 */
	public static String getTypeError(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getTypeStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	/**
	 * Web utility returns HTML code for an error or warning icon
	 * 
	 * @param item
	 * @return
	 */
	public static String getIncreaseError(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getIncreaseStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	/**
	 * Web utility returns HTML code for an error or warning icon
	 * 
	 * @param item
	 * @return
	 */
	public static String getLimitError(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getLimitStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	/**
	 * Identifies the required HTML code
	 * 
	 * @param errorStatus
	 * @return
	 */
	private static String getErrorIndicator(int errorStatus) {
		if (errorStatus == DeferralDetails.WARNING) {
			return com.manulife.pension.ps.web.Constants.WARNING_ICON_NO_TITLE;
		} else if (errorStatus == DeferralDetails.ERROR) {
			return com.manulife.pension.ps.web.Constants.ERROR_ICON_NO_TITLE;
		} else {
			return "";
		}
	}

	public static String getIncreaseType(DeferralDetails item) {
		StringBuffer addressHTML = new StringBuffer();

		String errorIndicator = getErrorIndicator(item.getLimitStatus());
		if (errorIndicator != null) {
			addressHTML.append(errorIndicator);
		}

		return addressHTML.toString();
	}

	public static void setContractServiceFeatureValues(int contractNumber, DeferralReportForm form) throws ApplicationException, SystemException, Exception {

		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();

		// AE - Start
		ContractServiceFeature csf = delegate.getContractServiceFeature(contractNumber, AUTO_ENROLLMENT);
		if (csf != null) {
			if (YES_ACI.equalsIgnoreCase(csf.getValue())) {
				form.setAutoEnrollmentEnabled(true);
			} else {
				form.setAutoEnrollmentEnabled(false);
			}
		}
		// AE - End

		// MD - Start
		csf = delegate.getContractServiceFeature(contractNumber, Constants.MANAGING_DEFERRALS);
		if (csf != null) {
			form.setPlanDeferralType(csf.getAttributeValue(Constants.medDeferralType));
			//form.setPlanLimitPercent(csf.getAttributeValue(Constants.medDeferralPlanLimitByPercent));
			
			int optOutNumberOfDays = 0;
			String optOutDays = csf.getAttributeValue(OPT_OUT);
			try {
				optOutNumberOfDays = Integer.parseInt(optOutDays);
			} catch (NumberFormatException e) {
				// It should be un-reacheable
			}
			form.setOptOutDays(optOutNumberOfDays);
		}
		// MD - Start

		// ACI - Start
		csf = delegate.getContractServiceFeature(contractNumber, Constants.AUTO_CONTRIBUTION);
		if (csf != null) {
			form.setAciDefaultAnniversaryDate(delegate.getContractAnniversaryDate(contractNumber));
		}

		// Get the ACI Signup Method
		String aciSignupMethod = delegate.determineSignUpMethod(contractNumber);
		aciSignupMethod = aciSignupMethod != null ? aciSignupMethod : StringUtils.EMPTY;
		form.setAciSignupMethod(aciSignupMethod);

		if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(form.getAciSignupMethod()) || ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod()))
			form.setACIOff(false);
		else
			form.setACIOff(true);

		// Get the Plan Page related data
		PlanDataLite planData = delegate.getPlanDataLight(contractNumber);
		if (planData != null) {
			form.setPlanEmpDefElection(planData.getPlanEmployeeDeferralElection());
			form.setPlanYearEnd(planData.getPlanYearEnd());

			if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(form.getAciSignupMethod()))
				form.setPlanAciAnnualApplyDate(planData.getAciAnnualApplyDate());
			else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod()))
				form.setPlanAciAnnualApplyDate(planData.getPlanYearEnd());

			if (form.getPlanEmpDefElection() != null) {
				PlanEmployeeDeferralElection planEmpDefElection = form.getPlanEmpDefElection();
				List<DayOfYear> allowedAnniversaryMonthDay = PlanEmployeeDeferralElection.getCalculatedEmployeeDeferralElectionDays(form.getPlanYearEnd(), planEmpDefElection
						.getEmployeeDeferralElectionCode(), planEmpDefElection.getEmployeeDeferralElectionSelectedDay(), planEmpDefElection.getEmployeeDeferralElectionSelectedMonths());
				form.setAllowedAnniversaryMonthDay(allowedAnniversaryMonthDay);
			}
			
			String irsplanDefLimit = (planData.getIrsAnnualMaximums()!= null && 
					planData.getIrsAnnualMaximums().getIrsAnnualRegularMaximumAmount() != null) 
					? planData.getIrsAnnualMaximums().getIrsAnnualRegularMaximumAmount().toPlainString():StringUtils.EMPTY;
			BigDecimal catchupAmt = (YES_ACI.equals(planData.getCatchUpContributionsAllowed()) &&
					planData.getIrsAnnualMaximums()!= null && 
					planData.getIrsAnnualMaximums().getIrsAnnualCatchUpAmount() != null)
					? planData.getIrsAnnualMaximums().getIrsAnnualCatchUpAmount() : new BigDecimal(0);
			form.setCatchupContriAmt(catchupAmt);
			String planContributionLimitAmt = (planData.getDeferralMaxAmount() != null ? planData.getDeferralMaxAmount().toString() : irsplanDefLimit);
			
			form.setPlanLimitAmount(planContributionLimitAmt);
			form.setPlanLimitPercent((planData.getDeferralMaxPercent()!= null) ? StringUtils.EMPTY + planData.getDeferralMaxPercent().intValue() : StringUtils.EMPTY);
		}

		if (csf != null && planData != null) {
			if (PERCENT.equals(form.getPlanDeferralType()) || EITHER.equals(form.getPlanDeferralType())) {
				if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(form.getAciSignupMethod())) {
					form.setAciDefaultDeferralLimitByPercent(planData.getAciDefaultAutoIncreaseMaxPercent() != null ? StringUtils.EMPTY + planData.getAciDefaultAutoIncreaseMaxPercent().intValue() : StringUtils.EMPTY);
					form.setAciDefaultDeferralIncreaseByPercent(planData.getAciDefaultIncreasePercent() != null ? StringUtils.EMPTY + planData.getAciDefaultIncreasePercent().intValue() : StringUtils.EMPTY);
				} else if (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod())) {
					form.setAciDefaultDeferralLimitByPercent(csf.getAttributeValue(Constants.aciDefaultDeferralLimitByPercent));
					form.setAciDefaultDeferralIncreaseByPercent(csf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByPercent));
				}
			}
			if (DOLLAR.equals(form.getPlanDeferralType()) || EITHER.equals(form.getPlanDeferralType())) {
				form.setAciDefaultDeferralIncreaseByAmount(csf.getAttributeValue(Constants.aciDefaultAnnualIncreaseByAmount));
				form.setAciDefaultDeferralLimitByAmount(csf.getAttributeValue(Constants.aciDefaultDeferralLimitByAmount));
			}
		}
		// ACI - Start
		form.calculateDNI();
	}

	public static boolean isEZstartOn(int contractNumber) throws ApplicationException, SystemException {

		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
		ContractServiceFeature csf = delegate.getContractServiceFeature(contractNumber, AUTO_ENROLLMENT);

		if ((csf != null) && (YES_ACI.equalsIgnoreCase(csf.getValue()))) {
			return true;
		}

		return false;
	}

	/**
	 * Uses business rules to determine the content of the Roth flag
	 * 
	 * @param details
	 * @return
	 */
	public static void calculateAutoIncreaseLimitAlert(DeferralDetails details, String aciDefaultDeferralLimitByAmount, String aciDefaultDeferralLimitByPercent, String planLimitAmount,
			String planLimitPercent) {
		if ((details.getBeforeTaxDeferralPct() == null || "".equals(details.getBeforeTaxDeferralPct().trim()))
				&& (details.getDesignatedRothDeferralAmt() == null || "".equals(details.getDesignatedRothDeferralAmt().trim()))) {
			details.setAutoIncreaseLimitAlert(null);
		} else {
			if (isEELimitReached(details, aciDefaultDeferralLimitByAmount, aciDefaultDeferralLimitByPercent)) {
				details.setAutoIncreaseLimitAlert("EE limit reached");
			}
			if (isERLimitReached(details, aciDefaultDeferralLimitByAmount, aciDefaultDeferralLimitByPercent, planLimitAmount, planLimitPercent)) {
				details.setAutoIncreaseLimitAlert("ER limit reached");
			}
			details.setAutoIncreaseLimitAlert(null);
		}
	}

	private static boolean isEELimitReached(DeferralDetails details, String aciDefaultDeferralLimitByAmount, String aciDefaultDeferralLimitByPercent) {
		Double traditionalDeferral = null;
		Double increaseAmount = null;
		Double contractDefaultDeferralLimit = null;
		Double employeeLimit = null;
		double sum = 0;

		if (Constants.DEFERRAL_TYPE_PERCENT.equals(details.getIncreaseType())) {
			if (details.getLimitPct() != null && !"".equals(details.getLimitPct())) {
				try {
					employeeLimit = Double.parseDouble(details.getLimitPct());
				} catch (NumberFormatException e) {
					employeeLimit = new Double(0);
					// It should never happen
				}
			} else {
				// It should never happen
				employeeLimit = new Double(0);
			}

			if (aciDefaultDeferralLimitByPercent != null && !"".equals(aciDefaultDeferralLimitByPercent)) {
				try {
					contractDefaultDeferralLimit = Double.parseDouble(aciDefaultDeferralLimitByPercent);
				} catch (NumberFormatException e) {
					contractDefaultDeferralLimit = new Double(0);
					// It should never happen
				}
			} else {
				contractDefaultDeferralLimit = new Double(0);
			}

			if (details.getBeforeTaxDeferralPct() != null && !"".equals(details.getBeforeTaxDeferralPct())) {
				try {
					traditionalDeferral = Double.parseDouble(details.getBeforeTaxDeferralPct());
				} catch (NumberFormatException e) {
					traditionalDeferral = new Double(0);
					// It should never happen
				}
				if (details.getIncreasePct() != null && !"".equals(details.getIncreasePct())) {
					try {
						increaseAmount = Double.parseDouble(details.getIncreasePct());
					} catch (NumberFormatException e) {
						increaseAmount = new Double(0);
						// It should never happen
					}

					sum = traditionalDeferral.doubleValue() + increaseAmount.doubleValue();
					if (sum > employeeLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				} else {
					// use contract value
					sum = traditionalDeferral.doubleValue() + contractDefaultDeferralLimit.doubleValue();
					if (sum > employeeLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		} else {
			if (details.getLimitAmt() != null && !"".equals(details.getLimitAmt())) {
				try {
					employeeLimit = Double.parseDouble(details.getLimitAmt());
				} catch (NumberFormatException e) {
					employeeLimit = new Double(0);
					// It should never happen
				}
			} else {
				// It should never happen
				employeeLimit = new Double(0);
			}

			if (aciDefaultDeferralLimitByAmount != null && !"".equals(aciDefaultDeferralLimitByAmount)) {
				try {
					contractDefaultDeferralLimit = Double.parseDouble(aciDefaultDeferralLimitByAmount);
				} catch (NumberFormatException e) {
					contractDefaultDeferralLimit = new Double(0);
					// It should never happen
				}
			} else {
				contractDefaultDeferralLimit = new Double(0);
			}

			if (details.getBeforeTaxDeferralAmt() != null && !"".equals(details.getBeforeTaxDeferralAmt())) {
				try {
					traditionalDeferral = Double.parseDouble(details.getBeforeTaxDeferralAmt());
				} catch (NumberFormatException e) {
					traditionalDeferral = new Double(0);
					// It should never happen
				}
				if (details.getIncreaseAmt() != null && !"".equals(details.getIncreaseAmt())) {
					try {
						increaseAmount = Double.parseDouble(details.getIncreaseAmt());
					} catch (NumberFormatException e) {
						increaseAmount = new Double(0);
						// It should never happen
					}

					sum = traditionalDeferral.doubleValue() + increaseAmount.doubleValue();
					if (sum > employeeLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				} else {
					// use contract value
					sum = traditionalDeferral.doubleValue() + contractDefaultDeferralLimit.doubleValue();
					if (sum > employeeLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		}

	}

	private static boolean isERLimitReached(DeferralDetails details, String aciDefaultDeferralLimitByAmount, String aciDefaultDeferralLimitByPercent, String planLimitAmount, String planLimitPercent) {
		Double traditionalDeferral = null;
		Double increaseAmount = null;
		Double contractDefaultDeferralLimit = null;
		Double planLimit = null;
		double sum = 0;

		if (Constants.DEFERRAL_TYPE_PERCENT.equals(details.getIncreaseType())) {
			if (planLimitPercent != null && !"".equals(planLimitPercent)) {
				try {
					planLimit = Double.parseDouble(planLimitPercent);
				} catch (NumberFormatException e) {
					planLimit = new Double(0);
					// It should never happen
				}
			} else {
				planLimit = new Double(0);
			}

			if (aciDefaultDeferralLimitByPercent != null && !"".equals(aciDefaultDeferralLimitByPercent)) {
				try {
					contractDefaultDeferralLimit = Double.parseDouble(aciDefaultDeferralLimitByPercent);
				} catch (NumberFormatException e) {
					contractDefaultDeferralLimit = new Double(0);
					// It should never happen
				}
			} else {
				contractDefaultDeferralLimit = new Double(0);
			}

			if (details.getBeforeTaxDeferralPct() != null && !"".equals(details.getBeforeTaxDeferralPct())) {
				try {
					traditionalDeferral = Double.parseDouble(details.getBeforeTaxDeferralPct());
				} catch (NumberFormatException e) {
					traditionalDeferral = new Double(0);
					// It should never happen
				}
				if (details.getIncreasePct() != null && !"".equals(details.getIncreasePct())) {
					try {
						increaseAmount = Double.parseDouble(details.getIncreasePct());
					} catch (NumberFormatException e) {
						increaseAmount = new Double(0);
						// It should never happen
					}

					sum = traditionalDeferral.doubleValue() + increaseAmount.doubleValue();
					if (sum > planLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				} else {
					// use contract value
					sum = traditionalDeferral.doubleValue() + contractDefaultDeferralLimit.doubleValue();
					if (sum > planLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			}
			return false;
		} else {
			if (planLimitAmount != null && !"".equals(planLimitAmount)) {
				try {
					planLimit = Double.parseDouble(planLimitAmount);
				} catch (NumberFormatException e) {
					planLimit = new Double(0);
					// It should never happen
				}
			} else {
				planLimit = new Double(0);
			}

			if (aciDefaultDeferralLimitByAmount != null && !"".equals(aciDefaultDeferralLimitByAmount)) {
				try {
					contractDefaultDeferralLimit = Double.parseDouble(aciDefaultDeferralLimitByAmount);
				} catch (NumberFormatException e) {
					contractDefaultDeferralLimit = new Double(0);
					// It should never happen
				}
			} else {
				contractDefaultDeferralLimit = new Double(0);
			}

			if (details.getBeforeTaxDeferralAmt() != null && !"".equals(details.getBeforeTaxDeferralAmt())) {
				try {
					traditionalDeferral = Double.parseDouble(details.getBeforeTaxDeferralAmt());
				} catch (NumberFormatException e) {
					traditionalDeferral = new Double(0);
					// It should never happen
				}
				if (details.getIncreaseAmt() != null && !"".equals(details.getIncreaseAmt())) {
					try {
						increaseAmount = Double.parseDouble(details.getIncreaseAmt());
					} catch (NumberFormatException e) {
						increaseAmount = new Double(0);
						// It should never happen
					}

					sum = traditionalDeferral.doubleValue() + increaseAmount.doubleValue();
					if (sum > planLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				} else {
					// use contract value
					sum = traditionalDeferral.doubleValue() + contractDefaultDeferralLimit.doubleValue();
					if (sum > planLimit.doubleValue()) {
						return true;
					} else {
						return false;
					}
				}
			}

			return false;
		}

	}

	/**
	 * Utility used to compare a date with current date, not using the time
	 * 
	 * @param date
	 * @return
	 */
	public static boolean afterCurrentDate(Date date) {
		Calendar cal = new GregorianCalendar();
		Calendar currentCal = new GregorianCalendar();
		cal.setTime(date);

		if (cal.get(Calendar.YEAR) > currentCal.get(Calendar.YEAR)) {
			return true;
		} else if (cal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR)) {
			if (cal.get(Calendar.MONTH) > currentCal.get(Calendar.MONTH)) {
				return true;
			} else if (cal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
				if (cal.get(Calendar.DAY_OF_MONTH) >= currentCal.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Utility used to compare a date with current date, not using the time
	 * 
	 * @param date
	 * @return
	 */
	public static boolean beforeCurrentDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal = DateUtils.truncate(cal, Calendar.DATE);

		Calendar currentCal = DateUtils.truncate(new GregorianCalendar(), Calendar.DATE);
		return cal.before(currentCal);
	}

	/**
	 * Utility used to compare two dates, just for month and day fields
	 * 
	 * @param date
	 * @return
	 */
	public static boolean monthAndDayBeforeCurrentDate(Date date) {
		Calendar cal = new GregorianCalendar();
		Calendar currentCal = new GregorianCalendar();
		currentCal.setTime(new Date());

		if (cal.get(Calendar.MONTH) < currentCal.get(Calendar.MONTH)) {
			return true;
		} else if (cal.get(Calendar.MONTH) == currentCal.get(Calendar.MONTH)) {
			if (cal.get(Calendar.DAY_OF_MONTH) < currentCal.get(Calendar.DAY_OF_MONTH)) {
				return true;
			}
		}

		return false;
	}

	public static String getPlanDeferralLimitMessage(DeferralReportForm form) {
		StringBuffer buffer = new StringBuffer();

		if (Constants.DEFERRAL_TYPE_PERCENT.equals(form.getPlanDeferralType())) {
			buffer.append("Plan limit is ");
			if (form.getPlanLimitPercent() == null || form.getPlanLimitPercent().trim().length() == 0) {
				buffer.append("not on file.");
			} else {
				buffer.append(form.getPlanLimitPercent());
				buffer.append("%");
			}
		} else if (Constants.DEFERRAL_TYPE_DOLLAR.equals(form.getPlanDeferralType())) {
			buffer.append("Plan limit is ");
			buffer.append("$");
			buffer.append(form.getPlanLimitAmount());
		} else if (Constants.DEFERRAL_TYPE_EITHER.equals(form.getPlanDeferralType())) {
			buffer.append("Your plan max is ");
			buffer.append(form.getPlanLimitPercent());
			buffer.append("%");
			buffer.append(" or $");
			buffer.append(form.getPlanLimitAmount());
		} else {
			buffer.append("<p><font color=\"#990000\"><strong> Not on file </strong></font> <img src=\"/assets/unmanaged/images/warning2.gif\" /></p>");
		}

		return buffer.toString();
	}

	public static String getDefaultDeferralLimitMessage(DeferralReportForm form) {
		StringBuffer buffer = new StringBuffer(StringUtils.EMPTY);

		if (form.isACIOff()) {
			return buffer.toString();
		}

		if (Constants.DEFERRAL_TYPE_PERCENT.equals(form.getPlanDeferralType())) {
			buffer.append("Default deferral limit is ");
			buffer.append(form.getAciDefaultDeferralLimitByPercent());
			buffer.append("%");
		} else if (Constants.DEFERRAL_TYPE_DOLLAR.equals(form.getPlanDeferralType())) {
			buffer.append("Default deferral limit is ");
			buffer.append("$");
			buffer.append(form.getAciDefaultDeferralLimitByAmount());
		} else if (Constants.DEFERRAL_TYPE_EITHER.equals(form.getPlanDeferralType())) {
			buffer.append("Your default deferral limit is ");
			buffer.append(form.getAciDefaultDeferralLimitByPercent());
			buffer.append("%");
			buffer.append(" or $");
			buffer.append(form.getAciDefaultDeferralLimitByAmount());
		}

		return buffer.toString();
	}

	public static String getDefaultDeferralIncreaseMessage(DeferralReportForm form) {
		StringBuffer buffer = new StringBuffer(StringUtils.EMPTY);

		if (form.isACIOff()) {
			return buffer.toString();
		}

		if (Constants.DEFERRAL_TYPE_PERCENT.equals(form.getPlanDeferralType())) {
			buffer.append("Default increase amount is ");
			buffer.append(form.getAciDefaultDeferralIncreaseByPercent());
			buffer.append("%");
		} else if (Constants.DEFERRAL_TYPE_DOLLAR.equals(form.getPlanDeferralType())) {
			buffer.append("Default increase amount is ");
			buffer.append("$");
			buffer.append(form.getAciDefaultDeferralIncreaseByAmount());
		} else if (Constants.DEFERRAL_TYPE_EITHER.equals(form.getPlanDeferralType())) {
			buffer.append("Your default deferral increase is ");
			buffer.append(form.getAciDefaultDeferralIncreaseByPercent());
			buffer.append("%");
			buffer.append(" or $");
			buffer.append(form.getAciDefaultDeferralIncreaseByAmount());
		}

		return buffer.toString();
	}

	public static String getNextADIMessage(DeferralReportForm form, DeferralDetails element) {
		double btdPct = 0;
		double inc = 0;
		double contrIncPct = 0;
		double btdAmt = 0;
		double contrIncAmt = 0;
		double contrLimPct = 0;
		double contrLimAmt = 0;
		double lim = 0;
		String message = "";
		String incType = "%";

		// Whether a PPT has personalized its values or not
		boolean isPptIncreaseAmountFound = false;
		boolean isPptLimitFound = false;

		if (element.getBeforeTaxDeferralPct() != null && element.getBeforeTaxDeferralPct().trim().length() > 0) {
			btdPct = Double.valueOf(element.getBeforeTaxDeferralPct());
			btdPct = btdPct * 100;
		}

		if (element.getIncrease() != null && element.getIncrease().trim().length() > 0) {
			inc = Double.valueOf(element.getIncrease());
			if (inc > 0) {
				isPptIncreaseAmountFound = true;
			}
		}

		if (element.getLimit() != null && element.getLimit().trim().length() > 0) {
			lim = Double.valueOf(element.getLimit());

			if (lim > 0) {
				// PPT has given personalized Limit (use this value to display
				// limit reached message)
				isPptLimitFound = true;
			}
		}

		if (form.getAciDefaultDeferralIncreaseByPercent() != null && form.getAciDefaultDeferralIncreaseByPercent().trim().length() > 0) {
			contrIncPct = Double.valueOf(form.getAciDefaultDeferralIncreaseByPercent());
		}
		if (element.getBeforeTaxDeferralAmt() != null && element.getBeforeTaxDeferralAmt().trim().length() > 0) {
			btdAmt = Double.valueOf(element.getBeforeTaxDeferralAmt());
		}

		if (form.getAciDefaultDeferralIncreaseByAmount() != null && form.getAciDefaultDeferralIncreaseByAmount().trim().length() > 0) {
			contrIncAmt = Double.valueOf(form.getAciDefaultDeferralIncreaseByAmount());
		}
		if (form.getAciDefaultDeferralLimitByPercent() != null && form.getAciDefaultDeferralLimitByPercent().trim().length() > 0) {
			contrLimPct = Double.valueOf(form.getAciDefaultDeferralLimitByPercent());
		}

		if (form.getAciDefaultDeferralLimitByAmount() != null && form.getAciDefaultDeferralLimitByAmount().trim().length() > 0) {
			contrLimAmt = Double.valueOf(form.getAciDefaultDeferralLimitByAmount());
		}

		if (element.getIncreaseType() != null && element.getIncreaseType().trim().length() > 0) {
			incType = element.getIncreaseType();
		}

		// Defect 2930 (SG):
		// If a PPT has personalized limits, then use those values to check
		// whether the limit
		// has been crossed or not, otherwise use the Contract's limits
		if ("%".equals(incType)) {
			message = getLimitReachedMessageIfAny(inc, lim, isPptLimitFound, isPptIncreaseAmountFound, btdPct, contrIncPct, contrLimPct);
		} else {
			message = getLimitReachedMessageIfAny(inc, lim, isPptLimitFound, isPptIncreaseAmountFound, btdAmt, contrIncAmt, contrLimAmt);
		}

		return message;
	}

	/**
	 * Checks participant's increaseAmount (or Percent), if given, with either
	 * participant's limit (if given) or contract limit, otherwise checks
	 * contract increase with either participant's limit (if given) or contract
	 * limit.
	 * 
	 * <pre>
	 *    If a personalized limit does not exist, then we should be using the contract default
	 *    limit to govern the increases.  CIS175 should trump (override) CIS174 when there is
	 *    a personalized limit
	 *    
	 *    CIS.174   If the participant EZincrease = ‘On’ and If the sum of the
	 *      Before tax deferral plus (the Increase amount or the contract deferral
	 *      increase amount, if the Increase amount is blank) is greater than the
	 *      Contract default deferral limit for a listed employee then 
	 *      Display “ER limit reached” text on the second line for the listed employee.
	 *    CIS.175   If the participant EZincrease = ‘On’ and If the sum of the Before
	 *      tax deferral plus (the Increase amount or the contract deferral increase
	 *      amount, if the Increase amount is blank) is greater than the Limit for a
	 *      listed employee then Display “EE limit reached” text on the second line
	 *      for the listed employee.
	 * </pre>
	 * 
	 * @param pptIncreaseAmount
	 * @param pptLimit
	 * @param isPptLimitFound
	 * @param isPptIncreaseAmountFound
	 * @param beforeTaxDeferralValue
	 * @param contractDefaultDeferralIncrease
	 * @param contractDefaultDeferralLimit
	 * @return
	 * 
	 */
	private static String getLimitReachedMessageIfAny(double pptIncreaseAmount, double pptLimit, boolean isPptLimitFound, boolean isPptIncreaseAmountFound, double beforeTaxDeferralValue,
			double contractDefaultDeferralIncrease, double contractDefaultDeferralLimit) {

		String message = null;
		if (isPptLimitFound) { // Use PPT limits, otherwise use Contract's
			// limits
			if (isPptIncreaseAmountFound) { // Use PPT increase amount for check
				if (beforeTaxDeferralValue + pptIncreaseAmount > pptLimit) {
					message = EE_LIMIT_REACHED;
				}
			} else if (beforeTaxDeferralValue + contractDefaultDeferralIncrease > pptLimit) {
				// Used contract Increase Percentage for check
				message = EE_LIMIT_REACHED;
			}
		} else { // PPT limits are not provided => use contract's limits
			if (isPptIncreaseAmountFound) { // Use PPT increase amount for check
				if (beforeTaxDeferralValue + pptIncreaseAmount > contractDefaultDeferralLimit) {
					message = ER_LIMIT_REACHED;
				}
			} else if (beforeTaxDeferralValue + contractDefaultDeferralIncrease > contractDefaultDeferralLimit) {
				// Used contract Increase Percentage for check
				message = ER_LIMIT_REACHED;
			}
		}

		return message;
	}

	/**
	 * Method that uses the business rules outlined in TPA + PSW access contrl
	 * matrix document to determine if the access to Deferral tab is permited
	 * 
	 * @param userProfile
	 * @return
	 */
	public static boolean isAllowedToAccessDeferrals(UserProfile userProfile) {
		/*
		 * if(userProfile.isInternalUser()) { return true; } else
		 * if(!PS_CONTRACT_STATUS
		 * .equalsIgnoreCase(userProfile.getCurrentContract().getStatus()) &&
		 * !PC_CONTRACT_STATUS
		 * .equalsIgnoreCase(userProfile.getCurrentContract().getStatus()) &&
		 * !DC_CONTRACT_STATUS
		 * .equalsIgnoreCase(userProfile.getCurrentContract().getStatus())) {
		 * return true; }
		 */

		return true;
	}

	public static boolean isAutoIncreaseChanged(DeferralReportForm form, DeferralDetails detail) {
		if (detail.getIncrease() == null && detail.getLimit() == null &&
				(detail.getAciSettingsInd() == null || StringUtils.EMPTY.equals(detail.getAciSettingsInd().trim()) || Constants.NO.equals(detail.getAciSettingsInd()))
				&& ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(form.getAciSignupMethod())) {
			return false;
		}

		return true;
	}

	public static Date getOptOutDate(int nrOfDays, Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -nrOfDays);
		return cal.getTime();
	}

	public static Date getDateNextADIFromClonedElement(String profileId, DeferralReportForm form) {
		List<DeferralDetails> list = ((DeferralReportForm) form.getClonedForm()).getTheItemList();

		if (list != null && list.size() > 0 && profileId != null) {
			for (DeferralDetails details : list) {
				if (profileId.equalsIgnoreCase(details.getProfileId())) {
					return details.getDateNextADI();
				}
			}
		}
		return null;
	}

	public static DeferralStatisticsSummary getStatisticsSummary(int contractId) throws SystemException {
		DeferralStatisticsSummary summary = DeferralSummaryDAO.getStatisticsSummary(contractId);
		return summary;
	}
}