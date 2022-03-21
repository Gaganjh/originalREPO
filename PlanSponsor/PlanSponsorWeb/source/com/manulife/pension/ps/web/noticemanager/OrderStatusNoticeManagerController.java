package com.manulife.pension.ps.web.noticemanager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.notice.valueobject.ContractMailingOrderReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * 
 * This is the action class for the Order Status page. 
 * In this page user can see the complete status of the order it is given from BuildYourPackage Page
 * 
 * @author Tamilarasu Krishnamoorthy
 *
 */
@Controller
@RequestMapping(value ="/noticemanager")

public class OrderStatusNoticeManagerController extends ReportController {
	
	@ModelAttribute("orderStatusForm")
	public OrderStatusNoticeManagerForm populateForm()
	{
		return new OrderStatusNoticeManagerForm();
	}

	
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/noticemanager/orderStatus.jsp");
		forwards.put("default","/noticemanager/orderStatus.jsp");
		forwards.put("secureHomePage","redirect:/do/home/homePage/");
		}

	private static final String DEFAULT_SORT_FIELD = ContractMailingOrderReportData.ORDER_STATUS_DATE;

	private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	private static final String LOGGED = "ORDER_STATUS_PAGE";

	@RequestMapping(value ="/orderstatus/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("orderStatusForm") OrderStatusNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }  
		String forward = super.doDefault(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
	@RequestMapping(value ="/orderstatus/" ,params= {"task=sort"},method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("orderStatusForm") OrderStatusNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }  
		String forward = super.doSort(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
	@RequestMapping(value ="/orderstatus/" ,params= {"task=page"},method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("orderStatusForm") OrderStatusNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }  
		String forward = super.doPage(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
	/**
	 * This is the method executed as default action method.
	 * 
	 * @param reportForm
	 *            BaseReportForm objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * @param response
	 *            HttpServletResponse objects reference
	 * 
	 * @return String-,
	 */
	
	 
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {


		UserProfile userProfile = getUserProfile(request);
		NoticeManagerUtility.getNoticeManagerTabSelection(userProfile, request, reportForm);
		OrderStatusNoticeManagerForm actionForm = (OrderStatusNoticeManagerForm)reportForm;
		
		actionForm.setUploadAndSharePageInd(false);
		request.getSession(false).setAttribute("OrderStatusPageInd", "true");
		if(!actionForm.getOrderStatusTab()){
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		try {
			if  (NoticeManagerUtility.validateProductRestriction(userProfile
					.getCurrentContract())
					|| NoticeManagerUtility.validateContractRestriction(userProfile
							.getCurrentContract())
					|| NoticeManagerUtility.validateDIStatus(
							userProfile.getCurrentContract(), userProfile.getRole()))  {
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		} catch (ContractDoesNotExistException ex) {
			throw new SystemException (ex, "Failed to retrieve contract details");
		}
		
		super.doCommon(reportForm, request, response);
		
	    return forwards.get("default");

	}
	@Override
	protected String getReportId() {
		return ContractMailingOrderReportData.REPORT_ID;	
	}

	@Override
	protected String getReportName() {
		return ContractMailingOrderReportData.REPORT_NAME;
	}

	/**
	 * Get  the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
					throws SystemException {
		return null;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(ContractMailingOrderReportData.FILTER_CONTRACT_NUMBER,
				userProfile.getCurrentContract().getContractNumber());
		request.getParameter(TASK_KEY);

		criteria.addFilter(ContractMailingOrderReportData.FILTER_TASK,
				ContractMailingOrderReportData.TASK_PRINT);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * If it's sort by Mailing Name, secondary sort is order number. 
	 * If it's sort by Number, secondary sort is mailing name.
	 * 
	 */
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String sortField = form.getSortField();
		String sortDirection = form.getSortDirection();

		criteria.insertSort(sortField, sortDirection);
		if (sortField.equals(
				ContractMailingOrderReportData.MAILING_NAME)) {
			
			// secondary sort is by Number
						criteria.insertSort(
								ContractMailingOrderReportData.ORDER_STATUS_DATE,
								sortDirection);
						
						criteria.insertSort(
								ContractMailingOrderReportData.ORDER_NUMBER,
								sortDirection);
		}else if (sortField.equals(
				ContractMailingOrderReportData.ORDER_STATUS)) {
			
			// secondary sort is by Number
						criteria.insertSort(
								ContractMailingOrderReportData.ORDER_STATUS_DATE,
								sortDirection);
						criteria.insertSort(
								ContractMailingOrderReportData.ORDER_NUMBER,
								sortDirection);
						
		} 
		else if (sortField.equals(
				ContractMailingOrderReportData.ORDER_STATUS_DATE)) {
			
			// NMOS 17 & 18.
			criteria.insertSort(
					ContractMailingOrderReportData.ORDER_NUMBER,
					sortDirection);
		}
	}
	/**
	 * Adds the page log information
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected void postExecute(OrderStatusNoticeManagerForm actionForm, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
	  HttpSession session = request.getSession(false);
		  super.postExecute( actionForm, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId =userProfile.getCurrentContract().getContractNumber();
		  String userAction =CommonConstants.ORDER_STATUS_PAGE;
		  if(session.getAttribute(LOGGED)==null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			  session.setAttribute(LOGGED, "VISITED");
		  }
	  }
	  
	  /**
	   * This code has been changed and added to Validate form and request against
	   * penetration attack, prior to other validations.
	   */

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
}

