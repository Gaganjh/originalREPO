package com.manulife.pension.ps.web.messagecenter;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.model.MCPreference;
import com.manulife.pension.ps.web.messagecenter.model.MCPreferencesHolder;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGeneratorFactory;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWMCDispatch;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;

/**
 * The Entry action from outside of Message Center.  It will redirect to either
 * a specific business tab when it was selected last time or to the summary tab
 * in all other cases.
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping( value ="/mcdispatch")

public class MCDispatchController extends PsController implements MCConstants {

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("carview","redirect:/do/mcCarView");
		forwards.put("carview_global","redirect:/do/mcCarView/global");
	}
	

	public MCDispatchController() {
		super(MCDispatchController.class);
	}
	@RequestMapping(value= {"","/global"},method= {RequestMethod.GET,RequestMethod.POST})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("carview");//if input forward not //available, provided default
	       }
		}
		
		UserProfile profile = SessionHelper.getUserProfile(request);
		
		if (profile.getRole().isInternalUser()) {
			String path =new UrlPathHelper().getPathWithinServletMapping(request);
			if (SessionHelper.getMCLeftMCFromGlobalContext(request) != null
					|| path.contains( MCConstants.mappingParameterGlobal)) {
				profile.setContractProfile(null);
				SessionHelper.clearSession(request);
				return forwards.get("carview_global");
			}
			//return mapping.findForward("carview");
					return forwards.get("carview");
		}
		MCPreferencesHolder userState = MCUtils.getPreferencesHolder(request);
		
		MCPreference gpreference = userState.getGlobalPreference();
		MCPreference preference = null;
		if (MCUtils.isInGlobalContext(request) || SessionHelper.getMCLeftMCFromGlobalContext(request) != null) {
			// go back to global context
			preference = gpreference;
			profile.setCurrentContract(null);
			SessionHelper.clearSession(request);
		} else {
			preference = userState.getContractPreference(profile
					.getCurrentContract().getContractNumber());
		}
		
		MessageCenterComponentId selectedTabId = preference.getSelectedTabId();
		MessageCenterComponentId selectedSectionId = preference
				.getSelectedSectionId();		
		preference.clearSelection();

		String url;
		if (selectedSectionId != null) {
			url = MCUrlGeneratorFactory.getInstance().getDetailSectionUrl(
					selectedTabId, selectedSectionId);
		} else {
			url = MCUrlGeneratorFactory.getInstance().getTabUrl(selectedTabId);
		}

		ControllerRedirect forward = new ControllerRedirect(url);
		return "redirect:"+forward.getPath();
	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	
	
	@Autowired
	   private PSValidatorFWMCDispatch  psValidatorFWMCDispatch;

	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWMCDispatch);
	}
	
}
