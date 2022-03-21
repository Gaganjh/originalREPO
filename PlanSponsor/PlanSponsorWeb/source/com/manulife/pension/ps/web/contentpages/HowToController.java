package com.manulife.pension.ps.web.contentpages;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWHowTo;
@Controller
@RequestMapping( value = "/contentpages")
 
public class HowToController extends PsController {

	public HowToController()
	{
		super(HowToController.class);
	} 
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
		}
	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("howTo","/contentpages/howTo.jsp");
	}
	@RequestMapping( value ="/howTo", method =  RequestMethod.GET)
		public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
				String pContetnKey = (String) request.getSession().getAttribute(Constants.PREVIOUS_CONTETN_KEY);
				String pind= (String) request.getSession().getAttribute(Constants.PREVIOUS_IND);
				String params= StringUtils.EMPTY ;
				if(StringUtils.isNotBlank(pContetnKey) || StringUtils.isNotBlank(pind)){
					params="?contentKey=" + pContetnKey + "&ind=" + pind;
				}
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("howTo");//if input forward not //available, provided default
		       }
			}
		
		
		
		String contentKey = (String) request.getParameter(Constants.CONTENT_KEY);
		String ind = ((String) request.getParameter(Constants.IND)).toLowerCase(); 
	    if (contentKey != null) {
		   request.getSession().setAttribute(Constants.PREVIOUS_CONTETN_KEY, contentKey);
		   request.getSession().setAttribute(Constants.PREVIOUS_IND, ind);
		}
	    return forwards.get("howTo");
	}

		
		@Autowired
		private PSValidatorFWHowTo  psValidatorFWHowTo;
		@InitBinder
		public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
		    binder.bind( request);
		    binder.addValidators(psValidatorFWHowTo);
		}
}
