package com.manulife.pension.ps.web.news;

import java.io.IOException;

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
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNewsIssue;

/**
 * Newsletter Issue Article page Action class 
 * This class is used to forward the users's request to 
 * the Newsletter Issue Article page
 * 
 * @author Mabel Au
 */

@Controller
@RequestMapping(value ="/news")
public class NewsletterIssueArticleController extends PsController
{ 
	@ModelAttribute("newsletterIssueArticleAction") 
	public DynaForm populateForm() 
	{
		return new DynaForm();
	}
	
	private static final String NEWSLETTER_ISSUE_ARTICLE_PAGE = "/news/newsletterIssueArticle.jsp";
	
	public NewsletterIssueArticleController() 
	{
		super(NewsletterIssueArticleController.class);
	} 
	
	@RequestMapping(value ="/newsletterIssueArticle" , method =  {RequestMethod.GET})
	public String doExecute (@Valid @ModelAttribute("newsletterIssueArticleAction") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              	return NEWSLETTER_ISSUE_ARTICLE_PAGE;//if input forward not //available, provided default
	       }
		}
	
		if ( logger.isDebugEnabled() )
			logger.debug(NewsletterIssueArticleController.class.getName()+":forwarding to Newsletter Issue Article Page.");
			
		return NEWSLETTER_ISSUE_ARTICLE_PAGE;	}			

	/* (non-Javadoc)
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations.
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	
	@Autowired
	   private PSValidatorFWNewsIssue  psValidatorFWNewsIssue;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWNewsIssue);
	}
}
