
package com.manulife.pension.ps.web.resources;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;

/**
 * Form list Action class
 * gets required value objects , forwards to quarterlyInvestmentGuide.jsp
 * PlanSponsor.
 *
 * @author Chris Shin
 * Jan 2004
 */




@Controller
@RequestMapping( value ="/resources")
public class QuarterlyInvestmentGuideController extends PsController {

	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("quarterlyInvestmentGuide","/resources/quarterlyInvestmentGuide.jsp");
	
	}
	

	public QuarterlyInvestmentGuideController()
	{
		super(QuarterlyInvestmentGuideController.class);
	}

	
	
	@RequestMapping(value ="/quarterlyInvestmentGuide/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		UserProfile userProfile = getUserProfile(request);

		// if there're no current contract, forward them to the home page finder
		if(userProfile.getCurrentContract()==null)
			return Constants.HOMEPAGE_FINDER_FORWARD;

		return forwards.get("quarterlyInvestmentGuide");
    }


	protected String createLayoutBean(HttpServletRequest request,
			String forward) {
		
		//Special code required for partial sub-menu on MTA
		//For MTA, the Resources menu has a partial sub-menu - Tools and Quarterly Investment Guide only.
		//Full Resources menu has 5 sub-menus and the QIG submenu is in position 5.
		//Therefore, we must reset the submenu for the QIG page
		//from 5 (in a full navbar) to 2.
	
		
		LayoutBean bean = null;

		//get the current LayoutBean
		if (forward != null) {
			bean = LayoutBeanRepository.getInstance().getPageBean(
					forward);
		}
		
		
		
		// get the user profile object to determine is the contract is MTA
		UserProfile userProfile = getUserProfile(request);
		boolean isMta = userProfile.getContractProfile().getContract().isMta();
	
		
		//if MTA contract, clone current bean, forward to page,  and save cloned bean in request
		if (isMta) {
			LayoutBean cloneBean = (LayoutBean) bean.clone();
			cloneBean.setSubmenu("2");
			if (cloneBean != null){
				forward = forwards.get(cloneBean.getLayoutURL());
				request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
			}	
		}	

		
		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).		

		//DEFAULT FORWARD - if NOT MTA contract
		//(not the condition we handled above) and if the bean is NOT null
		//forward to page and save the orginal bean in request.
		if ((!isMta) && (bean != null)) {
			forward = forwards.get(bean.getLayoutURL());
			request.setAttribute(Constants.LAYOUT_BEAN, bean);
		}

		return forward;
	}

}
