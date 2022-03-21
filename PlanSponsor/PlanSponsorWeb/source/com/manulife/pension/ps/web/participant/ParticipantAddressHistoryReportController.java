package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistory;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistoryReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.tools.render.AddressChangeTrackingHelper;
import com.manulife.util.render.SSNRender;

/**
 * ParticipantAddressHistoryReportAction class is used to retrieve 
 * Address History data for a specific contract, which is shown on the Address History
 * page.
 * 
 * @author Glen Lalonde
 */

@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"participantAddressHistoryReportForm"})

public class ParticipantAddressHistoryReportController extends ReportController {

	@ModelAttribute("participantAddressHistoryReportForm") 
	public  ParticipantAddressHistoryReportForm populateForm() 
	{
		return new  ParticipantAddressHistoryReportForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/participant/addressHistory.jsp");
		forwards.put("default","/participant/addressHistory.jsp");
		forwards.put("sort","/participant/addressHistory.jsp");
		forwards.put("filter","/participant/addressHistory.jsp");
		forwards.put("page","/participant/addressHistory.jsp");
		forwards.put("print","/participant/addressHistory.jsp");
	}

	
	
	protected static final String DOWNLOAD_COLUMN_HEADING_WITH_SSN =     
		 "Contract Number,Last Name,First Name,Middle Initial,SSN,Address Line1,Address Line2,City,State,Zip,Country,Employer Provided Email Address,Update,Source,Changed by,Segment";
	protected static final String DOWNLOAD_COLUMN_HEADING_WITH_EMPLOYEE_ID = 
		 "Contract Number,Last Name,First Name,Middle Initial,Employee ID,Address Line1,Address Line2,City,State,Zip,Country,Employer Provided Email Address,Update,Source,Changed by,Segment";	
 	protected static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
 	protected static final String FORMAT_DATE_SHORT_MDY_HH_MM = "MM/dd/yyyy HH:mm"; // db uses timestamp not date
 	 
	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter("contractNumber", new Integer(userProfile
				.getCurrentContract().getContractNumber()));

		ParticipantAddressHistoryReportForm psform = (ParticipantAddressHistoryReportForm) form;
 
		if(psform.getNamePhrase() !=null && psform.getNamePhrase().trim().length() > 0) {
	        criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_FIELD_2,
	            psform.getNamePhrase().trim());
		}

		if(!psform.getSsn().isEmpty()) {
	        criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_FIELD_3,
	            psform.getSsn().toString());
		}
		
		if (!"-".equals(psform.getStatus())) {
			criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_STATUS_FIELD, psform.getStatus());
		}
		
		if (userProfile.isInternalUser()==false) {
			criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_STATUS_FOR_EXTERNAL_FIELD, "Y");
		}
		
		if (!"-".equals(psform.getSegment())) {
			criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_SEGMENT_FIELD, psform.getSegment());
		}
		
		if (psform.getDivision() !=null && psform.getDivision().trim().length() >0 ) {
			criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_DIVISION, psform.getDivision().trim());
		}
		
		if (psform.getFromDate() != null && psform.getToDate() !=null) {
			SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY);
			SimpleDateFormat formatWithHour = new SimpleDateFormat(FORMAT_DATE_SHORT_MDY_HH_MM);
			try {
				criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_FIELD_4, format.parse(psform.getFromDate()));
				criteria.addFilter(ParticipantAddressHistoryReportData.FILTER_FIELD_5, formatWithHour.parse(psform.getToDate()+" 23:59")); // end of day
			} catch(ParseException pe) {
	           	 if (logger.isDebugEnabled()) {
	         		logger.debug("ParseException in date processing:", pe);
	         	}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <-  populateReportCriteria");
		}
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantAddressHistoryReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantAddressHistoryReportData.REPORT_NAME;
	}

	protected String getDefaultSort() {
		return "update"; // was "lastName";
	}

	protected String getDefaultSortDirection() {
		return ReportSort.DESC_DIRECTION;
	}

	/**
	 * Called by framework to gen excel (csv) data
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		UserProfile userProfile = getUserProfile(request);
//      SSE S024 allow to download report, but mask the ssn if required
 
        //SSE S024 determine wheather the ssn should be masked on the csv report
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        try{
        	maskSsnFlag =ReportDownloadHelper.isMaskedSsn(userProfile, userProfile.getCurrentContract().getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
		
		
		StringBuffer buffer = new StringBuffer();

		boolean isDownloadEmployeeNumber = 
			((ParticipantAddressHistoryReportData) report).isDownloadEmployeeNumber();
		
		if ( isDownloadEmployeeNumber )
	        buffer.append(DOWNLOAD_COLUMN_HEADING_WITH_EMPLOYEE_ID);
	    else
	        buffer.append(DOWNLOAD_COLUMN_HEADING_WITH_SSN);

		if (userProfile.getCurrentContract().hasSpecialSortCategoryInd())
			buffer.append(",Division");
		else
			buffer.append(",");

		int contractNumber = ((ParticipantAddressHistoryReportData) report).getContractNumber();
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			ParticipantAddressHistory theItem = (ParticipantAddressHistory) iterator.next();
			buffer.append(contractNumber).append(COMMA);

			if (theItem.getLastName() != null) 
				buffer.append(escapeField(theItem.getLastName().trim()));
			
			buffer.append(COMMA);

			if (theItem.getFirstName() != null) 
				buffer.append(escapeField(theItem.getFirstName().trim()));
			
			buffer.append(COMMA);
			
			if (theItem.getMiddleInitial() != null) 
				buffer.append(theItem.getMiddleInitial());
				
			buffer.append(COMMA);

			if ( !isDownloadEmployeeNumber && theItem.getSsn() != null) 			
				//SSE S024 determine wheather the ssn should be masked on the csv report
	           buffer.append(SSNRender.format(theItem.getSsn().trim(), "", maskSsnFlag));
			else if ( isDownloadEmployeeNumber && theItem.getEmpId() != null) 			
				buffer.append(theItem.getEmpId().trim());

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

			if (theItem.getZipCode() != null) 
				buffer.append(theItem.getZipCode().trim());

			buffer.append(COMMA);
							
			if (theItem.getCountry() != null) {
				buffer.append(escapeField(theItem.getCountry().trim()));
			}
			
			buffer.append(COMMA);
			
			if (theItem.getEmployerProvidedEmailAddress() != null) {
				buffer.append(escapeField(theItem.getEmployerProvidedEmailAddress().trim()));
			}
			
			buffer.append(COMMA);
			
			if (theItem.getUpdateDate() != null) {
				buffer.append(escapeField(theItem.getUpdateDate().toString()));
			}
			 
			buffer.append(COMMA);
			
			String source = AddressChangeTrackingHelper.getRenderedUpdatedSource(theItem.getSource());
			buffer.append(escapeField(source));
			
			buffer.append(COMMA);
			
			String changedBy = AddressChangeTrackingHelper.getRenderedUpdatedBy(
					      AddressChangeTrackingHelper.PSW_EDIT_ADRESS_OR_ADDRESS_HISTORY,
					      theItem.getUserTypeCode(), theItem.getSource(),  
					      theItem.getChangedByFirstName(), theItem.getChangedByLastName());    
			
			if (changedBy != null)  {
				buffer.append(escapeField(changedBy.trim()));
			}

			buffer.append(COMMA);
			if (theItem.isAccountHolder() == true) {
				buffer.append("Account Holder");
			} else {
				buffer.append("Non-Account Holder");
			}
			
			buffer.append(COMMA);			
			if (userProfile.getCurrentContract().hasSpecialSortCategoryInd() && theItem.getDivision() != null) {
				buffer.append(escapeField(theItem.getDivision()));
			}
			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}
	
	private String escapeField(String field) {
		if(field.indexOf(",") != -1 ) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else 	{
			return field;
		}
	}
	protected void populateReportForm(
			ParticipantAddressHistoryReportForm form, HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportForm");
        }

        String task = getTask(request);

        /*
         * Reset page number properly.
         */
        if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK)
                || task.equals(FILTER_TASK) || task.equals(PRINT_TASK)
                || task.equals(DOWNLOAD_TASK)
                || task.equals(PRINT_PDF_TASK) || task.equals(DOWNLOAD_ALL_TASK)) {
            form.setPageNumber(1);
        }

        /*
         * Set default sort if we're in default task.
         */
        if (task.equals(DEFAULT_TASK) || form.getSortDirection() == null
                || form.getSortDirection().length() == 0) {
            form.setSortDirection(getDefaultSortDirection());
        }

        /*
         * Set default sort direction if we're in default task.
         */
        if (task.equals(DEFAULT_TASK) || form.getSortField() == null
                || form.getSortField().length() == 0) {
            form.setSortField(getDefaultSort());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportForm");
        }
    }
	
	 
	public String doCommon( BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}

		String forward = super.doCommon( reportForm, request, response);
		
		ParticipantAddressHistoryReportForm form = (ParticipantAddressHistoryReportForm)reportForm;
		ParticipantAddressHistoryReportData report = (ParticipantAddressHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
		form.setReport(report);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		
		return forward;
	}	
	
	
	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@RequestMapping(value ="/addressHistory", method =  {RequestMethod.POST}) 
		public String execute(@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult, HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
        
        // check for selected access
        if (userProfile.isSelectedAccess()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
        
        // check if contract is discontinued
        if (userProfile.getCurrentContract().isDiscontinued()) {
            return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
        }
		
        ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();
	}
	
	
	@RequestMapping(value ="/addressHistory", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
			populateReportForm( theForm, request);
			ParticipantAddressHistoryReportData reportData = theForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("default");
		}
		       String forward=super.doDefault( form, request, response);
		       return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	@RequestMapping(value ="/addressHistory" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
			populateReportForm( theForm, request);
			ParticipantAddressHistoryReportData reportData = theForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("default");
		}
		       String forward=super.doFilter( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	@RequestMapping(value ="/addressHistory" ,params={"task=print"}  , method =  {RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
			}
			ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
			populateReportForm( theForm, request);
			ParticipantAddressHistoryReportData reportData = theForm.getReport();
			request.setAttribute(Constants.REPORT_BEAN, reportData);
			return forwards.get("default");
		}
		       String forward=super.doPrint( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	
	 @RequestMapping(value ="/addressHistory" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
	    public String doPage (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
		 if(bindingResult.hasErrors()){
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
				populateReportForm( theForm, request);
				ParticipantAddressHistoryReportData reportData = theForm.getReport();
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("default");
			}
		       String forward=super.doPage( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	
		 @RequestMapping(value ="/addressHistory" ,params={"task=sort"}  , method =  {RequestMethod.GET}) 
	    public String doSort (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
			 if(bindingResult.hasErrors()){
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						
					}
					ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
					populateReportForm( theForm, request);
					ParticipantAddressHistoryReportData reportData = theForm.getReport();
					request.setAttribute(Constants.REPORT_BEAN, reportData);
					return forwards.get("default");
				}
			 String forward=super.doSort( form, request, response);
			 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
		 @RequestMapping(value ="/addressHistory" ,params={"task=download"}  , method =  {RequestMethod.GET})	
	 public String doDownload (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			     throws IOException,ServletException, SystemException {
			 if(bindingResult.hasErrors()){
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						
					}
					ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
					populateReportForm( theForm, request);
					ParticipantAddressHistoryReportData reportData = theForm.getReport();
					request.setAttribute(Constants.REPORT_BEAN, reportData);
					return forwards.get("default");
				}
			     String forward=super.doDownload( form, request, response);
			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
			    }
	
		 @RequestMapping(value ="/addressHistory" ,params={"task=downloadAll"}  , method =  {RequestMethod.GET})
		 public String doDownloadAll (@Valid @ModelAttribute("participantAddressHistoryReportForm") ParticipantAddressHistoryReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			     throws IOException,ServletException, SystemException {
			 if(bindingResult.hasErrors()){
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						
					}
					ParticipantAddressHistoryReportForm theForm = (ParticipantAddressHistoryReportForm) form;
					populateReportForm( theForm, request);
					ParticipantAddressHistoryReportData reportData = theForm.getReport();
					request.setAttribute(Constants.REPORT_BEAN, reportData);
					return forwards.get("default");
				}
			     String forward=super.doDownloadAll( form, request, response);
			     return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
			    }  

	@Autowired
	   private PSValidatorFWDefault  psValidatorFWDefault;
	@Autowired
	private ParticipantAddressHistoryReportValidator participantAddressHistoryReportValidator;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWDefault);
	    binder.addValidators(participantAddressHistoryReportValidator);
	}
	
}
