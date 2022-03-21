package com.manulife.pension.bd.web.bob.planReview;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.planReview.sort.PalnReviewReportStep1PageColumn;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.BusinessUnit;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.Division;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentFormat;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentStatus;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.PartyIdentifier;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductSubType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductType;
import com.manulife.pension.service.planReview.valueobject.ActivityEventVo;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentPackage;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventStatus;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.util.render.RenderConstants;

/**
 * This is base action class used for Plan Review report pages.
 * 
 * @author Vanikishore
 * 
 */
public class BasePlanReviewReportController extends BOBReportController {
	
	protected static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);

	protected static String DEFAULT_SORT = "contractName";
	protected static String DEFAULT_SORT_DIRECTION = "asc";
	
	private static final String BOB_LEVEL_STEP1_LANDING_ACTION_PATH = "/bob/planReview/";
	private static final String CONTRACT_LEVEL_STEP1_LANDING_ACTION_PATH = "/bob/contract/planReview/";

	private static final String[] step1LandingActions = new String[] {
			BOB_LEVEL_STEP1_LANDING_ACTION_PATH,
			CONTRACT_LEVEL_STEP1_LANDING_ACTION_PATH };

	protected static final Logger logger = Logger.getLogger(BasePlanReviewReportController.class);

	/**
	 * Constructor class.
	 */
	public BasePlanReviewReportController(Class clazz) {
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
		
		/**
    	 * Production fix for not to show planReviewReport Link on live
    	 * 
    	 * This code change is to handle, even if the link is not available in live,
    	 * but if the user tried to bookmarks the plan review report link, it redirects to FRW home page.
    	 * 
    	 */
    	if(!PlanReviewReportUtils.isPlanReviewLaunched()) {
        	if(!PlanReviewReportUtils.isPlanReviewFunctionalityAvailable()) {
        		
        		//  if the  plan review launched is 'false' and 
        		//  if the plan review available is false 
        		// -- >  redirects to FRW home page.
        		
        		//return mapping.findForward("homePage");
        		return "homePage";
        	}
    	}

		// verify whether User have access to PlanReview Functionality
		if (!isUserHaveAccessToPlanReviewFunctionality(request)) {
			//forward = mapping.findForward(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_UNAVAILABLE_PAGE);
			return BDConstants.FORWARD_PLAN_REVIEW_REPORTS_UNAVAILABLE_PAGE;
		}
		String mappedPath = new UrlPathHelper().getPathWithinApplication(request);
		// if requested to land on Step 1 page.
		if (Arrays.toString(step1LandingActions).contains(mappedPath)) {
			
			if(DEFAULT_TASK.equalsIgnoreCase(getTask(request))) {
				return forward;
			} 
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
					// if bookmarked Step 1 page.
					if (Arrays.toString(step1LandingActions).contains(mappedPath)) {
						// remove the task parameter and resubmit the Step 1 page.
						
						
						ControllerForward forward1 = new ControllerForward(BDConstants.REFRESH, BDConstants.DO
								+ new UrlPathHelper().getPathWithinServletMapping(request), true);
						return "redirect:"+forward1.getPath();
					}
					
					BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
					
					if(userProfile.isInternalUser()) {
						//return mapping.findForward("homePage");
						return "homePage";
					}
					
					//return mapping.findForward(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE);
					return BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE;
				} else {

					request.getSession(false).removeAttribute(
							BDConstants.PAGE_REGULARLY_NAVIGATED_IND);
				}
			}

			// set to default
			planReviewReportBaseForm
					.setPageRegularlyNavigated(Boolean.FALSE.toString());
			String mapping="bob";
			if (!StringUtils.equalsIgnoreCase(
					BDConstants.PR_BOB_LEVEL_PARAMETER, mapping)) {
				super.preExecute( form, request, response);
			}

		}

		return forward;
	}

	/**
	 * This method will get the required information.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon() in ContractReviewReportsAction");
		}
		PlanReviewReportForm contractReviewReportForm = (PlanReviewReportForm) reportForm;

		populateReportForm( reportForm, request);

		List<PlanReviewReportUIHolder> contractReviewReportVOList = contractReviewReportForm
				.getDisplayContractReviewReports();
		Collections.sort(
				contractReviewReportVOList,
				PalnReviewReportStep1PageColumn
						.getContractReviewReportStep1PageColumn(
								reportForm.getSortField())
						.getComparatorInstance(reportForm.getSortDirection()));

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon() in ContractReviewRequestAction.");
		}

		//return findForward(mapping, getTask(request));
		return findForward(getTask(request));
	}

	/**
	 * @param actualContractReviewReports
	 * @param displayContractReviewReports
	 */
	protected void populateActualContractReviewReports(
			List<PlanReviewReportUIHolder> actualContractReviewReports,
			List<PlanReviewReportUIHolder> displayContractReviewReports) {
		for (PlanReviewReportUIHolder vo : actualContractReviewReports) {
			int index = displayContractReviewReports.indexOf(vo);
			if (index != -1) {
				PlanReviewReportUIHolder modified = displayContractReviewReports
						.get(index);
				vo.setContractSelected(modified.isContractSelected());
				vo.setSelectedIndustrySegment(modified
						.getSelectedIndustrySegment());
				vo.setSelectedperformanceAndExpenseRatio(modified
						.isSelectedperformanceAndExpenseRatio());
				vo.setSelectedReportMonthEndDate(modified
						.getSelectedReportMonthEndDate());
			}
		}
	}

	/**
	 * @param actualContractReviewReports
	 * @param displayContractReviewReports
	 * @throws SystemException
	 */
	protected void populateDisplayContractReviewReports(
			List<PlanReviewReportUIHolder> actualContractReviewReports,
			List<PlanReviewReportUIHolder> displayContractReviewReports)
			throws SystemException {

		displayContractReviewReports.clear();

		for (PlanReviewReportUIHolder vo : actualContractReviewReports) {
			PlanReviewReportUIHolder displayContractReviewReport = vo
					.cloneObject();
			displayContractReviewReport.setIndustrySegementOptions(vo
					.getIndustrySegementOptions());
			displayContractReviewReport.setReportMonthEndDates(vo
					.getReportMonthEndDates());
			displayContractReviewReports.add(displayContractReviewReport);
		}

	}

	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#
	 * populateReportForm(org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		String task = getTask(request);

		/*
		 * Set default sort if we're in default task.
		 */
		if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
				|| reportForm.getSortDirection().length() == 0) {
			reportForm.setSortDirection(getDefaultSortDirection());
		}

		/*
		 * Set default sort direction if we're in default task.
		 */
		if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
				|| reportForm.getSortField().length() == 0) {
			reportForm.setSortField(getDefaultSort());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
	}

	/*
	 * Do not change any of below below methods.
	 */

	@Override
	protected String getReportId() {
		return null;
	}

	@Override
	protected String getReportName() {
		return null;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		// Do nothing

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

		Boolean isPlanReviewAdminUser = (Boolean)(request.getSession(false)
				.getAttribute(BDConstants.IS_PLAN_REVIEW_ADMIN_USER));

		if (isPlanReviewAdminUser == null) {

			boolean isUserhavePlanReviewPermissions = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.isUserPermissionsForPlanReviewReport(userProfileId);

			isPlanReviewAdminUser = Boolean.valueOf(isUserhavePlanReviewPermissions);

			request.getSession(false).setAttribute(
					BDConstants.IS_PLAN_REVIEW_ADMIN_USER,
					Boolean.valueOf(isUserhavePlanReviewPermissions));

		}

		return isPlanReviewAdminUser;

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
		
		} else {
			// internal user
			brokerId = String.valueOf(userProfile.getBDPrincipal()
					.getProfileId());

		}

		return brokerId;
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
	
	protected PlanReviewReportDocumentPackage getPlanReviewDocumentList(
			String userProfileId, ActivityVo getDocListActivity,
			String publishDocActivityId, Date publishDocRequestedDate)
			throws SystemException {

		try {

			return PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.getPlanReviewDocumentList(userProfileId,
							getDocListActivity, publishDocActivityId,
							publishDocRequestedDate);
		
		} catch (Exception exception) {

			logger.error(
					"Exception occured for PlanReview getDocumentList request: ["
							+ getDocListActivity.toString()
							+ "] for publishDocActivityId: "
							+ publishDocActivityId, exception);

			// record the activity event to fail
			PlanReviewServiceDelegate.getInstance(
					new BaseEnvironment().getApplicationId())
					.recordPlanReveiwActivityEvent(
							getDocListActivity.getActivityId(),
							new ActivityEventVo(EventSourceCode.RPS,
									ActivityEventCode.GET_DOC_LIST_END,
									ActivityEventStatus.FAILED,
									StringUtils.substring(exception.getMessage(), 0, 254)));
		}

		return null;
	}
	
	protected void populateTibcoGetDocumentListProperties(
			PlanReviewRequestVO planReviewRequestVo,
			List<PlanReviewReportUIHolder> planReviewReportUIHolders) {
		
		for (ActivityVo actVo : planReviewRequestVo.getActivityVoList()) {
			
			actVo.setDivision(Division.USA);
			actVo.setBusinessUnit(BusinessUnit.RPS);
			actVo.setProductType(ProductType.LIFEINSURANCE);
			actVo.setProductSubType(ProductSubType.VARIABLELIFEINSURANCE);
			
			Map<PartyIdentifier, String> partyIdentifiersMap = new HashMap<PartyIdentifier, String>();
			
			String contractId = String.valueOf(actVo
					.getContractId());
			
			PlanReviewReportUIHolder uiHolder = 
					 getPlanReviewReportUIHolder(contractId, 
							 planReviewReportUIHolders);
			
			partyIdentifiersMap.put(PartyIdentifier.CONTRACTNUMBER, contractId);
			partyIdentifiersMap.put(PartyIdentifier.BROKERID,
					uiHolder.getAgencySellerId());
			partyIdentifiersMap.put(PartyIdentifier.USERID, "rpsuser");
			
			actVo.setPartyIdentifier(partyIdentifiersMap);
			
			actVo.setDocumentFormat(DocumentFormat.PDF);
			actVo.setDocumentType(DocumentType.PLANREVIEW);
			actVo.setDocumentStatus(DocumentStatus.ACTIVE);
			
		}
	}
	
	/**
	 * This method is used to populate populateActivityEvent data.
	 * 
	 * @param PlanReviewReportUIHolder
	 * @param ActivityVo
	 * @throws SystemException
	 */
	protected void populateActivityEvent(ActivityVo activity,
			ActivityEventCode activityEventCode,
			ActivityEventStatus eventStatus, String statusMessage) {
		
		List<ActivityEventVo> activityEventVoList = new ArrayList<ActivityEventVo>();
		ActivityEventVo activityEventVo = new ActivityEventVo();
		activityEventVo.setActivityEventCode(activityEventCode);
		activityEventVo.setActivityEventSourceCode(EventSourceCode.RPS);
		activityEventVo.setActivityEventStatus(eventStatus);
		activityEventVo.setStatusMessage(statusMessage);
		activityEventVo.setCreatedTimeStamp(new Timestamp(System
				.currentTimeMillis()));
		activityEventVoList.add(activityEventVo);
		activity.setActivityEventVoList(activityEventVoList);

	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;

	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	
}