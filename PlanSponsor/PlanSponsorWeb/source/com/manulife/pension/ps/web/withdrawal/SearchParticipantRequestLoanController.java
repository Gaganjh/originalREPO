package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ContractDoesNotExistException;
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
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCSF;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
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
@RequestMapping( value = {"/loan"})
@SessionAttributes({"searchWithdrawalRequestForm"})

public class SearchParticipantRequestLoanController extends BaseWithdrawalReportController {
	@ModelAttribute("searchWithdrawalRequestForm") 
	public SearchParticipantRequestWithdrawalForm populateForm() 
	{
		return new SearchParticipantRequestWithdrawalForm();
		}
	public static Map<String,String>forwards=new HashMap<>();
	public static final String INPUT="input";
	public static final String LOAN_SEARCH_SUMMARY = "/loan/searchSummary.jsp";
	static{
		forwards.put(INPUT,LOAN_SEARCH_SUMMARY);
		forwards.put("default",LOAN_SEARCH_SUMMARY);
		forwards.put("search",LOAN_SEARCH_SUMMARY);
		forwards.put("sort",LOAN_SEARCH_SUMMARY);
		forwards.put("page",LOAN_SEARCH_SUMMARY);
		forwards.put("filter",LOAN_SEARCH_SUMMARY);
		forwards.put("cancel","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
		forwards.put("fromSession",LOAN_SEARCH_SUMMARY);
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

    @RequestMapping(value = "/searchSummary/", method = {RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
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
	              return forwards.get(INPUT);
	       }
	       String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
		   return "redirect:"+forward;
		}
    	
    	


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
        //TODO Commenting this code, as we created separate controller for withdrawal.. this is only for Loan search.
        /*if (isLoanPage(mapping)) {*/// loan feature
			if (userProfile.getRole().isInternalUser()) {// Internal users
				final ContractInfo contractInfo = WithdrawalServiceDelegate
						.getInstance().getContractInfo(
								currentContract.getContractNumber(),
								userProfile.getPrincipal());

				// Populate extra contract based permissions dependant upon role and contract:
				WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());
				
				hasAccess = contractInfo.getHasIntitiateLoanPermission();
				forward = forwards.get(INPUT);
			} else if (currentContract != null) {// PSW & TPA & BundledGA users with a
													// contract selected

				LoanSettings loanSettings = LoanServiceDelegate.getInstance()
						.getLoanSettings(currentContract.getContractNumber());
				if (loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
					if (userProfile.getRole().hasPermission(
							PermissionType.INITIATE_LOANS)) {
						hasAccess = true;
					}
					forward =  forwards.get(INPUT);
				}
			} else {// TPA users with TPA version that is no contract selected
				// TODO Mark Eldridge - Bundled GA: Do we need to alter this?
				boolean tpaFirmHasInitiateLoansPermission = SecurityHelper
						.checkForAnyTpaFirmWithPermission(userProfile
								.getPrincipal(), PermissionType.INITIATE_LOANS);
				hasAccess = tpaFirmHasInitiateLoansPermission;
				forward =  forwards.get(INPUT);
			}

			 //TODO Commenting this code, as we created separate controller for withdrawal.. this is only for Loan search.
			form.setCsfType("LOAN");
			form.setFilterContractId("");
			form.setFilterParticipantLastName("");
			
		/*} else {// Withdrawal feature
			forward = forward = forwards.get(INPUT);
			hasAccess = true;
		}*/
        
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
		//super.postExecute(form, request, response);
        if(hasAccess){// if the user has access to the page (applicable for both loan and withdrawal)
        	return forward;
        }
        else{//if the user not having access to the page. 
         return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
        }
    
    }

    public String doCommon( final BaseReportForm reportForm,
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
            // Check for initiate online loan feature
            //TODO Commenting this code, as we created separate controller for withdrawal.. this is only for Loan search.
		/*	if (mapping.getParameter() != null) {*/
				LoanSettings loanSettings = LoanServiceDelegate.getInstance()
						.getLoanSettings(currentContract.getContractNumber());
				if (loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()) {
					hasAccess = userProfile.getRole().hasPermission(PermissionType.INITIATE_LOANS);
				}
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
				final String contractId = StringUtils.trim(theForm
						.getFilterContractId());
				LoanSettings loanSettings = null;
				try {
					loanSettings = LoanServiceDelegate.getInstance()
							.getLoanSettings(Integer.parseInt(contractId));
					if (loanSettings != null) {
						if (!loanSettings.isAllowOnlineLoans()
								&& loanSettings.isLrk01()) {// if allow online loans is no and LRK01 is on
							hasAccess = true;
							SearchParticipantRequestWithdrawalForm participantForm = (SearchParticipantRequestWithdrawalForm) reportForm;	
							// set the warning message
							participantForm.setShowOldILoansLink(true);
							isReportDataEmpty = true;

						}

						// if allow online loans is no and LRK01 is off
						else if (!loanSettings.isAllowOnlineLoans()
								&& !loanSettings.isLrk01()) {
							isReportDataEmpty = true;
							hasAccess = true;
						} else {
							boolean tpaFirmHasInitiateLoansPermission = SecurityHelper
									.checkForAnyTpaFirmWithPermission(
											principal,
											PermissionType.INITIATE_LOANS);
							hasAccess = tpaFirmHasInitiateLoansPermission;
						}
					} else {
						isReportDataEmpty = true;
						hasAccess = true;
					}
				} catch (Exception exception) {
					//if (exception.getClass().getName().equalsIgnoreCase(
					//Object obj = exception.getCause();
					if(exception.getCause()instanceof ContractDoesNotExistException){
					//if (exception.getCause().equals("ContractDoesNotExistException")){							
						isReportDataEmpty = true;
						hasAccess = true;
					}

				}

			}
			// To check initiate withdrawal permission for create withdrawal
			// request

		if (!hasAccess) {
			return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
		} // fi

        String forward = super.doCommon( reportForm, request, response);

        final SearchParticipantRequestWithdrawalForm actionForm = (SearchParticipantRequestWithdrawalForm) reportForm;

        final SearchParticipantRequestWithdrawalForm sessionForm = (SearchParticipantRequestWithdrawalForm) request
                .getSession().getAttribute(SESSION_FORM);
        request.getSession().setAttribute(SESSION_FORM, actionForm);

        // TODO: This seemed to be breaking the search for non-TPA users. For
		// now I've just changed
        // the set bean to a get. Look into if this is the correct way to do it,
		// or not.

        // final SearchParticipantWithdrawalReportData reportData = new
        // SearchParticipantWithdrawalReportData(
        // null, 0);
        // request.setAttribute(Constants.REPORT_BEAN, reportData);
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
        		actionForm.setSelectedContractId(item.getContractNumber());
        		actionForm.setSelectedContractName(item.getContractName());
        	}
        }
        SearchParticipantRequestWithdrawalForm participantForm = (SearchParticipantRequestWithdrawalForm) reportForm;
		participantForm.setCsfType("LOAN");
        return forwards.get(forward);
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
     * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(
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

    @Autowired
    private SearchParticipantRequestWithdrawalValidator SearchParticipantRequestLoanValidator;

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
    
    @RequestMapping(value = "/searchSummary/",params= {"task=cancel"}, method = { RequestMethod.POST})
	public String doCancel(@ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException { 
    	logger.debug("doCancel> Entry.");
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	    request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	    	    return forwards.get(INPUT);
	       }
	       
	       String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	       return "redirect:"+forward;
		}
    	logger.debug("doCancel> Exit.");
    	form.setCsfType("");
    	form.setFilterParticipantLastName("");
    	form.setFilterContractId("");
        return forwards.get(ACTION_FORWARD_CANCEL);
    }
    
    @RequestMapping(value = "/searchSummary/",params= {"task=filter"}, method = {RequestMethod.POST})
	public String doFilter(@Valid@ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException { 
    	logger.debug("doSearch -> Entry.");
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        populateReportForm( form, request);
            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
                    null, 0);
            request.setAttribute(Constants.REPORT_BEAN, reportData);
	       if(errDirect!=null){
	    	    request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	    	    return forwards.get(INPUT);
	        }
	       String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	       return "redirect:"+forward;
		}
    	 String forward = super.doFilter(form, request, response);
    	//super.postExecute(form, request, response);
        
        logger.debug("doSearch -> Exit.");
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    
    @RequestMapping(value = "/searchSummary/",params= {"task=page"}, method = {RequestMethod.POST})
   	public String doPage(@Valid@ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
   			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException { 
       	logger.debug("doPage -> Entry.");
    	
       	if(bindingResult.hasErrors()){
   	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   	        populateReportForm( form, request);
               SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
                       null, 0);
               request.setAttribute(Constants.REPORT_BEAN, reportData);
   	       if(errDirect!=null){
   	    	    request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   	    	    return forwards.get(INPUT);
   	        }
   	       String forwardError="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
   	       return "redirect:"+forwardError;
   		}
      String forward = super.doPage(form, request, response);
       	//super.postExecute(form, request, response);
           
           logger.debug("doPage -> Exit.");
           return forwards.get(forward);
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
     * @see com.manulife.pension.ps.web.controller.PsAction#preExecute(SearchParticipantRequestWithdrawalForm, javax.servlet.http.HttpServletRequest,
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
            //Check for allow online loans
          //TODO Commenting this code, as we created separate controller for withdrawal.. this is only for Loan search.
           /* if(isLoanPage(mapping)){*/
            	LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(currentContract.getContractNumber()) ;        		
            	hasAccess = (loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01());
           /* }
            else{// check for withdrawal permission
            	hasAccess = (contractInfo.getShowCreateWithdrawalRequestLink() && contractInfo.getCsfAllowOnlineWithdrawals());                
            }*/            
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
            //To check initiate loan permission 
           /* if(isLoanPage(mapping)){*/
            	
        		boolean tpaFirmHasInitiateLoansPermission = SecurityHelper.checkForAnyTpaFirmWithPermission(
                principal, PermissionType.INITIATE_LOANS);
        		hasAccess = tpaFirmHasInitiateLoansPermission;
        		
        		logger.debug(new StringBuffer("preExecute> TPA has access [").append(hasAccess).append(
        			"] with initiate permission [").append(tpaFirmHasInitiateLoansPermission)
        			.append("]").toString());
            
            //}
            // To check initiate withdrawal permission 
           /* else
            {
            	final PermissionType permissionType = PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE;
            	boolean tpaFirmHasInitiatePermission = SecurityHelper.checkForAnyTpaFirmWithPermission(
            			principal, permissionType);
            	hasAccess = (tpaFirmHasInitiatePermission);
            
            	logger.debug(new StringBuffer("preExecute> TPA has access [").append(hasAccess).append(
                    "] with initiate permission [").append(tpaFirmHasInitiatePermission)
                    .append("]").toString());
            }*/
        } // fi
        if (!hasAccess) {
            return forwards.get(BaseWithdrawalController.ACTION_FORWARD_NO_PERMISSION);
        } else {
            return super.preExecute( form, request, response);
        }
    }
    
    @RequestMapping(value = "/searchSummary/",params= {"task=fromSession"}, method = {RequestMethod.GET})
	public String doSearchform(@Valid@ModelAttribute("searchWithdrawalRequestForm") SearchParticipantRequestWithdrawalForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws SystemException, ServletException, IOException { 
    	logger.debug("doSearch -> Entry.");
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        populateReportForm( form, request);
            SearchParticipantWithdrawalReportData reportData = new SearchParticipantWithdrawalReportData(
                    null, 0);
            request.setAttribute(Constants.REPORT_BEAN, reportData);
	       if(errDirect!=null){
	    	    request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	    	    return forwards.get(INPUT);
	        }
	       String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
	       return "redirect:"+forward;
		}
    	 final SearchParticipantRequestWithdrawalForm sessionForm = (SearchParticipantRequestWithdrawalForm) request
                 .getSession().getAttribute(SESSION_FORM);
         if (sessionForm != null) {
             form.copyFrom(sessionForm);
             request.getSession().setAttribute(SESSION_FORM, null);
         }
    	 String forward = super.doFilter(form, request, response);
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
  
    @Autowired
	   private PSValidatorFWCSF  psValidatorFWInput;

    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(SearchParticipantRequestLoanValidator);
	}
}
