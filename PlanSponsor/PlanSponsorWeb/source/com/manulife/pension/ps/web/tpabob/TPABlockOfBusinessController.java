package com.manulife.pension.ps.web.tpabob;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
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

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.navigation.BaseMenuItem;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.tpabob.valueobject.TPABlockOfBusinessReportVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.participant.payrollSelfService.PayrollFeedbackServiceType;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tpabob.util.TPABlockOfBusinessUtility;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;
import com.manulife.pension.service.contract.valueobject.FswStatus;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping(value ="/tpabob")
@SessionAttributes({"tpaBlockOfBusinessForm"})
public class TPABlockOfBusinessController extends ReportController{
	@ModelAttribute("tpaBlockOfBusinessForm")
	public TPABlockOfBusinessForm populateForm() {
		return new TPABlockOfBusinessForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("default", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("sort", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("filter", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("print", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("page", "/tpabob/tpaBlockOfBusiness.jsp");
		forwards.put("csvDownloadPage", "/tpabob/tpaBlockOfBusinessCSVInputPage.jsp");

	}
	

    /**
     * The preExecute() method was overriden to solve the back button problem. 
     * Also, when the user clicks on a Contract Link in TPA BOB page, this method checks if the
     * user has access to that contract. If yes, the user is taken to Contract Home Page.
     * 
     * @throws ServletException
     * @throws IOException
     * @throws SystemException
     */
	
	 
	public String preExecute(ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {

		String forward = null;

		TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) form;
		UserProfile userProfile = SessionHelper.getUserProfile(request);

		if (tpaBobForm.getContractNumberSelected() != null && tpaBobForm.getContractNumberSelected().length() > 0) {
			int contractNumber = Integer.parseInt(tpaBobForm.getContractNumberSelected());

			// Internal / TPA User Profile ID
			String userProfileID = getFilterValue(TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID, tpaBobForm,
					userProfile, request);
			// Internal / TPA User Role
			String userRole = getFilterValue(TPABlockOfBusinessReportData.FILTER_USER_ROLE, tpaBobForm, userProfile,
					request);

			String mimickedUserProfileID = null;
			String micmickedUserRole = null;
			if (userProfile.isInternalUser()) {
				// TPA User Profile ID
				mimickedUserProfileID = getFilterValue(TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
						tpaBobForm, userProfile, request);

				// TPA User Role
				micmickedUserRole = getFilterValue(TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, tpaBobForm,
						userProfile, request);
			}

			Long mimickedUserProfileIDL = mimickedUserProfileID == null ? null : Long.parseLong(mimickedUserProfileID);

			if (TPABlockOfBusinessUtility.verifyContract(tpaBobForm, request, Long.parseLong(userProfileID), userRole,
					mimickedUserProfileIDL, micmickedUserRole, String.valueOf(contractNumber))) {
				SelectContractDetailUtil.selectContract(getUserProfile(request), contractNumber);
				return forward = Constants.HOMEPAGE_FINDER_FORWARD;
			}
		}

		if (StringUtils.equalsIgnoreCase("POST", request.getMethod())) {

			// For the "download" task, the below code is being avoided, because, the below
			// code was
			// making another call to reset() method due to which the checkBox indicators
			// were being
			// reset.
			if (!DOWNLOAD_TASK.equals(getTask(request))) {
				// do a refresh so that there's no problem using the back button

				ControllerForward forward1 = new ControllerForward("refresh",
						"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request),
						true);
				return "redirect:"+forward1.getPath();

			}
		}

		
		return forward;
	}


	 /**
	     * This method was overriden so that few variables in the Form retain their values even after
	     * resetting the form.
	     */
	    protected BaseReportForm resetForm(BaseReportForm reportForm, HttpServletRequest request) throws SystemException {

	        // Since the doDefault() method resets the Form, and since we need the tpaUserIDSelected
	        // value, we are getting the value before the call to resetForm() and later, putting it back
	        // into Form after the call to resetForm().
	        TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) reportForm;
	        String tpaUserIDSelected = tpaBobForm.getTpaUserIDSelected();
	        
	        BaseReportForm form = super.resetForm(reportForm, request);

	        ((TPABlockOfBusinessForm) form).setTpaUserIDSelected(tpaUserIDSelected);
	        
	        return form;
	    }
	    /**
	     * This is the method called for every action taken such as sorting / paging etc.
	     * In this method we check for Error / Info conditions and call super.doCommon() method.
	     */
	    public String doCommon(BaseReportForm form,
	            HttpServletRequest request, HttpServletResponse response) throws SystemException {
	        String forward = null;

	        TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) form;

	        beforeDoCommon(form, request, response);
	        
	        // Check for Error Messages.
	        boolean isErrorMsgPresent = checkForErrorConditions(tpaBobForm, request);
	        if (isErrorMsgPresent) {
	            if (logger.isDebugEnabled()) {
	                logger.debug("exit -> doCommon() in TPABlockOfBusinessAction. Error Messages Found.");
	            }
	            return forwards.get("input");
	        }

	        boolean isInfoMsgPresent = checkForInfoMsgConditions(tpaBobForm, request);
	        if (isInfoMsgPresent) {
	            request.setAttribute(CommonConstants.REPORT_BEAN, new TPABlockOfBusinessReportData());
	            if (logger.isDebugEnabled()) {
	                logger.debug("exit -> doCommon() in TPABlockOfBusinessAction. Info Messages Found.");
	            }
	            return forwards.get("input");
	        }

	        
	        forward = super.doCommon(form, request, response);

	        // Store the DBSessionID into the session.
	        setStoredProcSessionIDIntoMap(tpaBobForm, request);

	        ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
	        validateReportData(report, form, request);
	        
	        return forward;
	    }
	    
	    /**
	     * This method will carry out all the common operations that needs to be done for every request.
	     * 
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @throws SystemException
	     */
	    private void beforeDoCommon(BaseReportForm form,
	            HttpServletRequest request, HttpServletResponse response) throws SystemException {
	        
	        TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) form; 
	        /*UserProfile userProfile = SessionHelper.getUserProfile(request);*/
	        
	        // Check if the user has selected default date or not..
	        tpaBobForm.setIsDefaultDateSelected(false);
	        
	        String asOfDateChosen = tpaBobForm.getAsOfDateSelected();
	        if (StringUtils.isEmpty(asOfDateChosen)) {
	            tpaBobForm.setIsDefaultDateSelected(true);
	        } else {
	            Date asOfDateSelected = getAsOfDate(tpaBobForm);
	            Date defaultAsOfDate = TPABlockOfBusinessUtility.getMonthEndDates().get(0);
	            if (asOfDateSelected.compareTo(defaultAsOfDate) == 0) {
	                tpaBobForm.setIsDefaultDateSelected(true);
	            }
	        }
	        
	        // Call populateReportForm() method so that the Form is populated.
	        populateReportForm(form, request);

	/*        if (userProfile.getRole().isInternalUser()) {
	            // Check if the tpa user id is coming from Search TPA page. Search TPA page populates
	            // the "tpaUserIDSelected" in the TPABOBForm and sends it to this action class. This
	            // happens when a Internal User logs in & navigates to TPA BOB page thru
	            // TPA-Search/Select page.
	            String tpaUserIDSelected = tpaBobForm.getTpaUserIDSelected();

	            if (!StringUtils.isBlank(tpaUserIDSelected)) {
	                // Set the TPA UserInfo in the session.
	                UserInfo tpaUserInfo = TPABlockOfBusinessUtility.getUserInfo(Long
	                        .parseLong(tpaUserIDSelected));
	                request.getSession(false).setAttribute(Constants.TPA_USER_INFO, tpaUserInfo);
	            }
	        }                
	        */
	        buildTpaBOBTabs(request, tpaBobForm);
	    }

	    /**
	     * This method creates the TPA - BOB Tabs.
	     * 
	     * @param request - HttpServletRequest object.
	     * @param tpaBobForm - TPABlockOfBusinessForm object.
	     */
	    private void buildTpaBOBTabs(HttpServletRequest request, TPABlockOfBusinessForm tpaBobForm) {
	        
	        ArrayList<BaseMenuItem> tabsList = new ArrayList<BaseMenuItem>(3);

	        String currentTab = tpaBobForm.getCurrentTab();

	        BaseMenuItem activeTabBean = new BaseMenuItem(Constants.TPA_ACTIVE_TAB_ID,
	                Constants.TPA_ACTIVE_TAB_TITLE, Constants.DO + Constants.TPA_ACTIVE_TAB_URL,
	                Boolean.FALSE);
	        BaseMenuItem pendingTabBean = new BaseMenuItem(Constants.TPA_PENDING_TAB_ID,
	                Constants.TPA_PENDING_TAB_TITLE, Constants.DO + Constants.TPA_PENDING_TAB_URL, 
	                Boolean.FALSE);
	        BaseMenuItem discontinuedTabBean = new BaseMenuItem(Constants.TPA_DISCONTINUED_TAB_ID,
	                Constants.TPA_DISCONTINUED_TAB_TITLE, Constants.DO + Constants.TPA_DISCONTINUED_TAB_URL,
	                Boolean.FALSE);
	        
	        if (Constants.TPA_ACTIVE_TAB_ID.equals(currentTab)) {
	            activeTabBean.setIsEnabled(Boolean.TRUE);
	        }
	        else if (Constants.TPA_PENDING_TAB_ID.equals(currentTab)) {
	            pendingTabBean.setIsEnabled(Boolean.TRUE);
	        }
	        else if (Constants.TPA_DISCONTINUED_TAB_ID.equals(currentTab)) {
	            discontinuedTabBean.setIsEnabled(Boolean.TRUE);
	        }

	        tabsList.add(activeTabBean);
	        tabsList.add(pendingTabBean);
	        tabsList.add(discontinuedTabBean);
	        
	        request.setAttribute(Constants.TPA_BOB_TABS_LIST, tabsList);
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
	     * @return - boolean value indicating if the error messages need to be shown or not.
	     */
	    private boolean checkForErrorConditions(TPABlockOfBusinessForm reportForm,
	            HttpServletRequest request) {
	        boolean isErrorMsgPresent = false;

	        ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

	        /**
	         * Display Error Message if Contract Name < 3 characters.
	         */
	        String contractName = reportForm.getContractName();

	        if (StringUtils.trimToNull(contractName) != null
	                && StringUtils.trimToNull(contractName).length() < 3) {
	            ValidationError contractLessThan3Digits = new ValidationError(
	                    Constants.TPA_CONTRACT_NAME_FIELD, ErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
	            errorMessages.add(contractLessThan3Digits);
	        }

	        if (!errorMessages.isEmpty()) {
	            isErrorMsgPresent = true;
	            request.setAttribute(Environment.getInstance().getErrorKey(), errorMessages);
	        }

	        return isErrorMsgPresent;
	    }

	    /**
	     * This method checks for those conditions where we need to display a Info message to the user.
	     * @param reportForm
	     * @param request
	     * @return - boolean value indicating if the info messages need to be shown or not.
	     */
	    private boolean checkForInfoMsgConditions(TPABlockOfBusinessForm reportForm,
	            HttpServletRequest request) {
	        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();

	        boolean isInfoMsgPresent = false;
	        
	        if (!StringUtils.isBlank(reportForm.getContractNumber())) {
	            if (!StringUtils.isNumeric(reportForm.getContractNumber().trim())) {
	                GenericException exception = new GenericExceptionWithContentType(
	                        ContentConstants.TPA_BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
	                        ContentTypeManager.instance().MISCELLANEOUS);
	                infoMessages.add(exception);

	                isInfoMsgPresent = true;
	            }
	        }

	        if (!infoMessages.isEmpty()) {
	            setMessagesInRequest(request, infoMessages, Constants.TPA_BOB_INFO_MSG);
	        }
	        
	        return isInfoMsgPresent;
	    }

		/**
		 * This method forwards the request to the CSV Download Page.
		 * 
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws SystemException
		 * @throws ServletException 
		 * @throws IOException 
		 */
	    @RequestMapping(value ="/tpaBlockOfBusiness/", params= {"task=csvDownloadPage"},method = { RequestMethod.POST, RequestMethod.GET })
		public String doCsvDownloadPage(@ModelAttribute("tpaBlockOfBusinessForm") TPABlockOfBusinessForm form, HttpServletRequest request, HttpServletResponse response)
				throws SystemException, IOException, ServletException {
	    	String forward = preExecute(form, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
			TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) form;

			tpaBobForm.setAllColumnsChecked();

			// buildTpaBOBTabs() method depends on the currentTab being set in the Form.
			// Hence, the
			// currentTab is being set.
			String currentTab = Constants.TPA_ACTIVE_TAB_ID;
			/*
			 * if (Constants.TPA_PENDING_TAB_ID.equals(mapping.getParameter())) { currentTab
			 * = Constants.TPA_PENDING_TAB_ID; } else if
			 * (Constants.TPA_DISCONTINUED_TAB_ID.equals(mapping.getParameter())) {
			 * currentTab = Constants.TPA_DISCONTINUED_TAB_ID; }
			 */
			tpaBobForm.setCurrentTab(currentTab);

			buildTpaBOBTabs(request, tpaBobForm);

			return forwards.get("csvDownloadPage");
		}
	    
		/**
		 * This method is used to populate the Action Form.
		 */
	    protected void populateReportForm(BaseReportForm reportForm,
	            HttpServletRequest request) {
	        
	    	super.populateReportForm(reportForm, request);
	    	
	    	TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) reportForm;
	        
	        UserProfile userProfile = SessionHelper.getUserProfile(request);

	        if (userProfile.getRole().isInternalUser()) {
	            // Check if the tpa user id is coming from Search TPA page. Search TPA page populates
	            // the "tpaUserIDSelected" in the TPABOBForm and sends it to this action class. This
	            // happens when a Internal User logs in & navigates to TPA BOB page thru
	            // TPA-Search/Select page.
	            String tpaUserIDSelected = tpaBobForm.getTpaUserIDSelected();
	            // The tpa user id will also be stored in session. This is so that it is retained even
	            // when the Form is reset by a call in doDefault() method.
	            String tpaUserIDStoredInSession = (String) request.getSession(false).getAttribute(
	                    Constants.TPA_USERID_REQ_PARAM);
	            
	            if (!StringUtils.isBlank(tpaUserIDSelected)) {
	                request.getSession(false).setAttribute(Constants.TPA_USERID_REQ_PARAM, tpaUserIDSelected);
	                tpaBobForm.setTpaUserIDSelected(tpaUserIDSelected);
	                
	            } else {
	                tpaBobForm.setTpaUserIDSelected(tpaUserIDStoredInSession);
	            }
	        }

	        // Set the as of date if date present in the form is null.
	        String asOfDateChosen = tpaBobForm.getAsOfDateSelected();
	        if (StringUtils.isEmpty(asOfDateChosen)) {
	            Date asOfDate = getAsOfDate(tpaBobForm);
	            tpaBobForm.setAsOfDateSelected(String.valueOf(asOfDate.getTime()));
	        }
	        
	        // Get the current tab selected.
	        String currentTab = Constants.TPA_ACTIVE_TAB_ID;
	        /*if (Constants.TPA_PENDING_TAB_ID.equals(mapping.getParameter())) {
	            currentTab = Constants.TPA_PENDING_TAB_ID;
	        } else if (Constants.TPA_DISCONTINUED_TAB_ID.equals(mapping.getParameter())) {
	            currentTab = Constants.TPA_DISCONTINUED_TAB_ID;
	        }*/
	        tpaBobForm.setCurrentTab(currentTab);
	        
	        // Populate the columnList based on the current tab.
	        tpaBobForm.setApplicableColumnsForCurrentTab(TPABlockOfBusinessUtility
	                .getApplicableColumnsListForTab(currentTab));
	    }

	    @Override
	    protected String getDefaultSort() {
	        return TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID;
	    }

	    @Override
	    protected String getDefaultSortDirection() {
	        return ReportSort.ASC_DIRECTION;
	    }

	    /**
	     * This method creates the CSV version of the report.
	     */
	    @Override
	    protected byte[] getDownloadData(BaseReportForm form, ReportData report,
	            HttpServletRequest request) throws SystemException {

	        StringBuffer buffer = new StringBuffer(300);

	        TPABlockOfBusinessForm reportForm = (TPABlockOfBusinessForm) form;
	        UserProfile userProfile = SessionHelper.getUserProfile(request);
	        UserRole userRole = userProfile.getRole();

	        String[] currentTabs = null;
	        if (reportForm.getIsDefaultDateSelected()) {
	            currentTabs = new String[] { Constants.TPA_ACTIVE_TAB_ID, Constants.TPA_PENDING_TAB_ID,
	                    Constants.TPA_DISCONTINUED_TAB_ID };
	        } else {
	            currentTabs = new String[] { Constants.TPA_ACTIVE_TAB_ID,
	                    Constants.TPA_DISCONTINUED_TAB_ID };
	        }
	        
	        // This Map will hold the Report Data corresponding to each tab.
	        Map<String, TPABlockOfBusinessReportData> reportDataList = new HashMap<String, TPABlockOfBusinessReportData>();

	        try {
	            for (String currentTab : currentTabs) {
	                reportDataList.put(currentTab, getReportDataForTab(currentTab, reportForm,
	                        userProfile, userRole));
	            }

	            // Show the Main report information..
	            buffer.append(populateDownloadAllDataForTabs(currentTabs, reportDataList, reportForm,
	                    request));
	        } catch (ReportServiceException e) {
	            logger.error("Received a Report service exception: ", e);
	            throw new SystemException(e, "Received a Report service exception: " + e);
	        } catch (ContentException e) {
	            logger.error("Received a ContentException: ", e);
	            throw new SystemException(e, "Received a ContentException: " + e);
	        }
	        
	        return buffer.toString().getBytes();
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
	    private TPABlockOfBusinessReportData getReportDataForTab(String currentTab,
	            TPABlockOfBusinessForm reportForm, UserProfile userProfile, UserRole userRole)
	            throws ReportServiceException, SystemException {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getReportDataForTab().");
	        }

	        TPABlockOfBusinessReportData reportData = new TPABlockOfBusinessReportData();

	        ReportCriteria filteringCriteriaSaved = reportForm.getFilteringCriteriaSaved();
	        if (filteringCriteriaSaved == null) {
	            return reportData;
	        }

	        // The CSV will always be sorted based on Contract Name ascending, then, Contract Number
	        // ascending.
	        ReportSortList reportSortList = new ReportSortList();
	        String sortColumnName = TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID;
	        String sortColumnDirection = ReportSort.ASC_DIRECTION;
	        reportSortList.add(new ReportSort(sortColumnName, sortColumnDirection));

	        sortColumnName = TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID;
	        sortColumnDirection = ReportSort.ASC_DIRECTION;
	        reportSortList.add(new ReportSort(sortColumnName, sortColumnDirection));
	        // setSorts is used instead of insertSort, so that we want to get rid off old sort
	        // criteria.
	        filteringCriteriaSaved.setSorts(reportSortList);
	        
	        addFilterCriteria(filteringCriteriaSaved,
	                TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
	                TPABlockOfBusinessUtility
	                        .getContractStatus(currentTab));

	        reportData = (TPABlockOfBusinessReportData) ReportServiceDelegate.getInstance()
	                .getReportData(
	                filteringCriteriaSaved);

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit -> getReportDataForTab().");
	        }

	        return reportData;
	    }

	    /**
	     * This method creates the String representing the Main report that is to be shown in the CSV.
	     * 
	     * @param reportData
	     * @param reportForm
	     * @return - String representing the Main report that is to be shown in the CSV.
	     */
	    @SuppressWarnings("unchecked")
	    private String populateDownloadAllDataForTabs(String[] currentTabs,
	            Map<String, TPABlockOfBusinessReportData> reportDataList,
	            TPABlockOfBusinessForm reportForm, HttpServletRequest request) throws SystemException,
	            ContentException {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> populateDownloadAllDataForTabs().");
	        }

	        StringBuffer buff = new StringBuffer(255);

	        TPABlockOfBusinessReportData currentReportData = (TPABlockOfBusinessReportData) request
	                .getAttribute(Constants.REPORT_BEAN);

	        // Current Report Data will be null, if there were any info messages / error messages to be
	        // shown in the page and we never went to DAO to get the report details. i.e., we caught
	        // validation - Error/Info messages in the doCommon() method.
	        if (currentReportData == null) {
	            buff.append(Constants.SINGLE_SPACE_SYMBOL);
	            return buff.toString();
	        }

	        // Get the columns selected by the user..
	        Map<String, Boolean> columnsSelectedInfo = new HashMap<String, Boolean>();
	        for (String columnID : TPABlockOfBusinessUtility.getAllColumnListCSV()) {
	            columnsSelectedInfo.put(columnID, isColumnSelectedCSV(columnID, reportForm));
	        }

	        // Show Column Titles for the selected columns..
	        for (String columnID : TPABlockOfBusinessUtility.getAllColumnListCSV()) {
	            if (columnsSelectedInfo.get(columnID) != null && columnsSelectedInfo.get(columnID)) {
	                String columnTitle = TPABlockOfBusinessUtility.getCsvColumnsIdToTitleMap().get(
	                        columnID);
	                buff.append(columnTitle).append(COMMA);
	            }
	        }
	        buff.append(LINE_BREAK);

	        for (String currentTab : currentTabs) {
	            TPABlockOfBusinessReportData reportData = reportDataList.get(currentTab);

	            if (reportData != null) {
	                ArrayList<TPABlockOfBusinessReportVO> tpaBobReportList = (ArrayList<TPABlockOfBusinessReportVO>) reportData
	                        .getDetails();

	                if (tpaBobReportList != null && !tpaBobReportList.isEmpty()) {
	                    // Show Column values for the selected columns..
	                    for (TPABlockOfBusinessReportVO tpaBobReportVO : tpaBobReportList) {
	                        for (String columnID : TPABlockOfBusinessUtility.getAllColumnListCSV()) {
	                            if (columnsSelectedInfo.get(columnID) != null
	                                    && columnsSelectedInfo.get(columnID)) {
	                                String columnValue = getColumnValueForCSV(columnID, tpaBobReportVO,
	                                        reportForm);
	                                if (StringUtils.isBlank(columnValue)) {
	                                    columnValue = Constants.NA.toLowerCase();
	                                }
	                                columnValue = StringUtils.trimToEmpty(StringEscapeUtils.unescapeXml(columnValue));
	                                buff.append(getCsvString(columnValue.trim())).append(COMMA);
	                            }
	                        }
	                        buff.append(LINE_BREAK);
	                    }
	                }
	            }
	        }
	        
	        return buff.toString();
	    }
	    
	    /**
	     * This method will be used to check if a given column was selected by the user in CSV page.
	     * 
	     * @param columnID - the column ID for which we are checking if it was selected or not.
	     * @param reportForm - TPABlockOfBusinessForm object
	     * @return - true - if the column was selected, else, returns false.
	     */
	    private boolean isColumnSelectedCSV(String columnID, TPABlockOfBusinessForm reportForm) {
	        boolean columnSelected = false;
	        
	        if (TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(columnID)) {
	            return reportForm.isContractNameCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID.equals(columnID)) {
	            return reportForm.isContractNumberCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_TPA_FIRM_ID.equals(columnID)) {
	            return reportForm.isTpaFirmIDCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID.equals(columnID)) {
	            return reportForm.isBrokerNameCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CAR_NAME_ID.equals(columnID)) {
	            return reportForm.isJhClientRepNameCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_STATUS_ID.equals(columnID)) {
	            return reportForm.isContractStatusCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_EFFECTIVE_ID.equals(columnID)) {
	            return reportForm.isContractEffDtCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID.equals(columnID)) {
	            return reportForm.isLivesCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_ALLOCATED_ASSETS_ID.equals(columnID)) {
	            return reportForm.isAllocatedAssetsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_LOAN_ASSETS_ID.equals(columnID)) {
	            return reportForm.isLoanAssetsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CASH_ACCOUNT_BALANCE_ID.equals(columnID)) {
	            return reportForm.isCashAccountBalanceCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PBA_ASSETS_ID.equals(columnID)) {
	            return reportForm.isPbaAssetsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID.equals(columnID)) {
	            return reportForm.isTotalContractAssetsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_EZ_START_ID.equals(columnID)) {
	            return reportForm.isEzStartCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_EZ_INCREASE_ID.equals(columnID)) {
	            return reportForm.isEzIncreaseCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_DIRECT_MAIL_ID.equals(columnID)) {
	            return reportForm.isDirectMailCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_GIFL_ID.equals(columnID)) {
	            return reportForm.isGiflCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_MANAGED_ACCOUNT_ID.equals(columnID)) {
	            return reportForm.isManagedAccountCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_SEND_SERVICE_ID.equals(columnID)) {
	            return reportForm.isSendServiceCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_ID.equals(columnID)) {
	            return reportForm.isJohnHancockPassiveTrusteeCheckBoxInd();
	        }
	        //RPSSO-124653 Start
	        else if (TPABlockOfBusinessReportData.COL_SFC_ID.equals(columnID)) {
	            return reportForm.isSfcCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PEP_ID.equals(columnID)) {
	            return reportForm.isPepCheckBoxInd();
	        }	
	      //RPSSO-124653 End 
	        else if (TPABlockOfBusinessReportData.COL_PAM_ID.equals(columnID)) {
	            return reportForm.isPamCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_ONLINE_BENEFICIARY_DESIGNATION_ID.equals(columnID)) {
	                return reportForm.isOnlineBeneficiaryDsgnCheckBoxInd();
	            
	        } 
	        else if (TPABlockOfBusinessReportData.COL_ONLINE_WITHDRAWALS_ID.equals(columnID)) {
	            return reportForm.isOnlineWithdrawalsCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_SYS_WITHDRAWALS_ID.equals(columnID)) {
	            return reportForm.isSysWithdrawalCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_VESTING_PERCENTAGE_ID.equals(columnID)) {
	            return reportForm.isVestingPercentageCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_VESTING_ON_STATEMENTS_ID.equals(columnID)) {
	            return reportForm.isVestingOnStatementsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PERMITTED_DISPARITY_ID.equals(columnID)) {
	            return reportForm.isPermittedDisparityCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_FSW_ID.equals(columnID)) {
	            return reportForm.isFswCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_DIO_ID.equals(columnID)) {
	            return reportForm.isDioCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_TPA_SIGNING_AUTHORITY_ID.equals(columnID)) {
	            return reportForm.isTpaSigningAuthorityCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_COW_ID.equals(columnID)) {
	            return reportForm.isCowCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_PAYROL_PATH_ID.equals(columnID)) {
	            return reportForm.isPayRollPathCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PAYROLL_FEEDBACK_SVC_ID.equals(columnID)) {
	            return reportForm.isPayrollFeedbackCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_ID.equals(columnID)) {
	            return reportForm.isPlanHighlightsCheckBoxInd();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_REVIEWED_ID.equals(columnID)) {
	            return reportForm.isPlanHighlightsReviewedCheckBoxInd();
	        }
	        else if (TPABlockOfBusinessReportData.COL_INSTALLMENTS_ID.equals(columnID)) {
	            return reportForm.isInstallmentsCheckBoxInd();
	        } 
	        
	        return columnSelected;
	    }

	    /**
	     * This method will be used to get the column value for the given column ID, to be shown in CSV
	     * page.
	     * 
	     * @param columnID - the column ID for which we are checking if it was selected or not.
	     * @param tpaBobReportVO - TPABlockOfBusinessReportVO object
	     * @return - String representation of column value to be displayed in CSV
	     * @throws SystemException 
	     */
	    private String getColumnValueForCSV(String columnID, TPABlockOfBusinessReportVO tpaBobReportVO,
	            TPABlockOfBusinessForm reportForm) throws SystemException {
	        
	        if (TPABlockOfBusinessReportData.COL_CONTRACT_NAME_ID.equals(columnID)) {
	            return tpaBobReportVO.getContractName();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_NUMBER_ID.equals(columnID)) {
	            return String.valueOf(tpaBobReportVO.getContractNumber());
	        } 
	        else if (TPABlockOfBusinessReportData.COL_TPA_FIRM_ID.equals(columnID)) {
	            return tpaBobReportVO.getTpaFirmID();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_FINANCIAL_REP_ID.equals(columnID)) {
	            return tpaBobReportVO.getFinancialRepName();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CAR_NAME_ID.equals(columnID)) {
	            return tpaBobReportVO.getCarName();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_STATUS_ID.equals(columnID)) {
	            return tpaBobReportVO.getContractStatus();
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CONTRACT_EFFECTIVE_ID.equals(columnID)) {
	        	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        	String contractEffectiveDate = "N/A";
	        	try{
		            Date defaultDate = dateFormat.parse("9999-12-31");
		            if(!defaultDate.equals(tpaBobReportVO.getContractEffectiveDate())){
		            	contractEffectiveDate = DateRender.formatByPattern(tpaBobReportVO.getContractEffectiveDate(), null,
		                        RenderConstants.EXTRA_LONG_MDY);
		            }
	        	}catch(ParseException parseException){}
	        	
	            return contractEffectiveDate;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_NUM_OF_LIVES_ID.equals(columnID)) {
	            if (Constants.DEFINED_BENEFIT_ID.equals(tpaBobReportVO.getProductType())) {
	                return Constants.HYPHON_SYMBOL;
	            } else {
	                return String.valueOf(tpaBobReportVO.getNumOfLives());
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_ALLOCATED_ASSETS_ID.equals(columnID)) {
	            if (tpaBobReportVO.getAllocatedAssets() != null) {
	                return tpaBobReportVO.getAllocatedAssets().toString();
	            }
	            return Constants.SPACE_SYMBOL;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_LOAN_ASSETS_ID.equals(columnID)) {
	            if (tpaBobReportVO.getLoanAssets() != null) {
	                return tpaBobReportVO.getLoanAssets().toString();
	            }
	            return Constants.SPACE_SYMBOL;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_CASH_ACCOUNT_BALANCE_ID.equals(columnID)) {
	            if (tpaBobReportVO.getCashAccountBalance() != null) {
	                return tpaBobReportVO.getCashAccountBalance().toString();
	            }
	            return Constants.SPACE_SYMBOL;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PBA_ASSETS_ID.equals(columnID)) {
	            if (tpaBobReportVO.getPbaAssets() != null) {
	                return tpaBobReportVO.getPbaAssets().toString();
	            }
	            return Constants.SPACE_SYMBOL;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_TOTAL_ASSETS_ID.equals(columnID)) {
	            if (tpaBobReportVO.getTotalAssets() != null) {
	                return tpaBobReportVO.getTotalAssets().toString();
	            }
	            return Constants.SPACE_SYMBOL;
	        } 
	        else if (TPABlockOfBusinessReportData.COL_EZ_START_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getEzStart();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_EZ_INCREASE_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getEzIncrease();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_DIRECT_MAIL_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getDirectMail();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_GIFL_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getGifl();
	            }
	        } 	        
		else if (TPABlockOfBusinessReportData.COL_MANAGED_ACCOUNT_ID.equals(columnID)) {
			if (reportForm.getIsDefaultDateSelected()) {
				return tpaBobReportVO.getManagedAccountServiceTypeDesc();
			}
		}     
	        else if (TPABlockOfBusinessReportData.COL_SEND_SERVICE_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getSendService();
	            }
	        }
	        else if (TPABlockOfBusinessReportData.COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractJHPassiveTrusteeCode = tpaBobReportVO.getJohnHancockPassiveTrusteeCode();
	        		if(StringUtils.isNotEmpty(contractJHPassiveTrusteeCode) && !StringUtils.equals("I", StringUtils.trimToEmpty(contractJHPassiveTrusteeCode))){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractJHPassiveTrusteeCode))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        }else if (TPABlockOfBusinessReportData.COL_ONLINE_BENEFICIARY_DESIGNATION_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractOnlineBeneficiaryId = tpaBobReportVO.getOnlineBeneficiaryDesignation();
	        		
					if(null!=contractOnlineBeneficiaryId){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractOnlineBeneficiaryId))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        }
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        }else if (TPABlockOfBusinessReportData.COL_TPA_SIGNING_AUTHORITY_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractTPASign = tpaBobReportVO.getTpaSigningAuthority();
	        		
					if(null!=contractTPASign){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractTPASign))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        }else if (TPABlockOfBusinessReportData.COL_PAYROL_PATH_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractPayRollPath = tpaBobReportVO.getPayRollPath();
	        		
					if(null!=contractPayRollPath){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractPayRollPath))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
		}  else if (TPABlockOfBusinessReportData.COL_SFC_ID.equals(columnID)) {
			if (reportForm.getIsDefaultDateSelected()) {
				if (SmallPlanFeature.SFC.equals(tpaBobReportVO.getSmallPlanOptionCode())) {
					return Constants.YES;
				} else {
					return Constants.NO;
				}
			}
		} else if (TPABlockOfBusinessReportData.COL_PEP_ID.equals(columnID)) {
			if (reportForm.getIsDefaultDateSelected()) {
				if (SmallPlanFeature.PEP.equals(tpaBobReportVO.getSmallPlanOptionCode())) {
					return Constants.YES;
				} else {
					return Constants.NO;
				}
			}
		} else if (TPABlockOfBusinessReportData.COL_PAYROLL_FEEDBACK_SVC_ID.equals(columnID)
				&& (reportForm.getIsDefaultDateSelected())) {
			String contractPayrollFeedback = tpaBobReportVO.getPayrollFeedback();
			if (null != contractPayrollFeedback) {
				PayrollFeedbackServiceType payrollFeedbackServiceType = PayrollFeedbackServiceType
						.valueOf(StringUtils.trimToEmpty(contractPayrollFeedback));
				if (payrollFeedbackServiceType != null) {
					return payrollFeedbackServiceType.getDescription();
				}
			}
			return PayrollFeedbackServiceType.NS.getDescription();
		}else if (TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractPlanHighLights = tpaBobReportVO.getPlanHighlights();
	        		
					if(null!=contractPlanHighLights){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractPlanHighLights))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        }else if (TPABlockOfBusinessReportData.COL_PLAN_HIGHLIGHTS_REVIEWED_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractPlanHighLightsReviewed = tpaBobReportVO.getPlanHighlightsReviewed();
	        		
					if(null!=contractPlanHighLightsReviewed){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractPlanHighLightsReviewed))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        }else if (TPABlockOfBusinessReportData.COL_INSTALLMENTS_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	            	String contractInstallments = tpaBobReportVO.getInstallments();
	        		
					if(null!=contractInstallments){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contractInstallments))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
	        		}else{
	        			return Constants.NO;
	        		}
	            }
	        } else if (TPABlockOfBusinessReportData.COL_SYS_WITHDRAWALS_ID.equals(columnID)) {
	        	boolean sywCode= false;
	            if (reportForm.getIsDefaultDateSelected()) {
	            	try {
						sywCode=ContractServiceDelegate.getInstance().isSystematicWithdrawalFeatureON(tpaBobReportVO.getContractNumber());
					} catch (SystemException e) {					
						logger.error("TPABlockOfBusinessAction Systematic Withdrawal"+e.getMessage());
					}
	            	if(sywCode)
	            	{
	            	String contracSystematcWithdrawals = tpaBobReportVO.getSysWithdrawals();    		
					if(null!=contracSystematcWithdrawals){
	        			if(StringUtils.equals("Y", StringUtils.trimToEmpty(contracSystematcWithdrawals))) {
	        				return Constants.YES;
	        			}else {
	        				return Constants.NO;
	        			}
					}
	        		}else{
	        			return Constants.NO;
	        		}
	            }else{
	            	return Constants.SYW_NA;
	            }
	        }
	        
	        else if (TPABlockOfBusinessReportData.COL_PAM_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                String pamIndicators = tpaBobReportVO.getPam();
	                String pamValue = Constants.SPACE_SYMBOL;

	                if (!StringUtils.isBlank(pamIndicators)) {
	                    if (pamIndicators.contains(Constants.COMMA_SYMBOL)) {
	                        String[] pamIndicator = pamIndicators.split(Constants.COMMA_SYMBOL);

	                        if (pamIndicator != null) {
	                            for (String pamInd : pamIndicator) {
	                                if (Constants.PAM_ACT_ID.equalsIgnoreCase(pamInd.trim())) {
	                                    pamValue += Constants.PAM_ACT_VALUE;
	                                } else if (Constants.PAM_DSB_ID.equalsIgnoreCase(pamInd.trim())) {
	                                    pamValue += Constants.PAM_DSB_VALUE;
	                                } else if (Constants.PAM_RTD_ID.equalsIgnoreCase(pamInd.trim())) {
	                                    pamValue += Constants.PAM_RTD_VALUE;
	                                } else if (Constants.PAM_TRM_ID.equalsIgnoreCase(pamInd.trim())) {
	                                    pamValue += Constants.PAM_TRM_VALUE;
	                                }
	                                pamValue += Constants.COMMA_SYMBOL + Constants.SINGLE_SPACE_SYMBOL;
	                            }
	                            pamValue = StringUtils.removeEnd(pamValue, Constants.COMMA_SYMBOL
	                                    + Constants.SINGLE_SPACE_SYMBOL);
	                        }
	                    } else {
	                        String pamInd = pamIndicators.trim();
	                        if (Constants.PAM_ACT_ID.equalsIgnoreCase(pamInd.trim())) {
	                            pamValue += Constants.PAM_ACT_VALUE;
	                        } else if (Constants.PAM_DSB_ID.equalsIgnoreCase(pamInd.trim())) {
	                            pamValue += Constants.PAM_DSB_VALUE;
	                        } else if (Constants.PAM_RTD_ID.equalsIgnoreCase(pamInd.trim())) {
	                            pamValue += Constants.PAM_RTD_VALUE;
	                        } else if (Constants.PAM_TRM_ID.equalsIgnoreCase(pamInd.trim())) {
	                            pamValue += Constants.PAM_TRM_VALUE;
	                        }
	                    }
	                } else {
	                    pamValue = Constants.NONE;
	                }
	                return pamValue;
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_ONLINE_WITHDRAWALS_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getOnlineWithdrawals();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_VESTING_PERCENTAGE_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                String vestingPercentage = tpaBobReportVO.getVestingPercentage();

	                if (!StringUtils.isBlank(vestingPercentage)) {
	                    if (Constants.TPAP.equalsIgnoreCase(vestingPercentage.trim())) {
	                        return Constants.SUBMITTED_BY_TPA;
	                    } else {
	                        return Constants.CALCULATED_BY_JH;
	                    }
	                }
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_VESTING_ON_STATEMENTS_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getVestingOnStatements();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_PERMITTED_DISPARITY_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getPermittedDisparity();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_FSW_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                final String fswCofidCode = tpaBobReportVO.getFsw();
	                switch (FswStatus.of(fswCofidCode)) {
	                    case QUALIFIED: return "Yes";
	                    case NOT_QUALIFIED: return "No";
	                    case COFIDUCIARY:
	                    case NOT_ELIGIBLE:
	                        return "Not Eligible";
	                    default: throw new AssertionError("Unrecognized FSW status code, " + fswCofidCode);
	                }
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_DIO_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                StringBuffer dioInfo = new StringBuffer();
	                ArrayList<DefaultInvestmentFundVO> dioList = tpaBobReportVO.getDio();
	                if (dioList != null) {
	                    for (DefaultInvestmentFundVO dio : dioList) {
	                        dioInfo.append(dio.getFundName()).append(Constants.SINGLE_SPACE_SYMBOL)
	                                .append("(").append(dio.getPercentage()).append(Constants.PERCENT_SYMBOL)
	                                .append(")").append(Constants.SEMICOLON_SYMBOL)
	                                .append(Constants.SINGLE_SPACE_SYMBOL);
	                    }
	                }
	                return dioInfo.toString();
	            }
	        } 
	        else if (TPABlockOfBusinessReportData.COL_COW_ID.equals(columnID)) {
	            if (reportForm.getIsDefaultDateSelected()) {
	                return tpaBobReportVO.getCow();
	            }
	        }

	        return null;
	    }
	    
	    @Override
	    protected String getReportId() {
	        return TPABlockOfBusinessReportData.REPORT_ID;
	    }

	    @Override
	    protected String getReportName() {
	        return TPABlockOfBusinessReportData.REPORT_NAME;
	    }

	    /**
	     * This method will return the File Name of the CSV file.
	     * 
	     * The CSV file will be of the Format "TPABlockOfBusiness-mmddyyyy.csv".
	     */
	    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
	        return TPABlockOfBusinessReportData.REPORT_NAME
	                + Constants.HYPHON_SYMBOL
	                + DateRender.format(getAsOfDate((TPABlockOfBusinessForm) form),
	                        RenderConstants.MEDIUM_MDY_SLASHED).replace(Constants.SLASH_SYMBOL,
	                        Constants.SPACE_SYMBOL) + CSV_EXTENSION;
	    }

	    /**
	     * This method gives the as of date selected by the user.
	     * 
	     * @param reportForm - BlockOfBusinessForm object.
	     * @return - The as of date.
	     */
	    private Date getAsOfDate(TPABlockOfBusinessForm reportForm) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getAsOfDate().");
	        }
	        String asOfDateSelected = reportForm.getAsOfDateSelected();
	        List<Date> asOfDateList = null;
	        Date asOfDate = null;
	        // "asOfDateSelected" is null if the default asOfDate was not changed by user in the page.
	        if (StringUtils.isEmpty(asOfDateSelected)) {
	            try {
	                asOfDateList = TPABlockOfBusinessUtility.getMonthEndDates();
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
	     * This method has been overridden to save the Filtering criteria that has been created till
	     * now.
	     * 
	     * The filter criteria created is saved in the Form object, which will be later used to get the
	     * "filters used" value in CSV/PDF.
	     */
	    protected ReportCriteria getReportCriteria(String reportId, BaseReportForm form,
	            HttpServletRequest request) throws SystemException {
	        ReportCriteria reportCriteria = super.getReportCriteria(reportId, form, request);

	        ((TPABlockOfBusinessForm) form).setFilteringCriteriaSaved(reportCriteria);

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

	        TPABlockOfBusinessForm reportForm = (TPABlockOfBusinessForm) form;

	        UserProfile userProfile = (UserProfile) SessionHelper.getUserProfile(request);
	        if (userProfile == null) {
	            throw new SystemException("UserProfile is null");
	        }

	        addUserProfileRelatedFilterCriteria(userProfile, criteria, reportForm, request);

	        String dbSessionID = getFilterValue(TPABlockOfBusinessReportData.FILTER_DB_SESSION_ID,
	                reportForm, userProfile, request);
	        if (!StringUtils.isEmpty(dbSessionID)) {
	            addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_DB_SESSION_ID, Integer
	                    .valueOf(dbSessionID));
	        }

	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
	                getFilterValue(TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES,
	                        reportForm, userProfile, request));

	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_AS_OF_DATE, getFilterValue(
	                TPABlockOfBusinessReportData.FILTER_AS_OF_DATE, reportForm, userProfile, request));
	        
	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID,
	                    getFilterValue(TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID,
	                            reportForm, userProfile, request));

	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID,
	                getFilterValue(TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID,
	                        reportForm, userProfile, request));
	        
	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID,
	                getFilterValue(TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID,
	                        reportForm, userProfile, request));
	        
	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_US_OR_NY, getFilterValue(
	                TPABlockOfBusinessReportData.FILTER_US_OR_NY, reportForm, userProfile, request));
	        
	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID,
	                getFilterValue(TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID,
	                        reportForm, userProfile, request));
	        
	        addFilterCriteria(criteria, TPABlockOfBusinessReportData.CSV_DOWNLOAD_IND, getFilterValue(
	                TPABlockOfBusinessReportData.CSV_DOWNLOAD_IND, reportForm, userProfile, request));
	        
	    }

	    /**
	     * Sorting is done only based on the given sort field, Secondary sort column Contract Number. 
	     * 
	     */
	    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
	        
	        TPABlockOfBusinessForm tpaBobForm = (TPABlockOfBusinessForm) form;

	        String sortField = tpaBobForm.getSortField();
	        String sortDirection = tpaBobForm.getSortDirection();

	        criteria.insertSort(sortField, sortDirection);
	        if (!Constants.TPA_CONTRACT_NUMBER_SORT_FIELD.equals(sortField)) {
	            criteria.insertSort(Constants.TPA_CONTRACT_NUMBER_SORT_FIELD, ReportSort.ASC_DIRECTION);
	        }
	    }

	    /**
	     * Add userProfile specific filter criteria such as UserProfileID, userRole.
	     * 
	     * @param userProfile
	     * @param criteria
	     * @throws SystemException
	     */
	    private void addUserProfileRelatedFilterCriteria(UserProfile userProfile,
	            ReportCriteria criteria, TPABlockOfBusinessForm reportForm, HttpServletRequest request)
	            throws SystemException {

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> addUserProfileRelatedFilterCriteria()");
	        }
	        
	        if (userProfile.isInternalUser()) {
	            // Internal User Profile ID
	            String internalUserProfileID = getFilterValue(TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID,
	                    reportForm, userProfile, request);
	            if (internalUserProfileID != null) {
	                addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID, Long
	                        .valueOf(internalUserProfileID));
	            }
	            // Internal User Role
	            addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_USER_ROLE, getFilterValue(
	                    TPABlockOfBusinessReportData.FILTER_USER_ROLE, reportForm, userProfile, request));

	            // TPA User Profile ID
	            String tpauserProfileID = getFilterValue(TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID,
	                    reportForm, userProfile, request);
	            if (tpauserProfileID != null) {
	                addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID, Long
	                        .valueOf(tpauserProfileID));
	            }
	            // TPA User Role
	            addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, getFilterValue(
	                    TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE, reportForm, userProfile, request));
	        } else {
	            // TPA User Profile ID
	            String userProfileID = getFilterValue(TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID,
	                    reportForm, userProfile, request);
	            if (userProfileID != null) {
	                addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID, Long
	                        .valueOf(userProfileID));
	            }
	            // TPA User Role
	            addFilterCriteria(criteria, TPABlockOfBusinessReportData.FILTER_USER_ROLE, getFilterValue(
	                    TPABlockOfBusinessReportData.FILTER_USER_ROLE, reportForm, userProfile, request));
	        }
	        
	        if (logger.isDebugEnabled()) {
	            logger.debug("exit -> addUserProfileRelatedFilterCriteria()");
	        }
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
	    private String getFilterValue(String filterID, TPABlockOfBusinessForm reportForm,
	            UserProfile userProfile, HttpServletRequest request) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("inside getFilterValue()");
	        }
	        try {
	            if (TPABlockOfBusinessReportData.FILTER_USER_PROFILE_ID.equals(filterID)) {
	                return String.valueOf(userProfile.getPrincipal().getProfileId());
	            } else if (TPABlockOfBusinessReportData.FILTER_USER_ROLE.equals(filterID)) {
	                // If Internal user, return his user Role, else return 'TPA'
	                if (userProfile.isInternalUser()) {
	                    return userProfile.getRole().getRoleId();
	                } else {
	                    return Constants.TPA_ROLE;
	                }
	            } else if (TPABlockOfBusinessReportData.FILTER_MIMIC_USER_PROFILE_ID.equals(filterID)) {
	                // Only internal user will call use this Mimic function. Return the TPA UserProfile
	                // ID of the user on which the Internal User had clicked in "Search / Select TPA"
	                // page.
	                if (userProfile.getRole().isInternalUser()) {
	                    if (!StringUtils.isBlank(reportForm.getTpaUserIDSelected())) {
	                        return reportForm.getTpaUserIDSelected();
	                    }
	                }
	            } 
	            else if (TPABlockOfBusinessReportData.FILTER_MIMIC_USER_ROLE.equals(filterID)) {
	                // Only internal user will call use this Mimic function.
	                if (userProfile.getRole().isInternalUser()) {
	                    return Constants.TPA_ROLE;
	                }
	            }
	            else if (TPABlockOfBusinessReportData.FILTER_DB_SESSION_ID.equals(filterID)) {
	                Integer dbSessionID = getStoredProcSessionIDForAsOfDate(request, reportForm);
	                return dbSessionID == null ? null : dbSessionID.toString();
	            } 
	            else if (TPABlockOfBusinessReportData.FILTER_CONTRACT_STATUS_CODES.equals(filterID)) {
	                return TPABlockOfBusinessUtility.getContractStatus(reportForm.getCurrentTab());
	            } 
	            else if (TPABlockOfBusinessReportData.FILTER_AS_OF_DATE.equals(filterID)) {
	                Date reportAsOfDate = getAsOfDate(reportForm);
	                if (reportAsOfDate != null) {
	                    Long reportAsOfDateL = reportAsOfDate.getTime();
	                    return reportAsOfDateL.toString();
	                }
	                return null;
	            } 
	            else if (TPABlockOfBusinessReportData.FILTER_CONTRACT_NAME_ID.equals(filterID)) {
	                return reportForm.getContractName();
	            }
	            else if (TPABlockOfBusinessReportData.FILTER_CONTRACT_NUMBER_ID.equals(filterID)) {
	                if (!StringUtils.isBlank(reportForm.getContractNumber())) {
	                    if (StringUtils.isNumeric(reportForm.getContractNumber().trim())) {
	                        return reportForm.getContractNumber().trim();
	                    }
	                }
	            }
	            else if (TPABlockOfBusinessReportData.FILTER_FINANCIAL_REP_OR_ORG_NAME_ID.equals(filterID)) {
	                return reportForm.getFinancialRepNameOrOrgName();
	            }
	            else if (TPABlockOfBusinessReportData.FILTER_US_OR_NY.equals(filterID)) {
	                String siteMode = Environment.getInstance().getSiteLocation();
	                return Constants.COMPANY_NAME_NY.equalsIgnoreCase(siteMode) ? Constants.COMPANY_NAME_NY
	                        : Constants.COMPANY_NAME_US;
	            }
	            else if (TPABlockOfBusinessReportData.FILTER_CAR_NAME_ID.equals(filterID)) {
	                return reportForm.getCarName();
	            } else if (TPABlockOfBusinessReportData.CSV_DOWNLOAD_IND.equals(filterID)) {
	                if (DOWNLOAD_TASK.equals(getTask(request))) {
	                    return "Y";
	                }
	            }
	        } catch (NullPointerException ne) {
	            // Do Nothing.
	        }
	        return null;
	    }
	    
	    /**
	     * This method is used to set the session ID obtained from stored proc, into a Map.
	     * 
	     * @param reportForm - The Form.
	     * @param request - The HttpServlet Request.
	     */
	    @SuppressWarnings("unchecked")
	    private void setStoredProcSessionIDIntoMap(TPABlockOfBusinessForm reportForm,
	            HttpServletRequest request) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> setStoredProcSessionIDIntoSession().");
	        }

	        TPABlockOfBusinessReportData reportData = (TPABlockOfBusinessReportData) request
	                .getAttribute(Constants.REPORT_BEAN);
	        if (reportData == null) {
	            return;
	        }

	        Date asOfDate = getAsOfDate(reportForm);

	        // This variable will be used to store the session ID that is sent to Database.
	        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
	                .getAttribute(Constants.DB_SESSION_ID);
	        if (storedProcSessionIDMap == null) {
	            storedProcSessionIDMap = new HashMap<Date, Integer>();
	        }
	        storedProcSessionIDMap.put(asOfDate, reportData.getDbSessionID());

	        request.getSession(false).setAttribute(Constants.DB_SESSION_ID, storedProcSessionIDMap);

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
	            TPABlockOfBusinessForm reportForm) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getStoredProcSessionIDForAsOfDate().");
	        }

	        Date asOfDate = getAsOfDate(reportForm);
	        Map<Date, Integer> storedProcSessionIDMap = (Map<Date, Integer>) request.getSession(false)
	                .getAttribute(Constants.DB_SESSION_ID);

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
	     * This method is used to validate the report, post the report data has been created.
	     * 
	     * @throws SystemException
	     */
	    protected void validateReportData(ReportData report, BaseReportForm form,
	            HttpServletRequest request) throws SystemException {
	        
	        TPABlockOfBusinessReportData reportData = (TPABlockOfBusinessReportData) report;
	        TPABlockOfBusinessForm reportForm = (TPABlockOfBusinessForm) form;
	        
	        UserProfile userProfile = SessionHelper.getUserProfile(request);
	        
	        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
	        
	        /**
	         * #1. Show a Informational message to the user if the current filter criteria did not fetch
	         * any results.
	         */
	        if (reportData != null) {
	            if (reportData.getDetails() != null && reportData.getDetails().size() == 0) {
	                ArrayList<LabelValueBean> filtersUsed = getFiltersUsed(userProfile, reportForm,
	                        request);
	                if (filtersUsed != null && !filtersUsed.isEmpty()) {
	                    GenericException exception = new GenericExceptionWithContentType(
	                            ContentConstants.TPA_BOB_NO_CONTRACTS_FOR_FILTER_ENTERED,
	                            ContentTypeManager.instance().MISCELLANEOUS);
	                    infoMessages.add(exception);
	                }
	            }
	        }
	        
	        if (!infoMessages.isEmpty()) {
	            setMessagesInRequest(request, infoMessages, Constants.TPA_BOB_INFO_MSG);
	        }
	    }
	    
	    
	    /**
	     * This method will give a ArrayList of filters used.
	     * 
	     * @param userProfile - UserProfile object.
	     * @param reportForm - The Form.
	     * @param request - HttpServletRequest object.
	     * @return - An ArrayList of Filters used, and the Filter Value.
	     * @throws SystemException
	     */
	    private ArrayList<LabelValueBean> getFiltersUsed(UserProfile userProfile,
	            TPABlockOfBusinessForm reportForm, HttpServletRequest request) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getFiltersUsed().");
	        }

	        ArrayList<LabelValueBean> filtersUsed = new ArrayList<LabelValueBean>();

	        for (String filterID : TPABlockOfBusinessUtility.getAllFilters()) {
	            String filterValue = getFilterValue(filterID, reportForm, userProfile, request);
	            if (!StringUtils.isEmpty(filterValue)) {
	                String filterTitle = TPABlockOfBusinessUtility.getFiltersIdToTitleMap().get(
	                        filterID);
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
 
	    @RequestMapping(value ="/tpaBlockOfBusiness/", method =  {RequestMethod.POST,RequestMethod.GET}) 
	    public String doDefault (@Valid @ModelAttribute("tpaBlockOfBusinessForm")   TPABlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
	    	String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
	    	if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
	    	
				 forward =super.doDefault( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	  
	    @RequestMapping(value ="/tpaBlockOfBusiness/" ,params={"task=filter"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	    public String doFilter (@Valid @ModelAttribute("tpaBlockOfBusinessForm")   TPABlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
	    	String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
	    	if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
				 forward =super.doFilter( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	   @RequestMapping(value ="/tpaBlockOfBusiness/", params={"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	    public String doPage (@Valid @ModelAttribute("tpaBlockOfBusinessForm") TPABlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
			String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
	    	if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
				 forward =super.doPage( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	   @RequestMapping(value ="/tpaBlockOfBusiness/", params={"task=sort"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	   public String doSort (@Valid @ModelAttribute("tpaBlockOfBusinessForm") TPABlockOfBusinessForm actionForm,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
			String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
	   	if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
			 forward =super.doSort( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	   @RequestMapping(value ="/tpaBlockOfBusiness/",
	   params={"task=download"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	   public String doDownload (@Valid @ModelAttribute("tpaBlockOfBusinessForm") TPABlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
			String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
				 forward =super.doDownload( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	   
	   @RequestMapping(value ="/tpaBlockOfBusiness/" ,params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	   public String doDownloadAll (@Valid @ModelAttribute("tpaBlockOfBusinessForm") TPABlockOfBusinessForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	   throws IOException,ServletException, SystemException {
			String forward = preExecute(actionForm, request, response);
	    	if(StringUtils.isNotBlank(forward)) {
	    		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	    	}
		   if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
				 forward =super.doDownloadAll( actionForm, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}  
        
    /*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#Validate(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax.servlet.http.HttpServletRequest)
	 */
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
