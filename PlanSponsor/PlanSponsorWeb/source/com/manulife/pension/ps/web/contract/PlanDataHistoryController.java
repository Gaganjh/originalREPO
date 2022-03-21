package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.contract.valueobject.PlanDataHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.contract.util.PlanDataHistoryValidator;
import com.manulife.pension.ps.web.contract.util.PlanDataValidationErrors;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.util.PlanUtils;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.validator.ValidationError;

/**
 * PlanDataHistoryAction class. This class is used to forward the users's
 * request to Plan Data History page
 * 
 * @author David Li
 */

@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"planDataHistoryForm"})

public class PlanDataHistoryController extends ReportController {
	
	@ModelAttribute("planDataHistoryForm") 
	public  PlanDataHistoryForm populateForm()
	{
		return new  PlanDataHistoryForm();
		}
	
	public static Map<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("input", "/contract/planDataHistory.jsp");
		forwards.put("default", "/contract/planDataHistory.jsp");
		forwards.put("sort", "/contract/planDataHistory.jsp");
		forwards.put("filter", "/contract/planDataHistory.jsp");
		forwards.put("page", "/contract/planDataHistory.jsp");
		forwards.put("print", "/contract/planDataHistory.jsp");
	}
	
	
	public PlanDataHistoryController() {
		
		super(PlanDataHistoryController.class);
	}


	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		PlanDataHistoryForm historyForm = (PlanDataHistoryForm) form;
		int planId = historyForm.getPlanId();
		String fieldName = StringUtils.trimToEmpty(historyForm.getFieldName());
		String userName = StringUtils.trimToEmpty(historyForm.getUserName());
		String fromDate = StringUtils.trimToEmpty(historyForm.getFromDate());
		String toDate = StringUtils.trimToEmpty(historyForm.getToDate());

		criteria.addFilter(PlanDataHistoryReportData.FILTER_PLAN_ID,
				new Integer(planId));

		if (!StringUtils.isBlank(fieldName)) {
			criteria.addFilter(PlanDataHistoryReportData.FILTER_FIELD_NAME,
					fieldName);
		}

		if (!StringUtils.isBlank(userName)) {
			criteria.addFilter(PlanDataHistoryReportData.FILTER_USER_ID,
					userName);
		}

		try {
			if (!StringUtils.isBlank(fromDate)) {
				Date from = PlanUtils.s2d(fromDate);
				criteria.addFilter(PlanDataHistoryReportData.FILTER_FROM_DATE,
						PlanUtils.d2ts(from));
			}

			if (!StringUtils.isBlank(toDate)) {
				Date to = PlanUtils.s2d(toDate);
				Date nextDay = PlanUtils.getNextDay(to);
				criteria.addFilter(PlanDataHistoryReportData.FILTER_TO_DATE,
						PlanUtils.d2ts(nextDay));
			}
		} catch (ParseException pe) {
			// should not happen
		}
	}

	protected void populateReportForm(
			final BaseReportForm reportForm, final HttpServletRequest request) {
		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	protected String getDefaultSort() {
		return PlanDataHistoryReportData.DEFAULT_SORT;
	}

	protected String getDefaultSortDirection() {
		return PlanDataHistoryReportData.DEFAULT_SORT_DIRECTION;
	}

	protected String getReportId() {
		return PlanDataHistoryReportData.REPORT_ID;
	}

	protected String getReportName() {
		return PlanDataHistoryReportData.REPORT_NAME;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	protected Collection doValidate( BaseReportForm form,

			HttpServletRequest request) {
		PlanDataHistoryForm historyForm = (PlanDataHistoryForm) form;
		// This code has been changed and added to
		// Validate form and request against penetration attack, prior to other validations
     	List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		try {
			if (getTask(request).equals(FILTER_TASK)) {
				PlanDataHistoryValidator validator = new PlanDataHistoryValidator();
				validator.validate(historyForm, validationErrors);

				if (validationErrors != null && validationErrors.size() > 0) {
					PlanDataValidationErrors errors = new PlanDataValidationErrors(
							validationErrors);
					request.setAttribute(PlanConstants.VALIDATION_ERRORS,
						errors);

					populateReportForm(historyForm, request);
					request.setAttribute(Constants.REPORT_BEAN, historyForm
							.getReport());
				}
			}
		} catch (Exception e) {
			logger
					.warn("Exception catched in PlanDataHistoryAction.doValidate:"
							+ e.getMessage());
		}

		return validationErrors;
	}


	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		PlanDataHistoryForm historyForm = (PlanDataHistoryForm) reportForm;
		if (historyForm.getPlanId() == 0
			&& request.getParameter("planId") != null) {
			historyForm.setPlanId(Integer.valueOf(
					request.getParameter("planId")).intValue());
		}

		String forward = super.doCommon(historyForm, request, response);
		PlanDataHistoryReportData report = (PlanDataHistoryReportData) request.getAttribute(Constants.REPORT_BEAN);
		historyForm.setReport(report);

		return forward;
	}

	public BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		PlanDataHistoryForm historyForm = (PlanDataHistoryForm) reportForm;

		try {
			PlanDataHistoryForm blankForm = (PlanDataHistoryForm) historyForm
					.getClass().newInstance();
			blankForm.setPlanId(historyForm.getPlanId());

			PropertyUtils.copyProperties(historyForm, blankForm);
			historyForm.reset( request);
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"resetForm", "exception in resetting the form");
		}

		return historyForm;
	}
	
	@RequestMapping(value = "/planDataHistory/", method = RequestMethod.GET)
	public String doDefault(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				return forwards.get(errDirect);
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planDataHistory/",params = { "task=print"}, method = RequestMethod.GET)
	public String doPrint(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				return forwards.get(errDirect);
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doPrint(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = "/planDataHistory/", params = { "task=default" }, method = RequestMethod.POST)
	public String doReset(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect);
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planDataHistory/", params = { "task=filter" }, method = { RequestMethod.POST })
	public String doFilter(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect);
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doFilter(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planDataHistory/", params = { "task=page" }, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				return forwards.get(errDirect);
			}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doPage(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/planDataHistory/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("planDataHistoryForm") PlanDataHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);				
				request.setAttribute(Constants.REPORT_BEAN, actionForm.getReport());
				return forwards.get(errDirect);
		}
		}
		Collection validationErrors = doValidate(actionForm, request);
		if (!validationErrors.isEmpty()) {
			setErrorsInSession(request, validationErrors);
			return forwards.get("default");
		}
		String forward = super.doSort(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

}
