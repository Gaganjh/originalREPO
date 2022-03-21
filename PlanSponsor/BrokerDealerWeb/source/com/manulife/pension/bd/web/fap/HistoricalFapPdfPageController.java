package com.manulife.pension.bd.web.fap;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.security.bd.valueobject.UserSiteInfoValueObject;

/**
 * Forwards to the Historical Funds PDF page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value = "/fap")
@SessionAttributes({ "historicalFapPdfPageForm" })
public class HistoricalFapPdfPageController extends BDController {

	@ModelAttribute("historicalFapPdfPageForm")
	public HistoricalFapPdfPageForm populateForm() {
		return new HistoricalFapPdfPageForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/fap/historicalFapPdfPage.jsp");
		forwards.put("historicalFapPdfPage","/fap/historicalFapPdfPage.jsp");
	}

	private static final String HISTORICAL_PDF_PAGE = "historicalFapPdfPage";
	private static final String CHOICE = "CHOICE";
	private static final String NO = "NO";
	private static final String YES = "YES";
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}

	public HistoricalFapPdfPageController() {
		super(HistoricalFapPdfPageController.class);
	}

	/**
	 * This is the default action to forward to the historical FAP page
	 * 
	 * @see BaseController#doExecute()
	 */
	@RequestMapping(value = "/fapHistoricalPdf", method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("historicalFapPdfPageForm") HistoricalFapPdfPageForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		BDUserProfile userProfile = getUserProfile(request);

		if (userProfile.isInternalUser()) {
			actionForm.setIncludeNml(CHOICE);
		} else if (BDUserProfileHelper.associatedWithApprovingFirm(userProfile, NFACodeConstants.NML)) {
			actionForm.setIncludeNml(YES);
		} else {
			actionForm.setIncludeNml(NO);
		}

		if (userProfile.getDefaultFundListing() != null) {
			actionForm.setRegion(userProfile.getDefaultFundListing().name());
		} else {
			actionForm.setRegion(UserSiteInfoValueObject.SiteLocation.USA.name());
		}

		if (logger.isDebugEnabled())
			logger.debug(HistoricalFapPdfPageController.class.getName() + ":forwarding to Historical Funds PDF page.");

		return forwards.get(HISTORICAL_PDF_PAGE);
	}
}
