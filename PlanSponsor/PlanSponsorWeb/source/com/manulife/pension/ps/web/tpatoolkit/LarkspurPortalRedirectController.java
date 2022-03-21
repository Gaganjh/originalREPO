package com.manulife.pension.ps.web.tpatoolkit;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.PsProperties;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLarks;

/**
 * Redirects to the Larkspur page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value = "/LarkspurPortal/")
public class LarkspurPortalRedirectController extends PsController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	private static final String URL = "larkspur_site_location";
	private static final String USER_US = "larkspur_user_us";
	private static final String USER_NY = "larkspur_user_ny";
	private static final String PASSWORD = "larkspur_password";
	private static final String RESPONSE_TYPE = "text/html";
	private static final String LOCATION = "Location";

	/**
	 * Constructor.
	 */
	public LarkspurPortalRedirectController() {
		super(LarkspurPortalRedirectController.class);
	}

	/**
	 * @see BaseController#doExecute()
	 */

	@RequestMapping(method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entry -> Larkspur Portal Redirect Action");
		}

		String user;
		Environment environment = Environment.getInstance();
		if (Constants.SITEMODE_USA.equals(environment.getSiteLocation())) {
			user = getUserUs();
		} else {
			user = getUserNy();
		}
		response.setContentType(RESPONSE_TYPE);
		StringBuffer site = new StringBuffer();
		site.append(getUrl()).append("?User=").append(user).append("&pw=").append(getPassword());
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader(LOCATION, site.toString());

		if (logger.isDebugEnabled()) {
			logger.debug("Exit <- Larkspur Portal Redirect Action");
		}

		return null;
	}

	/**
	 * The method gets larkspur site url
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUrl() throws SystemException {
		return PsProperties.getInstance().getProperty(URL);
	}

	/**
	 * The method gets us user name
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUserUs() throws SystemException {
		return PsProperties.getInstance().getProperty(USER_US);
	}

	/**
	 * The method gets ny user name
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUserNy() throws SystemException {
		return PsProperties.getInstance().getProperty(USER_NY);
	}

	/**
	 * The method gets the password
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getPassword() throws SystemException {
		return PsProperties.getInstance().getProperty(PASSWORD);
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWLarks psValidatorFWLarks;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWLarks);
	}

}
