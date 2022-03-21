package com.manulife.pension.ps.web.profiles;

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

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This action detemines the appropriate version of the manage profiles page to 
 * display for the given user profile.  The decision is based on the permission
 * that has been granted to the user of the given profile.
 * 
 * Client-Manages-Client version
 * All client user roles with Selected Access = No and Manage External Users = Yes
 * 
 * Internal-Manages-Client version
 * All internal user roles with Manage External Users = Yes
 * 
 * Other-Manages-Own version
 * All client user roles with Selected Access = Yes OR Manage External Users = No
 * TPA with Manage TPA Users = No
 * All internal user roles with Manage External Users = No AND Manage Internal Users = No
 * 
 * Internal-Manages-Internal version
 * All internal user roles with Manage Internal Users = Yes
 * 
 * TPA-Manages-TPA version
 * TPA with Manage TPA Users = Yes
 * 
 * Note: Based on the current requirements, an internal user will never have
 * Manage External Users = Yes and Manage Internal Users = Yes.
 * 
 * @author Charles Chan
 * @author James Suzuki
 * 
 */


@Controller
@RequestMapping( value = "/profiles")
@SessionAttributes({"manageUsersReportForm"})
public class ManageUsersSelectorController extends PsController
{
	
	@ModelAttribute("manageUsersReportForm") 
	public ManageUsersReportForm populateForm() 
	{
		return new ManageUsersReportForm();
		
	}
	private static final String MANAGE_TPA_USER = "tpa";
	private static final String MANAGE_INTERNAL_USER = "internal";
	private static final String HOMEPAGE_FINDER_FORWARD_REDIRECT="homeRedirect";
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put(MANAGE_INTERNAL_USER,"/do/profiles/manageInternalUsers/");
		forwards.put(MANAGE_TPA_USER,"/do/profiles/manageTpaUsers/");
		forwards.put(HOMEPAGE_FINDER_FORWARD_REDIRECT,"redirect:/do/home/homePageFinder/");
	}
	
	/**
	 * Constructor.
	 */
	public ManageUsersSelectorController()
	{
		this(ManageUsersSelectorController.class);
	}

	/**
	 * Constructor.
	 * 
	 * @param clazz
	 */
	public ManageUsersSelectorController(Class clazz)
	{
		super(clazz);
	}

	/**
	 * Forwards to the appropriate version of the manage profiles page based on
	 * the permission granted to the user of the given user profile.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doExecute(
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	//It is an intermediate URL which is hidden so while loading the page it gets GET call and when it is redirecting from otehr page it is getting a POST call.
	@RequestMapping(value ="/manageUsers/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("manageUsersReportForm") ManageUsersReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String forwardName = null;
			UserRole role = getUserProfile(request).getPrincipal().getRole();
			if (role.hasPermission(PermissionType.MANAGE_TPA_USERS))
			{
				forwardName = MANAGE_TPA_USER;
			}
			else if (role.hasPermission(PermissionType.MANAGE_INTERNAL_USERS))
			{
				forwardName = MANAGE_INTERNAL_USER;
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(forwardName);
	       }
		}
	
	
		String forwardName = null;

		if (request.getParameter("lastVisited") != null)
		{
			forwardName = SessionHelper.getLastVisitedManageUsersPage(request);
		}

		if (forwardName == null)
		{
			UserRole role = getUserProfile(request).getPrincipal().getRole();

			if (role.hasPermission(PermissionType.MANAGE_EXTERNAL_USERS) && !role.hasPermission(PermissionType.SELECTED_ACCESS))
			{
				// Manage External User page retired, hence redirecting to home page
				forwardName = HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
			else if (role.hasPermission(PermissionType.MANAGE_TPA_USERS))
			{
				forwardName = MANAGE_TPA_USER;
			}
			else if (role.hasPermission(PermissionType.MANAGE_INTERNAL_USERS))
			{
				forwardName = MANAGE_INTERNAL_USER;
			}
			else
			{
				// Manage External User page retired, hence redirecting to home page
				forwardName = HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		}
		
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				getUserProfile(request).getPrincipal());
		request.setAttribute("userInfo", userInfo);
		
		SessionHelper.setLastVisitedManageUsersPage(request, forwardName);
		return forwards.get(forwardName);
	}
	
	  @Autowired
	  private ManageUsersSelectValidator  manageUsersSelectValidator;
      @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	  binder.bind(request);
	  binder.addValidators(manageUsersSelectValidator);
	   
	}
    
	
	
}