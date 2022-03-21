package com.manulife.pension.ps.web.userguide;

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

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentDescription;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.view.MutableGuideArticle;
import com.manulife.pension.content.view.MutableLayoutPage;
import com.manulife.pension.content.view.MutableUserGuide;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWThirdlevel;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.manager.ContentCacheManager;



@Controller
@RequestMapping(value = "/contentpages")
public class ThirdLevelController extends PsController 
{
	@ModelAttribute("thirdlevelForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("thirdlevel","/contentpages/userguide/thirdlevel.jsp");
		forwards.put("thirdlevelpop","/contentpages/userguide/thirdlevelpop.jsp");
	}
	
	private static final String THIRD_LEVEL = "thirdlevel";
	private static final String THIRD_LEVEL_POP = "thirdlevelpop";
	
	public ThirdLevelController()
	{
		super(ThirdLevelController.class);
	} 
	
	
	@RequestMapping(value ={"/userguide/thirdlevel/"},  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("thirdlevelForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		String forward=preExecute(actionForm, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(THIRD_LEVEL);//if input forward not //available, provided default

            // forwards.get(THIRD_LEVEL);//if input forward not //available, provided default
        	}
        } 
		 forward = execute(request, response);
		 if(forward!=null) {
			return forward;
		 }
		return forwards.get(THIRD_LEVEL);
	}
	
	@RequestMapping(value ={ "/userguide/thirdlevelpop/"},  method =  {RequestMethod.GET}) 
	public String doExecutePopUp(@Valid @ModelAttribute("thirdlevelForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		String forward=preExecute(actionForm, request, response);
		 if(StringUtils.isNotBlank(forward)) {
			 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		 }
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(THIRD_LEVEL);//if input forward not //available, provided default

            // forwards.get(THIRD_LEVEL);//if input forward not //available, provided default
        	}
        } 
		 forward = execute(request, response);
		 if(forward!=null) {
			return forward;
		 }
		return forwards.get(THIRD_LEVEL_POP);
	}
	
	public String execute(HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		UserProfile up =(UserProfile) request.getSession(false).getAttribute(
				Constants.USERPROFILE_KEY);
		int parentId = 0;
		String strContentKey = request.getParameter("contentKey");
		int contentKey = 0;
		if(strContentKey != null) {
			contentKey = Integer.parseInt(strContentKey);
		}
		
		try {
			BrowseServiceDelegate browseDelegate = BrowseServiceDelegate.getInstance();
			Content bean = ContentCacheManager.getInstance().getContentById(contentKey, ContentTypeManager.instance().GUIDE_ARTICLE);
			//MutableLayoutPage layoutPage = (MutableLayoutPage)bean;
			//parentId = layoutPage.getParentId();
			if(bean instanceof MutableGuideArticle) {
				MutableGuideArticle guideArticle = (MutableGuideArticle)bean;
				int guideParentKey = guideArticle.getParentKey();
				//Find the parent User Guide
				MutableUserGuide userGuide = (MutableUserGuide)browseDelegate.findContent(guideParentKey, ContentTypeManager.instance().USER_GUIDE);
				ContentDescription[] pageDescs = browseDelegate.findRelatedTo(userGuide.getId());
				//Should have only one page, get the first one
				if(pageDescs != null && pageDescs[0] != null) {
					MutableLayoutPage page = (MutableLayoutPage)browseDelegate.findContent(pageDescs[0].getKey(), ContentTypeManager.instance().LAYOUT_PAGE);
					parentId = page.getParentId();
				}				
			}
		} catch(ContentException e){
            logger.warn("Got an exception while retrieving content:", e);
            return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		//String parentId = request.getParameter("parentId");
		
		//We are allowing users with selected access permission to access this 
		//page via securityInfo.xml. However, we need to restrict them from 
		//accessing the Admin Guides and Fiduciary Guides
		if(up.getPrincipal().getRole().hasPermission(PermissionType.SELECTED_ACCESS) && 
				((parentId==73) || (parentId==75)))
			return Constants.HOMEPAGE_FINDER_FORWARD;
		return null;
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	
	   @Autowired
	   private PSValidatorFWThirdlevel  psValidatorFWThirdlevel;
	   @InitBinder
	   public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWThirdlevel);
	}
	
}