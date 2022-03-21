package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBColumnCriteria;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBColumnsApplicableToTab;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBFilterCriteria;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBFilterMap;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.FilterInfoBean;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.BDAuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.navigation.UserMenu;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.ProtectedStringBuffer;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.LabelInfoBean;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.bob.dao.BlockOfBusinessDAO;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO;
import com.manulife.pension.ps.service.report.bob.valueobject.BrokerInfoVO;
import com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeDetailsVO;
import com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeRangeVO;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.fee.util.Constants;
import com.manulife.pension.service.fee.valueobject.RiaBandItem;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.util.ArrayUtility;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * This class builds the Block Of Business page.
 * 
 * @author harlomte
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"blockOfBusinessForm"})

public class BlockOfBusinessPendingDiscontinuedController extends BDReportController {
	@ModelAttribute("blockOfBusinessForm") 
	public BlockOfBusinessForm populateForm() 
	{
		return new BlockOfBusinessForm();
		}
	
	

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessDiscontinued.jsp");
		forwards.put("blockOfBusinessDiscontinued","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("inputRedirect","redirect:/do/bob/blockOfBusiness/Active/");
    	forwards.put("bobToContractInformation","redirect:/do/bob/contract/contractInformation/");
    	forwards.put("default","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("sort","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("filter","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("page","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("download","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("downloadAll","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("printPDF","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("refresh","/bob/blockOfBusinessDiscontinued.jsp");
    	forwards.put("planReviewReports","redirect:/do/bob/planReview/");
    	forwards.put("blockOfBusinessActive","/bob/blockOfBusinessActive.jsp");
		forwards.put("blockOfBusinessPending","/bob/blockOfBusinessPending.jsp");
		forwards.put("blockOfBusinessOutstanding","/bob/blockOfBusinessOutstanding.jsp");
		}

	
    private static final String XSLT_FILE_KEY_NAME = "BlockOfBusinessReport.XSLFile";
    
    protected static final String CLASS_STRING = "class";

    private Logger logger = Logger.getLogger(BlockOfBusinessPendingDiscontinuedController.class);

    private Category interactionLog = Category.getInstance(ServiceLogRecord.class);

    private ServiceLogRecord logRecord = new ServiceLogRecord("BlockOfBusinessAction");

    /**
     * Constructor class.
     */
    public BlockOfBusinessPendingDiscontinuedController() {
        super(BlockOfBusinessPendingDiscontinuedController.class);
    }

    /**
     * The preExecute() method was overridden to solve the back button problem.
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
        
        String contractNum = request.getParameter(BDConstants.CONTRACT_NUMBER);
        if (contractNum != null) {
            // We have received a request to go to Contract Information page. Redirecting to
            // Contract Information page.
            try {
                BobContextUtils.setUpBobContext(request);
            } catch (SystemException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "SystemException caught in BlockOfBusinessAction -> execute() method:"
                                    + e.getUniqueId(), e);
                } // fi

                return forwards.get("input");
            }
            
            // BobContext could be null, if the user has not yet gone to any other contract-specific
            // page in the current session.
            // The contract in the BobContext could be null, if the contractNum request parameter
            // send had a invalid contract number value or the user does not have access to that
            // contract number.
            if (BDSessionHelper.getBobContext(request) == null
                    || BDSessionHelper.getBobContext(request).getCurrentContract() == null) {
                forward = forwards.get(BDConstants.BOB_INPUT_REDIRECT_FORWARD);
            } else {
                forward = forwards.get(BDConstants.BOB_CONTRACT_INFO_REDIRECT_FORWARD);
            }
            
            return forward;
        }
        
        if (StringUtils.equalsIgnoreCase(BDConstants.POST, request.getMethod())) {
//TODO need to take cared below line 222
            // do a refresh so that there's no problem using the back button
        	ControllerForward forward1 = new ControllerForward(BDConstants.REFRESH, BDConstants.DO
                    + new UrlPathHelper().getPathWithinServletMapping(request) + BDConstants.QTASK + getTask(request), true);
        		//ControllerForward  forward1 = new ControllerForward("refresh", "/do" + new UrlPathHelper().getPathWithinServletMapping(request), true); 
        		return "redirect:"+forward1.getPath();
        }
        return forward;
    }

    /**
     * The doDefault() method has been overridden to add the functionality: - if the user is able to
     * view the "Advance Filtering" section, and if he clicks on another tab, the
     * "Advance Filtering" section should still be visible.
     */  

    @RequestMapping(value ="/blockOfBusiness/Discontinued/",  method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault() in BlockOfBusinessAction.");
        }
         forward = super.doDefault( actionForm, request, response);      
        // When we change the tab, we get the "showAdvanceFilter" option as request parameter.
        String showAdvanceFilter = request.getParameter(BDConstants.REQ_PARAM_SHOW_ADVANCE_FILTER);
        if (showAdvanceFilter != null) {
            if (Boolean.parseBoolean(showAdvanceFilter)) {
            	actionForm.setShowAdvanceFilter(true);
            } else {
            	actionForm.setShowAdvanceFilter(false);
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault() in BlockOfBusinessAction.");
        }
        
		request.getSession().setAttribute("topTabSelected","blockOfBusiness");
		return forward; 
    }
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=default"}, method ={RequestMethod.POST,RequestMethod.GET}) 
    public String doDefaultNew (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response, request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault() in BlockOfBusinessPendingAction.");
        }
         forward = doDefault( actionForm, request, response);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
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
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=reset"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doReset (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault() in BlockOfBusinessAction.");
        }
         forward = doDefault( actionForm, request, response);

        // Show the Advanced Filter section..
        ((BlockOfBusinessForm) actionForm).setShowAdvanceFilter(true);

        return forward;
    }  
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doFilter");
        }

         forward = super.doFilter(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doFilter");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    @RequestMapping(value ="/blockOfBusiness/Discontinued/", params={"task=sort"}, method ={RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSort");
        }

         forward = super.doSort(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doSort");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=downloadAll"}, method ={RequestMethod.GET}) 
    public String doDownloadAll (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownloadAll");
        }

         forward = super.doDownloadAll(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownloadAll");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=download"}, method ={RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownload");
        }

         forward = super.doDownload(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownload");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) form, request, response, request.getServletContext() );
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
    	String forward=super.doPage( form, request, response);
    	 return StringUtils.contains(forward, "/")?forward:forwards.get(forward); 
    }
    
    
    
    @RequestMapping(value ="/blockOfBusiness/Discontinued/",params={"task=printPDF"}, method ={RequestMethod.GET}) 
    public String doPrintPDF (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm,	 BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> PrintPDF");
        }

         forward = super.doPrintPDF(actionForm, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- PrintPDF");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    /**
     * This method will get the BOB information.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    public String doCommon( BaseReportForm form,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon() in BlockOfBusinessAction.");
        }
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;

        if (reportForm.getIsPageAccessed() == null || !reportForm.getIsPageAccessed()) {
            // Log Page Access into MRL.
            logPageAccess( request, BDSessionHelper.getUserProfile(request));
            // Indicator that the page has already been accessed.
            reportForm.setIsPageAccessed(Boolean.TRUE);
        }
        
        // Retrieving filters used by user, 
        //		to check whether they are valid for navigating to tab.
        ArrayList<FilterInfoBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        
        // This method will carry out all the common operations we need to do before invoking the
        // super.doCommon() method.
        String forwardName = beforeDoCommon( form, request, response,request.getServletContext());

        
        // Check for Error Messages.
        boolean isErrorMsgPresent = checkForErrorConditions(userProfile, reportForm, request, filtersUsed);
        if (isErrorMsgPresent) {
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doCommon() in BlockOfBusinessAction. Error Messages Found.");
            }
            addBrokerReportingInfoMessage(reportForm, request);
            return forwards.get(forwardName);
        }

        // Check for Info Messages.
        boolean isInfoMsgPresent = checkForInfoMessageConditions(userProfile, reportForm, request);
        if (isInfoMsgPresent) {
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> doCommon() in BlockOfBusinessAction. Info Messages Found.");
            }
            addBrokerReportingInfoMessage(reportForm, request);
            return forwards.get(forwardName);
        }

        super.doCommon( form, request, response);

        // Get DBSessionID and place it into Map.
        setStoredProcSessionIDIntoMap(reportForm, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doCommon() in BlockOfBusinessAction.");
        }
        
        addBrokerReportingInfoMessage(reportForm, request);
        
        DynamicColumnModification.INSTANCE.override(
                new ColumnValueOverrideEnabler(
                        (BOBColumnsApplicableToTab) request.getAttribute(BDConstants.APPLICABLE_COLUMNS)),
                        ArrayUtility.toUnsortedSet(
                                (BlockOfBusinessReportData) request.getAttribute(BDConstants.REPORT_BEAN)));
        
        setLegends(request, reportForm);
        
        return forwards.get(forwardName);
    }

    /**
     * This method is used to log the Page Access into MRL.
     * 
     * @param userProfile - BDUserProfile object.
     */
    private void logPageAccess( HttpServletRequest request,
            BDUserProfile userProfile) {
        StringBuffer logData = new StringBuffer();
        Long profileID = null;
        // If a Internal user is mimicking an external user, log the profileID of Internal user.
        // If a Internal user is not in mimic mode, then log the profileID of the Internal user.
        if (userProfile.isInMimic()) {
            BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
                    .getMimckingUserProfile(request);
            profileID = mimickingUserProfile.getBDPrincipal().getProfileId();
        } else {
            profileID = userProfile.getBDPrincipal().getProfileId();
        }

        String pageAccessed = BDConstants.PAGE_ACCESSED_DISCONTINUED_TAB;
        /* if (BDConstants.DISCONTINUED_TAB.equals(discontinuedTab)) {
            pageAccessed = BDConstants.PAGE_ACCESSED_DISCONTINUED_TAB;
        } else if (BDConstants.PENDING_TAB.equals(mapping.getParameter())) {
            pageAccessed = BDConstants.PAGE_ACCESSED_PENDING_TAB;
        } else if (BDConstants.DISCONTINUED_TAB.equals(mapping.getParameter())) {
            pageAccessed = BDConstants.PAGE_ACCESSED_DISCONTINUED_TAB;
        }*/

        logData.append(BDConstants.BOB_LOG_USER_PROFILE_ID).append(profileID).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BOB_LOG_PAGE_ACCESSED)
                .append(BDConstants.BOB_LOG_BLOCK_OF_BUSINESS)
                .append(BDConstants.COMMA_SYMBOL)
                .append(BDConstants.SINGLE_SPACE_SYMBOL)
                .append(pageAccessed)
                .append(BDConstants.SINGLE_SPACE_SYMBOL)
                .append(BDConstants.TAB)
                .append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BOB_LOG_MIMIC_MODE).append(
                userProfile.isInMimic() ? BDConstants.YES_VALUE : BDConstants.NO_VALUE).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        if (userProfile.isInMimic()) {
            logData.append(BDConstants.BOB_LOG_MIMICKED_USER_PROFILE_ID).append(
                userProfile.getBDPrincipal().getProfileId()).append(BDConstants.SEMICOLON_SYMBOL);
        }
        
        logData.append(BDConstants.BOB_LOG_DATE_OF_ACTION).append(new Date()).append(
                BDConstants.SEMICOLON_SYMBOL);
        
        logData.append(BDConstants.BOB_LOG_ACTION_TAKEN).append(
                BDConstants.BOB_LOG_BD_BLOCK_OF_BUSINESS).append(pageAccessed).append(
                BDConstants.BOB_LOG_PAGE_ACCESS).append(BDConstants.SEMICOLON_SYMBOL);
        
        BlockOfBusinessUtility.logWebActivity("doDefault", logData.toString(), userProfile, logger,
                interactionLog, logRecord);
    }
    
    /**
     * This method will carry out all the common operations that needs to be done for every request.
     * This method will: 
     *      - Build BOB Tabs. 
     *      - Populate User Name and other details into Form. 
     *      - Get the applicable Column/Filter applicable for the currently selected tab 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    private String beforeDoCommon( BaseReportForm form,
            HttpServletRequest request, HttpServletResponse response,ServletContext servlet) throws SystemException {
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        
        // Build the Tabs.
        buildBOBTabs(reportForm, request);
        // Populate User Name and other summary section related information.
        populateSummarySectionInfoIntoForm(reportForm, request);
        
        // Find the forward
        String forwardName = BDConstants.DISCONTINUED_TAB_FORWARD;
        String currentTab = BDConstants.DISCONTINUED_TAB;
        /* if (BDConstants.DISCONTINUED_TAB.equals(discontinuedTab)) {
        	 forwardName = BDConstants.DISCONTINUED_TAB_FORWARD;
             currentTab = BDConstants.DISCONTINUED_TAB;
        } else if (BDConstants.PENDING_TAB.equals(mapping.getParameter())) {
            forwardName = BDConstants.PENDING_TAB_FORWARD;
            currentTab = BDConstants.PENDING_TAB;
        } else if (BDConstants.DISCONTINUED_TAB.equals(mapping.getParameter())) {
            forwardName = BDConstants.DISCONTINUED_TAB_FORWARD;
            currentTab = BDConstants.DISCONTINUED_TAB;
        }*/
        reportForm.setCurrentTab(currentTab);

        // Populate the other details into Action Form.
        populateReportForm( form, request);
        
        // Put the applicable Filters into the Form, based on the current Tab.
        BOBFilterMap bobFilterMap = BOBFilterCriteria.getApplicableFilters(currentTab, userRole,
                userProfile.isInMimic());
        Map<String, LabelInfoBean> quickFilters = bobFilterMap.getApplicableQuickFilters();
        Map<String, LabelInfoBean> advanceFilters = bobFilterMap.getApplicableAdvFilters();
        
        // If the user is a Firm Rep and has only one Firm associated to him, then, we don't need to
        // show the Firm Name Filter.
        if (userRole instanceof BDFirmRep) {
            List<BrokerDealerFirm> bdFirm = BlockOfBusinessUtility
                    .getAssociatedFirmsForBDFirmRep(userProfile);
            
            if (bdFirm == null || bdFirm.isEmpty() || bdFirm.size() == 1) {
                LabelInfoBean filterFirm = quickFilters
                        .get(BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
                filterFirm.setEnabled(false);
                quickFilters.put(BlockOfBusinessReportData.FILTER_BDFIRM_NAME, filterFirm);

                filterFirm = advanceFilters.get(BlockOfBusinessReportData.FILTER_BDFIRM_NAME);
                filterFirm.setEnabled(false);
                advanceFilters.put(BlockOfBusinessReportData.FILTER_BDFIRM_NAME, filterFirm);
            }
        }
        reportForm.setBobQuickFiltersMap(quickFilters);
        reportForm.setBobAdvancedFiltersMap(advanceFilters);

        // Get the applicable Columns and put into request as a attribute.
        BOBColumnsApplicableToTab bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab,
                userRole, userProfile.isInMimic()).createCopy();
        request.setAttribute(BDConstants.APPLICABLE_COLUMNS, bobColumns);
        
        // Show the Footnotes describing the
        // "Historical Information pertains only to certain contract info" when the Report as of
        // Date is not equal to the default date:
        Boolean isDefaultDateSelected = BlockOfBusinessUtility.isDefaultDateSelected(reportForm
                .getAsOfDateSelected());
        reportForm.setShowHistoricalContractInfoFootnote(!isDefaultDateSelected);

        // Show the Footnote describing about the Pending, Outstanding proposal 
        // contract counts are as of Latest as of date. Display the footnote only when the 
        // latest as of date is not selected.
        reportForm.setShowPNAndPPContractCountAsOfLatestDateFootnote(!isDefaultDateSelected);
        
        // Show the Footnotes describing the Assets column in DI tab.
            reportForm.setShowDIFootnote(Boolean.TRUE);
        //CL 131060 change
        String version = BOBColumnCriteria.getVersionInfoForUserType(userRole, userProfile.isInMimic());
		if (BDConstants.COLUMNS_VERSION1.equals(version)
				|| BDConstants.COLUMNS_VERSION2.equals(version)
				|| BDConstants.COLUMNS_VERSION3.equals(version)) {
			reportForm.setLevel1User(true);
		}
		
		request.setAttribute("showPlanReviewReportsLink",
				Boolean.valueOf(showPlanReviewReportsLink(userProfile, request)));
        
        
        return forwardName;
    }
    
   

	/**
     * This method populates the Indicator which tells whether the Advanced Filter should be
     * displayed or not. Also, this method resets the filter criteria belonging to quick filter
     * section, if the advanced filter was submitted.
     * 
     * @param form - BlockOfBusinessForm
     * @param request - HttpServletRequest.
     */
    private void populateShowAdvancedFilterInd(BlockOfBusinessForm form, HttpServletRequest request) {

        String task = getTask(request);

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
    }
    
    /**
     * This method populates the summary section related information into the Form.
     * 
     * @param reportForm
     * @param request
     * @throws SystemException
     */
    private void populateSummarySectionInfoIntoForm(BlockOfBusinessForm reportForm,
            HttpServletRequest request) throws SystemException {
        reportForm.setInternalUserAndNotInMimickModeInd(false);
        reportForm.setFirmRepUserInd(false);

        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        if (userProfile.getRole() instanceof BDInternalUser && !userProfile.isInMimic()) {
            BDPrincipal principal = userProfile.getBDPrincipal();
            reportForm.setInternalUserAndNotInMimickModeInd(true);
            reportForm.setInternalUserName(principal.getFirstName()
                    + BDConstants.SINGLE_SPACE_SYMBOL + principal.getLastName());
        } else if (userProfile.getRole() instanceof BDFirmRep) {
            reportForm.setFirmRepUserInd(true);
            ArrayList<String> firmNames = BlockOfBusinessUtility
                    .getAssociatedFirmNamesForFirmRep(userProfile);
            reportForm.setAssociatedFirmNames(firmNames);
        } else {
            BDPrincipal principal;
            try {
                principal = BlockOfBusinessUtility.getPrincipalForFinancialRepOrAsst(userProfile);
            } catch (SecurityServiceException e) {
                throw new SystemException(e,
                        "SecurityServiceException occurred in populateSummarySectionInfoIntoForm() method: "
                                + e.getMessage());
            }
            reportForm.setFinancialRepUserName(principal.getFirstName()
                    + BDConstants.SINGLE_SPACE_SYMBOL + principal.getLastName());
        }
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
            BlockOfBusinessForm reportForm,
            HttpServletRequest request,
            ArrayList<FilterInfoBean> filtersUsed) throws SystemException{
        boolean isErrorMsgPresent = false;

        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

        if (!BDConstants.NO_QUICK_OR_ADV_FILTER_SUBMITTED
                .equals(isQuickFilterSubmitted(reportForm))) {

            /**
             * Display Error Message if Contract Name < 3 characters.
             */
            String contractName = null;
            if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                String quickFilterSelected = reportForm.getQuickFilterSelected();
                if (BlockOfBusinessReportData.FILTER_CONTRACT_NAME.equals(quickFilterSelected)) {
                    contractName = reportForm.getQuickFilterContractName();
                }
            } else {
                contractName = reportForm.getContractName();
            }
            
            String contractNumber = null;
            if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                String quickFilterSelected = reportForm.getQuickFilterSelected();
                if (BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER.equals(quickFilterSelected)) {
                	contractNumber = reportForm.getQuickFilterContractNumber();
                }
            } else {
            	contractNumber = reportForm.getContractNumber();
            }
            
            if (StringUtils.trimToNull(contractName) != null
                    && StringUtils.trimToNull(contractName).length() < 3) {
                GenericException exception = new GenericException(
                        BDErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
                errorMessages.add(exception);
            }
            
            if (StringUtils.trimToNull(contractNumber) != null
                    && StringUtils.trimToNull(contractNumber).length() < 3) {
                GenericException exception = new GenericException(
                        BDErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
                errorMessages.add(exception);
            }
            
            /**
             * Display Error Message if Financial Rep Name / Org Name < 3 characters.
             */
            String FinancialRepOrOrgName = null;
            if (BDConstants.ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
            	FinancialRepOrOrgName = reportForm.getFinancialRepName();
            }

            if (StringUtils.trimToNull(FinancialRepOrOrgName) != null
                    && StringUtils.trimToNull(FinancialRepOrOrgName).length() < 3) {
                GenericException exception = new GenericException(
                        BDErrorCodes.FINANCIALREP_NAME_LESS_THAN_THREE_DIGITS);
                errorMessages.add(exception);
            }
            
            /**
             * 2. Validate the Asset Range From, Asset Range To filter values.
             */
            String assetRangeFrom = reportForm.getAssetRangeFrom();
            String assetRangeTo = reportForm.getAssetRangeTo();
            boolean skipAssetRangeValidate = false;
            if (StringUtils.isBlank(assetRangeFrom) && StringUtils.isBlank(assetRangeTo)) {
                skipAssetRangeValidate = true;
            }

            if (!skipAssetRangeValidate) {
                if (StringUtils.isBlank(assetRangeFrom) && !StringUtils.isBlank(assetRangeTo)) {
                    assetRangeFrom = BDConstants.ZERO_STRING;
                }
                if (!StringUtils.isBlank(assetRangeFrom) && StringUtils.isBlank(assetRangeTo)) {
                    assetRangeTo = BDConstants.ZERO_STRING;
                }

                // Strip off all the occurrences of Comma.
                assetRangeFrom = StringUtils.remove(assetRangeFrom.trim(), BDConstants.COMMA_SYMBOL);
                assetRangeTo = StringUtils.remove(assetRangeTo.trim(), BDConstants.COMMA_SYMBOL);

                BigDecimal assetRangeFromValue = null;
                BigDecimal assetRangeToValue = null;
                try {
                    assetRangeFromValue = new BigDecimal(assetRangeFrom);
                    assetRangeToValue = new BigDecimal(assetRangeTo);

                    if (assetRangeFromValue == null || assetRangeToValue == null
                            || assetRangeToValue.compareTo(BDConstants.MIN_ASSET_VALUE) < 0
                            || assetRangeFromValue.compareTo(BDConstants.MIN_ASSET_VALUE) < 0
                            || assetRangeToValue.compareTo(BDConstants.MAX_ASSET_VALUE) > 0
                            || assetRangeFromValue.compareTo(BDConstants.MAX_ASSET_VALUE) > 0) {
                        GenericException exception = new GenericException(
                                BDErrorCodes.BOB_INVALID_RANGE_FOR_ASSET_RANGE_FROM_TO);
                        errorMessages.add(exception);
                    } else if (assetRangeFromValue.compareTo(assetRangeToValue) > 0) {
                        GenericException exception = new GenericException(
                                BDErrorCodes.BOB_MIN_ASSET_RANGE_GT_THAN_MAX_ASSET_RANGE);
                        errorMessages.add(exception);
                    }
                } catch (NumberFormatException nfe) {
                    GenericException exception = new GenericException(
                            BDErrorCodes.BOB_INVALID_FORMAT_ASSET_RANGE_FROM_TO);
                    errorMessages.add(exception);
                }

            }
            
            // While navigating between tabs, 
            //		if search criteria entered in the report tab from which the user navigates 
            //		is not applicable for the report tab navigating to, then display an error message and reset filters
			boolean isValidFiltersUsed = isFiltersUsedValidForCurrentTab(
							reportForm.getCurrentTab(), filtersUsed, reportForm,
							userProfile, userProfile.getRole());
            if(!isValidFiltersUsed){
            	GenericException exception = new GenericException(
                        BDErrorCodes.BOB_SEARCH_CRITERIA_NOT_APPLICABLE);
                errorMessages.add(exception);
                // Reset filters
                reportForm.resetQuickFilter();
                reportForm.resetAdvancedFilter();
            }
            
        }
        
        if (!errorMessages.isEmpty()) {
            isErrorMsgPresent = true;
            setErrorsInRequest(request, errorMessages);
        }

        return isErrorMsgPresent;
    }
    /**
     * Adds broker reporting info message
     * 
     * @param reportForm
     * @param request
     */
    private void addBrokerReportingInfoMessage(BlockOfBusinessForm reportForm, HttpServletRequest request) {
    	
    	if (reportForm.hasNoContractsForDetailedBrokerReport()) {
			ArrayList<GenericException> infoMessagesAboveColHeader = new ArrayList<GenericException>();
			GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
					BDContentConstants.NO_ACTIVE_CONTRACTS_TO_DISPLAY,
                    ContentTypeManager.instance().MISCELLANEOUS,false);
			infoMessagesAboveColHeader.add(exception);
			setMessagesInRequest(request, infoMessagesAboveColHeader,
	                    BDConstants.INFO_MSG_DISPLAY_ABOVE_COLUMN_HEADER);
		}
    	
    }

    /**
     * This method checks for those conditions where we need to display a Informational message to
     * the user.
     * 
     * @param userRole - the BDUserRole object
     * @param reportForm - The BlockOfBusinessForm object
     * @param request - the HttpServletRequest object.
     * @return - a arrayList of informational message exceptions to be shown to the user.
     * @throws SystemException
     */
    private boolean checkForInfoMessageConditions(
            BDUserProfile userProfile,
            BlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> checkForInfoMessageConditions().");
        }
        
        boolean isInfoMsgPresent = false;
        boolean noContractsForFilterEnteredMessageAdded = false;
        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
        ArrayList<GenericException> infoMessagesAboveColHeader = new ArrayList<GenericException>();
        
        /**
         * 1. Show a message to the Internal User (not RVP) to use Filter criteria to display the
         * BOB report. This is done only for the first time the user accesses the page.
         */
        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        if (quickFilterSubmitted == null) { // quickFilterSubmitted is null for first time access.
            if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userProfile.getRole())) {
                GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                        BDContentConstants.BOB_ENTER_SEARCH_CRITERIA_TO_DISPLAY_REPORT,
                        ContentTypeManager.instance().MISCELLANEOUS,false);
                infoMessagesAboveColHeader.add(exception);
            }
        }

        /**
         * 2. Validate the Contract Number filter value.
         */
        if (!BDConstants.NO_QUICK_OR_ADV_FILTER_SUBMITTED
                .equals(isQuickFilterSubmitted(reportForm))) {

            if (BDConstants.ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                String contractNumber = getFilterValue(
                        BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER, reportForm, userProfile,
                        request, Boolean.FALSE);
                
                if (!StringUtils.isEmpty(contractNumber)) {
                    contractNumber = StringUtils.trim(contractNumber);
                    if (!StringUtils.isNumeric(contractNumber)) {
                        GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                                BDContentConstants.BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
                                ContentTypeManager.instance().MISCELLANEOUS,false);
                        infoMessages.add(exception);
                        noContractsForFilterEnteredMessageAdded = true;
                    }
                }
            }            
            if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                String contractNumber = getFilterValue(
                        BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER, reportForm, userProfile,
                        request, Boolean.FALSE);
                
                if (!StringUtils.isEmpty(contractNumber)) {
                    contractNumber = StringUtils.trim(contractNumber);
                    if (!StringUtils.isNumeric(contractNumber)) {
                        GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                                BDContentConstants.BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
                                ContentTypeManager.instance().MISCELLANEOUS,false);
                        infoMessages.add(exception);
                        noContractsForFilterEnteredMessageAdded = true;
                    }
                }
            }
        }
        
        /**
         * 3. Show a info message when the firm name entered by the user is invalid. When this happens,
         * the firm-name may have a value, but the firmID will be empty/null.
         */
    	String firmIDSelected = ""; 
        String firmNameSelected = "";
        if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
        	firmIDSelected = reportForm.getQuickFilterFirmIDSelected();
        	firmNameSelected = reportForm.getQuickFilterFirmNameSelected();
        } else if (BDConstants.ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
        	firmIDSelected = reportForm.getFirmIDSelected();
        	firmNameSelected = reportForm.getFirmNameSelected();
        }
        
        if (!StringUtils.isBlank(firmNameSelected) && StringUtils.isBlank(firmIDSelected)) {
        	if (!noContractsForFilterEnteredMessageAdded) {
            	GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                        BDContentConstants.BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
                        ContentTypeManager.instance().MISCELLANEOUS,false);
            	infoMessages.add(exception);
                noContractsForFilterEnteredMessageAdded = true;
        	}
        }
        
        if (!infoMessages.isEmpty()) {
            isInfoMsgPresent = true;
            setMessagesInRequest(request, infoMessages,
                    BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        }
        
        if (!infoMessagesAboveColHeader.isEmpty()) {
            isInfoMsgPresent = true;
            setMessagesInRequest(request, infoMessagesAboveColHeader,
                    BDConstants.INFO_MSG_DISPLAY_ABOVE_COLUMN_HEADER);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> checkForInfoMessageConditions().");
        }
        
        return isInfoMsgPresent;
    }
    
    /**
     * This method is used to set the session ID obtained from stored proc, into a Map.
     * 
     * @param reportForm - The Form.
     * @param request - The HttpServlet Request.
     */
    @SuppressWarnings("unchecked")
    private void setStoredProcSessionIDIntoMap(BlockOfBusinessForm reportForm,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> setStoredProcSessionIDIntoSession().");
        }
        
        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
        if (reportData == null) {
            return;
        }

        Date asOfDate = getAsOfDate(reportForm);
        
        // This variable will be used to store the session ID that is sent to Database.
        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
                .getAttribute(BDConstants.DB_SESSION_ID);
        if (storedProcSessionIDMap == null) {
            storedProcSessionIDMap = new HashMap<Date, Integer>();
        }
        storedProcSessionIDMap.put(asOfDate, reportData.getDbSessionID());
        
        request.getSession(false).setAttribute(BDConstants.DB_SESSION_ID, storedProcSessionIDMap);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> setStoredProcSessionIDIntoSession().");
        }
    }

    /**
     * This method is used to obtain the session ID from the storedProcSessionIDMap in the Form.
     * 
     * @param reportForm - The BlockOfBusinessForm.
     * @return - String representing the session ID. null, if the session ID is not present.
     */
    @SuppressWarnings("unchecked")
    private Integer getStoredProcSessionIDForAsOfDate(HttpServletRequest request,
            BlockOfBusinessForm reportForm) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getStoredProcSessionIDForAsOfDate().");
        }

        Date asOfDate = getAsOfDate(reportForm);
        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
                .getAttribute(BDConstants.DB_SESSION_ID);
        
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
     * The Default sort field.
     */
    @Override
    protected String getDefaultSort() {
        return BlockOfBusinessReportData.DEFAULT_SORT_COLUMN_NAME;
        
    }

    /**
     * The Default sort direction.
     */
    @Override
    protected String getDefaultSortDirection() {
        return ReportSort.ASC_DIRECTION;
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
        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) report;
        if (reportData.getResultTooBigInd()) {
            noOfRows = 0;
        }
        
        return noOfRows;
    }

    /**
     * This is the main method where the XML is generated. The XML will be used for PDF generation.
     */
    public Document prepareXMLFromReport(BaseReportForm form, ReportData report,
            HttpServletRequest request) throws SystemException, ParserConfigurationException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> prepareXMLFromReport().");
        }
        
        PDFDocument doc = new PDFDocument();

        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();
        
        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);
        
        if (reportData == null) {
            return null;
        }
        
        BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext())
                .getLayoutBean(BDPdfConstants.BLOCK_OF_BUSINESS_ACTIVE_TAB_PATH, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();

        Element rootElement = doc.createRootElement(BDPdfConstants.BLOCK_OF_BUSINESS);

        // Logo, Report Name, As Of Date, Intro-1, Intro-2 elements.
        setLogoAndPageName(layoutPageBean, doc, rootElement);
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByStyle(
                getAsOfDate(reportForm), null, RenderConstants.MEDIUM_STYLE));
        setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);

        // Show Filters Used
        ArrayList<FilterInfoBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        if (filtersUsed != null && !filtersUsed.isEmpty()) {
            Element filtersUsedElement = doc.createElement(BDPdfConstants.FILTERS_USED);
            for (FilterInfoBean filterUsed : filtersUsed) {
                Element filterElement = doc.createElement(BDPdfConstants.FILTER);
                doc.appendTextNode(filterElement, BDPdfConstants.FILTER_TITLE, filterUsed
                        .getFilterInfo().getTitle());
                
                // For the Asset Range From, Asset Range To, show the values in proper formatting.
                if (BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM.equals(filterUsed
                        .getFilterInfo().getId())
                        || BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO.equals(filterUsed
                                .getFilterInfo().getId())) {
                    String assetRange = filterUsed.getFilterValue();
                    try {
                        if (!StringUtils.isEmpty(assetRange)) {
                            filterUsed.setFilterValue(NumberRender.formatByType(assetRange,
                                    null, RenderConstants.CURRENCY_TYPE, Boolean.FALSE));
                        }
                    } catch (NumberFormatException nfe) {
                        // do nothing.
                    }
                }
                
                doc.appendTextNode(filterElement, BDPdfConstants.FILTER_VALUE, filterUsed
                        .getFilterValue());
                doc.appendElement(filtersUsedElement, filterElement);
            }
            doc.appendElement(rootElement, filtersUsedElement);
        }
        
        // Add a element which tells whether the Latest as of date was selected or not.
		doc.appendTextNode(rootElement,
				BDPdfConstants.BOB_IS_LATEST_ASOF_DATE_SELECTED,
				BlockOfBusinessUtility.isDefaultDateSelected(
						reportForm.getAsOfDateSelected()).toString());

        // Summary Info.
        showSummaryInfoInPDF(rootElement, doc, layoutPageBean, userProfile, userRole, reportData,
                reportForm);

        // Report Details.
        showMainReportInPDF(rootElement, reportForm.getCurrentTab(), doc, layoutPageBean,
                userProfile, userRole, reportData);
        
        
        // Show Error Messages for result Too Big Ind.
        // setErrorMessagesXMLElementsForReqAttr(doc, rootElement, request,
        // BDConstants.ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        
        // Show Info Messages..
        setInfoMessagesXMLElementsForReqAttr(doc, rootElement, request,
                BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        
        // Show PDF Capped message.
        if (form.getPdfCapped()) {
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }

        setFooterXMLElements(reportForm, layoutPageBean, doc, rootElement, request, reportData);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> prepareXMLFromReport().");
        }
        
        return doc.getDocument();
    }

    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets footer, footnotes and disclaimer XML elements common for reports. There are 2
     * additional footers that are being appended to the PDF in this method.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param reportData 
     * @param params
     */
    protected void setFooterXMLElements(BlockOfBusinessForm reportForm, LayoutPage layoutPageBean,
            PDFDocument doc, Element rootElement, HttpServletRequest request, BlockOfBusinessReportData reportData) {
        
        super.setFooterXMLElements(layoutPageBean, doc, rootElement, request);

        BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
        
        Element legendRootElement = null;
        
        if (reportForm.isCompensationSectionDisplayed() || (reportForm.isRiaSectionDisplayed() && bobSummaryVO.getHasRiaFees())
        		||  (reportForm.isFiduciarySectionDisplayed() && bobSummaryVO.getHasContractsWithCofidSelected())) {
        	legendRootElement = doc.createElement(BDPdfConstants.LEGENDS);
			doc.appendElement(rootElement, legendRootElement);
			PdfHelper.convertIntoDOM(BDPdfConstants.LEGEND, legendRootElement, doc, BDConstants.LEGEND);
        }
        
		if (reportForm.isCompensationSectionDisplayed()) {
			for(String legend : reportForm.getLegends()) {
				PdfHelper.convertIntoDOM(BDPdfConstants.LEGEND, legendRootElement, doc, legend);
			}
			
			String footnote = ContentHelper.getContentText(
                    BDContentConstants.AB_COLUMN_FOOTNOTE,
                    ContentTypeManager.instance().FOOTNOTE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.AB_FOOTNOTE, rootElement, doc, footnote);
            
            footnote = ContentHelper.getContentText(
                    BDContentConstants.DAILY_UPDATE_FOOTNOTE,
                    ContentTypeManager.instance().FOOTNOTE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.DAILY_UPDATE_FOOTNOTE, rootElement, doc, footnote);
		}
		
		if (reportForm.isRiaSectionDisplayed() && bobSummaryVO.getHasRiaFees()){
			for(String legend : reportForm.getRiaLegends()) {
				PdfHelper.convertIntoDOM(BDPdfConstants.LEGEND, legendRootElement, doc, legend);
			}
		}
		
		if (reportForm.isFiduciarySectionDisplayed() && bobSummaryVO.getHasContractsWithCofidSelected()){
			for(String legend : reportForm.getFiduciaryServicesTabLegends()) {
				PdfHelper.convertIntoDOM(BDPdfConstants.LEGEND, legendRootElement, doc, legend);
			}
		}
        
        // Append the Footnotes for Historical Footnotes
        if (reportForm.getShowHistoricalContractInfoFootnote()) {
            String additionalFootNoteText = ContentHelper.getContentText(
                    BDContentConstants.BOB_HISTORICAL_CONTRACT_INFO_FOOTNOTE,
                    ContentTypeManager
                            .instance().FOOTNOTE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.BOB_HISTORICAL_FOOTNOTE, rootElement, doc, additionalFootNoteText);
        }
        // Append the Footnotes for DI tab.
        if (reportForm.getShowDIFootnote()) {
            String additionalFootNoteText = ContentHelper.getContentText(
                    BDContentConstants.BOB_ASSETS_COLUMN_IN_DI_TAB_FOOTNOTE,
                    ContentTypeManager
                            .instance().FOOTNOTE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.BOB_DISCONTINUED_TAB_FOOTNOTE, rootElement, doc, additionalFootNoteText);
        }
        // Append the Footnotes for Historical Footnotes
        if (reportForm.getShowPNAndPPContractCountAsOfLatestDateFootnote()) {
            String additionalFootNoteText = ContentHelper.getContentText(
                    BDContentConstants.BOB_PN_PP_CONTRACT_CNT_ASOFLATESTDATE_FOOTNOTE,
                    ContentTypeManager
                            .instance().FOOTNOTE, null);
			PdfHelper
					.convertIntoDOM(
							BDPdfConstants.BOB_PN_PP_CONTRACT_CNT_ASOFLATESTDATE_FOOTNOTE,
							rootElement, doc, additionalFootNoteText);
        }
    }
    
    /**
     * This method will generate a part of XML file that has the BOB Report Summary Info.
     * 
     * @param parentElement - DOM parent Element to which the summary info will be appended.
     * @param doc - DOM document.
     * @param layoutPageBean - BDLayoutBean having information specific to the page.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportData - contains report information to be displayed as summary.
     */
    private void showSummaryInfoInPDF(Element parentElement, PDFDocument doc,
            LayoutPage layoutPageBean, BDUserProfile userProfile, BDUserRole userRole,
            BlockOfBusinessReportData reportData, BlockOfBusinessForm form) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showSummaryInfoInPDF().");
        }
        
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);
        
        BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
        
        if (bobSummaryVO == null) {
            return;
        }

        // If the user is a Internal User, then show his Name.
        if (userRole instanceof BDInternalUser && !userProfile.isInMimic()) {
            Element internalUserInfoElement = doc.createElement(BDPdfConstants.INTERNAL_USER_INFO);
            doc.appendTextNode(internalUserInfoElement, BDPdfConstants.USER_NAME, form
                    .getInternalUserName());
            doc.appendElement(summaryInfoElement, internalUserInfoElement);
            // If the user is a BDFirmRep, show his Name and BDFirm's associated to his profile.
        } else if (userRole instanceof BDFirmRep) {
            ArrayList<String> firmNamesList = form.getAssociatedFirmNames();
            if (firmNamesList != null && !firmNamesList.isEmpty()) {
                Element bdFirmRepInfo = doc.createElement(BDPdfConstants.BDFIRM_REP_INFO);
                Element associatedFirmName = doc.createElement(BDPdfConstants.ASSOCIATED_FIRM_NAMES);
                for (String firmName : firmNamesList) {
                    doc.appendTextNode(associatedFirmName, BDPdfConstants.FIRM_NAME, firmName);
                }
                doc.appendElement(bdFirmRepInfo, associatedFirmName);
                doc.appendElement(summaryInfoElement, bdFirmRepInfo);
            }
        } else {
            Element brokerInfoElement = doc.createElement(BDPdfConstants.FINANCIAL_REP_INFO);
            
            ArrayList<BrokerInfoVO> brokerInfoList = bobSummaryVO.getBrokerInfoVO();
            if (brokerInfoList != null && !brokerInfoList.isEmpty()) {
                doc.appendTextNode(brokerInfoElement, BDPdfConstants.USER_NAME, form
                        .getFinancialRepUserName());

                Element producerCodeDetailsElement = doc
                        .createElement(BDPdfConstants.PRODUCER_CODE_AND_FIRMNAME_LIST);
                
                for (BrokerInfoVO brokerInfo : brokerInfoList) {
                    Element prodCodeAndFirmNameElement = doc
                            .createElement(BDPdfConstants.PRODUCER_CODE_AND_FIRMNAME);
                    doc.appendTextNode(prodCodeAndFirmNameElement, BDPdfConstants.PRODUCER_CODE,
                            brokerInfo.getProducerCode());
                    doc.appendTextNode(prodCodeAndFirmNameElement, BDPdfConstants.FIRM_NAME,
                            brokerInfo.getBdFirmName());
                    doc.appendElement(producerCodeDetailsElement, prodCodeAndFirmNameElement);
                }
                doc.appendElement(brokerInfoElement, producerCodeDetailsElement);
                doc.appendElement(summaryInfoElement, brokerInfoElement);
            }
        }
        
        if (bobSummaryVO.getActiveContractAssets() != null) {
            String activeContractAssets = BDConstants.HYPHON_SYMBOL;
            if (!reportData.getResultTooBigInd()) {
                activeContractAssets = NumberRender.formatByType(bobSummaryVO
                        .getActiveContractAssets(), null, RenderConstants.CURRENCY_TYPE);
            }
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.ACTIVE_CONTRACT_ASSETS,
                    activeContractAssets);
        }

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_ACTIVE_CONTRACTS, String
                .valueOf(bobSummaryVO.getNumOfActiveContracts()));

        // Num Of Lives
        String numOfLives = BDConstants.HYPHON_SYMBOL;
        if (!reportData.getResultTooBigInd()) {
            numOfLives = String.valueOf(bobSummaryVO.getNumOfLives());
        }
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_LIVES, numOfLives);

        // Num of Oustanding Proposal
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_OUTSTANDING_PROPOSALS, String
                .valueOf(bobSummaryVO.getNumOfOutstandingProposals()));

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PENDING_CONTRACTS, String
                .valueOf(bobSummaryVO.getNumOfPendingContracts()));

        doc.appendElement(parentElement, summaryInfoElement);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showSummaryInfoInPDF().");
        }
    }

    /**
     * This method generates part of XML file, which is the Main report to be shown in PDF.
     * 
     * @param parentElement - DOM parent Element to which the main report info will be appended.
     * @param currentTab - String giving the current Tab.
     * @param doc - DOM document
     * @param layoutPageBean - BDLayoutBean having information specific to the page.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportData - contains report information to be displayed as Main report.
     */
    @SuppressWarnings("unchecked")
    private void showMainReportInPDF(Element parentElement, String currentTab, PDFDocument doc,
            LayoutPage layoutPageBean, BDUserProfile userProfile, BDUserRole userRole,
            BlockOfBusinessReportData reportData) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showMainReportInPDF().");
        }
        
        BOBColumnsApplicableToTab bobColumns;
        Map<String, Integer> columnWidth = new HashMap<String, Integer>();
        BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
        try {
            
            bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole, userProfile
                    .isInMimic()).createCopy();
            
            DynamicColumnModification.INSTANCE.override(
                    new ColumnValueOverrideEnabler(bobColumns),
                    ArrayUtility.toUnsortedSet(reportData));
            
        } catch (SystemException e) {
            // Returning back so that if any kind of exception happens, the PDF will not print the
            // main report.
            logger.error("Received a SystemException: ", e);
            return;
        }
        
        Element reportDtlsElement = doc.createElement(BDPdfConstants.BOB_REPORT_DETAILS);
        columnWidth.put(BDConstants.COL_CONTRACT_STATUS_ID, findWordWithMaxSize(columnWidth
                .get(BDConstants.COL_CONTRACT_STATUS_ID), BDConstants.COL_CONTRACT_STATUS));

        for (String columnName : BlockOfBusinessUtility.columnsList) {
            if (bobColumns.isColumnEnabledForPdfAndCsv(columnName)  && displayColumn(columnName, bobSummaryVO)) {
                // Contract Name is a No Wrap column. Hence, the size of its column is taken for its
                // full title without splitting it at Spaces.
                if (BlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(columnName) || 
                		BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID.equals(columnName)) {
                    String columnTitle = bobColumns.getTitle(columnName);
                    Integer existingSize = columnWidth.get(columnName) == null ? 0 : columnWidth.get(columnName);
                    Integer columnSize = existingSize > columnTitle.length() ? existingSize : columnTitle.length();
                    columnWidth.put(columnName, columnSize);
                }
                columnWidth.put(columnName, findWordWithMaxSize(columnWidth.get(columnName),
                        bobColumns.getTitle(columnName)));
            }
        }

        ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData
                .getDetails();
        
        if (bobReportVOList != null) {
            Element reportDtlElement = doc.createElement(BDPdfConstants.BOB_REPORT_DETAIL);
            Integer maxRowsToBeShown = getMaxCappedRowsInPDF();
            for (BlockOfBusinessReportVO bobReportVO : bobReportVOList) {
                if (maxRowsToBeShown-- <= 0) {
                    break;
                }
                
                Element reportRowElement = doc.createElement(BDPdfConstants.REPORT_ROW);

                for (String columnName : BlockOfBusinessUtility.columnsListPDF) {
                    if (bobColumns.isColumnEnabledForPdfAndCsv(columnName)  && displayColumn(columnName, bobSummaryVO)) {
                        String columnValue = getColumnValueForCSVPDF(columnName, bobReportVO, false);
                        // Add a attribute to the row cell if the value needs to be right aligned.
                        // This information is sent to the PDF.
                        if (BlockOfBusinessUtility.rightAlignedColumnsList.contains(columnName)) {
                            doc.appendTextNodeWithAttribute(reportRowElement,
                                    BDPdfConstants.ROW_CELL, columnValue,
                                    BDPdfConstants.COLUMN_RIGHT_ALIGNED, BDConstants.SPACE_SYMBOL);
                        } else if (BlockOfBusinessUtility.centerAlignedColumnsList.contains(columnName)) {
                            doc.appendTextNodeWithAttribute(reportRowElement,
                                    BDPdfConstants.ROW_CELL, columnValue,
                                    BDPdfConstants.COLUMN_CENTER_ALIGNED, BDConstants.SPACE_SYMBOL);
                        } else {
                            doc
                                    .appendTextNode(reportRowElement, BDPdfConstants.ROW_CELL,
                                            columnValue);
                        }
                        columnWidth.put(columnName, findWordWithMaxSize(
                                columnWidth.get(columnName), columnValue));
                    }
                }
                doc.appendElement(reportDtlElement, reportRowElement);
            }
            doc.appendElement(reportDtlsElement, reportDtlElement);
        }

        // Since the # of Columns are dynamic and we do not know what column widths should be
        // assigned for each column, we are getting the length of maximum word under each column and
        // using it to assign column widths.
        Element reportColHeaderElement = doc.createElement(BDPdfConstants.REPORT_COLUMN_HEADER);
        Element colHeaderInfoElement = doc.createElement(BDPdfConstants.COLUMN_HEADER_INFO);

        for (String columnName : BlockOfBusinessUtility.columnsListPDF) {
            if (bobColumns.isColumnEnabledForPdfAndCsv(columnName)  && displayColumn(columnName, bobSummaryVO)) {
                String title = bobColumns.getPDFTitle(columnName);

                colHeaderInfoElement = doc.createElement(BDPdfConstants.COLUMN_HEADER_INFO);
                // Add a attribute to the row cell if the value needs to be center aligned.
                // This information is sent to the PDF.
                if (BlockOfBusinessUtility.centerAlignedColumnHeaderList.contains(columnName)) {
                    doc.appendTextNodeWithAttribute(colHeaderInfoElement,
                    		BDPdfConstants.COLUMN_HEADER_NAME, title, 
                            BDPdfConstants.COLUMN_CENTER_ALIGNED, BDConstants.SPACE_SYMBOL);                
                } else {
                	doc.appendTextNode(colHeaderInfoElement, BDPdfConstants.COLUMN_HEADER_NAME, title);
                }
                doc.appendTextNode(colHeaderInfoElement, BDPdfConstants.COLUMN_HEADER_WIDTH,
                        columnWidth.get(columnName).toString());
                doc.appendElement(reportColHeaderElement, colHeaderInfoElement);
            }
        }
        doc.appendElement(reportDtlsElement, reportColHeaderElement);
        doc.appendElement(parentElement, reportDtlsElement);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showMainReportInPDF().");
        }
    }

    /**
     * This method will get the max word size from a given existingMaxWordSize and the maximum word
     * size found in the title.
     * 
     * @param existingMaxWordSize - max word size already found during previous calls to
     *            findWordWithMaxSize() method.
     * @param title - The String in which we are trying to find the maximum word size.
     * @return - the Max Integer of "existingMaxWordSize" and the max word size found in "title"
     */
    public Integer findWordWithMaxSize(Integer existingMaxWordSize, String title) {
        int maxSize = 0;
        if (existingMaxWordSize != null) {
            maxSize = existingMaxWordSize;
        }

        String[] individualWords = StringUtils.split(title, BDConstants.SINGLE_SPACE_SYMBOL);
        if (individualWords != null && individualWords.length > 0) {
            for (String word : individualWords) {
                if (word.length() > maxSize) {
                    maxSize = word.length();
                }
            }
        }
        return maxSize;
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
     * This method will be called when the user clicks on "CSV get ALL" button.
     * 
     * This method gets the report information from all the tabs and shows in it one CSV file.
     */
    protected byte[] getDownloadAllData(BaseReportForm form, ReportData report,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadAllData().");
        }
        
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();

        String[] currentTabs = new String[] { BDConstants.ACTIVE_TAB,
                BDConstants.OUTSTANDING_PROPOSALS_TAB, BDConstants.PENDING_TAB,
                BDConstants.DISCONTINUED_TAB };
        
        // Get all the filters that have been used by the user in JSP page. Check if the filters
        // used are valid for all the tabs.
        ArrayList<FilterInfoBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        // isFiltersUsedValidMap will hold the tab Name and a Boolean value indicating if the
        // filters used are applicable for that tab or not.
        Map<String, Boolean> isFiltersUsedValidMap = new HashMap<String, Boolean>();
        for (String currentTab : currentTabs) {
            // Get Applicable Filters..
            boolean isFiltersUsedValidForCurrentTab = isFiltersUsedValidForCurrentTab(currentTab,
                    filtersUsed, reportForm, userProfile, userRole);
            isFiltersUsedValidMap.put(currentTab, isFiltersUsedValidForCurrentTab);
        }
        
        String buff = new String();
        // This Map will hold the Report Data corresponding to each tab.
        Map<String, BlockOfBusinessReportData> reportDataList = new HashMap<String, BlockOfBusinessReportData>();
        
        try {
            for (String currentTab : currentTabs) {
                if (isFiltersUsedValidMap.get(currentTab)) {
                    reportDataList.put(currentTab, getReportDataForTab(currentTab, reportForm,
                            userProfile, userRole));
                }
            }
            buff = populateDownloadAllDataForTabs(currentTabs, isFiltersUsedValidMap, userProfile,
                    userRole, reportDataList, reportForm, request);
        } catch (ReportServiceException e) {
            logger.error("Received a Report service exception: ", e);
            throw new SystemException(e, "Received a Report service exception: " + e);
        } catch (ContentException e) {
            logger.error("Received a ContentException: ", e);
            throw new SystemException(e, "Received a ContentException: " + e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getDownloadAllData().");
        }
        
        return buff.getBytes();
    }
    
    /**
     * This method retrieves the report Data to be shown in a given Tab.
     * 
     * @param currentTab - The tab name
     * @param reportForm - The BlockOfBusinessForm.
     * @param userProfile - The BDUserProfile object.
     * @param userRole - The BDUserRole object.
     * @return - returns the Main Report Data to be shown on the given tab.
     * @throws ReportServiceException
     * @throws SystemException
     */
    private BlockOfBusinessReportData getReportDataForTab(String currentTab,
            BlockOfBusinessForm reportForm, BDUserProfile userProfile, BDUserRole userRole)
            throws ReportServiceException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportDataForTab().");
        }
        
        BlockOfBusinessReportData reportData = new BlockOfBusinessReportData();

        ReportCriteria filteringCriteriaSaved = reportForm.getFilteringCriteriaSaved();
        if (filteringCriteriaSaved == null) {
            return reportData;
        }
        
        ReportSortList reportSortList = filteringCriteriaSaved.getSorts();
        // Get the first report sort column. The first element in the ReportSortList will contain the 
        // Column based on which the report sorting was done in online report.
        ReportSort reportSort = reportSortList.get(0);
        String sortColumnName = reportSort.getSortField();
        String sortColumnDirection = reportSort.getSortDirection();
        if (!isSortColumnEnabledInAllTabs(sortColumnName, userProfile, userRole)) {
        	// Have the Default sort of Contract Name (/Proposal Name) column.
        	if (BDConstants.OUTSTANDING_PROPOSALS_TAB.equals(currentTab)) {
                sortColumnName = BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID;
        	} else {
                sortColumnName = BlockOfBusinessReportData.DEFAULT_SORT_COLUMN_NAME;
        	}
        	sortColumnDirection = ReportSort.ASC_DIRECTION;
        	
            reportSortList = new ReportSortList();
            reportSortList.add(new ReportSort(sortColumnName, sortColumnDirection));
            // setSorts is used instead of insertSort, so that we want to get rid off old sort
            // criteria.
            filteringCriteriaSaved.setSorts(reportSortList);
        } else {
        	ReportSortList reportSortListUpdated = updateSecondarySortOrders(reportSortList, currentTab);
        	filteringCriteriaSaved.setSorts(reportSortListUpdated);
        }
        
        addFilterCriteria(filteringCriteriaSaved,
                BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES, BlockOfBusinessUtility
                        .getContractStatus(currentTab));

        reportData = (BlockOfBusinessReportData) ReportServiceDelegate.getInstance().getReportData(
                filteringCriteriaSaved);
       
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getReportDataForTab().");
        }
        
        return reportData;
    }

    /**
     * This method updates the secondary sort orders based on the current tab we are in. If the
     * secondary sort order Contract Name / Contract Number are being used, it is changed to 
     * Client Short Name / Proposal Number for Outstanding Proposals tab. Similarly, the reverse is done
     * for other tabs.
     * 
     * @param reportSort
     * @param currentTab
     * @return - ReportSortList of all the sort orders.
     */
    @SuppressWarnings("unchecked")
	private ReportSortList updateSecondarySortOrders(ReportSortList reportSortList, String currentTab) {
    	ReportSortList reportSortListUpdated = new ReportSortList();
    	
    	if (reportSortList != null) {
    		Iterator sortListIterator = reportSortList.iterator();
    		while (sortListIterator.hasNext()) {
    			ReportSort reportSort = (ReportSort)sortListIterator.next();
    			String sortColumn = reportSort.getSortField();
    			String sortDirection = reportSort.getSortDirection();

    			if (BDConstants.OUTSTANDING_PROPOSALS_TAB.equals(currentTab)) {
    				if (BlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(sortColumn)) {
    					sortColumn = BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID;
    					sortDirection = ReportSort.ASC_DIRECTION;
    				}
    				if (BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID.equals(sortColumn)) {
    					sortColumn = BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID;
    					sortDirection = ReportSort.ASC_DIRECTION;
    				}
    			} else {
    				if (BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID.equals(sortColumn)) {
    					sortColumn = BlockOfBusinessReportData.COL_CONTRACT_NAME_ID;
    					sortDirection = ReportSort.ASC_DIRECTION;
    				}
    				if (BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID.equals(sortColumn)) {
    					sortColumn = BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID;
    					sortDirection = ReportSort.ASC_DIRECTION;
    				}
    			}
    			
    			reportSortListUpdated.add(new ReportSort(sortColumn, sortDirection));
    		}
    	}
    	
    	return reportSortListUpdated;
    }
    
    /**
     * For the current Tab, we fetch the applicable filters. We also fetch the filters that have
     * been used by the user in the JSP page. If there are any filters used in the JSP page which
     * are not applicable for the current tab, then, we return false. If all the filters used are
     * applicable filters for the current tab, then, return true.
     * 
     * @param currentTab
     * @param filtersUsed
     * @param reportForm
     * @param userProfile
     * @param userRole
     * @return
     * @throws SystemException
     */
    private boolean isFiltersUsedValidForCurrentTab(String currentTab,
            ArrayList<FilterInfoBean> filtersUsed, BlockOfBusinessForm reportForm,
            BDUserProfile userProfile, BDUserRole userRole) throws SystemException {

        boolean isFiltersUsedValidForCurrentTab = true;

        if (!BDConstants.NO_QUICK_OR_ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
            Map<String, LabelInfoBean> applicableFilters = null;

            // Get applicable Filters for the current tab.
            BOBFilterMap bobFilterMap = BOBFilterCriteria.getApplicableFilters(currentTab, userRole,
                    userProfile.isInMimic());

            if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                applicableFilters = bobFilterMap.getApplicableQuickFilters();
            } else {
                applicableFilters = bobFilterMap.getApplicableAdvFilters();
            }

            if (applicableFilters != null) {
                for (FilterInfoBean filterUsed : filtersUsed) {
                    if (filterUsed != null && filterUsed.getFilterInfo() != null) {
                    // If the user has used filters which are not part of applicableFilters, then
                        // return false.
                        if (applicableFilters.get(filterUsed.getFilterInfo().getId()) != null
                                && applicableFilters.get(filterUsed.getFilterInfo().getId())
                                        .getEnabled()) {
                            continue;
                        } else {
                            isFiltersUsedValidForCurrentTab = false;
                            break;
                        }
                    }
                }
            } else {
                return false;
            }
        }

        return isFiltersUsedValidForCurrentTab;
    }

    /**
     * This method checks if a given "sort Column Name" is enabled in all Tabs.
     * 
     * @param columnName - the given sort column field name.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @return - "true" if the the given column field name is applicable for all the tabs, else,
     *         "false" is returned.
     */
    private boolean isSortColumnEnabledInAllTabs(String columnName, BDUserProfile userProfile,
            BDUserRole userRole) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> isSortColumnEnabledInAllTabs().");
        }
        
        String[] currentTabs = new String[] { BDConstants.ACTIVE_TAB,
                BDConstants.OUTSTANDING_PROPOSALS_TAB, BDConstants.PENDING_TAB,
                BDConstants.DISCONTINUED_TAB };

        boolean sortColumnEnabled = true;

        for (String currentTab : currentTabs) {
            BOBColumnsApplicableToTab bobColumns;
            try {
                bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole,
                        userProfile.isInMimic());
            } catch (SystemException e) {
                return false;
            }
            if (!bobColumns.isColumnEnabled(columnName)) {
                sortColumnEnabled = false;
                break;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> isSortColumnEnabledInAllTabs().");
        }
        
        return sortColumnEnabled;
    }

    /**
     * This method creates the CSV report by taking the report data from all the tabs and creating
     * one single CSV file.
     * 
     * @param currentTabs - An array of all the tabs.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportDataList - A Map of <tabName, reportData corresponding to that tab>
     * @param reportForm - BlockOfBusinessForm object.
     * @param request - The HttpServletRequest object.
     * @return - returns the String in CSV format, that is to be sent back to user.
     * @throws SystemException
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    private String populateDownloadAllDataForTabs(String[] currentTabs,
            Map<String, Boolean> isFiltersUsedValidMap, BDUserProfile userProfile,
            BDUserRole userRole, Map<String, BlockOfBusinessReportData> reportDataList,
            BlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException,
            ContentException {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadAllDataForTabs().");
        }
        
        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);

        BlockOfBusinessReportData currentReportData = (BlockOfBusinessReportData) request
        .getAttribute(BDConstants.REPORT_BEAN);

        // Current Report Data will be null, if there were any info messages / error messages to be
        // shown in the page and we never went to DAO to get the report details. i.e., we caught
        // validation - Error/Info messages in the doCommon() method.
        if (currentReportData == null) {
            buff.append(BDConstants.SINGLE_SPACE_SYMBOL);
            return buff.toString();
        }
        
        buff.append(BDConstants.CSV_BLOCK_OF_BUSINESS).append(LINE_BREAK);
        buff.append(showSummaryInfoInCSV(userProfile, userRole, currentReportData, reportForm));
        buff.append(LINE_BREAK);
        
        buff.append(showAsOfDateAndFiltersSelectedInCSV(reportForm, userProfile, request, currentReportData));
        buff.append(LINE_BREAK);

        Map<String, Boolean> allApplicableColumnsMap = getAllApplicableColumnsForAllTabs(
                currentTabs, userProfile, userRole, reportDataList);
        
        BOBColumnsApplicableToTab bobColumns;
        try {
            bobColumns = BOBColumnCriteria.getApplicableColumns(currentTabs[0], userRole,
                    userProfile.isInMimic()).createCopy();
        } catch (SystemException e) {
            // Return the StringBuffer without any values in it.
            return buff.toString();
        }
        
        DynamicColumnModification.INSTANCE.override(
                new ColumnModifierConjunction(
                        ArrayUtility.toUnsortedSet(
                                new ColumnHeadingDisabler(allApplicableColumnsMap),
                                new ColumnValueOverrideEnabler(bobColumns))),
                        reportDataList.values());

        buff.append(BDConstants.COL_CONTRACT_STATUS);
        for (String columnName : BlockOfBusinessUtility.columnsList) {
            if (allApplicableColumnsMap.get(columnName)) {
                buff.append(COMMA).append(bobColumns.getTitle(columnName));
            }
        }
        buff.append(LINE_BREAK);

        for (String currentTab : currentTabs) {
            String currentTabTitle = BlockOfBusinessUtility.contractStatusTitleMap.get(currentTab);
            
            try {
                
                bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole,
                        userProfile.isInMimic()).createCopy();
                
                DynamicColumnModification.INSTANCE.override(
                        new ColumnValueOverrideEnabler(bobColumns),
                        reportDataList.values());
                
            } catch (SystemException e) {
                // Return the StringBuffer without any values in it.
                return buff.toString();
            }

            BlockOfBusinessReportData reportData = reportDataList.get(currentTab);
            if (reportData == null || reportData.getDetails() == null
                    || reportData.getDetails().isEmpty()) {
                if (!StringUtils.isEmpty(currentTabTitle)) {
                    buff.append(currentTabTitle);
                } else {
                    buff.append(BDConstants.HYPHON_SYMBOL);
                }
                buff.append(COMMA);

                if (!isFiltersUsedValidMap.get(currentTab)) {
                    // The chosen filters by the user are not valid for the current tab. Hence,
                    // display the message.
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BOB_FILTERS_USED_DO_NOT_APPLY, ContentTypeManager
                                    .instance().MISCELLANEOUS,false);
                    String exceptionText = ContentHelper.getMessage(exception);
                    if (!StringUtils.isEmpty(exceptionText)) {
                        buff.append(getCsvString(exceptionText)).append(LINE_BREAK);
                    }

                } else {
                    // There are no rows to be shown for the current tab. Hence, display the
                    // message.
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BOB_NO_RECORDS_MEET_CONDITIONS_ENTERED,
                            ContentTypeManager.instance().MISCELLANEOUS,false);
                    String exceptionText = ContentHelper.getMessage(exception);
                    if (!StringUtils.isEmpty(exceptionText)) {
                        buff.append(getCsvString(exceptionText)).append(LINE_BREAK);
                    }
                }
                continue;
            }
            // If resultTooBigInd is set to Yes, then show result too big message.
            if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userProfile.getRole())
                    && reportData.getResultTooBigInd()) {
                if (!StringUtils.isEmpty(currentTabTitle)) {
                    buff.append(currentTabTitle);
                } else {
                    buff.append(BDConstants.HYPHON_SYMBOL);
                }
                buff.append(COMMA);

                reportData.setDetails(null);
                GenericException exception = new GenericException(
                        BDErrorCodes.BOB_RESULT_TOO_BIG);
                List<GenericException> errorList = new ArrayList<GenericException>();
                errorList.add(exception);
                String[] exceptionTexts = ContentHelper.getMessagesUsingContentType(errorList);
                if(exceptionTexts!= null){
                	if (!StringUtils.isEmpty(exceptionTexts[0])) {
                		buff.append(getCsvString(exceptionTexts[0])).append(LINE_BREAK);
                	}
                }
            }
            
            BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
            
            ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData
                    .getDetails();
            
            if (bobReportVOList != null) {
                for (BlockOfBusinessReportVO bobReportVO : bobReportVOList) {
                    if (!StringUtils.isEmpty(currentTabTitle)) {
                        buff.append(currentTabTitle);
                    } else {
                        buff.append(BDConstants.HYPHON_SYMBOL);
                    }
                    buff.append(COMMA);

                    for (String columnName : BlockOfBusinessUtility.columnsList) {
                    	// In CSV-ALL report, for Outstanding Proposals contracts, we need to 
                    	// show the "Proposal Name" under the "Contract Name" header.
                    	// The following steps were taken to do this:
                    	// 1. In allApplicableColumnsMap, disabled the "Proposal Name" column so that
                    	//	  it will not get displayed in CSV-ALL report.
                    	// 2. For the Outstanding Proposals contracts, displaying the "Proposal Name" 
                    	//    value under "Contract Name" column.  
                    	if (BDConstants.OUTSTANDING_PROPOSALS_TAB.equals(currentTab)) {
                    		// Under Contract Name column header, display the Proposal Name value.
                    		if (BlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(columnName)) {
                            	String columnValue = getColumnValueForCSVPDF(
                            			BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID, bobReportVO, true);
                                if (StringUtils.isBlank(columnValue)) {
                                    columnValue = BDConstants.SPACE_SYMBOL;
                                }
                                buff.append(columnValue.trim());
                                
                                continue;
                    		} 
                    		// Do not show the Proposal Name value.
                    		else if (BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID.equals(columnName)) {
                    			continue;
                    		}
                    	}

                    	if (bobColumns.isColumnEnabledForPdfAndCsv(columnName)  && displayColumn(columnName, bobSummaryVO)) {
                            String columnValue = getColumnValueForCSVPDF(columnName, bobReportVO, true);
                            if (StringUtils.isBlank(columnValue)) {
                                columnValue = BDConstants.SPACE_SYMBOL;
                            }
                            buff.append(columnValue.trim());
                        } else if (allApplicableColumnsMap.get(columnName)) {
                            buff.append(BDConstants.SPACE_SYMBOL).append(COMMA);
                        }
                    }
                    buff.append(LINE_BREAK);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateDownloadAllDataForTabs().");
        }
        
        return buff.toString();
    }

    /**
     * The CSV_All file shows in the main report, those columns that are enabled in atleast one of
     * the tabs. This method helps in retrieving all those columns that are enabled in atleast one
     * tab.
     * 
     * @param currentTabs - An Array of all the tabs.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @return - a Map of <column Names, "true" if the column is enabled in atleast one tab>
     */
    private Map<String, Boolean> getAllApplicableColumnsForAllTabs(String[] currentTabs,
            BDUserProfile userProfile, BDUserRole userRole,  Map<String, BlockOfBusinessReportData> reportDataList) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAllApplicableColumnsForAllTabs().");
        }
        
        Map<String, Boolean> allApplicableColumnsMap = new HashMap<String, Boolean>();
        for (String columnID : BlockOfBusinessUtility.columnsList) {
            allApplicableColumnsMap.put(columnID, Boolean.FALSE);
        }
        BOBColumnsApplicableToTab bobColumns = null;
        try {
            for (String currentTab : currentTabs) {
            	BlockOfBusinessReportData reportData = reportDataList.get(currentTab);
            	if (reportData != null && reportData.getDetails() != null
                        && !reportData.getDetails().isEmpty()) {
	            	BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
	                bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole,
	                        userProfile.isInMimic());
	                for (String columnID : BlockOfBusinessUtility.columnsList) {
	                    if (displayColumn(columnID, bobSummaryVO) && bobColumns.isColumnEnabledForPdfAndCsv(columnID)) {
	                        allApplicableColumnsMap.put(columnID, Boolean.TRUE);
	                    }
	                }
            	}
            	
            	if(reportData == null && !isApplicableColumnsAvailable(allApplicableColumnsMap)){
            		bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole,
                            userProfile.isInMimic());
                    for (String columnID : BlockOfBusinessUtility.columnsList) {
                        if (bobColumns.isColumnEnabledForPdfAndCsv(columnID) && !isRiaColumn(columnID)) {
                            allApplicableColumnsMap.put(columnID, Boolean.TRUE);
                        }
                    }
            	}
            }
            
            // In CSV-ALL report, the Proposal Name is displayed under the Contract Name column itself.
            // Hence, disabling the Proposal Name column so that it does not get displayed in CSV-ALL.
            allApplicableColumnsMap.put(BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID, Boolean.FALSE);
            
        } catch (SystemException e) {
            // Do nothing.
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getAllApplicableColumnsForAllTabs().");
        }
        
        return allApplicableColumnsMap;
    }

    /**
     * Checks whether it is RIA column
     * @param columnID
     * @return
     */
    private boolean isRiaColumn(String columnID) {
    	if(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT.equals(columnID) ||
    			BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR.equals(columnID)){
    		return true;
    	}
		return false;
	}

	/**
     * To check is columns are applicable already
     * @param allApplicableColumnsMap
     * @return
     */
    private boolean isApplicableColumnsAvailable(
			Map<String, Boolean> allApplicableColumnsMap) {
		for(Map.Entry<String,Boolean> entry : allApplicableColumnsMap.entrySet()){
			Boolean available = entry.getValue();
			if(available){
					return true;
			}
		}
		return false;
	}

	/**
     * This method is called when the user clicks on "CSV" button. This method generates the CSV
     * file to be given back to the user.
     */
    @Override
    protected byte[] getDownloadData(BaseReportForm form, ReportData report,
            HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDownloadData().");
        }
        
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;

        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        BDUserRole userRole = userProfile.getRole();

        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);

        String buff = populateDownloadDataForTab(reportForm.getCurrentTab(), userProfile, userRole,
                reportData, reportForm, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getDownloadData().");
        }
        
        return buff.getBytes();
    }

    /**
     * This method is called for generating CSV file.
     * 
     * @param currentTab - the current tab.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportData - report data containing the information to be displayed in CSV.
     * @param reportForm - BlockOfBusinessForm object
     * @param request - the HttpServletRequest object.
     * @return - the string having CSV representation of data.
     * @throws SystemException
     */
    private String populateDownloadDataForTab(String currentTab, BDUserProfile userProfile,
            BDUserRole userRole, BlockOfBusinessReportData reportData,
            BlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadDataForTab().");
        }
        
        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);

        if (reportData == null) {
            buff.append(BDConstants.SINGLE_SPACE_SYMBOL);
            return buff.toString();
        }

        buff.append(BDConstants.CSV_BLOCK_OF_BUSINESS).append(LINE_BREAK);
        buff.append(showSummaryInfoInCSV(userProfile, userRole, reportData, reportForm));
        buff.append(LINE_BREAK);
        
        buff.append(showAsOfDateAndFiltersSelectedInCSV(reportForm, userProfile, request, reportData));
        buff.append(LINE_BREAK);

        buff.append(showMainReportInCSV(currentTab, userProfile, userRole, reportForm, reportData,
                request));

        // Messages to be displayed.
        String[] messageColl = null;
        try {
            messageColl = getMessagesToDisplay(request);
        } catch (ContentException e) {
            throw new SystemException(e, getClass().getName(), "populateDownloadDataForTab",
                    "Something wrong with CMA");
        }
        if (messageColl != null && messageColl.length > 0) {
            for (String message : messageColl) {
                buff.append(getCsvString(message)).append(LINE_BREAK);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateDownloadDataForTab().");
        }
        return buff.toString();

    }

    /**
     * This method gets the Summary Information of the report data, to be displayed in CSV file.
     * 
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportData - report data containing the information to be displayed in CSV.
     * @return
     * @throws SystemException
     * @throws SecurityServiceException
     */
    private String showSummaryInfoInCSV(BDUserProfile userProfile, BDUserRole userRole,
            BlockOfBusinessReportData reportData, BlockOfBusinessForm form) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showSummaryInfoInCSV().");
        }
        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);
        
        BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
        
        // If the user is a Internal User, then show his Name.
        if (userRole instanceof BDInternalUser && !userProfile.isInMimic()) {
            buff.append(BDConstants.CSV_BDINTERNAL_USER_NAME).append(BDConstants.COLON_SYMBOL)
                    .append(COMMA).append(getCsvString(form.getInternalUserName())).append(
                            LINE_BREAK);
        // If the user is a BDFirmRep, show his Name and BDFirm's associated to his profile.
        } else if (userRole instanceof BDFirmRep) {
            ArrayList<String> firmNamesList = form.getAssociatedFirmNames();
            if (firmNamesList != null && firmNamesList.size() > 0) {
                buff.append(BDConstants.CSV_FIRM_NAMES).append(BDConstants.COLON_SYMBOL);
            }
            
            if (firmNamesList != null && !firmNamesList.isEmpty()) {
                buff.append(COMMA).append(getCsvString(firmNamesList.get(0))).append(LINE_BREAK);

                if (firmNamesList.size() > 1) {
                    for (String firmName : firmNamesList.subList(1, firmNamesList.size())) {
                        buff.append(BDConstants.SPACE_SYMBOL).append(COMMA).append(
                                getCsvString(firmName)).append(LINE_BREAK);
                    }
                }
            }
        // If the user is a FinancialRep, FinancialRep's assistant or a Internal user mimicking
            // them, then show the Financial Rep User Name and list of Producer codes,
            // associated Firm Names.
        } else {
            buff.append(BDConstants.CSV_FINANCIAL_REP).append(BDConstants.COLON_SYMBOL).append(
                    COMMA).append(getCsvString(form.getFinancialRepUserName())).append(LINE_BREAK);

            ArrayList<BrokerInfoVO> brokerInfoList = bobSummaryVO.getBrokerInfoVO();
            if (brokerInfoList != null && !brokerInfoList.isEmpty()) {
                buff.append(BDConstants.CSV_PRODUCER_CODE).append(COMMA).append(
                        BDConstants.CSV_FIRM_NAME).append(LINE_BREAK);
                for (BrokerInfoVO brokerInfo : brokerInfoList) {
                    buff.append(brokerInfo.getProducerCode()).append(COMMA).append(
                            getCsvString(brokerInfo.getBdFirmName())).append(LINE_BREAK);
                }
            }
        }
        buff.append(LINE_BREAK);

        if (bobSummaryVO.getActiveContractAssets() != null) {
            buff.append(BDConstants.CSV_ACTIVE_CONTRACT_ASSETS).append(COMMA);
            if (reportData.getResultTooBigInd()) {
                buff.append(BDConstants.HYPHON_SYMBOL);
            } else {
                buff.append(getCsvString(bobSummaryVO.getActiveContractAssets()));
            }
            buff.append(LINE_BREAK);
        }
        
        buff.append(BDConstants.CSV_NUM_ACTIVE_CONTRACTS).append(COMMA).append(
                bobSummaryVO.getNumOfActiveContracts()).append(LINE_BREAK);
        
        buff.append(BDConstants.CSV_NUM_OF_LIVES).append(COMMA);
        if (reportData.getResultTooBigInd()) {
            buff.append(BDConstants.HYPHON_SYMBOL);
        } else {
            buff.append(bobSummaryVO.getNumOfLives());
        }
        buff.append(LINE_BREAK);
        
        buff.append(LINE_BREAK);

		buff.append(BDConstants.CSV_NUM_OUTSTANDING_PROPOSALS).append(
				form.getShowPNAndPPContractCountAsOfLatestDateFootnote() ? "^" : "")
				.append(COMMA).append(bobSummaryVO.getNumOfOutstandingProposals())
				.append(LINE_BREAK);
        
        buff.append(BDConstants.CSV_NUM_PENDING_CONTRACTS).append(
				form.getShowPNAndPPContractCountAsOfLatestDateFootnote() ? "^" : "")
				.append(COMMA).append(bobSummaryVO.getNumOfPendingContracts())
				.append(LINE_BREAK);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showSummaryInfoInCSV().");
        }
        return buff.toString();
    }

    /**
     * This method gets the "as of date" , "filers used" to be displayed in CSV file.
     * 
     * @param reportForm - BlockOfBusinessForm object.
     * @param userProfile - BDUserProfile object.
     * @param request - The HttpServletRequest object.
     * @param reportData 
     * @return - String having the CSV representation of "as of date", "filters used".
     * @throws SystemException
     */
    private String showAsOfDateAndFiltersSelectedInCSV(BlockOfBusinessForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request, BlockOfBusinessReportData reportData) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showAsOfDateAndFiltersSelectedInCSV().");
        }
        // Append Report As Of Date.
        ProtectedStringBuffer buff = new ProtectedStringBuffer(200);
        buff.append(BDConstants.CSV_REPORT_AS_OF).append(COMMA).append(
                DateRender.formatByStyle(getAsOfDate(reportForm), null,
                        RenderConstants.MEDIUM_STYLE)).append(LINE_BREAK);
        
        buff.append(showLegendSummaryInfoInCSV(reportForm, reportData));
        
        // Append Filters used.
        ArrayList<FilterInfoBean> filtersUsed = getFiltersUsed(userProfile, reportForm, request);
        if (filtersUsed != null && !filtersUsed.isEmpty()) {
            FilterInfoBean firstFilterUsed = filtersUsed.get(0);
            if(StringUtils.equals(firstFilterUsed.getFilterInfo().getId(), CLASS_STRING)){
					String classMediumName;
					try {
						classMediumName = BlockOfBusinessDAO.getFundClassMedName(firstFilterUsed.getFilterValue());
						firstFilterUsed.setFilterValue(classMediumName);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
            buff.append(BDConstants.CSV_FILTERS_USED).append(COMMA).append(
                    firstFilterUsed.getFilterInfo().getTitle()).append(COMMA).append(
                            getCsvString(firstFilterUsed.getFilterValue())).append(LINE_BREAK);

            for (FilterInfoBean filterUsed : filtersUsed.subList(1, filtersUsed.size())) {
            	
            	if(StringUtils.equals(filterUsed.getFilterInfo().getId(), CLASS_STRING)){
					String classMediumName;
					try {
						classMediumName = BlockOfBusinessDAO.getFundClassMedName(filterUsed.getFilterValue());
						filterUsed.setFilterValue(classMediumName);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            }
            	
                buff.append(BDConstants.SPACE_SYMBOL).append(COMMA).append(
                        filterUsed.getFilterInfo().getTitle()).append(COMMA).append(
                        getCsvString(filterUsed.getFilterValue())).append(LINE_BREAK);
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> showAsOfDateAndFiltersSelectedInCSV().");
        }
        return buff.toString();
    }

    /**
     * This method gives a list of Filters used.
     * 
     * @throws SystemException
     */
    private ArrayList<FilterInfoBean> getFiltersUsed(BDUserProfile userProfile,
            BlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getFiltersUsed().");
        }
        ArrayList<FilterInfoBean> filtersUsed = new ArrayList<FilterInfoBean>();
        
        // Append Filters used.
        if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
            Map<String, LabelInfoBean> quickFiltersMap = reportForm.getBobQuickFiltersMap();
            for (String filterID : BlockOfBusinessUtility.quickFilterList) {
                String filterValue = getFilterValue(filterID, reportForm, userProfile, request,
                        Boolean.TRUE);
                if (!StringUtils.isEmpty(filterValue)) {
                    if (quickFiltersMap.get(filterID) != null) {
                        filtersUsed.add(new FilterInfoBean(quickFiltersMap.get(filterID),
                                filterValue));
                        break;
                    }
                }
            }
        } else if (BDConstants.ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
            Map<String, LabelInfoBean> advanceFiltersMap = reportForm.getBobAdvancedFiltersMap();
            for (String filterID : BlockOfBusinessUtility.filterList) {
                String filterValue = getFilterValue(filterID, reportForm, userProfile, request,
                        Boolean.TRUE);
                if (!StringUtils.isEmpty(filterValue)) {
                    if (advanceFiltersMap.get(filterID) != null) {
                        filtersUsed.add(new FilterInfoBean(advanceFiltersMap.get(filterID),
                                filterValue));
                    }
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getFiltersUsed().");
        }
        return filtersUsed;
    }

    /**
     * This method gives the as of date selected by the user.
     * 
     * @param reportForm - BlockOfBusinessForm object.
     * @return - The as of date.
     */
    private Date getAsOfDate(BlockOfBusinessForm reportForm) {
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
     * This method generates the main report, to be shown in CSV file.
     * 
     * @param currentTab - the current tab.
     * @param userProfile - BDUserProfile object.
     * @param userRole - BDUserRole object.
     * @param reportForm - BlockOfBusinessForm object
     * @param reportData - report data containing the information to be displayed in CSV.
     * @param request - the HttpServletRequest object.
     * @return - the string having CSV representation of main report data.
     */
    @SuppressWarnings("unchecked")
    private String showMainReportInCSV(String currentTab, BDUserProfile userProfile,
            BDUserRole userRole, BlockOfBusinessForm reportForm,
            BlockOfBusinessReportData reportData, HttpServletRequest request) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> showMainReportInCSV().");
        }
        ProtectedStringBuffer buff = new ProtectedStringBuffer(255);
        BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
        
        BOBColumnsApplicableToTab bobColumns;
        try {
            
            bobColumns = BOBColumnCriteria.getApplicableColumns(currentTab, userRole, userProfile
                    .isInMimic()).createCopy();
            
            DynamicColumnModification.INSTANCE.override(
                    new ColumnValueOverrideEnabler(bobColumns),
                    ArrayUtility.toUnsortedSet(reportData));
            
        } catch (SystemException e) {
            // Return the StringBuffer without any values in it.
            return buff.toString();
        }
        
        buff.append(BDConstants.COL_CONTRACT_STATUS);
        for (String columnName : BlockOfBusinessUtility.columnsList) {
            if (bobColumns.isColumnEnabledForPdfAndCsv(columnName) && displayColumn(columnName, bobSummaryVO)) {
                buff.append(COMMA).append(bobColumns.getTitle(columnName));
            }
        }
        buff.append(LINE_BREAK);
        
        ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData
                .getDetails();
        
        if (bobReportVOList != null) {
            for (BlockOfBusinessReportVO bobReportVO : bobReportVOList) {
                String currentTabTitle = BlockOfBusinessUtility.contractStatusTitleMap
                        .get(currentTab);
                if (!StringUtils.isEmpty(currentTabTitle)) {
                    buff.append(currentTabTitle);
                } else {
                    buff.append(BDConstants.HYPHON_SYMBOL);
                }
                buff.append(COMMA);
                
                for (String columnName : BlockOfBusinessUtility.columnsList) {
                    if (bobColumns.isColumnEnabledForPdfAndCsv(columnName)  && displayColumn(columnName, bobSummaryVO)) {
                        String columnValue = getColumnValueForCSVPDF(columnName, bobReportVO, true);
                        if (StringUtils.isBlank(columnValue)) {
                            columnValue = BDConstants.SPACE_SYMBOL;
                        }
                        buff.append(columnValue.trim());
                    }
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
     * This method gets a string array of messages to be displayed in CSV file.
     * 
     * @param request - the HttpServletRequest object.
     * @return - A String array of messages.
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    private String[] getMessagesToDisplay(HttpServletRequest request) throws ContentException {
        
        String[] messageColl = null;

        ArrayList<GenericException> infoMessages = (ArrayList<GenericException>) request
                .getAttribute(BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);

        if (infoMessages != null) {
            messageColl = ContentHelper.getMessagesUsingContentType(infoMessages);
        }

        return messageColl;
    }

    /**
     * This method is used by CSV, PDF generation functionality to get the column values to be
     * displayed either in CSV or PDF.
     * 
     * @param columnID - the column ID for which the value needs to be retrieved.
     * @param bobReportVO -
     * @param isCSV - pass "true" if the method is being called by CSV functionality, else, "false".
     * @return - Column value in string format.
     */
    private String getColumnValueForCSVPDF(String columnID, BlockOfBusinessReportVO bobReportVO,
            boolean isCSV) {
        String columnValue = getColumnValue(columnID, bobReportVO, isCSV);
        if (columnValue == null) {
            columnValue = BDConstants.SPACE_SYMBOL;
        }
        
        if (isCSV) {
            if (BlockOfBusinessReportData.COL_PRODUCER_CODES_OF_BROKERS_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_NAMES_OF_THE_BROKERS_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID
                            .equals(columnID)
                    || BlockOfBusinessReportData.COL_ASSET_CHARGE_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_TR_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_REG_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_COMMISSIONS_ASSET_AB_ID.equals(columnID)
                    || BlockOfBusinessReportData.COL_COMMISSIONS_PRICE_CREDIT_ID.equals(columnID)
                    || columnValue.contains(COMMA)) {
                columnValue = getCsvString(columnValue);
            }
            return columnValue + COMMA;
        } else {
            return columnValue;
        }
    }
    
    /**
     * This method gets the column value, given the column name.
     * 
     * @param columnID - column name.
     * @param bobReportVO - the VO containing the report information.
     * @return - Column value in string format.
     */
    private String getColumnValue(String columnID, BlockOfBusinessReportVO bobReportVO,
            boolean isCSV) {
        if (logger.isDebugEnabled()) {
            logger.debug("inside getColumnValue().");
        }
        if (BlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(columnID)) {
            return bobReportVO.getContractName();
        }
        else if (BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID.equals(columnID)) {
            return String.valueOf(bobReportVO.getContractNumber());
        }
        else if (BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID.equals(columnID)) {
            return bobReportVO.getProposalName();
        }
        else if (BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID.equals(columnID)) {
            return String.valueOf(bobReportVO.getProposalNumber());
        }
        else if (BlockOfBusinessReportData.COL_CONTRACT_EFF_DT_ID.equals(columnID)) {
            return DateRender.formatByPattern(bobReportVO.getContractEffectiveDate(), null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
        }
        else if (BlockOfBusinessReportData.COL_PROPOSAL_DT_ID.equals(columnID)) {
            return DateRender.formatByPattern(bobReportVO.getProposalDate(), null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
        }
        else if (BlockOfBusinessReportData.COL_CONTRACT_PLAN_YEAR_END_MMDD_ID.equals(columnID)) {
            return DateRender.formatByPattern(bobReportVO.getContractPlanYearEnd(), null,
                    BDConstants.DATE_MM_DD_SLASHED);
        }
        else if (BlockOfBusinessReportData.COL_CONTRACT_STATE_ID.equals(columnID)) {
            return bobReportVO.getContractState();
        }
        else if (BlockOfBusinessReportData.COL_NUM_OF_LIVES.equals(columnID)) {
            if (bobReportVO.getNumOfLives() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                return String.valueOf(bobReportVO.getNumOfLives());
            }
        }
        else if (BlockOfBusinessReportData.COL_TOTAL_ASSETS_ID.equals(columnID)) {
            if (bobReportVO.getTotalAssets() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String totalAssets = bobReportVO.getTotalAssets().toString();
                if (isCSV) {
                    return totalAssets;
                } else {
                    return NumberRender.formatByType(totalAssets, null,
                            RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID
                .equals(columnID)) {
            if (bobReportVO.getTransferredAssetsPriorToCharges() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String transferredAssetsPriorToChgs = bobReportVO
                        .getTransferredAssetsPriorToCharges().toString();
                if (isCSV) {
                    return transferredAssetsPriorToChgs;
                } else {
                    return NumberRender.formatByType(transferredAssetsPriorToChgs, null,
                            RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_ASSET_CHARGE_ID.equals(columnID)) {
            if (bobReportVO.getAssetCharge() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String assetCharge = bobReportVO.getAssetCharge().toString();
                if (isCSV) {
                    return assetCharge;
                } else {
                    return NumberRender.format(assetCharge, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_EXPECTED_FIRST_YEAR_ASSETS_ID.equals(columnID)) {
            if (bobReportVO.getExpectedFirstYearAssets() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String expectedFirstYearAssets = bobReportVO.getExpectedFirstYearAssets()
                        .toString();
                if (isCSV) {
                    return expectedFirstYearAssets;
                } else {
                    return NumberRender.formatByType(expectedFirstYearAssets, null,
                            RenderConstants.CURRENCY_TYPE, Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_TR_ID.equals(columnID)) {
            if (bobReportVO.getCommissionsDepositTR() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commisionDepositTR = bobReportVO.getCommissionsDepositTR().toString();
                if (isCSV) {
                    return commisionDepositTR;
                } else {
                    return NumberRender.format(commisionDepositTR, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
        else if (BlockOfBusinessReportData.COL_COMMISSIONS_DEPOSIT_REG_ID.equals(columnID)) {
            if (bobReportVO.getCommissionsDepositReg() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commisionDepositReg = bobReportVO.getCommissionsDepositReg().toString();
                if (isCSV) {
                    return commisionDepositReg;
                } else {
                    return NumberRender.format(commisionDepositReg, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_COMMISSIONS_ASSET_AB_ID.equals(columnID)) {
            if (bobReportVO.getCommissionsAssetAB() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commisionAssetAB = bobReportVO.getCommissionsAssetAB().toString();
                if (isCSV) {
                    return commisionAssetAB;
                } else {
                    return NumberRender.format(commisionAssetAB, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
      // FD 2013 changes 
     //---------------------------------------------------------------------------------------------------------------------------------------------------------------   
        else if (BlockOfBusinessReportData.COMMISSION_DEPOSIT_TR_1YR.equals(columnID)) {
            if (bobReportVO.getCommissionDepositTr1yr() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsDBTR = bobReportVO.getCommissionDepositTr1yr().toString();
                if (isCSV) {
                    return commissionsDBTR;
                } else {
                    return NumberRender.format(commissionsDBTR, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
        else if (BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_1YR.equals(columnID)) {
            if (bobReportVO.getCommissionDepositReg1Yr() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsDBReg = bobReportVO.getCommissionDepositReg1Yr().toString();
                if (isCSV) {
                    return commissionsDBReg;
                } else {
                    return NumberRender.format(commissionsDBReg, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COMMISSION_DEPOSIT_REG_REN.equals(columnID)) {
            if (bobReportVO.getCommissionDepositRegRen() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsDBRGRen = bobReportVO.getCommissionDepositRegRen().toString();
                if (isCSV) {
                    return commissionsDBRGRen;
                } else {
                    return NumberRender.format(commissionsDBRGRen, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
        else if (BlockOfBusinessReportData.COMMISSION_ASSET_1YR.equals(columnID)) {
            if (bobReportVO.getCommissionAsset1Year() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsAB1 = bobReportVO.getCommissionAsset1Year().toString();
                if (isCSV) {
                    return commissionsAB1;
                } else {
                    return NumberRender.format(commissionsAB1, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
        else if (BlockOfBusinessReportData.COMMISSION_ASSET_REN.equals(columnID)) {
            if (bobReportVO.getCommissionAssetRen() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsABRen = bobReportVO.getCommissionAssetRen().toString();
                if (isCSV) {
                    return commissionsABRen;
                } else {
                    return NumberRender.format(commissionsABRen, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID.equals(columnID)) {
            if (bobReportVO.getRiaTotalFee() == null) {
            	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK)) {
            		return bobReportVO.getRiaFirmName();
            	} else {
            		return StringUtils.EMPTY;
            	}
            } else {
                String commissionsRIAFees = bobReportVO.getRiaTotalFee().toString();
                if (isCSV) {
                    return NumberRender.format(commissionsRIAFees, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.DECIMAL_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                } else {
                    return NumberRender.format(commissionsRIAFees, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        
        else if (BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS.equals(columnID)) {
            if (bobReportVO.getCommissionAssetAllYrs() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String commissionsAB = bobReportVO.getCommissionAssetAllYrs().toString();
                if (isCSV) {
                    return commissionsAB;
                } else {
                    return NumberRender.format(commissionsAB, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        //----------------------------------------------------------------------------------------------------------------------------------------
        
        
        //RIA FEE CHANGES------------------------------------------------------------------------------------------------------------------------
        
        else if (BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT.equals(columnID)) {
            if (StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.isRiaBpsAvailable()) {
            		return bobReportVO.getRiaFirmName();
            } else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || !bobReportVO.isRiaBpsAvailable()){
        		return BDConstants.HYPHON_SYMBOL;
        	} else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.isRiaBpsAvailable()){
                String riaBps = bobReportVO.getRiaBps().toString();
                if (isCSV) {
                    return riaBps;
                } else {
                    return NumberRender.format(riaBps, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaBpsMin() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaBpsMin() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaBpsMin() != null){
        		String riaBpsMin = bobReportVO.getRiaBpsMin().toString();
                if (isCSV) {
                    return riaBpsMin;
                } else{
                    return NumberRender.format(riaBpsMin, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaBpsMax() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaBpsMax() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaBpsMax() != null){
        		String riaBpsMax = bobReportVO.getRiaBpsMax().toString();
                if (isCSV) {
                    return riaBpsMax;
                } else{
                    return NumberRender.format(riaBpsMax, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaAcBlend() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaAcBlend() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaAcBlend() != null){
        		String riaAcBlend = bobReportVO.getRiaAcBlend().toString();
                if (isCSV) {
                    return riaAcBlend;
                } else{
                    return NumberRender.format(riaAcBlend, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaAcTiered() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaAcTiered() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaAcTiered() != null){
        		String riaAcTiered = bobReportVO.getRiaAcTiered().toString();
                if (isCSV) {
                    return riaAcTiered;
                } else{
                    return NumberRender.format(riaAcTiered, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaFlatFeePerHead() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaFlatFeePerHead() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaFlatFeePerHead() != null){
        		String riaFlatFeePerHead = bobReportVO.getRiaFlatFeePerHead().toString();
                if (isCSV) {
                    return riaFlatFeePerHead;
                } else{
                    return NumberRender.format(riaFlatFeePerHead, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT.equals(columnID)) {
        	if(StringUtils.equals(bobReportVO.getRiaFirmName(), BDConstants.AESTRIK) && bobReportVO.getRiaFlatFeeProrata() != null){
        		return bobReportVO.getRiaFirmName();
        	}else if(StringUtils.isEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) || bobReportVO.getRiaFlatFeeProrata() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	}else if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(bobReportVO.getRiaFirmName())) && bobReportVO.getRiaFlatFeeProrata() != null){
        		String riaFlatFeeProrata = bobReportVO.getRiaFlatFeeProrata().toString();
                if (isCSV) {
                    return riaFlatFeeProrata;
                } else{
                    return NumberRender.format(riaFlatFeeProrata, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}else{
        		return BDConstants.HYPHON_SYMBOL;
        	}
        }
        
        else if (BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE.equals(columnID)) {
        	if(bobReportVO.getCofid321ABFee() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	} else {
        		String cofid321ABFee = bobReportVO.getCofid321ABFee().toString();
                if (isCSV) {
                    return cofid321ABFee;
                } else{
                    return NumberRender.format(cofid321ABFee, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}
        }
        
        else if (BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT.equals(columnID)) {
        	if(bobReportVO.getCofid321DBFee() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	} else {
        		String cofid321DBFee = bobReportVO.getCofid321DBFee().toString();
                if (isCSV) {
                    return cofid321DBFee;
                } else{
                    return NumberRender.format(cofid321DBFee, null,
                            BDConstants.AMOUNT_FORMAT_TWO_DECIMALS, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}
        }
        
        else if (BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE.equals(columnID)) {
        	if(bobReportVO.getCofid338ABFee() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	} else {
        		String cofid338ABFee = bobReportVO.getCofid338ABFee().toString();
                if (isCSV) {
                    return cofid338ABFee;
                } else{
                    return NumberRender.format(cofid338ABFee, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}
        }
        
        else if (BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT.equals(columnID)) {
        	if(bobReportVO.getCofid338DBFee() == null){
        		return BDConstants.HYPHON_SYMBOL;
        	} else {
        		String cofid338DBFee = bobReportVO.getCofid338DBFee().toString();
                if (isCSV) {
                    return cofid338DBFee;
                } else{
                    return NumberRender.format(cofid338DBFee, null,
                            BDConstants.AMOUNT_FORMAT_TWO_DECIMALS, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
        	}
        }
        
        else if (BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR.equals(columnID)) {
            if (bobReportVO.getDes338Ind() == null) {
                return BDConstants.HYPHON_SYMBOL;
            } else {
                String des338Ind = bobReportVO.getDes338Ind().toString();
                if (isCSV) {
                    return des338Ind;
                } else {
                    return des338Ind;
                }
            }
        }
        
        //----------------------------------------------------------------------------------------------------------------------------------------
        
        
        else if (BlockOfBusinessReportData.COL_COMMISSIONS_PRICE_CREDIT_ID.equals(columnID)) {
			//CL 122165 fix - Report displays 0.000 rather HYPHON
            String commisionPriceCredit = bobReportVO.getCommissionsPriceCredit();
            if (StringUtils.isEmpty(commisionPriceCredit)) {
                return BDConstants.HYPHON_SYMBOL;
            }else {
                if (isCSV) {
                    return commisionPriceCredit;
                } else {
                    return NumberRender.format(commisionPriceCredit, null,
                            BDConstants.AMOUNT_3DECIMAL_FORMAT, RenderConstants.CURRENCY_TYPE,
                            BDConstants.SCALE_3, BDConstants.ROUNDING_MODE, BDConstants.INTDIGITS,
                            Boolean.FALSE);
                }
            }
        }
        else if (BlockOfBusinessReportData.COL_PRODUCER_CODES_OF_BROKERS_ID.equals(columnID)) {
            if (bobReportVO.getProducerCodes() == null) {
                return BDConstants.SPACE_SYMBOL;
            } else {
                return bobReportVO.getProducerCodes();
            }
        }
        else if (BlockOfBusinessReportData.COL_NAMES_OF_THE_BROKERS_ID.equals(columnID)) {
            if (bobReportVO.getFinancialRepNameAndFirmName() == null) {
                return BDConstants.SPACE_SYMBOL;
            }
            return bobReportVO.getFinancialRepNameAndFirmName();
        }
        else if (BlockOfBusinessReportData.COL_BDFIRM_NAME_ID.equals(columnID)) {
            // TODO: Need to remove this. This column is not being used anymore.
            return "firm Name place Hodler";
        }
        else if (BlockOfBusinessReportData.COL_RVP_ID.equals(columnID)) {
            return bobReportVO.getRvpName();
        }
        else if (BlockOfBusinessReportData.COL_PRODUCT_TYPE_ID.equals(columnID)) {
            return bobReportVO.getProductType();
        }
        else if (BlockOfBusinessReportData.COL_US_OR_NY_ID.equals(columnID)) {
            return bobReportVO.getUsOrNy();
        }
        else if (BlockOfBusinessReportData.COL_CLASS_ID.equals(columnID)) {
            return bobReportVO.getFundClass();
        }
        else if (BlockOfBusinessReportData.COL_DISCONTINUED_DATE_ID.equals(columnID)) {
            String discontinuedDate = BDConstants.HYPHON_SYMBOL;
            if (BDConstants.CONTRACT_STATUS_DI.equals(bobReportVO.getContractStatusCode())) {
                discontinuedDate = DateRender.formatByPattern(bobReportVO.getDiscontinuanceDate(),
                        null, RenderConstants.MEDIUM_MDY_SLASHED);
            }
            return discontinuedDate;
        }
        return null;
    }

    /**
     * This method will return the File Name of the CSV file.
     * 
     * The CSV file will be of the Format "BlockOfBusiness-mmddyyyy.csv".
     */
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        return    BlockOfBusinessReportData.CSV_REPORT_NAME
                + BDConstants.HYPHON_SYMBOL
                + DateRender.format(getAsOfDate((BlockOfBusinessForm) form),
                        RenderConstants.MEDIUM_MDY_SLASHED).replace(BDConstants.SLASH_SYMBOL,
                        BDConstants.SPACE_SYMBOL) 
                + CSV_EXTENSION;
    }

    /**
     * This method will return the File Name of the CSV file.
     * 
     * The CSV file will be of the Format "BlockOfBusinessAll-mmddyyyy.csv".
     */
    protected String getFileNameForCSVAll(BaseReportForm form, HttpServletRequest request) {
        return BlockOfBusinessReportData.CSV_ALL_REPORT_NAME
                + BDConstants.HYPHON_SYMBOL
                + DateRender.format(getAsOfDate((BlockOfBusinessForm) form),
                        RenderConstants.MEDIUM_MDY_SLASHED).replace(BDConstants.SLASH_SYMBOL,
                        BDConstants.SPACE_SYMBOL) + CSV_EXTENSION;
    }

    /**
     * This method gets the report handler name.
     */
    @Override
    protected String getReportId() {
        return BlockOfBusinessReportData.REPORT_ID;
    }

    /**
     * This method gets the CSV report name.
     */
    @Override
    public String getReportName() {
        return BlockOfBusinessReportData.CSV_REPORT_NAME;
    }

	/**
	 * Sorting is being done in the following manner:
	 * 
	 * If sort is by Contract Name, 
	 * 	Contract Name ascending,
	 * 	Contract Number ascending.
	 * 
	 * If sort is by Contract Number, 
	 * 	Contract Number ascending. 
	 * 
	 * If sort is by any other field,
	 *  {field} ascending / descending 
	 * 	Contract Name ascending,
	 * 	Contract Number ascending. 
	 * 
	 * If the user is present in Outstanding Proposals tab, sorting will be done by 
	 *  - "Client Short Name" instead of "Contract Name"
	 *  - "Proposal Number" instead of "Contract Number" 
	 *  in the example above.
	 *  
	 */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        String sortField = form.getSortField();
        String sortDirection = form.getSortDirection();

    	criteria.insertSort(sortField, sortDirection);

    	// Contract Name, Contract Number columns are not present in Outstanding Proposals tab.
    	// Outstanding Proposals tab has Proposal Name, Proposal Number. Hence, based on the current tab,
    	// inserting the correct secondary sort order.
    	String contractNameOrProposalNameSortField = BlockOfBusinessReportData.COL_CONTRACT_NAME_ID;
    	String contractNumberOrProposalNumberSortField = BlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID;

    	if (BDConstants.OUTSTANDING_PROPOSALS_TAB.equals(((BlockOfBusinessForm)form).getCurrentTab())) {
    		contractNameOrProposalNameSortField = BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID;
    		contractNumberOrProposalNumberSortField = BlockOfBusinessReportData.COL_PROPOSAL_NUMBER_ID;
    	}
    	
    	if (contractNameOrProposalNameSortField.equals(sortField)) {
    		criteria.insertSort(contractNumberOrProposalNumberSortField, ReportSort.ASC_DIRECTION);
        } else if (contractNumberOrProposalNumberSortField.equals(sortField)) {
        	// Do nothing. No other secondary sort needs to be added.. 
        	// No two records will have the same Contract Number (/Proposal Number).
        } else {
        	criteria.insertSort(contractNameOrProposalNameSortField, ReportSort.ASC_DIRECTION);
        	criteria.insertSort(contractNumberOrProposalNumberSortField, ReportSort.ASC_DIRECTION);
        }
    }

    /**
     * This method has been overridden to save the Filtering criteria that has been created till
     * now.
     * 
     * The filter criteria created is saved in the Form object, which will be later used to get the
     * "filters used" value in CSV/PDF.
     */
    protected ReportCriteria getReportCriteria(String reportId, BaseReportForm form,
            HttpServletRequest request) throws SystemException {
        ReportCriteria reportCriteria = super.getReportCriteria(reportId, form, request);

        ((BlockOfBusinessForm) form).setFilteringCriteriaSaved(reportCriteria);

        return reportCriteria;
    }

    /**
     * This method is used to populate the Criteria object that will be sent to the Stored Proc with
     * all the Filtering Criteria selected.
     * 
     * This method gathers the Criteria Information such as UserProfileID, UserRole, Info of the
     * User if he is in Mimic mode, Filtering Criteria selected by user and places it into the
     * Criteria object.
     */
    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportCriteria()");
        }

        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        
        BDUserProfile userProfile = (BDUserProfile) BDSessionHelper.getUserProfile(request);
        if (userProfile == null) {
            throw new SystemException("UserProfile is null");
        }

        addUserProfileRelatedFilterCriteria(userProfile, criteria, reportForm, request);
        
        String dbSessionID = getFilterValue(BlockOfBusinessReportData.FILTER_DB_SESSION_ID,
                reportForm, userProfile, request, Boolean.FALSE);
        if (!StringUtils.isEmpty(dbSessionID)) {
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_DB_SESSION_ID, Integer
                    .valueOf(dbSessionID));
        }

        addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
                getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES, reportForm,
                        userProfile, request, Boolean.FALSE));
        
        addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_AS_OF_DATE, getFilterValue(
                BlockOfBusinessReportData.FILTER_AS_OF_DATE, reportForm, userProfile, request,
                Boolean.FALSE));
        
        //To fetch complete records without Page Limit for Plan Review report Step1 Page
        if(getTask(request).equalsIgnoreCase("planReviewReports")){
        	criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        }
        
        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        // quickFilterSubmitted is null when accessing the page for the first time.
        if (quickFilterSubmitted == null) {
            return;
        }
        if (quickFilterSubmitted) {
            
            String quickFilterSelected = reportForm.getQuickFilterSelected();
            ArrayList<Integer> partyIDList = new ArrayList<Integer>();
            
            if (BlockOfBusinessReportData.FILTER_CONTRACT_NAME.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NAME)) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_NAME,
                            getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_NAME,
                                reportForm, userProfile, request, Boolean.FALSE));
                }
                
            } else if (BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER,
                            getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER,
                                reportForm, userProfile, request, Boolean.FALSE));
                }
                
            } else if (BlockOfBusinessReportData.FILTER_RPV_NAME.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) {
                    String rvpNameID = getFilterValue(BlockOfBusinessReportData.FILTER_RPV_NAME,
                            reportForm, userProfile, request, Boolean.FALSE);
					if (!BDUserProfileHelper.isInternalUser(userProfile)) {
						addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_RPV_NAME, rvpNameID);
					} else {
						if (!StringUtils.isEmpty(rvpNameID)) {
							partyIDList.add(new Integer(rvpNameID));
						}
						addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID,
		                            partyIDList);
					}
                }
            } else if (BlockOfBusinessReportData.FILTER_BDFIRM_NAME.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) {
                    String bdFirmNameID = getFilterValue(
                            BlockOfBusinessReportData.FILTER_BDFIRM_NAME, reportForm, userProfile,
                            request, Boolean.FALSE);
                    // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID
                    // as the "partyID", we should be sending it as the filter criteria.
                    if (BDUserProfileHelper.isFirmRep(userProfile)
                            || userProfile.getRole() instanceof BDRvp) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_FIRM_NAME,
                                bdFirmNameID);
                    } else {
                    if (!StringUtils.isEmpty(bdFirmNameID)) {
                        partyIDList.add(new Integer(bdFirmNameID));
                    }
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID,
                            partyIDList);
                }
                }
            } else if (BlockOfBusinessReportData.FILTER_FUND_CLASS.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_FUND_CLASS)) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_FUND_CLASS,
                        getFilterValue(
                                BlockOfBusinessReportData.FILTER_FUND_CLASS,
                                reportForm,
                                    userProfile, request, Boolean.FALSE));
                }
            } else if (BlockOfBusinessReportData.FILTER_PRODUCT_TYPE.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PRODUCT_TYPE,
                            getFilterValue(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE,
                                reportForm,
                                    userProfile, request, Boolean.FALSE));
                }
            } else if (BlockOfBusinessReportData.FILTER_US_OR_NY.equals(quickFilterSelected)) {
                if (reportForm.isQuickFilterEnabled(BlockOfBusinessReportData.FILTER_US_OR_NY)) {
                    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_US_OR_NY,
                            getFilterValue(BlockOfBusinessReportData.FILTER_US_OR_NY,
                                reportForm,
                                    userProfile, request, Boolean.FALSE));
                }
            }
        } else {
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NAME)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_NAME,
                        getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_NAME, reportForm,
                                userProfile, request, Boolean.FALSE));
            }
            if (reportForm
                    .isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER,
                    getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER, reportForm,
                            userProfile, request, Boolean.FALSE));
            }
            if (reportForm
                    .isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME,
                    getFilterValue(BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME, reportForm,
                            userProfile, request, Boolean.FALSE));
            }
            if (reportForm
                    .isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM)
                    && reportForm
                            .isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO)) {
                String assetRangeFrom = getFilterValue(
                    BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM, reportForm, userProfile,
                    request, Boolean.FALSE);
                String assetRangeTo = getFilterValue(
                        BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO,
                    reportForm, userProfile,
                        request, Boolean.FALSE);

                BigDecimal assetRangeFromValue = null;
                BigDecimal assetRangeToValue = null;
                try {
                    // Strip off all the occurrences of Comma.
                    if (!StringUtils.isEmpty(assetRangeFrom)) {
                        assetRangeFrom = StringUtils.remove(assetRangeFrom,
                                BDConstants.COMMA_SYMBOL);
                        assetRangeFromValue = new BigDecimal(assetRangeFrom);
                    }

                    if (!StringUtils.isEmpty(assetRangeTo)) {
                        assetRangeTo = StringUtils.remove(assetRangeTo, BDConstants.COMMA_SYMBOL);
                        assetRangeToValue = new BigDecimal(assetRangeTo);
                    }
                } catch (NumberFormatException nfe) {
                    // Do nothing. We can still proceed. the Asset Range From, asset Range To values
                    // will be null, and hence, will not be sent as a filter criteria to the stored
                    // proc.
                }

                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM,
                    assetRangeFromValue);
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO,
                    assetRangeToValue);
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_CONTRACT_STATE)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CONTRACT_STATE,
                    getFilterValue(BlockOfBusinessReportData.FILTER_CONTRACT_STATE, reportForm,
                            userProfile, request, Boolean.FALSE));
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_FUND_CLASS)) {
            	addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_FUND_CLASS,
                    getFilterValue(BlockOfBusinessReportData.FILTER_FUND_CLASS, reportForm,
                            userProfile, request, Boolean.FALSE));
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_CSF_FEATURE)) {
            	addFilterCriteria( criteria, BlockOfBusinessReportData.DF_FUND_FAMILY_CODE,
            			BlockOfBusinessUtility.getFundFamilyOptionsMap());  
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_CSF_FEATURE,
                    getFilterValue(BlockOfBusinessReportData.FILTER_CSF_FEATURE, reportForm,
                            userProfile, request, Boolean.FALSE));
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_US_OR_NY)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_US_OR_NY,
                        getFilterValue(
                    BlockOfBusinessReportData.FILTER_US_OR_NY, reportForm, userProfile,
                    request, Boolean.FALSE));
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_PRODUCT_TYPE)) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PRODUCT_TYPE,
                        getFilterValue(
                    BlockOfBusinessReportData.FILTER_PRODUCT_TYPE, reportForm, userProfile,
                    request, Boolean.FALSE));
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) {
	            // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
                // the partyID filter, we should be sending it as the filter criteria.
        	    if (BDUserProfileHelper.isFirmRep(userProfile) || userProfile.getRole() instanceof BDRvp) {
            	    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_FIRM_NAME,
                        getFilterValue(BlockOfBusinessReportData.FILTER_BDFIRM_NAME, reportForm,
                                userProfile, request, Boolean.FALSE));
	            }
            }
            if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) {
	            // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
                // the partyID filter, we should be sending it as the filter criteria.
        	    if (!BDUserProfileHelper.isInternalUser(userProfile)) {
            	    addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_RPV_NAME,
                        getFilterValue(BlockOfBusinessReportData.FILTER_RPV_NAME, reportForm,
                                userProfile, request, Boolean.FALSE));
	            }
            }
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_PARTY_ID,
                    getListOfPartyIDs(reportForm, userProfile, request)); 
            
            
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportCriteria()");
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
            ReportCriteria criteria, BlockOfBusinessForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> addUserProfileRelatedFilterCriteria()");
        }
        
        // In Mimic mode, the user profile id is of the user who is mimicking. When not in mimic
        // mode, it is the user profile id of the user currently logged in.
        String userProfileID = getFilterValue(BlockOfBusinessReportData.FILTER_USER_PROFILE_ID,
                reportForm, userProfile, request, Boolean.FALSE);
        if (userProfileID != null) {
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_USER_PROFILE_ID, Long
                    .valueOf(userProfileID));
        }

        // In Mimic mode, the user role is of the user who is mimicking. When not in mimic
        // mode, it is the user role of the user currently logged in.
        addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_USER_ROLE, getFilterValue(
                BlockOfBusinessReportData.FILTER_USER_ROLE, reportForm, userProfile, request,
                Boolean.FALSE)); 

        if (userProfile.isInMimic()) {
            // In Mimic mode, the user profile id is of the user who is mimicked.
            userProfileID = getFilterValue(BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
                    reportForm, userProfile, request, Boolean.FALSE);
            if (userProfileID != null) {
                addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
                        Long.valueOf(userProfileID));
            }

            // In Mimic mode, the user role is of the user who is mimicked.
            addFilterCriteria(criteria, BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE,
                    getFilterValue(BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, reportForm,
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
    private ArrayList<Integer> getListOfPartyIDs(BlockOfBusinessForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getListOfPartyIDs()");
        }
        ArrayList<Integer> partyIDList = new ArrayList<Integer>();

        if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_BDFIRM_NAME)) {
            // If the user is a Firm Rep or a RVP User, then, instead of passing the firmID as
            // the filter criteria, we should be sending the Firm name as the filter criteria.
            if (!BDUserProfileHelper.isFirmRep(userProfile)
                    && !(userProfile.getRole() instanceof BDRvp)) {
            String bdFirmNameID = getFilterValue(BlockOfBusinessReportData.FILTER_BDFIRM_NAME,
                    reportForm, userProfile, request, Boolean.FALSE);
            if (!StringUtils.isEmpty(bdFirmNameID)) {
                partyIDList.add(new Integer(bdFirmNameID));
            }
        }
        }
        if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_RPV_NAME)) {
        	if (BDUserProfileHelper.isInternalUser(userProfile)) {
        		String rpvNameID = getFilterValue(BlockOfBusinessReportData.FILTER_RPV_NAME,
                        reportForm, userProfile, request, Boolean.FALSE);
                if (!StringUtils.isEmpty(rpvNameID)) {
                    partyIDList.add(new Integer(rpvNameID));
                }
        	}
        }
        if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_SALES_REGION)) {
            String salesRegionID = getFilterValue(BlockOfBusinessReportData.FILTER_SALES_REGION,
                reportForm, userProfile, request, Boolean.FALSE);
            if (!StringUtils.isEmpty(salesRegionID)) {
                partyIDList.add(new Integer(salesRegionID));
            }
        }
        if (reportForm.isAdvancedFilterEnabled(BlockOfBusinessReportData.FILTER_SALES_DIVISION)) {
            String salesDivisionID = getFilterValue(
                    BlockOfBusinessReportData.FILTER_SALES_DIVISION, reportForm, userProfile,
                    request, Boolean.FALSE);
            if (!StringUtils.isEmpty(salesDivisionID)) {
                partyIDList.add(new Integer(salesDivisionID));
            }
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
     * @param reportForm - BlockOfBusinessForm object.
     * @param userProfile - BDUSerProfile object.
     * @param request - the HttpServletRequest object.
     * @param isCsvOrPdf - boolean variable which will tell us if we want to use the Filter value to
     *            display in CSV or PDF.
     * @return - the filter value.
     * @throws SystemException
     */
    private String getFilterValue(String filterID, BlockOfBusinessForm reportForm,
            BDUserProfile userProfile, HttpServletRequest request, boolean isCsvOrPdf)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("inside getFilterValue()");
        }
        try {
            // Filters common to both Quick, Advanced filtering sections.
            if (BlockOfBusinessReportData.FILTER_USER_PROFILE_ID.equals(filterID)) {
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
            else if (BlockOfBusinessReportData.FILTER_USER_ROLE.equals(filterID)) {
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
            } 
            else if (BlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Profile ID
                // is of that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return String.valueOf(userProfile.getBDPrincipal().getProfileId());
                }
            } 
            else if (BlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE.equals(filterID)) {
                // If the Internal user is mimicking a external user, the Mimicking user Role is of
                // that External user being mimicked.
                if (userProfile.isInMimic()) {
                    return userProfile.getRole().getRoleType().getUserRoleCode();
                }
            } 
            else if (BlockOfBusinessReportData.FILTER_DB_SESSION_ID.equals(filterID)) {
                
                Integer dbSessionID = getStoredProcSessionIDForAsOfDate(request, reportForm) == null ? null
                        : getStoredProcSessionIDForAsOfDate(request, reportForm);
                return dbSessionID == null ? null : dbSessionID.toString();
            } 
            else if (BlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES.equals(filterID)) {
                return BlockOfBusinessUtility.getContractStatus(reportForm.getCurrentTab());
            } 
            else if (BlockOfBusinessReportData.FILTER_AS_OF_DATE.equals(filterID)) {
                Date reportAsOfDate = getAsOfDate(reportForm);
                if (reportAsOfDate != null) {
                    Long reportAsOfDateL = reportAsOfDate.getTime();
                    return reportAsOfDateL.toString();
                }
                return null;
            } 
            if (BDConstants.QUICK_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                // Get the filter value, for the submitted quick filter.
                if (BlockOfBusinessReportData.FILTER_CONTRACT_NAME.equals(filterID)) {
                    return reportForm.getQuickFilterContractName();
                }
                else if (BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER.equals(filterID)) {                	
                    return reportForm.getQuickFilterContractNumber();
                }
                else if (BlockOfBusinessReportData.FILTER_RPV_NAME.equals(filterID)) {
                    // If we want to show the RVP for display purpose, return the RVP Name, else
                    // return RVP id.
                    if (isCsvOrPdf) {
                        String rvpName = BlockOfBusinessUtility.getRvpNameForIDSelected(reportForm
                                .getQuickFilterRvpSelected());
                        return rvpName;
                    } else {
                    	if (!BDUserProfileHelper.isInternalUser(userProfile)) {
                    		 String rvpName = BlockOfBusinessUtility.getRvpNameSelected(reportForm
                                     .getQuickFilterRvpSelected());
                             return rvpName;
                    	} else {
                    		return reportForm.getQuickFilterRvpSelected();
                    	}
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_BDFIRM_NAME.equals(filterID)) {
                    if (StringUtils.isBlank(reportForm.getQuickFilterFirmIDSelected())) {
                        return null;
                    }
                    if (isCsvOrPdf) {
                        String firmName = BDConstants.SPACE_SYMBOL;
                        // For a Firm Rep User, since we show the Firm Name filter in JSP page as a
                        // drop down box, the quickFilterFirmNameSelected does not get populated in
                        // JSP page. We just have the quickFilterFirmIDSelected populated (based on
                        // the firm selected by the user from drop down). We need to get the Firm
                        // Name corresponding to the Firm ID.
                        if (userProfile.getRole() instanceof BDFirmRep) {
                            firmName = BlockOfBusinessUtility.getFirmNameForAssociatedFirmID(
                                    userProfile, reportForm.getQuickFilterFirmIDSelected());
                        } else {
                            firmName = reportForm.getQuickFilterFirmNameSelected();
                        }
                        return firmName;
                        
                    } else {
                        if (userProfile.getRole() instanceof BDFirmRep
                                || userProfile.getRole() instanceof BDRvp) {
                            String firmID = null;
                            if (StringUtils.isNumeric(reportForm.getQuickFilterFirmIDSelected())) {
                                firmID = BlockOfBusinessUtility.getFirmIDForFirmPartyID(Long
                                        .parseLong(reportForm.getQuickFilterFirmIDSelected()));
                            }
                            return firmID; // Firm ID
                        } else {
                            return reportForm.getQuickFilterFirmIDSelected(); // PartyID.
                        }
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_FUND_CLASS.equals(filterID)) {
                    return reportForm.getQuickFilterFundClassSelected();
                } 
                else if (BlockOfBusinessReportData.FILTER_PRODUCT_TYPE.equals(filterID)) {
                    return reportForm.getQuickFilterProductTypeSelected();
                }
                else if (BlockOfBusinessReportData.FILTER_US_OR_NY.equals(filterID)) {
                    return reportForm.getQuickFilterUsOrNySelected();
                }
            } else if (BDConstants.ADV_FILTER_SUBMITTED.equals(isQuickFilterSubmitted(reportForm))) {
                // Get the filter value, for the submitted advanced filter.
                if (BlockOfBusinessReportData.FILTER_CONTRACT_NAME.equals(filterID)) {
                    return reportForm.getContractName();
                } 
                else if (BlockOfBusinessReportData.FILTER_CONTRACT_NUMBER.equals(filterID)) {
                    return reportForm.getContractNumber();
                } 
                else if (BlockOfBusinessReportData.FILTER_FINANCIAL_REP_NAME.equals(filterID)) {
                    return reportForm.getFinancialRepName();
                } 
                else if (BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM.equals(filterID)
                        || BlockOfBusinessReportData.FILTER_ASSET_RANGE_TO.equals(filterID)) {
                    String assetRange = null;
                    if (BlockOfBusinessReportData.FILTER_ASSET_RANGE_FROM.equals(filterID)) {
                        assetRange = reportForm.getAssetRangeFrom();
                    } else {
                        assetRange = reportForm.getAssetRangeTo();
                    }
                    
                    BigDecimal assetRangeValue = null;
                    try {
                        if (!StringUtils.isBlank(assetRange)) {
                            assetRange = StringUtils.remove(assetRange.trim(),
                                    BDConstants.COMMA_SYMBOL);
                            assetRangeValue = new BigDecimal(assetRange);
                        }
                    } catch (NumberFormatException nfe) {
                        // do nothing.
                    }
                    
                    if (assetRangeValue == null) {
                        return null;
                    }
                    
                    return assetRangeValue.toString();
                } 
                else if (BlockOfBusinessReportData.FILTER_CONTRACT_STATE.equals(filterID)) {
                    return reportForm.getContractState();
                } 
                else if (BlockOfBusinessReportData.FILTER_FUND_CLASS.equals(filterID)) {
                    return reportForm.getFundClassSelected();
                } 
                else if (BlockOfBusinessReportData.FILTER_CSF_FEATURE.equals(filterID)) {
                    // If we want to show the CSF for display purpose, return the CSF Name,
                    // else return CSF id.
                    if (isCsvOrPdf) {
                        String csfValue = reportForm.getCsfFeatureSelected();
                        if (csfValue == null) {
                            csfValue = BDConstants.SPACE_SYMBOL;
                        }
                        
                        String csfTitle = BDConstants.SPACE_SYMBOL;
                        
                        List<LabelValueBean> csfList = BlockOfBusinessUtility.getCSFFeatures();
                        if (csfList != null && !csfList.isEmpty()) {
                            for (LabelValueBean csfInfo : csfList) {
                                if (csfValue.equals(csfInfo.getValue())) {
                                    csfTitle = csfInfo.getLabel();
                                    break;
                                }
                            }
                        }
                        return csfTitle;
                    } else {
                        return reportForm.getCsfFeatureSelected();
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_PRODUCT_TYPE.equals(filterID)) {
                    return reportForm.getProductType();
                } 
                else if (BlockOfBusinessReportData.FILTER_US_OR_NY.equals(filterID)) {
                    return reportForm.getUsNySelected();
                } 
                else if (BlockOfBusinessReportData.FILTER_BDFIRM_NAME.equals(filterID)) {
                    if (StringUtils.isBlank(reportForm.getFirmIDSelected())) {
                        return null;
                    }
                    if (isCsvOrPdf) {
                        String firmName = BDConstants.SPACE_SYMBOL;
                        // For a Firm Rep User, since we show the Firm Name filter in JSP page as a
                        // drop down box, the quickFilterFirmNameSelected does not get populated in
                        // JSP page. We just have the quickFilterFirmIDSelected populated (based on
                        // the firm selected by the user from drop down). We need to get the Firm
                        // Name corresponding to the Firm ID.
                        if (userProfile.getRole() instanceof BDFirmRep) {
                            firmName = BlockOfBusinessUtility.getFirmNameForAssociatedFirmID(
                                    userProfile, reportForm.getFirmIDSelected());
                        } else {
                            firmName = reportForm.getFirmNameSelected();
                        }
                        return firmName;
                        
                    } else {
                        // Need to return the Firm ID as the Filter value for the users Firm Rep / RVP.
                        if (userProfile.getRole() instanceof BDFirmRep
                                || userProfile.getRole() instanceof BDRvp) {
                            String firmID = null;
                            if (StringUtils.isNumeric(reportForm.getFirmIDSelected())) {
                                firmID = BlockOfBusinessUtility.getFirmIDForFirmPartyID(Long
                                        .parseLong(reportForm.getFirmIDSelected()));
                            }
                            return firmID; // Firm ID
                        } else {
                            return reportForm.getFirmIDSelected(); // Party ID.
                        }
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_RPV_NAME.equals(filterID)) {
                    // If we want to show the RVP for display purpose, return the RVP Name, else
                    // return
                    // RVP id.
                    if (isCsvOrPdf) {
                        String rvpName = BlockOfBusinessUtility.getRvpNameForIDSelected(reportForm
                                .getRvpSelected());
                        return rvpName;
                    } else {
                    	if (!BDUserProfileHelper.isInternalUser(userProfile)) {
                    		 String rvpName = BlockOfBusinessUtility.getRvpNameSelected(reportForm.getRvpSelected());
                             return rvpName;
                    	} else {
                    		return reportForm.getRvpSelected();
                    	}
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_SALES_REGION.equals(filterID)) {
                    // If we want to show the Region for display purpose, return the Region Name,
                    // else
                    // return Region id.
                    if (isCsvOrPdf) {
                        String regionName = BlockOfBusinessUtility
                                .getRegionNameForIDSelected(reportForm.getSalesRegionSelected());
                        return regionName;
                    } else {
                        return reportForm.getSalesRegionSelected();
                    }
                } 
                else if (BlockOfBusinessReportData.FILTER_SALES_DIVISION.equals(filterID)) {
                    // If we want to show the Division for display purpose, return the Division
                    // Name,
                    // else return Division id.
                    if (isCsvOrPdf) {
                        String divisionName = BlockOfBusinessUtility
                                .getDivisionNameForIDSelected(reportForm.getSalesDivisionSelected());
                        return divisionName;
                    } else {
                        return reportForm.getSalesDivisionSelected();
                    }
                }
            }
        } catch (NullPointerException ne) {
            // Do Nothing.
        }
        return null;
    }

    /**
     * This method returns a value indicating if we have visited the page in the default mode (1) or
     * if the Quick Filter has been submitted (2) or if the Advanced Filter has been submitted.
     * 
     * @param reportForm
     * @return
     */
    private Integer isQuickFilterSubmitted(BlockOfBusinessForm reportForm) {
        Boolean quickFilterSubmitted = reportForm.getFromQuickFilter();
        
        // quickFilterSubmitted is null when accessing the page for the first time.
        if (quickFilterSubmitted == null) {
            return BDConstants.NO_QUICK_OR_ADV_FILTER_SUBMITTED;
        } else if (quickFilterSubmitted) {
            return BDConstants.QUICK_FILTER_SUBMITTED;
        } else {
            return BDConstants.ADV_FILTER_SUBMITTED;
        }
    }
    
    /**
     * This method is used to populate Form.
     * 
     * 1. If the Advance Filter has been submitted, the Quick Filter values are reset. 
     * 2. Also, based on, if Advance Filter was submitted or, Quick Filter was submitted,
     * the "Advance Filter" section is either shown or not shown.
     */
    @Override
    protected void populateReportForm( BaseReportForm reportForm,
            HttpServletRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportForm()");
        }
        super.populateReportForm( reportForm, request);

        BlockOfBusinessForm form = (BlockOfBusinessForm) reportForm;

        String task = getTask(request);

        // If the current tab is Outstanding Proposals tab, use the "Proposal Name" as the Default sort field.
        if (BDConstants.OUTSTANDING_PROPOSALS_TAB.equals(form.getCurrentTab())) {
            /*
             * Set default sort if we're in default task.
             */
            if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
                    || reportForm.getSortDirection().length() == 0) {
                reportForm.setSortDirection(ReportSort.ASC_DIRECTION);
            }

            /*
             * Set default sort direction if we're in default task.
             */
            if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
                    || reportForm.getSortField().length() == 0) {
                reportForm.setSortField(BlockOfBusinessReportData.COL_PROPOSAL_NAME_ID);
            }
        }

        // The report as of date, Financial Rep Name filter information comes from Broker Listing
        // page when the user in Broker Listing page clicks on Financial Rep Name Link.
        String reportAsOfDate = request.getParameter(BDConstants.REQ_PARAM_REPORT_AS_OF_DATE);
        String financialRepName = request.getParameter(BDConstants.REQ_PARAM_FINANCIAL_REP_NAME);
        if (!StringUtils.isEmpty(reportAsOfDate) && !StringUtils.isEmpty(financialRepName)) {
            form.setAsOfDateSelected(reportAsOfDate);
            form.setFinancialRepName(financialRepName);
            // The Financial Rep Name filter in Block of Business page is located in Advance Filter
            // section. The below code will make sure the advance filter section is shown.
            form.setFromQuickFilter(Boolean.FALSE);
        }
        
        // Populate the showAdvacedFilterID into the report Form. This ID tells whether to show the
        // advanced filter section or not.
        populateShowAdvancedFilterInd(form, request);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> populateReportForm()");
        }
    }

    /**
     * This method builds the Tabs in Block of Business Page.
     * 
     * Depending on the Report as of Date, few of the tabs are disabled.
     * 
     * @param form - containing the asOfDate selected by the user.
     * @param request - The request object.
     * @throws SystemException
     */
    private void buildBOBTabs(BlockOfBusinessForm reportForm, HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> buildBOBTabs()");
        }
        Boolean isDefaultDateSelected = BlockOfBusinessUtility.isDefaultDateSelected(reportForm
                .getAsOfDateSelected());
        
        UserMenu userMenu = new UserMenu();
        
        UserMenuItem userMenuItem = new UserMenuItem(BDConstants.ACTIVE_TAB,
                BDConstants.ACTIVE_TAB_TITLE, BDConstants.DO + BDConstants.ACTIVE_TAB_URL);
        userMenu.addLevelOneUserMenuItem(userMenuItem);
        
        userMenuItem = new UserMenuItem(BDConstants.OUTSTANDING_PROPOSALS_TAB,
                BDConstants.OUTSTANDING_PROPOSALS_TAB_TITLE,
                BDConstants.DO
                        + BDConstants.OUTSTANDING_PROPOSALS_TAB_URL);
        if (!isDefaultDateSelected) {
            userMenuItem.setIsEnabled(Boolean.FALSE);
        }
        userMenu.addLevelOneUserMenuItem(userMenuItem);
        
        userMenuItem = new UserMenuItem(BDConstants.PENDING_TAB, BDConstants.PENDING_TAB_TITLE,
                BDConstants.DO + BDConstants.PENDING_TAB_URL);
        if (!isDefaultDateSelected) {
            userMenuItem.setIsEnabled(Boolean.FALSE);
        }
        userMenu.addLevelOneUserMenuItem(userMenuItem);
        
        userMenuItem = new UserMenuItem(BDConstants.DISCONTINUED_TAB,
                BDConstants.DISCONTINUED_TAB_TITLE, BDConstants.DO
                        + BDConstants.DISCONTINUED_TAB_URL);
        userMenu.addLevelOneUserMenuItem(userMenuItem);

        request.getSession(false).setAttribute(BDConstants.BOB_MENU_ID, userMenu);
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> buildBOBTabs()");
        }
    }

    /**
     * This method is used to validate the input.
     */
    
    
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	    binder.addValidators(blockOfBusinessValidator);
	}
    @Autowired 
    private BlockOfBusinessValidator blockOfBusinessValidator;

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
        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
        
        BlockOfBusinessForm reportForm = (BlockOfBusinessForm) form;
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        
        BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request
                .getAttribute(BDConstants.REPORT_BEAN);

        // Setting the values in Summary section as 0, if they are coming thru stored proc as null.
        if (reportData != null) {
            BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
            if (bobSummaryVO != null) {
                if (bobSummaryVO.getActiveContractAssets() == null) {
                    bobSummaryVO.setActiveContractAssets(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (bobSummaryVO.getNumOfActiveContracts() == null) {
                    bobSummaryVO.setNumOfActiveContracts(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (bobSummaryVO.getNumOfDiscontinuedContracts() == null) {
                    bobSummaryVO.setNumOfDiscontinuedContracts(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (bobSummaryVO.getNumOfOutstandingProposals() == null) {
                    bobSummaryVO.setNumOfOutstandingProposals(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
                if (bobSummaryVO.getNumOfPendingContracts() == null) {
                    bobSummaryVO.setNumOfPendingContracts(new BigDecimal(
                            BDConstants.DEFAULT_VALUE_ZERO));
                }
            }
        }

        if (reportData != null) {
            if (BlockOfBusinessUtility.isInternalUserAndNotRVP(userProfile.getRole())
                    && reportData.getResultTooBigInd()) {
                reportData.setDetails(null);
                request.setAttribute(BDConstants.REPORT_BEAN, reportData);
                GenericException exception = new GenericException(
                        BDErrorCodes.BOB_RESULT_TOO_BIG);
                errorMessages.add(exception);
            } else if (reportData.getDetails() != null && reportData.getDetails().size() == 0) {
                ArrayList<FilterInfoBean> filtersUsed = getFiltersUsed(userProfile, reportForm,
                        request);
                if (filtersUsed == null || filtersUsed.isEmpty()) {
                    String[] params = null;
                    String currentTabTitle = BlockOfBusinessUtility.contractStatusTitleMap
                            .get(reportForm.getCurrentTab());
                    if (!StringUtils.isEmpty(currentTabTitle)) {
                        params = new String[] { currentTabTitle };
                    }
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BOB_NO_RESULT_TO_DISPLAY_FOR_TAB, params,
                            ContentTypeManager.instance().MISCELLANEOUS,false);
                    infoMessages.add(exception);
                } else {
                    GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                            BDContentConstants.BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
                            ContentTypeManager.instance().MISCELLANEOUS,false);
                    infoMessages.add(exception);
                }
            }
        }
        
        if (!infoMessages.isEmpty()) {
            setMessagesInRequest(request, infoMessages,
                    BDConstants.INFO_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        }
        if (!errorMessages.isEmpty()) {
            setMessagesInRequest(request, errorMessages,
                    BDConstants.ERROR_MSG_DISPLAY_UNDER_COLUMN_HEADER);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> validateReportData()");
        }
    }
    
    /**
     * Invokes the refresh task
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    @RequestMapping(value ="/blockOfBusiness/Discontinued/", params={"task=refresh"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doRefresh (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doRefresh");
        }

         forward = doCommon( actionForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doRefresh");
        }

        return forward;
    }
   
    /**
     * Disable column dynamically
     * 
     * @param request
     */
    private void setLegends(HttpServletRequest request, BlockOfBusinessForm reportForm) {
    	 BlockOfBusinessReportData reportData = (BlockOfBusinessReportData) request.getAttribute(BDConstants.REPORT_BEAN);
    	 BOBColumnsApplicableToTab bobColumns  = (BOBColumnsApplicableToTab) request.getAttribute(BDConstants.APPLICABLE_COLUMNS);
    	 Collection<String> legends = new ArrayList<String>();
         
    	 if (reportData != null && reportForm.isCompensationSectionDisplayed()) {
        	 legends.add(BDConstants.LEGEND_1);
        	 legends.add(BDConstants.LEGEND_2);
        	 legends.add(BDConstants.LEGEND_3);
        	 legends.add(BDConstants.LEGEND_4);
        	 legends.add(BDConstants.LEGEND_5);
        	 legends.add(BDConstants.LEGEND_6);
        	 legends.add(BDConstants.LEGEND_7);
        	 // If AB all years column enabled
        	 if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS).getEnabled()) {
        		legends.add( ContentHelper.getContentText(
						BDContentConstants.BOB_LEGEND_AB,
						ContentTypeManager.instance().MISCELLANEOUS, null));
        	 }
        	// If RIA column enabled 
        	 if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID).getEnabled()) {
        		 legends.add(BDConstants.LEGEND_8);
        	 }
         }
         Collection<String> riaLegends = new ArrayList<String>();
         if (reportData != null && reportForm.isRiaSectionDisplayed()){
        	 // If RIA column enabled and has multiple RIA's
        	 if((bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT).getEnabled() ||
        			 bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT).getEnabled() 
        			 )
        			 && reportData.getBobSummaryVO().getHasContractWithMulipleRiaAssociated()) {
        		 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.BOB_LEGEND_RIA,
						ContentTypeManager.instance().MISCELLANEOUS, null));
        	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_ASSET_BASED_BPS_FEE_AMT ,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.LEGEND_RIA_ASSET_BASED_BPS_MIN_FEE_AMT ,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_ASSET_BASED_BPS_MAX_FEE_AMT ,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_ASSET_BASED_BLENDED_FEE_AMT,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_ASSET_BASED_TIERED_FEE_AMT,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_FLAT_PER_HEAD_FEE_AMT ,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
	         if(bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT).getEnabled()) {
	        	 riaLegends.add( ContentHelper.getContentText(
						BDContentConstants.RIA_FLAT_PRORATA_FEE_AMT ,
						ContentTypeManager.instance().MISCELLANEOUS, null));
	    	 }
         }
         
		reportForm.setLegends(legends);
		reportForm.setRiaLegends(riaLegends);
		reportForm.setFiduciaryServicesTabLegends(getFiduciaryServicesTabLegends(reportData, reportForm, bobColumns));
	}
    
    private Collection<String> getFiduciaryServicesTabLegends (BlockOfBusinessReportData reportData, BlockOfBusinessForm reportForm, BOBColumnsApplicableToTab bobColumns) {
    	Collection<String> fiduciaryServicesTabLegends = new ArrayList<String>();
 		if (reportData != null && reportForm.isFiduciarySectionDisplayed()) {
 			if (bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE).getEnabled()) {
 				fiduciaryServicesTabLegends.add(ContentHelper.getContentText(BDContentConstants.LEGEND_COFID_321_ASSET_BASED_BPS_FEE,
 						ContentTypeManager.instance().MISCELLANEOUS, null));
 			}
 			if (bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT).getEnabled()) {
 				fiduciaryServicesTabLegends.add(ContentHelper.getContentText(BDContentConstants.LEGEND_COFID_321_DOLLAR_BASED_FEE_AMT,
 						ContentTypeManager.instance().MISCELLANEOUS, null));
 			}
 			if (bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE).getEnabled()) {
 				fiduciaryServicesTabLegends.add(ContentHelper.getContentText(BDContentConstants.LEGEND_COFID_338_ASSET_BASED_BPS_FEE,
 						ContentTypeManager.instance().MISCELLANEOUS, null));
 			}
 			if (bobColumns.getApplicableColumnsForTab().get(BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT).getEnabled()) {
 				fiduciaryServicesTabLegends.add(ContentHelper.getContentText(BDContentConstants.LEGEND_COFID_338_DOLLAR_BASED_FEE_AMT,
 						ContentTypeManager.instance().MISCELLANEOUS, null));
 			}
 		}
    	return fiduciaryServicesTabLegends;
    }
    
	/**
	 * Invokes the legends available in the BDConstants
	 * @param reportData 
	 * 
	 */
	private String showLegendSummaryInfoInCSV(BlockOfBusinessForm form, BlockOfBusinessReportData reportData) {
		ProtectedStringBuffer buff = new ProtectedStringBuffer(255);
		
		BlockOfBusinessSummaryVO bobSummaryVO = reportData.getBobSummaryVO();
		
		if(form.isCompensationSectionDisplayed() || (form.isRiaSectionDisplayed() && bobSummaryVO.getHasRiaFees())
				|| (form.isFiduciarySectionDisplayed() && bobSummaryVO.getHasContractsWithCofidSelected())){
			buff.append(LINE_BREAK);
			buff.append(BDConstants.LEGEND).append(LINE_BREAK);
		}
		
		if(form.isCompensationSectionDisplayed()) {
			for(String legend : form.getLegends()) {
				buff.append(getCsvString(legend)).append(LINE_BREAK);
			}
		}
		
		if (form.isRiaSectionDisplayed()  && bobSummaryVO.getHasRiaFees()){
				for(String legend : form.getRiaLegends()) {
					buff.append(getCsvString(legend)).append(LINE_BREAK);
				}
	    }
		
		if (form.isFiduciarySectionDisplayed() && bobSummaryVO.getHasContractsWithCofidSelected()) {
			for (String legend : form.getFiduciaryServicesTabLegends()) {
				buff.append(getCsvString(legend)).append(LINE_BREAK);
			}
		}
		
		if (form.isCompensationSectionDisplayed()) {

            String footnote = ContentHelper.getContentText(
                    BDContentConstants.AB_COLUMN_FOOTNOTE,
                    ContentTypeManager.instance().FOOTNOTE, null);
            buff.append(getCsvString(footnote)).append(LINE_BREAK);

            footnote = ContentHelper.getContentText(
                    BDContentConstants.DAILY_UPDATE_FOOTNOTE,
                    ContentTypeManager.instance().FOOTNOTE, null);
            buff.append(getCsvString(footnote)).append(LINE_BREAK);

        }
		
		if(form.isCompensationSectionDisplayed() || (form.isRiaSectionDisplayed() && bobSummaryVO.getHasRiaFees())
				|| (form.isFiduciarySectionDisplayed() && bobSummaryVO.getHasContractsWithCofidSelected())){
			buff.append(LINE_BREAK);
		    buff.append(LINE_BREAK);
		}
		
		return buff.toString();
	}
	
	// JAN 2015 Release ACR Rewrite Changes
	
	/**
     * The doPlanReviewReports() method to implement the functionality: - if the user navigates
     * from Block Of Business page to Contract Review Report Page, the records filtered in BOB Page would 
     * be carried to Contract Review Pages
     */
	
	
	
	/**
	 * Populates PlanReviewReportVO details
	 * 
	 * @param reportData
	 * 
	 * @return ArrayList<PlanReviewReportVO>
	 * 
	 */
	private ArrayList<PlanReviewReportUIHolder> buildContractReviewReportList(
			BlockOfBusinessReportData reportData) {

		ArrayList<PlanReviewReportUIHolder> contractReviewRequestList = new ArrayList<PlanReviewReportUIHolder>();

		if (reportData != null) {

			ArrayList<BlockOfBusinessReportVO> bobReportVOList = (ArrayList<BlockOfBusinessReportVO>) reportData
					.getDetails();

			for (BlockOfBusinessReportVO bobReportVo : bobReportVOList) {
				PlanReviewReportUIHolder contractReviewRequestVO = new PlanReviewReportUIHolder();
				contractReviewRequestVO.setContractNumber(bobReportVo
						.getContractNumber());
				contractReviewRequestVO.setContractName(bobReportVo
						.getContractName());
				contractReviewRequestVO.setContractEffectiveDate(bobReportVo
						.getContractEffectiveDate());
				contractReviewRequestVO.setContractStatusCode(bobReportVo
						.getContractStatusCode());
				contractReviewRequestVO.setContractStatusEffectiveDate(bobReportVo.getDiscontinuanceDate());
				contractReviewRequestVO.setProductId(bobReportVo.getProductId());
				contractReviewRequestList.add(contractReviewRequestVO);
				
			}
		}
		return contractReviewRequestList;
	}
	
	
	/**
     * Returns a flag to indicate whether to show the PlanReviewReports link (of BOB search) to the
     * given user
     * 
     * @param profile
     * @return boolean
     */
    private boolean showPlanReviewReportsLink(BDUserProfile profile,HttpServletRequest request) {
        boolean showPlanReviewReportsLink = false;
        try {
        	
        	/**
        	 * Production fix for not showing planReviewReport Link on live
        	 */
        	if(!PlanReviewReportUtils.isPlanReviewLaunched()) {
	        	if(!PlanReviewReportUtils.isPlanReviewFunctionalityAvailable()) {
	        		
	        		//  if the  plan review launched is 'false' and 
	        		//  if the plan review available is false 
	        		// -- >  Plan Review Reports link will suppressed
	        		
	        		return false;
	        	}
        	}
        	
			SecurityManager securityManager = ApplicationHelper
							.getSecurityManager(request.getServletContext());
			AuthorizationSubject s = new BDAuthorizationSubject();
			s.setUserProfile(profile);
			
			showPlanReviewReportsLink = securityManager.isUserAuthorized(s, URLConstants.PlanReview);
			
		} catch (Exception e) {
			logger.error("Unexpected exception occurred while validating on display of PlanReviewReportsLink in BOB page: " + e.getMessage()
					, e);
		}
        return showPlanReviewReportsLink;
    }
	 
    /**
     * display column
     * 
     * @param columns
     * @param dataConditions
     * 
     * @return boolean
     */
    private boolean displayColumn(String columnName, BlockOfBusinessSummaryVO sumary) {
    	if(sumary != null){
	    	if(BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MIN_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_ASSET_BASED_BPS_MAX_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_ASSET_BASED_BLENDED_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_ASSET_BASED_TIERED_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_FLAT_PER_HEAD_FEE_AMT.equals(columnName) ||
	    			BlockOfBusinessReportData.RIA_FLAT_PRORATA_FEE_AMT.equals(columnName)){
	    		return sumary.getHasRiaFees();
	    	}else if(BlockOfBusinessReportData.RIA_338_DESIGNATION_INDICATOR.equals(columnName)){
	    		return sumary.getHas338Designation();
			} else if (BlockOfBusinessReportData.COFID_321_ASSET_BASED_BPS_FEE.equals(columnName)
					|| BlockOfBusinessReportData.COFID_321_DOLLAR_BASED_FEE_AMT.equals(columnName)
					|| BlockOfBusinessReportData.COFID_338_ASSET_BASED_BPS_FEE.equals(columnName)
					|| BlockOfBusinessReportData.COFID_338_DOLLAR_BASED_FEE_AMT.equals(columnName)) {
				return sumary.getHasContractsWithCofidSelected();
			} else {
	    		return true;
	    	}
    	}else{
    		return true;
    	}
    }

    @RequestMapping(value ="/blockOfBusiness/Discontinued/" ,params={"task=viewBpsFeeDetails"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doViewBpsFeeDetails (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response,@RequestParam("discontinuedTab") String discontinuedTab) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

 		Map<String, Object> results = null;
 		List<RiaBandItem> riaTieredBandsplit = null;
		BigDecimal riaAcTieredRate = null;
		RiaFeeDetailsVO riaFeeDetailsVO = new RiaFeeDetailsVO();
		
		int contractNumber = Integer.parseInt(request.getParameter("contractNum"));
		int proposalNumber = Integer.parseInt(request.getParameter("propNum"));
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance("PS");
		Date asOfDate = getAsOfDate(actionForm);
		if(contractNumber > 0){
			results = feeServiceDelegate.getRiaFeesForContract(contractNumber,asOfDate,true);
		}
		else{
			results = feeServiceDelegate.getRiaFeesForProposal(proposalNumber);
		}
		riaAcTieredRate = (BigDecimal) results.get(Constants.RIA_FEE_AC_TIERED);
		if (riaAcTieredRate != null) {
			riaFeeDetailsVO.setRiaTieredRate(riaAcTieredRate);
		}
		riaTieredBandsplit = (List<RiaBandItem>) results.get(Constants.RIA_TIERED_BANDS);
		List<RiaFeeRangeVO> riaFeeRangeList = new ArrayList<RiaFeeRangeVO>();
		for (RiaBandItem riaTieredSplit : riaTieredBandsplit) {
			RiaFeeRangeVO riaTieredBand = new RiaFeeRangeVO();
			riaTieredBand.setMinAmt(riaTieredSplit.getBandMinAmount());
			riaTieredBand.setMaxAmt(riaTieredSplit.getBandMaxAmount());
			riaTieredBand.setBandRate(riaTieredSplit.getBandAnnualFeePct());
			riaFeeRangeList.add(riaTieredBand);
		}
 		List<RiaFeeDetailsVO> riaFeeDetailsList = new ArrayList<RiaFeeDetailsVO>();
 		riaFeeDetailsList.add(riaFeeDetailsVO);
 		actionForm.setRiaFeeDetailsVO(riaFeeDetailsList);
 		actionForm.setRiaFeeRangeVO(riaFeeRangeList);
 		if (logger.isDebugEnabled()) {
 			logger.debug("entry -> doViewTieredFeeDetails() in BlockOfBusinessAction");
 		}
 		return findForward(getTask(request));
 	}

    @RequestMapping(value ="/blockOfBusiness/Discontinued/", params={"task=viewTieredFeeDetails"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doViewTieredFeeDetails (@Valid @ModelAttribute("blockOfBusinessForm") BlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response,@RequestParam("discontinuedTab") String discontinuedTab) 
    throws IOException,ServletException, SystemException {
    	String  forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
    	if(bindingResult.hasErrors()){
    		try {
				beforeDoCommon( (BaseReportForm) actionForm, request, response,request.getServletContext());
			} catch (SystemException e) {
				//do nothing
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}


 		Map<String, Object> results = null;
 		List<RiaBandItem> riaBlendBandsplit = null;
		BigDecimal riaAcBlendRate = null;
		RiaFeeDetailsVO riaFeeDetailsVO = new RiaFeeDetailsVO();
		
		int contractNumber = Integer.parseInt(request.getParameter("contractNum"));
		int proposalNumber = Integer.parseInt(request.getParameter("propNum"));
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance("PS");
		Date asOfDate = getAsOfDate(actionForm);
		if(contractNumber > 0){
			results = feeServiceDelegate.getRiaFeesForContract(contractNumber,asOfDate,true);
		}
		else{
			results = feeServiceDelegate.getRiaFeesForProposal(proposalNumber);
		}
		riaAcBlendRate = (BigDecimal) results.get(Constants.RIA_FEE_AC_BLEND);
		if (riaAcBlendRate != null) {
			riaFeeDetailsVO.setRiaBlendRate(riaAcBlendRate);
		}
		riaBlendBandsplit = (List<RiaBandItem>) results.get(Constants.RIA_BLEND_BANDS);
		List<RiaFeeRangeVO> riaFeeRangeList = new ArrayList<RiaFeeRangeVO>();
		for (RiaBandItem riaBlendSplit : riaBlendBandsplit) {
			RiaFeeRangeVO riaBlendBand = new RiaFeeRangeVO();
			riaBlendBand.setMinAmt(riaBlendSplit.getBandMinAmount());
			riaBlendBand.setMaxAmt(riaBlendSplit.getBandMaxAmount());
			riaBlendBand.setBandRate(riaBlendSplit.getBandAnnualFeePct());
			riaFeeRangeList.add(riaBlendBand);
		}
 		List<RiaFeeDetailsVO> riaFeeDetailsList = new ArrayList<RiaFeeDetailsVO>();
 		riaFeeDetailsList.add(riaFeeDetailsVO);
 		actionForm.setRiaFeeDetailsVO(riaFeeDetailsList);
 		actionForm.setRiaFeeRangeVO(riaFeeRangeList);
 		if (logger.isDebugEnabled()) {
 			logger.debug("entry -> doViewBlendFeeDetails() in BlockOfBusinessAction");
 		}
 		return findForward( getTask(request));
 	}
 	
}