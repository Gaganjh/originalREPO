package com.manulife.pension.ps.web.withdrawal;

import static com.manulife.pension.ps.web.withdrawal.WebConstants.WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * This class allows for the navigation to the withdrawals pages from other locations in the site
 * (or from emails, etc.).
 * 
 * @author glennpa
 */
@Controller
@RequestMapping( value = "/withdrawal")

public class NavigateToWithdrawalController extends BaseWithdrawalController {
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","redirect:/do/withdrawal/viewRequest/");
		forwards.put("noPermission","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
		forwards.put("defaultEdit","redirect:/do/withdrawal/beforeProceedingReview/");
		forwards.put("viewItem","redirect:/do/withdrawal/viewRequest/");
		forwards.put("noPermission","redirect:/do/withdrawal/loanAndWithdrawalRequestsInit/");
		}
	

    private static final String NAVIGATE_PARAMETER_VALUE_VIEW = "view";

    private static final String NAVIGATE_PARAMETER_VALUE_EDIT = "edit";

    /**
     * Default constructor.
     */
    public NavigateToWithdrawalController() {
    }

    /**
     * Default constructor.
     * 
     * @param clazz The class used to configure the logger.
     */
    public NavigateToWithdrawalController(Class clazz) {
        super(clazz);
    }

    /**
     * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
     *      com.manulife.pension.ps.web.controller.PsAutoForm,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value = {"/entry/edit/"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefaultReview(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	
	 if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("defaultEdit");//if input forward not //available, provided default
	       }
		}

	 model.addAttribute("mapping", "edit");
    if (logger.isDebugEnabled()) {
        logger.debug("doDefault> Entry.");
    }
    String forward=doDefault(form,model, request, response);
    model.remove("mapping");
    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
    @RequestMapping(value = {"/entry/view/"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
 	public String doDefaultView(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
 	throws IOException,ServletException, SystemException {
    	
 	 if(bindingResult.hasErrors()){
 	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	       if(errDirect!=null){
 	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
 	       }
 		}

 	 model.addAttribute("mapping", "view");
     if (logger.isDebugEnabled()) {
         logger.debug("doDefault> Entry.");
     }
     String forward=doDefault(form,model,request, response);
     model.remove("mapping");
     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 	}
    
	public String doDefault(DynaForm form,
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException { 
   
    
        String mode = (String)model.get("mapping");
        if (mode == null) {
            return forwards.get(ACTION_FORWARD_NO_PERMISSION);
        } // fi

        final String submissionIdParameter = request.getParameter("submissionId");
        final Integer submissionId = Integer.parseInt(submissionIdParameter);

        // Lookup the appropriate withdrawal request meta data.
        final WithdrawalRequestMetaData withdrawalRequestMetaData = WithdrawalServiceDelegate
                .getInstance().getWithdrawalRequestMetaData(submissionId);

        final String statusCode = withdrawalRequestMetaData.getStatusCode();
        
        // States that are never valid to view -- should redirect to the list page.
        if (StringUtils.equals(statusCode, WithdrawalStateEnum.DRAFT.getStatusCode())) {
            // The request is in draft.
            return forwards.get(ACTION_FORWARD_NO_PERMISSION);
        } // fi
        
        if (StringUtils.equals(statusCode, WithdrawalStateEnum.EXPIRED.getStatusCode())) {
            // The request is expired.
            return forwards.get(ACTION_FORWARD_NO_PERMISSION);
        } // fi

        final Date expirationDate = withdrawalRequestMetaData.getExpirationDate();
        final Date midnightToday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        if ((expirationDate == null) || (expirationDate.before(midnightToday))) {
            // The request is expired.
            return forwards.get(ACTION_FORWARD_NO_PERMISSION);
        } // fi

        // The 'origin' page is always the list page if there's an error that needs to go back to
        // the origin page, as we don't really have an origin page.
        WithdrawalRequestMetaDataUi withdrawalRequestMetaDataUi = new WithdrawalRequestMetaDataUi(
                withdrawalRequestMetaData, WebConstants.WITHDRAWAL_LIST_ORIGINATOR);
        request.getSession(false).setAttribute(WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE,
                withdrawalRequestMetaDataUi);

        // Check if it's valid to send the request to the edit page (it needs to be in a pending
        // status) and if it isn't, just send to the view page.
        if (StringUtils.equals(mode, NAVIGATE_PARAMETER_VALUE_EDIT)) {
            if (!((StringUtils.equals(statusCode, WithdrawalStateEnum.PENDING_REVIEW
                    .getStatusCode()))
                    || (StringUtils.equals(statusCode, WithdrawalStateEnum.PENDING_APPROVAL
                            .getStatusCode())))) {
                // The request status is invalid for this type of redirect.
                return forwards.get(ACTION_FORWARD_VIEW_ITEM);
            } // fi
        } // fi
        if(mode=="edit"){
        	 return forwards.get("defaultEdit");
        }else{
        	 return forwards.get(ACTION_FORWARD_DEFAULT);
        }
    }
   
    @Autowired
    private PSValidatorFWInput psValidatorFWInput;
    
    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
