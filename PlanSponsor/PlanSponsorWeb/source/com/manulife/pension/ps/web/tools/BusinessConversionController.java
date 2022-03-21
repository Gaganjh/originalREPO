package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.platform.web.validation.rules.ContractNumberRule;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.tools.util.BusinessConversionHelper;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.IntermediaryContact;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;

/**
 * Action for business convertion
 * 
 * @author Steven Wang
 * 
 */
@Controller
@RequestMapping(value ="/tools")
@SessionAttributes({"businessConversionForm"})

public class BusinessConversionController extends PsAutoController {

	@ModelAttribute("businessConversionForm") 
	public BusinessConversionForm populateForm() 
	{
		return new BusinessConversionForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tools/businessConversionStandardHeader.jsp");
		forwards.put("default","/tools/businessConversionNoNavHeader.jsp");
		forwards.put("search","/tools/businessConversionNoNavHeader.jsp");
		forwards.put("save","/tools/businessConversionNoNavHeader.jsp");
		forwards.put("cancel","redirect:/do/home/ChangeContract/");
	}

    public static final String FROM_SEARCH_CONTRACT_PAGE_ACTION = "/do/tools/businessConversionNoNavHeader/";

    public static final String FROM_MANAGER_USER_PAGE_ACTION = "/do/tools/businessConversionStandardHeader/";

    public static final String NO_RECORD_FOUND = "noData";

    public static final String SELECT_ONE = "select one";

    /**
     * Default action just go to business conversion page without data
     */
    @RequestMapping(value ={"/businessConversionStandardHeader/","/businessConversionNoNavHeader/"},  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	
        // clear form content since form bean in session scope
        form.clear( request);
        form.setContractNumber(null);
        // check request come from select contract page or manage user page
        if (request.getParameter("from") != null && request.getParameter("from").length() > 0) {
            String fromPage = request.getParameter("from");
            if (Constants.SEARCH_CONTRACT_PAGE.equalsIgnoreCase(fromPage))
                form.setTargetAction(FROM_SEARCH_CONTRACT_PAGE_ACTION);
            else
                form.setTargetAction(FROM_MANAGER_USER_PAGE_ACTION);
        }
        return forwards.get("default");
    }

    /**
     * Search profiles action
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ={"/businessConversionStandardHeader/","/businessConversionNoNavHeader/"} ,params={"action=search"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSearch (@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward= validate(form, request);
    	if(StringUtils.isNotEmpty(forward))
    	{
    		return forwards.get("search");
    	}
        Collection<GenericException> errors = new ArrayList<GenericException>();
        
        form.clear( request);
        String contractNumber = form.getContractNumber();
        Contract contract = null;
        try {
            contract = getContract(Integer.valueOf(form.getContractNumber()));

            if (isConvertible(contract, errors)) {
                UserProfile loggedInUserProfile = getUserProfile(request);
                Principal loggedInPrincipal = loggedInUserProfile.getPrincipal();
                form.setContractName(contract.getCompanyName());
                Collection<UserInfo> users = getConvertingUsers(contractNumber, loggedInPrincipal);

                boolean isUserProfilesLocked = true;

                for (UserInfo userInfo : users) {
                    long userProfileId = userInfo.getProfileId();

                    if (!LockServiceDelegate.getInstance().lock(LockHelper.USER_PROFILE_LOCK_NAME,
                            LockHelper.USER_PROFILE_LOCK_NAME + userProfileId,
                            loggedInPrincipal.getProfileId())) {
                        try {
                            Lock lockInfo = LockServiceDelegate.getInstance().getLockInfo(
                                    LockHelper.USER_PROFILE_LOCK_NAME,
                                    LockHelper.USER_PROFILE_LOCK_NAME + userProfileId);

                            UserInfo lockOwnerUserInfo = SecurityServiceDelegate.getInstance()
                                    .searchByProfileId(getUserProfile(request).getPrincipal(),
                                            lockInfo.getLockUserProfileId());

                            String lockOwnerDisplayName = LockHelper.getLockOwnerDisplayName(
                                    getUserProfile(request), lockOwnerUserInfo);
                            errors.add(new GenericException(1057,
                                    new String[] { lockOwnerDisplayName }));
                            form.setNoData(true);
                            isUserProfilesLocked = false;
                            break;
                        } catch (SecurityServiceException e) {
                            throw new SystemException(e,
                                    "com.manulife.pension.ps.web.tools.BusinessConversionAction",
                                    "doSearch", "Failed to get user info of lock own. "
                                            + e.toString());
                        }
                    }
                }

                if (isUserProfilesLocked) {
                    form.setUsers(users);
                    List profileList = new ArrayList();
                    populateProfileList(users, profileList);

                    if (profileList.size() == 0) {
                        form.setNoData(true);
                    } else {
                        form.setRelatedProfiles(profileList);
                    }
                }
            } else {
                form.setNoData(true);
            }

        } catch (ContractNotExistException | ReportServiceException se) {
            errors.add(new GenericException(1043));
        }

        if (!errors.isEmpty()) {
            SessionHelper.setErrorsInSession(request, errors);
            form.setNoData(true);
        }

        form.storeClonedForm();
        return forwards.get("search");
    }

    /**
     * Determines whether the given contract is in a valid state to business convert the contract
     * user's security roles, permissions, preferences, and attributes.
     * 
     * @param contract
     * @param errors collection that will contains reasons why a contract is not convertible
     * @return true if a contact can be converted; otherwise false
     */
    private boolean isConvertible(Contract contract, Collection<GenericException> errors) {
        boolean isConvertible = false;

        if (contract == null) {
            errors.add(new GenericException(ErrorCodes.SEARCH_CONTRACT_NO_RESULT));
        } else if (!hasValidConversionContractStatus(contract)) {
            // Error if contract is a invalid BCN14
            errors.add(new GenericException(2323));
        } else if (contract.isBusinessConverted()) {
            // Error if contract is business converted contract BCN15
            errors.add(new GenericException(ErrorCodes.BUSINESS_CONVERTED_CONTRACT));
        } else {
            isConvertible = true;
        }

        return isConvertible;
    }

    /**
     * Save page information
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws SecurityServiceException 
     */
    @RequestMapping(value ={"/businessConversionStandardHeader/","/businessConversionNoNavHeader/"}, params={"action=save"} , method =  {RequestMethod.POST}) 
    public String doSave (@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException, SecurityServiceException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward= validate(form, request);
    	if(StringUtils.isNotEmpty(forward))
    	{
    		return forwards.get("search");
    	}
         int contractNumber = Integer.parseInt(form.getContractNumber());
         // update all profiles with a given contract number
         Collection<UserInfo> users = updateConvertingUsers(form.getUsers(), form
                 .getRelatedProfiles(), contractNumber, request);
         // call service to save business converted users
         SecurityServiceDelegate.getInstance().convertUsers(getUserProfile(request).getPrincipal(),
                 contractNumber, users);

         for (UserInfo userInfo : users) {
             LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
                     LockHelper.USER_PROFILE_LOCK_NAME + userInfo.getProfileId());
         }

         // clear form bean
         form.clear( request);
         form.setContractNumber(null);
         return forwards.get("save");
    	
    	
      
    }

    /**
     * Cancel action on page has profile list
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ={"/businessConversionStandardHeader/","/businessConversionNoNavHeader/"}, params={"action=cancel"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancel (@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	
        Collection users = form.getUsers();

        if (users != null) {
            for (Iterator i = users.iterator(); i.hasNext();) {
                LockServiceDelegate.getInstance().releaseLock(LockHelper.USER_PROFILE_LOCK_NAME,
                        LockHelper.USER_PROFILE_LOCK_NAME + ((UserInfo) i.next()).getProfileId());
            }
        }

        form.clear( request);
        form.setContractNumber(null);
        return forwards.get("cancel");
    }

    /**
     * back action on page has not profile list
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ={"/businessConversionStandardHeader/","/businessConversionNoNavHeader/"}, params={"action=back"}, method =  {RequestMethod.POST}) 
    public String doBack (@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	
        return forwards.get("cancel");
    }
    
    @RequestMapping(value ="/businessConversionStandardHeader/",params={"action=printPDF"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("businessConversionForm") BusinessConversionForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	
	       String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }

    /**
     * Checks whether we're in the right state.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#validate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */
    protected String validate( ActionForm actionForm,
            HttpServletRequest request) {

        BusinessConversionForm form = (BusinessConversionForm) actionForm;
        Collection errors =doValidate( actionForm, request);
        /*
         * Errors are stored in the session so that our REDIRECT can look up the errors.
         */
        if (!errors.isEmpty()) {
            if (form.isSearchAction())
                form.clear( request);
            SessionHelper.setErrorsInSession(request, errors);
            return forwards.get("input");
        }

        return null;
    }

    /**
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */
    protected Collection doValidate( ActionForm actionForm,
            HttpServletRequest request) {
        BusinessConversionForm form = (BusinessConversionForm) actionForm;
        Collection errors = super.doValidate( actionForm, request);
        // search action
        if (form.isSearchAction()) {
            // General contract number rule BCN12, BCN13,BCN14
            ContractNumberRule.getInstance().validate(BusinessConversionForm.FIELD_CONTRACT_NUMBER,
                    errors, form.getContractNumber());

        }
        // save action
        if (form.isSaveAction()) {
            // check contact type
            for (BusinessConvertionItem item : form.getRelatedProfiles()) {
                if (item.getRole().getValue().equals(IntermediaryContact.ID)) {
                    if (SELECT_ONE.equals(item.getContactType())) {
                        String[] params = new String[1];
                        params[0] = item.getUserName();
                        errors.add(new GenericException(2537, params));// testing
                    }
                }
            }
        }
        return errors;
    }

    /**
     * Get contract object base on contract number
     * 
     * @param userRole
     * @param contractNumber
     * @return
     * @throws SystemException
     */
    private Contract getContract(Integer contractNumber) throws ContractNotExistException,
            SystemException {
        Contract contract = null;

        contract = ContractServiceDelegate.getInstance().getContractDetails(
                contractNumber.intValue(), 6);

        if (contract != null && contract.getCompanyName() == null) {
            contract = null;
        }

        return contract;
    }

    /**
     * Determine whether a given contract has a valid status for conversion.
     * 
     * @param contract
     * @return true if the given contract has a valid status for conversion
     */
    private boolean hasValidConversionContractStatus(Contract contract) {
        return (Contract.STATUS_PROPOSAL_SIGNED.equals(contract.getStatus())
                || Contract.STATUS_DETAILS_COMPLETED.equals(contract.getStatus())
                || Contract.STATUS_PENDING_CONTRACT_APPROVAL.equals(contract.getStatus())
                || Contract.STATUS_CONTRACT_APPROVED.equals(contract.getStatus())
                || Contract.STATUS_ACTIVE_CONTRACT.equals(contract.getStatus())
                || Contract.STATUS_INACTIVE_CONTRACT.equals(contract.getStatus()) || Contract.STATUS_CONTRACT_FROZEN
                .equals(contract.getStatus()));
    }

    /**
     * populate profile list base on report data
     * 
     * @param reportData
     * @param profileList
     */
    private void populateProfileList(Collection users, List profileList) {
        for (Iterator i = users.iterator(); i.hasNext();) {
            UserInfo user = (UserInfo) i.next();
            BusinessConvertionItem item = new BusinessConvertionItem();
            item.setUserName(user.getLastName() + "," + user.getFirstName());
            String ssn = user.getSsn();
            if (ssn != null && ssn.length() > 0) {
                item.setSsn(new Ssn(ssn));
            }
            item.setProfileStatus(user.getProfileStatus());
            item.setWebAccess(user.isWebAccessInd());
            item.setPrimaryContact(user.getPrimaryContact());
            item.setMailRecipient(user.getClientMailRecipient());
            item.setTrusteeMailRecipient(user.getTrusteeMailRecipient());
            item.setPptStatementConsultant(user.isStatementRecipient());
            item.setSignReceivedTrustee(user.getSignReceivedTrustee());
            item.setSignReceivedAuthorizedSignor(user.getSignReceivedAuthSigner());
            
            profileList.add(item);
        }

    }

    /**
     * Get all profile that will be converted
     * 
     * @param contractNumber
     * @param principal
     * @return
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    private Collection<UserInfo> getConvertingUsers(String contractNumber, Principal principal)
            throws SystemException, ReportServiceException {
        ReportData reportData = null;
        ReportCriteria criteria = getReportCriteria(getReportId());
        populateReportCriteria(criteria, contractNumber, principal);
        reportData = getReportData(criteria);
        return reportData.getDetails();

    }

    /**
     * Populate report criteria
     * 
     * @param criteria
     * @param contractNumber
     * @param principal
     */
    private void populateReportCriteria(ReportCriteria criteria, String contractNumber,
            Principal principal) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria");
        }
        // set filter is contract number
        String filter = ManageUsersReportData.FILTER_CONTRACT_NUMBER;
        String filterValue = contractNumber;
        criteria.addFilter(ManageUsersReportData.FILTER_FILTER, filter);
        criteria.addFilter(ManageUsersReportData.FILTER_VALUE, filterValue);
        criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL, principal);
        criteria.setSearchPSContacts(true);
        // set page size no limit means on one page
        criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportCriteria");
        }

    }

    /**
     * Call service get a manage user report data
     * 
     * @param reportCriteria
     * @return
     * @throws SystemException
     * @throws ReportServiceException
     */
    private ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }

        ReportServiceDelegate service = ReportServiceDelegate.getInstance();
        ReportData bean = service.getReportData(reportCriteria);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getReportData");
        }

        return bean;
    }

    /**
     * Get manage user report id
     * 
     * @return
     */
    private String getReportId() {
        return ManageUsersReportData.REPORT_ID;
    }

    /**
     * Get a report criteria base on report id
     * 
     * @param reportId
     * @return
     * @throws SystemException
     */
    private ReportCriteria getReportCriteria(String reportId) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportCriteria");
        }
        ReportCriteria criteria = new ReportCriteria(reportId);

        return criteria;
    }

    /**
     * Update all profiles with a given contract base on user selected information from page
     * 
     * @param users
     * @param profileList
     * @param contractNumber
     * @param request
     * @return
     * @throws SystemException
     * @throws SecurityServiceException
     */
    private Collection<UserInfo> updateConvertingUsers(Collection users, List profileList,
            int contractNumber, HttpServletRequest request) throws SystemException,
            SecurityServiceException {
        //
        List<UserInfo> convertingUsers = new ArrayList<UserInfo>();
        Iterator userIterator = users.iterator();
        Iterator profilesIterator = profileList.iterator();
        while (userIterator.hasNext() && profilesIterator.hasNext()) {
            UserInfo reportUser = (UserInfo) userIterator.next();
            // report user did not populate contract permission need search again
            UserInfo user = SecurityServiceDelegate.getInstance().searchByUserName(
                    getUserProfile(request).getPrincipal(), reportUser.getUserName());
            // contract permission before business converted
            ContractPermission oldContract = user.getContractPermission(contractNumber);
            Map oldMap = BusinessConversionHelper.getContractPermissionAsMap(oldContract);
            // user selected new role for profiles
            BusinessConvertionItem item = (BusinessConvertionItem) profilesIterator.next();
            BusinessConversionHelper.populateUserContractPermission(user
                    .getContractPermission(contractNumber), item);
            // Used to set the web access indicator..
            //BusinessConversionHelper.updateUserInfo(user, item);
            // convert new contract to a flat map
            Map newMap = BusinessConversionHelper.getContractPermissionAsMap(user
                    .getContractPermission(contractNumber));
            // get changes
            user.setUpdatedInformation(BusinessConversionHelper.getChanges(newMap, oldMap));
            convertingUsers.add(user);
        }
        return convertingUsers;
    }
    
    @Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}
