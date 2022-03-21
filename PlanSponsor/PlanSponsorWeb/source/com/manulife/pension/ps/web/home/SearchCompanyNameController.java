package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
@Controller
@RequestMapping(value = "/home")

@SessionAttributes({ "searchCompanyNameForm" })
public class SearchCompanyNameController extends ReportController{
// select contract report attributes
	private static final String DEFAULT_SORT_FIELD = "contractName";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int CONTRACT_PAGE_SIZE = 20;
	public final static String DI_DURATION_24_MONTH = "24";

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {

		forwards.put("home","redirect:/do/home/homePageFinder/");
		forwards.put("contract","redirect:/do/home/searchContract/");
		
		forwards.put("default","/home/searchContractPage.jsp");
		forwards.put("input","/home/searchContractPage.jsp");
		forwards.put("sort","/home/searchContractPage.jsp");
		forwards.put("filter","/home/searchContractPage.jsp");
		forwards.put("page","/home/searchContractPage.jsp");
		forwards.put("print","/home/searchContractPage.jsp");

	}
	

	// From BaseReportAction
	/* Logger */
	private Logger logger = Logger.getLogger(SearchCompanyNameController.class);
	protected static final String TASK_KEY = "task";
	protected static final String DEFAULT_TASK = "default";

	@ModelAttribute("searchCompanyNameForm")
	public SearchCompanyNameForm populateForm() {
		return new SearchCompanyNameForm(); // populates form for the first time if
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsAction#postExecute(org.apache
	 * .struts.action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void postExecute(SearchCompanyNameForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		 super.postExecute(form, request, response);
		UserInfo loginUserInfo = SecurityServiceDelegate.getInstance()
				.getUserInfo(getUserProfile(request).getPrincipal());
		request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
		request.setAttribute(Constants.CONTRACT_MESSAGE_COUNTS,
				MessageCountsDecorate.getMessageCounts(request.getServletContext(), request));
		request.setAttribute(Constants.USER_MESSAGE_COUNTS,
				MessageCountsDecorate.getTotalMessageCounts(request.getServletContext(), request));
	}


	public static UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
		} else {
			return null;
		}
	}
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		SearchContractForm searchContractForm = (SearchContractForm) request.getSession(false)
				.getAttribute("searchContractForm");
		if ("search".equals(request.getParameter("search"))) {
			searchContractForm.clear();
			searchContractForm.setSortDirection(DEFAULT_SORT_DIRECTION);
			searchContractForm.setSortField(DEFAULT_SORT_FIELD);
			searchContractForm.setShowButton(true);
		} else {
			String temp = request.getParameter("pageNumber");
			if (temp != null && temp.length() != 0)
				searchContractForm.setPageNumber(Integer.parseInt(temp));
			else
				searchContractForm.setPageNumber(1);

			temp = request.getParameter("sortField");
			if (temp != null && temp.length() != 0)
				searchContractForm.setSortField(temp);

			temp = request.getParameter("sortDirection");
			if (temp != null && temp.length() != 0)
				searchContractForm.setSortDirection(temp);
		}

		SearchCompanyNameForm myForm =  (SearchCompanyNameForm)form;
		if (myForm.getContractNumber() != null && myForm.getContractNumber().length() != 0) {
			try {
				Integer.parseInt(myForm.getContractNumber());
			} catch (NumberFormatException e) {
				searchContractForm.setRunStoredProc(false);
				return;
			}

			FunctionalLogger.INSTANCE.log("Search Contract", request, myForm.getContractNumber(), getClass(),
					getMethodName(myForm, request));

			criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING,
					"C.CONTRACT_ID=" + myForm.getContractNumber());
		} else if (myForm.getCompanyName() != null && myForm.getCompanyName().length() != 0) {
			if (myForm.getCompanyName().length() < 3) {
				Collection errors = new ArrayList();
				errors.add(new GenericException(2159));
				request.setAttribute(Environment.getInstance().getErrorKey(), errors);
				searchContractForm.setRunStoredProc(false);
				return;
			} else {

				FunctionalLogger.INSTANCE.log("Search Contract", request, myForm.getCompanyName(), getClass(),
						getMethodName(myForm, request));
				criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, "upper(C.CONTRACT_NAME) LIKE '"
						+ sqlizeTheString(myForm.getCompanyName().toUpperCase()) + "%'");
			}
		} else {
			Collection errors = new ArrayList();
			errors.add(new GenericException(2158));
			request.setAttribute(PsBaseUtil.ERROR_KEY, errors);
			searchContractForm.setRunStoredProc(false);
			return;
		}

		searchContractForm.setRunStoredProc(true);

		// default sort criteria
		criteria.setPageSize(CONTRACT_PAGE_SIZE);

		UserProfile userProfile = getUserProfile(request);

		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID,
				String.valueOf(userProfile.getPrincipal().getProfileId()));
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION,
				Environment.getInstance().getDBSiteLocation());

		// Andrew Park: after repackaging user role must be passed in as String
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE,
				ConversionUtility.convertToStoredProcRole(userProfile.getRole()));

		// This is added to fix the problem occurs
		// when user click back button on the secure homepage.
		userProfile.setCurrentContract((Contract)null);
	}

	/**
     * This method returns the appropriate action methods. It turns the first
     * letter of the task name to capital letter and prefixes with "do". The
     * following methods are used for the reports:
     * <ul>
     * <li>doDefault()
     * <li>doFilter()
     * <li>doPage()
     * <li>doSort()
     * <li>doDownload()
     * <li>doPrint()
     * </ul>
     */
    final protected String getMethodName(SearchCompanyNameForm actionForm, HttpServletRequest request) {

        String task = getTask(request);
        String methodName = "do" + task.substring(0, 1).toUpperCase()
                + task.substring(1);
        return methodName;
    }

    /**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }

	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		SearchContractForm searchContractForm = (SearchContractForm) request.getSession(false)
				.getAttribute("searchContractForm");
		if (!searchContractForm.getRunStoredProc()) {
			ReportData report = new SelectContractReportData(reportCriteria, 0);
			report.setDetails(new ArrayList());
			return report;
		}

		return super.getReportData(reportId, reportCriteria, request);
		//return getReportData(reportId, reportCriteria, request);
	}

	protected void populateDownloadData(PrintWriter out, BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {
	}

	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {
		return null;
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

	/**
	 * This methos is overridden in order to set the available contracts in the
	 * session so nobody can set another contract that is not available to them.
	 *
	 * @see doCommon() in ReportAction
	 */
	protected String doCommon(BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		SearchContractForm searchContractForm = (SearchContractForm) request.getSession(false)
				.getAttribute("searchContractForm");
		// if session form is null, user came from bookmark, so send them to
		// home page
		if (null == searchContractForm) {
			return forwards.get("home");
		}

		int reportSize = 0;
		String forward = super.doCommon( reportForm, request, response);
		ReportData reportData = (ReportData) request.getAttribute(Constants.REPORT_BEAN);
		populateIloanIndicator(reportData);
		request.getSession(false).setAttribute(Constants.AVAILABLE_CONTRACTS_LIST_KEY, reportData.getDetails());
		/*
		 * SSE 191 When any PSW user searches for a contract on the Contract Search page
		 * and the application only finds one contract to list on the Contract Search
		 * page the application must automatically invoke the same behavior that would
		 * occur when a user selects a listed contract on the page
		 */
		if (reportData != null && reportData.getDetails() != null) {
			reportSize = reportData.getDetails().size();
			int cn = -1;
			if (reportSize == 1) {
				Iterator it = reportData.getDetails().iterator();
				SelectContract selectContract = (SelectContract) it.next();
				cn = selectContract.getContractNumber();
				if (cn != -1)
					if (null == searchContractForm)
						searchContractForm = new SearchContractForm();

				searchContractForm.setContractNumber(String.valueOf(cn));
				request.getSession(false).setAttribute("searchContractForm", searchContractForm);
				return forwards.get("contract");
				
			}
		}
		return forward;
	}

	protected BaseReportForm resetForm(BaseReportForm reportForm,
			HttpServletRequest request) throws SystemException {
		return reportForm;
	}

	/**
	 * This method is to used to add another ' after ' in order to make the SQL
	 * statement valid
	 *
	 * @param str
	 * @return String
	 */
	private static String sqlizeTheString(String str) {
		StringBuffer temp = new StringBuffer();
		int initialIndex = 0;
		int index = 0;
		while ((index = str.indexOf("'", initialIndex)) != -1) {
			temp.append(str.substring(initialIndex, index + 1)).append("'");
			initialIndex = index + 1;
		}

		if (temp.length() == 0)
			temp.append(str);

		return temp.toString();
	}

	/**
	 * This method gets the contractIds and pass the contract ids to get the
	 * Loansettings object which is use to check the LRK01 and allow online CSF
	 * values to display "*" (indicates iloans) in the search contract page.
	 * 
	 * @param reportData
	 * @throws SystemException
	 */
	private void populateIloanIndicator(ReportData reportData) throws SystemException {
		// Get the contract numbers from the report data and form an Integer
		// array
		if (reportData != null && reportData.getDetails().size() > 0) {
			Integer[] contractNumbers = new Integer[reportData.getDetails().size()];
			int contractIdTemp = 0;
			Iterator<SelectContract> ContractNumberExtractIterator = reportData.getDetails().iterator();
			while (ContractNumberExtractIterator.hasNext()) {
				SelectContract contract = (SelectContract) ContractNumberExtractIterator.next();
				int contractNumber = contract.getContractNumber();
				contractNumbers[contractIdTemp] = contractNumber;
				contractIdTemp++;
			}

			// get the loanSettings values for the contract numbers
			Map<Integer, ArrayList<LoanSettings>> loanSettingsMap = LoanServiceDelegate.getInstance()
					.getPartialLoanSettingsData(contractNumbers);

			// Compare the contract numbers from report data and loanSettings
			// and
			// populate the OnlineLoanLRKIndicator indicator.
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
	}

	/*
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the
	 * CL#137697.
	 * 
	
	 */
	
	@Autowired
	PSValidatorFWInput psValidatorFWInput;
	
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
	
	@RequestMapping(value="/searchCompanyName/",method =  RequestMethod.GET) 
	public String doDefault (@Valid @ModelAttribute("searchCompanyNameForm") SearchCompanyNameForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("home");//if input forward not //available, provided default
	       }
		}
	    String forward=doCommon( actionForm, request, response);
	    postExecute(actionForm, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	
}