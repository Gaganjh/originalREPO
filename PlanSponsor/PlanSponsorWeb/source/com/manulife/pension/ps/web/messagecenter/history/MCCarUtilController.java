package com.manulife.pension.ps.web.messagecenter.history;

import java.io.IOException;
import java.util.Collection;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.messagecenter.MCAbstractController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSummary;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * AJAX implementation for setting message status as VISITED
 * 
 */
@Controller
@RequestMapping( value = {"/mcCarView","/messagecenter"})

public class MCCarUtilController extends MCAbstractController {
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("summary", "redirect:/do/mcCarView");
	}

	private static final String ACTION_GET_RECIPIENTS = "getRecipients";
	private static final String ACTION_GET_MULTIPLE_CONTRACTS = "getMultipleContracts";
	private static final String ACTION_CHECK_ACCESS = "checkAccess";
	private static final String ACTION_INFO_LINK = "infoLink";
	
	public static final String REQUEST_PARAM_ROLE = "role";
	public static final String REQUEST_PARAM_USER_ID = "userId";
	public static final String REQUEST_PARAM_CONTRACT_ID = "contractId";
	public static final String REQUEST_PARAM_URL = "url";

	public static final String REQUEST_VALUE_TPA = "TPA";
	public static final String RESULT_YES = "yes";
	public static final String RESULT_NO = "no";
	public static final String RESULT_NO_RESULTS = "noresults";
	
	//Added to fix the MessageCenter Production issues with Loan Repayment details 
	private static final String ampersandSymbol ="&";
	private static final String equalSymbol = "=";
	
	@RequestMapping(value = {"/getRecipients"},  method =  {RequestMethod.GET}) 
	public String doGetRecipients(@Valid @ModelAttribute("dynaForm") DynaForm form,  BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("summary");
			}
		}
	
		return doExecute(form,"getRecipients", request, response);
	}
	
	@RequestMapping(value = {"/history/infoLink"},  method =  {RequestMethod.GET}) 
	public String doHistoryInfoLink(@Valid @ModelAttribute("dynaForm") DynaForm form,  BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("summary");
			}
		}
		return doExecute(form,"infoLink", request, response);
		
	}
	
	@RequestMapping(value = {"/getMultipleContracts"},  method =  {RequestMethod.GET}) 
	public String doGetMultipleContracts(@Valid @ModelAttribute("dynaForm") DynaForm form,  BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("summary");
			}
		}
		return doExecute(form,"getMultipleContracts", request, response);
		
	}
	
	@RequestMapping(value = {"/infoLink"},  method =  {RequestMethod.GET}) 
	public String doInfolink(@Valid @ModelAttribute("dynaForm") DynaForm form,  BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("summary");
			}
		}
	
		return doExecute(form,"infoLink", request, response);
	}
	
	@RequestMapping(value = {"/checkAccess"},  method =  {RequestMethod.GET}) 
	public String doCheckAccess(@Valid @ModelAttribute("dynaForm") DynaForm form,  BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forwards.get("summary");
			}
		}
		return doExecute(form,"checkAccess", request, response);
	
		
	}
	
	
	public String doExecute( ActionForm form, String param, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if (StringUtils.equals(param, ACTION_INFO_LINK)) {
			return doInfoLink( form, request, response);
		}
		
		response.setHeader("Cache-Control", "must-revalidate");
		response.setContentType("text/plain");
		String resultString = "";
		if (StringUtils.equals(param, ACTION_GET_RECIPIENTS)) {
			if (StringUtils.isEmpty(request.getParameter(REQUEST_PARAM_CONTRACT_ID))) {
				resultString = RESULT_NO_RESULTS; // this is a dummy string. A valid result must start with
													// GetRecipientsResult
			} else {
				try {
					Integer contractId = Integer.parseInt(request.getParameter(REQUEST_PARAM_CONTRACT_ID));
					resultString = MCCarViewUtils.getRecipientList(request.getServletContext(), contractId);
				} catch (NumberFormatException e) {
					resultString = RESULT_NO_RESULTS;
				}
			}
		}
		
		else if (StringUtils.equals(param, ACTION_GET_MULTIPLE_CONTRACTS)) {
			//if the user is a TPA, then we need to allow them to check this box for tpa firm messages. 
			//no need to even see if they have multiple contract.
			if (StringUtils.equals(REQUEST_VALUE_TPA, request.getParameter(REQUEST_PARAM_ROLE))) {
				resultString = RESULT_YES;
			} else {
				Collection<Integer> contracts = MCCarViewUtils.getUserContractIds(Integer.valueOf(request
						.getParameter(REQUEST_PARAM_USER_ID)), request.getParameter(REQUEST_PARAM_ROLE));
				resultString = contracts.size() > 1 ? RESULT_YES : RESULT_NO;
			}
		} else if ( StringUtils.equals(param, ACTION_CHECK_ACCESS)){
			Integer contractId = Integer.valueOf(StringUtils.trim(request
					.getParameter(REQUEST_PARAM_CONTRACT_ID)));
			String url = request.getParameter(REQUEST_PARAM_URL);
			resultString = checkAccess(request, url, contractId) ? url + "||" + contractId : RESULT_NO;
		}

		try {
			response.getWriter().print(resultString);
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing result.");
		}
		return null;
	}

	/**
	 * checks if a user has access to a url. 
	 * we must set then reset the contract context for the security manager to work. 
	 * 
	 * @param profile
	 * @param url
	 * @return
	 * @throws SystemException 
	 */
	private boolean checkAccess(HttpServletRequest request, String url, int contractId) throws SystemException {
		//get the old contract.
		UserProfile userProfile = getUserProfile(request);
		Contract oldContract = userProfile.getCurrentContract();
		if (contractId == 0) {
			// if its a firm message, set it to null
			userProfile.setContractProfile(null);
		} else if (oldContract == null || oldContract.getContractNumber() != contractId) {
			// if its for a different contract, then select the other contract
			SelectContractDetailUtil.selectContract(userProfile, contractId);
		}
		boolean returnValue = SecurityManager.getInstance().isUserAuthorized(userProfile, url);
		if (oldContract != null && oldContract.getContractNumber() != contractId) {
			// reset the context to the old context
			SelectContractDetailUtil.selectContract(userProfile, contractId);
		} else if (oldContract == null) {
			userProfile.setContractProfile(null);
		}
		return returnValue;
	}

	/**
	 * 
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	private String doInfoLink( ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		//lets see if we clicked this link from the global MC pages. 
		UserProfile user = getUserProfile(request);
		Integer contractId = Integer.valueOf(request.getParameter(REQUEST_PARAM_CONTRACT_ID));
		boolean contractMessage = contractId != 0;
		String url = request.getParameter(REQUEST_PARAM_URL);

		if (!checkAccess(request, url, contractId)) {
			ControllerRedirect f = new ControllerRedirect(MCConstants.CarViewUrl);
			return f.getPath();
		}
		
		Boolean isInGlobalContext = MCUtils.isInGlobalContext(request);
		Boolean changingContexttoContractContext = (isInGlobalContext && contractMessage)
				|| !isInGlobalContext && contractMessage && user.getCurrentContract().getContractNumber() != contractId;
		Boolean changingContextToNull = !contractMessage;
		
		//if they are coming from the global view then current contract will be null and we must set the contract context
		//if they are coming from the contract view and they clicked on a diffent contract, then we must switch it to that contract. 
		
		if (changingContexttoContractContext) {
			// clean up first
			user.setContractProfile(null);

			SessionHelper.clearSession(request);
			SelectContractDetailUtil.selectContract(user, contractId);
		}
		if (!isInGlobalContext && changingContextToNull) {
			user.setContractProfile(null);
			SessionHelper.clearSession(request);
		}
		// we must store the fact that they are leaving the message center via
		// our link.
		//because we need to NOT reset the form if they come back after they've left. 
		if (isInGlobalContext) {
			SessionHelper.setMCLeftMCFromGlobalContext(request, true);
		} else {
			SessionHelper.unsetMCLeftMCFromGlobalContext(request);
		}
		ControllerRedirect forward = new ControllerRedirect(url);

		return forward.getPath();
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	@Autowired
	private PSValidatorFWSummary psValidatorFWSummary;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.addValidators(psValidatorFWSummary);
	}
}
	

