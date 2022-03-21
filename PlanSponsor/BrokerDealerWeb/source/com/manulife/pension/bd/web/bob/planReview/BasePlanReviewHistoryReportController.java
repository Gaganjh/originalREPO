package com.manulife.pension.bd.web.bob.planReview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This is base action class used for Plan Review report History pages.
 * 
 * @author akarave
 * 
 */
@Controller

public abstract class BasePlanReviewHistoryReportController extends BOBReportController {

	protected static String DEFAULT_SORT = "contractName";
	protected static String DEFAULT_SORT_DIRECTION = "asc";

	protected static final Logger logger = Logger.getLogger(BasePlanReviewHistoryReportController.class);

	/**
	 * Constructor class.
	 */
	public BasePlanReviewHistoryReportController(Class clazz) {
		super(clazz);
	}

	/**
	 * The preExecute method has been overriden to see if the contractNumber is
	 * coming as part of request parameter. If the contract Number is coming as
	 * part of request parameter, the BobContext will be setup with contract
	 * information of the contract number passed in the request parameter.
	 * 
	 */
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		String forward = null;

		// verify whether User have access to PlanReview Functionality
		if (!isUserHaveAccessToPlanReviewFunctionality(request)) {
			forward = BDConstants.FORWARD_PLAN_REVIEW_REPORTS_UNAVAILABLE_PAGE;
		}

		if (forward == null) {
			

			/*
			 * Code To Handle Irregular Navigation using combination of Form
			 * Parameters,Request Parameter and Session Attribute
			 */
			BasePlanReviewReportForm planReviewReportBaseForm = (BasePlanReviewReportForm) form;

			if (Boolean.FALSE.equals(StringUtils
					.isBlank(planReviewReportBaseForm
							.getPageRegularlyNavigated()) ? Boolean.FALSE
					: Boolean.valueOf(planReviewReportBaseForm
							.getPageRegularlyNavigated()))
					&& Boolean.FALSE
							.equals(StringUtils.isBlank(request
									.getParameter(BDConstants.PAGE_REGULARLY_NAVIGATED_IND)) ? Boolean.FALSE
									: Boolean.valueOf(request
											.getParameter(BDConstants.PAGE_REGULARLY_NAVIGATED_IND)))) {

				if (!Boolean.TRUE
						.equals(request.getSession(false).getAttribute(
								BDConstants.PAGE_REGULARLY_NAVIGATED_IND))) {
					
					BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
					
					if(userProfile.isInternalUser()) {
						return "homePage";
					}
					
					return BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE;
				} else {

					request.getSession(false).removeAttribute(
							BDConstants.PAGE_REGULARLY_NAVIGATED_IND);
				}
			}

			// set to default
			planReviewReportBaseForm
					.setPageRegularlyNavigated(Boolean.FALSE.toString());

			/*if (!StringUtils.equalsIgnoreCase(BDConstants.PR_BOB_LEVEL_PARAMETER, mapping.getParameter())) {
				super.preExecute( form, request, response);
			}*/
		}

		return forward;
	}

	/**
	 * This method is used to set Session parameter to handle Irregular
	 * navigation
	 * 
	 * @param request
	 */

	protected void setRegularPageNavigation(HttpServletRequest request) {

		request.getSession(false).setAttribute(
				BDConstants.PAGE_REGULARLY_NAVIGATED_IND, Boolean.TRUE);

	}

	/**
	 * This method is used to verify whether User have access to PlanReview
	 * Functionality
	 * 
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private boolean isUserHaveAccessToPlanReviewFunctionality(
			HttpServletRequest request) throws SystemException {

		return isPlanReviewFunctionalityAvailable();
	}

	/**
	 * This method is used to check the plan Review operational indicator
	 * navigation
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @throws SystemException
	 */

	private boolean isPlanReviewFunctionalityAvailable() throws SystemException {

		BaseEnvironment environment = new BaseEnvironment();

		String isplanReviewFunctionalityOprational = environment
				.getNamingVariable(
						BDConstants.PLAN_REVIEW_REPORT_AVAILABILITY_NAMING_VARIABLE,
						null);

		if (StringUtils.isBlank(isplanReviewFunctionalityOprational)) {
			throw new IllegalArgumentException(
					"invalid value for the naming variable: "
							+ BDConstants.PLAN_REVIEW_REPORT_AVAILABILITY_NAMING_VARIABLE);
		}

		return Boolean.valueOf(isplanReviewFunctionalityOprational);

	}

	public boolean isPlanReviewAdminUser(HttpServletRequest request)
			throws SystemException {

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		long userProfileId = userProfile.getBDPrincipal().getProfileId();

		String isPlanReviewAdminUser = String.valueOf(request.getSession(false)
				.getAttribute(BDConstants.IS_PLAN_REVIEW_ADMIN_USER));

		if (StringUtils.isBlank(isPlanReviewAdminUser)) {

			boolean isUserhavePlanReviewPermissions = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.isUserPermissionsForPlanReviewReport(userProfileId);

			isPlanReviewAdminUser = String
					.valueOf(isUserhavePlanReviewPermissions);

			request.getSession(false).setAttribute(
					BDConstants.IS_PLAN_REVIEW_ADMIN_USER,
					Boolean.valueOf(isUserhavePlanReviewPermissions));

		}

		return Boolean.valueOf(isPlanReviewAdminUser);

	}

	/**
	 * Help method check if current contract is defined benefit contract
	 * 
	 * @return
	 */
	protected boolean isDefinedBenefitContract(String productId) {

		if (Contract.DEFINED_BENEFIT_CONTRACT_PRODUCT_CODE_US.equals(productId)
				|| Contract.DEFINED_BENEFIT_CONTRACT_PRODUCT_CODE_NY
						.equals(productId))

			return true;
		else
			return false;
	}

	protected void setDefaultPlansponsorMagazineIndustryCodeForContracts(
			List<PlanReviewReportUIHolder> reportList) throws SystemException {

		List<Integer> contractList = new ArrayList<Integer>();

		for (PlanReviewReportUIHolder uiHolder : reportList) {
			contractList.add(uiHolder.getContractNumber());
		}

		Map<String, String> contractDefaultIndustryCodeMap = new HashMap<String, String>();

		if (!contractList.isEmpty()) {
			contractDefaultIndustryCodeMap = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.getDefaultPlansponsorMagazineIndustryCodesForContract(
							contractList);
		}

		for (PlanReviewReportUIHolder uiHolder : reportList) {
			String defaultIndustryCode = contractDefaultIndustryCodeMap
					.get(String.valueOf(uiHolder.getContractNumber()));

			if (StringUtils.isNotBlank(defaultIndustryCode)) {
				uiHolder.setSelectedIndustrySegment(defaultIndustryCode);
			}

		}

	}

	protected String getBrokerId(HttpServletRequest request)
			throws SystemException {

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		String brokerId = StringUtils.EMPTY;

		if (!userProfile.isInternalUser()) {
			// external user
			if (userProfile.getRole() instanceof BDFinancialRep
					|| userProfile.getRole() instanceof BDFinancialRepAssistant) {

				if (userProfile.getRole() instanceof BDFinancialRep) {
					// if User is Financial Rep Level 2
					BDFinancialRep financialRepRole = (BDFinancialRep) userProfile
							.getBDPrincipal().getBDUserRole();
					brokerId = financialRepRole.getPrimary().getSsnTaxId();
				} else {
					// if User is Assistant Financial Rep Level 2
					BDAssistantUserProfile assistantUserProfile = (BDAssistantUserProfile) userProfile;
					BDFinancialRep financialRepRole = (BDFinancialRep) assistantUserProfile
							.getParentPrincipal().getBDUserRole();
					brokerId = financialRepRole.getPrimary().getSsnTaxId();
				}

			} else {
				// means a user other than Financial Rep Level 2 or Assistant
				// trying to request
				throw new SystemException(
						"Illegal Access to Plan Review Request pages by External user: "
								+ userProfile);
			}

		}

		return brokerId;
	}
	
	
	
	/**
     * Populate sort criteria in the criteria object using the given FORM. The
     * default implementation inserts the FORM's sort field and sort direction.
     * Override this if more sorting is required.
     *
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        
    	if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            /*if (!form.getSortField().equals(getDefaultSort())) {
                criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
            }*/
        } else  {
        	criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
        }
    }
	
	

	protected PlanReviewReportUIHolder getPlanReviewReportUIHolder(
			String contractNumber,
			List<PlanReviewReportUIHolder> reportUIHolderList) {

		if (StringUtils.isBlank(contractNumber)
				|| !StringUtils.isNumeric(contractNumber)) {
			throw new IllegalArgumentException("Invalid contract Number : "
					+ contractNumber);
		}

		PlanReviewReportUIHolder planReviewReportUIHolder = new PlanReviewReportUIHolder();
		planReviewReportUIHolder.setContractNumber(Integer
				.parseInt(contractNumber));

		int index = reportUIHolderList.indexOf(planReviewReportUIHolder);

		if (index == -1) {
			throw new IllegalArgumentException(
					"Invalid contract Number in list : " + contractNumber);
		}

		planReviewReportUIHolder = reportUIHolderList.get(index);

		return planReviewReportUIHolder;
	}

}