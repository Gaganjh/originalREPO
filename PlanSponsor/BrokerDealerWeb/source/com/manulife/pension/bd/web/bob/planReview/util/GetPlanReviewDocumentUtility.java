/**
 * 
 */
package com.manulife.pension.bd.web.bob.planReview.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
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
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.security.role.BDFinancialRep;
import com.manulife.pension.service.security.role.BDFinancialRepAssistant;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventStatus;
import com.manulife.pension.util.PlanReviewConstants.ActivitySatusCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityTypeCode;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.PlanReviewConstants.RequstStatusCode;
import com.manulife.util.render.RenderConstants;

/**
 * 
 * This class to handle/perform GetDocList and GetDoc operations 
 * 
 * @author akarave
 *
 */
public class GetPlanReviewDocumentUtility {
	
	protected static final Logger logger = Logger.getLogger(GetPlanReviewDocumentUtility.class);
	
	protected static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	
	public GetPlanReviewDocumentUtility() {
		super();
	}

		
	public PlanReviewReportDocumentVo getPlanReviewDocument(
			final List<PlanReviewReportUIHolder> planReviewRecords,
			HttpServletRequest request,
			PlanReviewDocumentType planReviewDocumentType)
			throws SystemException {


		BDUserProfile userProfile = getUserProfile(request);
		
		String userProfileId = String.valueOf(userProfile.getBDPrincipal().getProfileId());
		
		PlanReviewRequestVO getPlanReviewDocListRequest = populateRequestDetails(
				planReviewRecords, request,
				RequestTypeCode.GET_DOC_LIST);
		
		// insert getdocList
		getPlanReviewDocListRequest = PlanReviewServiceDelegate.getInstance(
				Environment.getInstance().getApplicationId())
				.insertGetDocumentListRequestDetails(
						getPlanReviewDocListRequest);
		
		PlanReviewRequestVO getPlanReviewRequest = populateRequestDetails(
				planReviewRecords, request, RequestTypeCode.GET_DOC);
		
		// insert print doc details
		getPlanReviewRequest = PlanReviewServiceDelegate.getInstance(
				Environment.getInstance().getApplicationId())
				.insertGetDocumentRequestDetails(
						getPlanReviewRequest);
		
		populateTibcoGetDocumentListProperties(
				getPlanReviewDocListRequest, planReviewRecords);
		
		for (ActivityVo getDocListActivity : getPlanReviewDocListRequest
				.getActivityVoList()) {

			PlanReviewReportUIHolder uiHolder = getPlanReviewReportUIHolder(
					String.valueOf(getDocListActivity.getContractId()),
					planReviewRecords);

			Date reportMonthEndDate = null;

			try {
				reportMonthEndDate = SHORT_MDY_FORMATTER.parse(uiHolder
						.getSelectedReportMonthEndDate());
			} catch (ParseException e) {
				throw new SystemException(e,
						"Invalid Plan Reivew Report Month-End Date: "
								+ uiHolder.getSelectedReportMonthEndDate());
			}

			// TIBCO GET DOC LIST call
			PlanReviewReportDocumentPackage planReviewReportDocPackage = getPlanReviewDocumentList(
					userProfileId,
					getDocListActivity,
					String.valueOf(uiHolder.getPublishDocumentActivityId()),
					reportMonthEndDate);

			ActivityVo getDocumentActivity = null;
			
			for(ActivityVo activity : getPlanReviewRequest.getActivityVoList()) {
				
				if (StringUtils.equals(
						String.valueOf(getDocListActivity.getContractId()),
						String.valueOf(activity.getContractId()))) {
					getDocumentActivity = activity;
					break;
				}
			}
			
			if(getDocumentActivity == null){
				logger.error("Invalid getDocument request. getPlanReviewDocListRequest =[" +
						getPlanReviewDocListRequest+ "],   getPlanReviewRequest=[" +
						getPlanReviewRequest + "]");
				
				continue;
			}
			
			if (planReviewReportDocPackage != null) {
				
				String dstDocumentID = StringUtils.EMPTY;
				boolean isHavingDocument = false;
				for (PlanReviewReportDocumentVo document : planReviewReportDocPackage.getPlanReviewReportDocumentVoList()) {

					if (StringUtils.equalsIgnoreCase(
							planReviewDocumentType
									.getDocumentTypeCode(), document
									.getDocumentType())) {
						isHavingDocument = true;
						dstDocumentID = document.getDocumentId();
						
						break;
					}
				}
				
				if(isHavingDocument) {
					
					getDocumentActivity.setDstoDocumentPackage(planReviewReportDocPackage);
					
					// record the activity event to OK
					PlanReviewServiceDelegate.getInstance(
							new BaseEnvironment().getApplicationId())
							.recordPlanReveiwActivityEvent(
									getDocumentActivity.getActivityId(),
									new ActivityEventVo(EventSourceCode.RPS,
											ActivityEventCode.GET_DOC_START,
											ActivityEventStatus.OK,
											"OK for PublishDocumentActivityId: " + String.valueOf(uiHolder.getPublishDocumentActivityId())));
					
					// Trigger webservice TIBCO GET DOC for Contract Review Report PDF
					return getPlanReviewDocument(getDocumentActivity, userProfileId, dstDocumentID);
					
				} else {
					
					// record the activity event to fail
					PlanReviewServiceDelegate.getInstance(
							new BaseEnvironment().getApplicationId())
							.recordPlanReveiwActivityEvent(
									getDocumentActivity.getActivityId(),
									new ActivityEventVo(EventSourceCode.RPS,
											ActivityEventCode.GET_DOC_START,
											ActivityEventStatus.FAILED,
											"Failed, since no " + planReviewDocumentType
											.getDocumentTypeCode() + " found in GetDocList response for PublishActivityId: " 
													+ String.valueOf(uiHolder.getPublishDocumentActivityId())));
				}
			} else {
				
				// record the activity event to fail
				PlanReviewServiceDelegate.getInstance(
						new BaseEnvironment().getApplicationId())
						.recordPlanReveiwActivityEvent(
								getDocumentActivity.getActivityId(),
								new ActivityEventVo(EventSourceCode.RPS,
										ActivityEventCode.GET_DOC_START,
										ActivityEventStatus.FAILED,
										"Failed, since no plan review documents found in GetDocList response for PublishActivityId: " 
										+ String.valueOf(uiHolder.getPublishDocumentActivityId())));
				
			}
		}
		
		return null;
	}
	
	
	private PlanReviewReportDocumentVo getPlanReviewDocument(ActivityVo getDocumentActivity,
			String userProfileId, String dstDocumentID) throws SystemException {

		populateTibcoGetDocumentProperties(getDocumentActivity, dstDocumentID);

		try {
			return PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.getPlanReviewDocument(getDocumentActivity, userProfileId);
		} catch (Exception exception) {

			logger.error("Exception occured for PlanReview getDocument request: ["
					+ getDocumentActivity.toString() + "] for dstDocumentID: "
					+ dstDocumentID, exception);

			// record the activity event to fail
			PlanReviewServiceDelegate.getInstance(
					new BaseEnvironment().getApplicationId())
					.recordPlanReveiwActivityEvent(
							getDocumentActivity.getActivityId(),
							new ActivityEventVo(EventSourceCode.RPS,
									ActivityEventCode.GET_DOC_END,
									ActivityEventStatus.FAILED,
									StringUtils.substring(exception.getMessage(), 0, 254)));
		}
		
		return null;
	}
	
	private void populateTibcoGetDocumentProperties(ActivityVo activity, String dstDocumentID) {
		
		activity.setDivision(Division.USA);
		activity.setBusinessUnit(BusinessUnit.RPS);
		activity.setProductType(ProductType.ANNUITIES);
		activity.setProductSubType(ProductSubType.VARIABLELIFEINSURANCE);
		activity.setDocumentId(dstDocumentID);
		activity.setDocumentId(dstDocumentID);
		activity.setDocumentFormat(DocumentFormat.PDF);
		activity.setDocumentType(DocumentType.REGULAR);
		activity.setDocumentStatus(DocumentStatus.ACTIVE);
	}
	
	
	/**
	 * This method is used to store the Report date from Step2 when user clicks
	 * on GenerateReport.
	 * 
	 * @param planReviewReportVOList
	 * @param request
	 * @throws SystemException
	 */
	private PlanReviewRequestVO populateRequestDetails(
			List<PlanReviewReportUIHolder> planReviewReportVOList,
			HttpServletRequest request, RequestTypeCode requestTypeCode)
			throws SystemException {

		PlanReviewRequestVO planReviewRequest = populateRequestDetails(request, requestTypeCode);

		if (RequestTypeCode.GET_DOC.equals(requestTypeCode)) {
			
			populateActivityDetails(planReviewReportVOList,
					planReviewRequest, ActivityTypeCode.GET_DOC);
		} else {
			
			populateActivityDetails(planReviewReportVOList,
					planReviewRequest, ActivityTypeCode.GET_DOC_LIST);
		}
		
		return planReviewRequest;
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
	private PlanReviewRequestVO populateRequestDetails(
			HttpServletRequest request, RequestTypeCode requestTypeCode)
			throws SystemException {

		PlanReviewRequestVO planReviewRequest = new PlanReviewRequestVO();

		BDUserProfile userProfile = getUserProfile(request);
		planReviewRequest.setBrokerId(getBrokerId(request));

		if (!userProfile.isInternalUser()) {
			// external user
			if (userProfile.getRole() instanceof BDFinancialRep
					|| userProfile.getRole() instanceof BDFinancialRepAssistant) {

				if (userProfile.isInMimic()) {
					// if User is in mimic
					BDUserProfile mimickingUserProfile = getMimckingUserProfile(request);
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
	
	/**
	 * This method would return the UserProfile object of the mimicking user.
	 * 
	 * @return - BDUserProfile object of the mimicking user.
	 */
	private BDUserProfile getMimckingUserProfile(
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
	
	private String getBrokerId(HttpServletRequest request)
			throws SystemException {

		BDUserProfile userProfile = getUserProfile(request);
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
	

	/**
	 * This method is used to populate data.
	 * 
	 * @param planReviewReportVOList
	 * @param requestDataRecord
	 * @throws SystemException
	 */
	private void populateActivityDetails(
			List<PlanReviewReportUIHolder> planReviewReportVOList,
			PlanReviewRequestVO planReviewRequestVO ,ActivityTypeCode activityTypeCode) throws SystemException {

		List<ActivityVo> activityList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uiHolder : planReviewReportVOList) {
			
			ActivityVo activity = new ActivityVo();
			activity.setContractId(uiHolder.getContractNumber());
			activity.setTypeCode(activityTypeCode);
			activity.setStatusCode(ActivitySatusCode.PENDING);
			
			if (ActivityTypeCode.GET_DOC_LIST.equals(activityTypeCode)) {
				
				populateActivityEvent(activity,
						ActivityEventCode.GET_DOC_LIST_START, ActivityEventStatus.OK, "OK for PublishDocumentActivityId: " + uiHolder.getPublishDocumentActivityId());
			} 
			
			activityList.add(activity);
		}
		planReviewRequestVO.setActivityVoList(activityList);
	}
	
	/**
	 * This method is used to populate populateActivityEvent data.
	 * 
	 * @param PlanReviewReportUIHolder
	 * @param ActivityVo
	 * @throws SystemException
	 */
	private void populateActivityEvent(ActivityVo activity,
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
	
	private PlanReviewReportDocumentPackage getPlanReviewDocumentList(
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
	
	private void populateTibcoGetDocumentListProperties(
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
	
	private PlanReviewReportUIHolder getPlanReviewReportUIHolder(
			String contractNumber,
			final List<PlanReviewReportUIHolder> reportUIHolderList) {

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
	
	/**
	 * Returns the user profile associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The user profile object associated with the request (or null if
	 *         none is found).
	 */
	public static BDUserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		BDUserProfile userProfile = null;
		if (session != null) {
			userProfile = (BDUserProfile) session.getAttribute(BDConstants.USERPROFILE_KEY);
		}
		return userProfile; 
	}
	
}
