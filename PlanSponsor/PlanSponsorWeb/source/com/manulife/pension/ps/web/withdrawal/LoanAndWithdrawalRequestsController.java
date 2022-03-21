package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.SubmissionServiceDelegate;
import com.manulife.pension.ps.web.tools.util.LockManager;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.BundledGaApprover;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.converter.ConverterHelper;

/**
 * LoanAndWithdrawalRequestsAction is the action class for the loan and withdrawal requests page.
 * 
 * @author Paul_Glenn
 */
public class LoanAndWithdrawalRequestsController extends BaseWithdrawalReportController {

	private static final Logger logger = Logger.getLogger(LoanAndWithdrawalRequestsController.class);
	
    private static final String DEFAULT_SORT_FIELD = LoanAndWithdrawalReportData.SORT_REQUEST_DATE;

    private static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;

    private static final String REQUEST_STATUS_ALL = "14, W1, W3, W5, W6, W7, L1, L2, L3, L4, L6, L8, L9, LB, 99";    
    
    private static final String DEFAULT_LOAN_REQUEST_STATUS = "14, L1, L3";
    
    private static final String DEFAULT_WITHDRAWAL_REQUEST_STATUS =  "14, W5, W6";
    
    private static final String DEFAULT_LOANAND_WITHDRAWAL_REQUEST_STATUS =  "14, W5, W6:14, L1, L3";    
    
    private static final String LOAN =  "L";
    
    private static final String WITHDRAWAL =  "W";
    
    private static final String LOAN_AND_WITHDRAWAL =  "WL";

    private static final String LOCK_ERROR_MAPPING = "lockError";

    private static final String VIEW_ITEM_MAPPING = "viewItem";

    private static final String EDIT_ITEM_MAPPING = "editItem";

    private static final String REVIEW_ITEM_MAPPING = "reviewItem";

    private static final String CLASS_NAME = LoanAndWithdrawalRequestsController.class.getName();
    
    private static final String LOANS_ONLY = "loanOnly";

	private static final String WITHDRAWAL_ONLY = "withdrawalOnly";

	private static final String BOTH_LOANS_AND_WITHDRAWAL = "loanAndWithdrawals";

	private static final String REQ_TPA_PERMISSION_MAP = "tpaPermissionMap";
	
    private static final String REQ_CONTRACT_INFO_MAP = "contractInfoMap";
    
    private static final String REQ_USER_ROLE_MAP = "userRoleMap";
    

    
    /**
     * Checks if any TPA firm has the given permission.
     * 
     * @param request
     * @param principal
     * @param permissionType
     * @return
     * @throws SystemException
     */
    private boolean checkForAnyTpaFirmWithPermission(HttpServletRequest request,
			Principal principal, PermissionType permissionType)
			throws SystemException {

		Map<String, Boolean> tpaPermissionMap = (Map<String, Boolean>) request
				.getAttribute(REQ_TPA_PERMISSION_MAP);
		if (tpaPermissionMap == null) {
			tpaPermissionMap = new HashMap<String, Boolean>();
			request.setAttribute(REQ_TPA_PERMISSION_MAP, tpaPermissionMap);
		}

		String permissionCode = PermissionType
				.getPermissionCode(permissionType);
		boolean tpaFirmHasPermission = false;

		if (!tpaPermissionMap.containsKey(permissionCode)) {
			tpaFirmHasPermission = SecurityHelper
					.checkForAnyTpaFirmWithPermission(principal, permissionType);
			tpaPermissionMap.put(permissionCode, tpaFirmHasPermission);
		}

		tpaFirmHasPermission = tpaPermissionMap.get(permissionCode);

		return tpaFirmHasPermission;
	}
    
    /**
     * Retrieves the user role for the given principal for the given contract.
     * 
     * @param request
     * @param principal
     * @param contractId
     * @return
     * @throws SystemException
     */
    private UserRole getUserRoleForContract(HttpServletRequest request,
			Principal principal, Integer contractId) throws SystemException {
		Map<Integer, UserRole> userRoleMap = (Map<Integer, UserRole>) request
				.getAttribute(REQ_USER_ROLE_MAP);
		if (userRoleMap == null) {
			userRoleMap = new HashMap<Integer, UserRole>();
			request.setAttribute(REQ_USER_ROLE_MAP, userRoleMap);
		}

		UserRole tempUserRole = userRoleMap.get(contractId);
		if (tempUserRole == null) {
			UserRole userRole = principal.getRole();
      		tempUserRole = SecurityHelper.getUserRoleForContract(principal, userRole, contractId);
			userRoleMap.put(contractId, tempUserRole);
		}
		return tempUserRole;
	}
    
    /**
	 * Retrieves the contract information for the given contract, populated with
	 * the proper permission.
	 * 
	 * @param request
	 * @param contractId
	 * @param principal
	 * @return
	 * @throws SystemException
	 */
	private ContractInfo getContractInfo(HttpServletRequest request,
			Integer contractId, Principal principal) throws SystemException {

		Map<Integer, ContractInfo> contractInfoMap = (Map<Integer, ContractInfo>) request
				.getAttribute(REQ_CONTRACT_INFO_MAP);
		if (contractInfoMap == null) {
			contractInfoMap = new HashMap<Integer, ContractInfo>();
			request.setAttribute(REQ_CONTRACT_INFO_MAP, contractInfoMap);
		}

		ContractInfo contractInfo = contractInfoMap.get(contractId);
		if (contractInfo == null) {
			contractInfo = WithdrawalServiceDelegate.getInstance()
					.getContractInfo(contractId, principal);
			UserRole userRole = getUserRoleForContract(request, principal,
					contractId);
			WithdrawalRequestUi.populatePermissions(contractInfo, userRole);
			contractInfoMap.put(contractId, contractInfo);
		}
		return contractInfo;
	}

    /**
     * This method is called to populate a report criteria from the report action form and the
     * request. It is called right before getReportData is called.
     */
    protected void populateReportCriteria(final ReportCriteria criteria,
            final BaseReportForm form, final HttpServletRequest request) throws SystemException {

        // get the user profile object
        final UserProfile userProfile = getUserProfile(request);

        final LoanAndWithdrawalRequestsForm loanAndWithdrawalForm = (LoanAndWithdrawalRequestsForm) form;

        final String contractNumber = StringUtils.trim(loanAndWithdrawalForm
                .getFilterContractNumber());

        criteria
                .addFilter(LoanAndWithdrawalReportData.FILTER_PRINCIPAL, userProfile.getPrincipal());

        if (StringUtils.isNotBlank(contractNumber)) {
            logger.info(new StringBuffer(
                    "populateReportCriteria> Adding contract number filter for [").append(
                    contractNumber).append("]").toString());
            criteria.addFilter(LoanAndWithdrawalReportData.FILTER_CONTRACT_NUMBER, new Integer(
                    contractNumber.trim()));
        }

        final List<Integer> searchableContracts = getSearchableContracts(userProfile);
        if (logger.isInfoEnabled()) {
            logger.info(new StringBuffer("populateReportCriteria> Found searchable contracts [")
                    .append(searchableContracts).append("]").toString());
        } // fi

        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_SEARCHABLE_CONTRACTS,
                searchableContracts);

        String fromDateStr = loanAndWithdrawalForm.getFilterFromDate();
        String toDateStr = loanAndWithdrawalForm.getFilterToDate();
        Date fromDate = null;
        Date toDate = null;
        final DateFormat dateFormat = ConverterHelper.getDefaultDateFormat();

        if (StringUtils.isNotBlank(fromDateStr)) {
            try {
                fromDate = dateFormat.parse(fromDateStr);
            } catch (ParseException e) {
                throw new SystemException("Invalid fromDate");
            }
        }
        if (StringUtils.isNotBlank(toDateStr)) {
            try {
                toDate = dateFormat.parse(toDateStr);
            } catch (ParseException e) {
                throw new SystemException("Invalid toDate");
            }
        }
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_FROM_DATE, fromDate);
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TO_DATE, toDate);

        final String requestReason = loanAndWithdrawalForm.getFilterRequestReason();
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_REASON,
                (requestReason != null && !requestReason.equalsIgnoreCase("-1") && !requestReason.equalsIgnoreCase(LOAN)) ? "'"
                        + requestReason + "'" : null);
        String requestType = WithdrawalWebUtil.getTypeOfRequest(userProfile);
        if (requestReason != null && !requestReason.equals("") && !requestReason.equalsIgnoreCase("-1")){
        	if (requestReason.equalsIgnoreCase(LOAN)){
        		criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,LOAN);
        	} else {
        		criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,WITHDRAWAL);
        	}
        } else {
        	//get the request type and populate the criteria.
        	
        	if(LOANS_ONLY.equals(requestType)){
        		criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,LOAN);
        	}
        	else if(WITHDRAWAL_ONLY.equals(requestType)){
        		criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,WITHDRAWAL);
        	}
        	else{
        		criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE,LOAN_AND_WITHDRAWAL);
        	}
        }
        //set the default request status values based on loan only /withdrawal only /loan and withdrawal scenario
        if (loanAndWithdrawalForm.getFilterRequestStatus() == null) {
			if (requestType.equals(WithdrawalWebUtil.LOANANDWITHDRAWAL)) {
				loanAndWithdrawalForm
						.setFilterRequestStatus(DEFAULT_LOANAND_WITHDRAWAL_REQUEST_STATUS);
			} else if (requestType.equals(WithdrawalWebUtil.WITHDRAWAL_ONLY)) {
				loanAndWithdrawalForm.setFilterRequestStatus(DEFAULT_WITHDRAWAL_REQUEST_STATUS);
			} else {
				loanAndWithdrawalForm.setFilterRequestStatus(DEFAULT_LOAN_REQUEST_STATUS);
			}
		}
       String requestStatus = loanAndWithdrawalForm.getFilterRequestStatus();
       //set the request status into criteria
       if(requestType != null && requestType.equals(WithdrawalWebUtil.LOANANDWITHDRAWAL))
              requestStatus=getFilteredRequestStatus(requestStatus,requestReason);
       
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_REQUEST_STATUS,
                (requestStatus != null && !requestStatus.equalsIgnoreCase("-1")) ? requestStatus
                        : REQUEST_STATUS_ALL);

        final String lastName = StringUtils.trim(loanAndWithdrawalForm
                .getFilterParticipantLastName());
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_LAST_NAME, (StringUtils
                .isNotBlank(lastName)) ? "" + StringEscapeUtils.escapeSql(lastName) + "" : null);

        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_SITE_LOCATION, Environment
                .getInstance().getDBSiteLocation());

        final BigDecimal userProfileId = new BigDecimal((int) getUserProfile(request)
                .getPrincipal().getProfileId());
        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_PROFILE_ID, userProfileId);

        // FIXME wait until security is implemented
        final boolean isTPA = userProfile.getRole().isTPA();
        if (isTPA) {
        	criteria.addFilter(LoanAndWithdrawalReportData.FILTER_ROLE_CODE, "TPA");
        } else if (userProfile.getRole() instanceof BundledGaCAR || userProfile.getRole() instanceof BundledGaApprover) {
        	criteria.addFilter(LoanAndWithdrawalReportData.FILTER_ROLE_CODE, userProfile.getRole().getRoleId());
        } else {
        	criteria.addFilter(LoanAndWithdrawalReportData.FILTER_ROLE_CODE, "PSW");
        }

        final String ssn = loanAndWithdrawalForm.getSsn().toString();

        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_SSN, StringUtils.trimToNull(ssn));

        final String contractName = StringUtils.trim(loanAndWithdrawalForm.getFilterContractName());
        loanAndWithdrawalForm.setFilterContractName(contractName);

        criteria.addFilter(LoanAndWithdrawalReportData.FILTER_CONTRACT_NAME, (StringUtils
                .isNotBlank(contractName)) ? "'" + contractName + "%'" : null);
    }

    protected List<Integer> getSearchableContracts(final UserProfile userProfile)
            throws SystemException {    	 

    	StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			if (logger.isDebugEnabled()) {
				logger.debug("getSearchableContracts> Entry.");
			} // fi
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        List<Integer> searchableContracts = new ArrayList<Integer>();
        final Contract currentContract = userProfile.getCurrentContract();
        if (currentContract != null) {
            searchableContracts.add(currentContract.getContractNumber());
        } else if (userProfile.getRole().isTPA()) {
            if (currentContract != null) {
                searchableContracts.add(currentContract.getContractNumber());
            } else {
                List<Integer> list = TPAServiceDelegate.getInstance()
						.getContractsWithLoanOrWDFeatureByTPA(
								userProfile.getPrincipal().getProfileId());
				searchableContracts.addAll(list);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getSearchableContracts> Exit.");
        } // fi
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info("getSearchableContracts timing:"
						+ stopWatch.toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return searchableContracts;
    }

    /**
     * This method populates a default form when the report page is first brought up. This method is
     * called before populateReportCriteria() to allow default sort and other criteria to be set
     * properly.
     * 
     * @param mapping The action mapping
     * @param reportForm The report form to populate.
     * @param request The current request object.
     * 
     * @see com.manulife.pension.ps.web.report.ReportController#populateReportForm(ActionMapping,
     *      com.manulife.pension.ps.web.report.BaseReportForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    protected void populateReportForm(
            final BaseReportForm reportForm, final HttpServletRequest request) {

        super.populateReportForm( reportForm, request);
    }

    /**
     * @param mapping The Struts Action Mapping object.
     * @param reportForm The current report form.
     * @param request The current request object.
     * @param response The current response object.
     * @return The ActionForward appropriate for this task. If validateForm is true and the
     *         validation fails, the ActionMapping's input page is returned.
     */
    public String doDefault( final BaseReportForm reportForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws SystemException {
    	
        final StopWatch stopWatch = new StopWatch();
        
		try {
			logger.info("doDefault> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        final UserProfile userProfile = getUserProfile(request);

        boolean hasAccess = isAccessGranted(request, userProfile);

        if (!hasAccess) {
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD);
        	return Constants.HOMEPAGE_FINDER_FORWARD;
        } // fi

        //ActionForward forward;
        String forward;
        if (getTask(request).equalsIgnoreCase(VIEW_ITEM_MAPPING)) {
            forward = doViewItem( reportForm, request, response);
        } else {
            forward = doCommon( reportForm, request, response);
        } // fi

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
        return forward;
    }


    public String doViewItem(final BaseReportForm reportForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws SystemException {

        final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doViewItem> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        final LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        setLookupData(actionForm,request);

        final String submissionId = request.getParameter(Constants.WITHDRAWAL_REQUEST_SUBMISSIONID);

        request.getSession().setAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_SUBMISSIONID,
                submissionId);

        request.getSession().setAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_CONTRACTID,
                request.getParameter(Constants.WITHDRAWAL_REQUEST_CONTRACTID));

        request.getSession().setAttribute(
                Constants.WITHDRAWAL_VIEW_PAGE + "." + Constants.WITHDRAWAL_REQUEST_PROFILEID,
                request.getParameter(Constants.WITHDRAWAL_REQUEST_PROFILEID));
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doViewItem> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		//TODO
        //return findForward( VIEW_ITEM_MAPPING);
		return  VIEW_ITEM_MAPPING;
    }

    /**
     * We now need to check if the user has access to this page. It's basically the same check that
     * is done for the link to this page (from the list page).
     * 
     * @param userProfile The user profile of the user to check access for.
     * @return boolean - True if the user has access, false otherwise.
     * @throws SystemException If an exception occurs.
     */
    protected boolean isAccessGranted(final HttpServletRequest request, final UserProfile userProfile) throws SystemException {

        // We now need to check if the user has access to this page. It's basically the same check
        // that is done for the link to this page (from the list page).
        boolean hasAccess = false;
        final Contract currentContract = userProfile.getCurrentContract();
        if (userProfile.getRole() instanceof InternalServicesCAR) {
            hasAccess = false;
        } else if (currentContract != null) {
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doDefault> Handling user with selected contract [")
                        .append(currentContract).append("].").toString());
            }
            final ContractInfo contractInfo = getContractInfo(request, currentContract.getContractNumber(),
                            userProfile.getPrincipal());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer("doDefault> Loaded contract information [").append(
                        contractInfo).append(")]").toString());
            }
            
            hasAccess = ((contractInfo.getShowWithdrawalToolsLink()) && 
            		(contractInfo.getCsfAllowOnlineWithdrawals() ||contractInfo.getCsfAllowOnlineLoans()));
 
            
                    	
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer("doDefault> User has access [").append(hasAccess)
                                .append("] with show tools link [").append(
                                        contractInfo.getShowWithdrawalToolsLink()).append(
                                        "] and CSF Online Withdrawals [").append(
                                        contractInfo.getCsfAllowOnlineWithdrawals()).append("]")
                                .toString());
            }

            logger.debug("doDefault> Handling TPA without selected contract.");
        } else {
            final Principal principal = userProfile.getPrincipal();
            
            hasAccess = checkForAnyTpaFirmWithPermission(request, principal,
					PermissionType.VIEW_ALL_LOANS);

			if (!hasAccess) {
				hasAccess = checkForAnyTpaFirmWithPermission(request,
						principal, PermissionType.VIEW_ALL_WITHDRAWALS);
			}

			if (!hasAccess) {
				hasAccess = checkForAnyTpaFirmWithPermission(request,
						principal, PermissionType.INITIATE_LOANS);
			}

			if (!hasAccess) {
				hasAccess = checkForAnyTpaFirmWithPermission(request,
						principal,
						PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE);
			}
        } // fi
        return hasAccess;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
     */
    protected String getReportId() {
        return LoanAndWithdrawalReportData.REPORT_ID;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
     */
    protected String getReportName() {
        return LoanAndWithdrawalReportData.REPORT_NAME;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
     */
    protected String getDefaultSortDirection() {
        return DEFAULT_SORT_DIRECTION;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
     */
    protected String getDefaultSort() {
        return DEFAULT_SORT_FIELD;
    }

    /**
     * Not implemented.
     * 
     * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData
     */
    protected byte[] getDownloadData(final BaseReportForm reportForm, final ReportData report,
            final HttpServletRequest request) throws SystemException {

        return "Download not implemented.".getBytes();
    }

    /**
     * This is the method to be extended for validation.
     * 
     * @return Error Collection
     */
    protected Collection<GenericException> doValidate(
            final ActionForm form, final HttpServletRequest request) {

        LoanAndWithdrawalRequestsForm theForm = (LoanAndWithdrawalRequestsForm) form;

        final Collection<GenericException> errors = new ArrayList<GenericException>(0);

        // do not validate since the filters are going to be reset
        if (getTask(request).equalsIgnoreCase("resetFilters")) {
            return errors;
        }

        Date fromDate = null;
        Date toDate = null;
        final DateFormat dateFormat = ConverterHelper.getDefaultDateFormat();
        dateFormat.setLenient(false);
        final String fromDateStr = StringUtils.trim(theForm.getFilterFromDate());
        final String toDateStr = StringUtils.trim(theForm.getFilterToDate());

        theForm.setFilterFromDate(fromDateStr);
        theForm.setFilterToDate(toDateStr);

        if (StringUtils.isNotBlank(fromDateStr)) {
            try {
                fromDate = dateFormat.parse(fromDateStr);
            } catch (ParseException parseException) {
                errors.add(new ValidationError("filterFromDate", ErrorCodes.INVALID_DATE));
            }
            if (fromDate != null) {
                Calendar twoYearsAgo = Calendar.getInstance();
                twoYearsAgo.add(Calendar.YEAR, -2);
                twoYearsAgo = DateUtils.truncate(twoYearsAgo, Calendar.DAY_OF_MONTH);
                if (fromDate.before(twoYearsAgo.getTime())) {

                    // Check and see if we started this request on the previous day
                    if (!DateUtils.isSameDay(theForm.getDisplayDate(), new Date())) {

                        // Quietly convert from date to two years ago
                        theForm.setFilterFromDate(dateFormat.format(twoYearsAgo.getTime()));
                        fromDate = twoYearsAgo.getTime();
                    } else {
                        // No rollover of date - treat as an error
                        errors.add(new ValidationError("filterFromDate",
                                ErrorCodes.FROM_DATE_BEFORE_24_MONTHS));
                    }
                }
            }
        } else {
            errors.add(new ValidationError("filterFromDate", ErrorCodes.FROM_DATE_EMPTY));
        }

        if (StringUtils.isNotBlank(toDateStr)) {
            try {
                toDate = dateFormat.parse(toDateStr);
            } catch (ParseException e) {
                errors.add(new ValidationError("filterToDate", ErrorCodes.INVALID_DATE));
            }
            Date today = new Date();
            if (toDate != null && toDate.after(today)) {
                errors.add(new ValidationError("filterToDate", ErrorCodes.TO_DATE_AFTER_TODAY));
            }
        } else {
            errors.add(new ValidationError("filterToDate", ErrorCodes.TO_DATE_EMPTY));
        }

        if (fromDate != null && toDate != null) {
            if (fromDate.after(toDate)) {
                errors.add(new ValidationError(new String[] { "filterFromDate", "filterToDate" },
                        ErrorCodes.FROM_DATE_AFTER_TO));
            }
        }

        // Strips spaces on the form fields.
        theForm.setSsnOne(StringUtils.trim(theForm.getSsnOne()));
        theForm.setSsnTwo(StringUtils.trim(theForm.getSsnTwo()));
        theForm.setSsnThree(StringUtils.trim(theForm.getSsnThree()));

        final String ssn = theForm.getSsn().toString();
        if (StringUtils.isNotEmpty(ssn) && (!ssn.matches("[0-9]{9}") || ssn.equals("000000000"))) {
            errors.add(new ValidationError("ssn", ErrorCodes.SSN_INVALID));
        }

        final String contractNumber = StringUtils.trim(theForm.getFilterContractNumber());
        theForm.setFilterContractNumber(contractNumber);
        if (StringUtils.isNotBlank(contractNumber)
                && (((contractNumber.length() < Constants.CONTRACT_NUMBER_MIN_LENGTH) || (contractNumber
                        .length() > Constants.CONTRACT_NUMBER_MAX_LENGTH)) || (!(NumberUtils
                        .isDigits(contractNumber))))) {
            errors.add(new ValidationError("filterContractNumber",
                    ErrorCodes.CONTRACT_NUMBER_INVALID));
        }

        final String lastName = StringUtils.trim(theForm.getFilterParticipantLastName());
        theForm.setFilterParticipantLastName(lastName);

        if (StringUtils.isNotBlank(lastName)) {
            if ((!(lastName.matches("[ a-zA-Z'.-]*")))
                    || (lastName.length() > GlobalConstants.LAST_NAME_LENGTH_MAXIMUM)) {
                errors.add(new ValidationError("filterParticipantLastName",
                        ErrorCodes.LAST_NAME_INVALID));
            } // fi

            if ((StringUtils.isBlank(contractNumber))
                    && (getShowTpaVersion(getUserProfile(request)))) {
                errors.add(new ValidationError("filterContractNumber",
                        ErrorCodes.PARTICIPANT_SEARCH_REQUIRES_A_CONTRACT));
            } // fi
        } // fi

        if (CollectionUtils.isNotEmpty(errors)) {
            final LoanAndWithdrawalReportData emptyReport = new LoanAndWithdrawalReportData(null, 0);
            emptyReport.setDetails(new ArrayList());
            try {
                setLookupData(form,request);
            } catch (SystemException systemException) {
                logger
                        .error(
                                "LoanAndWithdrawalRequestsAction doValidate() Could not retrieve lookup data",
                                systemException);
            }
            request.setAttribute(Constants.REPORT_BEAN, emptyReport);
            setErrorsInSession(request, errors);
        }

        return errors;
    }

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#populateSortCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
     *      com.manulife.pension.ps.web.report.BaseReportForm)
     */
    protected void populateSortCriteria(final ReportCriteria criteria, final BaseReportForm form) {

        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            // FIXME -- decide if secondary sort field required
            criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
        }
    }

    /**
     * {@inheritDoc}
     */
    /*
    @SuppressWarnings("rawtypes")
	protected String validate(final  final Form actionForm,
            final HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("validate> Entry.");
        }
        *//**
		 * This code has been changed and added to Validate form and request
		 * against penetration attack, prior to other validations as part of the
		 * CL#137697.
		 *//*
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(actionForm,
				mapping, request, CommonConstants.DEFAULT);
		if (penErrors != null && penErrors.size() > 0) {
			ActionForward forward = new ActionForward(CommonConstants.ERROR_RDRCT, "/do" + mapping.getPath(),true);
			return forward;
		}
        // Prevent user from copy and paste URL.
        final UserProfile userProfile = getUserProfile(request);
        // FIXME -- add to role ?
        
         * if (!userProfile.isWithdrawalAccess()) { // place the not authorized error msg in the
         * request Collection error = new ArrayList(1); error.add(new ValidationError("NOT
         * AUTHORIZED", ErrorCodes.WITHDRAWAL_INVALID_PERMISSION)); setErrorsInSession(request,
         * error); return findForward( "tools"); }
         
        return super.validate( actionForm, request);
    }
*/ 

	@Autowired
    private PSValidatorFWDefault psValidatorFWDefault;  

	@InitBinder
	protected void initBinder(HttpServletRequest request,
				ServletRequestDataBinder  binder) {
		binder.addValidators(psValidatorFWDefault);
	}
    public String doCommon(final BaseReportForm reportForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws SystemException {    	
    	LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
    	UserRole userRole = getUserProfile(request).getRole();
    	
    	// WRL 114 & WRL 115 if the search is made by using contract number and
		// contract name
        actionForm.setLoanTypeILoan(false);
        if (StringUtils.isNotBlank(actionForm.getFilterRequestReason())
				&& (actionForm.getFilterRequestReason().equalsIgnoreCase("L") || actionForm
						.getFilterRequestReason().equals("-1"))) {

			if (StringUtils.isNotBlank(actionForm.getFilterContractNumber())) {

				if (isILoan(actionForm.getFilterContractNumber())) {
					actionForm.setLoanTypeILoan(true);
				}
			} else if (StringUtils.isNotBlank(actionForm.getFilterContractName())) {
				
				actionForm.setLoanTypeILoan(false);
				final UserProfile userProfile = getUserProfile(request);
				final List<Integer> contracts = getContractsByName(userProfile);
				
				if (contracts.size() == 1) {
					for (Integer contract : contracts) {
						if (isILoan(String.valueOf(contract))) {
							actionForm.setLoanTypeILoan(true);
						}
					}
				}
			} else {
				actionForm.setLoanTypeILoan(false);
			}
		}
        
        String forward = super.doCommon( reportForm, request, response);
        Principal principal = getUserProfile(request).getPrincipal();
        Integer userProfileId = new Integer((int) principal.getProfileId());
        
        // add extra information to the report data
        LoanAndWithdrawalReportData data = (LoanAndWithdrawalReportData) request
                .getAttribute(Constants.REPORT_BEAN);
        logger.info(new StringBuffer("doCommon> Loaded data [").append(data).append("]").toString());
        
        Collection<LoanAndWithdrawalItem> details = (Collection<LoanAndWithdrawalItem>) data
				.getDetails();

        if (details != null) {
			for (LoanAndWithdrawalItem item : details) {
				logger.info(new StringBuffer("doCommon> Examining item [")
						.append(item).append("]").toString());

				ContractInfo contractInfo = getContractInfo(request, item
						.getContractNumber(), principal);

				UserRole tempUserRole = getUserRoleForContract(request,
						principal, contractInfo.getContractId());

				item.setUserRole(tempUserRole);
				item.setContractInformation(contractInfo);
				item.setUserProfileId(userProfileId);
			}
		}
        
        setLookupData(actionForm,request);
        setShowSearchLink(request, actionForm);

        LoanAndWithdrawalRequestsForm sessionForm = (LoanAndWithdrawalRequestsForm) request
                .getSession().getAttribute("reportForm");
        actionForm.setDisplayDate(new Date());
        if (sessionForm != null) {
            actionForm.copyFrom(sessionForm);
            request.getSession().setAttribute("reportForm", null);
        }     
        return forward;
    }

    /**
	 * Gets the contracts for the given userProfile
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException
	 */
    private List<Integer> getContractsByName(final UserProfile userProfile)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("getSearchableContracts> Entry.");
		} // fi

		List<Integer> contracts = new ArrayList<Integer>();
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
		
		for (TPAFirmInfo firmInfo : userInfo.getTpaFirmsAsCollection()) {
			if (firmInfo.getContractPermission().isViewAllWithdrawals()) {
				contracts.addAll(TPAServiceDelegate.getInstance()
						.getContractsByFirm(firmInfo.getId()));
			} // fi
		} // end for

		if (logger.isDebugEnabled()) {
			logger.debug("getSearchableContracts> Exit.");
		} // fi

		return contracts;
	}


    /**
     * Method called when user clicks on the editItem button for a record. Forwards the request to
     * the beforeProceedingPage, and sets the form in the session in case the BeforeProceedingAction
     * validation fails, and the flow is returned to the LoanAndWithdrawalRequest page with an
     * error. Depending on the status of the request it chooses between two mappings, the init or
     * the edit one
     */
    public String doEditItem(  final BaseReportForm reportForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException, SystemException {

        final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doEditItem> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        setLookupData(actionForm,request);

        HttpSession session = request.getSession();
        session.setAttribute("reportForm", actionForm);

        UserProfile userProfile = getUserProfile(request);
        Integer contractId = Integer.valueOf(request.getParameter("contractId"));
        Integer profileId = Integer.valueOf(request.getParameter("profileId"));
        Integer submissionId = Integer.valueOf(request.getParameter("submissionId"));

        Lockable withdrawalStub = SubmissionServiceDelegate.getInstance()
                .getLoanAndWithdrawalLockable(submissionId, contractId, profileId);
        boolean locked = LockManager.getInstance(request.getSession()).lock(withdrawalStub,
                String.valueOf(userProfile.getPrincipal().getProfileId()));
        if (!locked) {
            // cannot obtain a lock, generate an error and redisplay the page
            Collection<GenericException> lockError = new ArrayList<GenericException>(1);
            lockError.add(new ValidationError("LOCKED", ErrorCodes.WITHDRAWAL_LOCKED));
            setErrorsInSession(request, lockError);
            //TODO
            //return findForward( LOCK_ERROR_MAPPING);
            return LOCK_ERROR_MAPPING;
        }

        String status = request.getParameter("requestStatus");
        final String forward;
        if (status.equalsIgnoreCase(WithdrawalStateEnum.DRAFT.getStatusCode())) {
        	//TODO
            //forward = findForward( EDIT_ITEM_MAPPING);
            forward=EDIT_ITEM_MAPPING;
        } else {
            // Otherwise status is Pending review or Pending approval
        	//TODO
            //forward = findForward( REVIEW_ITEM_MAPPING);
        	forward=REVIEW_ITEM_MAPPING;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doEditItem> Exit with forward [").append(forward)
                    .append("].").toString());
        }
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doEditItem> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        return forward;
    }


    /**
	 * Retrieves lookup data and sets it in the form.
	 */
    private void setLookupData(final ActionForm reportForm,
    		HttpServletRequest request) throws SystemException {
    	logger.debug("setLookupData> Entry.");
    	final UserProfile userProfile = getUserProfile(request);
    	final LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;

    	
    	String status = WithdrawalWebUtil.getTypeOfRequest(	userProfile);
    	
    	if (status != null && !"".equals(status)) {
    		final Map lookupData = WithdrawalServiceDelegate.getInstance()
    		.getLookupData(null, StringUtils.EMPTY,
    				getLookupDataKeys(status, actionForm));
    		actionForm.setLookupData(lookupData);
    	}

    	logger.debug("setLookupData> Exit.");
    }    
    
    /**
     * @param status
     * @return
     */
    private Collection<String> getLookupDataKeys(String status,
    		LoanAndWithdrawalRequestsForm actionForm) {

    	Collection<String> lookupDataKeys = new ArrayList<String>();

    	lookupDataKeys.add(CodeLookupCache.REQUEST_TYPES);

    	if (status.equals(WITHDRAWAL_ONLY)) {
    		actionForm
    		.setRequestStatusCode(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED);
    		actionForm
    		.setReasonStatusCode(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
    		lookupDataKeys
    		.add(CodeLookupCache.WITHDRAWAL_REQUEST_STATUS_ORDERED);
    		lookupDataKeys.add(CodeLookupCache.ONLINE_WITHDRAWAL_REASONS);
    	} else if (status.equals(LOANS_ONLY)) {
    		actionForm
    		.setRequestStatusCode(CodeLookupCache.LOAN_REQUEST_STATUS);
    		actionForm.setReasonStatusCode(CodeLookupCache.LOAN_REASONS);
    		lookupDataKeys.add(CodeLookupCache.LOAN_REASONS);
    		lookupDataKeys.add(CodeLookupCache.LOAN_REQUEST_STATUS);
    	} else {
    		actionForm
    		.setRequestStatusCode(CodeLookupCache.LOAN_AND_WITHDRAWAL_REQUEST_STATUS);
    		actionForm
    		.setReasonStatusCode(CodeLookupCache.LOAN_AND_WITHDRAWAL_REASONS);
    		lookupDataKeys.add(CodeLookupCache.LOAN_AND_WITHDRAWAL_REASONS);
    		lookupDataKeys
    		.add(CodeLookupCache.LOAN_AND_WITHDRAWAL_REQUEST_STATUS);
    	}

    	return lookupDataKeys;
    }

    /**
	 * Sets a flag that determines whether the search link should be shown or
	 * suppressed.
	 */
    private void setShowSearchLink(final HttpServletRequest request, final ActionForm reportForm)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("LoanAndWithdrawalRequestsAction.setShowSearchLink> Entry.");
        }
        final LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        final UserProfile userProfile = getUserProfile(request);
        final Contract currentContract = userProfile.getCurrentContract();
        if (currentContract != null) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug(new StringBuffer(
                                "LoanAndWithdrawalRequestsAction.setShowSearchLink> Handling user with selected contract [")
                                .append(currentContract).append("].").toString());
            }
            final ContractInfo contractInfo = getContractInfo(request, currentContract.getContractNumber(), userProfile.getPrincipal());
            if (logger.isDebugEnabled()) {
                logger
						.debug(new StringBuffer(
								"LoanAndWithdrawalRequestsAction.setShowSearchLink> Loaded contract information [")
								.append(contractInfo).append(")]").toString());
            }
            actionForm.setShowSearchLink(contractInfo.getShowCreateWithdrawalRequestLink());
            actionForm.setShowLoanCreateLink(contractInfo.getShowCreateLoanRequestLink());
        } else {
            if (logger.isDebugEnabled()) {
                logger
                        .debug("LoanAndWithdrawalRequestsAction.setShowSearchLink> Handling TPA without selected contract.");
            } // fi

            final Principal principal = userProfile.getPrincipal();
			final PermissionType permissionType = PermissionType.INITIATE_WITHDRAWALS_AND_VIEW_MINE;
            final PermissionType permissionTypeForLoansInitiate=PermissionType.INITIATE_LOANS;
			boolean tpaFirmHasInitiatePermission = checkForAnyTpaFirmWithPermission(
					request, principal, permissionType);
			boolean tpaFirmHasLoanInitiatePermission = checkForAnyTpaFirmWithPermission(
					request, principal, permissionTypeForLoansInitiate);
			actionForm.setShowSearchLink(tpaFirmHasInitiatePermission);
			actionForm.setShowLoanCreateLink(tpaFirmHasLoanInitiatePermission);
        } // fi

        if (logger.isDebugEnabled()) {
            logger.debug("LoanAndWithdrawalRequestsAction.setShowSearchLink> Exit.");
        }
    }
    public String doResetFilters(
             final BaseReportForm reportForm, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException,
            SystemException {

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doResetFilters> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        actionForm.copyFrom(new LoanAndWithdrawalRequestsForm());
        doDefault( actionForm, request, response);
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doResetFilters> Exiting - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		//TODO
        //return mapping.getInputForward();
		return "input";
    }
    public String doParticipantSearch(
             final BaseReportForm reportForm, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException,
            SystemException {

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doParticipantSearch> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        setLookupData(actionForm,request);

        HttpSession session = request.getSession();
        session.setAttribute("reportForm", actionForm);
        session.setAttribute(TASK_KEY, "participantSearch");

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doParticipantSearch> Exiting - time duration [")
						.append(stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		//TODO
        //return findForward( "participantSearch");
		return "participantSearch";
    }
    public String doLoanParticipantSearch(
             final BaseReportForm reportForm, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException, ServletException,
            SystemException {

		final StopWatch stopWatch = new StopWatch();
		try {
			logger.info("doParticipantSearch> Entry - starting timer.");
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        LoanAndWithdrawalRequestsForm actionForm = (LoanAndWithdrawalRequestsForm) reportForm;
        setLookupData(actionForm,request);

        HttpSession session = request.getSession();
        session.setAttribute("reportForm", actionForm);
        session.setAttribute(TASK_KEY, "loanParticipantSearch");

		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doParticipantSearch> Exiting - time duration [")
						.append(stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		//TODO
        //return findForward( "loanParticipantSearch");
		return "loanParticipantSearch";
    }

    /**
     * Determines if the TPA without contract version of the page/data is shown.
     * 
     * @param userProfile The current user profile.
     * @return boolean - True if the user is a TPA user without a selected contract, false
     *         otherwise.
     */
    private boolean getShowTpaVersion(final UserProfile userProfile) {
        final UserRole userRole = userProfile.getRole();
        if (userRole.isTPA()) {
            if (userProfile.getCurrentContract() == null) {
                return true;
            } // fi
        } // fi
        return false;
    }
    
    /**This method returns the status code based on request reason. (loan/withdrawal reasons)
     * Eg(If the contract has loan and withdrawal feature and if the user select the loan or withdrawal reason 
     * from the drop down. search should be done only for the specific feature(either loan or withdrawal))
     * @param requestStatus
     * @param requestReason
     * @return
     */
    private String getFilteredRequestStatus(String requestStatus,
    		String requestReason) {
    	String[] requestCodes = requestStatus.split(":");
    	String withdrawalRequestStatus = null;
    	String loanRequestStatus = null;
    	int index = 0;
    	if (requestReason == null || requestReason.equals("-1") || 
    			 requestCodes.length == 1) {
    		return requestStatus.replace(":", ",");
    	}

    	if (requestCodes[index].contains("W")) {
    		withdrawalRequestStatus = requestCodes[index];
    		loanRequestStatus = requestCodes[index + 1];
    	} else {
    		withdrawalRequestStatus = requestCodes[index + 1];
    		loanRequestStatus = requestCodes[index];
    	}

    	if (requestReason.equalsIgnoreCase(LOAN))
    		return loanRequestStatus;
    	else
    		return withdrawalRequestStatus;

    }
    
    private boolean isILoan(String contractNumber)
			throws NumberFormatException, SystemException {

		LoanSettings loanSettings = LoanServiceDelegate.getInstance()
				.getLoanSettings(new Integer(contractNumber));
		if (loanSettings != null && loanSettings.isLrk01() && !loanSettings.isAllowOnlineLoans()) {
			return true;
		}

		return false;
	}    
    
}
