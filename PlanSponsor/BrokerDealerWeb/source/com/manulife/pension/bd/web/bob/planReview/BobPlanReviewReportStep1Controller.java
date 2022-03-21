package com.manulife.pension.bd.web.bob.planReview;

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
import com.manulife.pension.bd.web.bob.planReview.util.PersistStep1SortDetails;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ContractSearchUtility;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.contract.valueobject.ContractVO;
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
 * This action class builds the Step 1 Plan Review Request page.
 * 
 * @author Manjunath
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"planReviewReportForm"})

public class BobPlanReviewReportStep1Controller extends BasePlanReviewReportController {

	@ModelAttribute("planReviewReportForm") 
	public PlanReviewReportForm populateForm() 
	{
		return new PlanReviewReportForm();
		}
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/planReview/step1BobPlanReviewReports.jsp");
		forwards.put("default","/planReview/step1BobPlanReviewReports.jsp");
		forwards.put("contractReviewReportFilterData","/planReview/step1BobPlanReviewReports.jsp");
		forwards.put("sort","/planReview/step1BobPlanReviewReports.jsp");
		forwards.put("customize","redirect:/do/bob/planReview/Customize/");
		forwards.put("continue","redirect:/do/bob/planReview/Customize/"); 
		forwards.put("history","redirect:/do/bob/planReview/History/");
		forwards.put("homePage","redirect:/do/home/");
	}

	private int[] DATE_COMPARISON_FIELDS = new int[] { Calendar.YEAR,
			Calendar.MONTH, Calendar.DATE };

	protected static final Logger logger = Logger.getLogger(BobPlanReviewReportStep1Controller.class);

	/**
	 * Constructor class.
	 */
	public BobPlanReviewReportStep1Controller() {
		super(BobPlanReviewReportStep1Controller.class);
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
	@RequestMapping(value ="/planReview/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doDefault() in BobPlanReviewReportStep1Action");
		}

		
		actionForm.setWarningExist(false);
		
		Boolean isRequestFromHistoryOrPrint = (Boolean) request.getSession(false).getAttribute(
							BDConstants.REQUEST_FROM_HISTORY_OR_PRINT);

		// if not returns from Step 2 page to this page.
		if (!actionForm.isRequestBackFromStep2()) {

			if (request.getSession().getAttribute(
					BDConstants.C0NTRACT_REVIEW_REPORT_BEAN) != null) {
				/*
				 * Below method populate contractReviewReportList from session,
				 * if the user navigates from BOB page.
				 */
				actionForm.clear();
				actionForm.reset(request);
				actionForm.setAllContractSelected(false);
				actionForm.setAllRatioReportSelected(false);
				populateDefultContractReviewReportVOList(
						actionForm, request);
			} else if (isRequestFromHistoryOrPrint != null && Boolean.TRUE
					.equals(isRequestFromHistoryOrPrint)) {
				/*
				 * if the user navigates from History or Results or Print page.
				 */
				actionForm.getContractReviewReportVOList().clear();
				
				for (PlanReviewReportUIHolder holder : actionForm.getActualPlanReviewReports()) {
					
					PlanReviewReportUIHolder planReviewReport = holder
							.cloneObject();
					planReviewReport.setIndustrySegementOptions(holder
							.getIndustrySegementOptions());
					planReviewReport.setReportMonthEndDates(holder
							.getReportMonthEndDates());
					
					actionForm.getContractReviewReportVOList().add(planReviewReport);
					
				}
				
			} else {
				/*
				 * When the User Visits the PlanReview Page for the first time
				 * from the BOB header, records will be fetched by calling the
				 * Contract Service
				 */
				actionForm.clear();
				actionForm.reset( request);
				actionForm.setAllContractSelected(false);
				actionForm.setAllRatioReportSelected(false);
				retrivePlanReviewReport(actionForm, request);
			}
		}
		
		if (isRequestFromHistoryOrPrint == null || (isRequestFromHistoryOrPrint != null && !Boolean.TRUE
				.equals(isRequestFromHistoryOrPrint))) {
			// set the default Plansponsor Magazine Industry Code For Contract
			// reports
			setDefaultPlansponsorMagazineIndustryCodeForContracts(actionForm
					.getContractReviewReportVOList());
	
			// if not returns from Step 2 page to this page.
			if (!actionForm.isRequestBackFromStep2()) {
				actionForm.getActualPlanReviewReports().clear();
				
				for (PlanReviewReportUIHolder holder : actionForm.getContractReviewReportVOList()) {
					
					PlanReviewReportUIHolder planReviewReport = holder
							.cloneObject();
					planReviewReport.setIndustrySegementOptions(holder
							.getIndustrySegementOptions());
					planReviewReport.setReportMonthEndDates(holder
							.getReportMonthEndDates());
					planReviewReport.setContractSelected(false);
					
					actionForm.getActualPlanReviewReports().add(planReviewReport);
					
				}
			}
		}
		
		populateDisplayContractReviewReports(
				actionForm.getContractReviewReportVOList(),
				actionForm.getDisplayContractReviewReports());

		 forward = doCommon( actionForm, request, response);

		// set to default value.
		actionForm.setRequestBackFromStep2(false);

		request.getSession(false).setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE, BDConstants.PR_BOB_LEVEL_PARAMETER);
		request.getSession(false).removeAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault() in BobPlanReviewReportStep1Action.");
		}

		return forwards.get(forward);

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
	protected String doCommon( BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
			throws  SystemException {
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon() in BobPlanReviewReportStep1Action");
		}

		PlanReviewReportForm planReviewReportForm = (PlanReviewReportForm) reportForm;

		String forward = super.doCommon( reportForm, request,
				response);

		planReviewReportForm
				.setStep1SortDetails(getStep1SortDetails(reportForm));

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon() in BobPlanReviewReportStep1Action.");
		}

		return forward;
	}

	/**
	 * Get the Step1SortDetails from the report form
	 * 
	 * @param reportForm
	 * @return
	 */
	private PersistStep1SortDetails getStep1SortDetails(
			BaseReportForm reportForm) {

		final String sortField = reportForm.getSortField();
		final String sortDirection = reportForm.getSortDirection();

		return new PersistStep1SortDetails(sortField, sortDirection);
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
	@RequestMapping(value ="/planReview/" ,params={"task=history"}   , method =  {RequestMethod.POST}) 
	public String doHistory (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
			logger.debug("entry -> doHistory() in BobPlanReviewReportStep1Action");
		}

		setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doHistory() in BobPlanReviewReportStep1Action.");
		}

		return forwards.get(getTask(request));
	}

	/**
	 * This method will used to handle the Sort data for BOB Pages.
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
	@RequestMapping(value ="/planReview/", params={"task=sort"} , method =  {RequestMethod.POST}) 
	public String doSort (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
			logger.debug("entry -> doSort() in BobPlanReviewReportStep1Action");
		}

		
		form.setWarningExist(false);

		 forward = super.doSort(form, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSort() in BobPlanReviewReportStep1Action.");
		}

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.bob.contractReviewReports.
	 * BaseContractReviewRequestAction
	 * #populateReportForm(org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		PlanReviewReportForm planReviewReportForm = (PlanReviewReportForm) reportForm;
		PersistStep1SortDetails step1SortDetails = planReviewReportForm
				.getStep1SortDetails();

		String task = getTask(request);
		/*
		 * Set default sort if we're in default task.
		 */
		if (planReviewReportForm.isRequestBackFromStep2()) {

			planReviewReportForm.setRequestBackFromStep2(false);
			reportForm.setSortDirection(step1SortDetails.getSortDirection());
			reportForm.setSortField(step1SortDetails.getSortField());

		} else if ((task.equals(DEFAULT_TASK)
				|| reportForm.getSortDirection() == null || reportForm
				.getSortDirection().length() == 0)) {

			reportForm.setSortDirection(getDefaultSortDirection());
			if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
					|| reportForm.getSortField().length() == 0) {
				reportForm.setSortField(getDefaultSort());
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}
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
	@RequestMapping(value ="/planReview/", params={"task=customize"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCustomize (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
			logger.debug("entry -> doCustomize() in BobPlanReviewReportStep1Action");
		}
		request.setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE,
				BDConstants.PR_BOB_LEVEL_PARAMETER);
		
		List<GenericException> warnings = new ArrayList<GenericException>();
		List<GenericException> errors = new ArrayList<GenericException>();
		form.setWarningExist(false);
		form.setErrorExists(Boolean.FALSE);

		// CR9 Changes
		// When the user clicks on the Next button the
		// system will determine for each contract row selected on the Step 1
		// page,
		// if any contract rows meet the Allowed Request Limit Condition for PDF
		// requests,
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		String userProfileId = null;
		if (userProfile.isInMimic()) {
			BDUserProfile mimikedUserProfile = PlanReviewReportUtils
					.getMimckingUserProfile(request);
			userProfileId = String.valueOf(mimikedUserProfile.getBDPrincipal()
					.getProfileId());

		} else {
			userProfileId = String.valueOf(userProfile.getBDPrincipal()
					.getProfileId());
		}

		errors.addAll(validateUserAllowedRequestLimit(
				form,userProfileId,userProfile.isInMimic()));
		if(!errors.isEmpty()){
			form.setErrorExists(Boolean.TRUE);
			setErrorsInSession(request, errors);
			return forwards.get("input");
		}

		warnings.addAll(validatePlanReviewReportStatus(
				form, getBrokerId(request)));

		warnings.addAll(validateSelectedIndustrySegmentValue(form));

		if (!warnings.isEmpty()) {
			form.setWarningExist(true);
			setReportWarningsInSession(request, warnings);
			return forwards.get("input");
		}

		populateActualContractReviewReports(
				form.getContractReviewReportVOList(),
				form.getDisplayContractReviewReports());
		setRegularPageNavigation(request);

		return forwards.get(getTask(request));
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
	@RequestMapping(value ="/planReview/", params={"task=continue"}, method =  {RequestMethod.POST}) 
	public String doContinue (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
			logger.debug("entry -> doContinue() in BobPlanReviewReporttStep1Action");
		}

		populateActualContractReviewReports(
				form.getContractReviewReportVOList(),
				form.getDisplayContractReviewReports());

		setRegularPageNavigation(request);

		return forwards.get( getTask(request));
	}
	@RequestMapping(value ="/planReview/",params={"task=filter"} , method =  {RequestMethod.POST}) 
	public String doFilter (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
		 forward=super.doFilter( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/planReview/" ,params={"task=page"} , method =  {RequestMethod.POST}) 
	public String doPage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
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
		 forward=super.doPage( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	/**
	 * 
	 * @param errorMessages
	 * @param selectedIndustrySegment
	 * @param selectedlist
	 * @throws SystemException
	 */
	private ArrayList<GenericException> validateSelectedIndustrySegmentValue(
			PlanReviewReportForm planReviewReportForm)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateSelectedIndustrySegmentValue");
		}

		final String SELECTINDUSTRYSEGMENT = "selectedIndustrySegment";
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		int count = -1;

		nextFee: for (PlanReviewReportUIHolder slectedReport : planReviewReportForm
				.getDisplayContractReviewReports()) {
			count++;
			if (slectedReport.isContractSelected()) {

				if(slectedReport.isDefinedBenefitContract()) {
					continue nextFee;
				}
				
				if (StringUtils.isBlank(slectedReport
						.getSelectedIndustrySegment())) {
					errorMessages.add(new ValidationError(SELECTINDUSTRYSEGMENT
							+ count, BDErrorCodes.INDUSTRY_SEGMENT_BLANK,
							Type.warning));
					continue nextFee;
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> validateSelectedIndustrySegmentValue");
		}

		return errorMessages;
	}

	/**
	 * 
	 * @param planReviewReportForm
	 * @param request
	 * @throws SystemException
	 */
	private void populateDefultContractReviewReportVOList(
			PlanReviewReportForm planReviewReportForm,
			HttpServletRequest request) throws SystemException {

		List<PlanReviewReportUIHolder> contractReviewReportVOList = new ArrayList<PlanReviewReportUIHolder>();
		contractReviewReportVOList = (ArrayList<PlanReviewReportUIHolder>) request
				.getSession().getAttribute(
						BDConstants.C0NTRACT_REVIEW_REPORT_BEAN);
		request.getSession().removeAttribute(
				BDConstants.C0NTRACT_REVIEW_REPORT_BEAN);
		List<Integer> contractList = new ArrayList<Integer>();
		for (PlanReviewReportUIHolder vo : contractReviewReportVOList) {
			contractList.add(vo.getContractNumber());
		}

		populatePlanReviewStep1Attributes(planReviewReportForm,
				contractReviewReportVOList, request);

		planReviewReportForm
				.setContractReviewReportVOList(contractReviewReportVOList);

	}

	/**
	 * 
	 * @param planReviewReportForm
	 * @param request
	 * @throws SystemException
	 */
	private void retrivePlanReviewReport(
			PlanReviewReportForm planReviewReportForm,
			HttpServletRequest request) throws SystemException {
		List<PlanReviewReportUIHolder> contractReviewReportVOList = new ArrayList<PlanReviewReportUIHolder>();
		List<Integer> contractList = new ArrayList<Integer>();
		List<PeriodEndingReportDateVO> monthEndDates = new ArrayList<PeriodEndingReportDateVO>();
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		List<String> contractStatusList = new ArrayList<String>();
		contractStatusList.add("AC");

		boolean includeIAStatus = false;
		List<ContractVO> contractVOList = ContractSearchUtility
				.getBOBContractDetails(request, userProfile,
						contractStatusList, includeIAStatus);
		if (contractVOList.size() > 0) {
			for (ContractVO vo : contractVOList) {
				contractList.add(vo.getContractNumber());
			}

			// TODO can get List<Dates> from DAO so that we should have avoided
			// the date formatter
			monthEndDates = PlanReviewReportUtils
					.getActivePlanReviewPeriodEndDates(false);

			for (ContractVO contract : contractVOList) {
				populatePlanReviewReportVO(contract,
						planReviewReportForm, contractReviewReportVOList,
						monthEndDates, request);
			}
		}

		planReviewReportForm
				.setContractReviewReportVOList(contractReviewReportVOList);
	}

	/**
	 * 
	 * @param contractVO
	 * @param contractReviewReportVOList
	 * @param dateMap
	 * @throws SystemException
	 */
	private void populatePlanReviewReportVO(ContractVO contractVO,
			PlanReviewReportForm planReviewReportForm,
			List<PlanReviewReportUIHolder> contractReviewReportVOList,
			List<PeriodEndingReportDateVO> monthEndDates,
			HttpServletRequest request) throws SystemException {

		PlanReviewReportUIHolder contractReviewReportVO = new PlanReviewReportUIHolder();
		contractReviewReportVO
				.setContractNumber(contractVO.getContractNumber());
		contractReviewReportVO.setContractName(contractVO.getContractName());
		contractReviewReportVO.setContractStatusCode(contractVO
				.getContractStatusCode());
		contractReviewReportVO.setContractEffectiveDate(contractVO
				.getContractEffectiveDate());
		contractReviewReportVO.setContractStatusEffectiveDate(contractVO
				.getContractStatusEffectiveDate());

		contractReviewReportVO.setProductId(contractVO.getProductId().trim());
		
		contractReviewReportVO
				.setDefinedBenefitContract(isDefinedBenefitContract(contractVO
						.getProductId().trim()));
		
		contractReviewReportVO
				.setIndustrySegementOptions(isDefinedBenefitContract(contractVO
						.getProductId().trim()) ? null : PlanReviewReportUtils
						.getPlanReviewIndustrySegmentList(request));

		Date recentMonthEndDate = null;
		if (monthEndDates != null && !monthEndDates.isEmpty()) {
			contractReviewReportVO.setReportMonthEndDates(monthEndDates);

			populateValidMonthEndDates(monthEndDates, contractReviewReportVO);

			List<PeriodEndingReportDateVO> validMonthEndDates = contractReviewReportVO.getReportMonthEndDates();
			
			if(validMonthEndDates != null && !validMonthEndDates.isEmpty()) {
				try {
					recentMonthEndDate = SHORT_MDY_FORMATTER
							.parse(PlanReviewReportUtils
									.getRecentPeriodEndingDate(validMonthEndDates));
				} catch (ParseException e) {
					throw new SystemException(e,
							"Exception occured while Parsing the recent date."
									+ recentMonthEndDate);
				}
			}
		}

		validatePlanReviewReport(contractReviewReportVO, recentMonthEndDate,
				contractVO.getContractEffectiveDate());

		contractReviewReportVOList.add(contractReviewReportVO);

		if (!(contractReviewReportVO.getPlanReviewReportDisabled())) {
			planReviewReportForm.setDisabledContract(true);
		}

	}

	/**
	 * 
	 * @param contractReviewReportVOList
	 * @param contractList
	 * @return
	 * @throws SystemException
	 */
	private void populatePlanReviewStep1Attributes(
			PlanReviewReportForm planReviewReportForm,
			List<PlanReviewReportUIHolder> contractReviewReportVOList,
			HttpServletRequest request) throws SystemException {

		// TODO can get List<Dates> from DAO so that we should have avoided the
		// date formatter
		List<PeriodEndingReportDateVO> monthEndDates = PlanReviewReportUtils
				.getActivePlanReviewPeriodEndDates(false);

		for (PlanReviewReportUIHolder contractReviewReportVO : contractReviewReportVOList) {
			Date recentMonthEndDate = null;

			if (monthEndDates != null && !monthEndDates.isEmpty()) {
				populateValidMonthEndDates(monthEndDates,
						contractReviewReportVO);

				List<PeriodEndingReportDateVO> validMonthEndDates = contractReviewReportVO.getReportMonthEndDates();
				
				if(validMonthEndDates != null && !validMonthEndDates.isEmpty()) {

					try {
	
						recentMonthEndDate = SHORT_MDY_FORMATTER.parse(PlanReviewReportUtils
								.getRecentPeriodEndingDate(validMonthEndDates));
					} catch (ParseException c) {
						throw new SystemException(c,
								"Exception occured while Parsing the recent date."
										+ "for contract "
										+ contractReviewReportVO
												.getContractNumber()
										+ " recent month end date is"
										+ recentMonthEndDate);
					}
				}
			}

			contractReviewReportVO
					.setIndustrySegementOptions(!isDefinedBenefitContract(contractReviewReportVO
							.getProductId().trim()) ? PlanReviewReportUtils
							.getPlanReviewIndustrySegmentList(request) : null);

			validatePlanReviewReport(contractReviewReportVO,
					recentMonthEndDate,
					contractReviewReportVO.getContractEffectiveDate());
			
			contractReviewReportVO.setContractSelected(false);

			if (!(contractReviewReportVO.getPlanReviewReportDisabled())) {
				planReviewReportForm.setDisabledContract(true);
			}
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
			PlanReviewReportUIHolder contractReviewReportVO)
			throws SystemException {
		// SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		List<PeriodEndingReportDateVO> validMonthEndDates = new ArrayList<PeriodEndingReportDateVO>();
		try {

			if (StringUtils.equals(BDConstants.CONTRACT_STATUS_DI,
					contractReviewReportVO.getContractStatusCode())) {

				for (PeriodEndingReportDateVO monthend : monthEndDates) {

					Calendar cutoffCal = Calendar.getInstance();
					cutoffCal.setTime(contractReviewReportVO
							.getContractEffectiveDate());
					cutoffCal.add(Calendar.MONTH, BDConstants.THREE_MONTHS);
					Date cutOffDate = cutoffCal.getTime();

					if (DateComparator.compare(SHORT_MDY_FORMATTER
							.parse(monthend.getPeriodEndingReportDate()),
							contractReviewReportVO
									.getContractStatusEffectiveDate(),
							DATE_COMPARISON_FIELDS) < 0
							&& DateComparator.compare(
									SHORT_MDY_FORMATTER.parse(monthend
											.getPeriodEndingReportDate()),
									cutOffDate, DATE_COMPARISON_FIELDS) > 0) {
						validMonthEndDates.add(monthend);
					}
				}
			} else {

				for (PeriodEndingReportDateVO monthend : monthEndDates) {

					Calendar cutoffCal = Calendar.getInstance();
					cutoffCal.setTime(contractReviewReportVO
							.getContractEffectiveDate());
					cutoffCal.add(Calendar.MONTH, BDConstants.THREE_MONTHS);
					Date cutOffDate = cutoffCal.getTime();
					if (DateComparator.compare(SHORT_MDY_FORMATTER
							.parse(monthend.getPeriodEndingReportDate()),
							cutOffDate, DATE_COMPARISON_FIELDS) > 0) {
						validMonthEndDates.add(monthend);
					}
				}
			}
		} catch (ParseException exception) {
			throw new SystemException(exception,
					"Exception occured while Parsing the recent date."
							+ "for contract "
							+ contractReviewReportVO.getContractNumber());
		}

		contractReviewReportVO.setReportMonthEndDates(validMonthEndDates);

	}

	/**
	 * 
	 * @param planReviewReportVO
	 * @param recentMonthDate
	 * @param contractEffectiveDate
	 */
	private void validatePlanReviewReport(
			PlanReviewReportUIHolder planReviewReportVO, Date recentMonthDate,
			Date contractEffectiveDate) {

		boolean isPlanReviewReportDisabled = false;

		if (recentMonthDate == null || contractEffectiveDate == null) {

			isPlanReviewReportDisabled = true;

		} else {

			Calendar cutoffCal = Calendar.getInstance();
			cutoffCal.setTime(recentMonthDate);
			cutoffCal.add(Calendar.MONTH, BDConstants.LESS_THREE_MONTHS);
			Date cutOffDate = cutoffCal.getTime();

			if (StringUtils.equals(BDConstants.CF_STATUS,
					planReviewReportVO.getContractStatusCode())
					|| DateComparator.compare(contractEffectiveDate,
							cutOffDate, DATE_COMPARISON_FIELDS) > 0) {

				isPlanReviewReportDisabled = true;
			}
		}

		if (isPlanReviewReportDisabled) {

			planReviewReportVO.setPlanReviewReportDisabled(true);
			planReviewReportVO
					.setPlanReviewReportDisabledText(BDConstants.CONTRACT_EFF_DATE_AND_STATUS_DIESABLED_TEXT);

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
		List<ActivityVo> actVoList = getActivityVOListForCRequest(planReviewReportForm
				.getDisplayContractReviewReports());

		List<Integer> contractsWhichAlreadyReportExist = null;

		if (actVoList != null && actVoList.size() > 0) {
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
	 * CR #9 checking each contract row selected on the Step 1 page, if any
	 * contract rows meet the Allowed Request Limit Condition for PDF requests,
	 * and
	 */
	private Collection<GenericException> validateUserAllowedRequestLimit(
			PlanReviewReportForm planReviewReportForm,
			String userProfileId, boolean isUserProfileMimked)
			throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		List<ActivityVo> actVoList = getActivityVOListForCRequest(planReviewReportForm
				.getDisplayContractReviewReports());

		Map<Integer, String> contractsWhichAlreadyRequests = null;

		if (actVoList != null && actVoList.size() > 0) {
			contractsWhichAlreadyRequests = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.validateUserAllowedRequestLimit(actVoList, userProfileId,
							isUserProfileMimked, new Timestamp(System.currentTimeMillis()), false);
		}

		if (contractsWhichAlreadyRequests != null && !contractsWhichAlreadyRequests.isEmpty()) {
			int index = 0;
			List<Integer> indexList = new ArrayList<Integer>();
			StringBuffer contractString = new StringBuffer();
			for (PlanReviewReportUIHolder uIHolder : planReviewReportForm
					.getDisplayContractReviewReports()) {

				if (PlanReviewConstants.YES
						.equalsIgnoreCase(contractsWhichAlreadyRequests
								.get(uIHolder.getContractNumber()))) {
					contractString.append(uIHolder.getContractNumber()
							+ ",&nbsp;");
					indexList.add(index);

				}
				++index;
			}
			
			
			if(StringUtils.isNotBlank(contractString.toString())) {
			
				errorMessages.add(new ValidationError(
						"selectedReportMonthEndDate",
						BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
						new Object[] { contractString.substring(0, contractString.lastIndexOf(",")) }));
			}
			
		}
		
		return errorMessages;
	}

	private List<ActivityVo> getActivityVOListForCRequest(
			List<PlanReviewReportUIHolder> displayContractReviewReports)
			throws SystemException {
		// SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<ActivityVo> actVoList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uIHolder : displayContractReviewReports) {
			if (uIHolder.isContractSelected()) {
				ActivityVo actVo = new ActivityVo();
				PublishDocumentPackageVo docPkgVo = new PublishDocumentPackageVo();
				actVo.setContractId(uIHolder.getContractNumber());
				try {
					docPkgVo.setPeriodEndDate(SHORT_MDY_FORMATTER
							.parse(uIHolder.getSelectedReportMonthEndDate()));
				} catch (ParseException e) {
					throw new SystemException(e,
							"exception while parsing month end date: "
									+ uIHolder.getSelectedReportMonthEndDate());
				}
				actVo.setDocumentPackageVo(docPkgVo);
				actVoList.add(actVo);
			}
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
	
	/**
	 * avoids token generation as this class acts as intermediate for many
	 * transactions(non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired
	 * (java.lang.String)
	 */
	/*@Override
	protected boolean isTokenRequired(String action) {
		return true;

	}*/
	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ezk.web.BaseController#isTokenValidatorEnabled(java
	 * .lang.String)
	 */
	@Override
	protected boolean isTokenValidatorEnabled(String action) {

		// avoids methods from validation which ever is not required
		if (StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "Continue")
						|| StringUtils.equalsIgnoreCase(action, "Customize"))) {
			return true;
		} else {
			return false;
		}
	
	}
	
	
	
}
