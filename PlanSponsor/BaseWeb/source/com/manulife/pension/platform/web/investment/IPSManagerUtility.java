package com.manulife.pension.platform.web.investment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.contract.ContractConstants.ContractStatus;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.ipsr.valueobject.FromFundVO;
import com.manulife.pension.service.ipsr.valueobject.FundInstruction;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;
import com.manulife.util.render.RenderConstants;

public class IPSManagerUtility {

	/**
	 * Checks whether View icon/results page is available for Review Status &
	 * Sub Status
	 * 
	 * @param reviewRequestStatus
	 * @param reviewRequestSubStatus
	 * @return boolean
	 */
	public static boolean isViewAvailable(String reviewRequestStatus,
			String reviewRequestSubStatus) {
		boolean isViewAvailable = false;

		if (Constants.IPSR_PENDING_APPROVAL_STATUS.equals(reviewRequestStatus)
				&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
						.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_PENDING_COMPLETION_STATUS
				.equals(reviewRequestStatus)
				&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_EXPIRED_STATUS.equals(reviewRequestStatus)
				&& Constants.IPSR_EXPIRED_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_CANCELLED_STATUS.equals(reviewRequestStatus)
				&& Constants.IPSR_CANCELLED_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_PARTIALLY_COMPLETE_STATUS
				.equals(reviewRequestStatus)
				&& Constants.IPSR_PARTIALLY_COMPLETE_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_COMPLETED_STATUS.equals(reviewRequestStatus)
				&& Constants.IPSR_COMPLETED_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		} else if (Constants.IPSR_FORCED_COMPLETE_STATUS.equals(reviewRequestStatus)
				&& Constants.IPSR_FORCED_COMPLETE_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isViewAvailable = true;
		}

		return isViewAvailable;
	}
	
	public static boolean isInstructionsAvailableForView(
			List<IPSFundInstructionPresentation> ipsFundInstPresentationList) {

		return CollectionUtils.isNotEmpty(ipsFundInstPresentationList);
	}
	
	/**
	 * Checks whether Edit icon/results page is available for Review Status &
	 * Sub Status
	 * 
	 * @param reviewRequestStatus
	 * @param reviewRequestSubStatus
	 * @param contractStatus
	 * @param isAllFundInstDisabled
	 * @param contractId
	 * @return boolean
	 */
	public static boolean isEditAvailable(String reviewRequestStatus,
			String reviewRequestSubStatus, String contractStatus,
			boolean isAllFundInstDisabled, int contractId)
			throws SystemException {
		boolean isEditAvailable = false;

		if (!("CF".equalsIgnoreCase(contractStatus)
				|| "DI".equalsIgnoreCase(contractStatus) || "IA"
				.equalsIgnoreCase(contractStatus))
				&& !isAllFundInstDisabled) {
			if (isIPSServiceAvailable(contractId)) {
				if (Constants.IPSR_PENDING_APPROVAL_STATUS
						.equals(reviewRequestStatus)
						&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
								.equals(reviewRequestSubStatus)) {
					isEditAvailable = true;
				}
			}
		}

		return isEditAvailable;
	}
	
	/**
	 * Checks whether Cancel icon/results page is available for Review Status &
	 * Sub Status
	 * 
	 * @param reviewRequestStatus
	 * @param reviewRequestSubStatus
	 * @param processingDate
	 * @param contractStatus
	 * @param contractId
	 * @return boolean
	 */
	public static boolean isCancelAvailable(String reviewRequestStatus,
			String reviewRequestSubStatus, Date processingDate,
			String contractStatus, int contractId) throws SystemException {
		boolean isCancelAvailable = false;

		if (!("CF".equalsIgnoreCase(contractStatus)
				|| "DI".equalsIgnoreCase(contractStatus) || "IA"
				.equalsIgnoreCase(contractStatus))) {
			if (isIPSServiceAvailable(contractId)) {
				if (Constants.IPSR_PENDING_APPROVAL_STATUS
						.equals(reviewRequestStatus)
						&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
								.equals(reviewRequestSubStatus)) {
					isCancelAvailable = true;
				} else if (Constants.IPSR_PENDING_COMPLETION_STATUS
						.equals(reviewRequestStatus)
						&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS
								.equals(reviewRequestSubStatus)) {
					isCancelAvailable = true;
				}

				if (isCancelAvailable) {

					if (isCancellationAllowed(processingDate)) {
						isCancelAvailable = true;
					} else {
						isCancelAvailable = false;
					}
				}
			}
		}

		return isCancelAvailable;
	}

	/**
	 * Check whether NYSE is available
	 * If Current Date is as on IPS IAT Effective Date
	 * 
	 * @param processingDate
	 * @param availabilityStatus
	 * @return boolean
	 * @throws SystemException
	 */
	public static boolean isCancellationAllowed(Date processingDate) throws SystemException {
		boolean isCancellationAllowed = true;
		AvailabilityStatus availabilityStatus = null;
		
		if (processingDate != null) {
			GregorianCalendar currentDateCal = new GregorianCalendar();
			resetTheTime(currentDateCal);

			GregorianCalendar ipsIatDateCal = new GregorianCalendar();
			ipsIatDateCal.setTime(processingDate);
			resetTheTime(ipsIatDateCal);

			try {

				// If Current Date is as on IPS IAT Effective Date
				if(DateUtils.isSameDay(currentDateCal, ipsIatDateCal)){
					availabilityStatus = AccountServiceDelegate
							.getInstance().getNYSEAvailabilityStatusAsOf(
									new Date(), false);
					Date downTime = availabilityStatus.getDownTime();
					if (DateUtils.isSameDay(currentDateCal.getTime(), downTime)) {
						GregorianCalendar currentTime = new GregorianCalendar();
						GregorianCalendar nyseClosureTime = new GregorianCalendar();
						nyseClosureTime.setTime(downTime);

						if (currentTime.after(nyseClosureTime)) {
							isCancellationAllowed = false;
						}
					}
				}
			} catch (Exception e) {
				throw new SystemException(e,
						"Exception in Checking Availability of NYSE");
			}
		} 
		
		return isCancellationAllowed;
	}

	/**
	 * Checks whether Participant Notification Link is available for Review
	 * Status & Sub Status
	 * 
	 * @param reviewRequestStatus
	 * @param reviewRequestSubStatus
	 * @return boolean
	 */
	public static boolean isParticipantNotificationLinkAvailable(
			String reviewRequestStatus, String reviewRequestSubStatus) {
		boolean isParticipantNotificationLinkAvailable = false;

		if (Constants.IPSR_PENDING_COMPLETION_STATUS
				.equals(reviewRequestStatus)
				&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS.equals(reviewRequestSubStatus)) {
			isParticipantNotificationLinkAvailable = true;
		} 

		return isParticipantNotificationLinkAvailable;
	}
	
	/**
	 * Checks whether Interim Report Link is available for Review
	 * Status & Sub Status
	 * 
	 * @param ipsReviewRequestList
	 * @param contractStatus TODO
	 * @return boolean
	 */
	public static boolean isInterimReportLinkAvailable(
			List<IPSRReviewRequest> ipsReviewRequestList, String contractStatus) {
		boolean interimReportAvailable = true;
		
		if (!StringUtils.equalsIgnoreCase(ContractStatus.ACTIVE, contractStatus)) {
			interimReportAvailable = false;
		}
		
		if (interimReportAvailable) {
			for (IPSRReviewRequest ipsrReviewRequest : ipsReviewRequestList) {
				if (Constants.IPSR_INITIAL_STATUS.equals(ipsrReviewRequest
						.getReviewRequestStatus())
						&& Constants.IPSR_INITIAL_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				} else if (Constants.IPSR_PENDING_ANALYSIS_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_ANALYSIS_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				} else if (Constants.IPSR_PENDING_REPORT_GENERATION_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_REPORT_GENERATION_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				} else if (Constants.IPSR_PENDING_APPROVAL_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				} else if (Constants.IPSR_PENDING_COMPLETION_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				} else if (Constants.IPSR_PARTIALLY_COMPLETE_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PARTIALLY_COMPLETE_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					interimReportAvailable = false;
					break;
				}
			}
		}
		return interimReportAvailable;
	}
	
	/**
	 * Checks whether all Fund Instructions are ignored
	 * 
	 * @param ipsReviewFundInstructionList
	 * @return boolean
	 */
	public static boolean isAllFundsInstructionsIgnored(
			List<FundInstruction> ipsReviewFundInstructionList) {

		List<String> ignoredFundList = new ArrayList<String>();

		// Check for Fund Instructions is available for the review id
		if (ipsReviewFundInstructionList != null
				&& !ipsReviewFundInstructionList.isEmpty()) {
			// Iterate through the Fund Instructions list
			for (FundInstruction fundInstruction : ipsReviewFundInstructionList) {
				String actionIndicator = StringUtils.trim(fundInstruction
						.getActionIndicator());

				// Checks condition for Action taken for Approved or Ignored
				// And add it to corresponding Lists
				if (StringUtils.equals(Constants.ACTION_IGNORED,
						actionIndicator)) {
					ignoredFundList.add(fundInstruction.getFromFundVO()
							.getFundCode());
				} else if (StringUtils.equals(Constants.SYSTEM_IGNORED,
						actionIndicator)) {
					ignoredFundList.add(fundInstruction.getFromFundVO()
							.getFundCode());
				}
			}
		}

		boolean isAllReplacementIgnored = false;

		// All Funds are ignored by Trustee
		if (ipsReviewFundInstructionList != null
				&& ignoredFundList != null
				&& !ipsReviewFundInstructionList.isEmpty()
				&& !ignoredFundList.isEmpty()
				&& ipsReviewFundInstructionList.size() == ignoredFundList
						.size()) {
			isAllReplacementIgnored = true;
		}

		return isAllReplacementIgnored;
	}

	/**
	 * Checks whether at least one Fund Instruction is approved
	 * 
	 * @param ipsReviewFundInstructionList
	 * @return boolean
	 */
	public static boolean isAtleastIneFundsInstructionApproved(
			List<FundInstruction> ipsReviewFundInstructionList) {
		boolean isAtleastOneFundInstructionApproved = false;
		// Check for Fund Instructions is available for the review id
		if (ipsReviewFundInstructionList != null
				&& !ipsReviewFundInstructionList.isEmpty()) {
			// Iterate through the Fund Instructions list
			for (FundInstruction fundInstruction : ipsReviewFundInstructionList) {
				String actionIndicator = StringUtils.trim(fundInstruction
						.getActionIndicator());

				// Checks condition for Action taken for Approved or Ignored
				// And add it to corresponding Lists
				if (Constants.ACTION_APPROVED.equals(actionIndicator)) {
					isAtleastOneFundInstructionApproved = true;
					break;
				}
			}
		}

		return isAtleastOneFundInstructionApproved;
	}

	/**
	 * Check whether the Review Request is the current Review
	 * 
	 * @param ipsrReviewRequests
	 * @param reviewRequestId
	 * @return boolean
	 */
	public static boolean isCurrentReview(
			List<IPSRReviewRequest> ipsrReviewRequests, int reviewRequestId) {
		boolean isCurrentReview = false;

		if (ipsrReviewRequests != null) {
			IPSRReviewRequest ipsrReviewRequest = ipsrReviewRequests.get(0);
			if (reviewRequestId == ipsrReviewRequest.getReviewRequestId()) {
				isCurrentReview = true;
			}
		}

		return isCurrentReview;
	}
	
	/**
	 * Populate the fund instruction with the values that user has selected
	 * 
	 * @param presentationList
	 * @param fundInstructionList
	 */
	public static void populateFundInstructionFromPresentation(
			List<IPSFundInstructionPresentation> presentationList,
			List<FundInstruction> fundInstructionList) {

		// Iterate through the fund instructions
		for (FundInstruction fundInstruction : fundInstructionList) {

			String assetClass = fundInstruction.getAssetClass();
			
			// Iterate through the presentation list and get the action selected
			// by the user for the replacement fund and set it to fund instruction
			for (IPSFundInstructionPresentation fundInstPresentation : presentationList) {
				if (assetClass.equals(fundInstPresentation.getAssetClass())) {

					List<ToFundVO> toFundVOList = fundInstPresentation
							.getToFundVO();

					for (ToFundVO toFundVO : toFundVOList) {
						if (fundInstruction.getToFundVO().getFundCode().equals(
								toFundVO.getFundCode())) {
							String actionInd = toFundVO.getActionIndicator();
							if (!toFundVO.isActionEnabled()
									&& com.manulife.pension.service.contract.util.Constants.ACTION_IGNORED
											.equals(toFundVO
													.getActionIndicator())) {
								actionInd = com.manulife.pension.service.contract.util.Constants.SYSTEM_IGNORED;
							}
							
							// If the current fund is same as top ranked fund then set the action as system ignored
							String currentFund = fundInstruction.getFromFundVO().getFundCode();
							String topRankedFund = fundInstruction.getToFundVO().getFundCode();
							if(StringUtils.equals(currentFund, topRankedFund)) {
								fundInstruction.setActionIndicator(com.manulife.pension.service.contract.util.Constants.SYSTEM_IGNORED);
							} else {
								fundInstruction.setActionIndicator(actionInd);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Converts the string into date format.
	 * 
	 * @param iatEffectiveDate
	 * @return
	 * @throws ParseException
	 */
	public static Date getIATEffectiveDateInDateFormat(String iatEffectiveDate) throws ParseException{
		return DateUtils.parseDate(iatEffectiveDate,
				new String[] { RenderConstants.MEDIUM_MDY_SLASHED });
	}
	
	/**
	 * Reset the time stamp
	 * 
	 * @param cal
	 */
	public static void resetTheTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
	}
	
	/**
	 * Check whether New Service Review Date is available to change can be shown or not
	 * for showing dialog box when change link is clicked
	 * 
	 * @param ipsReviewRequestList
	 * @return boolean
	 */
	public static boolean isEditIPSManagerNotAvailable(List<IPSRReviewRequest> ipsReviewRequestList) {
		boolean isServiceDateNotAvailableToChange = false;
		
		if (ipsReviewRequestList != null && !ipsReviewRequestList.isEmpty()) {
			for (IPSRReviewRequest ipsrReviewRequest : ipsReviewRequestList) {
				if (Constants.IPSR_PENDING_REPORT_GENERATION_STATUS.equals(ipsrReviewRequest
						.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_REPORT_GENERATION_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					isServiceDateNotAvailableToChange = true;
					break;
				} else if (Constants.IPSR_PENDING_APPROVAL_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
								.equals(ipsrReviewRequest
										.getReviewRequestSubStatus())) {
					isServiceDateNotAvailableToChange = true;
					break;
				} else if (Constants.IPSR_PENDING_COMPLETION_STATUS
						.equals(ipsrReviewRequest.getReviewRequestStatus())
						&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS.equals(ipsrReviewRequest
								.getReviewRequestSubStatus())) {
					isServiceDateNotAvailableToChange = true;
					break;
				}
			}
		}
		return isServiceDateNotAvailableToChange;
	}
	
	/**
	 * Checks whether the fund is analyzed
	 * 
	 * @param specialFundIndicatorMap
	 * @param fundId
	 * @return
	 */
	private static boolean isFundAnalyzed(Map<String, String> specialFundIndicatorMap, String fundId) {

		String analyzedIndicator = "";
		
		// Get the Special Fund Code for each Current Fund
		if (specialFundIndicatorMap != null) {
			analyzedIndicator = specialFundIndicatorMap.get(fundId);
		}

		// Set the From Fund as Analyzed Fund if its Indicator is 'A'
		if (StringUtils.equals(Constants.ANALYZED_FUND_IND, analyzedIndicator)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method used to check whether all current funds are valid for a review id
	 *  
	 * @param fromFundVOList
	 * @param specialFundIndicatorMap
	 * @return boolean
	 */
	public static boolean isAllFromFundsInvalid(List<FromFundVO> fromFundVOList,
			Map<String, String> specialFundIndicatorMap) {
		int fromFundFailedCount = 0;
		int validFromFundCount = 0;
		for (FromFundVO fromFundVO : fromFundVOList) {
			boolean isFromFundClosed = fromFundVO.isFundClosed();
			boolean isFromFundAnalyzed = isFundAnalyzed(
					specialFundIndicatorMap, fromFundVO.getFundCode());
			boolean isFromFundSelected = fromFundVO.getFundCode() != null ? fromFundVO
					.isFundSelected()
					: true;
			boolean isFromFundDIO = fromFundVO.isDIOFund();

			if (StringUtils.isNotBlank(fromFundVO.getFundCode())
					&& (!isFromFundAnalyzed || !isFromFundSelected
							|| isFromFundClosed || isFromFundDIO)) {
				fromFundFailedCount++;
			}
			
			validFromFundCount++;
		}

		// If all the from funds are not valid then all the radio
		// buttons should be disabled and set as ignored
		boolean allFromFundsAreInvalid = fromFundFailedCount == validFromFundCount ? true
				: false;
		
		return allFromFundsAreInvalid;
	}
	
	/**
	 * This method is used to check the given top fund is valid.
	 * 
	 * @param toFundVO
	 * @return boolean
	 */
	public static boolean isToFundValid(ToFundVO toFundVO) {

		boolean isToFundClosed = toFundVO.isFundClosed();
		boolean isToFundClosedToNewBus = toFundVO.isFundClosedToNewBusiness();

		// If the to fund is invalid then set them as disabled and
		// set the ind as ignored
		if (isToFundClosed || isToFundClosedToNewBus
				|| toFundVO.isFundDifferentAssetClsAndNonFSWAssetCls()) {
			return false;
		}

		return true;
	}
	
	/**
	 * Gets the whether IPS Service is available for the Contract or not
	 * 
	 * @param contractId
	 * @return boolean
	 * @throws SystemException
	 */
	public static boolean isIPSServiceAvailable(int contractId)
			throws SystemException {

		boolean ipsAvailable = false;
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();

		InvestmentPolicyStatementVO ipsBaseData = delegate
				.getIpsBaseData(contractId);

		if (ipsBaseData != null && ipsBaseData.isIpsAvailable()) {
			ipsAvailable = true;
		} else {
			ipsAvailable = false;
		}

		return ipsAvailable;
	}
}
