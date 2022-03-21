package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContractSearchReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping( value = "/tpafee")
@SessionAttributes({"tpaFeeScheduleContractSearchForm"})

public class TPAFeeScheduleContractSearchController extends ReportController {

	@ModelAttribute("tpaFeeScheduleContractSearchForm") 
	public TPAFeeScheduleContractSearchForm populateForm()
	{
		return new TPAFeeScheduleContractSearchForm();
		}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/tpafee/contractSearch.jsp");
		forwards.put("default","/tpafee/contractSearch.jsp");
		forwards.put("sort","/tpafee/contractSearch.jsp");
		forwards.put("error","/tpafee/contractSearch.jsp");
		forwards.put("page","/tpafee/contractSearch.jsp");
		forwards.put("search","/tpafee/contractSearch.jsp");
		forwards.put("View","redirect:/do/tpafee/tpafeeSchedule/");
		forwards.put("filter","redirect:/do/tpafee/tpafeeSchedule/");
		forwards.put("SelectContract","/tpafee/TPAContractSelect.jsp");
		forwards.put("viewTpaCustomizedContractFee","redirect:/do/viewTpaCustomizedContractFee/");}

	
	public TPAFeeScheduleContractSearchController() {
		super(TPAFeeScheduleContractSearchController.class);

	}

	private static final String DEFAULT_SORT_FIELD = "contractName";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int CONTRACT_PAGE_SIZE = 35;
	private static Environment env = Environment.getInstance();

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected int getPageSize(HttpServletRequest request) {
		return CONTRACT_PAGE_SIZE;
	}

	@RequestMapping(value ="/contractSearch/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		doValidate(form,request);
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	String forward = null;
		if(request.getSession().getAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID) != null) {
			String selectedTpaFirmId = String.valueOf(request.getSession().getAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID));
			form.setTpaId(Integer.parseInt(selectedTpaFirmId));
		} else {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		forward = doCommon( form, request, response);
	
		return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
	}

	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)  throws SystemException {
		
		TPAFeeScheduleContractSearchForm tpaFeeScheduleContractSearchForm = (TPAFeeScheduleContractSearchForm) form;
		criteria.setPageSize(CONTRACT_PAGE_SIZE);
		criteria.addFilter(
				TPAFeeScheduleContractSearchReportData.FILTER_TPA_FIRM_ID,
				String.valueOf(tpaFeeScheduleContractSearchForm.getTpaId()));
		
		String sitemode = env.getSiteLocation();
		String companyCode = sitemode.equals(Constants.SITEMODE_USA) ? RequestConstants.COMPANY_CODE_USA
						: RequestConstants.COMPANY_CODE_NY;
		
		
		criteria
				.addFilter(
						TPAFeeScheduleContractSearchReportData.FILTER_COMPANY_CODE,
						companyCode);

		if ((StringUtils.equals(FILTER_TASK, getTask(request))
				|| StringUtils.equals(PAGE_TASK, getTask(request)) || StringUtils.equals(SORT_TASK, getTask(request)))
				&& (StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm.getContractNumber())
				|| StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm.getContractName()))) {
			
			if (StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm
					.getContractNumber())) {
				criteria.addFilter(
						TPAFeeScheduleContractSearchReportData.FILTER_CONTRACT_NUMBER,
						tpaFeeScheduleContractSearchForm.getContractNumber());
			} 
			
			if (StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm
					.getContractName())) {
				criteria.addFilter(
						TPAFeeScheduleContractSearchReportData.FILTER_CONTRACT_NAME,
						tpaFeeScheduleContractSearchForm.getContractName());
			}
			
			// Get the available TPA firms for the logged in user. This will be used during the validation.
			Map<Integer, String> tpaFirmMap = TPAServiceDelegate.getInstance()
			.retrieveTpaFirmsByTPAUserProfileId(
							getUserProfile(request).getPrincipal().getProfileId(), companyCode);
			StringBuffer buffer = new StringBuffer();
			
			for(Integer tpaFirmId : tpaFirmMap.keySet()) {
				buffer.append(tpaFirmId);
				buffer.append(",");
			}
			
			String masterTpaFirmIds = StringUtils.chomp(buffer.toString().trim(), ",");
			
			criteria.addFilter(TPAFeeScheduleContractSearchReportData.FILTER_MASTER_TPA_FIRM_IDS, masterTpaFirmIds);
			
		}
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TPAFeeScheduleContractSearchReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return TPAFeeScheduleContractSearchReportData.REPORT_NAME;
	}
	 
	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		String forward = null;
		TPAFeeScheduleContractSearchForm form = (TPAFeeScheduleContractSearchForm)reportForm;
		if(isFilterTask(request)) {
			// Reset the sort criteria
			form.setSortField(getDefaultSort());
			form.setSortDirection(getDefaultSortDirection());
		}
		
		forward = super.doCommon( form, request, response);
		
		TPAFeeScheduleContractSearchReportData reportData = (TPAFeeScheduleContractSearchReportData) request.getAttribute(Constants.REPORT_BEAN);
			
		form.setTotalCount((reportData.getDetails().size()));
			
		if (isDefaultTask(request)) {
			// If the total no of contracts is less than 50 then suppress the search box
			if (reportData.isContractCountBelowMaxLimit()) {
				form.setEnableContractSearch(false);
				
				if (reportData.getDetails().size() == 1) {
					TPAFeeScheduleContract feeScheduleContract = (TPAFeeScheduleContract) reportData.getDetails().toArray()[0];
					return redirectToViewPage(request,feeScheduleContract.getContractId());
				} 
			} else {
				form.setEnableContractSearch(true);
				form.resetSearchFilters();
				reportData.setDetails(null);
			}
			
			return forward;
		}
			
		if(isFilterTask(request)) {
			form.setEnableContractSearch(true);

			List<GenericException> errors = validateResult(request, form, reportData);
			if (!errors.isEmpty()) {
				setErrorsInRequest(request, errors);
				reportData.setDetails(null);
				return forwards.get("input");
			} 
		}
		
		// If there is only one contract available then navigate the user to view custom fee schedule page
		if (reportData.getDetails().size() == 1) {
			TPAFeeScheduleContract feeScheduleContract = (TPAFeeScheduleContract) reportData.getDetails().toArray()[0];
			return redirectToViewPage(request,  feeScheduleContract.getContractId());
		} 

		return  forwards.get("input");
	}

	/**
	 * Validate the search results
	 * 
	 * @param request
	 * @param reportForm
	 * @param data
	 * @return
	 */
	private ArrayList<GenericException> validateResult (HttpServletRequest request, ActionForm reportForm, ReportData data) {
		TPAFeeScheduleContractSearchReportData reportData = (TPAFeeScheduleContractSearchReportData) data;
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		TPAFeeScheduleContractSearchForm tpaFeeScheduleContractSearchForm = (TPAFeeScheduleContractSearchForm) reportForm;
		
		if (reportData.getDetails().isEmpty()) {
			if (StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm
					.getContractName())) {
				if (reportData.isInvalidContractSearch()) {
					errorMessages
					.add(new ValidationError(
							Constants.EMPTY_STRING,
							ErrorCodes.INVALID_CONTRACT_NAME));
				} else if (reportData.isContractExistsOnTheOtherFirm()) {
					String[] params = {String.valueOf(reportData
							.getOtherFirmIdWhereContractExist())};
					errorMessages
							.add(new ValidationError(
									Constants.EMPTY_STRING,
									ErrorCodes.CONTRACT_UNDER_DIFFERENT_TPA_FIRM_FILTER_CONTRACT_NAME,
									params));
				} else {

					errorMessages.add(new ValidationError(
							Constants.EMPTY_STRING,
							ErrorCodes.NO_MATCH_FOR_CONTRACT_INFORMATION));
				}
			}

			if (StringUtils.isNotBlank(tpaFeeScheduleContractSearchForm
					.getContractNumber())) {
				if (reportData.isInvalidContractSearch()) {
					errorMessages
					.add(new ValidationError(
							Constants.EMPTY_STRING,
							ErrorCodes.INVALID_CONTRACT_NUMBER));
				} else if (reportData.isContractExistsOnTheOtherFirm()) {
					String[] params = {String.valueOf(reportData
							.getOtherFirmIdWhereContractExist())};
					errorMessages
							.add(new ValidationError(
									Constants.EMPTY_STRING,
									ErrorCodes.CONTRACT_UNDER_DIFFERENT_TPA_FIRM_FILTER_CONTRACT_NUMBER,
									params));
				} else {
					errorMessages.add(new ValidationError(
							Constants.EMPTY_STRING,
							ErrorCodes.NO_MATCH_FOR_CONTRACT_INFORMATION));
				}
			}
		}
		
		return errorMessages;
	}
	
	/**
	 * Validate the input fields
	 * 
	 * @param request
	 * @param reportForm
	 * @return
	 */
	private boolean validateInputFields(HttpServletRequest request, ActionForm reportForm)  {
		
		TPAFeeScheduleContractSearchForm tpaFeeScheduleContractSearchForm = (TPAFeeScheduleContractSearchForm) reportForm;
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		boolean isValid = true;
		
		String contractName = StringUtils.trimToNull(tpaFeeScheduleContractSearchForm.getContractName());
		String contractId = tpaFeeScheduleContractSearchForm.getContractNumber();
				
		// Validate contract name length >0 & <3
		if (StringUtils.isNotBlank(contractName)) {

			if (!isValidContractName(tpaFeeScheduleContractSearchForm
					.getContractName())) {
				ValidationError exception = new ValidationError(
						Constants.TPA_CONTRACT_NAME_SORT_FIELD,
						ErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
				errorMessages.add(exception);
			}
		}
		
		// Validate the contract number field
		if (StringUtils.isNotBlank(contractId)) {

			if (!NumberUtils.isNumber(tpaFeeScheduleContractSearchForm
					.getContractNumber().trim())) {
				ValidationError exception = new ValidationError(
						Constants.TPA_CONTRACT_NUMBER_SORT_FIELD,
						ErrorCodes.NON_NUMERIC_CONTRACT_NUMBER);
				errorMessages.add(exception);
			}
		}

		
		if (!errorMessages.isEmpty()) {
			isValid = false;
			setErrorsInRequest(request, errorMessages);
		}

		return isValid;
	}
	
	/**
	 * Validate the contract name
	 * 
	 * @param contractName
	 * @return
	 */
	private boolean isValidContractName(String contractName) {

		if (StringUtils.trimToNull(contractName) != null
				&& StringUtils.trimToNull(contractName).length() < 3) {
			return false;
		} else {
			return true;
		}
	}

	protected static UserProfile getUserProfile(HttpServletRequest request) {
		return PsController.getUserProfile(request);
	}

	/**
	 * Validate method
	 */
	@SuppressWarnings("rawtypes")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request)  {
		/*
		 * This code has been changed and added to Validate form and
		 * request against penetration attack, prior to other validations
		 */
		TPAFeeScheduleContractSearchForm theForm=(TPAFeeScheduleContractSearchForm)form;
		
		
		List<GenericException> errors = new ArrayList<GenericException>();
		boolean isValid = true;
		
		if (isFilterTask(request)) {
			isValid = validateInputFields(request, form);
		}

		if (!isValid) {
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}
		return errors;
	}
	
	private boolean isFilterTask(HttpServletRequest request) {
		return StringUtils.equals(FILTER_TASK, getTask(request));
	}
	
	private boolean isDefaultTask (HttpServletRequest request) {
		return StringUtils.equals(DEFAULT_TASK, getTask(request));
	}
	
	/**
	 * Method to redirect To View Page
	 * 
	 * @param request
	 * @param mapping
	 * @param tpaFirmId
	 * @return newForward
	 * @throws SystemException
	 */
	private String redirectToViewPage(HttpServletRequest request,
			 Integer contractId) {
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT, contractId.toString());
		return forwards.get( Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
		
	@Override
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		if (form.getSortField() != null) {
			criteria.insertSort(form.getSortField(), form.getSortDirection());
			if (!form.getSortField().equals(getDefaultSort())) {
				criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
			}
		}
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Reset the search fields
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contractSearch/",params= {"task=reset"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doReset(@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doReset");
		}

		// Enable the search box and set an empty report data
		form.setEnableContractSearch(true);
		form.resetSearchFilters();
		
		// Reset the sort direction
		form.setSortField(getDefaultSort());
		form.setSortDirection(getDefaultSortDirection());
		
		TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
				null, 0);
		request.setAttribute(Constants.REPORT_BEAN, reportData);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return forwards.get("input");
	}
	
	@RequestMapping(value ="/contractSearch/", params={"task=filter"}, method ={RequestMethod.POST}) 
    public String doFilter (@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		Collection errors = doValidate(form, request);
    	if(!errors.isEmpty()){
    		setErrorsInRequest(request, errors);
    		return forwards.get("input");
    	}
    	
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
	@RequestMapping(value ="/contractSearch/", params={"task=sort"}, method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		Collection errors = doValidate(form, request);
    	if(!errors.isEmpty()){
    		setErrorsInRequest(request, errors);
    		return forwards.get("input");
    	}
    	
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
	@RequestMapping(value ="/contractSearch/", params={"task=page"}, method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		Collection errors = doValidate(form, request);
    	if(!errors.isEmpty()){
    		setErrorsInRequest(request, errors);
    		return forwards.get("input");
    	}
    	
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
	
	
	/**
	 * 
	 * action method to forward to view customize contract page
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contractSearch/",params= {"task=customizeContract"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCustomizeContract(@Valid @ModelAttribute("tpaFeeScheduleContractSearchForm") TPAFeeScheduleContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( form, request);
			TPAFeeScheduleContractSearchReportData reportData = new TPAFeeScheduleContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String contractNumber = form.getSelectedContractNumber();
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT, contractNumber);
		return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}