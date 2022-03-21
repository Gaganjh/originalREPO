/*
 * Created on May 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.iloans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.DateOfFirstPayment31GTLoanDate;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.DateOfFirstPaymentGTLoanDateRule;
import com.manulife.pension.ps.web.validation.rules.amortizationSchedule.DateRule;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.LoanRequestPackage;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * @author sternlu
 * 
 * LoanCreatePackageAction Validates user input and then calls account srevice
 * to create pdf loan package
 */
@Controller
@RequestMapping( value = "/iloans")
@SessionAttributes({"createLoanPackageForm"})

public class LoanCreatePackageController extends PsAutoController {
	@ModelAttribute("createLoanPackageForm")
	public LoanCreatePackageForm populateForm() {
		return new LoanCreatePackageForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	private static final String FORWARD_REFRESH = "refresh";
	static {
		forwards.put("input", "/iloans/createLoanPackage.jsp");
		forwards.put("createLoanPackage", "/iloans/iloans/createloanPackage.jsp");
		forwards.put(FORWARD_REFRESH, "redirect:/do/iloans/createLoanPackage/?action=refresh");
		forwards.put("viewLoanRequest", "redirect:/do/iloans/viewLoanRequests/");
	}

	@RequestMapping(value ="/createLoanPackage/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("createLoanPackageForm") LoanCreatePackageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		String forward = validate(form,request);
		if(forward != null){
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}

		
		if(!form.isPopulated())
			populateForm(form, request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		
		return forwards.get("input");

	}
	@RequestMapping(value ="/createLoanPackage/" ,params={"action=generatePdf","task=generatePdf"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCreatePackage (@Valid @ModelAttribute("createLoanPackageForm") LoanCreatePackageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward = validate(form,request);
		if(forward != null){
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		Collection errors = new ArrayList();
		HttpSession session = request.getSession(false);
		
		if (session.getAttribute(IloansHelper.LOAN_REQUEST_ID_PARM) != null) {
			String profileId = (String) session
					.getAttribute(IloansHelper.PROFILE_ID_PARM);

			String contractNumber = (String) session
					.getAttribute(IloansHelper.CONTRACT_NUMBER_PARM);

			String loanRequestId = (String) session
					.getAttribute(IloansHelper.LOAN_REQUEST_ID_PARM);

			TPAFirmInfo firm = TPAServiceDelegate.getInstance()
					.getFirmInfoByContractId(Integer.parseInt(contractNumber));
			String tpaId = String.valueOf(firm.getId());

			int pdfStreamLength = 0;
			AccountServiceDelegate delegate = AccountServiceDelegate
					.getInstance();
			LoanRequestPackage loanPackage = null;
	
			try {
				
				loanPackage = delegate.getLoanRequestTPAPackage(tpaId,
						profileId, contractNumber, loanRequestId, form
								.getDLoanDate(), form.getDNextRepaymentDate());
				if (loanPackage.getErrorCodes().length > 0)
					parseErrorCodes(loanPackage.getErrorCodes(), errors, form);

				if (errors.isEmpty()) {
	
					request.getSession(false).setAttribute("pdf",
							loanPackage.getPdf());
				} else
				{
					setErrorsInSession(request, errors);
					//return mapping.findForward(FORWARD_REFRESH);
					return FORWARD_REFRESH;
				}

			} catch (AccountException e) {
				IloansHelper.removeSessionAttributes(session);
				e.printStackTrace();
				throw new SystemException(e, this.getClass().getName(),
						"generatePdf", e.getMessage());
			} catch (Exception e) {
				IloansHelper.removeSessionAttributes(session);
				e.printStackTrace();
				throw new SystemException(e, this.getClass().getName(),
						"generatePdf", e.getMessage());
			}
		}
		return forwards.get("input");

	}

	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {
		/*This code has been changed and added to Validate form and
		 * request against penetration attack, prior to other validations
		 */
	    
		Collection errors = super.doValidate( form, request);
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}

		LoanCreatePackageForm packageForm = (LoanCreatePackageForm) form;
		if (logger.isDebugEnabled()) {
			logger.debug(packageForm.toString());
			Enumeration enumParams = request.getParameterNames();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String value = request.getParameter(key);
				logger.debug("Request Param " + key + " = " + value);
			}

		}
		// only validate for the generatePdfAction
		if (packageForm.isCreatePackageAction()) {

			//User needs to enter Date of Loan and . The dates has to be
			// in
			// MM/DD/YYYY format,
			DateRule dateRule = new DateRule(ErrorCodes.INVALID_LOAN_DATE);
			packageForm.setDLoanDate(dateRule.validate(
					LoanCreatePackageForm.FIELD_LOAN_DATE, errors,
					packageForm.getLoanDate()));

			dateRule = new DateRule(ErrorCodes.INVALID_NEXT_PAYMENT_DATE);
			packageForm.setDNextRepaymentDate(dateRule.validate(
					LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
					errors, packageForm.getNextRepaymentDate()));
			//The Date of Next Repayment needs to be an older date than the
			// date of the loan. We only check on this condition if the
			// valid dates have been entered

			Date testDate = new Date();

			if (packageForm.getDLoanDate() != null) {
				if (!packageForm.getDLoanDate().after(testDate))
					errors.add(new ValidationError(
							LoanCreatePackageForm.FIELD_LOAN_DATE,
							ErrorCodes.LOAN_DATE_LT_CURRENT_DAY));
			}
			/*
			 * if (packageForm.getDNextRepaymentDate() != null) { if
			 * (!packageForm.getDNextRepaymentDate().after(testDate)) errors
			 * .add(new ValidationError(
			 * LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
			 * ErrorCodes.NEXT_PAYMENT_DATE_LT_CURRENT_DAY)); }
			 */
			if (packageForm.getDLoanDate() != null
					&& packageForm.getDNextRepaymentDate() != null) {
				Pair pair = new Pair(packageForm.getDLoanDate(), packageForm
						.getDNextRepaymentDate());
				new DateOfFirstPaymentGTLoanDateRule(
						ErrorCodes.NEXT_PAYMENT_DATE_LT_LOAN_DATE).validate(
						LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
						errors, pair);
				if(errors.isEmpty())
				{
				 new DateOfFirstPayment31GTLoanDate(
						ErrorCodes.NEXT_PAYMENT_DATE_LT_LOAN_DATE_31).validate(
								LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
								errors, pair);
				}
			}
			if (errors.isEmpty())
				packageForm.setValidated(true);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		return errors;
	}

	private AccountServiceDelegate getAccountService() {

		return AccountServiceDelegate.getInstance();
	}

	private void parseErrorCodes(int[] errorCodes, Collection errors,
			LoanCreatePackageForm form) {
		for (int i = 0; i < errorCodes.length; i++) {
			String errorMessage = null;
			switch (errorCodes[i]) {
			case LoanRequestPackage.ERROR_CODE_RYL_BAD_EFFECTIVE_DATE: // 
				errors.add(new ValidationError(
						LoanCreatePackageForm.FIELD_LOAN_DATE,
						ErrorCodes.INVALID_LOAN_DATE));
				break;
			case LoanRequestPackage.ERROR_CODE_RYL_BAD_FIRST_PAYROLL_DATE://
				errors.add(new ValidationError(
						LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
						ErrorCodes.INVALID_NEXT_PAYMENT_DATE));
				break;
			case LoanRequestPackage.ERROR_CODE_RYL_FIRST_PAYROLL_DATE_TOO_LOW://
				errors.add(new ValidationError(
						LoanCreatePackageForm.FIELD_NEXT_REPAYMENT_DATE,
						ErrorCodes.NEXT_PAYMENT_DATE_LT_LOAN_DATE));
				break;

			case LoanRequestPackage.ERROR_CODE_RYL_BAD_FIRST_PAYROLL_DATE_SEMI_MONTHLY://
				errors.add(new ValidationError("",
						ErrorCodes.BAD_FIRST_PAYROLL_DATE_SEMI_MONTHLY));
				break;

			default:
				errors.add(new ValidationError("",
						ErrorCodes.ILOANS_GENERIC_ERROR));

			}
		}
	}

	@RequestMapping(value ="/createLoanPackage/", params={"action=back","task=back"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doBackToLoanRequestAction (@Valid @ModelAttribute("createLoanPackageForm") LoanCreatePackageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward = validate(form,request);
		if(forward != null){
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		IloansHelper.removeSessionAttributes(request.getSession(false));
		return forwards.get("viewLoanRequest");

	}

	private CustomerServicePrincipal getCustomerServicePrincipal(String clientId) {

		CustomerServicePrincipal principal = new CustomerServicePrincipal();
		principal.setName(clientId);
		principal
				.setRoles(new String[] { CustomerServicePrincipal.ROLE_SUPER_USER });

		return principal;
	}
	@RequestMapping(value ="/createLoanPackage/", params={"action=refresh","task=refresh"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("createLoanPackageForm") LoanCreatePackageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward = validate(form,request);
		if(forward != null){
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		return forwards.get("input");
	}
	protected String validate(
			ActionForm actionForm, HttpServletRequest request) {


		Collection errors = doValidate( actionForm, request);
		//setErrorsInRequest(request, errors);

		if (!errors.isEmpty()) {
			/*
			 * Go to the input page if validation fails.
			 */
			setErrorsInSession(request, errors);
			return forwards.get(FORWARD_REFRESH);
		}
		return null;

	}
	
	@RequestMapping(value ="/createLoanPackage/",params={"task=printPDF"}  , method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("createLoanPackageForm") LoanCreatePackageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = validate(form,request);
		if(forward != null){
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		 forward = super.doPrintPDF(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}


	
	
	protected void populateForm(LoanCreatePackageForm form,HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		String savedLoanDate = (String) session.getAttribute("iloanDate");
		if (savedLoanDate != null)
			form.setLoanDate(savedLoanDate);
		String savedPayrollDate = (String) session.getAttribute("payrollDate");
		if (savedPayrollDate != null)
			form.setNextRepaymentDate(savedPayrollDate);
		session.removeAttribute("iloanDate");
		session.removeAttribute("payrollDate");
		form.setPopulated(true);
	}
	
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
	}