package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.iloans.util.DateFormatter;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;

/**
 * Action class for the Loan Request Report. It gets the data from Customer
 * Service database,
 * 
 * 
 * @author Chris Shin
 * @version CS1.0 (March 1, 2004)
 */
@Controller
@RequestMapping( value = "/iloans")
@SessionAttributes({"loanRequestForm"})

public class LoanRequestConfirmationController extends LoanRequestBaseController {
	
	@ModelAttribute("loanRequestForm") 
	public LoanRequestForm populateForm()
	{
		return new LoanRequestForm();
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/iloans/loanRequestConfirmation.jsp");
		forwards.put("loanRequestConfirmation","/iloans/loanRequestConfirmation.jsp");
		forwards.put("modifyLoanRequest","redirect:/do/iloans/loanRequestPage1/");
		forwards.put(FORWARD_CONTINUE,"redirect:/do/iloans/createLoanPackage/");
		forwards.put(FORWARD_VIEW_LOAN_REQUEST,"redirect:/do/iloans/viewLoanRequests/");
	}

	// following 3 values passed over from calling session
	private static final String EMPLOYEE_CATEGORY_CODE = "EE";

	private static final BigDecimal ZERO = new BigDecimal("0");

	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

	public LoanRequestConfirmationController() {
		super(LoanRequestConfirmationController.class);
	}

	
	@RequestMapping(value ="/loanRequestConfirmation/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("loanRequestForm") LoanRequestForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
	
		
		String forward="loanRequestConfirmation";
		Collection errors = null;
		HttpSession session = request.getSession(false);
		//LoanRequestForm loanRequestForm = (LoanRequestForm) form;
		String button = actionForm.getButton();
		String parentPage = (String)session.getAttribute("iloansParentPage");

		
		if(parentPage ==null ||(!"view".equals(parentPage) && !"confirmation".equals(parentPage)&& !"second".equals(parentPage)))
			
			return forwards.get(FORWARD_VIEW_LOAN_REQUEST);
		if ("".equals(button)) 
		{
			if (!actionForm.isLoaded())
				populateForm(actionForm, request);			
			String task = request.getParameter("task");
			session.setAttribute("iloansParentPage","confirmation");
			if ("AP".equalsIgnoreCase(actionForm.getLoanRequestData()
						.getRequestStatusCode())
						|| "DE".equalsIgnoreCase(actionForm
								.getLoanRequestData().getRequestStatusCode())) 
			{
					if (task == null){
						////TODO saveToken(request);
					}

			} else 
			{
					IloansHelper.removeSessionAttributes(session);
					
					forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);
			}
			//3.6.6 Ref 23.4 & 23.5 Save data on the database for TPA initiated
			// loan requests
		} else if (FORWARD_VIEW_LOAN_REQUEST.equals(button)) {

				session.removeAttribute("loanRequestForm");
				IloansHelper.removeSessionAttributes(session);
				////TODO resetToken(request);
				
				forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);

		} else if ("saveAndExit".equals(button)) {
			if (actionForm.isTpaInitiated()
					&& actionForm.isLoanRequestApprovable()) 
			{
				//if (isTokenValid(request))
					if (request != null)
					errors = saveLoanRequest(actionForm);// commit
			}
			if (errors == null || errors.size() == 0) {
				session.removeAttribute("loanRequestForm");
				IloansHelper.removeSessionAttributes(session);
				////TODO resetToken(request);
				
				forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);
			}
			
			
		} else if ("send".equals(button)) {
			errors = saveLoanRequest(actionForm);
			session.removeAttribute("loanRequestForm");
			
			forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);
		} else if ("modify".equals(button)) {
			session.setAttribute("iloansParentPage", "confirmation");
			
			forward=forwards.get("modifyLoanRequest");

		} else if (FORWARD_CONTINUE.equals(button)) {
			// check if the loan status not 'AP' then save
			/*if (!isTokenValid(request))*/
			if (request != null)
				
				forward=forwards.get(FORWARD_VIEW_LOAN_REQUEST);
			else {
				if (LoanRequestData.REQUEST_STATUS_CODE_LOAN_APPROVED
						.equalsIgnoreCase(actionForm.getProceedWithLoan())) 
					errors = saveLoanRequest(actionForm);// commit

				if (errors == null || errors.size() == 0) {
					LoanCreatePackageForm createLoanPackageForm = new LoanCreatePackageForm();
					Date reqDate =actionForm.getLoanRequestData().getRylLoanEffectiveDate();
					if(reqDate!=null)
						session.setAttribute("iloanDate",DateFormatter.format(reqDate));
					Date payrollDate =actionForm.getLoanRequestData().getRylFirstPayrollDate();
					if(payrollDate !=null)
						session.setAttribute("payrollDate",DateFormatter.format(payrollDate));

					session.removeAttribute("loanRequestForm");
					//save loan request and forward to create loan package
					////TODO resetToken(request);
					
					forward=forwards.get(FORWARD_CONTINUE);
				}
			}
		}

		if (errors != null && errors.size() > 0) {
			setErrorsInSession(request, errors);
		}
		actionForm.setButton("");
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
		
/*
	private void populateForm(LoanRequestForm theForm,
			HttpServletRequest request) throws SystemException {

		LoanRequestData loanRequest = null;
		HttpSession session = request.getSession(false);
		UserProfile userProfile = getUserProfile(request);
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile);

		String profileId = request.getParameter("profileId");
		String contractNumber = request.getParameter("contractNumber");
		TPAFirmInfo firm = TPAServiceDelegate.getInstance()
				.getFirmInfoByContractId(Integer.parseInt(contractNumber));
		String tpaId = String.valueOf(firm.getId());
		String loanRequestId = request.getParameter("loanRequestId");
		try {
			loanRequest = getAccountService().getLoanRequest(tpaId, profileId,
					contractNumber, loanRequestId);
		} catch (Exception e) {
			throw new SystemException(e, LoanRequestConfirmationAction.class
					.getName(), "populateForm", e.getMessage());
		}

		theForm.setLoanRequestData(loanRequest);
	}
*/

	private void populateForm(LoanRequestForm theForm,
			HttpServletRequest request) throws SystemException {

		LoanRequestData loanRequest = null;
		HttpSession session = request.getSession(false);
		String profileId = (String)session.getAttribute(IloansHelper.PROFILE_ID_PARM);
		String contractNumber = (String)session.getAttribute(IloansHelper.CONTRACT_NUMBER_PARM);
		TPAFirmInfo firm = TPAServiceDelegate.getInstance()
				.getFirmInfoByContractId(Integer.parseInt(contractNumber));
		String tpaId = String.valueOf(firm.getId());
		String loanRequestId = (String)session.getAttribute(IloansHelper.LOAN_REQUEST_ID_PARM);
		try {
			loanRequest = getAccountService().getLoanRequest(tpaId, profileId,
					contractNumber, loanRequestId);
		} catch (Exception e) {
			throw new SystemException(e, LoanRequestConfirmationController.class
					.getName(), "populateForm", e.getMessage());
		}

		theForm.setLoanRequestData(loanRequest);
	}
	protected void completeLoanAction(LoanRequestForm theForm,
			String tpaId) throws SystemException {

		try {
			if (FORWARD_CONTINUE.equals(theForm.getButton())||"send".equals(theForm.getButton()) )
			{
			LoanRequestData loanRequestData = getAccountService()
					.commitLoanRequestTPAApprovalTransaction(
							getTPAId(theForm.getContractNumber()),
							theForm.getTransactionId());
			}
			else // "save and exit"
			{
				//3.5.12 If a TPA Initiated Loan Request and Loan Request Status not =
				// Not Proceed [18.11]
				String status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_PENDING;
				if (theForm.isTpaInitiated()
						&& LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED
								.equals(theForm.getProceedWithLoan()))
					status = LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED;
					LoanRequestData loanRequestData = getAccountService()
						.saveLoanRequestTPAApprovalTransaction(tpaId,
						theForm.getTransactionId(), status);	
			}
		} catch (Exception e) {
			throw new SystemException(e, this.getClass().getName(),
					"completeLoanAction", e.getMessage());
		}
	}

	protected void setSessionAttributes(HttpSession session,
			LoanRequestForm form) {

		session.setAttribute(IloansHelper.PROFILE_ID_PARM, form
				.getLoanRequestData().getProfileId());

		session.setAttribute(IloansHelper.CONTRACT_NUMBER_PARM, form
				.getLoanRequestData().getContractNumber());

		session.setAttribute(IloansHelper.LOAN_REQUEST_ID_PARM, form
				.getLoanRequestData().getLoanRequestId());

	}

	/*
	 * avoids token generation as this class acts as intermediate for many
	 * transactions(non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired
	 * (java.lang.String)
	 
	protected boolean isTokenRequired(String action) {
		return true;
	}

	
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ezk.web.BaseAction#isTokenValidatorEnabled(java
	 * .lang.String)
	 
	@Override
	public boolean isTokenValidatorEnabled(String actionName) {
		// avoids methods from validation which ever is not required
		if (StringUtils.isNotEmpty(actionName)
				&& (StringUtils.contains(actionName, "Default") || StringUtils
						.contains(actionName, "default")
						|| StringUtils.contains(actionName, "Print"))) {
			return false;
		} else {
			return true;
		}
	}*/



}