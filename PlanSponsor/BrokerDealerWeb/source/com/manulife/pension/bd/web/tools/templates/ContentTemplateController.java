package com.manulife.pension.bd.web.tools.templates;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWContentTemplate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;

/**
 * 
 * Creating a common template jsp to load all static CMA related pages. 
 * 1) Funds & performance
 * 
 * @author aambrose
 */

@Controller
@RequestMapping( value = {"/publicFP","/contactUs","/partneringWithUs","/productsAndServices","/publicForms","/levelOneForms","/helpfulLinks","/recommendedSettings","/howDoI","/glossary","/siteMap"})
public class ContentTemplateController extends BDController {
	@ModelAttribute("contentTemplateForm") 
	public ContentTemplateForm populateForm() 
	{
		return new ContentTemplateForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("publicFP","/tools/templates/fundsAndPerformance.jsp");
		forwards.put("publicHome","/home/public_home.jsp");
		forwards.put("contactUs","/tools/templates/contactUs.jsp" );
		forwards.put("publicContactUs","/tools/templates/publicContactUs.jsp");
		forwards.put("securedHome","/do/secured/"); 
		forwards.put("fail","redirect:/do/home");
		forwards.put("partneringWithUs","/tools/templates/partneringWithUs.jsp");
		forwards.put("productsAndServices","/tools/templates/productsAndServices.jsp");
		forwards.put("publicForms","/tools/templates/publicForms.jsp");
		forwards.put("levelOneForms","/tools/templates/levelOneForms.jsp");
		forwards.put("helpfulLinks","/tools/templates/helpfulLinks.jsp");
		forwards.put("recommendedSettings","/tools/templates/recommendedSettings.jsp");
		forwards.put("howDoI","/tools/help/howDoI.jsp");
		forwards.put("glossary","/tools/help/glossary.jsp");
		forwards.put("siteMap","/tools/help/siteMap.jsp"); 

	}
	
	private static final String PUBLIC_CONTACT_US = "publicContactUs";

	private static final String SECURED_CONTACT_US = "contactUs";

	public ContentTemplateController() {
		super(ContentTemplateController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doExecute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("contentTemplateForm") ContentTemplateForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 forwards.get("publicHome");//if input forward not //available, provided default
			 }
		 }	

		String strForwarPath = BDConstants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		BDUserProfile bdUserProfile = BDSessionHelper.getUserProfile(request);
		strForwarPath = StringUtils.remove(new UrlPathHelper().getPathWithinApplication(request), "/WEB-INF/do/");
		strForwarPath = StringUtils.remove(strForwarPath, BDConstants.SLASH_SYMBOL);
		if (StringUtils.isNotBlank(new UrlPathHelper().getPathWithinApplication(request))) {
			if (bdUserProfile != null
					&& (StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request),
							PUBLIC_CONTACT_US) || StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request), SECURED_CONTACT_US))) {
				strForwarPath = SECURED_CONTACT_US;
			} else if (bdUserProfile == null
					&& (StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request),
							SECURED_CONTACT_US) || StringUtils.contains(new UrlPathHelper().getPathWithinApplication(request), PUBLIC_CONTACT_US))) {
				strForwarPath = PUBLIC_CONTACT_US;
			}

			actionForm.setCurrentPageName(strForwarPath);
		}
		
		BDSessionHelper.removeBOBTabSelectionFromSession(request);
		return forwards.get(strForwarPath);
	}
	
	@Autowired
	   private BDValidatorFWContentTemplate  bdValidatorFWContentTemplate;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWContentTemplate);
	}
	
	
}
