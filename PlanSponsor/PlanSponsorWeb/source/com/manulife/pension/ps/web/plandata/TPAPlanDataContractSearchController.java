package com.manulife.pension.ps.web.plandata;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;


@Controller
@RequestMapping( value ="/plandata")
@SessionAttributes({"tpaPlanDataContractSearchForm"})

public class TPAPlanDataContractSearchController extends ReportController {

	@ModelAttribute("tpaPlanDataContractSearchForm") 
	public TPAPlanDataContractSearchForm populateForm() 
	{
		return new TPAPlanDataContractSearchForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/plandata/contractSearch.jsp");
		forwards.put("default","/plandata/contractSearch.jsp");
		forwards.put("sort","/plandata/contractSearch.jsp");
		forwards.put("error","/plandata/contractSearch.jsp");
		forwards.put("page","/plandata/contractSearch.jsp");
		forwards.put("search","/plandata/contractSearch.jsp");
		forwards.put("SelectContract","/plandata/TPAContractSelect.jsp");
		forwards.put("viewNoticePlanData","redirect:/do/viewNoticePlanData/");
	}

	
	
	public TPAPlanDataContractSearchController() {
		super(TPAPlanDataContractSearchController.class);
	}

	private static final String DEFAULT_SORT_FIELD = "contractName";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int CONTRACT_PAGE_SIZE = 35;
	private static Environment env = Environment.getInstance();
	public static final String REPORT_TITLE = "SEND Service";
	public static final String TPA_TITLE = "TPA: ";
	public static final String CONTRACT_LIST = "Contract List";
	public static final String CONTRACT_NAME = "Contract name";
	public static final String CONTRACT_NUMBER = "Contract number";
	public static final String LAST_UPDATED = "Last updated";
	public static final String LAST_UPDATED_BY = "Last updated by";
	public static final String SERVICE_SELECTED = "Service Selected";
	public static final String NO_DATA = "There is no data to display.";
	public static final String UNDERSCORE = "_";
	
	public static final FastDateFormat dateFormatter = FastDateFormat.getInstance("MM/dd/yyyy");
	public static final DecimalFormat decimalFormatter = new DecimalFormat("00");
	
	private String tpaFirmId;
	
	public String getTpaFirmId() {
		return tpaFirmId;
	}
	
	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}
	
	//synchronizes this method to avoid race condition.
	public static synchronized String formatDecimalFormatter(int value) { 
		return decimalFormatter.format(value); 
	}
	
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected int getPageSize(HttpServletRequest request) {
		return CONTRACT_PAGE_SIZE;
	}
	

	@RequestMapping(value ="/contractSearch/", method ={RequestMethod.GET,RequestMethod.POST}) 
    public String doDefault (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		doValidate(form, request);
		if(bindingResult.hasErrors()){
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
			setTpaFirmId(selectedTpaFirmId);
		} else {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		request.getSession().removeAttribute(Constants.NOTICE_PLAN_COMMON_VO);
		request.getSession().removeAttribute(Constants.NOTICE_PLAN_DATA_VO);
		request.getSession().removeAttribute(Constants.SELECTED_TPA_CONTRACT_IN_SESSION);

		forward = doCommon( form, request, response);
	
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/contractSearch/", params={"task=sort"}, method ={RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {

			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
        String forward = super.doSort(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	@RequestMapping(value ="/contractSearch/", params={"task=page"}, method ={RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
        String forward = super.doPage(form, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	@RequestMapping(value ="/contractSearch/", params={"task=filter"}, method ={RequestMethod.POST}) 
    public String doFilter (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
    	Boolean isValid = doValidate1(form,request);
		if (!isValid) {
			 return forwards.get("input");
        
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
	
	
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)  throws SystemException {
		
	    TPAPlanDataContractSearchForm tpaPlanDataContractSearchForm = (TPAPlanDataContractSearchForm) form;
		criteria.addFilter(
				TPAPlanDataContractSearchReportData.FILTER_TPA_FIRM_ID,
				String.valueOf(tpaPlanDataContractSearchForm.getTpaId()));
		
		String sitemode = env.getSiteLocation();
		String companyCode = sitemode.equals(Constants.SITEMODE_USA) ? RequestConstants.COMPANY_CODE_USA
						: RequestConstants.COMPANY_CODE_NY;

		criteria.addFilter(
		        TPAPlanDataContractSearchReportData.USER_PROFILE_ID,
		        String.valueOf(getUserProfile(request).getPrincipal().getProfileId()));
		
		criteria
				.addFilter(
				        TPAPlanDataContractSearchReportData.FILTER_COMPANY_CODE,
						companyCode);
		
		UserProfile userProfile = getUserProfile(request);
		if (userProfile.isInternalUser()) {
			criteria.setExternalUser(false);
		}
		else {
			criteria.setExternalUser(true);
		}

		if ((StringUtils.equals(FILTER_TASK, getTask(request)) 
				|| StringUtils.equals(PAGE_TASK, getTask(request)) || StringUtils.equals(SORT_TASK, getTask(request)))
				&& (StringUtils.isNotBlank(tpaPlanDataContractSearchForm.getContractNumber())
				|| StringUtils.isNotBlank(tpaPlanDataContractSearchForm.getContractName()))) {
			
			if (StringUtils.isNotBlank(tpaPlanDataContractSearchForm
					.getContractNumber())) {
				criteria.addFilter(
				        TPAPlanDataContractSearchReportData.FILTER_CONTRACT_NUMBER,
						tpaPlanDataContractSearchForm.getContractNumber());
			} 
			
			if (StringUtils.isNotBlank(tpaPlanDataContractSearchForm
					.getContractName())) {
				criteria.addFilter(
				        TPAPlanDataContractSearchReportData.FILTER_CONTRACT_NAME,
						tpaPlanDataContractSearchForm.getContractName());
			}
			
			// Get the available TPA firms for the logged in user. This will be used during the validation.
			Map<Integer, String> tpaFirmMap = null;
			if (userProfile.isInternalUser()) {
				tpaFirmMap = TPAServiceDelegate.getInstance()
						.retrieveTpaFirmsByTPAUserProfileId(
										Long.parseLong(userProfile.getSelectedTpaUserProfileId()), companyCode);
			}
			else {
				tpaFirmMap = TPAServiceDelegate.getInstance()
						.retrieveTpaFirmsByTPAUserProfileId(
										userProfile.getPrincipal().getProfileId(), companyCode);
			}
			StringBuffer buffer = new StringBuffer();
			
			for(Integer tpaFirmId : tpaFirmMap.keySet()) {
				buffer.append(tpaFirmId);
				buffer.append(",");
			}
			
			String masterTpaFirmIds = StringUtils.chomp(buffer.toString().trim(), ",");
			
			criteria.addFilter(TPAPlanDataContractSearchReportData.FILTER_MASTER_TPA_FIRM_IDS, masterTpaFirmIds);
			
		}
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return TPAPlanDataContractSearchReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		String selectedTpaFirmId = String.valueOf(getTpaFirmId());
		Calendar reportRequestedDate = Calendar.getInstance();
		
		StringBuilder fileName = new StringBuilder();
		fileName
				.append(selectedTpaFirmId)
				.append(TPAPlanDataContractSearchReportData.REPORT_NAME)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.MONTH) + 1).trim())
				.append(UNDERSCORE)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.DATE)).trim())
				.append(UNDERSCORE)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.YEAR)).trim());
		return fileName.toString();
	}

	
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		String forward = null;
		
		TPAPlanDataContractSearchForm tpaPlanDataContractSearchForm = (TPAPlanDataContractSearchForm) reportForm;
		
		if(isFilterTask(request)) {
			// Reset the sort criteria
			tpaPlanDataContractSearchForm.setSortField(getDefaultSort());
			tpaPlanDataContractSearchForm.setSortDirection(getDefaultSortDirection());
		}
		
		forward = super.doCommon( reportForm, request, response);
		
		TPAPlanDataContractSearchReportData reportData = (TPAPlanDataContractSearchReportData) request.getAttribute(Constants.REPORT_BEAN);
			
		tpaPlanDataContractSearchForm.setTotalCount((reportData.getDetails().size()));
			
		if (isDefaultTask(request)) {
			// If the total no of contracts is less than 50 then suppress the search box
			if (reportData.isContractCountBelowMaxLimit()) {
				tpaPlanDataContractSearchForm.setEnableContractSearch(false);
				/*
				if (reportData.getDetails().size() == 1) {
					TPAPlanDataContract planDataContract = (TPAPlanDataContract) reportData.getDetails().toArray()[0];
					return redirectToViewPage(request, mapping, planDataContract.getContractId());
				} */
			    return forward;
			} else {
				tpaPlanDataContractSearchForm.setEnableContractSearch(true);
				tpaPlanDataContractSearchForm.resetSearchFilters();
				//reportData.setDetails(null);
			}
			
			return forward;
		}
			
		if(isFilterTask(request)) {
			tpaPlanDataContractSearchForm.setEnableContractSearch(true);

			List<GenericException> errors = validateResult(request, reportForm, reportData);
			if (!errors.isEmpty()) {
				setErrorsInRequest(request, errors);
				
				reportData.setDetails(null);
				return forwards.get("input");
			} 
		}
		
		// If there is only one contract available then navigate the user to view custom fee schedule page
		/*
		if (reportData.getDetails().size() == 1) {
		    TPAPlanDataContract planDataContract = (TPAPlanDataContract) reportData.getDetails().toArray()[0];
			return redirectToViewPage(request, mapping, planDataContract.getContractId());
		} */

		return forwards.get("input");
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
		TPAPlanDataContractSearchReportData reportData = (TPAPlanDataContractSearchReportData) data;
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		TPAPlanDataContractSearchForm tpaPlanDataContractSearchForm = (TPAPlanDataContractSearchForm) reportForm;
		
		if (reportData.getDetails().isEmpty()) {
			if (StringUtils.isNotBlank(tpaPlanDataContractSearchForm
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

			if (StringUtils.isNotBlank(tpaPlanDataContractSearchForm
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
		
		TPAPlanDataContractSearchForm tpaPlanDataContractSearchForm = (TPAPlanDataContractSearchForm) reportForm;
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		boolean isValid = true;
		
		String contractName = StringUtils.trimToNull(tpaPlanDataContractSearchForm.getContractName());
		String contractId = tpaPlanDataContractSearchForm.getContractNumber();
				
		// Validate contract name length >0 & <3
		if (StringUtils.isNotBlank(contractName)) {

			if (!isValidContractName(tpaPlanDataContractSearchForm
					.getContractName())) {
				ValidationError exception = new ValidationError(
						Constants.TPA_CONTRACT_NAME_SORT_FIELD,
						ErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
				errorMessages.add(exception);
			}
		}
		
		// Validate the contract number field
		if (StringUtils.isNotBlank(contractId)) {

			if (!NumberUtils.isNumber(tpaPlanDataContractSearchForm
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
	
	private Boolean doValidate1( ActionForm form,
			HttpServletRequest request)  {
		boolean isValid = true;
		
		if (isFilterTask(request)) {
			isValid = validateInputFields(request, form);
		}

		if (!isValid) {
			TPAPlanDataContractSearchReportData reportData = new TPAPlanDataContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}
		return isValid;
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
	
	/*private ActionForward redirectToViewPage(HttpServletRequest request,
			ActionMapping mapping, Integer contractId) { 
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT, contractId.toString());
		return findForward(mapping, Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}*/
		
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
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		Collection errors = new ArrayList();
        
		StringBuffer buffer = new StringBuffer();
		
		String tpaDescription=StringUtils.EMPTY;
		String tpaFirmIdString = String.valueOf(request.getSession().getAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID));
		
		TPAPlanDataContractSearchForm form = (TPAPlanDataContractSearchForm) reportForm;
		
		String contractName = form.getContractName();
		boolean errorExists = false;
		if(form.getContractName().length()>1 && form.getContractName().length()<3) {
			errorExists = true;
		}
		
		Integer contractNumber;
		try {
		contractNumber = !TPAPlanDataWebUtility.isNullOrEmpty(form.getContractNumber())?Integer.valueOf(form.getContractNumber()):null;
		}
		catch (Exception e) {
			contractNumber = null;
			errorExists = true;
		}
		
		if(StringUtils.isEmpty(tpaFirmIdString) 
				|| !StringUtils.isNumeric(tpaFirmIdString)) {
			throw new SystemException("Incorrect TPA ID - " + tpaFirmIdString);
		}
		
		int  tpaFirmId = Integer.valueOf(tpaFirmIdString);
		
		TPAFirmInfo firm = TPAServiceDelegate.getInstance().getFirmInfo(tpaFirmId);
		String tpaFirmName = firm.getName();
		
		Calendar reportRequestedDate = Calendar.getInstance();

        buffer.append(REPORT_TITLE).append(LINE_BREAK).append(LINE_BREAK);
        
        buffer.append(escapeField(tpaDescription.concat(TPA_TITLE).concat(String.valueOf(tpaFirmId)).concat(Constants.HYPHON_SYMBOL).concat(tpaFirmName))).append(LINE_BREAK).append(LINE_BREAK);
        
        buffer.append(CONTRACT_LIST).append(LINE_BREAK);
        
        if(errorExists) {
        	buffer.append(NO_DATA);
        	return buffer.toString().getBytes();
        }
        
        Collection filteredData = getFilteredResult(report.getDetails(), contractName, contractNumber);
        
        if(null==filteredData || filteredData.size()==0) {
        	buffer.append(NO_DATA);
        	return buffer.toString().getBytes();
        }
        
        Iterator iterator = filteredData.iterator();
        
        buffer.append(CONTRACT_NAME).append(COMMA)
				.append(CONTRACT_NUMBER).append(COMMA)
				.append(LAST_UPDATED).append(COMMA)
				.append(LAST_UPDATED_BY).append(COMMA)
				.append(SERVICE_SELECTED).append(LINE_BREAK);
        
        while (iterator.hasNext()) {
        	TPAPlanDataContract theItem = 
				(TPAPlanDataContract) iterator.next();
        	buffer.append(DOUBLE_QUOTE).append(theItem.getContractName()).append(DOUBLE_QUOTE).append(COMMA)
        		  .append(DOUBLE_QUOTE).append(theItem.getContractId()).append(DOUBLE_QUOTE).append(COMMA);
        	if(null!=theItem.getCreatedTS()) {
        		buffer.append(DOUBLE_QUOTE).append(dateFormatter.format(theItem.getCreatedTS())).append(DOUBLE_QUOTE);
        	}
        	buffer.append(COMMA);
        	if(null!=theItem.getCreatedTS()) {
        			buffer.append(DOUBLE_QUOTE).append(theItem.getCreatedUser()).append(DOUBLE_QUOTE);
        	}
        	buffer.append(COMMA);
        	buffer.append(DOUBLE_QUOTE).append(theItem.getServiceSelected()).append(DOUBLE_QUOTE);
        	buffer.append(LINE_BREAK);
        }
        
        return buffer.toString().getBytes();
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
	@RequestMapping(value ="/contractSearch/" ,params= {"task=reset"}, method =  {RequestMethod.POST}) 
    public String doReset (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
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
		
		/*TPAPlanDataContractSearchReportData reportData = new TPAPlanDataContractSearchReportData(
				null, 0);
		request.setAttribute(Constants.REPORT_BEAN, reportData);*/
		
		String forward = doCommon( form, request, response);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doReset");
		}
		return forward;
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
	
	@RequestMapping(value ="/contractSearch/",params= {"task=customizeContract"}, method =  {RequestMethod.POST}) 
    public String doCustomizeContract (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
		String contractNumber = form.getSelectedContractNumber();
		String contractName = form.getSelectedContractName();
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT, contractNumber);
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT_NAME, contractName);
		if(null!=request.getSession().getAttribute(Constants.TPA_PLAN_DATA_FORM)){
			 TabPlanDataForm tabPlanDataForm = (TabPlanDataForm) request.getSession().getAttribute(Constants.TPA_PLAN_DATA_FORM);
			 tabPlanDataForm.setAction("default");
			 request.getSession().setAttribute(Constants.TPA_PLAN_DATA_FORM, tabPlanDataForm);
		}
		return forwards.get(Constants.VIEW_NOTICE_PLAN_DATA);
	}
	
	@RequestMapping(value ="/contractSearch/", params={"task=download"}, method =  {RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownload");
        }

        String forward = super.doDownload(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownload");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	@RequestMapping(value ="/contractSearch/", params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
    public String doDownloadAll (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownloadAll");
        }

        String forward = super.doDownload(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownloadAll");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	@RequestMapping(value ="/contractSearch/", params={"task=print"}, method =  {RequestMethod.GET}) 
    public String doPrint (@Valid @ModelAttribute("tpaPlanDataContractSearchForm") TPAPlanDataContractSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doprint");
        }

        String forward = super.doPrint(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doprint");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
	private Collection getFilteredResult(Collection<TPAPlanDataContract> list, String contractName, Integer contractNumber) {
		ArrayList<TPAPlanDataContract> contractFilteredList;
		if(contractNumber==null) {
			contractFilteredList = (ArrayList<TPAPlanDataContract>) list;
		}
		else {
			contractFilteredList = new ArrayList<TPAPlanDataContract>();
			for(TPAPlanDataContract item : list) {
				if(item.getContractId() == contractNumber) {
					contractFilteredList.add(item);
				}
			}
		}
		
		ArrayList<TPAPlanDataContract> finalList;
		if(contractName.length()==0) {
			return contractFilteredList;
		}
		else {
			finalList = new ArrayList<TPAPlanDataContract>();
			for(TPAPlanDataContract item : contractFilteredList) {
				if(item.getContractName().toLowerCase().contains(contractName.toLowerCase())) {
					finalList.add(item);
				}
			}
		}
		
		return finalList;
	}
	

	
	/**
	 * Adding escape field if any comma character found in a String
	 * 
	 * @param field
	 * @return
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append(QUOTE).append(field).append(QUOTE);
			return newField.toString();

		} else {
			return field;
		}
	}
}