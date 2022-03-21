package com.manulife.pension.ps.web.fandp;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.fap.FapController;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.fap.util.FapReportsUtility;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTPAFContinue;

/**
 * Action class specific to the TPA Funds & Performance
 *
 * @author ayyalsa
 *
 */
@Controller
@RequestMapping(value ="/fap")

public class TPAFandpController extends PSTPAFandpBaseController {

	@ModelAttribute("fapForm")
	public FapForm populateForm() {
		return new FapForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/fap/fap.jsp");
		forwards.put("continue", "/WEB-INF/fap/tpaFandpFilterResults.jsp");
		forwards.put("continue_FapFilter","/WEB-INF/fap/tpaFandpFilterResults.jsp");
		forwards.put("customQuery","/WEB-INF/fap/tpaFandpCustomQueryFilter.jsp");
		forwards.put("assetClassDefinitions","/WEB-INF/fap/tpaFandpAssetClassDefinitions.jsp");
		forwards.put("fundInformation","/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("pricesAndYTD", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("performanceAndFees", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("standardDeviation", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("fundCharacteristics1", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("fundCharacteristics2", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("morningstar", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("fundScorecard", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("additionalParameters", "/WEB-INF/fap/tpaFapAdditonalParametersBox.jsp");
		forwards.put("PerformanceAndFeesMonthly", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
		forwards.put("PerformanceAndFeesQuarterly", "/WEB-INF/fap/tpaFandpTabLayout.jsp");
	}
	
	@RequestMapping(value = {"/tpaFandp/"}, method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("fapForm") FapForm actionForm,BindingResult bindingResult,HttpServletRequest request,
	            HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				FapForm fapActionForm = (FapForm) actionForm;
				try {
					populateBaseFilterOptions(fapActionForm, request);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				fapActionForm.setMessagesExist(true);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("continue");
			}
		}

		FapForm fapForm = (FapForm) actionForm;

		String companyName = CommonEnvironment.getInstance().getSiteLocation().toUpperCase();

		// Based on the URL set the company ID
		if (StringUtils.contains(companyName, FapConstants.COMPANY_NAME_US)) {
			fapForm.setCompanyId(FapConstants.COMPANY_ID_US);
		} else if (StringUtils.contains(companyName, FapConstants.COMPANY_NAME_NY)) {
			fapForm.setCompanyId(FapConstants.COMPANY_ID_NY);
		}

		setAttributesInSession(request, "FileName", "TPA_Fund&Performance");

		// populate the Base filter with default values

			forward = super.doDefault(fapForm, request, response);
		postExecute(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);

	}
	@RequestMapping(value = {"/tabs/","/fapFilterAction/"}, method = {RequestMethod.GET})
	public String doDefaultFapFilter(@Valid @ModelAttribute("fapForm") FapForm actionForm,BindingResult bindingResult,HttpServletRequest request,
	            HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				FapForm fapActionForm = (FapForm) actionForm;
				try {
					populateBaseFilterOptions(fapActionForm, request);
				} catch (SystemException e) {
					e.printStackTrace();
				}
				fapActionForm.setMessagesExist(true);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("continue_FapFilter");
			}
		}

		FapForm fapForm = (FapForm) actionForm;

		String companyName = CommonEnvironment.getInstance().getSiteLocation().toUpperCase();

		// Based on the URL set the company ID
		if (StringUtils.contains(companyName, FapConstants.COMPANY_NAME_US)) {
			fapForm.setCompanyId(FapConstants.COMPANY_ID_US);
		} else if (StringUtils.contains(companyName, FapConstants.COMPANY_NAME_NY)) {
			fapForm.setCompanyId(FapConstants.COMPANY_ID_NY);
		}

		setAttributesInSession(request, "FileName", "TPA_Fund&Performance");

		// populate the Base filter with default values

			forward = super.doDefault(fapForm, request, response);
		postExecute(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);

	}

	/**
	 * Gets the Columns Value Object and the Tab's value Object from the request and
	 * sets it to the form
	 * 
	 * @param mapping
	 * @param actionform
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */ 
	@RequestMapping(value = { "/tpaFandp/", "/tabs/", "/fapFilterAction/" }, params = {
			"action=displayTabs" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doDisplayTabs(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}

		form.setColumnsInfo((HashMap<String, List>) request.getAttribute(FapConstants.COLUMNS_INFO_OBJECT));

		form.setFundcheckAsOfDate((String) request.getAttribute(FapConstants.FUND_CHECK_ASOFDATE));

		String tabName = getSelectedTabName(form);
		// set the tabSelected
		form.setTabSelected(tabName);
		
		postExecute(form, request, response);
		
		return forwards.get(form.getTabSelected());

	}

	@RequestMapping(value = { "/tpaFandp/", "/tabs/", "/fapFilterAction/" }, params = {
			"action=enableCustomQuery" }, method = { RequestMethod.POST })
	public String doEnableCustomQuery(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doEnableCustomQuery(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=InsertOrRemoveRows"}, method = { RequestMethod.POST })
	public String doInsertOrRemoveRows(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
		if (StringUtils.isBlank(forward)) {
			 forward = super.doInsertOrRemoveRows(form, request, response);
		}
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=toggleValue" }, method = {
			RequestMethod.POST })
	public String doToggleValue(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
		
			forward = super.doToggleValue(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=SaveCustomQuery" }, method = {
			RequestMethod.POST })
	public String doSaveCustomQuery(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
		
			 forward = super.doSaveCustomQuery(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=CustomQueryFilter" }, method = {
			RequestMethod.POST })
	public String doCustomQueryFilter(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}

			forward = super.doCustomQueryFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=customQueryEvents" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doCustomQueryEvents(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
		
			forward = super.doCustomQueryEvents(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = {"action=EditCriteria"}, method = {
			RequestMethod.POST})
	public String doEditCriteria(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doEditCriteria(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=advanceFilter" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doAdvanceFilter(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doAdvanceFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = {"action=InnerFilterOption"}, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doInnerFilterOption(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doInnerFilterOption(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=filterFundIds" }, method = {
			RequestMethod.POST})
	public String doFilterFundIds(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doFilterFundIds(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = {  "action=FilterFundIds" }, method = {
			RequestMethod.POST})
	public String doFilterFundIds1(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doFilterFundIds(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=SaveFundList"}, method = {
			RequestMethod.POST })
	public String doSaveFundList(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			 forward = super.doSaveFundList(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=sortForSloshBoxes" }, method = {
			RequestMethod.POST, RequestMethod.GET })
	public String doSortForSloshBoxes(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			 forward = super.doSortForSloshBoxes(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=displayAssetClass" }, method = { RequestMethod.GET })
	public String doDisplayAssetClass(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			 forward = super.doDisplayAssetClass(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = {"action=filter"}, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
		
			forward = super.doFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/" }, params = {
			"action=sort" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			 forward = super.doSort(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}							

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/" }, params = {
			"action=reportsAndDownload" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doReportsAndDownload(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			 forward = super.doReportsAndDownload(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
	}

	@RequestMapping(value = { "/tpaFandp/","/tabs/","/fapFilterAction/" }, params = {"action=changeDropDownList" }, method = { RequestMethod.POST,RequestMethod.GET })
	public String doChangeDropDownList(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doChangeDropDownList(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=storeFapFormInSession" }, method = { RequestMethod.POST, RequestMethod.GET })
	public String doStoreFapFormInSession(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			try {
				populateBaseFilterOptions(form, request);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("continue");
			}
		}
			forward = super.doStoreFapFormInSession(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=downloadCsv" }, method = { RequestMethod.GET })
	public String doDownloadCsv(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		 forward = super.doDownloadCsv(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=downloadCsvAll" }, method = { RequestMethod.GET })
	public String doDownloadCsvAll(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		 forward = super.doDownloadCsvAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=printPdf" }, method = { RequestMethod.GET })
	public String doPrintPdf(@Valid @ModelAttribute("fapForm") FapForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException, ContentException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		 forward = super.doPrintPdf(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	
	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=generateIReport" }, method = { RequestMethod.POST,RequestMethod.GET })
	public String doGenerateIReport(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException , ContentException{
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not available, provided default
			}
		}
		 forward = super.doGenerateIReport(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = {"action=filterFundScorecardMetrics" }, method = { RequestMethod.POST })
	public String doFilterFundScorecardMetrics(@Valid @ModelAttribute("fapForm") FapForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			form.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
				// //available, provided default
			}
		}
		 forward = super.doFilterFundScorecardMetrics(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * Removes the fundsAndPerformance value object from the session. This
	 * method will be triggered when there are contract related error messages.
	 * In this case only the column headers for the report table to should be
	 * displayed
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = {"/tpaFandp/","/tabs/","/fapFilterAction/"}, params = { "action=displayHeaders" }, method = { RequestMethod.POST })
	public String doDisplayHeaders(@Valid @ModelAttribute("fapForm") FapForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("sessionExpired");// if input forward not
														// //available, provided
														// default
			}
		}

		String tabSelected = actionForm.getTabSelected();

		if (StringUtils.isBlank(tabSelected)) {
			tabSelected = FapConstants.FUND_INFORMATION_TAB_ID;
		} else {
			tabSelected = getTabName(actionForm);
		}

		// Remove the value object and the footnotes symbols from the session
		removeAttributesFromSession(request, FapConstants.VO_FUNDS_AND_PERFORMANCE);
		removeAttributesFromSession(request, "symbolsArray");

		synchronized (FapController.class) {
			FapTabUtility.asOfDates = new HashMap<String, Date>();
		}

		FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request
				.getSession().getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

		HashMap<String, List> currentTabColumns = new HashMap<String, List>();
		if (FapConstants.FUNDSCORECARD_TAB_ID.equals(tabSelected)) {
			currentTabColumns = (HashMap<String, List>) FapReportsUtility.getHeaderValueObject(tabSelected,
					FapTabUtility.class, fundScoreCardMetricsSelection);
		} else {
			currentTabColumns = (HashMap<String, List>) FapReportsUtility.getHeaderValueObject(tabSelected,
					FapTabUtility.class, FapConstants.WEB_FORMAT);
		}

		// create the columns info for the tab
		actionForm.setColumnsInfo(currentTabColumns);

		if (FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY.equals(actionForm.getBaseFilterSelect())) {
			actionForm.setContractSearchText(actionForm.getContractSelect());
		}

		return forwards.get(tabSelected);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */

	@Autowired
	private PSValidatorFWTPAFContinue psValidatorFWTPAFContinue;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWTPAFContinue);
	}
}
