package com.manulife.pension.bd.web.bob.participant;

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

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.util.ViewStatementsUtility;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.BDPrincipal;

/**
 * Participant Statements Action class This class is used to forward the users's request to the
 * Participant statement page to access the Data Impact site for viewing statements.
 * 
 * @author Marcos Rogovsky
 * @author AAmbrose [code moved from plansponsor]
 */
@Controller
@RequestMapping(value ="/bob")

public class ParticipantStatementsController extends BDController {
	@ModelAttribute("participantStatementsForm")
	public ParticipantStatementsForm populateForm()
	{
		return new ParticipantStatementsForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantStatements.jsp");
		forwards.put("dstoStatements","redirect:/do/bob/participant/participantStatementSearch/");
		forwards.put("participantStatements","/participant/participantStatements.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
		}

	private static final String PARTICIPANT_STATEMENTS_PAGE = "participantStatements";
    private static String URL = "";

    public ParticipantStatementsController() {
        super(ParticipantStatementsController.class);
    }

    /**
     * The preExecute method has been overriden to see if the contractNumber is coming as part of
     * request parameter. If the contract Number is coming as part of request parameter, the
     * BobContext will be setup with contract information of the contract number passed in the
     * request parameter.
     * 
     */
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {
        super.preExecute( form, request, response);

        BobContextUtils.setUpBobContext(request);

        BobContext bob = BDSessionHelper.getBobContext(request);
        if (bob == null || bob.getCurrentContract() == null) {
            return forwards.get(BDConstants.BOB_PAGE_FORWARD);
        }
        if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}
        BobContextUtils.setupProfileId(request);

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value ="/participant/participantStatements/", method ={RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("participantStatementsForm") ParticipantStatementsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward = preExecute(actionForm, request, response);
    	if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    
        BobContext bobContext = getBobContext(request);

        String profileId = request.getParameter(BDConstants.PROFILE_ID);
        String ssn = null;
        ParticipantAccountVO participantAccountVO = null;
        ParticipantAccountDetailsVO participantDetailsVO = null;

        Contract currentContract = bobContext.getCurrentContract();
        if (profileId != null) {
            int contractNumber = currentContract.getContractNumber();
            String productId = currentContract.getProductId();

            BDPrincipal principal = getUserProfile(request).getBDPrincipal();
            participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(
                    principal, contractNumber, productId, profileId, null, false, false);
            participantDetailsVO = participantAccountVO.getParticipantAccountDetailsVO();
            ssn = participantDetailsVO.getSsn();
            URL = ViewStatementsUtility.getUrlForParticipantStatements("4");
        }else{
        	URL = ViewStatementsUtility.getUrlForStatements("4");
        }

        // generate the encrypted cookie for the Data Impact site
        response.addCookie(ViewStatementsUtility
                .getCookie(currentContract.getContractNumber(), ssn));
        if (StringUtils.isNotBlank(ssn)) {
            request.setAttribute(BDConstants.IS_PARTICIPANT, BDConstants.YES);
        } else {
            request.setAttribute(BDConstants.IS_PARTICIPANT, BDConstants.NO);
        }

        if (logger.isDebugEnabled())
            logger.debug(ParticipantStatementsController.class.getName()
                    + ":forwarding to Participant Statements Page.");
        
        //TODO need to check the passing parameters
        ControllerForward forwardNew  = new ControllerForward(profileId, false);
        forwardNew.setPath(URL);
        forwardNew.setRedirect(true);
        
        return forwardNew.getPath();
        // return mapping.findForward(PARTICIPANT_STATEMENTS_PAGE);
    }
    /** This code has been changed and added  to 
  	 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
}
