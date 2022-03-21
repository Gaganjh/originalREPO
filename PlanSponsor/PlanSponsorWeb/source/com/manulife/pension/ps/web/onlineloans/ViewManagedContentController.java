package com.manulife.pension.ps.web.onlineloans;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLoan;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;

/**
 * 
 */

@Controller
@RequestMapping( value ="/onlineloans")
@SessionAttributes({"loanForm"})

public class ViewManagedContentController extends PsAutoController {
	@ModelAttribute("loanForm") public LoanForm populateForm() {return new LoanForm();}

	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{ 
		forwards.put("truthInLendingNotice","/onlineloans/truthInLendingNotice.jsp");
		forwards.put("promissoryNote","/onlineloans/promissoryNote.jsp");
	}
	
    private static final String REQ_PARAM_SUBMISSION_ID = "submissionId";

    private static final String FORWARD_TRUTH_IN_LENDING_NOTICE = "truthInLendingNotice";

    private static final String FORWARD_PROMISSORY_NOTE = "promissoryNote";

    private static final String REQ_MANAGED_CONTENT_HTML = "managedContentHtml";

    private static final String REQ_MANAGED_CONTENT_REQUESTED = "managedContentRequested";

    private static final String REQ_SHOW_INTRO_1_DISCLAIMER = "showIntro1Disclaimer";

    public ViewManagedContentController() {
    }

    protected String getCurrentForward() {
        return AbstractLoanController.ACTION_FORWARD_VIEW_MANAGED_CONTENT;
    }
     
    protected String preExecute(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    

        String forward = super.preExecute( actionForm, request,
                response);
        if (forward != null) {
            return forward;
        }

        
        Loan loan = getLoan(actionForm, request);

        if (loan == null) {
            /*
             * If the loan cannot be read, probably due to an invalid submission
             * ID/contract ID combination, return to the list page.
             */
            return forwards.get(AbstractLoanController.ACTION_FORWARD_LIST);
        }

        // Controls display of the intro 1 disclaimer in the jsp.
        if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(loan.getStatus())) {
            request.setAttribute(REQ_SHOW_INTRO_1_DISCLAIMER, "true");
        } else { 
            request.setAttribute(REQ_SHOW_INTRO_1_DISCLAIMER, "false");
        }

        UserProfile userProfile = getUserProfile(request);
        Integer contractId = loan.getContractId();
        
        /*
         * Check other permissions and accessible contracts to determine if we
         * are okay to stay in this action.
         */
        UserRole userRoleWithPermissions = LoanActionForwardHelper
                .getUserRoleWithPermissions(userProfile, contractId);

        LoanSettings loanSettings = LoanServiceDelegate.getInstance()
				.getLoanSettings(loan.getContractId());

        forward = LoanActionForwardHelper.getActionForwardIfLoanNotAccessible(
                getCurrentForward(),  actionForm, request, userProfile,
                userRoleWithPermissions, loanSettings, loan);

        if (forward != null) {
            return forward;
        }

        return null;
    }

    protected Loan getLoan(final AutoForm actionForm,
            final HttpServletRequest request) throws SystemException {
        /*
         * First, check if there is already an loan object in the request. It's
         * safe to use the request's loan because it's gone after each request.
         */
        Loan loan = (Loan) request.getAttribute(AbstractLoanController.REQ_LOAN_DATA);
        if (loan != null) {
            return loan;
        }

        UserProfile userProfile = getUserProfile(request);

        Integer contractId = getContractId(request);
        String submissionId = StringUtils.trimToNull(request.getParameter(
                AbstractLoanController.PARAM_SUBMISSION_ID));

        if (submissionId == null || contractId == null) {
            return null;
        }
        loan = LoanServiceDelegate.getInstance().read(
                (int) userProfile.getPrincipal().getProfileId(),
                contractId, new Integer(submissionId));

        return loan;
    }

    public Integer getContractId(HttpServletRequest request) {
        /*
         * Get contract ID from parameter, if it's not found, check the FORM
         * itself and then the ID in the currently selected contract.
         */
        Integer contractId = null;
        UserProfile userProfile = getUserProfile(request);

        String contractIdStr = request.getParameter(
                AbstractLoanController.PARAM_CONTRACT_ID);
        if (StringUtils.isBlank(contractIdStr)) {
            Contract contract = userProfile.getCurrentContract();
            if (contract != null) {
                contractId = contract.getContractNumber();
            } else {
                return null;
            }
        } else {
            try {
                contractId = org.apache.commons.lang.math.NumberUtils
                        .createInteger(contractIdStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return contractId;
    }
    
    @RequestMapping(value ="/viewManagedContent/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
		}
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("truthInLendingNotice");//if input forward not //available, provided default
	       }
		}
        return null;
    }

    @RequestMapping(value ="/viewManagedContent/" ,params={"action=viewTruthInLendingNotice"}   , method =  {RequestMethod.GET}) 
    public String doViewTruthInLendingNotice (@Valid @ModelAttribute("loanForm") LoanForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forwardPre = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forwardPre)) {
			return StringUtils.contains(forwardPre,'/')?forwardPre:forwards.get(forwardPre);
		}
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("truthInLendingNotice");//if input forward not //available, provided default
	       }
		}
        String html = null;
        LoanDocumentServiceDelegate delegate = LoanDocumentServiceDelegate.getInstance();
        UserProfile userProfile = getUserProfile(request);
        Integer userProfileId = Long.valueOf(userProfile.getPrincipal().getProfileId()).intValue();
        Integer contractId = getContractId(request);
        if (contractId == null) {
        	ControllerRedirect forward=new ControllerRedirect(AbstractLoanController.ACTION_FORWARD_LIST);
        	return forward.getPath();
          
        }
        Integer submissionId = Integer.valueOf(request.getParameter(REQ_PARAM_SUBMISSION_ID));
        html = delegate.getTruthInLendingNoticeHtml(userProfileId, contractId, submissionId);

        request.setAttribute(REQ_MANAGED_CONTENT_HTML, html);
        request.setAttribute(REQ_MANAGED_CONTENT_REQUESTED, FORWARD_TRUTH_IN_LENDING_NOTICE);
        return forwards.get(FORWARD_TRUTH_IN_LENDING_NOTICE);
    }

    @RequestMapping(value ="/viewManagedContent/", params={"action=viewPromissoryNote"} , method =  {RequestMethod.GET}) 
    public String doViewPromissoryNote (@Valid @ModelAttribute("loanForm") LoanForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forwardPre = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forwardPre)) {
			return StringUtils.contains(forwardPre,'/')?forwardPre:forwards.get(forwardPre);
		}
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("truthInLendingNotice");//if input forward not //available, provided default
	       }
		}
        String html = null;
        LoanDocumentServiceDelegate delegate = LoanDocumentServiceDelegate.getInstance();
        UserProfile userProfile = getUserProfile(request);
        Integer userProfileId = Long.valueOf(userProfile.getPrincipal().getProfileId()).intValue();
        Integer contractId = getContractId(request);
        if (contractId == null) {
      
            ControllerRedirect forward=new ControllerRedirect(AbstractLoanController.ACTION_FORWARD_LIST);
            return forward.getPath();
        }
        Integer submissionId = Integer.valueOf(request.getParameter(REQ_PARAM_SUBMISSION_ID));
        html = delegate.getPromissoryNoteAndIrrevocablePledgeHtml(userProfileId, contractId, submissionId);

        request.setAttribute(REQ_MANAGED_CONTENT_HTML, html);
        request.setAttribute(REQ_MANAGED_CONTENT_REQUESTED, FORWARD_PROMISSORY_NOTE);
        return forwards.get(FORWARD_PROMISSORY_NOTE);
    }
    @RequestMapping(value ="/viewManagedContent/" ,params={"action=printPDF","task=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("loanForm") LoanForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	String forwardPre = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forwardPre)) {
			return StringUtils.contains(forwardPre,'/')?forwardPre:forwards.get(forwardPre);
		}
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("truthInLendingNotice");//if input forward not //available, provided default
	       }
		}
		String forward = super.doPrintPDF(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

    /**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 */

	 @Autowired
	   private PSValidatorFWLoan  psValidatorFWLoan;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWLoan);
	}
}
