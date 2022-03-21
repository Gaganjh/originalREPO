package com.manulife.pension.bd.web.proxy;

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

import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * 
 * This class acts as a struts pass through to the jhNavigator proxy jsp. It is used by the Find Literature landing
 * page's Search Engine that Marketing added. The point of the proxy is to allow their AJAX to talk to our servlet
 * jhNavigator.jsp. This servlet sends the parameters provided to the actual JHNavigator site and gets the response
 * which it sends back to the caller.
 * 
 * @author Mark Eldridge
 * 
 */
@Controller
@RequestMapping(value ="/jhNavigator")

public class JHNavigatorController extends BDController {
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{ 
	return	new DynaForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("jhNavigator","/WEB-INF/proxy/jhNavigator.jsp");
		forwards.put("error","/home/public_home.jsp");}  

	
    private static final String JH_NAVIGATOR_PROXY_PATH = "jhNavigator";

    public JHNavigatorController() {
        super(JHNavigatorController.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.apache.struts.action.ActionMapping,
     * org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			forwards.get("fail");//if input forward not //available, provided default
    		}
    	}
    	return forwards.get(JH_NAVIGATOR_PROXY_PATH);
    }

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
    @Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
}
