package com.manulife.pension.bd.web.messagecenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDefault;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.message.report.valueobject.BDMessageReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.validator.ValidationError;

/**
 * The message center for BDW report action
 * 
 * @author guweigu
 *
 */
@Controller
@RequestMapping(value ="/messagecenter")
@SessionAttributes({"messageCenterForm"})

public class BDMessageCenterController extends BDReportController {

	@ModelAttribute("messageCenterForm") 
	public BDMessageCenterForm populateForm() 
	{
		return new BDMessageCenterForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/messagecenter/messagecenter.jsp");
		forwards.put("page","/messagecenter/messagecenter.jsp");
		forwards.put("refresh","/messagecenter/messagecenter.jsp");
		forwards.put("sort","/messagecenter/messagecenter.jsp"); 
		forwards.put("filter","/messagecenter/messagecenter.jsp");
		forwards.put("delete","redirect:/do/messagecenter/?task=refresh");
		}

	
	// The default sorting sequence
	private static final ReportSort[] DefaultSort = new ReportSort[] {
			new ReportSort(BDMessageReportData.SORT_FIELD_EFFECTIVE_DATE,
					ReportSort.DESC_DIRECTION),
			new ReportSort(BDMessageReportData.SORT_FIELD_CONTRACT_NAME,
							ReportSort.ASC_DIRECTION),
			new ReportSort(BDMessageReportData.SORT_FIELD_CONTRACT_NUMBER,
					ReportSort.ASC_DIRECTION),
			new ReportSort(BDMessageReportData.SORT_FIELD_SHORT_TEXT,
					ReportSort.ASC_DIRECTION), };

	private final ValidationError DeleteMessageError = new ValidationError("",
			BDErrorCodes.MC_NO_SELECTED_MESSAGE);

	public BDMessageCenterController() {
		super(BDMessageCenterController.class);
	}

	@Override
	protected String getDefaultSort() {
		return BDMessageReportData.SORT_FIELD_EFFECTIVE_DATE;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		throw new SystemException("Download is not supported");
	}

	@Override
	protected String getReportId() {
		return BDMessageReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return BDMessageReportData.REPORT_NAME;
	}

	@RequestMapping(value ="/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("messageCenterForm") BDMessageCenterForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
	       }
		}
		
		String task = getTask(request);

		if (StringUtils.equalsIgnoreCase("POST", request.getMethod()) && !StringUtils.equals("delete", task)) {
			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + task, true);
			return forward.getPath();
		}

		String forward=super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		criteria.addFilter(BDMessageReportData.FILTER_USER_PROFILE_ID,
				BDSessionHelper.getUserProfile(request).getBDPrincipal()
						.getProfileId());
		criteria.addFilter(BDMessageReportData.FILTER_SECTION_ID,
				BDMessageCenterFacade.getInstance().getBDMCSection().getId()
						.getValue());
	}

	@Override
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		String selectedSortField = form.getSortField();
		String selectedSortDir = form.getSortDirection();

		ReportSortList sortList = new ReportSortList();
		sortList.add(new ReportSort(selectedSortField, selectedSortDir));
		for (ReportSort s : DefaultSort) {
			if (!StringUtils.equals(s.getSortField(), selectedSortField)) {
				sortList.add(new ReportSort(s.getSortField(), s
						.getSortDirection()));
			}
		}
		criteria.setSorts(sortList);
	}

	// for testing purpose
	@Override
	protected int getPageSize(HttpServletRequest request) {
		return super.getPageSize(request);
	}

	/**
	 * Refresh the current search result
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/", params= {"task=refresh"} ,method =  {RequestMethod.GET}) 
	public String doRefresh(@Valid @ModelAttribute("messageCenterForm") BDMessageCenterForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("default");
			}
		}
		String task = getTask(request);

		if (StringUtils.equalsIgnoreCase("POST", request.getMethod()) && !StringUtils.equals("delete", task)) {
			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + task, true);
			return forward.getPath();
		}
	
		String forward = doCommon( actionForm, request, response);

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value ="/", params={"task=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("messageCenterForm") BDMessageCenterForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return  forwards.get("default");
			}
		}
		String task = getTask(request);

		if (StringUtils.equalsIgnoreCase("POST", request.getMethod()) && !StringUtils.equals("delete", task)) {
// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + task, true);
			return forward.getPath();
		}
    	String forward=super.doSort( actionForm, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
	/**
	 * Delete the selected message ids
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/", params= {"task=delete"} ,method =  {RequestMethod.POST}) 
	public String doDelete(@Valid @ModelAttribute("messageCenterForm") BDMessageCenterForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		String task = getTask(request);

		if (StringUtils.equalsIgnoreCase("POST", request.getMethod()) && !StringUtils.equals("delete", task)) {
			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + task, true);
			return forward.getPath();
		}
	
		// no delete is allowed for mimic mode
		if (BDMessageCenterUtils.isUnderMimic(request)) {
			return forwards.get("delete");
		}
		int[] messageIds = actionForm.getSelectedMessageIds();
		if (messageIds == null || messageIds.length == 0) {
			List<ValidationError> error = new ArrayList<ValidationError>(1);
			error.add(DeleteMessageError);
			setErrorsInSession(request, error);
		} else {
			BDMessageCenterFacade.getInstance().deleteMessages(
					BDSessionHelper.getUserProfile(request), messageIds);
		}
		return forwards.get("delete");
	}

	/**
	 * Override the super class implementation to check if the current
	 * page number is beyond the total page count from the report result.
	 * This could happen when delete operation happens before this.
	 */
	
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		
		String forward = super.doCommon( reportForm, request,
				response);
		ReportData data = (ReportData)request.getAttribute(CommonConstants.REPORT_BEAN);
		if (data != null) {
			// if the current page number in the Form is greater than
			// the total page count in the report, then set it to last
			// page of the page number
			if (data.getTotalPageCount() < reportForm.getPageNumber()) {
				reportForm.setPageNumber(data.getTotalPageCount());
			}
		} else {
			reportForm.setPageNumber(1);
		}
		return forward;
	}
	
	
	@Autowired
	private BDValidatorFWDefault bdValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWDefault);
	}
		
}
