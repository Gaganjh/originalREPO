package com.manulife.pension.bd.web.tools;

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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.DynaForm;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Redirects to the ERISA page
 * 
 * @author Mark Eldridge
 * 
 */
@Controller
@RequestMapping(value ="/Erisa")

public class ErisaRedirectController extends BDController {
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("fail","/do/home/");
	}
	
    private static final String RESPONSE_TYPE = "text/html";

    private static final String LOCATION = "Location";

    /**
     * Constructor.
     */
    public ErisaRedirectController() {
        super(ErisaRedirectController.class);
    }

    /**
     * @see BaseController#doExecute()
     */
    @RequestMapping(value = "/" ,method =  {RequestMethod.GET})
	 public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {	
   	if(bindingResult.hasErrors()){
   		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
   		if(errDirect!=null){
   			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
   			forwards.get("fail");//if input forward not //available, provided default
   		}
   	}
    if (logger.isDebugEnabled()) {
            logger.debug("Entry -> ERISA Redirect Action");
        }
        
        BaseEnvironment environment = new BaseEnvironment();
		String URL = environment.getNamingVariable(BDConstants.ERISA_ONLINE_URL, null);
		
        response.setContentType(RESPONSE_TYPE);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader(LOCATION, URL);

        if (logger.isDebugEnabled()) {
            logger.debug("Exit <- ERISA Redirect Action");
        }

        return null;
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    @Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
}
