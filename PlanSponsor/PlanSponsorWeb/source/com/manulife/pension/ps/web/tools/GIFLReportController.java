package com.manulife.pension.ps.web.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PdfConstants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fee.valueobject.ClassType;
import com.manulife.pension.service.fee.valueobject.FundFeeVO;
import com.manulife.pension.service.fee.valueobject.GIFLReportDetails;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.NumberRender;

/**
 * Action class for GIFL Report 
 * @author Eswar
 *
 */
@Controller
@RequestMapping(value ="/giflDisclosurePdfReport")
public class GIFLReportController extends BaseAutoController {
	@ModelAttribute("dynaForm")
	public DynaForm populateForm() {
		return new DynaForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("secondaryWindowError", "/WEB-INF/global/secondaryWindowError.jsp");
	}

	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	private static final String JHRPS_LOGO_FILE = "JHRPS-logo-blue.jpg";
	private static final String XSL_FILE_NAME = "./xslstylesheet/GIFL_Disclosure_Report.xsl";
	private static final String IMAGE_BLACK_BAR_ONE = "Black_Bar_1.gif";
	private static final String IMAGE_BLACK_BAR_TWO = "Black_Bar_2.gif";
	private static final String IMAGE_BLACK_BAR_THREE = "Black_Bar_Header.bmp";
	private static final String TYPE_ONE_CLASS = "C01";
	
	/**
	 * Constructor
	 */
	public GIFLReportController() {
		super(GIFLReportController.class);
	}
	
	@RequestMapping(value ="/", method = {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (logger.isDebugEnabled()) {
            logger.debug("enter GIFLReportAction.doDefault");
        }
		GIFLReportDetails giflReportDetails = null;
		List<GenericException> errors = new ArrayList<GenericException>();
		Date currentDate = new Date();
		String giflVersion = request.getParameter("giflCode");
		String classType = request.getParameter("classType");
		try {
			String companyId = CommonEnvironment.getInstance().getSiteLocation();
			String locationId = StringUtils.equals(companyId,
					Constants.SITEMODE_USA) ? Constants.COUNTRY_USA_CODE : Constants.COUNTRY_NY_CODE;
			Location location = StringUtils.equals(companyId,
					Constants.SITEMODE_USA) ? Location.US : Location.NEW_YORK;
			
			String appId = Environment.getInstance().getAppId();
			FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(appId);
			
			ClassType selectedClassType = null;
			List<ClassType> classTypes = feeServiceDelegate.getClassTypes(new Date());
			for (ClassType classTypeVO : classTypes) {
				if(StringUtils.equals(classTypeVO.getClassShortName(), classType)) {
					selectedClassType = classTypeVO;
				}
			}
			if(selectedClassType == null) {
				throw new SystemException("Select Class cannot be null" 
						+ " .paramter passed is " + classType);
			}
			
			boolean  isNy = StringUtils.equalsIgnoreCase(companyId, Constants.SITEMODE_USA) ? false : true;
			List<String> giflVersions = feeServiceDelegate.getGIFLVersionsForEffectiveDate(new Date(), isNy);
			
			boolean validGIFLVersion = false;
			for(String giflVersionCode : giflVersions) {
				if(StringUtils.equals(giflVersionCode, giflVersion)) {
					validGIFLVersion = true;
				}
			}
			
			if(!validGIFLVersion) {
				throw new SystemException("Select GIFL version is invalid" 
						+ " .paramter passed is " + giflVersion);
			}
			giflReportDetails = feeServiceDelegate.getGIFLDisclosureItems(
					giflVersion, currentDate, locationId, selectedClassType);
			ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(
					giflReportDetails, selectedClassType, giflVersion, location);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
		    response.setHeader("Pragma", "no-cache");
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", "inline");
	        response.setContentLength(pdfOutStream.size());
			ServletOutputStream sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
			sos.flush();
		} catch (Exception exception) {
			logger.error("exception occured in GIFLReportAction.doDefault() ", exception);
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);

		}
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit GIFLReportAction.doDefault");
        }
        
		return null;
	}
	
	/**
     * This method is used to get XSL file name for PDF generation
     * 
     * @return String XSLT file name
     */
    protected String getXSLTFileName() {
    	return XSL_FILE_NAME;
    }

    /**
     * This method would return the key present in ReportsXSL.properties file. This key has the
     * value as path to FOP Configuration file.
     * 
     * @return String
     */
    protected String getFOPConfigFileName() {
        return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
    }
    
    
    /**
     * This method is used to get CMA value.
     * 
     * @param key
     * 
     * @param location
     * 
     * @return String
     */
    protected String getContent(int key, Location  location) {
    	return ContentHelper.getContentText(key,
                ContentTypeManager.instance().FEE_DISCLOSURE, location);
    }
    
    /*
     * This is used to image path
     * 
     * @param file
     * 
     * @return String
     */
	private String getUnmanagedImagePath(String file) {
		StringBuffer imagePath = new StringBuffer();
		imagePath.append(PdfHelper.class.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX + file));
		return imagePath.toString();
	}
    
    /**
     * This is used to get output stream in which the PDF content is written into a byte array
     * @param giflReportDetails 
     * @param selectedClassType 
     * @param giflVersion 
     * @param location 
     * 
     * @return pdfOutStream
     * 
     * @throws SystemException
     */
	protected ByteArrayOutputStream prepareXMLandGeneratePDF(GIFLReportDetails giflReportDetails,
			ClassType selectedClassType, String giflVersion, Location location) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside prepareXMLandGeneratePDF");
		}
		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		Object xmlTree = null;
		try {
			xmlTree = prepareReportXMLData(giflReportDetails, selectedClassType, giflVersion, location);
			String xsltFileName = getXSLTFileName();
			String configFileName = getFOPConfigFileName();
			if (xmlTree == null || xsltFileName == null) {
				return pdfOutStream;
			}
			String configfile = ReportsXSLProperties.get(configFileName);
			String includedXSLPath = ReportsXSLProperties.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
			if (xmlTree instanceof Document) {
				pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document) xmlTree, xsltFileName,
						configfile, includedXSLPath);
			}
		} catch (ParserConfigurationException pce) {
			logger.error("DocumentBuilder cannot be created which satisfies the configuration requested.", pce);
			throw new SystemException(pce, "prepareXMLandGeneratePDF method Failed ");
		} catch (Exception e) {
			logger.error("exception occured while creating the OutputStream of the PDF file", e);
			if (xmlTree != null) {
				logger.error(toXml((Document) xmlTree));
			}
			throw new SystemException(e, "prepareXMLandGeneratePDF method Failed ");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting prepareXMLandGeneratePDF");
		}
		return pdfOutStream;
	}
    
	/*
	 * This method is used to get PDFDocument object.
	 * 
	 * @param giflReportDetails 
	 * @param selectedClassType 
	 * @param giflCode 
	 * @param location
	 *  
	 * @return doc
	 * 
	 * @throws ParserConfigurationException
	 * @throws SystemException 
	 */
	private Document prepareReportXMLData(GIFLReportDetails giflReportDetails,
			ClassType selectedClassType, String giflCode, Location location)
			throws ParserConfigurationException, SystemException {
		PDFDocument doc = new PDFDocument();
    	Element rootElement = doc.createRootElement(PdfConstants.ROOT);
    	doc.appendTextNode(rootElement, PdfConstants.JH_LOGO_PATH, getUnmanagedImagePath(JHRPS_LOGO_FILE));
    	doc.appendTextNode(rootElement, PdfConstants.REPORT_AS_OF_DATE_LABEL, getReportAsOfDateLabel(location));
    	synchronized(Constants.dateFormat_MMMddyyyy){
    		doc.appendTextNode(rootElement, PdfConstants.REPORT_AS_OF_DATE,
    				Constants.dateFormat_MMMddyyyy.format(new Date()));
    	}
		doc.appendTextNode(rootElement, PdfConstants.REPORT_HEADER_TITLE1, getGIFLReportName(giflCode));
    	PdfHelper.convertIntoDOM(PdfConstants.REPORT_HEADER_TITLE2, rootElement, doc, getReportHeaderTitle1(location));
    	doc.appendTextNode(rootElement, PdfConstants.ALL_PAGE_FOOTER, getAllPageReportFooter1(location));
    	getExpenseRatioAndInvestmentCharges(doc, rootElement, giflReportDetails, selectedClassType, giflCode, location);
    	return doc.getDocument();
    }
    
    /*
     * This is used to add fund fee information to document
     * 
     * @param doc
     * @param rootElement
     * @param giflReportDetails
     * @param selectedClassType
     * @param giflCode 
     * @param location
     * 
     * @throws SystemException
     */
    private void getExpenseRatioAndInvestmentCharges(PDFDocument doc,Element rootElement,
    		GIFLReportDetails giflReportDetails, ClassType selectedClassType, String giflCode, Location  location)  throws SystemException{
    	Map<String, List<FundFeeVO>>  feeData = giflReportDetails.getFundFee();
    	List<String> fundFootnotes = new ArrayList<String>();
    	List<String> reportFootNotes = new ArrayList<String>();
    	Element investmentCharges = doc.createElement(PdfConstants.SECTION5);
    	// Report Description
    	String description = getReportAppendixIntro(location);
    	description = StringUtility.substituteParams(description,
				new Object[] { getGIFLReportName(giflCode) });
    	PdfHelper.convertIntoDOM(PdfConstants.DESCRIPTION, investmentCharges, doc, description);
    	PdfHelper.convertIntoDOM(PdfConstants.DESCRIPTION1, investmentCharges, doc, getReportAppendixDescription1(location));
    	PdfHelper.convertIntoDOM(PdfConstants.DESCRIPTION3, investmentCharges, doc, getReportAppendixDescription3(location));
    	PdfHelper.convertIntoDOM(PdfConstants.INVESTMENT_SUMMARY, investmentCharges, doc, getReportAppendixInvestmentSummary(location));
    	doc.appendTextNode(investmentCharges, PdfConstants.FUND_CLASS_LABEL, getReportAppendixFundClass(location));
    	doc.appendTextNode(investmentCharges, PdfConstants.FUND_CLASS_VALUE, selectedClassType.getClassLongName());
    	// Image path details
    	doc.appendTextNode(investmentCharges, PdfConstants.IMAGE_BLACK_BAR_ONE,
				getUnmanagedImagePath(IMAGE_BLACK_BAR_ONE));
		doc.appendTextNode(investmentCharges, PdfConstants.IMAGE_BLACK_BAR_TWO,
				getUnmanagedImagePath(IMAGE_BLACK_BAR_TWO));
		doc.appendTextNode(investmentCharges,  PdfConstants.IMAGE_BLACK_BAR_THREE, 
				getUnmanagedImagePath(IMAGE_BLACK_BAR_THREE));
		//Table Header elements
		Date effectiveDate = giflReportDetails.getEffectiveDate();
		synchronized(Constants.dateFormat_MMMddyyyy){
			doc.appendTextNode(investmentCharges, PdfConstants.REPORT_AS_OF_DATE,
					Constants.dateFormat_MMMddyyyy.format(effectiveDate));
		}
		boolean isNonClass1SignatureContract = true;
		if(StringUtils.equalsIgnoreCase(TYPE_ONE_CLASS, selectedClassType.getClassShortName())) {
			isNonClass1SignatureContract = false;
		}
		Element headerElement = doc.createElement(PdfConstants.TABLE_HEADER);
    	String[] headers = null;
    	if (isNonClass1SignatureContract) {
    		headers = new String[] { getReportAppendixFundCode(location),
    				getReportAppendixFundName(location),
    				getReportAppendixUnderlyingFund(location),
    				getReportAppendixFundCurrentASOf(location),
    				getReportAppendixInvestmentServices(location),
    				getReportAppendixUnderlyingFundNetCost(location),
    				getReportAppendixPlanServices(location),
    				getReportAppendixRevenueFromUnderlyingFund(location),  
    				getReportAppendixRevenueFromSubAccount(location),
    				getReportAppendixTotalRevenue(location),
    				getReportAppendixExpenseRatioPercentage(location),
    	} ;
    	}else {
    		headers = new String[] { getReportAppendixFundCode(location),
    				getReportAppendixFundName(location),
    				getReportAppendixUnderlyingFund(location),
    				getReportAppendixFundCurrentASOf(location),
    				getReportAppendixInvestmentServices(location),
    				getReportAppendixUnderlyingFundNetCost(location),
    				getReportAppendixPlanServices(location),
    				getReportAppendixRevenueFromUnderlyingFund(location),  
    				getReportAppendixRevenueFromSubAccount(location),
    				getReportAppendixTotalRevenue(location),
    				getReportAppendixExpenseRatioPercentage(location),
    	} ;
    	}
    	for (int i = 0; i < headers.length; i++) {
    		PdfHelper.convertIntoDOM(PdfConstants.TABLE_COLUMN.concat(String.valueOf((i + 1))), headerElement, doc,  headers[i]);
    	}
    	doc.appendElement(investmentCharges, headerElement);
		// feeData
		Element fundCategorys = doc.createElement(PdfConstants.FUND_CATEGORYS);
		Element classTypes = doc.createElement(PdfConstants.CLASS_TYPE);
		classTypes.setAttribute(PdfConstants.CLASS_TYPE_NAME, giflReportDetails.getInvestmentGroup().getGroupname());
		for (Entry<String, List<FundFeeVO>> macpac : feeData.entrySet()) {
 			String macpacId = macpac.getKey();
 			List<FundFeeVO> fundList = macpac.getValue();
 			//fund details
 			Element fundCategory = doc.createElement(PdfConstants.FUND_CATEGORY);
 			fundCategory.setAttribute(PdfConstants.FUND_CATEGORY_NAME, getGIFLMacPacName(macpacId));
 	    	Element fundsElement = doc.createElement(PdfConstants.FUNDS);
 			for (FundFeeVO fund : fundList) {
 				Element fundElement = doc.createElement(PdfConstants.FUND);
 	        	String fundCode = fund.getFundId();
 	        	String fundName = fund.getFundName();
 	        	String fundUnderlying = null;
 	        	String underlyingFundNetCost = null;
 	        	String revenueFromUnderlyingFund = null;
 	        	String revenueFromSubAccount = null;
 	        	String totalCost = null;
 	        	String expenseRatioPercentage = null;
				// enables flag to perform the ER Calculations for CL0.
				if (com.manulife.pension.service.fee.util.Constants.CONTRACT_CLASS0
						.equals(selectedClassType.getClassId())) {
					fund.setClassZero(true);
				}
 	        	// fund name with footnote marker
				String footnoteMarker = StringUtils.EMPTY;
				if(!fund.getFootNoteMarkers().isEmpty()) {
					for(String marker : fund.getFootNoteMarkers()){
						fundFootnotes.add(marker);
						footnoteMarker += marker;
					}
					footnoteMarker = "<sup>"+footnoteMarker+"</sup>";
				}
 	        	// fund code
 	        	doc.appendTextNode(fundElement, PdfConstants.FUND_CODE, fundCode);
 	        	PdfHelper.convertIntoDOM(PdfConstants.FUND_NAME, fundElement, doc, fundName + footnoteMarker);
				if (StringUtils.equals(
								com.manulife.pension.service.fee.util.Constants.FUND_TYPE_GARUNTEED,
								fund.getFundType())) {
					fundUnderlying = getReportAppendixSeeImportantInformationText(location);
					underlyingFundNetCost = getReportAppendixNotApplicableText(location);
					revenueFromUnderlyingFund = getReportAppendixNotApplicableText(location);
					revenueFromSubAccount = getReportAppendixNotApplicableText(location);
					totalCost = getReportAppendixNotApplicableText(location);
					expenseRatioPercentage = getReportAppendixNotApplicableText(location);
 	        	}else{
 	        		fundUnderlying = StringUtils.trimToEmpty(fund.getUnderLyingFundName());
 	        		underlyingFundNetCost = getFormatValue(fund.getUnderlyingFundNetCost());
 	        		revenueFromUnderlyingFund = getFormatValue(fund.getRevenueFromUnderlyingFund());
 	        		revenueFromSubAccount = getFormatValue(fund.getRevenueFromSubAccount());
					totalCost = getFormatValue(fund.getTotalRevenueUsedTowardsPlanCosts());
					expenseRatioPercentage = getFormatValue(fund.getExpenseRatio());
 	        	}
				doc.appendTextNode(fundElement, PdfConstants.FUND_UNDERLYING_NAME, fundUnderlying);
				Element informationServices = doc.createElement(PdfConstants.INVESTMENT_SERVICES);
 	        	doc.appendTextNode(informationServices, PdfConstants.UNDERLYING_FUND_NETCOST, underlyingFundNetCost);
 	        	doc.appendElement(fundElement, informationServices);
 	        	Element planServices = doc.createElement(PdfConstants.PLAN_SERVICES);
 	        	doc.appendTextNode(planServices, PdfConstants.REVENUE_FROM_UNDERLYING_FUND, revenueFromUnderlyingFund);
 	        	doc.appendTextNode(planServices, PdfConstants.REVENUE_FROM_SUBACCOUNT, revenueFromSubAccount);
 	        	doc.appendTextNode(planServices, PdfConstants.TOTAL_REVENUE, totalCost);
 	        	doc.appendElement(fundElement, planServices);
 	        	doc.appendTextNode(fundElement, PdfConstants.EXPENSE_RATIO_PER, expenseRatioPercentage);
 	        	doc.appendElement(fundsElement, fundElement);
 			}
 			doc.appendElement(fundCategory, fundsElement);
 			doc.appendElement(classTypes, fundCategory);
 		}
		doc.appendElement(fundCategorys, classTypes);
    	doc.appendElement(investmentCharges, fundCategorys);
    	doc.appendElement(rootElement, investmentCharges);
    	
    	Element footerInfo = doc.createElement(PdfConstants.FOOTER_INFO);
    	// Foot notes
    	Element footersElement = doc.createElement(PdfConstants.FOOTERS);
    	reportFootNotes.addAll(getFundFootnotes(fundFootnotes, location));
    	for (String footer : reportFootNotes) {
    		PdfHelper.convertIntoDOM(PdfConstants.FOOTER, footersElement, doc, footer);
    	}
    	doc.appendElement(footerInfo, footersElement);
    	// Copy rights
    	PdfHelper.convertIntoDOM(PdfConstants.DESCRIPTION1, footerInfo, doc, getReportImportantInfoLastDesc1(location));
    	doc.appendTextNode(footerInfo, PdfConstants.DESCRIPTION2, getReportImportantInfoLastDesc2(location));
    	doc.appendTextNode(footerInfo, PdfConstants.COPY_RIGHT, getReportImportantInfoCopyRights(location));
    	
		if (Location.NEW_YORK.equals(location)) {
			PdfHelper.convertIntoDOM(PdfConstants.DOCKET_NUMBER, footerInfo,
					doc, MessageFormat.format(getFooterFormNumber(location),selectedClassType.getClassId()));
			doc.appendTextNode(footerInfo, PdfConstants.GA_CODE, getFooterGACode(location));
			 
		} else {
			PdfHelper.convertIntoDOM(PdfConstants.DOCKET_NUMBER, footerInfo,
					doc, MessageFormat.format(getFooterFormNumber(location),selectedClassType.getClassId()));
			doc.appendTextNode(footerInfo, PdfConstants.GA_CODE, getFooterGACode(location));
		}
    	doc.appendElement(rootElement, footerInfo);
    }
    
    /*
     * This method is used to get foot notes
     * 
     * @param symbolsArrays
     * @param location
     * 
     * @return footnotesText
     * 
     * @throws SystemException
     */
    private List<String> getFundFootnotes(List<String> symbolsArrays, Location  location) throws SystemException {
    	ArrayList<Footnote> footnotes = new ArrayList<Footnote>();
    	ArrayList<String> footnotesText = new ArrayList<String>();
    	Content[] contents = null;
    	HashMap<String, Footnote> fundFootNotesTable = new HashMap<String, Footnote>();
    	
    	
    	try {
    		contents = ContentCacheManager.getInstance().getContentByType(ContentTypeManager.instance().FOOTNOTE);
		} catch (ContentException e) {
			throw new SystemException("Error getting footnote content");
		}
    	for (Content content : contents) {
    		fundFootNotesTable.put(((Footnote)content).getSymbol(),( Footnote) content);
		}
    	for(String symbol : symbolsArrays) {
			footnotes.add(fundFootNotesTable.get(symbol));
		}
    	
    	Collections.sort(footnotes , new Comparator<Footnote>() {
			public int compare(Footnote f1, Footnote f2) {
				if ( f1.getOrderNumber() < f2.getOrderNumber() )
					return -1;
				else if ( f1.getOrderNumber() > f2.getOrderNumber() )	
					return 1;
					
				return 0;
			}
		});
    	
		for (Footnote footnote : footnotes) {
			String text = null;
			if (Location.NEW_YORK.equals(location)) {
				text = footnote.getNyText();
				if (text != null) {
					footnotesText.add(formatFundFootnotes(footnote.getSymbol(),
							text));
				} else {
					text = footnote.getText();
					footnotesText.add(formatFundFootnotes(footnote.getSymbol(),
							text));
				}
			} else {
				text = footnote.getText();
				footnotesText.add(formatFundFootnotes(footnote.getSymbol(),
						text));
			}
		}
    	
    	return footnotesText;
    }
    
    /*
	 * returns text enclosed in super tag
	 * 
	 * @param marker
	 * @param text
	 * 
	 * @return String
	 */
	private String formatFundFootnotes(String marker, String text) {
		return new StringBuilder().append("<sup>")
		                          .append(marker)
		                          .append("</sup>")
		                          .append(text).toString();
	}
    
    private String getFormatValue(BigDecimal value){
    	return NumberRender.formatByPattern(value, PdfConstants.DECIMAL_ZERO,
    			PdfConstants.FORMAT_TWO_DECIMALS, 2,
				BigDecimal.ROUND_HALF_UP);
    }
    
	private String getReportAsOfDateLabel(Location  location){
		return getContent(ContentConstants.GIFL_AS_OF_DATE_LABEL_KEY,location);
	}
	
	private String getReportHeaderTitle1(Location  location){
		return getContent(ContentConstants.GIFL_HEADER_TITLE1_KEY, location);
	}
	
	private String getAllPageReportFooter1(Location  location){
		return getContent(ContentConstants.GIFL_ALL_PAGE_FOOTER_KEY, location);
	}
	
	private String getReportAppendixIntro(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_INTRO, location);
	}
	
	private String getReportAppendixDescription1(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_DESCRIPTION_KEY_1, location);
	}
	
	private String getReportAppendixDescription3(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_DESCRIPTION_KEY_3, location);
	}
	
	private String getReportAppendixInvestmentSummary(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_INVESTMENT_SUMMARY_KEY, location);
	}
	
	public String getReportAppendixFundClass(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FUND_CLASS_KEY, location);
	}
	
	private String getReportAppendixFundCode(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FUND_CODE_KEY, location);
	}
	
	private String getReportAppendixFundName(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FUND_NAME_KEY, location);
	}
	
	/**
	 * To get the CMA Key 86611
	 * @param location
	 * @return Investment Services
	 */
	private String getReportAppendixInvestmentServices(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_INVESTMENT_SERVICES_KEY, location);
	}
	
	/**
	 * To get the CMA Key 76818
	 * @param location
	 * @return Underlying Fund Net Cost (%)
	 */
	private String getReportAppendixUnderlyingFundNetCost(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_UNDERLYING_FUND_NETCOST_KEY, location);
	}
	
	/**
	 * To get the CMA Key 86612
	 * @param location
	 * @return Plan Services
	 */
	private String getReportAppendixPlanServices(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_PLAN_SERVICES_KEY, location);
	}
	
	/**
	 * To get the CMA Key 76819
	 * @param location
	 * @return Revenue from Underlying Fund (%) (12b1, STA, Other)
	 */
	private String getReportAppendixRevenueFromUnderlyingFund(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_REVENUE_FROM_UNDERLYING_FUND_KEY, location);
	}

	/**
	 * To get the CMA Key 76820
	 * @param location
	 * @return Revenue From Sub-account (%)
	 */
	private String getReportAppendixRevenueFromSubAccount(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_REVENUE_FROM_SUBACCOUNT_KEY, location);
	}
	
	/**
	 * To get the CMA Key 86613
	 * @param location
	 * @return Total Revenue Used Towards Plan Cost (%)
	 */
	private String getReportAppendixTotalRevenue(Location location){
		return getContent(ContentConstants.GIFL_APPENDIX_TOTAL_REVENUE_KEY, location);
	}
	
	/**
	 * To get the CMA Key 76816
	 * @param location
	 * @return Underlying fund
	 */
	private String getReportAppendixUnderlyingFund(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FUND_UNDERLYING_KEY, location);
	}
	
	/**
	 * To get the CMA Key 76805
	 * @param location
	 * @return Information is current as of
	 */
	private String getReportAppendixFundCurrentASOf(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FUND_CURRENT_AS_OF_KEY, location);
	}
	
	/**
	 * To get the CMA Key 76822
	 * @param location
	 * @return Expense Ratio (%)
	 */
	private String getReportAppendixExpenseRatioPercentage(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_EXPENSE_RATIO_PER_KEY, location);
	}
	
	private String getReportAppendixNotApplicableText(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_DESCRIPTION1_NOT_APPLICABLE, location);
	}
	
	private String getReportAppendixSeeImportantInformationText(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_DESCRIPTION1_SEE_IMPORTANT_INFO, location);
	}
	
	private String getReportImportantInfoCopyRights(Location  location){
		return getContent(ContentConstants.GIFL_IMP_INFO_COPY_RIGHT_KEY, location);
	}
	
	private String getReportImportantInfoLastDesc1(Location  location){
		return getContent(ContentConstants.GIFL_IMP_INFO_LAST_DESC1_KEY, location);
	}
	
	private String getReportImportantInfoLastDesc2(Location  location){
		return getContent(ContentConstants.GIFL_IMP_INFO_LAST_DESC2_KEY, location);
	}
	
	private String getFooterFormNumber(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FOOTER_FORM_NUMBER, location);
	}
	
	private String getFooterGACode(Location  location){
		return getContent(ContentConstants.GIFL_APPENDIX_FOOTER_GA_CODE, location);
	}
    
    /**
	 * This method is used to return the xml 
	 * document string for the pdf.
	 * 
     * @param doc 
	 * 
	 * @return writer xml document string.
	 */
	protected String toXml(Document doc) {
		
		logger.debug("enter -> toXml()");
		
		DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
        
        writer.flush();
        writer.toString();
			
		logger.debug("exit <- toXml()");
		
		return writer.toString();
		
	}
	
	/**
	 * This is used to get Long name for GIFL Version  
	 * @param giflVersion
	 * 
	 * @return giflVersionDesc
	 * 
	 * @throws SystemException
	 */
	protected String getGIFLReportName(String giflVersion) throws SystemException {
		
		Map<String, String> giflVersionMap = EnvironmentServiceDelegate
				.getInstance(Environment.getInstance().getAppId()).getGIFLVersionNames();
		String giflVersionDesc = null;
		if (giflVersionMap != null && !giflVersionMap.isEmpty()) {
			giflVersionDesc = giflVersionMap.get(giflVersion);
		}

		// if the returned giflVersionDesc is null, return giflVersion
		if (giflVersionDesc == null) {
			giflVersionDesc = giflVersion;
		}
		
		return giflVersionDesc;
	}
	

	/**
	 * This is used to get name for MACPAC code 
	 * 
	 * @param macPacCode
	 * 
	 * @return giflMacPacName
	 * 
	 * @throws SystemException
	 */
	protected String getGIFLMacPacName(String macPacCode) throws SystemException {

		Map<String, String> giflMacPacMap = EnvironmentServiceDelegate
				.getInstance(Environment.getInstance().getAppId()) .getMacpacDescriptions();
		String giflMacPacName = null;
		if (giflMacPacMap != null && !giflMacPacMap.isEmpty()) {
			giflMacPacName = giflMacPacMap.get(macPacCode.trim());
		}

		// if the returned giflMacPacName is null, return macPacCode
		if (giflMacPacName == null) {
			giflMacPacName = macPacCode;
		}

		return giflMacPacName;
	}

}
