package com.manulife.pension.bd.web.bob.contract;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.planReview.BasePlanReviewReportController;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportForm;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PeriodEndingReportDateVO;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.util.DateComparator;
import com.manulife.pension.util.PlanReviewConstants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;
import com.manulife.util.render.RenderConstants;

/**
 * This action class builds the Step 1 Individual Contract Review page.
 * 
 * @author Vanikishore
 * 
 */
@Controller
@RequestMapping( value ="/bob")
@SessionAttributes({"planReviewReportForm"})

public class IndividualPlanReviewReportStep1Controller extends
		BasePlanReviewReportController {
	@ModelAttribute("planReviewReportForm") 
	public PlanReviewReportForm populateForm()
	
	{
		return new PlanReviewReportForm();
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/planReview/step1IndividualPlanReviewReport.jsp");
		forwards.put("default","/contract/planReview/step1IndividualPlanReviewReport.jsp");
		forwards.put( "customize", "redirect:/do/bob/contract/planReview/Customize/");
		forwards.put( "continue","redirect:/do/bob/contract/planReview/Customize/");
		forwards.put("history","redirect:/do/bob/contract/planReview/History/");
		forwards.put("homePage","redirect:/do/home/");
	}

	protected static final Logger logger = Logger.getLogger(IndividualPlanReviewReportStep1Controller.class);
	
	private int[] DATE_COMPARISON_FIELDS = new int[] { Calendar.YEAR,
			Calendar.MONTH, Calendar.DATE };

	/**
	 * Constructor class.
	 */
	public IndividualPlanReviewReportStep1Controller() {
		super(IndividualPlanReviewReportStep1Controller.class);
	}
	
	public static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);

	/**
	 * This method will used to handle the data retrieval when user visits the
	 * pages directly.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */ 

	
	@RequestMapping(value ="/contract/planReview/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        /*if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }*/
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}


		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault() in IndividualContractReviewReportStep1Action");
		}

		List<PlanReviewReportUIHolder> contractReviewReportVOList = new ArrayList<PlanReviewReportUIHolder>();
		List<Integer> contractList = new ArrayList<Integer>();
		List<PeriodEndingReportDateVO> monthEndDate = new ArrayList<PeriodEndingReportDateVO>();

		BobContext bobContext = getBobContext(request);
		Contract currentContract = bobContext.getCurrentContract();
		
		if (!actionForm.isRequestBackFromStep2()) {

			contractList.add(currentContract.getContractNumber());

			actionForm.clear();

			PlanReviewReportUIHolder contractReviewReportVO = new PlanReviewReportUIHolder();
			contractReviewReportVO.setContractNumber(currentContract
					.getContractNumber());
			contractReviewReportVO.setContractName(currentContract
					.getCompanyName());
			contractReviewReportVO.setContractStatusCode(currentContract
					.getStatus());
			contractReviewReportVO.setContractEffectiveDate(currentContract
					.getEffectiveDate());
			contractReviewReportVO
					.setContractStatusEffectiveDate(currentContract
							.getContractStatusEffectiveDate());
			contractReviewReportVO
					.setIndustrySegementOptions(!isDefinedBenefitContract(currentContract
							.getProductId().trim()) ? PlanReviewReportUtils
							.getPlanReviewIndustrySegmentList(request) : null);
			
			contractReviewReportVO.setDefinedBenefitContract(isDefinedBenefitContract(currentContract
							.getProductId().trim()));

			monthEndDate = PlanReviewReportUtils
					.getActivePlanReviewPeriodEndDates(isPlanReviewAdminUser(request));

			// checking the contract effective date is less than 3 months prior
			// to
			// the most recent Report Month End Date
			setRecentMonthEndDatesForContracts(actionForm,
					monthEndDate, contractReviewReportVO, currentContract);

			contractReviewReportVOList.add(contractReviewReportVO);

			actionForm
					.setContractReviewReportVOList(contractReviewReportVOList);
		}
		
		// set to default value.
		actionForm.setRequestBackFromStep2(false);
				
		// Populate Default Industry segment options for contract
		setDefaultPlansponsorMagazineIndustryCodeForContracts(actionForm
				.getContractReviewReportVOList());

		populateDisplayContractReviewReports(
				actionForm.getContractReviewReportVOList(),
				actionForm.getDisplayContractReviewReports());

		 forward = doCommon( actionForm, request, response);

		actionForm.setWarningExist(false);
		
		request.getSession(false).setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE, BDConstants.PR_CONTRACT_LEVEL_PARAMETER);
		
		request.getSession(false)
				.setAttribute(
						BDConstants.IS_DB_CONTRACT,
						isDefinedBenefitContract(currentContract.getProductId()
								.trim()));
		
		request.getSession(false).removeAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault() in IndividualContractReviewReportStep1Action.");
		}

		return forwards.get(forward);
	}

	private void setRecentMonthEndDatesForContracts(
			PlanReviewReportForm planReviewReportForm,
			List<PeriodEndingReportDateVO> monthEndDates,
			PlanReviewReportUIHolder contractReviewReportVO,
			Contract currentContract) throws SystemException {
		Date recentMonthEndDate = null;
		// List<LabelValueBean> dateList=
		// dateMap.get(contractReviewReportVO.getContractNumber());
		if (monthEndDates != null && !monthEndDates.isEmpty()) {

			populateValidMonthEndDates(monthEndDates,
					 currentContract,  contractReviewReportVO);
			
			List<PeriodEndingReportDateVO> validMonthEndDates = contractReviewReportVO.getReportMonthEndDates();
			
			if(validMonthEndDates != null && !validMonthEndDates.isEmpty()) {
				try {
					recentMonthEndDate = SHORT_MDY_FORMATTER.parse(PlanReviewReportUtils
							.getRecentPeriodEndingDate(validMonthEndDates));
				} catch (ParseException c) {
					throw new SystemException(c,
							"Exception occured while Parsing the recent date."
									+ "for contract "
									+ contractReviewReportVO.getContractNumber()
									+ " recent month end date is"
									+ recentMonthEndDate);
				}
			}
		}
		validatePlanReviewReport(contractReviewReportVO, recentMonthEndDate,
				currentContract.getEffectiveDate(),
				currentContract.getStatus());

		if (!(contractReviewReportVO.getPlanReviewReportDisabled())) {
			planReviewReportForm.setDisabledContract(true);
		}else{
			planReviewReportForm.setDisabledContract(false);
		}

	}

	/**
	 * To Compare the month end dates and effect date of the contract as per
	 * BPRB.47
	 * 
	 * @param monthEndDates
	 * @param contractReviewReportVO
	 */

	private void populateValidMonthEndDates(
			List<PeriodEndingReportDateVO> monthEndDates,
			Contract currentContract, PlanReviewReportUIHolder contractReviewReportVO)
			throws SystemException {
		
		List<PeriodEndingReportDateVO> validMonthEndDates = new ArrayList<PeriodEndingReportDateVO>();
		try {

			if (StringUtils.equals(BDConstants.CONTRACT_STATUS_DI,
					currentContract.getStatus())) {
				
				for (PeriodEndingReportDateVO monthend : monthEndDates) {
					
					Calendar cutoffCal = Calendar.getInstance();
					cutoffCal.setTime( currentContract.getEffectiveDate());
					cutoffCal.add(Calendar.MONTH, BDConstants.THREE_MONTHS);
					Date cutOffDate = cutoffCal.getTime();
					
					if (DateComparator.compare(SHORT_MDY_FORMATTER.parse(monthend
							.getPeriodEndingReportDate()),
							currentContract.getContractStatusEffectiveDate(),
							DATE_COMPARISON_FIELDS) < 0
							&& DateComparator.compare(SHORT_MDY_FORMATTER.parse(monthend
									.getPeriodEndingReportDate()),
									cutOffDate, DATE_COMPARISON_FIELDS) > 0) {
						validMonthEndDates.add(monthend);
					}
				}
			} else {
				
				for (PeriodEndingReportDateVO monthend : monthEndDates) {
					
					Calendar cutoffCal = Calendar.getInstance();
					cutoffCal.setTime(currentContract.getEffectiveDate());
					cutoffCal.add(Calendar.MONTH, BDConstants.THREE_MONTHS);
					Date cutOffDate = cutoffCal.getTime();
					
					if (DateComparator.compare(SHORT_MDY_FORMATTER.parse(monthend
							.getPeriodEndingReportDate()),
							cutOffDate, DATE_COMPARISON_FIELDS) > 0) {
						validMonthEndDates.add(monthend);
					}
				}
			}
		} catch (ParseException exception) {
			throw new SystemException( exception,
					"Exception occured while Parsing the recent date."
							+ "for contract "
							+ currentContract.getContactId() + " :: [ " + currentContract + " ]");
		}

		contractReviewReportVO.setReportMonthEndDates(validMonthEndDates);
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
			final BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon() in IndividualContractReviewReportStep1Action");
		}

		PlanReviewReportForm planReviewReportForm = (PlanReviewReportForm) reportForm;

		String forward = super.doCommon( reportForm, request,
				response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon() in IndividualContractReviewReportStep1Action.");
		}
		planReviewReportForm.setPageRegularlyNavigated(Boolean.TRUE
				.toString());
		return forward;
	}

	/**
	 * This method will redirect to the Step-2 (Customization) Page.
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
	@RequestMapping(value ="/contract/planReview/", params={"task=customize"} , method =  {RequestMethod.POST}) 
	public String doCustomize(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCustomize() in IndividualContractReviewReportStep1Action");
		}

		

		List<GenericException> warnings = new ArrayList<GenericException>();
		List<GenericException> errors = new ArrayList<GenericException>();
		request.setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE,
				BDConstants.PR_CONTRACT_LEVEL_PARAMETER);

		actionForm.setWarningExist(false);
		actionForm.setErrorExists(Boolean.FALSE);
		String brokerId = getBrokerId(request);
		//CR # 9 Changes
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request); 
		String userProfileId = null;
		if(userProfile.isInMimic()){
			BDUserProfile mimikedUserProfile = PlanReviewReportUtils
					.getMimckingUserProfile(request);
			userProfileId = String.valueOf(mimikedUserProfile.getBDPrincipal().getProfileId());
			
		}else{
			userProfileId = String.valueOf(userProfile.getBDPrincipal().getProfileId());
		}
		
		errors.addAll(validateUserAllowedRequestLimit(
				actionForm,userProfileId,userProfile.isInMimic()));
		
		if (!errors.isEmpty()) {
			actionForm.setErrorExists(Boolean.TRUE);	
			setErrorsInSession(request, errors);
			return forwards.get("input");
		}

		Boolean isDBContract = (Boolean) request.getSession(false)
				.getAttribute(BDConstants.IS_DB_CONTRACT);
		request.getSession(false).removeAttribute(BDConstants.IS_DB_CONTRACT);
		if (isDBContract == null || !isDBContract) {
			warnings.addAll(validateIndustrySelect(actionForm));
		}
		warnings.addAll(validatePlanReviewReportStatus(
				actionForm, brokerId));

		if (!warnings.isEmpty()) {
			actionForm.setWarningExist(true);	
			setReportWarningsInSession(request, warnings);
			return forwards.get("input");
		}

		populateActualContractReviewReports(
				actionForm.getContractReviewReportVOList(),
				actionForm.getDisplayContractReviewReports());
		
		actionForm.setPageRegularlyNavigated(Boolean.TRUE
				.toString());
		return forwards.get( getTask(request));

	}

	/**
	 * This method will redirect to the Step-2 (Customization) Page.
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
	@RequestMapping(value ="/contract/planReview/", params={"task=continue"} , method =  {RequestMethod.POST}) 
	public String doContinue(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCustomize() in IndividualContractReviewReportStep1Action");
		}

		

		populateActualContractReviewReports(
				actionForm.getContractReviewReportVOList(),
				actionForm.getDisplayContractReviewReports());
		 forward = doCommon( actionForm, request, response);
		return forwards.get(forward);
	}

	/**
	 * This method will redirect to the History Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contract/planReview/", params={"task=history"} , method =  {RequestMethod.POST}) 
	public String doHistory(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistory() in IndividualContractReviewReportStep1Action");
		}

		setRegularPageNavigation(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doHistory() in IndividualContractReviewReportStep1Action.");
		}
		
		return forwards.get(getTask(request));
	}

	private List<GenericException> validateIndustrySelect(
			PlanReviewReportForm planReviewReportForm) throws SystemException {

		final String SELECT_INDUSTRY_SEGMENT = "selectedIndustrySegment";
		
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

			validateIndustrySegment(errorMessages, SELECT_INDUSTRY_SEGMENT,
					planReviewReportForm
							.getDisplayContractReviewReports());

		return errorMessages;

	}

	private static void validateIndustrySegment(
			ArrayList<GenericException> errorMessages,
			String selectedIndustrySegment,
			List<PlanReviewReportUIHolder> selectedlist) throws SystemException {

		int count = -1;

		uIHolder: for (PlanReviewReportUIHolder slectedReport : selectedlist) {
			count++;
			if (StringUtils.isBlank(slectedReport.getSelectedIndustrySegment())) {
				errorMessages.add(new ValidationError(selectedIndustrySegment
						+ count, BDErrorCodes.INDUSTRY_SEGMENT_BLANK,
						Type.warning));
				continue uIHolder;
			}
		}
	}

	private void validatePlanReviewReport(
			PlanReviewReportUIHolder planReviewReportVO, Date recentMonthDate,
			Date contractEffectiveDate, String contractStatus) {
		
		boolean isPlanReviewReportDisabled = false;

		if (recentMonthDate == null || contractEffectiveDate == null) {
			
			isPlanReviewReportDisabled = true;
			
		} else {
			
			Calendar cutoffCal = Calendar.getInstance();
			cutoffCal.setTime(recentMonthDate);
			cutoffCal.add(Calendar.MONTH, BDConstants.LESS_THREE_MONTHS);
			
			Date cutOffDate = cutoffCal.getTime();
			
			if (StringUtils.equals(BDConstants.CF_STATUS, contractStatus)
					|| DateComparator.compare(contractEffectiveDate,
							cutOffDate, DATE_COMPARISON_FIELDS) > 0) {
				isPlanReviewReportDisabled = true;
			}
		}
		
		if (isPlanReviewReportDisabled) {
			
			planReviewReportVO.setPlanReviewReportDisabled(true);
			planReviewReportVO.setPlanReviewReportDisabledText(BDConstants.CONTRACT_LEVEL_DIESABLED_TEXT);
		
		} else {
			planReviewReportVO.setPlanReviewReportDisabled(false);
		}
	}

	/*
	 * Checking the plan review report is in already in progress or completed
	 * status with same broker id and monthend date.
	 */
	private Collection<GenericException> validatePlanReviewReportStatus(
			PlanReviewReportForm planReviewReportForm,
			String brokerId) throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		List<ActivityVo> actVoList = getActivityVOListForContractRequest(planReviewReportForm.getDisplayContractReviewReports());

		List<Integer> contractsWhichAlreadyReportExist = null;

		if (actVoList != null) {
			contractsWhichAlreadyReportExist = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.validatePlanReviewReport(actVoList, brokerId);
		}

		if (contractsWhichAlreadyReportExist != null) {
			int index = 0;
			for (PlanReviewReportUIHolder uIHolder : planReviewReportForm
					.getDisplayContractReviewReports()) {
				if (contractsWhichAlreadyReportExist.contains(uIHolder
						.getContractNumber())) {
					errorMessages.add(new ValidationError("contractNumber"
							+ index,
							BDErrorCodes.INPROGRESS_PLAN_REVIEW_REPORT,
							Type.warning));
				}
				++index;
			}
		}
		return errorMessages;
	}
	/*
	 * CR #9
	 * checking each contract row selected on the Step 1 page, if any contract rows meet the Allowed Request Limit Condition for PDF requests, and  
	 */
	private Collection<GenericException> validateUserAllowedRequestLimit(
			PlanReviewReportForm planReviewReportForm,
			String userProfileId, boolean isUserProfileMimked)
			throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		List<ActivityVo> actVoList = getActivityVOListForContractRequest(planReviewReportForm
				.getDisplayContractReviewReports());

		Map<Integer, String> contractsWhichAlreadyRequests = null;

		if (actVoList != null && actVoList.size() > 0) {
			contractsWhichAlreadyRequests = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.validateUserAllowedRequestLimit(actVoList, userProfileId,
							isUserProfileMimked, new Timestamp(System.currentTimeMillis()), false);
		}

		if (contractsWhichAlreadyRequests != null && !contractsWhichAlreadyRequests.isEmpty()) {
			for (PlanReviewReportUIHolder uIHolder : planReviewReportForm
					.getDisplayContractReviewReports()) {
				
				if (PlanReviewConstants.YES
						.equalsIgnoreCase(contractsWhichAlreadyRequests
								.get(uIHolder.getContractNumber()))) {
					errorMessages.add(new ValidationError(
							"selectedReportMonthEndDate" ,
							BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
							new Object[] {String.valueOf(uIHolder.getContractNumber()) }));

				}
			}
				
		}
		return errorMessages;
	}
	
	
	private List<ActivityVo> getActivityVOListForContractRequest(
			List<PlanReviewReportUIHolder> displayContractReviewReports) throws SystemException {
		List<ActivityVo> actVoList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uIHolder : displayContractReviewReports) {
				ActivityVo actVo = new ActivityVo();
				PublishDocumentPackageVo docPkgVo = new PublishDocumentPackageVo();
				actVo.setContractId(uIHolder.getContractNumber());
				try {
					docPkgVo.setPeriodEndDate(SHORT_MDY_FORMATTER.parse(uIHolder
							.getSelectedReportMonthEndDate()));
				} catch (ParseException e) {
					throw new SystemException(e,
							"exception while parsing month end date: "
									+ uIHolder.getSelectedReportMonthEndDate());
				}
				actVo.setDocumentPackageVo(docPkgVo);
				actVoList.add(actVo);
		}
		return actVoList;
	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
	/** avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
     * (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     */

	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ezk.web.BaseAction#isTokenValidatorEnabled(java
	 * .lang.String)
	 */
/*	@Override
	protected boolean isTokenValidatorEnabled(String action) {

		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "Continue")
						|| StringUtils.equalsIgnoreCase(action, "Customize"))?true: false;
	
	}*/
}
