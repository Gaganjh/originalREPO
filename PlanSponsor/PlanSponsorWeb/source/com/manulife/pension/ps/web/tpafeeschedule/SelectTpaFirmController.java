package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Action class to handle the Select TPA Firm page for TPA Users associated with
 * multiple TPA firm ID's
 * 
 * @author Akhil Khanna
 * 
 */
@Controller
@RequestMapping( value = "/feeSchedule")

public class SelectTpaFirmController extends BaseAutoController {

	@ModelAttribute("selectTpaFirmForm") 
	public SelectTpaFirmForm populateForm() 
	{
		return new SelectTpaFirmForm();
		}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/home/selectContractPage.jsp");
		forwards.put("default","/tpafeeschedule/selectTpaFirm.jsp");
		forwards.put("view","redirect:/do/viewTpaStandardFeeSchedule/");
		forwards.put("goToCustomizeContractSearchPage","redirect:/do/tpafee/contractSearch/");
		forwards.put("defaultForInternalUser","/tpafeeschedule/selectTpaFirmForInternalUser.jsp");
	}

	private static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE = "view";
	private static final String TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE = "goToCustomizeContractSearchPage";

	public SelectTpaFirmController() {
		super(SelectTpaFirmController.class);
	}

	/**
	 * Method to go to the Select TPA firm page or view page based upon certain
	 * permissions to the User
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value ="/selectTpaFirm/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("selectTpaFirmForm") SelectTpaFirmForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);

		Map<Integer, String> tpaFirmMap=new TreeMap<Integer, String>();
		
        String companyCode = Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment.getInstance().getSiteLocation()) 
        		? Constants.COMPANY_ID_NY : Constants.COMPANY_ID_US;
		
		//For Internal Users only
		if (userProfile.getRole().isInternalUser()) {
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(Constants.TPA_USER_INFO);
			tpaFirmMap = TPAServiceDelegate.getInstance()
					.retrieveTpaFirmsByTPAUserProfileId(userInfo.getProfileId(), companyCode);
		} 
		else {
				// For other users
			tpaFirmMap = TPAServiceDelegate.getInstance()
			.retrieveTpaFirmsByTPAUserProfileId(
							userProfile.getPrincipal().getProfileId(), companyCode);
		}
		
		// no access if no TPA firms
		if(tpaFirmMap.isEmpty()) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		Integer tpaFirmId = null;
		
		if (tpaFirmMap.size() == 1) {

			for (Map.Entry<Integer, String> tpaFirm : tpaFirmMap.entrySet()) {
				tpaFirmId = tpaFirm.getKey();
			}
		}

		// checks for User with single TPA firm ID
		if (tpaFirmId != null) {
			if (userProfile.getRole().isInternalUser()) {
				return redirectToViewPage(request, tpaFirmId);
			} else {
				if (isTPAUserManager(userProfile)
						|| isStandardFeeExist(tpaFirmId)) {
					return redirectToViewPage(request , tpaFirmId);
				} else {
					return redirectToCustomizeContractSearchPage(request,
							 tpaFirmId);
				}
			}
		}
		
		request.setAttribute("tpaFirmMap", tpaFirmMap);
		if (userProfile.getRole().isInternalUser()){
			return forwards.get("defaultForInternalUser");
		}
		return forwards.get("default");
	}

	/**
	 * Method to check if the User has Tpa User Manager privileges
	 * 
	 * @param userProfile
	 * @return isTpaUserManager
	 * @throws SystemException
	 */

	private boolean isTPAUserManager(UserProfile userProfile)
			throws SystemException {
		boolean isTpaUserManager = false;
		if (userProfile.getPrincipal().getRole().hasPermission(
				PermissionType.MANAGE_TPA_USERS)) {
			isTpaUserManager = true;
		}
		return isTpaUserManager;
	}


	/**
	 * Method to check if the Standard Fee schedule exists
	 * 
	 * @param tpaFirmId
	 * @return isStandardFeeListPresent
	 * @throws SystemException
	 */

	private boolean isStandardFeeExist(Integer tpaFirmId) throws SystemException {
		boolean isStandardFeeListPresent = false;
		FeeServiceDelegate feeDelegate = FeeServiceDelegate
				.getInstance(CommonConstants.PS_APPLICATION_ID);

		List<FeeDataVO> standardFeeList = feeDelegate.getTpaStandardFeeScheduleData(tpaFirmId);
		if (!standardFeeList.isEmpty()) {
			isStandardFeeListPresent = true;
		}
		return isStandardFeeListPresent;
	}

	/**
	 * Method redirect To standard View Page
	 * 
	 * @param request
	 * @param mapping
	 * @param tpaFirmId
	 * @return newForward
	 * @throws SystemException
	 */

	private String redirectToViewPage(HttpServletRequest request,
			 Integer tpaFirmId) {

		request.getSession().setAttribute(
				Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
				String.valueOf(tpaFirmId));
		request.getSession().setAttribute(
				Constants.REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE,
				Boolean.TRUE.toString());
		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE);
	}
	
	/**
	 * Method to redirect To View customize Page
	 * 
	 * @param request
	 * @param mapping
	 * @param tpaFirmId
	 * @return newForward
	 * @throws SystemException
	 */

	private String redirectToCustomizeContractSearchPage(
			HttpServletRequest request,   Integer tpaFirmId) {

		request.getSession().setAttribute(
				Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
				String.valueOf(tpaFirmId));

		return forwards.get(TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);

	}
	
	/**
	 * Method to go to the tpa standard page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * 
	 */
	
	@RequestMapping(value ="/selectTpaFirm/"   ,params={"action=standardize"}   , method =  {RequestMethod.GET}) 
	public String doStandardize (@Valid @ModelAttribute("selectTpaFirmForm") SelectTpaFirmForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Integer tpaFirmId = Integer.valueOf(form.getSelectedTpaFirmId());
		
		if (userProfile.getRole().isInternalUser()) {
			return redirectToViewPage(request,  tpaFirmId);
		} else {
			if (isTPAUserManager(userProfile)
					|| isStandardFeeExist(tpaFirmId)) {
				return redirectToViewPage(request,  tpaFirmId);
			} else {
				return redirectToCustomizeContractSearchPage(request,
						 tpaFirmId);
			}
		}
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations 
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
