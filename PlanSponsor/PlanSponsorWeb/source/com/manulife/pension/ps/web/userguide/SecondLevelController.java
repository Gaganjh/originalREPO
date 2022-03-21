package com.manulife.pension.ps.web.userguide;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.view.MutableLayoutPage;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWSecondLevel;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.PilotCAR;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.manager.ContentCacheManager;

@Controller
@RequestMapping( value = "/contentpages")
public class SecondLevelController extends PsController 
{
	@ModelAttribute("secondlevelForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("secondlevel","/contentpages/userguide/secondlevel.jsp");
		forwards.put("landingpage","/do/contentpages/userguide/landingpage/?parentId=73");
	}	
	
	private static final String SECOND_LEVEL = "secondlevel";
	private static final String LANDING_PAGE = "landingpage";
	private static final String AUTO_SIGNUP_IND = "autoSignupInd";
	
	
	private static final Integer ADMIN_GIFL_LINK_ID=59817;
	private static final Integer ADMIN_GIFL_SELECT_LINK_ID=66718;
	
	private static Integer NOTICE_MANAG_LINK_ID = 88380;
	
	private static Map<Integer,Integer> adminGiflLinksMap= new HashMap<Integer,Integer> ();
	{
		adminGiflLinksMap.put(ADMIN_GIFL_LINK_ID, ADMIN_GIFL_LINK_ID);
		adminGiflLinksMap.put(ADMIN_GIFL_SELECT_LINK_ID, ADMIN_GIFL_SELECT_LINK_ID);
		
	}
	
	private static final Integer AUTO_CONTENT_LINK_ID=72485;
	private static final Integer SIGNUP_CONTENT_LINK_ID=72486;
	
	private static Map<Integer,Integer> autoSignupLinksMap= new HashMap<Integer,Integer> ();
	{
		autoSignupLinksMap.put(AUTO_CONTENT_LINK_ID, AUTO_CONTENT_LINK_ID);
		autoSignupLinksMap.put(SIGNUP_CONTENT_LINK_ID, SIGNUP_CONTENT_LINK_ID);
		
	}
	
	public SecondLevelController()
	{
		super(SecondLevelController.class);
	} 
	
	@RequestMapping(value ="/userguide/secondlevel/", method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("secondlevelForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		
		
		String forward=preExecute(actionForm, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		//preExecute(actionForm, request, response);
		 
		if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(SECOND_LEVEL);//if input forward not //available, provided default
          //  forwards.get(SECOND_LEVEL);//if input forward not //available, provided default
       	}
       } 
	
		UserProfile up =(UserProfile) request.getSession(false).getAttribute(
				Constants.USERPROFILE_KEY);
		int parentId = 0;
		boolean noticeManagerflag = false;
		
		String strContentKey = request.getParameter("contentKey");
		String giflVersion = up.getContractProfile().getContract().getGiflVersion();
		int contentKey = 0;
		if(strContentKey != null) {
			contentKey = Integer.parseInt(strContentKey);
		} else  {
			logger.warn("The content key is null");
			return forwards.get("homePageFinder");
		}
		
		try {
			Content bean = ContentCacheManager.getInstance().getContentById(contentKey, ContentTypeManager.instance().LAYOUT_PAGE);
			MutableLayoutPage layoutPage = (MutableLayoutPage)bean;
			parentId = layoutPage.getParentId();
		} catch(ContentException e){
            logger.warn("Got an exception while retreiving content: ", e);
            return forwards.get("homePageFinder");
		}
		//String parentId = request.getParameter("parentId");
		
		try {
			if (!(NoticeManagerUtility.validateProductRestriction(up
					.getCurrentContract())
					|| NoticeManagerUtility.validateContractRestriction(up
							.getCurrentContract())
					|| NoticeManagerUtility.validateDIStatus(
							up.getCurrentContract(), up.getRole())
					|| up.getRole() instanceof PilotCAR || up.isInternalServicesCAR()|| up.getRole()instanceof InternalUserManager) ) {
				noticeManagerflag = true;
			}
			
		} catch (ContractDoesNotExistException ex) {
	         logger.warn("Got an exception while retreiving contract details: ", ex);
		}
		
		//We are allowing users with selected access permission to access this 
		//page via securityInfo.xml. However, we need to restrict them from 
		//accessing the Admin Guides and Fiduciary Guides
		if(up.getPrincipal().getRole().hasPermission(PermissionType.SELECTED_ACCESS) && 
				((parentId==73) || (parentId==75)))
			return forwards.get("homePageFinder");
		//GIFL 1C
		// Possible bookmark. User trying to access GIFL secondlevel for a non-gifl contract 
		if(up.getCurrentContract() != null && !up.getCurrentContract().getHasContractGatewayInd() && adminGiflLinksMap.containsKey(contentKey)
				|| (Constants.GIFL_VERSION_03.equals(giflVersion) && ADMIN_GIFL_LINK_ID.equals(contentKey))
				|| (!Constants.GIFL_VERSION_03.equals(giflVersion) && ADMIN_GIFL_SELECT_LINK_ID.equals(contentKey))
				|| (!noticeManagerflag && NOTICE_MANAG_LINK_ID.equals(contentKey))){
			return forwards.get(LANDING_PAGE);
		}
		// Possible bookmark. User trying to access secondlevel not from the landing page for Auto/Sign-up feature
		String autoSignupMethod = (String)request.getSession(false).getAttribute(AUTO_SIGNUP_IND);
		if(autoSignupLinksMap.containsKey(contentKey) 
				&& !((ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO.equals(autoSignupMethod) && AUTO_CONTENT_LINK_ID.equals(contentKey))
						|| (ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP.equals(autoSignupMethod) && SIGNUP_CONTENT_LINK_ID.equals(contentKey)))){
			return forwards.get(LANDING_PAGE);
		}
		return forwards.get(SECOND_LEVEL);
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	
	 @Autowired
	   private PSValidatorFWSecondLevel  psValidatorFWSecondLevel;
	  @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWSecondLevel);
	}
	
}