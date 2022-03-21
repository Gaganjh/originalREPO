package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;		
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.HashMap;
import javax.validation.Valid;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportDetailVO;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportSummaryVO;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 *
 *
 * @author nvintila
 * @date Feb 8, 2004
 * @time 3:28:37 PM
 */
@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"investmentAllocationPageForm"})

public class InvestmentAllocationDetailsReportController extends ReportController {

	@ModelAttribute("investmentAllocationPageForm")
	public InvestmentAllocationPageForm populateForm() 
	{
		return new InvestmentAllocationPageForm();
		}

	public static Map<String,String> forwards = new HashMap<>();
	public static final String INVEST_ALLOCATION_DETAILS_REPORT = "/investment/investmentAllocationDetailsReport.jsp";
	static{ 
		forwards.put("input",INVEST_ALLOCATION_DETAILS_REPORT);
		forwards.put("default", INVEST_ALLOCATION_DETAILS_REPORT);
		forwards.put("sort",INVEST_ALLOCATION_DETAILS_REPORT);
		forwards.put("filter",INVEST_ALLOCATION_DETAILS_REPORT);
		forwards.put("page",INVEST_ALLOCATION_DETAILS_REPORT); 
		forwards.put("print",INVEST_ALLOCATION_DETAILS_REPORT);
		}

    private static final String AMOUNT_FORMAT = "########0.00";

    protected static final String DEFAULT_SORT_FIELD = AllocationDetailsReportDetailVO.keys.name;
    protected static final String DEFAULT_SORT_FIELD2 = AllocationDetailsReportDetailVO.keys.ssn;
    protected static final String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
    protected static final int DEFAULT_PAGE_SIZE = 35;//todo ReportCriteria.NOLIMIT_PAGE_SIZE;
    protected static final String COMMA = ",";
    protected static final String DOWNLOAD_COLUMN_HEADING_SUMMARY = "Employee assets total, Employer assets Total, Total assets";

    /**
     * If this is called from the investment allocation page as opposed to just
     * being refreshed (to resort or filter by a different date) then take the
     * JavaScript POSTed data (see doViewDetails()) and pass it on as
     * parameters to the report framework.
     */
    protected void populateReportCriteria(ReportCriteria criteria,
        BaseReportForm form, HttpServletRequest request) {

    		UserProfile userProfile = getUserProfile(request);
            criteria.addFilter(InvestmentAllocationReportData.FILTER_CONTRACT_NO,
                new Integer(userProfile.getCurrentContract().getContractNumber()));
        criteria.addFilter(InvestmentAllocationReportData.FILTER_FUND_ID,
            ((InvestmentAllocationPageForm) form).getSelectedFundID());
        criteria.addFilter(InvestmentAllocationReportData.FILTER_ASOFDATE_DETAILS,
            ((InvestmentAllocationPageForm) form).getAsOfDateDetails());
    }


    protected String getReportId() {
        return InvestmentAllocationDetailsReportData.REPORT_ID;
    }

    protected String getReportName() {
        return InvestmentAllocationDetailsReportData.REPORT_NAME;
    }

    protected String getDefaultSort() {
        return DEFAULT_SORT_FIELD;
    }

    protected String getDefaultSortDirection() {
        return DEFAULT_SORT_DIRECTION;
    }

    protected int getPageSize(HttpServletRequest request) {

        return DEFAULT_PAGE_SIZE;
    }

    /**
     * This purpose of this override is to render an error message in case
     * no matches are found.
     */
    @RequestMapping(value ="/investmentAllocationDetailsReport/",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward = null;
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
        }
    
        forward = super.doDefault( actionForm, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

        //this will have to be smarter because it will need to go through the whole list
        //and check whether all allocations are zero.  Wait until we have the VO.
        if (report.getDetails().isEmpty()) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.NO_PARTICIPANTS_INVESTED));
            request.setAttribute(Environment.getInstance().getErrorKey(), errors);
        }
		
        report.prepareForRendering(((InvestmentAllocationPageForm) actionForm).isAsOfDateDetailsCurrent());
        return forwards.get(forward);
    }

    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=page"} , method = {RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	String forward = null;
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
			 return "redirect:"+forward;
        	}
        }
    
        forward = super.doPage( actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

	
        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());
        return forwards.get(forward);
    }
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=print"} , method = {RequestMethod.GET}) 
    public String doPrint (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	String forward = null;
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
			 return "redirect:"+forward;
        	}
        }
    
        forward = super.doPrint( actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

	
        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());
        return forwards.get(forward);
    }
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=download"} , method = {RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	String forward = null;
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
			 return "redirect:"+forward;
        	}
        }
    
        forward = super.doDownload(actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

	
        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());
        return forwards.get(forward);
    }


    /**
     * Note: this is pretty much identical with doDefault() but has to exist
     * in order to prepare some rendering flags and the error message.
     */
    @RequestMapping(value ="/investmentAllocationDetailsReport/", params={"task=sort"} , method =  {RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	String forward = null;
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
			 return "redirect:"+forward;
        	}
        }
    
    
        forward = super.doSort( actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

        //this will have to be smarter because it will need to go through the whole list
        //and check whether all allocations are zero.  Wait until we have the VO.
        if (report.getDetails().isEmpty()) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.NO_PARTICIPANTS_INVESTED));
            request.setAttribute(Environment.getInstance().getErrorKey(), errors);
        }

        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());
        return forwards.get(forward);
    }

    /**
     *
     */
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request) {


        StringBuffer buffer = new StringBuffer();
        
        Contract currentContract = getUserProfile(request).getCurrentContract();
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) reportForm;
        String asOfDate = investmentPageForm.getAsOfDateReport();
        buffer.append("As of,").append(DateRender.formatByPattern(new java.util.Date(Long.valueOf(asOfDate)
						.longValue()), " ", RenderConstants.MEDIUM_MDY_SLASHED)).append(LINE_BREAK);

		buffer.append(LINE_BREAK);

        AllocationDetailsReportSummaryVO summary =
            ((InvestmentAllocationDetailsReportData) report).getSummary();

        buffer.append("Fund name,").append(summary.getFundName()).append(LINE_BREAK);
        
        buffer.append("Participants invested,").append(NumberRender.formatByPattern(summary.getParticipantsCount(),
                "0", "########0")).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
                
        buffer.append(DOWNLOAD_COLUMN_HEADING_SUMMARY).append(LINE_BREAK);       
        buffer.append(NumberRender.formatByPattern(summary.getEmployeeAssetsTotal(),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
        buffer.append(NumberRender.formatByPattern(summary.getEmployerAssetsTotal(),
                ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
        buffer.append(NumberRender.formatByPattern(summary.getAssetsTotal(),
                "0.00", "########0.00")).append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		
        String DOWNLOAD_COLUMN_HEADING_DETAILS = "Name, SSN, Ongoing Contributions?, " +
            "Employee assets, Employer Assets, Total Assets";

        buffer.append(DOWNLOAD_COLUMN_HEADING_DETAILS);
		//SSE024, mask ssn if no download report full ssn permission
        boolean maskSSN = true;
		try{
        	maskSSN =ReportDownloadHelper.isMaskedSsn(getUserProfile(request), currentContract.getContractNumber() );
         
        }catch (SystemException se)
        {
        	  logger.error(se);
        	// log exception and output blank ssn
        }
        Iterator iterator = report.getDetails().iterator();
        int rowNum = 0;

        while (iterator.hasNext()) {
        	buffer.append(LINE_BREAK);
            rowNum++;
            AllocationDetailsReportDetailVO theItem = (AllocationDetailsReportDetailVO) iterator.next();

                // Note: The formatting logic used here should be the same with
                // the one used in the investmentAllocationDetailsReport.jsp and
                // it is with the exception of the commas in amounts: cannot use
                // commas here since they also are delimiters for the columns.
            buffer.append(theItem.getName()).append(COMMA);
            buffer.append(SSNRender.format(theItem.getSsn(), "", maskSSN)).append(COMMA);
            String ongoingContributions = theItem.getOngoingContributions();
            buffer.append(ongoingContributions != null ? ongoingContributions : "n/a").append(COMMA);
            buffer.append(NumberRender.formatByPattern(theItem.getEmployeeAssetsAmount(),
                    ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
            buffer.append(NumberRender.formatByPattern(theItem.getEmployerAssetsAmount(),
                    ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
            buffer.append(NumberRender.formatByPattern(theItem.getTotalAssetsAmount(),
                    ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
        }
       

        return buffer.toString().getBytes();
    }
    
	protected BaseReportForm resetForm( BaseReportForm reportForm, 
			HttpServletRequest request) throws SystemException {

		//do nothing since we don't want to reset anything on this form
		
		return reportForm;
	}
    
	/**
	 * Invokes the filter task (e.g. limiting date range). It uses the common
	 * workflow with validateForm set to true.
	 *
	 * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
	 *      HttpServletResponse, boolean)
	 */
	
	@RequestMapping(value ="/investmentAllocationDetailsReport/", params={"task=filter"}  , method =  {RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		String forward = null;
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
			 return "redirect:"+forward;
        	}
        }
	
	
		if (logger.isDebugEnabled()) {
			logger.debug("+ doFilter");
		}

		forward = super.doFilter( actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(Constants.REPORT_BEAN);

        //this will have to be smarter because it will need to go through the whole list
        //and check whether all allocations are zero.  Wait until we have the VO.
        if (report.getDetails().isEmpty()) {
            Collection errors = new ArrayList();
            errors.add(new GenericException(ErrorCodes.NO_PARTICIPANTS_INVESTED));
            request.setAttribute(Environment.getInstance().getErrorKey(), errors);
        }

        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());


		if (logger.isDebugEnabled()) {
			logger.debug("- doFilter");
		}

		return forwards.get(forward);
	}	
	
	/*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 */

	  @Autowired
 private PSValidatorFWInput  psValidatorFWInput;

	  @InitBinder
public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
  binder.bind( request);
  binder.addValidators(psValidatorFWInput);
}
}

