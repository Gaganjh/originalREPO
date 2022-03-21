package com.manulife.pension.bd.web.tools.salestools;

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

//import com.manulife.pension.delegate.TPAServiceDelegate;
//import com.manulife.pension.exception.ApplicationException;
//import com.manulife.pension.ps.web.Constants;
//import com.manulife.pension.ps.web.census.util.CensusUtils;
//import com.manulife.pension.ps.web.controller.PsAction;
//import com.manulife.pension.ps.web.pagelayout.LayoutBean;
//import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
//import com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi;
//import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
//import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
//import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
//import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
//import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;

import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWToolsMenu;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

@Controller
@RequestMapping(value ="/salesTools")

public class ToolsMenuController extends BDController {

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("toolsMenu","/tools/salestools/toolsMenu.jsp");
		}

	private static final String TOOLSMENU = "toolsMenu";

	public ToolsMenuController() {
		super(ToolsMenuController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.
	 * apache.struts.action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	 @RequestMapping(value = "/" ,method =  {RequestMethod.GET})
	 public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {	
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 forwards.get("toolsMenu");//if input forward not //available, provided default
			 }
		 }
		BDUserProfile userProfile = getUserProfile(request);
		return forwards.get("toolsMenu");
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	   private BDValidatorFWToolsMenu  bdValidatorFWToolsMenu;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWToolsMenu);
	}
}