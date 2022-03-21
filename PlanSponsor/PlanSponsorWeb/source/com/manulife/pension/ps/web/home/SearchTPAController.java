package com.manulife.pension.ps.web.home;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.validation.Valid;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tpabob.TPABlockOfBusinessEntryForm;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.TPAUsersReportData;

/**
 * SearchTPAAction class This class is used to get the TPA report details for
 * search TPA page.
 * 
 * @author Ramamohan Gulla
 */

@Controller
@RequestMapping( value = "/home")
@SessionAttributes({"searchTPAForm"})


public class SearchTPAController extends ReportController {


	@ModelAttribute("searchTPAForm")
	public SearchTPAForm populateForm() {
		return new SearchTPAForm();
	}
	@ModelAttribute("tpaBlockOfBusinessEntryForm")
	public TPABlockOfBusinessEntryForm populateEntryForm() {
		return new TPABlockOfBusinessEntryForm();
	}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/home/searchTPA.jsp"); 
		forwards.put("default","/home/searchTPA.jsp"); 
		forwards.put("sort","/home/searchTPA.jsp");
		forwards.put("filter","/home/searchTPA.jsp"); 
		forwards.put("page","/home/searchTPA.jsp");
		}

	public static final String RESULTS_MESSAGE_KEY = "resultsMessageKey";
	private final RegularExpressionRule tpaUserNameRERule = new RegularExpressionRule(
			ErrorCodes.TPA_USERNAME_VIOLATE_NAMING_STANDARD,
			Constants.FIRST_NAME_LAST_NAME_RE);

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return TPAUsersReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		Exception ex = new UnsupportedOperationException(
				"Downloading data is not supported.");
		throw new SystemException(ex, getClass().getName(), "Downloading data",
				ex.getMessage());
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return TPAUsersReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return TPAUsersReportData.SORT_FIELD_LAST_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		SearchTPAForm theForm = (SearchTPAForm) form;

		criteria.addFilter(TPAUsersReportData.FILTER_FILTER,
				theForm.getFilter());
		criteria.addFilter(ManageUsersReportData.FILTER_VALUE,
				theForm.getFilterValue());
		criteria.addFilter(ManageUsersReportData.FILTER_PRINCIPAL,
				getUserProfile(request).getPrincipal());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}

	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@Autowired
	private SearchTpaValidator searchTpaValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(searchTpaValidator);
	}
	
	/**
	 * This method is overrided not to reset the form.
	 */
	protected BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		return reportForm;
	}

	/**
	 * This method is overridden in order to handle search,sort,page tasks.
	 *
	 * @see doCommon() in ReportAction
	 */
	 
	protected String doCommon(BaseReportForm actionForm,  HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		String forward;
		SearchTPAForm form = (SearchTPAForm) actionForm;
		request.removeAttribute(Constants.REPORT_BEAN);

		// If the task is searching TPA
		if ("search".equals(request.getParameter("search"))) {
			if (StringUtils.isEmpty(form.getFilter())
					|| StringUtils.isEmpty(form.getFilterValue())) {
				// Puts an empty ReportData into the request.
				TPAUsersReportData reportData = new TPAUsersReportData();
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				form.setStoredProcExecute(Constants.NO);
				forward = forwards.get("input");
			} else {
				form.setSortDirection("asc");
				form.setSortField("sortByLastName");
				form.setPageNumber(1);
				forward = super
						.doCommon( actionForm, request, response);
				form.setStoredProcExecute(Constants.YES);
			}
			// If the task is sorting
		} else if ("sort".equals(request.getParameter("task"))) {
			String temp = request.getParameter("sortField");
			if (temp != null && temp.length() != 0)
				form.setSortField(temp);

			temp = request.getParameter("sortDirection");
			if (temp != null && temp.length() != 0)
				form.setSortDirection(temp);

			form.setPageNumber(1);
			forward = super.doCommon( actionForm, request, response);

			// If the task is paging.
		} else if ("page".equals(request.getParameter("task"))) {
			String temp = request.getParameter("pageNumber");
			if (temp != null && temp.length() != 0)
				form.setPageNumber(Integer.parseInt(temp));
			else
				form.setPageNumber(1);

			forward = super.doCommon( actionForm, request, response);

		} else {
			TPAUsersReportData reportData = new TPAUsersReportData();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			forward = forwards.get("default");
		}

		return forward;
	}

	@RequestMapping(value = {"/searchTPA/","/searchTPAName/"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			TPAUsersReportData reportData = new TPAUsersReportData();
			request.setAttribute(Constants.REPORT_BEAN, reportData);   
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	    	   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
        	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		       String forward=super.doDefault( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	@RequestMapping(value ={"/searchTPA/","/searchTPAName/"} ,params={"action=filter","task=filter"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
    
    	 @RequestMapping(value ={"/searchTPA/","/searchTPAName/"} ,params={"action=page","task=page"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    	    public String doPage (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    throws IOException,ServletException, SystemException {
    	    	if(bindingResult.hasErrors()){
    		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		       if(errDirect!=null){
    		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		       }
    			}
    		       String forward=super.doPage( form, request, response);
    				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    	 }
    	
    		 @RequestMapping(value ={"/searchTPA/","/searchTPAName/"} ,params={"action=sort","task=sort"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
     	    public String doSort (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
     	    throws IOException,ServletException, SystemException {
     	    	if(bindingResult.hasErrors()){
     		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
     		       if(errDirect!=null){
     		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
     		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
     		       }
     			}
     		       String forward=super.doSort( form, request, response);
     				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
     	 }
    		 @RequestMapping(value ={"/searchTPA/","/searchTPAName/"} ,params={"action=download","task=download"}  , method =  {RequestMethod.POST,RequestMethod.GET})	
    	 public String doDownload (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    			     throws IOException,ServletException, SystemException {
    			     	   if(bindingResult.hasErrors()){
    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    			     		     if(errDirect!=null){
    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			    return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    			     		    }
    			     }
    			     String forward=super.doDownload( form, request, response);
    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    			    }
    	
    		 @RequestMapping(value ={"/searchTPA/","/searchTPAName/"} ,params={"action=downloadAll","task=downloadAll"}  , method =  {RequestMethod.POST,RequestMethod.GET})
    		 public String doDownloadAll (@Valid @ModelAttribute("searchTPAForm") SearchTPAForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    			     throws IOException,ServletException, SystemException {
    			     	   if(bindingResult.hasErrors()){
    			     		     String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    			     		     if(errDirect!=null){
    			     		      request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			    return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    			     		    }
    			     }
    			     String forward=super.doDownloadAll( form, request, response);
    			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    			    }  	

}
