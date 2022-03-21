package com.manulife.pension.bd.web.bob.investment;

import static com.manulife.pension.bd.web.BDConstants.CL2;
import static com.manulife.pension.bd.web.BDConstants.CL5;
import static com.manulife.pension.bd.web.BDConstants.VIEW_BY_ASSET_CLASS;
import static com.manulife.pension.bd.web.BDConstants.ZERO_STRING;
import static com.manulife.pension.bd.web.BDConstants.SIGPLUS;
import static com.manulife.pension.bd.web.BDPdfConstants.INVESTMENT_OPTION_TEXT;
import static com.manulife.pension.platform.web.CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS;
import static com.manulife.pension.platform.web.CommonConstants.BD_PRODUCT_ID;
import static com.manulife.pension.platform.web.CommonConstants.BD_PRODUCT_NY_ID;
import static com.manulife.pension.platform.web.CommonConstants.FUND_PACKAGE_MULTICLASS;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.INVALLOCATION_SORT_COLUMN;
import static com.manulife.pension.platform.web.CommonConstants.INVALLOCATION_SORT_DIRECTION;
import static com.manulife.pension.platform.web.CommonConstants.REPORT_BEAN;
import static com.manulife.pension.platform.web.CommonConstants.REPORT_BEAN_INVESTMENT;
import static com.manulife.pension.platform.web.CommonConstants.SLASH_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.VIEW_BY_ASSET;
import static com.manulife.pension.platform.web.CommonErrorCodes.NO_ALLOCATIONS_FOUND;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.EMPLOYEE_ASSETS;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.EMPLOYER_ASSETS;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.FUND_CLASS;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.FUND_NAME;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.MARKETING_SORT_ORDER;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.PARTICIPANTS_INVESTED_CURRENT;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.PERCENTAGE_OF_TOTAL;
import static com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails.TOTAL_ASSETS;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.CSV_REPORT_NAME;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_ASOFDATE_REPORT;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_CONTRACT_NO;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_CURRENTDATE;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_ISPBA;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_ORGANIZING_OPTION;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_SITE;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_VIEW_OPTION;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.REPORT_ID;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.SORT_ASCENDING;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.SORT_DESCENDING;
import static com.manulife.pension.service.report.valueobject.ReportCriteria.NOLIMIT_PAGE_SIZE;
import static com.manulife.pension.service.report.valueobject.ReportSort.ASC_DIRECTION;
import static com.manulife.pension.service.report.valueobject.ReportSort.DESC_DIRECTION;
import static com.manulife.util.render.RenderConstants.MEDIUM_MDY_SLASHED;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.investment.FundSeriesNameHelper;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetails;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationTotals;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.service.account.entity.FundOffering;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

import groovyjarjarcommonscli.ParseException;

/**
 * . This is the action class for Contract Investment Allocations Report Page
 * 
 * @author Siby Thomas
 * 
 */
@Controller
@RequestMapping(value ="/bob/investment")
@SessionAttributes({"investmentAllocationPageForm"})

public class InvestmentAllocationReportController extends BOBReportController {
	@ModelAttribute("investmentAllocationPageForm") 
	public InvestmentAllocationPageForm populateForm() 
	{
		return new InvestmentAllocationPageForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/investmentAllocationReport.jsp");
		forwards.put("default","/investment/investmentAllocationReport.jsp");
		forwards.put("sort","/investment/investmentAllocationReport.jsp");
		forwards.put("filter","/investment/investmentAllocationReport.jsp");
		forwards.put("page","/investment/investmentAllocationReport.jsp");
		forwards.put("print","/investment/investmentAllocationReport.jsp");
		}

    /**
     * . Constructor
     */
    public InvestmentAllocationReportController() {
        super(InvestmentAllocationReportController.class);
    }

    private static final String DOWNLOAD_COLUMN_HEADING_SUMMARY = ",Options With Assets, "
            + "Participants Invested, Employee Assets($), "
            + " Employer Assets($), Total Assets($), % Of Total";

    private static final String DOWNLOAD_COLUMN_HEADING_FUNDS = ",Ticker Symbol,Class,"
            + "Participants Invested Current ,Participants Invested Future, "
            + "Employee Assets ($), Employer Assets($), Total Assets ($), % Of Total";

    private static final String DOWNLOAD_COLUMN_HEADING_FUNDS_1 = ",Ticker Symbol,Class,"
            + "Participants Invested Current, ,Employee Assets ($),"
            + " Employer Assets($), Total Assets ($), % Of Total";
    
    private static final String XSLT_FILE_KEY_NAME = "InvestmentAllocationReport.XSLFile";
    
    private static final int DEDUCT_THIRTEEN = -13;
    
    private static final String OPTIONS_COLUMN = "option";
    private static final String PARTICPANT_INVESTED_COLUMN = "participantsInvested";
    private static final String EMPLOYEE_ASSETS_COLUMN = "employeeAssets";
    private static final String EMPLOYER_ASSETS_COLUMN = "employerAssets";
    private static final String TOTAL_ASSETS_COLUMN = "totalAssets";
    private static final String PERCENTAGE_OF_TOTAL_COLUMN = "percentageOfTotal";
    private static final String CLASS_COLUMN = "class";
    private static DecimalFormat percentFormatter = new DecimalFormat("#0.00");
    
   /**
    * The method is to make the DecimalFormat synchronized in order to make it thread safe
    * 
    * @param value
    * @return Percentage
    * @throws ParseException
    */
   public static synchronized String formatPercentageFormatter(Double value) {
       return percentFormatter.format(value);
       }
   
    /**
     * . The method populates the report criteria
     * 
     * @param criteria The ReportCriteria object
     * @param form The BaseReportForm form
     * @param request The current request object
     * 
     * @throws SystemException
     */
    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) throws SystemException {

        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) form;
        Contract contract = getBobContext(request).getCurrentContract();
        criteria.addFilter(FILTER_CONTRACT_NO, new Integer(contract.getContractNumber()));
        criteria.addFilter(FILTER_ISPBA, new Boolean(contract.isPBA()));
        criteria.addFilter(FILTER_SITE, Environment.getInstance().getSiteLocation());
        if (investmentPageForm.getAsOfDateReport() == null) {
            investmentPageForm.setAsOfDateReport(String.valueOf(contract.getContractDates()
                    .getAsOfDate().getTime()));
        }
        criteria.addFilter(FILTER_ASOFDATE_REPORT, investmentPageForm.getAsOfDateReport());
        criteria.addFilter(FILTER_VIEW_OPTION, investmentPageForm.getViewOption());
        criteria.addFilter(FILTER_CURRENTDATE, Long.toString(contract.getContractDates()
                .getAsOfDate().getTime()));
        criteria.addFilter(FILTER_ORGANIZING_OPTION, investmentPageForm.getOrganizingOption());
    }

    /**
     * @see BaseReportController#getReportId()
     */
    @Override
    protected String getReportId() {
        return REPORT_ID;
    }

    /**
     * @see BaseReportController#getReportName()
     */
    @Override
    protected String getReportName() {
        return CSV_REPORT_NAME;
    }

    /**
     * @see BaseReportController#getDefaultSort()
     */
    @Override
    protected String getDefaultSort() {
        // the default sort field and order is determined behind the scenes by the database
        return null;
    }

    /**
     * @see BaseReportController#getDefaultSortDirection()
     */
    @Override
    protected String getDefaultSortDirection() {
        // the default sort field and order is determined behind the scenes by the database
        return null;
    }

    /**
     * @see BaseReportController#getPageSize()
     */
    @Override
    protected int getPageSize(final HttpServletRequest request) {
        return NOLIMIT_PAGE_SIZE;
    }

    /**
     * The method is called when the task is doDownload. It returns the report in a byte[](csv)
     * 
     * @param reportForm The reportForm bean
     * @param report The ReportData object containing the contents for the report
     * @param request The current request object
     * 
     * @return byte[]
     */
    @Override
    @SuppressWarnings("unchecked")
    protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) {

        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) reportForm;
        String asOfDate = investmentPageForm.getAsOfDateReport();
        StringBuffer buffer = new StringBuffer();
        Contract currentContract = getBobContext(request).getCurrentContract();

        buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);


        buffer.append("As of,").append(
                DateRender.formatByPattern(new Date(Long.valueOf(asOfDate).longValue()),
                        SPACE_SYMBOL, MEDIUM_MDY_SLASHED));
        
        buffer.append(COMMA);
        
        if (VIEW_BY_ASSET_CLASS.equals(investmentPageForm.getOrganizingOption())) {
            buffer.append("Organized By Asset Class");
        } else {
            buffer.append("Organized By Risk/Return Category");
        }
        
        buffer.append(COMMA);

        if (VIEW_BY_ASSET.equals(investmentPageForm.getViewOption())) {
            buffer.append("Asset view");
        } else {
            buffer.append("Activity view");
        }

        buffer.append(LINE_BREAK);
        buffer.append(LINE_BREAK);
        buffer.append("Contract Investment Allocation Summary");
        buffer.append(LINE_BREAK);
        buffer.append("Number Of Investment Options Selected").append(COMMA).append(
                investmentPageForm.getNumberOfInvestmentOptionSelected());
        buffer.append(LINE_BREAK);
        buffer.append(LINE_BREAK);

        InvestmentAllocationReportData iAReportData = (InvestmentAllocationReportData) report;

        String series = FundSeriesNameHelper.getFundSeriesName(currentContract.getProductId(),
                getBobContext(request).getContractSiteLocation(), currentContract
                        .getFundPackageSeriesCode());

        buffer.append(series + DOWNLOAD_COLUMN_HEADING_SUMMARY);

        Iterator iterator = iAReportData.getAllocationTotals().iterator();
        while (iterator.hasNext()) {
            buffer.append(LINE_BREAK);
            AllocationTotals theTotals = (AllocationTotals) iterator.next();
            if (FundCategory.LIFECYCLE.equals(theTotals.getFundCategoryType())) {
                buffer.append(InvestmentAllocationPageForm.LIFECYCLE_TEXT).append(COMMA);
            } else if (FundCategory.LIFESTYLE.equals(theTotals.getFundCategoryType())) {
                buffer.append(InvestmentAllocationPageForm.LIFESTYLE_TEXT).append(COMMA);
            } else if (FundCategory.GIFL.equals(theTotals.getFundCategoryType())) {
                buffer.append(InvestmentAllocationPageForm.GIFL_TEXT).append(COMMA);
            } else if (FundCategory.NON_LIFESTYLE_LIFECYCLE.equals(theTotals.getFundCategoryType())) {
                buffer.append(getCsvString(investmentPageForm.getTotalText())).append(COMMA);
            } else if (FundCategory.PBA.equals(theTotals.getFundCategoryType())) {
                buffer.append(InvestmentAllocationPageForm.PBA_TEXT).append(COMMA);
            }

            buffer.append(theTotals.getNumberOfOptions()).append(COMMA);
            buffer.append(theTotals.getParticipantsInvested()).append(COMMA);

            String eeAssets = escapeField(NumberRender.formatByPattern(theTotals
                    .getEmployeeAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS));
            buffer.append(eeAssets).append(COMMA);

            String erAssets = escapeField(NumberRender.formatByPattern(theTotals
                    .getEmployerAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS));
            buffer.append(erAssets).append(COMMA);

            String totalAssets = escapeField(NumberRender.formatByPattern(theTotals
                    .getTotalAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS));
            buffer.append(totalAssets).append(COMMA);
            buffer.append(formatPercentageFormatter(theTotals.getPercentageOfTotal() * 100.0d))
                    .append(COMMA);
        }

        buffer.append(LINE_BREAK);
        buffer.append(LINE_BREAK);

        if (investmentPageForm.isAsOfDateReportCurrent()) {
            buffer.append(series + DOWNLOAD_COLUMN_HEADING_FUNDS);
        } else {
            buffer.append(series + DOWNLOAD_COLUMN_HEADING_FUNDS_1);
        }

        SortedMap categoryFundMap = iAReportData.getAllocationDetails();
        Set categoryKeys = categoryFundMap.keySet();
        Iterator categoryFundIterator = categoryKeys.iterator();
        //ME - CL 123194 - Moved line break that appears b/w each class/risk classification.
        buffer.append(LINE_BREAK);
        
        while (categoryFundIterator.hasNext()) {
            FundCategory fundCategory = (FundCategory) categoryFundIterator.next();
            buffer.append(fundCategory.getCategoryDesc());
            buffer.append(LINE_BREAK);
            ArrayList allocationDetailsList = (ArrayList) categoryFundMap.get(fundCategory);
            Iterator detailsIterator = allocationDetailsList.iterator();
            while (detailsIterator.hasNext()) {
                AllocationDetails allocationDetails = (AllocationDetails) detailsIterator.next();
                buffer.append(allocationDetails.getFundName()).append(COMMA);
                
                if("".equals(allocationDetails.getTickerSymbol())) {
                    buffer.append("-").append(COMMA); 
                } else {
                buffer.append(allocationDetails.getTickerSymbol()).append(COMMA);
                }
                
                if("PB".equals(fundCategory.getCategoryCode())) {
                    buffer.append("-").append(COMMA); 
                } else {
                    buffer.append(allocationDetails.getFundClass()).append(COMMA);
                }
                
                if (investmentPageForm.isAsOfDateReportCurrent()) {
                    buffer.append(allocationDetails.getParticipantsInvestedCurrent()).append(COMMA);
                    buffer.append(allocationDetails.getParticipantsInvestedFuture()).append(COMMA);
                } else {
                    buffer.append(allocationDetails.getParticipantsInvestedCurrent()).append(COMMA);
                    buffer.append(COMMA);
                }

                buffer.append(allocationDetails.getEmployeeAssets()).append(COMMA);
                buffer.append(allocationDetails.getEmployerAssets()).append(COMMA);
                buffer.append(allocationDetails.getTotalAssets()).append(COMMA);
                buffer.append(
                		formatPercentageFormatter(
                                allocationDetails.getPercentageOfTotal() * 100.0d)).append(COMMA);
                buffer.append(LINE_BREAK);
            }
        }

        return buffer.toString().getBytes();
    }

   
    @RequestMapping(value ="/investmentAllocationReport/", method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forward=preExecute(actionForm, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		if (bindingResult.hasErrors()) {
			InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getSession(false)
					.getAttribute(REPORT_BEAN_INVESTMENT);
			request.setAttribute(REPORT_BEAN_INVESTMENT, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

        
         forward = super.doDefault( actionForm, request, response);
        InvestmentAllocationReportData report = (InvestmentAllocationReportData) request
                .getAttribute(REPORT_BEAN);
        report.sort(MARKETING_SORT_ORDER, SORT_ASCENDING);
        Collection<Exception> errors = new ArrayList<Exception>();
        request.setAttribute(REPORT_BEAN_INVESTMENT, report);
        request.getSession(false).setAttribute(REPORT_BEAN_INVESTMENT, report);
        if (!hasAllocations(report.getAllocationTotals())) {
            errors.add(new GenericException(NO_ALLOCATIONS_FOUND));
        }
        actionForm.setCurrentBusinessDate(report.getContractDates().getAsOfDate());
        actionForm.setNonLifestyle(report.getAllocationTotals());
        actionForm
                .setNumberOfInvestmentOptionSelected(report.getNumberOfFundsSelected());
        actionForm.setJhiIndicatorFlg(report.isJhiIndicatorFlg());
        request.setAttribute(Environment.getInstance().getErrorKey(), errors);

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }

    /**
     * Invokes the sort task . It uses the common work flow with validateForm set to true.
     * 
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest, HttpServletResponse,
     *      boolean)
     */
    @RequestMapping(value ="/investmentAllocationReport/" ,params={"task=sort"}   , method =  {RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		if (bindingResult.hasErrors()) {
			InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getSession(false)
					.getAttribute(REPORT_BEAN_INVESTMENT);
			request.setAttribute(REPORT_BEAN_INVESTMENT, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

        String task = getTask(request);
        InvestmentAllocationReportData report = null;
        HttpSession session = request.getSession(false);
        report = (InvestmentAllocationReportData) session.getAttribute(REPORT_BEAN_INVESTMENT);
        session.removeAttribute(REPORT_BEAN_INVESTMENT);
        String column = form.getSortField();
        String direction = form.getSortDirection();
        session.setAttribute(INVALLOCATION_SORT_COLUMN, column);
        session.setAttribute(INVALLOCATION_SORT_DIRECTION, direction);
        
        if (OPTIONS_COLUMN.equalsIgnoreCase(column)) {
            column = FUND_NAME;
        }
        if (PARTICPANT_INVESTED_COLUMN.equalsIgnoreCase(column)) {
            column = PARTICIPANTS_INVESTED_CURRENT;
        }
        if (EMPLOYEE_ASSETS_COLUMN.equalsIgnoreCase(column)) {
            column = EMPLOYEE_ASSETS;
        }
        if (EMPLOYER_ASSETS_COLUMN.equalsIgnoreCase(column)) {
            column = EMPLOYER_ASSETS;
        }
        if (TOTAL_ASSETS_COLUMN.equalsIgnoreCase(column)) {
            column = TOTAL_ASSETS;
        }
        if (PERCENTAGE_OF_TOTAL_COLUMN.equalsIgnoreCase(column)) {
            column = PERCENTAGE_OF_TOTAL;
        }
        if (CLASS_COLUMN.equalsIgnoreCase(column)) {
            column = FUND_CLASS;
        }
        if (DESC_DIRECTION.equals(direction)) {
            direction = SORT_DESCENDING;
        }
        if (ASC_DIRECTION.equals(direction)) {
            direction = SORT_ASCENDING;
        }

        report.sort(column, direction);

        session.setAttribute(REPORT_BEAN_INVESTMENT, report);
        request.setAttribute(REPORT_BEAN_INVESTMENT, report);
        forward = forwards.get(task);
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }

    /**
     * . The method returns a boolean value if the allocationTotal is greater than 0
     * 
     * @param allocationTotals ArrayList
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    private boolean hasAllocations(final ArrayList allocationTotals) {
        AllocationTotals[] totals = new AllocationTotals[allocationTotals.size()];
        allocationTotals.toArray(totals);

        for (AllocationTotals total : totals) {
            if (total.getTotalAssets() > 0.0d) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the report Data from the session if it exists or the task is default. If the task is not
     * default (i.e. doPrint or doDownload), the reportData will be retrieved from the session
     * 
     * @param reportId The reportId
     * @param reportCriteria The criteria to populate
     * @param request The current request object
     * 
     * @return reportData The ReportData object containing the contents for the report
     * 
     * @throws SystemException
     * @throws ReportServiceException
     */
    @Override
    protected ReportData getReportData(String reportId, ReportCriteria reportCriteria,
            HttpServletRequest request) throws SystemException, ReportServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("+ getReportData");
        }
        String task = super.getTask(request);
        HttpSession session = request.getSession(false);
        ReportData report = (ReportData) session.getAttribute(REPORT_BEAN_INVESTMENT);

        ReportData bean = null;
        if (report == null || DEFAULT_TASK.equals(task) || FILTER_TASK.equals(task)) {
            bean = super.getReportData(reportId, reportCriteria, null);
            populateFundClass(bean, request);
        } else {
            bean = report;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("- getReportData");
        }
        return bean;
    }

    /**
     * Invokes the filter task (e.g. limiting date range). It uses the common workflow with
     * validateForm set to true.
     * 
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest, HttpServletResponse,
     *      boolean)
     */
    @RequestMapping(value ="/investmentAllocationReport/", params={"task=filter"} , method =  {RequestMethod.GET}) 
    public String doFilter (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		if (bindingResult.hasErrors()) {
			InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getSession(false)
					.getAttribute(REPORT_BEAN_INVESTMENT);
			request.setAttribute(REPORT_BEAN_INVESTMENT, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("+ doFilter");
		}
		 forward = super.doFilter(form, request, response);
		InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getAttribute(REPORT_BEAN);
		report.sort(MARKETING_SORT_ORDER, SORT_ASCENDING);
		request.setAttribute(REPORT_BEAN_INVESTMENT, report);
		request.getSession(false).setAttribute(REPORT_BEAN_INVESTMENT, report);
		form.setSortField(null);
		form.setSortDirection(null);

		form.setNumberOfInvestmentOptionSelected(report.getNumberOfFundsSelected());
		request.getSession(false).setAttribute(REPORT_BEAN_INVESTMENT, report);
		if (logger.isDebugEnabled()) {
			logger.debug("- doFilter");
		}
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }

    @RequestMapping(value ="/investmentAllocationReport/", params={"task=printPDF"} , method =  {RequestMethod.GET}) 
    public String doPrintPdf (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	  String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		if (bindingResult.hasErrors()) {
			InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getSession(false)
					.getAttribute(REPORT_BEAN_INVESTMENT);
			request.setAttribute(REPORT_BEAN_INVESTMENT, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
			 forward = super.doPrintPDF(form, request, response);
			 
			 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
    @RequestMapping(value ="/investmentAllocationReport/", params={"task=download"} , method =  {RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	 String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		if (bindingResult.hasErrors()) {
			InvestmentAllocationReportData report = (InvestmentAllocationReportData) request.getSession(false)
					.getAttribute(REPORT_BEAN_INVESTMENT);
			request.setAttribute(REPORT_BEAN_INVESTMENT, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		 forward = super.doDownload(form, request, response);
		 
		 return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		
    }
    /**
     * . Validate the input action form only for Activity view. Date selected cannot be more than 13
     * months ago from most recent month end date.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */
	@Autowired
	private InvestmentAllocationReportValidator investmentAllocationReportValidator;
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
		binder.addValidators(investmentAllocationReportValidator);
	}
    /**
     * @see BaseReportController#getFileName()
     */
    @Override
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) form;
        String asOfDate = investmentPageForm.getAsOfDateReport();
        Contract currentContract = getBobContext(request).getCurrentContract();
        String csvFileName = getReportName()
                + HYPHON_SYMBOL
                + currentContract.getContractNumber()
                + HYPHON_SYMBOL
                + DateRender.format(new Date(Long.valueOf(asOfDate)), MEDIUM_MDY_SLASHED).replace(
                        SLASH_SYMBOL, SPACE_SYMBOL) + CSV_EXTENSION;
        return csvFileName;
    }

    /**
     * . The method populates the funds with the corresponding asset class values
     * 
     * @param bean ReportData
     * @param request HttpServletRequest
     * 
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    private void populateFundClass(ReportData bean, HttpServletRequest request)
            throws SystemException { 
        Contract contract = getBobContext(request).getCurrentContract();
        FundClassUtility fundClassUtility = FundClassUtility.getInstance();
        InvestmentAllocationReportData reportData = (InvestmentAllocationReportData) bean;
        Map<FundCategory, ArrayList<AllocationDetails>> allocationDetails = ((InvestmentAllocationReportData) bean)
                .getAllocationDetails();
        Set<FundCategory> fundCategory = allocationDetails.keySet();
        boolean jhiIndicatorFlg = false;
        for (FundCategory category : fundCategory) {
            List<AllocationDetails> allocationDetail = allocationDetails.get(category);
            for (AllocationDetails details : allocationDetail) {
                if (FUND_PACKAGE_MULTICLASS.equalsIgnoreCase(contract.getFundPackageSeriesCode())) {
                	if(isValidFundClassNumber(fundClassUtility
                            .getFundClassNumber(details.getRateType()))){
	                    details.setFundClass(fundClassUtility.getFundClassMediumName(details.getRateType()));
                	}
                } else if (BD_PRODUCT_ID.equalsIgnoreCase(contract.getProductId())
                        || BD_PRODUCT_NY_ID.equalsIgnoreCase(contract.getProductId())) {
                	if(isValidFundClassNumber(fundClassUtility
                            .getFundClassNumber(CL2))){
	                    details.setFundClass(fundClassUtility.getFundClassMediumName(CL2));
                	}
                } else {
                	if(isValidFundClassNumber(fundClassUtility
                            .getFundClassNumber(CL5))){
                		details.setFundClass(fundClassUtility.getFundClassMediumName(CL5));
                	}
                }
                if(!jhiIndicatorFlg && null!=details.getFundClass() && details.getFundClass().equals(SIGPLUS)){
                	jhiIndicatorFlg = true;
                }
            }
        }
        reportData.setJhiIndicatorFlg(jhiIndicatorFlg);
    }
    
    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request) throws ParserConfigurationException {
        
        InvestmentAllocationPageForm form = (InvestmentAllocationPageForm) reportForm;
        InvestmentAllocationReportData data = (InvestmentAllocationReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();
        
        // Gets layout page for investmentAllocationReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.INVESTMENT_ALLOCATION_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.INVESTMENT_ALLOCATION);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets Roth message.
        setRothMessageElement(doc, rootElement, request);
        
        if(data.isJhiIndicatorFlg()) {
        // Sets SIG+ Disclosure message.
        	setSigPlusDisclosureMessageElement(doc, rootElement);
        }

        // Sets Summary Info.
        setSummaryInfoXMLElements(doc, rootElement, data, layoutPageBean, form);

        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        String bodyHeader2 = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.BODY2_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER2, rootElement, doc, bodyHeader2);
        
        String asOfDate = form.getComparableAsOfDateDetails();
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, 
            DateRender.formatByPattern(asOfDate, null, RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED));
        
        doc.appendTextNode(rootElement, BDPdfConstants.ORGANIZING_OPTION, form.getOrganizingOption());
        doc.appendTextNode(rootElement, BDPdfConstants.VIEW_OPTION, form.getViewOption());
        
        // Sets fund series name.
        Contract contract = getBobContext(request).getCurrentContract();
        String fundSeriesName = FundSeriesNameHelper.getFundSeriesName(contract.getProductId(), 
                                getBobContext(request).getContractSiteLocation(), contract.getFundPackageSeriesCode());     
        doc.appendTextNode(rootElement, BDPdfConstants.FUND_SERIES_NAME, fundSeriesName);

        // Sets Report Summary Info.
        setReportSummaryInfoXMLElements(doc, rootElement, data, form);
        
        // Gets number of rows present in report.
        int noOfRows = getNumberOfRowsInReport(report);
        
        //Main report - start
        if (noOfRows > 0) {

            Element reportDetailsElement = doc.createElement(BDPdfConstants.REPORT_DETAILS);
            Element reportDetailElement = null;
            Iterator iterator = data.getAllocationDetails().keySet().iterator();
            // Gets number of rows to be shown in PDF.
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {

                reportDetailElement = doc.createElement(BDPdfConstants.REPORT_DETAIL);
                Element allocationDetailsElement = doc
                        .createElement(BDPdfConstants.ALLOCATION_DETAILS);
                Element allocationDetailElement;

                FundCategory fundCategory = (FundCategory) iterator.next();

                // Sets fund category description.
                doc.appendTextNode(allocationDetailsElement, BDPdfConstants.FUND_CATEGORY,
                        fundCategory.getCategoryDesc());

                for (AllocationDetails allocationDetails : (ArrayList<AllocationDetails>) data
                        .getAllocationDetails().get(fundCategory)) {

                    if (rowCount <= maxRowsinPDF) {
                        allocationDetailElement = doc
                                .createElement(BDPdfConstants.ALLOCATION_DETAIL);
                        // Sets report details.
                        setReportDetailsXMLElements(doc, allocationDetailElement,
                                allocationDetails, fundCategory, form);
                        doc.appendElement(allocationDetailsElement, allocationDetailElement);
                        rowCount++;
                    }
                        
                }
                doc.appendElement(reportDetailElement, allocationDetailsElement);
                doc.appendElement(reportDetailsElement, reportDetailElement);

            }
            doc.appendElement(rootElement, reportDetailsElement);
            
        }
        //Main report - end
      
        if (form.getPdfCapped()) {
            // Sets PDF Capped message.
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        
        // Sets footer, footnotes and disclaimer
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets footer text for PBA.
        if (contract.isPBA()) {
            PdfHelper.setFootnotePBAXMLElement(doc, rootElement);
        }
        
        return doc.getDocument();

    }
    
    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param allocationDetailElement
     * @param allocationDetails
     * @param fundCategory
     * @param isAsOfDateReportCurrent
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element allocationDetailElement, AllocationDetails allocationDetails,
                 FundCategory fundCategory, InvestmentAllocationPageForm form) {
        
        if (allocationDetails != null) {
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.FUND_NAME, allocationDetails.getFundName());
            
            // Checks PB fund category and create an XML element to identify and fix super-script symbol
            // beside text in XSL.
            if (fundCategory.getCategoryCode().equals(FundOffering.FUND_TYPE_PBA)) {
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.PB_FUND_CATEGORY, null);     
            }
            if(BDPdfConstants.BLANK_STRING.equals(allocationDetails.getTickerSymbol())) {
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.TICKER_SYMBOL, "-");
            } else {
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.TICKER_SYMBOL, String.valueOf(allocationDetails.getTickerSymbol()));
            }
            
            if("PB".equals(fundCategory.getCategoryCode())) {
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.FUND_CLASS, "-");
            } else {
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.FUND_CLASS, String.valueOf(allocationDetails.getFundClass()));
            }
            
            // Sets participants invested (current) if it exceeds zero.
            if (allocationDetails.getParticipantsInvestedCurrent() > 0) {    
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.PPTS_INVESTED_CURRENT, String.valueOf(allocationDetails.getParticipantsInvestedCurrent()));
            }
            
            if (form.isAsOfDateReportCurrent()) {
                
                // Sets an XML element to determine whether "Participants invested (current/ongoing)" or
                // "Participants invested (current)" comes in report header.
                doc.appendTextNode(allocationDetailElement, BDPdfConstants.AS_OF_DATE_REPORT_CURRENT,
                        BDPdfConstants.BLANK_STRING);
                
                // Sets zero if sum of participants invested (both current and future) equals zero.
                if (allocationDetails.getParticipantsInvestedCurrent()
                        + allocationDetails.getParticipantsInvestedFuture() == 0) {
                    doc.appendTextNode(allocationDetailElement, BDPdfConstants.PPTS_INVESTED_CURRENT,
                            String.valueOf(0));
                }
                else {
                    // Sets participants invested (future) if participants invested (current) exceeds zero.
                    if (allocationDetails.getParticipantsInvestedCurrent() > 0) {    
                        doc.appendTextNode(allocationDetailElement, BDPdfConstants.PPTS_INVESTED_FUTURE, String.valueOf(allocationDetails.getParticipantsInvestedFuture()));  
                    }
                }
                
            }
           
            String eeAssets = NumberRender.formatByPattern(allocationDetails.getEmployeeAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.TOTAL_EE_ASSETS, eeAssets);
            
            String erAssets = NumberRender.formatByPattern(allocationDetails.getEmployerAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.TOTAL_ER_ASSETS, erAssets);
            
            String totalAssets = NumberRender.formatByPattern(allocationDetails.getTotalAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.TOTAL_ASSETS, totalAssets);
            
            String totalPercent = NumberRender.formatByPattern(allocationDetails.getPercentageOfTotal(), ZERO_STRING, CommonConstants.PERCENT_FORMAT, 4, BigDecimal.ROUND_HALF_UP);
            doc.appendTextNode(allocationDetailElement, BDPdfConstants.TOTAL_PERCENT, totalPercent);
        }
        
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
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, InvestmentAllocationReportData data, 
                 LayoutPage layoutPageBean, InvestmentAllocationPageForm form) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        doc.appendTextNode(summaryInfoElement, BDPdfConstants.NO_OF_INVESTMENT_OPTION, String.valueOf(form.getNumberOfInvestmentOptionSelected()));
  
        doc.appendElement(rootElement, summaryInfoElement);
    }

    /**
     * This method sets report summary information XML elements
     * 
     * @param doc
     * @param rootElement
     * @param data
     * @param form
     */
    @SuppressWarnings("unchecked")
    private void setReportSummaryInfoXMLElements(PDFDocument doc, Element rootElement, InvestmentAllocationReportData data, 
                 InvestmentAllocationPageForm form) {
        Element reportSummaryDetailsElement = doc.createElement(BDPdfConstants.REPORT_SUMMARY_DETAILS);
        Element reportSummaryDetailElement;
        if (data.getAllocationTotals() != null) {
            for (AllocationTotals allocationTotals : (ArrayList<AllocationTotals>)data.getAllocationTotals()) { 
                
                reportSummaryDetailElement = doc.createElement(BDPdfConstants.REPORT_SUMMARY_DETAIL);
                
                String fundCategoryType = allocationTotals.getFundCategoryType();
                
                // Checks investment option selected by contract and sets text according to option.
                if (fundCategoryType.equals(FundCategory.NON_LIFESTYLE_LIFECYCLE)) {
                    doc.appendTextNode(reportSummaryDetailElement, INVESTMENT_OPTION_TEXT, form
                            .getTotalText());
                }
                else if (fundCategoryType.equals(FundCategory.LIFECYCLE)) {
                    doc.appendTextNode(reportSummaryDetailElement, INVESTMENT_OPTION_TEXT,
                            InvestmentAllocationPageForm.LIFECYCLE_TEXT);
                }
                else if (fundCategoryType.equals(FundCategory.LIFESTYLE)) {
                    doc.appendTextNode(reportSummaryDetailElement, INVESTMENT_OPTION_TEXT,
                            InvestmentAllocationPageForm.LIFESTYLE_TEXT);
                }
                else if (fundCategoryType.equals(FundCategory.GIFL)) {
                    doc.appendTextNode(reportSummaryDetailElement, INVESTMENT_OPTION_TEXT,
                            InvestmentAllocationPageForm.GIFL_TEXT);
                }
                else if (fundCategoryType.equals(FundCategory.PBA)) {
                    doc.appendTextNode(reportSummaryDetailElement, INVESTMENT_OPTION_TEXT,
                            InvestmentAllocationPageForm.PBA_TEXT);
                }
                
                String noOfOptions = NumberRender.formatByType(allocationTotals.getNumberOfOptions(), ZERO_STRING, RenderConstants.INTEGER_TYPE);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.NO_OF_OPTIONS, noOfOptions);
                
                String pptInvested = NumberRender.formatByType(allocationTotals.getParticipantsInvested(), ZERO_STRING, RenderConstants.INTEGER_TYPE);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.PPT_INVESTED, pptInvested);
                
                String eeAssets = NumberRender.formatByPattern(allocationTotals.getEmployeeAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.TOTAL_EE_ASSETS, eeAssets);
                
                String erAssets = NumberRender.formatByPattern(allocationTotals.getEmployerAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.TOTAL_ER_ASSETS, erAssets);
                
                String totalAssets = NumberRender.formatByPattern(allocationTotals.getTotalAssets(), ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.TOTAL_ASSETS, totalAssets);
                
                String totalPercent = NumberRender.formatByPattern(allocationTotals.getPercentageOfTotal(), ZERO_STRING, CommonConstants.PERCENT_FORMAT, 4, BigDecimal.ROUND_HALF_UP);
                doc.appendTextNode(reportSummaryDetailElement, BDPdfConstants.TOTAL_PERCENT, totalPercent);
                
                doc.appendElement(reportSummaryDetailsElement, reportSummaryDetailElement);
    
            }
        }
        doc.appendElement(rootElement, reportSummaryDetailsElement); 
    }
    
    /**
     * @See BaseReportAction#getNumberOfRowsInReport()
     * Each allocation detail comprises one row in report table and so this method is 
     * overridden and modified.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Integer getNumberOfRowsInReport(ReportData report) {
        InvestmentAllocationReportData data = (InvestmentAllocationReportData) report;
        int noOfRows = 0;
        if (data.getAllocationDetails() != null) {
            for (FundCategory fundCategory :  (Set<FundCategory>) data.getAllocationDetails().keySet()) { 
                ArrayList<AllocationDetails> allocationDetails = (ArrayList<AllocationDetails>) data.getAllocationDetails().get(fundCategory); 
                noOfRows += allocationDetails.size();    
            }
        }
        return noOfRows;
    }
 
    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }
    
    /**
     * @param String fundClassNumber
     * @return boolean true/false.
     * if valid Fund Class Number then it will return true otherwise false. 
     */
    private boolean isValidFundClassNumber(String fundClassNumber){
    	boolean isValidFundClassNumber = false;
    	if(StringUtils.isNotBlank(fundClassNumber) && StringUtils.isNumeric(fundClassNumber)){
    		isValidFundClassNumber = true;
    	}
    	return isValidFundClassNumber;
    	
    }
    
    /**
     * don't want excel to think the , is the next field
     * 
     * @param field
     * @return String
     */
    private static String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(
                    CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }
}
