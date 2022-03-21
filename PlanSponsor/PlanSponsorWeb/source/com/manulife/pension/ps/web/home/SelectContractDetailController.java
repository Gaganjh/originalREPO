package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * SelectContractDetailAction ReportAction class This class is used to get the
 * list of contracts
 *
 * @author Cornelia Diaconescu
 */
@Controller
@RequestMapping(value = "/home")
@SessionAttributes({ "selectContractForm" })

public class SelectContractDetailController extends ReportController {

	@ModelAttribute("selectContractForm")
	public SelectContractForm populateForm() {
		return new SelectContractForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/home/selectContractPage.jsp");
		forwards.put("default", "/home/selectContractPage.jsp");
		forwards.put("sort", "/home/selectContractPage.jsp");
		forwards.put("filter", "/home/selectContractPage.jsp");
		forwards.put("page", "/home/selectContractPage.jsp");
		forwards.put("print", "/home/selectContractPage.jsp");
		forwards.put("homeRedirect", "redirect:/do/home/selectContractPage.jsp");
	}

	// select contract report attributes
	private static final String DEFAULT_SORT_FIELD = "contractName";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int CONTRACT_PAGE_SIZE = 20;

	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected int getPageSize(HttpServletRequest request) {
		return CONTRACT_PAGE_SIZE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ps.web.controller.PsAction#postExecute(org.apache
	 * .struts.action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */

	protected void postExecute(SelectContractForm actionForm, HttpServletRequest request, HttpServletResponse response,
			ServletContext servlet) throws IOException, ServletException, SystemException {

		super.postExecute(actionForm, request, response);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		request.setAttribute(Constants.CONTRACT_MESSAGE_COUNTS,
				MessageCountsDecorate.getMessageCounts(servlet, request));
		request.setAttribute(Constants.USER_MESSAGE_COUNTS,
				MessageCountsDecorate.getTotalMessageCounts(servlet, request));
	}

	@RequestMapping(value = "/selectContractDetail/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
			}
		}
		ServletContext servlet = request.getSession().getServletContext();
		String forward = super.doDefault(actionForm, request, response);
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/selectContractDetail/", params = { "task=filter" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doFilter(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
				
			}
		}

		String forward = super.doFilter(actionForm, request, response);
		ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/selectContractDetail/", params = { "task=page" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
				
			}
		}

		String forward = super.doPage(actionForm, request, response);
		ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/selectContractDetail/", params = { "task=sort" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
				
			}
		}

		String forward = super.doSort(actionForm, request, response);
		ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/selectContractDetail/", params = { "task=download" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
				
			}
		}
		String forward = super.doDownload(actionForm, request, response);
		ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/selectContractDetail/", params = { "task=downloadAll" }, method = { RequestMethod.POST,
			RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("selectContractForm") SelectContractForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon((BaseReportForm) actionForm, request, null);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("homeRedirect");
				
			}
		}
		String forward = super.doDownloadAll(actionForm, request, response);
		ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request) {

		// default sort criteria
		criteria.setPageSize(CONTRACT_PAGE_SIZE);
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID,
				String.valueOf(userProfile.getPrincipal().getProfileId()));
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION,
				Environment.getInstance().getDBSiteLocation());

		// Andrew Park: after repackaging, user role must be passed in as String
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE,
				ConversionUtility.convertToStoredProcRole(userProfile.getRole()));

		// TO DO What to add here for the searchString
		criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);

		// This is added to fix the problem occurs
		// when user click back button on the secure homepage.
		userProfile.setCurrentContract(null);
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return SelectContractReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return SelectContractReportData.REPORT_NAME;
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {

		/*
		 * String COMMA = ","; String DOWNLOAD_COLUMN_HEADING =
		 * "Contract Name, Contract Number";
		 * 
		 * StringBuffer buffer = new StringBuffer();
		 * buffer.append(DOWNLOAD_COLUMN_HEADING);
		 * 
		 * Iterator iterator = report.getDetails().iterator(); while
		 * (iterator.hasNext()) { SelectContract theItem = (SelectContract)
		 * iterator.next();
		 * buffer.append(theItem.getContractName()).append(COMMA);
		 * buffer.append(theItem.getContractNumber()).append(COMMA);
		 * buffer.delete(0, buffer.length() - 1); } return
		 * buffer.toString().getBytes();
		 */

		throw new UnsupportedOperationException();

	}

	/**
	 * This methos is overridden in order to set the available contracts in the
	 * session so nobody can set another contract that is not available to them.
	 *
	 * @see doCommon() in ReportAction
	 */

	protected String doCommon(BaseReportForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws SystemException {

		UserProfile userProfile = getUserProfile(request);
		if (userProfile != null && userProfile.getNumberOfContracts() == 1
				&& userProfile.getCurrentContract() != null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		String forward = super.doCommon(actionForm, request, response);
		ReportData reportData = (ReportData) request.getAttribute(Constants.REPORT_BEAN);
		populateIloanIndicator(reportData);

		request.getSession(false).setAttribute(Constants.AVAILABLE_CONTRACTS_LIST_KEY, reportData.getDetails());

		return forward;
	}

	/**
	 * This method gets the contractIds and pass the contract ids to get the
	 * Loansettings object which is use to check the LRK01 and allow online CSF
	 * values to display "*" (indicates iloans) in the select contract page.
	 * 
	 * @param reportData
	 * @throws SystemException
	 */
	private void populateIloanIndicator(ReportData reportData) throws SystemException {
		// Get the contract numbers from the report data and form an Integer
		// array
		Integer[] contractNumbers = new Integer[reportData.getDetails().size()];
		int contractIdTemp = 0;
		Iterator<SelectContract> ContractNumberExtractIterator = reportData.getDetails().iterator();
		while (ContractNumberExtractIterator.hasNext()) {
			SelectContract contract = (SelectContract) ContractNumberExtractIterator.next();
			int contractNumber = contract.getContractNumber();
			contractNumbers[contractIdTemp] = contractNumber;
			contractIdTemp++;
		}

		// get the Loansetting values for the contract numbers
		Map<Integer, ArrayList<LoanSettings>> loanSettingsMap = LoanServiceDelegate.getInstance()
				.getPartialLoanSettingsData(contractNumbers);

		// Compare the contract numbers from report data and loansettings and
		// populate the loanlrk indicator.
		Iterator<SelectContract> reportDataIterator = reportData.getDetails().iterator();
		while (reportDataIterator.hasNext()) {
			SelectContract contract = (SelectContract) reportDataIterator.next();
			int contractNumber = contract.getContractNumber();
			if (loanSettingsMap != null && loanSettingsMap.size() > 0) {
				if (loanSettingsMap.containsKey(contractNumber)) {
					ArrayList<LoanSettings> loanSettingsList = (ArrayList<LoanSettings>) loanSettingsMap
							.get(contractNumber);
					LoanSettings loanSettings = (LoanSettings) loanSettingsList.get(0);

					if (loanSettings.isLrk01() && !loanSettings.isAllowOnlineLoans()) {
						contract.setOldIloanIndicator(true);
					}

				}
			}

		}

	}

	// This code has been changed and added to Validate form and
	// request against penetration attack, prior to other validations as part of
	// the CL#137697.
	/*
	 * @SuppressWarnings("rawtypes") public Collection doValidate( Form form,
	 * HttpServletRequest request) { Collection penErrors =
	 * PsValidation.doValidatePenTestAutoAction(form, mapping, request,
	 * CommonConstants.INPUT); if (penErrors != null && penErrors.size() > 0) {
	 * request.removeAttribute(PsBaseAction.ERROR_KEY); try { doCommon(
	 * (BaseReportForm)form, request, null); } catch (SystemException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } return penErrors;
	 * } return super.doValidate( form, request); }
	 */

	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
