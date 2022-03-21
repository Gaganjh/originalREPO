package com.manulife.pension.bd.web.mimic;

import java.io.IOException;
import java.util.HashMap;

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

import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.login.UserCookie;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.bd.web.DynaForm;

/**
 * End the mimic session
 * 
 * @author guweigu
 * 
 */
@Controller
@RequestMapping(value = "/mimic")

public class EndMimicController extends BDController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("success", "redirect:/do/usermanagement/search?task=refresh");
		forwards.put("fail", "redirect:/do/home/");
	}
	private MimicSession mimicSession = new MimicSession();

	public static final String FORWARD_SUCCESS = "success";

	public static final String FORWARD_FAIL = "fail";

	public EndMimicController() {
		super(EndMimicController.class);
	}

	@RequestMapping(value = "/end", method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("fail");
			}
		}
		BDUserProfile currentUser = BDSessionHelper.getUserProfile(request);
		if (currentUser.isInMimic()) {
			mimicSession.endMimicSession(request);
			try {
				BDUserProfile mimicker = BDSessionHelper.getUserProfile(request);
				UserCookie cookie = new UserCookie(mimicker);
				response.addCookie(cookie);
			} catch (Exception e) {
				logger.error("Error setting UserCookie to mimic user - Advisor will not see mimicked content! - ", e);
			}
			return forwards.get(FORWARD_SUCCESS);
		} else {
			return forwards.get(FORWARD_FAIL);
		}
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	@Autowired
	private BDValidatorFWFail bdValidatorFWFail;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWFail);
	}
}
