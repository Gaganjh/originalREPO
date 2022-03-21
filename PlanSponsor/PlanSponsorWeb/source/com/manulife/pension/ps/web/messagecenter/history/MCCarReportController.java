package com.manulife.pension.ps.web.messagecenter.history;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.report.valueobject.MessageReportData;
import com.manulife.pension.service.message.report.valueobject.MessageReportDetails;
import com.manulife.pension.service.message.valueobject.MessageTemplate;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;

/*
 * As per status messages. 
 * 
 * NO recipient
 * 1. active means just check the message status
 * 2. archived means the opposite - not active
 * 
 * WITH recipient
 * 1. active means message status active and not removed
 * 2. archived means
 * 				-any of the archive status
 * 				-or message status active and removed. 
 * 
 * 
 * 
 * 
 */

@Controller
@RequestMapping(value ="/mcCarView")
@SessionAttributes({"carViewForm"})

public class MCCarReportController extends ReportController {

	@ModelAttribute("carViewForm")
	public MCMessageReportForm populateForm() {
		return new MCMessageReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input","/messagecenter/car_view.jsp");
		forwards.put("default","/messagecenter/car_view.jsp");
		forwards.put("filter","/messagecenter/car_view.jsp");
		forwards.put("sort","/messagecenter/car_view.jsp");
		forwards.put("page","/messagecenter/car_view.jsp");
		forwards.put("print","/messagecenter/car_view.jsp");
	}
	
	protected void postExecute(BaseReportForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SystemException {
		super.postExecute( form, request, response);

		MessageReportData data = (MessageReportData) request.getAttribute(Constants.REPORT_BEAN);
		if (data.getReportCriteria() != null && data.getDetails() != null) {
			Collection<Integer> contractIds = (Collection<Integer>) data.getReportCriteria().getFilterValue(
					MessageReportData.FILTER_CONTRACT_IDS);
			Integer messageId = (Integer) data.getReportCriteria().getFilterValue(MessageReportData.FILTER_MESSAGE_ID);
			if (messageId != null && (contractIds == null || contractIds.size() == 0)) {
				// this means its a message id search. there is, at most 1 row
				// in the return set.
				Collection<MessageReportDetails> details = data.getDetails();

				if (details.size() == 1) {
					MessageReportDetails row = details.iterator().next();
					if (!row.getContractId().equals("TPA Firm")) {
						// check the contract for this site.
						try {
							UserProfile userProfile = getUserProfile(request);
							Contract c = ContractServiceDelegate.getInstance().getContractDetails(
									Integer.parseInt(StringUtils.trim(row.getContractId())),
									EnvironmentServiceDelegate.getInstance()
									.retrieveContractDiDuration(userProfile.getRole(), 0,
											null));
							if ((StringUtils.equals(c.getCompanyCode(), "094") && Environment.getInstance()
									.getSiteLocation().equals("usa"))
									|| (StringUtils.equals(c.getCompanyCode(), "019") && Environment.getInstance()
											.getSiteLocation().equals("ny"))) {
								data.setDetails(new HashSet<MessageReportDetails>());
								data.setTotalCount(0);
							}
						} catch (ContractNotExistException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private static Logger log = Logger.getLogger(MCCarReportController.class);

	private static final String[] SortFieldOrder = {
			MessageReportData.SORT_FIELD_EFFECTIVE_TS,
			MessageReportData.SORT_FIELD_MESSAGE_STATUS,
			MessageReportData.SORT_FIELD_PRIORITY,
			MessageReportData.SORT_FIELD_RECIPIENT_STATUS,
			MessageReportData.SORT_FIELD_SHORT_TEXT };

	private static final String[] SortDirectionOrder = {
			ReportSort.DESC_DIRECTION, ReportSort.ASC_DIRECTION,
			ReportSort.DESC_DIRECTION, ReportSort.ASC_DIRECTION,
			ReportSort.ASC_DIRECTION };

	@RequestMapping(value= {"","/global"},method =  {RequestMethod.POST} )
	public String execute(BaseReportForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			log.debug("Post for filter/sorting is redirected as get");
			ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
			return "redirect:" + forward.getPath();
		}
	
		return null;
	}

	@Override
	protected String getDefaultSort() {
		return MessageReportData.SORT_FIELD_EFFECTIVE_TS;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		return null;
	}

	@Override
	protected String getReportId() {
		return MessageReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return MessageReportData.REPORT_NAME;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {

		MCMessageReportForm f = (MCMessageReportForm) form;

		String lastName = f.getLastName();

		criteria.addFilter(MessageReportData.FILTER_APPLICATION_ID, Environment
				.getInstance().getAppId());

		String recipient = f.getRecipientId();
		UserRole recipientRole = null;
		if (!StringUtils.isEmpty(recipient)) {
			criteria.addFilter(MessageReportData.FILTER_USER_PROFILE_ID, Long
					.parseLong(recipient));
			//figure out if this is a 
		}

		Collection<Integer> contractIds = new ArrayList<Integer>();
		Collection<Integer> firmIds = new ArrayList<Integer>();
		if ( !StringUtils.isEmpty(recipient) && f.getAllMessages() != null  && f.getAllMessages()) {
			contractIds = MCCarViewUtils.getUserContractIds(Integer.valueOf(recipient), f.getRecipientRole());
			if ( StringUtils.equals(f.getRecipientRole(), ThirdPartyAdministrator.ID) ) {
				firmIds = MCCarViewUtils.getFirmIdsForContracts(contractIds);
			}
		} else if ( !StringUtils.isEmpty(f.getContractId() ))  {
			Set<Integer> accessablecontracts = new HashSet<Integer>();
			accessablecontracts.add(Integer.parseInt(f.getContractId()));
            contractIds.addAll(accessablecontracts);
		}
		if ( !contractIds.isEmpty() ) {
			criteria.addFilter(MessageReportData.FILTER_CONTRACT_IDS, contractIds);
		}
		if ( !firmIds.isEmpty()) {
			criteria.addFilter(MessageReportData.FILTER_TPA_FIRM_IDS, firmIds);
		}

		if (!StringUtils.isEmpty(lastName)) {
			criteria.addFilter(MessageReportData.FILTER_LAST_NAME, lastName);
		}

		String ssn = f.getSsn();
		if (!StringUtils.isEmpty(ssn)) {
			criteria.addFilter(MessageReportData.FILTER_SSN, ssn);
		}

		final String messageId = StringUtils.trim(f.getMessageId());
		String messageStatus = f.getMessageStatus();

        if (StringUtils.isNotBlank(messageId)) {
            criteria.addFilter(MessageReportData.FILTER_MESSAGE_ID, Integer.parseInt(messageId));
		}

		if (!StringUtils.isEmpty(messageStatus)) {
			if (StringUtils.isEmpty(recipient)) {
				if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ALL.getValue())) {
					// no filter

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ACTIVE.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ARCHIVED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_NOT_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ESCALATED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ESCALATED.getValue());
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.EXPIRED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.EXPIRED.getValue());
					// message_action_type = "DCP" and message_status = "DN"
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.REPLACED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.REPLACED.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.OBSOLETED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.OBSOLETE.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.COMPLETEDONLINE.getValue())) {
					criteria
							.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
									com.manulife.pension.service.message.valueobject.Message.MessageStatus.COMPLETED
											.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.DECLAREDCOMPLETE.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_TYPE_CODE,
							MessageTemplate.MessageActionType.DECLARE_COMPLETE.getValue());
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.DONE.getValue());
				} 
			} else {
				//the recipient specific ones. 
				if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ALL.getValue())) {
					// no filter

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ACTIVE.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE.getValue());
					criteria.addFilter(MessageReportData.FILTER_RECIPIENT_STATUS, "N"); //means not hidden

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ARCHIVED.getValue())) {

					/*message_status = RP,EX,CA or
					 * DCP && DN or
					 * AC && HI ( this applies to all of ACT, INF and DCP messages )
					 */
					criteria.addFilter(MessageReportData.FILTER_RECIPIENT_ARCHIVED,
							"dummy value, not used");

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.EXPIRED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.EXPIRED.getValue());
					// message_action_type = "DCP" and message_status = "DN"
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.REPLACED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.REPLACED.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.OBSOLETED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.OBSOLETE.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.COMPLETEDONLINE.getValue())) {
					criteria
							.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
									com.manulife.pension.service.message.valueobject.Message.MessageStatus.COMPLETED
											.getValue());

				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.DECLAREDCOMPLETE.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_TYPE_CODE,
							MessageTemplate.MessageActionType.DECLARE_COMPLETE.getValue());
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.DONE.getValue());
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.REMOVEDFROMVIEW.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ACTIVE.getValue());
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_TYPE_CODE,
							MessageTemplate.MessageActionType.FYI.getValue());
					criteria.addFilter(MessageReportData.FILTER_RECIPIENT_STATUS, "Y"); // means
																						// hidden
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.REMOVEDBYSTILLACTIVE
						.getValue())) {
						criteria.addFilter(MessageReportData.FILTER_REMOVED_STILL_ACTIVE, "dummy, not used"); // means hidden
				} else if (StringUtils.equals(messageStatus, MCCarViewUtils.MessageStatus.ESCALATED.getValue())) {
					criteria.addFilter(MessageReportData.FILTER_MESSAGE_STATUS,
							com.manulife.pension.service.message.valueobject.Message.MessageStatus.ESCALATED.getValue());
				} 	else {

				}				
			}
		}
		

		String tab = f.getTab();
		
		List tabnew= MCHistoryUtils.getTabs(request.getServletContext());
		f.setTabb(tabnew);
		String section = f.getSection();
		String type = f.getType();

		if (!StringUtils.isEmpty(section)) {
			criteria.addFilter(MessageReportData.FILTER_SECTION_ID, Integer
					.parseInt(section));
		}

		if (!StringUtils.isEmpty(tab) && StringUtils.isEmpty(section)) {
			criteria.addFilter(MessageReportData.FILTER_TAB_ID, Integer
					.parseInt(tab));
		}

		if (!StringUtils.isEmpty(type)) {
			criteria.addFilter(MessageReportData.FILTER_MESSAGE_TEMPLATE_ID,
					Integer.parseInt(type));
		}
		// Heartbeat messages will not be filtered out for internal users
		/*
		 * if (StringUtils.isEmpty(recipient)) {
			criteria.addFilter(MessageReportData.FILTER_HEARTBEAT_MESSAGE,
					MCConstants.HeartBeatTemplateId);
		}*/
		
		if (!StringUtils.isEmpty(f.getSubmissionId())) {
			criteria.addFilter(MessageReportData.FILTER_SUBMISSION_ID, Integer
					.parseInt(f.getSubmissionId()));
		}

		if (f.getFromDateAsDate() != null) {
			criteria.addFilter(MessageReportData.FILTER_FROM_DATE, f
					.getFromDateAsDate());
		}
		if (f.getToDateAsDate() != null) {
			criteria.addFilter(MessageReportData.FILTER_TO_DATE, f
					.getToDateAsDate());
		}
	}

	@Override
	protected void populateSortCriteria(ReportCriteria criteria,
			BaseReportForm form) {
		MCMessageReportForm f = (MCMessageReportForm) form;
		boolean recipientRelated = false;
		if (!StringUtils.isEmpty(f.getRecipientId())) {
			recipientRelated = true;
		}

		String sortDir = form.getSortDirection();
		String sortField = form.getSortField();
		// if no sort field is in the form, use the default one
		if (StringUtils.isEmpty(sortField)) {
			sortField = getDefaultSort();
			sortDir = getDefaultSortDirection();
		}
		// find the index of the selected sortField
		if (recipientRelated
				|| !(sortField.equals(MessageReportData.SORT_FIELD_PRIORITY) || sortField
						.equals(MessageReportData.SORT_FIELD_RECIPIENT_STATUS)
						|| sortField.equals(MessageReportData.SORT_FIELD_ARCHIVED))) {
			criteria.insertSort(sortField, sortDir);
		} else {
			// needs to reset the default sort
			f.setSortField(getDefaultSort());
			f.setSortDirection(getDefaultSortDirection());
		}
		for (int i = 0; i < SortFieldOrder.length; i++) {
			if (recipientRelated
					|| !(SortFieldOrder[i]
							.equals(MessageReportData.SORT_FIELD_PRIORITY) || SortFieldOrder[i]
							.equals(MessageReportData.SORT_FIELD_RECIPIENT_STATUS))) {
				// the selected field/dir is the first one
				// the rest keep the same.
				if (!sortField.equals(SortFieldOrder[i])) {
					criteria.insertSort(SortFieldOrder[i],
							SortDirectionOrder[i]);
				}
			}
		}
	}

	
	@Override
	protected BaseReportForm resetForm(
			BaseReportForm reportForm, HttpServletRequest request)
			throws SystemException {
		String path =new UrlPathHelper().getPathWithinServletMapping(request);
		boolean comingFromSelectContractPage = path.contains(MCConstants.mappingParameterGlobal);
		boolean justSelectedAContract = SessionHelper.getMCSelectContract(request) != null ;
		if ( justSelectedAContract ) {
			//reset it..only need to know the first time
			SessionHelper.unsetMCSelectContract(request);
		}
		
		MCMessageReportForm form = (MCMessageReportForm)reportForm;
		if (comingFromSelectContractPage || justSelectedAContract) {
			form = (MCMessageReportForm) super.resetForm(reportForm, request);
			form.setRecipientList(new ArrayList<LabelValueBean>());
			if (getUserProfile(request).getCurrentContract() != null) {
				form.setContractId(Integer.toString(SessionHelper.getUserProfile(request).getCurrentContract()
						.getContractNumber()));
			}
		}
		return form;
	}
	@RequestMapping(value= {"","/global"},method =  {RequestMethod.GET} )
	public String doDefault(@Valid @ModelAttribute("carViewForm") MCMessageReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			MessageReportData reportData = new MessageReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		
		MCMessageReportForm f = (MCMessageReportForm) form;
		if (f.getViewPersonalization() != null && f.getViewPersonalization()) {
			f.setViewPersonalization(false);
			ControllerForward forward = new ControllerForward("refresh",
					"/do/mcCarView/viewEmailPreferences?" + MCConstants.ParamUserProfileId + "=" + f.getRecipientId()
							+ "&" + MCConstants.ParamContractId + "=" + f.getContractId(),true);
			return "redirect:" + forward.getPath();
		}
		
		String forward = super.doDefault(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value= {"","/global"},params="task=filter",method =  RequestMethod.GET )
	public String doFilter(@Valid @ModelAttribute("carViewForm") MCMessageReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			MessageReportData reportData = new MessageReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		MCMessageReportForm f = (MCMessageReportForm) form;
		if ( f.getViewPersonalization() != null && f.getViewPersonalization() ) {
			f.setViewPersonalization(false);
			ControllerForward forward = new ControllerForward("refresh",
					"/do/mcCarView/viewEmailPreferences?" + MCConstants.ParamUserProfileId + "=" + f.getRecipientId()
							+ "&" + MCConstants.ParamContractId + "=" + f.getContractId(),true);
			return "redirect:" + forward.getPath();
		}
		Collection<GenericException> errors = super.doValidate(form,
				request);
			f.validate(errors);
			if (!errors.isEmpty()) {
				f.sortErrors(errors);
				MessageReportData reportData = new MessageReportData(null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				SessionHelper.setErrorsInSession(request, errors);
				return forwards.get("input");
			}
		String forward = super.doFilter(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value= {"","/global"}, params="task=page",method = RequestMethod.GET )
	public String doPage(@Valid @ModelAttribute("carViewForm") MCMessageReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			MessageReportData reportData = new MessageReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doPage(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value= {"","/global"}, params="task=sort",method = RequestMethod.GET )
	public String doSort(@Valid @ModelAttribute("carViewForm") MCMessageReportForm form, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			MessageReportData reportData = new MessageReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}
		String forward = super.doSort(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@Autowired
	MCCarReportValidator mCCarReportValidator;
	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}
	
}
