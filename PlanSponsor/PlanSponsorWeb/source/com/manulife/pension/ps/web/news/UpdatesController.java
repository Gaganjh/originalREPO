package com.manulife.pension.ps.web.news;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;

@Controller
@RequestMapping(value="/news")

public class UpdatesController extends PsController {

	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}
	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("current","/news/currentUpdates.jsp"); 
		forwards.put("newsLetter","/news/newsletterIssueIndex.jsp"); 
		forwards.put("archive","/news/archivedUpdates.jsp"); 
		forwards.put("legislative","/news/currentLegislativeUpdates.jsp"); 
		forwards.put("tpaLegislative","/news/currentTpaLegislativeUpdates.jsp"); 
		forwards.put("archiveLegisl","/news/archivedLegislativeUpdates.jsp"); 
		forwards.put("currentTpaUpdates","/news/currentTpaUpdates.jsp"); 
		forwards.put("archivedTpaUpdate","/news/archivedTpaUpdates.jsp"); 
		forwards.put("currentTpaIndustryUpdates","/news/currentTpaIndustryUpdates.jsp"); 
		forwards.put("archivedTpaIndustryUpdates","/news/archivedTpaIndustryUpdates.jsp");
		forwards.put("tpaForumNewsletter","/news/tpaForumNewsletter.jsp");
	}
	
	
	
	private static final String CURRENT_LEGISLATIVE_UPDATES_PAGEID = "/news/currentLegislativeUpdates.jsp";
	private static final String ARCHIVED_LEGISLATIVE_UPDATES_PAGEID = "/news/archivedLegislativeUpdates.jsp";
	private static final String CURRENT_TPA_UPDATES_PAGEID = "/news/currentTpaUpdates.jsp";
	private static final String CURRENT_TPA_INDUSTRY_UPDATES_PAGEID = "/news/currentTpaIndustryUpdates.jsp";
	public UpdatesController() {
		super(UpdatesController.class);
	}
	
	@RequestMapping(value = "/currentTpaUpdates/" , method =  {RequestMethod.GET})
	public String doCurrentTPAUpdate(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("currentTpaUpdates");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("currentTpaUpdates");
	}
	
	@RequestMapping(value = "/archivedTpaUpdates/" , method =  {RequestMethod.GET})
	public String doArchiveTPAUpdate(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("archivedTpaUpdate");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("archivedTpaUpdate");
	}
	
	@RequestMapping(value = "/currentTpaIndustryUpdates//" , method =  {RequestMethod.GET})
	public String doCurrentTpaIndustry(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("currentTpaIndustryUpdates");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("currentTpaIndustryUpdates");
	}
	
	@RequestMapping(value = "/archivedTpaIndustryUpdates/" , method =  {RequestMethod.GET})
	public String doArchiveTpaIndustry(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("archivedTpaIndustryUpdates");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("archivedTpaIndustryUpdates");
	}
	

	@RequestMapping(value = "/tpaForumNewsletter/" , method =  {RequestMethod.GET})
	public String doTpaForumNewsletter(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("tpaForumNewsletter");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("tpaForumNewsletter");
	}
	@RequestMapping(value = "/currentUpdates/" , method =  {RequestMethod.GET})
	public String doCurrentUpdate(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("current");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("current");
	}

	@RequestMapping(value = "/archivedUpdates/" , method =  {RequestMethod.GET})
	public String doArchiveUpdate (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("archive");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("archive");
	}

	@RequestMapping(value = "/currentLegislativeUpdates/" , method =  {RequestMethod.GET})
	public String doCurrentLegislative (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("legislative");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("legislative");
	}

	@RequestMapping(value ="/currentTpaLegislativeUpdates/", method =  {RequestMethod.GET})
	public String doCurrentTPALegislative (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("tpaLegislative");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("tpaLegislative");
	}

	@RequestMapping(value = "/archivedLegislativeUpdates/" , method =  {RequestMethod.GET})
	public String doArchiveLegislative (@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("archiveLegisl");//if input forward not //available, provided default
	       }
	
		}
	
		return forwards.get("archiveLegisl");
	}

	
	protected String createLayoutBean(HttpServletRequest request,
			String forward) {
		
		//Special code required for variable sub-menu when CMA Newsletters are available.
		//The News menu has a 2 variations.  If the newsletters are available - Updates, News,
		//Legislative updates.  Or if the newsletters are NOT available - Updates and Legislative
		//updates.  Therefore, we need to reset the Legislative Updates submenu (both archive and current)
		//if the CMA newsletters are available.  When the newsletters are not available, the Default 
		//sub-menu setting is 2 for Legislative updates.
		//When the newsletters are available, the Legislative updates sub-menu changes to position 3. 		
		
		LayoutBean bean = null;
		
		//get the current LayoutBean
		if (forward != null) {
			bean = LayoutBeanRepository.getInstance().getPageBean(
					forward);
		}


		// get the pageID	
		String pageID = bean.getId();
		
		//get the Environment variable 'isCMANewsLetterAvailable'
		Environment env = Environment.getInstance();
		boolean  CMANewsLetterAvailable  = env.isCMANewsLetterAvailable();
		
		
		//if the CMA newsletters are available
		if (CMANewsLetterAvailable) {
			//if the pageID is either the current Legislative Updates page or
			//the archived Legislative Updates page, then clone current bean, forward to page,
			//and save cloned bean in request
			if ((pageID.equals( CURRENT_LEGISLATIVE_UPDATES_PAGEID)) || (pageID.equals(ARCHIVED_LEGISLATIVE_UPDATES_PAGEID))) {
				LayoutBean cloneBean = (LayoutBean) bean.clone();
				cloneBean.setSubmenu("3");
				if (cloneBean != null){
					forward = forwards.get(cloneBean.getLayoutURL());
					request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
				}	
			}
		}		


		//if the CMA newsletters are available, and the PageIDs are NOT for the current Legislative updates
		//page or NOT for the archived Legislative updates (in other words the pageIDs are for the current
		//updates and archived updates pages), then forward to page and save the original bean in request.
		if ((CMANewsLetterAvailable) && (!pageID.equals(CURRENT_LEGISLATIVE_UPDATES_PAGEID)) && (!pageID.equals(ARCHIVED_LEGISLATIVE_UPDATES_PAGEID))) {
			forward = forwards.get(bean.getLayoutURL());
			request.setAttribute(Constants.LAYOUT_BEAN, bean);
		}

		
		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).		

		//DEFAULT FORWARD - if the CMA Newsletters are NOT available and if the bean is NOT null
		//forward to page and save the orginal bean in request.
		if ((!CMANewsLetterAvailable) && (bean != null)) {
			forward = forwards.get(bean.getLayoutURL());
			request.setAttribute(Constants.LAYOUT_BEAN, bean);
		}
		
		//if the SITE is NY and the PAGE id Current TPA Updates
		String subMenuValue = null;
		if ( Constants.SITEMODE_NY.equalsIgnoreCase(env.getSiteLocation()) ) {
			if ( pageID.equals(CURRENT_TPA_UPDATES_PAGEID) )
				subMenuValue = "1";
			else if ( pageID.equals(CURRENT_TPA_INDUSTRY_UPDATES_PAGEID) )
				subMenuValue = "2";
			
			if ( subMenuValue != null ) {	
				LayoutBean cloneBean = (LayoutBean) bean.clone();
				cloneBean.setSubmenu(subMenuValue);
			
				if (cloneBean != null){
					forward = forwards.get(cloneBean.getLayoutURL());
					request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
				}
			}
		}
	
		return forward;
	}
	/* (non-Javadoc)
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
