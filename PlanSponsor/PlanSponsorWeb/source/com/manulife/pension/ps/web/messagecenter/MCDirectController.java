package com.manulife.pension.ps.web.messagecenter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * Direct action to the message center
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping( value = "/mcdirect")

public class MCDirectController extends PsController {

	public MCDirectController() {
		super(MCDirectController.class);
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("summary","redirect:/do/messagecenter/summary");
	}

	@RequestMapping(value ="/summary",  method =  {RequestMethod.POST}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = "summary";

		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Set<Integer> accessibleContracts = userProfile.getMessageCenterAccessibleContracts();
		if (accessibleContracts == null || accessibleContracts.isEmpty()) {
			forward = Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		} else {
			Contract current = userProfile.getCurrentContract();
			if (current != null) {
				// if the current contract is not mc accessible
				// returns
				if (!accessibleContracts.contains(current.getContractNumber())) {
					forward = Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
				}
			}
		}
		return forwards.get(forward);
	}
}
