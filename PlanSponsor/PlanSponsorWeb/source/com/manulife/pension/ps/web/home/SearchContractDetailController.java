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
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * SearchContractDetailAction ReportAction class This class is used to get the
 * list of contracts
 *
 */
@Controller
@RequestMapping( value = "/home")
@SessionAttributes({"searchContractForm"})

public class SearchContractDetailController extends ReportController {

	@ModelAttribute("searchContractForm")
	public  SearchContractForm populateForm() {
		return new  SearchContractForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/home/searchContractPage.jsp");
		forwards.put("default","/home/searchContractPage.jsp"); 
		forwards.put("sort","/home/searchContractPage.jsp");
		forwards.put("filter","/home/searchContractPage.jsp"); 
		forwards.put("page","/home/searchContractPage.jsp"); 
		forwards.put("print","/home/searchContractPage.jsp");
		forwards.put("defaultRedirect","redirect:/do/home/searchContractPage.jsp");
		}

	
	//select contract report attributes
	private static final String DEFAULT_SORT_FIELD = "contractName";
	private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
	private static final int CONTRACT_PAGE_SIZE = 20;
	private final static String DI_DURATION_24_MONTH = "24";
	private final static String DI_DURATION_6_MONTH = "6";		

	protected String getDefaultSort()
	{
		return DEFAULT_SORT_FIELD;
	}
	protected String getDefaultSortDirection()
	{
		return DEFAULT_SORT_DIRECTION;
	}

	protected int getPageSize(HttpServletRequest request)
	{
		return CONTRACT_PAGE_SIZE;
	}

    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.controller.PsAction#postExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	
	protected void postExecute( SearchContractForm actionForm,HttpServletRequest request,HttpServletResponse response,ServletContext servlet) 
	throws IOException,ServletException, SystemException {
	    super.postExecute( actionForm, request, response);
        UserInfo loginUserInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				getUserProfile(request).getPrincipal());
        request.setAttribute(Constants.USERINFO_KEY, loginUserInfo);
        request.setAttribute(Constants.CONTRACT_MESSAGE_COUNTS,
				MessageCountsDecorate.getMessageCounts(servlet, request));
        request.setAttribute(Constants.USER_MESSAGE_COUNTS,
				MessageCountsDecorate.getTotalMessageCounts(servlet, request));
    }

	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
	{
		UserProfile userProfile = getUserProfile(request);

		SearchContractForm searchContractForm = (SearchContractForm)form;
		request.getSession(false).setAttribute("searchContractForm",searchContractForm);
		if (userProfile.getRole() instanceof InternalUser && request.getParameter("showAll")==null && request.getParameter(TASK_KEY)==null)
		{
			searchContractForm.setRunStoredProc(false);
			return;
		}

		searchContractForm.setRunStoredProc(true);
		// default sort criteria
		criteria.setPageSize(CONTRACT_PAGE_SIZE);

		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, String.valueOf(userProfile
				.getPrincipal().getProfileId()));
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION, Environment
				.getInstance().getDBSiteLocation());

		// Andrew Park: after repackaging, user role must be passed in as String
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE,ConversionUtility.convertToStoredProcRole(userProfile.getRole()));

		criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);
		
		// This is added to fix the problem occurs 
		// when user click back button on the secure homepage.
		userProfile.setCurrentContract(null);		
		
	}
	
	
	@RequestMapping(value ="/searchContractDetail/" , method = {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		ServletContext servlet = request.getSession().getServletContext();
	
		if(bindingResult.hasErrors()){
			
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("defaultRedirect");
	       }
		}

		String forward=super.doDefault( actionForm, request, response);
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
   }
	
	
	@RequestMapping(value ="/searchContractDetail/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get("defaultRedirect");
	       }
		}
		
       String forward=super.doFilter( actionForm, request, response);
       ServletContext servlet = request.getSession().getServletContext();
	   postExecute(actionForm, request, response, servlet);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/searchContractDetail/", params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get("defaultRedirect");
	       }
		}
       String forward=super.doPage( actionForm, request, response);
       ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/searchContractDetail/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get("defaultRedirect");
	       }
		}
       String forward=super.doSort( actionForm, request, response);
       ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	

	@RequestMapping(value ="/searchContractDetail/",
	params={"task=download"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get("defaultRedirect");
	       }
		}
       String forward=super.doDownload( actionForm, request, response);
       ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value ="/searchContractDetail/" ,params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownloadAll (@Valid @ModelAttribute("searchContractForm") SearchContractForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get("defaultRedirect");
	       }
		}
	    String forward=super.doDownloadAll( actionForm, request, response);
	    ServletContext servlet = request.getSession().getServletContext();
		postExecute(actionForm, request, response, servlet);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	

	protected ReportData getReportData(String reportId,
			ReportCriteria reportCriteria,
			HttpServletRequest request) throws SystemException, ReportServiceException
	{
		SearchContractForm searchContractForm = (SearchContractForm)request.getSession(false).getAttribute("searchContractForm");
		if (!searchContractForm.getRunStoredProc())
		{
			ReportData report = new SelectContractReportData(reportCriteria, 0);
			report.setDetails(new ArrayList());
			return report;
		}

		return super.getReportData(reportId, reportCriteria, request);
	}


	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId()
	{
		return SelectContractReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName()
	{
		return SelectContractReportData.REPORT_NAME;
	}

	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request)
	{
		return null;
	}


	/**
	 * This methos is overridden in order to set the
	 * available contracts in the session so nobody can
	 * set another contract that is not available to them.
	 *
	 * @see doCommon() in ReportAction
	 */
	protected String doCommon(BaseReportForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
	
        UserProfile userProfile = getUserProfile(request);
        if (userProfile != null && userProfile.getNumberOfContracts() == 1 && userProfile.getCurrentContract() != null) {
            return Constants.HOMEPAGE_FINDER_FORWARD;
        }
		String forward = super.doCommon( actionForm, request, response);
		ReportData reportData = (ReportData)request.getAttribute(Constants.REPORT_BEAN);
		populateIloanIndicator(reportData);
		request.getSession(false).setAttribute(Constants.AVAILABLE_CONTRACTS_LIST_KEY, reportData.getDetails());

		return forward;
	}
	
	private void populateIloanIndicator(ReportData reportData)
			throws SystemException {
		// Get the contract numbers from the report  data and form an Integer
		// array
		if (reportData != null && reportData.getDetails().size() > 0) {
			Integer[] contractNumbers = new Integer[reportData.getDetails()
					.size()];
			int contractIdTemp = 0;
			Iterator<SelectContract> ContractNumberExtractIterator = reportData
					.getDetails().iterator();
			while (ContractNumberExtractIterator.hasNext()) {
				SelectContract contract = (SelectContract) ContractNumberExtractIterator
						.next();
				int contractNumber = contract.getContractNumber();
				contractNumbers[contractIdTemp] = contractNumber;
				contractIdTemp++;
			}

			// get the loanSettings values for the contract numbers
			Map<Integer, ArrayList<LoanSettings>> loanSettingsMap = LoanServiceDelegate
					.getInstance().getPartialLoanSettingsData(contractNumbers);

			// Compare the contract numbers from report data and loanSettings
			// and
			// populate the OnlineLoanLRKIndicator indicator.
			Iterator<SelectContract> reportDataIterator = reportData
					.getDetails().iterator();
			while (reportDataIterator.hasNext()) {
				SelectContract contract = (SelectContract) reportDataIterator
						.next();
				int contractNumber = contract.getContractNumber();
				if (loanSettingsMap != null && loanSettingsMap.size() > 0) {
					if (loanSettingsMap.containsKey(contractNumber)) {
						ArrayList<LoanSettings> loanSettingsList = (ArrayList<LoanSettings>) loanSettingsMap
								.get(contractNumber);
						LoanSettings loanSettings = (LoanSettings) loanSettingsList
								.get(0);

						if (loanSettings.isLrk01()
								&& !loanSettings.isAllowOnlineLoans()) {
							contract.setOldIloanIndicator(true);
						}

					}
				}

			}

		}
	}
	
	/**
	 * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	  @Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWDefault);
	}
}
