package com.manulife.pension.ps.web.takeatour;

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
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTakeATour;
import com.manulife.pension.ps.web.withdrawal.BeforeProceedingForm;

/**
 * Take A Tour page Action class 
 * This class is used to forward the users's request to 
 * the Take A Tour page
 * 
 * @author Raja Krishna
 */

  
@Controller
@RequestMapping(value ="/takeatour")
public class TakeATourController extends PsController
{ 
	
	@ModelAttribute("withdrawalBeforeProceedingForm") 
	public BeforeProceedingForm populateForm() 
	{
		return new BeforeProceedingForm();
		
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{forwards.put("takeatour","/takeatour/takeATour.jsp");
	
	}
	
	
	private static final String TAKE_A_TOUR = "takeatour";
	
	public TakeATourController() 
	{
		super(TakeATourController.class);
	} 

	@RequestMapping(value ="/takeATour",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("withdrawalBeforeProceedingForm") BeforeProceedingForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("takeatour");//if input forward not //available, provided default
	       }
		}
	

		if ( logger.isDebugEnabled() )
			logger.debug(TakeATourController.class.getName()+":forwarding to Take A Tour Page.");


		return forwards.get(TAKE_A_TOUR);
	}						                
	/*protected ActionForward createLayoutBean(HttpServletRequest request,
			ActionForward forward, ActionMapping mapping) {
		
		//Special code required for partial NY
		//For NY, the Tools menu  does not have APEX  option in sub-menu
		//US Tools menu has 4 sub-menus including APEX.
		//Therefore, we must reset the submenu for the QIG page
		//from 4 (in a full navbar) to 3.
	
		
		LayoutBean bean = null;

		//get the current LayoutBean
		if (forward != null) {
			bean = LayoutBeanRepository.getInstance().getPageBean(
					forward.getPath());
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
				forward = mapping.findForward(cloneBean.getLayoutURL());
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
			forward = mapping.findForward(bean.getLayoutURL());
			request.setAttribute(Constants.LAYOUT_BEAN, bean);
		}

		return forward;
	}*/
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */  
	 @Autowired
	   private PSValidatorFWTakeATour  psValidatorFWTakeATour;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTakeATour);
	}
	
}