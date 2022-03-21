package com.manulife.pension.bd.web.bob.investment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
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

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.report.BDPdfHelper;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.service.account.AdhocRORException;
import com.manulife.pension.service.account.SystemUnavailableException;
import com.manulife.pension.service.account.valueobject.AdhocROROutput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ParticipantListVO;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"rateOfReturnCalculatorForm"})

public class RateOfReturnCalculatorDefinedBenifitController extends BaseAutoController {
	@ModelAttribute("rateOfReturnCalculatorForm") 
	public RateOfReturnCalculatorForm populateForm() 
	{
		return new RateOfReturnCalculatorForm();
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/rorCalculator_db.jsp");
		forwards.put("default","/contract/rorCalculator_db.jsp");
		forwards.put("calculate","/contract/rorCalculator_db.jsp");
		forwards.put("reset","/contract/rorCalculator_db.jsp");
		forwards.put("back","redirect:/do/bob/contract/contractInformation");
		}

    public static final String CALCULATE_ADHOC_ROR_FORM = "rateOfReturnCalculatorForm";

    public static final String CALCULATE_BUTTON = "Calculate";

    public static final String DEFAULT = "default";

    public static final String CALCULATE = "calculate";

    public static final String BACK_BUTTON = "Back";

    public static final String BACK = "back";

    public static final String RESET_BUTTON = "Reset";

    public static final String RESET = "reset";

    public static final String HOME_BUTTON = "Home";

    public static final String HOME = "home";
    
    public static final String PRINT_PDF_ICON = "printPDFReport";

    public static final String PRINT_PDF = "printPDFReport";

    public static final String EMPTY = StringUtils.EMPTY;

    public static final int DATE_VALID = 0;

    public static final int DATE_INVALID = -1;

    public static final int DATE_EMPTY = -2;

    public static final int DAY_INVALID = 1;

    public static final int MONTH_INVALID = 2;

    public static final int YEAR_INVALID_MIN = 4;

    public static final int YEAR_INVALID = 5;

    public static final int JANUARY_MAX = 31;

    public static final int FEBRUARY_MAX = 28;

    public static final int FEBRUARY_MAXLEAP = 29;

    public static final int MARCH_MAX = 31;

    public static final int APRIL_MAX = 30;

    public static final int MAY_MAX = 31;

    public static final int JUNE_MAX = 30;

    public static final int JULY_MAX = 31;

    public static final int AUGUST_MAX = 31;

    public static final int SEPTEMBER_MAX = 30;

    public static final int OCTOBER_MAX = 31;

    public static final int NOVEMBER_MAX = 30;

    public static final int DECEMBER_MAX = 31;

    /* constants for sanity check */
    public static final int MIN_YEAR = 1950;

    public static final int MAX_YEAR = 3000;
    
    private static final String XSLT_FILE_KEY_NAME = "RateOfReturnCalculator.XSLFile";
    
    public static final String COMMA = ",";

    public RateOfReturnCalculatorDefinedBenifitController() {
        super(RateOfReturnCalculatorDefinedBenifitController.class);
    }

    @RequestMapping(value ="/contract/RORCalculatorDb/", method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

    	if(bindingResult.hasErrors()){
			
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
				//return forwards.get("definedBenefitAccount");//if input forward not //available, provided default
			}
		}
    	
    	RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm)actionForm;
    	BobContext bob = BDSessionHelper.getBobContext(request) ;
        String profileId = null;
        if ("DB06".equalsIgnoreCase(bob.getContractProfile().getContract()
				.getProductId())
				|| "DBNY06".equalsIgnoreCase(bob.getContractProfile()
						.getContract().getProductId())) {

			Contract contract = bob.getCurrentContract();
			if (contract != null) {
				ParticipantListVO participantList = ContractServiceDelegate
						.getInstance().getParticipantList(
								contract.getContractNumber(), "1");
				ArrayList<ParticipantVO> list = (ArrayList<ParticipantVO>) participantList
						.getParticipants();

				profileId = list.get(0).getProfileId();
			}
		} else {
			profileId = bob.getPptProfileId();
		}
        
        //Reset Form on page load
        rateOfReturnCalculatorForm.reset( request);
        rateOfReturnCalculatorForm.setRateOfReturn("");
        rateOfReturnCalculatorForm.setResultsCalculatedFlag(false);
        
        ParticipantServiceDelegate participantServiceDelegate = ParticipantServiceDelegate.getInstance(); 
        ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
        
        int participantId = participantServiceDelegate.getParticipantIdByProfileId(Long.parseLong(profileId), bob.getCurrentContract().getContractNumber());
        
        String [] pptName = contractServiceDelegate.getParticipantFirstAndLastName(participantId);
    	
        String contractName = contractServiceDelegate.getContractName(bob.getCurrentContract().getContractNumber());
        
        rateOfReturnCalculatorForm.setParticipantFirstName(pptName[0].trim());
    	rateOfReturnCalculatorForm.setParticipantLastName(pptName[1].trim());
    	rateOfReturnCalculatorForm.setContractName(contractName);
        // TODO Auto-generated method stub
        return forwards.get(DEFAULT);
    }
    @RequestMapping(value ="/contract/RORCalculatorDb/", params={"action=reset"} , method =  {RequestMethod.POST}) 
    public String doReset(@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
   
    	if(bindingResult.hasErrors()){
			
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
				//return forwards.get("definedBenefitAccount");//if input forward not //available, provided default
			}
		}
        HttpSession session = request.getSession(false);
        session.removeAttribute(CALCULATE_ADHOC_ROR_FORM);
       
        actionForm.reset( request);
        actionForm.setRateOfReturn("");
        actionForm.setResultsCalculatedFlag(false);
        return forwards.get(RESET);
    }
    @RequestMapping(value ="/contract/RORCalculatorDb/", params={"action=calculate"} , method =  {RequestMethod.POST}) 
    public String doCalculate(@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
   
		if(bindingResult.hasErrors()){
					
					String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
					if(errDirect!=null){
						request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
						return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
						
					}
				}
    	
      
        HttpSession session = request.getSession(false);
        String forward = null;

        float rs = 0;

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

        // get the form

        actionForm.setResultsCalculatedFlag(false);

        //UserProfile userProfile = getUserProfile(request);
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        // handle cacluate function

        if (RateOfReturnCalculatorForm.TIME_PERIOD_FROM_DATE_RANGE
                .equals(actionForm.getTimePeriodFromToday())) {

            // user entered dates
            startDate = new GregorianCalendar(Integer.parseInt(actionForm
                    .getStartYear()),
                    Integer.parseInt(actionForm.getStartMonth()) - 1,
                    Integer.parseInt(actionForm.getStartDay()));
            endDate = new GregorianCalendar(Integer.parseInt(actionForm
                    .getEndYear()), Integer.parseInt(actionForm.getEndMonth()) - 1,
                    Integer.parseInt(actionForm.getEndDay()));

        } else {
            // end date is today
            endDate = new GregorianCalendar();
            endDate.add(Calendar.DAY_OF_YEAR, -1);
            // calculate start date
            if (RateOfReturnCalculatorForm.TIME_PERIODS_REQUIRE_CALCULATION
                    .contains(actionForm.getTimePeriodFromToday())) {

                // start date is 1 month, 3 month ...from today
                startDate.setTime(addMonth(
                        Integer.parseInt(actionForm.getTimePeriodFromToday())
                                * (-1), new GregorianCalendar().getTime()));
            } else {
                // must be YTD so make start date jan 1 of this year
                startDate.set(endDate.get(Calendar.YEAR), 0, 1);
            }
        }

        // setup result dates for form

        actionForm.setResultStartDate(df.format(startDate.getTime()));
        actionForm.setResultEndDate(df.format(endDate.getTime()));

        try {
        	
            AccountServiceDelegate accountService = AccountServiceDelegate.getInstance();
            
            BobContext bob = BDSessionHelper.getBobContext(request) ;
            String profileId = null;
			if ("DB06".equalsIgnoreCase(bob.getContractProfile().getContract()
					.getProductId())
					|| "DBNY06".equalsIgnoreCase(bob.getContractProfile()
							.getContract().getProductId())) {

				Contract contract = bob.getCurrentContract();
				if (contract != null) {
					ParticipantListVO participantList = ContractServiceDelegate
							.getInstance().getParticipantList(
									contract.getContractNumber(), "1");
					ArrayList<ParticipantVO> list = (ArrayList<ParticipantVO>) participantList
							.getParticipants();

					profileId = list.get(0).getProfileId();
				}
			} else {
				profileId = bob.getPptProfileId();
			}
        		
            String contractNumber = Integer.toString(bob.getCurrentContract().getContractNumber());
            
            AdhocROROutput arOutput = accountService.executeAdhocRORTransaction(null,
                    startDate.getTime(), endDate.getTime(), profileId, contractNumber);

            int returnCode = arOutput.getErrorCode();

            if (returnCode == 4702) {
                processError(request, "***ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY***" + returnCode,
                        BDErrorCodes.ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY);
            } else if (returnCode == 4703) {
                processError(request,
                        "***ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END***"
                                + returnCode,
                        BDErrorCodes.ERROR_CALCADHOCROR_MF_ROR_FOR_ONE_DAY_WITH_CASH_FLOW_AT_END);
            } else if (returnCode == 4704) {
                processError(request, "***ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS***"
                        + returnCode,
                        BDErrorCodes.ERROR_CALCADHOCROR_MF_CLOSING_BALANCE_THREE_DOLLARS);
            } else {

                BigDecimal rateOfReturn = arOutput.getRateOfReturn();
                if (rateOfReturn != null) {

                    rs = rateOfReturn.floatValue();
                    actionForm.setResultsCalculatedFlag(true);

                    actionForm.setRateOfReturn(String.valueOf(rs));

                    // handle special case where warning message should show
                    // just after ROR result
                    if (returnCode == 3404) {
                    	actionForm.setWarningMessageFlag(true);
                    }
                } else {

                    String systemOutMsg = null;
                    String errorMsg = arOutput.getErrorMessage();
                    if ((returnCode != 0) && (errorMsg != null)) {
                        systemOutMsg = "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***" + " "
                                + String.valueOf(returnCode) + " " + errorMsg;
                    } else {
                        systemOutMsg = "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***";
                    }

                    processError(request, systemOutMsg,
                    		BDErrorCodes.ERROR_CALCADHOCROR_MF_GENERAL_ERROR);
                }
            }

        } catch (AdhocRORException e) {
            processError(request, "***ERROR_CALCADHOCROR_MF_GENERAL_ERROR***",
            		BDErrorCodes.ERROR_CALCADHOCROR_MF_GENERAL_ERROR);

        } catch (SystemUnavailableException e) {
            processError(request, "***ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS***",
            		BDErrorCodes.ERROR_ACCOUNT_BALANCE_OUTSIDE_SERVICE_HOURS);

        } catch (SystemException e) {
            processError(request, "***SYSTEM_UNAVAILABLE***", BDErrorCodes.SYSTEM_UNAVAILABLE);
        }

        forward = forwards.get(CALCULATE);

        return forward;
    }

    /**
     * This method validates the parameters' values
     * 
     * @param mapping ActionMapping object
     * 
     * @param request HttpServletRequest object
     * 
     * @return Returns the ActionErrors object
     */
   @Autowired
   private RateOfReturnCalculatorDefinedBenifitValidator rateOfReturnCalculatorDefinedBenifitValidator;
    
    /**
	 * This method is used to generate interim review report
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 */
   
   @RequestMapping(value ="/contract/RORCalculatorDb/", params={"action=printPDFReport"} , method =  {RequestMethod.GET}) 
   public String doPrintPDFReport(@Valid @ModelAttribute("rateOfReturnCalculatorForm") RateOfReturnCalculatorForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   throws IOException,ServletException, SystemException {
	
	   if(bindingResult.hasErrors()){
			
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
				//return forwards.get("definedBenefitAccount");//if input forward not //available, provided default
			}
		}

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }
        
        System.out.println("Test");
        //doCommon(mapping, reportForm, request, response);
        ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);

        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(actionForm, report, request);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline");
        response.setContentLength(pdfOutStream.size());

        try {
            ServletOutputStream sos = response.getOutputStream();
            pdfOutStream.writeTo(sos);
            sos.flush();
        } catch (IOException ioException) {
            throw new SystemException(ioException, "Exception writing pdfData.");
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException ioException) {
                throw new SystemException(ioException, "Exception writing pdfData.");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting doPrintPDF");
        }
        return null;
    }
	
	
	
	/**
     * This method will generate the PDF and return a ByteArrayOutputStream which will be sent back to
     * the user.
     * This method would:
     * - Create the XML-String from VO.
     * - Create the PDF using the created XML-String and XSLT file.
     * @throws ContentException
     */
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(AutoForm form,
            ReportData report, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }
        
        RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) form;
        
        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {

            Object xmlTree = prepareXMLFromReport(form, report, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null || !rateOfReturnCalculatorForm.getResultsCalculatedFlag()) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document)xmlTree, xsltfile, configfile, includedXSLPath);
            }

        } catch (Exception exception) {
            String message = null;
            if (exception instanceof ContentException) {
                message = "Error occured while retrieveing CMA Content during PDF creation.";
            } else if (exception instanceof ParserConfigurationException) {
                message = "Error occured while creating Document object during PDF creation.";
            } else if (exception instanceof FOPException || exception instanceof TransformerException ||
                    exception instanceof IOException) {
                message = "Error occured during PDF generation.";
            } else {
                message = "Error occured during PDF generation.";
            }

            throw new SystemException(exception, message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting prepareXMLandGeneratePDF");
        }
        return pdfOutStream;
    }
    
    /**
	 * 
	 * @param rateOfReturnCalculatorForm
	 * @param investmentCostReportData
	 * @param request
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
    @SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(AutoForm form, ReportData investmentCostReportData, HttpServletRequest request)
			throws ParserConfigurationException, TransformerException {
    	
    	PDFDocument doc = new PDFDocument();
        String contentText;
        
        RateOfReturnCalculatorForm rateOfReturnCalculatorForm = (RateOfReturnCalculatorForm) form;

        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.RATE_OF_RETURN_CALCULATOR_DB_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.RATE_OF_RETURN_CALCULATOR);
        
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        doc.appendTextNode(rootElement, PdfConstants.FIRST_NAME, rateOfReturnCalculatorForm.getParticipantFirstName());
        doc.appendTextNode(rootElement, PdfConstants.LAST_NAME, rateOfReturnCalculatorForm.getParticipantLastName());
        doc.appendTextNode(rootElement, PdfConstants.CONTRACT_NAME, rateOfReturnCalculatorForm.getContractName());
        
        String productId = BDSessionHelper.getBobContext(request).getContractProfile().getContract().getProductId();
        doc.appendTextNode(rootElement, PdfConstants.PRODUCT_ID, productId);
        
        String bodyHeader1 = ContentUtility.getContentAttributeText(layoutPageBean,
                BDContentConstants.BODY1_HEADER, null);
        PdfHelper.convertIntoDOM(BDPdfConstants.BODY_HEADER1, rootElement, doc, bodyHeader1);
        
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        Date asOfDate = currentContract.getContractDates().getAsOfDate();
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, DateRender.formatByPattern(
                asOfDate, null, RenderConstants.MEDIUM_MDY_SLASHED));
        
        doc.appendTextNode(rootElement, "startDate", rateOfReturnCalculatorForm.getResultStartDate());
        doc.appendTextNode(rootElement, "endDate", rateOfReturnCalculatorForm.getResultEndDate());
        doc.appendTextNode(rootElement, "rateOfReturn", rateOfReturnCalculatorForm.getRateOfReturn());
        
        // Sets footer, footnotes and disclaimer
        setFooterXMLElements(layoutPageBean, doc, rootElement, request);
        
        // Read XML Output
        try
        {
           DOMSource domSource = new DOMSource(doc.getDocument());
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
        
        return doc.getDocument();
    }
    
    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets Logo, PathName, Introduction-1 & 2 XML elements common for reports.
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setIntroXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, HttpServletRequest request) {
        Contract currentContract = BDSessionHelper.getBobContext(request).getCurrentContract();
        PdfHelper.setIntroXMLElements(layoutPageBean, doc, rootElement, currentContract);
    }
    
    /**
     * This method is used by PDF Generation functionality.
     * 
     * This sets footer, footnotes and disclaimer XML elements common for reports
     * 
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setFooterXMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement, HttpServletRequest request, String[]... params) {
        Location location = ApplicationHelper.getRequestContentLocation(request);
        BDPdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                BDContentConstants.BD_GLOBAL_DISCLOSURE, params);

    }

    /**
     * Empty and Null check for the date fields
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int isValidDate(String year, String month, String day) {
        if ((year == null && month == null && day == null)
                || (year.trim().length() == 0 && month.trim().length() == 0 && day.trim().length() == 0)) {
            return DATE_EMPTY;
        }
        try {
            return isValidDateRange(Integer.parseInt(year), Integer.parseInt(month),
                    Integer.parseInt(day));
        } catch (Exception e) {
            return DATE_INVALID;
        }

    }

    /**
     * Month and year validation
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int isValidDateRange(int year, int month, int day) {
        int valid = DATE_VALID;

        GregorianCalendar today = new GregorianCalendar();

        /* sanity check. detailed check should be done separately */
        if (year < MIN_YEAR || year > MAX_YEAR) {
            valid = valid | YEAR_INVALID;
        }
        switch (month) {
            case 1:
                if (day > JANUARY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 2:
                if (today.isLeapYear(year)) {
                    if (day > FEBRUARY_MAXLEAP)
                        valid = valid | DAY_INVALID;
                } else {
                    if (day > FEBRUARY_MAX)
                        valid = valid | DAY_INVALID;
                }
                break;
            case 3:
                if (day > MARCH_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 4:
                if (day > APRIL_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 5:
                if (day > MAY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 6:
                if (day > JUNE_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 7:
                if (day > JULY_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 8:
                if (day > AUGUST_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 9:
                if (day > SEPTEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 10:
                if (day > OCTOBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 11:
                if (day > NOVEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            case 12:
                if (day > DECEMBER_MAX)
                    valid = valid | DAY_INVALID;
                break;
            default:
                valid = valid | MONTH_INVALID;
        }

        return valid;
    }

    /**
     * Utility method for calculating number of months in range of selection
     * 
     * @param months
     * @param date
     * @return
     */
    public static Date addMonth(int months, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * Processing errors
     * 
     * @param request
     * @param systemOutMsg
     * @param errorCode
     */
    @SuppressWarnings("unchecked")
    protected void processError(HttpServletRequest request, String systemOutMsg, int errorCode) {
        ArrayList errors = new ArrayList();

        request.getSession().setAttribute(BdBaseController.ERROR_KEY, errors);
        errors.add(new GenericException(errorCode));
    }
    
    public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}
    
    /**
    * This method would return the key present in ReportsXSL.properties file.
    * This key has the value as path to FOP Configuration file.
    *
    * @return String
    */
   protected String getFOPConfigFileName() {
       return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
   }
   
   /**
    * This method will get the max word size from a given existingMaxWordSize and the maximum word
    * size found in the title.
    * 
    * @param existingMaxWordSize - max word size already found during previous calls to
    *            findWordWithMaxSize() method.
    * @param title - The String in which we are trying to find the maximum word size.
    * @return - the Max Integer of "existingMaxWordSize" and the max word size found in "title"
    */
   public Integer findWordWithMaxSize(Integer existingMaxWordSize, String title) {
       int maxSize = 0;
       if (existingMaxWordSize != null) {
           maxSize = existingMaxWordSize;
       }

       String[] individualWords = StringUtils.split(title, BDConstants.SINGLE_SPACE_SYMBOL);
       if (individualWords != null && individualWords.length > 0) {
           for (String word : individualWords) {
               if (word.length() > maxSize) {
                   maxSize = word.length();
               }
           }
       }
       return maxSize;
   }
  
   /**
    * This method returns the # of rows where the PDF will be capped.
    * @return Integer
    */
   protected Integer getMaxCappedRowsInPDF() {
       return Integer.valueOf(ReportsXSLProperties.get(CommonConstants.MAX_CAPPED_ROWS_IN_PDF));
   }
   
   /**
    * This method is used by PDF Generation functionality.
    *
    * This sets the Logo, Path Name XML elements.
    *
    * @param layoutPageBean
    * @param doc
    * @param rootElement
    * @param request
    */
   protected void setLogoAndPageName(LayoutPage layoutPageBean, PDFDocument doc,
           Element rootElement) {
       PdfHelper.setLogoAndPageName(layoutPageBean, doc, rootElement);
   }
   
   /**
    * This method gets layout page for the given layout id.
    * 
    * @param path
    * @return LayoutPage
    */
   protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
       BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext()).getLayoutBean(id, request);
       LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
       return layoutPageBean;
   }
   
   @Autowired
   private BDValidatorFWInput  bdValidatorFWInput;
   @InitBinder
   protected void initBinder(HttpServletRequest request,
   			ServletRequestDataBinder  binder) {
   	binder.bind(request);
    	binder.addValidators(rateOfReturnCalculatorDefinedBenifitValidator);
    	binder.addValidators(bdValidatorFWInput);
   }

}
