package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalReportData;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalRequestItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWWithdrawalInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;


@Controller
@RequestMapping( value = {"/withdrawal"})
@SessionAttributes({"searchWithdrawalRequestForm","loanAndWithdrawalRequestsForm"})

public class SearchParticipantRequestWithdrawalController extends BaseWithdrawalReportController {
	@ModelAttribute("searchWithdrawalRequestForm") 
	public SearchParticipantRequestWithdrawalForm populateForm() 
	{
		return new SearchParticipantRequestWithdrawalForm();
		}
	@ModelAttribute("loanAndWithdrawalRequestsForm") 
	public LoanAndWithdrawalRequestsForm populateForm1() 
	{
		return new LoanAndWithdrawalRequestsForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/withdrawal/searchSummary.jsp");
		forwards.put("default","/withdrawal/searchSummary");
		forwards.put("search","/withdrawal/searchSummary.jsp");
		forwards.put("sort","/withdrawal/searchSummary.jsp");
		forwards.put("page","/withdrawal/searchSummary.jsp");
		forwards.put("filter","/withdrawal/searchSummary.jsp");
		forwards.put("cancel","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
		forwards.put("fromSession","/withdrawal/searchSummary.jsp" );
		forwards.put("noPermission","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
			}

    private static final Logger logger = Logger
            .getLogger(SearchParticipantRequestWithdrawalController.class);

    public static final String ACTION_FORWARD_CANCEL = "cancel";

    public static final String ACTION_FORWARD_PARTICIPANT_SEARCH = "participantSearch";
    
    public static final String ACTION_FORWARD_LOAN_PARTICIPANT_SEARCH = "loanParticipantSearch";

    public static final String ACTION_FORWARD_FROM_SESSION = "fromSession";

    private static final String DEFAULT_SORT_FIELD = SearchParticipantWithdrawalReportData.SORT_PARTICIPANT_NAME;

    private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

    private static final String SESSION_FORM = "searchParticipantReportForm";

    @RequestMapping(value = "/searchSummary/", method = {  RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
 
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	    populateReportForm( form, request);
	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
	                    null, 0);
	            request.setAttribute(Constants.REPORT_BEAN, reportData);
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	form.setFilterParticipantLastName("");
		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doDefault> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        String forward =null;
        Boolean hasAccess = false;

        final SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
                null, 0);
        request.setAttribute(Constants.REPORT_BEAN, reportData);
        final UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        
      		 forward = forwards.get("input");
			hasAccess = true;
			form.setFilterContractId("");
			form.setCsfType("");        
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doDefault> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        if(hasAccess){// if the user has access to the page (applicable for both loan and withdrawal)
        	return forward;
        }
        else{//if the user not having access to the page. 
         return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
        }
    
    }

   
    
    public String doCommon(final BaseReportForm reportForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws SystemException { 
    	
        // We now need to check if the user has access to this page. It's basically the same check
        // that is done for the link to this page (from the list page).
        boolean hasAccess = false;
        boolean isReportDataEmpty = false;       
        final UserProfile userProfile = getUserProfile(request);        
        final Contract currentContract = userProfile.getCurrentContract();
        final SearchParticipantRequestWithdrawalForm theForm = (SearchParticipantRequestWithdrawalForm) reportForm;
        if (currentContract != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doCommon> Handling user with selected contract [")
                        .append(currentContract).append("].").toString());
            }
            final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance()
                    .getContractInfo(userProfile.getCurrentContract().getContractNumber(),
                            userProfile.getPrincipal());
            WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doCommon> Loaded contract information [").append(
                        contractInfo).append(")]").toString());
            }
      
				hasAccess = (contractInfo.getShowCreateWithdrawalRequestLink() && contractInfo
						.getCsfAllowOnlineWithdrawals());

			if (logger.isDebugEnabled()) {
				logger.debug(new StringBuffer("doCommon> User has access [")
						.append(hasAccess).append(
								"] with show withdrawal request link [")
						.append(
								contractInfo
										.getShowCreateWithdrawalRequestLink())
						.append("] and CSF Online Withdrawals [").append(
								contractInfo.getCsfAllowOnlineWithdrawals())
						.append("]").toString());
			}

		} else {
			if (logger.isDebugEnabled()) {
				logger
						.debug("doCommon> Handling TPA without selected contract.");
			} // fi

			final Principal principal = userProfile.getPrincipal();
			 				final PermissionType permissionType = PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE;

				boolean tpaFirmHasInitiatePermission = SecurityHelper
						.checkForAnyTpaFirmWithPermission(principal,
								permissionType);

				hasAccess = (tpaFirmHasInitiatePermission);

				logger.debug(new StringBuffer("preExecute> TPA has access [")
						.append(hasAccess).append(
								"] with initiate permission [").append(
								tpaFirmHasInitiatePermission).append("]")
						.toString());
		}

		if (!hasAccess) {
			return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
		} // fi

        String forward = super.doCommon( theForm, request, response);

        

        final SearchParticipantRequestWithdrawalForm sessionForm = (SearchParticipantRequestWithdrawalForm) request
                .getSession().getAttribute(SESSION_FORM);
        request.getSession().setAttribute(SESSION_FORM, theForm);

        if(isReportDataEmpty){
        	SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
                    null, 0);
            request.setAttribute(Constants.REPORT_BEAN, reportData);
        }
        else{
        final SearchParticipantWithdrawalReportData reportData = (SearchParticipantWithdrawalReportData) request
                .getAttribute(Constants.REPORT_BEAN);
        

        // Need to display the selected contract number and name - grab from first line of data
        	if (CollectionUtils.isNotEmpty(reportData.getDetails())) {
        		final SearchParticipantWithdrawalRequestItem item = (SearchParticipantWithdrawalRequestItem) reportData
        		.getDetails().iterator().next();
        		theForm.setSelectedContractId(item.getContractNumber());
        		theForm.setSelectedContractName(item.getContractName());
        	}
        }
        SearchParticipantRequestWithdrawalForm participantForm = (SearchParticipantRequestWithdrawalForm) theForm;
        participantForm.setCsfType("");
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
     */
    protected String getDefaultSort() {
        return DEFAULT_SORT_FIELD;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
     */
    protected String getDefaultSortDirection() {
        return DEFAULT_SORT_DIRECTION;
    }

    /**
     * Not implemented.
     * 
     * @see ReportController#getDownloadData(BaseReportForm, ReportData, HttpServletRequest)
     */
    protected byte[] getDownloadData(final BaseReportForm reportForm, final ReportData report,
            final HttpServletRequest request) throws SystemException {
        return "Download not implemented.".getBytes();
    }

    protected String getReportId() {
        return SearchParticipantWithdrawalReportData.REPORT_ID;
    }

    protected String getReportName() {
        return SearchParticipantWithdrawalReportData.REPORT_NAME;
    }

    /**
     * This method populates a default form when the report page is first brought up. This method is
     * called before populateReportCriteria() to allow default sort and other criteria to be set
     * properly.
     * 
     * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(ActionMapping,
     *      com.manulife.pension.ps.web.report.BaseReportForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateReportForm( BaseReportForm reportForm,
            HttpServletRequest request) {

        super.populateReportForm( reportForm, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ReportData getReportData(final String reportId, final ReportCriteria reportCriteria,
            final HttpServletRequest request) throws SystemException, ReportServiceException {

        logger.debug("getReportData> Entry.");
        // Get the user profile object from the request.
        final UserProfile userProfile = getUserProfile(request);
        final Principal principal = userProfile.getPrincipal();

        if (userProfile.getPrincipal().getRole().isTPA()) {
            final Integer contractId = (Integer) reportCriteria
                    .getFilterValue(SearchParticipantWithdrawalReportData.FILTER_CONTRACT_ID);
            // If missing the permission, show no results.
            if (!SecurityHelper.checkTpaPermissionForContract(principal, principal.getRole(),
                    contractId, PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE)
                    && !SecurityHelper.checkTpaPermissionForContract(principal, principal.getRole(),
                            contractId, PermissionType.INITIATE_LOANS)) {
                return new SearchParticipantWithdrawalReportData(reportCriteria, 0);
            } // fi
        } // fi

        final ReportData reportData = super.getReportData(reportId, reportCriteria, request);

        logger.debug("getReportData> Exit.");
        return reportData;
    }

    /**
     * This method is called to populate a report criteria from the report action form and the
     * request. It is called right before getReportData is called.
     */
    protected void populateReportCriteria(final ReportCriteria criteria,
            final BaseReportForm form, HttpServletRequest request) throws SystemException {

        final UserProfile userProfile = getUserProfile(request);
        final Contract currentContract = userProfile.getCurrentContract();
        final SearchParticipantRequestWithdrawalForm searchForm = (SearchParticipantRequestWithdrawalForm) form;
        final String contractId = searchForm.getFilterContractId();

        if (StringUtils.isNotBlank(contractId)) {
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_CONTRACT_ID,
                    new Integer(contractId));
        } else {
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_CONTRACT_ID,
                    (currentContract != null ? new Integer(currentContract.getContractNumber())
                            : null));
        }
        final String lastName = searchForm.getFilterParticipantLastName();
        if (StringUtils.isNotBlank(lastName)) {
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_LAST_NAME, lastName
                    .trim());
            searchForm.setFilterParticipantLastName(lastName.trim());
        }

        final Ssn ssn = searchForm.getSsn();
        if (!searchForm.getSsn().isEmpty()) {
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_SSN, ssn.toString());
        }

        criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_SITE_LOCATION, Environment
                .getInstance().getDBSiteLocation());

        if (userProfile.getPrincipal().getRole().isTPA()
        /* && contractId != null && contractId.trim().length() > 0 */) {
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_IS_TPA, new Boolean(
                    true));
            final Integer userProfileId = new Integer((int) getUserProfile(request).getPrincipal()
                    .getProfileId());
            criteria.addFilter(SearchParticipantWithdrawalReportData.FILTER_USER_PROFILE_ID,
                    userProfileId);
        }
    }
    @RequestMapping(value = "/searchSummary/",params= {"task=page"}, method = { RequestMethod.POST })
   	public String doPage(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException { 
    	
       	if(bindingResult.hasErrors()){
   	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   	       if(errDirect!=null){
   	    	   populateReportForm( form, request);
   	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
   	                    null, 0);
   	            request.setAttribute(Constants.REPORT_BEAN, reportData);
   	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
   	       }
   		}
      
           logger.debug("doPage> Entry.");
            String forward=super.doPage(form, request, response);
           logger.debug("doPage> Exit.");
           return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value = "/searchSummary/",params= {"task=sort"}, method = { RequestMethod.POST })
   	public String doSort(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException {
    	
       	if(bindingResult.hasErrors()){
   	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   	       if(errDirect!=null){
   	    	   populateReportForm( form, request);
   	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
   	                    null, 0);
   	            request.setAttribute(Constants.REPORT_BEAN, reportData);
   	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
   	       }
   		}
      
           logger.debug("doSort> Entry.");
           String forward=super.doSort(form, request, response);
           logger.debug("doSort> Exit.");
           return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value = "/searchSummary/",params= {"task=filter"}, method = { RequestMethod.POST })
   	public String doFilter(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
   			throws IOException, ServletException, SystemException { 
    	
       	if(bindingResult.hasErrors()){
   	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   	       if(errDirect!=null){
   	    	   populateReportForm( form, request);
   	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
   	                    null, 0);
   	            request.setAttribute(Constants.REPORT_BEAN, reportData);
   	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
   	       }
   		}
      
           logger.debug("doFilter> Entry.");
           
            String forward=super.doFilter(form, request, response);
           logger.debug("doFilter> Exit.");
           return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
       }

    /**
     * doCancel is called when the page 'cancel' button is pressed.
     * 
     * @param mapping The action mapping.
     * @param actionForm The action form.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return ActionForward The forward to process.
     * @throws IOException When an IO problem occurs.
     * @throws ServletException When a Servlet problem occurs.
     * @throws SystemException When a generic application problem occurs.
     */
    
    @RequestMapping(value = "/searchSummary/",params= {"task=cancel"}, method = { RequestMethod.POST, RequestMethod.GET })
	public String doCancel(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   populateReportForm( form, request);
	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
	                    null, 0);
	            request.setAttribute(Constants.REPORT_BEAN, reportData);
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
   
        logger.debug("doCancel> Entry.");
        
         String forward= ACTION_FORWARD_CANCEL;
        logger.debug("doCancel> Exit.");
        form.setFilterParticipantLastName("");
    	form.setFilterContractId("");
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    /*
     * Just a placeholder invoked when coming from the list page to avoid using multiple mappings
     * and InitAction.
     */
        @RequestMapping(value = "/searchSummary/",params= {"task=participantSearch"}, method = { RequestMethod.POST})
    	public String doParticipantSearch(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
    			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
    			throws IOException, ServletException, SystemException { 
        	
        	if(bindingResult.hasErrors()){
        		   populateReportForm( form, request);
   	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
   	                    null, 0);
   	            request.setAttribute(Constants.REPORT_BEAN, reportData);
    	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    	       if(errDirect!=null){
    	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    	       }
    		}
        logger.debug("doParticipantSearch> Entry.");
        String forward = doDefault( form, request, response);
        logger.debug("doParticipantSearch> Exit.");
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    /*
     * Method called when validation fails or the user presses cancel on the BeforeProceeding page.
     */
        @RequestMapping(value = "/searchSummary/",params= {"task=fromSession"}, method = { RequestMethod.GET})
    	public String doFromSession(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
    			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
    			throws IOException, ServletException, SystemException {  
        	
        	if(bindingResult.hasErrors()){
    	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    	       if(errDirect!=null){
    	    	   populateReportForm( form, request);
   	            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
   	                    null, 0);
   	            request.setAttribute(Constants.REPORT_BEAN, reportData);
    	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    	       }
    		}
   
        
        final SearchParticipantRequestWithdrawalForm sessionForm = (SearchParticipantRequestWithdrawalForm) request
                .getSession().getAttribute(SESSION_FORM);
        if (sessionForm != null) {
            form.copyFrom(sessionForm);
            request.getSession().setAttribute(SESSION_FORM, null);
        }
         String forward= doFilter( form, request, response);
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }

    protected String getTask(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        String task = (String) session.getAttribute(TASK_KEY);
        if (task == null) {
            task = super.getTask(request);
        }
        return task;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.ps.web.controller.PsAction#preExecute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected String preExecute( SearchParticipantRequestWithdrawalForm form,
			 HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException { 
    
   
    

        // We now need to check if the user has access to this page. It's basically the same check
        // that is done for the link to this page (from the list page).
        boolean hasAccess = false;
        final UserProfile userProfile = getUserProfile(request);
        final Contract currentContract = userProfile.getCurrentContract();
        
        if (currentContract != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("preExecute> Handling user with selected contract [")
                        .append(currentContract).append("].").toString());
            }
            final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance()
                    .getContractInfo(userProfile.getCurrentContract().getContractNumber(),
                            userProfile.getPrincipal());
            WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("preExecute> Loaded contract information [").append(
                        contractInfo).append(")]").toString());
            }
            	hasAccess = (contractInfo.getShowCreateWithdrawalRequestLink() && contractInfo.getCsfAllowOnlineWithdrawals());                
                   
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer("preExecute> User has access [").append(hasAccess)
                                .append("] with show withdrawal request link [").append(
                                        contractInfo.getShowCreateWithdrawalRequestLink()).append(
                                        "] and CSF Online Withdrawals [").append(
                                        contractInfo.getCsfAllowOnlineWithdrawals()).append("]")
                                .toString());
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("preExecute> Handling TPA without selected contract.");
            } // fi

            final Principal principal = userProfile.getPrincipal();
         
            
            	final PermissionType permissionType = PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE;
            	boolean tpaFirmHasInitiatePermission = SecurityHelper.checkForAnyTpaFirmWithPermission(
            			principal, permissionType);
            	hasAccess = (tpaFirmHasInitiatePermission);
            
            	logger.debug(new StringBuffer("preExecute> TPA has access [").append(hasAccess).append(
                    "] with initiate permission [").append(tpaFirmHasInitiatePermission)
                    .append("]").toString());
            
        } // fi
        if (!hasAccess) {
            return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
        } else {
            return super.preExecute( form, request, response);
        }
    }
    
    /**
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 */
    
    @RequestMapping(value ="/searchSummary/", params= {"action=printPDF"}, method =  {RequestMethod.GET})
	public String doPrintPDF( @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,BindingResult bindingResult,
			HttpServletRequest request,  HttpServletResponse response)
					throws IOException, ServletException, SystemException {
    	
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				   populateReportForm( form, request);
		            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
		                    null, 0);
		            request.setAttribute(Constants.REPORT_BEAN, reportData);
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defualt");//if input forward not //available, provided default
			}
		}
		 String forward=super.doExecute( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
    
    
    @Autowired
	   private PSValidatorFWWithdrawalInput  psValidatorFWInput;
    @Autowired
    private SearchParticipantRequestWithdrawalValidator SearchParticipantRequestWithdrawalValidator;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(SearchParticipantRequestWithdrawalValidator);
	}
}
