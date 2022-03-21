package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLoanAndWithdrawal;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;

/**
 * LoanAndWithdrawalRequestsPageForwardAction is the action class used to
 * display the loan and withdrawal requests page. It verifies and forward the
 * control to the corresponding page. All other functionality is done by the
 * LoanAndWithdrawalRequestsAction
 * 
 * @author Ranjith sp
 */

@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"loanAndWithdrawalRequestsForm"})

public class LoanAndWithdrawalRequestsPageForwardController extends PsController {
	@ModelAttribute("loanAndWithdrawalRequestsForm") 
	public LoanAndWithdrawalRequestsForm populateForm()
	{
		return new LoanAndWithdrawalRequestsForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("loanOnly","/withdrawal/loanRequestListPage.jsp");
		forwards.put("withdrawalOnly","/withdrawal/withdrawalRequestListPage.jsp");
		forwards.put("loanAndWithdrawals","/withdrawal/loanAndWithdrawalRequestsListPage.jsp");
		}

	private static final String LOANS_ONLY = "loanOnly";

	private static final String WITHDRAWAL_ONLY = "withdrawalOnly";

	private static final String DEFAULT_LOAN_REQUEST_STATUS = "14, L1, L3";

	private static final String DEFAULT_WITHDRAWAL_REQUEST_STATUS = "14, W5, W6";

	private static final String DEFAULT_LOANAND_WITHDRAWAL_REQUEST_STATUS = "14, W5, W6:14, L1, L3";

	/**
	 * Constructor
	 */
	public LoanAndWithdrawalRequestsPageForwardController() {
		super(LoanAndWithdrawalRequestsPageForwardController.class);
	}

	/**
	 * Constructor
	 */
	public LoanAndWithdrawalRequestsPageForwardController(Class clazz) {
		super(clazz);

	}

	/**
	 * This is a static reference to the logger.
	 */
	private static final Logger logger = Logger
			.getLogger(LoanAndWithdrawalRequestsPageForwardController.class);

	/**
	 * identify type of request(loan or withdrawal or loan and withdrawal) based
	 * on access permissions.
	 * 
	 * @see com.manulife.pension.ps.web.withdrawal.LoanAndWithdrawalRequestsController#doDefault(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.ps.web.report.ReportForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping( value = "/loanAndWithdrawalRequestsPage/" ,method = {RequestMethod.GET,RequestMethod.POST})
	
	public String doExecute(@Valid @ModelAttribute("loanAndWithdrawalRequestsForm") LoanAndWithdrawalRequestsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              String typeOfRequest = WithdrawalWebUtil.getTypeOfRequest(getUserProfile(request));
	              return forwards.get(typeOfRequest);
	       }
	       
		}
		final UserProfile userProfile = getUserProfile(request);
		String typeOfRequest = WithdrawalWebUtil.getTypeOfRequest(userProfile);
		
		if (StringUtils.isNotBlank(actionForm.getTask())
				&& "resetFilters".equals(actionForm.getTask())) {
			actionForm.copyFrom(new LoanAndWithdrawalRequestsForm());
			// set default request status based on type of request(loan
			// only/loan and withdrawal/withdrawal only )
			if (typeOfRequest.equals(WithdrawalWebUtil.LOANANDWITHDRAWAL)) {
				actionForm
						.setFilterRequestStatus(DEFAULT_LOANAND_WITHDRAWAL_REQUEST_STATUS);
				actionForm.setFilterRequestReason("-1");
			} else if (typeOfRequest.equals(WithdrawalWebUtil.WITHDRAWAL_ONLY)) {
				actionForm
						.setFilterRequestStatus(DEFAULT_WITHDRAWAL_REQUEST_STATUS);
				actionForm.setFilterRequestReason("-1");
			} else {
				actionForm.setFilterRequestStatus(DEFAULT_LOAN_REQUEST_STATUS);
				actionForm.setFilterRequestReason("1");
			}
			// set the lookup data.
			setLookupData(actionForm, request);
		}
		actionForm.setFilterParticipantLastName("");
		actionForm.setFilterParticipantSSN("");
		actionForm.setTypeOfRequest(typeOfRequest);
		return forwards.get(typeOfRequest);

	}

	/**
	 * {@inheritDoc} This method is used to find the correct forward path based
	 * on the conditions. used only in case of error messages .
	 */
	protected String validate(
			final ActionForm actionForm, final HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger
					.debug("validate> Entry. in LoanAndWithdrawalRequestsPageForwardAction");
		}

		final UserProfile userProfile = getUserProfile(request);

		String forward = super.validate( actionForm, request);
		if (forward != null) {
			try {
				String typeOfRequest = WithdrawalWebUtil
						.getTypeOfRequest(userProfile);
				return forwards.get(typeOfRequest);
			} catch (SystemException e) {
				// do nothing.
			}
		}
		return forward;
	}

	private void setLookupData(final ActionForm reportForm,
			HttpServletRequest request) throws SystemException {
		logger.debug("setLookupData> Entry.");
		final UserProfile userProfile = getUserProfile(request);
		final LoanAndWithdrawalRequestsForm actionForm = 
				(LoanAndWithdrawalRequestsForm) reportForm;
		String status = WithdrawalWebUtil.getTypeOfRequest(userProfile);

		if (status != null && !"".equals(status)) {
			final Map lookupData = WithdrawalServiceDelegate.getInstance()
					.getLookupData(null, StringUtils.EMPTY,
							getLookupDataKeys(status, actionForm));
			actionForm.setLookupData(lookupData);
		}

		logger.debug("setLookupData> Exit.");
	}

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
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 */
	
	
	 @Autowired
	   private PSValidatorFWLoanAndWithdrawal  psValidatorFWLoanAndWithdrawal;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWLoanAndWithdrawal);
	}
}
