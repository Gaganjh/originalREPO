package com.manulife.pension.bd.web.bob.planReview.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.planReview.CoverPageImage;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportStep2Controller;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.bob.planReview.sort.BaseContractReviewReportComparator;
import com.manulife.pension.bd.web.bob.planReview.sort.BaseHistoryDetailsComparator;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.planReview.jms.PlanReviewRequestMDBUtility;
import com.manulife.pension.service.planReview.jms.PlanReviewRequset;
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
import com.manulife.pension.service.planReview.valueobject.PeriodEndingReportDateVO;
import com.manulife.pension.service.planReview.valueobject.PlanReviewCoverImageDetails;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentPackage;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventStatus;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.PlanReviewConstants.RequstStatusCode;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.RenderConstants;

/**
 * This is a Utility class for Plan Review report pages.
 * 
 * @author R Vanikishore
 * 
 */
public class PlanReviewReportUtils {
	
	private static final Logger logger = Logger
			.getLogger(PlanReviewReportUtils.class);

	protected static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	/**
	 * Constructing Monthly Report dates using DB call ;call initiated from
	 * Action
	 * 
	 * @param contractList
	 * @return Date Map
	 * @throws SystemException
	 */
	public static List<PeriodEndingReportDateVO> getActivePlanReviewPeriodEndDates(
			boolean isAdminUser) throws SystemException {
		return (PlanReviewServiceDelegate.getInstance(Environment.getInstance()
				.getApplicationId())
				.getActivePlanReviewPeriodEndDates(isAdminUser));
	}

	/**
	 * This method would return the UserProfile object of the mimicking user.
	 * 
	 * @return - BDUserProfile object of the mimicking user.
	 */
	public static BDUserProfile getMimckingUserProfile(
			HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> mimickingUserSession = (Map<String, Object>) request
				.getSession(false).getAttribute(
						BDConstants.ATTR_MIMICKING_SESSION);
		if (mimickingUserSession == null) {
			return null;
		}

		BDUserProfile mimickingInternalUserProfile = (BDUserProfile) mimickingUserSession
				.get(BDConstants.USERPROFILE_KEY);

		return mimickingInternalUserProfile;
	}

	/**
	 * getting the comparator instance for sorting in Step1 and Step2 page.
	 * 
	 * @param sortDirection
	 * @param comparatorClass
	 * @return comparatorClass
	 * @throws SystemException
	 */
	public static BaseContractReviewReportComparator getComparatorInstance(

	String sortDirection,
			Class<? extends BaseContractReviewReportComparator> comparatorClass)
			throws SystemException {

		try {

			return comparatorClass.getConstructor(String.class).newInstance(
					sortDirection);

		} catch (InstantiationException e) {
			throw new SystemException(e, "InstantiationException occured for :"
					+ comparatorClass.toString());
		} catch (IllegalAccessException e) {
			throw new SystemException(e, "IllegalAccessException occured for :"
					+ comparatorClass.toString());
		} catch (NoSuchMethodException e) {
			throw new SystemException(e, "NoSuchMethodException occured for :"
					+ comparatorClass.toString());
		} catch (InvocationTargetException e) {
			throw new SystemException(e,
					"InvocationTargetException occured for :"
							+ comparatorClass.toString());
		}
	}

	// @TODO to be replaced with Actual values
	// method to populate shipping details
	public static ShippingVO populateShippingdetails(String brokerID)
			throws SystemException {

		return  PlanReviewServiceDelegate.getInstance(
				Environment.getInstance().getApplicationId())
				.getBrokerDefaultAddress(brokerID);
	}

	/**
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static List<LabelValueBean> getPlanReviewIndustrySegmentList(
			HttpServletRequest request) throws SystemException {

		@SuppressWarnings("unchecked")
		List<LabelValueBean> magazineIndustryDropdownValues = (List<LabelValueBean>) request
				.getSession(false).getAttribute(
						BDConstants.ALL_MAGAZINE_INDUSTRY_DROPDOWN_VALUES);

		if (magazineIndustryDropdownValues == null) {
			magazineIndustryDropdownValues = PlanReviewServiceDelegate.
					getInstance(Environment.getInstance().getApplicationId())
					.getAllMagazineIndustryDropdownValues();
			/*
			 * return
			 * (PlanReviewServiceDelegate.getInstance(Environment.getInstance()
			 * .getApplicationId()).getPlanReviewIndustrySegmentList());
			 */
			request.getSession(false).setAttribute(
					BDConstants.ALL_MAGAZINE_INDUSTRY_DROPDOWN_VALUES,
					magazineIndustryDropdownValues);
		}

		
		return magazineIndustryDropdownValues;

		
	}
	
	/**
	 *  retrieve Plan Review Cover Page Image List
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static List<PlanReviewCoverImageDetails> getPlanReviewCoverPageImageList(
			HttpServletRequest request) throws SystemException {

		@SuppressWarnings("unchecked")
		List<PlanReviewCoverImageDetails> planReviewCoverImageDetails = (List<PlanReviewCoverImageDetails>) request
				.getSession(false).getAttribute(
						BDConstants.PLAN_REVIEW_COVER_PAGE_IMAGE_LIST);

		if (planReviewCoverImageDetails == null) {
			
			planReviewCoverImageDetails = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.getPlanReviewCoverPageImageDetails();
			
			request.getSession(false).setAttribute(
					BDConstants.PLAN_REVIEW_COVER_PAGE_IMAGE_LIST,
					planReviewCoverImageDetails);
		}

		return planReviewCoverImageDetails;
	}


	/**
	 * 
	 * @return
	 * @throws SystemException
	 */
	public static List<LabelValueBean> getNumberOfCopiesDropDownValues()
			throws SystemException {
		
		int maxNumberOfCopies = 9;
		
		List<LabelValueBean> numberOfCopiesDropDownList = new ArrayList<LabelValueBean>();
			for (int i = 1; i <= maxNumberOfCopies; i++) {
				numberOfCopiesDropDownList.add(new LabelValueBean(String.valueOf(i), String
						.valueOf(i)));
			}
			return numberOfCopiesDropDownList;
	}


	public static BaseHistoryDetailsComparator getComparatorInstanceHistoryDetails(
			String sortDirection,
			Class<? extends BaseHistoryDetailsComparator> comparatorClass)
			throws SystemException {
		try {
			Constructor<? extends BaseHistoryDetailsComparator> constructor = comparatorClass
					.getConstructor(new Class[] { String.class });
			BaseHistoryDetailsComparator comparator = constructor
					.newInstance(sortDirection);
			return comparator;

		} catch (InstantiationException e) {
			throw new SystemException(e, "InstantiationException occured for :"
					+ comparatorClass.toString());
		} catch (IllegalAccessException e) {
			throw new SystemException(e, "IllegalAccessException occured for :"
					+ comparatorClass.toString());
		} catch (NoSuchMethodException e) {
			throw new SystemException(e, "NoSuchMethodException occured for :"
					+ comparatorClass.toString());
		} catch (InvocationTargetException e) {
			throw new SystemException(e,
					"InvocationTargetException occured for :"
							+ comparatorClass.toString());
		}
	}

	/**
	 * Returns Recent (Max) Date string from list of LabelValueBean
	 * 
	 * @param periodEndingDates
	 * @return
	 */
	public static String getRecentPeriodEndingDate(
			List<PeriodEndingReportDateVO> periodEndingDates) {

		PeriodEndingReportDateVO maxLabelValueBean = Collections.max(
				periodEndingDates, new Comparator<PeriodEndingReportDateVO>() {

					@Override
					public int compare(PeriodEndingReportDateVO object1,
							PeriodEndingReportDateVO object2) {

						if (object1 == null) {
							return -1;
						} else if (object2 == null) {
							return 1;
						}

						SimpleDateFormat formatter = new SimpleDateFormat(
								"MM/dd/yyyy");

						Date date01 = null;
						Date date02 = null;
						try {

							date01 = formatter.parse(object1
									.getPeriodEndingReportDate());
							date02 = formatter.parse(object2
									.getPeriodEndingReportDate());
						} catch (ParseException exception) {
							throw new IllegalArgumentException(
									"Exception occured while Parsing the one of the dates: "
											+ String.valueOf(object1
													.getPeriodEndingReportDate()
													.toString())
											+ " and "
											+ String.valueOf(object2
													.getPeriodEndingReportDate()
													.toString()));
						}

						return date01.compareTo(date02);
					}

				});

		return maxLabelValueBean.getPeriodEndingReportDate();
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
	public static boolean isUserHaveAccessToPlanReviewFunctionality(
			HttpServletRequest request) throws SystemException {

		boolean isUserHaveAccessToPlanReviewFunctionality = true;

		if (!isPlanReviewFunctionalityAvailable()) {

			// It means the Plan Review Functionality is OFF

			// Validate if the user assistantRoleis internal user and having
			// admin permission (PRUV) Plan Review
			// Functionality
			BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
			if (!(userProfile.isInternalUser() && isPlanReviewAdminUser(request))) {
				isUserHaveAccessToPlanReviewFunctionality = false;
			}
		}

		return isUserHaveAccessToPlanReviewFunctionality;
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

	public static boolean isPlanReviewFunctionalityAvailable() throws SystemException {

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
	
	/**
	 * This method is used to check the plan Review operational indicator
	 * navigation
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @throws SystemException
	 */

	public static boolean isPlanReviewLaunched() throws SystemException {

		BaseEnvironment environment = new BaseEnvironment();

		String isPlanReviewLaunched = environment
				.getNamingVariable(
						BDConstants.PLAN_REVIEW_LAUNCHED_NAMING_VARIABLE,
						null);

		if (StringUtils.isBlank(isPlanReviewLaunched)) {
			throw new IllegalArgumentException(
					"invalid value for the naming variable: "
							+ BDConstants.PLAN_REVIEW_LAUNCHED_NAMING_VARIABLE);
		}
		return Boolean.valueOf(isPlanReviewLaunched);

	}
	
	public static boolean isPlanReviewAdminUser(HttpServletRequest request)
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
	 * This method is used to store the Report date from Step2 when user clicks
	 * on GenerateReport.
	 * 
	 * @param contractReviewReportVOList
	 * @param request
	 * @param requestTypeCode (PUBLISH, PRINT, GETDOC, GETDOCLIST)
	 * @param brokerId 
	 * @throws SystemException
	 */
	public static PlanReviewRequestVO populateRequestDetails(
			HttpServletRequest request, RequestTypeCode requestTypeCode)
			throws SystemException {

		PlanReviewRequestVO planReviewRequest = new PlanReviewRequestVO();

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		planReviewRequest.setBrokerId(getBrokerId(request));

		if (!userProfile.isInternalUser()) {
			// external user
			if (userProfile.getRole() instanceof BDFinancialRep
					|| userProfile.getRole() instanceof BDFinancialRepAssistant) {

				if (userProfile.isInMimic()) {
					// if User is in mimic
					BDUserProfile mimickingUserProfile = PlanReviewReportUtils
							.getMimckingUserProfile(request);
					planReviewRequest.setMimickedUserProfileId(BigDecimal
							.valueOf(mimickingUserProfile.getBDPrincipal()
									.getProfileId()));
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
			planReviewRequest.setGeneratedBrokerId((String.valueOf(userProfile
					.getBDPrincipal().getProfileId())));

		}

		planReviewRequest.setUserProfileid(new BigDecimal(userProfile
				.getBDPrincipal().getProfileId()));

		BigDecimal dbSessionId = (BigDecimal) request.getSession()
				.getAttribute(BDConstants.FILTER_DB_SESSION_ID);
		planReviewRequest.setSessionId(String.valueOf(dbSessionId));

		planReviewRequest.setRequestTpeCode(requestTypeCode);
		planReviewRequest.setRequestStatusCode(RequstStatusCode.STARTED);

		planReviewRequest
				.setCreatedTS(new Timestamp(System.currentTimeMillis()));

		// crRequestDTO.setUpdatedTS(new Timestamp(System.currentTimeMillis()));
		return planReviewRequest;
	}

	public static void firePlanReviewRequestEvents(
			PlanReviewRequestVO planReviewRequestVO,
			List<PlanReviewReportUIHolder> planReviewReportUIHolderList,
			HttpServletRequest request) throws SystemException {

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		boolean includeNML = includeNMLFunds(userProfile) ;
		
		for (ActivityVo activity : planReviewRequestVO.getActivityVoList()) {
			
			PlanReviewRequset requestEvent = new PlanReviewRequset();
			requestEvent.setRequestID(String.valueOf(planReviewRequestVO
					.getRequestId()));
			requestEvent
					.setActivityID(String.valueOf(activity.getActivityId()));
			requestEvent.setConsumerName("PlanReviewRequestMDB");
			requestEvent.setInitiator(String.valueOf(userProfile
					.getBDPrincipal().getProfileId()));
			requestEvent.setOriginatorClass(PlanReviewReportStep2Controller.class
					.getName());
			requestEvent.setOriginatorMethod("firePlanReviewRequestEvents");
			requestEvent.setBrokerSellerId(planReviewRequestVO.getBrokerSellerId());
			requestEvent.setNmlFlag(includeNML);
			PublishDocumentPackageVo DocumentPackage = (PublishDocumentPackageVo) activity
					.getDocumentPackageVo();

			if (DocumentPackage.isCmaSelectedCoverPageImageInd()) {
				PlanReviewReportUIHolder uiHolder = getPlanReviewReportUIHolder(
						String.valueOf(activity.getContractId()),
						planReviewReportUIHolderList);

				CoverPageImage cmacoverPageImage = uiHolder
						.getCmaCoverPageImage();

				requestEvent
						.setHighResolutionCoverImageUncPath(cmacoverPageImage
								.getHighResolutionUNCPath());

				requestEvent
						.setHighResolutionCmaCoverImagePath(cmacoverPageImage
								.getHighResolutionImagePath());

				requestEvent
						.setLowResolutionCoverImageUncPath(cmacoverPageImage
								.getLowResolutionUNCPath());

				requestEvent
						.setLowResolutionCmaCoverImagePath(cmacoverPageImage
								.getLowResolutionImagePath());
			}

			try {
				PlanReviewRequestMDBUtility.getInstance(
						new BaseEnvironment().getApplicationId())
						.prepareAndSendJMSMessage(requestEvent);
			} catch (Exception exception) {
				
				logger.error("Exception occured for PlanReviewRequset MDB message: " + requestEvent.toString() , exception);
				
				// record the activity event to fail
				PlanReviewServiceDelegate.getInstance(new BaseEnvironment().getApplicationId())
				.recordPlanReveiwActivityEvent(activity.getActivityId(), 
						new ActivityEventVo(EventSourceCode.RPS,
								ActivityEventCode.MDB_START,
								ActivityEventStatus.FAILED, "MDB invoke failed."));
			}
		}
	}
	
	private static String getBrokerId(HttpServletRequest request)
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
	
	public static PlanReviewReportUIHolder getPlanReviewReportUIHolder(
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
	
	public static Map<String, String> getViewDisableReasonMap() {
		
		Map<String, String> viewDisableReasonMap = new LinkedHashMap<String, String>();
		
		viewDisableReasonMap.put("0", "Incorrect presenter's name");
		viewDisableReasonMap.put("1", "Incorrect cover image or logo ");
		viewDisableReasonMap.put("2", "Incorrect industry selected for benchmarks");
		
		return viewDisableReasonMap;	
	}
	
	/**
     * Logs the web activities
     * 
     * @param action
     * @param profile
     * @param currentPage
     * @param nextPage
     * @param form
     */
    public static void logPlanReviewResubmitActivity(String action, String logData, BDUserProfile profile,
            Logger logger, Category interactionLog, ServiceLogRecord logRecord) {
        try {
            ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
            record.setMethodName(action);
            record.setApplicationId(Environment.getInstance().getApplicationId());
            record.setData(logData);
            record.setDate(new Date());
            record.setPrincipalName(profile.getBDPrincipal().getUserName());
            record.setUserIdentity(String.valueOf(profile.getBDPrincipal().getProfileId()));

            interactionLog.error(record);
        } catch (CloneNotSupportedException e) {
            // log the error, but don't interrupt regular processing
            logger.error("error when trying to log Plan Review Resubmit Activity into MRL the data:" + logData
                    + ". Exception caught= " + e);
        }
    }
 /**
  * This Method is used to add NML indicator to historical iReports or not 
  * @param userProfile
  * @return
  * @throws SystemException
  */
    
    protected static boolean includeNMLFunds(BDUserProfile userProfile) 
	throws SystemException {
		
		boolean includeNMLFunds = false;
		
		if (BDUserProfileHelper.associatedWithApprovingFirm(
				userProfile, NFACodeConstants.NML) &&
				(BDUserProfileHelper.isFirmRep(userProfile) ||
						BDUserProfileHelper.isFinancialRep(userProfile) ||
						BDUserProfileHelper.isFinancialRepAssistant(userProfile))) {
			
			includeNMLFunds = true;
		}

		return includeNMLFunds;
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

}