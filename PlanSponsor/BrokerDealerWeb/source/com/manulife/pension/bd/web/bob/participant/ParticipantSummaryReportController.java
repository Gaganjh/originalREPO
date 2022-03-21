package com.manulife.pension.bd.web.bob.participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;				//CL 110234
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;		//CL 110234
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.LIADisplayHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryTotals;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.LifeIncomeAmountDetailsVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;
//CL 110234

/**
 * This action handles the creation of the ParticipantSummaryReport. It will
 * also create the participant summary download.
 * 
 * 
 * @author aambrose
 * @see BDReportController for details
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"participantSummaryReportForm"})


public final class ParticipantSummaryReportController extends BDReportController {
	
	@ModelAttribute("participantSummaryReportForm") 
	public ParticipantSummaryReportForm populateForm() 
	
	{
		return new ParticipantSummaryReportForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantSummaryReport.jsp");
		forwards.put("default","/participant/participantSummaryReport.jsp");
		forwards.put("sort","/participant/participantSummaryReport.jsp");
		forwards.put("filter","/participant/participantSummaryReport.jsp");
		forwards.put("page","/participant/participantSummaryReport.jsp");
		forwards.put("print","/participant/participantSummaryReport.jsp");
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
	}

	protected static final String DOWNLOAD_COLUMN_HEADING_GENERAL = "Total Participants,";

	protected static final String DOWNLOAD_COLUMN_HEADING_TOTAL = "Employee Assets,Employer Assets,Total Assets";

	protected static final String DOWNLOAD_COLUMN_HEADING_AVERAGE = "Average employee assets,Average employer assets,Average assets";

	private static final String XSLT_FILE_KEY_NAME = "PptSummaryReport.XSLFile";

	private static final String NOT_PROVIDED = "not provided";

	private static final String LAST_NAME = "last_name";

	private static final String TOTAL_ASSETS = "total_assets";

	private static final String CONTRIB_STATUS = "cont_status";
	
	private static final String EMP_STATUS = "emp_status";			//CL 110234
	
	private static final String DIVISION = "division";
	
	private static final String MANAGED_ACCOUNT = "ma";

	private static final String ROW_HEADING_TOTAL = "Total";

	private static final String ROW_HEADING_AVERAGE = "Average";

	private static final String OUTSTANDING_LOANS_BALANCE = "Outstanding Loans";

	private static final String AS_OF = "As of";

	private static final String STATUS = "Status";

	private static final String LAST_NAME_STARTS_WITH = "Last name starts with";
	
	private static final String DIVISION_LABEL = "Division";

	private static final String SSN_IS = "SSN is";

	private static final String NAME_SSN_AGE = "Name,SSN,Date Of Birth (Age),";
	
	private static final String NAME_SSN_DIVISION_AGE = "Name,SSN,Division,Date Of Birth (Age),";

	private static final String STATUS_DEFAULT_HEADING = "Employment Status,Employment Status Effective Date,Contribution Status,Eligibility Date,Investment Instruction Type,";	//CL 110234

	private static final String ROTH_MONEY = "Roth Money";

	private static final String OUTSTANDING_LOANS = "Outstanding Loans ($)";

	private static final String GIFL = "Guaranteed Income Feature ";
	
	private static final String MA = "Mgd. Account Feature ";
	
	private static final String GIFL_UPPERCASE = "Guaranteed Income For Life ";

	public static final String CVS_REPORT_NAME = "ParticipantSummary-";

	private static final int NUMBER_9 = 9;
	
	private static final String ASSETS_FROM = "Assets From";
	
	private static final String ASSETS_TO = "Assets To";
	
	private static final String CONTRIBUTION_STATUS = "Contribution Status";
	
	private static final String EMPLOYMENT_STATUS = "Employment Status";			//CL 110234
	
	private static final String FILTERS_USED = "Filters used";
	
    private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
            BDErrorCodes.LAST_NAME_INVALID,
            BDRuleConstants.FIRST_NAME_LAST_NAME_RE);
    
	private static final String YES = "Yes";
	

	/**
	 * Constructor for ParticipantSummaryReportAction
	 */
	public ParticipantSummaryReportController() {
		super(ParticipantSummaryReportController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDownloadData(com.manulife.pension.platform.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	
	
	
	@RequestMapping(value ="/participant/participantSummary/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
		String forward= preExecute(actionForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }
	 if(bindingResult.hasErrors()){
		 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		 if(errDirect!=null){
			 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			 populateReportForm( actionForm, request);
				ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
						null, 0);
				request.setAttribute(BDConstants.REPORT_BEAN,reportData);
			 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		 }
	 }
	 forward=super.doDefault(actionForm, request, response);
	  return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	
	@RequestMapping(value ="/participant/participantSummary/" ,params={"task=download"}  , method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
		
		String forward= preExecute(actionForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }			
	 if(bindingResult.hasErrors()){
		 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
			ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
								null, 0);
				request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
					 }
		 }	
	        forward=super.doDownload(actionForm, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
			
 @RequestMapping(value ="/participant/participantSummary/" ,params={"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	 String forward= preExecute(actionForm, request, response);
     if (StringUtils.isNotBlank(forward)) {
   	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
      }
	 if(bindingResult.hasErrors()){
		 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		 if(errDirect!=null){
			 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
			 populateReportForm( actionForm, request);
				ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
						null, 0);
				request.setAttribute(BDConstants.REPORT_BEAN,reportData);
			 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		 }
	 }		
        forward=super.doFilter( actionForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
 
	
	@RequestMapping(value ="/participant/participantSummary/", params={"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward= preExecute(actionForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }
		if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		
        forward=super.doPage( actionForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	
	@RequestMapping(value ="/participant/participantSummary/", params={"task=sort"}  , method =  {RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward= preExecute(actionForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }
		if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
        forward=super.doSort( actionForm, request, response);
	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	@RequestMapping(value = "/participant/participantSummary/", params = {"task=print"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String doPrint(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm reportForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward= preExecute(reportForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }
		if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( reportForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		
		request.setAttribute("printFriendly", true);
		 forward = super.doPrint(reportForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participant/participantSummary/", params = {"task=printPDF"}, method = {RequestMethod.GET})
	public String doPrintpdf(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm reportForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		String forward= preExecute(reportForm, request, response);
	       if (StringUtils.isNotBlank(forward)) {
	     	  return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	        }
		if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( reportForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		request.setAttribute("printFriendly", true);
		 forward = super.doPrintPDF(reportForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getDownloadData");
		}

		ParticipantSummaryReportForm form = (ParticipantSummaryReportForm) reportForm;

		// Only show loans values if the loan feature is enabled for the current
		// contract
		boolean showLoans = form.getHasLoansFeature();
		// To check the contract level gateway status
		boolean showGateway = form.getHasContractGatewayInd();
		// Gateway Phase 1 -- end

		// To check the contract level MA status
		boolean showManagedAccount = form.getHasManagedAccountInd();
		// Only show Roth values if there are Roth money types enabled for the
		// current contract
		boolean showRoth = form.getHasRothFeature();

		ParticipantSummaryTotals totals = ((ParticipantSummaryReportData) report)
				.getParticipantSummaryTotals();

		Date asOfDate = new Date(Long.parseLong(form.getAsOfDate()));

		StringBuffer buffer = new StringBuffer();

		Contract currentContract = getBobContext(request).getCurrentContract();
		buffer.append(BDConstants.CONTRACT).append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

		// section one of the CSV report
		buffer.append(DOWNLOAD_COLUMN_HEADING_GENERAL).append(
				totals.getTotalParticipants()).append(LINE_BREAK);

		// section two of the CSV report
		buffer.append(LINE_BREAK);
		buffer.append(COMMA);
		buffer.append(DOWNLOAD_COLUMN_HEADING_TOTAL);
		if (showLoans) {
			buffer.append(COMMA).append(OUTSTANDING_LOANS_BALANCE);
		}
		buffer.append(LINE_BREAK);
		buffer.append(ROW_HEADING_TOTAL).append(COMMA);
		buffer.append(getCsvString(NumberRender
                .formatByType(totals.getEmployeeAssetsTotal(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE))).append(COMMA);
		buffer.append(getCsvString(
				NumberRender
                .formatByType(totals.getEmployerAssetsTotal(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE))).append(COMMA);
		buffer.append(getCsvString(NumberRender
                .formatByType(totals.getTotalAssets(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE)));
		if (showLoans)
			buffer.append(COMMA).append(getCsvString(NumberRender
                    .formatByType(totals.getOutstandingLoans(), BDConstants.DEFAULT_VALUE_ZERO,
                            RenderConstants.CURRENCY_TYPE)));
		buffer.append(LINE_BREAK);

		// section three of the CSV report
		buffer.append(ROW_HEADING_AVERAGE).append(COMMA);
		buffer.append(getCsvString(NumberRender
                .formatByType(totals.getEmployeeAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE))).append(COMMA);
		buffer.append(
				getCsvString(NumberRender.formatByType(totals
						.getEmployerAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
		                RenderConstants.CURRENCY_TYPE))).append(COMMA);
		buffer.append(getCsvString(NumberRender.formatByType(totals
				.getTotalAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
                RenderConstants.CURRENCY_TYPE)));
		if (showLoans) {
			buffer.append(COMMA).append(
					getCsvString(NumberRender.formatByType(totals
							.getOutstandingLoansAverage(), BDConstants.DEFAULT_VALUE_ZERO,
	                        RenderConstants.CURRENCY_TYPE)));
		}
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		buffer.append(FILTERS_USED).append(LINE_BREAK);
		buffer.append(AS_OF).append(COMMA).append(
				DateRender.formatByPattern(asOfDate, "",
						RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);
		if (BDConstants.NO.equals(form.getShowCustomizeFilter())) {
			
		 if(StringUtils.isNotBlank(form.getParticipantFilter())) {
            if (form.getParticipantFilter().equals(LAST_NAME)) {
    			buffer.append(LAST_NAME_STARTS_WITH).append(COMMA).append(
    					form.getQuickFilterNamePhrase()).append(LINE_BREAK);
            } else if (form.getParticipantFilter().equals(DIVISION)) {
                buffer.append(DIVISION_LABEL).append(COMMA).append(
                        form.getQuickFilterDivision()).append(LINE_BREAK);
            } else if (form.getParticipantFilter().equals(TOTAL_ASSETS)) {
    			buffer.append(ASSETS_FROM).append(COMMA).append(BDConstants.DOLLAR_SIGN).append(
    					form.getQuickTotalAssetsFrom()).append(LINE_BREAK);
    			buffer.append(ASSETS_TO).append(COMMA).append(BDConstants.DOLLAR_SIGN).append(
    					form.getQuickTotalAssetsTo()).append(LINE_BREAK);
            } else if (form.getParticipantFilter().equals(CONTRIB_STATUS)) {
    			buffer.append(CONTRIBUTION_STATUS).append(COMMA).append(
    					form.getQuickFilterStatus()).append(LINE_BREAK);
            } else if (form.getParticipantFilter().equals(EMP_STATUS)) {			//CL 110234
    			buffer.append(EMPLOYMENT_STATUS).append(COMMA).append(				//CL 110234
    					form.getQuickFilterEmploymentStatus()).append(LINE_BREAK); 	//CL 110234   			
			} else if (form.getParticipantFilter().equals(MANAGED_ACCOUNT)) {
				buffer.append(MA).append(COMMA).append(BDConstants.YES_VALUE).append(LINE_BREAK);
			}          
     } 
             if (form.getHasContractGatewayInd() && form.isAsOfDateCurrent()) {
                if (form.getQuickFilterGatewayChecked()) {
        			buffer.append(GIFL).append(COMMA).append(
        					BDConstants.YES_VALUE).append(LINE_BREAK);
                }
            }
            // Quick filter - end
        } else if (BDConstants.YES.equals(form.getShowCustomizeFilter())) {// Custom filter - start
            if (StringUtils.isNotBlank(form.getNamePhrase())) {
    			buffer.append(LAST_NAME_STARTS_WITH).append(COMMA).append(
    					form.getNamePhrase()).append(LINE_BREAK);
            }
            if(StringUtils.isNotBlank(form.getDivision())) {
                buffer.append(DIVISION_LABEL).append(COMMA).append(
                        form.getDivision()).append(LINE_BREAK);
            }
            if (StringUtils.isNotBlank(form.getTotalAssetsFrom())) {
    			buffer.append(ASSETS_FROM).append(COMMA).append(BDConstants.DOLLAR_SIGN).append(
    					form.getTotalAssetsFrom()).append(LINE_BREAK);
            }
            if (StringUtils.isNotBlank(form.getTotalAssetsTo())) {
    			buffer.append(ASSETS_TO).append(COMMA).append(BDConstants.DOLLAR_SIGN).append(
    					form.getTotalAssetsTo()).append(LINE_BREAK);
            }
            //CL 110234 Begin
            if (form.isAsOfDateCurrent() && form.getEmploymentStatus() != null) {
    			buffer.append(EMPLOYMENT_STATUS).append(COMMA).append(
    					getEmpStatusDescription(form.getEmploymentStatus())).append(LINE_BREAK);
            }
            else if (form.getBaseAsOfDate().equals(form.getAsOfDate())) {
    			buffer.append(EMPLOYMENT_STATUS).append(COMMA).append(BDConstants.CSV_ALL).append(LINE_BREAK);
            }
            //CL 110234 End            
            if (form.getStatus() != null) {
    			buffer.append(CONTRIBUTION_STATUS).append(COMMA).append(
    					form.getStatus()).append(LINE_BREAK);
            }
            else if (form.getBaseAsOfDate().equals(form.getAsOfDate())) {
    			buffer.append(CONTRIBUTION_STATUS).append(COMMA).append(BDConstants.CSV_ALL).append(LINE_BREAK);
            }
            if(form.getHasContractGatewayInd()) {
                if (form.getGatewayChecked()) {
        			buffer.append(GIFL).append(COMMA).append(BDConstants.YES_VALUE).append(LINE_BREAK);                	
                }
            }
            
            if(form.getHasManagedAccountInd()) {
                if (form.getManagedAccountChecked()) {
        			buffer.append(MA).append(COMMA).append(BDConstants.YES_VALUE).append(LINE_BREAK);                	
                }
            }
        }
        // Custom filter - end		
		// filters used

		// section four of the CSV report
		buffer.append(LINE_BREAK);
		if(form.isShowDivision()) {
		    buffer.append(NAME_SSN_DIVISION_AGE);
		} else {
		    buffer.append(NAME_SSN_AGE);
		}
		if (form.isAsOfDateCurrent()) {
			buffer.append(STATUS_DEFAULT_HEADING);
			if (showRoth) {
				buffer.append(ROTH_MONEY).append(COMMA);
			}
		}
		buffer.append("Employee Assets($),Employer Assets($),Total Assets($)");

		if (showLoans) {
			buffer.append(COMMA).append(OUTSTANDING_LOANS);
		}
		// To display gateway column header in download report
		// Column headers should be in sentence case
		if (form.isAsOfDateCurrent() && showGateway) {
			buffer.append(COMMA).append("Guaranteed Income Feature ");
		}		
		// To display Managed Account column header in download report
		// Column headers should be in sentence case
		if (form.isAsOfDateCurrent() && showManagedAccount) {
			buffer.append(COMMA).append(MA);
		}
		buffer.append(LINE_BREAK);
		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			ParticipantSummaryDetails theItem = (ParticipantSummaryDetails) iterator
					.next();

			buffer.append(getCsvString(theItem.getLastName()+ COMMA + BDConstants.SINGLE_SPACE_SYMBOL +theItem.getFirstName())).append(COMMA);
			buffer.append(SSNRender.format(theItem.getSsn(), null)).append(
					COMMA);
			if(form.isShowDivision()) {
			    buffer.append(escapeCommaForCsv(theItem.getDivision())).append(
	                    COMMA);
			}
			buffer.append(DateRender
                    .formatByPattern(theItem.getBirthDate(), NOT_PROVIDED,
                            RenderConstants.MEDIUM_MDY_SLASHED)).append(
					theItem.getShowAge() ? " ("+String.valueOf(theItem
							.getAge())+")" : "").append(COMMA);
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
				buffer.append(DateRender
		                .formatByPattern(theItem.getEligibilityDate(), NOT_PROVIDED,
		                        RenderConstants.MEDIUM_MDY_SLASHED)).append(COMMA);
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

			if (theItem.getEmployeeAssets() < 0.0)
				buffer.append(BDConstants.HYPHON_SYMBOL);
			else
				buffer.append(getCsvString(
						NumberRender.formatByPattern(theItem.getEmployeeAssets(),
			                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS)));
			buffer.append(COMMA);

			if (theItem.getEmployerAssets() < 0.0)
				buffer.append(BDConstants.HYPHON_SYMBOL);
			else
				buffer.append(
						getCsvString(NumberRender.formatByPattern(theItem.getEmployerAssets(),
			                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS)));
			buffer.append(COMMA);

			if (theItem.getTotalAssets() < 0.0)
				buffer.append(BDConstants.HYPHON_SYMBOL);
			else
				buffer.append(
						getCsvString(NumberRender.formatByPattern(theItem.getTotalAssets(),
			                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS)));

			if (showLoans)
				buffer.append(COMMA).append(
						getCsvString(NumberRender.formatByPattern(theItem.getOutstandingLoans(),
			                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS)
						));

			if (form.isAsOfDateCurrent() && showGateway) {
				// If LIA is turned on append LIA indicator
				if (theItem.isShowLIADetailsSection()) {
					buffer.append(COMMA).append(theItem.getParticipantGatewayInd()).append("/").append(CommonConstants.LIA_IND_TEXT);
				} else {
					buffer.append(COMMA).append(theItem.getParticipantGatewayInd());
				}
			}
			if (form.isAsOfDateCurrent() && showManagedAccount) {
				buffer.append(COMMA).append(theItem.getManagedAccountStatusInd());
			}

			buffer.append(LINE_BREAK);
		}
		if(report.getDetails()!=null && report.getDetails().size()==0){
			buffer.append(LINE_BREAK);
			buffer.append("Your search criteria produced no results.Please change your search criteria.");
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
	 * @See BaseReportAction#prepareXMLFromReport()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws ParserConfigurationException {

		ParticipantSummaryReportForm form = (ParticipantSummaryReportForm) reportForm;
		ParticipantSummaryReportData data = (ParticipantSummaryReportData) report;

		int rowCount = 1;
		int maxRowsinPDF;

		PDFDocument doc = new PDFDocument();

		LayoutPage layoutPageBean = getLayoutPage(
				BDPdfConstants.PPT_SUMMARY_REPORT_PATH, request);

		Element rootElement = doc.createRootElement(BDPdfConstants.PPT_SUMMARY);
		
        if(form.isShowDivision()){
            doc.appendTextNode(rootElement, BDPdfConstants.LEGAL_LANDSCAPE,
                    String.valueOf(form.isShowDivision()));
        }

		setIntroXMLElements(layoutPageBean, doc, rootElement, request);

		String body1Text = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.BODY1, null);
		PdfHelper.convertIntoDOM(PdfConstants.BODY1_TEXT, rootElement, doc, body1Text);
		
		if (form.getHasRothFeature()) {
		    String rothText = ContentHelper.getContentText(CommonContentConstants.MISCELLANEOUS_ROTH_INFO, ContentTypeManager.instance().MISCELLANEOUS, null);
		    PdfHelper.convertIntoDOM(PdfConstants.ROTH_MSG, rootElement, doc, rothText);
		}
		
		
		// Summary Info
		setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form);

		// Filters
		setFilterXMLElements(doc, rootElement, form);

		int noOfRows = getNumberOfRowsInReport(report);
		if (noOfRows > 0) {
			// Transaction Details - start
			Element txnDetailsElement = doc
					.createElement(BDPdfConstants.TXN_DETAILS);
			Element txnDetailElement;
			Iterator iterator = data.getDetails().iterator();
			maxRowsinPDF = form.getCappedRowsInPDF();
			for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
				txnDetailElement = doc.createElement(BDPdfConstants.TXN_DETAIL);
				ParticipantSummaryDetails theItem = (ParticipantSummaryDetails) iterator
						.next();
				setReportDetailsXMLElements(doc, txnDetailElement, theItem,
						form);
				doc.appendElement(txnDetailsElement, txnDetailElement);
				rowCount++;
			}
			doc.appendElement(rootElement, txnDetailsElement);
			// Transaction Details - end
		}

		if (form.getPdfCapped()) {
			doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED,
					getPDFCappedText());
		}

		setFooterXMLElements(layoutPageBean, doc, rootElement, request);

		return doc.getDocument();
	}

	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see BaseReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantSummaryReportData.DEFAULT_SORT;
	}

	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantSummaryReportData.REPORT_ID;
	}

	/**
	 * @return String cvs report file name
	 */
	protected String getReportName() {
		return CVS_REPORT_NAME;
	}

	/**
	 * @see BaseReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// default sort criteria
		// this is already set in the super

		Contract currentContract = getBobContext(request).getCurrentContract();

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
		// adding gateway filter to report criteria
		if (psform.isAsOfDateCurrent() && psform.getGatewayChecked()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_6,
					"gateway");
		}

		if (psform.isAsOfDateCurrent() && psform.getManagedAccountChecked()) {
			criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_14,
					"managed");
		}
		if (StringUtils.isNotBlank(psform.getTotalAssetsFrom())) {
			if (StringUtils.contains(psform.getTotalAssetsFrom(),
					BDConstants.COMMA_SYMBOL)) {
				criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_8,
						StringUtils.remove(psform.getTotalAssetsFrom()
								.toString(), BDConstants.COMMA_SYMBOL));

			} else {
				criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_8,
						psform.getTotalAssetsFrom().toString());
			}
		}

		if (StringUtils.isNotBlank(psform.getTotalAssetsTo())) {
			if (StringUtils.contains(psform.getTotalAssetsTo(),
					BDConstants.COMMA_SYMBOL)) {
				criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_9,
						StringUtils.remove(
								psform.getTotalAssetsTo().toString(),
								BDConstants.COMMA_SYMBOL));
			} else {
				criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_9,
						psform.getTotalAssetsTo().toString());
			}
		}
		
		  if (!StringUtils.isEmpty(psform.getDivision())) {
	            criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_7, 
	                    psform.getDivision());
	        }
		  
        //CL 110234 Begin
        if (!StringUtils.isEmpty(psform.getEmploymentStatus()) 
        		&& psform.isAsOfDateCurrent()) {
            criteria.addFilter(ParticipantSummaryReportData.FILTER_FIELD_10, 
            		psform.getEmploymentStatus());
        }
        //CL 110234 End

		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.report.BDReportAction#doCommon(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	
	 
	public String doCommon(BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		ParticipantSummaryReportForm actionForm=(ParticipantSummaryReportForm)form;
		if (actionForm.getResetGateway() != null) {
			if ("false".equals(actionForm.getResetGateway())) {
				actionForm.resetForm();
			}
		}
		if (actionForm.getResetManagedAccount() != null) {
			if ("false".equals(actionForm.getResetManagedAccount())) {
				actionForm.resetForm2();
			}
		}

		String forward = super.doCommon( actionForm, request,
				response);

		Contract currentContract = getBobContext(request).getCurrentContract();
		
		getBobContext(request).setPptProfileId(null);
		
		// Resetting participant gifl indicator in bob context while coming to
		// Participant Summary
		getBobContext(request).setParticipantGiflInd(null);

		actionForm.setBaseAsOfDate(String.valueOf(currentContract.getContractDates()
				.getAsOfDate().getTime()));
		if (StringUtils.isEmpty(actionForm.getAsOfDate()))
			actionForm.setAsOfDate(actionForm.getBaseAsOfDate());

		if (!actionForm.isAsOfDateCurrent()) {
			// reset the status filter
			actionForm.setStatus(null);
			actionForm.resetForm();
		}

		ParticipantSummaryReportData report = (ParticipantSummaryReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		// PPR41
		actionForm
				.setHasLoansFeature(currentContract.isLoanFeature()
						|| report.getParticipantSummaryTotals()
								.getOutstandingLoans() != 0.0);

		actionForm.setHasRothFeature(currentContract.hasRothNoExpiryCheck());
		// Gateway Phase 1 -- start
		// contrat level gateway check and reset the gateway checkbox status
		actionForm.setHasContractGatewayInd(currentContract
				.getHasContractGatewayInd());
		// Gateway Phase 1 -- end
		actionForm.setShowDivision(currentContract.hasSpecialSortCategoryInd());
		
		//CL 110234 Begin
        // populate list of employee statuses for the dropdown
		List employeeStatusList = ParticipantServiceDelegate.getInstance().getEmployeeStatusesListWithoutC(BDConstants.BD_APPLICATION_ID);
		actionForm.setStatusList(employeeStatusList);
        //CL 110234 End
		// Managed Account check
		ManagedAccountServiceFeatureLite managedAccountService = ContractServiceDelegate.getInstance()
				.getContractSelectedManagedAccountServiceLite(currentContract.getContractNumber());
		actionForm.setHasManagedAccountInd(managedAccountService != null);
        populatePartcipantsLIADetails(report, currentContract.getContractNumber(), actionForm);
        
        if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportForm(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseController#doValidate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	

	 @Autowired
  private BDValidatorFWInput  bdValidatorFWInput;
	 @Autowired
	 private ParticipantSummaryReportvalidator participantSummaryReportvalidator;
@InitBinder
 public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
   binder.bind( request);
   binder.addValidators(bdValidatorFWInput);
   binder.addValidators(participantSummaryReportvalidator);
}
 
 
	/**
	 * @see BaseController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
 @RequestMapping(value ="/participant/participantSummary/",  method =  {RequestMethod.POST}) 
 public String execute(@Valid @ModelAttribute("participantSummaryReportForm") ParticipantSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
	 
	 
	 ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();
	}
	 
 

	/**
	 * The preExecute method has been overriden to see if the contractNumber is
	 * coming as part of request parameter. If the contract Number is coming as
	 * part of request parameter, the BobContext will be setup with contract
	 * information of the contract number passed in the request parameter.
	 * 
	 */
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		super.preExecute( form, request, response);

		BobContextUtils.setUpBobContext(request);

		BobContext bob = BDSessionHelper.getBobContext(request);
		if (bob == null || bob.getCurrentContract() == null) {
			return forwards.get(BDConstants.BOB_PAGE_FORWARD);
		}

		if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}

        BobContextUtils.setupProfileId(request);
        
        return null;
    }
    
    /**
     * To add the COMMA symbol for cvs report.
     * 
     * @param String field
     * @return
     */
    private String escapeField(String field) {
        if (field.indexOf(COMMA) != BDConstants.NUM_MINUS_1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(BDConstants.DOUBLE_QUOTES).append(field).append(
                    BDConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }

    /*
     * construc the cvs report name as “ParticipantSummary-<contract#>-<mmddyyyy>” <mmddyyyy> is
     * the currently selected As Of Date @param HttpServletRequest request (non-Javadoc)
     * 
     * @see com.manulife.pension.platform.web.report.BaseReportAction#getFileName(javax.servlet.http.HttpServletRequest)
     */
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {

        Contract currentContract = getBobContext(request).getCurrentContract();

        Date asOfDate = new Date(Long
                .parseLong(((ParticipantSummaryReportForm) form).getAsOfDate()));

        return getReportName()
                + currentContract.getContractNumber()
                + BDConstants.HYPHON_SYMBOL
                + DateRender.format(asOfDate, RenderConstants.MEDIUM_MDY_SLASHED).replace(
                        BDConstants.SLASH_SYMBOL, BDConstants.SPACE_SYMBOL) + CSV_EXTENSION;
    }
    
    /**
     * This method sets summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param data
     * @param layoutPageBean
     * @param form
     */
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, ParticipantSummaryReportData data, 
                 LayoutPage layoutPageBean, ParticipantSummaryReportForm form) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);

        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        ParticipantSummaryTotals totals = ((ParticipantSummaryReportData) data)
                .getParticipantSummaryTotals();

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PPT, String.valueOf(totals
                .getTotalParticipants()));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.EE_ASSETS_TOTAL, NumberRender
                .formatByType(totals.getEmployeeAssetsTotal(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.ER_ASSETS_TOTAL, NumberRender
                .formatByType(totals.getEmployerAssetsTotal(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.EE_ASSETS_AVG, NumberRender
                .formatByType(totals.getEmployeeAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.ER_ASSETS_AVG, NumberRender
                .formatByType(totals.getEmployerAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_ASSETS, NumberRender
                .formatByType(totals.getTotalAssets(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));
        doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_ASSETS_AVG, NumberRender
                .formatByType(totals.getTotalAssetsAverage(), BDConstants.DEFAULT_VALUE_ZERO,
                        RenderConstants.CURRENCY_TYPE));

        if (form.getHasLoansFeature()) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.OUTSTANDING_LOANS, NumberRender
                    .formatByType(totals.getOutstandingLoans(), BDConstants.DEFAULT_VALUE_ZERO,
                            RenderConstants.CURRENCY_TYPE));
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.OUTSTANDING_LOANS_AVG,
                    NumberRender.formatByType(totals.getOutstandingLoansAverage(),
                            BDConstants.DEFAULT_VALUE_ZERO,
                            RenderConstants.CURRENCY_TYPE));
        }
        doc.appendElement(rootElement, summaryInfoElement);
    }
    
    /**
     * This method sets filter XML elements
     * 
     * @param doc
     * @param rootElement
     * @param form
     */
    private void setFilterXMLElements(PDFDocument doc, Element rootElement, ParticipantSummaryReportForm form) {
        Element filterElement = doc.createElement(BDPdfConstants.FILTERS);
        // Quick filter - start
		doc.appendTextNode(filterElement, BDPdfConstants.ASOF_DATE, DateRender
				.formatByPattern(new Date(Long.parseLong(form.getAsOfDate())),
						null, RenderConstants.MEDIUM_MDY_SLASHED));

       if (BDConstants.NO.equals(form.getShowCustomizeFilter())) {
        if(StringUtils.isNotBlank(form.getParticipantFilter())) {
            if (form.getParticipantFilter().equals(LAST_NAME)) {
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_NAME_PHARSE, form
                        .getQuickFilterNamePhrase());
            } else if (form.getParticipantFilter().equals(TOTAL_ASSETS)) {
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_ASSETS_FROM, form
                        .getQuickTotalAssetsFrom());
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_ASSETS_TO, form
                        .getQuickTotalAssetsTo());
            } else if (form.getParticipantFilter().equals(EMP_STATUS)) {					//CL 110234
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_EMP_STATUS, 			//CL 110234
                		getEmpStatusDescription(form.getQuickFilterEmploymentStatus()));  	//CL 110234              
            } else if (form.getParticipantFilter().equals(CONTRIB_STATUS)) {
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_STATUS, form
                        .getQuickFilterStatus());
            } else if (form.getParticipantFilter().equals(DIVISION)) {
                doc.appendTextNode(filterElement, BDPdfConstants.QUICK_DIVISION, form
                        .getQuickFilterDivision());
            } else if(form.getParticipantFilter().equals(MANAGED_ACCOUNT)) {
            	doc.appendTextNode(filterElement, BDPdfConstants.QUICK_MANAGED_ACCOUNT_CHECKED, BDConstants.YES_VALUE);
            }
        } 
            if (form.getHasContractGatewayInd() && form.isAsOfDateCurrent()) {
                if (form.getQuickFilterGatewayChecked()) {
                    doc.appendTextNode(filterElement, BDPdfConstants.QUICK_GATEWAY_CHECKED, BDConstants.YES_VALUE);
                }
            }
        }
        // Quick filter - end
        
        // Custom filter - start
        else if (BDConstants.YES.equals(form.getShowCustomizeFilter())) {
            if (StringUtils.isNotBlank(form.getNamePhrase())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_NAME_PHARSE, form
                        .getNamePhrase());
            }
            if (StringUtils.isNotBlank(form.getDivision())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_DIVISION_PHARSE, form
                        .getDivision());
            }
            if (StringUtils.isNotBlank(form.getTotalAssetsFrom())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_ASSETS_FROM, form
                        .getTotalAssetsFrom());
            }
            if (StringUtils.isNotBlank(form.getTotalAssetsTo())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_ASSETS_TO, form
                        .getTotalAssetsTo());
            }
            //CL 110234 Begin
            if (form.isAsOfDateCurrent() && form.getEmploymentStatus() != null) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_EMP_STATUS, 
                		getEmpStatusDescription(form.getEmploymentStatus()));
            }
            else if (form.getBaseAsOfDate().equals(form.getAsOfDate())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_EMP_STATUS, BDConstants.CSV_ALL);
            }
            //CL 110234 End
            if (form.getStatus() != null) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_STATUS, form.getStatus());
            }
            else if (form.getBaseAsOfDate().equals(form.getAsOfDate())) {
                doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_STATUS, BDConstants.CSV_ALL);
            }
            if(form.getHasContractGatewayInd()) {
                if (form.getGatewayChecked()) {
                    doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_GATEWAY_CHECKED, BDConstants.YES_VALUE);
                }
            }
            if(form.getHasManagedAccountInd()) {
                if (form.getManagedAccountChecked()) {
                    doc.appendTextNode(filterElement, BDPdfConstants.CUSTOM_MANAGED_ACCOUNT_CHECKED, BDConstants.YES_VALUE);
                }
            }
        }
        // Custom filter - end
        doc.appendElement(rootElement, filterElement);
    }
    
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param txnDetailElement
     * @param theItem
     * @param data
     * @param form
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element txnDetailElement, ParticipantSummaryDetails theItem, 
            ParticipantSummaryReportForm form) {
        if (theItem != null) {
            doc.appendTextNode(txnDetailElement, BDPdfConstants.LAST_NAME, theItem
                    .getLastName());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.FIRST_NAME, theItem
                    .getFirstName());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.PPT_SSN, SSNRender.format(
                    theItem.getSsn(), null));
            doc.appendTextNode(txnDetailElement, BDPdfConstants.DATE_OF_BIRTH, DateRender
                    .formatByPattern(theItem.getBirthDate(), NOT_PROVIDED,
                            RenderConstants.MEDIUM_MDY_SLASHED));
            if (theItem.getShowAge()) {
                doc.appendTextNode(txnDetailElement, BDPdfConstants.AGE, String.valueOf(theItem
                        .getAge()));
            }
            
            if(form.isShowDivision()) {
                doc.appendTextNode(txnDetailElement, BDPdfConstants.DIVISION, String.valueOf(theItem
                        .getDivision()));
            }
    
            doc.appendTextNode(txnDetailElement, BDPdfConstants.ELIGIBILITY_DATE, DateRender
                    .formatByPattern(theItem.getEligibilityDate(), NOT_PROVIDED,
                            RenderConstants.MEDIUM_MDY_SLASHED));
            if(form.isAsOfDateCurrent()){							            											//CL 110234
            doc.appendTextNode(txnDetailElement, BDPdfConstants.ASOFDATECURRENTFLAG, BDPdfConstants.ASOFDATECURRENTFLAG);	//CL 110234
            }																												//CL 110234
            doc.appendTextNode(txnDetailElement, BDPdfConstants.EMPLOYMENT_STATUS, theItem.getEmploymentStatusDescription());//CL 110234
            doc.appendTextNode(txnDetailElement, BDPdfConstants.STATUS, theItem.getStatus());
            doc.appendTextNode(txnDetailElement, BDPdfConstants.INVESTMENT_INSTRUCTION_TYPE, theItem
                    .getInvestmentInstructionType());
            if (form.getHasRothFeature()) {
                doc.appendTextNode(txnDetailElement, BDPdfConstants.ROTH_MONEY, theItem
                        .getRothInd());
            }
            String asset = NumberRender.formatByPattern(theItem.getEmployeeAssets(),
                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(txnDetailElement, BDPdfConstants.EE_ASSETS_TOTAL, asset);
            asset = NumberRender.formatByPattern(theItem.getEmployerAssets(),
                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(txnDetailElement, BDPdfConstants.ER_ASSETS_TOTAL, asset);
            asset = NumberRender.formatByPattern(theItem.getTotalAssets(),
                    BDConstants.DEFAULT_VALUE_ZERO, CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(txnDetailElement, BDPdfConstants.TOTAL_ASSETS, asset);
    
            if (form.getHasLoansFeature()) {
                asset = NumberRender.formatByPattern(theItem.getOutstandingLoans(),
                        BDConstants.DEFAULT_VALUE_ZERO,
                        CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(txnDetailElement, BDPdfConstants.OUTSTANDING_LOANS, asset);
            }
            if (form.getHasContractGatewayInd()) {
            	
            	String gateWayInd = null;
            	
                if (form.isAsOfDateCurrent()) {
                	gateWayInd = theItem.getParticipantGatewayInd();
                } else {
                	gateWayInd = theItem.getDefaultGateway();
                }

                if (StringUtils.equalsIgnoreCase(gateWayInd, YES) && theItem.isShowLIADetailsSection()) {
                	doc.appendTextNode(txnDetailElement, BDPdfConstants.DEFAULT_GATEWAY, gateWayInd +"/"+ CommonConstants.LIA_IND_TEXT);
                } else {
                	doc.appendTextNode(txnDetailElement, BDPdfConstants.DEFAULT_GATEWAY, gateWayInd);
                }
                
                
            }
            // Managed Account Feature
            if (form.getHasManagedAccountInd()) {
            	
            	String managedAccountInd = null;            	
                if (form.isAsOfDateCurrent()) {
                	managedAccountInd = theItem.getManagedAccountStatusInd();
                } else {
                	managedAccountInd = theItem.getDefaultManagedAccount();
                }
                	doc.appendTextNode(txnDetailElement, BDPdfConstants.DEFAULT_MANAGED_ACCOUNT, managedAccountInd );
            }
        }
    }
    /**
     * Sorting is done only based on the given sort field otherwise based on the Default Sort Direction.
     */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
        }else{
            criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
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
    
    private static String escapeCommaForCsv(String value){
        return StringUtils.isNotBlank(value) ? "\""+value+"\"" : StringUtils.EMPTY;  
      }
    
    
}
