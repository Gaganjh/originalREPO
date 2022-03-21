package com.manulife.pension.bd.web.bob.investment;

import static com.manulife.pension.bd.web.BDConstants.AMOUNT_FORMAT;
import static com.manulife.pension.bd.web.BDConstants.CL2;
import static com.manulife.pension.bd.web.BDConstants.CL5;
import static com.manulife.pension.bd.web.BDConstants.INTEGER_FORMAT;
import static com.manulife.pension.bd.web.BDConstants.ZERO_STRING;
import static com.manulife.pension.bd.web.BDConstants.SIGPLUS;
import static com.manulife.pension.bd.web.BDErrorCodes.NO_PARTICIPANTS_INVESTED;
import static com.manulife.pension.platform.web.CommonConstants.AMOUNT_FORMAT_TWO_DECIMALS;
import static com.manulife.pension.platform.web.CommonConstants.BD_PRODUCT_ID;
import static com.manulife.pension.platform.web.CommonConstants.BD_PRODUCT_NY_ID;
import static com.manulife.pension.platform.web.CommonConstants.FUND_PACKAGE_MULTICLASS;
import static com.manulife.pension.platform.web.CommonConstants.HYPHON_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.REPORT_BEAN;
import static com.manulife.pension.platform.web.CommonConstants.SLASH_SYMBOL;
import static com.manulife.pension.platform.web.CommonConstants.SPACE_SYMBOL;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData.CSV_REPORT_NAME;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData.REPORT_ID;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_ASOFDATE_DETAILS;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_CONTRACT_NO;
import static com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData.FILTER_FUND_ID;
import static com.manulife.pension.service.report.valueobject.ReportSort.ASC_DIRECTION;
import static com.manulife.util.render.RenderConstants.CURRENCY_TYPE;
import static com.manulife.util.render.RenderConstants.MEDIUM_MDY_SLASHED;
import static com.manulife.util.render.SSNRender.MASK;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

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

import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportDetailVO;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportSummaryVO;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * . This is the action class for contact investment allocation details page
 * 
 * @author Siby Thomas
 */
@Controller
@RequestMapping(value ="/bob/investment")
@SessionAttributes({"investmentAllocationPageForm"})
public class InvestmentAllocationDetailsReportController extends BOBReportController {
	@ModelAttribute("investmentAllocationPageForm") 
	public InvestmentAllocationPageForm populateForm() 
	{
		return new InvestmentAllocationPageForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/investmentAllocationDetailsReport.jsp");
		forwards.put("default","/investment/investmentAllocationDetailsReport.jsp");
		forwards.put("sort","/investment/investmentAllocationDetailsReport.jsp");
		forwards.put("filter","/investment/investmentAllocationDetailsReport.jsp");
		forwards.put("page","/investment/investmentAllocationDetailsReport.jsp");
		forwards.put("print","/investment/investmentAllocationDetailsReport.jsp");
		}

    protected static final String DEFAULT_SORT_FIELD = AllocationDetailsReportDetailVO.keys.name;

    protected static final int DEFAULT_PAGE_SIZE = 35;

    protected static final String DOWNLOAD_COLUMN_HEADING_SUMMARY = "Employee assets total,"
            + " Employer assets Total, Total assets";

    protected static final String DOWNLOAD_COLUMN_HEADING_DETAILS = "Name, SSN, "
            + "Ongoing Contributions?, Employee Assets, Employer Assets, Total Assets";
    
    private static final String XSLT_FILE_KEY_NAME = "InvestmentAllocationDetailsReport.XSLFile";

    /**
     * . Constructor
     */
    public InvestmentAllocationDetailsReportController() {
        super(InvestmentAllocationDetailsReportController.class);
    }

    /**
     * If this is called from the investment allocation page as opposed to just being refreshed (to
     * resort or filter by a different date) then take the JavaScript POSTed data (see
     * doViewDetails()) and pass it on as parameters to the report framework.
     * 
     * @param criteria
     * @param form
     * @param request
     * 
     */
    @Override
    protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
            HttpServletRequest request) {
    	InvestmentAllocationPageForm actionForm = (InvestmentAllocationPageForm)form;
        Contract contract = getBobContext(request).getCurrentContract();
        criteria.addFilter(FILTER_CONTRACT_NO, new Integer(contract.getContractNumber()));
        criteria.addFilter(FILTER_FUND_ID, actionForm.getSelectedFundID());
        criteria.addFilter(FILTER_ASOFDATE_DETAILS, actionForm.getAsOfDateDetails());
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
        return DEFAULT_SORT_FIELD;
    }

    /**
     * @see BaseReportController#getDefaultSortDirection()
     */
    @Override
    protected String getDefaultSortDirection() {
        return ASC_DIRECTION;
    }

    /**
     * 
     * @see BaseReportController#getPageSize()
     */
    @Override
    protected int getPageSize(HttpServletRequest request) {
        return DEFAULT_PAGE_SIZE;
    }
    
    /**
     * @see BaseReportController#doCommon()
     */
    protected String doCommon( BaseReportForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        String forward = super.doCommon( actionForm, request, response);
        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData) request
                .getAttribute(REPORT_BEAN);

        String task = getTask(request);

        if (DEFAULT_TASK.equals(task) || FILTER_TASK.equals(task) || SORT_TASK.equals(task)) {
            if (report.getDetails().isEmpty()) {
                Collection<Exception> errors = new ArrayList<Exception>();
                errors.add(new GenericException(NO_PARTICIPANTS_INVESTED));
                request.setAttribute(Environment.getInstance().getErrorKey(), errors);
            }
        }

        getFundClass(report, request);

        report.prepareForRendering(((InvestmentAllocationPageForm) actionForm)
                .isAsOfDateDetailsCurrent());

        return forward;
    }
    @RequestMapping(value ="/investmentAllocationDetailsReport/",  method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				
				doCommon(form, request, null);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
			}
		}
		forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=filter"}, method =  {RequestMethod.POST}) 
    public String doFilter (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=print"} , method = {RequestMethod.GET}) 
    public String doPrint (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(actionform, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(actionform, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
    
        forward = super.doPrint( actionform, request, response);

        InvestmentAllocationDetailsReportData report = (InvestmentAllocationDetailsReportData)
            request.getAttribute(REPORT_BEAN);

	
        report.prepareForRendering(((InvestmentAllocationPageForm) actionform).isAsOfDateDetailsCurrent());
        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=sort"}, method =  {RequestMethod.POST}) 
    public String doSort (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=page"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doPage (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/", params={"task=printPDF"}, method ={RequestMethod.GET,RequestMethod.POST}) 
    public String doPrintPDF (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
         forward = super.doPrintPDF(form, request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doPrintPDF");
        }

        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
    }
    
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=downloadAll"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDownloadAll (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
    
    @RequestMapping(value ="/investmentAllocationDetailsReport/",params={"task=download"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doDownload (@Valid @ModelAttribute("investmentAllocationPageForm") InvestmentAllocationPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				doCommon(form, request, null);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}
		forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
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

        StringBuffer buffer = new StringBuffer();
        Contract currentContract = getBobContext(request).getCurrentContract();
     
        buffer.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);

        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) reportForm;
        String asOfDate = investmentPageForm.getAsOfDateDetails();

        buffer.append("As of,").append(
                DateRender.formatByPattern(new java.util.Date(Long.valueOf(asOfDate).longValue()),
                        SPACE_SYMBOL, MEDIUM_MDY_SLASHED)).append(LINE_BREAK);

        buffer.append(LINE_BREAK);

        AllocationDetailsReportSummaryVO summary = ((InvestmentAllocationDetailsReportData) report)
                .getSummary();

        buffer.append("Fund name,").append(summary.getFundName()).append(LINE_BREAK);

        buffer.append("Participants invested,").append(
                NumberRender.formatByPattern(summary.getParticipantsCount(), ZERO_STRING,
                        INTEGER_FORMAT)).append(LINE_BREAK);
        
        if ("PBA".equals(summary.getFundId()) || "NPB".equals(summary.getFundId())) {
            buffer.append("Class,").append("Not applicable").append(LINE_BREAK);
        } else if (null != summary.getFundClass() && SIGPLUS.equals(summary.getFundClass())) {
            buffer.append("Class,").append(summary.getFundClass()).append(LINE_BREAK);
        } else {
            buffer.append("Class,").append(
                    NumberRender.formatByPattern(summary.getFundClass(), ZERO_STRING,
                            INTEGER_FORMAT)).append(LINE_BREAK);
        }

        buffer.append(LINE_BREAK);

        buffer.append(DOWNLOAD_COLUMN_HEADING_SUMMARY).append(LINE_BREAK);

        buffer.append(
                NumberRender.formatByPattern(summary.getEmployeeAssetsTotal(), ZERO_AMOUNT_STRING,
                        AMOUNT_FORMAT)).append(COMMA);
        buffer.append(
                NumberRender.formatByPattern(summary.getEmployerAssetsTotal(), ZERO_AMOUNT_STRING,
                        AMOUNT_FORMAT)).append(COMMA);
        buffer.append(
                NumberRender.formatByPattern(summary.getAssetsTotal(), ZERO_AMOUNT_STRING,
                        AMOUNT_FORMAT)).append(LINE_BREAK);

        buffer.append(LINE_BREAK);

        buffer.append(DOWNLOAD_COLUMN_HEADING_DETAILS);

        Iterator iterator = report.getDetails().iterator();
        int rowNum = 0;
        while (iterator.hasNext()) {
            buffer.append(LINE_BREAK);
            rowNum++;
            AllocationDetailsReportDetailVO theItem = (AllocationDetailsReportDetailVO) iterator
                    .next();

            buffer.append(theItem.getName()).append(COMMA);

            buffer.append(SSNRender.format(theItem.getSsn(), MASK)).append(COMMA);

            String ongoingContributions = theItem.getOngoingContributions();

            buffer.append(ongoingContributions != null ? ongoingContributions : HYPHON_SYMBOL)
                    .append(COMMA);

            buffer.append(
                    NumberRender.formatByPattern(theItem.getEmployeeAssetsAmount(),
                            ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);

            buffer.append(
                    NumberRender.formatByPattern(theItem.getEmployerAssetsAmount(),
                            ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);

            buffer.append(
                    NumberRender.formatByPattern(theItem.getTotalAssetsAmount(),
                            ZERO_AMOUNT_STRING, AMOUNT_FORMAT)).append(COMMA);
        }

        return buffer.toString().getBytes();
    }

    /**
     * @See BaseReportAction#prepareXMLFromReport()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Document prepareXMLFromReport(BaseReportForm reportForm,
           ReportData report, HttpServletRequest request) throws ParserConfigurationException {
        
        InvestmentAllocationPageForm form = (InvestmentAllocationPageForm) reportForm;
        InvestmentAllocationDetailsReportData data = (InvestmentAllocationDetailsReportData) report;
        int rowCount = 1;
        int maxRowsinPDF;

        PDFDocument doc = new PDFDocument();
        
        // Gets layout page for investmentAllocationDetailsReport.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.INVESTMENT_ALLOCATION_DETAILS_PATH, request);
        
        Element rootElement = doc.createRootElement(BDPdfConstants.INVESTMENT_ALLOCATION_DETAILS);

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
        
        String asOfDate = form.getComparableAsOfDateDetails();
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByPattern(asOfDate, null, 
                           RenderConstants.MEDIUM_YMD_DASHED, RenderConstants.MEDIUM_MDY_SLASHED));

        // Gets number of rows present in report page.
        int noOfRows = getNumberOfRowsInReport(report);
        if (noOfRows > 0) {
            // Transaction Details - start
            Element fundDetailsElement = doc.createElement(BDPdfConstants.FUND_DETAILS);
            Element fundDetailElement;
            Iterator iterator = report.getDetails().iterator();
            // Gets number of rows to be shown in PDF.
            maxRowsinPDF = form.getCappedRowsInPDF();
            for (int i = 0; i < noOfRows && rowCount <= maxRowsinPDF; i++) {
                fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
                AllocationDetailsReportDetailVO theItem = (AllocationDetailsReportDetailVO) iterator.next();
                // Sets main report.
                setReportDetailsXMLElements(doc, fundDetailElement, theItem);
                doc.appendElement(fundDetailsElement, fundDetailElement);
                rowCount++;
            }
            doc.appendElement(rootElement, fundDetailsElement);
            // Transaction Details - end
        }
        else {
            // Sets info. message to be shown in the main table.
            String message = ContentHelper.getContentText(BDContentConstants.WARNING_NO_PARTICIPANTS_INVESTED_IN_THE_FUND, ContentTypeManager.instance().MESSAGE, null);
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, rootElement, doc, message);
        }
      
        if (form.getPdfCapped()) {
            // Sets PDF Capped message.
            doc.appendTextNode(rootElement, BDPdfConstants.PDF_CAPPED, getPDFCappedText());
        }
        
        // Sets footer, footnotes and disclaimer
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Sets footer text for PBA.
        if (getBobContext(request).getCurrentContract().isPBA()) {
            PdfHelper.setFootnotePBAXMLElement(doc, rootElement);
        }
       
        return doc.getDocument();

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
    private void setSummaryInfoXMLElements(PDFDocument doc, Element rootElement, InvestmentAllocationDetailsReportData data, 
                 LayoutPage layoutPageBean, InvestmentAllocationPageForm form) {
        Element summaryInfoElement = doc.createElement(BDPdfConstants.SUMMARY_INFO);
        
        String subHeader = ContentUtility.getContentAttributeText(layoutPageBean, BDContentConstants.SUB_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.SUB_HEADER, summaryInfoElement, doc, subHeader);

        if (data.getSummary() != null) {
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.FUND_NAME, data.getSummary().getFundName());
            
            if ("PBA".equals(data
                    .getSummary().getFundId()) || "NPB".equals(data
                            .getSummary().getFundId())) {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.FUND_CLASS, "Not applicable");
            } else {
                doc.appendTextNode(summaryInfoElement, BDPdfConstants.FUND_CLASS, String.valueOf(data
                        .getSummary().getFundClass()));
            }
            
            String noOfPpts = NumberRender.formatByPattern(data.getSummary().getParticipantsCount(), 
                              ZERO_STRING, INTEGER_FORMAT);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.NUM_OF_PPT, noOfPpts);
            
            String totalEEAsset = NumberRender.formatByType(data.getSummary()
                    .getEmployeeAssetsTotal(), ZERO_AMOUNT_STRING, CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_EE_ASSETS, totalEEAsset);
    
            String totalERAsset = NumberRender.formatByType(data.getSummary()
                    .getEmployerAssetsTotal(), ZERO_AMOUNT_STRING, CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_ER_ASSETS, totalERAsset);
                
            String totalAsset = NumberRender.formatByType(data.getSummary().getAssetsTotal(), 
                                ZERO_AMOUNT_STRING, CURRENCY_TYPE);
            doc.appendTextNode(summaryInfoElement, BDPdfConstants.TOTAL_ASSETS, totalAsset);
        }
            
        doc.appendElement(rootElement, summaryInfoElement);  
    }

    /**
     * This method sets report details XML elements
     * 
     * @param doc
     * @param fundDetailElement
     * @param theItem
     */
    private void setReportDetailsXMLElements(PDFDocument doc, Element fundDetailElement, AllocationDetailsReportDetailVO theItem) {
        if (theItem != null) {
            doc.appendTextNode(fundDetailElement, BDPdfConstants.PPT_NAME, theItem.getName());
            
            String ssnString = SSNRender.format(theItem.getSsn(), null);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.PPT_SSN, ssnString);
            
            String ongoingContributions = theItem.getOngoingContributions();
    
            doc.appendTextNode(fundDetailElement, BDPdfConstants.ONGOING_CONTRIB, 
                    (ongoingContributions != null ? ongoingContributions : HYPHON_SYMBOL));
    
            String totalEEAsset = NumberRender.formatByPattern(theItem.getEmployeeAssetsAmount(), 
                           ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.TOTAL_EE_ASSETS, totalEEAsset);
    
            String totalERAsset = NumberRender.formatByPattern(theItem.getEmployerAssetsAmount(), 
                           ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.TOTAL_ER_ASSETS, totalERAsset);
    
            String totalAsset = NumberRender.formatByPattern(theItem.getTotalAssetsAmount(), 
                         ZERO_AMOUNT_STRING, AMOUNT_FORMAT_TWO_DECIMALS);
            doc.appendTextNode(fundDetailElement, BDPdfConstants.TOTAL_ASSETS, totalAsset);
        }
    }

        
    /**
     * @See BaseReportAction#getXSLTFileName()
     */
    @Override
    public String getXSLTFileName() {
        return XSLT_FILE_KEY_NAME;
    }

    /**
     * @see BaseReportController#resetForm()
     */
    @Override
    protected BaseReportForm resetForm(
            BaseReportForm reportForm, HttpServletRequest request) throws SystemException {
        return reportForm;
    }

    /**
     * @see BaseReportController#getFileName()
     */
    @Override
    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
        InvestmentAllocationPageForm investmentPageForm = (InvestmentAllocationPageForm) form;
        String asOfDate = investmentPageForm.getAsOfDateDetails();
        Contract currentContract = getBobContext(request).getCurrentContract();
        StringBuffer csvFileName = new StringBuffer();
        
        csvFileName.append(getReportName()).append(HYPHON_SYMBOL);
        csvFileName.append(currentContract.getContractNumber()).append(HYPHON_SYMBOL);
        csvFileName.append(investmentPageForm.getSelectedFundName().replace(" ", SPACE_SYMBOL));
        csvFileName.append(HYPHON_SYMBOL);
        csvFileName.append(DateRender.format(new Date(Long.valueOf(asOfDate)), MEDIUM_MDY_SLASHED)
                .replace(SLASH_SYMBOL, SPACE_SYMBOL));
        csvFileName.append(CSV_EXTENSION);
        
        return csvFileName.toString();
    }

    /**
     * retrieves the fund class
     * 
     * @param bean
     * @param request
     * 
     * @throws SystemException
     */
    private void getFundClass(ReportData bean, HttpServletRequest request)
            throws SystemException {
        Contract contract = getBobContext(request).getCurrentContract();
        FundClassUtility fundClassUtility = FundClassUtility.getInstance();
        InvestmentAllocationDetailsReportData reportData = (InvestmentAllocationDetailsReportData) bean;
        AllocationDetailsReportSummaryVO details = ((InvestmentAllocationDetailsReportData) bean)
                .getSummary();
        if (FUND_PACKAGE_MULTICLASS.equalsIgnoreCase(contract.getFundPackageSeriesCode())) {
            details.setFundClass(fundClassUtility.getFundClassMediumName(details.getRateType()));
        } else if (BD_PRODUCT_ID.equalsIgnoreCase(contract.getProductId())
                || BD_PRODUCT_NY_ID.equalsIgnoreCase(contract.getProductId())) {
            details.setFundClass(fundClassUtility.getFundClassMediumName(CL2));
        } else {
            details.setFundClass(fundClassUtility.getFundClassMediumName(CL5));
        }
        if(null!=details.getFundClass() && details.getFundClass().equals(SIGPLUS)){
            reportData.setJhiIndicatorFlg(true);
        }
    }
    
    /**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate(ActionMapping mapping, Form form, HttpServletRequest request) {
		Collection penErrors = FrwValidation.doValidatePenTestAutoAction(form, mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			try {
				doCommon(mapping, (BaseReportForm)form, request, null);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
}
