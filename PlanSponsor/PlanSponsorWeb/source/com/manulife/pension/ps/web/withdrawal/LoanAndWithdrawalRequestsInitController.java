package com.manulife.pension.ps.web.withdrawal;

import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.CommonMrlLoggingUtil;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * LoanAndWithdrawalRequestsInitAction is the action class used to display the loan and withdrawal
 * requests page. It resets the form values that might have been saved for navigation purposes. All
 * other functionality is done by the LoanAndWithdrawalRequestsAction
 * 
 * @author Mihai Popa
 */

@Controller
@RequestMapping( value = "/withdrawal")
@SessionAttributes({"loanAndWithdrawalRequestsForm"})

public class LoanAndWithdrawalRequestsInitController extends LoanAndWithdrawalRequestsController {
	@ModelAttribute("loanAndWithdrawalRequestsForm")
	public LoanAndWithdrawalRequestsForm populateForm() 
	{
		return new LoanAndWithdrawalRequestsForm();
		}
	

	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/WEB-INF/do/withdrawal/loanAndWithdrawalRequestsPage/");
		forwards.put("default","/WEB-INF/do/withdrawal/loanAndWithdrawalRequestsPage/");
		forwards.put("print","/WEB-INF/do/withdrawal/loanAndWithdrawalRequestsPage/");}

    /**
     * This is a static reference to the logger.
     */
    private static final Logger logger = Logger
            .getLogger(LoanAndWithdrawalRequestsInitController.class);

    /**
     * Resets the form values that might have been saved for navigation purposes.
     * @throws IOException 
     * @throws ServletException 
     * 
     * @see com.manulife.pension.ps.web.withdrawal.LoanAndWithdrawalRequestsController#doDefault(org.apache.struts.action.ActionMapping,
     *      AutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */  

    @RequestMapping(value = "/loanAndWithdrawalRequestsInit/", method = {  RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("loanAndWithdrawalRequestsForm") LoanAndWithdrawalRequestsForm form,BindingResult bindingResult,
			 HttpServletRequest request, HttpServletResponse response)
			throws SystemException, ServletException, IOException {
    
        request.getSession(false).removeAttribute(WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE);
        if (logger.isDebugEnabled()) {
            logger.debug("LoanAndWithdrawalRequestsInitAction.doDefault> Entry.");
        }
        
        final UserProfile userProfile = getUserProfile(request);
        boolean hasAccess = isAccessGranted(request, userProfile);

        if (!hasAccess) {
        	CommonMrlLoggingUtil.logUnAuthAcess(request,"User is not authorized",this.getClass().getName()+":"+"processRequest");
            return Constants.HOMEPAGE_FINDER_FORWARD;
        } // fi


        // reset the form in the session if any
        // this will ensure that the user always sees
        // the default view of the report
       final BaseReportForm blankForm = resetForm( form, request);
        String forward = doCommon( blankForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
                    "LoanAndWithdrawalRequestsInitAction.doDefault> Exit with forward [").append(
                    forward).append("].").toString());
        }
        return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
}
