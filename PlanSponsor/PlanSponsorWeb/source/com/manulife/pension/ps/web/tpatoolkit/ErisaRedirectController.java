
package com.manulife.pension.ps.web.tpatoolkit;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.PsProperties;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWLarks;

/**
 * Redirects to the ERISA page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value = "/Erisa/")
public class ErisaRedirectController extends PsController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	private static final String URL = "erisa_site_location";
	private static final String USER_US = "erisa_user_us";
	private static final String USER_NY = "erisa_user_ny";
	private static final String RESPONSE_TYPE = "text/html";
	private static final String LOCATION = "Location";

	/**
	 * Constructor.
	 */
	public ErisaRedirectController() {
		super(ErisaRedirectController.class);
	}

	/**
	 * @see BaseController#doExecute()
	 */
	@RequestMapping(method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entry -> ERISA Redirect Action");
		}
		Environment environment = Environment.getInstance();
		response.setContentType(RESPONSE_TYPE);
		String URL;
		if (Constants.SITEMODE_USA.equals(environment.getSiteLocation())) {
			URL = environment.getNamingVariable(Constants.ERISA_USA_ONLINE_URL, null);
		} else {
			URL = environment.getNamingVariable(Constants.ERISA_NY_ONLINE_URL, null);
		}
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader(LOCATION, URL);
		if (logger.isDebugEnabled()) {
			logger.debug("Exit <- ERISA Redirect Action");
		}
		return null;
	}

	/**
	 * The method gets erisa site url
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUrl() throws SystemException {
		return PsProperties.getInstance().getProperty(URL);
	}

	/**
	 * The method gets the user name
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUserForUS() throws SystemException {
		return PsProperties.getInstance().getProperty(USER_US);
	}

	/**
	 * The method gets the password
	 * 
	 * @return String
	 * @throws SystemException
	 */
	private String getUserForNY() throws SystemException {
		return PsProperties.getInstance().getProperty(USER_NY);
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
