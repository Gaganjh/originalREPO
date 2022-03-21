package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.platform.web.validation.rules.NameRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresTotals;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.ProtectedStringBuffer;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * ParticipantForfeituresReportAction class handles the creation of 
 * Accounts Forfeitures reporting page. This class has been extended from 
 * ReportAction class to reuse the existing reporting framework behavior.
 *
 * @see ReportController for details
 * 
 * @author Vinothkumar Balasubramaniyam
 */
@Controller
@RequestMapping( value ="/participant")
@SessionAttributes({"participantForfeituresReportForm"})

public final class ParticipantForfeituresReportController extends ReportController {

	@ModelAttribute("participantForfeituresReportForm")
	public  ParticipantForfeituresReportForm populateForm()
	{
		return new  ParticipantForfeituresReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{ 
		forwards.put("input","/participant/participantForfeitureReport.jsp");
		forwards.put("default","/participant/participantForfeitureReport.jsp");
		forwards.put("sort","/participant/participantForfeitureReport.jsp");
		forwards.put("filter","/participant/participantForfeitureReport.jsp");
		forwards.put("page","/participant/participantForfeitureReport.jsp");
		forwards.put("print","/participant/participantForfeitureReport.jsp");
		}

	protected static final String DOWNLOAD_COLUMN_HEADING_GENERAL = "Total participants,";
	protected static final String DOWNLOAD_COLUMN_HEADING_TOTAL = "Total employee assets,Total employer assets,Total assets,Total Forfeitures";
	protected static final String DOWNLOAD_COLUMN_HEADING_AVERAGE = "Average employee assets,Average employer assets,Average assets,Average Forfeitures";

	private static final String ASTERISK = "*";
	private static final String AMOUNT_FORMAT = "##0.00";
	private static final String ANOTHER_TAB = "anotherTab";
	private static final String FROM_PAGE = "fromPage";
	private static final String FORFEITURES_ASOFDATE = "FORFEITURES_ASOFDATE";
	private static final String FORFEITURES_LASTNAME = "FORFEITURES_LASTNAME";
	private static final String FORFEITURES_STATUS = "FORFEITURES_STATUS";
	private static final String FORFEITURES_SSN = "FORFEITURES_SSN";
	private static final String FORFEITURES_DIVISION = "FORFEITURES_DIVISION";
	
	/**
	 * Default Constructor for ParticipantForfeituresReportAction
	 */
	public ParticipantForfeituresReportController() {
		super(ParticipantForfeituresReportController.class);
	}

	/**
	 * Method to download the report data
	 * 
	 * @see ReportController#getDownloadData(BaseReportForm,
	 *      ReportData,HttpServletRequest)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData reportData,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		Collection errors = new ArrayList();
		ProtectedStringBuffer buffer = new ProtectedStringBuffer();
        ParticipantForfeituresReportForm theForm = (ParticipantForfeituresReportForm) reportForm;
        String namePhrase = theForm.getNamePhrase();
        
        if (namePhrase != null && namePhrase.trim().length() > 0) {
            NameRule.getLastNameInstance().validate(
                    ParticipantForfeituresReportForm.FIELD_LAST_NAME, errors,
                    namePhrase);
        }
        
        if (!theForm.getSsn().isEmpty()) {
            SsnRule.getInstance().validate(
                    ParticipantForfeituresReportForm.FIELD_SSN, errors,
                    theForm.getSsn());
        }
		ParticipantForfeituresReportForm form = (ParticipantForfeituresReportForm) reportForm;
		Contract currentContract = getUserProfile(request).getCurrentContract();
		
        // Indicator to determine if the division column is shown
        boolean showDivision = form.getShowDivision();

		ParticipantForfeituresTotals participantForfeitureTotals = 
			((ParticipantForfeituresReportData) reportData).getParticipantForfeituresTotals();
				
		Date asOfDate = new Date(Long.parseLong(form.getAsOfDate()));
				
        buffer.append("Contract").append(COMMA).append(
			currentContract.getContractNumber()).append(COMMA).append(
			escapeField(currentContract.getCompanyName())).append(LINE_BREAK);
        
		// section one of the CSV report
		buffer.append("As of,").append(DateRender.formatByPattern(asOfDate, "", 
				RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
		if(CollectionUtils.isEmpty(errors)) {
	        buffer.append(DOWNLOAD_COLUMN_HEADING_GENERAL)
	        .append(participantForfeitureTotals.getTotalParticipants())
	        .append(LINE_BREAK);		    
		} else {
		    buffer.append(DOWNLOAD_COLUMN_HEADING_GENERAL).append("0").append(LINE_BREAK);
		}

    	if(CollectionUtils.isEmpty(errors)) {
			buffer.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL);
			buffer.append(LINE_BREAK);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getEmployeeAssetsTotal()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getEmployerAssetsTotal()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getTotalAssets()),
					ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getForfeituresTotal()),
					ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			buffer.append(LINE_BREAK);

			buffer.append(LINE_BREAK);
			buffer.append(DOWNLOAD_COLUMN_HEADING_AVERAGE);
			buffer.append(LINE_BREAK);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getEmployeeAssetsAverage()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getEmployerAssetsAverage()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getTotalAssetsAverage()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
			buffer.append(NumberRender.formatByPattern(
					new Double(participantForfeitureTotals.getForfeituresAverage()),
	                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
	
            if (form.getStatus() != null) {
            	buffer.append("Contribution Status,").append(form.getStatus()).append(LINE_BREAK);
            } else {
            	buffer.append("Contribution Status,").append("All").append(LINE_BREAK);
           	}
		}
    	
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

		buffer.append(LINE_BREAK);
		buffer.append("Last name,First name,SSN,");
		buffer.append("Contribution status, Withdrawal transaction date,");
		buffer.append("Employee assets,Employer assets,Total assets,Forfeitures");
		
		// When the contract Special Sort Election is not “No” or blank, then show Division field
		if (showDivision) {
			buffer.append(",Division");
		}
		
        boolean maskSsnFlag = true;// set the mask ssn flag to true as a default
        UserProfile user = getUserProfile(request);
        try {
        	maskSsnFlag = ReportDownloadHelper.isMaskedSsn(user, currentContract.getContractNumber() );
        } catch (SystemException systemException){
        	//log exception and output blank ssn
        	logger.error(systemException);
        }
		buffer.append(LINE_BREAK);
		
		Collection<ParticipantForfeituresDetails> details = reportData.getDetails();
		for(ParticipantForfeituresDetails theItem : details){
		
			buffer.append(escapeField(theItem.getLastName())).append(COMMA);
			buffer.append(escapeField(theItem.getFirstName())).append(COMMA);
			buffer.append(SSNRender.format(theItem.getSsn(), null, maskSsnFlag)).append(COMMA);
			
			buffer.append(theItem.getStatus()).append(COMMA);
			buffer.append(theItem.getTerminationDate() == null ? "" : 
				DateRender.formatByPattern(theItem.getTerminationDate(), "", RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED));
			buffer.append(COMMA);
			
			if (theItem.getEmployeeAssets() < 0.0) { 
				buffer.append(ASTERISK);
			} else {
				buffer.append(NumberRender.formatByPattern(new Double(theItem.getEmployeeAssets()),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			}
			buffer.append(COMMA);
			
			if (theItem.getEmployerAssets() < 0.0) {
				buffer.append(ASTERISK);
			} else {
				buffer.append(NumberRender.formatByPattern(new Double(theItem.getEmployerAssets()),			
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			}
			buffer.append(COMMA);
			
			if (theItem.getTotalAssets() < 0.0) {
				buffer.append(ASTERISK);
			} else { 
				buffer.append(NumberRender.formatByPattern(new Double(theItem.getTotalAssets()),			
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			}
			buffer.append(COMMA);

			if (theItem.getForfeitures() < 0.0) {
				buffer.append(ASTERISK);
			} else { 
				buffer.append(NumberRender.formatByPattern(new Double(theItem.getForfeitures()),			
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT));
			}
			buffer.append(COMMA);
			
            if (showDivision && theItem.getDivision() != null) {
            	buffer.append(escapeField(theItem.getDivision().trim()));
            }
            buffer.append(COMMA);

			buffer.append(LINE_BREAK);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getDownloadData");
		}
		return buffer.toString().getBytes();
	}
	
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
		return ParticipantForfeituresReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantForfeituresReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantForfeituresReportData.REPORT_NAME;
	}

	/**
	 * @see ReportController#populateReportCriteria(ReportCriteria, BaseReportForm, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter(ParticipantForfeituresReportData.FILTER_CONTRACT_NUMBER, 
				Integer.toString(currentContract.getContractNumber()));

		ParticipantForfeituresReportForm pfForm = (ParticipantForfeituresReportForm) form;

		if (!StringUtils.isEmpty(pfForm.getAsOfDate())) {
			criteria.addFilter(ParticipantForfeituresReportData.FILTER_ASOFDATE,
					pfForm.getAsOfDate());
		}

		if (!StringUtils.isEmpty(pfForm.getStatus())) {
			criteria.addFilter(ParticipantForfeituresReportData.FILTER_STATUS,
					((ParticipantForfeituresReportForm) form).getStatus());
		}

		if (!StringUtils.isEmpty(pfForm.getNamePhrase())) {
			criteria.addFilter(ParticipantForfeituresReportData.FILTER_LASTNAME,
					pfForm.getNamePhrase());
		}

		if (!pfForm.getSsn().isEmpty()) {
			criteria.addFilter(ParticipantForfeituresReportData.FILTER_SSN,
					pfForm.getSsn().toString());
		}
		
        if (!StringUtils.isEmpty(pfForm.getDivision())) {
            criteria.addFilter(ParticipantForfeituresReportData.FILTER_DIVISION, 
            		pfForm.getDivision());
        }

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * @see BaseReportController#doCommon(ActionMapping, BaseReportForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	
	public String doCommon ( BaseReportForm reportForm,HttpServletRequest request,HttpServletResponse response) 
		    throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		
		ParticipantForfeituresReportForm form = (ParticipantForfeituresReportForm) reportForm;

		String forward = super.doCommon( form, request,
				response);
		
		UserProfile userProfile = getUserProfile(request);
		form.setBaseAsOfDate(String.valueOf(userProfile.getCurrentContract()
				.getContractDates().getAsOfDate().getTime()));
		
		if (StringUtils.isEmpty(form.getAsOfDate())) {
			form.setAsOfDate(form.getBaseAsOfDate());
		}

		Contract currentContract = userProfile.getCurrentContract();
		form.setShowDivision(currentContract.hasSpecialSortCategoryInd());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	/**
	 * Method to populate the Report Action Form
	 * 
     * @param mapping
     *            ActionMapping object
     * @param reportForm
     *            The report form to populate
     * @param request
     *            The current request object 
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		
		//Accounts Forfeitures Tab navigation from other Tabs - Start		
		ParticipantSummaryReportForm form = (ParticipantSummaryReportForm) reportForm;
		HttpSession session =request.getSession(false);
		String fromPage = request.getParameter(FROM_PAGE);
		if (fromPage != null && fromPage.equalsIgnoreCase(ANOTHER_TAB)){
			populateSessionAttributesToForm(form, session);
		} else {
			populateFormAttributesToSession(form, session);	
		}
		//Accounts Forfeitures Tab navigation from other Tabs - End		
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
				.getAttribute(FORFEITURES_DIVISION));
		form.setDivision(tempValue);

		// for SSN field
		Ssn strSSN = (Ssn) session
				.getAttribute(FORFEITURES_SSN);
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
				.getAttribute(FORFEITURES_STATUS));
		form.setStatus(tempValue);

		// for LastName field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(FORFEITURES_LASTNAME));
		form.setNamePhrase(tempValue);

		// for AsOfDate field
		tempValue = StringUtils.trimToEmpty((String) session
				.getAttribute(FORFEITURES_ASOFDATE));
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
		session.setAttribute(FORFEITURES_DIVISION, form.getDivision());
		session.setAttribute(FORFEITURES_SSN, form.getSsn());
		session.setAttribute(FORFEITURES_STATUS, form.getStatus());
		session.setAttribute(FORFEITURES_LASTNAME, form.getNamePhrase());
		session.setAttribute(FORFEITURES_ASOFDATE, form.getAsOfDate());
	}	

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired
	private ParticipantForfeituresReportValidator participantForfeituresReportValidator;
	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(participantForfeituresReportValidator);
	}
	/**
	 * @see PsController#execute( ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	  @RequestMapping(value = "/participantForfeiture", method = { RequestMethod.POST })
		public String execute(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {

		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		  
			ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
			return "redirect:" + forward.getPath();

		}
	  @RequestMapping(value = "/participantForfeiture", method = { RequestMethod.GET })
		public String doDefault(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {
		
		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
		
			}
		  
			String forward = super.doDefault(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	  @RequestMapping(value = "/participantForfeiture", params = { "task=filter" }, method = { RequestMethod.GET })
		public String doFilter(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {
		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
		
			}
		 
			String forward = super.doFilter(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	  @RequestMapping(value = "/participantForfeiture", params = { "task=page" }, method = { RequestMethod.GET })
			public String doPage(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException, SystemException {
			  if (bindingResult.hasErrors()) {
					String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if (errDirect != null) {
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						
					}
					populateReportForm( form, request);
					ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
							null, 0);
					request.setAttribute(Constants.REPORT_BEAN, reportData);
					return forwards.get("input");// if input forward not
													// //available, provided default
			
				}
			  
				String forward = super.doPage(form, request, response);
				return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
			}
	  @RequestMapping(value = "/participantForfeiture", params = { "task=sort" }, method = { RequestMethod.GET })
		public String doSort(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {
		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
		
			}
		  
			String forward = super.doSort(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	  @RequestMapping(value = "/participantForfeiture", params = { "task=print" }, method = { RequestMethod.GET})
		public String doPrint(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {
		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
		
			}
		 
			String forward = super.doPrint(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	  
	  
	  @RequestMapping(value = "/participantForfeiture", params = { "task=download" }, method = { RequestMethod.GET })
		public String doDownload(@Valid @ModelAttribute("participantForfeituresReportForm") ParticipantForfeituresReportForm form,BindingResult bindingResult, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SystemException {
		  if (bindingResult.hasErrors()) {
				String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
				if (errDirect != null) {
					request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
					
				}
				populateReportForm( form, request);
				ParticipantForfeituresReportData reportData = new ParticipantForfeituresReportData(
						null, 0);
				request.setAttribute(Constants.REPORT_BEAN, reportData);
				return forwards.get("input");// if input forward not
												// //available, provided default
		
			} 
			String forward = super.doDownload(form, request, response);
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
	  
	  
	/**
	 * Method to return the escape key  
	 * 
	 * @param field
	 * @return String field
	 */
	private String escapeField(String field) {
		if (field.indexOf(",") != -1) {
			StringBuilder newField = new StringBuilder();
			field = newField.append("\"").append(field).append("\"").toString();
		}
		return field;
	}

}
