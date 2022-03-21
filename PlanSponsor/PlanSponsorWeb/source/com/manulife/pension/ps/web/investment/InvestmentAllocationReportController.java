package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.FundSeriesNameHelper;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationTotals;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping(value = "/investment")
@SessionAttributes({ "investmentAllocationPageForm" })

public class InvestmentAllocationReportController extends ReportController {

	@ModelAttribute("investmentAllocationPageForm")
	public InvestmentAllocationPageForm populateForm() {
		return new InvestmentAllocationPageForm();
	}

	public static Map<String,String> forwards = new HashMap<>();
	public static final String INVESTMENT_ALLOCATION_REPORT = "/investment/investmentAllocationReport.jsp";
	static {
		forwards.put("input", INVESTMENT_ALLOCATION_REPORT);
		forwards.put("default", INVESTMENT_ALLOCATION_REPORT);
		forwards.put("sort", INVESTMENT_ALLOCATION_REPORT);
		forwards.put("filter", INVESTMENT_ALLOCATION_REPORT);
		forwards.put("page", INVESTMENT_ALLOCATION_REPORT);
		forwards.put("print", INVESTMENT_ALLOCATION_REPORT);
	}

	// protected static final String DEFAULT_SORT_FIELD =
	// InvestmentAllocationReportData.SORT_OPTION;
	// protected static final String DEFAULT_SORT_DIRECTION =
	// ReportSort.ASC_DIRECTION;
	protected static final String DOWNLOAD_COLUMN_HEADING_SUMMARY = ",Options with assets, Participants invested, Employee assets($), Employer assets($), Total assets($), % of total";
	protected static final String DOWNLOAD_COLUMN_HEADING_FUNDS = "Investment option,Participants invested current ,Participants invested future, Employee assets ($), Employer assets($), Total assets ($), % of total";
	protected static final String DOWNLOAD_COLUMN_HEADING_FUNDS_1 = "Investment option,Participants invested current, ,Employee assets ($), Employer assets($), Total assets ($), % of total";
	protected static final int DEFAULT_PAGE_SIZE = ReportCriteria.NOLIMIT_PAGE_SIZE;

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {

		InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) form;

		Contract contract = getUserProfile(request).getCurrentContract();

		criteria.addFilter(InvestmentAllocationReportData.FILTER_CONTRACT_NO,
				new Integer(contract.getContractNumber()));

		criteria.addFilter(InvestmentAllocationReportData.FILTER_ISPBA, new Boolean(contract.isPBA()));

		criteria.addFilter(InvestmentAllocationReportData.FILTER_SITE, Environment.getInstance().getSiteLocation());

		// Populate as of date from the form into the criteria.
		if (investmentPageForm.getAsOfDateReport() == null) {
			investmentPageForm.setAsOfDateReport(String.valueOf(contract.getContractDates().getAsOfDate().getTime()));
		}
		criteria.addFilter(InvestmentAllocationReportData.FILTER_ASOFDATE_REPORT,
				investmentPageForm.getAsOfDateReport());

		// filter for view option
		criteria.addFilter(InvestmentAllocationReportData.FILTER_VIEW_OPTION, investmentPageForm.getViewOption());

		// Populate current date from the contract object into the criteria.

		criteria.addFilter(InvestmentAllocationReportData.FILTER_CURRENTDATE,
				Long.toString(contract.getContractDates().getAsOfDate().getTime()));
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return InvestmentAllocationReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return InvestmentAllocationReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		// the default sort field and order is determined behind the scenes
		// by the database
		return null;
	}

	protected String getDefaultSortDirection() {
		// the default sort field and order is determined behind the scenes
		// by the database
		return null;
	}

	protected int getPageSize(HttpServletRequest request) {

		return DEFAULT_PAGE_SIZE;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {

		InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) reportForm;
		String asOfDate = investmentPageForm.getAsOfDateReport();

		String location = Environment.getInstance().getDBSiteLocation();

		StringBuffer buffer = new StringBuffer();
		UserRole role = null;

		Contract currentContract = getUserProfile(request).getCurrentContract();
		buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(COMMA)
				.append(currentContract.getCompanyName()).append(LINE_BREAK);

		buffer.append("As of,").append(DateRender.formatByPattern(new Date(Long.valueOf(asOfDate).longValue()), " ",
				RenderConstants.MEDIUM_MDY_SLASHED));

		role = getUserProfile(request).getRole();
		if (role instanceof InternalUser || role instanceof ThirdPartyAdministrator) {
			if (investmentPageForm.getViewOption().equals(Constants.VIEW_BY_ASSET))
				buffer.append(COMMA).append("Asset view");
			else
				buffer.append(COMMA).append("Activity view");
		}

		buffer.append(LINE_BREAK).append(LINE_BREAK);

		InvestmentAllocationReportData IAReportData = (InvestmentAllocationReportData) report;

		String series = FundSeriesNameHelper.getFundSeriesName(currentContract.getProductId(), location,
				currentContract.getFundPackageSeriesCode());

		buffer.append(series + DOWNLOAD_COLUMN_HEADING_SUMMARY);

		ReportCriteria criteria = report.getReportCriteria();

		Iterator iterator = IAReportData.getAllocationTotals().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			AllocationTotals theTotals = (AllocationTotals) iterator.next();
			if (FundCategory.LIFECYCLE.equals(theTotals.getFundCategoryType())) {
				buffer.append(InvestmentAllocationPageForm.LIFECYCLE_TEXT).append(COMMA);
			} else if (FundCategory.LIFESTYLE.equals(theTotals.getFundCategoryType())) {
				buffer.append(InvestmentAllocationPageForm.LIFESTYLE_TEXT).append(COMMA);
			} else if (FundCategory.GIFL.equals(theTotals.getFundCategoryType())) {
				buffer.append(InvestmentAllocationPageForm.GIFL_TEXT).append(COMMA);
			} else if (FundCategory.NON_LIFESTYLE_LIFECYCLE.equals(theTotals.getFundCategoryType())) {
				buffer.append(getCsvString(investmentPageForm.getTotalText())).append(COMMA);
			} else if (FundCategory.PBA.equals(theTotals.getFundCategoryType())) {
				buffer.append(InvestmentAllocationPageForm.PBA_TEXT).append(COMMA);
			}

			buffer.append(theTotals.getNumberOfOptions()).append(COMMA);
			buffer.append(theTotals.getParticipantsInvested()).append(COMMA);
			buffer.append(theTotals.getEmployeeAssets()).append(COMMA);
			buffer.append(theTotals.getEmployerAssets()).append(COMMA);
			buffer.append(theTotals.getTotalAssets()).append(COMMA);
			buffer.append(theTotals.getPercentageOfTotal() * 100d).append(COMMA);
		}

		buffer.append(LINE_BREAK);

		buffer.append(LINE_BREAK);

		if (investmentPageForm.isAsOfDateReportCurrent())
			buffer.append(DOWNLOAD_COLUMN_HEADING_FUNDS);
		else
			buffer.append(DOWNLOAD_COLUMN_HEADING_FUNDS_1);

		SortedMap categoryFundMap = IAReportData.getAllocationDetails();
		Set categoryKeys = categoryFundMap.keySet();
		Iterator categoryFundIterator = categoryKeys.iterator();

		// iterate for each fund category
		while (categoryFundIterator.hasNext()) {
			buffer.append(LINE_BREAK);

			FundCategory fundCategory = (FundCategory) categoryFundIterator.next();

			buffer.append(fundCategory.getCategoryDesc());
			buffer.append(LINE_BREAK);

			ArrayList allocationDetailsList = (ArrayList) categoryFundMap.get(fundCategory);
			Iterator detailsIterator = allocationDetailsList.iterator();

			// iterate for each investment option in the current category
			while (detailsIterator.hasNext()) {
				AllocationDetails allocationDetails = (AllocationDetails) detailsIterator.next();

				buffer.append(allocationDetails.getFundName()).append(COMMA);

				if (investmentPageForm.isAsOfDateReportCurrent()) {
					buffer.append(allocationDetails.getParticipantsInvestedCurrent()).append(COMMA);
					buffer.append(allocationDetails.getParticipantsInvestedFuture()).append(COMMA);
				} else {
					buffer.append(allocationDetails.getParticipantsInvestedCurrent()).append(COMMA);
					buffer.append(COMMA);
				}

				buffer.append(allocationDetails.getEmployeeAssets()).append(COMMA);
				buffer.append(allocationDetails.getEmployerAssets()).append(COMMA);
				buffer.append(allocationDetails.getTotalAssets()).append(COMMA);
				buffer.append(allocationDetails.getPercentageOfTotal() * 100d).append(COMMA);
				buffer.append(LINE_BREAK);
			}

		}

		return buffer.toString().getBytes();
	}

	@RequestMapping(value = "/investmentAllocationReport/", method = {RequestMethod.GET})
	public String doDefault(
			@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}

		String forward = super.doDefault(actionForm, request, response);

		InvestmentAllocationReportData report = (InvestmentAllocationReportData) request
				.getAttribute(Constants.REPORT_BEAN);

		report.sort(AllocationDetails.MARKETING_SORT_ORDER, InvestmentAllocationReportData.SORT_ASCENDING);
		Collection errors = new ArrayList();

		request.setAttribute(Constants.REPORT_BEAN, report);
		request.getSession(false).setAttribute(Constants.REPORT_BEAN, report);
		if (!hasAllocations(report.getAllocationTotals())) {
			errors.add(new GenericException(ErrorCodes.NO_ALLOCATIONS_FOUND));
		}

		actionForm.setCurrentBusinessDate(report.getContractDates().getAsOfDate());

		actionForm.setNonLifestyle(report.getAllocationTotals());

		request.setAttribute(Environment.getInstance().getErrorKey(), errors);

		return forwards.get(forward);
	}

	@RequestMapping(value = "/investmentAllocationReport/", params = {"task=sort"}, method = {RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;										
			}
		}
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}

		String task = getTask(request);
		InvestmentAllocationReportData report = null;
		String forward = null;
		HttpSession session = request.getSession(false);
		report = (InvestmentAllocationReportData) session.getAttribute(Constants.REPORT_BEAN);
		session.removeAttribute(Constants.REPORT_BEAN);
		String column = form.getSortField();
		String direction = form.getSortDirection();

		session.setAttribute(Constants.INVALLOCATION_SORT_COLUMN, column);
		session.setAttribute(Constants.INVALLOCATION_SORT_DIRECTION, direction);

		if (column.equalsIgnoreCase("option"))
			column = AllocationDetails.FUND_NAME;
		if (column.equalsIgnoreCase("participantsInvested"))
			column = AllocationDetails.PARTICIPANTS_INVESTED_CURRENT;
		if (column.equalsIgnoreCase("employeeAssets"))
			column = AllocationDetails.EMPLOYEE_ASSETS;
		if (column.equalsIgnoreCase("employerAssets"))
			column = AllocationDetails.EMPLOYER_ASSETS;
		if (column.equalsIgnoreCase("totalAssets"))
			column = AllocationDetails.TOTAL_ASSETS;
		if (column.equalsIgnoreCase("percentageOfTotal"))
			column = AllocationDetails.PERCENTAGE_OF_TOTAL;

		if (direction.equalsIgnoreCase("desc"))
			direction = InvestmentAllocationReportData.SORT_DESCENDING;
		if (direction.equalsIgnoreCase("asc"))
			direction = InvestmentAllocationReportData.SORT_ASCENDING;

		report.sort(column, direction);

		session.setAttribute(Constants.REPORT_BEAN, report);
		request.setAttribute(Constants.REPORT_BEAN, report);
		forward = forwards.get(task);
		return forward;
	}

	private boolean hasAllocations(ArrayList allocationTotals) {

		AllocationTotals[] totals = new AllocationTotals[allocationTotals.size()];
		allocationTotals.toArray(totals);

		for (int i = 0; i < totals.length; i++) {

			AllocationTotals total = totals[i];
			if (total.getTotalAssets() > 0.0d) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Get the report Data from the session if it exists or the task is default. If
	 * the task is not default (ie. doPrint or doDownload), the reportData will be
	 * retrieved from the session
	 * 
	 * @param reportId
	 *            The reportId
	 * @param criteria
	 *            The criteria to populate
	 * @param request
	 *            The current request object
	 * 
	 * @return reportData The ReportData object containing the contents for the
	 *         report
	 * 
	 * @exception SystemException
	 * @exception SystemUnavailableException
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("+ getReportData");
		}

		String task = super.getTask(request);

		HttpSession session = request.getSession(false);
		ReportData report = (ReportData) session.getAttribute(Constants.REPORT_BEAN);

		ReportData bean = null;
		if (report == null || task.equals(DEFAULT_TASK) || task.equals(FILTER_TASK)) {
			bean = super.getReportData(reportId, reportCriteria, null);
		} else {
			bean = report;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("- getReportData");
		}

		return bean;
	}

	/**
	 * Invokes the filter task (e.g. limiting date range). It uses the common
	 * workflow with validateForm set to true.
	 *
	 * @see #doCommon(BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 */
	@RequestMapping(value = "/investmentAllocationReport/", params = {"task=filter"}, method = {RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("+ doFilter");
		}
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}

		String forward = super.doFilter(form, request, response);
		InvestmentAllocationReportData report = (InvestmentAllocationReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		report.sort(AllocationDetails.MARKETING_SORT_ORDER, InvestmentAllocationReportData.SORT_ASCENDING);
		form.setSortField(null);
		form.setSortDirection(null);
		request.getSession(false).setAttribute(Constants.REPORT_BEAN, report);

		if (logger.isDebugEnabled()) {
			logger.debug("- doFilter");
		}

		return forwards.get(forward);
	}
	
	@RequestMapping(value = "/investmentAllocationReport/", params = {"task=print"}, method = {RequestMethod.GET })
	public String doPrint(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("+ doFilter");
		}
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}

		String forward = super.doPrint(form, request, response);
		InvestmentAllocationReportData report = (InvestmentAllocationReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		report.sort(AllocationDetails.MARKETING_SORT_ORDER, InvestmentAllocationReportData.SORT_ASCENDING);
		form.setSortField(null);
		form.setSortDirection(null);
		request.getSession(false).setAttribute(Constants.REPORT_BEAN, report);

		if (logger.isDebugEnabled()) {
			logger.debug("- doFilter");
		}

		return forwards.get(forward);
	}
	
	@RequestMapping(value = "/investmentAllocationReport/", params = {"task=download"}, method = {RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("+ doFilter");
		}

		String forward = super.doDownload(form, request, response);
		InvestmentAllocationReportData report = (InvestmentAllocationReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		report.sort(AllocationDetails.MARKETING_SORT_ORDER, InvestmentAllocationReportData.SORT_ASCENDING);
		form.setSortField(null);
		form.setSortDirection(null);
		request.getSession(false).setAttribute(Constants.REPORT_BEAN, report);

		if (logger.isDebugEnabled()) {
			logger.debug("- doFilter");
		}

		return forwards.get(forward);
	}


	/**
	 * Validate the input action form only for Activity view. Date selected cannot
	 * be more than 13 months ago from most recent month end date.
	 * 
	 */

	/*public String doCommon(InvestmentAllocationPageForm form,  HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		String forward = super.doCommon(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}*/

	@RequestMapping(value = "/investmentAllocationReport/", params = {"task=page"}, method = {RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		
		Collection validationErrors = doValidate(form, request);
		if(!validationErrors.isEmpty()){
			setErrorsInSession(request, validationErrors);
			return forwards.get("input");
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}


	/**
	 * Validate the input action form only for Activity view.
	 * Date selected cannot be more than 13 months ago from most
	 * recent month end date.
	 * 
	 * 
	 */
	protected Collection doValidate(ActionForm form, HttpServletRequest request)
	{
		
		Collection errors = super.doValidate(form, request);
		InvestmentAllocationPageForm actionForm = (InvestmentAllocationPageForm) form;
 		
        // if this is called using the default URL i.e. no parameters
		// do not validate
		if (request.getParameterNames().hasMoreElements()) {
			// validate only for Activity view
			if (actionForm.getViewOption() != null
					&& actionForm.getAsOfDateReport() != null
					&& actionForm.getViewOption().equals(Constants.VIEW_BY_ACTIVITY) ) {

				Contract contract = getUserProfile(request).getCurrentContract();

				// retrieve the most recent month end, ie, the first element of the monthEndDates
				ArrayList monthEndDates =(ArrayList) contract.getContractDates().getMonthEndDates();
				if (monthEndDates !=null && monthEndDates.size()>0)//CL 7105 : check for no month end dates
				{
				Date mostRecentMonthEnd = (Date) monthEndDates.get(0);
				Calendar cal = Calendar.getInstance();
				cal.setTime(mostRecentMonthEnd);
				// calculate the month end date 13 months before most recent month end
				cal.add(Calendar.MONTH, -13);
			    Date activityViewMonthEndBoundary = cal.getTime();
			    /*
			     * eliminate the possibility of backend returning
			     * incomplete year of data
			     */			    
				if (Long.valueOf(actionForm.getAsOfDateReport()).longValue() 
						< activityViewMonthEndBoundary.getTime()) {
					errors.add(new GenericException(ErrorCodes.INVALID_ASOFDATE_SELECTED));
				}
				}
			}
	
			/*
			 * Resets the information for JSP to display.
			 */
			if (errors.size() > 0) {
				/*
				 * Repopulates action form and request with default information.
				 */
				populateReportForm(actionForm, request);
				// signal the JSP to display the date dropdowns again for the user to change
				// their selection
				request.setAttribute("displayDates", "true");
			}
		}
		return errors;
	} 
	/*
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations
	 * 
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}