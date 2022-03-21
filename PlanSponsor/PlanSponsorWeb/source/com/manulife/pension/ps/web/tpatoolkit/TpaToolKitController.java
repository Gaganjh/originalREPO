package com.manulife.pension.ps.web.tpatoolkit;

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
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpaToolKit;


/**
 * TPA Tool Kit page Action class 
 * This class is used to forward the users's request to 
 * the TPA Tool Kit page
 * 
 * @author Romeo Harricharran
 */

@Controller
@RequestMapping( value = "/tpatoolkit")
public class TpaToolKitController extends PsController
{ 
	
	
	@ModelAttribute("tpatoolkitAction") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
	forwards.put("tpatoolkit_test","/tpatoolkit/test.jsp");
	forwards.put("tpatoolkit_tpaAdmin","/tpatoolkit/tpaAdministrationGuide.jsp");
	forwards.put("tpatoolkit_tpaAudit","/tpatoolkit/tpaAuditPackage.jsp");
	forwards.put("tpatoolkit_tpaOther","/tpatoolkit/tpaOtherGuideTools.jsp");
	}
	
	
	
	private static final String TPA_TOOL_KIT = "tpatoolkit";
	
	public TpaToolKitController() 
	{
		super(TpaToolKitController.class);
	} 
	
	 @RequestMapping(value = "/test/",  method =  {RequestMethod.GET}) 
		public String doTest(@Valid @ModelAttribute("tpatoolkitAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get("tpatoolkit_test");//if input forward not //available, provided default
		       }
			}
	

		if ( logger.isDebugEnabled() )
			logger.debug(TpaToolKitController.class.getName()+":forwarding to Tpa Tool Kit Page.");


		return forwards.get("tpatoolkit_test");
	}	
	 
	 @RequestMapping(value = "/tpaAdministrationGuide/",  method =  {RequestMethod.GET}) 
		public String doTpaAdmin(@Valid @ModelAttribute("tpatoolkitAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get("tpatoolkit_tpaAdmin");//if input forward not //available, provided default
		       }
			}
	

		if ( logger.isDebugEnabled() )
			logger.debug(TpaToolKitController.class.getName()+":forwarding to Tpa Tool Kit Page.");


		return forwards.get("tpatoolkit_tpaAdmin");
	}					 				
	 
	 @RequestMapping(value = "/tpaAuditPackage/",  method =  {RequestMethod.GET}) 
		public String doTpaAudit(@Valid @ModelAttribute("tpatoolkitAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get("tpatoolkit_tpaAudit");//if input forward not //available, provided default
		       }
			}
	

		if ( logger.isDebugEnabled() )
			logger.debug(TpaToolKitController.class.getName()+":forwarding to Tpa Tool Kit Page.");


		return forwards.get("tpatoolkit_tpaAudit");
	}				
	 
	 @RequestMapping(value = "/tpaOtherGuideTools/",  method =  {RequestMethod.GET}) 
		public String doTpaOther(@Valid @ModelAttribute("tpatoolkitAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get("tpatoolkit_tpaOther");//if input forward not //available, provided default
		       }
			}
	

		if ( logger.isDebugEnabled() )
			logger.debug(TpaToolKitController.class.getName()+":forwarding to Tpa Tool Kit Page.");


		return forwards.get("tpatoolkit_tpaOther");
	}				
	 
	protected String createLayoutBean(HttpServletRequest request,
			String forward) {
		
		//Special code required for partial NY
		//For NY, the Tools menu  does not have APEX  option in sub-menu
		//US Tools menu has 4 sub-menus including APEX.
		//Therefore, we must reset the submenu for the QIG page
		//from 4 (in a full navbar) to 3.
	
		
		LayoutBean bean = null;

		//get the current LayoutBean
		if (forward != null) {
			bean = LayoutBeanRepository.getInstance().getPageBean(
					forward);
		}

		
		//if "NY", clone current bean, forward to page,  and save cloned bean in request
		boolean isNY ="NY".equalsIgnoreCase(Environment.getInstance().getSiteLocation());
		if (isNY) {
			if(bean !=null)
			{
				LayoutBean cloneBean = (LayoutBean) bean.clone();
			String submenu = cloneBean.getSubmenu();
			
			if("3".equals(submenu))
					cloneBean.setSubmenu("2");
			if("4".equals(submenu))
				cloneBean.setSubmenu("3");
			if (cloneBean != null){
				forward = forwards.get(cloneBean.getLayoutURL());
				request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
			}	
			}
		}	

		
		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).		

		//DEFAULT FORWARD - if NOT NY 
		//(not the condition we handled above) and if the bean is NOT null
		//forward to page and save the orginal bean in request.
		if ((!isNY) && (bean != null)) {
			forward = forwards.get(bean.getLayoutURL());
			request.setAttribute(Constants.LAYOUT_BEAN, bean);
		}

		
		return forward;
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */  
	 @Autowired
	   private PSValidatorFWTpaToolKit  psValidatorFWTpaToolKit;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpaToolKit);
	}
	
}