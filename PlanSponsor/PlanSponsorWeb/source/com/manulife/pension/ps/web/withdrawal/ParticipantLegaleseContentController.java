package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;


/**
 * ParticipantLegaleseContentAction is the action class for the view participant legalese content page.
 *
 * @author Maria Lee
 *
 * @action-url /withdrawal/viewRequest
 * @action-form ParticipantLegaleseContentForm
 * @action-forward default /withdrawal/view/viewParticipantLegaleseContent.jsp
  */
@Controller
@RequestMapping( value = "/withdrawal")

public class ParticipantLegaleseContentController extends BaseWithdrawalController {

	@ModelAttribute("participantLegaleseContentForm") 
	public ParticipantLegaleseContentForm populateForm() 
	{
		return new ParticipantLegaleseContentForm();
		}
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("default","/withdrawal/view/viewParticipantLegaleseContent.jsp");
		}

    private static final Logger logger = Logger.getLogger(ParticipantLegaleseContentController.class);
    private static final String CLASS_NAME = ParticipantLegaleseContentController.class.getName();

    /**
     * {@inheritDoc}
     */ 

    
    @RequestMapping(value = "/viewParticipantLegalese/", params= {"task=print"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String doDefault(@Valid @ModelAttribute("participantLegaleseContentForm") ParticipantLegaleseContentForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException { 
    	
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
    
        Integer submissionId = new Integer(request.getParameter("submissionId"));
        String texts = WithdrawalServiceDelegate.getInstance().
            getAgreedLegaleseContent(submissionId, WithdrawalRequest.CMA_SITE_CODE_EZK);

        actionForm.setLegaleseContentText(texts);
        return forwards.get("default");
    }
       
}