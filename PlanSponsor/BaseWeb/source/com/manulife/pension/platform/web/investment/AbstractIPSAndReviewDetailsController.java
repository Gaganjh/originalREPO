package com.manulife.pension.platform.web.investment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.IPSDocumentInfo;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.IPSRStatusChangeEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.fundevaluation.engine.EngineException;
import com.manulife.pension.fundevaluation.engine.FundEvaluationEngineDelegate;
import com.manulife.pension.fundevaluation.engine.FundRank;
import com.manulife.pension.fundevaluation.engine.input.Measurement;
import com.manulife.pension.fundevaluation.engine.input.RankingCriteria;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.DataValidationHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.platform.web.util.XSLFileURIResolver;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.InvestmentPolicyStatementVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.environment.util.BusinessParamConstants;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.ipsr.document.valueobject.ParticipantNotificationInfo;
import com.manulife.pension.service.ipsr.exception.FundSheetNotAvailableException;
import com.manulife.pension.service.ipsr.util.PDFGenerator;
import com.manulife.pension.service.ipsr.valueobject.FromFundVO;
import com.manulife.pension.service.ipsr.valueobject.FundInstruction;
import com.manulife.pension.service.ipsr.valueobject.FundRedemptionFeesVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Abstract action class for IPS and review details pages
 * 
 * @author thangjo
 * 
 */
public abstract class AbstractIPSAndReviewDetailsController extends BaseAutoController {
	
	public static final Logger logger = Logger.getLogger(AbstractIPSAndReviewDetailsController.class);

	protected static final String VIEW_IPS_AND_DETAILS_PAGE = "viewIPSAndDetails";

	protected static final String EDIT_IPS_AND_DETAILS_PAGE = "editIPSAndDetails";

	protected static final String DEFAULT_ACTION = "default";
	
	protected static final String FRW_HOME_ACTION = "homePage";
	
	protected static final String PSW_HOME_ACTION = "homePageFinder";

	protected static final String EDIT_ACTION = "edit";

	protected static final String SAVE_ACTION = "save";

	protected static final String EDIT_MODE = "editMode";

	protected static final String VIEW_MODE = "viewMode";

	protected static final String CHANGE_HISTORY = "ipsChangeHistory";

	protected static final String SUCCESS_IND = "ipsSuccessInd";
	
	protected static final String VIEW_REVIEW_ACTION = "viewIPSReviewResults";

	protected static final String EDIT_REVIEW_ACTION = "editIPSReviewResults";
	
	protected static final String APPROVE_CONFIRMATION_ACTION = "approveIPSReviewResults";
	
	protected static final String CANCEL_CONFIRMATION_ACTION = "canceIPSReview";

	protected static final String SPLITTER_TEXT = "-------------- Select --------------";

	private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

	protected static final String MEDIUM_MMMMDDYYYY = "MMM dd - yyyy";
	
	protected static final String MEDIUM_MMMMDDYYYY_COMMA = "MMM dd, yyyy";

	protected static final String MEDIUM_MMMDDYYYY = "MMM/dd/yyyy";
	
	protected static final String MEDIUM_MMDD_SLASHED = "MM/dd";
	
	protected static final String MEDIUM_MMMM = "MMMM";
	
	protected static final String MEDIUM_DD = "dd";

	protected static final String BACK_ACTION = "back";
	
	protected static final String NEW_SERVICE_DATE_TEXT = "serviceDateSuccessfullySaved";
	
	protected static final String ZERO = "0";
	
	protected static final String INITIATED = "Upcoming Review Notification sent";
	
	protected static final String IN_PROGRESS = "In progress";
	
	protected static final String READY_FOR_REVIEW = "Ready for review";
	
	protected static final String APPROVED_FUND_CHANGES_FOR = "Approved fund changes for ";
	
	protected static final String NO_FUNDS_CHANGES_IDENTIFIED = "No Funds match warning threshold";
	
	protected static final String TRUSTEE_IGNORED_ALL_REPLACEMENTS = "Trustee ignored all replacements";
	
	protected static final String FUND_CHANGES_COMPLETE = "Fund changes complete";
	
	protected static final String EXPIRED = "Expired";
	
	protected static final String CANCELLED_ON = "Cancelled on ";
	
	protected static final String OVERRIDDEN = "Overridden by a new scheduled review";
	
	protected static final String APPROVED = "Approved";
	
	protected static final String IGNORED = "Ignored";
	
	protected static final String NO_ACTION_TAKEN = "No action taken";
	
	protected static final String TRUE = "true";
	
	protected static final String FALSE = "false";
	
	protected static final String PROCESSING_DATE = "processingDate";
	
	protected static final String EDIT_AVAILABLE = "editAvailable";
	
	private static String URL = null;
	
	private static String EZK_USA_NAME_SPACE_BINDING = "usa.ezk.url";
	
	private static String EZK_NY_NAME_SPACE_BINDING = "ny.ezk.url";
	
	protected static final String ASSET_CLS_ORDER = "assetClsOrder";
	
	protected static final String MARKETING_SORT_ORDER = "marketingSortOrder";
	
	protected static final String LONG_MMMMDDYYYY_COMMA = "MMMM dd, yyyy";
	
	private static String OPEN_BRACE = "(";
	
	private static String CLOSED_BRACE = ")";
	
	private static String DASH = "-";
	
	private static final String LIFE_CYCLE_ASSET_CLASS = "LCF";
	private static final String LIFE_STYLE_ASSET_CLASS = "LSF";
	private static final String SECTOR = "SEC";
	private final List<String> ignoredAssetClasses = Arrays.asList(new String[]{LIFE_CYCLE_ASSET_CLASS, LIFE_STYLE_ASSET_CLASS, SECTOR});
	
	
	/**
	 * Populate the for bean with necessary data
	 * 
	 * @param ipsAssistServiceForm
	 * @param contractId
	 * @throws SystemException
	 */
	protected void populateFormBean(int contractId,
			IPSAndReviewDetailsForm ipsAssistServiceForm,
			Map<String, String> criteriaDescriptionMap) throws SystemException {
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		InvestmentPolicyStatementVO investmentPolicyStatementVO = delegate
				.getIPSCriteria(contractId);
		InvestmentPolicyStatementVO ipsBaseData = ContractServiceDelegate
				.getInstance().getIpsBaseData(contractId);
		List<CriteriaAndWeightingPresentation> criteriaWeightPresentationList = new ArrayList<CriteriaAndWeightingPresentation>();
		List<String> colorCodeList = CommonConstants.ipsColorCode;
		Map<String, Integer> ipsCriteriaMap = investmentPolicyStatementVO
				.getInvestmentPolicyStatementCriteria();

		ipsAssistServiceForm.setCriteriaAndWeighting(ipsCriteriaMap);
		// ipsAssistServiceForm.setSaveSuccess(false);

		// Populate the CriteriaAndWeightingPresentation bean to render the
		// legend, criteria and weightings in the JSP
		// and compute the total weighting
		int index = 0;
		for (String criteriaCode : ipsCriteriaMap.keySet()) {
			CriteriaAndWeightingPresentation criteriaAndWeightingPresentation = new CriteriaAndWeightingPresentation();
			criteriaAndWeightingPresentation
					.setCriteriaDesc(criteriaDescriptionMap.get(criteriaCode
							.trim()));
			criteriaAndWeightingPresentation.setColorCode(colorCodeList
					.get(index));
			criteriaAndWeightingPresentation.setWeighting(Integer
					.toString(ipsCriteriaMap.get(criteriaCode.trim())));
			criteriaAndWeightingPresentation.setCriteriaCode(criteriaCode
					.trim());
			criteriaWeightPresentationList
					.add(criteriaAndWeightingPresentation);
			index++;
		}

		
		// For edit page add place holder for empty criteria with color code
		if (StringUtils.equals(EDIT_MODE, ipsAssistServiceForm.getMode())) {
			for (int i = index; i < criteriaDescriptionMap.size(); i++) {
				CriteriaAndWeightingPresentation criteriaAndWeightingPresentation = new CriteriaAndWeightingPresentation();
				criteriaAndWeightingPresentation.setColorCode(colorCodeList
						.get(i));
				criteriaWeightPresentationList
						.add(criteriaAndWeightingPresentation);
			}
		}

		// Set the presentation VO to the form bean
		ipsAssistServiceForm
				.setCriteriaAndWeightingPresentationList(criteriaWeightPresentationList);
		ipsAssistServiceForm.setLastModifiedOn(investmentPolicyStatementVO
				.getStartTS());
		ipsAssistServiceForm.setLastUpdatedUserId(investmentPolicyStatementVO
				.getCreatedUserId());
		ipsAssistServiceForm
				.setLastUpdatedUserIdType(investmentPolicyStatementVO
						.getCreatedUserIdType());
		ipsAssistServiceForm.setSourceChannelCode(investmentPolicyStatementVO
				.getCreatedSourceChannelCode());
		if (ipsBaseData != null) {
			ipsAssistServiceForm.setiPSAssistServiceAvailable(ipsBaseData
					.isIpsAvailable());
			ipsAssistServiceForm.setAnnualReviewDate(DateRender
					.formatByPattern(ipsBaseData.getAnnualReviewDate(), null,
							MEDIUM_MMDD_SLASHED, RenderConstants.MEDIUM_MD));

			// Populate New Service Date
			if (ipsBaseData.getAnnualReviewDate() != null) {
				
				//Check if aannual review date is Feb 29 then change to March 1
				if(ipsBaseData
						.getAnnualReviewDate().getMonth() == 2 && ipsBaseData.getAnnualReviewDate().getDay() == 29) {
					ipsAssistServiceForm.setNewAnnualReviewMonth(ZERO
							+ Integer.toString(ipsBaseData
									.getAnnualReviewDate().getMonth()+1));
					ipsAssistServiceForm.setNewAnnualReviewDate("01");
					
				} else if(ipsBaseData.getAnnualReviewDate().getMonth() < 10) {
					ipsAssistServiceForm.setNewAnnualReviewMonth(ZERO
							+ Integer.toString(ipsBaseData
									.getAnnualReviewDate().getMonth()));
					ipsAssistServiceForm.setNewAnnualReviewDate(Integer
							.toString(ipsBaseData.getAnnualReviewDate().getDay()));
					
				} else {
					ipsAssistServiceForm.setNewAnnualReviewMonth(Integer
							.toString(ipsBaseData.getAnnualReviewDate()
									.getMonth()));
					ipsAssistServiceForm.setNewAnnualReviewDate(Integer
							.toString(ipsBaseData.getAnnualReviewDate().getDay()));
				}
				
			}
		}
		ipsAssistServiceForm
				.setTotalWeighting(getTotalWeighting(criteriaWeightPresentationList));
	}

	/**
	 * Return total weighting
	 * 
	 * @param criteriaAndWeightingList
	 * @return
	 */
	protected int getTotalWeighting(
			List<CriteriaAndWeightingPresentation> criteriaAndWeightingList) {
		int totalWeighting = 0;
		for (CriteriaAndWeightingPresentation criteriaAndWeighting : criteriaAndWeightingList) {
			if (!DataValidationHelper.isBlankOrNull(criteriaAndWeighting
					.getWeighting())) {
				totalWeighting = totalWeighting
						+ Integer.parseInt(criteriaAndWeighting.getWeighting());
			}
		}
		return totalWeighting;
	}

	/**
	 * Create the PieChartBean
	 * 
	 * @param ipsCriteriaMap
	 * @param colorCodeList
	 * @return
	 */
	protected PieChartBean createPieChartBean(
			Map<String, Integer> ipsCriteriaMap, List<String> colorCodeList) {
		PieChartBean pieChart = new PieChartBean();
		// Set the default values
		pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
		pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
		pieChart.setBorderColor("#EAEAEA");
		pieChart.setShowWedgeLabels(true);
		pieChart.setUsePercentsAsWedgeLabels(true);
		pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
		pieChart.setBorderWidth((float) 1.5);
		pieChart.setWedgeLabelOffset(65);
		pieChart.setFontSize(10);
		pieChart.setFontBold(true);
		pieChart.setDrawBorders(true);
		pieChart.setWedgeLabelExtrusionColor("#000000");

		// Set the color code and value
		String suppressWedgesFromKeys = "";
		int index = 0;
		for (String criteriaCode : ipsCriteriaMap.keySet()) {
			pieChart.addPieWedge("wedge" + index, ipsCriteriaMap
					.get(criteriaCode), colorCodeList.get(index), " ", "1",
					"#FFFFFF", 0);
			suppressWedgesFromKeys = suppressWedgesFromKeys + ";Wedge" + index;

			index++;
		}

		pieChart.setSuppressWedgesFromKey(suppressWedgesFromKeys);
		pieChart.setAppletWidth(110);
		pieChart.setAppletHeight(115);
		pieChart.setPieWidth(110);
		pieChart.setPieHeight(110);
		return pieChart;
	}

	/**
	 * Get the IPS Review Requests for a Contract
	 * within the number of Contract Effective Days 
	 * 
	 * @param contractId
	 * @return List<IPSRReviewRequest>
	 * @throws SystemException
	 */
	protected List<IPSRReviewRequest> getIPSReviewRequests(int contractId)
			throws SystemException {
		List<IPSRReviewRequest> ipsrReviewRequests = new ArrayList<IPSRReviewRequest>();
		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();
		
		Map<String, String> businessParam = EnvironmentServiceDelegate
				.getInstance().getBusinessParamMap();
		
		int ipsReviewMonths = Integer.parseInt(businessParam
				.get(BusinessParamConstants.IPS_REQUESTS_DURATION));
		
		ipsrReviewRequests = ipsrServiceDelegate.getIPSRReviewRequests(
				contractId, ipsReviewMonths);
		return ipsrReviewRequests;
	}

	/**
	 * Populate the form bean with IPS Review Request Details
	 * 
	 * @param ipsReviewRequestList
	 * @param ipsAssistServiceForm
	 * @param contractId
	 * @param isAccessible
	 * @param contractStatus
	 */
	protected void populateIPSReviewDetailsToForm(
			List<IPSRReviewRequest> ipsReviewRequestList,
			IPSAndReviewDetailsForm ipsAssistServiceForm, int contractId,
			boolean isAccessible, String contractStatus, String companyName) throws SystemException {
		List<IPSReviewReportDetailsVO> ipsReviewReportDetailsVOs = new ArrayList<IPSReviewReportDetailsVO>();

		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();

		// Get the IPS Fund Instructions for Contract
		Map<Integer, List<FundInstruction>> ipsReviewFundInstructionsMap = ipsrServiceDelegate
				.getIPSReviewFundInsructionsForContractId(contractId);

		List<FundInstruction> ipsReviewFundInstructionList = null;

		ipsAssistServiceForm.setInterimReportLinkAvailable(false);
		int counter = 0;

		// Get the Proposal Special Fund Code for all Funds for the Contract
		Map<String, String> specialFundIndicatorMap = ipsrServiceDelegate
				.getProposalSpecialFunds(contractId);
		
		// Iterate through the ipsReviewRequestList
		for (IPSRReviewRequest ipsrReviewRequest : ipsReviewRequestList) {
			IPSReviewReportDetailsVO ipsReviewReportDetailsVO = new IPSReviewReportDetailsVO();
			int reviewRequestId = ipsrReviewRequest.getReviewRequestId();
			String reviewRequestStatus = ipsrReviewRequest
					.getReviewRequestStatus() != null ? ipsrReviewRequest
					.getReviewRequestStatus().trim() : Constants.EMPTY_STRING;
			String reviewRequestSubStatus = ipsrReviewRequest
					.getReviewRequestSubStatus() != null ? ipsrReviewRequest
					.getReviewRequestSubStatus().trim()
					: Constants.EMPTY_STRING;

			Date ipsIATEffectiveDate = ipsrReviewRequest.getProcessingDate();
			
			Date annualReviewDate = ipsrReviewRequest.getProjectedReviewDate();

			Date statusDate = ipsrReviewRequest
					.getReviewRequestStateEffectiveDate();
			
			boolean isAllReplacementIgnored = false;

			ipsReviewFundInstructionList = ipsrServiceDelegate.getIPSReviewFundInsructions(contractId, reviewRequestId);
			// Get the IPS Fund Instructions for each Review id
			if (ipsReviewFundInstructionList != null) {
				/*ipsReviewFundInstructionList = ipsReviewFundInstructionsMap
						.get(reviewRequestId);*/
				
				// Check for Fund Instructions is available for the review id
				isAllReplacementIgnored = IPSManagerUtility.isAllFundsInstructionsIgnored(ipsReviewFundInstructionList);
			}

			// Check whether the Review Report is available in
			// eReports server for Contract Id for the Projected Review Date
			boolean isReportAvailable = false;

			try {
				isReportAvailable = ContractServiceDelegate.getInstance()
						.isContractDocAvailable(String.valueOf(contractId),
								annualReviewDate, Constants.IPS_DOCUMENT_TYPE,StringUtils.EMPTY);
			} catch (SystemException e) {
				logger
						.debug("Exception while checking Review Report in eReport"
								+ reviewRequestId);
			}
			
			List<IPSFundInstructionPresentation> ipsFundInstPresentationList = null;
			if(ipsReviewFundInstructionList != null && !ipsReviewFundInstructionList.isEmpty()) {
				ipsFundInstPresentationList = populateIPSFundInstructionPresentation(ipsReviewFundInstructionList);
			}
			
			
			
			boolean allFundInstDisabled=false;
			if(ipsFundInstPresentationList != null && !ipsFundInstPresentationList.isEmpty()) {
				allFundInstDisabled = isAllFundInstructionsInvalid(ipsFundInstPresentationList, specialFundIndicatorMap);
			}
			
			boolean isViewAvailable = false;
			
			if (IPSManagerUtility.isViewAvailable(reviewRequestStatus, reviewRequestSubStatus)
					&& IPSManagerUtility.isInstructionsAvailableForView(ipsFundInstPresentationList)) {
				isViewAvailable = true;
			}
			
			ipsReviewReportDetailsVO.setViewAvailable(isViewAvailable);
			ipsReviewReportDetailsVO.setEditAvailable(IPSManagerUtility.isEditAvailable(reviewRequestStatus, reviewRequestSubStatus, contractStatus, allFundInstDisabled,contractId));
			ipsReviewReportDetailsVO.setCancelAvailable(IPSManagerUtility.isCancelAvailable(reviewRequestStatus, reviewRequestSubStatus, ipsIATEffectiveDate, contractStatus,contractId));
			ipsReviewReportDetailsVO.setParticipantNoticationAvailable(IPSManagerUtility.isParticipantNotificationLinkAvailable(reviewRequestStatus, reviewRequestSubStatus));
			
			ipsReviewReportDetailsVO.setShowNoFundMatchingTresholdIcon(false);
			
			// Check conditions for View, Edit, Cancel icon, Report Status,
			// Report Link
			// based on Review Request Status and Sub Status
			if (!(StringUtils.equals(Constants.IPSR_VOID_STATUS,
					reviewRequestStatus) && StringUtils.equals(
					Constants.IPSR_VOID_SUB_STATUS, reviewRequestSubStatus))) {
				ipsReviewReportDetailsVO.setReviewRequestId(Integer
						.toString(reviewRequestId));
				ipsReviewReportDetailsVO.setContractId(Integer
						.toString(ipsrReviewRequest.getContractId()));

				ipsReviewReportDetailsVO.setAnnualReviewDate(DateRender
						.formatByPattern(annualReviewDate, "",
								MEDIUM_MMMMDDYYYY_COMMA));

				// Set the values for Initiated status
				if (StringUtils.equals(Constants.IPSR_INITIAL_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(
								Constants.IPSR_INITIAL_SUB_STATUS,
								reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO
							.setReviewRequestStatus(INITIATED);
				}
				// Set the values for Pending Analysis status
				else if (StringUtils.equals(Constants.IPSR_PENDING_ANALYSIS_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(
								Constants.IPSR_PENDING_ANALYSIS_SUB_STATUS,
								reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO
							.setReviewRequestStatus(IN_PROGRESS);

					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Review completed status
				else if (StringUtils.equals(Constants.IPSR_PENDING_REPORT_GENERATION_STATUS,
						reviewRequestStatus)
						&& StringUtils
								.equals(
										Constants.IPSR_PENDING_REPORT_GENERATION_SUB_STATUS,
										reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO
							.setReviewRequestStatus(IN_PROGRESS);

					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Pending Approval status
				else if (StringUtils.equals(Constants.IPSR_PENDING_APPROVAL_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(
								Constants.IPSR_PENDING_APPROVAL_SUB_STATUS,
								reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO
							.setReviewRequestStatus(READY_FOR_REVIEW);
					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				}
				// Set the values for Pending completion status
				else if (StringUtils.equals(
						Constants.IPSR_PENDING_COMPLETION_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(Constants.IPSR_PENDING_COMPLETION_SUB_STATUS,
								reviewRequestSubStatus)) {

					if (ipsIATEffectiveDate != null) {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(APPROVED_FUND_CHANGES_FOR
										+ DateRender.formatByPattern(
												ipsIATEffectiveDate, "",
												MEDIUM_MMMDDYYYY));
					}

					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Expired status
				else if (StringUtils.equals(Constants.IPSR_EXPIRED_STATUS
						, reviewRequestStatus)
						&& StringUtils.equals(Constants.IPSR_EXPIRED_SUB_STATUS
								, reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO.setReviewRequestStatus(EXPIRED);

					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Canceled status
				else if (StringUtils.equals(Constants.IPSR_CANCELLED_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(Constants.IPSR_CANCELLED_SUB_STATUS,
								reviewRequestSubStatus)) {
					if (statusDate != null) {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(CANCELLED_ON
										+ DateRender.formatByPattern(
												statusDate, "",
												MEDIUM_MMMDDYYYY));
					}
					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				}
				// Set the values for Partially Completed status
				else if (StringUtils.equals(Constants.IPSR_PARTIALLY_COMPLETE_STATUS
						, reviewRequestStatus) && StringUtils.equals(Constants.IPSR_PARTIALLY_COMPLETE_SUB_STATUS
						, reviewRequestSubStatus)) {
					if (ipsIATEffectiveDate != null) {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(APPROVED_FUND_CHANGES_FOR
										+ DateRender.formatByPattern(
												ipsIATEffectiveDate, "",
												MEDIUM_MMMDDYYYY));
					}
					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Forced Complete status
				else if (StringUtils.equals(
						Constants.IPSR_FORCED_COMPLETE_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(Constants.IPSR_FORCED_COMPLETE_SUB_STATUS,
								reviewRequestSubStatus)) {
					ipsReviewReportDetailsVO.setReviewRequestStatus(OVERRIDDEN);
					
					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				} 
				// Set the values for Completed status
				else if (StringUtils.equals(Constants.IPSR_COMPLETED_STATUS,
						reviewRequestStatus)
						&& StringUtils.equals(Constants.IPSR_COMPLETED_SUB_STATUS,
								reviewRequestSubStatus)) {
					// If there is no Fund Instructions
					if (CollectionUtils.isEmpty(ipsReviewFundInstructionList)
							|| !IPSManagerUtility.isInstructionsAvailableForView(ipsFundInstPresentationList)) {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(NO_FUNDS_CHANGES_IDENTIFIED);
						ipsReviewReportDetailsVO.setShowNoFundMatchingTresholdIcon(true);
					} 
					// If all Replacement Funds are ignored by Trustee
					else if (isAllReplacementIgnored) {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(TRUSTEE_IGNORED_ALL_REPLACEMENTS);
					} 
					// else if the Status is Completed
					else {
						ipsReviewReportDetailsVO
								.setReviewRequestStatus(FUND_CHANGES_COMPLETE);
					}

					if (isReportAvailable) {
						ipsReviewReportDetailsVO
								.setCurrentReportLinkAccessible(true);
					}
				}

				// For Current Review Report
				if (counter == 0) {
					ipsReviewReportDetailsVO.setCurrentReview(true);
				} else {
					ipsReviewReportDetailsVO.setCurrentReview(false);
					String previousReportLabel = "IPSM-"
							+ StringUtils.trim(DateRender.formatByPattern(
									annualReviewDate, "",
									MEDIUM_MMMMDDYYYY_COMMA)) + "-"
							+ StringUtils.trim(companyName);
					ipsReviewReportDetailsVO
							.setPreviousReportLabel(previousReportLabel);
				}
				
				if (ipsReviewFundInstructionList == null
						|| (ipsReviewFundInstructionList != null && ipsReviewFundInstructionList
								.isEmpty())) {
					ipsReviewReportDetailsVO.setViewAvailable(false);
					ipsReviewReportDetailsVO.setEditAvailable(false);
				}

				// Accessible for Internal users and Trustees only
				if (!isAccessible) {
					ipsReviewReportDetailsVO.setEditAvailable(false);
					ipsReviewReportDetailsVO.setCancelAvailable(false);
				}

				counter++;

				ipsReviewReportDetailsVOs.add(ipsReviewReportDetailsVO);
			}
		}

		// Show Interim Report Link if there is no review in the above
		// incremented statuses
		ipsAssistServiceForm.setInterimReportLinkAvailable(IPSManagerUtility
				.isInterimReportLinkAvailable(ipsReviewRequestList,
						contractStatus));
		
		// Sets Review Report Details to Form
		ipsAssistServiceForm
				.setIpsReviewReportDetailsList(ipsReviewReportDetailsVOs);
	}

	/**
	 * Populate the Fund Instructions of a Review request Id to Form Bean
	 * 
	 * @param ipsAssistServiceForm
	 * @param ipsrReviewRequest
	 * @throws SystemException
	 */
	protected void populateIPSReviewFundInstructionToForm(
			IPSReviewResultDetailsForm ipsAssistServiceForm,
			IPSRReviewRequest ipsrReviewRequest, long profileId,
			IPSRServiceDelegate ipsrServiceDelegate) throws SystemException {

		BigDecimal lastUpdatedUserProfile = new BigDecimal(profileId);

		if (ipsrReviewRequest != null) {
			int reviewRequestId = ipsrReviewRequest.getReviewRequestId();
			int contractId = ipsrReviewRequest.getContractId();
			List<FundInstruction> fundInstructions = ipsrServiceDelegate
					.getIPSReviewFundInsructions(contractId, reviewRequestId);
			Date ipsIATEffectiveDate = ipsrReviewRequest.getProcessingDate();
			//HashMap<String, String> fswAssetClasses = FundServiceDelegate
			//		.getInstance().getWarrantyAssetClasses();
			List<FundInstruction> ipsReviewFundInstructionList = new ArrayList<FundInstruction>();

			// Populate the Top Rank for each current Fund
			//Map<String, List<FundInstruction>> topRankedFundInstructionsMap = populateTopRankedFundList(fundInstructions);

			// Get the Proposal Special Fund Code for all Funds for the Contract
			Map<String, String> specialFundIndicatorMap = ipsrServiceDelegate
					.getProposalSpecialFunds(contractId);

			// Get the Fund Redemption Fees for the Contract
			HashMap<String, FundRedemptionFeesVO> fundRedemtionFeesMap = ipsrServiceDelegate
					.getFundRedemptionFeesForContract(contractId);
			
			Set<String> assetClassesWithRankings = getAssetClassesWithRankings(contractId);

			boolean contractRedemptionAvailable = false;
			boolean participantRedemptionFeesAvailable = false;
			String analyzedIndicator = null;
			String reviewRequestStatus = ipsrReviewRequest
					.getReviewRequestStatus() != null ? ipsrReviewRequest
					.getReviewRequestStatus().trim() : Constants.EMPTY_STRING;

			String reviewRequestSubStatus = ipsrReviewRequest
					.getReviewRequestSubStatus() != null ? ipsrReviewRequest
					.getReviewRequestSubStatus().trim()
					: Constants.EMPTY_STRING;

			if (Constants.IPSR_PENDING_COMPLETION_STATUS
					.equalsIgnoreCase(reviewRequestStatus)
					&& Constants.IPSR_PENDING_COMPLETION_SUB_STATUS
							.equalsIgnoreCase(reviewRequestSubStatus)) {
				ipsAssistServiceForm.setParticipantNotificationAvailable(true);
			}

			// Check whether the Review Report is available in
			// eReports server for Contract Id for the Projected Review Date
			boolean isReportAvailable = false;
			
			try {
				isReportAvailable = ContractServiceDelegate.getInstance()
						.isContractDocAvailable(String.valueOf(contractId),
								ipsrReviewRequest.getProjectedReviewDate(),
								Constants.IPS_DOCUMENT_TYPE,StringUtils.EMPTY);
			} catch (SystemException e) {
				logger
						.debug("Exception while checking Review Report in eReport"
								+ reviewRequestId);
			}

			ipsAssistServiceForm.setReportLinkAvailable(isReportAvailable);

			for (FundInstruction fundInstruction : fundInstructions) {

				boolean fromFundHasPartRedemptionFee = isParticipantRedemptionFeeAvailable(
						fundRedemtionFeesMap, fundInstruction.getFromFundVO().getFundCode());
				boolean fromFundHasContractRedemptionFee = isContractRedemptionFeeAvailable(
						fundRedemtionFeesMap, fundInstruction.getFromFundVO().getFundCode());
				boolean toFundHasPartRedemptionFee = isParticipantRedemptionFeeAvailable(
						fundRedemtionFeesMap, fundInstruction.getToFundVO().getFundCode());
				boolean toFundHasContractRedemptionFee = isContractRedemptionFeeAvailable(
						fundRedemtionFeesMap, fundInstruction.getToFundVO().getFundCode());			
				boolean isFromFundClosed = fundInstruction.getFromFundVO().isFundClosed();
				boolean isToFundClosed = fundInstruction.getToFundVO().isFundClosed();
				
				// Get the Special Fund Code for each Current Fund
				if (specialFundIndicatorMap != null) {
					analyzedIndicator = specialFundIndicatorMap
							.get(fundInstruction.getFromFundVO().getFundCode());
				}

				// Set the From Fund as Analyzed Fund if its Indicator is 'A'
				if (StringUtils.equals(Constants.ANALYZED_FUND_IND,
						analyzedIndicator)) {
					fundInstruction.getFromFundVO().setFundAnalyzed(true);
				} else {
					fundInstruction.getFromFundVO().setFundAnalyzed(false);
				}
				
				// Set the contract redemption fee indicator
				if (fromFundHasContractRedemptionFee || toFundHasContractRedemptionFee) {
					contractRedemptionAvailable = true;
				} 
				
				// Set the participant redemption fee indicator
				if(fromFundHasPartRedemptionFee || toFundHasPartRedemptionFee){
					participantRedemptionFeesAvailable = true;
				} 
				
				// Checks condition for Asset Class of Current Fund is different from
				// Asset Class of Fund Instruction
				if (!StringUtils.equals(fundInstruction.getAssetClass(),
						fundInstruction.getFromFundVO().getFundAssetClass())) {
					if (assetClassesWithRankings.contains(fundInstruction.getFromFundVO().getFundAssetClass())) {
						fundInstruction.getFromFundVO()
								.setFundDifferentAssetClsAndFSWAssetCls(true);
					} else {
						fundInstruction.getFromFundVO()
								.setFundDifferentAssetClsAndNonFSWAssetCls(true);
					}
				}
				
				// Checks condition for Asset Class of Top Ranked Fund is
				// different from Asset Class of Fund Instruction
				if (!StringUtils.equals(fundInstruction.getAssetClass(),
						fundInstruction.getToFundVO().getFundAssetClass())) {
					if (assetClassesWithRankings.contains(fundInstruction.getToFundVO().getFundAssetClass())) {
						fundInstruction.getToFundVO()
								.setFundDifferentAssetClsAndFSWAssetCls(true);
					} else {
						fundInstruction.getToFundVO()
								.setFundDifferentAssetClsAndNonFSWAssetCls(true);
					}
				}
				
				fundInstruction.getFromFundVO().setFundSheetLinkAvailable(
						isFundSheetLinkAvailable(reviewRequestStatus,
								reviewRequestSubStatus, isFromFundClosed));
				fundInstruction.getToFundVO().setFundSheetLinkAvailable(
						isFundSheetLinkAvailable(reviewRequestStatus,
								reviewRequestSubStatus, isToFundClosed));
				
				if (ipsAssistServiceForm.isCurrentReview()) {
					// Populates Fund Information Icon tool tip contents
					populateFundInfomartionIcon(fundInstruction,
							fromFundHasPartRedemptionFee,
							fromFundHasContractRedemptionFee,
							toFundHasPartRedemptionFee,
							toFundHasContractRedemptionFee);
				}

				fundInstruction.setLastUpdatedUserProfileId(lastUpdatedUserProfile);

				// If its not a fsw asset class don't show it
				if (assetClassesWithRankings.contains(fundInstruction.getAssetClass())) {
					ipsReviewFundInstructionList.add(fundInstruction);
				}
			}

			// If at least one Fund is having Contract level Redemption Fees
			// display Contract Level Footnote
			if (contractRedemptionAvailable) {
				ipsAssistServiceForm.setContractRedemptionFeesAvailable(true);
			}

			// If at least one Fund is having Participant level Redemption Fees
			// display Participant Level Footnote
			if (participantRedemptionFeesAvailable) {
				ipsAssistServiceForm
						.setParticipantRedemptionFeesAvailable(true);
			}

			// Populate the fund instructions to presentation value object 
			List<IPSFundInstructionPresentation> ipsFundInstPresentationList = populateIPSFundInstructionPresentation(ipsReviewFundInstructionList);

			// Populate the individual action indicator
			if (Constants.IPSR_PENDING_APPROVAL_STATUS
					.equalsIgnoreCase(ipsrReviewRequest
							.getReviewRequestStatus())
					&& Constants.IPSR_PENDING_APPROVAL_SUB_STATUS
							.equalsIgnoreCase(ipsrReviewRequest
									.getReviewRequestSubStatus())) {
				populateActionIndicator(ipsAssistServiceForm,
						ipsFundInstPresentationList, specialFundIndicatorMap);
			}
				
			BeanComparator comparator = new BeanComparator(ASSET_CLS_ORDER);
			Collections.sort(ipsFundInstPresentationList, comparator);
			
			ipsAssistServiceForm.setFundInstructionList(fundInstructions);
			ipsAssistServiceForm
					.setIpsReviewFundInstructionList(ipsFundInstPresentationList);
		}
	}

	/**
	 *  Checks condition for Current Fund and Top Ranked fund to decide whether it will enable & unselected
	 *  or disabled & selected only for Edit Review page
	 * 
	 * @param mode
	 * @param ipsFundInstPresentationList
	 * @param specialFundIndicatorMap
	 */
	 private void populateActionIndicator(
			IPSReviewResultDetailsForm ipsAssistServiceForm,
			List<IPSFundInstructionPresentation> ipsFundInstPresentationList,
			Map<String, String> specialFundIndicatorMap) {

		boolean isAllFundInstructionsIgnored = true;
		for (IPSFundInstructionPresentation ipsFundInstPresentation : ipsFundInstPresentationList) {

			// If all the from funds are not valid then all the radio
			// buttons should be disabled and set as ignored
			boolean allFromFundsAreInvalid = IPSManagerUtility.isAllFromFundsInvalid( ipsFundInstPresentation
					.getFromFundVO(), specialFundIndicatorMap);

			if (allFromFundsAreInvalid) {
				for (ToFundVO toFundVO : ipsFundInstPresentation.getToFundVO()) {
					toFundVO.setActionEnabled(false);
					toFundVO.setActionIndicator(Constants.ACTION_IGNORED);
				}
			} else {
				for (ToFundVO toFundVO : ipsFundInstPresentation.getToFundVO()) {

					// If the to fund is invalid then set them as disabled and
					// set the ind as ignored
					if (!IPSManagerUtility.isToFundValid(toFundVO)) {
						toFundVO.setActionEnabled(false);
						toFundVO.setActionIndicator(Constants.ACTION_IGNORED);
					} else {
						toFundVO.setActionEnabled(true);
						// toFundVO.setActionIndicator(Constants.EMPTY_STRING);
						isAllFundInstructionsIgnored = false;
					}
				}
			}
		}
		
		ipsAssistServiceForm.setAllFundInstructionsIgnored(isAllFundInstructionsIgnored);
		
		// If at least one Action Taken Radio button is enabled
		if (!isAllFundInstructionsIgnored) {
			ipsAssistServiceForm.setIpsIATEffectiveDateAvailable(true);
		}
	}
	
	/**
	 * Populates the fund instruction to the presentation VO. All the fund
	 * instructions are grouped based on the asset class.
	 * 
	 * @param fundInstructionList
	 * @return
	 */
	private List<IPSFundInstructionPresentation> populateIPSFundInstructionPresentation(List<FundInstruction> fundInstructionList){
		
		List<IPSFundInstructionPresentation> ipsFundInstructionPresentationList = new ArrayList<IPSFundInstructionPresentation>();
		
		// Iterate through the fund instruction list and group them based on the asset class
		for(FundInstruction fundInstruction: fundInstructionList){
			IPSFundInstructionPresentation ipsFundInstructionPresentation = null;
			boolean fundInstAlreadyExist = false;
			
			for(IPSFundInstructionPresentation ipsFundInstPresentation:ipsFundInstructionPresentationList){
				if (StringUtils.equals(ipsFundInstPresentation.getAssetClass(),
						fundInstruction.getAssetClass())) {
					ipsFundInstructionPresentation = ipsFundInstPresentation;
					fundInstAlreadyExist = true;
					break;
				}
			}
			
			List<FromFundVO> fromFundVOList = null;
			List<ToFundVO> toFundVOList = null;
			
			if(fundInstAlreadyExist){
				// If the fund instruction already exist then add the to fund and from fund to the existing list
				toFundVOList = ipsFundInstructionPresentation.getToFundVO();
				boolean isToFundAvailable = false;
				boolean isFromFundAvailable = false;
				
				for(ToFundVO toFundVO : toFundVOList){
					
					if (StringUtils.equals(fundInstruction.getToFundVO()
							.getFundCode(), toFundVO.getFundCode())) {
						isToFundAvailable = true;
					}
				}
				
				if(!isToFundAvailable){
					toFundVOList.add(fundInstruction.getToFundVO());
					ipsFundInstructionPresentation.setToFundVO(toFundVOList);
				}
				
				fromFundVOList = ipsFundInstructionPresentation.getFromFundVO();
				
				for(FromFundVO fromFundVO : fromFundVOList){
					
					if (StringUtils.equals(fundInstruction.getFromFundVO()
									.getFundCode(), fromFundVO.getFundCode())) {
						isFromFundAvailable = true;
					}
				}
				
				if(!isFromFundAvailable){
					fromFundVOList.add(fundInstruction.getFromFundVO());
					ipsFundInstructionPresentation.setFromFundVO(fromFundVOList);
				}
				fromFundVOList = ipsFundInstructionPresentation.getFromFundVO();
			} else {
				if (!StringUtils.equals(Constants.SYSTEM_IGNORED,
						fundInstruction.getActionIndicator())) {
					toFundVOList = new ArrayList<ToFundVO>();
					toFundVOList.add(fundInstruction.getToFundVO());
					fromFundVOList = new ArrayList<FromFundVO>();
					fromFundVOList.add(fundInstruction.getFromFundVO());
					ipsFundInstructionPresentation = new IPSFundInstructionPresentation();
					ipsFundInstructionPresentation.setAssetClass(fundInstruction.getAssetClass());
					ipsFundInstructionPresentation.setAssetClassName(fundInstruction.getAssetClassName());
					ipsFundInstructionPresentation.setFromFundVO(fromFundVOList);
					ipsFundInstructionPresentation.setToFundVO(toFundVOList);
					ipsFundInstructionPresentation.setActionIndicator(fundInstruction.getActionIndicator());
					ipsFundInstructionPresentation.setActionEnabled(fundInstruction.isActionEnabled());
					ipsFundInstructionPresentationList.add(ipsFundInstructionPresentation);
				}
			}
		}
		
		List<IPSFundInstructionPresentation> finalPresentationList = new ArrayList<IPSFundInstructionPresentation>();
		
		for (IPSFundInstructionPresentation presentationVO: ipsFundInstructionPresentationList){
			List<FromFundVO> fromFundVOList = presentationVO.getFromFundVO();
			List<ToFundVO> toFundList = presentationVO.getToFundVO();
			List<FromFundVO> tempFromFundVOList = new ArrayList<FromFundVO>();
			
			if(fromFundVOList == null || fromFundVOList.isEmpty()) {
				finalPresentationList.add(presentationVO);
				continue;
			}
			
			for (FromFundVO fromFundVO : fromFundVOList) {
				boolean isFromFundSameAsTopRankedFund = false;
				
				for (ToFundVO toFundVO : toFundList) {
					if (StringUtils.equals(fromFundVO.getFundCode(),
							toFundVO.getFundCode())) {
						isFromFundSameAsTopRankedFund = true;
						break;
					}
				}
				
				if (!isFromFundSameAsTopRankedFund) {
					tempFromFundVOList.add(fromFundVO);
				}
			}
			
			if (!tempFromFundVOList.isEmpty()){
				presentationVO.setFromFundVO(tempFromFundVOList);
				finalPresentationList.add(presentationVO);
			}
			
			BeanComparator comparator = new BeanComparator(MARKETING_SORT_ORDER);
			Collections.sort(fromFundVOList, comparator);
			Collections.sort(toFundList, comparator);
		}
		
		return finalPresentationList;
	}
	
	/**
	 * Checks whether contract redemption fee is applicable
	 * 
	 * @param fundRedemtionFeesMap
	 * @param fundId
	 * @return
	 */
	private boolean isContractRedemptionFeeAvailable(
			Map<String, FundRedemptionFeesVO> fundRedemtionFeesMap,
			String fundId) {

		boolean isContractRedemptionFeeAvailable = false;

		// Set the Redemption Fees for the From Fund & To Fund
		if (fundRedemtionFeesMap != null) {
			FundRedemptionFeesVO fundRedemptionFeesVO = fundRedemtionFeesMap.get(fundId);

			// Set Redemption fees as Available if current fund has redemption fees
			if (fundRedemptionFeesVO != null) {

				if (fundRedemptionFeesVO.isContractRedemptionFeesAvailable()) {
					isContractRedemptionFeeAvailable = true;
				}
			}
		}

		return isContractRedemptionFeeAvailable;
	}

	/**
	 * Checks whether fund sheet link is available
	 * 
	 * @param reviewStatus
	 * @param reviewSubStatus
	 * @param isFundClosed
	 * @return
	 */
	private boolean isFundSheetLinkAvailable(String reviewStatus,
			String reviewSubStatus, boolean isFundClosed) {
		
		boolean isFundSheetLinkAvailable = true;
		
		// If the Status is Completed or Expired or Canceled
		// set the Fund Sheet link is available for Current & Top ranked
		// fund
		if ((Constants.IPSR_COMPLETED_STATUS.equalsIgnoreCase(reviewStatus) 
					&& Constants.IPSR_COMPLETED_SUB_STATUS.equalsIgnoreCase(reviewSubStatus))
				|| (Constants.IPSR_EXPIRED_STATUS.equalsIgnoreCase(reviewStatus) 
					&& Constants.IPSR_EXPIRED_SUB_STATUS.equalsIgnoreCase(reviewSubStatus))
				|| (Constants.IPSR_CANCELLED_STATUS.equalsIgnoreCase(reviewStatus) 
					&& Constants.IPSR_CANCELLED_SUB_STATUS.equalsIgnoreCase(reviewSubStatus))
				|| (isFundClosed)) {
			isFundSheetLinkAvailable = false;
		}
		
		return isFundSheetLinkAvailable;
	}
	
	/**
	 * Checks whether participant redemption fee is applicable
	 * 
	 * @param fundRedemtionFeesMap
	 * @param fundId
	 * @return
	 */
	private boolean isParticipantRedemptionFeeAvailable(
			Map<String, FundRedemptionFeesVO> fundRedemtionFeesMap,
			String fundId) {

		boolean isParticipantRedemptionFeesAvailable = false;

		// Set the Redemption Fees for the From Fund & To Fund
		if (fundRedemtionFeesMap != null) {
			FundRedemptionFeesVO fundRedemptionFeesVO = fundRedemtionFeesMap.get(fundId);

			// Set Redemption fees as Available if current fund has redemption fees
			if (fundRedemptionFeesVO != null) {
				if (fundRedemptionFeesVO.isParticipantRedemptionFeesAvailable()) {
					isParticipantRedemptionFeesAvailable = true;
				}
			}
		}

		return isParticipantRedemptionFeesAvailable;
	}
	
	
	
	/**
	 * This method used to print the PDF in separate window for FRW Landing &
	 * Review Result page.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doPrintPDF(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("Inside doPrintPDF");
		}

		ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(
				actionForm, request);

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline");
		response.setContentLength(pdfOutStream.size());

		try {
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing pdfData.");
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception writing pdfData.");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doPrintPDF");
		}
		return null;
	}

	/**
	 * This method is used to reset the time to 00:00:00 a.m.
	 * 
	 * @param cal
	 * @return GregorianCalendar
	 */
	protected GregorianCalendar resetTheTime(GregorianCalendar cal) {

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		return cal;
	}

	/**
	 * This method used to prepare XML and generate PDF for FRW Landing & Review
	 * Result page.
	 * 
	 * @param actionForm
	 * @param request
	 * @return pdfOutStream
	 * @throws SystemException
	 */
	protected ByteArrayOutputStream prepareXMLandGeneratePDF(
			AutoForm actionForm, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside prepareXMLandGeneratePDF");
		}

		String xmlTree = prepareXMLFromReport(actionForm, request);
		String xsltFileName = getXSLTFileName();
		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		if (xmlTree == null || xsltFileName == null) {
			return pdfOutStream;
		}
		String xsltfile = ReportsXSLProperties.get(xsltFileName);
		String includedXSLPath = ReportsXSLProperties
				.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
		PDFGenerator pdfGenerator = new PDFGenerator();

		URIResolver uriResolver = new XSLFileURIResolver(includedXSLPath);
		try {
			pdfGenerator.generatePDF(xmlTree, xsltfile, uriResolver,
					pdfOutStream);
		} catch (SAXException e) {
			throw new SystemException(e,
					"Problem occurs while parsing the VO to xml.");
		} catch (IOException e) {
			throw new SystemException(e,
					"Problem occurs while writing pdfData.");
		} catch (TransformerException e) {
			throw new SystemException(e,
					"Problem occurs while transform xml data to pdf.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting prepareXMLandGeneratePDF");
		}
		return pdfOutStream;
	}

	/**
	 * This method needs to be overridden by Landing & Review Result page action
	 * classes that needs PDF Generation functionality. This method would
	 * generate the XML file.
	 * 
	 * @param actionForm
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	protected String prepareXMLFromReport(AutoForm actionForm,
			HttpServletRequest request) throws SystemException {
		return null;
	}

	/**
	 * This method needs to be overridden by Landing & Review Result page action
	 * classes that needs PDF Generation functionality. This method would return
	 * the key present in ReportsXSL.properties file. This key has the value as
	 * path to XSLT file, which will be used during PDF generation.
	 * 
	 * @return String
	 */
	protected String getXSLTFileName() {
		return null;
	}
	
	/**
	 * This method is used to get the Participant Notification Details
	 * 
	 * @param request
	 * @param response
	 * @param contract
	 * @throws SystemException
	 * @throws IOException
	 */
	protected void populateParticipantNotificationDetails(
			HttpServletRequest request, HttpServletResponse response,
			Contract contract, IPSViewParticiapantNotificationForm ipsParticipantNotificationForm,
			int requestId, Collection<GenericException> errorMessages,boolean isPswApp)
			throws SystemException, IOException {
		String contactNo=new String();
		String comments=new String();
		ParticipantNotificationInfo info = new ParticipantNotificationInfo();

		int contractId = new Integer(contract.getContractNumber());
		if (isPswApp) {
			String contactName = ipsParticipantNotificationForm.getContactName();
			String street =ipsParticipantNotificationForm.getStreet();
			String cityAndState =ipsParticipantNotificationForm.getCityAndState();
			String ZipCode =ipsParticipantNotificationForm.getZipCode();
			PhoneNumber phoneNumber = ipsParticipantNotificationForm.getTelephoneNumber();
			String areaCode= phoneNumber.getAreaCode();
			String phonePrefix= phoneNumber.getPhonePrefix();
			String phoneSuffix= phoneNumber.getPhoneSuffix();
			if(areaCode!=null && phonePrefix!=null && phoneSuffix!=null){
			contactNo = OPEN_BRACE.concat(areaCode).concat(CLOSED_BRACE).concat(phonePrefix).concat(DASH).concat(phoneSuffix);
			}
			comments = ipsParticipantNotificationForm.getComments();
			

			if (StringUtils.isNotBlank(contactName) && StringUtils.isNotBlank(contactNo)) {
				info.setContactName(contactName);
				info.setContactNumber(contactNo);
				info.setWordGroup(PdfConstants.WORD_GROUP1);
			} 
			
			if (StringUtils.isNotBlank(street)) {
				info.setStreet(street);
			} 
			if (StringUtils.isNotBlank(cityAndState)) {
				info.setCityAndState(cityAndState);
			} 
			if (StringUtils.isNotBlank(ZipCode)) {
				info.setZipCode(ZipCode);
			} 

			if (StringUtils.isNotBlank(comments)) {
				info.setFlag(true);
				info.setComments(comments);
			} else {
				info.setComments(Constants.EMPTY_STRING);
			}
		} else {
			info.setWordGroup(PdfConstants.WORD_GROUP2);
		}

		// This URL is used in framing the fund sheet URL.
		String url = CommonEnvironment.getInstance().getFundSheetURL();
		
		IPSRReviewRequest ipsrReviewRequest = IPSRServiceDelegate.getInstance()
				.getIPSRReviewRequestForRequestId(requestId);
		info.setContractId(contractId);
		info.setUrl(url);
		info.setFundSeries(contract.getFundPackageSeriesCode());
		info.setProductId(contract.getProductId());
		BaseEnvironment baseEnvironment = new BaseEnvironment();
		
		// Setting the Site Location.
		if (contract.getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA)) {
			info.setSiteLocation(CommonConstants.COUNTRY_USA);
			URL = baseEnvironment.getNamingVariable(
					EZK_USA_NAME_SPACE_BINDING, null);
			info.setSiteUrl(URL);
		}
		if (contract.getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			info.setSiteLocation(CommonConstants.COUNTRY_NY);
			URL = baseEnvironment.getNamingVariable(
					EZK_NY_NAME_SPACE_BINDING, null);
			info.setSiteUrl(URL);
		}

		// Get the notification PDF
		byte[] result = null;
		try {
			result = IPSRServiceDelegate.getInstance()
					.getParticipantNotification(requestId, info);
		} catch (FundSheetNotAvailableException e) {
			logger.error("Exception occurred while getting Participant Notification", e);
			errorMessages.add(new GenericException(
					CommonErrorCodes.FUND_SHEET_NOT_AVAILABLE_EXCEPTON));
		} 
		
		if (errorMessages.isEmpty()) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(result);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("application/pdf");
			String filename = "Participant-Notification-"
					+ DateRender.formatByPattern(
							ipsrReviewRequest.getProcessingDate(),
							Constants.EMPTY_STRING,
							CommonConstants.MEDIUM_MDY_DASHED) + "-"
					+ contract.getCompanyName().trim() + ".pdf";
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.setContentLength(outputStream.size());

			try {
				ServletOutputStream sos = response.getOutputStream();
				outputStream.writeTo(sos);
				sos.flush();
			} catch (IOException ioException) {
				logger.error("IOException occurred while writing Participant Notification", ioException);
				throw new SystemException(ioException,
					"Exception writing participant notification pdfData.");
			} finally {
				try {
					response.getOutputStream().close();
				} catch (IOException ioException) {
					logger.error("IOException occurred while closing OutputStream Participant Notification", ioException);
					throw new SystemException(ioException,
							"Exception writing participant notification pdfData.");
				}
			}
		}
	}
	
	/**
	 * Loads the Months for New Service Month drop down
	 * 
	 * @return
	 */
	protected static Map<String, String> loadMonths() {
		Map<String, String> monthMap = new LinkedHashMap<String, String>();

		monthMap.put("01", "January");
		monthMap.put("02", "Febuary");
		monthMap.put("03", "March");
		monthMap.put("04", "April");
		monthMap.put("05", "May");
		monthMap.put("06", "June");
		monthMap.put("07", "July");
		monthMap.put("08", "August");
		monthMap.put("09", "September");
		monthMap.put("10", "October");
		monthMap.put("11", "November");
		monthMap.put("12", "December");

		return monthMap;
	}
	
	/**
	 * Populates Fund Information Icon tool tip contents
	 * 
	 * @param fundInstruction
	 * @param fswAssetClasses
	 * @throws ContentException
	 */
	private void populateFundInfomartionIcon(FundInstruction fundInstruction, 
			boolean fromFundHasPartRedemptionFee, boolean fromFundHasContractRedemptionFee,
			boolean toFundHasPartRedemptionFee, boolean toFundHasContractRedemptionFee) {

		FromFundVO fromFundVO = fundInstruction.getFromFundVO();
		ToFundVO toFundVO = fundInstruction.getToFundVO();

		StringBuffer fromFundInfoBuffer = new StringBuffer();
		String messageText = null;

		// Condition for Current Fund Information Icon
		if (StringUtils.isNotBlank(fundInstruction.getFromFundVO()
				.getFundCode())) {
			if (fromFundHasPartRedemptionFee || fromFundHasContractRedemptionFee) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_REDEMTION_FEES_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);
				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (!fundInstruction.getFromFundVO().isFundAnalyzed()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_NOT_ANALYZED_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (fundInstruction.getFromFundVO().isFundClosed()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_CLOSED_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (!fundInstruction.getFromFundVO().isFundSelected()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_DESELECTED_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (fundInstruction.getFromFundVO().isDIOFund()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_DIO_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (fundInstruction.getFromFundVO()
					.isFundDifferentAssetClsAndFSWAssetCls()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_DIFFERENT_FSW_ASSETCLS_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}

			if (fundInstruction.getFromFundVO()
					.isFundDifferentAssetClsAndNonFSWAssetCls()) {
				messageText = ContentHelper
						.getContentText(
								CommonContentConstants.IPS_DIFFERENT_NON_FSW_ASSETCLS_CURRENT_FUND_INSTRUCTION_ICON_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS,
								null);

				buildToolTipText(fromFundInfoBuffer, messageText);
			}
			
		}
		
		if (StringUtils.isBlank(fundInstruction.getFromFundVO().getFundCode())) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_NO_CURRENT_FROM_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS,
							null);

			buildToolTipText(fromFundInfoBuffer, messageText);
		}
		
		if (StringUtils.isNotBlank(fromFundInfoBuffer.toString())) {
			fromFundVO.setFundInformation(fromFundInfoBuffer.toString()
					.replaceAll("'", "").trim());
		}

		StringBuffer toFundInfoBuffer = new StringBuffer();

		// Condition for Top Ranked Fund Information Icon
		if (toFundHasPartRedemptionFee || toFundHasContractRedemptionFee) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_REDEMTION_FEES_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);
		}

		if (fundInstruction.getToFundVO().isFundClosed()) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_CLOSED_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);
		}

		if (fundInstruction.getToFundVO().isFundClosedToNewBusiness()) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_CLOSED_NEW_BUSINESS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);
		}

		if (fundInstruction.getToFundVO().isDIOFund()) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_DIO_TOP_RANKED_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);
		}

		if (fundInstruction.getToFundVO()
				.isFundDifferentAssetClsAndFSWAssetCls()) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_DIFFERENT_FSW_ASSETCLS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);
		}

		if (fundInstruction.getToFundVO()
				.isFundDifferentAssetClsAndNonFSWAssetCls()) {
			messageText = ContentHelper
					.getContentText(
							CommonContentConstants.IPS_DIFFERENT_NON_FSW_ASSETCLS_TOP_RANKED_FUND_INSTRUCTION_ICON_TEXT,
							ContentTypeManager.instance().MISCELLANEOUS, null);

			buildToolTipText(toFundInfoBuffer, messageText);

		}

		if (StringUtils.isNotBlank(toFundInfoBuffer.toString())) {
			toFundVO.setFundInformation(toFundInfoBuffer.toString().replaceAll(
					"'", ""));
		}

		fundInstruction.setFromFundVO(fromFundVO);
		fundInstruction.setToFundVO(toFundVO);
	}
	
	/**
	 * Builds the tool tip text with <br>
	 * tag
	 * 
	 * @param stringBuffer
	 * @param text
	 */
	private void buildToolTipText(StringBuffer stringBuffer, String text) {
		if (StringUtils.isNotBlank(stringBuffer.toString())) {
			stringBuffer.append("<br>").append(StringUtils.trim(text));
		} else {
			stringBuffer.append(StringUtils.trim(text));
		}
	}
	
	/**
	 * Action Method to open Overlay page 
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	public String doChange(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {

		String reviewRequestId = request.getParameter("reviewRequestId");
		
		request.setAttribute("reviewRequestId", reviewRequestId);
		
		String isFromLandingPage = request.getParameter("isFromLandingPage");
		
		request.setAttribute("isFromLandingPage", isFromLandingPage);
		
		//return mapping.findForward("additionalParameterOverlay");
		return "additionalParameterOverlay";
	}
	
	/**
	 * Gets the content for the contentKey
	 * 
	 * @param key
	 * @return Content
	 * @throws ContentException
	 */
	protected Content getContent(int key) throws ContentException {
		return BrowseServiceDelegate.getInstance().findContentByKey(key);
	}
	
	/**
	 * Gets the whether IPS Service is available for the Contract or not
	 * 
	 * @param contractId
	 * @return boolean
	 * @throws SystemException
	 */
	protected boolean isIPSServiceAvailable(int contractId)
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

	/**
	 * Populate the IAT effective date calendar start date and end date
	 * 
	 * @param ipsAssistServiceForm
	 * @param expiryDate
	 * @throws SystemException
	 */
	protected void populateValidIPSIATEffectiveDates(
			IPSReviewResultDetailsForm ipsAssistServiceForm, Date expiryDate)
			throws SystemException {
		
		Map<String, String> businessParam = EnvironmentServiceDelegate
		.getInstance().getBusinessParamMap();

		int iatEffectiveDateRange = Integer.parseInt(businessParam
				.get(BusinessParamConstants.IAT_EFFECTIVE_DATE_RANGE));

		// Start date is current date + 45 days
		Calendar currentDateCal = Calendar.getInstance();
		currentDateCal.add(Calendar.DAY_OF_MONTH, iatEffectiveDateRange);

		// End date is expire date + 45 days
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.setTime(expiryDate);
		endDateCal.add(Calendar.DAY_OF_MONTH, iatEffectiveDateRange);
		// reset the time stamp and just take the dates
		IPSManagerUtility.resetTheTime(currentDateCal);
		IPSManagerUtility.resetTheTime(endDateCal);
		
		if (currentDateCal.before(endDateCal)) {
			ipsAssistServiceForm.setIatStartDate(DateRender.format(
					currentDateCal.getTime(),
					RenderConstants.MEDIUM_MDY_SLASHED));
			/*ipsAssistServiceForm.setIpsIatEffectiveDate((DateRender.format(
					currentDateCal.getTime(),
					RenderConstants.MEDIUM_MDY_SLASHED)));*/
			StringBuffer buffer = new StringBuffer("[");
			while (!currentDateCal.after(endDateCal)) {
				buffer.append("new Date(");
				buffer.append(currentDateCal.get(Calendar.YEAR) + ","
						+ currentDateCal.get(Calendar.MONTH) + ","
						+ currentDateCal.get(Calendar.DAY_OF_MONTH));
				buffer.append("),");
				currentDateCal.add(Calendar.DAY_OF_MONTH, 1);
				
				if (currentDateCal.getTime().equals(endDateCal.getTime())) {
					buffer.append("new Date(");
					buffer.append(currentDateCal.get(Calendar.YEAR) + ","
							+ currentDateCal.get(Calendar.MONTH) + ","
							+ currentDateCal.get(Calendar.DAY_OF_MONTH));
					buffer.append("),");
				}
			}
			buffer.setCharAt(buffer.length() - 1, ']');
			ipsAssistServiceForm.setValidDatesForJavaScript(buffer.toString());
		}
		else
		{
			/*ipsAssistServiceForm.setIpsIatEffectiveDate((DateRender.format(
					currentDateCal.getTime(),
					RenderConstants.MEDIUM_MDY_SLASHED)));*/
		}
	}

	/**
	 * This method will trigger the status change event.
	 * 
	 * @param ipsrReviewRequest
	 * @throws SystemException
	 */
	protected void createStatusChangeEvent(IPSRReviewRequest ipsrReviewRequest)
			throws SystemException {

		if(ipsrReviewRequest != null) {
			// It will create IPSR Status Change Event
			IPSRStatusChangeEvent ipsrStatusChangeEvent = new IPSRStatusChangeEvent();
			ipsrStatusChangeEvent.setInitiator(Event.SYSTEM_USER_PROFILE_ID);
			ipsrStatusChangeEvent.setContractId(ipsrReviewRequest.getContractId());
			ipsrStatusChangeEvent.setReviewRequestStatus(ipsrReviewRequest
					.getReviewRequestStatus());
			ipsrStatusChangeEvent.setReviewRequestSubStatus(ipsrReviewRequest
					.getReviewRequestSubStatus());
			EventClientUtility.getInstance(null).prepareAndSendJMSMessage(
					ipsrStatusChangeEvent);
		}
	}
	
	/**
	 * Method to get the Year for Annual Review Date
	 * 
	 * @param annualReviewDate
	 * @return serviceReviewDate
	 */
	protected Date getServiceReviewDate(DayOfYear annualReviewDate) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getServiceReviewDate");
		}

		Calendar currentCalendar = Calendar.getInstance();
		int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
		int currentYear = currentCalendar.get(Calendar.YEAR);
		int currentDate = currentCalendar.get(Calendar.DATE);

		GregorianCalendar reviewCalendar = new GregorianCalendar();

		if (annualReviewDate.getMonth() > currentMonth) {
			reviewCalendar.set(Calendar.YEAR, currentYear);
		} else if (annualReviewDate.getMonth() == currentMonth) {
			if (annualReviewDate.getDay() >= currentDate) {
				reviewCalendar.set(Calendar.YEAR, currentYear);
			} else {
				reviewCalendar.set(Calendar.YEAR, currentYear + 1);
			}
		}  else {
			reviewCalendar.set(Calendar.YEAR, currentYear + 1);
		}
		
		reviewCalendar.set(Calendar.MONTH, annualReviewDate.getMonth()-1);
		reviewCalendar.set(Calendar.DAY_OF_MONTH, annualReviewDate.getDay());
		reviewCalendar = resetTheTime(reviewCalendar);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getServiceReviewDate");
		}
		
		return reviewCalendar.getTime();
	}
	
	/**
	 * Populates Details for Annual Review Report PDF
	 * 
	 * @param request
	 * @param contract
	 * @param response
	 * @param errors
	 * @throws NumberFormatException
	 * @throws SystemException
	 */
	protected void populateReviewReport(HttpServletRequest request,
			Contract contract, HttpServletResponse response,
			Collection<GenericException> errors) throws NumberFormatException,
			SystemException {
		String reviewRequestId = request.getParameter("reviewRequestId");

		IPSRServiceDelegate delegate = IPSRServiceDelegate.getInstance();

		IPSRReviewRequest ipsrReviewRequest = delegate
				.getIPSRReviewRequestForRequestId(Integer
						.parseInt(reviewRequestId));

		IPSDocumentInfo documentInfo = null;
		DocumentFileOutput outFileInfo = null;
		byte[] reviewReport = null;
		Date annualReviewDate = null;

		try {
			if (ipsrReviewRequest != null) {
				annualReviewDate = ipsrReviewRequest.getProjectedReviewDate();

				FastDateFormat fastDateFormat = FastDateFormat
						.getInstance(com.manulife.pension.service.contract.util.Constants.MEDIUM_MMMMDDYYYY);

				if (annualReviewDate != null) {
					// Checks for whether Review Report Available for the Contract and Review Date
					if (ContractServiceDelegate.getInstance().isContractDocAvailable(
									String.valueOf(contract.getContractNumber()), annualReviewDate,
									com.manulife.pension.service.contract.util.Constants.IPS_DOCUMENT_TYPE,StringUtils.EMPTY)) {
						
						documentInfo = new IPSDocumentInfo(String
								.valueOf(ipsrReviewRequest.getContractId()),
								fastDateFormat.format(annualReviewDate));
						if (documentInfo != null) {
							outFileInfo = EReportsServiceDelegate.getInstance()
									.getDocument(documentInfo);

							if (outFileInfo != null) {
								int reportLength = outFileInfo.getLength();
								if (reportLength != 0) {
									reviewReport = outFileInfo
											.getReportFragment();
								}
							}
						}

						ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();

						String companyName = "";
								
						if(StringUtils.isNotBlank(contract.getCompanyName())) {
							String compName = contract.getCompanyName().trim();
							companyName = compName.replaceAll(" ", "-");
						}
						pdfOutStream.write(reviewReport);
						response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						response.setHeader("Pragma", "no-cache");						
						response.setContentType("application/pdf");
						String filename = "IPSM-"
								+ DateRender.formatByPattern(annualReviewDate,
										Constants.EMPTY_STRING,
										CommonConstants.MEDIUM_MDY_DASHED)
								+ "-" + companyName
								+ ".pdf";
						response.setHeader("Content-Disposition",
								"attachment; filename=\"" + filename + "\"");
						response.setContentLength(pdfOutStream.size());
						ServletOutputStream sos = response.getOutputStream();
						pdfOutStream.writeTo(sos);
						sos.flush();

					} else {
						errors.add(new GenericException(
								CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
					}
				}
			}
		} catch (SystemException systemException) {
			logger.error("SystemException occurred while getting Annual Review Report from EReports", systemException);
			errors.add(new GenericException(
					CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (IOException ioException) {
			logger.error("IOException occurred while getting Annual Review Report from EReports", ioException);
			errors.add(new GenericException(
					CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} catch (Exception exception) {
			logger.error("Exception occurred while getting Annual Review Report from EReports", exception);
			errors.add(new GenericException(
					CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		} finally {
			try {
				if(errors.isEmpty()) {
					response.getOutputStream().close();
				}
			} catch (IOException ioException) {
				throw new SystemException(ioException,
						"Exception writing pdfData.");
			}
		}
	}
	
	
	/**
	 * This method is used to check at least one fund instruction having enabled radio button for a review id.
	 * 
	 * @param ipsFundInstPresentationList
	 * @param specialFundIndicatorMap
	 * @return
	 */
	private boolean isAllFundInstructionsInvalid(List<IPSFundInstructionPresentation> ipsFundInstPresentationList,
			Map<String, String> specialFundIndicatorMap){
		boolean isAllFundInstructionsIgnored = true;
		for (IPSFundInstructionPresentation ipsFundInstPresentation : ipsFundInstPresentationList) {

			// If all the from funds are not valid then all the radio
			// buttons should be disabled and set as ignored
			boolean allFromFundsAreInvalid = IPSManagerUtility.isAllFromFundsInvalid( ipsFundInstPresentation
					.getFromFundVO(), specialFundIndicatorMap);

			if (!allFromFundsAreInvalid){
				for (ToFundVO toFundVO : ipsFundInstPresentation.getToFundVO()) {

					// If the to fund is invalid then set them as disabled and
					// set the ind as ignored
					if (IPSManagerUtility.isToFundValid(toFundVO)) {
						isAllFundInstructionsIgnored = false;
					}
				}
			}
		}
		
		return isAllFundInstructionsIgnored;
	}
	
	private Set<String> getAssetClassesWithRankings(int contractId) throws SystemException {
		
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		
		HashMap<String, List<String>> contractFunds = contractServiceDelegate.getContractFundsByAssetClass(contractId);
		List<String> candidateFundList= new ArrayList<String>();
		for(Entry<String, List<String>> entry : contractFunds.entrySet()) {
	    	String assetClass = entry.getKey();
	    	if(!ignoredAssetClasses.contains(assetClass)) {
	    		candidateFundList.addAll(entry.getValue());
	    	}
	    }
		
		Date fundMetricsAsOfDate = FundServiceDelegate.getInstance().getFundMetricsCurrentAsOfDate();
		
		Set<String> assetClassesWithRankedFundsForIPSMonitoring = new LinkedHashSet<String>();
		
		InvestmentPolicyStatementVO investmentPolicyStatementVO = contractServiceDelegate.getIPSCriteria(contractId);
		RankingCriteria rankingCriteria = populateRankingCriteria(investmentPolicyStatementVO);
		
		FundEvaluationEngineDelegate fundEvaluationEngineDelegate = FundEvaluationEngineDelegate.getInstance();
		
		try {
			DataSource dataSource = JdbcHelper.getCachedDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
			HashMap<String, List<FundRank>> dateBasedFundRank = fundEvaluationEngineDelegate
						.getTopRankedFundsInAssetClass(dataSource, candidateFundList, rankingCriteria,
								Constants.RATE_TYPE, fundMetricsAsOfDate);
			assetClassesWithRankedFundsForIPSMonitoring = dateBasedFundRank.keySet();
		} catch (EngineException e) {
			throw new SystemException(e, "Exception ranking funds");
		} catch (NamingException e) {
			throw new SystemException(e, "Exception getting datasource.");
		}
		
		return assetClassesWithRankedFundsForIPSMonitoring;
	}
	
	/**
	 * method to populate Ranking Criteria
	 * 
	 * @param investmentPolicyStatementVO
	 * @return RankingCriteria
	 */
	private RankingCriteria populateRankingCriteria(InvestmentPolicyStatementVO investmentPolicyStatementVO){
		RankingCriteria rankingCriteria = new RankingCriteria();
		Map<String, Integer> investmentPolicyStatementCriteria = investmentPolicyStatementVO.getInvestmentPolicyStatementCriteria();
		
		for (Iterator<Map.Entry<String, Integer>> iterator = investmentPolicyStatementCriteria
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Integer> e = (Map.Entry<String, Integer>) iterator
					.next();
			String criteria = (String) e.getKey();
			Integer weighting = (Integer) e.getValue();
			BigDecimal weightings = new BigDecimal(weighting.intValue());
			
			if (Constants.THREE_YEAR_RETURN.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.ThreeYearReturn, weightings);
			} if (Constants.FIVE_YEAR_RETURN.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.FiveYearReturn, weightings);
			} if (Constants.TEN_YEAR_RETURN.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.TenYearReturn, weightings);
			} else if (Constants.ALPHA.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(Measurement.Alpha,
						weightings);
			} else if (Constants.SHARP_RATIO.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.SharpRatio, weightings);
			} else if (Constants.INFORMATION_RATIO.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.InformationRatio, weightings);
			} else if (Constants.R_SQUARED.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.RSquared, weightings);
			} else if (Constants.UPSIDE_CAPTURE.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.UpSideCapture, weightings);
			} else if (Constants.DOWNSIDE_CAPTURE.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.DownSideCapture, weightings);
			} else if (Constants.STANDARD_DEVIATION.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.StandardDeviation, weightings);
			} else if (Constants.BETA.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(Measurement.Beta,
						weightings);
			} else if (Constants.EXPENSE_RATIO.equals(criteria)) {
				rankingCriteria.addMeasurementWeighting(
						Measurement.ExpenseRatio, weightings);
			}
		}
		return rankingCriteria;
	}
}
