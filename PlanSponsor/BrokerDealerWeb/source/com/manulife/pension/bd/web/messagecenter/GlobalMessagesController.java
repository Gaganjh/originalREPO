package com.manulife.pension.bd.web.messagecenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWDefault;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.MessageCenter;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.MessageServiceDelegate;
import com.manulife.pension.event.BDWGlobalMessageEvent;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.service.message.report.valueobject.BDGlobalMessageReportData;
import com.manulife.pension.service.message.valueobject.Message.MessageStatus;
import com.manulife.pension.service.message.valueobject.MessageTemplate;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * Action Class for Global Messages PAge
 * 
 * @author Sithomas
 */
@Controller
@RequestMapping(value ="/globalMessages")
@SessionAttributes({"globalMessagesForm"})

public class GlobalMessagesController extends BDReportController {
	@ModelAttribute("globalMessagesForm") 
	public GlobalMessagesForm populateForm()
	{
		return new GlobalMessagesForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/messagecenter/globalMessages.jsp");
		forwards.put("sort","/messagecenter/globalMessages.jsp");
		forwards.put("refresh","/messagecenter/globalMessages.jsp");
		forwards.put("publish","redirect:/do/globalMessages/?task=refresh");
		forwards.put("expire","redirect:/do/globalMessages/?task=refresh");
	}

	
	private static final int GlobalMessageContentGroupId = 125;
    
    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

    private ServiceLogRecord logRecord = new ServiceLogRecord("GlobalMessagesAction");

    private static final int GLOBAL_MESSAGE_TEMPLATE_ID = 120;

    public GlobalMessagesController() {
        super(GlobalMessagesController.class);
    }

    @Override
    protected String getDefaultSort() {
        return BDGlobalMessageReportData.SORT_CMA_KEY;
    }

    @Override
    protected String getDefaultSortDirection() {
        return ReportSort.DESC_DIRECTION;
    }

    @Override
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException {
        throw new SystemException("Download data is not supported");
    }

    @Override
    protected String getReportId() {
        return BDGlobalMessageReportData.REPORT_ID;
    }

    @Override
    protected String getReportName() {
        return BDGlobalMessageReportData.REPORT_NAME;
    }

    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) throws SystemException {
    	criteria.addFilter(BDGlobalMessageReportData.FILTER_CONTENT, getGlobalMessageContent());
    }

    @Override
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        String selectedField = form.getSortField();
        String selectDirection = form.getSortDirection();
        if (StringUtils.isEmpty(selectedField)) {
            selectedField = getDefaultSort();
            selectDirection = getDefaultSortDirection();
        }
        criteria.insertSort(selectedField, selectDirection);
        // selected sort is other than CMA key
        if (!StringUtils.equals(BDGlobalMessageReportData.SORT_CMA_KEY, selectedField)) {
            criteria.insertSort(BDGlobalMessageReportData.SORT_CMA_KEY, ReportSort.DESC_DIRECTION);
        }
    }
    
    

    /**
     * The method is executed when the user clicks publish button in bd global jsp
     * 
     *@see BaseReportController#doExecute(ActionMapping, org.apache.struts.action.Form, HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value ="/", params= {"task=publish"} ,method =  {RequestMethod.GET,RequestMethod.POST})
    public String doPublish( @Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws  SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
    		}
    	}

        String task = getTask(request);

        if (StringUtils.equalsIgnoreCase("POST", request.getMethod())
                && !StringUtils.equals("publish", task)  && !StringUtils.equals("expire", task)) {
            // do a refresh so that there's no problem using tha back button
            ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
                    + "?task=" + task, true);
            return forward.getPath();
        }


        int contentId = Integer.parseInt(theForm.getSelectedContentId());
        long userProfileId = getUserProfile(request).getBDPrincipal().getProfileId();
        boolean isMessagePublished = theForm.isMessagePublished();

        // create and trigger bd global message event
        Event event = getGlobalEvent(contentId, userProfileId);
        EventClientUtility.getInstance(BDConstants.BD_APPLICATION_ID)
                .prepareAndSendJMSMessage(event);
        // set published info message in the request scope
        Collection<GenericException> infoMessages = new ArrayList<GenericException>();
        infoMessages.add(new GenericExceptionWithContentType(
                BDContentConstants.GLOBAL_MESSAGES_PUBLISH_SUCCESS,
                ContentTypeManager.instance().MISCELLANEOUS));
        request.getSession().setAttribute(BDConstants.INFO_MESSAGES, infoMessages);
        // reset the selected radio button
        theForm.setSelectedContentId(null);
        
        // default sort
        theForm.setSortDirection(null);
        theForm.setSortField(null);
        
        // log the action
        if(isMessagePublished) {
            logMessages(request, contentId, BDConstants.GLOBAL_MESSAGE_REPUBLISH_ACTION_TEXT);
        } else {
            logMessages(request, contentId, BDConstants.GLOBAL_MESSAGE_PUBLISH_ACTION_TEXT);
        }
        
        return forwards.get("publish");
    }

    /**
     * The method is executed when the user clicks expire button in bd global jsp
     * 
     *@see BaseReportController#doExecute(ActionMapping, org.apache.struts.action.Form, HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value ="/", params= {"task=expire"} ,method =  {RequestMethod.GET,RequestMethod.POST})
    public String doExpire( @Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm theForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
    		}
    	}

        String task = getTask(request);

        if (StringUtils.equalsIgnoreCase("POST", request.getMethod())
                && !StringUtils.equals("publish", task)  && !StringUtils.equals("expire", task)) {
            // do a refresh so that there's no problem using tha back button
            ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
                    + "?task=" + task, true);
            return forward.getPath();
        }

        
        // if the message is not published display error message else expire the message
        if (!theForm.isMessagePublished()) {
            Collection<GenericException> errors = new ArrayList<GenericException>();
            errors.add(new GenericException(BDErrorCodes.GLOBAL_MESSAGES_EXPIRE_NOT_ALLOWED));
            setErrorsInSession(request, errors);
        } else {
            MessageServiceDelegate delegate = MessageServiceDelegate
            .getInstance(BDConstants.BD_APPLICATION_ID);
            int contentId = Integer.parseInt(theForm.getSelectedContentId());
            long userProfileId = getUserProfile(request).getBDPrincipal().getProfileId();

            // get bd global event
            Event event = getGlobalEvent(contentId, userProfileId);
            // get message temlate for bd global message
            MessageTemplate messsageTemplate = delegate
                    .getMessageTemplate(GLOBAL_MESSAGE_TEMPLATE_ID);
            // set message status to expired
            delegate.updateMessagesStatusToTargetStatus(event, messsageTemplate,
                    MessageStatus.EXPIRED);
            
            // set expired info message in the request scope
            Collection<GenericException> infoMessages = new ArrayList<GenericException>();
            infoMessages.add(new GenericExceptionWithContentType(
                    BDContentConstants.GLOBAL_MESSAGES_EXPIRE_SUCCESS,
                    ContentTypeManager.instance().MISCELLANEOUS));
            request.getSession().setAttribute(BDConstants.INFO_MESSAGES, infoMessages);
            
            // log the action
            logMessages(request, contentId, BDConstants.GLOBAL_MESSAGE_EXPIRE_ACTION_TEXT);
            
            // reset the selected radio button
            theForm.setSelectedContentId(null);
            
            // default sort
            theForm.setSortDirection(null);
            theForm.setSortField(null);
        }

        return forwards.get("expire");
    }
    
    @RequestMapping(value ="/", method =  {RequestMethod.GET}) 
    public String doDefault( @Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
    		}
    	}

        String task = getTask(request);

        if (StringUtils.equalsIgnoreCase("POST", request.getMethod())
                && !StringUtils.equals("publish", task)  && !StringUtils.equals("expire", task)) {
            // do a refresh so that there's no problem using tha back button
            ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
                    + "?task=" + task, true);
            return forward.getPath();
        }

        String forward=super.doDefault( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
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
    public String doRefresh(
    		@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    	    		throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");
			}
		}

    	        String task = getTask(request);

    	        if (StringUtils.equalsIgnoreCase("POST", request.getMethod())
    	                && !StringUtils.equals("publish", task)  && !StringUtils.equals("expire", task)) {
    	            // do a refresh so that there's no problem using tha back button
    	            ControllerForward forward = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
    	                    + "?task=" + task, true);
    	            return forward.getPath();
    	        }

        String forward = doCommon( form, request, response);

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
    }
 
    
    	
   
    
    @RequestMapping(value ="/" ,params={"task=filter"}, method =  {RequestMethod.POST}) 
    public String doFilter (@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    @RequestMapping(value ="/", params={"task=page"}, method =  {RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    @RequestMapping(value ="/", params={"task=sort"}, method =  {RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    @RequestMapping(value ="/",
    params={"task=download"}  , method =  {RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    @RequestMapping(value ="/" ,params={"task=downloadAll"}, method =  {RequestMethod.GET}) 
    public String doDownloadAll (@Valid @ModelAttribute("globalMessagesForm") GlobalMessagesForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
    
    
    /**
     * Returns a BD Global event
     * 
     * @param contentId
     * @param userProfileId
     * @param methodName
     * @return Event
     */
    private Event getGlobalEvent(final int contentId, final long userProfileId) {
        BDWGlobalMessageEvent event = new BDWGlobalMessageEvent(this.getClass().getName(),
                "getGlobalEvent");
        event.setContentKey(contentId);
        event.setInitiator(userProfileId);
        return event;
    }
    
    /**
     * This method is used to log the action messages into MRL.
     * 
     * @param request - HttpServletRequest
     * @param cmaKey - the CMA id
     * @param actionTaken the action taken
     */
    private void logMessages(HttpServletRequest request, int cmaKey, String actionTaken) {

        StringBuffer logData = new StringBuffer();
        BDUserProfile userProfile = getUserProfile(request);
        Long profileID = userProfile.getBDPrincipal().getProfileId();
        
        logData.append(BDConstants.BRL_LOG_USER_PROFILE_ID).append(profileID).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BRL_LOG_DATE_OF_ACTION).append(new Date()).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.GLOBAL_MESSAGE_CONTENT_KEY_TEXT).append(cmaKey).append(
                BDConstants.SEMICOLON_SYMBOL);

        logData.append(BDConstants.BRL_LOG_PAGE_ACCESSED).append("Global Messages").append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BRL_LOG_ACTION_TAKEN).append(actionTaken).append(BDConstants.SEMICOLON_SYMBOL);

        logWebActivity(actionTaken, logData.toString(), userProfile,
                logger, interactionLog, logRecord);
    }
    
    
    /**
     * Logs the web activities
     * 
     * @param action
     * @param logData
     * @param profile
     * @param logger
     * @param interactionLog
     * @param logRecord
     */
    private static void logWebActivity(String action, String logData, BDUserProfile profile,
            Logger logger, Category interactionLog, ServiceLogRecord logRecord) {
        try {
            ServiceLogRecord record = (ServiceLogRecord) logRecord.clone();
            record.setServiceName("GlobalMessagesAction");
            record.setMethodName(action);
            record.setData(logData);
            record.setDate(new Date());
            record.setPrincipalName(profile.getBDPrincipal().getUserName());
            record.setUserIdentity(profile.getBDPrincipal().getProfileId()
                    + profile.getBDPrincipal().getUserName());

            interactionLog.error(record);
        } catch (CloneNotSupportedException e) {
            // log the error, but don't interrupt regular processing
            logger.error("error trying to log a web activity for Global Messages page " + e);
        }
    }
    
	/**
	 * Retrieve all the CMA defined global messages
	 * 
	 * @return
	 * @throws SystemException
	 */
	private List<MessageCenter> getGlobalMessageContent()
			throws SystemException {
		try {
			List<MessageCenter> gms = new ArrayList<MessageCenter>();
			Content[] contents = BrowseServiceDelegate.getInstance()
					.findContent(ContentTypeManager.instance().MESSAGE_CENTER);
			for (Content c : contents) {
				if (((MutableContent) c).getParentId() == GlobalMessageContentGroupId) {
					gms.add((MessageCenter) c);
				}
			}
			return gms;
		} catch (ContentException e) {
			throw new SystemException(e,
					"Fail to retrieve Message center content");
		}
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	private BDValidatorFWDefault bdValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWDefault);
	}
	
	
	
}
