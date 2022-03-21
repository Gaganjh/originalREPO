package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummary;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

 /* This action handles the creation of the ParticipantEnrollmentSummaryReport. It will
 * also create the participant enrollment summary download.
 */
@Controller
@RequestMapping(value ="/participant/participantEnrollDownload/")
@SessionAttributes({"participantEnrollmentSummaryReportForm"})

 public final class ParticipantEnrollmentSummaryReportController extends ReportController {
	
	@ModelAttribute("participantEnrollmentSummaryReportForm") 
	public  ParticipantEnrollmentSummaryReportForm populateForm()
	{
		return new  ParticipantEnrollmentSummaryReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantEnrollmentSummaryReport.jsp"); 
		forwards.put("default","/participant/participantEnrollmentSummaryReport.jsp");
		forwards.put("sort","/participant/participantEnrollmentSummaryReport.jsp"); 
		forwards.put("filter","/participant/participantEnrollmentSummaryReport.jsp");
		forwards.put("page","/participant/participantEnrollmentSummaryReport.jsp");
		forwards.put("print","/participant/participantEnrollmentSummaryReport.jsp");
		}

	
 	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
 	public static final String BEN_COL_HEADINGS = "Beneficiary Type,Relationship to Participant,Beneficiary Last Name,Beneficiary First Name,% of Share,Beneficiary Date of Birth,Beneficiary Address Line 1,Beneficiary Address Line 2,Beneficiary City,Beneficiary State of Residence,Beneficiary Country,Beneficiary ZIP";

 	public static final String DETAILS_FULL_COLS_INTERNET = "Last name, First name, SSN,Date of Birth,Payroll number,Division,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String DETAILS_HIDE_DIV_INTERNET ="Last name, First name, SSN,Date of Birth,Payroll number,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String DETAILS_HIDE_PAYROLL_INTERNET = "Last name, First name, SSN,Date of Birth,Division,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String DETAILS_HIDE_BOTH_INTERNET = "Last name, First name, SSN,Date of Birth,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String DETAILS_FULL_COLS = "Last name, First name, SSN,Date of Birth,Payroll number,Division,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status";
 	public static final String DETAILS_HIDE_DIV = "Last name, First name, SSN,Date of Birth,Payroll number,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status";
 	public static final String DETAILS_HIDE_PAYROLL = "Last name, First name, SSN,Date of Birth,Division,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status";
 	public static final String DETAILS_HIDE_BOTH = "Last name, First name, SSN,Date of Birth,Residence State,Enrollment Method,Enrollment Processing Date,Normal Retirement Date,Contribution Status";
 	public static final String SUMMARY_FULL_COLS_INTERNET = "Last name, First name, SSN,Date of Birth,Payroll number,Division,Enrollment Method,Enrollment Processing Date,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String SUMMARY_HIDE_DIV_INTERNET = "Last name, First name, SSN,Date of Birth,Payroll number,Enrollment Method,Enrollment Processing Date,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String SUMMARY_HIDE_PAYROLL_INTERNET = "Last name, First name, SSN,Date of Birth,Division,Enrollment Method,Enrollment Processing Date,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String SUMMARY_HIDE_BOTH_INTERNET = "Last name, First name, SSN,Date of Birth,Enrollment Method,Enrollment Processing Date,Eligible to Defer,401(k) deferral ($),401(k) deferral (%)";
 	public static final String SUMMARY_FULL_COLS = "Last name, First name, SSN,Date of Birth,Payroll number, Division,Enrollment Method,Enrollment Processing Date";
 	public static final String SUMMARY_HIDE_DIV = "Last name, First name, SSN,Date of Birth,Payroll number,Enrollment Method,Enrollment Processing Date";
 	public static final String SUMMARY_HIDE_PAYROLL = "Last name, First name, SSN,Date of Birth,Division,Enrollment Method,Enrollment Processing Date";
 	public static final String SUMMARY_HIDE_BOTH = "Last name, First name, SSN,Date of Birth,Enrollment Method,Enrollment Processing Date";
 	public static final String DEFERRAL_COMMENT = "Deferral Comment";
 	public static final String ROTH_DEFERRALS = "Roth deferral ($),Roth deferral (%)";

    
 	/**
	 * Constructor for ParticipantSummaryReportAction
	 */
	public ParticipantEnrollmentSummaryReportController() {
		super(ParticipantEnrollmentSummaryReportController.class);
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
 		ParticipantEnrollmentSummaryReportForm form = (ParticipantEnrollmentSummaryReportForm) reportForm;

		//Display number of Participants with enrollments
		int numberParticipantsWithEnrollments = ((ParticipantEnrollmentSummaryReportData) report)
				.getNumberParticipantsWithEnrollments();

		String typeOfPageLayout = form.getTypeOfPageLayout();
		String printType = form.getPrintType();
		
		int rothCount = ((ParticipantEnrollmentSummaryReportData) report)
		.getNumberOfRothParticipants();


		//get dates for display
		Date fromDate = new Date();
		Date toDate = new Date();

		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
		
		try {
       		fromDate = format.parse(form.getFromDate());
       	} catch(ParseException pe) {
           	 if (logger.isDebugEnabled()) {
        		logger.debug("ParseException in fromDate getDownloadData() ParticipantEnrollmentSummaryReportAction:", pe);
        	}
       	}    

		try {
       		toDate = format.parse(form.getToDate());
       	} catch(ParseException pe) {
           	 if (logger.isDebugEnabled()) {
        		logger.debug("ParseException in toDate getDownloadData() ParticipantEnrollmentSummaryReportAction:", pe);
        	}
       	}   
		
				
		StringBuffer buffer = new StringBuffer();
		
        Contract currentContract = getUserProfile(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);
		
		// section one of the CSV report
		buffer.append("From date,To date");
		buffer.append(LINE_BREAK);
		buffer.append(DateRender.formatByPattern(fromDate, "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA).append(DateRender.formatByPattern(toDate, "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Number participants enrolled during the date range ").append(numberParticipantsWithEnrollments).append(LINE_BREAK);

		// section two of the CSV report
		buffer.append(LINE_BREAK);
		
		
				
		//column headings - first determine if there are Web Enrollments or not
		//then determine if hide division, if hide payroll, and if hide both division and payroll
		if (printType.equals("allDetails")){
			if(form.getHasInternetEnrollments() || form.getHasAutoEnrollments()){
				if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)){
						buffer.append(DETAILS_FULL_COLS_INTERNET);
						if (rothCount > 0){
							buffer.append("," + ROTH_DEFERRALS);
						}
						buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_DIVISION)){
						buffer.append(DETAILS_HIDE_DIV_INTERNET);
						if (rothCount > 0){
							buffer.append("," + ROTH_DEFERRALS);
						}
						buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_PAYROLL)){
						buffer.append(DETAILS_HIDE_PAYROLL_INTERNET);
						if (rothCount > 0){
							buffer.append("," + ROTH_DEFERRALS);
						}
						buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){	
						buffer.append(DETAILS_HIDE_BOTH_INTERNET);
						if (rothCount > 0){
							buffer.append("," + ROTH_DEFERRALS);
						}
						buffer.append("," + DEFERRAL_COMMENT);
				}

				
			} else {
				if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)){
					buffer.append(DETAILS_FULL_COLS);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_DIVISION)){
					buffer.append(DETAILS_HIDE_DIV);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_PAYROLL)){
					buffer.append(DETAILS_HIDE_PAYROLL);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){	
					buffer.append(DETAILS_HIDE_BOTH);	
				}					
			}
		} else {//summary report -- NO BENEFICIARY DETAILS	
			if(form.getHasInternetEnrollments() || form.getHasAutoEnrollments()){
				if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)){
					buffer.append(SUMMARY_FULL_COLS_INTERNET);
					if (rothCount > 0){
						buffer.append("," + ROTH_DEFERRALS);
					}
					buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_DIVISION)){
					buffer.append(SUMMARY_HIDE_DIV_INTERNET);
					if (rothCount > 0){
						buffer.append("," + ROTH_DEFERRALS);
					}
					buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_PAYROLL)){
					buffer.append(SUMMARY_HIDE_PAYROLL_INTERNET);
					if (rothCount > 0){
						buffer.append("," + ROTH_DEFERRALS);
					}
					buffer.append("," + DEFERRAL_COMMENT);
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){	
					buffer.append(SUMMARY_HIDE_BOTH_INTERNET);
					if (rothCount > 0){
						buffer.append("," + ROTH_DEFERRALS);
					}
					buffer.append("," + DEFERRAL_COMMENT);
				}
			} else {
				if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)){
					buffer.append(SUMMARY_FULL_COLS);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_DIVISION)){
					buffer.append(SUMMARY_HIDE_DIV);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_PAYROLL)){
					buffer.append(SUMMARY_HIDE_PAYROLL);	
				} else if (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){	
					buffer.append(SUMMARY_HIDE_BOTH);	
				}
			}	
		}
		buffer.append(LINE_BREAK);
		
		//loop through details
		Iterator iterator = report.getDetails().iterator();
			while (iterator.hasNext()) {
				ParticipantEnrollmentSummary theItem = (ParticipantEnrollmentSummary) iterator.next();
				buffer.append(escapeField(theItem.getLastName()).trim()).append(COMMA);	
				buffer.append(escapeField(theItem.getFirstName()).trim()).append(COMMA);
				buffer.append(theItem.getSsn()).append(COMMA);
				buffer.append(DateRender.formatByPattern(theItem.getBirthDate(), Constants.NOT_ENTERED, RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
				
				//if full columns, hide division column or not hide both - show the payroll column
				if (!typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){
					if ((typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)) || (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_DIVISION))) {
						buffer.append(theItem.getEmployerDesignatedID()).append(COMMA);
					}
				}
				//if full columns, hide payroll column or not hide both - show the Division column
				if (!typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_BOTH)){
					if ((typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.FULL_COLUMNS)) || (typeOfPageLayout.equals(ParticipantEnrollmentSummaryReportForm.HIDE_PAYROLL))) {
						buffer.append(theItem.getOrganizationUnitID()).append(COMMA);
					}
				}
				if (printType.equals("allDetails")){//Residence State
					if (theItem.getResidenceStateCode() == null || theItem.getResidenceStateCode().trim().equals("")){
						buffer.append(Constants.NOT_ENTERED).append(COMMA);
					}else {
						buffer.append(theItem.getResidenceStateCode()).append(COMMA);
					}	
				}

				buffer.append(theItem.getEnrollmentMethod()).append(COMMA);						
				buffer.append(DateRender.formatByPattern(theItem.getEnrollmentProcessedDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);					

				
				if (printType.equals("allDetails")){//Normal Retirement Date
					buffer.append(DateRender.formatByPattern(theItem.getNormalRetirementDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);					
				}

				if (printType.equals("allDetails")){//Contribution Status
					buffer.append(theItem.getContributionStatus()).append(COMMA);
				}
				
				
				if(form.getHasInternetEnrollments() == true || form.getHasAutoEnrollments() == true){
                    if(!Constants.INTERNET.equalsIgnoreCase(theItem.getEnrollmentMethod()) &&
                       !Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) &&
                       !Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod())) {
                        buffer.append(Constants.NA).append(COMMA);
                    } else if(theItem.getEligibleToDeferInd() != null && !Constants.NA.equalsIgnoreCase(theItem.getEligibleToDeferInd())) {
                        buffer.append(theItem.getEligibleToDeferInd()).append(COMMA);
                    } else if(Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) ||
                            Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod())) {
                        buffer.append(Constants.ELIGIBLE_TO_DEFER).append(COMMA);
                    } else {
                        buffer.append(Constants.NA).append(COMMA); 
                    }					
				}	

				
				if(form.getHasInternetEnrollments() == true || form.getHasAutoEnrollments() == true){

					if (theItem.hasTradDeferral()){
						
						if (theItem.getContributionAmt() > 0){
							buffer.append(theItem.getContributionAmt());
						} else {
							buffer.append("0");
						}
						buffer.append(COMMA);
						if (theItem.getContributionPct() > 0){
							buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPct()),
							"", "##"));
						} else {
							buffer.append(NumberRender.formatByPattern(new Double("0"),
							"", "##"));
						}									
					} else {
						buffer.append(NumberRender.formatByPattern(new Double("0"),
								"", "##"));
						buffer.append(COMMA);
						buffer.append(NumberRender.formatByPattern(new Double("0"),
						"", "##"));								
					}
					
					if (rothCount > 0){
						if (theItem.hasRothDeferral()){
							buffer.append(COMMA);
							if (theItem.getContributionAmtRoth() > 0){
								buffer.append(theItem.getContributionAmtRoth());
							}else {
								buffer.append("0");
							}
							buffer.append(COMMA);
							if (theItem.getContributionPctRoth() > 0 ){
								buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPctRoth()),"", "##"));	
							} else {
								buffer.append(NumberRender.formatByPattern(new Double("0"),
								"", "##"));
							}
						} else {
							buffer.append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double("0"),
							"", "##"));
							buffer.append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double("0"),
							"", "##"));
						}
					}						
					
					buffer.append(COMMA);
                    if((Constants.NA.equalsIgnoreCase(theItem.getDeferralComment()) ||
                            Constants.NOT_ENTERED.equalsIgnoreCase(theItem.getDeferralComment())) && 
                       theItem.getEnrollmentMethod() != null && 
                       (Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) || 
                               Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()))) {
                        // The comment is going to be AUTO_DEFAULTED for 'Auto' or 'Was Auto Enroll'
                        buffer.append(Constants.AUTO_DEFAULTED).append(COMMA);
                    } else {
                        buffer.append(theItem.getDeferralComment()).append(COMMA);
                    }
                    
				}		
					
				buffer.append(LINE_BREAK);
			}


		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}
 	/**
	 * @see ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}
 	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantEnrollmentSummaryReportData.DEFAULT_SORT;
	}
 	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantEnrollmentSummaryReportData.REPORT_ID;
	}
 	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantEnrollmentSummaryReportData.REPORT_NAME;
	}
 	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}
 		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_1, Integer.toString(currentContract.getContractNumber()));

		
		ParticipantEnrollmentSummaryReportForm psform = (ParticipantEnrollmentSummaryReportForm) form;


		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

	
		//get the from Date
		if(!StringUtils.isEmpty(psform.getFromDate())) {
			try {
        		Date fromDate = format.parse(psform.getFromDate());
        		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_2,fromDate);
        		
        	} catch(ParseException pe) {
           	 	if (logger.isDebugEnabled()) {
        			logger.debug("ParseException in fromDate getDownloadData() ParticipantEnrollmentSummaryReportAction:", pe);
        		}
        	}        	
		}

		//get the to Date
		if(!StringUtils.isEmpty(psform.getToDate())) {
			try {
        		Date toDate = format.parse(psform.getToDate());
        		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_3,toDate);
        	} catch(ParseException pe) {
           	 	if (logger.isDebugEnabled()) {
        			logger.debug("ParseException in toDate getDownloadData() ParticipantEnrollmentReportAction:", pe);
        		}
        	}   			
		}


		if(!StringUtils.isEmpty(psform.getNamePhrase())) {
	        criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_4,
	            psform.getNamePhrase());
		}
 		if(!psform.getSsn().isEmpty()) {
	        criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_5,
	            psform.getSsn().toString());
		}
 		if (logger.isDebugEnabled()) {
			logger.debug("criteria= "+criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}
 
	public String doCommon( ParticipantEnrollmentSummaryReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	doValidate(form, request);
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
 		String forward = super.doCommon( form, request,response);
		
		
		//get the Report object		
		ParticipantEnrollmentSummaryReportData report = (ParticipantEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);		
		//get the report details		
		ArrayList details = new ArrayList(report.getDetails());
		
		//as long as there are details, determine if columns are hidden (payroll and division)
		//determine if there are internet enrollments and beneficiaries
		if (details.size() > 0) {
			if (report.getNonEmptyOrganizationUnitCount() > 0 && getUserProfile(request).getCurrentContract().hasSpecialSortCategoryInd()) {
				form.setHasDivisionFeature(true);
			} else {
				form.setHasDivisionFeature(false);
			}	
		
			if (report.getNonEmptyPayrollCount() > 0) {
				form.setHasPayrollNumberFeature(true);
			} else {
				form.setHasPayrollNumberFeature(false);
			}

			if (report.getNumberInternetEnrollments() > 0) {
				form.setHasInternetEnrollments(true);
			} else {
				form.setHasInternetEnrollments(false);
			}
            
            if (report.getNumberAutoEnrollments() > 0) {
                form.setHasAutoEnrollments(true);
            } else {
                form.setHasAutoEnrollments(false);
            }   

		}		
		
		if (form.getIsSearch()){
			form.setIsInitialSearch(false);
		}	
		
		request.getSession().setAttribute(Constants.REPORT_BEAN, report);	
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}



	
	protected void populateReportForm( BaseReportForm reportForm, HttpServletRequest request) 
	{
 		super.populateReportForm( reportForm, request);
 		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}
 	/**
	 * @see PsController#execute( ActionForm,BindingResult, HttpServletRequest, HttpServletResponse)
	 */

	@RequestMapping(value = "/participantEnrollDownload/", method = { RequestMethod.POST})
	public String execute(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();	
		}
	
	@RequestMapping(value = "/participantEnrollDownload/", method = { RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doDefault(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantEnrollDownload/", params = { "task=filter" },method = { RequestMethod.GET})
	public String doFilter(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doFilter(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantEnrollDownload/",params = { "task=sort" }, method = { RequestMethod.GET})
	public String doSort(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doSort(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantEnrollDownload/",params = { "task=page" }, method = { RequestMethod.GET})
	public String doPage(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doPage(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantEnrollDownload/",params = { "task=print" }, method = { RequestMethod.GET})
	public String doPrint(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doPrint(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantEnrollDownload/",params = { "task=download" }, method = { RequestMethod.GET})
	public String doDownload(@Valid @ModelAttribute("participantEnrollmentSummaryReportForm") ParticipantEnrollmentSummaryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			populateReportForm( form, request);
			ParticipantEnrollmentSummaryReportData reportData = new ParticipantEnrollmentSummaryReportData(null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);			
			return forwards.get("input");// if input forward not
											// //available, provided default
		}
	  
				String forward=doDownload(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	private String escapeField(String field)
	{
        if(field == null) {
            return "";
        }
        
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
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@Autowired
	private ParticipantEnrollmentSummaryReportActionValidator participantEnrollmentSummaryReportActionValidator;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(participantEnrollmentSummaryReportActionValidator);
	}
 }