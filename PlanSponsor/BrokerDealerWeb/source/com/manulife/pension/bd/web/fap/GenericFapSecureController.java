package com.manulife.pension.bd.web.fap;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWSessionExpired;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;

/**
 * Action class specific to the Generic F&P - Secure page
 * 
 * @author SAyyalusamy
 *
 */
@Controller
@RequestMapping(value ="/fap")
public class GenericFapSecureController extends BDFapBaseController {
	
	@ModelAttribute("fapForm") 
	public FapForm populateForm() 
	{
	 return new FapForm();
	}
	
	@Autowired
	private BDValidatorFWSessionExpired  bdValidatorFWSessionExpired;
	
    @InitBinder
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWSessionExpired);
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("inputUS","/fap/fapUS.jsp");
		forwards.put("inputNY","/fap/fapNY.jsp");
		forwards.put("US","redirect:/do/fap/US");
		forwards.put("NY","redirect:/do/fap/NY");
		forwards.put("sessionExpired","/WEB-INF/fap/fapFilterResults.jsp");
	}
	
	/**
	 * Based on the URL, the CMA contents are loaded. 
	 * i.e. 
	 * If the action URL contains US, then load the US CMA contents
	 * If the action URL contains NY, then load the NY CMA contents  
	 */
	
	@RequestMapping(value = {"/","/US","/NY"},  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("fapForm") FapForm actionForm, 
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			actionForm.setMessagesExist(true);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			return	forwards.get("sessionExpired");// if input forward not
												// //available, provided default
			}
		}
		// Based on the URL set the company ID
		if (StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request), FapConstants.COMPANY_NAME_US)) {
			actionForm.setCompanyId(FapConstants.COMPANY_ID_US);
		} else if (StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request),
				FapConstants.COMPANY_NAME_NY)) {
			actionForm.setCompanyId(FapConstants.COMPANY_ID_NY);
			ApplicationHelper.setRequestContentLocation(request, Location.NEW_YORK);
		} else {
			// this logic will be applicable only when the user clicks the
			// Funds & Performance link from the secured home page.
			BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
			if (bdUserProfile != null) {
				if (StringUtils.equalsIgnoreCase(bdUserProfile.getDefaultFundListing().getCode(), FapConstants.COMPANY_NAME_NY)) {
					return forwards.get(FapConstants.COMPANY_NAME_NY);
				} else {
					return forwards.get(FapConstants.COMPANY_NAME_US);
				}
			}
		}

		// set the show NML option
		actionForm.setShowNML(showNMLOption(request));

		// set the mimic mode
		actionForm.setInMimic(isUserInMimic(request));

		request.getSession().setAttribute("FileName", "Generic_Fund&Performance");

		String topTabSelected = (String) request.getSession().getAttribute("topTabSelected");
		if ("blockOfBusiness".equals(topTabSelected)) {
			BDSessionHelper.removeBOBTabSelectionFromSession(request);
		}
		request.getSession().setAttribute("topTabSelected", "fundsAndPerformance");
		// populate the Base filter with default values
		
		super.doDefault(actionForm, request, response);
		
		if (StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request), FapConstants.COMPANY_NAME_NY)) {
			return forwards.get("inputNY");
		} else {
			return forwards.get("inputUS");
		}
	}
	
	/**
	 * validates whether the NML option should be shown for the user
	 * 
	 * @param request
	 * @return true, if the user is internal user
	 * @throws SystemException
	 */
	private boolean showNMLOption(HttpServletRequest request) throws SystemException {
		boolean enableNML = false;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		if (BDUserProfileHelper.isInternalUser(userProfile)) {
			enableNML = true;
		}
		return enableNML;
	}
}
