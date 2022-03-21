package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.RegulatoryDisclosureForm;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWRegulatoryDisclosure;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails.UserIdType;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.util.content.GenericException;

/**
 * This is the action class for the Plan Information document Generation page.
 * This contains the logic to display the Plan Information document.
 * 
 * @author Rajesh Rajendran
 * 
 */
@Controller
@RequestMapping(value = "/planInfo")
public class PlanInformationController extends PsController {

	@ModelAttribute("disclosureForm")
	public RegulatoryDisclosureForm populateForm() {
		return new RegulatoryDisclosureForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("regulatoryDisclosure", "/fee/regulatoryDisclosure.jsp");
	}

	/* Logger */
	private Logger logger = Logger.getLogger(PlanInformationController.class);
	boolean isDebugEnabled = logger.isDebugEnabled();
	boolean isErrorEnabled = logger.isEnabledFor(Level.ERROR);
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";

	/**
	 * Constructor
	 */
	public PlanInformationController() {
		super(PlanInformationController.class);
	}

	/**;
	 * Method to generate the PIF form
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException,
	 *             ServletException, SystemException
	 */
	@RequestMapping(value = "" ,method = { RequestMethod.GET })
	public String doExecute(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("regulatoryDisclosure");
			}
		}
		if (isDebugEnabled) {
			logger.debug("entry -> doExecute");
		}
		Integer contractId = 0;
		UserProfile userProfile = getUserProfile(request);
		if (userProfile != null) {
			contractId = userProfile.getCurrentContract().getContractNumber();
		}
		String forward = null;
		byte[] downloadData = null;
		List<GenericException> errors = new ArrayList<GenericException>();
		try {
			Access404a5 contractAcc = getUserProfile(request).getAccess404a5();
			if (contractAcc.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE) == null) {
				return forward = Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
			UserAccess access = null;
			if (userProfile.isTPA()) {
				access = UserAccess.TPA;
			} else if (userProfile.isInternalUser()) {
				access = UserAccess.INTERNAL_USER;
			} else if (userProfile.getRole().isPlanSponsor()) {
				access = UserAccess.PSW;
			}
			FeeDisclosureUserDetails userDetails = getUserDetails(userProfile);
			downloadData = FundServiceDelegate.getInstance()
					.getPIAddendumBinary(userProfile.getCurrentContract().getContractNumber(), userDetails, access);

		} catch (Exception e) {
			logger.error("Exception while generating the IPI Addendum document for contract ["
					+ userProfile.getCurrentContract().getContractNumber() + "] " + e.getMessage(), e);
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN,
					LayoutBeanRepository.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		}

		if (downloadData != null && downloadData.length > 0) {
			BaseReportController.streamDownloadData(request, response, "application/msword",
					"ADDENDUM_" + contractId + ".doc", downloadData);
		}

		if (isDebugEnabled) {
			logger.debug("exit -> doExecute");
		}
		return forward;
	}

	private FeeDisclosureUserDetails getUserDetails(UserProfile userProfile) {

		FeeDisclosureUserDetails userDetails = new FeeDisclosureUserDetails();

		userDetails.setUserRoleCode(userProfile.getPrincipal().getRole().toString());
		userDetails.setApplicationId("PSW");
		userDetails.setRequestedTime(new Date().getTime());
		if (userProfile.isInternalUser()) {
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getUserName()));
			userDetails.setUserFirstName(userProfile.getPrincipal().getFirstName());
			userDetails.setUserLastName(userProfile.getPrincipal().getLastName());
		} else {
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}

		return userDetails;
	}

	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWRegulatoryDisclosure psValidatorFWRegulatoryDisclosure;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWRegulatoryDisclosure);
	}

}
