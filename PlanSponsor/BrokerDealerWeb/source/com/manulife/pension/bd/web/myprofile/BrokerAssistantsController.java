package com.manulife.pension.bd.web.myprofile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

@Controller
@RequestMapping(value ="/myprofile")

public class BrokerAssistantsController extends BDController {

	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	@ModelAttribute("addBrokerAssistantForm")
	public AddBrokerAssistantForm addBrokerAssistantForm() {
		return new AddBrokerAssistantForm();
	}

	@ModelAttribute("manageBrokerAssistantForm")
	public ManageBrokerAssistantForm manageBrokerAssistantForm() {
		return new ManageBrokerAssistantForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("listing", "/myprofile/assistants.jsp");
		forwards.put("fail", "/myprofile/assistants.jsp");
		forwards.put("myprofileDispatch", "redirect:/do/myprofileDispatch/");
	}

	public BrokerAssistantsController() {
		super(BrokerAssistantsController.class);
	}

	@RequestMapping(value = "/assistants", method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");
			}
		}
		//if user is bookmarked the URL, we still need to challenge.
		if(Objects.nonNull(request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND))) {
			if((boolean)request.getSession().getAttribute(CommonConstants.CHALLENGE_PASSCODE_IND)) {
				request.getSession().setAttribute("myProfileCurrentTab", MyProfileNavigation.AssistantsTabId);
				return forwards.get("myprofileDispatch");
			}
		}
		MyProfileContext context = MyProfileUtil.getContext(request.getServletContext(), request);
		context.getNavigation().setCurrentTabId(MyProfileNavigation.AssistantsTabId);
		MyProfileUtil.updateAssistantList(request);
		return forwards.get("listing");
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
}
