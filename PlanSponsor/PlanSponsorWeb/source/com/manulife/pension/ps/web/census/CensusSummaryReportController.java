package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

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
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusInfoFilterCriteriaHelper;
import com.manulife.pension.ps.web.census.util.CensusLookups;
import com.manulife.pension.ps.web.census.util.CensusSummaryUtils;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.census.util.FilterCriteriaVo;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action handles the creation of the CensusSummaryReport. It will also
 * create the census summary download.
 *
 * @author Diana Macean
 * @see ReportController for details
 */
@Controller
@RequestMapping( value = "/census")	
@SessionAttributes({"censusSummaryReportForm"})

public final class CensusSummaryReportController extends ReportController {
	@ModelAttribute("censusSummaryReportForm") 
	public CensusSummaryReportForm populateForm()
	{
		return new CensusSummaryReportForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{ 
		forwards.put("input","/census/censusSummaryReport.jsp");
		forwards.put("default","/census/censusSummaryReport.jsp");
		forwards.put("sort","/census/censusSummaryReport.jsp");
		forwards.put("filter","/census/censusSummaryReport.jsp");
		forwards.put("page","/census/censusSummaryReport.jsp");
		forwards.put("print","/census/censusSummaryReport.jsp");
		forwards.put("reset","/census/censusSummaryReport.jsp");
	}

    protected static final int SSN_LENGTH = 9;
    protected static final String DOWNLOAD_COLUMN_HEADING = "cens.h10,Cont#,SSN#,FirstName,LastName,Initial,NamePrefix,EEID#,Address1,Address2,City,State,ZipCode,Country," +
    "StateRes,ERProvEmail,Division,BirthDate,HireDate,EmplStat,EmplStatDate,EligInd,EligDate,OptOutInd,YTDHrs,PlanYTDComp,YTDHrsWkCompDt," +
    "BaseSalary,BfTxDefPct,DesigRothPct,BfTxFltDoDef,DesigRothAmt";
    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

    private static final String FromContractHomeParamName = "fromContractHome";
    private static final String TRANSACTION_CODE="cens.d";

	/**
	 * Constructor for CensusSummaryReportAction.
	 */
	public CensusSummaryReportController() {
		super(CensusSummaryReportController.class);
	}

    /**
     * @see com.manulife.pension.ps.web.report.ReportController#getPageSize(javax.servlet.http.HttpServletRequest)
     */
    protected int getPageSize(HttpServletRequest request) {
        UserProfile profile = getUserProfile(request);
        return profile.getPreferences()
                .getInt(UserPreferenceKeys.REPORT_PAGE_SIZE,
                        super.getPageSize(request));
    }

    /**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
     * @param reportForm BaseReportForm
     * @param report ReportData
     * @param request HttpServletRequest
     * @return byte[]
	 */
	protected byte[] getDownloadData(final BaseReportForm reportForm,
			final ReportData report, final HttpServletRequest request) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
		
		final StopWatch stopWatch = new StopWatch();
		try {
			if (logger.isInfoEnabled()) {
				logger.info("getDownloadData - starting timer.");
			}
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		CensusSummaryReportForm form = (CensusSummaryReportForm) reportForm;
        UserProfile user = getUserProfile(request);
        UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(user.getPrincipal());
        Contract currentContract = user.getCurrentContract();
        
        // find the contract sort code
        String sortCode = currentContract.getParticipantSortOptionCode();

        String todayDate = DateRender.formatByPattern(
                Calendar.getInstance().getTime(),
                "", RenderConstants.MEDIUM_MDY_SLASHED);

        boolean isECEnabled = false;
        try{
            
            ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
            	.getInstance().getContractServiceFeature(currentContract.getContractNumber(),
                ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
            isECEnabled =   ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();
        }catch (ApplicationException ae) {
            throw new SystemException(ae.getMessage());
        }
        
        
        
        StringBuffer buffer = new StringBuffer();

        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }

        // heading and records
        buffer.append(DOWNLOAD_COLUMN_HEADING);

        Iterator iterator = report.getDetails().iterator();
        while (iterator.hasNext()) {
            buffer.append(LINE_BREAK);
            CensusSummaryDetails theItem = (CensusSummaryDetails) iterator.next();
            buffer.append(TRANSACTION_CODE).append(COMMA);
            buffer.append(currentContract.getContractNumber()).append(COMMA);
           //SSE S024 determine wheather the ssn should be masked on the csv report
            buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);
            buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
            buffer.append(escapeField(theItem.getLastName())).append(COMMA);

            if (theItem.getMiddleInitial() != null)
                buffer.append(escapeField(theItem.getMiddleInitial()));
            buffer.append(COMMA);

            if (theItem.getNamePrefix() != null)
                buffer.append(theItem.getNamePrefix());
            buffer.append(COMMA);

            if (theItem.getEmployeeNumber() != null && theItem.getEmployeeNumber().trim().length() > 0) {
                buffer.append(StringUtils.leftPad(theItem.getEmployeeNumber().trim(), 9, "0"));
            }
            buffer.append(COMMA);

            if (theItem.getAddressLine1() != null)
                buffer.append(escapeField(theItem.getAddressLine1().trim()));
            buffer.append(COMMA);

            if (theItem.getAddressLine2() != null)
                buffer.append(escapeField(theItem.getAddressLine2().trim()));
            buffer.append(COMMA);

            if (theItem.getCity() != null)
                buffer.append(escapeField(theItem.getCity().trim()));
            buffer.append(COMMA);

            if (theItem.getStateCode() != null)
                buffer.append(escapeField(theItem.getStateCode().trim()));
            buffer.append(COMMA);

            if (theItem.getZipCode() != null) {
                String zipCode = StringUtils.trim(theItem.getZipCode());
                if (zipCode.length() > 0) {
                	buffer.append(zipCode.toUpperCase());
                }
            }
            buffer.append(COMMA);

            if (theItem.getCountry() != null)
                buffer.append(escapeField(theItem.getCountry().trim()));
            buffer.append(COMMA);

            if (theItem.getStateOfResidence() != null)
                buffer.append(escapeField(theItem.getStateOfResidence().trim()));
            buffer.append(COMMA);

            if (theItem.getEmployeeProvidedEmail() != null)
                buffer.append(escapeField(theItem.getEmployeeProvidedEmail().trim()));
            buffer.append(COMMA);

            if (theItem.getDivision() != null)
                buffer.append(escapeField(theItem.getDivision().trim()));
            buffer.append(COMMA);

            buffer.append(DateRender.formatByPattern(theItem.getBirthDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
            buffer.append(DateRender.formatByPattern(theItem.getHireDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            if (theItem.getStatus() != null)
                buffer.append(escapeField(theItem.getStatus().trim()));
            buffer.append(COMMA);

            buffer.append(DateRender.formatByPattern(theItem.getEmployeeStatusDate(), "",
                                RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            if (theItem.getEligibleToDeferInd() != null)
                buffer.append(escapeField(theItem.getEligibleToDeferInd().trim()));
            buffer.append(COMMA);
            
            if("Y".equalsIgnoreCase(theItem.getProvidedEligibilityDateInd() )){
            	buffer.append(DateRender.formatByPattern(theItem.getEligibilityDate(), "",
                    RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
            }else{
            	buffer.append("").append(COMMA);
            }

            if (theItem.getOptOut() != null)
                buffer.append(escapeField(theItem.getOptOut().trim()));
            buffer.append(COMMA);

            if (theItem.getPlanYTDHoursWorked() != null)
                buffer.append(escapeField(NumberRender.formatByType(theItem.getPlanYTDHoursWorked(), "", RenderConstants.INTEGER_TYPE)));
            buffer.append(COMMA);
            
            buffer.append(escapeField(CensusSummaryUtils.getMaskedValue(theItem.getPlanYTDCompensation(), theItem, user, userInfo, false))).append(COMMA);
            buffer.append(DateRender.formatByPattern(theItem.getPlanYTDHoursWorkedEffDate(), "",
                    RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);

            buffer.append(escapeField(CensusSummaryUtils.getMaskedValue(theItem.getAnnualBaseSalary(), theItem, user, userInfo, false))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByPattern(theItem.getBeforeTaxDeferralPercentage(), "", "###.###", 3, BigDecimal.ROUND_HALF_DOWN))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByPattern(theItem.getDesigRothDeferralPercentage(), "", "###.###", 3, BigDecimal.ROUND_HALF_DOWN))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByType(theItem.getBeforeTaxDeferralAmount(), "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
            buffer.append(escapeField(NumberRender.formatByType(theItem.getDesigRothDeferralAmount(), "", RenderConstants.CURRENCY_TYPE, false))).append(COMMA);
        }
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"getDownloadData - stoping timer - time duration [")
						.append(stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see ReportController#getDefaultSortDirection()
     * @return String
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getDefaultSort()
     * @return String
	 */
	protected String getDefaultSort() {
		return CensusSummaryReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
     * @return String
	 */
	protected String getReportId() {
		return CensusSummaryReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
     *
     * @return String
	 */
	protected String getReportName() {
        return CensusSummaryReportData.REPORT_NAME;
	}

    /**
     * Given a report ID, returns the downloaded CSV file name.
     *
     * @param request
     *
     * @return The file name used for the downloaded CSV.
     */
    protected String getFileName(HttpServletRequest request) {
        // defaults to .csv extension
        String dateString = null;
        synchronized (DATE_FORMATTER) {
            dateString = DATE_FORMATTER.format(new Date());
        }
        return "Census_Summary_Report_for_" + getUserProfile(request).getCurrentContract().getContractNumber() +
            "_for_" + dateString + CSV_EXTENSION;
    }

	/**
     *
	 * @see ReportController#populateReportCriteria(ReportCriteria,
     *                      BaseReportForm, HttpServletRequest)
     *
     * @param criteria ReportCriteria
     * @param form BaseReportForm
     * @param request HttpServletRequest
	 */
	protected void populateReportCriteria(final ReportCriteria criteria,
			final BaseReportForm form, final HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

        String task = getTask(request);
        
        // for download task do not process warnings
        if (task.equals(DOWNLOAD_TASK)) {
            criteria.setReportId(CensusSummaryReportData.TEMPLATE_REPORT_ID);
        }
        
		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract
				.getContractNumber()));

		CensusSummaryReportForm psform = (CensusSummaryReportForm) form;

		FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
		
		if(filterCriteriaVo == null ){
			filterCriteriaVo = new FilterCriteriaVo();
		}
		
		// If the task is default then reset the page no and sort details that
		// are cached in eligibility tab and deferral tab.
		if (task.equals(DEFAULT_TASK)) {
			filterCriteriaVo.clearDeferralSortDetails();
			filterCriteriaVo.clearEligibilitySortDetails();
		}
		
		// Populate the filter criterias
		CensusInfoFilterCriteriaHelper.populateCensusSummaryTabFilterCriteria(task, filterCriteriaVo, psform, criteria);
		
		// set filterCriteriaVo back to session
        SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
        
        // if external user, don't display Cancelled employees
        criteria.setExternalUser(userProfile.getRole().isExternalUser());

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}

    /**
     * @param mapping ActionMapping
     * @param reportForm BaseReportForm
     * @param request HttpServletRequest
     */
	protected void populateReportForm(
			final BaseReportForm reportForm, final HttpServletRequest request) {
		super.populateReportForm(reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

    /**
     * Populate sort criteria in the criteria object using the given FORM.
     * Default sort:
     * - by last name, first name, middle initial
     *
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            if (!form.getSortField().equals(getDefaultSort())) {
                criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
            }
        }
    }

	/**
	 * Validate the input form. The search field must not be empty.
	 *
	 
     *
     * @param mapping ActionMapping
     * @param form BaseReportForm
     * @param request HttpServletRequest
     * @return Collection
     */

	@Autowired 
	private CensusSummaryReportValidator censusSummaryReportValidator;

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     *
     * @param mapping ActionMapping
     * @param form BaseReportForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
	 */
	
	@RequestMapping(value ="/censusSummary",  method =  {RequestMethod.GET}) 
	public String doDefualt(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = null;
		
		if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		forward = doCommon(form, request, response);
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
        // check for selected access
        if (userProfile.isSelectedAccess()) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        }

        // check if contract is discontinued
        if (userProfile.getCurrentContract().isDiscontinued()) {
        	return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
            //return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
        }

        // Check if the source is from contract home
		// if it is, then reset all the search criteria except the name
		if (StringUtils.equalsIgnoreCase("true", request
				.getParameter(FromContractHomeParamName))) {
			((CensusSummaryReportForm) form)
					.resetSearchParameterFromContractHome();
		}

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			String mappedPath = new UrlPathHelper().getPathWithinApplication(request);
			// do a refresh so that there's no problem using tha back button
			 forward = new UrlPathHelper().getPathWithinApplication(request);
			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward;
		}
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}

   /**
    * @see ReportController#doCommon(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
    *
    * @param mapping ActionMapping
    * @param form BaseReportForm
    * @param request HttpServletRequest
    * @param response HttpServletResponse
    * @return ActionForward
    */ 

	 
	public String doCommon(
            final BaseReportForm reportForm, final HttpServletRequest request,
            final HttpServletResponse response)
    throws SystemException
    {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doCommon");
        }
        
        final StopWatch stopWatch = new StopWatch();
		try {
			if (logger.isInfoEnabled()) {
				logger.info("doCommon - starting timer.");
			}
			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        String forward = super.doCommon( reportForm, request,
                response);
        CensusSummaryReportForm form = (CensusSummaryReportForm) reportForm;

        // set permission flag for editing
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        
        if (DOWNLOAD_TASK.equals(getTask(request))) {
            
            FunctionalLogger.INSTANCE.log("Download census report", userProfile, getClass(), getMethodName( reportForm, request));
            
        } else {
            
            FunctionalLogger.INSTANCE.log("Census Summary page", request, getClass(), getMethodName( reportForm, request));
            
        }
        
        form.setAllowedToEdit(userProfile.isAllowedUpdateCensusData()
				&& !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract().getStatus()));

        // set permission flag for adding a new employee
        form.setAllowedToAdd( userProfile.isAllowedUpdateCensusData() &&
                              !Contract.STATUS_CONTRACT_DISCONTINUED.equals(userProfile.getCurrentContract().getStatus())
                            );

        int contractId = userProfile.getCurrentContract().getContractNumber();

        // set permission flag for eligibility tab
        boolean isEnabled = userProfile.isInternalUser() || CensusUtils.isAutoEnrollmentEnabled(contractId);
        form.setAllowedToAccessEligibTab(isEnabled);

               
        // set permission flag for deferral tab
        boolean allowedToAccessDeferrals = DeferralUtils.isAllowedToAccessDeferrals(userProfile);
        form.setAllowedToAccessDeferralTab(allowedToAccessDeferrals);

        // set permission flag for How To ... auto enrollment
        form.setAllowedToAutoEnrollment(CensusUtils.isAutoEnrollmentEnabled(contractId));

        // set permission flag for download census report
        form.setAllowedToDownload(
        	(userProfile.isInternalUser() && userProfile.isAllowedUpdateCensusData()) ||
            (userProfile.getRole().isExternalUser()));

        // set permission flag for vesting tab
        form.setAllowedToAccessVestingTab(
                CensusUtils.isVestingEnabled(contractId) &&
                !userProfile.getCurrentContract().isDefinedBenefitContract() /*
                TODO &&
                userProfile.getCurrentContract().isContractAllocated()*/);

        // populate list of employee statuses for the dropdown
        List employeeStatusList = null;
        // if external user, do not display Cancelled status in the dropdown
        if (userProfile.isInternalUser()) {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatuses();
        } else {
            employeeStatusList = CensusLookups.getInstance().getEmploymentStatusesWithoutC();
        }
        form.setStatusList(employeeStatusList);

        // populate list of segments for the dropdown
        form.setSegmentList(CensusLookups.getInstance().getSegments());
        //Get the CSF OBD value and Set the Census form
        boolean isOBDSEnabled = false;
        try {
        	
        	ContractServiceFeature csf = ContractServiceDelegate.getInstance().getContractServiceFeature(currentContract.getContractNumber(),
        									ServiceFeatureConstants.ONLINE_BENEFICIARY_DESIGNATION);
        	if(csf!=null){
        		isOBDSEnabled = ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();
        	}
        	
        } catch(ApplicationException ae) {
        	throw new SystemException(ae.getMessage());
        }
        
        // set permission flag for download beneficiary report.
        form.setAllowedToDownloadBeneficiaryReport(isOBDSEnabled);
        
		try {
			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuffer(
						"doCommon - stoping timer - time duration [").append(
						stopWatch.toString()).append("]").toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doCommon");
        }
        return forward;
    }
    
    /**
	 * This method is called when reset button is clicked
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
     * @throws ServletException 
     * @throws IOException 
	 */
	  @RequestMapping(value = "/censusSummary/", params={"task=reset"}, method =  {RequestMethod.POST}) 
	public String doReset(@Valid @ModelAttribute("censusSummaryReportForm") BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		  
				if (logger.isDebugEnabled()) {
					logger.debug("entry -> doReset");
				}
				
				FilterCriteriaVo filterCriteriaVo = SessionHelper.getFilterCriteriaVO(request);
				
				//Reset the session object for remebering filter criteria
				if(filterCriteriaVo != null){
					filterCriteriaVo = new FilterCriteriaVo();
				}
				
				SessionHelper.setFilterCriteriaVO(request, filterCriteriaVo);
				
				//Reset the form bean
				super.resetForm( reportForm, request);

				String forward = doCommon( reportForm, request, response);

				if (logger.isDebugEnabled()) {
					logger.debug("exit <- doReset");
				}
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	

    private String escapeField(String field)
    {
        if(field.indexOf(",") != -1 )
        {
            StringBuffer newField = new StringBuffer();
            newField = newField.append("\"").append(field).append("\"");
            return newField.toString();
        }
        else
        {
            return field;
        }
    }
    
    @RequestMapping(value = "/censusSummary/", params = {"task=filter"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		
    	if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/censusSummary/", params = {"task=page"}, method = {RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = super.doPage(form, request, response);
		if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/censusSummary/", params = {"task=sort"}, method = {RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = super.doSort(form, request, response);
		if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/censusSummary/", params = {"task=download"}, method = {RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = super.doDownload(form, request, response);
		if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}

	@RequestMapping(value = "/censusSummary/", params = {"task=downloadAll"}, method = {RequestMethod.GET})
	public String doDownloadAll(@Valid @ModelAttribute("censusSummaryReportForm") CensusSummaryReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward = super.doDownloadAll(form, request, response);
		if(bindingResult.hasErrors()){
			populateReportForm( form, request);
			CensusSummaryReportData reportData = new CensusSummaryReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward);
	}
	
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(censusSummaryReportValidator);
	}
	
}
