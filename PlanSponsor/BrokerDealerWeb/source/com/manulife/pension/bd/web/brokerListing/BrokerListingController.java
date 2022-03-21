package com.manulife.pension.bd.web.brokerListing;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportData;
import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportVO;
import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingSummaryVO;
import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ProtectedStringBuffer;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWError;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This is the Action class for Broker Listing page.
 * 
 * @author harlomte
 * 
 */
@Controller
@RequestMapping( value ="/brokerListing")
@SessionAttributes({"brokerListingForm"})

public class BrokerListingController extends BDReportController {
	@ModelAttribute("brokerListingForm") 
	public BrokerListingForm populateForm() 
	{
		return new BrokerListingForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/brokerListing/brokerListing.jsp");
		forwards.put("error","/brokerListing/brokerListing.jsp");
		forwards.put("brokerListing","/brokerListing/brokerListing.jsp" );
		forwards.put("default","/brokerListing/brokerListing.jsp");
		forwards.put("sort","/brokerListing/brokerListing.jsp");
		forwards.put("filter","/brokerListing/brokerListing.jsp");
		forwards.put("page","/brokerListing/brokerListing.jsp"); 
		forwards.put("download","/brokerListing/brokerListing.jsp");
		forwards.put("printPDF","/brokerListing/brokerListing.jsp ");
		}

	

    private static final String XSLT_FILE_KEY_NAME = "BrokerListingReport.XSLFile";
    
    private Logger logger = Logger.getLogger(BrokerListingController.class);

    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

    private ServiceLogRecord logRecord = new ServiceLogRecord("BrokerListingAction");

    /**
     * Constructor.
     */
    public BrokerListingController() {
        super(BrokerListingController.class);
    }

    /**
     * The preExecute() method was overriden to solve the back button problem.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws SystemException
     */
    @Override
    public String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {
        
        String forward = null;
        forward = super.preExecute( form, request, response);
        if (forward != null) {
            return forward;
        }
        
        if (StringUtils.equalsIgnoreCase("POST", request.getMethod())) {

            // do a refresh so that there's no problem using the back button
        	ControllerForward   forward1 = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinApplication(request)
                    + "?task=" + getTask(request), true);
            return forward1.getPath();
        }
        return forward;
    }

    /**
     * This method will get the Broker Listing Information.
     */
    
    
    public String doCommon( BaseReportForm form,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {


        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon() in BrokerListingAction.");
        }
        
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BrokerListingForm actionForm = (BrokerListingForm) form;
        
        beforeDoCommon( actionForm, request, response);
        
        // Check for Error Messages.
        boolean isErrorMsgPresent = checkForErrorConditions(userProfile, actionForm, request);
        if (isErrorMsgPresent) {
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doCommon() in BrokerListingAction. Error Messages Found.");
            }
            return forwards.get("input");
        }

        // Check for Info Messages.
        boolean isInfoMsgPresent = checkForInfoMessageConditions(userProfile, actionForm, request);
        if (isInfoMsgPresent) {
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doCommon() in BrokerListingAction. Info Messages Found.");
            }

            return forwards.get("input");
        }

        super.doCommon( actionForm, request, response);
        
        setStoredProcSessionIDIntoMap(actionForm, request);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doCommon() in BrokerListingAction.");
        }
        
        return forwards.get(BDConstants.BROKER_LISTING_FORWARD);
    }

    /**
     * This method is called when the user clicks on the Reset button. This calls the doDefault()
     * method and makes sure the advanced filter section is still open.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    
    @RequestMapping(value ="/" ,params={"action=reset","task=reset"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doReset (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return  forwards.get("error");//if input forward not //available, provided default
	       }
		}
     if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault() in BlockOfBusinessAction.");
        }
        String forward = doDefault( form, request, response);

        // Show the Advanced Filter section..
        ((BrokerListingForm) form).setShowAdvanceFilter(true);

        return forward;
    }
    @RequestMapping(value ="/", method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
      	String  forward= preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
  	if(bindingResult.hasErrors()){
  		try {
			beforeDoCommon( (BaseReportForm) form, request, response);
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");//if input forward not //available, provided default
    		} 
    	}
    	
    	 forward=super.doDefault( form, request, response);
    	 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
    }
    
    @RequestMapping(value ="/",params={"task=default"}, method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doDefaultNew (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	
    	String forward=super.doDefault( form, request, response);
    	 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    } 
    @RequestMapping(value ="/" ,params={"task=filter"}, method ={RequestMethod.POST})
    public String doFilter (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    		}
    	}
    	String forward=super.doFilter( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward); 
    }
    @RequestMapping(value ="/", params={"task=page"}, method ={RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward= preExecute(form, request, response);
        if (StringUtils.isNotBlank(forward)) {
      	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
         }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	 forward=super.doPage( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);  
    }
    
    @RequestMapping(value ="/", params={"task=sort"},method ={RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward= preExecute(form, request, response);
        if (StringUtils.isNotBlank(forward)) {
      	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
         }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	 forward=super.doSort( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);  
    }
    
    @RequestMapping(value ="/",
    params={"task=download"},method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward= preExecute(form, request, response);
        if (StringUtils.isNotBlank(forward)) {
      	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
         }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	 forward=super.doDownload( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);  
    }
    
    @RequestMapping(value ="/" ,params={"task=downloadAll"},method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doDownloadAll (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward= preExecute(form, request, response);
        if (StringUtils.isNotBlank(forward)) {
      	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
         }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	 forward=super.doDownloadAll( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);  
    }
    @RequestMapping(value ="/" ,params={"task=printPDF"},method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("brokerListingForm") BrokerListingForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward= preExecute(form, request, response);
        if (StringUtils.isNotBlank(forward)) {
      	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
         }
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return  forwards.get("error");
    		}
    	}
    	 forward=super.doPrintPDF( form, request, response);
    	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);  
    }
    /**
     * This method is used to do the tasks that need to be done before calling the doCommon()
     * method.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws SystemException
     */
    private void beforeDoCommon( BaseReportForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();
        BrokerListingForm reportForm = (BrokerListingForm) form;

        if (DEFAULT_TASK.equals(getTask(request))
                || (reportForm.getIsPageAccessed() == null || !reportForm.getIsPageAccessed())) {
            // Log Page Access into MRL only when the user navigates to the page. Don't log into MRL
            // when the user does a sorting, filtering, etc.
            logPageAccess(request, userProfile);

            reportForm.setIsPageAccessed(Boolean.TRUE);
        }

        // Get applicable filters. - This step is common for both quick Filters and Advance Filters.
        reportForm.setFiltersMap(BrokerListingUtility.getApplicableFilters(userRole, userProfile));

        // Call Populate the Form.
        populateReportForm( form, request);
    }

    /**
     * This method checks for those conditions where we need to display a Error message to the user.
     * The Error conditions could have been checked in doValidate() method, but there are few clean
     * up activities that were not being performed until and unless we come to
     * doDefault()/doCommon() method. Hence, the doValidate() method is not being used to show the
     * Error conditions.
     * 
     * @param userRole
     * @param reportForm
     * @param request
     * @return
     */
	private boolean checkForErrorConditions(BDUserProfile userProfile,
			BrokerListingForm reportForm, HttpServletRequest request) {
        boolean isErrorMsgPresent = false;

        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        /**
         * Display Error Message if Financial Rep Name / Org Name < 3 characters.
         */
        String financialRepOrOrgName = null;
        if (quickFilterSubmitted != null) {
        	if (quickFilterSubmitted) {
        		financialRepOrOrgName = reportForm.getQuickFilterFinancialRepName();
        	} else {
        		financialRepOrOrgName = reportForm.getFinancialRepName();
        	}

        	if (StringUtils.trimToNull(financialRepOrOrgName) != null
                    && StringUtils.trimToNull(financialRepOrOrgName).length() < 3) {
                GenericException exception = new GenericException(
                        BDErrorCodes.FINANCIALREP_NAME_LESS_THAN_THREE_DIGITS);
                errorMessages.add(exception);
            }
        }
        
        if (!errorMessages.isEmpty()) {
            isErrorMsgPresent = true;
            setErrorsInRequest(request, errorMessages);
        }

        return isErrorMsgPresent;
    }
    
    /**
     * This method checks for those conditions where we need to display a Informational message to
     * the user.
     * 
     * @param userRole - the BDUserRole object
     * @param reportForm - The BrokerListingForm object
     * @param request - the HttpServletRequest object.
     * @return - a arrayList of informational message exceptions to be shown to the user.
     * @throws SystemException
     */
    private boolean checkForInfoMessageConditions(BDUserProfile userProfile,
            BrokerListingForm reportForm, HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> checkForInfoMessageConditions().");
        }

        boolean isInfoMsgPresent = false;
        boolean noContractsForFilterEnteredMessageAdded = false;
        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();

        /**
         * 1. Show a message to the Internal User (not RVP) to use Filter criteria to display the
         * BOB report. This is done only for the first time the user accesses the page.
         */
        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        if (quickFilterSubmitted == null) { // quickFilterSubmitted is null for first time access.
            if (BrokerListingUtility.isInternalUserAndNotRVP(userProfile.getRole())) {
                GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                        BDContentConstants.BROKER_LISTING_ENTER_SEARCH_CRITERIA_TO_DISPLAY_REPORT,
                        ContentTypeManager.instance().MISCELLANEOUS,false);
                infoMessages.add(exception);
            }
        }

        /**
         * 2. If the Producer Code entered by the user is not numeric, show the Information message.
         */
        String producerCode = null;
        if (quickFilterSubmitted == null || quickFilterSubmitted) {
            producerCode = getFilterValue(BrokerListingReportData.FILTER_QF_PRODUCER_CODE_ID,
                    reportForm, userProfile, request, false);
        } else {
            producerCode = getFilterValue(BrokerListingReportData.FILTER_PRODUCER_CODE_ID,
                    reportForm, userProfile, request, false);
        }
        if (!StringUtils.isBlank(producerCode) && !StringUtils.isNumeric(producerCode.trim())) {
            GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                    BDContentConstants.BROKER_LISTING_NO_PRODUCERS_FOR_FILTER_ENTERED,
                    ContentTypeManager.instance().MISCELLANEOUS, false);
            infoMessages.add(exception);
            noContractsForFilterEnteredMessageAdded = true;
        }
        
        /**
         * 3. Show a info message when the firm name entered by the user is invalid. When this happens,
         * the firm-name may have a value, but the firmID will be empty/null.
         */
        if (quickFilterSubmitted != null) {
        	String firmIDSelected = ""; 
            String firmNameSelected = "";
            if (quickFilterSubmitted) {
            	firmIDSelected = reportForm.getQuickFilterBDFirmID();
            	firmNameSelected = reportForm.getQuickFilterBDFirmName();
            } else {
            	firmIDSelected = reportForm.getBdFirmID();
            	firmNameSelected = reportForm.getBdFirmName();
            }

            if (!StringUtils.isBlank(firmNameSelected) && StringUtils.isBlank(firmIDSelected)) {
            	if (!noContractsForFilterEnteredMessageAdded) {
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BROKER_LISTING_NO_PRODUCERS_FOR_FILTER_ENTERED,
                            ContentTypeManager.instance().MISCELLANEOUS, false);
                    infoMessages.add(exception);
                    noContractsForFilterEnteredMessageAdded = true;
            	}
            }
        }
        
        if (!infoMessages.isEmpty()) {
            isInfoMsgPresent = true;
            setMessagesInRequest(request, infoMessages, BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> checkForInfoMessageConditions().");
        }

        return isInfoMsgPresent;
    }

    /**
     * This method is used to log the Page Access into MRL.
     * 
     * @param userProfile - BDUserProfile object.
     */
    private void logPageAccess(HttpServletRequest request, BDUserProfile userProfile) {
        StringBuffer logData = new StringBuffer();
        Long profileID = null;
        if (userProfile.isInMimic()) {
            BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
                    .getMimckingUserProfile(request);
            profileID = mimickingUserProfile.getBDPrincipal().getProfileId();
        } else {
            profileID = userProfile.getBDPrincipal().getProfileId();
        }
        logData.append(BDConstants.BRL_LOG_USER_PROFILE_ID).append(profileID).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BRL_LOG_PAGE_ACCESSED).append(
                BDConstants.BRL_LOG_FINANCIAL_REP_LISTING).append(BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BRL_LOG_MIMIC_MODE).append(
                userProfile.isInMimic() ? BDConstants.YES_VALUE : BDConstants.NO_VALUE).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        if (userProfile.isInMimic()) {
            logData.append(BDConstants.BRL_LOG_MIMICKED_USER_PROFILE_ID).append(
                    userProfile.getBDPrincipal().getProfileId()).append(
                    BDConstants.SEMICOLON_SYMBOL);
        }
        
        logData.append(BDConstants.BRL_LOG_DATE_OF_ACTION).append(new Date()).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BRL_LOG_ACTION_TAKEN).append(
                BDConstants.BRL_LOG_BD_BLOCK_OF_BUSINESS).append(BDConstants.SEMICOLON_SYMBOL);

        BlockOfBusinessUtility.logWebActivity("doDefault", logData.toString(), userProfile, logger,
                interactionLog, logRecord);
    }

    /**
     * This method is used to set the session ID obtained from stored proc, into a Map.
     * 
     * @param reportForm - The Form.
     * @param request - The HttpServlet Request.
     */
    @SuppressWarnings("unchecked")
    private void setStoredProcSessionIDIntoMap(BrokerListingForm reportForm,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> setStoredProcSessionIDIntoSession().");
        }

        BrokerListingReportData reportData = (BrokerListingReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
        if (reportData == null) {
            return;
        }

        Date asOfDate = getAsOfDate(reportForm);
        // This variable will be used to store the session ID that is sent to Database.
        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
                .getAttribute(BDConstants.BRL_DB_SESSION_ID);
        if (storedProcSessionIDMap == null) {
            storedProcSessionIDMap = new HashMap<Date, Integer>();
        }

        storedProcSessionIDMap.put(asOfDate, reportData.getDbSessionID());

        request.getSession(false).setAttribute(BDConstants.BRL_DB_SESSION_ID, storedProcSessionIDMap);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> setStoredProcSessionIDIntoSession().");
        }
    }

    /**
     * This method is used to obtain the session ID from the storedProcSessionIDMap in the Form.
     * 
     * @param reportForm - The BrokerListingForm.
     * @return - String representing the session ID. null, if the session ID is not present.
     */
    @SuppressWarnings("unchecked")
    private Integer getStoredProcSessionIDForAsOfDate(BrokerListingForm reportForm,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStoredProcSessionIDForAsOfDate().");
        }

        Date asOfDate = getAsOfDate(reportForm);
        
        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
                .getAttribute(BDConstants.BRL_DB_SESSION_ID);
        
        if (storedProcSessionIDMap == null) {
            return null;
        }
        Integer storedProcSessionID = storedProcSessionIDMap.get(asOfDate);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getStoredProcSessionIDForAsOfDate().");
        }

        return storedProcSessionID;
    }


    /**
     * This method will give the Default Sort to be applied to the Report. Since the default sort
     * will be taken care by the stored proc itself, we do not need to apply any default sort here.
     */
    @Override
    protected String getDefaultSort() {
        return BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID;
    }

    /**
     * This method will give the Default Sort Direction to be applied to the Report. Since the
     * default sort direction will be taken care by the stored proc itself, we do not need to apply
     * any default sort here.
     */
    @Override
    protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;
    }

    /**
     * This method would return the key present in ReportsXSL.properties file. This key has the
     * value as path to XSLT file, which will be used during PDF generation.
     * 
     * @return String - XSLT file location.
     */
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }

    /**
     * This method will return the # of rows that is present in the current Report. The Integer
     * returned will be used to see if we need to cap the # of rows or not.
     * 
     * @param report
     * @return - The number of rows in the current Report.
     */
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = super.getNumberOfRowsInReport(report);

        // When the Result is too big, we do not get any rows from the stored proc. At this time, we
        // are making the no of rows to 0, so that the PDF capped message is not shown to the user.
        BrokerListingReportData reportData = (BrokerListingReportData) report;
        if (reportData.getResultTooBigInd()) {
            noOfRows = 0;
        }

        return noOfRows;
    }

    /**
     * This method will be used to prepare the XML file that will be later used to create the PDF.
     */
    public Document prepareXMLFromReport(BaseReportForm form, ReportData report,
            HttpServletRequest request) throws SystemException, ParserConfigurationException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> prepareXMLFromReport().");
        }

        PDFDocument doc = new PDFDocument();

        BrokerListingForm reportForm = (BrokerListingForm) form;

        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

        BrokerListingReportData reportData = (BrokerListingReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);

        if (reportData == null) {
            return null;
        }
        
        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext())
                .getLayoutBean(BDPdfConstants.BROKER_LISTING_PATH, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();

        Element rootElement = doc.createRootElement(BDPdfConstants.BROKER_LISTING);

        // Logo, Report Name, As Of Date, Intro-1, Intro-2 elements.
        setLogoAndPageName(layoutPageBean, doc, rootElement);
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByStyle(
                getAsOfDate(reportForm), null, RenderConstants.MEDIUM_STYLE));
        setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        // Show Filters Used
        ArrayList<LabelValueBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        if (filtersUsed != null && !filtersUsed.isEmpty()) {
            Element filtersUsedElement = doc.createElement(BDPdfConstants.FILTERS_USED);
            for (LabelValueBean filterUsed : filtersUsed) {
                Element filterElement = doc.createElement(BDPdfConstants.FILTER);
                doc.appendTextNode(filterElement, BDPdfConstants.FILTER_TITLE, filterUsed.getLabel());
                doc.appendTextNode(filterElement, BDPdfConstants.FILTER_VALUE, filterUsed.getValue());
                doc.appendElement(filtersUsedElement, filterElement);
            }
            doc.appendElement(rootElement, filtersUsedElement);
        }

        // Summary Info.
        showSummaryInfoInPDF(rootElement, doc, layoutPageBean, userProfile, reportData);

        // Report Details.
        showMainReportInPDF(rootElement, doc, reportData);

        // Show Info Messages..
        setInfoMessagesXMLElements(doc, rootElement, request);

        // Show PDF Capped message.
        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }

        setFooterXMLElements(layoutPageBean, doc, rootElement, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> prepareXMLFromReport().");
        }

        return doc.getDocument();
    }

    /**
     * This method is used to create the part of XML that will show the Summary Information in PDF.
     * 
     * @param parentElement - The Root Element.
     * @param doc - PDFDocument object.
     * @param layoutPageBean - Layout bean
     * @param userProfile - BDUserProfile object.
     * @param reportData - Report Data.
     */
    private void showSummaryInfoInPDF(Element parentElement, PDFDocument doc,
            LayoutPage layoutPageBean, BDUserProfile userProfile, BrokerListingReportData reportData) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showSummaryInfoInPDF().");
        }

        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);
        
        
        BrokerListingSummaryVO brokerListingSummaryVO = reportData.getBrokerListingSummaryVO();
        
        // If the user is a Internal User, then show his Name.
        if (userProfile.getRole() instanceof BDInternalUser && !userProfile.isInMimic()) {
            Element internalUserInfoElement = doc.createElement(BDPdfConstants.INTERNAL_USER_INFO);
            String userName = userProfile.getBDPrincipal().getFirstName()
                    + BDConstants.SINGLE_SPACE_SYMBOL + userProfile.getBDPrincipal().getLastName();
            doc.appendTextNode(internalUserInfoElement, BDPdfConstants.USER_NAME, userName);
            doc.appendElement(summaryInfoElement, internalUserInfoElement);
            // If the user is a BDFirmRep, show his Name and BDFirm's associated to his profile.
        } else if (userProfile.getRole() instanceof BDFirmRep) {
            List<BrokerDealerFirm> bdFirms = ((BDFirmRep) userProfile.getRole())
                    .getBrokerDealerFirmEntities();
            ArrayList<String> firmNames = new ArrayList<String>();
            for (BrokerDealerFirm bdFirm : bdFirms) {
                if (!firmNames.contains(bdFirm.getFirmName())) {
                    firmNames.add(bdFirm.getFirmName());
                }
            }

            if (firmNames != null && !firmNames.isEmpty()) {
                Element bdFirmRepInfo = doc.createElement(BDPdfConstants.BDFIRM_REP_INFO);
                Element associatedFirmName = doc.createElement(BDPdfConstants.ASSOCIATED_FIRM_NAMES);
                for (String firmName : firmNames) {
                    doc.appendTextNode(associatedFirmName, BDPdfConstants.FIRM_NAME, firmName);
                }
                doc.appendElement(bdFirmRepInfo, associatedFirmName);
                doc.appendElement(summaryInfoElement, bdFirmRepInfo);
            }
        }

        if (brokerListingSummaryVO.getTotalContractAssets() != null) {
			String totalContractAssets = BDConstants.HYPHON_SYMBOL;
			if (!reportData.getResultTooBigInd()) {
				totalContractAssets = NumberRender.formatByType(
						brokerListingSummaryVO.getTotalContractAssets(), null,
						RenderConstants.CURRENCY_TYPE);
			}
			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.TOTAL_CONTRACT_ASSETS, totalContractAssets);
        }
        
        if (brokerListingSummaryVO.getTotalNumberOfContracts() != null) {
			String totalNumOfContracts = BDConstants.HYPHON_SYMBOL;
			if (!reportData.getResultTooBigInd()) {
				totalNumOfContracts = NumberRender.formatByType(
						brokerListingSummaryVO.getTotalNumberOfContracts(),
						null, RenderConstants.INTEGER_TYPE);
			}

			doc.appendTextNode(summaryInfoElement,
					BDPdfConstants.TOTAL_NUM_OF_CONTRACTS, totalNumOfContracts);
        }
        
        if (brokerListingSummaryVO.getTotalNumberOfFinancialReps() != null) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_NUM_OF_FINANCIAL_REPS,
                    NumberRender.formatByType(brokerListingSummaryVO
                            .getTotalNumberOfFinancialReps(), null, RenderConstants.INTEGER_TYPE));
        }
        
        doc.appendElement(parentElement, summaryInfoElement);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showSummaryInfoInPDF().");
        }
    }

    /**
     * This method creates that part of XML that will show the Main report in the PDF.
     * 
     * @param parentElement - The Root Element.
     * @param doc - PDFDocument object.
     * @param reportData - Report Data.
     */
    @SuppressWarnings("unchecked")
    private void showMainReportInPDF(Element parentElement, PDFDocument doc,
            BrokerListingReportData reportData) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showMainReportInPDF().");
        }
        ArrayList<String> columnsList = BrokerListingUtility.getColumnsList();

        ArrayList<BrokerListingReportVO> brokerListingReportDetails = (ArrayList) reportData
                .getDetails();

        Element reportDtlsElement = doc.createElement(BDPdfConstants.REPORT_DETAILS);
        Integer maxRowsToBeShown = getMaxCappedRowsInPDF();
        
        if (brokerListingReportDetails != null) {
            for (BrokerListingReportVO brokerListingReportVO : brokerListingReportDetails) {
                if (maxRowsToBeShown-- <= 0) {
                    break;
                }
                
                Element reportDtlElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
                for (String columnID : columnsList) {
                    doc.appendTextNode(reportDtlElement, columnID, getColumnValue(columnID,
                            brokerListingReportVO));
                }
                doc.appendElement(reportDtlsElement, reportDtlElement);
            }
        }
        doc.appendElement(parentElement, reportDtlsElement);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showMainReportInPDF().");
        }
    }
    
    /**
     * This method will be called when the user clicks on download CSV button.
     */
    @Override
    protected byte[] getDownloadData(BaseReportForm form, ReportData report,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData().");
        }

        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);
        BrokerListingForm reportForm = (BrokerListingForm) form;
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BrokerListingReportData reportData = (BrokerListingReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
        
        if (reportData == null) {
            buff.append(BDConstants.SINGLE_SPACE_SYMBOL);
            return buff.toString().getBytes();
        }

        buff.append(BDConstants.CSV_FINANCIAL_REP_LISTING).append(LINE_BREAK);
        
        // Summary Information:
        buff.append(showSummaryInfoInCSV(reportData, userProfile));
        
        buff.append(LINE_BREAK);
        // Report as of & Filters used
        buff.append(showAsOfDateAndFiltersSelectedInCSV(reportForm, userProfile, request));
        
        buff.append(LINE_BREAK);
        // Main Report..
        buff.append(showMainReportInCSV(reportData, userProfile));
        
        // Messages to be displayed in CSV.
        String[] messages = null;
        try {
            messages = getMessagesToDisplay(request);
        } catch (ContentException ce) {
            if (logger.isDebugEnabled()) {
                logger.debug("Content Exception caught in getDownloadData() method: Exception is:"
                        + ce);
            }
        }
        if (messages != null) {
            int messageCount = 1;
            for (String message : messages) {
                buff.append(messageCount++).append(COMMA).append(message).append(LINE_BREAK);
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getDownloadData().");
        }
        return buff.toString().getBytes();
    }

    /**
     * This method will construct that part of CSV used to show Summary Information.
     * 
     * @param reportData - Report Data
     * @param userProfile - BDUserProfile object.
     * @return - String having the Summary Information in CSV format.
     */
    private String showSummaryInfoInCSV(BrokerListingReportData reportData,
            BDUserProfile userProfile) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showSummaryInfoInCSV().");
        }

        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);

        BrokerListingSummaryVO brokerListingSummaryVO = reportData.getBrokerListingSummaryVO();
        
        // Show user Name:
        if (userProfile.getRole() instanceof BDInternalUser && !userProfile.isInMimic()) {
            buff.append(BDConstants.CSV_USER_NAME).append(BDConstants.COLON_SYMBOL).append(COMMA);
            String userName = userProfile.getBDPrincipal().getFirstName()
                    + BDConstants.SINGLE_SPACE_SYMBOL + userProfile.getBDPrincipal().getLastName();
            buff.append(getCsvString(userName)).append(LINE_BREAK);
            
        } else if (userProfile.getRole() instanceof BDFirmRep) {
            buff.append(BDConstants.CSV_SUMMARY).append(BDConstants.COLON_SYMBOL).append(COMMA);
            List<BrokerDealerFirm> bdFirms = ((BDFirmRep) userProfile.getRole())
                    .getBrokerDealerFirmEntities();
            ArrayList<String> firmNames = new ArrayList<String>();
            for (BrokerDealerFirm bdFirm : bdFirms) {
                if (!firmNames.contains(bdFirm.getFirmName())) {
                    firmNames.add(bdFirm.getFirmName());
                }
            }
            
            String firstFirmName = firmNames.get(0);
            buff.append(getCsvString(firstFirmName)).append(LINE_BREAK);

            for (String firmName : firmNames.subList(1, firmNames.size())) {
                buff.append(BDConstants.SPACE_SYMBOL).append(COMMA).append(getCsvString(firmName))
                        .append(LINE_BREAK);
            }
        }
        
        buff.append(BDConstants.CSV_TOTAL_CONTRACT_ASSETS).append(BDConstants.COLON_SYMBOL).append(
                COMMA);
        if (brokerListingSummaryVO.getTotalContractAssets() != null) {
			String totalContractAssets = BDConstants.HYPHON_SYMBOL;
			if (!reportData.getResultTooBigInd()) {
				totalContractAssets = NumberRender.formatByType(
						brokerListingSummaryVO.getTotalContractAssets(), null,
						RenderConstants.CURRENCY_TYPE);
			}

			buff.append(getCsvString(totalContractAssets));
        }
        buff.append(LINE_BREAK);
        
        buff.append(BDConstants.CSV_TOTAL_NUM_OF_CONTRACTS).append(BDConstants.COLON_SYMBOL)
                .append(COMMA);
        if (brokerListingSummaryVO.getTotalNumberOfContracts() != null) {
			String totalNumberOfContracts = BDConstants.HYPHON_SYMBOL;
			if (!reportData.getResultTooBigInd()) {
				totalNumberOfContracts = NumberRender.formatByType(
						brokerListingSummaryVO.getTotalNumberOfContracts(),
						null, RenderConstants.INTEGER_TYPE);
			}

			buff.append(getCsvString(totalNumberOfContracts));
        }
        buff.append(LINE_BREAK);
        
        buff.append(BDConstants.CSV_TOTAL_NUM_OF_FINANCIAL_REPS).append(BDConstants.COLON_SYMBOL)
                .append(COMMA);
        if (brokerListingSummaryVO.getTotalNumberOfFinancialReps() != null) {
            buff.append(getCsvString(NumberRender.formatByType(brokerListingSummaryVO
                    .getTotalNumberOfFinancialReps(), null, RenderConstants.INTEGER_TYPE)));
        }
        buff.append(LINE_BREAK);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showSummaryInfoInCSV().");
        }
        return buff.toString();
    }

    /**
     * This method gets the "as of date" , "filers used" to be displayed in CSV file.
     * 
     * @param reportForm - BrokerListingForm object.
     * @param userProfile - BDUserProfile object.
     * @param request - The HttpServletRequest object.
     * @return - String having the CSV representation of "as of date", "filters used".
     * @throws SystemException
     */
    private String showAsOfDateAndFiltersSelectedInCSV(BrokerListingForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showAsOfDateAndFiltersSelectedInCSV().");
        }
        // Append Report As Of Date.
        ProtectedStringBuffer buff = new ProtectedStringBuffer(200);
        buff.append(BDConstants.CSV_REPORT_AS_OF).append(COMMA).append(
                DateRender.formatByStyle(getAsOfDate(reportForm), null,
                        RenderConstants.MEDIUM_STYLE)).append(LINE_BREAK);

        // Append Filters used.
        ArrayList<LabelValueBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        if (filtersUsed != null && !filtersUsed.isEmpty()) {
            LabelValueBean firstFilterUsed = filtersUsed.get(0);
			buff.append(BDConstants.CSV_FILTERS_USED)
				.append(BDConstants.COLON_SYMBOL).append(COMMA)
				.append(firstFilterUsed.getLabel()).append(COMMA)
				.append(getCsvString(firstFilterUsed.getValue())).append(LINE_BREAK);

            for (LabelValueBean filterUsed : filtersUsed.subList(1, filtersUsed.size())) {
                buff.append(BDConstants.SPACE_SYMBOL).append(COMMA).append(filterUsed.getLabel())
                        .append(COMMA).append(filterUsed.getValue()).append(LINE_BREAK);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showAsOfDateAndFiltersSelectedInCSV().");
        }
        return buff.toString();
    }

    /**
     * This method will give a ArrayList of filters used.
     * 
     * @param userProfile - BDUserProfile object.
     * @param reportForm - The Form.
     * @param request - HttpServletRequest object.
     * @return - An ArrayList of Filters used, and the Filter Value.
     * @throws SystemException
     */
    private ArrayList<LabelValueBean> getFiltersUsed(BDUserProfile userProfile,
            BrokerListingForm reportForm, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getFiltersUsed().");
        }

        ArrayList<LabelValueBean> filtersUsed = new ArrayList<LabelValueBean>();
        
        ArrayList<String> filters = null;
        
        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        // quickFilterSubmitted is null when accessing the page for the first time.
        if (quickFilterSubmitted != null && quickFilterSubmitted) {
            filters = BrokerListingUtility.quickFilters;
        } else {
            filters = BrokerListingUtility.advFilters;
        }

        for (String filterID : filters) {
            String filterValue = getFilterValue(filterID, reportForm, userProfile, request,
                    Boolean.TRUE);
            if (!StringUtils.isEmpty(filterValue)) {
                String filterTitle = BrokerListingUtility.filterDetails.get(filterID);
                if (filterTitle != null) {
                    filtersUsed.add(new LabelValueBean(filterTitle, filterValue));
                }
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getFiltersUsed().");
        }

        return filtersUsed;
    }

    /**
     * This method will create that part of CSV that shows the Main Report.
     * 
     * @param reportData - Report Data
     * @param userProfile - BDUserProfile object.
     * @return - String representing the Main Report in CSV format.
     */
    @SuppressWarnings("unchecked")
    private String showMainReportInCSV(BrokerListingReportData reportData, BDUserProfile userProfile) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showMainReportInCSV().");
        }

        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);

        ArrayList<String> columnsList = BrokerListingUtility.getColumnsList();
        for (String columnID : columnsList) {
            buff.append(getCsvString(BrokerListingUtility.getColumnsInfoMap().get(columnID)))
                    .append(COMMA);
        }
        buff.append(LINE_BREAK);
        
        ArrayList<BrokerListingReportVO> brokerListingReportDetails = (ArrayList) reportData
                .getDetails();

        if (brokerListingReportDetails != null) {
            for (BrokerListingReportVO brokerListingReportVO : brokerListingReportDetails) {
                for (String columnID : columnsList) {
                    String columnValue = getColumnValue(columnID, brokerListingReportVO);
                    if (StringUtils.isBlank(columnValue)) {
                        columnValue = BDConstants.SPACE_SYMBOL;
                    }
                    buff.append(getCsvString(columnValue.trim())).append(COMMA);
                }
                buff.append(LINE_BREAK);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showMainReportInCSV().");
        }

        return buff.toString();
    }

    /**
     * This method will return the File Name of the CSV file.
     * 
     * The CSV file will be of the Format "FinancialRepList-mmddyyyy.csv".
     */
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        return BrokerListingReportData.CSV_REPORT_NAME
                + BDConstants.HYPHON_SYMBOL
                + DateRender.format(getAsOfDate((BrokerListingForm) form),
                        RenderConstants.MEDIUM_MDY_SLASHED).replace(BDConstants.SLASH_SYMBOL,
                        BDConstants.SPACE_SYMBOL) + CSV_EXTENSION;
    }

    /**
     * This method will return the columnValue taken from the BrokerListingReportVO, given a Column
     * ID.
     * 
     * @param columnID - the column ID.
     * @param brokerListingReportVO - The report having the Column Information.
     * @return
     */
    private String getColumnValue(String columnID, BrokerListingReportVO brokerListingReportVO) {
        if (logger.isDebugEnabled()) {
            logger.debug("inside getColumnValue().");
        }

        if (BrokerListingReportData.COL_FINANCIAL_REP_NAME_ID.equals(columnID)) {
             return brokerListingReportVO.getFinancialRepName();
        }
        else if (BrokerListingReportData.COL_FIRM_NAME_ID.equals(columnID)) {
            return brokerListingReportVO.getFirmName();
        }
        else if (BrokerListingReportData.COL_CITY_ID.equals(columnID)) {
            if (StringUtils.isBlank(brokerListingReportVO.getCity())) {
                return BDConstants.NOT_APPLICABLE;
            } else {
                return brokerListingReportVO.getCity();
            }
        }
        else if (BrokerListingReportData.COL_STATE_ID.equals(columnID)) {
            if (StringUtils.isBlank(brokerListingReportVO.getState())) {
                return BDConstants.NOT_APPLICABLE;
            } else {
                return brokerListingReportVO.getState();
            }
        }
        else if (BrokerListingReportData.COL_ZIP_CODE_ID.equals(columnID)) {
            if (StringUtils.isBlank(brokerListingReportVO.getZipCode())) {
                return BDConstants.NOT_APPLICABLE;
            } else {
                return brokerListingReportVO.getZipCode();
            }
        }
        else if (BrokerListingReportData.COL_PRODUCER_CODE_ID.equals(columnID)) {
            return brokerListingReportVO.getProducerCode();
        }
        else if (BrokerListingReportData.COL_NUM_OF_CONTRACTS_ID.equals(columnID)) {
            if (brokerListingReportVO.getNumOfContracts() != null) {
                return NumberRender.formatByType(brokerListingReportVO.getNumOfContracts(), null,
                    RenderConstants.INTEGER_TYPE, Boolean.FALSE);
            }
        }
        else if (BrokerListingReportData.COL_BL_TOTAL_ASSETS_ID.equals(columnID)) {
            if (brokerListingReportVO.getTotalAssets() != null) {
                return NumberRender.formatByType(brokerListingReportVO.getTotalAssets(), null,
                    RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
            } else {
                return BDConstants.HYPHON_SYMBOL;
            }
        }
        
        return null;
    }
    
    /**
     * This method helps in finding out the report handler.
     */
    @Override
    protected String getReportId() {
        return BrokerListingReportData.REPORT_ID;
    }

    /**
     * This method gives back the CSV Name.
     */
    @Override
    protected String getReportName() {
        return BrokerListingReportData.CSV_REPORT_NAME;
    }

    /**
     * This method is used to populate Form.
     * 
     * 1. If the Advance Filter has been submitted, the Quick Filter values are reset. 2. Also,
     * based on, if Advance Filter was submitted or, Quick Filter was submitted, the
     * "Advance Filter" section is either shown or not shown.
     */
    @Override
    protected void populateReportForm( BaseReportForm reportForm,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportForm()");
        }
        super.populateReportForm( reportForm, request);

        String task = getTask(request);

        BrokerListingForm form = (BrokerListingForm) reportForm;
        // When Advanced Filter is submitted, the Quick Filter expression values are being reset.
        Boolean quickFilterSubmitted = form.getFromQuickFilter();
        if (quickFilterSubmitted != null && !quickFilterSubmitted) {
            form.resetQuickFilter();
        }

        if (!(DOWNLOAD_TASK.equals(task) || BDConstants.PRINT_PDF_TASK.equals(task) || BDConstants.DOWNLOAD_ALL_TASK
                .equals(task))) {
            // quickFilterSubmitted is null when we visit the page for the first time.
            quickFilterSubmitted = form.getFromQuickFilter();
            if (quickFilterSubmitted == null) {
                form.setShowAdvanceFilter(false);
            } else if (quickFilterSubmitted) {
                form.setShowAdvanceFilter(false);
            } else {
                form.setShowAdvanceFilter(true);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportForm()");
        }
    }

    /**
     * Sorting is done only based on the given sort field, Secondary sort column Producer Code. 
     */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        String sortField = form.getSortField();
        String sortDirection = form.getSortDirection();

    	criteria.insertSort(sortField, sortDirection);
    	if (!BrokerListingReportData.COL_PRODUCER_CODE_ID.equals(sortField)) {
            criteria.insertSort(BrokerListingReportData.COL_PRODUCER_CODE_ID, ReportSort.ASC_DIRECTION);
    	}
    }
    
    /**
     * This method will get the Filter Information entered by the user and place it in the
     * ReportCriteria.
     */
    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria().");
        }

        BrokerListingForm reportForm = (BrokerListingForm) form;

        BDUserProfile userProfile = (BDUserProfile) BDSessionHelper.getUserProfile(request);
        if (userProfile == null) {
            throw new SystemException("UserProfile is null");
        }
        addUserProfileRelatedFilterCriteria(userProfile, criteria, reportForm, request);

        String dbSessionID = getFilterValue(BrokerListingReportData.FILTER_DB_SESSION_ID,
                reportForm, userProfile, request, Boolean.FALSE);
        if (!StringUtils.isEmpty(dbSessionID)) {
            addFilterCriteria(criteria,
                    BrokerListingReportData.FILTER_DB_SESSION_ID, Integer
                    .valueOf(dbSessionID));
        }

        addFilterCriteria(criteria, BrokerListingReportData.FILTER_AS_OF_DATE, getFilterValue(
                BrokerListingReportData.FILTER_AS_OF_DATE, reportForm, userProfile, request,
                Boolean.FALSE));

        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        // quickFilterSubmitted is null when accessing the page for the first time.
        if (quickFilterSubmitted == null) {
            return;
        }
        if (quickFilterSubmitted) {

            String quickFilterSelected = reportForm.getQuickFilterSelected();
            ArrayList<Integer> partyIDList = new ArrayList<Integer>();

            if (BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID.equals(quickFilterSelected)) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID,
                        getFilterValue(BrokerListingReportData.FILTER_QF_FINANCIALREP_NAME_ID,
                                reportForm, userProfile, request, Boolean.FALSE));

            } else if (BrokerListingReportData.FILTER_BDFIRM_NAME_ID.equals(quickFilterSelected)) {
                String bdFirmNameID = getFilterValue(
                        BrokerListingReportData.FILTER_QF_BDFIRM_NAME_ID, reportForm, userProfile,
                        request, Boolean.FALSE);

                // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
                // the filter criteria, we should be sending the Firm name as the filter criteria.
                if (BDUserProfileHelper.isFirmRep(userProfile) || userProfile.getRole() instanceof BDRvp) {
                addFilterCriteria(criteria,
                            BrokerListingReportData.FILTER_BROKER_DEALER_FIRM_NAME, bdFirmNameID);
                } else {
                    // Send the partyID as the filter criteria.
                    if (!StringUtils.isEmpty(bdFirmNameID) && StringUtils.isNumeric(bdFirmNameID)) {
                        partyIDList.add(new Integer(bdFirmNameID));
                    }
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID, partyIDList);
                }
            } else if (BrokerListingReportData.FILTER_CITY_NAME_ID.equals(quickFilterSelected)) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_CITY_NAME_ID,
                        getFilterValue(BrokerListingReportData.FILTER_QF_CITY_NAME_ID, reportForm,
                                userProfile, request, Boolean.FALSE));

            } else if (BrokerListingReportData.FILTER_STATE_CODE_ID.equals(quickFilterSelected)) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_STATE_CODE_ID,
                        getFilterValue(BrokerListingReportData.FILTER_QF_STATE_CODE_ID, reportForm,
                                userProfile, request, Boolean.FALSE));

            } else if (BrokerListingReportData.FILTER_ZIP_CODE_ID.equals(quickFilterSelected)) {
                String zipCode = getFilterValue(BrokerListingReportData.FILTER_QF_ZIP_CODE_ID,
                        reportForm, userProfile, request, Boolean.FALSE);
                if (zipCode != null && zipCode.length() == 5) {
                    addFilterCriteria(criteria, BrokerListingReportData.FILTER_ZIP_CODE_ID, zipCode);
                }
            } else if (BrokerListingReportData.FILTER_PRODUCER_CODE_ID.equals(quickFilterSelected)) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_PRODUCER_CODE_ID,
                        getFilterValue(BrokerListingReportData.FILTER_QF_PRODUCER_CODE_ID,
                                reportForm, userProfile, request, Boolean.FALSE));

            } else if (BrokerListingReportData.FILTER_RVP_ID.equals(quickFilterSelected)) {
                String rvpNameID = getFilterValue(BrokerListingReportData.FILTER_QF_RVP_ID,
                        reportForm, userProfile, request, Boolean.FALSE);
                if (!StringUtils.isEmpty(rvpNameID)) {
                    partyIDList.add(new Integer(rvpNameID));
                }
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID, partyIDList);
                
            } else if (BrokerListingReportData.FILTER_REGION_ID.equals(quickFilterSelected)) {
                String rvpNameID = getFilterValue(BrokerListingReportData.FILTER_QF_REGION_ID,
                        reportForm, userProfile, request, Boolean.FALSE);
                if (!StringUtils.isEmpty(rvpNameID)) {
                    partyIDList.add(new Integer(rvpNameID));
                }
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID, partyIDList);
                
            } else if (BrokerListingReportData.FILTER_DIVISION_ID.equals(quickFilterSelected)) {
                String rvpNameID = getFilterValue(BrokerListingReportData.FILTER_QF_DIVISION_ID,
                        reportForm, userProfile, request, Boolean.FALSE);
                if (!StringUtils.isEmpty(rvpNameID)) {
                    partyIDList.add(new Integer(rvpNameID));
                }
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID, partyIDList);
            }
        } else {
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID,
                    getFilterValue(BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID,
                            reportForm,
                            userProfile, request, Boolean.FALSE));
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_CITY_NAME_ID,
                    getFilterValue(BrokerListingReportData.FILTER_CITY_NAME_ID, reportForm,
                            userProfile, request, Boolean.FALSE));
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_STATE_CODE_ID,
                    getFilterValue(BrokerListingReportData.FILTER_STATE_CODE_ID, reportForm,
                            userProfile, request, Boolean.FALSE));
            String zipCode = getFilterValue(BrokerListingReportData.FILTER_ZIP_CODE_ID, reportForm,
                    userProfile, request, Boolean.FALSE);
            if (zipCode != null && zipCode.length() == 5) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_ZIP_CODE_ID, zipCode);
            }
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_PRODUCER_CODE_ID,
                    getFilterValue(BrokerListingReportData.FILTER_PRODUCER_CODE_ID, reportForm,
                            userProfile, request, Boolean.FALSE));
            
            // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
            // the filter criteria, we should be sending the Firm name as the filter criteria.
            if (BDUserProfileHelper.isFirmRep(userProfile) || userProfile.getRole() instanceof BDRvp) {
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_BROKER_DEALER_FIRM_NAME,
                        getFilterValue(BrokerListingReportData.FILTER_BDFIRM_NAME_ID,
                                reportForm, userProfile, request, Boolean.FALSE));
            }
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_PARTY_ID, getListOfPartyIDs(
                    reportForm, userProfile, request)); 
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportCriteria().");
        }
    }

    /**
     * Add userProfile specific filter criteria such as UserProfileID, userRole.
     * 
     * @param userProfile
     * @param criteria
     * @throws SystemException
     */
    private void addUserProfileRelatedFilterCriteria(BDUserProfile userProfile,
            ReportCriteria criteria, BrokerListingForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> addUserProfileRelatedFilterCriteria()");
        }
        // In Mimic mode, the user profile id is of the user who is mimicking. When not in mimic
        // mode, it is the user profile id of the user currently logged in.
        String userProfileID = getFilterValue(BrokerListingReportData.FILTER_USER_PROFILE_ID,
                reportForm, userProfile, request, Boolean.FALSE);
        if (userProfileID != null) {
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_USER_PROFILE_ID, Long
                    .valueOf(userProfileID));
        }

        // In Mimic mode, the user role is of the user who is mimicking. When not in mimic
        // mode, it is the user role of the user currently logged in.
        addFilterCriteria(criteria, BrokerListingReportData.FILTER_USER_ROLE, getFilterValue(
                BrokerListingReportData.FILTER_USER_ROLE, reportForm, userProfile, request,
                Boolean.FALSE));

        if (userProfile.isInMimic()) {
            // In Mimic mode, the user profile id is of the user who is mimicked.
            userProfileID = getFilterValue(BrokerListingReportData.FILTER_MIMIC_USER_PROFILE_ID,
                    reportForm, userProfile, request, Boolean.FALSE);
            if (userProfileID != null) {
                addFilterCriteria(criteria, BrokerListingReportData.FILTER_MIMIC_USER_PROFILE_ID,
                        Long.valueOf(userProfileID));
            }

            // In Mimic mode, the user role is of the user who is mimicked.
            addFilterCriteria(criteria, BrokerListingReportData.FILTER_MIMIC_USER_ROLE,
                    getFilterValue(BrokerListingReportData.FILTER_MIMIC_USER_ROLE, reportForm,
                            userProfile, request, Boolean.FALSE));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> addUserProfileRelatedFilterCriteria()");
        }
    }

    /**
     * This method gives back a list of partyID's selected as Filter.
     * 
     * If one of the filters RVP, Sales Region, Sales Division, Firm Rep Name are used as Filtering
     * Criteria, the party ID of the selected value will be sent back to the stored proc to get back
     * the filtered report.
     * 
     * @param reportForm
     * @return - List of PartyID's of selected Filters.
     * @throws SystemException
     */
    private ArrayList<Integer> getListOfPartyIDs(BrokerListingForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getListOfPartyIDs()");
        }
        ArrayList<Integer> partyIDList = new ArrayList<Integer>();

        
        // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
        // the filter criteria, we should be sending the Firm name as the filter criteria.
        if (!BDUserProfileHelper.isFirmRep(userProfile) && !(userProfile.getRole() instanceof BDRvp)) {
            String bdFirmNameID = getFilterValue(BrokerListingReportData.FILTER_BDFIRM_NAME_ID,
                    reportForm, userProfile, request, Boolean.FALSE);
            if (!StringUtils.isEmpty(bdFirmNameID) && StringUtils.isNumeric(bdFirmNameID)) {
                partyIDList.add(new Integer(bdFirmNameID));
            }
        }
        String rpvNameID = getFilterValue(BrokerListingReportData.FILTER_RVP_ID, reportForm,
                userProfile, request, Boolean.FALSE);
        if (!StringUtils.isEmpty(rpvNameID)) {
            partyIDList.add(new Integer(rpvNameID));
        }
        String salesRegionID = getFilterValue(BrokerListingReportData.FILTER_REGION_ID,
                reportForm,
                userProfile, request, Boolean.FALSE);
        if (!StringUtils.isEmpty(salesRegionID)) {
            partyIDList.add(new Integer(salesRegionID));
        }
        String salesDivisionID = getFilterValue(BrokerListingReportData.FILTER_DIVISION_ID,
                reportForm, userProfile, request, Boolean.FALSE);
        if (!StringUtils.isEmpty(salesDivisionID)) {
            partyIDList.add(new Integer(salesDivisionID));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getListOfPartyIDs()");
        }
        return partyIDList;
    }

    /**
     * This method returns the Filter value submitted by the user, given the filter name.
     * 
     * @param filterID - the filter name
     * @param reportForm - BrokerListingForm object.
     * @param userProfile - BDUSerProfile object.
     * @param request - the HttpServletRequest object.
     * @param isCsvOrPdf - boolean variable which will tell us if we want to use the Filter value to
     *            display in CSV or PDF.
     * @return - the filter value.
     * @throws SystemException
     */
    private String getFilterValue(String filterID, BrokerListingForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request, boolean isCsvOrPdf)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("inside getFilterValue()");
        }
        try {
            if (BrokerListingReportData.FILTER_USER_PROFILE_ID.equals(filterID)) {
                // If the Internal user is mimicking a external user, the user Profile ID is of that
                // Internal user. If the user is not in mimick mode, then, the user Profile ID is of
                // the current user logged in.
                if (userProfile.isInMimic()) {
                    BDUserProfile mimickingInternalUserProfile = BlockOfBusinessUtility
                            .getMimckingUserProfile(request);
                    if (mimickingInternalUserProfile == null) {
                        return null;
                    }
                    
                    return String.valueOf(mimickingInternalUserProfile.getBDPrincipal()
                            .getProfileId());
                } else {
                    return String.valueOf(userProfile.getBDPrincipal().getProfileId());
                }
            } 
            else if (BrokerListingReportData.FILTER_USER_ROLE.equals(filterID)) {
                // If the Internal user is mimicking a external user, the user Role is of that
                // Internal user. If the user is not in mimick mode, then, the user Role is of
                // the current user logged in.
                if (userProfile.isInMimic()) {
                    BDUserProfile mimickingInternalUserProfile = BlockOfBusinessUtility
                            .getMimckingUserProfile(request);
                    if (mimickingInternalUserProfile == null) {
                        return null;
                    }
                    
                    return mimickingInternalUserProfile.getRole().getRoleType().getUserRoleCode();
                } else {
                    return userProfile.getRole().getRoleType().getUserRoleCode();
                }
            } else if (BrokerListingReportData.FILTER_MIMIC_USER_PROFILE_ID.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Profile ID
                // is of that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return String.valueOf(userProfile.getBDPrincipal().getProfileId());
                }
            } else if (BrokerListingReportData.FILTER_MIMIC_USER_ROLE.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Role is of
                // that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return userProfile.getRole().getRoleType().getUserRoleCode();
                }
            }
            else if (BrokerListingReportData.FILTER_DB_SESSION_ID.equals(filterID)) {

                Integer dbSessionID = getStoredProcSessionIDForAsOfDate(reportForm, request) == null ? null
                        : getStoredProcSessionIDForAsOfDate(reportForm, request);
                return dbSessionID == null ? null : dbSessionID.toString();
            }
            else if (BrokerListingReportData.FILTER_AS_OF_DATE.equals(filterID)) {
                Date reportAsOfDate = getAsOfDate(reportForm);
                if (reportAsOfDate != null) {
                    Long reportAsOfDateL = reportAsOfDate.getTime();
                    return reportAsOfDateL.toString();
                }
                return null;
            } 
            else if (BrokerListingReportData.FILTER_FINANCIALREP_NAME_ID.equals(filterID)) {
                return reportForm.getFinancialRepName();
            } 
            else if (BrokerListingReportData.FILTER_BDFIRM_NAME_ID.equals(filterID)) {
                // The filter criteria for a Firm Rep, RVP user is passed as the Firm Name. For
                // other users, the filter criteria is passed as Firm ID. This method returns either
                // Firm Name or Firm ID depending on if the user is a Firmrep/RVP or any other
                // Internal User.
                if (BDUserProfileHelper.isFirmRep(userProfile) || userProfile.getRole() instanceof BDRvp) {
                    String firmName = BDConstants.SPACE_SYMBOL;
                    // For a Firm Rep User, since we show the Firm Name filter in JSP page as a
                    // drop down box, the quickFilterFirmNameSelected does not get populated in
                    // JSP page. We just have the quickFilterFirmIDSelected populated (based on
                    // the firm selected by the user from drop down). We need to get the Firm
                    // Name corresponding to the Firm ID.
                    if (userProfile.getRole() instanceof BDFirmRep) {
                        firmName = BlockOfBusinessUtility.getFirmNameForAssociatedFirmID(
                                userProfile, reportForm.getBdFirmID());
                    } else {
                        firmName = reportForm.getBdFirmName();
                    }
                    return firmName;

                } else {
                    if (isCsvOrPdf) {
                        return reportForm.getBdFirmName();
                    } else {
                    return reportForm.getBdFirmID();
                }
            } 
            } 
            else if (BrokerListingReportData.FILTER_CITY_NAME_ID.equals(filterID)) {
                return reportForm.getCityName();
            } 
            else if (BrokerListingReportData.FILTER_STATE_CODE_ID.equals(filterID)) {
                return reportForm.getStateCode();
            } 
            else if (BrokerListingReportData.FILTER_ZIP_CODE_ID.equals(filterID)) {
                return reportForm.getZipCode();
            } 
            else if (BrokerListingReportData.FILTER_PRODUCER_CODE_ID.equals(filterID)) {
                return reportForm.getProducerCode();
            } 
            else if (BrokerListingReportData.FILTER_RVP_ID.equals(filterID)) {
                // If we want to show the RVP for display purpose, return the RVP Name, else return
                // RVP id.
                if (isCsvOrPdf) {
                    String rvpName = BlockOfBusinessUtility.getRvpNameForIDSelected(reportForm
                            .getRvpName());
                    return rvpName;
                } else {
                    return reportForm.getRvpName();
                }
            } 
            else if (BrokerListingReportData.FILTER_REGION_ID.equals(filterID)) {
                // If we want to show the Region for display purpose, return the Region Name, else
                // return Region id.
                if (isCsvOrPdf) {
                    String regionName = BlockOfBusinessUtility
                            .getRegionNameForIDSelected(reportForm.getSalesRegion());
                    return regionName;
                } else {
                    return reportForm.getSalesRegion();
                }
            } 
            else if (BrokerListingReportData.FILTER_DIVISION_ID.equals(filterID)) {
                // If we want to show the Division for display purpose, return the Division Name,
                // else return Division id.
                if (isCsvOrPdf) {
                    String divisionName = BlockOfBusinessUtility
                            .getDivisionNameForIDSelected(reportForm.getSalesDivision());
                    return divisionName;
                } else {
                    return reportForm.getSalesDivision();
                }
            } 
            else if (BrokerListingReportData.FILTER_QF_FINANCIALREP_NAME_ID.equals(filterID)) {
                return reportForm.getQuickFilterFinancialRepName();
            } 
            else if (BrokerListingReportData.FILTER_QF_BDFIRM_NAME_ID.equals(filterID)) {
                // The filter criteria for a Firm Rep, RVP user is passed as the Firm Name. For
                // other users, the filter criteria is passed as Firm ID. This method returns either
                // Firm Name or Firm ID depending on if the user is a Firmrep/RVP or any other
                // Internal User.
                if (BDUserProfileHelper.isFirmRep(userProfile) || userProfile.getRole() instanceof BDRvp) {
                    String firmName = BDConstants.SPACE_SYMBOL;
                    // For a Firm Rep User, since we show the Firm Name filter in JSP page as a
                    // drop down box, the quickFilterFirmNameSelected does not get populated in
                    // JSP page. We just have the quickFilterFirmIDSelected populated (based on
                    // the firm selected by the user from drop down). We need to get the Firm
                    // Name corresponding to the Firm ID.
                    if (userProfile.getRole() instanceof BDFirmRep) {
                        firmName = BlockOfBusinessUtility.getFirmNameForAssociatedFirmID(
                                userProfile, reportForm.getQuickFilterBDFirmID());
                    } else {
                        firmName = reportForm.getQuickFilterBDFirmName();
                    }
                    return firmName;

                } else {
                    if (isCsvOrPdf) {
                        return reportForm.getQuickFilterBDFirmName();
                    } else {
                    return reportForm.getQuickFilterBDFirmID();
                }
            } 
            } 
            else if (BrokerListingReportData.FILTER_QF_CITY_NAME_ID.equals(filterID)) {
                return reportForm.getQuickFilterCityName();
            } 
            else if (BrokerListingReportData.FILTER_QF_STATE_CODE_ID.equals(filterID)) {
                return reportForm.getQuickFilterStateCode();
            } 
            else if (BrokerListingReportData.FILTER_QF_ZIP_CODE_ID.equals(filterID)) {
                return reportForm.getQuickFilterZipCode();
            } 
            else if (BrokerListingReportData.FILTER_QF_PRODUCER_CODE_ID.equals(filterID)) {
                return reportForm.getQuickFilterProducerCode();
            } 
            else if (BrokerListingReportData.FILTER_QF_RVP_ID.equals(filterID)) {
                // If we want to show the RVP for display purpose, return the RVP Name, else return
                // RVP id.
                if (isCsvOrPdf) {
                    String rvpName = BlockOfBusinessUtility.getRvpNameForIDSelected(reportForm
                            .getQuickFilterRVPName());
                    return rvpName;
                } else {
                    return reportForm.getQuickFilterRVPName();
                }
            } 
            else if (BrokerListingReportData.FILTER_QF_REGION_ID.equals(filterID)) {
                // If we want to show the Region for display purpose, return the Region Name, else
                // return Region id.
                if (isCsvOrPdf) {
                    String regionName = BlockOfBusinessUtility
                            .getRegionNameForIDSelected(reportForm.getQuickFilterSalesRegion());
                    return regionName;
                } else {
                    return reportForm.getQuickFilterSalesRegion();
                }
            } 
            else if (BrokerListingReportData.FILTER_QF_DIVISION_ID.equals(filterID)) {
                // If we want to show the Division for display purpose, return the Division Name,
                // else return Division id.
                if (isCsvOrPdf) {
                    String divisionName = BlockOfBusinessUtility
                            .getDivisionNameForIDSelected(reportForm.getQuickFilterSalesDivision());
                    return divisionName;
                } else {
                    return reportForm.getQuickFilterSalesDivision();
                }
            }
        } catch (NullPointerException ne) {
            // Do Nothing.
        }
        return null;
    }

    /**
     * This method gives the as of date selected by the user.
     * 
     * @param reportForm - BrokerListingForm object.
     * @return - The as of date.
     */
    private Date getAsOfDate(BrokerListingForm reportForm) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAsOfDate().");
        }
        String asOfDateSelected = reportForm.getAsOfDateSelected();
        List<Date> asOfDateList = null;
        Date asOfDate = null;
        // "asOfDateSelected" is null if the default asOfDate was not changed by user in the page.
        if (StringUtils.isEmpty(asOfDateSelected)) {
            try {
                asOfDateList = BlockOfBusinessUtility.getMonthEndDates();
            } catch (SystemException e) {
                // Do Nothing.
            }
            if (asOfDateList != null && !asOfDateList.isEmpty()) {
                asOfDate = asOfDateList.get(0);
            }
        } else {
            asOfDate = new Date(Long.valueOf(asOfDateSelected));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getAsOfDate().");
        }
        return asOfDate;
    }

    /**
     * This method is used to validate the report, post the report data has been created.
     * 
     * @throws SystemException
     */
    protected void validateReportData(ReportData report, BaseReportForm form,
            HttpServletRequest request) throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> validateReportData()");
        }
        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
        ArrayList<GenericException> infoMessagesUnderColumnHeader = new ArrayList<GenericException>();
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
        
        BrokerListingForm reportForm = (BrokerListingForm) form;
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

        BrokerListingReportData reportData = (BrokerListingReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);

        // Setting the values in Summary section as 0, if they are coming thru stored proc as null.
        if (reportData != null) {
            BrokerListingSummaryVO brokerListingSummaryVO = reportData.getBrokerListingSummaryVO();
            if (brokerListingSummaryVO != null) {
                if (brokerListingSummaryVO.getTotalContractAssets() == null) {
                    brokerListingSummaryVO.setTotalContractAssets(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (brokerListingSummaryVO.getTotalNumberOfContracts() == null) {
                    brokerListingSummaryVO.setTotalNumberOfContracts(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (brokerListingSummaryVO.getTotalNumberOfFinancialReps() == null) {
                    brokerListingSummaryVO.setTotalNumberOfFinancialReps(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
            }
        }

        /**
         * #1. If current report data is not equal to the latest as of date, then, show the
         * Historical Message Box. Although this Informational message condition check does not
         * belong in this method, for the lack of a better place, this error check has been done
         * here.
         */
        boolean isReportAsOfDateLatest = isReportAsOfDateLatest(reportForm);
        if (isReportAsOfDateLatest) {
            GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                    BDContentConstants.BROKER_LISTING_HISTORICAL_CONTRACT_INFORMATION,
                    ContentTypeManager.instance().MISCELLANEOUS,false);
            infoMessages.add(exception);
        }

        /**
         * #2. Show a Informational message to the user if the current filter criteria did not fetch
         * any results.
         */
        if (reportData != null) {
			if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userProfile
					.getRole())
					&& reportData.getResultTooBigInd()) {
				reportData.setDetails(null);
				request.setAttribute(BDConstants.REPORT_BEAN, reportData);
				GenericException exception = new GenericException(
						BDErrorCodes.BOB_RESULT_TOO_BIG);
				errorMessages.add(exception);
			} else if (reportData.getDetails() != null
					&& reportData.getDetails().size() == 0) {
                ArrayList<LabelValueBean> filtersUsed = getFiltersUsed(userProfile, reportForm,
                        request);
                if (filtersUsed != null && !filtersUsed.isEmpty()) {
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BROKER_LISTING_NO_PRODUCERS_FOR_FILTER_ENTERED,
                            ContentTypeManager.instance().MISCELLANEOUS,false);
                    infoMessagesUnderColumnHeader.add(exception);
                }
            }
        }
        
        if (!infoMessages.isEmpty()) {
            setMessagesInRequest(request, infoMessages);
        }
		if (!errorMessages.isEmpty()) {
			setMessagesInRequest(request, errorMessages,
					BDConstants.ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER);
		}
        if (!infoMessagesUnderColumnHeader.isEmpty()) {
            setMessagesInRequest(request, infoMessagesUnderColumnHeader,
                    BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        }
    }

    /**
     * This method will check if the current as of date selected is the latest or not.
     * 
     * @param reportForm - The report form.
     * @return - true, if the report as of date is the latest, else, false.
     */
    private boolean isReportAsOfDateLatest(BrokerListingForm reportForm) {
        Date asOfDateSelected = getAsOfDate(reportForm);
        Date latestAsOfDate = null;
        try {
            latestAsOfDate = BlockOfBusinessUtility.getMonthEndDates().get(0);
        } catch (SystemException se) {
            // Do nothing.
        }
        if (latestAsOfDate != null) {
            if (latestAsOfDate.compareTo(asOfDateSelected) != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to get the Messages to be displayed.
     * 
     * @param request - HttpServletRequest object
     * @return - An array of Error Messages to be displayed.
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    private String[] getMessagesToDisplay(HttpServletRequest request) throws ContentException {
        ArrayList<GenericException> messages = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MESSAGES);

        ArrayList<GenericException> messagesUnderColumnHeader = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);

        if (messagesUnderColumnHeader != null && !messagesUnderColumnHeader.isEmpty()) {
            if (messages != null) {
                messages.addAll(messagesUnderColumnHeader);
            } else {
                messages = messagesUnderColumnHeader;
            }
        }
        
        String[] messageColl = null;
        if (messages != null) {
            messageColl = ContentHelper.getMessagesUsingContentType(messages);
        }
        return messageColl;
    }

    /**
     * This sets XML elements of Informational messages common for reports
     * 
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    public void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement,
            HttpServletRequest request) {
        ArrayList<GenericException> messages = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MESSAGES);

        ArrayList<GenericException> messagesUnderColumnHeader = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);

        if (messagesUnderColumnHeader != null && !messagesUnderColumnHeader.isEmpty()) {
            if (messages != null) {
                messages.addAll(messagesUnderColumnHeader);
            } else {
                messages = messagesUnderColumnHeader;
            }
        }

        setInfoMessagesXMLElements(doc, rootElement, messages);

	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    
    @Autowired
	   private BDValidatorFWError  bdValidatorFWError;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWError);
}
	
}
