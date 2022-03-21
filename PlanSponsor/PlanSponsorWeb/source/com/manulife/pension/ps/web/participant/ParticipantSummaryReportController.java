package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;				//CL 110234
import java.util.Iterator;
import java.util.List;					//CL 110234
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.LIADisplayHelper;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryTotals;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This action handles the creation of the ParticipantSummaryReport. It will
 * also create the participant summary download.
 *
 * @author Tony Tomasone
 * @see ReportController for details
 */

@Controller
@RequestMapping(value="/participant")
@SessionAttributes({"participantSummaryReportForm"})

public final class ParticipantSummaryReportController extends ReportController {
	
	@ModelAttribute("participantSummaryReportForm") 
	public  ParticipantSummaryReportForm  populateForm() 
	{
		return new  ParticipantSummaryReportForm ();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{ 
		
	forwards.put("input","/participant/participantSummaryReport.jsp");
	forwards.put("default","/participant/participantSummaryReport.jsp");
	forwards.put("sort","/participant/participantSummaryReport.jsp");
	forwards.put("filter","/participant/participantSummaryReport.jsp");
	forwards.put("page","/participant/participantSummaryReport.jsp");
	forwards.put("print","/participant/participantSummaryReport.jsp");
	}

	protected static final String DOWNLOAD_COLUMN_HEADING_GENERAL = "Total count of participants,";

	protected static final String DOWNLOAD_COLUMN_HEADING_TOTAL = "Total employee assets,Total employer assets,Total assets";

	protected static final String DOWNLOAD_COLUMN_HEADING_AVERAGE = "Average employee assets,Average employer assets,Average assets";

	private static final String ASTERISK = "*";

	private static final String AMOUNT_FORMAT = "##0.00";
	
	//Accounts Summary Tab navigation from other Tabs - Start
	private static final String ANOTHER_TAB = "anotherTab";
	private static final String FROM_PAGE = "fromPage";
	//Accounts Summary Tab navigation from other Tabs - End

	/**
	 * Constructor for ParticipantSummaryReportAction
	 */
	public ParticipantSummaryReportController() {
		super(ParticipantSummaryReportController.class);
	}

	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

        //Fix CL#103591 starts
		Collection errors = new ArrayList();
        ParticipantSummaryReportForm theForm = (ParticipantSummaryReportForm) reportForm;
        String namePhrase = theForm.getNamePhrase();
        if (namePhrase != null && namePhrase.trim().length() > 0) {
            NameRule.getLastNameInstance().validate(
                    ParticipantSummaryReportForm.FIELD_LAST_NAME, errors,
                    namePhrase);
        }
        if (!theForm.getSsn().isEmpty()) {
            SsnRule.getInstance().validate(
                    ParticipantSummaryReportForm.FIELD_SSN, errors,
                    theForm.getSsn());
        }
        //Fix CL#103591 ends
		ParticipantSummaryReportForm form = (ParticipantSummaryReportForm) reportForm;
		Contract currentContract = getUserProfile(request).getCurrentContract();
		
		// Only show loans values if the loan feature is enabled for the current contract
		boolean showLoans = form.getHasLoansFeature();
		//To check the contract level gateway status
		boolean showGateway = form.getHasContractGatewayInd();
        //Gateway Phase 1 -- end
		
		//Only show MA if the Managed Account is enabled
		boolean showManagedAccount = form.getHasManagedAccountInd();
		
		// Only show Roth values if there are Roth money types enabled for the current contract
		boolean showRoth = form.getHasRothFeature();
		
        // Indicator to determine if the division column is shown
        boolean showDivision = form.getShowDivision();

		ParticipantSummaryTotals totals = ((ParticipantSummaryReportData) report)
				.getParticipantSummaryTotals();
				
		Date asOfDate = new Date(Long.parseLong(form.getAsOfDate()));
				
		ProtectedStringBuffer buffer = new ProtectedStringBuffer();

        buffer.append("Contract").append(COMMA).append(
			currentContract.getContractNumber()).append(COMMA).append(
			currentContract.getCompanyName()).append(LINE_BREAK);
		
		// section one of the CSV report
		buffer.append("As of,").append(DateRender.formatByPattern(asOfDate, "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
		if(CollectionUtils.isEmpty(errors)) {//Fix CL#103591
	        buffer.append(DOWNLOAD_COLUMN_HEADING_GENERAL).append(totals.getTotalParticipants()).append(LINE_BREAK);		    
		} else {//Fix CL#103591 starts
		    buffer.append(DOWNLOAD_COLUMN_HEADING_GENERAL).append("0").append(LINE_BREAK);
		}//Fix CL#103591 ends

    	if(CollectionUtils.isEmpty(errors)) {//Fix CL#103591
			// section two of the CSV report
			buffer.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL);
			if (showLoans) buffer.append(",Total balance of outstanding loans");
			buffer.append(LINE_BREAK);
			buffer.append(NumberRender.formatByPattern(new Double(totals.getEmployeeAssetsTotal()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(new Double(totals.getEmployerAssetsTotal()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(new Double(totals.getTotalAssets()),
	                "0.00", "##0.00"));
			if (showLoans) 
				buffer.append(COMMA).append(NumberRender.formatByPattern(new Double(totals.getOutstandingLoans()),
	                "0.00", "##0.00"));
			buffer.append(LINE_BREAK);

		// section three of the CSV report
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING_AVERAGE);
		if (showLoans) buffer.append(",Average balance of outstanding loans");
		buffer.append(LINE_BREAK);
		buffer.append(NumberRender.formatByPattern(new Double(totals.getEmployeeAssetsAverage()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
		buffer.append(NumberRender.formatByPattern(new Double(totals.getEmployerAssetsAverage()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
		buffer.append(NumberRender.formatByPattern(new Double(totals.getTotalAssetsAverage()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
		if (showLoans)
				buffer.append(COMMA).append(NumberRender.formatByPattern(new Double(totals.getOutstandingLoansAverage()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		// filters used
        //CL 110234 Begin
		if (form.isAsOfDateCurrent()) {
            if (form.getEmploymentStatus() != null) {
            	buffer.append("Employment Status,").append(getEmpStatusDescription(form.getEmploymentStatus())).append(LINE_BREAK);
            }else{
            	buffer.append("Employment Status,").append("All").append(LINE_BREAK);
            }
            if (form.getStatus() != null) {
            	buffer.append("Contribution Status,").append(form.getStatus()).append(LINE_BREAK);
            }else{
            	buffer.append("Contribution Status,").append("All").append(LINE_BREAK);
            	}
			}
	}
        //CL 110234 End
		if (!StringUtils.isEmpty(form.getNamePhrase()) ) {
			buffer.append("Last name starts with,").append(form.getNamePhrase()).append(LINE_BREAK);
		}
		if (!form.getSsn().isEmpty()) {
			if (form.getSsn().toString().length() == 9) {
				buffer.append("SSN is,").append(SSNRender.format(form.getSsn(), null)).append(LINE_BREAK);
			} else {
				buffer.append("SSN is,").append(form.getSsn().toString()).append(LINE_BREAK);
			}	
		}
        if (!StringUtils.isEmpty(form.getDivision())) {
            buffer.append("Division starts with,").append(form.getDivision()).append(LINE_BREAK);
        }

		// section four of the CSV report
		buffer.append(LINE_BREAK);
		buffer.append("Last name,First name,SSN,Age,");
		if (form.isAsOfDateCurrent()) {
			buffer.append("Employment status,Employment status effective date,Contribution status,Default date of Birth,Investment instruction type,");		//CL 110234
			if (showRoth) {
				buffer.append("Roth money,");
			}
		}
		buffer.append("Employee assets,Employer assets,Total assets");
			
		if (showLoans) buffer.append(",Outstanding loans");
		// To display gateway column header in download report 
		if (form.isAsOfDateCurrent() && showGateway)
		{
			buffer.append(",Guar. Income feature");
		}
		// To display MA column header in download report
		if (form.isAsOfDateCurrent() && showManagedAccount) {
			buffer.append(",Mgd. Account Feature");
		}
		if (showDivision) {
			buffer.append(",Division");
		}
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        UserProfile user = getUserProfile(request);
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
		buffer.append(LINE_BREAK);
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			ParticipantSummaryDetails theItem = 
				(ParticipantSummaryDetails) iterator.next();
				
			buffer.append(escapeField(theItem.getLastName())).append(COMMA);
			buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
			buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);
			//buffer.append(SSNRender.format(theItem.getSsn(), null)).append(COMMA);
			buffer.append(theItem.getAge()==0 ? "" : String.valueOf(theItem.getAge())).append(COMMA);
			String investInstTypeDes="";
			if (form.isAsOfDateCurrent()) {
			//CL 110234	Begin
				if(theItem.getEmploymentStatus()!= null){
					buffer.append(theItem.getEmploymentStatusDescription()).append(COMMA);
				}else{
					buffer.append(COMMA);
				}
				if(theItem.getEffectiveDate()!= null){
					buffer.append(theItem.getEffectiveDate()).append(COMMA);
				}else{
					buffer.append(COMMA);
				}
				//CL 110234 End
				buffer.append(theItem.getStatus()).append(COMMA);
				buffer.append(theItem.getDefaultBirthDate()).append(COMMA);
				if(theItem.getInvestmentInstructionType() != null)
	            {
	            	if("TR".equalsIgnoreCase(theItem.getInvestmentInstructionType()))
	            	{
	            		investInstTypeDes = "TR – Instructions were provided by Trustee - Mapped";
	            	}
	            	else if("PA".equalsIgnoreCase(theItem.getInvestmentInstructionType()))
	            	{
	            		investInstTypeDes = "PA – Participant Provided";
	            	}
	            	else if("PR".equalsIgnoreCase(theItem.getInvestmentInstructionType()))
	            	{
	            		investInstTypeDes = "PR – Instructions prorated - participant instructions incomplete / incorrect";
	            	}
	            	else if("DF".equalsIgnoreCase(theItem.getInvestmentInstructionType()))
	            	{
	            		investInstTypeDes = "DF – Default investment option was used";
	            	}    
	            	else if("MA".equalsIgnoreCase(theItem.getInvestmentInstructionType()))
	            	{
	            		investInstTypeDes = "MA - Managed Accounts";
	            	}
	            }
				buffer.append(investInstTypeDes).append(COMMA);
				if (showRoth) {
					buffer.append(theItem.getRothInd()).append(COMMA);
				}
			}	
			
			if (theItem.getEmployeeAssets() < 0.0) buffer.append(ASTERISK);
			else buffer.append(NumberRender.formatByPattern(new Double(theItem.getEmployeeAssets()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			buffer.append(COMMA);
			
			if (theItem.getEmployerAssets() < 0.0) buffer.append(ASTERISK);
			else buffer.append(NumberRender.formatByPattern(new Double(theItem.getEmployerAssets()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			buffer.append(COMMA);
			
			if (theItem.getTotalAssets() < 0.0) buffer.append(ASTERISK);
			else buffer.append(NumberRender.formatByPattern(new Double(theItem.getTotalAssets()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			
			if (showLoans)
					buffer.append(COMMA).append(NumberRender.formatByPattern(new Double(theItem.getOutstandingLoans()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			
			if (form.isAsOfDateCurrent() && showGateway)
			{	
				// If LIA is turned on append LIA indicator
				if (theItem.isShowLIADetailsSection()) {
					buffer.append(COMMA).append(theItem.getParticipantGatewayInd()).append("/").append(CommonConstants.LIA_IND_TEXT);
				} else {
					buffer.append(COMMA).append(theItem.getParticipantGatewayInd());
				}
			}
			// changes related to managed Account
			if (form.isAsOfDateCurrent() && showManagedAccount)
			{	
				buffer.append(COMMA).append(theItem.getManagedAccountStatusInd());
				
			}
            if (showDivision && theItem.getDivision() != null)
            	buffer.append(COMMA).append(escapeField(theItem.getDivision().trim()));
            buffer.append(COMMA);

			
			buffer.append(LINE_BREAK);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}
		return buffer.toString().getBytes();
	}
	//CL 110234 Begin
	/**
	 * This method will return the employment status description
	 */	
	private String getEmpStatusDescription(String status){
		
		HashMap<String, String> statusDescMap = new HashMap<String, String> ();
		statusDescMap.put("All", "All");
		statusDescMap.put("A", "Active");
		statusDescMap.put("C", "Cancelled");
		statusDescMap.put("D", "Deceased");
		statusDescMap.put("P", "Disabled");
		statusDescMap.put("R", "Retired");
		statusDescMap.put("T", "Terminated");
		
		return statusDescMap.get(status);
		
	}
	//CL 110234 End

	/**
	 * @see ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantSummaryReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantSummaryReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantSummaryReportData.REPORT_NAME;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract
				.getContractNumber()));

		ParticipantSummaryReportForm psform = (ParticipantSummaryReportForm) form;

		if (!StringUtils.isEmpty(psform.getAsOfDate())) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_2,
					psform.getAsOfDate());
		}

		if (!StringUtils.isEmpty(psform.getStatus())
				&& psform.isAsOfDateCurrent()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_3,
					((ParticipantSummaryReportForm) form).getStatus());
		}

		if (!StringUtils.isEmpty(psform.getNamePhrase())) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_4,
					psform.getNamePhrase());
		}

		if (!psform.getSsn().isEmpty()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_5,
					psform.getSsn().toString());
		}
		//adding gateway filter to report criteria
		if (psform.isAsOfDateCurrent() && psform.getGatewayChecked()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_6,
					"gateway");
		}
		
		// adding managed account filter to report criteria
		if (psform.isAsOfDateCurrent() && psform.getManagedAccountChecked()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_14, "managed");
		}

        if (!StringUtils.isEmpty(psform.getDivision())) {
            criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_7, 
            		psform.getDivision());
        }
        //CL 110234 Begin
        if (!StringUtils.isEmpty(psform.getEmploymentStatus())) {
            criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_10, 
            		psform.getEmploymentStatus());
        }
        //CL 110234 End

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}
	
	 
	 protected String doCommon(
			 BaseReportForm form, HttpServletRequest request,
	            HttpServletResponse response) throws
	            SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		ParticipantSummaryReportForm actionForm = (ParticipantSummaryReportForm) form;
		if (actionForm.getResetGateway() != null) {
			if (actionForm.getResetGateway().equals("false"))
				actionForm.resetForm();
		}
		if (actionForm.getResetManagedAccount() != null) {
			if("false".equals(actionForm.getResetManagedAccount())){
				actionForm.resetForm();
			}
		}

		String forward = super.doCommon( actionForm, request,
				response);

		UserProfile userProfile = getUserProfile(request);
		
		if (DOWNLOAD_TASK.equals(getTask(request))) {
			FunctionalLogger.INSTANCE.log("Download Accounts (Contract)", userProfile, getClass(), getMethodName( actionForm, request));
		} else {
			FunctionalLogger.INSTANCE.log("Accounts (Contract) page", userProfile, getClass(), getMethodName( actionForm, request));
		}
		
		actionForm.setBaseAsOfDate(String.valueOf(userProfile.getCurrentContract()
				.getContractDates().getAsOfDate().getTime()));
		if (StringUtils.isEmpty(actionForm.getAsOfDate()))
			actionForm.setAsOfDate(actionForm.getBaseAsOfDate());

		if (!actionForm.isAsOfDateCurrent()) {
			// reset the status filter
			actionForm.setStatus(null);
			actionForm.resetForm();
		}

		ParticipantSummaryReportData report = (ParticipantSummaryReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		Contract currentContract = userProfile.getCurrentContract();

		// PPR41 
		actionForm.setHasLoansFeature(currentContract.isLoanFeature() || 
				report.getParticipantSummaryTotals().getOutstandingLoans() != 0.0);
		actionForm.setHasRothFeature(currentContract.hasRothNoExpiryCheck());
		actionForm.setShowDivision(currentContract.hasSpecialSortCategoryInd());
		
		//Gateway Phase 1 -- start
		//contract level gateway check and reset the gateway checkbox status
		actionForm.setHasContractGatewayInd(userProfile.getContractProfile()
				.getContract().getHasContractGatewayInd());
		//Gateway Phase 1 -- end
		
		// Managed Account check
		ManagedAccountServiceFeatureLite managedAccountService = ContractServiceDelegate.getInstance()
				.getContractSelectedManagedAccountServiceLite(currentContract.getContractNumber());
		actionForm.setHasManagedAccountInd(managedAccountService != null);
		
		//CL 110234 Begin
        // populate list of employee statuses for the dropdown
        List employeeStatusList = ParticipantServiceDelegate.getInstance().getEmployeeStatusesListWithoutC(Constants.PS_APPLICATION_ID);
        actionForm.setStatusList(employeeStatusList);
        //CL 110234 End
        populatePartcipantsLIADetails(report, currentContract.getContractNumber(), actionForm);
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}
	
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		
		//Accounts Summary Tab navigation from other Tabs - Start		
		ParticipantSummaryReportForm form = (ParticipantSummaryReportForm) reportForm;
		HttpSession session =request.getSession(false);
		String fromPage = request.getParameter(FROM_PAGE);
		if (fromPage != null && fromPage.equalsIgnoreCase(ANOTHER_TAB)){
			populateSessionAttributesToForm(form, session);
		} else {
			populateFormAttributesToSession(form, session);	
		}		
		//Accounts Summary Tab navigation from other Tabs - End
	}
	
		@RequestMapping(value ="/participantSummary",  method =  {RequestMethod.GET}) 
	    public String doDefault(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
		 
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		 String forward=super.doDefault(actionForm, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		@RequestMapping(value ="/participantSummary",params={"task=default"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	    public String doDefaultReset(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
		 
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		 String forward=super.doDefault(actionForm, request, response);
		  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
	 
		@RequestMapping(value ="/participantSummary" ,params={"task=filter"}  , method =  {RequestMethod.GET,RequestMethod.POST}) 
		public String doFilter(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
		       return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
			}
			
	       String forward=super.doFilter(actionForm, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		
		
		@RequestMapping(value ="/participantSummary", params={"task=page"}, method =  {RequestMethod.GET}) 
		public String doPage(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
		       }
			}
	       String forward=super.doPage( actionForm, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
		
		
		@RequestMapping(value ="/participantSummary", params={"task=sort"}  , method =  {RequestMethod.GET}) 
		public String doSort (@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			
			if(bindingResult.hasErrors()){
				String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("default");//if input forward not //available, provided default
		       }
			}
	       String forward=super.doSort( actionForm, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
		}
	 
		
		@RequestMapping(value = "/participantSummary", params = {"task=print"}, method = {RequestMethod.GET})
		public String doPrint(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm reportForm,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
				}
			}
			String forward = super.doPrint(reportForm, request, response);
			request.setAttribute("printFriendly", true);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		
		@RequestMapping(value = "/participantSummary", params = {"task=download"}, method = {RequestMethod.GET})
		public String doDownload(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm reportForm,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
				}
			}
			String forward = super.doDownload(reportForm, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		
		@RequestMapping(value = "/participantSummary", params = {"task=downloadAll"}, method = {RequestMethod.GET})
		public String doDownloadAll(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm reportForm,
				BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException, SystemException {
			if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("default");// if
				}
			}
			String forward = super.doDownloadAll(reportForm, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	/**
	 * Method to populate the Form attributes from Session
	 * 
	 * @param reportForm
	 * @param session
	 */
	private void populateSessionAttributesToForm(ParticipantSummaryReportForm form, 
			HttpSession session) {

		String tempValue = null;

		// for Division field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_7));
		form.setDivision(tempValue);

		// for SSN field
		Ssn strSSN = (Ssn) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_5);
		Ssn ssnTemp = new Ssn();
		if ( strSSN != null ){
			ssnTemp.setDigits(0, strSSN.getDigits(0));
			ssnTemp.setDigits(1, strSSN.getDigits(1));
			ssnTemp.setDigits(2, strSSN.getDigits(2));
	
			// Assign the individual SSN entries into respective input fields
			form.setSsnOne(strSSN.getDigits(0));
			form.setSsnTwo(strSSN.getDigits(1));
			form.setSsnThree(strSSN.getDigits(2));
		}

		// for status field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_3));
		form.setStatus(tempValue);

		// for Employment Status field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_10));
		form.setEmploymentStatus(tempValue);

		// for LastName field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_4));
		form.setNamePhrase(tempValue);

		// for AsOfDate field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(ParticipantSummaryReportData.FILTER_FIELD_2));
		form.setAsOfDate(tempValue);
	}
	
	/**
	 * Method to populate the Form attributes to Session
	 * 
	 * @param form
	 * @param session
	 */
	private void populateFormAttributesToSession(ParticipantSummaryReportForm form, 
			HttpSession session) {
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_7, form
				.getDivision());
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_5, form
				.getSsn());
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_3, form
				.getStatus());
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_10, form
				.getEmploymentStatus());
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_4, form
				.getNamePhrase());
		session.setAttribute(ParticipantSummaryReportData.FILTER_FIELD_2, form
				.getAsOfDate());
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired 
	ParticipantSummaryReportValidator participantSummaryReportValidator;

	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value ="/participantSummary",  method =  {RequestMethod.POST}) 
   
	 public String execute(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// do a refresh so that there's no problem using tha back button
			ControllerForward forward = new ControllerForward("refresh", "/do"
					+ new UrlPathHelper().getPathWithinApplication(request) + "?task=" + getTask(request), true);
			
			if (logger.isDebugEnabled()) {
				logger.debug("forward = " + forward);
			}
			return forward.getPath();
		}
		
		return null;
	}

	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
	
	/**
	 * Method to populate the particpant's LIA details to report.
	 * 
	 * @param report
	 * @param contractNumber
	 * @throws SystemException
	 */
	private void populatePartcipantsLIADetails(
			ParticipantSummaryReportData report, int contractNumber, ParticipantSummaryReportForm form)
			throws SystemException {
		
		List<ParticipantSummaryDetails> participantSummaryDetails = (List<ParticipantSummaryDetails>) report
				.getDetails();
		List<ParticipantSummaryDetails> liaParticipantSummaryDetails = new ArrayList<ParticipantSummaryDetails>();
		List<ParticipantSummaryDetails> nonLiaParticipantSummaryDetails = new ArrayList<ParticipantSummaryDetails>();
		List<ParticipantSummaryDetails> sortedParticipantSummaryDetails = new ArrayList<ParticipantSummaryDetails>();
		Map<String, LifeIncomeAmountDetailsVO> liaDetails = ContractServiceDelegate
				.getInstance().getParticpantLIADetailsForContract(
						contractNumber);
		for (ParticipantSummaryDetails participantSummary : participantSummaryDetails) {
			LifeIncomeAmountDetailsVO participantLiadetail = liaDetails
					.get(participantSummary.getProfileId());
			participantSummary.setLiaDetails(participantLiadetail);
			if (participantLiadetail != null
					&& LIADisplayHelper
							.isShowLIADetailsSection(participantLiadetail
									.getAnniversaryDate())) {
				participantSummary.setShowLIADetailsSection(true);
				liaParticipantSummaryDetails.add(participantSummary);

			} else {
				nonLiaParticipantSummaryDetails.add(participantSummary);
			}
		}
		// should be re-sort based on selection of 'Guar. Income feature' feild
		if ("participantGatewayInd".equals(form.getSortField()) && !liaParticipantSummaryDetails.isEmpty()) {
			if ("desc".equals(form.getSortDirection())) {
				sortedParticipantSummaryDetails
						.addAll(liaParticipantSummaryDetails);
				sortedParticipantSummaryDetails
						.addAll(nonLiaParticipantSummaryDetails);
			} else {
				sortedParticipantSummaryDetails
						.addAll(nonLiaParticipantSummaryDetails);
				sortedParticipantSummaryDetails
						.addAll(liaParticipantSummaryDetails);
			}
			report.setDetails(sortedParticipantSummaryDetails);
		}
	}
	
	/*
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#137697.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org
	 * .apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax
	 * .servlet.http.HttpServletRequest)
	 */


	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	@InitBinder  
	public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(participantSummaryReportValidator);
	}
}
