package com.manulife.pension.ps.web.messagecenter.history;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.history.MCHistoryUtils.MessageStatus;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.message.report.valueobject.MessageHistoryReportData;
import com.manulife.pension.service.message.valueobject.MessageRecipient.RecipientStatus;
import com.manulife.pension.service.message.valueobject.MessageTemplate;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

@Controller
@RequestMapping(value = "/messagecenter")
@SessionAttributes({ "messageHistoryForm" })

public class MCMessageHistoryController extends ReportController {
	@ModelAttribute("messageHistoryForm")
	public MCMessageHistoryForm populateForm() {
		return new MCMessageHistoryForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/messagecenter/message_history.jsp");
		forwards.put("default", "/messagecenter/message_history.jsp");
		forwards.put("filter", "/messagecenter/message_history.jsp");
		forwards.put("sort", "/messagecenter/message_history.jsp");
		forwards.put("page", "/messagecenter/message_history.jsp");
		forwards.put("print", "/messagecenter/message_history.jsp");
	}

	
	public String doExecute( MCMessageHistoryForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws  SystemException {

		if (!(getUserProfile(request).getRole().isExternalUser() || getUserProfile(request).isBundledGACAR())) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		if (!MCEnvironment.isMessageCenterAvailable(request)) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		return null;
	}

	private static Logger log = Logger.getLogger(MCMessageHistoryController.class);

	private static final String[] SortFieldOrder = { MessageHistoryReportData.SORT_FIELD_EFFECTIVE_TS,
			MessageHistoryReportData.SORT_FIELD_PRIORITY, MessageHistoryReportData.SORT_FIELD_SHORT_TEXT };

	private static final String[] SortDirectionOrder = { ReportSort.DESC_DIRECTION, ReportSort.DESC_DIRECTION,
			ReportSort.ASC_DIRECTION };

	@RequestMapping(value ="/history", method = {RequestMethod.POST})
	public String execute(MCMessageHistoryForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String task = getTask(request);
		if ("POST".equalsIgnoreCase(request.getMethod()) && !"unhide".equals(task) && !"undoComplete".equals(task)) {
			log.debug("Post for filter/sorting is redirected as get");
			ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request), true);

			return forward.getPath();
		}
		return null;
	}

	@Override
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		if (SessionHelper.getUserProfile(request).isInternalUser()
				&& !SessionHelper.getUserProfile(request).isBundledGACAR()) {
			// needs to remove the Priority sorting
			ReportSortList sortList = reportCriteria.getSorts();
			for (int i = 0; i < sortList.size(); i++) {
				if (sortList.get(i).getSortField().equals(MessageHistoryReportData.SORT_FIELD_PRIORITY)) {
					sortList.remove(i);
					break;
				}
			}
		}
		return super.getReportData(reportId, reportCriteria, request);
	}

	@Override
	protected String getDefaultSort() {
		return MessageHistoryReportData.SORT_FIELD_EFFECTIVE_TS;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	@Override
	protected String getReportId() {
		return MessageHistoryReportData.REPORT_ID;
	}

	@Override
	protected BaseReportForm resetForm(BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		MCMessageHistoryForm form = (MCMessageHistoryForm) reportForm;
		if (getUserProfile(request).getCurrentContract() != null) {
			form.setContractId(
					Integer.toString(SessionHelper.getUserProfile(request).getCurrentContract().getContractNumber()));
		}
		return form;
	}

	@Override
	protected String getReportName() {
		return MessageHistoryReportData.REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) throws SystemException {

		MCMessageHistoryForm f = (MCMessageHistoryForm) form;

		String lastName = f.getLastName();
		UserProfile profile = SessionHelper.getUserProfile(request);

		criteria.addFilter(MessageHistoryReportData.FILTER_APPLICATION_ID, Environment.getInstance().getAppId());

		if (!profile.isInternalUser() || profile.isBundledGACAR()) {
			criteria.addFilter(MessageHistoryReportData.FILTER_USER_PROFILE_ID, profile.getPrincipal().getProfileId());
		}

		Set<Integer> contractIds = new HashSet<Integer>();
		contractIds.addAll(profile.getMessageCenterAccessibleContracts());
		String contractId = f.getContractId();
		if (StringUtils.isNotEmpty(contractId) && contractIds.contains(new Integer(contractId))) {
			contractIds.clear();
			contractIds.add(new Integer(contractId));
		}
		criteria.addFilter(MessageHistoryReportData.FILTER_CONTRACT_IDS, contractIds);

		if (MCUtils.isInGlobalContext(request) && profile.getRole().isTPA() && StringUtils.isEmpty(contractId)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_TPA_FIRM_IDS, profile.getMessageCenterTpaFirms());
		}

		if (!StringUtils.isEmpty(lastName)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_LAST_NAME, lastName);
		}

		String ssn = f.getSsn();
		if (!StringUtils.isEmpty(ssn)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_SSN, ssn);
		}
		String tab = f.getTab();
		String section = f.getSection();
		String type = f.getType();

		if (!StringUtils.isEmpty(section)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_SECTION_ID, Integer.parseInt(section));
		}

		if (!StringUtils.isEmpty(tab) && StringUtils.isEmpty(section)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_TAB_ID, Integer.parseInt(tab));
		}

		if (!StringUtils.isEmpty(type)) {
			criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_TEMPLATE_ID, Integer.parseInt(type));
		}

		if (!StringUtils.isEmpty(f.getSubmissionId())) {
			criteria.addFilter(MessageHistoryReportData.FILTER_SUBMISSION_ID, Integer.parseInt(f.getSubmissionId()));
		}

		if (f.getFromDateAsDate() != null) {
			criteria.addFilter(MessageHistoryReportData.FILTER_FROM_DATE, f.getFromDateAsDate());
		}
		if (f.getToDateAsDate() != null) {
			criteria.addFilter(MessageHistoryReportData.FILTER_TO_DATE, f.getToDateAsDate());
		}

		if (StringUtils.isNotEmpty(f.getMessageStatusForHistory())) {
			String code = f.getMessageStatusForHistory();
			if (StringUtils.equals(code, MessageStatus.DECLAREDCOMPLETE.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_TYPE_CODE,
						MessageTemplate.MessageActionType.DECLARE_COMPLETE.getValue());
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.DONE.getValue());

			} else if (StringUtils.equals(code, MessageStatus.EXPIRED.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.EXPIRED.getValue());

			} else if (StringUtils.equals(code, MessageStatus.ESCALATED.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.ESCALATED.getValue());

			} else if (StringUtils.equals(code, MessageStatus.REPLACED.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.REPLACED.getValue());

			} else if (StringUtils.equals(code, MessageStatus.OBSOLETED.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.OBSOLETE.getValue());

			} else if (StringUtils.equals(code, MessageStatus.COMPLETEDONLINE.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.COMPLETED.getValue());

			} else if (StringUtils.equals(code, MessageStatus.REMOVEDFROMVIEW.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_TYPE_CODE,
						MessageTemplate.MessageActionType.FYI.getValue());
				criteria.addFilter(MessageHistoryReportData.FILTER_MESSAGE_STATUS,
						com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE.getValue());
				criteria.addFilter(MessageHistoryReportData.FILTER_RECIPIENT_STATUS, RecipientStatus.HIDDEN.getValue());

			} else if (StringUtils.equals(code, MessageStatus.REMOVEDBYSTILLACTIVE.getValue())) {
				criteria.addFilter(MessageHistoryReportData.FILTER_REMOVED_STILL_ACTIVE, "dummy, not used"); // means
																												// hidden
			}
		}
	}

	@Override
	protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
		String sortDir = form.getSortDirection();
		String sortField = form.getSortField();
		// if no sort field is in the form, use the default one
		if (StringUtils.isEmpty(sortField)) {
			sortField = getDefaultSort();
			sortDir = getDefaultSortDirection();
		}
		// find the index of the selected sortField
		criteria.insertSort(sortField, sortDir);
		for (int i = 0; i < SortFieldOrder.length; i++) {
			// the selected field/dir is the first one
			// the rest keep the same.
			if (!sortField.equals(SortFieldOrder[i])) {
				criteria.insertSort(SortFieldOrder[i], SortDirectionOrder[i]);
			}
		}
	}

	/*@Autowired
	private MCMessageHistoryValidator mCMessageHistoryValidator;*/
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

	@RequestMapping(value = "/history", params = {"task=unhide"},method={RequestMethod.GET,RequestMethod.POST})
	public String doUnhide(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		int messageId = Integer.parseInt(actionForm.getMessageId());

		MessageServiceFacadeFactory.getInstance(request.getServletContext()).unRemoveMessage(SessionHelper.getUserProfile(request),
				messageId);
		//ControllerForward forward = new ControllerForward("refresh", "/do"
			//	+ new UrlPathHelper().getPathWithinApplication(request) + "?task=filter", true);
		//return forward.getPath();
		return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=filter";
	}

	@RequestMapping(value ="/history", params = {"task=undoComplete"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String doUndoComplete(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

		int messageId = Integer.parseInt(actionForm.getMessageId());

		MessageServiceFacadeFactory.getInstance(request.getServletContext()).undoCompleteMessage(SessionHelper.getUserProfile(request),
				messageId);
		
		//ControllerForward forward = new ControllerForward("refresh", "/do"
		//	+ new UrlPathHelper().getPathWithinApplication(request) + "?task=filter", true);
		//return forward.getPath();
		return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=filter";
		
		
	}
	
	@RequestMapping(value ="/history",  method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

	String forward = super.doDefault(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	
	@RequestMapping(value ="/history",  params = {"task=filter"},  method = {RequestMethod.GET,RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		String task = getTask(request);
		if ("POST".equalsIgnoreCase(request.getMethod()) && !"unhide".equals(task) && !"undoComplete".equals(task)) {
			log.debug("Post for filter/sorting is redirected as get");
			//return new ControllerForward("/do" + new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request), true).getPath();
			ControllerForward forward = new ControllerForward("refresh",
       				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
       		return "redirect:" + forward.getPath();
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
		Collection error = doValidate(actionForm, request);
		if(error.size()>0) {
			return forwards.get("input");
		}
	String forward = super.doFilter(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	
	@RequestMapping(value ="/history",  params = {"task=page"},  method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(Constants.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
	String forward = super.doPage(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	@RequestMapping(value ="/history",  params = {"task=sort"},  method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(Constants.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
	String forward = super.doSort(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	@RequestMapping(value ="/history",  params = {"task=download"},  method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
	String forward = super.doDownload(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	@RequestMapping(value ="/history",  params = {"task=downloadAll"},  method = {RequestMethod.GET})
	public String doDownloadAll(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
	String forward = super.doDownloadAll(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	
	@RequestMapping(value ="/history",  params = {"task=print"},  method = {RequestMethod.GET})
	public String doPrint(@Valid @ModelAttribute("messageHistoryForm") MCMessageHistoryForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws SystemException  {
		String defaultForward = doExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(defaultForward)) {
			return StringUtils.contains(defaultForward, '/') ? defaultForward : forwards.get(defaultForward); 
		}
		if (bindingResult.hasErrors()) {
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			MessageHistoryReportData reportData = new MessageHistoryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
	String forward = super.doPrint(actionForm, request, response);
	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
		
	}
	protected Collection doValidate(ActionForm form,
			HttpServletRequest request) {
		Collection<GenericException> errors = super.doValidate( form,
				request);
			MCMessageHistoryForm f = (MCMessageHistoryForm) form;
			//must validate the contract id here since MCMessageHistoryForm is used by more than action,
			//and this is only applicable for the history portion
			if ( StringUtils.isNotEmpty(f.getContractId())) {
				try {
				Integer contractId = new Integer(f.getContractId()); 
				if(!getUserProfile(request).getMessageCenterAccessibleContracts().contains(contractId) ) {
					errors.add(new ValidationError("contractId", ErrorCodes.CONTRACT_NUMBER_INVALID ));
				}
				} catch (NumberFormatException e ) {
					errors.add(new ValidationError("contractId", ErrorCodes.CONTRACT_NUMBER_INVALID ));
				}
			}
			
			f.validate(errors);
			if (errors.size() > 0) {
				f.sortErrors(errors);
				MessageHistoryReportData reportData = new MessageHistoryReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				SessionHelper.setErrorsInSession(request, errors);
			}
		return errors;
	}
	
	
	
}
