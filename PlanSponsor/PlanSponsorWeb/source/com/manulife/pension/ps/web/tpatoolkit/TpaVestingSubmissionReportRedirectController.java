package com.manulife.pension.ps.web.tpatoolkit;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;

/**
 * This class is the action class that is used to determine how to redirect a request for the TPA
 * Vesting Report. If the user is internal, they are directed to the 'Control Reports' page,
 * otherwise they are directed to the 'TPA Toolkit' page.
 * 
 * @author Paul Glenn
 */
@Controller
@RequestMapping(value = "/tpatoolkit")
public class TpaVestingSubmissionReportRedirectController extends PsController {
	

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("tpa", "redirect:/do/tpatoolkit/tpaOtherGuideTools/#vesting");
		forwards.put("internal", "redirect:/do/tools/controlReports/#vesting");
	}

	private static final String ACTION_FORWARD_TPA = "tpa";
    private static final String ACTION_FORWARD_INTERNAL = "internal";

    /**
     * Default Constructor.
     * 
     * @param clazz
     */
    public TpaVestingSubmissionReportRedirectController() {
        super(TpaVestingSubmissionReportRedirectController.class);
    }

    /**
     * Provides the execute logic for this action.
     * 
     * @param mapping The action mapping.
     * @param form The form.
     * @param request The request.
     * @param response The response.
     * @return ActionForward - The action forward to redirect this request to.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doExecute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */

	@RequestMapping(value = "/tpaVestingSubmissionReportRedirect/", method = { RequestMethod.GET })
	public String doExecute( HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		final UserProfile userProfile = getUserProfile(request);
		if (userProfile.isInternalUser()) {
			// Internal users are directed to the control reports page.
			return forwards.get(ACTION_FORWARD_INTERNAL);
		} // fi

		return forwards.get(ACTION_FORWARD_TPA);

	}

}
