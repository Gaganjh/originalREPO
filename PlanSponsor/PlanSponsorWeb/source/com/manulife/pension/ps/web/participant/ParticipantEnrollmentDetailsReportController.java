package com.manulife.pension.ps.web.participant;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummary;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
/**
 * This action handles the creation of the ParticipantEnrollmentDetailsReport. It will
 * also create the participant enrollment details download.
 *
 */
@Controller
@RequestMapping(value="/participant")
@SessionAttributes({"participantEnrollmentDetailsReportForm"})
public final class ParticipantEnrollmentDetailsReportController extends ReportController {
 	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
 	private static final String FORMAT_PATTERN = "###.###";
 	
 	@ModelAttribute("participantEnrollmentDetailsReportForm") 
	public  ParticipantEnrollmentDetailsReportForm populateForm()
	{
		return new  ParticipantEnrollmentDetailsReportForm();
		}	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantEnrollmentDetailsReport.jsp");
		forwards.put("default","/participant/participantEnrollmentDetailsReport.jsp");
		forwards.put("sort","/participant/participantEnrollmentDetailsReport.jsp");
		forwards.put("filter","/participant/participantEnrollmentDetailsReport.jsp");
		forwards.put("page","/participant/participantEnrollmentDetailsReport.jsp");
		forwards.put("print","/participant/participantEnrollmentDetailsReport.jsp");
	}
        
	/**
	 * Constructor for ParticipantSummaryReportAction
	 */
	public ParticipantEnrollmentDetailsReportController() {
		super(ParticipantEnrollmentDetailsReportController.class);
	}
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},  method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDefault(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},params={"task=sort"},  method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSort(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},params={"task=filter"},  method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},params={"task=page"},  method =  {RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPage(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},params={"task=print"},   method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrint(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ={"/participantEnrollmentDetails","/partEnrollDetailDownload"},params={"task=download"},   method =  {RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("participantEnrollmentDetailsReportForm") ParticipantEnrollmentDetailsReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
	              return forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownload(actionForm, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,HttpServletRequest request) {

 		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}
 		
 		ParticipantEnrollmentSummaryReportData summaryReport = (ParticipantEnrollmentSummaryReportData)request.getSession().getAttribute(Constants.REPORT_BEAN);

		StringBuffer buffer = new StringBuffer();
		
        Contract currentContract = getUserProfile(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);
		
		buffer.append(LINE_BREAK);
		
		//loop through details
		Iterator iterator = summaryReport.getDetails().iterator();
		while (iterator.hasNext()) {
				ParticipantEnrollmentSummary theItem = (ParticipantEnrollmentSummary) iterator.next();
				buffer.append("Participant Last Name").append(COMMA).append(theItem.getLastName()).append(LINE_BREAK);
				buffer.append("Participant First Name").append(COMMA).append(theItem.getFirstName()).append(LINE_BREAK);
				buffer.append("SSN").append(COMMA).append(theItem.getSsn()).append(LINE_BREAK);							
	
				buffer.append("Date of Birth")
					  .append(COMMA)
					  .append(theItem.getBirthDate() == null 
							  ? "Not entered" 
							  : DateRender.formatByPattern(theItem.getBirthDate(), "", RenderConstants.MEDIUM_MDY_SLASHED))
					  .append(LINE_BREAK);
				
				if (theItem.getResidenceStateCode() == null || theItem.getResidenceStateCode().trim().equals("")){	
					buffer.append("Residence state").append(COMMA).append(Constants.NOT_ENTERED).append(LINE_BREAK);
				}else {
					buffer.append("Residence state").append(COMMA).append(theItem.getResidenceStateCode()).append(LINE_BREAK);						
				}
				
				
				if (theItem.getEmployerDesignatedID() != null && !theItem.getEmployerDesignatedID().trim().equals("")){	
					buffer.append("Payroll Number").append(COMMA).append(theItem.getEmployerDesignatedID()).append(LINE_BREAK);
				}
				if (theItem.getOrganizationUnitID()!= null && !theItem.getOrganizationUnitID().trim().equals("")){	
					buffer.append("Division").append(COMMA).append(theItem.getOrganizationUnitID()).append(LINE_BREAK);
				}
				buffer.append("Enrollment method").append(COMMA).append(theItem.getEnrollmentMethod()).append(LINE_BREAK);
				buffer.append("Enrollment processed date").append(COMMA).append(DateRender.formatByPattern(theItem.getEnrollmentProcessedDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
				buffer.append("Normal retirement date").append(COMMA).append(DateRender.formatByPattern(theItem.getNormalRetirementDate(), "", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
				buffer.append("Contribution status").append(COMMA).append(theItem.getContributionStatus()).append(LINE_BREAK);

                if(!Constants.INTERNET.equalsIgnoreCase(theItem.getEnrollmentMethod()) &&
                   !Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) &&
                   !Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod())) {
                    // It is suppresed
                } else if(theItem.getEligibleToDeferInd() != null && !Constants.NA.equalsIgnoreCase(theItem.getEligibleToDeferInd())) {
                    buffer.append("Eligible to defer").append(COMMA).append(theItem.getEligibleToDeferInd()).append(LINE_BREAK);
                } else if(Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) ||
                        Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod())) {
                    buffer.append("Eligible to defer").append(COMMA).append(Constants.ELIGIBLE_TO_DEFER).append(LINE_BREAK);                    
                }                   

                if(Constants.INTERNET.equalsIgnoreCase(theItem.getEnrollmentMethod()) ||
                        Constants.AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod()) ||
                        Constants.WAS_AUTO_ENROLL.equalsIgnoreCase(theItem.getEnrollmentMethod())){

					if (theItem.hasTradDeferral()){
						if (theItem.getContributionAmt() > 0){
							buffer.append("Deferral to Traditional 401(k)($)").append(COMMA);
							buffer.append(theItem.getContributionAmt());
						} else {
							buffer.append("Deferral to Traditional 401(k)($)").append(COMMA);
							buffer.append("0");
						}
						
						if (theItem.getContributionPct() > 0){
							buffer.append(LINE_BREAK);
							buffer.append("Deferral to Traditional 401(k)(%)").append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPct()),
							"", FORMAT_PATTERN,3, BigDecimal.ROUND_HALF_DOWN)).append("%");
						} else {
							buffer.append(LINE_BREAK);
							buffer.append("Deferral to Traditional 401(k)(%)").append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double("0"),
							"", FORMAT_PATTERN,3, BigDecimal.ROUND_HALF_DOWN)).append("%");
						}									
					}	
						
						
					if (theItem.hasRothDeferral()){
						buffer.append(LINE_BREAK);
						if (theItem.getContributionAmtRoth() > 0){
							buffer.append("Deferral to Roth 401(k)($)").append(COMMA);
							buffer.append(theItem.getContributionAmtRoth());
						}else {
							buffer.append("Deferral to Roth 401(k)($)").append(COMMA);
							buffer.append("0");
						}
						
						if (theItem.getContributionPctRoth() > 0 ){
							buffer.append(LINE_BREAK);
							buffer.append("Deferral to Roth 401(k)(%)").append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double(theItem.getContributionPctRoth()),"", FORMAT_PATTERN,3, BigDecimal.ROUND_HALF_DOWN)).append("%");	
						} else {
							buffer.append(LINE_BREAK);
							buffer.append("Deferral to Roth 401(k)(%)").append(COMMA);
							buffer.append(NumberRender.formatByPattern(new Double("0"),
							"", FORMAT_PATTERN,3, BigDecimal.ROUND_HALF_DOWN)).append("%");
						}
					}
				}
					
				if (Constants.NOT_ENTERED.equalsIgnoreCase(theItem.getDeferralComment())) {
                    buffer.append(LINE_BREAK);
                    buffer.append("Deferral at Enrollment").append(COMMA).append(theItem.getDeferralComment()).append(LINE_BREAK);					
				} 
		
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
		return "";
	}

	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return "";
	}

//	/**
//	 * @see ReportAction#getReportId()
//	 */
//	protected String getReportId() {
//		return ParticipantEnrollmentDetailsReportData.REPORT_ID;
//	}
//
//	/**
//	 * @see ReportAction#getReportName()
//	 */
//	protected String getReportName() {
//		return ParticipantEnrollmentDetailsReportData.REPORT_NAME;
//	}

	
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
        String requestedProfileId = (String)request.getParameter("profileId");
        
		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_1, Integer.toString(currentContract.getContractNumber()));
		SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);

		// required by stored proc
		try {
    		Date fromDate = format.parse("01/01/1940");
    		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_2,fromDate);
    		Date toDate = format.parse("01/01/9999");
    		criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_3,toDate);        		
    	} catch(ParseException pe) {
    	} 
    	
    	criteria.addFilter(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_6, requestedProfileId);

	}

	 
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		String forward = super.doCommon( reportForm, request,response); // ACI2, support direct linking.
		
        ParticipantEnrollmentDetailsReportForm form = (ParticipantEnrollmentDetailsReportForm)reportForm;
        form.setSpecialSortCategoryInd(getUserProfile(request).getCurrentContract().hasSpecialSortCategoryInd());
        
		ParticipantEnrollmentSummaryReportData report = (ParticipantEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
		
		String task = getTask(request);

		if (task.equals(DEFAULT_TASK)) {
			String strProfileId = request.getParameter("profileId");
			double profileId = Double.parseDouble(strProfileId);
			ArrayList selectedRecord = new ArrayList(1);
	    
			if (report != null){
				ArrayList details = new ArrayList(report.getDetails());
				Iterator iterator = details.iterator(); 
				while (iterator.hasNext()) { 
					ParticipantEnrollmentSummary item = (ParticipantEnrollmentSummary)iterator.next(); 
					if (item.getProfileId() == profileId){
						selectedRecord.add(item);
						report.setDetails(selectedRecord);
						// there should only be one record
						break;
					}
				}	

			}
			
			request.setAttribute(Constants.REPORT_BEAN, report);
			request.getSession().setAttribute(Constants.REPORT_BEAN, report); // needed for download to work.
		} else { // print, no ProfileId sent in, so just grab that set data and place in request. 
			request.setAttribute(Constants.REPORT_BEAN, request.getSession().getAttribute(Constants.REPORT_BEAN));
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forwards.get("page");
	}

	private String escapeField(String field)
	{
        if(field == null) return ""; 
            
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
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}