
package com.manulife.pension.ps.web.participant;

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
import org.springframework.web.util.UrlPathHelper;


import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.util.ViewStatementsUtility;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.log.LogUtility;

/**
 * Participant Statements Action class 
 * This class is used to forward the users's request to 
 * the Participant Menu page
 * 
 * @author Marcos Rogovsky
 */
@Controller
@RequestMapping(value="/participant")

public class ParticipantStatementsController extends PsController 
{
	
	@ModelAttribute("participantStatementsForm") 
	public  ParticipantStatementsForm  populateForm() 
	{
		return new  ParticipantStatementsForm ();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("dstoStatements","redirect:/do/participant/participantStatementSearch");
		forwards.put("participantStatements","/participant/participantStatements.jsp");
		}

	
	
	private static final String PARTICIPANT_STATEMENTS_PAGE = "participantStatements";
	
	public ParticipantStatementsController()
	{
		super(ParticipantMenuController.class);
	} 
	
	/**
	 * Pre execute method to handle the DSTO flag
	 */
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException, SystemException {
		return  forwards.get(CommonConstants.DSTO_STATEMENTS_FWD);
	}
	
	@RequestMapping(value ="/participantStatements",method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("participantStatementsForm") ParticipantStatementsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forwardPreExecute=preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forwardPreExecute)){
			return forwardPreExecute;
		}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             String forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
  		   return "redirect:"+forward;
        	}
        }
	
        String ssn = null;
        UserProfile profile = getUserProfile(request);
        String profileId = request.getParameter("profileId");
        //user can navigate from (1)Quick Report dropdown by choosing Statements (2)after login, follows employee link (3) Quick Report dropdown - Account --> Click on Participant name with masked SSN, next - click View this participants statement link
        if (profileId != null){
            ParticipantAccountVO participantAccountVO = null;
            ParticipantAccountDetailsVO participantDetailsVO = null;
            Contract currentContract = profile.getCurrentContract();
            int contractNumber = currentContract.getContractNumber();
            String productId = profile.getCurrentContract().getProductId();
            
            Principal principal = getUserProfile(request).getPrincipal();
            try{
            participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(principal, contractNumber,
                    productId, profileId, null, false, false);
            }
            catch(SystemException se){
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
            }
            participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();
            ssn = participantDetailsVO.getSsn();
        }
        // generate the encrypted cookie for the Data Impact site
	    response.addCookie(ViewStatementsUtility.getCookie(profile.getCurrentContract().getContractNumber(), ssn));
		
		if ( ssn != null && ssn.trim().length() > 0 )
			request.setAttribute("isParticipant", "Y");
		else
			request.setAttribute("isParticipant", "N");		
			
		
		if ( logger.isDebugEnabled() )
			logger.debug(ParticipantMenuController.class.getName()+":forwarding to Participant Statements Page.");
			
		return forwards.get(PARTICIPANT_STATEMENTS_PAGE);
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}
}
